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


////////////////////////////////////////////////////////////////////////////////
//! 3D box with an associated transformation matrix.
/*!
\class SbXfBox3f
\ingroup Basics
A 3D box with an arbitrary transformation applied.  This class is useful when
a box will be transformed frequently; if an SbBox3f is used for this
purpose it will expand each time it is transformed in order to keep itself
axis-aligned.  Transformations can be accumulated on an SbXfBox3f
without expanding the box, and after all transformations have been done, the
box can be expanded to an axis-aligned box if necessary.

\par See Also
\par
SbBox3f, SbBox2f, SbBox2s, SbVec3f, SbVec2f, SbVec2s, SbMatrix, SoGetBoundingBoxAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbXfBox3f extends SbBox3f {
	
//
// Default constructor - leaves box totally empty
//
public SbXfBox3f()
{
    xform.makeIdentity();
    xformInv.makeIdentity();
    makeEmpty();
}
	
	
//
// Constructor given minimum and maximum points 
//
public SbXfBox3f( final SbVec3f _min, final SbVec3f _max)
{
    xform.makeIdentity();
    xformInv.makeIdentity();
    setBounds(_min, _max);
}

//
// Constructor given Box3f
//
public SbXfBox3f( SbBox3f box)
{
    xform.makeIdentity();
    xformInv.makeIdentity();
    super.copyFrom(box);
}

//
//Constructor given Box3f
//
public SbXfBox3f( SbXfBox3f box)
{
 copyFrom(box);
}

//
//Constructor given minimum and maximum points 
//
public void constructor( final SbVec3f _min, final SbVec3f _max)
{
 xform.makeIdentity();
 xformInv.makeIdentity();
 setBounds(_min, _max);
}

// java port
public void copyFrom(Object otherObject) {
	SbXfBox3f other = (SbXfBox3f)otherObject;
    super.copyFrom(other);
    xform.copyFrom(other.xform);
    xformInv.copyFrom(other.xformInv);
}
	
	
	   public SbVec3f getMin() { return super.getMin(); }
	public SbVec3f getMax() { return super.getMax(); }
		    	
    private final SbMatrix xform = new SbMatrix();
    private final SbMatrix xformInv = new SbMatrix();

    
	public static final float PRECISION_LIMIT = (1.0e-13f);

//
// Set the transformation on the box.  This is careful about
// non-invertable transformations.
//
public void
setTransform(final SbMatrix m)
{
    xform.copyFrom(m); 
    
    // Check for degenerate matrix:
    float det = m.det4();
    if (det < PRECISION_LIMIT && det > -PRECISION_LIMIT) {
        // We'll mark inverse[0][0] with FLT_MAX (max floating point
        // value) as special value to indicate degenerate transform.
        xformInv.copyFrom(new SbMatrix(Float.MAX_VALUE,0,0,0,
                            0,0,0,0,   0,0,0,0,  0,0,0,0));
    } else {
        xformInv.copyFrom( m.inverse());
    }
}

//#undef PRECISION_LIMIT

    //! Gets the transformation on the box, and its inverse.
    public SbMatrix     getTransform()         { return xform; }
    //! Gets the transformation on the box, and its inverse.
    public SbMatrix     getInverse()           { return xformInv; }



//
// Return the center of a box
//
public SbVec3fSingle
getCenter() 
{
    final SbVec3fSingle     p = new SbVec3fSingle();

    // transform the center before returning it
    xform.multVecMatrix((getMin().operator_add(getMax())).operator_mul(.5f), p);

    return p;
}

//
//Return the center of a box
//
public void
getCenter(SbVec3fSingle p) 
{

 // transform the center before returning it
 xform.multVecMatrix((getMin().operator_add(getMax())).operator_mul(.5f), p);
}

//
// Extend (if necessary) to contain given 3D point
//
public void
extendBy(final SbVec3f pt)
{
    // If our transform is degenerate, project this box, which will
    // transform min/max and get a box with identity xforms:
    if (xformInv.getValue()[0][0] == Float.MAX_VALUE) {
        this.copyFrom(new SbXfBox3f(this.project()));
    }
    
    final SbVec3f p = new SbVec3f();
    xformInv.multVecMatrix(pt, p);
    super.extendBy(p);
}

//
// Finds the volume of the box (0 for an empty box)
//

public float
getVolume() 
{
    if (isEmpty())
        return 0.0f;

    // The volume of a transformed box is just its untransformed
    // volume times the determinant of the upper-left 3x3 of
    // the xform matrix. Quoth Paul Strauss: "Pretty cool, indeed."
    float objVol = super.getVolume();
    float factor = xform.det3();
    return factor * objVol;
}

//
// Extends XfBox3f (if necessary) to contain given XfBox3f
//

public void
extendBy(final SbXfBox3f bb)
{
    if (bb.isEmpty())                   // bb is empty, no change
        return;
    else if (isEmpty())                 // we're empty, use bb
        this.copyFrom(bb);

    else if (xformInv.getValue()[0][0] != Float.MAX_VALUE && bb.xformInv.getValue()[0][0] != Float.MAX_VALUE) {
        // Neither box is empty and they are in different spaces. To
        // get the best results, we'll perform the merge of the two
        // boxes in each of the two spaces. Whichever merge ends up
        // being smaller is the one we'll use.
        // Note that we don't perform a project() as part of the test.
        // This is because projecting almost always adds a little extra
        // space. It also gives an unfair advantage to the
        // box more closely aligned with world space.  In the simplest
        // case this might be preferable. However, over many objects,
        // we are better off going with the minimum in local space,
        // and not worrying about projecting until the very end.

        final SbXfBox3f       xfbox1 = new SbXfBox3f(), xfbox2 = new SbXfBox3f();
        final SbBox3f         box1 = new SbBox3f(), box2 = new SbBox3f();

        // Convert bb into this's space to get box1
        xfbox1.copyFrom(bb);
        // Rather than calling transform(), which calls inverse(),
        // we'll do it ourselves, since we already know the inverse matrix.
        // I.e., we could call: xfbox1.transform(xformInv);
        xfbox1.xform.operator_mul_equal(xformInv);
        xfbox1.xformInv.multRight(xform);
        box1.copyFrom(xfbox1.project());

        // Convert this into bb's space to get box2
        xfbox2.copyFrom(this);
        // Same here for: xfbox2.transform(bb.xformInv);
        xfbox2.xform.operator_mul_equal(bb.xformInv);
        xfbox2.xformInv.multRight(bb.xform);
        box2.copyFrom(xfbox2.project());

        // Extend this by box1 to get xfbox1
        xfbox1.copyFrom(this);
        xfbox1.SbBox3f_extendBy(box1);
        // Use SbBox3f method; box1 is already in xfbox1's space
        // (otherwise, we'll get an infinite loop!)

        // Extend bb by box2 to get xfbox2
        xfbox2.copyFrom(bb);
        xfbox2.SbBox3f_extendBy(box2);
        // Use SbBox3f method; box2 is already in xfbox2's space
        // (otherwise, we'll get an infinite loop!)

        float vol1 = xfbox1.getVolume();
        float vol2 = xfbox2.getVolume();

        // Take the smaller result and extend appropriately
        if (vol1 <= vol2) {
            super.extendBy(box1);
        }
        else {
            this.copyFrom(bb);
            super.extendBy(box2);
        }
    }
    else if (xformInv.getValue()[0][0] == Float.MAX_VALUE) {
        if (bb.xformInv.getValue()[0][0] == Float.MAX_VALUE) {
            // Both boxes are degenerate; project them both and
            // combine them:
            final SbBox3f box = new SbBox3f(this.project());
            box.extendBy(bb.project());
            this.copyFrom( new SbXfBox3f(box));
        } else {
            // this is degenerate; transform our min/max into bb's
            // space, and combine there:
            final SbBox3f box = new SbBox3f(getMin(), getMax());
            box.transform(xform.operator_mul(bb.xformInv));
            this.copyFrom( bb);
            super.extendBy(box);
        }
    } else {
        // bb is degenerate; transform it into our space and combine:
        final SbBox3f box = new SbBox3f(bb.getMin(), bb.getMax());
        box.transform(bb.xform.operator_mul(xformInv));
        super.extendBy(box);
    }
}

//
// Returns TRUE if intersection of given point and Box3f is not empty
// (being careful about degenerate transformations...).
//
public boolean
intersect(final SbVec3f pt)
{
    if (xformInv.getValue()[0][0] != Float.MAX_VALUE) {
        final SbVec3f p = new SbVec3f();
        xformInv.multVecMatrix(pt, p);
        return super.intersect(p);
    }
    final SbBox3f box = new SbBox3f(this.project());  // Degenerate; project and test:
    return box.intersect(pt);
}

//
// Transform this box by a matrix
//
public void
transform(final SbMatrix m) 
{
    final SbMatrix new_xf = xform.operator_mul(m);
    setTransform(new_xf);
}
    
    private final SbBox3f     box = new SbBox3f(); // SINGLE_THREAD
    
	// Projects an SbXfBox3f to an SbBox3f. 
	public SbBox3f project() {
		
	     box.constructor(getMin(), getMax());
	          box.transform(xform);
	          return box;
	     	}
	
	public boolean operator_equal_equal(final SbXfBox3f b2) {
		final SbXfBox3f b1 = this;
	    final SbBox3f b1Proj = b1.project();
	    final SbBox3f b2Proj = b2.project();
	    return (b1Proj.operator_equal_equal(b2Proj));
	}
}
