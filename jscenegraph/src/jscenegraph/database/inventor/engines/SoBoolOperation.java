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
 |      This file contains the declaration of the BoolOperation engine
 |
 |   Classes:
 |      SoBoolOperation
 |
 |   Author(s)          : Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFBool;
import jscenegraph.database.inventor.fields.SoMFEnum;
import jscenegraph.database.inventor.fields.SoMField;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Performs Boolean operations.
/*!
\class SoBoolOperation
\ingroup Engines
This engine performs a Boolean operation on two inputs,
and returns both the result of the operation and its inverse.


The input fields can have multiple values, allowing the engine to 
perform several Boolean operations in parallel. 
One input may have more values than the other.  In that case,
the last value of the shorter input will be repeated as necessary.

\par File Format/Default
\par
\code
BoolOperation {
  a FALSE
  b FALSE
  operation A
}
\endcode

\par See Also
\par
SoEngineOutput, SoCalculator
*/
////////////////////////////////////////////////////////////////////////////////

public class SoBoolOperation extends SoEngine {
	
    //SO_ENGINE_HEADER(SoBoolOperation);
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
   	
  public

    enum Operation {
            CLEAR,
             SET,
            A,
        NOT_A,
                 B,
                 NOT_B,
            A_OR_B,
        NOT_A_OR_B,
            A_OR_NOT_B,
        NOT_A_OR_NOT_B,
            A_AND_B,
        NOT_A_AND_B,
            A_AND_NOT_B,
        NOT_A_AND_NOT_B,
            A_EQUALS_B,
            A_NOT_EQUALS_B;
    	
    	public int getValue() {
    		return ordinal();
    	}
    };
	  
    //! First argument to the Boolean operation.
    public final SoMFBool    a = new SoMFBool();

    //! Second argument to the Boolean operation.
    public final SoMFBool    b = new SoMFBool();

    //! The Boolean operation.
    public final SoMFEnum    operation = new SoMFEnum();

    //@}

    //! \name Outputs
    //@{

    //! Result of the Boolean operation applied to the inputs.
    public final SoEngineOutput output = new SoEngineOutput();      

    //! Inverse of \b output .
    public final SoEngineOutput inverse = new SoEngineOutput();     

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoBoolOperation()
//
////////////////////////////////////////////////////////////////////////
{
	super();
	engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoBoolOperation.class,this);
   	
	engineHeader.SO_ENGINE_CONSTRUCTOR(SoBoolOperation.class, inputData, outputData, parentInputData[0], parentOutputData[0]);
	engineHeader.SO_ENGINE_ADD_MINPUT(a,"a",        (false));
	engineHeader.SO_ENGINE_ADD_MINPUT(b,"b",        (false));
	engineHeader.SO_ENGINE_ADD_MINPUT(operation,"operation",        (Operation.A.ordinal()));
	engineHeader.SO_ENGINE_ADD_MOUTPUT(output,"output", SoMFBool.class);
	engineHeader.SO_ENGINE_ADD_MOUTPUT(inverse,"inverse", SoMFBool.class);

	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.CLEAR);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.SET);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_A);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_OR_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_A_OR_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_OR_NOT_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_A_OR_NOT_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_AND_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_A_AND_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_AND_NOT_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.NOT_A_AND_NOT_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_EQUALS_B);
	engineHeader.SO_ENGINE_DEFINE_ENUM_VALUE(Operation.A_NOT_EQUALS_B);

	engineHeader.SO_ENGINE_SET_MF_ENUM_TYPE(operation,"operation", "Operation");
    isBuiltIn = true;
}


    

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.engines.SoEngine#evaluate()
	 */
	@Override
	protected void evaluate() {
    int na = a.getNum();
    int nb = b.getNum();
    int noperation = operation.getNum();
    int nout = Math.max(Math.max(na,nb),noperation);
    SoSubEngine.SO_ENGINE_OUTPUT(output, SoMFBool.class, (o)-> ((SoMFBool)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(inverse, SoMFBool.class, (o) -> ((SoMFBool)o).setNum(nout));

    for (int i=0; i<nout; i++) {
        boolean va = a.operator_square_bracket(SoEngineUtil.clamp(i,na));
        boolean vb = b.operator_square_bracket(SoEngineUtil.clamp(i,nb));
        boolean result = false;
        switch (Operation.values()[operation.operator_square_bracket(SoEngineUtil.clamp(i,noperation))]) {
        case CLEAR:             result = false;                 break;
        case SET:               result = true;                  break;
        case A:                 result = va;                    break;
        case NOT_A:             result = !va;                   break;
        case B:                 result = vb;                    break;
        case NOT_B:             result = !vb;                   break;
        case A_OR_B:            result = va || vb;              break;
        case NOT_A_OR_B:        result = (!va) || vb;           break;
        case A_OR_NOT_B:        result = va || (!vb);           break;
        case NOT_A_OR_NOT_B:    result = (!va) || (!vb);        break;
        case A_AND_B:           result = va && vb;              break;
        case NOT_A_AND_B:       result = (!va) && (vb);         break;
        case A_AND_NOT_B:       result = va && (!vb);           break;
        case NOT_A_AND_NOT_B:   result = (!va) && (!vb);        break;
        case A_EQUALS_B:        result = (va == vb);            break;
        case A_NOT_EQUALS_B:    result = (va != vb);            break;
        }
        final int ii = i;
        final boolean fresult = result;

        SoSubEngine.SO_ENGINE_OUTPUT(output, SoMFBool.class, (o) -> ((SoMFBool)o).set1Value(ii, fresult));
        SoSubEngine.SO_ENGINE_OUTPUT(inverse, SoMFBool.class, (o) -> ((SoMFBool)o).set1Value(ii, !fresult));
    }
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoBoolOperation class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
	classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoBoolOperation.class, "BoolOperation", SoEngine.class, parentInputData, parentOutputData);
}

}
