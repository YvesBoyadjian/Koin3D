/**
 * 
 */
package jscenegraph.port;

import jscenegraph.opengl.GL2;

/**
 * @author Yves Boyadjian
 *
 */
public interface IGLCallback {

	void run(GL2 gl, Object data);
}
