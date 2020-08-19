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
 |      many subclasses of SoNode. They may be used to make SoNode
 |      subclassing easier. In all of the macros, the "className"
 |      parameter refers to the name of the node subclass.
 |
 |      Methods containing the word "ABSTRACT" are to be used for
 |      abstract subclasses in place of the corresponding regular
 |      macro.  Note that abstract classes may not have fields (you
 |      cannot call ADD_FIELD in their constructor).
 |
 |   Defined macros:
 |
 |      Within class header:
 |
 |              SO_NODE_HEADER(className)
 |              SO_NODE_ABSTRACT_HEADER(className)
 |
 |      Within class source:
 |
 |          At file scope:
 |
 |              SO_NODE_SOURCE(className)
 |              SO_NODE_ABSTRACT_SOURCE(className)
 |
 |          Class initialization (initClass):
 |
 |              SO_NODE_INIT_CLASS()
 |              SO_NODE_INIT_ABSTRACT_CLASS()
 |
 |          Constructor initialization:
 |
 |              SO_NODE_CONSTRUCTOR(className)
 |              SO_NODE_ADD_FIELD(fieldMember,(defaultValue))
 |              SO_NODE_DEFINE_ENUM_VALUE(enumType,enumValue)
 |              SO_NODE_IS_FIRST_INSTANCE()     //!< a boolean value
 |
 |                 //!< the following are defined in <fields/So[SM]fEnum.h>:
 |              SO_NODE_SET_SF_ENUM_TYPE(fieldName,enumType)
 |              SO_NODE_SET_MF_ENUM_TYPE(fieldName,enumType)
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell, Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoType.CreateMethod;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFEnum;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.fields.SoMFUInt32;
import jscenegraph.database.inventor.fields.SoMField;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSField;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.port.SoNodePtr;

/**
 * @author Yves Boyadjian
 * 
 * Java port
 *
 */
public class SoSubNode {

	protected Class thisClass;
	protected SoNode thisParent;
	
	  private static final Map<Class,SoType> classTypeId = new HashMap<Class,SoType>();            /* Type id              */    
	  private  static final Set<Class>      firstInstance = new HashSet<Class>(); /* true until 2nd c'tor call */        
	  private  static final Map<Class,SoFieldData[]> fieldData = new HashMap<Class,SoFieldData[]>();                                   
	  private  static final Map<Class, SoFieldData[][]>    parentFieldData = new HashMap<Class,SoFieldData[][]>();
	    
	  
	  protected SoSubNode(Class<? extends SoNode> class1, SoNode parent) {
		  
		  thisClass = class1;
		  thisParent = parent;
		  
		  if( !fieldData.containsKey(class1)) {
			  throw new IllegalStateException("Class "+ class1 + " not initialized");
		  }
	  }
	  
	public                                                                     
    static SoType       getClassTypeId(Class klass)        /* Returns class type id */   
                                    { 
		SoType type = classTypeId.get(klass);
		
		if(type == null) {
			type = SoType.badType();			
		}
		return type;
		}                
	
	// java port
	public SoType getClassTypeId() {
		return classTypeId.get(thisClass);
	}
	
  public                                                                  
    SoFieldData   getFieldData() {
	  return fieldData.get(thisClass)[0]; 
  }
  public  static SoFieldData[] getFieldDataPtr(Class klass)                              
        { return fieldData.get(klass); }
  
  
  public static SoSubNode SO_NODE_ABSTRACT_HEADER(Class<? extends SoNode> class1, SoNode parent) {
	return new SoSubNode(class1,parent);
  }	

  
  public static SoSubNode SO_NODE_HEADER(Class<? extends SoNode> class1, SoNode parent) {
	return new SoSubNode(class1,parent);
  }	

  public static void SO__NODE_INIT_ABSTRACT_CLASS(Class className, String classPrintName, 
		  Class<? extends SoBase> parentClass) {
	  SO__NODE_INIT_CLASS(className,classPrintName,parentClass,false);
  }
  	
/////////////////////////////////////////////
///
/// This initializes the type identifer variables defined in
/// SO_NODE_HEADER or SO_NODE_ABSTRACT_HEADER. This macro
/// should be called from within initClass(). The parentClass argument
/// should be the class that this subclass is derived from.
///

public static void SO_NODE_INIT_CLASS(Class className, String classPrintName, Class parentClass,String parentPrintClass) {
	SO__NODE_INIT_CLASS(className, classPrintName, parentClass);
//    if ((SoNode.nextActionMethodIndex <     0) ||                            
//        (SoNode.nextActionMethodIndex > 32767)){                             
//        SoDebugError.post("SO_NODE_INIT_CLASS",                              
//                           "Overflow of SoNode::nextActionMethodIndex");      
//        throw new RuntimeException();//abort();                                                              
//    }                
//    
//    SoType.CreateMethod createMethod = new SoType.CreateMethod() {
//		
//		@Override
//		public Object run() {
//			Constructor<?> constructor;
//			try {
//				constructor = className.getConstructor();
//			} catch (NoSuchMethodException | SecurityException e) {
//				throw new RuntimeException(e);
//			}
//			try {
//				return constructor.newInstance();
//			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) {
//				throw new RuntimeException(e);
//			}/*className::createInstance;*/
//		}
//	};
//    
//    classTypeId.put(className,                                                             
//        SoType.createType(SoType.fromName(new SbName(parentPrintClass)),                
//                   new SbName(className.getSimpleName()),                                      
//                   createMethod,                                
//                   (short)(SoNode.nextActionMethodIndex++)));    
//    
//	  if( !fieldData.containsKey(className)) { // java port
//		  final SoFieldData[] fieldData1 = new SoFieldData[1];                                   
//		  final SoFieldData[][]    parentFieldData1 = new SoFieldData[1][];
//		  fieldData.put(className, fieldData1);
//		  parentFieldData.put(className, parentFieldData1);
//	  }
//	  else {
//		  throw new IllegalStateException("Class "+ className + " already initialized");
//	  }
//	  
//    //parentFieldData = parentClass::getFieldDataPtr();                         
//	  SoFieldData[][]    parentFieldData1 = parentFieldData.get(className);
//	  
//	  try {
//		parentFieldData1[0] = (SoFieldData[])parentClass.getMethod("getFieldDataPtr").invoke(null);
//	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
//			| SecurityException e) {
//		throw new RuntimeException(e);
//	}
//	  
}

	public static void SO_NODE_INIT_ABSTRACT_CLASS(Class className, Class parentClass, String parentPrintClass) {  
    if ((SoNode.nextActionMethodIndex <     0) ||                             
        (SoNode.nextActionMethodIndex > 32767)){                             
        SoDebugError.post("SO_NODE_INIT_ABSTRACT_CLASS",                     
                           "Overflow of SoNode::nextActionMethodIndex");      
        throw new RuntimeException();//abort();                                                              
    }                                                                         
    classTypeId.put(className,
        SoType.createType(SoType.fromName(new SbName(parentPrintClass)),                
                   new SbName(className.getSimpleName()),                                      
                   null,                                                      
                   (short)(SoNode.nextActionMethodIndex++)));   
    
	  if( !fieldData.containsKey(className)) { // java port
		  final SoFieldData[] fieldData1 = new SoFieldData[1];                                   
		  final SoFieldData[][]    parentFieldData1 = new SoFieldData[1][];
		  fieldData.put(className, fieldData1);
		  parentFieldData.put(className, parentFieldData1);
	  }
	  else {
		  throw new IllegalStateException("Class "+ className + " already initialized");
	  }
	  
    
    //parentFieldData = parentClass.getFieldDataPtr();
	  SoFieldData[][]    parentFieldData1 = parentFieldData.get(className);
	  
	  try {
		parentFieldData1[0] = (SoFieldData[])parentClass.getMethod("getFieldDataPtr").invoke(null);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
			| SecurityException e) {
		throw new RuntimeException(e);
	}
	  
}

  
  public static void SO__NODE_INIT_CLASS(
		  Class className, String classPrintName, 
		  Class<? extends SoBase> parentClass) {
	  SO__NODE_INIT_CLASS(className,classPrintName,parentClass,true);
  }
  protected static void SO__NODE_INIT_CLASS(
		  Class className, String classPrintName, 
		  Class<? extends SoBase> parentClass,
		  boolean create) {
	  	  
	  if( !fieldData.containsKey(className)) {
		  final SoFieldData[] fieldData1 = new SoFieldData[1];                                   
		  final SoFieldData[][]    parentFieldData1 = new SoFieldData[1][];
		  fieldData.put(className, fieldData1);
		  parentFieldData.put(className, parentFieldData1);
	  }
	  else {
		  throw new IllegalStateException("Class "+ className + " already initialized");
	  }
	  
	  SoFieldData[][]    parentFieldData1 = parentFieldData.get(className);
	  
	    if ((SoNode.nextActionMethodIndex <     0) ||                            
	            (SoNode.nextActionMethodIndex > 32767)){                             
	            SoDebugError.post("SO__NODE_INIT_CLASS",                             
	                               "Overflow of SoNode::nextActionMethodIndex");      
	            //abort();
	            throw new IllegalStateException("Overflow of SoNode::nextActionMethodIndex");
	        }
	    	SoType parent = null;
			try {
				parent = (SoType)parentClass.getMethod("getClassTypeId").invoke(null);
			} catch (IllegalArgumentException e2) {
				throw new IllegalStateException(e2);
			} catch (SecurityException e2) {
				throw new IllegalStateException(e2);
			} catch (IllegalAccessException e2) {
				throw new IllegalStateException(e2);
			} catch (InvocationTargetException e2) {
				throw new IllegalStateException(e2);
			} catch (NoSuchMethodException e2) {
				throw new IllegalStateException(e2);
			}
	    	
			try {
				CreateMethod createMethod = null;
				if(create) {
					final Constructor<?> constructor = className.getConstructor();

			    	createMethod = new CreateMethod() {

						@Override
						public Object run() {						
							try {
								return constructor.newInstance();
							} catch (IllegalArgumentException e) {
								throw new RuntimeException(e);
							} catch (InstantiationException e) {
								throw new RuntimeException(e);
							} catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							} catch (InvocationTargetException e) {
								throw new RuntimeException(e);
							}
						}
			    		
			    	};
				}
		        try {
					parentFieldData1[0] = (SoFieldData[])parentClass.getMethod("getFieldDataPtr").invoke(null);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}//parentClass.getFieldDataPtr();                         
		  
		        classTypeId.put(className, SoType.createType(parent/*parentClass.getClassTypeId()*/,                     
		                       new SbName(classPrintName),                                            
		                       createMethod/*className.createInstance*/,                                
		                       (short)(SoNode.nextActionMethodIndex++)));      
		        
			} catch (SecurityException e1) {
				throw new RuntimeException(e1);
			} catch (NoSuchMethodException e1) {
				throw new RuntimeException(e1);
			}
  }

  public void SO_NODE_CONSTRUCTOR(Class klass) {
	  if(!Objects.equals(klass,thisClass)) {
		  throw new IllegalArgumentException(klass + " : Wrong class, should be : " + thisClass);
	  }
	  SO_NODE_CONSTRUCTOR();
  }
	  
  public void SO_NODE_CONSTRUCTOR() {
	  
	  SoNode node = thisParent;
	  
	  SO__NODE_CHECK_INIT(thisClass);
	    if (fieldData.get(thisClass)[0] == null)                                                    
	    	fieldData.get(thisClass)[0] = new SoFieldData(                                          
            parentFieldData.get(thisClass)[0] != null ? parentFieldData.get(thisClass)[0][0] : null);                       
    else                                                                       {
        firstInstance.add(thisClass);//firstInstance = false;                                                
    }
    node.isBuiltIn = false;                                                        
	  
  }
  
  /**
   * Coin 3D
   */
  public void SO_NODE_INTERNAL_CONSTRUCTOR() {
	  SO_NODE_CONSTRUCTOR();
	  thisParent.isBuiltIn = true;
  }
  
  public void SO__NODE_CHECK_INIT(Class klass) {
	    if (classTypeId.get(klass) == null || classTypeId.get(klass).equals(SoType.badType())) {                                   
	        SoDebugError.post("SO_NODE_CONSTRUCTOR",                             
	                           "Can't construct a node of type "                  
	                           +klass.getSimpleName()+                               
	                           " until initClass() has been called");             
	        return;                                                               
	    }                                                                         	  
  }
  
  protected void SO__NODE_CHECK_CONSTRUCT() {
	  SoFieldData[] fd = fieldData.get(thisClass);
	    if (fd[0] == null) {                                                  
	        SoDebugError.post(thisClass.getSimpleName(),                                             
	                           "Instance not properly constructed.\n"+             
	                           "Did you forget to put SO_NODE_CONSTRUCTOR()"+      
	                           " in the constructor?");                           
	        fd[0] = new                                                       
	            SoFieldData(parentFieldData.get(thisClass)[0] != null ? parentFieldData.get(thisClass)[0][0] : null);           
	    }                                                                         
	   
  }
  
  public void SO_NODE_DEFINE_ENUM_VALUE( Enum value) {
	  String enumType = value.getClass().getSimpleName();
	  String enumValueName = value.name();
	  
	  Method getValueMethod;
	try {
		getValueMethod = value.getClass().getMethod("getValue", null);
		  int enumValue = (Integer)getValueMethod.invoke(value,null);
		  SO_NODE_DEFINE_ENUM_VALUE(enumType,enumValueName,enumValue);
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
  
  public boolean firstInstance() {
	  return !firstInstance.contains(thisClass);
  }
  
  public void SO_NODE_DEFINE_ENUM_VALUE(String enumType, String enumValueName, int enumValue) {
      SO__NODE_CHECK_CONSTRUCT();                                   
      if (firstInstance())                                                    
          fieldData.get(thisClass)[0].addEnumValue(enumType,                      
                              enumValueName,                         
                              enumValue);                                   
	  
  }
  
  public void SO_NODE_SET_SF_ENUM_TYPE(SoSFEnum field, String fieldName, String enumType) {
	  SO__SF_ENUM_SET_TYPE(field, fieldName, enumType,"NODE",fieldData.get(thisClass)[0]);
  }
  
  public void SO_NODE_SET_MF_ENUM_TYPE(SoMFEnum field, String fieldName, String enumType) {
	  SO__MF_ENUM_SET_TYPE(field, fieldName, enumType,"NODE",fieldData.get(thisClass)[0]);
  }  
  
  public void SO__SF_ENUM_SET_TYPE(SoSFEnum field, String fieldName, String enumType, String contMacroName, SoFieldData contData)
  {  
        final int[] _so_sf_enum_num = new int[1];                                                  
        final int[][] _so_sf_enum_vals = new int[1][];                                                
        final SbName[][] _so_sf_enum_names = new SbName[1][];                                            
        contData.getEnumData(enumType,                            
                                _so_sf_enum_num,                              
                                _so_sf_enum_vals,                             
                                _so_sf_enum_names);                           
        SO__SF_ENUM_CHECK_DATA(_so_sf_enum_vals[0],                              
                               enumType,                           
                               fieldName,                          
                               contMacroName);                                
        field.setEnums(_so_sf_enum_num[0],                                   
                           _so_sf_enum_vals[0],                                  
                           _so_sf_enum_names[0]);                                
    }
  
  public void SO__SF_ENUM_CHECK_DATA(int[] vals, String typeName, String fieldName, String containerMacroName) {
      if (vals == null && firstInstance())                                    
      SoDebugError.post("SO_SET_SF_ENUM_TYPE",                         
                         "Field "+ fieldName+": Did you forget to"+    
                         " use SO_"+containerMacroName+"_DEFINE_ENUM_VALUE("+typeName+", ...)?");                 	  
  }
  
  public void SO__MF_ENUM_SET_TYPE(SoMFEnum field, String fieldName, String enumType, String contMacroName, SoFieldData contData)
  {  
        final int[] _so_sf_enum_num = new int[1];                                                  
        final int[][] _so_sf_enum_vals = new int[1][];                                                
        final SbName[][] _so_sf_enum_names = new SbName[1][];                                            
        contData.getEnumData(enumType,                            
                                _so_sf_enum_num,                              
                                _so_sf_enum_vals,                             
                                _so_sf_enum_names);                           
        SO__SF_ENUM_CHECK_DATA(_so_sf_enum_vals[0],                              
                               enumType,                           
                               fieldName,                          
                               contMacroName);                                
        field.setEnums(_so_sf_enum_num[0],                                   
                           _so_sf_enum_vals[0],                                  
                           _so_sf_enum_names[0]);                                
    }
  
  /////////////////////////////////////////////
   ///
   /// This adds the info for a field to the SoFieldData and sets the
   /// default value for it. The parameters are as follows:
   ///      fieldName:      the name of the field (as a member)
   ///      defValue:       the default value enclosed in parentheses
   ///
   /// For example,
   ///
   ///      SO_NODE_ADD_FIELD(ambientColor, (0.2, 0.2, 0.2));
   ///      SO_NODE_ADD_FIELD(shininess,    (0.0));
   ///
   /// adds info about fields named ambientColor and shininess with the
   /// given default values.
   ///
   
  public <T> void SO_NODE_ADD_FIELD(SoSField<T> field,String fieldName, T defValue) {
	  SO_NODE_ADD_SFIELD(field, fieldName, defValue);
  }
  public void SO_NODE_ADD_FIELD(SoSFEnum field,String fieldName, Enum defValue) {
	  SO_NODE_ADD_SFIELD(field, fieldName, defValue);
  }
   public <T> void SO_NODE_ADD_SFIELD(SoSField<T> field,String fieldName, T defValue) {
 	   SO__NODE_CHECK_CONSTRUCT();
        if (firstInstance())                    {                                
        fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName,                   
                            field);            
        }
    field.setValue(defValue);                                    
    field.setContainer(thisParent);                                   
   }
   
   public void SO_NODE_ADD_SFIELD(SoSFEnum field,String fieldName, Enum defValue) {
 	   SO__NODE_CHECK_CONSTRUCT();
        if (firstInstance())                    {                                
        fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName,                   
                            field);            
        }
    field.setValue(defValue);                                    
    field.setContainer(thisParent);                                   
   }
   
   public void SO_NODE_ADD_MFIELD( SoMField field,String fieldName, Object ... defValue) {
	   
	   SoNode that = thisParent;
	   
       SO__NODE_CHECK_CONSTRUCT(/*__FILE__*/);
       if (firstInstance()){
           fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName,
                               field);
       }
       if(field instanceof SoMFInt32) {
    	   ((SoMFInt32)field).setValue((int)defValue[0]);
       }
       else if(field instanceof SoMFUInt32) {
    	   ((SoMFUInt32)field).setValue((int)defValue[0]);
       }
       else if(field instanceof SoMFNode && defValue == null) {
    	   field.setValue(new SoNodePtr());    	   
       }
       else {
    	   field.setValue(defValue[0]);
       }
       field.setContainer(thisParent);
   }
 
// New for Coin-3
   public void SO_NODE_ADD_EMPTY_MFIELD( SoMField field,String fieldName) {
	   
	   SoNode that = thisParent;
	   
       SO__NODE_CHECK_CONSTRUCT(/*__FILE__*/);
       if (firstInstance()){
           fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName,
                               field);
       }
       field.setContainer(thisParent);
   }
 
   /////////////////////////////////////////////////
///
/// This is a boolean value that can be tested
/// in constructors.
///

public boolean SO_NODE_IS_FIRST_INSTANCE()                                           {
    return (firstInstance());
}

public void SO_NODE_INTERNAL_CONSTRUCTOR(Class<? extends SoNode> class1) {
	SO_NODE_INTERNAL_CONSTRUCTOR(); //TODO
}

public void SO_VRMLNODE_INTERNAL_CONSTRUCTOR(Class<? extends SoNode> _class_) {
  SO_NODE_INTERNAL_CONSTRUCTOR(_class_);
  thisParent.setNodeType(SoNode.NodeType.VRML2);
}

public void SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(SoMField _field_, String fieldName) {
  do { 
    _field_.setFieldType(SoField.FieldType.EXPOSED_FIELD.getValue());
    _field_.setContainer(thisParent);
    fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
  } while(false);
}

public void SO_VRMLNODE_ADD_EVENT_IN(SoMField _field_, String fieldName) {
  do { 
    _field_.setFieldType(SoField.FieldType.EVENTIN_FIELD.getValue());
    _field_.setContainer(thisParent);
    fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
  } while(false);
}

public void SO_VRMLNODE_ADD_EVENT_IN(SoField _field_, String fieldName) {
	  do { 
	    _field_.setFieldType(SoField.FieldType.EVENTIN_FIELD.getValue());
	    _field_.setContainer(thisParent);
	    fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
	  } while(false);
	}

public void SO_VRMLNODE_ADD_EVENT_OUT(SoMField _field_, String fieldName) {
do { 
  _field_.setFieldType(SoField.FieldType.EVENTOUT_FIELD.getValue()); 
  _field_.setContainer(thisParent); 
  fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
} while(false);
}

public void SO_VRMLNODE_ADD_EVENT_OUT(SoField _field_, String fieldName) {
do { 
  _field_.setFieldType(SoField.FieldType.EVENTOUT_FIELD.getValue()); 
  _field_.setContainer(thisParent); 
  fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
} while(false);
}

public <T> void SO_VRMLNODE_ADD_FIELD(SoSField<T> field,String fieldName, T defValue) {
	SO_NODE_ADD_FIELD(field,fieldName,defValue);
}

public <T> void SO_VRMLNODE_ADD_MFIELD(SoMField<T> field,String fieldName, T defValue) {
	SO_NODE_ADD_MFIELD(field,fieldName,defValue);
}

public void SO_VRMLNODE_ADD_FIELD(SoSFEnum field,String fieldName, Enum defValue) {
	SO_NODE_ADD_FIELD(field,fieldName,defValue);
}

public <T> void SO_VRMLNODE_ADD_EXPOSED_FIELD(SoSField<T> field,String fieldName, T defValue) {
	field.setFieldType(SoField.FieldType.EXPOSED_FIELD.getValue()); 
	SO_NODE_ADD_FIELD(field,fieldName,defValue);
}

public <T> void SO_VRMLNODE_ADD_EXPOSED_FIELD(SoMField<T> field,String fieldName, T defValue) {
	field.setFieldType(SoField.FieldType.EXPOSED_FIELD.getValue()); 
	SO_NODE_ADD_MFIELD(field,fieldName,defValue);
}

public <T> void SO_VRMLNODE_ADD_EMPTY_MFIELD(SoMField<T>_field_,String fieldName) {
do { 
  _field_.setContainer(thisParent); 
  fieldData.get(thisClass)[0].addField(thisClass,thisParent, fieldName, _field_);
} while(false);
}
}
