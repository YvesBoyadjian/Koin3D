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
 |      This file contains the implementation of the TimeCounter engine
 |
 |   Classes:
 |      SoTimeCounter
 |
 |   Author(s)          : Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFShort;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.fields.SoSFTrigger;
import jscenegraph.database.inventor.fields.SoSField;

/**
 * @author Yves Boyadjian
 *
 */
////////////////////////////////////////////////////////////////////////////////
//! Timed integer counter.
/*!
\class SoTimeCounter
\ingroup Engines
This engine is a counter that outputs numbers, starting
at a minimum value, increasing by a step value, and ending with
a number that does not exceed the maximum value.  When the 
maximum number is reached, it starts counting from the beginning again. 


The difference between this engine and the SoCounter engine, is
that this engine also has a \b timeIn  input, which allows the
counting cycles to be timed.  This engine counts automatically over time;
it does not need to be triggered to go to the next step.
By default, the \b timeIn  input is connected to the \b realTime  global field.  
It can, however, be connected to any time source. 


The \b frequency  input field controls how many min-to-max cycles
are performed per second.  For example, a \b frequency  value of 0.5 means
that it will take 2 seconds to complete a single min-to-max cycle.


The steps in the count cycle do not necessarily all have the same duration.
Using the \b duty  input field, you can arbitrarily split the time period
of the count cycle between the steps.
For example, if there are 5 steps in the cycle, a duty input of 
(1., 2., 2., 2., 1.) 
will make the second, third, and fourth steps take twice as long as the 
first and last steps.


At any time the counter can be reset to a specific value.  If you
set the \b reset  input field to a value, the engine will
continue counting from there.  Note that the counter will
always output numbers based on the \b min , \b max  and \b step  values, 
and setting the \b reset  value does not affect the those input fields.
If the reset value is not a legal counter value, the counter will still
behave as though it is:
\code
If reset is greater than max, the counter is set to max.
If reset is less than min, the counter is set to min.
If reset is between step values, the counter is set to the lower step.
\endcode
Each time a counting cycle is started, the \b syncOut  output is triggered.  
This output can be used to synchronize some other event with the 
counting cycle.
Other events can also synchronize the counter by triggering the 
\b syncIn  input.


You can pause the engine, by setting the \b on  input to false, and
it will stop updating the output field. 
When you turn off the pause, by setting \b on  to true, it 
will start counting again from where it left off.

\par File Format/Default
\par
\code
TimeCounter {
  min 0
  max 1
  step 1
  on true
  frequency 1
  duty 1
  timeIn 1316437066.463
  reset 0
}
\endcode

\par See Also
\par
SoCounter, SoElapsedTime, SoEngineOutput
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTimeCounter extends SoEngine {

    //SO_ENGINE_HEADER(SoGate);

	
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

    //! \name Inputs
    //@{

    //! Minimum value for the counter.
    public final SoSFShort           min = new SoSFShort();            

    //! Maximum value for the counter.
    public final SoSFShort           max = new SoSFShort();            

    //! Counter step value.
    public final SoSFShort           step = new SoSFShort();           

    //! Counter pauses if this is set to false.
    public final SoSFBool            on = new SoSFBool();             

    //! Number of \b min -to-\b max  cycles per second.
    public final SoSFFloat           frequency = new SoSFFloat();      

    //! Duty cycle values.
    public final SoMFFloat           duty = new SoMFFloat();           

    //! Running time.
    public final SoSFTime            timeIn = new SoSFTime();         

    //! Restart at the beginning of the cycle.
    public final SoSFTrigger         syncIn = new SoSFTrigger();         

    //! Reset the counter to the specified value.
    public final SoSFShort           reset = new SoSFShort();          

    //@}

    //! \name Outputs
    //@{

    //! Counts \b min -to-\b max , in \b step  increments.
    public final SoEngineOutput      output = new SoEngineOutput();         

    //! Triggers at cycle start.
    public final SoEngineOutput      syncOut = new SoEngineOutput();        

    private enum State {
        ON,                     //!< counting
        PAUSED                  //!< holding
    }
    private State state;
    private enum Todo {
        RECALC(1<<0),
        RESET(1<<1),
        SYNC(1<<2),
        PAUSE(1<<3),
        UNPAUSE(1<<4);
    	
    	private int value;
        
        Todo(int value) {
        	this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
    };
    
    private int        todo;
    private final SbTime              period = new SbTime();         //!< total cycle time
    private int                 nStages;
    private int                 curStage;
    private int                 prevStage;
    
    
    private static class Stage {
        public int     val;            //!< counter value for stage
        public float   duty;           //!< normalized fraction of cycle
        public final SbTime  offset = new SbTime();         //!< time from start of cycle    	
    }
	private Stage[] stages;
    private final SbTime              baseTime = new SbTime();       //!< Starting time for cycle
    private final SbTime              pauseOffset = new SbTime();    //!< Starting time for cycle
	  
	 // SO_ENGINE_ABSTRACT_HEADER
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTimeCounter()
//
////////////////////////////////////////////////////////////////////////
{
	super();
	engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoTimeCounter.class,this);

	engineHeader.SO_ENGINE_CONSTRUCTOR(SoTimeCounter.class,inputData, outputData, parentInputData[0], parentOutputData[0]);
	engineHeader.SO_ENGINE_ADD_INPUT(min,"min", (short)(0));
	engineHeader.SO_ENGINE_ADD_INPUT(max,"max", (short)(1));
	engineHeader.SO_ENGINE_ADD_INPUT(step,"step", (short)(1));
	engineHeader.SO_ENGINE_ADD_INPUT(on,"on", (true));
	engineHeader.SO_ENGINE_ADD_INPUT(frequency,"frequency", (1.0f));
	engineHeader.SO_ENGINE_ADD_MINPUT(duty,"duty", (1.0f));
	engineHeader.SO_ENGINE_ADD_INPUT(timeIn,"timeIn",   (SbTime.zero()));
	engineHeader.SO_ENGINE_ADD_INPUT(syncIn,"syncIn", (null)); //java port
	engineHeader.SO_ENGINE_ADD_INPUT(reset,"reset", (0));
	engineHeader.SO_ENGINE_ADD_OUTPUT(output,"output", SoSFShort.class);
	engineHeader.SO_ENGINE_ADD_OUTPUT(syncOut,"syncOut", SoSFTrigger.class);

    state               = State.ON;
    todo                = Todo.RECALC.getValue()|Todo.SYNC.getValue();
    stages              = null;
    curStage            = -1;
    prevStage           = -1;
    isBuiltIn           = true;

    // default connection for time source
    timeIn.connectFrom(SoDB.getGlobalField("realTime"));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    stages = null;//delete [] stages;
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Writes instance to SoOutput. Takes care of not writing out
//    connection to realTime that is created in constructor.
//
// Use: private

//public void writeInstance(SoOutput out)
////
//////////////////////////////////////////////////////////////////////////
//{
//    // If connected to realTime, disable connection to prevent
//    // connection from being written and mark timeIn as default to
//    // prevent value from being written
//    boolean timeInWasDefault = timeIn.isDefault();
//    boolean timeInConnectionEnabled = timeIn.isConnectionEnabled();
//    final SoField[] timeInConnection = new SoField[1]; //ptr
//    timeIn.getConnectedField(timeInConnection);
//    if (timeInConnection[0] == SoDB.getGlobalField("realTime")) {
//        timeIn.enableConnection(false);
//        timeIn.setDefault(true);
//    }
//    super.writeInstance(out);
//
//    timeIn.enableConnection(timeInConnectionEnabled);
//    timeIn.setDefault(timeInWasDefault);
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads stuff into instance.  Works around a problem with old
//    files that contain explicit references to the default
//    connection to realTime.
//
// Use: private

public boolean
readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    boolean result = super.readInstance(in, flags);
    todo |= Todo.RESET.getValue();
    final SoField[] timeInConnection = new SoField[1];
    timeIn.getConnectedField(timeInConnection);
    if (timeInConnection[0] == SoDB.getGlobalField("realTime")) {
        timeIn.disconnect();
        timeIn.connectFrom(timeInConnection[0]);
    }

    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Detects when the inputs changed.  Notifications are only propogated
// when the output is going to change; periodic clock-ticks are suppressed
// while waiting for the next stage
//
// Use: private

protected void
inputChanged(SoField whichInput)
//
////////////////////////////////////////////////////////////////////////
{
    // get current time value
    SbTime now = timeIn.getValue();

    boolean enable = false;
    boolean syncEnable = /*!!*/(todo& Todo.SYNC.getValue())!=0;
    if (whichInput == min ||
        whichInput == max ||
        whichInput == step ||
        whichInput == frequency ||
        whichInput == duty) {
        todo |= Todo.RECALC.getValue();
    }
    else if (whichInput == reset) {
        todo |= Todo.RESET.getValue();
    }
    else if (whichInput == syncIn) {
        todo |= Todo.SYNC.getValue();
        syncEnable = true;
    }
    else if (whichInput == on) {
        if (!on.getValue() && state != State.PAUSED) {
            todo |= Todo.PAUSE.getValue();
            todo &= ~Todo.UNPAUSE.getValue();
        }
        else if (on.getValue() && state == State.PAUSED) {
            todo |= Todo.UNPAUSE.getValue();
            todo &= ~Todo.PAUSE.getValue();
        }
    }
    else if (whichInput == timeIn && on.getValue() && stages != null) {
        final SbTime off = now.operator_minus(baseTime);
        if (off.operator_greater_equal(stages[curStage+1].offset) || off.operator_less( stages[curStage].offset)) {
            enable = true;
            if (off.operator_greater( stages[nStages].offset))
                syncEnable = true;
        }
    }
    if (todo!=0) enable = true;

    output.enable(enable);
    syncOut.enable(syncEnable);

//#ifdef DEVELOP
//    printf("TimeCounter::inputChanged %s state=%s on=%d reset=%d enable=%d syncEnable=%d todo=",
//           whichInput==&min     ? "min   " :
//           whichInput==&max     ? "max   " :
//           whichInput==&step    ? "step  " :
//           whichInput==&reset   ? "reset " :
//           whichInput==&syncIn  ? "syncIn" :
//           whichInput==&on              ? "on    " :
//           whichInput==&timeIn  ? "------" :
//           " ...  ",
//           state==ON            ? "ON    " :
//           state==PAUSED                ? "PAUSED" :
//           "??????",
//           on.getValue(),
//           reset.getValue(),
//           enable,
//           syncEnable);
//    if (todo) {
//        int t=todo;
//        if (todo&RECALC) printf("RECALC,"), t &= ~RECALC;
//        if (todo&RESET) printf("RESET,"), t &= ~RESET;
//        if (todo&SYNC) printf("SYNC,"), t &= ~SYNC;
//        if (todo&PAUSE) printf("PAUSE,"), t &= ~PAUSE;
//        if (todo&UNPAUSE) printf("UNPAUSE,"), t &= ~UNPAUSE;
//        if (t) printf("????");
//    }
//    else printf("0");
//    printf("\n");
//#endif

    /***********
    **  
    ** first do the things queued up in the "todo" bitmask
    */

    // recalculate timing/counting info
    if ((todo&Todo.RECALC.getValue())!=0) {

        // get values
        int mn = min.getValue();
        int mx = max.getValue();
        int stp = step.getValue();
        if (stp == 0) stp = 1;
        if (mn > mx) { int t = mn; mn=mx; mx=t; }

        int nDuty = duty.getNum();
        period.setValue( 1.0f/frequency.getValue());

        // reallocate stage array
        //delete [] stages; java port
        nStages = 1+Math.abs((mx-mn)/stp);
        stages  = new Stage[nStages + 1];        // sentinnel on end
        for(int i=0;i<=nStages;i++) {
        	stages[i] = new Stage();
        }

        // make sure curStage isn't out of range
        while (curStage >= nStages)     curStage -= nStages;

        // fill stage values and duties (non normalized)
        int i, val;
        for (i=0, val=stp>0?mn:mx; i<nStages; i++, val+=stp) {
            stages[i].val = val;
            stages[i].duty = (i < nDuty) ? duty.operator_square_bracket(i) : 1.0f;
        }

        // normalize duties
        double total;
        for (total=0, i=0; i<nStages; i++) total += stages[i].duty;
        for (i=0; i<nStages; i++) stages[i].duty /= total;

        // set offsets
        stages[0].offset.setValue(0.0f);
        for (i=1; i<nStages; i++) {
            stages[i].offset.copyFrom(stages[i-1].offset.operator_add(period.operator_mul(stages[i-1].duty)));
        }
        stages[nStages].offset.copyFrom(period);        // sentinnel value

//#ifdef DEVELOP
//        // debugging output
//        printf("TimeCounter::evaluate RECALC min=%d max=%d step=%d "
//               "nStages=%d freq=%g period=%g\n",
//               mn, mx, stp, nStages, frequency.getValue(), period);
//        for (i=0; i<nStages; i++) {
//            printf("\tstage %d:\tval=%d\tduty=%g\toffset=%g\n",
//                   i, stages[i].val, stages[i].duty, stages[i].offset.getValue());
//        }
//#endif
    }

    // adjust the base time to reset the cycle
    if ((todo&Todo.SYNC.getValue())!=0) {

        curStage = 0;
        baseTime.copyFrom(now);
        pauseOffset.setValue(0.0f);      // in case we're currently paused

//#ifdef DEVELOP
//        printf("TimeCounter::evaluate SYNC now=%s baseTime=%s\n",
//               now.format("%s.%i").getString(),
//               baseTime.format("%s.%i").getString());
//#endif
    }

    // adjust the base time to reset the cycle
    if ((todo&Todo.RESET.getValue())!=0) {
        // get inputs
        final int r = reset.getValue();
        int stp = step.getValue();
        if (stp==0) stp = 1;

        // convert input value to stage index
        curStage = (r-stages[0].val)/stp;
        if (curStage < 0)
            curStage = 0;
        else if (curStage >= nStages)
            curStage = nStages-1;

        // adjust base time so that requested stage is starting now.
        baseTime.copyFrom(now.operator_minus(stages[curStage].offset));
        pauseOffset.copyFrom(stages[curStage].offset);  // in case we're currently paused

//#ifdef DEVELOP
//        printf("TimeCounter::evaluate RESET reset=%d curStage=%d "
//               "val=%d offset=%s now=%s baseTime=%s\n",
//               r, curStage, stages[curStage].val,
//               stages[curStage].offset.format("%s.%i").getString(),
//               now.format("%s.%i").getString(),
//               baseTime.format("%s.%i").getString());
//#endif
    }

    // when initiating a pause, remember the offset into the cycle
    if ((todo&Todo.PAUSE.getValue())!=0) {
        pauseOffset.copyFrom(now.operator_minus(baseTime));
        state = State.PAUSED;
//#ifdef DEVELOP
//        printf("TimeCounter::evaluate PAUSE now=%s baseTime=%s pauseOffset=%s\n",
//               now.format("%s.%i").getString(),
//               baseTime.format("%s.%i").getString(),
//               pauseOffset.format("%s.%i").getString());
//#endif
    }

    // to stop a pause, adjust the base time so that the saved offset
    // is maintained
    if ((todo&Todo.UNPAUSE.getValue())!=0) {
        baseTime.copyFrom(now.operator_minus(pauseOffset));
        state = State.ON;
//#ifdef DEVELOP
//        printf("TimeCounter::evaluate UNPAUSE now=%s pauseOffset=%s "
//               "baseTime=%s\n",
//               now.format("%s.%i").getString(),
//               pauseOffset.format("%s.%i").getString(),
//               baseTime.format("%s.%i").getString());
//#endif
    }

    todo = 0;
    /*
    ** done with the things queued up in the "todo" bitmask
    **  
    ************/
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Evaluation routine
//
// Use: private

protected void
evaluate()
//
////////////////////////////////////////////////////////////////////////
{
    int i;
    // get current time value
    SbTime now = timeIn.getValue();

    // find out the current segment
    if (state==State.ON) {

        // move baseTime to the start of the current cycle
        while (now.operator_minus(baseTime).operator_greater_equal( period)) baseTime.operator_add_equal(period);
        while (baseTime.operator_minus(now).operator_greater(period)) baseTime.operator_minus_equal(period);

        // search for the offset
        final SbTime offset = now.operator_minus(baseTime);
        for (i=nStages-1; i!=0; i--)
            if (offset.operator_greater_equal( stages[i].offset))
                break;

        curStage = i;
    }

//#ifdef DEVELOP
//printf("TimeCounter::evaluate state=%s now=%s baseTime=%s offset=%s "
//       "curStage=%d output=%d\n",
//       state==ON                ? "ON    " :
//       state==PAUSED            ? "PAUSED" : "??????",
//       now.format("%s.%i").getString(),
//       baseTime.format("%s.%i").getString(),
//       (now-baseTime).format("%s.%i").getString(),
//       curStage,
//       stages[curStage].val);
//#endif
    SoSubEngine.SO_ENGINE_OUTPUT(output, SoSFShort.class, (o)-> ((SoSField)o).setValue(stages[curStage].val));
    prevStage = curStage;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoCalculator class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
	classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoTimeCounter.class, "TimeCounter", SoEngine.class, parentInputData, parentOutputData);
}

	
}
