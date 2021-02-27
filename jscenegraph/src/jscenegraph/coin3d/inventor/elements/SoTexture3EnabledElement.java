
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

import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoGLTextureEnabledElement Inventor/elements/SoGLTextureEnabledElement.h
  \brief The SoGTexture3EnabledElement class is a an element which stores
  whether 3D texturing is enabled or not.
  \ingroup elements

  \COIN_CLASS_EXTENSION

  \sa SoTextureEnabledElement
  \since Coin 2.0
  \since TGS Inventor 2.6
*/

public class SoTexture3EnabledElement extends SoInt32Element {


/*!
  Sets the state of this element.
*/
public static void
set(SoState state,
                               /*SoNode node,*/
                               boolean enabled)
{
	SoInt32Element.set(classStackIndexMap.get(SoTexture3EnabledElement.class), state/*, node*/, (enabled ? 1 : 0));
}


// doc from parent
public void
init(SoState state)
{
  this.data = SoTexture3EnabledElement.getDefault() ? 1 : 0;
}

/*!
  Return current state of this element.
*/
public static boolean
get(SoState state)
{
  return (SoInt32Element.get(classStackIndexMap.get(SoTexture3EnabledElement.class), state))!=0;
}

/*!
  Returns default state of this element (FALSE).
*/
public static boolean
getDefault()
{
  return false;
}

public void
setElt(int value)
{
  this.data = value;
}
}
