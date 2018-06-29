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
 |      This file defines the SoGLShapeHintsElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLShapeHintsElement
///  \ingroup Elements
///
///  Element that stores current shape hints and sends commands to GL
///  based on them. All three hints must be set at the same time; to
///  leave any hint as is, use the "AS_IS" enum value.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLShapeHintsElement extends SoShapeHintsElement {

  private
    //! We save the state to figure out if the hints were
    //! copied from a parent element; if they were, then caches will have
    //! to depend on that element because if it changes we have to have
    //! a chance to change our decision about what GL calls to make.
    //! If this is NULL, then there are no cache dependencies.
    SoState            copiedFromParent;


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // Let our base class do its stuff..
    super.init(state);

    copiedFromParent = null;

}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides push() method to copy from previous element
//
// Use: public

public void push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // Let the parent copy values for us...
    super.push(state);

    // But remember that we're getting our values from above:
    copiedFromParent = state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void pop(SoState state, final SoElement childElt)
//
////////////////////////////////////////////////////////////////////////
{
    // Since popping this element has GL side effects, make sure any
    // open caches capture it.   We may not send any GL commands, but
    // the cache dependency must exist even if we don't send any GL
    // commands, because if the element changes, the _lack_ of GL
    // commands here is a bug (remember, GL commands issued here are
    // put inside the cache).
    capture(state);
    copiedFromParent = null;

    // If the previous element didn't have the same value...
    final SoGLShapeHintsElement child =
        ( SoGLShapeHintsElement ) childElt;

    // Restore previous shape hints if anything differs
    if (child.vertexOrdering != vertexOrdering ||
        child.shapeType      != shapeType)
        send(state.getGL2());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual set method, overridden to send GL commands
//
// Use: protected

public void setElt(VertexOrdering _vertexOrdering,
                              ShapeType _shapeType, FaceType _faceType)
//
////////////////////////////////////////////////////////////////////////
{
    // Optimization:  on push, we copy the value from the previous
    // element.  If the element is set to the same value, we don't
    // bother sending it, but do add a cache dependency on the
    // previous element.

    if (_vertexOrdering == VertexOrdering.ORDERING_AS_IS) 
        _vertexOrdering = vertexOrdering;
    if (_faceType == FaceType.FACE_TYPE_AS_IS) 
        _faceType = faceType;
    if (_shapeType == ShapeType.SHAPE_TYPE_AS_IS) 
        _shapeType = shapeType;

    // If anything is different, resend everything:
    // Note: don't bother with faceType, we don't send any GL commands
    // based on it (in OpenGL, everything is convex):
    boolean needToSend = (vertexOrdering != _vertexOrdering ||
                         shapeType      != _shapeType);

    // Let base class keep state up to date:
    super.setElt(_vertexOrdering, _shapeType, _faceType);

    if (needToSend) {
        send(copiedFromParent.getGL2());
        copiedFromParent = null;
    }
    else if (copiedFromParent != null) {
        // Cache dependency-- we rely on previous element to set up GL
        // correctly.
        SoGLShapeHintsElement parent =
            (SoGLShapeHintsElement ) getNextInStack();
        parent.capture(copiedFromParent);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends shape hints in element to GL.
//
// Use: private

private void send(GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    if (vertexOrdering != VertexOrdering.UNKNOWN_ORDERING) {

        // Tell GL which ordering to use
        gl2.glFrontFace(vertexOrdering == VertexOrdering.CLOCKWISE ? GL2.GL_CW : GL2.GL_CCW);

        // If the shapes are solid and the vertices are ordered,
        // we know we can do successful backface culling
        if (shapeType == ShapeType.SOLID) {
            gl2.glEnable(GL2.GL_CULL_FACE);
            gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_FALSE);
        }

        // If the shapes are not solid, but the vertices are ordered,
        // we know that we can do successful two-sided lighting
        else {
            gl2.glDisable(GL2.GL_CULL_FACE);
            gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
        }
    }

    // We know nothing
    else {
        gl2.glDisable(GL2.GL_CULL_FACE);
        gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_FALSE);
    }
}
}
