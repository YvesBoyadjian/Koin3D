/**
 * 
 */
package jscenegraph.coin3d.inventor.base;

import org.lwjglx.util.glu.GLUtessellator;
import org.lwjglx.util.glu.GLUtessellatorCallbackAdapter;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.glue.GLUWrapper;
import jscenegraph.coin3d.glue.GLUWrapper_t;
import jscenegraph.coin3d.inventor.SbTesselator;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Util;

import jscenegraph.opengl.GL2;
import jscenegraph.opengl.glu.GLU;

/**
 * @author Yves Boyadjian
 *
 */
public class SbGLUTessellator implements Destroyable {
	
	public interface callback {
		void invoke(Object v0, Object v1, Object v2, Object data);
	}

	  callback callback;
	  Object cbdata;
	  /*coin_GLUtessellator*/GLUtessellator tessobj; //ptr

	  private class v {
		  public final double[] c = new double[3];
		  
		  public v(float x, float y, float z) {
			  c[0] = x;
			  c[1] = y;
			  c[2] = z;
		  }
		  };
	  final SbList<v> coords = new SbList<v>();

	  private /*GLenum*/int triangletessmode;
	  private int vertexidx;
	  private final Object[] vertexdata = new Object[2];
	  private boolean stripflipflop;
	  
	public SbGLUTessellator(callback cb, Object userdata) {
		  assert(cb != null);// && "tessellation without callback is meaningless");
		  this.callback = cb;
		  this.cbdata = userdata;

		  // allocated later on demand, so there is no resource allocation
		  // when just putting an SbGLUTessellator on the current stack frame:
		  this.tessobj = null;
	}
	
	public void destructor() {
		  if (this.tessobj != null) { GLUWrapper.GLUWrapper().gluDeleteTess.invoke(this.tessobj); }		
	}

	// *************************************************************************

	public static boolean
	available()
	{
	  return GLUWrapper.GLUWrapper().available != 0 &&
			  GLUWrapper.GLUWrapper().gluNewTess != null &&
			  GLUWrapper.GLUWrapper().gluTessBeginPolygon != null  &&
			  GLUWrapper.GLUWrapper().gluTessBeginContour != null  &&
			  GLUWrapper.GLUWrapper().gluTessEndContour != null  &&
			  GLUWrapper.GLUWrapper().gluTessEndPolygon != null  &&
			  GLUWrapper.GLUWrapper().gluDeleteTess != null;
	}

	// *************************************************************************

private static void 
cb_begin(int mode, Object x)
{
  SbGLUTessellator t = (SbGLUTessellator)(x);

  t.triangletessmode = mode;
  t.vertexidx = 0;
  t.stripflipflop = false;
}

private static void 
cb_vertex(Object vertex_data, Object x)
{
  SbGLUTessellator t = (SbGLUTessellator)(x);

  switch (t.triangletessmode) {
  case GL2.GL_TRIANGLE_FAN:
    if (t.vertexidx == 0) { t.vertexdata[0] = vertex_data; }
    else if (t.vertexidx == 1) { t.vertexdata[1] = vertex_data; }
    else {
      t.callback.invoke(t.vertexdata[0], t.vertexdata[1], vertex_data, t.cbdata);
      t.vertexdata[1] = vertex_data;
    }
    break;

  case GL2.GL_TRIANGLE_STRIP:
    if (t.vertexidx == 0) { t.vertexdata[0] = vertex_data; }
    else if (t.vertexidx == 1) { t.vertexdata[1] = vertex_data; }
    else {
      t.callback.invoke(t.vertexdata[t.stripflipflop ? 1 : 0],
                  t.vertexdata[t.stripflipflop ? 0 : 1],
                  vertex_data,
                  t.cbdata);

      t.vertexdata[0] = t.vertexdata[1];
      t.vertexdata[1] = vertex_data;
      t.stripflipflop = t.stripflipflop ? false : true;
    }
    break;

  case GL2.GL_TRIANGLES:
    if (t.vertexidx % 3 == 0) { t.vertexdata[0] = vertex_data; }
    else if (t.vertexidx % 3 == 1) { t.vertexdata[1] = vertex_data; }
    else if (t.vertexidx % 3 == 2) {
      t.callback.invoke(t.vertexdata[0], t.vertexdata[1], vertex_data, t.cbdata);
    }
    break;

  default:
    assert(false);
    break;
  }

  t.vertexidx++;
}

static int v_error = -1;

private static void 
cb_error(int err, Object obj)
{
  // These would be user errrors on our side, so catch them:
  assert(err != GLU.GLU_TESS_MISSING_BEGIN_POLYGON);
  assert(err != GLU.GLU_TESS_MISSING_END_POLYGON);
  assert(err != GLU.GLU_TESS_MISSING_BEGIN_CONTOUR);
  assert(err != GLU.GLU_TESS_MISSING_END_CONTOUR);
  
  // We will get this error if there are polygons with intersecting
  // edges (a "bow-tie" polygon, for instance), but this may be hard
  // to avoid for the app programmer, so we have made it possible to
  // silence this error messages by an envvar (according to the GLU
  // docs, the tessellator will be ok, it just ignored those polygons
  // and generate no output):
  if (err == GLU.GLU_TESS_NEED_COMBINE_CALLBACK) {
    if (v_error == -1) {
      String env = TidBits.coin_getenv("COIN_GLU_SILENCE_TESS_COMBINE_WARNING");
      v_error = (env != null && (Util.atoi(env) > 0))?1:0;
    }
    // requested to be silenced
    if (v_error != 0) { return; }
  }

  SoDebugError.post("SbGLUTessellator::cb_error",
                     "GLU library tessellation error: '"+GLUWrapper.GLUWrapper().gluErrorString.invoke(err)+"'"
                     );
}


	// *************************************************************************

	  private static int v = -1;
	  
	// Whether or not the SbGLUTessellator should be preferred over our
	// own Coin SbTesselator class, for tessellating faceset polygons.
	public static boolean
	preferred()
	{
		if( !SbTesselator.IMPLEMENTED ) {
			return true; // YB
		}
		
	  if (v == -1) {
	    String e = TidBits.coin_getenv("COIN_PREFER_GLU_TESSELLATOR");
	    v = (e != null && (Util.atoi(e) > 0)) ? 1 : 0;

	    if (v != 0 && !SbGLUTessellator.available()) {
	      SoDebugError.postWarning("SbGLUTessellator::preferred",
	                                "Preference setting "+
	                                "COIN_PREFER_GLU_TESSELLATOR indicates that "+
	                                "GLU tessellation is wanted, but GLU library "+
	                                "detected to not have this capability.");
	      v = 0;
	    }
	  }
	  return v != 0 ? true : false;
	}

	public void beginPolygon() {
		beginPolygon(new SbVec3f(0,0,0));
	}
	// *************************************************************************

public 	void
beginPolygon(final SbVec3f normal)
	{
	  if (this.tessobj == null) {
	    GLUtessellator t = this.tessobj = GLUWrapper.GLUWrapper().gluNewTess.invoke();

	    GLUWrapper_t.gluTessCallback f = GLUWrapper.GLUWrapper().gluTessCallback;
	    (f).invoke(t, GLU.GLU_TESS_BEGIN_DATA, new GLUtessellatorCallbackAdapter() {
			@Override
			public void beginData(int type, Object polygonData) {
				SbGLUTessellator.cb_begin(type, polygonData);
			}
	    });
	    (f).invoke(t, GLU.GLU_TESS_VERTEX_DATA, new GLUtessellatorCallbackAdapter() {
			@Override
	        public void vertexData(Object vertexData, Object polygonData) {
				SbGLUTessellator.cb_vertex(vertexData, polygonData);
			}
	    });
	    (f).invoke(t, GLU.GLU_TESS_ERROR_DATA, new GLUtessellatorCallbackAdapter() {
			@Override
	        public void errorData(int errnum, Object polygonData) {	    	
				SbGLUTessellator.cb_error(errnum, polygonData);
			}
	    });
	  }

	  GLUWrapper.GLUWrapper().gluTessBeginPolygon.invoke(this.tessobj, this);
	  if (normal.operator_not_equal(new SbVec3f(0, 0, 0))) {
		  GLUWrapper.GLUWrapper().gluTessNormal.invoke(this.tessobj, normal.getValueRead()[0], normal.getValueRead()[1], normal.getValueRead()[2]);
	  }

	  GLUWrapper.GLUWrapper().gluTessBeginContour.invoke(this.tessobj);
	}


public void
addVertex(final SbVec3f v, Object data)
{
  v c = new v( v.getValueRead()[0], v.getValueRead()[1], v.getValueRead()[2] );
  this.coords.append(c); // needs to be stored until gluTessEndPolygon()

  int l = this.coords.getLength();
  GLUWrapper.GLUWrapper().gluTessVertex.invoke(this.tessobj, this.coords.operator_square_bracket(l - 1).c, 0, data);
}

public void
endPolygon()
{
	GLUWrapper.GLUWrapper().gluTessEndContour.invoke(this.tessobj);
	GLUWrapper.GLUWrapper().gluTessEndPolygon.invoke(this.tessobj);

  this.coords.truncate(0);
}


	// *************************************************************************
}
