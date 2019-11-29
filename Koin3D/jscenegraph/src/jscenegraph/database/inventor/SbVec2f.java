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

import java.util.function.DoubleConsumer;

import jscenegraph.port.FloatArray;
import jscenegraph.port.Mutable;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;


////////////////////////////////////////////////////////////////////////////////
//! 2D vector class.
/*!
\class SbVec2f
\ingroup Basics
2D vector class used to store 2D vectors and points. This class is used
throughout Inventor for arguments and return values.
{}

\par See Also
\par
SbVec3f, SbVec4f, SbVec2s, SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2f implements Mutable {
	protected final FloatMemoryBuffer vec;
	protected int indice;

	// Default constructor.
	public SbVec2f() {
		vec = FloatMemoryBuffer.allocateFloats(2);
		indice = 0;
	}

	// Constructor given vector components.
	public SbVec2f(float[] v) {
		vec = FloatMemoryBuffer.allocateFloats(2);
		indice = 0;
		 setValue(v);
	}

	// Constructor given vector components.
	public SbVec2f(float x, float y) {
		vec = FloatMemoryBuffer.allocateFloats(2); 		
		indice = 0;
		 setValue(x, y);
	}

	// java port
	public SbVec2f(final SbVec2f other) {
		vec = FloatMemoryBuffer.allocateFloats(2);
		indice = 0;
		setValue(other.vec);
	}
	
	/**
	 * Internal contructor
	 * @param array
	 * @param indice
	 */
	public SbVec2f(FloatMemoryBuffer array, int indice) {
		vec = array;
		this.indice = indice;
	}
	
	public SbVec2f(FloatArray vpCoords, int i) {
		vec = vpCoords.getValues();
		indice = vpCoords.getStart()+2*i;
	}

	// Sets the vector components.
	public SbVec2f setValue(float[] v) {
	     s(0, v[0]);
	     s(1, v[1]);

	     return (this);
	}

	// Sets the vector components.
	public SbVec2f setValue(FloatMemoryBuffer v) {
	     s(0, v.getFloat(0));
	     s(1, v.getFloat(1));

	     return (this);
	}

	// Sets the vector components.
	public SbVec2f setValue(float x, float y) {

		  s(0, x);
		    s(1, y);

		    return this;
	}

    //! Returns vector components.
    public float[] getValueRead() {
    	float[] vecRead = new float[2];
    	vecRead[0] = g(0); 
    	vecRead[1] = g(1); 
    	return vecRead; 
    }

	/**
	 * Internal method
	 * @param i
	 * @return
	 */
	protected float g(int i) {
		return vec.getFloat(indice+i);
	}
	
	/**
	 * Internal method
	 * @param i
	 * @param v
	 */
	protected void s(int i, float v) {
		vec.setFloat(indice+i, v);
	}
	    
    /**
     * for internal use
     * @return
     */
    protected final float[] getValueRef() {
    	if( indice != 0) {
    		throw new IllegalStateException();
    	}
    	return vec.toFloatArray(); 
    }
    
	public float operator_square_bracket(int i) {
		return g(i);
	}

	public void operator_square_bracket(int i, float value) {
		s(i, value);
	}


	//
	// Nondestructive unary negation - returns a new vector
	//

	public SbVec2f operator_minus()
	{
	    return new SbVec2f(-g(0), -g(1));
	}

//
// Component-wise binary vector subtraction operator
//

public SbVec2f
operator_minus(final SbVec2f v2)
{
	final SbVec2f v1 = this;
    return new SbVec2f(v1.g(0) - v2.g(0),
                  v1.g(1) - v2.g(1));
}


    //! Component-wise scalar multiplication and division operators.
    public SbVec2f    operator_div_equal(float d)
        { return operator_mul_equal (1.0f / d); }

 //
   // Equality comparison operator. Componenents must match exactly.
   //

  public boolean
   operator_equal_equal(final SbVec2f v2)
   {
	  final SbVec2f v1 = this;
       return (v1.g(0) == v2.g(0) &&
               v1.g(1) == v2.g(1));
   }


	public boolean operator_not_equal(SbVec2f range) {
		return !operator_equal_equal(range);
	}

//
// Component-wise vector addition operator
//

public SbVec2f operator_add_equal(final SbVec2f u)
{
    s(0, g(0)+ u.g(0));
    s(1, g(1)+ u.g(1));

    return this;
}


//
// Returns dot (inner) product of vector and another vector
//

public float dot(final SbVec2f v)
{
    return g(0) * v.g(0) + g(1) * v.g(1);
}


//
// Component-wise scalar multiplication operator
//

    public SbVec2f
    operator_mul_equal(float d)
{
    s(0, g(0)* d);
    s(1, g(1)* d);

    return this;
}


//
// Component-wise binary scalar multiplication operator
//

public SbVec2f
operator_mul(float d)
{
	final SbVec2f v = this;
    return new SbVec2f(v.g(0) * d, v.g(1) * d);
}

//
// Component-wise binary vector addition operator
//

public SbVec2f
operator_add( final SbVec2f v2)
{
	final SbVec2f v1 = this;
    return new SbVec2f(v1.g(0) + v2.g(0),
                  v1.g(1) + v2.g(1));
}


//
// Changes vector to be unit length
//

public float normalize()
{
    float len = length();

    if (len != 0.0) {
		(this).operator_mul_equal(1.0f / len);
	} else {
		setValue(0.0f, 0.0f);
	}

    return len;
}


//
// Returns geometric length of vector
//

public float length()
{
    return (float)Math.sqrt(g(0) * g(0) + g(1) * g(1));
}



	@Override
	public void copyFrom(Object other) {
		SbVec2f otherVec2f = (SbVec2f)other;
		s(0, otherVec2f.g(0));
		s(1, otherVec2f.g(1));
	}

	// java port
	public static int sizeof() {
		return 2 * Float.SIZE / Byte.SIZE;
	}

	/**
	 * Allocates an array of SbVec2f
	 * @param num
	 * @return
	 */
	public static SbVec2f[] allocate(int num) {
		SbVec2f[] retVal = new SbVec2f[num];
		for(int i=0; i< num;i++) {
			retVal[i] = new SbVec2f();
		}
		return retVal;
	}

	/**
	 * java port
	 * @return
	 */
	public DoubleConsumer[] getRef() {
		DoubleConsumer[] ref = new DoubleConsumer[2];
		ref[0] = value -> s(0, (float)value);
		ref[1] = value -> s(1, (float)value);
		return ref;
	}

	public float getX() { // java port
		return g(0);
	}
	
	public float getY() { // java port
		return g(1);
	}

	/**
	 * java port
	 * @param x
	 */
	public void setX(float x) {
		s(0, x);
	}

	/**
	 * java port
	 * @param y
	 */
	public void setY(float y) {
		s(1, y);
	}
 }

