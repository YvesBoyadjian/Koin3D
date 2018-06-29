/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.fields.SoMFInt32;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterArray1i extends SoUniformShaderParameter {
	
	public final SoMFInt32 value = new SoMFInt32();

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shaderObject) {
		// TODO Auto-generated method stub

	}

}
