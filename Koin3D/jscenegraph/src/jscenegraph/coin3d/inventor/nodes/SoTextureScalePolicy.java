/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.SoTextureScalePolicyElement;
import jscenegraph.coin3d.inventor.elements.SoTextureScaleQualityElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureScalePolicy extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureScalePolicy.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureScalePolicy.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureScalePolicy.class); }    	  	
	
	  public enum Policy {
		    USE_TEXTURE_QUALITY,
		    SCALE_DOWN,
		    SCALE_UP,
		    FRACTURE;
		  
		  public int getValue() {
			  return ordinal();
		  }

		public static Policy fromValue(Integer value) {
			return Policy.values()[value];
		}
		  };

		  public final SoSFEnum policy = new SoSFEnum();
		  public final SoSFFloat quality = new SoSFFloat();

/*!
  Constructor.
*/
public SoTextureScalePolicy()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoTextureScalePolicy.class);
  nodeHeader.SO_NODE_ADD_FIELD(policy,"policy", (SoTextureScalePolicy.Policy.USE_TEXTURE_QUALITY.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(quality,"quality", (0.5f));
  
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.USE_TEXTURE_QUALITY);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.SCALE_DOWN);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.SCALE_UP);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.FRACTURE);
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(policy,"policy", "Policy");
}

/*!
  Destructor.
*/
public void destructor ()
{
	super.destructor();
}

// Doc from superclass.
public static void
initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoTextureScalePolicy, SO_FROM_COIN_2_0);
	SoSubNode.SO__NODE_INIT_CLASS(SoTextureScalePolicy.class, "TextureScalePolicy", SoNode.class);
  SO_ENABLE(SoGLRenderAction.class, SoTextureScalePolicyElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoTextureScaleQualityElement.class);
}

public static SoTextureScalePolicyElement.Policy
convert_policy( SoTextureScalePolicy.Policy policy)
{
  switch (policy) {
  default:
    assert(false);// && "unknown policy");
  case USE_TEXTURE_QUALITY:
    return SoTextureScalePolicyElement.Policy.USE_TEXTURE_QUALITY;
    //break;
  case SCALE_DOWN:
    return SoTextureScalePolicyElement.Policy.SCALE_DOWN;
    //break;
  case SCALE_UP:
    return SoTextureScalePolicyElement.Policy.SCALE_UP;
    //break;
  case FRACTURE:
    return SoTextureScalePolicyElement.Policy.FRACTURE;
    //break;
  }
  // needed for gcc 4.0.1 (Mac OS X)
  //return SoTextureScalePolicyElement.Policy.USE_TEXTURE_QUALITY;
}

// Doc from superclass.
public void
GLRender(SoGLRenderAction action)
{
  if (!this.policy.isIgnored()) {
    SoTextureScalePolicyElement.set(action.getState(), this, 
                                     convert_policy(Policy.fromValue(this.policy.getValue())));
  }
  if (!this.quality.isIgnored()) {
    SoTextureScaleQualityElement.set(action.getState(), this,
                                      this.quality.getValue());
  }
}
		  
}
