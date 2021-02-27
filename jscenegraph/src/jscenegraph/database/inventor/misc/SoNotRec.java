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


///////////////////////////////////////////////////////////////////////////////
///
////\class SoNotRec
///
///  Records one step in the path taken by notification. Instances of
///  this class are typically allocated on the stack as automatic
///  variables.
///
///  There is an SoBase pointer in the record that points to the base
///  (node, path, or engine) from which the notification is being
///  propagated. The contents of this pointer depend on the type of
///  the notification. The type also indicates the nature of the
///  notification. These are the possible types:
///
///      CONTAINER:  A field has changed value and is notifying its
///                  container node or engine. The base is the container.
///
///      PARENT:     A child of a group has changed and is notifying
///                  its parent. The base is the child.
///
///      SENSOR:     A node or path is notifying a sensor of a change.
///                  The base is the thing to which the sensor is attached.
///
///      FIELD:      A field in a node or engine is notifying the
///                  field to which it is connected. The base is the
///                  container of the source field.
///
///      ENGINE:     An engine is notifying a field to which it is
///                  connected. The base is the engine.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNotRec {

    //! Notification types (what receives notification). Note that
    //! these are also used for maintaining lists of auditors in
    //! SoBase instances. Each auditor uses one of these types to
    //! indicate how it is auditing the instance. This is then
    //! propagated to the auditors in the notification records.
	public enum Type {
		CONTAINER,	//!< Field notifying container
		PARENT,		//!< Child node notifying parent
		SENSOR,		//!< Some base notifying sensor
		FIELD, 		//!< Field notifying connected field
		ENGINE		//!< Engine notifying connected field
	}
	
	private SoBase base; // ptr
	private Type type;
	private SoNotRec previous; // ptr
	
	// Constructor - passed the base pointer. 
	public SoNotRec(SoBase b) {
		 base = b; 
	}
	
	public void destructor() {
		base = null;
		previous = null;
		type = null;
	}
	
	// Sets notification type. 
	public void setType(SoNotRec.Type t) {
		type = t;
	}
	
	// Returns base pointer, type, or previous record in list. 
	public SoBase getBase() {
		 return base; 
	}
	
	public SoNotRec.Type getType() {
		return type;
	}
	
	public SoNotRec getPrevious() {
		return previous;
	}
	
	// Sets previous record pointer. 
	public void setPrevious(SoNotRec prev) {
		 previous = prev; 
	}
}
