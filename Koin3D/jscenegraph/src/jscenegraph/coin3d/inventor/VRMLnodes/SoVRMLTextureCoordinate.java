/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLTextureCoordinate extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLTextureCoordinate.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTextureCoordinate.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTextureCoordinate.class); }
	  
	  private SoVRMLTextureCoordinateP pimpl;
	  
	  public final SoMFVec2f point = new SoMFVec2f();

	  
/*!
  Constructor.
*/
public SoVRMLTextureCoordinate()
{
  pimpl = new SoVRMLTextureCoordinateP();
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTextureCoordinate.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(point,"point");
}

	  
	  
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLTextureCoordinate, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLTextureCoordinate.class, "VRMLTextureCoordinate", SoNode.class);
}

}
