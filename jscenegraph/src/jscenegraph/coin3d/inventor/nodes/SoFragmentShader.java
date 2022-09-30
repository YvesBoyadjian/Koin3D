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

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoSubNode;

/*!
  \class SoFragmentShader SoFragmentShader.h Inventor/nodes/SoFragmentShader.h
  \brief The SoFragmentShader class is used for setting up fragment shader programs.
  \ingroup nodes

  See \link coin_shaders Shaders in Coin \endlink for more information
  on how to set up a scene graph with shaders.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    FragmentShader {
      isActive TRUE
      sourceType FILENAME
      sourceProgram ""
      parameter []
    }
  \endcode

  \sa SoShaderObject
  \sa SoShaderProgram
  \since Coin 2.5
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoFragmentShader extends SoShaderObject {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFragmentShader.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoFragmentShader.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
    	if(nodeHeader == null) {
    		return super.getTypeId();
    	}
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoFragmentShader.class); }              
	

/*!
  Constructor.
*/
public SoFragmentShader()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoFragmentShader.class*/);
  isBuiltIn = true;  
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
  assert(ptr != null);// && "No active OpenGL context found!");
  if (ptr == null) return false; // Always bail out. Even when compiled in 'release' mode.

  cc_glglue glue = SoGL.instance_from_context_ptr(ptr);

  if (sourceType == SourceType.ARB_PROGRAM) {
    return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_FRAGMENT_PROGRAM);
  }
  else if (sourceType == SourceType.GLSL_PROGRAM) {
    return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_SHADER_OBJECT);
  }
  // FIXME: Add support for detecting missing Cg support (20050427
  // handegar)
  else if (sourceType == SourceType.CG_PROGRAM) return true;

  return false;
}

  
  public static void initClass()
  //
  ////////////////////////////////////////////////////////////////////////
  {
      SO__NODE_INIT_CLASS(SoFragmentShader.class, "FragmentShader", SoShaderObject.class);

  }  

}
