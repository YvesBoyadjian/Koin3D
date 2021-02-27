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
 |      This file defines the abstract SoFieldConverter base class.
 |
 |   Author(s)          : Paul S. Strauss, Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SoEngineOutputList;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoFieldConverter
///
///  Abstract base class for all field converter engines. A field
///  converter engine is used to convert the value of a field of one
///  type to another. These engines are registered with the global
///  database so connections between fields of different types can be
///  made by automatically inserting a converter instance.
///  A single class of converter engine may be registered for several
///  different conversions.
///  Field converters are never written to file
///  (SoField::writeConnection takes care of that).
///
//////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoFieldConverter extends SoEngine {
	
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
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoFieldConverter()
//
////////////////////////////////////////////////////////////////////////
{
	engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoElapsedTime.class,this);
	   
    engineHeader.SO_ENGINE_CONSTRUCTOR(SoFieldConverter.class,inputData,outputData,parentInputData[0],parentOutputData[0]);
}

	  
	  
	/**
	 * These must be defined in each subclass. 
	 * They return the input and output connections of the given types.
	 * 
	 * @param type
	 * @return
	 */
	public abstract SoField getInput(SoType type);
	
	public abstract SoEngineOutput getOutput(SoType type);
	
	/**
	 * Returns the input that is connected. 
	 * By default, this searches through the field data for the field that is 
	 * connected; 
	 * you can redefine to make more efficient. 
	 * 
	 * @return
	 */
	public SoField getConnectedInput() {
		
	     final SoFieldData fd = getFieldData();
	          for (int i = 0; i < fd.getNumFields(); i++) {
	              SoField f = fd.getField(this, i);
	              if (f.isConnected())
	                  return f;
	          }
	          return null;
	     	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds all fields connected from the output of the converter to
//    the given list.
//
// Use: public

public int
getForwardConnections(final SoFieldList list) 
//
////////////////////////////////////////////////////////////////////////
{
    final SoEngineOutputList  outputs = new SoEngineOutputList();
    int                 numConnections = 0;

    // Get all of the outputs into a list
    getOutputs(outputs);

    // For each output, add all connections to the given list
    for (int i = 0; i < outputs.getLength(); i++)
        numConnections += ((SoEngineOutput)outputs.operator_square_bracket(i)).getForwardConnections(list);

    return numConnections;
}
	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoFieldConverter class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       classTypeId = SoSubEngine.SO__ENGINE_INIT_ABSTRACT_CLASS(SoFieldConverter.class, "FieldConverter",
	                                          SoEngine.class, parentInputData, parentOutputData);
	   }
	   	
}
