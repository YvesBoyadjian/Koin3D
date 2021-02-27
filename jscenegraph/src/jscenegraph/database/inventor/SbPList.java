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
 |   $Revision: 1.3 $
 |
 |   Description:
 |      This contains the definition of the SbPList generic pointer
 |      list class. An SbPList is a list of (void *) pointers that
 |      allows easy insertion, removal, and other operations.
 |
 |      The SbIntList class allows lists of integers to be created.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.util.Objects;

import jscenegraph.port.Destroyable;
import jscenegraph.port.Mutable;

////////////////////////////////////////////////////////////////////////////////
//! List of generic (void *) pointers.
/*!
\class SbPList
\ingroup Basics
This class manages a dynamic list of generic <tt>void *</tt> pointers.
This class allows random access, insertion, and removal.

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbPList<E> implements Destroyable, Mutable {
	
	private static final int DEFAULT_INITIAL_SIZE = 4;
	
	private Object[] ptrs;
	private int nPtrs; 
	private int ptrsSize;
	
	public SbPList() {
		
	     ptrs  = null;
	          nPtrs = ptrsSize = 0;
	     
	          setSize(0);
	     	}
	
	/**
	 * initSize specifies an initial size for the list, 
	 * which is useful as an optimization if you can estimate 
	 * the length of the list before you construct it. 
	 * 
	 * @param initSize
	 */
	public SbPList(int initSize) {
		
	     ptrs  = null;
	          nPtrs = ptrsSize = 0;
	      
	          setSize(initSize);          // Makes enough room for initSize entries
	          setSize(0);                 // Sets nPtrs back to 0
	      	}
	
//
// Initialize one PList from another
//

public SbPList(final SbPList pl)
{
    int i;

    nPtrs = pl.nPtrs;
    ptrsSize = pl.ptrsSize;
    ptrs = new Object[ptrsSize];

    for (i = 0; i < nPtrs; i++)
        ptrs[i] = pl.ptrs[i];
}

	
	// Adds given pointer to end of list. 
	public void append(E ptr) {
		 if (nPtrs + 1 > ptrsSize) expand(nPtrs + 1);
		 ptrs[nPtrs] = ptr;
		 nPtrs++;
	}
	
	// Returns index of given pointer in list, or -1 if not found. 
	public int find(E ptr) {
		  int i;
		   
		    for (i = 0; i < nPtrs; i++)
		    if (Objects.equals(ptrs[i], ptr))
		    return(i);
		   
		    return -1; // Not found		  
	}
	
	// Inserts given pointer in list before pointer with given index. 
	public void insert(E ptr, int addBefore) {
		
	     int i;
	      
	          // If addBefore is off the end of the list, grow the list (and
	          // initialize any new elements to NULL)
	          if (addBefore > nPtrs) grow(addBefore);
	      
	          // Make room for one more
	          setSize(nPtrs + 1);
	      
	          // Move pointers forward to make room
	          for (i = nPtrs - 1; i > addBefore; --i)
	              ptrs[i] = ptrs[i - 1];
	      
	          // Insert the new one
	          ptrs[addBefore] = ptr;
	     	}
	
	// Removes pointer with given index. 
	public void remove(int which) {
		 int i;
		  
		   // Move all pointers after the ith one backward
		   for (i = which; i < nPtrs - 1; i++)
		   ptrs[i] = ptrs[i + 1];
		  
		   ptrs[nPtrs - 1] = null; //java port
		   // Shrink the list
		   setSize(nPtrs - 1);
		 		
	}

	// Returns number of pointers in list. 
	public int getLength() {
		 return nPtrs; 
	}
	
	// Removes all pointers after one with given index, inclusive.
	public void truncate(int start) {
		int previous_Ptrs = nPtrs;
		nPtrs = start;
		if (ptrs != null) {
			for (int i = start; i < previous_Ptrs; i++) {
				ptrs[i] = null; // java port
			}
		}
	}
	
	// Returns pointer with given index. 
	public E operator_square_bracket(int i) {
		 if (i >= nPtrs) grow(i); return (E)ptrs[i];
	}
	
	// java port
	public void operator_square_bracket(int i, E object) {
		 if (i >= nPtrs) grow(i);
		 ptrs[i] = object; 
	}
	
	// Internal versions of [] that do not check for bounds: 
	public E get(int i) {
		 return (E)ptrs[i];
	}
	
	//
	// Makes the list big enough for "newSize" elements, and initializes
	// any newly created elements to NULL.
	//
	private void grow(int max) {
		  int newSize = max+1;
		  int oldSize = nPtrs;
		   
//		  #ifdef DEBUG
//		  	if (newSize <= oldSize)
//		    SoDebugError::post("(internal) SbPList::grow", "newSize <= oldSize!");
//		  #endif /* DEBUG */
		   
		    // Get around the const thing:
		    SbPList me = (SbPList)this;
		   
		    me.setSize(newSize);
		   
		    for (int i = oldSize; i < newSize; i++) {
		    me.ptrs[i] = null;
		    }		  
	}
	
	private void setSize(int size) {
		 for( int i = size; i < nPtrs; i++) { // java port
			 ptrs[i] = null;
		 }
		 if (size > ptrsSize) expand(size); nPtrs = size; 
	}
	
	 //
	 // Changes size of list to be new size. If the new size is larger than
	 // the old one, it allocates more space.
	 //	
	private void expand(int size) {
		final Object[] newPtrs;
		   int i;
		  
		   if (ptrsSize == 0)
		   ptrsSize = DEFAULT_INITIAL_SIZE;
		  
		   while (size > ptrsSize) {
//		  #ifdef DEBUG
//		   // check for overflow
//		   int oldPtrsSize = ptrsSize;
//		   ptrsSize *= 2;
//		   if (ptrsSize < oldPtrsSize)
//		   SoDebugError::post("SbPList::expand",
//		   "Attempt to expand list beyond capacity;\n"
//		   "A core dump is likely");
//		  #else
		   ptrsSize *= 2;
//		  #endif
		   }
		  
		   newPtrs = new Object[ptrsSize];
		  
		   if (ptrs != null) {
		   for (i = 0; i < nPtrs; i++)
		   newPtrs[i] = ptrs[i];
		   //delete [] ptrs;
		   }
		  
		   ptrs = newPtrs;
		 
	}
	
	public  void set(int i, E j) { ptrs[i] = j; }

	// java port : destructor
	@Override
	public void destructor() {
		truncate(0);
		ptrs = null;
	}
	
    //! Assignment operator; copies list into this list.
	public SbPList operator_copy(final SbPList pl)   { copy(pl); return this; }
 	
    //! Assignment operator; copies list into this list.
    public SbPList operator_equal(final SbPList pl)   { copy(pl); return this; }

	
	 //
	   // Copies a pointer list
	   //
	   
	  public void
	   copy(final SbPList pl)
	   {
	       int i;
	   
	       setSize(pl.nPtrs);
	   
	       for (i = 0; i < nPtrs; i++)
	           ptrs[i] = pl.ptrs[i];
	   }

	@Override
	public void copyFrom(Object other) {
		SbPList otherList = (SbPList)other;
		copy(otherList);
	}
	
	public Object[] 
	getArrayPtr( /*int start*/)  
	{
	//#ifdef COIN_EXTRA_DEBUG
	  //assert(start >= 0 && start < nPtrs/*this.numitems*/);
	//#endif // COIN_EXTRA_DEBUG
	  return /*this.itembuffer*/ptrs;
	}

	   }
