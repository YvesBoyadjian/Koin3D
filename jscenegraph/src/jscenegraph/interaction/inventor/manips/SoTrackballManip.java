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
 |      This file defines the trackball manipulator class.
 |      It uses a SoTrackballDragger to edit the fields of a transformManip
 |
 |   Author(s): Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.manips;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.interaction.inventor.draggers.SoTrackballDragger;
import jscenegraph.interaction.inventor.nodes.SoSurroundScale;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Transform node with 3D interface for changing rotation and scaling.
/*!
\class SoTrackballManip
\ingroup Manips
SoTrackballManip 
is derived from SoTransform (by way of SoTransformManip). 
When its fields 
change, nodes following it in the scene graph rotate, scale, and/or translate.


As a subclass of SoTransformManip, this manipulator
also has a 3D interface to edit some of its fields.
In this case, the interface edits the \b rotation  and \b scaleFactor  fields.


A manipulator differs from a dragger. When you move a dragger,
no other nodes are affected.  When you move an SoTransformManip,
other nodes move along with it.
(See the reference page for SoTransformManip).


The interface for an SoTrackballManip is exactly the same as that
of the SoTrackballDragger.  
To find out more about the interface, see the reference page 
for SoTrackballDragger.  To find out how the manipulator uses a 
dragger to provide its interface, see the reference page for 
SoTransformManip.


On screen, this manipulator will surround the objects influenced by its
motion.  This is because it turns on the <em>surroundScale</em> part of the
dragger (See the reference page for SoSurroundScale)

\par File Format/Default
\par
\code
TrackballManip {
  translation 0 0 0
  rotation 0 0 1 0
  scaleFactor 1 1 1
  scaleOrientation 0 0 1 0
  center 0 0 0
}
\endcode

\par See Also
\par
SoCenterballManip, SoHandleBoxManip, SoJackManip, SoTabBoxManip, SoTrackballDragger, SoTransformBoxManip, SoTransformerManip, SoTransform, SoTransformManip
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTrackballManip extends SoTransformManip {


	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTrackballManip.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTrackballManip.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTrackballManip.class); }    	  	
	


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
public SoTrackballManip()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTrackballManip.class*/);
    isBuiltIn = true;

    SoTrackballDragger d = new SoTrackballDragger();
    setDragger(d);
    SoSurroundScale ss = (SoSurroundScale ) d.getPart("surroundScale",true);
    ss.numNodesUpToContainer.setValue(4);
    ss.numNodesUpToReset.setValue(3);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
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
//    initialize the class
//
// Use: public, internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTrackballManip.class, "TrackballManip", SoTransformManip.class);
}

}
