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
 |      SoEventCallback node class - invokes callbacks when the handle
 |   event action is applied to this node.
 |
 |   Author(s): David Mott
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Node which invokes callbacks for events.
/*!
\class SoEventCallback
\ingroup General
SoEventCallback will invoke application supplied
callback functions during SoHandleEventAction traversal.
Methods allow the application to specify which Inventor events should
trigger callbacks, and which path must be picked, if any, for the callback
invocation to occur. The application callback is able to get information about
the event and the pick detail, and may grab events, release events, and set
whether the event was handled.


If you register more than one callback
function in an SoEventCallback node, all the callback functions
will be invoked when an event occurs, even if one of the callbacks handles
the event. However, if the event is handled by any of the callback functions,
no other node in the scene graph will see the event.

\par See Also
\par
SoInteraction, SoSelection, SoHandleEventAction, SoDragger
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoEventCallback extends SoNode implements Destroyable {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoEventCallback.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoEventCallback.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoEventCallback.class); }              

	
	private class SoEventCallbackData implements Destroyable {
		public final SoType eventType = new SoType();
		public SoEventCallbackCB func;
		public Object userData;
		@Override
		public void destructor() {
			func = null;
			userData = null;
		}
	}
	
    //! Only invoke callbacks if this path was picked.
      //! If path is NULL, always invoke callbacks.
    private  SoPath              pathOfInterest;
  
     //! List of callback functions, event types, and user data.
 	private SbPList cblist;
	
    //! This is set for each SoHandleEventAction traversal of this node
    //! so that the apps callback routine can invoke methods on the action.
 	private  SoHandleEventAction eventAction;

	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Constructor
	    //
	  public  SoEventCallback()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        nodeHeader.SO_NODE_CONSTRUCTOR(/*SoEventCallback.class*/);
		  		 	    
	        isBuiltIn        = true;
	        cblist           = new SbPList();
	        pathOfInterest   = null;
	        eventAction      = null;
	    }
	    
	    ////////////////////////////////////////////////////////////////////////
	    //
	    // Destructor
	    //
	    public void destructor()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        if (pathOfInterest != null)
	            pathOfInterest.unref();
	    
	        // delete the data elements, and delete the callback list
	        for (int i = 0; i < cblist.getLength(); i++)
					((SoEventCallbackData )cblist.operator_square_bracket(i)).destructor();
				cblist.destructor();
				super.destructor();
	    }
	   	
	
	/**
	 * Specifies the callback functions to be invoked for different event types. 
	 * When invoked, the callback function will be passed the userData, along with 
	 * a pointer to this SoEventCallback node. 
	 * For example, passing SoMouseButtonEvent::getClassTypeId() means callbacks 
	 * will be invoked only when a mouse button is pressed or released. 
	 * Passing SoEvent::getClassTypeId() for the eventType will cause the callback 
	 * to be invoked for every event which passes through this event callback node. 
	 * 
	 * @param eventType
	 * @param f
	 * @param userData
	 */
	 //
	 // Pay attention to the passed type of event, calling f whenever
	 // and event of the passed type occurs. userData will be passed to f.
	 //
	    /**
	     * java port
	     * @param eventType
	     * @param f
	     * @param userData
	     */
		public void addEventCallback(Class eventType, SoEventCallbackCB f) {
			addEventCallback(SoType.getClassTypeId(eventType),f, null);
		}
	 	public void addEventCallback(Class eventType, SoEventCallbackCB f, Object userData) {
	 		addEventCallback(SoType.getClassTypeId(eventType),f, userData);
	 	}
	 	public void addEventCallback(SoType eventType, SoEventCallbackCB f, Object userData) {
		
	 		  SoEventCallbackData data = new SoEventCallbackData();
	 		   data.eventType.copyFrom(eventType);
	 		   data.func = f;
	 		   data.userData = userData;
	 		   cblist.append(data);
	 		 	}
	 	
	 	// Removes the given callback function. 
	 	 //
	 	   // No longer pay attention to the passed type of event for the function f.
	 	   //
	 	  public  void
	 	   removeEventCallback(
	 	       SoType eventType,
	 	       SoEventCallbackCB f,
	 	       Object userData)
	 	   //
	 	   {
	 	       // loop from the end of the list, in case we remove items
	 	       for (int i = cblist.getLength() - 1; i >= 0; i--) {
	 	           SoEventCallbackData data = (SoEventCallbackData ) cblist.operator_square_bracket(i);
	 	           if ((data.eventType.operator_equal_equal(eventType)) &&
	 	               (data.func == f) &&
	 	               (data.userData == userData)) {
	 	               
	 	               // found the func/event type/data triplet - remove it!
	 	               cblist.remove(i);
	 	               //delete data;
	 	               data.destructor();
	 	           }
	 	       }
	 	   }
	 	   

////////////////////////////////////////////////////////////////////////
//
// This copies the passed path into pathOfInterest.
//
// Use: public
//
public void
setPath(SoPath path)
{
    // ref the input path
    if (path != null)
        path.ref();

    // nuke the old path
    if (pathOfInterest != null) {
        pathOfInterest.unref();
        pathOfInterest = null;
    }
    // and copy the new path
    if (path != null) {
        pathOfInterest = path.copy();
        pathOfInterest.ref();
    }

    // unref the input path
    if (path != null)
        path.unref();

}

	 	  	 	
	
	/**
	 * Returns the event currently being handled, or NULL if traversal 
	 * is not taking place. 
	 * This should be called only from callback functions. 
	 * 
	 * @return
	 */
	public SoEvent getEvent() {
		return (eventAction != null ? eventAction.getEvent() : null); 
	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Invoke all callback funcs interested in the passed event.
	   //
	   // Use: private
	   //
	   private void
	   invokeCallbacks( SoEvent e)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // Call all callback funcs interested in this event type
	       for (int i = 0; i < cblist.getLength(); i++) {
	           SoEventCallbackData data = (SoEventCallbackData ) cblist.operator_square_bracket(i);
	           if (e.isOfType(data.eventType)) {
	               data.func.run (data.userData, this);
	           }
	       }
	   }
	   	
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // This is called for every SoHandleEventAction traversal.
	    //
	    // Use: protected
	    //
	   protected void
	    handleEvent(SoHandleEventAction ha)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        // set eventAction so that the app may call setHandled(), grab(), etc
	        eventAction = ha;
	    
	        // Are we monitoring a path?
	        if (pathOfInterest == null)
	            invokeCallbacks(ha.getEvent());
	        else {
	            // make sure the path of interest was picked
	            SoPickedPoint pp = ha.getPickedPoint();
	    
	            if ((pp != null) && pp.getPath().containsPath(pathOfInterest))
	                invokeCallbacks(ha.getEvent());
	        }
	    
	        eventAction = null;
	    }	   
	   
    ///////////////////////

    //! Tells the node the event was handled. The callback function is
    //! responsible for setting whether the event was handled or not.
    //! If there is more than one callback function registered
    //! with an SoEventCallback node, all of them will be
    //! invoked, regardless of whether one has handled
    //! the event or not.
    //! This should be called only from callback functions.
    public void                setHandled()
        { if (eventAction != null) eventAction.setHandled(); }

    //! Returns whether the event has been handled.
    //! This should be called only from callback functions.
    public boolean                isHandled() 
        { return (eventAction != null) ? eventAction.isHandled() : false; }

    //! Returns the SoHandleEventAction currently traversing this node,
    //! or NULL if traversal is not taking place. This should be called
    //! only from callback functions.
    public SoHandleEventAction        getAction() { return eventAction; }

	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initialize the node
	   //
	   // Use: public, internal
	   //
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SO__NODE_INIT_CLASS(SoEventCallback.class, "EventCallback", SoNode.class);
	   }
	   }
