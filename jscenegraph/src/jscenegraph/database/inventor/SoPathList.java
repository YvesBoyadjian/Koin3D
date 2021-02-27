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

import java.util.Arrays;
import java.util.Comparator;

import jscenegraph.database.inventor.misc.SoBase;


////////////////////////////////////////////////////////////////////////////////
//! Maintains a list of pointers to paths.
/*!
\class SoPathList
\ingroup General
This subclass of SoBaseList holds lists of pointers to
SoPaths.  It updates reference counts to paths in the list
whenever adding or removing pointers.

\par See Also
\par
SoPath
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPathList extends SoBaseList<SoPath> {
	
	public     SoPathList() {super(); }
  public  SoPathList(int size) { super(size); }
	
  public  SoPathList(final SoPathList l) {
	  super(l);
  }
	
    //! Adds a path to the end of the list.
    public void                append(SoPath ptr)
        { ((SoBaseList ) this).append((SoBase ) ptr); }

    //! Copies a list, keeping all reference counts correct.
    public SoPathList         operator_assign(final SoPathList l)
        { super.copy(l) ; return this; }


  public  SoPath operator_square_bracket(int i)
          { return (SoPath ) super.operator_square_bracket(i) ; }


  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Looks in the SoPathList for a path that matches a given one, using the
// == operator.  If it is found, return that index, otherwise retun -1
//
// Use: public

public int
findPath(final SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    int         i;
    SoPath      testPath;

    for(i = 0; i < getLength(); i++) {
        testPath = (this).operator_square_bracket(i);
        if (testPath.operator_equals(path))
            return i;
    }
    return -1;  // not found
}

  
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sorts path list in place based on (1) increasing address of head
//    node, then (2) increasing indices of children.
//
// Use: public

public void
sort()
//
////////////////////////////////////////////////////////////////////////
{
    SoPath[]      paths = new SoPath [getLength()];
    int         i;

    // Use qsort to do the work
    for (i = 0; i < getLength(); i++) {
        paths[i] = (this).operator_square_bracket(i);
        paths[i].ref();
    }

    qsort(paths, new Comparator<Object>() {

		@Override
		public int compare(Object o1, Object o2) {
			return comparePaths(o1,o2);
		}
    	//;	
    });

    // Move the paths back into this list
    for (i = 0; i < getLength(); i++)
        set(i, paths[i]);

    // Get rid of the array
    for (i = 0; i < getLength(); i++)
        paths[i].unref();
    //delete [] paths; java port
}

private void qsort(SoPath[] paths, Comparator<Object> comparator) {
	Arrays.sort(paths, comparator);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Comparison method for path list sorting.
//
// Use: private

private int
comparePaths(final Object p1Ptr, final Object p2Ptr)
//
////////////////////////////////////////////////////////////////////////
{
    final SoPath        p1, p2;

    p1 =  ( SoPath ) p1Ptr;
    p2 =  ( SoPath ) p2Ptr;

    // Most likely, the head nodes will be the same, so test this first
    if (p1.getHead() == p2.getHead()) {

        // Test indices in order. A missing child comes before an
        // existing child

        int     depth;
        for (depth = 1; depth < p1.getLength(); depth++) {
            if (depth >= p2.getLength())
                return 1;
            if (p1.getIndex(depth) < p2.getIndex(depth))
                return -1;
            if (p1.getIndex(depth) > p2.getIndex(depth))
                return 1;
        }

        // If we get here, then the paths are the same up to the end
        // of path 1. If path2 is longer, then it comes after
        if (p2.getLength() > p1.getLength())
            return -1;

        // Exact same paths
        return 0;
    }

    else if (p1.getHead().getAddress() < p2.getHead().getAddress())
        return -1;
    else
        return  1;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a sorted list, removes any path that (1) is a duplicate,
//    or (2) goes through a node that is the tail of another path.
//
// Use: public

public void
uniquify()
//
////////////////////////////////////////////////////////////////////////
{
    int                 i, lastSame;
     SoPath        p1;
     SoPath        p2;

    // Remove duplicates from the end to minimize array shuffling
    for (i = getLength() - 2; i >= 0; i--) {

        // Use the SoPath::findFork() method to determine the last
        // node that is on a common chain for both paths. Since the
        // paths are sorted, we can just check if this node is at the
        // end of the first path. If it is, the second one is a
        // duplicate and can be removed.

        p1 = (this).operator_square_bracket(i);
        p2 = (this).operator_square_bracket(i + 1);

        lastSame = p1.findFork(p2);

        if (lastSame == p1.getLength() - 1)
            remove(i + 1);
    }
}


}
  
 
