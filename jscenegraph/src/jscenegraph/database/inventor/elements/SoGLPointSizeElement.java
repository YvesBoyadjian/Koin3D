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
 |      This file defines the SoGLPointSizeElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;



///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLPointSizeElement
///  \ingroup Elements
///
///  Element that stores the current point size in GL. Overrides the
///  virtual methods on SoPointSizeElement to send the point size to GL
///  when necessary.
///
///  Note that this class relies on SoPointSizeElement to store the
///  size in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLPointSizeElement extends SoPointSizeElement {

  private
    //! This variable is TRUE if the value in this element was copied
    //! from our parent.  If set is called with the same value, no GL
    //! commands have to be done-- it is as if this element doesn't
    //! exist, and the parent's value is used.  Of course, a cache
    //! dependency will have to be added in this case (if the parent
    //! changes, the cache is not valid).  Set sets this flag to false.
    boolean                copiedFromParent;

    //! We need to store the state so we can get the viewport region element
    private SoState             state;

	
	
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

    copiedFromParent = false;

    // Save the state so we can get the SoViewportRegionElement when
    // we send the point size to GL
    state = _state;

}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying data from previous element
//
// Use: public

public void
push(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLPointSizeElement  prevElt =
        ( SoGLPointSizeElement ) getNextInStack();

    data = prevElt.data;

    copiedFromParent = true;
    state = _state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void
pop(SoState state, final SoElement childElt)
//
////////////////////////////////////////////////////////////////////////
{
    // Since popping this element has GL side effects, make sure any
    // open caches capture it.  We may not send any GL commands, but
    // the cache dependency must exist even if we don't send any GL
    // commands, because if the element changes, the _lack_ of GL
    // commands here is a bug (remember, GL commands issued here are
    // put inside the cache).
    capture(state);
    copiedFromParent = false;

    // If the previous element didn't have the same value...
    SoGLPointSizeElement child =
        ( SoGLPointSizeElement ) childElt;
        
    // Restore previous point size
    if (data != child.data)
        send(state.getGL2());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets point size in element.
//
// Use: protected, virtual

protected void
setElt(float size)
//
////////////////////////////////////////////////////////////////////////
{
    // Optimization:  on push, we copy the value from the previous
    // element.  If the element is set to the same value, we don't
    // bother sending it, but do add a cache dependency on the
    // previous element.

    if (data != size) {
        data = size;
        send(state.getGL2());
        copiedFromParent = false;
    }
    else if (copiedFromParent) {
        SoGLPointSizeElement parent =
            (SoGLPointSizeElement ) getNextInStack();
        parent.capture(state);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends point size in element to GL.
//
// Use: private

private void
send(GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    float       size;

    // If point size is 0 (the default), use the default GL value of 1.0
    if (data == 0.0)
        size = 1.0f;

    // Otherwise, compute the point size to be the appropriate number
    // of pixels, based on the current viewport region data
    else
        size = data * SoViewportRegionElement.get(state).getPixelsPerPoint();

    glPointSize(size);
}
	
}
