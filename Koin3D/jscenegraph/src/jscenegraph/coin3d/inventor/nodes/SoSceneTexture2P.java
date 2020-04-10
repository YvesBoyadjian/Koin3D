/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import java.util.Objects;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoLightElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.inventor.nodes.SoSceneTexture2.fbo_deletedata;
import jscenegraph.coin3d.inventor.threads.SbMutex;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.misc.Tidbits;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSceneTexture2P implements Destroyable {

	  static class fbo_data implements Destroyable {
		    int fbo_frameBuffer;
		    int fbo_depthBuffer;
		    final SbVec2s fbo_size = new SbVec2s();
		    boolean fbo_mipmap;
		    SoGLDisplayList fbo_texture; //ptr
		    SoGLDisplayList fbo_depthmap; // ptr
		    int cachecontext;

		    public fbo_data(int cachecontext) {
		        this.cachecontext = cachecontext;
		        this.fbo_frameBuffer = GL2.GL_INVALID_VALUE;
		        this.fbo_depthBuffer = GL2.GL_INVALID_VALUE;
		        this.fbo_texture = null;
		        this.fbo_depthmap = null;
		        this.fbo_size.setValue((short)-1,(short)-1);
		        this.fbo_mipmap = false;
		    }

			@Override
			public void destructor() {
			}
		  };

//		public:
//		  SoSceneTexture2P(SoSceneTexture2 * api);
//		  ~SoSceneTexture2P();

		  // FIXME: move the updateBuffer and SoGLImage handling into a new
		  // class called CoinOffscreenTexture (or something similar).
		  // pederb, 2007-03-07

		  public SoSceneTexture2 api; //ptr
		  public Object glcontext; //ptr
		  public final SbVec2s glcontextsize = new SbVec2s();
		  public /*GL2*/int contextid;

		  public fbo_data fbodata; //ptr
		  public SoGLImage glimage; //ptr
		  public int glimagecontext;
		  
		  public boolean buffervalid;

		  public boolean glimagevalid;
		  public boolean glrectangle;

		  public SoGLRenderAction glaction; //ptr

		  public boolean shouldCreateMipmap(SoState state) {
		    float q = SoTextureQualityElement.get(state);
		    return q > 0.5f;
		  }

		//#ifdef COIN_THREADSAFE
		  public final SbMutex mutex = new SbMutex();
		//#endif // COIN_THREADSAFE
		  public boolean canrendertotexture;
		  public MemoryBuffer offscreenbuffer;
		  public int offscreenbuffersize;
		  
		  
		  public SoSceneTexture2P(SoSceneTexture2 apiptr)
		  {
		    this.api = apiptr;
		    this.glcontext = null;
		    this.buffervalid = false;
		    this.glimagevalid = false;
		    this.glimage = null;
		    this.glimagecontext = 0;
		    this.glaction = null;
		    this.glcontextsize.setValue((short)-1,(short)-1);
		    this.glrectangle = false;
		    this.offscreenbuffer = null;
		    this.offscreenbuffersize = 0;
		    this.canrendertotexture = false;
		    this.contextid = -1; //YB
		    this.fbodata = null;
		  }

		  public void destructor()
		  {
		    this.deleteFrameBufferObjects(null, null);
		    Destroyable.delete(this.fbodata);

		    if (this.glimage != null) this.glimage.unref(null);
		    if (this.glcontext != null) {
		      SoGL.cc_glglue_context_destruct(this.glcontext);
		    }
		    //delete[] this.offscreenbuffer; java port
		    Destroyable.delete( this.glaction);
		  }

		  public void
		  updateBuffer(SoState state,  float quality)
		  {
			  GL2 gl2 = state.getGL2();
		    // make sure we've finished rendering to this context
		    gl2.glFlush();
		     cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));

		    boolean candofbo = SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_FRAMEBUFFER_OBJECT);
		    if (candofbo) {
		      // can't render to a FBO if we have a delayed transparency type
		      // involving path traversal in a second pass.
		      switch (this.getTransparencyType(state)) {
		      case NONE:
		      case BLEND:
		      case ADD:
		      case SCREEN_DOOR:
		        break;
		      default:
		        candofbo = false;
		        break;
		      }
		    }

		    if (!candofbo) {
		      this.updatePBuffer(state, quality);
		    }
		    else {
		      this.updateFrameBuffer(state, quality);
		    }
		  }

		  public void
		  updateFrameBuffer(SoState state,  float quality)
		  {
		    int i;
		    final SbVec2s size = new SbVec2s(this.api.size.getValue());
		    SoNode scene = this.api.scene.getValue();
		    assert(scene != null);

		    int cachecontext = SoGLCacheContextElement.get(state);
		     cc_glglue glue = SoGL.cc_glglue_instance(cachecontext);
		    boolean mipmap = this.shouldCreateMipmap(state);

		    fbo_data fbodata = this.fbodata;
		    if (fbodata == null) {
		      this.fbodata = fbodata = new fbo_data(0);
		    }

		    if ((fbodata.fbo_size.operator_not_equal( size)) || (mipmap != fbodata.fbo_mipmap) || 
		        (!Objects.equals(cachecontext, fbodata.cachecontext))) {
		      fbodata.fbo_mipmap = mipmap;
		      fbodata.fbo_size.copyFrom(size);
		      fbodata.cachecontext = cachecontext;
		      
		      if (this.glimage != null) {
		        this.glimage.unref(null);
		        this.glimage = null;
		        this.glimagecontext = 0;
		      }
		      if (this.glimage == null) {
		        this.glimage = new SoGLImage();
		        this.glimagecontext = SoGLCacheContextElement.get(state);
		        int flags = this.glimage.getFlags();
		        switch (SoSceneTexture2.TransparencyFunction.fromValue(this.api.transparencyFunction.getValue())) {
		        case NONE:
		          flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_FALSE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_FALSE.getValue();
		          break;
		        case ALPHA_TEST:
		          flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_TRUE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_TRUE.getValue();
		          break;
		        case ALPHA_BLEND:
		          flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_TRUE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_FALSE.getValue();
		          break;
		        default:
		          assert(false);// && "should not get here");
		          break;
		        }
		        this.glimage.setFlags(flags);
		      }

		      boolean finished = false;

		      SoSceneTexture2.Type type = SoSceneTexture2.Type.fromValue(this.api.type.getValue());
		      while (!finished) {
		        this.deleteFrameBufferObjects(glue, state);
		        finished = true;
		        boolean warn = type == SoSceneTexture2.Type.RGBA32F ? false : true;

		        if (!this.createFramebufferObjects(glue, state, type, warn)) {
		          if (type == SoSceneTexture2.Type.RGBA32F) { // common case. Fall back to 16 bit floating point textures
		            type = SoSceneTexture2.Type.RGBA16F;
		            finished = false;
		          }
		        }
		      }

		      // FIXME: for some reason we need to do this every frame. Investigate why.
		      if (this.api.type.getValue() == SoSceneTexture2.Type.DEPTH.getValue()) {
		        assert(fbodata.fbo_depthmap != null);
		        this.glimage.setGLDisplayList(fbodata.fbo_depthmap, state,
		                                        SoGLImage.Wrap.CLAMP, SoGLImage.Wrap.CLAMP);
		      }
		      else {
		        assert(fbodata.fbo_texture != null);
		        this.glimage.setGLDisplayList(fbodata.fbo_texture, state);
		      }
		    }

		    state.push();

		    // reset OpenGL/Coin state
		    SoGLShaderProgramElement.enable(state, false);
		    SoLazyElement.setToDefault(state);
		    SoShapeStyleElement.setTransparencyType(state, (int) this.getTransparencyType(state).getValue());
		    SoLazyElement.setTransparencyType(state, (int) this.getTransparencyType(state).getValue());

		    // disable all active textures
		    SoMultiTextureEnabledElement.disableAll(state);

		    GL2 gl2 = state.getGL2();
		    
		    // just disable all active light source
		    int numlights = SoLightElement.getLights(state).getLength();
		    for (i = 0; i < numlights; i++) {
		      gl2.glDisable((int) (GL2.GL_LIGHT0 + i));
		    }
		    float[] oldclearcolor = new float[4];
		    gl2.glGetFloatv(GL2.GL_COLOR_CLEAR_VALUE, oldclearcolor);

		    SoModelMatrixElement.set(state, this.api, SbMatrix.identity());

		    // store current framebuffer
		    int[] oldfb = new int[1];
		    gl2.glGetIntegerv( GL2.GL_FRAMEBUFFER_BINDING/*_EXT*/, oldfb,0 );

		    // set up framebuffer for rendering
		    SoGL.cc_glglue_glBindFramebuffer(glue, GL2.GL_FRAMEBUFFER/*_EXT*/, fbodata.fbo_frameBuffer);
		    this.checkFramebufferStatus(glue, true);

		    SoViewportRegionElement.set(state, new SbViewportRegion(fbodata.fbo_size));
		    final SbVec4f col = new SbVec4f(this.api.backgroundColor.getValue());
		    gl2.glClearColor(col.getValueRead()[0], col.getValueRead()[1], col.getValueRead()[2], col.getValueRead()[3]);
		    gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT|GL2.GL_COLOR_BUFFER_BIT);

		    SoGLRenderAction glaction = (SoGLRenderAction) state.getAction();
		    // traverse the new scene graph

		    // clear the abort callback before traversing the internal scene
		    // graph. This will make the FBO version behave like the pbuffer
		    // version, and avoid problems with the offscreen renderer which
		    // uses the abort callback to adjust the original viewport.

		    final SoGLRenderAction.SoGLRenderAbortCB[] old_func = new SoGLRenderAction.SoGLRenderAbortCB[1];
		    final Object[] old_userdata = new Object[1];
		    glaction.getAbortCallback(old_func, old_userdata);
		    glaction.setAbortCallback(null, null);
		    glaction.switchToNodeTraversal(scene);
		    glaction.setAbortCallback(old_func[0], old_userdata[0]);

		    // make sure rendering has completed before switching back to the previous context
		    gl2.glFlush();

		    if (this.api.type.getValue() == SoSceneTexture2.Type.DEPTH.getValue()) {
		      // need to copy the depth buffer into the depth texture object
		      SoGL.cc_glglue_glBindTexture(glue,GL2.GL_TEXTURE_2D, fbodata.fbo_depthmap.getFirstIndex());
		      gl2.glCopyTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, 0, 0,
		                          fbodata.fbo_size.getValue()[0], fbodata.fbo_size.getValue()[1]);
		      SoGL.cc_glglue_glBindTexture(glue, GL2.GL_TEXTURE_2D, 0);
		    }
		    else {
		      SoGL.cc_glglue_glBindTexture(glue,GL2.GL_TEXTURE_2D, fbodata.fbo_texture.getFirstIndex());
		      if (fbodata.fbo_mipmap) {
		        SoGL.cc_glglue_glGenerateMipmap(glue, GL2.GL_TEXTURE_2D);
		      }
		      SoGL.cc_glglue_glBindTexture(glue,GL2.GL_TEXTURE_2D, 0);
		    }

		    SoGL.cc_glglue_glBindFramebuffer(glue, GL2.GL_FRAMEBUFFER/*_EXT*/, (int)oldfb[0]);
		    this.checkFramebufferStatus(glue, true);


		    // restore old clear color
		    gl2.glClearColor(oldclearcolor[0], oldclearcolor[1], oldclearcolor[2], oldclearcolor[3]);

		    // enable lights again
		    for (i = 0; i < numlights; i++) {
		      gl2.glEnable((int) (GL2.GL_LIGHT0 + i));
		    }
		    state.pop();

		    SoGLLazyElement.getInstance(state).reset(state,
		                                               SoLazyElement.masks.LIGHT_MODEL_MASK.getValue()|
		                                               SoLazyElement.masks.TWOSIDE_MASK.getValue()|
		                                               SoLazyElement.masks.SHADE_MODEL_MASK.getValue());

		    this.buffervalid = true;
		    this.glimagevalid = true;
		  }

          static int didwarn = 0;
          
		  public void
		  updatePBuffer(SoState state,  float quality)
		  {
			  GL2 gl2 = state.getGL2();
			  
		    SbVec2s size = new SbVec2s(this.api.size.getValue());

		    SoNode scene = this.api.scene.getValue();
		    assert(scene != null);

		    if ((this.glcontext != null && this.glcontextsize.operator_not_equal(size)) || (size.operator_equal_equal(new SbVec2s((short)0,(short)0)))) {
		      if (this.glimage != null) {
		        this.glimage.unref(state);
		        this.glimage = null;
		        this.glimagecontext = 0;
		      }
		      if (this.glcontext != null) {
		        SoGL.cc_glglue_context_destruct(this.glcontext);
		        this.glcontextsize.setValue((short)-1,(short)-1);
		        this.glcontext = null;
		      }
		      if (this.glaction != null) {
		        // Note: Recreating the glaction (below) will also get us a new contextid.
		        Destroyable.delete( this.glaction);
		        this.glaction = null;
		      }
		      this.glimagevalid = false;
		    }
		    if (size.operator_equal_equal(new SbVec2s((short)0,(short)0))) return;

		    // FIXME: temporary until non power of two textures are supported,
		    // pederb 2003-12-05 YB
		    size.getValue()[0] = (short) /*Tidbits.coin_geq_power_of_two(*/size.getValue()[0]/*)*/;
		    size.getValue()[1] = (short) /*Tidbits.coin_geq_power_of_two(*/size.getValue()[1]/*)*/;

		    if (this.glcontext == null) {
		      this.glcontextsize.copyFrom( size);
		       // disabled until an pbuffer extension is available to create a
		      // render-to-texture pbuffer that has a non power of two size.
		      // pederb, 2003-12-05
		      if (1 != 0) {
		        this.glcontextsize.getValue()[0] = (short) /*Tidbits.coin_geq_power_of_two(*/size.getValue()[0]/*)*/;
		        this.glcontextsize.getValue()[1] = (short) /*Tidbits.coin_geq_power_of_two(*/size.getValue()[1]/*)*/;

		        if (this.glcontextsize.operator_not_equal(size)) {
		          if (didwarn == 0) {
		            SoDebugError.postWarning("SoSceneTexture2P.updatePBuffer",
		                                      "Requested non power of two size, but your OpenGL "+
		                                      "driver lacks support for such pbuffer textures.");
		            didwarn = 1;
		          }
		        }
		      }
		      this.glrectangle = false;
		      if (!Tidbits.coin_is_power_of_two(this.glcontextsize.getValue()[0]) ||
		          !Tidbits.coin_is_power_of_two(this.glcontextsize.getValue()[1])) {
		        // we only get here if the OpenGL driver can handle non power of
		        // two textures/pbuffers.
		        this.glrectangle = true;
		      }
		      // FIXME: make it possible to specify what kind of context you want
		      // (RGB or RGBA, I guess). pederb, 2003-11-27
		      this.glcontext = SoGL.cc_glglue_context_create_offscreen(this.glcontextsize.getValue()[0],
		                                                           this.glcontextsize.getValue()[1]);
		      this.canrendertotexture = SoGL.cc_glglue_context_can_render_to_texture(this.glcontext);

		      if (this.glaction == null) {
		        this.contextid = SoGLCacheContextElement.getUniqueCacheContext();
		        this.glaction = new SoGLRenderAction(new SbViewportRegion(this.glcontextsize));
		        this.glaction.addPreRenderCallback(SoSceneTexture2P::prerendercb,
		                                             (Object) this.api);
		      } else {
		        this.glaction.setViewportRegion(new SbViewportRegion(this.glcontextsize));
		      }

		      this.glaction.setTransparencyType(this.getTransparencyType(state));
		      this.glaction.setCacheContext(this.contextid);
		      this.glimagevalid = false;
		    }

		    if (!this.buffervalid) {
		      assert(this.glaction != null);
		      assert(this.glcontext != null);
		      this.glaction.setTransparencyType(SoGLRenderAction.TransparencyType.fromValue(
		                                          SoShapeStyleElement.getTransparencyType(state)));

		      SoGL.cc_glglue_context_make_current(this.glcontext);
		      gl2.glEnable(GL2.GL_DEPTH_TEST);
		      this.glaction.apply(scene);
		      // Make sure that rendering to pBuffer is completed to avoid
		      // flickering. DON'T REMOVE THIS. You have been warned.
		      gl2.glFlush();

		      if (!this.canrendertotexture) {
		        final SbVec2s size1 = new SbVec2s(this.glcontextsize);
		        int reqbytes = ((int)size1.getValue()[0])*size1.getValue()[1]*4;
		        if (reqbytes > this.offscreenbuffersize) {
		          //delete[] this.offscreenbuffer; java port
		          this.offscreenbuffer = MemoryBuffer.allocateBytes(reqbytes);//new /*unsigned char*/byte[reqbytes];
		          this.offscreenbuffersize = reqbytes;
		        }
		        gl2.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 1);
		        gl2.glReadPixels(0, 0, size1.getValue()[0], size1.getValue()[1], GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE,
		                     this.offscreenbuffer);
		        gl2.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 4);
		      }
		      
		      SoGL.cc_glglue_context_reinstate_previous(this.glcontext);
		    }
		    if (!this.glimagevalid || (this.glimage == null)) {
		      // just delete old glimage
		      if (this.glimage != null) {
		        this.glimage.unref(state);
		        this.glimage = null;
		      }
		      this.glimage = new SoGLImage();
		      this.glimagecontext = SoGLCacheContextElement.get(state);
		      int flags = this.glimage.getFlags();
		      if (this.glrectangle) {
		        flags |= SoGLImage.Flags.RECTANGLE.getValue();
		      }
		      switch (SoSceneTexture2.TransparencyFunction.fromValue(this.api.transparencyFunction.getValue())) {
		      case NONE:
		        flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_FALSE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_FALSE.getValue();
		        break;
		      case ALPHA_TEST:
		        flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_TRUE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_TRUE.getValue();
		        break;
		      case ALPHA_BLEND:
		        flags |= SoGLImage.Flags.FORCE_TRANSPARENCY_TRUE.getValue()|SoGLImage.Flags.FORCE_ALPHA_TEST_FALSE.getValue();
		        break;
		      default:
		        assert(false);// && "should not get here");
		        break;
		      }
		      if (this.canrendertotexture) {
		        // bind texture to pbuffer
		        this.glimage.setPBuffer(state, this.glcontext,
		        		SoSceneTexture2.translateWrap(SoSceneTexture2.Wrap.fromValue(this.api.wrapS.getValue())),
		        		SoSceneTexture2.translateWrap(SoSceneTexture2.Wrap.fromValue(this.api.wrapT.getValue())),
		                                  quality);
		      }
		      this.glimage.setFlags(flags);
		    }
		    if (!this.canrendertotexture) {
		      assert(this.glimage != null);
		      assert(this.offscreenbuffer != null);
		      this.glimage.setData(this.offscreenbuffer,
		                             this.glcontextsize,
		                             4,
		                             SoSceneTexture2.translateWrap(SoSceneTexture2.Wrap.fromValue(this.api.wrapS.getValue())),
		                             SoSceneTexture2.translateWrap(SoSceneTexture2.Wrap.fromValue(this.api.wrapT.getValue())),
		                             quality);
		    }
		    this.glimagevalid = true;
		    this.buffervalid = true;
		  }

		  public static void
		  prerendercb(Object userdata, SoGLRenderAction action)
		  {
		    SoSceneTexture2 thisp = (SoSceneTexture2) userdata;
		    SbVec4f col = thisp.backgroundColor.getValue();
		    
		    GL2 gl2 = new GL2() {};
		    
		    gl2.glClearColor(col.getValueRead()[0], col.getValueRead()[1], col.getValueRead()[2], col.getValueRead()[3]);
		    gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT|GL2.GL_COLOR_BUFFER_BIT);
		  }

		  public boolean
		  createFramebufferObjects( cc_glglue glue, SoState state,
		                                              SoSceneTexture2.Type type,
		                                              boolean warn)
		  {
		    fbo_data fbodata = this.fbodata;
		    assert(fbodata != null);  

		    assert(fbodata.fbo_texture == null);
		    assert(fbodata.fbo_depthmap == null);
		    assert(fbodata.fbo_frameBuffer == GL2.GL_INVALID_VALUE);
		    assert(fbodata.fbo_depthBuffer == GL2.GL_INVALID_VALUE);

		    GL2 gl2 = state.getGL2();
		    
		    // store old framebuffer
		    final int[] oldfb = new int[1];
		    gl2.glGetIntegerv( GL2.GL_FRAMEBUFFER_BINDING/*_EXT*/, oldfb, 0 );

		    fbodata.fbo_frameBuffer = SoGL.cc_glglue_glGenFramebuffers(glue/*, 1, fbodata.fbo_frameBuffer*/); //java port
		    fbodata.fbo_depthBuffer = SoGL.cc_glglue_glGenRenderbuffers(glue/*, 1, fbodata.fbo_depthBuffer*/); // java port
		    SoGL.cc_glglue_glBindFramebuffer(glue, GL2.GL_FRAMEBUFFER/*_EXT*/, fbodata.fbo_frameBuffer);

		    fbodata.fbo_texture = new SoGLDisplayList(state, SoGLDisplayList.Type.TEXTURE_OBJECT);
		    fbodata.fbo_texture.ref();
		    fbodata.fbo_texture.open(state);

		    int gltype = GL2.GL_FLOAT;
		    final int[] internalformat = new int[1]; internalformat[0] = GL2.GL_RGBA8;
		    final int[] format = new int[1]; format[0] = GL2.GL_RGBA;

		    SoSceneTexture2.soscenetexture2_translate_type(type, internalformat, format);

		    switch (SoSceneTexture2.Type.fromValue(this.api.type.getValue())) {
		    case RGBA8:
		    case DEPTH:
		      gltype = GL2.GL_UNSIGNED_BYTE;
		      break;
		    default:
		      break;
		    }

		    gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0,
		                 internalformat[0],
		                 fbodata.fbo_size.getValue()[0], fbodata.fbo_size.getValue()[1],
		                 0, /* border */
		                 format[0],
		                 gltype, /*null*/0);

		    // for mipmaps
		    // FIXME: add support for CLAMP_TO_BORDER in SoSceneTexture2 and SoTextureImageElement

		    int wraps = (int) this.api.wrapS.getValue();
		    int wrapt = (int) this.api.wrapT.getValue();

		    boolean clamptoborder_ok =
		      SoGLDriverDatabase.isSupported(glue, "GL_ARB_texture_border_clamp") ||
		      SoGLDriverDatabase.isSupported(glue, "GL_SGIS_texture_border_clamp");

		    if (wraps == GL2.GL_CLAMP_TO_BORDER && !clamptoborder_ok) wraps = GL2.GL_CLAMP;
		    if (wrapt == GL2.GL_CLAMP_TO_BORDER && !clamptoborder_ok) wrapt = GL2.GL_CLAMP;

		    gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, wraps);
		    gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, wrapt);

		    gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, fbodata.fbo_mipmap ? GL2.GL_LINEAR_MIPMAP_LINEAR : GL2.GL_LINEAR);
		    gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		    if (fbodata.fbo_mipmap) {
		      SoGL.cc_glglue_glGenerateMipmap(glue, GL2.GL_TEXTURE_2D);
		    }

		    if (SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ANISOTROPIC_FILTERING)) {
		      gl2.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT,
		                      SoGL.cc_glglue_get_max_anisotropy(glue));
		    }

		    fbodata.fbo_texture.close(state);

		    if (type == SoSceneTexture2.Type.DEPTH) {
		      fbodata.fbo_depthmap = new SoGLDisplayList(state, SoGLDisplayList.Type.TEXTURE_OBJECT);
		      fbodata.fbo_depthmap.ref();
		      fbodata.fbo_depthmap.open(state);

		      gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0,
		                   GL2.GL_DEPTH_COMPONENT, /* GL_DEPTH_COMPONENT24? */
		                   fbodata.fbo_size.getValue()[0], fbodata.fbo_size.getValue()[1],
		                   0, /* border */
		                   GL2.GL_DEPTH_COMPONENT,
		                   GL2.GL_UNSIGNED_BYTE, /*null*/0);

		      if (SoGLDriverDatabase.isSupported(glue, "GL_ARB_texture_border_clamp") ||
		          SoGLDriverDatabase.isSupported(glue, "GL_SGIS_texture_border_clamp")) {
		        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_BORDER);
		        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_BORDER);
		      }
		      else {
		        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		      }
		      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
		      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_COMPARE_FUNC, GL2.GL_LEQUAL);

		      if (SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ANISOTROPIC_FILTERING)) {
		        gl2.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT,
		                        SoGL.cc_glglue_get_max_anisotropy(glue));
		      }

		      fbodata.fbo_depthmap.close(state);
		    }

		    if (fbodata.fbo_texture != null) {
		      // attach texture to framebuffer color object
		      SoGL.cc_glglue_glFramebufferTexture2D(glue,
		                                       GL2.GL_FRAMEBUFFER/*_EXT*/,
		                                       GL2.GL_COLOR_ATTACHMENT0/*_EXT*/,
		                                       GL2.GL_TEXTURE_2D,
		                                       (int) fbodata.fbo_texture.getFirstIndex(),
		                                       0);
		    }

		    // create the render buffer
		    SoGL.cc_glglue_glBindRenderbuffer(glue, GL2.GL_RENDERBUFFER/*_EXT*/, fbodata.fbo_depthBuffer);
		    SoGL.cc_glglue_glRenderbufferStorage(glue, GL2.GL_RENDERBUFFER/*_EXT*/,
		                                    GL2.GL_DEPTH_COMPONENT24,
		                                    fbodata.fbo_size.getValue()[0], fbodata.fbo_size.getValue()[1]);
		    // attach renderbuffer to framebuffer
		    SoGL.cc_glglue_glFramebufferRenderbuffer(glue,
		                                        GL2.GL_FRAMEBUFFER/*_EXT*/,
		                                        GL2.GL_DEPTH_ATTACHMENT/*_EXT*/,
		                                        GL2.GL_RENDERBUFFER/*_EXT*/,
		                                        fbodata.fbo_depthBuffer);

		    boolean ret = this.checkFramebufferStatus(glue, warn);
		    SoGL.cc_glglue_glBindFramebuffer(glue, GL2.GL_FRAMEBUFFER/*_EXT*/, (int)oldfb[0]);

		    return ret;
		  }

		  public void
		  deleteFrameBufferObjects( cc_glglue glue, SoState state)
		  {
		    fbo_data fbodata = this.fbodata;
		    if (fbodata == null) return; // might happen if the scene texture isn't traversed
		    
		    if (fbodata.fbo_texture != null) {
		      fbodata.fbo_texture.unref(state);
		      fbodata.fbo_texture = null;
		    }
		    if (fbodata.fbo_depthmap != null) {
		      fbodata.fbo_depthmap.unref(state);
		      fbodata.fbo_depthmap = null;
		    }
		    if (glue != null && state != null && SoGLCacheContextElement.get(state) == fbodata.cachecontext) {
		      if (fbodata.fbo_frameBuffer != GL2.GL_INVALID_VALUE) {
		        SoGL.cc_glglue_glDeleteFramebuffers(glue, 1, fbodata.fbo_frameBuffer);
		        fbodata.fbo_frameBuffer = GL2.GL_INVALID_VALUE;
		      }
		      if (fbodata.fbo_depthBuffer != GL2.GL_INVALID_VALUE) {
		        SoGL.cc_glglue_glDeleteRenderbuffers(glue, 1, fbodata.fbo_depthBuffer);
		        fbodata.fbo_depthBuffer = GL2.GL_INVALID_VALUE;
		      }
		    }
		    else {
		      SoSceneTexture2.fbo_deletedata dd = new SoSceneTexture2.fbo_deletedata();
		      dd.frameBuffer = fbodata.fbo_frameBuffer;
		      dd.depthBuffer = fbodata.fbo_depthBuffer;
		      SoGLCacheContextElement.scheduleDeleteCallback(fbodata.cachecontext,
		    		  SoSceneTexture2::fbo_delete_cb, dd);
		    }
		    fbodata.fbo_frameBuffer = GL2.GL_INVALID_VALUE;
		    fbodata.fbo_depthBuffer = GL2.GL_INVALID_VALUE;
		  }

		  public boolean
		  checkFramebufferStatus( cc_glglue glue,  boolean warn)
		  {
		    // check if the buffers have been successfully set up
		    int status = SoGL.cc_glglue_glCheckFramebufferStatus(glue, GL2.GL_FRAMEBUFFER/*_EXT*/);
		    String error = "";
		    switch (status){
		    case GL2.GL_FRAMEBUFFER_COMPLETE/*_EXT*/:
		      break;
		    case GL2.GL_FRAMEBUFFER_UNSUPPORTED/*_EXT*/:
		      error = "GL_FRAMEBUFFER_UNSUPPORTED_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT\n";
		      break;
		    case GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER/*_EXT*/:
		      error = "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT\n";
		      break;
		    default: break;
		    }
		    if (error != "") {
		      if (warn) {
		        SoDebugError.post("SoSceneTexture2P.createFramebufferObjects",
		                           "GL Framebuffer error: "+ error/*.getString()*/);
		      }
		      return false;
		    }
		    return true;
		  }

		  public SoGLRenderAction.TransparencyType
		  getTransparencyType(SoState state)
		  {
		    SoNode node = this.api.sceneTransparencyType.getValue();
		    if (node != null && node.isOfType(SoTransparencyType.getClassTypeId())) {
		      return SoGLRenderAction.TransparencyType.fromValue(
		        ((SoTransparencyType)node).value.getValue());
		    }
		    return SoGLRenderAction.TransparencyType.fromValue(
		      SoShapeStyleElement.getTransparencyType(state));
		  }


		  
}
