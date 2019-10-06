/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoCameraLOD extends SoGroup {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLOD.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoLOD.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoLOD.class); }    	  			

	  public final SoMFFloat range = new SoMFFloat();
	  public final SoSFVec3f center = new SoSFVec3f();

	  private SoLODP pimpl;

	  
class SoLODP /*: public SoSoundElementHelper*/implements Destroyable
{
public
  SoLODP(SoCameraLOD master) { this.master = master; };
  SoCameraLOD master;
@Override
public void destructor() {
	master = null;
}
};

public SoCamera camera; // ptr

//SO_NODE_SOURCE(SoLOD);

/*!
  Default constructor.
*/
public SoCameraLOD()
{
  this.commonConstructor();
}

/*!
  Constructor.

  The argument should be the approximate number of children which is
  expected to be inserted below this node. The number need not be
  exact, as it is only used as a hint for better memory resource
  allocation.
*/
public SoCameraLOD(int numchildren) {
  super(numchildren);

  this.commonConstructor();
}

// private
private void
commonConstructor()
{
  pimpl = new SoLODP(this);

  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoLOD.class);

  nodeHeader.SO_NODE_ADD_FIELD(center,"center", (new SbVec3f(0, 0, 0)));
  nodeHeader.SO_NODE_ADD_MFIELD(range,"range", (0.0f));

  // Make multivalue field empty, as that is the default.
  this.range.setNum(0);
  // So it's not written in its default state on SoWriteAction
  // traversal.
  this.range.setDefault(true);
  isBuiltIn = true;
}

/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete(pimpl);
  super.destructor();
}

// Documented in superclass.
public static void
initClass()
{
    SoSubNode.SO__NODE_INIT_CLASS(SoLOD.class, "LOD", SoGroup.class);	 
  //SO_NODE_INTERNAL_INIT_CLASS(SoLOD, SO_FROM_INVENTOR_2_1|SoNode::VRML1);
}

public void setCamera(SoCamera camera) {
	this.camera = camera;
}

// Documented in superclass.
public void
doAction(SoAction action)
{
	  final int[] numindices = new int[1];
	  final int[][] indices = new int[1][];
	  SoAction.PathCode pathcode = action.getPathCode(numindices, indices);
	  if (pathcode == SoAction.PathCode.IN_PATH) {
	    this.children.traverseInPath(action, numindices[0], indices[0]);
	  }
	  else {
	    int idx = this.whichToTraverse(action);
	    if (idx >= 0) {
//	      this.children.traverse(action, idx);
//	      pimpl.enableTraversingOfInactiveChildren();
//	      pimpl.traverseInactiveChildren(this, action, idx, pathcode,
//	                                              this.getNumChildren(),
//	                                              this.getChildren());
	        children.traverse(action, /*childToTraverse*/idx, /*childToTraverse*/idx);
	    	
	    }
	  }
}

// Documented in superclass.
public void
callback(SoCallbackAction action)
{
  /*SoLOD::*/doAction((SoAction)action);
}

// Documented in superclass.
//public void TODO
//audioRender(SoAudioRenderAction action)
//{
//  pimpl.preAudioRender(this, action);
//  SoLOD::doAction((SoAction*)action);
//  pimpl.postAudioRender(this, action);
//}

// Documented in superclass.
public void
GLRender(SoGLRenderAction action)
{
  switch (action.getCurPathCode()) {
  case NO_PATH:
  case BELOW_PATH:
    GLRenderBelowPath(action);
    break;
  case IN_PATH:
    GLRenderInPath(action);
    break;
  case OFF_PATH:
    GLRenderOffPath(action);
    break;
  default:
    throw new IllegalStateException( "unknown path code.");
    //break;
  }
}

// Documented in superclass.
public void
GLRenderBelowPath(SoGLRenderAction action)
{
  int idx = this.whichToTraverse(action);
  if (idx >= 0) {
    SoNode child = (SoNode) this.children.get(idx);
    action.pushCurPath(idx/*, child*/);//TODO
    if (!action.abortNow()) {
      //SoNodeProfiling profiling; TODO
      //profiling.preTraversal(action);
      child.GLRenderBelowPath(action);
      //profiling.postTraversal(action);
    }
    action.popCurPath();
  }
  // don't auto cache LOD nodes.
  SoGLCacheContextElement.shouldAutoCache(action.getState(),
                                           SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
}

// Documented in superclass.
public void
GLRenderInPath(SoGLRenderAction action)
{
  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  SoAction.PathCode pathcode = action.getPathCode(numindices, indices);

  if (pathcode == SoAction.PathCode.IN_PATH) {
    for (int i = 0; (i < numindices[0]) && !action.hasTerminated(); i++) {
      int idx = indices[0][i];
      SoNode node = this.getChild(idx);
      action.pushCurPath(idx/*, node*/); //TODO
      if (!action.abortNow()) {
        //SoNodeProfiling profiling; TODO
        //profiling.preTraversal(action);
        node.GLRenderInPath(action);
        //profiling.postTraversal(action);
      }
      action.popCurPath(pathcode);
    }
  }
  else {
    assert(pathcode == SoAction.PathCode.BELOW_PATH);
    /*SoLOD::*/GLRenderBelowPath(action);
  }
}

// Documented in superclass.
public void
GLRenderOffPath(SoGLRenderAction action)
{
  int idx = this.whichToTraverse(action);;
  if (idx >= 0) {
    SoNode node = this.getChild(idx);
    if (node.affectsState()) {
      action.pushCurPath(idx/*, node*/); //TODO
      if (!action.abortNow()) {
        //SoNodeProfiling profiling; TODO
        //profiling.preTraversal(action);
        node.GLRenderOffPath(action);
        //profiling.postTraversal(action);
      }
      action.popCurPath();
    }
  }
}

// Documented in superclass.
public void
rayPick(SoRayPickAction action)
{
  /*SoLOD::*/doAction((SoAction)action);
}

// Documented in superclass.
public void
getBoundingBox(SoGetBoundingBoxAction action)
{
  // FIXME: SGI OIV seems to do some extra work here, but the manual
  // pages states that it should do a normal SoGroup traversal.
  // we should _not_ use whichToTraverse() to calculate bbox as
  // this would cause cache dependencies on the camera and
  // the model matrix.                       pederb, 2001-02-21
  super.getBoundingBox(action);
}

// Documented in superclass.
public void
getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  /*SoLOD::*/doAction((SoAction)action);
}

/*!
  Returns the child to traverse based on the ranges in
  SoLOD::range. Will clamp to index to the number of children.  This
  method will return -1 if no child should be traversed.  This will
  only happen if the node has no children though.
*/
protected int
whichToTraverse(SoAction action)
{
  SoState state = action.getState();
  final SbMatrix mat = SoModelMatrixElement.get(state); //ref
  //final SbViewVolume vv = SoViewVolumeElement.get(state); //ref

  final SbVec3f worldcenter = new SbVec3f();
  mat.multVecMatrix(this.center.getValue(), worldcenter);
  
  SbVec3f world_camera_position = camera.position.getValue();

  float dist = (/*vv.getProjectionPoint()*/world_camera_position.operator_minus( worldcenter)).length();
  
  SoRecursiveIndexedFaceSet SoRecursiveIndexedFaceSet = (SoRecursiveIndexedFaceSet)getChild(1);
  
  RecursiveChunk rc = SoRecursiveIndexedFaceSet.recursiveChunk;
  
  float model_x, model_y;
  
  SbVec3f model_xyz = new SbVec3f();
  
  SbVec3f world_camera_direction = camera.orientation.getValue().multVec(new SbVec3f(0,0,-1)); 
  
  mat.inverse().multVecMatrix(world_camera_position, model_xyz);
  
  world_camera_direction.normalize();
  
  model_xyz.add(world_camera_direction.operator_mul(500));
  
  model_x = model_xyz.getX();
  model_y = model_xyz.getY();
  
  if( rc.isInside(model_x, model_y)) {
	  dist = 0;
  }
  else {
	  
	  //System.out.print("dist = "+dist);
	  dist = rc.distance(model_x, model_y);
	  
	  //System.out.println(" dist = "+dist);	  
  }
  int i;
  int n = this.range.getNum();

  for (i = 0; i < n; i++) {
    if (dist < this.range.operator_square_bracket(i)) break;
  }
  if (i >= this.getNumChildren()) i = this.getNumChildren() - 1;
  return i;
}

// Doc from superclass.
public void
notify(SoNotList nl)
{
  super.notify(nl);
  //pimpl.notifyCalled(); TODO
}

	  

}
