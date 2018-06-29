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
 |      This file defines the SoPickRayElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoPickRayElement
///  \ingroup Elements
///
///  Element that stores the current ray to use for picking. It
///  contains the world space ray information in terms of an
///  SbViewVolume. The projection point and view direction of the
///  volume represent the ray itself. The width (which equals the
///  height) represents the diameter of the picking circle in the near
///  plane. The near and far planes represent the distances to the near
///  and far planes between which valid intersections must lie.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPickRayElement extends SoElement {

  protected final
    SbViewVolume        volume = new SbViewVolume();         //!< Ray as view volume
	

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
    // Nothing to initialize. Just wait for a real picking ray to be set?
}

  
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to always return FALSE. This element
//    should never have anything to do with cache invalidation.
//
// Use: public

public boolean
matches( SoElement element)
//
////////////////////////////////////////////////////////////////////////
{
    SoDebugError.post("SoPickRayElement::matches",
                       "This method should never be called!");

    return false;
}
		
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Override method to return NULL; it should never be called.
	   //
	   // Use: protected
	   
	  public SoElement 
	   copyMatchInfo()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoDebugError.post("SoPickRayElement::copyMatchInfo",
	                          "This method should never be called!");
	   
	       return null;
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the ray view volume in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, final SbViewVolume volume)
//
////////////////////////////////////////////////////////////////////////
{
    SoPickRayElement    elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoPickRayElement ) getElement(state, classStackIndexMap.get(SoPickRayElement.class));

    if (elt != null)
        elt.volume.copyFrom(volume);
}

	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the ray view volume from the state
//
// Use: public, static

public static SbViewVolume 
get(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoPickRayElement elt;

    elt = (SoPickRayElement ) getConstElement(state, classStackIndexMap.get(SoPickRayElement.class));

    return elt.volume;
}

	  
	   }
