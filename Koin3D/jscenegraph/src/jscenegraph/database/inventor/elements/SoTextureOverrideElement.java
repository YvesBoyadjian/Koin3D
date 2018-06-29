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
 |      This file defines the SoTextureQualityElement and
 |      SoTextureOverrideElement classes.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoTextureOverrideElement
///  \ingroup Elements
///
///  Element that stores a flag for each type of element which can be
///  overridden.  Nodes implement override by setting the appropriate
///  bit if their override flag is on, and ignoring overridden elements
///  if the corresponding bit in the state's SoTextureOverrideElement is set.
///  
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureOverrideElement extends SoElement {
	
    enum Flags {
        TEXTURE_QUALITY(0x1),
        TEXTURE_IMAGE(0x2);
        private int value;
        Flags(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

	

    private int flags;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.  All flags are initialized to FALSE (0).
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    flags = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying flags from previous top instance.
//
// Use: public
public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureOverrideElement elt =
        (SoTextureOverrideElement )getNextInStack();

    flags = elt.flags;
    elt.capture(state);  // Capture previous element
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set texture quality override flag
//
// Use: public, static
public static boolean
getQualityOverride(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureOverrideElement elt;
    elt = ( SoTextureOverrideElement )
        getConstElement(state, classStackIndexMap.get(SoTextureOverrideElement.class));
    return (elt.flags & Flags.TEXTURE_QUALITY.getValue()) != 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set texture image override flag
//
// Use: public, static
public static boolean
getImageOverride(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureOverrideElement elt;
    elt = ( SoTextureOverrideElement )
        getConstElement(state, classStackIndexMap.get(SoTextureOverrideElement.class));
    return (elt.flags & Flags.TEXTURE_IMAGE.getValue()) != 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set texture quality override flag
//
// Use: public, static
public static void
setQualityOverride(SoState state,
                                             boolean override)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureOverrideElement    elt;
    elt = (SoTextureOverrideElement )getElement(state, classStackIndexMap.get(SoTextureOverrideElement.class));
    if (override)
        elt.flags |= Flags.TEXTURE_QUALITY.getValue();
    else
        elt.flags &= ~Flags.TEXTURE_QUALITY.getValue();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set texture image override flag
//
// Use: public, static
public static void
setImageOverride(SoState state,
                                           boolean override)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureOverrideElement    elt;
    elt = (SoTextureOverrideElement )getElement(state, classStackIndexMap.get(SoTextureOverrideElement.class));
    if (override)
        elt.flags |= Flags.TEXTURE_IMAGE.getValue();
    else
        elt.flags &= ~Flags.TEXTURE_IMAGE.getValue();
}
    
	
	@Override
	public boolean matches(SoElement elt) {
		return (flags == ((SoTextureOverrideElement ) elt).flags);
	}

	@Override
	public SoTextureOverrideElement copyMatchInfo() {
	    SoTextureOverrideElement result =
	            (SoTextureOverrideElement )getTypeId().createInstance();

	        result.flags = flags;

	        return result;

	}

}
