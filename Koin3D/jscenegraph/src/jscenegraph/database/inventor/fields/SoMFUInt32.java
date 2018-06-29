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
 |      SoMFUInt32
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.util.function.IntConsumer;

import jscenegraph.database.inventor.SoInput;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of uint32_tegers.
/*!
\class SoMFUInt32
\ingroup Fields
A multiple-value field that contains any number of uint32_t
(32-bit) integers.


SoMFUInt32s are written to file as one or more uint32_t
integers, in decimal, hexadecimal or octal format.


When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 17, 0xFFFFE0, 0755 ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFUInt32 extends SoMField<Object> {

	private int[] values;

	@Override
	protected Integer constructor() {
		return new Integer(0);
	}

	@Override
	protected Integer[] arrayConstructor(int length) {
		return new Integer[length];
	}


protected void allocValues(int newNum) {
	if (values == null) {
		if (newNum > 0) {
			values = new int[newNum];
		}
	} else {
		int[] oldValues = values;
		int i;

		if (newNum > 0) {
			values = new int[newNum];
			for (i = 0; i < num && i < newNum; i++)
				values[i] = oldValues[i];
		} else
			values = null;
		// delete [] oldValues; java port
	}

	num = maxNum = newNum;
}

//! Copies value indexed by "from" to value indexed by "to"
protected void copyValue(int to, int from) {
	values[to] = values[from];
}

	
	// java port
	public int[] getValuesI(int start) {
		evaluate();
		
		if(start == 0) {
			return values;
		}

		int retLength = values.length - start;

		int[] retVal = new int[retLength];

		for (int i = 0; i < retLength; i++) {
			retVal[i] = (int) values[i + start];
		}
		return retVal;
	}


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
    return in.read(new IntConsumer(){

		@Override
		public void accept(int value) {
			values[index] = value;
		}
	});
}

/* Set field to have one value */
public void setValue(int newValue) {
	makeRoom(1);
	values[0] = newValue;
	valueChanged();
}

/**
 * java port
 * @param start
 * @param colors
 */
public void setValues(int start, int[] newValues) {
	int localNum = newValues.length;
	int newNum = start + localNum, i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < localNum; i++) {
		values[start + i] = newValues[i];
	}
	valueChanged();

}

public int operator_square_bracketI(int i) {
	evaluate();
	return (int) values[i];
}

}
