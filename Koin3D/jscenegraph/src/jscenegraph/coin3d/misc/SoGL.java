package jscenegraph.coin3d.misc;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.glue.Wglglue_contextdata;
import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.bundles.SoVertexAttributeBundle;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.errors.DebugError;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoBasic;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;
import jscenegraph.port.FloatArray;
import jscenegraph.port.FloatBufferAble;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.MutableSbVec3fArray;
import jscenegraph.port.SbColorArray;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.SbVec4fArray;
import jscenegraph.port.Util;
import jscenegraph.port.VoidPtr;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

public class SoGL {

	// flags for cone, cylinder and cube

	public static final int SOGL_RENDER_SIDE         =0x01;
	public static final int SOGL_RENDER_TOP          =0x02;
	public static final int SOGL_RENDER_BOTTOM       =0x04;
	public static final int SOGL_MATERIAL_PER_PART   =0x08;
	public static final int SOGL_NEED_NORMALS        =0x10;
	public static final int SOGL_NEED_TEXCOORDS      =0x20;
	public static final int SOGL_NEED_3DTEXCOORDS    =0x40;
	public static final int SOGL_NEED_MULTITEXCOORDS =0x80;// internal

	
	static int COIN_MAXIMUM_TEXTURE2_SIZE = -1;
	static int COIN_MAXIMUM_TEXTURE3_SIZE = -1;
	
	static int didwarn = 0;

	static cc_glglue_offscreen_cb_functions offscreen_cb = null;
	
	// Convenience function for access to OpenGL wrapper from an SoState
// pointer.
public static cc_glglue 
sogl_glue_instance( SoState  state)
{
  SoGLRenderAction  action = (SoGLRenderAction )state.getAction();
  // FIXME: disabled until we figure out why this doesn't work on some
  // Linux systems (gcc 3.2 systems, it seems). pederb, 2003-11-24
//#if 0
//  assert(action.isOfType(SoGLRenderAction.getClassTypeId()) &&
//         "must have state from SoGLRenderAction to get hold of GL wrapper");
//  return cc_glglue_instance(action.getCacheContext());
//#else // disabled
  if (action.isOfType(SoGLRenderAction.getClassTypeId())) {
    return cc_glglue_instance(action.getCacheContext());
  }
  if (didwarn == 0) {
    didwarn = 1;
    SoDebugError.postWarning("sogl_glue_instance",
                              "Wrong action type detected. Please report this to <coin-support@sim.no>, "+
                              "and include information about your system (compiler, Linux version, etc.");
  }
  // just return some cc_glglue instance. It usually doesn't matter
  // that much unless multiple contexts on multiple displays are used.
  return cc_glglue_instance(1);
//#endif // workaround version
}

static Map<Integer, cc_glglue> glues = new HashMap<>();

// java port
public static cc_glglue cc_glglue_instance(int cacheContext) {
	
	cc_glglue glue = glues.get(cacheContext);
	if(glue == null) {
		glue = new cc_glglue(cacheContext);
		glues.put(cacheContext, glue);
	}
	return glue;
}


// Needed until all logic in SoGLTextureImageElement is moved to SoGLMultiTextureImageElement
//public static void
//sogl_update_shapehints_transparency(SoState state)
//{
//  SoShapeStyleElement.setTransparentTexture(state, 
//                                             SoGLTextureImageElement.hasTransparency(state) ||
//                                             SoGLMultiTextureImageElement.hasTransparency(state));
//}

static int COIN_GLERROR_DEBUGGING = -1;

// Used by library code to decide whether or not to add extra
// debugging checks for glGetError().
public static boolean
sogl_glerror_debugging()
{
  if (COIN_GLERROR_DEBUGGING == -1) {
    String str = /*coin_getenv*/System.getenv("COIN_GLERROR_DEBUGGING");
    COIN_GLERROR_DEBUGGING = ((str!=null) ? Util.atoi(str) : 0);
  }
  return (COIN_GLERROR_DEBUGGING == 0) ? false : true;
}

public static cc_glglue
instance_from_context_ptr(Object ctx)
{
  /* The id can really be anything unique for the current context, but
     we should avoid a crash with the possible ids defined by
     SoGLCacheContextElement. It's a bit of a hack, this. */

  /* MSVC7 on 64-bit Windows wants this extra cast. */
  int cast_aid = (int)ctx;
  /* FIXME: holy shit! This doesn't look sensible at all! (Could this
     e.g. be where the remote rendering bugs are coming from?)
     20050525 mortene.*/
  int id = (int)cast_aid;

  return cc_glglue_instance(id);
}


public static void
cc_glglue_glActiveTexture(final cc_glglue w,
                          /*GLenum*/int texture)
{
  //assert(w.glActiveTexture);
  w.getGL2().glActiveTexture(texture);
}

/*!
Note that the \e internalformat parameter corresponds to the \e
internalFormat parameter to glTexImage2D; either the number of
components per texel or a constant specifying the internal texture format.
*/
public static boolean
coin_glglue_is_texture_size_legal( cc_glglue glw,
                                int xsize, int ysize, int zsize,
                                int internalformat,
                                int format,
                                int type,
                                boolean mipmap)
{
if (zsize == 0) { /* 2D textures */
  if (COIN_MAXIMUM_TEXTURE2_SIZE > 0) {
    if (xsize > COIN_MAXIMUM_TEXTURE2_SIZE) return false;
    if (ysize > COIN_MAXIMUM_TEXTURE2_SIZE) return false;
    return true;
  }
  if (SoGL.cc_glglue_has_2d_proxy_textures(glw)) {
    return SoGL.proxy_mipmap_2d(xsize, ysize, internalformat, format, type, mipmap, glw.getGL2());
  }
  else {
    if (xsize > glw.max_texture_size) return false;
    if (ysize > glw.max_texture_size) return false;
    return true;
  }
}
else { /*  3D textures */
  if (SoGL.cc_glglue_has_3d_textures(glw)) {
    if (COIN_MAXIMUM_TEXTURE3_SIZE > 0) {
      if (xsize > COIN_MAXIMUM_TEXTURE3_SIZE) return false;
      if (ysize > COIN_MAXIMUM_TEXTURE3_SIZE) return false;
      if (zsize > COIN_MAXIMUM_TEXTURE3_SIZE) return false;
      return true;
    }
    return SoGL.proxy_mipmap_3d(glw, xsize, ysize, zsize, internalformat, format, type, mipmap,glw.getGL2());
  }
  else {
//#if COIN_DEBUG
    boolean first = true;
    if (first) {
      DebugError.cc_debugerror_post("glglue_is_texture_size_legal",
                         "3D not supported with this OpenGL driver");
      first = false;
    }
//#endif /*  COIN_DEBUG */
    return false;
  }
}
}

private static boolean cc_glglue_has_3d_textures(cc_glglue glw) {
	return true;
}

/*  proxy mipmap creation */
static boolean
proxy_mipmap_2d(int width, int height,
                int internalFormat,
                int format,
                int type,
                boolean mipmap, GL2 gl2)
{
  final int[] w = new int[1];
  int level;
  int levels = compute_log(SoBasic.cc_max(width, height));

  gl2.glTexImage2D(GL2.GL_PROXY_TEXTURE_2D, 0, internalFormat, width, height, 0,
               format, type, /*null*/0);
  gl2.glGetTexLevelParameteriv(GL2.GL_PROXY_TEXTURE_2D, 0,
                           GL2.GL_TEXTURE_WIDTH, w);

  if (w[0] == 0) return false;
  if (!mipmap) return true;

  for (level = 1; level <= levels; level++) {
    if (width > 1) width >>= 1;
    if (height > 1) height >>= 1;
    gl2.glTexImage2D(GL2.GL_PROXY_TEXTURE_2D, level, internalFormat, width,
                 height, 0, format, type,
                 /*null*/0);
    gl2.glGetTexLevelParameteriv(GL2.GL_PROXY_TEXTURE_2D, 0,
                             GL2.GL_TEXTURE_WIDTH, w);
    if (w[0] == 0) return false;
  }
  return true;
}

static int
compute_log(int value)
{
  int i = 0;
  while (value > 1) { value>>=1; i++; }
  return i;
}

public static boolean
cc_glglue_has_2d_proxy_textures( cc_glglue w)
{
  if (!glglue_allow_newer_opengl(w)) return false;

  // Our Proxy code seems to not be compatible with Intel drivers
  // FIXME: should be handled by SoGLDriverDatabase
  if (w.vendor_is_intel) return false;

  /* FIXME: there are differences between the 1.1 proxy mechanisms and
     the GL_EXT_texture proxy extension; the 1.1 support considers
     mipmaps. I think. Check documentation in the GL spec. If that is
     correct, we can't really use them interchangeable versus each
     other like we now do in Coin code. 20030121 mortene. */
  return
    cc_glglue_glversion_matches_at_least(w, 1, 1, 0) ||
    cc_glglue_glext_supported(w, "GL_EXT_texture");
}

/* Returns a flag which indicates whether or not to allow the use of
OpenGL 1.1+ features and extensions.

We default to *not* allowing this if rendering is indirect, as
we've seen major problems with at least NVidia GLX when using
OpenGL 1.1+ features. It can be forced on by an environment
variable, though.

(A better strategy *might* be to default to allow it, but to smoke
out and warn if we detect NVidia GLX, and in addition to provide an
environment variable that disables it.)
*/
public static boolean
glglue_allow_newer_opengl( cc_glglue w)
{
	return true;
}


public static boolean
cc_glglue_glversion_matches_at_least( cc_glglue w,
                                     int major,
                                     int minor,
                                     int revision)
{
  final int[] glmajor = new int[1], glminor = new int[1], glrev = new int[1];
  cc_glglue_glversion(w, glmajor, glminor, glrev);

  if (glmajor[0] < major) return false;
  else if (glmajor[0] > major) return true;
  if (glminor[0] < minor) return false;
  else if (glminor[0] > minor) return true;
  if (glminor[0] < revision) return false;
  return true;
}

public static void
cc_glglue_glversion( cc_glglue  w,
                    int[] major,
                    int[] minor,
                    int[] release)
{
  if (!glglue_allow_newer_opengl(w)) {
    major[0] = 1;
    minor[0] = 0;
    release[0] = 0;
  }
  else {
    major[0] = w.version.major;
    minor[0] = w.version.minor;
    release[0] = w.version.release;
  }
}

public static boolean
cc_glglue_glext_supported( cc_glglue wrapper, String extension)
{
	/*
  uintptr_t key = (uintptr_t)cc_namemap_get_address(extension);

  void * result = null;
  if (cc_dict_get(wrapper.glextdict, key, &result)) {
    return result != null;
  }
  result = coin_glglue_extension_available(wrapper.extensionsstr, extension) ?
    (void*) 1 : null;
  cc_dict_put(wrapper.glextdict, key, result);

  return result != null;*/
	return true;
}


/* proxy mipmap creation. 3D version. */
public static boolean
proxy_mipmap_3d( cc_glglue glw, int width, int height, int depth,
                int internalFormat,
                int format,
                int type,
                boolean mipmap, GL2 gl2)
{
  final int[] w = new int[1];
  int level;
  int levels = compute_log(SoBasic.cc_max(SoBasic.cc_max(width, height), depth));

  SoGL.cc_glglue_glTexImage3D(glw, GL2.GL_PROXY_TEXTURE_3D, 0, internalFormat,
                         width, height, depth, 0, format, type,
                         null);
  gl2.glGetTexLevelParameteriv(GL2.GL_PROXY_TEXTURE_3D, 0,
                           GL2.GL_TEXTURE_WIDTH, w);
  if (w[0] == 0) return false;
  if (!mipmap) return true;

  for (level = 1; level <= levels; level++) {
    if (width > 1) width >>= 1;
    if (height > 1) height >>= 1;
    if (depth > 1) depth >>= 1;
    SoGL.cc_glglue_glTexImage3D(glw, GL2.GL_PROXY_TEXTURE_3D, level, internalFormat,
                           width, height, depth, 0, format, type,
                           null);
    gl2.glGetTexLevelParameteriv(GL2.GL_PROXY_TEXTURE_3D, 0,
                             GL2.GL_TEXTURE_WIDTH, w);
    if (w[0] == 0) return false;
  }
  return true;
}


public static void
cc_glglue_glTexImage3D(cc_glglue w,
                       int target,
                       int level,
                       int internalformat,
                       int width,
                       int height,
                       int depth,
                       int border,
                       int format,
                       int type,
                       ByteBuffer pixels)
{
  //assert(w.glTexImage3D);
  w.getGL2().glTexImage3D(target, level, internalformat,
                  width, height, depth, border,
                  format, type, pixels);
}

public static int
cc_glglue_max_texture_units( cc_glglue w)
{
  if (!glglue_allow_newer_opengl(w)) return 1;
  return w.maxtextureunits; /* will be 1 when multitexturing is not available */
}


public static void
cc_glglue_glBindFramebuffer(cc_glglue glue, int target, int framebuffer)
{
  assert(glue.has_fbo);
  glue.glBindFramebuffer(target, framebuffer);
}


public static void
cc_glglue_glDeleteFramebuffers(cc_glglue glue, /*GLsizei*/int n, int[] framebuffers)
{
  //assert(glue.has_fbo);
  glue.glDeleteFramebuffers(n, framebuffers);
}

public static void cc_glglue_glDeleteFramebuffers(cc_glglue glue, int n, int frameBuffer) {
	  //assert(glue.has_fbo);
	  glue.glDeleteFramebuffers(n, frameBuffer);
}

public static void cc_glglue_glDeleteRenderbuffers(cc_glglue glue, int n, int renderbuffers) {
	  //assert(glue.has_fbo);
	  glue.glDeleteRenderbuffers(n, renderbuffers);	
}

public static void
cc_glglue_glMultiTexCoord2fv( cc_glglue w,
                             /*GLenum*/int target,
                             float[] v)
{
  //assert(w.glMultiTexCoord2fv);
  w.glMultiTexCoord2fv(target, v);
}

public static void
cc_glglue_glMultiTexCoord3fv( cc_glglue w,
                             /*GLenum*/int target,
                             float[] v)
{
  //assert(w.glMultiTexCoord3fv);
  w.glMultiTexCoord3fv(target, v);
}

public static void
cc_glglue_glMultiTexCoord4fv( cc_glglue w,
                             /*GLenum*/int target,
                             float[] v)
{
  //assert(w.glMultiTexCoord4fv);
  w.glMultiTexCoord4fv(target, v);
}

public static int
cc_glglue_glCheckFramebufferStatus( cc_glglue glue, int target)
{
  assert(glue.has_fbo);
  return glue.glCheckFramebufferStatus(target);
}

public static void cc_glglue_context_destruct(Object ctx) {
	
/* TODO
	  if (offscreen_cb != null && offscreen_cb.destruct != null) {
		    (offscreen_cb.destruct).apply(ctx);
		  } else {
//		#ifdef HAVE_NOGL
	//	  assert(FALSE && "unimplemented");
		//#elif defined(HAVE_GLX)
		  //glxglue_context_destruct(ctx);
		//#elif defined(HAVE_WGL)
		  wglglue_context_destruct(ctx);
		  */
}

public static void cc_glglue_glBindTexture(cc_glglue w, int target, int texture) {
	  //assert(w.glBindTexture);
	  w.glBindTexture(target, texture);

}

public static void cc_glglue_glGenerateMipmap(cc_glglue glue, int target) {
	  glue.glGenerateMipmap(target);
}

public static void cc_glglue_glFramebufferRenderbuffer(cc_glglue glue, int target, int attachment,
		int renderbuffertarget, int renderbuffer) {
	  assert(glue.has_fbo);
	  glue.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
}

public static void
cc_glglue_glRenderbufferStorage( cc_glglue glue, int target, int internalformat, short width, short height)
{
  assert(glue.has_fbo);
  glue.glRenderbufferStorage(target, internalformat, width, height);
}

public static void
cc_glglue_glBindRenderbuffer( cc_glglue glue, int target, int renderbuffer)
{
  assert(glue.has_fbo);
  glue.glBindRenderbuffer(target, renderbuffer);
}

public static void
cc_glglue_glFramebufferTexture2D( cc_glglue glue, int target, int attachment, int textarget, int texture, int level)
{
  assert(glue.has_fbo);
  glue.glFramebufferTexture2D(target, attachment, textarget, texture, level);
}

public static float cc_glglue_get_max_anisotropy(cc_glglue glue) {
	  return glue.max_anisotropy;
}

public static int cc_glglue_glGenFramebuffers(cc_glglue glue/*, int n, int framebuffer*/) {
	  assert(glue.has_fbo);
	  //assert(n==1);
	  int[] framebuffers = new int[1];
	  //framebuffers[0] = framebuffer;
	  glue.glGenFramebuffers(1, framebuffers);
	  
	  return framebuffers[0];
}

public static int cc_glglue_glGenRenderbuffers(cc_glglue glue/*, int n, int renderbuffer*/) {
	  assert(glue.has_fbo);
	  //assert(n==1);
	  int[] renderbuffers = new int[1];
	  //renderbuffers[0] = renderbuffer;
	  glue.glGenRenderbuffers(1, renderbuffers);
	  return renderbuffers[0];
}

public static Object cc_glglue_context_create_offscreen(short width, short height) {
	  if (offscreen_cb != null && offscreen_cb.create_offscreen != null) {
		    return (offscreen_cb.create_offscreen).apply(width, height);
		  } else {
//		#ifdef HAVE_NOGL
//		  assert(FALSE && "unimplemented");
//		  return null;
//		#elif defined(HAVE_GLX)
//		  return glxglue_context_create_offscreen(width, height);
//		#elif defined(HAVE_WGL)
		  return null;// wglglue_context_create_offscreen(width, height); TODO
//		#else
//		#if defined(HAVE_AGL)
//		  check_force_agl();
//		  if (COIN_USE_AGL > 0) return aglglue_context_create_offscreen(width, height); else
//		#endif
//		#if defined(HAVE_CGL)
//		  return cglglue_context_create_offscreen(width, height);
//		#else
//		#endif
//		#endif
}
}

public static boolean cc_glglue_context_can_render_to_texture(Object glcontext) {
	  /* No render-to-texture support in external offscreen rendering. */
	  if (offscreen_cb != null) return false;

	// TODO Auto-generated method stub
	return false;
}

public static boolean cc_glglue_context_make_current(Object ctx) {
	  if (offscreen_cb != null && offscreen_cb.make_current != null) {
		    return offscreen_cb.make_current.apply(ctx);
		  } else {
//		#ifdef HAVE_NOGL
//		  assert(FALSE && "unimplemented");
//		  return FALSE;
//		#elif defined(HAVE_GLX)
//		  return glxglue_context_make_current(ctx);
//		#elif defined(HAVE_WGL)
			  if( ctx instanceof Wglglue_contextdata) {
				  Wglglue_contextdata wglglue_contextdata = (Wglglue_contextdata)ctx;
				  long window = wglglue_contextdata.pbufferwnd;

					// Make the OpenGL context current
					glfwMakeContextCurrent(window);
			        GLCapabilities swtCapabilities = GL.createCapabilities();	        
					return true;
			  }
		  return false;//wglglue_context_make_current(ctx); TODO
		  }
}

public static void cc_glglue_context_reinstate_previous(Object ctx) {
	  /* FIXME: I believe two cc_glglue_context_make_current() invocations
    before invoking this function would make this function behave
    erroneously, as previous contexts are not stacked (at least not
    in the GLX implementation, which I have checked), but only the
    last context is kept track of.

    Probably needs to be fixed. Or at least we should detect and
    assert, if this is not allowed for some reason.

    20040621 mortene. */

 if (offscreen_cb != null && offscreen_cb.reinstate_previous != null) {
   (offscreen_cb.reinstate_previous).apply(ctx);
 } else {
 //wglglue_context_reinstate_previous(ctx); TODO
 }
}

public static void
cc_glglue_glDrawArrays(cc_glglue glue,
                       /*GLenum*/int mode, /*GLint*/int first, /*GLsizei*/int count)
{
  //assert(glue.glDrawArrays);
  glue.glDrawArrays(mode, first, count);
}

public static void
cc_glglue_glDrawElements(cc_glglue glue,
                         /*GLenum*/int mode, /*GLsizei*/int count, /*GLenum*/int type,
                         /*VoidPtr*/IntArrayPtr indices)
{
  //assert(glue.glDrawElements);
  glue.glDrawElements(mode, count, type, indices);
}

public static void cc_glglue_glMultiDrawElements(cc_glglue glue, int mode, IntArrayPtr count, int type,
		VoidPtr[] indices, int primcount) {
	  //assert(glue.glMultiDrawElements);
	  glue.glMultiDrawElements(mode, count, type, indices, primcount);
}

public static void
cc_glglue_glGenBuffers(cc_glglue glue, /*GLsizei*/int n, /*GLu*/int[] buffers)
{
  //assert(glue.glGenBuffers);
  glue.glGenBuffers(n, buffers);
}

public static void cc_glglue_glGenBuffers(cc_glglue glue, int n, Integer[] buffers) {
	  //assert(glue.glGenBuffers);
	int length = buffers.length;
	int[] buffersN = new int[length];
	  glue.glGenBuffers(n, buffersN);
	  for(int i=0; i< length;i++) {
		  buffers[i] = buffersN[i];
	  }
}

public static void cc_glglue_glBindBuffer(cc_glglue glue, int target, int buffer) {
	  //assert(glue.glBindBuffer);
	  glue.glBindBuffer(target, buffer);
}

public static void cc_glglue_glBufferData(cc_glglue glue, int target, long size, VoidPtr data, int usage) {
	  //assert(glue.glBufferData);
	  glue.glBufferData(target, size, data, usage);
}

public static void cc_glglue_glColorPointer(cc_glglue glue, int size, int type, int stride, SbColorArray pointer) {
	  //assert(glue.glColorPointer);
	  glue.glColorPointer(size, type, stride, pointer);
}

public static void cc_glglue_glEnableClientState(cc_glglue glue, int array) {
	  //assert(glue.glEnableClientState);
	  glue.glEnableClientState(array);
}

public static void cc_glglue_glClientActiveTexture(cc_glglue w, int texture) {
//	  if (!w.glClientActiveTexture && texture == GL_TEXTURE0)
//		    return;
//		  assert(w.glClientActiveTexture);
		  w.glClientActiveTexture(texture);
}

public static void cc_glglue_glTexCoordPointer(cc_glglue glue, int size, int type, int stride, FloatBufferAble pointer) {
	  //assert(glue.glTexCoordPointer);
	  glue.glTexCoordPointer(size, type, stride, pointer);
}

public static void cc_glglue_glNormalPointer(cc_glglue glue, int type, int stride, FloatBufferAble pointer) {
	  //assert(glue.glNormalPointer);
	  glue.glNormalPointer(type, stride, pointer);
}

public static void cc_glglue_glVertexPointer(cc_glglue glue, int size, int type, int stride, FloatBufferAble pointer) {
	  //assert(glue.glVertexPointer);
	  glue.glVertexPointer(size, type, stride, pointer);
}

public static void cc_glglue_glDisableClientState(cc_glglue glue, int array) {
	  //assert(glue.glDisableClientState);
	  glue.glDisableClientState(array);
}

static int SOGL_AUTOCACHE_REMOTE_MIN = 500000;
static int SOGL_AUTOCACHE_REMOTE_MAX = 5000000;
static int SOGL_AUTOCACHE_LOCAL_MIN = 100000;
static int SOGL_AUTOCACHE_LOCAL_MAX = 1000000;
static int SOGL_AUTOCACHE_VBO_LIMIT = 65536;

/*!
  Called by each shape during rendering. Will enable/disable autocaching
  based on the number of primitives.
*/
static boolean didtestenv = false;
public static void
sogl_autocache_update(SoState state, int numprimitives, boolean didusevbo)
{
  if (!didtestenv) {
    String env;
    env = TidBits.coin_getenv("COIN_AUTOCACHE_REMOTE_MIN");
    if (env != null) {
      SOGL_AUTOCACHE_REMOTE_MIN = Util.atoi(env);
    }
    env = TidBits.coin_getenv("COIN_AUTOCACHE_REMOTE_MAX");
    if (env != null) {
      SOGL_AUTOCACHE_REMOTE_MAX = Util.atoi(env);
    }
    env = TidBits.coin_getenv("COIN_AUTOCACHE_LOCAL_MIN");
    if (env != null) {
      SOGL_AUTOCACHE_LOCAL_MIN = Util.atoi(env);
    }
    env = TidBits.coin_getenv("COIN_AUTOCACHE_LOCAL_MAX");
    if (env != null) {
      SOGL_AUTOCACHE_LOCAL_MAX = Util.atoi(env);
    }
    env = TidBits.coin_getenv("COIN_AUTOCACHE_VBO_LIMIT");
    if (env != null) {
      SOGL_AUTOCACHE_VBO_LIMIT = Util.atoi(env);
    }
    didtestenv = true;
  }

  int minval = SOGL_AUTOCACHE_LOCAL_MIN;
  int maxval = SOGL_AUTOCACHE_LOCAL_MAX;
  if (SoGLCacheContextElement.getIsRemoteRendering(state)) {
    minval = SOGL_AUTOCACHE_REMOTE_MIN;
    maxval = SOGL_AUTOCACHE_REMOTE_MAX;
  }
  if (numprimitives <= minval) {
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
  }
  else if (numprimitives >= maxval) {
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
  }
  SoGLCacheContextElement.incNumShapes(state);

  if (didusevbo) {
    // avoid creating caches when rendering large VBOs
    if (numprimitives > SOGL_AUTOCACHE_VBO_LIMIT) {
      SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }
  }
}


public static void
sogl_render_faceset( final SoGLCoordinateElement vertexlist,
                    IntArrayPtr vertexindices,
                    int num_vertexindices,
                    final SbVec3fArray normals,
                    IntArrayPtr normindices,
                    final SoMaterialBundle materials,
                    IntArrayPtr matindices,
                    final SoTextureCoordinateBundle texcoords,
                    IntArrayPtr texindices,
                    final SoVertexAttributeBundle attribs,
                    final int nbind,
                    final int mbind,
                    final int attribbind,
                    final int dotexture,
                    final int doattribs)
{
  SOGL_FACESET_GLRENDER(nbind, mbind, attribbind, 
		  											vertexlist,
                                                   vertexindices,
                                                   num_vertexindices,
                                                   normals,
                                                   normindices,
                                                   materials,
                                                   matindices,
                                                   texcoords,
                                                   texindices,
                                                   attribs,
                                                   dotexture,
                                                   doattribs
                                                   );
}

public static void SOGL_FACESET_GLRENDER(final int normalbinding, final int materialbinding, final int vertexattributebinding,
		SoGLCoordinateElement vertexlist,
		IntArrayPtr    vertexindices,
		int    num_vertexindices,
		SbVec3fArray    normals,
		IntArrayPtr    normindices,
		SoMaterialBundle    materials,
		IntArrayPtr    matindices,
		SoTextureCoordinateBundle    texcoords,
		IntArrayPtr    texindices,
		SoVertexAttributeBundle    attribs,
		int    dotexture,
		int    doattribs
		)  {
	SOGL_FACESET_GLRENDER_RESOLVE_ARG1(normalbinding, materialbinding, vertexattributebinding, 
				vertexlist,
                vertexindices,
                num_vertexindices,
                normals,
                normindices,
                materials,
                matindices,
                texcoords,
                texindices,
                attribs,
                dotexture,
                doattribs
			);
}

enum AttributeBinding {
    OVERALL(0),
    PER_FACE(1),
    PER_FACE_INDEXED(2),
    PER_VERTEX(3),
    PER_VERTEX_INDEXED(4);
	
	private int value;
	
	AttributeBinding(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static AttributeBinding fromValue(int value) {
		switch(value) {
		case 0 : return AttributeBinding.OVERALL;
		case 1 : return AttributeBinding.PER_FACE;
		case 2 : return AttributeBinding.PER_FACE_INDEXED;
		case 3 : return AttributeBinding.PER_VERTEX;
		case 4 : return AttributeBinding.PER_VERTEX_INDEXED;
		default : 
			return null;
		}
	}
  };



private static final void SOGL_FACESET_GLRENDER_RESOLVE_ARG1(int normalbinding, int materialbinding, int vertexattributebinding, 
		SoGLCoordinateElement vertexlist,
		IntArrayPtr    vertexindices,
		int    num_vertexindices,
		SbVec3fArray    normals,
		IntArrayPtr    normindices,
		SoMaterialBundle    materials,
		IntArrayPtr    matindices,
		SoTextureCoordinateBundle    texcoords,
		IntArrayPtr    texindices,
		SoVertexAttributeBundle    attribs,
		int    dotexture,
		int    doattribs
		)  {
switch (SoGL.AttributeBinding.fromValue(normalbinding)) { 
case OVERALL: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding.OVERALL, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding.PER_FACE, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE_INDEXED: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding.PER_FACE_INDEXED, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding.PER_VERTEX, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX_INDEXED: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding.PER_VERTEX_INDEXED, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
default: 
  throw new IllegalArgumentException("invalid normal binding argument"); 
}
}

private static final void  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(SoGL.AttributeBinding normalbinding, int materialbinding, int vertexattributebinding, 
		SoGLCoordinateElement vertexlist,
		IntArrayPtr    vertexindices,
		int    num_vertexindices,
		SbVec3fArray    normals,
		IntArrayPtr    normindices,
		SoMaterialBundle    materials,
		IntArrayPtr    matindices,
		SoTextureCoordinateBundle    texcoords,
		IntArrayPtr    texindices,
		SoVertexAttributeBundle    attribs,
		int    dotexture,
		int    doattribs
		)  {
switch (SoGL.AttributeBinding.fromValue(materialbinding)) { 
case OVERALL: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, SoGL.AttributeBinding.OVERALL, vertexattributebinding,
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, SoGL.AttributeBinding.PER_FACE, vertexattributebinding,
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE_INDEXED: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, SoGL.AttributeBinding.PER_FACE_INDEXED, vertexattributebinding,
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, SoGL.AttributeBinding.PER_VERTEX, vertexattributebinding,
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX_INDEXED: 
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, SoGL.AttributeBinding.PER_VERTEX_INDEXED, vertexattributebinding,
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
default: 
  throw new IllegalArgumentException("invalid material binding argument"); 
}
}

private static final void SOGL_FACESET_GLRENDER_RESOLVE_ARG3(SoGL.AttributeBinding normalbinding, SoGL.AttributeBinding materialbinding, int vertexattributebinding,
		SoGLCoordinateElement vertexlist,
		IntArrayPtr    vertexindices,
		int    num_vertexindices,
		SbVec3fArray    normals,
		IntArrayPtr    normindices,
		SoMaterialBundle    materials,
		IntArrayPtr    matindices,
		SoTextureCoordinateBundle    texcoords,
		IntArrayPtr    texindices,
		SoVertexAttributeBundle    attribs,
		int    dotexture,
		int    doattribs
		)  {
switch (SoGL.AttributeBinding.fromValue(vertexattributebinding)) { 
case OVERALL: 
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, SoGL.AttributeBinding.OVERALL, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE: 
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, SoGL.AttributeBinding.OVERALL, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_FACE_INDEXED: 
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, SoGL.AttributeBinding.OVERALL, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX: 
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, SoGL.AttributeBinding.PER_VERTEX, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
case PER_VERTEX_INDEXED: 
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, SoGL.AttributeBinding.PER_VERTEX_INDEXED, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
		  ); 
  break; 
default: 
	throw new IllegalArgumentException("invalid vertex attribute binding argument"); 
}
}

private static void SOGL_FACESET_GLRENDER_CALL_FUNC(SoGL.AttributeBinding normalbinding, SoGL.AttributeBinding materialbinding, SoGL.AttributeBinding vertexattributebinding,
		SoGLCoordinateElement vertexlist,
		IntArrayPtr    vertexindices,
		int    num_vertexindices,
		SbVec3fArray    normals,
		IntArrayPtr    normindices,
		SoMaterialBundle    materials,
		IntArrayPtr    matindices,
		SoTextureCoordinateBundle    texcoords,
		IntArrayPtr    texindices,
		SoVertexAttributeBundle    attribs,
		int    dotexture,
		int    doattribs
		) {
	FaceSet_GLRender(normalbinding, materialbinding, vertexattributebinding, 
			vertexlist,
            vertexindices,
            num_vertexindices,
            normals,
            normindices,
            materials,
            matindices,
            texcoords,
            texindices,
            attribs,
            dotexture,
            doattribs
			);
}

// This is the same code as in SoGLCoordinateElement.send().
// It is inlined here for speed (~15% speed increase).
private static void SEND_VERTEX(int _idx_, boolean is3d, SbVec3fArray coords3d, SbVec4fArray coords4d, GL2 gl2) {
	if (is3d) gl2.glVertex3fv(coords3d.toFloatBuffer(_idx_)/*.getValueRead(),0*/);             
	else gl2.glVertex4fv(coords4d.get(_idx_).getValueRead(),0);
}

// Variable used for counting errors and make sure not a
// bunch of errormessages flood the screen.
private static int current_errors = 0;

//  template < int NormalBinding,
//             int MaterialBinding,
//             int VertexAttributeBinding >
  static void FaceSet_GLRender(
		  SoGL.AttributeBinding NormalBinding, SoGL.AttributeBinding MaterialBinding, SoGL.AttributeBinding VertexAttributeBinding,
		  SoGLCoordinateElement vertexlist,
                     IntArrayPtr vertexindices_,
                     int numindices,
                     SbVec3fArray normals_,
                     IntArrayPtr normalindices_,
                     SoMaterialBundle materials,
                     IntArrayPtr matindices_,
                     SoTextureCoordinateBundle texcoords,
                     IntArrayPtr texindices_,
                       SoVertexAttributeBundle attribs,
                       int dotexture,
                       int doattribs)
  {
	    // just in case someone forgot
	    if (matindices_ == null) matindices_ = vertexindices_;
	    if (normalindices_ == null) normalindices_ = vertexindices_;

	  IntArrayPtr vertexindices = IntArrayPtr.copyOf(vertexindices_);
	  MutableSbVec3fArray normals = normals_ != null ? new MutableSbVec3fArray(normals_) : null;
	  IntArrayPtr normalindices = IntArrayPtr.copyOf(normalindices_);
	  IntArrayPtr matindices = IntArrayPtr.copyOf(matindices_);
	  IntArrayPtr texindices = texindices_ != null ? new IntArrayPtr(texindices_) : null;

    int texidx = 0;

    SbVec3fArray coords3d = null;
    SbVec4fArray coords4d = null;
    boolean is3d = vertexlist.is3D();
    if (is3d) {
      coords3d = vertexlist.getArrayPtr3();
    }
    else {
      coords4d = vertexlist.getArrayPtr4();
    }

    int mode = GL2.GL_POLYGON; // ...to save a test
    int newmode;
    IntArrayPtr viptr = IntArrayPtr.copyOf(vertexindices);
    IntArrayPtr vistartptr = IntArrayPtr.copyOf(vertexindices);
    IntArrayPtr viendptr = IntArrayPtr.plus(viptr,numindices);
    int v1, v2, v3, v4, v5 = 0; // v5 init unnecessary, but kills a compiler warning.
    int numverts = vertexlist.getNum();

    final SbVec3fSingle dummynormal = new SbVec3fSingle(0,0,1);
    MutableSbVec3fArray currnormal = new MutableSbVec3fArray(dummynormal);
    if ((SoGL.AttributeBinding)NormalBinding == SoGL.AttributeBinding.PER_VERTEX ||
       (SoGL.AttributeBinding)NormalBinding == SoGL.AttributeBinding.PER_FACE ||
       (SoGL.AttributeBinding)NormalBinding == SoGL.AttributeBinding.PER_VERTEX_INDEXED ||
       (SoGL.AttributeBinding)NormalBinding == SoGL.AttributeBinding.PER_FACE_INDEXED ||
       dotexture != 0) {
      if (normals != null) currnormal = new MutableSbVec3fArray(normals);
    }

    int matnr = 0;
    int attribnr = 0;

    if (doattribs != 0 && (SoGL.AttributeBinding)VertexAttributeBinding == SoGL.AttributeBinding.OVERALL) {
      attribs.send(0);
    }

    GL2 gl2 = new GL2() {};
    
    while (viendptr != null && viptr.plusLessThan(2, viendptr)) {
      v1 = viptr.get(); viptr.plusPlus();
      v2 = viptr.get(); viptr.plusPlus();
      v3 = viptr.get(); viptr.plusPlus();

      // This test is for robustness upon buggy data sets
      if (v1 < 0 || v2 < 0 || v3 < 0 ||
          v1 >= numverts || v2 >= numverts || v3 >= numverts) {

        if (current_errors < 1) {
          SoDebugError.postWarning("[faceset].GLRender", "Erroneous polygon detected. "+
                                    "Ignoring (offset: "+(viptr.minus(vistartptr) - 3)+", ["+v1+" "+v2+" "+v3+"]). Should be within "+
                                    " [0, "+(numverts - 1)+"] This message will only be shown once, but "+
                                    "more errors might be present"
                                     );
        }
        current_errors++;
        break;
      }
      if(viptr.lessThan(viendptr)) { v4 = viptr.get(); viptr.plusPlus();} else { v4 = -1;};
      if (v4  < 0) newmode = GL2.GL_TRIANGLES;
      // This test for numverts is for robustness upon buggy data sets
      else if (v4 >= numverts) {
        newmode = GL2.GL_TRIANGLES;

        if (current_errors < 1) {
          SoDebugError.postWarning("[faceset].GLRender", "Erroneous polygon detected. "+
                                    "(offset: "+(viptr.minus(vistartptr) - 4)+", ["+v1+" "+v2+" "+v3+" "+v4+"]). Should be within "+
                                    " [0, "+(numverts - 1)+"] This message will only be shown once, but "+
                                    "more errors might be present"
                                    );
        }
        current_errors++;
      }
      else {
        if(viptr.lessThan(viendptr)) { v5 = viptr.get(); viptr.plusPlus(); } else {v5 = -1;}
        if (v5 < 0) newmode = GL2.GL_QUADS;
        // This test for numverts is for robustness upon buggy data sets
        else if (v5 >= numverts) {
          newmode = GL2.GL_QUADS;

          if (current_errors < 1) {
            SoDebugError.postWarning("[faceset].GLRender", "Erroneous polygon detected. "+
                                      "(offset: "+(viptr.minus(vistartptr) - 5)+", ["+v1+" "+v2+" "+v3+" "+v4+" "+v5+"]). Should be within "+
                                      " [0, "+(numverts - 1)+"] This message will only be shown once, but "+
                                      "more errors might be present"
                                       );
          }
          current_errors++;
        }
        else newmode = GL2.GL_POLYGON;
      }
      
      if (newmode != mode) {
        if (mode != GL2.GL_POLYGON) gl2.glEnd();
        mode = newmode;
        gl2.glBegin( mode);
      }
      else if (mode == GL2.GL_POLYGON) gl2.glBegin(GL2.GL_POLYGON);

      /* vertex 1 *********************************************************/
      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX ||
          (AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
        materials.send(matnr++, true);
      } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED ||
                 (AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
        materials.send(matindices.get(), true);matindices.plusPlus();
      }

      if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX ||
          (AttributeBinding)NormalBinding == AttributeBinding.PER_FACE) {
        currnormal.assign(normals); normals.plusPlus();
        gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
      } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED ||
                 (AttributeBinding)NormalBinding == AttributeBinding.PER_FACE_INDEXED) {
        currnormal.assign(normals,normalindices.get()); normalindices.plusPlus();
        gl2.glNormal3fv(currnormal.toFloatBuffer()/*get(0).getValueRead(),0*/);
      }

      if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
       attribs.send(attribnr++);
      } else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        attribs.send(vertexindices.get()); vertexindices.plusPlus();
      }

      if (dotexture != 0) {
    	  int dummy;
    	  if(texindices != null) {
    		  dummy = texindices.get();
    		  texindices.plusPlus();
    	  }
    	  else {
    		  dummy = texidx;
    		  texidx++;
    	  }
        texcoords.send(/*texindices != null ? *texindices++ : texidx++*/dummy,
                        vertexlist.get3(v1),
                        currnormal.get(0));
      }

      SEND_VERTEX(v1,is3d,coords3d,coords4d,gl2);

      /* vertex 2 *********************************************************/
      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
        materials.send(matnr++, true);
      } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        materials.send(matindices.get(), true);matindices.plusPlus();
      }

      // nvidia color-per-face-bug workaround
      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
        materials.send(matnr-1, true);
      } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
        materials.send(matindices.get(-1), true);
      }

      if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
        currnormal.assign(normals);normals.plusPlus();
        gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
      } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        currnormal.assign(normals,normalindices.get());normalindices.plusPlus();
        gl2.glNormal3fv(currnormal.toFloatBuffer()/*.get(0).getValueRead(),0*/);
      }

      if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
       attribs.send(attribnr++);
      } else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        attribs.send(vertexindices.get());vertexindices.plusPlus();
      }

      if (dotexture != 0) {
    	  int dummy;
    	  if(texindices != null) {
    		  dummy = texindices.get();
    		  texindices.plusPlus();
    	  }
    	  else {
    		  dummy = texidx;
    		  texidx++;
    	  }
        texcoords.send(/*texindices ? *texindices++ : texidx++*/dummy,
                        vertexlist.get3(v2),
                        currnormal.get(0));
      }

      SEND_VERTEX(v2,is3d,coords3d,coords4d,gl2);

      /* vertex 3 *********************************************************/
      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
        materials.send(matnr++, true);
      } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        materials.send(matindices.get(), true); matindices.plusPlus();
      }

      // nvidia color-per-face-bug workaround
      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
        materials.send(matnr-1, true);
      } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
        materials.send(matindices.get(-1), true);
      }

      if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
        currnormal.assign(normals);normals.plusPlus();
        gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
      } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        currnormal.assign(normals,normalindices.get());normalindices.plusPlus();
        gl2.glNormal3fv(currnormal.toFloatBuffer()/*get(0).getValueRead(),0*/);
      }

      if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
       attribs.send(attribnr++);
      } else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        attribs.send(vertexindices.get());vertexindices.plusPlus();
      }

      if (dotexture != 0) {
    	  int dummy;
    	  if(texindices != null) {
    		  dummy = texindices.get();
    		  texindices.plusPlus();
    	  }
    	  else {
    		  dummy = texidx;
    		  texidx++;
    	  }
        texcoords.send(/*texindices ? *texindices++ : texidx++*/dummy,
                        vertexlist.get3(v3),
                        currnormal.get(0));
      }

      SEND_VERTEX(v3,is3d,coords3d,coords4d,gl2);

      if (mode != GL2.GL_TRIANGLES) {
        /* vertex 4 (quad or polygon)**************************************/
        if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
          materials.send(matnr++, true);
        } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
          materials.send(matindices.get(), true); matindices.plusPlus();
        }

        // nvidia color-per-face-bug workaround
        if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
          materials.send(matnr-1, true);
        } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
          materials.send(matindices.get(-1), true);
        }

        if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
          currnormal.assign(normals);normals.plusPlus();
          gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
        } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
          currnormal.assign(normals,normalindices.get());normalindices.plusPlus();
          gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
        }

        if (dotexture != 0) {
        	int dummy;
        	if(texindices != null) {
        		dummy = texindices.get();
        		texindices.plusPlus();
        	}
        	else {
        		dummy = texidx;
        		texidx++;
        	}
          texcoords.send(/*texindices ? *texindices++ : texidx++*/dummy,
                          vertexlist.get3(v4),
                          currnormal.get(0));
        }

        if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
          attribs.send(attribnr++);
        } 
        else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
          attribs.send(vertexindices.get()); vertexindices.plusPlus();
        }
        SEND_VERTEX(v4,is3d,coords3d,coords4d,gl2);

        if (mode == GL2.GL_POLYGON) {
          /* vertex 5 (polygon) ********************************************/
          if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
            materials.send(matnr++, true);
          } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
            materials.send(matindices.get(), true); matindices.plusPlus();
          }

          // nvidia color-per-face-bug workaround
          if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
            materials.send(matnr-1, true);
          } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
            materials.send(matindices.get(-1), true);
          }

          if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
            currnormal.assign(normals);normals.plusPlus();
            gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
          } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
            currnormal.assign(normals,normalindices.get());normalindices.plusPlus();
            gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
          }

          if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
            attribs.send(attribnr++);
          } else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
            attribs.send(vertexindices.get());vertexindices.plusPlus();
          }

          if (dotexture != 0) {
        	  int dummy;
        	  if(texindices != null) {
        		  dummy = texindices.get();
        		  texindices.plusPlus();
        	  }
        	  else {
        		  dummy = texidx;
        		  texidx++;
        	  }
            texcoords.send(/*texindices ? *texindices++ : texidx++*/dummy,
                            vertexlist.get3(v5),
                            currnormal.get(0));

          }

          SEND_VERTEX(v5,is3d,coords3d,coords4d,gl2);

          if(viptr.lessThan(viendptr)) { v1 = viptr.get(); viptr.plusPlus();}else { v1 = -1;}
          while (v1 >= 0) {
            // For robustness upon buggy data sets
            if (v1 >= numverts) {
              if (current_errors < 1) {
                SoDebugError.postWarning("[faceset].GLRender", "Erroneous polygon detected. "+
                                          "(offset: "+(viptr.minus(vistartptr) - 1)+", [... "+v1+"]). Should be within "+
                                          "[0, "+(numverts - 1)+"] This message will only be shown once, but "+
                                          "more errors might be present"
                                           );
              }
              current_errors++;
              break;
            }

            /* vertex 6-n (polygon) *****************************************/
            if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
              materials.send(matnr++, true);
            } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
              materials.send(matindices.get(), true);matindices.plusPlus();
            }

            // nvidia color-per-face-bug workaround
            if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE) {
              materials.send(matnr-1, true);
            } else if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_FACE_INDEXED) {
              materials.send(matindices.get(-1), true);
            }

            if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
              currnormal.assign(normals);normals.plusPlus();
              gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
            } else if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
              currnormal.assign(normals,normalindices.get());normalindices.plusPlus();
              gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
            }

            if (dotexture != 0) {
            	int dummy;
            	if(texindices != null) {
            		dummy = texindices.get();
            		texindices.plusPlus();
            	}
            	else {
            		dummy = texidx;
            		texidx++;
            	}
              texcoords.send(/*texindices ? *texindices++ : texidx++*/dummy,
                              vertexlist.get3(v1),
                              currnormal.get(0));
            }

            if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX) {
              attribs.send(attribnr++);
            } else if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
              attribs.send(vertexindices.get());vertexindices.plusPlus();
            }
            SEND_VERTEX(v1,is3d,coords3d,coords4d,gl2);

            if(viptr.lessThan(viendptr)) { v1 = viptr.get(); viptr.plusPlus();}else {v1 = -1;}
          }
          gl2.glEnd(); /* draw polygon */
        }
      }

      if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        matindices.plusPlus();
      }
      if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        normalindices.plusPlus();
      }
      if ((AttributeBinding)VertexAttributeBinding == AttributeBinding.PER_VERTEX_INDEXED) {
        vertexindices.plusPlus();
      }

      if (dotexture != 0) {
        if (texindices != null) texindices.plusPlus();
      }
    }
    // check if triangle or quad
    if (mode != GL2.GL_POLYGON) gl2.glEnd();
  }

public static boolean cc_glglue_has_blendfuncseparate(cc_glglue glue) {
	return true;
}

public static void cc_glglue_glBlendFuncSeparate(cc_glglue glue, int rgbsrc, int rgbdst, int alphasrc,
		int alphadst) {
	  glue.glBlendFuncSeparate(rgbsrc, rgbdst, alphasrc, alphadst);
}

//
// the 12 triangles in the cube
//
static int sogl_cube_vindices[] =
{
  0, 1, 3, 2,
  5, 4, 6, 7,
  1, 5, 7, 3,
  4, 0, 2, 6,
  4, 5, 1, 0,
  2, 3, 7, 6
};

static float sogl_cube_texcoords[] =
{
  1.0f, 1.0f,
  0.0f, 1.0f,
  0.0f, 0.0f,
  1.0f, 0.0f
};

static float sogl_cube_3dtexcoords[][] =
{
  {1.0f, 1.0f, 1.0f},
  {1.0f, 1.0f, 0.0f},
  {1.0f, 0.0f, 1.0f},
  {1.0f, 0.0f, 0.0f},
  {0.0f, 1.0f, 1.0f},
  {0.0f, 1.0f, 0.0f},
  {0.0f, 0.0f, 1.0f},
  {0.0f, 0.0f, 0.0f}
};

static float sogl_cube_normals[] =
{
  0.0f, 0.0f, 1.0f,
  0.0f, 0.0f, -1.0f,
  -1.0f, 0.0f, 0.0f,
  1.0f, 0.0f, 0.0f,
  0.0f, 1.0f, 0.0f,
  0.0f, -1.0f, 0.0f
};


public static void
sogl_generate_cube_vertices(SbVec3fArray varray,
                       float w,
                       float h,
                       float d)
{
  for (int i = 0; i < 8; i++) {
    varray.get(i).setValue((i&1)!=0 ? -w : w,
                       (i&2)!=0 ? -h : h,
                       (i&4)!=0 ? -d : d);
  }
}



public static void
sogl_render_cube( float width,
                  float height,
                  float depth,
                 SoMaterialBundle material,
                 int flagsin,
                 SoState state)
{
  boolean[] unitenabled = null;
  final int[] maxunit = new int[1];
  cc_glglue glue = null;

  int flags = flagsin;

  if (state != null) {
    unitenabled =
      SoMultiTextureEnabledElement.getEnabledUnits(state, maxunit);
    if (unitenabled != null) {
      glue = sogl_glue_instance(state);
      flags |= SOGL_NEED_MULTITEXCOORDS;
    }
    else maxunit[0] = -1;
  }

  GL2 gl2 = state.getGL2();


  SbVec3fArray varray = new SbVec3fArray(FloatMemoryBuffer.allocateFromFloatArray(new float[8*3]));
  sogl_generate_cube_vertices(varray,
                         width * 0.5f,
                         height * 0.5f,
                         depth * 0.5f);
  gl2.glBegin(GL2.GL_QUADS);
  IntArrayPtr iptr = new IntArrayPtr(sogl_cube_vindices);
  int u;
  
  SbVec3fArray cn = new SbVec3fArray(FloatMemoryBuffer.allocateFromFloatArray(sogl_cube_normals));
  SbVec2fArray ct = new SbVec2fArray(FloatMemoryBuffer.allocateFromFloatArray(sogl_cube_texcoords));

  for (int i = 0; i < 6; i++) { // 6 quads
    if ((flags & SOGL_NEED_NORMALS)!=0)
      gl2.glNormal3fv(/*sogl_cube_normals[i*3]*/cn.get(i).getValueRead());
    if ((flags & SOGL_MATERIAL_PER_PART)!=0)
      material.send(i, true);
    for (int j = 0; j < 4; j++) {
      if ((flags & SOGL_NEED_3DTEXCOORDS)!=0) {
    	  gl2.glTexCoord3fv(sogl_cube_3dtexcoords[iptr.get()]);
      }
      else if ((flags & SOGL_NEED_TEXCOORDS)!=0) {
    	  gl2.glTexCoord2fv(/*&sogl_cube_texcoords[j<<1]*/ct.get(j).getValueRead());
      }
      if ((flags & SOGL_NEED_MULTITEXCOORDS)!=0) {
        for (u = 1; u <= maxunit[0]; u++) {
          if (unitenabled[u]) {
            SoGL.cc_glglue_glMultiTexCoord2fv(glue, (int) (GL2.GL_TEXTURE0 + u),
                                         /*&sogl_cube_texcoords[j<<1]*/ct.get(j).getValueRead());
          }
        }
      }
      gl2.glVertex3fv(varray.get(iptr.get()).getValueRead(),0); iptr.plusPlus();
    }
  }
  gl2.glEnd();

  if (state != null) {
    // always encourage auto caching for cubes
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    SoGLCacheContextElement.incNumShapes(state);
  }
}

public static void
sogl_render_cylinder( float radius,
                      float height,
                      int numslices,
                     SoMaterialBundle material,
                     int flagsin,
                     SoState state)
{
  boolean[] unitenabled = null;
  final int[] maxunit = new int[1];
  cc_glglue glue = null;

  int flags = flagsin;

  if (state != null) {
    unitenabled =
      SoMultiTextureEnabledElement.getEnabledUnits(state, maxunit);
    if (unitenabled != null) {
      glue = sogl_glue_instance(state);
      flags |= SOGL_NEED_MULTITEXCOORDS;
    }
    else maxunit[0] = -1;
  }

  int i, u;
  int slices = numslices;
  if (slices > 128) slices = 128;
  if (slices < 4) slices = 4;

  float h2 = height * 0.5f;

  final SbVec3fArray coords = new SbVec3fArray(FloatMemoryBuffer.allocateFromFloatArray(new float[129*3]));
  final SbVec3fArray normals = new SbVec3fArray(FloatMemoryBuffer.allocateFromFloatArray(new float[130*3]));
  final SbVec2fArray texcoords = new SbVec2fArray(FloatMemoryBuffer.allocateFromFloatArray(new float[129*2]));

  sogl_generate_3d_circle(coords, slices, radius, -h2);
  coords.get(slices).copyFrom( coords.get(0));
  if ((flags & SOGL_NEED_3DTEXCOORDS)!=0 ||
      ((flags & SOGL_NEED_TEXCOORDS)!=0 &&
       (flags & (SOGL_RENDER_BOTTOM | SOGL_RENDER_TOP))!=0)) {
    sogl_generate_2d_circle(texcoords, slices, 0.5f);
    texcoords.get(slices).copyFrom(texcoords.get(0));
  }

  if ((flags & SOGL_NEED_NORMALS)!=0) {
    sogl_generate_3d_circle(normals, slices, 1.0f, 0.0f);
    normals.get(slices).copyFrom( normals.get(0));
    normals.get(slices+1).copyFrom( normals.get(1));
  }

  int matnr = 0;
  
  GL2 gl2;
  if(state != null) {
	  gl2 = state.getGL2();
  }
  else {
	  gl2 = new GL2() {};
  }

  if ((flags & SOGL_RENDER_SIDE)!=0) {
    gl2.glBegin(GL2.GL_QUAD_STRIP);
    i = 0;

    float t = 0.0f;
    float inc = 1.0f / slices;

    while (i <= slices) {
      if ((flags & SOGL_NEED_TEXCOORDS)!=0) {
    	  gl2.glTexCoord2f(t, 1.0f);
      }
      else if ((flags & SOGL_NEED_3DTEXCOORDS)!=0) {
    	  gl2.glTexCoord3f(texcoords.get(i).getValueRead()[0]+0.5f, 1.0f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
      }
      if ((flags & SOGL_NEED_NORMALS)!=0) {
    	  gl2.glNormal3fv(normals.get(i).getValueRead());
      }
      if ((flags & SOGL_NEED_MULTITEXCOORDS)!=0) {
        for (u = 1; u <= maxunit[0]; u++) {
          if (unitenabled[u]) {
            SoGL.cc_glglue_glMultiTexCoord2f(glue, (int) (GL2.GL_TEXTURE0 + u),
                                        t, 1.0f);
          }
        }
      }

      SbVec3f c = coords.get(i);
      gl2.glVertex3f(c.getValueRead()[0], h2, c.getValueRead()[2]);
      if ((flags & SOGL_NEED_TEXCOORDS)!=0) {
    	  gl2.glTexCoord2f(t, 0.0f);
      }
      else if ((flags & SOGL_NEED_3DTEXCOORDS)!=0) {
    	  gl2.glTexCoord3f(texcoords.get(i).getValueRead()[0]+0.5f, 0.0f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
      }
      if ((flags & SOGL_NEED_MULTITEXCOORDS)!=0) {
        for (u = 1; u <= maxunit[0]; u++) {
          if (unitenabled[u]) {
            SoGL.cc_glglue_glMultiTexCoord2f(glue, (int) (GL2.GL_TEXTURE0 + u),
                                        t, 0.0f);
          }
        }
      }
      gl2.glVertex3f(c.getValueRead()[0], c.getValueRead()[1], c.getValueRead()[2]);
      i++;
      t += inc;
    }

    matnr++;
    gl2.glEnd();
  }

  if ((flags & (SOGL_NEED_TEXCOORDS|SOGL_NEED_3DTEXCOORDS|SOGL_NEED_MULTITEXCOORDS))!=0 &&
      (flags & (SOGL_RENDER_BOTTOM | SOGL_RENDER_TOP))!=0) {
    sogl_generate_2d_circle(texcoords, slices, 0.5f);
    texcoords.get(slices).copyFrom(texcoords.get(0));
  }

  if ((flags & SOGL_RENDER_TOP)!=0) {
    if ((flags & SOGL_MATERIAL_PER_PART)!=0) {
      material.send(matnr, true);
    }
    gl2.glBegin(GL2.GL_TRIANGLE_FAN);
    gl2.glNormal3f(0.0f, 1.0f, 0.0f);

    for (i = 0; i < slices; i++) {
      if ((flags & SOGL_NEED_TEXCOORDS)!=0) {
    	  gl2.glTexCoord2f(texcoords.get(i).getValueRead()[0]+0.5f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
      }
      else if ((flags & SOGL_NEED_3DTEXCOORDS)!=0) {
    	  gl2.glTexCoord3f(texcoords.get(i).getValueRead()[0]+0.5f, 1.0f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
      }
      if ((flags & SOGL_NEED_MULTITEXCOORDS)!=0) {
        for (u = 1; u <= maxunit[0]; u++) {
          if (unitenabled[u]) {
            SoGL.cc_glglue_glMultiTexCoord2f(glue, (int) (GL2.GL_TEXTURE0 + u),
                                       texcoords.get(i).getValueRead()[0]+0.5f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
          }
        }
      }
      SbVec3f c = coords.get(i);
      gl2.glVertex3f(c.getValueRead()[0], h2, c.getValueRead()[2]);
    }
    gl2.glEnd();
    matnr++;
  }
  if ((flags & SOGL_RENDER_BOTTOM)!=0) {
    if ((flags & SOGL_MATERIAL_PER_PART)!=0) {
      material.send(matnr, true);
    }
    gl2.glBegin(GL2.GL_TRIANGLE_FAN);
    gl2.glNormal3f(0.0f, -1.0f, 0.0f);

    for (i = slices-1; i >= 0; i--) {
      if ((flags & SOGL_NEED_TEXCOORDS)!=0) {
    	  gl2.glTexCoord2f(texcoords.get(i).getValueRead()[0]+0.5f, texcoords.get(i).getValueRead()[1]+0.5f);
      }
      else if ((flags & SOGL_NEED_3DTEXCOORDS)!=0) {
    	  gl2.glTexCoord3f(texcoords.get(i).getValueRead()[0]+0.5f, 0.0f, 1.0f - texcoords.get(i).getValueRead()[1]-0.5f);
      }
      if ((flags & SOGL_NEED_MULTITEXCOORDS)!=0) {
        for (u = 1; u <= maxunit[0]; u++) {
          if (unitenabled[u]) {
            SoGL.cc_glglue_glMultiTexCoord2f(glue, (int) (GL2.GL_TEXTURE0 + u),
                                        texcoords.get(i).getValueRead()[0]+0.5f, texcoords.get(i).getValueRead()[1]+0.5f);
          }
        }
      }
      gl2.glVertex3fv(coords.get(i).getValueRead());
    }
    gl2.glEnd();
  }
  if (state != null && (SoComplexityTypeElement.get(state) ==
                SoComplexityTypeElement.Type.OBJECT_SPACE)) {
    // encourage auto caching for object space
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    SoGLCacheContextElement.incNumShapes(state);
  }
  else {
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
  }
}


// generate a 3d circle in the x-z plane
public static void
sogl_generate_3d_circle(SbVec3fArray coords, int num, float radius, float y)
{
  float delta = 2.0f*(float)(Math.PI)/(float)(num);
  float angle = 0.0f;
  for (int i = 0; i < num; i++) {
    coords.get(i).setX( -(float)(Math.sin(angle)) * radius);
    coords.get(i).setY( y);
    coords.get(i).setZ( -(float)(Math.cos(angle)) * radius);
    angle += delta;
  }
}

// generate a 2d circle
public static void
sogl_generate_2d_circle(SbVec2fArray coords, int num, float radius)
{
  float delta = 2.0f*(float)(Math.PI)/(float)(num);
  float angle = 0.0f;
  for (int i = 0; i < num; i++) {
    coords.get(i).setX( -(float)(Math.sin(angle)) * radius);
    coords.get(i).setY( -(float)(Math.cos(angle)) * radius);
    angle += delta;
  }
}

public static void
cc_glglue_glMultiTexCoord2f( cc_glglue w,
                            int target,
                            float s,
                            float t)
{
  //assert(w->glMultiTexCoord2f);
  w.glMultiTexCoord2f(target, s, t);
}

public static void cc_glglue_glDeleteBuffers(cc_glglue glue, int n, int[] buffers) {
	  //assert(glue.glDeleteBuffers);
	  glue.glDeleteBuffers(n, buffers);
}

public static boolean cc_glglue_isdirect(cc_glglue w) {
	  return true; // w.glx.isdirect; YB
}


}
