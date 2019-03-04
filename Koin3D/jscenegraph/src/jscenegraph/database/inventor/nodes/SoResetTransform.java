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
 |      This file defines the SoResetTransform node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLModelMatrixElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBitMask;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node that resets the current transformation to identity.
/*!
\class SoResetTransform
\ingroup Nodes
This node resets the current transformation to identity. It can be
used to apply an absolute world space transformation afterwards, such
as translating to a specific point from within a hierarchy.
An SoResetTransform node should probably be used under an
SoSeparator or SoTransformSeparator so it won't change
transformations for the rest of the scene graph.
An SoResetTransform node can also be used to reset the current
bounding box to empty during traversal of an
SoGetBoundingBoxAction, if the \b whatToReset  field has the
\b BBOX  bit set.

\par File Format/Default
\par
\code
ResetTransform {
whatToReset TRANSFORM
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> If specified, resets current transformation matrix to identity. 
\par
SoGetBoundingBoxAction
<BR> If specified, resets current transformation matrix to identity and current computed bounding box to be empty. 
\par
SoGetMatrixAction
<BR> Returns identity matrix. 

\par See Also
\par
SoTransform
*/
////////////////////////////////////////////////////////////////////////////////

public class SoResetTransform extends SoTransformation {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoResetTransform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoResetTransform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoResetTransform.class); }    	  	
	
	

    //! Which things get reset:
	public enum ResetType {
        TRANSFORM       (0x01),                 //!< Transformation
        BBOX            (0x02);                  //!< Bounding box
		private int value;
		ResetType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
    };

    //! \name Fields
    //@{

    //! Specifies which items to reset when the node is traversed.
    public final SoSFBitMask         whatToReset = new SoSFBitMask();    


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoResetTransform()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoResetTransform*/);
	nodeHeader.SO_NODE_ADD_SFIELD(whatToReset,"whatToReset", (ResetType.TRANSFORM.getValue()));

    // Set up static info for enumerated type field
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ResetType.TRANSFORM);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ResetType.BBOX);

    // Set up info in enumerated type field
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(whatToReset,"whatToReset", "ResetType");

    isBuiltIn = true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements most actions.
//
// Use: extender

public void
 doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // If has effect on transform
    if (! whatToReset.isIgnored() && (whatToReset.getValue() & ResetType.TRANSFORM.getValue())!=0)
        SoModelMatrixElement.makeIdentity(action.getState(), this);
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
     doAction(action);
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
    // If has effect on transform
    if (! whatToReset.isIgnored() && (whatToReset.getValue() & ResetType.TRANSFORM.getValue())!=0)
        SoGLModelMatrixElement.makeIdentity(action.getState(), this);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Changes transformation used when computing bounding box. Also
//    sets bounding box to empty if so specified.
//
// Use: extender

public void
 getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Do regular transform stuff
     doAction(action);

    // If supposed to reset bounding box to empty
    if ((whatToReset.getValue() & ResetType.BBOX.getValue())!=0) {
        action.getXfBoundingBox().makeEmpty();
        action.resetCenter();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: extender

protected void
 getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // If has effect on transform
    if (! whatToReset.isIgnored() && (whatToReset.getValue() & ResetType.TRANSFORM.getValue())!=0) {

        // Overwrite the current matrices with identity
        action.getMatrix().makeIdentity();
        action.getInverse().makeIdentity();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles pick action.
//
// Use: extender

protected void
 pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
     doAction(action);
}
    
    

////////////////////////////////////////////////////////////////////////
//
//Description:
//This initializes the SoResetTransform class.
//
//Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
SoSubNode.SO__NODE_INIT_CLASS(SoResetTransform.class, "ResetTransform", SoTransformation.class);
}

    
}
