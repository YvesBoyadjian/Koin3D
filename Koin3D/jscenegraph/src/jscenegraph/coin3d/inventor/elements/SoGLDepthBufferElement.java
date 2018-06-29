
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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */

/*
 * ! \class SoGLDepthBufferElement Inventor/elements/SoGLDepthBufferElement.h
 * \brief The SoGLDepthBufferElement controls the OpenGL depth buffer.
 *
 * \ingroup elements \COIN_CLASS_EXTENSION \since Coin 3.0
 */

public class SoGLDepthBufferElement extends SoDepthBufferElement {

	/**
	 *
	 */
	public SoGLDepthBufferElement() {
		// nothing to do
	}

	/*
	 * ! Internal Coin method.
	 */
	@Override
	public void init(SoState state) {
		super.init(state);
	}

	/*
	 * ! Internal Coin method.
	 */
	@Override
	public void push(SoState state) {
		SoGLDepthBufferElement prev = (SoGLDepthBufferElement) (this.getNextInStack());
		this.test = prev.test;
		this.write = prev.write;
		this.function = prev.function;
		this.range.copyFrom(prev.range);
		prev.capture(state);
	}

	/*
	 * ! Internal Coin method.
	 */
	@Override
	public void pop(SoState state, SoElement prevTopElement) {
		SoGLDepthBufferElement prev = (SoGLDepthBufferElement) (prevTopElement);
		if (this.test != prev.test || this.write != prev.write || this.function != prev.function
				|| this.range.operator_not_equal(prev.range)) {
			this.updategl(state.getGL2());
		}
	}

	/*
	 * ! Set this element's values.
	 */
	@Override
	public void setElt(GL2 gl2, boolean test, boolean write, DepthWriteFunction function, final SbVec2f range) {
		boolean update = (test != this.test) || (write != this.write) || (function != this.function)
				|| (range.operator_not_equal(this.range));

		super.setElt(gl2, test, write, function, range);

		if (update) {
			this.updategl(gl2);
		}
	}

	/*
	 * ! This method performs the OpenGL updates.
	 */
	public void updategl(GL2 gl2) {
		if (this.test) {
			gl2.glEnable(GL.GL_DEPTH_TEST);
		} else {
			gl2.glDisable(GL.GL_DEPTH_TEST);
		}

		if (this.write) {
			gl2.glDepthMask(GL.GL_TRUE != 0);
		} else {
			gl2.glDepthMask(GL.GL_FALSE != 0);
		}

		switch (this.function) {
		case NEVER:
			gl2.glDepthFunc(GL.GL_NEVER);
			break;
		case ALWAYS:
			gl2.glDepthFunc(GL.GL_ALWAYS);
			break;
		case LESS:
			gl2.glDepthFunc(GL.GL_LESS);
			break;
		case LEQUAL:
			gl2.glDepthFunc(GL.GL_LEQUAL);
			break;
		case EQUAL:
			gl2.glDepthFunc(GL.GL_EQUAL);
			break;
		case GEQUAL:
			gl2.glDepthFunc(GL.GL_GEQUAL);
			break;
		case GREATER:
			gl2.glDepthFunc(GL.GL_GREATER);
			break;
		case NOTEQUAL:
			gl2.glDepthFunc(GL.GL_NOTEQUAL);
			break;
		default: // assert(!"unknown depth function");
		}

		gl2.glDepthRange(this.range.getValue()[0], this.range.getValue()[1]);
	}
}
