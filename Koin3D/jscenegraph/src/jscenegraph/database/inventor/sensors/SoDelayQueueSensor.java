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
 |   Description:
 |      Abstract base class for all sensors that are added to the delay
 |      queue, which means that they will be executed at some time in the
 |      future (when there is idle time, or when a user-defined timeout
 |      occurs-- see SoDB::setDelaySensorTimeout()).
 |
 |      Sensors in the delay queue are sorted by priorities. A priority of
 |      0 indicates that the sensor is immediate and should be triggered
 |      immediately when it is scheduled. Sensors with other priorities
 |      are sorted by increasing priority.
 |
 |   Author(s)          : Nick Thompson, Paul Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for sensors not dependent on time.
/*!
\class SoDelayQueueSensor
\ingroup Sensors
Delay queue sensors are separate from timer queue sensors (see
SoTimerQueueSensor) and provide methods for setting the relative
priorities of the sensors in the delay queue (sensors with higher
priorities will be triggered first).


Sensors with non-zero priorities are added to the delay queue when
scheduled, and are all processed once, in order, when the delay queue
is processed, which normally happens as part of your program's main
loop.  Typically, the delay
queue is processed whenever there are no events waiting to be
distributed and there are no timer queue sensors waiting to be
triggered.  The delay queue also has a timeout to ensure that delay
queue sensors are triggered even if there are always events or timer
sensors waiting; see
SoDB::setDelaySensorTimeout().


Sensors with priority 0 are treated specially.  Priority 0 sensors are
triggered almost immediately after they are scheduled, before the
program returns to the main loop.  Priority 0 sensors are not
necessarily triggered immediately when they are scheduled, however; if
they are scheduled as part of the evaluation of a field connection
network they may not be triggered until the evaluation of the network
is complete.  Also, if a priority 0 sensor is scheduled within the
callback method of another priority 0 sensor, it will not be triggered
until the callback method is complete (also note that if more than one
priority 0 sensor is scheduled, the order in which they fire is
undefined).

\par See Also
\par
SoTimerQueueSensor, SoDataSensor, SoFieldSensor, SoIdleSensor, SoOneShotSensor, SoNodeSensor, SoPathSensor, SoSensorManager
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDelayQueueSensor extends SoSensor implements Destroyable {
	
	protected boolean scheduled;
	
	private int priority;
	private int counter;
	
	public SoDelayQueueSensor() {
		super();
		 priority = getDefaultPriority();
		   scheduled = false;
		   counter = 0;		 
	}
	
	 //
	 // Description:
	 // Constructor with function and data.
	 //
	 // Use: protected
	 
	 public SoDelayQueueSensor(SoSensorCB func, Object data) {
	  super(func, data);
	 //
	 
	  priority = getDefaultPriority();
	  scheduled = false;
	  counter = 0;
	 }
	 
	 public void destructor() {
		    // Make sure sensor isn't still scheduled to be triggered
		    unschedule();		 
	 }
	 
	 /**
	  * Sets the priority of the sensor. 
	  * Priorities can be changed at any time; 
	  * if the priority is changed to zero and it is already 
	  * scheduled, the sensor is immediately triggered and 
	  * removed from the queue. 
	  * 
	  * @param pri
	  */
	public void setPriority(int pri) {
	     priority = pri;
	      
	          if (isScheduled()) {
	              unschedule();
	              schedule();
	          }	     	
	}
	
	// Gets the priority of the sensor.
	public int getPriority() {
		return priority;
	}

	/**
	 * If this sensor's priority is non-zero, adds this sensor to 
	 * the list of delay queue sensors ready to be triggered. 
	 * This is a way of making a sensor fire without changing the 
	 * thing it is sensing. 
	 * Calling schedule() within the callback function causes the 
	 * sensor to be called repeatedly. Because sensors are processed 
	 * only once every time the delay queue is processed (even if they 
	 * reschedule themselves), timers and events will still be processed. 
	 * This should not be done with a priority zero sensor because an 
	 * infinite loop will result. 
	 */
	@Override
	public void schedule() {
		  // Don't do anything if there's no callback function.
		  if (func == null)
		  return;
		 
		  // Priority 0 means trigger the sensor immediately. These sensors
		  // are added to the queue anyway, and are triggered when
		  // notification is done. See comments in SoDB.h.
		 
		  if (! scheduled) {
		  // Insert into queue
		  SoDB.getSensorManager().insertDelaySensor(this);
		 
		  scheduled = true;
		  }
				
	}
	
	// If this sensor is scheduled, removes it from the delay queue so that 
	// it will not be triggered. 
	public void unschedule() {
		 if (scheduled) {
			   SoDB.getSensorManager().removeDelaySensor(this);
			   scheduled = false;
			   }
			  		
	}
	
    //! This method is overriden by IdleSensors to tell sensor manager
    //! that they should only be processed when there really is idle
    //! time (and not when the delay queue timeout expires).
    public boolean        isIdleOnly() {
    	return false;
    }
	
    //! Set/inquire counter when sensor was last triggered. This allows
    //! the sensor manager to avoid triggering a sensor more than once
    //! during the same call to processDelayQueue().
    public void                setCounter(int c)          { counter = c;  }
    public int            getCounter()               { return counter; }

    
    
	/**
	 * Returns TRUE if this sensor has been scheduled and 
	 * is waiting in the delay queue to be triggered. 
	 * Sensors are removed from the queue before their callback function 
	 * is triggered. 
	 */
	public boolean isScheduled() {
		return scheduled;
	}
	
	// Returns the default delay queue sensor priority, which is 100. 
	public static int getDefaultPriority() {
		return 100;
	}
	
	//
	 // Description:
	 // Returns TRUE if this sensor should precede sensor s in the delay
	 // queue: it must have a lower priority number than s.
	 //
	 // Use: private, virtual
	 
	public boolean isBefore(SoSensor s) {
		
		  // We must assume that s is also an SoDelayQueueSensor
		  return (getPriority() <= ((SoDelayQueueSensor ) s).getPriority());
		
		}
	
	/**
	 * Triggers the sensor, calling its callback function. 
	 * This overrides the method in SoSensor because it has to reset 
	 * the schedule flag before triggering. 
	 */
	public void trigger() {
		  // Mark the sensor as unscheduled
		  scheduled = false;
		 
		  // Do the normal triggering stuff
		  super.trigger();
				
	}
}
