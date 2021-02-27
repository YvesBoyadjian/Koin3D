/*
 *
 *  Copyright (C) 2010 MeVis Medical Solutions AG,  All Rights Reserved.
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
 *  Contact information: MeVis Medical Solutions AG
 *  Universitätsallee 29, D-28359 Bremen, GERMANY, or:
 *
 *  http://www.mevis.de/mms
 *
 */

/*
 * Copyright (C) 2010 MeVis Medical Solutions AG
 *
 *   \file    SoMouseWheelEvent.h
 *   \author  Florian Link
 *   \date    11/2000
 */

package jscenegraph.mevis.inventor.events;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.events.SoEvent;


//! Open Inventor SoEvent that represents a MouseWheel movement
/*!
This class represents a MouseWheel event. It stores the wheel rotation,
which may be positive or negative, depending on the move of the wheel

\author Florian Link
*/
/**
 * @author Yves Boyadjian
 *
 */
public class SoMouseWheelEvent extends SoEvent {

	public
		  enum Orientation {
		    HORIZONTAL,
		    VERTICAL
		  };

		  private
			  //! the wheel's rotation
			  short _wheelRotation;
			  //! the wheel's orientation 
	private		  Orientation _orientation;
	
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
            classTypeId = SoType.createType(SoEvent.getClassTypeId(), new SbName("MouseWheelEvent"));
        }
	
       //! set the wheel rotation (should only be called by the event producer, not by the event user
       public void  setWheelRotation(short rot) {
    	   _wheelRotation = rot;    	   
       }
       
       //! set the wheel's orientation
       public void  setWheelOrientation(Orientation orient) {
    	   _orientation = orient;
       }
       

public short getWheelRotation() 
{
  return _wheelRotation;
}

       
}
