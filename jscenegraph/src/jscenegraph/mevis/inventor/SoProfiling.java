/*
 *
 *  Copyright (C) 2013 MeVis Medical Solutions AG,  All Rights Reserved.
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
 *  http://www.mevis.de
 *
 */

/*
 * Copyright (C) 2013 MeVis Medical Solutions AG
 *
 *   \file    SoProfiling.h
 *   \author  Florian Link
 *   \date    02/2013
 */

package jscenegraph.mevis.inventor;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;


//! Static class that offers profiling callbacks for a number of selected
//! Open Inventor features, e.g. field/container notifications.
/**
 * @author Yves Boyadjian
 *
 */
public class SoProfiling {
	
	public interface EnterScopeApplyActionCB {
		
		public void run(SoAction action);
	}
	
	public interface EnterScopeFieldNotifyCB {
		
		public void run(SoField action);
	}
	
	public interface EnterScopeFieldContainerNotifyCB {
		
		public void run(SoFieldContainer action);
	}
	
	public interface EnterScopeProcessDelayQueueCB extends Runnable {
	}
	
	public interface EnterScopeProcessTimerQueueCB extends Runnable {
	}
	
	public interface LeaveScopeCB extends Runnable {
		
	}
	
	private static boolean _enabled;

	private static EnterScopeApplyActionCB _enterScopeApplyActionCB;	   
	private static EnterScopeFieldNotifyCB _enterScopeFieldNotifyCB;
	private static EnterScopeFieldContainerNotifyCB _enterScopeFieldContainerNotifyCB;
	private static EnterScopeProcessDelayQueueCB _enterScopeProcessDelayQueueCB;
	private static EnterScopeProcessTimerQueueCB _enterScopeProcessTimerQueueCB;
	private static LeaveScopeCB	_leaveScopeCB;
	
	public static boolean isEnabled() { return _enabled; }
	public static void setEnabled(boolean flag) { _enabled = flag; }
	
	public static EnterScopeApplyActionCB getEnterScopeApplyActionCB() { return _enterScopeApplyActionCB; }
	public static EnterScopeFieldNotifyCB getEnterScopeFieldNotifyCB() { return _enterScopeFieldNotifyCB; }
	public static EnterScopeFieldContainerNotifyCB getEnterScopeFieldContainerNotifyCB() { return _enterScopeFieldContainerNotifyCB; }
	public static EnterScopeProcessDelayQueueCB getEnterScopeProcessDelayQueueCB() { return _enterScopeProcessDelayQueueCB; }
	public static EnterScopeProcessTimerQueueCB getEnterScopeProcessTimerQueueCB() { return _enterScopeProcessTimerQueueCB; }
	public static LeaveScopeCB getLeaveScopeCB() { return _leaveScopeCB; }
}
