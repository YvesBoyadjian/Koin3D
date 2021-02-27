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
 |      This file contains the definition of the SoTempPath class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc;

import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.nodes.SoNode;



///////////////////////////////////////////////////////////////////////////////
///
////\class SoTempPath
///
///  This class is internal to Inventor. It allows "temporary" paths to
///  be created - these paths do not support the same notification
///  behavior as SoPaths, and are therefore faster to construct and
///  modify. They are used during traversal to maintain the current
///  path through a graph. Since such paths are short-lived, there is
///  no reason to pay the price of the auditor overhead.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTempPath extends SoFullPath {

	public SoTempPath(int approxLength) {
		super(approxLength);
	     auditPath(false);           // Don't want overhead of auditors
	     impl.nodes.addReferences(false); // Don't bother ref/unref nodes	     		
	}

	public SoTempPath(SoTempPath other) {
		this(other.getLength());
		impl.copyFrom(other.impl);
	}

	public void copyFrom(SoTempPath other) {
		impl.copyFrom(other.impl);
	}

	/*!
	  Append a node (specified by \a node and parent child \a index) to the path.
	  This method is only available in SoTempPath, since it will not
	  consider auditing or hidden children.
	*/
	public void
	simpleAppend(SoNode node, int index)
	{
	  // this will make SoPath rescan the path for hidden children the
	  // next time getLength() is called.
	  this.impl.firsthiddendirty = true;

	  // just append node and index
	  this.impl.nodes.append(node);
	  this.impl.indices.append(index);
	}

	/*!  
	  Replace the tail of this path. The node is specified by \a node
	  and parent child \a index. This method is only available in
	  SoTempPath,, since it will not consider auditing or hidden children.  
	*/
	public void 
	replaceTail(SoNode node, int index)
	{
	  // this will make SoPath rescan the path for hidden children the
	  // next time getLength() is called.
	  this.impl.firsthiddendirty = true;

	  // just replace the last node and index
	  int i = this.impl.nodes.getLength() - 1;
	  this.impl.nodes.set(i, (SoBase) node);
	  this.impl.indices.operator_square_bracket(i, index);
	}
}
