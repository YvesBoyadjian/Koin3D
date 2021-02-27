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
 |      SoMFString
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.StringArray;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of strings.
/*!
\class SoMFString
\ingroup Fields
A multiple-value field that contains any number of strings.


SoMFStrings are written to file as one or more strings within
double quotes.  Any characters (including newlines) may appear within
the quotes. To include a double quote character within the string,
precede it with a backslash.  For example:
\code
[ cowEnizer , "Scene Boy" , "He said, \"I did not!\"" ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFString extends SoMField<String,StringArray> {

	@Override
	protected String constructor() {
		return "";
	}

	@Override
	protected StringArray arrayConstructor(int length) {
		return new StringArray(length);
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
	final String[] ret = new String[1];
    if (in.read(ret)) {
    	values.setO(index, ret[0]);
    	return true;
    }
    return false;
}

	public void setValues(int start, String[] values) {
		int length = values.length;
		for( int i=0; i<length;i++) {
			this.values.setO(i+start, values[i]);
		}
	}
}
