/**
 * 
 */
package jscenegraph.coin3d.glue;

/**
 * @author Yves Boyadjian
 *
 */
public class GLUWrapper {

	private static GLUWrapper_t GLU_instance = null;
	
	/* Implemented by using the singleton pattern. */
	public static GLUWrapper_t GLUWrapper()
	{
		if(GLU_instance == null) {
			GLU_instance = new GLUWrapper_t();
		}
		return GLU_instance;
	}
}
