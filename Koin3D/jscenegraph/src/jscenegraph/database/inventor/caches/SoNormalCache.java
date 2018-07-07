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
 |   Description:
 |      This file defines the SoNormalCache class, which is used for
 |      storing generated normals.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.caches;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Destroyable;


/////////////////////////////////////////////////////////////////////////
///
///  Class SoNormalCache:
///
///  A normal cache stores a list of normals.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNormalCache extends SoCache implements Destroyable {

  private
    int                 numNormals;             //!< Number of normals
    private SbVec3f[]       normals;               //!< Array of normals


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNormalCache(SoState state) { super(state);
//
////////////////////////////////////////////////////////////////////////

    numNormals = 0;
    normals = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (normals != null) {
        normals = null;
        }
}

    //! Returns the number of normals and list of normals
public     int                 getNum()           { return numNormals; }
public  SbVec3f[]      getNormals()       { return normals;    }

// java port
public float[] getNormalsFloat() {
	float[] normalArray = new float[numNormals*3];
	int index=0;
	for(int i=0;i<numNormals;i++) {
		normalArray[index] = normals[i].getValueRead()[0];
		index++;
		normalArray[index] = normals[i].getValueRead()[1];
		index++;
		normalArray[index] = normals[i].getValueRead()[2];
		index++;
	}
	return normalArray;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Stores a list of normals in the cache.
//
// Use: public

public void
set(int _numNormals, final SbVec3f[] _normals)
//
////////////////////////////////////////////////////////////////////////
{
    numNormals = _numNormals;
    normals    = _normals;
}
}
