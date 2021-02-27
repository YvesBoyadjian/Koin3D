/**
 * 
 */
package com.jogamp.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL3 extends GL3ES3, GL2GL3 {


	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8e73;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_VERSION_4_1</code>, <code>GL_NV_viewport_array</code><br>Alias for: <code>GL_VIEWPORT_INDEX_PROVOKING_VERTEX_NV</code> - CType: int */
	  public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825f;
	  /** <code>GL_ARB_texture_gather</code><br>Alias for: <code>GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS_ARB</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS = 0x8f9f;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 0x8e4a;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT2x4_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT2x4 = 0x8f4a;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT2x3_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT2x3 = 0x8f49;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ARB_blend_func_extended</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_SRC1_COLOR_EXT</code> - CType: int */
	  public static final int GL_SRC1_COLOR = 0x88f9;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_LINES_ADJACENCY_ARB = 0xa;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB = 0x8ddf;
	  /** <code>GL_ARB_shading_language_include</code> - CType: int */
	  public static final int GL_SHADER_INCLUDE_ARB = 0x8dae;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_VEC4_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_VEC4 = 0x8ffe;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_VEC3_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_VEC3 = 0x8ffd;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_VEC2_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_VEC2 = 0x8ffc;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_LINE_STRIP_ADJACENCY_ARB = 0xb;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB = 0x8da7;
	  /** <code>GL_ARB_texture_compression_bptc</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT_ARB = 0x8e8e;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_VERSION_4_1</code>, <code>GL_NV_viewport_array</code><br>Alias for: <code>GL_VIEWPORT_BOUNDS_RANGE_NV</code> - CType: int */
	  public static final int GL_VIEWPORT_BOUNDS_RANGE = 0x825d;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_COMPATIBLE_SUBROUTINES = 0x8e4b;
	  /** <code>GL_ARB_depth_clamp</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_depth_clamp</code><br>Alias for: <code>GL_DEPTH_CLAMP_NV</code> - CType: int */
	  public static final int GL_DEPTH_CLAMP = 0x864f;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_TRIANGLE_STRIP_ADJACENCY_ARB = 0xd;
	  /** <code>GL_ARB_cl_event</code> - CType: int */
	  public static final int GL_SYNC_CL_EVENT_ARB = 0x8240;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_TRIANGLES_ADJACENCY_ARB = 0xc;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_ARB = 0x8da9;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ARB_blend_func_extended</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_ONE_MINUS_SRC1_COLOR_EXT</code> - CType: int */
	  public static final int GL_ONE_MINUS_SRC1_COLOR = 0x88fa;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 0x8de6;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8e74;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT4_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT4 = 0x8f48;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT3_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT3 = 0x8f47;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT2_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT2 = 0x8f46;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_VARYING_COMPONENTS_ARB = 0x8ddd;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8e49;
	  /** <code>GL_ARB_texture_compression_bptc</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_BPTC_UNORM_ARB = 0x8e8c;
	  /** <code>GL_ARB_texture_compression_bptc</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM_ARB = 0x8e8d;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB = 0x8c29;
	  /** <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 0x2;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_OUTPUT_TYPE_ARB = 0x8ddc;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_VERTICES_OUT_ARB = 0x8dda;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8de8;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB = 0x8de0;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 0x8e48;
	  /** <code>GL_ARB_shading_language_include</code> - CType: int */
	  public static final int GL_NAMED_STRING_LENGTH_ARB = 0x8de9;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT4x2_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT4x2 = 0x8f4d;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT4x3_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT4x3 = 0x8f4e;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_INPUT_TYPE_ARB = 0x8ddb;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ARB_blend_func_extended</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_ONE_MINUS_SRC1_ALPHA_EXT</code> - CType: int */
	  public static final int GL_ONE_MINUS_SRC1_ALPHA = 0x88fb;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84f0;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_PROGRAM_POINT_SIZE_ARB = 0x8642;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_SHADER_ARB = 0x8dd9;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_ACTIVE_SUBROUTINES = 0x8de5;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_VERTEX_VARYING_COMPONENTS_ARB = 0x8dde;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_VERSION_4_1</code>, <code>GL_NV_viewport_array</code><br>Alias for: <code>GL_VIEWPORT_SUBPIXEL_BITS_NV</code> - CType: int */
	  public static final int GL_VIEWPORT_SUBPIXEL_BITS = 0x825c;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB = 0x8de1;
	  /** <code>GL_ARB_cl_event</code> - CType: int */
	  public static final int GL_SYNC_CL_EVENT_COMPLETE_ARB = 0x8241;
	  /** <code>GL_ARB_geometry_shader4</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB = 0x8da8;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ARB_blend_func_extended</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_MAX_DUAL_SOURCE_DRAW_BUFFERS_EXT</code> - CType: int */
	  public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88fc;
	  /** <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_PROGRAM_POINT_SIZE_EXT</code>, <code>GL_PROGRAM_POINT_SIZE_ARB</code> - CType: int */
	  public static final int GL_PROGRAM_POINT_SIZE = 0x8642;
	  /** <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_CONTEXT_CORE_PROFILE_BIT = 0x1;
	  /** <code>GL_ARB_texture_compression_bptc</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT_ARB = 0x8e8f;
	  /** <code>GL_ARB_shading_language_include</code> - CType: int */
	  public static final int GL_NAMED_STRING_TYPE_ARB = 0x8dea;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT3x4_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT3x4 = 0x8f4c;
	  /** <code>GL_ARB_gpu_shader_fp64</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_vertex_attrib_64bit</code><br>Alias for: <code>GL_DOUBLE_MAT3x2_EXT</code> - CType: int */
	  public static final int GL_DOUBLE_MAT3x2 = 0x8f4b;
	  /** <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_CONTEXT_PROFILE_MASK = 0x9126;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_MAX_SUBROUTINES = 0x8de7;
	  /** <code>GL_ARB_shader_subroutine</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 0x8e47;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84f1;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_VERSION_4_1</code>, <code>GL_NV_viewport_array</code><br>Alias for: <code>GL_MAX_VIEWPORTS_NV</code> - CType: int */
	  public static final int GL_MAX_VIEWPORTS = 0x825b;

}
