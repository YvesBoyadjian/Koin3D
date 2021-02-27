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
 |      This file contains the definition of the SbSphereProjector
 |      class. This is an abstract base class for projectors
 |      that somehow use a sphere in their projection.
 |        
 |   Author(s)          : Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.port.Mutable;



////////////////////////////////////////////////////////////////////////////////
//! Sphere projector.
/*!
\class SbSphereProjector
\ingroup Projectors
SbSphereProjector 
is an abstract base class for projectors that use a sphere in
their projection.
Sphere projectors are typically used to write
interactive 3D manipulators and viewers.

\par See Also
\par
SbCylinderProjector, SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SbSphereProjector extends SbProjector implements Mutable {

    //! Are intersections done on the front half (if not, they're done on
      //! the back half) of the sphere?
      protected boolean      intersectFront;
  
      protected final SbSphere    sphere = new SbSphere();                 //!< The sphere being used.
      protected boolean      orientToEye;            //!< TRUE if always oriented to eye.
      protected boolean      needSetup;              //!< Set TRUE whenever sphere, work space
                                          //! or orientation changes.
  
      //! cached last point on this projector
      protected final SbVec3f     lastPoint = new SbVec3f();
      

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors
//
// Use: protected
//
////////////////////////////////////////////////////////////////////////

public SbSphereProjector(boolean orient)
{ super();

    orientToEye = false;
    setSphere(new SbSphere(new SbVec3f(0,0,0), 1.0f));
    setOrientToEye(orient);
    setFront(true);
}

public SbSphereProjector(
    final SbSphere s,
    boolean orient)
    { super();

    orientToEye = false;
    setSphere(s);
    setOrientToEye(orient);
    setFront(true);
}

      
     public void
       setSphere(final SbSphere s)
       {
           sphere.copyFrom(s);
           needSetup = true;
      }

public void setOrientToEye(boolean b)
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


     //! Apply the projector using the given point, returning the
        //! point in three dimensions that it projects to.
        //! The point should be normalized from 0-1, with (0,0) at the lower-left.
       public abstract SbVec3f     project(final SbVec2f point);
    

public boolean isPointInFront(final SbVec3f point )
{
    final SbViewVolume viewVol = new SbViewVolume(getViewVolume());
    boolean         ptInFront = true;
    if ( viewVol.getProjectionType() == SbViewVolume.ProjectionType.PERSPECTIVE ) {
        final SbVec3f lclProjPt = new SbVec3f();
        worldToWorking.multVecMatrix( viewVol.getProjectionPoint(), lclProjPt);
        SbVec3f cntrToProj = lclProjPt.operator_minus( sphere.getCenter());
        SbVec3f cntrToInput = point.operator_minus( sphere.getCenter());
        if ( cntrToInput.dot( cntrToProj ) < 0.0 )
            ptInFront = false;
    }
    else {
        final SbVec3f lclZDir = new SbVec3f();
        worldToWorking.multDirMatrix( viewVol.zVector(), lclZDir );
        SbVec3f cntrToInput = point.operator_minus( sphere.getCenter());
        if ( cntrToInput.dot( lclZDir ) < 0.0 )
            ptInFront = false;
    }
    return ptInFront;
}

public void setWorkingSpace(final SbMatrix space)
{
    super.setWorkingSpace(space);
    needSetup = true;
}

       
        /**
      * Apply the projector using the given point, returning the point in three dimensions 
      * that it projects to. 
      * This also returns in rot a rotation on the surface of the sphere from the last 
      * projected point to this one. 
      * The passed point should be normalized (i.e. lie in the range [0.0,1.0]), 
      * with (0,0) at the lower-left. 
      * 
      * @param point
      * @param rot
      * @return
      */
     public SbVec3f
      projectAndGetRotation(
          final SbVec2f point, final SbRotation rot)
      {
          SbVec3f oldPoint = new SbVec3f(lastPoint);
          SbVec3f newPoint = new SbVec3f(project(point));
          rot.copyFrom(getRotation(oldPoint, newPoint));
          return newPoint;
      }
     
     //! Get a rotation given two points on this sphere projector.
        //! The rotation will be on the surface of the sphere.
     public abstract SbRotation  getRotation(final SbVec3f point1,
                                        final SbVec3f point2);

 	@Override
 	public void copyFrom(Object other) {
 		SbSphereProjector otherSbSphereProjector = (SbSphereProjector)other;
 		
 	    //! Are intersections done on the front half (if not, they're done on
 	      //! the back half) of the sphere?
 	      intersectFront = otherSbSphereProjector.intersectFront;
 	  
 	      sphere.copyFrom(otherSbSphereProjector.sphere);                 //!< The sphere being used.
 	      orientToEye = otherSbSphereProjector.orientToEye;            //!< TRUE if always oriented to eye.
 	      needSetup = otherSbSphereProjector.needSetup;              //!< Set TRUE whenever sphere, work space
 	                                          //! or orientation changes.
 	  
 	      //! cached last point on this projector
 	      lastPoint.copyFrom(otherSbSphereProjector.lastPoint);
 		
 		super.copyFrom(other);
 	}

	public void destructor() {
		super.destructor();
		
	}
}
