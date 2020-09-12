/**
 * 
 */
package jscenegraph.coin3d.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Yves Boyadjian
 *
 */
public class Condvar {

/*! Wait indefinitely for the \a condvar conditional variable
    using the specified \a mutex lock. */

public static int
cc_condvar_wait(/*cc_condvar*/Condition condvar, /*cc_mutex*/Lock mutex)
{
  int ok;
  assert(condvar != null);
  ok = internal_condvar_wait(condvar, mutex);
  assert(ok == Common.cc_retval.CC_OK.ordinal());
  return ok;
}

/*! Wake one thread waiting for the \a condvar conditional variable. */

public static void
cc_condvar_wake_one(Condition condvar)
{
  int ok;
  assert(condvar != null);
  ok = internal_condvar_wake_one(condvar);
  assert(ok == Common.cc_retval.CC_OK.ordinal());
}


static int
internal_condvar_wait(Condition condvar, /*cc_mutex **/Lock mutex)
{
	try {
		condvar.await();
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return Common.cc_retval.CC_ERROR.ordinal();
	}
	
//	try {
//		synchronized(condvar) {
//			condvar.wait();
//		}
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return Common.cc_retval.CC_ERROR.ordinal();
//	}
	return Common.cc_retval.CC_OK.ordinal();
}

static int
internal_condvar_wake_one(Condition condvar)
{
	synchronized(condvar) {
		condvar.signal();
	}
	return Common.cc_retval.CC_OK.ordinal();
}

}
