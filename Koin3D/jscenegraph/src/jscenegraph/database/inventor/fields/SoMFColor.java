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
 |      SoMFColor
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbColorArray;
import jscenegraph.port.SbVec3fArray;


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of RGB colors stored as three floats.
/*!
\class SoMFColor
\ingroup Fields
A multiple-value field that contains any number of RGB colors, stored
as instances of SbColor.  Values may be set in either RGB (red,
green, blue) or HSV (hue, saturation, value) color spaces.


SoMFColors are written to file as one or more RGB triples of
floating point numbers in standard scientific notation.  When more
than one value is present, all of the values are enclosed in square
brackets and separated by commas.  For example:
\code
[ 1.0 0.0 0.0, 0 1 0, 0 0 1 ]
\endcode
represents the three colors red, green, and blue.

\par See Also
\par
SbColor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFColor extends SoMField<SbColor> {

	private float[] valuesArray;
	
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

	public void setValue(float r, float g, float b) {
		setValue(new SbColor(r, g, b));
	}
	
	public void setValue(final float[] rgb) {
		setValue(new SbColor(rgb));
	}
	
	/* Set field to have one value */
	public void setValue(SbColor newValue) {
		makeRoom(1);
		Mutable dest = new SbColor(valuesArray,0);
		Mutable src = (Mutable) newValue;
		dest.copyFrom(src);
		valueChanged();
	}

	/**
	 * java port
	 * @param start
	 * @param newValues
	 */
	public void setValues(int start, float[][] newValues) {
		int num = newValues.length; // Number of values to set
		int newNum = start + num;
		int i;

		if (newNum > getNum())
			makeRoom(newNum);

		for (i = 0; i < num; i++) {
			valuesArray[(start + i)*3] = newValues[i][0];
			valuesArray[(start + i)*3+1] = newValues[i][1];
			valuesArray[(start + i)*3+2] = newValues[i][2];
		}
		valueChanged();
	}


	/**
	 * java port
	 * @param start
	 * @param newValues
	 */
public void setValues(int start, float[] newValues) {
	int num = newValues.length/3; // Number of values to set
	int newNum = start + num;
	int i;

	if (newNum > getNum())
		makeRoom(newNum);

	for (i = 0; i < num; i++) {
		valuesArray[(start + i)*3] = newValues[3*i];
		valuesArray[(start + i)*3+1] = newValues[3*i+1];
		valuesArray[(start + i)*3+2] = newValues[3*i+2];
	}
	valueChanged();
}
	@Override
	protected SbColor constructor() {
		return new SbColor();
	}

	@Override
	protected SbColor[] arrayConstructor(int length) {
		return new SbColor[length];
	}

	private float[] arrayConstructorInternal(int length) {
		return new float[length*3];
	}

	/* Get pointer into array of values */
	@Deprecated
	public SbColor[] getValues(int start) {
		evaluate();

		SbColor[] shiftedValues = new SbColor[valuesArray.length/3 - start];
		for (int i = start; i < valuesArray.length/3; i++) {
			shiftedValues[i - start] = new SbColor(valuesArray,i*3);
		}
		return shiftedValues;
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
	DoubleConsumer[] ref = getValuesSbColorArray().get(index).getRef();
	return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads array of binary values from file as one chunk.
//
// Use: private

protected boolean
readBinaryValues(SoInput in, int numToRead)
//
////////////////////////////////////////////////////////////////////////
{
	float[] valsFloat = new float[3 * numToRead];
    if(in.readBinaryArray((float[] ) valsFloat, 3 * numToRead)) {
	    for(int i=0; i< numToRead;i++) {	    	
	    	valuesArray[i*3] = valsFloat[3*i];
	    	valuesArray[i*3+1] = valsFloat[3*i+1];
	    	valuesArray[i*3+2] = valsFloat[3*i+2];
	    	//((SbColor)values[i]).setValue(valsFloat[3*i],valsFloat[3*i+1],valsFloat[3*i+2]);
	    }
	    return true;
    }
    return false;
}

/* Get non-const pointer into array of values for batch edits */          
public SbColor[] startEditing()                                
    { 
	evaluate(); 
	return getValues(0); 
	}                                        
                                                                          
/**
 * java port
 * @param index
 * @param rgb
 */
public void set1Value(int index, float[] rgb) {
	set1Value(index, new SbColor(rgb));
}

/* Set 1 value at given index */
public void set1Value(int index, SbColor newValue) {
	if (index >= getNum())
		makeRoom(index + 1);
	valuesArray[index*3] = newValue.getX();
	valuesArray[index*3+1] = newValue.getY();
	valuesArray[index*3+2] = newValue.getZ();
	valueChanged();
}

public SbColor operator_square_bracket(int i) {
	evaluate();
	return new SbColor(valuesArray,i*3);
}

public SbColorArray startEditingFast()                                
{ 
	evaluate(); 
	return new SbColorArray(valuesArray); 
}                                        

public SbColorArray getValuesSbColorArray() {
	evaluate();

	return new SbColorArray(valuesArray); 		
}
}
