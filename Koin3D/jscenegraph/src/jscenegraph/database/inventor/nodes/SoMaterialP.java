/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.mevis.inventor.misc.SoVBO;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterialP {
	
	public int materialtype;
	public int transparencyflag;
	public final SoColorPacker single_colorpacker = new SoColorPacker();

	public SoVBO vbo;
	
	SoColorPacker getColorPacker() {
		return this.single_colorpacker;
	}

	public void destructor() {
		// TODO Auto-generated method stub
		
	}
}
