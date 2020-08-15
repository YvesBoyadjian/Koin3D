/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.elements.SoColorPacker;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLMaterialP {

	  public final SbColor tmpambient = new SbColor();
	  public float tmptransparency;

	  public final SoColorPacker single_colorpacker = new SoColorPacker();

	  SoColorPacker getColorPacker() {
//		  #ifdef COIN_THREADSAFE
//		      SoColorPacker ** cptr = (SoColorPacker**) this->colorpacker_storage.get();
//		      return * cptr;
//		  #else // COIN_THREADSAFE
		      return this.single_colorpacker;
//		  #endif // COIN_THREADSAFE
		    }

}
