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
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Classes:
 |      SoMFRotation
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;


////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of SbRotations.
/*!
\class SoMFRotation
\ingroup Fields
multiple-value field that contains any number of SbRotations.


SoMFRotations are written to file as one or more sets of four
floating point values.  Each set of 4 values is an axis of rotation
followed by the amount of right-handed rotation about that axis, in
radians.


When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ 1 0 0 0, -.707 -.707 0 1.57 ]
\endcode

\par See Also
\par
SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFRotation extends SoMField<SbRotation> {

	@Override
	protected SbRotation constructor() {
		return new SbRotation();
	}

	@Override
	protected SbRotation[] arrayConstructor(int length) {
		return new SbRotation[length];
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads one (indexed) value from file. Returns FALSE on error.
//
// Use: private

public boolean read1Value(SoInput in, int index)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     axis = new SbVec3f();
    final float[]       angle = new float[1];

    if (! (in.read(axis.getRef()[0]) &&
           in.read(axis.getRef()[1]) &&
           in.read(axis.getRef()[2]) &&
           in.read(angle)))
        return false;

    set1Value(index, axis, angle[0]);

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets one rotation value from an axis and angle. (Convenience function)
//
// Use: public

public void set1Value(int index, final SbVec3f axis, float angle)
//
////////////////////////////////////////////////////////////////////////
{
    set1Value(index, new SbRotation(axis, angle));
}

}
