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

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Class for representing a rotation.
/*!
\class SbRotation
\ingroup Basics
Object that stores a rotation. There are several ways to specify a
rotation: quaternion (4 floats), 4x4 rotation matrix, or axis and angle.
All angles are in radians and all rotations are right-handed.
{}
\par
NOTES

Rotations are stored internally as quaternions.

\par See Also
\par
SbVec3f, SbMatrix
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbRotation implements Mutable {
	
	private final float[] quat = new float[4];

	/*!
      The default constructor just initializes a valid rotation. The
      actual value is unspecified, and you should not depend on it.
    */
	public SbRotation() {
		// This translates to zero rotation around the positive Z-axis.
		quat[3] = 1;
	}
	
	public SbRotation(SbRotation other) {
		quat[0] = other.quat[0];
		quat[1] = other.quat[1];
		quat[2] = other.quat[2];
		quat[3] = other.quat[3];
	}
	
    //! \see SbRotation(const SbVec3f &rotateFrom, const SbVec3f &rotateTo)
      public SbRotation(float q0, float q1, float q2, float q3)
          { setValue(q0, q1, q2, q3); }
  	
    //! \see SbRotation(const SbVec3f &rotateFrom, const SbVec3f &rotateTo)
    public SbRotation(final SbMatrix m)
        { setValue(m); }

	
	public SbRotation(SbVec3f axis, float radians) {
		 setValue(axis, radians); 
	}
	
	/**
	 * Constructors for rotation. 
	 * The axis/radians constructor creates a rotation 
	 * of angle radians about the given axis. 
	 * The constructors that take four floats create a 
	 * quaternion from those floats (careful, this differs 
	 * from the four numbers in an axis/radian definition). 
	 * Matrix constructor requires a valid rotation matrix. 
	 * The rotateFrom/To constructor defines rotation that 
	 * rotates from one vector into another. 
	 * The rotateFrom and rotateTo vectors are normalized 
	 * by the constructor before calculating the rotation. 
	 */
	public SbRotation( SbVec3f rotateFrom, SbVec3f rotateTo)
	{ setValue(rotateFrom, rotateTo); }
	
	/**
	 * java port
	 * @param other
	 */
	public SbRotation(SbRotationd other) {
		double[] q0q1q2q3 = new double[4];
		other.getValue(q0q1q2q3);
		quat[0] = (float)q0q1q2q3[0];
		quat[1] = (float)q0q1q2q3[1];
		quat[2] = (float)q0q1q2q3[2];
		quat[3] = (float)q0q1q2q3[3];
	}

    //! \see SbRotation(const SbVec3f &rotateFrom, const SbVec3f &rotateTo)
    public void constructor(final SbMatrix m)
        { setValue(m); }

	// Returns pointer to array of 4 components defining quaternion. 
	public float[] getValue() {
		 return (quat); 
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns corresponding 3D rotation axis vector and angle in radians.
//
// Use: public

    private final SbVec3fSingle     q = new SbVec3fSingle(); // SINGLE_THREAD
    
public void
getValue(final SbVec3f axis, final float[] radians)
//
////////////////////////////////////////////////////////////////////////
{
    float       len;

    q.getValue()[0] = quat[0];
    q.getValue()[1] = quat[1];
    q.getValue()[2] = quat[2];

    if ((len = q.length()) > 0.00001) {
    	axis.copyFrom(q); axis.operator_mul_equal(1.0f / len);
        //axis.copyFrom( q.operator_mul(1.0f / len));
        radians[0] = 2.0f * (float)Math.acos(quat[3]);
    }

    else {
        axis.setValue(0.0f, 0.0f, 1.0f);
        radians[0] = 0.0f;
    }
}

	
	
	// Returns corresponding 4x4 rotation matrix. 
	public void getValue(SbMatrix matrix) {
		
		float[][] m = matrix.getValue();
		
	    m[0][0] = 1 - 2.0f * (quat[1] * quat[1] + quat[2] * quat[2]);
	         m[0][1] =     2.0f * (quat[0] * quat[1] + quat[2] * quat[3]);
	         m[0][2] =     2.0f * (quat[2] * quat[0] - quat[1] * quat[3]);
	         m[0][3] = 0.0f;
	     
	         m[1][0] =     2.0f * (quat[0] * quat[1] - quat[2] * quat[3]);
	         m[1][1] = 1 - 2.0f * (quat[2] * quat[2] + quat[0] * quat[0]);
	         m[1][2] =     2.0f * (quat[1] * quat[2] + quat[0] * quat[3]);
	         m[1][3] = 0.0f;
	     
	         m[2][0] =     2.0f * (quat[2] * quat[0] + quat[1] * quat[3]);
	         m[2][1] =     2.0f * (quat[1] * quat[2] - quat[0] * quat[3]);
	         m[2][2] = 1 - 2.0f * (quat[1] * quat[1] + quat[0] * quat[0]);
	         m[2][3] = 0.0f;
	     
	         m[3][0] = 0.0f;
	         m[3][1] = 0.0f;
	         m[3][2] = 0.0f;
	         m[3][3] = 1.0f;
	     	}
	
	// Changes a rotation to be its inverse. 
	public SbRotation invert() {
	     float invNorm = 1.0f / norm();
	      
	          quat[0] = -quat[0] * invNorm;
	          quat[1] = -quat[1] * invNorm;
	          quat[2] = -quat[2] * invNorm;
	          quat[3] =  quat[3] * invNorm;
	      
	          return this;	     		
	}
	
    //! Returns the inverse of a rotation.
    public SbRotation          inverse() 
        { final SbRotation q = new SbRotation(this); return q.invert(); }


	
	// Sets value of rotation from 4 individual components of a quaternion. 
	public SbRotation setValue(float q0, float q1, float q2, float q3) {
		
		 quat[0] = q0;
		   quat[1] = q1;
		   quat[2] = q2;
		   quat[3] = q3;
		   normalize();
		  
		   return this;
		 
		 }
	
	// Sets value of vector from 3D rotation axis vector and angle in radians. 
	public SbRotation setValue(SbVec3f axis, float radians) {
		
		  final SbVec3f q = new SbVec3f();
		   
		    q.copyFrom(axis);
		    q.normalize();
		   
		    q.operator_mul_equal((float)Math.sin(radians / 2.0));
		   
		    quat[0] = q.operator_square_bracket(0);
		    quat[1] = q.operator_square_bracket(1);
		    quat[2] = q.operator_square_bracket(2);
		   
		    quat[3] = (float)Math.cos(radians / 2.0);
		   
		    return this;
		  	}
	
	//
	 // Description:
	 // Sets rotation to rotate one direction vector to another.
	 //
	 // Use: public
	 
	public SbRotation setValue(SbVec3f rotateFrom, SbVec3f rotateTo) {
		
		  SbVec3f from = (SbVec3f)rotateFrom.clone();
		    SbVec3f to = (SbVec3f)rotateTo.clone();
		    SbVec3f axis = new SbVec3f();
		    float cost;
		   
		    from.normalize();
		    to.normalize();
		    cost = from.dot(to);
		   
		    // check for degeneracies
		    if (cost > 0.99999) { // vectors are parallel
		    quat[0] = quat[1] = quat[2] = 0.0f;
		    quat[3] = 1.0f;
		    return this;
		    }
		    else if (cost < -0.99999) { // vectors are opposite
		    // find an axis to rotate around, which should be
		    // perpendicular to the original axis
		    // Try cross product with (1,0,0) first, if that's one of our
		    // original vectors then try (0,1,0).
		    SbVec3f tmp = from.cross(new SbVec3f(1.0f, 0.0f, 0.0f));
		    if (tmp.length() < 0.00001)
		    tmp = from.cross(new SbVec3f(0.0f, 1.0f, 0.0f));
		   
		    tmp.normalize();
		    setValue(tmp.operator_square_bracket(0),
		    		tmp.operator_square_bracket(1),
		    		tmp.operator_square_bracket(2), 0.0f);
		    return this;
		    }
		   
		    axis = rotateFrom.cross(rotateTo);
		    axis.normalize();
		   
		    // use half-angle formulae
		    // sin^2 t = ( 1 - cos (2t) ) / 2
		    axis.operator_mul_equal((float)Math.sqrt(0.5 * (1.0 - cost)));
		   
		    // scale the axis by the sine of half the rotation angle to get
		    // the normalized quaternion
		    quat[0] = axis.operator_square_bracket(0);
		    quat[1] = axis.operator_square_bracket(1);
		    quat[2] = axis.operator_square_bracket(2);
		   
		    // cos^2 t = ( 1 + cos (2t) ) / 2
		    // w part is cosine of half the rotation angle
		    quat[3] = (float)Math.sqrt(0.5 * (1.0 + cost));
		   
		    return this;
		   
		  }

	/*!
      Reset the rotation by the four quaternions in the array.
      \sa getValue().
     */
	public SbRotation
	setValue(float[] q)
	{
		this.quat[0] = q[0];
		this.quat[1] = q[1];
		this.quat[2] = q[2];
		this.quat[3] = q[3];

		SbVec4f quatVec = new SbVec4f(q);

		if (quatVec.normalize() == 0.0f) {
//#if COIN_DEBUG
			SoDebugError.postWarning("SbRotation::setValue",
					"Quarternion has zero length => "+
							"undefined rotation.");
//#endif // COIN_DEBUG
		}
		q = quatVec.getValueRead();
		this.quat[0] = q[0];
		this.quat[1] = q[1];
		this.quat[2] = q[2];
		this.quat[3] = q[3];
		return this;
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value of rotation from a rotation matrix.
//    This algorithm is from "Quaternions and 4x4 Matrices", Ken
//    Shoemake, Graphics Gems II.
//
//  Here's the logic:
//    We're trying to find a quaterion 'q' that represents the same
//    rotation as the given matrix 'm'.
//    We know how to convert a quaterion to a rotation matrix; the
//    matrix is given by (where x,y,z,w are the quaterion elements):
//
//    x^2-y^2-z^2+w^2  2xy+2zw          2wx - 2yw        0
//    2xy-2zw          y^2-z^2-x^2+w^2  2yz + 2xw        0
//    2zx+2zw          2yz-2xw          z^2-x^2-y^2+w^2  0
//    0                0                0                x^2+y^2+z^2+w^2
//             (note that x^2+y^2+z^2+w^2==1 for a normalized quaterion)
//
//    We know m, we want to find x,y,z,w.  If you don't mind doing
//    square roots, then it is easy; for example:
//    m[0][0]+m[1][1]+m[2][2]+m[3][3] = 4w^2
//       (write it all out, and see all the terms cancel)
//    Or, w = sqrt(m[0][0]+m[1][1]+m[2][2]+m[3][3])/2
//    Similarly,
//        x = sqrt(m[0][0]-m[1][1]-m[2][2]+m[3][3])/2
//        y = sqrt(m[1][1]-m[2][2]-m[0][0]+m[3][3])/2
//        z = sqrt(m[2][2]-m[0][0]-m[1][1]+m[3][3])/2
//    However, you only really need to do one sqrt and find one of
//    x,y,z,w, because using the other elements of the matrix you
//    find, for example, that:
//    m[0][1]+m[1][0] = 2xy+2zw+2xy-2zw = 4xy
//    So if you know either x or y, you can find the other.
//
//    That is assuming that the first thing you find isn't zero, of
//    course.  In fact, you want the first element you find to be as
//    large as possible, to get more accuracy in the division.  You
//    can rewrite the diagonal elements as:
//    (w^2 - x^2 - y^2 - z^2) + 2x^2    = m[0][0]
//    (w^2 - x^2 - y^2 - z^2) + 2y^2    = m[1][1]
//    (w^2 - x^2 - y^2 - z^2) + 2z^2    = m[2][2]
//    ... and write the sum of the diagonals as:
//    (w^2 - x^2 - y^2 - z^2) + 2w^2    = m[0][0]+m[1][1]+m[2][2]+m[3][3]
//
//    Why do this?  Because now it is easy to see that if x is greater
//    than y, z, or w, then m[0][0] will be greater than the other
//    diagonals or the sum of the diagonals.
//
//    So, the overall strategy is:  Figure out which if x,y,z, or w
//    will be greatest by looking at the diagonals.  Compute that
//    value using the sqrt() formula.  Then compute the other values
//    using the other set of formulas.
//
// Use: public

    final SbMatrix check = new SbMatrix(); // SINGLE_THREAD
    
public SbRotation 
setValue(final SbMatrix _m)
//
////////////////////////////////////////////////////////////////////////
{
    int i, j, k;
    
    float[][] m = _m.getValue();

    // First, find largest diagonal in matrix:
    if (m[0][0] > m[1][1]) {
        if (m[0][0] > m[2][2]) {
            i = 0;
        }
        else i = 2;
    }
    else {
        if (m[1][1] > m[2][2]) {
            i = 1;
        }
        else i = 2;
    }
    if (m[0][0]+m[1][1]+m[2][2] > m[i][i]) {
        // Compute w first:
        quat[3] = (float)Math.sqrt(m[0][0]+m[1][1]+m[2][2]+m[3][3])/2.0f;

        // And compute other values:
        quat[0] = (m[1][2]-m[2][1])/(4*quat[3]);
        quat[1] = (m[2][0]-m[0][2])/(4*quat[3]);
        quat[2] = (m[0][1]-m[1][0])/(4*quat[3]);
    }
    else {
        // Compute x, y, or z first:
        j = (i+1)%3; k = (i+2)%3;
    
        // Compute first value:
        quat[i] = (float)Math.sqrt(m[i][i]-m[j][j]-m[k][k]+m[3][3])/2.0f;
       
        // And the others:
        quat[j] = (m[i][j]+m[j][i])/(4*quat[i]);
        quat[k] = (m[i][k]+m[k][i])/(4*quat[i]);

        quat[3] = (m[j][k]-m[k][j])/(4*quat[i]);
    }

//#ifdef DEBUG
    // Check to be sure output matches input:
    getValue(check);
    boolean ok = true;
    for (i = 0; i < 4 && ok; i++) {
        for (j = 0; j < 4 && ok; j++) {
            if (Math.abs(m[i][j]-check.getValue()[i][j]) > 1.0e-5)
                ok = false;
        }
    }
    if (!ok) {
        SoDebugError.post("SbRotation::setValue(const SbMatrix &)",
                           "Rotation does not agree with matrix; "+
                           "this routine only works with rotation "+
                           "matrices!");
    }
//#endif

    return (this);
}

	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies by another rotation.
//
// Use: public

public SbRotation 
operator_mul_equal(final SbRotation q)
//
////////////////////////////////////////////////////////////////////////
{
    float p0, p1, p2, p3;

    p0 = (q.quat[3] * quat[0] + q.quat[0] * quat[3] +
          q.quat[1] * quat[2] - q.quat[2] * quat[1]);
    p1 = (q.quat[3] * quat[1] + q.quat[1] * quat[3] +
          q.quat[2] * quat[0] - q.quat[0] * quat[2]);
    p2 = (q.quat[3] * quat[2] + q.quat[2] * quat[3] +
          q.quat[0] * quat[1] - q.quat[1] * quat[0]);
    p3 = (q.quat[3] * quat[3] - q.quat[0] * quat[0] -
          q.quat[1] * quat[1] - q.quat[2] * quat[2]);
    quat[0] = p0;
    quat[1] = p1;
    quat[2] = p2;
    quat[3] = p3;

    normalize();

    return(this);
}

	
	
	 //
	 // Description:
	 // Returns the norm (square of the 4D length) of the quaternion
	 // defining the rotation.
	 //
	 // Use: private
	 
	 private float norm()
	 //
	 {
	  return (quat[0] * quat[0] +
	  quat[1] * quat[1] +
	  quat[2] * quat[2] +
	  quat[3] * quat[3]);
	 }
	 
	//
	 // Description:
	 // Normalizes a rotation quaternion to unit 4D length.
	 //
	 // Use: private
	 
	private void normalize() {
		
		  float dist = 1.0f / (float)Math.sqrt(norm());
		   
		    quat[0] *= dist;
		    quat[1] *= dist;
		    quat[2] *= dist;
		    quat[3] *= dist;
		  	}
	
	//  Puts the given vector through this rotation
	// Multiplies the given vector by the matrix of this rotation. 
	public void multVec(final SbVec3f src, final SbVec3f dst) {
	     final SbMatrix myMat = new SbMatrix();
	          getValue( myMat );
	      
	          myMat.multVecMatrix( src, dst );
	     
	}

	public SbVec3f multVec(final SbVec3f src) { // java port
		final SbVec3f dst = new SbVec3f();
		multVec(src,dst);
		return dst;
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns 4 individual components of rotation quaternion.
//
// Use: public

public void getValue(final float[] q0q1q2q3)
//
////////////////////////////////////////////////////////////////////////
{
	q0q1q2q3[0] = quat[0];
	q0q1q2q3[1] = quat[1];
	q0q1q2q3[2] = quat[2];
	q0q1q2q3[3] = quat[3];
}

	
////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns corresponding 3D rotation matrix.
//
//Use: public

    //! Returns corresponding 4x4 rotation matrix
    public SbMatrix getMatrix() {
        final SbMatrix mat = new SbMatrix();
        getValue(mat);
        return mat;
    }
    
    //! Returns corresponding 3D rotation axis vector.
    public SbVec3f getAxis() { SbVec3f axis = new SbVec3f(); final float[] radians = new float[1]; getValue(axis, radians); return axis; }
    //! Returns corresponding angle in radians.
    public float   getAngle() { SbVec3f axis = new SbVec3f(); final float[] radians = new float[1]; getValue(axis, radians); return radians[0]; }    
    
	@Override
	public void copyFrom(Object other) {
		if(other instanceof SbMatrix) {
			other = new SbRotation((SbMatrix) other);
		}
		SbRotation otherRotation = (SbRotation)other;
		quat[0] = otherRotation.quat[0];
		quat[1] = otherRotation.quat[1];
		quat[2] = otherRotation.quat[2];
		quat[3] = otherRotation.quat[3];
	}
	
	/**
	 * Multiplication of two rotation
	 * @param q2
	 * @return
	 */
	public SbRotation operator_mul(SbRotation q2) {
		SbRotation q1 = this;
		
	    SbRotation q = new SbRotation(q2.quat[3] * q1.quat[0] + q2.quat[0] * q1.quat[3] +
	    		                     q2.quat[1] * q1.quat[2] - q2.quat[2] * q1.quat[1],
	    		   
	    		                     q2.quat[3] * q1.quat[1] + q2.quat[1] * q1.quat[3] +
	    		                     q2.quat[2] * q1.quat[0] - q2.quat[0] * q1.quat[2],
	    		   
	    		                     q2.quat[3] * q1.quat[2] + q2.quat[2] * q1.quat[3] +
	    		                     q2.quat[0] * q1.quat[1] - q2.quat[1] * q1.quat[0],
	    		   
	    		                     q2.quat[3] * q1.quat[3] - q2.quat[0] * q1.quat[0] -
	    		                     q2.quat[1] * q1.quat[1] - q2.quat[2] * q1.quat[2]);
	    		       q.normalize();
	    		   
	    		       return (q);
	    		   		
	}
	
	 /**
	  * Spherical linear interpolation: as t goes from 0 to 1, returned value goes from rot0 to rot1. 
	  * 
	  * @param rot0
	  * @param rot1
	  * @param t
	  * @return
	  */
	   //
	   // 
	   //
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Spherical linear interpolation: as t goes from 0 to 1, returned
	   //    value goes from rot0 to rot1.
	   //
	   // Use: public
	   
	   public static SbRotation
	   slerp(final SbRotation rot0, final SbRotation rot1, float t)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	           final float[]    r1q = rot1.getValue();
	   
	           final SbRotation      rot = new SbRotation();
	           float[]           rot1q = new float[4];
	           double          omega, cosom, sinom;
	           double          scalerot0, scalerot1;
	           int             i;
	   
	           // Calculate the cosine
	           cosom = rot0.quat[0]*rot1.quat[0] + rot0.quat[1]*rot1.quat[1]
	                   + rot0.quat[2]*rot1.quat[2] + rot0.quat[3]*rot1.quat[3];
	   
	           // adjust signs if necessary
	           if ( cosom < 0.0 ) {
	                   cosom = -cosom;
	                   for ( int j = 0; j < 4; j++ )
	                           rot1q[j] = -r1q[j];
	           } else  {
	                   for ( int j = 0; j < 4; j++ )
	                           rot1q[j] = r1q[j];
	           }
	   
	           // calculate interpolating coeffs
	           if ( (1.0 - cosom) > 0.00001 ) {
	                   // standard case
	                   omega = Math.acos(cosom);
	                   sinom = Math.sin(omega);
	                   scalerot0 = Math.sin((1.0 - t) * omega) / sinom;
	                   scalerot1 = Math.sin(t * omega) / sinom;
	           } else {
	                   // rot0 and rot1 very close - just do linear interp.
	                   scalerot0 = 1.0 - t;
	                  scalerot1 = t;
	           }
	   
	           // build the new quarternion
	           for (i = 0; i <4; i++)
	                   rot.quat[i] = (float)(scalerot0 * rot0.quat[i] + scalerot1 * rot1q[i]);
	   
	           return rot;
	   }
	   	
	     //! Returns a null rotation.
	   public     static SbRotation   identity()
	            { return new SbRotation(0.0f, 0.0f, 0.0f, 1.0f); }

	public boolean operator_not_equal(SbRotation value) {
		return !operator_equal_equal(value);
	}
	    
	public void setIdentity() {
		setValue(0, 0, 0, 1);
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Equality comparison operator.
//
// Use: public

public boolean
operator_equal_equal(final SbRotation q2)
//
////////////////////////////////////////////////////////////////////////
{
	final SbRotation q1 = this;
    return (q1.quat[0] == q2.quat[0] &&
            q1.quat[1] == q2.quat[1] &&
            q1.quat[2] == q2.quat[2] &&
            q1.quat[3] == q2.quat[3]);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Equality comparison operator within given tolerance - the square
//    of the length of the maximum distance between the two vectors.
//
// Use: public

public boolean
equals(final SbRotation r, float tolerance)
//
////////////////////////////////////////////////////////////////////////
{
    return new SbVec4f(quat).equals(new SbVec4f(r.quat), tolerance);
}



	     }
