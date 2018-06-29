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
 |      This file defines the SoFaceDetail class.
 |
 |   Author(s)          : Thaddeus Beier, Dave Immel, Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.details;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;


////////////////////////////////////////////////////////////////////////////////
//! Stores detail information about vertex-based shapes made of faces.
/*!
\class SoFaceDetail
\ingroup Details
This class contains detail information about a point on a face in a
vertex-based shape made of faces.  The information includes the number
of points in the face, the points forming the vertices of the face,
and the index of the face within the shape.


Note that when an SoFaceDetail is returned from picking (in an
SoPickedPoint), it will contain details for all points defining
the face that was intersected. However, when an SoFaceDetail is
created for a triangle produced during primitive generation (in an
SoPrimitiveVertex), it will contain details for only the three
vertices of the triangle.

\par See Also
\par
SoDetail, SoPickedPoint, SoPrimitiveVertex, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFaceDetail extends SoDetail {

	
	  private
	    static final SoType       classTypeId = new SoType();            //!< Type identifier
	  
	    //! Returns type identifier for this class.
	   public static SoType       getClassTypeId() { return new SoType(classTypeId); }

	    //! Returns the type identifier for a specific instance.
	   public SoType      getTypeId() {
		   return classTypeId;
	   }

	
	
  private
    SoPointDetail[]               point;
    private int                     numPoints;
    private int                     faceIndex, partIndex;

    //! Returns the number of points in the face.
    public int                     getNumPoints()     { return numPoints; }

    //! Returns information about the point forming the \p i'th vertex of the
    //! face, represented as an SoPointDetail.
    public SoPointDetail        getPoint(int i) { return point[i]; }

    //! Returns the index of the face within the shape.
    public int                     getFaceIndex()     { return faceIndex; }

    //! Returns the index of the part containing the face within the shape.
    public int                     getPartIndex()     { return partIndex; }

    
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns an instance that is a copy of this instance. The caller
//    is responsible for deleting the copy when done.
//
// Use: public, virtual
//

public SoDetail 
copy() 
//
////////////////////////////////////////////////////////////////////////
{
    SoFaceDetail newDetail = new SoFaceDetail();

    newDetail.faceIndex = faceIndex;
    newDetail.partIndex = partIndex;

    newDetail.setNumPoints(numPoints);
    for (int i = 0; i < numPoints; i++)
        newDetail.setPoint(i, point[i]);

    return newDetail;
}

    
	
    //! Sets the face index and part index
    public void                        setFaceIndex(int i) { faceIndex = i; }
    public void                        setPartIndex(int i) { partIndex = i; }

    //! Return a pointer to the point details.
    public SoPointDetail[]             getPoints()             { return point; }

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the number of points in the face and allocates room for the
//    points.
//
// Use: extender
//

public void
setNumPoints(int num)
//
////////////////////////////////////////////////////////////////////////
{
    // Free up old ones if necessary
    if (point != null && numPoints < num) {
        //delete [] point; java port
        point = null;   // So they get allocated below
    }

    // Allocate space if necessary
    if (point == null && num > 0) {
        point = new SoPointDetail[num];
        for(int i=0; i<num; i++) {
        	point[i] = new SoPointDetail();
        }
    }
    numPoints = num;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies a point detail from the given detail.
//
// Use: extender
//

public void
setPoint(int index, final SoPointDetail pd)
//
////////////////////////////////////////////////////////////////////////
{
    point[index].copyFrom(pd);
}
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes class.
//
// Use: internal
//

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    //SO_DETAIL_INIT_CLASS(SoFaceDetail, SoDetail);
    classTypeId.copyFrom( SoType.createType(SoDetail.getClassTypeId(),           
            new SbName("SoFaceDetail"), null));
}


}
