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
 |      This file defines the base class for draggers that
 |      use a click-drag-release paradigm.
 |
 | NOTE TO DEVELOPERS:
 |     For info about the structure of SoDragger:
 |     [1] compile: /usr/share/src/Inventor/samples/ivNodeKitStructure
 |     [2] type:    ivNodeKitStructure SoDragger.
 |     [3] The program prints a diagram of the scene graph and a table with 
 |         information about each part.
 |
 |   Author(s): Paul Isaacs, David Mott, Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.draggers;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoAuditorList;
import jscenegraph.database.inventor.misc.SoCallbackList;
import jscenegraph.database.inventor.misc.SoCallbackListCB;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.misc.SoTempPath;
import jscenegraph.database.inventor.nodes.SoMatrixTransform;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.interaction.inventor.nodekits.SoInteractionKit;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;
import jscenegraph.nodekits.inventor.nodekits.SoNodekitCatalog;
import jscenegraph.nodekits.inventor.nodekits.SoSubKit;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Base class for nodekits that move in response to click-drag-release mouse events.
/*!
\class SoDragger
\ingroup Draggers
SoDragger 
is the base class for all nodekits you move by using the mouse to
click-drag-and-release. More specifically, 
they are operated by a start (mouse button 1 pressed over dragger to pick it),
followed by dragging (mouse motion events are interpreted by the dragger
and result in some form of motion and/or change to a field), 
followed by finish (mouse up).


Each dragger has a different paradigm for interpreting mouse motion and 
changing its fields as a result.
Draggers map 2D mouse motion into motion of a point on 3D lines, planes, 
spheres or cylinders.  (See the SbProjector reference pages.)
Then they react to this motion of a point through 3-space by scaling, 
translating, or rotating.
For example, SoTranslate2Dragger maps mouse motion onto a
3D plane, then translates to follow the cursor as it moves within that plane.


Every dragger has <em>fields</em> that describe its current state.
Scaling draggers have a \b scaleFactor  field,  rotational draggers have
a \b rotation  field, etc.
All draggers have the \b isActive  field, defined in this class.
It is true while the dragger is being dragged, false otherwise.


Draggers that have only one part to pick and one motion field are called
<em>simple draggers</em>. Examples are the SoRotateDiscDragger, 
SoScale1Dragger, and SoTranslate2Dragger.


Draggers that create assemblies out of other draggers and then orchestrate
the motion of the whole assembly are call <em>composite draggers</em>.
SoTransformBoxDragger is a composite dragger made entirely of simple
draggers.  SoDirectionalLightDragger contains both a simple dragger
(SoRotateSphericalDragger) and a composite dragger (SoDragPointDragger)
When using a composite dragger, the fields of the composite dragger
are the ones you should work with. Draggers
lower down in the assemblage usually have zeroed out values.
For example, when you drag the face
of a transformBox, an SoTranslate2Dragger, the transformBox "steals"
the translation from the child dragger and transfers it up to the top of the 
composite dragger, where it effects all pieces of the assemblage. 

 
Draggers always keep their fields up to date, including while they are
being dragged. So you can use field-to-field connections and engines to 
connect dragger values to other parts of your scene graph.  
Hence draggers can be easily utilized as input devices for 
mouse-driven 3D interface elements.
You can also register value-changed callbacks, which are called whenever any
of the fields is changed.


Also, if you set the field of a dragger through some method other than dragging,
(by calling setValue(), for example), 
the dragger's internal SoFieldSensor will sense this and the dragger
will move to satisfy that new value.  


This makes it easy to constrain draggers to keep their fields within certain 
limits: if the limit is exceeded, just set it back to the exceeded maximum or 
minimum.  You can do this even as the dragger is in use, by constraining the
field value within a value-changed callback that you add with
addValueChangedCallback().  In this
case, be sure to temporarily disable the other value-changed callbacks using
enableValueChangedCallbacks().
Doing this will prevent infinite-looping; changing the value followed
by calling the callbacks which change the value ad infinitum.


When you drag a dragger, the dragger only moves itself.
Draggers do not change the state or affect objects that follow in the scene
graph.
For example a dragger does not ever behave like an SoTransform and 
change the current transformation matrix.
Draggers are not transforms, even if they have field names like
translation, rotation, scaleFactor.  Many draggers, such 
as SoTrackballDragger, have a corresponding  SoTransformManip, in 
this case SoTrackballManip.  The manipulator is a subclass of SoTransform,
and affects other objects in the scene; it uses a trackball <em>dragger</em> to
provide its user interface.  
In this way, draggers are employed extensively by manipulators.
Callback functions on the dragger allow its employer to be
notified of start, motion, finish, and value changes.
In all cases, the callback function is passed a pointer to the
dragger which initiated the callback.
(See the various man pages for more details on specific draggers and 
manipulators).


All draggers are nodekits.
However, draggers do not list their parts in the Parts section of the
reference page. Instead, there is a section called Dragger Resources,
more suited to describe the parts made available to the programmer.
Because of space limitations, the Dragger Resources section only 
appears in the online versions of the reference pages. 
Each dragger has some parts you can pick on, and other parts that replace
them when they are <em>active</em> or moving.  These active parts are often
just the same geometry in another color.  
Draggers also have pieces for displaying feedback.
Each of these pieces has a default scene graph, as well as a special
function within the dragger.  Each part also has a <tt>resource name</tt>.
All this information is contained in the <tt>DRAGGER RESOURCES</tt> section.


Since draggers are nodekits, you can set the parts in any instance of a dragger using
setPart().


But draggers also give each part a <em>resource name</em>.
When a dragger builds a part, it looks in the global dictionary for the
node with that \b resourceName . By putting a new entry in the dictionary,
you can override that default.
The default part geometries are defined as resources for each class,
and each class has a file you can change to alter the defaults.
The files are listed in each dragger's man page.
You can make your program use different default resources for the parts
by copying the listed file from the directory
\b /usr/share/data/draggerDefaults 
into your own directory, editing the file, and then
setting the environment variable \b SO_DRAGGER_DIR  to be a path to that directory.

\par See Also
\par
SoInteractionKit, SoCenterballDragger, SoDirectionalLightDragger, SoDragPointDragger, SoHandleBoxDragger, SoJackDragger, SoPointLightDragger, SoRotateCylindricalDragger, SoRotateDiscDragger, SoRotateSphericalDragger, SoScale1Dragger, SoScale2Dragger, SoScale2UniformDragger, SoScaleUniformDragger, SoSpotLightDragger, SoTabBoxDragger, SoTabPlaneDragger, SoTrackballDragger, SoTransformBoxDragger, SoTransformerDragger, SoTranslate1Dragger, SoTranslate2Dragger
*/
////////////////////////////////////////////////////////////////////////////////

public class SoDragger extends SoInteractionKit {
	
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoDragger.class,this);
	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoDragger.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoDragger.class); }              

    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

	
    //SO_KIT_CATALOG_ENTRY_HEADER(motionMatrix);
	protected final SoSFNode motionMatrix = new SoSFNode();
	
    //! These control behavior of the rotational parts. If FRONT, no matter 
    //! where you hit the dragger relative to its center, it will behave as if 
    //! you are dragging from the front. If BACK, the rear. If USE_PICK,
    //! then it depends on the relationship between the picked point and the
    //! center of the dragger. 
    public enum ProjectorFrontSetting {
        FRONT, BACK, USE_PICK
    };
	

    //! This field is a boolean that enables / disables interaction
    public final SoSFBool enable = new SoSFBool();

    //! This field is a boolean that is true when the mouse is down and
    //! we are dragging, false otherwise.
    public final SoSFBool  isActive = new SoSFBool();

	
	  private	    SoCallbackList startCallbacks;
	  private	    SoCallbackList motionCallbacks;
	  private	    SoCallbackList finishCallbacks;
	  private	    SoCallbackList valueChangedCallbacks;
		   
	  private	    SoCallbackList otherEventCallbacks;
		   	
	  private   boolean    valueChangedCallbacksEnabled;
	  private SoDragger activeChildDragger;

	  private SoHandleEventAction handleEventAction; //!< current event action

    //! A record of the world space point which initiated the dragging.
    //! This might be set from a pickedPoint of a handleEventAction,
    //! or explicitly from a given point when a meta-key callback 
    //! changes gestures mid-stream and wants to select its own transitional
    //! starting point.
	  private final SbVec3f             startingWorldPoint = new SbVec3f();       

    //! Used by parent draggers to save initial matrix when motion begins. 
	  private final SbMatrix startMotionMatrix = new SbMatrix();

    //! Where the locater was when dragging was initiated
	  private final SbVec2s     startLocater = new SbVec2s();


    //! sets pick path and tempPathToThis
    private SoPath              pickPath;          //!< pick path to the dragger


    private final SbName surrogateNameInPickOwner = new SbName();
    private SoPath pathToSurrogatePickOwner;
    private SoPath surrogatePathInPickOwner;

    //! A tempPath leading to 'this.' It is used to calculate the
    //! motionMatrix. It is set when a dragger first grabs event, through the
    //! handleEvent action, or by setCameraInfo() if null at the time.
    //! A tempPath does not increase any ref counts, so keeping this path
    //! will not prevent this node from being deleted. However, we must be
    //! very careful before using it, since some nodes my get deleted out from
    //! under us. The variable tempPathNumKidsHack helps us fix up paths
    //! that have changed since the path was set.
    private SoTempPath          tempPathToThis;
    private SbPList             tempPathNumKidsHack;

    //! Minimum number of pixels to move before choosing a constraint
    //! based on the gesture.
    private int         minGesture;

    //! The smallest scale that any dragger will write. If the user attempts
    //! to go below this amount, the dragger will set it to this minimum.
    //! Default is .0001
    private static float        minScale = .001f;

    private final SbViewVolume        viewVolume = new SbViewVolume();         //!< view volume for xsection tests
    private final SbViewportRegion    vpRegion = new SbViewportRegion();           //!< view volume for xsection tests

    //! We keep these matrices and their inverses around
    //! to use when converting between spaces. The four
    //! pairs are cached; the pair becomes invalud when its
    //! valid flag is set to false.

    //! These matrices are ones that are commonly queried by
    //! subclasses. So, we cache them.
    //! Two things determine the localToWorld matrix. The pathToThis and
    //! the motionMatrix. If either value is invalid, we need to recalculate.
    //! But depending on which one it is, we either need to run a matrix
    //! action on the pick path or just on the motionMatrix.
    private boolean     cachedPathToThisValid;
    private boolean     cachedMotionMatrixValid;
    private final SbMatrix cachedMotionMatrix = new SbMatrix();
    //! PostMotion is the matrix space just AFTER the motion matrix. 
    private final SbMatrix postMotionToWorldMatrix = new SbMatrix();
    private final SbMatrix worldToPostMotionMatrix = new SbMatrix();
    //! PreMotion is the matrix space just BEFORE the motion matrix. 
    private final SbMatrix preMotionToWorldMatrix = new SbMatrix();  
    private final SbMatrix worldToPreMotionMatrix = new SbMatrix();

    //! Used so that draggers don't try to include themselves
    //! as part of their own sizing boxes. By default this is false.
    private boolean ignoreInBbox;

    //! Keeps track of whether the mouse has been moved since the mouse
    //! went down.
    private boolean mouseMovedYet;

    private ProjectorFrontSetting projectorFrontSetting;
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
public SoDragger()
//
////////////////////////////////////////////////////////////////////////
{
	super();
	kitHeader.SO_KIT_CONSTRUCTOR(SoDragger.class);

    isBuiltIn = true;

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(motionMatrix,"motionMatrix", SoMatrixTransform.class, false, 
                                "topSeparator", "geomSeparator",false);

    // initialize fields.
    kitHeader.SO_KIT_ADD_FIELD(isActive,"isActive",(false));
    kitHeader.SO_KIT_ADD_FIELD(enable,"enable", (true));

    SO_KIT_INIT_INSTANCE();

    // init local variables
    startingWorldPoint.copyFrom(new SbVec3f(0,0,0));

    // callback lists
    startCallbacks = new SoCallbackList();
    motionCallbacks = new SoCallbackList();
    finishCallbacks = new SoCallbackList();

    valueChangedCallbacks = new SoCallbackList();
    valueChangedCallbacksEnabled = true;
    activeChildDragger = null;

    otherEventCallbacks = new SoCallbackList();

    // initialize tempPathToThis, pickPath
    tempPathToThis = null;
    tempPathNumKidsHack = null;
    pickPath = null;

    // initialize surrogatePick info.
    surrogateNameInPickOwner.copyFrom("");
    pathToSurrogatePickOwner = null;
    surrogatePathInPickOwner = null;

    // The matrix cache starts as invalid
    cachedPathToThisValid = false;
    cachedMotionMatrixValid = false;

    // By default, do include this dragger in bounding box calculations.
    // This is temporarily overridden when the dragger itself applies
    // a bbox action so that it doesn't include itself.
    ignoreInBbox = false;

    // Minimum amount to move before choosing a constraint based
    // on the user's gesture.
    minGesture = 8; // pixels

    setHandleEventAction( null );
    setCameraInfo( null );

    // By default, projectors for rotation will call setFront() based
    // on where the mouse went down: on the front or rear of the virtual sphere
    // or cylinder.
    setFrontOnProjector( ProjectorFrontSetting.USE_PICK );
}

    public void setFrontOnProjector( ProjectorFrontSetting newVal )
        { projectorFrontSetting = newVal; }
    public ProjectorFrontSetting getFrontOnProjector()  
        { return projectorFrontSetting;}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if ( pickPath != null )
         pickPath.unref();

    // tempPaths actually get deleted, not unref'ed
    if ( tempPathToThis != null ) {
        tempPathToThis.destructor();
        tempPathToThis = null;
    }
    if ( tempPathNumKidsHack != null ) {
        tempPathNumKidsHack.destructor();
        tempPathNumKidsHack = null;
    }

    if ( activeChildDragger != null)
        activeChildDragger.unref();

    setNoPickedSurrogate();

    startCallbacks.destructor();
    motionCallbacks.destructor();
    finishCallbacks.destructor();

    valueChangedCallbacks.destructor();

    otherEventCallbacks.destructor();
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This attempts to handle the passed event. It will call methods on
// the SoHandleEventAction if it wants to grab and if it handles the event.
//
// Use: public
//
public void
handleEvent(SoHandleEventAction ha)
//
////////////////////////////////////////////////////////////////////////
{
    // let base class traverse the children
    if ( ha.getGrabber() != this ) 
        super.handleEvent( ha );

    // if no children handled the event, we will!
    if ( ha.isHandled() )
        return;

    setHandleEventAction( ha );

    // get event and window size from the action
    final SoEvent event = ha.getEvent();

    if (mouseButtonPressed(event)) {

        boolean happyPath = false;

        final SoPickedPoint pp = ha.getPickedPoint();
        SoPath pPath = ( pp != null ) ? pp.getPath() : null; 

        if ( pPath != null) {

            // Is our current traversal path part of the pickPath?
            if ( pPath.containsPath(ha.getCurPath())) {
                happyPath = true;
            }
            // Is the path a surrogate path for us or any draggers 
            // contained within us?
            else {
                final SoPath[] pathToOwner = new SoPath[1], surrogatePath = new SoPath[1];
                final SbName  surrogateName = new SbName();

                if (isPathSurrogateInMySubgraph( pPath, pathToOwner, 
                                        surrogateName, surrogatePath) ) {
                    pathToOwner[0].ref();
                    surrogatePath[0].ref();
                    if (shouldGrabBasedOnSurrogate( pPath, surrogatePath[0] )) {
                        setPickedSurrogate( pathToOwner[0], surrogateName, 
                                            surrogatePath[0] );
                        happyPath = true;
                    }
                    surrogatePath[0].unref();
                    pathToOwner[0].unref();
                }
            }
        }

        if ( happyPath ) {

            setStartingPoint( pp );
                
            // Since the pick path may be on a surrogate object, use
            // the current action path to get a path to this node.
            setTempPathToThis( ha.getCurPath() );
            setCameraInfo( ha );

            setPickPath( pPath );

            ha.setGrabber(this);
            ha.setHandled();
        }                   
        else 
            otherEventCallbacks.invokeCallbacks(this);
    }
    else if ( event.isOfType(SoLocation2Event.getClassTypeId() ) && 
              ha.getGrabber() == this ) {

            mouseMovedYet = true;
            motionCallbacks.invokeCallbacks(this);

            ha.setHandled();
    }
    else if (mouseButtonReleased(event) &&
              ha.getGrabber() == this ) {

            // Releasing the grabber will result in a call to 
            // grabEventsCleanup(), which will do some important things...
            ha.releaseGrabber();

            if ( mouseMovedYet == true ) {
                // If the mouse didn't move, then don't handle the event.
                // Let some other node make use of it (for example, the 
                // selection node might want to de-select what's inside
                // the dragger.
                ha.setHandled();
            }
    }
    else
        otherEventCallbacks.invokeCallbacks(this);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called during handleEvent. It checks if the left mouse button
//    is pressed. May be overwritten to implement other button masks.
//    Addd by cschumann 2015.08.24 to allow to overwrite button masks
//
// Use: public
//
public boolean
mouseButtonPressed(final SoEvent ev)
{
  return ( SoMouseButtonEvent.SO_MOUSE_PRESS_EVENT(ev, SoMouseButtonEvent.Button.BUTTON1) && enable.getValue() );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called during handleEvent. It checks if the left mouse button
//    is released. May be overwritten to implement other button masks.
//    Addd by cschumann 2015.08.24 to allow to overwrite button masks
//
// Use: public
//
public boolean
mouseButtonReleased(final SoEvent ev)
{
  return ( SoMouseButtonEvent.SO_MOUSE_RELEASE_EVENT(ev, SoMouseButtonEvent.Button.BUTTON1) && enable.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This virtual function is called when a dragger gains
//    status as "grabber" of events.
//
// Use: public
//
public void
grabEventsSetup()
//
////////////////////////////////////////////////////////////////////////
{
    renderCaching.setValue( SoInteractionKit.CacheEnabled.OFF);

    setStartLocaterPosition( getLocaterPosition() );
    saveStartParameters();

    mouseMovedYet = false;

    isActive.setValue( true );
    startCallbacks.invokeCallbacks(this);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This virtual function is called when a dragger loses
//    status as "grabber" of events.
//
// Use: public
//
public void
grabEventsCleanup()
//
////////////////////////////////////////////////////////////////////////
{
    isActive.setValue( false );
    finishCallbacks.invokeCallbacks(this);

    setPickPath( null );
    setNoPickedSurrogate();

    renderCaching.setValue(SoInteractionKit.CacheEnabled.AUTO);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Returns the event carried by the current handleEventAction,
//  if any.
//
// Use: protected
//
protected SoPath 
getPickPath() 
//
////////////////////////////////////////////////////////////////////////
{
    final SoDragger subDragger = getActiveChildDragger();
    if (subDragger != null)
        return subDragger.getPickPath();
    else
        return pickPath;
}

    //! Called by the above child callbacks. Establishes which child dragger
    //! is currently active. Set at beginning of child callback, returned to
    //! original value at end. Returns current value at time method is called.
////////////////////////////////////////////////////////////////////////
//
//Description:
//This sets the 'activeChildDragger.'
//Called by childStartCB(), childFinishCB(),
//childValueChangedCB(), etc.
//Establishes which dragger is the child, information
//needed by the parent in order to do what it needs to do.
//
    void       setActiveChildDragger( SoDragger newChildDragger ) {
    if (newChildDragger != null)
        newChildDragger.ref();

    if (activeChildDragger != null)
        activeChildDragger.unref();

    activeChildDragger = newChildDragger;
    	
    }
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//     This function is invoked by child draggers 
//     when they change their value.
//
public void 
transferMotion( SoDragger childDragger)
//
////////////////////////////////////////////////////////////////////////
{
    // Get the motion matrix from the child
        final SbMatrix childMotion = new SbMatrix(childDragger.getMotionMatrix());

    // There's a lot we don't need to bother with if the childMotion is
    // identity...
        boolean childIdent = ( childMotion.operator_equal_equal(SbMatrix.identity()) );
    
    // Return if childMotion is identity and our motionMatrix already 
    // matches our saved startMatrix.
        if ( childIdent && (getMotionMatrix().operator_equal_equal( getStartMotionMatrix())))
            return;

        if ( !childIdent ) {
            // First, set the childDragger matrix to identity.
            childDragger.setMotionMatrix( SbMatrix.identity() );

            // Convert the childMotion from child LOCAL space to  world space.
            childDragger.transformMatrixLocalToWorld(childMotion,childMotion);

            // Convert the childMotion from world space to our LOCAL space.
            transformMatrixWorldToLocal(childMotion,childMotion);
        }

    // Append this transformed child motion to our saved start matrix.
        final SbMatrix newMotion = new SbMatrix(getStartMotionMatrix());
        if ( !childIdent )
            newMotion.multLeft( childMotion );

        setMotionMatrix( newMotion );

    // Changing the parent matrix invalidates the matrix cache of the
    // childDragger
        childDragger.cachedPathToThisValid = false;
}

    
    
    public SoDragger getActiveChildDragger() { return activeChildDragger; }

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Returns the event carried by the current handleEventAction,
//  if any.
//
// Use: protected
//
protected SoEvent 
getEvent() 
//
////////////////////////////////////////////////////////////////////////
{
    if ( getHandleEventAction()  == null ) {
//#ifdef DEBUG
        SoDebugError.post("SoDragger::getEvent", "HandleEvent action is NULL");
//#endif
        return null;
    }

    return ( getHandleEventAction().getEvent() );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Returns the point in space that begins the dragging gesture.
//  Usually this is just the picked point, but in special
//  cases, the point is set without picking. 
//  For example, when a modifier key goes down, a dragger will often
//  start a new drag gesture without doing a pick. Instead, the previously
//  used point will be chosen to start a new gesture.
//
// Use: protected
//
protected SbVec3f
getWorldStartingPoint()
//
////////////////////////////////////////////////////////////////////////
{
    return new SbVec3f(startingWorldPoint);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Transforms the starting point into local space and returns it.
//
// Use: protected
//
protected SbVec3f
getLocalStartingPoint()
//
////////////////////////////////////////////////////////////////////////
{
    SbVec3f thePoint = getWorldStartingPoint();

    getWorldToLocalMatrix().multVecMatrix(thePoint, thePoint);
    return thePoint;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called to get the position of the locater in 
//    normalized (0.0 to 1.0) screen space.  (0,0) is lower left
//    the event. 
//
// Use: protected
//
protected SbVec2f
getNormalizedLocaterPosition()
//
////////////////////////////////////////////////////////////////////////
{
    return new SbVec2f( getEvent().getNormalizedPosition( getViewportRegion() ));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is called to get the position of the locater in 
//    non-normalized screen space.  (0,0) is lower left
//    the event. 
//
// Use: protected
//
protected SbVec2s
getLocaterPosition()
//
////////////////////////////////////////////////////////////////////////
{
    return( getEvent().getPosition( getViewportRegion()) );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Returns whether the locater has moved enough to assume a
//  constraint gesture.
//
// Use: private
//
protected boolean
isAdequateConstraintMotion()
//
////////////////////////////////////////////////////////////////////////
{
    SbVec2s moved = (getStartLocaterPosition().operator_minus(getLocaterPosition()));
    short lengthSquared = (short)(moved.getValue()[0]*moved.getValue()[0] + moved.getValue()[1]*moved.getValue()[1]);
    return (boolean) (lengthSquared >= minGesture*minGesture);
}

    //! Get the position of the locater when dragging started.
    public SbVec2s     getStartLocaterPosition()      { return new SbVec2s(startLocater); }

    //! The start locater position is automatically set when button 1
    //! goes down over the dragger. Subclasses may wish to reset it, such
    //! as when a constraint key goes down during dragging.
    public void        setStartLocaterPosition(SbVec2s p)   { startLocater.copyFrom(p); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This sets the viewport and view volume information 
//    explicitly to given values.
//
// Use: protected
//
protected void
setCameraInfo(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // If NULL, use default values
    if (action == null ) {

        viewVolume.ortho(-1, 1, -1, 1, 1, 10);
        vpRegion.copyFrom(new SbViewportRegion((short)1,(short)1));
    }
    // If we've got an action...
    else {
        // Get the viewport and viewVolume...
        SoState state = action.getState();
        viewVolume.copyFrom( SoViewVolumeElement.get( state ));
        vpRegion.copyFrom( SoViewportRegionElement.get(state));
    }

    // Here's a chance to set up a good tempPathToThis if for some reason
    // we don't already have one.
    SoPath pathToMe = createPathToThis();
    if (pathToMe != null)  {
        // We've can create a path. Just get rid of the one it gave us.
        pathToMe.ref();
        pathToMe.unref();
    }
    else if (action != null )
        setTempPathToThis( action.getCurPath() );
}



public void 
transformMatrixToLocalSpace( final SbMatrix fromMatrix,
            final SbMatrix toMatrix, final SbName fromSpacePartName)
{
    final SbMatrix fromToLocalM = new SbMatrix(), localToFromM = new SbMatrix();

    getPartToLocalMatrix( fromSpacePartName, fromToLocalM, localToFromM);

    toMatrix.copyFrom(fromMatrix);
    toMatrix.multRight( fromToLocalM );
    toMatrix.multLeft( localToFromM );
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Simple functions for adding to and removing from
//    the different callback lists.
//
////////////////////////////////////////////////////////////////////////
/////////
// START
/////////
//! Start callbacks are made after the mouse button 1 goes down and the 
//! dragger determines that it has been picked.  If it is going to begin dragging,
//! it grabs events and invokes the startCallbacks.
public void addStartCallback( SoDraggerCB f) {
	addStartCallback(f, null);
}
public void addStartCallback( SoDraggerCB f, Object d )
{  startCallbacks.addCallback((callbackData)-> f.invoke(d, (SoDragger)callbackData), d ); }

public void removeStartCallback( SoDraggerCB f, Object d )
{  startCallbacks.removeCallback( (SoCallbackListCB )f, d ); }

	// Motion callbacks are called after each movement of the mouse during dragging.  
public void addMotionCallback(SoDraggerCB f) {
	addMotionCallback(f, null);
}
	public void addMotionCallback(SoDraggerCB f, Object userData) {
		 motionCallbacks.addCallback( (callbackData)-> f.invoke(userData, (SoDragger)callbackData), userData ); 
	}
	
	// Motion callbacks are called after each movement of the mouse during dragging. 
	public void removeMotionCallback(SoDraggerCB f, Object userData) {
		  motionCallbacks.removeCallback( (SoCallbackListCB )f, userData ); 
	}

	// Finish callbacks are made after dragging ends and the dragger has stopped grabbing events. 
	public void addFinishCallback(SoDraggerCB f) {
		addFinishCallback(f, null);
	}
	public void addFinishCallback(SoDraggerCB f, Object userData) {
		 finishCallbacks.addCallback( (callbackData)-> f.invoke(userData, (SoDragger)callbackData), userData ); 
	}
	
	public void removeFinishCallback( SoDraggerCB f, Object d )
	{  finishCallbacks.removeCallback( (callbackData)-> f.invoke(d, (SoDragger)callbackData), d ); } //TODO
	
	
    //! Value-changed callbacks are made after a dragger changes any of its fields.
    //! This does not include changes to the \b isActive  field.
    //! See also enableValueChangedCallbacks.
    public void    addValueChangedCallback(SoDraggerCB f) {
    	addValueChangedCallback(f, null);
    }
    public void    addValueChangedCallback(SoDraggerCB f, Object userData) {
    	  valueChangedCallbacks.addCallback( (callbackData)-> f.invoke(userData, (SoDragger)callbackData), userData ); 
    }
    //! \see addValueChangedCallback
    public void removeValueChangedCallback(SoDraggerCB f) { //java port
    	removeValueChangedCallback(f, null);
    }
    public void removeValueChangedCallback(SoDraggerCB f, Object userData) {
    	 valueChangedCallbacks.removeCallback( (SoCallbackListCB )f, userData );
    }
    
/////////
// OTHEREVENT
/////////
    public void addOtherEventCallback( SoDraggerCB f) {
    	addOtherEventCallback(f, null);
    }
public void addOtherEventCallback( SoDraggerCB f, Object d )
{  otherEventCallbacks.addCallback( (callbackData)-> f.invoke(d, (SoDragger)callbackData), d ); }

public void removeOtherEventCallback( SoDraggerCB f, Object d )
{  otherEventCallbacks.removeCallback( (SoCallbackListCB )f, d ); }

    

public void registerChildDragger(SoDragger child)
{
    // This calls transferMotion, followed by the callbacks for this node.
    child.addValueChangedCallback( 
                        SoDragger::childTransferMotionAndValueChangedCB, this );

    child.addStartCallback(SoDragger::childStartCB, this);
    child.addMotionCallback(SoDragger::childMotionCB, this);
    child.addFinishCallback(SoDragger::childFinishCB, this);
    child.addOtherEventCallback(SoDragger::childOtherEventCB, this);
}

public void unregisterChildDragger(SoDragger child)
{
    child.removeValueChangedCallback( 
                        SoDragger::childTransferMotionAndValueChangedCB, this );

    child.removeStartCallback(SoDragger::childStartCB, this);
    child.removeMotionCallback(SoDragger::childMotionCB, this);
    child.removeFinishCallback(SoDragger::childFinishCB, this);
    child.removeOtherEventCallback(SoDragger::childOtherEventCB, this);
}

void registerChildDraggerMovingIndependently(SoDragger child)
{
    child.addValueChangedCallback( SoDragger::childValueChangedCB, this );

    child.addStartCallback(SoDragger::childStartCB, this);
    child.addMotionCallback(SoDragger::childMotionCB, this);
    child.addFinishCallback(SoDragger::childFinishCB, this);
    child.addOtherEventCallback(SoDragger::childOtherEventCB, this);
}

void unregisterChildDraggerMovingIndependently(SoDragger child)
{
    child.removeValueChangedCallback( SoDragger::childValueChangedCB, this );

    child.removeStartCallback(SoDragger::childStartCB, this);
    child.removeMotionCallback(SoDragger::childMotionCB, this);
    child.removeFinishCallback(SoDragger::childFinishCB, this);
    child.removeOtherEventCallback(SoDragger::childOtherEventCB, this);
}


public static void childTransferMotionAndValueChangedCB(Object parentAsVoid, 
                                                     SoDragger childDragger)
{
    SoDragger parent = (SoDragger ) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction();
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        // Before calling the other valueChanged callbacks, transfer the
        // motion of the childDragger into our own motion matrix.
            // We do not want to trigger any of our other valueChanged callbacks
            // while this is being done...
            boolean saveEnabled = parent.enableValueChangedCallbacks( false );
            parent.transferMotion( childDragger );
            parent.enableValueChangedCallbacks( saveEnabled );

        parent.valueChanged();
    parent.setActiveChildDragger( savedChild );

        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}

public static void childValueChangedCB( Object parentAsVoid, SoDragger childDragger)
{
    SoDragger parent = (SoDragger) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction(); // ptr
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        parent.valueChanged();
    parent.setActiveChildDragger( savedChild );
        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}


public static void childStartCB(Object parentAsVoid, SoDragger childDragger )
{
    SoDragger parent = (SoDragger ) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        parent.saveStartParameters();

        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction();
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        parent.setStartingPoint( childDragger.getWorldStartingPoint() );

        // While the child is manipulating, we should not bother caching here.
        parent.renderCaching.setValue( SoInteractionKit.CacheEnabled.OFF);
        parent.startCallbacks.invokeCallbacks(parent);
    parent.setActiveChildDragger( savedChild );
        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}

public static void childMotionCB(Object parentAsVoid, SoDragger childDragger )
{
    SoDragger parent = (SoDragger ) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction();
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        parent.motionCallbacks.invokeCallbacks(parent);
    parent.setActiveChildDragger( savedChild );
        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}

public static void childFinishCB(Object parentAsVoid, SoDragger childDragger )
{
    SoDragger parent = (SoDragger ) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction();
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        // When child is finished manipulating, we resume caching.
        parent.renderCaching.setValue( SoInteractionKit.CacheEnabled.AUTO);
        parent.finishCallbacks.invokeCallbacks(parent);
    parent.setActiveChildDragger( savedChild );
        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}

public static void childOtherEventCB(Object parentAsVoid, SoDragger childDragger )
{
    SoDragger parent = (SoDragger ) parentAsVoid;

    SoDragger savedChild = parent.getActiveChildDragger();
    if (savedChild != null) savedChild.ref();
    parent.setActiveChildDragger( childDragger );
        // Save these variables to we can put 'em back when we're done.
        SoHandleEventAction oldHa  = parent.getHandleEventAction();
        SbViewVolume         oldVV  = new SbViewVolume(parent.getViewVolume());
        SbViewportRegion     oldVPR = new SbViewportRegion(parent.getViewportRegion());

        parent.setHandleEventAction(childDragger.getHandleEventAction());
        parent.setViewVolume(childDragger.getViewVolume());
        parent.setViewportRegion(childDragger.getViewportRegion());

        SoPath pathToKid = childDragger.createPathToThis();
        if (pathToKid != null) pathToKid.ref();
        parent.setTempPathToThis( pathToKid );
        if (pathToKid != null) pathToKid.unref();

        parent.otherEventCallbacks.invokeCallbacks(parent);
    parent.setActiveChildDragger( savedChild );
        // Restore saved values of our variables
        parent.setHandleEventAction(oldHa);
        parent.setViewVolume(oldVV);
        parent.setViewportRegion(oldVPR);

    if (savedChild != null) savedChild.unref();
}



    //! Get the motion matrix.  The motion matrix places the dragger
    //! relative to its parent space.  (Generally, it is better to look
    //! in a dragger's fields to find out about its state. For example, 
    //! looking at the 'translation' field of an SoTranslate1Dragger is
    //! easier than extracting the translation from its motion matrix.
public SbMatrix getMotionMatrix()
//
////////////////////////////////////////////////////////////////////////
{
    // Try the fast way to access this node first...
    SoMatrixTransform mm = (SoMatrixTransform ) motionMatrix.getValue();

    // If that fails, then make a new part...
    if (mm == null)
        mm = (SoMatrixTransform ) getAnyPart(new SbName("motionMatrix"), true);

    return ( mm.matrix.getValue() );
}

    

	/**
	 * You can temporarily disable a dragger's valueChangedCallbacks. 
	 * The method returns a value that tells you if callbacks were already enabled. 
	 * Use this method if you write a valueChanged callback of your own 
	 * and you change one of the dragger's fields within the callback. 
	 * (For example, when writing a callback to constrain your dragger). 
	 * Disable first, then change the field, then re-enable the callbacks 
	 * (if they were enabled to start with). 
	 * All this prevents you from entering an infinite loop of changing values, 
	 * calling callbacks which change values, etc. 
	 * 
	 * @param newVal
	 * @return
	 */
	public final boolean enableValueChangedCallbacks(boolean newVal) {
		
	     boolean oldVal = valueChangedCallbacksEnabled;
	          valueChangedCallbacksEnabled = newVal;
	          return oldVal;
	     }
	
    //! Set the motion matrix.  Triggers value changed callbacks, but only
    //! if (newMatrix != the current motionMatrix)
    public void            setMotionMatrix( final SbMatrix newMatrix ) {
    // Return if no change...
        if ( getMotionMatrix().operator_equal_equal(newMatrix) )
            return;

    // Set motion matrix (the field will be non-null, 
    // since 'getMotionMatrix()' was just called
        ((SoMatrixTransform )motionMatrix.getValue()).matrix.setValue( newMatrix);

    // We'll need to recalculate the conversion matrices.
        cachedMotionMatrixValid = false;

    // Invokes the value changed callbacks
        valueChanged();    	
    }	
	
    //! Invokes the valueChangedCallbacks.
    //! These are invoked whenever setMotionMatrix() changes the motion matrix.
    //! If a subclass wishes to invoke the valueChanged callbacks for some
    //! other reason, they may call valueChanged(). Example: SoSpotLightDragger
    //! changes its angle field without altering the motionMatrix. So it
    //! calls valueChanged() to invoke callbacks.
    public void valueChanged() {
    if (valueChangedCallbacksEnabled == true)
        valueChangedCallbacks.invokeCallbacks(this);    	
    }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Saves the motionMatrix as the startMotionMatrix.
//    The startMotionMatrix is used to remember where the dragger was
//    at the beginning of dragging.
//
public void
saveStartParameters()
//
////////////////////////////////////////////////////////////////////////
{
    startMotionMatrix.copyFrom(getMotionMatrix());
}

    

// Called by the SoBaseKit::write() method. 
//
// Draggers don't want to write out fields if they have default vals.
//
// sets isActive to default if default and not connected.
//
// Looks for fields named: 
//    'rotation'    where value is within TINY of SbRotation::identity()
//    'translation' where value is within TINY of (0,0,0)
//    'center'      where value is within TINY of (0,0,0)
//    'scaleFactor' where value is within TINY of (1,1,1)
// and sets them to default if they are not connected from a field.
// (note that most draggers are missing at least some of these, but thats okay)
//
// Then calls the method for SoInteractionKit.
//
// NOTE: Parts which are set to default may still wind up writing to file 
//       if, for example, they lie on a path.
public void setDefaultOnNonWritingFields()
{
    // We don't write out isActive if it has default value.
    if ( ! (isActive.isConnected() && isActive.isConnectionEnabled())
         && isActive.getValue() == false )
                      isActive.setDefault(true);

    // Since so many draggers have fields named 'rotation',
    // 'translation', 'scaleFactor', and 'center', we'll check for them
    // in the base class and set them to default if we can...
final float TINY = 0.00001f;
    SoField f;
    if ( (f = getField( "rotation" )) != null ) {
        if ( !(f.isConnected() && f.isConnectionEnabled())
             && (((SoSFRotation )f).getValue()).equals(
                        SbRotation.identity(),TINY))
        f.setDefault(true);
    }
    if ( (f = getField( "translation" )) != null ) {
        if ( !(f.isConnected() && f.isConnectionEnabled())
             && (((SoSFVec3f )f).getValue()).equals(new SbVec3f(0,0,0),TINY))
        f.setDefault(true);
    }
    if ( (f = getField( "scaleFactor" )) != null ) {
        if ( !(f.isConnected() && f.isConnectionEnabled())
             && (((SoSFVec3f )f).getValue()).equals(new SbVec3f(1,1,1),TINY))
        f.setDefault(true);
    }
    if ( (f = getField( "center" )) != null ) {
        if ( !(f.isConnected() && f.isConnectionEnabled())
             && (((SoSFVec3f )f).getValue()).equals(new SbVec3f(0,0,0),TINY))
        f.setDefault(true);
    }
//#undef TINY

    // This node may change after construction, but we still
    // don't want to write it out.
    motionMatrix.setDefault(true);

    // Call the base class...
    super.setDefaultOnNonWritingFields();
}
    
    //! Changes only that part of the matrix for which this dragger has fields.
    //! For instance, when called by an SoTranslate1Dragger, any rotations and
    //! scales in the matrix will remain unchanged. But the translation will
    //! be replaced with value in the 'translation' field.
    //!   First, factor mtx to get values for the following:
    //!      trans, rot, scale, scaleOrient.
    //!      If there's a center field, send it to the factor method.
    //!   Replace any of these four values if fields are found named
    //!     'translation' 'scaleFactor' 'rotation' and/or 'scaleOrientation'
    //!     Note that this could be all or none of the 4 listed.
    //!     Values for which no fields are found remain unchanged.
    //!   Build a new matrix using the new values. Some may be the originals
    //!     and some may have been replaced.
    void workFieldsIntoTransform( SbMatrix mtx ) {
    final SbVec3f    trans = new SbVec3f(); SbVec3f  translationPtr = null;
    final SbRotation rot = new SbRotation(); SbRotation   rotationPtr = null;
    final SbVec3f    scale = new SbVec3f(); SbVec3f  scaleFactorPtr = null;
    final SbRotation orient = new SbRotation(); SbRotation scaleOrientationPtr = null;
    final SbVec3f    center = new SbVec3f(); SbVec3f centerPtr = null;
    SoField    f;

    // Assign values from any fields you might find...
        if ( (f = getField( "translation" )) != null ) {
            trans.copyFrom(  ((SoSFVec3f )f).getValue());
            translationPtr = trans;
        }
        if ( (f = getField( "rotation" )) != null ) {
            rot.copyFrom(  ((SoSFRotation )f).getValue());
            rotationPtr = rot;
        }
        if ( (f = getField( "scaleFactor" )) != null ) {
            scale.copyFrom(  ((SoSFVec3f )f).getValue());
            scaleFactorPtr = scale;
        }
        if ( (f = getField( "scaleOrientation" )) != null ) {
            orient.copyFrom(  ((SoSFRotation )f).getValue());
            scaleOrientationPtr = orient;
        }
        if ( (f = getField( "center" )) != null ) {
            center.copyFrom(  ((SoSFVec3f )f).getValue());
            centerPtr = center;
        }

    workValuesIntoTransform( mtx, translationPtr,
        rotationPtr, scaleFactorPtr, scaleOrientationPtr, centerPtr );
    	
    }
    
    public void
workValuesIntoTransform( final SbMatrix mtx, 
                            final SbVec3f translationPtr,
                            final SbRotation rotationPtr, 
                            final SbVec3f scaleFactorPtr,
                            final SbRotation scaleOrientationPtr, 
                            final SbVec3f centerPtr )
{
    final SbVec3f    trans = new SbVec3f(), scale = new SbVec3f();
    final SbRotation rot = new SbRotation(), scaleOrient = new SbRotation();
    final SbVec3f    center = new SbVec3f(0,0,0);

    // To begin with, get the values currently in the matrix. If we were 
    // given a center, use it for the calculations.
        if ( centerPtr != null )
            center.copyFrom(  centerPtr);
        SoDragger.getTransformFast( mtx, trans,rot,scale,scaleOrient,center);

    // Now, replace any values which should be dictated by our input.
    // Don't need to do center again, since it should remain unchanged.
        if ( translationPtr != null )
            trans.copyFrom( translationPtr); 
        if ( rotationPtr != null )
            rot.copyFrom( rotationPtr); 
        if ( scaleFactorPtr != null )
            scale.copyFrom( scaleFactorPtr); 
        if ( scaleOrientationPtr != null )
            scaleOrient.copyFrom( scaleOrientationPtr); 

    // Finally, construct a new transform with these values.
        mtx.setTransform( trans, rot, scale, scaleOrient, center );
}

    
static void
getTransformFast( final SbMatrix mtx, final SbVec3f translation,
                             final SbRotation rotation, final SbVec3f scaleFactor,
                             final SbRotation scaleOrientation, 
                             final SbVec3f center )
{
    if (center.operator_not_equal(new SbVec3f(0,0,0))) {
        // to get fields for a non-0 center, we
        // need to decompose a new matrix "m" such
        // that [-center][m][center] = [this]
        // i.e., [m] = [center][this][-center]
        // (this trick stolen from Showcase code)
        final SbMatrix m = new SbMatrix(),c = new SbMatrix();
        m.setTranslate(center.operator_minus());
        m.multLeft(mtx);
        c.setTranslate(center);
        m.multLeft(c);
        SoDragger.getTransformFast( m, translation, rotation, scaleFactor,
                               scaleOrientation);
    }
    else
        SoDragger.getTransformFast( mtx, translation, rotation, scaleFactor,
                               scaleOrientation);
}

static void
getTransformFast(  final SbMatrix mtx, final SbVec3f translation,
                        final SbRotation rotation, final SbVec3f scaleFactor,
                        final SbRotation scaleOrientation)
{
    boolean canDoFast = true;

    // If the last column is (0,0,0,1), then we don't have to worry 
    // about projection matrix.  If not, we need to call SbMatrix::factor
    if ( mtx.getValue()[0][3] != 0 || mtx.getValue()[1][3] != 0 || mtx.getValue()[2][3] != 0 || mtx.getValue()[3][3] != 1)
        canDoFast = false;

    // You get maxXVec, matYVec, and matZVec if you send the xVec,yVec,zVec,
    // through the matrix.  
    final SbVec3f xVec = new SbVec3f(1,0,0), yVec = new SbVec3f(0,1,0), zVec = new SbVec3f(0,0,1);
    final SbVec3f matXVec = new SbVec3f( mtx.getValue()[0][0], mtx.getValue()[0][1], mtx.getValue()[0][2] );
    final SbVec3f matYVec = new SbVec3f( mtx.getValue()[1][0], mtx.getValue()[1][1], mtx.getValue()[1][2] );
    final SbVec3f matZVec = new SbVec3f( mtx.getValue()[2][0], mtx.getValue()[2][1], mtx.getValue()[2][2] );
    // If they are orthogonal, that means the 
    // scaleOrientation is identity() and we are free to factor the matrix.
    // Only need to test two sets, since 3rd answer is implicit.
final float TINY = 0.00001f;
    if (       Math.abs( matXVec.dot( matYVec )) > TINY )
        canDoFast = false;
    else if (  Math.abs( matYVec.dot( matZVec )) > TINY )
        canDoFast = false;
//#undef TINY

    if ( canDoFast == true ) {

        scaleOrientation.setValue(0,0,0,1);

        // Translation is just the first three entries in bottom row.
        translation.setValue( mtx.getValue()[3][0], mtx.getValue()[3][1], mtx.getValue()[3][2] );

        // scaleFactor is scale of the three transformed axes.
        // Kill two birds with one stone and normalize to get the sizes...
        scaleFactor.setValue( matXVec.normalize(),
                              matYVec.normalize(),
                              matZVec.normalize());

        rotation.copyFrom( new SbMatrix( matXVec.getValueRead()[0], matXVec.getValueRead()[1], matXVec.getValueRead()[2], 0,
                             matYVec.getValueRead()[0], matYVec.getValueRead()[1], matYVec.getValueRead()[2], 0,
                             matZVec.getValueRead()[0], matZVec.getValueRead()[1], matZVec.getValueRead()[2], 0,
                             0,          0,          0,          1 ));
    }
    else {
        // If 'canDoFast' == false, send the info to SbMatrix::factor
//#ifdef DEBUG
//#if 0
//        SoDebugError::post("SoDragger::getTransformFast", 
//                      "This is a tricky matrix. Giving it to SbMatrix::factor");
//#endif
//#endif

        final SbMatrix proj = new SbMatrix(), rotMatrix = new SbMatrix(), scaleOrientMatrix = new SbMatrix();
        mtx.factor(scaleOrientMatrix, scaleFactor, rotMatrix, translation,proj);
        rotation.copyFrom(rotMatrix);
        // have to transpose because factor gives transpose of correct answer
        scaleOrientation.copyFrom( scaleOrientMatrix.transpose());  
    }
}

    
////////////////////////////////////////////////////////////////////////
//
// Use: EXTENDER public
//
// Description:
// Get the matrix which converts from the space of one part into
// local space. Good to use if transforms occur between 'motionMatrix'
// and the space you want to work in.
// Note: This routine will try not to create parts that don't exist.
//       Instead it finds the existing part that precedes it in traversal.
//       But this only works if the partName is in this nodekit's catalog.
//       If the part is nested within another kit below this one or 
//       sitting inside a list part, the part will be created when it 
//       doesn't exist.
//
private static SoGetMatrixAction ma = null;
public void 
getPartToLocalMatrix( final String partName, 
            final SbMatrix partToLocalMatrix, final SbMatrix localToPartMatrix) {	 
	getPartToLocalMatrix( new SbName(partName), 
	            partToLocalMatrix, localToPartMatrix);
}
public void 
getPartToLocalMatrix( final SbName partName, 
            final SbMatrix partToLocalMatrix, final SbMatrix localToPartMatrix)
//
////////////////////////////////////////////////////////////////////////
{
    // We need to temporarily ref ourself, since we build paths
    // and stuff...
    ref();

    SoPath pathToMe = createPathToThis();
    if (pathToMe != null)
        pathToMe.ref();

    SoPath pathToPart;

    // We want to figure this out without creating any parts unnecessarily.
    // So, instead of forcing creation, do a check.
        pathToPart = SoPath.cast(createPathToAnyPart(partName,false,false,false,pathToMe));

    // If we didn't find a path that already exists:
        if (pathToPart == null) {
            final SoNodekitCatalog cat = getNodekitCatalog();
            int pNum = cat.getPartNumber(partName);
            if ( pNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND ) {

                // If it fails and the part is in this catalog, then traverse 
                // backwards until we can find the last part before partName 
                // and after "motionMatrix" If we hit "motionMatrix", or 
                // "this", then just set path to null
                int thisPnum   = cat.getPartNumber("this");
                int motMatPnum = cat.getPartNumber("motionMatrix");

                while ((pathToPart == null) &&
                       (pNum != thisPnum) && (pNum != motMatPnum)) 
                {
                    // Find left sibling or parent.  Can check 'em together
                    // since left sibling never precedes parent.
                    for (int i = pNum-1; i >= 0; i-- ) {
                        if ((cat.getRightSiblingPartNumber(i) == pNum) ||
                            (cat.getParentPartNumber(pNum) == i)) {
                            pNum = i;
                            break;
                        }
                    }
                    if ( pNum != thisPnum && pNum != motMatPnum ) {
                        pathToPart = createPathToAnyPart(cat.getName(pNum),
                                false, false, false, pathToMe);
                    }
                }

            }
            else {
                // If the part doesn't exist yet and the partName
                // is not in this catalog, we're sort of stuck. Force 
                // it and create the part anyway. (2nd arg == true)
                pathToPart = createPathToAnyPart(partName,true,
                                                 false,false,pathToMe);
            }
        }

    // We don't need this path anymore.
    if (pathToMe != null)
        pathToMe.unref();

    if ( pathToPart == null ) {
        partToLocalMatrix.copyFrom( SbMatrix.identity());
        localToPartMatrix.copyFrom( SbMatrix.identity());
        // Undo temporary ref on ourself.
        unrefNoDelete();
        return;
    }

    pathToPart.ref();

    if (ma == null)
        ma = new SoGetMatrixAction( getViewportRegion() );
    else
        ma.setViewportRegion( getViewportRegion() );

    ma.apply( pathToPart );
    final SbMatrix partToWorld = new SbMatrix(ma.getMatrix());
    final SbMatrix worldToPart = new SbMatrix(ma.getInverse());

    pathToPart.unref();

    partToLocalMatrix.copyFrom( partToWorld);
    partToLocalMatrix.multRight( getWorldToLocalMatrix() );

    localToPartMatrix.copyFrom( getLocalToWorldMatrix());
    localToPartMatrix.multRight( worldToPart);

    // Undo temporary ref on ourself.
    unrefNoDelete();
}

    public SbViewVolume      getViewVolume() { return viewVolume; }
    public void  setViewVolume(final SbViewVolume vol) { viewVolume.copyFrom(vol); }

    public SbViewportRegion getViewportRegion() { return vpRegion; }
    public void  setViewportRegion(final  SbViewportRegion reg) { vpRegion.copyFrom(reg); }

    //! Get the most recent handleEventAction.
    public SoHandleEventAction getHandleEventAction() 
        { return handleEventAction; }
    public void setHandleEventAction( SoHandleEventAction newAction ) {
        handleEventAction = newAction;
    }
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Use: private
//
    private static SoSearchAction sa = null;
private void
setTempPathToThis( final SoPath somethingClose )
//
////////////////////////////////////////////////////////////////////////
{
    // Check that we're not doing something unnecessary...
    // If paths don't fork until 'this', we can keep the path we've got.
    // But even if we've already got an okay path, we should invalidate
    // here, since some matrix has probably changed.
    if ( tempPathToThis != null && somethingClose != null ) {
        int forkInd = tempPathToThis.findFork( somethingClose );
        if ( forkInd == (tempPathToThis.getLength() - 1)) {
            cachedPathToThisValid           = false;
            return;
        }
    }

    // We can not keep our old tempPathToThis. Delete it now. 
    // ( you delete tempPaths, not ref/unref)
    if (tempPathToThis != null) {
        tempPathToThis.destructor();
        tempPathToThis = null;
    }
    if (tempPathNumKidsHack != null) {
        tempPathNumKidsHack.destructor();
        tempPathNumKidsHack = null;
    }

    // Figure out tempPathToThis from somethingClose, which may 
    // not be exactly what we need...
    if ( somethingClose != null && somethingClose.containsNode(this) ) {
        // Create tempPathToThis. Copy from something close.
        tempPathToThis 
            = createTempPathFromFullPath( SoFullPath.cast(somethingClose) );

        // Then pop off  'til the you get to this.
        for (SoNode n = tempPathToThis.getTail(); n != this; ) {
            tempPathToThis.pop();
            n = tempPathToThis.getTail();
        }
    }
    else if ( somethingClose != null && somethingClose.getLength() > 0 ) {
        // Start at the root and search for 'this' underneath.
        // Turn on nodekit searching for the duration...
        if (sa == null)
            sa = new SoSearchAction();
        else
            sa.reset();

        boolean oldBool = SoBaseKit.isSearchingChildren();
        SoBaseKit.setSearchingChildren( true );
            sa.setNode( this );
            sa.apply( somethingClose.getHead() );
            SoBaseKit.setSearchingChildren( oldBool );
        if ( sa.getPath() != null )
            tempPathToThis 
                = createTempPathFromFullPath(SoFullPath.cast(sa.getPath()));
    }

    // Create the tempPathNumKidsHack.  This is an sbPlist that records
    // how many children are under each node along the path.
    // We use it later to reconstruct the path if the parent/child pairs
    // are still okay, but the indices in the path have changed.
    if ( tempPathToThis != null ) {
        SoNode pathNode;

        tempPathNumKidsHack = new SbPList( tempPathToThis.getLength() );

        for ( int i = 0; i < tempPathToThis.getLength(); i++ ) {
            pathNode = tempPathToThis.getNode(i);
            if ( pathNode==null  || pathNode.getChildren()==null )
                tempPathNumKidsHack.append( (Object) 0 );
            else
                tempPathNumKidsHack.append( 
                            (Object) (int) pathNode.getChildren().getLength() );
        }
    }

    // In both cases, the cachedPath has changed...
    cachedPathToThisValid           = false;
}

    

////////////////////////////////////////////////////////////////////////
//
// Use: private
//
//private    static SoGetMatrixAction ma = null;
private void
validateMatrices()
//
////////////////////////////////////////////////////////////////////////
{
    // If both aspects of the matrices are still okay, then we can
    // continue to use the cached values.
    if ( cachedPathToThisValid && cachedMotionMatrixValid )
        return;

    // If the tempPathToThis is no longer valid, then we've got to run a 
    // getMatrix action to find the preMotionToWorld and 
    // worldToPreMotion matrices.
    if ( cachedPathToThisValid == false ) {

        // Do a get matrix action from world space to this node.
        SoPath pathToMe = createPathToThis();
        if ( pathToMe != null ) {
            pathToMe.ref();
            if (ma == null)
                ma = new SoGetMatrixAction( getViewportRegion() );
            else
                ma.setViewportRegion( getViewportRegion() );

            ma.apply( pathToMe );
            preMotionToWorldMatrix.copyFrom( ma.getMatrix());
            worldToPreMotionMatrix.copyFrom( ma.getInverse());
            pathToMe.unref();
        }
        else {
            preMotionToWorldMatrix.makeIdentity();
            worldToPreMotionMatrix.makeIdentity();
        }
    }

    // If the cachedMotionMatrix is no longer valid, then figure 'em out...
    if ( cachedMotionMatrixValid == false ) {
        cachedMotionMatrix.copyFrom( getMotionMatrix());
    }

    // Last, we append the motion matrix to the preMotion matrix to get
    // the postMotion matrix.
    postMotionToWorldMatrix.copyFrom(preMotionToWorldMatrix);
    postMotionToWorldMatrix.multLeft( cachedMotionMatrix );

    worldToPostMotionMatrix.copyFrom( worldToPreMotionMatrix);
    worldToPostMotionMatrix.multRight( cachedMotionMatrix.inverse() );

    cachedMotionMatrixValid = true;
    cachedPathToThisValid = true;
}
    
    
////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
public SbMatrix
getLocalToWorldMatrix()
//
////////////////////////////////////////////////////////////////////////
{
    // Make sure everything is kosher
    validateMatrices();

    return new SbMatrix(postMotionToWorldMatrix);
}

////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
public SbMatrix getWorldToLocalMatrix()
//
////////////////////////////////////////////////////////////////////////
{
    // Make sure everything is kosher
    validateMatrices();

    return new SbMatrix(worldToPostMotionMatrix);
}


////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
public void
transformMatrixLocalToWorld( final SbMatrix   fromMatrix, 
                                          final SbMatrix         toMatrix)
//
////////////////////////////////////////////////////////////////////////
{
    toMatrix.copyFrom( fromMatrix);

    final SbMatrix forward = new SbMatrix(getLocalToWorldMatrix());
    final SbMatrix backward = new SbMatrix(getWorldToLocalMatrix());

    toMatrix.multRight( forward );
    toMatrix.multLeft( backward );
}

////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
public void
transformMatrixWorldToLocal( final SbMatrix   fromMatrix, 
                                          final SbMatrix         toMatrix)
//
////////////////////////////////////////////////////////////////////////
{
    toMatrix.copyFrom(fromMatrix);

    final SbMatrix forward = new SbMatrix(getWorldToLocalMatrix());
    final SbMatrix backward = new SbMatrix(getLocalToWorldMatrix());

    toMatrix.multRight( forward );
    toMatrix.multLeft( backward );
}



public SoPath 
createPathToThis() 
{
    if ( isTempPathToThisOk() )
        return tempPathToThis.copy();
    else
        return null;
}

public boolean
isTempPathToThisOk()
{
    // ??? Hack alert! ???
    //     This whole method is a hack!
    // 

    if ( tempPathToThis == null || tempPathNumKidsHack == null)
        return false;

    boolean isOkay = true;

    SoNode actualNode = null;
    SoNode nodeInPath = null;
    
    for(int numFmTail = 0; numFmTail < tempPathToThis.getLength();numFmTail++){

        int numFmHead = tempPathToThis.getLength() - numFmTail -1;
        nodeInPath    = tempPathToThis.getNode( numFmHead );

        if (numFmTail == 0) {
            if (this != nodeInPath) {
                isOkay = false;
                break;
            }
        }
        else {
            // actual node is still node from last time.
            // See if you can find 'nodeInPath' as a parent in the
            // auditor list of actualNode.
            final SoAuditorList aList = actualNode.getAuditors();
            if ( aList.find( nodeInPath, SoNotRec.Type.PARENT ) == -1 ) {
                isOkay = false;
                break;
            }
            else {
                // Since this is a temp path, it does not get notified 
                // when children are added or deleted. So we should
                // check and adjust the indices in the path if they have
                // gotten screwed up...

                // Make sure that the
                // index is correct for this parent/child pair.
                SoNode parent = nodeInPath;
                SoNode child  = actualNode;

                SoChildList children = parent.getChildren();
                if (children == null) {
                    isOkay = false;
                    break;
                }

                int indexInPath = tempPathToThis.getIndex(numFmHead+1);

                int numKidsNow    = children.getLength();
                int numKidsBefore = (int) ((int) (tempPathNumKidsHack).operator_square_bracket(numFmHead));

                // To be correct, the childNode has to be the correct numbered
                // child under the parent, and the parent should still
                // have the same number of children it did before. 
                if (   (numKidsNow != numKidsBefore)
                    || (numKidsNow <= indexInPath)
                    || ((children).operator_square_bracket(indexInPath) != child) ) {

                    // We have a problem. Try to fix it.

                    // First, figure out where it is most likely to belong.
                    // Also, update the tempPathNumKidsHack
                    int bestSpot = indexInPath;
                    if ( numKidsNow != numKidsBefore ) {
                        // update the number of kids.
                        tempPathNumKidsHack.remove(numFmHead);
                        tempPathNumKidsHack.insert( (Object) (int) numKidsNow, numFmHead);

                        bestSpot = indexInPath + (numKidsNow - numKidsBefore);
                    }

                    // Look forward and back from bestSpot and try to find the
                    // node as a child. See which way we come to the node first.
                    // (we have to do this because of instancing.)
                    int early;
                    int late;
                    int newSpot = -1;
                    for ( early = bestSpot, late = bestSpot; 
                          early >= 0 || late < numKidsNow;
                          early--, late++ ) {
                        if (early >= 0 && early < numKidsNow &&
                            (child == (children).operator_square_bracket(early))) {
                                newSpot = early;
                                break;
                        }
                        if (late >= 0 && late < numKidsNow &&
                            (child == (children).operator_square_bracket(late))) {
                                newSpot = late;
                                break;
                        }
                    }

                    if ( newSpot == -1 ) {
                        isOkay = false;
                        break;
                    }

                    if ( newSpot != indexInPath ) {

                        // We need to alter the path so we increment or
                        // decrement the index.

                        // Increments or decrements index of the child, since
                        // some children may have been added or deleted.
                        if (newSpot < indexInPath) {
                            for (int j = newSpot; j < indexInPath; j++ )
                                tempPathToThis.removeIndex(nodeInPath, 0 );
                        }
                        else if (newSpot > indexInPath) {
                            for (int j = newSpot; j > indexInPath; j-- )
                                tempPathToThis.insertIndex(nodeInPath, 0 );
                        }
                    }
                }
            }
        }
        actualNode = nodeInPath;
    }

    if (isOkay == false) {
        if (tempPathToThis != null) {
            tempPathToThis.destructor();
            tempPathToThis = null;
        }
        if (tempPathNumKidsHack != null) {
            tempPathNumKidsHack.destructor();
            tempPathNumKidsHack = null;
        }
    }

    return isOkay;
}

    
public SoTempPath 
createTempPathFromFullPath( final SoFullPath fp )
{
    SoTempPath answer = new SoTempPath( fp.getLength() );

    for ( int i = 0; i < fp.getLength(); i++ )
        answer.append( fp.getNode(i) );

    return answer;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the pickPath to be the given path.
//
public void
setPickPath( final SoPath newPickPath )
//
////////////////////////////////////////////////////////////////////////
{
    // Set the pickPath. This should just equal the input path.
    // ref the input before unref'ing the old path, in case they're the same
        if ( newPickPath != null )
            newPickPath.ref();

        // get rid of old path if it exists
        if ( pickPath != null ) {
            pickPath.unref();
            pickPath = null;
        }
        // add new path if it exists
        if ( newPickPath != null )
            pickPath = (SoPath ) newPickPath;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the surrogatePick information to initial values.
//
public void
setNoPickedSurrogate()
//
////////////////////////////////////////////////////////////////////////
{
    // Set the name to be empty
    surrogateNameInPickOwner.copyFrom("");

    // get rid of old paths if they exist.
    if ( pathToSurrogatePickOwner != null ) {
         pathToSurrogatePickOwner.unref();
         pathToSurrogatePickOwner = null;
    }
    if ( surrogatePathInPickOwner != null ) {
         surrogatePathInPickOwner.unref();
         surrogatePathInPickOwner = null;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
public boolean
shouldGrabBasedOnSurrogate( final SoPath pickPath, 
                                final SoPath surrogatePath )
//
////////////////////////////////////////////////////////////////////////
{
    // The pickPath must contain the surrogatePath
    if ( pickPath.containsPath( surrogatePath ) == false )
        return false;

    final SoFullPath fullPick = SoFullPath.cast(pickPath);
    final SoFullPath fullSurr = SoFullPath.cast(surrogatePath);

    // Find the tail of surrogatePath.
    SoNode surrTail = fullSurr.getTail();

    // Go from the tail of pickPath backwards.
    // If you find a dragger before you find surrTail, return FALSE.
    // Otherwise, return TRUE.
    SoNode pickNode;
    for (int i = fullPick.getLength() - 1; i >= 0; i-- ) {
        pickNode = fullPick.getNode(i);
        if (pickNode == surrTail)
            return true;
        if (pickNode.isOfType( SoDragger.getClassTypeId() ))
            return false;
    }
    // Should never get here...
    return false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the surrogatePick information.
//
public void
setPickedSurrogate( final SoPath pathToOwner, final SbName nameUsedByOwner,
                               final SoPath pathUsedByOwner )
//
////////////////////////////////////////////////////////////////////////
{
    // Set the pathToSurrogatePickOwner. 
        if ( pathToOwner != pathToSurrogatePickOwner ) {
            if ( pathToOwner != null )
                pathToOwner.ref();

            if ( pathToSurrogatePickOwner != null )
                 pathToSurrogatePickOwner.unref();

            pathToSurrogatePickOwner = pathToOwner;
        }

    // Set the surrogatePathInPickOwner. 
        if ( pathUsedByOwner != surrogatePathInPickOwner ) {
            if ( pathUsedByOwner != null )
                pathUsedByOwner.ref();

            if ( surrogatePathInPickOwner != null )
                 surrogatePathInPickOwner.unref();

            surrogatePathInPickOwner = pathUsedByOwner;
        }

    // Set the name...
        surrogateNameInPickOwner.copyFrom( nameUsedByOwner);
}

    //! Surrogate Pick Information. This is relevant if the we are dragging
    //! based on the pick of a surrogate part. This happens when you call
    //! SoInteractionKit::setPartAsPath() and thus specify a 'stand-in' path 
    //! to pick when initiating a drag.
    //! Owner: 
    //!       Path to the InteractionKit that owns the surrogate part.
            public SoPath getSurrogatePartPickedOwner() 
                  { return pathToSurrogatePickOwner;}
    //! Name:  
    //!       Name of the surrogate part within the owners list.
            public SbName getSurrogatePartPickedName() 
                { return surrogateNameInPickOwner; }
    //! SurrogatePath: 
    //!        The owner's surrogate path for this name. This path may 
    //!        differ from the actual pickPath, but it is guaranteed to 
    //!        be contained within the pickPath.
            public SoPath getSurrogatePartPickedPath()  
                { return surrogatePathInPickOwner;}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the point in space where dragger is to begin.
//    The pickedPoint version would usually be the result of a pick.
//    Assumes that 'newPoint' is in world space.
//
public void
setStartingPoint( final SoPickedPoint newPoint )
//
////////////////////////////////////////////////////////////////////////
{
    startingWorldPoint.copyFrom(newPoint.getPoint());
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the point in space where dragger is to begin.
//    The SbVec3f version would usually be a piont saved from the end of 
//    another gesture. For example, when a modifier key goes down, we might
//    save the current position and use it to begin another connected gesture.
//    Assumes that 'newPoint' is in world space.
//
public void
setStartingPoint( final SbVec3f newPoint )
//
////////////////////////////////////////////////////////////////////////
{
    startingWorldPoint.copyFrom(newPoint);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This implements the SoGetBoundingBoxAction.
//
// Use: protected
//
public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if ( ! ignoreInBbox )
        super.getBoundingBox( action );
}

    //! Set and get the number of pixels of movement required to
    //! initiate a constraint gesture. Default is 8.
public     void    setMinGesture(int pixels)       { minGesture = pixels; }
    //! Set and get the number of pixels of movement required to
    //! initiate a constraint gesture. Default is 8.
public    int     getMinGesture()            { return minGesture; }

    //! The smallest scale that any dragger will write. If the user attempts
    //! to go below this amount, the dragger will set it to this minimum.
    //! Default is .001
public     static void  setMinScale( float newMinScale ) { minScale = newMinScale; }
    //! Get min scale.
public     static float getMinScale() { return minScale; }

        //! Used to calculate motion relative to the dragger's position at the
    //! beginning of the dragging.
    public SbMatrix getStartMotionMatrix() { return startMotionMatrix; }
    

    final SbMatrix //java port
    appendTranslation( final SbMatrix mtx, 
                            final SbVec3f translation)
    {
    	return appendTranslation(mtx, translation, null);
    }
final SbMatrix
appendTranslation( final SbMatrix mtx, 
                        final SbVec3f translation, 
                        final SbMatrix conversion )
{
    boolean    isCnvIdent = (conversion == null 
                          || (conversion).operator_equal_equal(SbMatrix.identity()));
    boolean    isMtxIdent = (mtx.operator_equal_equal(SbMatrix.identity()));

    // Get motion into local Space.
    // Local space for translation is at the beginning of the matrix.
    // Convert using multDirMatrix, not multVecMatrix, since this is motion.
    final SbVec3f lclMotion = new SbVec3f(translation);
    if ( !isCnvIdent )
        conversion.multDirMatrix( lclMotion, lclMotion );
    if ( !isMtxIdent )
        mtx.multDirMatrix( lclMotion, lclMotion );

    SbVec3f startTranslate = new SbVec3f(mtx.getValue()[3]);
    SbVec3f newTranslate = startTranslate.operator_add(lclMotion);

    final SbMatrix answer = new SbMatrix(mtx);
    answer.getValue()[3][0] = newTranslate.getValueRead()[0];
    answer.getValue()[3][1] = newTranslate.getValueRead()[1];
    answer.getValue()[3][2] = newTranslate.getValueRead()[2];

    return answer;
}

public SbMatrix // java port
appendScale( final SbMatrix mtx, 
                  final SbVec3f scale, final SbVec3f scaleCenter)
{
	return appendScale(mtx, scale, scaleCenter,null);
}
public SbMatrix 
appendScale( final SbMatrix mtx, 
                  final SbVec3f scale, final SbVec3f scaleCenter,
                  final SbMatrix conversion )
{
    boolean    isCnvIdent = (conversion == null 
                          || (conversion).operator_equal_equal(SbMatrix.identity()));
    boolean    isMtxIdent = (mtx.operator_equal_equal(SbMatrix.identity()));
    final SbMatrix  convInverse = new SbMatrix(), mtxInverse = new SbMatrix();


    if ( !isCnvIdent )
        convInverse.copyFrom( conversion.inverse());
    if ( !isMtxIdent )
        mtxInverse.copyFrom( mtx.inverse());

    // Calculate 'matrixWithScale.' 
    // [matrixWithScale] = [convInverse][scaleMtx][conversion][mtx]
        // Create a scaling matrix;
            final SbMatrix scaleMtx = new SbMatrix();
            scaleMtx.setScale( scale );
        // convert it to space at end of matrix, 
            // [scaleMtx] = [convInverse][scaleMtx][conversion]
            if ( !isCnvIdent ) {
                scaleMtx.multRight( (conversion) );
                scaleMtx.multLeft( convInverse );
            }
        // Append this scaling to mtx.
        // [mtxWithScale] = [scaleMtx][mtx]
            final SbMatrix mtxWithScale = new SbMatrix();
            if ( !isMtxIdent ) {
                mtxWithScale.copyFrom( mtx);
                mtxWithScale.multLeft( scaleMtx );
            }
            else
                mtxWithScale.copyFrom( scaleMtx);

    // Extract the new values from the merged matrix.
        final SbVec3f    mrgTrans = new SbVec3f(), mrgScale = new SbVec3f();
        final SbRotation mrgRot = new SbRotation(), mrgScaleOrient = new SbRotation();
        getTransformFast(mtxWithScale,mrgTrans,mrgRot, mrgScale,mrgScaleOrient);

    // Constrain the scaling to be greater than getMinScale().
        final SbVec3f okayMrgScale = new SbVec3f(mrgScale);
        for (int i = 0; i < 3; i++ ) {
            if (okayMrgScale.getValueRead()[i] <= getMinScale() )
                okayMrgScale.setValue(i, getMinScale());
        }

    final SbVec3f okayScale = new SbVec3f();
    if ( okayMrgScale.operator_equal_equal(mrgScale) )
        okayScale.copyFrom(scale);
    else {
        // If we needed to constrain, figure out 'okayScale.' 

        // First, construct 'okayMtxWithScale.'
        // This is a version of 'matrixWithScale' where we replace
        // 'mrgScale' with 'okayMrgScale.'
            final SbMatrix okayMtxWithScale = new SbMatrix();
            okayMtxWithScale.setTransform( mrgTrans, mrgRot, 
                                        okayMrgScale, mrgScaleOrient );

        // Using the same relationship as earlier:
        // [okayMtxWithScale] = [convInverse][okayScaleMtx][conversion][mtx]
        // Solve for 'okayScaleMtx' to find the scale matrix that gives
        // us the desired result.
            final SbMatrix okayScaleMtx = new SbMatrix(okayMtxWithScale);
            if ( !isMtxIdent )
                okayScaleMtx.multRight( mtxInverse );
            if ( !isCnvIdent ) {
                okayScaleMtx.multRight( convInverse );
                okayScaleMtx.multLeft( (conversion) );
            }

        // Get the okayScale from its matrix.
            okayScale.setValue(0, okayScaleMtx.getValue()[0][0]);
            okayScale.setValue(1, okayScaleMtx.getValue()[1][1]);
            okayScale.setValue(2, okayScaleMtx.getValue()[2][2]);
    }

    // Now we've got a scale (okayScale) and scaleCenter we know we can use.
    // Create the right matrix and append it to get our answer.
    // [answer] = [convInvserse][scaleAboutCenter][conversion][mtx]
    // where: [scaleAboutCenter] = [centerInverse][okayScale][center]
        final SbMatrix scaleAboutCenter = new SbMatrix();
        scaleAboutCenter.setScale( okayScale );
        if ( scaleCenter.operator_not_equal(new SbVec3f(0,0,0) )) {
            final SbMatrix tm = new SbMatrix();
            tm.setTranslate( scaleCenter );
            scaleAboutCenter.multRight( tm );
            tm.setTranslate( scaleCenter.operator_minus() );
            scaleAboutCenter.multLeft( tm );
        }

        final SbMatrix answer = new SbMatrix(scaleAboutCenter);
        if ( !isCnvIdent ) {
            answer.multLeft( convInverse );
            answer.multRight( (conversion) );
        }
        if ( !isMtxIdent )
            answer.multRight( mtx );

    return answer;
}

public SbMatrix //java port
appendRotation( final SbMatrix mtx,
                    final SbRotation rot, final SbVec3f rotCenter) {
	return appendRotation(mtx, rot, rotCenter, null);
}
public SbMatrix
appendRotation( final SbMatrix mtx,
                    final SbRotation rot, final SbVec3f rotCenter,
                    final SbMatrix conversion )
{
    boolean    isCnvIdent = (conversion == null 
                          || (conversion).operator_equal_equal(SbMatrix.identity()));
    boolean    isMtxIdent = (mtx.operator_equal_equal(SbMatrix.identity()));
    final SbMatrix  convInverse = new SbMatrix(), mtxInverse = new SbMatrix();


    if ( !isCnvIdent )
        convInverse.copyFrom( conversion.inverse());
    if ( !isMtxIdent )
        mtxInverse.copyFrom( mtx.inverse());

    // Create a matrix for rotating about the rotCenter and append it to our
    // mtx.
    // [answer] = [convInvserse][rotateAboutCenter][conversion][mtx]
    // where: [rotateAboutCenter] = [centerInverse][rotMat][center]
        final SbMatrix rotateAboutCenter = new SbMatrix();
        rotateAboutCenter.setRotate( rot );
        if ( rotCenter.operator_not_equal( new SbVec3f(0,0,0) )) {
            final SbMatrix tm = new SbMatrix();
            tm.setTranslate( rotCenter );
            rotateAboutCenter.multRight( tm );
            tm.setTranslate( rotCenter.operator_minus() );
            rotateAboutCenter.multLeft( tm );
        }

        final SbMatrix answer = new SbMatrix(rotateAboutCenter);
        if ( !isCnvIdent ) {
            answer.multLeft( convInverse );
            answer.multRight( (conversion) );
        }
        if ( !isMtxIdent )
            answer.multRight( mtx );

    return answer;
}


	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes ALL Inventor dragger classes.
	   //
	   // Use: internal
	   
	   public static void
	   initClasses()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoDragger.initClass();
	       // simple scale draggers
	       //SoScale1Dragger.initClass();
	       //SoScale2Dragger.initClass();
	       //SoScale2UniformDragger.initClass();
	       SoScaleUniformDragger.initClass();
	       // simple translate draggers
	       SoTranslate1Dragger.initClass();
	       SoTranslate2Dragger.initClass();
	       // simple rotation draggers
	       SoRotateSphericalDragger.initClass();
	       SoRotateCylindricalDragger.initClass();
	       //SoRotateDiscDragger.initClass();
	       // coord draggers
	       //SoDragPointDragger.initClass();
	       // transform draggers
	       //SoJackDragger.initClass();
	       SoHandleBoxDragger.initClass();
	       SoCenterballDragger.initClass();
	       //SoTabPlaneDragger.initClass();
	       SoTabBoxDragger.initClass();
	       SoTrackballDragger.initClass();
	       // composite transform draggers
	       // init these after all the canonical draggers
	       //SoPointLightDragger.initClass();
	       SoTransformBoxDragger.initClass();
	       //SoTransformerDragger.initClass();
	       // lightDraggers
	       //SoDirectionalLightDragger.initClass();
	       //SoSpotLightDragger.initClass();
	   }
	   	
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Initialize the dragger
	    //
	    // Use: public, internal
	    //
	   public static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SO__KIT_INIT_CLASS(SoDragger.class, "Dragger", SoInteractionKit.class);
	    }
	    }
