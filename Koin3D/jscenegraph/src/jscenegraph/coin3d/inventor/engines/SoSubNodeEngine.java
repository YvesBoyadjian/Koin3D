/**
 * 
 */
package jscenegraph.coin3d.inventor.engines;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jscenegraph.coin3d.inventor.VRMLnodes.SoVRMLTimeSensor;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSField;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSubNodeEngine extends SoSubNode {

	  private  static final Map<Class,SoEngineOutputData[]> outputData = new HashMap<Class,SoEngineOutputData[]>();
	  private  static final Map<Class, SoEngineOutputData[][]>    parentOutputData = new HashMap<Class,SoEngineOutputData[][]>();
	  
	protected SoSubNodeEngine(Class<? extends SoNode> class1, SoNode parent) {
		super(class1, parent);
		// TODO Auto-generated constructor stub
	}

	  public static SoSubNodeEngine SO_NODEENGINE_HEADER(Class<? extends SoNode> class1, SoNode parent) {
			return new SoSubNodeEngine(class1,parent);
		  }	

	public static void SO_NODEENGINE_INTERNAL_INIT_CLASS(Class<? extends SoNodeEngine> className, String classPrintName) {
		SoSubNode.SO__NODE_INIT_CLASS(className, classPrintName, SoNodeEngine.class);
		
		  if( !outputData.containsKey(className)) {
			  final SoEngineOutputData[] fieldData1 = new SoEngineOutputData[1];                                   
			  final SoEngineOutputData[][]    parentFieldData1 = new SoEngineOutputData[1][];
			  outputData.put(className, fieldData1);
			  parentOutputData.put(className, parentFieldData1);

			  parentFieldData1[0] = SoNodeEngine.getOutputDataPtr();//(SoFieldData[])parentClass.getMethod("getFieldDataPtr").invoke(null);
		  }
		  else {
			  throw new IllegalStateException("Class "+ className + " already initialized");
		  }
	}

	  public                                                                  
	  SoEngineOutputData   getOutputData() {
		  return outputData.get(thisClass)[0]; 
	  }
	  public  static SoEngineOutputData[] getOutputDataPtr(Class klass)                              
	        { return outputData.get(klass); }

	public void SO_NODEENGINE_INTERNAL_CONSTRUCTOR(Class<? extends SoNodeEngine> class1) {
	    SO_NODEENGINE_CONSTRUCTOR(class1); 
	    /* Restore value of isBuiltIn flag (which is set to FALSE */ 
	    /* in the SO_ENGINE_CONSTRUCTOR() macro). */ 
		thisParent.isBuiltIn = true;		
	}

	private void SO_NODEENGINE_CONSTRUCTOR(Class<? extends SoNodeEngine> class1) {
		SO_NODE_CONSTRUCTOR(class1);
		
	    if (outputData.get(thisClass)[0] == null)                                                    
	    	outputData.get(thisClass)[0] = new SoEngineOutputData(                                          
            parentOutputData.get(thisClass)[0] != null ? parentOutputData.get(thisClass)[0][0] : null);                       
	}
	  
	public void SO_NODEENGINE_ADD_OUTPUT(SoEngineOutput _output_, String outputName, Class<? extends SoSField> _type_)  {
	  do { 
		  outputData.get(thisClass)[0].addOutput((SoNodeEngine)thisParent, outputName, 
	                          _output_, 
	                          SoField.getClassTypeId(_type_)); 
	    _output_.setNodeContainer((SoNodeEngine)thisParent); 
	  } while(false);
}
	
}
