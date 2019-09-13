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
  \class SoShape SoShape.h Inventor/nodes/SoShape.h
  \brief The SoShape class is the superclass for geometry shapes.
  \ingroup nodes

  The node types which have actual geometry to render inherits this
  class. For convenience, the SoShape class contains various common
  code used by the subclasses.
*/

// *************************************************************************


// *************************************************************************

/*!
  \enum SoShape::TriangleShape
  \COININTERNAL
*/

/*!
  \fn void SoShape::computeBBox(SoAction * action, SbBox3f & box, SbVec3f & center)

  Implemented by SoShape subclasses to let the SoShape superclass know
  the exact size and weighted center point of the shape's bounding box.

  The bounding box and center point should be calculated and returned
  in the local coordinate system.

  The method implements action behavior for shape nodes for
  SoGetBoundingBoxAction. It is invoked from
  SoShape::getBoundingBox(). (Subclasses should \e not override
  SoNode::getBoundingBox().)

  The \a box parameter sent in is guaranteed to be an empty box, while
  \a center is undefined upon function entry.
*/

/*!
  \fn void SoShape::generatePrimitives(SoAction * action)

  The method implements action behavior for shape nodes for
  SoCallbackAction. It is invoked from
  SoShape::callback(). (Subclasses should \e not override
  SoNode::callback().)

  The subclass implementations uses the convenience methods
  SoShape::beginShape(), SoShape::shapeVertex(), and
  SoShape::endShape(), with SoDetail instances, to pass the primitives
  making up the shape back to the caller.
*/

// *************************************************************************

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShapeP implements Destroyable {

	  public enum Flags {
		    SHOULD_BBOX_CACHE ( 0x1),
		    NEED_SETUP_SHAPE_HINTS ( 0x2),
		    DISABLE_VERTEX_ARRAY_CACHE ( 0x4);
		    private int value;
		    
		    Flags(int value) {
		    	this.value = value;
		    }
		    public int getValue() {
		    	return value;
		    }
		  };
		  
		  public static double bboxcachetimelimit;
	  public SoBoundingBoxCache bboxcache; //TODO
	  public int flags;

	  public void lock() { }
	  public void unlock() { }
	@Override
	public void destructor() {
	    if (this.bboxcache != null) { this.bboxcache.unref(); }
//	    if (this.pvcache != null) { this.pvcache.unref(); } FIXME
//	    Destroyable.delete( this.bumprender);
	}
}
