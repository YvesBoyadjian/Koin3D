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
  \class SoGLRenderAction SoGLRenderAction.h Inventor/actions/SoGLRenderAction.h
  \brief The SoGLRenderAction class renders the scene graph with OpenGL calls.
  \ingroup actions

  Applying this method at a root node for a scene graph, path or
  pathlist will render all geometry contained within that instance to
  the current OpenGL context.
 */

// *************************************************************************

/*!
  \typedef void SoGLRenderPassCB(void * userdata)

  Callback functions for the setPassCallback() method need to be of
  this type.

  \a userdata is a void pointer to any data the application need to
  know of in the callback function (like for instance a \e this
  pointer).

  \sa setPassCallback()
 */

// *************************************************************************

// *************************************************************************

/*!
  \enum SoGLRenderAction::TransparencyType

  Various settings for how to do rendering of transparent objects in
  the scene. Some of the settings will provide faster rendering, while
  others give you better quality rendering.

  Note that doing correct rendering of \e multiple transparent objects
  often fails, because to be 100% correct, all polygons needs to be
  rendered in sorted order, and polygons can't intersect each
  other. In a dynamic scene graph it is often impossible to guarantee
  that no polygons intersect, and finding an algorithm that does
  correct sorting of polygons for all possible cases is very hard and
  time-consuming.

  The highest quality transparency mode in the original SGI / TGS Open
  Inventor is SoGLRenderAction::SORTED_OBJECT_BLEND, where all
  transparent objects are rendered in sorted order in a rendering pass
  after all opaque objects. However, this mode does not sort the
  polygons, and if you have an object where some polygon A is behind
  some other polygon B, the transparency will only be correct if A
  happens to be rendered before B. For other camera angles, where B is
  behind A, the transparency will not be correct.

  In Coin we have a new transparency mode that solves some of these
  problems: SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_BLEND. In
  addition to sorting the objects, all polygons inside each object is
  also sorted back-to-front when rendering. But, if you have
  intersecting objects and/or intersecting polygons, even this
  transparency mode will fail. Also, because of the polygon sorting,
  this transparency mode is quite slow. It is possible to speed things
  up using the SoTransparencyType node, though, which enables you to
  set different transparency modes for different parts of the scene
  graph. If you have only have a few objects where you need to sort
  the polygons, you can use
  SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_BLEND for those, and
  for instance SoGLRenderAction::SORTED_OBJECT_BLEND for all other
  transparent objects.

  The highest quality transparency mode in Coin is
  SoGLRenderAction::SORTED_LAYERS_BLEND. It is also the only mode that
  overrides all other modes in the scenegraph.

  (One important note about this mode: we've had reports from users
  that some OpenGL drivers -- possibly particular for some Mac OS X
  systems -- significantly degrades rendering performance. So be
  careful and test your application on a wide variety of run-time
  systems when using SoGLRenderAction::SORTED_LAYERS_BLEND.)

  \sa SoTransparencyType
*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SCREEN_DOOR

  Transparent triangles are rendered with a dither pattern. This is
  a fast (on most GFX cards) but not-so-high-quality transparency mode.

  One particular feature of this mode is that you are guaranteed that
  it always renders the transparent parts of the scene correct with
  regard to internal depth ordering of objects / polygons, something
  which is not the case for any other transparency mode.

  Polygons rendered with only transparent textures are not shown as
  being transparent when using this mode. The reason being that the
  SCREEN_DOOR mode is working on polygons, not pixels. To render
  polygons with dither pattern, a material node has to be inserted
  into the scenegraph with it's transparency field set.
*/

/*!

  \var SoGLRenderAction::TransparencyType SoGLRenderAction::ADD

  Transparent objects are rendered using additive alpha blending.
  Additive blending is probably mostly used to create special
  transparency effects. The new pixel color is calculated as the
  current pixel color plus the source pixel color multiplied with the
  source pixel alpha value.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::DELAYED_ADD

  SoGLRenderAction::DELAYED_ADD Transparent objects are rendered using
  additive alpha blending, in a second rendering pass with depth
  buffer updates disabled.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SORTED_OBJECT_ADD

  Transparent objects are rendered using additive alpha blending.
  Opaque objects are rendered first, and transparent objects are
  rendered back to front with z-buffer updates disabled.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::BLEND

  Transparent objects are rendered using multiplicative alpha blending.

  Multiplicative alpha blending is the blending type that is most
  often used to render transparent objects. The new pixel value is
  calculated as the old pixel color multiplied with one minus the
  source alpha value, plus the source pixel color multiplied with the
  source alpha value.

  We recommend that you use this transparency mode if you have only
  one transparent object in your scene, and you know that it will be
  rendered after the opaque objects.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::DELAYED_BLEND

  Transparent objects are rendered using multiplicative alpha
  blending, in a second rendering pass with depth buffer updates
  disabled.

  Use this transparency type when you have one transparent object, or
  several transparent object that you know will never overlap (when
  projected to screen). Since the transparent objects are rendered
  after opaque ones, you'll not have to worry about putting the
  transparent objects at the end of your scene graph. It will not be
  as fast as the BLEND transparency type, of course, since the scene
  graph is traversed twice.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SORTED_OBJECT_BLEND

  Transparent objects are rendered using multiplicative alpha
  blending, Opaque objects are rendered first, and transparent objects
  are rendered back to front with z-buffer updates disabled.

  Use this transparency mode when you have several transparent object
  that you know might overlap (when projected to screen). This method
  will require 1 + num_transparent_objects rendering passes. Path
  traversal is used when rendering transparent objects, of course, but
  it might still be slow if you have lots of state changes before your
  transparent object. When using this mode, we recommend placing the
  transparent objects as early as possible in the scene graph to
  minimize traversal overhead.
*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_ADD

  This transparency type is a Coin extension versus the original SGI
  Open Inventor API.

  Transparent objects are rendered back to front, and triangles in
  each object are sorted back to front before rendering.

  See description for SORTED_OBJECT_SORTED_TRIANGLE_BLEND for more
  information about this transparency type.

*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_BLEND

  This transparency type is a Coin extension versus the original SGI
  Open Inventor API.

  Transparent objects are rendered back to front, and triangles in
  each object are sorted back to front before rendering.

  Use this transparency type when you have one (or more) transparent
  object(s) where you know triangles might overlap inside the object.
  This transparency type might be very slow if you have an object with
  lots of triangles, since all triangles have to be sorted before
  rendering, and an unoptimized rendering loop is used when rendering.
  Lines and points are not sorted before rendering. They are rendered
  as in the normal SORTED_OBJECT_BLEND transparency type.

  Please note that this transparency mode does not guarantee
  "correct" transparency rendering. It is almost impossible to find an
  algorithm that will sort triangles correctly in all cases, and
  intersecting triangles are not handled. Also, since each object
  is handled separately, two intersecting object will lead to
  incorrect transparency.
*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::NONE

  This transparency type is a Coin extension versus the Open Inventor
  API.

  Turns off transparency for objects, even if transparency is set using
  an SoMaterial node.

  \since Coin 1.0
*/

/*!
  \var SoGLRenderAction::TransparencyType SoGLRenderAction::SORTED_LAYERS_BLEND

  This transparency type is a Coin extension versus the original SGI
  Open Inventor API.

  By using this transparency type, the SoGLRenderAction will render
  normal and intersecting transparent objects correctly independent of
  rendering order. It is the only transparency type rendering mode
  which is guaranteed to do so.

  This mode is different from all other modes in that it overrides the
  SoTransparencyType nodes in the scenegraph; all objects are drawn using
  SORTED_LAYERS_BLEND.

  There are currently two separate code paths for this mode. Both
  paths are heavily based on OpenGL extensions. The first method is
  based on extensions which are only available on NVIDIA chipsets
  (GeForce3 and above, except GeForce4 MX). These extensions are \c
  GL_NV_texture_shader, \c GL_NV_texture_rectangle or \c
  GL_EXT_texture_rectangle, \c GL_NV_register_combiners, \c
  GL_ARB_shadow and \c GL_ARB_depth_texture.  Please note that this
  transparency type occupy all four texture units on the NVIDIA card
  for all the rendering passes, except the first. Textured surfaces
  will therefore only be textured if they are not occluded by another
  transparent surface.

  The second method utilise the \c GL_ARB_fragment_program
  extension. This extension is currently supported by the GeForceFX
  family and the Radeon 9500 and above. This technique is faster than
  the pure NVIDIA method. The fragment program method will
  automatically be chosen if possible.
  Please note that one should beware not to place the near-plane too
  close to the camera due to the lack of floating point precision
  control in fragment programs. Doing so may lead to loss of precision
  around the edges and 'jaggedness' of the transparent geometry.

  Setting the environment variable COIN_SORTED_LAYERS_USE_NVIDIA_RC to
  '1' will force the use of former code path instead of the latter,
  even if it is available.

  A rendering context with >= 24 bits depth buffer and 8 bits alpha
  channel must be the current rendering context for this blending mode
  to actually become activated. If the current rendering canvas does
  not have these properties, Coin will fall back on a simpler
  transparency handling mode. If you are using one of the
  window-system binding libraries provided by Kongsberg Oil & Gas Technologies,
  e.g. SoXt, SoQt or SoWin, you will need to explicitly enable this in
  your viewer. See the API documentation of the \c setAlphaChannel()
  method of either SoXtGLWidget, SoQtGLWidget or SoWinGLWidget.

  The detection of whether or not the SORTED_LAYERS_BLEND mode can be
  used will be done automatically by the Coin internals. If one or
  more of the necessary conditions listed above are unavailable,
  SoGLRenderAction::SORTED_OBJECT_BLEND will be used as the
  transparency type instead.

  To be able to render correct transparency independent of object
  order, one have to render in multiple passes. This technique is
  based on depth-peeling which strips away depth layers with each
  successive pass. The number of passes is therefore an indication of
  how deep into the scene transparent surfaces will be rendered with
  transparency. A higher number will lead to a lower framerate but
  higher quality for scenes with a lot of transparent surfaces. The
  default number of passes is '4'. This number can be specified using
  the SoGLRenderAction::setSortedLayersNumPasses() or by letting the
  environment variable \c COIN_NUM_SORTED_LAYERS_PASSES or \c
  OIV_NUM_SORTED_LAYERS_PASSES specify the number of passes.

  A more detailed presentation of the algorithm is written by Cass
  Everitt at NVIDIA;

  "Interactive Order-Independent Transparency"
  http:://developer.nvidia.com/object/order_independent_transparency.html

  \since Coin 2.2
  \since TGS Inventor 4.0
*/

// FIXME:
//  todo: - Add debug printout info concerning choosen blend method.
//        - Add GL_[NV/HP]_occlusion_test support making the number of passes adaptive.
//        - Maybe pbuffer support to eliminate the slow glCopyTexSubImage2D calls.
//        - Investigate whether the TGS method using only EXT_texture_env_combine is a
//          feasible method (especially when it comes to speed and number of required
//          texture units). [If more than two units are needed, then
//          a GeForce3++ card is required, which again is already
//          supported using the NVIDIA method.]
// (20031128 handegar)
//

/*!
  \enum SoGLRenderAction::AbortCode

  The return codes which an SoGLRenderAbortCB callback function should
  use.

  \sa setAbortCallback()
*/
/*!
  \var SoGLRenderAction::AbortCode SoGLRenderAction::CONTINUE
  Continue rendering as usual.
*/
/*!
  \var SoGLRenderAction::AbortCode SoGLRenderAction::ABORT
  Abort the rendering action immediately.
*/
/*!
  \var SoGLRenderAction::AbortCode SoGLRenderAction::PRUNE
  Do not render the current node or any of its children, but continue
  the rendering traversal.
*/
/*!
  \var SoGLRenderAction::AbortCode SoGLRenderAction::DELAY
  Delay rendering of the current node (and its children) until the
  next rendering pass.
*/

/*!
  \typedef typedef SoGLRenderAction::SoGLRenderAbortCB(void * userdata)
  Abort callbacks should be of this type.
  \sa setAbortCallback()
*/

/*!
  \typedef float SoGLSortedObjectOrderCB(void * userdata, SoGLRenderAction * action)

  A callback used for controlling the transparency sorting order.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
   \enum SoGLRenderAction::SortedObjectOrderStrategy

   Used for enumerating the different transparency sorting strategies.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
  \var SoGLRenderAction::SortedObjectOrderStrategy SoGLRenderAction::BBOX_CENTER

  Do the sorting based on the center of the object bounding box.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
  \var SoGLRenderAction::SortedObjectOrderStrategy SoGLRenderAction::BBOX_CLOSEST_CORNER

  Do the sorting based on the bounding box corner closest to the camera.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
  \var SoGLRenderAction::SortedObjectOrderStrategy SoGLRenderAction::BBOX_FARTHEST_CORNER

  Do the sorting based on the bounding box corner farthest from the camera.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
  \var SoGLRenderAction::SortedObjectOrderStrategy SoGLRenderAction::CUSTOM_CALLBACK

  Use a custom callback to determine the sorting order.

  \sa setSortedObjectOrderStrategy().
  \since Coin 2.5
*/

/*!
  \enum SoGLRenderAction::TransparentDelayedObjectRenderType

  Enumerates the render types of transparent objects.
*/

/*!
  \var SoGLRenderAction::TransparentDelayedObjectRenderType SoGLRenderAction::ONE_PASS

  Normal one pass rendering. This might cause artifacts for non-solid objects.
*/

/*!
  \var SoGLRenderAction::TransparentDelayedObjectRenderType SoGLRenderAction::NONSOLID_SEPARATE_BACKFACE_PASS

  Non-solid objects are handled in an extra rendering pass. Backfacing
  polygons are rendered in the first pass, and the front facing in the
  second pass.
*/

// *************************************************************************
package jscenegraph.database.inventor.actions;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import jscenegraph.database.inventor.nodes.SoCamera;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.annex.profiler.SoProfiler;
import jscenegraph.coin3d.inventor.elements.SoDepthBufferElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.actions.SoGLRenderAction.SoGLRenderPassCB;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLRenderPassElement;
import jscenegraph.database.inventor.elements.SoGLUpdateAreaElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoCallbackList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Util;

import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLRenderActionP {

	  SoGLRenderAction action; //ptr
	  final SbViewportRegion viewport = new SbViewportRegion();
	  int numpasses;	  
	  boolean internal_multipass;
	  
	  boolean passupdate;
	  SoGLRenderPassCB passcallback; //ptr
	  Object passcallbackdata;	  
	  SoGLRenderAction.SoGLRenderAbortCB abortcallback;
	  Object abortcallbackdata;

	  int cachecontext; //!< GL cache context
	  int currentpass;
	  final SoPathList delayedpaths = new SoPathList();
	  
	  final SoPathList sorttranspobjpaths = new SoPathList();
	  final SbList<Float> sorttranspobjdistances = new SbList<>();
	  
	  SoGetBoundingBoxAction bboxaction;
	  final SbVec2f updateorigin = new SbVec2f(), updatesize = new SbVec2f();	  
	  boolean needglinit;
	  boolean isrendering;
	  boolean isrenderingoverlay;	  
	  boolean transpobjdepthwrite;
	  final SoCallbackList precblist = new SoCallbackList();
	  
	  SoGLRenderAction.TransparencyType transparencytype;
	  boolean smoothing;
	  boolean delayedpathrender;
	  boolean transparencyrender;
	  final SoPathList transpobjpaths = new SoPathList();
	  SoGLRenderAction.TransparentDelayedObjectRenderType transpdelayedrendertype;
	  boolean renderingtranspbackfaces;
	  
	  enum Rendering { RENDERING_UNSET, RENDERING_SET_DIRECT, RENDERING_SET_INDIRECT };
	  Rendering rendering;	  
	  int sortedlayersblendpasses;	  
	  
	  SoNode cachedprofilingsg; // ptr

	  final int[] depthtextureid = new int[1];
	  final int[] hilotextureid = new int[1];
	  int[] rgbatextureids;
	  
	  final int[] sortedlayersblendprogramid = new int[1];
	  short viewportheight;
	  short viewportwidth;
	  boolean sortedlayersblendinitialized;
	  final SbMatrix sortedlayersblendprojectionmatrix = new SbMatrix();
	  int sortedlayersblendcounter;
	  boolean usenvidiaregistercombiners = false;
	  

String sortedlayersblendprogram =
"!!ARBfp1.0\n"+
"OPTION ARB_precision_hint_nicest;\n"+
"TEMP tmp;\n"+
"PARAM c0 = {0, 1, 0.0040000002, 0};\n"+ // 0.004 = precision delta value for float division
"TEMP R0;\n"+
"TEMP R1;\n"+
"TEMP H0;\n"+
"TXP R0.x, fragment.texcoord[3], texture[3], RECT;\n"+
"RCP R1.x, fragment.texcoord[3].w;\n"+
"MAD R0.x, fragment.texcoord[3].z, R1.x, -R0.x;\n"+
"ADD R0.x, c0.z, -R0.x;\n"+
"CMP H0.x, R0.x, c0.x, c0.y;\n"+
"MOV H0, -H0.x;\n"+
"KIL H0;\n"+
// -- Adding texture from unit 0 --
// FIXME: This is a hackish solution. Texture settings like
// GL_MODULATE, GL_LUMINANCE etc. are ignored. (20031215 handegar)
"TEX tmp, fragment.texcoord[0], texture[0], 2D;\n"+
"MOV tmp.a, 0;\n"+
"ADD result.color, fragment.color.primary, tmp;\n"+
"END";

	  

// Sort paths with transparent objects before rendering.
public void
doPathSort()
{
  // need to cast to SbPList to avoid ref/unref problems, since
  // operator[] is overloaded with non-virtual inheritance.
  SbPList plist = this.sorttranspobjpaths; //ptr
  Float[] darray = (Float[])(this.sorttranspobjdistances.getArrayPtr(new Float[this.sorttranspobjdistances.getLength()]));

  int i, j, distance, n = this.sorttranspobjdistances.getLength();
  Object ptmp;
  float dtmp;

  // shell sort algorithm (O(nlog(n))
  for (distance = 1; distance <= n/9; distance = 3*distance + 1) ;
  for (; distance > 0; distance /= 3) {
    for (i = distance; i < n; i++) {
      dtmp = darray[i];
      ptmp = plist.get(i);
      j = i;
      while (j >= distance && darray[j-distance] < dtmp) {
        darray[j] = darray[j-distance];
        plist.set(j, plist.get(j-distance));
        j -= distance;
      }
      darray[j] = dtmp;
      plist.set(j, ptmp);
    }
  }
}


	// *************************************************************************
	// methods in SoGLRenderActionP

	// Private function to save transparent paths that need to be sorted.
	// The transparent paths that don't need to be sorted are rendered
	// after the sorted ones.
	public void
	addSortTransPath(SoPath path)
	{
		//TODO
	}


	// Private function which "unwinds" the real value of the "rendering"
	// variable.
	public boolean
	isDirectRendering( SoState state) 
	{
	  boolean isdirect;
	  if (this.rendering == Rendering.RENDERING_UNSET) {
	    final cc_glglue w = SoGL.sogl_glue_instance(state);
	    isdirect = SoGL.cc_glglue_isdirect(w);
	  }
	  else {
	    isdirect = this.rendering == Rendering.RENDERING_SET_DIRECT;
	  }

	  // Update to keep in sync.
	  this.action.setRenderingIsRemote(!isdirect);

	  return isdirect;
	}

    static boolean first = true;
    static boolean first2 = true;
    
	//
	// render the scene. Called from beginTraversal()
	//
	public void
	render(SoNode node)
	{
	  this.isrendering = true;

	  SoState state = this.action.getState();
	  state.push();
	  
	  GL2 gl2 = state.getGL2();

	  SoShapeStyleElement.setTransparencyType(state,
	                                           this.transparencytype.getValue());

	  SoLazyElement.disableBlending(state);

	  SoViewportRegionElement.set(state, this.viewport);
	  SoDepthBufferElement.set(state, true, true,
	                            SoDepthBufferElement.DepthWriteFunction.LEQUAL,
	                            new SbVec2f(0.0f, 1.0f), false);
	  SoLazyElement.setTransparencyType(state,
	                                     (int)(this.transparencytype.getValue()));

	  if (this.transparencytype == SoGLRenderAction.TransparencyType.SORTED_LAYERS_BLEND) {
	    SoOverrideElement.setTransparencyTypeOverride(state, node, true);
	  }

	  SoLazyElement.setColorMaterial(state, true);

	  SoGLUpdateAreaElement.set(state,
	                             this.updateorigin, this.updatesize);

	  SoGLCacheContextElement.set(state, this.cachecontext,
	                               false, !this.isDirectRendering(state));
	  SoGLRenderPassElement.set(state, 0);

	  this.precblist.invokeCallbacks((Object)(this.action));

	  if (this.action.getNumPasses() > 1 && this.internal_multipass) {
	    // Check if the current OpenGL context has an accumulation buffer
	    // (rendering multiple passes doesn't make much sense otherwise).
	    final int[] accumbits = new int[1];
	    gl2.glGetIntegerv(GL2.GL_ACCUM_RED_BITS, accumbits);

	    if (accumbits[0] == 0) {
	      //static SbBool first = TRUE;
	      if (first) {
	        SoDebugError.postWarning("SoGLRenderActionP::render",
	                                  "Multipass rendering requested,\nbut current "+
	                                  "GL context has no accumulation buffer - "+
	                                  "falling back to single pass\nrendering.");
	        first = false;
	      }
	      this.renderSingle(node);
	    } else {
	      this.renderMulti(node);
	    }
	  } else {
	    this.renderSingle(node);
	  }

	  if (SoProfiler.isOverlayActive()) {
	    if (node == this.cachedprofilingsg) {
	      SoNode profileroverlay = SoActionP.getProfilerOverlay();
	      if (profileroverlay != null) {
	        this.isrenderingoverlay = true;
	        SoProfiler.enable(false);
	        this.renderSingle(profileroverlay);
	        SoProfiler.enable(true);
	        this.isrenderingoverlay = false;
	      }
	    } else {
	      if (first2) {
	        SoDebugError.postWarning("SoGLRenderAcionP::render",
	                                  "Profiling overlay is only enabled for the first "+
	                                  "scene graph in the viewer.");
	        first2 = false;
	      }
	    }
	  }

	  state.pop();
	  this.isrendering = false;
	}

	float sinc(float x) {
	    if ( x == 0 ) {
	        return 1f;
        }
	    return (float)Math.sin(x)/x;
    }

	//
	// render multiple passes (antialiasing)
	//
	public void
	renderMulti(SoNode node)
	{
	  assert(this.numpasses > 1);
	  float fraction = 1.0f / (float)(this.numpasses);

	  int storedpass = this.currentpass;

	  this.currentpass = 0;
	  this.renderSingle(node);
	  if (this.action.hasTerminated()) return;

	  SoState state = this.action.getState();
	  
	  GL2 gl2 = state.getGL2();
	  
	  gl2.glAccum(GL2.GL_LOAD, fraction);

	  for (int i = 1; i < this.numpasses; i++) {
	    if (this.passupdate) {
	      gl2.glAccum(GL2.GL_RETURN, (float)(this.numpasses) / (float)(i));
	    }
	    if (this.passcallback != null) this.passcallback.run(gl2,this.passcallbackdata);
	    else gl2.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
	    this.currentpass = i;
	    this.renderSingle(node);

	    if (this.action.hasTerminated()) {
	      this.currentpass = storedpass;
	      return;
	    }
	    gl2.glAccum(GL2.GL_ACCUM, fraction);
	  }
	  this.currentpass = storedpass;
	  gl2.glAccum(GL2.GL_RETURN, 1.0f);

//	  if(this.numpasses < 2) {
//	      return;
//      }
//
//	  int width = viewport.getViewportSizePixels().getX();
//	  int height = viewport.getViewportSizePixels().getY();
//
//	  if ( width < 3 || height < 3) {
//	      return;
//      }
//
//	  ByteBuffer image = BufferUtils.createByteBuffer(3*3*Float.BYTES);
//
//        final SbVec2f samplePoint = new SbVec2f();
//
//        FloatBuffer floatBuffer = image.asFloatBuffer();
//        int index;
//
//        float[] x = new float[3];
//        float[] y = new float[3];
//
//	  for( int pass=0;pass<this.numpasses;pass++) {
//          SoCamera.getJitterSample(this.numpasses, pass,samplePoint);
//          float dx = samplePoint.getX();
//          float dy = samplePoint.getY();
//          for( int i= -1; i<=1; i++) {
//              x[i+1] += sinc((i+dx)*(float)Math.PI);
//          }
//          for( int j = -1; j<=1;j++) {
//              y[j+1] += sinc((j+dy)*(float)Math.PI);
//          }
//      }
//
//	  x[0] = x[2] = -0.2f;
//	  x[1] = 1.4f;
//
//        y[0] = y[2] = -0.2f;
//        y[1] = 1.4f;
//
//        for( int i= -1; i<=1; i++) {
//            for (int j = -1; j <= 1; j++) {
//                float value = x[i+1]*y[j+1];
//                index = i + 1 + (j + 1) * 3;
//                floatBuffer.put(index,value);
//            }
//        }
//
//    for(index=0;index<9;index++) {
//        float value = floatBuffer.get(index)/*/this.numpasses/this.numpasses*/;
//        floatBuffer.put(index, value);
//    }
//
//	  gl2.glEnable(GL2.GL_CONVOLUTION_2D);
//	  gl2.glConvolutionFilter2D( GL2.GL_CONVOLUTION_2D,
//
//            GL2.GL_RGB,
//
//            3,
//
//            3,
//
//            GL2.GL_LUMINANCE,
//
//            GL2.GL_FLOAT,
//
//            image );
////
////      //gl2.glRasterPos2i(width/2-100, height/2-100);
////	  //gl2.glCopyPixels(0,0,width/2,height/2,GL2.GL_COLOR);
////
////        //gl2.glPixelZoom(2.0f, 2.0f);
//        //gl2.glRasterPos2i(0, 0);
//        gl2.glRasterPos2f(-1f, -1f);
//
//        gl2.glReadBuffer(GL2.GL_FRONT);
//        gl2.glDrawBuffer(GL2.GL_FRONT);
//        //gl2.glClear(GL_DEPTH_BUFFER_BIT);
//        //gl2.glDisable(GL2.GL_DEPTH_TEST);
//
//        gl2.glCopyPixels(-1, -1, width, height, GL2.GL_COLOR);
//        gl2.glDisable(GL2.GL_CONVOLUTION_2D);
	}

	

//
// render a single pass. Might start a transparency or delayed pass
// though.
//
public void
renderSingle(SoNode node)
{
  SoState state = this.action.getState();
  
  GL2 gl2 = state.getGL2();

  SoGLRenderPassElement.set(state, this.currentpass);
  SoGLCacheContextElement.set(state, this.cachecontext,
                               false, !this.isDirectRendering(state));

  assert(this.delayedpathrender == false);
  assert(this.transparencyrender == false);

  // Truncate just in case
  this.sorttranspobjpaths.truncate(0);
  this.transpobjpaths.truncate(0);
  this.sorttranspobjdistances.truncate(0);
  this.delayedpaths.truncate(0);

  // Do order independent transparency rendering
  if (this.transparencytype == SoGLRenderAction.TransparencyType.SORTED_LAYERS_BLEND) {
    final int[] depthbits = new int[1], alphabits = new int[1];
    gl2.glGetIntegerv(GL2.GL_DEPTH_BITS, depthbits);
    gl2.glGetIntegerv(GL2.GL_ALPHA_BITS, alphabits);

    final cc_glglue w = SoGL.sogl_glue_instance(state);
    // FIXME: What should we do when >8bits per channel becomes normal? (20031125 handegar)
    if (SoGLDriverDatabase.isSupported(w, SoGLDriverDatabase.SO_GL_SORTED_LAYERS_BLEND) && (depthbits[0] >= 24) && (alphabits[0] == 8)) {
      doSortedLayersBlendRendering(state, node);
    }
    else {

      if (!SoGLDriverDatabase.isSupported(w, SoGLDriverDatabase.SO_GL_SORTED_LAYERS_BLEND))
        SoDebugError.postWarning("renderSingle", "Sorted layers blend cannot be enabled "+
                                  "due to missing OpenGL extensions. Rendering using "+
                                  "SORTED_OBJECTS_BLEND instead.");
      else
        SoDebugError.postWarning("renderSingle", "Sorted layers blend cannot be enabled if "+
                                  "ALPHA size != 8 (currently "+alphabits+") or DEPTH size < 24 "+
                                  "(currently "+depthbits+"). Rendering using SORTED_OBJECTS_BLEND instead."
                                  );

      // Do regular SORTED_OBJECT_BLEND if sorted layers blend is unsupported
      this.transparencytype = SoGLRenderAction.TransparencyType.SORTED_OBJECT_BLEND;
      render(node); // Render again using the fallback transparency type.
    }

    return;
  }

  this.action.beginTraversal(node);

  if ((this.transpobjpaths.getLength() != 0 || this.sorttranspobjpaths.getLength() != 0) &&
      !this.action.hasTerminated()) {

    this.transparencyrender = true;
    // disable writing into the z-buffer when rendering transparent
    // objects

    if (!this.transpobjdepthwrite) {
      SoDepthBufferElement.set(state, true, false,
                                SoDepthBufferElement.DepthWriteFunction.LEQUAL,
                                new SbVec2f(0.0f, 1.0f), false);
    }
    SoGLCacheContextElement.set(state, this.cachecontext,
                                 true, !this.isDirectRendering(state));

    int numtransppasses = 1;
    switch (this.transpdelayedrendertype) {
    default:
      break;
    case NONSOLID_SEPARATE_BACKFACE_PASS:
      numtransppasses = 2;
      break;
    }

    // All paths in the sorttranspobjpaths should be sorted
    // back-to-front and rendered
    this.doPathSort();
    int i;
    for (i = 0; i < this.sorttranspobjpaths.getLength(); i++) {
      for (int pass = 0; pass < numtransppasses; pass++) {
        if (numtransppasses == 2) {
          switch (pass) {
          case 0:
            gl2.glCullFace(GL2.GL_FRONT);
            this.renderingtranspbackfaces = true;
            break;
          case 1:
            gl2.glCullFace(GL2.GL_BACK);
            this.renderingtranspbackfaces = false;
            break;
          }
        }
        this.action.apply((SoPath)this.sorttranspobjpaths.get(i));
      }
    }

    for (int pass = 0; pass < numtransppasses; pass++) {
      if (numtransppasses == 2) {
        switch (pass) {
        case 0:
          gl2.glCullFace(GL2.GL_FRONT);
          this.renderingtranspbackfaces = true;
          break;
        case 1:
          gl2.glCullFace(GL2.GL_BACK);
          this.renderingtranspbackfaces = false;
          break;
        }
      }
      // Render all transparent paths that should not be sorted
      this.action.apply(this.transpobjpaths, true);
    }
    // enable writing again. FIXME: consider if it's ok to push/pop state instead
    if (!this.transpobjdepthwrite) {
      SoDepthBufferElement.set(state, true, true,
                                SoDepthBufferElement.DepthWriteFunction.LEQUAL,
                                new SbVec2f(0.0f, 1.0f), false);
    }
    this.transparencyrender = false;
  }

  if (this.delayedpaths.getLength() != 0 && !this.action.hasTerminated()) {
    this.delayedpathrender = true;
    this.action.apply(this.delayedpaths, true);
    this.delayedpathrender = false;
  }

  // truncate lists to unref paths.
  this.sorttranspobjpaths.truncate(0);
  this.transpobjpaths.truncate(0);
  this.sorttranspobjdistances.truncate(0);
  this.delayedpaths.truncate(0);

}

	
public void
setupBlending(SoState state, SoGLRenderAction.TransparencyType transptype)
{

  switch (transptype) {
  case /*SoGLRenderAction::*/BLEND:
  case /*SoGLRenderAction::*/DELAYED_BLEND:
  case /*SoGLRenderAction::*/SORTED_OBJECT_BLEND:
  case /*SoGLRenderAction::*/SORTED_OBJECT_SORTED_TRIANGLE_BLEND:
    SoLazyElement.enableBlending(state, GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
    break;
  case /*SoGLRenderAction::*/ADD:
  case /*SoGLRenderAction::*/DELAYED_ADD:
  case /*SoGLRenderAction::*/SORTED_OBJECT_ADD:
  case /*SoGLRenderAction::*/SORTED_OBJECT_SORTED_TRIANGLE_ADD:
    SoLazyElement.enableBlending(state, GL2.GL_SRC_ALPHA, GL2.GL_ONE);
    break;
  default:
    throw new IllegalArgumentException( "should not get here");
    //break;
  }
}
	  

// Remember a path containing a transparent object for later
// rendering. We know path == this->getCurPath() when we get here.
// This method is only used to add paths that are to be rendered after
// all transparent paths that need sorting have been rendered, so no
// need to calculate distances. Just add to list.
public void
addTransPath(SoPath path)
{
  this.transpobjpaths.append(path);
}


public void
doSortedLayersBlendRendering( SoState state, SoNode node)
{
	GL2 gl2 = state.getGL2();

  cc_glglue glue = SoGL.sogl_glue_instance(state);
  this.initSortedLayersBlendRendering(state);
  this.setupSortedLayersBlendTextures(state);
  this.sortedlayersblendinitialized = true;

  gl2.glDisable(GL2.GL_BLEND);

  // The 'sortedlayersblendcounter' must be global so that it can be
  // reached by 'setupNVRegisterCombiners()' at all times during the
  // scenegraph traversals.
  for(this.sortedlayersblendcounter=0;
      this.sortedlayersblendcounter < this.sortedlayersblendpasses;
      this.sortedlayersblendcounter++) {

    // FIXME: A trick here would be to do an occlusion test if
    // possible (EXT_occlusion_test is more or less free). The choosen
    // number of passes would then be treated as the the upper number
    // of passes. (20031208 handegar)
    renderOneBlendLayer(state, this.sortedlayersblendcounter > 0,
                        this.sortedlayersblendcounter < (this.sortedlayersblendpasses-1),
                        node);

  }

  // Blend together the aquired RGBA layers
  if (glue.has_arb_fragment_program && !this.usenvidiaregistercombiners)
    renderSortedLayersFP(state);
  else
    renderSortedLayersNV(state);

}

public void
texgenEnable(GL2 gl2, boolean enable)
{
    if (enable) {
        gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
        gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
        gl2.glEnable(GL2.GL_TEXTURE_GEN_R);
        gl2.glEnable(GL2.GL_TEXTURE_GEN_Q);
    }
    else {
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_S);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_T);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_R);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_Q);
    }
}


public void
eyeLinearTexgen()
{

  float col1[] = { 1, 0, 0, 0 };
  float col2[] = { 0, 1, 0, 0 };
  float col3[] = { 0, 0, 1, 0 };
  float col4[] = { 0, 0, 0, 1 };
  
  SoState state = action.getState();
  GL2 gl2 = state.getGL2();

  gl2.glTexGenfv(GL2.GL_S,GL2.GL_EYE_PLANE, col1);
  gl2.glTexGenfv(GL2.GL_T,GL2.GL_EYE_PLANE, col2);
  gl2.glTexGenfv(GL2.GL_R,GL2.GL_EYE_PLANE, col3);
  gl2.glTexGenfv(GL2.GL_Q,GL2.GL_EYE_PLANE, col4);

  gl2.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
  gl2.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
  gl2.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
  gl2.glTexGeni(GL2.GL_Q, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);

}


public void
renderOneBlendLayer( SoState state,
                                       boolean peel, boolean updatedepthtexture, SoNode node)
{
	GL2 gl2 = state.getGL2();

  final cc_glglue glue = SoGL.sogl_glue_instance(state);

  // Setup clearcolor alpha value to 1.0f when blending using NVIDIA
  // extensions. Must do this every time to make sure the alpha-value
  // stays correct.
  final float[] clearcolor = new float[4];
  gl2.glGetFloatv(GL.GL_COLOR_CLEAR_VALUE, clearcolor);
  if (glue.has_arb_fragment_program && !this.usenvidiaregistercombiners)
    gl2.glClearColor(clearcolor[0], clearcolor[1], clearcolor[2], 0.0f);
  else
	  gl2.glClearColor(clearcolor[0], clearcolor[1], clearcolor[2], 1.0f);
  gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);


  // Clear all errors before traversal, just in case.
  int glerror =  SoGL.sogl_glerror_debugging() ? gl2.glGetError() : GL2.GL_NO_ERROR;
  while (glerror != 0) {
    SoDebugError.postWarning("renderOneBlendLayer",
                              "glError() = "+glerror+"\n");
    glerror = gl2.glGetError();
  }

  // Do the rendering
  this.action.beginTraversal(node);

  if(peel) {
    if (glue.has_arb_fragment_program && !this.usenvidiaregistercombiners) {
      // Fragment program clean-up
      gl2.glDisable(GL2.GL_FRAGMENT_PROGRAM_ARB);
      gl2.glDisable(GL2.GL_TEXTURE_RECTANGLE);
      gl2.glDisable(GL2.GL_ALPHA_TEST);

      SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE3);
      gl2.glDisable(GL2.GL_TEXTURE_RECTANGLE);
      this.texgenEnable(gl2,false);

      gl2.glMatrixMode(GL2.GL_TEXTURE);
      gl2.glLoadIdentity();
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE0);
      gl2.glDisable(GL2.GL_TEXTURE_RECTANGLE);
      gl2.glDisable(GL2.GL_ALPHA_TEST);

    }
    else {
//      // Regular NViDIA register combiner clean-up
//      cc_glglue_glActiveTexture(glue, GL_TEXTURE3);
//      glDisable(GL_TEXTURE_RECTANGLE_EXT);
//      this.texgenEnable(FALSE);
//
//      glMatrixMode(GL_TEXTURE);
//      glLoadIdentity();
//      glMatrixMode(GL_MODELVIEW);
//      cc_glglue_glActiveTexture(glue, GL_TEXTURE0);
//      glDisable(GL2.GL_REGISTER_COMBINERS_NV);
//      glDisable(GL2.GL_ALPHA_TEST);

    }
  }

  if (!glue.has_arb_fragment_program || this.usenvidiaregistercombiners)
    gl2.glDisable(GL2.GL_TEXTURE_SHADER_NV);

  // FIXME: It might be a smart thing to use PBuffers for the RGBA
  // layers instead of copying from the framebuffer. The copying seems
  // to be a performance hit for large canvases. (20031127 handegar)

  // copy the RGBA of the layer to a texture
  gl2.glEnable(GL2.GL_TEXTURE_RECTANGLE);
  gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE, this.rgbatextureids[this.sortedlayersblendcounter]);
  gl2.glCopyTexSubImage2D(GL2.GL_TEXTURE_RECTANGLE, 0, 0, 0, 0, 0,
                      this.viewportwidth, this.viewportheight);

  if (updatedepthtexture) {
    gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE, this.depthtextureid[0]);
    gl2.glCopyTexSubImage2D(GL2.GL_TEXTURE_RECTANGLE, 0, 0, 0, 0, 0,
                        this.viewportwidth, this.viewportheight);
  }

}

public void
initSortedLayersBlendRendering( SoState state)
{
	GL2 gl2 = state.getGL2();

  if (this.sortedlayersblendinitialized) // Do this only once
    return;

  // Supporting both the TGS envvar and the COIN envvar. If both are
  // present, the COIN envvar will be used.
  String envtgs = TidBits.coin_getenv("OIV_NUM_SORTED_LAYERS_PASSES");
  if (envtgs != null && (Util.atoi(envtgs) > 0))
    this.sortedlayersblendpasses = Util.atoi(envtgs);

  String envcoin = TidBits.coin_getenv("COIN_NUM_SORTED_LAYERS_PASSES");
  if (envcoin != null && (Util.atoi(envcoin) > 0))
    this.sortedlayersblendpasses = Util.atoi(envcoin);

  String envusenvidiarc = TidBits.coin_getenv("COIN_SORTED_LAYERS_USE_NVIDIA_RC");
  if (envusenvidiarc != null && (Util.atoi(envusenvidiarc) > 0))
    ;//this.usenvidiaregistercombiners = true;

  this.rgbatextureids = new int[this.sortedlayersblendpasses];

  final cc_glglue glue = SoGL.sogl_glue_instance(state);
  if (glue.has_arb_fragment_program && !this.usenvidiaregistercombiners) {

    // Initialize fragment program
    //
    // FIXME: the program id must be bound to the current rendering
    // context, and deallocated when it is destructed. 20040718 mortene.
    glue.glGenProgramsARB(1, this.sortedlayersblendprogramid);
    glue.glBindProgramARB(GL2.GL_FRAGMENT_PROGRAM_ARB, this.sortedlayersblendprogramid[0]);
    glue.glProgramStringARB(GL2.GL_FRAGMENT_PROGRAM_ARB, GL2.GL_PROGRAM_FORMAT_ASCII_ARB,
                             sortedlayersblendprogram.length(),
                             sortedlayersblendprogram);

    // FIXME: Maybe a wrapper for catching fragment program errors
    // should be a part of GLUE... (20031204 handegar)
    final int[] errorPos = new int[1];
    int err = SoGL.sogl_glerror_debugging() ? gl2.glGetError() : GL2.GL_NO_ERROR;
    if (err != 0) {
      gl2.glGetIntegerv(GL2.GL_PROGRAM_ERROR_POSITION_ARB, errorPos);
      SoDebugError.postWarning("initSortedLayersBlendRendering",
                                "Error in fragment program! (byte pos: "+errorPos+") '"+GL11.glGetString(GL2.GL_PROGRAM_ERROR_STRING_ARB)+"'.\n"
                                );

    }

    gl2.glDisable(GL2.GL_FRAGMENT_PROGRAM_ARB);

  }

}


public void
setupFragmentProgram()
{

  if (this.sortedlayersblendcounter == 0)  // Is this not the first pass?
    return;
  final cc_glglue glue = SoGL.sogl_glue_instance(this.action.getState());

  SoState state = action.getState();
  GL2 gl2 = state.getGL2();

  gl2.glEnable(GL2.GL_FRAGMENT_PROGRAM_ARB);
  glue.glBindProgramARB(GL2.GL_FRAGMENT_PROGRAM_ARB, this.sortedlayersblendprogramid[0]);

  // UNIT #3
  gl2.glMatrixMode(GL2.GL_MODELVIEW);
  SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE3);

  gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE/*_NV*/, this.depthtextureid[0]);
  gl2.glEnable(GL2.GL_TEXTURE_RECTANGLE/*_NV*/);

  gl2.glPushMatrix();
  gl2.glLoadIdentity();
  this.eyeLinearTexgen();
  gl2.glPopMatrix();
  this.texgenEnable(gl2,true);

  gl2.glMatrixMode(GL2.GL_TEXTURE);
  gl2.glLoadIdentity();
  gl2.glScalef(this.viewportwidth, this.viewportheight, 1);
  gl2.glTranslatef(0.5f, 0.5f, 0.5f);
  gl2.glScalef(0.5f, 0.5f, 0.5f);
  gl2.glMultMatrixf((float [])(this.sortedlayersblendprojectionmatrix.getValueLinear()));
  gl2.glMatrixMode(GL2.GL_MODELVIEW);

  gl2.glAlphaFunc(GL2.GL_GREATER, 0);
  gl2.glEnable(GL2.GL_ALPHA_TEST);

  // UNIT #0
  SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE0);

}


public void
setupRegisterCombinersNV()
{
	//TODO
}

public void
setupSortedLayersBlendTextures( SoState state)
{
	GL2 gl2 = state.getGL2();

  final SbViewportRegion vpr = this.action.getViewportRegion(); //ref
  final SbVec2s canvassize = vpr.getViewportSizePixels(); //ref

  // Do we have to reinitialize the textures?
  if (((canvassize.getValue()[1] != this.viewportheight) ||
       (canvassize.getValue()[0] != this.viewportwidth)) ||
      !this.sortedlayersblendinitialized) {

    final cc_glglue glue = SoGL.sogl_glue_instance(state);


    if (this.sortedlayersblendinitialized) {
      // Remove the old textures to make room for new ones if size has changed.
      gl2.glDeleteTextures(/*1, &*/this.depthtextureid);
      gl2.glDeleteTextures(/*this.sortedlayersblendpasses,*/ this.rgbatextureids);
    }

    // Depth texture setup
    gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

    // FIXME: the texture id must be bound to the current rendering
    // context, and deallocated when it is destructed. 20040718 mortene.
    gl2.glGenTextures(1, this.depthtextureid);
    gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE, this.depthtextureid[0]);
    gl2.glTexImage2D(GL2.GL_TEXTURE_RECTANGLE, 0, GL2.GL_DEPTH_COMPONENT24, canvassize.getValue()[0], canvassize.getValue()[1],
                 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_INT, /*null*/0);
    gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
    gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
    gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

    if (glue.has_arb_fragment_program && !this.usenvidiaregistercombiners) {
      // Not disabled as default by NVIDIA when using fragment programs (according to spec.)
      gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_NONE);
    }
    else {
      gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
      gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_COMPARE_FUNC, GL2.GL_LEQUAL);
    }

    // The "register combiner"-way if explicitly choosen or FP is unavailable
    if(this.usenvidiaregistercombiners) {
      // HILO texture setup
      short HILOtexture[] = {0, 0};
      // FIXME: the texture id must be bound to the current rendering
      // context, and deallocated when it is destructed. 20040718 mortene.
      gl2.glGenTextures(1, this.hilotextureid);
      gl2.glBindTexture(GL2.GL_TEXTURE_2D, this.hilotextureid[0]);
      gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_HILO_NV, 1, 1, 0, GL2.GL_HILO_NV,
                   GL2.GL_UNSIGNED_SHORT, HILOtexture);
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
    }


    // RGBA layers setup
    // FIXME: What if channels are > 8 bits? This must be examined
    // closer... [Only highend ATI cards supports these resolutions if
    // I'm not mistaken.] (20031126 handegar)
    //
    // FIXME: the texture ids must be bound to the current rendering
    // context, and deallocated when it is destructed. 20040718 mortene.
    gl2.glGenTextures(this.sortedlayersblendpasses, this.rgbatextureids);
    for (int i=0;i<sortedlayersblendpasses;++i) {
      gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE, this.rgbatextureids[i]);
      gl2.glCopyTexImage2D(GL2.GL_TEXTURE_RECTANGLE, 0, GL2.GL_RGBA8, 0, 0, canvassize.getValue()[0], canvassize.getValue()[1], 0);
      gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
      gl2.glTexParameteri(GL2.GL_TEXTURE_RECTANGLE, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
    }

    this.viewportwidth = canvassize.getValue()[0];
    this.viewportheight = canvassize.getValue()[1];

  }

}

public void
renderSortedLayersFP( SoState state)
{
	GL2 gl2 = state.getGL2();

  final cc_glglue glue = SoGL.sogl_glue_instance(state);

  gl2.glMatrixMode(GL2.GL_PROJECTION);
  gl2.glLoadIdentity();
  gl2.glOrtho(0, this.viewportwidth, 0, this.viewportheight, -1, 1);
  gl2.glMatrixMode(GL2.GL_MODELVIEW);
  gl2.glLoadIdentity();

  gl2.glDisable(GL2.GL_DEPTH_TEST);
  gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);

  boolean cullface = gl2.glIsEnabled(GL2.GL_CULL_FACE);
  boolean lighting = gl2.glIsEnabled(GL2.GL_LIGHTING);

  gl2.glDisable(GL2.GL_CULL_FACE);
  gl2.glDisable(GL2.GL_FRAGMENT_PROGRAM_ARB);
  gl2.glDisable(GL2.GL_ALPHA_TEST);

  SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE0);

  gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

  gl2.glEnable(GL2.GL_BLEND);
  gl2.glDisable(GL2.GL_LIGHTING);
  gl2.glColor3f(1.0f,1.0f,1.0f);
  gl2.glEnable(GL2.GL_TEXTURE_RECTANGLE);

  for(int i=this.sortedlayersblendpasses-1;i>=0;--i) {
    gl2.glBindTexture(GL2.GL_TEXTURE_RECTANGLE, this.rgbatextureids[i]);
    gl2.glBegin(GL2.GL_QUADS);
    gl2.glTexCoord2f(0, 0);
    gl2.glVertex2f(0, 0);
    gl2.glTexCoord2f(0, this.viewportheight);
    gl2.glVertex2f(0, this.viewportheight);
    gl2.glTexCoord2f(this.viewportwidth, this.viewportheight);
    gl2.glVertex2f(this.viewportwidth, this.viewportheight);
    gl2.glTexCoord2f(this.viewportwidth, 0);
    gl2.glVertex2f(this.viewportwidth, 0);
    gl2.glEnd();
  }

  gl2.glDisable(GL2.GL_TEXTURE_RECTANGLE);

  gl2.glDisable(GL2.GL_BLEND);
  gl2.glEnable(GL2.GL_DEPTH_TEST);

  if (cullface)
    gl2.glEnable(GL2.GL_CULL_FACE);

  if (lighting)
    gl2.glEnable(GL2.GL_LIGHTING);

}

public void
renderSortedLayersNV( SoState state)
{
//
//  const cc_glglue * glue = sogl_glue_instance(state);
//
//  glMatrixMode(GL_PROJECTION);
//  glLoadIdentity();
//  glOrtho(0, this->viewportwidth, 0, this->viewportheight, -1, 1);
//  glMatrixMode(GL_MODELVIEW);
//  glLoadIdentity();
//
//  glDisable(GL_DEPTH_TEST);
//  glClear(GL_COLOR_BUFFER_BIT);
//
//
//  // Must make sure that the GL_CULL_FACE state is preserved if the scene
//  // contains both solid and non-solid shapes.
//  SbBool cullface = glIsEnabled(GL_CULL_FACE);
//  glDisable(GL_CULL_FACE);
//
//  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//  glEnable(GL_BLEND);
//
//  cc_glglue_glActiveTexture(glue, GL_TEXTURE0);
//
//  //
//  //  Register combiners 1.0 script:
//  //   !!RC1.0
//  //   rgb.out = tex0;
//  //   rgb.a = tex0;
//  //
//  glue->glCombinerParameteriNV(GL_NUM_GENERAL_COMBINERS_NV, 1);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_RGB, GL_VARIABLE_A_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_RGB, GL_VARIABLE_B_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_RGB, GL_VARIABLE_C_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_RGB, GL_VARIABLE_D_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glCombinerOutputNV(GL_COMBINER0_NV, GL_RGB, GL_DISCARD_NV, GL_DISCARD_NV,
//                           GL_DISCARD_NV, GL_ZERO, GL_ZERO, GL_FALSE, GL_FALSE, GL_FALSE);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_ALPHA, GL_VARIABLE_A_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_ALPHA);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_ALPHA, GL_VARIABLE_B_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_ALPHA);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_ALPHA, GL_VARIABLE_C_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_ALPHA);
//  glue->glCombinerInputNV(GL_COMBINER0_NV, GL_ALPHA, GL_VARIABLE_D_NV, GL_ZERO,
//                          GL_UNSIGNED_IDENTITY_NV, GL_ALPHA);
//  glue->glCombinerOutputNV(GL_COMBINER0_NV, GL_ALPHA, GL_DISCARD_NV, GL_DISCARD_NV,
//                           GL_DISCARD_NV, GL_ZERO, GL_ZERO, GL_FALSE, GL_FALSE, GL_FALSE);
//
//  glue->glCombinerParameteriNV(GL_COLOR_SUM_CLAMP_NV, 0);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_A_NV, GL_ZERO,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_B_NV, GL_ZERO,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_C_NV, GL_ZERO,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_D_NV, GL_TEXTURE0,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_E_NV, GL_ZERO,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_F_NV, GL_ZERO,
//                               GL_UNSIGNED_IDENTITY_NV, GL_RGB);
//  glue->glFinalCombinerInputNV(GL_VARIABLE_G_NV, GL_TEXTURE0,
//                               GL_UNSIGNED_IDENTITY_NV, GL_ALPHA);
//
//  glEnable(GL_REGISTER_COMBINERS_NV);
//  glEnable(GL_TEXTURE_RECTANGLE_EXT);
//
//  for(int i=this->sortedlayersblendpasses-1;i>=0;--i) {
//    glBindTexture(GL_TEXTURE_RECTANGLE_EXT, this->rgbatextureids[i]);
//    glBegin(GL_QUADS);
//    glTexCoord2f(0, 0);
//    glVertex2f(0, 0);
//    glTexCoord2f(0, this->viewportheight);
//    glVertex2f(0, this->viewportheight);
//    glTexCoord2f(this->viewportwidth, this->viewportheight);
//    glVertex2f(this->viewportwidth, this->viewportheight);
//    glTexCoord2f(this->viewportwidth, 0);
//    glVertex2f(this->viewportwidth, 0);
//    glEnd();
//  }
//
//  glDisable(GL_REGISTER_COMBINERS_NV);
//  glDisable(GL_TEXTURE_RECTANGLE_EXT);
//
//  glDisable(GL_BLEND);
//  glEnable(GL_DEPTH_TEST);
//
//  if (cullface)
//    glEnable(GL_CULL_FACE);
//
}


}
