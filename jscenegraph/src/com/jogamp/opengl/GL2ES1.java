package com.jogamp.opengl;

import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;

public interface GL2ES1 extends GL, GLPointerFunc, GLLightingFunc, GLMatrixFunc {


	  /** <code>GL_VERSION_1_4</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_SGIS_point_parameters</code>, <code>GL_EXT_point_parameters</code>, <code>GL_ARB_point_parameters</code><br>Alias for: <code>GL_POINT_SIZE_MAX_SGIS</code>, <code>GL_POINT_SIZE_MAX_EXT</code>, <code>GL_POINT_SIZE_MAX_ARB</code> - CType: int */
	  public static final int GL_POINT_SIZE_MAX = 0x8127;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC2_ALPHA = 0x858a;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MODELVIEW_STACK_DEPTH = 0xba3;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_NORMAL_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 0x8897;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_COLOR_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_POINTER = 0x8090;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_PREVIOUS_EXT</code>, <code>GL_PREVIOUS_ARB</code> - CType: int */
	  public static final int GL_PREVIOUS = 0x8578;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND0_RGB_EXT</code>, <code>GL_OPERAND0_RGB_ARB</code> - CType: int */
	  public static final int GL_OPERAND0_RGB = 0x8590;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_VERTEX_ARRAY_SIZE_EXT</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_SIZE = 0x807a;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_HINT = 0xc54;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_TEXTURE_COORDS = 0xb03;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_COLOR = 0xb00;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_VERTEX_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_TYPE = 0x807b;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_MAX_TEXTURE_UNITS_ARB</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_UNITS = 0x84e2;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_VERTEX_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 0x8896;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_rescale_normal</code><br>Alias for: <code>GL_RESCALE_NORMAL_EXT</code> - CType: int */
	  public static final int GL_RESCALE_NORMAL = 0x803a;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_VERTEX_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_STRIDE = 0x807c;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_COLOR_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 0x8898;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE0_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE0 = 0x3000;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE1_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE1 = 0x3001;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE2_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE2 = 0x3002;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE3_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE3 = 0x3003;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_RGB_SCALE_EXT</code>, <code>GL_RGB_SCALE_ARB</code> - CType: int */
	  public static final int GL_RGB_SCALE = 0x8573;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE4_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE4 = 0x3004;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_CLIP_PLANE5_IMG</code> - CType: int */
	  public static final int GL_CLIP_PLANE5 = 0x3005;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_QCOM_alpha_test</code><br>Alias for: <code>GL_ALPHA_TEST_QCOM</code> - CType: int */
	  public static final int GL_ALPHA_TEST = 0xbc0;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_COLOR_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_STRIDE = 0x8083;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_LIGHTS = 0xd31;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_COMBINE_RGB_EXT</code>, <code>GL_COMBINE_RGB_ARB</code> - CType: int */
	  public static final int GL_COMBINE_RGB = 0x8571;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PROJECTION_STACK_DEPTH = 0xba4;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND1_ALPHA_EXT</code>, <code>GL_OPERAND1_ALPHA_ARB</code> - CType: int */
	  public static final int GL_OPERAND1_ALPHA = 0x8599;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_NORMAL_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_POINTER = 0x808f;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_sRGB</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_CLIENT_ACTIVE_TEXTURE_ARB</code> - CType: int */
	  public static final int GL_CLIENT_ACTIVE_TEXTURE = 0x84e1;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_STACK_DEPTH = 0xba5;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_PERSPECTIVE_CORRECTION_HINT = 0xc50;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIGHT_MODEL_TWO_SIDE = 0xb52;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_TEXTURE_COORD_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 0x8089;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC0_RGB = 0x8580;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_MODE = 0xb65;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_QCOM_alpha_test</code><br>Alias for: <code>GL_ALPHA_TEST_FUNC_QCOM</code> - CType: int */
	  public static final int GL_ALPHA_TEST_FUNC = 0xbc1;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_IMG_user_clip_plane</code><br>Alias for: <code>GL_MAX_CLIP_PLANES_IMG</code> - CType: int */
	  public static final int GL_MAX_CLIP_PLANES = 0xd32;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_VERTEX_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_VERTEX_ARRAY_POINTER = 0x808e;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_TEXTURE_COORD_ARRAY_SIZE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 0x8088;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MODULATE = 0x2100;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND2_ALPHA_EXT</code>, <code>GL_OPERAND2_ALPHA_ARB</code> - CType: int */
	  public static final int GL_OPERAND2_ALPHA = 0x859a;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_STACK_DEPTH = 0xd39;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_ENV = 0x2300;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_NORMAL_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_STRIDE = 0x807f;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC1_RGB = 0x8581;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_COLOR = 0xb66;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_point_parameters</code>, <code>GL_SGIS_point_parameters</code>, <code>GL_ARB_point_parameters</code><br>Alias for: <code>GL_POINT_SIZE_MIN_EXT</code>, <code>GL_POINT_SIZE_MIN_SGIS</code>, <code>GL_POINT_SIZE_MIN_ARB</code> - CType: int */
	  public static final int GL_POINT_SIZE_MIN = 0x8126;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND0_ALPHA_EXT</code>, <code>GL_OPERAND0_ALPHA_ARB</code> - CType: int */
	  public static final int GL_OPERAND0_ALPHA = 0x8598;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_SMOOTH = 0xb10;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_EXP2 = 0x801;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_EXP = 0x800;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_COLOR_ARRAY_SIZE_EXT</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_SIZE = 0x8081;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_LIGHT_MODEL_AMBIENT = 0xb53;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_TEXTURE_COORD_ARRAY_POINTER_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 0x8092;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_CURRENT_NORMAL = 0xb02;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_PROJECTION_STACK_DEPTH = 0xd38;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code>, <code>GL_NV_path_rendering</code>, <code>GL_NV_register_combiners</code><br>Alias for: <code>GL_PRIMARY_COLOR_EXT</code>, <code>GL_PRIMARY_COLOR_ARB</code>, <code>GL_PRIMARY_COLOR_NV</code> - CType: int */
	  public static final int GL_PRIMARY_COLOR = 0x8577;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_ENV_COLOR = 0x2201;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC2_RGB = 0x8582;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_INTERPOLATE_EXT</code>, <code>GL_INTERPOLATE_ARB</code> - CType: int */
	  public static final int GL_INTERPOLATE = 0x8575;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_DECAL = 0x2101;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_SRC1_ALPHA_EXT</code> - CType: int */
	  public static final int GL_SRC1_ALPHA = 0x8589;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_ENV_MODE = 0x2200;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_QCOM_alpha_test</code><br>Alias for: <code>GL_ALPHA_TEST_REF_QCOM</code> - CType: int */
	  public static final int GL_ALPHA_TEST_REF = 0xbc2;
	  /** <code>GL_KHR_robustness</code>, <code>GL_KHR_robustness</code>, <code>GL_EXT_robustness</code><br>Alias for: <code>GL_CONTEXT_ROBUST_ACCESS_KHR</code>, <code>GL_CONTEXT_ROBUST_ACCESS_EXT</code> - CType: int */
	  public static final int GL_CONTEXT_ROBUST_ACCESS = 0x90f3;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 0x889a;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_point_parameters</code><br>Alias for: <code>GL_POINT_DISTANCE_ATTENUATION_ARB</code> - CType: int */
	  public static final int GL_POINT_DISTANCE_ATTENUATION = 0x8129;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_ALPHA_SCALE = 0xd1c;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code>, <code>GL_NV_path_rendering</code><br>Alias for: <code>GL_CONSTANT_EXT</code>, <code>GL_CONSTANT_ARB</code>, <code>GL_CONSTANT_NV</code> - CType: int */
	  public static final int GL_CONSTANT = 0x8576;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_COLOR_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_COLOR_ARRAY_TYPE = 0x8082;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_texture_env_dot3</code>, <code>GL_EXT_texture_env_dot3</code><br>Alias for: <code>GL_DOT3_RGB_ARB</code>, <code>GL_DOT3_RGB_EXT</code> - CType: int */
	  public static final int GL_DOT3_RGB = 0x86ae;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_POINT_SMOOTH_HINT = 0xc51;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_SHADE_MODEL = 0xb54;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND1_RGB_EXT</code>, <code>GL_OPERAND1_RGB_ARB</code> - CType: int */
	  public static final int GL_OPERAND1_RGB = 0x8591;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_STACK_OVERFLOW_KHR</code> - CType: int */
	  public static final int GL_STACK_OVERFLOW = 0x503;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_texture_env_dot3</code>, <code>GL_EXT_texture_env_dot3</code>, <code>GL_IMG_texture_env_enhanced_fixed_function</code><br>Alias for: <code>GL_DOT3_RGBA_ARB</code>, <code>GL_DOT3_RGBA_EXT</code>, <code>GL_DOT3_RGBA_IMG</code> - CType: int */
	  public static final int GL_DOT3_RGBA = 0x86af;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_END = 0xb64;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_OES_point_sprite</code>, <code>GL_NV_point_sprite</code>, <code>GL_ARB_point_sprite</code><br>Alias for: <code>GL_COORD_REPLACE_OES</code>, <code>GL_COORD_REPLACE_NV</code>, <code>GL_COORD_REPLACE_ARB</code> - CType: int */
	  public static final int GL_COORD_REPLACE = 0x8862;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_SUBTRACT_ARB</code> - CType: int */
	  public static final int GL_SUBTRACT = 0x84e7;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_TEXTURE_COORD_ARRAY_STRIDE_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 0x808a;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_array</code><br>Alias for: <code>GL_NORMAL_ARRAY_TYPE_EXT</code> - CType: int */
	  public static final int GL_NORMAL_ARRAY_TYPE = 0x807e;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC0_ALPHA = 0x8588;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_COMBINE_EXT</code>, <code>GL_COMBINE_ARB</code> - CType: int */
	  public static final int GL_COMBINE = 0x8570;
	  /** <code>GL_VERSION_2_0</code>, <code>GL_ARB_point_sprite</code>, <code>GL_NV_point_sprite</code>, <code>GL_OES_point_sprite</code><br>Alias for: <code>GL_POINT_SPRITE_ARB</code>, <code>GL_POINT_SPRITE_NV</code>, <code>GL_POINT_SPRITE_OES</code> - CType: int */
	  public static final int GL_POINT_SPRITE = 0x8861;
	  /** <code>GL_ES_VERSION_3_2</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_KHR_debug</code><br>Alias for: <code>GL_STACK_UNDERFLOW_KHR</code> - CType: int */
	  public static final int GL_STACK_UNDERFLOW = 0x504;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_ATI_fragment_shader</code><br>Alias for: <code>GL_ADD_ATI</code> - CType: int */
	  public static final int GL_ADD = 0x104;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_DENSITY = 0xb62;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_OPERAND2_RGB_EXT</code>, <code>GL_OPERAND2_RGB_ARB</code> - CType: int */
	  public static final int GL_OPERAND2_RGB = 0x8592;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_SGIS_generate_mipmap</code><br>Alias for: <code>GL_GENERATE_MIPMAP_SGIS</code> - CType: int */
	  public static final int GL_GENERATE_MIPMAP = 0x8191;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG = 0xb60;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_FOG_START = 0xb63;
	  /** <code>GL_VERSION_ES_1_0</code>, <code>GL_VERSION_1_0</code> - CType: int */
	  public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 0xd36;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_COMBINE_ALPHA_EXT</code>, <code>GL_COMBINE_ALPHA_ARB</code> - CType: int */
	  public static final int GL_COMBINE_ALPHA = 0x8572;
	  /** <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture_env_combine</code>, <code>GL_ARB_texture_env_combine</code><br>Alias for: <code>GL_ADD_SIGNED_EXT</code>, <code>GL_ADD_SIGNED_ARB</code> - CType: int */
	  public static final int GL_ADD_SIGNED = 0x8574;

}
