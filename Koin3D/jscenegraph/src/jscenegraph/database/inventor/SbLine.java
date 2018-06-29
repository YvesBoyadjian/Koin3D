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
//! Directed line in 3D.
/*!
\class SbLine
\ingroup Basics
Represents a directed line in 3D. This is a basic Inventor datatype that is
used for representing a 3D line. It is used as input and output by a variety of
Inventor classes.
{}

\par See Also
\par
SbVec3f, SbPlane
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbLine implements Mutable {
	
		        //! Parametric description:
		        //!  l(t) = pos + t * dir
	private final SbVec3f     pos = new SbVec3f();
	private final SbVec3f     dir = new SbVec3f();
	
	public SbLine() {
		
	}
	
	// copy constructor (java port)
	public SbLine(final SbLine other) {
		copyFrom(other);
	}
	
	/**
	 * Constructors. 
	 * To construct a line from a position and direction, use: SbLine(p0, p0 + dir). 
	 * The line is directed from p0 to p1. 
	 */
	 // construct a line given 2 points on the line
	   	public SbLine(final SbVec3f p0, final SbVec3f p1) {
	   		setValue(p0, p1);
	}
		    	
	// Sets line to pass through points p0 and p1. 
	 // setValue constructs a line give two points on the line
	 public void setValue(SbVec3f p0, SbVec3f p1) {
	     pos.copyFrom(p0);
	          dir.copyFrom(p1.operator_minus(p0));
	          dir.normalize();
	     		
	}

    //! Returns position of line origin point and direction vector of line.
     public SbVec3f      getPosition()      { return pos; }
     //! Returns position of line origin point and direction vector of line.
     public SbVec3f      getDirection()     { return dir; }

	@Override
	public void copyFrom(Object other) {
		SbLine line = (SbLine)other;
		pos.copyFrom(line.pos);
		dir.copyFrom(line.dir);
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersect the line with a 3D box.  The line is intersected with the
//    twelve triangles that make up the box.
//
// Use: internal

public boolean
intersect(final SbBox3f box, final SbVec3f enter, final SbVec3f exit)
//
////////////////////////////////////////////////////////////////////////
{
    if (box.isEmpty())
        return false;

    final SbVec3f       pos = getPosition(), dir = getDirection();
    final SbVec3f       max = box.getMax(), min = box.getMin();
    final SbVec3f[]             points = new SbVec3f[8];
    points[0] = new SbVec3f();
    points[1] = new SbVec3f();
    points[2] = new SbVec3f();
    points[3] = new SbVec3f();
    points[4] = new SbVec3f();
    points[5] = new SbVec3f();
    points[6] = new SbVec3f();
    points[7] = new SbVec3f();
    		final SbVec3f inter = new SbVec3f(), bary = new SbVec3f();
    // final SbPlane             plane = new SbPlane();
    int                 i, v0 = -1, v1 = -1, v2 = -1;
    final boolean[]              front = new boolean[1]; front[0] = false;
    		boolean  valid, validIntersection = false;

    //
    // First, check the distance from the ray to the center
    // of the box.  If that distance is greater than 1/2
    // the diagonal distance, there is no intersection
    // diff is the vector from the closest point on the ray to the center
    // dist2 is the square of the distance from the ray to the center
    // radi2 is the square of 1/2 the diagonal length of the bounding box
    //
    float    t = (box.getCenter().operator_minus(pos)).dot(dir);
    final SbVec3f  diff = new SbVec3f(pos.operator_add(dir.operator_mul(t)).operator_minus(box.getCenter()));
    float    dist2 = diff.dot(diff);
    float    radi2 = (max.operator_minus(min)).dot(max.operator_minus(min)) * 0.25f;

    if (dist2 > radi2)
        return false;

    // set up the eight coords of the corners of the box
    for(i = 0; i < 8; i++)
        points[i].setValue((i & 01)!=0 ? min.getValue()[0] : max.getValue()[0],
                           (i & 02)!=0 ? min.getValue()[1] : max.getValue()[1],
                           (i & 04)!=0 ? min.getValue()[2] : max.getValue()[2]);

    // intersect the 12 triangles.
    for(i = 0; i < 12; i++) {
        switch(i) {
        case  0: v0 = 2; v1 = 1; v2 = 0; break;         // +z
        case  1: v0 = 2; v1 = 3; v2 = 1; break;

        case  2: v0 = 4; v1 = 5; v2 = 6; break;         // -z
        case  3: v0 = 6; v1 = 5; v2 = 7; break;

        case  4: v0 = 0; v1 = 6; v2 = 2; break;         // -x
        case  5: v0 = 0; v1 = 4; v2 = 6; break;

        case  6: v0 = 1; v1 = 3; v2 = 7; break;         // +x
        case  7: v0 = 1; v1 = 7; v2 = 5; break;

        case  8: v0 = 1; v1 = 4; v2 = 0; break;         // -y
        case  9: v0 = 1; v1 = 5; v2 = 4; break;

        case 10: v0 = 2; v1 = 7; v2 = 3; break;         // +y
        case 11: v0 = 2; v1 = 6; v2 = 7; break;
        }
        if ((valid = intersect(points[v0], points[v1], points[v2],
                               inter, bary, front)) == true) {
            if  (front[0]) {
                enter.copyFrom(inter);
                validIntersection = valid;
            }
            else {
                exit.copyFrom(inter);
                validIntersection = valid;
            }
        }
    }
    return validIntersection;
}

	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersects the line with the triangle formed bu v0, v1, v2.
//    Returns TRUE if there is an intersection. If there is an
//    intersection, barycentric will contain the barycentric
//    coordinates of the intersection (for v0, v1, v2, respectively)
//    and front will be set to TRUE if the ray intersected the front
//    of the triangle (as defined by the right hand rule).
//
// Use: internal

public boolean
intersect(final SbVec3f v0, final SbVec3f v1, final SbVec3f v2,
                  final SbVec3f intersection,
                  final SbVec3f barycentric, final boolean[] front)
//
////////////////////////////////////////////////////////////////////////
{
    //////////////////////////////////////////////////////////////////
    //
    // The method used here is described by Didier Badouel in Graphics
    // Gems (I), pages 390 - 393.
    //
    //////////////////////////////////////////////////////////////////

final float EPSILON = 1e-10f;

    //
    // (1) Compute the plane containing the triangle
    //
    final SbVec3f     v01 = v1.operator_minus(v0);
    final SbVec3f     v12 = v2.operator_minus(v1);
    final SbVec3f     norm = v12.cross(v01);  // Un-normalized normal
    // Normalize normal to unit length, and make sure the length is
    // not 0 (indicating a zero-area triangle)
    if (norm.normalize() < EPSILON)
        return false;

    //
    // (2) Compute the distance t to the plane along the line
    //
    float d = getDirection().dot(norm);
    if (d < EPSILON && d > -EPSILON)
        return false;                   // Line is parallel to plane
    float t = norm.dot(v0.operator_minus( getPosition())) / d;

    // Note: we DO NOT ignore intersections behind the eye (t < 0.0)

    //
    // (3) Find the largest component of the plane normal. The other
    //     two dimensions are the axes of the aligned plane we will
    //     use to project the triangle.
    //
    float       xAbs = norm.getValue()[0] < 0.0 ? -norm.getValue()[0] : norm.getValue()[0];
    float       yAbs = norm.getValue()[1] < 0.0 ? -norm.getValue()[1] : norm.getValue()[1];
    float       zAbs = norm.getValue()[2] < 0.0 ? -norm.getValue()[2] : norm.getValue()[2];
    int         axis0, axis1;

    if (xAbs > yAbs && xAbs > zAbs) {
        axis0 = 1;
        axis1 = 2;
    }
    else if (yAbs > zAbs) {
        axis0 = 2;
        axis1 = 0;
    }
    else {
        axis0 = 0;
        axis1 = 1;
    }

    //
    // (4) Determine if the projected intersection (of the line and
    //     the triangle's plane) lies within the projected triangle.
    //     Since we deal with only 2 components, we can avoid the
    //     third computation.
    //
    float intersection0 = getPosition().getValue()[axis0] + t * getDirection().getValue()[axis0];
    float intersection1 = getPosition().getValue()[axis1] + t * getDirection().getValue()[axis1];

    final SbVec2f     diff0 = new SbVec2f(), diff1 = new SbVec2f(), diff2 = new SbVec2f();
    boolean      isInter = false;
    float       alpha = Float.NaN, beta;

    diff0.getValue()[0] = intersection0 - v0.getValue()[axis0];
    diff0.getValue()[1] = intersection1 - v0.getValue()[axis1];
    diff1.getValue()[0] = v1.getValue()[axis0]     - v0.getValue()[axis0];
    diff1.getValue()[1] = v1.getValue()[axis1]     - v0.getValue()[axis1];
    diff2.getValue()[0] = v2.getValue()[axis0]     - v0.getValue()[axis0];
    diff2.getValue()[1] = v2.getValue()[axis1]     - v0.getValue()[axis1];

    // Note: This code was rearranged somewhat from the code in
    // Graphics Gems to provide a little more numeric
    // stability. However, it can still miss some valid intersections
    // on very tiny triangles.
    isInter = false;
    beta = ((diff0.getValue()[1] * diff1.getValue()[0] - diff0.getValue()[0] * diff1.getValue()[1]) /
            (diff2.getValue()[1] * diff1.getValue()[0] - diff2.getValue()[0] * diff1.getValue()[1]));
    if (beta >= 0.0 && beta <= 1.0) {
        alpha = -1.0f;
        if (diff1.getValue()[1] < -EPSILON || diff1.getValue()[1] > EPSILON) 
            alpha = (diff0.getValue()[1] - beta * diff2.getValue()[1]) / diff1.getValue()[1];
        else
            alpha = (diff0.getValue()[0] - beta * diff2.getValue()[0]) / diff1.getValue()[0];
        isInter = (alpha >= 0.0 && alpha + beta <= 1.0);
    }

    //
    // (5) If there is an intersection, set up the barycentric
    //     coordinates and figure out if the front was hit.
    //
    if (isInter) {
        barycentric.setValue(1.0f - (alpha + beta), alpha, beta);
        front[0] = (getDirection().dot(norm) < 0.0);
        intersection.copyFrom( getPosition().operator_add(getDirection().operator_mul(t)));
    }

    return isInter;

//#undef EPSILON
}
 	

// find points on this line and on line2 that are closest to each other.
// If the lines intersect, then ptOnThis and ptOnLine2 will be equal.
// If the lines are parallel, then FALSE is returned, and the contents of
// ptOnThis and ptOnLine2 are undefined.
public boolean
getClosestPoints( final SbLine line2,
                                final SbVec3f ptOnThis,
                                final SbVec3f ptOnLine2 )
{
    float    s, t, A, B, C, D, E, F, denom;
    final SbVec3f  pos2 = new SbVec3f(line2.getPosition());
    final SbVec3f  dir2 = new SbVec3f(line2.getDirection());


//  DERIVATION:
//      [1] parametric descriptions of desired pts
//          pos  + s * dir  = ptOnThis
//          pos2 + t * dir2 = ptOnLine2
//      [2] (By some theorem or other--)
//          If these are closest points between lines, then line connecting 
//          these points is perpendicular to both this line and line2.
//          dir  . ( ptOnLine2 - ptOnThis ) = 0
//          dir2 . ( ptOnLine2 - ptOnThis ) = 0
//          OR...
//          dir  . ( pos2 + t * dir2 - pos - s * dir ) = 0
//          dir2 . ( pos2 + t * dir2 - pos - s * dir ) = 0
//      [3] Rearrange the terms:
//          t * [ dir  . dir2 ] - s * [dir  . dir ] = dir  . pos - dir  . pos2 
//          t * [ dir2 . dir2 ] - s * [dir2 . dir ] = dir2 . pos - dir2 . pos2 
//      [4] Let:
//          A= dir  . dir2
//          B= dir  . dir
//          C= dir  . pos - dir . pos2
//          D= dir2 . dir2
//          E= dir2 . dir
//          F= dir2 . pos - dir2 . pos2
//          So [3] above turns into:
//          t * A    - s * B =   C
//          t * D    - s * E =   F
//      [5] Solving for s gives:
//          s = (CD - AF) / (AE - BD)
//      [6] Solving for t gives:
//          t = (CE - BF) / (AE - BD)

    A = dir.dot( dir2 );
    B = dir.dot( dir );
    C = dir.dot( pos ) - dir.dot( pos2 );
    D = dir2.dot( dir2 );
    E = dir2.dot( dir );
    F = dir2.dot( pos ) - dir2.dot( pos2 );


    denom = A * E - B * D;
    if ( denom == 0.0)    // the two lines are parallel
        return false;

    s = ( C * D - A * F ) / denom;
    t = ( C * E - B * F ) / denom;
    ptOnThis.copyFrom( pos.operator_add( dir.operator_mul( s)));
    ptOnLine2.copyFrom( pos2.operator_add( dir2.operator_mul( t)));

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//  Returns the closes point on this line to the given point.
//
// Use: public

public SbVec3f
getClosestPoint(final SbVec3f point) 
//
////////////////////////////////////////////////////////////////////////
{
    // vector from origin of this line to given point
    SbVec3f p = point.operator_minus(pos);

    // find the length of p when projected onto this line
    // (which has direction d)
    // length = |p| * cos(angle b/w p and d) = (p.d)/|d|
    // but |d| = 1, so
    float length = p.dot(dir);

    // vector coincident with this line
    final SbVec3f proj = new SbVec3f(dir);
    proj.operator_mul_equal( length);

    SbVec3f result = pos.operator_add(proj);
    return result;
}    



}
