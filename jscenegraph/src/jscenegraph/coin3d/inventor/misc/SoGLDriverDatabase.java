/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.database.inventor.SbName;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLDriverDatabase {

/**************************************************************************/

// OpenGL features that can't be checked with a single GL_ARB/EXT extension test
public static final String SO_GL_MULTIDRAW_ELEMENTS    ="COIN_multidraw_elements";
public static final String SO_GL_POLYGON_OFFSET        ="COIN_polygon_offset";
public static final String SO_GL_TEXTURE_OBJECT        ="COIN_texture_object";
public static final String SO_GL_3D_TEXTURES           ="COIN_3d_textures";
public static final String SO_GL_MULTITEXTURE          ="COIN_multitexture";
public static final String SO_GL_TEXSUBIMAGE           ="COIN_texsubimage";
public static final String SO_GL_2D_PROXY_TEXTURES     ="COIN_2d_proxy_textures";
public static final String SO_GL_TEXTURE_EDGE_CLAMP    ="COIN_texture_edge_clamp";
public static final String SO_GL_TEXTURE_COMPRESSION   ="COIN_texture_compression";
public static final String SO_GL_COLOR_TABLES          ="COIN_color_tables";
public static final String SO_GL_COLOR_SUBTABLES       ="COIN_color_subtables";
public static final String SO_GL_PALETTED_TEXTURES     ="COIN_paletted_textures";
public static final String SO_GL_BLEND_EQUATION        ="COIN_blend_equation";
public static final String SO_GL_VERTEX_ARRAY          ="COIN_vertex_array";
public static final String SO_GL_NV_VERTEX_ARRAY_RANGE ="COIN_nv_vertex_array_range";
public static final String SO_GL_VERTEX_BUFFER_OBJECT  ="COIN_vertex_buffer_object";
public static final String SO_GL_ARB_FRAGMENT_PROGRAM  ="COIN_arb_fragment_program";
public static final String SO_GL_ARB_VERTEX_PROGRAM    ="COIN_arb_vertex_program";
public static final String SO_GL_ARB_VERTEX_SHADER     ="COIN_arb_vertex_shader";
public static final String SO_GL_ARB_SHADER_OBJECT     ="COIN_arb_shader_object";
public static final String SO_GL_OCCLUSION_QUERY       ="COIN_occlusion_query";
public static final String SO_GL_FRAMEBUFFER_OBJECT    ="COIN_framebuffer_object";
public static final String SO_GL_ANISOTROPIC_FILTERING ="COIN_anisotropic_filtering";
public static final String SO_GL_SORTED_LAYERS_BLEND   ="COIN_sorted_layers_blend";
public static final String SO_GL_BUMPMAPPING           ="COIN_bumpmapping";
public static final String SO_GL_VBO_IN_DISPLAYLIST    ="COIN_vbo_in_displaylist";
public static final String SO_GL_NON_POWER_OF_TWO_TEXTURES ="COIN_non_power_of_two_textures";
public static final String SO_GL_GENERATE_MIPMAP       ="COIN_generate_mipmap";
public static final String SO_GL_GLSL_CLIP_VERTEX_HW   ="COIN_GLSL_clip_vertex_hw";

static SoGLDriverDatabaseP pimpl_instance = null;

public static SoGLDriverDatabaseP pimpl()
{
  if (pimpl_instance == null) {
    pimpl_instance = new SoGLDriverDatabaseP();
    //cc_coin_atexit((coin_atexit_f*) sogldriverdatabase_atexit);
  }
  return pimpl_instance;
}

/*!

  Convenience function which checks whether \a feature is supported
  for \a context.  If \a feature is an OpenGL extension, it checks if
  it is actually supported by the driver, and then calls
  SoGLDriverDatabase::isBroken() to check if the feature is broken for
  \a context.

 */
	public static boolean isSupported(cc_glglue context, String feature) {
		  return pimpl().isSupported(context, new SbName(feature));
	}

	public static void init() {
		  // make sure the private static class is created to avoid race conditions
		  pimpl();
	}
}

