/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 * Implements the equivalent of C++ Destructor
 */
public interface Destroyable {

	/**
	 * Destructor
	 */
	void destructor();
	
	/**
	 * Helper method
	 * @param destroyable
	 */
	static void delete(Destroyable destroyable) {
		if(destroyable != null) {
			destroyable.destructor();
		}
	}
}
