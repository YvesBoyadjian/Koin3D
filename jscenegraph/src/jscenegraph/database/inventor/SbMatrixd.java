/**
 * 
 */
package jscenegraph.database.inventor;

import java.util.function.DoubleConsumer;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbMatrixd implements Mutable {
	
	  private  final double[][]      matrix = new double[4][4];         //!< Storage for 4x4 matrix

	    //! Default constructor
	  public SbMatrixd() { }


    //
    // Constructor from a 4x4 array of elements
    //

    public SbMatrixd(final double[][] m)
    {
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

    // java port
    public void copyFrom(final double[][] m) {
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

    /* (non-Javadoc)
     * @see jscenegraph.port.Mutable#copyFrom(java.lang.Object)
     */
    @Override
    public void copyFrom(Object other) {
        SbMatrixd sbMatrix = (SbMatrixd) other;
        copyFrom(sbMatrix.matrix);
    }

    //! Sets matrix to rotate by given rotation.
    public void        setRotate(final SbRotationd rotation) {
        rotation.getValue(this);    	
    }

	// java port
	public final double[][] getValue() {
		return matrix;
	}
	

//
// Returns 4x4 array of elements
//

public void getValue(double[][] m)
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

/**
 * Java port
 * @return
 */
public DoubleConsumer[][] getRef() {
	DoubleConsumer[][] ref = new DoubleConsumer[4][4];
	ref[0][0] = value -> matrix[0][0] = value;
	ref[0][1] = value -> matrix[0][1] = value;
	ref[0][2] = value -> matrix[0][2] = value;
	ref[0][3] = value -> matrix[0][3] = value;
	ref[1][0] = value -> matrix[1][0] = value;
	ref[1][1] = value -> matrix[1][1] = value;
	ref[1][2] = value -> matrix[1][2] = value;
	ref[1][3] = value -> matrix[1][3] = value;
	ref[2][0] = value -> matrix[2][0] = value;
	ref[2][1] = value -> matrix[2][1] = value;
	ref[2][2] = value -> matrix[2][2] = value;
	ref[2][3] = value -> matrix[2][3] = value;
	ref[3][0] = value -> matrix[3][0] = value;
	ref[3][1] = value -> matrix[3][1] = value;
	ref[3][2] = value -> matrix[3][2] = value;
	ref[3][3] = value -> matrix[3][3] = value;
	return ref;
}

/**
 * java port
 * @param proj
 */
public void setValue(SbMatrix other) {
	final float[][] m = other.getValue();
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

private double MULT_LEFT(double[][] m, double[][] matrix, int i,int j)  {
	
			return 
		m[i][0]*matrix[0][j] + 
        m[i][1]*matrix[1][j] + 
        m[i][2]*matrix[2][j] + 
        m[i][3]*matrix[3][j];
}

//
// Multiplies matrix by given matrix on left
//

public SbMatrixd multLeft(final SbMatrixd m)
{
    // Trivial cases
    if (IS_IDENTITY(m.getValue()))
        return this;
    else if (IS_IDENTITY(matrix)) {
        this.copyFrom(m);
        return this;
    }
        
    final double[][]      tmp = new double[4][4];
    double[][] md = m.getValue();

    tmp[0][0] = MULT_LEFT(md,matrix,0,0);
    tmp[0][1] = MULT_LEFT(md,matrix,0,1);
    tmp[0][2] = MULT_LEFT(md,matrix,0,2);
    tmp[0][3] = MULT_LEFT(md,matrix,0,3);
    tmp[1][0] = MULT_LEFT(md,matrix,1,0);
    tmp[1][1] = MULT_LEFT(md,matrix,1,1);
    tmp[1][2] = MULT_LEFT(md,matrix,1,2);
    tmp[1][3] = MULT_LEFT(md,matrix,1,3);
    tmp[2][0] = MULT_LEFT(md,matrix,2,0);
    tmp[2][1] = MULT_LEFT(md,matrix,2,1);
    tmp[2][2] = MULT_LEFT(md,matrix,2,2);
    tmp[2][3] = MULT_LEFT(md,matrix,2,3);
    tmp[3][0] = MULT_LEFT(md,matrix,3,0);
    tmp[3][1] = MULT_LEFT(md,matrix,3,1);
    tmp[3][2] = MULT_LEFT(md,matrix,3,2);
    tmp[3][3] = MULT_LEFT(md,matrix,3,3);

    this.copyFrom(tmp);
    return this;
}

//
//Macro for checking is a matrix is idenity.
//

private static boolean IS_IDENTITY(double[][] matrix){ return (
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


public double[] getValueLinear() {
	double[] valueLinear = new double[16];
	valueLinear[0] = matrix[0][0];
	valueLinear[1] = matrix[0][1];
	valueLinear[2] = matrix[0][2];
	valueLinear[3] = matrix[0][3];
	valueLinear[4] = matrix[1][0];
	valueLinear[5] = matrix[1][1];
	valueLinear[6] = matrix[1][2];
	valueLinear[7] = matrix[1][3];
	valueLinear[8] = matrix[2][0];
	valueLinear[9] = matrix[2][1];
	valueLinear[10] = matrix[2][2];
	valueLinear[11] = matrix[2][3];
	valueLinear[12] = matrix[3][0];
	valueLinear[13] = matrix[3][1];
	valueLinear[14] = matrix[3][2];
	valueLinear[15] = matrix[3][3];
	return valueLinear;
}

	
}
