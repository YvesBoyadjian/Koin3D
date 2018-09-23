/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec4f;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter4f extends SoUniformShaderParameter {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameter4f.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameter4f.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameter4f.class); }
	  
	  public final SoSFVec4f value = new SoSFVec4f();
	
	  public SoShaderParameter4f()
	  {
		  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShaderParameter4f.class);
		  nodeHeader.SO_NODE_ADD_FIELD(value,"value", new SbVec4f(0,0,0,0));
	  }

	  
	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		  this.ensureParameter(shader);
		  this.getGLShaderParameter(shader.getCacheContext()).set4f(shader,
		                         this.value.getValue().getValueRead(),
		                         this.name.getValue()/*.getString()*/,
		                         this.identifier.getValue());
	}

	public static void initClass()
	{
      SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameter4f.class, "ShaderParameter4f", SoUniformShaderParameter.class);
	}

}
