/**
 * 
 */
package jscenegraph.port;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbIntList;

/**
 * @author Yves Boyadjian
 *
 */
public class FILE {
	
	public static final int EOF = -1;
	private static final int BUFFER_SIZE = 1 << 20;
	/* Seek method constants */

	public static final int SEEK_CUR    =1;
	public static final int SEEK_END    =2;
	public static final int SEEK_SET    =0;

	
	PushbackInputStream in;
	
	SbListInt rl = new SbListInt();
	
	long position_indicator;

	public FILE(InputStream in) {
		this.in = new PushbackInputStream(new BufferedInputStream(in, BUFFER_SIZE));
	}

	public FILE(InputStream in, long length) {
		this.in = new PushbackInputStream(new BufferedInputStream(in, BUFFER_SIZE),(int)length);
	}

	public static int fclose(FILE fp) {
		if(fp.in != null) {
			try {
				fp.in.close();
				return 0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Java port
	 * @param fileName
	 * @param options
	 * @return
	 */
	public static FILE fopen(String fileName, String options) {
		FileSystem fileSystem = FileSystems.getDefault();
		Path fileNamePath = fileSystem.getPath(fileName);
		return fopen(fileNamePath,options);
	}

	public static FILE fopen(Path fileNamePath, String options) {
	    OpenOption option = null;
	    switch(options) {
	    case "r":
	    case "rb":
	    	option = StandardOpenOption.READ;
	    	break;
	    }
	    
	    try {
			InputStream inputStream = Files.newInputStream(fileNamePath, option);
			GZIPInputStream gzip;
			try {
				gzip = new GZIPInputStream(inputStream);
			} catch(ZipException e) {
				// not a gzip file
				gzip = null;
			}
			if ( gzip != null) {
				inputStream = gzip;
			}
			else {
				inputStream = Files.newInputStream(fileNamePath, option);
			}

			long length;
			try {
				File file = fileNamePath.toFile();
				length = fileNamePath.toFile().length();
			}catch (UnsupportedOperationException e) {
				length = BUFFER_SIZE;
			}

			return new FILE(inputStream, length);
		} catch (IOException e) {
			return null;
		}
	}

	public InputStream getInputStream() {
		return in;
	}

	public static void ungetc(char c, FILE fp) {
		try {
			fp.in.unread(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getc(FILE fp) {
		try {
			return fp.in.read();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int feof(FILE fp) {
		try {
			int nextByte = fp.in.read();
			if(nextByte == -1) {
				return -1;
			}
			else {
				fp.in.unread(nextByte);
				return 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int fscanf(FILE fp, String string, double[] d) {
		try {
		switch(string) {
		case "%lf":
			StringBuilder chars = new StringBuilder();
			boolean isSpace = false;
			do {
					int c = fp.in.read();
				if( c == -1 || Character.isSpace((char)c) || c == ',' || c == '}' || c == ']' ) {
					isSpace = true;
					if( c != -1) {
						fp.in.unread(c);
					}
				}
				else {
					chars.append((char)c);
				}
			} while(!isSpace);
			d[0] = Double.parseDouble(chars.toString());
			return 1;
			default:
				return 0;
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EOF;
	}

	public static long fread(char[] cc, int sizeof, int length, FILE fp) {
		
		int i;
		for(i=0;i<length*sizeof;i++) {
			try {
				int b1;
				b1 = fp.in.read();
				if(b1 == -1) break;
				cc[i] = (char)b1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		return i/sizeof;
	}

	public static int fread(byte[] c, int sizeof, int length, FILE fp) {

		int i;
		for(i=0;i<length*sizeof;i++) {
			try {
				int b1;
				b1 = fp.in.read();
				if(b1 == -1) break;
				c[i] = (byte)b1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return i/sizeof;
	}

	public static int fread(int[] c, int sizeof, int length, FILE fp) {
		
		if(sizeof != Integer.BYTES) {
			throw new IllegalArgumentException();
		}
		
		int i;
		for(i=0; i<length;i++) {
			try {
				int b1;
				b1 = fp.in.read();
				if(b1 == -1) break;
				int b2 = fp.in.read();
				if(b2 == -1) break;
				int b3 = fp.in.read();
				if(b3 == -1) break;
				int b4 = fp.in.read();
				if(b4 == -1) break;
				byte[] bytes = new byte[4];
				bytes[0] = (byte)b1;
				bytes[1] = (byte)b2;
				bytes[2] = (byte)b3;
				bytes[3] = (byte)b4;
				ByteBuffer wrapped = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
				c[i] = wrapped.getInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	public static int sscanf(String s, String format, double[] arg) {
		// TODO Auto-generated method stub
		if(Objects.equals(format,"%lf")) {
			try {
				arg[0] = Double.parseDouble(s);
				return 1;
			}
			catch(NumberFormatException e) {
				return FILE.EOF;
			}
		}
		return FILE.EOF;
	}

	public static int fseek(
		    FILE _Stream,
		    long  _Offset,
		    int   _Origin
		    ) {
		if(_Origin == FILE.SEEK_END && _Offset == 0) {
			try {
				int r;
				do {
					r = _Stream.in.read();
					_Stream.rl.append(r);
				} while( r != -1);
				_Stream.rl.truncate(_Stream.rl.getLength()-1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		else if(_Origin == FILE.SEEK_SET) {
			int pos = ftell(_Stream);
			if(pos > _Offset) {
				long back = pos - _Offset;
				for(int i=0;i<back;i++) {
					int b = _Stream.rl.operator_square_bracket(_Stream.rl.getLength()-1);
					try {
						_Stream.in.unread(b);
						_Stream.rl.truncate(_Stream.rl.getLength()-1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return 0;
			}
			else if ( pos == _Offset) {
				return 0;
			}
		}
		return -1; // TODO
	}

	public static int ftell(
		    FILE _Stream
		    ) {
		if(_Stream.rl.getLength()==0) {
			return 0;
		}
		int delta = 0;
		if(_Stream.rl.operator_square_bracket(_Stream.rl.getLength()-1)== -1) {
			delta = -1;
		}
		return _Stream.rl.getLength() + delta;
	}

	public static int fwrite(byte[] tmp, int i, int j, FILE fp) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
