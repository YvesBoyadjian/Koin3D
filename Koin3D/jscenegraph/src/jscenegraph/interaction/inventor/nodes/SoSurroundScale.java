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
 |      This file defines the SoSurroundScale node class.
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.nodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransformation;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Transformation node that adjusts the current matrix so a default cube will surround other objects.
/*!
\class SoSurroundScale
\ingroup Nodes
When traversed by an action, this node
appends a transformation to the current transformation matrix
so that a default size  cube will surround the objects specified 
by its fields. Transform manipulators, such as SoHandleBoxManip,
use these nodes to make themselves surround other objects.

 
This node only recalculates after the 
invalidate() method has been
called. Otherwise it uses a saved scale and translation.


When calculating what to surround, the SoSurroundScale
looks at the current path in
the action and at its own field values.  
Then SoSurroundScale applies an SoGetBoundingBoxAction 
to the node that is
\b numNodesUpToContainer  nodes above it on the path.
SoSurroundScale also tells the action to reset
the bounding box upon traversal of the
node located \b numNodesUpToReset  nodes above it in the path.
The SoSurroundScale then appends a translation and scale
to the current transformation
so that a default size SoCube will translate and scale to fit this
bounding box.


For example, when an SoHandleBoxManip wants to surround the objects it
is going to move, the scene graph will look something like this:
\code
(Cr
                 RootNode
       -------------------------
       |                        |
     handleBoxManip        movingStuff
       |
     handleBoxDragger
       |
     separator
      -----------------------------------
      |            |                     |
    motionMatrix  surroundScale      cubeGeom
The SoHandleBoxDragger wants to transform the cubeGeom so that it
surrounds the movingStuff. So it sets the surroundScale fields to:
     numNodesUpToContainer = 4;
     numNodesUpToReset = 3;
\endcode
The SoBoundingBoxAction will then be applied to RootNode, 
with a reset after traversing the SoHandleBoxManip.
So the SoSurroundScale will surround the objects below separator, and
to the right of handleBoxManip, producing the desired effect.

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Accumulates scaling and translation transformations into  the current transformation. 
\par
SoGetMatrixAction
<BR> Returns the matrix corresponding to the scaling and translation. 

\par See Also
\par
SoTransformation, SoTransformManip, SoCenterballDragger, SoCenterballManip, SoHandleBoxDragger, SoHandleBoxManip, SoJackDragger, SoJackManip, SoTabBoxDragger, SoTabBoxManip, SoTrackballDragger, SoTrackballManip, SoTransformBoxDragger, SoTransformBoxManip
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSurroundScale extends SoTransformation { //TODO

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSurroundScale.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSurroundScale.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSurroundScale.class); }    	  	
	
    //! \name Fields
    //@{

    //! When traversed by an action, if surroundScale needs to 
    //! calculate a new box, surroundScale
    //! looks at the current path in
    //! the action. It travels up this path a distance of \b numNodesUpToContainer  
    //! and  applies an SoGetBoundingBoxAction to the node that it finds
    //! there.
	  public final SoSFInt32           numNodesUpToContainer = new SoSFInt32();

    //! Before applying the SoGetBoundingBoxAction (see the 
    //! \b numNodesUpToContainer  field aove) the surroundScale node
    //! travels up the path a distance of \b numNodesUpToReset  
    //! and tells the action to reset
    //! the bounding box upon traversal of that node.
	  public final SoSFInt32           numNodesUpToReset = new SoSFInt32();

    //@}
	  
	  protected final SbVec3f    cachedScale = new SbVec3f();
	    protected final SbVec3f    cachedInvScale = new SbVec3f();
	    protected final SbVec3f    cachedTranslation = new SbVec3f();
	    protected boolean       cacheOK;

	    protected boolean doTranslations;

	    private
	        boolean ignoreInBbox;

public SoSurroundScale()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoSurroundScale.class*/);

    isBuiltIn = true;

    nodeHeader.SO_NODE_ADD_FIELD(numNodesUpToContainer,"numNodesUpToContainer",     (0) );
    nodeHeader.SO_NODE_ADD_FIELD(numNodesUpToReset,"numNodesUpToReset",     (0) );

    ignoreInBbox = false;
    doTranslations = true;

    // These variables are used to assist in making the node more efficient
    // by caching the values given back from updateMySurroundParams.
    cachedScale.setValue(1,1,1);
    cachedInvScale.setValue(1,1,1);
    cachedTranslation.setValue(0,0,0);
    cacheOK = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

protected    void setIgnoreInBbox( boolean newVal ) { ignoreInBbox = newVal; }
protected    boolean isIgnoreInBbox() { return ignoreInBbox; }


// Snaps numbers within epsilong of zero to zero
private float FUDGE(float x,float epsilon) {
if ((x >= 0.0) && (x < epsilon)) 
    x = epsilon; 
else if ((x < 0.0) && (x > -epsilon)) 
    x = -epsilon;
return x;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: protected

private static SoGetBoundingBoxAction boundingBoxAction = null;
protected void
updateMySurroundParams(SoAction action,
                                        final SbMatrix myInv )
//
////////////////////////////////////////////////////////////////////////
{
    final SoFullPath curPath = SoFullPath.cast(action.getCurPath());
    int curPathLength = curPath.getLength();

    // If the container node is out of range, just return.
        int numUpCon = (int) numNodesUpToContainer.getValue();
        if (   (numUpCon <= 0)    || (numUpCon > (curPathLength - 1))  ){
            cachedScale.setValue(1,1,1);
            cachedInvScale.setValue(1,1,1);
            cachedTranslation.setValue(0,0,0);
            cacheOK = false;
            return;
        }

    // CHECK TO SEE IF OUR CACHED VALUES ARE OKAY
    // IF SO, JUST RETURN 
        if (   cacheOK  ) 
            return;

    // Find the path to apply the bounding box action to. It should end
    // 'numUpCon' above this one.
        SoPath applyPath = curPath.copy(0, (curPathLength - numUpCon));
        applyPath.ref();

    // See if there is a node to do a reset at. If so, build a resetPath
        SoPath resetPath = null;
        int numUpReset = (int) numNodesUpToReset.getValue();

        if (numUpReset >= 0 && (numUpReset < numUpCon) ) {
            // Build a path ending at the reset node.
            resetPath = curPath.copy(0, curPathLength - numUpReset );
            resetPath.ref();
        }
        SoFullPath fullResetPath = SoFullPath.cast(resetPath);

    // Create a getBoundingBox action
    // Set the reset path if we have one.
    // Apply the bounding box action and find out how big the box was.
    // Temporarily set the ignoreInBbox flag TRUE, so we don't infinite loop!

        final SbViewportRegion vpRegion = new SbViewportRegion((short)0,(short)0);
        SoState state = action.getState();
        vpRegion.copyFrom( SoViewportRegionElement.get(state));

        if (boundingBoxAction == null)
            boundingBoxAction = new SoGetBoundingBoxAction(vpRegion);
        else
            boundingBoxAction.setViewportRegion(vpRegion);

        if (fullResetPath!= null)
            boundingBoxAction.setResetPath( fullResetPath, false,
                                                SoGetBoundingBoxAction.ResetType.BBOX);

        boolean oldFlag = isIgnoreInBbox();
        setIgnoreInBbox( true );
        boundingBoxAction.apply( applyPath );
        setIgnoreInBbox( oldFlag );
        SbXfBox3f myXfBox = boundingBoxAction.getXfBoundingBox();

        // Transform the box into our local space, then project it.
        myXfBox.transform( myInv );
        SbBox3f myBox = new SbBox3f(myXfBox.project());

    // Get the scale for this node to add to the ctm.
        if (myBox.isEmpty()) {
            cachedScale.setValue(1,1,1);
            cachedInvScale.setValue(1,1,1);
            cachedTranslation.setValue(0,0,0);
            cacheOK = true;
            return;
        }
        else {
            float[] xyz = new float[3];
            myBox.getSize(xyz);
            cachedScale.setValue(  .5f*xyz[0], .5f*xyz[1], .5f*xyz[2] );

            float minLength = .01f * cachedScale.length();
            // Macro defined just before beginning of this method.
            cachedScale.getValue()[0] = FUDGE(cachedScale.getValue()[0],minLength);
            cachedScale.getValue()[1] = FUDGE(cachedScale.getValue()[1],minLength);
            cachedScale.getValue()[2] = FUDGE(cachedScale.getValue()[2],minLength);

            // Find the inverse values
            for (int j = 0; j < 3; j++ )
                cachedInvScale.getValue()[j] = 1.0f / cachedScale.getValue()[j];
        }

    // Get the translation for this node to add to the ctm.
        // This will get the cube centered about the bbox center.
        // If the bounding box is not centered at the origin, we have to
        // move the cube to the correct place. 
        if (doTranslations)
            cachedTranslation.copyFrom((   myBox.getMin().operator_add( myBox.getMax()) ).operator_mul(0.5f));
        else
            cachedTranslation.setValue(0,0,0);

    // Establish the cached values to save us some time later...
        cacheOK = true;

        if (resetPath != null)
            resetPath.unref();
        if (applyPath != null)
            applyPath.unref();
}

//#undef FUDGE

public void
callback( SoCallbackAction action )
{
    SoSurroundScale_doAction( action );
}

public void
GLRender( SoGLRenderAction action )
{
    SoSurroundScale_doAction( action );
}

public void
getBoundingBox( SoGetBoundingBoxAction action )
{
    if ( ignoreInBbox == false )
    	SoSurroundScale_doAction( action );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: protected

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
        SbMatrix        ctm = action.getMatrix();
        SbMatrix        inv = action.getInverse();
        final SbMatrix m = new SbMatrix();

    // Get my scale and its inverse
        if (cacheOK == false)
            updateMySurroundParams( action, inv );

    // Append the translation.
        if (doTranslations) {
            m.setTranslate( cachedTranslation );
            ctm.multLeft( m );
            m.setTranslate( cachedTranslation.operator_minus() );
            inv.multRight( m );
        }
        else
            cachedTranslation.setValue(0,0,0);

    // Append the scale.
        m.setScale( cachedScale );
        ctm.multLeft( m );
        m.setScale( cachedInvScale );
        inv.multRight( m );
}

public void
pick( SoPickAction action )
{
    SoSurroundScale_doAction( action );
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles any action derived from SoAction.
//
// Use: private

private void
SoSurroundScale_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState     state = action.getState();

    SbMatrix theCtm = SoModelMatrixElement.get(state);

    if (cacheOK == false )
        updateMySurroundParams( action, theCtm.inverse() );

    if (doTranslations)
        SoModelMatrixElement.translateBy(state, this, cachedTranslation );
    else
        cachedTranslation.setValue(0,0,0);

    SoModelMatrixElement.scaleBy(state, this, cachedScale );
}

	  

    //! If you call this, then next time an action is applied the node will re-calculate
    //! it's cached translation and scale values.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Sets the cacheOK flag to FALSE
//This means that the next action coming through this node will cause the
//scale and translation to be re-calculated.
//
//Use: public

    public void invalidate() {
        cacheOK = false;
        touch();
    }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoSurroundScale class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoSurroundScale.class, "SurroundScale", SoTransformation.class);
}

}
