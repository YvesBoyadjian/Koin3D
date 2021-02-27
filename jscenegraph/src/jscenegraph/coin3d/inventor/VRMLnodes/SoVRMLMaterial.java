/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.FloatArray;
import jscenegraph.port.SbColorArray;

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

    static int didwarn = 0;

// Doc in parent
public void
SoVRMLMaterial_doAction(SoAction action)
{
  SoState state = action.getState();

  int bitmask = 0;
  int flags = SoOverrideElement.getFlags(state);

//#define TEST_OVERRIDE(bit) ((SoOverrideElement::bit & flags) != 0)

  if (!this.diffuseColor.isIgnored() &&
      !((SoOverrideElement.ElementMask.AMBIENT_COLOR.getValue() & flags) != 0 )) {
    pimpl.tmpambient.setValue(this.diffuseColor.getValue());
    if (!this.ambientIntensity.isIgnored())
      pimpl.tmpambient.operator_mul_equal(this.ambientIntensity.getValue());
    bitmask |= SoLazyElement.masks.AMBIENT_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setAmbientColorOverride(state, this, true);
    }
  }
  if (!this.diffuseColor.isIgnored() &&
      !((SoOverrideElement.ElementMask.DIFFUSE_COLOR.getValue() & flags) != 0)) {
    // Note: the override flag bit values for diffuseColor and
    // transparency are equal (done like that to match SGI/TGS
    // Inventor behavior), so overriding one will also override the
    // other.
    bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setDiffuseColorOverride(state, this, true);
    }
  }
  if (!this.emissiveColor.isIgnored() &&
      !((SoOverrideElement.ElementMask.EMISSIVE_COLOR.getValue() & flags) != 0)) {

    bitmask |= SoLazyElement.masks.EMISSIVE_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setEmissiveColorOverride(state, this, true);
    }

  }
  if (!this.specularColor.isIgnored() &&
      !((SoOverrideElement.ElementMask.SPECULAR_COLOR.getValue() & flags) != 0)) {
    bitmask |= SoLazyElement.masks.SPECULAR_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setSpecularColorOverride(state, this, true);
    }
  }
  if (!this.shininess.isIgnored() &&
      !((SoOverrideElement.ElementMask.SHININESS.getValue() & flags) != 0)) {
    bitmask |= SoLazyElement.masks.SHININESS_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setShininessOverride(state, this, true);
    }
  }
  if (!this.transparency.isIgnored() &&
      !((SoOverrideElement.ElementMask.TRANSPARENCY.getValue() & flags) != 0)) {
    pimpl.tmptransparency = this.transparency.getValue();
    bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
    // Note: the override flag bit values for diffuseColor and
    // transparency are equal (done like that to match SGI/TGS
    // Inventor behavior), so overriding one will also override the
    // other.
    if (this.isOverride()) {
      SoOverrideElement.setTransparencyOverride(state, this, true);
    }
  }
//#undef TEST_OVERRIDE

  if (bitmask != 0) {
//#if COIN_DEBUG
    if ((bitmask & SoLazyElement.masks.SHININESS_MASK.getValue())!=0) {
      if ( didwarn == 0 && (this.shininess.getValue() < 0.0f || this.shininess.getValue() > 1.0f)) {
        SoDebugError.postWarning("SoMaterial::GLRender",
                                  "Shininess out of range [0-1]. "+
                                  "The shininess value will be clamped. "+
                                  "This warning will be printed only once, but there might be more errors. "+
                                  "You should check and fix your code and/or VRML exporter.");
        didwarn = 1;
      }
    }
//#endif // COIN_DEBUG
    
    SbColorArray dummyDiffuse = SbColorArray.allocate(1);
    dummyDiffuse.get(0).setValue(this.diffuseColor.getValue());
    
    SoLazyElement.setMaterials(state, this, bitmask,
                                pimpl.getColorPacker(),
                                dummyDiffuse/*this.diffuseColor.getValue()*/, 1,
                                FloatArray.wrap(pimpl.tmptransparency), 1,
                                pimpl.tmpambient,
                                this.emissiveColor.getValue(),
                                this.specularColor.getValue(),
                                SbBasic.SbClamp(this.shininess.getValue(), 0.0f, 1.0f),
                                pimpl.tmptransparency > 0.0f);
  }
}

// Doc in parent
public void GLRender(SoGLRenderAction action)
{
  SoVRMLMaterial_doAction(action);
}

// Doc in parent
public void callback(SoCallbackAction action)
{
  SoVRMLMaterial_doAction(action);
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
