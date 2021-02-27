/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 * 
 * All mutable objects must implement this interface
 *
 */
public interface Mutable {

	// copy operator (java port)
	public void copyFrom(Object other);

	static public int sizeof() { // For memcpy
		return 1;
	}
}
