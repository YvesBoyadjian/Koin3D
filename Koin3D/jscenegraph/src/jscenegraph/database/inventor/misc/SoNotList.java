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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Definition of (internal) SoNotRec and SoNotList classes, which
 |      handle notification propagation through instances of classes
 |      derived from SoBase.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc;

import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Mutable;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoNotList
///
///  Holds a list of SoNotRec notification records. Points to the first
///  and last records in the list. The records are linked backwards by
///  "previous" pointers.
///
///  The list also points to the first notification record that
///  involves a node. This info is used to determine which node
///  actually notified sensors. There is also a pointer to the last
///  field that changed, which is needed by field-specific sensors.
///
//////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public class SoNotList implements Mutable {

	 private SoNotRec first, last; // ptr
	 private  SoNotRec firstAtNode; // ptr
	 private  SoField lastField; // ptr
	 private  long timeStamp;
	 
	 public SoNotList() {
		 first = last = firstAtNode = null;
		 lastField = null;
		 timeStamp = SoNode.getNextNodeId();
	 }
	 
	 // Copy constructor. 
	 public SoNotList(final SoNotList copyFrom) {
		 first = copyFrom.first;
		 last = copyFrom.last;
		 firstAtNode = copyFrom.firstAtNode;
		 lastField = copyFrom.lastField;
		 timeStamp = copyFrom.timeStamp;
	 }
	 
	 public void destructor() {
		 first = last = firstAtNode = null;
		 lastField = null;
	 }
	 
	 // Appends given non-field record to end of list. 
	 public void append(SoNotRec rec) {
		 
		 rec.setPrevious(last);
		   last = rec;
		   if (first == null)
		   first = rec;
		  
		   if (firstAtNode == null &&
		   rec.getBase() instanceof /*.isOfType(*/SoNode/*.getClassTypeId())*/)
		   firstAtNode = rec;
	}
	
	 // Appends given (container) field record to end of list. We assume the base in the record is a node. 
	 public void append(SoNotRec rec, SoField field) {
		 
		append(rec);
		   
		lastField = field;
		firstAtNode = rec;
	 }
	 
	 // Sets the type of the last (current) record in the list.
	 // Reset firstAtNode pointer if we're going through field-to-field or engine-to-field connections. 
	 public void setLastType(SoNotRec.Type t) {
		 
		  last.setType(t);
		    if (t == SoNotRec.Type.FIELD || t == SoNotRec.Type.ENGINE)
		    firstAtNode = null;
		   	 }
	 
	 public SoNotRec getLastRec() {
		return last;
	}
	 
	 /**
	  * Returns first record in list that has a node base in the current chain of 
	  * node-to-node notification. 
	  * This information is passed to sensor callbacks to indicate which node 
	  * initiated notification in the graph. 
	  * 
	  * @return
	  */
	 public SoNotRec getFirstRecAtNode() {
		 return firstAtNode;
	 }
	 
	 // Returns last field set by notification (or NULL if notification did not 
	 // originate at or propagate through a field) 
	 public SoField getLastField() {
		 return lastField;
	 }
	 
    //! Returns the time stamp so nodes can check if notification has
    //! already been handled
    public long            getTimeStamp()             { return timeStamp; }

	 

	 public void copy(final SoNotList copyFrom) {
		 first = copyFrom.first;
		 last = copyFrom.last;
		 firstAtNode = copyFrom.firstAtNode;
		 lastField = copyFrom.lastField;
		 timeStamp = copyFrom.timeStamp;
	 }

	@Override
	public void copyFrom(Object other) {
		SoNotList otherList = (SoNotList) other;
		copy(otherList);
	}
}
