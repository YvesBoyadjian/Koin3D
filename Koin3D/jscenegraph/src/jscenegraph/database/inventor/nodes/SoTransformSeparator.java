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
 |      This file defines the SoTransformSeparator node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
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
import jscenegraph.database.inventor.elements.SoBBoxModelMatrixElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Group node that saves and restores transformation state.
/*!
\class SoTransformSeparator
\ingroup Nodes
This group node is similar to the SoSeparator node in that it
saves state before traversing its children and restores it afterwards.
However, it saves only the current transformation; all other state is
left as is. This node can be useful for positioning a camera, since
the transformations to the camera will not affect the rest of the
scene, even through the camera will view the scene. Similarly, this
node can be used to isolate transformations to light sources or other
objects.

\par File Format/Default
\par
\code
TransformSeparator {
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoRayPickAction
<BR> Saves the current transformation state, traverses all children, and restores the previous transformation state. 

\par See Also
\par
SoResetTransform, SoTransformation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTransformSeparator extends SoGroup {
	
    //SO_NODE_HEADER(SoTransformSeparator);

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTransformSeparator.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoTransformSeparator.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoTransformSeparator.class); }              


   ////////////////////////////////////////////////////////////////////////
    //
    // Description:
    //    Constructor
    //
    // Use: public
    
   public SoTransformSeparator()
    //
    ////////////////////////////////////////////////////////////////////////
    {
	   super();	         
       nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTransformSeparator.class*/);               
        isBuiltIn = true;
    }
       
	// Constructor that takes approximate number of children. 
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Constructor that takes approximate number of children.
	    //
	    // Use: public
	    
	   public SoTransformSeparator(int nChildren)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
			super(nChildren);
			nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTransformSeparator.class*/);               
	        isBuiltIn = true;
	    }
	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    This initializes the SoTransformSeparator class.
	    //
	    // Use: internal
	    
	   public static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SO__NODE_INIT_CLASS(SoTransformSeparator.class, "TransformSeparator", SoGroup.class);
	    }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

//public SoTransformSeparator()
////
//////////////////////////////////////////////////////////////////////////
//{
//    SO_NODE_CONSTRUCTOR(SoTransformSeparator);
//    isBuiltIn = TRUE;
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor that takes approximate number of children.
//
// Use: public

//public SoTransformSeparator(int nChildren) : SoGroup(nChildren)
////
//////////////////////////////////////////////////////////////////////////
//{
//    SO_NODE_CONSTRUCTOR(SoTransformSeparator);
//    isBuiltIn = TRUE;
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Typical action traversal. Assumes the model matrix element is
//    enabled.
//
// Use: extender

private void
SoTransformSeparator_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    // Remember the current matrix:
    final SbMatrix    saveMat = new SbMatrix(SoModelMatrixElement.pushMatrix(state));

    // Do regular group stuff
    SoGroup_doAction(action);

    // Set the matrix back to original value...
    SoModelMatrixElement.popMatrix(state, saveMat);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for callback action.
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTransformSeparator_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for GL rendering
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTransformSeparator_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for picking
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTransformSeparator_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for get bounding box
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    // Remember the current matrices:
    final SbMatrix matrix = new SbMatrix(), localmatrix = new SbMatrix();
    SoBBoxModelMatrixElement.pushMatrix(state, matrix, localmatrix);

    // Do regular group stuff
    super.getBoundingBox(action);

    // Set the matrices back to original value...
    SoBBoxModelMatrixElement.popMatrix(state, matrix, localmatrix);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements getMatrix action.
//
// Use: extender

public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Only need to compute matrix if transformSeparator is a node in middle of
    // current path chain. We don't need to push or pop the state,
    // since this shouldn't have any effect on other nodes being
    // traversed.

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
}
	   
}
