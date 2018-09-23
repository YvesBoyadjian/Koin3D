/**
 * 
 */
package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoSpotLight;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShadowSpotLight extends SoSpotLight {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShadowSpotLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShadowSpotLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShadowSpotLight.class); }    	  	
	
	  public final SoSFNode shadowMapScene = new SoSFNode();
	  public final SoSFFloat nearDistance = new SoSFFloat();
	  public final SoSFFloat farDistance = new SoSFFloat();

	  /*!
	  Constructor.
	*/
	public SoShadowSpotLight()
	{
	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShadowSpotLight.class);
	  nodeHeader.SO_NODE_ADD_FIELD(shadowMapScene,"shadowMapScene", (null));
	  nodeHeader.SO_NODE_ADD_FIELD(nearDistance,"nearDistance", (-1.0f));
	  nodeHeader.SO_NODE_ADD_FIELD(farDistance,"farDistance", (-1.0f));
	}

	  
	  public static void
	  initClass()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
	      SO__NODE_INIT_CLASS(SoShadowSpotLight.class, "ShadowSpotLight", SoSpotLight.class);
	  }

}
