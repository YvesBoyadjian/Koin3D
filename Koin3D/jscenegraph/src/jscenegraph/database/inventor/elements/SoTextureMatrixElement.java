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
 |      This file defines the SoTextureMatrixElement class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoTextureMatrixElement
///  \ingroup Elements
///
///  Element that stores the current texture matrix - the cumulative
///  transformation applied to subsequent shapes. Because the matrix is
///  cumulative, this class is derived from SoAccumulatedElement. The
///  set() method replaces the current matrix, while all the others
///  (mult(), translateBy(), etc.) multiply into it. Node id's of the
///  nodes that affect the element are accumulated properly.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureMatrixElement extends SoAccumulatedElement {

  protected
    final SbMatrix            textureMatrix = new SbMatrix();



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    textureMatrix.makeIdentity();
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
    SoTextureMatrixElement elt = (SoTextureMatrixElement)getNextInStack();
    textureMatrix.copyFrom(elt.textureMatrix);
    nodeIds.copyFrom(elt.nodeIds);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets Texture matrix to identity matrix in element accessed from state.
//
// Use: public, static

public static void
makeIdentity(SoState state, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureMatrixElement ) getElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    if (elt != null) {
        elt.makeEltIdentity(state);

        // Update node id list in element
        elt.setNodeId(node);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies the given matrix into the Texture matrix
//
// Use: public, static

public static void
mult(SoState state, SoNode node,
                           final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureMatrixElement ) getElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    if (elt != null) {
        elt.multElt(state, matrix);

        // Update node id list in element
        elt.addNodeId(node);
    }
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
    SoTextureMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureMatrixElement ) getElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    if (elt != null) {
        elt.translateEltBy(state, translation);

        // Update node id list in element
        elt.addNodeId(node);
    }
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
    SoTextureMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureMatrixElement ) getElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    if (elt != null) {
        elt.rotateEltBy(state, rotation);

        // Update node id list in element
        elt.addNodeId(node);
    }
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
    SoTextureMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureMatrixElement ) getElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    if (elt != null) {
        elt.scaleEltBy(state, scaleFactor);

        // Update node id list in element
        elt.addNodeId(node);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns Texture matrix from state.
//
// Use: public, static

public static SbMatrix 
get(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureMatrixElement elt;

    elt = ( SoTextureMatrixElement )
        getConstElement(state, classStackIndexMap.get(SoTextureMatrixElement.class));

    return elt.getElt();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets Texture matrix in element to identity matrix.
//
// Use: protected, virtual

protected void
makeEltIdentity(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    textureMatrix.makeIdentity();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies into Texture matrix in element.
//
// Use: protected, virtual

protected void
multElt(SoState state, final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    textureMatrix.multLeft(matrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Translates Texture matrix in element by the given vector.
//
// Use: public, virtual

public void
translateEltBy(SoState state, final SbVec3f translation)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // It's faster to just add to the translation elements of the
    // matrix than to multiply two matrices...

    for (i = 0; i < 3; i++)
        textureMatrix.getValue()[3][i] += (textureMatrix.getValue()[0][i] * translation.getValue()[0] +
                              textureMatrix.getValue()[1][i] * translation.getValue()[1] +
                              textureMatrix.getValue()[2][i] * translation.getValue()[2]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Rotates Texture matrix in element by the given rotation.
//
// Use: public, virtual

public void
rotateEltBy(SoState state, final SbRotation rotation)
//
////////////////////////////////////////////////////////////////////////
{
    final SbMatrix    m = new SbMatrix();

    rotation.getValue(m);
    textureMatrix.multLeft(m);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Scales Texture matrix in element by the given factors.
//
// Use: public, virtual

public void
scaleEltBy(SoState state, final SbVec3f scaleFactor)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // It's faster to just multiply into the elements of the
    // matrix than to multiply two matrices...

    for (i = 0; i < 4; i++) {
        textureMatrix.getValue()[0][i] *= scaleFactor.getValue()[0];
        textureMatrix.getValue()[1][i] *= scaleFactor.getValue()[1];
        textureMatrix.getValue()[2][i] *= scaleFactor.getValue()[2];
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns Texture matrix from element.
//
// Use: protected, virtual

protected SbMatrix 
getElt()
//
////////////////////////////////////////////////////////////////////////
{
    return textureMatrix;
}

}
