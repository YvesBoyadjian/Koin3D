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
 |      SoMFVec4f
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.nio.ByteBuffer;
import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Array;
import jscenegraph.port.FloatArray;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.SbVec4fArray;
import jscenegraph.port.Util;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of four-dimensional vectors.
/*!
\class SoMFVec4f
\ingroup Fields
A multiple-value field that contains any number of four-dimensional
vectors.


SoMFVec4fs are written to file as one or more triples of floating
point values separated by whitespace.


When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 0 0 0, 1.2 3.4 5.6, 98.6 -4e1 212 ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

public class SoMFVec4f extends SoMField<SbVec4f,SbVec4fArray> {

	//private FloatMemoryBuffer valuesArray;

	@Override
	protected SbVec4f constructor() {
		return new SbVec4f();
	}

	@Override
	protected SbVec4fArray arrayConstructor(int length) {
		return new SbVec4fArray(FloatMemoryBuffer.allocateFloats(length*4));
	}

	/* Get pointer into array of values 
	 * 
	 * Faster method
	 * 
	 * */
//	public Array<SbVec4f> getValuesArray(int start) {
//		evaluate();
//				
//		Array<SbVec4f> shiftedValues = new Array<SbVec4f>(SbVec4f.class, start, values);
//		return shiftedValues;
//	}

	/* Get pointer into array of values */
//	@Deprecated
//	public SbVec4f[] getValues(int start) {
//		evaluate();
//
//		SbVec4f[] shiftedValues = new SbVec4f[valuesArray.numFloats()/4 - start];
//		for (int i = start; i < valuesArray.numFloats()/4; i++) {
//			shiftedValues[i - start] = new SbVec4f(valuesArray,i*4);
//		}
//		return shiftedValues;
//	}

	/**
	 * Java port
	 * @return
	 */
//	public FloatMemoryBuffer getValuesRef() {
//		evaluate();
//		
//		return valuesArray;
//	}
//
	/* Get pointer into array of values 
	 * 
	 * Faster method
	 * 
	 * */
	public FloatArray getValuesArray(int start) {
		evaluate();
				
		return values.toFloatArray().plus(start*4);
//		FloatArray shiftedValues = new FloatArray( start*4, values.);
//		return shiftedValues;
	}

//	public ByteBuffer getValuesBytes(int start) {
//		FloatArray values = getValuesArray(start);
//		return Util.toByteBuffer(values);
//	}

	public void setValuesPointer(FloatMemoryBuffer userdata) {
		makeRoom(0);
		  if (userdata != null) { 
			    values = new SbVec4fArray(userdata);
			    // userDataIsUsed = true; COIN3D 
			    num = maxNum = userdata.numFloats()/4; 
			    valueChanged(); 
		} 
		
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
		DoubleConsumer[] ref = getValuesSbVec4fArray().get(index).getRef();
		return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]) && in.read(ref[3]));
	}

    //! Set the \p index'th value to the given floating point values.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Sets one vector value from 3 separate floats. (Convenience function)
//
//Use: public

	public void set1Value(int index, float x, float y, float z, float w) {
	    set1Value(index, new SbVec4f(x, y, z, w));		
	}

//	protected void allocValues(int newNum) {
//		if (valuesArray == null) {
//			if (newNum > 0) {
//				valuesArray = arrayConstructorInternal(newNum);
//			}
//		} else {
//			FloatMemoryBuffer oldValues = valuesArray;
//			int i;
//
//			if (newNum > 0) {
//				valuesArray = arrayConstructorInternal(newNum);
//				for (i = 0; i < num && i < newNum; i++) { // FIXME : array optimisation
//					valuesArray.setFloat(4*i, oldValues.getFloat(4*i));
//					valuesArray.setFloat(4*i+1, oldValues.getFloat(4*i+1));
//					valuesArray.setFloat(4*i+2, oldValues.getFloat(4*i+2));
//					valuesArray.setFloat(4*i+3, oldValues.getFloat(4*i+3));
//				}
//			} else
//				valuesArray = null;
//			// delete [] oldValues; java port
//		}
//
//		num = maxNum = newNum;
//	}

	/* Set field to have one value */
	public void setValue(SbVec4f newValue) {
		makeRoom(1);
		Mutable dest = values.getO(0);// new SbVec4f(valuesArray,0);
		Mutable src = (Mutable) newValue;
		dest.copyFrom(src);
		valueChanged();
	}

    /* Get non-const pointer into array of values for batch edits */          
//    public SbVec4f[] startEditing()                                
//        { 
//    	evaluate(); 
//    	return getValues(0); 
//    	}                                        
                                                                              
	/* Set 1 value at given index */
	public void set1Value(int index, SbVec4f newValue) {
		if (index >= getNum())
			makeRoom(index + 1);
		values.setO(index,newValue);
//		valuesArray.setFloat(index*4, newValue.getX());
//		valuesArray.setFloat(index*4+1, newValue.getY());
//		valuesArray.setFloat(index*4+2, newValue.getZ());
//		valuesArray.setFloat(index*4+3, newValue.getW());
		valueChanged();
	}

    public SbVec4fArray getValuesSbVec4fArray() {
		evaluate();

		return values;//new SbVec4fArray(valuesArray); 		
    }
}
