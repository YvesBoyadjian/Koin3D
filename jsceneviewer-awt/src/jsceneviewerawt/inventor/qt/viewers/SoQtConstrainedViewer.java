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

package jsceneviewerawt.inventor.qt.viewers;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jsceneviewerawt.inventor.qt.SoQtCameraController;

import java.awt.*;

/**
 * @author Yves Boyadjian
 *
 */

//////////////////////////////////////////////////////////////////////////////
//
//Class: SoQtConstrainedViewer
//
//////////////////////////////////////////////////////////////////////////////

public class SoQtConstrainedViewer extends SoQtFullViewer {


//	public SoQtConstrainedCameraController getCameraController() {
//		return (SoQtConstrainedCameraController)super.getCameraController();
//	}

    // Specifies the upward direction of the viewer. This up direction is
    // used by the viewers to constrain the camera when tilting up/down, and
    // also used when rotating the camera right/left.
    // The default is the +Y (0,1,0) direction.
	public void            setUpDirection(final SbVec3f newUpDirection)
	{
	    final SbRotation rot = new SbRotation(upDirection, newUpDirection);
	    upDirection.copyFrom(newUpDirection);

	    SoCamera camera = getCameraController().getCamera(); // java port

	    // rotate the camera and check for constrain
	    if (camera != null) {
	        camera.orientation.setValue(rot.operator_mul( camera.orientation.getValue()));
	        ((SoQtConstrainedCameraController)getCameraController()).checkForCameraUpConstrain();
	    }
	}

	public SbVec3f         getUpDirection()                { return upDirection; }

    // redefine these to add constrained viewer functionality
	public void    setCamera(SoCamera newCamera, boolean createdCamera)
{
    // call base class routine
	super.setCamera(newCamera, createdCamera);

    SoCamera camera = getCameraController().getCamera(); // java port

    // now check for constrains
    if (camera != null)
    	((SoQtConstrainedCameraController)getCameraController()).checkForCameraUpConstrain();
}

	public void    saveHomePositionSlot()
{
    // call the parent class
    super.saveHomePositionSlot();

    origUpDirection.copyFrom(upDirection);
}

	public void    resetToHomePositionSlot()
{
    // call the parent class
    super.resetToHomePositionSlot();

    upDirection.copyFrom(origUpDirection);
}

	public void    recomputeSceneSize()
{
		SoNode sceneGraph = getCameraController().getSceneGraph(); // java port
		
    if (sceneGraph==null || sceneRoot==null) {
        sceneSize = sceneHeight = 0.0f;
        return;
    }

    // Use assignment notation to disambiguate from expression (edison)
    final SoGetBoundingBoxAction bboxAct = new SoGetBoundingBoxAction(new SbViewportRegion(getGlxSize()));
    bboxAct.apply(sceneRoot);
    final SbBox3f bbox = new SbBox3f(bboxAct.getBoundingBox());

    if (bbox.isEmpty()) {
        sceneSize = sceneHeight = 0.0f;
        return;
    }

    // ??? this assumes Y is up right now (for sceneHeight)
    final float[] x = new float[1], y = new float[1], z = new float[1];
    bbox.getSize(x, /*sceneHeight*/y, z);sceneHeight = y[0];
    sceneSize = (x[0] > z[0]) ? x[0] : z[0];

    if (sceneSize <= 0.0) {
        sceneSize = 0.0f;
    }
    if (sceneHeight <= 0.0) {
        sceneHeight = 0.0f;
    }
}


 
	protected   SoQtConstrainedViewer(
            SoQtFullViewer.BuildFlag flag,
            SoQtCameraController.Type type,
            Container parent, int f) {
		super(flag, type, parent, f);
		
    	cameraController = new SoQtConstrainedCameraController(type, upDirection) {

			@Override
			protected void animationStarted() {
				SoQtConstrainedViewer.this.animationStarted();
			}

			@Override
			protected void animationFinished() {
				SoQtConstrainedViewer.this.animationFinished();
			}

			@Override
			public void setCamera(SoCamera newCamera, boolean cameraCreated) {
				SoQtConstrainedViewer.this.setCamera(newCamera, cameraCreated);
			}

			@Override
			public void setSeekMode(boolean flag) {
				SoQtConstrainedViewer.this.setSeekMode(flag);
			}
    		
    	};
    	
		
		    // init local vars
		    upDirection.setValue(0, 1, 0);
		    sceneHeight = 0.0f;

		    // assign decoration titles
		    setBottomWheelTitle("Rotate");
		    setLeftWheelTitle("Tilt");
		    setRightWheelTitle("Dolly");

	        cameraController.setSceneRoot(sceneRoot, 1);
	}

	
	
	public void destructor() {
		super.destructor();
	}

	protected final SbVec3f         upDirection = new SbVec3f();
	protected float           sceneHeight;

    // Tilts the camera, restraining it to 180 degree rotation from the
    // up direction. A positive angle tilts the camera up.
	protected void        tiltCamera(float deltaAngle)
{
	    SoCamera camera = getCameraController().getCamera(); // java port

    if (camera == null) { return; }

    // get camera forward direction
    final SbMatrix mx = new SbMatrix();
    mx.copyFrom( camera.orientation.getValue());
    final SbVec3f forward = new SbVec3f(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);

    // get the angle between the foward and up direction
    // as well as the axis of rotation
    final SbRotation rot = new SbRotation(forward, upDirection);
    final SbVec3f axis = new SbVec3f();
    final float[] angle = new float[1];
    rot.getValue(axis, angle);
    // make angle in [-PI,PI] range
    if (angle[0] > Math.PI) {
        angle[0] -= 2*Math.PI;
    } else if (angle[0] < -Math.PI) {
        angle[0] += 2*Math.PI;
    }
    // make rotation toward up direction positive angle
    if (angle[0] < 0.0) {
        angle[0] = -angle[0];
        axis.copyFrom( axis.operator_minus());
    }

    // check if we are already looking almost along the up direction
    if ( (angle[0] <= MIN_ANGLE && deltaAngle > 0) ||
         (angle[0] >= (Math.PI - MIN_ANGLE) && deltaAngle < 0) )
    { return; }

    // clamp the angle change as to not get too close along the up direction
    if (deltaAngle > 0 && deltaAngle > (angle[0] - MIN_ANGLE)) {
        deltaAngle = angle[0] - MIN_ANGLE;
    } else if (deltaAngle < 0 && deltaAngle < (angle[0] + MIN_ANGLE - Math.PI)) {
        deltaAngle = angle[0] + MIN_ANGLE - (float)Math.PI;
}

    // finally rotate the camera by the given angle
    rot.setValue(axis, deltaAngle);
    camera.orientation.setValue( camera.orientation.getValue().operator_mul(rot));
}


    // Redefine these to do constrained viewing tasks.
    // The bottom wheel rotates the camera around the up direction, the
    // left wheel tilts the camera up/down constraning to 180 degree from
    // the up direction.
//	protected void        bottomWheelMotion(double newVal)
//{
//	    SoCamera camera = getCameraController().getCamera(); // java port
//
//    if (camera == null) { return; }
//
//    // get rotation and apply to camera
//    final SbRotation rot = new SbRotation(upDirection, bottomWheelValue - newVal);
//    camera.orientation.setValue( camera.orientation.getValue().operator_mul(rot));
//
//    bottomWheelValue = newVal;
//}

	
	
//	protected void        leftWheelMotion(double newVal)
//{
//    tiltCamera(leftWheelValue - newVal);
//
//    leftWheelValue = newVal;
//}

    // this routine is called by subclasses to find and set the new
    // up direction given the current mouse position. If something is
    // picked, the normal to the object picked will be used to specify
    // the new up direction.
	protected void                findUpDirection(final SbVec2s mouseLocation)
{
	    SoCamera camera = getCameraController().getCamera(); // java port

    if (camera == null) { return; }

    // do the picking
    final SbVec2s size = new SbVec2s(getGlxSize());
    final SoRayPickAction pick = new SoRayPickAction( new SbViewportRegion(size) );
    pick.setPoint(mouseLocation);
    pick.setRadius(1.0f);
    pick.setPickAll(false);
    pick.apply(sceneRoot);

    // makes sure something got picked, then
    // get the picked point.
    SoPickedPoint pp = pick.getPickedPoint();
    if (pp == null) { return; }
    SbVec3f normal = pp.getNormal();

    // check whether the normal is pointing toward the camera, else
    // flip the normal around.
    SbVec3f point = pp.getPoint();
    if ( normal.dot(camera.position.getValue().operator_minus(point)) < 0 ) {
        normal.negate();
    }

//printf("%f %f %f\n", normal[0], normal[1], normal[2]);

    setUpDirection(normal);
}
 
 private final SbVec3f         origUpDirection = new SbVec3f(); // used to save/reset
 
 private final static float MIN_ANGLE     =  5*(float)Math.PI/180.f;   // minimum angle between look at
                                        // direction and up direction (in rad)



}
