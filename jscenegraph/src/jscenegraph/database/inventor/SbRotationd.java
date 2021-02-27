/**
 * 
 */
package jscenegraph.database.inventor;

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbRotationd implements Mutable { //TODO
	
	  private final double[]      quat = new double[4];        //!< Storage for quaternion components

	    public SbRotationd()
        {}

	    public SbRotationd(double q0, double q1, double q2, double q3)
        { 
	    	setValue(q0, q1, q2, q3); 
	    }

	    public SbRotationd(final SbMatrixd m)
        { 
	    	setValue(m); 
	    }

    //! \see SbRotationd(const SbVec3d &rotateFrom, const SbVec3d &rotateTo)
    public SbRotationd(final SbVec3d axis, double radians)
        { setValue(axis, radians); }



	/* (non-Javadoc)
	 * @see jscenegraph.port.Mutable#copyFrom(java.lang.Object)
	 */
	@Override
	public void copyFrom(Object other) {
		SbRotationd otherRotation = (SbRotationd)other;
		//TODO
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns 4 individual components of rotation quaternion.
//
// Use: public

public void getValue(final double[] q0q1q2q3)
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
// Description:
//    Returns corresponding 4x4 rotation matrix.
//
// Use: public

public void getValue(final SbMatrixd matrixM)
//
////////////////////////////////////////////////////////////////////////
{
	double[][] matrix = matrixM.getValue();
	
    matrix[0][0] = 1 - 2.0 * (quat[1] * quat[1] + quat[2] * quat[2]);
    matrix[0][1] =     2.0 * (quat[0] * quat[1] + quat[2] * quat[3]);
    matrix[0][2] =     2.0 * (quat[2] * quat[0] - quat[1] * quat[3]);
    matrix[0][3] = 0.0;

    matrix[1][0] =     2.0 * (quat[0] * quat[1] - quat[2] * quat[3]);
    matrix[1][1] = 1 - 2.0 * (quat[2] * quat[2] + quat[0] * quat[0]);
    matrix[1][2] =     2.0 * (quat[1] * quat[2] + quat[0] * quat[3]);
    matrix[1][3] = 0.0;

    matrix[2][0] =     2.0 * (quat[2] * quat[0] + quat[1] * quat[3]);
    matrix[2][1] =     2.0 * (quat[1] * quat[2] - quat[0] * quat[3]);
    matrix[2][2] = 1 - 2.0 * (quat[1] * quat[1] + quat[0] * quat[0]);
    matrix[2][3] = 0.0;

    matrix[3][0] = 0.0;
    matrix[3][1] = 0.0;
    matrix[3][2] = 0.0;
    matrix[3][3] = 1.0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value of rotation from 4 individual components of a
//    quaternion.
//
// Use: public

public SbRotationd setValue(double q0, double q1, double q2, double q3)
//
////////////////////////////////////////////////////////////////////////
{
    quat[0] = q0;
    quat[1] = q1;
    quat[2] = q2;
    quat[3] = q3;
    normalize();

    return (this);
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

SbRotationd setValue(final SbMatrixd mM)
//
////////////////////////////////////////////////////////////////////////
{
    int i, j, k;
    
    double[][]m = mM.getValue();

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
        quat[3] = Math.sqrt(m[0][0]+m[1][1]+m[2][2]+m[3][3])/2.0;

        // And compute other values:
        quat[0] = (m[1][2]-m[2][1])/(4*quat[3]);
        quat[1] = (m[2][0]-m[0][2])/(4*quat[3]);
        quat[2] = (m[0][1]-m[1][0])/(4*quat[3]);
    }
    else {
        // Compute x, y, or z first:
        j = (i+1)%3; k = (i+2)%3;
    
        // Compute first value:
        quat[i] = Math.sqrt(m[i][i]-m[j][j]-m[k][k]+m[3][3])/2.0;
       
        // And the others:
        quat[j] = (m[i][j]+m[j][i])/(4*quat[i]);
        quat[k] = (m[i][k]+m[k][i])/(4*quat[i]);

        quat[3] = (m[j][k]-m[k][j])/(4*quat[i]);
    }

//#ifdef DEBUG
    // Check to be sure output matches input:
    final SbMatrixd checkM = new SbMatrixd();
    getValue(checkM);
    double[][]check = checkM.getValue();
    boolean ok = true;
    for (i = 0; i < 4 && ok; i++) {
        for (j = 0; j < 4 && ok; j++) {
            if (Math.abs(m[i][j]-check[i][j]) > 1.0e-10)
                ok = false;
        }
    }
    if (!ok) {
        SoDebugError.post("SbRotationd::setValue(const SbMatrixd &)",
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
//    Sets value of rotation from 3D rotation axis vector and angle in
//    radians.
//
// Use: public

public SbRotationd setValue(final SbVec3d axis, double radians)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3d     q = new SbVec3d();

    q.copyFrom(axis);
    q.normalize();

    q.operator_mul_equal( Math.sin(radians / 2.0));

    quat[0] = q.operator_square_bracket(0);
    quat[1] = q.operator_square_bracket(1);
    quat[2] = q.operator_square_bracket(2);

    quat[3] = Math.cos(radians / 2.0);

    return(this);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the norm (square of the 4D length) of the quaternion
//    defining the rotation.
//
// Use: private

private double norm()
//
////////////////////////////////////////////////////////////////////////
{
    return (quat[0] * quat[0] +
            quat[1] * quat[1] +
            quat[2] * quat[2] +
            quat[3] * quat[3]);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Normalizes a rotation quaternion to unit 4D length.
//
// Use: private

private void normalize()
//
////////////////////////////////////////////////////////////////////////
{
    double       dist = 1.0 / Math.sqrt(norm());

    quat[0] *= dist;
    quat[1] *= dist;
    quat[2] *= dist;
    quat[3] *= dist;
}

}
