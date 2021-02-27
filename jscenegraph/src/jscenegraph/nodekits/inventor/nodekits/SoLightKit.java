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
 |      Defines the SoLightKit class. 
 |      This is the base class for all other lights.
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.database.inventor.nodes.SoTransformSeparator;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Light nodekit class.
/*!
\class SoLightKit
\ingroup Nodekits
This nodekit class is used to create light nodes that have
a local transformation and a geometry icon to represent the light source.
SoLightKit adds three public parts to the basic nodekit:
<em>transform</em>, <em>light</em>, and <em>icon</em>.


SoLightKit creates an SoDirectionalLight as the <em>light</em> part 
by default - all other parts are <tt>NULL</tt> at creation.


You can move the light relative to the rest of the scene by creating 
and editing the <em>transform</em> part.


You can add a geometrical representation for the light by
setting the <em>icon</em> part to be any scene graph you like.


SoLightKit also adds two private parts. An SoTransformSeparator 
contains the effect of <em>transform</em> to move only the <em>light</em> and <em>icon</em>, 
while allowing the <em>light</em> to illuminate the rest of the scene.
The second private part is an SoSeparator, which keeps property nodes 
within the <em>icon</em> geometry from affecting the rest of the scene.
It also serves to cache the <em>icon</em> even when the <em>light</em> 
or <em>transform</em> is changing.


SoLightKit is derived from SoBaseKit and thus also includes a
<em>callbackList</em> part for adding callback nodes.

\par Parts
\par
\b transform
<BR> This part positions and orients the light and icon relative to the rest   of the scene.     Its effect is kept local to this nodekit     by a private part of type SoTransformSeparator. The <em>transform</em>      part is <tt>NULL</tt> by default. If you ask for <em>transform</em> using      getPart(),  an SoTransform will be returned.     But you may set the part to be any subclass of SoTransform.     For example, set the <em>transform</em> to be an SoDragPointManip     and the <em>light</em> to be an SoPointLight. Then you can move the light     by dragging the manipulator with the mouse. 
\par
\b light
<BR> The light node for this nodekit. This can be set to any node derived     from SoLight.  An SoDirectionalLight is created by default,     and it is also the type of light returned when the you request that     the nodekit build a light for you. 
\par
\b icon
<BR> This part is a user-supplied scene graph that represents the light     source. It is <tt>NULL</tt> by default em an SoCube is created by the     lightkit when a method requires it to build the part itself. 

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoCameraKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoLightKit extends SoBaseKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoLightKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoLightKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoLightKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! defines fields for the new parts in the catalog
    protected final SoSFNode transformGroup = new SoSFNode();
    protected final SoSFNode transform = new SoSFNode();
    protected final SoSFNode light = new SoSFNode();
    protected final SoSFNode iconSeparator = new SoSFNode();
    protected final SoSFNode icon = new SoSFNode();


    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoLightKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoLightKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(transformGroup,"transformGroup", SoTransformSeparator.class,
                            true,  "this","", true);
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(transform,"transform", SoTransform.class, 
                                    true, "transformGroup","", true  );
    kitHeader.SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY(light,"light", SoLight.class, SoDirectionalLight.class, 
                                    false, "transformGroup","", true  );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY( iconSeparator,"iconSeparator", SoSeparator.class, true,
                                    "transformGroup","", true);
    kitHeader.SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY( icon,"icon", SoNode.class, SoCube.class, true,
                                    "iconSeparator","", true);

    SO_KIT_INIT_INSTANCE();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoLightKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoLightKit.class, "LightKit", SoBaseKit.class );
}

}
