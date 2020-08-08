/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodePtr implements Mutable {
	
	SoNode ptr;

	@Override
	public void copyFrom(Object other) {
		SoNodePtr otherPtr = (SoNodePtr) other;
		ptr = otherPtr.ptr;
	}

	public SoNode get() {
		return ptr;
	}

	public SoNode set(SoNode soNode) {
		ptr = soNode;
		return ptr;
	}

}
