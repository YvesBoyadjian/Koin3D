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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file contains the definition of the SoPath and SoFullPath
 |      classes, and the related class SoLightPath
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Alan Norton
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoFullPath
///
///  This SoEXTENDER class allows expert users to examine "hidden"
///  children in paths. SoPath allows access from the head node down to
///  the first node with hidden children, but no further. Casting an
///  SoPath to an SoFullPath (which is always guaranteed to be safe)
///  allows you to get at hidden children. SoFullPath overrides some of
///  the methods defined on paths to make this possible.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFullPath extends SoPath {
	
	SoPath castedPath;
	
	  protected SoFullPath(int approxLength) {
		  super(approxLength);		  
	  }
		    
	private SoFullPath(SoPath path) {
		super(path);
	}
	
	/**
	 * Java port
	 * @param path
	 * @return
	 */
	public static SoFullPath cast(SoPath path) {
		if(path == null) {
			return null;
		}
		return new SoFullPath(path);
	}

	// These return the first and last nodes in a path chain. 
	public SoNode getTail() {
		 return (impl.nodes.operator_square_bracket(getFullLength() - 1)); 
	}
	
	// java port
	public static final SoNode getTail(SoPath path) {
		return (path.impl.nodes.operator_square_bracket(path.getFullLength() - 1));
	}
	
	/**
	 * Override methods that normally stop at last public child:
	 * These allow a path to be treated as a stack; 
	 * they push a node at the end of the chain and pop the last node off. 
	 */
	public void pop() {
		 truncate(getFullLength() - 1);
	}
	
    //! Returns a pointer to the \p i'th node. Passing 0 for \p i returns the tail node.
    public SoNode             getNodeFromTail(int i) 
        { return (impl.nodes.operator_square_bracket(getFullLength() - 1 - i)); }

    //! Returns the index of the
    //! \p i'th node (within its parent) in the chain, counting backward from
    //! the tail node. Passing 0 for \p i returns the tail node's index.
    public int                 getIndexFromTail(int i) 
        { return (impl.indices.operator_square_bracket(getFullLength() - 1 - i)); }

	public int getLength() {
		 return getFullLength(); 
	}
}
