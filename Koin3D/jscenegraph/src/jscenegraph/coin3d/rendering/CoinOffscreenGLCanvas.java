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

package jscenegraph.coin3d.rendering;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.glue.Gl;
import jscenegraph.coin3d.glue.Gl_wgl;
import jscenegraph.coin3d.inventor.misc.SoContextHandler;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class CoinOffscreenGLCanvas {
	  
	  private static int tilesizeroof = Integer.MAX_VALUE; // java port
	  
	  private final SbVec2s size = new SbVec2s();

	  private Object context;
	  private int renderid;
	  private Object current_hdc;
	  
	// *************************************************************************

	  //unsigned int CoinOffscreenGLCanvas::tilesizeroof = UINT_MAX;

	  // *************************************************************************

	  public CoinOffscreenGLCanvas()
	  {
	    this.size.copyFrom( new SbVec2s((short)0, (short)0));
	    this.context = null;
	    this.current_hdc = null;
	  }

	  public void destructor()
	  {
	    if (this.context != null) { this.destructContext(); }
	  }

	  // *************************************************************************

	  public static boolean
	  clampSize(final SbVec2s reqsize)
	  {
	    // getMaxTileSize() returns the theoretical maximum gathered from
	    // various GL driver information. We're not guaranteed that we'll be
	    // able to allocate a buffer of this size -- e.g. due to memory
	    // constraints on the gfx card.

	    final SbVec2s maxsize = new SbVec2s(CoinOffscreenGLCanvas.getMaxTileSize());
	    if (maxsize.operator_equal_equal(new SbVec2s((short)0, (short)0))) { return false; }

	    reqsize.getValue()[0] = (short)Math.min(reqsize.getValue()[0], maxsize.getValue()[0]);
	    reqsize.getValue()[1] = (short)Math.min(reqsize.getValue()[1], maxsize.getValue()[1]);

	    // Fit the attempted allocation size to be less than the largest
	    // tile size we know have failed allocation. We do this to avoid
	    // trying to set the tilesize to dimensions which will very likely
	    // fail -- as attempting to find a workable tilesize is an expensive
	    // operation when the SoOffscreenRenderer instance already has a GL
	    // context set up (destruction and creation of a new one will take
	    // time, and it will also kill all GL resources tied to the
	    // context).
	    while (((( int)reqsize.getValue()[0]) * (( int)reqsize.getValue()[1])) >
	           CoinOffscreenGLCanvas.tilesizeroof) {
	      // shrink by halving the largest dimension:
	      if (reqsize.getValue()[0] > reqsize.getValue()[1]) { reqsize.getValue()[0] /= 2; }
	      else { reqsize.getValue()[1] /= 2; }
	    }

	    if ((reqsize.getValue()[0] == 0) || (reqsize.getValue()[1] == 0)) { return false; }
	    return true;
	  }

	  public void
	  setWantedSize(SbVec2s reqsize)
	  {
	    assert((reqsize.getValue()[0] > 0) && (reqsize.getValue()[1] > 0) && "invalid dimensions attempted set" != null);

	    boolean ok = CoinOffscreenGLCanvas.clampSize(reqsize);
	    if (!ok) {
	      if (this.context != null) { this.destructContext(); }
	      this.size.copyFrom( new SbVec2s((short)0, (short)0));
	      return;
	    }

	    // We check if the current GL canvas is much larger than what is
	    // requested, as to then free up potentially large memory resources,
	    // even if we already have a large enough canvas.
	    int oldres = (int)this.size.getValue()[0] * (int)this.size.getValue()[1];
	    int newres = (int)reqsize.getValue()[0] * (int)reqsize.getValue()[1];
	    boolean resourcehog = (oldres > (newres * 16)) && !CoinOffscreenGLCanvas.allowResourcehog();

	    // Since the operation of context destruction and reconstruction has
	    // the potential to be such a costly operation (because GL caches
	    // are smashed, among other things), we try hard to avoid it.
	    //
	    // So avoid it if not really necessary, by checking that if we
	    // already have a working GL context with size equal or larger to
	    // the requested, don't destruct (and reconstruct).
	    //
	    // We can have a different sized internal GL canvas as to what
	    // SoOffscreenRenderer wants, because glViewport() is used from
	    // SoOffscreenRenderer to render to the correct viewport dimensions.
	    if (this.context != null &&
	        (this.size.getValue()[0] >= reqsize.getValue()[0]) &&
	        (this.size.getValue()[1] >= reqsize.getValue()[1]) &&
	        !resourcehog) {
	      return;
	    }

	    // Ok, there's no way around it, we need to destruct the GL context:

	    if (CoinOffscreenGLCanvas.debug()) {
	      SoDebugError.postInfo("CoinOffscreenGLCanvas::setWantedSize",
	                             "killing current context, (clamped) reqsize==["+reqsize.getValue()[0]+", "+reqsize.getValue()[1]+"],"+
	                             " previous size==["+this.size.getValue()[0]+", "+this.size.getValue()[1]+"], resourcehog=="+
	                             (resourcehog ? "TRUE" : "FALSE"));
	    }

	    if (resourcehog) {
	      // If we were hogging too much memory for the offscreen context,
	      // simply go back to the requested size, to free up all that we
	      // can.
	      this.size.copyFrom( reqsize);
	    }
	    else {
	      // To avoid costly reconstruction on "flutter", by one or two
	      // dimensions going a little bit up and down from frame to frame,
	      // we try to expand the GL canvas up-front to what perhaps would
	      // be sufficient to avoid further GL canvas destruct- /
	      // reconstruct-operations.
	      this.size.getValue()[0] = (short)Math.max(reqsize.getValue()[0], this.size.getValue()[0]);
	      this.size.getValue()[1] = (short)Math.max(reqsize.getValue()[1], this.size.getValue()[1]);
	    }

	    if (this.context != null) { this.destructContext(); }
	  }

	  public SbVec2s 
	  getActualSize()
	  {
	    return this.size;
	  }

	  // *************************************************************************

	  public int
	  tryActivateGLContext()
	  {
	    if (this.size.operator_equal_equal(new SbVec2s((short)0, (short)0))) { return 0; }

	    if (this.context == null) {
	  //#if defined(HAVE_WGL)
	      /* NOTE: This discrepancy between the different glue flavors is due to a
	      driver bug that causes the coordinates fed to the gl_FragCoord fragment
	      shader input register to be flipped when using render-to-texture capable
	      pbuffers. Ref COINSUPPORT-1284. 20101214 tarjei. */
	      this.context = Gl_wgl.wglglue_context_create_offscreen(this.size.getValue()[0],
	                                                       this.size.getValue()[1],
	                                                       false);
	  //#else
//	      this.context = cc_glglue_context_create_offscreen(this.size.getValue()[0],
//	                                                         this.size.getValue()[1]);
	  //#endif
	      if (CoinOffscreenGLCanvas.debug()) {
	        SoDebugError.postInfo("CoinOffscreenGLCanvas::tryActivateGLContext",
	                               "Tried to create offscreen context of dimensions "+
	                               "<"+this.size.getValue()[0]+", "+this.size.getValue()[1]+"> -- "+(this.context == null ? "failed" : "succeeded")
	                               );
	      }

	      if (this.context == null) { return 0; }

	      // Set up mapping from GL context to SoGLRenderAction context id.
	      this.renderid = SoGLCacheContextElement.getUniqueCacheContext();

	      // need to change this, for the getHDC() function, since a
	      // reference to current_hdc is returned (yes, this is dumb, but
	      // such is the TGS / Mercury Inventor API)
	      this.current_hdc = Gl.cc_glglue_win32_HDC(this.context);
	    }

	    if (SoGL.cc_glglue_context_make_current(this.context) == false) {
	      if (CoinOffscreenGLCanvas.debug()) {
	        SoDebugError.post("CoinOffscreenGLCanvas::tryActivateGLContext",
	                           "Couldn't make context current.");
	      }
	      return 0;
	    }
	    return this.renderid;
	  }

	  public static void
	  clampToPixelSizeRoof(SbVec2s s)
	  {
	    int pixelsize;
	    do {
	      pixelsize = (int)s.getValue()[0] * (int)s.getValue()[1];
	      if (pixelsize == 0) { return; } // avoid never-ending loop

	      if (pixelsize >= CoinOffscreenGLCanvas.tilesizeroof) {
	        // halve the largest dimension, and try again:
	        if (s.getValue()[0] > s.getValue()[1]) { s.getValue()[0] /= 2; }
	        else { s.getValue()[1] /= 2; }
	      }
	    } while (pixelsize >= CoinOffscreenGLCanvas.tilesizeroof);
	  }

	  // Activates an offscreen GL context, and returns a guaranteed unique
	  // id to use with SoGLRenderAction::setCacheContext().
	  //
	  // If the given context can not be made current (due to e.g. any error
	  // condition resulting from the attempt at setting up the offscreen GL
	  // context), 0 is returned.
	  public int
	  activateGLContext()
	  {
	    // We try to allocate the wanted size, and then if we fail,
	    // successively try with smaller sizes (alternating between halving
	    // width and height) until either a workable offscreen buffer was
	    // found, or no buffer could be made.
	    int ctx;
	    do {
	      CoinOffscreenGLCanvas.clampToPixelSizeRoof(this.size);

	      ctx = this.tryActivateGLContext();
	      if (ctx != 0) { break; }

	      // if we've allocated a context, but couldn't make it current
	      if (this.context != null) { this.destructContext(); }

	      // we failed with this size, so make sure we only try with smaller
	      // tile sizes later
	      int failedsize =
	        (int)this.size.getValue()[0] * (int)this.size.getValue()[1];
	      assert(failedsize <= CoinOffscreenGLCanvas.tilesizeroof);
	      CoinOffscreenGLCanvas.tilesizeroof = failedsize;

	      // keep trying until 32x32 -- if even those dimensions doesn't
	      // work, give up, as too small tiles will cause the processing
	      // time to go through the roof due to the huge number of passes:
	      if ((this.size.getValue()[0] <= 32) && (this.size.getValue()[1] <= 32)) { break; }
	    } while (true);

	    return ctx;
	  }

	  public void
	  deactivateGLContext()
	  {
	    assert(this.context != null);
	    SoGL.cc_glglue_context_reinstate_previous(this.context);
	  }

	  // *************************************************************************

	  public void
	  destructContext()
	  {
	    assert(this.context != null);

	    if (SoGL.cc_glglue_context_make_current(this.context)) {
	      SoContextHandler.destructingContext(this.renderid);
	      this.deactivateGLContext();
	    }
	    else {
	      if (CoinOffscreenGLCanvas.debug()) {
	        SoDebugError.post("CoinOffscreenGLCanvas::destructContext",
	                           "Couldn't activate context -- resource clean-up "+
	                           "not complete.");
	      }
	    }

	    SoGL.cc_glglue_context_destruct(this.context);

	    this.context = null;
	    this.renderid = 0;
	    this.current_hdc = null;
	  }

	  // *************************************************************************

	  /* This abomination is needed to support SoOffscreenRenderer::getDC(). */
	  public /*const void * const &*/Object
	  getHDC() 
	  {
	    return this.current_hdc;
	  }

	  public void updateDCBitmap()
	  {
	  //cc_glglue_win32_updateHDCBitmap(this.context); TODO
	  }
	  // *************************************************************************

	  // Pushes the rendered pixels into the internal memory array.
	  public void
	  readPixels(byte[] dst,
	                                    final SbVec2s vpdims,
	                                    int dstrowsize,
	                                    int nrcomponents)
	  {
		  readPixels(dst,0,vpdims,dstrowsize,nrcomponents);
	  }
	  public void
	  readPixels(byte[] dst, int offset,
	                                    final SbVec2s vpdims,
	                                    int dstrowsize,
	                                    int nrcomponents)
	  {
		  GL2 gl2 = new GL2() {};
		  
	    gl2.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

	    // First reset all settings that can influence the result of a
	    // glReadPixels() call, to make sure we get the actual contents of
	    // the buffer, unmodified.
	    //
	    // The values set up below matches the default settings of an
	    // OpenGL driver.

	    gl2.glPixelStorei(GL2.GL_PACK_SWAP_BYTES, 0);
	    gl2.glPixelStorei(GL2.GL_PACK_LSB_FIRST, 0);
	    gl2.glPixelStorei(GL2.GL_PACK_ROW_LENGTH, (int)dstrowsize);
	    gl2.glPixelStorei(GL2.GL_PACK_SKIP_ROWS, 0);
	    gl2.glPixelStorei(GL2.GL_PACK_SKIP_PIXELS, 0);

	    // FIXME: should use best possible alignment, for speediest
	    // operation. 20050617 mortene.
	  //   glPixelStorei(GL_PACK_ALIGNMENT, 4);
	    gl2.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 1);

	    gl2.glPixelTransferi(GL2.GL_MAP_COLOR, 0);
	    gl2.glPixelTransferi(GL2.GL_MAP_STENCIL, 0);
	    gl2.glPixelTransferi(GL2.GL_INDEX_SHIFT, 0);
	    gl2.glPixelTransferi(GL2.GL_INDEX_OFFSET, 0);
	    gl2.glPixelTransferf(GL2.GL_RED_SCALE, 1);
	    gl2.glPixelTransferf(GL2.GL_RED_BIAS, 0);
	    gl2.glPixelTransferf(GL2.GL_GREEN_SCALE, 1);
	    gl2.glPixelTransferf(GL2.GL_GREEN_BIAS, 0);
	    gl2.glPixelTransferf(GL2.GL_BLUE_SCALE, 1);
	    gl2.glPixelTransferf(GL2.GL_BLUE_BIAS, 0);
	    gl2.glPixelTransferf(GL2.GL_ALPHA_SCALE, 1);
	    gl2.glPixelTransferf(GL2.GL_ALPHA_BIAS, 0);
	    gl2.glPixelTransferf(GL2.GL_DEPTH_SCALE, 1);
	    gl2.glPixelTransferf(GL2.GL_DEPTH_BIAS, 0);

	    final int[] i = new int[1];//0;
	    final float[] f = new float[1];//0.0f;
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_I_TO_I, 1, f);
	    gl2.glPixelMapuiv(GL2.GL_PIXEL_MAP_S_TO_S, 1, i);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_I_TO_R, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_I_TO_G, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_I_TO_B, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_I_TO_A, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_R_TO_R, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_G_TO_G, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_B_TO_B, 1, f);
	    gl2.glPixelMapfv(GL2.GL_PIXEL_MAP_A_TO_A, 1, f);

	    // The flushing of the OpenGL pipeline before and after the
	    // glReadPixels() call is done as a work-around for a reported
	    // OpenGL driver bug: on a Win2000 system with ATI Radeon graphics
	    // card, the system would hang hard if the flushing was not done.
	    //
	    // This is obviously an OpenGL driver bug, but the workaround of
	    // doing excessive flushing has no real ill effects, so we just do
	    // it unconditionally for all drivers. Note that it might not be
	    // necessary to flush both before and after glReadPixels() to work
	    // around the bug (this was not established with the external
	    // reporter), but again it shouldn't matter if we do.
	    //
	    // For reference, the specific driver which was reported to fail has
	    // the following characteristics:
	    //
	    // GL_VENDOR="ATI Technologies Inc."
	    // GL_RENDERER="Radeon 9000 DDR x86/SSE2"
	    // GL_VERSION="1.3.3446 Win2000 Release"
	    //
	    // mortene.

	    gl2.glFlush(); gl2.glFinish();

	    assert((nrcomponents >= 1) && (nrcomponents <= 4));

	    if (nrcomponents < 3) {
	      byte[] tmp = new byte[(int)vpdims.getValue()[0]*vpdims.getValue()[1]*4];
	      gl2.glReadPixels(0, 0, vpdims.getValue()[0], vpdims.getValue()[1],
	                   nrcomponents == 1 ? GL2.GL_RGB : GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, tmp);

	      byte[] src = tmp;
	      int src_ = 0;
	      int dst_ = 0;
	      // manually convert to grayscale
	      for (short y = 0; y < vpdims.getValue()[1]; y++) {
	        for (short x = 0; x < vpdims.getValue()[0]; x++) {
	          double v = src[0+src_] * 0.3 + src[1+src_] * 0.59 + src[2+src_] * 0.11;
	          dst[dst_] = (byte)((int) v); dst_++;
	          if (nrcomponents == 2) {
	            dst[dst_] = src[3+src_]; dst_++;
	          }
	          src_ += nrcomponents == 1 ? 3 : 4;
	        }
	      }
	      //delete[] tmp; java port
	    }
	    else {
	      gl2.glReadPixels(0, 0, vpdims.getValue()[0], vpdims.getValue()[1],
	                   nrcomponents == 3 ? GL2.GL_RGB : GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, dst);
	    }
	    gl2.glFlush(); gl2.glFinish();

	    gl2.glPopAttrib();
	  }

	  // *************************************************************************

	  static boolean tilesize_cached = false;
	  static int[] maxtile = { 0, 0 };

	  static void tilesize_cleanup()
	  {
	    tilesize_cached = false;
	    maxtile[0] = maxtile[1] = 0;
	  }

	  // Return largest size of offscreen canvas system can handle. Will
	  // cache result, so only the first look-up is expensive.
	  public static SbVec2s
	  getMaxTileSize()
	  {
	    // cache the values in static variables so that a new context is not
	    // created every time render() is called in SoOffscreenRenderer
	    if (tilesize_cached) return new SbVec2s((short)maxtile[0], (short)maxtile[1]);

	    tilesize_cached = true; // Flip on first run.

	    //coin_atexit((coin_atexit_f*) tilesize_cleanup, CC_ATEXIT_NORMAL);

	    final int[] width = new int[1], height = new int[1];
	    Gl.cc_glglue_context_max_dimensions(width, height);

	    if (CoinOffscreenGLCanvas.debug()) {
	      SoDebugError.postInfo("CoinOffscreenGLCanvas::getMaxTileSize",
	                             "cc_glglue_context_max_dimensions()==["+width+", "+height+"]");
	    }

	    // Makes it possible to override the default tilesizes. Should prove
	    // useful for debugging problems on remote sites.
	    String env = TidBits.coin_getenv("COIN_OFFSCREENRENDERER_TILEWIDTH");
	    int forcedtilewidth = env != null ? Util.atoi(env) : 0;
	    env = TidBits.coin_getenv("COIN_OFFSCREENRENDERER_TILEHEIGHT");
	    int forcedtileheight = env != null ? Util.atoi(env) : 0;

	    if (forcedtilewidth != 0) { width[0] = forcedtilewidth; }
	    if (forcedtileheight != 0) { height[0] = forcedtileheight; }

	    // Also make it possible to force a maximum tilesize.
	    env = TidBits.coin_getenv("COIN_OFFSCREENRENDERER_MAX_TILESIZE");
	    int maxtilesize = env != null ? Util.atoi(env) : 0;
	    if (maxtilesize != 0) {
	      width[0] = Math.min(width[0], maxtilesize);
	      height[0] = Math.min(height[0], maxtilesize);
	    }

	    // cache result for later calls, and clamp to fit within a short
	    // integer type
	    maxtile[0] = Math.min(width[0], (int)Short.MAX_VALUE);
	    maxtile[1] = Math.min(height[0], (int)Short.MAX_VALUE);

	    return new SbVec2s((short)maxtile[0], (short)maxtile[1]);
	  }

	  // *************************************************************************

	    static int flag = -1; // -1 means "not initialized" in this context
	    
	  public static boolean
	  debug()
	  {
	    if (flag == -1) {
	      String env = TidBits.coin_getenv("COIN_DEBUG_SOOFFSCREENRENDERER");
	      flag = (env != null && (Util.atoi(env) > 0)) ? 1 : 0;
	    }
	    return flag != 0;
	  }

	    static int resourcehog_flag = -1; // -1 means "not initialized" in this context
	    
	  public static boolean
	  allowResourcehog()
	  {
	    if (resourcehog_flag == -1) {
	      String env = TidBits.coin_getenv("COIN_SOOFFSCREENRENDERER_ALLOW_RESOURCEHOG");
	      resourcehog_flag = (env != null && (Util.atoi(env) > 0)) ? 1 : 0;
	      SoDebugError.postInfo("CoinOffscreenGLCanvas",
	                             "Ignoring resource hogging due to set COIN_SOOFFSCREENRENDERER_ALLOW_RESOURCEHOG environment variable.");
	    }
	    return resourcehog_flag != 0;
	  }

	  // *************************************************************************
	  
}
