/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterArray1f extends SoUniformShaderParameter {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameterArray1f.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameterArray1f.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameterArray1f.class); }    	  	
	
	
	public final SoMFFloat value = new SoMFFloat();
	

public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoShaderParameterArray1f,
    //                          SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
    SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameterArray1f.class, "ShaderParameterArray1f", SoUniformShaderParameter.class);
}

public SoShaderParameterArray1f()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(SoShaderParameterArray1f.class);
  nodeHeader.SO_NODE_ADD_MFIELD(value,"value", (0));
}

	

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
//		  this.ensureParameter(shader);
//		  this.getGLShaderParameter(shader.getCacheContext()).set1f(shader,
//		                                                               this.value.getValue(),
//		                                                               this.name.getValue()/*.getString()*/,
//		                                                               this.identifier.getValue());
		  this.ensureParameter(shader);
		  this.getGLShaderParameter(shader.getCacheContext()).set1fv(shader,
		                                                                this.value.getNum(),
		                                                                this.value.getValuesFloat(0), //java port
		                                                                this.name.getValue()/*.getString()*/,
		                                                                this.identifier.getValue());
	}

}
