/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.fields.SoMFFloat;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterArray1f extends SoUniformShaderParameter {
	
	public final SoMFFloat value = new SoMFFloat();

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shaderObject) {
		// TODO Auto-generated method stub

	}

}
