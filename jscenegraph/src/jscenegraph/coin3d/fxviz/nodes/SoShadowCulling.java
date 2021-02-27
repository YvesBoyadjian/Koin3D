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
  \class SoShadowCulling SoShadowCulling.h Inventor/annex/FXViz/nodes/SoShadowCulling.h
  \brief The SoShadowCulling class is a node for setting the shadow style on nodes.

  \ingroup nodes

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    ShadowCulling {
        mode AS_IS_CULLING
    }
  \endcode

  \since Coin 2.5
*/

// *************************************************************************

/*!
  \enum SoShadowCulling::Mode

  Enumerates the available culling modes.
*/

/*!
  \var SoSFEnum SoShadowCulling::mode
  
  Sets the culling mode. Default is AS_IS_CULLING.
*/

/*!
  \var SoShadowCulling::Mode SoShadowCulling::AS_IS_CULLING
  
  Use the culling specified in the scene graph.
*/

/*!
  \var SoShadowCulling::Style SoShadowCulling::NO_CULLING

  Render both backfacing and frontfacting triangles into the shadow map.
*/


// *************************************************************************

package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.coin3d.fxviz.elements.SoGLShadowCullingElement;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShadowCulling extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShadowCulling.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShadowCulling.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShadowCulling.class); }    

	  public enum Mode {
		    AS_IS_CULLING(SoGLShadowCullingElement.Mode.AS_IS_CULLING.getValue()),
		    NO_CULLING (SoGLShadowCullingElement.Mode.NO_CULLING.getValue());
		  private int value;
		  Mode(int value) {
			  this.value = value;
		  }
		  public int getValue() {
			  return value;
		  }
		  };

		  public final SoSFEnum mode = new SoSFEnum();

		  /*!
		  Constructor.
		*/
		public SoShadowCulling()
		{
		  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShadowCulling.class);

		  nodeHeader.SO_NODE_ADD_FIELD(mode,"mode", (Mode.AS_IS_CULLING.getValue()));

		  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Mode.AS_IS_CULLING);
		  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Mode.NO_CULLING);
		  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(mode,"mode", "Mode");
		}

		// Doc from superclass.
		public static void
		initClass()
		{
		  //SO_NODE_INTERNAL_INIT_CLASS(SoShadowCulling, SO_FROM_COIN_2_5);
	        SoSubNode.SO__NODE_INIT_CLASS(SoShadowCulling.class, "ShadowCulling", SoNode.class);
		  SO_ENABLE(SoGLRenderAction.class, SoGLShadowCullingElement.class);
		}

		// Doc from superclass.
		public void GLRender(SoGLRenderAction action)
		{
		  SoState state = action.getState();

		  if ((SoShapeStyleElement.get(state).getFlags() & SoShapeStyleElement.Flags.SHADOWMAP.getValue())!=0) {
		    int mode = this.mode.getValue();
		    SoGLShadowCullingElement.set(state, /*this,*/ mode);
		    
		    if (mode == Mode.NO_CULLING.getValue()) {
		      SoShapeHintsElement.set(state, /*null,*/ 
		                               SoShapeHintsElement.VertexOrdering.UNKNOWN_ORDERING,
		                               SoShapeHintsElement.ShapeType.UNKNOWN_SHAPE_TYPE,
		                               SoShapeHintsElement.FaceType.UNKNOWN_FACE_TYPE);
		      SoOverrideElement.setShapeHintsOverride(state, null, true);
		    }
		    else {
		      // FIXME: need to restore the previous ShapeHints settings in some way,
		      // or require that this node is used only inside a separator
		      SoOverrideElement.setShapeHintsOverride(state, null, false);
		    }
		  }
		}
}
