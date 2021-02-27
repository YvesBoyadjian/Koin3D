/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLNormalP implements Destroyable {
	SoVBO vbo;

	@Override
	public void destructor() {
		Destroyable.delete(vbo);
	}
}
