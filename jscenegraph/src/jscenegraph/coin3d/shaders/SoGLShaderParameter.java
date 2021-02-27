
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

/**
 * @author Yves Boyadjian
 *
 */
public interface SoGLShaderParameter {

   void set1f( SoGLShaderObject  shader,  float value,  String name,  int id);
   void set2f( SoGLShaderObject  shader,  float[] value,  String name,  int id);
   void set3f( SoGLShaderObject  shader,  float[] value,  String name,  int id);
   void set4f( SoGLShaderObject  shader,  float[] value,  String name,  int id);

   void set1fv( SoGLShaderObject  shader,  int num,  float[] value,  String name,  int id);
   void set2fv( SoGLShaderObject  shader,  int num,  float[] value,  String name,  int id);
   void set3fv( SoGLShaderObject  shader,  int num,  float[] value,  String name,  int id);
   void set4fv( SoGLShaderObject  shader,  int num,  float[] value,  String name,  int id);

   void setMatrix( SoGLShaderObject  shader,  float[] value,  String name,  int id);
   void setMatrixArray( SoGLShaderObject  shader,  int num,  float[] value,  String name,  int id);

   void set1i( SoGLShaderObject  shader,  int value,  String name,  int id);
   void set2i( SoGLShaderObject  shader,  int[] value,  String name,  int id);
   void set3i( SoGLShaderObject  shader,  int[] value,  String name,  int id);
   void set4i( SoGLShaderObject  shader,  int[] value,  String name,  int id);

   void set1iv( SoGLShaderObject  shader,  int num,  int[] value,  String name,  int id);
   void set2iv( SoGLShaderObject  shader,  int num,  int[] value,  String name,  int id);
   void set3iv( SoGLShaderObject  shader,  int num,  int[] value,  String name,  int id);
   void set4iv( SoGLShaderObject  shader,  int num,  int[] value,  String name,  int id);

   SoShader.Type shaderType() ;
}
