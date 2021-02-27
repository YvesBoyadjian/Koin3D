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
 |      This file defines some macros that implement things common to
 |      subclasses of SoEngine. They may be used to make SoEngine
 |      subclassing easier. In all of the macros, the "className"
 |      parameter refers to the name of the engine subclass.
 |
 |   Defined macros:
 |
 |      Within class header:
 |
 |              SO_ENGINE_HEADER(className)
 |              SO_ENGINE_ABSTRACT_HEADER(className)
 |
 |      Within class source:
 |
 |          At file scope:
 |
 |              SO_ENGINE_SOURCE(className)
 |              SO_ENGINE_ABSTRACT_SOURCE(className)
 |
 |          Inside the initClass method:
 |
 |              SO_ENGINE_INIT_CLASS()
 |              SO_ENGINE_INIT_ABSTRACT_CLASS()
 |
 |          Inside the constructor:
 |
 |              SO_ENGINE_CONSTRUCTOR(className)
 |              SO_ENGINE_ADD_INPUT(inputName, (defaultValue))
 |              SO_ENGINE_ADD_OUTPUT(outputName, outputType)
 |              SO_ENGINE_DEFINE_ENUM_VALUE(enumType, enumValue)
 |              SO_ENGINE_IS_FIRST_INSTANCE()   //!< a boolean value
 |
 |                 //!< the following are defined in <fields/So[SM]fEnum.h>:
 |              SO_ENGINE_SET_SF_ENUM_TYPE(fieldName,enumType)
 |              SO_ENGINE_SET_MF_ENUM_TYPE(fieldName,enumType)
 |
 |          Inside the evaluate method:
 |
 |              SO_ENGINE_OUTPUT(outputName, outputType, method)
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoType.CreateMethod;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFEnum;
import jscenegraph.database.inventor.fields.SoMField;
import jscenegraph.database.inventor.fields.SoSField;
import jscenegraph.port.SbNameArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSubEngine {

	Class<? extends SoEngine> class1; 
	private final SoEngine soEngine;
	
	private boolean firstInstance = true;

	private SoFieldData[] inputData; 
	private SoEngineOutputData[] outputData;
	
	public SoSubEngine(Class<? extends SoEngine> class2, SoEngine soEngine2) {
		this.class1 = class2;
		this.soEngine = soEngine2;
	}

//////////////////////////////////////////////////////////////////////////////
///
/// Macro to be called within each constructor
///
	
	public void alternate_init(		
			final SoFieldData[] inputData, 
		final SoEngineOutputData[] outputData
) {
		this.inputData = inputData;
		this.outputData = outputData;		
	}

public boolean SO_ENGINE_CONSTRUCTOR(Class<? extends SoEngine> className, 
		final SoFieldData[] inputData, 
		final SoEngineOutputData[] outputData,
		final SoFieldData[] parentInputData,
		final SoEngineOutputData[] parentOutputData) {
	
	this.inputData = inputData;
	this.outputData = outputData;
	
    int _value_false= 0;
    
    do {                                                                      
        SO__ENGINE_CHECK_INIT(className);                                     
        if (inputData[0] == null) {                                              
            inputData[0] = new SoFieldData(parentInputData != null ?                     
                                        parentInputData[0] : null);             
            outputData[0] = new SoEngineOutputData(parentOutputData != null ?            
                                                parentOutputData[0] : null);    
        }                                                                     
        else                                                                  
            firstInstance = false;                                            
        //isBuiltIn = false; java port : false by default anyway                                                    
    } while (_value_false != 0);
    return firstInstance;
    }

protected void SO__ENGINE_CHECK_INIT(Class<? extends SoEngine> className) {                                      
    if (soEngine.getClassTypeId().equals(SoType.badType())) {                                   
        SoDebugError.post("SO_ENGINE_CONSTRUCTOR",                           
                           "Can't construct an engine of type "+               
                           className.getSimpleName()+                              
                           "until initClass() has been called");              
        return;                                                               
    }
}

/////////////////////////////////////////////////
///
/// This is a boolean value that can be tested
/// in constructors.
///

	public boolean SO_ENGINE_IS_FIRST_INSTANCE() { return (firstInstance == true); }

	
/////////////////////////////////////////////
///
/// This takes care of writing the value to all connected outputs.
/// This should be called in the evaluate() routine.
///

	public static void SO_ENGINE_OUTPUT(SoEngineOutput outputName, Class<?> type, Consumer<Object> code) {                              
    boolean _value_false= false;                                                      
    do {                                                                      
        if (outputName.isEnabled()) {                                         
            for (int _eng_out_i = 0;                                          
                 _eng_out_i < outputName.getNumConnections();                 
                 _eng_out_i++) {                                              
                SoField _eng_out_temp = (SoField) outputName.operator_square_bracket(_eng_out_i);        
                if (!_eng_out_temp.isReadOnly()) {                           
                    //_eng_out_temp->code;
                	code.accept(_eng_out_temp);
                }                                                             
            }                                                                 
        }                                                                     
    } while (_value_false);                                                   
    }

	public static SoSubEngine SO_ENGINE_HEADER(Class<? extends SoEngine> class1, SoEngine soEngine) {
		return SO_ENGINE_ABSTRACT_HEADER(class1, soEngine);
	}

	public static SoSubEngine SO_ENGINE_ABSTRACT_HEADER(Class<? extends SoEngine> class1, SoEngine soEngine) {
		return new SoSubEngine(class1,soEngine);
	}

/////////////////////////////////////////////
///
/// This adds the info for an input to the SoFieldData and sets the
/// default value for it. The parameters are as follows:
///      inputName:      the name of the input (as a member)
///      defValue:       the default value enclosed in parentheses
///
/// For example,
///
///      SO_ENGINE_ADD_INPUT(vector1, (0, 0, 0));
///      SO_ENGINE_ADD_INPUT(triggerTime, (0.0));
///
/// adds info about inputs named vector1 and triggerTime with the
/// given default values.  The inputs must be public member variables 
/// of a type derived from SoField.
///

	public void SO_ENGINE_ADD_INPUT(SoSField input, String inputName, Object defaultValue) {
    boolean _value_false= false;                                                      
    do {                                                                      
        //SO__ENGINE_CHECK_CONSTRUCT(__FILE__);                                 
        if (firstInstance)                                                    
            inputData[0].addField(class1,soEngine, inputName, input);                            
        input.setValue(defaultValue);                                    
        input.setContainer(soEngine);                                   
    } while (_value_false);                                                   
	}

	public void SO_ENGINE_ADD_MINPUT(SoMField input, String inputName, Object ... defaultValue) {
	    boolean _value_false= false;                                                      
	    do {                                                                      
	        //SO__ENGINE_CHECK_CONSTRUCT(__FILE__);                                 
	        if (firstInstance)                                                    
	            inputData[0].addField(class1,soEngine, inputName, input);                            
	        input.setValue(defaultValue[0]);                                    
	        input.setContainer(soEngine);                                   
	    } while (_value_false);                                                   
		}

/////////////////////////////////////////////
///
/// This adds the info for an output to the SoEngineOutputData.
/// The parameters are as follows:
///      fieldName:      the name of the output (as a member)
///      type:           the type of the output (name of SoField subclass)
///
/// For example,
///
///      SO_ENGINE_ADD_OUTPUT(result, SoSFVec3f);
///      SO_ENGINE_ADD_OUTPUT(hour, SoSFInt32);
///
/// adds info about outputs named result and int32_t that can be hooked up
/// to fields of the given type.
/// The outputs must be public member variables of type SoEngineOutput.
///

	public void SO_ENGINE_ADD_OUTPUT(SoEngineOutput output, String outputName, Class<? extends SoSField> type) {
	    boolean _value_false= false;                                                      
	    do {                                                                      
	        //SO__ENGINE_CHECK_CONSTRUCT(__FILE__);                                 
	        if (firstInstance)                                                    
	            outputData[0].addOutput(soEngine, outputName,                
	                                  output,                          
	                                  SoField.getClassTypeId(type));                    
	        output.setContainer(soEngine);                                  
	    } while (_value_false);                                                   
	}

	public void SO_ENGINE_ADD_MOUTPUT(SoEngineOutput output, String outputName, Class<? extends SoMField> type) {
	    boolean _value_false= false;                                                      
	    do {                                                                      
	        //SO__ENGINE_CHECK_CONSTRUCT(__FILE__);                                 
	        if (firstInstance)                                                    
	            outputData[0].addOutput(soEngine, outputName,                
	                                  output,                          
	                                  SoField.getClassTypeId(type));                    
	        output.setContainer(soEngine);                                  
	    } while (_value_false);                                                   
	}

////////////////////////////////////////////////////////////
///
///  Internal initialization macros
///

public static SoType SO__ENGINE_INIT_CLASS(final Class className, String classPrintName, Class parentClass, 
		final SoFieldData[][] parentInputData, final SoEngineOutputData[][] parentOutputData) {
	
	SoType classTypeId = null;
	
    boolean _value_false= false;                                                      
    do {                                                                      
//        classTypeId =                                                         
//            SoType.createType(parentClass.getClassTypeId(),                 
//                               classPrintName,                                
//                               &className.createInstance);                   
//        parentInputData = parentClass.getInputDataPtr();                     
//        parentOutputData = parentClass.getOutputDataPtr();                   
    	try {
    		Method getClassTypeId = parentClass.getMethod("getClassTypeId");
    		try {
    			SoType parentType = (SoType)getClassTypeId.invoke(null);
    	        classTypeId = SoType.createType(parentType, new SbName(classPrintName), new CreateMethod() {

					@Override
					public Object run() {
						try {
							return 	className.newInstance();//className.getMethod("createInstance").invoke(null);
						} catch (IllegalAccessException | IllegalArgumentException |
								 SecurityException e) {
				    		throw new IllegalStateException(e);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
				    		throw new IllegalStateException(e);
						}
					}
    	        	
    	        }
    	        	);
    	        Method getInputDataPtr = parentClass.getMethod("getInputDataPtr");
    	        Method getOutputDataPtr = parentClass.getMethod("getOutputDataPtr");
    	        parentInputData[0] = (SoFieldData[])getInputDataPtr.invoke(null);                          
    	        parentOutputData[0] = (SoEngineOutputData[])getOutputDataPtr.invoke(null);                        
    			return classTypeId;
    		} catch (IllegalArgumentException e) {
    			throw new IllegalStateException(e);
    		} catch (IllegalAccessException e) {
    			throw new IllegalStateException(e);
    		} catch (InvocationTargetException e) {
    			throw new IllegalStateException(e);
    		}
    	} catch (SecurityException e) {
    		throw new IllegalStateException(e);
    	} catch (NoSuchMethodException e) {
    		throw new IllegalStateException(e);
    	}
    } while (_value_false);
    }

protected static SoType SO__ENGINE_INIT_ABSTRACT_CLASS(Class<? extends SoEngine> className, String classPrintName,
        Class<? extends SoEngine> parentClass, final SoFieldData[][] parentInputData, final SoEngineOutputData[][] parentOutputData) {
	try {
		Method getClassTypeId = parentClass.getMethod("getClassTypeId");
		try {
			SoType parentType = (SoType)getClassTypeId.invoke(null);
	        SoType classTypeId = SoType.createType(parentType, new SbName(classPrintName));
	        Method getInputDataPtr = parentClass.getMethod("getInputDataPtr");
	        Method getOutputDataPtr = parentClass.getMethod("getOutputDataPtr");
	        parentInputData[0] = (SoFieldData[])getInputDataPtr.invoke(null);                          
	        parentOutputData[0] = (SoEngineOutputData[])getOutputDataPtr.invoke(null);                        
			return classTypeId;
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	} catch (SecurityException e) {
		throw new IllegalStateException(e);
	} catch (NoSuchMethodException e) {
		throw new IllegalStateException(e);
	}
}

/////////////////////////////////////////////
///
/// This registers a value of an enum type.
///      enumType:       the name of the enum type
///      enumValue:      the name of a value of that enum type
///
/// If a engine defines an enum, each of the enum's values
/// should be registered using this macro.  For example:
///
///      [ in MyFunc.h file: ]
///      class MyFunc {
///              ...
///              enum Chipmunk { ALVIN, SIMON, THEODORE };
///              ...
///      }
///      
///      [ in constructor MyFunc::MyFunc(): ]
///      SO_ENGINE_DEFINE_ENUM_VALUE(Chipmunk, ALVIN);
///      SO_ENGINE_DEFINE_ENUM_VALUE(Chipmunk, SIMON);
///      SO_ENGINE_DEFINE_ENUM_VALUE(Chipmunk, THEODORE);
///

public void SO_ENGINE_DEFINE_ENUM_VALUE( Enum value) {
	  String enumType = value.getClass().getSimpleName();
	  String enumValueName = value.name();
	  
	  Method getValueMethod;
	try {
		getValueMethod = value.getClass().getMethod("getValue", null);
		  int enumValue = (Integer)getValueMethod.invoke(value,null);
		  SO_ENGINE_DEFINE_ENUM_VALUE(enumType,enumValueName,enumValue);
	} catch (SecurityException e) {
		throw new IllegalStateException(e);
	} catch (NoSuchMethodException e) {
		throw new IllegalStateException(e);
	} catch (IllegalArgumentException e) {
		throw new IllegalStateException(e);
	} catch (IllegalAccessException e) {
		throw new IllegalStateException(e);
	} catch (InvocationTargetException e) {
		throw new IllegalStateException(e);
	}
	  
}

	public void SO_ENGINE_DEFINE_ENUM_VALUE(String enumType, String enumValueName, int enumValue) {                     
    int _value_false= 0;                                                      
    do {                                                                      
        SO__ENGINE_CHECK_CONSTRUCT(/*__FILE__*/);                                 
        if (firstInstance)                                                    
            inputData[0].addEnumValue(enumType, enumValueName, enumValue);         
    } while (_value_false != 0);                                                   
    }

	private void SO__ENGINE_CHECK_CONSTRUCT() {                                   
    int _value_false= 0;                                                      
    do {                                                                      
        if (inputData == null) {                                              
            SoDebugError.post("",                                         
                               "Instance not properly constructed.\n"+         
                               "Did you forget to put "+        
                               "SO_ENGINE_CONSTRUCTOR()"+                      
                               " in the constructor?");                       
//            inputData[0] = new SoFieldData(parentInputData != null ?                     
//                                        parentInputData[0] : null);             
//            outputData[0] = new SoEngineOutputData(parentOutputData != null ?            
//                                                parentOutputData[0] : null);    
        }                                                                     
    } while(_value_false != 0);                                                    
    }

	public void SO_ENGINE_SET_MF_ENUM_TYPE(SoMFEnum field, String fieldName, String enumType)  {                      
		SO__MF_ENUM_SET_TYPE(field, fieldName,enumType,"ENGINE",inputData[0]);
	}

/////////////////////////////////////////////
///
/// This defines the specific type of enum expected by a particular
/// SoMFEnum field.
///

public void SO__MF_ENUM_SET_TYPE(SoMFEnum field, String fieldName, String enumType, String contMacroName, SoFieldData contData) {  
    int _value_false= 0;                                                      
    do {                                                                      
        final int[] _so_mf_enum_num = new int[1];                                                  
        final int[][] _so_mf_enum_vals = new int[1][];                                                
        final SbNameArray[] _so_mf_enum_names = new SbNameArray[1];                                            
        contData.getEnumData(enumType,                            
                                _so_mf_enum_num,                              
                                _so_mf_enum_vals,                             
                                _so_mf_enum_names);                           
        SO__MF_ENUM_CHECK_DATA(_so_mf_enum_vals[0],                              
                               enumType,                           
                               fieldName,                          
                               contMacroName);                                
        field.setEnums(_so_mf_enum_num[0],                                   
                           _so_mf_enum_vals[0],                                  
                           _so_mf_enum_names[0]);                                
    } while (_value_false != 0);                                                   
    }

	public void SO__MF_ENUM_CHECK_DATA(int[] vals, String typeName, String fieldName, String containerMacroName) {  
    int _value_false= 0;                                                      
    do {                                                                      
        if (vals == null && firstInstance)                                    
            SoDebugError.post("SO_SET_MF_ENUM_TYPE",                         
                               "Field "+fieldName+" (%s, line %d): Did you forget to"+    
                               " use SO_"+containerMacroName+"_DEFINE_ENUM_VALUE("+typeName+", ...)?"      
                               /*, __FILE__, __LINE__,*/                 
                               );                 
    } while (_value_false != 0);                                                   
    }
	
}
