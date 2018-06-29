/**
 * 
 */
package jscenegraph.port;

import java.util.ArrayList;
import java.util.List;

import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class VectorOfSbVec3f {
	
	private final List<SbVec3f> list = new ArrayList<SbVec3f>(); 

	public void add(SbVec3f vec) {
		list.add(new SbVec3f(vec));
	}

	public SbVec3f get(int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

}
