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
 |      This file contains definitions of SbBoxes, 2D/3D boxes. A
 |      box has planes parallel to the major axes and can therefore
 |      be specified by two points on a diagonal.  The points with minimum
 |      and maximum x, y, and z coordinates are used.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, David Mott
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.util.function.DoubleConsumer;

import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! 3D box class.
/*!
\class SbBox3f
\ingroup Basics
3D box which has planes parallel to the major axes and is specified by two
points on a diagonal. This class is part of the standard Inventor
datatype classes and is used as input and output to geometry operations (see
SoGetBoundingBoxAction).

\par See Also
\par
SbXfBox3f, SbBox2f, SbBox2s, SbVec3f, SbVec2f, SbVec2s, SbMatrix, SoGetBoundingBoxAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbBox3f implements Mutable {

	private final SbVec3fSingle min = new SbVec3fSingle();
	private final SbVec3fSingle max = new SbVec3fSingle();

	public SbBox3f() {
		makeEmpty();
	}

	public SbBox3f(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		 min.setValue(xmin, ymin, zmin); max.setValue(xmax, ymax, zmax);
	}

	// java port
	public SbBox3f(SbBox3f other) {
		copyFrom(other);
	}

	/**
	 * Constructor given minimum and maximum points min and max
	 * are the corners of the diagonal that define the box.
	 *
	 * @param min
	 * @param max
	 */
	public SbBox3f(SbVec3f min, SbVec3f max) {
		 this.min.copyFrom(min); this.max.copyFrom(max);
	}

	/**
	 * Java port
	 * @param volume
	 */
	public SbBox3f(float[] volume) {
		float xmin = volume[0];
		float ymin = volume[1];
		float zmin = volume[2];
		float xmax = volume[3];
		float ymax = volume[4];
		float zmax = volume[5];
		 min.setValue(xmin, ymin, zmin); max.setValue(xmax, ymax, zmax);
	}

	/**
	 * Returns the minimum point of the box.
	 * The minimum point is the corner of the box with
	 * the lowest X, Y, and Z values.
	 *
	 * @return
	 */
	public SbVec3f getMin() {
		 return min;
	}

	/**
	 * Returns the maximum point of the box.
	 * The maximum point is the corner of the box with
	 * the highest X, Y, and Z values.
	 *
	 * @return
	 */
	public SbVec3f getMax() {
		 return max;
	}

	// Returns the center of the box.
	public SbVec3f getCenter() {
	     return new SbVec3f(0.5f * (min.getValueRead()[0] + max.getValueRead()[0]),
	    		                       0.5f * (min.getValueRead()[1] + max.getValueRead()[1]),
	    		                       0.5f * (min.getValueRead()[2] + max.getValueRead()[2]));

	}

	//
// Extends Box3f (if necessary) to contain given 3D point
//

public void
extendBy(final SbVec3f ptV)
{
    if (ptV.getX() < min.getX()) {
    	this.min.setValue(0, ptV.getX());
    }
    if (ptV.getY() < min.getY()) {
    	this.min.setValue(1, ptV.getY());
    }
    if (ptV.getZ() < min.getZ()) {
    	this.min.setValue(2, ptV.getZ());
    }
    if (ptV.getX() > max.getX()) {
    	this.max.setValue(0, ptV.getX());
    }
    if (ptV.getY() > max.getY()) {
    	this.max.setValue(1, ptV.getY());
    }
    if (ptV.getZ() > max.getZ()) {
    	this.max.setValue(2, ptV.getZ());
    }
}


	public void SbBox3f_extendBy(SbBox3f bb) {
	     if (bb.min.getValueRead()[0] < min.getValueRead()[0]) {
            min.getValue()[0] = bb.min.getValueRead()[0];
        }
	          if (bb.min.getValueRead()[1] < min.getValueRead()[1]) {
                min.getValue()[1] = bb.min.getValueRead()[1];
            }
	          if (bb.min.getValueRead()[2] < min.getValueRead()[2]) {
                min.getValue()[2] = bb.min.getValueRead()[2];
            }
	          if (bb.max.getValueRead()[0] > max.getValueRead()[0]) {
                max.getValue()[0] = bb.max.getValueRead()[0];
            }
	         if (bb.max.getValueRead()[1] > max.getValueRead()[1]) {
                max.getValue()[1] = bb.max.getValueRead()[1];
            }
	         if (bb.max.getValueRead()[2] > max.getValueRead()[2]) {
                max.getValue()[2] = bb.max.getValueRead()[2];
            }
	}
	// Extends Box3f (if necessary) to contain given Box3f.
	public void extendBy(SbBox3f bb) {

		SbBox3f_extendBy(bb);
	    	}


//
// Returns true if intersection of given point and Box3f is not empty
//

public boolean
intersect(final SbVec3f pt)
{
    return ((pt.getX() >= min.getX()) &&
            (pt.getY() >= min.getY()) &&
            (pt.getZ() >= min.getZ()) &&
            (pt.getX() <= max.getX()) &&
            (pt.getY() <= max.getY()) &&
            (pt.getZ() <= max.getZ()));
}

//
// Returns true if intersection of given Box3f and Box3f is not empty
//

public boolean
intersect(final SbBox3f bb)
{
    return ((bb.max.getValueRead()[0] >= min.getValueRead()[0]) && (bb.min.getValueRead()[0] <= max.getValueRead()[0]) &&
            (bb.max.getValueRead()[1] >= min.getValueRead()[1]) && (bb.min.getValueRead()[1] <= max.getValueRead()[1]) &&
            (bb.max.getValueRead()[2] >= min.getValueRead()[2]) && (bb.min.getValueRead()[2] <= max.getValueRead()[2]));
}



	// Common get and set functions.
	public void setBounds (float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		 min.setValue(xmin, ymin, zmin); max.setValue(xmax, ymax, zmax);
	}

	public void setBounds(SbVec3f _min, SbVec3f _max) {
		 min.copyFrom(_min); max.copyFrom(_max);
	}

    public void        getBounds(final float[] xmin, final float[] ymin, final float[] zmin,
                          final float[] xmax, final float[] ymax, final float[] zmax)
        { min.getValue(xmin, ymin, zmin); max.getValue(xmax, ymax, zmax); }



	public void getBounds(final SbVec3f _min, final SbVec3f _max) {
		 _min.copyFrom(min); _max.copyFrom(max);
	}

	 //
	 // Sets Box3f to contain nothing
	 //

	 public void makeEmpty()
	 {
	  min.setValue( Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
	  max.setValue(- Float.MAX_VALUE, - Float.MAX_VALUE, - Float.MAX_VALUE);
	}

	 // Returns true if the box is empty, and FALSE otherwise.
	 public boolean isEmpty() {
		 return max.getValueRead()[0] < min.getValueRead()[0];
	 }

//
// View-volume culling: axis-aligned bounding box against view volume,
// given as Model/View/Projection matrix.
//
//
// Inputs:
//    MVP:       Matrix from object to NDC space
//                 (model/view/projection)
// Inputs/Outputs:
//    cullBits:  Keeps track of which planes we need to test against.
//               Has three bits, for X, Y and Z.  If cullBits is 0,
//               then the bounding box is completely inside the view
//               and no further cull tests need be done for things
//               inside the bounding box.  Zero bits in cullBits mean
//               the bounding box is completely between the
//               left/right, top/bottom, or near/far clipping planes.
// Outputs:
//    SbBool:    true if bbox is completely outside view volume
//               defined by MVP.
//

//
// How:
//
// An axis-aligned bounding box is the set of all points P,
// Pmin < P < Pmax.  We're interested in finding out whether or not
// any of those points P are clipped after being transformed through
// MVP (transformed into clip space).
//
// A transformed point P'[x,y,z,w] is inside the view if [x,y,z] are
// all between -w and w.  Otherwise the point is outside the view.
//
// Instead of testing individual points, we want to treat the range of
// points P.  We want to know if:  All points P are clipped (in which
// case the cull test succeeds), all points P are NOT clipped (in
// which case they are completely inside the view volume and no more
// cull tests need be done for objects inside P), or some points are
// clipped and some aren't.
//
// P transformed into clip space is a 4-dimensional solid, P'.  To
// speed things up, this algorithm finds the 4-dimensional,
// axis-aligned bounding box of that solid and figures out whether or
// not that bounding box intersects any of the sides of the view
// volume.  In the 4D space with axes x,y,z,w, the view volume planes
// are the planes x=w, x=-w, y=w, y=-w, z=w, z=-w.
//
// This is all easier to think about if we think about each of the X,
// Y, Z axes in clip space independently; worrying only about the X
// axis for a moment:
//
// The idea is to find the minimum and maximum possible X,W
// coordinates of P'.  If all of the points in the
// [(Xmin,Xmax),(Wmin,Wmax)] range satisfy -|W| < X < |W| (|W| is
// absolute value of W), then the original bounding box P is
// completely inside the X-axis (left/right) clipping planes of the
// view volume.  In (x,w) space, a point (x,w) is clipped depending on
// which quadrant it is in:
//
//    x=-w       x=w
//      \   Q0   /
//       \  IN  /
//        \    /
//         \  /
// Q1       \/  Q2
// CLIPPED  /\  CLIPPED
//         /  \
//        /    \
//       /  Q3  \
//      / CLIPPED\
//
// If the axis-aligned box [(Xmin,Xmax),(Wmin,Wmax)] lies entirely in
// Q0, then it is entirely inside the X-axis clipping planes (IN
// case).  If it is not in Q0 at all, then it is clipped (OUT).  If it
// straddles Q0 and some other quadrant, the bounding box intersects
// the clipping planes (STRADDLE).  The 4 corners of the bounding box
// are tested first using bitwise tests on their quadrant numbers; if
// they determine the case is STRADDLE then a more refined test is
// done on the 8 points of the original bounding box.
// The test isn't perfect-- a bounding box that straddles both Q1 and
// Q2 may be incorrectly classified as STRADDLE; however, those cases
// are rare and the cases that are incorrectly classified will likely
// be culled when testing against the other clipping planes (these
// cases are cases where the bounding box is near the eye).
//
// Finding [(Xmin,Xmax),(Wmin,Wmax)] is easy.  Consider Xmin.  It is
// the smallest X coordinate when all of the points in the range
// Pmin,Pmax are transformed by MVP; written out:
//     X = P[0]*M[0][0] + P[1]*M[1][0] + P[2]*M[2][0] + M[3][0]
// X will be minimized when each of the terms is minimized.  If the
// matrix entry for the term is positive, then the term is minimized
// by choosing Pmin; if the matrix entry is negative, the term is
// minimized by choosing Pmax.  Three 'if' test will let us calculate
// the transformed Xmin.  Xmax can be calculated similarly.
//
// Testing for IN/OUT/STRADDLE for the Y and Z coordinates is done
// exactly the same way.
//

//
// Helper functions:
//
// Given a range in object space, find the minimum or maximum for the
// X,Y,Z or W coordinates in the transformed space.
//    3 multiplies, 3 adds, 3 comparisons/branches.
// Reverse min and max for the opposite test...
private static float
minExtreme(final SbVec3f min, final SbVec3f max, final
           SbMatrix MVP, int whichCoord) {
    return
        (MVP.getValue()[0][whichCoord]>0.0f ? min.getValueRead()[0] : max.getValueRead()[0])*MVP.getValue()[0][whichCoord] +
        (MVP.getValue()[1][whichCoord]>0.0f ? min.getValueRead()[1] : max.getValueRead()[1])*MVP.getValue()[1][whichCoord] +
        (MVP.getValue()[2][whichCoord]>0.0f ? min.getValueRead()[2] : max.getValueRead()[2])*MVP.getValue()[2][whichCoord] +
        MVP.getValue()[3][whichCoord];
}

private static int
quadrant(float x, float y) {
    //return (x < -y) | ((x > y) << 1);
	// java port
	int maybe_one = (x < -y) ? 1 : 0;
	int maybe_two = (x > y) ? 2 : 0;
	return maybe_one | maybe_two;
}

private static int
findQuadrant(float x, float y, float z,
             int n, final SbMatrix  MVP)
{
    float c = MVP.getValue()[0][n]*x + MVP.getValue()[1][n]*y + MVP.getValue()[2][n]*z + MVP.getValue()[3][n] ;
    float w = MVP.getValue()[0][3]*x + MVP.getValue()[1][3]*y + MVP.getValue()[2][3]*z + MVP.getValue()[3][3] ;
    return quadrant(c, w);
}





	 // Transforms box by matrix, enlarging box to contain result.

	 //
	   // Transforms Box3f by matrix, enlarging Box3f to contain result.
	   // Clever method courtesy of Graphics Gems, pp. 548-550
	   //
	   // This works for projection matrices as well as simple affine
	   // transformations.  Coordinates of the box are rehomogenized if there
	   // is a projection matrix
	   //
	  	 public void transform(SbMatrix m) {

	     // a transformed empty box is still empty
		       if (isEmpty()) {
                return;
            }

		       final SbVec3fSingle     newMin = new SbVec3fSingle(), newMax = new SbVec3fSingle();
		       int         i;

		       for (i = 0; i < 3; i++) {
		           newMin.getValue()[i] = minExtreme(min, max, m, i);
		           newMax.getValue()[i] = minExtreme(max, min, m, i);
		       }
		       float Wmin = minExtreme(min, max, m, 3);
		       float Wmax = minExtreme(max, min, m, 3);

		       // Division by small W's make things bigger; wacky things happen
		       // if W's are negative, but negative W's are wacky so I think
		       // that's OK:

		       newMin.operator_div_equal(Wmax);
		       newMax.operator_div_equal(Wmin);

		       min.copyFrom(newMin);
		       max.copyFrom(newMax);
		   	 }


//
// Finds the volume of the box (0 for an empty box)
//

public float
getVolume()
{
    if (isEmpty()) {
        return 0.0f;
    }

    return (max.getValueRead()[0] - min.getValueRead()[0]) * (max.getValueRead()[1] - min.getValueRead()[1]) * (max.getValueRead()[2] - min.getValueRead()[2]);
}



	  	 // java port
	  	 public void getBounds(float[] bounds) {
	  		 float[] mini = min.getValueRead();
	  		 float[] maxi = max.getValueRead();
	  		 bounds[0] = mini[0];
	  		 bounds[1] = mini[1];
	  		 bounds[2] = mini[2];
	  		 bounds[3] = maxi[0];
	  		 bounds[4] = maxi[1];
	  		 bounds[5] = maxi[2];
	  	 }

    // java port
    public float[] getBounds() {
        float[] bounds = new float[6];
        getBounds(bounds);
        return bounds;
    }

    //! Gets box size.
	public    void        getSize(final float[] sizeX, final float[] sizeY, final float[] sizeZ)
        { sizeX[0] = max.getValueRead()[0] - min.getValueRead()[0];
          sizeY[0] = max.getValueRead()[1] - min.getValueRead()[1];
          sizeZ[0] = max.getValueRead()[2] - min.getValueRead()[2]; }

	public void getSize(final float[] xyz) {
        { xyz[0] = max.getValueRead()[0] - min.getValueRead()[0];
        xyz[1] = max.getValueRead()[1] - min.getValueRead()[1];
        xyz[2] = max.getValueRead()[2] - min.getValueRead()[2]; }
	}

	public void getSize(final SbVec3f xyz) {
        { xyz.setValue(0, max.getValueRead()[0] - min.getValueRead()[0]);
        xyz.setValue(1, max.getValueRead()[1] - min.getValueRead()[1]);
        xyz.setValue(2, max.getValueRead()[2] - min.getValueRead()[2]); }
	}

	  public void getSize(DoubleConsumer sizeX, DoubleConsumer sizeY, DoubleConsumer sizeZ)
	    { if (isEmpty()) { sizeX.accept(0); sizeY.accept(0); sizeZ.accept(0); }
	      else { sizeX.accept( max.getValueRead()[0] - min.getValueRead()[0]); sizeY.accept( max.getValueRead()[1] - min.getValueRead()[1]); sizeZ.accept(max.getValueRead()[2] - min.getValueRead()[2]); } }

	
	     // java port
	     @Override
        public void copyFrom(Object otherObject) {
	    	 SbBox3f other = (SbBox3f) otherObject;
	    	 min.copyFrom(other.min);
	    	 max.copyFrom(other.max);
	     }


public boolean
outside(final SbMatrix MVP, final int[] cullBits)
{
    float Wmax = minExtreme(max, min, MVP, 3);
    if (Wmax < 0) {
        return true;
    }

    float Wmin = minExtreme(min, max, MVP, 3);

    // Do each coordinate:
    for (int i = 0; i < 3; i++) {
        if ((cullBits[0] & (1<<i))!=0) {  // STRADDLES:
            float Cmin = minExtreme(min, max, MVP, i);

            // The and_bits and or_bits are used to keep track of
            // which quadrants point lie in.  The cases are:
            //
            // All in Q0:  IN
            //    (or_bits == 0)  --> and_bits MUST also be 0
            // Some/all in Q1, some/all in Q3: CULLED
            // Some/all in Q2, some/none in Q3: CULLED
            //    (and_bits != 0, or_bits != 0)
            // Some in Q1, some in Q2, some/none in Q3: STRADDLE
            //    (and_bits == 0, or_bits !=0)
            //
            int and_bits;
            int or_bits;
            and_bits = or_bits = quadrant(Cmin, Wmin);

            int q0 = quadrant(Cmin, Wmax);
            and_bits &= q0;
            or_bits |= q0;
            // Hit the STRADDLE case as soon as and_bits == 0 and
            // or_bits != 0:
            if (!(and_bits == 0 && or_bits != 0)) {
                float Cmax = minExtreme(max, min, MVP, i);

                q0 = quadrant(Cmax, Wmin);
                and_bits &= q0;
                or_bits |= q0;
                if (!(and_bits == 0 && or_bits != 0)) {
                    q0 = quadrant(Cmax, Wmax);
                    and_bits &= q0;
                    or_bits |= q0;

                    // Either completely IN or completely OUT:
                    if (or_bits == 0) { // IN
                        cullBits[0] &= ~(1<<i); // Clear bit
                        continue; // Continue for loop
                    }
                    else if (and_bits != 0) {
                        return true;  // CULLED
                    }
                }
            }

            // Before we give up and just claim it straddles, do a
            // more refined test-- check the 8 corners of the
            // bounding box:

            // Test to see if all 8 corner points of the bounding box
            // are in the same quadrant.  If so, the object is either
            // completely in or out of the view.  Otherwise, it straddles
            // at least one of the view boundaries.

            // java port
            final float[] min_ = min.getValueRead();
            final float[] max_ = max.getValueRead();

            and_bits = or_bits = findQuadrant(min_[0], min_[1], min_[2], i, MVP);
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(max_[0], max_[1], max_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(max_[0], min_[1], min_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(min_[0], max_[1], max_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(min_[0], max_[1], min_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(max_[0], min_[1], max_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(max_[0], max_[1], min_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;
            if (and_bits == 0 && or_bits != 0) {
                continue;
            }

            q0 = findQuadrant(min_[0], min_[1], max_[2], i, MVP);
            and_bits &= q0;
            or_bits |= q0;

            // Either completely IN or completely OUT:
            if (or_bits == 0) { // IN
                cullBits[0] &= ~(1<<i); // Clear bit
                continue; // Continue for loop
            }
            else if (and_bits != 0) {
                return true;  // CULLED
            }
        }
    }
    return false;  // Not culled
}

//////////////////////////////////////////////////////////////////////////////
//
// Equality comparison operator.
//
public boolean
operator_equal_equal( final SbBox3f b2)
//////////////////////////////////////////////////////////////////////////////
{
	final SbBox3f b1 = this;
    return ( (b1.min == b2.min) && (b1.max == b2.max ) );
}

public SbVec3f getSize() { // java port
	float[] xyz = new float[3];
	getSize(xyz);
	return new SbVec3f(xyz);
}

/**
 * Java port
 * @param geometryBBox
 * @return
 */
public boolean contains(SbBox3f other) {
	if(other.min.getX() < min.getX()) {
		return false;
	}
	if(other.min.getY() < min.getY()) {
		return false;
	}
	if(other.min.getZ() < min.getZ()) {
		return false;
	}
	if(other.max.getX() > max.getX()) {
		return false;
	}
	if(other.max.getY() > max.getY()) {
		return false;
	}
	if(other.max.getZ() > max.getZ()) {
		return false;
	}
	return true;
}


/*!
  Return the point on the box closest to the given \a point.
*/
public SbVec3f
getClosestPoint(final SbVec3f point)
{
  final SbVec3fSingle closest = new SbVec3fSingle(point);
  
  final float[] closest_getValue = closest.getValue(); 
  
  final float[] min_getValue = this.min.getValue();
  final float[] max_getValue = this.max.getValue();

  final SbVec3fSingle center = new SbVec3fSingle(this.getCenter());
  float devx = closest_getValue[0] - center.getValue()[0];
  float devy = closest_getValue[1] - center.getValue()[1];
  float devz = closest_getValue[2] - center.getValue()[2];
  float halfwidth = (max_getValue[0] - min_getValue[0]) / 2.0f;
  float halfheight = (max_getValue[1] - min_getValue[1]) / 2.0f;
  float halfdepth = (max_getValue[2] - min_getValue[2]) / 2.0f;

  // Move point to be on the nearest plane of the box.
  if ((Math.abs(devx) > Math.abs(devy)) && (Math.abs(devx) > Math.abs(devz)))
    closest_getValue[0] = center.getValue()[0] + halfwidth * ((devx < 0.0f) ? -1.0f : 1.0f);
  else if (Math.abs(devy) > Math.abs(devz))
    closest_getValue[1] = center.getValue()[1] + halfheight * ((devy < 0.0f) ? -1.0f : 1.0f);
  else
    closest_getValue[2] = center.getValue()[2] + halfdepth * ((devz < 0.0f) ? -1.0f : 1.0f);

  // Clamp to be inside box.
  closest_getValue[0] = Math.min(Math.max(closest_getValue[0], min_getValue[0]), max_getValue[0]);
  closest_getValue[1] = Math.min(Math.max(closest_getValue[1], min_getValue[1]), max_getValue[1]);
  closest_getValue[2] = Math.min(Math.max(closest_getValue[2], min_getValue[2]), max_getValue[2]);

  return closest;
}

}
