/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.actions.SoAudioRenderAction;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLTransform extends SoVRMLGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLTransform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTransform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTransform.class); }    	  	
	
	  public final SoSFVec3f translation = new SoSFVec3f();
	  public final SoSFRotation rotation = new SoSFRotation();
	  public final SoSFVec3f scale = new SoSFVec3f();
	  public final SoSFRotation scaleOrientation = new SoSFRotation();
	  public final SoSFVec3f center = new SoSFVec3f();

/*!
  Constructor.
*/
public SoVRMLTransform()
{
	SoVRMLTransform_commonConstructor();
}

/*!
  Constructor. \a numchildren is the expected number of children.
*/
public SoVRMLTransform(int numchildren) {
  super(numchildren);
  SoVRMLTransform_commonConstructor();
}

private void
SoVRMLTransform_commonConstructor()
{
	nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTransform.class);

	nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(translation,"translation", new SbVec3f(0.0f, 0.0f, 0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(rotation,"rotation", (SbRotation.identity()));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(scale,"scale", new SbVec3f(1.0f, 1.0f, 1.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(scaleOrientation,"scaleOrientation", (SbRotation.identity()));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(center,"center", new SbVec3f(0.0f, 0.0f, 0.0f));
}

// Doc in parent
public void
doAction(SoAction action)
{
	SoVRMLTransform_doAction(action);
}

public void
SoVRMLTransform_doAction(SoAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  SoGroup_doAction(action);
  state.pop();
}

// Doc in parent
public void callback(SoCallbackAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  super.callback(action);
  state.pop();
}

// Doc in parent
public void getBoundingBox(SoGetBoundingBoxAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  super.getBoundingBox(action);
  state.pop();
}

// Doc in parent
public void getMatrix(SoGetMatrixAction action)
{
  // need to push/pop to handle SoUnitsElement correctly
  action.getState().push();

  final SbMatrix m = new SbMatrix();
  m.setTransform(this.translation.getValue(),
                 this.rotation.getValue(),
                 this.scale.getValue(),
                 this.scaleOrientation.getValue(),
                 this.center.getValue());
  action.getMatrix().multLeft(m);
  SbMatrix mi = m.inverse();
  action.getInverse().multRight(mi);

  SoGroup_getMatrix(action);
  action.getState().pop();
}

// Doc in parent
public void rayPick(SoRayPickAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  super.rayPick(action);
  state.pop();
}

// Doc in parent
public void audioRender(SoAudioRenderAction action)
{
  SoVRMLTransform_doAction((SoAction)action);
}

// Doc in parent
public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  SoGroup_getPrimitiveCount(action);
  state.pop();
}

// Doc in parent
public void GLRenderBelowPath(SoGLRenderAction action)
{
  SoState state = action.getState();
  state.push();
  this.applyMatrix(state);
  super.GLRenderBelowPath(action);
  state.pop();
}

// Doc in parent
public void GLRenderInPath(SoGLRenderAction action)
{
  if (action.getCurPathCode() == SoAction.PathCode.IN_PATH) {
    SoState state = action.getState();
    state.push();
    this.applyMatrix(state);
    super.GLRenderInPath(action);
    state.pop();
  }
  else {
    // we got to the end of the path
    this.GLRenderBelowPath(action);
  }
}

// Doc in parent
public void notify(SoNotList list)
{
  super.notify(list);
}

//
// applies transformation to state.
//
public void applyMatrix(SoState state)
{
  final SbMatrix matrix = new SbMatrix();
  matrix.setTransform(this.translation.getValue(),
                      this.rotation.getValue(),
                      this.scale.getValue(),
                      this.scaleOrientation.getValue(),
                      this.center.getValue());
  if (matrix.operator_not_equal(SbMatrix.identity())) {
    SoModelMatrixElement.mult(state, this, matrix);
  }
}

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLTransform, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLTransform.class, "VRMLTransform", SoVRMLGroup.class);
	}

}
