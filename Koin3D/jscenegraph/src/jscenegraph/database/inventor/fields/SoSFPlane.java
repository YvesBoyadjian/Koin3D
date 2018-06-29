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
 |   Classes:
 |      SoSFPlane
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Field containing a plane equation.
/*!
\class SoSFPlane
\ingroup Fields
A field containing a plane equation (an SbPlane).


SoSFPlanes are written to file as four floating point values
separated by whitespace.  The first three are the normal direction of
the plane, the fourth is the distance of the plane from the origin
(in the direction of the normal).

\par See Also
\par
SbPlane, SoField, SoSField, SoMFPlane
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSFPlane extends SoSField<SbPlane> {

	@Override
	protected SbPlane constructor() {
		return new SbPlane();
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error.
//
// Use: private

public boolean readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     normal = new SbVec3f();
    final float[]       distance = new float[1];

    if (! (in.read(normal.getRef()[0]) &&
           in.read(normal.getRef()[1]) &&
           in.read(normal.getRef()[2]) &&
           in.read(distance)))
        return false;

    setValue(new SbPlane(normal, distance[0]));

    return true;
}

}
