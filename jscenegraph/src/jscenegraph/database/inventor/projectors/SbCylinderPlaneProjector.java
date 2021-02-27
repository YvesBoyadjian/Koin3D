/**
 * 
 */
package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */
public class SbCylinderPlaneProjector extends SbCylinderSectionProjector {

    public SbCylinderPlaneProjector() {
    	this(.9f);
    }
    public SbCylinderPlaneProjector(float edgeTol) {
    	this(edgeTol, true);
    }
    public SbCylinderPlaneProjector(float tol, boolean orient) {
    	super(tol,orient);
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
    SbCylinderPlaneProjector newProjector = new SbCylinderPlaneProjector();

    //(*newProjector) = (*this);
    newProjector.copyFrom(this);

    return newProjector;
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
    if (! tolPlane.intersect(workingLine, planeIntersection))
//#ifdef DEBUG
        SoDebugError.post("SbCylinderPlaneProjector::project",
                           "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif
    
    final SbVec3f cylIntersection = new SbVec3f(), dontCare = new SbVec3f();
    boolean hitCyl;
    if ( intersectFront == true )
        hitCyl = cylinder.intersect(workingLine, cylIntersection, dontCare);
    else
        hitCyl = cylinder.intersect(workingLine, dontCare, cylIntersection);

    if (hitCyl == false ) {
        // missed the cylinder, so hit the plane
        result.copyFrom( planeIntersection);
    }
    else {
        // See if the hit on the cylinder is within tolerance.
        // Project it onto the plane, and find the distance to
        // the planeLine.

        final SbLine projectLine = new SbLine(cylIntersection, cylIntersection.operator_add(planeDir));
        final SbVec3f projectIntersection = new SbVec3f();
        if (! tolPlane.intersect(projectLine, projectIntersection))
//#ifdef DEBUG
            SoDebugError.post("SbCylinderPlaneProjector::project",
                               "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif

        final SbVec3f vecToPoint = projectIntersection.operator_minus( 
                             planeLine.getClosestPoint(projectIntersection));
        float dist = vecToPoint.length();
        
        if (dist < tolDist)
            result.copyFrom( cylIntersection);
        else
            result.copyFrom( planeIntersection);
    }

    lastPoint.copyFrom( result);
    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//  Get a rotation based on two points on this projector.
//  Rotations are _always_ about the axis of the cylinder.
//
// Use: virtual protected
//
////////////////////////////////////////////////////////////////////////

public SbRotation
getRotation(final SbVec3f p1, final SbVec3f p2)
{
    boolean tol1 = isWithinTolerance(p1);
    boolean tol2 = isWithinTolerance(p2);
    return getRotation(p1, tol1, p2, tol2);
}

public SbRotation
getRotation(
    final SbVec3f p1, boolean tol1,
    final SbVec3f p2, boolean tol2)
    
{
    if (tol1 && tol2) {
        // Both points in tolerance, rotate about
        // cylinder's axis.

        // Find perpendiculars to from cylinder's axis to each
        // point.
        final SbVec3f v1 = p1.operator_minus( cylinder.getAxis().getClosestPoint(p1));
        final SbVec3f v2 = p2.operator_minus( cylinder.getAxis().getClosestPoint(p2));
        
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
    else if (!tol1 && !tol2) {
        final SbVec3f v1 = p1.operator_minus( planeLine.getClosestPoint(p1));
        final SbVec3f v2 = p2.operator_minus( planeLine.getClosestPoint(p2));
        if ( v1.dot( v2 ) < 0.0 ) {
            // points are on opposite sides of the cylinder.
            // Break this rotation up into 3 parts.
            // [1] p1 to ptOnCylP1Side,
            // [2] ptOnCylP1Side to ptOnCylP2Side,
            // [3] ptOnCylP2Side to p2.

            // Find the points on planeLine that are closest to p1 and p2
            final SbVec3f linePtNearestP1 = new SbVec3f(planeLine.getClosestPoint(p1));
            final SbVec3f linePtNearestP2 = new SbVec3f(planeLine.getClosestPoint(p2));

            // Find the directions that go from the points in the line towards
            // p1 and p2
            final SbVec3f dirToP1 = p1.operator_minus( linePtNearestP1);
            final SbVec3f dirToP2 = p2.operator_minus(linePtNearestP2);
            dirToP1.normalize();
            dirToP2.normalize();

            // Find the points on the cylinder nearest p1 and p2.
            final SbVec3f ptOnCylP1Side = linePtNearestP1.operator_add( dirToP1.operator_mul( tolDist));
            final SbVec3f ptOnCylP2Side = linePtNearestP2.operator_add( dirToP2.operator_mul( tolDist));

            return
                getRotation(p1, false, ptOnCylP1Side, false).operator_mul(
                getRotation(ptOnCylP1Side, true, ptOnCylP2Side, true)).operator_mul(
                getRotation(ptOnCylP2Side, false, p2, false));
        }
        else {
            // Points are on same side of the cylinder.
            // Rotate from one to the other, and only
            // keep portion perpendicular to the cylinder's axis.

            final SbVec3f diff = v2.operator_minus(v1);
            
            float d = diff.length();
            
            // moving a distance of 2*PI*radius is equivalent to 
            // rotating through an angle of 2*PI.
            // So,  (d / 2*PI*radius) = (angle / 2*PI);
            // angle = d / radius

            float angle = (cylinder.getRadius()==0.0) 
                          ? 0 : (d / cylinder.getRadius());

            final SbVec3f rotAxis = planeDir.cross(v1);
            
            // Moving towards or moving away from cylinder.
            if (v2.length() > v1.length())
                return new SbRotation(rotAxis, angle);
            else
                return new SbRotation(rotAxis, -angle);
        }
    }
    else {
        // One point in tolerance, one point out of tolerance.
        // (Pretend that the two points lie on a line
        // that is perpendicular to the axis of the cyl.)
        // Find the point on both the plane and cylinder that
        // is on that line.
        // Rotate twice:
        // (1) from the point on the cylinder to intersection
        // (2) from intersection to point off cylinder

        // offCylinderPt is the one that isn't within tolerance
        final SbVec3f offCylinderPt = (tol1) ? p2 : p1;

        // Find point on planeLine closest to offCylinderPt
        final SbVec3f linePtNearest = new SbVec3f(planeLine.getClosestPoint(offCylinderPt));

        // Find direction that goes from linePtNearest towards offCylinderPt
        final SbVec3f dirToOffCylinderPt = offCylinderPt.operator_minus(linePtNearest);
        dirToOffCylinderPt.normalize();

        // Find point on the cylinder nearest offCylinderPt
        SbVec3f ptOnCylinder = linePtNearest.operator_add( dirToOffCylinderPt.operator_mul(tolDist));

        if (tol1) {

            // p1 is on cyl, p2 off - went off cylinder
            return
                getRotation(p1, true, ptOnCylinder, true).operator_mul(
                getRotation(ptOnCylinder, false, p2, false));
        }
        else {

            // p1 is off cyl, p2 on - came on to cylinder
            return
                getRotation(p1, false, ptOnCylinder, false).operator_mul(
                getRotation(ptOnCylinder, true, p2, true));
        }
    }
}

}
