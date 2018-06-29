/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.fields.SoSFMatrix;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameterMatrix extends SoUniformShaderParameter {
	
	public final SoSFMatrix value = new SoSFMatrix();

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shaderObject) {
		// TODO Auto-generated method stub

	}

}
