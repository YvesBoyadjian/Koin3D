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
 |      Defines the SoSceneKit class. 
 |      Organizes four lists into a scene:
 |         cameraList       -- a list of SoCameraKits and its subclasses.
 |         lightList        -- a list of SoLightKits and its subclasses.
 |         childList        -- a list of SoSeparatorKit and its subclasses
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
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Scene nodekit class.
/*!
\class SoSceneKit
\ingroup Nodekits
This nodekit is used to organize camera, (SoCameraKit),
light, (SoLightKit), and object, (SoShapeKit, SoSeparatorKit,
and SoWrapperKit) nodekits into a scene.  A scene is composed
of a list of cameras, a list of lights, and a list of children.
There are three parts created by this nodekit:
<em>cameraList</em>, <em>lightList</em>, and <em>childList</em>.


The <em>cameraList</em> part is a <em>list part</em> of SoCameraKit nodes.
The list itself is an SoNodeKitListPart, and since
only one camera can be active at a time, the
<em>container</em> of the <em>list part</em> is an SoSwitch node. Use
setCameraNumber(), and the
scene kit will set the switch to make that camera active.


The <em>lightList</em> part is a list of SoLightKit nodes.
The <em>lightList</em> is used to illuminate the objects
contained in the <em>childList</em> part.


The <em>childList</em> part contains a set of SoSeparatorKit nodes.
You can add any kind of SoSeparatorKit to this list,
including SoShapeKit and SoWrapperKit.  Since each 
SoSeparatorKit in turn contains a <em>childList</em>, this part is used
to describe a hierarchical scene. 
(See the reference page for SoSeparatorKit).
All members of <em>childList</em> are lit by the lights in
<em>lightList</em> and rendered by the active camera in <em>cameraList</em>.

\par Parts
\par
\b cameraList
<BR> This part is an SoNodeKitListPart It has a <em>container</em> that is an SoSwitch node. The list may contain  	only SoCameraKit nodekits.  The active child of 	the SoSwitch is the <em>active</em> camera. 	This part is <tt>NULL</tt> by default, but is automatically created 	whenever you add a camera, as with 	setPart("cameraList[0]", myNewCamera) 	. 	
\par
\b lightList
<BR> This part is an SoNodeKitListPart that uses an defines an SoGroup as its <em>container</em> The list may contain only 	SoLightKit nodekits.  Add SoLightKits to this 	list and they will  light the members of the <em>childList</em> 	of this SoSceneKit. 	This part is <tt>NULL</tt> by default, but is automatically created 	when you add a light. 
\par
\b childList
<BR> This part is an SoNodeKitListPart that uses an 	SoGroup for its <em>container</em>. The list may contain only 	SoSeparatorKit nodekits or nodekits derived 	from SoSeparatorKit (e.g., SoShapeKit and SoWrapperKit). 	These children represent the objects in the scene. 	This part is <tt>NULL</tt> by default, but is automatically created 	whenever you add a child to the <em>childList</em>.  	Also, when asked to build a member of the <em>childList</em>, the scenekit 	will build an SoShapeKit by default. 	So if the <em>childList</em> part is NULL, and you call: 	getPart("childList[0]", true) 	. 	the scene kit will create the <em>childList</em> and add an SoShapeKit  	as the new element in the list. 	

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoCameraKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSceneKit extends SoBaseKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoSceneKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoSceneKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoSceneKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! defines fields for the new parts in the catalog
    protected final SoSFNode topSeparator = new SoSFNode();
    protected final SoSFNode cameraList = new SoSFNode();
    protected final SoSFNode lightList = new SoSFNode();
    protected final SoSFNode childList = new SoSFNode();
    
    
   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSceneKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoSceneKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.

    // this is a switch node, so the user can pick which camera to look 
    // through.
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(topSeparator,"topSeparator", SoSeparator.class, true, "this","", false);
    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(cameraList,"cameraList", SoSwitch.class, true,
                                        "topSeparator","", SoCameraKit.class, true );
    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(lightList,"lightList", SoGroup.class, true,
                                        "topSeparator","", SoLightKit.class, true );
    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(childList,"childList", SoGroup.class, true,
                                        "topSeparator","", SoShapeKit.class, true );
    kitHeader.SO_KIT_ADD_LIST_ITEM_TYPE(childList,"childList", SoSeparatorKit.class );

    SO_KIT_INIT_INSTANCE();

    // Install a camera kit by default.
    setPart( "cameraList[0]", new SoCameraKit() );

    setCameraNumber( 0 );
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
//     gets the child number of the camera in 'cameraList' that is currently
//     selected by the switch node.
//
// Use: public

public int
getCameraNumber()
//
////////////////////////////////////////////////////////////////////////
{
    SoNode camContainer = getContainerNode( "cameraList" );

    if (        camContainer == null
         ||   !(camContainer.isOfType( SoSwitch.getClassTypeId() ) ) )
        return -1;

    SoSwitch camSwitch = (SoSwitch ) camContainer;

    int answer = (int) camSwitch.whichChild.getValue();
    return answer;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//     sets the switch node in 'cameraList' to be the number given
//
// Use: public

public void
setCameraNumber(int camNum )
//
////////////////////////////////////////////////////////////////////////
{

    SoNode camContainer = getContainerNode( "cameraList" );

    if (        camContainer == null
         ||   !(camContainer.isOfType( SoSwitch.getClassTypeId() ) ) )
        return;

    SoSwitch camSwitch = (SoSwitch ) camContainer;

    int         numCams = camSwitch.getNumChildren();

    if ( camNum < 0 || (camNum >= 0 && camNum < numCams) ) {

        camSwitch.whichChild.setValue( camNum );
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    overrides method in SoNode to return false.
//
// Use: public
//
public boolean
affectsState() 
{
    return false;
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoSceneKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoSceneKit.class, "SceneKit", SoBaseKit.class );
}

}
