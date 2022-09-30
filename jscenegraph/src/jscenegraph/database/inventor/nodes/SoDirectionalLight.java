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
 |      This file defines the SoDirectionalLight node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_SPOT_CUTOFF;
import static jscenegraph.opengl.fixedfunc.GLLightingFunc.GL_SPOT_EXPONENT;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoLightElement;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLightIdElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;


////////////////////////////////////////////////////////////////////////////////
//! Node representing a directional light source.
/*!
\class SoDirectionalLight
\ingroup Nodes
This node defines a directional light source that illuminates along
rays parallel to a given 3-dimensional vector.

\par File Format/Default
\par
\code
DirectionalLight {
  on TRUE
  intensity 1
  color 1 1 1
  direction 0 0 -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Activates this light (if so specified) during traversal. All shape nodes that come after this light in the scene graph are illuminated by this light. The light's direction is affected by the current transformation. 

\par See Also
\par
SoPointLight, SoSpotLight
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDirectionalLight extends SoLight {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoDirectionalLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoDirectionalLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoDirectionalLight.class); }    	  	
	
	

	public final SoSFVec3f direction = new SoSFVec3f();

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoDirectionalLight class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoDirectionalLight.class, "DirectionalLight", SoLight.class);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoDirectionalLight()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoDirectionalLight*/);
    nodeHeader.SO_NODE_ADD_SFIELD(direction,"direction", new SbVec3f(0.0f, 0.0f, -1.0f));
    isBuiltIn = true;
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
    SoState state = action.getState();
    id = SoGLLightIdElement.increment(state);

    // Element is being overridden or we have too many sources for GL
    // to handle? Skip the whole deal.
    if (id < 0)
        return;

    
    SoLightElement.add(state, this, SoModelMatrixElement.get(state).operator_mul( 
            SoViewingMatrixElement.get(state)));

    //
    // Create a new source and send it to GL. The SoGLLightIdElement
    // has already enabled the light.
    //
    
    GL2 gl2 = Ctx.get(action.getCacheContext());

    final SbVec3f     v3 = new SbVec3f();
    final SbVec4fSingle     v4 = new SbVec4fSingle();

    id = GL_LIGHT0 + id;

    // RGBA intensities of source are the product of the color and
    // intensity, with 1.0 alpha
    v3.copyFrom(color.getValue().operator_mul(intensity.getValue()));
    v4.setValue(v3.getValueRead()[0], v3.getValueRead()[1], v3.getValueRead()[2], 1.0f);

    gl2.glLightfv( id, GL_AMBIENT, new SbVec4fSingle(0.0f, 0.0f, 0.0f, 1.0f).getValue(),0);
    gl2.glLightfv( id, GL_DIFFUSE,  v4.getValue(),0);
    gl2.glLightfv( id, GL_SPECULAR, v4.getValue(),0);

    // "Position" is the direction vector negated with a 0.0 w
    // component. Yet another GL peccadillo.
    v3.copyFrom(direction.getValue());
    v4.setValue(-v3.getValueRead()[0], -v3.getValueRead()[1], -v3.getValueRead()[2], 0.0f);
    gl2.glLightfv( id, GL_POSITION, v4.getValue(),0);

    // Make sure no spotlight stuff is on
    gl2.glLightf( id, GL_SPOT_EXPONENT, 0.0f);
    gl2.glLightf( id, GL_SPOT_CUTOFF, 180.0f);

    // Attenuation does not matter for directional sources.
}

}
