/**
 * 
 */
package jscenegraph.coin3d.glue;

import org.lwjglx.util.glu.GLUtessellator;
import org.lwjglx.util.glu.GLUtessellatorCallback;

import com.jogamp.opengl.glu.GLU;

import jscenegraph.database.inventor.SbVec2f;

/**
 * @author Yves Boyadjian
 *
 */
public class GLUWrapper_t {
	
	public interface gluNewTess {
		GLUtessellator invoke();
	}
	
	public interface gluTessBeginPolygon {
		void invoke(GLUtessellator tobj, Object object);
	}
	
	public interface gluTessBeginContour {
		void invoke(GLUtessellator tobj);
	}
	
	public interface gluTessEndContour {
		void invoke(GLUtessellator tobj);
	}
	
	public interface gluTessEndPolygon {
		void invoke(GLUtessellator tobj);
	}
	
	public interface gluDeleteTess {
		void invoke(GLUtessellator tobj);
	}
	
	public interface gluTessCallback {
		void invoke(GLUtessellator tobj, int gluBegin, GLUtessellatorCallback cb);
	}
	
	public interface gluTessNormal {
		void invoke(GLUtessellator tobj, double x, double y, double z);
	}
	
	public interface gluErrorString {
		String invoke(int error_code);
	}
	
	public interface gluTessVertex {
		void invoke(GLUtessellator tobj, double[] v, int i, Object t); 
	}

	public static final int  available = 1;
	public static final gluNewTess gluNewTess = GLU::gluNewTess;
	public static final gluTessBeginPolygon gluTessBeginPolygon = GLU::gluTessBeginPolygon;
	public static final gluTessBeginContour gluTessBeginContour = GLU::gluTessBeginContour;
	public static final gluTessEndContour gluTessEndContour = GLU::gluTessEndContour;
	public static final gluTessEndPolygon gluTessEndPolygon = GLU::gluTessEndPolygon;
	public static final gluDeleteTess gluDeleteTess = GLU::gluDeleteTess;
	public static final gluTessCallback gluTessCallback = GLU::gluTessCallback;
	public static final gluTessNormal gluTessNormal = GLU::gluTessNormal;
	public static final gluErrorString gluErrorString = GLU::gluErrorString;
	public static final gluTessVertex gluTessVertex = GLU::gluTessVertex;
}
