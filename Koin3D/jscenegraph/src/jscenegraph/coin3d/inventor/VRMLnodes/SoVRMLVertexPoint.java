/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLVertexPoint extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLVertexPoint.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLVertexPoint.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLVertexPoint.class); }    
	  
	  public final SoSFNode coord = new SoSFNode();
	  public final SoSFNode color = new SoSFNode();	  

	  /*!
	  Constructor.
	*/
	  public SoVRMLVertexPoint()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLVertexPoint.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(coord,"coord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(color,"color", (null));
	}

	  
	// Doc in parent
	public void computeBBox(SoAction action, final SbBox3f box,
	                               final SbVec3f center)
	{
		//TODO
	}
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLVertexPoint, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLVertexPoint.class, "VRMLVertexPoint", SoVRMLGeometry.class);
	}

	
}
