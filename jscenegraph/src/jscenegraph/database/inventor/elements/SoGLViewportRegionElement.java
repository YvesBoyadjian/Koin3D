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
 |      This file defines the SoGLViewportRegionElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLViewportRegionElement
///  \ingroup Elements
///
///  Element that stores the current viewport region in GL. Overrides
///  the virtual methods on SoViewportRegionElement to send the region
///  to GL when necessary.
///
///  Note that this class relies on SoViewportRegionElement to store the
///  region in the instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLViewportRegionElement extends SoViewportRegionElement {
	
	GL2 gl2;
	
	public SoGLViewportRegionElement() {}

  private
    //! This flag lets us know if the element is set to its initial
    //! (usually bogus) value, which is not sent to GL. This is so we
    //! can tell whether the GL value is up to date.
    boolean              isDefault;


	  public static void
	   initClass(final Class<? extends SoElement> javaClass)
	   {
		  SoElement.initClass(javaClass);
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
    // Do regular stuff
    super.init(state);

    // Set flag to indicate we are using the default value
    isDefault = true;
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
    final SoGLViewportRegionElement nextElt =
        (SoGLViewportRegionElement ) getNextInStack();

    viewportRegion.copyFrom(nextElt.viewportRegion);
    isDefault      = nextElt.isDefault;
    
    gl2 = state.getGL2(); // java port
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
    gl2 = state.getGL2();

    // If the new top element had a default value, set it to the
    // current value, which will remain in effect in GL
    if (isDefault) {
        final SoGLViewportRegionElement prevElt =
            ( SoGLViewportRegionElement ) prevTopElement;
        viewportRegion.copyFrom( prevElt.viewportRegion);
        isDefault = false;
    }

    // Otherwise, restore the previous viewport
    else {
        // Since popping this element has GL side effects, make sure any
        // open caches capture it
        capture(state);

        // Restore previous viewport region
        send();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets GLviewport region in element.
//
// Use: protected, virtual

protected void
setElt(final SbViewportRegion vpReg)
//
////////////////////////////////////////////////////////////////////////
{
    // If we already have a non-default value and it's the same as the
    // new one, do nothing
    // NOTE: since we know that no nodes set this element, we don't     ???
    // have to worry about making open caches depend on it. This may    ???
    // change later.                                                    ???
    if (! isDefault && viewportRegion.operator_equal_equal(vpReg))
        return;

    // Set region in element and send to GL
    viewportRegion.copyFrom(vpReg);
    isDefault      = false;
    send();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends current viewport region to GL.
//
// Use: private

private void
send()
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec2s       vpOrig  = viewportRegion.getViewportOriginPixels();
    final SbVec2s       vpSize  = viewportRegion.getViewportSizePixels();

    gl2.glViewport(vpOrig.getValue()[0], vpOrig.getValue()[1], vpSize.getValue()[0], vpSize.getValue()[1]);
}
}
