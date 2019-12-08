/**
 * 
 */
package com.jogamp.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.jogamp.common.nio.Buffers;

import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.VoidPtr;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL2 extends GL2ES1, GL2GL3 {
	

	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 0x889d;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_ARB = 0x8e50;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY8UI_EXT</code> - CType: int */
	  public static final int GL_INTENSITY8UI = 0x8d7f;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_POINTER_ARB = 0x86ac;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_GEN_MODE_OES</code> - CType: int */
	  public static final int GL_TEXTURE_GEN_MODE = 0x2500;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_MAGNITUDE_BIAS_NV = 0x8718;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87cf;
	  /** <code>GL_NVX_gpu_memory_info</code> - CType: int */
	  public static final int GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX = 0x9049;
	  /** <code>GL_NV_tessellation_program5</code> - CType: int */
	  public static final int GL_TESS_CONTROL_PROGRAM_NV = 0x891e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_CLEAR_VALUE = 0xb80;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE = 0x8a06;
	  /** <code>GL_NV_multisample_filter_hint</code> - CType: int */
	  public static final int GL_MULTISAMPLE_FILTER_HINT_NV = 0x8534;
	  /** <code>GL_NV_tessellation_program5</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_PROGRAM_NV = 0x891f;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGBA_DXT5_S3TC = 0x83a4;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW8_ARB = 0x8728;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SOURCE0_RGB_EXT</code>, <code>GL_SOURCE0_RGB_ARB</code> - CType: int */
	  public static final int GL_SOURCE0_RGB = 0x8580;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA8I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA8I = 0x8d93;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 0x87f4;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_BLUE_MAX_CLAMP_INGR = 0x8566;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_RED_SCALE_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_RED_SCALE = 0x80b4;
	  /** <code>GL_NV_fill_rectangle</code> - CType: int */
	  public static final int GL_FILL_RECTANGLE_NV = 0x933c;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_ERROR_STRING_ARB = 0x8874;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC2_NV = 0x8fe9;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_INDEX_ARRAY_STRIDE_ARB = 0x8848;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA8UI_EXT</code> - CType: int */
	  public static final int GL_ALPHA8UI = 0x8d7e;
	  /** <code>GL_EXT_texture_mirror_clamp</code> - CType: int */
	  public static final int GL_MIRROR_CLAMP_EXT = 0x8742;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 0x88b4;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_RGBA_FLOAT32_APPLE = 0x8814;
	  /** <code>GL_NV_tessellation_program5</code> - CType: int */
	  public static final int GL_TESS_CONTROL_PROGRAM_PARAMETER_BUFFER_NV = 0x8c74;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_CUBIC_EXT = 0x8334;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_TYPE = 0x8454;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DURATION_RAW_INTEL = 0x94f2;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT_NV = 0x86f5;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX22_ARB = 0x88d6;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DEPENDENT_GB_TEXTURE_2D_NV = 0x86ea;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_NV_sRGB_formats</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_SLUMINANCE8_NV</code>, <code>GL_SLUMINANCE8_EXT</code> - CType: int */
	  public static final int GL_SLUMINANCE8 = 0x8c47;
	  /** <code>GL_NV_shader_thread_group</code> - CType: int */
	  public static final int GL_WARP_SIZE_NV = 0x9339;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_SINGLE_CONTEXT_INTEL = 0x0;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_PROGRAM_INVOCATIONS_NV = 0x8e5a;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_INTENSITY_SNORM = 0x9013;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB2_NV = 0x86c8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_A_TO_A = 0xc79;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_BLEND_COLOR_COMMAND_NV = 0xb;
	  /** <code>GL_NV_path_rendering_shared_edge</code> - CType: int */
	  public static final int GL_SHARED_EDGE_NV = 0xc0;
	  /** <code>GL_APPLE_flush_buffer_range</code><br>Alias for: <code>GL_BUFFER_SERIALIZED_MODIFY_APPLE</code> - CType: int */
	  public static final int GL_BUFFER_SERIALIZED_MODIFY = 0x8a12;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_RGB_FLOAT16_APPLE = 0x881b;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV = 0x903c;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_MAT4_ARB = 0x8b5c;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGBA16_NV = 0x888a;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEXEL_OFFSET_NV = 0x8905;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_EDGE_FLAG_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 0x808c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MOV_EXT = 0x8799;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DEPENDENT_HILO_TEXTURE_2D_NV = 0x8858;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_ALPHA_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_ALPHA_SIZE = 0x80dd;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD5_EXT = 0x87a2;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MUL_EXT = 0x8786;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NORMALIZED_RANGE_EXT = 0x87e0;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_SUBROUTINE_NUM_NV = 0x8f45;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA32UI_EXT</code> - CType: int */
	  public static final int GL_ALPHA32UI = 0x8d72;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD14_EXT = 0x87ab;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COLOR_MATERIAL_FACE = 0xb55;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_2D_NV = 0x86c0;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_VEC4_ARB = 0x8b52;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_EDGE_FLAG_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 0x889b;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_RECIP_SQRT_EXT = 0x8795;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV = 0x86f3;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_TERMINATE_SEQUENCE_COMMAND_NV = 0x0;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_DEPTH_REPLACE_NV = 0x86ed;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_PALETTE_ARB = 0x8840;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 0x8e53;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_SIZE_EXT</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE = 0x845a;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGBA8_NV = 0x86fc;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE = 0x8451;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIST_MODE = 0xb30;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGB_S3TC = 0x83a0;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX3_ARB = 0x88c3;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_2D_MATRIX_NV = 0x86e1;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_3D_ARB = 0x8b5f;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_WIDE_LINE_HINT_PGI = 0x1a222;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88af;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT8_VEC4_NV = 0x8fef;
	  /** <code>GL_NV_conservative_raster</code> - CType: int */
	  public static final int GL_CONSERVATIVE_RASTERIZATION_NV = 0x9346;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGB4_S3TC = 0x83a1;
	  /** <code>GL_APPLE_aux_depth_stencil</code> - CType: int */
	  public static final int GL_AUX_DEPTH_STENCIL_APPLE = 0x8a14;
	  /** <code>GL_ARB_shading_language_100</code> - CType: int */
	  public static final int GL_SHADING_LANGUAGE_VERSION_ARB = 0x8b8c;
	  /** <code>GL_NV_parameter_buffer_object</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 0x8da2;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x8808;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE_SNORM = 0x9011;
	  /** <code>GL_APPLE_texture_range</code> - CType: int */
	  public static final int GL_TEXTURE_STORAGE_HINT_APPLE = 0x85bc;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_INDEX_EXT = 0x8782;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_RESAMPLE_ZERO_FILL_OML = 0x8987;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_SAMPLE_POSITION_NV = 0x8e50;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_HILO_TEXTURE_2D_NV = 0x8854;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_COLOR_SUM_EXT</code>, <code>GL_COLOR_SUM_ARB</code> - CType: int */
	  public static final int GL_COLOR_SUM = 0x8458;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV = 0x9035;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX31_ARB = 0x88df;
	  /** <code>GL_NV_framebuffer_multisample_coverage</code> - CType: int */
	  public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV = 0x8cab;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER8_ATI = 0x882d;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_ARRAY_STRIDE = 0x8455;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_NV = 0x86ec;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_INTENSITY_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_INTENSITY_SIZE = 0x8061;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB13_NV = 0x86d3;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_SCALE_NV = 0x8853;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA32I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA32I = 0x8d87;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_INDEX_ARRAY_TYPE_ARB = 0x8847;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB5_NV = 0x86cb;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA32I_EXT</code> - CType: int */
	  public static final int GL_ALPHA32I = 0x8d84;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT_MAG_VIB_NV = 0x86f7;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_INTENSITY_NV = 0x8707;
	  /** <code>GL_EXT_texture_swizzle</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_A_EXT = 0x8e45;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_DOT4_EXT = 0x8785;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 0x862e;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_Z4Y12Z4CB12Z4CR12_444_NV = 0x9037;
	  /** <code>GL_ARB_texture_rectangle</code> - CType: int */
	  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB = 0x84f8;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT8_VEC4_NV = 0x8fe3;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_MIN_SPARSE_LEVEL_AMD = 0x919b;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGBA32_NV = 0x888b;
	  /** <code>GL_NV_geometry_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV = 0x8c27;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV = 0x864d;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX26_ARB = 0x88da;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_FORCE_BLUE_TO_ONE_NV = 0x8860;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_CONSERVE_MEMORY_HINT_PGI = 0x1a1fd;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 0x8623;
	  /** <code>GL_AMD_transform_feedback4</code> - CType: int */
	  public static final int GL_STREAM_RASTERIZATION_AMD = 0x91a0;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_CLAMP_EXT = 0x878e;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB = 0x88b7;
	  /** <code>GL_NV_transform_feedback2</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV = 0x8e23;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX17_ARB = 0x88d1;
	  /** <code>GL_APPLE_row_bytes</code> - CType: int */
	  public static final int GL_UNPACK_ROW_BYTES_APPLE = 0x8a16;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_PERFMON_RESULT_AMD = 0x8bc6;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE8UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE8UI = 0x8d80;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD1_EXT = 0x879e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ZOOM_X = 0xd16;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ZOOM_Y = 0xd17;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_UNPACK_RESAMPLE_OML = 0x8985;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_SOFTLIGHT_KHR</code>, <code>GL_SOFTLIGHT_NV</code> - CType: int */
	  public static final int GL_SOFTLIGHT = 0x929c;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_COLOR_CONVERSION_MATRIX_NV = 0x9029;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_FORMAT_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_FORMAT = 0x8017;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_STIPPLE_REPEAT = 0xb26;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_EXP_BASE_2_EXT = 0x8791;
	  /** <code>GL_EXT_direct_state_access</code> - CType: int */
	  public static final int GL_TRANSPOSE_PROGRAM_MATRIX_EXT = 0x8e2e;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_FOG_EXT = 0x87bd;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_LO_BIAS_NV = 0x8715;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_INFO_LOG_LENGTH_ARB = 0x8b84;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_TEXTURE_MATERIAL_PARAMETER_EXT = 0x8352;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SPHERE_MAP = 0x2402;
	  /** <code>GL_OVR_multiview</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_NUM_VIEWS_OVR = 0x9630;
	  /** <code>GL_NV_sRGB_formats</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3_NV</code>, <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3 = 0x8c4e;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW17_ARB = 0x8731;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE = 0x80d0;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_AMD = 0x8bc2;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_CURRENT_FOG_COORD = 0x8453;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE16_ALPHA16_SNORM = 0x901a;
	  /** <code>GL_NV_texgen_emboss</code> - CType: int */
	  public static final int GL_EMBOSS_MAP_NV = 0x855f;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_BOOL_VEC3_ARB = 0x8b58;
	  /** <code>GL_AMD_name_gen_delete</code> - CType: int */
	  public static final int GL_DATA_BUFFER_AMD = 0x9151;
	  /** <code>GL_EXT_compiled_vertex_array</code> - CType: int */
	  public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT = 0x81a8;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_COMPILE_STATUS_ARB = 0x8b81;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_NV_sRGB_formats</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_SLUMINANCE8_ALPHA8_NV</code>, <code>GL_SLUMINANCE8_ALPHA8_EXT</code> - CType: int */
	  public static final int GL_SLUMINANCE8_ALPHA8 = 0x8c45;
	  /** <code>GL_EXT_cull_vertex</code> - CType: int */
	  public static final int GL_CULL_VERTEX_EYE_POSITION_EXT = 0x81ab;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_C4F_N3F_V3F = 0x2a26;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_2D_RECT_SHADOW_ARB = 0x8b64;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD18_EXT = 0x87af;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX13_ARB = 0x88cd;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T2F_N3F_V3F = 0x2a2b;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88b0;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_fragment_program</code>, <code>GL_NV_fragment_program</code><br>Alias for: <code>GL_MAX_TEXTURE_COORDS_ARB</code>, <code>GL_MAX_TEXTURE_COORDS_NV</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_COORDS = 0x8871;
	  /** <code>GL_NV_framebuffer_multisample_coverage</code> - CType: int */
	  public static final int GL_MULTISAMPLE_COVERAGE_MODES_NV = 0x8e12;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_SUBPIXEL_BITS_ARB = 0x933d;
	  /** <code>GL_NV_occlusion_query</code> - CType: int */
	  public static final int GL_CURRENT_OCCLUSION_QUERY_ID_NV = 0x8865;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_CULL_FRAGMENT_NV = 0x86e7;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_WRITEMASK = 0xc21;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_VALIDATE_STATUS_ARB = 0x8b83;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_RASTER_FIXED_SAMPLE_LOCATIONS_EXT = 0x932a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MODE_BIT = 0x20;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_RASTER_SAMPLES_EXT = 0x9328;
	  /** <code>GL_ARB_texture_rectangle</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_RECTANGLE_ARB = 0x84f6;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV = 0x86d9;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x8809;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_PURGEABLE_APPLE = 0x8a1d;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ELEMENTS_COMMAND_NV = 0x2;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_STRICT_LIGHTING_HINT_PGI = 0x1a217;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV = 0x870d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_VERTEX_3 = 0xd97;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_VERTEX_4 = 0xd98;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_NATIVE_GRAPHICS_HANDLE_PGI = 0x1a202;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV = 0x887a;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_MAP_ATTRIB_U_ORDER_NV = 0x86c3;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_FLOAT16_VEC3_NV = 0x8ffa;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_LENGTH_ARB = 0x8627;
	  /** <code>GL_NV_parameter_buffer_object</code> - CType: int */
	  public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 0x8da3;
	  /** <code>GL_EXT_direct_state_access</code> - CType: int */
	  public static final int GL_PROGRAM_MATRIX_EXT = 0x8e2d;
	  /** <code>GL_NV_vdpau_interop</code> - CType: int */
	  public static final int GL_SURFACE_STATE_NV = 0x86eb;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DATA_BOOL32_INTEL = 0x94fc;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 0x87f1;
	  /** <code>GL_AMD_query_buffer_object</code> - CType: int */
	  public static final int GL_QUERY_BUFFER_BINDING_AMD = 0x9193;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DEPENDENT_AR_TEXTURE_2D_NV = 0x86e9;
	  /** <code>GL_ARB_shadow_ambient</code> - CType: int */
	  public static final int GL_TEXTURE_COMPARE_FAIL_VALUE_ARB = 0x80bf;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NEGATIVE_W_EXT = 0x87dc;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_COLOR_SUM_ARB = 0x8458;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_COLOR_CONVERSION_MAX_NV = 0x902a;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_SET_LT_EXT = 0x878d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_BLUE_BITS = 0xd5a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIST_INDEX = 0xb33;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_INDEX_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_STRIDE = 0x8086;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_GRID_DOMAIN = 0xdd0;
	  /** <code>GL_APPLE_texture_range</code> - CType: int */
	  public static final int GL_TEXTURE_RANGE_LENGTH_APPLE = 0x85b7;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_ARRAY_STRIDE_EXT = 0x87e6;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_INVARIANT_VALUE_EXT = 0x87ea;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT = 0x8da9;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MATRIX_EXT = 0x87c0;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET_NV = 0x8e5b;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_INDEX_BIT_PGI = 0x80000;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_NV_sRGB_formats</code><br>Alias for: <code>GL_SLUMINANCE_ALPHA_EXT</code>, <code>GL_SLUMINANCE_ALPHA_NV</code> - CType: int */
	  public static final int GL_SLUMINANCE_ALPHA = 0x8c44;
	  /** <code>GL_PGI_vertex_hints</code> - CType: long */
	  public static final long GL_TEXCOORD4_BIT_PGI = 0x80000000L;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_RGBA_INTEGER_MODE_EXT</code> - CType: int */
	  public static final int GL_RGBA_INTEGER_MODE = 0x8d9e;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW0_ARB = 0x1700;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_MATRIX_NV = 0x86e1;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_INTENSITY_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_INTENSITY = 0x84ec;
	  /** <code>GL_ARB_parallel_shader_compile</code> - CType: int */
	  public static final int GL_COMPLETION_STATUS_ARB = 0x91b1;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_NV = 0x8852;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB8_NV = 0x86ce;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_INTENSITY16_EXT</code> - CType: int */
	  public static final int GL_INTENSITY16 = 0x804d;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_INTENSITY12_EXT</code> - CType: int */
	  public static final int GL_INTENSITY12 = 0x804c;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_ATTRIBS_ARB = 0x88ac;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_REDUCE_EXT</code> - CType: int */
	  public static final int GL_REDUCE = 0x8016;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW13_ARB = 0x872d;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FRAGMENT_DEPTH_EXT</code> - CType: int */
	  public static final int GL_FRAGMENT_DEPTH = 0x8452;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE = 0x8a09;
	  /** <code>GL_NVX_gpu_memory_info</code> - CType: int */
	  public static final int GL_GPU_MEMORY_INFO_TOTAL_AVAILABLE_MEMORY_NVX = 0x9048;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_HI_SIZE_NV = 0x871b;
	  /** <code>GL_EXT_disjoint_timer_query</code>, <code>GL_EXT_timer_query</code> - CType: int */
	  public static final int GL_TIME_ELAPSED_EXT = 0x88bf;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_PROGRAM_OBJECT_ARB = 0x8b40;
	  /** <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_STORAGE_CLIENT_APPLE = 0x85b4;
	  /** <code>GL_EXT_422_pixels</code> - CType: int */
	  public static final int GL_422_REV_EXT = 0x80cd;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_STIPPLE = 0xb42;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SLUMINANCE_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SLUMINANCE = 0x8c4a;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SLUMINANCE_ALPHA_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SLUMINANCE_ALPHA = 0x8c4b;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_ALPHA_SIZE_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_ALPHA_SIZE = 0x802b;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW22_ARB = 0x8736;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE16_SNORM = 0x9019;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_MAG_SIZE_NV = 0x871f;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER12_ATI = 0x8831;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: int */
	  public static final int GL_QUERY_DEPTH_PASS_EVENT_BIT_AMD = 0x1;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8906;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_RED_BIAS_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_RED_BIAS = 0x80b8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COMPILE_AND_EXECUTE = 0x1301;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_MAX_RATIONAL_EVAL_ORDER_NV = 0x86d7;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE6_ALPHA2_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE6_ALPHA2 = 0x8044;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_FLOOR_EXT = 0x878f;
	  /** <code>GL_ARB_parallel_shader_compile</code> - CType: int */
	  public static final int GL_MAX_SHADER_COMPILER_THREADS_ARB = 0x91b0;
	  /** <code>GL_EXT_depth_bounds_test</code> - CType: int */
	  public static final int GL_DEPTH_BOUNDS_TEST_EXT = 0x8890;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX10_ARB = 0x88ca;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_ATTRIBS_ARB = 0x88ad;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_1D_SHADOW_ARB = 0x8b61;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_MULTIPLY_NV</code>, <code>GL_MULTIPLY_KHR</code> - CType: int */
	  public static final int GL_MULTIPLY = 0x9294;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_transpose_matrix</code><br>Alias for: <code>GL_TRANSPOSE_MODELVIEW_MATRIX_ARB</code> - CType: int */
	  public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 0x84e3;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER5_ATI = 0x882a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BLUE_BIAS = 0xd1b;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE32UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE32UI = 0x8d74;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUTO_NORMAL = 0xd80;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT16_VEC2_NV = 0x8fe5;
	  /** <code>GL_NV_geometry_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV = 0x8c28;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_separate_specular_color</code><br>Alias for: <code>GL_SINGLE_COLOR_EXT</code> - CType: int */
	  public static final int GL_SINGLE_COLOR = 0x81f9;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_ARB_color_buffer_float</code><br>Alias for: <code>GL_CLAMP_FRAGMENT_COLOR_ARB</code> - CType: int */
	  public static final int GL_CLAMP_FRAGMENT_COLOR = 0x891b;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_MULTISAMPLE_RASTERIZATION_ALLOWED_EXT = 0x932b;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_ALPHA8_SNORM = 0x9014;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_ALPHA_BIAS_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 0x80bb;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD20_EXT = 0x87b1;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER1_ATI = 0x8826;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_TRIANGULAR_2D_NV = 0x86c1;
	  /** <code>GL_NV_vdpau_interop</code> - CType: int */
	  public static final int GL_SURFACE_REGISTERED_NV = 0x86fd;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC3_ARB = 0x8fea;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAX_VERTEX_HINT_PGI = 0x1a22d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_R_TO_R = 0xc76;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_2D_NV = 0x86e8;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP1_APPLE = 0x8a00;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA_INTEGER_EXT</code> - CType: int */
	  public static final int GL_ALPHA_INTEGER = 0x8d97;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_FORMAT_ARB = 0x8876;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_ALPHA_NV = 0x8705;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88a6;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB = 0x8b88;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_RGBA_FLOAT16_ATI = 0x881a;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_TEXTURE_LUMINANCE_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_LUMINANCE_SIZE = 0x8060;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_STENCIL_REF_COMMAND_NV = 0xc;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX6_ARB = 0x88c6;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIST_BIT = 0x20000;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_CURRENT_WEIGHT_ARB = 0x86a8;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_ALPHA_MAX_CLAMP_INGR = 0x8567;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW4_ARB = 0x8724;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_FIELD_LOWER_NV = 0x9023;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD11_EXT = 0x87a8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_CLEAR_VALUE = 0xc20;
	  /** <code>GL_ARB_texture_filter_minmax</code> - CType: int */
	  public static final int GL_WEIGHTED_AVERAGE_ARB = 0x9367;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_TYPE_ARB = 0x86a9;
	  /** <code>GL_ARB_imaging</code>, <code>GL_HP_convolution_border_modes</code><br>Alias for: <code>GL_REPLICATE_BORDER_HP</code> - CType: int */
	  public static final int GL_REPLICATE_BORDER = 0x8153;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_ALPHA_FLOAT16_APPLE = 0x881c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD26_EXT = 0x87b7;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_MATRICES_ARB = 0x862f;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY32I_EXT</code> - CType: int */
	  public static final int GL_INTENSITY32I = 0x8d85;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_PROGRAMMABLE_SAMPLE_LOCATION_TABLE_SIZE_NV = 0x9340;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_LUMINANCE_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_LUMINANCE_TYPE = 0x8c14;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_BUFFER_NV = 0x9020;
	  /** <code>GL_NV_geometry_program4</code> - CType: int */
	  public static final int GL_GEOMETRY_PROGRAM_NV = 0x8c26;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88b3;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_SIZE_ARB = 0x86ab;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LOAD = 0x101;
	  /** <code>GL_NV_conservative_raster</code> - CType: int */
	  public static final int GL_SUBPIXEL_PRECISION_BIAS_Y_BITS_NV = 0x9348;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T2F_V3F = 0x2a27;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_CURRENT_VERTEX_WEIGHT_EXT = 0x850b;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_Y_AMD = 0x9196;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_ARRAY_POINTER_EXT = 0x87e9;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_BORDER = 0x1005;
	  /** <code>GL_NV_sRGB_formats</code>, <code>GL_EXT_texture_sRGB</code><br>Alias for: <code>GL_COMPRESSED_SRGB_S3TC_DXT1_NV</code>, <code>GL_COMPRESSED_SRGB_S3TC_DXT1_EXT</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_S3TC_DXT1 = 0x8c4c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT = 0x87cb;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_HILO16_NV = 0x86fa;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88a2;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT = 0x87ce;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALLOW_DRAW_MEM_HINT_PGI = 0x1a211;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_DIFFERENCE_NV</code>, <code>GL_DIFFERENCE_KHR</code> - CType: int */
	  public static final int GL_DIFFERENCE = 0x929e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 0xcb4;
	  /** <code>GL_KHR_debug</code>, <code>GL_VERSION_4_3</code> - CType: int */
	  public static final int GL_DISPLAY_LIST = 0x82e7;
	  /** <code>GL_NV_deep_texture3D</code> - CType: int */
	  public static final int GL_MAX_DEEP_3D_TEXTURE_WIDTH_HEIGHT_NV = 0x90d0;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT8_NV = 0x8fec;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_ALPHA16_EXT</code> - CType: int */
	  public static final int GL_ALPHA16 = 0x803e;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_OUTPUT_TYPE_EXT = 0x8ddc;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MIN_EXT = 0x878b;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_ALPHA12_EXT</code> - CType: int */
	  public static final int GL_ALPHA12 = 0x803d;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGBA_NV = 0x86fb;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD9_EXT = 0x87a6;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_LUMINANCE_FLOAT16_ATI = 0x881e;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE16_ALPHA16_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE16_ALPHA16 = 0x8048;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW26_ARB = 0x873a;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_WIDTH_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_WIDTH = 0x8018;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_ELEMENT_ADDRESS_COMMAND_NV = 0x8;
	  /** <code>GL_APPLE_ycbcr_422</code>, <code>GL_APPLE_rgb_422</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_8_8_REV_APPLE = 0x85bb;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_VERTICES_OUT_EXT = 0x8dda;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_VIEWPORT_COMMAND_NV = 0x10;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB = 0x8b87;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_MAG_FILTER_EXT = 0x8331;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_Z_EXT = 0x87d7;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT16_NV = 0x8fe4;
	  /** <code>GL_EXT_index_func</code> - CType: int */
	  public static final int GL_INDEX_TEST_FUNC_EXT = 0x81b6;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_EMISSION_BIT_PGI = 0x800000;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC2_ARB = 0x8ff5;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_SAMPLE_LOCATION_PIXEL_GRID_ARB = 0x9343;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_FLOAT16_ATI = 0x881f;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE = 0x8a02;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_PIXEL_GRID_HEIGHT_NV = 0x933f;
	  /** <code>GL_ATI_pixel_format_float</code> - CType: int */
	  public static final int GL_COLOR_CLEAR_UNCLAMPED_VALUE_ATI = 0x8835;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_BLUE_SCALE_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_BLUE_SCALE = 0x801e;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB0_NV = 0x86c6;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_DARKEN_KHR</code>, <code>GL_DARKEN_NV</code> - CType: int */
	  public static final int GL_DARKEN = 0x9297;
	  /** <code>GL_VERSION_2_1</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_SECONDARY_COLOR = 0x845f;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T4F_C4F_N3F_V4F = 0x2a2d;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_CURRENT_MATRIX_ARB = 0x8641;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_INTEGER_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_INTEGER = 0x8d9c;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_FLOAT16_APPLE = 0x881f;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV = 0x9025;
	  /** <code>GL_EXT_pixel_buffer_object</code> - CType: int */
	  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING_EXT = 0x88ef;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC3_ARB = 0x8ff6;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV = 0x8908;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD24_EXT = 0x87b5;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_ALPHA8_NV = 0x8706;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_FILTER_SCALE_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_FILTER_SCALE = 0x8014;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT16_VEC3_NV = 0x8ff2;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_PASS_THROUGH_NV = 0x885b;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_VIBRANCE_SCALE_NV = 0x8713;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_BINDING_EXT = 0x8def;
	  /** <code>GL_INTEL_map_texture</code> - CType: int */
	  public static final int GL_TEXTURE_MEMORY_LAYOUT_INTEL = 0x83ff;
	  /** <code>GL_NV_uniform_buffer_unified_memory</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_ADDRESS_NV = 0x936f;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_X_AMD = 0x9195;
	  /** <code>GL_AMD_query_buffer_object</code> - CType: int */
	  public static final int GL_QUERY_RESULT_NO_WAIT_AMD = 0x9194;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC4_NV = 0x8feb;
	  /** <code>GL_AMD_interleaved_elements</code> - CType: int */
	  public static final int GL_VERTEX_ID_SWIZZLE_AMD = 0x91a5;
	  /** <code>GL_OML_interlace</code> - CType: int */
	  public static final int GL_INTERLACE_OML = 0x8980;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_MAGNITUDE_SCALE_NV = 0x8712;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD31_EXT = 0x87bc;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_FLOAT32_APPLE = 0x8819;
	  /** <code>GL_APPLE_transform_hint</code> - CType: int */
	  public static final int GL_TRANSFORM_HINT_APPLE = 0x85b1;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_IUI_N3F_V2F_EXT = 0x81af;
	  /** <code>GL_NV_conservative_raster_dilate</code> - CType: int */
	  public static final int GL_CONSERVATIVE_RASTER_DILATE_GRANULARITY_NV = 0x937b;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX16_ARB = 0x88d0;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV = 0x887c;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_HINT_BIT = 0x8000;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SOURCE1_RGB_EXT</code>, <code>GL_SOURCE1_RGB_ARB</code> - CType: int */
	  public static final int GL_SOURCE1_RGB = 0x8581;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD15_EXT = 0x87ac;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T2F_C4F_N3F_V3F = 0x2a2c;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88aa;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_NV = 0x140e;
	  /** <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_INTENSITY16F_ARB</code> - CType: int */
	  public static final int GL_INTENSITY16F = 0x881d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RENDER = 0x1c00;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_CROSS_PRODUCT_EXT = 0x8797;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX4_ARB = 0x88c4;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGB32_NV = 0x8889;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_BUFFER_BINDING_NV = 0x9021;
	  /** <code>GL_APPLE_texture_range</code>, <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_STORAGE_SHARED_APPLE = 0x85bf;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_VERTEX23_BIT_PGI = 0x4;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_INTENSITY_FLOAT16_ATI = 0x881d;
	  /** <code>GL_APPLE_ycbcr_422</code> - CType: int */
	  public static final int GL_YCBCR_422_APPLE = 0x85b9;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RG32_NV = 0x8887;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_SUBTYPE_ARB = 0x8b4f;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_GEOMETRY_INPUT_TYPE_EXT = 0x8ddb;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SELECT = 0x1c02;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_ALPHA_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_ALPHA = 0x84e9;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_RGB_FLOAT32_ATI = 0x8815;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW29_ARB = 0x873d;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC4_NV = 0x8ff7;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_TEXTURE_APPLICATION_MODE_EXT = 0x834f;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_INVARIANT_EXT = 0x87c2;
	  /** <code>GL_EXT_cull_vertex</code> - CType: int */
	  public static final int GL_CULL_VERTEX_EXT = 0x81aa;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD2_EXT = 0x879f;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_PIXEL_GRID_HEIGHT_ARB = 0x933f;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_R16_NV = 0x8884;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_T2F_IUI_N3F_V3F_EXT = 0x81b4;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT8_MAG8_INTENSITY8_NV = 0x870b;
	  /** <code>GL_OML_subsample</code> - CType: int */
	  public static final int GL_FORMAT_SUBSAMPLE_24_24_OML = 0x8982;
	  /** <code>GL_NV_fog_distance</code> - CType: int */
	  public static final int GL_FOG_DISTANCE_MODE_NV = 0x855a;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_ARRAY_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_ARRAY = 0x8457;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_BOOL_ARB = 0x8b56;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_LOCAL_CONSTANT_DATATYPE_EXT = 0x87ed;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 0xcb6;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT_MAG_NV = 0x86f6;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_SUBROUTINE_PARAMETERS_NV = 0x8f44;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_RESAMPLE_AVERAGE_OML = 0x8988;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T2F_C4UB_V3F = 0x2a29;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT16_VEC3_NV = 0x8fe6;
	  /** <code>GL_APPLE_specular_vector</code> - CType: int */
	  public static final int GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE = 0x85b0;
	  /** <code>GL_AMD_interleaved_elements</code> - CType: int */
	  public static final int GL_VERTEX_ELEMENT_SWIZZLE_AMD = 0x91a4;
	  /** <code>GL_ARB_texture_rectangle</code> - CType: int */
	  public static final int GL_TEXTURE_RECTANGLE_ARB = 0x84f5;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX25_ARB = 0x88d9;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_MINMAX_FORMAT_EXT</code> - CType: int */
	  public static final int GL_MINMAX_FORMAT = 0x802f;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_BACK_NORMALS_HINT_PGI = 0x1a223;
	  /** <code>GL_EXT_abgr</code> - CType: int */
	  public static final int GL_ABGR_EXT = 0x8000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_NORMAL = 0xd92;
	  /** <code>GL_VERSION_1_1</code> - CType: long */
	  public static final long GL_ALL_CLIENT_ATTRIB_BITS = 0xffffffffL;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_FRAGMENT_COLOR_EXT = 0x834c;
	  /** <code>GL_NV_parameter_buffer_object</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 0x8da1;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88ae;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_FRAGMENT_NORMAL_EXT = 0x834a;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB15_NV = 0x86d5;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_YCBYCR8_422_NV = 0x9031;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_RED_MIN_CLAMP_INGR = 0x8560;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_FRAGMENT_PROGRAM_INTERPOLATION_OFFSET_BITS_NV = 0x8e5d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_EVAL_BIT = 0x10000;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_INT_VEC4_ARB = 0x8b55;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD19_EXT = 0x87b0;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_COVERAGE_MODULATION_NV = 0x9332;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_PARAMETERS_ARB = 0x88a8;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_RECTANGLE_NV = 0x864c;
	  /** <code>GL_NV_copy_depth_to_color</code> - CType: int */
	  public static final int GL_DEPTH_STENCIL_TO_BGRA_NV = 0x886f;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: int */
	  public static final int GL_QUERY_DEPTH_FAIL_EVENT_BIT_AMD = 0x2;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGB_UNSIGNED_ALPHA_NV = 0x870c;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 0x8872;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_1D_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_1D = 0x8010;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_COLOR0_EXT = 0x879b;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_ARRAY_TYPE_EXT = 0x87e7;
	  /** <code>GL_NV_deep_texture3D</code> - CType: int */
	  public static final int GL_MAX_DEEP_3D_TEXTURE_DEPTH_NV = 0x90d1;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT = 0x850d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SELECTION_BUFFER_SIZE = 0xdf4;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_NAME_STACK_DEPTH = 0xd37;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_BUFFER_PITCH_NV = 0x9028;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88ab;
	  /** <code>GL_EXT_texture_mirror_clamp</code> - CType: int */
	  public static final int GL_MIRROR_CLAMP_TO_BORDER_EXT = 0x8912;
	  /** <code>GL_NV_framebuffer_mixed_samples</code>, <code>GL_NV_multisample_coverage</code> - CType: int */
	  public static final int GL_COLOR_SAMPLES_NV = 0x8e20;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_ACTIVE_VERTEX_UNITS_ARB = 0x86a5;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX12_ARB = 0x88cc;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_RED_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_RED_SIZE = 0x80da;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT8_NV = 0x8fe0;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_NAME_LENGTH_MAX_INTEL = 0x94fe;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_THROUGHPUT_INTEL = 0x94f3;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_COLOR_MATRIX_SGI</code> - CType: int */
	  public static final int GL_COLOR_MATRIX = 0x80b1;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV = 0x9039;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_OBJECT_PLANE = 0x2501;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_ARRAY_POINTER = 0x8456;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TRANSFORM_BIT = 0x1000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SCISSOR_BIT = 0x80000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_TEXTURE_COORD_3 = 0xd95;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_TEXTURE_COORD_2 = 0xd94;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_TEXTURE_COORD_4 = 0xd96;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA_INTEGER_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_INTEGER = 0x8d9d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_TEXTURE_COORD_1 = 0xd93;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX0_ARB = 0x88c0;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_DT_SIZE_NV = 0x871e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 0xcb2;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA32UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA32UI = 0x8d75;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_NV = 0x8e50;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ALPHA_BIAS = 0xd1d;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV = 0x887d;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_NATIVE_GRAPHICS_BEGIN_HINT_PGI = 0x1a203;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87ca;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SHADER_OBJECT_ARB = 0x8b48;
	  /** <code>GL_EXT_cmyka</code> - CType: int */
	  public static final int GL_PACK_CMYK_HINT_EXT = 0x800e;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_BINDING_ARB = 0x8677;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_COLOR_NV</code>, <code>GL_HSL_COLOR_KHR</code> - CType: int */
	  public static final int GL_HSL_COLOR = 0x92af;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NEGATIVE_X_EXT = 0x87d9;
	  /** <code>GL_NV_transform_feedback2</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_NV = 0x8e22;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_PARAMETERS_ARB = 0x88a9;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV = 0x9034;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_SIGNED_HILO8_NV = 0x885f;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_OBJECT_LINEAR_NV</code> - CType: int */
	  public static final int GL_OBJECT_LINEAR = 0x2401;
	  /** <code>GL_EXT_polygon_offset_clamp</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_CLAMP_EXT = 0x8e1b;
	  /** <code>GL_NVX_gpu_memory_info</code> - CType: int */
	  public static final int GL_GPU_MEMORY_INFO_EVICTION_COUNT_NVX = 0x904a;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB3_NV = 0x86c9;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_RECLAIM_MEMORY_HINT_PGI = 0x1a1fe;
	  /** <code>GL_EXT_cmyka</code> - CType: int */
	  public static final int GL_CMYKA_EXT = 0x800d;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 0x8625;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX29_ARB = 0x88dd;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DS_SCALE_NV = 0x8710;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_SAMPLE_MASK_NV = 0x8e51;
	  /** <code>GL_NV_vdpau_interop</code> - CType: int */
	  public static final int GL_SURFACE_MAPPED_NV = 0x8700;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER9_ATI = 0x882e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_INDEX = 0xb01;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_COLOR_CONVERSION_OFFSET_NV = 0x902c;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 0xcb1;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_HILO_TEXTURE_RECTANGLE_NV = 0x8855;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_BOOL_VEC2_ARB = 0x8b57;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_2D_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_2D = 0x8011;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP_COLOR = 0xd10;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA16UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA16UI = 0x8d7b;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_GRID_DOMAIN = 0xdd2;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_LOCAL_EXT = 0x87c4;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_INTENSITY_FLOAT32_APPLE = 0x8817;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_INDEX_ARRAY_ARB = 0x8844;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGB16_NV = 0x8888;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_PROXY_POST_CONVOLUTION_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 0x80d4;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_C4UB_V2F = 0x2a22;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_ATTACHED_OBJECTS_ARB = 0x8b85;
	  /** <code>GL_EXT_index_material</code> - CType: int */
	  public static final int GL_INDEX_MATERIAL_PARAMETER_EXT = 0x81b9;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_MAP_ATTRIB_V_ORDER_NV = 0x86c4;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW31_ARB = 0x873f;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_T2F_IUI_V2F_EXT = 0x81b1;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW1_EXT = 0x850a;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE32I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE32I = 0x8d86;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_FORMAT_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_FORMAT = 0x8027;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FEEDBACK = 0x1c01;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_INDEX_ARRAY_SIZE_ARB = 0x8846;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 0xcb5;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RG16_NV = 0x8886;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE4_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE4 = 0x803f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUX_BUFFERS = 0xc00;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_PIXEL_GRID_WIDTH_ARB = 0x933e;
	  /** <code>GL_EXT_clip_volume_hint</code> - CType: int */
	  public static final int GL_CLIP_VOLUME_CLIPPING_HINT_EXT = 0x80f0;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_C4UB_V3F = 0x2a23;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGBA_S3TC = 0x83a2;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ELEMENTS_STRIP_COMMAND_NV = 0x4;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_VERTEX_BLEND_ARB = 0x86a7;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 0x889c;
	  /** <code>GL_OVR_multiview</code> - CType: int */
	  public static final int GL_MAX_VIEWS_OVR = 0x9631;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PASS_THROUGH_TOKEN = 0x700;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW16_ARB = 0x8730;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_POWER_EXT = 0x8793;
	  /** <code>GL_EXT_texture_snorm</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RGBA_SNORM = 0x8f93;
	  /** <code>GL_NV_texture_expand_normal</code> - CType: int */
	  public static final int GL_TEXTURE_UNSIGNED_REMAP_MODE_NV = 0x888f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_STIPPLE = 0xb24;
	  /** <code>GL_ARB_imaging</code>, <code>GL_HP_convolution_border_modes</code><br>Alias for: <code>GL_CONSTANT_BORDER_HP</code> - CType: int */
	  public static final int GL_CONSTANT_BORDER = 0x8151;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT16_NV = 0x8ff0;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_MINMAX_EXT</code> - CType: int */
	  public static final int GL_MINMAX = 0x802e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_VERTEX_4 = 0xdb8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_VERTEX_3 = 0xdb7;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_COLOR4_BIT_PGI = 0x20000;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY32UI_EXT</code> - CType: int */
	  public static final int GL_INTENSITY32UI = 0x8d73;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 0x80d5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_COLORDODGE_NV</code>, <code>GL_COLORDODGE_KHR</code> - CType: int */
	  public static final int GL_COLORDODGE = 0x9299;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_W_EXT = 0x87d8;
	  /** <code>GL_EXT_texture_compression_latc</code> - CType: int */
	  public static final int GL_COMPRESSED_LUMINANCE_LATC1_EXT = 0x8c70;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_TOKEN = 0x701;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_BIAS_NV = 0x86e3;
	  /** <code>GL_NV_fog_distance</code> - CType: int */
	  public static final int GL_EYE_RADIAL_NV = 0x855b;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_GREEN_BIAS_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_GREEN_BIAS = 0x8021;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD29_EXT = 0x87ba;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DEPENDENT_RGB_TEXTURE_3D_NV = 0x8859;
	  /** <code>GL_AMD_name_gen_delete</code> - CType: int */
	  public static final int GL_PERFORMANCE_MONITOR_AMD = 0x9152;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_DS_SIZE_NV = 0x871d;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW3_ARB = 0x8723;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DS_BIAS_NV = 0x8716;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALWAYS_SOFT_HINT_PGI = 0x1a20d;
	  /** <code>GL_APPLE_texture_range</code> - CType: int */
	  public static final int GL_TEXTURE_RANGE_POINTER_APPLE = 0x85b8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_PIXEL_MAP_TABLE = 0xd34;
	  /** <code>GL_EXT_texture_snorm</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RGB_SNORM = 0x8f92;
	  /** <code>GL_APPLE_texture_range</code>, <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_STORAGE_CACHED_APPLE = 0x85be;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_AVERAGE_EXT = 0x8335;
	  /** <code>GL_NV_uniform_buffer_unified_memory</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_UNIFIED_NV = 0x936e;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_NV_vertex_program</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_PROGRAM_TWO_SIDE_NV</code>, <code>GL_VERTEX_PROGRAM_TWO_SIDE_ARB</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_TWO_SIDE = 0x8643;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER6_ATI = 0x882b;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_QUERY_NAME_LENGTH_MAX_INTEL = 0x94fd;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_NV_sRGB_formats</code><br>Alias for: <code>GL_SLUMINANCE_EXT</code>, <code>GL_SLUMINANCE_NV</code> - CType: int */
	  public static final int GL_SLUMINANCE = 0x8c46;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_MAX_CONVOLUTION_HEIGHT_EXT</code> - CType: int */
	  public static final int GL_MAX_CONVOLUTION_HEIGHT = 0x801b;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_MAX_BINDABLE_UNIFORM_SIZE_EXT = 0x8ded;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_WIDTH_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_WIDTH = 0x8026;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_ALPHA_BITS = 0xd5b;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_RAW_INTEL = 0x94f4;
	  /** <code>GL_AMD_sparse_texture</code>, <code>GL_ARB_sparse_texture</code>, <code>GL_EXT_sparse_texture</code><br>Alias for: <code>GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB</code>, <code>GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_EXT</code> - CType: int */
	  public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 0x919a;
	  /** <code>GL_INTEL_map_texture</code> - CType: int */
	  public static final int GL_LAYOUT_LINEAR_CPU_CACHED_INTEL = 0x2;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW1_STACK_DEPTH_EXT = 0x8502;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_POSITION = 0xb07;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT8_VEC2_NV = 0x8fe1;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_TOKEN = 0x702;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_CURRENT_PALETTE_MATRIX_ARB = 0x8843;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_HUE_NV</code>, <code>GL_HSL_HUE_KHR</code> - CType: int */
	  public static final int GL_HSL_HUE = 0x92ad;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MATRIX_INDEX_ARRAY_POINTER_ARB = 0x8849;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RG_NV = 0x8881;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T2F_C3F_V3F = 0x2a2a;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_texture_lod_bias</code><br>Alias for: <code>GL_TEXTURE_FILTER_CONTROL_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_FILTER_CONTROL = 0x8500;
	  /** <code>GL_AMD_name_gen_delete</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_OBJECT_AMD = 0x9154;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_ALPHA4_EXT</code> - CType: int */
	  public static final int GL_ALPHA4 = 0x803b;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_FRAGMENT_PROGRAM_ARB = 0x8804;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_EDGE_FLAG_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY_POINTER = 0x8093;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DT_BIAS_NV = 0x8717;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 0xcb8;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW21_ARB = 0x8735;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_CUBIC_WEIGHT_EXT = 0x8333;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER13_ATI = 0x8832;
	  /** <code>GL_ARB_texture_buffer_object</code>, <code>GL_EXT_texture_buffer_object</code><br>Alias for: <code>GL_TEXTURE_BUFFER_FORMAT_ARB</code>, <code>GL_TEXTURE_BUFFER_FORMAT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BUFFER_FORMAT = 0x8c2e;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 0x8cd4;
	  /** <code>GL_EXT_stencil_clear_tag</code> - CType: int */
	  public static final int GL_STENCIL_CLEAR_TAG_VALUE_EXT = 0x88f3;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT = 0x8336;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_INDEX = 0xd91;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_SUCCESS_NV = 0x902f;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87cc;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COPY_PIXEL_TOKEN = 0x706;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD12_EXT = 0x87a9;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHT_ARRAY_EXT = 0x850c;
	  /** <code>GL_EXT_texture_compression_latc</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_LUMINANCE_ALPHA_LATC2_EXT = 0x8c73;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_FRONT_FACE_COMMAND_NV = 0x12;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_ALPHA_FLOAT32_ATI = 0x8816;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGBA4_DXT5_S3TC = 0x83a5;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_STRING_ARB = 0x8628;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_BIT = 0x1;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COEFF = 0xa00;
	  /** <code>GL_EXT_422_pixels</code> - CType: int */
	  public static final int GL_422_AVERAGE_EXT = 0x80ce;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_NUM_VIDEO_CAPTURE_STREAMS_NV = 0x9024;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB6_NV = 0x86cc;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV = 0x902d;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SOURCE0_ALPHA_EXT</code>, <code>GL_SOURCE0_ALPHA_ARB</code> - CType: int */
	  public static final int GL_SOURCE0_ALPHA = 0x8588;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALLOW_DRAW_WIN_HINT_PGI = 0x1a20f;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_SET_GE_EXT = 0x878c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MAX_EXT = 0x878a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_VIEWPORT_BIT = 0x800;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_env_combine</code>, <code>GL_EXT_texture_env_combine</code><br>Alias for: <code>GL_SOURCE2_ALPHA_ARB</code>, <code>GL_SOURCE2_ALPHA_EXT</code> - CType: int */
	  public static final int GL_SOURCE2_ALPHA = 0x858a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DOMAIN = 0xa02;
	  /** <code>GL_REND_screen_coordinates</code> - CType: int */
	  public static final int GL_SCREEN_COORDINATES_REND = 0x8490;
	  /** <code>GL_NV_fog_distance</code> - CType: int */
	  public static final int GL_EYE_PLANE_ABSOLUTE_NV = 0x855c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD6_EXT = 0x87a3;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_POINTER = 0x8456;
	  /** <code>GL_NV_occlusion_query</code> - CType: int */
	  public static final int GL_PIXEL_COUNT_AVAILABLE_NV = 0x8867;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER2_ATI = 0x8827;
	  /** <code>GL_EXT_cmyka</code> - CType: int */
	  public static final int GL_UNPACK_CMYK_HINT_EXT = 0x800f;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_C3F_V3F = 0x2a24;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_HILO_NV = 0x86f4;
	  /** <code>GL_EXT_stencil_two_side</code> - CType: int */
	  public static final int GL_STENCIL_TEST_TWO_SIDE_EXT = 0x8910;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FEEDBACK_BUFFER_TYPE = 0xdf2;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_PROGRAMMABLE_SAMPLE_LOCATION_ARB = 0x9341;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_BIAS_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_BIAS = 0x80d7;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_VERTEX_CONSISTENT_HINT_PGI = 0x1a22b;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW12_ARB = 0x872c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD21_EXT = 0x87b2;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RETURN = 0x102;
	  /** <code>GL_NV_light_max_exponent</code> - CType: int */
	  public static final int GL_MAX_SPOT_EXPONENT_NV = 0x8505;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_transpose_matrix</code><br>Alias for: <code>GL_TRANSPOSE_COLOR_MATRIX_ARB</code> - CType: int */
	  public static final int GL_TRANSPOSE_COLOR_MATRIX = 0x84e6;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_SCALAR_EXT = 0x87be;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE = 0x8a03;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW25_ARB = 0x8739;
	  /** <code>GL_EXT_provoking_vertex</code> - CType: int */
	  public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT = 0x8e4c;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET_NV = 0x8e5c;
	  /** <code>GL_EXT_422_pixels</code> - CType: int */
	  public static final int GL_422_EXT = 0x80cc;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_LUMINANCE_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_LUMINANCE = 0x84ea;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_CURRENT_FOG_COORDINATE_EXT</code> - CType: int */
	  public static final int GL_CURRENT_FOG_COORDINATE = 0x8453;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_WRITE_PIXEL_DATA_RANGE_NV = 0x8878;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD25_EXT = 0x87b6;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_LOCAL_CONSTANT_VALUE_EXT = 0x87ec;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_GRID_SEGMENTS = 0xdd1;
	  /** <code>GL_EXT_direct_state_access</code> - CType: int */
	  public static final int GL_PROGRAM_MATRIX_STACK_DEPTH_EXT = 0x8e2f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_Q = 0x2003;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_S = 0x2000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_R = 0x2002;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_T = 0x2001;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_PERCENTAGE_AMD = 0x8bc3;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_EDGE_FLAG_ARRAY_EXT</code> - CType: int */
	  public static final int GL_EDGE_FLAG_ARRAY = 0x8079;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_BIT = 0x80;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_NOP_COMMAND_NV = 0x1;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_IUI_N3F_V3F_EXT = 0x81b0;
	  /** <code>GL_NV_texture_shader2</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_TEXTURE_3D_NV = 0x86ef;
	  /** <code>GL_INGR_interlace_read</code> - CType: int */
	  public static final int GL_INTERLACE_READ_INGR = 0x8568;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_NATIVE_GRAPHICS_END_HINT_PGI = 0x1a204;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_MODE = 0xc30;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALLOW_DRAW_FRG_HINT_PGI = 0x1a210;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_AMBIENT_BIT_PGI = 0x100000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 0xcb9;
	  /** <code>GL_APPLE_flush_buffer_range</code><br>Alias for: <code>GL_BUFFER_FLUSHING_UNMAP_APPLE</code> - CType: int */
	  public static final int GL_BUFFER_FLUSHING_UNMAP = 0x8a13;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_STRICT_DEPTHFUNC_HINT_PGI = 0x1a216;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_GREEN_MIN_CLAMP_INGR = 0x8561;
	  /** <code>GL_NV_framebuffer_multisample_coverage</code> - CType: int */
	  public static final int GL_RENDERBUFFER_COLOR_SAMPLES_NV = 0x8e10;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_LUMINANCE_FLOAT32_APPLE = 0x8818;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_TEXTURE_STORAGE_SPARSE_BIT_AMD = 0x1;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC2_ARB = 0x8fe9;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_SHADOW_ATTENUATION_EXT = 0x834e;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 0x88b6;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB10_NV = 0x86d0;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW7_ARB = 0x8727;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: long */
	  public static final long GL_QUERY_ALL_EVENT_BITS_AMD = 0xffffffffL;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_INDEX_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_TYPE = 0x8085;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_PROGRAMMABLE_SAMPLE_LOCATIONS_NV = 0x9342;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGB8_NV = 0x86ff;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX7_ARB = 0x88c7;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_LO_SIZE_NV = 0x871c;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_LUMINANCE8_ALPHA8_NV = 0x8704;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RED_SCALE = 0xd14;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_VIBRANCE_BIAS_NV = 0x8719;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_GREEN_SCALE_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE = 0x80b5;
	  /** <code>GL_ATI_meminfo</code> - CType: int */
	  public static final int GL_RENDERBUFFER_FREE_MEMORY_ATI = 0x87fd;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD3_EXT = 0x87a0;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_RASTER_MULTISAMPLE_EXT = 0x9327;
	  /** <code>GL_SUNX_constant_data</code> - CType: int */
	  public static final int GL_TEXTURE_CONSTANT_DATA_SUNX = 0x81d6;
	  /** <code>GL_APPLE_client_storage</code> - CType: int */
	  public static final int GL_UNPACK_CLIENT_STORAGE_APPLE = 0x85b2;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_COLOR_FLOAT_APPLE = 0x8a0f;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_ARB_shadow</code><br>Alias for: <code>GL_COMPARE_R_TO_TEXTURE_ARB</code> - CType: int */
	  public static final int GL_COMPARE_R_TO_TEXTURE = 0x884e;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_LOCALS_EXT = 0x87d3;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_UNDEFINED_APPLE = 0x8a1c;
	  /** <code>GL_NV_occlusion_query</code> - CType: int */
	  public static final int GL_PIXEL_COUNTER_BITS_NV = 0x8864;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM = 0x8024;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_LOCALS_EXT = 0x87c9;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_BUFFER_BIT = 0x200;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_RGBA_FLOAT16_APPLE = 0x881a;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD16_EXT = 0x87ad;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX15_ARB = 0x88cf;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_LUMINANCE8_NV = 0x8702;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_HI_SCALE_NV = 0x870e;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_SAMPLE_MASK_VALUE_NV = 0x8e52;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_INTENSITY_EXT</code> - CType: int */
	  public static final int GL_INTENSITY = 0x8049;
	  /** <code>GL_EXT_index_func</code> - CType: int */
	  public static final int GL_INDEX_TEST_EXT = 0x81b5;
	  /** <code>GL_NV_vdpau_interop</code> - CType: int */
	  public static final int GL_WRITE_DISCARD_NV = 0x88be;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_CLEAR_COLOR_VALUE_NV = 0x888d;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX1_ARB = 0x88c1;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_GREEN_BIAS_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS = 0x80b9;
	  /** <code>GL_EXT_texture_swizzle</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_R_EXT = 0x8e42;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_1D_ARB = 0x8b5d;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_transpose_matrix</code><br>Alias for: <code>GL_TRANSPOSE_TEXTURE_MATRIX_ARB</code> - CType: int */
	  public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 0x84e5;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_RECTANGLE_NV = 0x8857;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_INDEX_ARRAY_EXT</code> - CType: int */
	  public static final int GL_INDEX_ARRAY = 0x8077;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_ALPHA_SCALE_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_ALPHA_SCALE = 0x801f;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_PROGRAM_RESULT_COMPONENTS_NV = 0x8907;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_NV = 0x8e5e;
	  /** <code>GL_EXT_texture_sRGB</code>, <code>GL_NV_sRGB_formats</code><br>Alias for: <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5_EXT</code>, <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5_NV</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5 = 0x8c4f;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_NORMAL_BIT_PGI = 0x8000000;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC4_ARB = 0x8ff7;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV = 0x9033;
	  /** <code>GL_NVX_gpu_memory_info</code> - CType: int */
	  public static final int GL_GPU_MEMORY_INFO_DEDICATED_VIDMEM_NVX = 0x9047;
	  /** <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_RANGE_APPLE = 0x851d;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_SATURATION_KHR</code>, <code>GL_HSL_SATURATION_NV</code> - CType: int */
	  public static final int GL_HSL_SATURATION = 0x92ae;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGB_NV = 0x8882;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA16I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA16I = 0x8d8d;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: int */
	  public static final int GL_QUERY_STENCIL_FAIL_EVENT_BIT_AMD = 0x4;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_NV = 0x140f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ORDER = 0xa01;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_2D_RECT_ARB = 0x8b63;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV = 0x887b;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX24_ARB = 0x88d8;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_CONST_EYE_NV = 0x86e5;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_INTENSITY8_EXT</code> - CType: int */
	  public static final int GL_INTENSITY8 = 0x804b;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_COORDS_ARB = 0x8871;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_INTENSITY4_EXT</code> - CType: int */
	  public static final int GL_INTENSITY4 = 0x804a;
	  /** <code>GL_EXT_shared_texture_palette</code> - CType: int */
	  public static final int GL_SHARED_TEXTURE_PALETTE_EXT = 0x81fb;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_STIPPLE_PATTERN = 0xb25;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_FULL_STIPPLE_HINT_PGI = 0x1a219;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE = 0x845b;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_COLOR3_BIT_PGI = 0x10000;
	  /** <code>GL_EXT_cmyka</code> - CType: int */
	  public static final int GL_CMYK_EXT = 0x800c;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_COVERAGE_MODULATION_TABLE_NV = 0x9331;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_PROXY_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_PROXY_COLOR_TABLE = 0x80d3;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x8805;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_INDEX_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 0x8899;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB9_NV = 0x86cf;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BITMAP = 0x1a00;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALWAYS_FAST_HINT_PGI = 0x1a20c;
	  /** <code>GL_NV_texgen_emboss</code> - CType: int */
	  public static final int GL_EMBOSS_LIGHT_NV = 0x855d;
	  /** <code>GL_APPLE_ycbcr_422</code>, <code>GL_APPLE_rgb_422</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_8_8_APPLE = 0x85ba;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_4D_COLOR_TEXTURE = 0x604;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_COLORBURN_NV</code>, <code>GL_COLORBURN_KHR</code> - CType: int */
	  public static final int GL_COLORBURN = 0x929a;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX11_ARB = 0x88cb;
	  /** <code>GL_NV_parameter_buffer_object</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 0x8da0;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_TEXTURE_FLOAT_COMPONENTS_NV = 0x888c;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 0x8624;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER = 0x845d;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE12_ALPHA12_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE12_ALPHA12 = 0x8047;
	  /** <code>GL_NV_vertex_program4</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER_NV = 0x88fd;
	  /** <code>GL_NV_conservative_raster</code> - CType: int */
	  public static final int GL_MAX_SUBPIXEL_PRECISION_BIAS_BITS_NV = 0x9349;
	  /** <code>GL_NV_transform_feedback2</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV = 0x8e24;
	  /** <code>GL_AMD_name_gen_delete</code> - CType: int */
	  public static final int GL_QUERY_OBJECT_AMD = 0x9153;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_ALPHA_FLOAT16_ATI = 0x881c;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 0xcb3;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_EYE_LINEAR_NV</code> - CType: int */
	  public static final int GL_EYE_LINEAR = 0x2400;
	  /** <code>GL_EXT_texture_sRGB</code>, <code>GL_NV_sRGB_formats</code><br>Alias for: <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1_EXT</code>, <code>GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1_NV</code> - CType: int */
	  public static final int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1 = 0x8c4d;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 0x8640;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_RGB_NV = 0x86fe;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 0xb51;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_8_8_S8_S8_REV_NV = 0x86db;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_VEC2_ARB = 0x8b50;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_HALF_APPLE = 0x140b;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT8_MAG8_NV = 0x870a;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE = 0x8a08;
	  /** <code>GL_EXT_texture_swizzle</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_RGBA_EXT = 0x8e46;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_RESAMPLE_REPLICATE_OML = 0x8986;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_EYE_PLANE = 0x2502;
	  /** <code>GL_NV_primitive_restart</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART_NV = 0x8558;
	  /** <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE = 0x851e;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_FRAGMENT_MATERIAL_EXT = 0x8349;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_MIN_FILTER_EXT = 0x8332;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT = 0x87cd;
	  /** <code>GL_EXT_compiled_vertex_array</code> - CType: int */
	  public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT = 0x81a9;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_OPTIMIZED_EXT = 0x87d4;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW1_MATRIX_EXT = 0x8506;
	  /** <code>GL_NV_occlusion_query</code> - CType: int */
	  public static final int GL_PIXEL_COUNT_NV = 0x8866;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_INT_VEC3_ARB = 0x8b54;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_T2F_IUI_N3F_V2F_EXT = 0x81b3;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_CURRENT_VERTEX_EXT = 0x87e2;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_SRC = 0x8450;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MULT = 0x103;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_DATATYPE_EXT = 0x87e5;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_PARTIAL_SUCCESS_NV = 0x902e;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_ALPHA_SCALE_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 0x80b7;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_transpose_matrix</code><br>Alias for: <code>GL_TRANSPOSE_PROJECTION_MATRIX_ARB</code> - CType: int */
	  public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 0x84e4;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87d2;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_ALPHA_REF_COMMAND_NV = 0xf;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB = 0x88a5;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA16UI_EXT</code> - CType: int */
	  public static final int GL_ALPHA16UI = 0x8d78;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGBA_MODE_NV = 0x888e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COLOR_MATERIAL_PARAMETER = 0xb56;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT = 0x8510;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_3_BYTES_NV</code> - CType: int */
	  public static final int GL_3_BYTES = 0x1408;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_OFFSET = 0xd13;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT8_NV = 0x8709;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_GREEN_SCALE_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_GREEN_SCALE = 0x801d;
	  /** <code>GL_AMD_name_gen_delete</code> - CType: int */
	  public static final int GL_SAMPLER_OBJECT_AMD = 0x9155;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX28_ARB = 0x88dc;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_2D_NV = 0x8856;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_SNORM = 0x9012;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD30_EXT = 0x87bb;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_GREEN_BITS = 0xd59;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_INT_SAMPLER_RENDERBUFFER_NV = 0x8e57;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_TEXTURE_LIGHT_EXT = 0x8350;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_MIXED_DEPTH_SAMPLES_SUPPORTED_NV = 0x932f;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_INTENSITY_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_INTENSITY_SIZE = 0x80df;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA16I_EXT</code> - CType: int */
	  public static final int GL_ALPHA16I = 0x8d8a;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_RELEASED_APPLE = 0x8a19;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_ATTRIBUTE_ADDRESS_COMMAND_NV = 0x9;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_STRIDE = 0x8455;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_2D_BIAS_NV = 0x86e3;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_S8_S8_8_8_NV = 0x86da;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_TEXTURE_INTENSITY_TYPE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_INTENSITY_TYPE = 0x8c15;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_INTENSITY16_SNORM = 0x901b;
	  /** <code>GL_NV_shader_thread_group</code> - CType: int */
	  public static final int GL_SM_COUNT_NV = 0x933b;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_EDGE_FLAG = 0xb43;
	  /** <code>GL_NV_fragment_coverage_to_color</code> - CType: int */
	  public static final int GL_FRAGMENT_COVERAGE_TO_COLOR_NV = 0x92dd;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88a7;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_VERTEX4_BIT_PGI = 0x8;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_VARIANTS_EXT = 0x87d0;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_ATTRIB_STACK_DEPTH = 0xd35;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_RGB_FLOAT32_APPLE = 0x8815;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NEGATIVE_Y_EXT = 0x87da;
	  /** <code>GL_EXT_texture_array</code><br>Alias for: <code>GL_COMPARE_REF_DEPTH_TO_TEXTURE_EXT</code> - CType: int */
	  public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE = 0x884e;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_COLOR_INDEXES_BIT_PGI = 0x1000000;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_COLOR1_EXT = 0x879c;
	  /** <code>GL_INTEL_map_texture</code> - CType: int */
	  public static final int GL_LAYOUT_LINEAR_INTEL = 0x1;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x880c;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_HARDLIGHT_NV</code>, <code>GL_HARDLIGHT_KHR</code> - CType: int */
	  public static final int GL_HARDLIGHT = 0x929b;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_COLOR_MATRIX_STACK_DEPTH_SGI</code> - CType: int */
	  public static final int GL_COLOR_MATRIX_STACK_DEPTH = 0x80b2;
	  /** <code>GL_VERSION_3_0</code>, <code>GL_ARB_color_buffer_float</code><br>Alias for: <code>GL_CLAMP_VERTEX_COLOR_ARB</code> - CType: int */
	  public static final int GL_CLAMP_VERTEX_COLOR = 0x891a;
	  /** <code>GL_NV_framebuffer_multisample_coverage</code> - CType: int */
	  public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV = 0x8e11;
	  /** <code>GL_NV_depth_clamp</code> - CType: int */
	  public static final int GL_DEPTH_CLAMP_NV = 0x864f;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_AFFINE_DEPTH_REPLACE_NV = 0x885d;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 0x8622;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88b1;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 0xcb7;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW19_ARB = 0x8733;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_CURRENT_SECONDARY_COLOR_EXT</code> - CType: int */
	  public static final int GL_CURRENT_SECONDARY_COLOR = 0x8459;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COMPILE = 0x1300;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_BIT = 0x4;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code>, <code>GL_OES_matrix_palette</code><br>Alias for: <code>GL_WEIGHT_ARRAY_BUFFER_BINDING_ARB</code>, <code>GL_WEIGHT_ARRAY_BUFFER_BINDING_OES</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 0x889e;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_LO_SCALE_NV = 0x870f;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_DONOT_FLUSH_INTEL = 0x83f9;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER14_ATI = 0x8833;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_TEXTURE_RENDERBUFFER_NV = 0x8e55;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW15_ARB = 0x872f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_GREEN_BIAS = 0xd19;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_PASS_THROUGH_NV = 0x86e6;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_COLOR_CONVERSION_MIN_NV = 0x902b;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_NEGATE_EXT = 0x8783;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_ZERO_EXT = 0x87dd;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_EXT = 0x8780;
	  /** <code>GL_APPLE_row_bytes</code> - CType: int */
	  public static final int GL_PACK_ROW_BYTES_APPLE = 0x8a15;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_RED_SCALE_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_RED_SCALE = 0x801c;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_GRID_SEGMENTS = 0xdd3;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_RESULT_COMPONENTS_NV = 0x8909;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_YCBAYCR8A_4224_NV = 0x9032;
	  /** <code>GL_EXT_packed_float</code><br>Alias for: <code>GL_RGBA_SIGNED_COMPONENTS_EXT</code> - CType: int */
	  public static final int GL_RGBA_SIGNED_COMPONENTS = 0x8c3c;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_TIMESTAMP_INTEL = 0x94f5;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI = 0x87f5;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW24_ARB = 0x8738;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_ARB = 0x140e;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: int */
	  public static final int GL_QUERY_DEPTH_BOUNDS_FAIL_EVENT_BIT_AMD = 0x8;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW30_ARB = 0x873e;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB1_NV = 0x86c7;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_INVARIANTS_EXT = 0x87c7;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 0x8875;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 0x2;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_2_BYTES_NV</code> - CType: int */
	  public static final int GL_2_BYTES = 0x1407;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW0_EXT = 0x1700;
	  /** <code>GL_NV_compute_program5</code> - CType: int */
	  public static final int GL_COMPUTE_PROGRAM_NV = 0x90fb;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_AMBIENT_AND_DIFFUSE_BIT_PGI = 0x200000;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_PREVIOUS_TEXTURE_INPUT_NV = 0x86e4;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT16_VEC2_NV = 0x8ff1;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_DELETE_STATUS_ARB = 0x8b80;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_STRICT_SCISSOR_HINT_PGI = 0x1a218;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_HEIGHT_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_HEIGHT = 0x8019;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_FULL_RANGE_EXT = 0x87e1;
	  /** <code>GL_NV_transform_feedback2</code> - CType: int */
	  public static final int GL_TRANSFORM_FEEDBACK_BINDING_NV = 0x8e25;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ELEMENTS_INSTANCED_COMMAND_NV = 0x6;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_CLIENT_PIXEL_STORE_BIT = 0x1;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MATERIAL_SIDE_HINT_PGI = 0x1a22c;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE16I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE16I = 0x8d8c;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_MINMAX_SINK_EXT</code> - CType: int */
	  public static final int GL_MINMAX_SINK = 0x8030;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_LIGHTEN_KHR</code>, <code>GL_LIGHTEN_NV</code> - CType: int */
	  public static final int GL_LIGHTEN = 0x9298;
	  /** <code>GL_ATI_meminfo</code> - CType: int */
	  public static final int GL_VBO_FREE_MEMORY_ATI = 0x87fb;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FEEDBACK_BUFFER_POINTER = 0xdf0;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_BLUE_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_BLUE_SIZE = 0x80dc;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER3_ATI = 0x8828;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI = 0x87f7;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RENDER_MODE = 0xc40;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_CLIP_FAR_HINT_PGI = 0x1a221;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_CUBE_ARB = 0x8b60;
	  /** <code>GL_EXT_texture_perturb_normal</code> - CType: int */
	  public static final int GL_PERTURB_EXT = 0x85ae;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_SUBPIXEL_BITS_NV = 0x933d;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_DISTANCE = 0xb09;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DRAW_PIXEL_TOKEN = 0x705;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DT_SCALE_NV = 0x8711;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV = 0x86f1;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CLAMP = 0x2900;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW2_ARB = 0x8722;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_STENCIL_SAMPLES_NV = 0x932e;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_PERFMON_RESULT_AVAILABLE_AMD = 0x8bc4;
	  /** <code>GL_ATI_meminfo</code> - CType: int */
	  public static final int GL_TEXTURE_FREE_MEMORY_ATI = 0x87fc;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD28_EXT = 0x87b9;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_GREEN_MAX_CLAMP_INGR = 0x8565;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX19_ARB = 0x88d3;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_VALUE_EXT = 0x87e4;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY = 0x8457;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_FIELD_UPPER_NV = 0x9022;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_COUNTER_RANGE_AMD = 0x8bc1;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY16UI_EXT</code> - CType: int */
	  public static final int GL_INTENSITY16UI = 0x8d79;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_MAX_CONVOLUTION_WIDTH_EXT</code> - CType: int */
	  public static final int GL_MAX_CONVOLUTION_WIDTH = 0x801a;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING = 0x889d;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DATA_UINT32_INTEL = 0x94f8;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_INDEX_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_INDEX_ARRAY_POINTER = 0x8091;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ENABLE_BIT = 0x2000;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC2_NV = 0x8ff5;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_ARB_depth_texture</code><br>Alias for: <code>GL_DEPTH_TEXTURE_MODE_ARB</code> - CType: int */
	  public static final int GL_DEPTH_TEXTURE_MODE = 0x884b;
	  /** <code>GL_APPLE_texture_range</code> - CType: int */
	  public static final int GL_STORAGE_PRIVATE_APPLE = 0x85bd;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT = 0x8de2;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_object</code><br>Alias for: <code>GL_TEXTURE_RESIDENT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_RESIDENT = 0x8067;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DATA_FLOAT_INTEL = 0x94fa;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_MAX_COLOR_MATRIX_STACK_DEPTH_SGI</code> - CType: int */
	  public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 0x80b3;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x8810;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW11_ARB = 0x872b;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_INDEX = 0xb61;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_LUMINANCE_SIZE_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_LUMINANCE_SIZE = 0x802c;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER10_ATI = 0x882f;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB = 0x880d;
	  /** <code>GL_OML_subsample</code> - CType: int */
	  public static final int GL_FORMAT_SUBSAMPLE_244_244_OML = 0x8983;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DEPTH_BIAS = 0xd1f;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_FRAME_WIDTH_NV = 0x9038;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT8_VEC2_NV = 0x8fed;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_INTENSITY8_NV = 0x8708;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD7_EXT = 0x87a4;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_SINK_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_SINK = 0x802d;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ARRAYS_STRIP_COMMAND_NV = 0x5;
	  /** <code>GL_EXT_stencil_clear_tag</code> - CType: int */
	  public static final int GL_STENCIL_TAG_BITS_EXT = 0x88f2;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_BLUE_BIAS_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS = 0x80ba;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB12_NV = 0x86d2;
	  /** <code>GL_NV_gpu_program5</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_NV = 0x8e5f;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_ADD_EXT = 0x8787;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MIN_PROGRAM_TEXEL_OFFSET_NV = 0x8904;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_R_NV = 0x8880;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_matrix</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_BLUE_SCALE_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE = 0x80b6;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE8I_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE8I = 0x8d92;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_BUFFER_OBJECT_APPLE = 0x85b3;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_MIXED_STENCIL_SAMPLES_SUPPORTED_NV = 0x9330;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW28_ARB = 0x873c;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV = 0x86f0;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIST_BASE = 0xb32;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_NAME_STACK_DEPTH = 0xd70;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_SCALE_NV = 0x86e2;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_G_TO_G = 0xc77;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code> - CType: int */
	  public static final int GL_INDEX = 0x8222;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_LUMINANCE_FLOAT16_APPLE = 0x881e;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_SUM_UNITY_ARB = 0x86a6;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_RED_MAX_CLAMP_INGR = 0x8564;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_T2F_IUI_V3F_EXT = 0x81b2;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_FAILURE_NV = 0x9030;
	  /** <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_STORAGE_HINT_APPLE = 0x851f;
	  /** <code>GL_VERSION_1_1</code> - CType: long */
	  public static final long GL_CLIENT_ALL_ATTRIB_BITS = 0xffffffffL;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_FORMAT_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_FORMAT = 0x80d8;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_MAP_TESSELLATION_NV = 0x86c2;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_LOG_BASE_2_EXT = 0x8792;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_LUMINANCE_ALPHA_NV = 0x8703;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 0x8e54;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_BORDER_VALUES_NV = 0x871a;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX21_ARB = 0x88d5;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_X_EXT = 0x87d5;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_COLOR = 0xb04;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV = 0x9036;
	  /** <code>GL_AMD_debug_output</code> - CType: int */
	  public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_AMD = 0x9143;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW0_MATRIX_EXT = 0xba6;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM = 0x100;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX8_ARB = 0x88c8;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_LOCAL_CONSTANT_EXT = 0x87c3;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_GEN_Q = 0xc63;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_GEN_R = 0xc62;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE12_ALPHA4_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE12_ALPHA4 = 0x8046;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_GEN_S = 0xc60;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_GEN_T = 0xc61;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_MAX_MAP_TESSELLATION_NV = 0x86d6;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD22_EXT = 0x87b3;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DATA_UINT64_INTEL = 0x94f9;
	  /** <code>GL_NV_compute_program5</code> - CType: int */
	  public static final int GL_COMPUTE_PROGRAM_PARAMETER_BUFFER_NV = 0x90fc;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_PROGRAMMABLE_SAMPLE_LOCATION_NV = 0x9341;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_TEXTURE_2D_NV = 0x86ee;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_MAT2_ARB = 0x8b5a;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB4_NV = 0x86ca;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x8806;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_LUMINANCE_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_LUMINANCE_SIZE = 0x80de;
	  /** <code>GL_EXT_texture_swizzle</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_G_EXT = 0x8e43;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW6_ARB = 0x8726;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_MAX_SAMPLE_MASK_WORDS_NV = 0x8e59;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_FLOAT16_VEC4_NV = 0x8ffb;
	  /** <code>GL_EXT_index_material</code> - CType: int */
	  public static final int GL_INDEX_MATERIAL_FACE_EXT = 0x81ba;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI = 0x87f3;
	  /** <code>GL_AMD_occlusion_query_event</code> - CType: int */
	  public static final int GL_OCCLUSION_QUERY_EVENT_MASK_AMD = 0x874f;
	  /** <code>GL_ARB_texture_filter_minmax</code> - CType: int */
	  public static final int GL_TEXTURE_REDUCTION_MODE_ARB = 0x9366;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_ALPHA_SNORM = 0x9010;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SHADER_OPERATION_NV = 0x86df;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_OVERLAY_KHR</code>, <code>GL_OVERLAY_NV</code> - CType: int */
	  public static final int GL_OVERLAY = 0x9296;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RED_BIAS = 0xd15;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_R32_NV = 0x8885;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COLOR_INDEX = 0x1900;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_VERTEX_EXT = 0x879a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_3D = 0x601;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_ACTIVE_UNIFORMS_ARB = 0x8b86;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x880f;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_VARIANTS_EXT = 0x87c6;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_GREEN_SCALE = 0xd18;
	  /** <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_INTENSITY32F_ARB</code> - CType: int */
	  public static final int GL_INTENSITY32F = 0x8817;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX23_ARB = 0x88d7;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE8_SNORM = 0x9015;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX2_ARB = 0x88c2;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_POST_CONVOLUTION_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_COLOR_TABLE = 0x80d1;
	  /** <code>GL_EXT_pixel_buffer_object</code> - CType: int */
	  public static final int GL_PIXEL_PACK_BUFFER_BINDING_EXT = 0x88ed;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_HILO8_NV = 0x885e;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_INVARIANTS_EXT = 0x87d1;
	  /** <code>GL_NV_tessellation_program5</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_PATCH_ATTRIBS_NV = 0x86d8;
	  /** <code>GL_EXT_pixel_buffer_object</code> - CType: int */
	  public static final int GL_PIXEL_PACK_BUFFER_EXT = 0x88eb;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX30_ARB = 0x88de;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_2D_ARB = 0x8b5e;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_PROGRAMMABLE_SAMPLE_LOCATIONS_ARB = 0x9342;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_EXT = 0x8dee;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_COUNTER_TYPE_AMD = 0x8bc0;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_TRANSFORM_2D_MATRIX_EXT = 0x8338;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_LIST_NESTING = 0xb31;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_BLUE_MIN_CLAMP_INGR = 0x8562;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_LUMINANCE_NV = 0x8701;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_RGBA_MODE = 0xc31;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE = 0x845c;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_SCISSOR_COMMAND_NV = 0x11;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_ALPHA_BIAS_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_ALPHA_BIAS = 0x8023;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_SAMPLE_LOCATION_PIXEL_GRID_WIDTH_NV = 0x933e;
	  /** <code>GL_S3_s3tc</code> - CType: int */
	  public static final int GL_RGBA4_S3TC = 0x83a3;
	  /** <code>GL_NV_copy_depth_to_color</code> - CType: int */
	  public static final int GL_DEPTH_STENCIL_TO_RGBA_NV = 0x886e;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_ARB = 0x8620;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_AMD = 0x9199;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIGHTING_BIT = 0x40;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW9_ARB = 0x8729;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_SAMPLER_RENDERBUFFER_NV = 0x8e56;
	  /** <code>GL_NV_sample_locations</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_SAMPLE_LOCATION_PIXEL_GRID_NV = 0x9343;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_EDGEFLAG_BIT_PGI = 0x40000;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT = 0x8de3;
	  /** <code>GL_EXT_index_material</code> - CType: int */
	  public static final int GL_INDEX_MATERIAL_EXT = 0x81b8;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_HSL_LUMINOSITY_KHR</code>, <code>GL_HSL_LUMINOSITY_NV</code> - CType: int */
	  public static final int GL_HSL_LUMINOSITY = 0x92b0;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV = 0x903a;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_SOURCE_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_SOURCE = 0x8450;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_PIXEL_TRANSFORM_2D_EXT = 0x8330;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_FRAC_EXT = 0x8789;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DEPENDENT_RGB_TEXTURE_CUBE_MAP_NV = 0x885a;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MVP_MATRIX_EXT = 0x87e3;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_SCALE_NV = 0x8851;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BLUE_SCALE = 0xd1a;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MAX_VERTEX_UNITS_ARB = 0x86a4;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP1_COLOR_4 = 0xd90;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_TEXTURE_SHADER_NV = 0x86de;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_COMPONENTS = 0x1003;
	  /** <code>GL_EXT_texture_snorm</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RED_SNORM = 0x8f90;
	  /** <code>GL_VERSION_1_5</code> - CType: int */
	  public static final int GL_FOG_COORD = 0x8451;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_LAST_VIDEO_CAPTURE_STATUS_NV = 0x9027;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_TEX_INDIRECTIONS_ARB = 0x8807;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_EXT = 0x87c1;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_3D_COLOR = 0x602;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x880a;
	  /** <code>GL_NV_geometry_program4</code>, <code>GL_EXT_geometry_shader4</code> - CType: int */
	  public static final int GL_PROGRAM_POINT_SIZE_EXT = 0x8642;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_TEMPORARIES_ARB = 0x88a4;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_ALPHA16_SNORM = 0x9018;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD13_EXT = 0x87aa;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD4_EXT = 0x87a1;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT8_VEC3_NV = 0x8fe2;
	  /** <code>GL_NV_conservative_raster_dilate</code> - CType: int */
	  public static final int GL_CONSERVATIVE_RASTER_DILATE_NV = 0x9379;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MULTIPLY_MATRIX_EXT = 0x8798;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_FRACTIONAL_TESSELLATION_NV = 0x86c5;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_RESAMPLE_DECIMATE_OML = 0x8989;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP2_APPLE = 0x8a01;
	  /** <code>GL_EXT_texture_swizzle</code> - CType: int */
	  public static final int GL_TEXTURE_SWIZZLE_B_EXT = 0x8e44;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_object</code><br>Alias for: <code>GL_TEXTURE_PRIORITY_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_PRIORITY = 0x8066;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_PROXY_HISTOGRAM_EXT</code> - CType: int */
	  public static final int GL_PROXY_HISTOGRAM = 0x8025;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_MAX_DRAW_BUFFERS_ATI = 0x8824;
	  /** <code>GL_EXT_422_pixels</code> - CType: int */
	  public static final int GL_422_REV_AVERAGE_EXT = 0x80cf;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_TOKEN = 0x703;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_SCALE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_SCALE = 0x80d6;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_ATI = 0x87f0;
	  /** <code>GL_NV_light_max_exponent</code> - CType: int */
	  public static final int GL_MAX_SHININESS_NV = 0x8504;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NEGATIVE_Z_EXT = 0x87db;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX18_ARB = 0x88d2;
	  /** <code>GL_EXT_texture_compression_latc</code> - CType: int */
	  public static final int GL_COMPRESSED_SIGNED_LUMINANCE_LATC1_EXT = 0x8c71;
	  /** <code>GL_EXT_bindable_uniform</code> - CType: int */
	  public static final int GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT = 0x8de4;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB = 0x8841;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW20_ARB = 0x8734;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_HILO16_NV = 0x86f8;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_ATTENUATION_EXT = 0x834d;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE = 0x8a04;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW18_ARB = 0x8732;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_ONE_EXT = 0x87de;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD0_EXT = 0x879d;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_FLUSH_INTEL = 0x83fa;
	  /** <code>GL_NVX_gpu_memory_info</code> - CType: int */
	  public static final int GL_GPU_MEMORY_INFO_EVICTED_MEMORY_NVX = 0x904b;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_TABLE_TOO_LARGE_EXT</code> - CType: int */
	  public static final int GL_TABLE_TOO_LARGE = 0x8031;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_LINE_WIDTH_COMMAND_NV = 0xd;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 0x8626;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ALL_ATTRIB_BITS = 0xfffff;
	  /** <code>GL_NV_float_buffer</code> - CType: int */
	  public static final int GL_FLOAT_RGBA_NV = 0x8883;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_SUB_EXT = 0x8796;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VECTOR_EXT = 0x87bf;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_CULL_MODES_NV = 0x86e0;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_VEC3_ARB = 0x8b51;
	  /** <code>GL_SUNX_constant_data</code> - CType: int */
	  public static final int GL_UNPACK_CONSTANT_DATA_SUNX = 0x81d5;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_SAMPLER_2D_SHADOW_ARB = 0x8b62;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DURATION_NORM_INTEL = 0x94f1;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_3DFX_multisample</code>, <code>GL_ARB_multisample</code>, <code>GL_EXT_multisample</code><br>Alias for: <code>GL_MULTISAMPLE_BIT_3DFX</code>, <code>GL_MULTISAMPLE_BIT_ARB</code>, <code>GL_MULTISAMPLE_BIT_EXT</code> - CType: int */
	  public static final int GL_MULTISAMPLE_BIT = 0x20000000;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_SEPARABLE_2D_EXT</code> - CType: int */
	  public static final int GL_SEPARABLE_2D = 0x8012;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_BINDING_EXT = 0x8781;
	  /** <code>GL_OML_resample</code> - CType: int */
	  public static final int GL_PACK_RESAMPLE_OML = 0x8984;
	  /** <code>GL_VERSION_1_0</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_4_BYTES_NV</code> - CType: int */
	  public static final int GL_4_BYTES = 0x1409;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_RGBA_FLOAT32_ATI = 0x8814;
	  /** <code>GL_EXT_stencil_two_side</code> - CType: int */
	  public static final int GL_ACTIVE_STENCIL_FACE_EXT = 0x8911;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_RED_BIAS_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_RED_BIAS = 0x8020;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_BOOL_VEC4_ARB = 0x8b59;
	  /** <code>GL_NV_tessellation_program5</code> - CType: int */
	  public static final int GL_TESS_EVALUATION_PROGRAM_PARAMETER_BUFFER_NV = 0x8c75;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_BIT = 0x8;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_DOT3_EXT = 0x8784;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_BITMAP_TOKEN = 0x704;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX27_ARB = 0x88db;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_INTENSITY_FLOAT16_APPLE = 0x881d;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_CLIP_NEAR_HINT_PGI = 0x1a220;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DESC_LENGTH_MAX_INTEL = 0x94ff;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_COMMAND_NV = 0xe;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB7_NV = 0x86cd;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER7_ATI = 0x882c;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87c8;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE = 0x8a07;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_separate_specular_color</code><br>Alias for: <code>GL_LIGHT_MODEL_COLOR_CONTROL_EXT</code> - CType: int */
	  public static final int GL_LIGHT_MODEL_COLOR_CONTROL = 0x81f8;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_POST_COLOR_MATRIX_COLOR_TABLE_SGI</code> - CType: int */
	  public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE = 0x80d2;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_BITS = 0xd51;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_INT_VEC2_ARB = 0x8b53;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SIGNED_HILO_NV = 0x86f9;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_HI_BIAS_NV = 0x8714;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE16_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE16 = 0x8042;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_V2F = 0x2a20;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_ROUND_EXT = 0x8790;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_POST_CONVOLUTION_BLUE_BIAS_EXT</code> - CType: int */
	  public static final int GL_POST_CONVOLUTION_BLUE_BIAS = 0x8022;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_B_TO_B = 0xc78;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB11_NV = 0x86d1;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_OES_texture_cube_map</code>, <code>GL_NV_texgen_reflection</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code><br>Alias for: <code>GL_NORMAL_MAP_OES</code>, <code>GL_NORMAL_MAP_NV</code>, <code>GL_NORMAL_MAP_ARB</code>, <code>GL_NORMAL_MAP_EXT</code> - CType: int */
	  public static final int GL_NORMAL_MAP = 0x8511;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_V3F = 0x2a21;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_TEXTURE_COORD_4 = 0xdb6;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_TEXTURE_COORD_3 = 0xdb5;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_TEXTURE_COORD_2 = 0xdb4;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_TEXTURE_COORD_1 = 0xdb3;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE12_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE12 = 0x8041;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 0x8643;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_VOLATILE_APPLE = 0x8a1a;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_TEXCOORD3_BIT_PGI = 0x40000000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_SHIFT = 0xd12;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_B = 0xc74;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_QUAD_STRIP = 0x8;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_A = 0xc75;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_NV = 0x8850;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_G = 0xc73;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_I = 0xc70;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_R = 0xc72;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INDEX_LOGIC_OP = 0xbf1;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;
	  /** <code>GL_NV_conservative_raster</code> - CType: int */
	  public static final int GL_SUBPIXEL_PRECISION_BIAS_X_BITS_NV = 0x9347;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHTING_EXT = 0x8509;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 0xd3b;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_T4F_V4F = 0x2a28;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_ALLOW_DRAW_OBJ_HINT_PGI = 0x1a20e;
	  /** <code>GL_INGR_color_clamp</code> - CType: int */
	  public static final int GL_ALPHA_MIN_CLAMP_INGR = 0x8563;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ARRAYS_INSTANCED_COMMAND_NV = 0x7;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_INSTRUCTIONS_ARB = 0x88a0;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_RGB_FLOAT16_ATI = 0x881b;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX14_ARB = 0x88ce;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_DRAW_ARRAYS_COMMAND_NV = 0x3;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_EVAL_ORDER = 0xd30;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_VARIANT_ARRAY_EXT = 0x87e8;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD17_EXT = 0x87ae;
	  /** <code>GL_ARB_texture_rectangle</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_RECTANGLE_ARB = 0x84f7;
	  /** <code>GL_ARB_sample_locations</code> - CType: int */
	  public static final int GL_PROGRAMMABLE_SAMPLE_LOCATION_TABLE_SIZE_ARB = 0x9340;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_MAX_PALETTE_MATRICES_ARB = 0x8842;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT16_VEC4_NV = 0x8ff3;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_TEXCOORD1_BIT_PGI = 0x10000000;
	  /** <code>GL_NV_texture_shader3</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_TEXTURE_1D_NV = 0x885c;
	  /** <code>GL_NV_explicit_multisample</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 0x8e58;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC3_NV = 0x8fea;
	  /** <code>GL_APPLE_object_purgeable</code> - CType: int */
	  public static final int GL_RETAINED_APPLE = 0x8a1b;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER4_ATI = 0x8829;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_INDEX = 0xdb1;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT = 0x850e;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_SHADER_CONSISTENT_NV = 0x86dd;
	  /** <code>GL_NV_shader_buffer_store</code> - CType: int */
	  public static final int GL_SHADER_GLOBAL_ACCESS_BARRIER_BIT_NV = 0x10;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x880e;
	  /** <code>GL_NV_conservative_raster_dilate</code> - CType: int */
	  public static final int GL_CONSERVATIVE_RASTER_DILATE_RANGE_NV = 0x937a;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE_ALPHA8UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA8UI = 0x8d81;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_IUI_V3F_EXT = 0x81ae;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SOURCE1_ALPHA_EXT</code>, <code>GL_SOURCE1_ALPHA_ARB</code> - CType: int */
	  public static final int GL_SOURCE1_ALPHA = 0x8589;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW1_ARB = 0x850a;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER15_ATI = 0x8834;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP_STENCIL = 0xd11;
	  /** <code>GL_ARB_imaging</code>, <code>GL_HP_convolution_border_modes</code><br>Alias for: <code>GL_CONVOLUTION_BORDER_COLOR_HP</code> - CType: int */
	  public static final int GL_CONVOLUTION_BORDER_COLOR = 0x8154;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI = 0x87f8;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_SPECULAR_BIT_PGI = 0x4000000;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT = 0x850f;
	  /** <code>GL_NV_texgen_emboss</code> - CType: int */
	  public static final int GL_EMBOSS_CONSTANT_NV = 0x855e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_S_TO_S = 0xc71;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_LUMINANCE16UI_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE16UI = 0x8d7a;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_LUMINANCE_FLOAT32_ATI = 0x8818;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_secondary_color</code><br>Alias for: <code>GL_SECONDARY_COLOR_ARRAY_EXT</code> - CType: int */
	  public static final int GL_SECONDARY_COLOR_ARRAY = 0x845e;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX9_ARB = 0x88c9;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_NEGATIVE_ONE_EXT = 0x87df;
	  /** <code>GL_EXT_framebuffer_sRGB</code><br>Alias for: <code>GL_FRAMEBUFFER_SRGB_CAPABLE_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_SRGB_CAPABLE = 0x8dba;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_MAX_SPARSE_TEXTURE_SIZE_AMD = 0x9198;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_PROGRAM_ERROR_POSITION_ARB = 0x864b;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW23_ARB = 0x8737;
	  /** <code>GL_EXT_texture_perturb_normal</code> - CType: int */
	  public static final int GL_TEXTURE_NORMAL_EXT = 0x85af;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88b2;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_DEPTH_SAMPLES_NV = 0x932d;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_RED_SIZE_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_RED_SIZE = 0x8028;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_BORDER_MODE_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_BORDER_MODE = 0x8013;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV = 0x8da5;
	  /** <code>GL_NV_command_list</code> - CType: int */
	  public static final int GL_UNIFORM_ADDRESS_COMMAND_NV = 0xa;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV = 0x9026;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_KHR_blend_equation_advanced</code><br>Alias for: <code>GL_EXCLUSION_NV</code>, <code>GL_EXCLUSION_KHR</code> - CType: int */
	  public static final int GL_EXCLUSION = 0x92a0;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LOGIC_OP = 0xbf1;
	  /** <code>GL_EXT_cull_vertex</code> - CType: int */
	  public static final int GL_CULL_VERTEX_OBJECT_POSITION_EXT = 0x81ac;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_3D_COLOR_TEXTURE = 0x603;
	  /** <code>GL_NV_parameter_buffer_object</code> - CType: int */
	  public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 0x8da4;
	  /** <code>GL_EXT_texture_snorm</code>, <code>GL_VERSION_3_1</code> - CType: int */
	  public static final int GL_RG_SNORM = 0x8f91;
	  /** <code>GL_EXT_provoking_vertex</code> - CType: int */
	  public static final int GL_PROVOKING_VERTEX_EXT = 0x8e4f;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FEEDBACK_BUFFER_SIZE = 0xdf1;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_MIN_LOD_WARNING_AMD = 0x919c;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DEPTH_SCALE = 0xd1e;
	  /** <code>GL_ARB_matrix_palette</code> - CType: int */
	  public static final int GL_CURRENT_MATRIX_INDEX_ARB = 0x8845;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ACCUM_RED_BITS = 0xd58;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_COLOR_4 = 0xdb0;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_BIT = 0x40000;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW14_ARB = 0x872e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SELECTION_BUFFER_POINTER = 0xdf3;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_POSITION_VALID = 0xb08;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV = 0x864e;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LINE_RESET_TOKEN = 0x707;
	  /** <code>GL_AMD_query_buffer_object</code> - CType: int */
	  public static final int GL_QUERY_BUFFER_AMD = 0x9192;
	  /** <code>GL_EXT_texture_compression_latc</code> - CType: int */
	  public static final int GL_COMPRESSED_LUMINANCE_ALPHA_LATC2_EXT = 0x8c72;
	  /** <code>GL_EXT_index_array_formats</code> - CType: int */
	  public static final int GL_IUI_V2F_EXT = 0x81ad;
	  /** <code>GL_NV_primitive_restart</code> - CType: int */
	  public static final int GL_PRIMITIVE_RESTART_INDEX_NV = 0x8559;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_POINT_MODE_ATI = 0x87f2;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_LUMINANCE_ALPHA_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 0x84eb;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 0xb06;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT8_VEC3_NV = 0x8fee;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_MAX_RASTER_SAMPLES_EXT = 0x9329;
	  /** <code>GL_NV_pixel_data_range</code> - CType: int */
	  public static final int GL_READ_PIXEL_DATA_RANGE_NV = 0x8879;
	  /** <code>GL_PGI_misc_hints</code> - CType: int */
	  public static final int GL_PREFER_DOUBLEBUFFER_HINT_PGI = 0x1a1f8;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_EVENT_INTEL = 0x94f0;
	  /** <code>GL_APPLE_float_pixels</code> - CType: int */
	  public static final int GL_ALPHA_FLOAT32_APPLE = 0x8816;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_UNSIGNED_INT64_VEC3_NV = 0x8ff6;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_OES_texture_cube_map</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_NV_texgen_reflection</code><br>Alias for: <code>GL_REFLECTION_MAP_OES</code>, <code>GL_REFLECTION_MAP_ARB</code>, <code>GL_REFLECTION_MAP_EXT</code>, <code>GL_REFLECTION_MAP_NV</code> - CType: int */
	  public static final int GL_REFLECTION_MAP = 0x8512;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_GPA_EXTENDED_COUNTERS_INTEL = 0x9500;
	  /** <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x880b;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV = 0x86f2;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_separate_specular_color</code><br>Alias for: <code>GL_SEPARATE_SPECULAR_COLOR_EXT</code> - CType: int */
	  public static final int GL_SEPARATE_SPECULAR_COLOR = 0x81fa;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_EXT_fog_coord</code><br>Alias for: <code>GL_FOG_COORDINATE_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_FOG_COORDINATE_ARRAY_TYPE = 0x8454;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_convolution</code><br>Alias for: <code>GL_CONVOLUTION_FILTER_BIAS_EXT</code> - CType: int */
	  public static final int GL_CONVOLUTION_FILTER_BIAS = 0x8015;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_BIT = 0x2;
	  /** <code>GL_REND_screen_coordinates</code> - CType: int */
	  public static final int GL_INVERTED_SCREEN_W_REND = 0x8491;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT16_VEC4_NV = 0x8fe7;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW27_ARB = 0x873b;
	  /** <code>GL_ARB_vertex_program</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 0x8869;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_INTENSITY8_SNORM = 0x9017;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD27_EXT = 0x87b8;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 0x88b5;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_WIDTH_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_WIDTH = 0x80d9;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_GLOBAL_CONTEXT_INTEL = 0x1;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW5_ARB = 0x8725;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_SHININESS_BIT_PGI = 0x2000000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ATTRIB_STACK_DEPTH = 0xbb0;
	  /** <code>GL_NV_framebuffer_mixed_samples</code> - CType: int */
	  public static final int GL_COVERAGE_MODULATION_TABLE_SIZE_NV = 0x9333;
	  /** <code>GL_NV_evaluators</code> - CType: int */
	  public static final int GL_EVAL_VERTEX_ATTRIB14_NV = 0x86d4;
	  /** <code>GL_NV_video_capture</code> - CType: int */
	  public static final int GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV = 0x903b;
	  /** <code>GL_EXT_pixel_transform</code> - CType: int */
	  public static final int GL_MAX_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT = 0x8337;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_DSDT_MAG_INTENSITY_NV = 0x86dc;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_BLUE_SIZE_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_BLUE_SIZE = 0x802a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAP2_NORMAL = 0xdb2;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 0x88a1;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_COUNTER_DATA_DOUBLE_INTEL = 0x94fb;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_FLOAT16_NV = 0x8ff8;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_RECIP_EXT = 0x8794;
	  /** <code>GL_AMD_sparse_texture</code> - CType: int */
	  public static final int GL_VIRTUAL_PAGE_SIZE_Z_AMD = 0x9197;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA_FLOAT32_ATI = 0x8819;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_RASTER_INDEX = 0xb05;
	  /** <code>GL_ARB_imaging</code>, <code>GL_EXT_histogram</code><br>Alias for: <code>GL_HISTOGRAM_GREEN_SIZE_EXT</code> - CType: int */
	  public static final int GL_HISTOGRAM_GREEN_SIZE = 0x8029;
	  /** <code>GL_ARB_gpu_shader_int64</code> - CType: int */
	  public static final int GL_INT64_VEC4_ARB = 0x8feb;
	  /** <code>GL_EXT_texture_mirror_clamp</code> - CType: int */
	  public static final int GL_MIRROR_CLAMP_TO_EDGE_EXT = 0x8743;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_STRIDE_ARB = 0x86aa;
	  /** <code>GL_NV_uniform_buffer_unified_memory</code> - CType: int */
	  public static final int GL_UNIFORM_BUFFER_LENGTH_NV = 0x9370;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER11_ATI = 0x8830;
	  /** <code>GL_INTEL_performance_query</code> - CType: int */
	  public static final int GL_PERFQUERY_WAIT_INTEL = 0x83fb;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87c5;
	  /** <code>GL_NV_fragment_coverage_to_color</code> - CType: int */
	  public static final int GL_FRAGMENT_COVERAGE_COLOR_NV = 0x92de;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON_STIPPLE_BIT = 0x10;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX5_ARB = 0x88c5;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OP_MADD_EXT = 0x8788;
	  /** <code>GL_ATI_pixel_format_float</code> - CType: int */
	  public static final int GL_RGBA_FLOAT_MODE_ATI = 0x8820;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD10_EXT = 0x87a7;
	  /** <code>GL_APPLE_vertex_array_range</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_APPLE = 0x8521;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_ALPHA8I_EXT</code> - CType: int */
	  public static final int GL_ALPHA8I = 0x8d90;
	  /** <code>GL_ATI_texture_float</code> - CType: int */
	  public static final int GL_INTENSITY_FLOAT32_ATI = 0x8817;
	  /** <code>GL_EXT_vertex_weighting</code> - CType: int */
	  public static final int GL_MODELVIEW0_STACK_DEPTH_EXT = 0xba3;
	  /** <code>GL_OML_interlace</code> - CType: int */
	  public static final int GL_INTERLACE_READ_OML = 0x8981;
	  /** <code>GL_APPLE_vertex_program_evaluators</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE = 0x8a05;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_TYPE_ARB = 0x8b4e;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_TEXCOORD2_BIT_PGI = 0x20000000;
	  /** <code>GL_AMD_performance_monitor</code> - CType: int */
	  public static final int GL_PERFMON_RESULT_SIZE_AMD = 0x8bc5;
	  /** <code>GL_ATI_pn_triangles</code> - CType: int */
	  public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI = 0x87f6;
	  /** <code>GL_NV_shader_thread_group</code> - CType: int */
	  public static final int GL_WARPS_PER_SM_NV = 0x933a;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_WEIGHT_ARRAY_ARB = 0x86ad;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SOURCE2_RGB_EXT</code>, <code>GL_SOURCE2_RGB_ARB</code> - CType: int */
	  public static final int GL_SOURCE2_RGB = 0x8582;
	  /** <code>GL_EXT_raster_multisample</code> - CType: int */
	  public static final int GL_EFFECTIVE_RASTER_SAMPLES_EXT = 0x932c;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_FLOAT_MAT3_ARB = 0x8b5b;
	  /** <code>GL_NV_gpu_shader5</code>, <code>GL_AMD_gpu_shader_int64</code> - CType: int */
	  public static final int GL_FLOAT16_VEC2_NV = 0x8ff9;
	  /** <code>GL_INTEL_map_texture</code> - CType: int */
	  public static final int GL_LAYOUT_DEFAULT_INTEL = 0x0;
	  /** <code>GL_VERSION_1_1</code> - CType: int */
	  public static final int GL_N3F_V3F = 0x2a25;
	  /** <code>GL_ATI_draw_buffers</code> - CType: int */
	  public static final int GL_DRAW_BUFFER0_ATI = 0x8825;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_MAT_DIFFUSE_BIT_PGI = 0x400000;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POLYGON = 0x9;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MATRIX20_ARB = 0x88d4;
	  /** <code>GL_EXT_index_func</code> - CType: int */
	  public static final int GL_INDEX_TEST_REF_EXT = 0x81b7;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_COLOR_INDEXES = 0x1603;
	  /** <code>GL_OVR_multiview</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_BASE_VIEW_INDEX_OVR = 0x9632;
	  /** <code>GL_EXT_light_texture</code> - CType: int */
	  public static final int GL_TEXTURE_MATERIAL_FACE_EXT = 0x8351;
	  /** <code>GL_OES_texture_3D</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_OES</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET = 0x8cd4;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_Y_EXT = 0x87d6;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUX0 = 0x409;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUX1 = 0x40a;
	  /** <code>GL_ATI_pixel_format_float</code>, <code>GL_ARB_color_buffer_float</code><br>Alias for: <code>GL_RGBA_FLOAT_MODE_ATI</code>, <code>GL_RGBA_FLOAT_MODE_ARB</code> - CType: int */
	  public static final int GL_RGBA_FLOAT_MODE = 0x8820;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_INVARIANT_DATATYPE_EXT = 0x87eb;
	  /** <code>GL_PGI_vertex_hints</code> - CType: int */
	  public static final int GL_VERTEX_DATA_HINT_PGI = 0x1a22a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUX2 = 0x40b;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_AUX3 = 0x40c;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY16I_EXT</code> - CType: int */
	  public static final int GL_INTENSITY16I = 0x8d8b;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_blend_equation_advanced</code>, <code>GL_NV_blend_equation_advanced</code><br>Alias for: <code>GL_SCREEN_KHR</code>, <code>GL_SCREEN_NV</code> - CType: int */
	  public static final int GL_SCREEN = 0x9295;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD23_EXT = 0x87b4;
	  /** <code>GL_ARB_shader_objects</code> - CType: int */
	  public static final int GL_OBJECT_LINK_STATUS_ARB = 0x8b82;
	  /** <code>GL_EXT_texture_snorm</code> - CType: int */
	  public static final int GL_LUMINANCE8_ALPHA8_SNORM = 0x9016;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_2D = 0x600;
	  /** <code>GL_ARB_imaging</code>, <code>GL_SGI_color_table</code><br>Alias for: <code>GL_COLOR_TABLE_GREEN_SIZE_SGI</code> - CType: int */
	  public static final int GL_COLOR_TABLE_GREEN_SIZE = 0x80db;
	  /** <code>GL_EXT_vertex_shader</code> - CType: int */
	  public static final int GL_OUTPUT_TEXTURE_COORD8_EXT = 0x87a5;
	  /** <code>GL_ARB_vertex_program</code>, <code>GL_ARB_fragment_program</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88a3;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 0xcb0;
	  /** <code>GL_NV_gpu_program4</code> - CType: int */
	  public static final int GL_MAX_PROGRAM_GENERIC_RESULTS_NV = 0x8da6;
	  /** <code>GL_EXT_depth_bounds_test</code> - CType: int */
	  public static final int GL_DEPTH_BOUNDS_EXT = 0x8891;
	  /** <code>GL_ARB_vertex_blend</code> - CType: int */
	  public static final int GL_MODELVIEW10_ARB = 0x872a;
	  /** <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 0xbb1;
	  /** <code>GL_EXT_pixel_buffer_object</code> - CType: int */
	  public static final int GL_PIXEL_UNPACK_BUFFER_EXT = 0x88ec;
	  /** <code>GL_EXT_texture_integer</code><br>Alias for: <code>GL_INTENSITY8I_EXT</code> - CType: int */
	  public static final int GL_INTENSITY8I = 0x8d91;
	  /** <code>GL_NV_texture_shader</code> - CType: int */
	  public static final int GL_OFFSET_TEXTURE_2D_SCALE_NV = 0x86e2;
	  
	public static final int GL_GENERATE_MIPMAP_SGIS = 0x8191;
	public static final int GL_GENERATE_MIPMAP_HINT_SGIS     = 0x8192;
	  	  	  
	default void glBegin(int arg) {
		org.lwjgl.opengl.GL11.glBegin(arg);
	}

	default void glEnd() {
		org.lwjgl.opengl.GL11.glEnd();
	}

	default void glDisable(int arg) {
		org.lwjgl.opengl.GL11.glDisable(arg);
	}

	default void glEnable(int arg) {
		org.lwjgl.opengl.GL11.glEnable(arg);
	}

	default void glEnableClientState(int arg) {
		org.lwjgl.opengl.GL11.glEnableClientState(arg);
	}

	default void glBindBuffer(int arg1, int arg2) {
		org.lwjgl.opengl.GL15.glBindBuffer(arg1, arg2);
	}

	default void glEndList() {
		org.lwjgl.opengl.GL11.glEndList();
	}

	default void glFlush() {
		org.lwjgl.opengl.GL11.glFlush();
	}

	default void glDepthFunc(int arg) {
		org.lwjgl.opengl.GL11.glDepthFunc(arg);
	}

	default void glDisableClientState(int arg) {
		org.lwjgl.opengl.GL11.glDisableClientState(arg);
	}

	default void glEnableVertexAttribArray(int arg) {
		org.lwjgl.opengl.GL20.glEnableVertexAttribArray(arg);
	}

	default void glDisableVertexAttribArray(int arg) {
		org.lwjgl.opengl.GL20.glDisableVertexAttribArray(arg);
	}
	
	default void glVertex3fv(float[] arg) {
		org.lwjgl.opengl.GL11.glVertex3fv( arg);		
	}

	default void glVertex3fv(float[] arg1, int arg2) {
		if(arg2 == 0)
			org.lwjgl.opengl.GL11.glVertex3fv( arg1);
		else {
			float[] three = new float[3];
			three[0] = arg1[arg2];
			three[1] = arg1[arg2+1];
			three[2] = arg1[arg2+2];
			org.lwjgl.opengl.GL11.glVertex3fv( three);
		}
	}

	default void glVertex3fv(FloatBuffer arg) {
		org.lwjgl.opengl.GL11.glVertex3fv( arg);
	}

	default void glMatrixMode(int arg) {
		org.lwjgl.opengl.GL11.glMatrixMode(arg);
	}

	default void glAccum(int arg1, float arg2) {
		org.lwjgl.opengl.GL11.glAccum(arg1,arg2);
	}

	default void glBindTexture(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glBindTexture(arg1,arg2);
	}

	default void glAttachObjectARB(long arg1, long arg2) {
		org.lwjgl.opengl.ARBShaderObjects.glAttachObjectARB((int)arg1, (int)arg2);
	}

	default void glGetIntegerv(int arg1, int[] arg2) {
		org.lwjgl.opengl.GL11.glGetIntegerv(arg1,arg2);
	}

	default void glGetIntegerv(int arg1, int[] arg2, int arg3) {
		if( arg3 != 0) {
			throw new IllegalArgumentException();
		}
		org.lwjgl.opengl.GL11.glGetIntegerv(arg1,arg2);
	}

	default void glVertexAttribPointer(int arg1, int arg2, int arg3, boolean arg4, int arg5, int arg6) {
		org.lwjgl.opengl.GL20.glVertexAttribPointer(arg1, arg2, arg3, arg4, arg5, arg6);
	}

	default void glVertexAttribPointer(int arg1, int arg2, int arg3, boolean arg4, int arg5, ByteBuffer arg6) {
		org.lwjgl.opengl.GL20.glVertexAttribPointer(arg1, arg2, arg3, arg4, arg5, arg6);		
	}

	default void glVertexAttribPointer(int arg1, int arg2, int arg3, boolean arg4, int arg5, ShortBuffer arg6) {
		org.lwjgl.opengl.GL20.glVertexAttribPointer(arg1, arg2, arg3, arg4, arg5, arg6);		
	}

	default void glVertexAttribPointer(int arg1, int arg2, int arg3, boolean arg4, int arg5, IntBuffer arg6) {
		org.lwjgl.opengl.GL20.glVertexAttribPointer(arg1, arg2, arg3, arg4, arg5, arg6);		
	}

	default void glVertexAttribPointer(int arg1, int arg2, int arg3, boolean arg4, int arg5, FloatBuffer arg6) {
		org.lwjgl.opengl.GL20.glVertexAttribPointer(arg1, arg2, arg3, arg4, arg5, arg6);				
	}

	
	default void glPopMatrix() {
		org.lwjgl.opengl.GL11.glPopMatrix();
	}

	default void glTranslatef(float arg1, float arg2, float arg3) {
		org.lwjgl.opengl.GL11.glTranslatef(arg1,arg2,arg3);
	}

	default void glMaterialfv(int arg1, int arg2, float[] arg3, int arg4) {
		org.lwjgl.opengl.GL11.glMaterialfv(arg1,arg2,arg3);
	}

	default void glMaterialfv(int arg1, int arg2, float[] arg3) {
		org.lwjgl.opengl.GL11.glMaterialfv(arg1,arg2,arg3);
	}

	default void glClear(int arg) {
		org.lwjgl.opengl.GL11.glClear(arg);
	}

	default void glNormal3fv(float[] arg1, int arg2) {
		if(arg2 != 0) {
			throw new IllegalArgumentException();
		}
		org.lwjgl.opengl.GL11.glNormal3fv( arg1);
	}
	
	default void glNormal3fv(float[] arg1) {
		org.lwjgl.opengl.GL11.glNormal3fv( arg1);
	}

	default void glTexCoord2f(float arg1, float arg2) {
		org.lwjgl.opengl.GL11.glTexCoord2f( arg1, arg2);
	}

	default void glNormal3fv(FloatBuffer arg) {
		org.lwjgl.opengl.GL11.glNormal3fv(arg);
	}

	default void glLightf(int arg1, int arg2, float arg3) {
		org.lwjgl.opengl.GL11.glLightf(arg1,arg2,arg3);
	}

	default void glVertex2fv(float[] arg1, int arg2) {
		org.lwjgl.opengl.GL11.glVertex2fv(arg1);
	}

	default void glPixelStorei(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glPixelStorei(arg1,arg2);
	}

	default void glRects(short arg1, short arg2, short arg3, short arg4) {
		org.lwjgl.opengl.GL11.glRects(arg1, arg2, arg3, arg4);
	}

	default void glLightfv(int arg1, int arg2, float[] arg3, int arg4) {
		org.lwjgl.opengl.GL11.glLightfv(arg1,arg2,arg3);
	}

	default void glPushMatrix() {
		org.lwjgl.opengl.GL11.glPushMatrix();
	}

	default void glLoadIdentity() {
		org.lwjgl.opengl.GL11.glLoadIdentity();
	}

	default void glShadeModel(int arg) {
		org.lwjgl.opengl.GL11.glShadeModel(arg);
	}

	default void glDepthMask(boolean arg) {
		org.lwjgl.opengl.GL11.glDepthMask(arg);
	}

	default int glGetError() {
		return org.lwjgl.opengl.GL11.glGetError();
	}

	default void glFogf(int arg1, float arg2) {
		org.lwjgl.opengl.GL11.glFogf(arg1,arg2);
	}

	default void glLoadMatrixf(float[] arg1, int arg2) {
		org.lwjgl.opengl.GL11.glLoadMatrixf(arg1);
	}

	default void glTexCoord2fv(float[] arg1, int arg2) {
		org.lwjgl.opengl.GL11.glTexCoord2fv(arg1);
	}

	default void glNewList(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glNewList(arg1,arg2);
	}

	default void glTexCoord4fv(float[] arg1, int arg2) {
		org.lwjgl.opengl.GL11.glTexCoord4fv(arg1);
	}

	default void glVertexAttrib4fv(int arg1, float[] arg2, int arg3) {
		org.lwjgl.opengl.GL20.glVertexAttrib4fv(arg1,arg2);
	}

	default void glPolygonMode(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glPolygonMode(arg1,arg2);
	}

	default void glColor4ubv(byte[] arg1, int arg2) {		
		org.lwjgl.opengl.GL11.glColor4ubv(Buffers.newDirectByteBuffer(arg1));
	}

	default void glNormal3f(float arg1, float arg2, float arg3) {
		org.lwjgl.opengl.GL11.glNormal3f(arg1,arg2,arg3);
	}

	default void glDrawBuffer(int arg) {
		org.lwjgl.opengl.GL11.glDrawBuffer(arg);
	}

	default void glDepthRange(double arg1, double arg2) {
		org.lwjgl.opengl.GL11.glDepthRange(arg1,arg2);
	}

	default void glTexGenf(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glTexGenf(arg1,arg2,arg3);
	}

	default void glTexParameteri(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glTexParameteri(arg1,arg2,arg3);
	}

	default void glViewport(short arg1, short arg2, short arg3, short arg4) {
		org.lwjgl.opengl.GL11.glViewport(arg1,arg2,arg3,arg4);
	}

	default void glCallList(int arg) {
		org.lwjgl.opengl.GL11.glCallList(arg);
	}

	default void glLightModeli(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glLightModeli(arg1,arg2);
	}

	default void glColorPointer(int arg1, int glUnsignedByte, int arg3, int i) {
		org.lwjgl.opengl.GL11.glColorPointer(arg1, glUnsignedByte, arg3, i);
	}

	default void glColorPointer(int arg1, int arg2, int arg3, FloatBuffer arg4) {
		org.lwjgl.opengl.GL11.glColorPointer(arg1,arg2,arg3,arg4);
	}

	default void glColorPointer(int arg1, int arg2, int arg3, IntBuffer arg4) {
		org.lwjgl.opengl.GL11.glColorPointer(arg1,arg2,arg3,arg4);
	}

	default void glColorPointer(int arg1, int arg2, int arg3, ByteBuffer arg4) {
		org.lwjgl.opengl.GL11.glColorPointer(arg1,arg2,arg3,arg4);
	}

	default void glFrontFace(int arg) {
		org.lwjgl.opengl.GL11.glFrontFace(arg);
	}

	default void glIndexi(int arg) {
		org.lwjgl.opengl.GL11.glIndexi(arg);
	}

	default void glTexImage2D(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7,
			int arg8, ByteBuffer arg9) {
		org.lwjgl.opengl.GL11.glTexImage2D(arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9);
	}

	default void glTexImage2D(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7,
			int arg8, short[] array) {
		Buffers.newDirectShortBuffer(array);
		org.lwjgl.opengl.GL11.glTexImage2D(arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,array);
	}

	default void glTexImage2D(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7,
			int arg8, long arg9) {
		org.lwjgl.opengl.GL11.glTexImage2D(arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9);
	}

	default void glTexGenfv(int arg1, int arg2, float[] arg3, int arg4) {
		if( arg4 != 0 ) {
			throw new IllegalArgumentException();
		}
		org.lwjgl.opengl.GL11.glTexGenfv(arg1,arg2,arg3);
	}

	default void glTexGenfv(int arg1, int arg2, float[] arg3) {
		org.lwjgl.opengl.GL11.glTexGenfv(arg1,arg2,arg3);
	}

	default void glPopAttrib() {
		org.lwjgl.opengl.GL11.glPopAttrib();
	}

	default boolean isExtensionAvailable(String string) {
		return true;
	}

	default void glBufferData(int arg1, long arg2, Buffer arg3, int arg4) {
		if(arg3 instanceof ByteBuffer) {
			org.lwjgl.opengl.GL15.glBufferData(arg1,/* arg2,*/ (ByteBuffer)arg3, arg4);
		}
		else if(arg3 instanceof FloatBuffer) {
			org.lwjgl.opengl.GL15.glBufferData(arg1,/* arg2,*/ (FloatBuffer)arg3, arg4);
		}
		else if(arg3 instanceof IntBuffer) {
			org.lwjgl.opengl.GL15.glBufferData(arg1,/* arg2,*/ (IntBuffer)arg3, arg4);
		}
		else {
			org.lwjgl.opengl.GL15.glBufferData(arg1, arg2, arg4);
		}
	}

	default void glBufferData(int arg1, long arg2, int[] arg3, int arg4) {
		org.lwjgl.opengl.GL15.glBufferData(arg1,arg3,arg4);
	}
	default void glBufferData(int arg1, long arg2, float[] arg3, int arg4) {
		org.lwjgl.opengl.GL15.glBufferData(arg1,arg3,arg4);
	}
	default void glBufferSubData(int arg1, long arg2, int arg3, ByteBuffer arg4) {
		org.lwjgl.opengl.GL15.glBufferSubData(arg1,arg2,/*arg3,*/arg4);
	}

	default void glTexEnvi(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glTexEnvi(arg1,arg2,arg3);
	}

	default void glTexEnvfv(int arg1, int arg2, float[] arg3, int arg4) {
		org.lwjgl.opengl.GL11.glTexEnvfv(arg1,arg2,arg3);
	}

	default void glRasterPos3f(float arg1, float arg2, float arg3) {
		org.lwjgl.opengl.GL11.glRasterPos3f(arg1,arg2,arg3);
	}

	default void glBitmap(int arg1, int arg2, float arg3, float arg4, float arg5, float arg6, byte[] arg7, int arg8) {
		org.lwjgl.opengl.GL11.glBitmap(arg1,arg2,arg3,arg4,arg5,arg6, Buffers.newDirectByteBuffer(arg7));
	}

	default void glOrtho(double arg1, double arg2, double arg3, double arg4, double arg5, double arg6) {
		org.lwjgl.opengl.GL11.glOrtho(arg1,arg2,arg3,arg4,arg5,arg6);
	}

	default void glBlendFunc(int arg1, int arg2) {
		org.lwjgl.opengl.GL11.glBlendFunc(arg1,arg2);
	}

	default void glCallLists(int arg1, int arg2, ShortBuffer arg3) {
		org.lwjgl.opengl.GL11.glCallLists(arg3);
	}

	default void glCallLists(int arg1, int arg2, ByteBuffer arg3) {
		org.lwjgl.opengl.GL11.glCallLists(arg2, arg3);
	}

	default void glListBase(int arg) {
		org.lwjgl.opengl.GL11.glListBase(arg);
	}

	default void glVertex2fv(FloatBuffer object) {
		org.lwjgl.opengl.GL11.glVertex2fv(object);
	}

	default void glPushAttrib(int arg) {
		org.lwjgl.opengl.GL11.glPushAttrib(arg);
	}

	default void glTexGeni(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glTexGeni(arg1,arg2,arg3);
	}

	default void glRasterPos3fv(float[] arg1, int arg2) {
		org.lwjgl.opengl.GL11.glRasterPos3fv(arg1);
	}

	default void glVertexPointer(int arg1, int arg2, int arg3, int arg4) {
		org.lwjgl.opengl.GL11.glVertexPointer( arg1, arg2, arg3, arg4);
	}

	default void glVertexPointer(int arg1, int glFloat, int arg3, ByteBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glVertexPointer(arg1, glFloat, arg3, dataPtr);
	}

	default void glVertexPointer(int arg1, int glFloat, int arg3, FloatBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glVertexPointer(arg1, glFloat, arg3, dataPtr);
	}

	default void glNormalPointer(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glNormalPointer(arg1, arg2, arg3);
	}

	default void glNormalPointer(int glFloat, int arg2, ByteBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glNormalPointer(glFloat, arg2, dataPtr);
	}

	default void glNormalPointer(int glFloat, int arg2, FloatBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glNormalPointer(glFloat, arg2, dataPtr);
	}

	default void glTexCoord2fv(FloatBuffer floatBuffer) {
		org.lwjgl.opengl.GL11.glTexCoord2fv(floatBuffer);
	}

	default void glIndexiv(int[] c, int c_offset) {
		org.lwjgl.opengl.GL11.glIndexiv(c/*, c_offset*/);
	}

	default void glTexCoordPointer(int arg1, int arg2, int arg3, int arg4) {
		org.lwjgl.opengl.GL11.glTexCoordPointer(arg1,arg2,arg3,arg4);
	}

	default void glTexCoordPointer(int arg1, int glFloat, int arg3, FloatBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glTexCoordPointer(arg1, glFloat, arg3, dataPtr);
	}

	default void glTexCoordPointer(int arg1, int glFloat, int arg3, ByteBuffer dataPtr) {
		org.lwjgl.opengl.GL11.glTexCoordPointer(arg1, glFloat, arg3, dataPtr);
	}

	default void glVertex4fv(float[] v, int v_offset) {
		if(v_offset == 0)
			org.lwjgl.opengl.GL11.glVertex4fv(v/*, v_offset*/);
		else {
			float[] four = new float[4];
			four[0] = v[v_offset];
			four[1] = v[v_offset+1];
			four[2] = v[v_offset+2];
			four[3] = v[v_offset+3];
			org.lwjgl.opengl.GL11.glVertex4fv(four/*, v_offset*/);
		}
	}

	default void glClearColor(float arg0, float arg1, float arg2,
			float arg3) {
		org.lwjgl.opengl.GL11.glClearColor( arg0, arg1, arg2, arg3);
	}

	default void glClearIndex(int arg) {
		org.lwjgl.opengl.GL11.glClearIndex(arg);
	}

	default void glPolygonStipple(byte[] bs, int i) {
		org.lwjgl.opengl.GL11.glPolygonStipple(Buffers.newDirectByteBuffer(bs)/*, i*/);
	}

	default void glPolygonStipple(byte[] bs) {
		org.lwjgl.opengl.GL11.glPolygonStipple(Buffers.newDirectByteBuffer(bs));
	}

	default void glGetBooleanv(int glRgbaMode, byte[] b, int i) {
		ByteBuffer buf = Buffers.newDirectByteBuffer(b);
		org.lwjgl.opengl.GL11.glGetBooleanv(glRgbaMode, buf);
		b[0] = buf.get();
	}

	default void glColorMaterial(int glFrontAndBack, int glDiffuse) {
		org.lwjgl.opengl.GL11.glColorMaterial( glFrontAndBack, glDiffuse);
	}

	default void glMaterialf(int glFrontAndBack, int glShininess, float f) {
		org.lwjgl.opengl.GL11.glMaterialf( glFrontAndBack, glShininess, f);
	}

	default int glGenLists(int i) {
		return org.lwjgl.opengl.GL11.glGenLists(i);
	}

	default void glColor3f(float arg1, float arg2, float arg3) {
		org.lwjgl.opengl.GL11.glColor3f(arg1,arg2,arg3);
	}

	default void glLineWidth(float f) {
		org.lwjgl.opengl.GL11.glLineWidth(f);
	}

	default void glDeleteBuffersARB(int[] startIndex) {
		ARBVertexBufferObject.glDeleteBuffersARB(startIndex);
	}

	default void glDeleteTextures(int[] startIndex) {
		org.lwjgl.opengl.GL11.glDeleteTextures(startIndex);
	}

	default void glDeleteLists(int i, int num) {
		org.lwjgl.opengl.GL11.glDeleteLists(i,num);
	}

	default void glDrawArrays(int arg1, int arg2, int arg3) {
		org.lwjgl.opengl.GL11.glDrawArrays(arg1,arg2,arg3);
	}

	default void glActiveTexture(int texture) {
		org.lwjgl.opengl.GL13.glActiveTexture(texture);
	}

	default void glGetTexLevelParameteriv(int target, int level, int pname, int[] params) {
		org.lwjgl.opengl.GL11.glGetTexLevelParameteriv(target,level,pname,params);
	}

	default void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border,
			int format, int type, ByteBuffer pixels) {
		org.lwjgl.opengl.GL12.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
	}

	default void glDeleteFramebuffers(int n, int[] framebuffers) {
		org.lwjgl.opengl.GL30.glDeleteFramebuffers(framebuffers);
	}

	default void glDeleteFramebuffers(int n, int frameBuffer) {
		org.lwjgl.opengl.GL30.glDeleteFramebuffers(frameBuffer);
	}

	default void glDeleteRenderbuffers(int n, int renderbuffers) {
		org.lwjgl.opengl.GL30.glDeleteRenderbuffers(renderbuffers);
	}

	default void glMultiTexCoord2fv(int target, float[] v) {
		org.lwjgl.opengl.GL13.glMultiTexCoord2fv(target, v);
	}

	default void glMultiTexCoord3fv(int target, float[] v) {
		org.lwjgl.opengl.GL13.glMultiTexCoord3fv(target, v);
	}

	default void glMultiTexCoord4fv(int target, float[] v) {
		org.lwjgl.opengl.GL13.glMultiTexCoord4fv(target, v);
	}

	default void glTexEnvf(int glTextureEnv, int glAlphaScale, float alphascale) {
		org.lwjgl.opengl.GL11.glTexEnvf(glTextureEnv, glAlphaScale, alphascale);
	}

	default int glCheckFramebufferStatus(int target) {
		return org.lwjgl.opengl.GL30.glCheckFramebufferStatus(target);
	}

	default void glGetFloatv(int pname, float[] params) {
		org.lwjgl.opengl.GL11.glGetFloatv(pname, params);
	}

	default void glBindFrameBuffer(int target, int framebuffer) {
		org.lwjgl.opengl.GL30.glBindFramebuffer(target, framebuffer);
	}

	default void glCopyTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
		org.lwjgl.opengl.GL11.glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	default void glGenerateMipmap(int target) {
		org.lwjgl.opengl.GL30.glGenerateMipmap(target);
	}

	default void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		org.lwjgl.opengl.GL30.glFramebufferRenderbuffer( target, attachment, renderbuffertarget, renderbuffer);
	}

	default void glRenderbufferStorage(int target, int internalformat, short width, short height) {
		org.lwjgl.opengl.GL30.glRenderbufferStorage(target, internalformat, width, height);
	}

	default void glBindRenderbuffer(int target, int renderbuffer) {
		org.lwjgl.opengl.GL30.glBindRenderbuffer( target, renderbuffer);
	}

	default void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		org.lwjgl.opengl.GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
	}

	default void glTexParameterf(int glTexture2d, int glTextureMaxAnisotropyExt, float cc_glglue_get_max_anisotropy) {
		org.lwjgl.opengl.GL11.glTexParameterf(glTexture2d, glTextureMaxAnisotropyExt, cc_glglue_get_max_anisotropy);
	}

	default void glGenFramebuffers(int n, int[] framebuffers) {
		assert(n == framebuffers.length);
		org.lwjgl.opengl.GL30.glGenFramebuffers( framebuffers);
	}

	default void glGenRenderbuffers(int n, int[] renderbuffers) {
		assert(n == renderbuffers.length);
		org.lwjgl.opengl.GL30.glGenRenderbuffers( renderbuffers);		
	}

	default void glReadPixels(int x, int y, short width, short height, int format, int type, byte[] offscreenbuffer) {
		int capacity = offscreenbuffer.length;
		ByteBuffer bb = BufferUtils.createByteBuffer(capacity);
		org.lwjgl.opengl.GL11.glReadPixels(x,y,width,height,format,type, bb);
		bb.get(offscreenbuffer);
	}

	default void glReadPixels(int x, int y, short width, short height, int format, int type, MemoryBuffer offscreenbuffer) {
		//int capacity = offscreenbuffer.length;
		ByteBuffer bb = offscreenbuffer.toByteBuffer();//BufferUtils.createByteBuffer(capacity);
		org.lwjgl.opengl.GL11.glReadPixels(x,y,width,height,format,type, bb);
		//bb.get(offscreenbuffer);
	}

	default boolean glIsEnabled(int function) {
		return org.lwjgl.opengl.GL11.glIsEnabled(function);
	}

	default void glVertex2s(short arg0, short arg1) {
		org.lwjgl.opengl.GL11.glVertex2s(arg0, arg1);
	}

	default void glGenBuffers(int n, int[] buffers) {
		assert(n == buffers.length);
		org.lwjgl.opengl.GL15.glGenBuffers(buffers);
	}

	default void glClientActiveTexture(int texture) {
		org.lwjgl.opengl.GL13.glClientActiveTexture(texture);
	}

	default void glVertexAttrib1fARB(int index, float x) {
		org.lwjgl.opengl.GL20.glVertexAttrib1f/*ARB*/(index,x);
	}

	default void glVertexAttrib2fvARB(int index, float[] v) {
		org.lwjgl.opengl.GL20.glVertexAttrib2fv/*ARB*/(index,v);
	}

	default void glVertexAttrib3fvARB(int index, float[] v) {
		org.lwjgl.opengl.GL20.glVertexAttrib3fv/*ARB*/(index,v);
	}

	default void glVertexAttrib4fvARB(int index, float[] v) {
		org.lwjgl.opengl.GL20.glVertexAttrib4fv/*ARB*/(index,v);
	}

	default void glVertexAttrib1sARB(int index, short x) {
		org.lwjgl.opengl.GL20.glVertexAttrib1s/*ARB*/(index,x);
	}

	default void glDisableVertexAttribArrayARB(int index) {
		org.lwjgl.opengl.GL20.glDisableVertexAttribArray/*ARB*/(index);
	}

	default void glEnableVertexAttribArrayARB(int index) {
		org.lwjgl.opengl.GL20.glEnableVertexAttribArray/*ARB*/(index);
	}

	default int glGetAttribLocationARB(int programobj, String name) {
		return org.lwjgl.opengl.GL20.glGetAttribLocation/*ARB*/(programobj,name);
	}

	default void glDrawElements(int mode, int count, int type, VoidPtr indices) {
		ByteBuffer bb = indices.toByteBuffer();
		org.lwjgl.opengl.GL11.glDrawElements(mode,type,bb);
	}

	default void glDrawElements(int mode, int count, int type, IntArrayPtr indices) {
		
//		try ( MemoryStack stack = stackPush() ) {
//			ByteBuffer bb = stack.malloc(indices.size()*Integer.BYTES);
//			bb.asIntBuffer().put(indices.getValues(),indices.getStart(),indices.size());
//			bb.clear();
//			org.lwjgl.opengl.GL11.glDrawElements(mode,type,bb);
//		}
		org.lwjgl.opengl.GL11.glDrawElements(mode,/*type,*/indices.toIntBuffer());
	}

	default void glDrawElements(int mode, int count, int type, long indices) {
		org.lwjgl.opengl.GL11.glDrawElements(mode,count,type,indices);
	}

	default void glMultiDrawElements(int mode, IntArrayPtr count, int type, VoidPtr[] indices, int primcount) {
		
		assert(primcount == count.size());
		for(int i=0;i<primcount;i++) {
			VoidPtr iindices = indices[i];
			int icount = count.get(i);
			glDrawElements(mode, icount, type, iindices);
		}
	}

	default void glColor4ub(byte red, byte green, byte blue, byte alpha) {
		org.lwjgl.opengl.GL11.glColor4ub(red, green, blue, alpha);
	}

	default void glBlendFuncSeparate(int rgbsrc, int rgbdst, int alphasrc, int alphadst) {
		org.lwjgl.opengl.GL14.glBlendFuncSeparate(rgbsrc, rgbdst, alphasrc, alphadst);
	}

	default void glAlphaFunc(int func, float value) {
		org.lwjgl.opengl.GL11.glAlphaFunc(func, value);
	}

	default void glTexCoord3fv(float[] v) {
		org.lwjgl.opengl.GL11.glTexCoord3fv(v);
	}

	default void glTexCoord2fv(float[] v) {
		org.lwjgl.opengl.GL11.glTexCoord2fv(v);
	}

	default void glTexCoord3f(float f, float g, float h) {
		org.lwjgl.opengl.GL11.glTexCoord3f(f, g, h);
	}

	default void glVertex3f(float x, float y, float z) {
		org.lwjgl.opengl.GL11.glVertex3f(x, y, z);
	}

	default void glMultiTexCoord2f(int target, float s, float t) {
		org.lwjgl.opengl.GL13.glMultiTexCoord2f(target,s,t);
	}

	default void glDeleteBuffers(int n, int[] buffers) {
		assert( n == buffers.length);
		org.lwjgl.opengl.GL15.glDeleteBuffers(buffers);
	}

	default void glGenProgramsARB(int n, int[] ids) {
		assert( n == ids.length );
		org.lwjgl.opengl.ARBVertexProgram.glGenProgramsARB(ids);
	}

	default void glBindProgramARB(int code, int id) {
		org.lwjgl.opengl.ARBVertexProgram.glBindProgramARB(code,id);
	}
	
	default void glProgramStringARB( int a, int b, int len, String str) {
		assert ( len == str.length());
		Charset charset = Charset.forName("UTF-8");
		byte[] array = str.getBytes(charset);
		ByteBuffer buffer = Buffers.newDirectByteBuffer(array);
		org.lwjgl.opengl.ARBVertexProgram.glProgramStringARB(a,b, buffer);
	}
	
	default void glGenTextures(int n, int[] ids) {
		assert( n == ids.length);
		org.lwjgl.opengl.GL11.glGenTextures(ids);
	}

	default void glGenTextures( int[] ids) {
		org.lwjgl.opengl.GL11.glGenTextures(ids);
	}

	default void glVertex2f(float x, float y) {
		org.lwjgl.opengl.GL11.glVertex2f(x, y);
	}

	default void glCopyTexImage2D(int glTextureRectangle, int i, int glRgba8, int j, int k, short s, short t, int l) {
		org.lwjgl.opengl.GL11.glCopyTexImage2D(glTextureRectangle, i, glRgba8, j, k, s, t, l);
	}

	default void glCullFace(int glFront) {
		org.lwjgl.opengl.GL11.glCullFace(glFront);
	}

	default void glScalef(float arg0, float arg1, float arg2) {
		org.lwjgl.opengl.GL11.glScalef(arg0, arg1, arg2);
	}

	default void glMultMatrixf(float[] fs) {
		org.lwjgl.opengl.GL11.glMultMatrixf(fs);
	}

	default void glPixelTransferi(int arg0, int arg1) {
		org.lwjgl.opengl.GL11.glPixelTransferi(arg0, arg1);
	}

	default void glPixelTransferf(int arg0, float arg1) {
		org.lwjgl.opengl.GL11.glPixelTransferf(arg0, arg1);
	}

	default void glPixelMapfv(int map, int i, float[] values) {
		if(i != values.length) {
			throw new IllegalArgumentException();
		}
		org.lwjgl.opengl.GL11.glPixelMapfv(map,values);
	}

	default void glPixelMapuiv(int map, int i, int[] values) {
		if(i != values.length) {
			throw new IllegalArgumentException();
		}
		org.lwjgl.opengl.GL11.glPixelMapuiv(map, values);
	}

	default void glFinish() {
		org.lwjgl.opengl.GL11.glFinish();
	}
}
