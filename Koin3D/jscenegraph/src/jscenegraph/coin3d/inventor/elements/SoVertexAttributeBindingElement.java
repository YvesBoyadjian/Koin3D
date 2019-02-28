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
  \class SoVertexAttributeBindingElement Inventor/elements/SoVertexAttributeBindingElement.h
  \brief The SoVertexAttributeBindingElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.
*/

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexAttributeBindingElement extends SoInt32Element {

	public
		  enum Binding {
		    OVERALL(0),
		    // PER_FACE = 1,
		    // PER_FACE_INDEXED = 2,
		    PER_VERTEX(3),
		    PER_VERTEX_INDEXED(4);
		    //DEFAULT(4/*PER_VERTEX_INDEXED*/);
			  private int value;
			  Binding(int value) {
				  this.value = value;
			  }
			  public int getValue() {
				  return value;
			  }
			  public static Binding fromValue(int value) {
				  switch(value) {
				  case 0: return Binding.OVERALL;
				  case 3: return Binding.PER_VERTEX;
				  case 4: return Binding.PER_VERTEX_INDEXED;
				  }
				return null;
			  }
		  }; // enum Binding

	/**
	 * 
	 */
	public SoVertexAttributeBindingElement() {
		// TODO Auto-generated constructor stub
	}

	public void
	set(SoState state,
	                              SoNode node,
	                              Binding binding)
	{
	  assert(binding.getValue() >= Binding.OVERALL.getValue() &&
	         binding.getValue() <= Binding.PER_VERTEX_INDEXED.getValue()
	        );
	  SoInt32Element.set(classStackIndexMap.get(SoVertexAttributeBindingElement.class), state, /*node,*/ binding.getValue());
	}

	//! FIXME: write doc.

	public void
	init(SoState state)
	{
	  super.init(state);
	  this.data = getDefault().getValue();
	}

	//! FIXME: write doc.

	//$ EXPORT INLINE
	public void
	set(SoState state, Binding binding)
	{
	  set(state, null, binding);
	}

	//! FIXME: write doc.

	//$ EXPORT INLINE
	public static SoVertexAttributeBindingElement.Binding
	get(SoState state)
	{
	  return Binding.fromValue(SoInt32Element.get(classStackIndexMap.get(SoVertexAttributeBindingElement.class), state));
	}

	//! FIXME: write doc.

	//$ EXPORT INLINE
	public SoVertexAttributeBindingElement.Binding
	getDefault()
	{
	  return Binding./*DEFAULT*/PER_VERTEX_INDEXED;
	}
}
