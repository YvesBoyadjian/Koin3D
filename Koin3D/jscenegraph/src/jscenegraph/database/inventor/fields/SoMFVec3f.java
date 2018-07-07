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
import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Array;
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
	public SbVec3f[] getValues(int start) {
		evaluate();

		SbVec3f[] shiftedValues = new SbVec3f[values.length - start];
		for (int i = start; i < values.length; i++) {
			shiftedValues[i - start] = (SbVec3f) values[i];
		}
		return shiftedValues;
	}

	/* Get pointer into array of values 
	 * 
	 * Faster method
	 * 
	 * */
	public Array<SbVec3f> getValuesArray(int start) {
		evaluate();
				
		Array<SbVec3f> shiftedValues = new Array<SbVec3f>(SbVec3f.class, start, values);
		return shiftedValues;
	}

	public ByteBuffer getValuesBytes(int start) {
		Array<SbVec3f> values = getValuesArray(start);
		return Util.toByteBuffer(values);
	}

	// java port
	public float[] getValuesFloat(int start) {
		evaluate();

		float[] shiftedValues = new float[(values.length - start) * 3];
		int index = 0;
		for (int i = start; i < values.length; i++) {
			shiftedValues[index] = ((SbVec3f) values[i]).getValueRead()[0];
			index++;
			shiftedValues[index] = ((SbVec3f) values[i]).getValueRead()[1];
			index++;
			shiftedValues[index] = ((SbVec3f) values[i]).getValueRead()[2];
			index++;
		}
		return shiftedValues;
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

		for (i = 0; i < num; i++)
			((SbVec3f) values[start + i]).setValue(xyz[i]);

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

		for (i = 0; i < num; i++)
			((SbVec3f) values[start + i]).setValue(xyz[3*i],xyz[3*i+1],xyz[3*i+2]);

		valueChanged();
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

		for (i = 0; i < num; i++)
			((SbVec3f) values[start + i]).setValue(xyz[i]);

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
		DoubleConsumer[] ref = getValues(0)[index].getRef();
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

	
}
