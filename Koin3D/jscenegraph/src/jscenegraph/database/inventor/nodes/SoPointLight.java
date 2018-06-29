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
 |      This file defines the SoPointLight node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLightIdElement;
import jscenegraph.database.inventor.elements.SoLightAttenuationElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Node representing a point light source.
/*!
\class SoPointLight
\ingroup Nodes
This node defines a point light source at a fixed 3D location.  A
point source illuminates equally in all directions; that is, it is
omni-directional.

\par File Format/Default
\par
\code
PointLight {
  on TRUE
  intensity 1
  color 1 1 1
  location 0 0 1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Activates this light (if so specified) during traversal. All shape nodes that come after this light in the scene graph are illuminated by this light. The light's location is affected by the current transformation. 

\par See Also
\par
SoDirectionalLight, SoSpotLight
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPointLight extends SoLight implements Destroyable {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPointLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPointLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPointLight.class); }    	  	
	
	

	public final SoSFVec3f location = new SoSFVec3f();
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoPointLight()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPointLight*/);
    nodeHeader.SO_NODE_ADD_SFIELD(location,"location", new SbVec3f(0.0f, 0.0f, 1.0f));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates a light source during rendering.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    int     id;

    // Don't turn light on if it's off
    if (! on.getValue())
        return;

    // Get a new light id to use for this light
    id = SoGLLightIdElement.increment(action.getState());

    // Element is being overridden or we have too many sources for GL
    // to handle? Skip the whole deal.
    if (id < 0)
        return;

    //
    // Create a new source and send it to GL. The SoGLLightIdElement
    // has already enabled the light.
    //

    final SbVec3f     v3 = new SbVec3f();
    final SbVec4f     v4 = new SbVec4f();

    id = GL2.GL_LIGHT0 + id;

    // RGBA intensities of source are the product of the color and
    // intensity, with 1.0 alpha
    v3.copyFrom( color.getValue().operator_mul(intensity.getValue()));
    v4.setValue(v3.getValue()[0], v3.getValue()[1], v3.getValue()[2], 1.0f);
    
    GL2 gl2 = action.getCacheContext();

    gl2.glLightfv( id, GL2.GL_AMBIENT, new SbVec4f(0.0f, 0.0f, 0.0f, 1.0f).getValue(),0);
    gl2.glLightfv( id, GL2.GL_DIFFUSE,  v4.getValue(),0);
    gl2.glLightfv( id, GL2.GL_SPECULAR, v4.getValue(),0);

    // Set position
    v3.copyFrom( location.getValue());
    v4.setValue(v3.getValue()[0], v3.getValue()[1], v3.getValue()[2], 1.0f);
    gl2.glLightfv( id, GL2.GL_POSITION, v4.getValue(),0);

    // Make sure no spotlight stuff is on
    gl2.glLightf( id, GL2.GL_SPOT_EXPONENT, 0.0f);
    gl2.glLightf( id, GL2.GL_SPOT_CUTOFF, 180.0f);

    // Attenuation is accessed from the state
    SbVec3f atten = SoLightAttenuationElement.get(action.getState());
    gl2.glLightf( id, GL2.GL_CONSTANT_ATTENUATION,  atten.getValue()[2]);
    gl2.glLightf( id, GL2.GL_LINEAR_ATTENUATION,    atten.getValue()[1]);
    gl2.glLightf( id, GL2.GL_QUADRATIC_ATTENUATION, atten.getValue()[0]);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoPointLight class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoPointLight.class, "PointLight", SoLight.class);
}


}
