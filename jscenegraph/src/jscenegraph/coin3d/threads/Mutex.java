/**
 * 
 */
package jscenegraph.coin3d.threads;

import java.util.concurrent.locks.Lock;

/**
 * @author Yves Boyadjian
 *
 */
public class Mutex {


/*! Locks the \a mutex specified. */
public static void
cc_mutex_lock(/*cc_mutex*/Lock mutex)
{
	mutex.lock();
}
/*! Unlocks the specified \a mutex.*/

public static void
cc_mutex_unlock(/*cc_mutex **/ Lock mutex)
{
	mutex.unlock();
}
}
