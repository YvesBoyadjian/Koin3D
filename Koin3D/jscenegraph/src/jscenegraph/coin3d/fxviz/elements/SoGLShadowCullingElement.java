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
  \class SoGLShadowCullingElement SoGLShadowCullingElement.h Inventor/annex/FXViz/elements/SoGLShadowCullingElement.h
  \brief The SoGLShadowCullingElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.

  \since Coin 2.5
*/

package jscenegraph.coin3d.fxviz.elements;

import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLShadowCullingElement extends SoInt32Element {

	  public enum Mode {
		    AS_IS_CULLING(0),
		    FRONT_FACE_CULLING(1),
		    NO_CULLING(2);
		  
		  private int value;
		  
		  Mode(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }
		  };

		//! FIXME: write doc.

		  public static void
		  set(SoState state,
		                                /*SoNode node,*/
		                                int mode)
		  {
		    SoInt32Element.set(classStackIndexMap.get(SoGLShadowCullingElement.class), state,/* node,*/ mode);
		  }

		  
		  public void
		  init(SoState state)
		  {
		    super.init(state);
		    this.data = getDefault();
		  }

		//! FIXME: write doc.

		  public static int get(SoState state)
		  {
		    return SoInt32Element.get(classStackIndexMap.get(SoGLShadowCullingElement.class), state);
		  }

		  //! FIXME: write doc.

		  public int
		  getDefault()
		  {
		    return Mode.AS_IS_CULLING.getValue();
		  }

		  public void 
		  push(SoState state)
		  {
		    SoGLShadowCullingElement prev = (SoGLShadowCullingElement) this.getNextInStack();

		    this.data = prev.data;
		  }

		  public void 
		  pop(SoState state, final SoElement prevTopElement)
		  {
		    SoGLShadowCullingElement prev = (SoGLShadowCullingElement) prevTopElement;
		    if (prev.data != this.data) {
		      this.updateGL(prev.data, this.data);
		    }
		  }

		  public void 
		  setElt(int value)
		  {
		    if (this.data != value) {
		      this.updateGL(this.data, value);
		    }
		    super.setElt(value);
		  }

		  public void 
		  updateGL(int oldvalue, int value)
		  {
		    // nothing to do yet. We might support more culling modes in the future though
		  }

		  
}
