
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

package jscenegraph.coin3d.inventor.misc;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;

/**
 * @author Yves Boyadjian
 *
 */

/*!
  \class SoGLBigImage include/Inventor/misc/SoGLBigImage.h
  \brief The SoGLBigImage class is used to handle 2D OpenGL textures of any size.

  This class is internal. To enable/disable big-image texture handling
  you should use the SoTextureScalePolicy node.

  The technique used is the following: split the texture into x*y
  equal size blocks. All these subtextures are of size 2^n, and are
  typically quite small (256x256 or smaller).  Each triangle is
  clipped, based on the texture coordinates, into several smaller
  triangles. The triangles will then be guaranteed to use only one
  subtexture. Then the triangles are projected onto the screen, and
  the maximum projected size for each subtexture is
  calculated. Subtextures outside the viewport will be culled. Each
  subtexture is then sampled down to a 2^n value close to the
  projected size, and a GL texture is created with this size. This GL
  texture is used when rendering triangles that are clipped into that
  subtexture.

  Mipmapping is disabled for SoGLBigImage. Aliasing problems shouldn't
  occur because the projected size of the texture is calculated on the
  fly.  When mipmapping is enabled, the amount of texture memory used
  is doubled, and creating the texture object is much slower, so we
  avoid this for SoGLBigImage.

  \COIN_CLASS_EXTENSION

  \since Coin 2.0
*/

public class SoGLBigImage extends SoGLImage {
	
  private static final SoType classTypeId = new SoType();

//the number of subtextures that can be changed (resized) each frame.
//By keeping this number small, we avoid slow updates when zooming in
//on an image, as only few textures are changed each frame.
static int CHANGELIMIT = 4;

	

/*!
  \COININTERNAL
*/
public static void
initClass()
{
  assert(classTypeId.isBad());
  classTypeId.copyFrom(
    SoType.createType(SoGLImage.getClassTypeId(), new SbName("GLBigImage")));
  //coin_atexit((coin_atexit_f*) soglbigimagep_cleanup, CC_ATEXIT_NORMAL); TODO
}

// Doc in superclass.
public static SoType
getClassTypeId()
{
  assert(!classTypeId.isBad());
  return classTypeId;
}

// Doc in superclass.
public SoType
getTypeId() 
{
  return SoGLBigImage.getClassTypeId();
}

/*!
  Sets the change limit. Returns the old limit.
  
  \sa exceededChangeLimit()
  \since Coin 2.3
*/
public static int
setChangeLimit( int limit)
{
  int old = CHANGELIMIT;
  CHANGELIMIT = limit;
  return old;
}

}
