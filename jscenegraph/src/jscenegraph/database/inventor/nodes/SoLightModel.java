/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision $
 |
 |   Description:
 |      This file defines the SoLightModel node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Node that defines the lighting model to use when rendering.
/*!
\class SoLightModel
\ingroup Nodes
This node defines the lighting model to be used when rendering
subsequent shapes. The lighting model is specified in the \b model 
field. When the default model (Phong lighting) is used, light sources
are required in a scene for objects to be easily visible.

\par File Format/Default
\par
\code
LightModel {
  model PHONG
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current lighting model in the state. 

\par See Also
\par
SoBaseColor, SoEnvironment, SoLight, SoMaterial
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoLightModel extends SoNode {

   private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLightModel.class,this);
   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoLightModel.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoLightModel.class); }              
   
     public
       //! Lighting model:
       enum Model {
           BASE_COLOR(SoLazyElement.LightModel.BASE_COLOR.getValue()), //!< Just use base color
          PHONG      (SoLazyElement.LightModel.PHONG.getValue());       //!< Phong lighting
          
           private int value;
           
           Model(int value) {
        	   this.value = value;
           }
           
           public int getValue() {
        	   return value;
           }
      };
  
      //! \name Fields
      //@{
  
      //! Lighting model to use
      public final SoSFEnum            model = new SoSFEnum();
  
      //@}
  
      //! Creates a light model node with default settings.
      public SoLightModel() {
    	  
    	  nodeHeader.SO_NODE_CONSTRUCTOR();
    	      	      
    	  nodeHeader.SO_NODE_ADD_SFIELD(model,"model", SoLazyElement.getDefaultLightModel());
    	      
    	          // Set up static info for enumerated type field
    	          nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Model", "BASE_COLOR", Model.BASE_COLOR.getValue());
    	          nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Model", "PHONG", Model.PHONG.getValue());
    	      
    	          // Set up info in enumerated type field
    	          nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(model,"model", "Model");
    	      
    	          isBuiltIn = true;    	         	  
      }
  
    public
       void        doAction(SoAction action) {
        SoState     state = action.getState();
         
             if (! model.isIgnored()
                 && ! SoOverrideElement.getLightModelOverride(state)) {
                 if (isOverride()) {
                     SoOverrideElement.setLightModelOverride(state, this, true);
                 }
         
                 SoLazyElement.LightModel lt =
                     SoLazyElement.LightModel.fromValue(model.getValue());
         
                 SoLazyElement.setLightModel(state, lt.getValue());
             }
             	
    }
    public void        GLRender(SoGLRenderAction action) {
    	 /*SoLightModel.*/doAction(action);
    }
    public void        callback(SoCallbackAction action) {
    	 /*SoLightModel.*/doAction(action);
    }
  
     public static void         initClass() {
    	 
    	 SoSubNode.SO__NODE_INIT_CLASS(SoLightModel.class, "LightModel", SoNode.class);
          
              //SO_ENABLE(SoGLRenderAction, SoShapeStyleElement);
    	 SoGLRenderAction.enableElement(SoShapeStyleElement.class);
              //SO_ENABLE(SoCallbackAction, SoShapeStyleElement);
    	 SoCallbackAction.enableElement(SoShapeStyleElement.class);
              //SO_ENABLE(SoGLRenderAction, SoGLLazyElement);
    	 SoGLRenderAction.enableElement(SoGLLazyElement.class);
              //SO_ENABLE(SoCallbackAction, SoLazyElement);
    	 SoCallbackAction.enableElement(SoLazyElement.class);
             	 
     }
  
    protected void close(){}
 }
