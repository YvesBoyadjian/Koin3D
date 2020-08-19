/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLSensor extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLSensor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLSensor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLSensor.class); }    	  	
	
	  public
		  final SoSFBool isActive = new SoSFBool();
	  public final SoSFBool enabled = new SoSFBool();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLSensor, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLSensor.class, "VRMLSensor", SoNode.class);
	}

	public SoVRMLSensor()
	{
	  nodeHeader.SO_NODE_CONSTRUCTOR(SoVRMLSensor.class);

	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isActive,"isActive");
	  // DragSensor reads from this field. Initialize it.
	  this.isActive.setValue(false);
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(enabled,"enabled", (true));
	}

}
