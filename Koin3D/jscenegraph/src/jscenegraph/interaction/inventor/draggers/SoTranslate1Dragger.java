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
 |      This file defines the one dimensional translate dragger class.
 |      This is a simple dragger that allows an object to
 |      be translated in one dimension, along a given direction
 |
 |      The mouse position is projected onto a line determined
 |      by the translation direction and the point that the mouse
 |      first hit. The amount moved along that line determines the 
 |      amount of translation.
 |
 | NOTE TO DEVELOPERS:
 |     For info about the structure of SoTranslate1Dragger:
 |     [1] compile: /usr/share/src/Inventor/samples/ivNodeKitStructure
 |     [2] type:    ivNodeKitStructure SoTranslate1Dragger.
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
 |  translate1Translator                   - translator
 |  translate1TranslatorActive             - translatorActive
 |  translate1Feedback                     - feedback
 |  translate1FeedbackActive               - feedbackActive
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
import jscenegraph.nodekits.inventor.nodekits.SoNodekitCatalog;
import jscenegraph.nodekits.inventor.nodekits.SoSubKit;


/**
 * @author Yves
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Object you can translate along a line by dragging With the mouse.
/*!
\class SoTranslate1Dragger
\ingroup Draggers
SoTranslate1Dragger 
is a simple dragger that translates 
in one dimension when dragged with the mouse.  
It moves along the x axis of its local space, determined by its 
location in the scene graph. 
Transformation nodes placed before it will 
affect both the dragger and the direction of motion.

 
This node has a \b translation  field 
which always reflects 
its position in local space.  
If you set the field, the dragger will move accordingly.
You can also connect fields of other nodes or engines from 
this one
to make them follow the dragger's motion.


This dragger contains four parts, <em>translator</em>, <em>translatorActive</em>,
<em>feedback</em>, and <em>feedbackActive</em>.  


Each of these is set by default from
a resource described in the <tt>Dragger Resources</tt> section of the online
reference page for this class.  You 
can change the parts in any instance of this dragger using 
setPart().


You can make your program use different default resources for the parts
by copying the file
\b /usr/share/data/draggerDefaults/translate1Dragger.iv 
into your own directory, editing the file, and then 
setting the environment variable \b SO_DRAGGER_DIR  to be a path to that directory.

\par File Format/Default
\par
\code
Translate1Dragger {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
  isActive FALSE
  translation 0 0 0
}
\endcode

\par See Also
\par
SoInteractionKit, SoDragger, SoCenterballDragger, SoDirectionalLightDragger, SoDragPointDragger, SoHandleBoxDragger, SoJackDragger, SoPointLightDragger, SoRotateCylindricalDragger, SoRotateDiscDragger, SoRotateSphericalDragger, SoScale1Dragger, SoScale2Dragger, SoScale2UniformDragger, SoScaleUniformDragger, SoSpotLightDragger, SoTabBoxDragger, SoTabPlaneDragger, SoTrackballDragger, SoTransformBoxDragger, SoTransformerDragger, SoTranslate2Dragger
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTranslate1Dragger extends SoDragger {
	
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoTranslate1Dragger.class,this);	
	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoTranslate1Dragger.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader == null ? super.getTypeId() : kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoTranslate1Dragger.class); }              

    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    public final SoSFVec3f translation = new SoSFVec3f();
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initialize the dragger
//
// Use: public, internal
//
public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoTranslate1Dragger.class, "Translate1Dragger", SoDragger.class);
}

    
 }
