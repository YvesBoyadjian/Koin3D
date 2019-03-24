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
 * Author(s): Nick Thompson, Paul Isaacs, David Mott, Gavin Bell
 *             Alain Dumesny
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */


package jsceneviewerglfw.inventor.qt;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.interaction.inventor.SoInteraction;
import jscenegraph.mevis.inventor.SoMeVis;
import jscenegraph.nodekits.inventor.nodekits.SoNodeKit;
import jsceneviewerglfw.inventor.qt.SoQtEventHandler;
import jsceneviewerglfw.inventor.qt.devices.SoQtDevice;
import jsceneviewerglfw.inventor.qt.devices.SoQtMouseWheel;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQt {
	
	public static final int SOQT_ZERO_TIMER_THRESHOLD = 100;	
	
	private static boolean _initialized;
	private static SoQtEventHandler  _eventHandler;
	    
	    // This binds Inventor with Qt so that they may work together.
	    // It calls SoDB::init(), SoNodeKit::init(), SoInteraction::init(),
	    // SoMeVis::init()
	public static void init() {
		init("MeVisLab");
	}
	
	public static void init( String appName) {
		init(appName,"Inventor");
	}
	
	public static void init ( String appName, String className) {
	    // see if we are already initialized!
	    if (_initialized) {
	        return;
	    }
	    _initialized = true;

	    // init Inventor
	    SoDB.init();
	    SoNodeKit.init();
	    SoInteraction.init();
	    SoMeVis.init();
	  
	    // register class with run-time type system
	    SoQtDevice.initClass();
	    SoQtMouseWheel.initClass();

	    // bind Inventor with Qt
	    _eventHandler = new SoQtEventHandler();		
	}

    public static SoQtEventHandler eventHandler() { return _eventHandler; }
    public static boolean loopDetected() {
    	  return _eventHandler != null ? _eventHandler.loopDetected() : false;    	
    }
}
