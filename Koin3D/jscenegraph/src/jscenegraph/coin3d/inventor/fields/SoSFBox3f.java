
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

package jscenegraph.coin3d.inventor.fields;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoSField;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoSFBox3f SoSFBox3f.h Inventor/fields/SoSFBox3f.h
  \brief The SoSFBox3f class is a container for an SbBox3f vector.
  \ingroup fields

  This field is used where nodes, engines or other field containers
  needs to store a box.

  \COIN_CLASS_EXTENSION
  \since Coin 2.0
*/

public class SoSFBox3f extends SoSField<SbBox3f> {

	@Override
	protected SbBox3f constructor() {
		return new SbBox3f();
	}

	@Override
	public boolean readValue(SoInput in) {
  float[] min = new float[3];
  float[] max = new float[3];
  if (!in.read((double x) -> {min[0] = (float)x;}) ||
      !in.read((double x) -> {min[1] = (float)x;}) ||
      !in.read((double x) -> {min[2] = (float)x;}) ||
      !in.read((double x) -> {max[0] = (float)x;}) ||
      !in.read((double x) -> {max[1] = (float)x;}) ||
      !in.read((double x) -> {max[2] = (float)x;})) {
    SoReadError.post(in, "Couldn't read SoSFBox3f");
    return false;
  }
  this.setValue(min[0], min[1], min[2], max[0], max[1], max[2]);
  return true;
	}

/*!
  Set value of vector.
*/
public void
setValue(float xmin, float ymin, float zmin,
                    float xmax, float ymax, float zmax)
{
  this.setValue(new SbBox3f(xmin, ymin, zmin, xmax, ymax, zmax));
}


/*!
  Set value of vector.
*/
public void
setValue(final SbVec3f minvec, final SbVec3f maxvec)
{
  this.setValue(new SbBox3f(minvec, maxvec));
}


/*!
  Set value of vector.
*/
public void
getValue(final SbBox3f box)
{
  box.copyFrom( value);
}

}
