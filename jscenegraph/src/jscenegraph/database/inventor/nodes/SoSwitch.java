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
 |      This file defines the SoSwitch node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoSwitchElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;


////////////////////////////////////////////////////////////////////////////////
//! Group node that traverse one chosen child.
/*!
\class SoSwitch
\ingroup Nodes
This group node usually traverses only one or none of its children.
It implements an operation similar to the <tt>switch</tt> statement in C.
One can use this node to switch on and off the effects of some
properties or to switch between different properties.


The \b whichChild  field specifies the index of the child to traverse,
where the first child has index 0.


A value of <tt>SO_SWITCH_NONE</tt> (-1, the default) means do not traverse
any children. A value of <tt>SO_SWITCH_INHERIT</tt> (-2) allows the index
to be inherited from a previously-encountered SoSwitch node or
from certain other nodes (such as SoArray or SoMultipleCopy)
that set the switch value. A value of <tt>SO_SWITCH_ALL</tt> (-3) traverses
all children, making the switch behave exactly like a regular
SoGroup.

\par File Format/Default
\par
\code
Switch {
  whichChild -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoHandleEventAction, SoRayPickAction
<BR> Traverses the chosen child or children. 
\par
SoSearchAction
<BR> If the action's Searching-All flag is set, always traverses all children.  Otherwise, traverses just the chosen child or children. 

\par See Also
\par
SoArray, SoLevelOfDetail, SoMultipleCopy, SoPathSwitch
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSwitch extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSwitch.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSwitch.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSwitch.class); }    	  	
	
	
	public static final int SO_SWITCH_NONE  = (-1); /* Don't traverse any children */
	public static final int SO_SWITCH_INHERIT = (-2); /* Inherit value from state */
	public static final int SO_SWITCH_ALL = (-3); /* Traverse all children */
	
	
	public final SoSFInt32 whichChild = new SoSFInt32();
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSwitch()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoSwitch*/);
    nodeHeader.SO_NODE_ADD_SFIELD(whichChild,"whichChild", (SO_SWITCH_NONE));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor that takes approximate number of children.
//
// Use: public

public SoSwitch(int nChildren) { super(nChildren);
//
////////////////////////////////////////////////////////////////////////

    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoSwitch*/);
    nodeHeader.SO_NODE_ADD_SFIELD(whichChild,"whichChild", (SO_SWITCH_NONE));
    isBuiltIn = true;
}

	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Overrides method in SoNode to return FALSE if there is no
	   //    selected child or the selected child does not affect the state.
	   //
	   // Use: public
	   
	   public boolean
	   affectsState()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       int which;
	       if (whichChild.isIgnored())
	           which = SO_SWITCH_NONE;
	       else
	           which = whichChild.getValue();
	   
	       // Special case-- if called during application of an
	       // SoSearchAction that is searching all:
	       if (SoSearchAction.duringSearchAll)
	           which = SO_SWITCH_ALL;
	   
	       if (whichChild.isIgnored() || which == SO_SWITCH_NONE)
	           return false;
	   
	       if (which == SO_SWITCH_ALL ||
	           which == SO_SWITCH_INHERIT)
	           return true;
	   
	       if (getChild(which).affectsState())
	           return true;
	   
	       return false;
	   }
	   	
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Implements search action.
	    //
	    // Use: extender
	    //
	    ////////////////////////////////////////////////////////////////////////
	    
	   public void
	    search(SoSearchAction action)
	    {
	        // if the action is searching everything, then do so...
	        if (action.isSearchingAll())
	            super.search(action);
	    
	        // Otherwise, traverse according to the regular switch node rules
	        else {
	            // First, make sure this node is found if we are searching for
	            // switches
	            SoNode_search(action);
	    
	            // Recurse
	            if (! action.isFound())
	                SoSwitch_doAction(action);
	        }
	    }
	    
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Typical action traversal.
	    //
	    // Use: extender
	    
	   public void
	    SoSwitch_doAction(SoAction action)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        final int[]         numIndices = new int[1];
	        final int[][]   indices = new int[1][];
	    
	        if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH) {
	            int     i;
	            for (i = 0; i < numIndices[0]; i++)
	                doChild(action, indices[0][i]);
	        }
	    
	        else
	            doChild(action);
	    }
	    	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for callback action.
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSwitch_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for GL rendering
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSwitch_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for picking
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSwitch_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for get bounding box
//
// Use: extender

public void getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSwitch_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for handle event
//
// Use: extender

public void handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSwitch_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements getMatrix action.
//
// Use: extender

public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Only need to compute matrix if switch is a node in middle of
    // current path chain or is off path chain (since the only way
    // this could be called if it is off the chain is if the switch is
    // under a group that affects the chain).

    switch (action.getPathCode(numIndices, indices)) {

      case NO_PATH:
      case BELOW_PATH:
        break;

      case OFF_PATH:
      case IN_PATH:
        SoSwitch_doAction(action);
        break;
    }
}
	   
	    ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Traverses correct child based on value of whichChild field. If
	    //    matchIndex is non-negative, the indicated child must match it
	    //    for it to be traversed. Sets switch element in state unless we
	    //    are inheriting the value.
	    //
	    // Use: private
	    
	    public void
	    doChild(SoAction action) {
	    	doChild(action, -1);
	    }
	    
	    public void
	    doChild(SoAction action, int matchIndex)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        int     which;
	    
	        if (whichChild.isIgnored())
	            which = SO_SWITCH_NONE;
	    
	        else
	            which = whichChild.getValue();
	    
	        // If index is inherited from state, get value from there
	        if (which == SO_SWITCH_INHERIT) {
	    
	            which = SoSwitchElement.get(action.getState());
	    
	            // Make sure it is in range
	            if (which >= getNumChildren())
	                which %= getNumChildren();
	        }
	    
	        // Store resulting index in state if not already inherited from there
	        else
	            SoSwitchElement.set(action.getState(), which);
	    
	        // Now we have the real value to deal with
	    
	        switch (which) {
	    
	          case SO_SWITCH_NONE:
	            break;
	    
	          case SO_SWITCH_ALL:
	            // If traversing to compute bounding box, we have to do some
	            // work in between children
	            if (action.isOfType(SoGetBoundingBoxAction.getClassTypeId())) {
	                SoGetBoundingBoxAction bba = (SoGetBoundingBoxAction ) action;
	                final SbVec3f     totalCenter = new SbVec3f(0.0f, 0.0f, 0.0f);
	                int         numCenters = 0;
	                int         lastChild = (matchIndex >= 0 ? matchIndex :
	                                         getNumChildren()  - 1);
	    
	                for (int i = 0; i <= lastChild; i++) {
	                    children.traverse(bba, i, i);
	                    if (bba.isCenterSet()) {
	                        totalCenter.operator_add_equal(bba.getCenter());
	                        numCenters++;
	                        bba.resetCenter();
	                    }
	                }
	                // Now, set the center to be the average:
	                if (numCenters != 0)
	                    bba.setCenter(totalCenter.operator_div((float)numCenters), false);
	            }
	            else {
	                if (matchIndex >= 0)
	                    children.traverse(action, 0, matchIndex);
	                else
	                    children.traverse(action);
	            }
	            break;
	    
	          default:
	    
	            // Make sure index is reasonable
	            if (which < 0 || which >= getNumChildren()) {
//	    #ifdef DEBUG
	                SoDebugError.post("SoSwitch::doChild",
	                                   "Child index "+which+" is out of range 0 - "+(getNumChildren() - 1)+" "+
	                                   "(applying "+action.getTypeId().getName().getString()+")");
//	    #endif /* DEBUG */
	                break;
	            }
	    
	            // Traverse indicated child
	            if (matchIndex < 0 || matchIndex == which)
	                children.traverse(action, (int) which);
	        }
	    }
	    	   
	   
	 ////////////////////////////////////////////////////////////////////////
	  //
	  // Description:
	  //    This initializes the SoSwitch class.
	  //
	  // Use: internal
	  
	 public static void
	  initClass()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
	      SoSubNode.SO__NODE_INIT_CLASS(SoSwitch.class, "Switch", SoGroup.class);
	 
	      //SO_ENABLE(SoCallbackAction, SoSwitchElement);
	      SoCallbackAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoGLRenderAction, SoSwitchElement);
	      SoGLRenderAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoGetBoundingBoxAction, SoSwitchElement);
	      SoGetBoundingBoxAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoGetMatrixAction, SoSwitchElement);
	      SoGetMatrixAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoHandleEventAction, SoSwitchElement);
	      SoHandleEventAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoPickAction, SoSwitchElement);
	      SoPickAction.enableElement(SoSwitchElement.class);
	      //SO_ENABLE(SoSearchAction, SoSwitchElement);
	      SoSearchAction.enableElement(SoSwitchElement.class);
	  }
	  }
