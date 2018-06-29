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

import jscenegraph.database.inventor.SoType.CreateMethod;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.nodekits.inventor.SoNodeKitPath;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Path that points to a list of hierarchical nodes.
/*!
\class SoPath
\ingroup General
A path represents a scene graph or subgraph.  It contains a list of
pointers to nodes forming a chain from some root to some descendent.
Each node in the chain is a child of the previous node. Paths are used
to refer to some object in a scene graph precisely and unambiguously,
even if there are many instances of the object. Therefore, paths are
returned by both the SoRayPickAction and SoSearchAction.


When an action is applied to a path, only the nodes in the subgraph
defined by the path are traversed. These include: the nodes in the
path chain, all nodes (if any) below the last node in the path, and
all nodes whose effects are inherited by any of these nodes.


SoPath attempts to maintain consistency of paths even when the
structure of the scene graph changes. For example, removing a child
from its parent when both are in a path chain cuts the path chain at
that point, leaving the top part intact. Removing the node to the left
of a node in a path adjusts the index for that node. Replacing a child
of a node when both the parent and the child are in the chain replaces
the child in the chain with the new child, truncating the path below
the new child.


Note that only public children of nodes are accessible from an
SoPath. Nodes like node kits that limit access to their children
may provide other ways to get more information, such as by using the
SoNodeKitPath class.

\par See Also
\par
SoNode, SoRayPickAction, SoSearchAction, SoNodeKitPath
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves
 *
 */
public class SoPath extends SoBase implements Destroyable {
	
	protected static class SoPathImpl {
		public final SoNodeList nodes = new SoNodeList(); //!< Pointers to nodes
		protected final SbIntList indices = new SbIntList(); //!< Child indices
		private int numPublic; //!< How many children are public
		private int minNumPublic; //!< Minimum we KNOW are public
		private boolean doAuditors; //!< TRUE if auditors to be maintained
	}
	
	protected SoPathImpl impl = new SoPathImpl();
	
	private static SoType classTypeId; //!< TypeId of paths

	/* (non-Javadoc)
	 * @see com.openinventor.inventor.misc.SoBase#getTypeId()
	 */
	@Override
	public SoType getTypeId() {
		return classTypeId;
	}
	
	/**
	 * Java port. For SoFullPath
	 * @param path
	 */
	protected SoPath(SoPath path) {
		impl = path.impl;
	}
	
	// Constructs an empty path. 
	public SoPath() {
//		 #ifdef DEBUG
		       if (! SoDB.isInitialized())
		           SoDebugError.post("SoPath::SoPath",
		                              "Cannot construct paths before "+
		                              "calling SoDB::init()");
//		   #endif /* DEBUG */
		   
		       impl.doAuditors = true;
		       impl.minNumPublic = 0;
		       impl.numPublic  = 0;
	}
	
	public SoPath(int approxLength) {
		
		impl.doAuditors = true;
		impl.numPublic  = 0;
		impl.minNumPublic  = 0;
	     	}
	
	// Constructs a path and sets the head node to the given node.
	//
	   // Description:
	   //    Constructor given head node to insert in path.
	   //
	   // Use: public
	  
	public SoPath(SoNode node) {
		
//		 #ifdef DEBUG
		       if (! SoDB.isInitialized())
		           SoDebugError.post("SoPath::SoPath",
		                              "Cannot construct paths before "+
		                              "calling SoDB::init()");
//		   #endif /* DEBUG */
		   
		       impl.doAuditors = true;
		       impl.numPublic  = 0;
		       impl.minNumPublic  = 0;
		       setHead(node);
		  	}
	
	public void destructor() {
		truncate(0,false);
		super.destructor();
	}

	/**
	 * Sets head node (first node in chain). 
	 * The head node must be set before the append() or push() methods 
	 * may be called. 
	 * 
	 * @param node
	 */
	public void setHead(SoNode node) {
		  // Clear out existing nodes
		truncate(0, false);
		 
		// The index value doesn't matter, since it should never be used
		  append(node, -1);
		 
		  if (impl.doAuditors)
		  startNotify();
				
	}
	
	/**
	 * Adds node to end of chain; 
	 * the node is the childIndex'th child of the current tail node. 
	 * 
	 * @param childIndex
	 */
	 //
	   // Description:
	   //    Adds a node to the end of the current chain of nodes in a path.
	   //    The node is given by its child index in the currently last node
	   //    in the path.
	   //
	   // Use: public
	   	public void append(int childIndex) {
		
	        SoNode      tail;
	         
//	         #ifdef DEBUG
	             // Make sure there is already a node in the path
	             if (getFullLength() == 0) {
	                 SoDebugError.post("SoPath::append", "No head node to append to");
	                 return;
	             }
//	         #endif /* DEBUG */
	         
	             // Get real tail node of path
	             tail = impl.nodes.operator_square_bracket(getFullLength() - 1);
	         
//	         #ifdef DEBUG
	             // If there is one, make sure it can have children
	             SoChildList children = tail.getChildren();
	             if (children == null) {
	                 SoDebugError.post("SoPath::append",
	                                    "Tail node does not allow children to be added");
	                 return;
	             }
	         
	             // And make sure index is valid
	             if (childIndex < 0 || childIndex >= children.getLength()) {
	                 SoDebugError.post("SoPath::append",
	                                    "Invalid child index "+ childIndex);
	                 return;
	             }
//	         #endif
	         
	             append((tail.getChildren()).operator_square_bracket(childIndex), childIndex);
	        	}
	
	/**
	 * Adds node to end of chain; uses the first occurrence of 
	 * childNode as child of current tail node. If the path is empty, 
	 * this is equivalent to setHead(childNode). 
	 */
	public void append(SoNode childNode) {
		
		  SoNode tail;
		    int childIndex;
		   
		    // If the path is empty, this is a setHead() operation
		    if (getFullLength() == 0) {
		    setHead(childNode);
		    return;
		    }
		   
		    // Get real tail node of path
		    tail = impl.nodes.operator_square_bracket(getFullLength() - 1);
		   
//		   #ifdef DEBUG
		    // Make sure tail node can have children
		    if (tail.getChildren() == null) {
		    SoDebugError.post("SoPath::append",
		    "Tail node does not allow children to be added");
		    return;
		    }
//		   #endif
		   
		    // Find index of node as child of tail node
		    childIndex = tail.getChildren().find(childNode);
		   
//		   #ifdef DEBUG
		    // Make sure index is valid
		    if (childIndex < 0) {
		    SoDebugError.post("SoPath::append",
		    "Node to append is not a child of path tail");
		    return;
		    }
//		   #endif
		   
		    append(childNode, childIndex);
		   
		  }
	
    //! These allow a path to be treated as a stack; they push a node at the
    //! end of the chain and pop the last node off.
    public void                push(int childIndex) { append(childIndex); }
    //! These allow a path to be treated as a stack; they push a node at the
    //! end of the chain and pop the last node off.
    public void                pop()                { truncate(getFullLength() - 1); }

	
	// These return the first and last nodes in a path chain. 
	public SoNode getHead() {
		 return impl.nodes.operator_square_bracket(0); 
	}
	public SoNode getTail() { return (impl.nodes.operator_square_bracket(getLength() - 1)); }
	
	/**
	 * Returns a pointer to the i'th node in the chain. 
	 * Calling getNode(0) is equivalent to calling getHead(). 
	 * 
	 * @param i
	 * @return
	 */
	public SoNode getNode(int i) {
		 return (impl.nodes.operator_square_bracket(i)); 
	}
	
    //! Returns a pointer to the \p i'th node.
    //! Passing 0 for \p i returns the tail node.
    public SoNode getNodeFromTail(int i) 
        { return (impl.nodes.operator_square_bracket(getLength() - 1 - i)); }

	
	// Return the index of the i'th node (within its parent) in the chain. 
	public int getIndex(int i) {
		 return (impl.indices.operator_square_bracket(i)); 
	}
	
	//
	   // Description:
	   //    Returns the public length of the path.
	   //
	   // Use: public
	   
	public int getLength() {
		
	     // Cast const away...
		       SoPath This = (SoPath )this;
		   
		       // If we aren't sure how many are public, figure it out:
		       if (impl.numPublic == -1) {
		   
		           int lastPublicIndex = 0;
		           if (impl.minNumPublic > 1)
		               lastPublicIndex = impl.minNumPublic - 1;
		   
		          // Last test is for the second to last node.
		           // If it passes, then lastPublicIndex will be incremented to be the
		           // final node, which we don't need to test.
		   
		           for (  ; lastPublicIndex < (getFullLength() - 1) ; lastPublicIndex++) {
		               // Children of this node will be private, so stop.
		               if ( ! impl.nodes.operator_square_bracket(lastPublicIndex).isOfType(SoGroup.getClassTypeId()))
		                   break;
		           }
		           This.impl.numPublic = This.impl.minNumPublic = lastPublicIndex + 1;
		       }
		       return impl.numPublic;
		   	}
	
	/**
	 * Truncates the path chain, removing all nodes from index start on. 
	 * Calling truncate(0) empties the path entirely. 
	 * 
	 * @param start
	 */
	public void truncate(int start) {
	     truncate(start, true);
	}
	
	public int getFullLength() {
		return impl.nodes.getLength();
	}
	
	//
	// Description:
	// Appends the given node and index to the lists.
	//
	// Use: private
	 
	private void append(SoNode node, int index) {
		  // Optimization here: we just set numPublic to -1 to indicate that
		  // we aren't sure how many are public, since keeping it up to date
		  // can be fairly expensive if the path is changing a lot.
		impl.numPublic = -1;
		 
		  // Append to lists
		impl.nodes.append(node);
		impl.indices.append(index);
		 
		  // Tell the childlist of the node that the path cares about it
		  if (impl.doAuditors) {
		  SoChildList childList = node.getChildren();
		  if (childList != null) {
		  childList.addPathAuditor(this);
		  }
		  }
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called by SoType::createInstance to create a new
//    instance of a path, given its name.
//
// Use: private

public Object
createInstance()
//
////////////////////////////////////////////////////////////////////////
{
    return (Object)(new SoPath());
}

	    //! Reads stuff into instance of SoPath. Returns FALSE on error.
    public boolean        readInstance(SoInput in, short flags) {
    	return false; //TODO
    }

	
	
	//
	// Description:
	// Removes all nodes in the path starting at the given node index
	// (0 = head of path chain, etc.). Flag indicates whether to notify.
	//
	// Use: private
	 
	private void truncate(int start, boolean doNotify) {
		  int i;
		   
//		   #ifdef DEBUG
		    if (start < 0 || (start > 0 && start >= getFullLength())) {
		    SoDebugError.post("SoPath::truncate", "Starting index is invalid");
		    return;
		    }
//		   #endif /* DEBUG */
		   
		    // Remove path from all affected nodes' auditors lists
		    if (impl.doAuditors)
		    for (i = start; i < getFullLength(); i++) {
		    SoChildList childList = impl.nodes.operator_square_bracket(i).getChildren();
		    if (childList != null) {
		    childList.removePathAuditor(this);
		    }
		    }
		   
		    // Truncate both lists
		    impl.nodes.truncate(start);
		    impl.indices.truncate(start);
		   
		    if (start < impl.minNumPublic) {
		    	impl.minNumPublic = impl.numPublic = start;
		    }
		   
		    if (impl.doAuditors && doNotify)
		    startNotify();
		   		
	}
	
	// Returns TRUE if the node is found anywhere in the path chain. 
	public boolean containsNode(SoNode node) {
		
	     for (int i = 0; i < getFullLength(); i++)
	    	           if (impl.nodes.operator_square_bracket(i) == node)
	    	               return true;
	    	   
	    	       return false;
	    	  	}
	
////////////////////////////////////////////////////////////////////////
 //
 // Description:
 //    Returns TRUE if the nodes in the chain in the passed path are
 //    contained (in consecutive order) in this path chain.
 //
 // Use: public
 
public  boolean
 containsPath( SoPath path)
 //
 ////////////////////////////////////////////////////////////////////////
 {
     int i, j;
 
     // First find the head of the target path in this path
     for (i = 0; i < getFullLength(); i++)
         if (getNode(i) == path.getHead())
             break;
 
     // Head node is not there
     if (i == getFullLength())
         return false;
 
     // If there aren't enough nodes left in this path, then no match
     if (getFullLength() - i < path.getFullLength())
         return false;
 
     // Otherwise, start comparing indices in the two paths to see if the
     // paths match. Skip the head node, which we already know is a match.
     for (j = 1; j < path.getFullLength(); j++)
         if (path.getIndex(j) != getIndex(i + j))
             return false;
 
     return true;
 }
 	
	
	/**
	 * This is called when a node in the path chain replaces a child. 
	 * The passed index is the index of the child to be removed 
	 * 
	 * @param parent
	 * @param index
	 * @param newChild
	 */
	public void replaceIndex(SoNode parent, int index, SoNode newChild) {
	     int i;
	      
	          // Find index of parent node in path
	          for (i = 0; i < getFullLength(); i++)
	              if (impl.nodes.operator_square_bracket(i) == parent)
	                  break;
	      
//	      #ifdef DEBUG
	          if (i == getFullLength()) {
	              SoDebugError.post("(internal) SoPath::replaceIndex",
	                                 "Can't find parent node");
	              return;
	          }
//	      #endif /* DEBUG */
	      
	          // If there is a next node and it is the one being replaced, change it
	          if (++i < getFullLength() && impl.indices.operator_square_bracket(i) == index) {
	      
	              // The children of the new node are most likely different from
	              // those in the old one. So if the path continued below the
	              // old node, truncate it so the path won't believe that the
	              // old children are under the new node.
	              truncate(i, false);
	      
	              // Add the new node and same old index
	              append(newChild, index);
	          }
	     		
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the given notification list involves a change to
//    a node that affects the path. It is assumed that the last (most
//    recent) node in the list is the head node of the path.
//
// Use: internal

public boolean
isRelevantNotification(SoNotList list) 
//
////////////////////////////////////////////////////////////////////////
{
    // Trace down the notification list to find the first node (if
    // any) that is not on the path. Stop when we reach the first
    // (in time) record that was at a node in the graph.

     SoNotRec      rec = list.getLastRec();
     SoNotRec      prevRec = null;
    int                 curIndex = 0;
    boolean                offPath  = false;

    while (rec != null && curIndex < getLength()) {

        // Stop if the node in the current record is not the next node
        // in the path
        if (rec.getBase() != getNode(curIndex)) {
            offPath = true;
            break;
        }

        // Go to the next record and path node, IF we're following a
        // PARENT notification
        if (rec.getPrevious() != null &&
            rec.getPrevious().getType() != SoNotRec.Type.PARENT) {
            break;
        }
        prevRec = rec;
        rec = rec.getPrevious();

        curIndex++;
    }

    // If not all notified nodes are on the path, we have to do some
    // extra testing
    if (offPath) {

        SoNode    node;
        int             index;

        // The "rec" record points to a node that is off the path.
        // Find out the index of this node in the parent node, which
        // is pointed to by the previous record (which is guaranteed
        // to exist and be on the path).
        node = ( SoNode ) rec.getBase();
        index= (( SoNode )prevRec.getBase()).getChildren().find(node);

        // If the node is to the right of the path, the change does
        // not affect the path
        if (index > getIndex(curIndex))
            return false;

        // If it is to the left, it doesn't affect the path if any of
        // the notification records go through a separator-type object:
        else {
            while (true) {
                if (! node.affectsState())
                    return false;

                rec = rec.getPrevious();
                if (rec == null || rec.getType() != SoNotRec.Type.PARENT)
                    break;

                node = ( SoNode ) rec.getBase();
            }
        }
    }

    // If we made it this far, it's a relevant notification
    return true;
}

	
	
	/**
	 * Creates and returns a new path that is a copy of some or all of this path. 
	 * Copying starts at the given index (default is 0, which is the head node). 
	 * A numNodes of 0 (the default) means copy all nodes from the starting index 
	 * to the end. 
	 * Returns null on error. 
	 * 
	 * @return
	 */
	// java port
	public SoPath copy() {
		return copy(0,0);
	}
	
	 //
	   // Description:
	   //    Creates and returns a new path that is a copy of some or all of
	   //    this path. Copying starts at the given index (default is 0,
	   //    which is the head node). numNodes = 0 (the default) means copy
	   //    all nodes from the starting index to the end. Returns null on error.
	   //
	   // Use: public
	
	// java port
	public SoPath copy(int startFromNodeIndex) {
		return copy(startFromNodeIndex,0);
	}
	
	   public SoPath 
	   copy(int startFromNodeIndex, int numNodes)
	   //
	   {
	       SoPath      newPath;
	       int         lastNodeIndex, i;
	   
//	   #ifdef DEBUG
	       if (startFromNodeIndex < 0 ||
	           startFromNodeIndex >= getFullLength()) {
	           SoDebugError.post("SoPath::copy", "Starting index is invalid");
	           return null;
	       }
//	   #endif /* DEBUG */
	   
	       if (numNodes == 0)
	           lastNodeIndex = getFullLength() - 1;
	   
	       else {
	           lastNodeIndex = startFromNodeIndex + numNodes - 1;
	   
//	   #ifdef DEBUG
	           if (lastNodeIndex >= getFullLength()) {
	               lastNodeIndex  = getFullLength() - 1;
	               SoDebugError.postWarning("SoPath::copy",
	                                         "Copying only "+(lastNodeIndex - startFromNodeIndex + 1)+" nodes (asked for "+numNodes+")");
	           }
//	   #endif /* DEBUG */
	       }
	   
	       // Allocate path of correct size
	       newPath = new SoPath(lastNodeIndex - startFromNodeIndex + 1);
	   
	       newPath.setHead(getNode(startFromNodeIndex));
	       for (i = startFromNodeIndex + 1; i <= lastNodeIndex; i++)
	           newPath.append(impl.indices.operator_square_bracket(i));
	   
	       return newPath;
	   }
	   
	   protected void auditPath(boolean flag) {
		   impl.doAuditors = flag; 
	   }

	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    This initializes the SoPath class.
	    //
	    // Use: internal
	    
	   public static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        // Create SoType type
	        classTypeId = SoType.createType(SoBase.getClassTypeId(), new SbName("Path"),
	                                         new CreateMethod() {

												@Override
												public Object run() {
													return new SoPath();
												}
	        	
	        });
	    }
	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Comparison operator tests path equality. Two paths are equal if
	    //    their chains are the same length and all node pointers in them
	    //    are the same.
	    //
	    // Use: public
	    	   
	   public boolean operator_equals(SoPath other) {
		     int i;
		     
		     SoPath p1 = this;
		     SoPath p2 = other;
		      
		          if (p1.getFullLength() != p2.getFullLength())
		              return false;
		      
		          // Compare path nodes from bottom up since there is more
		          // likelihood that they differ at the bottom. That means that
		          // unequal paths will exit this loop sooner.
		      
		          for (i = p1.getFullLength() - 1; i >= 0; --i)
		              if (p1.impl.nodes.operator_square_bracket(i) != p2.impl.nodes.operator_square_bracket(i) || p1.impl.indices.operator_square_bracket(i) != p2.impl.indices.operator_square_bracket(i))
		                  return false;
		      
		          return true;
		     		   
	   }
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the last path given the name 'name'.  Returns null if
//    there is no path with the given name.
//
// Use: public

public SoPath 
getByName(final SbName name)
//
////////////////////////////////////////////////////////////////////////
{
    return (SoPath )getNamedBase(name, SoPath.getClassTypeId());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds all paths named 'name' to the list.  Returns the number of
//    paths found.
//
// Use: public

public int
getByName( SbName name, SoPathList list)
//
////////////////////////////////////////////////////////////////////////
{
    return getNamedBases(name, list, SoPath.getClassTypeId());
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called when a node in the path chain has a child added.
//    The passed index is the index of the new child. This makes sure
//    the integrity of the path is not corrupted.
//
// Use: internal

public void
insertIndex(SoNode parent, int newIndex)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Find index of parent node in path
    for (i = 0; i < getFullLength(); i++)
        if (impl.nodes.operator_square_bracket(i) == parent)
            break;

//#ifdef DEBUG
    if (i == getFullLength()) {
        SoDebugError.post("(internal) SoPath::insertIndex",
                           "Can't find parent node");
        return;
    }
//#endif /* DEBUG */

    // Increment index of next node if there is one and if necessary
    if (++i < getFullLength() && impl.indices.operator_square_bracket(i) >= newIndex) {
    	impl.indices.operator_square_bracket(i, impl.indices.operator_square_bracket(i)+1);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called when a node in the path chain has a child removed.
//    The passed index is the index of the child to be removed. This
//    makes sure the integrity of the path is not corrupted.
//
// Use: internal

public void
removeIndex(SoNode parent, int oldIndex)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Find index of parent node in path
    for (i = 0; i < getFullLength(); i++)
        if (impl.nodes.operator_square_bracket(i) == parent)
            break;

//#ifdef DEBUG
    if (i == getFullLength()) {
        SoDebugError.post("(internal) SoPath::removeIndex",
                           "Can't find parent node");
        return;
    }
//#endif /* DEBUG */

    // If there is a next node
    if (++i < getFullLength()) {

        // If the next node is the one being removed, truncate the path
        if (impl.indices.operator_square_bracket(i) == oldIndex)
            truncate(i, false);

        // Decrement index of next node if it is after the removed one
        else if (impl.indices.operator_square_bracket(i) > oldIndex)
        	impl.indices.operator_square_bracket(i,impl.indices.operator_square_bracket(i)-1);
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    If the paths have different head nodes, this returns -1.
//    Otherwise, it returns the index into the chain of the last node
//    (starting at the head) that is the same for both paths.
//
// Use: public

public int
findFork(final SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    int shorterLength, i;

    // Return -1 if heads are different nodes
    if (path.getHead() != getHead())
        return -1;

    // Find length of shorter path
    shorterLength = path.getFullLength();
    if (getFullLength() < shorterLength)
        shorterLength = getFullLength();

    // Return the index of the last pair of nodes that match
    for (i = 1; i < shorterLength; i++)
        if (getIndex(i) != path.getIndex(i))
            return i - 1;

    // All the nodes matched - return the index of the tail
    return shorterLength - 1;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((impl == null) ? 0 : impl.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof SoPath))
		return false;
	SoPath other = (SoPath) obj;
	if (impl == null) {
		if (other.impl != null)
			return false;
	} else if (impl != other.impl)
		return false;
	return true;
}

public static SoPath cast(SoPath path) {
	if(path == null) {
		return null;
	}
	return new SoPath(path);
}

	   
}
