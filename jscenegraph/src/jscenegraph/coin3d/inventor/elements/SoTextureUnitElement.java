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
  \class SoTextureUnitElement Inventor/elements/SoTextureUnitElement.h
  \brief The SoTextureUnitElement class is yet to be documented.

  This element is used for keeping control of multitexturing usage.

  FIXME: write doc.

  \ingroup elements
  \COIN_CLASS_EXTENSION
  \since Coin 2.2
*/

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureUnitElement extends SoInt32Element {

	//! FIXME: write doc.

	public static void
	set(SoState state,
	                          /*SoNode node,*/
	                          int unit)
	{
	  SoInt32Element.set(classStackIndexMap.get(SoTextureUnitElement.class), state, /*node,*/ unit);
	}

	//! FIXME: write doc.

	public void
	init(SoState state)
	{
	  super.init(state);
	  this.data = 0;
	}

	//! FIXME: write doc.

	public static int
	get(SoState state)
	{
	  return (int)(SoInt32Element.get(classStackIndexMap.get(SoTextureUnitElement.class), state));
	}
}
