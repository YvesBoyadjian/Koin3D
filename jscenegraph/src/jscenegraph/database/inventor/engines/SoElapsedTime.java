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
 |      This file contains the implementation of the ElapsedTime engine
 |
 |   Classes:
 |      SoElapsedTime
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.*;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Basic controllable time source.
/*!
\class SoElapsedTime
\ingroup Engines
This engine functions as a stopwatch; it outputs the time that has
elapsed since it started running.
By default, the \b timeIn  input is connected to the \b realTime  global field.
It can, however, be connected to any other time source.


The ouput from the engine is the time that has elapsed since it
started running, or since the \b reset  input was last triggered.
You can affect the speed of the output time by setting the
\b speed  scale factor.  A value greater than 1.0 will speed up
the output, and a value less than 1.0 will slow it down.


If you pause the engine, by setting the \b pause  input to TRUE, it stops
updating the \b timeOut  output.  When you turn off the pause, it jumps
to its current position without losing time.
Alternatively, if you want to stop the engine for a while, and then restart it
from where it left off, use the \b on  input field.

\par File Format/Default
\par
\code
ElapsedTime {
  timeIn 1316437066.35
  speed 1
  on TRUE
  pause FALSE
}
\endcode

\par See Also
\par
SoTimeCounter, SoOneShot, SoEngineOutput
*/
////////////////////////////////////////////////////////////////////////////////

public class SoElapsedTime extends SoEngine {

    // SO_ENGINE_ABSTRACT_HEADER

    public
    static SoType getClassTypeId() { return classTypeId; }
    public    SoType      getTypeId()  /* Returns type id   */
    {
        return classTypeId;
    }
    public
    SoFieldData          getFieldData()  {
        return inputData[0];
    }
    public    SoEngineOutputData   getOutputData() {
        return outputData[0];
    }
    public
    static SoFieldData[]         getInputDataPtr()
    { return ( SoFieldData[])inputData; }
    public   static SoEngineOutputData[] getOutputDataPtr()
    { return ( SoEngineOutputData[])outputData; }
    private
    static SoType       classTypeId;    /* Type id              */
    //private  static boolean       firstInstance = true;  /* True for first ctor call */
    private  static final SoFieldData[]  inputData = new SoFieldData[1];     /* Info on input fields */
    private  static final SoEngineOutputData[]  outputData = new SoEngineOutputData[1];            /* Info on outputs */
    private  static final SoFieldData[][]    parentInputData = new SoFieldData[1][];      /* parent's fields */
    private  static final SoEngineOutputData[][] parentOutputData = new SoEngineOutputData[1][];

    // SO_ENGINE_ABSTRACT_HEADER

    public static SoElapsedTime createInstance() {
        return new SoElapsedTime();
    }

    //! \name Inputs
    //@{

    //! Running time.
    public final SoSFTime            timeIn = new SoSFTime();

    //! Scale factor for time.
    public final SoSFFloat           speed = new SoSFFloat();

    //! TRUE to start running, FALSE to stop.
    public final SoSFBool            on = new SoSFBool();

    //! TRUE to freeze, FALSE to continue running.
    public final SoSFBool            pause = new SoSFBool();

    //! Reset the base time.
    public final SoSFTrigger         reset = new SoSFTrigger();

    //@}

    //! While the engine is on, it keeps track of 'clock' time.
    //! This is the amount of real time that passes multiplied by the speed.
    //! If the speed input varies while the engine is on, then 'clock' time
    //! advances non-uniformly.
    //!
    //! Pausing the engine will freeze the timeOut value, but internally the
    //! 'clock' time will continue to advance.
    //! Unpause the engine and the timeOut will jump forward to display
    //! 'clock' time.
    //!
    //! Stop the engine (by setting 'on' to FALSE) to freeze both the
    //! timeOut value and the 'clock' time. Re-start (by setting 'on'
    //! to TRUE) and both timeOut and 'clock' will continue from where
    //! they were at the time the engine was stopped.
    //!
    public final SoEngineOutput      timeOut = new SoEngineOutput();

    private enum State {
        STOPPED,        //!< Clock is not running
        RUNNING,        //!< Clock is running
        PAUSED          //!< Clock is running, but output is frozen
    };


    private final SbTime pausetime = new SbTime();
    private final SbTime lasttime = new SbTime();
    private final SbTime currtime = new SbTime();
    private State status;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

    public SoElapsedTime()
//
////////////////////////////////////////////////////////////////////////
    {
        engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoElapsedTime.class,this);

        engineHeader.SO_ENGINE_CONSTRUCTOR(SoElapsedTime.class, inputData, outputData, parentInputData[0], parentOutputData[0]);
        engineHeader.SO_ENGINE_ADD_INPUT(timeIn,"timeIn",   (SbTime.zero()));
        engineHeader.SO_ENGINE_ADD_INPUT(speed,"speed", (1.0f));
        engineHeader.SO_ENGINE_ADD_INPUT(on,"on", (true));
        engineHeader.SO_ENGINE_ADD_INPUT(pause,"pause", (false));
        engineHeader.SO_ENGINE_ADD_INPUT(reset,"reset", (null));
        engineHeader.SO_ENGINE_ADD_OUTPUT(timeOut,"timeOut", SoSFTime.class);

        // default time source connection
        SoField realtime = SoDB.getGlobalField(new SbName("realTime"));
        timeIn.connectFrom(realtime);

        this.currtime.copyFrom(SbTime.zero());
        this.lasttime.copyFrom(((SoSFTime)realtime).getValue());
        status = SoElapsedTime.State.RUNNING;

        isBuiltIn   = true;
    }

    // Documented in superclass.
    protected void evaluate()
    {
        if (this.status == SoElapsedTime.State.STOPPED) {
            SoSubEngine.SO_ENGINE_OUTPUT(timeOut, SoSFTime.class, (Object time) -> ((SoSFTime)time).setValue(this.currtime));
        }
        else {
            SbTime now = new SbTime(this.timeIn.getValue());
            this.currtime.operator_add_equal( (now.operator_minus(this.lasttime)).operator_mul(this.speed.getValue()));
            this.lasttime.copyFrom(now);
            if (this.status == SoElapsedTime.State.PAUSED) {
                SoSubEngine.SO_ENGINE_OUTPUT(timeOut, SoSFTime.class, (Object time) -> ((SoSFTime)time).setValue(this.pausetime));
            }
            else {
                SoSubEngine.SO_ENGINE_OUTPUT(timeOut, SoSFTime.class, (Object time) -> ((SoSFTime)time).setValue(this.currtime));
            }
        }
    }

    // Documented in superclass.
    public void inputChanged(SoField which)
    {
        if (which == this.timeIn) return;

        // Default to turn output off, only turn it back on if the engine is
        // running.
        this.timeOut.enable(false);

        if (which == this.reset) {
            this.currtime.copyFrom(SbTime.zero());
            this.lasttime.copyFrom(this.timeIn.getValue());
        }
        else if (which == this.pause) {
            if (this.pause.getValue() && this.status == SoElapsedTime.State.RUNNING) {
                this.status = SoElapsedTime.State.PAUSED;
                this.pausetime.copyFrom(this.currtime);
            }
            else if (!this.pause.getValue() && this.status == SoElapsedTime.State.PAUSED) {
                this.status = SoElapsedTime.State.RUNNING;
            }
        }
        else if (which == this.on) {
            if (this.on.getValue() && this.status == SoElapsedTime.State.STOPPED) {
                this.status = SoElapsedTime.State.RUNNING;
                this.lasttime.copyFrom(this.timeIn.getValue());
            }
            else if (!this.on.getValue() && this.status != SoElapsedTime.State.STOPPED) {
                this.status = SoElapsedTime.State.STOPPED;
            }
        }

        this.timeOut.enable(this.status == SoElapsedTime.State.RUNNING);
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoElapsedTime class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoElapsedTime.class, "ElapsedTime", SoEngine.class, parentInputData, parentOutputData);
    }

}
