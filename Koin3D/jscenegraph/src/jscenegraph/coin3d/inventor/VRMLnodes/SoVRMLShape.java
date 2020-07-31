/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

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
