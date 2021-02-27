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
 |      This file contains the definition of the SbCylinderProjector
 |      class. This is an abstract base class for projectors
 |      that somehow use a cylinder in their projection.
 |
 |      The getRotation methods for an SbCylinder will always
 |      return a rotation that is about the axis of the cylinder.
 |        
 |   Author(s)          : Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbCylinder;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Cylinder projector.
/*!
\class SbCylinderProjector
\ingroup Projectors
SbCylinderProjector 
is an abstract base class for projectors that use a cylinder in
their projection. The
getRotation() method
 
for an SbCylinderProjector will always
return a rotation that is about the axis of the cylinder.
Cylinder projectors are typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

public abstract class SbCylinderProjector extends SbProjector {
	
    //! Are intersections done on the front half ( if not, they're done on th e
    //! back half) of the cylinder?
    protected boolean        intersectFront;

    protected final SbCylinder  cylinder = new SbCylinder();               //!< Cylinder for this projector.
    protected boolean        orientToEye;            //!< TRUE if always oriented to eye.
    protected boolean        needSetup;              //!< Set TRUE whenever cylinder,
                                        //! work space or orientation changes.

    //! Cached last point on this projector. Subclasses must set
    //! this in their project and getRotation methods.
    protected final SbVec3f     lastPoint = new SbVec3f();
    
	@Override
	public void copyFrom(Object other) {
		SbCylinderProjector otherProjector = (SbCylinderProjector)other;
		super.copyFrom(other);
		
	    intersectFront = otherProjector.intersectFront;

	    cylinder.copyFrom(otherProjector.cylinder);               //!< Cylinder for this projector.
	    orientToEye = otherProjector.orientToEye;            //!< TRUE if always oriented to eye.
	    needSetup = otherProjector.needSetup;              //!< Set TRUE whenever cylinder,
	    lastPoint.copyFrom(otherProjector.lastPoint);
	}
	
    //! Set and get the cylinder on which to project points.
    //! The default cylinder is aligned with the Y axis and has radius 1.0.
    public SbCylinder getCylinder()              { return cylinder; }

    //! Set and get whether the projector should always be oriented towards the eye.
    //! Set to FALSE if the tolerance should be evaluated in working space.
    public boolean                isOrientToEye()       { return orientToEye; }

    //! Set and get whether the projector should intersect the half of the
    //! cylinder that faces the eye.
    public boolean                isFront()          { return intersectFront; }
    
    //! Apply the projector using the given point, returning the
    //! point in three dimensions that it projects to.
    //! The point should be normalized from 0-1, with (0,0) at the lower-left.
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.projectors.SbProjector#project(jscenegraph.database.inventor.SbVec2f)
	 */
	@Override
	public abstract SbVec3f project(SbVec2f point);

	public void destructor() {
		super.destructor();
	}

    //! Get a rotation given two points on this cylinder projector.
    //! The rotation will be about the axis of the cylinder.
	public abstract SbRotation getRotation(SbVec3f point1, SbVec3f point2);

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: protected
//
////////////////////////////////////////////////////////////////////////

public SbCylinderProjector(boolean orient) {
super();

    orientToEye = false;
    setCylinder(new SbCylinder(new SbLine(new SbVec3f(0,0,0), new SbVec3f(0,1,0)), 1.0f));
    setOrientToEye(orient);
    setFront(true);
}

public SbCylinderProjector(
    final SbCylinder c,
    boolean orient) {
super();

    orientToEye = false;
    setCylinder(c);
    setOrientToEye(orient);
    setFront(true);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
// Misc set routines.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public void
setCylinder(final SbCylinder c)
{
    cylinder.copyFrom(c);
    needSetup = true;
}

public void
setOrientToEye(boolean b)
{
    if (orientToEye != b) {
        orientToEye = b;
        needSetup = true;
    }   
}

public void
setFront(boolean b)
{
    intersectFront = b;
    needSetup = true;
}

public boolean
isPointInFront(final SbVec3f point )
{
    final SbViewVolume viewVol = new SbViewVolume(getViewVolume());
    boolean         ptInFront = true;

    final SbVec3f closestPtOnAxis = new SbVec3f(), axisPtToInput = new SbVec3f();

    closestPtOnAxis.copyFrom(cylinder.getAxis().getClosestPoint( point ));
    axisPtToInput.copyFrom(point.operator_minus(closestPtOnAxis));

    if ( viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE ) {
        final SbVec3f lclProjPt = new SbVec3f();
        worldToWorking.multVecMatrix( viewVol.getProjectionPoint(), lclProjPt);
        SbVec3f cylPtToProj = lclProjPt.operator_minus(closestPtOnAxis);
        if ( axisPtToInput.dot( cylPtToProj ) < 0.0 ) 
            ptInFront = false;
    }
    else {
        final SbVec3f lclZDir = new SbVec3f();
        worldToWorking.multDirMatrix( viewVol.zVector(), lclZDir );
        if ( axisPtToInput.dot( lclZDir ) < 0.0 ) 
            ptInFront = false;
    }
    return ptInFront;   
}

public void
setWorkingSpace(final SbMatrix space)
{
    super.setWorkingSpace(space);
    needSetup = true;
}

public SbVec3f
projectAndGetRotation(
    final SbVec2f point, final SbRotation rot)
{
    final SbVec3f oldPoint = new SbVec3f(lastPoint);
    final SbVec3f newPoint = new SbVec3f(project(point));
    rot.copyFrom(getRotation(oldPoint, newPoint));
    return newPoint;
}

	
}
