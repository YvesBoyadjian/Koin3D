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
 |      This file contains definitions of SbBoxes, 2D/3D boxes. A
 |      box has planes parallel to the major axes and can therefore
 |      be specified by two points on a diagonal.  The points with minimum
 |      and maximum x, y, and z coordinates are used.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, David Mott
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! 2D box class.
/*!
\class SbBox2s
\ingroup Basics
2D box which has planes parallel to the major axes and is specified by two
points (specified with short integers) on a diagonal. This class is part of
the standard Inventor datatype classes and is used as input and output to
geometry operations.

\par See Also
\par
SbBox3f, SbXfBox3f, SbBox2f, SbVec3f, SbVec2f, SbVec2s, SbMatrix
*/
////////////////////////////////////////////////////////////////////////////////

public class SbBox2s {

	  private final
	    //! Minimum and maximum points
	    SbVec2s     min = new SbVec2s(), max = new SbVec2s();
	  
	  public SbBox2s() {
		  makeEmpty(); 
	  }
	  
	    //! Constructor for a 2D integer box.  \p xmin, \p ymin, \p xmax,
	    //! and \p ymax are the bounds of the box.
	    public SbBox2s(short xmin, short ymin, short xmax, short ymax)
	        { min.setValue(xmin, ymin); max.setValue(xmax, ymax); }


//////////////////////////////////////////////////////////////////////////////
//
// Sets Box2s to contain nothing
//
public void
makeEmpty()
//
//////////////////////////////////////////////////////////////////////////////
{
    min.setValue(Short.MAX_VALUE, Short.MAX_VALUE);
    max.setValue(Short.MIN_VALUE, Short.MIN_VALUE);
}

	  
	  
	    //! Returns the minimum and maximum points of the box.
	    public SbVec2s      getMin()                   { return min; }
	    //! Returns the minimum and maximum points of the box.
	    public SbVec2s      getMax()                   { return max; }

	  
}
