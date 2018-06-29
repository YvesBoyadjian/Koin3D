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
 |      This file defines the SoLight node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLightIdElement;
import jscenegraph.database.inventor.elements.SoLightAttenuationElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFFloat;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all light source nodes.
/*!
\class SoLight
\ingroup Nodes
SoLight is the abstract base class for all light nodes. A light
node defines an illumination source that may affect subsequent shapes
in the scene graph, depending on the current lighting style.
Light sources are affected by the current transformation. A light node
under a separator does not affect any objects outside that separator.


You can also use a node kit to create a light; see the reference page for
SoLightKit.

\par Action Behavior
\par
SoGLRenderAction
<BR> Activates this light (if so specified) during traversal. All shape nodes that come after this light in the scene graph are illuminated by this light. 

\par See Also
\par
SoDirectionalLight, SoEnvironment, SoLightKit, SoLightModel, SoMaterial, SoPointLight, SoSpotLight
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoLight extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLight.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoLight.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoLight.class); }    	  	
	

    //! Determines whether the source is active or inactive. When inactive, the
    //! source does not illuminate at all.
    public final SoSFBool            on = new SoSFBool();             
	
	public final SoSFFloat intensity = new SoSFFloat();
	
	public final SoSFColor color = new SoSFColor();

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoLight class. Since this is an abstract
	   //    class, this doesn't need to deal with field data. Subclasses,
	   //    do, however.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoLight.class, "Light", SoNode.class);
	   
	       // Enable elements used by light source classes:
	       //SO_ENABLE(SoGLRenderAction, SoGLLightIdElement);
	       SoGLRenderAction.enableElement(SoGLLightIdElement.class);
	       //SO_ENABLE(SoGLRenderAction, SoLightAttenuationElement);
	       SoGLRenderAction.enableElement(SoLightAttenuationElement.class);
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoLight()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoLight*/);

    // Create and initialize fields used in all subclasses
    nodeHeader.SO_NODE_ADD_SFIELD(on,"on",        (true));
    nodeHeader.SO_NODE_ADD_SFIELD(intensity,"intensity", (1.0f));
    nodeHeader.SO_NODE_ADD_SFIELD(color,"color",     new SbColor(1.0f, 1.0f, 1.0f));
}

	  
}
