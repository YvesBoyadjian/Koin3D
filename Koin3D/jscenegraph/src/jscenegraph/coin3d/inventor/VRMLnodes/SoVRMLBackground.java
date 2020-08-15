/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLBackground extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLBackground.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLBackground.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLBackground.class); }    	  	
	
	  public final SoMFColor groundColor = new SoMFColor();
	  public final SoMFFloat groundAngle = new SoMFFloat();
	  public final SoMFColor skyColor = new SoMFColor();
	  public final SoMFFloat skyAngle = new SoMFFloat();
	  public final SoMFString backUrl = new SoMFString();
	  public final SoMFString bottomUrl = new SoMFString();
	  public final SoMFString frontUrl = new SoMFString();
	  public final SoMFString leftUrl = new SoMFString();
	  public final SoMFString rightUrl = new SoMFString();
	  public final SoMFString topUrl = new SoMFString();

	  protected final SoSFBool set_bind = new SoSFBool(); // eventIn
	  protected final SoSFBool isBound = new SoSFBool();  // eventOut

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLBackground, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLBackground.class, "VRMLBackground", SoNode.class);
//	  SoGetBoundingBoxAction.addMethod(SoVRMLBackground.getClassTypeId(), TODO 
//	                                    background_bbfix);
//
//	  String env = coin_getenv("COIN_VIEWUP");
//	  if (env) {
//	    float data[3];
//	    int n = sscanf(env, "%f%f%f", &data[0], &data[1], &data[2]);
//	    if (n == 3) {
//	      final SbVec3f v = new SbVec3f(data[0], data[1], data[2]);
//	      v.normalize();
//	      vrmlbackground_viewup[0] = v[0];
//	      vrmlbackground_viewup[1] = v[1];
//	      vrmlbackground_viewup[2] = v[2];
//	      vrmlbackground_viewup_set = TRUE;
//	    }
//	  }
	}

	/*!
	  Constructor.
	*/
	public SoVRMLBackground()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLBackground.class);

	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(groundColor,"groundColor");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(skyColor,"skyColor");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(groundAngle,"groundAngle");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(skyAngle,"skyAngle");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(backUrl,"backUrl");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(bottomUrl,"bottomUrl");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(frontUrl,"frontUrl");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(leftUrl,"leftUrl");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(rightUrl,"rightUrl");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(topUrl,"topUrl");

	  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(set_bind,"set_bind");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isBound,"isBound");

//	  pimpl = new SoVRMLBackgroundP(this);
//	  pimpl.children = new SoChildList(this);
//	  
//	  // Binding sensors 
//	  pimpl.setbindsensor = new SoFieldSensor(background_bindingchangeCB, pimpl);
//	  pimpl.isboundsensor = new SoFieldSensor(background_bindingchangeCB, pimpl);
//	 
//	  pimpl.setbindsensor.attach(this.set_bind);
//	  pimpl.isboundsensor.attach(this.isBound);
//
//	  pimpl.setbindsensor.setPriority(5);
//	  pimpl.isboundsensor.setPriority(5);
//
//	  // Geometry sensors
//	  pimpl.groundanglesensor = new SoFieldSensor(background_geometrychangeCB, pimpl);
//	  pimpl.groundcolorsensor = new SoFieldSensor(background_geometrychangeCB, pimpl);
//	  pimpl.skyanglesensor = new SoFieldSensor(background_geometrychangeCB, pimpl);
//	  pimpl.skycolorsensor = new SoFieldSensor(background_geometrychangeCB, pimpl);
//
//	  pimpl.groundanglesensor.attach(this.groundAngle);
//	  pimpl.groundcolorsensor.attach(this.groundColor);
//	  pimpl.skyanglesensor.attach(this.skyAngle);
//	  pimpl.skycolorsensor.attach(this.skyColor);
//
//	  pimpl.groundanglesensor.setPriority(5); 
//	  pimpl.groundcolorsensor.setPriority(5); 
//	  pimpl.skyanglesensor.setPriority(5);
//	  pimpl.skycolorsensor.setPriority(5);
//
//	  // URL/skybox sensors  
//	  pimpl.backurlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//	  pimpl.fronturlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//	  pimpl.lefturlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//	  pimpl.righturlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//	  pimpl.bottomurlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//	  pimpl.topurlsensor = new SoFieldSensor(background_vrmltexturechangeCB, pimpl);
//
//	  pimpl.backurlsensor.attach(this.backUrl);
//	  pimpl.fronturlsensor.attach(this.frontUrl);
//	  pimpl.lefturlsensor.attach(this.leftUrl);
//	  pimpl.righturlsensor.attach(this.rightUrl);
//	  pimpl.bottomurlsensor.attach(this.bottomUrl);
//	  pimpl.topurlsensor.attach(this.topUrl);
//
//	  pimpl.backurlsensor.setPriority(5);
//	  pimpl.fronturlsensor.setPriority(5);
//	  pimpl.lefturlsensor.setPriority(5);
//	  pimpl.righturlsensor.setPriority(5);
//	  pimpl.bottomurlsensor.setPriority(5);
//	  pimpl.topurlsensor.setPriority(5);
//
//	  pimpl.geometrybuilt = false;  
//	  pimpl.camera = null;
//	  pimpl.rootnode = null;
	}


}
