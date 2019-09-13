
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

import java.util.Objects;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLSLShaderParameter implements SoGLShaderParameter {
	
private
  int location;
private String cacheName;
private /*GLsizei*/int cacheSize;
private /*GLenum*/int cacheType;
private boolean isActive;
private int programid;

	

public SoGLSLShaderParameter()
{
  this.location  = -1;
  this.cacheType = GL2.GL_FLOAT;
  this.cacheName = "";
  this.cacheSize =  0;
  this.isActive = true;
  this.programid = 0;
}

public void destructor()
{
	
}

public SoShader.Type
shaderType() 
{ 
  return SoShader.Type.GLSL_SHADER;
}

public void
set1f( SoGLShaderObject  shader,
                              float value,  String name,  int id)
{
  if (this.isValid(shader, name, GL2.GL_FLOAT))
    shader.GLContext().glUniform1fARB(this.location, value);
}

public void
set2f( SoGLShaderObject  shader,
                              float[] value,  String name,  int id)
{
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC2_ARB))
    shader.GLContext().glUniform2fARB(this.location, value[0], value[1]);
}

public void
set3f( SoGLShaderObject  shader,
                              float[] v,  String name,  int id)
{
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC3_ARB))
    shader.GLContext().glUniform3fARB(this.location, v[0], v[1], v[2]);
}

public void
set4f( SoGLShaderObject  shader,
                              float[] v,  String name,  int id)
{
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC4_ARB))
    shader.GLContext().glUniform4fARB(this.location, v[0], v[1], v[2], v[3]);
}


public void
set1fv( SoGLShaderObject  shader,  int num,
                               float[]value,  String  name,  int id)
{
  final int[] cnt = new int[1]; cnt[0] = num;
  if (this.isValid(shader, name, GL2.GL_FLOAT, cnt))
    shader.GLContext().glUniform1fvARB(this.location, cnt[0], value);
}

public void
set2fv( SoGLShaderObject  shader,  int num,
                               float[] value,  String name,  int id)
{
  final int[] cnt = new int[1]; cnt[0] = num;
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC2_ARB, cnt))
    shader.GLContext().glUniform2fvARB(this.location, cnt[0], value);
}

public void
set3fv( SoGLShaderObject  shader,  int num,
                               float[] value,  String  name,  int id)
{
  final int[] cnt = new int[1]; cnt[0] = num;
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC3_ARB, cnt))
    shader.GLContext().glUniform3fvARB(this.location, cnt[0], value);
}

public void
set4fv( SoGLShaderObject  shader,  int num,
                               float[] value,  String  name,  int id)
{
  final int[] cnt = new int[1]; cnt[0] = num;
  if (this.isValid(shader, name, GL2.GL_FLOAT_VEC4_ARB, cnt))
    shader.GLContext().glUniform4fvARB(this.location, cnt[0], value);
}

public void
setMatrix( SoGLShaderObject shader,
                                  float[] value,  String  name,
                                  int id)
{
  if (this.isValid(shader, name, GL2.GL_FLOAT_MAT4_ARB))
    shader.GLContext().glUniformMatrix4fvARB(this.location,1,false,value);
}

  
public void
setMatrixArray( SoGLShaderObject shader,
                                       int num,  float[]value,
                                       String name,  int id)
{
  final int[] cnt = new int[1]; cnt[0] = num;
  if (this.isValid(shader, name, GL2.GL_FLOAT_MAT4_ARB, cnt))
    shader.GLContext().glUniformMatrix4fvARB(this.location,cnt[0],false,value);
}


public void
set1i( SoGLShaderObject  shader,
                              int value,  String  name,  int id)
{
  if (this.isValid(shader, name, GL2.GL_INT))
    shader.GLContext().glUniform1iARB(this.location, value);
}

public void
set2i( SoGLShaderObject  shader,
                              int[] value,  String  name,
                              int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC2_ARB))
    shader.GLContext().glUniform2iARB(this.location, value[0], value[1]);
}

public void
set3i( SoGLShaderObject  shader,
                              int[] v,  String  name,
                              int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC3_ARB))
    shader.GLContext().glUniform3iARB(this.location, v[0], v[1], v[2]);
}

public void
set4i( SoGLShaderObject  shader,
                              int[] v,  String  name,
                              int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC4_ARB))
    shader.GLContext().glUniform4iARB(this.location, v[0], v[1], v[2], v[3]);
}

public void
set1iv( SoGLShaderObject  shader,
                               int num,
                               int[] value,  String  name,
                               int id)
{
  if (this.isValid(shader, name, GL2.GL_INT))
    shader.GLContext().glUniform1ivARB(this.location, num, (int[]) value);
}

public void
set2iv( SoGLShaderObject  shader,
                               int num,
                               int[] value,  String  name,
                               int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC2_ARB))
    shader.GLContext().glUniform2ivARB(this.location, num, ( int[])value);
}

public void
set3iv( SoGLShaderObject  shader,
                               int num,
                               int[] v,  String  name,
                               int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC3_ARB))
    shader.GLContext().glUniform3ivARB(this.location, num, ( int[])v);
}

public void
set4iv( SoGLShaderObject  shader,
                               int num,
                               int[] v,  String  name,
                               int id)
{
  if (this.isValid(shader, name, GL2.GL_INT_VEC4_ARB))
    shader.GLContext().glUniform4ivARB(this.location, num, ( int[])v);
}

//#include <stdio.h>
public boolean isValid( SoGLShaderObject shader, String name,
        /*GLenum*/int type) {
	return isValid(shader,name,type,null);
}
public boolean
isValid( SoGLShaderObject  shader,
                                String  name, /*GLenum*/int type,
                               final int[] num)
{
  assert(shader!=null);
  assert(shader.shaderType() == SoShader.Type.GLSL_SHADER);
  
  /*COIN_GLhandle*/int pHandle = ((SoGLSLShaderObject)shader).programHandle;
  int pId = ((SoGLSLShaderObject)shader).programid;
  
  // return true if uniform isn't active. We warned the user about
  // this when we found it to be inactive.
  if ((pId == this.programid) && (this.location > -1) && !this.isActive) return true;
  
  if ((pId == this.programid) && (this.location > -1) && 
      Objects.equals(this.cacheName,name) && (this.cacheType == type)) {
    if (num != null) { // assume: ARRAY
      if (this.cacheSize < num[0]) {
        // FIXME: better error handling - 20050128 martin
        SoDebugError.postWarning("SoGLSLShaderParameter::isValid",
                                  "parameter "+this.cacheName+"["+this.cacheSize+"] < input["+num[0]+"]!");
        num[0] = this.cacheSize;
      }
      return (num[0] > 0);
    }
    return true;
  }
  
   cc_glglue g = shader.GLContext();
  
  this.cacheSize = 0;  
  this.location = g.glGetUniformLocationARB(pHandle,
                                              ( /*COIN_GLchar **/String)name);
  this.programid = pId;
  
  if (this.location == -1)  {
//#if COIN_DEBUG
    SoDebugError.postWarning("SoGLSLShaderParameter::isValid",
                              "parameter '"+name+"' not found in program.");
//#endif // COIN_DEBUG
    return false;
  }
  final int[] activeUniforms = new int[1];
  g.glGetObjectParameterivARB(pHandle, GL2.GL_OBJECT_ACTIVE_UNIFORMS_ARB, activeUniforms);

  int i;
  final int[] tmpSize = new int[1];
  /*GLenum*/final int[] tmpType = new int[1];
  /*GLsizei*/final int[] length = new int[1];
  /*COIN_GLchar*/byte[] myName = new byte[256];
  
  this.cacheName = name;
  this.isActive = false; // set uniform to inactive while searching
  
  // this will only happen once after the variable has been added so
  // it's not a performance issue that we have to search for it here.
  for (i = 0; i < activeUniforms[0]; i++) {
    g.glGetActiveUniformARB(pHandle, i, /*128,*/ length, tmpSize, 
                             tmpType, myName);
    int zeroIndex = 0;
    for(int index=0;index<256;index++) {
    	if(myName[index]==0) {
    		zeroIndex = index;
    		break;
    	}
    }
    if (Objects.equals(this.cacheName , new String(myName,0,zeroIndex))) {
      this.cacheSize = tmpSize[0];
      this.cacheType = tmpType[0];
      this.isActive = true;
      break;
    }
  }
  if (!this.isActive) {
    // not critical, but warn user so they can remove the unused parameter
//#if COIN_DEBUG
    SoDebugError.postWarning("SoGLSLShaderParameter::isValid",
                              "parameter '"+this.cacheName+"' not active.");
//#endif // COIN_DEBUG
    // return here since cacheSize and cacheType will not be properly initialized
    return true;
  }

  if (type == GL2.GL_INT) {
    switch (this.cacheType) {
    case GL2.GL_INT:
    case GL2.GL_SAMPLER_1D_ARB:
    case GL2.GL_SAMPLER_2D_ARB:
    case GL2.GL_SAMPLER_3D_ARB:
    case GL2.GL_SAMPLER_CUBE_ARB:
    case GL2.GL_SAMPLER_1D_SHADOW_ARB:
    case GL2.GL_SAMPLER_2D_SHADOW_ARB:
    case GL2.GL_SAMPLER_2D_RECT_ARB:
    case GL2.GL_SAMPLER_2D_RECT_SHADOW_ARB: 
      break;
    default: 
      return false;
    }
  }
  else if (this.cacheType != type)
    return false;

  if (num != null) { // assume: ARRAY
    if (this.cacheSize < num[0]) {
      // FIXME: better error handling - 20050128 martin
      SoDebugError.postWarning("SoGLSLShaderParameter::isValid",
                                "parameter "+this.cacheName+"["+this.cacheSize+"] < input["+num[0]+"]!");
      num[0] = this.cacheSize;
    }
    return (num[0] > 0);
  }
  return true;
}
}
