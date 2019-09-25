
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
import com.jogamp.opengl.GL3;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter1i;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLSLShaderObject extends SoGLShaderObject {

	int programHandle;
	private	  /*COIN_GLhandle*/int shaderHandle;
	private	  boolean isattached;
	int programid;
		  
	private static int soglshaderobject_idcounter = 1;


public SoGLSLShaderObject( int cachecontext) {
  super(cachecontext);

  this.programHandle = 0;
  this.shaderHandle = 0;
  this.isattached = false;
  this.programid = 0;
}

public void destructor()
{
  // make sure we don't detach, since the program might have been
  // destructed already. FIXME: investigate if not calling detach will
  // lead to memory leaks. pederb, 2006-10-17
  this.isattached = false;
  this.unload();
  super.destructor();
}

// *************************************************************************

public SoShader.Type
shaderType() 
{
  return SoShader.Type.GLSL_SHADER;
}

public boolean
isLoaded() 
{
  return (this.shaderHandle != 0);
}

public void
load(String srcStr)
{
  this.unload();
  this.setParametersDirty(true);

  final int[] flag = new int[1];
  /*GLenum*/int sType;

  switch (this.getShaderType()) {
  default:
    throw new IllegalStateException(" unknown shader type");
  case VERTEX:
    sType = GL3.GL_VERTEX_SHADER/*_ARB*/;
    break;
  case FRAGMENT:
    sType = GL3.GL_FRAGMENT_SHADER/*_ARB*/;
    break;
  case GEOMETRY:
    sType = GL3.GL_GEOMETRY_SHADER/*_EXT*/;
    break;
  }

  this.shaderHandle = this.glctx.glCreateShaderObjectARB(sType);
  this.programid = 0;

  if (this.shaderHandle == 0) return;
  this.programid = soglshaderobject_idcounter++;

  this.glctx.glShaderSourceARB(this.shaderHandle, 1, (String)srcStr, null);
  this.glctx.glCompileShaderARB(this.shaderHandle);

  if (SoGLSLShaderObject.didOpenGLErrorOccur("SoGLSLShaderObject::load()",glctx)) {
    this.shaderHandle = 0;
    return;
  }

  this.glctx.glGetObjectParameterivARB(this.shaderHandle,
                                         GL2.GL_OBJECT_COMPILE_STATUS_ARB,
                                         flag);
  SoGLSLShaderObject.printInfoLog(this.GLContext(), this.shaderHandle,
                                   this.getShaderType().ordinal());

  if (flag[0]==0) this.shaderHandle = 0;
}

public void
unload()
{
  this.detach();
  if (this.shaderHandle != 0) { this.glctx.glDeleteObjectARB(this.shaderHandle); }
  this.shaderHandle = 0;
  this.programHandle = 0;
  this.programid = 0;
}

public SoGLShaderParameter 
getNewParameter() 
{
  return new SoGLSLShaderParameter();
}

// *************************************************************************

public void
attach(/*COIN_GLhandle*/int programHandle)
{
  if (programHandle <= 0 || this.programHandle == programHandle) return;

  detach();

  if (this.shaderHandle != 0) {
    this.programHandle = programHandle;
    this.glctx.glAttachObjectARB(this.programHandle, this.shaderHandle);
    this.isattached = true;
  }
}

public void
detach()
{
  if (this.isattached && this.programHandle != 0 && this.shaderHandle != 0) {
    this.glctx.glDetachObjectARB(this.programHandle, this.shaderHandle);
    this.isattached = false;
    this.programHandle = 0;
  }
}

public boolean
isAttached()
{
  return this.isattached;
}

public static void
printInfoLog( cc_glglue g, /*COIN_GLhandle*/int handle, int objType)
{
  final int[] length = new int[1];

  g.glGetObjectParameterivARB(handle, GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, length);

  if (length[0] > 1) {
    /*COIN_GLchar*/byte[] infoLog = new /*COIN_GLchar*/byte[length[0]];
    final /*GLsizei*/int[] charsWritten = new int[1];
    g.glGetInfoLogARB(handle, length[0], charsWritten, infoLog);
    String s = ("GLSL");
    switch (objType) {
    case 0: s += "vertexShader "; break;
    case 1: s += "fragmentShader "; break;
    case 2: s += "geometryShader "; break;
    default: ;// do nothing
    }
    SoDebugError.postInfo("SoGLSLShaderObject::printInfoLog",
                           s+" log: '"+Util.toString(infoLog)+"'");
    //delete [] infoLog; java port
  }
}

public static boolean
didOpenGLErrorOccur( String source, cc_glglue g)
{
  boolean retCode = false;
  boolean glerror_debug = SoGL.sogl_glerror_debugging();

  // only do a glFlush if COIN_GLERROR_DEBUGGING is set since it can
  // degrade performance a lot. If glFlush is not executed here, gl
  // errors from the shaders might not get caught until after the
  // geometry is rendered, which makes debugging really confusing.
  if (glerror_debug) {
    g.getGL2().glFlush();
  } 

  /*GLenum*/int glErr = g.getGL2().glGetError();
  while (glErr != GL2.GL_NO_ERROR) {
    SoDebugError.post(source, "error: '"+g.coin_glerror_string(glErr)+"' "+(glerror_debug ? "":
                       "(set envvar COIN_GLERROR_DEBUGGING=1 "+
                       "and re-run to get more information)"));
    
    retCode = /*true*/false; // YB TODO
    glErr = g.getGL2().glGetError();
  }
  return retCode;
}

//#include <stdio.h>
//#include <Inventor/SbName.h>
//#include <Inventor/nodes/SoShaderParameter.h>
//#include <Inventor/elements/SoGLTextureImageElement.h>
//#include <Inventor/elements/SoLightModelElement.h>
//#include <Inventor/actions/SoAction.h>
//#include <stdio.h>

public void 
updateCoinParameter(SoState state, final SbName name, SoShaderParameter  param, int value)
{
  /*COIN_GLhandle*/int pHandle = this.programHandle;
  if (pHandle != 0) {
    cc_glglue glue = this.GLContext();
    
    // FIXME: set up a dict for the supported Coin variables 
    SoShaderParameter1i p = (SoShaderParameter1i) param;
    
    if (p != null) {
      if (p.value.getValue() != value) p.value.operator_assign(value);
    }
    else {
      int location = glue.glGetUniformLocationARB(pHandle,
                                                     (/*const COIN_GLchar **/String)name.getString());
      
//#if 0
//      fprintf(stderr,"action: %s, name: %s, loc: %d, handle: %p\n", 
//              state.getAction().getTypeId().getName().getString(),
//              name.getString(), location, pHandle);
//#endif
      if (location >= 0) {
        glue.glUniform1iARB(location, value);
      }
    }
  }
}
}
