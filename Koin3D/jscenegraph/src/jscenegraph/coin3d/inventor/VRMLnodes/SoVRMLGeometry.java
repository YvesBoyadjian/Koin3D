/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLGeometry extends SoShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLGeometry.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLGeometry.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLGeometry.class); } 
	  
	  private SoVRMLGeometryP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVRMLGeometry()
	{
	  pimpl = new SoVRMLGeometryP();
	  // supply a NULL-pointer as parent, since notifications will be 
	  // handled by the fields that actually contain the node(s)
	  pimpl.childlist = new SoChildList(null);
	  pimpl.childlistvalid = false;
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLGeometry.class);
	}


/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete( pimpl.childlist);
  Destroyable.delete(pimpl);
}

// Doc in parent
public boolean shouldGLRender(SoGLRenderAction action)
{
  return super.shouldGLRender(action);
}
 
/*!
  Convenience method that updates the shape hints element.
*/
public void setupShapeHints(SoState state, boolean ccw, boolean solid)
{
  if ((SoOverrideElement.getFlags(state) & SoOverrideElement.ElementMask.SHAPE_HINTS.getValue()) == 0) {
    SoShapeHintsElement.set(state, this, 
                             ccw ? SoShapeHintsElement.VertexOrdering.COUNTERCLOCKWISE : SoShapeHintsElement.VertexOrdering.CLOCKWISE,
                             solid ? SoShapeHintsElement.ShapeType.SOLID : SoShapeHintsElement.ShapeType.UNKNOWN_SHAPE_TYPE,
                             SoShapeHintsElement.FaceType.FACE_TYPE_AS_IS);
  }
}

// Doc in parent
public SoChildList getChildren()
{
  if (!pimpl.childlistvalid) {
    // this is not 100% thread safe. The assumption is that no nodes
    // will be added or removed while a scene graph is being
    // traversed. For Coin, this is an ok assumption.
    pimpl.lockChildList();
    // test again after we've locked
    if (!pimpl.childlistvalid) {
      SoVRMLGeometry thisp = (SoVRMLGeometry) this;
      SoVRMLParent.updateChildList(thisp, thisp.pimpl.childlist);
      thisp.pimpl.childlistvalid = true;
    }
    pimpl.unlockChildList();
  }
  return pimpl.childlist;
}

// Doc in parent
public void search(SoSearchAction action)
{
  SoNode_search(action);
  if (action.isFound() || this.getChildren() == null) return;

  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];
  if (action.getPathCode(numindices, indices) == SoAction.PathCode.IN_PATH) {
    this.getChildren().traverseInPath(action, numindices[0], indices[0]);
  }
  else {
    this.getChildren().traverse(action); // traverse all children
  }
}

// Doc in parent
public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f != null && f.getTypeId() == SoSFNode.getClassTypeId(SoSFNode.class)) {
    pimpl.childlistvalid = false;
  }
  super.notify(list);
}

// Doc in parent
public void copyContents(SoFieldContainer from,
                             boolean copyConn)
{
  super.copyContents(from, copyConn);
  pimpl.childlistvalid = false;
  pimpl.childlist.truncate(0);
}

	  
	  
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLGeometry, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLGeometry.class, "VRMLGeometry", SoShape.class);
	}

}
