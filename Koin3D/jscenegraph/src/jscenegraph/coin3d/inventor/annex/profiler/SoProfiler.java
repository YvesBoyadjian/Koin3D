/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler;

import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */
public class SoProfiler {

	static class profiler {
	    static boolean initialized = false;

	    static boolean enabled = false;
		
	}

/*!
  Returns whether profiling info is shown in an overlay fashion on
  the GL canvas or not.
*/
public static boolean
isOverlayActive()
{
  return false;//SoProfiler.isEnabled() && profiler.overlay.active; TODO
}

/*!
  Returns whether profiling info is shown on the console or not.
*/
public static boolean
isConsoleActive()
{
  return false;//SoProfiler.isEnabled() && profiler.console.active; TODO
}


/*!
  Enable/disable the profiling subsystem at runtime.
*/
public static void
enable(boolean enable)
{
  if (!profiler.initialized) {
    assert("SoProfiler module not initialized" == null);
    SoDebugError.post("SoProfiler::enable", "module not initialized");
    return;
  }
  profiler.enabled = enable;
}

	public static boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
