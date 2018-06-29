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
 |   $Revision $
 |
 |   Description:
 |      This file defines the SoProfileCoordinateElement class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoProfileCoordinateElement
///  \ingroup Elements
///
///  Element storing the current profile coordinates, as either 2-D or
///  3-D points.
///
///  This class allows read-only access to the top element in the state
///  to make accessing several values in it more efficient. Individual
///  values must be accessed through this instance.
///
//////////////////////////////////////////////////////////////////////////////

public class SoProfileCoordinateElement extends SoReplacedElement {

  protected
    int             numCoords;
  protected  SbVec2f[]       coords2;
  protected  SbVec3f[]       coords3;
  protected boolean                coordsAre2D;

  private
	    static SbVec2f      defaultCoord2;

  private final SbVec2f             convert2 = new SbVec2f();       //!< To convert from 3-D to 2-D
  private final SbVec3f             convert3 = new SbVec3f();       //!< To convert from 2-D to 3-D
	
    //! Returns the number of coordinate points in an instance
    public int             getNum()           { return numCoords; }

    //! TRUE if set2() was called.
    public boolean                is2D() { return coordsAre2D; }

    //! Returns the default profile coordinate
    public static SbVec2f      getDefault2() { return new SbVec2f(0.0f, 0.0f); }
    public static SbVec3f      getDefault3() { return new SbVec3f(0.0f, 0.0f, 1.0f); }

    
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
    super.init(state);

    // Initialize default coordinate storage if not already done
    if (defaultCoord2 == null) {
        defaultCoord2  = new SbVec2f();
        defaultCoord2.copyFrom(getDefault2());
    }

    // Assume 2D until told otherwise
    coordsAre2D = true;
    coords2     = new SbVec2f[1]; coords2[0] = defaultCoord2;
    numCoords   = 1;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the 2-D coordinates in element accessed from state.
//
// Use: public

public static void set2(SoState state, SoNode node,
                          int numCoords, final SbVec2f[] coords)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinateElement  elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoProfileCoordinateElement )
        getElement(state, classStackIndexMap.get(SoProfileCoordinateElement.class), node);

    if (elt != null) {
        elt.numCoords   = numCoords;
        elt.coords2     = coords;
        elt.coordsAre2D = true;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the 3-D coordinates in element accessed from state.
//
// Use: public

public static void set3(SoState state, SoNode node,
                          int numCoords, final SbVec3f[] coords)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinateElement  elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoProfileCoordinateElement )
        getElement(state, classStackIndexMap.get(SoProfileCoordinateElement.class), node);

    if (elt != null) {
        elt.numCoords   = numCoords;
        elt.coords3     = coords;
        elt.coordsAre2D = false;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the top (current) instance of the element in the state.
//
// Use: public

public static SoProfileCoordinateElement getInstance(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    return ( SoProfileCoordinateElement )
        getConstElement(state, classStackIndexMap.get(SoProfileCoordinateElement.class));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the indexed coordinate from an element as an SbVec2f.
//
// Use: public

public SbVec2f get2(int index)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (index < 0 || index >= numCoords)
        SoDebugError.post("SoProfileCoordinateElement::get2",
                           "Index ("+index+") is out of range 0 - "+(numCoords - 1));
//#endif /* DEBUG */

    if (coordsAre2D)
        return coords2[index];

    // Convert from 3-D if necessary
    else {
        // Cast the const away...
        SoProfileCoordinateElement elt = (SoProfileCoordinateElement ) this;
        final SbVec3f           c3  = coords3[index];

        elt.convert2.getValue()[0] = c3.getValue()[0] / c3.getValue()[2];
        elt.convert2.getValue()[1] = c3.getValue()[1] / c3.getValue()[2];

        return convert2;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the indexed coordinate from an element as an SbVec3f.
//
// Use: public

public SbVec3f get3(int index)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (index < 0 || index >= numCoords)
        SoDebugError.post("SoProfileCoordinateElement::get3",
                           "Index ("+index+") is out of range 0 - "+(numCoords - 1));
//#endif /* DEBUG */

    // Convert from 2-D if necessary
    if (coordsAre2D) {
        // Cast the const away...
        SoProfileCoordinateElement elt = (SoProfileCoordinateElement ) this;
        final SbVec2f           c2  = coords2[index];

        elt.convert3.getValue()[0] = c2.getValue()[0];
        elt.convert3.getValue()[1] = c2.getValue()[1];
        elt.convert3.getValue()[2] = 0.0f;

        return convert3;
    }

    else
        return coords3[index];
}

    
}
