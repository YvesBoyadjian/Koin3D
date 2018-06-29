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
 |      SoMFBool
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of boolean values.
/*!
\class SoMFBool
\ingroup Fields
A multiple-value field that contains any number of boolean values.


SoMFBools are written to file as one or more boolean values, which
are written as "0" (representing a false value), "1", "TRUE", or
"FALSE".


When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 0, FALSE, 1, TRUE ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

public class SoMFBool extends SoMField<Boolean> {

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#read1Value(jscenegraph.database.inventor.SoInput, int)
	 */
	@Override
	public boolean read1Value(SoInput in, int index) {
    // accept 0 or 1 for both binary and ascii
    // with integer fields)
    final int[] intValue = new int[1];
    if (in.read(intValue)) {
        values[index] = intValue[0]!=0;
        if (intValue[0] != 0 && intValue[0] != 1) {
            SoReadError.post(in, "Illegal value for SoMFBool: "+intValue+" "+
                              "(must be 0 or 1)");
            return false;
        }
        return true;
    }

    // binary doesn't use TRUE/FALSE strings
    if (in.isBinary())
        return false;

    // read TRUE/FALSE keyword
    final SbName n = new SbName();
    if (! in.read(n, true))
        return false;
    
    if (n.operator_equal_equal("TRUE")) {
        values[index] = true;
        return true;
    }

    if (n.operator_equal_equal("FALSE")) {
        values[index] = false;
        return true;
    }

    SoReadError.post(in, "Unknown value (\""+n.getString()+"\") for SoMFBool "+
                      "(must be TRUE or FALSE)");
    return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Boolean constructor() {
		return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected Boolean[] arrayConstructor(int length) {
		return new Boolean[length];
	}

}
