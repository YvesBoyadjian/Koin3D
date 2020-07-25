/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.threads.SbStorage;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;

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
}
