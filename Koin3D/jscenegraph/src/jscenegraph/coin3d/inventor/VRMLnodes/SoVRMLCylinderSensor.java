/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.projectors.SbCylinderPlaneProjector;
import jscenegraph.database.inventor.projectors.SbCylinderProjector;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLCylinderSensor extends SoVRMLDragSensor {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLCylinderSensor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLCylinderSensor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLCylinderSensor.class); }    	  	
	

	  public final SoSFFloat diskAngle = new SoSFFloat();
	  public final SoSFFloat maxAngle = new SoSFFloat();
	  public final SoSFFloat minAngle = new SoSFFloat();
	  public final SoSFFloat offset = new SoSFFloat();
	  public final SoSFRotation rotation_changed = new SoSFRotation();

	  private SbCylinderProjector cylinderproj; //ptr

/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLCylinderSensor, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLCylinderSensor.class, "VRMLCylinderSensor", SoVRMLDragSensor.class);
}

/*!
  Constructor.
*/
public SoVRMLCylinderSensor()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLCylinderSensor.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(diskAngle,"diskAngle", (0.262f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(maxAngle,"maxAngle", (-1.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(minAngle,"minAngle", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(offset,"offset", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(rotation_changed,"rotation_changed");

  this.cylinderproj = new SbCylinderPlaneProjector();
}
}
