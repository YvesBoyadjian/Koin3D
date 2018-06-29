/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.database.inventor.fields.SoSFVec2f;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter2f extends SoUniformShaderParameter {
	
	public final SoSFVec2f value = new SoSFVec2f();

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter#updateParameter(jscenegraph.coin3d.shaders.SoGLShaderObject)
	 */
	@Override
	public void updateParameter(SoGLShaderObject shaderObject) {
		// TODO Auto-generated method stub

	}

}
