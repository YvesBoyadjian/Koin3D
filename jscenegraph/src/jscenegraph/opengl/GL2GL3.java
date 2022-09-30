/**
 * 
 */
package jscenegraph.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL2GL3  extends GL2ES3 {


	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY_ADDRESS_NV = 0x8f26;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_2D_RECT_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT = 0x8dd5;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_ADDRESS_NV = 0x8f28;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_Y_ARB = 0x9196;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_PACK_IMAGE_HEIGHT_EXT</code> - CType: int */
	  public static final int GL_PACK_IMAGE_HEIGHT = 0x806c;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_GREEN_INTEGER_EXT</code> - CType: int */
	  public static final int GL_GREEN_INTEGER = 0x8d95;
	  /** <code>GL_VERSION_4_0</code>, <code>GL_ARB_texture_cube_map_array</code><br>Alias for: <code>GL_PROXY_TEXTURE_CUBE_MAP_ARRAY_ARB</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 0x900b;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SRGB_ALPHA_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_ALPHA = 0x8c49;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_S3TC_DXT1_RGB = 0x82cc;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_GATHER = 0x82a2;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_10_10_10_2 = 0x82c3;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_TEXTURE = 0x829d;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BACK_LEFT = 0x402;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_POLYGON_OFFSET_POINT_NV</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_POINT = 0x2a01;
	  /** <code>GL_NV_shader_buffer_load</code> - CType: int */
	  public static final int GL_GPU_ADDRESS_NV = 0x8f34;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_SAMPLER_1D_ARRAY_SHADOW_EXT</code> - CType: int */
	  public static final int GL_SAMPLER_1D_ARRAY_SHADOW = 0x8dc3;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_TEXTURE_COMPRESSION_HINT_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSION_HINT = 0x84ef;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_RGTC1_RED = 0x82d0;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB = 0x919a;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_VIEW = 0x82b5;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_STENCIL_INDEX16_EXT</code> - CType: int */
	  public static final int GL_STENCIL_INDEX16 = 0x8d49;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_2D_MULTISAMPLE_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_2D_MULTISAMPLE = 0x9060;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENTS = 0x8284;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGB5_EXT</code> - CType: int */
	  public static final int GL_RGB5 = 0x8050;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGB4_EXT</code> - CType: int */
	  public static final int GL_RGB4 = 0x804f;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_RENDERABLE = 0x8289;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV = 0x8f2a;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_ARB_texture_rectangle</code>, <code>GL_NV_texture_rectangle</code><br>Alias for: <code>GL_TEXTURE_RECTANGLE_ARB</code>, <code>GL_TEXTURE_RECTANGLE_NV</code> - CType: int */
	  public static final int GL_TEXTURE_RECTANGLE = 0x84f5;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_API_ERROR_AMD = 0x9149;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_LOW_AMD = 0x9148;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_1D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_1D = 0x9062;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TESS_CONTROL_TEXTURE = 0x829c;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_READ_PIXELS = 0x828c;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_SMOOTH_HINT = 0xc53;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_1D_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_1D = 0x9057;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_POINT_NV</code> - CType: int */
	  public static final int GL_POINT = 0x1b00;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_INVOCATIONS_ARB = 0x82f0;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_LENGTH_NV = 0x8f2c;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_IMAGE_1D_ARRAY = 0x9052;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_COMPUTE_SHADER_INVOCATIONS_ARB = 0x82f5;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_POLYGON_OFFSET_LINE_NV</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_LINE = 0x2a02;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_STENCIL_TYPE = 0x827d;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_WIDTH = 0x827e;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_TESS_CONTROL_SHADER_PATCHES_ARB = 0x82f1;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_PRIMITIVES_SUBMITTED_ARB = 0x82ef;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_24_BITS = 0x82c9;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_texture_lod_bias</code><br>Alias for: <code>GL_TEXTURE_LOD_BIAS_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_LOD_BIAS = 0x8501;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_CLIPPING_OUTPUT_PRIMITIVES_ARB = 0x82f7;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_GREEN_SIZE = 0x8272;
	  /** <code>GL_AMD_stencil_operation_extended</code> - CType: int */
	  public static final int GL_SET_AMD = 0x874a;
	  /** <code>GL_AMD_sample_positions</code> - CType: int */
	  public static final int GL_SUBSAMPLE_DISTANCE_AMD = 0x883f;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_1D_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_1D = 0x8b5d;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_occlusion_query</code><br>Alias for: <code>GL_SAMPLES_PASSED_ARB</code> - CType: int */
	  public static final int GL_SAMPLES_PASSED = 0x8914;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE1_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE1 = 0x3001;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE0_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE0 = 0x3000;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE3_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE3 = 0x3003;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE2_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE2 = 0x3002;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE5_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE5 = 0x3005;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE4_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE4 = 0x3004;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE7_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE7 = 0x3007;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_CLIP_DISTANCE6_APPLE</code> - CType: int */
	  public static final int GL_CLIP_DISTANCE6 = 0x3006;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_HEIGHT = 0x827f;
	  /** <code>GL_ARB_texture_swizzle</code>, <code>GL_VERSION_3_3</code>, <code>GL_EXT_texture_swizzle</code><br>Alias for: <code>GL_TEXTURE_SWIZZLE_RGBA_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_RGBA = 0x8e46;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_ARB_color_buffer_float</code><br>Alias for: <code>GL_FIXED_ONLY_ARB</code> - CType: int */
	  public static final int GL_FIXED_ONLY = 0x891d;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_TEXTURE_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_1D_ARRAY = 0x8c18;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_SHADOW = 0x82a1;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_SPARSE_TEXTURE_FULL_ARRAY_CUBE_MIPMAPS_ARB = 0x91a9;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_SIZE_GRANULARITY = 0xb13;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT = 0x82b2;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE = 0x92c4;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_VERTICES_SUBMITTED_ARB = 0x82ee;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MIPMAP = 0x8293;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_1D_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_1D = 0x8dc9;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_UNPACK_COMPRESSED_BLOCK_DEPTH = 0x9129;
	  /** <code>GL_EXT_texture_sRGB_decode</code> - CType: int */
	  public static final int GL_TEXTURE_SRGB_DECODE_EXT = 0x8a48;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_S3TC_DXT3_RGBA = 0x82ce;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_1D_ARRAY = 0x9068;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_R3_G3_B2 = 0x2a10;
	  /** <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_UPPER_LEFT = 0x8ca2;
	  /** <code>GL_AMD_depth_clamp_separate</code> - CType: int */
	  public static final int GL_DEPTH_CLAMP_FAR_AMD = 0x901f;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_1D_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_1D = 0x8dd1;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_BGRA_INTEGER_EXT</code> - CType: int */
	  public static final int GL_BGRA_INTEGER = 0x8d9b;
	  /** <code>GL_ARB_sparse_buffer</code> - CType: int */
	  public static final int GL_SPARSE_STORAGE_BIT_ARB = 0x400;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_PACK_SKIP_IMAGES_EXT</code> - CType: int */
	  public static final int GL_PACK_SKIP_IMAGES = 0x806b;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_multiview_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER_EXT</code> - CType: int */
	  public static final int GL_DRAW_BUFFER = 0xc01;
	  /** <code>GL_ARB_texture_compression_rgtc</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_compression_rgtc</code><br>Alias for: <code>GL_COMPRESSED_RED_RGTC1_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_RED_RGTC1 = 0x8dbb;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_BLUE_INTEGER_EXT</code> - CType: int */
	  public static final int GL_BLUE_INTEGER = 0x8d96;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_SHARED_SIZE = 0x8277;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_GEOMETRY_TEXTURE = 0x829e;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_1_X_8 = 0x82c1;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_1D_EXT</code> - CType: int */
	  public static final int GL_IMAGE_1D = 0x904c;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DOUBLE = 0x140a;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 0x92c5;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_TEXTURE_BINDING_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_1D_ARRAY = 0x8c1c;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_SHADER_INVOCATIONS_ARB = 0x82f2;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD = 0x914a;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_DRAW_INDIRECT_ADDRESS_NV = 0x8f41;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8cdc;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_AMD = 0x9144;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_MEDIUM_AMD = 0x9147;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS_EXT</code> - CType: int */
	  public static final int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8f39;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_shader</code><br>Alias for: <code>GL_MAX_VARYING_FLOATS_ARB</code> - CType: int */
	  public static final int GL_MAX_VARYING_FLOATS = 0x8b4b;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_LOGGED_MESSAGES_AMD = 0x9145;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_RED_SIZE = 0x8271;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_PACK_COMPRESSED_BLOCK_HEIGHT = 0x912c;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 0x92cb;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_2D_RECT_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_2D_RECT = 0x8dcd;
	  /** <code>GL_EXT_texture_sRGB_decode</code> - CType: int */
	  public static final int GL_SKIP_DECODE_EXT = 0x8a4a;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_SAMPLER_BUFFER_AMD = 0x9001;
	  /** <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_LOWER_LEFT = 0x8ca1;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_PIXEL_TYPE = 0x82aa;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DEPTH_RENDERABLE = 0x8287;
	  /** <code>GL_AMD_stencil_operation_extended</code> - CType: int */
	  public static final int GL_STENCIL_BACK_OP_VALUE_AMD = 0x874d;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_UNPACK_COMPRESSED_BLOCK_WIDTH = 0x9127;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_BLUE_TYPE = 0x827a;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SRGB_WRITE = 0x8298;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_X_ARB = 0x9195;
	  /** <code>GL_AMD_depth_clamp_separate</code> - CType: int */
	  public static final int GL_DEPTH_CLAMP_NEAR_AMD = 0x901e;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_BPTC_UNORM = 0x82d2;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_CLIPPING_INPUT_PRIMITIVES_ARB = 0x82f6;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DOUBLEBUFFER = 0xc32;
	  /** <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_norm16</code><br>Alias for: <code>GL_R16_EXT</code> - CType: int */
	  public static final int GL_R16 = 0x822a;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SHADER_IMAGE_STORE = 0x82a5;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD = 0x9003;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_SAMPLER_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_SAMPLER_1D_ARRAY = 0x8dc0;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 0x82ac;
	  /** <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RG = 0x8226;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COLOR_ENCODING = 0x8296;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_CLEAR_BUFFER = 0x82b4;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_1D = 0xde0;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_DRAW_INDIRECT_LENGTH_NV = 0x8f42;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FILTER = 0x829a;
	  /** <code>GL_ARB_uniform_buffer_object</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 0x8a45;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_ADDRESS_NV = 0x8f25;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_COMPATIBILITY_CLASS = 0x82b6;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_2_X_16 = 0x82bd;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_INT_SAMPLER_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_SAMPLER_1D_ARRAY = 0x8dce;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_TEXTURE_SPARSE_ARB = 0x91a6;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_2_X_32 = 0x82ba;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_GET_TEXTURE_IMAGE_TYPE = 0x8292;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_2D_RECT_SHADOW_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_2D_RECT_SHADOW = 0x8b64;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_packed_pixels</code><br>Alias for: <code>GL_UNSIGNED_BYTE_3_3_2_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_BYTE_3_3_2 = 0x8032;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_11_11_10 = 0x82c2;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_DEPTH_TYPE = 0x827c;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_TESSELLATION_MODE_AMD = 0x9004;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_FRAGMENT_SHADER_INVOCATIONS_ARB = 0x82f4;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_1D_ARRAY = 0x905d;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_MAX_IMAGE_SAMPLES_EXT</code> - CType: int */
	  public static final int GL_MAX_IMAGE_SAMPLES = 0x906d;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_32_BITS = 0x82c8;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_ARB_color_buffer_float</code><br>Alias for: <code>GL_CLAMP_READ_COLOR_ARB</code> - CType: int */
	  public static final int GL_CLAMP_READ_COLOR = 0x891c;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_APPLICATION_AMD = 0x914f;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92c9;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_S3TC_DXT1_RGBA = 0x82cd;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_packed_pixels</code><br>Alias for: <code>GL_UNSIGNED_INT_8_8_8_8_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_8_8_8_8 = 0x8035;
	  /** <code>GL_VERSION_1_2</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 0x8367;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSED_BLOCK_SIZE = 0x82b3;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_NV_texture_rectangle</code>, <code>GL_ARB_texture_rectangle</code><br>Alias for: <code>GL_MAX_RECTANGLE_TEXTURE_SIZE_NV</code>, <code>GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB</code> - CType: int */
	  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE = 0x84f8;
	  /** <code>GL_ARB_texture_compression_rgtc</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RG_RGTC2 = 0x8dbd;
	  /** <code>GL_ARB_transform_feedback3</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8e70;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_STEREO = 0xc33;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_UNPACK_SWAP_BYTES = 0xcf0;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_INT_SAMPLER_BUFFER_AMD = 0x9002;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD = 0x914b;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_LENGTH_NV = 0x8f2e;
	  /** <code>GL_EXT_framebuffer_multisample_blit_scaled</code> - CType: int */
	  public static final int GL_SCALED_RESOLVE_FASTEST_EXT = 0x90ba;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_RGTC2_RG = 0x82d1;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_LINE_NV</code> - CType: int */
	  public static final int GL_LINE = 0x1b01;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_COMPATIBILITY_CLASS = 0x82a8;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MANUAL_GENERATE_MIPMAP = 0x8294;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_read_format_bgra</code>, <code>GL_IMG_read_format</code><br>Alias for: <code>GL_UNSIGNED_SHORT_4_4_4_4_REV_EXT</code>, <code>GL_UNSIGNED_SHORT_4_4_4_4_REV_IMG</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV = 0x8365;
	  /** <code>GL_NV_shader_buffer_load</code> - CType: int */
	  public static final int GL_BUFFER_GPU_ADDRESS_NV = 0x8f1d;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_NV_texture_rectangle</code>, <code>GL_ARB_texture_rectangle</code><br>Alias for: <code>GL_PROXY_TEXTURE_RECTANGLE_NV</code>, <code>GL_PROXY_TEXTURE_RECTANGLE_ARB</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_RECTANGLE = 0x84f7;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_DEPTH = 0x8280;
	  /** <code>GL_AMD_blend_minmax_factor</code> - CType: int */
	  public static final int GL_FACTOR_MAX_AMD = 0x901d;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_IMAGE_TYPE = 0x8290;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 0x82ae;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_DRAW_INDIRECT_UNIFIED_NV = 0x8f40;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_gpu_shader4</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 0x8dd6;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_ALPHA_SIZE = 0x8274;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_LENGTH_NV = 0x8f32;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_2_X_8 = 0x82c0;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_TEXEL_SIZE = 0x82a7;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_STENCIL_RENDERABLE = 0x8288;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 0x906b;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_GET_TEXTURE_IMAGE_FORMAT = 0x8291;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COLOR_COMPONENTS = 0x8283;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_BGR_INTEGER_EXT</code> - CType: int */
	  public static final int GL_BGR_INTEGER = 0x8d9a;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8cdb;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906c;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_LAYERS = 0x8281;
	  /** <code>GL_ARB_transform_feedback3</code>, <code>GL_VERSION_4_0</code>, <code>GL_ATI_vertex_streams</code><br>Alias for: <code>GL_MAX_VERTEX_STREAMS_ATI</code> - CType: int */
	  public static final int GL_MAX_VERTEX_STREAMS = 0x8e71;
	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_transform_feedback2</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8e24;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SRGB_READ = 0x8297;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_2D_MULTISAMPLE_ARRAY_EXT</code> - CType: int */
	  public static final int GL_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9056;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMBINED_DIMENSIONS = 0x8282;
	  /** <code>GL_VERSION_1_2</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_5_6_5_REV = 0x8364;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_RENDERABLE_LAYERED = 0x828a;
	  /** <code>GL_NV_shader_buffer_load</code> - CType: int */
	  public static final int GL_MAX_SHADER_BUFFER_ADDRESS_NV = 0x8f35;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code>, <code>GL_NV_vertex_program</code><br>Alias for: <code>GL_VERTEX_PROGRAM_POINT_SIZE_ARB</code>, <code>GL_VERTEX_PROGRAM_POINT_SIZE_NV</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642;
	  /** <code>GL_ARB_pipeline_statistics_query</code> - CType: int */
	  public static final int GL_GEOMETRY_SHADER_PRIMITIVES_EMITTED_ARB = 0x82f3;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_read_format_bgra</code><br>Alias for: <code>GL_UNSIGNED_SHORT_1_5_5_5_REV_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV = 0x8366;
	  /** <code>GL_AMD_stencil_operation_extended</code> - CType: int */
	  public static final int GL_STENCIL_OP_VALUE_AMD = 0x874c;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FRONT_RIGHT = 0x401;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_PROXY_TEXTURE_2D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_2D_ARRAY = 0x8c1b;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_GATHER_SHADOW = 0x82a3;
	  /** <code>GL_ARB_internalformat_query2</code><br>Alias for: <code>GL_SRGB_DECODE_ARB</code> - CType: int */
	  public static final int GL_SRGB_DECODE = 0x8299;
	  /** <code>GL_ARB_transform_feedback_overflow_query</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_STREAM_OVERFLOW_ARB = 0x82ed;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD = 0x914e;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_STENCIL_COMPONENTS = 0x8285;
	  /** <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_POINT_SPRITE_COORD_ORIGIN = 0x8ca0;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COLOR_RENDERABLE = 0x8286;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_ADDRESS_NV = 0x8f22;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_render_snorm</code><br>Alias for: <code>GL_RGBA16_SNORM_EXT</code> - CType: int */
	  public static final int GL_RGBA16_SNORM = 0x8f9b;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_POLYGON_MODE_NV</code> - CType: int */
	  public static final int GL_POLYGON_MODE = 0xb40;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_1D_SHADOW_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_1D_SHADOW = 0x8b61;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD = 0x914d;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_UNPACK_COMPRESSED_BLOCK_SIZE = 0x912a;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD = 0x914c;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FULL_SUPPORT = 0x82b7;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_ARB_texture_rectangle</code>, <code>GL_NV_texture_rectangle</code><br>Alias for: <code>GL_TEXTURE_BINDING_RECTANGLE_ARB</code>, <code>GL_TEXTURE_BINDING_RECTANGLE_NV</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_RECTANGLE = 0x84f6;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_128_BITS = 0x82c4;
	  /** <code>GL_EXT_texture_sRGB_decode</code> - CType: int */
	  public static final int GL_DECODE_EXT = 0x8a49;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGBA2_EXT</code> - CType: int */
	  public static final int GL_RGBA2 = 0x8055;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_UNPACK_COMPRESSED_BLOCK_HEIGHT = 0x9128;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_IMAGE_FORMAT = 0x828f;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COMPUTE_TEXTURE = 0x82a0;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_ADDRESS_NV = 0x8f29;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_APPLE_clip_distance</code><br>Alias for: <code>GL_MAX_CLIP_DISTANCES_APPLE</code> - CType: int */
	  public static final int GL_MAX_CLIP_DISTANCES = 0xd32;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_LENGTH_NV = 0x8f33;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_2D_MULTISAMPLE_EXT</code> - CType: int */
	  public static final int GL_IMAGE_2D_MULTISAMPLE = 0x9055;
	  /** <code>GL_EXT_x11_sync_object</code> - CType: int */
	  public static final int GL_SYNC_X11_FENCE_EXT = 0x90e1;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_NUM_SPARSE_LEVELS_ARB = 0x91aa;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_S3TC_DXT5_RGBA = 0x82cf;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_NUM_VIRTUAL_PAGE_SIZES_ARB = 0x91a8;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_8_BITS = 0x82cb;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PACK_LSB_FIRST = 0xd01;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_STENCIL_SIZE = 0x8276;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_2D_RECT_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_2D_RECT = 0x905a;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_TESSELLATION_FACTOR_AMD = 0x9005;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9061;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_SMOOTH = 0xb41;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_DISCRETE_AMD = 0x9006;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV = 0x8f20;
	  /** <code>GL_ARB_texture_compression_rgtc</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_compression_rgtc</code><br>Alias for: <code>GL_COMPRESSED_SIGNED_RED_RGTC1_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_RED_RGTC1 = 0x8dbc;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_LENGTH_NV = 0x8f2f;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_GREEN_TYPE = 0x8279;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_CAVEAT_SUPPORT = 0x82b8;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_64_BITS = 0x82c6;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_PROXY_TEXTURE_1D_ARRAY_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_1D_ARRAY = 0x8c19;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_map_buffer_alignment</code> - CType: int */
	  public static final int GL_MIN_MAP_BUFFER_ALIGNMENT = 0x90bc;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA = 0x84ee;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_polygon_mode</code><br>Alias for: <code>GL_FILL_NV</code> - CType: int */
	  public static final int GL_FILL = 0x1b02;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_1_X_32 = 0x82bb;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_1_X_16 = 0x82be;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV = 0x8f27;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_PROXY_TEXTURE_2D_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_2D = 0x8064;
	  /** <code>GL_AMD_blend_minmax_factor</code> - CType: int */
	  public static final int GL_FACTOR_MIN_AMD = 0x901c;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_4_X_16 = 0x82bc;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_RGB_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB = 0x84ed;
	  /** <code>GL_KHR_no_error</code> - CType: int */
	  public static final int GL_CONTEXT_FLAG_NO_ERROR_BIT_KHR = 0x8;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_PROXY_TEXTURE_3D_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_3D = 0x8070;
	  /** <code>GL_ARB_sparse_buffer</code> - CType: int */
	  public static final int GL_SPARSE_BUFFER_PAGE_SIZE_ARB = 0x82f8;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_SUPPORTED = 0x826f;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_LENGTH_NV = 0x8f31;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_PROXY_TEXTURE_1D_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_1D = 0x8063;
	  /** <code>GL_ARB_provoking_vertex</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_provoking_vertex</code><br>Alias for: <code>GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT</code> - CType: int */
	  public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 0x8e4c;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 0x86a0;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SRGB_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB = 0x8c48;
	  /** <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_RED = 0x8225;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_4_X_32 = 0x82b9;
	  /** <code>GL_ARB_texture_compression_rgtc</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_RG_RGTC2 = 0x8dbe;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_2D_RECT_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_2D_RECT = 0x8b63;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_BLUE_SIZE = 0x8273;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV = 0x8f1e;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_SIZE_RANGE = 0xb12;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_UNIFIED_NV = 0x8f1f;
	  /** <code>GL_ARB_seamless_cube_map</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 0x884f;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_PACK_COMPRESSED_BLOCK_SIZE = 0x912e;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_ARB = 0x9199;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_CATEGORY_OTHER_AMD = 0x9150;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 0x92c6;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_LENGTH_NV = 0x8f2b;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 0x92c8;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_AUTO_GENERATE_MIPMAP = 0x8295;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_IMAGE_2D_RECT_EXT</code> - CType: int */
	  public static final int GL_IMAGE_2D_RECT = 0x904f;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_NV_primitive_restart</code><br>Alias for: <code>GL_PRIMITIVE_RESTART_INDEX_NV</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART_INDEX = 0x8f9e;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92da;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_Z_ARB = 0x9197;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_PACK_COMPRESSED_BLOCK_DEPTH = 0x912d;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_96_BITS = 0x82c5;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_ADDRESS_NV = 0x8f24;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_PIXEL_FORMAT = 0x82a9;
	  /** <code>GL_VERSION_1_2</code> - CType: int */
	  public static final int GL_UNSIGNED_BYTE_2_3_3_REV = 0x8362;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_HIGH_AMD = 0x9146;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_LENGTH_NV = 0x8f2d;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 0x92ca;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82af;
	  /** <code>GL_VERSION_1_2</code> - CType: int */
	  public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY = 0xb13;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BACK_RIGHT = 0x403;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_DEPTH_SIZE = 0x8275;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_PREFERRED = 0x8270;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_READ_PIXELS_TYPE = 0x828e;
	  /** <code>GL_ARB_transform_feedback2</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_transform_feedback2</code><br>Alias for: <code>GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8e23;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VERTEX_TEXTURE = 0x829b;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_ADDRESS_NV = 0x8f21;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code><br>Alias for: <code>GL_PROXY_TEXTURE_CUBE_MAP_ARB</code>, <code>GL_PROXY_TEXTURE_CUBE_MAP_EXT</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_CUBE_MAP = 0x851b;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_render_snorm</code><br>Alias for: <code>GL_R16_SNORM_EXT</code> - CType: int */
	  public static final int GL_R16_SNORM = 0x8f98;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_NV_primitive_restart</code><br>Alias for: <code>GL_PRIMITIVE_RESTART_NV</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART = 0x8f9d;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PACK_SWAP_BYTES = 0xd00;
	  /** <code>GL_NV_texture_multisample</code> - CType: int */
	  public static final int GL_TEXTURE_COLOR_SAMPLES_NV = 0x9046;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_MAX_SPARSE_TEXTURE_SIZE_ARB = 0x9198;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_texture_norm16</code><br>Alias for: <code>GL_RGB16_SNORM_EXT</code> - CType: int */
	  public static final int GL_RGB16_SNORM = 0x8f9a;
	  /** <code>GL_ARB_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_INDEX_ARB = 0x91a7;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_BPTC_FLOAT = 0x82d3;
	  /** <code>GL_VERSION_1_2</code> - CType: int */
	  public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY = 0xb23;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY_LENGTH_NV = 0x8f30;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RIGHT = 0x407;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_WIDTH_GRANULARITY = 0xb23;
	  /** <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 0x1;
	  /** <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_norm16</code><br>Alias for: <code>GL_RG16_EXT</code> - CType: int */
	  public static final int GL_RG16 = 0x822c;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_16_BITS = 0x82ca;
	  /** <code>GL_ARB_transform_feedback_overflow_query</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_OVERFLOW_ARB = 0x82ec;
	  /** <code>GL_NV_vertex_buffer_unified_memory</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_ADDRESS_NV = 0x8f23;
	  /** <code>GL_ARB_shader_image_load_store</code>, <code>GL_VERSION_4_2</code>, <code>GL_EXT_shader_image_load_store</code><br>Alias for: <code>GL_UNSIGNED_INT_IMAGE_2D_RECT_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_IMAGE_2D_RECT = 0x9065;
	  /** <code>GL_VERSION_4_2</code>, <code>GL_ARB_compressed_texture_pixel_storage</code> - CType: int */
	  public static final int GL_PACK_COMPRESSED_BLOCK_WIDTH = 0x912b;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code>, <code>GL_EXT_texture_norm16</code><br>Alias for: <code>GL_RGB16_EXT</code> - CType: int */
	  public static final int GL_RGB16 = 0x8054;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SHADER_IMAGE_LOAD = 0x82a4;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGB12_EXT</code> - CType: int */
	  public static final int GL_RGB12 = 0x8053;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_BLEND = 0x828b;
	  /** <code>GL_ARB_shader_atomic_counters</code>, <code>GL_VERSION_4_2</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 0x92c7;
	  /** <code>GL_AMD_pinned_memory</code> - CType: int */
	  public static final int GL_EXTERNAL_VIRTUAL_MEMORY_BUFFER_AMD = 0x9160;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_1D = 0x8068;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_UNPACK_LSB_FIRST = 0xcf1;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LEFT = 0x406;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code>, <code>GL_EXT_texture_norm16</code><br>Alias for: <code>GL_RGBA16_EXT</code> - CType: int */
	  public static final int GL_RGBA16 = 0x805b;
	  /** <code>GL_NV_texture_multisample</code> - CType: int */
	  public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV = 0x9045;
	  /** <code>GL_EXT_framebuffer_multisample_blit_scaled</code> - CType: int */
	  public static final int GL_SCALED_RESOLVE_NICEST_EXT = 0x90bb;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VIEW_CLASS_48_BITS = 0x82c7;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGBA12_EXT</code> - CType: int */
	  public static final int GL_RGBA12 = 0x805a;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_FRAGMENT_TEXTURE = 0x829f;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SHADER_IMAGE_ATOMIC = 0x82a6;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_WIDTH_RANGE = 0xb22;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_IMAGE_CLASS_4_X_8 = 0x82bf;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_TEXTURE_COMPRESSED_BLOCK_WIDTH = 0x82b1;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_SRC1_ALPHA_EXT</code> - CType: int */
	  public static final int GL_SRC1_ALPHA = 0x8589;
	  /** <code>GL_AMD_vertex_shader_tesselator</code>, <code>GL_AMD_vertex_shader_tessellator</code> - CType: int */
	  public static final int GL_CONTINUOUS_AMD = 0x9007;
	  /** <code>GL_AMD_stencil_operation_extended</code> - CType: int */
	  public static final int GL_REPLACE_VALUE_AMD = 0x874b;
	  /** <code>GL_VERSION_3_1</code>, <code>GL_EXT_render_snorm</code><br>Alias for: <code>GL_RG16_SNORM_EXT</code> - CType: int */
	  public static final int GL_RG16_SNORM = 0x8f99;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FRONT_LEFT = 0x400;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 0x82ad;
	  /** <code>GL_ARB_provoking_vertex</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_provoking_vertex</code><br>Alias for: <code>GL_PROVOKING_VERTEX_EXT</code> - CType: int */
	  public static final int GL_PROVOKING_VERTEX = 0x8e4f;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_RED_TYPE = 0x8278;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_INTERNALFORMAT_ALPHA_TYPE = 0x827b;
	  /** <code>GL_ARB_internalformat_query2</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_READ_PIXELS_FORMAT = 0x828d;

}
