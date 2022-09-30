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

/*!
  \class SoGLMultiTextureMatrixElement Inventor/elements/SoGLMultiTextureMatrixElement.h
  \brief The SoGLMultiTextureMatrixElement class is used to update the OpenGL texture matrix.
  \ingroup elements

  Since (for some weird reason) most OpenGL implementations have a very
  small texture matrix stack, and since the matrix stack also is broken
  on many OpenGL implementations, the texture matrix is always loaded
  into OpenGL. We do not push() and pop() matrices.
*/

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLMultiTextureMatrixElement extends SoMultiTextureMatrixElement {

	private int cachecontext;
	

	// doc from parent
	public void
	init(SoState state)
	{
	  super.init(state);

	  SoAction action = state.getAction();
	  assert(action.isOfType(SoGLRenderAction.getClassTypeId()));
	  // fetch cache context from action since SoGLCacheContextElement
	  // might not be initialized yet.
	  SoGLRenderAction glaction = (SoGLRenderAction) action;
	  this.cachecontext = glaction.getCacheContext();
	}

	public void
	push(SoState state)
	{
	  super.push(state);
	  SoGLMultiTextureMatrixElement prev = (SoGLMultiTextureMatrixElement)
	    this.getNextInStack();

	  this.cachecontext = prev.cachecontext;

	  // capture previous element since we might or might not change the
	  // GL state in set/pop
	  prev.capture(state);
	}

	// doc from parent
	public void
	pop(SoState state,
	                                   SoElement prevTopElement)
	{
	  super.pop(state, prevTopElement);

	  SoGLMultiTextureMatrixElement prev = 
	    (SoGLMultiTextureMatrixElement) (prevTopElement);
	  
	  SbMatrix identity = SbMatrix.identity();
	  int numunits = Math.max(this.getNumUnits(),
	                             prev.getNumUnits());
	  for (int i = 0; i < numunits; i++) {
	    SbMatrix thism = 
	      (i < this.getNumUnits()) ?
	      this.getUnitData(i).textureMatrix : identity;
	    
	    SbMatrix prevm = 
	      (i < prev.getNumUnits()) ? 
	      prev.getUnitData(i).textureMatrix : identity;
	    
	    if (thism.operator_not_equal(prevm)) {
	      this.updategl(i);
	    }
	  }
	}

	// doc from parent
	public void
	multElt( int unit, SbMatrix matrix)
	{
	  super.multElt(unit, matrix);
	  this.updategl(unit);
	}

	public void
	setElt( int unit, SbMatrix matrix)
	{
	  super.setElt(unit, matrix);
	  this.updategl(unit);
	}


	// updates GL state
	public void
	updategl( int unit) 
	{
	  cc_glglue glue = SoGL.cc_glglue_instance(this.cachecontext);
	  if (unit != 0) {
	    SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + unit));
	  }
	  GL2 gl2 = glue.getGL2();
	  gl2.glMatrixMode(GL2.GL_TEXTURE);
	  if (unit < this.getNumUnits()) {
	    gl2.glLoadMatrixf(this.getUnitData(unit).textureMatrix/*[0]*/.toGL(),0);
	  }
	  else {
	    gl2.glLoadIdentity();
	  }
	  gl2.glMatrixMode(GL2.GL_MODELVIEW);
	  if (unit != 0) {
	    SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ GL2.GL_TEXTURE0);
	  }
	}

	
}
