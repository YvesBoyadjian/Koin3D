/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoSFMatrix;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterMatrix extends SoUniformShaderParameter {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameterMatrix.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameterMatrix.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameterMatrix.class); }    	  	
	
	
	public final SoSFMatrix value = new SoSFMatrix();

	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoShaderParameterMatrix,
	    //                          SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
		SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameterMatrix.class, "ShaderParameterMatrix", SoUniformShaderParameter.class);
	}

	public SoShaderParameterMatrix()
	{
	  nodeHeader.SO_NODE_CONSTRUCTOR(SoShaderParameterMatrix.class);
	  nodeHeader.SO_NODE_ADD_FIELD(value,"value", (new SbMatrix(1,0,0,0,
	                                     0,1,0,0,
	                                     0,0,1,0,
	                                     0,0,0,1)));
	}

	
	
	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		  this.ensureParameter(shader);

		  this.getGLShaderParameter(shader.getCacheContext()).setMatrix(shader,  this.value.getValue().getValueLinear()/*[0]*/,
		                                                                   this.name.getValue()/*.getString()*/,
		                                                                   this.identifier.getValue());
	}

}
