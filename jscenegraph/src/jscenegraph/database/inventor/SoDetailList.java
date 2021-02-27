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

import jscenegraph.database.inventor.details.SoDetail;


////////////////////////////////////////////////////////////////////////////////
//! Maintains a list of instances of details.
/*!
\class SoDetailList
\ingroup Details
This subclass of SbPList holds lists of instances of
classes derived from SoDetail.

\par See Also
\par
SoDetail
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDetailList extends SbPList<SoDetail> {

  public
    SoDetailList() {                     super();     }
	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy constructor.
//
// Use: public

public SoDetailList(final SoDetailList l) {
	super(l);


    // We need to copy the details, since we delete them when we
    // truncate the list
    for (int i = 0; i < getLength(); i++)
        operator_square_bracket(i, ((SoDetail)operator_square_bracket(i)).copy());
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Delete all details on the list from one with given index on,
//    inclusive.
//
// Use: public

public void
truncate(int start)
//
////////////////////////////////////////////////////////////////////////
{
    int         i;

    for ( i = start; i < getLength(); i++) {
        if ( (this).operator_square_bracket(i) != null) {
            ((SoDetail)(this).operator_square_bracket(i)).destructor();
        }
    }

    super.truncate(start);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies list, making copy instances of each detail in list.
//
// Use: public

public void
copy( SoDetailList l)
//
////////////////////////////////////////////////////////////////////////
{
    truncate(0);

    int num = l.getLength();
    for (int i = 0; i < num; i ++) {
        SoDetail detail = (SoDetail)l.operator_square_bracket(i);
        if (detail != null)
            append(detail.copy());
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets an element of a list, deleting old entry.
//
// Use: public

public void
set(int i, SoDetail detail)
//
////////////////////////////////////////////////////////////////////////
{
    if ((this).operator_square_bracket(i) != null)
        (this).operator_square_bracket(i).destructor();

    ( this).operator_square_bracket(i, detail);
}

    //! Access an element of a list
    public SoDetail           operator_square_bracket(int i)
        { return  (SoDetail ) super.operator_square_bracket(i) ; }

	
}
