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
 |      This file contains the definition of the SbCylinderSectionProjector
 |      class. This projector projects the mouse position onto the
 |      section of a cylinder that has been sliced by a plane.
 |
 |      The tolerance slice can be specified as a fraction of the radius
 |      of the cylinder. The projection point will not extend beyond the
 |      sliced portion of the cylinder.
 |
 |   Author(s)          : Howard Look, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbCylinder;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Cylinder-section projector.
/*!
\class SbCylinderSectionProjector
\ingroup Projectors
SbCylinderSectionProjector 
projects a window space point (usually based on the mouse location)
onto the section of a cylinder that has been sliced by a plane.
Two projected points can produce a rotation
along the cylinder's axis.
The tolerance slice can be specified as a fraction of the radius
of the cylinder. The projection point will not extend beyond the
sliced portion of the cylinder.


Incremental changes (delta rotation) can be computed during
interactive sessions. Cylinder projectors are typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderPlaneProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

public class SbCylinderSectionProjector extends SbCylinderProjector {
	
	    //! Information about the ring tolerance.
    protected float               tolerance;  //!< the edge tolerance
    protected float               tolDist;    //!< dist from planeLine to cylinder
    
    //! Information about the plane used for intersection testing.
    protected final SbVec3f     planeDir = new SbVec3f();               //!< normal direction
    protected final SbLine      planeLine = new SbLine();              //!< line parallel to axis, but in plane
    protected float       planeDist;              //!< distance from cylinder center
    protected final SbPlane     tolPlane = new SbPlane();               //!< the plane itself
	

    //! Set and get the edge tolerance as a fraction of the
    //! radius of the cylinder. If this is 1.0, the projector is a
    //! half cylinder. If this is .1, the projector is a slice of
    //! the cylinder with radius .1*radius.  Default is .9.
    public float               getTolerance()               { return tolerance; }

    
    //! Default constructor.
    //! The default view volume is undefined.
    //! The default working space is identity (world space).
    //! The default cylinder is centered about the Y axis and has a radius of 1.0.
    //! The default edge tolerance is .9.
    //! The default eye orientation is TRUE.
    public SbCylinderSectionProjector() {
    	this(.9f,true);
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public SbCylinderSectionProjector(
    float tol,
    boolean orient) {
	super(orient);

    setTolerance(tol);
}

public SbCylinderSectionProjector(
    final SbCylinder c,
    float tol,
    boolean orient) {
	super(c, orient);

    setTolerance(tol);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns an instance that is a copy of this instance. The caller
//    is responsible for deleting the copy when done.
//
// Use: public, virtual
//

public SbProjector 
copy() 
//
////////////////////////////////////////////////////////////////////////
{
    SbCylinderSectionProjector newProjector = new SbCylinderSectionProjector();

    //(*newProjector) = (*this);
    newProjector.copyFrom(this);

    return newProjector;
}

	@Override
	public void copyFrom(Object other) {
		SbCylinderSectionProjector otherProjector = (SbCylinderSectionProjector)other;
		super.copyFrom(other);
		
	    tolerance = otherProjector.tolerance;  //!< the edge tolerance
	    tolDist = otherProjector.tolDist;    //!< dist from planeLine to cylinder
	    
	    //! Information about the plane used for intersection testing.
	    planeDir.copyFrom(otherProjector.planeDir);               //!< normal direction
	    planeLine.copyFrom(otherProjector.planeDir);              //!< line parallel to axis, but in plane
	    planeDist = otherProjector.planeDist;              //!< distance from cylinder center
	    tolPlane.copyFrom(otherProjector.tolPlane);               //!< the plane itself
	}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Project the mouse position onto a point on this cylinder.
//
// Use: public

public SbVec3f
project(final SbVec2f point)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f result = new SbVec3f();
    final SbLine workingLine = new SbLine(getWorkingLine(point));

    if (needSetup)
        setupTolerance();

    final SbVec3f planeIntersection = new SbVec3f();
    final SbVec3f cylIntersection = new SbVec3f(), dontCare = new SbVec3f();
    boolean hitCylinder;
    if ( intersectFront == true )
        hitCylinder = cylinder.intersect(workingLine, cylIntersection,dontCare);
    else
        hitCylinder = cylinder.intersect(workingLine,dontCare, cylIntersection);

    if (hitCylinder) {
        // drop the cylinder intersection onto the tolerance plane

        final SbLine projectLine = new SbLine(cylIntersection, cylIntersection.operator_add(planeDir));
        if (! tolPlane.intersect(projectLine, planeIntersection))
//#ifdef DEBUG
            SoDebugError.post("SbCylinderSectionProjector::project",
                               "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif
    }
    else if (! tolPlane.intersect(workingLine, planeIntersection))
//#ifdef DEBUG
        SoDebugError.post("SbCylinderSectionProjector::project",
                           "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif

    
    final SbVec3f vecToPoint = planeIntersection.operator_minus( 
                         planeLine.getClosestPoint(planeIntersection));
    float dist = vecToPoint.length();
        
    if (dist < tolDist) {
//#ifdef DEBUG
        if (! hitCylinder)
            SoDebugError.post("SbCylinderSectionProjector::project",
                               "Couldn't intersect with cylinder");
//#endif
        result.copyFrom(cylIntersection);
    }
    else {
        // get the point that is on the tolerance line
        final SbVec3f tolVec = new SbVec3f(vecToPoint);
        final SbVec3f axisPoint = planeIntersection.operator_minus(tolVec);
        tolVec.normalize();
        tolVec.operator_mul_equal( tolDist);
        result.copyFrom(axisPoint.operator_add(tolVec));
    }

    lastPoint.copyFrom(result);
    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
// Set the edge tolerance, 0.0 - 1.0.
//
// Use: public

public void
setTolerance(float t)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (t < 0.0) {
        SoDebugError.post("SbCylinderSectionProjector::setEdgeTolerance",
                           "Tolerance cannot be less than 0.0");
        t = 0.0f;
    }
    else if (t > 1.0) {
        SoDebugError.post("SbCylinderSectionProjector::setEdgeTolerance",
                           "Tolerance cannot be greater than 1.0");
        t = 1.0f;
    }
//#endif

    tolerance = t;
    needSetup = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Find if this point on the projector is within tolerance.
//
// Use: virtual public

public boolean
isWithinTolerance(final SbVec3f point)
//
////////////////////////////////////////////////////////////////////////
{
    if (needSetup)
        setupTolerance();

    // Drop the point onto the tolerance plane. 
    final SbVec3f planeIntersection = new SbVec3f();
    final SbLine projectLine = new SbLine(point, point.operator_add(planeDir));
    if (! tolPlane.intersect(projectLine, planeIntersection))
//#ifdef DEBUG
        SoDebugError.post("SbCylinderSectionProjector::isWithinTolerance",
                           "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif
    
    final SbVec3f vecToPoint = planeIntersection.operator_minus(
                         planeLine.getClosestPoint(planeIntersection));
    float dist = vecToPoint.length();
    
    if (dist < tolDist)
        return true;
    else
        return false;
}    

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Sets up the tolerance plane.
//
// Use: protected
protected void
setupTolerance()
//
////////////////////////////////////////////////////////////////////////
{
    // The plane may either be in working space, or always
    // oriented towards the eye (as best as possible). In
    // either case, the plane is always parallel to the axis
    // of the cylinder.

    // Find orientation of the tolerance plane, in working space.
    // The plane is defined by the axis vector and the
    // perpendicular to the axis and eyeDir (or z)
    final SbVec3f perpDir = new SbVec3f(); // perpendicular to axis and plane
    if (orientToEye) {
        final SbVec3f eyeDir = new SbVec3f();
        
        if (viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE) {
            // find the projection point in working space coords
            final SbVec3f workingProjPoint = new SbVec3f();
            worldToWorking.multVecMatrix(
                viewVol.getProjectionPoint(), workingProjPoint);
        
            eyeDir.copyFrom( workingProjPoint.operator_minus(cylinder.getAxis().getPosition()));
        }
        else {
            // Use the projection direction in an orthographic
            // view vol
            worldToWorking.multDirMatrix(viewVol.zVector(), eyeDir);
        }        

        perpDir.copyFrom( (cylinder.getAxis().getDirection()).cross(eyeDir));
    }
    else {
        perpDir.copyFrom( (cylinder.getAxis().getDirection()).cross(new SbVec3f(0,0,1)));
    }
    planeDir.copyFrom( perpDir.cross(cylinder.getAxis().getDirection()));
    planeDir.normalize();

    if ( intersectFront == false )
        planeDir.operator_mul_equal( -1.0f);

    // distance from planePoint to edge of tolerance
    tolDist = cylinder.getRadius() * tolerance;
    
    // find disntance from the center of the cylinder to the tolerance
    // plane
    planeDist = (float)Math.sqrt((cylinder.getRadius()*cylinder.getRadius()) - 
                (tolDist * tolDist));

    // plane given direction and distance to origin
    final SbVec3f planePoint = planeDir.operator_mul(planeDist).operator_add(cylinder.getAxis().getPosition());
    tolPlane.copyFrom( new SbPlane(planeDir, planePoint));
    planeLine.setValue(planePoint,
                       planePoint.operator_add( cylinder.getAxis().getDirection()));
    
    needSetup = false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//  Get a rotation based on two points on this projector.
//  Rotations are _always_ about the axis of the cylinder.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public SbRotation
getRotation(final SbVec3f p1, final SbVec3f p2)
{
    // Find perpendiculars to from cylinder's axis to each
    // point.
    final SbVec3f v1 = p1.operator_minus(cylinder.getAxis().getClosestPoint(p1));
    final SbVec3f v2 = p2.operator_minus(cylinder.getAxis().getClosestPoint(p2));
        
    float cosAngle = v1.dot(v2)/(v1.length()*v2.length());
        
    // prevent numerical instability problems
    if ((cosAngle > 1.0) || (cosAngle < -1.0))
        return SbRotation.identity();
            
    float angle = (float)Math.acos(cosAngle);

    // This will either be the same as the cylinder's
    // axis, or the same but with direction reversed
    SbVec3f rotAxis = v1.cross(v2);
        
    return new SbRotation(rotAxis, angle);
}
    
}
