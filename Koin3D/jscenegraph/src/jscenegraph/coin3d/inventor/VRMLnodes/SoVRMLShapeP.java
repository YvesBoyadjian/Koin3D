/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.caches.SoGLCacheList;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLShapeP implements Destroyable {

	public
		  SoGLCacheList cachelist; //ptr
		  public SoChildList childlist; //ptr
		  public boolean childlistvalid;
		public void lockChildList() {
			// TODO Auto-generated method stub
			
		}
		public void unlockChildList() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void destructor() {
			// TODO Auto-generated method stub
			
		}
}
