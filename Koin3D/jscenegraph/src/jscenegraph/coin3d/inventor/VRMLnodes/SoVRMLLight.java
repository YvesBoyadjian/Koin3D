/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLLight extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLLight.class); }    	  	

	  public final SoSFBool on = new SoSFBool();
	  public final SoSFFloat intensity = new SoSFFloat();
	  public final SoSFColor color = new SoSFColor();
	  public final SoSFFloat ambientIntensity = new SoSFFloat();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLLight, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLLight.class, "VRMLLight", SoNode.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLLight()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLLight.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(on,"on", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(intensity,"intensity", (1.0f));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(color,"color", new SbColor(1.0f, 1.0f, 1.0f));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(ambientIntensity,"ambientIntensity", (0.0f));
	}

}
