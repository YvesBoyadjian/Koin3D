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
 |      SoSFRotation
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbRotationd;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;


////////////////////////////////////////////////////////////////////////////////
//! Field containing a rotation.
/*!
\class SoSFRotation
\ingroup Fields
A field containing a single SbRotation (an arbitrary rotation).


SoSFRotations are written to file as four floating point values
separated by whitespace.  The 4 values represent an axis of rotation
followed by the amount of right-handed rotation about that axis, in
radians.  For example, a 180 degree rotation about the Y axis is:
\code
0 1 0  3.14159265
\endcode

\par See Also
\par
SbRotation, SoField, SoSField, SoMFRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFRotation extends SoSField<SbRotation> {
	
	// Set the field to the rotation given by axis/angle. 
	public void setValue(final SbVec3f axis, float angle) {
	     setValue(new SbRotation(axis, angle));
	}
	
	// Set the field to the given quaternion. 
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Sets value from 4 separate floats describing a quaternion.
	    //    (Convenience function)
	    //
	    // Use: public
	    
	   public void
	    setValue(float q0, float q1, float q2, float q3)  // The 4 floats
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        setValue(new SbRotation(q0, q1, q2, q3));
	    }
	    
		@Override
		protected SbRotation constructor() {		
			return new SbRotation();
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
    final SbVec3f     axis = new SbVec3f();
    final float[]       angle = new float[1];

    if (! (in.read(axis.getRef()[0]) &&
           in.read(axis.getRef()[1]) &&
           in.read(axis.getRef()[2]) &&
           in.read(angle)))
        return false;

    setValue(axis, angle[0]);

    return true;
}

/**
 * java port
 * @param rotationd
 */
public void setValue(SbRotationd rotationd) {
	setValue(new SbRotation(rotationd));
}

/*!
  Return value of rotation as an \a axis and an \a angle around this
  \a axis.
*/
public float getValue(final SbVec3f axis)
{
	final float[] angle = new float[1];
  this.getValue().getValue(axis, angle);
  return angle[0]; // java port
}

	   }
