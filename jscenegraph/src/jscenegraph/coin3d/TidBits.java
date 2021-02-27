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

	static int COIN_DEBUG_EXTRA = -1;
	static int COIN_DEBUG_NORMALIZE = -1;

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


/*
 * Will return TRUE if extra debugging information is enabled. These
 * are typically debugging messages extra for Coin and not found in
 * SGI Inventor. Also, these debugging message will not necessarily
 * mean that anything is wrong, but they can be useful for debugging
 * anyway.
 */
public static int
coin_debug_extra()
{
//#if COIN_DEBUG
  return COIN_DEBUG_EXTRA;
//#else /* COIN_DEBUG */
//  return 0;
//#endif /* !COIN_DEBUG */
}

/* jitter values from OpenGL Programming Guide */
static float jitter2[] = {
  0.25f, 0.77f,
  0.75f, 0.25f
};
static float jitter3[] = {
  0.5033922635f, 0.8317967229f,
  0.7806016275f, 0.2504380877f,
  0.2261828938f, 0.4131553612f
};
static float jitter4[] = {
  0.375f, 0.25f,
  0.125f, 0.75f,
  0.875f, 0.25f,
  0.625f, 0.75f
};
static float jitter5[] = {
  0.5f, 0.5f,
  0.3f, 0.1f,
  0.7f, 0.9f,
  0.9f, 0.3f,
  0.1f, 0.7f
};
static float jitter6[] = {
  0.4646464646f, 0.4646464646f,
  0.1313131313f, 0.7979797979f,
  0.5353535353f, 0.8686868686f,
  0.8686868686f, 0.5353535353f,
  0.7979797979f, 0.1313131313f,
  0.2020202020f, 0.2020202020f
};
static float jitter8[] = {
  0.5625f, 0.4375f,
  0.0625f, 0.9375f,
  0.3125f, 0.6875f,
  0.6875f, 0.8125f,
  0.8125f, 0.1875f,
  0.9375f, 0.5625f,
  0.4375f, 0.0625f,
  0.1875f, 0.3125f
};
static float jitter9[] = {
  0.5f, 0.5f,
  0.1666666666f, 0.9444444444f,
  0.5f, 0.1666666666f,
  0.5f, 0.8333333333f,
  0.1666666666f, 0.2777777777f,
  0.8333333333f, 0.3888888888f,
  0.1666666666f, 0.6111111111f,
  0.8333333333f, 0.7222222222f,
  0.8333333333f, 0.0555555555f
};
static float jitter12[] = {
  0.4166666666f, 0.625f,
  0.9166666666f, 0.875f,
  0.25f, 0.375f,
  0.4166666666f, 0.125f,
  0.75f, 0.125f,
  0.0833333333f, 0.125f,
  0.75f, 0.625f,
  0.25f, 0.875f,
  0.5833333333f, 0.375f,
  0.9166666666f, 0.375f,
  0.0833333333f, 0.625f,
  0.583333333f, 0.875f
};
static float jitter16[] = {
  0.375f, 0.4375f,
  0.625f, 0.0625f,
  0.875f, 0.1875f,
  0.125f, 0.0625f,
  0.375f, 0.6875f,
  0.875f, 0.4375f,
  0.625f, 0.5625f,
  0.375f, 0.9375f,
  0.625f, 0.3125f,
  0.125f, 0.5625f,
  0.125f, 0.8125f,
  0.375f, 0.1875f,
  0.875f, 0.9375f,
  0.875f, 0.6875f,
  0.125f, 0.3125f,
  0.625f, 0.8125f
};

static float[] jittertab[] = {
  jitter2,
  jitter3,
  jitter4,
  jitter5,
  jitter6,
  jitter8,
  jitter8,
  jitter9,
  jitter12,
  jitter12,
  jitter12,
  jitter16,
  jitter16,
  jitter16,
  jitter16
};

/*
  Calculate the view volume jitter vector when doing multipass
  antialiasing rendering.
*/
public static void
coin_viewvolume_jitter(int numpasses, int curpass, int[] vpsize, float[] jitter)
{
  float[] jittab;

  /* FIXME: support more rendering passes by generating jitter tables
   * using some clever algorithm. pederb, 2001-02-21 */
  if (numpasses > 16) numpasses = 16;
  if (curpass >= numpasses) curpass = numpasses - 1;

  jittab = jittertab[numpasses-2];

  if (numpasses < 2) {
    jitter[0] = 0.0f;
    jitter[1] = 0.0f;
    jitter[2] = 0.0f;
  }
  else {
    jitter[0] = (jittab[curpass*2] - 0.5f) * 2.0f / ((float)vpsize[0]);
    jitter[1] = (jittab[curpass*2+1] - 0.5f) * 2.0f / ((float)vpsize[1]);
    jitter[2] = 0.0f;
  }
}


}
