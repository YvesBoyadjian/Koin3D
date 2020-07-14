/**
 * 
 */
package jscenegraph.coin3d.glue;

/**
 * @author Yves Boyadjian
 *
 */
public class Win32Api {

	/* ********************************************************************** */

	/* internal helper function */
	public static void
	cc_win32_print_error(String callerfuncname, String apifuncname,
	                     int lasterror)
	{
		String str = "(callerfuncname=='"+callerfuncname+"', apifuncname=='"+apifuncname+"', lasterror=="+lasterror+")";
        
        Gl_wgl.cc_debugerror_post("cc_win32_print_error",str);
	}
}
