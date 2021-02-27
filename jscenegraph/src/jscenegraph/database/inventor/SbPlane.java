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
 |   $Revision: 1.3 $
 |
 |   Description:
 |      This file contains definitions of various linear algebra classes,
 |      such as vectors, coordinates, etc..
 |
 |   Classes:
 |      SbVec3f
 |      SbVec2f
 |      SbVec2s
 |      SbVec3s         //!< Extension to SGI OIV 2.1
 |      SbVec4f
 |      SbRotation
 |      SbMatrix
 |      SbViewVolume
 |
 |      SbLine
 |      SbPlane
 |      SbSphere
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, 
 |                        David Mott, Alain Dumesny
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Oriented plane in 3D.
/*!
\class SbPlane
\ingroup Basics
Represents an oriented plane in 3D. This is a lightweight
class/datatype that is used for arguments to some Inventor objects. 
{}

\par See Also
\par
SbVec3f, SbLine
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbPlane implements Mutable {
	
	   private final SbVec3f     normalVec = new SbVec3f();
		    
		        private float       distance;
		   	
        // Default constructor. 
        public SbPlane() {
        	
        }

        /**
         * Construct a plane given three points. p0, p1, and p2 represent 
         * three points in the plane. 
         */
        public SbPlane( final SbVec3f p0, final SbVec3f p1, final SbVec3f p2)
         {
             normalVec.copyFrom((p1.operator_minus(p0)).cross(p2.operator_minus(p0)));
            normalVec.normalize();
             distance = normalVec.dot(p0);
         }

        //! Construct a plane given normal and distance from origin along normal.
        //! Orientation is given by the normal vector n.
        public SbPlane(final SbVec3f n, float d) {
            normalVec.copyFrom(n);
            normalVec.normalize();
            distance = d;        	
        }

        /**
	 * Construct a plane given normal and a point to pass through 
	 * Orientation is given by the normal vector n. 
	 * 
	 * @param n
	 * @param p
	 */
	 // construct a plane given normal and a point to pass through
	    public SbPlane( final SbVec3f n, final SbVec3f p)
	    {
	        normalVec.copyFrom(n);
	        normalVec.normalize();
	        distance = normalVec.dot(p);
	    }


// offset a plane by a given distance
public void
offset(float d)
{
    distance += d;
}

	    

	@Override
	public void copyFrom(Object other) {
		SbPlane sbPlane = (SbPlane)other;
		normalVec.copyFrom(sbPlane.normalVec);
		distance = sbPlane.distance;
	}
	
	/**
	 * Intersect line and plane, returning TRUE if there is an intersection, 
	 * FALSE if line is parallel to plane. 
	 * 
	 * @param l
	 * @param intersection
	 * @return
	 */
	 // intersect line and plane
	 public boolean intersect(final SbLine l, final SbVec3f intersection) {
	     float t, denom;
	      
	          // solve for t:
	         //  n . (l.p + t * l.d) - d == 0
	     
	         denom = normalVec.dot(l.getDirection());
	         if ( denom == 0.0 )
	             return false;
	     
	         //  t = - (n . l.p - d) / (n . l.d)
	         t = - (normalVec.dot(l.getPosition()) - distance) /  denom;
	     
	         intersection.copyFrom(l.getPosition().operator_add(l.getDirection().operator_mul(t)));
	         return true;	    	
	}

	 // Returns normal vector to plane. 
	 public SbVec3f getNormal() {
		 return normalVec; 
	 }

	 //! Returns distance from origin to plane.
    public float               getDistanceFromOrigin()    { return distance; }


// transform a plane by the given matrix
public void
transform( SbMatrix matrix)
{
    // Find the point on the plane along the normal from the origin
    final SbVec3f     point = normalVec.operator_mul(distance);

    // Transform the plane normal by the matrix
    // to get the new normal. Use the inverse transpose
    // of the matrix so that normals are not scaled incorrectly.
    final SbMatrix invTran = matrix.inverse().transpose();
    invTran.multDirMatrix(normalVec, normalVec);
    normalVec.normalize();

    // Transform the point by the matrix
    matrix.multVecMatrix(point, point);

    // The new distance is the projected distance of the vector to the
    // transformed point onto the (unit) transformed normal. This is
    // just a dot product.
    distance = point.dot(normalVec);
}

	 
// Returns TRUE if the given point is within the half-space defined by
// the plane
public boolean
isInHalfSpace(final SbVec3f point)
{
    // Multiply point by plane equation coefficients, compare distances
    return point.dot(normalVec) >= distance;
}

/*!
  Return the distance from \a point to plane. Positive distance means
  the point is in the plane's half space.

  This method is an extension specific to Coin versus the original SGI
  Inventor API.
*/
public float
getDistance(SbVec3f point)
{
  // convert to double before doing the dot product to increase precision of the result
  final SbVec3d dp = new SbVec3d(point);
  final SbVec3d dn = new SbVec3d(this.normalVec);
  return (float) (dp.dot(dn)) - this.distance;
}


}
