/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter2f extends SoUniformShaderParameter {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameter2f.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameter2f.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameter2f.class); }    	  	
	
	public final SoSFVec2f value = new SoSFVec2f();

	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoShaderParameter2f,
	    //                          SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
      SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameter2f.class, "ShaderParameter2f", SoUniformShaderParameter.class);
	}

	public SoShaderParameter2f()
	{
	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShaderParameter2f.class);
	  nodeHeader.SO_NODE_ADD_FIELD(value,"value", new SbVec2f(0,0));
	}

	
	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shader) {
		  this.ensureParameter(shader);
		  this.getGLShaderParameter(shader.getCacheContext()).set2f(shader,
		                         this.value.getValue().getValueRead(),
		                         this.name.getValue()/*.getString()*/,
		                         this.identifier.getValue());
	}

}
