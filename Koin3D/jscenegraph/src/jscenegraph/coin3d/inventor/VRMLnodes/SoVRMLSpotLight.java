/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLSpotLight extends SoVRMLLight {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLSpotLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLSpotLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLSpotLight.class); }    	  	

	  public final SoSFVec3f location = new SoSFVec3f();
	  public final SoSFVec3f direction = new SoSFVec3f();
	  public final SoSFFloat beamWidth = new SoSFFloat();
	  public final SoSFFloat cutOffAngle = new SoSFFloat();
	  public final SoSFFloat radius = new SoSFFloat();
	  public final SoSFVec3f attenuation = new SoSFVec3f();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLSpotLight, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLSpotLight.class, "VRMLSpotLight", SoVRMLLight.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLSpotLight()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLSpotLight.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(location,"location", new SbVec3f(0.0f, 0.0f, 0.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(direction,"direction", new SbVec3f(0.0f, 0.0f, -1.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(beamWidth,"beamWidth", ((float)(Math.PI)/2.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(cutOffAngle,"cutOffAngle", ((float)(Math.PI)/4.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(radius,"radius", (100.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(attenuation,"attenuation", new SbVec3f(1.0f, 0.0f, 0.0f));
	}

}
