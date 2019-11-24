/**
 * 
 */
package application.scenegraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class OverallTexture {
	
	ChunkArray ca;
	
	SoTexture2 texture;

	public OverallTexture(ChunkArray chunkArray) {
		this.ca = chunkArray;
		
		texture = new SoTexture2();
		texture.image.setValue(new SbVec2s((short)ca.getW(),(short)ca.getH()),3,getImage2(), true);
	}

	private MemoryBuffer getImage2() {
		int w = ca.getW();
		int h = ca.getH();
		
		byte[] image;
		
		if( (image = loadImage2()) == null ) {
		
			image = new byte[w*h*3];
			
			int indexImage = 0;
			for(int j=0;j<h;j++) {
				for(int i=0;i<w;i++) {
					int index = i*h+j;
					//int indexI = i+w*j;
					
					int RGBA = ca.colorsGetRGBA(index);
					
					image[indexImage] = (byte)((RGBA>>>24) & 0xFF);indexImage++;
					image[indexImage] = (byte)((RGBA>>>16) & 0xFF);indexImage++;
					image[indexImage] = (byte)((RGBA>>>8) & 0xFF);indexImage++;
					
	//				image[indexI*3] = ca.colorsGet(index*4);
	//				image[indexI*3+1] = ca.colorsGet(index*4+1);
	//				image[indexI*3+2] = ca.colorsGet(index*4+2);
				}
			}
			saveImage2(image); //not very efficient ( 3 seconds gain)
		}
		
		return MemoryBuffer.allocateFromByteArray(image);
	}
	
	private void saveImage2(byte[] image) {
		File file = new File("overall_texture.mri");
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(image);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] loadImage2() {
		File file = new File( "overall_texture.mri" );
		
		if( !file.exists()) {
			return null;
		}
		if( !file.isFile()) {
			return null;
		}
		if ( file.length() != getTextureFileLength()) {
			return null;
		}
		byte[] buffer = null; 
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			buffer = new byte[(int)file.length()];
			fileInputStream.read(buffer);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return buffer;
	}
	
	private long getTextureFileLength() {
		int w = ca.getW();
		int h = ca.getH();
		return w* h * 3;
	}

	private byte[] getImage() {
		int w = ca.getW();
		int h = ca.getH();
		byte[] image = new byte[w*h*3];
		
		for(int i=0;i<w;i++) {
			for(int j=0;j<h;j++) {
				int index = i*h+j;
				int indexI = i+w*j;
				
				image[indexI*3] = ca.colorsGet(index*4);
				image[indexI*3+1] = ca.colorsGet(index*4+1);
				image[indexI*3+2] = ca.colorsGet(index*4+2);
			}
		}
		
		return image;
	}

	public SoNode getTexture() {
		return texture;
	}

}
