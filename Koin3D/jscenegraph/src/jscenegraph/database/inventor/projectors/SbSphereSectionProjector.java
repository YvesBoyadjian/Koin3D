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
 |      This file contains the definition of the SbSphereSectionProjector
 |      class. This projector projects the mouse position onto the 
 |      section of a sphere that has been sliced by a plane.
 |
 |      The tolerance slice can be specified as a fraction of the radius
 |      of the sphere. The projection point will not extend beyond
 |      the sliced portion of the sphere.
 |
 |      This projector is good for trackballs that only do
 |      pure roll when the mouse is off the sliced portion of
 |      the sphere.
 |        
 |   Author(s)          : Howard Look, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

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
//! Sphere-section projector.
/*!
\class SbSphereSectionProjector
\ingroup Projectors
SbSphereSectionProjector 
projects a window space point (usually based on the mouse location)
onto the section of a sphere that has been sliced by a plane.
Two projected points can produce a rotation
about the sphere's center.
The tolerance slice can be specified as a fraction of the radius
of the sphere. The projection point will not extend beyond the
sliced portion of the sphere.


Incremental changes (delta rotation) can be computed during
interactive sessions. Sphere projectors are typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderProjector, SbCylinderSectionProjector, SbCylinderPlaneProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

public class SbSphereSectionProjector extends SbSphereProjector {
	
    //! Information about the slice tolerance.
    protected float               tolerance;  //!< the edge tolerance
    protected float               tolDist;    //!< dist from planePoint to tolerance slice

    protected float               radialFactor;
    
    //! Information about the plane used for intersection testing.
    protected final SbVec3f     planePoint = new SbVec3f();             //!< point on plane
    protected final SbVec3f     planeDir = new SbVec3f();               //!< normal direction
    protected float       planeDist;              //!< distance from sphere center
    protected final SbPlane     tolPlane = new SbPlane();               //!< the plane itself
	
	
    //! Default constructor.
    //! The default view volume is undefined.
    //! The default working space is identity (world space).
    //! The default sphere to use has a radius of 1.0 and is centered at (0,0,0).
    //! The default edge tolerance is .9.
    //! The default eye orientation is TRUE.
    public SbSphereSectionProjector() {
    	this(.9f);
    }
    public SbSphereSectionProjector(float  edgeTol) {
    	this(edgeTol, true);
    }
    public SbSphereSectionProjector(float  tol,
            boolean orientToEye) {
    	super(orientToEye);
        setTolerance(tol);
        setRadialFactor(0.0f);
    }
	
    //! Set and get the edge tolerance as a fraction of the
    //! radius of the sphere. If this is 1.0, the projector is a
    //! hemisphere. If this is .1, the projector is a slice of
    //! the sphere with radius .1*radius.  Default is .9.
    public float               getTolerance()               { return tolerance; }

    //! \see getRadialFactor
    public void                setRadialFactor(float rad ) { radialFactor = rad;}
    //! Set and get the radial rotation factor.
    //! When the mouse is dragged off the edge of the sphere, the mouse
    //! motion can be classified as either tangential (moving in a circle
    //! around the sphere) or radial (moving toward or away from the center).
    //! The tangential motion will always map to a rotation around the center, 
    //! (like the hands of a clock).
    //! The radial motion, by default, has no effect. But if you set the 
    //! \b radialFactor  to be &gt; 0.0, this motion will make the sphere rotate
    //! as if the mouse is pulling the top of the sphere out toward the
    //! mouse. 
    //! If \b radialFactor  = 1.0, then pulling has a `normal' feel (that is, the
    //! mouse motion causes the same amount of rotation as if you had rotated
    //! by hitting the actual surface of the sphere).
    //! Default is 0.0
    public float               getRadialFactor()  { return radialFactor; }



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
    SbSphereSectionProjector newProjector = new SbSphereSectionProjector();

    //(*newProjector) = (*this);
    newProjector.copyFrom(this);

    return newProjector;
}

	@Override
	public void copyFrom(Object other) {
		SbSphereSectionProjector otherProjector = (SbSphereSectionProjector)other;
		super.copyFrom(other);
		
    //! Information about the slice tolerance.
    tolerance = otherProjector.tolerance;  //!< the edge tolerance
    tolDist = otherProjector.tolDist;    //!< dist from planePoint to tolerance slice

    radialFactor = otherProjector.radialFactor;
    
    //! Information about the plane used for intersection testing.
    planePoint.copyFrom(otherProjector.planePoint);             //!< point on plane
    planeDir.copyFrom(otherProjector.planeDir);               //!< normal direction
    planeDist = otherProjector.planeDist;              //!< distance from sphere center
    tolPlane.copyFrom(otherProjector.tolPlane);               //!< the plane itself
		
	}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Project the mouse position onto a point on this sphere.
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
    final SbVec3f sphereIntersection = new SbVec3f(), dontCare = new SbVec3f();

    boolean hitSphere;

    // Depending on whether we are intersecting front or rear, we care 
    // about different arguments returned from intersect
    if ( intersectFront )
        hitSphere = sphere.intersect(workingLine, sphereIntersection, dontCare);
    else 
        hitSphere = sphere.intersect(workingLine, dontCare, sphereIntersection);

    if (hitSphere) {
        // drop the sphere intersection onto the tolerance plane

        final SbLine projectLine = new SbLine(sphereIntersection, sphereIntersection.operator_add(planeDir));
        if (! tolPlane.intersect(projectLine, planeIntersection))
//#ifdef DEBUG
            SoDebugError.post("SbSphereSectionProjector::project",
                               "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif
    }
    else if (! tolPlane.intersect(workingLine, planeIntersection))
//#ifdef DEBUG
        SoDebugError.post("SbSphereSectionProjector::project",
                           "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif

    float dist = (planeIntersection.operator_minus(planePoint)).length();

    if (dist < tolDist) {
//#ifdef DEBUG
        if (! hitSphere)
            SoDebugError.post("SbSphereSectionProjector::project",
                               "Couldn't intersect with sphere");
//#endif
        result.copyFrom(sphereIntersection);
    }
    else {
        result.copyFrom(planeIntersection);
    }

    lastPoint.copyFrom( result);
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
        SoDebugError.post("SbSphereSectionProjector::setEdgeTolerance",
                           "Tolerance cannot be less than 0.0");
        t = 0.0f;
    }
    else if (t > 1.0) {
        SoDebugError.post("SbSphereSectionProjector::setEdgeTolerance",
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
// Find if this point on the sphere is within the tolerance
// ring.
//
// Use: virtual public

public boolean
isWithinTolerance(final SbVec3f point)
//
////////////////////////////////////////////////////////////////////////
{
    if (needSetup)
        setupTolerance();

    // Drop a perpendicular from the point to the tolerance
    // plane
    final SbVec3f planeIntersection = new SbVec3f();
    final SbLine line = new SbLine(point, point.operator_add(planeDir));

    if (! tolPlane.intersect(line, planeIntersection)) {
//#ifdef DEBUG
        SoDebugError.post("SbSphereSectionProjector::isWithinTolerance",
                           "Couldn't intersect with plane");
//#else
        /* Do nothing */;
//#endif
        return false;
    }

    float dist = (planeIntersection.operator_minus(planePoint)).length();

    // Need to give a little slack to allow for fp precision
    // Err on the side of returning this point out of tolerance
    // so that it is easier to get pure roll.
    // ??? Seems like a lot of fudge, doesn't it? Perhaps this
    // should relate to the radius of the sphere, or be
    // a percentage of the tolerance.
    return (dist < (tolDist - .001));
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
    // Find the intersection point on the tolerance plane.
    // The plane may either be in working space, or always
    // oriented towards the eye.

    // find orientation of the tolerance plane, in working space
    if (orientToEye) {
        if (viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE) {
            // find the projection point in working space coords
            final SbVec3f workingProjPoint = new SbVec3f();
            worldToWorking.multVecMatrix(
                viewVol.getProjectionPoint(), workingProjPoint);

            planeDir.copyFrom( workingProjPoint.operator_minus( sphere.getCenter()));
        }
        else {
            // Use the projection direction in an orthographic
            // view vol
            worldToWorking.multDirMatrix(viewVol.zVector(), planeDir);
        }        
        planeDir.normalize();
    }
    else {
        planeDir.setValue(0,0,1);
    }

    // distance from planePoint to edge of tolerance ring
    tolDist = sphere.getRadius() * tolerance;

    // find disntance from the center of the sphere to the tolerance
    // plane
    planeDist =(float)
        Math.sqrt((sphere.getRadius()*sphere.getRadius()) -
              (tolDist * tolDist));

    // If we are intersecting with the back half of the sphere, then
    // face the plane the other way.
    if ( intersectFront == false )
        planeDir.operator_mul_equal( -1.0f);

    // plane given direction and point to pass through
    planePoint.copyFrom(sphere.getCenter().operator_add( planeDir.operator_mul(planeDist)));

    tolPlane.copyFrom( new SbPlane(planeDir, planePoint));

    needSetup = false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//  Get a rotation based on two points on this projector.
//
// Use: public

public SbRotation
getRotation(final SbVec3f p1, final SbVec3f p2)
//
////////////////////////////////////////////////////////////////////////
{
    boolean tol1 = isWithinTolerance(p1);
    boolean tol2 = isWithinTolerance(p2);

    if (tol1 && tol2) {
        // both points in tolerance, rotate about
        // sphere center

        return new SbRotation(
            p1.operator_minus(sphere.getCenter()),
            p2.operator_minus(sphere.getCenter()));
    }
    else if (!tol1 && !tol2) {
        // both points out of tolerance, rotate about
        // plane point

        // Would like to just use this:
        final SbRotation badRot = new SbRotation(p1.operator_minus( planePoint), p2.operator_minus( planePoint));
        // but fp instablity gives back a goofy axis, so we don't get
        // pure roll.

        // So we need to snap the axis to be parallel to plane dir
        final SbVec3f badAxis = new SbVec3f(); final float[] goodAngle = new float[1];
        badRot.getValue(badAxis, goodAngle);

        final SbVec3f goodAxis = new SbVec3f();
        if (badAxis.dot(planeDir) > 0.0)
            goodAxis.copyFrom( planeDir);
        else        
            goodAxis.copyFrom( planeDir.operator_minus());

        final SbRotation rollRot = new SbRotation(goodAxis, goodAngle[0]);

        //Now find rotation in the direction perpendicular to this:
        final SbVec3f diff1 = p1.operator_minus( planePoint);
        final SbVec3f diff2 = p2.operator_minus( planePoint);
        float d = diff2.length() - diff1.length();

        // Check for degenerate cases
        float theta = d / sphere.getRadius();
        if ( Math.abs(theta) < 0.000001 || Math.abs(theta) > 1.0 )
            return rollRot;

        diff1.normalize();
        SbVec3f pullAxis = planeDir.cross( diff1 );
        pullAxis.normalize();
        final SbRotation pullRot = new SbRotation(pullAxis, getRadialFactor() * theta );

        final SbRotation totalRot = rollRot.operator_mul(pullRot);
        return totalRot;

    }
    else {
        // one point in, one point out, so rotate about
        // the center of the sphere from the point on the
        // sphere to the intersection of the plane and the
        // sphere closest to the point off the sphere

        final SbLine planeLine = new SbLine();
        final SbVec3f intersection = new SbVec3f();

        if (tol1) {
            planeLine.setValue(planePoint, p2);
        }
        else {
            planeLine.setValue(planePoint, p1);
        }

        if (! sphere.intersect(planeLine, intersection))
//#ifdef DEBUG
            SoDebugError.post("SbSphereSectionProjector::getRotation",
                               "Couldn't intersect plane line with sphere");
//#else
        /* Do nothing */;
//#endif

        if (tol1) {
            // went off sphere
            return new SbRotation(
                p1.operator_minus( sphere.getCenter()),
                intersection.operator_minus( sphere.getCenter()));
        }
        else {
            // came on to sphere
            // "Hey cutie. You've got quite a radius..."
            return new SbRotation(
                intersection.operator_minus( sphere.getCenter()),
                p2.operator_minus( sphere.getCenter()));
        }
    }

}

}
