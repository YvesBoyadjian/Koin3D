/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.coin3d.shaders.SoGLShaderParameter;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoUniformShaderParameter extends SoShaderParameter {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoUniformShaderParameter.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoUniformShaderParameter.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoUniformShaderParameter.class); }              
	
  // FIXME: add a cache context destruction callback, pederb 2005-11-30
  private final Map <Integer,SoGLShaderParameter> glparams = new HashMap<>();

public SoUniformShaderParameter()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoUniformShaderParameter.class*/);

  //PRIVATE(this) = new SoUniformShaderParameterP;
}

public void destructor()
{
    //SbList <uint32_t> keylist;
    //this->glparams.makeKeyList(keylist);
    for (/*int i = 0; i < keylist.getLength(); i++*/Map.Entry<Integer,SoGLShaderParameter> entry : glparams.entrySet()) {
      SoGLShaderParameter param;
      param = entry.getValue();//(void) this->glparams.get(keylist[i], param);
      deleteGLParameter(param);
    }
  //delete PRIVATE(this);
	super.destructor();
}

	private static void deleteGLParameter(SoGLShaderParameter param) {
    // FIXME: schedule for delete, pederb 2005-11-30
  }

public void
ensureParameter(SoGLShaderObject shader)
{
  assert(shader != null);
  int context = shader.getCacheContext();
  SoGLShaderParameter  param;
  if ((param = this.glparams.get(context))==null) {
    param = shader.getNewParameter();
    this.glparams.put(context, param);
  }
  if (param.shaderType() != shader.shaderType()) {
    deleteGLParameter(param);
    param = shader.getNewParameter();
    this.glparams.put(context, param);
  }
}

public SoGLShaderParameter 
getGLShaderParameter(int cachecontext)
{
  SoGLShaderParameter glparam;
  if ((glparam = this.glparams.get(cachecontext))!= null) return glparam;
  return null;
}

	public abstract void updateParameter(SoGLShaderObject shaderObject);
  
  
  public static void
  initClass()
  {
//    SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoUniformShaderParameter.class,
//                                         SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
      SoSubNode.SO__NODE_INIT_CLASS(SoUniformShaderParameter.class, "UniformShaderParameter", SoShaderParameter.class);
  }

}
