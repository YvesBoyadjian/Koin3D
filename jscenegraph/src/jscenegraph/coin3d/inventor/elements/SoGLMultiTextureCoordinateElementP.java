/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLMultiTextureCoordinateElementP {

	public
		  final SbListOfMutableRefs<SoGLMultiTextureCoordinateElement.GLUnitData> unitdata = new SbListOfMutableRefs<>(()->new SoGLMultiTextureCoordinateElement.GLUnitData());
	public	  int contextid;
		  
		  // switch/case table for faster rendering.
	public	  enum SendLookup {
		    UNINITIALIZED,
		    NONE,
		    FUNCTION,
		    TEXCOORD2,
		    TEXCOORD3,
		    TEXCOORD4
		  };
		  public final SbList<SendLookup> sendlookup = new SbList<>();
		  public cc_glglue glue;
		  public final SoGLMultiTextureCoordinateElement.GLUnitData defaultdata = new SoGLMultiTextureCoordinateElement.GLUnitData();
		  void ensureCapacity(int unit) {
		    while (unit >= this.unitdata.getLength()) {
		      this.unitdata.append(new SoGLMultiTextureCoordinateElement.GLUnitData());
		    }
		  }
		  
		public void destructor() { // java port
			unitdata.destructor();
		}

}
