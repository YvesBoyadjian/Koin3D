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
 * Copyright (C) 1990-93   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoSelection node class.
 |
 |   Classes:   SoSelection
 |
 |   Author(s)  : David Mott, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.nodes;

import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.events.SoButtonEvent;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoCallbackList;
import jscenegraph.database.inventor.misc.SoCallbackListCB;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Manages a list of selected objects.
/*!
\class SoSelection
\ingroup General
SoSelection defines a node which can be inserted into a scene graph
and will generate and manage a selection list from picks on any node
in the subgraph below it. Nodes are selected based on a current
selection policy.
Callback functions report back to the application when a path has been
selected or deselected. The selection list can also be managed
programmatically.


When handling events, SoSelection makes sure that the mouse release
event was over the same object as the mouse press event before
changing the list of selected objects. This allows users to mouse
down on an object, change their mind and move the cursor off the object,
then release the mouse button without altering the selection.


The selection can be highlighted automatically through the SoQtRenderArea,
or the application can provide custom highlights. Please see the
chapter "Creating a Selection Highlight Style" in the
<em>Inventor Toolmaker</em>.

\par File Format/Default
\par
\code
Selection {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
  policy SHIFT
}
\endcode

\par See Also
\par
SoEventCallback, SoQtRenderArea, SoBoxHighlightRenderAction, SoLineHighlightRenderAction
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSelection extends SoSeparator {
	
	   private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSelection.class,this);	   	
	
		public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSelection.class); }                   
	    public SoType      getTypeId()       /* Returns type id      */
	    {
			return nodeHeader.getClassTypeId();	
	    }
	  public                                                                  
	    SoFieldData   getFieldData() {
		  return nodeHeader.getFieldData(); 
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSelection.class); }              

    //!
    //! Selection policy
    //! ----------------

    //! Default selection policy is SoSelection.SHIFT.
    public enum Policy {
        //! left mouse pick on object clears selection, then selects object.
        //! left mouse pick on nothing clears selection.
        //! only one object may be selected at a time. 
        SINGLE,
        
        //! left mouse pick on object toggles its selection status.
        //! left mouse pick on nothing does nothing.
        //! multiple objects may be selected.
        TOGGLE,
        
        //! when shift key is down, selection policy is TOGGLE.
        //! when shift key is up, selection policy is SINGLE.
        //! multiple objects may be selected.
        SHIFT;
    	
    	public int getValue() {
    		return ordinal();
    	}
    };

	  
    //! \name Fields
    //@{

    //! Selection policy that is followed in response to user interaction.
    //! This can be set to <tt>SoSelection.SINGLE</tt>, <tt>SoSelection.TOGGLE</tt>, or
    //! <tt>SoSelection.SHIFT</tt>.
    public final SoSFEnum            policy = new SoSFEnum(); 

	  
	   
	  protected
		    //! List of paths of selected objects
		    final SoPathList          selectionList = new SoPathList();

    //! Selection callback lists
    protected SoCallbackList      selCBList;
    protected SoCallbackList      deselCBList;
    protected SoCallbackList      startCBList;
    protected SoCallbackList      finishCBList;
    
    //! Pick filter is a single callback function, not a list
    protected SoSelectionPickCB   pickCBFunc;
    protected Object                pickCBData;
    protected boolean                callPickCBOnlyIfSelectable;
    
    //! Change callbacks
    protected  SoCallbackList      changeCBList;
    
    //! Mouse down picked path
    protected SoPath              mouseDownPickPath;
    protected boolean                pickMatching;
 	
    public void
     addChangeCallback(SoSelectionClassCB f, Object userData)
     {
         if (changeCBList == null)
             changeCBList = new SoCallbackList();
         changeCBList.addCallback(new SoCallbackListCB() {

			@Override
			public void invoke(Object callbackData) {
				f.invoke(userData, (SoSelection)callbackData);
			}
        	 
         }, userData);
     }
    
	 public void
	   removeChangeCallback(SoSelectionClassCB f, Object userData)
	   {
	       if (changeCBList != null)
	           changeCBList.removeCallback(new SoCallbackListCB () {

				@Override
				public void invoke(Object callbackData) {
					f.invoke(userData, (SoSelection)callbackData);
				}
	        	   
	           }, userData);
	   }
	 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: public
//
public SoSelection()
{
    constructorCommon();
}

	 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor initialization
//
// Use: private
//
private void constructorCommon()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoSelection.class*/);
    nodeHeader.SO_NODE_ADD_FIELD(policy,"policy", (SoSelection.Policy.SHIFT.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.SINGLE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.TOGGLE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Policy.SHIFT);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(policy,"policy", "Policy");

    isBuiltIn = true;

    // selection callback functions and user data
    selCBList = null;
    deselCBList = null;
    startCBList = null;
    finishCBList = null;  
    changeCBList = null;
    
    pickCBFunc = null;
    pickCBData = null;
    
    mouseDownPickPath = null;
    pickMatching = true;
}

	 
	 
	 
    //! Return the number of paths in the selection list.
    public int                 getNumSelected() { return selectionList.getLength(); }
	 
	 
//////////////////////////////////////////////////////////////////////////////
//
//  Description:
//    get the ith path.
//
//  Use: public
//
public SoPath getPath(int index)
//
//////////////////////////////////////////////////////////////////////////////
{
    SoPath p;
    
//#ifdef DEBUG
    if ((index >= selectionList.getLength()) || (index < 0)) {
        SoDebugError.post("SoSelection.getPath", "Index out of range.  Index = "+index+", numSelected = "+selectionList.getLength());
        p = null;
    }
    else
//#endif
        p = selectionList.operator_square_bracket(index);
        
    return p;
}

    //! Traversal routine for SoHandleEventAction - this will call
    //! action.setHandled() if the event is handled

////////////////////////////////////////////////////////////////////////
//
//Description:
//Traversal for event handling. The parent class SoSeparator is called
//to traverse the children. If the event is not handled by any children,
//then handle it here by invoking the current selection policy.
//Calls action.setHandled() if the event was handled.
//
//Use: protected

    public void        handleEvent(SoHandleEventAction action) {
    // let SoSeparator traverse the children
    super.handleEvent(action);

    // On Button1 press, see who got picked.
    // On Button1 release, see who got picked.
    // If pick-matching is on, and they match,
    // then select the object that got picked.
    //??? this event should be programmer configurable! translation tables?
    final SoEvent event = action.getEvent();
    if (! event.isOfType(SoMouseButtonEvent.getClassTypeId()))
        return;

    final SoMouseButtonEvent be = ( SoMouseButtonEvent ) event;
    if (be.getButton() != SoMouseButtonEvent.Button.BUTTON1)
        return;

    if ((pickMatching && (be.getState() == SoButtonEvent.State.DOWN)) ||
        (be.getState() == SoButtonEvent.State.UP)) {        
        //??? change the action so we only pick the FIRST thing, not a list?
        final SoPickedPoint pickedPoint = action.getPickedPoint();
        SoPath pickPath = null;
        
        // Get the pick path
        if (pickedPoint != null) {
            // If the pick callback exists, let it tell us what path to pick
            if (pickCBFunc != null) {
                if (callPickCBOnlyIfSelectable) {
                    // Make sure pick passes through this
                    if (pickedPoint.getPath().containsNode(this))
                        pickPath = (pickCBFunc).invoke(pickCBData, pickedPoint);
                }
                else pickPath = (pickCBFunc).invoke(pickCBData, pickedPoint);
            }
            // Else no pick CB - use the picked path as is.
            else pickPath = pickedPoint.getPath();
        }
        
        // For button press, save the pick path whether a child
        // handled the event or not - we may get a crack at the
        // button release event later.
        if (be.getState() == SoButtonEvent.State.DOWN) {
            if (mouseDownPickPath != null)
                mouseDownPickPath.unref(); // out with the old...
                
            mouseDownPickPath = copyFromThis(pickPath);
            
            if (mouseDownPickPath != null) {
                mouseDownPickPath.ref();       // ...in with the new
            }

            // 2005-04-11 Felix: If paths are required to match, handle the action
            // since it will be set as handled on SoButtonEvent.UP and both form an
            // integrated whole from the point of interaction.
            if (pickMatching || (mouseDownPickPath != null)) {
                action.setHandled();
            }
        }
        // Else button release.
        // If no children handled the event during SoSeparator.handleEvent()
        // traversal, handle it here.
        else if (! action.isHandled()) {
            // For button release, get the pick path and see if it matches
            // the button press pick path.
            //
            // If they match, invoke the selection policy.
            // If they do NOT match, do nothing.
            
            if (pickPath == null) {
                // If nothing was picked, pass null to invokeSelectionPolicy.
                if ((! pickMatching) || (mouseDownPickPath == null)) {
                    invokeSelectionPolicy(null, be.wasShiftDown());
                    action.setHandled();
                }
                // else paths do not match - ignore event
            }
            else {
                pickPath.ref();
                
                if ((! pickMatching) || (mouseDownPickPath != null)) {
                    // Mouse down pick hit something.
                    // Was it same as mouse up pick?
                    // See if the pick path passes through this node
                    SoPath mouseUpPickPath = copyFromThis(pickPath);
                    if (mouseUpPickPath != null) {
                        mouseUpPickPath.ref();
                    
                        // If paths match, invoke the selection policy. 
                        if ((! pickMatching) ||
                            (mouseDownPickPath.operator_equals(mouseUpPickPath))) {
                            if (mouseUpPickPath.getLength() == 1) {
                                // Path is to ONLY the selection node.
                                // Act as if nothing were picked, but
                                // allow traversal to continue.
                                invokeSelectionPolicy(null, be.wasShiftDown());
                            }
                            else {
                                // Alter selection and halt traversal.
                                invokeSelectionPolicy(mouseUpPickPath,
                                    be.wasShiftDown());
                                action.setHandled();
                            }
                        }
                        // else paths do not match - ignore event
                        
                        mouseUpPickPath.unref();
                    }
                    // else path does not pass through this node - ignore event
                }
                // else paths do not match - ignore event
                
                pickPath.unref();
            }
            
            // All done with mouse down pick path
            if (mouseDownPickPath != null) {
                mouseDownPickPath.unref();
                mouseDownPickPath = null;
            }
        }
    }
    	
    }
    
	 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Find 'this' in the path, then copy the path from 'this' down to
// the path tail. If 'this' is not in the path, return null.
//
// Use: private
//
private SoPath copyFromThis(final SoPath path) 
{
    if (path == null)
        return null;
        
    SoNode node;
    int i, indexToThis = -1;
    SoPath p = null;

    SoFullPath fullInPath = SoFullPath.cast(path);

    for (i = 0; i < fullInPath.getLength(); i++) {
        node = fullInPath.getNode(i);
        if (node == (SoNode ) this) {
            indexToThis = i;
            break;
        }
    }
    
    if (indexToThis != -1) {
        p = fullInPath.copy(indexToThis);
    }
    
    return p;
}

    //! Selection policies (shiftSelection is a combination of these)
    //! invokeSelectionPolicy() is called from handleEvent() - for a customized
    //! selection policy, either derive a class and write a new handleEvent()
    //! method, or use an SoEventCallback node placed such that it receives
    //! events before the selection node.
////////////////////////////////////////////////////////////////////////
//
//Description:
//invoke the appropriate routine which implements the current
//selection policy. 
//
//Use: protected
//
    public void                invokeSelectionPolicy(SoPath path, boolean shiftDown) {
        // employ the current selection policy
    switch (Policy.values()[policy.getValue()]) {
        case SINGLE:
            performSingleSelection(path);
            break;
        case TOGGLE:
            performToggleSelection(path);
            break;
        case SHIFT:
            if (shiftDown)
                 performToggleSelection(path); // SHIFT DOWN
            else performSingleSelection(path); // NO SHIFT
            break;
        default:
//#ifdef DEBUG
            SoDebugError.post("SoSelection::invokeSelectionPolicy",
            "Unknown selection policy "+ policy.getValue());
//#endif
            break;
    }
    	
    }
	 
	 
////////////////////////////////////////////////////////////////////////
//
// Description:
//   clear the selection, then select the picked object.
//   if no object is picked, then nothing is selected.
//   only one object may be selected at a time. The passed path should have
//   already been copied (i.e. we just store it!)
//
// Use: protected
//
public void performSingleSelection(SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    boolean needFinishCB = false;
    
    // let app know (if and only if) user is changing the selection
    if ((getNumSelected() > 0) || (path != null)) {
        if (startCBList != null)
            startCBList.invokeCallbacks(this);
        needFinishCB = true;
    }

    if (path == null) {
        deselectAll();
    }
    else {
        if (isSelected(path)) {
            // Deselect everything except the given path.
            // This avoids deselecting and then selecting the same thing.
            int which = selectionList.findPath(path);
            if (which != -1) {
                for (int i = selectionList.getLength() - 1; i >= 0; i--) {
                    if (i != which)
                        removePath(i);
                }
            }
        }
        else {
            deselectAll();
            addPath(path);
        }
    }
    
    // let app know user is done changing the selection
    if (needFinishCB) {
        if (finishCBList != null)
            finishCBList.invokeCallbacks(this);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Add path to the selection list. It is assumed that path.head
// is this - DO NOT CALL THIS FUNCTION unless this is true. Also, the
// path must include more than just the selection node by itself.
//
// Use: private
//
private void addPath(SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    // Add the path to the selection list,
    // and notify the app that this path has been selected
    if (selectionList.findPath(path) == -1) {
        path.ref();
        selectionList.append(path);
        if (selCBList != null)
            selCBList.invokeCallbacks(path);
        path.unref();
        
        if (changeCBList != null)
            changeCBList.invokeCallbacks(this);
    }
}


//////////////////////////////////////////////////////////////////////////////
//
//  returns TRUE if the path is already selected
//
//  Use: public
//
public boolean isSelected(final SoPath path)
//
//////////////////////////////////////////////////////////////////////////////
{
//??? should we copyFromThis() as a convenience?
    return (findPath(path) != -1);
}


//////////////////////////////////////////////////////////////////////////////
//
//  returns the index of the path in the selection list
//
//  Use: protected
//
protected int findPath(final SoPath path)
//
//////////////////////////////////////////////////////////////////////////////
{
    int index = -1;
    if (path != null) {
        SoPath selPath = null;
        if (path.getHead() != (SoNode ) this)
             selPath = copyFromThis(path);
        else selPath = (SoPath ) path; // const cast away
        
        // selPath still not null? (copyFromThis() might have returned null)
        if (selPath != null) {
            selPath.ref();
            index = ((SoPathList)selectionList).findPath(selPath);
            selPath.unref();
        }
    }
    
    return index;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    removes every path in the selection list
//
// Use: public
public void deselectAll()
//
////////////////////////////////////////////////////////////////////////
{
    for(int i = selectionList.getLength() - 1; i >= 0; i--)
        deselect(i);
}


//////////////////////////////////////////////////////////////////////////////
//
//  This method takes the path indexed by `which' out of the selection list.
//  The path will be unreferenced by the SoPathList (SoBaseList) class.
//
//  Use: public
//
//
public void deselect(int which)
//
//////////////////////////////////////////////////////////////////////////////
{
    removePath(which);
}


//////////////////////////////////////////////////////////////////////////////
//
//  This method takes the path indexed by `which' out of the selection list.
//  The path will be unreferenced by the SoPathList (SoBaseList) class.
//
//  Use: protected
//
//
protected void removePath(int which)
//
//////////////////////////////////////////////////////////////////////////////
{
    // Remove the path from the selection list,
    // and notify the app that this path has been deselected.
    if (which >= 0) {
        SoPath p = (SoPath ) selectionList.operator_square_bracket(which);
        p.ref();
        selectionList.remove(which);
        if (deselCBList != null)
            deselCBList.invokeCallbacks(p);
        p.unref();     
        
        if (changeCBList != null)
            changeCBList.invokeCallbacks(this);
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//   picking on an object toggles that objects selection status.
//   multiple objects may be selected. The passed path should have
//   already been copied (i.e. we just store it!)
//
// Use: protected
//
public void performToggleSelection(SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    if (path != null) {
        // let app know user is changing the selection
        if (startCBList != null)
            startCBList.invokeCallbacks(this);
        
        // toggle the picked object
        int which = findPath(path);
        if (which == -1)
             addPath(path);     // path not in list, add it
        else removePath(which); // path in list, remove it

        // let app know user is done changing the selection
        if (finishCBList != null)
            finishCBList.invokeCallbacks(this);
    }
}

    //! The selection callbacks are invoked every time an object is selected, whether
    //! it be from user interaction or from method call.
    //! The callbacks are invoked after the object has been added to the selection list.
public void    addSelectionCallback(SoSelectionPathCB f) {
	addSelectionCallback(f, null);
}
    public void    addSelectionCallback(SoSelectionPathCB f, Object userData) {
    if (selCBList == null)
        selCBList = new SoCallbackList();
    selCBList.addCallback((callbackData)->f.invoke(userData, (SoPath)callbackData), userData);    	
    }
    //! Remove selection callback.
    public void    removeSelectionCallback(SoSelectionPathCB f) {
    	removeSelectionCallback(f, null);    	
    }
    public void    removeSelectionCallback(SoSelectionPathCB f, Object userData) {
    if (selCBList != null)
        selCBList.removeCallback((SoCallbackListCB ) f, userData);
    	
    }

    //! The deselection callbacks are invoked every time an object is deselected, whether
    //! it be from user interaction or from method call.
    //! This is invoked after the object has been removed from the selection list.
    public void    addDeselectionCallback(SoSelectionPathCB f) {
    	addDeselectionCallback(f, null);    	
    }
    public void    addDeselectionCallback(SoSelectionPathCB f, Object userData) {
    if (deselCBList == null)
        deselCBList = new SoCallbackList();
    deselCBList.addCallback((callbackData)->f.invoke(userData, (SoPath)callbackData), userData);    	
    }
    //! Remove deselection callback.
    public void    removeDeselectionCallback(SoSelectionPathCB f) {
    	removeDeselectionCallback(f, null);
    }
    public void    removeDeselectionCallback(SoSelectionPathCB f, Object userData) {
    	    if (deselCBList != null)
        deselCBList.removeCallback((SoCallbackListCB ) f, userData);    	
    }


	 
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoSelection class.
	   //
	   // Use: private
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
		  SoSubNode.SO__NODE_INIT_CLASS(SoSelection.class, "Selection", SoSeparator.class);
	   }	 
}
