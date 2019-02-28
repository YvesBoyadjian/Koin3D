/**
 * 
 */
package jscenegraph.coin3d.inventor;

import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class SbTesselator {

	public interface SbTesselatorCB {
		void invoke(Object v0, Object v1, Object v2, Object data);
	}

	public SbTesselator(SbTesselatorCB func, Object data) {
		//TODO
	}

	public void beginPolygon() {
		// TODO Auto-generated method stub
		
	}

	public void endPolygon() {
		// TODO Auto-generated method stub
		
	}

	public void addVertex(SbVec3f v, Object object) {
		// TODO Auto-generated method stub
		
	}

	public void destructor() {
		// TODO Auto-generated method stub
		
	}
}
