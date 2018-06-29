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

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
/*
 * ! \class SoDepthBufferElement Inventor/elements/SoDepthBufferElement.h \brief
 * The SoDepthBufferElement controls the depth buffer settings.
 *
 * \ingroup elements \COIN_CLASS_EXTENSION \since Coin 3.0
 */

public class SoDepthBufferElement extends SoElement {

	public enum DepthWriteFunction {
		NEVER, ALWAYS, LESS, LEQUAL, EQUAL, GEQUAL, GREATER, NOTEQUAL;

		public int getValue() {
			return ordinal();
		}

		public static DepthWriteFunction fromValue(int value) {
			return DepthWriteFunction.values()[value];
		}
	};

	protected boolean test;
	protected boolean write;
	protected DepthWriteFunction function;
	protected final SbVec2f range = new SbVec2f();

	/**
	 *
	 */
	public SoDepthBufferElement() {
		// nothing to do
	}

	/*
	 * ! Internal Coin method.
	 */
	@Override
	public void init(SoState state) {
		super.init(state);
		this.test = true;
		this.write = true;
		this.function = DepthWriteFunction.LEQUAL;
		this.range.setValue(0.0f, 1.0f);
	}

	/*
	 * ! Internal Coin method.
	 */
@Override
public void
push(SoState  state)
{
  final SoDepthBufferElement  prev = (SoDepthBufferElement)
    (
     this.getNextInStack()
     );
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
	public void pop(SoState state, final SoElement prevTopElement) {
	}

	/*
	 * ! Set this element's values.
	 */
	public static void set(SoState state, boolean test, boolean write, DepthWriteFunction function,
			final SbVec2f range) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));

		elem.setElt(state.getGL2(), test, write, function, range);
	}

	/*
	 * ! Fetches this element's values.
	 */
	public void get(SoState state, final boolean[] test_out, final boolean[] write_out,
			final DepthWriteFunction[] function_out, final SbVec2f range_out) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getConstElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));
		test_out[0] = elem.test;
		write_out[0] = elem.write;
		function_out[0] = elem.function;
		range_out.copyFrom(elem.range);
	}

	/*
	 * ! Returns the depth test enabled state.
	 */
	public static boolean getTestEnable(SoState state) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getConstElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));
		return elem.test;
	}

	/*
	 * ! Returns the depth buffer write enabled state.
	 */
	public static boolean getWriteEnable(SoState state) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getConstElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));
		return elem.write;
	}

	/*
	 * ! Returns the set depth buffer write function.
	 */
	public static SoDepthBufferElement.DepthWriteFunction getFunction(SoState state) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getConstElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));
		return elem.function;
	}

	/*
	 * ! Returns the depth buffer value range used.
	 */
	public static SbVec2f getRange(SoState state) {
		SoDepthBufferElement elem = (SoDepthBufferElement) (SoElement.getConstElement(state,
				classStackIndexMap.get(SoDepthBufferElement.class)));
		return new SbVec2f(elem.range);
	}

	/*
	 * ! Internal Coin method.
	 */
@Override
public boolean
matches(final SoElement  element)
{
  SoDepthBufferElement  elem =
    ( SoDepthBufferElement )(element);

  return (elem.test == this.test)
    && (elem.write == this.write)
    && (elem.function == this.function)
    && (elem.range.operator_equal_equal(this.range));
}

	/*
	 * ! Internal Coin method.
	 */
@Override
	public SoElement copyMatchInfo()
{
		SoDepthBufferElement elem = (SoDepthBufferElement) (this.getTypeId().createInstance());
		elem.test = this.test;
		elem.write = this.write;
		elem.function = this.function;
		elem.range.copyFrom(this.range);
  return elem;
}

/*!
  Virtual method to set the state to get derived elements updated.
*/
public void

			setElt(GL2 gl2, boolean test, boolean write, DepthWriteFunction function, SbVec2f range)
{
  this.test = test;
  this.write = write;
  this.function = function;
		this.range.copyFrom(range);
}

}
