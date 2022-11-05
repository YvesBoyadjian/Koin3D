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
 |      SoMFInt32
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.IntArray;
import jscenegraph.port.IntArrayPtr;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of int32_t integers.
/*!
\class SoMFInt32
\ingroup Fields
A multiple-value field that contains any number of int32_t (32-bit)
integers.


SoMFInt32s are written to file as one or more integer values, in
decimal, hexadecimal or octal format.  When more than one value is
present, all of the values are enclosed in square brackets and
separated by commas; for example:
\code
[ 17, -0xE20, -518820 ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFInt32 extends SoMField<Integer,IntArray> {

	//private int[] values;

	/**
	 * java port
	 * @param start
	 * @param newValues
	 */
//    public void setValues(int start, int[] newValues) {
//    	Integer[] newIValues = new Integer[newValues.length];
//    	for(int i=0;i<newValues.length;i++) {
//    		newIValues[i] = new Integer(newValues[i]);
//    	}
//    	super.setValues(start,newIValues);
//    }

                /**
                 * java port                                                              
                 * @param start
                 * @param localNum
                 * @param newValues
                 */
    public void setValues(int start, int localNum, int[] newValues)             
{                                                                             
    int newNum = start + localNum, i;                                         
                                                                              
    if (newNum > getNum())                                                    
        makeRoom(newNum);                                                     
                                                                              
    for (i = 0; i < localNum; i++)                                                    
        values.set(start + i, newValues[i]);                                     
                                                                              
    valueChanged();                                                           
}                                                                             
                                                                              
    
	@Override
	protected Integer constructor() {
		return 0;
	}

	@Override
	protected IntArray arrayConstructor(int length) {
		return new IntArray(length);
	}
	
	// java port
//	public int[] getValuesInt(int index) {
//		Integer[] valuesInteger = getValues(index);
//		int returnLength = valuesInteger.length;
//		int[] returnValue = new int[returnLength];
//		for(int i=0; i< returnLength;i++) {
//			returnValue[i] = valuesInteger[i];
//		}
//		return returnValue;
//	}


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
	final int[] ret = new int[1];
    if (in.read(ret)) {
    	values.set(index, ret[0]);
    	return true;
    }
    return false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads array of binary values from file as one chunk.
//
// Use: private

protected boolean
readBinaryValues(SoInput in,    // Reading specification
                          int numToRead)   // Number of values to read
//
////////////////////////////////////////////////////////////////////////
{
	int[] valuesI = new int[numToRead];
	boolean retVal = (in.readBinaryArray(valuesI, numToRead));
	if(retVal) {
		for(int i=0;i<numToRead;i++) values.set(i, valuesI[i]);
	}
	return retVal;
}

/**
 * Java port
 * @param buffer
 */
public void setValuesBuffer(ByteBuffer buffer) {
	IntBuffer ib = buffer.asIntBuffer();
	int length = ib.remaining();
	int[] array = new int[length];
	ib.get(array);
	setValues(0,array);
}

//protected void allocValues(int newNum) {
//	if (values == null) {
//		if (newNum > 0) {
//			values = new int[newNum];
//		}
//	} else {
//		int[] oldValues = values;
//		int i;
//
//		if (newNum > 0) {
//			values = new int[newNum];
//			for (i = 0; i < num && i < newNum; i++)
//				values[i] = oldValues[i];
//		} else
//			values = null;
//		// delete [] oldValues; java port
//	}
//
//	num = maxNum = newNum;
//}

/* Set field to have one value */
public void setValue(int newValue) {
	makeRoom(1);
	values.set(0, newValue);
	valueChanged();
}

public void setValues(int start, int[] newValues) {
	int localNum = newValues.length;
	int newNum = start + localNum, i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < localNum; i++) {
		values.set(start + i, newValues[i]);
	}
	valueChanged();

}

public void setValuesPointer(int[] newValues) {
	makeRoom(0);
	if(newValues != null) {
		values = new IntArray(0,newValues);
		num = maxNum = newValues.length;
		valueChanged();
	}
}

/* Set 1 value at given index */
public void set1Value(int index, int newValue) {
	if (index >= getNum())
		makeRoom(index + 1);
	values.set(index, newValue);
	valueChanged();
}

/* Get pointer into array of values */
public int[] getValuesI(int start) {
	evaluate();
	
	if(start == 0) {
		if (values == null) {
			return null;
		}
		return values.values();
	}
	
	throw new IllegalArgumentException();

//	int retLength = values.length() - start;
//
//	int[] retVal = new int[retLength];
//
//	for (int i = 0; i < retLength; i++) {
//		retVal[i] = (int) values.get(i + start);
//	}
//	return retVal;
}

/**
 * Java port
 * @param start
 * @return
 */
public IntArrayPtr getValuesIntArrayPtr(int start) {
	evaluate();
	
	if( values == null ) {
		return null;
	}

	return new IntArrayPtr(start, values);
}

public int operator_square_bracketI(int i) {
	evaluate();
	return (int) values.get(i);
}

//public Integer operator_square_bracket(int i) {
//	evaluate();
//	return (Integer) values.getO(i);
//}

// ! Copies value indexed by "from" to value indexed by "to"
protected void copyValue(int to, int from) {
	values.set(to, values.get(from));
}


///* Get non-const pointer into array of values for batch edits */          
//public int[] startEditingI()                                
//    { evaluate(); return values; }                                        
                                                                          
}
