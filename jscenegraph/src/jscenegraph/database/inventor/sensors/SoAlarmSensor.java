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
 |      Timer queue sensor that is scheduled to be triggered at a specific
 |      time, which is specified either absolutely (e.g., 2pm) or
 |      relatively (e.g., 5 minutes from now). Once an alarm sensor is
 |      triggered, it is not rescheduled.
 |
 |   Author(s)          : Gavin Bell, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.errors.SoDebugError;

////////////////////////////////////////////////////////////////////////////////
//! Triggers a callback once sometime in the future.
/*!
\class SoAlarmSensor
\ingroup Sensors
This type of sensor can be used to schedule a one-time callback for
some time in the future.  The sensor is not guaranteed to be called at
exactly that time, but will be called sometime after the specified
time.

\par See Also
\par
SoOneShotSensor, SoTimerSensor, SoTimerQueueSensor, SbTime
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoAlarmSensor extends SoTimerQueueSensor {
	
	private final SbTime time = new SbTime();
	private boolean timeSet;

	/**
	 * Creation methods. 
	 * The second method takes the callback function and data 
	 * to be called when the sensor is triggered. 
	 * 
	 * @param func
	 * @param data
	 */
	public SoAlarmSensor(SoSensorCB func, Object data) {
		super(func,data);
		timeSet = false;
	}
	
	/**
	 * Sets the sensor to go off at the specified time. 
	 * You must also call schedule() for the sensor to be triggered. 
	 * If the sensor is already scheduled, it must be unscheduled 
	 * and then rescheduled for the change in the trigger time to 
	 * take effect. 
	 * 
	 * @param absTime
	 */
	public void setTime(SbTime absTime) {
		  time.copyFrom(absTime);
		  timeSet = true;		  		
	}
	
	/**
	 * Sets the sensor to go off the given amount of time from now. 
	 * You must also call schedule() for the sensor to be triggered. 
	 * If the sensor is already scheduled, it must be unscheduled and 
	 * then rescheduled for the change in the trigger time to take effect. 
	 * 
	 * @param relTime
	 */
	public void setTimeFromNow(final SbTime relTime) {
		setTime(SbTime.getTimeOfDay().operator_add(relTime));
	}
	
    //! Returns the time the sensor is scheduled to be triggered. This
    //! differs from getTriggerTime() in that this method returns the
    //! time the sensor was set to be scheduled, even if it has not yet
    //! been scheduled.
    public SbTime       getTime()                  { return time; }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides the regular schedule() method because we have to set
//    up the trigger time first.
//
// Use: public

public void
schedule()
//
////////////////////////////////////////////////////////////////////////
{
    // It is an error to schedule an alarm sensor that has not had its
    // time set
    if (! timeSet) {
        SoDebugError.post("SoAlarmSensor::schedule",
                           "Alarm time was never set");
        return;
    }

    // Set the timer to trigger at the base time
    setTriggerTime(time);

    // Do standard scheduling stuff
    super.schedule();
}

	
}
