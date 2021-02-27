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

import java.util.HashMap;
import java.util.Map;


///////////////////////////////////////////////////////////////////////////////
///
////\class SbIntList
///
///  List of generic (void *) pointers. SbIntList allows random access,
///  insertion, and removal.
///
//////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public class SbIntList extends SbPList {
	
	// java port 
	private static final Map<Integer, Object> toVoidPtr = new HashMap<Integer, Object>(); 

	// java port 
	private static final Map<Object, Integer> fromVoidPtr = new HashMap<Object, Integer>(); 
	
	
	public SbIntList() {
		super();
	}

	public SbIntList(int initSize) {
		super(initSize);
	}
	
    //! Adds given pointer to end of list.
    public void        append(Object integer) {
    	append(((Integer)integer).intValue());
    }
    public void        append(int integer)
    {
    	addIntegerToMap(integer);
    	super.append(toVoidPtr.get(integer)); 
    }

    //! Returns index of given pointer in list, or -1 if not found.
    public int         find(Object integer) {
    	return find(((Integer)integer).intValue());
    }
    public int         find(int integer)
    { 
		addIntegerToMap(integer);
		return super.find(toVoidPtr.get(integer)); 
	}

    //! Inserts given pointer in list before pointer with given index.
    public void        insert(Object integer, int addBefore) {
    	insert(((Integer)integer).intValue(),addBefore);
    }
    public void        insert(int integer, int addBefore)
        { 
    		addIntegerToMap(integer);
    		
    		super.insert(toVoidPtr.get(integer), addBefore); 
    	}

    public Integer operator_square_bracket(int i)
        { 
    		return fromVoidPtr.get(super.operator_square_bracket(i)); 
    	}
    
	// java port
	public void operator_square_bracket(int i, Object object) {
		Integer integer = (Integer)object;
		addIntegerToMap(integer);		
		super.operator_square_bracket(i, toVoidPtr.get(integer));
	}    
    
	/**
	 * Java port
	 */
	public  void set(int i, Object object) {
		Integer integer = (Integer)object;
		addIntegerToMap(integer);		
		super.set(i, toVoidPtr.get(integer));
	}	

	
    /**
     * java port
     * 
     * @param integer
     */
    private void addIntegerToMap(int integer) {
    	if(!toVoidPtr.containsKey(integer)) {
    		Object value = new Object();
    		toVoidPtr.put(integer,value);
    		fromVoidPtr.put(value,integer);
    	}    	
    }
    
    /**
     * Java port
     * @return
     */
    public int[] getIntArrayPtr() {
    	int size = getLength();
    	int[] array = new int[size];
    	for(int i =0; i<size;i++) {
    		array[i] = operator_square_bracket(i);
    	}
    	return array;
    }
}
