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
 |      This file defines the SoGLTextureEnabledElement class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoTextureEnabledElement;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLTextureEnabledElement
///  \ingroup Elements
///
///  Element that enables/disables textures.  This is implemented as a
///  separate element from the TextureImageElement so the texture image
///  can be changed without blowing caches containing shapes-- shapes
///  need to know if texturing is going on or not so they can decide to
///  send down texture coordinates or not.
///
///  This is set by the GLTextureImageElement and the
///  GLTextureImageElement; the interaction between the three is a
///  little complicated.  Each of the other two elements always sets up
///  the GL texture state appropriately; however, texturing is only
///  enabled if both elements agree that it should be enabled (they
///  check each other's value before calling set).
///
///  This element cannot be overridden, but, since the elements that
///  set it check each other's value, overriding those elements has
///  the same effect.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLTextureEnabledElement extends SoTextureEnabledElement {

    //! We save the state to figure out if the lastPattern variable was
    //! copied from a parent element; if it was, then caches will have
    //! to depend on that element because if it changes we have to have
    //! a chance to change our decision about what GL calls to make.
    //! If this is NULL, then there are no cache dependencies.
    SoState            copiedFromParent;

	GL2 gl2;

	    //! Used by shapes to figure out if they need to send texture
    //! coordinates:
    public static boolean       get(SoState state)
        { return (boolean)(SoInt32Element.get(classStackIndexMap.get(SoGLTextureEnabledElement.class), state) != 0); }

	
	
    //! By default there is not texture
	public static boolean       getDefault()    { return false; }

	
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
    data = getDefault() ? 1 : 0;

    copiedFromParent = null;
}

/////////////////////////////////////////////////////////////////////////
//
// Description:
//   Set the current texture enablement in the state
//
public static void    
set(SoState state, boolean value)
{
	SoTextureEnabledElement.set(classStackIndexMap.get(SoGLTextureEnabledElement.class), state, (int)(value?1:0)); //COIN3D        
    SoShapeStyleElement.setTextureEnabled(state,value);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides push() method to copy texture enabled flag
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{    
    // java port
	gl2 = SoGLCacheContextElement.get(state);
	
    SoGLTextureEnabledElement     prevElt =
        ( SoGLTextureEnabledElement ) getNextInStack();

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
    // java port
	gl2 = SoGLCacheContextElement.get(state);
	
    // Since popping this element has GL side effects, make sure any
    // open caches capture it.  We may not send any GL commands, but
    // the cache dependency must exist even if we don't send any GL
    // commands, because if the element changes, the _lack_ of GL
    // commands here is a bug (remember, GL commands issued here are
    // put inside the cache).
    capture(state);
    copiedFromParent = null;

    // If the previous element didn't have the same value...
    SoGLTextureEnabledElement child =
        (SoGLTextureEnabledElement )childElt;
        
    // Restore previous enabled flag
    if (data != child.data)
        send();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets Enabled in element accessed from state.
//
// Use: internal, virtual

public void
setElt(int flag)
//
////////////////////////////////////////////////////////////////////////
{
    // Optimization:  on push, we copy the value from the previous
    // element.  If the element is set to the same value, we don't
    // bother sending it, but do add a cache dependency on the
    // previous element.

    if (data != flag) {
        data = flag;
        send();
        copiedFromParent = null;
    }
    else if (copiedFromParent != null) {
        SoGLTextureEnabledElement parent = 
            (SoGLTextureEnabledElement ) getNextInStack();
        parent.capture(copiedFromParent);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends flag in element to GL.
//
// Use: private

private void
send()
//
////////////////////////////////////////////////////////////////////////
{
    if (data != 0)
        gl2.glEnable(GL2.GL_TEXTURE_2D);
    else
        gl2.glDisable(GL2.GL_TEXTURE_2D);
}
}
