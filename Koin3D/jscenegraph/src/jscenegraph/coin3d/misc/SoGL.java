package jscenegraph.coin3d.misc;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLTextureImageElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Util;

public class SoGL {

	static int didwarn = 0;
// Convenience function for access to OpenGL wrapper from an SoState
// pointer.
public static cc_glglue 
sogl_glue_instance( SoState  state)
{
  SoGLRenderAction  action = (SoGLRenderAction )state.getAction();
  // FIXME: disabled until we figure out why this doesn't work on some
  // Linux systems (gcc 3.2 systems, it seems). pederb, 2003-11-24
//#if 0
//  assert(action->isOfType(SoGLRenderAction::getClassTypeId()) &&
//         "must have state from SoGLRenderAction to get hold of GL wrapper");
//  return cc_glglue_instance(action->getCacheContext());
//#else // disabled
  if (action.isOfType(SoGLRenderAction.getClassTypeId())) {
    return cc_glglue_instance(action.getCacheContext());
  }
  if (didwarn == 0) {
    didwarn = 1;
    SoDebugError.postWarning("sogl_glue_instance",
                              "Wrong action type detected. Please report this to <coin-support@sim.no>, "+
                              "and include information about your system (compiler, Linux version, etc.");
  }
  // just return some cc_glglue instance. It usually doesn't matter
  // that much unless multiple contexts on multiple displays are used.
  return cc_glglue_instance(/*1*/null);
//#endif // workaround version
}

// java port
public static cc_glglue cc_glglue_instance(GL2 cacheContext) {
	return new cc_glglue(cacheContext);
}


// Needed until all logic in SoGLTextureImageElement is moved to SoGLMultiTextureImageElement
public static void
sogl_update_shapehints_transparency(SoState state)
{
  SoShapeStyleElement.setTransparentTexture(state, 
                                             SoGLTextureImageElement.hasTransparency(state) ||
                                             SoGLMultiTextureImageElement.hasTransparency(state));
}

static int COIN_GLERROR_DEBUGGING = -1;

// Used by library code to decide whether or not to add extra
// debugging checks for glGetError().
public static boolean
sogl_glerror_debugging()
{
  if (COIN_GLERROR_DEBUGGING == -1) {
    String str = /*coin_getenv*/System.getenv("COIN_GLERROR_DEBUGGING");
    COIN_GLERROR_DEBUGGING = ((str!=null) ? Util.atoi(str) : 0);
  }
  return (COIN_GLERROR_DEBUGGING == 0) ? false : true;
}

public static cc_glglue
instance_from_context_ptr(Object ctx)
{
  /* The id can really be anything unique for the current context, but
     we should avoid a crash with the possible ids defined by
     SoGLCacheContextElement. It's a bit of a hack, this. */

  /* MSVC7 on 64-bit Windows wants this extra cast. */
  GL2 cast_aid = (GL2)ctx;
  /* FIXME: holy shit! This doesn't look sensible at all! (Could this
     e.g. be where the remote rendering bugs are coming from?)
     20050525 mortene.*/
  GL2 id = (GL2)cast_aid;

  return cc_glglue_instance(id);
}

}
