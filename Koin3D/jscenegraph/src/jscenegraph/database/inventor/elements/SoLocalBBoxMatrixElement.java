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
 |      This file defines the SoLocalBBoxMatrixElement class.
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


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoLocalBBoxMatrixElement
///  \ingroup Elements
///
///  Element that stores the transformation matrix from object space to
///  some local coordinate space during application of an
///  SoGetBoundingBoxAction. This element is needed to allow separators
///  (and other caching nodes) to store cached bounding boxes in their
///  own local space. Shapes that extend the current bounding box
///  computed by the action use this matrix to transform the bounding
///  box into local space. Separators cache this box, then transform it
///  into the next local space up the hierarchy.
///
///  Because this element is set to identity by separators before
///  traversing their children, it can never have an affect on any
///  caching separator that it is not under. Therefore, it should
///  never be tested for cache invalidation, so its matches() method
///  prints an error message and returns FALSE. It also means that it
///  does not have to save node id's so there are no nodes passed in to
///  any of the methods.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves
 *
 */
public class SoLocalBBoxMatrixElement extends SoElement {
	
	   protected final SbMatrix            localMatrix = new SbMatrix();
		    	
  private
    //! This stores the inverse of the current model matrix at the time
    //! the element is created. It is needed in case the element is set
    //! to contain a new matrix, since we want to factor out the
    //! effects of the non-local part of the matrix.
    final SbMatrix            modelInverseMatrix = new SbMatrix();
	   
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
    localMatrix.makeIdentity();
    modelInverseMatrix.makeIdentity();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying matrix from previous top instance.
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    localMatrix.copyFrom(((SoLocalBBoxMatrixElement ) getNextInStack()).localMatrix);

    // Update the modelInverseMatrix to contain the inverse of the
    // current model matrix.  We don't any caching dependencies to be
    // created, so we do this by using the state method and not the
    // element method:
    SoModelMatrixElement mme = ( SoModelMatrixElement )
        state.getConstElement(SoModelMatrixElement.getClassStackIndex(SoModelMatrixElement.class));
    modelInverseMatrix.copyFrom( mme.modelMatrix.inverse());
}


	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to print an error message and return
//    FALSE. See the class header comment for details.
//
// Use: public

public boolean
matches( SoElement element)
//
////////////////////////////////////////////////////////////////////////
{
    SoDebugError.post("SoLocalBBoxMatrixElement::matches",
                       "This method should never be called!");

    return false;
}


	@Override
	public SoElement copyMatchInfo() {
	     SoDebugError.post("SoLocalBBoxMatrixElement::copyMatchInfo",
	    		                          "This method should never be called!");
	    		   
	    		       return null;	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns local matrix from state.
	   //
	   // Use: public, static
	   
	  public static SbMatrix 
	   get(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoLocalBBoxMatrixElement elt;
	   
	       elt = ( SoLocalBBoxMatrixElement )
	           getConstElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));
	   
	       return elt.localMatrix;
	   }
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Resets all current instances in the state to identity.
	   //
	   // Use: public, static
	   
	  public static void
	   resetAll(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoLocalBBoxMatrixElement    elt;
	   
	       // Get an instance we can change (pushing if necessary)
	       elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));
	   
	       while (elt != null) {
	           elt.localMatrix.makeIdentity();
	           elt = (SoLocalBBoxMatrixElement ) elt.getNextInStack();
	       }
	   }
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets local matrix to identity matrix in element accessed from state.
	   //
	   // Use: public, static
	   
	  public static void
	   makeIdentity(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoLocalBBoxMatrixElement    elt;
	   
	       // Get an instance we can change (pushing if necessary)
	       elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));
	   
	       elt.localMatrix.makeIdentity();
	   }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets local matrix to given matrix in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    // Multiply by the inverse of the current model matrix to cancel
    // out any non-local matrix effects
    elt.localMatrix.copyFrom( matrix.operator_mul(elt.modelInverseMatrix));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies the given matrix into the local matrix
//
// Use: public, static

public static void
mult(SoState state, final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    elt.localMatrix.multLeft(matrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates by the given vector.
//
// Use: public, static

public static void
translateBy(SoState state,
                                      final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;
    int                         i;

    // Get an instance we can change (pushing if necessary)
    elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    for (i = 0; i < 3; i++)
        elt.localMatrix.getValue()[3][i] +=
            (elt.localMatrix.getValue()[0][i] * translation.getValue()[0] +
             elt.localMatrix.getValue()[1][i] * translation.getValue()[1] +
             elt.localMatrix.getValue()[2][i] * translation.getValue()[2]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates by the given rotation.
//
// Use: public, static

public static void
rotateBy(SoState state, final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    final SbMatrix    m = new SbMatrix();
    rotation.getValue(m);
    elt.localMatrix.multLeft(m);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales by the given factors.
//
// Use: public, static

public static void
scaleBy(SoState state, final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;
    int                         i;

    // Get an instance we can change (pushing if necessary)
    elt = (SoLocalBBoxMatrixElement ) getElement(state, classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    // It's faster to just multiply into the elements of the
    // matrix than to multiply two matrices...

    for (i = 0; i < 4; i++) {
        elt.localMatrix.getValue()[0][i] *= scaleFactor.getValue()[0];
        elt.localMatrix.getValue()[1][i] *= scaleFactor.getValue()[1];
        elt.localMatrix.getValue()[2][i] *= scaleFactor.getValue()[2];
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    "pushes" the current matrix.  This is used by TransformSeparator
//    to efficiently save/restore only the matrix state.
//
// Use: public, static

public static SbMatrix
pushMatrix(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;

    // This is NOT equivalent to a ::get, so we don't use the
    // SoElement::getConstElement method but instead use the
    // state.getElementNoPush method:
    elt = (SoLocalBBoxMatrixElement ) 
        state.getElementNoPush(classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    return elt.localMatrix;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//   Undoes a pushMatrix().
//
// Use: public, static

public static void
popMatrix(SoState state, final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoLocalBBoxMatrixElement    elt;

    // This is NOT equivalent to a ::get, so we don't use the
    // SoElement::getConstElement method but instead use the
    // state.getElementNoPush method:
    elt = (SoLocalBBoxMatrixElement ) 
        state.getElementNoPush(classStackIndexMap.get(SoLocalBBoxMatrixElement.class));

    elt.localMatrix.copyFrom( matrix);
}

	  
}
