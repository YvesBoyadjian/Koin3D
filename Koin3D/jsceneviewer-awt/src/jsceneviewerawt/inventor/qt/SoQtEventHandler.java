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


package jsceneviewerawt.inventor.qt;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jsceneviewerawt.QTimer;

/**
 * @author Yves Boyadjian
 *
 */
//THIS CLASS SHOULD BE CONSIDERED PRIVATE
//this class ties Inventor sensors into Xt events
public class SoQtEventHandler {

    QTimer     qtTimer;
    final SbTime      currentDeadline = new SbTime();

    QTimer     qtWorkProc;

    private int _zeroTimerCount;
    private boolean _isTriggeredByTimer;
    

  //----------------------------------------------------------------------
  //
  //  SoQtEventHandler - this class ties in sensors to Qt events
  //
  //----------------------------------------------------------------------

  SoQtEventHandler()
  {
      qtTimer     = new QTimer (this);
      qtWorkProc  = new QTimer (this);
      qtTimer.setSingleShot (true);
      qtWorkProc.setSingleShot (true);
      qtTimer.connect((obj) -> {SoQtEventHandler.qtTimerCallback((SoQtEventHandler)obj);});
      qtWorkProc.connect((obj) -> {SoQtEventHandler.qtWorkProcCallback((SoQtEventHandler)obj);});

      SoDB.getSensorManager().setChangedCallback
          (SoQtEventHandler::sensorManagerChangedCallback,
           this);
      
      _zeroTimerCount = 0;
      _isTriggeredByTimer = false;

      // Call this once because things might already be in the timer queue.
      // If we don't call this and no new things happen, then the callbacks
      // will never be set up.
      setUpCallbacks();
  }

	public boolean loopDetected()
	{
	  return _zeroTimerCount >= SoQt.SOQT_ZERO_TIMER_THRESHOLD;
	}


void
setUpCallbacks()
{
    final SbTime      nextEvent = new SbTime();
    boolean isZeroTimer = false;

    // If we have a timer at some point, schedule that
    if (SoDB.getSensorManager().isTimerSensorPending (nextEvent)) {
        // Only change the timer if its deadline is different from the
        // current one.
        if (!qtTimer.isActive() || nextEvent.operator_not_equal(currentDeadline)) {
            currentDeadline.copyFrom(nextEvent);
            long microsec = (nextEvent.operator_minus(SbTime.getTimeOfDay())).getMicrosecValue();
            if (microsec < 0) { microsec = 0; }
            qtTimer.start (microsec);
        }
    } else if (qtTimer.isActive()) {           // get rid of existing timer
        qtTimer.stop();
    }

    // If we have an idle task, schedule a workproc for it
    if (SoDB.getSensorManager().isDelaySensorPending()) {
        // only schedule it if one is not already active
        if (!qtWorkProc.isActive()) {
            int msec = loopDetected()?1:0;
            qtWorkProc.start (msec);
        }
        isZeroTimer = true;
    }
    else if (qtWorkProc.isActive()) {          // remove obsolete workproc
        qtWorkProc.stop();
    }

    if (_isTriggeredByTimer && isZeroTimer) {
      if (!loopDetected()) {
        _zeroTimerCount++;
        if (loopDetected()){
          indicateLoop(true);
        }
      }
    } else {
      if (loopDetected()) {
        indicateLoop(false);
      }
      _zeroTimerCount = 0;
    }
}

//! This is emitted if it is detected that OpenInventor is in a busy loop
void        indicateLoop(boolean detected) {
	// TODO
}


static void
qtTimerCallback(SoQtEventHandler that)
{
    // process the timer queue
	that._isTriggeredByTimer = true;
    SoDB.getSensorManager().processTimerQueue();
    that.setUpCallbacks();
    that._isTriggeredByTimer = false;
}

static void
qtWorkProcCallback(SoQtEventHandler that)
{
    // process the idle queue
	that._isTriggeredByTimer = true;
    SoDB.getSensorManager().processDelayQueue(true);
    that.setUpCallbacks();
    that._isTriggeredByTimer = false;
}

// Callback for changes to the sensor manager
static void
sensorManagerChangedCallback (Object data)
{
    SoQtEventHandler eh = (SoQtEventHandler ) data;

    // update the Qt timeout and workproc if necessary
    eh.setUpCallbacks();
}

}
