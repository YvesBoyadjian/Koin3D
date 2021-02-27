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
 |      SoSFMatrix
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoInput;


////////////////////////////////////////////////////////////////////////////////
//! Field containing a 4x4 matrix.
/*!
\class SoSFMatrix
\ingroup Fields
A field containing a transformation matrix (an SbMatrix).


SoSFMatrices are written to file as 16 floating point
numbers separated by whitespace.  For example, an identity matrix is
written as:
\code
1 0 0 0  0 1 0 0  0 0 1 0  0 0 0 1
\endcode

\par See Also
\par
SoField, SoSField, SoMFMatrix, SbMatrix
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFMatrix extends SoSField<SbMatrix> {

	@Override
	protected SbMatrix constructor() {		
		return new SbMatrix();
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
	DoubleConsumer[][] ref = value.getRef();
    return  in.read(ref[0][0]) && in.read(ref[0][1])
         && in.read(ref[0][2]) && in.read(ref[0][3])
         && in.read(ref[1][0]) && in.read(ref[1][1])
         && in.read(ref[1][2]) && in.read(ref[1][3])
         && in.read(ref[2][0]) && in.read(ref[2][1])
         && in.read(ref[2][2]) && in.read(ref[2][3])
         && in.read(ref[3][0]) && in.read(ref[3][1])
         && in.read(ref[3][2]) && in.read(ref[3][3]);
}

/**
 * java port
 * @param i
 * @param j
 * @param k
 * @param l
 * @param m
 * @param n
 * @param o
 * @param p
 * @param q
 * @param r
 * @param s
 * @param t
 * @param u
 * @param v
 * @param w
 * @param x
 */
public void setValue(int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s, int t, int u, int v,
		int w, int x) {
	SbMatrix matrix = new SbMatrix( i,  j,  k,  l,  m,  n,  o,  p,  q,  r,  s,  t,  u,  v,
			 w,  x);
	setValue(matrix);
}

	
}
