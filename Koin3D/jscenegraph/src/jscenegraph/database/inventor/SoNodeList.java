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

import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Maintains a list of pointers to nodes.
/*!
\class SoNodeList
\ingroup General
This subclass of SoBaseList holds lists of pointers to
SoNodes.  It updates reference counts to nodes in the list
whenever adding or removing pointers.

\par See Also
\par
SoNode
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodeList extends SoBaseList {
	
   public SoNodeList() { super();  }
   public SoNodeList(int size) { super(size); }
   public SoNodeList(final SoNodeList l) {     super(l); }
 	

	// Adds a pointer to the end of the list. 
	public void append(SoNode ptr) {
		 super.append((SoBase) ptr); 
	}
	
	public SoNode operator_square_bracket(int i) {
		return (SoNode) super.operator_square_bracket(i);
	}

    //! Copies a list, keeping all reference counts correct.
    public SoNodeList         operator_assign( SoNodeList l)
        { super.copy(l) ; return this; }

    //! Copies a list, keeping all reference counts correct.
    public void        copyFrom(Object other) // java port
        { final SoNodeList l = (SoNodeList)other; super.copy(l) ; }

}
