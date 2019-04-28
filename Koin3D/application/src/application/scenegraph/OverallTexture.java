/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.nodes.SoNode;

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
		texture.image.setValue(new SbVec2s((short)ca.getW(),(short)ca.getH()),3,getImage(), true);
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
