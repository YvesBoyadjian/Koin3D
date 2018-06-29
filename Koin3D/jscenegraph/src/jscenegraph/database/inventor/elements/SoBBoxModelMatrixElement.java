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
 |      This file defines the SoBBoxModelMatrixElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoBBoxModelMatrixElement
///  \ingroup Elements
///
///  Element that stores the current model matrix for use with the
///  SoGetBoundingBoxAction. Overrides the virtual methods on
///  SoModelMatrixElement to also set the current
///  SoLocalBBoxMatrixElement.
///
///  This class relies on SoModelMatrixElement to store the matrix in
///  the instance, and inherits most of its methods from it.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoBBoxModelMatrixElement extends SoModelMatrixElement {

	   private
		        //! Stores pointer to state so we can access the SoLocalBBoxMatrixElement
		        SoState             state;
		    
		        //! This is used to make sure the pushMatrix/popMatrix methods are
		        //! called correctly.
	   private        static boolean       pushPopCallOK;
		   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    // Save pointer to state so we can access the
    // SoLocalBBoxMatrixElement later
    state = _state;

    super.init(_state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying matrix and state pointer from previous
//    top instance.
//
// Use: public

public void
push(SoState _state)
//
////////////////////////////////////////////////////////////////////////
{
    // Save pointer to state so we can access the
    // SoLocalBBoxMatrixElement later
    state = _state;

    super.push(_state);
}

//! Allows the SoGetBoundingBoxAction to reset the current model
//! matrix to identity and all currently-open local matrices to
//! identity

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Resets the top element to identity and also resets all current
//    instances of the SoLocalBBoxMatrixElement to identity. This is
//    used by SoGetBoundingBoxAction to implement its reset path.
//
// Use: public, static

public static void
reset(SoState _state, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoBBoxModelMatrixElement    elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoBBoxModelMatrixElement )
        getElement(_state, classStackIndexMap.get(SoBBoxModelMatrixElement.class));

    if (elt != null) {
        // Reset this element
        elt.makeEltIdentity();

        // Update node id list in element
        elt.setNodeId(node);

        // Reset all open SoLocalBBoxMatrixElement instances
        SoLocalBBoxMatrixElement.resetAll(_state);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

protected void
makeEltIdentity()
//
////////////////////////////////////////////////////////////////////////
{
    super.makeEltIdentity();
    SoLocalBBoxMatrixElement.makeIdentity(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

protected void
setElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    super.setElt(matrix);
    SoLocalBBoxMatrixElement.set(state, matrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

protected void
multElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    super.multElt(matrix);
    SoLocalBBoxMatrixElement.mult(state, matrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

public void
translateEltBy(final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    super.translateEltBy(translation);
    SoLocalBBoxMatrixElement.translateBy(state, translation);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

public void
rotateEltBy(final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    super.rotateEltBy(rotation);
    SoLocalBBoxMatrixElement.rotateBy(state, rotation);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method on SoModelMatrixElement to also update the
//    SoLocalBBoxMatrixElement.
//
// Use: protected, virtual

public void
scaleEltBy(final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    super.scaleEltBy(scaleFactor);
    SoLocalBBoxMatrixElement.scaleBy(state, scaleFactor);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Because two model matrices are kept track of during the
// getBoundingBoxAction (the local model matrix, used by
// Separators to calculate their bbox caches, and the real model
// matrix), replacement routines for pushMatrix/popMatrix must be
// given; TransformSeparator::getBoundingBox uses these methods to
// correctly keep both matrices up-to-date.
//
// Use: public, static

public static void
pushMatrix(SoState state,
                final SbMatrix matrix, final SbMatrix localmatrix)
//
////////////////////////////////////////////////////////////////////////
{
    pushPopCallOK = true;
    matrix.copyFrom(SoModelMatrixElement.pushMatrix(state));
    localmatrix.copyFrom(SoLocalBBoxMatrixElement.pushMatrix(state));
    pushPopCallOK = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//   Undoes a pushMatrix().
//
// Use: public, static

public static void
popMatrix(SoState state,
                final SbMatrix matrix, final SbMatrix localmatrix)
//
////////////////////////////////////////////////////////////////////////
{
    pushPopCallOK = true;
    SoModelMatrixElement.popMatrix(state, matrix);
    SoLocalBBoxMatrixElement.popMatrix(state, localmatrix);
    pushPopCallOK = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method to print error:
//
// Use: protected, virtual

public SbMatrix
pushMatrixElt()
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (!pushPopCallOK)
        SoDebugError.post("SoBBoxModelMatrixElement::pushMatrixElt",
                "Nodes must call SoBBoxModelMatrixElement::pushMatrix"+
                " for getBoundingBox, not SoModelMatrixElement::pushMatrix");
//#endif
    return super.pushMatrixElt();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides method to print error:
//
// Use: protected, virtual

public void
popMatrixElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (!pushPopCallOK)
        SoDebugError.post("SoBBoxModelMatrixElement::popMatrixElt",
               "Nodes must call SoBBoxModelMatrixElement::popMatrix"+
               " for getBoundingBox, not SoModelMatrixElement::popMatrix");
//#endif
    super.popMatrixElt(matrix);
}
	    
	     }
