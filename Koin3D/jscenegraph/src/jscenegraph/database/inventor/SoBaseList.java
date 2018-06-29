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


///////////////////////////////////////////////////////////////////////////////
///
/// Subclasses of the SbPList class which hold lists of pointers of a
/// specific type.
///
/// Each contains:
///      A default constructor
///      A constructor taking an initial number of items in the list
///      An "append" function that adds a pointer to the end of the list
///      The index ([]) operator that returns the nth pointer in the list
///
//////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
//! Maintains a list of pointers to instances of the SoBase classes.
/*!
\class SoBaseList
\ingroup General
This subclass of SbPList holds lists of pointers to
instances of classes derived from SoBase (an abstract class). A
flag indicates whether adding an instance pointer to the list should
add a reference to the instance. If this flag is TRUE, then adding and
removing pointers from the list updates reference counts in the
corresponding instances.

\par See Also
\par
SoBase, SoNodeList, SoPathList
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoBaseList extends SbPList {
	
	private boolean addRefs;
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Default constructor.
	    //
	    // Use: public
	    
	   public SoBaseList()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
		   super();
	    
	        // By default, this adds references to things in the list when
	        // they are added
	        addRefs = true;
	    }
	    
	    ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Constructor that takes initial approximate size (number of items
	    //    in list).
	    //
	    // Use: public
	    
	    public SoBaseList(int size)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	    	super(size);
	    
	        // By default, this adds references to things in the list when
	        // they are added
	        addRefs = true;
	    }
	    
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor that takes another list to copy values from.
//
// Use: public

public SoBaseList(final SoBaseList l)
//
////////////////////////////////////////////////////////////////////////
{
    // By default, this adds references to things in the list when
    // they are added
    addRefs = true;

    copy(l);
}

	    
	    
	   // Adds a pointer to the end of the list. 
	public void append(SoBase ptr) {
		  super.append((Object) ptr);
		    if (addRefs && ptr != null)
		    ptr.ref();
		  		
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Insert given pointer into list before pointer with given index
//
// Use: public

public void
insert(SoBase ptr,         // pointer to insert
                   int addBefore)       // index to add before
//
////////////////////////////////////////////////////////////////////////
{
    if (addRefs && ptr != null)
        ptr.ref();
    super.insert((Object) ptr, addBefore);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Remove pointer with given index
//
// Use: public

public void
remove(int which)           // index of pointer to remove
//
////////////////////////////////////////////////////////////////////////
{
    if (addRefs && (this).operator_square_bracket(which) != null)
        (this).operator_square_bracket(which).unref();
    super.remove(which);
}

	
	
	// Removes all pointers after one with given index, inclusive. 
	public void truncate(int start) {
		
		  int i;
		   
		    if (addRefs) {
		    for ( i = start; i < getLength(); i++) {
		    if ( this.operator_square_bracket(i) != null ) {
		    this.operator_square_bracket(i).unref();
		    }
		    }
		    }
		   
		    super.truncate(start);
		  
		  }
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy a list, keeping all reference counts correct
//
// Use: public

public void
copy( SoBaseList bList)       // list to copy from
//
////////////////////////////////////////////////////////////////////////
{
    int         i;

    truncate(0);

    if (addRefs) {
        for (i = 0; i < bList.getLength(); i++) {
            if ( bList.operator_square_bracket(i) != null ) {
                bList.operator_square_bracket(i).ref();
            }
        }
    }

    super.copy(bList);
}

	

	public SoBase operator_square_bracket(int i) {
		return (SoBase) super.operator_square_bracket(i);
	}
	
	// Sets an element of a list. 
	public void set(int i, SoBase ptr) {
		
	     if (addRefs) {
	    	           if ( ptr != null) {
	    	               ptr.ref() ;
	    	           }
	    	           if ( (this).operator_square_bracket(i) != null) {
	    	               (this).operator_square_bracket(i).unref();
	    	           }
	    	       }
	    	       super.operator_square_bracket(i, (Object) ptr);
	    	  	}
	
	/**
	 * Indicates whether to call ref() and unref() for bases in the list 
	 * when adding/removing them. 
	 * The default value is TRUE. 
	 * 
	 * @param flag
	 */
	public void addReferences(boolean flag) {
		 addRefs = flag; 
	}
}
