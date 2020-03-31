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
 |      This file defines the SoGLModelMatrixElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.misc.SoState;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLModelMatrixElement
///  \ingroup Elements
///
///  Element that stores the current model matrix in GL. Overrides the
///  virtual methods on SoModelMatrixElement to send the matrix to GL
///  when necessary.
///
///  Note that this class relies on SoModelMatrixElement to store the
///  matrix in the instance. This is less expensive in the long run
///  than asking GL for the matrix when it is needed.
///
///  Because GL stores the model and view matrices in one matrix, this
///  has to do a little extra work when setting the model matrix to
///  identity or another matrix. (It has to set the GL model-view
///  matrix correctly.)
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLModelMatrixElement extends SoModelMatrixElement {

  private
    //! We need to save the state to access the viewing matrix
    SoState             state;

    //! And we need to remember the nodeId of the viewing matrix
    //! element to see if it changes between pushMatrixElt() and
    //! popMatrixElt().
    private long            viewEltNodeId;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    // Do normal initialization stuff
    super.init(_state);

    // Save state in instance in case we need it later
    state = _state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, causing side effects in GL.
//
// Use: public

public void
push(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    // Save the old matrix first, just in case
    super.push(_state);

    // Save state in instance in case we need it later
    state = _state;
    
    GL2 gl2 = state.getGL2();

    gl2.glPushMatrix();

    // And remember the viewMatrixElement nodeId from our parent:
    SoGLModelMatrixElement mtxElt = 
        (SoGLModelMatrixElement ) getNextInStack();
    viewEltNodeId = mtxElt.viewEltNodeId;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void
pop(SoState state,  SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = state.getGL2();
    gl2.glPopMatrix();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets model matrix to identity matrix in element accessed from
//    state. Overrides method on SoModelMatrixElement, since we have
//    to keep the model-view matrix set correctly.
//
// Use: protected, virtual

protected void
makeEltIdentity()
//
////////////////////////////////////////////////////////////////////////
{
    // Since the GL stores a model-view matrix, which is composed of
    // both the view matrix and the model matrix, we have to resend
    // the view matrix to effectively make the model matrix identity.
    // First we have to access the view matrix from the state and then
    // set the model matrix to that value. Accessing the view matrix
    // will "capture" that element, which is necessary for caching to
    // detect the affects of changes to the view matrix on this
    // operation.
	GL2 gl2 = state.getGL2();

    final SbMatrix viewMat = SoGLViewingMatrixElement.get(state);

    // Set the matrix in the element to identity
    super.makeEltIdentity();

    // Send the current viewing matrix to GL
    gl2.glLoadMatrixf((float []) viewMat.toGL(),0);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets model matrix in element. Assumes that the passed matrix
//    contains the correct combination of view and model matrices.
//
// Use: protected

protected void
setElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    // See the comments in makeEltIdentity() as to why we need to deal
    // with the viewing matrix

    final SbMatrix viewMat = SoGLViewingMatrixElement.get(state);

    // Set the matrix in the element to the given one
    super.setElt(matrix);
    
    GL2 gl2 = state.getGL2();

    // Send the product of the viewing matrix and the given matrix to GL
    gl2.glLoadMatrixf((float []) (matrix.operator_mul(viewMat)).toGL(),0);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies into model matrix in element.
//
// Use: protected, virtual

protected void
multElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    super.multElt(matrix);
    
    glMultMatrixf((float []) matrix.toGL());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates model matrix in element by the given vector.
//
// Use: public, virtual

public void
translateEltBy(final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    super.translateEltBy(translation);
    
    GL2 gl2 = state.getGL2();

    gl2.glTranslatef(translation.getValueRead()[0], translation.getValueRead()[1], translation.getValueRead()[2]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates model matrix in element by the given rotation.
//
// Use: public, virtual

public void
rotateEltBy(final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     axis = new SbVec3f();
    final float[]       angle = new float[1];

    super.rotateEltBy(rotation);

    rotation.getValue(axis, angle);
    
    GL2 gl2 = state.getGL2();

    glRotatef(angle[0] * (180.0f / (float)Math.PI), axis.getValueRead()[0], axis.getValueRead()[1], axis.getValueRead()[2]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales model matrix in element by the given factors.
//
// Use: public, virtual

public void
scaleEltBy(final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    super.scaleEltBy(scaleFactor);
    
    GL2 gl2 = state.getGL2();

    glScalef(scaleFactor.getValueRead()[0], scaleFactor.getValueRead()[1], scaleFactor.getValueRead()[2]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual method that does matrix push.  GL version overrides to
//    make glPushMatrix call:
//
// Use: public, static

public SbMatrix
pushMatrixElt()
//
////////////////////////////////////////////////////////////////////////
{
    //
    // There's no cache dependency here, because we only care if the
    // nodeId changes when traversing our children, we don't really
    // care about the particular value of the ViewingMatrixElement.
    // Well, actually I'm lying a little here.  IF you happened to
    // instance a camera both above and below a TransformSeparator,
    // and THEN you replaced the instance above the TransformSeparator
    // with another camera, AND the TransformSeparator was in a cache,
    // you'll get incorrect behavior.  I'm not going to worry about
    // that case...
    //
    viewEltNodeId = SoGLViewingMatrixElement.getNodeId(state);
    
    GL2 gl2 =state.getGL2();

    gl2.glPushMatrix();
    return super.pushMatrixElt();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual method that does matrix pop.  GL version overrides to
//    make glPopMatrix call:
//
// Use: public, static

public void
popMatrixElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = state.getGL2();
	
    gl2.glPopMatrix();
    super.popMatrixElt(matrix);

    long afterNodeId = SoGLViewingMatrixElement.getNodeId(state);

    if (afterNodeId != viewEltNodeId) {
        // Camera underneth us, must reset:
        setElt(matrix);
    }
}

}
