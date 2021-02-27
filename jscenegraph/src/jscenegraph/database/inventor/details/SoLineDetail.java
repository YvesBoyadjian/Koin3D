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
 |      This file defines the SoLineDetail class.
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
//! Stores detail information about vertex-based shapes made of line segments.
/*!
\class SoLineDetail
\ingroup Details
This class contains detail information about a point on a line segment
in a vertex-based shape made of line segments. The information
includes the points at the ends of the segment, and the index of the
segment within the shape.

\par See Also
\par
SoDetail, SoPickedPoint, SoPrimitiveVertex, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoLineDetail extends SoDetail {
	
	  private
	    static final SoType       classTypeId = new SoType();            //!< Type identifier
	  
	    //! Returns type identifier for this class.
	   public static SoType       getClassTypeId() { return new SoType(classTypeId); }

	    //! Returns the type identifier for a specific instance.
	   public SoType      getTypeId() {
		   return classTypeId;
	   }

	
	
  private
    final SoPointDetail[]               point = new SoPointDetail[2];
    int                     lineIndex, partIndex;
	
    public SoLineDetail() {
    	point[0] = new SoPointDetail();
    	point[1] = new SoPointDetail();
    }

    //! These return information about the two points forming the end vertices of
    //! the line segment, represented as an SoPointDetail.
    public SoPointDetail        getPoint0()        { return point[0]; }
    //! These return information about the two points forming the end vertices of
    //! the line segment, represented as an SoPointDetail.
    public SoPointDetail        getPoint1()        { return point[1]; }

    //! Returns the index of the line the segment is part of within a shape,
    //! such as the third line within an SoLineSet.
    public int                     getLineIndex()     { return lineIndex; }

    //! Returns the index of the part containing the line segment within the
    //! shape. Usually, the part index is the same as the line segment index,
    //! such as the fifth segment overall within an SoLineSet.
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
    SoLineDetail newDetail = new SoLineDetail();

    newDetail.point[0]  = point[0];
    newDetail.point[1]  = point[1];
    newDetail.lineIndex = lineIndex;
    newDetail.partIndex = partIndex;

    return newDetail;
}

    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies a point detail from the given detail.
//
// Use: extender
//

public void
setPoint0(final SoPointDetail pd)
//
////////////////////////////////////////////////////////////////////////
{
    point[0].copyFrom(pd);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies a point detail from the given detail.
//
// Use: extender
//

public void
setPoint1(final SoPointDetail pd)
//
////////////////////////////////////////////////////////////////////////
{
    point[1].copyFrom(pd);
}
    
    //! Sets the line index and part index
    public void                        setLineIndex(int i) { lineIndex = i; }
    public void                        setPartIndex(int i) { partIndex = i; }

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
    //SO_DETAIL_INIT_CLASS(SoLineDetail, SoDetail);
    classTypeId.copyFrom( SoType.createType(SoDetail.getClassTypeId(),           
            new SbName("SoLineDetail"), null));
}


/*!
  Convenience method for library client code when setting up a
  SoLineDetail instance to use the line index as a counter.
*/
public void
incLineIndex()
{
  this.lineIndex++;
}

/*!
  Convenience method for library client code when setting up a
  SoLineDetail instance to use the part index as a counter.
*/
public void
incPartIndex()
{
  this.partIndex++;
}
    
}
