/**
 * 
 */
package jterrain.profiler;

/**
 * @author Yves Boyadjian
 *
 */
public class PrProfiler {

	public static final void PR_START_PROFILE(String name) {}
	public static final void PR_START_OBJ_PROFILE(String name, Object object) {}
	public static final void PR_STOP_PROFILE(String name) {}
	public static final void PR_STOP_OBJ_PROFILE(String name, Object object) {}
	public static final void PR_INIT_PROFILER() {}
	public static final void PR_PRINT_RESULTS(String filename) {}

}
