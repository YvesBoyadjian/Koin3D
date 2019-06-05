/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

package jscenegraph.coin3d.inventor.elements.gl;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoVertexAttributeData;
import jscenegraph.coin3d.inventor.elements.SoVertexAttributeElement;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFShort;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.fields.SoMFVec4f;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLVertexAttributeElement extends SoVertexAttributeElement {

	/**
	 * 
	 */
	public SoGLVertexAttributeElement() {
		super();
	}


public static SoGLVertexAttributeElement 
getInstance(SoState state)
{
  return ( SoGLVertexAttributeElement )
    (getConstElement(state, classStackIndexMap.get(SoGLVertexAttributeElement.class)));
}

//typedef String Key;
//typedef SoVertexAttributeData Type;

//
// send attributes to gl
//
private static boolean first = true;
private static void send_attribs( String key,
                         final SoVertexAttributeData attribdata,
                         Object closure)
{
  SoVertexAttributeData data = (SoVertexAttributeData) attribdata;
  assert(data!=null);

  // only send attributes that were found in the shader object
  if (data.index < 0) {
    return;
  }

  int dataindex = (( int ) closure);
  cc_glglue glue = SoGL.sogl_glue_instance(data.state);

//#if COIN_DEBUG
  if (dataindex >= data.data.getNum()) {
    if (first) {
      SoDebugError.post("SoGLVertexAttributeElement::send",
                         "attribute index out of bounds.");
      first = false;
    }
  }
//#endif

  if (data.type.equals(SoMFFloat.getClassTypeId(SoMFFloat.class))) {
    SoMFFloat mfield = (SoMFFloat)(data.data);
    float[] attribs = mfield.getValuesFloat(0);
    glue.glVertexAttrib1fARB(data.index, attribs[dataindex]);

  } else if (data.type.equals(SoMFVec2f.getClassTypeId(SoMFVec2f.class))) {

    SoMFVec2f mfield = (SoMFVec2f)(data.data);
    SbVec2f[] attribs = mfield.getValues(0);
    glue.glVertexAttrib2fvARB(data.index, attribs[dataindex].getValueRead());

  } else if (data.type.equals(SoMFVec3f.getClassTypeId(SoMFVec3f.class))) {

    SoMFVec3f mfield = (SoMFVec3f)(data.data);
    SbVec3f[] attribs = mfield.getValues(0);
    glue.glVertexAttrib3fvARB(data.index, attribs[dataindex].getValueRead());

  } else if (data.type.equals(SoMFVec4f.getClassTypeId(SoMFVec4f.class))) {

    SoMFVec4f mfield = (SoMFVec4f)(data.data);
    SbVec4f[] attribs = mfield.getValues(0);
    glue.glVertexAttrib4fvARB(data.index, attribs[dataindex].getValueRead());

  } else if (data.type.equals(SoMFShort.getClassTypeId(SoMFShort.class))) {

    SoMFShort mfield = (SoMFShort)(data.data);
    Short[] attribs = mfield.getValues(0);
    glue.glVertexAttrib1sARB(data.index, attribs[dataindex]);

  } else {
    throw new IllegalArgumentException("unknown attribute type");
  }
}

//
// get the index of each specified attribute.
//
private static void query_attribs(String key,
		SoVertexAttributeData attribdata,
                          Object closure)
{
  SoVertexAttributeData data = (SoVertexAttributeData ) attribdata;
  cc_glglue glue = SoGL.sogl_glue_instance(data.state);

  SoGLShaderProgram shaderprogram =
    (SoGLShaderProgram)(SoGLShaderProgramElement.get(data.state));

  if (shaderprogram != null && shaderprogram.glslShaderProgramLinked()) {
    int shaderobj = shaderprogram.getGLSLShaderProgramHandle(data.state);

    data.index = glue.glGetAttribLocationARB((int) shaderobj,
                                               (String) key);
//#if COIN_DEBUG
    if (data.index < 0) {
      SoDebugError.postWarning("SoGLVertexAttributeElement::addElt",
                                "vertex attribute '"+key+"' not used in vertex shader");
    }
//#endif // COIN_DEBUG
  }
}

//
// enable vertex array rendering, with or without vbo
//
private static void enable_vbo(String key,
		SoVertexAttributeData attribdata,
                       Object closure)
{
  SoVertexAttributeData data = (SoVertexAttributeData ) attribdata;

  // only enable vertex array rendering for attributes that were
  // actually used in the shader object
  if (data.index < 0) { return; }

  SoGLRenderAction action = (SoGLRenderAction)(closure);
  cc_glglue glue = SoGL.sogl_glue_instance(action.getState());

  SoCoordinateElement coords =
    SoCoordinateElement.getInstance(action.getState());
  assert(coords.getNum() == attribdata.data.getNum());

  Object dataptr = null;

  if (data.vbo != null) {
    data.vbo.bindBuffer(action.getCacheContext());
  } else {
    SoGL.cc_glglue_glBindBuffer(glue, GL2.GL_ARRAY_BUFFER, 0);
    dataptr = attribdata.dataptr;
  }

  glue.glVertexAttribPointerARB(data.index,
                                 attribdata.num,
                                 attribdata.gltype,
                                 GL2.GL_FALSE != 0, 0, dataptr);

  glue.glEnableVertexAttribArrayARB(data.index);
}

//
// disable vertex array rendering, with or without vbo
//
private static void disable_vbo(String key,
		SoVertexAttributeData attribdata,
                        Object closure)
{
  SoVertexAttributeData data = (SoVertexAttributeData ) attribdata;
  // only disable vertex array rendering for attributes that were
  // actually used in the shader object
  if (data.index < 0) { return; }

  SoGLRenderAction action = (SoGLRenderAction)(closure);
  cc_glglue glue = SoGL.sogl_glue_instance(action.getState());

  glue.glDisableVertexAttribArrayARB(data.index);
}

//! FIXME: write doc.
public void
send(int index)
{
  this.applyToAttributes(SoGLVertexAttributeElement::send_attribs, /*(void *) &*/index);
}

//! FIXME: write doc.
public void
addElt(SoVertexAttributeData attribdata)
{
  super.addElt(attribdata);
  this.applyToAttributes(SoGLVertexAttributeElement::query_attribs, null);
}

//! FIXME: write doc.
public void
enableVBO(SoGLRenderAction action)
{
  this.applyToAttributes(SoGLVertexAttributeElement::enable_vbo, (Object) action);
}

//! FIXME: write doc.
public void
disableVBO(SoGLRenderAction action) 
{
  this.applyToAttributes(SoGLVertexAttributeElement::disable_vbo, (Object) action);
}
}
