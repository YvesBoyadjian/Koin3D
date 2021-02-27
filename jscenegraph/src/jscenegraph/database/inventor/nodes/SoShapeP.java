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

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

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
	  //public SoPrimitiveVertexCache pvcache; //TODO
	  public int flags;
	  // stores the number of frames rendered with no node changes
	  public int rendercnt/* : RENDERCNT_BITS*/;

	  public void lock() { }
	  public void unlock() { }
	@Override
	public void destructor() {
	    if (this.bboxcache != null) { this.bboxcache.unref(); }
//	    if (this.pvcache != null) { this.pvcache.unref(); } FIXME
//	    Destroyable.delete( this.bumprender);
	}

public static void calibrateBBoxCache()
{
  int i;
  int ARRAYSIZE = 100;

  // just create 100 random vertices
  final SbVec3fArray vecarray = new SbVec3fArray(FloatMemoryBuffer.allocateFloats(ARRAYSIZE*3));
  for (i = 0; i < ARRAYSIZE; i++) {	  
      vecarray.getO(i).setX( ((float) Math.random()) /*/ ((float) RAND_MAX)*/ );
      vecarray.getO(i).setY( ((float) Math.random()) /*/ ((float) RAND_MAX)*/ );
      vecarray.getO(i).setZ( ((float) Math.random()) /*/ ((float) RAND_MAX)*/ );
  }

  // FIXME: should really measure CPU time spent, and not just wall
  // time. See the item in Coin/docs/todo.txt on implementing a
  // "stopwatch" ADT. 20021111 mortene.
  final SbTime begin = new SbTime(SbTime.getTimeOfDay());
  final SbBox3f bbox = new SbBox3f();
  bbox.makeEmpty();
  for (i = 0; i < ARRAYSIZE; i++) {
    bbox.extendBy(vecarray.getO(i));
  }
  final SbTime end = new SbTime(SbTime.getTimeOfDay());
  SoShapeP.bboxcachetimelimit = end.getValue() - begin.getValue();
}
}
