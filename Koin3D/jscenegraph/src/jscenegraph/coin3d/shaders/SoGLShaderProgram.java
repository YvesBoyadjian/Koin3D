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

package jscenegraph.coin3d.shaders;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderProgramEnableCB;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLShaderProgram {

	//private SoGLARBShaderProgram  arbShaderProgram; TODO
	//private SoGLCgShaderProgram   cgShaderProgram; TODO
	private SoGLSLShaderProgram   glslShaderProgram;
	
	private boolean isenabled;
	private SoShaderProgramEnableCB enablecb;
	private Object enablecbclosure;
	private final SbList <Integer> objectids = new SbList<>();
	

public SoGLShaderProgram()
{
  //this.arbShaderProgram = new SoGLARBShaderProgram;
  //this.cgShaderProgram = new SoGLCgShaderProgram;
  this.glslShaderProgram = new SoGLSLShaderProgram();

  this.isenabled = false;
  this.enablecb = null;
  this.enablecbclosure = null;
}

public void destructor()
{
  //delete this.arbShaderProgram;
  //delete this.cgShaderProgram;
  this.glslShaderProgram.destructor();
}

public void
addShaderObject(SoGLShaderObject shader)
{
  this.objectids.append(shader.getShaderObjectId());
  switch (shader.shaderType()) {
  case ARB_SHADER:
    //this.arbShaderProgram.addShaderObject((SoGLARBShaderObject)shader);
    break;
  case CG_SHADER:
    //this.cgShaderProgram.addShaderObject((SoGLCgShaderObject)shader);
    break;
  case GLSL_SHADER:
    this.glslShaderProgram.addShaderObject((SoGLSLShaderObject)shader);
    break;
  default:
    //assert(false && "shaderType unknown!");
  }
}

public void
removeShaderObjects()
{
  //this.arbShaderProgram.removeShaderObjects();
  //this.cgShaderProgram.removeShaderObjects();
  this.glslShaderProgram.removeShaderObjects();
  this.glslShaderProgram.removeProgramParameters();
  this.objectids.truncate(0);
}

public void
enable(SoState  state)
{
    int cachecontext = SoGLCacheContextElement.get(state);
    cc_glglue glctx = SoGL.cc_glglue_instance(cachecontext);

  //this.arbShaderProgram.enable();
  //this.cgShaderProgram.enable();
  this.glslShaderProgram.enable(glctx);

  this.isenabled = true;
  if (this.enablecb != null) {
    this.enablecb.invoke(this.enablecbclosure, state, true);
  }
}

public void
disable(SoState state)
{
    int cachecontext = SoGLCacheContextElement.get(state);
    cc_glglue glctx = SoGL.cc_glglue_instance(cachecontext);

  //this.arbShaderProgram.disable();
  //this.cgShaderProgram.disable();
  this.glslShaderProgram.disable(glctx);

  this.isenabled = false;
  if (this.enablecb != null) {
    this.enablecb.invoke(this.enablecbclosure, state, false);
  }
}

public boolean 
isEnabled()  
{
  return this.isenabled;
}

public void
setEnableCallback(SoShaderProgramEnableCB  cb,
                                     Object closure)
{
  this.enablecb = cb;
  this.enablecbclosure = closure;
}

public void
updateCoinParameter(SoState state,  final SbName name,   int value)
{
  if (this.glslShaderProgram != null) {
    boolean enabled = this.isenabled;
    if (!enabled) this.enable(state);
    this.glslShaderProgram.updateCoinParameter(state, name, value);
    if (!enabled) this.disable(state);
  }
}

public void
addProgramParameter(int name, int value)
{
  if (this.glslShaderProgram != null) {
    this.glslShaderProgram.addProgramParameter(name, value);
  }
}

public void 
getShaderObjectIds(final SbList <Integer> ids)  
{
  ids.operator_assign(this.objectids);
}

public int 
getGLSLShaderProgramHandle(SoState state)  
{
    int cachecontext = SoGLCacheContextElement.get(state);
    cc_glglue glctx = SoGL.cc_glglue_instance(cachecontext);
 
  return this.glslShaderProgram.getProgramHandle(glctx);
}

public boolean 
glslShaderProgramLinked()  
{
  if (this.glslShaderProgram != null) {
    return this.glslShaderProgram.neededLinking();
  }
  return false;
}

	
}
