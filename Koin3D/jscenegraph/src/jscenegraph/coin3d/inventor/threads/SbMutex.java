/**
 * 
 */
package jscenegraph.coin3d.inventor.threads;

import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbMutex implements Destroyable {

	  public int lock() {
		    //cc_mutex_lock(this.mutex);
		    /* Useless return, here just for compatibility with TGS'
		       SbThreadMutex API: */
		    return 0;
		  }

		  public boolean tryLock() {
		    return true;//cc_mutex_try_lock(this.mutex) == CC_OK;
		  }

		  public int unlock() {
		    //cc_mutex_unlock(this.mutex);
		    /* Useless return, here just for compatibility with TGS'
		       SbThreadMutex API: */
		    return 0;
		  }

		@Override
		public void destructor() {
			// TODO Auto-generated method stub
			
		}

}
