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
 |      This file defines the SoGLDrawStyleElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import static jscenegraph.opengl.GL.GL_FRONT_AND_BACK;
import static jscenegraph.opengl.GL2GL3.GL_FILL;
import static jscenegraph.opengl.GL2GL3.GL_LINE;
import static jscenegraph.opengl.GL2GL3.GL_POINT;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLDrawStyleElement
///  \ingroup Elements
///
///  Element that changes the current draw style in GL. Overrides the
///  virtual methods on SoDrawStyleElement to send the draw style to GL
///  when necessary.
///
///  Note that this class relies on SoDrawStyleElement to store the
///  style in the instance.
///
///  Also note that this tells GL only how to draw polygons. Shapes
///  that contain lines still have to check for POINTS draw styles and
///  act accordingly.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLDrawStyleElement extends SoDrawStyleElement {
	
	private GL2 gl2;

	  private
    //! We save the state to figure out if the lastPattern variable was
    //! copied from a parent element; if it was, then caches will have
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

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{

    // Initialize base class stuff
    super.init(state);

    copiedFromParent = null;

}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides push() method to copy draw style
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
	gl2 = state.getGL2(); //java port

     SoGLDrawStyleElement  prevElt =
        ( SoGLDrawStyleElement ) getNextInStack();

    data = prevElt.data;

    copiedFromParent = state;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void
pop(SoState state, SoElement childElt)
//
////////////////////////////////////////////////////////////////////////
{
    gl2 = state.getGL2(); //java port

    // Since popping this element has GL side effects, make sure any
    // open caches capture it.  We may not send any GL commands, but
    // the cache dependency must exist even if we don't send any GL
    // commands, because if the element changes, the _lack_ of GL
    // commands here is a bug (remember, GL commands issued here are
    // put inside the cache).
    capture(state);
    copiedFromParent = null;

    // If the previous element didn't have the same value...
    SoGLDrawStyleElement child =
        ( SoGLDrawStyleElement )childElt;
        
    // Restore previous draw style
    if (data != child.data)
        send();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets draw style in element.
//
// Use: protected, virtual

public void
setElt(int style)
//
////////////////////////////////////////////////////////////////////////
{
    // Optimization:  on push, we copy the value from the previous
    // element.  If the element is set to the same value, we don't
    // bother sending it, but do add a cache dependency on the
    // previous element.

    if (data != style) {
        data = style;
        send();
        copiedFromParent = null;
    }
    else if (copiedFromParent != null) {
        SoGLDrawStyleElement parent = 
            (SoGLDrawStyleElement )getNextInStack();
        parent.capture(copiedFromParent);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends draw style in element to GL.
//
// Use: private

private void
send()
//
////////////////////////////////////////////////////////////////////////
{
    // Tell GL how to draw polygons. NOTE that this works only for
    // filled faces; shapes that contain lines still have to check for
    // POINTS draw styles and act accordingly.

    switch (Style.fromValue(data)) {

      case FILLED:
        gl2.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        break;

      case LINES:
        gl2.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        break;

      case POINTS:
        gl2.glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
        break;

      case INVISIBLE:
        // Nothing we can do here. This case is checked by the shapes
        // themselves.
        break;
    }
}
}
