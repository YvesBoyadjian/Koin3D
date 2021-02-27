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
 |      This file contains the definition of SbProjector. These take
 |      a point in two dimensions and project it to a point in three
 |      dimensions. Typically, the point is the mouse position.
 |
 |      SbProjectors are typically used by manipulators to turn the
 |      mouse position into a rotation or translation.
 |
 |   Author(s)          : Howard Look, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.projectors;

import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Base class for representing projectors.
/*!
\class SbProjector
\ingroup Projectors
SbProjector is the base class for all projector classes.
Projector classes
are used to convert from window space (usually based on the mouse location)
into a 3D point. This is done by projecting the window coordinate as a
3D vector onto a geometric function in 3-space, and computing the
intersection point. Most projectors actually compute incremental
changes and produce incremental rotations and translation as needed.
Projectors are used to write 3D interactive manipulators and viewers.

\par See Also
\par
SbCylinderProjector, SbCylinderPlaneProjector, SbCylinderSectionProjector, SbCylinderSheetProjector, SbLineProjector, SbPlaneProjector, SbSpherePlaneProjector, SbSphereProjector, SbSphereSectionProjector, SbSphereSheetProjector
*/
////////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public abstract class SbProjector implements SbBasic, Mutable {
	
    protected final SbViewVolume            viewVol = new SbViewVolume();
    protected final SbMatrix                worldToWorking = new SbMatrix();
    protected final SbMatrix                workingToWorld = new SbMatrix();
 	

////////////////////////////////////////////////////////////////////////
//
//Description:
//Constructors
//
//Use: protected
//
////////////////////////////////////////////////////////////////////////

public SbProjector()
{
	setWorkingSpace(SbMatrix.identity());
}

    
  public

    //! Apply the projector using the given point, returning the
    //! point in three dimensions that it projects to.
    //! The point should be normalized (lie in the range [0.0,1.0]), with (0,0) at
    //! the lower-left.
    abstract SbVec3f         project(final SbVec2f point);

 	
    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Set the view volume.
     //
     // Use: virtual public
    public void
     setViewVolume(final SbViewVolume vol)
     //
     ////////////////////////////////////////////////////////////////////////
     {
         viewVol.copyFrom(vol);
     }
     

    //! Get the view volume to use for the projection.
    public SbViewVolume getViewVolume() { return viewVol; }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the working space.
//
// Use: virtual public
public void setWorkingSpace(final SbMatrix space)
//
////////////////////////////////////////////////////////////////////////
{
    workingToWorld.copyFrom(space);
    worldToWorking.copyFrom(workingToWorld.inverse());
}

    
    //! Given this mouse point, return the line it projects to
      //! in working space.
 	protected SbLine getWorkingLine(final SbVec2f point) {
		
 	     // First project the line to the world space line, then tranform
 		       // that line into working space.
 		   
 		       final SbLine worldLine = new SbLine();
 		       viewVol.projectPointToLine(point, worldLine);
 		   
 		       final SbLine workingLine = new SbLine();
 		       worldToWorking.multLineMatrix(worldLine, workingLine);
 		   
 		       return new SbLine(workingLine);
 		  	}
 	
    //! Creates and returns an exact copy of the projector.
    public abstract SbProjector     copy();

 	@Override
 	public void copyFrom(Object other) {
 		SbProjector otherProjector = (SbProjector)other;

 	    viewVol.copyFrom(otherProjector.viewVol);
 	    worldToWorking.copyFrom(otherProjector.worldToWorking);
 	    workingToWorld.copyFrom(otherProjector.workingToWorld);
 	 	
 	}


	public void destructor() {
		// TODO Auto-generated method stub
		
	} 	
}
