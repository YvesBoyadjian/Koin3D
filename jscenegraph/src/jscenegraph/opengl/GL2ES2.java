/**
 * 
 */
package jscenegraph.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL2ES2 {

	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_SHADER_KHR</code> - CType: int */
	  public static final int GL_SHADER = 0x82e1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_CURRENT_VERTEX_ATTRIB_ARB</code> - CType: int */
	  public static final int GL_CURRENT_VERTEX_ATTRIB = 0x8626;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_vertex_shader</code>, <code>GL_ARB_vertex_shader</code><br>Alias for: <code>GL_VERTEX_SHADER_EXT</code>, <code>GL_VERTEX_SHADER_ARB</code> - CType: int */
	  public static final int GL_VERTEX_SHADER = 0x8b31;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code>, <code>GL_AMD_debug_output</code><br>Alias for: <code>GL_MAX_DEBUG_MESSAGE_LENGTH_KHR</code>, <code>GL_MAX_DEBUG_MESSAGE_LENGTH_ARB</code>, <code>GL_MAX_DEBUG_MESSAGE_LENGTH_AMD</code> - CType: int */
	  public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 0x9143;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER1_NV</code>, <code>GL_DRAW_BUFFER1_ATI</code>, <code>GL_DRAW_BUFFER1_EXT</code>, <code>GL_DRAW_BUFFER1_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER1 = 0x8826;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8dfd;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_QUERY_KHR</code> - CType: int */
	  public static final int GL_QUERY = 0x82e3;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_VERTEX_SHADER_BIT_EXT</code> - CType: int */
	  public static final int GL_VERTEX_SHADER_BIT = 0x1;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 0x910f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_separate_stencil</code><br>Alias for: <code>GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI</code> - CType: int */
	  public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_ACTIVE_PROGRAM_EXT</code> - CType: int */
	  public static final int GL_ACTIVE_PROGRAM = 0x8259;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT5_EXT</code>, <code>GL_COLOR_ATTACHMENT5_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT5 = 0x8ce5;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB</code>, <code>GL_DEBUG_SOURCE_WINDOW_SYSTEM_KHR</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_separate_stencil</code><br>Alias for: <code>GL_STENCIL_BACK_FUNC_ATI</code> - CType: int */
	  public static final int GL_STENCIL_BACK_FUNC = 0x8800;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_COMPILE_STATUS = 0x8b81;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_GROUP_STACK_DEPTH_KHR</code> - CType: int */
	  public static final int GL_DEBUG_GROUP_STACK_DEPTH = 0x826d;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_VEC2_ARB</code> - CType: int */
	  public static final int GL_FLOAT_VEC2 = 0x8b50;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_VEC4_ARB</code> - CType: int */
	  public static final int GL_FLOAT_VEC4 = 0x8b52;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_VEC3_ARB</code> - CType: int */
	  public static final int GL_FLOAT_VEC3 = 0x8b51;
	  /** <code>GL_ARB_timer_query</code>, <code>GL_VERSION_3_3</code>, <code>GL_EXT_disjoint_timer_query</code><br>Alias for: <code>GL_TIMESTAMP_EXT</code> - CType: int */
	  public static final int GL_TIMESTAMP = 0x8e28;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_MAX_LABEL_LENGTH_KHR</code> - CType: int */
	  public static final int GL_MAX_LABEL_LENGTH = 0x82e8;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_SOURCE_OTHER_KHR</code>, <code>GL_DEBUG_SOURCE_OTHER_ARB</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_OTHER = 0x824b;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_CURRENT_PROGRAM = 0x8b8d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER10_ATI</code>, <code>GL_DRAW_BUFFER10_NV</code>, <code>GL_DRAW_BUFFER10_EXT</code>, <code>GL_DRAW_BUFFER10_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER10 = 0x882f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_SHADER_SOURCE_LENGTH = 0x8b88;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_shadow_samplers</code><br>Alias for: <code>GL_COMPARE_REF_TO_TEXTURE_EXT</code> - CType: int */
	  public static final int GL_COMPARE_REF_TO_TEXTURE = 0x884e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT12_NV</code>, <code>GL_COLOR_ATTACHMENT12_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT12 = 0x8cec;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER14_EXT</code>, <code>GL_DRAW_BUFFER14_ARB</code>, <code>GL_DRAW_BUFFER14_ATI</code>, <code>GL_DRAW_BUFFER14_NV</code> - CType: int */
	  public static final int GL_DRAW_BUFFER14 = 0x8833;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_PUSH_GROUP_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_SHADER_BINARY_FORMATS = 0x8df8;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_OES_texture_3D</code><br>Alias for: <code>GL_TEXTURE_BINDING_3D_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_3D = 0x806a;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_CONTEXT_FLAG_DEBUG_BIT_KHR</code> - CType: int */
	  public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 0x2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8ca4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER8_NV</code>, <code>GL_DRAW_BUFFER8_EXT</code>, <code>GL_DRAW_BUFFER8_ARB</code>, <code>GL_DRAW_BUFFER8_ATI</code> - CType: int */
	  public static final int GL_DRAW_BUFFER8 = 0x882d;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_STACK_UNDERFLOW_KHR</code> - CType: int */
	  public static final int GL_STACK_UNDERFLOW = 0x504;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_OES_texture_border_clamp</code>, <code>GL_NV_texture_border_clamp</code>, <code>GL_EXT_texture_border_clamp</code><br>Alias for: <code>GL_TEXTURE_BORDER_COLOR_OES</code>, <code>GL_TEXTURE_BORDER_COLOR_NV</code>, <code>GL_TEXTURE_BORDER_COLOR_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_BORDER_COLOR = 0x1004;
	  /** <code>GL_EXT_disjoint_timer_query</code><br>Alias for: <code>GL_GPU_DISJOINT_EXT</code> - CType: int */
	  public static final int GL_GPU_DISJOINT = 0x8fbb;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_MAX_VARYING_VECTORS = 0x8dfc;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT9_NV</code>, <code>GL_COLOR_ATTACHMENT9_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT9 = 0x8ce9;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_EXT_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER4_ATI</code>, <code>GL_DRAW_BUFFER4_NV</code>, <code>GL_DRAW_BUFFER4_ARB</code>, <code>GL_DRAW_BUFFER4_EXT</code> - CType: int */
	  public static final int GL_DRAW_BUFFER4 = 0x8829;
	  /** <code>GL_OES_vertex_type_10_10_10_2</code><br>Alias for: <code>GL_INT_10_10_10_2_OES</code> - CType: int */
	  public static final int GL_INT_10_10_10_2 = 0x8df7;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STREAM_DRAW_ARB</code> - CType: int */
	  public static final int GL_STREAM_DRAW = 0x88e0;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_NV_internalformat_sample_query</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_texture_storage_multisample_2d_array</code><br>Alias for: <code>GL_TEXTURE_2D_MULTISAMPLE_ARRAY_OES</code> - CType: int */
	  public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_ATTACHED_SHADERS = 0x8b85;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_LOW_INT = 0x8df3;
	  /** <code>GL_KHR_blend_equation_advanced_coherent</code> - CType: int */
	  public static final int GL_BLEND_ADVANCED_COHERENT_KHR = 0x9285;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 0x910e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8b8a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_STENCIL_BACK_WRITEMASK = 0x8ca5;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_texture_storage_multisample_2d_array</code><br>Alias for: <code>GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY_OES</code> - CType: int */
	  public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_separate_stencil</code><br>Alias for: <code>GL_STENCIL_BACK_FAIL_ATI</code> - CType: int */
	  public static final int GL_STENCIL_BACK_FAIL = 0x8801;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_MEDIUM_FLOAT = 0x8df1;
	  /** <code>GL_DMP_shader_binary</code> - CType: int */
	  public static final int GL_SHADER_BINARY_DMP = 0x9250;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_KHR</code>, <code>GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB</code> - CType: int */
	  public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code>, <code>GL_AMD_debug_output</code><br>Alias for: <code>GL_DEBUG_SEVERITY_HIGH_ARB</code>, <code>GL_DEBUG_SEVERITY_HIGH_KHR</code>, <code>GL_DEBUG_SEVERITY_HIGH_AMD</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_HIGH = 0x9146;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_SOURCE_API_ARB</code>, <code>GL_DEBUG_SOURCE_API_KHR</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_API = 0x8246;
	  /** <code>GL_ARB_get_program_binary</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_get_program_binary</code><br>Alias for: <code>GL_NUM_PROGRAM_BINARY_FORMATS_OES</code> - CType: int */
	  public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 0x87fe;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_2D_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_2D = 0x8b5e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_STENCIL_BACK_REF = 0x8ca3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_INT = 0x1404;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_MEDIUM_INT = 0x8df4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_shadow_samplers</code>, <code>GL_ARB_shadow</code><br>Alias for: <code>GL_TEXTURE_COMPARE_MODE_EXT</code>, <code>GL_TEXTURE_COMPARE_MODE_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COMPARE_MODE = 0x884c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_VALIDATE_STATUS = 0x8b83;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_AMD_debug_output</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_MAX_DEBUG_LOGGED_MESSAGES_AMD</code>, <code>GL_MAX_DEBUG_LOGGED_MESSAGES_KHR</code>, <code>GL_MAX_DEBUG_LOGGED_MESSAGES_ARB</code> - CType: int */
	  public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 0x9144;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT4_EXT</code>, <code>GL_COLOR_ATTACHMENT4_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT4 = 0x8ce4;
	  /** <code>GL_ARB_timer_query</code>, <code>GL_VERSION_3_3</code>, <code>GL_EXT_disjoint_timer_query</code>, <code>GL_EXT_timer_query</code><br>Alias for: <code>GL_TIME_ELAPSED_EXT</code> - CType: int */
	  public static final int GL_TIME_ELAPSED = 0x88bf;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT10_NV</code>, <code>GL_COLOR_ATTACHMENT10_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT10 = 0x8cea;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_SOURCE_SHADER_COMPILER_ARB</code>, <code>GL_DEBUG_SOURCE_SHADER_COMPILER_KHR</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_SEVERITY_NOTIFICATION_KHR</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 0x826b;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_SGIS_multisample</code>, <code>GL_NV_explicit_multisample</code>, <code>GL_EXT_multisample</code><br>Alias for: <code>GL_SAMPLE_MASK_VALUE_SGIS</code>, <code>GL_SAMPLE_MASK_VALUE_NV</code>, <code>GL_SAMPLE_MASK_VALUE_EXT</code> - CType: int */
	  public static final int GL_SAMPLE_MASK_VALUE = 0x8e52;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER0_EXT</code>, <code>GL_DRAW_BUFFER0_ARB</code>, <code>GL_DRAW_BUFFER0_ATI</code>, <code>GL_DRAW_BUFFER0_NV</code> - CType: int */
	  public static final int GL_DRAW_BUFFER0 = 0x8825;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shading_language_100</code><br>Alias for: <code>GL_SHADING_LANGUAGE_VERSION_ARB</code> - CType: int */
	  public static final int GL_SHADING_LANGUAGE_VERSION = 0x8b8c;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_disjoint_timer_query</code>, <code>GL_ARB_occlusion_query</code><br>Alias for: <code>GL_CURRENT_QUERY_EXT</code>, <code>GL_CURRENT_QUERY_ARB</code> - CType: int */
	  public static final int GL_CURRENT_QUERY = 0x8865;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_UNSIGNED_NORMALIZED_EXT</code>, <code>GL_UNSIGNED_NORMALIZED_ARB</code> - CType: int */
	  public static final int GL_UNSIGNED_NORMALIZED = 0x8c17;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_IMG_multisampled_render_to_texture</code><br>Alias for: <code>GL_TEXTURE_SAMPLES_IMG</code> - CType: int */
	  public static final int GL_TEXTURE_SAMPLES = 0x9106;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_AMD_debug_output</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_LOGGED_MESSAGES_KHR</code>, <code>GL_DEBUG_LOGGED_MESSAGES_AMD</code>, <code>GL_DEBUG_LOGGED_MESSAGES_ARB</code> - CType: int */
	  public static final int GL_DEBUG_LOGGED_MESSAGES = 0x9145;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_CUBE_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_CUBE = 0x8b60;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_LOW_FLOAT = 0x8df0;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT1_EXT</code>, <code>GL_COLOR_ATTACHMENT1_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT1 = 0x8ce1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_DELETE_STATUS = 0x8b80;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_disjoint_timer_query</code>, <code>GL_ARB_occlusion_query</code><br>Alias for: <code>GL_QUERY_RESULT_EXT</code>, <code>GL_QUERY_RESULT_ARB</code> - CType: int */
	  public static final int GL_QUERY_RESULT = 0x8866;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_OTHER_ARB</code>, <code>GL_DEBUG_TYPE_OTHER_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_OTHER = 0x8251;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT13_NV</code>, <code>GL_COLOR_ATTACHMENT13_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT13 = 0x8ced;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_EXT_blend_color</code><br>Alias for: <code>GL_BLEND_COLOR_EXT</code> - CType: int */
	  public static final int GL_BLEND_COLOR = 0x8005;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_shader</code><br>Alias for: <code>GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB</code> - CType: int */
	  public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8b4c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8b87;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER13_EXT</code>, <code>GL_DRAW_BUFFER13_ARB</code>, <code>GL_DRAW_BUFFER13_NV</code>, <code>GL_DRAW_BUFFER13_ATI</code> - CType: int */
	  public static final int GL_DRAW_BUFFER13 = 0x8832;
	  /** <code>GL_VERSION_3_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_occlusion_query2</code>, <code>GL_EXT_occlusion_query_boolean</code><br>Alias for: <code>GL_ANY_SAMPLES_PASSED_EXT</code> - CType: int */
	  public static final int GL_ANY_SAMPLES_PASSED = 0x8c2f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER7_NV</code>, <code>GL_DRAW_BUFFER7_ATI</code>, <code>GL_DRAW_BUFFER7_EXT</code>, <code>GL_DRAW_BUFFER7_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER7 = 0x882c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER3_ATI</code>, <code>GL_DRAW_BUFFER3_NV</code>, <code>GL_DRAW_BUFFER3_EXT</code>, <code>GL_DRAW_BUFFER3_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER3 = 0x8828;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_OUTPUT_KHR</code> - CType: int */
	  public static final int GL_DEBUG_OUTPUT = 0x92e0;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT8_EXT</code>, <code>GL_COLOR_ATTACHMENT8_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT8 = 0x8ce8;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_texture_storage_multisample_2d_array</code><br>Alias for: <code>GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY_OES</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_unpack_subimage</code><br>Alias for: <code>GL_UNPACK_SKIP_PIXELS_EXT</code> - CType: int */
	  public static final int GL_UNPACK_SKIP_PIXELS = 0xcf4;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture_type_2_10_10_10_REV</code><br>Alias for: <code>GL_UNSIGNED_INT_2_10_10_10_REV_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_texture_storage_multisample_2d_array</code><br>Alias for: <code>GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105;
	  /** <code>GL_ARB_get_program_binary</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_get_program_binary</code><br>Alias for: <code>GL_PROGRAM_BINARY_LENGTH_OES</code> - CType: int */
	  public static final int GL_PROGRAM_BINARY_LENGTH = 0x8741;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_SHADER_COMPILER = 0x8dfa;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code>, <code>GL_AMD_debug_output</code><br>Alias for: <code>GL_DEBUG_SEVERITY_LOW_KHR</code>, <code>GL_DEBUG_SEVERITY_LOW_ARB</code>, <code>GL_DEBUG_SEVERITY_LOW_AMD</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_LOW = 0x9148;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB</code>, <code>GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824e;
	  /** <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_PORTABILITY_ARB</code>, <code>GL_DEBUG_TYPE_PORTABILITY_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_PORTABILITY = 0x824f;
	  /** <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_PROGRAM_SEPARABLE_EXT</code> - CType: int */
	  public static final int GL_PROGRAM_SEPARABLE = 0x8258;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_BOOL_ARB</code> - CType: int */
	  public static final int GL_BOOL = 0x8b56;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_ERROR_ARB</code>, <code>GL_DEBUG_TYPE_ERROR_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_ERROR = 0x824c;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_unpack_subimage</code><br>Alias for: <code>GL_UNPACK_ROW_LENGTH_EXT</code> - CType: int */
	  public static final int GL_UNPACK_ROW_LENGTH = 0xcf2;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_HIGH_INT = 0x8df5;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT3_EXT</code>, <code>GL_COLOR_ATTACHMENT3_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT3 = 0x8ce3;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_ALL_SHADER_BITS_EXT</code> - CType: long */
	  public static final long GL_ALL_SHADER_BITS = 0xffffffffL;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_MAX_VERTEX_ATTRIBS_ARB</code> - CType: int */
	  public static final int GL_MAX_VERTEX_ATTRIBS = 0x8869;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_color</code><br>Alias for: <code>GL_CONSTANT_ALPHA_EXT</code> - CType: int */
	  public static final int GL_CONSTANT_ALPHA = 0x8003;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_EXT_multisample</code>, <code>GL_NV_explicit_multisample</code>, <code>GL_SGIS_multisample</code><br>Alias for: <code>GL_SAMPLE_MASK_EXT</code>, <code>GL_SAMPLE_MASK_NV</code>, <code>GL_SAMPLE_MASK_SGIS</code> - CType: int */
	  public static final int GL_SAMPLE_MASK = 0x8e51;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_OES_texture_3D</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_MAX_3D_TEXTURE_SIZE_OES</code>, <code>GL_MAX_3D_TEXTURE_SIZE_EXT</code> - CType: int */
	  public static final int GL_MAX_3D_TEXTURE_SIZE = 0x8073;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_LINK_STATUS = 0x8b82;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_SAMPLER_KHR</code> - CType: int */
	  public static final int GL_SAMPLER = 0x82e6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_shader</code><br>Alias for: <code>GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB</code> - CType: int */
	  public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8b4d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_EXT_unpack_subimage</code><br>Alias for: <code>GL_UNPACK_SKIP_ROWS_EXT</code> - CType: int */
	  public static final int GL_UNPACK_SKIP_ROWS = 0xcf3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_shadow_samplers</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_2D_SHADOW_EXT</code>, <code>GL_SAMPLER_2D_SHADOW_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_2D_SHADOW = 0x8b62;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_ACTIVE_UNIFORMS = 0x8b86;
	  /** <code>GL_NV_internalformat_sample_query</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_TEXTURE_2D_MULTISAMPLE = 0x9100;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_INFO_LOG_LENGTH = 0x8b84;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8dfb;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER6_EXT</code>, <code>GL_DRAW_BUFFER6_ARB</code>, <code>GL_DRAW_BUFFER6_NV</code>, <code>GL_DRAW_BUFFER6_ATI</code> - CType: int */
	  public static final int GL_DRAW_BUFFER6 = 0x882b;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENT = 0x1902;
	  /** <code>GL_KHR_robustness</code>, <code>GL_KHR_robustness</code>, <code>GL_EXT_robustness</code><br>Alias for: <code>GL_CONTEXT_ROBUST_ACCESS_KHR</code>, <code>GL_CONTEXT_ROBUST_ACCESS_EXT</code> - CType: int */
	  public static final int GL_CONTEXT_ROBUST_ACCESS = 0x90f3;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_shadow_samplers</code>, <code>GL_ARB_shadow</code><br>Alias for: <code>GL_TEXTURE_COMPARE_FUNC_EXT</code>, <code>GL_TEXTURE_COMPARE_FUNC_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COMPARE_FUNC = 0x884d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER12_ATI</code>, <code>GL_DRAW_BUFFER12_EXT</code>, <code>GL_DRAW_BUFFER12_ARB</code>, <code>GL_DRAW_BUFFER12_NV</code> - CType: int */
	  public static final int GL_DRAW_BUFFER12 = 0x8831;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_rg</code><br>Alias for: <code>GL_RG_EXT</code> - CType: int */
	  public static final int GL_RG = 0x8227;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_CALLBACK_USER_PARAM_KHR</code>, <code>GL_DEBUG_CALLBACK_USER_PARAM_ARB</code> - CType: int */
	  public static final int GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_sparse_texture</code>, <code>GL_VERSION_1_2</code>, <code>GL_OES_texture_3D</code>, <code>GL_EXT_texture3D</code><br>Alias for: <code>GL_TEXTURE_3D_OES</code>, <code>GL_TEXTURE_3D_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_3D = 0x806f;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_EXT_disjoint_timer_query</code>, <code>GL_ARB_occlusion_query</code><br>Alias for: <code>GL_QUERY_COUNTER_BITS_EXT</code>, <code>GL_QUERY_COUNTER_BITS_ARB</code> - CType: int */
	  public static final int GL_QUERY_COUNTER_BITS = 0x8864;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_PROGRAM_KHR</code> - CType: int */
	  public static final int GL_PROGRAM = 0x82e2;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_packed_pixels</code>, <code>GL_OES_vertex_type_10_10_10_2</code><br>Alias for: <code>GL_UNSIGNED_INT_10_10_10_2_EXT</code>, <code>GL_UNSIGNED_INT_10_10_10_2_OES</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_10_10_10_2 = 0x8df6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_BOOL_VEC4_ARB</code> - CType: int */
	  public static final int GL_BOOL_VEC4 = 0x8b59;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_BOOL_VEC3_ARB</code> - CType: int */
	  public static final int GL_BOOL_VEC3 = 0x8b58;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_BOOL_VEC2_ARB</code> - CType: int */
	  public static final int GL_BOOL_VEC2 = 0x8b57;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_BUFFER_KHR</code> - CType: int */
	  public static final int GL_BUFFER = 0x82e0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_SHADER_TYPE = 0x8b4f;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_FRAGMENT_SHADER_BIT_EXT</code> - CType: int */
	  public static final int GL_FRAGMENT_SHADER_BIT = 0x2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code> - CType: int */
	  public static final int GL_ACTIVE_ATTRIBUTES = 0x8b89;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_fragment_program</code>, <code>GL_ARB_fragment_program</code><br>Alias for: <code>GL_MAX_TEXTURE_IMAGE_UNITS_NV</code>, <code>GL_MAX_TEXTURE_IMAGE_UNITS_ARB</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 0x9109;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_INT_VEC4_ARB</code> - CType: int */
	  public static final int GL_INT_VEC4 = 0x8b55;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_INT_VEC2_ARB</code> - CType: int */
	  public static final int GL_INT_VEC2 = 0x8b53;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_INT_VEC3_ARB</code> - CType: int */
	  public static final int GL_INT_VEC3 = 0x8b54;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_MARKER_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_MARKER = 0x8268;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_color</code><br>Alias for: <code>GL_CONSTANT_COLOR_EXT</code> - CType: int */
	  public static final int GL_CONSTANT_COLOR = 0x8001;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_disjoint_timer_query</code>, <code>GL_ARB_occlusion_query</code><br>Alias for: <code>GL_QUERY_RESULT_AVAILABLE_EXT</code>, <code>GL_QUERY_RESULT_AVAILABLE_ARB</code> - CType: int */
	  public static final int GL_QUERY_RESULT_AVAILABLE = 0x8867;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_TYPE_POP_GROUP_KHR</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_POP_GROUP = 0x826a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT14_NV</code>, <code>GL_COLOR_ATTACHMENT14_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT14 = 0x8cee;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT7_EXT</code>, <code>GL_COLOR_ATTACHMENT7_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT7 = 0x8ce7;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_NUM_SHADER_BINARY_FORMATS = 0x8df9;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_STACK_OVERFLOW_KHR</code> - CType: int */
	  public static final int GL_STACK_OVERFLOW = 0x503;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_SOURCE_THIRD_PARTY_ARB</code>, <code>GL_DEBUG_SOURCE_THIRD_PARTY_KHR</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 0x8249;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_3</code>, <code>GL_EXT_texture_border_clamp</code>, <code>GL_ARB_texture_border_clamp</code>, <code>GL_NV_texture_border_clamp</code>, <code>GL_OES_texture_border_clamp</code>, <code>GL_SGIS_texture_border_clamp</code><br>Alias for: <code>GL_CLAMP_TO_BORDER_EXT</code>, <code>GL_CLAMP_TO_BORDER_ARB</code>, <code>GL_CLAMP_TO_BORDER_NV</code>, <code>GL_CLAMP_TO_BORDER_OES</code>, <code>GL_CLAMP_TO_BORDER_SGIS</code> - CType: int */
	  public static final int GL_CLAMP_TO_BORDER = 0x812d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_EXT_texture3D</code>, <code>GL_OES_texture_3D</code><br>Alias for: <code>GL_TEXTURE_WRAP_R_EXT</code>, <code>GL_TEXTURE_WRAP_R_OES</code> - CType: int */
	  public static final int GL_TEXTURE_WRAP_R = 0x8072;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_SAMPLER_2D_MULTISAMPLE = 0x9108;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT6_EXT</code>, <code>GL_COLOR_ATTACHMENT6_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT6 = 0x8ce6;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_explicit_multisample</code><br>Alias for: <code>GL_SAMPLE_POSITION_NV</code> - CType: int */
	  public static final int GL_SAMPLE_POSITION = 0x8e50;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER2_NV</code>, <code>GL_DRAW_BUFFER2_ATI</code>, <code>GL_DRAW_BUFFER2_EXT</code>, <code>GL_DRAW_BUFFER2_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER2 = 0x8827;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_fbo_color_attachments</code><br>Alias for: <code>GL_MAX_COLOR_ATTACHMENTS_EXT</code>, <code>GL_MAX_COLOR_ATTACHMENTS_NV</code> - CType: int */
	  public static final int GL_MAX_COLOR_ATTACHMENTS = 0x8cdf;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_ARB_debug_output</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_DEBUG_CALLBACK_FUNCTION_ARB</code>, <code>GL_DEBUG_CALLBACK_FUNCTION_KHR</code> - CType: int */
	  public static final int GL_DEBUG_CALLBACK_FUNCTION = 0x8244;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER9_NV</code>, <code>GL_DRAW_BUFFER9_EXT</code>, <code>GL_DRAW_BUFFER9_ARB</code>, <code>GL_DRAW_BUFFER9_ATI</code> - CType: int */
	  public static final int GL_DRAW_BUFFER9 = 0x882e;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_sRGB</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211;
	  /** <code>GL_ARB_ES3_compatibility</code>, <code>GL_VERSION_4_3</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_occlusion_query_boolean</code><br>Alias for: <code>GL_ANY_SAMPLES_PASSED_CONSERVATIVE_EXT</code> - CType: int */
	  public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8d6a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER11_ATI</code>, <code>GL_DRAW_BUFFER11_EXT</code>, <code>GL_DRAW_BUFFER11_ARB</code>, <code>GL_DRAW_BUFFER11_NV</code> - CType: int */
	  public static final int GL_DRAW_BUFFER11 = 0x8830;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_color</code><br>Alias for: <code>GL_ONE_MINUS_CONSTANT_ALPHA_EXT</code> - CType: int */
	  public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_separate_stencil</code><br>Alias for: <code>GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI</code> - CType: int */
	  public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_TYPE_PERFORMANCE_KHR</code>, <code>GL_DEBUG_TYPE_PERFORMANCE_ARB</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_PERFORMANCE = 0x8250;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_OES_standard_derivatives</code>, <code>GL_ARB_fragment_shader</code><br>Alias for: <code>GL_FRAGMENT_SHADER_DERIVATIVE_HINT_OES</code>, <code>GL_FRAGMENT_SHADER_DERIVATIVE_HINT_ARB</code> - CType: int */
	  public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8b8b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT2_EXT</code>, <code>GL_COLOR_ATTACHMENT2_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT2 = 0x8ce2;
	  /** <code>GL_ARB_separate_shader_objects</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_VERSION_4_1</code>, <code>GL_EXT_separate_shader_objects</code><br>Alias for: <code>GL_PROGRAM_PIPELINE_BINDING_EXT</code> - CType: int */
	  public static final int GL_PROGRAM_PIPELINE_BINDING = 0x825a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_MAX_DRAW_BUFFERS_NV</code>, <code>GL_MAX_DRAW_BUFFERS_EXT</code>, <code>GL_MAX_DRAW_BUFFERS_ARB</code>, <code>GL_MAX_DRAW_BUFFERS_ATI</code> - CType: int */
	  public static final int GL_MAX_DRAW_BUFFERS = 0x8824;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ATI_fragment_shader</code>, <code>GL_ARB_fragment_shader</code><br>Alias for: <code>GL_FRAGMENT_SHADER_ATI</code>, <code>GL_FRAGMENT_SHADER_ARB</code> - CType: int */
	  public static final int GL_FRAGMENT_SHADER = 0x8b30;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_OUTPUT_SYNCHRONOUS_KHR</code>, <code>GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB</code> - CType: int */
	  public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_PROGRAM_PIPELINE_KHR</code> - CType: int */
	  public static final int GL_PROGRAM_PIPELINE = 0x82e4;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_SOURCE_APPLICATION_KHR</code>, <code>GL_DEBUG_SOURCE_APPLICATION_ARB</code> - CType: int */
	  public static final int GL_DEBUG_SOURCE_APPLICATION = 0x824a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_NV_explicit_multisample</code><br>Alias for: <code>GL_MAX_SAMPLE_MASK_WORDS_NV</code> - CType: int */
	  public static final int GL_MAX_SAMPLE_MASK_WORDS = 0x8e59;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code><br>Alias for: <code>GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_KHR</code>, <code>GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB</code> - CType: int */
	  public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824d;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code> - CType: int */
	  public static final int GL_HIGH_FLOAT = 0x8df2;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_NV_draw_buffers</code><br>Alias for: <code>GL_COLOR_ATTACHMENT11_EXT</code>, <code>GL_COLOR_ATTACHMENT11_NV</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT11 = 0x8ceb;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_OES_texture_3D</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_SAMPLER_3D_OES</code>, <code>GL_SAMPLER_3D_ARB</code> - CType: int */
	  public static final int GL_SAMPLER_3D = 0x8b5f;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_MAX_DEBUG_GROUP_STACK_DEPTH_KHR</code> - CType: int */
	  public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 0x826c;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code>, <code>GL_OES_texture_storage_multisample_2d_array</code><br>Alias for: <code>GL_SAMPLER_2D_MULTISAMPLE_ARRAY_OES</code> - CType: int */
	  public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910b;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT15_NV</code>, <code>GL_COLOR_ATTACHMENT15_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT15 = 0x8cef;
	  /** <code>GL_KHR_debug</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_4_3</code>, <code>GL_KHR_debug</code>, <code>GL_ARB_debug_output</code>, <code>GL_AMD_debug_output</code><br>Alias for: <code>GL_DEBUG_SEVERITY_MEDIUM_KHR</code>, <code>GL_DEBUG_SEVERITY_MEDIUM_ARB</code>, <code>GL_DEBUG_SEVERITY_MEDIUM_AMD</code> - CType: int */
	  public static final int GL_DEBUG_SEVERITY_MEDIUM = 0x9147;
	  /** <code>GL_ARB_get_program_binary</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_get_program_binary</code><br>Alias for: <code>GL_PROGRAM_BINARY_FORMATS_OES</code> - CType: int */
	  public static final int GL_PROGRAM_BINARY_FORMATS = 0x87ff;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_MAT4_ARB</code> - CType: int */
	  public static final int GL_FLOAT_MAT4 = 0x8b5c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_MAT3_ARB</code> - CType: int */
	  public static final int GL_FLOAT_MAT3 = 0x8b5b;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_color</code><br>Alias for: <code>GL_ONE_MINUS_CONSTANT_COLOR_EXT</code> - CType: int */
	  public static final int GL_ONE_MINUS_CONSTANT_COLOR = 0x8002;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_shader_objects</code><br>Alias for: <code>GL_FLOAT_MAT2_ARB</code> - CType: int */
	  public static final int GL_FLOAT_MAT2 = 0x8b5a;
	  /** <code>GL_ES_VERSION_3_1</code>, <code>GL_ARB_texture_multisample</code>, <code>GL_VERSION_3_2</code> - CType: int */
	  public static final int GL_MAX_INTEGER_SAMPLES = 0x9110;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_NV_blend_equation_advanced</code>, <code>GL_EXT_texture_rg</code><br>Alias for: <code>GL_RED_NV</code>, <code>GL_RED_EXT</code> - CType: int */
	  public static final int GL_RED = 0x1903;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER5_NV</code>, <code>GL_DRAW_BUFFER5_ATI</code>, <code>GL_DRAW_BUFFER5_EXT</code>, <code>GL_DRAW_BUFFER5_ARB</code> - CType: int */
	  public static final int GL_DRAW_BUFFER5 = 0x882a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_ARB_vertex_program</code><br>Alias for: <code>GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_ARB_draw_buffers</code>, <code>GL_NV_draw_buffers</code>, <code>GL_ATI_draw_buffers</code><br>Alias for: <code>GL_DRAW_BUFFER15_EXT</code>, <code>GL_DRAW_BUFFER15_ARB</code>, <code>GL_DRAW_BUFFER15_NV</code>, <code>GL_DRAW_BUFFER15_ATI</code> - CType: int */
	  public static final int GL_DRAW_BUFFER15 = 0x8834;

}
