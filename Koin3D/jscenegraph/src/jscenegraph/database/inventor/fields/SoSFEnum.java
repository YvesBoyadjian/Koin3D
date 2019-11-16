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
 |      SoSFEnum
 |
 |   Macros:
 |
 |      SO_NODE_SET_SF_ENUM_TYPE(fieldName, enumType)
 |      SO_ENGINE_SET_SF_ENUM_TYPE(fieldName, enumType)
 |              These assign a specific type of enum to an SFEnum field.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.lang.reflect.InvocationTargetException;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;


////////////////////////////////////////////////////////////////////////////////
//! Field containing an enumerated value.
/*!
\class SoSFEnum
\ingroup Fields
A single-value field that contains an enumerated type
value, stored as an integer. Nodes that use this field class define
mnemonic names for the values. These names should be used when setting
or testing the values of the field, even though the values are treated
as integers in the methods.


SoSFEnums are written to file as a 
mnemonic enumerated type name.  The name differs among uses of this
field in various node or engine classes.  See the man pages for
specific nodes or engines
for the names (e.g. SoDrawStyle).

\par See Also
\par
SoField, SoSField, SoMFEnum
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFEnum extends SoSField<Integer> {
	
	   protected
		        boolean              legalValuesSet; //!< TRUE if setEnums called
	   protected int                 numEnums;       //!< Number of enumeration values
	   protected int[]                 enumValues;    //!< Enumeration values
	   protected SbName[]              enumNames;     //!< Mnemonic names of values
		   		   	
	   // java port
	public void setValue(Enum val) {
		Integer intVal;
		try {
			intVal = (Integer) val.getClass().getMethod("getValue", null).invoke(val, null);
			super.setValue(intVal);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected Integer constructor() {		
		return new Integer(0);
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value from given SbName, which must contain the name of a
//    valid enumerator.
//
// Use: public

	// java port
	public void
	setValue( String name) {
		setValue(new SbName(name));
	}
public void
setValue( SbName name)
//
////////////////////////////////////////////////////////////////////////
{
    final int[] enumVal = new int[1];

    if (findEnumValue(name, enumVal))
        setValue(enumVal[0]);

//#ifdef DEBUG
    else
        SoDebugError.post("SoSFEnum::setValue",
                           "No enum for name \""+name.getString()+"\"");
//#endif /* DEBUG */
}

	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Called by nodes to set legal enum names and values
	   //
	   // Use: extender public
	   
	  public void
	   setEnums(int num, int[] vals, SbName[] names)
	   
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	   //#ifdef DEBUG
	       if (legalValuesSet == true) {
	           SoDebugError.post("SoSFEnum::setEnums",
	                              "setEnums called twice");
	       }
	   //#endif
	   
	       legalValuesSet = true;
	       numEnums = num;
	       enumValues = vals;
	       enumNames = names;
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Looks up enum name, returns value. Returns FALSE if not found.
//
// Use: protected

protected boolean
findEnumValue(final SbName name, final int[] val)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Look through names table for one that matches
    for (i = 0; i < numEnums; i++) {
        if (name.operator_equal_equal(enumNames[i])) {
            val[0] = enumValues[i];
            return true;
        }
    }

    if (!legalValuesSet) {
        // Must be part of an unknown node, add name, value:
        int[] oldValues = enumValues;
        SbName[] oldNames = enumNames;
        enumValues = new int[numEnums+1];
        enumNames = new SbName[numEnums+1];
        if (numEnums != 0) {
            for (i = 0; i < numEnums; i++) {
                enumValues[i] = oldValues[i];
                enumNames[i] = oldNames[i];
            }
            //delete[] oldValues;
            //delete[] oldNames;
        }
        val[0] = numEnums;
        enumValues[numEnums] = numEnums;
        enumNames[numEnums] = name;
        ++numEnums;
        return true;
    }

    return false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Looks up enum value, returns pointer to name. Returns FALSE if
//    not found.
//
// Use: protected

protected boolean
findEnumName(int val, final SbName[] name) 
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Find string corresponding to given value
    for (i = 0; i < numEnums; i++) {
        if (val == enumValues[i]) {
            name[0] = enumNames[i];
            return true;
        }
    }

    return false;
}

	  
	   	
    // java port (no)
//	public void copyFrom(final SoField f) {
//		super.copyFrom(f);
//		SoSFEnum ef = (SoSFEnum)f;
//        legalValuesSet = ef.legalValuesSet; //!< TRUE if setEnums called
//        numEnums = ef.numEnums;       //!< Number of enumeration values
//        enumValues = ef.enumValues;    //!< Enumeration values
//        enumNames = ef.enumNames;     //!< Mnemonic names of values
//	}	  


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
    final SbName      n = new SbName();

    // Read mnemonic value as a character string identifier
    if (! in.read(n, true))
        return false;

    final int[] val = new int[1];
    if (findEnumValue(n, val)) { //java port
    	value = val[0];
        return true;
    }

    // Not found? Too bad
    SoReadError.post(in, "Unknown SoSFEnum enumeration value \""+n.getString()+"\"");
    return false;
}

}
