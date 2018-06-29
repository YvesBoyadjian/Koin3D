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
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Manager for scheduling and triggering sensors. The manager
 |      maintains a queue of delay sensors (derived from SoDelayQueueSensor)
 |      and timer sensors (derived from SoTimerQueueSensor).
 |
 |   Classes
 |      SoSensorManager
 |
 |   Author(s)          : Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.mevis.inventor.SoProfiling;
import jscenegraph.port.Callback;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSensorManager implements Destroyable {
	
	private final SoSensor[] delayQueue = new SoSensor[1];
	private final SoSensor[] timerQueue = new SoSensor[1];
	private final SoSensor[] rescheduleQueue = new SoSensor[1];
	
	private final SbTime delayQTimeout = new SbTime();
	private SoAlarmSensor delayQTimeoutSensor;
	private static final SoSensorCB delayQTimeoutCB = new SoSensorCB() {

		@Override
		public void run(Object data, SoSensor sensor) {
			//#ifdef DEBUG
//		    if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//		        SoDebug.RTPrintf("Processing delay queue; timeout expired\n");
//		    }
		//#endif

		    SoSensorManager s = (SoSensorManager )data;
		    s.processDelayQueue(false);
		}
		
	};
	
	private int processingQueue;
	
	private int delayQCount;
	
	private Callback changedFunc;
	private Object changedFuncData;
	
	private static final SbTime startTime = new SbTime();

	
	public SoSensorManager() {
		
	    changedFunc     = null;
	    changedFuncData = null;

	    delayQueue[0]  = null;
	    timerQueue[0] = null;
	    rescheduleQueue[0] = null;

	    delayQTimeout.copyFrom(SbTime.zero());
	    delayQTimeoutSensor = new SoAlarmSensor(delayQTimeoutCB, this);
//	#ifdef DEBUG
	    if (SoDebug.GetEnv("IV_DEBUG_SENSORS") != null) {
	        SoDebug.NamePtr("delayQTimeoutSensor", delayQTimeoutSensor);
	        startTime.copyFrom(SbTime.getTimeOfDay());
	    }
//	#endif

	    processingQueue = 0;

	    delayQCount = 0;
}
	
	public void destructor() {
		delayQTimeoutSensor.destructor();
	}
	
    //! Set up a function to call when either queue has a sensor added
    //! or removed
    public void                setChangedCallback(Callback funcArg, Object data)
        { changedFunc = funcArg; changedFuncData = data; }

	// Insert/remove an delay or timer event into/from the appropriate queue. 
	public void insertDelaySensor(SoDelayQueueSensor s) {
		
		 // If the sensor's priority isn't zero, AND the delay queue
		 // timeout isn't zero, and the delay queue sensor isn't already
		 // scheduled, get the delay queue timeout ready to go
		  // off.
		  if ((s.getPriority() != 0) && !delayQTimeoutSensor.isScheduled()
		  && (delayQTimeout != SbTime.zero())) {
		
		  delayQTimeoutSensor.unschedule();
		  delayQTimeoutSensor.setTimeFromNow(delayQTimeout);
		  delayQTimeoutSensor.schedule();
		 
		  }
		  insert(s, delayQueue);
		 
		  if (s.getPriority() != 0) {
//		 #ifdef DEBUG
//		  if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//		  SoDebug.RTPrintf("Inserted delay queue sensor: "
//		  "Name: %s, priority: %d\n",
//		  SoDebug.PtrName(s), s->getPriority());
//		  }
//		 #endif
		  // Call changed callbacks if necessary
		  notifyChanged();
		  }
		 
		}
	
	public void insertTimerSensor(SoTimerQueueSensor s) {
		
//		#ifdef DEBUG
//		  if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//		  SoDebug.RTPrintf("Inserting timer queue sensor: "
//		  "Name: %s, trigger time: %g\n",
//		  SoDebug.PtrName(s),
//		  (s->getTriggerTime()-startTime).getValue());
//		  }
//		 #endif
		  insert(s, timerQueue);
		 
		  // Call changed callbacks if necessary
		  notifyChanged();
		
		}
	
	public void removeDelaySensor(SoDelayQueueSensor s) {
		
		  remove(s, delayQueue);
		   
		    if (s.getPriority() != 0) {
		    // Call changed callbacks if necessary
		    notifyChanged();
		    }
		  
		  }
	
	public void removeTimerSensor(SoTimerQueueSensor s) {
		
		  remove(s, timerQueue);
		   
		    // Call changed callbacks if necessary
		    notifyChanged();
		  
		  }
	
	/**
	 * Timers are all rescheduled at the same time, 
	 * after they have been triggered. 
	 * This avoids timer queue saturation. 
	 * 
	 * @param s
	 */
////////////////////////////////////////////////////////////////////////
 //
 // Description:
 //    Adds a timer to the 'to-be-rescheduled' queue.
 //
 // Use: public
 	public void rescheduleTimer(SoTimerSensor s) {
 		 insert(s, rescheduleQueue);
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Remove a timer from the 'to-be-rescheduled' queue.
//
// Use: public

public void
removeRescheduledTimer(SoTimerQueueSensor s)
//
////////////////////////////////////////////////////////////////////////
{
    remove(s, rescheduleQueue);
}

 	
 	
	/**
	 * Set/get delay queue sensors (OneShot, Node, Path, etc) timeout value. 
	 * Delay sensor go off whenever there is idle time or when the timeout expires. 
	 * 
	 * @param t
	 */
	public void setDelaySensorTimeout(final SbTime t) {
//		 #ifdef DEBUG
//		       if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//		           SoDebug.RTPrintf("Setting delay sensor timeout to %g\n",
//		                             t.getValue());
//		       }
//		   #endif
		   
		       delayQTimeout.copyFrom(t);
		   
		       if (t.operator_equal(SbTime.zero())) {
		           delayQTimeoutSensor.unschedule();
		       } else {
		           if (delayQueue[0] != null) {
		               delayQTimeoutSensor.setTimeFromNow(t);
		               delayQTimeoutSensor.schedule();
		           }
		       }
		  		
	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets the delay sensor queue timeout.
//
// Use: public

public SbTime 
getDelaySensorTimeout()
//
////////////////////////////////////////////////////////////////////////
{
    return delayQTimeout;
}

	
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Processes all sensors in the delay queue.
//
// Use: public

public void
processDelayQueue(boolean isIdle)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
//    if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//        SoDebug.RTPrintf("Processing delay queue (isIdle:%d)\n", isIdle);
//    }
//#endif

    SoSensor            prev;
    SoDelayQueueSensor  first;

    if (delayQueue[0] == null)
        return;

    ++processingQueue;

    // We want to process all of the sensors that are currently in the
    // queue. But we have to be careful to NOT process sensors that
    // are added to the queue while we are processing it. Therefore,
    // we first mark all of the sensors in the queue as being current.
    // Then we process only the current sensors. When there are no
    // current sensors left, we're done. NOTE: removing a sensor marks
    // it as being not-current; inserting a new sensor does not mark
    // it as current.
//#ifdef DEBUG
    if (((SoDelayQueueSensor ) delayQueue[0]).getPriority() == 0)
        SoDebugError.post("SoSensorManager.processDelayQueue",
                           "Immediate sensor in delay queue!!!!");
//#endif

    // We want to process all of the sensors currently in the queue.
    // To avoid looping infinitely here, we do not trigger any sensor
    // more than once. To detect this, we keep a running counter
    // every time this method is called. When a sensor is triggered
    // here, it is marked with the current counter. If a sensor in the
    // queue is so marked, we know it has already been triggered once,
    // and we can skip it the next time.

    // Increment the count
    delayQCount++;

    boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeProcessDelayQueueCB()!=null)
                            && (delayQueue[0] != null);
    if (profilingEntered) {
      (SoProfiling.getEnterScopeProcessDelayQueueCB()).run();
    }

    while (delayQueue[0] != null) {

        // Find first sensor that has not yet been triggered (its
        // counter is not the same as our current counter), saving
        // previous sensor in queue (to keep links accurate)
        prev = null;

        first = (SoDelayQueueSensor ) delayQueue[0];
        while (first != null) {
            // Process this sensor IF:
            // -- it wasn't processed before, and
            // -- it is a OneShot, or an Idle and we have idle time
            if (first.getCounter() != delayQCount &&
                (isIdle || !first.isIdleOnly())) break;
            prev = first;
            first = (SoDelayQueueSensor ) first.getNextInQueue();
        }

        // If we didn't find one, we're done!
        if (first == null)
            break;

        // Remove sensor from queue
        if (prev == null)
            delayQueue[0] = first.getNextInQueue();
        else
            prev.setNextInQueue(first.getNextInQueue());
        first.setNextInQueue(null);

        // Mark the sensor with the current counter so it won't
        // trigger again immediately if it gets added to the queue again
        first.setCounter(delayQCount);

//#ifdef DEBUG
//        if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//            SoDebug.RTPrintf("Triggering delay queue sensor  "
//                              "Name: %s, priority: %d\n",
//                              SoDebug.PtrName(first), first->getPriority());
//            
//        }
//#endif

        // Implement catch and error posting in case of enabled
        // exception handling. Otherwise execute stuff normally 
        // and let it crash. See file SoCatch.h for more information.
//        SO_CATCH_START

          // Trigger the sensor. NOTE: this can add new entries to the
          // queue or remove entries.
          ((SoSensor ) first).trigger();

//        SO_CATCH_ELSE(;)
//
//          // Trigger the sensor. NOTE: this can add new entries to the
//          // queue or remove entries.
//          ((SoSensor ) first).trigger();
//
//        SO_CATCH_END(SoType.badType(), 
//                     "calling delay queue sensor callback", 
//                     SoType.badType(), 
//                     ".")
    }
    --processingQueue;

    // If there are still sensors left in the queue after processing,
    // setup the delay queue timeout:
    if ((delayQueue[0] != null) && (delayQTimeout.operator_not_equal(SbTime.zero()))) {
        delayQTimeoutSensor.setTimeFromNow(delayQTimeout);
        delayQTimeoutSensor.schedule();
    }

    if (profilingEntered && SoProfiling.getLeaveScopeCB() != null) {
      (SoProfiling.getLeaveScopeCB()).run();
    }
}

	
	
	// Process all immediate (priority 0) sensors in the delay queue.
    private static boolean processingImmediate = false;	
	public void processImmediateQueue() {
		
	    SoDelayQueueSensor  first;

	    if (delayQueue[0] == null)
	        return;

	    // Triggering an immediate sensor may initiate notification,
	    // calling this method recursively. To avoid doing extra work,
	    // just return if we are already processing immediate sensors.
	    if (processingImmediate)
	        return;

	    processingImmediate = true;
	    processingQueue++;

	    // NOTE: all immediate sensors are processed as added, even if
	    // this causes an infinite loop. (That's what they're supposed to
	    // do.) No counter check is made here.

	    while (delayQueue[0] != null &&
	           ((SoDelayQueueSensor ) delayQueue[0]).getPriority() == 0) {

	        first = (SoDelayQueueSensor ) delayQueue[0];
	        delayQueue[0] = delayQueue[0].getNextInQueue();

	        first.setNextInQueue(null);

//	#if 0
//	        if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//	            SoDebug.RTPrintf("Triggering immediate sensor  "
//	                              "Name: %s, priority: %d\n",
//	                              SoDebug.PtrName(first), first->getPriority());
//	        
//	        }
//	#endif

	        // Implement catch and error posting in case of enabled
	        // exception handling. Otherwise execute stuff normally 
	        // and let it crash. See file SoCatch.h for more information.
	        //SO_CATCH_START

	          ((SoSensor ) first).trigger();

//	        SO_CATCH_ELSE(;)
//
//	          ((SoSensor ) first).trigger();
//
//	        SO_CATCH_END(SoType.badType(), 
//	                     "calling immediate queue sensor callback", 
//	                     SoType.badType(), 
//	                     ".")
	    }

	    processingImmediate = false;
	    processingQueue--;
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Processes all sensors in the timer queue that are before the
//    current time.
//
// Use: public

public void processTimerQueue()
//
////////////////////////////////////////////////////////////////////////
{
    ++processingQueue;
    SoSensor    first;

    // NOTE: Infinite loops are possible if timerQueue sensors
    // reschedule themselves inside their trigger() routine.  This
    // isn't possible with Timer sensors, because they are added to
    // the rescheduleQueue and can only be triggered once per call to
    // processTimerQueue.
    // The assumption here is that if the user reschedules an alarm,
    // we assume they know what they are doing.

    boolean profilingEntered = SoProfiling.isEnabled() && SoProfiling.getEnterScopeProcessTimerQueueCB() != null &&
                            timerQueue[0] != null &&
                            ((SoTimerQueueSensor ) timerQueue[0]).getTriggerTime().operator_less_or_equals(SbTime.getTimeOfDay());
    if (profilingEntered) {
      (SoProfiling.getEnterScopeProcessTimerQueueCB()).run();
    }

    // Stop if no sensors left or next sensor really is after now
    while (timerQueue[0] != null &&
           ((SoTimerQueueSensor ) timerQueue[0]).getTriggerTime().operator_less_or_equals( 
               SbTime.getTimeOfDay())) {

        // Get pointer to first sensor
        first = timerQueue[0];

        // Remove sensor from queue.
        timerQueue[0] = timerQueue[0].getNextInQueue();
        first.setNextInQueue(null);

//#ifdef DEBUG
        if (SoDebug.GetEnv("IV_DEBUG_SENSORS") != null) {
            SoDebug.RTPrintf("Triggering timer sensor  "+
                      "Name: "+SoDebug.PtrName(first)+", trig time: "+(((SoTimerQueueSensor )first).
                              getTriggerTime().operator_minus(startTime)).getValue()+"\n"   );
        }
//#endif

        // Implement catch and error posting in case of enabled
        // exception handling. Otherwise execute stuff normally 
        // and let it crash. See file SoCatch.h for more information.
//        SO_CATCH_START

          // Trigger the sensor.
          first.trigger();

//        SO_CATCH_ELSE(;)
//
//          // Trigger the sensor.
//          first.trigger();
//
//        SO_CATCH_END(SoType.badType(), 
//                     "calling timer queue sensor callback", 
//                     SoType.badType(), 
//                     ".")
    }

    //
    // Timers used to reschedule themselves at the end of their
    // trigger() methods.  However, this can lead to timer queue
    // saturation if there are two sensors that take a long time in
    // their trigger() methods.  The first sensor will schedule itself
    // for sometime in the future, but by the time the second sensor
    // is done triggering (and scheduling itself in the future), the
    // first sensor is ready to fire.
    // By scheduling all timers relative to one time, AFTER they have
    // been triggered, this problem is avoided.
    //
    if (rescheduleQueue[0] != null) {
        SbTime  now = SbTime.getTimeOfDay();

        // Now reschedule timers, relative to now

        while (rescheduleQueue[0] != null) {
            SoTimerSensor timer = (SoTimerSensor )rescheduleQueue[0];
            // Remove sensor from queue
            rescheduleQueue[0] = rescheduleQueue[0].getNextInQueue();
            timer.setNextInQueue(null);

            timer.reschedule(now);
        }
    }   

    if (profilingEntered && SoProfiling.getLeaveScopeCB() != null) {
      (SoProfiling.getLeaveScopeCB()).run();
    }

    --processingQueue;
}

//! Returns TRUE if there is at least one sensor in the delay queue
public boolean                isDelaySensorPending()
                            { return (delayQueue[0] != null); }	

	//
	// Description:
	// Inserts a sensor into one of the queues. The head of the queue
	// is passed in by reference.
	//
	// Use: public
	private void insert(SoSensor s, final SoSensor [] head) {
		  SoSensor prev = null, next;
		   
		    // Loop through all sensors in queue
		    for (next = head[0]; next != null; next = next.getNextInQueue()) {
		   
		    // Stop if the new sensor should be inserted before the
		    // current one in the queue. Use the virtual isBefore() method
		    // which works for all types of sensors.
		    if (s.isBefore(next))
		    break;
		   
		    prev = next;
		    }
		   
		    // Insert sensor into queue
		    s.setNextInQueue(next);
		    if (prev == null)
		    head[0] = s;
		    else
		    prev.setNextInQueue(s);
		  		
	}
	
	//
	// Description:
	// Removes a sensor from one of the queues. The head of the queue
	// is passed in by reference.
	//
	// Use: public
	 
	private void remove(SoSensor s, final SoSensor [] head) {
		
	  SoSensor prev = null, cur;
	 
	  // Loop through all sensors in queue, looking for s
	  for (cur = head[0]; cur != null; cur = cur.getNextInQueue()) {
	 
	  if (cur == s)
	  break;
	 
	  prev = cur;
	  }
	 
	  // Remove sensor from queue
	  if (prev == null)
	  head[0] = s.getNextInQueue();
	  else
	  prev.setNextInQueue(s.getNextInQueue());
	  s.setNextInQueue(null);
	 }
	 
	
	//
	// Description:
	// Calls callback functions to tell them about changes to the
	// sensor queues. The callbacks are for higher-level toolkits
	// (like Xt) so timeouts can be set.
	//
	// Use: public
	 
	 private void notifyChanged()
	 //
	 {
	  if (changedFunc != null && processingQueue == 0) {
	  changedFunc.run(changedFuncData);
	 
//	 #ifdef DEBUG
//	  if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//	  SoDebug.RTPrintf("SoSensorManager.notifyChanged()\n");
//	  }
//	 #endif
	  }
	 }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if there is at least one sensor in the timer queue,
//    and returns the time of the first sensor in tm.
//
// Use: public

public boolean
isTimerSensorPending(final SbTime tm)
//
////////////////////////////////////////////////////////////////////////
{
    if (timerQueue[0] != null)
        tm.copyFrom( ((SoTimerQueueSensor ) timerQueue[0]).getTriggerTime());
    return (timerQueue[0] != null);
}

}
