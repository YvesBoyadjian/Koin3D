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
 *  Universit�tsallee 29, D-28359 Bremen, GERMANY, or:
 *
 *  http://www.mevis.de/mms
 *
 */

/*
 * Copyright (C) 2010 MeVis Medical Solutions AG
 *
 *   \file    SoQtMouseWheel.h
 *   \author  Florian Link
 *   \date    11/2000
 */

package jsceneviewerawt.inventor.qt.devices;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.mevis.inventor.events.SoMouseWheelEvent;
import jsceneviewerawt.inventor.qt.SoQtGLWidget.EventType;

import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Yves Boyadjian
 *
 */

//! A device that transforms QEvents that are WheelEvents into a SoMouseWheelEvent
/*!
\author Florian Link
*/
public class SoQtMouseWheel extends SoQtDevice {
	
	private
		  //! internal event object that is reused
		  SoMouseWheelEvent _event;

	  private                                                          
	    static final SoType classTypeId = new SoType();                                      
	  public  static Object createInstance() {
	    	return new SoQtMouseWheel();
	    }
	    
	public
		  static void initClass() {
		  SO_QT_DEVICE_INIT_CLASS(SoQtMouseWheel.class, SoQtDevice.class, classTypeId);
	}
	
	public SoEvent translateEvent(ComponentEvent event, EventType type)
	{
	  if (type == EventType.MOUSE_EVENT_MOUSE_SCROLLED) {
	    MouseWheelEvent wevent = (MouseWheelEvent) event;
	    if (_event == null) _event = new SoMouseWheelEvent();
	    _event.setWheelRotation((short)wevent.getWheelRotation());
	    _event.setWheelOrientation(/*(wevent.orientation() == Qt::Horizontal) ? SoMouseWheelEvent::HORIZONTAL : */SoMouseWheelEvent.Orientation.VERTICAL);

	    // Modifiers
	    _event.setShiftDown((wevent.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK)!=0);
	    _event.setCtrlDown((wevent.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK)!=0);
	    _event.setAltDown((wevent.getModifiersEx() & MouseEvent.ALT_DOWN_MASK)!=0);

	    setEventPosition(_event, wevent.getX(), wevent.getY());
	    _event.setTime(SbTime.getTimeOfDay());
	    //wevent.accept();
	    return _event;
	  } else {
	    return null;
	  }
	}
}
