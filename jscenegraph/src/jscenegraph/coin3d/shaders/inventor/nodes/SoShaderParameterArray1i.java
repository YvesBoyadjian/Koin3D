/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterArray1i extends SoUniformShaderParameter {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameterArray1i.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameterArray1i.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameterArray1i.class); }    	  	
	
	public final SoMFInt32 value = new SoMFInt32();


	public SoShaderParameterArray1i()
	{
		nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShaderParameterArray1i.class);
		nodeHeader.SO_NODE_ADD_MFIELD(value,"value", (0));
	}

/*!
  \copybrief SoNode::initClass(void)
*/
	public static void initClass()
	{
		SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameterArray1i.class,
				"ShaderParameterArray1i",
				/*SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0*/SoUniformShaderParameter.class);
	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		  this.ensureParameter(shader);
		  this.getGLShaderParameter(shader.getCacheContext()).set1iv(shader,
		                                                                this.value.getNum(),
		                                                                ( int[]) this.value.getValuesI(0), //java port
		                                                                this.name.getValue()/*.getString()*/,
		                                                                this.identifier.getValue());
	}

}
