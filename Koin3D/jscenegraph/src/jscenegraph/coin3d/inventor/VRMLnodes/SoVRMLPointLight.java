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
public class SoVRMLPointLight extends SoVRMLLight {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLPointLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLPointLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLPointLight.class); }    	  	

	  public final SoSFVec3f location = new SoSFVec3f();
	  public final SoSFFloat radius = new SoSFFloat();
	  public final SoSFVec3f attenuation = new SoSFVec3f();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLPointLight, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLPointLight.class, "VRMLPointLight", SoVRMLLight.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLPointLight()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLPointLight.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(location,"location", new SbVec3f(0.0f, 0.0f, 0.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(radius,"radius", (100.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(attenuation,"attenuation", new SbVec3f(1.0f, 0.0f, 0.0f));
	}

}
