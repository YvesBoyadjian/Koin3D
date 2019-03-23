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
 * Author(s): David Mott, Gavin Bell
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */


package jsceneviewer.inventor.qt.devices;

import java.lang.reflect.InvocationTargetException;

import jsceneviewer.TypedEvent;
import jsceneviewer.Composite;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.events.SoEvent;
import jsceneviewer.inventor.qt.SoQtEventHandler;
import jsceneviewer.inventor.qt.SoQtGLWidget.EventType;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQtDevice {
	
	  private
		    // base typeId
		    static final SoType       classTypeId = new SoType();

	  private final SbVec2s             winSize = new SbVec2s();    // size of the window this device works in
		    double              devicePixelRatio = 1;    // ratio between physical pixels and device-independent pixels
		    
		    // typeId of the class
	public static SoType getClassTypeId() { return new SoType(classTypeId); }
		  
	  public
		    // Initializes class
		    static void         initClass() {
		  classTypeId.copyFrom(SoType.createType(SoType.badType(), new SbName("SoQtDevice")));		  
	  }
		  
	  protected static SoType SO_QT_DEVICE_INIT_CLASS(final Class<? extends SoQtDevice> className, Class<? extends SoQtDevice> parentClass, SoType classTypeId) {
		  SoType parentClassTypeId;
		try {
			parentClassTypeId = (SoType)parentClass.getMethod("getClassTypeId").invoke(null);
	  classTypeId.copyFrom( SoType.createType(parentClassTypeId,   
	                new SbName(className.getSimpleName()), new SoType.CreateMethod() {
						
						@Override
						public Object run() {
							try {
								return className.getMethod("createInstance").invoke(null);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw new IllegalStateException();
							}
						}
					}));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException();
		}
			  return classTypeId;
	  }
	
	    // these functions will enable/disable this device for the passed widget.
	    public void        enable(Composite widget, SoQtEventHandler handler,
	                               Object closure) {}
	    public void        disable(Composite widget, SoQtEventHandler handler,
	                                Object closure) {}

	  
	    //! Notify the device about the ratio between physical pixels and device-independent pixels of
	    //! the viewer to which this device is attached. The value will be used for event translation.
	    //! \ingroup HiDPIExtender
	  public void setDevicePixelRatio(double scaleFactor) { devicePixelRatio = scaleFactor; }

	    // this converts an X event into an SoEvent.
	    // this returns NULL if the event is not from this device.
	  public SoEvent  translateEvent(TypedEvent qevent, EventType type) { return null; }

	  
	    // set the window size so that the device can correctly convert X
	    // window coordinates (origin at top,left) into Inventor window
	    // coordinates (origin at bottom,left).
	  public void                setWindowSize(final SbVec2s s) { winSize.copyFrom(s); }
	  public SbVec2s      getWindowSize()           { return winSize; }
	  
	  

	// Inline functions
	protected void setEventPosition(SoEvent event, int x, int y)
	{
	    event.setPosition(new SbVec2s((short)(x * devicePixelRatio), (short)((winSize.getValue()[1] - 1) - (y * devicePixelRatio))));
	}

}
