
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.inventor.misc;

import com.jogamp.opengl.GL2;

/**
 * @author Yves Boyadjian
 *
 */
public class SoContextHandler {
	
	public interface ContextDestructionCB {
		void run(GL2 contextid, Object userData);
	}

	/**
	 * 
	 */
	public SoContextHandler() {
		// TODO Auto-generated constructor stub
	}

	/*!
	  Add a callback which will be called every time a GL context is
	  destructed. The callback should delete all GL resources tied to that
	  context.
	  
	  All nodes/classes that allocate GL resources should set up a callback
	  like this. Add the callback in the constructor of the node/class,
	  and remove it in the destructor.

	  \sa removeContextDestructionCallback()
	*/
	public static void addContextDestructionCallback(ContextDestructionCB func,
	                                                Object closure)
	{
		//TODO
	}
	public static void removeContextDestructionCallback(ContextDestructionCB func, Object closure) {
		// TODO Auto-generated method stub
		
	}

}
