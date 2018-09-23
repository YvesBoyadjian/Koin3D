/**
 * 
 */
package jscenegraph.port;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author Yves Boyadjian
 *
 */
public class FILE {
	
	public static final int EOF = -1;
	private static final int BUFFER_SIZE = 1 << 10;
	/* Seek method constants */

	public static final int SEEK_CUR    =1;
	public static final int SEEK_END    =2;
	public static final int SEEK_SET    =0;

	
	PushbackInputStream in;

	public FILE(InputStream in) {
		this.in = new PushbackInputStream(new BufferedInputStream(in, BUFFER_SIZE));
	}

	public static void fclose(FILE fp) {
		if(fp.in != null) {
			try {
				fp.in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Java port
	 * @param fileName
	 * @param string
	 * @return
	 */
	public static FILE fopen(String fileName, String options) {
	    FileSystem fileSystem = FileSystems.getDefault();
	    Path fileNamePath = fileSystem.getPath(fileName);
	    OpenOption option = null;
	    switch(options) {
	    case "r":
	    case "rb":
	    	option = StandardOpenOption.READ;
	    	break;
	    }
	    
	    try {
			InputStream inputStream = Files.newInputStream(fileNamePath, option);
			return new FILE(inputStream);
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
			// TODO Auto-generated catch block
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
			StringBuffer chars = new StringBuffer();
			boolean isSpace = false;
			do {
					int c = fp.in.read();
				if( c == -1 || Character.isSpace((char)c) || c == ',') {
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

	public static long fread(char[] c, int i, int j, FILE fp) {
		// TODO Auto-generated method stub
		return 0;
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

	public static int sscanf(String backBuf, String string, double[] d) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static int fseek(
		    FILE _Stream,
		    long  _Offset,
		    int   _Origin
		    ) {
		return -1; // TODO
	}

	public static int ftell(
		    FILE _Stream
		    ) {
		return -1; // TODO
	}

	
}
