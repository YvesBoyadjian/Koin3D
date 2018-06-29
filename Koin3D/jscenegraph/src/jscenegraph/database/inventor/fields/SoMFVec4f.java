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
import jscenegraph.port.Util;

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

public class SoMFVec4f extends SoMField<SbVec4f> {

	@Override
	public boolean read1Value(SoInput in, int index) {
		DoubleConsumer[] ref = getValues(0)[index].getRef();
		return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]) && in.read(ref[3]));
	}

	@Override
	protected SbVec4f constructor() {
		return new SbVec4f();
	}

	@Override
	protected SbVec4f[] arrayConstructor(int length) {
		return new SbVec4f[length];
	}

	/* Get pointer into array of values 
	 * 
	 * Faster method
	 * 
	 * */
	public Array<SbVec4f> getValuesArray(int start) {
		evaluate();
				
		Array<SbVec4f> shiftedValues = new Array<SbVec4f>(SbVec4f.class, start, values);
		return shiftedValues;
	}

	// java port
	public ByteBuffer getValuesBytes(int start) {
		SbVec4f[] values = getValues(start);
		return Util.toByteBuffer(values);
	}

}
