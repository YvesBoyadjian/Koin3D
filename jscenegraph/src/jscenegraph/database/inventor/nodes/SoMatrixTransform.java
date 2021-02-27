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
 |      This file defines the SoMatrixTransform node class.
 |
 |   Author(s)          : Paul S. Strauss, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFMatrix;


////////////////////////////////////////////////////////////////////////////////
//! Node that specifies a 3D geometric transformation as a matrix.
/*!
\class SoMatrixTransform
\ingroup Nodes
This node defines a geometric 3D transformation with a single
SbMatrix. Note that some matrices (such as singular ones) may
result in errors in bounding boxes, picking, and lighting.

\par File Format/Default
\par
\code
MatrixTransform {
  matrix 1 0 0 0
0 1 0 0
0 0 1 0
0 0 0 1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Concatenates matrix given in the \b matrix  field with the current transformation matrix. 
\par
SoGetMatrixAction
<BR> Returns transformation matrix specified in the \b matrix  field. 

\par See Also
\par
SoTransform, SoMultipleCopy
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMatrixTransform extends SoTransformation {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoMatrixTransform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoMatrixTransform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoMatrixTransform.class); }    	  	
	


	  public final SoSFMatrix matrix = new SoSFMatrix();
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoMatrixTransform class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoMatrixTransform.class, "MatrixTransform", SoTransformation.class);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoMatrixTransform()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoMatrixTransform*/);
	nodeHeader.SO_NODE_ADD_SFIELD(matrix,"matrix", (SbMatrix.identity()));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements most actions.
//
// Use: extender

public void
SoMatrixTransform_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! matrix.isIgnored() && ! matrix.isDefault())
        SoModelMatrixElement.mult(action.getState(), this,
                                   matrix.getValue());
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
    SoMatrixTransform_doAction(action);
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
    SoMatrixTransform_doAction(action);
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
    SoMatrixTransform_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: private

protected void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! matrix.isIgnored() && ! matrix.isDefault()) {
        final SbMatrix        ctm = action.getMatrix();
        final SbMatrix        inv = action.getInverse();
        final SbMatrix        m = new SbMatrix(), mInv = new SbMatrix();

        m.copyFrom(matrix.getValue());
        ctm.multLeft(m);
        mInv.copyFrom(m.inverse());
        inv.multRight(mInv);
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
    SoMatrixTransform_doAction(action);
}

}
