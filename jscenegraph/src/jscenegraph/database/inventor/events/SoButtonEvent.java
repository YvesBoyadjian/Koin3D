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
 |      SoButtonEvent
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
//! Base class for all button events.
/*!
\class SoButtonEvent
\ingroup Errors
SoButtonEvent represents generic button press
and release events in the Inventor event model. It is the base class
for device-specific button events, namely 
SoKeyboardEvent, SoMouseButtonEvent, and
SoSpaceballButtonEvent.
This class stores the down/up state of the button
when the event occurred.

\par See Also
\par
SoEvent, SoKeyboardEvent, SoLocation2Event, SoMotion3Event, SoMouseButtonEvent, SoSpaceballButtonEvent, SoHandleEventAction, SoEventCallback, SoSelection, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoButtonEvent extends SoEvent {

    public enum State {
           UP, 
           DOWN, 
           UNKNOWN
       };
       
       private  State           state;       
         

       ////////////////////////////////////////////////////////////////////////
       //
       //Constructor
       //
       public SoButtonEvent()
       //
       ////////////////////////////////////////////////////////////////////////
       {
    	   state = SoButtonEvent.State.UNKNOWN;
       }

       
       public void setState(SoButtonEvent.State s) {
    	   state = s; 
       }
       
       public State getState() {
    	   return state; 
       }
       
       public static SoType getClassTypeId()        /* Returns class type id */   
                                       { return classTypeId; }                   
       public SoType      getTypeId()       /* Returns type id      */
       { return classTypeId; }
     private static SoType       classTypeId;             /* Type id              */       
  
       ////////////////////////////////////////////////////////////////////////
        //
        // Class initialization
        //
        // SoINTERNAL public
        //
       public static void initClass()
        //
        ////////////////////////////////////////////////////////////////////////
        {
            // Allocate a new node type id
            classTypeId = SoType.createType(SoEvent.getClassTypeId(), new SbName("ButtonEvent"));
        }
        
       }
