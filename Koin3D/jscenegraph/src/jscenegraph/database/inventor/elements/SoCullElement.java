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

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Array;

/*!
  \class SoCullElement SoCullElement.h Inventor/elements/SoCullElement.h
  \brief The SoCullElement class is used internally for render and pick culling.
  \ingroup elements

  The element holds all planes the geometry should be inside, and
  keeps a bitflag to signal which planes need to be tested.

  This element is an extension for Coin, and is not available in the
  original Open Inventor.

  The maximum number of planes in this element is 32, which should be
  more than enough, since the view frustum is represented by 6 planes,
  and the maximum number of OpenGL clipping planes is typically 6 or
  8.

  This element is designed for fast culling, and will not do optimal
  view frustum culling; a box might not be culled even though it is
  outside the view frustum. The assumption is that the view frustum is
  small compared to the world model. The element simply records all
  planes to be culled against, and the graph is not culled until it is
  completely outside one of the planes.

  SoCullElement is not active for other actions than SoGLRenderAction.
  It's possible to enable it for SoCallbackAction by updating it in
  a post camera callback though. Do something like this:

  \verbatim

  static SoCallbackAction::Response
  camera_cb(void * data, SoCallbackAction * action, const SoNode * node)
  {
    SoState * state = action.getState();
    SoCullElement::setViewVolume(state, SoViewVolumeElement::get(state));
    return SoCallbackAction::CONTINUE;
  }

  [...]
  SoCallbackAction cba(myviewport);
  cba.addPostCallback(SoCamera::getClassTypeId(), camera_cb, NULL);

  \endverbatim

  When the view volume is set in SoCullElement in the post camera
  callback, SoCallbackAction will perform culling on Separators and
  other nodes in the same way as SoGLRenderAction.

*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoCullElement extends SoElement {
	
	private static final int MAXPLANES = 32;
	
	private final Array<SbPlane> plane = new Array<>(SbPlane.class,new SbPlane[MAXPLANES]);
	
	private int numplanes;
	
	private int flags;
	
	private int vvindex;
	
	public SoCullElement() {
		super();
	}
	

	// doc from parent
	public void
	init(SoState /** COIN_UNUSED_ARG(*/state/*)*/)
	{
	  this.numplanes = 0;
	  this.flags = 0;
	  this.vvindex = -1;
	}

	// doc from parent
	public void
	push(SoState /** COIN_UNUSED_ARG(*/state/*)*/)
	{
	  final SoCullElement prev = ( SoCullElement )
	    (
	     this.getNextInStack()
	     );

	  this.flags = prev.flags;
	  this.numplanes = prev.numplanes;
	  this.vvindex = prev.vvindex;
	  for (int i = 0; i < prev.numplanes; i++) this.plane.set(i, prev.plane.get(i));
	}

	/*!
	  Sets the current view volume. In effect, this adds six planes to
	  the list of culling planes.  If a view volume has already been
	  set, the old view volume planes are overwritten by the new ones.
	  The view volume must be in the world coordinate systems.
	*/
	public static void
	setViewVolume(SoState state, final SbViewVolume vv)
	{
	  SoCullElement elem = (SoCullElement)
	    (
	     SoElement.getElement(state, classStackIndexMap.get(SoCullElement.class))
	     );
	  if (elem != null) {
	    if (elem.numplanes + 6 > SoCullElement.MAXPLANES) { // _very_ unlikely
	//#if COIN_DEBUG
	      SoDebugError.postWarning("SoCullElement::setViewVolume",  "too many planes");
	//#endif // COIN_DEBUG
	      return;
	    }
	    int i;
	    final Array<SbPlane> vvplane = new Array<>(SbPlane.class,new SbPlane[6]);
	    vv.getViewVolumePlanes(vvplane);
	    if (elem.vvindex >= 0) { // overwrite old view volume
	      for (i = 0; i < 6; i++) {
	        elem.plane.set(elem.vvindex+i, vvplane.get(i));
	        elem.flags &= ~(1<<(elem.vvindex+i));
	      }
	    }
	    else {
	      elem.vvindex = elem.numplanes;
	      for (i = 0; i < 6; i++) elem.plane.set(elem.numplanes++, vvplane.get(i));
	    }
	  }
	}

	/*!
	  Add plane geometry must be inside. The plane must be in the world
	  coordinate system.
	*/
	public void
	addPlane(SoState state, final SbPlane newplane)
	{
	  SoCullElement elem =
	    (SoCullElement)
	    (
	     SoElement.getElement(state, classStackIndexMap.get(SoCullElement.class))
	     );
	  if (elem != null) {
	    if (elem.numplanes >= SoCullElement.MAXPLANES) {  // _very_ unlikely
	//#if COIN_DEBUG
	      SoDebugError.postWarning("SoCullElement::addPlane",  "too many planes");
	//#endif // COIN_DEBUG
	      return;
	    }
	    elem.plane.set(elem.numplanes++, newplane);
	  }
	}

	/*!
	  Cull against \a box. If \a transform is \c true, the box is assumed
	  to be in object space, and will be transformed into world space
	  using the model matrix.  Returns \c true if box is outside one of
	  the planes, and updates the element to detect when geometry is
	  completely inside all planes.
	*/
	public boolean
	cullBox(SoState state, final SbBox3f box, final boolean transform)
	{
	  return SoCullElement.docull(state, box, transform, true);
	}

	/*!
	  Cull against \a box. If \a transform is \c true, the box is
	  assumed to be in object space, and will be transformed into world
	  space using the model matrix.  Returns \c true if box is outside one
	  of the planes. This method will not update the element state, just
	  perform a cull test against active planes.
	*/
	public static boolean
	cullTest(SoState state, final SbBox3f box, final boolean transform)
	{
	  return SoCullElement.docull(state, box, transform, false);
	}

	/*!
	  Returns \c true if the current geometry is completely inside all
	  planes. There is no need to do a cull test if this is the case.
	*/
	public boolean
	completelyInside(SoState state)
	{
	  // use SoState::getConstElement() to avoid cache dependency on this element
	  final SoCullElement  elem = (SoCullElement)
	    (
	     state.getConstElement(classStackIndexMap.get(SoCullElement.class))
	     );
	  int mask = 0x0001 << elem.numplanes;
	  return elem.flags == (mask-1);
	}

	// Documented in superclass. Overridden to assert that this method is
	// not called for this element.
	public boolean
	matches(SoElement elt) 
	{
	  assert(false);// "should not get here");
	  return false;
	}

	// Documented in superclass. Overridden to assert that this method is
	// not called for this element.
	public SoElement 
	copyMatchInfo()
	{
	  assert(false);// && "should not get here");
	  return null;
	}

	//
	// private method which does the actual culling
	//
	public static boolean
	docull(SoState state, final SbBox3f box, final boolean transform,
	                      final boolean updateelem)
	{
	  // try to avoid a push if possible
	  SoCullElement elem = (SoCullElement)
	    (
	     state.getElementNoPush(classStackIndexMap.get(SoCullElement.class))
	    );

	  if (elem == null) return false;

	  int i, j;
	  final SbVec3f min = new SbVec3f(), max = new SbVec3f();
	  min.copyFrom( box.getMin());
	  max.copyFrom( box.getMax());
	  final SbVec3f[] pts = new SbVec3f[8]; for ( i=0;i<8;i++) {pts[i] = new SbVec3f();}

	  final SbMatrix mm = new SbMatrix();
	  boolean identity = ! transform;
	  if (transform) {
	    boolean wasopen = state.isCacheOpen();
	    // close the cache, since we don't create a cache dependency on
	    // the model matrix element
	    state.setCacheOpen(false);
	    mm.copyFrom(SoModelMatrixElement.get(state));
	    state.setCacheOpen(wasopen);
	  }

	  // create the 8 box corner points
	  for (i = 0; i < 8; i++) {
	    pts[i].setValue(0, (i & 1) !=0 ? min.getValueRead()[0] : max.getValueRead()[0]);
	    pts[i].setValue(1, (i & 2) !=0 ? min.getValueRead()[1] : max.getValueRead()[1]);
	    pts[i].setValue(2, (i & 4) !=0 ? min.getValueRead()[2] : max.getValueRead()[2]);
	    if (!identity) mm.multVecMatrix(pts[i], pts[i]);
	  }

	  final int n = elem.numplanes;
	  int flags = elem.flags;
	  final Array<SbPlane> planes = elem.plane;
	  int mask = 0x0001;

	  for (i = 0; i < n; i++, mask<<=1) {
	    if ((flags & mask) == 0) {
	      int in = 0;
	      int out = 0;
	      for (j = 0; j < 8; j++) {
	        if (planes.get(i).isInHalfSpace(pts[j])) in++;
	        else out++;
	      }
	      if (in == 8) {
	        flags |= mask;
	      }
	      else if (out == 8) {
	        return true;
	      }
	    }
	  }
	  if (updateelem && (flags != elem.flags)) {
	    // force a push if necessary
	    /*SoCullElement*/ elem = (SoCullElement)
	      (
	       SoElement.getElement(state, classStackIndexMap.get(SoCullElement.class))
	       );
	    elem.flags = flags;
	  }
	  return false;
	}
	
}
