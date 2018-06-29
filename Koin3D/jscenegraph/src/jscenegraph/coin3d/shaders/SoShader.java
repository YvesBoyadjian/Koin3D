
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

import java.util.HashMap;
import java.util.Map;

import jscenegraph.coin3d.inventor.nodes.SoFragmentShader;
import jscenegraph.coin3d.inventor.nodes.SoGeometryShader;
import jscenegraph.coin3d.inventor.nodes.SoShaderObject;
import jscenegraph.coin3d.inventor.nodes.SoVertexShader;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter1f;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter1i;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderProgram;
import jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter;
import jscenegraph.database.inventor.SoType;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShader {

	static String SO_SHADER_DIR = null;
	
	static Map <String, String>  shader_dict = null;
	static Map <String, String> shader_builtin_dict = null;
	
  public enum Type {
    ARB_SHADER,
    CG_SHADER,
    GLSL_SHADER
  };

public static void
init()
{
  // Trigger loading and init of Cg library glue.
  //
  // FIXME: this function should rather be used from the relevant
  // class(es), so it is loaded only on demand. 20050125 mortene.
  //cc_cgglue_available();

  // --- initialization of elements (must be done first) ---------------
  if (SoGLShaderProgramElement.getClassTypeId(SoGLShaderProgramElement.class) == SoType.badType())
    SoGLShaderProgramElement.initClass(SoGLShaderProgramElement.class);

  // --- initialization of shader nodes --------------------------------
  if (SoShaderProgram.getClassTypeId() == SoType.badType())
    SoShaderProgram.initClass();
  if (SoShaderObject.getClassTypeId() == SoType.badType())
    SoShaderObject.initClass();
  if (SoFragmentShader.getClassTypeId() == SoType.badType())
    SoFragmentShader.initClass();
  if (SoVertexShader.getClassTypeId() == SoType.badType())
    SoVertexShader.initClass();
  if (SoGeometryShader.getClassTypeId() == SoType.badType())
    SoGeometryShader.initClass();

  // --- initialization of parameter nodes -----------------------------
  if (SoShaderParameter.getClassTypeId() == SoType.badType())
    SoShaderParameter.initClass();
  if (SoUniformShaderParameter.getClassTypeId() == SoType.badType())
    SoUniformShaderParameter.initClass();

  // float vector parameter nodes
  if (SoShaderParameter1f.getClassTypeId() == SoType.badType())
    SoShaderParameter1f.initClass();
//  if (SoShaderParameter2f.getClassTypeId() == SoType.badType())
//    SoShaderParameter2f.initClass();
//  if (SoShaderParameter3f.getClassTypeId() == SoType.badType())
//    SoShaderParameter3f.initClass();
//  if (SoShaderParameter4f.getClassTypeId() == SoType.badType())
//    SoShaderParameter4f.initClass();

  // float vector array parameter nodes
//  if (SoShaderParameterArray1f.getClassTypeId() == SoType.badType()) TODO
//    SoShaderParameterArray1f.initClass();
//  if (SoShaderParameterArray2f.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray2f.initClass();
//  if (SoShaderParameterArray3f.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray3f.initClass();
//  if (SoShaderParameterArray4f.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray4f.initClass();

  // matrix parameter nodes
//  if (SoShaderStateMatrixParameter.getClassTypeId() == SoType.badType()) TODO
//    SoShaderStateMatrixParameter.initClass();
//  if (SoShaderParameterMatrix.getClassTypeId() == SoType.badType())
//    SoShaderParameterMatrix.initClass();
//  if (SoShaderParameterMatrixArray.getClassTypeId() == SoType.badType())
//    SoShaderParameterMatrixArray.initClass();

  // int32 support
  if (SoShaderParameter1i.getClassTypeId() == SoType.badType())
    SoShaderParameter1i.initClass();

  // FIXME: Do we need int32 support (like in TGS)? 20040924 martin
//#if 1
//  if (SoShaderParameter2i.getClassTypeId() == SoType.badType()) TODO
//    SoShaderParameter2i.initClass();
//  if (SoShaderParameter3i.getClassTypeId() == SoType.badType())
//    SoShaderParameter3i.initClass();
//  if (SoShaderParameter4i.getClassTypeId() == SoType.badType())
//    SoShaderParameter4i.initClass();
//  if (SoShaderParameterArray1i.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray1i.initClass();
//  if (SoShaderParameterArray2i.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray2i.initClass();
//  if (SoShaderParameterArray3i.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray3i.initClass();
//  if (SoShaderParameterArray4i.getClassTypeId() == SoType.badType())
//    SoShaderParameterArray4i.initClass();
//#endif

  SO_SHADER_DIR = System.getenv("SO_SHADER_DIR");
  shader_dict = new HashMap <>();
  shader_builtin_dict = new HashMap <>();
  setupBuiltinShaders();

  //coin_atexit((coin_atexit_f*) soshader_cleanup, CC_ATEXIT_NORMAL);
}

public static void
setupBuiltinShaders()
{ //TODO
//  shader_builtin_dict.put("lights/PointLight", POINTLIGHT_shadersource);
//  shader_builtin_dict.put("lights/SpotLight", SPOTLIGHT_shadersource);
//  shader_builtin_dict.put("lights/DirectionalLight", DIRECTIONALLIGHT_shadersource);
//  shader_builtin_dict.put("lights/DirSpotLight", DIRSPOTLIGHT_shadersource);
//  shader_builtin_dict.put("vsm/VsmLookup", VSMLOOKUP_shadersource);
}
}
