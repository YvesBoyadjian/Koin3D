/**
 * 
 */
package com.jogamp.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL2ES3 extends GL2ES2 {


	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_transform_feedback2</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BINDING_NV</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BINDING = 0x8e25;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_MAX_IMAGE_UNITS_EXT</code> - CType: int */
	  public static final int GL_MAX_IMAGE_UNITS = 0x8f38;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_SGIS_texture_lod</code><br>Alias for: <code>GL_TEXTURE_MIN_LOD_SGIS</code> - CType: int */
	  public static final int GL_TEXTURE_MIN_LOD = 0x813a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT3x4_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT3x4 = 0x8b68;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT3x2_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT3x2 = 0x8b67;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_FRAMEBUFFER_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_BARRIER_BIT = 0x400;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR = 0x93d2;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_SIZE = 0x8a38;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_ACCESS_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_ACCESS = 0x8f3e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_shadow_samplers_array</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_SAMPLER_2D_ARRAY_SHADOW_NV</code>, <code>GL_SAMPLER_2D_ARRAY_SHADOW_EXT</code> - CType: int */
	  public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 0x8dc4;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_IMAGE_UNIFORMS = 0x90cf;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_LAYER_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_LAYER = 0x8f3d;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 0x8a40;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_surfaceless_context</code><br>Alias for: <code>GL_FRAMEBUFFER_UNDEFINED_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_UNDEFINED = 0x8219;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9276;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_ALL_BARRIER_BITS_EXT</code> - CType: long */
	  public static final long GL_ALL_BARRIER_BITS = 0xffffffffL;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_TEXTURE_FETCH_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_FETCH_BARRIER_BIT = 0x8;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_IMAGE_2D_ARRAY = 0x9053;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_GREEN_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_GREEN_TYPE = 0x8c11;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90c9;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_COMMAND_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_COMMAND_BARRIER_BIT = 0x40;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_NV_conditional_render</code><br>Alias for: <code>GL_QUERY_BY_REGION_NO_WAIT_NV</code> - CType: int */
	  public static final int GL_QUERY_BY_REGION_NO_WAIT = 0x8e16;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_BLUE_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BLUE_SIZE = 0x805e;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_ACTIVE_UNIFORM_BLOCKS = 0x8a36;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB32I_EXT</code> - CType: int */
	  public static final int GL_RGB32I = 0x8d83;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x5_KHR = 0x93b5;
	  /** <code>GL_ARB_texture_swizzle</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_texture_swizzle</code><br>Alias for: <code>GL_TEXTURE_SWIZZLE_R_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_R = 0x8e42;
	  /** <code>GL_ARB_texture_swizzle</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_texture_swizzle</code><br>Alias for: <code>GL_TEXTURE_SWIZZLE_B_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_B = 0x8e44;
	  /** <code>GL_ARB_texture_swizzle</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_texture_swizzle</code><br>Alias for: <code>GL_TEXTURE_SWIZZLE_A_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_A = 0x8e45;
	  /** <code>GL_ARB_texture_swizzle</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_texture_swizzle</code><br>Alias for: <code>GL_TEXTURE_SWIZZLE_G_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_G = 0x8e43;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_ARB_pixel_buffer_object</code>, <code>GL_EXT_pixel_buffer_object</code><br>Alias for: <code>GL_PIXEL_PACK_BUFFER_BINDING_ARB</code>, <code>GL_PIXEL_PACK_BUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_PIXEL_PACK_BUFFER_BINDING = 0x88ed;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R32UI = 0x8236;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RG11_EAC = 0x9272;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_NAME_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_NAME = 0x8f3a;
	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 0x8e23;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_robustness</code><br>Alias for: <code>GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB</code> - CType: int */
	  public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT = 0x4;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR = 0x93d4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA_INTEGER_EXT</code> - CType: int */
	  public static final int GL_RGBA_INTEGER = 0x8d99;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_MAX_ARRAY_TEXTURE_LAYERS_EXT</code> - CType: int */
	  public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88ff;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_shared_exponent</code>, <code>GL_APPLE_texture_packed_float</code><br>Alias for: <code>GL_UNSIGNED_INT_5_9_9_9_REV_EXT</code>, <code>GL_UNSIGNED_INT_5_9_9_9_REV_APPLE</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 0x8c3e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PACK_ROW_LENGTH = 0xd02;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_NV_conditional_render</code><br>Alias for: <code>GL_QUERY_WAIT_NV</code> - CType: int */
	  public static final int GL_QUERY_WAIT = 0x8e13;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_DYNAMIC_COPY_ARB</code> - CType: int */
	  public static final int GL_DYNAMIC_COPY = 0x88ea;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_MODE_NV</code>, <code>GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8c7f;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_ES3_2_compatibility</code><br>Alias for: <code>GL_MULTISAMPLE_LINE_WIDTH_RANGE_ARB</code> - CType: int */
	  public static final int GL_MULTISAMPLE_LINE_WIDTH_RANGE = 0x9381;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8a34;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_12x10_KHR = 0x93bc;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_NAME_LENGTH = 0x8a39;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_2D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_2D = 0x9063;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 0x9313;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RED_INTEGER_EXT</code> - CType: int */
	  public static final int GL_RED_INTEGER = 0x8d94;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_packed_depth_stencil</code><br>Alias for: <code>GL_TEXTURE_STENCIL_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_STENCIL_SIZE = 0x88f1;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_ARB_texture_buffer_object</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_EXT_texture_buffer_object</code>, <code>GL_OES_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BINDING_BUFFER_ARB</code>, <code>GL_TEXTURE_BINDING_BUFFER_EXT</code>, <code>GL_TEXTURE_BINDING_BUFFER_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_BUFFER = 0x8c2c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_NV</code>, <code>GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8c80;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_3D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_3D = 0x9064;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR = 0x93d0;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_RG11_EAC = 0x9273;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_ARB_pixel_buffer_object</code>, <code>GL_EXT_pixel_buffer_object</code><br>Alias for: <code>GL_PIXEL_UNPACK_BUFFER_BINDING_ARB</code>, <code>GL_PIXEL_UNPACK_BUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING = 0x88ef;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_3D_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_3D = 0x9059;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_2D_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_2D = 0x9058;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8a42;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_2D_ARRAY = 0x8dcf;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_SAMPLER_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_SAMPLER_2D_ARRAY = 0x8dc1;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_gpu_program4</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_MAX_PROGRAM_TEXEL_OFFSET_NV</code>, <code>GL_MAX_PROGRAM_TEXEL_OFFSET_EXT</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT4x3_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT4x3 = 0x8b6a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT4x2_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT4x2 = 0x8b69;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R16UI = 0x8234;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_RED_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_RED_TYPE = 0x8c10;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code>, <code>GL_ARB_copy_buffer</code>, <code>GL_NV_copy_buffer</code><br>Alias for: <code>GL_COPY_WRITE_BUFFER_NV</code> - CType: int */
	  public static final int GL_COPY_WRITE_BUFFER = 0x8f37;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR = 0x93d9;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x6_KHR = 0x93b9;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_2D_ARRAY = 0x9069;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_cube_map_array</code>, <code>GL_OES_texture_cube_map_array</code><br>Alias for: <code>GL_IMAGE_CUBE_MAP_ARRAY_EXT</code>, <code>GL_IMAGE_CUBE_MAP_ARRAY_OES</code> - CType: int */
	  public static final int GL_IMAGE_CUBE_MAP_ARRAY = 0x9054;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_TRIANGLES_ADJACENCY_EXT = 0xc;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_ATOMIC_COUNTER_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BARRIER_BIT = 0x1000;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R8I = 0x8231;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STREAM_COPY_ARB</code> - CType: int */
	  public static final int GL_STREAM_COPY = 0x88e2;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code>, <code>GL_OES_texture_cube_map_array</code><br>Alias for: <code>GL_INT_SAMPLER_CUBE_MAP_ARRAY_ARB</code>, <code>GL_INT_SAMPLER_CUBE_MAP_ARRAY_EXT</code>, <code>GL_INT_SAMPLER_CUBE_MAP_ARRAY_OES</code> - CType: int */
	  public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900e;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS_EXT</code>, <code>GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 0x92d3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_UNPACK_SKIP_IMAGES_EXT</code> - CType: int */
	  public static final int GL_UNPACK_SKIP_IMAGES = 0x806d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8dd7;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_texture_cube_map_array</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY_OES</code>, <code>GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY_ARB</code>, <code>GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900f;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_MATRIX_STRIDE = 0x8a3d;
	  /** <code>GL_EXT_provoking_vertex</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_FIRST_VERTEX_CONVENTION_EXT = 0x8e4d;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BARRIER_BIT = 0x800;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8a46;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STATIC_READ_ARB</code> - CType: int */
	  public static final int GL_STATIC_READ = 0x88e5;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_SIGNED_NORMALIZED = 0x8f9c;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_TEXTURE_UPDATE_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_UPDATE_BARRIER_BIT = 0x100;
	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_ACTIVE = 0x8e24;
	  /** <code>GL_ARB_get_program_binary</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8a35;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x8_KHR = 0x93b7;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90c7;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT = 0x8c29;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_SIZE = 0x8a2a;
	  /** <code>GL_EXT_provoking_vertex</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_LAST_VERTEX_CONVENTION_EXT = 0x8e4e;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 0x9279;
	  /** <code>GL_ARB_depth_buffer_float</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_depth_buffer_float</code><br>Alias for: <code>GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV</code> - CType: int */
	  public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8dad;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB8_ETC2 = 0x9274;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_SIZE = 0x92c3;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_ES3_2_compatibility</code><br>Alias for: <code>GL_MULTISAMPLE_LINE_WIDTH_GRANULARITY_ARB</code> - CType: int */
	  public static final int GL_MULTISAMPLE_LINE_WIDTH_GRANULARITY = 0x9382;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_ARB_texture_buffer_object</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_EXT_texture_buffer_object</code>, <code>GL_OES_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_ARB</code>, <code>GL_TEXTURE_BUFFER_EXT</code>, <code>GL_TEXTURE_BUFFER_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER = 0x8c2a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_MINOR_VERSION = 0x821c;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_NV_conditional_render</code><br>Alias for: <code>GL_QUERY_BY_REGION_WAIT_NV</code> - CType: int */
	  public static final int GL_QUERY_BY_REGION_WAIT = 0x8e15;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNIFORM_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_UNIFORM_BARRIER_BIT = 0x4;
	  /** <code>GL_ARB_vertex_array_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_vertex_array_object</code>, <code>GL_APPLE_vertex_array_object</code><br>Alias for: <code>GL_VERTEX_ARRAY_BINDING_OES</code>, <code>GL_VERTEX_ARRAY_BINDING_APPLE</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_BINDING = 0x85b5;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_BUFFER_UPDATE_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_BUFFER_UPDATE_BARRIER_BIT = 0x200;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 0x90c8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_ARB_pixel_buffer_object</code>, <code>GL_EXT_pixel_buffer_object</code><br>Alias for: <code>GL_PIXEL_PACK_BUFFER_ARB</code>, <code>GL_PIXEL_PACK_BUFFER_EXT</code> - CType: int */
	  public static final int GL_PIXEL_PACK_BUFFER = 0x88eb;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_PIXEL_BUFFER_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_PIXEL_BUFFER_BARRIER_BIT = 0x80;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8a43;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA16UI_EXT</code> - CType: int */
	  public static final int GL_RGBA16UI = 0x8d76;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_RASTERIZER_DISCARD_NV</code>, <code>GL_RASTERIZER_DISCARD_EXT</code> - CType: int */
	  public static final int GL_RASTERIZER_DISCARD = 0x8c89;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_NV</code>, <code>GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8c85;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_TESS_CONTROL_SHADER_BIT_OES</code>, <code>GL_TESS_CONTROL_SHADER_BIT_EXT</code> - CType: int */
	  public static final int GL_TESS_CONTROL_SHADER_BIT = 0x8;
	  /** <code>GL_ARB_geometry_shader4</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8cd4;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_texture_cube_map_array</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_ARRAY_OES</code>, <code>GL_TEXTURE_CUBE_MAP_ARRAY_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 0x9009;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_UNPACK_IMAGE_HEIGHT_EXT</code> - CType: int */
	  public static final int GL_UNPACK_IMAGE_HEIGHT = 0x806e;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR = 0x93dd;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_3D_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_3D = 0x8dcb;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_SHADER_IMAGE_ACCESS_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x20;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_draw_range_elements</code><br>Alias for: <code>GL_MAX_ELEMENTS_VERTICES_EXT</code> - CType: int */
	  public static final int GL_MAX_ELEMENTS_VERTICES = 0x80e8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_2D_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_2D = 0x8dca;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R8UI = 0x8232;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_ALPHA_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_ALPHA_TYPE = 0x8c13;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_FRAMEBUFFER_DEFAULT_LAYERS_OES</code>, <code>GL_FRAMEBUFFER_DEFAULT_LAYERS_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 0x9312;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_FORMAT_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_FORMAT = 0x906e;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_SAMPLER_BUFFER_AMD</code>, <code>GL_SAMPLER_BUFFER_OES</code>, <code>GL_SAMPLER_BUFFER_EXT</code> - CType: int */
	  public static final int GL_SAMPLER_BUFFER = 0x8dc2;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_sample_shading</code>, <code>GL_ARB_sample_shading</code><br>Alias for: <code>GL_MIN_SAMPLE_SHADING_VALUE_OES</code>, <code>GL_MIN_SAMPLE_SHADING_VALUE_ARB</code> - CType: int */
	  public static final int GL_MIN_SAMPLE_SHADING_VALUE = 0x8c37;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_cube_map_array</code>, <code>GL_OES_texture_cube_map_array</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY_EXT</code>, <code>GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY_OES</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 0x906a;
	  /** <code>GL_EXT_render_snorm</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RGBA8_SNORM = 0x8f97;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_ARB_pixel_buffer_object</code>, <code>GL_EXT_pixel_buffer_object</code><br>Alias for: <code>GL_PIXEL_UNPACK_BUFFER_ARB</code>, <code>GL_PIXEL_UNPACK_BUFFER_EXT</code> - CType: int */
	  public static final int GL_PIXEL_UNPACK_BUFFER = 0x88ec;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_UNIFORM_BLOCKS_EXT</code>, <code>GL_MAX_GEOMETRY_UNIFORM_BLOCKS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 0x8a2c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_sparse_texture</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_TEXTURE_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_2D_ARRAY = 0x8c1a;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_FRAMEBUFFER_WIDTH = 0x9315;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_MAX_ELEMENT_INDEX = 0x8d6b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT17 = 0x8cf1;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT16 = 0x8cf0;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT26 = 0x8cfa;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT25 = 0x8cf9;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT28 = 0x8cfc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT27 = 0x8cfb;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT22 = 0x8cf6;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT21 = 0x8cf5;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT24 = 0x8cf8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT23 = 0x8cf7;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT20 = 0x8cf4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT19 = 0x8cf3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT18 = 0x8cf2;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_DEPTH_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_DEPTH_TYPE = 0x8c16;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT31 = 0x8cff;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT30 = 0x8cfe;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT29 = 0x8cfd;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_QCOM_extended_get</code><br>Alias for: <code>GL_TEXTURE_INTERNAL_FORMAT_QCOM</code> - CType: int */
	  public static final int GL_TEXTURE_INTERNAL_FORMAT = 0x1003;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_discard_framebuffer</code><br>Alias for: <code>GL_COLOR_EXT</code> - CType: int */
	  public static final int GL_COLOR = 0x1800;
	  /** <code>GL_EXT_render_snorm</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RG8_SNORM = 0x8f95;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_GREEN_NV</code> - CType: int */
	  public static final int GL_GREEN = 0x1904;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8c76;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_internalformat_query</code> - CType: int */
	  public static final int GL_NUM_SAMPLE_COUNTS = 0x9380;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STATIC_COPY_ARB</code> - CType: int */
	  public static final int GL_STATIC_COPY = 0x88e6;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE = 0x92d8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_BUFFER_ACCESS_FLAGS = 0x911f;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTERS = 0x92d6;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB16UI_EXT</code> - CType: int */
	  public static final int GL_RGB16UI = 0x8d77;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR = 0x93d6;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR = 0x93d1;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_texture_cube_map_array</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code><br>Alias for: <code>GL_TEXTURE_BINDING_CUBE_MAP_ARRAY_OES</code>, <code>GL_TEXTURE_BINDING_CUBE_MAP_ARRAY_ARB</code>, <code>GL_TEXTURE_BINDING_CUBE_MAP_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 0x900a;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR = 0x93d3;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x8_KHR = 0x93ba;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 0x92cc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_VEC4_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_VEC4 = 0x8dc8;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_CUBE_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_CUBE = 0x905b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_VEC3_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_VEC3 = 0x8dc7;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_VEC2_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_VEC2 = 0x8dc6;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code>, <code>GL_ARB_copy_buffer</code>, <code>GL_NV_copy_buffer</code><br>Alias for: <code>GL_COPY_READ_BUFFER_NV</code> - CType: int */
	  public static final int GL_COPY_READ_BUFFER = 0x8f36;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_CUBE_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_CUBE = 0x9066;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA8_ETC2_EAC = 0x9278;
	  /** <code>GL_EXT_render_snorm</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_R8_SNORM = 0x8f94;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATOMIC_COUNTERS = 0x92d2;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90ce;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS_EXT</code>, <code>GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 0x92cf;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_READ_ONLY_ARB</code> - CType: int */
	  public static final int GL_READ_ONLY = 0x88b8;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x5_KHR = 0x93b8;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_OFFSET = 0x8a3b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_OES_texture_buffer</code>, <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_INT_SAMPLER_BUFFER_OES</code>, <code>GL_INT_SAMPLER_BUFFER_AMD</code>, <code>GL_INT_SAMPLER_BUFFER_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_BUFFER = 0x8dd0;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_copy_buffer</code> - CType: int */
	  public static final int GL_COPY_READ_BUFFER_BINDING = 0x8f36;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_5x5_KHR = 0x93b2;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD</code>, <code>GL_UNSIGNED_INT_SAMPLER_BUFFER_OES</code>, <code>GL_UNSIGNED_INT_SAMPLER_BUFFER_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 0x8dd8;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92db;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_shared_exponent</code><br>Alias for: <code>GL_TEXTURE_SHARED_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SHARED_SIZE = 0x8c3f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_shader</code><br>Alias for: <code>GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB</code> - CType: int */
	  public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8b4a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_BLUE_NV</code> - CType: int */
	  public static final int GL_BLUE = 0x1905;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8a41;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_BUFFER_MAP_LENGTH = 0x9120;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB32UI_EXT</code> - CType: int */
	  public static final int GL_RGB32UI = 0x8d71;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_CUBE_EXT</code> - CType: int */
	  public static final int GL_IMAGE_CUBE = 0x9050;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG_INTEGER = 0x8228;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_ARB_texture_buffer_object</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_EXT_texture_buffer_object</code>, <code>GL_OES_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_DATA_STORE_BINDING_ARB</code>, <code>GL_TEXTURE_BUFFER_DATA_STORE_BINDING_EXT</code>, <code>GL_TEXTURE_BUFFER_DATA_STORE_BINDING_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 0x8c2d;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_FRAMEBUFFER_LAYERS_EXT</code>, <code>GL_MAX_FRAMEBUFFER_LAYERS_OES</code> - CType: int */
	  public static final int GL_MAX_FRAMEBUFFER_LAYERS = 0x9317;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8a2b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_NV_sRGB_formats</code><br>Alias for: <code>GL_SRGB8_EXT</code>, <code>GL_SRGB8_NV</code> - CType: int */
	  public static final int GL_SRGB8 = 0x8c41;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PACK_SKIP_PIXELS = 0xd04;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB_INTEGER_EXT</code> - CType: int */
	  public static final int GL_RGB_INTEGER = 0x8d98;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT = 0x8218;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R16I = 0x8233;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ETC2 = 0x9275;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_GEOMETRY_SHADER_BIT_OES</code>, <code>GL_GEOMETRY_SHADER_BIT_EXT</code> - CType: int */
	  public static final int GL_GEOMETRY_SHADER_BIT = 0x4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_NV</code>, <code>GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8c8a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_ALPHA_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_ALPHA_SIZE = 0x805f;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_DYNAMIC_READ_ARB</code> - CType: int */
	  public static final int GL_DYNAMIC_READ = 0x88e9;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_NV</code>, <code>GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8c88;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_START = 0x8a29;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_texture_cube_map_array</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_cube_map_array</code><br>Alias for: <code>GL_INT_IMAGE_CUBE_MAP_ARRAY_OES</code>, <code>GL_INT_IMAGE_CUBE_MAP_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY = 0x905f;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 0x8a30;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 0x9310;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA16I_EXT</code> - CType: int */
	  public static final int GL_RGBA16I = 0x8d88;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_APPLE_texture_max_level</code>, <code>GL_SGIS_texture_lod</code><br>Alias for: <code>GL_TEXTURE_MAX_LEVEL_APPLE</code>, <code>GL_TEXTURE_MAX_LEVEL_SGIS</code> - CType: int */
	  public static final int GL_TEXTURE_MAX_LEVEL = 0x813d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_NV</code>, <code>GL_TRANSFORM_FEEDBACK_BUFFER_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8c8e;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_ATOMIC_COUNTERS = 0x92d7;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_TRIANGLE_STRIP_ADJACENCY_EXT = 0xd;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_LINE_STRIP_ADJACENCY_EXT = 0xb;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_BINDING = 0x8a3f;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_GREEN_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_GREEN_SIZE = 0x805d;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8a33;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_TYPE = 0x8a37;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_QCOM_extended_get</code><br>Alias for: <code>GL_TEXTURE_HEIGHT_QCOM</code> - CType: int */
	  public static final int GL_TEXTURE_HEIGHT = 0x1001;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_shared_exponent</code>, <code>GL_APPLE_texture_packed_float</code><br>Alias for: <code>GL_RGB9_E5_EXT</code>, <code>GL_RGB9_E5_APPLE</code> - CType: int */
	  public static final int GL_RGB9_E5 = 0x8c3d;
	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_EXT_debug_label</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_transform_feedback2</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_NV</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK = 0x8e22;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_BLUE_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_BLUE_TYPE = 0x8c12;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_NV_shadow_samplers_cube</code><br>Alias for: <code>GL_SAMPLER_CUBE_SHADOW_EXT</code>, <code>GL_SAMPLER_CUBE_SHADOW_NV</code> - CType: int */
	  public static final int GL_SAMPLER_CUBE_SHADOW = 0x8dc5;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_LINES_ADJACENCY_EXT = 0xa;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA8UI_EXT</code> - CType: int */
	  public static final int GL_RGBA8UI = 0x8d7c;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x1;
	  /** <code>GL_ARB_depth_buffer_float</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_depth_buffer_float</code><br>Alias for: <code>GL_DEPTH_COMPONENT32F_NV</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENT32F = 0x8cac;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_NV</code>, <code>GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8c8f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_R32I = 0x8235;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_LAYERED_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_LAYERED = 0x8f3c;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_4x4_KHR = 0x93b0;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR = 0x93db;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB8I_EXT</code> - CType: int */
	  public static final int GL_RGB8I = 0x8d8f;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS_EXT</code>, <code>GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 0x90cb;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG16I = 0x8239;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT = 0x8da7;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA32UI_EXT</code> - CType: int */
	  public static final int GL_RGBA32UI = 0x8d70;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG32UI = 0x823c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_fragment_shader</code><br>Alias for: <code>GL_MAX_FRAGMENT_UNIFORM_COMPONENTS_ARB</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8b49;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_NV_instanced_arrays</code>, <code>GL_ARB_instanced_arrays</code>, <code>GL_EXT_instanced_arrays</code>, <code>GL_ANGLE_instanced_arrays</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_DIVISOR_NV</code>, <code>GL_VERTEX_ATTRIB_ARRAY_DIVISOR_ARB</code>, <code>GL_VERTEX_ATTRIB_ARRAY_DIVISOR_EXT</code>, <code>GL_VERTEX_ATTRIB_ARRAY_DIVISOR_ANGLE</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88fe;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_ELEMENT_ARRAY_BARRIER_BIT_EXT</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_BARRIER_BIT = 0x2;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR = 0x93d5;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8d69;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_INTERLEAVED_ATTRIBS_NV</code>, <code>GL_INTERLEAVED_ATTRIBS_EXT</code> - CType: int */
	  public static final int GL_INTERLEAVED_ATTRIBS = 0x8c8c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_TEXTURE_BINDING_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_2D_ARRAY = 0x8c1d;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_QCOM_extended_get</code><br>Alias for: <code>GL_TEXTURE_WIDTH_QCOM</code> - CType: int */
	  public static final int GL_TEXTURE_WIDTH = 0x1000;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_sample_shading</code>, <code>GL_ARB_sample_shading</code><br>Alias for: <code>GL_SAMPLE_SHADING_OES</code>, <code>GL_SAMPLE_SHADING_ARB</code> - CType: int */
	  public static final int GL_SAMPLE_SHADING = 0x8c36;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_3D_EXT</code> - CType: int */
	  public static final int GL_IMAGE_3D = 0x904e;
	  /** <code>GL_ARB_provoking_vertex</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_provoking_vertex</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_LAST_VERTEX_CONVENTION_EXT</code>, <code>GL_LAST_VERTEX_CONVENTION_OES</code> - CType: int */
	  public static final int GL_LAST_VERTEX_CONVENTION = 0x8e4e;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_CONTEXT_FLAGS = 0x821e;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS_OES</code>, <code>GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 0x90cc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_NV_gpu_program4</code><br>Alias for: <code>GL_MIN_PROGRAM_TEXEL_OFFSET_EXT</code>, <code>GL_MIN_PROGRAM_TEXEL_OFFSET_NV</code> - CType: int */
	  public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_SGIS_texture_lod</code><br>Alias for: <code>GL_TEXTURE_MAX_LOD_SGIS</code> - CType: int */
	  public static final int GL_TEXTURE_MAX_LOD = 0x813b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code>, <code>GL_OES_texture_cube_map_array</code><br>Alias for: <code>GL_SAMPLER_CUBE_MAP_ARRAY_ARB</code>, <code>GL_SAMPLER_CUBE_MAP_ARRAY_EXT</code>, <code>GL_SAMPLER_CUBE_MAP_ARRAY_OES</code> - CType: int */
	  public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 0x900c;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_12x12_KHR = 0x93bd;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA32I_EXT</code> - CType: int */
	  public static final int GL_RGBA32I = 0x8d82;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG32I = 0x823b;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_6x6_KHR = 0x93b4;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 0x9316;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 0x92d1;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_NV</code>, <code>GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT</code> - CType: int */
	  public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8c8b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PACK_SKIP_ROWS = 0xd03;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_NUM_EXTENSIONS = 0x821d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code>, <code>GL_NV_vertex_program4</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_INTEGER_EXT</code>, <code>GL_VERTEX_ATTRIB_ARRAY_INTEGER_NV</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88fd;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR = 0x93da;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_CUBE_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8dd4;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_5x4_KHR = 0x93b1;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS_EXT</code>, <code>GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 0x92d4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_QUADS_OES</code>, <code>GL_QUADS_EXT</code> - CType: int */
	  public static final int GL_QUADS = 0x7;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_START = 0x92c2;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_discard_framebuffer</code><br>Alias for: <code>GL_DEPTH_EXT</code> - CType: int */
	  public static final int GL_DEPTH = 0x1801;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8a2d;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_OES_texture_buffer</code><br>Alias for: <code>GL_IMAGE_BUFFER_EXT</code>, <code>GL_IMAGE_BUFFER_OES</code> - CType: int */
	  public static final int GL_IMAGE_BUFFER = 0x9051;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_BUFFER_OES</code>, <code>GL_UNSIGNED_INT_IMAGE_BUFFER_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_BUFFER = 0x9067;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_READ_WRITE_ARB</code> - CType: int */
	  public static final int GL_READ_WRITE = 0x88ba;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code>, <code>GL_QCOM_extended_get</code><br>Alias for: <code>GL_TEXTURE_DEPTH_EXT</code>, <code>GL_TEXTURE_DEPTH_QCOM</code> - CType: int */
	  public static final int GL_TEXTURE_DEPTH = 0x8071;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS_OES</code>, <code>GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92ce;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_draw_range_elements</code><br>Alias for: <code>GL_MAX_ELEMENTS_INDICES_EXT</code> - CType: int */
	  public static final int GL_MAX_ELEMENTS_INDICES = 0x80e9;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_2D_EXT</code> - CType: int */
	  public static final int GL_IMAGE_2D = 0x904d;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_EVALUATION_SHADER_BIT_EXT</code>, <code>GL_TESS_EVALUATION_SHADER_BIT_OES</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_SHADER_BIT = 0x10;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_IMAGE_UNIFORMS_EXT</code>, <code>GL_MAX_GEOMETRY_IMAGE_UNIFORMS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 0x90cd;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code>, <code>GL_EXT_bindable_uniform</code><br>Alias for: <code>GL_UNIFORM_BUFFER_EXT</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER = 0x8a11;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_RED_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_RED_SIZE = 0x805c;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x6_KHR = 0x93b6;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB8UI_EXT</code> - CType: int */
	  public static final int GL_RGB8UI = 0x8d7d;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_IS_ROW_MAJOR = 0x8a3e;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_INDEX = 0x8a3a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_SGIS_texture_lod</code><br>Alias for: <code>GL_TEXTURE_BASE_LEVEL_SGIS</code> - CType: int */
	  public static final int GL_TEXTURE_BASE_LEVEL = 0x813c;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8a44;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_2D_ARRAY = 0x905e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGB16I_EXT</code> - CType: int */
	  public static final int GL_RGB16I = 0x8d89;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_R11_EAC = 0x9270;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_texture_lod_bias</code><br>Alias for: <code>GL_MAX_TEXTURE_LOD_BIAS_EXT</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_LOD_BIAS = 0x84fd;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_APPLE_texture_packed_float</code>, <code>GL_EXT_packed_float</code><br>Alias for: <code>GL_R11F_G11F_B10F_APPLE</code>, <code>GL_R11F_G11F_B10F_EXT</code> - CType: int */
	  public static final int GL_R11F_G11F_B10F = 0x8c3a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA8I_EXT</code> - CType: int */
	  public static final int GL_RGBA8I = 0x8d8e;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_shader_image_load_store</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_INT_IMAGE_BUFFER_OES</code>, <code>GL_INT_IMAGE_BUFFER_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_BUFFER = 0x905c;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_TEXTURE_COMPRESSED_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSED = 0x86a1;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS_EXT</code>, <code>GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 0x92cd;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_NV_conditional_render</code><br>Alias for: <code>GL_QUERY_NO_WAIT_NV</code> - CType: int */
	  public static final int GL_QUERY_NO_WAIT = 0x8e14;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_R11_EAC = 0x9271;
	  /** <code>GL_OES_draw_buffers_indexed</code>, <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_minmax</code><br>Alias for: <code>GL_MIN_EXT</code> - CType: int */
	  public static final int GL_MIN = 0x8007;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_ES3_2_compatibility</code>, <code>GL_EXT_primitive_bounding_box</code>, <code>GL_OES_primitive_bounding_box</code><br>Alias for: <code>GL_PRIMITIVE_BOUNDING_BOX_ARB</code>, <code>GL_PRIMITIVE_BOUNDING_BOX_EXT</code>, <code>GL_PRIMITIVE_BOUNDING_BOX_OES</code> - CType: int */
	  public static final int GL_PRIMITIVE_BOUNDING_BOX = 0x92be;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR = 0x93d7;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS_EXT</code>, <code>GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 0x8a32;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STREAM_READ_ARB</code> - CType: int */
	  public static final int GL_STREAM_READ = 0x88e1;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_IMAGE_UNIFORMS = 0x90ca;
	  /** <code>GL_ARB_provoking_vertex</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_provoking_vertex</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_FIRST_VERTEX_CONVENTION_OES</code>, <code>GL_FIRST_VERTEX_CONVENTION_EXT</code> - CType: int */
	  public static final int GL_FIRST_VERTEX_CONVENTION = 0x8e4d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_VARYINGS_NV</code>, <code>GL_TRANSFORM_FEEDBACK_VARYINGS_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8c83;
	  /** <code>GL_OES_draw_buffers_indexed</code>, <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_minmax</code><br>Alias for: <code>GL_MAX_EXT</code> - CType: int */
	  public static final int GL_MAX = 0x8008;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_ARB_texture_cube_map_array</code>, <code>GL_EXT_texture_cube_map_array</code>, <code>GL_OES_texture_cube_map_array</code><br>Alias for: <code>GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW_ARB</code>, <code>GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW_EXT</code>, <code>GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW_OES</code> - CType: int */
	  public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 0x900d;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_ARRAY_STRIDE = 0x8a3c;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT = 0x8da8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_transform_feedback</code>, <code>GL_NV_transform_feedback</code><br>Alias for: <code>GL_SEPARATE_ATTRIBS_EXT</code>, <code>GL_SEPARATE_ATTRIBS_NV</code> - CType: int */
	  public static final int GL_SEPARATE_ATTRIBS = 0x8c8d;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_1_4</code>, <code>GL_ARB_depth_texture</code><br>Alias for: <code>GL_TEXTURE_DEPTH_SIZE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_DEPTH_SIZE = 0x884a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG8UI = 0x8238;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_MAJOR_VERSION = 0x821b;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR = 0x93d8;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 0x9311;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_1</code>, <code>GL_OES_texture_buffer</code>, <code>GL_ARB_texture_buffer_object</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_EXT_texture_buffer_object</code><br>Alias for: <code>GL_MAX_TEXTURE_BUFFER_SIZE_OES</code>, <code>GL_MAX_TEXTURE_BUFFER_SIZE_ARB</code>, <code>GL_MAX_TEXTURE_BUFFER_SIZE_EXT</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 0x8c2b;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8a2f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT2x3_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT2x3 = 0x8b65;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_NV_non_square_matrices</code><br>Alias for: <code>GL_FLOAT_MAT2x4_NV</code> - CType: int */
	  public static final int GL_FLOAT_MAT2x4 = 0x8b66;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG16UI = 0x823a;
	  /** <code>GL_ARB_geometry_shader4</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_geometry_shader4</code><br>Alias for: <code>GL_MAX_VARYING_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_VARYING_COMPONENTS = 0x8b4b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_2D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_2D = 0x8dd2;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x10_KHR = 0x93bb;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_3D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_3D = 0x8dd3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_CUBE_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_CUBE = 0x8dcc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RGB8_SNORM = 0x8f96;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_6x5_KHR = 0x93b3;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_ATOMIC_COUNTERS_OES</code>, <code>GL_MAX_GEOMETRY_ATOMIC_COUNTERS_EXT</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 0x92d5;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_RG8I = 0x8237;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_multiview_draw_buffers</code>, <code>GL_NV_read_buffer</code><br>Alias for: <code>GL_READ_BUFFER_EXT</code>, <code>GL_READ_BUFFER_NV</code> - CType: int */
	  public static final int GL_READ_BUFFER = 0xc02;
	  /** <code>GL_ARB_stencil_texturing</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DEPTH_STENCIL_TEXTURE_MODE = 0x90ea;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_transform_feedback</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_NV_transform_feedback</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_PRIMITIVES_GENERATED_EXT</code>, <code>GL_PRIMITIVES_GENERATED_NV</code>, <code>GL_PRIMITIVES_GENERATED_OES</code> - CType: int */
	  public static final int GL_PRIMITIVES_GENERATED = 0x8c87;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 0x92d0;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code>, <code>GL_EXT_bindable_uniform</code><br>Alias for: <code>GL_UNIFORM_BUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_BINDING = 0x8a28;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92c0;
	  /** <code>GL_ARB_framebuffer_no_attachments</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;
	  /** <code>GL_ARB_depth_buffer_float</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_depth_buffer_float</code><br>Alias for: <code>GL_DEPTH32F_STENCIL8_NV</code> - CType: int */
	  public static final int GL_DEPTH32F_STENCIL8 = 0x8cad;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_BINDING_LEVEL_EXT</code> - CType: int */
	  public static final int GL_IMAGE_BINDING_LEVEL = 0x8f3b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_BUFFER_MAP_OFFSET = 0x9121;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92d9;
	  /** <code>GL_ARB_texture_rgb10_a2ui</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_RGB10_A2UI = 0x906f;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 0x92dc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_transform_feedback</code>, <code>GL_EXT_transform_feedback</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_START_NV</code>, <code>GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8c84;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_copy_buffer</code> - CType: int */
	  public static final int GL_COPY_WRITE_BUFFER_BINDING = 0x8f37;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_discard_framebuffer</code><br>Alias for: <code>GL_STENCIL_EXT</code> - CType: int */
	  public static final int GL_STENCIL = 0x1802;
	  /** <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR = 0x93dc;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92c1;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8a31;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8a2e;

}
