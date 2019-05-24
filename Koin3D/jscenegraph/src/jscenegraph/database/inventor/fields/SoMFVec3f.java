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
 |   Classes:
 |      SoMFVec3f
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.function.DoubleConsumer;

import org.lwjgl.BufferUtils;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Array;
import jscenegraph.port.FloatArray;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.Util;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of three-dimensional vectors.
/*!
\class SoMFVec3f
\ingroup Fields
A multiple-value field that contains any number of three-dimensional
vectors.


SoMFVec3fs are written to file as one or more triples of floating
point values separated by whitespace.


When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 0 0 0, 1.2 3.4 5.6, 98.6 -4e1 212 ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFVec3f extends SoMField<SbVec3f> {
	
	private float[] valuesArray;
	
	private FloatBuffer[] valuesBuffer = new FloatBuffer[1];

	/**
	 * Sets the field to contain the given value and only the given value (if
	 * the array had multiple values before, they are deleted).
	 * 
	 * @param xyz
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets to one vector value from array of 3 floats. (Convenience function)
	//
	// Use: public

	public void setValue(final float[] xyz) {
		setValue(new SbVec3f(xyz));
	}

	/* Get pointer into array of values */
	@Deprecated
	public SbVec3f[] getValues(int start) {
		evaluate();

		SbVec3f[] shiftedValues = new SbVec3f[valuesArray.length/3 - start];
		for (int i = start; i < valuesArray.length/3; i++) {
			shiftedValues[i - start] = new SbVec3f(valuesArray,i*3);
		}
		return shiftedValues;
	}

	/* Get pointer into array of values 
	 * 
	 * Faster method
	 * 
	 * */
	public FloatArray getValuesArray(int start) {
		evaluate();
				
		FloatArray shiftedValues = new FloatArray( start*3, valuesArray);
		return shiftedValues;
	}

	@Deprecated
	public ByteBuffer getValuesBytes(int start) {
		FloatArray values = getValuesArray(start);
		return Util.toByteBuffer(values);
	}

	// java port
	@Deprecated
	public float[] getValuesFloat(int start) {
		evaluate();

		float[] shiftedValues = new float[(valuesArray.length/3 - start) * 3];
		int index = 0;
		for (int i = start; i < valuesArray.length/3; i++) {
			shiftedValues[index] = valuesArray[i*3];
			index++;
			shiftedValues[index] = valuesArray[i*3+1];
			index++;
			shiftedValues[index] = valuesArray[i*3+2];
			index++;
		}
		return shiftedValues;
	}
	
	/**
	 * Java port
	 * @return
	 */
	public float[] getValuesRef() {
		evaluate();
		
		return valuesArray;
	}

	// Set values from array of arrays of 3 floats.

	//
	// Description:
	// Sets values from array of arrays of 3 floats. This can be useful
	// in some applications that have vectors stored in this manner and
	// want to keep them that way for efficiency.
	//
	// Use: public

	public void setValues(int start, // Starting index
			float xyz[][]) // Array of vector values
	//
	{
		int num = xyz.length; // Number of values to set
		int newNum = start + num;
		int i;

		if (newNum > getNum())
			makeRoom(newNum);

		for (i = 0; i < num; i++) {
			valuesArray[(start + i)*3] = xyz[i][0];
			valuesArray[(start + i)*3+1] = xyz[i][1];
			valuesArray[(start + i)*3+2] = xyz[i][2];
		}
		valueChanged();
	}

	/**
	 * Java port
	 * @param start
	 * @param xyz
	 */
	public void setValues(int start, float[] xyz) {
		int num = xyz.length/3; // Number of values to set
		int newNum = start + num;
		int i;

		if (newNum > getNum())
			makeRoom(newNum);

		for (i = 0; i < num; i++) {
			valuesArray[(start + i)*3] = xyz[3*i];
			valuesArray[(start + i)*3+1] = xyz[3*i+1];
			valuesArray[(start + i)*3+2] = xyz[3*i+2];
		}
		valueChanged();
	}

	public void setValuesPointer(float[] userdata) {
		setValuesPointer(userdata, null);
	}
	
	/**
	 * Values in buffer must be already initialized
	 * @param userdata
	 * @param buffer
	 */
	public void setValuesPointer(float[] userdata, FloatBuffer buffer) {
		makeRoom(0);
		  if (userdata != null) { 
			    valuesArray = userdata;
			    if(buffer != null && buffer.capacity() == userdata.length) {
			    	valuesBuffer[0] = buffer;
			    }
			    else {
			    	valuesBuffer[0] = BufferUtils.createFloatBuffer(userdata.length);
				    valuesBuffer[0].clear();
				    valuesBuffer[0].put(valuesArray, 0, userdata.length);
				    valuesBuffer[0].flip();
			    }
			    // userDataIsUsed = true; COIN3D 
			    num = maxNum = userdata.length/3; 
			    valueChanged(); 
		} 
		
	}
	
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets values from array of arrays of 3 floats. This can be useful
	// in some applications that have vectors stored in this manner and
	// want to keep them that way for efficiency.
	//
	// Use: public

	public void setValues(int start, // Starting index
			int num, // Number of values to set
			final float xyz[][/* 3 */]) // Array of vector values
	//
	////////////////////////////////////////////////////////////////////////
	{
		int newNum = start + num;
		int i;

		if (newNum > getNum())
			makeRoom(newNum);

		for (i = 0; i < num; i++) {
			valuesArray[(start + i)*3] = xyz[i][0];
			valuesArray[(start + i)*3+1] = xyz[i][1];
			valuesArray[(start + i)*3+2] = xyz[i][2];
		}
		valueChanged();
	}

	/**
	 * java port
	 * 
	 * @param start
	 * @param num
	 * @param skyBoxVertices
	 */
	public void setValues(int start, int num, float[][][] xyz3d) {
		int xyzLength = 0;
		int xyz3dLength = xyz3d.length;
		for (int i = 0; i < xyz3dLength; i++) {
			if (xyz3d[i] != null) {
				xyzLength += xyz3d[i].length;
			}
		}
		float[][] xyz = new float[xyzLength][];
		int j = 0;
		for (int i = 0; i < xyz3dLength; i++) {
			if (xyz3d[i] != null) {
				float[][] iArray = xyz3d[i];
				int iLength = iArray.length;
				for (int k = 0; k < iLength; k++) {
					xyz[j] = xyz3d[i][k];
					j++;
				}
			}
		}
		setValues(start, num, xyz);
	}

	@Override
	protected SbVec3f constructor() {
		return new SbVec3f();
	}

	@Override
	protected SbVec3f[] arrayConstructor(int length) {
		return new SbVec3f[length];
	}
	
	private float[] arrayConstructorInternal(int length) {
		return new float[length*3];
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Reads one (indexed) value from file. Returns FALSE on error.
	//
	// Use: private

	public boolean read1Value(SoInput in, int index)
	//
	////////////////////////////////////////////////////////////////////////
	{
		DoubleConsumer[] ref = getValuesSbVec3fArray().get(index).getRef();
		return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]));
	}

    //! Set the \p index'th value to the given floating point values.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Sets one vector value from 3 separate floats. (Convenience function)
//
//Use: public

	public void set1Value(int index, float x, float y, float z) {
	    set1Value(index, new SbVec3f(x, y, z));		
	}

	protected void allocValues(int newNum) {
		if (valuesArray == null) {
			if (newNum > 0) {
				valuesArray = arrayConstructorInternal(newNum);
			}
		} else {
			float[] oldValues = valuesArray;
			int i;

			if (newNum > 0) {
				valuesArray = arrayConstructorInternal(newNum);
				for (i = 0; i < num && i < newNum; i++) { // FIXME : array optimisation
					valuesArray[3*i] = oldValues[3*i];
					valuesArray[3*i+1] = oldValues[3*i+1];
					valuesArray[3*i+2] = oldValues[3*i+2];
				}
			} else
				valuesArray = null;
			// delete [] oldValues; java port
		}

		num = maxNum = newNum;
	}

	/* Set field to have one value */
	public void setValue(SbVec3f newValue) {
		makeRoom(1);
		Mutable dest = new SbVec3f(valuesArray,0);
		Mutable src = (Mutable) newValue;
		dest.copyFrom(src);
		valueChanged();
	}

    /* Get non-const pointer into array of values for batch edits */          
    public SbVec3f[] startEditing()                                
        { 
    	evaluate(); 
    	return getValues(0); 
    	}                                        
                                                                              
	/* Set 1 value at given index */
	public void set1Value(int index, SbVec3f newValue) {
		if (index >= getNum())
			makeRoom(index + 1);
		valuesArray[index*3] = newValue.getX();
		valuesArray[index*3+1] = newValue.getY();
		valuesArray[index*3+2] = newValue.getZ();
		valueChanged();
	}

	public SbVec3f operator_square_bracket(int i) {
		evaluate();
		return new SbVec3f(valuesArray,i*3);
	}
	
    public SbVec3fArray startEditingFast()                                
    { 
    	evaluate(); 
    	return new SbVec3fArray(valuesArray); 
	}                                        

    public SbVec3fArray getValuesSbVec3fArray() {
		evaluate();

		return new SbVec3fArray(valuesArray, valuesBuffer); 		
    }
}

