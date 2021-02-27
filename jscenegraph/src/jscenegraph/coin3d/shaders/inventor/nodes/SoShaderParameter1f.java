/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter1f extends SoUniformShaderParameter {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameter1f.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameter1f.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameter1f.class); }    	  	
	
public final SoSFFloat value = new SoSFFloat();
	
public SoShaderParameter1f()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(/*SoShaderParameter1f.class*/);
  nodeHeader.SO_NODE_ADD_FIELD(value,"value", (0f));
}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		ensureParameter(shader);
		getGLShaderParameter(shader.getCacheContext()).set1f(shader,
                                                               value.getValue(),
                                                               name.getValue(),
                                                               identifier.getValue());
	}

	public static void initClass()
	{
      SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameter1f.class, "ShaderParameter1f", SoUniformShaderParameter.class);
	}

}
