/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterArray2f extends SoUniformShaderParameter {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameterArray2f.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameterArray2f.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameterArray2f.class); }    	  	
	
	
	
	public final SoMFVec2f value = new SoMFVec2f();


public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoShaderParameterArray2f,
    //                          SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
	SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameterArray2f.class, "ShaderParameterArray2f", SoUniformShaderParameter.class);
}

public SoShaderParameterArray2f()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(SoShaderParameterArray2f.class);
  nodeHeader.SO_NODE_ADD_MFIELD(value,"value", new SbVec2f(0,0));
}

	
	
	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		  this.ensureParameter(shader);

		  int     num    = this.value.getNum();
		  float[] buffer = null;

		  if (num > 0) {
		    buffer = new float[2*num];
		    for (int i=0; i<num; i++) {
		      buffer[2*i+0] = this.value.operator_square_bracket(i).getValueRead()[0];
		      buffer[2*i+1] = this.value.operator_square_bracket(i).getValueRead()[1];
		    }
		  }

		  this.getGLShaderParameter(shader.getCacheContext())
		        .set2fv(shader, num, buffer,
		                 this.name.getValue()/*.getString()*/,
		                 this.identifier.getValue());
		  //if (buffer) delete[] buffer; java port
	}

}
