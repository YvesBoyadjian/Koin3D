
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

package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.SoDepthBufferElement;
import jscenegraph.coin3d.inventor.elements.SoGLDepthBufferElement;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */

/*
 * ! \class SoDepthBuffer SoDepthBuffer.h \brief The SoDepthBuffer class is a
 * node used to control the GL depth buffer.
 *
 * With this node you can control properties related to the OpenGL depth buffer
 * in a variety of ways.
 *
 * - you can enable and disable depth buffer testing during rendering,
 *
 * - you can enable and disable writing to the depth buffer during rendering,
 *
 * - you can set the function used for the depth buffer testing, and
 *
 * - you can set the value range used in the depth buffer.
 *
 * The value range setting is useful if you need to segment the 3D world into
 * different segments with different depth buffer resolutions to get a more
 * optimal depth buffer resolution distribution than what a single, uniform
 * depth buffer value range can give you.
 *
 * \ingroup nodes \COIN_CLASS_EXTENSION \since Coin 3.0
 */

public class SoDepthBuffer extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoDepthBuffer.class, this);

	public static SoType getClassTypeId() /* Returns class type id */
	{
		return SoSubNode.getClassTypeId(SoDepthBuffer.class);
	}

	@Override
	public SoType getTypeId() /* Returns type id */
	{
		return nodeHeader.getClassTypeId();
	}

	@Override
	public SoFieldData getFieldData() {
		return nodeHeader.getFieldData();
	}

	public static SoFieldData[] getFieldDataPtr() {
		return SoSubNode.getFieldDataPtr(SoDepthBuffer.class);
	}

	public enum DepthWriteFunction {
    NEVER(SoDepthBufferElement.DepthWriteFunction.NEVER.getValue()),
    ALWAYS ( SoDepthBufferElement.DepthWriteFunction.ALWAYS.getValue()),
    LESS ( SoDepthBufferElement.DepthWriteFunction.LESS.getValue()),
    LEQUAL ( SoDepthBufferElement.DepthWriteFunction.LEQUAL.getValue()),
    EQUAL ( SoDepthBufferElement.DepthWriteFunction.EQUAL.getValue()),
    GEQUAL ( SoDepthBufferElement.DepthWriteFunction.GEQUAL.getValue()),
    GREATER ( SoDepthBufferElement.DepthWriteFunction.GREATER.getValue()),
    NOTEQUAL ( SoDepthBufferElement.DepthWriteFunction.NOTEQUAL.getValue());

    private int value;

    DepthWriteFunction(int value) {
    	this.value = value;
    }
    public int getValue() {
    	return value;
    }
  };

	public final SoSFBool test = new SoSFBool();
	public final SoSFBool write = new SoSFBool();
	public final SoSFEnum function = new SoSFEnum();
	public final SoSFVec2f range = new SoSFVec2f();

	/**
	 *
	 */
	public SoDepthBuffer() {
		nodeHeader.SO_NODE_CONSTRUCTOR(/* SoDepthBuffer.class */);

		nodeHeader.SO_NODE_ADD_FIELD(test, "test", (true));
		nodeHeader.SO_NODE_ADD_FIELD(write, "write", (true));
		nodeHeader.SO_NODE_ADD_FIELD(function, "function", (SoDepthBuffer.DepthWriteFunction.LESS.getValue()));
		nodeHeader.SO_NODE_ADD_FIELD(range, "range", (new SbVec2f(0.0f, 1.0f)));

		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.NEVER);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.ALWAYS);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.LESS);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.LEQUAL);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.EQUAL);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.GEQUAL);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.GREATER);
		nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(DepthWriteFunction.NOTEQUAL);
		nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(function, "function", "DepthWriteFunction");
	}

	// Doc from parent
@Override
public void
GLRender(SoGLRenderAction  action)
{
  SoState  state = action.getState();
		boolean testenable = this.test.getValue();
		boolean writeenable = this.write.getValue();
		SoDepthBufferElement.DepthWriteFunction function =
				SoDepthBufferElement.DepthWriteFunction.fromValue(this.function.getValue());
  SbVec2f depthrange = this.range.getValue();

  // accommodate for ignored fields
  if (this.test.isIgnored()) {
			testenable = SoDepthBufferElement.getTestEnable(state);
  }
  // if we're rendering transparent objects, let SoGLRenderAction decide if
  // depth write should be enabled
  if (this.write.isIgnored() || action.isRenderingTranspPaths()) {
			writeenable = SoDepthBufferElement.getWriteEnable(state);
  }
  if (this.function.isIgnored()) {
			function = SoDepthBufferElement.getFunction(state);
  }
  if (this.range.isIgnored()) {
			range.setValue(SoDepthBufferElement.getRange(state));
  }

  // update element
		SoDepthBufferElement.set(state, testenable, writeenable,
                            function, depthrange);
}

	public static void initClass() {
		/* SO_NODE_INTERNAL_INIT_CLASS */SO__NODE_INIT_CLASS(SoDepthBuffer.class, "DepthBuffer",
				SoNode.class/* SO_FROM_COIN_3_0 */);

		SO_ENABLE(SoGLRenderAction.class, SoGLDepthBufferElement.class);
	}

}
