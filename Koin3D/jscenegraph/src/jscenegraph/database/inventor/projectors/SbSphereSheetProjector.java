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
 |      This file defines the SbSphereSheetProjector class. This is a
 |      sphere with a hyperbolic sheet draped over it. As the
 |      mouse moves away from the sphere, rotation gradually
 |      turns into pure roll.
 |
 |      This projector is good for trackballs that want to do
 |      constinuous roll. It allows pure roll as you move away
 |      from the sphere. (This projector uses the same paradigm 
 |      as the "Flip" demo.)
 |
 |      Warning: This projector tends to become unstable when
 |      the mouse position is beyond about 2*radius from the
 |      center of the sphere. Therefore, it is best to use this
 |      as a large, window-sized trackball.
 |
 |   Author(s)          : Howard Look, Gavin Bell, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Sphere-sheet projector.
/*!
\class SbSphereSheetProjector
\ingroup Projectors
SbSphereSheetProjector 
projects a window space point (usually based on the mouse location)
onto the surface of a sphere with a hyperbolic sheet draped over it.
This allows smooth transitions onto and off of the sphere.
Two projected points can produce a rotation
about the sphere's center.
When the mouse position projects on to the sheet, the
rotations will be as if the sheet is being dragged,
causing the sphere to roll beneath it.


Incremental changes (delta rotation) can be computed during
interactive sessions. Sphere projectors are typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderProjector, SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector
*/
////////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public class SbSphereSheetProjector extends SbSphereProjector implements Mutable {

    //! The projection point in working space.
    protected final   SbVec3f             workingProjPoint = new SbVec3f();
  
      //! Information about the plane used for intersection testing.
    protected final SbVec3f     planePoint = new SbVec3f();             //!< point on plane
    protected final SbVec3f     planeDir = new SbVec3f();               //!< normal direction
      float       planeDist;              //!< distance from sphere center
      protected final SbPlane     tolPlane = new SbPlane();               //!< the plane itself
  	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////
public SbSphereSheetProjector() {
	this(true);
}

      
public SbSphereSheetProjector(boolean orient)
{ super(orient);

}

//! Constructors. The first uses a default sphere centered at the origin
//! with radius 1.0; the sphere is supplied in the second.
//! The \b orientToEye 
//! parameter determines whether the sheet is perpendicular to the
//! eye, or perpendicular to the sphere's Z axis.  Setting that parameter to TRUE
//! (the default) specifies that the sheet be perpendicular to the
//! eye, which is most often the desired behavior.
//! 
//! 
//! The default view volume is undefined, and the working space is identity.
// java port
public SbSphereSheetProjector(final SbSphere sph) {
                       super(sph, true);
}

public SbSphereSheetProjector(
    final SbSphere s,
    boolean orient)
    { super(s, orient);

}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns an instance that is a copy of this instance. The caller
//    is responsible for deleting the copy when done.
//
// Use: public, virtual
//

public SbProjector copy()
//
////////////////////////////////////////////////////////////////////////
{
    SbSphereSheetProjector newProjector = new SbSphereSheetProjector();

    (newProjector).copyFrom(this);

    return newProjector;
}
      
	/**
	 * Apply the projector using the given point, returning the point 
	 * in three dimensions that it projects to. 
	 * The point should be normalized from 0-1, with (0,0) at the lower-left. 
	 * 
	 * @param point
	 * @return
	 */
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Projects a mouse point to three space.
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
        setupPlane();

    final SbVec3f planeIntersection = new SbVec3f();

    final SbVec3f sphereIntersection = new SbVec3f(), dontCare = new SbVec3f();
    boolean hitSphere;
    if ( intersectFront == true )
        hitSphere = sphere.intersect(workingLine, sphereIntersection, dontCare);
    else
        hitSphere = sphere.intersect(workingLine, dontCare, sphereIntersection);

    if (hitSphere) {
        // drop the sphere intersection onto the tolerance plane
        
        final SbLine projectLine = new SbLine(sphereIntersection, sphereIntersection.operator_add(planeDir));
        if (! tolPlane.intersect(projectLine, planeIntersection))
//#ifdef DEBUG
//            SoDebugError::post("SbSphereSheetProjector::project",
//                               "Couldn't intersect working line with plane");
//#else
        /* Do nothing */;
//#endif
    }
    else if (! tolPlane.intersect(workingLine, planeIntersection))
//#ifdef DEBUG
//        SoDebugError::post("SbSphereSheetProjector::project",
//                           "Couldn't intersect with plane");
//#else
        /* Do nothing */;
//#endif
    
    // Two possibilities:
    // (1) Intersection is on the sphere inside where the sheet
    //      hits it
    // (2) Intersection is off sphere, or on sphere but on the sheet
    float dist = (planeIntersection.operator_minus(planePoint)).length();
        
    // distance on the plane from the center
    // to the projection of the point where 
    // the sphere meets the hyperbolic sheet
    float sphereSheetDist = sphere.getRadius() * (float)M_SQRT1_2;

    if (dist < sphereSheetDist) {
        // project onto sphere
//#ifdef DEBUG
//        if (! hitSphere)
//            SoDebugError::post("SbSphereSheetProjector::project",
//                               "Couldn't intersect with sphere");
//#endif
        result.copyFrom(sphereIntersection);
    }
    else {
        // The equation of a hyperbola in the first quadrant with the
        // axes as its limits is :  y = f * 1/x
        // At the 45 degree point of both the sphere and the
        // hyperbola, x and y will be equal, and sqrt(x^2+y^2) will
        // equal radius.  Therefore, letting x = y:
        // sqrt(2 * x^2) = r --> r^2 / 2 = x^2
        // and:
        // x = f / x --> f = x^2 = r^2 / 2
        //
        // Sb, the equation of the hyperbola is just:
        // y = r^2 / 2 * 1/x
        //
        // In terms of a sphere in working space, y
        // is the offsetDist from the plane,
        // and x is dist from the planePoint.
        
        float f = sphere.getRadius()*sphere.getRadius()/2.0f;
        float offsetDist = f / dist;
            
        final SbVec3f offset = new SbVec3f();
        if (orientToEye) {
            if (viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE)
                offset.copyFrom( workingProjPoint.operator_minus(planeIntersection));
            else
                worldToWorking.multDirMatrix(viewVol.zVector(), offset);
                
            offset.normalize();
        }
        else {
            offset.setValue(0,0,1);
        }
        if ( intersectFront == false )
            offset.operator_mul_equal( -1.0f);
            
        offset.operator_mul_equal( offsetDist);
        result.copyFrom( planeIntersection.operator_add( offset));
    }

    lastPoint.copyFrom( result);
    return result;
}
	// Sets up the plane use to project on to. 
	//  Sets up the plane used for intersections.
	protected void setupPlane() {
	     // Find the intersection point on the tolerance plane.
		       // The plane may either be in working space, or always
		       // oriented towards the eye.
		   
		       // find orientation of the tolerance plane
		       if (orientToEye) {
		           if (viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE) {
		               // find the projection point in working space coords
		               worldToWorking.multVecMatrix(
		                   viewVol.getProjectionPoint(), workingProjPoint);
		   
		               planeDir.copyFrom( workingProjPoint.operator_minus(sphere.getCenter()));
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
		   
		       if ( intersectFront == false )
		           planeDir.operator_mul_equal( -1.0f);
		   
		       // for SphereSheetProjectors, the plane always passed
		       // through the origin
		       planeDist = 0.0f;
		   
		       // plane given direction and distance to origin
		       planePoint.copyFrom( sphere.getCenter());
		       tolPlane.copyFrom( new SbPlane(planeDir, planePoint));
		   
		       needSetup = false;
		  		
	}
	@Override
	public SbRotation getRotation(SbVec3f p1, SbVec3f p2) {
	     // Bad Method - 
		       // Makes the amount of rotation falls off as you travel along
		       // the hyperbolic sheet, which makes z-rotations more and more
		       // dominant as you get farther away from the center of the window.
		       // return SbRotation(
		       //    p1 - sphere.getCenter(),
		       //    p2 - sphere.getCenter());
		   
		       // Good Method -    
		       // Use the axis defined by the vectors (p1, p2),
		       // but use the 2D distance between the points (p1, p2) to generate
		       // the angle to rotate through.  The amount to rotate will be
		       // equivalent to the rotation that would occur if both p1 and p2
		       // were on the ballSize-sized sphere, so the equation is:
		       // sin(1/2 angle) = (1/2 d) / ballSize
		       // angle = 2 * asin(d / (2 * ballSize))
		       //
		       // Caveat: The angle gets so small as you
		       // move away from the sphere, the cross product becomes
		       // unreliable. Thus this projector is only really good
		       // for large trackballs.
		   
		       SbVec3f diff = p2.operator_minus(p1);
		       float d = diff.length();
		   
		       // Check for degenerate cases
		       float t = d / (2.0f * sphere.getRadius());
		       if (t < 0.000001)
		           return SbRotation.identity(); // Too close; no rotation
		       else if (t > 1.0)
		           return SbRotation.identity(); // Too much; bag it
		   
		       float angle = 2.0f * (float)Math.asin(t);
		   
		       SbVec3f v1 = new SbVec3f(p1.operator_minus(planePoint));
		       SbVec3f v2 = new SbVec3f(p2.operator_minus(planePoint));
		       SbVec3f axis = v1.cross(v2);
		       axis.normalize();
		   
		       return new SbRotation(axis, angle);
		   	}


	@Override
	public void copyFrom(Object other) {
		SbSphereSheetProjector otherSphereSheetProjector = (SbSphereSheetProjector)other;
		
	    //! The projection point in working space.
	    workingProjPoint.copyFrom(otherSphereSheetProjector.workingProjPoint);
	  
	      //! Information about the plane used for intersection testing.
	    planePoint.copyFrom(otherSphereSheetProjector.planePoint);             //!< point on plane
	    planeDir.copyFrom(otherSphereSheetProjector.planeDir);               //!< normal direction
	    planeDist = otherSphereSheetProjector.planeDist;              //!< distance from sphere center
	    tolPlane.copyFrom(otherSphereSheetProjector.tolPlane);               //!< the plane itself
		
		super.copyFrom(other);
	}
}
