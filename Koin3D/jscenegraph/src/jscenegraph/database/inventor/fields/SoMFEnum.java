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
 |      SoMFEnum
 |
 |   Macros:
 |
 |      SO_NODE_SET_MF_ENUM_TYPE(fieldName, enumType)
 |      SO_ENGINE_SET_MF_ENUM_TYPE(fieldName, enumType)
 |              These assign a specific type of enum to an MFEnum field.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.port.IntArray;
import jscenegraph.port.SbNameArray;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of enumerated type values.
/*!
\class SoMFEnum
\ingroup Fields
A multiple-value field that contains any number of enumerated type
values, stored as ints. Nodes that use this field class define
mnemonic names for values. These names should be used when setting or
testing the values of the field, even though the values are treated as
integers in the methods.


SoMFEnums are written to file as a set of
mnemonic enumerated type names.  These names differ among uses of this
field in various node classes.  See the reference pages for specific nodes
for the names.


When more than one value is present, all of the values are enclosed in
square brackets and separated by commas.

*/
////////////////////////////////////////////////////////////////////////////////

public class SoMFEnum extends SoMField<Integer,IntArray> {

  protected boolean                legalValuesSet; //!< TRUE if setEnums called
  protected int                 numEnums;       //!< Number of enumeration values
  protected int[]                 enumValues;    //!< Enumeration values
  protected SbNameArray              enumNames;     //!< Mnemonic names of values

	
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#read1Value(jscenegraph.database.inventor.SoInput, int)
	 */
	@Override
	public boolean read1Value(SoInput in, int idx) {
		  final SbName n = new SbName();
		  final int[] val = new int[1];

		  // Read mnemonic value as a character string identifier
		  if (!in.read(n, true)) {
		    // If we don't have any legal values for this field,
		    // give some slack and accept integer values.
		    if (this.legalValuesSet || !in.read(val)) {
		      SoReadError.post(in, "Couldn't read enumeration name");
		      return false;
		    }
		  }
		  else {
		    if (!this.findEnumValue(n, val)) {
		      // If we read an enum field written by an extension node,
		      // we won't have any defined enum values. This is indicated by
		      // this->legalValuesSet == FALSE. If this is the case, define
		      // any encountered enum values on the fly but keep legalValuesSet
		      // to FALSE in order not to fool built-in enum field to accept
		      // illegal values.
		      if (!this.legalValuesSet) {
		        int[] newvalues = new int[this.numEnums+1];
		        SbNameArray newnames = new SbNameArray(this.numEnums+1);
		        int i;
		        for (i = 0; i < this.numEnums; i++) {
		          newvalues[i] = this.enumValues[i];
		          newnames.setO(i, this.enumNames.getO(i));
		        }
		        newvalues[i] = i;
		        newnames.setO(i, n);
		        //delete[] this.enumValues; java port
		        //delete[] this.enumNames;
		        this.enumValues = newvalues;
		        this.enumNames = newnames;
		        this.numEnums += 1;
		        val[0] = i;
		      }
		      else {
		        SoReadError.post(in, "Unknown enumeration value \""+n.getString()+"\"");
		        return false;
		      }
		    }
		  }

		  assert(idx < this.maxNum);
		  this.values.set(idx, val[0]);
		  return true;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Integer constructor() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected IntArray arrayConstructor(int length) {
		return new IntArray(length);
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by nodes to set legal enum names and values
//
// Use: extender public

public void setEnums(int num, int[] vals, SbNameArray names)

//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (legalValuesSet == true) {
        SoDebugError.post("SoMFEnum::setEnums",
                           "setEnums called twice");
    }
//#endif

    legalValuesSet = true;
    numEnums = num;
    enumValues = vals;
    enumNames = names;
}

/*!
  Return in \a val the enumeration value which matches the given
  enumeration \a name.

  Returns \c TRUE if \a name is a valid enumeration string, otherwise
  \c FALSE.
*/
public boolean findEnumValue(final SbName name, final int[] val)
{
  // Look through names table for one that matches
  for (int i = 0; i < this.numEnums; i++) {
    if (name.operator_equal_equal(this.enumNames.getO(i))) {
      val[0] = this.enumValues[i];
      return true;
    }
  }
  return false;
}

}
