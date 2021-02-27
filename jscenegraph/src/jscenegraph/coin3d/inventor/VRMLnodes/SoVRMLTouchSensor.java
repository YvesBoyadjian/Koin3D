/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLTouchSensor extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLTouchSensor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTouchSensor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTouchSensor.class); }    	  	

	  public SoSFBool enabled = new SoSFBool();
	  public SoSFVec3f hitNormal_changed = new SoSFVec3f();
	  public SoSFVec3f hitPoint_changed = new SoSFVec3f();
	  public SoSFVec2f hitTexCoord_changed = new SoSFVec2f();
	  public SoSFBool isActive = new SoSFBool();
	  public SoSFBool isOver = new SoSFBool();
	  public SoSFTime touchTime = new SoSFTime();

	  private
		  boolean isactive;
	  
	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLTouchSensor, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLTouchSensor.class, "VRMLTouchSensor", SoNode.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLTouchSensor()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTouchSensor.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(enabled,"enabled", (true));

	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(hitNormal_changed,"hitNormal_changed");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(hitPoint_changed,"hitPoint_changed");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(hitTexCoord_changed,"hitTexCoord_changed");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isActive,"isActive");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isOver,"isOver");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(touchTime,"touchTime");

	  this.isactive = false;
	}
}
