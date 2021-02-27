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
 |      SoMotion3Event - 3d change in value (relative, not absolute)
 |
 |   Author(s): David Mott
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.events;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;


////////////////////////////////////////////////////////////////////////////////
//! 3D motion events.
/*!
\class SoMotion3Event
\ingroup Errors
SoMotion3Event represents 3D relative motion events in the Inventor
event model.

\par See Also
\par
SoEvent, SoButtonEvent, SoKeyboardEvent, SoLocation2Event, SoMouseButtonEvent, SoSpaceballButtonEvent, SoHandleEventAction, SoEventCallback, SoSelection, SoInteraction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMotion3Event extends SoEvent {

  private final   SbVec3f         translation = new SbVec3f();    //!< translation value
    private final SbRotation      rotation = new SbRotation();       //!< rotation value
    
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
            classTypeId = SoType.createType(SoEvent.getClassTypeId(), new SbName("Motion3Event"));
        }
    
}
