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
 |   $Revision: 1.3 $
 |
 |   Description:
 |      This file contains definitions of various linear algebra classes,
 |      such as vectors, coordinates, etc..
 |
 |   Classes:
 |      SbVec3f
 |      SbVec2f
 |      SbVec2s
 |      SbVec3s         //!< Extension to SGI OIV 2.1
 |      SbVec4f
 |      SbRotation
 |      SbMatrix
 |      SbViewVolume
 |
 |      SbLine
 |      SbPlane
 |      SbSphere
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, 
 |                        David Mott, Alain Dumesny
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Class for representing a sphere.
/*!
\class SbSphere
\ingroup Basics
Represents a sphere in 3D. This is a lightweight datatype that
is used for arguments or return values in the Inventor toolkit. See
SoSphere for a database sphere (used for rendering, picking, etc.).
{}

\par See Also
\par
SbVec3f, SbLine, SoSphere
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbSphere implements Mutable {

	private final SbVec3f     center = new SbVec3f();
	private float       radius;
		   
    //! Constructors.
	public SbSphere()  {}
	
	 // construct a sphere given a center and radius
	   public SbSphere(final SbVec3f c, float r)
	    {
	        center.copyFrom(c);
	        radius = r;
	    }
	    	
	@Override
	public void copyFrom(Object other) {
		SbSphere otherSphere = (SbSphere)other;
		center.copyFrom(otherSphere.center);
		radius = otherSphere.radius;
	}
	
	 
	    // Change the center and radius
	   public void
	    setValue(final SbVec3f c, float r)
	    {
	        center.copyFrom( c);
	        radius = r;
	    }
	    	
	// Return the center and radius. 
	public SbVec3f getCenter() {
		 return center; 
	}
	
	// Return the center and radius. 
	public float getRadius() {
		 return radius; 
	}

	 //////////////////////////////////////////////////////////////////////////////
	   //
	   // Return a sphere containing a given box
	   //
	  
	  public void
	   circumscribe( SbBox3f box)
	   //
	   //////////////////////////////////////////////////////////////////////////////
	   {
	       center.copyFrom((box.getMin().operator_add(box.getMax())).operator_mul(0.5f));
	       radius = (box.getMax().operator_minus(center)).length();
	   }
	   	

//////////////////////////////////////////////////////////////////////////////
//
// Sphere line intersection - this sets the parameter intersection,
// and returns TRUE if the line and sphere really do intersect.
//
// line-sphere intersection algorithm lifted from Eric Haines chapter in 
// Glassner's "Introduction to Ray Tracing", pp. 35-7
//
public boolean intersect(final SbLine l, final SbVec3f intersection) 
//
//////////////////////////////////////////////////////////////////////////////
{
    float   B,C;        // At^2 + Bt + C = 0, but A is 1 since we normalize Rd
    float   discr;      // discriminant (B^2 - 4AC)
    final SbVec3f v = new SbVec3f();
    float   t,sqroot;
    boolean    doesIntersect = true;

    // setup B,C
    v.copyFrom( l.getPosition().operator_minus( center));
    B = 2.0f * (l.getDirection().dot(v));
    C = v.dot(v) - (radius * radius);

    // compute discriminant
    // if negative, there is no intersection
    discr = B*B - 4.0f*C;
    if (discr < 0.0) {
        // line and sphere do not intersect
        doesIntersect = false;
    }
    else {
        // compute t0: (-B - sqrt(B^2 - 4AC)) / 2A  (A = 1)
        sqroot = (float)Math.sqrt(discr);
        t = (-B - sqroot) * 0.5f;
        if (t < 0.0) {
            // no intersection, try t1: (-B + sqrt(B^2 - 4AC)) / 2A  (A = 1)
            t = (-B + sqroot) * 0.5f;
        }

        if (t < 0.0) {
            // line and sphere do not intersect
            doesIntersect = false;
        }
        else {
            // intersection! point is (point + (dir * t))
            intersection.copyFrom( l.getPosition().operator_add((l.getDirection().operator_mul(t))));
        }
    }

    return doesIntersect;
}

	  
	  
    //////////////////////////////////////////////////////////////////////////////
    //
    // Sphere line intersection - this sets the parameter intersection,
    // and returns TRUE if the line and sphere really do intersect.
    //
    // line-sphere intersection algorithm lifted from Eric Haines chapter in 
    // Glassner's "Introduction to Ray Tracing", pp. 35-7
    //
   public boolean
    intersect(final SbLine l, final SbVec3f enter, final SbVec3f exit)
    //
    //////////////////////////////////////////////////////////////////////////////
    {
        float   B,C;        // At^2 + Bt + C = 0, but A is 1 since we normalize Rd
        float   discr;      // discriminant (B^2 - 4AC)
        final SbVec3f v = new SbVec3f();
        float   sqroot;
        boolean  doesIntersect = true;
    
        // setup B,C
        v.copyFrom(l.getPosition().operator_minus(center));
        B = 2.0f * (l.getDirection().dot(v));
        C = v.dot(v) - (radius * radius);
    
        // compute discriminant
        // if negative, there is no intersection
        discr = B*B - 4.0f*C;
    
        if (discr < 0.0) {
            // line and sphere do not intersect
            doesIntersect = false;
        }
        else {
            sqroot = (float)Math.sqrt(discr);
    
            float t0 = (-B - sqroot) * 0.5f;
            enter.copyFrom(l.getPosition().operator_add((l.getDirection().operator_mul(t0))));
    
            float t1 = (-B + sqroot) * 0.5f;
            exit.copyFrom(l.getPosition().operator_add((l.getDirection().operator_mul(t1))));
        }
    
        return doesIntersect;
   }
   
   /*!
   Set the sphere's radius.

   \sa setValue(), setCenter() and getRadius().
  */
public  void
 setRadius( float radiusarg)
 {
 //#if COIN_DEBUG
   if (radiusarg<0.0f)
     SoDebugError.postWarning("SbSphere::setRadius",
                               "Radius should be >= 0.0f.");
 //#endif // COIN_DEBUG
   this.radius = radiusarg;
 }

/*!
  Returns \a TRUE of the given point \a p lies within the sphere.
 */
public boolean
pointInside(final SbVec3f p)
{
  return (p.operator_minus(center)).length() < radius;
}

}
