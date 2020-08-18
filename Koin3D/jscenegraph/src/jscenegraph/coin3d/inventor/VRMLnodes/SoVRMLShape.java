/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.annex.profiler.SoNodeProfiling;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.actions.SoWriteAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLShape extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLShape.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLShape.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLShape.class); }    
	  
	  public final SoSFNode appearance = new SoSFNode();
	  public final SoSFNode geometry = new SoSFNode();
	  public final SoSFEnum renderCaching = new SoSFEnum();
	  public final SoSFEnum boundingBoxCaching = new SoSFEnum();

	  private static int sovrmlshape_numrendercaches = 0;

	  SoVRMLShapeP pimpl;
	
public SoVRMLShape()
{
  pimpl = new SoVRMLShapeP();

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLShape.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(appearance,"appearance", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(geometry,"geometry", (null));

  nodeHeader.SO_NODE_ADD_FIELD(renderCaching,"renderCaching", (SoSeparator.CacheEnabled.AUTO));
  nodeHeader.SO_NODE_ADD_FIELD(boundingBoxCaching,"boundingBoxCaching", (SoSeparator.CacheEnabled.AUTO));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.ON);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.OFF);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.AUTO);

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCaching,"renderCaching", "CacheEnabled");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching", "CacheEnabled");

  // supply a NULL-pointer as parent, since notifications will be 
  // handled by the fields that actually contain the node(s)
  pimpl.childlist = new SoChildList(null);
  pimpl.childlistvalid = false;
  pimpl.cachelist = null;
}


public void destructor()
{
  Destroyable.delete( pimpl.childlist);
  Destroyable.delete( pimpl.cachelist);
  Destroyable.delete( pimpl);
  super.destructor();
}

public void setNumRenderCaches(int num)
{
  sovrmlshape_numrendercaches = num;
}

public int getNumRenderCaches()
{
  return sovrmlshape_numrendercaches;
}

public boolean affectsState()
{
  return false;
}

public void doAction(SoAction action) {
	SoVRMLShape_doAction(action);
}

public void
SoVRMLShape_doAction(SoAction action)
{
  SoState state = action.getState();

  if (state.isElementEnabled(SoLazyElement.getClassStackIndex(SoLazyElement.class))) {
    if ((this.appearance.getValue() == null) ||
        (((SoVRMLAppearance)this.appearance.getValue()).material.getValue() == null)) {
      SoLazyElement.setLightModel(state, SoLazyElement.LightModel.BASE_COLOR.getValue());
    }
  }

  state.push();
  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  if (action.getPathCode(numindices, indices) == SoAction.PathCode.IN_PATH) {
    this.getChildren().traverseInPath(action, numindices[0], indices[0]);
  }
  else {
    this.getChildren().traverse(action); // traverse all children
  }
  state.pop();
}

public void callback(SoCallbackAction action)
{
  SoVRMLShape_doAction((SoAction) action);
}

public void GLRender(SoGLRenderAction action)
{
  SoState state = action.getState();
  state.push();

  if ((this.appearance.getValue() == null) ||
      (((SoVRMLAppearance)this.appearance.getValue()).material.getValue() == null)) {
    SoLazyElement.setLightModel(state, SoLazyElement.LightModel.BASE_COLOR.getValue());
  }

  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  SoAction.PathCode pathcode = action.getPathCode(numindices, indices);

  Object[] childarray = this.getChildren().getArrayPtr();

  if (pathcode == SoAction.PathCode.IN_PATH) {
    int lastchild = indices[0][numindices[0] - 1];
    for (int i = 0; i <= lastchild && !action.hasTerminated(); i++) {
      SoNode child = (SoNode)childarray[i];
      action.pushCurPath(i, child);
      if (action.getCurPathCode() != SoAction.PathCode.OFF_PATH ||
          child.affectsState()) {
        if (!action.abortNow()) {
          final SoNodeProfiling profiling = new SoNodeProfiling();
          profiling.preTraversal(action);
          child.GLRender(action);
          profiling.postTraversal(action);
          //profiling.destructor();
        }
        else {
          SoCacheElement.invalidate(state);
        }
      }
      action.popCurPath(pathcode);
    }
  }
  else {
    action.pushCurPath();
    int n = this.getChildren().getLength();
    for (int i = 0; i < n && !action.hasTerminated(); i++) {
      action.popPushCurPath(i, (SoNode)childarray[i]);
      if (action.abortNow()) {
        // only cache if we do a full traversal
        SoCacheElement.invalidate(state);
        break;
      }
      final SoNodeProfiling profiling = new SoNodeProfiling();
      profiling.preTraversal(action);
      ((SoNode)childarray[i]).GLRender(action);
      profiling.postTraversal(action);
    }
    action.popCurPath();
  }
  state.pop();
}



public void getBoundingBox(SoGetBoundingBoxAction action)
{
  SoState state = action.getState();
  state.push();
  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  if (action.getPathCode(numindices, indices) == SoAction.PathCode.IN_PATH) {
    this.getChildren().traverseInPath(action, numindices[0], indices[0]);
  }
  else {
    this.getChildren().traverse(action); // traverse all children
  }
  state.pop();
}


public void rayPick(SoRayPickAction action)
{
  SoVRMLShape_doAction(action);
}

//// Doc in parent
//public void write(SoWriteAction action)
//{
//  // do not call inherited::write() or SoGroup::write()
//  this.boundingBoxCaching.setDefault(true);
//  this.renderCaching.setDefault(true);
//  super.write(action);
//}

public void search(SoSearchAction action)
{
  // Include this node in the search.
  super.search(action);
  if (action.isFound()) return;

  SoVRMLShape_doAction(action);
}

public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  SoVRMLShape_doAction((SoAction) action);
}



public SoChildList getChildren()
{
  if (!pimpl.childlistvalid) {
    // this is not 100% thread safe. The assumption is that no nodes
    // will be added or removed while a scene graph is being
    // traversed. For Coin, this is an ok assumption.
	  pimpl.lockChildList();
    // test again after we've locked
    if (!pimpl.childlistvalid) {
      SoVRMLShape thisp = (SoVRMLShape) this;
      SoVRMLParent.updateChildList(thisp, pimpl.childlist);
      pimpl.childlistvalid = true;
    }
    pimpl.unlockChildList();
  }
  return pimpl.childlist;
}

public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f != null && f.getTypeId().operator_equal_equal(SoSFNode.getClassTypeId(SoSFNode.class))) {
    pimpl.childlistvalid = false;
  }
  super.notify(list);
}

	  
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass() // static
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLShape, SO_VRML97_NODE_TYPE);
	  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLShape.class, "VRMLShape", SoNode.class);	
  //coin_atexit((coin_atexit_f*)sovrmlshape_cleanup, CC_ATEXIT_NORMAL); 

  SoType type = new SoType(SoVRMLShape.getClassTypeId());
  SoRayPickAction.addMethod(type, SoNode::rayPickS);
}

}
