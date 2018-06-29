/**
 * 
 */
package jscenegraph.coin3d.shaders;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.shaders.SoShader.Type;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLCgShaderObject extends SoGLShaderObject {

	public SoGLCgShaderObject(GL2 cachecontext) {
		super(cachecontext);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.SoGLShaderObject#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.SoGLShaderObject#load(java.lang.String)
	 */
	@Override
	public void load(String sourceString) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.SoGLShaderObject#unload()
	 */
	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.SoGLShaderObject#shaderType()
	 */
	@Override
	public Type shaderType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.coin3d.shaders.SoGLShaderObject#getNewParameter()
	 */
	@Override
	public SoGLShaderParameter getNewParameter() {
		// TODO Auto-generated method stub
		return null;
	}

}
