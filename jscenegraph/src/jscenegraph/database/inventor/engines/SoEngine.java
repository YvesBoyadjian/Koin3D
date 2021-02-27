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
 |      This file defines the base SoEngine class, and the
 |      SoEngineOutput class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import java.lang.reflect.Field;

import jscenegraph.coin3d.inventor.engines.SoEngineAble;
import jscenegraph.coin3d.inventor.engines.SoInterpolate;
import jscenegraph.coin3d.inventor.engines.SoInterpolateVec3f;
import jscenegraph.coin3d.inventor.engines.SoNodeEngine;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoEngineList;
import jscenegraph.database.inventor.SoEngineOutputList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;


////////////////////////////////////////////////////////////////////////////////
//! Base class for all engines.
/*!
\class SoEngine
\ingroup Engines
SoEngine is the abstract base class for all engines.
Engines are objects used for animation and behavior.
They are lightweight objects that are connected between
nodes, the clock, and other engines to form interesting
behaviorial objects (e.g., a spinning windmill).


Engines are used to animate parts of a scene and/or to constrain
one part of a scene in relation to some other part of the scene.
An engine receives a number of input values, performs some operation
on them, and then copies the results into one or more
output fields.  Both the inputs and the outputs can be connected
to other fields or engines in the scene graph.  When an engine's
output values change, those new values are sent to any fields or
engines connected to them.

\par See Also
\par
SoBoolOperation, SoCalculator, SoComposeMatrix, SoComposeRotation, SoComposeRotationFromTo, SoComposeVec2f, SoComposeVec3f, SoComposeVec4f, SoComputeBoundingBox, SoConcatenate, SoCounter, SoDecomposeMatrix, SoDecomposeRotation, SoDecomposeVec2f, SoDecomposeVec3f, SoDecomposeVec4f, SoElapsedTime, SoGate, SoInterpolate, SoOnOff, SoOneShot, SoSelectOne, SoTimeCounter, SoTransformVec3f, SoTriggerAny
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoEngine extends SoFieldContainer implements SoEngineAble {

	protected SoSubEngine engineHeader; 
	
	private
		        static final SoType       classTypeId = new SoType();            //!< Type identifier
		   	
	boolean needsEvaluation;
    private boolean notifying;
	
	   public
		    
		        //! Returns the type identifier for the SoEngine class.
		        static SoType       getClassTypeId() { return new SoType(classTypeId); }
		   	
	     //! This is used by the input&output inheritence mechanism, hidden in
	        //! the SoSubEngine macros
	  public static SoFieldData[]            getInputDataPtr()  { return null; }
	  public static SoEngineOutputData[]     getOutputDataPtr() { return null; }
	    	   
	
	protected abstract void evaluate();
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor.  Engines are assumed to need evaluation when
//    first created (this is probably being paranoid, since
//    needsEvaluation is set when a connection is made to a engine
//    output, but hi-ho...).
//
// Use: protected

protected SoEngine()
//
////////////////////////////////////////////////////////////////////////
{
	this.engineHeader = engineHeader;
	
    needsEvaluation = true;
    notifying = false;
//#ifdef DEBUG
    if (! SoDB.isInitialized())
        SoDebugError.post("SoEngine::SoEngine",
                           "Cannot construct engines before "+
                           "calling SoDB::init()");
//#endif /* DEBUG */
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.  Called when its ref count goes to zero.
//
// Use: protected

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // Note: the SoEngine destructor doesn't have to worry about
    // unhooking connections to its inputs or outputs for the
    // following reasons:
    // -- a engine will only be deleted when it's outputs are
    // disconnected from everything, since making a connection to an
    // output increments the engines reference count.  So outputs
    // are OK.
    // -- The inputs are taken care of by their destructors (which
    // call disconnect()) and by SoBase::~SoBase, which disconnects
    // any forward connections to the engine's input fields.

    // In the debug case, our output's destructors will make sure they
    // aren't connected to anything, and will complain if they are.
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns a list of outputs in this engine.
//
// Use: public

public int
getOutputs(final SoEngineOutputList list)
//
////////////////////////////////////////////////////////////////////////
{
    int                         i;
    SoEngineOutputData    od = getOutputData();

    if (od == null)
        return 0;

    for (i = 0; i < od.getNumOutputs(); i++) {
        list.append(od.getOutput(this, i));
    }
    return od.getNumOutputs();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns a pointer to the output with the given name. If no such
//    output exists, NULL is returned.
//
// Use: public

public SoEngineOutput 
getOutput(final SbName outputName)
//
////////////////////////////////////////////////////////////////////////
{
    int                         i;
    SoEngineOutputData    od = getOutputData();

    if (od == null)
        return null;

    // Search outputs for one with given name
    for (i = 0; i < od.getNumOutputs(); i++)
        if (od.getOutputName(i).operator_equal_equal(outputName))
            return od.getOutput(this, i);

    // Not found...
    return null;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns (in outputName) the name of the output pointed to.
//    Returns FALSE if the output is not contained within the engine
//    instance.
//
// Use: public

public boolean
getOutputName(final SoEngineOutput output,
                          final SbName outputName)
//
////////////////////////////////////////////////////////////////////////
{
    int                         i;
    final SoEngineOutputData    od = getOutputData();

    if (od == null)
        return false;

    // Search outputs for one with given pointer
    for (i = 0; i < od.getNumOutputs(); i++) {
        if (od.getOutput(this, i) == output) {
            outputName.copyFrom(od.getOutputName(i));
            return true;
        }
    }

    // Not found...
    return false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates and returns a copy of the engine. All connections to
//    inputs are copied as is (without copying what's at the other end).
//
// Use: public

public SoEngine 
copy() 
//
////////////////////////////////////////////////////////////////////////
{
    // Set up an empty copy dictionary. The dictionary has to exist
    // for copying to work, even though this operation will never
    // store anything in it.
    initCopyDict();

    // Create a copy of this engine
    SoEngine newEngine = (SoEngine ) getTypeId().createInstance();
    newEngine.ref();

    // Copy the contents
    newEngine.copyContents(this, true);

    // Clean up
    copyDone();

    // Return the copy
    newEngine.unrefNoDelete();
    return newEngine;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the last engine given the name 'name'.  Returns NULL if
//    there is no engine with the given name.
//
// Use: public

public SoEngine 
getByName(final SbName name)
//
////////////////////////////////////////////////////////////////////////
{
    return (SoEngine )getNamedBase(name, SoEngine.getClassTypeId());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds all engines named 'name' to the list.  Returns the number of
//    engines found.
//
// Use: public

public int
getByName(final SbName name, final SoEngineList list)
//
////////////////////////////////////////////////////////////////////////
{
    return getNamedBases(name, list, SoEngine.getClassTypeId());
}

	
	
////////////////////////////////////////////////////////////////////////
//
//Description:
//This engine is a wrapper around the user's evaluate() routine.
//It takes care of some bookkeeping tasks common to all engines,
//and is called by SoField.getValue() routines.
//
//Use: internal

	public void evaluateWrapper() {
		
	    if (!needsEvaluation) return;

	    // Break cycles:
	    needsEvaluation = false;

	    int i;
	    final SoEngineOutputData od = getOutputData();
	    for (i = 0; i < od.getNumOutputs(); i++) {
	        SoEngineOutput out = od.getOutput(this, i);
	        out.prepareToWrite();
	    }
	    // Evaluate all our inputs:
	    // (this works around some problems with engines that
	    // don't always get their inputs during evaluation):
	    final SoFieldData fieldData = getFieldData();
	    for (i = 0; i < fieldData.getNumFields(); i++) {
	        SoField inputField = fieldData.getField(this, i);
	        inputField.evaluate();
	    }

	    evaluate();

	    for (i = 0; i < od.getNumOutputs(); i++) {
	        SoEngineOutput out = od.getOutput(this, i);
	        out.doneWriting();
	    }
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Propagates notification through the engine.
//
// Use: internal

public void
notify(final SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    if (notifying) return; // Avoid potential loops

    // There's an annoying bug with engines that enable and disable
    // their outputs in their inputChanged method that requires this
    // flag:
    notifying = true;

    needsEvaluation = true;

    SoNotRec lastRec = list.getLastRec();
    boolean notifiedFromContainer = 
        (lastRec != null && lastRec.getType() == SoNotRec.Type.CONTAINER);

    // If we are being notified by a change to one of our fields
    // (notification type is CONTAINER), pass that info on to any
    // subclass that cares.
    if (notifiedFromContainer) {
        inputChanged(list.getLastField());
    }

    // We may have auditors (fields that point to this engine), so
    // notify them before we add to the list
    super.notify(list);

    // Append a record of type ENGINE with the base set to this
    final SoNotRec    rec = new SoNotRec(this);
    rec.setType(SoNotRec.Type.ENGINE);
    list.append(rec);

    // Now notify our outputs' connected fields
    final SoNotList   workingList = new SoNotList(list);
    boolean        firstConnection = true;

    SoEngineOutputData od = getOutputData();
    for (int i = 0; i < od.getNumOutputs(); i++) {
        SoEngineOutput out = od.getOutput(this, i);
        if (out.isEnabled()) {
            for (int j = 0; j < out.getNumConnections(); j++) {

                // Make sure we save the original list for use each
                // time through the loop
                if (firstConnection)
                    firstConnection = false;
                else
                    workingList.copyFrom(list);

                out.operator_square_bracket(j).notify(workingList);
            }
        }
    }
    notifying = false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called whenever the value of an input is changed. The
//    default method does nothing. Subclasses can override this to
//    detect when a specific field is changed.
//
// Use: protected

protected void
inputChanged(SoField field)
//
////////////////////////////////////////////////////////////////////////
{
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    During a copy operation, this copies an instance that is
//    encountered through a field connection.
//
// Use: internal, virtual

public SoFieldContainer 
copyThroughConnection() 
//
////////////////////////////////////////////////////////////////////////
{
    // If there is already a copy of this engine, use it
    SoFieldContainer copy = findCopy(this, true);
    if (copy != null)
        return copy;

    // Otherwise, we need to figure out whether to create a copy of
    // this engine or to just return "this". We should return a copy
    // if this engine is "inside" (see SoNode::copy() for details). To
    // determine this, we need to check if we are connected from any
    // inside node or engine. (We know that if we get here, we must be
    // connected TO at least one inside node or engine, so we don't
    // have to worry about that direction.)

    if (shouldCopy()) {

        // Create and add a new instance to the dictionary
        SoEngine newEngine = (SoEngine ) getTypeId().createInstance();
        newEngine.ref();
        addCopy(this, newEngine);               // Adds a ref()
        newEngine.unrefNoDelete();

        // Find the copy and return it; this has the side effect of
        // copying the contents and letting the dictionary know it has
        // been copied once.
        return findCopy(this, true);
    }

    // Otherwise, just return this
    return (SoFieldContainer ) this;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Recursive procedure that determines if this engine should be
//    copied during a copy operation, or just referenced as is.
//
// Use: internal

public boolean
shouldCopy()
//
////////////////////////////////////////////////////////////////////////
{
    // We need to determine if we are connected from any "inside" node
    // or engine. Look through each connected input to determine this.

    SoFieldData fieldData = getFieldData();
    if (fieldData != null) {
        int numFields = fieldData.getNumFields();
        for (int i = 0; i < numFields; i++) {
            SoField inputField = fieldData.getField(this, i);
            if (inputField.referencesCopy())
                return true;
        }
    }

    // If we get here, we didn't find any inside connections
    return false;
}

	
	
	public abstract SoEngineOutputData getOutputData();
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the base SoEngine class.  Called by
	   //    SoDB::init().
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // Allocate a new engine type id
	       // No real parent id
	       classTypeId.copyFrom( SoType.createType(SoFieldContainer.getClassTypeId(),
	           new SbName("Engine"), null, (short)0));
	   }
	   	
	
	// Initialize ALL Inventor engine classes.
	public static void initClasses() {
		
	     //
		        // base class
		        //
		        SoEngine.initClass();

        SoNodeEngine.initClass();
		    
		        //
		        // collections of engines init'd by their own base class method
		        //
//		        SoInterpolate.initClasses();
		        SoInterpolate.initClass();
		        //SoInterpolateFloat.initClass();
		        //SoInterpolateRotation.initClass();
		        //SoInterpolateVec2f.initClass();
		        SoInterpolateVec3f.initClass();
		        //SoInterpolateVec4f.initClass();
		    
		        // Field converters
		        SoFieldConverter.initClass();  // Must come first
		        SoBuiltinFieldConverter.initClass();
//		        SoConvToTrigger.initClass();
		    
		        //
		        // composers/decomposers
		        //
		        //SoComposeMatrix.initClass();
		        //SoComposeRotation.initClass();
//		        SoComposeRotationFromTo.initClass();
//		        SoComposeVec2f.initClass();
		        //SoComposeVec3f.initClass();
//		        SoComposeVec4f.initClass();
//		        SoDecomposeMatrix.initClass();
		        //SoDecomposeRotation.initClass();
//		        SoDecomposeVec2f.initClass();
		        //SoDecomposeVec3f.initClass();
//		        SoDecomposeVec4f.initClass();
		    
		       // miscellaneous engines
		       SoBoolOperation.initClass();
		       SoCalculator.initClass();
//		       SoComputeBoundingBox.initClass();
//		       SoConcatenate.initClass();
//		       SoCounter.initClass();
		       SoElapsedTime.initClass();
		       SoGate.initClass();
//		       SoOnOff.initClass();
//		       SoOneShot.initClass();
//		       SoSelectOne.initClass();
		       SoTimeCounter.initClass();
		       //SoTransformVec3f.initClass();
//		       SoTriggerAny.initClass();
//		       SoUnknownEngine.initClass();
		  		
	}
	
	/**
	 * Java port 
	 * @param offset
	 * @return null if not found
	 */
	public SoEngineOutput plus(String offset) {
		
		Field[] publicFields = getClass().getFields();
		for(Field publicField : publicFields) {
			if(publicField.getName().equals(offset)) {
				try {
					return (SoEngineOutput)publicField.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	public int  clamp(int i, int n) {
		return (i < n) ? i : n-1; 
	}
	
    //! A very annoying double notification occurs with engines
    //! that enable their outputs during inputChanged; this flag
    //! prevents that:
    public boolean                isNotifying() { return notifying; }

}
