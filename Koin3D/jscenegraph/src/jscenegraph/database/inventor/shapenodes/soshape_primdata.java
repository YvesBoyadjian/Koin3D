/**
 * 
 */
package jscenegraph.database.inventor.shapenodes;

import jscenegraph.coin3d.inventor.SbTesselator;
import jscenegraph.coin3d.inventor.base.SbGLUTessellator;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoLineDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoShape;

/**
 * @author Yves Boyadjian
 *
 */
public class soshape_primdata {
	
	  SoShape.TriangleShape shapetype;
	  SoAction action; //ptr
	  SoShape shape; //ptr
	  SoPrimitiveVertex vertsArray; //ptr
	  SoPointDetail pointDetails; //ptr
	  SoFaceDetail faceDetail; //ptr
	  SoLineDetail lineDetail; //ptr
	  int arraySize;
	  int counter;
	  SbTesselator tess; //ptr
	  SbGLUTessellator glutess; //ptr
	  int faceCounter;

	  boolean matPerFace;
	  boolean normPerFace;
	


public void
beginShape(SoShape shapeptr, SoAction actionptr,
                               SoShape.TriangleShape shapetypearg,
                               SoDetail detail)
{
  this.shape = shapeptr;
  this.action = actionptr;
  this.shapetype = shapetypearg;
  // this is a hack. Only one of these will be used, and the
  // other one is an illegal cast.
  if(detail instanceof SoFaceDetail)
	  this.faceDetail = (SoFaceDetail )detail;
  if(detail instanceof SoLineDetail)
	  this.lineDetail = (SoLineDetail )detail;
  this.counter = 0;

  SoState state = action.getState();

  SoMaterialBindingElement.Binding mbind = SoMaterialBindingElement.get(state);
  SoNormalBindingElement.Binding nbind = SoNormalBindingElement.get(state);

  // need to test for PER_FACE bindings since special rules need to followed 
  // to get the correct per vertex material and normal indices in these cases
  // (basically the same rules as when sending geometry to OpenGL)
  this.matPerFace = 
    (mbind == SoMaterialBindingElement.Binding.PER_FACE) ||
    (mbind == SoMaterialBindingElement.Binding.PER_FACE_INDEXED);
  this.normPerFace = 
    (nbind == SoNormalBindingElement.Binding.PER_FACE) ||
    (nbind == SoNormalBindingElement.Binding.PER_FACE_INDEXED);
    
}

}
