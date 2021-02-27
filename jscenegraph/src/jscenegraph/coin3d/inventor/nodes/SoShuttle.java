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
  \class SoShuttle SoShuttle.h Inventor/nodes/SoShuttle.h
  \brief The SoShuttle class is used to oscillate between two translations.
  \ingroup nodes

  A smooth transition between translation0 and translation1 is created
  using a cosine function. In the beginning of the cycle, translation0
  is used. Halfway through the cycle, the resulting translation equals
  translation1, and at the end of the cycle, we're at translation0
  again.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    Shuttle {
        translation 0 0 0
        translation0 0 0 0
        translation1 0 0 0
        speed 1
        on TRUE
    }
  \endcode
*/

// *************************************************************************

/*!
  \var SoSFVec3f SoShuttle::translation0

  Translation at the start and end of the cycle. Default value is (0,
  0, 0).
*/
/*!
  \var SoSFVec3f SoShuttle::translation1

  Translation at the middle of the cycle. Default value is (0, 0, 0).
*/
/*!
  \var SoSFFloat SoShuttle::speed
  Number of cycles per second. Default value is 1.
*/
/*!
  \var SoSFBool SoShuttle::on
  Toggles animation on or off. Defauls to \c TRUE.
*/

// *************************************************************************

package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.engines.SoInterpolateVec3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoCalculator;
import jscenegraph.database.inventor.engines.SoCalculator.Expression;
import jscenegraph.database.inventor.engines.SoElapsedTime;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author BOYADJIAN
 *
 */
public class SoShuttle extends SoTranslation {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShuttle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShuttle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShuttle.class); }    	  	
	
	  public final SoSFVec3f translation0 = new SoSFVec3f();
	  public final SoSFVec3f translation1 = new SoSFVec3f();
	  public final SoSFFloat speed = new SoSFFloat();
	  public final SoSFBool on = new SoSFBool();
	  
	  private SoInterpolateVec3f interpolator; //ptr
	  private SoCalculator calculator; // ptr
	  private SoElapsedTime timer;

	  /*!
	  Constructor.
	*/
	public SoShuttle()
	{
	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShuttle.class);

	  nodeHeader.SO_NODE_ADD_FIELD(translation0,"translation0", (new SbVec3f(0.0f, 0.0f, 0.0f)));
	  nodeHeader.SO_NODE_ADD_FIELD(translation1,"translation1", (new SbVec3f(0.0f, 0.0f, 0.0f)));
	  nodeHeader.SO_NODE_ADD_FIELD(speed,"speed", (1.0f));
	  nodeHeader.SO_NODE_ADD_FIELD(on,"on", (true));

	  interpolator = new SoInterpolateVec3f();
	  interpolator.ref();
	  calculator = new SoCalculator();
	  calculator.ref();
	  timer = new SoElapsedTime();
	  timer.ref();

	  //calculator.expression.setValue("oa = (1.0 - cos(a*b*2*M_PI)) * 0.5");
	  calculator.expression.setValue(new Expression() {

		@Override
		public void run(float[] abcdefgh, SbVec3f[] ABCDEFGH, float[][] oaobocod, SbVec3f[] oAoBoCoD2) {
			float a = abcdefgh[0];
			float b = abcdefgh[1];
			float[] oa = oaobocod[0];
			
			oa[0] = (1.0f - (float)Math.cos(a*b*2*Math.PI)) * 0.5f;
		}
		  
	  });
	  calculator.a.connectFrom(timer.timeOut);
	  timer.on.connectFrom(on);
	  calculator.b.connectFrom(speed);
	  interpolator.input0.connectFrom(translation0);
	  interpolator.input1.connectFrom(translation1);
	  interpolator.alpha.connectFrom(calculator.oa);

	  translation.connectFrom(interpolator.output/*, true*/); // TODO COIN 3D

	}
	
	public void destructor() {
		
		  interpolator.unref();
		  calculator.unref();
		  timer.unref();
		
		super.destructor();
	}
	  
	// doc in parent
	  public static void
	  initClass()
	  {
		    SoSubNode.SO__NODE_INIT_CLASS(SoShuttle.class, "Shuttle", SoTranslation.class);
	    //SO_NODE_INTERNAL_INIT_CLASS(SoShuttle, SO_FROM_INVENTOR_1);
	  }

	  
}
