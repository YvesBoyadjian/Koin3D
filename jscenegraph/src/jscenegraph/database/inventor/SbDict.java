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
 |      This file defines dictionaries. A dictionary maps some sort of
 |      unique key to a data pointer. Keys are size_t integers.
 |
 |   Classes:
 |      SbDictEntry, SbDict
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */


package jscenegraph.database.inventor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jscenegraph.port.Destroyable;

///////////////////////////////////////////////////////////////////////////////
///
////\class SbDict
///
///  This is a dictionary mapping (size_t) integer keys to (void *) data
///  pointers.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbDict implements Destroyable {
	
	public interface Method {
		
		void apply(Object key, Object value);
	}
	
	private Map<Object,Object> table = new HashMap<Object,Object>();
	
	// java port
	public SbDict() {
		this(251);
	}
	
	public SbDict(int entries) {
		table = new HashMap<Object,Object>(entries);
	}
	
	/**
	 * Calls given routine (passing value) for each entry in dictionary. 
	 * The order of entries is not guaranteed to mean anything. 
	 * 
	 * @param method
	 */
	public void applyToAll(Method method) {
		
	          // Call rtn for each entry in dict
		// java port
	          for ( Entry<Object,Object> entry : table.entrySet()) {
	        	  method.apply(entry.getKey(), entry.getValue());
	          }
	     	}
	
	// Removes all entries from dictionary. 
	public void clear() {
		table.clear();
	}
	
	// Enters a key,value pair into the dictionary. 
	// Overwrites entry and returns FALSE if key already exists. 
	public boolean enter(Object key, Object value) {
		// java port
		boolean containsKey = table.containsKey(key);		
		table.put(key, value);
		return !containsKey;
	}

	// Finds entry with given key, setting value to point to value. 
	// Returns FALSE if no such entry. 
	public boolean find(Object key, Object[] value) {
		
		// java port
		boolean containsKey = table.containsKey(key);
		if(containsKey) {
			value[0] = table.get(key);
			return true;
		}
		else {
			value[0] = null;
			return false;
		}
	}
	
	// Removes the entry with the given key. Returns FALSE if no such entry. 
	public boolean remove(Object key) {
		// java port
		boolean containsKey = table.containsKey(key);
		if(containsKey) {
			table.remove(key);
			return true;
		}
		else {
			return false;
		}
	}

	// java port : destructor
	@Override
	public void destructor() {
		clear();
		table = null;
	}
}
