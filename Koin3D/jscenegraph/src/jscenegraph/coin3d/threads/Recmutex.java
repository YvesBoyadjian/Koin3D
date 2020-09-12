/**
 * 
 */
package jscenegraph.coin3d.threads;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Yves Boyadjian
 *
 */
public class Recmutex {

	static Cc_recmutex recmutex_field_lock; // ptr
	static Cc_recmutex recmutex_notify_lock; // ptr


/*!
  \internal
*/
static void
cc_recmutex_struct_init(Cc_recmutex recmutex)
{
	recmutex.mutex = new  ReentrantLock(true); //cc_mutex_struct_init(&recmutex->mutex);
  recmutex.condvar = recmutex.mutex.newCondition();//new Object();//cc_condvar_struct_init(&recmutex->condvar);
  
  recmutex.threadid = null;
  recmutex.level = 0;
  recmutex.waiters = 0;
}

	/*! Constructs a recursive mutex. */

	public static Cc_recmutex 
	cc_recmutex_construct()
	{
	  Cc_recmutex recmutex;
	  recmutex = new Cc_recmutex();//(cc_recmutex *) malloc(sizeof(cc_recmutex));
	  assert(recmutex != null);
	  cc_recmutex_struct_init(recmutex);

//	  { /* debugging */
//	    String = coin_getenv(COIN_DEBUG_MUTEX_COUNT);
//	    if (env && (atoi(env) > 0)) {
//	      cc_debug_mtxcount += 1;
//	      (void)fprintf(stderr, "DEBUG: live mutexes +1 => %u (recmutex++)\n",
//	                    cc_debug_mtxcount);
//	    }
//	  }

	  return recmutex;
	}
	
public static int 
cc_recmutex_internal_notify_lock()
{
  return cc_recmutex_lock(recmutex_notify_lock);
}

public static int 
cc_recmutex_internal_notify_unlock()
{
  return cc_recmutex_unlock(recmutex_notify_lock);
}


/*! Locks the recursive mutex \a recmutex. Returns the nesting level. */

public static int
cc_recmutex_lock(Cc_recmutex recmutex)
{
  return recmutex_lock_internal(recmutex, true ? 1 : 0);
}

/*! Unlocks the recursive mutex \a recmutex. Returns the nesting level
    after unlocking. */

public static int
cc_recmutex_unlock(Cc_recmutex recmutex)
{
  int level;
  assert(recmutex != null);
  assert(recmutex.threadid == /*cc_thread_id()*/java.lang.Thread.currentThread());
  assert(recmutex.level > 0);
  Mutex.cc_mutex_lock(recmutex.mutex);
  recmutex.level--;
  if (recmutex.level == 0 && recmutex.waiters != 0) {
    Condvar.cc_condvar_wake_one(recmutex.condvar);
  }
  level = recmutex.level;
  Mutex.cc_mutex_unlock(recmutex.mutex);
  return level;
}


/*
  Internal function used by cc_recmutex_lock() and cc_recmutex_try_lock().
*/
private static int recmutex_lock_internal(Cc_recmutex recmutex, int wait)
{
  int level = -1; /* return -1 for recmutex_try_lock() if we couldn't get the mutex */
  java.lang.Thread id = java.lang.Thread.currentThread();//cc_thread_id();
  
  assert(recmutex != null);
  Mutex.cc_mutex_lock(recmutex.mutex);
  if (recmutex.level == 0) {
    recmutex.level++;
    recmutex.threadid = id;
    level = recmutex.level;
  }
  else if (id == recmutex.threadid) {
    recmutex.level++;
    level = recmutex.level;
  }
  else if (wait != 0) {
    recmutex.waiters++;
    /* wait in loop, since some thread might snatch the mutex before 
       us when we receive a signal */
    do {
      Condvar.cc_condvar_wait(recmutex.condvar, recmutex.mutex);
    } while (recmutex.level > 0);
    
    assert(recmutex.level == 0);
    recmutex.waiters--;
    recmutex.threadid = id;
    recmutex.level++;
    level = recmutex.level;
  }
  Mutex.cc_mutex_unlock(recmutex.mutex);
  return level;  
}


public static void 
cc_recmutex_init()
{
  recmutex_field_lock = cc_recmutex_construct();
  recmutex_notify_lock = cc_recmutex_construct();
  /* atexit priority makes this callback trigger after normal cleanup
     functions which might still use a recmutex instance */
  //coin_atexit((coin_atexit_f*) recmutex_cleanup, CC_ATEXIT_THREADING_SUBSYSTEM); java port
}
}
