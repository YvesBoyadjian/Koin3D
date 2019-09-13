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
 |      Defines the SoHandleEventAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPickedPointList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.elements.SoWindowElement;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Allows nodes in a graph to receive input events.
/*!
\class SoHandleEventAction
\ingroup Actions
This class is used to allow nodes in a scene graph to handle input events.
It is usually invoked from a
component derived from SoQtRenderArea when the component receives a window
system event.


Manipulator, dragger and selection nodes respond to and process
events. Most other group nodes just pass the event to their children,
while most other nodes simply ignore the action entirely. Once a node
has indicated to the action that it has handled the event, traversal
stops.


A node that handles an event can also grab future events. Once it has
done so, all events will be sent directly to that node, with no
traversal taking place, until the node releases the grab.

\par See Also
\par
SoEvent, SoPickedPoint, SoRayPickAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoHandleEventAction extends SoAction implements Destroyable {
	
	public SoType getTypeId() {
		return classTypeId;
	}
    public static SoType getClassTypeId()                              
                                    { return classTypeId; }                   
    public static void addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
    // java port
    public  static void                 enableElement(Class<?> klass)         
    { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
    
    public static void enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
    protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
    private static SoType               classTypeId	;

	private SoEvent event;
    private SoNode              pickRoot;      //!< Root node for initiating picking
    private  SoPickedPoint      pickedPoint;   //!< PickedPoint from last pick
    private  boolean              pickValid;      //!< Whether last pick is still valid
    private  boolean              usedPickAll;    //!< TRUE if last pick used pickAll=TRUE
     private SoRayPickAction     pickAct;       //!< Pick action
    private SoNode              eventGrabber;  //!< Event grabber - gets all events
      private final SbViewportRegion    vpRegion = new SbViewportRegion();       //!< Current viewport region
  	
    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Constructor.
     //
     // Use: public
     
     public SoHandleEventAction(final SbViewportRegion region)
     //
     ////////////////////////////////////////////////////////////////////////
     {
         //SO_ACTION_CONSTRUCTOR(SoHandleEventAction);
         traversalMethods = methods;
        		           
     
         event           = null;
         pickRoot        = null;
         pickedPoint     = null;
         pickValid       = false;
         usedPickAll     = false;
         pickAct         = new SoRayPickAction(region);
         eventGrabber    = null;
         vpRegion.copyFrom(region);
     
         // Assume that we need to find only the closest object along the
         // ray. If the user requests all objects (by calling
         // getPickedPointList()), we will re-pick after calling
         // setPickAll(TRUE).
         pickAct.setPickAll(false);
     }
     
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (pickRoot != null)
        pickRoot.unref();

    pickAct.destructor();
    super.destructor();
}

     
     
     ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets the viewport region.  This should be called when the window
	   //    changes size.
	   //
	   // Use: public
	  public void
	   setViewportRegion(final SbViewportRegion newRegion)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       vpRegion.copyFrom(newRegion);
	       pickAct.setViewportRegion(newRegion);
	   }	
	
    //! Sets/returns the event being handled.
    public void setEvent(final SoEvent ev)     { event = ev; }
 
	public SoEvent getEvent() {
		return event;
	}
	
    //! Sets/returns whether any node has yet handled the event.
    public void                setHandled()            { setTerminated(true); }
	
	// Sets/returns whether any node has yet handled the event. 
	public boolean isHandled() {
		 return hasTerminated(); 
	}
	
    //! Initiates grabbing of future events. All events will be sent to the
    //! given node until the grab is released.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Set a node to be the current event grabber, or turn off event
//grabbing.
//
//Use: public

    public void                setGrabber(SoNode node) {
    // inform the previous grabber that it's no longer grabbing
    if (eventGrabber != null)
        eventGrabber.grabEventsCleanup();

    eventGrabber = node;

    // inform the new node that it's now grabbing
    if (eventGrabber != null)
        eventGrabber.grabEventsSetup();    	
    }
    //! Releases the grab.
    public void                releaseGrabber()            { setGrabber(null); }
    //! Returns the node that is currently grabbing events, or NULL if there is none.
    public SoNode             getGrabber()           { return eventGrabber; }

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the root node used for initiating a pick action for those
//    nodes that want to know who is under the cursor.
//
// Use: public

public void
setPickRoot(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    if (node != null)
        node.ref();

    if (pickRoot != null)
        pickRoot.unref();

    pickRoot = node;

    // Previous pick info is no longer valid
    pickValid = false;
}


	
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns the object hit (as an SoPickedPoint) by performing a
	   //    pick based on the current mouse location as specified in the
	   //    event the action is being applied for. This initiates a pick
	   //    action if necessary to find this object.
	   //
	   // Use: public
	   
	  public SoPickedPoint 
	   getPickedPoint()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	        SoPath        pathAppliedTo;
	   
	       // Re-use previous pickedPoint if it's still valid
	       if (pickValid)
	           return pickedPoint;
	   
	       // Otherwise, we have to do the pick...
	   
//	   #ifdef DEBUG
//	       if (event == NULL) {
//	           SoDebugError::post("SoHandleEventAction::getPickedPoint",
//	                              "Event is NULL");
//	           return NULL;
//	       }
//	   
//	       if (pickRoot == NULL) {
//	           SoDebugError::post("SoHandleEventAction::getPickedPoint",
//	                              "Picking root node is NULL");
//	           return NULL;
//	       }
//	   #endif /* DEBUG */
	   
	       // Set the pick region, using the raw X position and the windowSize 
	       // The radius of the region, in pixels, will be 5.0
	       pickAct.setPoint(event.getPosition());
	       pickAct.setRadius(5.0f);
	   
	       // If the action is being applied to a node or pathList, then
	       // apply to the pickRoot
	       if (getWhatAppliedTo() != AppliedCode.PATH)
	           pickAct.apply(pickRoot);
	   
	       // If the action is applied to a path, use the part of the path
	       // from the pickRoot on down. If the pickRoot does not appear in
	       // the path being applied to, just apply to the pickRoot node.
	       else {
	   
	           pathAppliedTo = getPathAppliedTo();
	   
	           // If pickRoot is head of path, just apply action to path
	           if (pathAppliedTo.getHead() == pickRoot)
	               pickAct.apply( (SoPath ) pathAppliedTo);
	   
	           else {
	               int i;
	   
	               // Search for pickRoot in path
	               for (i = 1; i < pathAppliedTo.getLength(); i++)
	                   if (pathAppliedTo.getNode(i) == pickRoot)
	                       break;
	   
	               // If found, construct a path from the pickRoot on down
	               if (i < pathAppliedTo.getLength()) {
	   
	                   // Copy path starting at node i (the pickRoot)
	                   SoPath  newPath = pathAppliedTo.copy(i);
	                   newPath.ref();
	   
	                   pickAct.apply(newPath);
	   
	                   newPath.unref();
	               }
	   
	               // If not found, just apply to root
	               else
	                   pickAct.apply(pickRoot);
	           }
	       }
	   
	       // The returned hit is the first one in the list
	       pickedPoint = pickAct.getPickedPoint();
	   
	       pickValid = true;
	       usedPickAll = pickAct.isPickAll();
	   
	       return pickedPoint;
	   }
	   	

////////////////////////////////////////////////////////////////////////
//
// Description:
//      Return a list of objects hit, sorted from front to back.
//
// Use: public
//
public SoPickedPointList 
getPickedPointList()
//
////////////////////////////////////////////////////////////////////////
{
    // The user obviously wants all objects along the pick ray, so we
    // have to set the pickAll flag in the pick action.

    // If the last pick we did is valid and already set pickAll to
    // TRUE, then we don't need to repick. Otherwise, we do.
    if (! pickValid || ! usedPickAll) {

        pickAct.setPickAll(true);

        // Make sure the pick always gets done
        pickValid = false;

        // Pick as usual, building the list of picked points
        getPickedPoint();

        // Reset this to FALSE, which is the default value
        pickAct.setPickAll(false);
    }

    return pickAct.getPickedPointList();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

protected void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    setPickRoot(node);

    SoViewportRegionElement.set(state, vpRegion);

    // if a node is grabbing, pass the event directly to it.
    if (eventGrabber != null)
        traverse(eventGrabber);

    // if no grabber or if the grabber declined to handle, pass the event
    // to the scene graph.
    if (! isHandled())
        traverse(node);
}

//! Sets/returns current viewport region to use for action.
public SbViewportRegion getViewportRegion() { return vpRegion; }




	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoHandleEventAction class.
	   //
	   // Use: internal
	   
	  public static  void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoHandleEventAction, SoAction);
	       enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
	       methods = new SoActionMethodList(SoAction.methods);                   
	       classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
	                                           new SbName("SoHandleEventAction"), null);	       
	   
	       //SO_ENABLE(SoHandleEventAction, SoViewportRegionElement);
	       SoHandleEventAction.enableElement(SoViewportRegionElement.class);

	       //SO_ENABLE(SoHandleEventAction, SoWindowElement);
	       SoHandleEventAction.enableElement(SoWindowElement.class);

	   }
	   }
