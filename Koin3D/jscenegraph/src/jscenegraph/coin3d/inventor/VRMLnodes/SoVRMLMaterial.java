/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLMaterial extends SoNode {

	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLMaterial.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLMaterial.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLMaterial.class); }    	  	
	
	  public final SoSFColor diffuseColor = new SoSFColor();
	  public final SoSFFloat ambientIntensity = new SoSFFloat();
	  public final SoSFColor specularColor = new SoSFColor();
	  public final SoSFColor emissiveColor = new SoSFColor();
	  public final SoSFFloat shininess = new SoSFFloat();
	  public final SoSFFloat transparency = new SoSFFloat();	  
	  
	  private SoVRMLMaterialP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVRMLMaterial()
	{
	  pimpl = new SoVRMLMaterialP();

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLMaterial.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(diffuseColor,"diffuseColor", new SbColor(0.8f, 0.8f, 0.8f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(ambientIntensity,"ambientIntensity", (0.2f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(specularColor,"specularColor", new SbColor(0.0f, 0.0f, 0.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(emissiveColor,"emissiveColor", new SbColor(0.0f, 0.0f, 0.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(shininess,"shininess", (0.2f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(transparency,"transparency", (0.0f));
	}

	  
	  
	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLMaterial, SO_VRML97_NODE_TYPE);
	    SO__NODE_INIT_CLASS(SoVRMLMaterial.class, "VRMLMaterial", SoNode.class);
	}

}
