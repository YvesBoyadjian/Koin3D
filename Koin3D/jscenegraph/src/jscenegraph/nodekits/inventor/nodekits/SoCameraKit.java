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
 |      Defines the SoCameraKit class. 
 |      Subclassed off of SoBaseKit, it is the base class for all camera kits.
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
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.database.inventor.nodes.SoTransformSeparator;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Camera nodekit class.
/*!
\class SoCameraKit
\ingroup Nodekits
This nodekit class is used to create camera nodes that have
a local transformation.  
SoCameraKit adds two public parts to the basic nodekit:
<em>transform</em> and <em>camera</em>. 


The <em>camera</em> part is created by default as an SoPerspectiveCamera
node, but may later be changed to any subclass of SoCamera.


You can move the camera relative to the rest of the scene by creating 
and editing the <em>transform</em> part.


SoCameraKit also adds a private part, <em>transformGroup</em>, which is
of type SoTransformSeparator. The kit uses this part to contain the
effect of <em>transform</em> to move only the <em>camera</em>, while allowing the
<em>camera</em> to affect the rest of the scene.


SoCameraKit is derived from SoBaseKit
and thus also includes a <em>callbackList</em> part for adding callback nodes.

\par Parts
\par
\b transform
<BR> A transform that positions and orients the camera relative to the   rest of the scene.    Private parts keep the effect of the <em>transform</em> part localized.   This part is <tt>NULL</tt> by default, but may be set to any subclass of    SoTransform   
\par
\b camera
<BR> The camera node for this nodekit. The <em>camera</em> part is created by default as an SoPerspectiveCamera node, but may later be changed to any subclass of SoCamera. (e.g., SoPerspectiveCamera, SoOrthographicCamera). 

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoCameraKit extends SoBaseKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoCameraKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoCameraKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoCameraKit.class); }              

	
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
    protected final SoSFNode camera = new SoSFNode();



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoCameraKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoCameraKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(transformGroup,"transformGroup", SoTransformSeparator.class, 
                                true, "this","", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(transform,"transform", SoTransform.class, 
                                    true, "transformGroup","", true  );
    kitHeader.SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY(camera,"camera", SoCamera.class, SoPerspectiveCamera.class,
                                         false, "transformGroup","", true);

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
//    This initializes the SoCameraKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoCameraKit.class, "CameraKit", SoBaseKit.class );
}

}
