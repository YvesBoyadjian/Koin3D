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
 |      This file contains the definition of the SbPlaneProjector
 |      class. This projects the mouse onto a plane. If orientToEye
 |      is set to true, the plane will be oriented to the eye.
 |        
 |   Author(s)          : Howard Look, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Plane projector.
/*!
\class SbPlaneProjector
\ingroup Projectors
SbPlaneProjector 
projects the mouse onto a plane.
This is typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderProjector, SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbLineProjector, SbSpherePlaneProjector, SbSphereProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

public class SbPlaneProjector extends SbProjector {
	

	  protected final SbPlane     plane = new SbPlane();
	  protected boolean        orientToEye;    //!< true is plane should be oriented to eye.
    protected boolean        needSetup;      //!< Set true when plane or orientToEye changes.
    protected final SbPlane     nonOrientPlane = new SbPlane(); //!< Non-EyeOriented plane.

    protected final SbVec3f     lastPoint = new SbVec3f();      //!< Cached last point.

    //! Set and get the plane to use.
    public SbPlane      getPlane()                  { return plane; }
    //! Get whether the projector should be oriented towards the eye.
    public boolean                isOrientToEye()            { return orientToEye; }

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

    //! Constructor. The default plane passes through the origin and is
    //! oriented perpendicular to the Z axis.
    public SbPlaneProjector() {
    	this(false);
    }
    
public SbPlaneProjector(boolean orient)
{
    orientToEye = false;
    setPlane(new SbPlane(new SbVec3f(0,0,1), 0));
    setOrientToEye(orient);
}

//! Constructor that takes a plane.
public SbPlaneProjector(final SbPlane plane) {
	this(plane,false);
}

public SbPlaneProjector(
    final SbPlane p,
    boolean orient)
{
    orientToEye = false;
    setPlane(p);
    setOrientToEye(orient);
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
    SbPlaneProjector newProjector = new SbPlaneProjector();

    (newProjector).copyFrom(this);

    return newProjector;
}

	@Override
	public void copyFrom(Object other) {
		SbPlaneProjector otherProjector = (SbPlaneProjector)other;
		super.copyFrom(other);
		
		plane.copyFrom(otherProjector.plane);
		orientToEye = otherProjector.orientToEye;    //!< true is plane should be oriented to eye.
	    needSetup = otherProjector.needSetup;      //!< Set true when plane or orientToEye changes.
	    nonOrientPlane.copyFrom(otherProjector.nonOrientPlane); //!< Non-EyeOriented plane.

	    lastPoint.copyFrom(otherProjector.lastPoint);      //!< Cached last point.
		
	}
////////////////////////////////////////////////////////////////////////
//
// Description:
//  Misc set routines.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public void
setPlane(final SbPlane p)
{
    plane.copyFrom(p); nonOrientPlane.copyFrom(p);
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

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Sets up the plane orientation, if necessary.
//
// Use: public

public void
setupPlane()
//
////////////////////////////////////////////////////////////////////////
{
    if (orientToEye) {

        // Find the point that the old plane passes through that
        // is closest to the origin, and make the new plane pass
        // through that same point.
        SbVec3f planePoint = plane.getNormal().operator_mul(plane.getDistanceFromOrigin());

        final SbVec3f newPlaneDir = new SbVec3f();
        // Find the normal for the new plane that points towards
        // the eye.
        if (viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE) {
            // find the projection point in working space coords
            final SbVec3f workingProjPoint = new SbVec3f();
            worldToWorking.multVecMatrix(
                viewVol.getProjectionPoint(), workingProjPoint);
        
            newPlaneDir.copyFrom(workingProjPoint.operator_minus(planePoint));
        }
        else {
            // Use the projection direction in an orthographic
            // view vol
            worldToWorking.multDirMatrix(viewVol.zVector(), newPlaneDir);
        }        
        newPlaneDir.normalize();

        plane.copyFrom( new SbPlane(newPlaneDir, planePoint));
    }
    else {
        // Reset the plane to be non-EyeOriented.
        plane.copyFrom( nonOrientPlane);
    }

    needSetup = false;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//  Project onto this projector.
//
//  Originally this method created workingLine by projecting the input
//  point into the scene and transforming it into workspace.
//  The workingLine was intersected with the projector plane to
//  get the result.
//
//  This technique provides good results when the cursor lies over the plane.
//  However, planes such as the ground plane vanish to a horizon line on the
//  view plane.  If the mouse is on the wrong side of the horizon line, it does
//  not intersect the projector plane.
//
//  In this case we need to find the closest point on the horizon line to 
//  the cursor and use that point instead.
//
//  In practical application, points on the horizon are infinitely far away
//  so the selected point will actually have to be some small distance
//  away from the horizon line.
//
//  Note that the horizon line is a line in the view plane and finding this
//  line is non-trivial.  Once the horizon line is found, we work in 
//  viewPlane space to find the closest point to the cursor.
//
// Use: public


public SbVec3f
project(final SbVec2f point)
//
////////////////////////////////////////////////////////////////////////
{
        final SbVec3f result = new SbVec3f();

    // Set up the plane if necessary
        if (needSetup)
            setupPlane();

    // Start by assuming that we will have a succussful intersection using
    // the normalized screen point we were given:
        final SbVec2f nrmScnClampedPt = new SbVec2f(point);

    // NOTE: when transforming normals you have to use the inverse
    // transpose or the normals will scale incorrectly.

    // Transform the plane from work space to world space.
        final SbVec3f workNormal = new SbVec3f(plane.getNormal());
        final SbVec3f workPlnPnt = new SbVec3f(workNormal.operator_mul( plane.getDistanceFromOrigin()));

        final SbVec3f wldNormal = new SbVec3f(), wldPlnPnt = new SbVec3f();
        final SbMatrix inverseTranspose = new SbMatrix(workingToWorld.inverse().transpose());
        inverseTranspose.multDirMatrix( workNormal, wldNormal );
        workingToWorld.multVecMatrix( workPlnPnt, wldPlnPnt );

    // Transform the plane from world space into space 
    // after the affine matrix (i.e. just before the projection matrix)
        final SbMatrix vvAffine = new SbMatrix(), vvProj = new SbMatrix();
        viewVol.getMatrices( vvAffine, vvProj );
        inverseTranspose.copyFrom(vvAffine.inverse().transpose());
        final SbVec3f postAffineNormal = new SbVec3f(), postAffinePlnPnt = new SbVec3f();
        inverseTranspose.multDirMatrix( wldNormal, postAffineNormal ); 
        postAffineNormal.normalize();
        vvAffine.multVecMatrix( wldPlnPnt, postAffinePlnPnt );

    // If now the plane is edge-on to the eye, there's no way we can give a 
    // good answer.  So just return the plane's origin and bail out:
    // The eye looks in the -z direction in postAffine space.
        SbVec3f postAffineEyeDir = new SbVec3f( 0, 0, -1 );
        // First test: see if plane normal is perpendicular to projection dir.
        if ( postAffineNormal.dot( postAffineEyeDir ) == 0.0 ) {
            boolean maybeOk = true;

            if ( viewVol.getProjectionType() == SbViewVolume.ProjectionType.ORTHOGRAPHIC ) {
                // If orthographic, then this will always be an impossible
                // plane to project into:
                maybeOk = false;
            }
            else {
                // Perpsective view: test to see if (0,0,0) lies within the 
                // plane. If so, then we are absolutely edge on.
                if ( postAffineNormal.dot( postAffinePlnPnt ) == 0.0 )
                    maybeOk = false;
            }

            if ( ! maybeOk ) {
//#ifdef DEBUG
                SoDebugError.post("SbPlaneProjector::project",
                                   "Plane is edge-on to view."+
                                   "Returning the plane origin");
//#endif
                result.copyFrom( plane.getNormal().operator_mul( plane.getDistanceFromOrigin()));
                return result;
            }
        }

    // If at this point the plane is flat against the view plane, all 
    // cursor positions will yield a valid intersection with the plane.
        boolean isFlatAgainstViewPlane = false;
        if ( Math.abs( postAffineNormal.dot( postAffineEyeDir )) == 1.0 ) 
            isFlatAgainstViewPlane = true;

    // It is important that our postAffinePlnPnt be in front of the eye.
    // Otherwise it will project onto the viewplane incorrectly.
    // For the remaining tasks, it is not important that it be the exact same 
    // point in space, just that it lies in the plane.  So check where it is 
    // and move it if need be.
        if ( (!isFlatAgainstViewPlane) && postAffinePlnPnt.getValueRead()[2] >= 0.0 ) {
            // Get a direction lying within the plane that is also within the
            // viewplane:
            SbVec3f dirInViewPlane = new SbVec3f(postAffineNormal.cross( new SbVec3f( 0,0,-1) ));
            // The second cross product give direction within plane and also
            // perpendicular to view plane.
            SbVec3f dirInMyPlane   = new SbVec3f(dirInViewPlane.cross( postAffineNormal ));

//#ifdef DEBUG
            if ( dirInMyPlane.getValueRead()[2] == 0.0 ) {
                SoDebugError.post("SbPlaneProjector::project",
                                   "Bad value for direction in plane");
            }
//#endif
            // Find out how far we need to travel along the direction
            // before we hit the z=0 plane:
            // postAffinePlnPnt[2] + distToGo * dirInMyPlane[2] = 0
            float distToGo = (-1 * (postAffinePlnPnt.getValueRead()[2] )) / dirInMyPlane.getValueRead()[2];
            // Go a bit further to insure negative z
            postAffinePlnPnt.copyFrom(postAffinePlnPnt.operator_add(dirInMyPlane.operator_mul(1.1f * distToGo)));
        }

    // If we've got a perspective view, we may need to clamp the point we
    // choose so that it's not too close to the horizon line.
    // Otherwise we'll just use our nrmScnClampedPt;

        if ( viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE 
            && !isFlatAgainstViewPlane ) {

            float vvW  = (viewVol.getWidth()  == 0.0) ? 1 : viewVol.getWidth();
            float vvH  = (viewVol.getHeight() == 0.0) ? 1 : viewVol.getHeight();

            // To find horizon line, get two directions within the plane,
            // find their vanishing points and connect them.

            // Get two different directions within the plane:
            // Note that since postAffineEyeDir & normal are not parallel
            // this is always a valid calculation.
            // Since cross product is always perp to both vecs, this dir will
            // lie in the viewplane:
            SbVec3f dirInViewPlane = new SbVec3f(postAffineEyeDir.cross( postAffineNormal ));
            SbVec3f postAffineDir1 = new SbVec3f(dirInViewPlane.cross( postAffineNormal ));
            SbVec3f postAffineDir2 = new SbVec3f((dirInViewPlane.operator_add( postAffineDir1) ).operator_mul(0.5f));
            postAffineDir2.normalize();

            // Get their vanishing points in normalized screen space.
            // Send a line out from (0,0,0) into the viewVolume towards 
            // each direction. Each vanishes at the same point as any other line
            // parallel to it.  Also, all points along that line project
            // to the same point on the near (or far) plane. So a line 
            // connecting (0,0,0) and the point at postAffineDir1 (or 2) will
            // intersect the near plane at its vanishing point.  Transforming 
            // any point on this line by vvProj will yield the same x,y result 
            // and the z component will vary with depth.
            // So multiply the postAffineDirs as vectors through the projection
            // matrix and use the x,y for the vanishing points.
            final SbVec3f projVanish1 = new SbVec3f(), projVanish2 = new SbVec3f(), projPlnPnt = new SbVec3f();
            vvProj.multVecMatrix( postAffineDir1,   projVanish1 );
            vvProj.multVecMatrix( postAffineDir2,   projVanish2 );
            vvProj.multVecMatrix( postAffinePlnPnt, projPlnPnt );

            // Transform from [-1,1] range to [0,1] range for normalized coords.
            final SbVec3fSingle nrmScnVanish1 = new SbVec3fSingle(), nrmScnVanish2 = new SbVec3fSingle(), nrmScnPlnPnt = new SbVec3fSingle();
            nrmScnVanish1.getValue()[0] = (1.0f + projVanish1.getValueRead()[0]) * 0.5f;
            nrmScnVanish1.getValue()[1] = (1.0f + projVanish1.getValueRead()[1]) * 0.5f;
            nrmScnVanish2.getValue()[0] = (1.0f + projVanish2.getValueRead()[0]) * 0.5f;
            nrmScnVanish2.getValue()[1] = (1.0f + projVanish2.getValueRead()[1]) * 0.5f;
            nrmScnPlnPnt.getValue()[0]  = (1.0f + projPlnPnt.getValueRead()[0])  * 0.5f;
            nrmScnPlnPnt.getValue()[1]  = (1.0f + projPlnPnt.getValueRead()[1])  * 0.5f;

            // Finally, get the vanishing points in viewPlane coords:
            SbVec3f vpVanish1 = new SbVec3f(nrmScnVanish1.getValueRead()[0] * vvW,nrmScnVanish1.getValueRead()[1] * vvH, 0);
            SbVec3f vpVanish2 = new SbVec3f(nrmScnVanish2.getValueRead()[0] * vvW,nrmScnVanish2.getValueRead()[1] * vvH, 0);
            SbVec3f  vpPlnPnt = new SbVec3f( nrmScnPlnPnt.getValueRead()[0] * vvW, nrmScnPlnPnt.getValueRead()[1] * vvH, 0);

            // Connect them to form the horizon:
            SbLine horizon = new SbLine( vpVanish1, vpVanish2 );

            // Is the input point on the opposite side of the horizon from
            // our plane?  Or on the same side but very close?
            // Introduce a new metric, lineToLine.
            // This is the distance on the view plane between the horizon line
            // and the line where our projection plane slices through the
            // viewplane.
            // Introduce an (arbitrary) amount, VANISH_DELTA.
            // If too near the horizon, select a point closer than the horizon 
            // by an amount of VANISH_DELTA times lineToLine.

            // First, determine sliceLine, the line where the projectionPlane
            // slices through the viewPlane:
// BEGIN STUFF FROM GRAPHICS GEMS 3, p.235 for intersecting 2 planes:
            final SbVec3f normM = new SbVec3f(postAffineNormal);
            float   distI = -1 * normM.dot( postAffinePlnPnt );
            final SbVec3f normN = new SbVec3f( 0, 0, -1 );
            float   distJ = -1 * viewVol.getNearDist();
            final SbVec3f normL = new SbVec3f(normM.cross( normN ));
            int indexU, indexV, indexW;
            if (   Math.abs(normL.getValueRead()[0]) > Math.abs(normL.getValueRead()[1]) 
                && Math.abs(normL.getValueRead()[0]) > Math.abs(normL.getValueRead()[2]) ) {
                indexW = 0; indexU = 1; indexV = 2;
            }
            else if ( Math.abs(normL.getValueRead()[1]) > Math.abs(normL.getValueRead()[2]) ) {
                indexW = 1; indexU = 0; indexV = 2;
            }
            else {
                indexW = 2; indexU = 0; indexV = 1;
            }
            final SbVec3fSingle slicePoint1 = new SbVec3fSingle(), slicePoint2 = new SbVec3fSingle();
            float denom =   (normM.getValueRead()[indexU] * normN.getValueRead()[indexV]) 
                          + (normM.getValueRead()[indexV] * normN.getValueRead()[indexU]);
            slicePoint1.getValue()[indexU] =
                ((normM.getValueRead()[indexV] * distJ) - (normN.getValueRead()[indexV] * distI)) / denom;
            slicePoint1.getValue()[indexV] =
                ((normN.getValueRead()[indexU] * distI) - (normM.getValueRead()[indexU] * distJ)) / denom;
            slicePoint1.getValue()[indexW] = 0.0f;
            normL.normalize();
// END STUFF FROM GRAPHICS GEMS 3, p.235 for intersecting 2 planes:
            // Convert slicePoint1 to viewPlane coords:
            final SbVec3fSingle vpSlicePoint = new SbVec3fSingle();
            vvProj.multVecMatrix( slicePoint1,   vpSlicePoint );
            vpSlicePoint.getValue()[0] = ((1.0f + vpSlicePoint.getValueRead()[0]) * 0.5f) * vvW;
            vpSlicePoint.getValue()[1] = ((1.0f + vpSlicePoint.getValueRead()[1]) * 0.5f) * vvH;
            vpSlicePoint.getValue()[2] = 0.0f;
            // Now calculate lineToLine, the distance between vpSlicePoint and
            // the horizon:
            SbVec3f nearToSlicePoint = new SbVec3f(horizon.getClosestPoint( vpSlicePoint ));
            float lineToLine = (vpSlicePoint.operator_minus( nearToSlicePoint)).length();
            // If lineToLine is as big as the average of the viewplane width
            // and height, then the plane is pretty flat on. Reduce lineToLine
            // to be that size.
            float sizeAverage = 0.5f * (vvW + vvH);
            if (lineToLine > sizeAverage)
                lineToLine = sizeAverage;
final float VANISH_DELTA =.01f;
            float vanishSafetyDist = VANISH_DELTA * lineToLine;
//#undef VANISH_DELTA
            SbVec3f vpInPoint = new SbVec3f(     point.getValueRead()[0] * vvW,     point.getValueRead()[1] * vvH, 0);
            SbVec3f nearToInPoint = new SbVec3f(horizon.getClosestPoint( vpInPoint ));
            SbVec3f nearToPlnPnt  = new SbVec3f(horizon.getClosestPoint( vpPlnPnt ));
            SbVec3f vec1 = vpInPoint.operator_minus( nearToInPoint);
            SbVec3f vec2 = vpPlnPnt.operator_minus( nearToPlnPnt);
            float vec1Length = vec1.length();
            if ( vec1.dot( vec2 ) < 0.0 || vec1Length < vanishSafetyDist ) {
                SbVec3f mvDir = new SbVec3f(vec2);
                mvDir.normalize();
                SbVec3f vpClampedPt = nearToInPoint.operator_add( mvDir.operator_mul(vanishSafetyDist));
                nrmScnClampedPt.setValue( vpClampedPt.getValueRead()[0] / vvW, 
                                          vpClampedPt.getValueRead()[1] / vvH );
            }

        }

    // Project our point into a line in working space 
        SbLine workingLine = getWorkingLine( nrmScnClampedPt );

    // Intersect that line with the working space plane
        if (! plane.intersect(workingLine, result)) {
//#ifdef DEBUG
            SoDebugError.post("SbPlaneProjector::project",
                               "Couldn't intersect with plane");
//#endif
        }

    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Get the vector based on one or two mouse positions.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public SbVec3f
getVector(final SbVec2f m1, final SbVec2f m2)
{
    SbVec3f p1 = project(m1);
    SbVec3f p2 = project(m2);
    
    lastPoint.copyFrom( p2);
    return p2.operator_minus(p1);
}

public SbVec3f
getVector(final SbVec2f m)
{
    SbVec3f p = project(m);
    SbVec3f result = p.operator_minus(lastPoint);
    lastPoint.copyFrom(p);
    return result;
}

	public void destructor() {
		//super.destructor();
	}

}
