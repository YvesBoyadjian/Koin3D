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
 |      SoSFBool
 |
 |   Author(s)          : Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;

////////////////////////////////////////////////////////////////////////////////
//! Field containing a single boolean value.
/*!
\class SoSFBool
\ingroup Fields
A field containing a single boolean (true or false) value.


SoSFBools may be written to file as "0" (representing FALSE), "1",
"TRUE", or "FALSE".

\par See Also
\par
SoField, SoSField, SoMFBool
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFBool extends SoSField<Boolean> {

	@Override
	protected Boolean constructor() {		
		return false;
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error.
//
// Use: private

public boolean readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    // accept 0 or 1 for both binary and ascii
    // with integer fields)
    final int[] intValue = new int[1];
    if (in.read(intValue)) {
        value = intValue[0]!=0;
        if (intValue[0] != 0 && intValue[0] != 1) {
            SoReadError.post(in, "Illegal value for SoSFBool: "+intValue+" "+
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
    if (!in.read(n, true))
        return false;
    
    if (n.operator_equals(new SbName("TRUE"))) {
        value = true;
        return true;
    }

    if (n.operator_equals(new SbName("FALSE"))) {
        value = false;
        return true;
    }

    SoReadError.post(in, "Unknown value (\""+n.getString()+"\") for SoSFBool "+"(must be TRUE or FALSE)");
    return false;
}

	
}
