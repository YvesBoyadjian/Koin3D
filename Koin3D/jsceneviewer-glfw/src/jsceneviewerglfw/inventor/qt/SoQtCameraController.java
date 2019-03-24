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

package jsceneviewerglfw.inventor.qt;

import static org.lwjgl.glfw.GLFW.*;

import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoOrthographicCamera;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoResetTransform;
import jscenegraph.database.inventor.nodes.SoRotation;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.database.inventor.projectors.SbSphereSheetProjector;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoNodeSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jsceneviewerglfw.inventor.qt.SoQtCameraController;

/**
 * @author Yves Boyadjian
 *
 */
//!  This class encapsulates the camera (and headlight) handling functions
//!  for SoQtViewer and derived classes.
public abstract class SoQtCameraController {

	// size of the rotation buffer, which is used to animate the spinning ball.
	public static final int ROT_BUFF_SIZE = 3;

    //! An EDITOR viewer will create a camera under the user supplied scene
    //! graph (specified in setSceneGraph()) if it cannot find one in the
    //! scene and will leave the camera behind when supplied with a new scene.
    //!
    //! A BROWSER viewer will also create a camera if it cannot find one in
    //! the scene, but will place it above the scene graph node (camera will
    //! not appear in the user supplied scene graph), and will automatically
    //! remove it when another scene is supplied to the viewer.
    public enum Type {
        BROWSER, // default viewer type
        EDITOR
    };
    

    //! variables used for interpolating seek animations
    protected float           seekDistance;
    protected boolean          seekDistAsPercentage; //! percentage/absolute flag
    protected boolean          computeSeekVariables;
    protected final SbVec3f         seekPoint = new SbVec3f();
	protected final SbVec3f seekNormal = new SbVec3f();
    protected final SbRotation      oldCamOrientation = new SbRotation();
	protected final SbRotation newCamOrientation = new SbRotation();
    protected final SbVec3f         oldCamPosition = new SbVec3f(), newCamPosition = new SbVec3f();

    //! global vars
    protected Type            type;
    protected SoCamera        camera;            //! camera being edited
    protected static SoSFTime realTime;          //! pointer to "realTime" global field

    //! local tree variables
    protected SoGroup         sceneRoot;         //! root node given to the RA
    public SoNode          sceneGraph;        //! user supplied scene graph
    protected int             cameraIndex;        //! where to insert the camera in the sceneRoot in BROWSER mode

    //! auto clipping vars and routines
    protected boolean          autoClipFlag;
    protected float           minimumNearPlane;   //! minimum near plane as percentage of far
    protected SoGetBoundingBoxAction autoClipBboxAction;
    
    	//! current state vars
    private final SoType          cameraType = new SoType();

    private final SbVec2s         sceneSize = new SbVec2s();

        //! camera original values, used to restore the camera
    private boolean          createdCamera;
    private final SbVec3f         origPosition = new SbVec3f();
    private final SbRotation      origOrientation = new SbRotation();
    private float           origNearDistance;
    private float           origFarDistance;
    private float           origFocalDistance;
    private float           origHeight;

        //! seek animation vars
    private boolean          seekModeFlag;       //! true when seek turned on externally
    private SoFieldSensor   seekAnimationSensor;
    private boolean          detailSeekFlag;
    private float           seekAnimTime;
    private final SbTime          seekStartTime = new SbTime();

        //! headlight variables
    private SoDirectionalLight  headlightNode;
    private SoGroup             headlightGroup;
    private SoGroup             headlightParent;
    private SoRotation          headlightRot;
    private boolean              headlightFlag;  //! true when headlight in turned on
    private SoNodeSensor        headlightParentSensor;

    private final SbVec2s         startPos = new SbVec2s(); // starting mouse position
    private SbSphereSheetProjector sphereSheet;
    private float           zoomSensitivity;

        // variables used for doing spinning animation
    private SbRotation[]      rotBuffer;
    private int             firstIndex, lastIndex;
    private final SbRotation      averageRotation = new SbRotation();
    private boolean          computeAverage;

        // camera panning vars
    private final SbVec3f         locator3D = new SbVec3f();
    private final SbPlane         focalplane = new SbPlane();

    
    public SoQtCameraController(Type t) {
    	sceneSize.copyFrom( new SbVec2s((short)0,(short)0));
    
        // init local vars
        type = t;
        sceneGraph = null;
        sceneRoot = null;
        cameraIndex = 0;
        camera = null;
        cameraType.copyFrom(SoPerspectiveCamera.getClassTypeId());
        createdCamera = false;

        // init auto clipping stuff
        autoClipFlag = true;
        minimumNearPlane = 0.001f;
        autoClipBboxAction = new SoGetBoundingBoxAction(new SbViewportRegion(new SbVec2s((short)1,(short)1)));  // ??? no valid size yet

        // init seek animation variables
        seekDistance = 50.0f;
        seekDistAsPercentage = true;
        seekModeFlag = false;
        detailSeekFlag = true;
        seekAnimTime = 2.0f;
        seekAnimationSensor = new SoFieldSensor(SoQtCameraController::seekAnimationSensorCB, this);

        // headlightGroup - we have a rotation which keeps the headlight
        // moving whenever the camera moves,  and a reset xform so
        // that the rest of the scene is not affected by the first rot.
        // these leaves the direction field in the headlight open for the
        // user to edit, allowing for the direction to change w.r.t. the camera.
        headlightGroup  = new SoGroup(3);
        headlightRot    = new SoRotation();
        headlightNode   = new SoDirectionalLight();
        headlightGroup.ref();
        headlightGroup.addChild(headlightRot);
        headlightGroup.addChild(headlightNode);
        headlightGroup.addChild(new SoResetTransform());
        headlightNode.direction.setValue(new SbVec3f(.2f, -.2f, -.9797958971f));
        headlightFlag = true;
        headlightParent = null;
        headlightParentSensor = new SoNodeSensor();
        headlightParentSensor.setDeleteCallback(SoQtCameraController::headlightParentDeletedCB, this);

        rotBuffer = new SbRotation[ROT_BUFF_SIZE];
        for(int i=0;i<ROT_BUFF_SIZE;i++) rotBuffer[i] = new SbRotation();

        // init the projector class
        final SbViewVolume vv = new SbViewVolume();
        vv.ortho(-1, 1, -1, 1, -10, 10);
        sphereSheet = new SbSphereSheetProjector();
        sphereSheet.setViewVolume( vv );
        sphereSheet.setSphere( new SbSphere( new SbVec3f(0, 0, 0), .7f) );

        zoomSensitivity = 1.0f;
    }
    

public void destructor()
{
    // delete everything
    sphereSheet = null;
    rotBuffer = null;
    seekAnimationSensor.destructor(); seekAnimationSensor = null;
    headlightParentSensor.destructor(); headlightParentSensor = null;
    autoClipBboxAction.destructor(); autoClipBboxAction = null;
    headlightGroup.unref();
}



    //! Set scene root node and at which index to insert the camera in BROWSER mode
    public void setSceneRoot(SoGroup root, int camIndex) {
        sceneRoot = root;
        cameraIndex = camIndex;    	
    }


public void setSceneGraph(SoNode newScene)
{
    // detach everything that depends on the old sceneGraph
    if ( sceneGraph != null ) {
        setCamera(null, false);
        sceneRoot.removeChild(sceneGraph);
    }

    sceneGraph = newScene;

    // now assign the new sceneGraph, find or create the new camera
    // and attach things back.
    if ( sceneGraph != null ) {
        sceneRoot.addChild(sceneGraph);

        // search for first camera in the scene
        final SoSearchAction sa = new SoSearchAction();
        sa.setType(SoCamera.getClassTypeId());
        sa.setSearchingAll(false); // don't look under off switches
        sa.apply(sceneGraph);

        SoCamera newCamera = null;
        if (sa.getPath() != null) {
            newCamera = (SoCamera )( SoFullPath.cast(sa.getPath())).getTail();
        }

        // if no camera found create one of the right kind...
        if ( newCamera == null ) {

            newCamera = (SoCamera) cameraType.createInstance();
            if (newCamera == null) {
//#ifdef DEBUG
                SoDebugError.post("SoQtCameraController::setSceneGraph",
                    "unknown camera type!");
//#endif
                // ??? what should we do here ?
                cameraType.copyFrom(SoPerspectiveCamera.getClassTypeId());
                newCamera = new SoPerspectiveCamera();
            }

            if (type == SoQtCameraController.Type.BROWSER) {
                // add camera after drawstyle stuff
                sceneRoot.insertChild(newCamera, cameraIndex);
            } else {
                // check to make sure scene starts with at least a group node
                if ( sceneGraph.isOfType(SoGroup.getClassTypeId()) ) {
                    ((SoGroup )sceneGraph).insertChild(newCamera, 0);
                } else {
                    // make scene start with a group node
                    SoGroup group = new SoGroup();
                    group.addChild(newCamera);
                    group.addChild(sceneGraph);
                    sceneRoot.addChild(group);
                    sceneRoot.removeChild(sceneGraph);
                    sceneGraph = group;
                }
            }

            newCamera.viewAll(sceneGraph, new SbViewportRegion(sceneSize));
            setCamera(newCamera, true);
        } else {
            setCamera(newCamera, false);
        }
        sa.destructor();
    }
}

//! Set and get the edited camera. setCamera() is only needed if the
//! first camera found when setSceneGraph() is called isn't the one
//! the user really wants to edit.
//! cameraCreated should be true if the camera was only created for this viewer.

public abstract void setCamera(SoCamera newCamera, boolean cameraCreated);

public void doSetCamera(SoCamera newCamera, boolean cameraCreated)
{
    // check for trivual return
    if (camera == newCamera) {
        return;
    }

    //
    // detach everything that depended on the old camera
    //
    if ( camera != null ) {

        if (headlightFlag) {
            setHeadlight(false);
            headlightFlag = true;  // can later be turned on
        }

        // remove the camera if we created one outside of the
        // scene graph.
        if (this.createdCamera && type == SoQtCameraController.Type.BROWSER) {
            if (sceneRoot.findChild(camera) >= 0) {
                sceneRoot.removeChild(camera);
            }
            this.createdCamera = false;
        }

        camera.unref();
    }

    camera = newCamera;

    //
    // attach everything that depends on the new camera
    //
    if ( camera != null) {
        camera.ref();
        this.createdCamera = cameraCreated;

        if (headlightFlag) {
            headlightFlag = false;  // enables the routine to be called
            setHeadlight(true);
        }

        saveHomePosition();
    }
}

public SoCamera       getCamera()        { return camera; }

//! This routine will toggle the current camera from perspective to
//! orthographic, and from orthographic back to perspective.
public void    toggleCameraType() {
    if (camera == null) {
        return;
    }

    // create the camera of the opposite kind and compute the wanted height
    // or heightAngle of the new camera.
    SoCamera newCam;
    if (camera.isOfType(SoPerspectiveCamera.getClassTypeId())) {
        float angle = ((SoPerspectiveCamera )camera).heightAngle.getValue();
        float height = camera.focalDistance.getValue() * (float)Math.tan(angle/2);
        newCam = new SoOrthographicCamera();
        ((SoOrthographicCamera )newCam).height.setValue( 2 * height);
    } else if (camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        float height = ((SoOrthographicCamera )camera).height.getValue() / 2;
        float angle = (float)Math.atan(height / camera.focalDistance.getValue());
        newCam = new SoPerspectiveCamera();
        ((SoPerspectiveCamera )newCam).heightAngle.setValue( 2 * angle);
    } else {
//#ifdef DEBUG
        SoDebugError.post("SoQtCameraController::toggleCameraType", "unknown camera type!");
//#endif
        return;
    }

    newCam.ref();

    // copy common stuff from the old to the new camera
    newCam.viewportMapping.setValue( camera.viewportMapping.getValue());
    newCam.position.setValue( camera.position.getValue());
    newCam.orientation.setValue( camera.orientation.getValue());
    newCam.aspectRatio.setValue( camera.aspectRatio.getValue());
    newCam.focalDistance.setValue( camera.focalDistance.getValue());

    // search for the old camera and replace it by the new camera
    final SoSearchAction sa = new SoSearchAction();
    sa.setNode(camera);
    sa.apply(sceneRoot);
    SoFullPath fullCamPath = SoFullPath.cast( sa.getPath());
    if (fullCamPath != null) {
        SoGroup parent = (SoGroup )fullCamPath.getNode(fullCamPath.getLength() - 2);
        parent.insertChild(newCam, parent.findChild(camera));
        SoCamera oldCam = camera;
        setCamera(newCam, true);

        // remove the old camera if it is still there (setCamera() might
        // have removed it) and set the created flag to true (for next time)
        if (parent.findChild(oldCam) >= 0) {
            parent.removeChild(oldCam);
        }
//#ifdef DEBUG
    } else {
        SoDebugError.post("SoQtCameraController::toggleCameraType", "camera not found!");
//#endif
    }
    newCam.unref();

}


public void arrowKeyPressed (int key)
{
    if (camera == null) {
        return;
    }

    // get the camera near plane height value
    float dist = 0.0f;
    if (camera.isOfType(SoPerspectiveCamera.getClassTypeId())) {
        float angle = ((SoPerspectiveCamera )camera).heightAngle.getValue();
        float length = camera.nearDistance.getValue();
        dist = length * (float)Math.tan(angle);
    } else if (camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        dist = ((SoOrthographicCamera )camera).height.getValue();
    }
    dist /= 2.0;

    // get camera right/left/up/down direction
    final SbMatrix mx = new SbMatrix();
    mx.copyFrom( camera.orientation.getValue().getMatrix());
    final SbVec3f dir = new SbVec3f();
    switch(key) {
        case GLFW_KEY_UP:
            dir.setValue(mx.getValue()[1][0], mx.getValue()[1][1], mx.getValue()[1][2]);
            break;
        case GLFW_KEY_DOWN:
            dir.setValue(-mx.getValue()[1][0], -mx.getValue()[1][1], -mx.getValue()[1][2]);
            break;
        case GLFW_KEY_RIGHT:
            dir.setValue(mx.getValue()[0][0], mx.getValue()[0][1], mx.getValue()[0][2]);
            dist *= camera.aspectRatio.getValue();
            break;
        case GLFW_KEY_LEFT:
            dir.setValue(-mx.getValue()[0][0], -mx.getValue()[0][1], -mx.getValue()[0][2]);
            dist *= camera.aspectRatio.getValue();
            break;
    }

    // finally reposition the camera
    camera.position.setValue(camera.position.getValue().operator_add(dir.operator_mul(dist)));
}



public void viewAll()
{
    if ( camera != null ) {
        camera.viewAll(sceneGraph, new SbViewportRegion(sceneSize));
    }
}


public void updateHeadlight()
{
    // update the headlight if necessary
    if (headlightFlag && camera != null) {
        // only update if the value is different, otherwise we get ping-pong effects
        // if the same camera is used for different viewers
        if (headlightRot.rotation.getValue() != camera.orientation.getValue()) {
            headlightRot.rotation.setValue(camera.orientation.getValue());
        }
    }
}



public void saveHomePosition()
{
    if (camera == null) {
        return;
    }

    origPosition.copyFrom(camera.position.getValue());
    origOrientation.copyFrom(camera.orientation.getValue());
    origNearDistance = camera.nearDistance.getValue();
    origFarDistance = camera.farDistance.getValue();
    origFocalDistance = camera.focalDistance.getValue();

    // save camera height (changed by zooming)
    if (camera.isOfType(SoPerspectiveCamera.getClassTypeId())) {
        origHeight = ((SoPerspectiveCamera )camera).heightAngle.getValue();
    } else if (camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        origHeight = ((SoOrthographicCamera )camera).height.getValue();
    }
}


public void resetToHomePosition()
{
    if (camera == null) {
        return;
    }

    camera.position.setValue( origPosition);
    camera.orientation.setValue( origOrientation);
    camera.nearDistance.setValue( origNearDistance);
    camera.farDistance.setValue( origFarDistance);
    camera.focalDistance.setValue( origFocalDistance);

    // restore camera height (changed by zooming)
    if (camera.isOfType(SoPerspectiveCamera.getClassTypeId())) {
        ((SoPerspectiveCamera )camera).heightAngle.setValue(origHeight);
    } else if (camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        ((SoOrthographicCamera )camera).height.setValue(origHeight);
    }
}


public boolean seekToPoint(final SbVec2s mouseLocation)
{
    if (camera == null) {
        setSeekMode(false);
        return false;
    }

    // do the picking
    // Use assignment notation to disambiguate from expression (edison)
    final SoRayPickAction pick = new SoRayPickAction(new SbViewportRegion(sceneSize));
    pick.setPoint(mouseLocation);
    pick.setRadius(1.0f);
    pick.setPickAll(false); // pick only the closest object
    pick.apply(sceneRoot);

    // makes sure something got picked
    SoPickedPoint pp = pick.getPickedPoint();
    if ( pp == null ) {
        setSeekMode(false);
        return false;
    }

    // Get picked point and normal if detailtSeek
    if (detailSeekFlag) {

        seekPoint.copyFrom( pp.getPoint());
        seekNormal.copyFrom( pp.getNormal());

        // check to make sure normal points torward the camera, else
        // flip the normal around
        if ( seekNormal.dot(camera.position.getValue().operator_minus(seekPoint)) < 0 ) {
            seekNormal.negate();
        }
    }
    // else get object bounding box as the seek point and the camera
    // orientation as the normal.
    else {
        // get center of object's bounding box
        // Use assignment notation to disambiguate from expression (edison)
        final SoGetBoundingBoxAction bba = new SoGetBoundingBoxAction(new SbViewportRegion(sceneSize));
        bba.apply(pp.getPath());
        final SbBox3f bbox = bba.getBoundingBox();
        seekPoint.copyFrom( bbox.getCenter());

        // keep the camera oriented the same way
        final SbMatrix mx = new SbMatrix();
        mx.copyFrom( camera.orientation.getValue());
        seekNormal.setValue(mx.getValue()[2][0], mx.getValue()[2][1], mx.getValue()[2][2]);
    }

    // now check if animation sensor needs to be scheduled
    computeSeekVariables = true;
    if (seekAnimTime == 0) {
        // jump to new location, no animation needed
        interpolateSeekAnimation(1.0f);
    } else {
        // schedule sensor and call viewer start callbacks
        if ( seekAnimationSensor.getAttachedField() == null ) {
            seekAnimationSensor.attach(getRealTime());
            seekAnimationSensor.schedule();
            animationStarted();
        }

        seekStartTime.copyFrom(getRealTime().getValue());
    }

    return true;    // successfull
}



public SoNode getSceneGraph()
{
    return sceneGraph;
}

public void            setSceneSize(final SbVec2s size) { sceneSize.copyFrom( size); }


private static void seekAnimationSensorCB(Object p, SoSensor sensor)
{
    SoQtCameraController v = (SoQtCameraController )p;

    // get the time difference
    SbTime time = getRealTime().getValue();
    float sec = (float)((time.operator_minus(v.seekStartTime)).getValue());
    if (sec == 0.0) {
        sec = 1.0f/72.0f; // at least one frame (needed for first call)
    }
    float t = (sec / v.seekAnimTime);

    // check to make sure the values are correctly clipped
    if (t > 1.0) {
        t = 1.0f;
    } else if ((1.0 - t) < 0.0001) {
        t = 1.0f; // this will be the last one...
    }

    // call subclasses to interpolate the animation
    v.interpolateSeekAnimation(t);

    // stops seek if this was the last interval
    if (t == 1.0) {
        v.setSeekMode(false);
    }
}


	private static void headlightParentDeletedCB(Object p, SoSensor sensor)
{
  // headlightParent has been deleted
  ((SoQtCameraController)p).headlightParent = null;
}


private static SoSFTime getRealTime()
{
    if ( realTime == null) {
        realTime = (SoSFTime ) SoDB.getGlobalField(new SbName("realTime"));
    }
    return realTime;
}

//! Externally set the viewer into/out off seek mode (default OFF). Actual
//! seeking will not happen until the viewer decides to (ex: mouse click).
//!
//! Note: setting the viewer out of seek mode while the camera is being
//! animated will stop the animation to the current location.
	public abstract void    setSeekMode(boolean flag);
	public void    doSetSeekMode(boolean flag) {
	    // check if seek is being turned off while seek animation is happening
	    if ( !flag && seekAnimationSensor.getAttachedField() != null ) {
	        seekAnimationSensor.detach();
	        seekAnimationSensor.unschedule();
	        animationFinished();
	    }

	    seekModeFlag = flag;		
	}
	public boolean          isSeekMode()                { return seekModeFlag; }

    protected abstract void    animationStarted();
    protected abstract void    animationFinished();

    //! Subclasses CAN redefine this to interpolate camera position/orientation
    //! while the seek animation is going on (called by animation sensor).
    //! The parameter t is a [0,1] value corresponding to the animation percentage
    //! completion. (i.e. a value of 0.25 means that animation is only 1/4 of the way
    //! through).
    protected void    interpolateSeekAnimation(float t) {
        if (camera == null) {
            return;
        }

        // check if camera new and old position/orientation have already
        // been computed.
        if (computeSeekVariables) {
            final SbMatrix mx = new SbMatrix();
            final SbVec3f viewVector = new SbVec3f();

            // save camera starting point
            oldCamPosition.copyFrom(camera.position.getValue());
            oldCamOrientation.copyFrom(camera.orientation.getValue());

            // compute the distance the camera will be from the seek point
            // and update the camera focalDistance.
            float dist;
            if ( seekDistAsPercentage ) {
                final SbVec3f seekVec = new SbVec3f(seekPoint.operator_minus(camera.position.getValue()));
                dist = seekVec.length() * (seekDistance / 100.0f);
            } else {
                dist = seekDistance;
            }
            camera.focalDistance.operator_assign(dist);

            // let subclasses have a chance to redefine what the
            // camera final orientation should be.
            computeSeekFinalOrientation();

            // find the camera final position based on orientation and distance
            mx.operator_assign(newCamOrientation);
            viewVector.setValue(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
            newCamPosition.copyFrom(seekPoint.operator_minus(viewVector.operator_mul(dist)));

            computeSeekVariables = false;
        }

        // Now position the camera according to the animation time

        // use and ease-in ease-out approach
        float cos_t = 0.5f - 0.5f * (float)Math.cos(t * SbBasic.M_PI);

        // get camera new rotation
        camera.orientation.operator_assign(SbRotation.slerp(oldCamOrientation, newCamOrientation, cos_t));

        // get camera new position
        camera.position.operator_assign(oldCamPosition.operator_add((newCamPosition.operator_minus(oldCamPosition)).operator_mul(cos_t)));
    	
    }
    

public void adjustCameraClippingPlanes()
{
    if (camera == null) {
        return;
    }

    // get the scene bounding box
    autoClipBboxAction.setViewportRegion(new SbViewportRegion(sceneSize));
    autoClipBboxAction.apply(sceneRoot);

    final SbXfBox3f xfbbox = new SbXfBox3f(autoClipBboxAction.getXfBoundingBox());

    // get camera transformation and apply to xfbbox
    // to align the bounding box to camera space.
    // This will enable us to simply use the z values of the
    // transformed bbox for near and far plane values.
    final SbMatrix mx = new SbMatrix();
    mx.setTranslate(camera.position.getValue().operator_minus());
    xfbbox.transform(mx);
    mx.copyFrom(camera.orientation.getValue().inverse().getMatrix());
    xfbbox.transform(mx);

    // get screen align bbox and figure the near and far plane values
    SbBox3f bbox = xfbbox.project();
    // take negative value and opposite to what one might think
    // because the camera points down the -Z axis
    float farDist = - bbox.getMin().getValueRead()[2];
    float nearDist = - bbox.getMax().getValueRead()[2];

    // scene is behind the camera so don't change the planes
    if (farDist < 0) {
        return;
    }

    // check for minimum near plane value (Value will be negative
    // when the camera is inside the bounding box).
    // Note: there needs to be a minimum near value for perspective
    // camera because of zbuffer resolution problem (plus the values
    // has to be positive). There is no such restriction for
    // an Orthographic camera (you can see behind you).
    if (! camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        if (nearDist < (minimumNearPlane * farDist)) {
            nearDist = minimumNearPlane * farDist;
        }
    }

    // give the near and far distances a little bit of slack in case
    // the object lies against the bounding box, otherwise the object
    // will be poping in and out of view.
    // (example: a cube is the same as it's bbox)
    nearDist *= 0.999;
    farDist *= 1.001;

    // finally assign camera plane values
    if (camera.nearDistance.getValue() != nearDist)
        camera.nearDistance.setValue(nearDist);
    if (camera.farDistance.getValue() != farDist)
        camera.farDistance.setValue(farDist);
}



protected void computeSeekFinalOrientation()
{
    final SbMatrix mx = new SbMatrix();
    final SbVec3f viewVector = new SbVec3f();

    // find the camera final orientation
    if ( isDetailSeek() ) {

        // get the camera new orientation
        mx.operator_assign( camera.orientation.getValue());
        viewVector.setValue(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        final SbRotation changeOrient = new SbRotation();
        changeOrient.setValue(viewVector, seekPoint.operator_minus( camera.position.getValue()));
        newCamOrientation.copyFrom( camera.orientation.getValue().operator_mul( changeOrient));
    } else {
        newCamOrientation.copyFrom( camera.orientation.getValue());
    }
}

//! Seek methods
//!
//! Routine to determine whether or not to orient camera on
//! picked point (detail on) or center of the object's bounding box
//! (detail off). Default is detail on.
public void    setDetailSeek(boolean onOrOff)   { detailSeekFlag = onOrOff; };
public boolean          isDetailSeek()              { return detailSeekFlag; }


public void setHeadlight(boolean insertFlag)
{
    // check for trivial return
    if (camera == null || headlightFlag == insertFlag) {
        headlightFlag = insertFlag;
        return;
    }

    if (headlightParent != null) {
        // remove previous headlight
        if (headlightParent.findChild(headlightGroup) >= 0) {
            headlightParent.removeChild(headlightGroup);
        }
        headlightParentSensor.detach();
        headlightParent = null;
    }

    //
    // find the camera parent to insert the headlight
    //
    if (insertFlag) {
        final SoSearchAction sa = new SoSearchAction();
        sa.setNode(camera);
        sa.apply(sceneRoot);
        SoFullPath fullPath = SoFullPath.cast( sa.getPath());
        if (fullPath == null) {
    //#if DEBUG
            SoDebugError.post("SoQtCameraController::setHeadlight",
                               "ERROR: cannot find camera in graph");
    //#endif
            // don't update the headlightFlag:
            return;
        }
        headlightParent = (SoGroup ) fullPath.getNodeFromTail(1);

        //
        // inserts the headlight group node
        //
        int camIndex;

        // check to make sure that the camera parent is not a switch node
        // (VRML camera viewpoints are kept under a switch node). Otherwise
        // we will insert the headlight right after the switch node.
        if (headlightParent.isOfType(SoSwitch.getClassTypeId())) {
            SoNode switchNode = headlightParent;
            headlightParent = (SoGroup ) fullPath.getNodeFromTail(2);
            camIndex = headlightParent.findChild(switchNode);
        } else {
            camIndex = headlightParent.findChild(camera);
        }
        // so we get notified if the headlightParent goes away:
        headlightParentSensor.attach(headlightParent);

        if (headlightParent.findChild(headlightGroup) < 0) {
          // insert the light group right after the camera
          if (camIndex >= 0) {
              headlightParent.insertChild(headlightGroup, camIndex+1);
          }
        }
        sa.destructor();
    }

    // we must still update the headlightFlag
    headlightFlag = insertFlag;
}

public boolean          isHeadlight()       { return headlightFlag; }

//! Set and get the auto clipping plane. When auto clipping is ON, the
//! camera near and far planes are dynamically adjusted to be as tight
//! as possible (least amount of stuff is clipped). When OFF, the user
//! is expected to manually set those planes within the preference sheet.
//! (default is ON).
public void            setAutoClipping(boolean flag) {
    autoClipFlag = flag;	
}

public boolean          isAutoClipping() { return autoClipFlag; }


	public boolean            isPerspective() {
		  return (camera != null) && camera.isOfType(SoPerspectiveCamera.getClassTypeId());		
	}
	public boolean            isOrthographic() {
		  return (camera != null) && camera.isOfType(SoOrthographicCamera.getClassTypeId());		
	}


public void rotateCamera(final SbRotation rot)
{
    if (camera == null) { return; }

    // get center of rotation
    SbRotation camRot = new SbRotation(camera.orientation.getValue());
    float radius = camera.focalDistance.getValue();
    final SbMatrix mx = new SbMatrix();
    mx.copyFrom(camRot.getMatrix());
    final SbVec3f forward = new SbVec3f( -mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
    final SbVec3f center = camera.position.getValue().operator_add(forward.operator_mul(radius));

    // apply new rotation to the camera
    camRot = rot.operator_mul(camRot);
    camera.orientation.setValue(camRot);

    // reposition camera to look at pt of interest
    mx.copyFrom(camRot.getMatrix());
    forward.setValue( -mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
    camera.position.setValue( center.operator_minus(forward.operator_mul(radius)));
}


public void zoomCamera (float amount)
{
    // NOTE: perhaps make minFocalDist and minCameraHeight configurable later
    final float minFocalDist    = 0.01f;
    final float minCameraHeight = 0.01f;

    if (camera == null) { return; }

    if (camera.isOfType(SoOrthographicCamera.getClassTypeId())) {
        // change the ortho camera height
        SoOrthographicCamera cam = (SoOrthographicCamera ) camera;
        float height = cam.height.getValue() * (float)Math.pow(2.0, amount);
        height = (height < minCameraHeight) ? minCameraHeight : height;
        cam.height.setValue( height);
    } else {
        // shorter/grow the focal distance given the mouse move
        float focalDistance = camera.focalDistance.getValue();;
        float newFocalDist = focalDistance * (float)Math.pow(2.0, amount);
        newFocalDist = (newFocalDist < minFocalDist) ? minFocalDist : newFocalDist;

        // finally reposition the camera
        final SbMatrix mx = new SbMatrix();
        mx.copyFrom( camera.orientation.getValue().getMatrix());
        final SbVec3f forward = new SbVec3f(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        camera.position.setValue(camera.position.getValue().operator_add(
                            forward.operator_mul((focalDistance - newFocalDist))));
        camera.focalDistance.setValue( newFocalDist);
    }
}



public void panCamera(final SbVec2s newPos)
{
    if (camera == null) { return; }

    final SbVec2f newLocator =
      new SbVec2f(newPos.getValue()[0]/(float)(sceneSize.getValue()[0]), newPos.getValue()[1]/(float)(sceneSize.getValue()[1]));
    // map new mouse location into the camera focal plane
    final SbViewVolume    cameraVolume = new SbViewVolume();
    final SbLine          line = new SbLine();
    final SbVec3f         newLocator3D = new SbVec3f();
    cameraVolume.copyFrom(camera.getViewVolume(sceneSize.getValue()[0]/(float)(sceneSize.getValue()[1])));
    cameraVolume.projectPointToLine(newLocator, line);
    focalplane.intersect(line, newLocator3D);

    // move the camera by the delta 3D position amount
    camera.position.setValue(camera.position.getValue().operator_add(
        (locator3D.operator_minus(newLocator3D))));

    // You would think we would have to set locator3D to
    // newLocator3D here.  But we don't, because moving the camera
    // essentially makes locator3D equal to newLocator3D in the
    // transformed space, and we will project the next newLocator3D in
    // this transformed space.
}


public void spinCamera(final SbVec2s newPos)
{

    final SbVec2f newLocator =
      new SbVec2f(newPos.getValue()[0]/(float)(sceneSize.getValue()[0]), newPos.getValue()[1]/(float)(sceneSize.getValue()[1]));
    // find rotation and rotate camera
    final SbRotation rot = new SbRotation();
    sphereSheet.projectAndGetRotation(newLocator, rot);
    rot.invert();

    rotateCamera(rot);

    // save rotation for animation
    lastIndex = ((lastIndex+1) % ROT_BUFF_SIZE);
    rotBuffer[lastIndex].copyFrom(rot);
    computeAverage = true;

    // check if queue is full
    if (((lastIndex+1) % ROT_BUFF_SIZE) == firstIndex) {
        firstIndex = ((firstIndex+1) % ROT_BUFF_SIZE);
    }
}


public void dollyCamera(final SbVec2s newPos)
{
    // moving the mouse up/down will move the camera futher/closer.
    // moving the camera sideway will not move the camera at all
    zoomCamera ((newPos.getValue()[1] - startPos.getValue()[1]) * zoomSensitivity / 40.0f);

    startPos.copyFrom( newPos);
}



    static final float maxSpinAngle = (float)(5.0/180.0*3.1415927); // max. 5 degrees per step
    
public boolean
doSpinAnimation()
{

    // check if average rotation needs to be computed
    if (computeAverage) {
        float averageAngle;
        final float[] angle = new float[1];
        final SbVec3f averageAxis = new SbVec3f(), axis = new SbVec3f();

        // get number of samples
        int num = (((lastIndex - firstIndex) + 1 +
            ROT_BUFF_SIZE) % ROT_BUFF_SIZE);

        // check for not enough samples
        if (num < 2) {
            return false;
        }

        // get average axis of rotation
        // ??? right now only take one sample
        rotBuffer[firstIndex].getValue(averageAxis, angle);

        // get average angle of rotation
        averageAngle = 0;
        for (int i=0; i<num; i++) {
            int n = (firstIndex + i) % ROT_BUFF_SIZE;
            rotBuffer[n].getValue(axis, angle);
            averageAngle += angle[0];
        }
        averageAngle /= (float)(num);

        // make rotation slower:
        averageAngle *= 0.5;
        // restrict rotation speed:
        if (averageAngle > maxSpinAngle) { averageAngle = maxSpinAngle; }

        averageRotation.setValue(averageAxis, averageAngle);
        computeAverage = false;
    }

    // rotate camera by average rotation
    rotateCamera(averageRotation);
    return true;
}


public void startDrag(final SbVec2s pos)
{
    startPos.copyFrom(pos);
  
    final SbVec2f relLocator = 
        new SbVec2f(startPos.getValue()[0]/(float)(sceneSize.getValue()[0]), startPos.getValue()[1]/(float)(sceneSize.getValue()[1]));

    // set the sphere sheet starting point
    sphereSheet.project(relLocator);

    // reset the animation queue
    firstIndex = 0;
    lastIndex = -1;

    if (camera == null) { return; }

    // Figure out the focal plane
    final SbMatrix mx = new SbMatrix();
    mx.copyFrom(camera.orientation.getValue().getMatrix());
    final SbVec3f forward = new SbVec3f(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
    final SbVec3f fp = camera.position.getValue().operator_add(
        forward.operator_mul(camera.focalDistance.getValue()));
    focalplane.copyFrom(new SbPlane(forward, fp));

    // map mouse starting position onto the panning plane
    final SbViewVolume    cameraVolume = new SbViewVolume();
    final SbLine          line = new SbLine();
    cameraVolume.copyFrom(camera.getViewVolume(sceneSize.getValue()[0]/(float)(sceneSize.getValue()[1])));
    cameraVolume.projectPointToLine(relLocator, line);
    focalplane.intersect(line, locator3D);
}


public void changeCameraValues(SoCamera newCamera)
{
    if (camera == null) {
        return;
    }

    // only paste cameras of the same type
    if (camera.getTypeId().operator_not_equal(newCamera.getTypeId())) {
        return;
    }

    // give our camera the values of the new camera
    camera.position        .copyFrom( newCamera.position);
    camera.orientation     .copyFrom( newCamera.orientation);
    camera.nearDistance    .copyFrom( newCamera.nearDistance);
    camera.farDistance     .copyFrom( newCamera.farDistance);
    camera.focalDistance   .copyFrom( newCamera.focalDistance);

    // get the height or heightAngle
    if (camera.isOfType(SoPerspectiveCamera.getClassTypeId())) {
        ((SoPerspectiveCamera )camera).heightAngle .copyFrom(
                ((SoPerspectiveCamera )newCamera).heightAngle);
    } else {
        ((SoOrthographicCamera )camera).height .copyFrom(
                ((SoOrthographicCamera )newCamera).height);
    }
}

}
