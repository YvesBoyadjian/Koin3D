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
 |      This file defines the SoState class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Destroyable;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoState
///
///  An SoState collects and holds state while traversing a scene
///  graph. A state is composed of a variety of elements, each of which
///  holds some specific information, such as coordinates or diffuse
///  color of the surface material.
///
///  Each element is stored in its own stack so that save and restore
///  can be implemented as push and pop. These stack operations are
///  performed lazily, so that pushing of a value occurs only when the
///  value would be overwritten, for efficiency.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoState implements Destroyable {

	   
	private		        SoAction            action;        
	private		        int                 depth;          
	private		        SoElement[]         stack;        
	private		        int                 numStacks;      
	private		        SoElement          topElement;     
		    
	private boolean              cacheOpen;      
	
	/**
	 * Constructor. 
	 * Takes pointer to action instance this state is part of 
	 * and a list of type-ids of elements that are enabled. 
	 * 
	 * @param action
	 * @param enabledElements
	 */
	public SoState(SoAction _action, SoTypeList enabledElements) {
	     int         i;
	          SoElement   elt;
	      
	          action = _action;
	          depth = 0;
	      
	          // Find out number of elements
	          numStacks = SoElement.getNumStackIndices();
	      
	          // Allocate stack pointers
	          stack = new SoElement [numStacks];
	      
	          // Initialize all stacks to NULL
	          for (i = 0; i < numStacks; i++)
	              stack[i] = null;
	      
	          // Allocate and initialize one instance of each enabled element.
	          // While doing this, set up threaded stack of elements
	          topElement = null;
	          for (i = 0; i < enabledElements.getLength(); i++) {
	              // Skip bad elements
	              if (enabledElements.operator_square_bracket(i).isBad())
	                  continue;
	      
	              elt = (SoElement ) enabledElements.operator_square_bracket(i).createInstance();
	              elt.setDepth(depth);
	              stack[elt.getStackIndex()] = elt;
	              elt.init(this);
	              elt.setNext(topElement);
	             elt.setNextInStack(null);
	             elt.setNextFree(null);
	             topElement = elt;
	         }
	     
	         // Push state to avoid clobbering initial element instances
	         push();
	     
	         // Assume there are no caches open yet
	         setCacheOpen(false);
	    		
	}
	
    //! Returns the action instance the state is part of
    public SoAction           getAction()       { return action; }

    /**
     * Java port
     * @return
     */
	public GL2 getGL2() {
		if(action instanceof SoGLRenderAction) {
			SoGLRenderAction GLRenderAction = (SoGLRenderAction)action;
			GL2 gl2 = GLRenderAction.getCacheContext();
			return gl2;
		}
		throw new IllegalStateException("Not in a SoGLRenderAction");
	}
	
    //! Returns the top (read-only) instance of the given element stack
    public SoElement getConstElement(int stackIndex)
          { return stack[stackIndex]; }
  	
		   	
	/**
	 * Pushes (saves) the current state until a pop() restores it. 
	 * The push is done lazily: this just increments the depth in the state. 
	 * When an element is accessed with getElement() and its depth is less 
	 * than the current depth, it is then pushed individually. 
	 */
	public void push() {
		 depth++; 
	}
	
	// Pops the state, restoring the state to just before the last push(). 
	public void pop() {
	     SoElement poppedElt/*, nextInStack*/;
	      
	          --depth;
	      
	          // Do the popping in two passes. The first calls the pop() method
	          // for all popped elements. The second pass actually removes the
	          // elements from the stacks and frees them up. We do this in two
	          // passes because calling pop() may add elements to the current
	          // SoCacheElement, so we want to make sure this is done before
	          // that element is popped.
	      
	          // Call pop() for all elements that were at previous depth
	          for (poppedElt = topElement;
	               poppedElt != null && poppedElt.getDepth() > depth;
	               poppedElt = poppedElt.getNext()) {
	      
	              // Find next element in same stack as popped element. This
	              // element will become the new top of that stack.
	              /*nextInStack =*/ poppedElt.getNextInStack();
	      
	              // Give new top element in stack a chance to update things.
	              // Pass old element instance just in case.
	              poppedElt.getNextInStack().pop(this, poppedElt);
	      
	          }
	      
	          // Actually pop all such elements
	          while (topElement != null && topElement.getDepth() > depth) {
	              poppedElt = topElement;
	      
	              // Remove from main stack
	              topElement = topElement.getNext();
	      
	              // Remove from element stack
	              stack[poppedElt.getStackIndex()] = poppedElt.getNextInStack();
	          }
	     
	}
	
    //! Returns TRUE if element with given stack index is enabled in state
    public boolean              isElementEnabled(int stackIndex)
        { return (stack[stackIndex] != null); }

	
	
	// Returns current depth of state. 
	public int getDepth() {
		 return depth; 
	}
	
	/**
	 * Sets/returns flag that indicates whether a cache is open. 
	 * This flag lets us optimize element capturing; 
	 * we don't need to try to capture elements if the flag is FALSE. 
	 * 
	 * @param flag
	 */
	public void setCacheOpen(boolean flag) {
		cacheOpen = flag; 
	}
	
	public boolean isCacheOpen() { return cacheOpen; }	

	// java port : destructor
	@Override
	public void destructor() {
	     // Pop state; matches push done in constructor
		       pop();
		   
//		   #ifdef DEBUG
		       if (depth != 0) {
		           SoDebugError.post("SoState::~SoState",
		                              "State destroyed with non-zero ("+depth+") "+
		                              "depth");
		       }
//		   #endif
		   
		       // Get rid of all the elements on all the stacks.
		       SoElement elt, nextElt;
		       int i;
		       for (i = 0; i < numStacks; i++) {
		           elt = stack[i];
		           while (elt != null) {
		               nextElt = elt.getNextFree();
		               elt.destructor();//delete elt; java port
		               elt = nextElt;
		           }
		       }
		   
		       // Get rid of stack pointer array
		       stack = null; //delete[] stack; java port
		  	
	}

	 
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns a writable instance of the element on the top of the
	   //    stack with the given index.
	   //
	   // Use: public
	   
	   public SoElement 
	   getElement(int stackIndex)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
	       // Nasty bug:  calling this routine while popping the state
	       // can case very bad things to happen (topElement.next chain will
	       // end up not being sorted by depth).  We'll check and make sure
	       // that doesn't happen:
	       if (depth < topElement.getDepth()) {
	           SoDebugError.post("SoState::getElement",
	               "Elements must not be changed while the state is being "+
	               "popped (element being changed: "+
	               SoElement.getIdFromStackIndex(stackIndex).getName().getString()+").");
	       }
//	   #endif
	   
	       // Get top of element stack with given index
	       SoElement   elt = stack[stackIndex];
	   
//	   #ifdef DEBUG
	       if (elt == null) {
	           SoDebugError.post("SoState::getElement", 
	        		   SoElement.getIdFromStackIndex(stackIndex).getName().getString()+" is not enabled");
	           return null;
	       }
//	   #endif /* DEBUG */
	   
	       // If element is not at current depth, we have to push a new
	       // element on the stack
	       if (elt.getDepth() < depth) {
	           SoElement newElt;
	   
	           // Each element stack is a doubly-linked list.  The
	           // nextInStack pointer points to the next lowest element, and
	           // the nextFree pointer points to the next hightest element.
	           // The top element's nextFree pointer points to a free element.
	           //
	           // With this scheme we only have to allocate elements for the
	           // stack once; pushing and popping during subsequent
	           // traversals just move the topElement pointer up and down the
	           // list.
	           if (elt.getNextFree() != null) {
	               newElt = elt.getNextFree();
	           } else {
	               newElt = (SoElement )(elt.getTypeId().createInstance());
	               elt.setNextFree(newElt);
	               newElt.setNextInStack(elt);
	               newElt.setNextFree(null);
	           }
	   
	           newElt.setDepth(depth);
	   
	           // Add it to the all-element stack
	           newElt.setNext(topElement);
	   
	           topElement = stack[stackIndex] = newElt;
	   
	           // Call push on new element in case it has side effects
	           newElt.push(this);
	   
	           // Return new element
	           elt = newElt;
	       }
	   
	       return elt;
	   }
	   
	     //! Internal-only, dangerous method that returns a writeable
	        //! element without checking for state depth and doing a push.
	        //! Be very careful and consider the caching implications before
	        //! using this method!
	   public     SoElement getElementNoPush(int stackIndex)
	            { return stack[stackIndex]; }
	   	   
}
