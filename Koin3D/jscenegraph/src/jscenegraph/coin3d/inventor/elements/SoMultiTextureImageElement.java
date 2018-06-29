
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

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoTextureImageElement;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoMultiTextureImageElement Inventor/elements/SoMultiTextureImageElement.h
  \brief The SoMultiTextureImageElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.

  \COIN_CLASS_EXTENSION

  \since Coin 2.2
*/

public class SoMultiTextureImageElement extends SoElement {

	public static final int MAX_UNITS =16; // FIXME: make dynamic?????

  public static class UnitData {
  
	  public  int nodeid;
	  public final SbVec3s size = new SbVec3s();
	  public int numComponents;
	  public byte[] bytes;
	  public SoTextureImageElement.Wrap wrapS, wrapT, wrapR;
	  public SoTextureImageElement.Model model;
	  public final SbColor blendColor = new SbColor();
  };

  private final SoMultiTextureImageElement.UnitData[] unitdata = new SoMultiTextureImageElement.UnitData[MAX_UNITS];
  
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.elements.SoElement#matches(jscenegraph.database.inventor.elements.SoElement)
	 */
	@Override
	public boolean matches(SoElement elt) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.elements.SoElement#copyMatchInfo()
	 */
	@Override
	public SoElement copyMatchInfo() {
		// TODO Auto-generated method stub
		return null;
	}

public SoMultiTextureImageElement.UnitData 
getUnitData(int unit) 
{
  assert(unit >= 0 && unit < MAX_UNITS);
  return /*PRIVATE(this)->*/unitdata[unit];
}

}
