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

package jscenegraph.coin3d.inventor;

import jscenegraph.port.Mutable;

/*!
  \class SbVec2i32 SbLinear.h Inventor/SbLinear.h
  \brief The SbVec2i32 class is a 2 dimensional vector with short integer 
  coordinates.
  \ingroup base

  This vector class is used by many other classes in
  Coin. It provides storage for a vector in 2 dimensions
  as well as simple integer arithmetic operations.

  \sa SbVec2f, SbVec2d, SbVec3s, SbVec3f, SbVec3d, SbVec4f, SbVec4d.
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2i32 implements Mutable {

protected final int[] vec = new int[2];

  public SbVec2i32() { }
  public SbVec2i32( int v[]) { vec[0] = v[0]; vec[1] = v[1]; }
  public SbVec2i32(int x, int y) { vec[0] = x; vec[1] = y; }
  
@Override
public void copyFrom(Object other) {
	SbVec2i32 otherVec = (SbVec2i32)other;
	vec[0] = otherVec.vec[0];
	vec[1] = otherVec.vec[1];
}

	/**
	 * java port
	 * @return
	 */
	public int[] getValue() {
		return vec;
	}
}
