/**
 * 
 */
package jscenegraph.database.inventor;

import java.util.function.IntConsumer;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3s implements Mutable {
	protected final short[] vec = new short[3]; 
	
    //! Default constructor
    public SbVec3s()                                           { }

    //! Constructor given an array of 3 components
    public SbVec3s( short v[])                           { setValue(v); }

    //! Constructor given 3 individual components
    public SbVec3s(short x, short y, short z)                  { setValue(x, y, z); }

//
// Sets value of vector from array of 3 components
//

public SbVec3s(SbVec3s other) {
		vec[0] = other.vec[0];
		vec[1] = other.vec[1];
		vec[2] = other.vec[2];
	}

public SbVec3s 
setValue(final short v[])     
{
    vec[0] = v[0];
    vec[1] = v[1];
    vec[2] = v[2];

    return (this);
}

//
// Sets value of vector from 3 individual components
//

public SbVec3s 
setValue(short x, short y, short z)    
{
    vec[0] = x;
    vec[1] = y;
    vec[2] = z;

    return (this);
}


	/* (non-Javadoc)
	 * @see jscenegraph.port.Mutable#copyFrom(java.lang.Object)
	 */
	@Override
	public void copyFrom(Object other) {
		operator_assign((SbVec3s)other);
	}

	// java port
	public void operator_assign(SbVec3s other) {
		vec[0] = other.vec[0];
		vec[1] = other.vec[1];
		vec[2] = other.vec[2];
	}

	public IntConsumer[] getRef() {
		IntConsumer[] ret = new IntConsumer[3];
		ret[0] = value -> vec[0] = (short) value;
		ret[1] = value -> vec[1] = (short) value;
		ret[2] = value -> vec[2] = (short) value;
		return ret;
	}

	public short[] getValue() {
		return vec;
	}

	public boolean operator_not_equal(SbVec3s other) {
		return vec[0] != other.vec[0] ||
				vec[1] != other.vec[1] ||
				vec[2] != other.vec[2];
	}
	
	public boolean operator_equal_equal(final SbVec3s v2) {
		final SbVec3s v1 = this;
		  return ((v1.vec[0] == v2.vec[0]) && (v1.vec[1] == v2.vec[1]) && (v1.vec[2] == v2.vec[2]));
		}

}
