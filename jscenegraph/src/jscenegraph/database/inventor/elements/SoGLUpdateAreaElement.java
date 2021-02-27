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
 |      This file defines the SoGLUpdateAreaElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2fSingle;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.misc.SoState;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLUpdateAreaElement
///  \ingroup Elements
///
///  Element that stores the rectangular area within the current
///  viewport region that needs to be updated when rendering. It can be
///  used for partial rendering updates when applications know that
///  only a portion of the objects need to be rerendered. Cameras can
///  use the info in this element to set up a view volume against which
///  culling can be performed. This element also sets up the GL scissor
///  box to limit drawing. 
///
///  The update area is specified in normalized viewport coordinates,
///  where (0,0) is the lower left corner of the viewport and (1,1) is
///  the upper right corner. The area is given as an origin and a size.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLUpdateAreaElement extends SoElement {

	   protected final
		        SbVec2fSingle             origin = new SbVec2fSingle(), size = new SbVec2fSingle();
		    	
  private
    //! This flag lets us know if the element is set to its initial
    //! default value, which is not sent to GL. This is so we can tell
    //! whether the GL value is up to date.
    boolean              isDefault;
	   
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to compare update areas.
//
// Use: public

public boolean
matches( SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLUpdateAreaElement uaElt = ( SoGLUpdateAreaElement ) elt;

    return (origin.operator_equal_equal(uaElt.origin) && size.operator_equal_equal(uaElt.size));
}
	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Create a copy of this instance suitable for calling matches()
	   //     on.
	   //
	   // Use: protected
	   
	  public SoElement 
	   copyMatchInfo()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoGLUpdateAreaElement result =
	           (SoGLUpdateAreaElement )getTypeId().createInstance();
	   
	       result.origin.copyFrom(origin);
	       result.size.copyFrom(size);
	   
	       return result;
	   }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    origin.copyFrom(getDefaultOrigin());
    size.copyFrom(getDefaultSize());

    // Set flag to indicate we are using the default value
    isDefault = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets update area in element accessed from state.
//
// Use: public, static

public static void
set(SoState state,
                           final SbVec2f origin, final SbVec2f size)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLUpdateAreaElement       elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoGLUpdateAreaElement ) getElement(state, classStackIndexMap.get(SoGLUpdateAreaElement.class));

    if (elt != null) {
        // If we already have a non-default value and it's the same as the
        // new one, do nothing
        // NOTE: since we know that no nodes set this element, we don't ???
        // have to worry about making open caches depend on it. This may???
        // change later.                                                ???
        if (! elt.isDefault && elt.origin.operator_equal_equal(origin) && elt.size.operator_equal_equal(size))
            return;

        // Set area in element and send to GL
        elt.origin.copyFrom(origin);
        elt.size.copyFrom(size);
        elt.isDefault = false;
        elt.send(state);
    }
}

	  
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns update area from state. Returns TRUE if the update area
//    is the default, namely, the entire viewport.
//
// Use: public, static

public static boolean
get(final SoState state, final SbVec2f origin, final SbVec2f size)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLUpdateAreaElement elt;

    elt = (SoGLUpdateAreaElement)
        getConstElement(state, classStackIndexMap.get(SoGLUpdateAreaElement.class));

    origin.copyFrom( elt.origin);
    size.copyFrom( elt.size);

    return (origin.operator_equal_equal(getDefaultOrigin()) && size.operator_equal_equal(getDefaultSize()));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying from previous element.
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoGLUpdateAreaElement nextElt =
        (SoGLUpdateAreaElement ) getNextInStack();

    origin.copyFrom( nextElt.origin);
    size.copyFrom(nextElt.size);
    isDefault   = nextElt.isDefault;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
// Use: public

public void
pop(SoState state, final SoElement prevTopElement)
//
////////////////////////////////////////////////////////////////////////
{
    // If the new top element had a default value, set it to the
    // current value, which will remain in effect in GL
    if (isDefault) {
        SoGLUpdateAreaElement prevElt =
            ( SoGLUpdateAreaElement ) prevTopElement;
        origin.copyFrom(prevElt.origin);
        size.copyFrom(prevElt.size);
        isDefault = false;
    }

    // Otherwise, restore the previous update area
    else {
        // Since popping this element has GL side effects, make sure any
        // open caches capture it
        capture(state);

        // Restore previous update area
        send(state);
    }
}




    //! Returns the default update area origin and size
	public static SbVec2f      getDefaultOrigin()      { return new SbVec2f(0.0f, 0.0f); }
    public static SbVec2f      getDefaultSize()        { return new SbVec2f(1.0f, 1.0f); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends current update area to GL.
//
// Use: private

private void
send(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
	
    // We need to know the current window size. This could
    // potentially be wrong if the window size changes during
    // rendering, but that's just not possible.
    final SbViewportRegion      vpReg = SoViewportRegionElement.get(state);
    final SbVec2s               winSize = vpReg.getWindowSize();

    glScissor((int) (origin.getValue()[0] * winSize.getValue()[0]),
              (int) (origin.getValue()[1] * winSize.getValue()[1]),
              (int) (size.getValue()[0] * winSize.getValue()[0]),
              (int) (size.getValue()[1] * winSize.getValue()[1]));
}

}
