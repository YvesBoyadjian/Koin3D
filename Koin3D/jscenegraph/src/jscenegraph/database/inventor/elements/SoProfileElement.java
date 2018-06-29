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
 |      This file defines the SoProfileElement class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Thad Beier,
 |                        Dave Immel, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SoNodeList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoProfile;

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoProfileElement
///  \ingroup Elements
///
///  Element storing 2D profiles for nurbs and 3d text
///
//////////////////////////////////////////////////////////////////////////////

public class SoProfileElement extends SoAccumulatedElement {

	  public
		    enum Profile {
		        START_FIRST,
		        START_NEW,
		        ADD_TO_CURRENT;
		    	
		    	public int getValue() {
		    		return ordinal();
		    	}
		    };

  protected
    //! list of profile nodes
    final SoNodeList          profiles = new SoNodeList();


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    profiles.truncate(0);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds the given profile to the list of profiles
//
// Use: public

public static void add(SoState state, SoProfile profile)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileElement    elt;

    elt = (SoProfileElement ) getElement(state, getClassStackIndex(SoProfileElement.class));

    if (elt != null && profile != null) {

        // append the given profile node to the list of profiles in the
        // element.  If the directive on the profile is START_FIRST,
        // truncate the profile list before adding this one.
        if (profile.linkage.getValue() == Profile.START_FIRST.getValue()) {
            elt.profiles.truncate(0);
            elt.clearNodeIds();
        }
        elt.profiles.append(profile);
        elt.addNodeId(profile);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the current profile node
//
// Use: public

public static SoNodeList get(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileElement elt;
    elt = ( SoProfileElement )
        getConstElement(state, getClassStackIndex(SoProfileElement.class));

    return elt.profiles;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override push to copy the existing profiles from the previous
//    set.
//
// Use: public

public void push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileElement elt;
    elt = (SoProfileElement ) getNextInStack();
    
    // Rely on SoNodeList::operator = to do the right thing...
    profiles.copyFrom( elt.profiles);
    nodeIds.copyFrom( elt.nodeIds);
}

}
