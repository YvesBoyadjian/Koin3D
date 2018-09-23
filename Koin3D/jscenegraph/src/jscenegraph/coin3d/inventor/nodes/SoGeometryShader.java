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

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;

/*!
  \class SoGeometryShader SoGeometryShader.h Inventor/nodes/SoGeometryShader.h
  \brief The SoGeometryShader class is used for loading geometry shader programs.
  \ingroup nodes

  See \link coin_shaders Shaders in Coin \endlink for more information
  on how to set up a scene graph with shaders.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    GeometryShader {
      isActive TRUE
      sourceType FILENAME
      sourceProgram ""
      parameter []
      inputType TRIANGLES_IN
      outputType TRIANGLE_STRIP_OUT
      maxEmit 8
    }
  \endcode

  \sa SoShaderObject
  \sa SoShaderProgram
  \since Coin 2.5
*/

/*!
  \enum SoGeometryShader::InputType

  Enumerates the input types.
*/

/*!
  \enum SoGeometryShader::OutputType

  Enumerates the output types.
*/

/*!
  \var SoGeometryShader::InputType SoGeometryShader::TRIANGLES_IN.

  Shader uses triangles as input. OpenGL will automatically convert quads
  and polygons into triangles.
*/

/*!
  \var SoGeometryShader::InputType SoGeometryShader::LINES_IN.

  Shader uses lines as input.
*/

/*!
  \var SoGeometryShader::InputType SoGeometryShader::POINTS_IN.

  Shader uses points as input.
*/

/*!
  \var SoGeometryShader::OutputType SoGeometryShader::POINTS_OUT.

  Shader generates points.
*/

/*!
  \var SoGeometryShader::OutputType SoGeometryShader::LINE_STRIP_OUT.

  Shader generates line strips.
*/

/*!
  \var SoGeometryShader::OutputType SoGeometryShader::TRIANLE_STRIP_OUT.

  Shader generates triangle strips.
*/


/*!
  \var SoSFEnum SoGeometryShader::inputType

  The type of geometry used as input to the shader. Default value is TRIANGLES_IN.
*/

/*!
  \var SoSFEnum SoGeometryShader::outputType

  The type of geometry generated from the shader. Default value is TRIANGLE_FAN_OUT.
*/

/*!
  \var SoSFInt32 SoGeometryShader::maxEmit

  The maximum number of vertices emitted from the shader. Default
  value is 8. This corresponds to the GL_GEOMETRY_VERTICES_OUT_EXT
  enum.
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoGeometryShader extends SoShaderObject {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoGeometryShader.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoGeometryShader.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoGeometryShader.class); }              

  
  enum InputType {
    POINTS_IN,
    LINES_IN,
    TRIANGLES_IN;
	  
	  public static InputType fromValue(int value) {
		  switch(value) {
		  case 0: return POINTS_IN;
		  case 1: return LINES_IN;
		  case 2: return TRIANGLES_IN;
		  default: return null;
		  }
	  }
  };

  enum OutputType {
    POINTS_OUT,
    LINE_STRIP_OUT,
    TRIANGLE_STRIP_OUT;
	  
	  public static OutputType fromValue(int value) {
		  switch(value) {
		  case 0: return POINTS_OUT;
		  case 1: return LINE_STRIP_OUT;
		  case 2: return TRIANGLE_STRIP_OUT;
		  default: return null;
		  }
	  }
  };

  public final SoSFEnum inputType = new SoSFEnum();
  public final SoSFEnum outputType = new SoSFEnum();
  public final SoSFInt32 maxEmit = new SoSFInt32();

  
/*!
  Constructor.
*/
public SoGeometryShader()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoGeometryShader.class*/);
  isBuiltIn = true;

  nodeHeader.SO_NODE_ADD_FIELD(inputType,"inputType", (InputType.TRIANGLES_IN.ordinal()));
  nodeHeader.SO_NODE_ADD_FIELD(outputType,"outputType", (OutputType.TRIANGLE_STRIP_OUT.ordinal()));
  nodeHeader.SO_NODE_ADD_FIELD(maxEmit,"maxEmit", (8));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(InputType.POINTS_IN);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(InputType.LINES_IN);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(InputType.TRIANGLES_IN);

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(OutputType.POINTS_OUT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(OutputType.LINE_STRIP_OUT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(OutputType.TRIANGLE_STRIP_OUT);

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(inputType,"inputType", "InputType");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(outputType,"outputType", "OutputType");
}

private static int first = 1;

public void
GLRender(SoGLRenderAction action)
{
  if (this.isActive.getValue()) {
    SoState state = action.getState();
    final int cachecontext = SoGLCacheContextElement.get(state);
    cc_glglue glue = SoGL.cc_glglue_instance(cachecontext);
    
    if (!SoGLDriverDatabase.isSupported(glue, "GL_EXT_geometry_shader4")) {
      if (first != 0) {
        first = 0;
        SoDebugError.post("SoGeometryShader::GLRender",
                           "Geometry shaders not support by hardware/driver");
      }
      return; // don't add this object to the program
    }
    else {
      /*GLenum*/int input = 0;
      /*GLenum*/int output = 0;

      switch (InputType.fromValue(this.inputType.getValue())) {
      default:
        assert(false);// && "invalid input type");
      case POINTS_IN:
        input = GL2.GL_POINTS;
        break;
      case LINES_IN:
        input = GL2.GL_LINES;
        break;
      case TRIANGLES_IN:
        input = GL2.GL_TRIANGLES;
        break;
      }
      switch (OutputType.fromValue(this.outputType.getValue())) {
      default:
        assert(false);// && "invalid input type");
      case POINTS_OUT:
        output = GL2.GL_POINTS;
        break;
      case LINE_STRIP_OUT:
        output = GL2.GL_LINE_STRIP;
        break;
      case TRIANGLE_STRIP_OUT:
        output = GL2.GL_TRIANGLE_STRIP;
        break;
      }
      SoGLShaderProgram shaderProgram = SoGLShaderProgramElement.get(action.getState());
      assert(shaderProgram!=null);

      shaderProgram.addProgramParameter(GL2.GL_GEOMETRY_INPUT_TYPE_EXT, input);
      shaderProgram.addProgramParameter(GL2.GL_GEOMETRY_OUTPUT_TYPE_EXT, output);
      shaderProgram.addProgramParameter(GL2.GL_GEOMETRY_VERTICES_OUT_EXT, this.maxEmit.getValue());
    }
  }
  super.GLRender(action);
}



/*!
  Returns a boolean indicating whether the requested source type is
  supported by the OpenGL driver or not.

  <i>Beware:</i> To get a correct answer, a valid OpenGL context must
  be available.
*/
public static boolean
isSupported(GL2 gl2, SourceType sourceType)
{
  // The function signature is not very well designed, as we really
  // need a guaranteed GL context for this. (We've chosen to be
  // compatible with TGS Inventor, so don't change the signature.)

  Object ptr = gl2;//coin_gl_current_context(); java port
  assert(ptr!= null);// && "No active OpenGL context found!");
  if (ptr == null) return false; // Always bail out. Even when compiled in 'release' mode.

  cc_glglue glue = SoGL.instance_from_context_ptr(ptr);

  if (sourceType == SourceType.ARB_PROGRAM) {
    return false;
  }
  else if (sourceType == SourceType.GLSL_PROGRAM) {
    return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_SHADER_OBJECT);
  }
  // AFAIK Cg has no support for geometry shaders (yet).
  // pederb, 20070410
  else if (sourceType == SourceType.CG_PROGRAM) return false;

  return false;
}

  
  public static void initClass()
  //
  ////////////////////////////////////////////////////////////////////////
  {
      SO__NODE_INIT_CLASS(SoGeometryShader.class, "GeometryShader", SoShaderObject.class);

  }  

}
