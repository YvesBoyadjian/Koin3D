/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.database.inventor;

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Array;

/*!
  \class SbDPViewVolume SbLinear.h Inventor/SbLinear.h
  \brief The SbDPViewVolume class is a double precision viewing volume in 3D space.
  \ingroup base

  This class contains the necessary information for storing a view
  volume.  It has methods for projection of primitives from or into
  the 3D volume, doing camera transforms, view volume transforms etc.

  \COIN_CLASS_EXTENSION

  \sa SbViewportRegion
  \since Coin 2.0
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SbDPViewVolume {
	
	public
		  enum ProjectionType { ORTHOGRAPHIC, PERSPECTIVE };
	
		  

		  private			  ProjectionType type;
		  private			  final SbVec3d projPoint = new SbVec3d();
		  private			  final SbVec3d projDir = new SbVec3d();
		  private			  double nearDist;
		  private			  double nearToFar;
		  private			  final SbVec3d llf = new SbVec3d();
		  private			  final SbVec3d lrf = new SbVec3d();
		  private			  final SbVec3d ulf = new SbVec3d();


	/*!
	  Returns the six planes defining the view volume in the following
	  order: left, bottom, right, top, near, far. Plane normals are
	  directed into the view volume.

	  This method is an extension for Coin, and is not available in the
	  original Open Inventor.
	*/
	public void
	getViewVolumePlanes(final Array<SbPlane> planes) 
	{
	  final SbVec3d far_ll = new SbVec3d();
	  final SbVec3d far_lr = new SbVec3d();
	  final SbVec3d far_ul = new SbVec3d();
	  final SbVec3d far_ur = new SbVec3d();

	  this.getPlaneRectangle(this.nearToFar, far_ll, far_lr, far_ul, far_ur);
	  SbVec3d near_ur = this.ulf.operator_add (this.lrf.operator_minus(this.llf));

	  SbVec3f f_ulf = dp_to_sbvec3f(this.ulf.operator_add (this.projPoint));
	  SbVec3f f_llf = dp_to_sbvec3f(this.llf.operator_add (this.projPoint));
	  SbVec3f f_lrf = dp_to_sbvec3f(this.lrf.operator_add (this.projPoint));
	  SbVec3f f_near_ur = dp_to_sbvec3f(near_ur.operator_add (this.projPoint));
	  SbVec3f f_far_ll = dp_to_sbvec3f(far_ll.operator_add (this.projPoint));
	  SbVec3f f_far_lr = dp_to_sbvec3f(far_lr.operator_add (this.projPoint));
	  SbVec3f f_far_ul = dp_to_sbvec3f(far_ul.operator_add (this.projPoint));
	  SbVec3f f_far_ur = dp_to_sbvec3f(far_ur.operator_add (this.projPoint));
	  
	  planes.set(0, new SbPlane(f_ulf, f_llf, f_far_ll));  // left
	  planes.set(1, new SbPlane(f_llf, f_lrf, f_far_lr)); // bottom
	  planes.set(2, new SbPlane(f_lrf, f_near_ur, f_far_ur)); // right
	  planes.set(3, new SbPlane(f_near_ur, f_ulf, f_far_ul)); // top
	  planes.set(4, new SbPlane(f_ulf, f_near_ur, f_lrf)); // near
	  planes.set(5, new SbPlane(f_far_ll, f_far_lr, f_far_ur)); // far

	  // check for inverted view volume (negative aspectRatio)
	  if (!planes.get(0).isInHalfSpace(f_lrf)) {
	    final SbVec3f n = new SbVec3f();
	    float D;

	    n.copyFrom( planes.get(0).getNormal());
	    D = planes.get(0).getDistanceFromOrigin();    
	    planes.set(0, new SbPlane(n.operator_minus(), -D));

	    n.copyFrom( planes.get(2).getNormal());
	    D = planes.get(2).getDistanceFromOrigin();    
	    planes.set(2, new SbPlane(n.operator_minus(), -D));
	  }
	  if (!planes.get(1).isInHalfSpace(f_near_ur)) {
	    final SbVec3f n = new SbVec3f();
	    float D;

	    n.copyFrom( planes.get(1).getNormal());
	    D = planes.get(1).getDistanceFromOrigin();    
	    planes.set(1, new SbPlane(n.operator_minus(), -D));

	    n.copyFrom( planes.get(3).getNormal());
	    D = planes.get(3).getDistanceFromOrigin();    
	    planes.set(3, new SbPlane(n.operator_minus(), -D));
	    
	  }

	  if (!planes.get(4).isInHalfSpace(f_far_ll)) {
	    final SbVec3f n = new SbVec3f();
	    float D;

	    n.copyFrom( planes.get(4).getNormal());
	    D = planes.get(4).getDistanceFromOrigin();    
	    planes.set(4, new SbPlane(n.operator_minus(), -D));

	    n.copyFrom( planes.get(5).getNormal());
	    D = planes.get(5).getDistanceFromOrigin();    
	    planes.set(5, new SbPlane(n.operator_minus(), -D));
	    
	  }

	}

	//
	// Returns the four points defining the view volume rectangle at the
	// specified distance from the near plane, towards the far plane. The
	// points are returned in normalized view volume coordinates
	// (projPoint is not added).
	public void
	getPlaneRectangle(final double distance, final SbVec3d  lowerleft,
	                                  final SbVec3d  lowerright,
	                                  final SbVec3d  upperleft,
	                                  final SbVec3d  upperright)
	{
	  final SbVec3d near_ur = new SbVec3d(this.ulf.operator_add(this.lrf.operator_minus(this.llf)));

	//#if COIN_DEBUG
	  if (this.llf.operator_equal_equal( new SbVec3d(0.0, 0.0, 0.0)) ||
	      this.lrf.operator_equal_equal( new SbVec3d(0.0, 0.0, 0.0)) ||
	      this.ulf.operator_equal_equal( new SbVec3d(0.0, 0.0, 0.0)) ||
	      near_ur.operator_equal_equal( new SbVec3d(0.0, 0.0, 0.0))) {
	    SoDebugError.postWarning("SbDPViewVolume::getPlaneRectangle",
	                              "Invalid frustum.");
	    
	  }
	//#endif // COIN_DEBUG

	  if (this.type == ProjectionType.PERSPECTIVE) {
	    double depth = this.nearDist + distance;
	    final SbVec3d dir = new SbVec3d();
	    dir.copyFrom(this.llf);
	    dir.normalize(); // safe to normalize here
	    lowerleft.copyFrom( dir.operator_mul( depth).operator_div( dir.dot(this.projDir)));

	    dir.copyFrom( this.lrf);
	    dir.normalize(); // safe to normalize here
	    lowerright.copyFrom( dir.operator_mul( depth).operator_div( dir.dot(this.projDir)));

	    dir.copyFrom( this.ulf);
	    dir.normalize(); // safe to normalize here
	    upperleft.copyFrom( dir.operator_mul( depth).operator_div( dir.dot(this.projDir)));
	    
	    dir.copyFrom( near_ur);
	    dir.normalize(); // safe to normalize here
	    upperright.copyFrom( dir.operator_mul( depth).operator_div( dir.dot(this.projDir)));
	  }
	  else {
	    lowerleft.copyFrom( this.llf.operator_add( this.projDir.operator_mul( distance)));
	    lowerright.copyFrom( this.lrf.operator_add( this.projDir.operator_mul( distance)));
	    upperleft.copyFrom( this.ulf.operator_add( this.projDir.operator_mul( distance)));
	    upperright.copyFrom( near_ur.operator_add( this.projDir.operator_mul( distance)));
	  }
	}

	//
	// some convenience function for converting between single precision
	// and double precision classes.
	//
	public static SbVec3f 
	dp_to_sbvec3f(final SbVec3d v)
	{
	  return new SbVec3f((float)(v.vec[0]), (float)(v.vec[1]), (float)(v.vec[2]));
	}

}
