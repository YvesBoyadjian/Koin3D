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
 |      This file defines the SoArray node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoSwitchElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFShort;
import jscenegraph.database.inventor.fields.SoSFVec3f;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Group node that creates a regular IxJxK array of copies of its children.
/*!
\class SoArray
\ingroup Nodes
This group node traverses its children, in order, several times,
creating a regular 3D array of copies of them. The number of copies
in each of the three directions is specified by fields, as are the
vectors used to separate the copies in each of the three dimensions.


For example, an SoArray node can be used to create a 2x3x4 array
of copies of its children, where the separation vectors between
adjacent copies in the three array dimensions are (1,2,3), (-4,-5,-6),
and (7,8,9), respectively. The base point of the array can be set to
one of several values, as described in the origin field.


Copies are traversed so that the first dimension cycles most quickly,
followed by the second, and then the third. This order is important
because SoArray sets the current switch value to N before
traversing the children for the Nth time (for use with inherited
switch values - see SoSwitch).

\par File Format/Default
\par
\code
Array {
  numElements1 1
  numElements2 1
  numElements3 1
  separation1 1 0 0
  separation2 0 1 0
  separation3 0 0 1
  origin FIRST
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Traverses all children for each array element, saving and restoring state before and after each traversal. 
\par
SoSearchAction
<BR> Traverses all children once, setting the inherited switch value to SO_SWITCH_ALL first. 

\par See Also
\par
SoMultipleCopy, SoSwitch
*/
////////////////////////////////////////////////////////////////////////////////

public class SoArray extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoArray.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoArray.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoArray.class); }    	  	
	
	  
	      //! \name Fields
    //@{
    //! Number of elements in 1st direction
    public final SoSFShort           numElements1 = new SoSFShort();   
    //! Number of elements in 2nd direction
    public final SoSFShort           numElements2 = new SoSFShort();   
    //! Number of elements in 3rd direction
    public final SoSFShort           numElements3 = new SoSFShort();   

    //@}

    public final SoSFVec3f           separation1 = new SoSFVec3f();    //!< Separation vector in 1st direction
    public final SoSFVec3f           separation2 = new SoSFVec3f();    //!< Separation vector in 2nd direction
    public final SoSFVec3f           separation3 = new SoSFVec3f();    //!< Separation vector in 3rd direction

    //! Array origin:
    enum Origin {
        FIRST,                          //!< Origin at first element
        CENTER,                         //!< Origin at center of elements
        LAST;                            //!< Origin at last element
    	
    	public int getValue() {
    		return ordinal();
    	}
    };

    public final SoSFEnum            origin = new SoSFEnum();         //!< Base point
	
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoArray()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoArray.class*/);

	nodeHeader.SO_NODE_ADD_FIELD(numElements1,"numElements1",     ((short)1));
	nodeHeader.SO_NODE_ADD_FIELD(numElements2,"numElements2",     ((short)1));
	nodeHeader.SO_NODE_ADD_FIELD(numElements3,"numElements3",     ((short)1));
	nodeHeader.SO_NODE_ADD_FIELD(separation1,"separation1",      (new SbVec3f(1.0f, 0.0f, 0.0f)));
	nodeHeader.SO_NODE_ADD_FIELD(separation2,"separation2",      (new SbVec3f(0.0f, 1.0f, 0.0f)));
	nodeHeader.SO_NODE_ADD_FIELD(separation3,"separation3",      (new SbVec3f(0.0f, 0.0f, 1.0f)));
	nodeHeader.SO_NODE_ADD_FIELD(origin,"origin",           (Origin.FIRST.getValue()));

    // Set up static info for enumerated type field
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Origin.FIRST);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Origin.CENTER);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Origin.LAST);

    // Set up info in enumerated type field
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(origin,"origin", "Origin");

    isBuiltIn = true;
}

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
//    Overrides method in SoNode to return false.
//
// Use: public

public boolean
affectsState() 
//
////////////////////////////////////////////////////////////////////////
{
    return false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements typical action traversal.
//
// Use: extender

private void
SoArray_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild = -1; // java port
    int         n1, n2, n3, i1, i2, i3, curIndex;
    final SbVec3f     sepVec1 = new SbVec3f(), sepVec2 = new SbVec3f(), sepVec3 = new SbVec3f();
    boolean        translateArray, gettingBBox;

    final SbVec3f     totalCenter = new SbVec3f(0,0,0);             // For bbox
    int         numCenters = 0, i;              // For bbox

    // We have to do some extra stuff here for computing bboxes
    gettingBBox = action.isOfType(SoGetBoundingBoxAction.getClassTypeId());

    // Determine which children to traverse, if any
    switch (action.getPathCode(numIndices, indices)) {
      case NO_PATH:
      case BELOW_PATH:
        lastChild = getNumChildren() - 1;
        break;

      case IN_PATH:
        // If this node is in a path, that means the path goes to one
        // of its children. There's no need to traverse this path more
        // than once in this case.
        lastChild = indices[0][numIndices[0] - 1];
        action.getState().push();
        children.traverse(action, 0, lastChild);
        action.getState().pop();
        return;

      case OFF_PATH:
        // This differs from SoGroup: if the array is not on the
        // path, don't bother traversing its children. Effectively the
        // same as a separator to the rest of the graph.
        return;
    }

    n1 = numElements1.getValue();
    n2 = numElements2.getValue();
    n3 = numElements3.getValue();

    translateArray = (! origin.isIgnored() && origin.getValue() != Origin.FIRST.getValue());

    if (translateArray) {
        final SbVec3f vecToCenter = (separation1.getValue().operator_mul(n1 - 1).operator_add(
                                separation2.getValue().operator_mul(n2 - 1)).operator_add(
                                separation3.getValue().operator_mul(n3 - 1))).operator_minus();

        if (origin.getValue() == Origin.CENTER.getValue())
            vecToCenter.operator_mul_equal( 0.5f);

        action.getState().push();

        // Use model matrix to translate the array to the correct place
        SoModelMatrixElement.translateBy(action.getState(),
                                          this, vecToCenter);
    }

    curIndex = 0;
    sepVec3.setValue(0.0f, 0.0f, 0.0f);
    for (i3 = 0; i3 < n3; i3++) {

        sepVec2.copyFrom( sepVec3);
        for (i2 = 0; i2 < n2; i2++) {

            sepVec1.copyFrom( sepVec2);
            for (i1 = 0; i1 < n1; i1++) {

                action.getState().push();

                // Set value in switch element to current index
                SoSwitchElement.set(action.getState(), curIndex++);

                // Translate element to correct place
                SoModelMatrixElement.translateBy(action.getState(),
                                                  this, sepVec1);

                // Set the center correctly after each child
                if (gettingBBox) {
                    SoGetBoundingBoxAction bba =
                        (SoGetBoundingBoxAction ) action;

                    for (i = 0; i <= lastChild; i++) {
                        children.traverse(action, i, i);
                        if (bba.isCenterSet()) {
                            totalCenter.operator_add_equal(bba.getCenter());
                            numCenters++;
                            bba.resetCenter();
                        }
                    }
                }

                else
                    children.traverse(action, 0, lastChild);

                action.getState().pop();

                sepVec1.operator_add_equal( separation1.getValue());
            }
            sepVec2.operator_add_equal( separation2.getValue());
        }
        sepVec3.operator_add_equal( separation3.getValue());
    }

    // Restore state if it was pushed because of centering translation
    if (translateArray)
        action.getState().pop();

    if (gettingBBox && numCenters > 0)
        ((SoGetBoundingBoxAction ) action).setCenter(totalCenter.operator_div(numCenters),
                                                       false);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for callback action.
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoArray_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for GL rendering
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoArray_doAction(action);
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
    // Disable pick culling. If it is enabled, this is what happens:
    // if one of our children is a separator, we traverse it N times
    // to do the pick. For each of those times, the separator (if it
    // does pick culling) applies an SoGetBoundingBoxAction to the
    // current path (which goes through this array). The array then
    // computes the bbox of all N elements, so we get NxN behavior,
    // which takes too long.

    boolean saveCullingFlag = action.isCullingEnabled();

    action.enableCulling(false);

    SoArray_doAction(action);

    action.enableCulling(saveCullingFlag);
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
    // We can't do any tricks that assume that the extents of the
    // bounding box of the array are formed by the corner elements,
    // since some child may do something a little weird, such as using
    // a switch to translate one element to an extreme position. We'll
    // just do the standard multi-element traversal.

    SoArray_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for handle event
//
// Use: extender

public void
handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Again, as in getBoundingBox(), someone could use a switch to
    // determine whether to handle an event. So we need to traverse
    // all of our children multiple times. But the matrix elements are
    // not enabled for this action, so we don't want to do any translation.

    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild = -1; //java port
    int         n1, n2, n3, i1, i2, i3, curIndex;

    // Determine which children to traverse, if any
    switch (action.getPathCode(numIndices, indices)) {
      case NO_PATH:
      case BELOW_PATH:
        lastChild = getNumChildren() - 1;
        break;

      case IN_PATH:
        lastChild = indices[0][numIndices[0] - 1];
        break;

      case OFF_PATH:
        return;
    }

    n1 = numElements1.getValue();
    n2 = numElements2.getValue();
    n3 = numElements3.getValue();

    curIndex = 0;

    for (i3 = 0; i3 < n3; i3++) {
        for (i2 = 0; i2 < n2; i2++) {
            for (i1 = 0; i1 < n1; i1++) {
                action.getState().push();
                SoSwitchElement.set(action.getState(), curIndex++);
                children.traverse(action, 0, lastChild);
                action.getState().pop();
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements getMatrix action.
//
// Use: extender

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Only need to compute matrix if array is a node in middle of
    // current path chain. We don't need to push or pop the state,
    // since this shouldn't have any effect on other nodes being
    // traversed.

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH) {

        // Translate entire array if necessary
        if (! origin.isIgnored() && origin.getValue() != Origin.FIRST.getValue()) {

            int n1 = numElements1.getValue();
            int n2 = numElements2.getValue();
            int n3 = numElements3.getValue();

            final SbVec3f vecToCenter = (separation1.getValue().operator_mul(n1 - 1).operator_add(
                                    separation2.getValue().operator_mul(n2 - 1)).operator_add(
                                    separation3.getValue().operator_mul(n3 - 1))).operator_minus();

            if (origin.getValue() == Origin.CENTER.getValue())
                vecToCenter.operator_mul_equal( 0.5f);

            // Translate the matrices in the action
            final SbMatrix m = new SbMatrix();
            m.setTranslate(vecToCenter);
            action.getMatrix().multLeft(m);
            m.setTranslate(vecToCenter.operator_minus());
            action.getInverse().multRight(m);
        }

        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements search action. If we are not searching all the
//    children we have to set the switch element correctly in case any
//    child contains a switch node that inherits the switch value. The
//    best approximation we can make here is to set it to traverse all
//    children. This is preferable to missing children.
//
// Use: extender

public void
search(SoSearchAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild = -1; //java port

    // First see if the caller is searching for this node
    SoNode_search(action);

    if (action.isFound())
        return;

    // See if we're supposed to search only if the stuff under the
    // array is relevant to the search path

    switch (action.getPathCode(numIndices, indices)) {

      case NO_PATH:
      case BELOW_PATH:
        lastChild = getNumChildren() - 1;
        break;

      case IN_PATH:
        lastChild = indices[0][numIndices[0] - 1];
        break;

      case OFF_PATH:
        if (! action.isSearchingAll())
            return;
        lastChild = getNumChildren() - 1;
        break;
    }

    action.getState().push();

    // Set value in switch element to traverse all children
    SoSwitchElement.set(action.getState(), SoSwitch.SO_SWITCH_ALL);

    children.traverse(action, 0, lastChild);

    action.getState().pop();
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoArray class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoArray.class, "Array", SoGroup.class);
}

}
