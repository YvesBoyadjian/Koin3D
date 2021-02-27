/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.threads.SbRWMutex;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLVertexShapeP implements Destroyable {

	  SoNormalCache normalcache; //ptr

	  final  SbRWMutex normalcachemutex = new SbRWMutex();
	  
	@Override
	public void destructor() {
		// TODO Auto-generated method stub
		
	}

}
