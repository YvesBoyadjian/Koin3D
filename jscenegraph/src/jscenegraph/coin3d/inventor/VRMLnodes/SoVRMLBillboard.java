/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.glue.Gl;
import jscenegraph.coin3d.inventor.annex.profiler.SoNodeProfiling;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLBillboard extends SoVRMLParent {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLBillboard.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLBillboard.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLBillboard.class); }    	  	
		
	
	public final SoSFVec3f axisOfRotation = new SoSFVec3f();

// Doc in parent
public void callback(SoCallbackAction action)
{
  SoVRMLBillboard_doAction((SoAction) action);
}

	
	
	// Doc in parent
	public void
	GLRender(SoGLRenderAction action)
	{
	  switch (action.getCurPathCode()) {
	  case NO_PATH:
	  case BELOW_PATH:
	    this.GLRenderBelowPath(action);
	    break;
	  case OFF_PATH:
	    // do nothing. Separator will reset state.
	    break;
	  case IN_PATH:
	    this.GLRenderInPath(action);
	    break;
	  }
	}
	

// Doc in parent
public void getBoundingBox(SoGetBoundingBoxAction action)
{
  SoState state = action.getState();
  state.push();
  this.performRotation(state);
  SoGroup_getBoundingBox(action);
  state.pop();
}

// Doc in parent
public void getMatrix(SoGetMatrixAction action)
{
  SoState state = action.getState();
  state.push();

  final SbViewVolume vv = SoViewVolumeElement.get(state);
  SbRotation rot = new SbRotation();
  rot = this.computeRotation(action.getInverse(), vv, rot);

  final SbMatrix rotM = new SbMatrix();
  rotM.setRotate(rot);
  action.getMatrix().multLeft(rotM);
  final SbMatrix invRotM = new SbMatrix();
  invRotM.setRotate(rot.inverse());
  action.getInverse().multRight(invRotM);

  SoGroup_getMatrix(action);
  state.pop();
}

	

    static boolean chkglerr = SoGL.sogl_glerror_debugging();
    
	// Doc in parent
	public void
	GLRenderBelowPath(SoGLRenderAction action)
	{
	  SoState state = action.getState();

	  // never cache this node
	  SoCacheElement.invalidate(state);

	  state.push();
	  this.performRotation(state);

	  int n = this.getChildren().getLength();
	  Object[] childarray = this.getChildren().getArrayPtr();

	  action.pushCurPath();
	  for (int i = 0; i < n && !action.hasTerminated(); i++) {
	    action.popPushCurPath(i, (SoNode)childarray[i]);
	    if (action.abortNow()) {
	      // only cache if we do a full traversal
	      break;
	    }
	    final SoNodeProfiling profiling = new SoNodeProfiling();
	    profiling.preTraversal(action);
	    ((SoNode)childarray[i]).GLRenderBelowPath(action);
	    profiling.postTraversal(action);

	//#if COIN_DEBUG
	    // The GL error test is default disabled for this optimized
	    // path.  If you get a GL error reporting an error in the
	    // Separator node, enable this code by setting the environment
	    // variable COIN_GLERROR_DEBUGGING to "1" to see exactly which
	    // node caused the error.
	    if (chkglerr) {
	      //cc_string str;
	      //cc_string_construct(&str);
	      	final String[] str = new String[1];
	      int errs = Gl.coin_catch_gl_errors(str);
	      if (errs > 0) {
	        SoDebugError.post("SoVRMLBillboard::GLRenderBelowPath",
	                           "GL error: '"+str[0]+"', nodetype: "+	                           
	                           this.getChildren().operator_square_bracket(i).getTypeId().getName().getString());
	      }
	      //cc_string_clean(&str);
	    }
	//#endif // COIN_DEBUG
	  }
	  action.popCurPath();
	  state.pop();
	}
	
	public void
	GLRenderInPath(SoGLRenderAction action )
	{
		  final int[] numindices = new int[1];
		  final int[][] indices = new int[1][];

		  SoAction.PathCode pathcode = action.getPathCode(numindices, indices);

		  if (pathcode == SoAction.PathCode.IN_PATH) {
		    SoState state = action.getState();
		    SoCacheElement.invalidate(state);
		    Object[] childarray = this.getChildren().getArrayPtr();
		    state.push();
		    this.performRotation(state);

		    int childidx = 0;
		    for (int i = 0; i < numindices[0]; i++) {
		      for (; childidx < indices[0][i] && !action.hasTerminated(); childidx++) {
		        SoNode offpath = (SoNode)childarray[childidx];
		        if (offpath.affectsState()) {
		          action.pushCurPath(childidx, offpath);
		          if (!action.abortNow()) {
		            final SoNodeProfiling profiling = new SoNodeProfiling();
		            profiling.preTraversal(action);
		            offpath.GLRenderOffPath(action);
		            profiling.postTraversal(action);
		          }
		          action.popCurPath(pathcode);
		        }
		      }
		      SoNode inpath = (SoNode)childarray[childidx];
		      action.pushCurPath(childidx, inpath);
		      if (!action.abortNow()) {
		        final SoNodeProfiling profiling = new SoNodeProfiling();
		        profiling.preTraversal(action);
		        inpath.GLRenderInPath(action);
		        profiling.postTraversal(action);
		      }
		      action.popCurPath(pathcode);
		      childidx++;
		    }
		    state.pop();
		  }
		  else {
		    // we got to the end of the path
		    assert(action.getCurPathCode() == SoAction.PathCode.BELOW_PATH);
		    this.GLRenderBelowPath(action);
		  }
	}
	
	// Doc in parent
	public void
	GLRenderOffPath(SoGLRenderAction action)
	{
	  // do nothing
	}
	
	public void doAction(SoAction action) {
		SoVRMLBillboard_doAction(action);
	}
	
	// Doc in parent
	public void
	SoVRMLBillboard_doAction(SoAction action)
	{
	  SoState state = action.getState();
	  state.push();
	  this.performRotation(state);
	  SoGroup_doAction(action);
	  state.pop();
	}

	// Doc in parent
	public void
	pick(SoPickAction action)
	{
	  SoVRMLBillboard_doAction((SoAction) action);
	}
	
// Doc in parent
public void search(SoSearchAction action)
{
  SoNode_search(action);
  if (action.isFound()) return;
  SoGroup_doAction(action);
}

	

	private final SbRotation rot = new SbRotation(); // SINGLE_THREAD
	
	private final SbMatrix imm = new SbMatrix(); // SINGLE_THREAD

	//
	// private method that appends the needed rotation to the state
	//
	public void
	performRotation(SoState state)
	{
	  /*imm = new SbMatrix(*/SoModelMatrixElement.get(state).inverse(imm)/*)*/;
	  final SbViewVolume vv = SoViewVolumeElement.get(state); // ref

	  /*rot = */computeRotation(imm, vv, rot);

	  // append the desired rotation to the state
	  SoModelMatrixElement.rotateBy(state, (SoNode) this, rot);
	}

	  private final SbVec3f up = new SbVec3f(), look = new SbVec3f(), right = new SbVec3f(); // SINGLE_THREAD
	  private final SbVec3f zero = new SbVec3f(); // SINGLE_THREAD
	  private final SbVec3f zVector = new SbVec3f(0.0f, 0.0f, 1.0f); // SINGLE_THREAD
	  private final SbVec3f dummy = new SbVec3f(); // SINGLE_THREAD
	  private final SbMatrix matrix = new SbMatrix(); // SINGLE_THREAD 
	//
	// private method that computes the needed rotation
	//
	public SbRotation
	computeRotation(final SbMatrix invMM, final SbViewVolume vv, final SbRotation dummyRot)
	{
	  final SbVec3f rotaxis = /*new SbVec3f(*/this.axisOfRotation.getValue()/*)*/;

	  up.constructor(); look.constructor(); right.constructor();
	  
	  invMM.multDirMatrix(vv.getViewUp(dummy), up);
	  invMM.multDirMatrix(vv.getProjectionDirection().operator_minus_with_dummy(dummy), look);

	  if (rotaxis.operator_equal_equal(/*new SbVec3f(0.0f, 0.0f, 0.0f)*/zero)) {
	    // always orient the billboard towards the viewer
	    right.copyFrom( up.cross(look,dummy));
	    up.copyFrom( look.cross(right,dummy));
	  } else { 
	    // The VRML97 spec calls for rotating the local z-axis of the
	    // billboard to face the viewer, pivoting around the axis of
	    // rotation. If the axis of rotation is the z axis, this angle
	    // will be zero, and no rotation can happen. We don't actually
	    // bother to compute this angle at all, but set up = rotaxis and
	    // use cross products from there to construct the rotation matrix.
	    // This is more numerically stable, more general, and the code is
	    // much nicer, but it also means that we must check specifically
	    // for this case.
	    if (rotaxis.operator_equal_equal(/*new SbVec3f(0.0f, 0.0f, 1.0f)*/zVector)) {
	    	dummyRot.setIdentity();
	    	return dummyRot;//new SbRotation(SbRotation.identity()); 
	    }
	    
	    up.copyFrom( rotaxis);
	    right.copyFrom( up.cross(look,dummy));
	    look.copyFrom( right.cross(up,dummy));
	  }

	  // construct the rotation matrix with the vectors defining the
	  // desired orientation
	  matrix.setIdentity();// = /*new SbMatrix(*/SbMatrix.identity()/*)*/;

	  right.normalize();
	  up.normalize();
	  look.normalize();
	  
	  matrix.getValue()[0][0] = right.operator_square_bracket(0);
	  matrix.getValue()[0][1] = right.operator_square_bracket(1);
	  matrix.getValue()[0][2] = right.operator_square_bracket(2);
	  
	  matrix.getValue()[1][0] = up.operator_square_bracket(0);
	  matrix.getValue()[1][1] = up.operator_square_bracket(1);
	  matrix.getValue()[1][2] = up.operator_square_bracket(2);
	  
	  matrix.getValue()[2][0] = look.operator_square_bracket(0);
	  matrix.getValue()[2][1] = look.operator_square_bracket(1);
	  matrix.getValue()[2][2] = look.operator_square_bracket(2);

	  dummyRot.constructor(matrix);
	  return dummyRot;//new SbRotation(matrix);
	}	

/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLBillboard, SO_VRML97_NODE_TYPE);
	  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLBillboard.class, "VRMLBillboard", SoVRMLParent.class);
}


}
