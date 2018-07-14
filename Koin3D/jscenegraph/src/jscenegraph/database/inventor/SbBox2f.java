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

import jscenegraph.port.Mutable;

////////////////////////////////////////////////////////////////////////////////
//! 2D box class.
/*!
\class SbBox2f
\ingroup Basics
2D box which has planes parallel to the major axes and is specified by
two points (specified as floating point) on a diagonal. This class is part
of the standard Inventor datatype classes and is used as input and output
to geometry operations.

\par See Also
\par
SbBox3f, SbXfBox3f, SbBox2s, SbVec3f, SbVec2f, SbVec2s, SbMatrix
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbBox2f implements Mutable {
	
	   private
		   final      SbVec2fSingle     min = new SbVec2fSingle(), max = new SbVec2fSingle();

    //! Constructs an empty box.
    public SbBox2f()                                   { makeEmpty(); };

    //! Constructor given bounds 
    public SbBox2f(float xmin, float ymin, float xmax, float ymax)
        { min.setValue(xmin, ymin); max.setValue(xmax, ymax); }

    //! Constructor given minimum and maximum points 
    public SbBox2f(final SbVec2f _min, final SbVec2f _max)
        { min.copyFrom(_min); max.copyFrom(_max); }

    //! Returns the minimum and maximum points of the box.
    public SbVec2f      getMin()                   { return min; }
    //! Returns the minimum and maximum points of the box.
    public SbVec2f      getMax()                   { return max; }

	   
	   
	// Extends Box2f (if necessary) to contain given 2D point. 
	public void extendBy(final SbVec2f pt) {
		
		float[] pt_ = pt.getValueRead(); // java port
		float[] min_ = min.getValue();
		float[] max_ = max.getValue();
		
	     if (pt_[0] < min_[0]) min_[0] = pt_[0];
	          if (pt_[0] > max_[0]) max_[0] = pt_[0];
	      
	          if (pt_[1] < min_[1]) min_[1] = pt_[1];
	          if (pt_[1] > max_[1]) max_[1] = pt_[1];
	      	}
	
	// Gets box size. (java port)
	public void getSize(float[] sizeXY) {
		 sizeXY[0] = max.getValue()[0] - min.getValue()[0]; sizeXY[1] = max.getValue()[1] - min.getValue()[1]; 
	}


//////////////////////////////////////////////////////////////////////////////
//
// Sets Box2f to contain nothing
//
public void
makeEmpty()
//
//////////////////////////////////////////////////////////////////////////////
{
    min.setValue( Float.MAX_VALUE,  Float.MAX_VALUE);
    max.setValue(-Float.MAX_VALUE, -Float.MAX_VALUE);
}

    //! Returns <tt>TRUE</tt> if the box is empty, and <tt>FALSE</tt> otherwise.
    public boolean        isEmpty()          { return max.getValue()[0] < min.getValue()[0]; }

    public void        getBounds(final SbVec2f _min, final SbVec2f _max)
        { _min.copyFrom(min); _max.copyFrom(max); }

	@Override
	public void copyFrom(Object other) {
		SbBox2f otherB = (SbBox2f)other;
		min.copyFrom(otherB.min);
		max.copyFrom(otherB.max);
	}


}
