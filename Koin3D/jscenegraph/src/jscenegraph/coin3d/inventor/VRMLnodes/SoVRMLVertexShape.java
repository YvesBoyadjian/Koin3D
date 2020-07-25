/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLVertexShape extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLVertexShape.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLVertexShape.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLVertexShape.class); } 
	  

	  public final SoSFNode coord = new SoSFNode();
	  public final SoSFNode texCoord = new SoSFNode();
	  public final SoSFNode normal = new SoSFNode();
	  public final SoSFNode color = new SoSFNode();
	  public final SoSFBool colorPerVertex = new SoSFBool();
	  public final SoSFBool normalPerVertex = new SoSFBool();
	  
	  private SoVRMLVertexShapeP pimpl;

	  /*!
	  Constructor.
	*/
	public SoVRMLVertexShape()
	{
	  pimpl = new SoVRMLVertexShapeP();
	  pimpl.normalcache = null;

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLVertexShape.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(coord,"coord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(texCoord,"texCoord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(normal,"normal", (null));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(color,"color", (null));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(colorPerVertex,"colorPerVertex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(normalPerVertex,"normalPerVertex", (true));
	}

	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLVertexShape, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLVertexShape.class, "VRMLVertexShape", SoVRMLGeometry.class);
	}

}
