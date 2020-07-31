/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.threads.SbStorage;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.caches.SoGLCacheList;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLGroupP {
	public
		  enum HasSound { YES, NO, MAYBE } ;

  SoBoundingBoxCache bboxcache; //ptr
  int bboxcache_usecount;
  int bboxcache_destroycount;

  SbStorage glcachestorage; //ptr
  
  HasSound hassoundchild;
  
  // when doing threadsafe rendering, each thread needs its own
// glcachelist
static class sovrmlgroup_storage {
  public SoGLCacheList glcachelist; // ptr
};


public static void
sovrmlgroup_storage_construct(Object data)
{
  sovrmlgroup_storage ptr = (sovrmlgroup_storage) data;
  ptr.glcachelist = null;
}

public static void 
sovrmlgroup_storage_destruct(Object data)
{
  sovrmlgroup_storage ptr = (sovrmlgroup_storage) data;
  Destroyable.delete( ptr.glcachelist);
}



  // lots of ifdefs here but it can't be helped...
  public SoVRMLGroupP() {
    this.glcachestorage = 
      new SbStorage(sovrmlgroup_storage.class,
                    SoVRMLGroupP::sovrmlgroup_storage_construct,
                    SoVRMLGroupP::sovrmlgroup_storage_destruct);
  }

  

  public static void invalidate_gl_cache(Object tls, Object obj) {
    sovrmlgroup_storage ptr = (sovrmlgroup_storage) tls;
    if (ptr.glcachelist != null) {
      ptr.glcachelist.invalidateAll();
    }
  }  
  
  public void invalidateGLCaches() {
    glcachestorage.applyToAll(SoVRMLGroupP::invalidate_gl_cache, null);
  }

  
public void lock() {
	// TODO Auto-generated method stub
	
}

public void unlock() {
	// TODO Auto-generated method stub
	
}
}
