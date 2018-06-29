/**
 * 
 */
package jscenegraph.port;

import com.jogamp.opengl.GL2;

/**
 * @author Yves Boyadjian
 *
 */
public interface IGLCallback {

	void run(GL2 gl, Object data);
}
