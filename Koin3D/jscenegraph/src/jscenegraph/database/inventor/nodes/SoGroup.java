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
 |      This file defines the SoGroup node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Base class for all group nodes.
/*!
\class SoGroup
\ingroup Nodes
This node defines the base class for all group nodes. SoGroup is a
node that contains an ordered list of child nodes. The ordering of the
child nodes represents the traversal order for all operations (for
example, rendering, picking, and so on). This node is simply a
container for the child nodes and does not alter the traversal state
in any way. During traversal, state accumulated for a child is passed
on to each successive child and then to the parents of the group
(SoGroup does not push or pop traversal state as SoSeparator
does).

\par File Format/Default
\par
\code
Group {
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoHandleEventAction, SoRayPickAction
<BR> Traverses each child in order. 
\par
SoGetMatrixAction
<BR> Does nothing unless the group is in the middle of the path chain the action is being applied to. If so, the children up to and including the next node in the chain are traversed. 
\par
SoSearchAction
<BR> If searching for group nodes, compares with this group. Otherwise, continues to search children. 
\par
SoWriteAction
<BR> Writes out the group node. This method also deals with any field data associated with the group node. As a result, this method is used for most subclasses of SoGroup as well. 

\par See Also
\par
SoArray, SoLevelOfDetail, SoMultipleCopy, SoPathSwitch, SoSeparator, SoSwitch
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGroup extends SoNode implements Destroyable {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoGroup.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoGroup.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoGroup.class); }              

	protected SoChildList children;
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Default constructor
	    //
	    // Use: public
	    
	   public SoGroup()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        children = new SoChildList(this);
	        nodeHeader.SO_NODE_CONSTRUCTOR();
	        isBuiltIn = true;
	    }
	    
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Constructor that takes approximate number of children.
	    //
	    // Use: public
	    
	   public SoGroup(int nChildren)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        children = new SoChildList(this, nChildren);
	        nodeHeader.SO_NODE_CONSTRUCTOR();
	        isBuiltIn = true;
	    }
	   
	   public void destructor() {
		   children.destructor();
	   }
	    	   	
	// Adds a child as last one in group.
	public void addChild(SoNode child) {
		
		 //#ifdef DEBUG 
		   if (child == null) {
		   SoDebugError.post("SoGroup::addChild", "NULL child node");
		   return;
		   }
		 //#endif /* DEBUG */
		  
		   children.append(child);
		 	}
	
////////////////////////////////////////////////////////////////////////
//
//Description:
//This inserts a child into a group so that it will have the given
//index.
//
//Use: public

	public void insertChild(SoNode child, int newChildIndex) {
		
//		 #ifdef DEBUG
		       if (child == null) {
		           SoDebugError.post("SoGroup::insertChild", "NULL child node");
		           return;
		       }
		   
		       // Make sure index is reasonable
		       if (newChildIndex < 0 || newChildIndex > getNumChildren()) {
		           SoDebugError.post("SoGroup::insertChild",
		                              "Index "+newChildIndex+" is out of range "+0+" - "+getNumChildren());
		           return;
		       }
//		   #endif /* DEBUG */
		   
		       // See if adding at end
		       if (newChildIndex >= getNumChildren())
		           children.append(child);
		   
		       else
		           children.insert(child, newChildIndex);
		  	}
	
	
	
	// Returns pointer to child node with the given index.
	public SoNode getChild(int index) {
		return children.operator_square_bracket(index);
	}
	
	/**
	 * Finds index of given child within group. 
	 * Returns -1 if not found. 
	 * 
	 * @param child
	 * @return
	 */
	
	public int findChild(final SoNode child) {
		
	     int i, num;
	      
	          num = getNumChildren();
	      
	          for (i = 0; i < num; i++)
	              if (getChild(i) == child) return(i);
	      
	          return(-1);
	     	}
	
	// Returns number of children.
	public int getNumChildren() {
		return children.getLength();
	}
	
	// Removes child with given index from group. 
	public void removeChild(int index) {
		
//		 #ifdef DEBUG
//		       if (index < 0 || index >= getNumChildren()) {
//		           SoDebugError::post("SoGroup::removeChild",
//		                              "Index %d is out of range %d - %d",
//		                              index, 0, getNumChildren() - 1);
//		           return;
//		       }
//		   #endif /* DEBUG */
		   
		       // Play it safe anyway...
		       if (index >= 0) {
		           children.remove(index);
		       }
		  	}
	
	// Removes first instance of given child from group. 
	public void removeChild(SoNode child) {
		 removeChild(findChild(child)); 
	}
	
	// Removes all children from group. 
	public void removeAllChildren() {
		   children.truncate(0);
	}
	
	// Replaces child with given index with new child. 
	public void replaceChild(int index, SoNode newChild) {
		
//		 #ifdef DEBUG
		       if (index < 0 || index >= getNumChildren()) {
		           SoDebugError.post("SoGroup::replaceChild",
		                              "Index "+index+" is out of range 0 - "+(getNumChildren() - 1));
		           return;
		       }
//		   #endif /* DEBUG */
		   
		       // Play it safe anyway...
		       if (index >= 0)
		           children.set(index, newChild);
		   	}
	
	// Replaces first instance of given child with new child. 
	public void replaceChild(SoNode oldChild, SoNode newChild) {
		 replaceChild(findChild(oldChild), newChild); 
	}
	
	// Returns pointer to children. 
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns pointer to children.
	   //
	   // Use: internal
	   
	   public SoChildList getChildren()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       return children;
	   }
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads stuff into instance of SoGroup. Returns FALSE on error.
//    This also deals with field data (if any), so this method should
//    be useful for most subclasses of SoGroup as well.
//
// Use: protected

public boolean readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    boolean readOK = true;

    // First, turn off notification for this node
    boolean saveNotify = enableNotify(false);

    // Read field info. We can't just call
    // SoFieldContainer::readInstance() to read the fields here
    // because we need to tell the SoFieldData that it's ok if a name
    // is found that is not a valid field name - it could be the name
    // of a child node.
    final boolean[] notBuiltIn = new boolean[1]; // Not used
    readOK = getFieldData().read(in, this, false, notBuiltIn);
    if (!readOK) return readOK;

    // If binary BUT was written without children (which can happen
    // if it was read as an unknown node and then written out in
    // binary), don't try to read children:
    if (!in.isBinary() || (flags & SoBase.BaseFlags.IS_GROUP.getValue()) != 0) 
        readOK = readChildren(in);
    
    // Re-enable notification
    enableNotify(saveNotify);

    return readOK;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads children into instance of SoGroup. Returns FALSE on error.
//
// Use: protected

public boolean readChildren(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    final SoBase[]      base = new SoBase[1];
    boolean        ret = true;

    // If reading binary, read number of children first
    if (in.isBinary()) {

        final int[]     numToRead = new int[1];
        int i;

        if (!in.read(numToRead))
            ret = false;

        else {
            for (i = 0; i < numToRead[0]; i++) {

                if (SoBase.read(in, base, SoNode.getClassTypeId()) &&
                    base[0] != null)
                    addChild((SoNode ) base[0]);

                // Running out of children is now an error, since the
                // number of children in the file must be exact
                else {
                    ret = false;
                    break;
                }
            }
            // If we are reading a 1.0 file, read the GROUP_END_MARKER
            if (ret && in.getIVVersion() == 1.0f) {
                final int GROUP_END_MARKER = -1;
                final int[] marker = new int[1];

                // Read end marker if it is there. If not, some sort of
                // error occurred.
                if (! in.read(marker) || marker[0] != GROUP_END_MARKER)
                    ret = false;
            }
        }
    }

    // ASCII: Read children until none left. Deal with children
    // causing errors by adding them as is.
    else {
        while (true) {
            ret = SoBase.read(in, base, SoNode.getClassTypeId()) && ret;

            // Add child, even if error occurred, unless there is no
            // child to add.
            if (base[0] != null)
                addChild((SoNode ) base[0]);

            // Stop when we run out of valid children
            else
                break;
        }
    }

    return ret;
}

	   
	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Implements typical traversal.
	    //
	    // Use: extender
	    
	   public void
	    SoGroup_doAction(SoAction action)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        final int[]         numIndices = new int[1];
	        final int[][]   indices = new int[1][];
	    
	        if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
	            children.traverse(action, 0, indices[0][numIndices[0] - 1]);
	    
	        else
	            children.traverse(action);
	    }
	    	   
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoGroup_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the GL render action
//
// Use: extender

public void
GLRender(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    final int[] numIndices = new int[1];
    final int[][] indices = new int[1][];
    SoAction.PathCode pc = action.getPathCode(numIndices, indices);

    // Perform fast-path GLRender traversal:
    if (pc != SoAction.PathCode.IN_PATH) {
        action.pushCurPath();
        for (int i = 0; i < children.getLength(); i++) {

            action.popPushCurPath(i);
            if (! action.abortNow())
                ((SoNode)(children.get(i))).GLRender(action);
            else
                SoCacheElement.invalidate(action.getState());

            // Stop if action has reached termination condition. (For
            // example, search has found what it was looking for, or event
            // was handled.)
            if (action.hasTerminated())
                break;
        }
        action.popCurPath();
    }

    else {

        // This is the same as SoChildList::traverse(), except that it
        // checks render abort for each child

        int lastChild = indices[0][numIndices[0] - 1];
        for (int i = 0; i <= lastChild; i++) {

            SoNode child = (SoNode ) children.get(i);

            if (pc == SoAction.PathCode.OFF_PATH && ! child.affectsState())
                continue;

            action.pushCurPath(i);
            if (action.getCurPathCode() != SoAction.PathCode.OFF_PATH ||
                child.affectsState()) {

                if (! action.abortNow())
                    child.GLRender(action);
                else
                    SoCacheElement.invalidate(action.getState());
            }

            action.popCurPath(pc);

            if (action.hasTerminated())
                break;
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the get bounding box action.  This takes care of averaging
//    the centers of all children to get a combined center.
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     totalCenter = new SbVec3f(0,0,0);
    int         numCenters = 0;
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild;

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        lastChild = indices[0][numIndices[0] - 1];
    else
        lastChild = getNumChildren() - 1;

    for (int i = 0; i <= lastChild; i++) {
        children.traverse(action, i, i);
        if (action.isCenterSet()) {
            totalCenter.operator_add_equal(action.getCenter());
            numCenters++;
            action.resetCenter();
        }
    }
    // Now, set the center to be the average. Don't re-transform the
    // average, which should already be transformed.
    if (numCenters != 0)
        action.setCenter(totalCenter.operator_div(numCenters), false);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the handle event thing
//
// Use: extender

public void
handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoGroup_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pick.
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoGroup_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does search...
//
// Use: extender

public void
search(SoSearchAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // First see if the caller is searching for this node
    super.search(action);

    // Then recurse on children if not already found
    if (! action.isFound())
        SoGroup_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements get matrix action.
//
// Use: extender

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Only need to compute matrix if group is a node in middle of
    // current path chain or is off path chain (since the only way
    // this could be called if it is off the chain is if the group is
    // under a group that affects the chain).

    switch (action.getPathCode(numIndices, indices)) {

      case NO_PATH:
        break;

      case IN_PATH:
        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
        break;

      case BELOW_PATH:
        break;

      case OFF_PATH:
        children.traverse(action);
        break;
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies the contents of the given node into this instance.
//
// Use: protected, virtual

public void
copyContents(final SoFieldContainer fromFC, boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    // Copy the usual stuff
    super.copyContents(fromFC, copyConnections);

    // Copy the kids
    final SoGroup fromGroup = ( SoGroup ) fromFC;
    for (int i = 0; i < fromGroup.getNumChildren(); i++) {

        // If this node is being copied, it must be "inside" (see
        // SoNode::copy() for details.) Therefore, all of its children
        // must be inside, as well.
        SoNode fromKid = fromGroup.getChild(i);
        SoNode kidCopy = (SoNode ) findCopy(fromKid, copyConnections);

//#ifdef DEBUG
        if (kidCopy == null)
            SoDebugError.post("(internal) SoGroup::copyContents",
                               "Child "+i+" has not been copied yet");
//#endif /* DEBUG */

        addChild(kidCopy);
    }
}
	   
	   
	    	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    This initializes the SoGroup class.
	    //
	    // Use: internal
	    
	   public static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SoSubNode.SO__NODE_INIT_CLASS(SoGroup.class, "Group", SoNode.class);
	    }
	    
	   }
