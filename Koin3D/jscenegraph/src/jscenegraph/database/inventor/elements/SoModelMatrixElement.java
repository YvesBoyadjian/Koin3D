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
 |      This file defines the SoModelMatrixElement class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoModelMatrixElement
///  \ingroup Elements
///
///  Element that stores the current model matrix - the cumulative
///  transformation applied to subsequent shapes. Because the matrix is
///  cumulative, this class is derived from SoAccumulatedElement. The
///  set() method replaces the current matrix, while all the others
///  (mult(), translateBy(), etc.) multiply into it. Node id's of the
///  nodes that affect the element are accumulated properly.
///
///  This element also stores the current view-volume culling
///  transformation, which is normally the view*projection matrices
///  (set by cameras), but which may be modified by sophisticated
///  culling schemes.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoModelMatrixElement extends SoAccumulatedElement {

	   final     SbMatrix            modelMatrix = new SbMatrix();
	   private final	        SbMatrix            cullMatrix = new SbMatrix();
	   private final        SbMatrix            modelCullMatrix = new SbMatrix();
	   private final Flags flags = new Flags();
	   private class Flags {
		            public boolean    isModelIdentity;
		            public boolean    haveCullMatrix; //!< true if cullMatrix set
		            public boolean    haveModelCull; //!< true if model*cull computed
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
    modelMatrix.makeIdentity();
    flags.isModelIdentity = true;
    flags.haveCullMatrix = false;
    flags.haveModelCull = false;
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
    SoModelMatrixElement mtxElt = (SoModelMatrixElement ) getNextInStack();

    modelMatrix.copyFrom( mtxElt.modelMatrix);
    flags.isModelIdentity  = mtxElt.flags.isModelIdentity;
    flags.haveCullMatrix = false;
    flags.haveModelCull = false;
    nodeIds.copy(mtxElt.nodeIds);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets model matrix to identity matrix in element accessed from state.
//
// Use: public, static

public static void
makeIdentity(SoState state, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.makeEltIdentity();

    // Update node id list in element
    elt.setNodeId(node);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets model matrix to given matrix in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, SoNode node,
                          final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.setElt(matrix);

    // Update node id list in element
    elt.setNodeId(node);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    "pushes" the current matrix.  This is used by TransformSeparator
//    to efficiently save/restore only the matrix state.  For caching
//    to work properly, the caller MUST NOT do anything with the
//    matrix besides pass it in to the ::popMatrix call.
//
// Use: public, static

public static SbMatrix
pushMatrix(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // This is NOT equivalent to a ::get, so we don't use the
    // SoElement::getConstElement method but instead use the
    // state.getElementNoPush method:
    elt = (SoModelMatrixElement ) 
        state.getElementNoPush(classStackIndexMap.get(SoModelMatrixElement.class));

    return elt.pushMatrixElt();
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
    SoModelMatrixElement        elt;

    // This is NOT equivalent to a ::get, so we don't use the
    // SoElement::getConstElement method but instead use the
    // state.getElementNoPush method:
    elt = (SoModelMatrixElement ) 
        state.getElementNoPush(classStackIndexMap.get(SoModelMatrixElement.class));

    elt.popMatrixElt(matrix);
}

	   
	
		   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets model matrix in element to identity matrix.
	   //
	   // Use: protected, virtual
	   
	  protected void
	   makeEltIdentity()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       modelMatrix.makeIdentity();
	       flags.isModelIdentity = true;
	       flags.haveModelCull = false;
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns model matrix from state, sets given flag to true if
//    matrix is known to be identity.
//
// Use: public, static

public static SbMatrix 
get(SoState state, final boolean[] isIdent)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement elt;

    elt = (SoModelMatrixElement )
        getConstElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    isIdent[0] = elt.flags.isModelIdentity;

    return elt.modelMatrix;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates by the given rotation.
//
// Use: public, static

public static void
rotateBy(SoState state, SoNode node,
                               final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.rotateEltBy(rotation);

    // Update node id list in element
    elt.addNodeId(node);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates model matrix in element by the given rotation.
//
// Use: public, virtual

public void
rotateEltBy(final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    final SbMatrix    m = new SbMatrix();

    rotation.getValue(m);
    modelMatrix.multLeft(m);

    flags.isModelIdentity  = false;     // Assume the worst
    flags.haveModelCull = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies the given matrix into the model matrix
//
// Use: public, static

public static void
mult(SoState state, SoNode node,
                           final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.multElt(matrix);

    // Update node id list in element
    elt.addNodeId(node);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets model matrix in element to given matrix.
//
// Use: protected, virtual

protected void
setElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    modelMatrix.copyFrom(matrix);
    flags.isModelIdentity  = false;     // Assume the worst
    flags.haveModelCull = false;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies into model matrix in element.
//
// Use: protected, virtual

protected void
multElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    modelMatrix.multLeft(matrix);
    flags.isModelIdentity  = false;     // Assume the worst
    flags.haveModelCull = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates by the given vector.
//
// Use: public, static

public static void
translateBy(SoState state, SoNode node,
                                  final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.translateEltBy(translation);

    // Update node id list in element
    elt.addNodeId(node);
}


	  ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates model matrix in element by the given vector.
//
// Use: public, virtual

public void
translateEltBy(final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // It's faster to just add to the translation elements of the
    // matrix than to multiply two matrices...

    for (i = 0; i < 3; i++)
        modelMatrix.getValue()[3][i] += (modelMatrix.getValue()[0][i] * translation.getValue()[0] +
                              modelMatrix.getValue()[1][i] * translation.getValue()[1] +
                              modelMatrix.getValue()[2][i] * translation.getValue()[2]);

    flags.isModelIdentity  = false;     // Assume the worst
    flags.haveModelCull = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales by the given factors.
//
// Use: public, static

public static void
scaleBy(SoState state, SoNode node,
                              final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.scaleEltBy(scaleFactor);

    // Update node id list in element
    elt.addNodeId(node);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales model matrix in element by the given factors.
//
// Use: public, virtual

public void
scaleEltBy(final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // It's faster to just multiply into the elements of the
    // matrix than to multiply two matrices...

    for (i = 0; i < 4; i++) {
        modelMatrix.getValue()[0][i] *= scaleFactor.getValue()[0];
        modelMatrix.getValue()[1][i] *= scaleFactor.getValue()[1];
        modelMatrix.getValue()[2][i] *= scaleFactor.getValue()[2];
    }

    flags.isModelIdentity  = false;     // Assume the worst
    flags.haveModelCull = false;
}


	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns model matrix from state.
//
// Use: public, static

public static SbMatrix 
get(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    final SoModelMatrixElement elt;

    elt = ( SoModelMatrixElement )
        getConstElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    return elt.modelMatrix;
}
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets cull matrix to given matrix
//
// Use: public, static

public static void
setCullMatrix(SoState state, SoNode node,
                                    final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement        elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoModelMatrixElement ) getElement(state, classStackIndexMap.get(SoModelMatrixElement.class));

    elt.cullMatrix.copyFrom(matrix);
    elt.flags.haveCullMatrix = true;

    // Update node id list in element
    elt.addNodeId(node);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get combined model+cull matrices
//
// Use: public, static

public static SbMatrix 
getCombinedCullMatrix(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement  elt;

    elt = ( SoModelMatrixElement ) getConstElement(state,
                                                         classStackIndexMap.get(SoModelMatrixElement.class));
    if (!elt.flags.haveModelCull) {
        // Cast const away:
        SoModelMatrixElement m_elt = (SoModelMatrixElement )elt;

        if (!elt.flags.haveCullMatrix) {
            // Grab cull matrix from one of our parent elements:
            SoModelMatrixElement parent = 
                ( SoModelMatrixElement )elt.getNextInStack();

            while ( (parent != null) && !parent.flags.haveCullMatrix)
                parent = 
                    ( SoModelMatrixElement )parent.getNextInStack();

            if (parent != null) {
                m_elt.cullMatrix.copyFrom(parent.cullMatrix);
                m_elt.flags.haveCullMatrix = true;
            }
            else {
                // Uh-oh, no cull matrix.
//#ifdef DEBUG
                SoDebugError.post("SoModelMatrixElement::getCombinedCullMatrix",
                                   "No cull matrix set (culling Separator "+
                                   "traversed before camera)");
//#endif
                m_elt.cullMatrix.copyFrom(SbMatrix.identity());
            }
        }
//#ifdef DEBUG
        if (SoDebug.GetEnv("IV_DEBUG_MATRIX_CULL") != null)
            SoDebug.RTPrintf("  M*VP calculated\n");
//#endif

        m_elt.modelCullMatrix.copyFrom(elt.modelMatrix.operator_mul(elt.cullMatrix));
        m_elt.flags.haveModelCull = true;
    }
//#ifdef DEBUG
    else if (SoDebug.GetEnv("IV_DEBUG_MATRIX_CULL") != null)
        SoDebug.RTPrintf("  MVP valid\n");
//#endif

    return elt.modelCullMatrix;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to return FALSE if flags.haveCullMatrix is TRUE.
//    That way, culling always breaks caches.
//
// Use: public

public boolean
matches( SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    SoModelMatrixElement  mmElt = ( SoModelMatrixElement ) elt;

    if (mmElt.flags.haveCullMatrix)
        return false;

    return (super.matches(elt));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual method that does matrix push.  GL version overrides to
//    make glPushMatrix call:
//
// Use: public, static

public SbMatrix
pushMatrixElt()
//
////////////////////////////////////////////////////////////////////////
{
    return new SbMatrix(modelMatrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual method that does matrix pop.  GL version overrides to
//    make glPopMatrix call:
//
// Use: public, static

public void
popMatrixElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    modelMatrix.copyFrom(matrix);
}



}
