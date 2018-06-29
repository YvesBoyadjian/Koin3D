/**
 * 
 */
package jscenegraph.database.inventor;

import java.util.function.DoubleConsumer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3d {

	  protected final double[]       vec = new double[3];         //!< Storage for vector components
	
	  public SbVec3d() {		  
	  }
	  
	  public SbVec3d(double x, double y, double z) { vec[0] = x; vec[1] = y; vec[2] = z; }
	  
	public SbVec3d(final SbVec3f v) {
		  setValue(v); 
	}
	
	public SbVec3d(final SbVec3d v) {
		vec[0] = v.vec[0];
		vec[1] = v.vec[1];
		vec[2] = v.vec[2];
	}
	
	/*!
	  Sets this vector to the single precision vector \a v, converting
	  the vector to a double precision vector.
	*/
	public SbVec3d 
	setValue(final SbVec3f v)
	{
	  vec[0] = (v.getValue()[0]);
	  vec[1] = (v.getValue()[1]);
	  vec[2] = (v.getValue()[2]);
	  return this;
	}

	
	  
//
// Changes vector to be unit length
//

public double normalize()
{
    double len = length();

    if (len != 0.0)
        (this).operator_mul_equal(1.0 / len);

    else setValue(0.0, 0.0, 0.0);

    return len;
}

    //! Accesses indexed component of vector
    public double operator_square_bracket(int i)            { return (vec[i]); }


    //! Sets the vector components.
    public SbVec3d setValue(double x, double y, double z)
         { vec[0] = x; vec[1] = y; vec[2] = z; return this; }


//
// Component-wise scalar multiplication operator
//

public SbVec3d operator_mul_equal(double d)
{
    vec[0] *= d;
    vec[1] *= d;
    vec[2] *= d;

    return this;
}

public SbVec3d operator_minus (final SbVec3d v2) {
	final SbVec3d v1 = this;
  SbVec3d v = new SbVec3d(v1); v.operator_minus_equal(v2); return v;
}

public SbVec3d operator_minus_equal(final SbVec3d v) { vec[0] -= v.vec[0]; vec[1] -= v.vec[1]; vec[2] -= v.vec[2]; return this; }

public SbVec3d operator_add(final SbVec3d v2) {
	final SbVec3d v1 = this;
	  final SbVec3d v = new SbVec3d(v1); v.operator_add_equal(v2); return v;
	}

public SbVec3d operator_add_equal (final SbVec3d v) { vec[0] += v.vec[0]; vec[1] += v.vec[1]; vec[2] += v.vec[2]; return this; }

public SbVec3d operator_mul (double d) {
	final SbVec3d v = this;
	  final SbVec3d val = new SbVec3d(v); val.operator_mul_equal( d); return val;
	}

public SbVec3d operator_div(double d) {
	final SbVec3d v = this;
	  //SbDividerChk("operator/(SbVec3d,double)", d);
	  final SbVec3d val = new SbVec3d(v); val.operator_div_equal( d); return val;
	}

public SbVec3d operator_div_equal (double d) {
	//SbDividerChk("SbVec3d::operator/=(double)", d); 
	return operator_mul_equal (1.0 / d); 
	}

public boolean operator_equal_equal (final SbVec3d  v2) {
	final SbVec3d v1 = this;
	  return ((v1.vec[0] == v2.vec[0]) && (v1.vec[1] == v2.vec[1]) && (v1.vec[2] == v2.vec[2]));
	}



public double dot(final SbVec3d v) { return vec[0] * v.vec[0] + vec[1] * v.vec[1] + vec[2] * v.vec[2]; }


//
// Returns geometric length of vector
//

public double length()
{
    return Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
}

public void copyFrom(SbVec3d other) {
	vec[0] = other.vec[0];
	vec[1] = other.vec[1];
	vec[2] = other.vec[2];
}


/**
 * java port
 * @return
 */
	public DoubleConsumer[] getRef() {
		DoubleConsumer[] ref = new DoubleConsumer[3];
		ref[0] = value -> vec[0] = value;
		ref[1] = value -> vec[1] = value;
		ref[2] = value -> vec[2] = value;
		return ref;
	}

// Returns vector components. 
public final double[] getValue() { return vec; }

/**
 * Java port
 * @return
 */
public double getX() {
	return vec[0];
}

/**
 * Java port
 * @return
 */
public double getY() {
	return vec[1];
}

/**
 * java port
 * @return
 */
public double getZ() {
	return vec[2];
}

/**
 * Java port
 * @param x
 */
public void setX(float x) {
	vec[0] = x;
}

/**
 * java port
 * @param y
 */
public void setY(float y) {
	vec[1] = y;
}

/**
 * Java port
 * @param z
 */
public void setZ(float z) {
	vec[2] = z;
}

}
