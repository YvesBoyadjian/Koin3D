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
 |   Description:
 |      This file contains the definitions of subclasses of SbPList for
 |      some of the specific Inventor pointer types so that lists of
 |      pointers can be created easily.
 |
 |   Classes:
 |      subclasses of SbPList:
 |              SoBaseList
 |              SoNodeList
 |              SoPathList
 |              SoEngineList
 |              SoTypeList
 |              SoDetailList
 |              SoPickedPointList
 |              SoFieldList
 |              SoEngineOutputList
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, David Mott,
 |                        Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

////////////////////////////////////////////////////////////////////////////////
//! Maintains a list of SoTypes.
/*!
\class SoTypeList
\ingroup General
This subclass of SbPList holds lists of SoType type identifiers.

\par See Also
\par
SoType
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTypeList extends SbPList {
	
    public SoTypeList()                        { super();     }
    public SoTypeList(int size)                { super(size); }
    public SoTypeList(final SoTypeList l)      { super(l);    }
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Add a typeId to the end of the list
//
// Use: public

public void
append(SoType typeId)       // typeId to append
//
////////////////////////////////////////////////////////////////////////
{
	SoType localType = new SoType(typeId);
    // we have to do some hackage to cast an SoType into a void *...
    Object hackage = localType.toVoidPtr();
    super.append(hackage);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns index of given typeId in list, or -1 if not found.
//
// Use: public

public int
find(SoType typeId)
//
////////////////////////////////////////////////////////////////////////
{
    // we have to do some hackage to cast an SoType into a void *...
    Object hackage = typeId.toVoidPtr();
    return super.find(hackage);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Insert given typeId into list before typeId with given index
//
// Use: public

public void
insert(SoType typeId,       // typeId to insert
                   int addBefore)       // index to add before
//
////////////////////////////////////////////////////////////////////////
{
    // we have to do some hackage to cast an SoType into a void *...
    Object hackage = typeId.toVoidPtr();
    super.insert(hackage, addBefore);
}



	// Access an element of a list. 
	public SoType operator_square_bracket(int i) {
	     // we have to do some hackage to cast our void * back to an SoType...
		       Object hackage = super.operator_square_bracket(i);
		       return SoType.fromVoidPtr(hackage);
		  		
	}
	
	// Sets an element of a list. 
	public void set(int i, SoType typeId) {
		
	     // we have to do some hackage to cast an SoType into a void *...
		       Object hackage = typeId.toVoidPtr();
		       super.operator_square_bracket(i, hackage);
		   	}

	// java port : destructor
	@Override
	public void destructor() {
		truncate(0);
		super.destructor();
	}
}
