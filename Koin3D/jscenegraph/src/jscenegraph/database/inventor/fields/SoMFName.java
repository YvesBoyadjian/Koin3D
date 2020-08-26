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
 |      SoMFName
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.SbNameArray;


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of names.
/*!
\class SoMFName
\ingroup Fields
A multiple-valued field containing any number of names.  Names are
short series of characters generally used for labels or names, and are
stored in a special table designed to allow fast lookup and
comparison.  For most purposes, an SoMFString field is probably
more appropriate.


SoMFNames are written to file as one or more strings of
characters.  Names must begin with an underscore or alphabetic
character, and must consist entirely of underscores, alphabetic
characters, or numbers.
When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ Fred, Wilma, _Part_01, translationField ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFName extends SoMField<SbName,SbNameArray> {
	
//	public void set1Value(int index, SbName newValue)                            
//	   {                                                                             
//	       if (index >= getNum())                                                    
//	           makeRoom(index + 1);                                                  
//	       values[index] = newValue;                                                 
//	       valueChanged();                                                           
//	   }

	@Override
	protected SbName constructor() {
		return new SbName();
	}

	@Override
	protected SbNameArray arrayConstructor(int length) {
		return new SbNameArray(length);
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
    return in.read(values.getO(index));
}

@Override
public SbNameArray doGetValues(int start) {
	return values.plus(start);
}

}
