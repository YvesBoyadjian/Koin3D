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
 |      This file defines the SoGLNormalElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLNormalElement
///  \ingroup Elements
///
///  Element that stores the current surface normals. Adds methods to
///  send the surface normal to GL on demand.
///
///  Note that this class relies on SoNormalElement to store the
///  normals in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLNormalElement extends SoNormalElement {

		private SoState state; // java port

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // Do standard initialization of normal element
    super.init(state);
    this.state = state;
    
    GL2 gl2 = state.getGL2();

    // Make sure OpenGL normalizes normal vectors. (This is disabled
    // by default.) Since our normals are likely to be scaled at some
    // point, we'll play it safe.
    gl2.glEnable(GL2.GL_NORMALIZE);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends normal with the given index to GL.
//
// Use: public

public void send(int index)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (index < 0 || index >= numNormals)
        SoDebugError.post("SoGLNormalElement::send",
                           "Index ("+index+") is out of range 0 - "+( numNormals - 1));
//#endif /* DEBUG */

    GL2 gl2 = SoGLCacheContextElement.get(state);

    gl2.glNormal3fv(normals[index].getValueRead(),0);
}
}
