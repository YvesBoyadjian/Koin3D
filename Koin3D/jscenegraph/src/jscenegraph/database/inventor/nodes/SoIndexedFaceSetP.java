/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.threads.SbRWMutex;
import jscenegraph.database.inventor.caches.SoConvexDataCache;
import jscenegraph.mevis.inventor.misc.SoVertexArrayIndexer;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoIndexedFaceSetP implements Destroyable {

	  public jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer vaindexer; //ptr
	  public SoConvexDataCache convexCache; //ptr
	  public int concavestatus;

	  //#ifdef COIN_THREADSAFE
	  // FIXME: a mutex for every instance seems a bit excessive,
	  // especially since MSWindows might have rather strict limits on the
	  // total amount of mutex resources a process (or even a user) can
	  // allocate. so consider making this a class-wide instance instead.
	  // -mortene.
	  public final SbRWMutex convexmutex = new SbRWMutex();
	//#endif // COIN_THREADSAFE

	  void readLockConvexCache() {
	//#ifdef COIN_THREADSAFE
	    this.convexmutex.readLock();
	//#endif // COIN_THREADSAFE
	  }
	  void readUnlockConvexCache() {
	//#ifdef COIN_THREADSAFE
	    this.convexmutex.readUnlock();
	//#endif // COIN_THREADSAFE
	  }
	  void writeLockConvexCache() {
	//#ifdef COIN_THREADSAFE
	    this.convexmutex.writeLock();
	//#endif // COIN_THREADSAFE
	  }
	  void writeUnlockConvexCache() {
	//#ifdef COIN_THREADSAFE
	    this.convexmutex.writeUnlock();
	//#endif // COIN_THREADSAFE
	  }
	@Override
	public void destructor() {
		// TODO Auto-generated method stub
		
	}
}
