
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

package jscenegraph.coin3d.shaders.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoReplacedElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLShaderProgramElement extends SoReplacedElement {

	public SoGLShaderProgram shaderProgram;
	
	private final SbList<Integer> objectids = new SbList<Integer>();
	private boolean enabled;
	
public void destructor()
{
  this.shaderProgram = null;
  super.destructor();
}

public void
init(SoState state)
{
  super.init(state);
  this.shaderProgram = null;
  this.enabled = false;
}

public static void
enable(SoState state, boolean onoff)
{
  SoGLShaderProgramElement element =
    (SoGLShaderProgramElement) SoElement.getElement(state,classStackIndexMap.get(SoGLShaderProgramElement.class));
  element.enabled = onoff;
  element.objectids.truncate(0);
  
  if (element.shaderProgram != null) {
    if (onoff) {
      if (!element.shaderProgram.isEnabled()) element.shaderProgram.enable(state);
    }
    else {
      if (element.shaderProgram.isEnabled()) element.shaderProgram.disable(state);
    }
    element.shaderProgram.getShaderObjectIds(element.objectids);
  }
}

public static void
set(SoState state, SoNode node,
                              SoGLShaderProgram program)
{
  SoGLShaderProgramElement element =
    (SoGLShaderProgramElement)SoReplacedElement.getElement(state,classStackIndexMap.get(SoGLShaderProgramElement.class), node);
  
  if (program != element.shaderProgram) {
    if (element.shaderProgram != null) element.shaderProgram.disable(state);
  }
  element.shaderProgram = program;
  element.enabled = false;
  element.objectids.truncate(0);
  if (program != null) program.getShaderObjectIds(element.objectids);
  // don't enable new program here. The node will call enable()
  // after setting up all the objects
}

public static SoGLShaderProgram 
get(SoState state)
{
  final SoElement element = getConstElement(state, classStackIndexMap.get(SoGLShaderProgramElement.class));
  assert(element!=null);
  return (( SoGLShaderProgramElement )element).shaderProgram;
}

public void
push(SoState state)
{
  SoGLShaderProgramElement  prev = (SoGLShaderProgramElement ) getNextInStack();
  assert(prev!=null);
  this.shaderProgram = prev.shaderProgram;
  this.enabled = prev.enabled;
  this.nodeId = prev.nodeId;
  this.objectids.operator_assign( prev.objectids);
  // capture previous element since we might or might not change the
  // GL state in set/pop
  prev.capture(state);
}

public void
pop(SoState state, final SoElement prevTopElement)
{
  SoGLShaderProgramElement  elem = (SoGLShaderProgramElement )prevTopElement;
  if (this.shaderProgram != elem.shaderProgram) {
    if (elem.shaderProgram != null) {
      elem.shaderProgram.disable(state);
      elem.enabled = false;
    }
    if (this.shaderProgram != null) {
      if (this.enabled) this.shaderProgram.enable(state);
    }
  }
  else if (this.shaderProgram != null) {
    if (this.enabled != elem.enabled) {
      if (this.enabled) this.shaderProgram.enable(state);
      else this.shaderProgram.disable(state);
    }
  }
  elem.shaderProgram = null;
}


public boolean
matches(final SoElement element)
{
  SoGLShaderProgramElement elem = (SoGLShaderProgramElement) element;
  return (this.enabled == elem.enabled) && (this.objectids.operator_equal_equal(elem.objectids));
}

public SoElement 
copyMatchInfo() 
{
  SoGLShaderProgramElement elem = 
    (SoGLShaderProgramElement) super.copyMatchInfo();
  
  elem.enabled = this.enabled;
  elem.objectids.operator_assign( this.objectids);
  return elem;
}
	
}
