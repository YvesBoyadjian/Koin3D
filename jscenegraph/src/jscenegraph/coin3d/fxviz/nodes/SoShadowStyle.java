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
  \class SoShadowStyle SoShadowStyle.h Inventor/annex/FXViz/nodes/SoShadowStyle.h
  \brief The SoShadowStyle class is a node for setting the shadow style on nodes.

  \ingroup nodes

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    ShadowStyle {
        style CASTS_SHADOW_AND_SHADOWED
    }
  \endcode

  \since Coin 2.5
*/

package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.coin3d.fxviz.elements.SoShadowStyleElement;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoShadowStyle extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShadowStyle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShadowStyle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShadowStyle.class); }    	  	
	
	  public enum Style {
		    NO_SHADOWING(SoShadowStyleElement.StyleFlags.NO_SHADOWING.getValue()), 
		    CASTS_SHADOW(SoShadowStyleElement.StyleFlags.CASTS_SHADOW.getValue()), 
		    SHADOWED(SoShadowStyleElement.StyleFlags.SHADOWED.getValue()), 
		    CASTS_SHADOW_AND_SHADOWED(SoShadowStyleElement.StyleFlags.CASTS_SHADOW_AND_SHADOWED.getValue());
		    
		    private int value;
		    
		    Style(int value) {
		    	this.value = value;
		    }
		    
		    public int getValue() {
		    	return value;
		    }
		  };
		  
	public final SoSFEnum style = new SoSFEnum();

	/*!
	  Constructor.
	*/
	public SoShadowStyle()
	{
	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShadowStyle.class);

	  nodeHeader.SO_NODE_ADD_FIELD(style,"style", (SoShadowStyle.Style.CASTS_SHADOW_AND_SHADOWED.getValue()));

	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.NO_SHADOWING);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.CASTS_SHADOW);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.SHADOWED);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.CASTS_SHADOW_AND_SHADOWED);
	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style,"style", "Style");
	}

	// Doc from superclass.
	public void
	GLRender(SoGLRenderAction action)
	{
	  SoState state = action.getState();

	  SoShadowStyleElement.set(state,
	                            /*this,*/
	                            (int) this.style.getValue());

	  if ((SoShapeStyleElement.get(state).getFlags() & SoShapeStyleElement.Flags.SHADOWS.getValue())!=0) {

	    if ((this.style.getValue() & Style.SHADOWED.getValue())!=0) {
	      SoGLShaderProgramElement.enable(state, true);
	    }
	    else {
	      SoGLShaderProgramElement.enable(state, false);
	    }
	  }
	}


	
	  
	// Doc from superclass.
	  public static void
	  initClass()
	  {
	       SoSubNode.SO__NODE_INIT_CLASS(SoShadowStyle.class, "ShadowStyle", SoNode.class);
	    //SO_NODE_INTERNAL_INIT_CLASS(SoShadowStyle, SO_FROM_COIN_2_5);
	    SO_ENABLE(SoGLRenderAction.class, SoShadowStyleElement.class);
	  }

}
