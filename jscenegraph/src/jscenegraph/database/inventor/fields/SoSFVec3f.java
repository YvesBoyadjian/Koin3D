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
 |      SoSFVec3f
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;


////////////////////////////////////////////////////////////////////////////////
//! Field containing a three-dimensional vector.
/*!
\class SoSFVec3f
\ingroup Fields
Field containing a three-dimensional vector.


SoSFVec3fs are written to file as three floating
point values separated by whitespace.

\par See Also
\par
SoField, SoSField, SoMFVec3f
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFVec3f extends SoSField<SbVec3f> {
	
	// Sets the field to the given value. 
	 //
	   // Description:
	    //    Sets value from 3 separate floats. (Convenience function)
	    //
	    // Use: public
	    
	   public void setValue(float x, float y, float z) {
		     setValue(new SbVec3f(x, y, z));
		}

	   @Override
		protected SbVec3f constructor() {		
			return new SbVec3f();
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
    return (in.read(value.getRef()[0]) &&
            in.read(value.getRef()[1]) &&
            in.read(value.getRef()[2]));
}

public void setValue(float[] vector) { // java port
	setValue(new SbVec3f(vector));
}

/**
 * Java port
 * @param vector
 */
public void setValue(SbVec3d vector) {
	float[] valuef = new float[3];
	valuef[0] = (float)vector.getX();
	valuef[1] = (float)vector.getY();
	valuef[2] = (float)vector.getZ();
	setValue(valuef);	
}

	}
