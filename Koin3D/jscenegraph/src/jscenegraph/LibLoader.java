/**
 * 
 */
package jscenegraph;

import org.eclipse.core.runtime.Platform;

/**
 * @author Yves Boyadjian
 *
 */
public class LibLoader {

    private static void loadLibrary(String libname) {
        System.loadLibrary(libname);
    }

    /**
     * 
     */
    public static void loadLibraries(boolean debug) throws UnsatisfiedLinkError, SecurityException {

        String osname = Platform.getOS();

        if (osname.equals(Platform.OS_LINUX)) {
        	// nothing to do
        }
        else {
        	String suffix = "";
        	String suffix2 = "";
        	if(debug) {
        		suffix = "_d";
        		suffix2 = "d";
        	}

            /*
             * Order of libraries is very important!!!
             */
            loadLibrary("jawt"); //$NON-NLS-1$
            loadLibrary("msvcr120"+suffix2); //$NON-NLS-1$
            loadLibrary("msvcp120"+suffix2); //$NON-NLS-1$
            loadLibrary("vcomp120"+suffix2); //$NON-NLS-1$
            loadLibrary("glew"+suffix);
            loadLibrary("jpeg"+suffix);
            loadLibrary("iconv"+suffix);
            loadLibrary("Inventor"+suffix); //$NON-NLS-1$
        }
    }
}
