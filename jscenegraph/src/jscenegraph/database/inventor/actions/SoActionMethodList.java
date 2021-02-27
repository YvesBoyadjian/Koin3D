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

import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoActionMethodList
///
///  Internal class.  A list of routines to call, one for each node type.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoActionMethodList extends SbPList<SoActionMethodList.SoActionMethod> {
	
	public static interface SoActionMethod {
		
		void run(SoAction soAction, SoNode soNode);
	}
	
	private      SoActionMethodList  parent;
	private	        int                 numValidTypes;
	
	//
	   // Description:
	   //    Dummy action used to mark entries in the action/method table as
	   //    empty. 
	   //
	  // Use: internal
	       private static SoActionMethod  dummyAction = new SoActionMethod() {

		@Override
		public void run(SoAction soAction, SoNode soNode) {
		}
    	
    	
    };
    
    // Constructor. Pass in list from parent action. 
 	public SoActionMethodList(SoActionMethodList parentList) {
 	     parent = parentList;
 	         numValidTypes = 0;
 	    
 	}
		    	
	/**
	 * Operator used to get and set methods. 
	 * The list will grow dynamically as we access 
	 * items off the end of the list, and entries will be 
	 * initialized to NULL. 
	 */
	public SoActionMethod operator_square_bracket(int i) {
		 return ((SoActionMethod)super.operator_square_bracket(i)); 
	}
	
	// Add a method to the appropriate place in the list. 
	public void addMethod(SoType nodeType, SoActionMethod method) {
//		 #ifdef DEBUG
//		       // Make sure nodeType is a kind of node!
//		       if (! nodeType.isDerivedFrom(SoNode::getClassTypeId()))
//		           SoDebugError::post("SoAction::addMethod", "%s is not a node type",
//		                              nodeType.getName().getString());
//		   #endif /* DEBUG */
		   
		       numValidTypes = 0;
		       this.operator_square_bracket(SoNode.getActionMethodIndex(nodeType) , method);
		  		
	}
	
	// This MUST be called before using the list. 
	// It fills in NULL entries with their parents' method. 
	public void setUp() {
		
	     if (numValidTypes == SoType.getNumTypes())
	    	           return;         // Already set up the table
	    	   
	    	       // SoNode's slot must be filled in.  If this action doesn't have a
	    	       // parent action, it is filled in with the null action.  If it
	    	       // does have a parent action, a dummy action is used, and the
	    	       // table is overwritten with the parent's method wherever the
	    	       // dummy action appears in a second pass.
	    	       int i = SoNode.getActionMethodIndex(SoNode.getClassTypeId());
	    	       if (this.operator_square_bracket(i) == null) {
	    	           if (parent == null)
	    	               this.operator_square_bracket(i, SoAction.nullAction);
	    	           else
	    	               this.operator_square_bracket(i, dummyAction);
	    	       }
	    	   
	    	       // Next, find all nodes derived from SoNode (note: it is a good
	    	       // thing we don't have to do this often, since getAllDerivedFrom
	    	       // must look through the entire list of types).
	    	       final SoTypeList nodes = new SoTypeList();
	    	       SoType.getAllDerivedFrom(SoNode.getClassTypeId(), nodes);
	    	   
	    	       // Now, for any empty slots, fill in the slot from a parent with a
	    	       // non-NULL slot:
	    	       for (i = 0; i < nodes.getLength(); i++) {
	    	           SoType n = nodes.operator_square_bracket(i);
	    	           if (this.operator_square_bracket(SoNode.getActionMethodIndex(n)) == null) {
	    	               this.operator_square_bracket(SoNode.getActionMethodIndex(n), parentMethod(n));
	    	           }
	    	       }
	    	   
	    	       // Inherit any undefined methods from parent class
	    	       if (parent != null) {
	    	           parent.setUp();
	    	   
	    	           for (i = 0; i < getLength(); i++) {
	    	   
	    	               SoActionMethod      method = this.operator_square_bracket(i);
	    	   
	    	               if (method == dummyAction)
	    	            	   operator_square_bracket(i,parent.operator_square_bracket(i));
	    	           }
	    	       }
	    	   
	    	       numValidTypes = SoType.getNumTypes();
	    	  	}
	
	//
	   // Description:
	   //    Recursively looks for a non-NULL action method from a node's
	   //    parents.
	   //
	   // Use: internal
	   	
	private SoActionMethod parentMethod(SoType t) {
		
	     SoActionMethod m;
	          SoType parent = t;
	      
	          // Look through parents until non-NULL method is found
	          do {
	              parent = parent.getParent();
	              m = this.operator_square_bracket(SoNode.getActionMethodIndex(parent));
	          } while (m == null);
	      
	          return m;
	     	}
}
