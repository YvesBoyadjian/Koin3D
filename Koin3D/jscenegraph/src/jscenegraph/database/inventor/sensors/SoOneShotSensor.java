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
 |      A one-shot sensor is added to the delay queue when scheduled. When
 |      triggered, it just calls its callback.
 |
 |   Author(s)          : Gavin Bell, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

////////////////////////////////////////////////////////////////////////////////
//! Sensor for one-time only callbacks.
/*!
\class SoOneShotSensor
\ingroup Sensors
A one-shot sensor is triggered once after it is scheduled, when the
delay queue is processed.  Like all delay queue sensors, one-shot
sensors with a non-zero priority are just added to the delay queue
when scheduled; if they are scheduled again before the delay queue is
processed nothing happens, and they are guaranteed to be called only
once when the delay queue is processed.  For example, a one-shot
sensor whose callback function redraws the scene might be scheduled
whenever the scene graph changes and whenever a window-system event
reporting that the window changed size occurs.  By using a one-shot,
the scene will only be redrawn once even if a window-changed-size
event occurs just after the scene graph is modified (or if several
window-changed-size events occur in a row).


Calling schedule() in the callback function is a useful way of
getting something to happen repeatedly as often as possible, while
still handling events and timeouts.


A priority 0 one-shot sensor isn't very useful, since scheduling it is
exactly the same as directly calling its callback function.

\par See Also
\par
SoIdleSensor, SoDelayQueueSensor
*/
/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoOneShotSensor SoOneShotSensor.h Inventor/sensors/SoOneShotSensor.h
  \brief The SoOneShotSensor class is a sensor which will trigger once.

  \ingroup sensors

  Since SoOneShotSensor is a subclass of SoDelayQueueSensor, it will
  trigger as soon as either the runtime system is idle, or if it is
  continually busy it will trigger within a fixed amount of time (this
  is by default 1/12th of a second, see
  SoSensorManager::setDelaySensorTimeout()).
*/


////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoOneShotSensor extends SoDelayQueueSensor {

/*!
  Constructor.
 */
public SoOneShotSensor()
{
}

/*!
  Constructor taking as parameters the sensor callback function and
  the userdata which will be passed to the callback.

  \sa setFunction(), setData()
 */
public SoOneShotSensor(SoSensorCB func, Object data) {
  super(func, data);

}

}
