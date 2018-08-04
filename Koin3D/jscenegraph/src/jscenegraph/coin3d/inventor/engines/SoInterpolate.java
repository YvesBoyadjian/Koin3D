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
  \class SoInterpolate SoInterpolate.h Inventor/engines/SoInterpolate.h
  \brief The SoInterpolate class is the base class for all interpolator engines.
  \ingroup engines

  Interpolators are used to linearly interpolate between two values.

  In Coin, we've chosen to implement all interpolators in separate
  files. If you want to be OIV compatible when programming, you should
  include the SoInterpolate.h, and not the interpolator file(s) you
  need.
*/

/*!
  \var SoSFFloat SoInterpolate::alpha

  The value which says how much we've should interpolate from first
  value to second value. A value equal to 0 will give an output equal
  to the first value, alpha equal to 1 gives the second value, any
  value in between gives a "weighted" interpolation between the two
  values.
*/
/*!
  \var SoEngineOutput SoInterpolate::output

  Interpolated values from the input fields. The type of the output
  will of course be the same as the type of the input fields of each
  non-abstract subclass inheriting SoInterpolate.
*/


package jscenegraph.coin3d.inventor.engines;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoBoolOperation;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.engines.SoSubEngine;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoInterpolate extends SoEngine {
	
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
	
	
	public final SoSFFloat alpha = new SoSFFloat();
	public final SoEngineOutput output = new SoEngineOutput();

	/*!
	  Default constructor.
	*/
	public SoInterpolate()
	{
	  // Don't use standard SO_ENGINE_CONSTRUCTOR.
		super();
		engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoInterpolate.class,this);
		engineHeader.alternate_init(inputData, outputData);

	  // Catch attempts to use an engine class which has not been
	  // initialized.
	  assert(SoInterpolate.classTypeId.operator_not_equal(SoType.badType()));
	  // Initialize a inputdata container for the class only once.
	  if (SoInterpolate.inputData[0] == null) {
	    // FIXME: is this really necessary for SoInterpolate? 20000505 mortene.
	    SoInterpolate.inputData[0] =
	      new SoFieldData(SoInterpolate.parentInputData[0]!=null ?
	                      SoInterpolate.parentInputData[0][0] : null);
	    SoInterpolate.outputData[0] =
	      new SoEngineOutputData(SoInterpolate.parentOutputData[0]!=null ?
	                             SoInterpolate.parentOutputData[0][0] : null);
	  }

	  engineHeader.SO_ENGINE_ADD_INPUT(alpha,"alpha", (0.0f));
	}
		
	/*!
	  Destructor.
	*/
	public void destructor()
	{
	//#if COIN_DEBUG && 0 // debug
	  SoDebugError.postInfo("SoInterpolate::~SoInterpolate", this.toString());
	//#endif // debug
	  Destroyable.delete(inputData[0]); inputData[0] = null;
	  //Destroyable.delete(outputData[0]); outputData[0] = null; //TODO
	}
	
////////////////////////////////////////////////////////////////////////
//
//Description:
//This initializes the SoInterpolate class.
//
//Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
	  //SO_ENGINE_INTERNAL_INIT_ABSTRACT_CLASS(SoInterpolate);
		classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoInterpolate.class, "Interpolate", SoEngine.class, parentInputData, parentOutputData);
}

}
