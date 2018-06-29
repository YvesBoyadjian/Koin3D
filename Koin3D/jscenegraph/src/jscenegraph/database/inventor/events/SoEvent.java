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
 |   $Revision: 1.1.1.1 $
 |
 |   Classes:
 |      SoEvent
 |
 |   Author(s): David Mott, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.events;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoType;

////////////////////////////////////////////////////////////////////////////////
//! Base class for all events.
/*!
\class SoEvent
\ingroup Errors
SoEvent is the base class for events in the Inventor
event model. An event typically represents a user action, such as
a mouse button being pressed or a keyboard key being released.
SoEvent contains general information found in all Inventor
events, including the time the event occurred, the position of the
locater when the event occurred, and the state of the modifier
keys when the event occurred.

\par See Also
\par
SoButtonEvent, SoKeyboardEvent, SoLocation2Event, SoMotion3Event, SoMouseButtonEvent, SoSpaceballButtonEvent, SoHandleEventAction, SoEventCallback, SoSelection, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoEvent {
	
    //! all of these are set according to when the event occurred
    private final SbTime timestamp = new SbTime();  //!< time the event occurred
 	private boolean shiftDown;
	private boolean ctrlDown;
	private boolean altDown;
	
    private final SbVec2s position = new SbVec2s();   //!< locator position when event occurred
    private final SbVec2s             viewportPos = new SbVec2s();    //!< position relative to viewport
    private final SbVec2f             normalizedPos = new SbVec2f();  //!< normalized position
    
    private static SoType       classTypeId;
    
////////////////////////////////////////////////////////////////////////
//
// Constructor
//
public SoEvent()
//
////////////////////////////////////////////////////////////////////////
{
    timestamp.copyFrom(new SbTime(0, 0));
    position.copyFrom(new SbVec2s((short)0, (short)0));
    shiftDown   = false;
    ctrlDown    = false;
    altDown     = false;
}

    
    
    // Return the type id for the SoEvent class. 
    public static SoType getClassTypeId() {
    	 return classTypeId; 
    }
    
    // Initializes base event class. 
    public static void initClass() {
    	
        // Allocate a new node type id
    	       // No real parent id, so pass 'badType' as the parent type
    	       classTypeId = SoType.createType(SoType.badType(), new SbName("Event"));
    	      }
    
    // Initialize ALL Inventor event classes. 
    public static void initClasses() {
    	
        SoEvent.initClass();
         
             SoButtonEvent.initClass();
             SoKeyboardEvent.initClass();
             SoMouseButtonEvent.initClass();
             SoSpaceballButtonEvent.initClass();
         
             SoLocation2Event.initClass();
             SoMotion3Event.initClass();
            }
    
    // Return the type id for this event instance. 
    public SoType getTypeId() {
    	return new SoType(classTypeId);
    }
    
    /**
     * This returns TRUE if the event is an instance of or derived from an 
     * event of the passed type.
     * 
     * @param type
     * @return
     */
    public boolean isOfType(SoType type) {
    	return getTypeId().isDerivedFrom(type);
    }
 
    //! Set the time at which the event occurred.
    public  void                setTime(SbTime t)               { timestamp.copyFrom(t); }
    //! Get the time at which the event occurred.
    public SbTime              getTime()                  { return new SbTime(timestamp); }
      
     /**
     * Set the window pixel location of the cursor when the event occurred. 
     * The position is relative to the lower left corner of the window in which 
     * the event occurred. 
     * 
     * @param p
     */
    public void setPosition(final SbVec2s p) {
    	 position.copyFrom(p); 
    }
	
	// Get position in pixel coodinates. 
	public SbVec2s getPosition() {
		 return position; 
	}
	

////////////////////////////////////////////////////////////////////////
//
// calculates the window position within the space of the viewport
// given by vpRgn.
//
public SbVec2s 
getPosition(final SbViewportRegion vpRgn) 
//
////////////////////////////////////////////////////////////////////////
{
    SoEvent             ev = (SoEvent )this;
    final SbVec2s       pixSize = vpRgn.getViewportOriginPixels();

    ev.viewportPos.getValue()[0] = (short)(position.getValue()[0] - pixSize.getValue()[0]);
    ev.viewportPos.getValue()[1] = (short)(position.getValue()[1] - pixSize.getValue()[1]);

    return viewportPos;
}

//! Get the normalized location of the cursor when the event occurred,
//! relative to the specified viewport region. The returned value will
//! lie between 0.0 and 1.0.
////////////////////////////////////////////////////////////////////////
//
//calculates the window position, normalizing coords to 0.0-1.0
//within the space of the viewport given by vpRgn.
//
public final SbVec2f  getNormalizedPosition(final SbViewportRegion vpRgn) {
	SoEvent             ev = (SoEvent )this;
    final SbVec2s       pixSize = vpRgn.getViewportSizePixels();

    final SbVec2f vpSize = new SbVec2f((float) pixSize.getValue()[0], (float) pixSize.getValue()[1]);

    final SbVec2s vpPos = new SbVec2s(getPosition( vpRgn ));

    if ( vpSize.getValue()[0] == 0.0 )
        ev.normalizedPos.getValue()[0] = 0.0f;
    else
        ev.normalizedPos.getValue()[0] = ((float) vpPos.getValue()[0]) / vpSize.getValue()[0];

    if ( vpSize.getValue()[1] == 0.0 )
        ev.normalizedPos.getValue()[1] = 0.0f;
    else
        ev.normalizedPos.getValue()[1] = ((float) vpPos.getValue()[1]) / vpSize.getValue()[1];

    return normalizedPos;
	
}
	

    //! Set whether the modifier keys were down when the event occurred.
      public void                setShiftDown(boolean isDown)     { shiftDown = isDown; }
      //! Set whether the modifier keys were down when the event occurred.
      public void                setCtrlDown(boolean isDown)      { ctrlDown = isDown; }
      //! Set whether the modifier keys were down when the event occurred.
      public void                setAltDown(boolean isDown)       { altDown = isDown; }
  
  	// Get whether the modifier keys were down when the event occurred.
	public boolean	wasShiftDown() {
		 return shiftDown;
	}
 
 	// Get whether the modifier keys were down when the event occurred.
	public boolean	wasCtrlDown() {
		 return ctrlDown; 
	}
 
 	// Get whether the modifier keys were down when the event occurred. More...
	public boolean	wasAltDown() {
		 return altDown; 
	}
}
