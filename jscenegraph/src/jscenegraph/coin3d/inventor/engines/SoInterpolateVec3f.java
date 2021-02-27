/**
 * 
 */
package jscenegraph.coin3d.inventor.engines;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.engines.SoGate;
import jscenegraph.database.inventor.engines.SoSubEngine;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec3f;

/**
 * @author BOYADJIAN
 *
 */
public class SoInterpolateVec3f extends SoInterpolate {

	  //SO_INTERPOLATE_HEADER(SoInterpolateVec3f);
	
    //SO_ENGINE_HEADER(SoInterpolateVec3f);
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

	 // SO_ENGINE_HEADER
	

	  public final SoMFVec3f input0 = new SoMFVec3f();
	  public final SoMFVec3f input1 = new SoMFVec3f();
	  
	  
	  public SoInterpolateVec3f() {
			super();
			engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoInterpolateVec3f.class,this);
			
		  engineHeader.SO_ENGINE_CONSTRUCTOR(SoInterpolateVec3f.class,inputData, outputData, parentInputData[0], parentOutputData[0]); 
		  //engineHeader.SO_ENGINE_ADD_INPUT(alpha,"alpha", (0.0f)); done in base class
		  engineHeader.SO_ENGINE_ADD_MINPUT(input0,"input0", new SbVec3f(0,0,0)); 
		  engineHeader.SO_ENGINE_ADD_MINPUT(input1,"input1", new SbVec3f(0,0,0)); 
		  //engineHeader.SO_ENGINE_ADD_MOUTPUT(output,"output", SoMFVec3f.class); done in base class
		  
		  this.isBuiltIn = false;
	  }
	  
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.engines.SoEngine#evaluate()
	 */
	@Override
	protected void evaluate() {
		  int n0 = input0.getNum(); 
		  int n1 = input1.getNum(); 
		  float a = alpha.getValue(); 
		  for (int i = Math.max(n0, n1) - 1; i >= 0; i--) { 
		    SbVec3f v0 = input0.operator_square_bracket(Math.min(i, n0-1)); 
		    SbVec3f v1 = input1.operator_square_bracket(Math.min(i, n1-1)); 
		    final int ii = i;
		    SoSubEngine.SO_ENGINE_OUTPUT(output, SoMFVec3f.class, (o)->( (SoMFVec3f)o).set1Value(ii, (v1.operator_minus(v0)).operator_mul(a).operator_add(v0))); 
		  }
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
classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoInterpolateVec3f.class, "InterpolateVec3f", SoInterpolate.class, parentInputData, parentOutputData);
}

	
}
