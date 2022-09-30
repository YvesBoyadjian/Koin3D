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
 |      This file defines the SoGLLinePatternElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;

import static org.lwjgl.opengl.GL11.*;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLLinePatternElement
///  \ingroup Elements
///
///  Element that stores the current line pattern in GL. Overrides the
///  virtual methods on SoLinePatternElement to send the line pattern
///  to GL when necessary.
///
///  Note that this class relies on SoLinePatternElement to store the
///  pattern in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLLinePatternElement extends SoLinePatternElement {

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
//    Overrides push() method to copy line pattern
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLLinePatternElement        prevElt =
        ( SoGLLinePatternElement ) getNextInStack();

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
    // Since popping this element has GL side effects, make sure any
    // open caches capture it.  We may not send any GL commands, but
    // the cache dependency must exist even if we don't send any GL
    // commands, because if the element changes, the _lack_ of GL
    // commands here is a bug (remember, GL commands issued here are
    // put inside the cache).
    capture(state);
    copiedFromParent = null;

    // If the previous element didn't have the same value...
    SoGLLinePatternElement child =
        ( SoGLLinePatternElement )childElt;
    
    GL2 gl2 = state.getGL2();
        
    // Restore previous line pattern
    if (data != child.data)
        send(gl2);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets line pattern in element.
//
// Use: protected, virtual

public void setElt(int pattern)
//
////////////////////////////////////////////////////////////////////////
{
    // Optimization:  on push, we copy the value from the previous
    // element.  If the element is set to the same value, we don't
    // bother sending it, but do add a cache dependency on the
    // previous element.
	
    if (data != pattern) {
        data = pattern;
        
    	// java port
    	GL2 gl2 = copiedFromParent.getGL2();

        send(gl2);
        copiedFromParent = null;
    }
    else if (copiedFromParent != null) {
        SoGLLinePatternElement parent = 
            (SoGLLinePatternElement ) getNextInStack();
        parent.capture(copiedFromParent);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends line pattern in element to GL.
//
// Use: private

private void
send(GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    // Special case for solid lines (the default line pattern):
    // disable line stippling for better efficiency
    if (data == getDefault())
        gl2.glDisable(GL2.GL_LINE_STIPPLE);

    else {
        gl2.glEnable(GL2.GL_LINE_STIPPLE);
        glLineStipple(1, (short)data);
    }
}
  
}
