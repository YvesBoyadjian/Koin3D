/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter1i extends SoUniformShaderParameter {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderParameter1i.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderParameter1i.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderParameter1i.class); }    	  	
	
	public
		  final SoSFInt32 value = new SoSFInt32();
	
public SoShaderParameter1i()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(/*SoShaderParameter1i.class*/);
  nodeHeader.SO_NODE_ADD_FIELD(value,"value", (0));
}

public void destructor()
{
	super.destructor();
}

public void
updateParameter(SoGLShaderObject shader)
{
  this.ensureParameter(shader);
  this.getGLShaderParameter(shader.getCacheContext()).set1i(shader,
                                                               this.value.getValue(),
                                                               this.name.getValue(),
                                                               this.identifier.getValue());
}

	

	public static void initClass()
	{
//	  SO_NODE_INTERNAL_INIT_CLASS(SoShaderParameter1i.class,
//	                              SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
      SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameter1i.class, "ShaderParameter1i", SoUniformShaderParameter.class);
	}


}
