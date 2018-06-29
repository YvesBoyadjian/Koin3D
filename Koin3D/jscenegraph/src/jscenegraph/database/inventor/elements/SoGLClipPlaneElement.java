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
 |      This file defines the SoGLClipPlaneElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import org.lwjgl.opengl.GL11;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLClipPlaneElement
///  \ingroup Elements
///
///  Element that adds a clipping plane to the set currently in GL.
///  Overrides the virtual methods on SoClipPlaneElement to send the
///  clipping plane to GL.
///
///  Note that this class relies on SoClipPlaneElement to store the
///  plane in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLClipPlaneElement extends SoClipPlaneElement {

	  private
		    static int          maxGLPlanes = -1;    //!< Holds max number of GL clip planes

	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void
pop(SoState state, SoElement prevTopElement)
//
////////////////////////////////////////////////////////////////////////
{
    // Since popping this element has GL side effects, make sure any
    // open caches capture it
    capture(state);

    // Disable clip planes created in previous elements. Note that the
    // index into the planes array is identical to the GL clip plane
    // id, for convenience.

    SoGLClipPlaneElement prevElt =
        ( SoGLClipPlaneElement ) prevTopElement;

    GL2 gl2 = state.getGL2();
    
    int maxId = prevElt.getNum();
    if (maxId > getMaxGLPlanes(gl2))
        maxId = getMaxGLPlanes(gl2);

    for (int i = prevElt.startIndex; i < maxId; i++)
        if (i < getMaxGLPlanes(gl2))
            gl2.glDisable((GL2.GL_CLIP_PLANE0 + i));

    // Do parent's pop stuff
    super.pop(state, prevTopElement);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the maximum number of concurrent clipping planes supported
//    by GL implementation.
//
// Use: protected, static

protected int
getMaxGLPlanes(GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    // Inquire GL if not already done
    if (maxGLPlanes < 0) {
        final int[]           max = new int[1];
        gl2.glGetIntegerv(GL2.GL_MAX_CLIP_PLANES, max,0);
        maxGLPlanes = (int) max[0];
    }

    return maxGLPlanes;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds clip plane to element and sends it to GL.
//
// Use: protected, virtual

protected void
addToElt(SoState state, final SbPlane plane,
                               final SbMatrix modelMatrix)
//
////////////////////////////////////////////////////////////////////////
{
    // Do normal stuff
    super.addToElt(state, plane, modelMatrix);
    
    GL2 gl2 = state.getGL2();

    // If we haven't run out of clipping planes, send this one to GL.
    // Note that we send the plane in object space, since GL already
    // will transform it by the current model matrix.
    int planeId = getNum() - 1;
    if (planeId < getMaxGLPlanes(gl2)) {

        SbPlane   objPlane = get(planeId, false);
        SbVec3f   norm     = objPlane.getNormal();
        double[]        planeEquation = new double[4];

        planeEquation[0] = norm.getValue()[0];
        planeEquation[1] = norm.getValue()[1];
        planeEquation[2] = norm.getValue()[2];
        planeEquation[3] = -objPlane.getDistanceFromOrigin();

        GL11.glClipPlane((GL2.GL_CLIP_PLANE0 + planeId), planeEquation);

        gl2.glEnable((GL2.GL_CLIP_PLANE0 + planeId));
    }
}
	  
}
