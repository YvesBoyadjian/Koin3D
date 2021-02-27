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
 |      SoMFVec2f
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

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FloatArray;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.Util;
import jscenegraph.port.VoidPtr;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of two-dimensional vectors.
/*!
\class SoMFVec2f
\ingroup Fields
A multiple-value field that contains any number of two-dimensional
vectors.


SoMFVec2fs are written to file as one or more pairs of floating
point values separated by whitespace.
When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 0 0, 1.2 3.4, 98.6 -4e1 ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFVec2f extends SoMField<SbVec2f,SbVec2fArray> {

//	private SbVec2fArray vec2fArray;
//	
//	private FloatMemoryBuffer valuesArray;
	
	//private FloatBuffer[] valuesBuffer = new FloatBuffer[1];

	@Override
	protected SbVec2f constructor() {
		return new SbVec2f();
	}

	@Override
	protected SbVec2fArray arrayConstructor(int length) {
		return new SbVec2fArray(FloatMemoryBuffer.allocateFloats(length*2));
	}

         // java port
//         public float[] getValuesFloat(int start)
//             { 
//        	 evaluate();
//        	 
//        	 float[] shiftedValues = new float[(values.length-start)*2];
//        	 int index = 0;
//        	 for(int i=start;i<values.length;i++) {
//        		 shiftedValues[index] = ((SbVec2f)values[i]).getValueRead()[0];
//        		 index++;
//        		 shiftedValues[index] = ((SbVec2f)values[i]).getValueRead()[1];
//        		 index++;
//        	 }        	 
//             return shiftedValues; 
//             }
         
//     	public float[] getValuesFloat(int start) {
//    		evaluate();
//
//    		float[] shiftedValues = new float[(valuesArray.numFloats()/2 - start) * 2];
//    		int index = 0;
//    		for (int i = start; i < valuesArray.numFloats()/2; i++) {
//    			shiftedValues[index] = valuesArray.getFloat(i*2);
//    			index++;
//    			shiftedValues[index] = valuesArray.getFloat(i*2+1);
//    			index++;
//    		}
//    		return shiftedValues;
//    	}
    	
         
     	/* Get pointer into array of values 
     	 * 
     	 * Faster method
     	 * 
     	 * */
//     	public Array<SbVec2f> getValuesArray(int start) {
//     		evaluate();
//     				
//     		Array<SbVec2f> shiftedValues = new Array<SbVec2f>(SbVec2f.class, start, values);
//     		return shiftedValues;
//     	}      
     	
    	/* Get pointer into array of values 
    	 * 
    	 * Faster method
    	 * 
    	 * */
    	public FloatArray getValuesArray(int start) {
    		evaluate();
    				
    		FloatArray shiftedValues = new FloatArray( start*2 + values.getStart()*2, values.getValuesArray());
    		return shiftedValues;
    	}

     	
         
//         public ByteBuffer getValuesBytes(int start) {
//        	 SbVec2f[] values = getValues(start);
//        	 return Util.toByteBuffer(values);
//         }

//     	public ByteBuffer getValuesBytes(int start) {
//    		FloatArray values = getValuesArray(start);
//    		return Util.toByteBuffer(values);
//    	}

         
 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads one (indexed) value from file. Returns FALSE on error.
//
// Use: private

public boolean read1Value(SoInput in, int index)
//
////////////////////////////////////////////////////////////////////////
{
	float[] dummy1 = new float[1];
	
	if(in.read(dummy1)) {
		//values.set(index*2, dummy1[0]);
	}
	else {
		return false;
	}
	
	float[] dummy2 = new float[1];
	
	if(in.read(dummy2)) {
		//valuesArray.setFloat(index*2+1, dummy2[0]);
	}	
	else {
		return false;
	}
	
	values.getO(index).setValue(dummy1[0],dummy2[0]);
	
	return true;
//	DoubleConsumer[] ref = ((SbVec2f)values[index]).getRef();
//    return (in.read(ref[0]) &&
//            in.read(ref[1]));
}

/**
 * Java port
 * @param start
 * @param values
 */
//public void setValues(int start, float[][] values) {
//	int nb = values.length;
//	SbVec2f[] valuesVec = new SbVec2f[nb];
//	for(int i=0;i<nb;i++) {
//		valuesVec[i] = new SbVec2f(values[i]);
//	}
//	setValues(start,valuesVec);
//}

//
// Description:
// Sets values from array of arrays of 2 floats. This can be useful
// in some applications that have vectors stored in this manner and
// want to keep them that way for efficiency.
//
// Use: public

public void setValues(int start, // Starting index
		float xy[][]) // Array of vector values
//
{
	int num = xy.length; // Number of values to set
	int newNum = start + num;
	int i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < num; i++) {
		values.getO(i + start).setValue(xy[i][0],xy[i][1]);
//		valuesArray.setFloat((start + i)*2, xy[i][0]);
//		valuesArray.setFloat((start + i)*2+1, xy[i][1]);
	}
	valueChanged();
}


/* Get pointer into array of values */
//public SbVec2f[] getValues(int start) {
//	evaluate();
//
//	SbVec2f[] shiftedValues = new SbVec2f[valuesArray.numFloats()/2 - start];
//	for (int i = start; i < valuesArray.numFloats()/2; i++) {
//		shiftedValues[i - start] = new SbVec2f(valuesArray,i*2);
//	}
//	return shiftedValues;
//}
//
/**
 * Java port
 * @return
 */
//public FloatMemoryBuffer getValuesRef() {
//	evaluate();
//	
//	return values.valuesArray;
//}
//
/**
 * Java port
 * @param start
 * @param xy
 */
public void setValues(int start, float[] xy) {
	int num = xy.length/2; // Number of values to set
	int newNum = start + num;
	int i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < num; i++) {
		values.getO(start + i).setValue(xy[2*i],xy[2*i+1]);
//		valuesArray.setFloat((start + i)*2, xy[2*i]);
//		valuesArray.setFloat((start + i)*2+1, xy[2*i+1]);
	}
	valueChanged();
}

public void setValues(int start, SbVec2f[] xy) {
	int num = xy.length;
	int newNum = start + num;
	
	if (newNum > getNum())
		makeRoom(newNum);
	
	for(int i=0; i<num; i++) {
		values.getO(start +i).copyFrom(xy[i]);
//		valuesArray.setFloat((start + i)*2, xy[i].getX());
//		valuesArray.setFloat((start + i)*2+1, xy[i].getY());		
	}
	valueChanged();
}

//public void setValuesPointer(float[] userdata) {
//	setValuesPointer(userdata, null);
//}

/**
 * Values in buffer must be already initialized
 * @param userdata
 * @param buffer
 */
public void setValuesPointer(FloatMemoryBuffer userdata) {
	setValuesPointer(userdata,true);
}

public void setValuesPointer(FloatMemoryBuffer userdata, boolean keepOwnership) {
	makeRoom(0);
	  if (userdata != null) { 
		    values = new SbVec2fArray(userdata);
//		    if(buffer != null && buffer.capacity() == userdata.length) {
//		    	valuesBuffer[0] = buffer;
//		    }
//		    else {
//		    	valuesBuffer[0] = BufferUtils.createFloatBuffer(userdata.length);
//			    valuesBuffer[0].clear();
//			    valuesBuffer[0].put(valuesArray, 0, userdata.length);
//			    valuesBuffer[0].flip();
//		    }
		    if(keepOwnership) {
		    	userDataIsUsed = true;
		    }
		    num = maxNum = userdata.numFloats()/2; 
		    valueChanged(); 
	} 
	
}

////////////////////////////////////////////////////////////////////////
//
// Description:
// Sets values from array of arrays of 2 floats. This can be useful
// in some applications that have vectors stored in this manner and
// want to keep them that way for efficiency.
//
// Use: public

public void setValues(int start, // Starting index
		int num, // Number of values to set
		final float xy[][/* 2 */]) // Array of vector values
//
////////////////////////////////////////////////////////////////////////
{
	int newNum = start + num;
	int i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < num; i++) {
		values.getO(start+i).setValue(xy[i][0], xy[i][1]);
//		valuesArray.setFloat((start + i)*2, xy[i][0]);
//		valuesArray.setFloat((start + i)*2+1, xy[i][1]);
	}
	valueChanged();
}

//protected void allocValues(int newNum) {
//	if (valuesArray == null) {
//		if (newNum > 0) {
//			valuesArray = arrayConstructorInternal(newNum);
//		}
//	} else {
//		FloatMemoryBuffer oldValues = valuesArray;
//		int i;
//
//		if (newNum > 0) {
//			valuesArray = arrayConstructorInternal(newNum);
//			for (i = 0; i < num && i < newNum; i++) { // FIXME : array optimisation
//				valuesArray.setFloat(2*i, oldValues.getFloat(2*i));
//				valuesArray.setFloat(2*i+1, oldValues.getFloat(2*i+1));
//			}
//		} else
//			valuesArray = null;
//		if( vec2fArray != null ) {
////			if( VoidPtr.has(vec2fArray)) {
////				Destroyable.delete(VoidPtr.create(vec2fArray));
////			}
//			vec2fArray = null;
//		}
//		// delete [] oldValues; java port
//	}
//
//	num = maxNum = newNum;
//}

/* Set field to have one value */
public void setValue(SbVec2f newValue) {
	makeRoom(1);
	Mutable dest = values.getO(0);//new SbVec2f(valuesArray,0);
	Mutable src = (Mutable) newValue;
	dest.copyFrom(src);
	valueChanged();
}

/* Get non-const pointer into array of values for batch edits */          
//public SbVec2f[] startEditing()                                
//    { 
//	evaluate(); 
//	return getValues(0); 
//	}                                        
                                                                          
/* Set 1 value at given index */
public void set1Value(int index, SbVec2f newValue) {
	if (index >= getNum()) {
		makeRoom(index + 1);
	}
	values.getO(index).copyFrom(newValue);
	
//	valuesArray.setFloat(index*2, newValue.getX());
//	valuesArray.setFloat(index*2+1, newValue.getY());
	valueChanged();
}

public SbVec2f operator_square_bracket(int i) {
	evaluate();
	return values.getO(i);//new SbVec2f(valuesArray,i*2);
}
                                                                      
public SbVec2fArray getValuesSbVec2fArray() {
	evaluate();

//	if( vec2fArray == null || vec2fArray.getValuesArray() != valuesArray ) {
//		vec2fArray = new SbVec2fArray(valuesArray);
//	}
	return values;
}

}
