/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import java.util.HashMap;

import jscenegraph.coin3d.glue.Gl;
import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLDriverDatabaseP {
	
	interface glglue_feature_test_f {
	  boolean invoke(final cc_glglue glue);
	}
	  
	final HashMap<String, glglue_feature_test_f> featuremap = new HashMap<>();	

	public SoGLDriverDatabaseP()
	{
	  this.initFunctions();
	}

	/*
	  Initialize tests and reserve some feature names for features that cannot
	  be tested directly as a single OpenGL extension test
	*/
	public void initFunctions()
	{
		
		  featuremap.put(new SbName(SoGLDriverDatabase.SO_GL_ANISOTROPIC_FILTERING).getString(),
                  Gl::cc_glglue_can_do_anisotropic_filtering);
		  
		  featuremap.put(new SbName(SoGLDriverDatabase.SO_GL_NON_POWER_OF_TWO_TEXTURES).getString(),
                  SoGL::coin_glglue_non_power_of_two_textures);

		  featuremap.put(new SbName(SoGLDriverDatabase.SO_GL_GENERATE_MIPMAP).getString(),
                  SoGL::coin_glglue_has_generate_mipmap);
	}
public boolean isSupported(final cc_glglue context, final SbName feature)
{
  // check if we're asking about an actual GL extension
  String str = feature.getString();
  if ((feature.getLength() > 3) && (str.charAt(0) == 'G') && (str.charAt(1) == 'L') && (str.charAt(2) == '_')) {
    if (!SoGL.cc_glglue_glext_supported(context, /*feature*/str)) return false;
  }
  else { // check our lookup table
	  glglue_feature_test_f iter = this.featuremap.get(feature.getString());
    if (iter!=null) {
      glglue_feature_test_f testfunc = iter;
      if (!testfunc.invoke(context)) return false;
    }
    else {
//      SoDebugError.post("SoGLDriverDatabase::isSupported",
//                         "Unknown feature '"+feature.getString()+"'.");
    }
  }
  return true;//!(isBroken(context, feature) || isDisabled(context, feature));
}

}
