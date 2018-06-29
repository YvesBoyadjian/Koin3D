
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

package jscenegraph.coin3d.inventor.elements.gl;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoTexture3EnabledElement;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoGLTexture3EnabledElement Inventor/elements/SoGLTexture3EnabledElement.h
  \brief The SoGLTexture3EnabledElement class is an element which controls
  whether 3D texturing is enabled or not.
  \ingroup elements

  \COIN_CLASS_EXTENSION

  \since Coin 2.0
  \since TGS Inventor 2.6
*/

public class SoGLTexture3EnabledElement extends SoTexture3EnabledElement {

	private SoState state;

	/**
	 * 
	 */
	public SoGLTexture3EnabledElement() {
		super();
	}


/*!
  Sets the state of this element.
*/
public static void
set(SoState state,
                               /*SoNode * const node,*/
                               boolean enabled)
{
	SoTexture3EnabledElement.set(state,/* node,*/ enabled);
  SoShapeStyleElement.setTexture3Enabled(state, enabled);
}


// doc from parent
public void
init(SoState  stateptr)
{
  super.init(stateptr);
  this.state = stateptr;
}

// Documented in superclass. Overridden to track GL state.
public void
push(SoState  stateptr)
{
  SoGLTexture3EnabledElement  prev = (SoGLTexture3EnabledElement) this.getNextInStack();

  this.data = prev.data;
  this.state = stateptr;
  // capture previous element since we might or might not change the
  // GL state in set/pop
  prev.capture(stateptr);
}

// Documented in superclass. Overridden to track GL state.
public void
pop(SoState  stateptr,
                                final SoElement prevTopElement)
{
  SoGLTexture3EnabledElement prev = (SoGLTexture3EnabledElement) prevTopElement;
  if (this.data != prev.data) {
    this.updategl();
  }
}

/*!
  Return current state of this element.
*/
public static boolean
get(SoState state)
{
  return (boolean) SoTexture3EnabledElement.get(state);
}

/*!
  Returns default state of this element (FALSE).
*/
public static boolean
getDefault()
{
  return SoTexture3EnabledElement.getDefault();
}

public void
setElt(int value)
{
  if (this.data != value) {
    this.data = value;
    this.updategl();
  }
}


//
// updates GL state if needed
//
public void
updategl()
{
  final cc_glglue glw = SoGL.sogl_glue_instance(this.state);
  
  GL2 gl2 = glw.getGL2();

  if (SoGLDriverDatabase.isSupported(glw, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    if (this.data != 0) gl2.glEnable(GL2.GL_TEXTURE_3D);
    else gl2.glDisable(GL2.GL_TEXTURE_3D);
  }
}
	
}
