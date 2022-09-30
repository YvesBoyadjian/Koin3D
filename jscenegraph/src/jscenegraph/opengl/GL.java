/**
 * 
 */
package jscenegraph.opengl;

/**
 * @author Yves Boyadjian
 *
 */
public interface GL {


	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ONE_MINUS_SRC_COLOR = 0x301;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SCISSOR_TEST = 0xc11;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ONE_MINUS_DST_COLOR = 0x307;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_ANGLE_framebuffer_multisample</code>, <code>GL_IMG_multisampled_render_to_texture</code>, <code>GL_EXT_multisampled_render_to_texture</code>, <code>GL_EXT_framebuffer_multisample</code>, <code>GL_NV_framebuffer_multisample</code><br>Alias for: <code>GL_RENDERBUFFER_SAMPLES_APPLE</code>, <code>GL_RENDERBUFFER_SAMPLES_ANGLE</code>, <code>GL_RENDERBUFFER_SAMPLES_IMG</code>, <code>GL_RENDERBUFFER_SAMPLES_EXT</code>, <code>GL_RENDERBUFFER_SAMPLES_NV</code> - CType: int */
	  public static final int GL_RENDERBUFFER_SAMPLES = 0x8cab;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8cd7;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_INVALID_FRAMEBUFFER_OPERATION_EXT</code>, <code>GL_INVALID_FRAMEBUFFER_OPERATION_OES</code> - CType: int */
	  public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_TEST = 0xb71;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_INVALID_OPERATION = 0x502;
	  /** <code>GL_EXT_texture_compression_s3tc</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83f2;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_RGB32F_EXT</code>, <code>GL_RGB32F_ARB</code> - CType: int */
	  public static final int GL_RGB32F = 0x8815;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_DYNAMIC_DRAW_ARB</code> - CType: int */
	  public static final int GL_DYNAMIC_DRAW = 0x88e8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_BITS = 0xd56;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_robustness</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code><br>Alias for: <code>GL_UNKNOWN_CONTEXT_RESET_KHR</code>, <code>GL_UNKNOWN_CONTEXT_RESET_EXT</code>, <code>GL_UNKNOWN_CONTEXT_RESET_ARB</code> - CType: int */
	  public static final int GL_UNKNOWN_CONTEXT_RESET = 0x8255;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_BUFFER_BIT = 0x400;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_framebuffer_multisample</code>, <code>GL_IMG_multisampled_render_to_texture</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_EXT_multisampled_render_to_texture</code>, <code>GL_EXT_framebuffer_multisample</code>, <code>GL_ANGLE_framebuffer_multisample</code><br>Alias for: <code>GL_MAX_SAMPLES_NV</code>, <code>GL_MAX_SAMPLES_IMG</code>, <code>GL_MAX_SAMPLES_APPLE</code>, <code>GL_MAX_SAMPLES_EXT</code>, <code>GL_MAX_SAMPLES_ANGLE</code> - CType: int */
	  public static final int GL_MAX_SAMPLES = 0x8d57;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8cd3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FRONT_FACE = 0xb46;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE0_ARB</code> - CType: int */
	  public static final int GL_TEXTURE0 = 0x84c0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE4_ARB</code> - CType: int */
	  public static final int GL_TEXTURE4 = 0x84c4;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE3_ARB</code> - CType: int */
	  public static final int GL_TEXTURE3 = 0x84c3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE2_ARB</code> - CType: int */
	  public static final int GL_TEXTURE2 = 0x84c2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE1_ARB</code> - CType: int */
	  public static final int GL_TEXTURE1 = 0x84c1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE8_ARB</code> - CType: int */
	  public static final int GL_TEXTURE8 = 0x84c8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE7_ARB</code> - CType: int */
	  public static final int GL_TEXTURE7 = 0x84c7;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE6_ARB</code> - CType: int */
	  public static final int GL_TEXTURE6 = 0x84c6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE5_ARB</code> - CType: int */
	  public static final int GL_TEXTURE5 = 0x84c5;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE9_ARB</code> - CType: int */
	  public static final int GL_TEXTURE9 = 0x84c9;
	  /** <code>GL_EXT_texture_compression_s3tc</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83f3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_OES_required_internalformat</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGB5_A1_OES</code>, <code>GL_RGB5_A1_EXT</code> - CType: int */
	  public static final int GL_RGB5_A1 = 0x8057;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_INCR = 0x1e02;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DST_ALPHA = 0x304;
	  /** <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_INVALIDATE_BUFFER_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 0x8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC_COLOR = 0x300;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NOTEQUAL = 0x205;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_CULL_FACE_MODE = 0xb45;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FASTEST = 0x1101;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_R16F_EXT</code> - CType: int */
	  public static final int GL_R16F = 0x822d;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ALIASED_POINT_SIZE_RANGE = 0x846d;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SHORT = 0x1402;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8cd2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ONE_MINUS_DST_ALPHA = 0x305;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DST_COLOR = 0x306;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LESS = 0x201;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_shader</code><br>Alias for: <code>GL_ONE_EXT</code> - CType: int */
	  public static final int GL_ONE = 0x1;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_EXT_sRGB</code><br>Alias for: <code>GL_SRGB_EXT</code> - CType: int */
	  public static final int GL_SRGB = 0x8c40;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_STENCIL_SIZE_EXT</code>, <code>GL_RENDERBUFFER_STENCIL_SIZE_OES</code> - CType: int */
	  public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8d55;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TRIANGLES = 0x4;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_OES_fixed_point</code><br>Alias for: <code>GL_FIXED_OES</code> - CType: int */
	  public static final int GL_FIXED = 0x140c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_RGBA = 0x1908;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NEAREST_MIPMAP_LINEAR = 0x2702;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_EXTENSIONS = 0x1f03;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_NV_framebuffer_blit</code>, <code>GL_ANGLE_framebuffer_blit</code>, <code>GL_EXT_framebuffer_blit</code><br>Alias for: <code>GL_DRAW_FRAMEBUFFER_BINDING_APPLE</code>, <code>GL_DRAW_FRAMEBUFFER_BINDING_NV</code>, <code>GL_DRAW_FRAMEBUFFER_BINDING_ANGLE</code>, <code>GL_DRAW_FRAMEBUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8ca6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_BYTE = 0x1400;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_SGIS_generate_mipmap</code><br>Alias for: <code>GL_GENERATE_MIPMAP_HINT_SGIS</code> - CType: int */
	  public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_EQUAL = 0x202;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ONE_MINUS_SRC_ALPHA = 0x303;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_UNITS = 0x2a00;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_RGBA4_EXT</code>, <code>GL_RGBA4_OES</code> - CType: int */
	  public static final int GL_RGBA4 = 0x8056;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_OES_blend_equation_separate</code>, <code>GL_EXT_blend_equation_separate</code><br>Alias for: <code>GL_BLEND_EQUATION_RGB_OES</code>, <code>GL_BLEND_EQUATION_RGB_EXT</code> - CType: int */
	  public static final int GL_BLEND_EQUATION_RGB = 0x8009;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code>, <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_RGB16F_EXT</code>, <code>GL_RGB16F_ARB</code> - CType: int */
	  public static final int GL_RGB16F = 0x881b;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multisample</code><br>Alias for: <code>GL_SAMPLE_COVERAGE_INVERT_ARB</code> - CType: int */
	  public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80ab;
	  /** <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_INVALIDATE_RANGE_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_INVALIDATE_RANGE_BIT = 0x4;
	  /** <code>GL_EXT_texture_filter_anisotropic</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT = 0x84ff;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8cd0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_VERSION = 0x1f02;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_read_format</code><br>Alias for: <code>GL_IMPLEMENTATION_COLOR_READ_FORMAT_OES</code> - CType: int */
	  public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8b9b;
	  /** <code>GL_EXT_read_format_bgra</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV_EXT = 0x8366;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DITHER = 0xbd0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8cd9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_INVALID_ENUM = 0x500;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_OES_texture_cube_map</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_OES</code>, <code>GL_TEXTURE_CUBE_MAP_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP = 0x8513;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ALWAYS = 0x207;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_INVERT = 0x150a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_ANGLE_framebuffer_blit</code>, <code>GL_NV_framebuffer_blit</code>, <code>GL_EXT_framebuffer_blit</code><br>Alias for: <code>GL_READ_FRAMEBUFFER_BINDING_APPLE</code>, <code>GL_READ_FRAMEBUFFER_BINDING_ANGLE</code>, <code>GL_READ_FRAMEBUFFER_BINDING_NV</code>, <code>GL_READ_FRAMEBUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_READ_FRAMEBUFFER_BINDING = 0x8caa;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LUMINANCE = 0x1909;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_2_0</code>, <code>GL_EXT_blend_equation_separate</code>, <code>GL_OES_blend_equation_separate</code><br>Alias for: <code>GL_BLEND_EQUATION_ALPHA_EXT</code>, <code>GL_BLEND_EQUATION_ALPHA_OES</code> - CType: int */
	  public static final int GL_BLEND_EQUATION_ALPHA = 0x883d;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_packed_depth_stencil</code>, <code>GL_OES_packed_depth_stencil</code><br>Alias for: <code>GL_DEPTH24_STENCIL8_EXT</code>, <code>GL_DEPTH24_STENCIL8_OES</code> - CType: int */
	  public static final int GL_DEPTH24_STENCIL8 = 0x88f0;
	  /** <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_FLUSH_EXPLICIT_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 0x10;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_IMG_multisampled_render_to_texture</code>, <code>GL_ANGLE_framebuffer_multisample</code>, <code>GL_EXT_multisampled_render_to_texture</code>, <code>GL_EXT_framebuffer_multisample</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_NV_framebuffer_multisample</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_IMG</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_ANGLE</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_APPLE</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_NV</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8d56;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture_rg</code><br>Alias for: <code>GL_RG8_EXT</code> - CType: int */
	  public static final int GL_RG8 = 0x822b;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_REPLACE_EXT</code> - CType: int */
	  public static final int GL_REPLACE = 0x1e01;
	  /** <code>GL_EXT_buffer_storage</code>, <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_WRITE_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_WRITE_BIT = 0x2;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_EXT_sRGB</code><br>Alias for: <code>GL_SRGB8_ALPHA8_EXT</code> - CType: int */
	  public static final int GL_SRGB8_ALPHA8 = 0x8c43;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_MAX_TEXTURE_SIZE = 0xd33;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_WIDTH_EXT</code>, <code>GL_RENDERBUFFER_WIDTH_OES</code> - CType: int */
	  public static final int GL_RENDERBUFFER_WIDTH = 0x8d42;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NEVER = 0x200;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FALSE = 0x0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_ARB_depth_texture</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_OES_required_internalformat</code>, <code>GL_SGIX_depth_texture</code><br>Alias for: <code>GL_DEPTH_COMPONENT16_ARB</code>, <code>GL_DEPTH_COMPONENT16_OES</code>, <code>GL_DEPTH_COMPONENT16_SGIX</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENT16 = 0x81a5;
	  /** <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_UNSYNCHRONIZED_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_UNSYNCHRONIZED_BIT = 0x20;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_BINDING_EXT</code>, <code>GL_FRAMEBUFFER_BINDING_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_BINDING = 0x8ca6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_PACK_ALIGNMENT = 0xd05;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_INTERNAL_FORMAT_OES</code>, <code>GL_RENDERBUFFER_INTERNAL_FORMAT_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8d44;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_stencil1</code><br>Alias for: <code>GL_STENCIL_INDEX1_EXT</code>, <code>GL_STENCIL_INDEX1_OES</code> - CType: int */
	  public static final int GL_STENCIL_INDEX1 = 0x8d46;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_vertex_buffer_object</code>, <code>GL_OES_mapbuffer</code><br>Alias for: <code>GL_BUFFER_MAPPED_ARB</code>, <code>GL_BUFFER_MAPPED_OES</code> - CType: int */
	  public static final int GL_BUFFER_MAPPED = 0x88bc;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_COLOR_CLEAR_VALUE = 0xc22;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code>, <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_RGBA16F_EXT</code>, <code>GL_RGBA16F_ARB</code> - CType: int */
	  public static final int GL_RGBA16F = 0x881a;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_OES_rgb8_rgba8</code>, <code>GL_OES_required_internalformat</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_RGBA8_OES</code>, <code>GL_RGBA8_EXT</code> - CType: int */
	  public static final int GL_RGBA8 = 0x8058;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_OES_blend_subtract</code>, <code>GL_EXT_blend_minmax</code><br>Alias for: <code>GL_BLEND_EQUATION_OES</code>, <code>GL_BLEND_EQUATION_EXT</code> - CType: int */
	  public static final int GL_BLEND_EQUATION = 0x8009;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_UNSIGNED_BYTE = 0x1401;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_BLUE_SIZE_OES</code>, <code>GL_RENDERBUFFER_BLUE_SIZE_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8d52;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_WRITEMASK = 0xb98;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_LUMINANCE32F_EXT</code>, <code>GL_LUMINANCE32F_ARB</code> - CType: int */
	  public static final int GL_LUMINANCE32F = 0x8818;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_OES_element_index_uint</code> - CType: int */
	  public static final int GL_UNSIGNED_INT = 0x1405;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_DEPTH_ATTACHMENT_EXT</code>, <code>GL_DEPTH_ATTACHMENT_OES</code> - CType: int */
	  public static final int GL_DEPTH_ATTACHMENT = 0x8d00;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_RGB10_A2_EXT</code> - CType: int */
	  public static final int GL_RGB10_A2 = 0x8059;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multisample</code><br>Alias for: <code>GL_SAMPLE_COVERAGE_VALUE_ARB</code> - CType: int */
	  public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80aa;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_framebuffer_blit</code>, <code>GL_EXT_framebuffer_blit</code>, <code>GL_APPLE_framebuffer_multisample</code>, <code>GL_ANGLE_framebuffer_blit</code><br>Alias for: <code>GL_DRAW_FRAMEBUFFER_NV</code>, <code>GL_DRAW_FRAMEBUFFER_EXT</code>, <code>GL_DRAW_FRAMEBUFFER_APPLE</code>, <code>GL_DRAW_FRAMEBUFFER_ANGLE</code> - CType: int */
	  public static final int GL_DRAW_FRAMEBUFFER = 0x8ca9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TRUE = 0x1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINE_STRIP = 0x3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_func_separate</code>, <code>GL_OES_blend_func_separate</code><br>Alias for: <code>GL_BLEND_SRC_RGB_EXT</code>, <code>GL_BLEND_SRC_RGB_OES</code> - CType: int */
	  public static final int GL_BLEND_SRC_RGB = 0x80c9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_FUNC = 0xb74;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_ELEMENT_ARRAY_BUFFER_ARB</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_MAX_RENDERBUFFER_SIZE_OES</code>, <code>GL_MAX_RENDERBUFFER_SIZE_EXT</code> - CType: int */
	  public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84e8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINEAR = 0x2601;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multisample</code><br>Alias for: <code>GL_SAMPLE_COVERAGE_ARB</code> - CType: int */
	  public static final int GL_SAMPLE_COVERAGE = 0x80a0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_5_6_5 = 0x8363;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_BACK = 0x405;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_BINDING_CUBE_MAP_ARB</code>, <code>GL_TEXTURE_BINDING_CUBE_MAP_EXT</code>, <code>GL_TEXTURE_BINDING_CUBE_MAP_OES</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_CUBE_MAP = 0x8514;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_blit</code>, <code>GL_ANGLE_framebuffer_blit</code>, <code>GL_NV_framebuffer_blit</code>, <code>GL_APPLE_framebuffer_multisample</code><br>Alias for: <code>GL_READ_FRAMEBUFFER_EXT</code>, <code>GL_READ_FRAMEBUFFER_ANGLE</code>, <code>GL_READ_FRAMEBUFFER_NV</code>, <code>GL_READ_FRAMEBUFFER_APPLE</code> - CType: int */
	  public static final int GL_READ_FRAMEBUFFER = 0x8ca8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_3DFX_multisample</code>, <code>GL_ARB_multisample</code>, <code>GL_EXT_multisample</code>, <code>GL_SGIS_multisample</code><br>Alias for: <code>GL_SAMPLE_BUFFERS_3DFX</code>, <code>GL_SAMPLE_BUFFERS_ARB</code>, <code>GL_SAMPLE_BUFFERS_EXT</code>, <code>GL_SAMPLE_BUFFERS_SGIS</code> - CType: int */
	  public static final int GL_SAMPLE_BUFFERS = 0x80a8;
	  /** <code>GL_EXT_read_format_bgra</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV_EXT = 0x8365;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_ALPHA_SIZE_OES</code>, <code>GL_RENDERBUFFER_ALPHA_SIZE_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8d53;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT = 0x1403;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_draw_buffers</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_draw_buffers</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_COLOR_ATTACHMENT0_NV</code>, <code>GL_COLOR_ATTACHMENT0_OES</code>, <code>GL_COLOR_ATTACHMENT0_EXT</code> - CType: int */
	  public static final int GL_COLOR_ATTACHMENT0 = 0x8ce0;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_LUMINANCE_ALPHA16F_EXT</code>, <code>GL_LUMINANCE_ALPHA16F_ARB</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA16F = 0x881f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_X_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_X_EXT</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_X_OES</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_EXT</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_OES</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_stencil_wrap</code>, <code>GL_OES_stencil_wrap</code><br>Alias for: <code>GL_INCR_WRAP_EXT</code>, <code>GL_INCR_WRAP_OES</code> - CType: int */
	  public static final int GL_INCR_WRAP = 0x8507;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_EXT</code>, <code>GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_OES</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_BLUE_BITS = 0xd54;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_RGB = 0x1907;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_color_buffer_half_float</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_RG16F_EXT</code> - CType: int */
	  public static final int GL_RG16F = 0x822f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SRC_ALPHA = 0x302;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_MAX_CUBE_MAP_TEXTURE_SIZE_ARB</code>, <code>GL_MAX_CUBE_MAP_TEXTURE_SIZE_EXT</code>, <code>GL_MAX_CUBE_MAP_TEXTURE_SIZE_OES</code> - CType: int */
	  public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_BINDING_OES</code>, <code>GL_RENDERBUFFER_BINDING_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_BINDING = 0x8ca7;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_robustness</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code><br>Alias for: <code>GL_RESET_NOTIFICATION_STRATEGY_KHR</code>, <code>GL_RESET_NOTIFICATION_STRATEGY_EXT</code>, <code>GL_RESET_NOTIFICATION_STRATEGY_ARB</code> - CType: int */
	  public static final int GL_RESET_NOTIFICATION_STRATEGY = 0x8256;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_COLOR_BUFFER_BIT = 0x4000;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_ALPHA32F_EXT</code>, <code>GL_ALPHA32F_ARB</code> - CType: int */
	  public static final int GL_ALPHA32F = 0x8816;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_polygon_offset</code><br>Alias for: <code>GL_POLYGON_OFFSET_FACTOR_EXT</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_FACTOR = 0x8038;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_EXT_bgra</code><br>Alias for: <code>GL_BGR_EXT</code> - CType: int */
	  public static final int GL_BGR = 0x80e0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_KEEP = 0x1e00;
	  /** <code>GL_VERSION_1_2</code>, <code>GL_IMG_read_format</code>, <code>GL_APPLE_texture_format_BGRA8888</code>, <code>GL_EXT_texture_format_BGRA8888</code>, <code>GL_EXT_bgra</code>, <code>GL_EXT_read_format_bgra</code><br>Alias for: <code>GL_BGRA_IMG</code>, <code>GL_BGRA_EXT</code> - CType: int */
	  public static final int GL_BGRA = 0x80e1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_COLOR_WRITEMASK = 0xc23;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_OES_texture_mirrored_repeat</code>, <code>GL_IBM_texture_mirrored_repeat</code>, <code>GL_ARB_texture_mirrored_repeat</code><br>Alias for: <code>GL_MIRRORED_REPEAT_OES</code>, <code>GL_MIRRORED_REPEAT_IBM</code>, <code>GL_MIRRORED_REPEAT_ARB</code> - CType: int */
	  public static final int GL_MIRRORED_REPEAT = 0x8370;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_ELEMENT_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SCISSOR_BOX = 0xc10;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_OES_texture_cube_map</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_POSITIVE_X_OES</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_X_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_X_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Y_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Y_EXT</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Y_OES</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_ARB_texture_cube_map</code>, <code>GL_EXT_texture_cube_map</code>, <code>GL_OES_texture_cube_map</code><br>Alias for: <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Z_ARB</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Z_EXT</code>, <code>GL_TEXTURE_CUBE_MAP_POSITIVE_Z_OES</code> - CType: int */
	  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DECR = 0x1e03;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_minmax</code>, <code>GL_OES_blend_subtract</code><br>Alias for: <code>GL_FUNC_ADD_EXT</code>, <code>GL_FUNC_ADD_OES</code> - CType: int */
	  public static final int GL_FUNC_ADD = 0x8006;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINES = 0x1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ALPHA_BITS = 0xd55;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_OES_blend_func_separate</code>, <code>GL_EXT_blend_func_separate</code><br>Alias for: <code>GL_BLEND_SRC_ALPHA_OES</code>, <code>GL_BLEND_SRC_ALPHA_EXT</code> - CType: int */
	  public static final int GL_BLEND_SRC_ALPHA = 0x80cb;
	  /** <code>GL_VERSION_2_1</code>, <code>GL_EXT_texture_sRGB</code>, <code>GL_EXT_sRGB</code><br>Alias for: <code>GL_SRGB_ALPHA_EXT</code> - CType: int */
	  public static final int GL_SRGB_ALPHA = 0x8c42;
	  /** <code>GL_ARB_framebuffer_sRGB</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_sRGB</code>, <code>GL_EXT_sRGB_write_control</code><br>Alias for: <code>GL_FRAMEBUFFER_SRGB_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_SRGB = 0x8db9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_packed_pixels</code><br>Alias for: <code>GL_UNSIGNED_SHORT_4_4_4_4_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_PASS_DEPTH_FAIL = 0xb95;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_HEIGHT_OES</code>, <code>GL_RENDERBUFFER_HEIGHT_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_HEIGHT = 0x8d43;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_POLYGON_OFFSET_FILL = 0x8037;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_LUMINANCE8_ALPHA8_EXT</code>, <code>GL_LUMINANCE8_ALPHA8_OES</code> - CType: int */
	  public static final int GL_LUMINANCE8_ALPHA8 = 0x8045;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_RGB565_OES</code> - CType: int */
	  public static final int GL_RGB565 = 0x8d62;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINE_WIDTH = 0xb21;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_packed_pixels</code><br>Alias for: <code>GL_UNSIGNED_SHORT_5_5_5_1_EXT</code> - CType: int */
	  public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_ALPHA8_EXT</code>, <code>GL_ALPHA8_OES</code> - CType: int */
	  public static final int GL_ALPHA8 = 0x803c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_CW = 0x900;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LEQUAL = 0x203;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_OES_required_internalformat</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture</code><br>Alias for: <code>GL_LUMINANCE8_OES</code>, <code>GL_LUMINANCE8_EXT</code> - CType: int */
	  public static final int GL_LUMINANCE8 = 0x8040;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_GREEN_BITS = 0xd53;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_RED_BITS = 0xd52;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code>, <code>GL_OES_rgb8_rgba8</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_RGB8_EXT</code>, <code>GL_RGB8_OES</code> - CType: int */
	  public static final int GL_RGB8 = 0x8051;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NO_ERROR = 0x0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_VALUE_MASK = 0xb93;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_WRAP_S = 0x2802;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_CLEAR_VALUE = 0xb73;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_WRAP_T = 0x2803;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_2D = 0xde1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_ACTIVE_TEXTURE_ARB</code> - CType: int */
	  public static final int GL_ACTIVE_TEXTURE = 0x84e0;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code>, <code>GL_KHR_robustness</code><br>Alias for: <code>GL_INNOCENT_CONTEXT_RESET_EXT</code>, <code>GL_INNOCENT_CONTEXT_RESET_ARB</code>, <code>GL_INNOCENT_CONTEXT_RESET_KHR</code> - CType: int */
	  public static final int GL_INNOCENT_CONTEXT_RESET = 0x8254;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_BUFFER_USAGE_ARB</code> - CType: int */
	  public static final int GL_BUFFER_USAGE = 0x8765;
	  /** <code>GL_ARB_ES2_compatibility</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_4_1</code>, <code>GL_OES_read_format</code><br>Alias for: <code>GL_IMPLEMENTATION_COLOR_READ_TYPE_OES</code> - CType: int */
	  public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8b9a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB</code> - CType: int */
	  public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86a2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LINE_LOOP = 0x2;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_BUFFER_SIZE_ARB</code> - CType: int */
	  public static final int GL_BUFFER_SIZE = 0x8764;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TRIANGLE_FAN = 0x6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_DEPTH_SIZE_EXT</code>, <code>GL_RENDERBUFFER_DEPTH_SIZE_OES</code> - CType: int */
	  public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8d54;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE20_ARB</code> - CType: int */
	  public static final int GL_TEXTURE20 = 0x84d4;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_MAX_VIEWPORT_DIMS = 0xd3a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_OES_stencil_wrap</code>, <code>GL_EXT_stencil_wrap</code><br>Alias for: <code>GL_DECR_WRAP_OES</code>, <code>GL_DECR_WRAP_EXT</code> - CType: int */
	  public static final int GL_DECR_WRAP = 0x8508;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE19_ARB</code> - CType: int */
	  public static final int GL_TEXTURE19 = 0x84d3;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE18_ARB</code> - CType: int */
	  public static final int GL_TEXTURE18 = 0x84d2;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE17_ARB</code> - CType: int */
	  public static final int GL_TEXTURE17 = 0x84d1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE16_ARB</code> - CType: int */
	  public static final int GL_TEXTURE16 = 0x84d0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE15_ARB</code> - CType: int */
	  public static final int GL_TEXTURE15 = 0x84cf;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE14_ARB</code> - CType: int */
	  public static final int GL_TEXTURE14 = 0x84ce;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE13_ARB</code> - CType: int */
	  public static final int GL_TEXTURE13 = 0x84cd;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE12_ARB</code> - CType: int */
	  public static final int GL_TEXTURE12 = 0x84cc;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE11_ARB</code> - CType: int */
	  public static final int GL_TEXTURE11 = 0x84cb;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE10_ARB</code> - CType: int */
	  public static final int GL_TEXTURE10 = 0x84ca;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE31_ARB</code> - CType: int */
	  public static final int GL_TEXTURE31 = 0x84df;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE30_ARB</code> - CType: int */
	  public static final int GL_TEXTURE30 = 0x84de;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture_rg</code><br>Alias for: <code>GL_R8_EXT</code> - CType: int */
	  public static final int GL_R8 = 0x8229;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_ALPHA16F_EXT</code>, <code>GL_ALPHA16F_ARB</code> - CType: int */
	  public static final int GL_ALPHA16F = 0x881c;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE29_ARB</code> - CType: int */
	  public static final int GL_TEXTURE29 = 0x84dd;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE28_ARB</code> - CType: int */
	  public static final int GL_TEXTURE28 = 0x84dc;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE27_ARB</code> - CType: int */
	  public static final int GL_TEXTURE27 = 0x84db;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE26_ARB</code> - CType: int */
	  public static final int GL_TEXTURE26 = 0x84da;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE25_ARB</code> - CType: int */
	  public static final int GL_TEXTURE25 = 0x84d9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE24_ARB</code> - CType: int */
	  public static final int GL_TEXTURE24 = 0x84d8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE23_ARB</code> - CType: int */
	  public static final int GL_TEXTURE23 = 0x84d7;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE22_ARB</code> - CType: int */
	  public static final int GL_TEXTURE22 = 0x84d6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multitexture</code><br>Alias for: <code>GL_TEXTURE21_ARB</code> - CType: int */
	  public static final int GL_TEXTURE21 = 0x84d5;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_FAIL = 0xb94;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FRONT = 0x404;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_SUBPIXEL_BITS = 0xd50;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_ARRAY_BUFFER_BINDING_ARB</code> - CType: int */
	  public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_COMPLETE_EXT</code>, <code>GL_FRAMEBUFFER_COMPLETE_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_COMPLETE = 0x8cd5;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_VENDOR = 0x1f00;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ALIASED_LINE_WIDTH_RANGE = 0x846e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_CCW = 0x901;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_REF = 0xb97;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE = 0x1702;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_PASS_DEPTH_PASS = 0xb96;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FRONT_AND_BACK = 0x408;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_LUMINANCE_ALPHA32F_EXT</code>, <code>GL_LUMINANCE_ALPHA32F_ARB</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA32F = 0x8819;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_LUMINANCE_ALPHA = 0x190a;
	  /** <code>GL_EXT_buffer_storage</code>, <code>GL_ARB_map_buffer_range</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_map_buffer_range</code><br>Alias for: <code>GL_MAP_READ_BIT_EXT</code> - CType: int */
	  public static final int GL_MAP_READ_BIT = 0x1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_OES</code>, <code>GL_FRAMEBUFFER_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER = 0x8d40;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_ARRAY_BUFFER_ARB</code> - CType: int */
	  public static final int GL_ARRAY_BUFFER = 0x8892;
	  /** <code>GL_ARB_texture_storage</code>, <code>GL_VERSION_4_2</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_TEXTURE_IMMUTABLE_FORMAT_EXT</code> - CType: int */
	  public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_OUT_OF_MEMORY = 0x505;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_NONE_OES</code> - CType: int */
	  public static final int GL_NONE = 0x0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_ALPHA = 0x1906;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DONT_CARE = 0x1100;
	  /** <code>GL_EXT_texture_compression_s3tc</code>, <code>GL_EXT_texture_compression_dxt1</code> - CType: int */
	  public static final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83f0;
	  /** <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_LUMINANCE16F_EXT</code>, <code>GL_LUMINANCE16F_ARB</code> - CType: int */
	  public static final int GL_LUMINANCE16F = 0x881e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_WRITEMASK = 0xb72;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES</code>, <code>GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8cd6;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_OES_blend_subtract</code>, <code>GL_EXT_blend_subtract</code><br>Alias for: <code>GL_FUNC_REVERSE_SUBTRACT_OES</code>, <code>GL_FUNC_REVERSE_SUBTRACT_EXT</code> - CType: int */
	  public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800b;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_REPEAT = 0x2901;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_RENDERER = 0x1f01;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_stencil8</code>, <code>GL_OES_texture_stencil8</code><br>Alias for: <code>GL_STENCIL_INDEX8_EXT</code>, <code>GL_STENCIL_INDEX8_OES</code> - CType: int */
	  public static final int GL_STENCIL_INDEX8 = 0x8d48;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_TEST = 0xb90;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_1</code>, <code>GL_OES_mapbuffer</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_WRITE_ONLY_OES</code>, <code>GL_WRITE_ONLY_ARB</code> - CType: int */
	  public static final int GL_WRITE_ONLY = 0x88b9;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_STATIC_DRAW_ARB</code> - CType: int */
	  public static final int GL_STATIC_DRAW = 0x88e4;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_POINTS = 0x0;
	  /** <code>GL_VERSION_1_4</code>, <code>GL_ARB_depth_texture</code>, <code>GL_SGIX_depth_texture</code>, <code>GL_OES_depth32</code><br>Alias for: <code>GL_DEPTH_COMPONENT32_ARB</code>, <code>GL_DEPTH_COMPONENT32_SGIX</code>, <code>GL_DEPTH_COMPONENT32_OES</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENT32 = 0x81a7;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_RANGE = 0xb70;
	  /** <code>GL_ARB_imaging</code>, <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_OES_blend_subtract</code>, <code>GL_EXT_blend_subtract</code><br>Alias for: <code>GL_FUNC_SUBTRACT_OES</code>, <code>GL_FUNC_SUBTRACT_EXT</code> - CType: int */
	  public static final int GL_FUNC_SUBTRACT = 0x800a;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_UNSUPPORTED_EXT</code>, <code>GL_FRAMEBUFFER_UNSUPPORTED_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8cdd;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_3DFX_multisample</code>, <code>GL_ARB_multisample</code>, <code>GL_EXT_multisample</code>, <code>GL_SGIS_multisample</code><br>Alias for: <code>GL_SAMPLES_3DFX</code>, <code>GL_SAMPLES_ARB</code>, <code>GL_SAMPLES_EXT</code>, <code>GL_SAMPLES_SGIS</code> - CType: int */
	  public static final int GL_SAMPLES = 0x80a9;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TEXTURE_BINDING_2D = 0x8069;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_ARB_depth_texture</code>, <code>GL_OES_depth24</code>, <code>GL_SGIX_depth_texture</code><br>Alias for: <code>GL_DEPTH_COMPONENT24_ARB</code>, <code>GL_DEPTH_COMPONENT24_OES</code>, <code>GL_DEPTH_COMPONENT24_SGIX</code> - CType: int */
	  public static final int GL_DEPTH_COMPONENT24 = 0x81a6;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_BITS = 0xd57;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_GREEN_SIZE_EXT</code>, <code>GL_RENDERBUFFER_GREEN_SIZE_OES</code> - CType: int */
	  public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8d51;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_func_separate</code>, <code>GL_OES_blend_func_separate</code><br>Alias for: <code>GL_BLEND_DST_RGB_EXT</code>, <code>GL_BLEND_DST_RGB_OES</code> - CType: int */
	  public static final int GL_BLEND_DST_RGB = 0x80c8;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_BLEND = 0xbe2;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code>, <code>GL_KHR_robustness</code><br>Alias for: <code>GL_NO_RESET_NOTIFICATION_EXT</code>, <code>GL_NO_RESET_NOTIFICATION_ARB</code>, <code>GL_NO_RESET_NOTIFICATION_KHR</code> - CType: int */
	  public static final int GL_NO_RESET_NOTIFICATION = 0x8261;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_packed_depth_stencil</code>, <code>GL_NV_packed_depth_stencil</code>, <code>GL_EXT_packed_depth_stencil</code><br>Alias for: <code>GL_DEPTH_STENCIL_OES</code>, <code>GL_DEPTH_STENCIL_NV</code>, <code>GL_DEPTH_STENCIL_EXT</code> - CType: int */
	  public static final int GL_DEPTH_STENCIL = 0x84f9;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ARB_vertex_buffer_object</code>, <code>GL_OES_mapbuffer</code><br>Alias for: <code>GL_BUFFER_ACCESS_ARB</code>, <code>GL_BUFFER_ACCESS_OES</code> - CType: int */
	  public static final int GL_BUFFER_ACCESS = 0x88bb;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NEAREST = 0x2600;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_EXT</code>, <code>GL_RENDERBUFFER_OES</code> - CType: int */
	  public static final int GL_RENDERBUFFER = 0x8d41;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_CULL_FACE = 0xb44;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code>, <code>GL_KHR_robustness</code><br>Alias for: <code>GL_GUILTY_CONTEXT_RESET_EXT</code>, <code>GL_GUILTY_CONTEXT_RESET_ARB</code>, <code>GL_GUILTY_CONTEXT_RESET_KHR</code> - CType: int */
	  public static final int GL_GUILTY_CONTEXT_RESET = 0x8253;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_VIEWPORT = 0xba2;
	  /** <code>GL_VERSION_4_5</code>, <code>GL_ES_VERSION_3_2</code>, <code>GL_KHR_robustness</code>, <code>GL_EXT_robustness</code>, <code>GL_ARB_robustness</code><br>Alias for: <code>GL_LOSE_CONTEXT_ON_RESET_KHR</code>, <code>GL_LOSE_CONTEXT_ON_RESET_EXT</code>, <code>GL_LOSE_CONTEXT_ON_RESET_ARB</code> - CType: int */
	  public static final int GL_LOSE_CONTEXT_ON_RESET = 0x8252;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_GREATER = 0x204;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT</code>, <code>GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES</code> - CType: int */
	  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8cd1;
	  /** <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_stencil4</code><br>Alias for: <code>GL_STENCIL_INDEX4_EXT</code>, <code>GL_STENCIL_INDEX4_OES</code> - CType: int */
	  public static final int GL_STENCIL_INDEX4 = 0x8d47;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_DEPTH_BUFFER_BIT = 0x100;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_OES_framebuffer_object</code>, <code>GL_EXT_framebuffer_object</code><br>Alias for: <code>GL_RENDERBUFFER_RED_SIZE_OES</code>, <code>GL_RENDERBUFFER_RED_SIZE_EXT</code> - CType: int */
	  public static final int GL_RENDERBUFFER_RED_SIZE = 0x8d50;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_blend_func_extended</code><br>Alias for: <code>GL_SRC_ALPHA_SATURATE_EXT</code> - CType: int */
	  public static final int GL_SRC_ALPHA_SATURATE = 0x308;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_UNPACK_ALIGNMENT = 0xcf5;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_LUMINANCE4_ALPHA4_EXT</code>, <code>GL_LUMINANCE4_ALPHA4_OES</code> - CType: int */
	  public static final int GL_LUMINANCE4_ALPHA4 = 0x8043;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_RG32F_EXT</code> - CType: int */
	  public static final int GL_RG32F = 0x8230;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_texture_rg</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_R32F_EXT</code> - CType: int */
	  public static final int GL_R32F = 0x822e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_multisample</code><br>Alias for: <code>GL_SAMPLE_ALPHA_TO_COVERAGE_ARB</code> - CType: int */
	  public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809e;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_TRIANGLE_STRIP = 0x5;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_NV_packed_depth_stencil</code>, <code>GL_EXT_packed_depth_stencil</code>, <code>GL_OES_packed_depth_stencil</code><br>Alias for: <code>GL_UNSIGNED_INT_24_8_NV</code>, <code>GL_UNSIGNED_INT_24_8_EXT</code>, <code>GL_UNSIGNED_INT_24_8_OES</code> - CType: int */
	  public static final int GL_UNSIGNED_INT_24_8 = 0x84fa;
	  /** <code>GL_EXT_texture_filter_anisotropic</code> - CType: int */
	  public static final int GL_TEXTURE_MAX_ANISOTROPY_EXT = 0x84fe;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_CLEAR_VALUE = 0xb91;
	  /** <code>GL_EXT_texture_compression_s3tc</code>, <code>GL_EXT_texture_compression_dxt1</code> - CType: int */
	  public static final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83f1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_EXT_vertex_shader</code><br>Alias for: <code>GL_ZERO_EXT</code> - CType: int */
	  public static final int GL_ZERO = 0x0;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_2</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_SGIS_texture_edge_clamp</code><br>Alias for: <code>GL_CLAMP_TO_EDGE_SGIS</code> - CType: int */
	  public static final int GL_CLAMP_TO_EDGE = 0x812f;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_4</code>, <code>GL_EXT_blend_func_separate</code>, <code>GL_OES_blend_func_separate</code><br>Alias for: <code>GL_BLEND_DST_ALPHA_EXT</code>, <code>GL_BLEND_DST_ALPHA_OES</code> - CType: int */
	  public static final int GL_BLEND_DST_ALPHA = 0x80ca;
	  /** <code>GL_VERSION_1_1</code>, <code>GL_EXT_texture_storage</code>, <code>GL_EXT_texture</code>, <code>GL_OES_required_internalformat</code><br>Alias for: <code>GL_RGB10_EXT</code> - CType: int */
	  public static final int GL_RGB10 = 0x8052;
	  /** <code>GL_APPLE_texture_format_BGRA8888</code>, <code>GL_EXT_texture_storage</code><br>Alias for: <code>GL_BGRA8_EXT</code> - CType: int */
	  public static final int GL_BGRA8 = 0x93a1;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_FLOAT = 0x1406;
	  /** <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_texture_storage</code>, <code>GL_ARB_texture_float</code><br>Alias for: <code>GL_RGBA32F_EXT</code>, <code>GL_RGBA32F_ARB</code> - CType: int */
	  public static final int GL_RGBA32F = 0x8814;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_ARB_framebuffer_object</code>, <code>GL_VERSION_3_0</code>, <code>GL_EXT_framebuffer_object</code>, <code>GL_OES_framebuffer_object</code><br>Alias for: <code>GL_STENCIL_ATTACHMENT_EXT</code>, <code>GL_STENCIL_ATTACHMENT_OES</code> - CType: int */
	  public static final int GL_STENCIL_ATTACHMENT = 0x8d20;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_INVALID_VALUE = 0x501;
	  /** <code>GL_VERSION_1_5</code>, <code>GL_ES_VERSION_3_0</code>, <code>GL_OES_mapbuffer</code>, <code>GL_ARB_vertex_buffer_object</code><br>Alias for: <code>GL_BUFFER_MAP_POINTER_OES</code>, <code>GL_BUFFER_MAP_POINTER_ARB</code> - CType: int */
	  public static final int GL_BUFFER_MAP_POINTER = 0x88bd;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_GEQUAL = 0x206;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_NICEST = 0x1102;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_1</code>, <code>GL_VERSION_1_0</code>, <code>GL_VERSION_ES_1_0</code> - CType: int */
	  public static final int GL_STENCIL_FUNC = 0xb92;
	  /** <code>GL_ES_VERSION_2_0</code>, <code>GL_VERSION_1_3</code>, <code>GL_VERSION_ES_1_0</code>, <code>GL_ARB_texture_compression</code><br>Alias for: <code>GL_COMPRESSED_TEXTURE_FORMATS_ARB</code> - CType: int */
	  public static final int GL_COMPRESSED_TEXTURE_FORMATS = 0x86a3;

	  // --- Begin CustomJavaCode .cfg declarations
	  
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_AND = 0x1501;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_AND_INVERTED = 0x1504;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_AND_REVERSE = 0x1502;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_BLEND_DST = 0x0BE0;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_BLEND_SRC = 0x0BE1;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_CLEAR = 0x1500;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_COLOR_LOGIC_OP = 0x0BF2;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_COPY = 0x1503;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_COPY_INVERTED = 0x150C;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_EQUIV = 0x1509;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_LINE_SMOOTH = 0x0B20;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_LINE_SMOOTH_HINT = 0x0C52;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_LOGIC_OP_MODE = 0x0BF0;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_MULTISAMPLE = 0x809D;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_NAND = 0x150E;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_NOOP = 0x1505;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_NOR = 0x1508;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_OR = 0x1507;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_OR_INVERTED = 0x150D;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_OR_REVERSE = 0x150B;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_POINT_FADE_THRESHOLD_SIZE = 0x8128;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_POINT_SIZE = 0x0B11;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_SAMPLE_ALPHA_TO_ONE = 0x809F;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_SET = 0x150F;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_SMOOTH_LINE_WIDTH_RANGE = 0x0B22;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_SMOOTH_POINT_SIZE_RANGE = 0x0B12;
	    /** Common in ES1, GL2 and GL3 */
	    public static final int GL_XOR = 0x1506;
	    /** Part of <code>GL_ARB_half_float_vertex</code>; <code>GL_NV_half_float</code>; <code>GL_ARB_half_float_pixel</code>, 
	        earmarked for ES 3.0 (hence kept in GL while fixing Bug 590)  */
	    public static final int GL_HALF_FLOAT = 0x140B;
	  
	    /** Part of <code>GL_EXT_framebuffer_object</code>; <code>GL_ES_VERSION_2_0</code>; <code>GL_OES_framebuffer_object</code> */
	    public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS = 0x8CDA;
	    /** Part of <code>GL_ES_VERSION_3_0</code>, <code>GL_VERSION_3_0</code>; <code>GL_EXT_packed_float</code> */
	    public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B;
	  
	  // ---- End CustomJavaCode .cfg declarations

}
