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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file defines the SoGate class.
 |
 |   Author(s)          : Ronen Barzel, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import static jscenegraph.database.inventor.misc.SoBasic.SO__CONCAT;

import java.util.Objects;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
//import jscenegraph.database.inventor.SoOutput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMField;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFName;
import jscenegraph.database.inventor.fields.SoSFTrigger;

/**
 * @author Yves Boyadjian
 *
 */



////////////////////////////////////////////////////////////////////////////////
//! Selectively copies its input to its output.
/*!
\class SoGate
\ingroup Engines
This engine selectively copies its input to its output.
The type of the input field can be any subclass of SoMField.
The type is specified when an instance of the class is created.
For example,
SoGate(SoMFFloat::getClassTypeId())
creates an engine that copies floating-point values.


The \b enable  input controls continous flow-through of values.
While \b enable  is TRUE, the input will be copied to the output. 
Alternatively, by touching the \b trigger  input, you can 
copy a single value from the input to the output.

 
Note that unlike most other engine fields, \b input  and
\b output  are pointers.
Note also that by default \b input  does not contain any values.

\par See Also
\par
SoEngineOutput, SoConcatenate, SoSelectOne
*/
////////////////////////////////////////////////////////////////////////////////

public class SoGate extends SoEngine {

    //SO_ENGINE_HEADER(SoGate);

	
	public                                                                     
	  static SoType getClassTypeId() { return classTypeId; }                      
//	public    SoType      getTypeId()  /* Returns type id   */
//	{
//		return classTypeId;
//	}
//	  public                                                                     
//	     SoFieldData          getFieldData()  {
//		  return inputData[0];
//	  }
//	public    SoEngineOutputData   getOutputData() {
//		return outputData[0];
//	}
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
	
	  public final SoSFBool enable = new SoSFBool();
	  public final SoSFTrigger trigger = new SoSFTrigger();
	  
	    //! Note that unlike most engines the input field is a pointer.
	    //! The default value for this field is no values.
	  public SoMField input;
	  
    //! Note that unlike most engines the output is a pointer.  The
    //! type of the output is the same as the input.
    public SoEngineOutput      output;

    private int conversionCase;
    
    private SoFieldData myInputData;
    private SoEngineOutputData myOutputData;

    private final SoSFName typeField = new SoSFName(); //!< Used when reading/writing
    

// Constants for all of the multi-value fields; gate's input can be
// any of the SoMF types, and its output will be the corresponding
// SoSF type.

private enum TypeConst {
    BitMask,
    Bool,
    Color,
    Enum,
    Float,
    Int32,
    Matrix,
    Name,
    Node,
    Path,
    Plane,
    Rotation,
    Short,
    String,
    Time,
    UInt32,
    UShort,
    Vec2f,
    Vec3f,
    Vec4f,
    BAD_TYPE
};    


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor; takes type of gate
//
// Use: public

public SoGate(SoType inputType)
//
////////////////////////////////////////////////////////////////////////
{
	super();
	engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoGate.class,this);

	engineHeader.SO_ENGINE_CONSTRUCTOR(SoGate.class,inputData, outputData, parentInputData[0], parentOutputData[0]);
	engineHeader.SO_ENGINE_ADD_INPUT(enable,"enable", (false));
	engineHeader.SO_ENGINE_ADD_INPUT(trigger,"trigger", null);

    myInputData = new SoFieldData(inputData[0]);
    myOutputData = new SoEngineOutputData(outputData[0]);
    input = null;

    setup(inputType);
    typeField.setValue(inputType.getName());

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor, used only when reading from file:
//
// Use: private
//
public SoGate()
//
////////////////////////////////////////////////////////////////////////
{
	super();
	engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoGate.class,this);

	engineHeader.SO_ENGINE_CONSTRUCTOR(SoGate.class, inputData, outputData, parentInputData[0], parentOutputData[0]);
	engineHeader.SO_ENGINE_ADD_INPUT(enable,"enable", (false));
	engineHeader.SO_ENGINE_ADD_INPUT(trigger,"trigger", null);

    myInputData = new SoFieldData(inputData[0]);
    myOutputData = new SoEngineOutputData(outputData[0]);
    input = null;

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private
//
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    myInputData.destructor();
    myOutputData.destructor();
    if (input != null) {
        input.destructor();
        output.destructor();
    }
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates input and output fields and input/output data
//
// Use: private
//

//This handy macro sets up conversionCase, which is used to quickly
//decide what type we're hooked up to at evaluate() time:

private boolean DECIDE(SoType inputType, String klass) {
     try {
		if(Objects.equals(inputType,SoType.getClassTypeId(SO__CONCAT("SoMF",klass)))) { 
		      conversionCase = TypeConst.valueOf(klass).ordinal(); 
		      return true;
		 }
	} catch (ClassNotFoundException e) {
		SoDebugError.post("SoGate::setup", e.getMessage());
		//throw new IllegalStateException(e);
	}                
     return false;
}

private void
setup(SoType inputType)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (input != null) {
        SoDebugError.post("SoGate::setup",
                           "Already initialized!");
    }
//#endif

    if (inputType.isDerivedFrom(SoMField.getClassTypeId(SoMField.class))) {
        input = (SoMField )inputType.createInstance();
    } else {
        input = null;
    }
    if (input == null) {
//#ifdef DEBUG
        SoDebugError.post("SoGate::setup",
                   "Couldn't create field of type "+ inputType.getName().getString());
//#endif
        conversionCase = TypeConst.BAD_TYPE.ordinal();
    } else {

        input.setContainer(this);

        // Pass in the static field data as the parent field data for
        // the per-instance field data:
        myInputData.addField(this.getClass(),this, "input", input);
    
        // Construct the output:
        output = new SoEngineOutput();
        output.setContainer(this);
        myOutputData.addOutput(this, "output", output, inputType);
        
        // Set up for which switch to use in evaluate() routine:
        if (DECIDE(inputType,"BitMask")) {}
        else if (DECIDE(inputType,"Bool")){}
        else if (DECIDE(inputType,"Color")){}
        else if (DECIDE(inputType,"Enum")){}
        else if (DECIDE(inputType,"Float")){}
        else if (DECIDE(inputType,"Int32")){}
        else if (DECIDE(inputType,"Matrix")){}
        else if (DECIDE(inputType,"Name")){}
        else if (DECIDE(inputType,"Node")){}
        else if (DECIDE(inputType,"Path")){}
        else if (DECIDE(inputType,"Plane")){}
        else if (DECIDE(inputType,"Rotation")){}
        else if (DECIDE(inputType,"Short")){}
        else if (DECIDE(inputType,"String")){}
        else if (DECIDE(inputType,"Time")){}
        else if (DECIDE(inputType,"UInt32")){}
        else if (DECIDE(inputType,"UShort")){}
        else if (DECIDE(inputType,"Vec2f")){}
        else if (DECIDE(inputType,"Vec3f")){}
        else if (DECIDE(inputType,"Vec4f")){}
//#undef DECIDE
        else {
//#ifdef DEBUG
            SoDebugError.post("SoGate::setup",
                        "Can't gate field of type "+inputType.getName().getString());
//#endif
            conversionCase = TypeConst.BAD_TYPE.ordinal();
        }
    }
}    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Notice when fields change to enable/disable output
//
// Use: private
//
public void inputChanged(SoField whichInput)
//
////////////////////////////////////////////////////////////////////////
{
    if (whichInput == enable) {
        output.enable(enable.getValue());
    }
    else if (whichInput == trigger)
        output.enable(true);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Redefines this to create an instance of the correct type.
//
// Use: internal, virtual

public SoFieldContainer 
copyThroughConnection() 
//
////////////////////////////////////////////////////////////////////////
{
    // See SoEngine::copyThroughConnection() for details of this...
    SoFieldContainer copy = findCopy(this, true);
    if (copy != null)
        return copy;
    if (shouldCopy()) {
        // Create and add a new instance to the dictionary
        SoType inputType = SoType.fromName(typeField.getValue());
        SoEngine newEngine = new SoGate(inputType);
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
//    Write input to output, and disable output (if necessary).
//
// Use: private
//

//Handy macro for doing type-correct setValues(getValues())
private boolean CASE(SoMField outField, int conversionCase, String klass) {
	if(conversionCase == TypeConst.valueOf(klass).ordinal()) {
		(/*(SO__CONCAT("SoMF",klass) )*/outField). 
		setValues(0, /*input.getNum(),*/ 
     (/*(SO__CONCAT("SoMF",klass))*/input).getValues(0)); 
		return true;
	}
	return false;
}
                              
public void
evaluate()
//
////////////////////////////////////////////////////////////////////////
{
    trigger.getValue(); // Force evaluation

    // For efficiency and to reduce bloat, we don't use the standard
    // SO_ENGINE_OUTPUT macro:

    if (!output.isEnabled()) return;

    for (int i = 0; i < output.getNumConnections(); i++) {
        SoMField outField = (SoMField )(output).operator_square_bracket(i);
        if (outField.isReadOnly()) continue;
        
        //switch(conversionCase) {
            if(CASE(outField,conversionCase,"BitMask")) {}
            else if(CASE(outField,conversionCase,"Bool")) {}
            else if(CASE(outField,conversionCase,"Color")) {}
            else if(CASE(outField,conversionCase,"Enum")) {}
            else if(CASE(outField,conversionCase,"Float")) {}
            else if(CASE(outField,conversionCase,"Int32")) {}
            else if(CASE(outField,conversionCase,"Matrix")) {}
            else if(CASE(outField,conversionCase,"Name")) {}
            else if(CASE(outField,conversionCase,"Node")) {}
            else if(CASE(outField,conversionCase,"Path")) {}
            else if(CASE(outField,conversionCase,"Plane")) {}
            else if(CASE(outField,conversionCase,"Rotation")) {}
            else if(CASE(outField,conversionCase,"Short")) {}
            else if(CASE(outField,conversionCase,"String")) {}
            else if(CASE(outField,conversionCase,"Time")) {}
            else if(CASE(outField,conversionCase,"UInt32")) {}
            else if(CASE(outField,conversionCase,"UShort")) {}
            else if(CASE(outField,conversionCase,"Vec2f")) {}
            else if(CASE(outField,conversionCase,"Vec3f")) {}
            else if(CASE(outField,conversionCase,"Vec4f")) {}
//#undef CASE
            else if(conversionCase ==  TypeConst.BAD_TYPE.ordinal()) {
            ; // Do nothing, already complained
            }
            else {
            // Something is seriously wrong:
//#ifdef DEBUG
            SoDebugError.post("SoGate::evaluate",
                           "conversionCase is "+conversionCase+"!");
//#endif
            }
        //}
        outField.setNum(input.getNum());
    }

    output.enable(enable.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns type id.
//
// Use: public
//
public SoType getTypeId()
//
////////////////////////////////////////////////////////////////////////
{
    return classTypeId;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns field data
//
// Use: internal
//
public SoFieldData getFieldData() 
//
////////////////////////////////////////////////////////////////////////
{
    return myInputData;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns output date
//
// Use: internal
//
public SoEngineOutputData getOutputData()
//
////////////////////////////////////////////////////////////////////////
{
    return myOutputData;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create an instance of the class, using private constructor.
//    Used only by database when reading in from file.
//
// Use: internal
//
public static Object createInstance()
//
////////////////////////////////////////////////////////////////////////
{
    return new SoGate();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Special read code needed to read in type of Gate engine.
//
// Use: private

public boolean
readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    final SbName typeName = new SbName();
    if (!in.read(typeName, true) ||
                typeName.operator_not_equal("type") || !typeField.read(in, new SbName("type"))) {

        SoReadError.post(in, "SoGate is missing type field");
        return false;
    }

    SoType inputType = SoType.fromName(typeField.getValue());
    if (! inputType.isDerivedFrom(SoMField.getClassTypeId(SoMField.class))) {
        SoReadError.post(in, "\""+typeField.getValue()+"\" is not a type of MField");
        return false;
    }
    
    setup(inputType);

    return super.readInstance(in, flags);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Special write code to write out type info
//
// Use: private

//private void
//writeInstance(SoOutput out)
////
//////////////////////////////////////////////////////////////////////////
//{
//    if (! writeHeader(out, false, true)) {
//
//        // Write type info
//        typeField.write(out, "type");
//
//        SoFieldData fieldData = getFieldData();
//
//        if (fieldData != null)
//            fieldData.write(out, this);
//
//        writeFooter(out);
//    }
//}    
	
	
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
	classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoGate.class, "Gate", SoEngine.class, parentInputData, parentOutputData);
}

	
}
