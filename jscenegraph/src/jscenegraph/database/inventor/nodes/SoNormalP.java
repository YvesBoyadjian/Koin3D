/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNormalP implements Destroyable {

	  SoVBO vbo; //ptr

	@Override
	public void destructor() {
		Destroyable.delete(vbo);
	}
}
