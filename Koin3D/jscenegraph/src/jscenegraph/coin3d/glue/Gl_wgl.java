/**
 * 
 */
package jscenegraph.coin3d.glue;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.WGL;
import org.lwjgl.opengl.WGLARBPbuffer;
import org.lwjgl.opengl.WGLARBRenderTexture;
import org.lwjgl.system.windows.User32;
import org.lwjgl.system.windows.WinBase;

/**
 * @author Yves Boyadjian
 *
 */
public class Gl_wgl {

	
	
	static interface wglglue_context_create_ {
		public boolean create( Wglglue_contextdata context, boolean warnonerrors);
	}
	
	static wglglue_context_create_ wglglue_context_create = Gl_wgl::wglglue_context_create_pbuffer;
	
/* ********************************************************************** */

/* Create and return a handle to an offscreen OpenGL buffer.

   Where p-buffer support is available that will be used instead of a
   standard offscreen WGL context, as it should render much faster
   (due to hardware acceleration).

   See:
     http://www.oss.sgi.com/projects/ogl-sample/registry/ARB/wgl_pbuffer.txt
   Or the older version:
     http://www.oss.sgi.com/projects/ogl-sample/registry/EXT/wgl_pbuffer.txt
*/
public static Object
wglglue_context_create_offscreen(int width, int height, boolean texture)
{
  Wglglue_contextdata swctx, pbctx = null; // ptr
  boolean ispbuffer;

  if (Gl.coin_glglue_debug() != 0) {
    cc_debugerror_postinfo("wglglue_context_create_offscreen",
                           "method called ");
  }

  swctx = wglglue_contextdata_init(width, height);
  assert(swctx != null);

  swctx.wanted_render_to_texture = texture;

  if (wglglue_context_create != null) {

    ispbuffer = true;//(wglglue_context_create == Gl_wgl::wglglue_context_create_pbuffer);

    /* don't warn if we fail to open a pbuffer context. we will try software  */
    if (wglglue_context_create.create(swctx, !ispbuffer)) { return swctx; }
//    wglglue_contextdata_cleanup(swctx); TODO
//
//    /* fall back to a software context */
//    if (ispbuffer) {
//      if (coin_glglue_debug()) {
//        cc_debugerror_postinfo("wglglue_context_create_offscreen",
//                               "pbuffer failed. Trying software ");
//      }
//      swctx = wglglue_contextdata_init(width, height);
//      assert(swctx);
//      if (wglglue_context_create_software(swctx, TRUE)) { return swctx; }
//      wglglue_contextdata_cleanup(swctx);
//    }
    return null;
  }

  /* As there could possibly be no valid wgl context at this moment,
     we have to first make a context and set it current to be able
     to query pbuffer extension availability. */

//  if (!wglglue_context_create_software(swctx, TRUE)) { TODO
//    wglglue_contextdata_cleanup(swctx);
//    return NULL;
//  }
//
//  /* ok, so we can at least use a non-pbuffer offscreen context */
//  wglglue_context_create = wglglue_context_create_software;
//
//  /* developer or user can force pbuffer support off with this envvar */
//  {
//    const char * env = coin_getenv("COIN_WGLGLUE_NO_PBUFFERS");
//    if (env && atoi(env) > 0) { return swctx; }
//  }
//
//  /* next, check if pbuffer support is available in the OpenGL
//     library image */
//
//  pbctx = wglglue_contextdata_init(width, height);
//  assert(pbctx);
//
//  pbctx->wanted_render_to_texture = texture;
//
//  /* attempt to create a pbuffer */
//  if (!wglglue_context_create_pbuffer(pbctx, FALSE)) {
//    wglglue_contextdata_cleanup(pbctx);
//    return swctx;
//  }
//
//  /* pbuffers are really supported, kill the software offscreen
//     context and use the pbuffer-enabled one */
//  wglglue_contextdata_cleanup(swctx);
//
//  wglglue_context_create = wglglue_context_create_pbuffer;

  return pbctx;
}

public static Wglglue_contextdata
wglglue_contextdata_init( int width, int height)
{
  Wglglue_contextdata context;

  context = (Wglglue_contextdata )new Wglglue_contextdata();

  context.width = width;
  context.height = height;
  context.memorydc = 0;//NULL;
  context.pbufferwnd = 0;//NULL;
  context.didcreatememorydc = false;
  context.shouldreleasememorydc = false;
  context.bitmap = 0;//NULL;
  context.hpbuffer = 0;//NULL;
  context.oldbitmap = 0;//NULL;
  context.wglcontext = 0;//NULL;
  context.storedcontext = 0;//NULL;
  context.storeddc = 0;//NULL;
  context.noappglcontextavail = false;
  context.supports_render_to_texture = false;
  context.wanted_render_to_texture = true;
  context.pbufferisbound = false;
  context.pixelformat = 0;

  return context;
}

static void
wglglue_contextdata_cleanup( Wglglue_contextdata ctx)
{
  if (ctx == null) { return; }

  /* FIXME: the error handling below can and should be simplified, by
     implementing and using excpetion catching wrappers from
     glue/win32api. 20031124 mortene. */

  if (ctx.wglcontext != 0 && ctx.noappglcontextavail) {
    final boolean r = WGL.wglDeleteContext(ctx.wglcontext);
    if (!r) {
    	Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
                           "wglDeleteContext", WinBase.GetLastError());
    }
  }
//  if (ctx.oldbitmap != 0) { YB : not implemented in LWJGL
//    final HGDIOBJ o = SelectObject(ctx.memorydc, ctx.oldbitmap);
//    if (!o) {
//    	Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
//                           "SelectObject", WinBase.GetLastError());
//    }
//  }
//  if (ctx.bitmap != 0) { YB : not implemented in LWJGL
//    final boolean r = DeleteObject(ctx.bitmap);
//    if (!r) {
//    	Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
//                           "DeleteObject", WinBase.GetLastError());
//    }
//  }
  if (ctx.hpbuffer != 0) {
    {
      final int r = WGLARBPbuffer.wglReleasePbufferDCARB(ctx.hpbuffer, WGLARBPbuffer.wglGetPbufferDCARB(ctx.hpbuffer));
      if (r==0) {
    	  Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
                             "wglReleasePbufferDC", WinBase.GetLastError());
      }
    }
    {
      final boolean r = WGLARBPbuffer.wglDestroyPbufferARB(ctx.hpbuffer);
      if (!r) {
    	  Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
                             "wglDestroyPbuffer", WinBase.GetLastError());
      }
    }
  }
  if (ctx.memorydc != 0) {
    if (ctx.didcreatememorydc) { // YB : not implemented in LWJGL
//      final boolean r = DeleteDC(ctx.memorydc);
//      if (!r) {
//    	  Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
//                             "DeleteDC", WinBase.GetLastError());
//      }
    }
    else if (ctx.shouldreleasememorydc && ctx.pbufferwnd != 0) {
      boolean ok = User32.ReleaseDC(ctx.pbufferwnd, ctx.memorydc);
      if (!ok) {
    	  Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
                             "ReleaseDC", WinBase.GetLastError());
      }
    }
  }
  if (ctx.pbufferwnd != 0) {
    boolean r = User32.DestroyWindow(ctx.pbufferwnd);
    if (!r) {
    	Win32Api.cc_win32_print_error("wglglue_contextdata_cleanup",
                           "DestroyWindow", WinBase.GetLastError());
    }
  }

  // free(ctx); java port
}



static int didregister = 0;

public static boolean
wglglue_context_create_pbuffer( Wglglue_contextdata ctx, boolean warnonerrors)
{
  Wglglue_contextdata context = (Wglglue_contextdata )ctx;

  if (Gl.coin_glglue_debug() != 0) {
    cc_debugerror_postinfo("wglglue_context_create_pbuffer", "creating pbuffer");
  }

  if (/*(context->memorydc = wglGetCurrentDC())*/false) {
    context.shouldreleasememorydc = false;
    context.didcreatememorydc = false;
    //context.wglcontext = wglGetCurrentContext(); TODO
  }
  else { context.noappglcontextavail = true; }

  if (context.noappglcontextavail) {
    /* FIXME: This should be reset in wglglue_cleanup() once we
       also properly unregister there... 20060207 kyrah */
    if (didregister == 0) {
//      WNDCLASS wc;
      didregister = 1;

//      wc.style          = CS_HREDRAW | CS_VREDRAW | CS_OWNDC;
//      wc.lpfnWndProc    = DefWindowProc;
//      wc.cbClsExtra     = 0;
//      wc.cbWndExtra     = 0;
//      wc.hInstance      = GetModuleHandle(NULL);
//      wc.hIcon          = NULL;
//      wc.hCursor        = NULL;
//      wc.hbrBackground  = NULL;
//      wc.lpszMenuName   = NULL;
//      wc.lpszClassName  = "coin_gl_wgl";
//
//      if (!RegisterClass(&wc)) {
//        DWORD dwError = GetLastError();
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "RegisterClass(&wc) failed with "
//                                  "error code %d.", dwError);
//        return falsE;
//      }
      /* FIXME: unregister at app exit? pederb, 2003-12-15 */
    }

    {
//      HWND hWnd;
//      HINSTANCE hInstance = GetModuleHandle(NULL);
//
//      if (!(hWnd = CreateWindow(
//                     "coin_gl_wgl",   /* class name */
//                     "coin_gl_wgl",   /* window title */
//                     0,               /* selected window style */
//                     0, 0,            /* window position */
//                     context->width,  /* calculate adjusted window width */
//                     context->height, /* calculate adjusted window height */
//                     NULL,            /* no parent window */
//                     NULL,            /* no menu */
//                     hInstance,       /* Instance */
//                     NULL)))          /* don't pass anything to WM_CREATE */
//      {
//        DWORD dwError = GetLastError();
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "CreateWindow(...) failed with "+
//                                  "error code "+dwError+".");
//        return FALSE;
//      }
    	if ( !glfwInit() ) {
    		return false;
    	}

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
    	
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable								
    	
		// Create the window
		long window = glfwCreateWindow(context.width, context.height, "", /*glfwGetPrimaryMonitor()*/0, 0);
		if ( window == NULL )
			return false;//throw new RuntimeException("Failed to create the GLFW window");
		
		//gladLoadGL(glfwGetProcAddress);
		
		// Make the OpenGL context current
//		glfwMakeContextCurrent(window);
//		glfwShowWindow(window);
		
      context.pbufferwnd = window;
      //context.memorydc = GetDC(hWnd);
      context.shouldreleasememorydc = true;
      context.didcreatememorydc = false;
//      if (context.memorydc == NULL) {
//        DWORD dwError = GetLastError();
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "GetDC(hWnd) failed with "
//                                  "error code %d.", dwError);
//        return false;
//      }
    }

//    if (!(wglglue_context_create_context(context, PFD_DRAW_TO_WINDOW))) {
//      return FALSE;
//    }
  }

  {
    boolean pbuffer = true;//wglglue_resolve_symbols(context);

    if (Gl.coin_glglue_debug() != 0) {
      cc_debugerror_postinfo("wglglue_context_create_pbuffer",
                             "PBuffer offscreen rendering is "+(pbuffer ? "" : "NOT ")+"supported "+
                             "by the OpenGL driver");
    }

    if (!pbuffer) {
      return false;
    }
  }

//  {
//    int pixformat;
//    int numFormats;
//    float fAttribList[] = { 0 };

//    int nontex_attrs[] = {
//      WGL_STENCIL_BITS_ARB, 1, /* FIXME: must be first, since we may
//                                  want to modify it later, and need to
//                                  know where it is. This is a
//                                  wart. 20060307 mortene. */
//      WGL_DRAW_TO_PBUFFER_ARB, TRUE,
//      WGL_COLOR_BITS_ARB, 32,
//      WGL_ALPHA_BITS_ARB, 8,
//      WGL_DEPTH_BITS_ARB, 24,
//      0
//    };
//    int tex_attrs[] = {
//      WGL_STENCIL_BITS_ARB, 1, /* FIXME: must be first, since we may
//                                  want to modify it later, and need to
//                                  know where it is. This is a
//                                  wart. 20060307 mortene. */
//      WGL_DRAW_TO_PBUFFER_ARB, TRUE,
//      WGL_BIND_TO_TEXTURE_RGBA_ARB, TRUE,
//      WGL_COLOR_BITS_ARB, 32,
//      WGL_ALPHA_BITS_ARB, 8,
//      WGL_DEPTH_BITS_ARB, 24,
//      0
//    };
//    int[] attrs[] = { nontex_attrs, tex_attrs };
//
//    int nontex_pbufferflags[] = { 0 };
//    int tex_pbufferflags[] = {
//      WGL_TEXTURE_FORMAT_ARB, WGL_TEXTURE_RGBA_ARB,
//      WGL_TEXTURE_TARGET_ARB, WGL_TEXTURE_2D_ARB,
//      0
//    };
//    int[] pbufferflags[] = { nontex_pbufferflags, tex_pbufferflags };

    int thetry;

//    {
//      /* FIXME: the following is a hack to get around a problem which
//         really demands more effort to be solved properly. See further
//         up in this source file, in the function to create a software
//         context, for a more elaborate explanation. 20060307 mortene.
//      */
//      int v = coin_glglue_stencil_bits_hack();
//      if (v != -1) {
//        assert(nontex_attrs[0] == WGL_STENCIL_BITS_ARB);
//        nontex_attrs[1] = v;
//        assert(tex_attrs[0] == WGL_STENCIL_BITS_ARB);
//        tex_attrs[1] = v;
//      }
//    }

//    /* iterate from end of arrays, which contains "best" option */
//    thetry = (sizeof(attrs) / sizeof(attrs[0]));
//    /* if render-to-texture extension not supported, don't attempt to
//       set up a pbuffer with those capabilities (could in theory cause
//       nasty WGL errors): */
//    if (!context->wanted_render_to_texture || wglglue_wglBindTexImageARB == NULL) {
//      thetry--;
//    }
//
//    while (thetry > 0) {
//      thetry--;
//
//      /* choose pixel format */
//      if (!wglglue_wglChoosePixelFormat(context->memorydc, attrs[thetry],
//                                        fAttribList, 1, &pixformat,
//                                        &numFormats)) {
//        if (warnonerrors || coin_glglue_debug()) {
//          cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                    "wglChoosePixelFormat() failed, try %u",
//                                    thetry);
//        }
//        continue;
//      }
//
//      /* create the pbuffer */
//      context->hpbuffer = wglglue_wglCreatePbuffer(context->memorydc,
//                                                   pixformat,
//                                                   context->width,
//                                                   context->height,
//                                                   pbufferflags[thetry]);
//      if (!context->hpbuffer) {
//        if (warnonerrors || coin_glglue_debug()) {
//          cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                    "wglCreatePbuffer(HDC, PixelFormat==%d, "
//                                    "Width==%d, Height==%d, AttribList==%p) "
//                                    "failed, try %u",
//                                    pixformat,
//                                    context->width, context->height,
//                                    pbufferflags[thetry], thetry);
//        }
//        continue;
//      }
//
//      /* success, set capability flag, and break loop */
//      if (coin_glglue_debug()) {
//        cc_debugerror_postinfo("wglglue_context_create_pbuffer",
//                               "wglCreatePbuffer() success, try %u", thetry);
//      }
//      context->supports_render_to_texture =
//        (pbufferflags[thetry] == tex_pbufferflags);
//      break;
//    }
//
//    /* if no way to construct a pbuffer was found: */
//    if (!context->hpbuffer) { return FALSE; }
//
//    context->pixelformat = pixformat;
//
//    /* delete/release device context and window in case we created it
//       ourselves */
//    if (context->memorydc) {
//      if (context->didcreatememorydc) {
//        BOOL r = DeleteDC(context->memorydc);
//        if (!r) {
//          cc_win32_print_error("wglglue_context_create_pbuffer",
//                               "DeleteDC", GetLastError());
//        }
//      }
//      else if (context->shouldreleasememorydc && context->pbufferwnd) {
//        BOOL r = ReleaseDC(context->pbufferwnd, context->memorydc);
//        if (!r) {
//          cc_win32_print_error("wglglue_context_create_pbuffer",
//                               "ReleaseDC", GetLastError());
//        }
//      } 
//    }
//    if (context->pbufferwnd) {
//      BOOL r = DestroyWindow(context->pbufferwnd);
//      if (!r) {
//        cc_win32_print_error("wglglue_context_create_pbuffer",
//                             "DestroyWindow", GetLastError());
//      }
//      context->pbufferwnd = null;
//    }
//
//
//	context->memorydc = CreateCompatibleDC(wglglue_wglGetPbufferDC(context->hpbuffer));
//
//    BITMAPINFO bmi;
//
//    bmi.bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
//    bmi.bmiHeader.biWidth = context->width;
//    bmi.bmiHeader.biHeight = context->height;
//    bmi.bmiHeader.biPlanes = 1;
//    bmi.bmiHeader.biBitCount = 24;
//    bmi.bmiHeader.biCompression = BI_RGB;
//    bmi.bmiHeader.biSizeImage = 0;
//    bmi.bmiHeader.biXPelsPerMeter = 0;
//    bmi.bmiHeader.biYPelsPerMeter = 0;
//    bmi.bmiHeader.biClrUsed  = 0;
//    bmi.bmiHeader.biClrImportant = 0;
//    bmi.bmiColors[0].rgbBlue = 0;
//    bmi.bmiColors[0].rgbGreen = 0;
//    bmi.bmiColors[0].rgbRed = 0;
//    bmi.bmiColors[0].rgbReserved = 0;
//
//    context->bitmap = CreateDIBSection(context->memorydc, &bmi, DIB_RGB_COLORS,
//                                      &(context->pvBits), NULL, 0);
//	SelectObject(context->memorydc, context->bitmap);
//
//    context->didcreatememorydc = TRUE;
//    context->shouldreleasememorydc = FALSE;
//    if (!context->memorydc) {
//      if (warnonerrors || coin_glglue_debug()) {
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "Couldn't create pbuffer's device context.");
//      }
//      return FALSE;
//    }
//
//    /* delete wgl context in case we created it ourselves */
//    if (context->noappglcontextavail) {
//      BOOL r = wglDeleteContext(context->wglcontext);
//      if (!r) {
//        if (warnonerrors || coin_glglue_debug()) {
//          cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                    "Couldn't create pbuffer's device context.");
//        }
//      }
//    }
//    context->wglcontext = wglCreateContext(wglglue_wglGetPbufferDC(context->hpbuffer));
//    if (!context->wglcontext) {
//      if (warnonerrors || coin_glglue_debug()) {
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "Couldn't create rendering context for the pbuffer.");
//      }
//      return FALSE;
//    }
//
//    /* set and output the actual pBuffer dimensions */
//    if (!wglglue_wglQueryPbuffer(context->hpbuffer,
//                                    WGL_PBUFFER_WIDTH_ARB,
//                                    (int *) &(context->width))) {
//      if (warnonerrors || coin_glglue_debug()) {
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "Couldn't query the pbuffer width.");
//      }
//      return FALSE;
//    }
//
//    if (!wglglue_wglQueryPbuffer(context->hpbuffer,
//                                 WGL_PBUFFER_HEIGHT_ARB,
//                                 (int *) &(context->height))) {
//      if (warnonerrors || coin_glglue_debug()) {
//        cc_debugerror_postwarning("wglglue_context_create_pbuffer",
//                                  "Couldn't query the pbuffer height.");
//      }
//      return FALSE;
//    }
//  }
//
//  if (coin_glglue_debug()) {
//    cc_debugerror_postinfo("wglglue_context_create_pbuffer",
//                           "success creating pbuffer, HGLRC==%p",
//                           context->wglcontext);
//  }

  return true;
}

public static void
wglglue_context_destruct(Object ctx)
{
  /* FIXME: needs to call into the (as of yet unimplemented)
     "destructing GL context" handler. 20030310 mortene. */

  Wglglue_contextdata context = ( Wglglue_contextdata )ctx;

  if (Gl.coin_glglue_debug() != 0) {
    cc_debugerror_postinfo("wglglue_context_destruct",
                           "destroy context, HGLRC==" + context.wglcontext);
  }

  wglglue_contextdata_cleanup(context);
}

/* ********************************************************************** */

public static void
wglglue_context_bind_pbuffer(Object ctx)
{
  boolean ok;

  Wglglue_contextdata context = ( Wglglue_contextdata )ctx;
  //assert(WGLARBRenderTexture.wglBindTexImageARB != NULL);
  assert(context.supports_render_to_texture);

  ok = WGLARBRenderTexture.wglBindTexImageARB(context.hpbuffer, WGLARBRenderTexture.WGL_FRONT_LEFT_ARB);
  assert(ok);
  context.pbufferisbound = true;
}

public static void cc_debugerror_postinfo(String string, String string2) {
	
	System.err.println(string+ " : "+ string2);
}

public static void cc_debugerror_postwarning(String string, String string2) {
	
	System.err.println(string+ " : "+ string2);
}

public static void cc_debugerror_post(String string, String string2) {
	
	System.err.println(string+ " : "+ string2);
}

}
