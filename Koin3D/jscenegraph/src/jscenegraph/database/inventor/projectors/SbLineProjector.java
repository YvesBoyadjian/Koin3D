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
 |      This file contains the definition of the SbLineProjector
 |      class. This projects the mouse onto a line. 
 |        
 |   Author(s)          : Howard Look, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
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
//! Line projector.
/*!
\class SbLineProjector
\ingroup Projectors
SbLineProjector 
projects a 2D point, typically the location of the cursor, onto a 3D line.

\par See Also
\par
SbCylinderProjector, SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

public class SbLineProjector extends SbProjector {
	
	  

	protected final SbLine      line = new SbLine();
	protected final SbVec3f     lastPoint = new SbVec3f();
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public SbLineProjector()
{
    setLine(new SbLine(new SbVec3f(0,0,0), new SbVec3f(0,1,0)));
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
    SbLineProjector newProjector = new SbLineProjector();

    (newProjector).copyFrom(this);

    return newProjector;
}

	@Override
	public void copyFrom(Object other) {
		super.copyFrom(other);
		line.copyFrom(((SbLineProjector)other).line);
		lastPoint.copyFrom(((SbLineProjector)other).lastPoint);
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
setLine(final SbLine l)
{
    line.copyFrom(l);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Project onto this projector.
//
//  Originally this method created a working line by projecting the input
//  point into the scene, then transforming that line into workspace.
//  Afterwards, the point on the projector's line closest to the workingLine
//  was used as the result.
//
//  This technique provides good results when the mouse is on the line, 
//  but when it leaves the line, the closest point is not optimal.
//
//  The new method first transforms the projector's line into screen space,
//  then finds the nearest point on the screenSpaceLine to the input point.
//  It then uses this new screen space line and then performs the projection
//  as it used to.  
//
//  The result is that the screen point which we project into the scene
//  is now guaranteed to lie on the working line.
//
//  In short, first we find the closest point in screen space, then
//  we project that point back into work space.
//
// Use: public


public SbVec3f
project(final SbVec2f point)
//
////////////////////////////////////////////////////////////////////////
{
    // Convert two line points to world space
        final SbLine worldLine = new SbLine();
        workingToWorld.multLineMatrix( line, worldLine );
        final SbVec3f wldPt1 = new SbVec3f(worldLine.getPosition());
        final SbVec3f wldDir = new SbVec3f(worldLine.getDirection());
        final SbVec3f wldPt2 = wldPt1.operator_add(wldDir);

    // Convert two line points to normalized screen space.
        final SbVec3f nrmScnPt1 = new SbVec3f(), nrmScnPt2 = new SbVec3f();
        viewVol.projectToScreen( wldPt1, nrmScnPt1 );
        viewVol.projectToScreen( wldPt2, nrmScnPt2 );

    // Convert two line points and input point 
    // to viewPlane space, a screen space that's got view plane's aspect ratio:
        float vvW  = (viewVol.getWidth()  == 0.0) ? 1 : viewVol.getWidth();
        float vvH  = (viewVol.getHeight() == 0.0) ? 1 : viewVol.getHeight();
        final SbVec3f     vpPt1 = new SbVec3f( nrmScnPt1.getValueRead()[0] * vvW, nrmScnPt1.getValueRead()[1] * vvH, 0);
        final SbVec3f     vpPt2 = new SbVec3f( nrmScnPt2.getValueRead()[0] * vvW, nrmScnPt2.getValueRead()[1] * vvH, 0);
        final SbVec3f vpInPoint = new SbVec3f(     point.getValue()[0] * vvW,     point.getValue()[1] * vvH, 0);

    // Create the viewPlaneLine -- our line expressed in viewPlane space:
        final SbLine  viewPlaneLine = new SbLine( vpPt1, vpPt2 );

    // In viewplane space, find the closest point on our line to the cursor.
        final SbVec3f vpClosestPt = new SbVec3f(viewPlaneLine.getClosestPoint( vpInPoint ));
        vpClosestPt.setValue( vpClosestPt.getValueRead()[0], vpClosestPt.getValueRead()[1], 0 );

    // If we've got a perspective view, we may need to clamp the point we
    // choose so that it's not too close to the vanishing point.
    // Otherwise we'll just use our vpClosestPt

        final SbVec3f vpClampedPt = new SbVec3f(vpClosestPt);

        if ( viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE ) {

        // Find the vanishing point of our line in viewPlane space: 
            // Convert the direction of our line from world space into space 
            // after the affine matrix (i.e. just before the projection matrix)
            final SbMatrix vvAffine = new SbMatrix(), vvProj = new SbMatrix();
            viewVol.getMatrices( vvAffine, vvProj );
            final SbVec3f postAffineDir = new SbVec3f();
            vvAffine.multDirMatrix( wldDir, postAffineDir ); 

          // If the direction of the line is parallel to the view plane,
          // then the z component of postAffineDir is 0.
          // In this case, we will not need to clamp our point and moreover,
          // if we try we'll wind up dividing by zero pretty soon.
          if ( postAffineDir.getValueRead()[2] != 0.0 ) {

            // If we send a line out from (0,0,0) into the viewVolume towards 
            // postAffineDir, it will vanish at the same point as any other line
            // parallel to this direction.  Also, all points along this line 
            // will project to the same point on the near (or far) plane.
            // So a line connecting (0,0,0) and the point at postAffineDir will
            // intersect the near plane at the vanishing point.  Transforming 
            // any point on this line by vvProj will yield the same x,y result 
            // and the z component will vary with depth.
            // So multiply the postAffineDir as a vector through the projection
            // matrix and use the x,y for the vanishing point.
            final SbVec3f projVanish = new SbVec3f();
            vvProj.multVecMatrix( postAffineDir, projVanish );

            // Convert from [-1,1] range to [0,1] range for normalized coords.
            final SbVec3fSingle nrmScnVanish = new SbVec3fSingle();
            nrmScnVanish.getValue()[0] = (1.0f + projVanish.getValueRead()[0]) * 0.5f;
            nrmScnVanish.getValue()[1] = (1.0f + projVanish.getValueRead()[1]) * 0.5f;

            // Finally, get the vanishing point in viewPlane coords:
            SbVec3f vpVanish = new SbVec3f( nrmScnVanish.getValueRead()[0] * vvW, nrmScnVanish.getValueRead()[1] * vvH, 0 );

//#if 0
//            // Check that the vanishing point is correct:
//            // Project nrmScnVanish on the plane to see if it goes along wldDir:
//            SbVec2f nrmScnVanish2( nrmScnVanish[0], nrmScnVanish[1] );
//            SbLine vanishWorldLine;
//            viewVol.projectPointToLine( nrmScnVanish2, vanishWorldLine );
//            SbVec3f test = vanishWorldLine.getDirection();
//            fprintf(stderr,"wldDir = %f %f %f\n",wldDir[0],wldDir[1],wldDir[2]);
//            fprintf(stderr,"checkDir = %f %f %f\n", test[0], test[1],test[2]);
//#endif

        // The points vpPt1 and vpPt2 define the line in viewPlane space.
        // We can't go on the other side of the vanishing point from these 
        // defining points in screen space or the point will be undefined when 
        // we cast it into world space.
        // So clamp our selected point to lie on vpPt1's side of the vanishing 
        // point.  Since points near the vanishing point will also be incredibly
        // far away, introduce an (arbitrary) metric, VANISH_DELTA.
        // Our selection must be more than VANISH_DELTA times the average of
        // viewVolumeHeight and viewVolumeWidth from the vanishing point.
final float VANISH_DELTA =.01f;
            float vanishSafetyDist = VANISH_DELTA * .5f * (vvW + vvH);
//#undef VANISH_DELTA
            // Make pt0, the point from which we measure distances along vpLine.
            // It will be one extra unit away from vpVanish than safetyDist
            final SbVec3f pt0 = new SbVec3f(viewPlaneLine.getPosition());
            pt0.setValue( pt0.getValueRead()[0], pt0.getValueRead()[1], 0 );
            SbVec3f pt0ToVanishDir = vpVanish.operator_minus(pt0);
            pt0ToVanishDir.normalize();
            float   pt0ToVanishDist = vanishSafetyDist + 1.0f;
            pt0.copyFrom( vpVanish.operator_minus( pt0ToVanishDir.operator_mul(pt0ToVanishDist)));

            // Get vector and dist from pt0 to vpClosestPt
            SbVec3f pt0ToClosest = vpClosestPt.operator_minus(pt0);
            float   pt0ToClosestDist = pt0ToClosest.length();

            // If vpClosestPt is too far from pt0, clamp it:
            float   clampDist = pt0ToVanishDist - vanishSafetyDist;
            if (    (pt0ToClosestDist > clampDist) 
                 && (pt0ToClosest.dot(pt0ToVanishDir) > 0.0) ) {
                vpClampedPt.copyFrom( pt0.operator_add(pt0ToVanishDir.operator_mul(clampDist)));
            }
          }
        }

    // Convert result back into normalized screen space:
        SbVec2f nrmScnClampedPt = new SbVec2f( vpClampedPt.getValueRead()[0] / vvW, vpClampedPt.getValueRead()[1] / vvH);

    // Create a line in working space by projecting our point into the scene:
        final SbVec3f result = new SbVec3f(), whoCares = new SbVec3f();
        final SbLine workingLine = new SbLine(getWorkingLine( nrmScnClampedPt ));

    // Find point on the projector line closest to workingLine
        if (! line.getClosestPoints(workingLine, result, whoCares)) {
//#ifdef DEBUG
            SoDebugError.post("SbLineProjector::project",
                               "Couldn't get closest point");
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
    
    lastPoint.copyFrom(p2);
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
	// TODO Auto-generated method stub
	
}

	
}
