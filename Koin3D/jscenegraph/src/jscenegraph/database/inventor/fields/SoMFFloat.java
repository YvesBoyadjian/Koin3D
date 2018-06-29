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
 |      SoMFFloat
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of floating point values.
/*!
\class SoMFFloat
\ingroup Fields
A multiple-value field that contains any number of floating point
values. 


SoMFFloats are written to file as one or more values in standard
scientific notation.  When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 1.0, 2.3, 5, 6.2e4, -100, ]
\endcode
The last comma is optional.

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFFloat extends SoMField<Float> {

    public void setValue(float newValue) {
    	super.setValue(new Float(newValue));
    }
    
    /**
     * Java port
     * @param start
     * @param newValues
     */
    public void setValues(int start, float[] newValues) {
    	int nbFloats = newValues.length;
    	Float[] floats = new Float[nbFloats];
    	for(int i=0;i<nbFloats;i++) {
    		floats[i] = newValues[i];
    	}
		setValues(start, floats);
    }

	@Override
	protected Float constructor() {
		return new Float(0);
	}

	@Override
	protected Float[] arrayConstructor(int length) {
		return new Float[length];
	}

	// java port
	public float[] getValuesFloat(int index) {
		Float[] valuesFloat = getValues(index);
		int returnLength = valuesFloat.length;
		float[] returnValue = new float[returnLength];
		for(int i=0; i< returnLength;i++) {
			returnValue[i] = valuesFloat[i];
		}
		return returnValue;
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
	final float[] ret = new float[1];
    if( in.read(ret)) {    	
    	values[index] = ret[0];
    	return true;
    }
    return false;
}

}
