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

package jscenegraph.coin3d.inventor.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jscenegraph.database.inventor.elements.SoAccumulatedElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexAttributeElement extends SoAccumulatedElement {

	public interface AttributeApplyFunc {
		
	void apply(String key, SoVertexAttributeData obj, Object closure);
		
	};
	  
	private static class SoVertexAttributeElementP {
		public final Map<String, SoVertexAttributeData> attribdict = new HashMap<>();
		};
		
		private SoVertexAttributeElementP pimpl;
		
	/**
	 * 
	 */
	public SoVertexAttributeElement() {
		super();
		pimpl = new SoVertexAttributeElementP();
	}

	public void
	init(SoState state)
	{
	  super.init(state);
	  this.clearNodeIds();
	}

	/*!
	  Overridden to copy vertex attributes and node ids.
	*/
	public void
	push(SoState state)
	{
	  super.push(state);

	  final SoVertexAttributeElement prev =
	    (SoVertexAttributeElement)(this.getNextInStack());

	  pimpl.attribdict.clear();
	  pimpl.attribdict.putAll(prev.pimpl.attribdict); // java port
	  this.copyNodeIds(prev);
	}

	public void
	add(SoState state,
	                              SoVertexAttributeData attribdata)
	{
	  SoVertexAttributeElement thisp =
	    (SoVertexAttributeElement)(SoElement.getElement(state, classStackIndexMap.get(SoVertexAttributeElement.class)));

	  thisp.addElt(attribdata);
	  thisp.addNodeId(attribdata.nodeid);
	}

	public void
	addElt(SoVertexAttributeData attribdata)
	{
	  pimpl.attribdict.put(attribdata.name.getString(), attribdata);
	}

	public static SoVertexAttributeElement 
	getInstance(SoState state)
	{
	  return ( SoVertexAttributeElement)
	    (getConstElement(state, classStackIndexMap.get(SoVertexAttributeElement.class)));
	}


	public int
	getNumAttributes()
	{
	  return pimpl.attribdict.size();//getNumElements(); // java port 
	}

	public void
	applyToAttributes(AttributeApplyFunc func, Object closure)
	{
	  for(Entry<String,SoVertexAttributeData> entry:pimpl.attribdict.entrySet()){
	    func.apply(entry.getKey(),entry.getValue(),closure);
	  }
	}

}
