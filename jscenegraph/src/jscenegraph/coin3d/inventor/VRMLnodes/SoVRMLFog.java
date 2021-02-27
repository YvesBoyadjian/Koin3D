/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.elements.SoEnvironmentElement;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLFog extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLFog.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLFog.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLFog.class); }    	  	

	  public final SoSFString fogType = new SoSFString();
	  public final SoSFFloat visibilityRange = new SoSFFloat();
	  public final SoSFColor color = new SoSFColor();

	  protected final
		  SoSFBool set_bind = new SoSFBool();
	protected final 	  SoSFBool isBound = new SoSFBool();


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLFog, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLFog.class, "VRMLFog", SoNode.class);
}

/*!
  Constructor.
*/
public SoVRMLFog()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLFog.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(fogType,"fogType", ("LINEAR"));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(visibilityRange,"visibilityRange", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(color,"color", new SbColor(1.0f, 1.0f, 1.0f));
  
  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(set_bind,"set_bind");
  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isBound,"isBound");

//  pimpl = new SoVRMLFogP(this); TODO
//
//  pimpl.fogType = SoEnvironmentElement.FogType.HAZE; // 'HAZE' == Linear fog
//  pimpl.fogColor = new SbVec3f(1,1,1);
//  pimpl.visibilityRange = 0;
//
//  // Binding sensors 
//  pimpl.setbindsensor = new SoFieldSensor(fog_bindingchangeCB, pimpl);
//  pimpl.isboundsensor = new SoFieldSensor(fog_bindingchangeCB, pimpl);
//  pimpl.setbindsensor.attach(this.set_bind);
//  pimpl.isboundsensor.attach(this.isBound);
//  pimpl.setbindsensor.setPriority(0);
//  pimpl.isboundsensor.setPriority(0);
//
//  // Field sensor
//  pimpl.fogtypesensor = new SoFieldSensor(fog_fieldsensorCB, pimpl);
//  pimpl.fogtypesensor.attach(this.fogType);
//  pimpl.fogtypesensor.setPriority(0);
//
//  pimpl.visibilitysensor = new SoFieldSensor(fog_fieldsensorCB, pimpl);
//  pimpl.visibilitysensor.attach(this.visibilityRange);
//  pimpl.visibilitysensor.setPriority(0);
//
//  pimpl.colorsensor = new SoFieldSensor(fog_fieldsensorCB, pimpl);
//  pimpl.colorsensor.attach(this.color);
//  pimpl.colorsensor.setPriority(0);

}

}
