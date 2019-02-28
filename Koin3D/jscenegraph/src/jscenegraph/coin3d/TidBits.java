/**
 * 
 */
package jscenegraph.coin3d;

import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class TidBits {

	/**
	 * 
	 */
	public TidBits() {
		// TODO Auto-generated constructor stub
	}


	  static int COIN_DEBUG_CACHING = -1;
	  
/*!
  Used for debugging caching.
*/
public static int
coin_debug_caching_level()
{
//#if COIN_DEBUG
  if (COIN_DEBUG_CACHING < 0) {
    String env = coin_getenv("COIN_DEBUG_CACHING");
    if (env != null) COIN_DEBUG_CACHING = Util.atoi(env);
    else COIN_DEBUG_CACHING = 0;
  }
  return COIN_DEBUG_CACHING;
//#else /* debug */
//  return 0;
//#endif /* !debug */
}


/**************************************************************************/

/* FIXME: should getenv/setenv/unsetenv be made mt-safe by locking access
 * to the envlist linked list under Win32?  20030205 larsa */

/*
  When working with MSWindows applications using Coin as a DLL,
  setenv() / getenv() will not work as expected, as the application
  and the DLL has different instances of the C library with different
  datastructures on different heaps. That's why we need this
  abstraction around the C-libs getenv() vs the Win32 API
  GetEnvironmentVariable() function.

  Note: do not deallocate the returned string -- they are supposed to
  be permanently available to all callers. Deallocating the resources
  is the responsibility of the application exit cleanup code (i.e. the
  internal library cleanup code -- application programmers won't need
  to care about it).
*/
public static String
coin_getenv(String envname)
{
	return System.getenv(envname); // java port
}
}
