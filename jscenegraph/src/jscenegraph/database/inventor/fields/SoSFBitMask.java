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
 |      SoSFBitMask
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
import jscenegraph.port.SbNameArray;

////////////////////////////////////////////////////////////////////////////////
//! Single-value field containing a set of bit flags.
/*!
\class SoSFBitMask
\ingroup Fields
A single-value field that contains a mask of bit flags,
stored as an integer. Nodes that use this field class define mnemonic
names for the bit flags. These names should be used when setting or testing
the values of the field, even though the values are treated as
integers in the methods.


The bit-wise "&" and "|" operators should be used when testing and
setting flags in a mask.  For example, to turn on the sides of a 3D
text node and turn off the back you would write:
\code
text3->parts = text3->parts.getValue() | SoText3::SIDES;
text3->parts = text3->parts.getValue() & ~SoText3::BACK;
\endcode
SoSFBitMasks are written to file as one or more mnemonic enumerated type
names, in this format:
\code
( flag1 | flag2 | ... )
\endcode
If only one flag is used in a mask, the parentheses are optional.
These names differ among uses of this field in various node or engine
classes.  See their man pages for the names.


The field values may also be represented as integers, but this is not
guaranteed to be portable.

\par See Also
\par
SoField, SoSField, SoMFBitMask
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFBitMask extends SoSFEnum {

// Special characters when reading or writing value in ASCII
private static final char OPEN_PAREN      ='(';
private static final char CLOSE_PAREN     =')';
private static final char BITWISE_OR      ='|';


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error. See the comments
//    in writeValue for file format details.
//
// Use: private

public boolean
readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    final char[]        c = new char[1];
    final SbName      n = new SbName();
    final int[]         v = new int[1];

    value = 0;

    if (in.isBinary()) {

        // Read all non-empty strings
        while (in.read(n, true) && ! ( n.operator_not()) ) {
            if (findEnumValue(n, v))
                value |= v[0];

            else {
                SoReadError.post(in,
                                  "Unknown SoSFBitMask bit mask value \""+n.getString()+"\"");
                return false;
            }
        }
    }

    else {
        // Read first character
        if (! in.read(c))
            return false;

        // Check for parenthesized list of bitwise-or'ed flags
        if (c[0] == OPEN_PAREN) {

            // Read names separated by BITWISE_OR
            while (true) {
                if (in.read(n, true) && ! ( n.operator_not()) ) {

                    if (findEnumValue(n, v))
                        value |= v[0];

                    else {
                        SoReadError.post(in, "Unknown SoSFBitMask bit "+
                                          "mask value \""+n.getString()+"\"" );
                        return false;
                    }
                }

                if (! in.read(c)) {
                    SoReadError.post(in, "EOF reached before '"+CLOSE_PAREN+"' "+
                                      "in SoSFBitMask value" );
                    return false;
                }

                if (c[0] == CLOSE_PAREN)
                    break;

                else if (c[0] != BITWISE_OR) {
                    SoReadError.post(in, "Expected '"+BITWISE_OR+"' or '"+CLOSE_PAREN+"', got '"+c+"' "+
                                      "in SoSFBitMask value");
                    return false;
                }
            }
        }

        else {
            in.putBack(c[0]);

            // Read mnemonic value as a character string identifier
            if (! in.read(n, true))
                return false;

            if (! findEnumValue(n, /*value*/v)) { // java port
                SoReadError.post(in, "Unknown SoSFBitMask bit "+
                                  "mask value \""+n.getString()+"\"" );
                return false;
            }
            value = v[0]; //java port
        }
    }

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Looks up enum name, returns value. Returns FALSE if not found.
//    SFEnum method not used because need to allocate unknown enum
//    bits uniquely.
//
// Use: protected virtual

protected boolean
findEnumValue(final SbName name, final int[] val)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Look through names table for one that matches
    for (i = 0; i < numEnums; i++) {
        if (name.operator_equal_equal(enumNames.getO(i))) {
            val[0] = enumValues[i];
            return true;
        }
    }

    if (!legalValuesSet) {
        // Must be part of an unknown node, add name, value:
        int[] oldValues = enumValues;
        SbNameArray oldNames = enumNames;
        enumValues = new int[numEnums+1];
        enumNames = new SbNameArray(numEnums+1);
        if (numEnums != 0) {
            for (i = 0; i < numEnums; i++) {
                enumValues[i] = oldValues[i];
                enumNames.setO(i, oldNames.getO(i));
            }
            //delete[] oldValues;
            //delete[] oldNames;
        }
        val[0] = 1<<numEnums;
        enumValues[numEnums] = 1<<numEnums;
        enumNames.setO(numEnums, name);
        ++numEnums;
        return true;
    }

    return false;
}

}
