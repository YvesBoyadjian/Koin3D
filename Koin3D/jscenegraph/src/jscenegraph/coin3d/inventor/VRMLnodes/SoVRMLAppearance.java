/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.annex.profiler.SoNodeProfiling;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLAppearance extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLAppearance.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLAppearance.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLAppearance.class); }
	  
  public final SoSFNode material = new SoSFNode();
  public final SoSFNode texture = new SoSFNode();
  public final SoSFNode textureTransform = new SoSFNode();
  
  private	  SoVRMLAppearanceP pimpl;
	  
/*!
  Constructor.
*/
public SoVRMLAppearance()
{
  pimpl = new SoVRMLAppearanceP();
  // supply a NULL-pointer as parent, since notifications will be
  // handled by the fields that actually contain the node(s)
  pimpl.childlist = new SoChildList(null);
  pimpl.childlistvalid = false;

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLAppearance.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(material,"material", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(texture,"texture", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(textureTransform,"textureTransform", (null));
}


/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete( pimpl.childlist );
  //Destroyable.delete( pimpl ); TODO
}

// doc in parent
public void
SoVRMLAppearance_doAction(SoAction action)
{
  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  if (action.getPathCode(numindices, indices) == SoAction.PathCode.IN_PATH) {
    this.getChildren().traverseInPath(action, numindices[0], indices[0]);
  }
  else {
    this.getChildren().traverse(action); // traverse all children
  }
}

// doc in parent
public void callback(SoCallbackAction action)
{
  SoVRMLAppearance_doAction(action);
}

// doc in parent
public void GLRender(SoGLRenderAction action)
{
  SoState state = action.getState();
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

  // workaround for weird (IMO) VRML97 texture/material handling. For
  // RGB[A] textures, the texture color should replace the diffuse color.
  final SbVec2s size = new SbVec2s();
  final int[] nc = new int[1];
  SoMultiTextureImageElement.getImage(state,0, size, nc);

  if (this.texture.getValue() != null &&
      SoTextureQualityElement.get(state) > 0.0f &&
      size.operator_not_equal(new SbVec2s((short)0,(short)0)) &&
      nc[0] >= 3) {

    float t = SoLazyElement.getTransparency(state, 0);
    int alpha = (int) ((1.0f - t) * 255.0f);

    // lock just in case two threads get here at the same time
    pimpl.lock();
    pimpl.fakecolor[0] = 0xffffff00 | alpha;
    pimpl.unlock();
    SoLazyElement.setPacked(state, this, 1, pimpl.fakecolor, alpha != 255);
  }
}

// doc in parent
public void search(SoSearchAction action)
{
  super.search(action);
  if (action.isFound()) return;
  SoVRMLAppearance_doAction(action);
}

// doc in parent
public SoChildList getChildren()
{
  if (!pimpl.childlistvalid) {
    // this is not 100% thread safe. The assumption is that no nodes
    // will be added or removed while a scene graph is being
    // traversed. For Coin, this is an ok assumption.
    pimpl.lock();
    // test again after we've locked
    if (!pimpl.childlistvalid) {
      SoVRMLAppearance thisp = (SoVRMLAppearance) this;
      SoVRMLParent.updateChildList(thisp, (thisp.pimpl.childlist));
      thisp.pimpl.childlistvalid = true;
    }
    pimpl.unlock();
  }
  return pimpl.childlist;
}

// doc in parent
public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f != null && f.getTypeId() == SoSFNode.getClassTypeId(SoSFNode.class)) {
    pimpl.childlistvalid = false;
  }
  super.notify(list);
}

  
	  
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLAppearance, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLAppearance.class, "VRMLAppearance", SoNode.class);	
	}

}
