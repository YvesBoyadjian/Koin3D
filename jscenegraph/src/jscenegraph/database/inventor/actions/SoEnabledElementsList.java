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
 |      Defines the SoAction class and related classes.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.errors.SoDebugError;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoEnabledElementsList
///
///  Internal class.  A list of elements enabled for a given action
///  class. Lists of elements can be merged, allowing an action class
///  to inherit enabled elements from its parent class.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoEnabledElementsList {
	
	   
	private        static int          counter;
		    
	private        int                 setUpCounter;
		    
	private	        final SoTypeList          elements = new SoTypeList();
		    
	private	        SoEnabledElementsList       parent;
	
	public  SoEnabledElementsList(
			           SoEnabledElementsList parentList)
			   //
			   {
			       setUpCounter = -1;
			       parent = parentList;
			   }
			   
		   
	/**
	 * Returns list of elements. 
	 * This automatically merges the elements with elements 
	 * enabled in the parentList. 
	 * 
	 * @return
	 */
	//
	   // Description:
	   //   Enables all elements from the given list that are not already
	   //   enabled in this one.
	   //
	   // Use: internal
	   
	public SoTypeList getElements() {
	     // Cast const away
		       SoEnabledElementsList This = (SoEnabledElementsList )this;
		   
		       // We only need to merge with our parent's list if some element
		       // has been enabled since the last time we merged.
		       if (setUpCounter != counter) {
		   
		           // We may enable new things here which could increment the 
		           // enabled elements counter. But all of these elements were already
		           // enabled once by the parent class. So we'll store the counter 
		           // now and restore it after this loop...
		           This.setUpCounter = counter;
		   
		           SoEnabledElementsList parentList = parent;
		           while (parentList != null) {
		               int         i;
		               final SoType      t = new SoType();
		   
		               for (i = 0; i < parentList.elements.getLength(); i++) {
		                   t.copyFrom(parentList.elements.operator_square_bracket(i));
		                   if (! t.isBad())
		                           This.enable(t, i);
		               }
		   
		               parentList = parentList.parent;
		           }
		   
		           // restore the counter...
		           counter = This.setUpCounter;
		       }
		   
		       return elements;
		  
	}
	
	// Adds an element to the list if it's not already in it. 
	public void enable(SoType elementType, int stackIndex) {
	     final SoType prev = new SoType(elements.operator_square_bracket(stackIndex));
	      
	          // If not enabled before or if enabled before but we are now
	          // enabling a more-specific subclass, add the element.
	          if (prev.isBad() || 
	              (elementType.operator_not_equal(prev) && elementType.isDerivedFrom(prev))) {
	              elements.set(stackIndex, elementType);
	      
	              // Increment global counter to indicate that lists have changed
	              counter++;
	          }
	      
//	      #ifdef DEBUG
	          // If we aren't enabling a more general super-class (and therefore
	          // don't need to do anything), error:
	          else if (! prev.isDerivedFrom(elementType)) {
	              String eltName = elementType.getName().getString();
	              SoDebugError.post("SoAction::enableElement",
	                                 "Cannot enable element "+eltName+" because element "+prev.getName().getString()+" "+
	                                 "is already enabled");
	          }
//	      #endif
	     		
	}

	// Returns the current setting of the global 
	// counter used to determine when lists are out of date. 
	public static int getCounter() {
		 return counter; 
	}
}
