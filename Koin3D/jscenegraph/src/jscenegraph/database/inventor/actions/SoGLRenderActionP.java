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

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.misc.SoCallbackList;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLRenderActionP {

	  SoGLRenderAction.SoGLRenderAbortCB abortcallback;
	  Object abortcallbackdata;
	  final SoCallbackList precblist = new SoCallbackList();
	  
	  SoGLRenderAction.TransparencyType transparencytype;
	  boolean smoothing;
	  boolean delayedpathrender;
	  boolean transparencyrender;
	  final SoPathList transpobjpaths = new SoPathList();
	  SoGLRenderAction.TransparentDelayedObjectRenderType transpdelayedrendertype;
	  boolean renderingtranspbackfaces;
	  boolean needglinit;
	  
	  final SbMatrix sortedlayersblendprojectionmatrix = new SbMatrix();

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


}
