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
  \class SoTextureUnit SoTextureUnit.h Inventor/nodes/SoTextureUnit.h
  \brief The SoTextureUnit class is a node for setting the active texture unit.
  \ingroup nodes

  When an SoTextureUnit node is inserted into the scene graph, all
  subsequent texture nodes (SoTexture2, SoTextureCoordinate2,
  SoTextureCoordinate3, SoTexture2Transform, SoTexture3Transform,
  SoTextureCoordinateEnvironment, SoTextureCoordinatePlane and SoComplexity)
  will affect the texture unit set in the unit field.

  See the SoGuiExample module for an usage example for this node.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    TextureUnit {
        unit 0
        mappingMethod IMAGE_MAPPING
    }
  \endcode

  \since Coin 2.2
*/


/*!
  \var SoSFInt32 SoTextureUnit::unit

  The texture unit which will be used for texture nodes following this
  node in the traversal. Default value of the field is 0.
*/

/*!
  \var SoSFEnum SoTextureUnit::mappingMethod

  The mapping method for this unit. Default is IMAGE_MAPPING.

  This field is not currently supported in Coin. It's included to
  support TGS' API. We might support the field in the future.
*/


/*!
  \var SoTextureUnit::MappingMethod SoTextureUnit::IMAGE_MAPPING

  Normal image mapping is used.
*/

/*!
  \var SoTextureUnit::MappingMethod SoTextureUnit::BUMP_MAPPING

  Bump mapping is used.
*/

// *************************************************************************

package jscenegraph.coin3d.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureMatrixElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureUnit extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureUnit.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureUnit.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureUnit.class); }    	  	
	
	  
	  public enum MappingMethod {
		  BUMP_MAPPING(0),
		  IMAGE_MAPPING(1);
		  
		  private int value;
		  
		  MappingMethod(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }
	  }
	  
	  public final SoSFInt32 unit = new SoSFInt32();
	  public final SoSFEnum mappingMethod = new SoSFEnum();
	  

/*!
  Constructor.
*/
public SoTextureUnit()
{
	nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoTextureUnit.class);

	nodeHeader.SO_NODE_ADD_FIELD(unit,"unit", (0));
	nodeHeader.SO_NODE_ADD_FIELD(mappingMethod,"mappingMethod", (MappingMethod.IMAGE_MAPPING.getValue()));

	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MappingMethod.IMAGE_MAPPING);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MappingMethod.BUMP_MAPPING);

	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(mappingMethod,"mappingMethod", "MappingMethod");
}

/*!
  Destructor.
*/
public void destructor()
{
	super.destructor();
}

// Doc from superclass.
public void GLRender(SoGLRenderAction action)
{
  SoTextureUnit_doAction((SoAction)action);

  SoState state = action.getState();
  cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));
  int maxunits = SoGL.cc_glglue_max_texture_units(glue);

  if (this.unit.getValue() >= maxunits) {
    boolean first = true;
    if (first) {
      SoDebugError.postWarning("SoTextureUnit::GLRender",
                                "Texture unit "+this.unit.getValue()+" (counting from 0) requested. "+
                                "Your system only supports "+maxunits+" texture unit"+(maxunits == 1 ? "" : "s")+". "+
                                "(This warning message only shown once, but "+
                                "there could be more cases of this in the "+
                                "scene graph.)");
      first = false;
    }
  }
}

// Doc from superclass.
void
SoTextureUnit_doAction(SoAction action)
{
  SoTextureUnitElement.set(action.getState(), /*this,*/
                            this.unit.getValue());
}

// Doc from superclass.
public void
callback(SoCallbackAction action)
{
  SoTextureUnit_doAction(action);
}

// Doc from superclass.
protected void
pick(SoPickAction action)
{
  SoTextureUnit_doAction(action);
}

// Doc from superclass.
public void
getBoundingBox(SoGetBoundingBoxAction action)
{
  SoTextureUnit_doAction(action);
}

protected void
getMatrix(SoGetMatrixAction action)
{
  SoTextureUnit_doAction(action);
}

/*!

  Returns the maximum number of texture units for the current GL
  context.  Do not call this method if you don't have a current active
  GL context. You should also know that your OpenGL driver supports
  multi-texturing.

  This function is provided only to be compatible with TGS Inventor.
  It's better to use cc_glglue_max_texture_units() if you're using
  Coin (declared in Inventor/C/glue/gl.h).
*/
public static int getMaxTextureUnit(GL2 gl2)
{
  final int[] tmp = new int[1];
  gl2.glGetIntegerv(GL2.GL_MAX_TEXTURE_UNITS, tmp,0);

  return (int) tmp[0];
}
	  
	  
	// Doc from superclass.
	  public static void
	  initClass()
	  {
		  SoSubNode.SO__NODE_INIT_CLASS(SoTextureUnit.class,"TextureUnit",SoNode.class); /*SO_FROM_INVENTOR_2_6|SO_FROM_COIN_2_0);*/
	    //SO_NODE_INTERNAL_INIT_CLASS(SoTextureUnit, SO_FROM_COIN_2_2);

	    SO_ENABLE(SoGLRenderAction.class, SoTextureUnitElement.class);
	    SO_ENABLE(SoCallbackAction.class, SoTextureUnitElement.class);
	    SO_ENABLE(SoPickAction.class, SoTextureUnitElement.class);
	    SO_ENABLE(SoGetBoundingBoxAction.class, SoTextureUnitElement.class);
	    SO_ENABLE(SoGetMatrixAction.class, SoTextureUnitElement.class);
	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureCoordinateElement.class);
	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureImageElement.class);
	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureEnabledElement.class);
	    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureMatrixElement.class);
	  }	  
}
