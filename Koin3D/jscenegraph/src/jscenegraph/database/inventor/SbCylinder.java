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
 |      SbCylinder
 |
 |   Author(s)          : Howard Look
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbCylinder implements Mutable {

  
	private  final SbLine      axis = new SbLine();
	private float       radius;

    //! Return the axis and radius.
    public SbLine       getAxis()          { return axis; }
    //! Return the axis and radius.
    public float        getRadius()        { return radius; }


//////////////////////////////////////////////////////////////////////////////
//
// Cylinder class
//
//////////////////////////////////////////////////////////////////////////////

// construct a default cylinder
public SbCylinder()
{
    axis.setValue(new SbVec3f(0.0f, 0.0f, 0.0f), new SbVec3f(0.0f, 1.0f, 0.0f));
    radius = 1.0f;
}

// construct a cylinder given an axis and radius
public SbCylinder(final SbLine a, float r)
{
    axis.copyFrom(a);
    radius = r;
}

// Change the axis and radius
public void
setValue(final SbLine a, float r)
{
    axis.copyFrom(a);
    radius = r;
}

//////////////////////////////////////////////////////////////////////////////
//
// Change just the axis
//

public void
setAxis(final SbLine a)
//
//////////////////////////////////////////////////////////////////////////////
{
    axis.copyFrom(a);
}

//////////////////////////////////////////////////////////////////////////////
//
// Change just the radius
//

public void
setRadius(float r)
//
//////////////////////////////////////////////////////////////////////////////
{
    radius = r;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Intersect given line with this cylinder, returning the
//  first intersection in result. Returns TRUE if there was an
//  intersection (and result is valid).
//
// Use: public

public boolean
intersect(final SbLine line, final SbVec3f result)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f whoCares = new SbVec3f();
    return intersect(line, result, whoCares);
}    

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Intersect given line with this cylinder, returning the
//  results in enter and exit. Returns TRUE if there was an
//  intersection (and results are valid).
//
// Use: public

public boolean
intersect(final SbLine line, final SbVec3f enter, final SbVec3f exit) 
//
////////////////////////////////////////////////////////////////////////
{
    // The intersection will actually be done on a radius 1 cylinder
    // aligned with the y axis, so we transform the line into that
    // space, then intersect, then transform the results back.

    // rotation to y axis
    final SbRotation  rotToYAxis = new SbRotation(axis.getDirection(), new SbVec3f(0,1,0));
    final SbMatrix    mtxToYAxis = new SbMatrix();
    mtxToYAxis.setRotate(rotToYAxis);

    // scale to unit space
    float       scaleFactor = 1.0f/radius;
    final SbMatrix    toUnitCylSpace = new SbMatrix();
    toUnitCylSpace.setScale(new SbVec3f(scaleFactor, scaleFactor, scaleFactor));
    toUnitCylSpace.multLeft(mtxToYAxis);

    // find the given line un-translated
    final SbVec3f origin = new SbVec3f(line.getPosition());
    origin.operator_minus_equal(axis.getPosition());
    final SbLine noTranslationLine = new SbLine(origin, origin.operator_add(line.getDirection()));

    // find the un-translated line in unit cylinder's space
    final SbLine cylLine = new SbLine();
    toUnitCylSpace.multLineMatrix(noTranslationLine, cylLine);

    // find the intersection on the unit cylinder
    final SbVec3f cylEnter = new SbVec3f(), cylExit = new SbVec3f();
    boolean intersected = unitCylinderIntersect(cylLine, cylEnter, cylExit);

    if (intersected) {
        // transform back to original space
        final SbMatrix fromUnitCylSpace = new SbMatrix(toUnitCylSpace.inverse());

        fromUnitCylSpace.multVecMatrix(cylEnter, enter);
        enter.operator_add_equal(axis.getPosition());

        fromUnitCylSpace.multVecMatrix(cylExit, exit);
        exit.operator_add_equal(axis.getPosition());
    }    

    return intersected;
}    

////////////////////////////////////////////////////////////////////////
//
// Description:
//  Intersect the line with a unit cylinder. Returns TRUE if
//  there was an intersection, and returns the intersection points
//  in enter and exit.
//
//  The cylinder has radius 1 and is aligned with the
//  y axis, such that x^2 + z^2 - 1 = 0
//
//  Taken from Pat Hanrahan's chapter in Glassner's
//  _Intro to Ray Tracing_, page 91, and some code
//  stolen from Paul Strauss.
//
// Use: private, static

private static boolean
unitCylinderIntersect(final SbLine l,
                                  final SbVec3f enter, final SbVec3f exit)
//
////////////////////////////////////////////////////////////////////////
{
    float               A, B, C, discr, sqroot, t0, t1;
    final SbVec3f       pos = l.getPosition(), dir = l.getDirection();
    boolean                doesIntersect = true;

    A = dir.getValue()[0] * dir.getValue()[0] + dir.getValue()[2] * dir.getValue()[2];

    B = 2.0f * (pos.getValue()[0] * dir.getValue()[0] + pos.getValue()[2] * dir.getValue()[2]);

    C = pos.getValue()[0] * pos.getValue()[0] + pos.getValue()[2] * pos.getValue()[2] - 1;

    // discriminant = B^2 - 4AC
    discr = B*B - 4.0f*A*C;

    // if discriminant is negative, no intersection
    if (discr < 0.0) {
        doesIntersect = false;
    }
    else {
        sqroot = (float)(Math.sqrt(discr));

        // magic to stabilize the answer
        if (B > 0.0) {
            t0 = -(2.0f * C) / (sqroot + B);
            t1 = -(sqroot + B) / (2.0f * A);
        }
        else {
            t0 = (2.0f * C) / (sqroot - B);
            t1 = (sqroot - B) / (2.0f * A);
        }           

        enter.copyFrom(pos.operator_add(dir.operator_mul(t0)));
        exit.copyFrom(pos.operator_add(dir.operator_mul(t1)));
    }

    return doesIntersect;
}
	
	/* (non-Javadoc)
	 * @see jscenegraph.port.Mutable#copyFrom(java.lang.Object)
	 */
	@Override
	public void copyFrom(Object other) {
		SbCylinder otherCylinder = (SbCylinder)other;
		axis.copyFrom(otherCylinder.axis);
		radius = otherCylinder.radius;
	}

}
