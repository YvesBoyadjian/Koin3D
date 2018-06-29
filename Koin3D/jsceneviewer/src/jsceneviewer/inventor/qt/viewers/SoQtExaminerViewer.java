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
 * Copyright (C) 1990-93   Silicon Graphics, Inc.
 * Author(s): Alain Dumesny
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */


package jsceneviewer.inventor.qt.viewers;

import java.time.Instant;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoOrthographicCamera;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoScale;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.database.inventor.nodes.SoTranslation;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoSensorCB;
import jsceneviewer.QDoubleSpinBox;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.SoQtIcons;
import jsceneviewer.inventor.qt.SoQtThumbWheel;

/**
 * @author Yves Boyadjian
 *
 */

//////////////////////////////////////////////////////////////////////////////
//
//Class: SoXtExaminerViewer
//
//Examiner viewer - allows the user to change the camera position
//by spinning a sphere in front of the viewpoint.
//
//
//Keys used by this viewer:
//-------------------------
//
//Left Mouse: Tumbles the virtual trackball.
//
//Middle Mouse:
//Ctrl + Left Mouse: Translate up, down, left and right.
//
//Ctrl + Middle Mouse:
//Left + Middle Mouse: Dolly in and out (gets closer to and further
//away from the object).
//
//<s> + click: Alternative to the Seek button. Press (but do not hold
//down) the <s> key, then click on a target object.
//
//Right Mouse: Open the popup menu.
//
//////////////////////////////////////////////////////////////////////////////

public class SoQtExaminerViewer extends SoQtFullViewer {
	
    // interaction modes offered by this viewer:
    enum ViewerModes {
        PICK_MODE,          // interacts directly with the OpenInventor scene
        SPIN_MODE,          // spin scene
        SPIN_MODE_ACTIVE,   //   ...when active
        PAN_MODE,           // pan scene
        PAN_MODE_ACTIVE,    //   ...when active
        DOLLY_MODE,         // zoom scene
        DOLLY_MODE_ACTIVE,  //   ...when active
        SEEK_MODE,          // zoom/move to clicked point
        INTERACT_MODE_COUNT
    };

    // viewer state variables
    final ViewerModes[]     modeTable = new ViewerModes[64]; // contains viewing mode depending on
                                   // keyboard modifiers and mouse buttons
    int             mode;
    Cursor[]        modeCursors;
    int             modeCursorCount;
    final SbVec2s         locator = new SbVec2s(); // mouse position
    boolean          firstBuild; // set false after buildWidget called once

    // point of rotation feedback vars
    boolean          feedbackFlag;
    double          feedbackSize;
    SoSeparator     feedbackRoot;
    SoSwitch        feedbackSwitch;
    SoTranslation   feedbackTransNode;
    SoScale         feedbackScaleNode;
    //static final String geometryBuffer;

    // variables used for doing spinning animation
    boolean          animationEnabled, animatingFlag;
    SoFieldSensor   animationSensor;
    Instant       lastMotionTime;

    // point of rotation pref sheet stuff
    double          feedbackSizeWheelValue;
    Composite        feedbackSizeWidgets;
    Button      enableSpinToggle; // SWT.CHECK
    Button      showRotPointToggle; // SWT.CHECK
    QDoubleSpinBox feedbackInput;
    SoQtThumbWheel     feedbackSizeWheel;

    boolean            nestingFlag;
    boolean            mouseInside; // set to true after mouse enter event
    
    // viewer toolbar actions
    protected Action        perspAction;

    
    //! Constructor/Destructor
	
    public SoQtExaminerViewer (BuildFlag flag) {
    	this(flag, Type.BROWSER);
    }
    public SoQtExaminerViewer (BuildFlag flag, Type type) {
    	this(flag, type, null);
    }
    public SoQtExaminerViewer (BuildFlag flag, Type type,
                    Composite parent) {
    	this(flag, type, parent, 0);
    }
    public SoQtExaminerViewer (BuildFlag flag, Type type,
                        Composite parent, int f) {
    	super(flag, type, parent, f);
        commonConstructor();
    }

    //! compatibility constructor
    public SoQtExaminerViewer (Composite parent, String name,
    boolean buildInsideParent, BuildFlag flag,
    Type type, boolean buildNow) {
    	super (flag, type, parent, SWT.NONE);

    //setObjectName (name);
    commonConstructor();
    if (buildNow) {
        buildWidget(0);
        updateCursor();
    }
}


    private void commonConstructor()
{
    nestingFlag = false;
    mouseInside = false;
    // init local vars
    modeCursorCount = ViewerModes.INTERACT_MODE_COUNT.ordinal();
    modeCursors     = new Cursor [modeCursorCount];
    setCursorShape (ViewerModes.PICK_MODE.ordinal(),  new Cursor(getDisplay(),0));
    setCursorShape (ViewerModes.SPIN_MODE.ordinal(),  SoQtIcons.getCursor (SoQtIcons.CursorNum.CURSOR_CURVED_HAND.ordinal()));
    setCursorShape (ViewerModes.PAN_MODE.ordinal(),   SoQtIcons.getCursor (SoQtIcons.CursorNum.CURSOR_FLAT_HAND.ordinal()));
    setCursorShape (ViewerModes.DOLLY_MODE.ordinal(), SoQtIcons.getCursor (SoQtIcons.CursorNum.CURSOR_POINTING_HAND.ordinal()));
    setCursorShape (ViewerModes.SEEK_MODE.ordinal(),  SoQtIcons.getCursor (SoQtIcons.CursorNum.CURSOR_TARGET.ordinal()));
    setCursorShape (ViewerModes.SPIN_MODE_ACTIVE.ordinal(),  getCursorShape (ViewerModes.SPIN_MODE.ordinal()));
    setCursorShape (ViewerModes.PAN_MODE_ACTIVE.ordinal(),   getCursorShape (ViewerModes.PAN_MODE.ordinal()));
    setCursorShape (ViewerModes.DOLLY_MODE_ACTIVE.ordinal(), getCursorShape (ViewerModes.DOLLY_MODE.ordinal()));

    // axis of rotation feedback vars
    feedbackFlag = false;
    feedbackRoot = null;
    feedbackSwitch = null;
    feedbackSize = 20.0;

    feedbackInput = null;
    feedbackSizeWheel = null;
    feedbackSizeWheelValue = 0.0;
    feedbackSizeWidgets = null;

    // init animation variables
    animationEnabled = true;
    animatingFlag = false;
    animationSensor = new
        SoFieldSensor(new SoSensorCB() {

			@Override
			public void run(Object data, SoSensor sensor) {
				animationSensorCB(data,sensor);				
			}        	
        }        		
        		, this);
//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_SENSORS") != null) {
        SoDebug.NamePtr("examinerSpinSensor", animationSensor);
    }
//#endif

    final String myTitle = "Examiner Viewer";
    setPopupMenuTitle (myTitle);
    //setWindowTitle (myTitle);
    //setWindowIconText (myTitle);

    setBottomWheelTitle ("Roty");
    setLeftWheelTitle ("Rotx");

    lastMotionTime = Instant.now().plusSeconds(-1);

    // this is probably unnecessary here, because a camera probably
    // doesn't exist at this moment - too lazy to think about it...
    ImageDescriptor icon = SoQtIcons.getIcon (
        getCameraController().isOrthographic()
        ? SoQtIcons.IconNum.ICON_ORTHO.ordinal()
        : SoQtIcons.IconNum.ICON_PERSP.ordinal());

    perspAction = new Action ( "Perspective", icon) {
    	public void run() {
    		SoQtExaminerViewer.this.toggleCameraTypeSlot();
    	}
    };

    restoreInteractions();
}
    

public void destructor()
{
    animationSensor.destructor();

    if (feedbackRoot != null) {
        feedbackRoot.unref();
    }
    super.destructor();
}




private void resetInteractions()
{
    for (int i=0;i<64;i++) {
        modeTable [i] = ViewerModes.PICK_MODE;
    }
}

private void restoreInteractions()
{
    resetInteractions();
    setInteraction ((SWT.BUTTON1 | SWT.BUTTON2), 0, ViewerModes.DOLLY_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON1) | (SWT.SHIFT), 0, ViewerModes.PAN_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON1) | (SWT.CTRL), 0, ViewerModes.PAN_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON1) | (SWT.SHIFT | SWT.CTRL), 0, ViewerModes.DOLLY_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON1), 0, ViewerModes.SPIN_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON2) | (SWT.CTRL), 0, ViewerModes.DOLLY_MODE_ACTIVE);
    setInteraction ((SWT.BUTTON2), 0, ViewerModes.PAN_MODE_ACTIVE);
    setInteraction ((SWT.SHIFT | SWT.CTRL), 0, ViewerModes.DOLLY_MODE);
    setInteraction ((SWT.SHIFT), 0, ViewerModes.PAN_MODE);
    setInteraction ((SWT.CTRL), 0, ViewerModes.PAN_MODE);
    setInteraction (0, 0, ViewerModes.SPIN_MODE);
    mode = isViewing() ? modeTable[0].ordinal() : ViewerModes.PICK_MODE.ordinal();
}


private void setInteraction (int inputState, int ignoreStateMask, ViewerModes mode)
{
    if (ignoreStateMask == 0) {
        modeTable [getModeIndex (inputState)] = mode;
    } else {
        // this is kind of overkill, since values are set more than once, but it's simple:
        for (int i=0;i<64;i++) {
            int state = (inputState & (0xffff ^ ignoreStateMask)) | (i & ignoreStateMask);
            modeTable [getModeIndex (state)] = mode;
        }
    }
}


private SoQtExaminerViewer.ViewerModes getInteraction (int inputState)
{
    return modeTable [getModeIndex (inputState)];
}


private int getModeIndex (int inputState)
{
    return ((inputState & SWT.BUTTON1)!= 0      ? 1  : 0) |
           ((inputState & SWT.BUTTON3)!= 0     ? 2  : 0) |
           ((inputState & SWT.BUTTON2)!= 0       ? 4  : 0) |
           ((inputState & SWT.SHIFT)!= 0   ? 8  : 0) |
           ((inputState & SWT.CONTROL)!= 0 ? 16 : 0) |
           ((inputState & SWT.ALT)!= 0     ? 32 : 0);
}



    // Set/get the cursor shape for a given viewing mode
public void
setCursorShape (int viewingMode, final Cursor shape)
{
    if (viewingMode >= ViewerModes.PICK_MODE.ordinal() && viewingMode < modeCursorCount && modeCursors != null) {
        // set new cursor shape for a_mode
        modeCursors [viewingMode] = shape;
        if (mouseInside && viewingMode == mode) {
            // update the currently shown cursor
            super.setGLCursor (shape);
        }
    }
}

// Set/get the cursor shape for a given viewing mode
public Cursor
getCursorShape (int viewingMode) 
{
    if (viewingMode >= ViewerModes.PICK_MODE.ordinal() && viewingMode < modeCursorCount && modeCursors != null) {
        return modeCursors [viewingMode];
    } else {
        // return default cursor
        return new Cursor(getDisplay(),0);
    }
}


protected void createViewerButtons(ToolBarManager parent)
{
    // create the default buttons
    super.createViewerButtons(parent);
    parent.add(perspAction);    
}

//The point of interest geometry description
private static final String geometryBuffer = ""
			+"#Inventor V2.0 ascii\n"
			+""
			+"Separator { "
			+"    PickStyle { style UNPICKABLE } "
			+"    LightModel { model BASE_COLOR } "
			+"    MaterialBinding { value PER_PART } "
			+"    DrawStyle { lineWidth 2 } "
			+"    Coordinate3 { point [0 0 0, 1 0 0, 0 1 0, 0 0 1] } "
			+"    BaseColor { rgb [1 0 0, 0 1 0, 0 0 1] } "
			+"    IndexedLineSet { coordIndex [1, 0, 2, -1, 0, 3] } "
			+"     "
			+"    LightModel { model PHONG } "
			+"    MaterialBinding { value OVERALL } "
			+"    Complexity { value .1 } "
			+"    Separator { "
			+"        Material { "
			+"            diffuseColor    [ 0.5 0 0 ] "
			+"            emissiveColor   [ 0.5 0 0 ] "
			+"        } "
			+"        Translation { translation 1 0 0 } "
			+"        RotationXYZ { axis Z angle -1.570796327 } "
			+"        Cone { bottomRadius .2 height .3 } "
			+"    } "
			+"    Separator { "
			+"        Material { "
			+"            diffuseColor    [ 0 0.5 0 ] "
			+"            emissiveColor   [ 0 0.5 0 ] "
			+"        } "
			+"        Translation { translation 0 1 0 } "
			+"        Cone { bottomRadius .2 height .3 } "
			+"    } "
			+"    Material { "
			+"        diffuseColor    [ 0 0 0.5 ] "
			+"        emissiveColor   [ 0 0 0.5 ] "
			+"    } "
			+"    Translation { translation 0 0 1 } "
			+"    RotationXYZ { axis X angle 1.570796327 } "
			+"    Cone { bottomRadius .2 height .3 } "
			+"} ";


protected void createFeedbackNodes()
{
    // make sure we havn't built this yet...
    if (feedbackRoot != null) { return; }

    feedbackRoot        = new SoSeparator(1);
    feedbackSwitch      = new SoSwitch(3);
    feedbackTransNode   = new SoTranslation();
    feedbackScaleNode   = new SoScale();
    feedbackRoot.ref();
    feedbackRoot.addChild( feedbackSwitch );
    feedbackSwitch.addChild( feedbackTransNode );
    feedbackSwitch.addChild( feedbackScaleNode );
    final SoInput in = new SoInput();
    in.setBuffer(geometryBuffer, geometryBuffer.length());
    final SoNode[] node = new SoNode[1];
    boolean ok = SoDB.read(in, node);
    if (ok && node != null) {
        feedbackSwitch.addChild(node[0]);
//#ifdef DEBUG
    } else {
        SoDebugError.post("SoQtExaminerViewer.createFeedbackNodes",
                            "couldn't read feedback axis geometry");
//#endif
    }
}


////////////////////////////////////////////////////////////////////////
//static callbacks stubs
////////////////////////////////////////////////////////////////////////

protected static void animationSensorCB (Object v, SoSensor sensor)
{
	if (!((SoQtExaminerViewer)v).getCameraController().doSpinAnimation()) {
		((SoQtExaminerViewer)v).stopAnimating();
}
}

protected void setFeedbackSizeWheelValue (double value)
{
	// grow/shrink the feedback based on the wheel rotation
	setFeedbackSize ((double) (feedbackSize) *
	Math.pow (12.0, (value - feedbackSizeWheelValue) / 360.0));
	feedbackSizeWheelValue = value;

}

//called when the viewer becomes visible/hidden - when hidden, make
//sure to temporary stop any ongoing animation (and restart it as soon
//as we become visible).
//
protected void hideEvent ()
{
	super.hideEvent ();

	// only do this if we are/were spinning....
	if (! animatingFlag) { return; }
	
	// if hidden, detach the field sensor, but don't change the
	// animatingFlag var to let us know we need to turn it back on
	// when we become visible....
	animationSensor.detach();
	animationSensor.unschedule();
}

protected void showEvent ()
{
	super.showEvent ();
	
	// only do this if we are/were spinning....
	if (! animatingFlag) { return; }
	
	// we now are visible again so attach the field sensor
	animationSensor.attach (viewerRealTime);
}

protected boolean isCurrentlyPicking()
{
return !isViewing() || (mode == ViewerModes.PICK_MODE.ordinal());
}
 
// Show/hide the point of rotation feedback, which only appears while
// in Viewing mode. (default OFF)
public void        setFeedbackVisibility(boolean insertFlag) {
	    // check for trivial return
    if (getCameraController().getCamera() == null || feedbackFlag == insertFlag) {
        feedbackFlag = insertFlag;
        return;
    }

    if (feedbackSizeWidgets != null) {
        feedbackSizeWidgets.setEnabled (insertFlag);
    }

    // find the camera parent to insert/remove the feedback root
    final SoSearchAction sa = new SoSearchAction();
    if (insertFlag) {
        sa.setNode(getCameraController().getCamera());
    } else {
        sa.setNode(feedbackRoot);
        sa.setSearchingAll(true); // find under OFF switches for removal
    }
    sa.apply(sceneRoot);
    SoFullPath fullPath = SoFullPath.cast( sa.getPath());
    if (fullPath == null) {
//#if DEBUG
        SoDebugError.post("SoQtExaminerViewer.setFeedbackVisibility",
                            insertFlag ? "ERROR: cannot find camera in graph" :
                            "ERROR: cannot find axis feedback in graph");
//#endif
        return;
    }
    SoGroup parent = (SoGroup ) fullPath.getNodeFromTail(1);

    feedbackFlag = insertFlag;

    // make sure the feedback has been built
    if (feedbackRoot == null) {
        createFeedbackNodes();
    }

    // inserts/remove the feedback axis group
    if (feedbackFlag) {
        int camIndex;

        // check to make sure that the camera parent is not a switch node
        // (VRML camera viewpoints are kept under a switch node). Otherwise
        // we will insert the feedback after the switch node.
        if (parent.isOfType(SoSwitch.getClassTypeId())) {
            SoNode switchNode = parent;
            parent = (SoGroup ) fullPath.getNodeFromTail(2);
            camIndex = parent.findChild(switchNode);
        } else {
            camIndex = parent.findChild(getCameraController().getCamera());
        }

        // return if feedback is already there (this should be an error !)
        if (parent.findChild(feedbackRoot) >= 0) {
            return;
        }

        // insert the feedback right after the camera+headlight (index+2)
        if (camIndex >= 0) {
            if (getCameraController().isHeadlight()) {
                parent.insertChild(feedbackRoot, camIndex+2);
            } else {
                parent.insertChild(feedbackRoot, camIndex+1);
            }
        }

        // make sure the feedback switch is turned to the correct state now
        // that the feedback root has been inserted in the scene
        feedbackSwitch.whichChild.setValue(viewingFlag ? SoSwitch.SO_SWITCH_ALL : SoSwitch.SO_SWITCH_NONE);
    } else {
        if (parent.findChild(feedbackRoot) >= 0) {
            parent.removeChild(feedbackRoot);
        }
    }
	
}
public boolean      isFeedbackVisible() { return feedbackFlag; }


// Set/get the point of rotation feeedback size in pixels (default 20).
public void        setFeedbackSize(double newSize) {
    if (feedbackSize == newSize) { return; }
    if (nestingFlag) { return; }
    nestingFlag = true;

    // assign new value and redraw (since it is not a field in the scene)
    feedbackSize = newSize;
    if (feedbackInput != null) {
        feedbackInput.setValue (newSize);
    }
    if (isFeedbackVisible() && isViewing()) {
        getSceneHandler().scheduleRedraw();
    }

    nestingFlag = false;		
	}
    public int         getFeedbackSize()          { return (int) feedbackSize; }


public void actualRedraw()
{
    // place the feedback at the focal point
    // ??? we really only need to do this when the camera changes
	SoCamera camera = getCameraController().getCamera();
    if (isViewing() && feedbackFlag && camera != null && feedbackRoot != null) {

        // adjust the position to be at the focal point
        final SbMatrix mx = new SbMatrix();
        mx.copyFrom(camera.orientation.getValue());
        final SbVec3f forward = new SbVec3f(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        feedbackTransNode.translation.setValue( camera.position.getValue().operator_add(
             forward.operator_mul(camera.focalDistance.getValue())));

        // adjust the size to be constant on the screen
        float height = 1.0f;
        if (getCameraController().isPerspective()) {
            float angle = ((SoPerspectiveCamera )camera).heightAngle.getValue();
            height = camera.focalDistance.getValue() * (float)Math.tan(angle/2);
        } else if (getCameraController().isOrthographic()) {
            height = ((SoOrthographicCamera )camera).height.getValue() / 2;
        }

        // ??? getGlxSize[1] == 0 the very first time, so return in that case
        // ??? else the redraws are 3 times slower from now on !! (alain)
        if (getGlxSize().getValue()[1] != 0) {
            float size = 2.0f * height * (float)feedbackSize / (float) (getGlxSize().getValue()[1]);
            feedbackScaleNode.scaleFactor.setValue(size, size, size);
        }
    }

    // have the base class draw the scene
    super.actualRedraw();
}



    
    // Stop animation, if it is occurring, and queuery if the viewer is
    // currently animating.
    public void        stopAnimating() {
        if (animatingFlag) {
            animatingFlag = false;
            animationSensor.detach();
            animationSensor.unschedule();
            interactiveCountDec();
        }
    }
    public boolean      isAnimating()                   { return animatingFlag; }



public void viewAll()
{
    // stop spinning
    if ( isAnimating() ) {
        stopAnimating();
    }

    // temporarily remove the feedback geometry
    if (feedbackFlag && isViewing() && feedbackSwitch != null) {
        feedbackSwitch.whichChild.setValue( SoSwitch.SO_SWITCH_NONE );
    }

    // call the base class
    getCameraController().viewAll();

    // now add the geometry back in
    if (feedbackFlag && isViewing() && feedbackSwitch != null) {
        feedbackSwitch.whichChild.setValue( SoSwitch.SO_SWITCH_ALL );
    }
}


public void resetToHomePosition()
{
    // stop spinning
    if ( isAnimating() ) {
        stopAnimating();
    }

    // call the base class
    getCameraController().resetToHomePosition();
}


public void setCamera(SoCamera newCamera, boolean createdCamera)
{
	SoCamera camera = getCameraController().getCamera();
	
    if (camera == newCamera) { return; }

    // set the right thumbwheel label and toggle button image based on
    // the camera type
    if (newCamera != null && (camera == null ||
        newCamera.getTypeId() != camera.getTypeId()))
    {
        if (newCamera.isOfType(SoOrthographicCamera.getClassTypeId())) {
            perspAction.setImageDescriptor(SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_ORTHO.ordinal()));
            setRightWheelTitle("Zoom");
        } else {
            perspAction.setImageDescriptor (SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_PERSP.ordinal()));
            setRightWheelTitle("Dolly");
        }
    }
    perspAction.setEnabled(newCamera != null && createdCamera);

    // detach feedback which depends on camera
    if ( feedbackFlag ) {
        setFeedbackVisibility(false);
        feedbackFlag = true;  // can later be turned on
    }

    // call parent class
    super.setCamera(newCamera, createdCamera);

    // attach feedback back on
    if ( feedbackFlag ) {
        feedbackFlag = false; // enables routine to be called
        setFeedbackVisibility(true);
    }
}


public void setViewing(boolean flag)
{
    if (flag == viewingFlag) { return; }

    // call the parent class
    super.setViewing(flag);

    updateViewerMode(-1, -1);

    // show/hide the feedback geometry based on the viewing state
    if (feedbackFlag && feedbackSwitch != null) {
        feedbackSwitch.whichChild.setValue(viewingFlag ? SoSwitch.SO_SWITCH_ALL : SoSwitch.SO_SWITCH_NONE);
    }
}


    
protected void processEvent (TypedEvent anyEvent, EventType type, final boolean[] isAccepted)
{
    SbVec2s raSize = getGlxSize();
    double   raViewScale = getGlxDevicePixelRatio();

    if (type == EventType.MOUSE_EVENT_MOUSE_DOWN)//QEvent.MouseButtonPress)
    {
        MouseEvent me = (MouseEvent) anyEvent;
        // remember start position of mouse drag
        locator.copyFrom( new SbVec2s((short)(me.x * raViewScale), (short)(raSize.getValue()[1] - (me.y * raViewScale))));

        if ((me.button == 1 || me.button == 2) &&
            mode == ViewerModes.SEEK_MODE.ordinal())
        {
            // handle temporary seek mode
            stopAnimating();
            getCameraController().seekToPoint(locator);
            //me.accept();
        } else {
            interactiveCountInc();
            stopAnimating();
        }
        updateViewerMode (me.stateMask & SWT.MODIFIER_MASK, (me.stateMask & SWT.BUTTON_MASK) | (me.button == 1 ? SWT.BUTTON1:0)| (me.button == 2 ? SWT.BUTTON2:0)| (me.button == 3 ? SWT.BUTTON3 :0) );
    } else if (type == EventType.KEY_EVENT_KEY_PRESSED/*QEvent.KeyPress*/) {
        KeyEvent ke = (KeyEvent )anyEvent;
        updateViewerMode (ke.stateMask & SWT.MODIFIER_MASK, ke.stateMask & SWT.BUTTON_MASK);
    } else if (type == EventType.MOUSE_EVENT_MOUSE_MOVE/*QEvent.MouseMove*/) {
        MouseEvent me = (MouseEvent) anyEvent;
        final SbVec2s pos = new SbVec2s((short)(me.x * raViewScale), (short)(raSize.getValue()[1] - (me.y * raViewScale)));
        if ((me.stateMask & SWT.BUTTON_MASK) ==0) {
            // update viewer mode while we are not dragging - this allows us
            // to change the viewing mode depending on the cursor location
            updateViewerMode (me.stateMask & SWT.MODIFIER_MASK, me.stateMask & SWT.BUTTON_MASK);
        }
        switch (ViewerModes.values()[mode]) {
            case SPIN_MODE_ACTIVE:
                lastMotionTime = Instant.now();
                getCameraController().spinCamera(pos);
                break;
            case PAN_MODE_ACTIVE:
            	getCameraController().panCamera(pos);
                break;
            case DOLLY_MODE_ACTIVE:
            	getCameraController().dollyCamera(pos);
                break;
            default:
                ;
        }
    } else if (type == EventType.MOUSE_EVENT_MOUSE_ENTER/*QEvent.Enter*/) {
    	MouseEvent me = (MouseEvent)anyEvent;
        updateViewerMode(me.stateMask & SWT.MODIFIER_MASK, me.stateMask & SWT.BUTTON_MASK);
        updateCursor();
        mouseInside = true;
    } else if (type == EventType.MOUSE_EVENT_MOUSE_EXIT/*QEvent.Leave*/) {
        super.unsetGLCursor();
        mouseInside = false;
    }

    //if (!anyEvent.isAccepted()) {
        super.processEvent (anyEvent, type, isAccepted);
    //}

    // updates for button and key releases will be done _after_ the mouse event might
    // have been delivered to the inventor scene - otherwise we might swallow some needed
    // events
    if (type == EventType.KEY_EVENT_KEY_RELEASED/*QEvent.KeyRelease*/) {
        KeyEvent ke = (KeyEvent )anyEvent;
        updateViewerMode (ke.stateMask & SWT.MODIFIER_MASK, ke.stateMask & SWT.BUTTON_MASK);
    } else if (type == EventType.MOUSE_EVENT_MOUSE_UP/*QEvent.MouseButtonRelease*/) {
        MouseEvent me = (MouseEvent) anyEvent;
        if ((me.button == 1/*Qt.LeftButton*/ || me.button == 2/*Qt.MidButton*/) &&
            mode == ViewerModes.SEEK_MODE.ordinal())
        {
            //me.accept();
        } else { //... ButtonRelease
            // check if we need to start spinning
            if (mode == ViewerModes.SPIN_MODE_ACTIVE.ordinal() && animationEnabled &&
                // Qt is missing a mechanism to obtain the time when
                // an event was generated (in contrast to processing time)
                lastMotionTime.plusMillis(100).isAfter(Instant.now()))
            {
                animatingFlag = true;
                animationSensor.attach(viewerRealTime);
                interactiveCountInc(); // will be decreased again in stopAnimating
            }
            interactiveCountDec();
        }
        updateViewerMode (me.stateMask & SWT.MODIFIER_MASK, me.stateMask & SWT.BUTTON_MASK & (me.button == 1 ? ~SWT.BUTTON1:SWT.BUTTON_MASK) & (me.button == 2 ? ~SWT.BUTTON2:SWT.BUTTON_MASK) & (me.button == 3 ? ~SWT.BUTTON3 :SWT.BUTTON_MASK));
    }
}


private void updateViewerMode (int modifiers, int buttons)
{
    // obtain modifiers and buttons if they weren't given:
    if (modifiers == -1) {
        modifiers = keyboardModifiers();
    }
    if (buttons == -1) {
        buttons = mouseButtons();
    }
    if (buttons == 0 && getCameraController().isSeekMode()) {
        // seek mode needs special handling because it's only temporary
        switchMode (ViewerModes.SEEK_MODE.ordinal(), buttons);
    } else if (!isViewing()) {
        switchMode (ViewerModes.PICK_MODE.ordinal(), buttons);
    } else {
        if (isAltSwitchingEnabled()) {
            // since 'alt' switches between viewing and picking, we remove
            // 'alt' from the modifiers, since we want to handle it as if
            // it wasn't pressed:
            modifiers = (modifiers | SWT.ALT) ^ SWT.ALT;
        }
        switchMode (getInteraction (modifiers | buttons).ordinal(), buttons);
    }
}

private int keyboardModifiers() {
	// TODO Auto-generated method stub
	return 0;
}
private int mouseButtons() {
	// TODO Auto-generated method stub
	return 0;
}
private void switchMode(int newMode, int buttons)
{
    if (mode == newMode) { return; }  //<- This wasn't there before - is this wrong?

    // assing new mode
    int prevMode = mode;
    mode = newMode;

    // update the cursor
    updateCursor();

    // switch to new viewer mode
    switch (ViewerModes.values()[newMode]) {
        case PICK_MODE:
            if (mouseInside) {
                //int buttons = QApplication::mouseButtons();
                if (prevMode != ViewerModes.SEEK_MODE.ordinal()) {
                    // in SEEK_MODE the interactive count isn't increased
                    // by a mouse press!
                    if ((buttons & SWT.BUTTON1)!=0/*Qt::LeftButton*/) { interactiveCountDec(); }
                    if ((buttons & SWT.BUTTON2)!=0/*Qt::MidButton*/)  { interactiveCountDec(); }
                }
                if ((buttons & SWT.BUTTON3)!=0/*Qt::RightButton*/) { interactiveCountDec(); }
            }
            stopAnimating();
            break;

        case DOLLY_MODE_ACTIVE:
        case SPIN_MODE_ACTIVE:
        case PAN_MODE_ACTIVE:
            getCameraController().startDrag(locator);
            break;
	case DOLLY_MODE:
		break;
	case INTERACT_MODE_COUNT:
		break;
	case PAN_MODE:
		break;
	case SEEK_MODE:
		break;
	case SPIN_MODE:
		break;
	default:
		break;
    }
}


private void updateCursor()
{
    // the viewer cursor are not enabled, then we don't set a new cursor.
    // Instead erase the old viewer cursor.
    if (! cursorEnabledFlag) {
        super.unsetGLCursor();
        return;
    } else {
        super.setGLCursor (getCursorShape (mode));
    }
}

public void setGLCursor ( Cursor cursor)
{
    setCursorShape (ViewerModes.PICK_MODE.ordinal(), cursor);
}

public void unsetGLCursor()
{
    setCursorShape (ViewerModes.PICK_MODE.ordinal(), new Cursor(getDisplay(),0));
}


protected void setSeekMode(boolean flag/*, int modifiers, int buttons*/)
{
    if ( !isViewing() ) { return; }

    // stop spinning
    if (isAnimating()) {
        stopAnimating();
    }

    // call the base class
    super.setSeekMode(flag/*, modifiers, buttons*/);

    updateViewerMode(-1, -1);
    updateCursor();
}


}
