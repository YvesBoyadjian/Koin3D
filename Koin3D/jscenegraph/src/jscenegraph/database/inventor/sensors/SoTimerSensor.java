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
 |      Timer sensors trigger repeatedly at regular
 |      intervals, starting at a given base time. By default, the base
 |      time is the current time (at the time the sensor is scheduled or
 |      rescheduled) and the interval is 1/30 of a second; both of these
 |      values may be changed.  If more than one interval passes while
 |      sensors are being triggered, then the extra intervals will be lost
 |      (timers always reschedule themselves to occur in the future).
 |      If more than one timer has expired (and therefore more than one
 |      needs to be rescheduled), all of them are first triggered, and are
 |      then rescheduled relative to the current time.  The reschedule()
 |      method is called by the sensor manager to accomplish this (see the
 |      comments there about saturating the timer queue).
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
//! Sensor that triggers callback repeatedly at regular intervals.
/*!
\class SoTimerSensor
\ingroup Sensors
Timer sensors trigger their callback function at regular intervals.
For example, a timer might be setup to call its callback function
every second on the second by setting the base time to SbTime(0.0)
and the interval to SbTime(1.0).  Timers are guaranteed to be
triggered only once when the timer queue is processed, so if the
application only processes the timer queue once every 5 seconds
(because it is busy doing something else) the once-a-second sensor's
callback will be called only once every 5 seconds.


Note also that SoTimers always schedule themselves to be triggered the
next even multiple of the interval time after the base time; so, for
example, if the once-a-second sensor is triggered at time 2.9 (because
the application way busy doing something at time 2.0 and didn't get
around to processing the sensor queue for a while) it will reschedule
itself to go off at time 3.0, not at time 3.9.  If the base time was
never set, the sensor would be scheduled for time 3.9.

\par See Also
\par
SoOneShotSensor, SoAlarmSensor, SoTimerQueueSensor, SbTime
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTimerSensor extends SoTimerQueueSensor implements Destroyable {

	   private final SbTime              baseTime = new SbTime();       //!< Base time for scheduling triggering
	   private final SbTime              interval = new SbTime();       //!< Interval between triggering
	   private  boolean              baseTimeSet;    //!< Whether base time was set by user
		    
	   private boolean              triggering;     //!< True if in trigger() code
		    
	   public SoTimerSensor() {
		   super();
		   
		     baseTimeSet = false;
		          interval.copyFrom(new SbTime(1.0 / 30.0));
		          triggering = false;
		      		   
	   }
		   	
	/**
	 * Creation methods. 
	 * The second method takes the callback function and data 
	 * to be called when the sensor is triggered. 
	 * 
	 * @param func
	 * @param data
	 */
	public SoTimerSensor(SoSensorCB func, Object data) {
		super(func, data);
	     baseTimeSet = false;
	          interval.copyFrom(new SbTime(1.0f / 30.0f));
	          triggering = false;
	     	}
	
	public void destructor() {
	    // Make sure sensor isn't still scheduled to be triggered
	    unschedule();
	    super.destructor();
	}
	
    //! Sets the base time.  The default base time is
    //! the time when the sensor is scheduled or rescheduled.
    public void               setBaseTime(final SbTime base)  { baseTime.copyFrom(base);
                                                           baseTimeSet = true;}
	
	public void setInterval(final SbTime intvl) {
		 interval.copyFrom(intvl); 
	}
	
	/**
	 * Java port
	 * @param intvl
	 */
	public void setInterval(float intvl) {
		setInterval(new SbTime(intvl));
	}
	
    //! Get the base time.
    public SbTime       getBaseTime()             { return baseTime;  }
	
	// Get the interval. 
	public SbTime getInterval() {
		 return interval; 
	}

    //! Triggers the sensor, calling its callback function. This
      //! overrides the method in SoSensor because it has to reschedule
      //! itself for the next interval after triggering.
     public void trigger() {
         triggering = true;
          
              // Add to the to-be-rescheduled queue
              SoDB.getSensorManager().rescheduleTimer(this);
          
              // Do the normal triggering stuff.  If the callback calls
              // schedule(), it will be ignored (because triggering = TRUE).  If
              // the callback calls unschedul(), the timer will be removed from
              // the rescheduleQueue.
              // Note that SoTimerQueueSensor::trigger is NOT called here,
              // because timers are not automatically unscheduled when they
              // trigger (see the corresponding logic in the schedule() and
              // unschedule() methods).
              SoSensor_trigger();
          
              // Note: the triggering process isn't considered over until the
              // sensor has been rescheduled or unscheduled.
             	 
     }


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
    final SbTime      now = SbTime.getTimeOfDay();

    // Set base time to current time if it was not set explicitly
    if (! baseTimeSet)
        baseTime.copyFrom(now);

    // If we are in the middle of the triggering this sensor, don't
    // bother setting the trigger time or adding it to the timer
    // queue; that will be done after trigger() returns and the
    // 'to-be-scheduled' queue is done.  However, if we are triggering
    // and we have been unscheduled, that means unschedule() was
    // called inside the trigger() callback; in this case, we should
    // add this sensor back onto the to-be-rescheduled queue.
    if (triggering) {
        if (!scheduled) {
            SoDB.getSensorManager().rescheduleTimer(this);
        }
        // In any case, wait for the sensor to be scheduled after it
        // has been triggered.
        return;
    }

    // Set the timer to trigger at the base time. If the base time is
    // before the current time, add sufficient whole intervals to get
    // it past the current time.
    setTriggerTime(baseTime.operator_add(
                   interval.operator_mul(Math.ceil((now.operator_minus(baseTime)).operator_div(interval) + 0.0000001f))));

    // Do standard scheduling stuff
    super.schedule();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Unschedules the sensor.  If this is called from inside a
//    trigger(), the sensor is removed from the to-be-rescheduled
//    queue (it has already been removed from the timer queue).
//
// Use: public

public void
unschedule()
//
////////////////////////////////////////////////////////////////////////
{
    if (triggering) {
        SoDB.getSensorManager().removeRescheduledTimer(this);
        scheduled = false;
        triggering = false;
    }
    else super.unschedule();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reschedules the sensor
//
// Use: internal public

public void
reschedule(final SbTime now)
//
////////////////////////////////////////////////////////////////////////
{
    // This will be set to TRUE by
    // SoTimerQueueSensor::schedule()
    scheduled = false;

    triggering = false;

    // Set base time to current time if it was not set explicitly
    if (! baseTimeSet) {
        baseTime.copyFrom(now);

        setTriggerTime(now.operator_add(interval).operator_add(new SbTime(0.0000001)));
    }
    else {
        // Reschedule to trigger again after the next interval. If we
        // missed some triggers because it took too long to trigger the
        // sensor, they are lost forever. The next trigger will take place
        // at the end of the next interval from now. Otherwise, this
        // sensor would just saturate the queue.
        final SbTime triggerTime = baseTime.operator_add(
        		interval.operator_mul( Math.ceil((now.operator_minus(baseTime)).operator_div( interval)
                          + 0.0000001)));

        setTriggerTime(triggerTime);
    }

    super.schedule();
}
}
