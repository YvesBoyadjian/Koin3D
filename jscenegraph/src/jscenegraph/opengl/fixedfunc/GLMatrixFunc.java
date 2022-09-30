/**
 * 
 */
package jscenegraph.opengl.fixedfunc;

/**
 * @author Yves Boyadjian
 *
 */
public interface GLMatrixFunc {

    public static final int GL_MATRIX_MODE = 0x0BA0;
    /** Matrix mode modelview */
    public static final int GL_MODELVIEW = 0x1700;
    /** Matrix mode projection */
    public static final int GL_PROJECTION = 0x1701;
    // public static final int GL_TEXTURE = 0x1702; // Use GL.GL_TEXTURE due to ambiguous GL usage
    /** Matrix access name for modelview */
    public static final int GL_MODELVIEW_MATRIX = 0x0BA6;
    /** Matrix access name for projection */
    public static final int GL_PROJECTION_MATRIX = 0x0BA7;
    /** Matrix access name for texture */
    public static final int GL_TEXTURE_MATRIX = 0x0BA8;

}
