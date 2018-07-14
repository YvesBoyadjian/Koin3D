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
 |      This file defines the base SoPrimitiveVertex class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.database.inventor.details.SoDetail;


////////////////////////////////////////////////////////////////////////////////
//! Represents a vertex of a generated primitive.
/*!
\class SoPrimitiveVertex
\ingroup General
An SoPrimitiveVertex represents a vertex of a primitive (triangle,
line segment, or point) that is being generated by an
SoCallbackAction. It contains an object-space point, normal,
texture coordinates, material index, and a pointer to an instance of
an SoDetail subclass. This detail may contain more information
about the vertex, or may be a NULL pointer if there is no such info.


Instances of SoPrimitiveVertex are typically created on the stack
by shape classes while they are generating primitives. Anyone who
wants to save them as return values from SoCallbackAction should
probably make copies of them.

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPrimitiveVertex {

    //! These return the surface point, normal, and texture coordinates in
    //! object space.
    public SbVec3f      getPoint()                 { return point; }
    //! These return the surface point, normal, and texture coordinates in
    //! object space.
    public SbVec3f      getNormal()                { return normal; }
    //! These return the surface point, normal, and texture coordinates in
    //! object space.
    public SbVec4f      getTextureCoords()         { return texCoords; }

    //! Returns the index into the current set of materials of the material
    //! active at the vertex.
    public int                 getMaterialIndex()     { return materialIndex; }

    //! Returns the detail giving more information about the vertex. Note that
    //! this pointer may be NULL if there is no more info.
    public SoDetail     getDetail()                { return detail; }



    //! These methods are typically called by shape classes during
    //! primtiive generation

    //! These set the object space point, normal, and texture coordinates:
    public void                setPoint(final SbVec3f pt)       { point.copyFrom(pt); }
    public void                setNormal(final SbVec3f norm)    { normal.copyFrom(norm); }
    public void                setTextureCoords(final SbVec4f t){ texCoords.copyFrom(t); }
    public void setTextureCoords(final SbVec2f tex) { /*textureCoords*/texCoords.setValue(tex.getValueRead()[0], tex.getValueRead()[1], 0.0f, 1.0f); } // COIN3D

    //! Sets the material index. The index is set to 0 during construction.
    public void                setMaterialIndex(int index)  { materialIndex = index; }

    //! Sets the detail corresponding to the vertex. The pointer may be
    //! NULL, although it is set to NULL during construction.
    public void                setDetail(SoDetail d)            { detail = d; }

  private
    final SbVec3f             point = new SbVec3f();          //!< Object-space point
    private final SbVec3f             normal = new SbVec3f();         //!< Object-space normal
    private final SbVec4f             texCoords = new SbVec4f();      //!< Object-space texture coordinates
    private int                 materialIndex;  //!< Material index
    private SoDetail            detail;        //!< Extra detail info

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoPrimitiveVertex()
//
////////////////////////////////////////////////////////////////////////
{
    // Initialize material index to 0, the most common value
    materialIndex = 0;

    // Initialize detail pointer to NULL
    detail = null;
}

    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy constructor
//
// Use: public

public SoPrimitiveVertex(final SoPrimitiveVertex pv)
//
////////////////////////////////////////////////////////////////////////
{
    operator_equal(pv);
}


    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy assignment operator.
//
// Use: public

public SoPrimitiveVertex 
operator_equal(final SoPrimitiveVertex pv)
//
////////////////////////////////////////////////////////////////////////
{
    point         .copyFrom( pv.point);
    normal        .copyFrom( pv.normal);
    texCoords     .copyFrom( pv.texCoords);
    materialIndex = pv.materialIndex;
    detail        = pv.detail;

    return this;
}

public void copyFrom(final SoPrimitiveVertex pv) {
	operator_equal(pv);
}
public void destructor() {
	// nothing to destroy
}
}
