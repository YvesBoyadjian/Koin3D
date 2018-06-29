
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
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoGLShaderObject {

	public

		  enum ShaderType {
		    VERTEX,
		    FRAGMENT,
		    GEOMETRY
		  };

		  public String sourceHint; // either the file name or the first line of source code
		  
		  
protected
   cc_glglue  glctx;
protected /*int*/GL2 cachecontext;

private
  ShaderType shadertype;
private boolean isActiveFlag ;
private boolean paramsdirty;
private int id;

	private static int shaderid = 0;


  public abstract boolean isLoaded();
  public abstract void load(String sourceString);
  public abstract void unload();
  public abstract SoShader.Type shaderType();
  public abstract SoGLShaderParameter getNewParameter();


public SoGLShaderObject( /*int*/GL2 cachecontext)
{
  this.isActiveFlag = true;
  this.shadertype = ShaderType.VERTEX;
  this.paramsdirty = true;
  this.glctx = SoGL.cc_glglue_instance(cachecontext);
  this.cachecontext = cachecontext;
  this.id = ++shaderid;
}

public void destructor() {
	
}

public cc_glglue 
GLContext()
{
  return this.glctx;
}

public /*int*/GL2
getCacheContext() 
{
  return this.cachecontext;
}

public void
setShaderType( ShaderType type)
{
  if (this.shadertype != type) {
    this.unload();
    this.shadertype = type;
  }
}

public SoGLShaderObject.ShaderType
getShaderType()
{
  return this.shadertype;
}

public void setIsActive(boolean flag)
{
  this.isActiveFlag = flag;
}

public boolean
isActive() 
{
  return (!this.isLoaded()) ? false : this.isActiveFlag;
}

public void
setParametersDirty(boolean flag)
{
  this.paramsdirty = flag;
}

public boolean
getParametersDirty() 
{
  return this.paramsdirty;
}

public void
updateCoinParameter(SoState state, SbName name, SoShaderParameter param, int val)
{
}

public int 
getShaderObjectId() 
{
  return this.id;
}
  
}
