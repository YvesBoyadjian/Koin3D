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
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoMaterial node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;


////////////////////////////////////////////////////////////////////////////////
//! Surface material definition node.
/*!
\class SoMaterial
\ingroup Nodes
This node defines the current surface material properties for all
subsequent shapes. SoMaterial sets several components of the
current material during traversal. 


Multiple values can be specified for
the \b diffuseColor  and \b transparency .
Different shapes interpret
materials with multiple values differently. To bind materials to
shapes, use an SoMaterialBinding node.

\par File Format/Default
\par
\code
Material {
  ambientColor 0.2 0.2 0.2
  diffuseColor 0.8 0.8 0.8
  specularColor 0 0 0
  emissiveColor 0 0 0
  shininess 0.2
  transparency 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the ambient color, the diffuse color, the specular color, the emissive color, the shininess, and the transparency of the current material. 

\par See Also
\par
SoBaseColor, SoLightModel, SoMaterialBinding, SoPackedColor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterial extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoMaterial.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoMaterial.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoMaterial.class); }    	  	
	

	 public final SoMFColor ambientColor = new SoMFColor();
	  
	 public final SoMFColor diffuseColor = new SoMFColor();
	  
	 public final SoMFColor specularColor = new SoMFColor();
	  
	 public final SoMFColor emissiveColor = new SoMFColor();
	  
    //! Shininess coefficient of the surface. Values can range from 0.0 for
    //! no shininess (a diffuse surface) to 1.0 for maximum shininess (a
    //! highly polished surface).
	 public final SoMFFloat shininess = new SoMFFloat();
	  
    //! Transparency value(s) of the surface. Values can range from 0.0 for
    //! opaque surfaces to 1.0 for completely transparent surfaces.  If the
    //! transparency type is SoGLRenderAction::SCREEN_DOOR then only the
    //! first transparency value will be used.  With other transparency types,
    //! multiple transparencies will be used, if the SoMaterial node
    //! contains as many transparencies as diffuse colors.  If there are not
    //! as many transparencies as diffuse colors, only the first transparency
    //! will be used.
	 public final SoMFFloat transparency = new SoMFFloat();
	  	

    protected SoColorPacker colorPacker;

    protected final SoVBO[] _vbo = new SoVBO[1];


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoMaterial()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoMaterial*/);

    nodeHeader.SO_NODE_ADD_MFIELD(ambientColor,"ambientColor",  (SoLazyElement.getDefaultAmbient()));
    nodeHeader.SO_NODE_ADD_MFIELD(diffuseColor,"diffuseColor",  (SoLazyElement.getDefaultDiffuse()));
    nodeHeader.SO_NODE_ADD_MFIELD(specularColor,"specularColor",(SoLazyElement.getDefaultSpecular()));
    nodeHeader.SO_NODE_ADD_MFIELD(emissiveColor,"emissiveColor",(SoLazyElement.getDefaultEmissive()));
    nodeHeader.SO_NODE_ADD_MFIELD(shininess,"shininess",        (SoLazyElement.getDefaultShininess()));
    nodeHeader.SO_NODE_ADD_MFIELD(transparency,"transparency",  (SoLazyElement.getDefaultTransparency()));
    isBuiltIn = true;
    colorPacker = new SoColorPacker();
    _vbo[0] = null;
}

	
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    This initializes the SoMaterial class.
	    //
	    // Use: internal
	    
	  public static  void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SoSubNode.SO__NODE_INIT_CLASS(SoMaterial.class, "Material", SoNode.class);
	   
	       // Enable elements:
	       SO_ENABLE(SoCallbackAction.class, SoLazyElement.class);
	        //SoCallbackAction.enableElement(SoLazyElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLLazyElement.class);
	        //SoGLRenderAction.enableElement(SoGLLazyElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
	        //SoGLRenderAction.enableElement(SoGLVBOElement.class);
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    colorPacker.destructor();
    if(_vbo[0] != null) // java port
    	_vbo[0].destructor();
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs accumulation of state for actions.
//
// Use: extender

public void SoMaterial_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState             state = action.getState();
    int bitmask = 0;    

    // Set all non-ignored components

    if (! ambientColor.isIgnored() && ambientColor.getNum() > 0
        && ! SoOverrideElement.getAmbientColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setAmbientColorOverride(state, this, true);
        }
        bitmask |= SoLazyElement.masks.AMBIENT_MASK.getValue(); 
    }

    if (! diffuseColor.isIgnored() && diffuseColor.getNum() > 0
        && ! SoOverrideElement.getDiffuseColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setDiffuseColorOverride(state, this, true);
            // Diffuse color and transparency share override state
            if (! transparency.isIgnored() && transparency.getNum() > 0)
                bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
        }
        bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
    }

    if (! transparency.isIgnored() && transparency.getNum() > 0
        && ! SoOverrideElement.getTransparencyOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setTransparencyOverride(state, this, true);
            // Diffuse color and transparency share override state
            if (! diffuseColor.isIgnored() && diffuseColor.getNum() > 0)
                bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
        }
        bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
    }
    if (! specularColor.isIgnored() && specularColor.getNum() > 0
        && ! SoOverrideElement.getSpecularColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setSpecularColorOverride(state, this, true);
        }
        bitmask |= SoLazyElement.masks.SPECULAR_MASK.getValue();
    }

    if (! emissiveColor.isIgnored() && emissiveColor.getNum() > 0
        && ! SoOverrideElement.getEmissiveColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setEmissiveColorOverride(state, this, true);
        }
        bitmask |= SoLazyElement.masks.EMISSIVE_MASK.getValue();
    }

    if (! shininess.isIgnored() && shininess.getNum() > 0
        && ! SoOverrideElement.getShininessOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setShininessOverride(state, this, true);
        }
        bitmask |= SoLazyElement.masks.SHININESS_MASK.getValue();
    }
    SoLazyElement.setMaterials(state, this, bitmask, colorPacker,   
        diffuseColor, transparency, ambientColor, 
        emissiveColor, specularColor, shininess);

}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Method for callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoMaterial_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Method for GL rendering
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoMaterial_doAction(action);

    // If there's only one color, we might as well send it now.  This
    // prevents cache dependencies in some cases that were
    // specifically optimized for Inventor 2.0.
    if (diffuseColor.getNum() == 1 && !diffuseColor.isIgnored())
        SoGLLazyElement.sendAllMaterial(action.getState());

    SoState state = action.getState();
    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
      // update the VBO, no data is passed since that is done by SoColorPacker later on
      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, _vbo);
    }
}
	  
}
