/**
 * 
 */
package jscenegraph.coin3d.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Yves Boyadjian
 *
 */
public class Cc_recmutex {
	java.lang.Thread threadid;
	  int level;
	  int waiters;
	  /*cc_mutex*/Lock mutex;
	  /*cc_condvar*/Condition condvar;
}
