/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer;
import jscenegraph.coin3d.inventor.threads.SbRWMutex;
import jscenegraph.database.inventor.caches.SoConvexDataCache;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedFaceSetP implements Destroyable {

	  SoVertexArrayIndexer vaindexer; //ptr
	  SoConvexDataCache convexCache; //ptr
	  int concavestatus;
	@Override
	public void destructor() {
		vaindexer = null;
		convexCache = null;
		concavestatus = 0;
	}

  final SbRWMutex convexmutex = new SbRWMutex();
  void readLockConvexCache() { this.convexmutex.readLock(); }
  void readUnlockConvexCache() { this.convexmutex.readUnlock(); }
  void writeLockConvexCache() { this.convexmutex.writeLock(); }
  void writeUnlockConvexCache() { this.convexmutex.writeUnlock(); }
}
