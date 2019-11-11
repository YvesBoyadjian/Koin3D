/**
 * 
 */
package jscenegraph.coin3d.inventor;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.rendering.CoinOffscreenGLCanvas;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoCullElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FILE;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class SoOffscreenRendererP implements Destroyable {

	public
	  SoOffscreenRendererP(SoOffscreenRenderer masterptr,
	                       final SbViewportRegion vpr)
	  {
		this(masterptr,vpr,null);
	  }
		public
		  SoOffscreenRendererP(SoOffscreenRenderer masterptr,
		                       final SbViewportRegion vpr,
		                       SoGLRenderAction glrenderaction)
		  {
		    this.master = masterptr;
		    this.didreadbuffer = true;

		    this.backgroundcolor.setValue(0,0,0);
		    this.components = SoOffscreenRenderer.Components.RGB;
		    this.buffer = null;
		    this.bufferbytesize = 0;
		    this.lastnodewasacamera = false;
			
		    if (glrenderaction != null) {
		      this.renderaction = glrenderaction;
		    }
		    else {
		      this.renderaction = new SoGLRenderAction(vpr);
		      this.renderaction.setCacheContext(SoGLCacheContextElement.getUniqueCacheContext());
		      this.renderaction.setTransparencyType(SoGLRenderAction.TransparencyType.SORTED_OBJECT_BLEND);
		    }

		    this.didallocation = (glrenderaction != null) ? false : true;
		    this.viewport.copyFrom(vpr);
			this.useDC = false;
		  }

		  public void destructor()
		  {
		    if (this.didallocation) { Destroyable.delete( this.renderaction); }
		  }

		  //static SbBool offscreenContextsNotSupported(void);

		  //static const char * debugTileOutputPrefix(void);

		  //static SoGLRenderAction::AbortCode GLRenderAbortCallback(void *userData);
		  //SbBool renderFromBase(SoBase * base);

		  //void setCameraViewvolForTile(SoCamera * cam);

//		  static SbBool writeToRGB(FILE * fp, unsigned int w, unsigned int h,
//		                           unsigned int nrcomponents, const uint8_t * imgbuf);

		  final SbViewportRegion viewport = new SbViewportRegion();
		  final SbColor backgroundcolor = new SbColor();
		  SoOffscreenRenderer.Components components;
		  SoGLRenderAction renderaction; //ptr
		  boolean didallocation;

		  //void updateDCBitmap();
		  boolean useDC;

		  byte[] buffer;
		  /*size_t*/int bufferbytesize;

		  final CoinOffscreenGLCanvas glcanvas = new CoinOffscreenGLCanvas();
		  final int[] glcanvassize = new int[2];

		  final int[] numsubscreens = new int[2];
		  // The subscreen size of the current tile. (Less than max if it's a
		  // right- or bottom-border tile.)
		  final int[] subsize = new int[2];
		  // Keeps track of the current tile to be rendered.
		  final SbVec2s currenttile = new SbVec2s();

		  boolean lastnodewasacamera;
		  SoCamera visitedcamera; // ptr

		  // used for lazy readPixels()
		  boolean didreadbuffer;
		private
		  SoOffscreenRenderer master; //ptr

		// *************************************************************************

		// Set the environment variable below to get the individual tiles
		// written out for debugging purposes. E.g.
		//
		//   $ export COIN_DEBUG_SOOFFSCREENRENDERER_TILEPREFIX="/tmp/offscreentile_"
		//
		// Tile X and Y position, plus the ".rgb" suffix, will be added when
		// writing.
		public static String
		debugTileOutputPrefix()
		{
		  return TidBits.coin_getenv("COIN_DEBUG_SOOFFSCREENRENDERER_TILEPREFIX");
		}

	      static boolean firstT = true;
	      
		// FIXME: this should really be done by SoCamera, on the basis of data
		// from an "SoTileRenderingElement". See BUGS.txt, item #121. 20050712 mortene.
		public void
		setCameraViewvolForTile(SoCamera cam)
		{
		  SoState state = (master.getGLRenderAction()).getState();

		  // A small trick to change the aspect ratio without changing the
		  // scenegraph camera.
		  final SbViewVolume vv = new SbViewVolume();
		  final float aspectratio = this.viewport.getViewportAspectRatio();
		  final SbVec2s vporigin = new SbVec2s(this.viewport.getViewportOriginPixels());

		  switch(SoCamera.ViewportMapping.fromValue(cam.viewportMapping.getValue())) {
		  case CROP_VIEWPORT_FILL_FRAME:
		  case CROP_VIEWPORT_LINE_FRAME:
		  case CROP_VIEWPORT_NO_FRAME:
		    vv.copyFrom( cam.getViewVolume(0.0f));

		    { // FIXME: should really fix this bug, not just warn that it is
		      // there. See item #191 in Coin/BUGS.txt for more information.
		      // 20050714 mortene.
		      if (firstT) {
		        final String[] s = new String[1];
		        cam.viewportMapping.get(s);
		        SoDebugError.postWarning("SoOffscreenRendererP::setCameraViewvolForTile",
		                                  "The SoOffscreenRenderer does not yet work "+
		                                  "properly with the SoCamera::viewportMapping "+
		                                  "field set to '"+s+"'");
		        firstT = false;
		      }
		    }
		    break;
		  case ADJUST_CAMERA:
		    vv.copyFrom( cam.getViewVolume(aspectratio));
		    if (aspectratio < 1.0f) vv.scale(1.0f / aspectratio);
		    break;
		  case LEAVE_ALONE:
		    vv.copyFrom( cam.getViewVolume(0.0f));
		    break;
		  default:
		    assert(false && "unknown viewport mapping"!=null);
		    break;
		  }

		  int LEFTINTPOS = (this.currenttile.getValue()[0] * this.glcanvassize[0]) - vporigin.getValue()[0];
		  int RIGHTINTPOS = LEFTINTPOS + this.subsize[0];
		  int TOPINTPOS = (this.currenttile.getValue()[1] * this.glcanvassize[1]) - vporigin.getValue()[1];
		  int BOTTOMINTPOS = TOPINTPOS + this.subsize[1];

		  SbVec2s fullsize = new SbVec2s(this.viewport.getViewportSizePixels());
		  float left = (float)(LEFTINTPOS) / (float)(fullsize.getValue()[0]);
		  float right = (float)(RIGHTINTPOS) / (float)(fullsize.getValue()[0]);
		  // Swap top / bottom, to flip the coordinate system for the Y axis
		  // the way we want it.
		  float top = (float)(BOTTOMINTPOS) / (float)(fullsize.getValue()[1]);
		  float bottom = (float)(TOPINTPOS) / (float)(fullsize.getValue()[1]);

		  if (CoinOffscreenGLCanvas.debug()) {
		    SoDebugError.postInfo("SoOffscreenRendererP::setCameraViewvolForTile",
		                           "narrowing for tile <"+this.currenttile.getValue()[0]+", "+this.currenttile.getValue()[1]+">: <"+left+", "+bottom+"> - <"+right+", "+top+">");
		  }

		  // Reshape view volume
		  vv.copyFrom( vv.narrow(left, bottom, right, top));

		  final SbMatrix proj = new SbMatrix(), affine = new SbMatrix();
		  vv.getMatrices(affine, proj);

		  // Support antialiasing if renderpasses > 1
		  if (renderaction.getNumPasses() > 1) {
		    final SbVec3f jittervec = new SbVec3f();
		    final SbMatrix m = new SbMatrix();
		    TidBits.coin_viewvolume_jitter(renderaction.getNumPasses(), renderaction.getCurPass(),
		                           this.glcanvassize, (float [])jittervec.getValueRead());
		    m.setTranslate(jittervec);
		    proj.multRight(m);
		  }

		  SoCullElement.setViewVolume(state, vv);
		  SoViewVolumeElement.set(state, cam, vv);
		  SoProjectionMatrixElement.set(state, cam, proj);
		  SoViewingMatrixElement.set(state, cam, affine);
		}

		// *************************************************************************

		public static boolean
		offscreenContextsNotSupported()
		{
		  // Returning FALSE means that offscreen rendering seems to be
		  // generally supported on the system.
		  //
		  // (It is however important to be robust and handle cases where it
		  // still fails, as this can happen due to e.g. lack of resources or
		  // other causes that may change during run-time.)

//		#ifdef HAVE_GLX
//		  return FALSE;
//		#elif defined(HAVE_WGL)
		  return false;
//		#elif defined(COIN_MACOS_10)
//		  return FALSE;
//		#endif
//
//		  // No win-system GL binding was found, so we're sure that offscreen
//		  // rendering can *not* be done.
//		  return TRUE;
		}

		//*************************************************************************

		// Callback when rendering scenegraph to subscreens. Detects when a
		// camera has just been traversed, and then invokes the method which
		// narrows the camera viewport according to the current tile we're
		// rendering to.
		//
		// FIXME: if possible, it would be better to pick up from the state
		// whatever data we're now grabbing directly from the SoCamera nodes.
		// It'd be more robust, I believe, as the elements set by SoCamera can
		// in principle also be set from other code. 20041006 mortene.
		//
		// UPDATE 20050711 mortene: on how to fix this properly, see item #121
		// in Coin/BUGS.txt.
		public static SoGLRenderAction.AbortCode
		GLRenderAbortCallback(Object userData)
		{
		  SoOffscreenRendererP thisp = (SoOffscreenRendererP ) userData;
		  SoFullPath path = SoFullPath.cast(thisp.renderaction.getCurPath());
		  SoNode node = path.getTail();
		  assert(node != null);

		  if (thisp.lastnodewasacamera) {
		    thisp.setCameraViewvolForTile(thisp.visitedcamera);
		    thisp.lastnodewasacamera = false;
		  }

		  if (node.isOfType(SoCamera.getClassTypeId())) {
		    thisp.visitedcamera = (SoCamera ) node;
		    thisp.lastnodewasacamera = true;

		    // FIXME: this is not really entirely sufficient. If a camera is
		    // already within a cached list upon the first invocation of a
		    // render pass, we'll never get a callback upon encountering it.
		    //
		    // This would be a fairly obscure case, though, as the glcache
		    // would have to be set up in another context, compatible for
		    // sharing GL data with the one set up internally by the
		    // SoOffscreenRenderer -- which is very unlikely.
		    //
		    // 20050512 mortene.
		    //
		    // UPDATE 20050711 mortene: on how to fix this properly, see item
		    // #121 in Coin/BUGS.txt. (The tile number should be in an
		    // element, which the SoCamera would query (and thereby also make
		    // the cache dependent on)).
		    SoCacheElement.invalidate(thisp.renderaction.getState());
		  }

		  return SoGLRenderAction.AbortCode.CONTINUE;
		}

	    static boolean firstB = true;

		  static int forcetiled = -1;
		  
	    // Collects common code from the two render() functions.
		public boolean
		renderFromBase(SoBase base)
		{
		  if (SoOffscreenRendererP.offscreenContextsNotSupported()) {
		    if (firstB) {
		      SoDebugError.post("SoOffscreenRenderer::renderFromBase",
		                         "SoOffscreenRenderer not compiled against any "+
		                         "window-system binding, it is defunct for this build.");
		      firstB = false;
		    }
		    return false;
		  }

		  final SbVec2s fullsize = new SbVec2s(this.viewport.getViewportSizePixels());
		  this.glcanvas.setWantedSize(fullsize);

		  // check if no possible canvas size was found
		  if (this.glcanvas.getActualSize().operator_equal_equal(new SbVec2s((short)0, (short)0))) { return false; }

		  final int newcontext = this.glcanvas.activateGLContext();
		  if (newcontext == 0) {
		    SoDebugError.postWarning("SoOffscreenRenderer::renderFromBase",
		                              "Could not set up an offscreen OpenGL context.");
		    return false;
		  }

		  final SbVec2s glsize = new SbVec2s(this.glcanvas.getActualSize());

		  // We need to know the actual GL viewport size for tiled rendering,
		  // in calculations when narrowing the camera view volume -- so we
		  // store away this value for the "found a camera"-callback.
		  //
		  // FIXME: seems unnecessary now, should be able to just query
		  // glcanvas.getActualSize() XXX
		  this.glcanvassize[0] = glsize.getValue()[0];
		  this.glcanvassize[1] = glsize.getValue()[1];

		  if (CoinOffscreenGLCanvas.debug()) {
		    SoDebugError.postInfo("SoOffscreenRendererP::renderFromBase",
		                           "fullsize==<"+fullsize.getValue()[0]+", "+fullsize.getValue()[1]+">, glsize==<"+glsize.getValue()[0]+", "+glsize.getValue()[1]+">");
		  }

		  // oldcontext is used to restore the previous context id, in case
		  // the render action is not allocated by us.
		  final int oldcontext = this.renderaction.getCacheContext();
		  this.renderaction.setCacheContext(newcontext);
		  
		  GL2 gl2 = new GL2() {};

		  if (CoinOffscreenGLCanvas.debug()) {
		    int[] colbits0 = new int[4];
		    int[] colbits1 = new int[4];
		    int[] colbits2 = new int[4];
		    int[] colbits3 = new int[4];
		    gl2.glGetIntegerv(GL.GL_RED_BITS, colbits0);
		    gl2.glGetIntegerv(GL.GL_GREEN_BITS, colbits1);
		    gl2.glGetIntegerv(GL.GL_BLUE_BITS, colbits2);
		    gl2.glGetIntegerv(GL.GL_ALPHA_BITS, colbits3);
		    SoDebugError.postInfo("SoOffscreenRenderer::renderFromBase",
		                           "GL context GL_[RED|GREEN|BLUE|ALPHA]_BITS=="+
		                           "["+colbits0[0]+", "+colbits1[0]+", "+colbits2[0]+", "+colbits3[0]+"]");
		  }

		  gl2.glEnable(GL.GL_DEPTH_TEST);
		  gl2.glClearColor(this.backgroundcolor.getValueRead()[0],
		               this.backgroundcolor.getValueRead()[1],
		               this.backgroundcolor.getValueRead()[2],
		               0.0f);

		  // Make this large to get best possible quality on any "big-image"
		  // textures (from using SoTextureScalePolicy).
		  //
		  // FIXME: this doesn't seem to be working, according to a report by
		  // Colin Dunlop. See bug item #108. 20050509 mortene.
		  //
		  // UPDATE 20050711 mortene: the bug report referred to above may not
		  // be correct. We should anyway fix this in a more appropriate
		  // manner, for instance by setting up a new element with a boolean
		  // value to indicate whether or not stuff should be rendered in
		  // maximum quality. That would be generally useful for having better
		  // control from the offscreenrenderer.
		  final int bigimagechangelimit = SoGLBigImage.setChangeLimit(Integer.MAX_VALUE/*INT_MAX*/);

		  // Deallocate old and allocate new target buffer, if necessary.
		  //
		  // If we need more space:
		  final int bufsize =
		    (int)fullsize.getValue()[0] * fullsize.getValue()[1] * master.getComponents().ordinal();
		  boolean alloc = (bufsize > this.bufferbytesize);
		  // or if old buffer was much larger, free up the memory by fitting
		  // to smaller size:
		  alloc = alloc || (bufsize <= (this.bufferbytesize / 8));

		  if (alloc) {
		    //delete[] this.buffer; java port
		    this.buffer = new byte[bufsize];
		    this.bufferbytesize = bufsize;
		  }

		  if (SoOffscreenRendererP.debugTileOutputPrefix() != null) {
		    //(void)memset(this.buffer, 0x00, bufsize);
		  }

		  // needed to clear viewport after glViewport() is called from
		  // SoGLRenderAction
		  this.renderaction.addPreRenderCallback(SoOffscreenRendererP::pre_render_cb, null);

		  // For debugging purposes, it has been made possible to use an
		  // envvar to *force* tiled rendering even when it can be done in a
		  // single chunk.
		  //
		  // (Note: don't use this envvar when using SoExtSelection nodes, for
		  // the reason noted below.)
		  if (forcetiled == -1) {
		    String env = TidBits.coin_getenv("COIN_FORCE_TILED_OFFSCREENRENDERING");
		    forcetiled = (env != null && (Util.atoi(env) > 0)) ? 1 : 0;
		    if (forcetiled != 0) {
		      SoDebugError.postInfo("SoOffscreenRendererP::renderFromBase",
		                             "Forcing tiled rendering.");
		    }
		  }

		  // FIXME: tiled rendering should be decided on the exact same
		  // criteria as is used in SoExtSelection to decide which size to use
		  // for its offscreen-buffer, as that node fails in VISIBLE_SHAPE
		  // mode with tiled rendering. This is a weakness with SoExtSelection
		  // which should be improved upon, if possible (i.e. fix
		  // SoExtSelection, rather than adding some kind of "semi-private"
		  // API to let SoExtSelection find out whether or not tiled rendering
		  // is used). 20041028 mortene.
		  boolean tiledrendering =
		    forcetiled != 0 || (fullsize.getValue()[0] > glsize.getValue()[0]) || (fullsize.getValue()[1] > glsize.getValue()[1]);

		  // Shall we use subscreen rendering or regular one-screen renderer?
		  if (tiledrendering) {
		    // we need to copy from GL to system memory if we're doing tiled rendering
		    this.didreadbuffer = true;

		    for (int i=0; i < 2; i++) {
		      this.numsubscreens[i] = (fullsize.getValue()[i] + (glsize.getValue()[i] - 1)) / glsize.getValue()[i];
		    }

		    // We have to grab cameras using this callback during rendering
		    this.visitedcamera = null;
		    this.renderaction.setAbortCallback(SoOffscreenRendererP::GLRenderAbortCallback, this);

		    // Render entire scenegraph for each subscreen.
		    for (int y=0; y < this.numsubscreens[1]; y++) {
		      for (int x=0; x < this.numsubscreens[0]; x++) {
		        this.currenttile.copyFrom( new SbVec2s((short)x, (short)y));

		        // Find current "active" tilesize.
		        this.subsize[0] = glsize.getValue()[0];
		        this.subsize[1] = glsize.getValue()[1];
		        if (x == (this.numsubscreens[0] - 1)) {
		          this.subsize[0] = fullsize.getValue()[0] % glsize.getValue()[0];
		          if (this.subsize[0] == 0) { this.subsize[0] = glsize.getValue()[0]; }
		        }
		        if (y == (this.numsubscreens[1] - 1)) {
		          this.subsize[1] = fullsize.getValue()[1] % glsize.getValue()[1];
		          if (this.subsize[1] == 0) { this.subsize[1] = glsize.getValue()[1]; }
		        }

		        SbViewportRegion subviewport = new SbViewportRegion(new SbVec2s((short)this.subsize[0], (short)this.subsize[1]));
		        this.renderaction.setViewportRegion(subviewport);

		        if (base.isOfType(SoNode.getClassTypeId()))
		          this.renderaction.apply((SoNode )base);
		        else if (base.isOfType(SoPath.getClassTypeId()))
		          this.renderaction.apply((SoPath )base);
		        else {
		          assert(false && "Cannot apply to anything else than an SoNode or an SoPath" != null);
		        }

		        int nrcomp = master.getComponents().ordinal();

		        int MAINBUF_OFFSET =
		          (glsize.getValue()[1] * y * fullsize.getValue()[0] + glsize.getValue()[0] * x) * nrcomp;

		        SbVec2s vpsize = new SbVec2s(subviewport.getViewportSizePixels());
		        this.glcanvas.readPixels(this.buffer, MAINBUF_OFFSET,
		                                  vpsize, fullsize.getValue()[0], nrcomp);

		        // Debug option to dump the (full) buffer after each
		        // iteration.
		        if (SoOffscreenRendererP.debugTileOutputPrefix() != null) {
		          String s = SoOffscreenRendererP.debugTileOutputPrefix()+"_"+x+"_"+y+".rgb"
		                    ;

		          FILE f = FILE.fopen(s, "wb");
		          assert(f != null);
		          boolean w = SoOffscreenRendererP.writeToRGB(f, fullsize.getValue()[0], fullsize.getValue()[1],
		                                                      nrcomp, this.buffer);
		          assert(w);
		          int r = FILE.fclose(f);
		          assert(r == 0);

		          // This is sometimes useful to enable during debugging to
		          // see the exact order and position of the tiles. Not
		          // enabled by default because it makes the final buffer
		          // completely blank.
//		#if 0 // debug
//		          (void)memset(this.buffer, 0x00, bufsize);
//		#endif // debug
		        }
		      }
		    }

		    this.renderaction.setAbortCallback(null, this);

		    if (this.visitedcamera == null) {
		      SoDebugError.postWarning("SoOffscreenRenderer::renderFromBase",
		                                "No camera node found in scenegraph while rendering offscreen image. "+
		                                "The result will most likely be incorrect.");
		    }

		  }
		  // Regular, non-tiled rendering.
		  else {
		    // do lazy buffer read (GL context is read in getBuffer())
		    this.didreadbuffer = false;
			
			final SbViewportRegion region = new SbViewportRegion();

			region.setViewportPixels((short)0,(short)0,fullsize.getValue()[0],fullsize.getValue()[1]);

		    this.renderaction.setViewportRegion(region);

		    SbTime t = SbTime.getTimeOfDay(); // for profiling

		    if (base.isOfType(SoNode.getClassTypeId()))
		      this.renderaction.apply((SoNode )base);
		    else if (base.isOfType(SoPath.getClassTypeId()))
		      this.renderaction.apply((SoPath )base);
		    else  {
		      assert(false && "Cannot apply to anything else than an SoNode or an SoPath"!= null);
		    }

		    if (CoinOffscreenGLCanvas.debug()) {
		      SoDebugError.postInfo("SoOffscreenRendererP::renderFromBase",
		                             "*TIMING* SoGLRenderAction::apply() took "+(SbTime.getTimeOfDay().operator_minus(t)).getValue() * 1000+" msecs"
		                             );
		      t = SbTime.getTimeOfDay();
		    }

		    if (CoinOffscreenGLCanvas.debug()) {
		      SoDebugError.postInfo("SoOffscreenRendererP::renderFromBase",
		                             "*TIMING* glcanvas.readPixels() took "+(SbTime.getTimeOfDay().operator_minus(t)).getValue() * 1000+" msecs"
		                             );
		    }
		  }

		  this.renderaction.removePreRenderCallback(SoOffscreenRendererP::pre_render_cb, null);

		  // Restore old value.
		  SoGLBigImage.setChangeLimit(bigimagechangelimit);

		  this.glcanvas.deactivateGLContext();
		  this.renderaction.setCacheContext(oldcontext); // restore old

		  if(this.useDC)
			this.updateDCBitmap();

		  return true;
		}

		// *************************************************************************

		static void
		pre_render_cb(Object userdata, SoGLRenderAction action)
		{
			GL2 gl2 = action.getState().getGL2();
			
		  gl2.glClear(GL.GL_DEPTH_BUFFER_BIT|GL.GL_COLOR_BUFFER_BIT);
		  action.setRenderingIsRemote(false);
		}


public void updateDCBitmap()
{
  this.glcanvas.updateDCBitmap();
}
// *************************************************************************

//
// avoid endian problems (little endian sucks, right? :)
//
static int
write_short(FILE fp, short val)
{
  byte[] tmp = new byte[2];
  tmp[0] = (byte)(val >>> 8);
  tmp[1] = (byte)(val & 0xff);
  return FILE.fwrite(tmp, 2, 1, fp);
}

public static boolean
writeToRGB(FILE fp, int w, int h,
                                 int nrcomponents,
                                 byte[] imgbuf)
{
  // FIXME: add code to rle rows, pederb 2000-01-10

  write_short(fp, (short)0x01da); // imagic
  write_short(fp, (short)0x0001); // raw (no rle yet)

  if (nrcomponents == 1)
    write_short(fp, (short)0x0002); // 2 dimensions (heightmap)
  else
    write_short(fp, (short)0x0003); // 3 dimensions

  write_short(fp, (short) w);
  write_short(fp, (short) h);
  write_short(fp, (short) nrcomponents);

  int BUFSIZE = 500;
  byte[] buf = new byte[BUFSIZE];
  //(void)memset(buf, 0, BUFSIZE); java port
  buf[7] = (byte)255; // set maximum pixel value to 255
  Util.strcpy(buf,8, "http://www.coin3d.org");
  int wrote = FILE.fwrite(buf, 1, BUFSIZE, fp);
  assert(wrote == BUFSIZE);

  byte[] tmpbuf = new byte[w];

  boolean writeok = true;
  for ( int c = 0; c < nrcomponents; c++) {
    for ( int y = 0; y < h; y++) {
      for ( int x = 0; x < w; x++) {
        tmpbuf[x] = imgbuf[(x + y * w) * nrcomponents + c];
      }
      writeok = writeok && (FILE.fwrite(tmpbuf, 1, w, fp) == w);
    }
  }

  if (!writeok) {
    SoDebugError.postWarning("SoOffscreenRendererP::writeToRGB",
                              "error when writing RGB file");
  }

  //delete [] tmpbuf; java port
  return writeok;
}

}
