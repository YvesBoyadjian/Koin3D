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
 |      This file defines the SoEnvironment node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoLightAttenuationElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Global environment node.
/*!
\class SoEnvironment
\ingroup Nodes
This node describes global environmental attributes such as ambient
lighting, light attenuation, and fog.


Ambient lighting is the amount of extra light impinging on each
surface point when the lighting model is Phong (see SoLightModel).


Light attenuation affects all subsequent lights in a scene (see
SoLight). It is a quadratic function of distance from a light
source to a surface point.  The three coefficients are specified in
the \b attenuation  field.  Attenuation works only for light sources
with a fixed location, such as point and spot lights.


Fog has one of four types, each of which blends each surface point
with the specified fog color. Each type interprets the \b visibility 
field to be the distance at which fog totally obscures objects. A
\b visibility  value of 0 (the default) causes the SoEnvironment
node to set up fog so that the visibility is the distance to the far
clipping plane of the current camera.


Note that this node has effect only during rendering, and that it
does not inherit field values from other SoEnvironment nodes.

\par File Format/Default
\par
\code
Environment {
  ambientIntensity 0.2
  ambientColor 1 1 1
  attenuation 0 0 1
  fogType NONE
  fogColor 1 1 1
  fogVisibility 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Sets the current environment parameters to those specified with this node. Successive geometries will be rendered using this environment. 

\par See Also
\par
SoLight, SoLightModel
*/
////////////////////////////////////////////////////////////////////////////////

public class SoEnvironment extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoEnvironment.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoEnvironment.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoEnvironment.class); }              


  public
    //! Type of fog:
    enum FogType {
        NONE,                   //!< No fog
        HAZE,                   //!< Linear increase in opacity with distance
        FOG,                    //!< Exponential increase in opacity
        SMOKE;                   //!< Exponential squared increase in opacity
    	
    	public int getValue() {
    		return ordinal();
    	}

		public static FogType fromValue(Integer value) {			
			return FogType.values()[value];
		}
    };


    //! \name Fields
    //@{

    //! Intensity and RGB color of ambient lighting (for Phong lighting).
    public final SoSFFloat           ambientIntensity = new SoSFFloat();

    //! Intensity and RGB color of ambient lighting (for Phong lighting).
    public final SoSFColor           ambientColor = new SoSFColor();

    //@}

    public final SoSFVec3f           attenuation = new SoSFVec3f();

    public final SoSFEnum            fogType = new SoSFEnum();
    public final SoSFColor           fogColor = new SoSFColor();
    public final SoSFFloat           fogVisibility = new SoSFFloat();

    //! Creates an environment node with default settings.
    public SoEnvironment() {
    	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoEnvironment.class*/);
    	nodeHeader.SO_NODE_ADD_FIELD(ambientIntensity,"ambientIntensity", (0.2f));
    	nodeHeader.SO_NODE_ADD_FIELD(ambientColor,"ambientColor",     new SbColor(1.0f, 1.0f, 1.0f));
    	nodeHeader.SO_NODE_ADD_FIELD(attenuation,"attenuation",      new SbVec3f(0.0f, 0.0f, 1.0f));
    	nodeHeader.SO_NODE_ADD_FIELD(fogType,"fogType",          (FogType.NONE.getValue()));
    	nodeHeader.SO_NODE_ADD_FIELD(fogColor,"fogColor",         new SbColor(1.0f, 1.0f, 1.0f));
    	nodeHeader.SO_NODE_ADD_FIELD(fogVisibility,"fogVisibility",    (0.0f));

    // Set up static info for enumerated type field
    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FogType.NONE);
    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FogType.HAZE);
    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FogType.FOG);
    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FogType.SMOKE);

    // Set up info in enumerated type fields
    	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(fogType,"fogType", "FogType");

    isBuiltIn = true;    	
    }

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up environment parameters during rendering.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     v3 = new SbVec3f();
    final SbVec4f     v4 = new SbVec4f();
    
    GL2 gl2 = action.getCacheContext();

    //////////////////////
    //
    // Set up ambient lighting.
    //

    // RGBA ambient intensity is the product of the color and
    // intensity, with 1.0 alpha
    v3.copyFrom( ambientColor.getValue().operator_mul(ambientIntensity.getValue()));
    v4.setValue(v3.getValue()[0], v3.getValue()[1], v3.getValue()[2], 1.0f);
    glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, v4.getValue());

    //////////////////////
    //
    // Set up light attenuation. This is stored in the
    // SoLightAttenuationElement, which is then accessed by subsequent
    // light sources.
    //

    SoLightAttenuationElement.set(action.getState(), this,
                                   attenuation.getValue());

    //////////////////////
    //
    // Set up fog.
    //
    FogType     type = FogType.fromValue(fogType.getValue());

    if (type == FogType.NONE)
        gl2.glDisable(GL2.GL_FOG);

    else {
        float visibility = fogVisibility.getValue();

        // Check for visibility of 0, which is the default value - this
        // means that we should use the far plane of the current view
        // volume as the visibility
        if (visibility == 0.0) {
            final SbViewVolume vol =
                SoViewVolumeElement.get(action.getState());
            visibility = vol.getFarDist();
        }

        gl2.glEnable(GL2.GL_FOG);
        glFogfv(GL2.GL_FOG_COLOR, fogColor.getValue().getValue());

        switch (type) {

          case NONE:
            // Can't get here!
            break;

          case HAZE:
            // Set up linear ramp based on visibility
        	  gl2.glFogf(GL2.GL_FOG_MODE,         GL2.GL_LINEAR);
        	  gl2.glFogf(GL2.GL_FOG_START,        0.0f);
        	  gl2.glFogf(GL2.GL_FOG_END,          visibility);
            break;
            
          case FOG:
        	  gl2.glEnable(GL2.GL_FOG);
        	  gl2.glFogf(GL2.GL_FOG_MODE,         GL2.GL_EXP);
        	  gl2.glFogf(GL2.GL_FOG_DENSITY,      computeDensity(visibility, false));
            break;
            
          case SMOKE:
        	  gl2.glFogf(GL2.GL_FOG_MODE,         GL2.GL_EXP2);
        	  gl2.glFogf(GL2.GL_FOG_DENSITY,      computeDensity(visibility, true));
            break;
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: extender
public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoLightAttenuationElement.set(action.getState(), this,
                                   attenuation.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes fog density based on visibility.
//
// Use: private

private float computeDensity(float visibility, boolean squared)
//
////////////////////////////////////////////////////////////////////////
{
    //
    // We want nearly total opacity at a distance of "visibility" from
    // the eye. The exponential GL fog function is:
    //
    //          f = e ** (-density * distance)
    //
    // (the exponent is squared in the SMOKE case)
    //
    // Since this function approaches 0 asymptotically, we have to
    // choose a reasonable cutoff point that approximates total
    // opacity. e ** -4 is about 0.018, so all we have to do is make
    // the exponent equal to -4 at a distance of "visibility".
    //

    if (squared)
        return 2.0f / visibility;

    else
        return 4.0f / visibility;
}
    
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoEnvironment class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoEnvironment.class, "Environment", SoNode.class);

    // Enable elements for appropriate actions:

    SO_ENABLE(SoCallbackAction.class, SoLightAttenuationElement.class);
}

}
