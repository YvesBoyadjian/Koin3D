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
 |      This file defines the SoGLTextureMatrixElement class.
 |
 |   Author(s)          : Gavin Bell
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

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLTextureMatrixElement
///  \ingroup Elements
///
///  Element that stores the current texture matrix in GL. Overrides the
///  virtual methods on SoTextureMatrixElement to send the matrix to GL
///  when necessary.
///
//////////////////////////////////////////////////////////////////////////////

public class SoGLTextureMatrixElement extends SoTextureMatrixElement {


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void pop(final SoState state, final SoElement element)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = state.getGL2();
	
    gl2.glMatrixMode(GL2.GL_TEXTURE);
    gl2.glLoadMatrixf((float [])(getElt().toGL()),0);
    gl2.glMatrixMode(GL2.GL_MODELVIEW);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Make the texture matrix the identity matrix.
//
// Use: protected, virtual

protected void makeEltIdentity(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    GL2 gl2 = state.getGL2();

    gl2.glMatrixMode(GL2.GL_TEXTURE);
    gl2.glLoadIdentity();
    gl2.glMatrixMode(GL2.GL_MODELVIEW);

    super.makeEltIdentity(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies into texture matrix in element.
//
// Use: protected, virtual

protected void multElt(SoState state, final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    GL2 gl2 = state.getGL2();

    gl2.glMatrixMode(GL2.GL_TEXTURE);
    glMultMatrixf((float []) matrix.toGL());
    gl2.glMatrixMode(GL2.GL_MODELVIEW);

    super.multElt(state, matrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates texture matrix in element by the given vector.
//
// Use: public, virtual

public void translateEltBy(SoState state, final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    GL2 gl2 = state.getGL2();

    gl2.glMatrixMode(GL2.GL_TEXTURE);
    gl2.glTranslatef(translation.getValue()[0], translation.getValue()[1], translation.getValue()[2]);
    gl2.glMatrixMode(GL2.GL_MODELVIEW);

    super.translateEltBy(state, translation);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates texture matrix in element by the given rotation.
//
// Use: public, virtual

public void rotateEltBy(SoState state, final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     axis = new SbVec3f();
    final float[]       angle = new float[1];

    rotation.getValue(axis, angle);

    GL2 gl2 = state.getGL2();

    gl2.glMatrixMode(GL2.GL_TEXTURE);
    glRotatef(angle[0] * (180.0f / (float)Math.PI), axis.getValue()[0], axis.getValue()[1], axis.getValue()[2]);
    gl2.glMatrixMode(GL2.GL_MODELVIEW);

    super.rotateEltBy(state, rotation);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales texture matrix in element by the given factors.
//
// Use: public, virtual

public void scaleEltBy(SoState state, final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    GL2 gl2 = state.getGL2();

    gl2.glMatrixMode(GL2.GL_TEXTURE);
    glScalef(scaleFactor.getValue()[0], scaleFactor.getValue()[1], scaleFactor.getValue()[2]);
    gl2.glMatrixMode(GL2.GL_MODELVIEW);

    super.scaleEltBy(state, scaleFactor);
}
	
}
