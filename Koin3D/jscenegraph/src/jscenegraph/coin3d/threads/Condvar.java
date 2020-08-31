/**
 * 
 */
package jscenegraph.coin3d.threads;

import java.util.concurrent.locks.Lock;

/**
 * @author Yves Boyadjian
 *
 */
public class Condvar {

/*! Wait indefinitely for the \a condvar conditional variable
    using the specified \a mutex lock. */

public static int
cc_condvar_wait(/*cc_condvar*/Object condvar, /*cc_mutex*/Lock mutex)
{
  int ok;
  assert(condvar != null);
  ok = internal_condvar_wait(condvar, mutex);
  assert(ok == Common.cc_retval.CC_OK.ordinal());
  return ok;
}

/*! Wake one thread waiting for the \a condvar conditional variable. */

public static void
cc_condvar_wake_one(Object condvar)
{
  int ok;
  assert(condvar != null);
  ok = internal_condvar_wake_one(condvar);
  assert(ok == Common.cc_retval.CC_OK.ordinal());
}


static int
internal_condvar_wait(Object condvar, /*cc_mutex **/Lock mutex)
{
	try {
		condvar.wait();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return Common.cc_retval.CC_ERROR.ordinal();
	}
	return Common.cc_retval.CC_OK.ordinal();
}

static int
internal_condvar_wake_one(Object condvar)
{
	condvar.notify();
	return Common.cc_retval.CC_OK.ordinal();
}

}
