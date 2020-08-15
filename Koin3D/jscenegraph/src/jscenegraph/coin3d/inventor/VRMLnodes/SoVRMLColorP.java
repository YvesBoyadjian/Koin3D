/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.mevis.inventor.misc.SoVBO;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLColorP {

	  final SoColorPacker single_colorpacker = new SoColorPacker();
	  
	  SoVBO vbo; //ptr

	  SoColorPacker getColorPacker() {
//		  #ifdef COIN_THREADSAFE
//		      SoColorPacker ** cptr = (SoColorPacker**) this->colorpacker_storage.get();
//		      return * cptr;
//		  #else // COIN_THREADSAFE
		      return this.single_colorpacker;
//		  #endif // COIN_THREADSAFE
		    }

}
