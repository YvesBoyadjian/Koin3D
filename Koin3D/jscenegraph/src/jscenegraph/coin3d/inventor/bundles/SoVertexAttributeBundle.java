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
  \class SoVertexAttributeBundle include/Inventor/bundles/SoVertexAttributeBundle.h
  \brief The SoVertexAttributeBundle class simplifies vertex attribute handling.
  \ingroup bundles

*/

package jscenegraph.coin3d.inventor.bundles;

import jscenegraph.coin3d.inventor.elements.SoVertexAttributeElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLVertexAttributeElement;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.bundles.SoBundle;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexAttributeBundle extends SoBundle {

	private
		   SoVertexAttributeElement elem; //ptr
	private SoGLVertexAttributeElement glelem; // ptr
		  
	/*!
	  Constructor.
	*/
	public SoVertexAttributeBundle(SoAction action, boolean forrendering) {
	  super(action);
	
	  this.elem = SoVertexAttributeElement.getInstance(this.state);
	  this.glelem = null;
	  if (forrendering) {
	    this.glelem = (SoGLVertexAttributeElement) (this.elem);
	  }
	}

	/*!
	  Destructor.
	*/
	public void destructor()
	{
		super.destructor();
	}

	public boolean 
	doAttributes() 
	{
	  return (this.elem.getNumAttributes() > 0);
	}

	/*!
	  Send the index'th attribute to OpenGL.
	*/
	public void 
	send(int index)
	{
	  assert(this.glelem != null);
	  this.glelem.send(index);
	}
}
