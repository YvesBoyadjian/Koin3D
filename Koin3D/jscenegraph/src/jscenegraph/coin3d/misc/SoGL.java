package jscenegraph.coin3d.misc;

import java.nio.ByteBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.errors.DebugError;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoBasic;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;
import jscenegraph.port.Util;

public class SoGL {

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
//  assert(action->isOfType(SoGLRenderAction::getClassTypeId()) &&
//         "must have state from SoGLRenderAction to get hold of GL wrapper");
//  return cc_glglue_instance(action->getCacheContext());
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

// java port
public static cc_glglue cc_glglue_instance(int cacheContext) {
	return new cc_glglue(cacheContext);
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
  //assert(w->glActiveTexture);
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
               format, type, null);
  gl2.glGetTexLevelParameteriv(GL2.GL_PROXY_TEXTURE_2D, 0,
                           GL2.GL_TEXTURE_WIDTH, w);

  if (w[0] == 0) return false;
  if (!mipmap) return true;

  for (level = 1; level <= levels; level++) {
    if (width > 1) width >>= 1;
    if (height > 1) height >>= 1;
    gl2.glTexImage2D(GL2.GL_PROXY_TEXTURE_2D, level, internalFormat, width,
                 height, 0, format, type,
                 null);
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

  void * result = NULL;
  if (cc_dict_get(wrapper->glextdict, key, &result)) {
    return result != NULL;
  }
  result = coin_glglue_extension_available(wrapper->extensionsstr, extension) ?
    (void*) 1 : NULL;
  cc_dict_put(wrapper->glextdict, key, result);

  return result != NULL;*/
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
  //assert(w->glTexImage3D);
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

public static void cc_glglue_glGenFramebuffers(cc_glglue glue, int n, int framebuffer) {
	  assert(glue.has_fbo);
	  assert(n==1);
	  int[] framebuffers = new int[1];
	  framebuffers[0] = framebuffer;
	  glue.glGenFramebuffers(n, framebuffers);
}

public static void cc_glglue_glGenRenderbuffers(cc_glglue glue, int n, int renderbuffer) {
	  assert(glue.has_fbo);
	  assert(n==1);
	  int[] renderbuffers = new int[1];
	  renderbuffers[0] = renderbuffer;
	  glue.glGenRenderbuffers(n, renderbuffers);
}

public static Object cc_glglue_context_create_offscreen(short width, short height) {
	  if (offscreen_cb != null && offscreen_cb.create_offscreen != null) {
		    return (offscreen_cb.create_offscreen).apply(width, height);
		  } else {
//		#ifdef HAVE_NOGL
//		  assert(FALSE && "unimplemented");
//		  return NULL;
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



}
