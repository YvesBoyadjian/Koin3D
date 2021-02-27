/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoTextureCoordinateBundle class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.bundles;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement.SoTexCoordTexgenCB;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement.SoTextureCoordinateFunctionCB;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;


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
  \class SoTextureCoordinateBundle include/Inventor/bundles/SoTextureCoordinateBundle.h
  \brief The SoTextureCoordinateBundle class simplifies texture coordinate handling.
  \ingroup bundles

  It is unlikely that application programmers should need to know how
  to use this class, as it is mostly intended for internal use.
*/
// FIXME: document class better.

/////////////////////////////////////////////////////////////////////////////// MEVISLAB
///
////\class SoTextureCoordinateBundle
///
///  Bundle that allows shapes to deal with texture coordinates more
///  easily. This class provides a fairly simple interface to texture
///  coordinate handling, including default texture coordinate
///  generation. This can be used during either rendering or primitive
///  generation.
///
///  This class can be used during either rendering or primitive
///  generation. For primitive generation, there are two cases,
///  distinguished by the flag returned by isFunction(). If this
///  flag is TRUE, the texture coordinates are to be generated using
///  the get(point, normal) method, which uses a software texture
///  coordinate function. (This process is also used for texture
///  coordinates that are generated by default when necessary - in this
///  case, the function does a linear map across two sides of the
///  bounding box of the shape.) If the isFunction() flag is FALSE, the
///  coordinates are accessed directly from the element using the
///  get(index) method.
///
///  For GL rendering, there is an additional case. If
///  needCoordinates() returns FALSE, no texture coordinates need to be
///  sent at all, and the bundle does not have to be used for anything
///  else. Otherwise, send(index) should be used.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves boyadjian
 *
 */

public class SoTextureCoordinateBundle extends SoBundle implements Destroyable {


	private static final int FLAG_FUNCTION           =0x01;
	private static final int FLAG_NEEDCOORDS         =0x02;
	private static final int FLAG_DEFAULT            =0x04;
	private static final int FLAG_DIDPUSH            =0x08;
	private static final int FLAG_3DTEXTURES         =0x10;
	private static final int FLAG_DIDINITDEFAULT     =0x20;
	private static final int FLAG_NEEDINDICES        =0x40;

	/*!
	  Constructor with \a action being the action applied to the node.
	  The \a forRendering parameter must be \e TRUE if the bundle is to
	  be used for sending texture coordinates to GL during rendering.
	  The \a setUpDefault must be \e TRUE if default texture coordinates
	  should be generated.
	*/
	public SoTextureCoordinateBundle(SoAction action, //java port
            boolean forRendering) {
		this(action,forRendering,true);
	}
public SoTextureCoordinateBundle(SoAction action,
                            boolean forRendering,
                            boolean setUpDefault) {
	super(action);
	
	  this.flags = 0;
	  //
	  // return immediately if there is no texture
	  //
	  final int[] lastenabled = new int[1]; lastenabled[0] = -1;
	  boolean[] multienabled = 
	    SoMultiTextureEnabledElement.getEnabledUnits(this.state, lastenabled);
	  boolean needinit = lastenabled[0] >= 0;
	  boolean glrender = forRendering || action.isOfType(SoGLRenderAction.getClassTypeId());
	  boolean bumpenabled = glrender && /*(SoBumpMapElement.get(this.state) != null)*/false; //TODO

	  if (!needinit && multienabled == null && !bumpenabled) return;
	  
	  // It is safe to assume that shapenode is of type SoShape, so we
	  // cast to SoShape before doing any operations on the node.
	  this.shapenode = (SoShape)(action.getCurPathTail());
	  
	  this.coordElt = SoMultiTextureCoordinateElement.getInstance(this.state);
	  
	  if(this.coordElt == null) {
		  int i=0;
	  }
	  for (int i = 0; i <= lastenabled[0]; i++) {
	    if (multienabled[i]) {
	      switch (this.coordElt.getType(i)) {
	      case /*SoMultiTextureCoordinateElement::*/DEFAULT:
	        this.initDefault(action, i);
	        break;
	      case /*SoMultiTextureCoordinateElement::*/EXPLICIT:
	        this.flags |= FLAG_NEEDINDICES;
	        if (this.coordElt.getNum(i) > 0) {
	          this.flags |= FLAG_NEEDCOORDS;
	        }
	        else {
	          this.initDefault(action, i);
	        }
	        break;
	      case /*SoMultiTextureCoordinateElement::*/FUNCTION:
	        this.flags |= FLAG_FUNCTION;
	        this.flags |= FLAG_NEEDCOORDS; // not automatically generated
	        break;
	        
	      case /*SoMultiTextureCoordinateElement::*/NONE_TEXGEN:
	        // texcoord won't be needed. This will only happen during SoGLRenderAction,
	        // when GL generates texture coorinates. Therefore, we will not set
	        // the FLAG_NEEDCOORDS here.
	        this.flags |= FLAG_FUNCTION;
	        if (!forRendering) {
	          this.flags |= FLAG_NEEDCOORDS;
	        }
	        break;
	      default:
	        throw new IllegalStateException("unknown CoordType");
	        //break;
	      }
	    }
	  }
	  // refetch element in case we pushed
	  if ((this.flags & FLAG_DIDPUSH)!=0) {
	    this.coordElt = SoMultiTextureCoordinateElement.getInstance(this.state);
	  }
	  this.glElt = null;
	  if (glrender) {
	    boolean needindices = false;
	    if (!needindices && this.isFunction()) {
	      // check if bump mapping needs texture coordinate indices
	      if (bumpenabled &&
	          /*(SoBumpMapCoordinateElement.getInstance(state).getNum())*/false) { //TODO
	        needindices = true;
	      }
	    }
	    if (needindices && this.isFunction()) this.flags |= FLAG_NEEDINDICES;
	    this.glElt = (SoGLMultiTextureCoordinateElement)(this.coordElt);
	    this.glElt.initMulti(action.getState());
	  }
	  if ((this.flags & FLAG_DEFAULT)!=0 && !setUpDefault) {
	    // FIXME: I couldn't be bothered to support this yet. It is for picking
	    // optimization only, I think. pederb, 20000218
	  }
}

/*!
Destructor.
*/
  public void destructor() {	  
	  if ((this.flags & FLAG_DIDPUSH)!=0) this.state.pop();
	  super.destructor();
  }

  /*!
  Returns \e TRUE if texture coordinates is needed during rendering.
*/
  public boolean needCoordinates() {
	  return (this.flags & FLAG_NEEDCOORDS) != 0;
  }
  
  /*!
  Returns \e TRUE if a texture coordinate function should be used.
*/
  public boolean isFunction() {
	  return (this.flags & FLAG_FUNCTION) != 0;
  }

  /*!
  Returns the texture coordinates at index \a index.
  Should only be used if SoTextureCoordinateBundle::isFunction() is \a FALSE.
*/
  public SbVec4f /*&*/get(int index) {
	  assert(coordElt!=null && (this.flags & FLAG_FUNCTION)==0);
	  return coordElt.get4(index);
  }
  
  /*!
  Returns the texture coordinates based on \a point and \a normal.
  Should only be used if SoTextureCoordinateBundle::isFunction() is \a TRUE.
*/
  public SbVec4f /*&*/get(final SbVec3f point, final SbVec3f normal) {
	  assert(this.coordElt != null && (this.flags & FLAG_FUNCTION)!=0);
	  if ((this.flags & FLAG_DEFAULT)!=0) {
	    final SbVec3f pt = new SbVec3f();
	    if ((this.flags & FLAG_3DTEXTURES)!=0) {
	      pt.copyFrom( point.operator_minus(this.defaultorigo));
	      this.dummyInstance.getRef()[2].accept( pt.getValueRead()[2]/this.defaultsize.getValueRead()[2]);
	    }
	    else {
	      pt.setValue(point.getValueRead()[this.defaultdim0]-this.defaultorigo.getValueRead()[0],
	                  point.getValueRead()[this.defaultdim1]-this.defaultorigo.getValueRead()[1],
	                  0.0f);
	    }
	    this.dummyInstance.getRef()[0].accept( pt.getValueRead()[0]/this.defaultsize.getValueRead()[0]);
	    this.dummyInstance.getRef()[1].accept( pt.getValueRead()[1]/this.defaultsize.getValueRead()[1]);
	    return this.dummyInstance;
	  }
	  else {
	    return coordElt.get(point, normal);
	  }
  }

  /*!
  \fn void SoTextureCoordinateBundle::send(const int index) const
  Send texture coordinates to GL. Should only be used if
  SoTextureCoordinateBundle::isFunction() is \a FALSE.
*/

  public void send( int index) {
    glElt.send(index);
  }

  /*!
  \fn void SoTextureCoordinateBundle::send(const int index, const SbVec3f &point, const SbVec3f &normal) const
  Convenience function that will make it transparent to the rendering
  code if ordinary texture coordinates or function texture coordinates
  are used.
*/

  public void send( int index, final SbVec3f point,
            final SbVec3f normal) {
    glElt.send(index, point, normal);
  }

  /*!

  Returns \e TRUE if isFunction() is \e TRUE, but the texture
  coordinate indices are needed either by bump mapping or by one of
  the other texture units.

  \since Coin 2.2
*/
  public boolean needIndices() {
	  return (this.flags & FLAG_NEEDINDICES) != 0;
  }

  private SoMultiTextureCoordinateElement coordElt; //ptr
  private SoGLMultiTextureCoordinateElement glElt; //ptr
  private int flags;

  // misc stuff for default texture coordinate mappping
  //
// callback for default texture coordinates (for texture unit 0)
//
  private static SbVec4f /*&*/ defaultCB(Object userdata,
                                   final SbVec3f point,
                                   final SbVec3f normal) {
	  
	  return ((SoTextureCoordinateBundle)userdata).get(point, normal);
  }
  
  //
// callback for default texture coordinates (for texture units > 0)
//
  private static SbVec4f /*&*/ defaultCBMulti(Object userdata,
                                        final SbVec3f point,
                                        final SbVec3f normal) {
	  
	  SoTextureCoordinateBundle thisp = (SoTextureCoordinateBundle)(userdata);

	  final SbVec3fSingle pt = new SbVec3fSingle();
	  if ((thisp.flags & FLAG_3DTEXTURES)!=0) {
	    pt.copyFrom(point.operator_minus(thisp.defaultorigo));
	    thisp.dummyInstance.getValue()[2] = pt.getCoord(2)/thisp.defaultsize.getCoord(2);
	  }
	  else {
	    pt.setValue(point.getCoord(thisp.defaultdim0)-thisp.defaultorigo.getCoord(0),
	                point.getCoord(thisp.defaultdim1)-thisp.defaultorigo.getCoord(1),
	                0.0f);
	  }
	  thisp.dummyInstance.getValue()[0] = pt.getCoord(0)/thisp.defaultsize.getCoord(0);
	  thisp.dummyInstance.getValue()[1] = pt.getCoord(1)/thisp.defaultsize.getCoord(1);
	  return thisp.dummyInstance;

  }
  
  private SoShape shapenode; //ptr
  private final SbVec3f defaultorigo = new SbVec3f();
  private final SbVec3f defaultsize = new SbVec3f();
  private final SbVec4fSingle dummyInstance = new SbVec4fSingle();
  private int defaultdim0, defaultdim1;
  
  //
// Set up stuff needed for default texture coordinate mapping callback
//
  private void initDefaultCallback(SoAction action) {
	  
	  this.flags |= FLAG_DIDINITDEFAULT;
	  //
	  // calculate needed stuff for default mapping
	  //
	  final SbBox3f box = new SbBox3f();
	  final SbVec3f center = new SbVec3f();
	  // this could be very slow, but if you're looking for speed, default
	  // texture coordinate mapping shouldn't be used. We might optimize this
	  // by using a SoTextureCoordinateCache soon though. pederb, 20000218

	  SoShape shape = (SoShape)(this.shapenode);
	  SoBoundingBoxCache bboxcache = shape.getBoundingBoxCache();
	  if (bboxcache != null && bboxcache.isValid(action.getState())) {
	    box.copyFrom(bboxcache.getProjectedBox());
	    if (bboxcache.isCenterSet()) center.copyFrom(bboxcache.getCenter());
	    else center.copyFrom(box.getCenter());
	  }
	  else {
	    shape.computeBBox(action, box, center);
	  }

	  // just use som default values if the shape bbox is empty
	  final SbVec3f size = new SbVec3f(1.0f, 1.0f, 1.0f);
	  final SbVec3f origo = new SbVec3f(0.f, 0.0f, 0.0f);
	  if (!box.isEmpty()) {
	    box.getSize(size.getRef()[0], size.getRef()[1], size.getRef()[2]);
	    origo.copyFrom(box.getMin());
	  }

	  // Map S,T,R to X,Y,Z for 3D texturing
	  if (SoMultiTextureEnabledElement.getMode(this.state, 0) == 
	      SoMultiTextureEnabledElement.Mode.TEXTURE3D) {
	    this.flags |= FLAG_3DTEXTURES;
	    this.defaultdim0 = 0;
	    this.defaultdim1 = 1;

	    this.defaultorigo.getRef()[2].accept(origo.getValueRead()[2]);
	    this.defaultsize.getRef()[2].accept(size.getValueRead()[2]);
	  }
	  else { // 2D textures
	    this.defaultsize.getRef()[2].accept(1.0f);
	    this.flags &= ~FLAG_3DTEXTURES;
	    // find the two biggest dimensions
	    int smallest = 0;
	    float smallval = size.getValueRead()[0];
	    if (size.getValueRead()[1] < smallval) {
	      smallest = 1;
	      smallval = size.getValueRead()[1];
	    }
	    if (size.getValueRead()[2] < smallval) {
	      smallest = 2;
	    }

	    this.defaultdim0 = (smallest + 1) % 3;
	    this.defaultdim1 = (smallest + 2) % 3;

	    if (size.getValueRead()[this.defaultdim0] == size.getValueRead()[this.defaultdim1]) {
	      // FIXME: this is probably an OIV bug. The OIV man pages are not
	      // clear on this point (surprise), but the VRML specification states
	      // that if the two dimensions are equal, the ordering X>Y>Z should
	      // be used.
//	#if 0 // the correct way to do it
//	      if (this.defaultdim0 > this.defaultdim1) {
//	        SbSwap(this.defaultdim0, this.defaultdim1);
//	      }
//	#else // the OIV way to do it.
	      if (this.defaultdim0 < this.defaultdim1) {
	        //SbSwap(this.defaultdim0, this.defaultdim1);
	    	int dummy = this.defaultdim0;
	    	this.defaultdim0 = this.defaultdim1;
	    	this.defaultdim1 = dummy;
	      }
//	#endif // OIV compatibility fix
	    }
	    else if (size.getValueRead()[this.defaultdim0] < size.getValueRead()[this.defaultdim1]) {
	      //SbSwap(this.defaultdim0, this.defaultdim1);
	    	int dummy = this.defaultdim0;
	    	this.defaultdim0 = this.defaultdim1;
	    	this.defaultdim1 = dummy;
	    }
	  }

	  this.defaultorigo.getRef()[0].accept( origo.getValueRead()[this.defaultdim0]);
	  this.defaultorigo.getRef()[1].accept( origo.getValueRead()[this.defaultdim1]);
	  this.defaultsize.getRef()[0].accept( size.getValueRead()[this.defaultdim0]);
	  this.defaultsize.getRef()[1].accept( size.getValueRead()[this.defaultdim1]);

	  // if bbox is empty in one dimension we just want to set it to
	  // 0.0. The size should be set to 1.0 to avoid division by zero.
	  for (int i = 0; i < 3; i++) {
	    if (this.defaultsize.getValueRead()[i] <= 0.0f) {
	      this.defaultsize.getRef()[i].accept(1.0f);
	    }
	  }

	  this.dummyInstance.getRef()[2].accept(0.0f);
	  this.dummyInstance.getRef()[3].accept(1.0f);

	  assert(this.defaultsize.getValueRead()[0] > 0.0f);
	  assert(this.defaultsize.getValueRead()[1] > 0.0f);
  }
  
  //
//initialize default texture coordinates for unit
//
  private void initDefault(SoAction action, int unit) {
  this.flags |= FLAG_NEEDCOORDS;
  this.flags |= FLAG_DEFAULT;
  this.flags |= FLAG_FUNCTION;

  if ((this.flags & FLAG_DIDPUSH)==0) {
    this.state.push();
    this.flags |= FLAG_DIDPUSH;
  }
  SoMultiTextureCoordinateElement.setFunction(this.state, this.shapenode, unit,
                                               SoTextureCoordinateBundle::defaultCBMulti,
                                               this);
  if ((this.flags & FLAG_DIDINITDEFAULT)==0) {
    this.initDefaultCallback(action);
  }

  }
};
