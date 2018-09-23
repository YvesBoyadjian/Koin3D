
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

// WARNING: This is work in progress. Do not expect this class to have
// a stable interface over a long period of time. It is installed
// because we need it in an internal project.

// FIXME: make sure we do a design review for this class for Coin v3.0
// pederb, 2001-11-28

package jscenegraph.coin3d.inventor.misc;

import java.util.Objects;
import java.util.function.Consumer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.glue.Gl;
import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.threads.SbStorage;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */

// FIXME: in my not so humble opinion, this class is an ugly mess --
// or at least its interface.
//
// Some examples: there are *4* functions named "setData()" -- which
// should throw up a huge red warning sign alone. Methods named
// "setData()" and "setFlags()" (the latter which we also have in
// SoGLImage) should make API designers cringe.  There's a whole bunch
// of *public* methods marked as being *private* and *internal* --
// another warning sign. The setData() method that creates a 2D
// texture object simply calls into the method that makes a 3D texture
// object, while the obvious right thing to do would be to refactor
// common code to private methods.
//
// Since this was made part of the public API of Coin 2, I guess we'll
// have to support it until the end of time, or at least a couple of
// major versions down the road. What we should do with it is to mark
// it as obsolete, then refactor the functionality out of it,
// preferably into a nice, *clean* little C API to complement the
// stuff contained in cc_glglue_*(), then convert all internal code to
// use that instead.
//
// 20030312 mortene.

// *************************************************************************

// FIXME: Add TEX3 enviroment variables (or general TEX variables?)
// (kintel 20011112)

// *************************************************************************

/*!
  \class SoGLImage include/Inventor/misc/SoGLImage.h
  \brief The SoGLImage class is used to handle OpenGL 2D/3D textures.

  A number of environment variables can be set to control how textures
  are created. This is useful to tune Coin to fit your system. E.g. if you
  are running on a laptop, it might be a good idea to disable linear
  filtering and mipmaps.

  \li COIN_TEX2_LINEAR_LIMIT: Linear filtering is enabled if
  Complexity::textureQuality is greater or equal to this
  value. Default value is 0.2.

  \li COIN_TEX2_MIPMAP_LIMIT: Mipmaps are created if textureQuality is
  greater or equal to this value. Default value is 0.5.

  \li COIN_TEX2_LINEAR_MIPMAP_LIMIT: Linear filtering between mipmap
  levels is enabled if textureQuality is greater or equal to this
  value. Default value is 0.8.

  \li COIN_TEX2_SCALEUP_LIMIT: Textures with width or height not equal
  to a power of two will always be scaled up if textureQuality is
  greater or equal to this value.  Default value is 0.7. If
  textureQuality is lower than this value, and the width or height is
  larger than 256 pixels, the texture is only scaled up if it's
  relatively close to the next power of two size. This could save a
  lot of texture memory.

  \li COIN_TEX2_USE_GLTEXSUBIMAGE: When set, and when the new texture
  data has the same attributes as the old data, glTexSubImage() will
  be used to copy new data into the texture instead of recreating the
  texture.  This is not enabled by default, since it seems to trigger
  a bug in the Linux nVidia drivers. It just happens in some
  unreproducable cases.  It could be a bug in our glTexSubImage()
  code, of course. :)

  \li COIN_TEX2_USE_SGIS_GENERATE_MIPMAP: When set, use the
  GL_SGIS_generate_mip extension (if available) to generate mipmaps,
  otherwise use a fast internal routine to generate them. Use of
  SGIS_generate_mipmap is not enabled by default since we suspect some
  ATi drivers have problems with this extensions.

  \li COIN_ENABLE_CONFORMANT_GL_CLAMP: When set, GL_CLAMP will be used
  when SoGLImage::CLAMP is specified as the texture wrap mode. By
  default GL_CLAMP_TO_EDGE is used, since this is usually what people
  want.  See
  http://www.opengl.org/discussion_boards/ubb/Forum3/HTML/007306.html
  for a discussion regarding GL_CLAMP and GL_CLAMP_TO_EDGE.

  \li COIN_TEX2_ANISOTROPIC_LIMIT: Anisotropic filtering is enabled
  for textures when the texture quality is higher than this value.
  Default value is 0.85

  \COIN_CLASS_EXTENSION

  \since Coin 2.0 */

// *************************************************************************

/*!
  \enum SoGLImage::Wrap

  Used to specify how texture coordinates < 0.0 and > 1.0 should be handled.
  It can either be repeated (REPEAT), clamped (CLAMP) or clamped to edge
  (CLAMP_TO_EDGE), which is useful when tiling textures. Since 2002-11-18,
  CLAMP will be treated as CLAMP_TO_EDGE. The environment variable
  COIN_ENABLE_CONFORMANT_GL_CLAMP can be used to override this behaviour.
*/

/*!
  \enum SoGLImage::ResizeReason

  Sent as a parameter to SoGLImageResizeCB as a hint to why an image
  is being resized. IMAGE means that a whole image is being initially
  resized (e.g. a texture image). SUBIMAGE and MIPMAP are not in use and
  reserved for future use.
*/

/*!
  \enum SoGLImage::Flags

  Can be used to tune/optimize the GL texture handling. Normally the
  texture quality will be used to decide scaling and filtering, and
  the image data will be scanned to decide if the image is (partly)
  transparent, and if the texture can be rendered using the cheaper
  alpha test instead of blending if it does contain transparency. If
  you know the contents of your texture image, or if you have special
  requirements on how the texture should be rendered, you can set the
  flags using the SoGLImage::setFlags() method.

*/

// FIXME: Support other reason values than IMAGE (kintel 20050531)
/*!
  \typedef bool SoGLImage::SoGLImageResizeCB(SoState * state,
                                             const SbVec3s &newsize,
                                             unsigned char * destbuffer,
                                             SoGLImage::ResizeReason reason,
                                             void * closure,
                                             class SoGLImage * image)

  Image resize callback type.
  If registered using setResizeCallback(), this function will be called
  whenever Coin needs to resize an image. The function will be called
  both for 2D and 3D images.

  \e state is the current state at the time of resizing.
  \e newsize is the requested new image size. Note that the z size of a
  2D image is 0.
  \e destbuffer is a pre-allocated buffer big enough to hold the pixels
  for the resized image. The # of bytes per pixel is the same as for the
  original image.
  \e reason is a hint about why the image is resized. At the moment,
  only IMAGE is supported.
  \e image is the original image.

  Return value: true if the resize ahs been resized, false if not.
  If false is returned, Coin will resize the image instead.
*/

// *************************************************************************

public class SoGLImage {
	
  public enum Wrap {
    REPEAT /*= 0*/,
    CLAMP,
    CLAMP_TO_EDGE,
    CLAMP_TO_BORDER
  };

  public enum ResizeReason {
    IMAGE /*= 0*/,
    SUBIMAGE,
    MIPMAP
  };
	
// *************************************************************************

private static float DEFAULT_LINEAR_LIMIT = 0.2f;
private static float DEFAULT_MIPMAP_LIMIT = 0.5f;
private static float DEFAULT_LINEAR_MIPMAP_LIMIT = 0.8f;
private static float DEFAULT_SCALEUP_LIMIT = 0.7f;
private static float DEFAULT_ANISOTROPIC_LIMIT = 0.85f;

private static float COIN_TEX2_LINEAR_LIMIT = -1.0f;
private static float COIN_TEX2_MIPMAP_LIMIT = -1.0f;
private static float COIN_TEX2_LINEAR_MIPMAP_LIMIT = -1.0f;
private static float COIN_TEX2_SCALEUP_LIMIT = -1.0f;
private static float COIN_TEX2_ANISOTROPIC_LIMIT = -1.0f;
private static int COIN_TEX2_USE_GLTEXSUBIMAGE = -1;
private static int COIN_TEX2_USE_SGIS_GENERATE_MIPMAP = -1;
private static int COIN_ENABLE_CONFORMANT_GL_CLAMP = -1;

// *************************************************************************

//buffer used for creating mipmap images
static SbStorage glimage_bufferstorage = null;

private static class soglimage_buffer {
  byte[] buffer;
  int buffersize;
  byte[] mipmapbuffer;
  int mipmapbuffersize;
}; 



	private static final SoType classTypeId = new SoType();
  private static int current_glimageid;
	
  private Object pbuffer;
  private SbImage image;
  private final SbImage dummyimage = new SbImage();
  private final SbVec3s glsize = new SbVec3s();
  private int glcomp;

  private boolean needtransparencytest;
  private boolean hastransparency;
  private boolean usealphatest;
  private int flags;
  private float quality;

  private SoGLImage.Wrap wraps;
  private SoGLImage.Wrap wrapt;
  private SoGLImage.Wrap wrapr;
  private int border;
  private boolean isregistered;
  private int imageage;
  private Consumer endframecb;
  
  private SoGLImage owner;
  private int glimageid;
  
//
// Texture resource management.
//
// FIXME: consider sorting images on an LRU strategy to
// speed up the process of searching for GL-images to free.
//

  private static SbList <SoGLImage> glimage_reglist;
  private static int glimage_maxage = 60;

  private class dldata {
  public
    dldata() {
      dlist = null; age = 0;  }
    public dldata(SoGLDisplayList dl) {
      dlist = dl;
        age = 0; }
    public dldata(final dldata  org) {
      dlist = org.dlist;
        age = org.age; }
    SoGLDisplayList dlist;
    int age;
  };

  

  private final SbList<dldata> /*<dldata>*/ dlists = new SbList();

  // these flags can be used to set texture properties.
  public enum Flags {
    // mipmap, scaling and filtering settings
    SCALE_DOWN (                0x0001),
    NO_MIPMAP (                 0x0002),
    LINEAR_MAG_FILTER (         0x0004),
    LINEAR_MIN_FILTER (         0x0008),
    LINEAR_MIPMAP_FILTER (      0x0010),

    // use if you know your image properties.
    FORCE_TRANSPARENCY_TRUE   ( 0x0020),
    FORCE_TRANSPARENCY_FALSE  ( 0x0040),
    FORCE_ALPHA_TEST_TRUE     ( 0x0080),
    FORCE_ALPHA_TEST_FALSE    ( 0x0100),

    INVINCIBLE                ( 0x0200), // never die of old age

    // use GL_NV_texture_rectangle or GL_EXT_texture_rectangle
    RECTANGLE                 ( 0x0400),

    // Compress texture if available from OpenGL
    COMPRESSED                ( 0x0800),

    // use quality value to decide mipmap, filtering and scaling. This
    // is the default.
    USE_QUALITY_VALUE         ( 0X8000);
    
    private int value;
    Flags(int value) {
    	this.value = value;
    }
	public int getValue() {
		return value;
	}
  };


	/**
	 * 
	 */
	public SoGLImage() {
		  assert(this.isregistered == false);
		  this.image = null;
		  this.pbuffer = null;
		  this.glsize.setValue((short)0,(short)0,(short)0);
		  this.glcomp = 0;
		  this.wraps = SoGLImage.Wrap.CLAMP;
		  this.wrapt = SoGLImage.Wrap.CLAMP;
		  this.wrapr = SoGLImage.Wrap.CLAMP;
		  this.border = 0;
		  this.flags = SoGLImage.Flags.USE_QUALITY_VALUE.getValue();
		  this.needtransparencytest = true;
		  this.hastransparency = false;
		  this.usealphatest = false;
		  this.quality = 0.4f;
		  this.imageage = 0;
		  this.endframecb = null;
		  this.glimageid = 0; // glimageid 0 is an empty image

	      COIN_TEX2_LINEAR_LIMIT = DEFAULT_LINEAR_LIMIT;
	      COIN_TEX2_MIPMAP_LIMIT = DEFAULT_MIPMAP_LIMIT;
	      COIN_TEX2_LINEAR_MIPMAP_LIMIT = DEFAULT_LINEAR_MIPMAP_LIMIT;
	      COIN_TEX2_SCALEUP_LIMIT = DEFAULT_SCALEUP_LIMIT;
	      COIN_TEX2_USE_GLTEXSUBIMAGE = 0;
	      COIN_TEX2_USE_SGIS_GENERATE_MIPMAP = 0;
	      COIN_ENABLE_CONFORMANT_GL_CLAMP = 0;
	      COIN_TEX2_ANISOTROPIC_LIMIT = DEFAULT_ANISOTROPIC_LIMIT;
	}

/*!
  This class has a private destuctor since we want users to supply
  the current GL state when deleting the image. This is to make sure
  gl texture objects are freed as soon as possible. If you supply
  null to this method, the gl texture objects won't be deleted
  until the next time an GLRenderAction is applied in the image's
  cache context(s).
*/
public void
unref(SoState state)
{
  if (this.pbuffer != null) this.setPBuffer(state, null);
  this.unrefDLists(state);
  destructor();
}

/*!
  Destructor.
*/
public void destructor()
{
  SoContextHandler.removeContextDestructionCallback(SoGLImage::contextCleanup, this);
  if (this.isregistered) SoGLImage.unregisterImage(this);
  this.unrefDLists(null);
  //delete PRIVATE(this);
}


// used internally to keep track of the SoGLImages
private static void
registerImage(SoGLImage image)
{
  //LOCK_GLIMAGE;
  if (glimage_reglist == null) {
    //coin_atexit((coin_atexit_f *)regimage_cleanup, CC_ATEXIT_NORMAL);
    glimage_reglist = new SbList<SoGLImage>();
  }
  assert(glimage_reglist.find(image) < 0);
  glimage_reglist.append(image);
  image.isregistered = true;
  //UNLOCK_GLIMAGE;
}

// used internally to keep track of the SoGLImages
private static void
unregisterImage(SoGLImage image)
{
  //assert(glimage_reglist); TODO

  //LOCK_GLIMAGE;
  int idx = glimage_reglist.find(image);
  assert(idx >= 0);
  if (idx >= 0) {
    glimage_reglist.removeFast(idx);
  }
  image.isregistered = false;
  //UNLOCK_GLIMAGE;
}



/*!
Sets the pbuffer for this texture. Experimental code, use with care.
*/
private void setPBuffer(SoState state, Object context) {
	setPBuffer(state, context, Wrap.REPEAT, Wrap.REPEAT, 0.5f);
}
  public void setPBuffer(SoState state,
                  Object  context,
                  Wrap wraps,
                  Wrap wrapt,
                  float quality) {
  if (this.pbuffer!=null && state != null) {
    // bind texture before releasing pbuffer
    this.getGLDisplayList(state).call(state);
    cc_glglue_context_release_pbuffer(this.pbuffer);
  }

  if (this.isregistered) SoGLImage.unregisterImage(this);
  this.unrefDLists(state);
  this.init(); // init to default values

  if (pbuffer != null) {
    this.pbuffer = pbuffer;
    this.wraps = wraps;
    this.wrapt = wrapt;

    this.glimageid = SoGLImage.getNextGLImageId(); // assign an unique id to this image
    this.needtransparencytest = true;
    this.hastransparency = false;
    this.usealphatest = false;
    this.quality = quality;


    if (this.pbuffer != null && !this.isregistered &&
        0==(this.getFlags() & Flags.INVINCIBLE.getValue())) {
      SoGLImage.registerImage(this);
    }
  }
	  
  }
  
  /*!
  Convenience 2D wrapper function around the 3D setData().
*/
public void
setData( SbImage image,
                   Wrap wraps,
                   Wrap wrapt,
                   float quality,
                   int border,
                   SoState createinstate)

{
  this.setData(image, wraps, wrapt, (Wrap)this/*.pimpl*/.wrapr,
                quality, border, createinstate);
}

  
  
/*!
  Sets flags to control how the texture is handled/initialized.
*/
public void
setFlags( int flags)
{
  this.flags = flags;
}

/*!
  Returns the flags.

  \sa setFlags()
*/
public int
getFlags() 
{
  return this.flags;
}

  
  
private void
init()
{
  assert(this.isregistered == false);
  this.image = null;
  this.pbuffer = null;
  this.glsize.setValue((short)0,(short)0,(short)0);
  this.glcomp = 0;
  this.wraps = SoGLImage.Wrap.CLAMP;
  this.wrapt = SoGLImage.Wrap.CLAMP;
  this.wrapr = SoGLImage.Wrap.CLAMP;
  this.border = 0;
  this.flags = SoGLImage.Flags.USE_QUALITY_VALUE.getValue();
  this.needtransparencytest = true;
  this.hastransparency = false;
  this.usealphatest = false;
  this.quality = 0.4f;
  this.imageage = 0;
  this.endframecb = null;
  this.glimageid = 0; // glimageid 0 is an empty image
}

  
  /*!
  Returns or creates a SoGLDisplayList to be used for rendering.
  Returns null if no SoDLDisplayList could be created.
*/
private void cc_glglue_context_release_pbuffer(Object pbuffer2) {
	// TODO Auto-generated method stub
	
}

public SoGLDisplayList 
getGLDisplayList(SoState state)
{
  //LOCK_GLIMAGE;
  SoGLDisplayList dl = this.findDL(state);
  //UNLOCK_GLIMAGE;

  if (dl == null) {
    dl = this.createGLDisplayList(state);
    if (dl != null) {
      //LOCK_GLIMAGE;
      this.dlists.append(/*SoGLImage::*/ new dldata(dl));
      //UNLOCK_GLIMAGE;
    }
  }
  if (dl!=null && !dl.isMipMapTextureObject() && this.image != null) {
    float quality = SoTextureQualityElement.get(state);
    float oldquality = this.quality;
    this.quality = quality;
    if (this.shouldCreateMipmap()) {
      //LOCK_GLIMAGE;
      // recreate DL to get a mipmapped image
      int n = this.dlists.getLength();
      for (int i = 0; i < n; i++) {
        if (this.dlists.operator_square_bracket(i).dlist == dl) {
          dl.unref(state); // unref old DL
          dl = this.createGLDisplayList(state);
          this.dlists.operator_square_bracket(i).dlist = dl;
          break;
        }
      }
      //UNLOCK_GLIMAGE;
    }
    else this.quality = oldquality;
  }
  return dl;
}


/*!
  Returns \e true if this texture has some pixels with alpha != 255
*/
public boolean
hasTransparency() 
{
  if ((this.flags & Flags.FORCE_TRANSPARENCY_TRUE.getValue())!=0) return true;
  if ((this.flags & Flags.FORCE_TRANSPARENCY_FALSE.getValue())!=0) return false;

  if (this.needtransparencytest) {
    ((SoGLImage)this)/*.pimpl*/.checkTransparency();
  }
  return this.hastransparency;
}

/*!
  Returns true if this image has some alpha value != 255, and all
  these values are 0. If this is the case, alpha test can be used
  to render this texture instead of for instance blending, which
  is usually slower and might yield z-buffer artifacts.
*/
public boolean
useAlphaTest()
{
  if ((this.flags & Flags.FORCE_ALPHA_TEST_TRUE.getValue())!=0) return true;
  if ((this.flags & Flags.FORCE_ALPHA_TEST_FALSE.getValue())!=0) return false;

  if (this.needtransparencytest) {
    ((SoGLImage)this)/*.pimpl*/.checkTransparency();
  }
  return this.usealphatest;
}

//
// Test image data for transparency by checking each texel.
//
private void
checkTransparency()
{
  this.needtransparencytest = false;
  this.usealphatest = false;
  this.hastransparency = false;

  final SbVec3s size = new SbVec3s();
  final int[] numcomponents = new int[1];
  byte[] bytes = (this.image!=null) ?
    this.image.getValue(size, numcomponents) : null;

  if (bytes == null) {
    if (this.glcomp == 2 || this.glcomp == 4) {
      // we must assume it has transparency, and that we
      // can't use alpha testing
      this.hastransparency = true;
    }
  }
  else {
    if (numcomponents[0] == 2 || numcomponents[0] == 4) {
      int n = size.getValue()[0] * size.getValue()[1] * ((size.getValue()[2] != 0) ? size.getValue()[2] : 1);
      int nc = numcomponents[0];
      int ptr = /* bytes +*/ nc - 1; // java port

      while (n!=0) {
        if (bytes[ptr] != 255 && bytes[ptr] != 0) break;
        if (bytes[ptr] == 0) this.usealphatest = true;
        ptr += nc;
        n--;
      }
      if (n > 0) {
        this.hastransparency = true;
        this.usealphatest = false;
      }
      else {
        this.hastransparency = this.usealphatest;
      }
    }
  }
}



private boolean
shouldCreateMipmap()
{
  if ((this.flags & SoGLImage.Flags.USE_QUALITY_VALUE.getValue()) != 0) {
    return this.quality >= COIN_TEX2_MIPMAP_LIMIT;
  }
  else {
    return (this.flags & SoGLImage.Flags.NO_MIPMAP.getValue()) == 0;
  }
}


// find dl for a context, null if not found
private SoGLDisplayList 
findDL(SoState state)
{
  int currcontext = SoGLCacheContextElement.get(state);
  int i, n = this.dlists.getLength();
  SoGLDisplayList dl;
  for (i = 0; i < n; i++) {
    dl = this.dlists.operator_square_bracket(i).dlist;
    if (dl.getContext() == currcontext) return dl;
  }
  return null;
}



//
// private method that in addition to creating the display list,
// tests the size of the image and performs a resize if the size is not
// a power of two.
// reallyCreateTexture is called (only) from here.
//
private SoGLDisplayList 
createGLDisplayList(SoState state)
{
  final SbVec3s size = new SbVec3s();
  final int[] numcomponents = new int[1];
  byte[] bytes =
    this.image != null ? this.image.getValue(size, numcomponents) : null;

  if (this.pbuffer == null && bytes == null) return null;

  int xsize = size.getValue()[0];
  int ysize = size.getValue()[1];
  int zsize = size.getValue()[2];
  boolean is3D = (size.getValue()[2]==0)?false:true;

  // these might change if image is resized
  byte[] imageptr = bytes;

  cc_glglue glw = SoGL.sogl_glue_instance(state);
  boolean mipmap = this.shouldCreateMipmap();
  
  if (imageptr != null) {
    if (is3D ||
        (!SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_NON_POWER_OF_TWO_TEXTURES) ||
         (mipmap && (!SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_GENERATE_MIPMAP) &&
                     !SoGLDriverDatabase.isSupported(glw, "GL_SGIS_generate_mipmap"))))) {
      //this.resizeImage(state, imageptr, xsize, ysize, zsize);
    }
  }
  SoCacheElement.setInvalid(true);
  if (state.isCacheOpen()) {
    SoCacheElement.invalidate(state);
  }
  
  SoGLDisplayList dl = new SoGLDisplayList(state,
                                            SoGLDisplayList.Type.TEXTURE_OBJECT,
                                            1, mipmap);
  dl.ref();

  if (bytes != null) {
    boolean is3D1 = (size.getValue()[2]==0)?false:true;
    if (is3D1) {
      dl.setTextureTarget((int) GL2.GL_TEXTURE_3D);
    }
    else {
      dl.setTextureTarget((int) ((this.flags & SoGLImage.Flags.RECTANGLE.getValue())!=0 ?
                                  GL2.GL_TEXTURE_RECTANGLE/*_EXT*/ : GL2.GL_TEXTURE_2D));
    }
  }

  dl.open(state);

  if (this.pbuffer != null) {
    // this.reallyBindPBuffer(state); //TODO YB
  }
  else {
    this.reallyCreateTexture(state, imageptr, numcomponents[0],
                              xsize, ysize, zsize,
                              dl.getType() == SoGLDisplayList.Type.DISPLAY_LIST,
                              mipmap,
                              this.border);
  }
  dl.close(state);
  return dl;
}


//
//Actually apply the texture filters using OpenGL calls.
//
public void
/*SoGLImageP::*/applyFilter( boolean ismipmap, GL2 gl2)
{
int target;

// Casting away const
final SbVec3s size = this.image !=null ? new SbVec3s(this.image.getSize()) : new SbVec3s(this.glsize);

if (size.getValue()[2] >= 1) target = GL2.GL_TEXTURE_3D;
else {
 target = (this.flags & SoGLImage.Flags.RECTANGLE.getValue())!=0 ?
   GL2.GL_TEXTURE_RECTANGLE/*_EXT*/ : GL2.GL_TEXTURE_2D;
}
if ((this.flags & SoGLImage.Flags.USE_QUALITY_VALUE.getValue())!=0) {
 if (this.quality < COIN_TEX2_LINEAR_LIMIT) {
   gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
   gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
 }
 else if ((this.quality < COIN_TEX2_MIPMAP_LIMIT) || !ismipmap) {
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
 }
 else if (this.quality < COIN_TEX2_LINEAR_MIPMAP_LIMIT) {
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST_MIPMAP_LINEAR);
 }
 else { // max quality
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
 }
}
else {
 if ((this.flags & SoGLImage.Flags.NO_MIPMAP.getValue())!=0 || !ismipmap) {
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER,
                   (this.flags & SoGLImage.Flags.LINEAR_MAG_FILTER.getValue())!=0 ?
                   GL2.GL_LINEAR : GL2.GL_NEAREST);
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER,
                   (this.flags & SoGLImage.Flags.LINEAR_MIN_FILTER.getValue())!=0 ?
                   GL2.GL_LINEAR : GL2.GL_NEAREST);
 }
 else {
	 gl2.glTexParameteri(target, GL2.GL_TEXTURE_MAG_FILTER,
                   (this.flags & SoGLImage.Flags.LINEAR_MAG_FILTER.getValue())!=0 ?
                   GL2.GL_LINEAR : GL2.GL_NEAREST);
   int minfilter = GL2.GL_NEAREST_MIPMAP_NEAREST;
   if ((this.flags & SoGLImage.Flags.LINEAR_MIPMAP_FILTER.getValue())!=0) {
     if ((this.flags & SoGLImage.Flags.LINEAR_MIN_FILTER.getValue())!=0)
       minfilter = GL2.GL_LINEAR_MIPMAP_LINEAR;
     else
       minfilter = GL2.GL_LINEAR_MIPMAP_NEAREST;
   }
   else if ((this.flags & SoGLImage.Flags.LINEAR_MIN_FILTER.getValue())!=0)
     minfilter = GL2.GL_NEAREST_MIPMAP_LINEAR;

   gl2.glTexParameteri(target, GL2.GL_TEXTURE_MIN_FILTER,
                   minfilter);
 }
}
}

  

//returns an unique uint32_t id for gl images
private static int
getNextGLImageId()
{
 return current_glimageid++;
}

public static int
translate_wrap(SoState state, SoGLImage.Wrap wrap)
{
  if (wrap == SoGLImage.Wrap.REPEAT) return GL2.GL_REPEAT;
  if (wrap == SoGLImage.Wrap.CLAMP_TO_BORDER) return GL2.GL_CLAMP_TO_BORDER;
  if (COIN_ENABLE_CONFORMANT_GL_CLAMP != 0) {
    if (wrap == SoGLImage.Wrap.CLAMP_TO_EDGE) {
      cc_glglue glw = SoGL.sogl_glue_instance(state);
      if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXTURE_EDGE_CLAMP)) return GL2.GL_CLAMP_TO_EDGE;
    }
    return GL2.GL_CLAMP;
  }
  cc_glglue glw = SoGL.sogl_glue_instance(state);
  if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXTURE_EDGE_CLAMP)) return GL2.GL_CLAMP_TO_EDGE;
  return GL2.GL_CLAMP;
}


public void
/*SoGLImageP::*/reallyCreateTexture(SoState state,
                                byte[] texture,
                                int numComponents,
                                int w, int h, int d,
                                boolean dlist, //FIXME: Not in use (kintel 20011129)
                                boolean mipmap,
                                int border)
{
  cc_glglue glw = SoGL.sogl_glue_instance(state);
  this.glsize.copyFrom(new SbVec3s((short) w, (short) h, (short) d));
  this.glcomp = numComponents;

  boolean compress =
    (this.flags & SoGLImage.Flags.COMPRESSED.getValue())!=0 &&
    SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXTURE_COMPRESSION);
  int internalFormat =
    Gl.coin_glglue_get_internal_texture_format(glw, numComponents, compress);
  int dataFormat = Gl.coin_glglue_get_texture_format(glw, numComponents);

  GL2 gl2 = state.getGL2();
  
  gl2.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

  //FIXME: Check cc_glglue capability as well? (kintel 20011129)
  if (SoMultiTextureEnabledElement.getMode(state) == 
      SoMultiTextureEnabledElement.Mode.TEXTURE3D) { // 3D textures
    gl2.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_WRAP_S,
                    translate_wrap(state, this.wraps));
    gl2.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_WRAP_T,
                    translate_wrap(state, this.wrapt));
    gl2.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_WRAP_R,
                    translate_wrap(state, this.wrapr));


    this.applyFilter(mipmap,gl2);

    if (!mipmap) {
      if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
        Gl.cc_glglue_glTexImage3D(glw, GL2.GL_TEXTURE_3D, 0, internalFormat, w, h, d,
                               border, dataFormat, GL2.GL_UNSIGNED_BYTE, texture);
      }
    }
    else { // mipmaps
      // We used to default to calling GLU's gluBuild3DMipmaps() here,
      // but that was axed, because the gluBuild[2|3]DMipmaps()
      // functions implicitly uses glGenTextures() and other OpenGL
      // 1.1+ functions -- which again can cause trouble when doing
      // remote rendering. (At least we've had lots of problems with
      // NVidia's GLX implementation for non-1.0 OpenGL stuff.)
      //
      //   (void)GLUWrapper().gluBuild3DMipmaps(GL_TEXTURE_3D, internalFormat,
      //                                         w, h, d, dataFormat,
      //                                         GL_UNSIGNED_BYTE, texture);

      fast_mipmap(state, w, h, d, numComponents, texture, false, compress);
    }
  }
  else { // 2D textures
    boolean mipmapimage = mipmap;
    boolean mipmapfilter = mipmap;
    boolean generatemipmap = false;

    int target = (this.flags & SoGLImage.Flags.RECTANGLE.getValue())!=0 ?
      GL2.GL_TEXTURE_RECTANGLE/*_EXT*/ : GL2.GL_TEXTURE_2D;

    gl2.glTexParameteri(target, GL2.GL_TEXTURE_WRAP_S,
                    translate_wrap(state, this.wraps));
    gl2.glTexParameteri(target, GL2.GL_TEXTURE_WRAP_T,
                    translate_wrap(state, this.wrapt));

    if (mipmap && (this.flags & SoGLImage.Flags.RECTANGLE.getValue())!=0) {
      mipmapimage = false;
      if (SoGLDriverDatabase.isSupported(glw, "GL_SGIS_generate_mipmap")) {
        gl2.glTexParameteri(target, GL2.GL_GENERATE_MIPMAP_SGIS, GL2.GL_TRUE);
      }
      else mipmapfilter = false;
    }
    // prefer GL_SGIS_generate_mipmap to glGenerateMipmap. It seems to
    // be better supported in drivers.
    else if (mipmap && SoGLDriverDatabase.isSupported(glw, "GL_SGIS_generate_mipmap")) {
      gl2.glTexParameteri(target, GL2.GL_GENERATE_MIPMAP_SGIS, GL2.GL_TRUE);
      mipmapimage = false;
    }
    // using glGenerateMipmap() while creating a display list is not
    // supported (even if the display list is never used). This is
    // probably because the OpenGL driver creates each mipmap level by
    // rendering it using normal OpenGL calls.
    else if (mipmap && SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_GENERATE_MIPMAP) && !state.isCacheOpen()) {
      mipmapimage = false;
      generatemipmap = true; // delay until after the texture image is set up
    }
    if ((this.quality > COIN_TEX2_ANISOTROPIC_LIMIT) &&
        SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_ANISOTROPIC_FILTERING)) {
      gl2.glTexParameterf(target, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT,
                      SoGL.cc_glglue_get_max_anisotropy(glw));
    }
    if (!mipmapimage) {
      // Create only level 0 texture. Mimpamps might be created by glGenerateMipmap
      gl2.glTexImage2D(target, 0, internalFormat, w, h,
                   border, dataFormat, GL2.GL_UNSIGNED_BYTE, Buffers.newDirectByteBuffer(texture));

      if (generatemipmap) {
        boolean wasenabled = true;
        // Woraround for ATi driver bug. GL_TEXTURE_2D needs to be
        // enabled when using glGenerateMipmap(), according to
        // dicussions on the opengl.org forums.
        if (glw.vendor_is_ati) {
          if (!gl2.glIsEnabled(GL2.GL_TEXTURE_2D)) {
            wasenabled = false;
            gl2.glEnable(GL2.GL_TEXTURE_2D);
          }
        }
        SoGL.cc_glglue_glGenerateMipmap(glw, target);
        if (!wasenabled) gl2.glDisable(GL2.GL_TEXTURE_2D);
      }
    }
    else { // mipmaps
      // The GLU function invocation has been disabled, for the
      // reasons stated in the code comments ~20 lines above on the
      // construction of 3D mipmap textures.
      //
      //   (void)GLUWrapper().gluBuild2DMipmaps(GL_TEXTURE_2D, internalFormat,
      //                                         w, h, dataFormat,
      //                                         GL_UNSIGNED_BYTE, texture);
      fast_mipmap(state, w, h, numComponents, texture, false, compress);
    }
    // apply the texture filters
    this.applyFilter(mipmapfilter,gl2);
  }
  gl2.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 4);
}



//
// unref all dlists stored in image
//
private void
unrefDLists(SoState state)
{
  int n = this.dlists.getLength();
  for (int i = 0; i < n; i++) {
    this.dlists.operator_square_bracket(i).dlist.unref(state);
  }
  this.dlists.truncate(0);
}

//
// Callback from SoContextHandler
//
private static void
contextCleanup(int context, Object closure)
{
  SoGLImage thisp = (SoGLImage) closure;
//#ifdef COIN_THREADSAFE
//  SoGLImageP::mutex.lock();
//#endif // COIN_THREADSAFE

  int n = thisp.dlists.getLength();
  int i = 0;

  while (i < n) {
    if (Objects.equals(thisp.dlists.operator_square_bracket(i).dlist.getContext() , context)) {
      thisp.dlists.operator_square_bracket(i).dlist.unref(null);
      thisp.dlists.remove(i);
      n--;
    }
    else i++;
  }
//#ifdef COIN_THREADSAFE
//  SoGLImageP::mutex.unlock();
//#endif // COIN_THREADSAFE
}

public void setData(byte[] bytes,
        final SbVec3s  size,
        final int numcomponents) {
	setData(bytes,size,numcomponents, Wrap.REPEAT,Wrap.REPEAT,Wrap.REPEAT,0.5f,0,null);
}

public void
setData(byte[] bytes,
                   final SbVec3s  size,
                   final int numcomponents,
                    Wrap wraps,
                    Wrap wrapt,
                    Wrap wrapr,
                    float quality)
{
	setData(bytes,size,numcomponents, wraps,wrapt,wrapr,quality,0,null);
}
/*!
  3D setData() wrapper. Supplies raw data, size and numcomponents instead of
  an SbImage. Creates a temporary image, then calls the read setData().
  \overload
*/
public void
setData(byte[] bytes,
                   final SbVec3s  size,
                   final int numcomponents,
                    Wrap wraps,
                    Wrap wrapt,
                    Wrap wrapr,
                    float quality,
                    int border,
                   SoState createinstate)
{
  this.dummyimage.setValuePtr(size, numcomponents, bytes);
  this.setData(this.dummyimage,
                wraps, wrapt, wrapr, quality,
                border, createinstate);
}


/*!
  Sets the data for this GL image. Should only be called when one
  of the parameters have changed, since this will cause the GL texture
  object to be recreated.  Caller is responsible for sending legal
  Wrap values.  CLAMP_TO_EDGE is only supported on OpenGL v1.2
  implementations, and as an extension on some earlier SGI
  implementations (GL_SGIS_texture_edge_clamp).

  For now, if quality > 0.5 when created, we create mipmaps, otherwise
  a regular texture is created.  Be aware, if you for instance create
  a texture with texture quality 0.4, and then later try to apply the
  texture with a texture quality greater than 0.5, the texture object
  will be recreated as a mipmap texture object. This will happen only
  once though, of course.

  If \a border != 0, the OpenGL texture will be created with this
  border size. Be aware that this might be extremely slow on most PC
  hardware.

  Normally, the OpenGL texture object isn't created until the first
  time it is needed, but if \a createinstate is != null, the texture
  object is created immediately. This is useful if you use a temporary
  buffer to hold the texture data. Be careful when using this feature,
  since the texture data might be needed at a later stage (for
  instance to create a texture object for another context).  It will
  not be possible to create texture objects for other cache contexts
  when \a createinstate is != null.

  Also if \a createinstate is supplied, and all the attributes are the
  same as the current data in the image, glTexSubImage() will be used
  to insert the image data instead of creating a new texture object.
  This is much faster on most OpenGL drivers, and is very useful, for
  instance when doing animated textures.

  If you supply null for \a image, the instance will be reset, causing
  all display lists and memory to be freed.
*/
public void
setData(final SbImage image,
                    Wrap wraps,
                    Wrap wrapt,
                    Wrap wrapr,
                    float quality,
                    int border,
                   SoState createinstate)

{
  this.imageage = 0;

  if (image == null) {
    this.unrefDLists(createinstate);
    if (this.isregistered) SoGLImage.unregisterImage(this);
    this.init(); // init to default values
    return;
  }

  this.glimageid = /*SoGLImageP.*/getNextGLImageId(); // assign an unique id to this image
  this.needtransparencytest = true;
  this.hastransparency = false;
  this.usealphatest = false;
  this.quality = quality;

  // check for special case where glTexSubImage can be used.
  // faster for most drivers.
  if (createinstate != null) { // We need the state for cc_glglue
    final cc_glglue glw = SoGL.sogl_glue_instance(createinstate);
    SoGLDisplayList dl = null;

    boolean copyok =
      wraps == this.wraps &&
      wrapt == this.wrapt &&
      wrapr == this.wrapr &&
      border == this.border &&
      border == 0 && // haven't tested with borders yet. Play it safe.
      (dl = this.findDL(createinstate)) != null;

    final SbVec3s size = new SbVec3s();
    final int[] nc = new int[1];
    byte[] bytes = image.getValue(size, nc);
    copyok = copyok && bytes != null && (size == this.glsize) && (nc[0] == this.glcomp);

    boolean is3D = (size.getValue()[2]==0)?false:true;
    boolean usesubimage = SoGLImage.COIN_TEX2_USE_GLTEXSUBIMAGE != 0 &&
      ((is3D && SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) ||
       (!is3D && SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXSUBIMAGE)));

    if (!usesubimage) copyok=false;
    if ((this.flags & Flags.RECTANGLE.getValue())!=0) copyok = false;

    if (copyok) {
      dl.ref();
      this.unrefDLists(createinstate);
      this.dlists.append(/*SoGLImageP::*/new dldata(dl));
      this.image = null; // data is temporary, and only for current context
      dl.call(createinstate);

      boolean compress =
        ((this.flags & Flags.COMPRESSED.getValue())!=0) &&
        SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXTURE_COMPRESSION);

      if (dl.isMipMapTextureObject()) {
        if (is3D)
          fast_mipmap(createinstate, size.getValue()[0], size.getValue()[1], size.getValue()[2], nc[0], bytes,
                      true, compress);
        else
          fast_mipmap(createinstate, size.getValue()[0], size.getValue()[1], nc[0], bytes,
                      true, compress);
      }
      else {
        /*GLenum*/int format = Gl.coin_glglue_get_texture_format(glw, nc[0]);
        if (is3D) {
          Gl.cc_glglue_glTexSubImage3D(glw, GL2.GL_TEXTURE_3D, 0, 0, 0, 0,
                                    size.getValue()[0], size.getValue()[1], size.getValue()[2],
                                    format, GL2.GL_UNSIGNED_BYTE,
                                     bytes);
        }
        else {
          Gl.cc_glglue_glTexSubImage2D(glw, GL2.GL_TEXTURE_2D, 0, 0, 0,
                                    size.getValue()[0], size.getValue()[1],
                                    format, GL2.GL_UNSIGNED_BYTE,
                                     bytes);
        }
      }
    }
    else {
      this.image = image;
      this.wraps = wraps;
      this.wrapt = wrapt;
      this.wrapr = wrapr;
      this.border = border;
      this.unrefDLists(createinstate);
      if (createinstate != null) {
        this.dlists.append(/*SoGLImageP::*/new dldata(this.createGLDisplayList(createinstate)));
        this.image = null; // data is assumed to be temporary
      }
    }
  }
  else {
    this.image = image;
    this.wraps = wraps;
    this.wrapt = wrapt;
    this.wrapr = wrapr;
    this.border = border;
    this.unrefDLists(createinstate);
  }

  if (this.image != null && !this.isregistered && 0==(this.getFlags() & Flags.INVINCIBLE.getValue())) {
    SoGLImage.registerImage(this);
  }
}

/*!
2D setData() wrapper. Supplies raw data, size and numcomponents instead of
an SbImage. Creates a temporary image, then calls the read setData().
\overload
*/

public void setData(byte[] bytes,
        final SbVec2s size,
        final int numcomponents,
        final Wrap wraps,
        final Wrap wrapt,
        final float quality) {
	setData(bytes,size,numcomponents,wraps,wrapt,quality,0,null);
}

public void
setData(byte[] bytes,
                 final SbVec2s size,
                 final int numcomponents,
                 final Wrap wraps,
                 final Wrap wrapt,
                 final float quality,
                 final int border,
                 final SoState createinstate)
{
	this/*.pimpl*/.dummyimage.setValuePtr(size, numcomponents, bytes);
	this.setData(this/*.pimpl*/.dummyimage,
              wraps, wrapt, quality,
              border, createinstate);
}



/*!
  Returns a pointer to the image data.
*/
public SbImage 
getImage()
{
  return this.image;
}


/*!
  Returns the wrap strategy for the S (horizontal) direction.
*/
public SoGLImage.Wrap
getWrapS()
{
  return this.wraps;
}

/*!
  Returns the wrap strategy for the T (vertical) direction.
*/
public SoGLImage.Wrap
getWrapT() 
{
  return this.wrapt;
}

/*!
  Returns the wrap strategy for the R (depth) direction.
*/
public SoGLImage.Wrap
getWrapR()
{
  return this.wrapr;
}

/*!
  Returns the texture quality for this texture image.

  \since Coin 2.5
*/
public float
getQuality()
{
  return this.quality;
}

/*!
  Returns an unique if for this GL image. This id can be used to
  test for changes in an SoGLImage's internal data.
*/
public int
getGLImageId() 
{
  return this.glimageid;
}

/*!
  \COININTERNAL
*/
public static void
initClass()
{
  assert(classTypeId.isBad());
  classTypeId.copyFrom( SoType.createType(SoType.badType(),
                                               new SbName("GLImage")));
//#ifdef COIN_THREADSAFE
//  SoGLImageP::mutex = new SbMutex;
//#endif // COIN_THREADSAFE
  glimage_bufferstorage = new SbStorage<soglimage_buffer>(() -> new soglimage_buffer()/*sizeof(soglimage_buffer), // java port
                                        glimage_buffer_construct, glimage_buffer_destruct*/);

  //coin_atexit((coin_atexit_f*)SoGLImage::cleanupClass, CC_ATEXIT_NORMAL); //TODO

  //SoGLCubeMapImage.initClass(); TODO
}


/*!
  Returns the type id for this class.
*/
public static SoType
getClassTypeId()
{
  assert(!classTypeId.isBad());
  return classTypeId;
}


// FIXME: grab better version of getTypeId() doc from SoBase, SoAction
// and / or SoDetail?  At least if this ever makes it into the public
// API.  20010913 mortene.
/*!
  Returns the type id for an SoGLImage instance.
*/
public SoType
getTypeId()
{
  return SoGLImage.getClassTypeId();
}

/*!
  Returns whether an SoGLImage instance inherits (or is of) type \a
  type.
*/
public boolean
isOfType(SoType type) 
{
  return this.getTypeId().isDerivedFrom(type);
}


// fast mipmap creation. no repeated memory allocations.
public static void
fast_mipmap(SoState state, int width, int height, int nc,
            byte[] data, boolean useglsubimage,
            boolean compress)
{
	GL2 gl2 = state.getGL2();
	
  final cc_glglue glw = SoGL.sogl_glue_instance(state);
  int internalFormat = Gl.coin_glglue_get_internal_texture_format(glw, nc, compress);
  /*GLenum*/int format = Gl.coin_glglue_get_texture_format(glw, nc);
  int levels = compute_log(width);
  int level = compute_log(height);
  if (level > levels) levels = level;

  int memreq = (Math.max(width>>1,1))*(Math.max(height>>1,1))*nc;
  byte[] mipmap_buffer = glimage_get_buffer(memreq, true);

  if (useglsubimage) {
    if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXSUBIMAGE)) {
      Gl.cc_glglue_glTexSubImage2D(glw, GL2.GL_TEXTURE_2D, 0, 0, 0,
                                width, height, format,
                                GL2.GL_UNSIGNED_BYTE, data);
    }
  }
  else {
    gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format,
    		GL2.GL_UNSIGNED_BYTE, Buffers.newDirectByteBuffer(data));
  }
  byte[] src = data;
  for (level = 1; level <= levels; level++) {
    halve_image(width, height, nc, src, mipmap_buffer);
    if (width > 1) width >>= 1;
    if (height > 1) height >>= 1;
    src = mipmap_buffer;
    if (useglsubimage) {
      if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_TEXSUBIMAGE)) {
        Gl.cc_glglue_glTexSubImage2D(glw, GL2.GL_TEXTURE_2D, level, 0, 0,
                                  width, height, format,
                                  GL2.GL_UNSIGNED_BYTE,  src);
      }
    }
    else {
      gl2.glTexImage2D(GL2.GL_TEXTURE_2D, level, internalFormat, width,
                   height, 0, format, GL2.GL_UNSIGNED_BYTE,
                    Buffers.newDirectByteBuffer(src));
    }
  }
}


//FIXME: Use as a special case of 3D image to reduce codelines ? (kintel 20011115)
public static void
halve_image( int width,  int height,  int nc,
            byte[] datain, byte[] dataout)
{
  assert(width > 1 || height > 1);

  int nextrow = width *nc;
  int newwidth = width >> 1;
  int newheight = height >> 1;
  /*byte[]*/int dst = 0;//dataout; // java port
  /*byte[]*/int src = 0;//datain; // java port

  // check for 1D images
  if (width == 1 || height == 1) {
    int n = Math.max(newwidth, newheight);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < nc; j++) {
        dataout[dst] = (byte)((datain[src+0] + datain[src+nc]) >> 1); // TODO >>> ?
        dst++; src++;
      }
      src += nc; // skip to next pixel
    }
  }
  else {
    for (int i = 0; i < newheight; i++) {
      for (int j = 0; j < newwidth; j++) {
        for (int c = 0; c < nc; c++) {
        	dataout[dst] = (byte)((datain[src+0] + datain[src+nc] + datain[src+nextrow] + datain[src+nextrow+nc] + 2) >> 2); // TODO >>> ?
          dst++; src++;
        }
        src += nc; // skip to next pixel
      }
      src += nextrow;
    }
  }
}



// fast mipmap creation. no repeated memory allocations. 3D version.
public static void
fast_mipmap(SoState  state, int width, int height, int depth,
            int nc, byte[] data, boolean useglsubimage,
            boolean compress)
{
  final cc_glglue glw = SoGL.sogl_glue_instance(state);
  int internalFormat = Gl.coin_glglue_get_internal_texture_format(glw, nc, compress);
  /*GLenum*/int format = Gl.coin_glglue_get_texture_format(glw, nc);
  int levels = compute_log(Math.max(Math.max(width, height), depth));

  int memreq = (Math.max(width>>1,1))*(Math.max(height>>1,1))*(Math.max(depth>>1,1))*nc;
  byte[] mipmap_buffer = glimage_get_buffer(memreq, true);

  // Send level 0 (original image) to OpenGL
  if (useglsubimage) {
    if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    	Gl.cc_glglue_glTexSubImage3D(glw, GL2.GL_TEXTURE_3D, 0, 0, 0, 0,
                                width, height, depth, format,
                                GL2.GL_UNSIGNED_BYTE, data);
    }
  }
  else {
    if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    	Gl.cc_glglue_glTexImage3D(glw, GL2.GL_TEXTURE_3D, 0, internalFormat,
                             width, height, depth, 0, format,
                             GL2.GL_UNSIGNED_BYTE, data);
    }
  }
  byte[] src = (byte[]) data;
  for (int level = 1; level <= levels; level++) {
    halve_image(width, height, depth, nc, src, mipmap_buffer);
    if (width > 1) width >>= 1;
    if (height > 1) height >>= 1;
    if (depth > 1) depth >>= 1;
    src = mipmap_buffer;
    if (useglsubimage) {
      if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    	  Gl.cc_glglue_glTexSubImage3D(glw, GL2.GL_TEXTURE_3D, level, 0, 0, 0,
                                  width, height, depth, format,
                                  GL2.GL_UNSIGNED_BYTE, src);
      }
    }
    else {
      if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    	  Gl.cc_glglue_glTexImage3D(glw, GL2.GL_TEXTURE_3D, level, internalFormat,
                               width, height, depth, 0, format,
                               GL2.GL_UNSIGNED_BYTE, src);
      }
    }
  }
}


public static void
halve_image( int width,  int height,  int depth,  int nc,
            byte[] datain, byte[] dataout)
{
  assert(width > 1 || height > 1 || depth > 1);

  int rowsize = width * nc;
  int imagesize = width * height * nc;
  int newwidth = width >> 1;
  int newheight = height >> 1;
  int newdepth = depth >> 1;
  int dst = 0;//dataout; // java port
  int src = 0;// datain; // java port

  int numdims = (width>=1?1:0)+(height>=1?1:0)+(depth>=1?1:0);
  // check for 1D images.
  if (numdims == 1) {
    int n = Math.max(Math.max(newwidth, newheight), newdepth);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < nc; j++) {
    	  dataout[dst] = (byte)((datain[src+0] + datain[src+nc]) >> 1); // TODO >>> ?
        dst++; src++;
      }
      src += nc; // skip to next pixel/row/image
    }
  }
  // check for 2D images
  else if (numdims == 2) {
    int s1,s2,blocksize;
    if (width==1) {
      s1 = newheight;
      blocksize = height * nc;
    }
    else {
      s1 = newwidth;
      blocksize = width * nc;
    }
    s2 = depth==1?newheight:newdepth;
    for (int j = 0; j < s2; j++) {
      for (int i = 0; i < s1; i++) {
        for (int c = 0; c < nc; c++) { // java port
        	dataout[dst] = (byte)((datain[src+0] + datain[src+nc] + datain[src+blocksize] + datain[src+blocksize+nc] + 2) >> 2); // TODO >>> ?
          dst++; src++;
        }
        src += nc; // skip to next pixel (x or y direction)
      }
      src += blocksize; // Skip to next row/image
    }
  }
  else { // 3D image
    for (int k = 0; k < newdepth; k++) {
      for (int j = 0; j < newheight; j++) {
        for (int i = 0; i < newwidth; i++) {
          for (int c = 0; c < nc; c++) {
        	  dataout[dst] = (byte)((datain[src+0] + datain[src+nc] +
        			  datain[src+rowsize] + datain[src+rowsize+nc] +
        			  datain[src+imagesize] + datain[src+imagesize+nc] +
        			  datain[src+imagesize+rowsize] + datain[src+imagesize+rowsize+nc] +
                    4) >> 3); // TODO >>> ?
            dst++; src++;
          }
          src += nc; // skip one pixel
        }
        src += rowsize; // skip one row
      }
      src += imagesize; // skip one image
    }
  }
}



static int
compute_log(int value)
{
  int i = 0;
  while (value > 1) { value>>=1; i++; }
  return i;
}


public static byte[]
glimage_get_buffer( int buffersize, boolean mipmap)
{
  soglimage_buffer buf = null;
  assert(glimage_bufferstorage != null);

  buf = (soglimage_buffer)
    glimage_bufferstorage.get();
  if (mipmap) {
    if (buf.mipmapbuffersize < buffersize) {
      //delete[] buf.mipmapbuffer; java port
      buf.mipmapbuffer = new byte[buffersize];
      buf.mipmapbuffersize = buffersize;
    }
    return buf.mipmapbuffer;
  }
  else {
    if (buf.buffersize < buffersize) {
      //delete[] buf.buffer; java port
      buf.buffer = new byte[buffersize];
      buf.buffersize = buffersize;
      // FIXME: this is an extremely lame workaround for a Purify UMR
      // reported by Tore Kristiansen of HitecO.
      //
      // An UMR is reported from buf.buffer (when disabling the
      // memset() workaround below) from somewhere within the
      // fast_mipmap() method. Purify doesn't go far enough down the
      // call-stack (probably because fast_mipmap() is a local static
      // method?)  for us to easily see where the exact error happens,
      // though.
      //
      // My guess is that the mipmap-buffer isn't completely "filled
      // out", and so the glTex[Sub]Image2D() call asks for more
      // pixels than was generated.
      //
      // Should try to get this problem reproduced locally before
      // attempting to fix it.
      //
      // 20030514 mortene.
      //(void)memset(buf.buffer, 0x55, buf.buffersize); java port
    }
    return buf.buffer;
  }
}

/*!
Should be called when a texture image is used. In Coin this is
handled by SoGLTextureImageElement, but if you use an SoGLImage on
your own, you should call this method to avoid that the display list
is deleted too soon. \a state should be your SoGLRenderAction state,
\a image the image you are about to use/have used.
*/
public static void
tagImage(SoState state, SoGLImage image)
{
assert(image != null);
if (image != null) {
  //LOCK_GLIMAGE;
  image.resetAge();
  image/*.pimpl*/.tagDL(state);
  //UNLOCK_GLIMAGE;
}
}

public void
resetAge() 
{
  this/*.pimpl*/.imageage = 0;
}

public void
tagDL(SoState state)
{
  int currcontext = SoGLCacheContextElement.get(state);
  int i, n = this.dlists.getLength();
  SoGLDisplayList dl;
  for (i = 0; i < n; i++) {
    dl = this.dlists.operator_square_bracket(i).dlist;
    if (dl.getContext() == currcontext) {
      this.dlists.operator_square_bracket(i).age = 0;
      break;
    }
  }
}

public void setGLDisplayList(SoGLDisplayList dl,
        SoState state) {
	setGLDisplayList(dl, state, Wrap.REPEAT, Wrap.REPEAT, 0.5f);
}

public void setGLDisplayList(SoGLDisplayList dl,
        SoState state,
        Wrap wraps,
        Wrap wrapt
        ) {
	setGLDisplayList(dl, state, wraps, wrapt, 0.5f);
}

public void
setGLDisplayList(SoGLDisplayList dl,
                            SoState state,
                            Wrap wraps,
                            Wrap wrapt,
                            float quality)
{
  if (this/*.pimpl*/.isregistered) SoGLImage.unregisterImage(this);
  this/*.pimpl*/.unrefDLists(state);
  dl.ref();
  this/*.pimpl*/.dlists.append(new /*SoGLImageP.*/dldata(dl));
  this/*.pimpl*/.image = null; // we have no data. Texture is organized outside this image
  this/*.pimpl*/.wraps = wraps;
  this/*.pimpl*/.wrapt = wrapt;
  this/*.pimpl*/.glimageid = /*SoGLImageP.*/getNextGLImageId(); // assign an unique id to this image
  this/*.pimpl*/.needtransparencytest = false;
  this/*.pimpl*/.hastransparency = false;
  this/*.pimpl*/.usealphatest = false;
  this/*.pimpl*/.quality = quality;

  // don't register this image. There's no way we can reload it if we
  // delete it because of old age.
}


}
