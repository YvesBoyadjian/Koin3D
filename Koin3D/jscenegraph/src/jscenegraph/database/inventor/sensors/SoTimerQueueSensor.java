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
 |      Abstract base class for all sensors that are added to the timer
 |      queue. Each sensor in the timer queue contains an SbTime
 |      indicating when the sensor should be triggered. Sensors in the
 |      timer queue are sorted by their trigger times.
 |      Each subclass defines public methods for specifying when and how
 |      to trigger the sensor.
 |
 |   Author(s)          : Nick Thompson, Paul Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.port.Destroyable;

////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for sensors dependent on time.
/*!
\class SoTimerQueueSensor
\ingroup Sensors
Timer queue sensors are sensors that trigger themselves at specific
times.  The timer queue is normally processed as part of a programs
main loop when the program is not busy doing something else.  Note
that processing the timer queue is not asynchronous em the program must
re-enter its main loop for timers to be triggered.  When the timer
queue is processed, all timers scheduled to go off at or before the
current time are triggered once, in order from earliest to latest.

\par See Also
\par
SoTimerSensor, SoAlarmSensor, SoIdleSensor, SoOneShotSensor, SoDataSensor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTimerQueueSensor extends SoSensor implements Destroyable {
	
	protected boolean scheduled;
	
	private final SbTime trigTime = new SbTime();
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Constructor
	    //
	    // Use: protected
	    
	   public SoTimerQueueSensor()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
		   super();
	        scheduled = false;
	    }
	    	public SoTimerQueueSensor(SoSensorCB func, Object data) {
		super(func,data);
		scheduled = false;
	}
	    	
	    	public void destructor() {
	    	    // Make sure sensor isn't still scheduled to be triggered
	    	    unschedule();	    	 
	    	    
	    	}
	
	/**
	 * Returns the time at which this sensor is scheduled to be triggered. 
	 * If the sensor is not scheduled the results are undefined. 
	 * 
	 * @return
	 */
	public SbTime getTriggerTime() {
		return trigTime;
	}

	/**
	 * Adds this sensor to the timer queue. 
	 * Subclasses provide methods for setting when the sensor 
	 * will be triggered. 
	 */
	@Override
	public void schedule() {
		 // Don't do anything if there's no callback function or it is
		  // already scheduled:
		  if (func == null || scheduled == true)
		  return;
		 
		  // Insert into queue
		  SoDB.getSensorManager().insertTimerSensor(this);
		 
		  scheduled = true;
	}
	
	// If this sensor is scheduled, removes it from the timer queue so that 
	// it will not be triggered. 
	public void unschedule() {
		  if (scheduled) {
			    // Remove from queue
			    SoDB.getSensorManager().removeTimerSensor(this);
			   
			    scheduled = false;
			    }
			  		
	}

	/**
	 * Returns TRUE if this sensor has been scheduled and is waiting 
	 * in the timer queue to be triggered. 
	 * 
	 * @return
	 */
	public boolean isScheduled() {
		return scheduled;
	}
	
	//
	// Description:
	// Returns TRUE if this sensor should precede sensor s in the timer
	// queue: it must have an earlier trigger time.
	//
	// Use: private, virtual
	
	public boolean isBefore(SoSensor s) {
		  // We must assume that s is also an SoTimerQueueSensor
		  return (getTriggerTime().operator_less_or_equals(((SoTimerQueueSensor ) s).getTriggerTime()));
				
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the trigger time.  If the sensor is scheduled, this
//    unschedules and reschedules it.
//
// Use: protected

protected void setTriggerTime(final SbTime time)
//
////////////////////////////////////////////////////////////////////////
{
    trigTime.copyFrom(time);
    if (isScheduled()) {
        SoDB.getSensorManager().removeTimerSensor(this);
        SoDB.getSensorManager().insertTimerSensor(this);
    }
}
	/**
	 * Triggers the sensor, calling its callback function. 
	 * This overrides the method in SoSensor because it has to 
	 * reset the schedule flag before triggering. 
	 */
	public void trigger() {
		 // Mark the sensor as unscheduled
		  scheduled = false;
		 
		  // Do the normal triggering stuff
		  super.trigger();
				
	}
	
	// java port
	public void SoSensor_trigger() {
		super.trigger();		
	}
}
