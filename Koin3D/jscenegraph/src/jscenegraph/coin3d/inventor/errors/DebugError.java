/**
 * 
 */
package jscenegraph.coin3d.inventor.errors;

/**
 * @author Yves Boyadjian
 *
 */
public class DebugError {

	public static void cc_debugerror_post(String string, String string2) {
		System.err.println(string+" : "+string2);
	}

}
