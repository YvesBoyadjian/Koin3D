/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.caches.SoConvexDataCache;

/**
 * @author Yves Boyadjian
 *
 */
public class SoFaceSetP {

	  SoConvexDataCache convexCache; // ptr
	  int concavestatus;
	  int primitivetype;

	/**
	 * 
	 */
	public SoFaceSetP() {
		// TODO Auto-generated constructor stub
	}

	  void readLockConvexCache() {
//		  #ifdef COIN_THREADSAFE
//		      this->convexmutex.readLock();
//		  #endif // COIN_THREADSAFE
		    }
		    void readUnlockConvexCache() {
//		  #ifdef COIN_THREADSAFE
//		      this->convexmutex.readUnlock();
//		  #endif // COIN_THREADSAFE
		    }
		    void writeLockConvexCache() {
//		  #ifdef COIN_THREADSAFE
//		      this->convexmutex.writeLock();
//		  #endif // COIN_THREADSAFE
		    }
		    void writeUnlockConvexCache() {
//		  #ifdef COIN_THREADSAFE
//		      this->convexmutex.writeUnlock();
//		  #endif // COIN_THREADSAFE
		    }
}
