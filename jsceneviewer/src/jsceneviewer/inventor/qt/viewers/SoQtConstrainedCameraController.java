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

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCamera;
import jsceneviewer.inventor.qt.SoQtCameraController;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoQtConstrainedCameraController extends SoQtCameraController {
	
	private final SbVec3f upDirection;

	public SoQtConstrainedCameraController(Type t, SbVec3f upDirection) {
		super(t);
		this.upDirection = upDirection;
	}


    // This is called during a paste.
    // We redefine this to keep the right vector of the camera
    // in a parallel plane.
	public void        changeCameraValues(SoCamera newCamera)
{
	    SoCamera camera = getCamera(); // java port

    if (camera == null) { return; }

    // only paste cameras of the same type
    if (camera.getTypeId().operator_not_equal(newCamera.getTypeId())) {
        return;
    }

    // let the base class copy camera values
    super.changeCameraValues(newCamera);

    // now check for constrains
    checkForCameraUpConstrain();
}



    // this routine checks the camera orientation and makes sure that the
    // current right vector and the ideal right vector (cross between the
    // view vector and world up direction) are the same (i.e. no unwanted
    // roll), else it fixes it. This keeps the up direction valid.
	protected void                checkForCameraUpConstrain()
{
	    SoCamera camera = getCamera(); // java port

    if (camera == null) { return; }

    // adjust the camera if necessary so that the new right vector
    // lies in a plane parallel to our old right vector
    final SbMatrix mx = new SbMatrix();
    mx.copyFrom( camera.orientation.getValue().getMatrix());
    final SbVec3f newForward = new SbVec3f(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
    final SbVec3f newRight = new SbVec3f(mx.getValue()[0][0], mx.getValue()[0][1], mx.getValue()[0][2]);

    // idealRight is the right vector computed from the pasted forward
    // and the existing world up vector
    final SbVec3f idealRight = newForward.cross(upDirection);
    idealRight.normalize();

    // if idealRight is 0, then the newForward is looking in the
    // same direction as the world up. In this case, we have to choose
    // a right vector, so we choose the newRight. Otherwise, we have
    // to rotate our orientation from the newRight to the idealRight.
    if (idealRight.operator_not_equal(new SbVec3f(0.0f, 0.0f, 0.0f))) {
        // rotate to idealRight!
        final SbRotation rot = new SbRotation(newRight, idealRight);
        camera.orientation.setValue(
            camera.orientation.getValue().operator_mul(rot));
    }
}


    // Redefine this to keep the up vector when seeking
	protected void        computeSeekFinalOrientation()
{
	    SoCamera camera = getCamera(); // java port

    final SbMatrix mx = new SbMatrix();
    final SbVec3f viewVector = new SbVec3f();

    // find the camera final orientation
    if ( isDetailSeek() ) {

        // get the camera unconstrained new orientation
        mx.copyFrom(camera.orientation.getValue());
        viewVector.setValue(-mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        final SbRotation changeOrient = new SbRotation();
        changeOrient.setValue(viewVector, seekPoint.operator_minus(camera.position.getValue()));
        newCamOrientation.copyFrom( camera.orientation.getValue().operator_mul(changeOrient));

        // check for constrains
        camera.orientation.setValue( newCamOrientation);
        checkForCameraUpConstrain();
        newCamOrientation.copyFrom( camera.orientation.getValue());
    } else {
    	newCamOrientation.copyFrom( camera.orientation.getValue());
    }
}


}
