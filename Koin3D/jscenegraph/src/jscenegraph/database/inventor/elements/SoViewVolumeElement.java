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
 |      This file defines the SoViewVolumeElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoViewVolumeElement
///  \ingroup Elements
///
///  Element that stores the current viewing volume.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoViewVolumeElement extends SoReplacedElement {

	  protected
    boolean              volumeWasSet;  //!< COMPAT 2.0 ???
    protected final SbViewVolume        viewVolume = new SbViewVolume();
	

	  public static void
	   initClass(final Class<? extends SoElement> javaClass)
	   {
		  SoElement.initClass(javaClass);
	   }


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
    super.init(state);

    viewVolume.ortho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the ray view volume in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, SoNode node,
                         final SbViewVolume volume)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewVolumeElement elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoViewVolumeElement ) getElement(state, classStackIndexMap.get(SoViewVolumeElement.class), node);

    if (elt != null) {
        elt.viewVolume.copyFrom(volume);
    }
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
    SoViewVolumeElement elt;

    elt = (SoViewVolumeElement )getConstElement(state, classStackIndexMap.get(SoViewVolumeElement.class));

    return elt.viewVolume;
}

}
