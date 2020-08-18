/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.elements.SoTextureScalePolicyElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.inventor.threads.SbMutex;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFImage;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.Destroyable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTexture2 extends SoTexture {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTexture2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTexture2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTexture2.class); }    
	
	  
public
 //! Texture model
 enum Model {
     MODULATE                ( SoMultiTextureImageElement.Model.MODULATE.getValue()),
     DECAL                   ( SoMultiTextureImageElement.Model.DECAL.getValue()),
     BLEND                   ( SoMultiTextureImageElement.Model.BLEND.getValue()),
 	REPLACE					( SoMultiTextureImageElement.Model.REPLACE.getValue());
     
     Model(int value) {
     	this.value = value;
     }
     
     private int value;
     
     public int getValue() {
     	return value;
     }
 };

 //! Texture wrap type
 public enum Wrap {
     REPEAT                  ( SoMultiTextureImageElement.Wrap.REPEAT.getValue()),
     CLAMP                   ( SoMultiTextureImageElement.Wrap.CLAMP.getValue());
     
     Wrap(int value) {
     	this.value = value;
     }
     private int value;
     
     public int getValue() {
     	return value;
     }

	public static Wrap fromValue(Integer value2) {
		if(value2 == REPEAT.getValue()) return REPEAT;
		if(value2 == CLAMP.getValue()) return CLAMP;
		return null;
	}
 };
 
 public final SoSFString filename = new SoSFString();
 public final SoSFImage image = new SoSFImage();
 public final SoSFEnum wrapS = new SoSFEnum();
 public final SoSFEnum wrapT = new SoSFEnum();
 public final SoSFEnum model = new SoSFEnum();
 public final SoSFColor blendColor = new SoSFColor();
 public final SoSFBool enableCompressedTexture = new SoSFBool();

private SoTexture2P pimpl;


static class SoTexture2P implements Destroyable {
public
  int readstatus;
  public SoGLImage glimage; //ptr
  public boolean glimagevalid;
  public SoFieldSensor filenamesensor; // ptr

  public static SbMutex mutex;

  public static void cleanup() {
    Destroyable.delete(SoTexture2P.mutex);
    SoTexture2P.mutex = null;
  }

@Override
public void destructor() {
	glimage = null;
	filenamesensor = null;
}
};

//SbMutex * SoTexture2P.mutex = null;

//#define PRIVATE(p) ((p).pimpl)

// *************************************************************************

//#ifdef COIN_THREADSAFE
//#define LOCK_GLIMAGE(_thisp_) (PRIVATE(_thisp_).mutex.lock())
//#define UNLOCK_GLIMAGE(_thisp_) (PRIVATE(_thisp_).mutex.unlock())
//#else // COIN_THREADSAFE
//#define LOCK_GLIMAGE(_thisp_)
//#define UNLOCK_GLIMAGE(_thisp_)
//#endif // COIN_THREADSAFE

// *************************************************************************

//SO_NODE_SOURCE(SoTexture2);

/*!
  Constructor.
*/
public SoTexture2()
{
  this.pimpl = new SoTexture2P();

  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoTexture2.class);

  nodeHeader.SO_NODE_ADD_FIELD(filename,"filename", (""));
  SbImage img = new SbImage();
  img.setValuePtr(new SbVec2s((short)0, (short)0), 0, null);
  nodeHeader.SO_NODE_ADD_FIELD(image,"image", img);
  nodeHeader.SO_NODE_ADD_FIELD(wrapS,"wrapS", (Wrap.REPEAT.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(wrapT,"wrapT", (Wrap.REPEAT.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(model,"model", (Model.MODULATE.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(blendColor,"blendColor", new SbColor(0.0f, 0.0f, 0.0f));
  nodeHeader.SO_NODE_ADD_FIELD(enableCompressedTexture,"enableCompressedTexture", (false));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.REPEAT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.CLAMP);

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapS,"wrapS", "Wrap");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapT,"wrapT", "Wrap");

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.MODULATE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.DECAL);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.BLEND);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.REPLACE);
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(model,"model", "Model");

  this.pimpl.glimage = null;
  this.pimpl.glimagevalid = false;
  this.pimpl.readstatus = 1;

  // use field sensor for filename since we will load an image if
  // filename changes. This is a time-consuming task which should
  // not be done in notify().
  this.pimpl.filenamesensor = new SoFieldSensor(SoTexture2::filenameSensorCB, this);
  this.pimpl.filenamesensor.setPriority(0);
  this.pimpl.filenamesensor.attach(this.filename);
}

/*!
  Destructor. Frees up internal resources used to store texture image
  data.
*/
public void destructor()
{
  if (this.pimpl.glimage != null) this.pimpl.glimage.unref(null);
  Destroyable.delete( this.pimpl.filenamesensor);
  Destroyable.delete( this.pimpl);
  super.destructor();
}

// Documented in superclass.
public static void
initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoTexture2, SO_FROM_INVENTOR_1|SoNode.VRML1);
	SoSubNode.SO__NODE_INIT_CLASS(SoTexture2.class, "Texture2", SoTexture.class);

  SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureImageElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureEnabledElement.class);

  SO_ENABLE(SoCallbackAction.class, SoMultiTextureEnabledElement.class);
  SO_ENABLE(SoCallbackAction.class, SoMultiTextureImageElement.class);

  SO_ENABLE(SoRayPickAction.class, SoMultiTextureEnabledElement.class);
  SO_ENABLE(SoRayPickAction.class, SoMultiTextureImageElement.class);

//#ifdef COIN_THREADSAFE
  SoTexture2P.mutex = new SbMutex();
//#endif // COIN_THREADSAFE

  //coin_atexit(SoTexture2P.cleanup, CC_ATEXIT_NORMAL);
}


// Documented in superclass. Overridden to check if texture file (if
// any) can be found and loaded.
public boolean
readInstance(SoInput in, short flags)
{
  this.pimpl.filenamesensor.detach();
  boolean readOK = super.readInstance(in, flags);
  this.setReadStatus((int) (readOK? 1 : 0));
  if (readOK && !filename.isDefault() && !filename.getValue().equals("")) {
    if (!this.loadFilename()) {
      SoReadError.post(in, "Could not read texture file '"+filename.getValue()+"'");
      this.setReadStatus(false ? 1 : 0);
    }
  }
  this.pimpl.filenamesensor.attach(this.filename);
  return readOK;
}

static SoGLImage.Wrap
translateWrap( SoTexture2.Wrap wrap)
{
  if (wrap == SoTexture2.Wrap.REPEAT) return SoGLImage.Wrap.REPEAT;
  return SoGLImage.Wrap.CLAMP;
}

static int didwarn = 0;

// Documented in superclass.
public void
GLRender(SoGLRenderAction action)
{
  SoState state = action.getState();
  int unit = SoTextureUnitElement.get(state);
  
  if ((unit == 0) && SoTextureOverrideElement.getImageOverride(state))
    return;

  float quality = SoTextureQualityElement.get(state);

  cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));
  SoTextureScalePolicyElement.Policy scalepolicy =
    SoTextureScalePolicyElement.get(state);
  boolean needbig = (scalepolicy == SoTextureScalePolicyElement.Policy.FRACTURE);
  SoType glimagetype = this.pimpl.glimage != null ? this.pimpl.glimage.getTypeId() : SoType.badType();
    
  this.pimpl.mutex.lock();//LOCK_GLIMAGE(this);
  
  if (!this.pimpl.glimagevalid || 
      (needbig && glimagetype.operator_not_equal(SoGLBigImage.getClassTypeId())) ||
      (!needbig && glimagetype.operator_not_equal(SoGLImage.getClassTypeId()))) {
    final int[] nc = new int[1];
    final SbVec2s size = new SbVec2s();
    MemoryBuffer bytes =
      this.image.getValue(size, nc);
    
    if (needbig &&
        (glimagetype.operator_not_equal(SoGLBigImage.getClassTypeId()))) {
      if (this.pimpl.glimage != null) this.pimpl.glimage.unref(state);
      this.pimpl.glimage = new SoGLBigImage();
    }
    else if (!needbig &&
             (glimagetype.operator_not_equal(SoGLImage.getClassTypeId()))) {
      if (this.pimpl.glimage != null) this.pimpl.glimage.unref(state);
      this.pimpl.glimage = new SoGLImage();
    }
    
    if (this.enableCompressedTexture.getValue()) {
      this.pimpl.glimage.setFlags(this.pimpl.glimage.getFlags()|
                                       SoGLImage.Flags.COMPRESSED.getValue());
    }

    if (scalepolicy == SoTextureScalePolicyElement.Policy.SCALE_DOWN) {
      this.pimpl.glimage.setFlags(this.pimpl.glimage.getFlags()|SoGLImage.Flags.SCALE_DOWN.getValue());
    }

    if (bytes != null && size.operator_not_equal(new SbVec2s((short)0,(short)0))) {
      this.pimpl.glimage.setData(bytes, size, nc[0],
                             translateWrap(Wrap.fromValue(this.wrapS.getValue())),
                             translateWrap(Wrap.fromValue(this.wrapT.getValue())),
                             quality);
      this.pimpl.glimagevalid = true;
      // don't cache while creating a texture object
      SoCacheElement.setInvalid(true);
      if (state.isCacheOpen()) {
        SoCacheElement.invalidate(state);
      }
    }
  }

  if (this.pimpl.glimage != null && this.pimpl.glimage.getTypeId().operator_equal_equal(SoGLBigImage.getClassTypeId())) {
    SoCacheElement.invalidate(state);
  }

  this.pimpl.mutex.unlock();//UNLOCK_GLIMAGE(this);
  
  SoMultiTextureImageElement.Model glmodel = SoMultiTextureImageElement.Model.fromValue( 
    this.model.getValue());
  
  if (glmodel == SoMultiTextureImageElement.Model.REPLACE) {
    if (!SoGL.cc_glglue_glversion_matches_at_least(glue, 1, 1, 0)) {
      if (didwarn==0) {
        SoDebugError.postWarning("SoTexture2.GLRender",
                                  "Unable to use the GL_REPLACE texture model. "+
                                  "Your OpenGL version is < 1.1. "+
                                  "Using GL_MODULATE instead.");
        didwarn = 1;
      }
      // use MODULATE and not DECAL, since DECAL only works for RGB
      // and RGBA textures
      glmodel = SoMultiTextureImageElement.Model.MODULATE;
    }
  }
  
  int maxunits = SoGL.cc_glglue_max_texture_units(glue);
  if (unit < maxunits) {
    SoGLMultiTextureImageElement.set(state, this, unit,
                                      this.pimpl.glimagevalid ? this.pimpl.glimage : null,
                                      glmodel,
                                      this.blendColor.getValue());
    
    SoGLMultiTextureEnabledElement.set(state, this, unit,
                                        this.pimpl.glimagevalid &&
                                        quality > 0.0f);
  }
  else {
    // we already warned in SoTextureUnit. I think it's best to just
    // ignore the texture here so that all texture for non-supported
    // units will be ignored. pederb, 2003-11-04
  }
}

static MemoryBuffer dummytex = MemoryBuffer.allocateBytes(4,(byte)0xff);// {(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
// Documented in superclass.
public void
SoTexture2_doAction(SoAction action)
{
  SoState state = action.getState();

  int unit = SoTextureUnitElement.get(state);
  if ((unit == 0) && SoTextureOverrideElement.getImageOverride(state))
    return;

  final int[] nc = new int[1];
  final SbVec2s size = new SbVec2s();
  MemoryBuffer bytes = this.image.getValue(size, nc);
  
  // if a filename has been set, but the file has not been loaded, supply
  // a dummy texture image to make sure texture coordinates are generated.
  if ((size.operator_equal_equal(new SbVec2s((short)0,(short)0))) &&
      this.image.isDefault() && 
      this.filename.getValue().length() != 0) {
    bytes = dummytex;
    size.copyFrom( new SbVec2s((short)2,(short)2));
    nc[0] = 1;
  }

  if (size.operator_not_equal(new SbVec2s((short)0,(short)0))) {
    SoMultiTextureImageElement.set(state, this, unit,
                                    size, nc[0], bytes,
                                    SoMultiTextureImageElement.Wrap.fromValue(this.wrapT.getValue()),
                                    SoMultiTextureImageElement.Wrap.fromValue(this.wrapS.getValue()),
                                    SoMultiTextureImageElement.Model.fromValue(model.getValue()),
                                    this.blendColor.getValue());
    SoMultiTextureEnabledElement.set(state, this, unit, true); 
  }
  else {
    SoMultiTextureImageElement.setDefault(state, this, unit);
    SoMultiTextureEnabledElement.set(state, this, unit, false);
  }
}

// doc from parent
public void
callback(SoCallbackAction action)
{
  SoTexture2_doAction(action);
}

// doc from parent
public void
rayPick(SoRayPickAction action)
{
  SoTexture2_doAction(action);
}

/*!
  Not implemented in Coin; should probably not have been public in the
  original SGI Open Inventor API.  We'll consider to implement it if
  requested.
*/
public boolean
readImage(String fname, int w, int h, int nc,
                      byte[] bytes)
{
  //COIN_OBSOLETED();
  return false;
}

/*!
  Returns read status. 1 for success, 0 for failure.
*/
public int
getReadStatus()
{
  return this.pimpl.readstatus;
}

/*!
  Sets read status.
  \sa getReadStatus()
 */
public void
setReadStatus(int s)
{
  this.pimpl.readstatus = s;
}

// Documented in superclass. Overridden to detect when fields change.
public void notify(SoNotList l)
{
  SoField f = l.getLastField();
  if (f == this.image) {
    this.pimpl.glimagevalid = false;

    // write image, not filename
    this.filename.setDefault(true);
    this.image.setDefault(false);
  }
  else if (f == this.wrapS || f == this.wrapT) {
    this.pimpl.glimagevalid = false;
  }
  super.notify(l);
}

//
// Called from readInstance() or when user changes the
// filename field.
//
public boolean
loadFilename()
{
  boolean retval = false;
  if (this.filename.getValue().length() != 0) {
    final SbImage tmpimage = new SbImage();
    SbStringList sl = SoInput.getDirectories();
    if (tmpimage.readFile(this.filename.getValue(),
                          sl.getArrayPtr(), sl.getLength())) {
      final int[] nc = new int[1];
      final SbVec2s size = new SbVec2s();
      MemoryBuffer bytes = tmpimage.getValue(size, nc);
      // disable notification on image while setting data from filename
      // as a notify will cause a filename.setDefault(TRUE).
      boolean oldnotify = this.image.enableNotify(false);
      this.image.setValue(size, nc[0], bytes);
      this.image.enableNotify(oldnotify);
      this.pimpl.glimagevalid = false; // recreate GL image in next GLRender()
      retval = true;
    }
    Destroyable.delete(tmpimage);
  }
  this.image.setDefault(true); // write filename, not image
  return retval;
}

//
// called when filename changes
//
public static void
filenameSensorCB(Object data, SoSensor sensor)
{
  SoTexture2 thisp = (SoTexture2) data;

  thisp.setReadStatus(1);
  if (thisp.filename.getValue().length() != 0 &&
      !thisp.loadFilename()) {
    SoDebugError.postWarning("SoTexture2.filenameSensorCB",
                              "Image file '"+thisp.filename.getValue()+"' could not be read");
    thisp.setReadStatus(0);
  }
  else if (thisp.filename.getValue().equals("")) {
    // setting filename to "" should reset the node to its initial state
    thisp.setReadStatus(0);
    thisp.image.setValue(new SbVec2s((short)0,(short)0), 0, null);
    thisp.image.setDefault(true);
    thisp.filename.setDefault(true);
  }
}

//#undef LOCK_GLIMAGE
//#undef UNLOCK_GLIMAGE
//#undef PRIVATE

}
