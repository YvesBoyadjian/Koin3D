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
  \class SoShadowStyleElement SoShadowStyleElement.h Inventor/annex/FXViz/elements/SoShadowStyleElement.h
  \brief The SoShadowStyleElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.

  \since Coin 2.5
*/

package jscenegraph.coin3d.fxviz.elements;

import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoShadowStyleElement extends SoInt32Element {

	  public enum StyleFlags {
		    NO_SHADOWING( 0x0), 
		    CASTS_SHADOW( 0x1), 
		    SHADOWED    ( 0x2),
		    CASTS_SHADOW_AND_SHADOWED( CASTS_SHADOW.getValue()|SHADOWED.getValue());
		  
		  private int value;
		  
		  StyleFlags(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }
		  };
		  
		  public static void set(SoState state,
		                           /* SoNode node,*/
		                            int style)
		  {
		    SoInt32Element.set(classStackIndexMap.get(SoShadowStyleElement.class), state, /*node,*/ style);
		  }

		  public void init(SoState state)
		  {
		    super.init(state);
		    this.data = getDefault();
		  }


public int get(SoState state)
{
  return super.get(classStackIndexMap.get(SoShadowStyleElement.class), state);
}


public int
getDefault()
{
  return StyleFlags.CASTS_SHADOW_AND_SHADOWED.getValue();
}
		  
}
