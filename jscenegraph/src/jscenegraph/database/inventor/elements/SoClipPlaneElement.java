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
 |      This file defines the SoClipPlaneElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoClipPlaneElement
///  \ingroup Elements
///
///  Element that stores the current set of clipping planes, specified
///  as SbPlanes.
///
///  When a plane is added, this element gets the current model matrix
///  from the state and stores it in the instance. This allows the
///  get() method to return the clip plane in object space (the plane
///  as originally defined) or world space (after being transformed by
///  the model matrix).
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoClipPlaneElement extends SoAccumulatedElement {
	
//
// This structure contains all information needed for a clipping
// plane. Pointers to instances of this structure are stored in the
// "planes" SbPList.
//

	private class So_ClipPlane {
    final SbPlane     objPlane = new SbPlane();       // World-space clipping plane
    final SbPlane     worldPlane = new SbPlane();     // World-space clipping plane
    final SbMatrix    objToWorld = new SbMatrix();     // Converts object space to world space
    boolean      worldPlaneValid;// TRUE if worldPlane was computed
    
    public void destructor() {
    	// nothing to do
    }
};

	

  protected
    final SbPList             planes = new SbPList();         //!< List of plane structures
    protected int                 startIndex;     //!< Index of 1st plane created
                                        //! in this instance
	
    

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
    planes.truncate(0);
    startIndex = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds a clip plane to the current set in the state.
//
// Use: public, static

public static void
add(SoState state, SoNode node, final SbPlane plane)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlaneElement  elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoClipPlaneElement ) getElement(state, classStackIndexMap.get(SoClipPlaneElement.class));

    if (elt != null) {
        elt.addToElt(state, plane, SoModelMatrixElement.get(state));

        // Update node id list in element
        elt.addNodeId(node);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override push to copy the existing planes from the previous
//    element.
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlaneElement elt =
        ( SoClipPlaneElement ) getNextInStack();

    // Use SbPList::operator = to copy the pointers to the existing
    // planes. Since the previous element can't be destroyed before
    // this one is, there is no problem with using pointers to
    // existing plane structures. However, any new ones we add have to
    // be freed up when this instance goes away, so we save the
    // starting index to allow us to fix this in pop().
    planes.operator_equal(elt.planes);
    startIndex = planes.getLength();
    nodeIds.operator_equal(elt.nodeIds);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override pop to free up plane structures that were created in
//    the instance that was removed from the state.
//
// Use: public

public void
pop(SoState state, SoElement prevTopElement)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlaneElement prevElt =
        ( SoClipPlaneElement ) prevTopElement;

    // Free up any plane structures that were created by prevElt
    for (int i = prevElt.startIndex; i < prevElt.planes.getLength(); i++)
        ((So_ClipPlane ) prevElt.planes.operator_square_bracket(i)).destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Prints element for debugging.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

//#ifdef DEBUG
//void
//SoClipPlaneElement::print(FILE *fp) const
//{
//    SoAccumulatedElement::print(fp);
//}
//#else  /* DEBUG */
//void
//SoClipPlaneElement::print(FILE *) const
//{
//}
//#endif /* DEBUG */

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds clipping plane to element.
//
// Use: protected, virtual

protected void
addToElt(SoState state, final SbPlane plane,
                             final SbMatrix modelMatrix)
//
////////////////////////////////////////////////////////////////////////
{
    So_ClipPlane newPlane = new So_ClipPlane();

    newPlane.objPlane.copyFrom(plane);
    newPlane.objToWorld.copyFrom(modelMatrix);
    newPlane.worldPlaneValid   = false;

    planes.append(newPlane);
}
    
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the first (top) instance of the element in the state.
//
// Use: public, static

public static SoClipPlaneElement 
getInstance(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    return ( SoClipPlaneElement )
        getConstElement(state, classStackIndexMap.get(SoClipPlaneElement.class));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the number of planes in the element
//
// Use: public

public int
getNum() 
//
////////////////////////////////////////////////////////////////////////
{
    return planes.getLength();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the nth plane stored in an instance in object or world
//    space.
//
// Use: public

public SbPlane 
get(int index, boolean inWorldSpace)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (index < 0 || index >= planes.getLength())
        SoDebugError.post("SoClipPlaneElement::get",
                           "Index ("+index+") is out of range 0 - "+(planes.getLength() - 1));
//#endif /* DEBUG */

    So_ClipPlane plane = (So_ClipPlane ) planes.operator_square_bracket(index);

    if (! inWorldSpace)
        return plane.objPlane;

    // Transform plane into world space if necessary
    if (! plane.worldPlaneValid) {
        plane.worldPlane.copyFrom(plane.objPlane);
        plane.worldPlane.transform(plane.objToWorld);
    }
    return plane.worldPlane;
}


}
