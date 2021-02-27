/**
 * 
 */
package application.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

/**
 * @author Yves Boyadjian
 *
 */
public class OpenGLMotionState extends btDefaultMotionState {

	public OpenGLMotionState(btTransform transform) {
		super();
		setStartWorldTrans(transform);
		setGraphicsWorldTrans(transform);
	}

	void GetWorldTransform(/*btScalar*/float[] transform) {
		btTransform trans = getGraphicsWorldTrans();
		trans.getOpenGLMatrix(transform);
	}
}
