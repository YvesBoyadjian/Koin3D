/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLPointSet extends SoVRMLVertexPoint {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLPointSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLPointSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLPointSet.class); }

	/*!
	  Constructor.
	*/
	public SoVRMLPointSet()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLPointSet.class);
	}

	
	// Doc in parent
	public void generatePrimitives(SoAction action)
	{
		//TODO
	}
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLPointSet, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLPointSet.class, "VRMLPointSet", SoVRMLVertexPoint.class);
	}

	
}
