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

import jscenegraph.port.Mutable;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

////////////////////////////////////////////////////////////////////////////////
//! 4D vector class.
/*!
\class SbVec4f
\ingroup Basics
4D vector class used to store homogeneous coordinates. This class is used
in Inventor for arguments and return values.
{}

\par See Also
\par
SbVec2f, SbVec3f, SbVec2s, SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec4f implements Mutable {
	
	   protected FloatMemoryBuffer vec4;         //!< Storage for vector components
	   protected int indice;
		   	
		public static int sizeof() {
			return 4*4;
		}
		
    //! Default constructor.
		public SbVec4f()                                           {
			vec4 = FloatMemoryBuffer.allocateFloats(4);
			indice = 0;			
		}

		/**
		 * Internal contructor
		 * @param array
		 * @param indice
		 */
		public SbVec4f(FloatMemoryBuffer array, int indice) {
			vec4 = array;
			this.indice = indice;
		}
		
    //! Constructor given vector components.
    public SbVec4f(	final float[] v)                           {
		vec4 = FloatMemoryBuffer.allocateFloats(4);
		indice = 0;			
    	setValue(v); 
    	}

    //! Constructor given vector components.
    public SbVec4f(float x, float y, float z, float w)       {
		vec4 = FloatMemoryBuffer.allocateFloats(4);
		indice = 0;			
    	setValue(x, y, z, w); 
    	}

		
		

    public SbVec4f(SbVec4f value) {
		vec4 = FloatMemoryBuffer.allocateFloats(4);
		indice = 0;			
		copyFrom(value);
	}

	/**
	 * Internal method
	 * @param i
	 * @return
	 */
	protected float g(int i) {
		return vec4.getFloat(indice+i);
	}
	
	/**
	 * Internal method
	 * @param i
	 * @param v
	 */
	protected void s(int i, float v) {
		vec4.setFloat(indice+i, v);
	}
	
	//! Accesses indexed component of vector
	public float operator_square_bracket(int i)            { 
		return g(i);
	}

	public void operator_square_bracket(int i, float value) {
		s(i, value);
	}

//
// Component-wise binary scalar multiplication operator
//

	public SbVec4f
	operator_mul( float d)
{
	 SbVec4f v = this;
    return new SbVec4f(v.g(0) * d, v.g(1) * d, v.g(2) * d, v.g(3) * d);
}

//
// Component-wise binary vector addition operator
//

public SbVec4f
operator_add(SbVec4f v2)
{
	SbVec4f v1 = this;
    return new SbVec4f(v1.g(0) + v2.g(0),
                   v1.g(1) + v2.g(1),
                   v1.g(2) + v2.g(2),
                   v1.g(3) + v2.g(3));
}

	

	public void copyFrom(SbVec4f t) {
		s(0, t.g(0));
		s(1, t.g(1));
		s(2, t.g(2));
		s(3, t.g(3));
	}

	// Returns vector components for reading only. 
    public final float[] getValueRead() {
    	float[] vecRead = new float[4];
    	vecRead[0] = g(0); 
    	vecRead[1] = g(1); 
    	vecRead[2] = g(2); 
    	vecRead[3] = g(3); 
    	return vecRead; 
    }
    

	protected final float[] getValueRef() {
    	if( indice != 0) {
    		throw new IllegalStateException();
    	}
		return vec4.toFloatArray();
	}
	
	
//
// Sets value of vector from array of 4 components
//

public SbVec4f 
setValue(final float[] v)     
{
    s(0, v[0]);
    s(1, v[1]);
    s(2, v[2]);
    s(3, v[3]);

    return (this);
}

//
// Sets value of vector from 4 individual components
//

public SbVec4f 
setValue(float x, float y, float z, float w)   
{
    s(0, x);
    s(1, y);
    s(2, z);
    s(3, w);

    return (this);
}


//
// Component-wise binary vector subtraction operator
//

public SbVec4f
operator_minus(final SbVec4f v2)
{
	final SbVec4f v1 = this;
    return new SbVec4f(v1.g(0) - v2.g(0),
                   v1.g(1) - v2.g(1),
                   v1.g(2) - v2.g(2),
                   v1.g(3) - v2.g(3));
}


//
// Equality comparison operator within a tolerance.
//

public boolean
equals(final SbVec4f v, float tolerance) 
{
    final SbVec4f     diff = this.operator_minus(v);

    return diff.dot(diff) <= tolerance;
}
	

//
// Returns dot (inner) product of vector and another vector
//

public float
dot(final SbVec4f v)
{
    return g(0) * v.g(0) + g(1) * v.g(1) + 
           g(2) * v.g(2) + g(3) * v.g(3) ;
}

@Override
public void copyFrom(Object other) {
	copyFrom((SbVec4f)other);
}

/**
 * java port
 * @return
 */
	public DoubleConsumer[] getRef() {
		DoubleConsumer[] ref = new DoubleConsumer[4];
		ref[0] = value -> s(0, (float)value);
		ref[1] = value -> s(1, (float)value);
		ref[2] = value -> s(2, (float)value);
		ref[3] = value -> s(3, (float)value);
		return ref;
	}

	/*!
	  Returns the vector as a Cartesian 3D vector in \a v. This means that
	  the 3 first components x, y and z will be divided by the fourth, w.
	*/
public void getReal(SbVec3f v) {
	  v.setValue(g(0)/g(3), g(1)/g(3), g(2)/g(3));
}

public void setValue(int i, float f) {
	s(i,f);
}

public float getX() {
	return g(0);
}

public float getY() {
	return g(1);
}

public float getZ() {
	return g(2);
}

public float getW() {
	return g(3);
}


 }
