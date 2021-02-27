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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.2 $
 |
 |   Classes:
 |      SoMouseButtonEvent
 |
 |   Author(s): David Mott, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.events;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;


////////////////////////////////////////////////////////////////////////////////
//! Mouse button press and release events.
/*!
\class SoMouseButtonEvent
\ingroup Errors
SoMouseButtonEvent represents mouse button press and release events
in the Inventor event model.

\par See Also
\par
SoEvent, SoButtonEvent, SoKeyboardEvent, SoLocation2Event, SoMotion3Event, SoSpaceballButtonEvent, SoHandleEventAction, SoEventCallback, SoSelection, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMouseButtonEvent extends SoButtonEvent {

	public enum Button {
		ANY,
		BUTTON1,
		BUTTON2,
		BUTTON3
	}
	
	 private
		       Button          button;     
		   
		       boolean          doubleClick;
	
	public static SoType getClassTypeId() /* Returns class type id */ 
	{ return classTypeId; } 
	
	public  SoType getTypeId(){ /* Returns type id */
		 return classTypeId; 
	}
	private	static SoType classTypeId; /* Type id */	
	

//! some convenience macros for determining if an event matches

public static boolean SO_MOUSE_PRESS_EVENT(SoEvent EVENT, SoMouseButtonEvent.Button BUTTON) {
    return (isButtonPressEvent(EVENT,BUTTON));
}
public static boolean SO_MOUSE_RELEASE_EVENT(SoEvent EVENT, SoMouseButtonEvent.Button BUTTON) {
    return (isButtonReleaseEvent(EVENT,BUTTON));
}
	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Class initialization
	   //
	   // SoINTERNAL public
	   //
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // Allocate a new event type id
	       classTypeId = SoType.createType(
	           SoButtonEvent.getClassTypeId(), new SbName("MouseButtonEvent"));
	   }
	   
	  ////////////////////////////////////////////////////////////////////////
	  //
	  //Constructor
	  //
	  public SoMouseButtonEvent()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
		  button = SoMouseButtonEvent.Button.ANY;
		  doubleClick = false;
	  }

	  	
	/**
	 * set/get which button generated the event, 
	 * either SO_MOUSE_BUTTON1, SO_MOUSE_BUTTON2, 
	 * or SO_MOUSE_BUTTON3 
	 * 
	 * @param b
	 */
	public void setButton(Button b) {
		 button = b;
	}
	
	public Button getButton() {
		 return button; 
	}
	
    //! set if the button press is a double click; MEVIS Inventor only
    public void setIsDoubleClick(boolean b) { doubleClick = b; }

    //! get if the button press is a double click; MEVIS Inventor only
    public boolean isDoubleClick() { return doubleClick; }


////////////////////////////////////////////////////////////////////////
//
// Convenience routine - this returns TRUE if the event is a mouse button
// press event matching the passed button.
//
// static public
//
public static boolean
isButtonPressEvent(final SoEvent e,
                                       SoMouseButtonEvent.Button whichButton)
//
////////////////////////////////////////////////////////////////////////
{
    boolean isMatch = false;
    
    // is it a mouse button event?
    if (e.isOfType(SoMouseButtonEvent.getClassTypeId())) {
        final SoMouseButtonEvent me = ( SoMouseButtonEvent ) e;
        
        // is it a press event?
        if (me.getState() == SoButtonEvent.State.DOWN) {
        
            // did the caller want any button press? or do they match?
            if ((whichButton == SoMouseButtonEvent.Button.ANY) ||
                (me.getButton() == whichButton))
                isMatch = true;
        }
    }
    
    return isMatch;
}

	//
	   // Convenience routine - this returns TRUE if the event is a mouse button
	   // release event matching the passed button.
	   //
	   // static public
	   //
	  public static boolean isButtonReleaseEvent(SoEvent e, Button whichButton) {
		
		     boolean isMatch = false;
		          
		          // is it a mouse button event?
		          if (e.isOfType(SoMouseButtonEvent.getClassTypeId())) {
		              final SoMouseButtonEvent me = ( SoMouseButtonEvent ) e;
		              
		              // is it a release event?
		              if (me.getState() == SoButtonEvent.State.UP) {
		              
		                  // did the caller want any button release? or do they match?
		                  if ((whichButton == Button.ANY) ||
		                      (me.getButton() == whichButton))
		                      isMatch = true;
		              }
		          }
		          
		          return isMatch;
		     	}

}
