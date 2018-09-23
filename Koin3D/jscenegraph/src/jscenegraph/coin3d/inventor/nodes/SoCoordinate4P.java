/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoCoordinate4P implements Destroyable {

	public SoVBO vbo;
	
	@Override
	public void destructor() {
		Destroyable.delete(vbo);
	}

}
