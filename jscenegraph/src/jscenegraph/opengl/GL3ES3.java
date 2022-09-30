/**
 * 
 */
package jscenegraph.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL3ES3 {


	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_GEOMETRY_INPUT_TYPE_EXT</code>, <code>GL_GEOMETRY_INPUT_TYPE_ARB</code> - CType: int */
	  public static final int GL_GEOMETRY_INPUT_TYPE = 0x8917;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_REFERENCED_BY_GEOMETRY_SHADER_EXT</code>, <code>GL_REFERENCED_BY_GEOMETRY_SHADER_OES</code> - CType: int */
	  public static final int GL_REFERENCED_BY_GEOMETRY_SHADER = 0x9309;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_REFERENCED_BY_TESS_CONTROL_SHADER_EXT</code>, <code>GL_REFERENCED_BY_TESS_CONTROL_SHADER_OES</code> - CType: int */
	  public static final int GL_REFERENCED_BY_TESS_CONTROL_SHADER = 0x9307;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_OVERLAY_KHR</code>, <code>GL_OVERLAY_NV</code> - CType: int */
	  public static final int GL_OVERLAY = 0x9296;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_TOP_LEVEL_ARRAY_SIZE = 0x930c;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_INPUT_COMPONENTS_EXT</code>, <code>GL_MAX_GEOMETRY_INPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 0x9123;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90d3;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_LINES_ADJACENCY_OES</code>, <code>GL_LINES_ADJACENCY_EXT</code>, <code>GL_LINES_ADJACENCY_ARB</code> - CType: int */
	  public static final int GL_LINES_ADJACENCY = 0xa;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_4x4_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_4x4 = 0x93b0;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_GPU_COMMANDS_COMPLETE_APPLE</code> - CType: int */
	  public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_INPUT_COMPONENTS_EXT</code>, <code>GL_MAX_TESS_CONTROL_INPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886c;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_FRACTIONAL_ODD_EXT</code>, <code>GL_FRACTIONAL_ODD_OES</code> - CType: int */
	  public static final int GL_FRACTIONAL_ODD = 0x8e7b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6 = 0x93d9;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5 = 0x93d8;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8 = 0x93da;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_HUE_NV</code>, <code>GL_HSL_HUE_KHR</code> - CType: int */
	  public static final int GL_HSL_HUE = 0x92ad;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_IMAGE_UNIFORMS = 0x91bd;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_LAYER_PROVOKING_VERTEX_EXT</code>, <code>GL_LAYER_PROVOKING_VERTEX_OES</code> - CType: int */
	  public static final int GL_LAYER_PROVOKING_VERTEX = 0x825e;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8f39;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_ACTIVE_RESOURCES = 0x92f5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_8x8_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x8 = 0x93b7;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_8x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x6 = 0x93b6;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_8x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_8x5 = 0x93b5;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_VARYING = 0x92f4;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_REFERENCED_BY_VERTEX_SHADER = 0x9306;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90d6;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5 = 0x93d5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6 = 0x93d6;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8 = 0x93d7;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS_OES</code>, <code>GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8e86;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_FRACTIONAL_EVEN_EXT</code>, <code>GL_FRACTIONAL_EVEN_OES</code> - CType: int */
	  public static final int GL_FRACTIONAL_EVEN = 0x8e7c;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_WORK_GROUP_SIZE = 0x91bf;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_MAX_NUM_ACTIVE_VARIABLES = 0x92f7;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_REFERENCED_BY_COMPUTE_SHADER = 0x930b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_EXT</code>, <code>GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB</code>, <code>GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8de1;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_ATOMIC_COUNTER_BUFFER_INDEX = 0x9301;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HARDLIGHT_NV</code>, <code>GL_HARDLIGHT_KHR</code> - CType: int */
	  public static final int GL_HARDLIGHT = 0x929b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_MAX_GEOMETRY_OUTPUT_VERTICES_OES</code>, <code>GL_MAX_GEOMETRY_OUTPUT_VERTICES_EXT</code>, <code>GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 0x8de0;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_0</code>, <code>GL_ARB_texture_gather</code>, <code>GL_NV_gpu_program5</code><br>Alias for: <code>GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_ARB</code>, <code>GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_NV</code> - CType: int */
	  public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8e5e;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4 = 0x93d0;
	  /** <code>GL_ARB_explicit_uniform_location</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_UNIFORM_LOCATIONS = 0x826e;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_UNIFORM_BLOCK = 0x92e2;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_STATUS_APPLE</code> - CType: int */
	  public static final int GL_SYNC_STATUS = 0x9114;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_OFFSET = 0x92fc;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_OES</code>, <code>GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_EXT</code>, <code>GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 0x8ddf;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_PATCH_VERTICES_OES</code>, <code>GL_MAX_PATCH_VERTICES_EXT</code> - CType: int */
	  public static final int GL_MAX_PATCH_VERTICES = 0x8e7d;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS_EXT</code>, <code>GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8e85;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90de;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_PROGRAM_OUTPUT = 0x92e4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_FLAGS_APPLE</code> - CType: int */
	  public static final int GL_SYNC_FLAGS = 0x9115;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_VERTEX_BINDING_DIVISOR = 0x82d6;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_UNSIGNALED_APPLE</code> - CType: int */
	  public static final int GL_UNSIGNALED = 0x9118;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90da;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BUFFER = 0x90d2;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS_EXT</code>, <code>GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8e83;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_LAYERED_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 0x8da7;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90db;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_TESS_GEN_VERTEX_ORDER_OES</code>, <code>GL_TESS_GEN_VERTEX_ORDER_EXT</code> - CType: int */
	  public static final int GL_TESS_GEN_VERTEX_ORDER = 0x8e78;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90df;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90dd;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_LINE_STRIP_ADJACENCY_OES</code>, <code>GL_LINE_STRIP_ADJACENCY_EXT</code>, <code>GL_LINE_STRIP_ADJACENCY_ARB</code> - CType: int */
	  public static final int GL_LINE_STRIP_ADJACENCY = 0xb;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_MATRIX_STRIDE = 0x92ff;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_ACTIVE_VARIABLES = 0x9305;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_SCREEN_KHR</code>, <code>GL_SCREEN_NV</code> - CType: int */
	  public static final int GL_SCREEN = 0x9295;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS_EXT</code>, <code>GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90d8;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS_EXT</code>, <code>GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8e80;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_gpu_shader5</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_gpu_program5</code>, <code>GL_OES_shader_multisample_interpolation</code><br>Alias for: <code>GL_MIN_FRAGMENT_INTERPOLATION_OFFSET_NV</code>, <code>GL_MIN_FRAGMENT_INTERPOLATION_OFFSET_OES</code> - CType: int */
	  public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 0x8e5b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_gpu_shader5</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_gpu_program5</code>, <code>GL_OES_shader_multisample_interpolation</code><br>Alias for: <code>GL_MAX_FRAGMENT_INTERPOLATION_OFFSET_NV</code>, <code>GL_MAX_FRAGMENT_INTERPOLATION_OFFSET_OES</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 0x8e5c;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91be;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_TOP_LEVEL_ARRAY_STRIDE = 0x930d;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91bc;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS_EXT</code>, <code>GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8e8a;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_10x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x6 = 0x93b9;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_10x8_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x8 = 0x93ba;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_10x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x5 = 0x93b8;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_PROGRAM_INPUT = 0x92e3;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10 = 0x93db;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 0x8da8;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_10x10_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_10x10 = 0x93bb;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_texture_buffer_range</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_OFFSET_OES</code>, <code>GL_TEXTURE_BUFFER_OFFSET_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_OFFSET = 0x919d;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_SATURATION_KHR</code>, <code>GL_HSL_SATURATION_NV</code> - CType: int */
	  public static final int GL_HSL_SATURATION = 0x92ae;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_GEN_LEVEL_EXT</code>, <code>GL_MAX_TESS_GEN_LEVEL_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_GEN_LEVEL = 0x8e7e;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82d9;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90dc;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 0x91bb;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_ARRAY_STRIDE = 0x92fe;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS_OES</code>, <code>GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8e89;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_TRIANGLES_ADJACENCY_EXT</code>, <code>GL_TRIANGLES_ADJACENCY_ARB</code>, <code>GL_TRIANGLES_ADJACENCY_OES</code> - CType: int */
	  public static final int GL_TRIANGLES_ADJACENCY = 0xc;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82d5;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_IS_ROW_MAJOR = 0x9300;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_TESS_CONTROL_OUTPUT_VERTICES_OES</code>, <code>GL_TESS_CONTROL_OUTPUT_VERTICES_EXT</code> - CType: int */
	  public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8e75;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS_OES</code>, <code>GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS_EXT</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90d7;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_0</code>, <code>GL_NV_gpu_program5</code>, <code>GL_ARB_texture_gather</code><br>Alias for: <code>GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_NV</code>, <code>GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_ARB</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8e5f;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_COLOR_NV</code>, <code>GL_HSL_COLOR_KHR</code> - CType: int */
	  public static final int GL_HSL_COLOR = 0x92af;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS_OES</code>, <code>GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90d9;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90ef;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_CONTROL_SHADER_EXT</code>, <code>GL_TESS_CONTROL_SHADER_OES</code> - CType: int */
	  public static final int GL_TESS_CONTROL_SHADER = 0x8e88;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_REFERENCED_BY_FRAGMENT_SHADER = 0x930a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_VERTEX_BINDING_STRIDE = 0x82d8;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COMPUTE_SHADER = 0x91b9;
	  /** <code>GL_VERSION_4_4</code>, <code>GL_ES_VERSION_3_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 0x82e5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_OUTPUT_COMPONENTS_EXT</code>, <code>GL_MAX_GEOMETRY_OUTPUT_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 0x9124;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_COLORBURN_NV</code>, <code>GL_COLORBURN_KHR</code> - CType: int */
	  public static final int GL_COLORBURN = 0x929a;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_6x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_6x6 = 0x93b4;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_6x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_6x5 = 0x93b3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5 = 0x93d3;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6 = 0x93d4;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_gpu_shader5</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_GEOMETRY_SHADER_INVOCATIONS_EXT</code>, <code>GL_GEOMETRY_SHADER_INVOCATIONS_OES</code> - CType: int */
	  public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 0x887f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_TIMEOUT_EXPIRED_APPLE</code> - CType: int */
	  public static final int GL_TIMEOUT_EXPIRED = 0x911b;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_GEN_SPACING_EXT</code>, <code>GL_TESS_GEN_SPACING_OES</code> - CType: int */
	  public static final int GL_TESS_GEN_SPACING = 0x8e77;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_BUFFER_VARIABLE = 0x92e5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_REFERENCED_BY_TESS_EVALUATION_SHADER_OES</code>, <code>GL_REFERENCED_BY_TESS_EVALUATION_SHADER_EXT</code> - CType: int */
	  public static final int GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BUFFER_SIZE = 0x90d5;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_VERTEX_BINDING_BUFFER = 0x8f4f;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_IS_PER_PATCH_OES</code>, <code>GL_IS_PER_PATCH_EXT</code> - CType: int */
	  public static final int GL_IS_PER_PATCH = 0x92e7;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 0x82da;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_ALREADY_SIGNALED_APPLE</code> - CType: int */
	  public static final int GL_ALREADY_SIGNALED = 0x911a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_MAX_NAME_LENGTH = 0x92f6;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS_OES</code>, <code>GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8e1f;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_UNIFORM = 0x92e1;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_OES_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT</code>, <code>GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB</code>, <code>GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_OES</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 0x8c29;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_gpu_shader5</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_shader_multisample_interpolation</code><br>Alias for: <code>GL_FRAGMENT_INTERPOLATION_OFFSET_BITS_OES</code> - CType: int */
	  public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 0x8e5d;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_texture_buffer_range</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT_OES</code>, <code>GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919f;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_BUFFER_BINDING = 0x9302;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_draw_indirect</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 0x8f43;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_GEOMETRY_SHADER_OES</code>, <code>GL_GEOMETRY_SHADER_EXT</code>, <code>GL_GEOMETRY_SHADER_ARB</code> - CType: int */
	  public static final int GL_GEOMETRY_SHADER = 0x8dd9;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_LUMINOSITY_KHR</code>, <code>GL_HSL_LUMINOSITY_NV</code> - CType: int */
	  public static final int GL_HSL_LUMINOSITY = 0x92b0;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_GEOMETRY_OUTPUT_TYPE_EXT</code>, <code>GL_GEOMETRY_OUTPUT_TYPE_ARB</code> - CType: int */
	  public static final int GL_GEOMETRY_OUTPUT_TYPE = 0x8918;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_OBJECT_TYPE_APPLE</code>, <code>GL_OBJECT_TYPE_ARB</code> - CType: int */
	  public static final int GL_OBJECT_TYPE = 0x9112;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_ARRAY_SIZE = 0x92fb;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_MULTIPLY_NV</code>, <code>GL_MULTIPLY_KHR</code> - CType: int */
	  public static final int GL_MULTIPLY = 0x9294;
	  /** <code>GL_ARB_viewport_array</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_UNDEFINED_VERTEX_OES</code>, <code>GL_UNDEFINED_VERTEX_EXT</code> - CType: int */
	  public static final int GL_UNDEFINED_VERTEX = 0x8260;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_COLORDODGE_NV</code>, <code>GL_COLORDODGE_KHR</code> - CType: int */
	  public static final int GL_COLORDODGE = 0x9299;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_ISOLINES_OES</code>, <code>GL_ISOLINES_EXT</code> - CType: int */
	  public static final int GL_ISOLINES = 0x8e7a;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_DARKEN_KHR</code>, <code>GL_DARKEN_NV</code> - CType: int */
	  public static final int GL_DARKEN = 0x9297;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_MAX_SERVER_WAIT_TIMEOUT_APPLE</code> - CType: int */
	  public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS_OES</code>, <code>GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886d;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_BUFFER_DATA_SIZE = 0x9303;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_NUM_ACTIVE_VARIABLES = 0x9304;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_geometry_shader</code>, <code>GL_ARB_geometry_shader4</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_TRIANGLE_STRIP_ADJACENCY_OES</code>, <code>GL_TRIANGLE_STRIP_ADJACENCY_ARB</code>, <code>GL_TRIANGLE_STRIP_ADJACENCY_EXT</code> - CType: int */
	  public static final int GL_TRIANGLE_STRIP_ADJACENCY = 0xd;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_GEN_MODE_EXT</code>, <code>GL_TESS_GEN_MODE_OES</code> - CType: int */
	  public static final int GL_TESS_GEN_MODE = 0x8e76;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_SOFTLIGHT_KHR</code>, <code>GL_SOFTLIGHT_NV</code> - CType: int */
	  public static final int GL_SOFTLIGHT = 0x929c;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS_OES</code>, <code>GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8e1e;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_TYPE = 0x92fa;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COMPUTE_SHADER_BIT = 0x20;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SIGNALED_APPLE</code> - CType: int */
	  public static final int GL_SIGNALED = 0x9119;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_NAME_LENGTH = 0x92f9;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_5x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_5x5 = 0x93b2;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_5x4_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_5x4 = 0x93b1;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_LIGHTEN_KHR</code>, <code>GL_LIGHTEN_NV</code> - CType: int */
	  public static final int GL_LIGHTEN = 0x9298;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_GEN_POINT_MODE_EXT</code>, <code>GL_TESS_GEN_POINT_MODE_OES</code> - CType: int */
	  public static final int GL_TESS_GEN_POINT_MODE = 0x8e79;
	  /** <code>GL_EXT_tessellation_shader</code>, <code>GL_VERSION_4_4</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED_OES</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED = 0x8221;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_FLUSH_COMMANDS_BIT_APPLE</code> - CType: int */
	  public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 0x1;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_WAIT_FAILED_APPLE</code> - CType: int */
	  public static final int GL_WAIT_FAILED = 0x911d;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_gpu_shader5</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_geometry_shader</code>, <code>GL_EXT_geometry_shader</code><br>Alias for: <code>GL_MAX_GEOMETRY_SHADER_INVOCATIONS_OES</code>, <code>GL_MAX_GEOMETRY_SHADER_INVOCATIONS_EXT</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 0x8e5a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_FENCE_APPLE</code> - CType: int */
	  public static final int GL_SYNC_FENCE = 0x9116;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code>, <code>GL_ARB_geometry_shader4</code><br>Alias for: <code>GL_GEOMETRY_VERTICES_OUT_EXT</code>, <code>GL_GEOMETRY_VERTICES_OUT_ARB</code> - CType: int */
	  public static final int GL_GEOMETRY_VERTICES_OUT = 0x8916;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_PATCHES_OES</code>, <code>GL_PATCHES_EXT</code> - CType: int */
	  public static final int GL_PATCHES = 0xe;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_VERTEX_BINDING_OFFSET = 0x82d7;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_SYNC_CONDITION_APPLE</code> - CType: int */
	  public static final int GL_SYNC_CONDITION = 0x9113;
	  /** <code>GL_ARB_vertex_type_2_10_10_10_rev</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_INT_2_10_10_10_REV = 0x8d9f;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12 = 0x93dd;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10 = 0x93dc;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_12x12_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_12x12 = 0x93bd;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 0x90eb;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_texture_buffer_range</code>, <code>GL_OES_texture_buffer</code>, <code>GL_EXT_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_SIZE_OES</code>, <code>GL_TEXTURE_BUFFER_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_SIZE = 0x919e;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_5</code>, <code>GL_KHR_robustness</code><br>Alias for: <code>GL_CONTEXT_LOST_KHR</code> - CType: int */
	  public static final int GL_CONTEXT_LOST = 0x507;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_RGBA_ASTC_12x10_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_ASTC_12x10 = 0x93bc;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS_EXT</code>, <code>GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8e7f;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BLOCK = 0x92e6;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5 = 0x93d2;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_texture_compression_astc_hdr</code>, <code>GL_KHR_texture_compression_astc_ldr</code><br>Alias for: <code>GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4 = 0x93d1;
	  /** <code>GL_ARB_compute_shader</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DISPATCH_INDIRECT_BUFFER = 0x90ee;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BUFFER_START = 0x90d4;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_EXCLUSION_NV</code>, <code>GL_EXCLUSION_KHR</code> - CType: int */
	  public static final int GL_EXCLUSION = 0x92a0;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS_OES</code>, <code>GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8e81;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_vertex_attrib_binding</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_BINDING = 0x82d4;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_PATCH_COMPONENTS_OES</code>, <code>GL_MAX_TESS_PATCH_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_MAX_TESS_PATCH_COMPONENTS = 0x8e84;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS_EXT</code>, <code>GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS_OES</code> - CType: int */
	  public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8e82;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_OES_texture_view</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_texture_view</code> - CType: int */
	  public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 0x82df;
	  /** <code>GL_VERSION_4_4</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_EXT_texture_buffer</code>, <code>GL_OES_texture_buffer</code><br>Alias for: <code>GL_TEXTURE_BUFFER_BINDING_EXT</code>, <code>GL_TEXTURE_BUFFER_BINDING_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_BINDING = 0x8c2a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_OES_tessellation_shader</code>, <code>GL_EXT_tessellation_shader</code><br>Alias for: <code>GL_PATCH_VERTICES_OES</code>, <code>GL_PATCH_VERTICES_EXT</code> - CType: int */
	  public static final int GL_PATCH_VERTICES = 0x8e72;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_DIFFERENCE_NV</code>, <code>GL_DIFFERENCE_KHR</code> - CType: int */
	  public static final int GL_DIFFERENCE = 0x929e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_2</code>, <code>GL_ARB_sync</code>, <code>GL_APPLE_sync</code><br>Alias for: <code>GL_CONDITION_SATISFIED_APPLE</code> - CType: int */
	  public static final int GL_CONDITION_SATISFIED = 0x911c;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_LOCATION = 0x930e;
	  /** <code>GL_ARB_sampler_objects</code>, <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code> - CType: int */
	  public static final int GL_SAMPLER_BINDING = 0x8919;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_draw_indirect</code>, <code>GL_VERSION_4_0</code> - CType: int */
	  public static final int GL_DRAW_INDIRECT_BUFFER = 0x8f3f;
	  /** <code>GL_ARB_tessellation_shader</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_0</code>, <code>GL_EXT_tessellation_shader</code>, <code>GL_OES_tessellation_shader</code><br>Alias for: <code>GL_TESS_EVALUATION_SHADER_EXT</code>, <code>GL_TESS_EVALUATION_SHADER_OES</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_SHADER = 0x8e87;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_shader_storage_buffer_object</code> - CType: int */
	  public static final int GL_SHADER_STORAGE_BARRIER_BIT = 0x2000;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_program_interface_query</code> - CType: int */
	  public static final int GL_BLOCK_INDEX = 0x92fd;

}
