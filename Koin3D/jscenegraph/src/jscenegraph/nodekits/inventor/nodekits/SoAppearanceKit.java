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
 |      Defines the SoAppearanceKit class. A parent node that manages 
 |      a collection of child nodes for
 |      complete description of the graphical appearance.
 |
 |   Author(s)          : Paul Isaacs, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoComplexity;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoEnvironment;
import jscenegraph.database.inventor.nodes.SoFont;
import jscenegraph.database.inventor.nodes.SoLightModel;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTexture2;

/**
 * @author Yves Boyadjian
 *
 */



////////////////////////////////////////////////////////////////////////////////
//! Appearance nodekit class.
/*!
\class SoAppearanceKit
\ingroup Nodekits
The SoAppearanceKit is used to create a group of property nodes
that will be used to affect subsequent <em>shape</em> nodes or nodekits in the 
scene graph.  


This nodekit defines seven new parts: <em>lightModel</em>, <em>environment</em>,
<em>drawStyle</em>, <em>material</em>, <em>complexity</em>, <em>texture2</em>, and <em>font</em>.
Note that
it does not include <em>binding</em> nodes such as SoMaterialBinding.


SoAppearanceKit is derived from SoBaseKit and thus also includes a
<em>callbackList</em> part for adding callback nodes.
\par
NOTE
 Note that SoSeparatorKit includes an SoAppearanceKit
	as a part. 
\par Parts
\par
\b lightModel
<BR> An SoLightModel node that affects any shapes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b environment
<BR> An SoEnvironment node that affects any nodes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b drawStyle
<BR> An SoDrawStyle node that affects any shapes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b material
<BR> An SoMaterial node that affects any shapes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b complexity
<BR> An SoComplexity node that affects any shapes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b texture2
<BR> An SoTexture2 node that affects any shapes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 
\par
\b font
<BR> An SoFont node that affects any text nodes that follow this     nodekit in the scene graph.  This part is <tt>NULL</tt> by default. 

\par See Also
\par
SoBaseKit, SoCameraKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoAppearanceKit extends SoBaseKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoAppearanceKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoAppearanceKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoAppearanceKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! defines fields for the new parts in the catalog
    protected final SoSFNode lightModel = new SoSFNode();
    protected final SoSFNode environment = new SoSFNode();
    protected final SoSFNode drawStyle = new SoSFNode();
    protected final SoSFNode material = new SoSFNode();
    protected final SoSFNode complexity = new SoSFNode();
    protected final SoSFNode texture2 = new SoSFNode();
    protected final SoSFNode font = new SoSFNode();

    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoAppearanceKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoAppearanceKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(lightModel,"lightModel",  SoLightModel.class, true, "this","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(environment,"environment", SoEnvironment.class,true, "this","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(drawStyle,"drawStyle",   SoDrawStyle.class,  true, "this","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(material,"material",    SoMaterial.class,   true, "this","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(complexity,"complexity",  SoComplexity.class, true, "this","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(texture2,"texture2",    SoTexture2.class,   true, "this","",true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(font,"font",        SoFont.class,     true, "this","",true );

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
//    This initializes the SoAppearanceKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoAppearanceKit.class, "AppearanceKit", SoBaseKit.class);
}

}
