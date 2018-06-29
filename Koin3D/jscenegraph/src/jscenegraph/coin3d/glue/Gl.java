
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

}
