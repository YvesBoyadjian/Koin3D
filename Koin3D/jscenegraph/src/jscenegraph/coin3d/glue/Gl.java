
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.glue;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import jscenegraph.coin3d.TidBits;
import jscenegraph.port.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Yves Boyadjian
 *
 */
public class Gl {


/*
  Convert from num components to client texture format for use
  in glTexImage*D's format parameter.
*/
public static /*GLenum*/int coin_glglue_get_texture_format(final cc_glglue glw, int numcomponents)
{
  /*GLenum*/int format;
  switch (numcomponents) {
  case 1:
    format = GL2.GL_LUMINANCE;
    break;
  case 2:
    format = GL2.GL_LUMINANCE_ALPHA;
    break;
  case 3:
    format = GL2.GL_RGB;
    break;
  case 4:
  default:
    format = GL2.GL_RGBA;
    break;
  }
  return format;
}

public static void
cc_glglue_glTexSubImage3D(final cc_glglue w,
                          /*GLenum*/int target,
                          int level,
                          int xoffset,
                          int yoffset,
                          int zoffset,
                          /*GLsizei*/int width,
                          /*GLsizei*/int height,
                          /*GLsizei*/int depth,
                          /*GLenum*/int format,
                          /*GLenum*/int type,
                          byte[]  pixels)
{
  //assert(w.glTexSubImage3D);
  glTexSubImage3D(target, level, xoffset, yoffset,
                     zoffset, width, height, depth, format,
                     type, Buffers.newDirectByteBuffer(pixels));
}


public static void
cc_glglue_glTexSubImage2D(final cc_glglue w,
                          /*GLenum*/int target,
                          int level,
                          int xoffset,
                          int yoffset,
                          /*GLsizei*/int width,
                          /*GLsizei*/int height,
                          /*GLenum*/int format,
                          /*GLenum*/int type,
                          byte[] pixels)
{
  //assert(w->glTexSubImage2D);
  glTexSubImage2D(target, level, xoffset, yoffset,
                     width, height, format, type, Buffers.newDirectByteBuffer(pixels));
}


/*
  Convert from num components to internal texture format for use
  in glTexImage*D's internalFormat parameter.
*/
public static int coin_glglue_get_internal_texture_format(final cc_glglue glw,
                                              int numcomponents,
                                              boolean compress)
{
  /*GLenum*/int format;
  if (compress) {
    switch (numcomponents) {
    case 1:
      format = GL2.GL_COMPRESSED_LUMINANCE/*_ARB*/;
      break;
    case 2:
      format = GL2.GL_COMPRESSED_LUMINANCE_ALPHA/*_ARB*/;
      break;
    case 3:
      format = GL2.GL_COMPRESSED_RGB/*_ARB*/;
      break;
    case 4:
    default:
      format = GL2.GL_COMPRESSED_RGBA/*_ARB*/;
      break;
    }
  }
  else {
    boolean usenewenums = true;//glglue_allow_newer_opengl(glw) && cc_glglue_glversion_matches_at_least(glw,1,1,0); java port
    switch (numcomponents) {
    case 1:
      format = usenewenums ? GL2.GL_LUMINANCE8 : GL2.GL_LUMINANCE;
      break;
    case 2:
      format = usenewenums ? GL2.GL_LUMINANCE8_ALPHA8 : GL2.GL_LUMINANCE_ALPHA;
      break;
    case 3:
      format = usenewenums ? GL2.GL_RGB8 : GL2.GL_RGB;
      break;
    case 4:
    default:
      format = usenewenums ? GL2.GL_RGBA8 : GL2.GL_RGBA;
      break;
    }
  }
  return format;
}


public static void
cc_glglue_glTexImage3D(final cc_glglue  w,
                       /*GLenum*/int target,
                       int level,
                       /*GLenum*/int internalformat,
                       /*GLsizei*/int width,
                       /*GLsizei*/int height,
                       /*GLsizei*/int depth,
                       int border,
                       /*GLenum*/int format,
                       /*GLenum*/int type,
                       byte[] pixels)
{
  //assert(w->glTexImage3D);
  glTexImage3D(target, level, internalformat,
                  width, height, depth, border,
                  format, type, Buffers.newDirectByteBuffer(pixels));
}

/*!
Returns the \e theoretical maximum dimensions for an offscreen
buffer.

Note that we're still not guaranteed that allocation of this size
will succeed, as that is also subject to e.g. memory constraints,
which is something that will dynamically change during the running
time of an application.

So the values returned from this function should be taken as hints,
and client code of cc_glglue_context_create_offscreen() and
cc_glglue_context_make_current() should re-request offscreen
contexts with lower dimensions if any of those fails.
*/
public static void
cc_glglue_context_max_dimensions(int[] width, int[] height)
{
	width[0] = 4096;
	height[0] = 4096;
}

/* This abomination is needed to support SoOffscreenRenderer::getDC(). */
public static Object
cc_glglue_win32_HDC(Object ctx)
{
//#if defined(HAVE_WGL)
//  return wglglue_context_win32_HDC(ctx); TODO
//#else /* not WGL */
  return null;
//#endif /* not WGL */
}

static int d = -1;
/* Return value of COIN_DEBUG_GLGLUE environment variable. */
public static int
coin_glglue_debug()
{
  if (d == -1) { d = glglue_resolve_envvar("COIN_DEBUG_GLGLUE"); }
  return (d > 0) ? 1 : 0;
}

/* Resolve and return the integer value of an environment variable. */
public static int
glglue_resolve_envvar(String txt)
{
  String val = TidBits.coin_getenv(txt);
  return val != null ? Util.atoi(val) : 0;
}

/* Simple utility function for dumping the current set of error codes
   returned from glGetError(). Returns number of errors reported by
   OpenGL. */

public static int
coin_catch_gl_errors(String[] str)
{
  int errs = 0;
  int glerr = glGetError();
  while (glerr != GL_NO_ERROR) {
    if (errs < 10) {
      if (errs > 0) {
        str[0] += ' ';
      }
      str[0] += cc_glglue.coin_glerror_string(glerr);
    }
    /* ignore > 10, so we don't run into a situation were we end up
       practically locking up the app due to vast amounts of errors */
    else if (errs == 10) {
      str[0] += "... and more";
    }

    errs++;
    glerr = glGetError();
  }
  return errs;
}


}
