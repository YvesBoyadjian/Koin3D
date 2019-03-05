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
 |      This file contains definitions of the SbString and SbName
 |      classes, which are useful variations on our friend, the
 |      character string.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.util.HashMap;
import java.util.Map;

import jscenegraph.port.Destroyable;
import jscenegraph.port.Mutable;

////////////////////////////////////////////////////////////////////////////////
//! Character string stored in a hash table.
/*!
\class SbName
\ingroup Basics
This class of strings stores the string in a hash table. It is
used by the Inventor toolkit for keywords and other unique names.
It is not recommended for general use (only in the context of Inventor
objects). When a string is stored in this table, a pointer to the
storage is returned. Two identical strings will return the same pointer.
This means that comparison of two SbNames
for equality can be accomplished by comparing their identifiers.
SbNames
are used for strings which are expected to show up frequently, such as
node names.

\par See Also
\par
SbString
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbName implements Mutable, Destroyable {
	
	// java port
	private static final Map<String,String> entries = new HashMap<String,String>();
	
	private String entry;
	
	public SbName() {
		entry = insert("");
	}
	
	public SbName(String s) {
		entry = insert(s);
	}
	
	public SbName(SbName n) {
		entry = n.entry;
	}
	
	private String insert(String s) {
		
		// java port
		if(s == null) {
			throw new IllegalArgumentException("SbName: String must not be null");
		}
		
		String entry = entries.get(s);
		if(entry != null) {
			return entry;
		}
		entries.put(s, s);
		return s;
	}
	
	public String getString() {
		return entry;
	}
	
	// Returns length of string. 
	public int getLength() {
		return entry.length();
	}
	
	// Unary "not" operator; returns TRUE if string is empty ("").
	// java port
	public boolean operator_not() {
		 return entry.isEmpty(); 
	}
	
	public boolean operator_equal_equal(String other) {
		return operator_equal_equal(new SbName(other));
	}
	public boolean operator_equal_equal(SbName other) {
		return operator_equals(other);
	}
	// Equality operator
	// java port
	public boolean operator_equals(SbName other) {
		return entry == other.entry;
	}
	
	public boolean operator_not_equal(SbName other) {
		return entry != other.entry;
	}
	
	public boolean operator_not_equal(String str) {
		return entry != entries.get(str);
	}
	
	/**
	 * java port
	 * source :
	 * 
	 * http://claude-astuces-it.blogspot.fr/2012/06/portage-cjava-isalnumvs-isletterordigit.html
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isalnum(char c) {
		return Character.isLetterOrDigit( c ) && (c < 128);
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if given character is legal starting character for
//    an identifier.
//
// Use: static, public

public static boolean isIdentStartChar(char c)
//
////////////////////////////////////////////////////////////////////////
{
    // Digits are illegal as first character:
    if (Character.isDigit(c)) return false;

    return isIdentChar(c);
}

	
	/**
	 * Returns TRUE if given character is a legal nonstarting 
	 * character for an identifier. 
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isIdentChar(char c) {
		
	     // Only 0-9, a-z, A-Z and _ are legal:
		       if (isalnum(c) || c == '_') return true;
		   
		       return false;
		  	}
	
	/**
	 * Returns TRUE if given character is a legal starting character 
	 * for an SoBase's name 
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isBaseNameStartChar(char c) {
		
	     // Digits are illegal as first character:
		       if (Character.isDigit(c)) return false; // java port
		   
		       return isIdentChar(c);
		  	}
	
	 //
	 // Characters that are illegal in identifiers:
	 //   . + ' " \ { }
	 static final String badCharacters = ".+\'\"\\{}";
	   
	 // java port
	 private static String strchr(String str, char c) {
		 int index = str.indexOf(c);
		 if(index == -1) {
			 return null;
		 }
		 return str.substring(index);
	 }
	 
	 // java port
	 public static boolean isspace(char c) {
		 return Character.isWhitespace(c);
	 }
	 
	 // java port
	 private static boolean iscntrl(char c) {
		 return Character.isISOControl(c);
	 }
	 
	 /**
	 * Returns TRUE if given character is a legal nonstarting character for an SoBase's name 
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isBaseNameChar(char c) {
		
	     // First, quick check for common case:
		       if (isalnum(c)) return true;
		   
		       // Now, look for bad characters:
		       if ((strchr(badCharacters, c) != null) ||
		           isspace(c) || iscntrl(c)) return false;
		   
		       // Anything left must be OK
		       return true;
		  	}
	
	@Override
	public void copyFrom(Object other) {
		if(other instanceof String) {
			other = new SbName((String)other);
		}
		SbName sbName = (SbName)other;
		entry = sbName.entry;
	}

	@Override
	public void destructor() {
		entry = null;
	}
	
	/**
	 * Java port
	 */
	public String toString() {
		return getString();
	}
	
	/*!
	  Returns an empty-string SbName instance.

	  \since Coin 3.0
	*/
	  static SbName emptyname = null;


	public static SbName 
	empty() // static
	{
	  if (emptyname == null) {
	    emptyname = new SbName("");
	    //coin_atexit(SbName_atexit, CC_ATEXIT_SBNAME);
	  }
	  return emptyname;
	}
	
}
