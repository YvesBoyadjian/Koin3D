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
 |      This file defines the SoRotation node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFRotation;


////////////////////////////////////////////////////////////////////////////////
//! Node representing a 3D rotation about an arbitrary axis.
/*!
\class SoRotation
\ingroup Nodes
This node defines a 3D rotation about an arbitrary axis through the
origin. The rotation is accumulated into the current transformation,
which is applied to subsequent shapes.  The \b rotation  field
provides a variety of methods for specifying the rotation.

\par File Format/Default
\par
\code
Rotation {
  rotation 0 0 1 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Accumulates rotation transformation into the current transformation. 
\par
SoGetMatrixAction
<BR> Returns the matrix corresponding to the rotation. 

\par See Also
\par
SoRotationXYZ, SoTransform
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoRotation extends SoTransformation {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoRotation.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoRotation.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoRotation.class); }    	  	
	
	

	public final SoSFRotation rotation = new SoSFRotation();
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoRotation()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoRotation*/);
	nodeHeader.SO_NODE_ADD_SFIELD(rotation,"rotation", new SbRotation(0.0f, 0.0f, 0.0f, 1.0f));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements most actions.
//
// Use: extender

public void
SoRotation_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! rotation.isIgnored() && ! rotation.isDefault())
        SoModelMatrixElement.rotateBy(action.getState(), this,
                                       rotation.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoRotation_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles GL render action
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoRotation_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles get bounding box action
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoRotation_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: extender

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! rotation.isIgnored() && ! rotation.isDefault()) {
        final SbRotation      rot  = new SbRotation(rotation.getValue());
        final SbMatrix        ctm = action.getMatrix();
        final SbMatrix        inv = action.getInverse();
        final SbMatrix        m = new SbMatrix();

        rot.getValue(m);
        ctm.multLeft(m);
        rot.invert();
        rot.getValue(m);
        inv.multRight(m);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles pick action.
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoRotation_doAction(action);
}
	////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoRotation class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoRotation.class, "Rotation", SoTransformation.class);
}

	
}
