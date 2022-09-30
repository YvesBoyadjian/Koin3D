/**
 * 
 */
package jscenegraph.opengl.glu;

import java.nio.ByteBuffer;

import org.lwjglx.util.glu.GLUtessellator;
import org.lwjglx.util.glu.GLUtessellatorCallback;
import org.lwjglx.util.glu.tessellation.GLUtessellatorImpl;

import jscenegraph.opengl.GL;

/**
 * @author Yves Boyadjian
 *
 */
public class GLU {
	
	public GLU() {
	}

	  
	  // Boolean
	  public static final int GLU_FALSE = 0;
	  public static final int GLU_TRUE = 1;
	  
	  // String Name
	  public static final int GLU_VERSION = 100800;
	  public static final int GLU_EXTENSIONS = 100801;
	  
	  // Extensions
	  public static final String versionString = "1.3";
	  public static final String extensionString = "GLU_EXT_nurbs_tessellator " +
	                                               "GLU_EXT_object_space_tess ";
	  
	  // ErrorCode
	  public static final int GLU_INVALID_ENUM = 100900;
	  public static final int GLU_INVALID_VALUE = 100901;
	  public static final int GLU_OUT_OF_MEMORY = 100902;
	  public static final int GLU_INVALID_OPERATION = 100904;
	  
	  
	  // QuadricDrawStyle
	  public static final int GLU_POINT = 100010;
	  public static final int GLU_LINE = 100011;
	  public static final int GLU_FILL = 100012;
	  public static final int GLU_SILHOUETTE = 100013;
	  
	  // QuadricCallback
	  // GLU_ERROR
	  
	  // QuadricNormal
	  public static final int GLU_SMOOTH = 100000;
	  public static final int GLU_FLAT = 100001;
	  public static final int GLU_NONE = 100002;
	  
	  // QuadricOrientation
	  public static final int GLU_OUTSIDE = 100020;
	  public static final int GLU_INSIDE = 100021;
	  
	  // NurbsDisplay
	  // GLU_FILL
	  //public static final int GLU_OUTLINE_POLYGON = 100240;
	  //public static final int GLU_OUTLINE_PATCH = 100241;
	  
	  // NurbsCallback
	  //public static final int GLU_NURBS_ERROR = 100103;
	  public static final int GLU_ERROR = 100103;
	  //public static final int GLU_NURBS_BEGIN = 100164;
	  //public static final int GLU_NURBS_BEGIN_EXT = 100164;
	  //public static final int GLU_NURBS_VERTEX = 100165;
	  //public static final int GLU_NURBS_VERTEX_EXT = 100165;
	  //public static final int GLU_NURBS_NORMAL = 100166;
	  //public static final int GLU_NURBS_NORMAL_EXT = 100166;
	  //public static final int GLU_NURBS_COLOR = 100167;
	  //public static final int GLU_NURBS_COLOR_EXT = 100167;
	  //public static final int GLU_NURBS_TEXTURE_COORD = 100168;
	  //public static final int GLU_NURBS_TEX_COORD_EXT = 100168;
	  //public static final int GLU_NURBS_END = 100169;
	  //public static final int GLU_NURBS_END_EXT = 100169;
	  //public static final int GLU_NURBS_BEGIN_DATA = 100170;
	  //public static final int GLU_NURBS_BEGIN_DATA_EXT = 100170;
	  //public static final int GLU_NURBS_VERTEX_DATA = 100171;
	  //public static final int GLU_NURBS_VERTEX_DATA_EXT = 100171;
	  //public static final int GLU_NURBS_NORMAL_DATA = 100172;
	  //public static final int GLU_NURBS_NORMAL_DATA_EXT = 100172;
	  //public static final int GLU_NURBS_COLOR_DATA = 100173;
	  //public static final int GLU_NURBS_COLOR_DATA_EXT = 100173;
	  //public static final int GLU_NURBS_TEXTURE_COORD_DATA = 100174;
	  //public static final int GLU_NURBS_TEX_COORD_DATA_EXT = 100174;
	  //public static final int GLU_NURBS_END_DATA = 100175;
	  //public static final int GLU_NURBS_END_DATA_EXT = 100175;
	  
	  // NurbsError
	  //public static final int GLU_NURBS_ERROR1 = 100251;
	  //public static final int GLU_NURBS_ERROR2 = 100252;
	  //public static final int GLU_NURBS_ERROR3 = 100253;
	  //public static final int GLU_NURBS_ERROR4 = 100254;
	  //public static final int GLU_NURBS_ERROR5 = 100255;
	  //public static final int GLU_NURBS_ERROR6 = 100256;
	  //public static final int GLU_NURBS_ERROR7 = 100257;
	  //public static final int GLU_NURBS_ERROR8 = 100258;
	  //public static final int GLU_NURBS_ERROR9 = 100259;
	  //public static final int GLU_NURBS_ERROR10 = 100260;
	  //public static final int GLU_NURBS_ERROR11 = 100261;
	  //public static final int GLU_NURBS_ERROR12 = 100262;
	  //public static final int GLU_NURBS_ERROR13 = 100263;
	  //public static final int GLU_NURBS_ERROR14 = 100264;
	  //public static final int GLU_NURBS_ERROR15 = 100265;
	  //public static final int GLU_NURBS_ERROR16 = 100266;
	  //public static final int GLU_NURBS_ERROR17 = 100267;
	  //public static final int GLU_NURBS_ERROR18 = 100268;
	  //public static final int GLU_NURBS_ERROR19 = 100269;
	  //public static final int GLU_NURBS_ERROR20 = 100270;
	  //public static final int GLU_NURBS_ERROR21 = 100271;
	  //public static final int GLU_NURBS_ERROR22 = 100272;
	  //public static final int GLU_NURBS_ERROR23 = 100273;
	  //public static final int GLU_NURBS_ERROR24 = 100274;
	  //public static final int GLU_NURBS_ERROR25 = 100275;
	  //public static final int GLU_NURBS_ERROR26 = 100276;
	  //public static final int GLU_NURBS_ERROR27 = 100277;
	  //public static final int GLU_NURBS_ERROR28 = 100278;
	  //public static final int GLU_NURBS_ERROR29 = 100279;
	  //public static final int GLU_NURBS_ERROR30 = 100280;
	  //public static final int GLU_NURBS_ERROR31 = 100281;
	  //public static final int GLU_NURBS_ERROR32 = 100282;
	  //public static final int GLU_NURBS_ERROR33 = 100283;
	  //public static final int GLU_NURBS_ERROR34 = 100284;
	  //public static final int GLU_NURBS_ERROR35 = 100285;
	  //public static final int GLU_NURBS_ERROR36 = 100286;
	  //public static final int GLU_NURBS_ERROR37 = 100287;
	  
	  // NurbsProperty
	  //public static final int GLU_AUTO_LOAD_MATRIX = 100200;
	  //public static final int GLU_CULLING = 100201;
	  //public static final int GLU_SAMPLING_TOLERANCE = 100203;
	  //public static final int GLU_DISPLAY_MODE = 100204;
	  //public static final int GLU_PARAMETRIC_TOLERANCE = 100202;
	  //public static final int GLU_SAMPLING_METHOD = 100205;
	  //public static final int GLU_U_STEP = 100206;
	  //public static final int GLU_V_STEP = 100207;
	  //public static final int GLU_NURBS_MODE = 100160;
	  //public static final int GLU_NURBS_MODE_EXT = 100160;
	  //public static final int GLU_NURBS_TESSELLATOR = 100161;
	  //public static final int GLU_NURBS_TESSELLATOR_EXT = 100161;
	  //public static final int GLU_NURBS_RENDERER = 100162;
	  //public static final int GLU_NURBS_RENDERER_EXT = 100162;
	  
	  // NurbsSampling
	  //public static final int GLU_OBJECT_PARAMETRIC_ERROR = 100208;
	  //public static final int GLU_OBJECT_PARAMETRIC_ERROR_EXT = 100208;
	  //public static final int GLU_OBJECT_PATH_LENGTH = 100209;
	  //public static final int GLU_OBJECT_PATH_LENGTH_EXT = 100209;
	  //public static final int GLU_PATH_LENGTH = 100215;
	  //public static final int GLU_PARAMETRIC_ERROR = 100216;
	  //public static final int GLU_DOMAIN_DISTANCE = 100217;
	  
	  // NurbsTrim
	  //public static final int GLU_MAP1_TRIM_2 = 100210;
	  //public static final int GLU_MAP1_TRIM_3 = 100211;
	  
	  // QuadricCallback
	  // GLU_ERROR
	  
	  // TessCallback
	  public static final int GLU_TESS_BEGIN = 100100;
	  public static final int GLU_BEGIN = 100100;
	  public static final int GLU_TESS_VERTEX = 100101;
	  public static final int GLU_VERTEX = 100101;
	  public static final int GLU_TESS_END = 100102;
	  public static final int GLU_END = 100102;
	  public static final int GLU_TESS_ERROR = 100103;
	  public static final int GLU_TESS_EDGE_FLAG = 100104;
	  public static final int GLU_EDGE_FLAG = 100104;
	  public static final int GLU_TESS_COMBINE = 100105;
	  public static final int GLU_TESS_BEGIN_DATA = 100106;
	  public static final int GLU_TESS_VERTEX_DATA = 100107;
	  public static final int GLU_TESS_END_DATA = 100108;
	  public static final int GLU_TESS_ERROR_DATA = 100109;
	  public static final int GLU_TESS_EDGE_FLAG_DATA = 100110;
	  public static final int GLU_TESS_COMBINE_DATA = 100111;
	  
	  // TessContour
	  public static final int GLU_CW = 100120;
	  public static final int GLU_CCW = 100121;
	  public static final int GLU_INTERIOR = 100122;
	  public static final int GLU_EXTERIOR = 100123;
	  public static final int GLU_UNKNOWN = 100124;
	  
	  // TessProperty
	  public static final int GLU_TESS_WINDING_RULE = 100140;
	  public static final int GLU_TESS_BOUNDARY_ONLY = 100141;
	  public static final int GLU_TESS_TOLERANCE = 100142;
	  // JOGL-specific boolean property, false by default, that may improve the tessellation
	  public static final int GLU_TESS_AVOID_DEGENERATE_TRIANGLES = 100149;
	  
	  // TessError
	  public static final int GLU_TESS_ERROR1 = 100151;
	  public static final int GLU_TESS_ERROR2 = 100152;
	  public static final int GLU_TESS_ERROR3 = 100153;
	  public static final int GLU_TESS_ERROR4 = 100154;
	  public static final int GLU_TESS_ERROR5 = 100155;
	  public static final int GLU_TESS_ERROR6 = 100156;
	  public static final int GLU_TESS_ERROR7 = 100157;
	  public static final int GLU_TESS_ERROR8 = 100158;
	  public static final int GLU_TESS_MISSING_BEGIN_POLYGON = 100151;
	  public static final int GLU_TESS_MISSING_BEGIN_CONTOUR = 100152;
	  public static final int GLU_TESS_MISSING_END_POLYGON = 100153;
	  public static final int GLU_TESS_MISSING_END_CONTOUR = 100154;
	  public static final int GLU_TESS_COORD_TOO_LARGE = 100155;
	  public static final int GLU_TESS_NEED_COMBINE_CALLBACK = 100156;
	  
	  // TessWinding
	  public static final int GLU_TESS_WINDING_ODD = 100130;
	  public static final int GLU_TESS_WINDING_NONZERO = 100131;
	  public static final int GLU_TESS_WINDING_POSITIVE = 100132;
	  public static final int GLU_TESS_WINDING_NEGATIVE = 100133;
	  public static final int GLU_TESS_WINDING_ABS_GEQ_TWO = 100134;
	  public static final double GLU_TESS_MAX_COORD = 1.0e150;
	  
	  /**
	   * Instantiates a GLU implementation object in respect to the given GL profile
	   * of the given GL.
	   */
	  public static final GLU createGLU(GL gl) {
	    try {
	        Class c = GLU.class;
	        return (GLU) c.newInstance();
	    } catch (Exception e) {
	    	return null;
	    }
	  }

	public static void gluScaleImage(int format, short s, short t, int glUnsignedByte, ByteBuffer wrap, short u, short v,
			int glUnsignedByte2, ByteBuffer wrap2) {
		org.lwjglx.util.glu.GLU.gluScaleImage(format, s, t, glUnsignedByte, wrap, u, v,
				glUnsignedByte2, wrap2);
	}

	public static void gluTessBeginPolygon(GLUtessellator tobj, Object object) {
		tobj.gluTessBeginPolygon(object);
	}

	public static void gluTessBeginContour(GLUtessellator tobj) {
		tobj.gluTessBeginContour();
	}

	public static void gluTessEndContour(GLUtessellator tobj) {
		tobj.gluTessEndContour();
	}

	public static void gluTessEndPolygon(GLUtessellator tobj) {
		tobj.gluTessEndPolygon();
	}

	public static GLUtessellator gluNewTess() {
		return GLUtessellatorImpl.gluNewTess();
	}
	
	public static void gluDeleteTess(GLUtessellator tobj) {
		tobj.gluDeleteTess();
	}

	public static void gluTessCallback(GLUtessellator tobj, int gluBegin, GLUtessellatorCallback cb) {
		tobj.gluTessCallback(gluBegin, cb);
	}

	public static void gluTessVertex(GLUtessellator tobj, double[] dv, int i, Object object) {
		tobj.gluTessVertex(dv, i, object);
	}

	public static String gluErrorString(int error_code) {
		return org.lwjglx.util.glu.GLU.gluErrorString(error_code);
	}

	public static void gluTessNormal(GLUtessellator tobj, double x, double y, double z) {
		tobj.gluTessNormal(x, y, z);
	}
}
