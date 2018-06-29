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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the transform box dragger class.
 |      This dragger allows you to perform rotations,uniform scales
 |      and translations, all in one tidy box.
 |
 | NOTE TO DEVELOPERS:
 |     For info about the structure of SoTabBoxDragger:
 |     [1] compile: /usr/share/src/Inventor/samples/ivNodeKitStructure
 |     [2] type:    ivNodeKitStructure SoTabBoxDragger.
 |     [3] The program prints a diagram of the scene graph and a table with 
 |         information about each part.
 |
 |  The following parts in this dragger are created at construction time.
 |  'ResourceName' corresponds to the name of the default geometry for the
 |  part. The dragger's constructor gets the scene graph for 'ResourceName'
 |  by querying the global dictionary ( SoDB::getByName("ResourceName"); ).  
 |
 |  Resource Name:                           Part Name:
 |
 |  tabBoxBoxGeom                          - boxGeom
 |
 |  tabBoxTranslator                       - tabPlane1.translator
 |  tabBoxScaleTabMaterial                 - tabPlane1.scaleTabMaterial
 |  tabBoxScaleTabHints                    - tabPlane1.scaleTabHints
 |  (ditto for tabPlane2 - tabPlane6)
 |
 |   Author(s): Paul Isaacs, Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.draggers;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.interaction.inventor.nodekits.SoInteractionKit;
import jscenegraph.nodekits.inventor.nodekits.SoNodekitCatalog;
import jscenegraph.nodekits.inventor.nodekits.SoSubKit;


/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Cubic object you can translate and scale by dragging with the mouse.
/*!
\class SoTabBoxDragger
\ingroup Draggers
SoTabBoxDragger 
is a composite dragger shaped like a box.
Inside it are six SoTabPlaneDraggers which the dragger positions
and orients to form a cube.
The operations available in an SoTabPlaneDragger (translation, 
1D scaling and 2D scaling) are available on each face of the cube. 
Since they each move in their local space, the dragger may be easily 
translated or scaled in any direction.


As a composite dragger, this class makes sure that when
one plane is dragged, the entire box moves together.


As each sub-dragger is moved, the SoTabBoxDragger updates 
its \b scaleFactor  and \b translation  fields.
As with all draggers, if you change a field the dragger will
move to match the new settings.


<em>Remember:</em> This is <em>not</em> an SoTransform!. 
If you want to move other objects with this dragger, you can either: 


[a] Use an SoTabBoxManip, which is subclassed from SoTransform. 
It creates one of
these draggers and uses it as the interface to change its fields.
(see the SoTabBoxManip man page).  


[b] Use field-to-field connections to connect the fields of this dragger to
those of any SoTransformation node.


You can change the parts in any instance of this dragger using
setPart().  The default part geometries
are defined as resources for this SoTabBoxDragger class.  They are
detailed below in the Dragger Resources section of the online reference
page for this class.  You can make your program use different default
resources for the parts by copying the file
\b /usr/share/data/draggerDefaults/tabBoxDragger.iv   into your own
directory, editing the file, and then setting the environment variable
\b SO_DRAGGER_DIR  to be a path to that directory.

\par File Format/Default
\par
\code
TabBoxDragger {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
  isActive FALSE
  translation 0 0 0
  scaleFactor 1 1 1
}
\endcode

\par See Also
\par
SoInteractionKit, SoDragger, SoCenterballDragger, SoDirectionalLightDragger, SoDragPointDragger, SoHandleBoxDragger, SoJackDragger, SoPointLightDragger, SoRotateCylindricalDragger, SoRotateDiscDragger, SoRotateSphericalDragger, SoScale1Dragger, SoScale2Dragger, SoScale2UniformDragger, SoScaleUniformDragger, SoSpotLightDragger, SoTabPlaneDragger, SoTrackballDragger, SoTransformBoxDragger, SoTransformerDragger, SoTranslate1Dragger, SoTranslate2Dragger
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTabBoxDragger extends SoDragger {
	
    //SO_KIT_HEADER(SoTabBoxDragger);
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoTabBoxDragger.class,this);
	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoTabBoxDragger.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader == null ? super.getTypeId() : kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoTabBoxDragger.class); }              
	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

	

    public final SoSFVec3f    translation = new SoSFVec3f();
      public final SoSFVec3f    scaleFactor = new SoSFVec3f();
      

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Initializes the type ID for this dragger node. This
//  should be called once after SoInteraction::init().
//
// Use: public
//
public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoTabBoxDragger.class, "TabBoxDragger", SoDragger.class);
}

      
 }
