/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoSceneTexture2 SoSceneTexture2.h Inventor/nodes/SoSceneTexture2.h
  \brief The SoSceneTexture2 class is used to create a 2D texture from a Coin scene graph.
  \ingroup nodes

  Lets the rendering of a scene graph be specified as a texture image
  to be used in another scene graph. Set up the scene graph used for a
  texture in the SoSceneTexture2.scene field.

  This node behaves exactly like SoTexture2 when it comes mapping the
  actual texture onto subsequent geometry. Please read the SoTexture2
  documentation for more information about how textures are mapped
  onto shapes.

  A notable feature of this node is that it will use offscreen
  pbuffers for hardware accelerated rendering, if they are available
  from the OpenGL driver. WGL, GLX and AGL, for OpenGL drivers on
  Microsoft Windows, X11 and Mac OS X, respectively, all support the
  OpenGL Architecture Review Board (ARB) pbuffer extension in later
  incarnations from most OpenGL vendors.

  Note also that the offscreen pbuffer will be used directly on the
  card as a texture, with no costly round trip back and forth from CPU
  memory, if the OpenGL driver supports the recent ARB_render_texture
  extension.

  An important limitation is that textures should have dimensions that
  are equal to a whole power-of-two, see documentation for
  SoSceneTexture.size.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    SceneTexture2 {
        size 256 256
        scene NULL
        sceneTransparencyType NULL
        type RGBA_UNSIGNED_BYTE
        backgroundColor 0 0 0 0
        transparencyFunction NONE
        wrapS REPEAT
        wrapT REPEAT
        model MODULATE
        blendColor 0 0 0
    }
  \endcode

  \since Coin 2.2
*/

// *************************************************************************

/*!
  \enum SoSceneTexture2.Model

  Texture mapping model, for deciding how to "merge" the texturemap
  with the object it is mapped onto.
*/
/*!
  \var SoSceneTexture2.Model SoSceneTexture2.MODULATE

  Texture color is multiplied by the polygon color. The result will
  be Phong shaded (if light model is PHONG).
*/
/*!
  \var SoSceneTexture2.Model SoSceneTexture2.DECAL

  Texture image overwrites polygon shading. Textured pixels will
  not be Phong shaded. Has undefined behaviour for grayscale and
  grayscale-alpha textures.
*/
/*!
  \var SoSceneTexture2.Model SoSceneTexture2.BLEND

  This model is normally used with monochrome textures (i.e. textures
  with one or two components). The first component, the intensity, is
  then used to blend between the shaded color of the polygon and the
  SoSceneTexture2.blendColor.
*/
/*!
  \var SoSceneTexture2.Model SoSceneTexture2.REPLACE

  Texture image overwrites polygon shading. Textured pixels will not
  be Phong shaded. Supports grayscale and grayscale alpha
  textures. This feature requires OpenGL 1.1. MODULATE will be used if
  OpenGL version < 1.1 is detected.
*/

/*!
  \enum SoSceneTexture2.Wrap

  Enumeration of wrapping strategies which can be used when the
  texturemap doesn't cover the full extent of the geometry.
*/
/*!
  \var SoSceneTexture2.Wrap SoSceneTexture2.REPEAT
  Repeat texture when coordinate is not between 0 and 1.
*/
/*!
  \var SoSceneTexture2.Wrap SoSceneTexture2.CLAMP
  Clamp coordinate between 0 and 1.
*/

/*!
  \enum SoSceneTexture2.TransparencyFunction

  For deciding how the texture's alpha channel is handled. It's not
  possible to automatically detect this, since the texture is stored
  only on the graphics card's memory, and it'd be too slow to fetch
  the image to test the alpha channel like Coin does for regular
  textures.
*/

/*!
  \var SoSceneTexture2.Transparency SoSceneTexture2.NONE
  The alpha channel is ignored.
*/

/*!
  \var SoSceneTexture2.Transparency SoSceneTexture2.ALPHA_TEST
  An alpha test function is used.
*/

/*!
  \var SoSceneTexture2.Transparency SoSceneTexture2.ALPHA_BLEND
  Alpha blending is used.
*/

/*!
  \var SoSFEnum SoSceneTexture2.wrapS

  Wrapping strategy for the S coordinate when the texturemap is
  narrower than the object to map onto.

  Default value is SoSceneTexture2.REPEAT.
*/
/*!
  \var SoSFEnum SoSceneTexture2.wrapT

  Wrapping strategy for the T coordinate when the texturemap is
  shorter than the object to map onto.

  Default value is SoSceneTexture2.REPEAT.
*/
/*!
  \var SoSFEnum SoSceneTexture2.model

  Texturemapping model for how the texturemap is "merged" with the
  polygon primitives it is applied to. Default value is
  SoSceneTexture2.MODULATE.
*/
/*!
  \var SoSFColor SoSceneTexture2.blendColor

  Blend color. Used when SoSceneTexture2.model is SoSceneTexture2.BLEND.

  Default color value is [0, 0, 0], black, which means no contribution
  to the blending is made.
*/

/*!
  \var SoSFVec2s SoSceneTexture2.size

  The size of the texture.

  This node currently only supports power of two textures.  If the
  size is not a power of two, the value will be rounded upwards to the
  next power of two.
*/

/*!
  \var SoSFNode SoSceneTexture2.scene

  The scene graph that is rendered into the texture.
*/

/*!
  \var SoSFNode SoSceneTexture2.sceneTransparencyType

  Used for overriding the transparency type for the sub scene graph.
  Should contain an instance of the SoTransparecyType node, or NULL to
  inherit the transparency type from the current viewer.

  Please note that if you want to render the texture using frame
  buffer objects, you need to use of of the NONE, SCREEN_DOOR, ADD or
  BLEND transparency types.

*/

/*!
  \var SoSFVec4f SoSceneTexture2.backgroundColor

  The color the color buffer is cleared to before rendering the scene.
  Default value is (0.0f, 0.0f, 0.0f, 0.0f).
*/

/*!
  \var SoSFEnum SoSceneTexture2.transparencyFunction

  The transparency function used. Default value is NONE.
*/

/*!
  \var SoSFNode SoSceneTexture2.type

  The type of texture to generate. RGBA8 for normal texture, DEPTH for
  a depth buffer texture, RGBA32F for a floating point RGBA texture.
  texture. Default is RGBA_UNSIGNED_BYTE.

*/

/*!
  \var SoSceneTexture2.Type SoSceneTexture2.RGBA8
  Specifies an RGBA texture with 8 bits per component.
*/

/*!
  \var SoSceneTexture2.Type SoSceneTexture2.DEPTH
  Specifies a depth buffer texture.
*/

/*!
  \var SoSceneTexture2.Type SoSceneTexture2.RGBA32F
  Specifies a RGBA texture with floating point components.
*/

package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoWriteAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.fields.SoSFVec4f;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.fields.SoSFVec2s;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSceneTexture2 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSceneTexture2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSceneTexture2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSceneTexture2.class); }    	  	
	

	  public enum Model {
		    MODULATE (SoMultiTextureImageElement.Model.MODULATE.getValue()),
		    DECAL (SoMultiTextureImageElement.Model.DECAL.getValue()),
		    BLEND (SoMultiTextureImageElement.Model.BLEND.getValue()),
		    REPLACE (SoMultiTextureImageElement.Model.REPLACE.getValue());
		    
		    private int value;
		    
		    Model(int value) {
		    	this.value = value;
		    }
		    
		    public int getValue() {
		    	return value;
		    }
		  };

		  public enum Wrap {
		    REPEAT (SoMultiTextureImageElement.Wrap.REPEAT.getValue()),
		    CLAMP (SoMultiTextureImageElement.Wrap.CLAMP.getValue()),
		    CLAMP_TO_BORDER (SoMultiTextureImageElement.Wrap.CLAMP_TO_BORDER.getValue());
		    
		    private int value;
		    
		    Wrap( int value) {
		    	this.value = value;
		    }
		    
		    public int getValue() {
		    	return value;
		    }

			public static Wrap fromValue(int value2) {
				if(value2 == REPEAT.getValue()) { return REPEAT;}
				if(value2 == CLAMP.getValue()) { return CLAMP;}
				if(value2 == CLAMP_TO_BORDER.getValue()) { return CLAMP_TO_BORDER;}
				return null;
			}
		  };

		  public enum TransparencyFunction {
		    NONE,
		    ALPHA_BLEND,
		    ALPHA_TEST;
			  
			  public int getValue() {
				  return ordinal();
			  }

			public static TransparencyFunction fromValue(int value) {
				switch(value) {
				case 0: return NONE;
				case 1: return ALPHA_BLEND;
				case 2: return ALPHA_TEST;
				}
				return null;
			}
		  };

		  public enum Type {
		    DEPTH,
		    RGBA8, // normal unsigned byte rgba
		    RGBA32F,
		    RGB32F,
		    RGBA16F,
		    RGB16F,

		    // FIXME: consider how many of these we should have here
		    R3_G3_B2,
		    RGB,
		    RGB4,
		    RGB5,
		    RGB8,
		    RGB10,
		    RGB12,
		    RGB16,
		    RGBA,
		    RGBA2,
		    RGBA4,
		    RGB5_A1,
		    RGB10_A2,
		    RGBA12,
		    RGBA16;
			  
			  public int getValue() {
				  return ordinal();
			  }

			public static Type fromValue(int value) {
				Type[] values = Type.values();
				return values[value];
			}
		  };

		  public final SoSFEnum wrapS = new SoSFEnum();
		  public final SoSFEnum wrapT = new SoSFEnum();
		  public final SoSFEnum model = new SoSFEnum();
		  public final SoSFColor blendColor = new SoSFColor();

		  public final SoSFVec4f backgroundColor = new SoSFVec4f();
		  public final SoSFVec2s size = new SoSFVec2s();
		  public final SoSFNode scene = new SoSFNode();
		  public final SoSFNode sceneTransparencyType = new SoSFNode();
		  public final SoSFEnum transparencyFunction = new SoSFEnum();

		  public final SoSFEnum type = new SoSFEnum();
		  
		  static class fbo_deletedata {
			    public int frameBuffer;
			    public int depthBuffer;
			  };

			  public static void fbo_delete_cb(Object closure, int contextid) {
			    cc_glglue glue = SoGL.cc_glglue_instance(contextid);
			    fbo_deletedata fbodata = (fbo_deletedata) (closure);
			    if (fbodata.frameBuffer != GL2.GL_INVALID_VALUE) {
			      SoGL.cc_glglue_glDeleteFramebuffers(glue, 1, fbodata.frameBuffer);
			    }
			    if (fbodata.depthBuffer != GL2.GL_INVALID_VALUE) {
			      SoGL.cc_glglue_glDeleteRenderbuffers(glue, 1, fbodata.depthBuffer);
			    }
			    // delete fbodata; java port
			  }

		  
		  

		  

	  private SoSceneTexture2P pimpl;
		  
		
	  
	  static SoGLImage.Wrap
	  translateWrap( SoSceneTexture2.Wrap wrap)
	  {
	    if (wrap == SoSceneTexture2.Wrap.CLAMP_TO_BORDER) return SoGLImage.Wrap.CLAMP_TO_BORDER;
	    if (wrap == SoSceneTexture2.Wrap.REPEAT) return SoGLImage.Wrap.REPEAT;
	    return SoGLImage.Wrap.CLAMP;
	  }

	  public SoSceneTexture2()
	  {
	    this.pimpl = new SoSceneTexture2P(this);

	    nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoSceneTexture2.class);
	    nodeHeader.SO_NODE_ADD_FIELD(size,"size", new SbVec2s((short)256, (short)256));
	    nodeHeader.SO_NODE_ADD_FIELD(scene,"scene", (null));
	    nodeHeader.SO_NODE_ADD_FIELD(sceneTransparencyType,"sceneTransparencyType", (null));
	    nodeHeader.SO_NODE_ADD_FIELD(backgroundColor,"backgroundColor", new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
	    nodeHeader.SO_NODE_ADD_FIELD(transparencyFunction,"transparencyFunction", (TransparencyFunction.NONE.getValue()));

	    nodeHeader.SO_NODE_ADD_FIELD(wrapS,"wrapS", (Wrap.REPEAT.getValue()));
	    nodeHeader.SO_NODE_ADD_FIELD(wrapT,"wrapT", (Wrap.REPEAT.getValue()));
	    nodeHeader.SO_NODE_ADD_FIELD(model,"model", (Model.MODULATE.getValue()));
	    nodeHeader.SO_NODE_ADD_FIELD(blendColor,"blendColor", new SbColor(0.0f, 0.0f, 0.0f));
	    nodeHeader.SO_NODE_ADD_FIELD(type,"type", (Type.RGBA8.getValue()));

	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.MODULATE);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.DECAL);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.BLEND);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.REPLACE);

	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.REPEAT);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.CLAMP);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.CLAMP_TO_BORDER);

	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(TransparencyFunction.NONE);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(TransparencyFunction.ALPHA_BLEND);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(TransparencyFunction.ALPHA_TEST);

	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapS,"wrapS", "Wrap");
	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapT,"wrapT", "Wrap");
	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(model,"model", "Model");
	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(transparencyFunction,"transparencyFunction", "TransparencyFunction");

	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA8);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.DEPTH);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA32F);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB32F);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA16F);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB16F);

	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.R3_G3_B2);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB4);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB5);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB8);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB10);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB12);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB16);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA2);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA4);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB5_A1);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGB10_A2);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA12);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.RGBA16);

	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(type,"type", "Type");
	  }

	  public void destructor()
	  {
	    Destroyable.delete(this.pimpl);
	    
	    scene.destructor(); //FIXME
	    sceneTransparencyType.destructor();
	    
	    super.destructor();
	  }

      static int didwarn = 0;

	  // Documented in superclass.
	  public void
	  GLRender(SoGLRenderAction action)
	  {
	    SoState state = action.getState();
	    if (SoTextureOverrideElement.getImageOverride(state))
	      return;

	    float quality = SoTextureQualityElement.get(state);

	    int cachecontext = SoGLCacheContextElement.get(state);
	     cc_glglue glue = SoGL.cc_glglue_instance(cachecontext);
	    SoNode root = this.scene.getValue();

	    // this is not a very good solution, but just recreate the
	    // pbuffer/framebuffer if the context has changed since it was
	    // created.
	    if (cachecontext != this.pimpl.glimagecontext) {
	      this.pimpl.glimagevalid = false;
	      this.pimpl.buffervalid = false;
	      // this will force the pbuffers and/or framebuffers to be recreated
	      this.pimpl.glcontextsize.copyFrom( new SbVec2s((short)-1, (short)-1));
	      if (this.pimpl.fbodata != null) {
	        this.pimpl.fbodata.fbo_size.copyFrom( new SbVec2s((short)-1, (short)-1));
	      }
	    }
	    this.pimpl.mutex.lock();

	    if (root != null && (!this.pimpl.buffervalid || !this.pimpl.glimagevalid)) {
	      this.pimpl.updateBuffer(state, quality);

	      // don't cache when we change the glimage
	      SoCacheElement.setInvalid(true);
	      if (state.isCacheOpen()) {
	        SoCacheElement.invalidate(state);
	      }
	    }
	    this.pimpl.mutex.unlock();
	    //UNLOCK_GLIMAGE(this);

	    SoMultiTextureImageElement.Model glmodel = SoMultiTextureImageElement.Model.fromValue(
	      this.model.getValue());

	    if (glmodel == SoMultiTextureImageElement.Model.REPLACE) {
	      if (!SoGL.cc_glglue_glversion_matches_at_least(glue, 1, 1, 0)) {
	        if (didwarn == 0) {
	          SoDebugError.postWarning("SoSceneTexture2.GLRender",
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

	    int unit = SoTextureUnitElement.get(state);
	    int maxunits = SoGL.cc_glglue_max_texture_units(glue);
	    if (unit < maxunits) {
	      SoGLMultiTextureImageElement.set(state, this, unit,
	                                        this.pimpl.glimage,
	                                        glmodel,
	                                        this.blendColor.getValue());

	      SoGLMultiTextureEnabledElement.set(state, this, unit,
	                                          this.pimpl.glimage != null &&
	                                          quality > 0.0f);
	    }
	    else {
	      // we already warned in SoTextureUnit. I think it's best to just
	      // ignore the texture here so that all texture for non-supported
	      // units will be ignored. pederb, 2003-11-04
	    }
	  }


	  // Documented in superclass.
	  public void
	  SoSceneTexture2_doAction(SoAction action)
	  {
//	  #if 0 // disabled until we figure out what to do here, pederb 2003-11-27
//	    SoState * state = action.getState();
//
//	    if (SoTextureOverrideElement.getImageOverride(state))
//	      return;
//
//	    if (size != SbVec2s(0,0)) {
//	      SoTextureImageElement.set(state, this,
//	                                 size, nc, bytes,
//	                                 (int)this.wrapT.getValue(),
//	                                 (int)this.wrapS.getValue(),
//	                                 (SoTextureImageElement.Model) model.getValue(),
//	                                 this.blendColor.getValue());
//	      SoTextureEnabledElement.set(state, this, TRUE);
//	    }
//	    else {
//	      SoTextureImageElement.setDefault(state, this);
//	      SoTextureEnabledElement.set(state, this, false);
//	    }
//	    if (this.isOverride()) {
//	      SoTextureOverrideElement.setImageOverride(state, TRUE);
//	    }
//	  #endif // disabled
	  }

	  // Documented in superclass.
	  public void
	  callback(SoCallbackAction action)
	  {
	    SoSceneTexture2_doAction(action);
	  }

	  // Documented in superclass.
	  public void
	  rayPick(SoRayPickAction action)
	  {
	    SoSceneTexture2_doAction(action);
	  }

	  // Documented in superclass.
	  public void
	  notify(SoNotList list)
	  {
	    SoField  f = list.getLastField();
	    if (f == this.scene ||
	        f == this.size ||
	        f == this.type) {
	      // rerender scene
	      this.pimpl.buffervalid = false;
	    }
	    else if (f == this.wrapS ||
	             f == this.wrapT ||
	             f == this.model ||
	             f == this.transparencyFunction ||
	             f == this.sceneTransparencyType) {
	      // no need to render scene again, but update the texture object
	      this.pimpl.glimagevalid = false;
	    }
	    super.notify(list);
	  }

	  // Documented in superclass.
	  public void
	  write(SoWriteAction action)
	  {
	    //super.write(action); TODO
	  }


	  //#undef PRIVATE

	  // *************************************************************************

	  //#define PUBLIC(obj) obj->api

	  public static void soscenetexture2_translate_type(SoSceneTexture2.Type type, final int[] internalformat, final int[] format)
	  {
	    format[0] = GL2.GL_RGBA;
	    internalformat[0] = GL2.GL_RGBA8;

	    switch (type) {
	    case DEPTH:
	    case RGBA8:
	      internalformat[0] = GL2.GL_RGBA8;
	      break;
	    case RGBA32F:
	      internalformat[0] = GL2.GL_RGBA32F/*_ARB*/;
	      break;
	    case RGB32F:
	      internalformat[0] = GL2.GL_RGB32F/*_ARB*/;
	      break;
	    case RGBA16F:
	      internalformat[0] = GL2.GL_RGBA16F/*_ARB*/;
	      break;
	    case RGB16F:
	      internalformat[0] = GL2.GL_RGB16F/*_ARB*/;
	      break;
	    case R3_G3_B2:
	      internalformat[0] = GL2.GL_R3_G3_B2;
	      break;
	    case RGB:
	      internalformat[0] = GL2.GL_RGB;
	      break;
	    case RGB4:
	      internalformat[0] = GL2.GL_RGB4;
	      break;
	    case RGB5:
	      internalformat[0] = GL2.GL_RGB5;
	      break;
	    case RGB8:
	      internalformat[0] = GL2.GL_RGB8;
	      break;
	    case RGB10:
	      internalformat[0] = GL2.GL_RGB10;
	      break;
	    case RGB12:
	      internalformat[0] = GL2.GL_RGB12;
	      break;
	    case RGB16:
	      internalformat[0] = GL2.GL_RGB16;
	      break;
	    case RGBA:
	      internalformat[0] = GL2.GL_RGBA;
	      break;
	    case RGBA2:
	      internalformat[0] = GL2.GL_RGBA2;
	      break;
	    case RGBA4:
	      internalformat[0] = GL2.GL_RGBA4;
	      break;
	    case RGB5_A1:
	      internalformat[0] = GL2.GL_RGB5_A1;
	      break;
	    case RGB10_A2:
	      internalformat[0] = GL2.GL_RGB10_A2;
	      break;
	    case RGBA12:
	      internalformat[0] = GL2.GL_RGBA12;
	      break;
	    case RGBA16:
	      internalformat[0] = GL2.GL_RGBA16;
	      break;
	    default:
	      assert(false);// && "unknown type");
	      break;
	    }
	  }

	  //#undef PUBLIC

	  //#undef LOCK_GLIMAGE
	  //#undef UNLOCK_GLIMAGE

	  // **************************************************************
	  
	  
	  
	  
	// Documented in superclass.
	  public static void
	  initClass()
	  {
	    //SO_NODE_INTERNAL_INIT_CLASS(SoSceneTexture2.class, SO_FROM_COIN_2_2);
	       SoSubNode.SO__NODE_INIT_CLASS(SoSceneTexture2.class, "SceneTexture2", SoNode.class);

	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureImageElement.class);
	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureEnabledElement.class);

	    SO_ENABLE(SoCallbackAction.class, SoMultiTextureImageElement.class);
	    SO_ENABLE(SoCallbackAction.class, SoMultiTextureEnabledElement.class);

	    SO_ENABLE(SoRayPickAction.class, SoMultiTextureImageElement.class);
	    SO_ENABLE(SoRayPickAction.class, SoMultiTextureEnabledElement.class);
	  }

}
