
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

package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.inventor.nodes.SoShaderObject;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoNodeSensor;
import jscenegraph.database.inventor.sensors.SoSensor;

/*!

  \class SoShaderProgram SoShaderProgram.h Inventor/nodes/SoShaderProgram.h

  \brief The SoShaderProgram class is used to specify a set of
  vertex/geometry/fragment objects.

  This node can store one of each of SoVertexShader, SoGeometryShader
  and SoFragmentShader in its shaderObject field. Coin will load all
  shader objects specified there, and attach all objects into a
  program before binding it as the current shader program.

  \ingroup shaders

  A typical scene graph with shaders will look something like this:

  \code

  Separator {
    ShaderProgram {
      shaderObject [
        VertexShader {
          sourceProgram "myvertexshader.glsl"
          parameter [
            ShaderParameter1f { name "myvertexparam" value 1.0 }
          ]
        }
        FragmentShader {
          sourceProgram "myfragmentshader.glsl"
          parameter [
            ShaderParameter1f { name "myfragmentparam" value 2.0 }
          ]
        }
      ]
    }
    Cube { }
  }

  \endcode

  This will render the Cube with the vertex and fragment shaders
  specified in myvertexshader.glsl and myfragmentshader.glsl. Coin
  also supports ARB shaders and Cg shaders (if the Cg library is
  installed). However, we recommend using GLSL since we will focus
  mostly on support this shader language.

  Coin defines some named parameters that can be added by the
  application programmer, and which will be automatically updated by
  Coin while traversing the scene graph.

  \li coin_texunit[n]_model - Set to 0 when texturing is disabled, and
  to SoTextureImageElement.Model if there's a current texture on the
  state for unit \a n.

  \li coin_light_model - Set to 1 for PHONG, 0 for BASE_COLOR lighting.

  Example scene graph that renders per-fragment OpenGL Phong lighting
  for one light source. The shaders assume the first light source is a
  directional light. This is the case if you open the file in a
  standard examiner viewer.

  The iv-file:
  \code
  Separator {
    ShaderProgram {
      shaderObject [
        VertexShader {
          sourceProgram "perpixel_vertex.glsl"
        }
        FragmentShader {
          sourceProgram "perpixel_fragment.glsl"
        }
      ]
    }
    Complexity { value 1.0 }
    Material { diffuseColor 1 0 0 specularColor 1 1 1 shininess 0.9 }
    Sphere { }

    Translation { translation 3 0 0 }
    Material { diffuseColor 0 1 0 specularColor 1 1 1 shininess 0.9 }
    Cone { }

    Translation { translation 3 0 0 }
    Material { diffuseColor 0.8 0.4 0.1 specularColor 1 1 1 shininess 0.9 }
    Cylinder { }
  }
  \endcode

  The vertex shader (perpixel_vertex.glsl):
  \code
  varying vec3 ecPosition3;
  varying vec3 fragmentNormal;

  void main(void)
  {
    vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;
    ecPosition3 = ecPosition.xyz / ecPosition.w;
    fragmentNormal = normalize(gl_NormalMatrix * gl_Normal);

    gl_Position = ftransform();
    gl_FrontColor = gl_Color;
  }
  \endcode

  The fragment shader (perpixel_vertex.glsl):
  \code
  varying vec3 ecPosition3;
  varying vec3 fragmentNormal;

  void DirectionalLight(in int i,
                        in vec3 normal,
                        inout vec4 ambient,
                        inout vec4 diffuse,
                        inout vec4 specular)
  {
    float nDotVP; // normal . light direction
    float nDotHV; // normal . light half vector
    float pf;     // power factor

    nDotVP = max(0.0, dot(normal, normalize(vec3(gl_LightSource[i].position))));
    nDotHV = max(0.0, dot(normal, vec3(gl_LightSource[i].halfVector)));

    if (nDotVP == 0.0)
      pf = 0.0;
    else
      pf = pow(nDotHV, gl_FrontMaterial.shininess);

    ambient += gl_LightSource[i].ambient;
    diffuse += gl_LightSource[i].diffuse * nDotVP;
    specular += gl_LightSource[i].specular * pf;
  }

  void main(void)
  {
    vec3 eye = -normalize(ecPosition3);
    vec4 ambient = vec4(0.0);
    vec4 diffuse = vec4(0.0);
    vec4 specular = vec4(0.0);
    vec3 color;

    DirectionalLight(0, normalize(fragmentNormal), ambient, diffuse, specular);

    color =
      gl_FrontLightModelProduct.sceneColor.rgb +
      ambient.rgb * gl_FrontMaterial.ambient.rgb +
      diffuse.rgb * gl_Color.rgb +
      specular.rgb * gl_FrontMaterial.specular.rgb;

    gl_FragColor = vec4(color, gl_Color.a);
  }
  \endcode

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    ShaderProgram {
      shaderObject []
    }
  \endcode

  \sa SoShaderObject
  \sa SoShaderProgram
  \since Coin 2.5
*/

/*!
  \var SoMFNode SoShaderProgram.shaderObject

  The shader objects.

*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderProgram extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderProgram.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderProgram.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderProgram.class); }    	  	
	
//	  public interface SoShaderProgramEnableCB {
//		  void apply(Object closure, 
//              SoState state,
//              boolean enable);
//	  }

	  public final SoMFNode shaderObject = new SoMFNode();
	  
	  private SoShaderProgramEnableCB enablecb;
	  private Object enablecbclosure;

//private SoShaderProgram * owner;
	  private final SoGLShaderProgram glShaderProgram = new SoGLShaderProgram();

	  private static void sensorCB(Object data, SoSensor sensor)
	  {
		  // nothing to do now
	  }
	  
	  private SoNodeSensor sensor;

// doc from parent
public static void initClass()
{
    SO__NODE_INIT_CLASS(SoShaderProgram.class, "ShaderProgram", SoNode.class);
//  SO_NODE_INTERNAL_INIT_CLASS(SoShaderProgram,
//                              SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);

  SO_ENABLE(SoGLRenderAction.class, SoGLShaderProgramElement.class);
}

/*!
  Constructor.
*/
public SoShaderProgram()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(/*SoShaderProgram.class*/);

  nodeHeader.SO_NODE_ADD_MFIELD(shaderObject,"shaderObject", null);
  shaderObject.setNum(0);
  shaderObject.setDefault(true);

   sensor = new SoNodeSensor(SoShaderProgram::sensorCB, this);
   sensor.attach(this);

  enablecb = null;
  enablecbclosure = null;
}

/*!
  Destructor.
*/
public void destructor()
{
	if(sensor != null) {
		sensor.destructor();
	}
	
	glShaderProgram.destructor();
	
	shaderObject.destructor(); //FIXME
	
  super.destructor();
}

// doc from parent
public void GLRender(SoGLRenderAction action)
{
//	  SoState state = action.getState();
//
//	  // FIXME: (Martin 2004-09-21) find an alternative to invalidating the cache
//	  SoCacheElement.invalidate(state);
//
//	  glShaderProgram.removeShaderObjects();
//	  glShaderProgram.setEnableCallback(enablecb,
//	                                          enablecbclosure);
//
//	  SoGLShaderProgramElement.set(state, this, glShaderProgram);
//
//	  int i, cnt = shaderObject.getNum();
//
//	  // load shader objects
//	  for (i = 0; i <cnt; i++) {
//	    SoNode node = shaderObject.operator_square_bracket(i)[0];
//	    if (node.isOfType(SoShaderObject.getClassTypeId())) {
//	      ((SoShaderObject )node).GLRender(action);
//	    }
//	  }
//
//	  // enable shader after all shader objects have been loaded
//	  SoGLShaderProgramElement.enable(state, true);
//
//	  // update parameters after all shader objects have been added and enabled
//
//	  for (i = 0; i <cnt; i++) {
//	    SoNode node = shaderObject.operator_square_bracket(i)[0];
//	    if (node.isOfType(SoShaderObject.getClassTypeId())) {
//	      ((SoShaderObject )node).updateParameters(state);
//	    }
//	  }
	  SoState state = action.getState();

	  int i, cnt = this.shaderObject.getNum();
	  if (cnt == 0) {
	    SoGLShaderProgramElement.set(state, this, null);
	    return;
	  }
	  // FIXME: (Martin 2004-09-21) find an alternative to invalidating the cache
	  SoCacheElement.invalidate(state);

	  this.glShaderProgram.removeShaderObjects();
	  this.glShaderProgram.setEnableCallback(this.enablecb,
	                                          this.enablecbclosure);

	  SoGLShaderProgramElement.set(state, this, this.glShaderProgram);

	  // load shader objects
	  for (i = 0; i <cnt; i++) {
	    SoNode node = this.shaderObject.operator_square_bracket(i).get();
	    if (node.isOfType(SoShaderObject.getClassTypeId())) {
	      ((SoShaderObject )node).GLRender(action);
	    }
	  }

	  // enable shader after all shader objects have been loaded
	  SoGLShaderProgramElement.enable(state, true);

	  // update parameters after all shader objects have been added and enabled

	  for (i = 0; i <cnt; i++) {
	    SoNode node = this.shaderObject.operator_square_bracket(i).get();
	    if (node.isOfType(SoShaderObject.getClassTypeId())) {
	      ((SoShaderObject )node).updateParameters(state);
	    }
	  }
}

// doc from parent
public void search(SoSearchAction action)
{
  // Include this node in the search.
  super.search(action);
  if (action.isFound()) return;

  // we really can't do this since this node hasn't got an SoChildList
  // instance
//#if 0 // disabled, not possible to search under this node
//  int numindices;
//  const int * indices;
//  if (action.getPathCode(numindices, indices) == SoAction.IN_PATH) {
//    // FIXME: not implemented -- 20050129 martin
//  }
//  else { // traverse all shader objects
//    int num = this.shaderObject.getNum();
//    for (int i=0; i<num; i++) {
//      SoNode * node = this.shaderObject[i];
//      action.pushCurPath(i, node);
//      SoNodeProfiling profiling;
//      profiling.preTraversal(action);
//      node.search(action);
//      profiling.postTraversal(action);
//      action.popCurPath();
//      if (action.isFound()) return;
//    }
//  }
//#endif // disabled
}

/*!
  Adds a callback which is called every time this program is enabled/disabled.
*/
public void
setEnableCallback(SoShaderProgramEnableCB cb,
                                   Object closure)
{
  enablecb = cb;
  enablecbclosure = closure;
}

// *************************************************************************


}
