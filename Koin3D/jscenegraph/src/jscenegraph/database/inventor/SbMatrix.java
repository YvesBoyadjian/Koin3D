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

/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

/*!
  \class SbMatrix SbLinear.h Inventor/SbLinear.h
  \brief The SbMatrix class is a 4x4 dimensional representation of a matrix.
  \ingroup base

  SbMatrix is used by many other classes in Coin.  It provides storage
  for a 4x4 matrix of single-precision floating point values.

  By definition, matrices in Coin should be set up in column-order
  mode. This is the same order as used by e.g. OpenGL, but note that
  books on geometry often uses the opposite row-order mode, which can
  confuse new-comers to the API.

  Another way to think of column-order matrices is that they use
  post-order multiplications: that is, to concatenate a transformation
  from a second matrix with your current matrix, it should be
  multiplied on the right-hand side, i.e. with the
  SbMatrix::multRight() method.

  If you have a matrix in row-order from some other source, it can be
  "converted" to column-order by transposing it with
  SbMatrix::transpose(). A simple example will help to explain
  this.

  With row-order matrices, a transformation matrix with position,
  rotation and scale looks like this:

  \code
  M = T * R * S
  \endcode

  Where T is translation, R is rotation and S is the scale. What this
  means is that scale is applied first. The scaled matrix is then
  rotated, and finally the scaled and rotated matrix is
  translated. When using column-order matrices, as done in Coin,
  matrices are represented slightly differently; the order of
  multiplication is reversed:

  \code
  M = S * R * T
  \endcode

  The transformation is just the same as the row-order matrix. The
  only difference being the order of multiplication. To understand why
  this is so, consider the sample transformation:

  \code
  M = T * R * S
  \endcode

  Converting M from a row-order matrix to a column-order matrix is
  done as follows:

  \code
  M^t = (T * R * S)^t
  M^t = ((T * R) * S)^t
  M^t = S^t * (T * R)^t
  M^t = S^t * R^t * T^t
  \endcode

  All left to be done is to remove the transpose symbols, and the
  matrices have been converted to column-order matrices:

  \code
  M = S * R * T
  \endcode

  This was done using the fact that:

  \code
  A^t = (B * C)^t = C^t * B^t
  \endcode

  Converting from column-order to row-order is done using the same
  principle.
*/

// FIXME:
//
//  * The SbMatrix::factor() function has not been implemented yet.
//
//  * The element access methods should be inlined.
//
//  * Not a lot of optimizations have been done yet, so there's a lot
//    of room for improvements.


package jscenegraph.database.inventor;

import java.util.function.DoubleConsumer;

import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! 4x4 matrix class.
/*!
\class SbMatrix
\ingroup Basics
4x4 matrix class/datatype used by many Inventor node and action classes.
The matrices are stored in row-major order.
{}

\par See Also
\par
SbVec3f, SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbMatrix implements Mutable {

	private final float[][] matrix = new float[4][4];
	
	// Default constructor. 
	public SbMatrix() {
		
	}
	
	// Constructor given all 16 elements in row-major order. 
	public SbMatrix(float a11, float a12, float a13, float a14,
			  float a21, float a22, float a23, float a24,
			  float a31, float a32, float a33, float a34,
			  float a41, float a42, float a43, float a44) {
		
		  matrix[0][0] = a11;
		    matrix[0][1] = a12;
		    matrix[0][2] = a13;
		    matrix[0][3] = a14;
		   
		    matrix[1][0] = a21;
		    matrix[1][1] = a22;
		    matrix[1][2] = a23;
		    matrix[1][3] = a24;
		   
		    matrix[2][0] = a31;
		    matrix[2][1] = a32;
		    matrix[2][2] = a33;
		    matrix[2][3] = a34;
		   
		    matrix[3][0] = a41;
		    matrix[3][1] = a42;
		    matrix[3][2] = a43;
		    matrix[3][3] = a44;
		   	}
		   	
	 //
	   // Constructor from a 4x4 array of elements
	   //
	   
	  	public SbMatrix(float[][] m) {
	  	     matrix[0][0] = m[0][0];
	  	        matrix[0][1] = m[0][1];
	  	        matrix[0][2] = m[0][2];
	  	        matrix[0][3] = m[0][3];
	  	        matrix[1][0] = m[1][0];
	  	        matrix[1][1] = m[1][1];
	  	        matrix[1][2] = m[1][2];
	  	        matrix[1][3] = m[1][3];
	  	        matrix[2][0] = m[2][0];
	  	        matrix[2][1] = m[2][1];
	  	        matrix[2][2] = m[2][2];
	  	        matrix[2][3] = m[2][3];
	  	        matrix[3][0] = m[3][0];
	  	        matrix[3][1] = m[3][1];
	  	        matrix[3][2] = m[3][2];
	  	        matrix[3][3] = m[3][3];	  	   		
	}
	  	
	  	/**
	  	 * Copy contructor (java port)
	  	 * 
	  	 * @param other
	  	 */
	  	public SbMatrix(final SbMatrix other) {
	  		float[][] m = other.matrix;
	  		setValue(m);
	  	}
	  	
	  	// Sets value from 4x4 array of elements. 
	  	public void setValue(float[][] m) {
	  	     matrix[0][0] = m[0][0];
	  	        matrix[0][1] = m[0][1];
	  	        matrix[0][2] = m[0][2];
	  	        matrix[0][3] = m[0][3];
	  	        matrix[1][0] = m[1][0];
	  	        matrix[1][1] = m[1][1];
	  	        matrix[1][2] = m[1][2];
	  	        matrix[1][3] = m[1][3];
	  	        matrix[2][0] = m[2][0];
	  	        matrix[2][1] = m[2][1];
	  	        matrix[2][2] = m[2][2];
	  	        matrix[2][3] = m[2][3];
	  	        matrix[3][0] = m[3][0];
	  	        matrix[3][1] = m[3][1];
	  	        matrix[3][2] = m[3][2];
	  	        matrix[3][3] = m[3][3];	  	   	  		
	  	}
	
	// Sets matrix to be identity. 
	public void makeIdentity() {
	     matrix[0][0] = 1.0f;
	          matrix[0][1] = 0.0f;
	          matrix[0][2] = 0.0f;
	          matrix[0][3] = 0.0f;
	          matrix[1][0] = 0.0f;
	          matrix[1][1] = 1.0f;
	          matrix[1][2] = 0.0f;
	          matrix[1][3] = 0.0f;
	          matrix[2][0] = 0.0f;
	          matrix[2][1] = 0.0f;
	          matrix[2][2] = 1.0f;
	          matrix[2][3] = 0.0f;
	          matrix[3][0] = 0.0f;
	          matrix[3][1] = 0.0f;
	          matrix[3][2] = 0.0f;
	          matrix[3][3] = 1.0f;	     		
	}
	
	// Sets matrix to rotate by given rotation. 
	public void setRotate(final SbRotation rotation) {
	     rotation.getValue(this);
	}
	
//
// Sets matrix to scale by given vector
//

public void setScale(final SbVec3f s)
{
    matrix[0][0] = s.getValueRead()[0];
    matrix[0][1] = 0.0f;
    matrix[0][2] = 0.0f;
    matrix[0][3] = 0.0f;
    matrix[1][0] = 0.0f;
    matrix[1][1] = s.getValueRead()[1];
    matrix[1][2] = 0.0f;
    matrix[1][3] = 0.0f;
    matrix[2][0] = 0.0f;
    matrix[2][1] = 0.0f;
    matrix[2][2] = s.getValueRead()[2];
    matrix[2][3] = 0.0f;
    matrix[3][0] = 0.0f;
    matrix[3][1] = 0.0f;
    matrix[3][2] = 0.0f;
    matrix[3][3] = 1.0f;
}	
	
	// Sets matrix to translate by given vector. 
	public void setTranslate(final SbVec3f tv) {
		//float[] t = tv.vec;
	     matrix[0][0] = 1.0f;
	          matrix[0][1] = 0.0f;
	          matrix[0][2] = 0.0f;
	          matrix[0][3] = 0.0f;
	          matrix[1][0] = 0.0f;
	          matrix[1][1] = 1.0f;
	          matrix[1][2] = 0.0f;
	          matrix[1][3] = 0.0f;
	          matrix[2][0] = 0.0f;
	          matrix[2][1] = 0.0f;
	          matrix[2][2] = 1.0f;
	          matrix[2][3] = 0.0f;
	          matrix[3][0] = tv.getX();//[0];
	          matrix[3][1] = tv.getY();//[1];
	          matrix[3][2] = tv.getZ();//t[2];
	          matrix[3][3] = 1.0f;	     		
	}
	
	// java port
	public final float[][] getValue() {
		return matrix;
	}
	
	public final float[] toGL() {
		float[] toGL = new float[16];
		toGL[0] = matrix[0][0];
		toGL[1] = matrix[0][1];
		toGL[2] = matrix[0][2];
		toGL[3] = matrix[0][3];
		toGL[4] = matrix[1][0];
		toGL[5] = matrix[1][1];
		toGL[6] = matrix[1][2];
		toGL[7] = matrix[1][3];
		toGL[8] = matrix[2][0];
		toGL[9] = matrix[2][1];
		toGL[10] = matrix[2][2];
		toGL[11] = matrix[2][3];
		toGL[12] = matrix[3][0];
		toGL[13] = matrix[3][1];
		toGL[14] = matrix[3][2];
		toGL[15] = matrix[3][3];
		return toGL;
	}
	
//
// Return an identity matrix
//

public static SbMatrix
identity()    
{
    return new SbMatrix(1.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 
                    0.0f, 0.0f, 0.0f, 1.0f);
}

	
	
	//
	// Macro for checking is a matrix is idenity.
	//

	public boolean IS_IDENTITY(float[][] matrix) { return ( 
		    (matrix[0][0] == 1.0) && 
		    (matrix[0][1] == 0.0) && 
		    (matrix[0][2] == 0.0) && 
		    (matrix[0][3] == 0.0) && 
		    (matrix[1][0] == 0.0) && 
		    (matrix[1][1] == 1.0) && 
		    (matrix[1][2] == 0.0) && 
		    (matrix[1][3] == 0.0) && 
		    (matrix[2][0] == 0.0) && 
		    (matrix[2][1] == 0.0) && 
		    (matrix[2][2] == 1.0) && 
		    (matrix[2][3] == 0.0) && 
		    (matrix[3][0] == 0.0) && 
		    (matrix[3][1] == 0.0) && 
		    (matrix[3][2] == 0.0) && 
		    (matrix[3][3] == 1.0));
	}
	
	/**
	 * Returns inverse of matrix. 
	 * Results are undefined for singular matrices. 
	 * Uses LU decomposition. 
	 * 
	 * @return
	 */
//
// Returns inverse of matrix. Results are undefined for
// singular matrices.  Uses LU decomposition, from Numerial Recipies in C,
// pp 43-45.  They say "There is no better way."
//
// Oh, yeah! Well, if the matrix is affine, there IS a better way.
// So we call affine_inverse to see if we can get away with it...
//

public SbMatrix
inverse()
{
    // Trivial case
    if (IS_IDENTITY(matrix))
        return SbMatrix.identity();

    // Affine case...
    final SbMatrix affineAnswer = new SbMatrix();
    if (  affine_inverse( new SbMatrix(matrix), affineAnswer ) )
        return affineAnswer;

    int[]         index = new int[4];
    final float[]       d = new float[1];
    float[][] invmat = new float[4][4];
    float temp;
    SbMatrix    inverse = new SbMatrix(this);
//#ifdef DEBUGGING
    int         i, j;
//#endif /* DEBUGGING */

    if(inverse.LUDecomposition(index, d)) {

//#ifdef DEBUGGING
        for(j = 0; j < 4; j++) {
            for(i = 0; i < 4; i++)
                invmat[j][i] = 0.0f;
            invmat[j][j] = 1.0f;
            inverse.LUBackSubstitution(index, invmat[j]);
        }
//#else
//        invmat[0][0] = 1.0;
//        invmat[0][1] = 0.0;
//        invmat[0][2] = 0.0;
//        invmat[0][3] = 0.0;
//        inverse.LUBackSubstitution(index, invmat[0]);
//        invmat[1][0] = 0.0;
//        invmat[1][1] = 1.0;
//        invmat[1][2] = 0.0;
//        invmat[1][3] = 0.0;
//        inverse.LUBackSubstitution(index, invmat[1]);
//        invmat[2][0] = 0.0;
//        invmat[2][1] = 0.0;
//        invmat[2][2] = 1.0;
//        invmat[2][3] = 0.0;
//        inverse.LUBackSubstitution(index, invmat[2]);
//        invmat[3][0] = 0.0;
//        invmat[3][1] = 0.0;
//        invmat[3][2] = 0.0;
//        invmat[3][3] = 1.0;
//        inverse.LUBackSubstitution(index, invmat[3]);
//#endif /* DEBUGGING */

//#ifdef DEBUGGING
        // transpose invmat
        for(j = 0; j < 4; j++) {
            for(i = 0; i < j; i++) {
                temp = invmat[i][j];
                invmat[i][j] = invmat[j][i];
                invmat[j][i] = temp;
            }
        }
//#else
//#define SWAP(i,j) \
//        temp = invmat[i][j]; \
//        invmat[i][j] = invmat[j][i]; \
//        invmat[j][i] = temp;
//
//        SWAP(1,0);
//        
//        SWAP(2,0);
//        SWAP(2,1);
//        
//        SWAP(3,0);
//        SWAP(3,1);
//        SWAP(3,2);
//#undef SWAP     
//#endif /* DEBUGGING */
        inverse.setValue(invmat);
    }

    return inverse;
}

// The following method finds the inverse of an affine matrix.
// The last column MUST be [0 0 0 1] for this to work.
// This is taken from graphics gems 2, page 603
//
// computes the inverse of a 3d affine matrix; i.e. a matrix with a 
// dimensionality of 4 where the right column has the entries (0,0,0,1).
//
// This procedure treats the 4 by 4 matrix as a block matrix and calculates
// the inverse of one submatrix for a significant performance 
// improvement over a general procedure that can invert any nonsingular matrix.
//
//             -1 
//  -1   |    |      |  -1    |
// M   = |A  0|  =   | A     0|
//       |    |      |        |
//       |    |      |   -1   |
//       |C  1|      |-CA    1|
//
// where   M is a 4 by 4 matrix,
//         A is the 3 by 3 upper left submatrix of M,
//         C is the 1 by 3 lower left submatrix of M.
// Input:
//   in - 3D affine matrix
// Output:
//   out - inverse of 3D affine matrix
// Returned Value:
//   TRUE  if input matrix is nonsingular and affine
//   FALSE otherwise

public boolean
affine_inverse(final SbMatrix inM, final SbMatrix outM)
{
	final float[][] in = inM.matrix; // java port
	final float[][] out = outM.matrix; // java port
    // Check if matrix is affine...
    if ( in[0][3] != 0.0 || in[1][3] != 0.0 || in[2][3] != 0.0 || 
         in[3][3] != 1.0 )
        return false;

    // Calculate the determinant of submatrix A and determine if the matrix
    // is singular as limited by the double precision floating 
    // point data representation

    double det_1;
    double pos, neg, temp;

//#define ACCUMULATE 
//    if (temp >= 0.0) 
//        pos += temp; 
//    else             
//        neg += temp;

    pos = neg = 0.0;
    temp =  (double)in[0][0] * in[1][1] * in[2][2];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    temp =  (double)in[0][1] * in[1][2] * in[2][0];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    temp =  (double)in[0][2] * in[1][0] * in[2][1];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    temp = -(double)in[0][2] * in[1][1] * in[2][0];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    temp = -(double)in[0][1] * in[1][0] * in[2][2];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    temp = -(double)in[0][0] * in[1][2] * in[2][1];
    //ACCUMULATE
    if (temp >= 0.0) 
        pos += temp; 
    else             
        neg += temp;
    det_1 = pos + neg;

//#undef ACCUMULATE

final double PRECISION_LIMIT = (1.0e-15f);

    // Is the submatrix A singular?
    temp = det_1 / (pos - neg);
    if (Math.abs(temp) < PRECISION_LIMIT)
        return false;

    // Calculate inverse(A) = adj(A) / det(A)
    det_1 = 1.0 / det_1;
    out[0][0] = (float)( ((double)in[1][1] * in[2][2] - in[1][2] * in[2][1]) * det_1);
    out[1][0] = (float)(-((double)in[1][0] * in[2][2] - in[1][2] * in[2][0]) * det_1);
    out[2][0] = (float)( ((double)in[1][0] * in[2][1] - in[1][1] * in[2][0]) * det_1);
    out[0][1] = (float)(-((double)in[0][1] * in[2][2] - in[0][2] * in[2][1]) * det_1);
    out[1][1] = (float)( ((double)in[0][0] * in[2][2] - in[0][2] * in[2][0]) * det_1);
    out[2][1] = (float)(-((double)in[0][0] * in[2][1] - in[0][1] * in[2][0]) * det_1);
    out[0][2] = (float)( ((double)in[0][1] * in[1][2] - in[0][2] * in[1][1]) * det_1);
    out[1][2] = (float)(-((double)in[0][0] * in[1][2] - in[0][2] * in[1][0]) * det_1);
    out[2][2] = (float)( ((double)in[0][0] * in[1][1] - in[0][1] * in[1][0]) * det_1);

    // Calculate -C * inverse(A)
    out[3][0] = (float)-( (double)in[3][0] * out[0][0] + 
                   in[3][1] * out[1][0] + 
                   in[3][2] * out[2][0] );
    out[3][1] = (float)-( (double)in[3][0] * out[0][1] + 
                   in[3][1] * out[1][1] + 
                   in[3][2] * out[2][1] );
    out[3][2] = (float)-( (double)in[3][0] * out[0][2] + 
                   in[3][1] * out[1][2] + 
                   in[3][2] * out[2][2] );

    // Fill in last column
    out[0][3] = out[1][3] = out[2][3] = 0.0f;
    out[3][3] = 1.0f;

//#undef PRECISION_LIMIT

    return true;
}


// MEVISLAB VERSION : NOT USED
// Perform an LU decomposition of a matrix.  From Numerical Recipes in C, pg 43
// Destroys the original matrix.  The resulting matrix is as follows
//
//      a b c d         1 0 0 0      a b c d
// LU = e f g h     L = e 1 0 0  U = 0 f g h
//      i j k l         i j 1 0      0 0 k l
//      m n o p         m n o 1      0 0 0 p
//
// For singular matrices, want to calculate something
// that is close to the inverse, so we can pick
// things scaled to zero in some dimension.
//
public boolean
LUDecomposition(final int index[/*4*/], final float[] d)
{
    int         imax = -1; //java port
    float       big, dum, sum, temp;
    float[]       vv = new float[4];
//#ifdef DEBUGGING
    int         i, j, k;
//#endif /* DEBUGGING */

    d[0] = 1.0f;
    
//#ifdef DEBUGGING
    for(i = 0; i < 4; i++) {
        big = 0.0f;
        for(j = 0; j < 4; j++)
            if((temp = Math.abs(matrix[i][j])) > big) big = temp;
        if(big == 0.0) {
            matrix[i][i] = 1e-6f;
            big = matrix[i][i];
        }
        vv[i] = 1.0f / big;
    }
//#else
//#define COMPUTE_VV(i) \
//    big = 0.0; \
//    if((temp = ABS(matrix[i][0])) > big) big = temp; \
//    if((temp = ABS(matrix[i][1])) > big) big = temp; \
//    if((temp = ABS(matrix[i][2])) > big) big = temp; \
//    if((temp = ABS(matrix[i][3])) > big) big = temp; \
//    if(big == 0.0) { \
//        matrix[i][i] = 1e-6; \
//        big = matrix[i][i]; \
//    } \
//    vv[i] = 1.0 / big;
//
//    COMPUTE_VV(0);      
//    COMPUTE_VV(1);      
//    COMPUTE_VV(2);      
//    COMPUTE_VV(3);
//#undef COMPUTE_VV       
//#endif /* DEBUGGING */

//#ifdef DEBUGGING
    // This is the code as it originally existed.
    // Below this is the unrolled, really unreadable version.

    for(j = 0; j < 4; j++) {

        // BLOCK 1
        for(i = 0; i < j; i++) {
            sum = matrix[i][j];
            for(k = 0; k < i; k++)
                sum -= matrix[i][k] * matrix[k][j];
            matrix[i][j] = sum;
        }

        big = 0.0f;

        // BLOCK 2
        for(i = j; i < 4; i++) {
            sum = matrix[i][j];
            for(k = 0; k < j; k++)
                sum -= matrix[i][k] * matrix[k][j];
            matrix[i][j] = sum;
            if((dum = vv[i] * Math.abs(sum)) >= big) {
                big = dum;
                imax = i;
            }
        }

        // BLOCK 3
        if(j != imax) {
            for(k = 0; k < 4; k++) {
                dum = matrix[imax][k];
                matrix[imax][k] = matrix[j][k];
                matrix[j][k] = dum;
            }
            d[0] = -d[0];
            vv[imax] = vv[j];
        }

        // BLOCK 4
        index[j] = imax;
        if(matrix[j][j] == 0.0) matrix[j][j] = 1e-20f;
        
        // BLOCK 5
        if(j != 4 - 1) {
            dum = 1.0f / (matrix[j][j]);
            for(i = j + 1; i < 4; i++)
                matrix[i][j] *= dum;
        }
        
    }
    
//#else
//
//    // This is the completely unreadable, but much faster
//    // version of the above code. First, some macros that
//    // never change.
//    
//// macro for block 3, inner k loop
//#define BLOCK3INNER(j,k) \
//    dum = matrix[imax][k]; \
//    matrix[imax][k] = matrix[j][k]; \
//    matrix[j][k] = dum;
//
//// macro for block 4
//#define BLOCK4(j) \
//    index[j] = imax; \
//    if(matrix[j][j] == 0.0) matrix[j][j] = 1e-20;
//
//    // Now the code...
//
//    // *********************************************
//    // j = 0
//    // *********************************************
//
//    // BLOCK 1, j = 0
//    // does nothing when j==0
//
//    big = 0.0;
//
//    // BLOCK 2, j = 0
//    
//// macro for block 2 when j == 0
//// inner k loop does nothing when j == 0
//#define BLOCK2J0(i) \
//    sum = matrix[i][0]; \
//    if((dum = vv[i] * ABS(sum)) >= big) { \
//        big = dum; \
//        imax = i; \
//    }
//        
//    // for(i = j; i < 4; i++)
//    BLOCK2J0(0)
//    BLOCK2J0(1)
//    BLOCK2J0(2)
//    BLOCK2J0(3)
//#undef BLOCK2J0
//    
//    // BLOCK 3, j = 0
//    if(0 != imax) {
//        BLOCK3INNER(0,0);
//        BLOCK3INNER(0,1);
//        BLOCK3INNER(0,2);
//        BLOCK3INNER(0,3);
//        d = -d;
//        vv[imax] = vv[0];
//    }
//
//    // BLOCK 4, j = 0
//    BLOCK4(0);
//
//    // BLOCK 5, j = 0
//    dum = 1.0 / (matrix[0][0]);
//    // for(i = j + 1; i < 4; i++)
//        matrix[1][0] *= dum;
//        matrix[2][0] *= dum;
//        matrix[3][0] *= dum;
//
//    // *********************************************
//    // j = 1
//    // *********************************************
//
//    // BLOCK 1, j = 1
//    // for(i = 0; i < j; i++) {
//    sum = matrix[0][1];
//    // for(k = 0; k < i; k++)
//        // nothing    
//
//    big = 0.0;
//
//    // BLOCK 2, j = 1
//    
//// macro for block 2 when j == 1
//#define BLOCK2J1(i) \
//    sum = matrix[i][1]; \
//    sum -= matrix[i][0] * matrix[0][1]; \
//    matrix[i][1] = sum; \
//    if((dum = vv[i] * ABS(sum)) >= big) { \
//        big = dum; \
//        imax = i; \
//    }
//        
//    // for(i = j; i < 4; i++)
//    BLOCK2J1(1)
//    BLOCK2J1(2)
//    BLOCK2J1(3)
//#undef BLOCK2J1
//    
//    // BLOCK 3, j = 1
//    if(1 != imax) {
//        BLOCK3INNER(1,0);
//        BLOCK3INNER(1,1);
//        BLOCK3INNER(1,2);
//        BLOCK3INNER(1,3);
//        d = -d;
//        vv[imax] = vv[1];
//    }
//
//    // BLOCK 4, j = 1
//    BLOCK4(1);
//
//    // BLOCK 5, j = 1
//    dum = 1.0 / (matrix[1][1]);
//    // for(i = j + 1; i < 4; i++)
//        matrix[2][1] *= dum;
//        matrix[3][1] *= dum;
//    
//    // *********************************************
//    // j = 2
//    // *********************************************
//
//    // BLOCK 1, j = 2
//    // for(i = 0; i < j; i++) {
//        // i = 0
//        sum = matrix[0][2];
//        // for(k = 0; k < i; k++)
//            // nothing
//        // i = 1            
//        sum = matrix[1][2];
//        // for(k = 0; k < i; k++)
//            sum -= matrix[1][0] * matrix[0][2];
//        matrix[1][2] = sum;
//        
//    big = 0.0;
//
//    // BLOCK 2, j = 2
//    
//// macro for block 2 when j == 2
//#define BLOCK2J2(i) \
//    sum = matrix[i][2]; \
//    sum -= matrix[i][0] * matrix[0][2]; \
//    sum -= matrix[i][1] * matrix[1][2]; \
//    matrix[i][2] = sum; \
//    if((dum = vv[i] * ABS(sum)) >= big) { \
//        big = dum; \
//        imax = i; \
//    }
//        
//    // for(i = j; i < 4; i++)
//    BLOCK2J2(2)
//    BLOCK2J2(3)
//#undef BLOCK2J2
//    
//    // BLOCK 3, j = 2
//    if(2 != imax) {
//        BLOCK3INNER(2,0);
//        BLOCK3INNER(2,1);
//        BLOCK3INNER(2,2);
//        BLOCK3INNER(2,3);
//        d = -d;
//        vv[imax] = vv[2];
//    }
//
//    // BLOCK 4, j = 2
//    BLOCK4(2);
//
//    // BLOCK 5, j = 2
//    dum = 1.0 / (matrix[2][2]);
//    // for(i = j + 1; i < 4; i++)
//        matrix[3][2] *= dum;
//
//    // *********************************************
//    // j = 3
//    // *********************************************
//
//    // BLOCK 1, j = 3
//    // for(i = 0; i < j; i++) {
//        // i = 0
//            sum = matrix[0][3];
//            // for(k = 0; k < i; k++)
//                // nothing
//        // i = 1            
//            sum = matrix[1][3];
//            // for(k = 0; k < i; k++)
//                sum -= matrix[1][0] * matrix[0][3];
//            matrix[1][3] = sum;
//        // i = 2
//            sum = matrix[2][3];
//            // for(k = 0; k < i; k++)
//                sum -= matrix[2][0] * matrix[0][3];
//                sum -= matrix[2][1] * matrix[1][3];
//            matrix[2][3] = sum;
//        
//    big = 0.0;
//
//    // BLOCK 2, j = 3
//    
//// macro for block 2 when j == 3
//#define BLOCK2J3(i) \
//    sum = matrix[i][3]; \
//    sum -= matrix[i][0] * matrix[0][3]; \
//    sum -= matrix[i][1] * matrix[1][3]; \
//    sum -= matrix[i][2] * matrix[2][3]; \
//    matrix[i][3] = sum; \
//    if((dum = vv[i] * ABS(sum)) >= big) { \
//        big = dum; \
//        imax = i; \
//    }
//        
//    // for(i = j; i < 4; i++)
//    BLOCK2J3(3)
//#undef BLOCK2J3
//    
//    // BLOCK 3, j = 3
//    if(3 != imax) {
//        BLOCK3INNER(3,0);
//        BLOCK3INNER(3,1);
//        BLOCK3INNER(3,2);
//        BLOCK3INNER(3,3);
//        d = -d;
//        vv[imax] = vv[3];
//    }
//
//    // BLOCK 4, j = 3
//    BLOCK4(3);
//
//    // BLOCK 5, j = 3
//    // does not execute when j == 3
//    
//#endif /* DEBUGGING */

    return true;
}

/*! COIN3D version
  This function produces a permuted LU decomposition of the matrix.  It
  uses the common single-row-pivoting strategy.

  \a FALSE is returned if the matrix is singular, which it never is, because
  of small adjustment values inserted if a singularity is found (as Open
  Inventor does too).

  The parity argument is always set to 1.0 or -1.0.  Don't really know what
  it's for, so it's not checked for correctness.

  The index[] argument returns the permutation that was done on the matrix
  to LU-decompose it.  index[i] is the row that row i was swapped with at
  step i in the decomposition, so index[] is not the actual permutation of
  the row indexes!

  BUGS:  The function does not produce results that are numerically identical
  with those produced by Open Inventor for the same matrices, because the
  pivoting strategy in OI was never fully understood.

  \sa SbMatrix::LUBackSubstitution
*/


//public boolean
//LUDecomposition(final int[] index, final float[] d)
//{
//    int i;
//    for (i = 0; i < 4; i++) index[i] = i;
//    d[0] = 1.0f;
//
//    float MINIMUM_PIVOT = 1e-6f; // Inventor fix...
//
//    for (int row = 1; row < 4; row++) {
//        int swap_row = row;
//        float max_pivot = 0.0f;
//        for (int test_row = row; test_row < 4; test_row++) {
//            float test_pivot = Math.abs(matrix[test_row][row]);
//            if (test_pivot > max_pivot) {
//                swap_row = test_row;
//                max_pivot = test_pivot;
//            }
//        }
//
//        // swap rows
//        if (swap_row != row) {
//            d[0] = -d[0];
//            index[row] = swap_row;
//            for (i = 0; i < 4; i++) {
//                float swap = matrix[row][i];
//                matrix[row][i] = matrix[swap_row][i];
//                matrix[swap_row][i] = swap;
//            }
//        }
//
//        float pivot = matrix[row][row];
//        if (matrix[row][row] == 0.0f) {
////            return FALSE;
//            // instead of returning FALSE on singulars...
//            matrix[row][row] = pivot = MINIMUM_PIVOT;
//        }
//
//        // the factorization
//        for (i = row + 1; i < 4; i++) {
//            float factor = (matrix[i][row] /= pivot);
//            for (int j = row + 1; j < 4; j++)
//                matrix[i][j] -= factor * matrix[row][j];
//        }
//    }
//    return true;
//}


//
// Perform back-subtitution on input LU matrix, from Numerial Recipies in C,
// pg 44
//
public void
LUBackSubstitution(int index[/*4*/], float b[/*4*/])
{
    int         ii = -1, ip, j;
    float       sum;
//#ifdef DEBUGGING
    int         i;
//#endif /* DEBUGGING */

//#ifdef DEBUGGING
    for(i = 0; i < 4; i++) {
        ip = index[i];
        sum = b[ip];
        b[ip] = b[i];
        if(ii >= 0)
            for(j = ii; j <= i - 1; j++)
                sum -= matrix[i][j] * b[j];
        else if(sum != 0) ii = i;
        b[i] = sum;
    }
//#else
//#define BACKSUB(i) \
//    ip = index[i]; \
//    sum = b[ip]; \
//    b[ip] = b[i]; \
//    if(ii >= 0) \
//        for(j = ii; j <= i - 1; j++) \
//            sum -= matrix[i][j] * b[j]; \
//    else if(sum) ii = i; \
//    b[i] = sum;
//
//    BACKSUB(0);
//    BACKSUB(1);
//    BACKSUB(2);
//    BACKSUB(3);
//#undef BACKSUB
//#endif /* DEBUGGING */

//#ifdef DEBUGGING
    for(i = 4 - 1; i >= 0; i--) {
        sum = b[i];
        for(j = i + 1; j < 4; j++)
            sum -= matrix[i][j]*b[j];
        b[i] = sum / matrix[i][i];
    }
//#else
//    sum = b[3];
//    b[3] = sum / matrix[3][3];
//
//    sum = b[2] - matrix[2][3]*b[3];
//    b[2] = sum / matrix[2][2];
//
//    sum = b[1] - matrix[1][2]*b[2] - matrix[1][3]*b[3];
//    b[1] = sum / matrix[1][1];
//        
//    sum = b[0] - matrix[0][1]*b[1] - matrix[0][2]*b[2] - matrix[0][3]*b[3];
//    b[0] = sum / matrix[0][0];
//#endif /* DEBUGGING */
}

	
	public static boolean IS_IDENTITY(SbMatrix m) {
		
		float[][] matrix = m.getValue();
		
		return ( 
			    (matrix[0][0] == 1.0) && 
			    (matrix[0][1] == 0.0) && 
			    (matrix[0][2] == 0.0) && 
			    (matrix[0][3] == 0.0) && 
			    (matrix[1][0] == 0.0) && 
			    (matrix[1][1] == 1.0) && 
			    (matrix[1][2] == 0.0) && 
			    (matrix[1][3] == 0.0) && 
			    (matrix[2][0] == 0.0) && 
			    (matrix[2][1] == 0.0) && 
			    (matrix[2][2] == 1.0) && 
			    (matrix[2][3] == 0.0) && 
			    (matrix[3][0] == 0.0) && 
			    (matrix[3][1] == 0.0) && 
			    (matrix[3][2] == 0.0) && 
			    (matrix[3][3] == 1.0));

	}
	
	public float MULT_RIGHT( int i, int j, SbMatrix m) {
		return (matrix[i][0]*m.matrix[0][j] + 
            matrix[i][1]*m.matrix[1][j] + 
            matrix[i][2]*m.matrix[2][j] + 
            matrix[i][3]*m.matrix[3][j]);
	}
	
	// Multiplies matrix by given matrix on right. 
	public SbMatrix multRight(final SbMatrix m) {
		
	     // Trivial cases
		      if (IS_IDENTITY(m))
		          return this;
		      else if (IS_IDENTITY(/*matrix*/this)) // java port
		          return (this.copyFrom(m.matrix));
		          
		      final float[][]       tmp = new float[4][4];
		  
//		  #define MULT_RIGHT(i,j) (matrix[i][0]*m.matrix[0][j] + 
//		                           matrix[i][1]*m.matrix[1][j] + 
//		                           matrix[i][2]*m.matrix[2][j] + 
//		                           matrix[i][3]*m.matrix[3][j])
		  
		      tmp[0][0] = MULT_RIGHT(0,0,m);
		      tmp[0][1] = MULT_RIGHT(0,1,m);
		      tmp[0][2] = MULT_RIGHT(0,2,m);
		      tmp[0][3] = MULT_RIGHT(0,3,m);
		      tmp[1][0] = MULT_RIGHT(1,0,m);
		      tmp[1][1] = MULT_RIGHT(1,1,m);
		      tmp[1][2] = MULT_RIGHT(1,2,m);
		      tmp[1][3] = MULT_RIGHT(1,3,m);
		      tmp[2][0] = MULT_RIGHT(2,0,m);
		      tmp[2][1] = MULT_RIGHT(2,1,m);
		      tmp[2][2] = MULT_RIGHT(2,2,m);
		      tmp[2][3] = MULT_RIGHT(2,3,m);
		      tmp[3][0] = MULT_RIGHT(3,0,m);
		      tmp[3][1] = MULT_RIGHT(3,1,m);
		      tmp[3][2] = MULT_RIGHT(3,2,m);
		      tmp[3][3] = MULT_RIGHT(3,3,m);
		  
//		  #undef MULT_RIGHT
		  
		      return (this.copyFrom(tmp));
		 	}

public float MULT_LEFT(int i,int j, SbMatrix m) {
	return m.matrix[i][0]*matrix[0][j] + 
                        m.matrix[i][1]*matrix[1][j] + 
                        m.matrix[i][2]*matrix[2][j] + 
                        m.matrix[i][3]*matrix[3][j];
}
	
//
// Multiplies matrix by given matrix on left
//

public SbMatrix 
multLeft(final SbMatrix m)
{
    // Trivial cases
    if (IS_IDENTITY(m))
        return this;
    else if (IS_IDENTITY(matrix)) {
        this.copyFrom(m);
        return this;
    }
    final float[][]       tmp= new float[4][4];

//#define MULT_LEFT(i,j) (m.matrix[i][0]*matrix[0][j] + \
//                        m.matrix[i][1]*matrix[1][j] + \
//                        m.matrix[i][2]*matrix[2][j] + \
//                        m.matrix[i][3]*matrix[3][j])

    tmp[0][0] = MULT_LEFT(0,0,m);
    tmp[0][1] = MULT_LEFT(0,1,m);
    tmp[0][2] = MULT_LEFT(0,2,m);
    tmp[0][3] = MULT_LEFT(0,3,m);
    tmp[1][0] = MULT_LEFT(1,0,m);
    tmp[1][1] = MULT_LEFT(1,1,m);
    tmp[1][2] = MULT_LEFT(1,2,m);
    tmp[1][3] = MULT_LEFT(1,3,m);
    tmp[2][0] = MULT_LEFT(2,0,m);
    tmp[2][1] = MULT_LEFT(2,1,m);
    tmp[2][2] = MULT_LEFT(2,2,m);
    tmp[2][3] = MULT_LEFT(2,3,m);
    tmp[3][0] = MULT_LEFT(3,0,m);
    tmp[3][1] = MULT_LEFT(3,1,m);
    tmp[3][2] = MULT_LEFT(3,2,m);
    tmp[3][3] = MULT_LEFT(3,3,m);

//#undef MULT_LEFT

    return (this.copyFrom(tmp));
}

	
	
	/**
	 * Multiplies given row vector by matrix, giving vector result. 
	 * Use this method to transform points from object coordinates to 
	 * world coordinates. 
	 * 
	 * @param src
	 * @param dst
	 */
	public void 	multVecMatrix (final SbVec3f src_, final SbVec3f dst)  {
		
	     float       x,y,z,w;
	     
	     float[] src = src_.getValueRead();
	          
	          x = src[0]*matrix[0][0] + src[1]*matrix[1][0] +
	              src[2]*matrix[2][0] + matrix[3][0];
	          y = src[0]*matrix[0][1] + src[1]*matrix[1][1] +
	              src[2]*matrix[2][1] + matrix[3][1];
	          z = src[0]*matrix[0][2] + src[1]*matrix[1][2] +
	              src[2]*matrix[2][2] + matrix[3][2];
	          w = src[0]*matrix[0][3] + src[1]*matrix[1][3] +
	              src[2]*matrix[2][3] + matrix[3][3];
	          
	          dst.setValue(x/w, y/w, z/w);
	     	}

    //
     // Multiplies given row vector by matrix, giving vector result
     // src is assumed to be a direction vector, so translation part of
     // matrix is ignored.
     //
     
    public void
     multDirMatrix(final SbVec3f srcV, final SbVec3f dst)
     {
         //float       x,y,z;
         float src0 = srcV.getX();
         float src1 = srcV.getY();
         float src2 = srcV.getZ();
     
         float x = src0*matrix[0][0] + src1*matrix[1][0] + src2*matrix[2][0];
         float y = src0*matrix[0][1] + src1*matrix[1][1] + src2*matrix[2][1];
         float z = src0*matrix[0][2] + src1*matrix[1][2] + src2*matrix[2][2];
     
         dst.setValue(x, y, z);
     }
         
    /**
     * Multiplies the given line's origin by the matrix, 
     * and the line's direction by the rotation portion of the 
     * matrix. 
     * 
     */
    //
     // Multiplies the given line by the matrix resulting in a new
     // line. The origin of the line is transformed by the whole matrix.
     // The translation part of the matrix is ignored when transforming
     // the direction of the line.
     //
     
    public void
     multLineMatrix(final SbLine src, final SbLine dst)
     {
             final SbVec3f pos = new SbVec3f(), dir = new SbVec3f();
             multVecMatrix(src.getPosition(), pos);
             multDirMatrix(src.getDirection(), dir);
             dst.setValue(pos, pos.operator_add(dir));
     }
     
	
	// java port
	public SbMatrix copyFrom(float[][] m) {
		matrix[0][0] = m[0][0];
		matrix[0][1] = m[0][1];
		matrix[0][2] = m[0][2];
		matrix[0][3] = m[0][3];
		matrix[1][0] = m[1][0];
		matrix[1][1] = m[1][1];
		matrix[1][2] = m[1][2];
		matrix[1][3] = m[1][3];
		matrix[2][0] = m[2][0];
		matrix[2][1] = m[2][1];
		matrix[2][2] = m[2][2];
		matrix[2][3] = m[2][3];
		matrix[3][0] = m[3][0];
		matrix[3][1] = m[3][1];
		matrix[3][2] = m[3][2];
		matrix[3][3] = m[3][3];
		return this;
	}

	@Override
	public void copyFrom(Object other) {
		SbMatrix sbMatrix = (SbMatrix)other;
		copyFrom(sbMatrix.matrix);
	}

    //! Set the matrix from an SbRotation.
    public  SbMatrix operator_assign( SbRotation q) { setRotate(q); return this; }
     
 //
 // Equality comparison operator. All componenents must match exactly.
 //
    
    public boolean             operator_equal_equal( final SbMatrix m2) {
    	final SbMatrix m1 = this;
        return (
                m1.matrix[0][0] == m2.matrix[0][0] &&
                m1.matrix[0][1] == m2.matrix[0][1] &&
                m1.matrix[0][2] == m2.matrix[0][2] &&
                m1.matrix[0][3] == m2.matrix[0][3] &&

                m1.matrix[1][0] == m2.matrix[1][0] &&
                m1.matrix[1][1] == m2.matrix[1][1] &&
                m1.matrix[1][2] == m2.matrix[1][2] &&
                m1.matrix[1][3] == m2.matrix[1][3] &&

                m1.matrix[2][0] == m2.matrix[2][0] &&
                m1.matrix[2][1] == m2.matrix[2][1] &&
                m1.matrix[2][2] == m2.matrix[2][2] &&
                m1.matrix[2][3] == m2.matrix[2][3] &&

                m1.matrix[3][0] == m2.matrix[3][0] &&
                m1.matrix[3][1] == m2.matrix[3][1] &&
                m1.matrix[3][2] == m2.matrix[3][2] &&
                m1.matrix[3][3] == m2.matrix[3][3]
                );
    }
    public boolean             operator_diff_equal( final SbMatrix m2)
        {
    	final SbMatrix m1 = this;
    	return !(m1.operator_equal_equal(m2)); }
    
    //
// Binary multiplication of matrices
//

public SbMatrix
operator_mul( final SbMatrix r)
{
	final SbMatrix l = this;
    final SbMatrix m = new SbMatrix(l);
    
    m.operator_mul_equal( r);
    
    return m;
}

    //! Performs right multiplication with another matrix.
    public SbMatrix   operator_mul_equal(final SbMatrix m)  { return multRight(m); }


//
// Returns transpose of matrix
//

public SbMatrix
transpose()
{
    return new SbMatrix(matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0],
                    matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1],
                    matrix[0][2], matrix[1][2], matrix[2][2], matrix[3][2],
                    matrix[0][3], matrix[1][3], matrix[2][3], matrix[3][3]);
}


//
// Composes the matrix from translation, rotation, scale, etc.
//

	private void TRANSLATE(SbVec3f vec, final SbMatrix m) {
		m.setTranslate(vec);
		multLeft(m);
		}
	private void ROTATE(SbRotation rot, final SbMatrix m) {
		rot.getValue(m);
		multLeft(m);
	}

public void setTransform(final SbVec3f translation,
                 final SbRotation rotation,
                 final SbVec3f scaleFactor,
                 final SbRotation scaleOrientation,
                 final SbVec3f center)
{
    final SbMatrix m = new SbMatrix();

    makeIdentity();
    
    if (translation.operator_not_equal(new SbVec3f(0,0,0)))
        TRANSLATE(translation,m);

    if (center.operator_not_equal(new SbVec3f(0,0,0)))
        TRANSLATE(center,m);

    if (rotation.operator_not_equal(new SbRotation(0,0,0,1)))
        ROTATE(rotation,m);

    if (scaleFactor.operator_not_equal(new SbVec3f(1,1,1))) {
        final SbRotation so = new SbRotation(scaleOrientation);
        if (so.operator_not_equal(new SbRotation(0,0,0,1)))
            ROTATE(so,m);
        
        m.setScale(scaleFactor);
        multLeft(m);

        if (so.operator_not_equal(new SbRotation(0,0,0,1))) {
            so.invert();
            ROTATE(so,m);
        }
    }

    if (center.operator_not_equal(new SbVec3f(0,0,0)))
        TRANSLATE(center.operator_minus(),m);

//#undef TRANSLATE
//#undef ROTATE
}

    //! Overloaded methods as a kludge because the compiler won't let
    //! us have SbVec3f(0,0,0) as a default value:
    public void        setTransform(final SbVec3f t, final SbRotation r,
                             final SbVec3f s)
                { setTransform(t, r, s,
                               new SbRotation(0,0,0,1), new SbVec3f(0,0,0)); }
    public void        setTransform(final SbVec3f t, final SbRotation r,
                             final SbVec3f s, final SbRotation so)
                { setTransform(t, r, s, so, new SbVec3f(0,0,0)); }


//
// Decomposes a rotation into translation etc, based on scale
//

public void getTransform(SbVec3f translation,
                    SbRotation rotation,
                    SbVec3f scaleFactor,
                    SbRotation scaleOrientation,
                    final SbVec3f center)
{
    final SbMatrix so = new SbMatrix(), rot = new SbMatrix(), proj = new SbMatrix();
    if (center.operator_not_equal(new SbVec3f(0,0,0))) {
        // to get fields for a non-0 center, we
        // need to decompose a new matrix "m" such
        // that [-center][m][center] = [this]
        // i.e., [m] = [center][this][-center]
        // (this trick stolen from Showcase code)
        final SbMatrix m = new SbMatrix(),c = new SbMatrix();
        m.setTranslate(center.operator_minus());
        m.multLeft(this);
        c.setTranslate(center);
        m.multLeft(c);
        m.factor(so,scaleFactor,rot,translation,proj);
    }
    else
        this.factor(so,scaleFactor,rot,translation,proj);

    scaleOrientation.copyFrom(so.transpose());  // have to transpose because factor 
                                        // gives us transpose of correct answer.
    rotation.copyFrom(rot);
}

public     void        getTransform(final SbVec3f t, final SbRotation r,
                          final SbVec3f s, final SbRotation so)
                { getTransform(t, r, s, so, new SbVec3f(0,0,0)); }



    //! Returns determinant of upper-left 3x3 submatrix
public float       det3() { return det3(0, 1, 2, 0, 1, 2); }



//
// Returns 4x4 array of elements
//

public void getValue(final float[][] m)
{
    m[0][0] = matrix[0][0];
    m[0][1] = matrix[0][1];
    m[0][2] = matrix[0][2];
    m[0][3] = matrix[0][3];
    m[1][0] = matrix[1][0];
    m[1][1] = matrix[1][1];
    m[1][2] = matrix[1][2];
    m[1][3] = matrix[1][3];
    m[2][0] = matrix[2][0];
    m[2][1] = matrix[2][1];
    m[2][2] = matrix[2][2];
    m[2][3] = matrix[2][3];
    m[3][0] = matrix[3][0];
    m[3][1] = matrix[3][1];
    m[3][2] = matrix[3][2];
    m[3][3] = matrix[3][3];
}


//
// Returns determinant of 3x3 submatrix composed of given row and
// column indices (0-3 for each).
//

public float
det3(int r1, int r2, int r3, int c1, int c2, int c3)
{
    return (   matrix[r1][c1] * matrix[r2][c2] * matrix[r3][c3]
            +  matrix[r1][c2] * matrix[r2][c3] * matrix[r3][c1]
            +  matrix[r1][c3] * matrix[r2][c1] * matrix[r3][c2]
            -  matrix[r1][c1] * matrix[r2][c3] * matrix[r3][c2]
            -  matrix[r1][c2] * matrix[r2][c1] * matrix[r3][c3]
            -  matrix[r1][c3] * matrix[r2][c2] * matrix[r3][c1]);
}



//
// Returns determinant of entire matrix
//

public float
det4()
{
    return (   matrix[0][3] * det3(1, 2, 3, 0, 1, 2)
            +  matrix[1][3] * det3(0, 2, 3, 0, 1, 2)
            +  matrix[2][3] * det3(0, 1, 3, 0, 1, 2)
            +  matrix[3][3] * det3(0, 1, 2, 0, 1, 2));
}


//
// Factors a matrix m into 5 pieces: m = r s r^ u t, where r^
// means transpose of r, and r and u are rotations, s is a scale,
// and t is a translation. Any projection information is returned
// in proj.
//
// routines for matrix factorization taken from BAGS code, written by
// John Hughes (?).  Original comment follows:
//
/* Copyright 1988, Brown Computer Graphics Group.  All Rights Reserved. */
/* --------------------------------------------------------------------------
 * This file contains routines to do the MAT3factor operation, which
 * factors a matrix m:
 *    m = r s r^ u t, where r^ means transpose of r, and r and u are
 * rotations, s is a scale, and t is a translation.
 *
 * It is based on the Jacobi method for diagonalizing real symmetric
 * matrices, taken from Linear Algebra, Wilkenson and Reinsch, Springer-Verlag
 * math series, Volume II, 1971, page 204.  Call number QA251W623.
 * In ALGOL!
 * -------------------------------------------------------------------------*/
/*
 * Variable declarations from the original source:
 *
 * n    : order of matrix A
 * eivec: true if eigenvectors are desired, false otherwise.
 * a    : Array [1:n, 1:n] of numbers, assumed symmetric!
 *
 * a    : Superdiagonal elements of the original array a are destroyed.
 *        Diagonal and subdiagonal elements are untouched.
 * d    : Array [1:n] of eigenvalues of a.
 * v    : Array [1:n, 1:n] containing (if eivec = TRUE), the eigenvectors of
 *        a, with the kth column being the normalized eigenvector with
 *        eigenvalue d[k].
 * rot  : The number of jacobi rotations required to perform the operation.
 */
               
public boolean
factor(final SbMatrix r, final SbVec3f s, final SbMatrix u, final SbVec3f t,
                 final SbMatrix proj)
{
    double      det;            /* Determinant of matrix A      */
    double      det_sign;       /* -1 if det < 0, 1 if det > 0  */
    double      scratch;
    int         i, j;
    final int[]         junk = new int[1];
    final SbMatrix    a = new SbMatrix(), b = new SbMatrix(), si = new SbMatrix();
    float[]       evalues = new float[3];
    final SbVec3f[]     evectors = new SbVec3f[3];
    evectors[0] = new SbVec3f();
    evectors[1] = new SbVec3f();
    evectors[2] = new SbVec3f();
    
    a.copyFrom(this);
    proj.makeIdentity();
    scratch = 1.0;
    
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            a.matrix[i][j] *= scratch;
        }
        t.setValue(i, (float)(matrix[3][i] * scratch));
        a.matrix[3][i] = a.matrix[i][3] = 0.0f;
    }
    a.matrix[3][3] = 1.0f;
    
    /* (3) Compute det A. If negative, set sign = -1, else sign = 1 */
    det = a.det3();
    det_sign = (det < 0.0 ? -1.0 : 1.0);
    if (det_sign * det < 1e-12)
        return(false);          // singular
    
    /* (4) B = A * A^  (here A^ means A transpose) */
    b.copyFrom( a.operator_mul( a.transpose()));
    
    b.jacobi3(evalues, evectors, junk);

    // find min / max eigenvalues and do ratio test to determine singularity
    
    r.copyFrom( new SbMatrix(evectors[0].getValueRead()[0], evectors[0].getValueRead()[1], evectors[0].getValueRead()[2], 0.0f, 
                 evectors[1].getValueRead()[0], evectors[1].getValueRead()[1], evectors[1].getValueRead()[2], 0.0f, 
                 evectors[2].getValueRead()[0], evectors[2].getValueRead()[1], evectors[2].getValueRead()[2], 0.0f, 
                 0.0f, 0.0f, 0.0f, 1.0f));
    
    /* Compute s = sqrt(evalues), with sign. Set si = s-inverse */
    si.makeIdentity();
    for (i = 0; i < 3; i++) {
        s.setValue(i, (float)(det_sign * Math.sqrt(evalues[i])));
        si.matrix[i][i] = 1.0f / s.getValueRead()[i];
    }
    
    /* (5) Compute U = R^ S! R A. */
    u.copyFrom( r.operator_mul(si).operator_mul(r.transpose()).operator_mul( a));
    
    return(true);
}


//
// Diagonalizes 3x3 matrix. See comment for factor().
//

private static final int SB_JACOBI_RANK = 3;

private static double ABS(double val) {
	return Math.abs(val);
}

public void jacobi3(final float[] evalues,
                  final SbVec3f[] evectors, final int[] rots)
{
    double      sm;             // smallest entry
    double      theta;          // angle for Jacobi rotation
    double      c, s, t;        // cosine, sine, tangent of theta
    double      tau;            // sine / (1 + cos)
    double      h, g;           // two scrap values
    double      thresh;         // threshold below which no rotation done
    double[]      b = new double[SB_JACOBI_RANK]; // more scratch
    double[]      z = new double[SB_JACOBI_RANK]; // more scratch
    int         p, q, i, j;
    double[][]      a = new double[SB_JACOBI_RANK][SB_JACOBI_RANK];
    
    // initializations
    for (i = 0; i < SB_JACOBI_RANK; i++) {
        b[i] = evalues[i] = matrix[i][i];
        z[i] = 0.0;
        for (j = 0; j < SB_JACOBI_RANK; j++) {
            evectors[i].setValue(j, (i == j) ? 1.0f : 0.0f);
            a[i][j] = matrix[i][j];
        }
    }
    
    rots[0] = 0;

    // Why 50? I don't know--it's the way the folks who wrote the
    // algorithm did it:
    for (i = 0; i < 50; i++) {
        sm = 0.0;
        for (p = 0; p < SB_JACOBI_RANK - 1; p++)
            for (q = p+1; q < SB_JACOBI_RANK; q++)
                sm += ABS(a[p][q]);
        
        if (sm == 0.0)
            return;
        
        thresh = (i < 3 ?
                  (.2 * sm / (SB_JACOBI_RANK * SB_JACOBI_RANK)) :
                  0.0);
        
        for (p = 0; p < SB_JACOBI_RANK - 1; p++) {
            for (q = p+1; q < SB_JACOBI_RANK; q++) {
                
                g = 100.0 * ABS(a[p][q]);
                
                if (i > 3 && (ABS(evalues[p]) + g == ABS(evalues[p])) &&
                    (ABS(evalues[q]) + g == ABS(evalues[q])))
                    a[p][q] = 0.0;
                
                else if (ABS(a[p][q]) > thresh) {
                    h = evalues[q] - evalues[p];
                    
                    if (ABS(h) + g == ABS(h))
                        t = a[p][q] / h;
                    else {
                        theta = .5 * h / a[p][q];
                        t = 1.0 / (ABS(theta) + Math.sqrt(1 + theta * theta));
                        if (theta < 0.0)  t = -t;
                    }
                    // End of computing tangent of rotation angle
                    
                    c = 1.0 / Math.sqrt(1.0 + t*t);
                    s = t * c;
                    tau = s / (1.0 + c);
                    h = t * a[p][q];
                    z[p]    -= h;
                    z[q]    += h;
                    evalues[p] -= h;
                    evalues[q] += h;
                    a[p][q] = 0.0;
                    
                    for (j = 0; j < p; j++) {
                        g = a[j][p];
                        h = a[j][q];
                        a[j][p] = g - s * (h + g * tau);
                        a[j][q] = h + s * (g - h * tau);
                    }
                    
                    for (j = p+1; j < q; j++) {
                        g = a[p][j];
                        h = a[j][q];
                        a[p][j] = g - s * (h + g * tau);
                        a[j][q] = h + s * (g - h * tau);
                    }
                    
                    for (j = q+1; j < SB_JACOBI_RANK; j++) {
                        g = a[p][j];
                        h = a[q][j];
                        a[p][j] = g - s * (h + g * tau);
                        a[q][j] = h + s * (g - h * tau);
                    }
                    
                    for (j = 0; j < SB_JACOBI_RANK; j++) {
                        g = evectors[j].getValueRead()[p];
                        h = evectors[j].getValueRead()[q];
                        evectors[j].setValue(p, (float)(g - s * (h + g * tau)));
                        evectors[j].setValue(q, (float)(h + s * (g - h * tau)));
                    }
                }
                rots[0]++;
            }
        }
        for (p = 0; p < SB_JACOBI_RANK; p++) {
        	b[p] += z[p];
            evalues[p] = (float)b[p];// += z[p];
            z[p] = 0;
        }
    }
}
//#undef SB_JACOBI_RANK

	/**
	 * Java port
	 * @return
	 */
    public DoubleConsumer[][] getRef() {
    	DoubleConsumer[][] ref = new DoubleConsumer[4][4];
		ref[0][0] = value -> matrix[0][0] = (float)value;
		ref[0][1] = value -> matrix[0][1] = (float)value;
		ref[0][2] = value -> matrix[0][2] = (float)value;
		ref[0][3] = value -> matrix[0][3] = (float)value;
		ref[1][0] = value -> matrix[1][0] = (float)value;
		ref[1][1] = value -> matrix[1][1] = (float)value;
		ref[1][2] = value -> matrix[1][2] = (float)value;
		ref[1][3] = value -> matrix[1][3] = (float)value;
		ref[2][0] = value -> matrix[2][0] = (float)value;
		ref[2][1] = value -> matrix[2][1] = (float)value;
		ref[2][2] = value -> matrix[2][2] = (float)value;
		ref[2][3] = value -> matrix[2][3] = (float)value;
		ref[3][0] = value -> matrix[3][0] = (float)value;
		ref[3][1] = value -> matrix[3][1] = (float)value;
		ref[3][2] = value -> matrix[3][2] = (float)value;
		ref[3][3] = value -> matrix[3][3] = (float)value;
    	return ref;
    }

    /**
     * Java port
     * @param i
     * @return
     */
	public float getValueAt(int index) {
		int i = index / 4;
		int j = index % 4;
		
		return matrix[i][j];
	}

	/**
	 * Java port
	 * @return
	 */
	public float[] getValueLinear() {
		float[] valueLinear = new float[16];
		for(int i=0; i< 16;i++) {
			valueLinear[i] = getValueAt(i);
		}
		return valueLinear;
	}

	public boolean operator_not_equal(SbMatrix value) {
		return !operator_equal(value);
	}

	private boolean operator_equal(SbMatrix value) {
		
		final SbMatrix m1 = this;
		final SbMatrix m2 = value;
		  for (int i=0; i < 4; i++) {
			    for (int j=0; j < 4; j++) {
			      if (m1.matrix[i][j] != m2.matrix[i][j]) 
			    	  return false;
			    }
			  }

			  return true;
	}
}
