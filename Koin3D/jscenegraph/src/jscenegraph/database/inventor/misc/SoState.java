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

/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 \**************************************************************************/

/*!
  \class SoState SoState.h Inventor/misc/SoState.h
  \brief The SoState class manages the Coin scene graph traversal state data.

  \ingroup general

  The SoState class is used by actions derived from the SoAction
  class. It manages the scene graph state as stacks of elements (i.e.
  instances of classes derived from SoElement).

  For more information on the inner workings of traversal states in
  Coin, we recommend the book &laquo;The Inventor Toolmaker&raquo; (ISBN
  0-201-62493-1), also available at SGI's <a
  href="http://techpubs.sgi.com/library">online library</a>. Search
  for "Toolmaker".
*/
// FIXME: should link to example(s) sourcecode extending the library
// by setting up new elements and/or actions. 20010716 mortene.

/*!
  \fn const SoElement * SoState::getConstElement(const int stackIndex) const

  This method returns a pointer to the top element of the given element
  stack.  The element is read-only and must not be changed under any
  circumstances or strange side-effects will occur.

  Note that this function will assert if the element with the given
  stack identity value is not presently on the state stack. To check
  whether or not an element is present in the stack, use
  SoState::isElementEnabled().
*/

/*!
  \fn SbBool SoState::isCacheOpen(void) const

  Returns whether a cache is open.
*/

/*!
  \fn SbBool SoState::isElementEnabled(const int stackindex) const

  This method returns TRUE if the element with the given element stack
  index is enabled, and FALSE otherwise.
*/

/*!
  SoElement * SoState::getElementNoPush(const int stackindex) const

  This method returns a pointer to a writable element without
  checking for state depth.  Use with care.
*/


package jscenegraph.database.inventor.misc;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;


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

	private		        SoElement[]         stack;
	private		        int                 numstacks;

	private boolean              cacheopen;

	SoStateP pimpl; //ptr

// *************************************************************************

	// Internal class used to store which elements are pushed for a depth.
// This makes it possible to avoid searching through all elements
// and testing depth in pop().
	static class sostate_pushstore implements Destroyable {
		public
		sostate_pushstore() {
			this.next = this.prev = null;
		}
		public final SbListInt elements = new SbListInt();
		public sostate_pushstore next; //ptr
		public sostate_pushstore prev; //ptr

		@Override
		public void destructor() {
			elements.destructor();
			next = null;
			prev = null;
		}
	};

	// class to store private data members
	static class SoStateP implements Destroyable {
		public
		SoAction action; // ptr
		SoElement[] initial; //ptr
		int depth;
		boolean ispopping;
		sostate_pushstore pushstore; //ptr

		@Override
		public void destructor() {
			action = null;
			initial = null;
			pushstore = null;
		}
	};

	/**
	 * Constructor. 
	 * Takes pointer to action instance this state is part of 
	 * and a list of type-ids of elements that are enabled. 
	 * 
	 * @param action
	 * @param enabledElements
	 */

// *************************************************************************

/*!
  The constructor.  The \a theAction argument is the action object the state
  is part of, and the \a enabledElements argument is an SoTypeList of the
  elements that are enabled for this action.

  The constructor pushes a default element onto the indexes of all the
  enabled element stacks.  SoElement::push() is not called on the initial
  elements in the SoState stacks, but SoElement::init() is.
*/
//	public SoState(SoAction _action, SoTypeList enabledElements) {
//	     int         i;
//	          SoElement   elt;
//
//	          action = _action;
//	          depth = 0;
//
//	          // Find out number of elements
//	          numStacks = SoElement.getNumStackIndices();
//
//	          // Allocate stack pointers
//	          stack = new SoElement [numStacks];
//
//	          // Initialize all stacks to NULL
//	          for (i = 0; i < numStacks; i++)
//	              stack[i] = null;
//
//	          // Allocate and initialize one instance of each enabled element.
//	          // While doing this, set up threaded stack of elements
//	          topElement = null;
//	          for (i = 0; i < enabledElements.getLength(); i++) {
//	              // Skip bad elements
//	              if (enabledElements.operator_square_bracket(i).isBad())
//	                  continue;
//
//	              elt = (SoElement ) enabledElements.operator_square_bracket(i).createInstance();
//	              elt.setDepth(depth);
//	              stack[elt.getStackIndex()] = elt;
//	              elt.init(this);
//	              elt.setNext(topElement);
//	             elt.setNextInStack(null);
//	             elt.setNextFree(null);
//	             topElement = elt;
//	         }
//
//	         // Push state to avoid clobbering initial element instances
//	         push();
//
//	         // Assume there are no caches open yet
//	         setCacheOpen(false);
//
//	}

	public SoState(SoAction theAction, final SoTypeList enabledelements)
	{
		pimpl = new SoStateP();

		pimpl.action = theAction;
		pimpl.depth = 0;
		pimpl.ispopping = false;
		this.cacheopen = false;

		int i;

		this.numstacks = SoElement.getNumStackIndices() ;

		// the stack member can be accessed from inline methods, and is
		// therefore not moved to the private class.
		this.stack = new SoElement[this.numstacks];
		pimpl.initial = new SoElement[this.numstacks];

		for (i = 0; i < this.numstacks; i++) {
			pimpl.initial[i] = null;
			this.stack[i] = null;
		}

		int numelements = enabledelements.getLength();
		for (i = 0; i < numelements; i++) {
			final SoType type = new SoType(enabledelements.operator_square_bracket(i));
			assert(type.isBad() || type.canCreateInstance());
			if (!type.isBad()) {
				SoElement element = (SoElement) type.createInstance();
				element.setDepth(pimpl.depth);
				int stackindex = element.getStackIndex();
				this.stack[stackindex] = element;
				pimpl.initial[stackindex] = element;
				element.init(this); // called for first element in state stack
			}
		}
		pimpl.pushstore = new sostate_pushstore();
	}

//	// java port : destructor
//	@Override
//	public void destructor() {
//	     // Pop state; matches push done in constructor
//		       pop();
//
////		   #ifdef DEBUG
//		       if (depth != 0) {
//		           SoDebugError.post("SoState::~SoState",
//		                              "State destroyed with non-zero ("+depth+") "+
//		                              "depth");
//		       }
////		   #endif
//
//		       // Get rid of all the elements on all the stacks.
//		       SoElement elt, nextElt;
//		       int i;
//		       for (i = 0; i < numStacks; i++) {
//		           elt = stack[i];
//		           while (elt != null) {
//		               nextElt = elt.getNextFree();
//		               elt.destructor();//delete elt; java port
//		               elt = nextElt;
//		           }
//		       }
//
//		       // Get rid of stack pointer array
//		       stack = null; //delete[] stack; java port
//
//	}

/*!
  The destructor.

  Note that when destruction happens, lagging events caused by lazy evaluation
  won't be performed.
*/

	public void destructor()
	{
		for (int i = 0; i < this.numstacks; i++) {
			SoElement elem = pimpl.initial[i];
			SoElement next;
			while (elem != null) {
				next = elem.getNextFree();//nextup; java port
				Destroyable.delete(elem);
				elem = next;
			}
		}

		//delete[] pimpl.initial; java port
		pimpl.initial = null;
		//delete[] this->stack; java port
		this.stack = null;

		sostate_pushstore item = pimpl.pushstore;
		while (item.prev != null) item = item.prev; // go to first item
		while (item != null) {
			sostate_pushstore next = item.next;
			Destroyable.delete(item);
			item = next;
		}
		Destroyable.delete(pimpl);
	}


	//! Returns the action instance the state is part of
    public SoAction           getAction()       { return pimpl.action; }

/*!
  This method returns a modifiable instance of the element on the top of
  the stack with the given \a stackindex.  Because of lazy programming,
  this function may need to do some work, so SoState::getConstElement()
  should be used instead whenever possible.
*/

	public SoElement
	getElement(int stackindex)
	{
		// catch attempts at setting an element from another element's pop()
		// method (yes, I did this stupid mistake myself and spent a long
		// time debugging it, pederb, 2007-08-01)
		assert(!pimpl.ispopping);

		if (!this.isElementEnabled(stackindex)) return null;
		SoElement element = this.stack[stackindex];

//#if 0 // debug
//		SoDebugError::postInfo("SoState::getElement",
//			"stackindex: %d, element: %p ('%s'), "
//		"stackdepth: %d, pushstack: %s",
//				stackindex, element,
//				element->getTypeId().getName().getString(),
//				element->getDepth(),
//				(element->getDepth() < pimpl->depth) ?
//		"yes" : "no");
//#endif // debug

		if (element.getDepth() < pimpl.depth) { // create elt of correct depth
		SoElement next = element.getNextFree();//nextup; java port
		if (next == null) { // allocate new element
			next = (SoElement) element.getTypeId().createInstance();
			next.setNextInStack(/*nextdown =*/ element); //java port
			element.setNextFree(/*nextup =*/ next); //java port
		}
		next.setDepth(pimpl.depth);
		next.push(this);
		this.stack[stackindex] = next;
		element = next;
		pimpl.pushstore.elements.append(stackindex);
	}
		return element;
	}

/*!
  This method pushes the state one level down.  This saves the state so it can
  be changed and later restored to this state by calling SoState::pop().

  The push and pop mechanism is performed lazily for efficiency reasons (avoids
  a lot of memory allocation and copying).  Only when a state element is
  actually going to be changed, that element will be pushed for real.
*/

	public void
	push()
	{
		if (pimpl.pushstore.next == null) {
		sostate_pushstore store = new sostate_pushstore();
		store.prev = pimpl.pushstore;
		pimpl.pushstore.next = store;
	}
		pimpl.pushstore = pimpl.pushstore.next;
		pimpl.pushstore.elements.truncate(0);
		pimpl.depth++;
	}

/*!
  This method pops the state to restore it to a previous state.
  Pops are performed eagerly but the code is very tight so there is
  no reason to worry about efficiency.
*/

	public void
	pop()
	{
		pimpl.ispopping = true;
		pimpl.depth--;
		int n = pimpl.pushstore.elements.getLength();
		if (n != 0) {
    IntArrayPtr array = pimpl.pushstore.elements.getArrayPtr();
			for (int i = n-1; i >= 0; i--) {
				int idx = array.get(i);
				SoElement elem = this.stack[idx];
				SoElement prev = elem.getNextInStack();//nextdown; java port
				assert(prev != null);
				prev.pop(this, elem);
				this.stack[idx] = prev;
			}
		}
		pimpl.pushstore.elements.truncate(0);
		pimpl.pushstore = pimpl.pushstore.prev;
		pimpl.ispopping = false;
	}

    /**
     * Java port
     * @return
     */
	public GL2 getGL2() {
		if(pimpl.action instanceof SoGLRenderAction) {
			SoGLRenderAction GLRenderAction = (SoGLRenderAction)pimpl.action;
			GL2 gl2 = Ctx.get(GLRenderAction.getCacheContext());
			return gl2;
		}
		return new GL2(){};
		//throw new IllegalStateException("Not in a SoGLRenderAction");
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
//	public void push() {
//		 depth++;
//	}
	
	// Pops the state, restoring the state to just before the last push(). 
//	public void pop() {
//	     SoElement poppedElt/*, nextInStack*/;
//
//	          --depth;
//
//	          // Do the popping in two passes. The first calls the pop() method
//	          // for all popped elements. The second pass actually removes the
//	          // elements from the stacks and frees them up. We do this in two
//	          // passes because calling pop() may add elements to the current
//	          // SoCacheElement, so we want to make sure this is done before
//	          // that element is popped.
//
//	          // Call pop() for all elements that were at previous depth
//	          for (poppedElt = topElement;
//	               poppedElt != null && poppedElt.getDepth() > depth;
//	               poppedElt = poppedElt.getNext()) {
//
//	              // Find next element in same stack as popped element. This
//	              // element will become the new top of that stack.
//	              /*nextInStack =*/ poppedElt.getNextInStack();
//
//	              // Give new top element in stack a chance to update things.
//	              // Pass old element instance just in case.
//	              poppedElt.getNextInStack().pop(this, poppedElt);
//
//	          }
//
//	          // Actually pop all such elements
//	          while (topElement != null && topElement.getDepth() > depth) {
//	              poppedElt = topElement;
//
//	              // Remove from main stack
//	              topElement = topElement.getNext();
//
//	              // Remove from element stack
//	              stack[poppedElt.getStackIndex()] = poppedElt.getNextInStack();
//	          }
//
//	}
	
    //! Returns TRUE if element with given stack index is enabled in state
    public boolean              isElementEnabled(int stackIndex)
        { return (stack[stackIndex] != null); }

	
	
	// Returns current depth of state. 
	public int getDepth() {
		 return pimpl.depth;
	}
	
	/**
	 * Sets/returns flag that indicates whether a cache is open. 
	 * This flag lets us optimize element capturing; 
	 * we don't need to try to capture elements if the flag is FALSE. 
	 * 
	 * @param flag
	 */
	public void setCacheOpen(boolean flag) {
		cacheopen = flag;
	}
	
	public boolean isCacheOpen() { return cacheopen; }

//	   ////////////////////////////////////////////////////////////////////////
//	   //
//	   // Description:
//	   //    Returns a writable instance of the element on the top of the
//	   //    stack with the given index.
//	   //
//	   // Use: public
//
//	   public SoElement
//	   getElement(int stackIndex)
//	   //
//	   ////////////////////////////////////////////////////////////////////////
//	   {
////	   #ifdef DEBUG
//	       // Nasty bug:  calling this routine while popping the state
//	       // can case very bad things to happen (topElement.next chain will
//	       // end up not being sorted by depth).  We'll check and make sure
//	       // that doesn't happen:
//	       if (depth < topElement.getDepth()) {
//	           SoDebugError.post("SoState::getElement",
//	               "Elements must not be changed while the state is being "+
//	               "popped (element being changed: "+
//	               SoElement.getIdFromStackIndex(stackIndex).getName().getString()+").");
//	       }
////	   #endif
//
//	       // Get top of element stack with given index
//	       SoElement   elt = stack[stackIndex];
//
////	   #ifdef DEBUG
//	       if (elt == null) {
//	           SoDebugError.post("SoState::getElement",
//	        		   SoElement.getIdFromStackIndex(stackIndex).getName().getString()+" is not enabled");
//	           return null;
//	       }
////	   #endif /* DEBUG */
//
//	       // If element is not at current depth, we have to push a new
//	       // element on the stack
//	       if (elt.getDepth() < depth) {
//	           SoElement newElt;
//
//	           // Each element stack is a doubly-linked list.  The
//	           // nextInStack pointer points to the next lowest element, and
//	           // the nextFree pointer points to the next hightest element.
//	           // The top element's nextFree pointer points to a free element.
//	           //
//	           // With this scheme we only have to allocate elements for the
//	           // stack once; pushing and popping during subsequent
//	           // traversals just move the topElement pointer up and down the
//	           // list.
//	           if (elt.getNextFree() != null) {
//	               newElt = elt.getNextFree();
//	           } else {
//	               newElt = (SoElement )(elt.getTypeId().createInstance());
//	               elt.setNextFree(newElt);
//	               newElt.setNextInStack(elt);
//	               newElt.setNextFree(null);
//	           }
//
//	           newElt.setDepth(depth);
//
//	           // Add it to the all-element stack
//	           newElt.setNext(topElement);
//
//	           topElement = stack[stackIndex] = newElt;
//
//	           // Call push on new element in case it has side effects
//	           newElt.push(this);
//
//	           // Return new element
//	           elt = newElt;
//	       }
//
//	       return elt;
//	   }
	   
	     //! Internal-only, dangerous method that returns a writeable
	        //! element without checking for state depth and doing a push.
	        //! Be very careful and consider the caching implications before
	        //! using this method!
	   public     SoElement getElementNoPush(int stackIndex)
	            { return stack[stackIndex]; }
	   	   
}
