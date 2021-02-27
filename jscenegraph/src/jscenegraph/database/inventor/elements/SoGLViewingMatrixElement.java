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
 |      This file defines the SoGLViewingMatrixElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLViewingMatrixElement
///  \ingroup Elements
///
///  Element that stores the current viewing matrix in GL. Overrides
///  the virtual methods on SoViewingMatrixElement to send the matrix
///  to GL when necessary.
///
///  Note that this class relies on SoViewingMatrixElement to store the
///  matrix in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLViewingMatrixElement extends SoViewingMatrixElement {

  private
    //! We need to store the state so we can get the model matrix element
    SoState             state;


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void
init(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    // Initialize base class stuff
    super.init(_state);

    // Save the state so we can get the SoModelMatrixElement when
    // we send the viewing matrix to GL
    state = _state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, saving state pointer.
//
// Use: public

public void
push(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    state = _state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element
//
// Use: public

public void
pop(SoState state, SoElement prevElt)
//
////////////////////////////////////////////////////////////////////////
{
    //
    // If the previous element is at depth zero, don't bother
    // restoring the matrices-- they will just be setup again the next
    // time a renderAction is applied.  Essentially, the first camera
    // in a scene graph will leak outside of its separator.
    //
    //if (prevElt.getDepth() != 0) { COIN3D
        // Since popping this element has GL side effects, make sure any
        // open caches capture it
        capture(state);

        // Restore previous view matrix
        send();
    //}
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets GLViewing matrix in element.
//
// Use: protected, virtual

protected void
setElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    viewingMatrix.copyFrom(matrix);
    send();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends current viewing matrix to GL.
//
// Use: private

private void
send()
//
////////////////////////////////////////////////////////////////////////
{
    // Get the current model matrix from the state. If it isn't
    // identity, make sure that it gets multiplied into the current
    // model-view matrix in GL.

    final boolean[] modelIsIdent = new boolean[1];
    final SbMatrix modelMat = SoModelMatrixElement.get(state, modelIsIdent);
    
    GL2 gl2 = state.getGL2();

    if (! modelIsIdent[0]) {
        final SbMatrix modelView = viewingMatrix.operator_mul(modelMat);
        gl2.glLoadMatrixf((float []) modelView.toGL(),0);
    }

    else
        gl2.glLoadMatrixf((float []) viewingMatrix.toGL(),0);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used by SoGLModelMatrixElement when it need to figure out if
//    this element has changed between a glPushMatrix() and a
//    glPopMatrix():
//
// Use: internal

public static long
getNodeId(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    final SoGLViewingMatrixElement vme = ( SoGLViewingMatrixElement )
        state.getConstElement(getClassStackIndex(SoGLViewingMatrixElement.class));
    return vme.SoReplacedElement_getNodeId();
}




}
