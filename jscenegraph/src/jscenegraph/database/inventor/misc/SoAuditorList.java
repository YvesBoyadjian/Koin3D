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
 |      This file contains the definition of the internal
 |      SoAuditorList class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc;

import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.sensors.SoDataSensor;

///////////////////////////////////////////////////////////////////////////////
///
////\class SoAuditorList
///
///  SoAuditorList class. This class maintains a list of instances that
///  audit (receive notification from) an SoBase or SoField. Each entry
///  in the list consists of a pointer to the auditor (base or field)
///  instance and a type code of type SoNotRec::Type.
///
///  The type of the auditor object pointer depends on the type code,
///  as follows:
///
///      Type code:      Auditor object pointer:
///
///      CONTAINER       The SoFieldContainer containing the field
///      PARENT          The parent node
///      SENSOR          The SoDataSensor instance
///      FIELD           The destination field instance
///      ENGINE          The destination field instance
///
///  Since there are two entries in the underlying SbPList for each
///  auditor, some of these methods have to do some fancy footwork.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoAuditorList extends SbPList {
	
	// Adds an auditor of the given type to the list. 
	public void append(Object auditor, SoNotRec.Type type) {
	     super.append(auditor);
	     super.append(type);
	    		
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets auditor and type for given index
//
// Use: public

public void
set(int index, Object auditor, SoNotRec.Type type)
//
////////////////////////////////////////////////////////////////////////
{
    // This is often called to set the first auditor in the list
    // (which is the container of fields and engine outputs, so handle
    // that case specially)
    if (super.getLength() == 0)
        append(auditor, type);

    else {
        SbPList list = this;
        list.operator_square_bracket(index * 2, auditor);
        list.operator_square_bracket(index * 2 + 1, type);
    }
}

	
	// Finds an auditor in the list, returning the index or -1 if not found. 
	public int find(Object auditor, SoNotRec.Type type) {
		
		  int i;
		   
		    for (i = 0; i < super.getLength(); i += 2)
		    if (this.operator_square_bracket(i) == auditor && this.operator_square_bracket(i+1) == type)
		    return i / 2;
		   
		    return -1;
		  
		  }
	
	
public int
findLast(Object auditor, SoNotRec.Type type)
{
  for (int i = super.getLength() - 2; i >= 0; i -= 2) {
    if (this.operator_square_bracket(i) == auditor && this.operator_square_bracket(i + 1) == type) {
      return i / 2;
    }
  }
  return -1;
}

	
	
	// Returns object or type for given index.	
	public Object getObject(int index) {
		return this.operator_square_bracket(index * 2);
	}
	
	public SoNotRec.Type getType(int index) {
		return (SoNotRec.Type) ( this.operator_square_bracket(index * 2 + 1));
	}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Removes an auditor from the list.
	   //
	   // Use: public
	   
	  public void
	   remove(int index)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
//	       if (index < 0 || index >= getLength()) {
//	           SoDebugError::post("SoAuditorList::remove",
//	                              "Index %d is out of range 0 - %d",
//	                              index, getLength() - 1);
//	           return;
//	       }
//	   #endif /* DEBUG */
	   
	       super.remove(index * 2 + 1);
	       super.remove(index * 2);
	   }
	  
    public void                remove(Object auditor, SoNotRec.Type type)
        { remove(find(auditor, type)); }
	  
	   	
	// Returns number of auditors in list. 
	public int getLength() {
		return super.getLength() / 2;
	}

	// Propagates notification to all auditors in list. 
	public void notify(SoNotList list) {
		
	    int numAuditors = getLength();

	    // No auditors? Do nothing.
	    if (numAuditors == 0)
	        ;

	    // If there's only 1 auditor just propagate to it. (This is the
	    // typical case, we assume.)
	    else if (numAuditors == 1)
	        notify1(list, 0);

	    // Otherwise, we have to do some extra work. We have to make a
	    // copy of the record list so we can propagate identical
	    // information to each auditor.
	    else {
	        final SoNotList workingList = new SoNotList(list);
	        int i;

	        for (i = 0; i < numAuditors; i++) {

	            // Copy given list to working list after first time -
	            // each notification may change stuff in the list
	            if (i > 0)
	                workingList.copy(list); // java port

	            notify1(workingList, i);
	        }
	        workingList.destructor();
	    }
	}
	//
	 // Description:
	 // Propagates notification to indexed auditor.
	 //
	 // Use: private
	 
	 void notify1(SoNotList list, int index)
	 //
	 {
	  SoNotRec.Type audType = getType(index);
	 
	  list.setLastType(audType);
	 
	  switch (audType) {
	 
	  // These 2 cases are notifying a base of some sort
	  case CONTAINER:
	  case PARENT:
	  ((SoBase ) getObject(index)).notify(list);
	  break;
	 
	  // This one is notifying a data sensor
	  case SENSOR:
	  ((SoDataSensor ) getObject(index)).notify(list);
	  break;
	 
	  // And these two are notifying a connected field
	  case FIELD:
	  case ENGINE:
	  ((SoField ) getObject(index)).notify(list);
	  //((SoField ) getObject(index)).evaluate(); // YB
	  break;
	  }
	 }
	}
