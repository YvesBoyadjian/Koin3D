/**
 * 
 */
package jscenegraph.coin3d.glue;

/**
 * @author Yves Boyadjian
 *
 */
/*** WGL offscreen contexts ***********************************************/

public class Wglglue_contextdata {

	  int width, height;

	  /*HDC*/int memorydc;
	  public /*HWND*/long pbufferwnd;
	  boolean didcreatememorydc;
	  boolean shouldreleasememorydc;
	  /*HBITMAP*/int bitmap, oldbitmap;
	  /*HGLRC*/int wglcontext;

	  /*HGLRC*/int storedcontext;
	  /*HDC*/int storeddc;

	  /*WGLGLUE_HPBUFFER*/long hpbuffer;
	  boolean noappglcontextavail;

	  boolean supports_render_to_texture;
	  boolean wanted_render_to_texture;
	  boolean pbufferisbound;
	  Object pvBits;
	  int pixelformat;
}
