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
 |      This file defines manipulators for transform nodes.
 |      they are subclassed from SoTransform.
 |
 |   Author(s): Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.manips;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.interaction.inventor.draggers.SoDragger;
import jscenegraph.interaction.inventor.nodes.SoSurroundScale;
import jscenegraph.nodekits.inventor.SoNodeKitPath;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Base class for all transform Nodes with built-in 3D user interfaces.
/*!
\class SoTransformManip
\ingroup Manips
SoTransformManip 
is the base class for all SoTransform nodes that have a built-in 
3D user interface.
Since it is derived from SoTransform, any changes to its
fields result in the rotation, scaling, and/or translation of nodes that
follow it in the scene graph.


Typically, you will want to replace a regular SoTransform with an
SoTransformManip (as when the user selects an object to be moved),
or vice versa (as when the object is deselected, and the motion interface
should go away).
Use the
replaceNode() method
to insert a manipulator into a scene graph, and the
replaceManip() method
to remove it when done.


Every subclass of SoTransformManip utilizes a dragger of some sort to 
provide a 3D interface. (This class does not have dragger; but all the 
subclasses do.)
However a manipulator differs from a dragger; it
influences other objects in the scene because, as an SoTransform,
it alters the state. 
The fields values and movement of a dragger, on the other hand, affect only 
the dragger itself.


Each SoTransformManip subclass utilizes its dragger by adding it
as a hidden child.  When an action is applied to the manipulator,
such as rendering or handling events, the manipulator first traverses the
dragger, and then the manipulator adds its transformation matrix to the state.
When you click-drag-release over the manipulator, it passes these events down
to the dragger, which moves as a result ("I can't <em>help</em> it, I'm a dragger!").


The manipulator maintains consistency between the fields
of the dragger and its own fields. Let's say you use the mouse to rotate
the <em>dragger</em>. Callbacks insure that the 
\b rotation  field of the <em>manipulator</em> will change by the same 
amount, resulting in the rotation of nodes which follow in the scene graph.
Similarly, if you set any of the SoTransformManip fields
the manipulator will
move the dragger accordingly. You can use this feature
to impose contraints on a manipulator:  If the user moves the manipulator so
that a field value becomes too large, you can set 
the field back to your desired maximum, and the whole thing will move back to 
where you specified.

 
Since each SoTransformManip uses a dragger to provide its interface,
you will generally be told to look at the dragger's reference page for 
details of how it moves and what the different parts are for.
The interface for the dragger and the manipulator will always be exactly the
same.
Usually, a SoTransformManip will surround the
objects that it influences (i.e., those that move along with it).  
This is because the manipulator 
turns on the <em>surroundScale</em> part of its dragger; so the 
dragger geometry expands to envelope the other objects (see the reference
page for SoSurroundScale).  


Because the dragger is a <em>hidden</em> child, you can see the dragger on screen
and interact with it, but the dragger 
does not show up when you write the manipulator to file. Also, any SoPath
will end at the manipulator. (See the Actions section of this reference page 
for a complete description of when the dragger is traversed).


If you want to get a pointer to the dragger
you can get it from the manipulator using the 
getDragger() method.  You will need to 
do this if you want to change the geometry of a manipulator, since the
geometry actually belongs to the dragger.

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoHandleEventAction, SoRayPickAction
<BR> First, traverses the dragger the way an SoGroup would. All draggers place themselves in space, but leave the current transformation unchanged when finished.  Then the SoTransformManip accumulates a transformation into the  current transformation just like its base class, SoTransform. 
\par
SoSearchAction
<BR> Searches just like an SoTransform.  Does not search the dragger, which is a hidden child. 
\par
SoWriteAction
<BR> Writes out just like an SoTransform. Does not write the dragger, which is a hidden child.   If you really need to write valuable information  about the dragger, such as customized geometry, you  can retrieve the dragger with the  getDragger() method and then write  it out separately. 

\par See Also
\par
SoDragger, SoTransform, SoCenterballManip, SoHandleBoxManip, SoJackManip, SoSurroundScale, SoTabBoxManip, SoTrackballManip, SoTransformBoxManip, SoTransformerManip
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTransformManip extends SoTransform {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTransformManip.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTransformManip.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTransformManip.class); }    	  	
	

	  protected SoFieldSensor rotateFieldSensor;
	  protected SoFieldSensor translFieldSensor;
	  protected SoFieldSensor scaleFieldSensor;
	  protected SoFieldSensor centerFieldSensor;
	  protected SoFieldSensor scaleOrientFieldSensor;

	  protected SoChildList children;
	  
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//  Constructor.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

// Default constructor.
public SoTransformManip()
{
    children = new SoChildList(this);

    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTransformManip.class*/);

    isBuiltIn = true;

    // Create the field sensors
    rotateFieldSensor = new SoFieldSensor((data,sensor)->SoTransformManip.fieldSensorCB(data,sensor), 
                                            this);
    translFieldSensor = new SoFieldSensor((data,sensor)->SoTransformManip.fieldSensorCB(data,sensor),
                                           this);
    scaleFieldSensor = new SoFieldSensor((data,sensor)->SoTransformManip.fieldSensorCB(data,sensor),
                                           this);
    centerFieldSensor = new SoFieldSensor((data,sensor)->SoTransformManip.fieldSensorCB(data,sensor),
                                           this);
    scaleOrientFieldSensor = new SoFieldSensor((data,sensor)->SoTransformManip.fieldSensorCB(data,sensor),
                                           this);

    rotateFieldSensor.setPriority(0);
    translFieldSensor.setPriority(0);
    scaleFieldSensor.setPriority(0);
    centerFieldSensor.setPriority(0);
    scaleOrientFieldSensor.setPriority(0);

    rotateFieldSensor.attach(rotation);
    translFieldSensor.attach(translation);
    scaleFieldSensor.attach(scaleFactor);
    centerFieldSensor.attach(center);
    scaleOrientFieldSensor.attach(scaleOrientation);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // Important to do this because dragger may have callbacks
    // to this node.
    setDragger(null);

    if (rotateFieldSensor != null)
        rotateFieldSensor.destructor();
    if (translFieldSensor != null)
        translFieldSensor.destructor();
    if (scaleFieldSensor != null)
        scaleFieldSensor.destructor();
    if (centerFieldSensor != null)
        centerFieldSensor.destructor();
    if (scaleOrientFieldSensor != null)
        scaleOrientFieldSensor.destructor();
    children.destructor();
    
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the dragger to be the given node...
//
public void setDragger( SoDragger newDragger )
//
////////////////////////////////////////////////////////////////////////
{
    SoDragger oldDragger = getDragger();
    if ( oldDragger != null) {
        oldDragger.removeValueChangedCallback((userData,dragger)->
                                SoTransformManip.valueChangedCB(userData,dragger),this);
        children.remove(0);
    }
        
    if (newDragger != null) {
        if (children.getLength() > 0)
            children.set(0, newDragger );
        else 
            children.append( newDragger );
        // Call the fieldSensorCB to transfer our values into the
        // new dragger.
        SoTransformManip.fieldSensorCB( this, null );
        newDragger.addValueChangedCallback((userData,dragger)->
                                SoTransformManip.valueChangedCB(userData,dragger),this);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the dragger to be the given node...
//
public SoDragger getDragger()
//
////////////////////////////////////////////////////////////////////////
{
    if ( children.getLength() > 0 ) {
        SoNode n = (children).operator_square_bracket(0);
        if (n.isOfType( SoDragger.getClassTypeId() ))
            return ( (SoDragger ) n );
//#ifdef DEBUG
        else {
            SoDebugError.post("SoTransformManip::getDragger",
                            "Child is not a dragger!");
        }
//#endif
    }

    return null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Replaces the tail of the path with this manipulator.
//
//    [1] Tail of fullpath must be correct type, or we return.
//    [2] If path has a nodekit, we try to use setPart() to insert manip.
//    otherwise:
//    [3] Path must be long enough, or we return without replacing.
//    [4] Second to last node must be a group, or we return without replacing.
//    [5] Copy values from node we are replacing into this manip
//    [6] Replace this manip as the child.
//    [7] Do not ref or unref anything. Assume that the user knows what 
//        they're doing.
//    [8] Do not fiddle with either node's field connections. Assume that the
//        user knows what they're doing.
//
static SoGetBoundingBoxAction kitBba = null;
static SoGetBoundingBoxAction bba = null;
public boolean replaceNode( SoPath p )
//
////////////////////////////////////////////////////////////////////////
{
    SoFullPath fullP = SoFullPath.cast( p);

    SoNode     fullPTail = fullP.getTail();
    if (fullPTail.isOfType(SoTransform.getClassTypeId()) == false ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceNode", 
            "End of path is not an SoTransform");
//#endif
        return false;
    }

    SoNode pTail = p.getTail();
    if ( pTail.isOfType(SoBaseKit.getClassTypeId())) {

        // Okay, we've got a nodekit here! Let's do this right...
        // If fullPTail is a part in the kit, then we've got to follow
        // protocol and let the kit set the part itself.
        SoBaseKit lastKit = (SoBaseKit ) (SoNodeKitPath.cast(p)).getTail();
        String partName = lastKit.getPartString(p);
        if ( partName != "" ) {
            SoTransform oldPart 
                = (SoTransform ) lastKit.getPart(partName, true); 
            if (oldPart != null) {
                oldPart.ref();
                lastKit.setPart(partName, this);

                // If the surroundScale exists, we must update it now.
                SoSurroundScale ss = (SoSurroundScale)SoBaseKit.SO_CHECK_PART( this.getDragger(), 
                                             "surroundScale", SoSurroundScale.class );
                if ( ss != null ) {
                    // If we have a surround scale node, invalidate it
                    // and force it to calculate its new matrix 
                    // by running a getMatrix action.  This must be done
                    // or 'transferFieldValues' will incorrectly set any
                    // internal matrices that depend on surroundScale.
                    // (SoCenterballManip is an example of a manip that
                    // requires this).
                    ss.invalidate();
                    if (kitBba == null)
                        kitBba = new SoGetBoundingBoxAction(new SbViewportRegion());
                    kitBba.apply(p);
                }

                transferFieldValues( oldPart, this );
                oldPart.unref();

                return true;
            }
            else {
                // Although the part's there, we couldn't get at it.
                // Some kind of problem going on
                return false;
            }
        }
        // If it's not a part, that means it's contained within a subgraph
        // underneath a part. For example, it's within the 'contents'
        // separator of an SoWrapperKit. In that case, the nodekit doesn't
        // care and we just continue into the rest of the method...
    }

    if ( fullP.getLength() < 2 ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceNode",
        "Path is too short!");
//#endif
        return false;
    }

    SoNode      parent = fullP.getNodeFromTail( 1 );
    if (parent.isOfType( SoGroup.getClassTypeId() ) == false ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceNode",
        "Parent node is not a group.!");
//#endif
        return false;
    }

    ref();

    // We need to ref fullPTail because it can't disappear here.
    // We've got to transfer its field values later on.
    fullPTail.ref();

    ((SoGroup )parent).replaceChild( fullPTail, this );

    // If the surroundScale exists, we must update it now.
    SoSurroundScale ss = (SoSurroundScale)SoBaseKit.SO_CHECK_PART( this.getDragger(), 
                                         "surroundScale", SoSurroundScale.class );
    if ( ss != null ) {
        // If we have a surround scale node, invalidate it
        // and force it to calculate its new matrix 
        // by running a getMatrix action.  This must be done
        // or 'transferFieldValues' will incorrectly set any
        // internal matrices that depend on surroundScale.
        // (SoCenterballManip is an example of a manip that
        // requires this).
        ss.invalidate();
        if (bba == null)
            bba = new SoGetBoundingBoxAction(new SbViewportRegion());
        bba.apply(p);
    }

    transferFieldValues( ((SoTransform )fullPTail), this );

    // Now that we've extracted the values from the fullPTail we
    // can unref it.
    fullPTail.unref();

    unrefNoDelete();
    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Replaces the tail of the path (which should be this manipulator)
//    with the given SoTransform node.
//
//    [1] Tail of fullpath must be this node, or we return.
//    [2] If path has a nodekit, we try to use setPart() to insert new node.
//    otherwise:
//    [3] Path must be long enough, or we return without replacing.
//    [4] Second to last node must be a group, or we return without replacing.
//    [5] Copy values from node we are replacing into this manip
//    [6] Replace this manip as the child.
//    [7] Do not ref or unref anything. Assume that the user knows what 
//        they're doing.
//    [8] Do not fiddle with either node's field connections. Assume that the
//        user knows what they're doing.
//
public boolean replaceManip( SoPath p, SoTransform newOne ) 
//
////////////////////////////////////////////////////////////////////////
{
    SoFullPath fullP = SoFullPath.cast(p);

    SoNode     fullPTail = fullP.getTail();
    if ((SoTransformManip )fullPTail != this ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceManip",
        "Child to replace is not this manip!");
//#endif
        return false;
    }

    SoNode pTail = p.getTail();
    if ( pTail.isOfType(SoBaseKit.getClassTypeId())) {

        // Okay, we've got a nodekit here! Let's do this right...
        // If fullPTail is a part in the kit, then we've got to follow
        // protocol and let the kit set the part itself.
        SoBaseKit lastKit = (SoBaseKit ) SoNodeKitPath.cast(p).getTail();
        String partName = lastKit.getPartString(p);
        if ( partName != "" ) {

            if (newOne == null)
                newOne = new SoTransform();
            newOne.ref();

            transferFieldValues( this, newOne );

            lastKit.setPart(partName, newOne);
            newOne.unrefNoDelete();
            return true;
        }
        // If it's not a part, that means it's contained within a subgraph
        // underneath a part. For example, it's within the 'contents'
        // separator of an SoWrapperKit. In that case, the nodekit doesn't
        // care and we just continue on through...
    }

    if ( fullP.getLength() < 2 ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceManip",
        "Path is too short!");
//#endif
        return false;
    }

    SoNode      parent = fullP.getNodeFromTail( 1 );
    if (parent.isOfType( SoGroup.getClassTypeId() ) == false ) {
//#ifdef DEBUG
        SoDebugError.post("SoTransformManip::replaceManip",
        "Parent node is not a group.!");
//#endif
        return false;
    }

    if (newOne == null)
        newOne = new SoTransform();
    newOne.ref();

    transferFieldValues( this, newOne );

    ((SoGroup )parent).replaceChild((SoTransformManip )this, newOne );

    newOne.unrefNoDelete();

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Redefines this to also copy the dragger.
//
// Use: protected, virtual

public void copyContents(final SoFieldContainer fromFC,
                               boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    // Do the usual stuff
    super.copyContents(fromFC, copyConnections);

    // Copy the dragger
    SoTransformManip origManip = (SoTransformManip ) fromFC;
    setDragger((SoDragger ) origManip.getDragger().copy(copyConnections));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the child list...
//
public SoChildList getChildren()
//
////////////////////////////////////////////////////////////////////////
{
    return children;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Transfers field values from one node to another...
//
public void transferFieldValues( final SoTransform from,
                                        SoTransform to )
//
////////////////////////////////////////////////////////////////////////
{
    SoTransformManip m = null;
    if (to.isOfType( SoTransformManip.getClassTypeId() ) )
        m = (SoTransformManip ) to;
    if (m != null) {
        // detach the field sensors.
        m.rotateFieldSensor.detach();
        m.translFieldSensor.detach();
        m.scaleFieldSensor.detach();
        m.centerFieldSensor.detach();
        m.scaleOrientFieldSensor.detach();
    }
    if (to.rotation.getValue().operator_not_equal(from.rotation.getValue()))
        to.rotation.setValue(               from.rotation.getValue());
    if (to.translation.getValue().operator_not_equal(from.translation.getValue()))
        to.translation.setValue(            from.translation.getValue());
    if (to.scaleFactor.getValue().operator_not_equal(from.scaleFactor.getValue()))
        to.scaleFactor.setValue(            from.scaleFactor.getValue());
    if (to.scaleOrientation.getValue().operator_not_equal(from.scaleOrientation.getValue()))
        to.scaleOrientation.setValue(       from.scaleOrientation.getValue());
    if (to.center.getValue().operator_not_equal(from.center.getValue()))
        to.center.setValue(            		from.center.getValue());
    if (m != null) {
        // call the callback, then reattach the field sensors.
        if (SoDebug.GetEnv("IV_DEBUG_TRANSFORM_MANIP_FIELDS") != null) { //TODO
//            fprintf(stderr,"before:\n");
//            SbVec3f t = m.translation.getValue();
//            SbVec3f s = m.scaleFactor.getValue();
//            SbVec3f c = m.center.getValue();
//            fprintf(stderr,"translation = %f %f %f\n", t[0], t[1], t[2]);
//            fprintf(stderr,"scale = %f %f %f\n", s[0], s[1], s[2]);
//            fprintf(stderr,"center = %f %f %f\n", c[0], c[1], c[2]);
        }
        SoTransformManip.fieldSensorCB( m, null );
        if (SoDebug.GetEnv("IV_DEBUG_TRANSFORM_MANIP_FIELDS") != null) { //TODO
//            fprintf(stderr,"after:\n");
//            SbVec3f t = m.translation.getValue();
//            SbVec3f s = m.scaleFactor.getValue();
//            SbVec3f c = m.center.getValue();
//            fprintf(stderr,"translation = %f %f %f\n", t[0], t[1], t[2]);
//            fprintf(stderr,"scale = %f %f %f\n", s[0], s[1], s[2]);
//            fprintf(stderr,"center = %f %f %f\n", c[0], c[1], c[2]);
        }
        m.rotateFieldSensor.attach( m.rotation);
        m.translFieldSensor.attach( m.translation);
        m.scaleFieldSensor.attach( m.scaleFactor );
        m.centerFieldSensor.attach(m.center);
        m.scaleOrientFieldSensor.attach( m.scaleOrientation );

    }
}

private void 
SoTransformManip_doAction( SoAction action )
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
    else
        children.traverse(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements get matrix action.
//
// Use: protected

public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Note: this is most unusual behavior!
    // 
    // What we have is a transform node that may sometimes find itself 
    // on a path. In the cases of NO_PATH and BELOW_PATH, we just return
    // the SoTransform matrix without traversing the kids. This is a cross
    // between SoGroup, which doesn't traverse, and SoTransform, which 
    // affects the matrix.
    // In the case of OFF_PATH, traverse the children first, like SoGroup,
    // then add the matrix, like SoTransform
    //
    // For IN_PATH, we do not want to affect the matrix, since its affect
    // occurs after the children are traversed, (at the 'back half' of 
    // traversal) and does not affect the nodes under the path.
    //
    switch (action.getPathCode(numIndices, indices)) {

        case NO_PATH:
            super.getMatrix(action);
            break;

        case IN_PATH:
            children.traverse(action, 0, indices[0][numIndices[0] - 1]);
            break;

        case BELOW_PATH:
        	super.getMatrix(action);
            break;

        case OFF_PATH:
            children.traverse(action);
            super.getMatrix(action);
            break;

    }
}


// These functions implement all actions for nodekits.
public void callback( SoCallbackAction action )
{ 
    SoTransformManip_doAction( action );
    super.callback(action);
}

public void getBoundingBox( SoGetBoundingBoxAction action )
{ 
    final SbVec3f     totalCenter = new SbVec3f(0,0,0);
    int         numCenters = 0;
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild;

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        lastChild = indices[0][numIndices[0] - 1];
    else
        lastChild = getNumChildren() - 1;

    // Traverse the children.
    for (int i = 0; i <= lastChild; i++) {
        children.traverse(action, i, i);
        if (action.isCenterSet()) {
            totalCenter.operator_plus_equal(action.getCenter());
            numCenters++;
            action.resetCenter();
        }
    }

    // Traverse this as a directional light
    super.getBoundingBox(action);
    if (action.isCenterSet()) {
        totalCenter.operator_plus_equal(action.getCenter());
        numCenters++;
        action.resetCenter();
    }

    // Now, set the center to be the average:
    if (numCenters != 0)
        action.setCenter(totalCenter.operator_div(numCenters), false);
}

public void 
GLRender( SoGLRenderAction action )
{
  SoTransformManip_doAction( action ); 
  super.GLRender(action);
}

public void 
handleEvent( SoHandleEventAction action )
{ 
  SoTransformManip_doAction( action ); 
  super.handleEvent(action);
}

public void pick( SoPickAction action )
{ 
  SoTransformManip_doAction( action ); 
  super.pick(action);
}

public void 
search( SoSearchAction action )
{ 
    // First see if the caller is searching for this
    super.search(action);
//MANIPS DONT TRAVERSE CHILDREN DURING SEARCH    SoTransformManip::doAction( action );
}

protected static void valueChangedCB( Object inManip, SoDragger inDragger )
{
    if (inDragger == null)
        return;

    SoTransformManip manip = (SoTransformManip ) inManip;

    final SbMatrix motMat = new SbMatrix(inDragger.getMotionMatrix());

    final SbVec3f trans = new SbVec3f(), scale = new SbVec3f();
    final SbRotation rot = new SbRotation(), scaleOrient = new SbRotation();
    final SbVec3f center = new SbVec3f(manip.center.getValue());

    // See if inDragger has a center field...
    SoField f;
    SoType fType = SoSFVec3f.getClassTypeId(SoSFVec3f.class);
    if ( (f = inDragger.getField("center"))!=null && f.isOfType( fType ) )
        center.copyFrom( ((SoSFVec3f )f).getValue());

    motMat.getTransform( trans, rot, scale, scaleOrient, center );

    // Disconnect the field sensors
    manip.rotateFieldSensor.detach();
    manip.translFieldSensor.detach();
    manip.scaleFieldSensor.detach();
    manip.centerFieldSensor.detach();
    manip.scaleOrientFieldSensor.detach();

    if ( manip.rotation.getValue().operator_not_equal(rot) )
        manip.rotation.setValue(rot);
    if ( manip.translation.getValue().operator_not_equal(trans) )
        manip.translation.setValue(trans);
    if ( manip.scaleFactor.getValue().operator_not_equal(scale) )
        manip.scaleFactor.setValue(scale);
    if ( manip.center.getValue().operator_not_equal(center) )
        manip.center.setValue(center);
    if ( manip.scaleOrientation.getValue().operator_not_equal(scaleOrient) )
        manip.scaleOrientation.setValue(scaleOrient);

    // Reconnect the field sensors
    manip.rotateFieldSensor.attach( (manip.rotation));
    manip.translFieldSensor.attach( (manip.translation));
    manip.scaleFieldSensor.attach( (manip.scaleFactor));
    manip.centerFieldSensor.attach( (manip.center));
    manip.scaleOrientFieldSensor.attach( (manip.scaleOrientation));
}

protected static void
fieldSensorCB( Object inManip, SoSensor sensor)
{
    SoTransformManip manip = (SoTransformManip ) inManip;
    SoDragger        dragger = manip.getDragger();

    if (dragger == null)
        return;

    final SbVec3f    trans       = new SbVec3f(manip.translation.getValue()); 
    final SbVec3f    scale       = new SbVec3f(manip.scaleFactor.getValue()); 
    final SbRotation rot         = new SbRotation(manip.rotation.getValue()); 
    final SbRotation scaleOrient = new SbRotation(manip.scaleOrientation.getValue()); 
    final SbVec3f    center      = new SbVec3f(manip.center.getValue()); 

    final SbMatrix newMat = new SbMatrix();
    newMat.setTransform( trans, rot, scale, scaleOrient, center );

    // We may be setting two different things at once-- a matrix and 
    // a center field. Temporarily disable valueChangedCBs on dragger, 
    // and call them when we are all done.
    boolean saveEnabled = dragger.enableValueChangedCallbacks(false);

        // If dragger has a center field, set it.
        SoField f;
        SoType  fType = SoSFVec3f.getClassTypeId(SoSFVec3f.class);
        if ( (f = dragger.getField( "center" ))!=null && f.isOfType( fType ) )
            ((SoSFVec3f )f).setValue(  manip.center.getValue() );

        // Set the motionMatrix.
        dragger.setMotionMatrix(newMat);

    // Re-enable value changed callbacks and call.
    dragger.enableValueChangedCallbacks( saveEnabled );
    dragger.valueChanged();
}
	  
  private int getNumChildren() { return (children.getLength()); }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//  Initializes the type ID for this manipulator node. This
//  should be called once after SoInteraction::init().
//
// Use: public
//
public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTransformManip.class, "TransformManip", SoTransform.class);
}

}
