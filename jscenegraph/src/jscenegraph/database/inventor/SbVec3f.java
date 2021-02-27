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

import java.nio.FloatBuffer;
import java.util.function.DoubleConsumer;

import jscenegraph.port.FloatArray;
import jscenegraph.port.KDebug;
import jscenegraph.port.Mutable;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;
import jscenegraph.port.memorybuffer.MemoryBuffer;

////////////////////////////////////////////////////////////////////////////////
//! 3D vector class.
/*!
\class SbVec3f
\ingroup Basics
3D vector class used to store 3D vectors and points. This class is used
throughout Inventor for arguments and return values.
{}

\par See Also
\par
SbVec2f, SbVec4f, SbVec2s, SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3f implements Cloneable, Mutable {
	
	//protected float[] vec;
	protected FloatMemoryBuffer vec;
	protected int indice;
	
	// Default constructor. 
	public SbVec3f() {
		
//		KDebug.count("SbVec3f");
		
		vec = FloatMemoryBuffer.allocateFloats(3);
		indice = 0;
	}

	/**
	 * Internal contructor
	 * @param array
	 * @param indice
	 */
	public SbVec3f(FloatMemoryBuffer array, int indice) {
		
//		KDebug.count("SbVec3f");
		
		vec = array;
		this.indice = indice;
	}
	
	// java port
	public SbVec3f(SbVec3f other) {
		
//		KDebug.count("SbVec3f");
		
		vec = FloatMemoryBuffer.allocateFloats(3);
		indice = 0;
		vec.setFloat(0, other.g(0));
		vec.setFloat(1, other.g(1));
		vec.setFloat(2, other.g(2));
	}
	
	// Constructor given vector components. 
	public SbVec3f(float[] v) {
		
//		KDebug.count("SbVec3f");
		
		vec = FloatMemoryBuffer.allocateFloats(3);
		indice = 0;
		 vec.setFloat(0, v[0]); vec.setFloat(1, v[1]); vec.setFloat(2, v[2]); 
	}

	/**
	 * Constructor given vector components.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public SbVec3f(float x, float y, float z)
	{ 
		
//		KDebug.count("SbVec3f");
		
		vec = FloatMemoryBuffer.allocateFloats(3); 		
		indice = 0;
		vec.setFloat(0, x); vec.setFloat(1, y); vec.setFloat(2, z); 
	}
	
	public SbVec3f(FloatArray vpCoords, int i) {
		
//		KDebug.count("SbVec3f");
		
		vec = vpCoords.getValues();
		indice = vpCoords.getStart()+3*i;
	}
	
	// java port
	public void constructor() {
		s(0,0);
		s(1,0);
		s(2,0);
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public static int sizeof() {
		return 4*3;
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
	
	// Returns right-handed cross product of vector and another vector. 
	public SbVec3f cross(final SbVec3f v) {
		
		  return new SbVec3f(g(1) * v.g(2) - g(2) * v.g(1),
				    g(2) * v.g(0) - g(0) * v.g(2),
				    g(0) * v.g(1) - g(1) * v.g(0));
				   
				  }
	
	// Returns right-handed cross product of vector and another vector. 
	public SbVec3f cross(final SbVec3f v, final SbVec3f dummy) {
		
		  dummy.setValue(g(1) * v.g(2) - g(2) * v.g(1),
				    g(2) * v.g(0) - g(0) * v.g(2),
				    g(0) * v.g(1) - g(1) * v.g(0));
				   
		  return dummy;
				  }
	
	public SbVec3f operator_cross_equal(SbVec3f v) {
		
		  float x = g(1) * v.g(2) - g(2) * v.g(1);
		  float y = g(2) * v.g(0) - g(0) * v.g(2);
		  float z = g(0) * v.g(1) - g(1) * v.g(0);
		  
		  s(0, x);
		  s(1, y);
		  s(2, z);
		
		return this;
	}

	
	// Returns dot (inner) product of vector and another vector. 
	public float dot(SbVec3f v) {
		
		  return (g(0) * v.g(0) +
				    g(1) * v.g(1) +
				    g(2) * v.g(2));
				  
				  }
	
	// Returns vector components for reading only. 
    public final float[] getValueRead() {
    	float[] vecRead = new float[3];
    	vecRead[0] = g(0); 
    	vecRead[1] = g(1); 
    	vecRead[2] = g(2); 
    	return vecRead; 
    }
    
    public final float getCoord(int dimension) {
    	return g(dimension);
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
    
    /**
     * Java port
     * @param index
     * @param value
     */
    public final void setValue(int index, float value) {
    	s(index, value);
    }
    
    /**
     * For JOGL
     * @return
     */
    public final FloatBuffer getValueGL() {
    	FloatBuffer fb = vec.toByteBuffer().asFloatBuffer();
    	fb.position(indice);
    	return fb;
    	//return FloatBuffer.wrap(vec,indice,3);
    }
	
    //
// Returns 3 individual components
//

public void
getValue(final float[] x, final float[] y, final float[] z)
{
    x[0] = g(0);
    y[0] = g(1);
    z[0] = g(2);
}
        
/**
 * java port
 * @param xyz
 */
public void
getValue(final float[] xyz)
{
    xyz[0] = g(0);
    xyz[1] = g(1);
    xyz[2] = g(2);
}
        
	//
	 // Returns geometric length of vector
	 //
	 
	 public float length()
	 {
	  return (float)Math.sqrt(g(0) * g(0) + g(1) * g(1) + g(2) * g(2));
	 }
	 	
	  public float sqrLength() { return g(0) * g(0) + g(1) * g(1) + g(2) * g(2); }
	
	// Changes vector to be unit length, returning 
	// the length before normalization. 
	public float normalize() {
		
		 float len = length();
		  
		   if (len != 0.0)
			   operator_mul_equal(1.0f / len);
		  
		   else setValue(0.0f, 0.0f, 0.0f);
		  
		   return len;
		 	}
	
	// Negates each component of vector in place. 
	public void negate() {
	     s(0, -g(0));
	     s(1, -g(1));
	     s(2, -g(2));	     		
	}
	
	// Sets the vector components. 
	public SbVec3f setValue(float[] v) {
		 s(0, v[0]); s(1, v[1]); s(2, v[2]); return this; 
	}
	
	// Sets the vector components. 
	public SbVec3f setValue(float x, float y, float z) {
		
		s(0, x); s(1, y); s(2, z); 
		return this;
	}
	
	// copy operator (java port)
	public void copyFrom(Object other) {
		SbVec3f sbVec3f = (SbVec3f)other;
		s(0, sbVec3f.g(0));
		s(1, sbVec3f.g(1));
		s(2, sbVec3f.g(2));
	}
	
	protected Object clone() {
		SbVec3f cloned = new SbVec3f();
		cloned.copyFrom(this);
		return cloned;
	}
	
	 //
	   // Component-wise vector addition operator
	   //
	   
	  public SbVec3f 
	   operator_plus_equal(SbVec3f v)
	   {
	       s(0,g(0) + v.g(0));
	       s(1,g(1) + v.g(1));
	       s(2,g(2) + v.g(2));
	   
	       return this;
	   }
	   	
	  //
// Component-wise vector subtraction operator
//

public SbVec3f 
operator_minus_equal(SbVec3f v)
{
    s(0, g(0) - v.g(0));
    s(1, g(1) - v.g(1));
    s(2, g(2) - v.g(2));

    return this;
}

	  
	
	//
	 // Component-wise scalar multiplication operator
	 //
	 
	public SbVec3f operator_mul_equal(float d) {
		
		  s(0,g(0) * d);
		    s(1,g(1) * d);
		    s(2,g(2) * d);
		   
		    return this;
	}
	
	// Component-wise scalar division operator. 
	public SbVec3f operator_div_equal(float d) {
		return operator_mul_equal(1.0f / d); 
	}
	
	// Component-wise vector addition operator.
	public SbVec3f operator_add_equal(SbVec3f v) {
		 s(0,g(0) + v.g(0));
		 s(1, g(1) + v.g(1));
		 s(2,g(2) + v.g(2));
		  
		   return this;		  		
	}
	
	// java port
	public float operator_square_bracket(int i) {
		return g(i);
	}
	
	// java port
	public float operator_square_bracket(int i, float value) {
		s(i, value);
		return value;
	}
	
	// Component-wise vector addition and subtraction operators. 
	public SbVec3f substract(SbVec3f v) {
	     s(0,g(0) - v.g(0));
          s(1,g(1) - v.g(1));
          s(2,g(2) - v.g(2));
      
          return this;
     	}

	// Component-wise binary scalar multiplication operator. 
	public SbVec3f operator_mul(float d) {
		SbVec3f v = this;
	     return new SbVec3f(v.g(0) * d,
	    		                     v.g(1) * d,
	    		                     v.g(2) * d);
	    		  	}

	 //
	   // Component-wise binary vector addition operator
	   //
	   
	  public SbVec3f operator_add(SbVec3f v2) {
		  SbVec3f v1 = this;
		     return new SbVec3f(v1.g(0) + v2.g(0),
		    		                     v1.g(1) + v2.g(1),
		    		                     v1.g(2) + v2.g(2));
		    		  	}
    
	  //
	   // Component-wise binary vector subtraction operator
	   //
	   
	  public SbVec3f operator_minus(SbVec3f v2) {
		  SbVec3f v1 = this;
		     return new SbVec3f(v1.g(0) - v2.g(0),
		    		                      v1.g(1) - v2.g(1),
		    		                      v1.g(2) - v2.g(2));		    		  		  
	  }
	  
	  // java port
	  public SbVec3f operator_minus(SbVec3f v2, SbVec3f dummy) {
		  SbVec3f v1 = this;
		     dummy.setValue(v1.g(0) - v2.g(0),
		    		                      v1.g(1) - v2.g(1),
		    		                      v1.g(2) - v2.g(2));
		     return dummy;
	  }
	  
	  //
	// Nondestructive unary negation - returns a new vector
	//
	  public SbVec3f operator_minus() {
		  return new SbVec3f(-g(0), -g(1), -g(2));
	  }

	  //
	// Nondestructive unary negation - returns a new vector
	//
	  public SbVec3f operator_minus_with_dummy(final SbVec3f dummy) {
		  dummy.setValue(-g(0), -g(1), -g(2));
		  return dummy;
	  }

	  public SbVec3f operator_div(float d)
	              { return operator_mul(1.0f / d); }
	  
	  public boolean operator_not_equal( SbVec3f v2)
{ 
		  SbVec3f v1 = this;
		  return !(v1.operator_equal_equal(v2)); 
}


//
// Equality comparison operator. Componenents must match exactly.
//

public boolean
operator_equal_equal( SbVec3f v2)
{
	SbVec3f v1 = this;
    return (v1.g(0) == v2.g(0) &&
            v1.g(1) == v2.g(1) &&
            v1.g(2) == v2.g(2));
}

	  
	  
//
// Returns principal axis that is closest (based on maximum dot
// product) to this vector.
//

private float TEST_AXIS( final SbVec3f axis, final SbVec3f bestAxis, float max) {
		float d;
	    if ((d = dot(axis)) > max) {                                              
	        max = d;                                                              
	        bestAxis.copyFrom( axis);                                                      
	    }
	    return max;
}

public SbVec3f
getClosestAxis()
{
    final SbVec3fSingle     axis = new SbVec3fSingle(0.0f, 0.0f, 0.0f), bestAxis = new SbVec3fSingle();
    float max = -21.234f;

    axis.getValue()[0] = 1.0f;      // +x axis
    max = TEST_AXIS(axis,bestAxis,max);

    axis.getValue()[0] = -1.0f;     // -x axis
    max = TEST_AXIS(axis,bestAxis,max);
    axis.getValue()[0] = 0.0f;

    axis.getValue()[1] = 1.0f;      // +y axis
    max = TEST_AXIS(axis,bestAxis,max);

    axis.getValue()[1] = -1.0f;     // -y axis
    max = TEST_AXIS(axis,bestAxis,max);
    axis.getValue()[1] = 0.0f;

    axis.getValue()[2] = 1.0f;      // +z axis
    max = TEST_AXIS(axis,bestAxis,max);

    axis.getValue()[2] = -1.0f;     // -z axis
    max = TEST_AXIS(axis,bestAxis,max);

    return bestAxis;
}

// java port
public float x() {
	return g(0);
}
  
//java port
public float y() {
	return g(1);
}
  
//java port
public float z() {
	return g(2);
}

/**
 * Allocates an array of SbVec3f
 * @param num
 * @return
 */
public static SbVec3f[] allocate(int num) {
	SbVec3f[] retVal = new SbVec3f[num];
	for(int i=0; i< num;i++) {
		retVal[i] = new SbVec3f();
	}
	return retVal;
}

/**
 * java port
 * @return
 */
	public DoubleConsumer[] getRef() {
		DoubleConsumer[] ref = new DoubleConsumer[3];
		ref[0] = value -> s(0, (float)value);
		ref[1] = value -> s(1, (float)value);
		ref[2] = value -> s(2, (float)value);
		return ref;
	}

//
// Equality comparison operator within a tolerance.
//

public boolean
equals(final SbVec3f v, float tolerance)
{
    final SbVec3f     diff = this.operator_minus(v);

    return diff.dot(diff) <= tolerance;
}

public float getX() { // java port
	return g(0);
}

public float getY() {
	return g(1);
}

public float getZ() {
	return g(2);
}

public void setX(float f) { // java port
	s(0, f);
}

public void setY(float f) {
	s(1, f);
}

public void setZ(float f) {
	s(2, f);
}

public float getValueAt(int axis) { // java port
	return g(axis);
}

/**
 * java port
 * @param f
 */
public void multiply(float f) {
	operator_mul_equal(f);
}

public void add(SbVec3f v) {
	operator_add_equal(v);
}

/**
 * java port
 * @param other
 */
public void setValue(SbVec3f other) {
	//setValue(other.getValueRead());
	s(0, other.g(0));
	s(1, other.g(1));
	s(2, other.g(2));
}

}
