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

import static jscenegraph.database.inventor.SbBasic.M_PI_2;

import jscenegraph.coin3d.inventor.base.SbClip;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Array;
import jscenegraph.port.Mutable;

////////////////////////////////////////////////////////////////////////////////
//! 3D viewing volume class.
/*!
\class SbViewVolume
\ingroup Basics
Class used to represent a 3D viewing volume. This class is used
to represent viewing frusta and picking volumes.  For perspective projection,
the view volume is a frustum. For orthographic (parallel) projection, the
view volume is a rectangular prism.

\par See Also
\par
SbVec3f, SbVec2f, SbBox3f, SbMatrix, SbRotation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbViewVolume implements Mutable {

	// ! Return projection information
	public enum ProjectionType {
		ORTHOGRAPHIC, PERSPECTIVE
	};

	public ProjectionType type;

	// ! Note that there is redundant info in this data structure and its
	// ! elements should not be changed by hand.
	final SbVec3f projPoint = new SbVec3f(); // !< must be (0,0,0) for ortho
	final SbVec3f projDir = new SbVec3f();
	public float nearDist; // !< distance to near plane
	public float nearToFar; // !< distance between z clips
	public final SbVec3f llf = new SbVec3f();
	public final SbVec3f lrf = new SbVec3f();
	public final SbVec3f ulf = new SbVec3f();

	private final SbDPViewVolume dpvv = new SbDPViewVolume();

	// ! Points on the near clipping plane. Add in the projPoint to
	// ! figure out where they are in world space:
	private final SbVec3f llfO = new SbVec3f(); // !< x = -w, y = -w, z = -w
	private final SbVec3f lrfO = new SbVec3f(); // !< x = w, y = -w, z = -w
	private final SbVec3f ulfO = new SbVec3f(); // !< x = -w, y = w, z = -w

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Yes, this should be inlineable
	//
	// Use: public

	public SbViewVolume()
	//
	////////////////////////////////////////////////////////////////////////
	{
	}

	// java port
	public SbViewVolume(SbViewVolume other) {
		copyFrom(other);
	}

	/**
	 * Returns two matrices corresponding to the view volume. The first is a viewing
	 * matrix, which is guaranteed to be an affine transformation. The second is
	 * suitable for use as a projection matrix in OpenGL.
	 * 
	 * @param affine
	 * @param proj
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns two matrices corresponding to the view volume. The
	// first is a viewing matrix, which is guaranteed to be an affine
	// transformation. The second is suitable for use as a projection
	// matrix in GL. This part about finding the projection matrix
	// is taken from the old GT Graphics Library User's Guide, page B-3.
	//
	// Use: public
	public void getMatrices(final SbMatrix affine, final SbMatrix projM) {
		final SbMatrix skewMatM = new SbMatrix();
		final float[][] skewMat = skewMatM.getValue();

		final SbVec3f rightV = lrfO.operator_minus(llfO);
		final float[] right = rightV.getValueRead();
		final SbVec3f upV = ulfO.operator_minus(llfO);
		final float[] up = upV.getValueRead();

		float width = rightV.length();
		float height = upV.length();

		//
		// skewMat is the matrix that would take a nice orthogonal view volume
		// that is aligned with x,y and neg-z and skew it to this view volume,
		// based on llfO being at the origin.
		//
		skewMat[0][0] = right[0] / width;
		skewMat[0][1] = right[1] / width;
		skewMat[0][2] = right[2] / width;
		skewMat[0][3] = 0;

		skewMat[1][0] = up[0] / height;
		skewMat[1][1] = up[1] / height;
		skewMat[1][2] = up[2] / height;
		skewMat[1][3] = 0;

		skewMat[2][0] = -projDir.getValueRead()[0];
		skewMat[2][1] = -projDir.getValueRead()[1];
		skewMat[2][2] = -projDir.getValueRead()[2];
		skewMat[2][3] = 0;

		skewMat[3][0] = 0;
		skewMat[3][1] = 0;
		skewMat[3][2] = 0;
		skewMat[3][3] = 1;

		// Therefore, its inverse takes our probably rotated and potentially
		// skewed view volume and makes it orthogonal (unskewed) and aligned
		// with neg-z axis
		final SbMatrix skewMatInv = new SbMatrix(skewMatM.inverse());

		affine.setTranslate((llfO.operator_add(projPoint).operator_minus()));
		affine.multRight(skewMatInv);

		final SbVec3f eye = new SbVec3f();
		affine.multVecMatrix(projPoint, eye);

		final SbMatrix moveToEye = new SbMatrix();
		moveToEye.setTranslate(eye.operator_minus());
		affine.multRight(moveToEye);

		final SbVec3f llfEye = new SbVec3f(), lrfEye = new SbVec3f(), ulfEye = new SbVec3f();
		skewMatInv.multVecMatrix(llfO, llfEye);
		skewMatInv.multVecMatrix(lrfO, lrfEye);
		skewMatInv.multVecMatrix(ulfO, ulfEye);

		projM.copyFrom(SbMatrix.identity());

		// Convenient stuff for building the projection matrices
		float rightMinusLeft = lrfEye.getValueRead()[0] - llfEye.getValueRead()[0];
		float rightPlusLeft = lrfEye.getValueRead()[0] + llfEye.getValueRead()[0];

		float topMinusBottom = ulfEye.getValueRead()[1] - llfEye.getValueRead()[1];
		float topPlusBottom = ulfEye.getValueRead()[1] + llfEye.getValueRead()[1];

		final float farMinusNear = nearToFar;
		float far1 = nearDist + nearToFar;
		float farPlusNear = far1 + nearDist;

		final float[][] proj = projM.getValue();

		if (type == ProjectionType.ORTHOGRAPHIC) {
			proj[0][0] = 2.0f / rightMinusLeft;
			proj[1][1] = 2.0f / topMinusBottom;
			proj[2][2] = -2.0f / farMinusNear;

			proj[3][0] = -rightPlusLeft / rightMinusLeft;
			proj[3][1] = -topPlusBottom / topMinusBottom;
			proj[3][2] = -farPlusNear / farMinusNear;
		} else { // type == PERSPECTIVE

			proj[0][0] = 2.0f * nearDist / rightMinusLeft;

			proj[1][1] = 2.0f * nearDist / topMinusBottom;

			proj[2][0] = rightPlusLeft / rightMinusLeft;
			proj[2][1] = topPlusBottom / topMinusBottom;
			proj[2][2] = -farPlusNear / farMinusNear;
			proj[2][3] = -1.0f;

			proj[3][2] = -2.0f * nearDist * far1 / farMinusNear;
			proj[3][3] = 0.0f;
		}

	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns a matrix that transforms the view volume into camera
	// space: it translates the view volume so the view point is at the
	// origin, and rotates it so the view direction is along the
	// negative z axis.
	//
	// Use: public

	public SbMatrix getCameraSpaceMatrix()
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbMatrix m = new SbMatrix(), m2 = new SbMatrix();

		// Translate projPoint to (0,0,0)
		m.setTranslate(projPoint.operator_minus());

		// Rotate projDir into negative z axis
		m2.setRotate(new SbRotation(projDir, new SbVec3f(0.0f, 0.0f, -1.0f)));
		m.operator_mul_equal(m2);

		return m;
	}

	/**
	 * Like the method above, but returns the affine and projection parts together
	 * in one matrix (i.e., affine.multRight( proj ) ).
	 * 
	 * @return
	 */
	public SbMatrix getMatrix() {

		final SbMatrix affine = new SbMatrix(), proj = new SbMatrix();
		getMatrices(affine, proj);

		return affine.multRight(proj);
	}

	// ! Maps a 2d point (in 0 <= x,y <= 1) to a 3d line.
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	//
	// Use: public

	public void projectPointToLine(final SbVec2f pt, SbVec3f line0, SbVec3f line1)
	//
	////////////////////////////////////////////////////////////////////////
	{
		float ptx = 2.0f * pt.operator_square_bracket(0) - 1.0f;
		float pty = 2.0f * pt.operator_square_bracket(1) - 1.0f;
		SbMatrix matM = new SbMatrix(getMatrix().inverse());
		float[][] mat = matM.getValue(); // java port
		float x, y, z, w;

		/* ptz = -1 */
		x = ptx * mat[0][0] + pty * mat[1][0] - mat[2][0] + mat[3][0];
		y = ptx * mat[0][1] + pty * mat[1][1] - mat[2][1] + mat[3][1];
		z = ptx * mat[0][2] + pty * mat[1][2] - mat[2][2] + mat[3][2];
		w = ptx * mat[0][3] + pty * mat[1][3] - mat[2][3] + mat[3][3];
		line0.setValue(x / w, y / w, z / w); // java port

		/* ptz = +1 */
		x += 2.0 * mat[2][0];
		y += 2.0 * mat[2][1];
		z += 2.0 * mat[2][2];
		w += 2.0 * mat[2][3];
		line1.setValue(x / w, y / w, z / w); // java port
	}

	// ! Maps a 2d point (in 0 <= x,y <= 1) to a 3d line.
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	//
	// Use: public

	public void projectPointToLine(final SbVec2f pt, SbLine line)
	//
	////////////////////////////////////////////////////////////////////////
	{
		SbVec3f p0 = new SbVec3f(), p1 = new SbVec3f();
		projectPointToLine(pt, p0, p1);
		line.setValue(p0, p1);
	}

	/**
	 * Maps the 3D point in world coordinates to a 2D point in normalized screen
	 * coordinates (0 <= x,y,z <= 1). The z-screen coordinate represents the
	 * homogenized z coordinate which goes (nonlinearly) from 0 at the near clipping
	 * plane to 1 at the far clipping plane.
	 * 
	 * @param src
	 * @param dst
	 */
	public void projectToScreen(final SbVec3f src, final SbVec3f dst_) {
		final SbMatrix mat = getMatrix();

		mat.multVecMatrix(src, dst_);

		float[] dst = dst_.getValueRead();

		// dst will now range from -1 to +1 in x, y, and z. Normalize this
		// to range from 0 to 1.
		dst_.setValue(0, (1.0f + dst[0]) / 2.0f);
		dst_.setValue(1, (1.0f + dst[1]) / 2.0f);
		dst_.setValue(2, (1.0f + dst[2]) / 2.0f);

	}

	// java port
	public SbVec3f projectToScreen(final SbVec3f src) {
		SbVec3f dst = new SbVec3f();
		projectToScreen(src, dst);
		return dst;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns a plane parallel to the near (or far) plane of the view
	// volume at a given distance from the projection point (eye).
	//
	// Use: public

	public SbPlane getPlane(float distFromEye)
	//
	////////////////////////////////////////////////////////////////////////
	{
		return new SbPlane(projDir.operator_minus(), projPoint.operator_add(projDir.operator_mul(distFromEye)));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns the point along the line of sight at the given distance
	// from the projection point (eye).
	//
	// Use: public

	public SbVec3f getSightPoint(float distFromEye)
	//
	////////////////////////////////////////////////////////////////////////
	{
		return projPoint.operator_add(projDir.operator_mul(distFromEye));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns the projection of a given point in normalized screen
	// coords (see projectToScreen()) onto the plane parallel to the
	// near plane that is at distFromEye units from the eye.
	//
	// Use: public

	public SbVec3f getPlanePoint(float distFromEye, final SbVec2f normPoint)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbLine line = new SbLine();
		final SbVec3f point = new SbVec3f();
		final SbPlane plane = getPlane(distFromEye);

		// Project screen point to line through view volume
		projectPointToLine(normPoint, line);

		// This intersection should always be valid, since the plane is
		// parallel to the near plane
		plane.intersect(line, point);

		return point;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns a rotation that would align a viewed object so that its
	// positive x-axis (of its object space) is to the right in the
	// view and it's positive y-axis is up. If rightAngleOnly is TRUE,
	// it will come as close as it can to this goal by using only 90
	// degree rotations.
	//
	// Use: public

	public SbRotation getAlignRotation(boolean rightAngleOnly)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbVec3f yAxis = new SbVec3f(0.0f, 1.0f, 0.0f);
		SbVec3f up = ulfO.operator_minus(llfO);
		SbVec3f right = lrfO.operator_minus(llfO);
		SbVec3f newRight = new SbVec3f();
		SbMatrix rotMat = new SbMatrix();
		SbRotation result = new SbRotation();

		up.normalize();
		right.normalize();

		if (!rightAngleOnly) {

			// First rotate so that y-axis becomes "up".
			result.setValue(yAxis, up);

			// Then rotate about "up" so the x-axis becomes "right".
			rotMat.setRotate(result);
			rotMat.multDirMatrix(new SbVec3f(1.0f, 0.0f, 0.0f), newRight);

			// Need to make certain that the rotation is phrased as a rot
			// about the y axis. If we just use
			// SbRotation(newRight,right), in the case where 'newRight' is
			// opposite direction of 'right' the algorithm gives us a 180
			// degree rot about z, not y, which screws things up.
			float thetaCos = newRight.dot(right);
			if (thetaCos < -0.99999) {
				result.operator_mul_equal(new SbRotation(new SbVec3f(0, 1, 0), 3.14159f));
			} else {
				result.operator_mul_equal(new SbRotation(newRight, right));
			}
		}

		else {
			final SbRotation rotToUp = new SbRotation(), rot1 = new SbRotation(), rot2 = new SbRotation();
			final SbVec3f vec = new SbVec3f();
			float d, max;
			int i;

			// Rotate to get the best possible rotation to put Y close to "up"
			rotToUp.setValue(yAxis, up.getClosestAxis());

			// Rotate to get the best possible rotation to put X close to "right".
			rotMat.setRotate(rotToUp);
			rotMat.multDirMatrix(new SbVec3f(1.0f, 0.0f, 0.0f), newRight);

			// Find which of the 4 rotations that are multiples of 90
			// degrees about the y-axis brings X closest to right

			max = -237.4f;

			for (i = 0; i < 4; i++) {

				// Rotate by -90, 0, 90, 180 degrees
				rot1.setValue(yAxis, (i - 1) * (float) M_PI_2);

				rot2.copyFrom(rot1.operator_mul(rotToUp));
				rotMat.setRotate(rot2);
				rotMat.multDirMatrix(newRight, vec);
				d = vec.dot(right);
				if (d > max) {
					result = rot2;
					max = d;
				}
			}
		}

		return result;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns a scale factor that would scale a unit sphere centered
	// at worldCenter so that it would appear to have the given radius
	// in normalized screen coordinates when projected onto the near
	// plane.
	//
	// Use: public

	public float getWorldToScreenScale(final SbVec3f worldCenter, float normRadius)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Project worldCenter into normalized coordinates
		final SbVec3f normCenter3 = new SbVec3f();
		projectToScreen(worldCenter, normCenter3);
		final SbVec2fSingle normCenter = new SbVec2fSingle(normCenter3.getValueRead()[0], normCenter3.getValueRead()[1]);

		// This method really behaves best if you keep the normalized
		// points within the (0,0) (1,1) range. So we shift the
		// normCenter if necessary
		boolean centerShifted = false;
		if (normCenter.getValue()[0] < 0.0) {
			normCenter.getValue()[0] = 0.0f;
			centerShifted = true;
		}
		if (normCenter.getValue()[0] > 1.0) {
			normCenter.getValue()[0] = 1.0f;
			centerShifted = true;
		}
		if (normCenter.getValue()[1] < 0.0) {
			normCenter.getValue()[1] = 0.0f;
			centerShifted = true;
		}
		if (normCenter.getValue()[1] > 1.0) {
			normCenter.getValue()[1] = 1.0f;
			centerShifted = true;
		}

		// We'll either take the distance between a point that's offset
		// vertically or horizontally from the original point. This
		// depends on the aspect ration of the view.
		//
		// If it's wider-than-tall, we'll use the vertical offset since
		// the area subtended in the world will not change vertically as
		// we stretch horizontally until we reach a square aspec.
		//
		// If it's taller-than-wide, we'll use the hozizontal offset for
		// similar reasons. Also, we'll offset up or down depending on
		// which brings us more toward the center of the viewport, where
		// results are better.

		// Should we use a vertical or horizontal offset?
		boolean goVertical = (getWidth() > getHeight());

		// Pick a point that is normRadius away from normCenter in the
		// desired direction.,
		final SbVec2fSingle offsetPoint = new SbVec2fSingle(normCenter);
		if (goVertical) {
			if (offsetPoint.getValue()[1] < 0.5)
				offsetPoint.getValue()[1] += normRadius;
			else
				offsetPoint.getValue()[1] -= normRadius;
		} else {
			if (offsetPoint.getValue()[0] < 0.5)
				offsetPoint.getValue()[0] += normRadius;
			else
				offsetPoint.getValue()[0] -= normRadius;
		}

		// The original method only works for perspective projections. The
		// problem is in construction of 'plane.' For an ortho view, this
		// should be a plane at the location worldCenter, but with a
		// normal in the direction parallel to our line.

		// Find centerLine, the line you get when you project normCenter into
		// the scene.
		final SbLine centerLine = new SbLine();
		projectPointToLine(normCenter, centerLine);

		// Find the plane that passes through worldCenter and is
		// perpendicular to the centerLine
		final SbVec3f norm = new SbVec3f(centerLine.getDirection());
		norm.normalize();
		final SbPlane plane = new SbPlane(norm, worldCenter);

		// Project offsetPoint onto that plane and return distance from
		// worldCenter.
		final SbLine offsetLine = new SbLine();
		projectPointToLine(offsetPoint, offsetLine);

		// Intersect centerLine with the plane to get the location of normCenter
		// projected onto that plane. If we didn't need to shift the normCenter
		// then this is just the same as worldCenter
		final SbVec3f worldSeedPoint = new SbVec3f(worldCenter);

		if (centerShifted == true) {
			boolean isOk = plane.intersect(centerLine, worldSeedPoint);

			if (!isOk) {
				// intersection did not succeeded, just return a value of 1
				return 1.0f;
			}
		}

		// Fix uninitialized memory read. We need to check if the plane
		// intersection is successful, and if not we must not use results
		// to calculate an answer. Instead, if the plane intersection
		// fails, we return 1.0
		final SbVec3f worldOffsetPoint = new SbVec3f();
		boolean isOk = plane.intersect(offsetLine, worldOffsetPoint);

		if (!isOk) {
			// intersection did not succeeded, just return a value of 1
			return 1.0f;
		}

		// intersection succeeded. return dist from worldCenter
		float answerDist = (worldSeedPoint.operator_minus(worldOffsetPoint)).length();

		return answerDist;
	}

	//
	// Description:
	// Projects the given 3D bounding box onto the near plane and
	// returns the size (in normalized screen coords) of the
	// rectangular region that encloses it.
	//
	// Use: public

	public SbVec2f projectBox(final SbBox3f box)
	//
	{
		final SbVec3f min = new SbVec3f(), max = new SbVec3f();
		final SbVec3f[] screenPoint = new SbVec3f[8];
		for (int i = 0; i < 8; i++) {
			screenPoint[i] = new SbVec3f();
		}

		box.getBounds(min, max);

		float[] min_ = min.getValueRead(); // java port
		float[] max_ = max.getValueRead(); // java port

		// Project points to (0 <= x,y,z <= 1) screen coordinates
		projectToScreen(new SbVec3f(min_[0], min_[1], min_[2]), screenPoint[0]);
		projectToScreen(new SbVec3f(min_[0], min_[1], max_[2]), screenPoint[1]);
		projectToScreen(new SbVec3f(min_[0], max_[1], min_[2]), screenPoint[2]);
		projectToScreen(new SbVec3f(min_[0], max_[1], max_[2]), screenPoint[3]);
		projectToScreen(new SbVec3f(max_[0], min_[1], min_[2]), screenPoint[4]);
		projectToScreen(new SbVec3f(max_[0], min_[1], max_[2]), screenPoint[5]);
		projectToScreen(new SbVec3f(max_[0], max_[1], min_[2]), screenPoint[6]);
		projectToScreen(new SbVec3f(max_[0], max_[1], max_[2]), screenPoint[7]);

		// Find the encompassing 2d box (-1 <= x,y <= 1)
		final SbBox2f fBox = new SbBox2f();
		for (int i = 0; i < 8; i++)
			fBox.extendBy(
					new SbVec2f(screenPoint[i].operator_square_bracket(0), screenPoint[i].operator_square_bracket(1)));

		// Return size of box
		final SbVec2fSingle size = new SbVec2fSingle();
		fBox.getSize(size.getValue());
		return size;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Given a view volume, this narrows the view to the given sub-rectangle
	// of the near plane. The coordinates of the rectangle are between
	// 0 and 1, where (0,0) is the lower-left corner of the near plane
	// and (1,1) is the upper-right corner.
	//
	// Use: public

	public SbViewVolume narrow(float left, float bottom, float right, float top)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbViewVolume vv = new SbViewVolume();

		vv.type = type;
		vv.projPoint.copyFrom(projPoint);
		vv.projDir.copyFrom(projDir);
		vv.llfO.copyFrom((lrfO.operator_minus(llfO)).operator_mul(left)
				.operator_add((ulfO.operator_minus(llfO)).operator_mul(bottom)).operator_add(llfO));
		vv.lrfO.copyFrom((lrfO.operator_minus(llfO)).operator_mul(right)
				.operator_add((ulfO.operator_minus(llfO)).operator_mul(bottom)).operator_add(llfO));
		vv.ulfO.copyFrom((lrfO.operator_minus(llfO)).operator_mul(left)
				.operator_add((ulfO.operator_minus(llfO)).operator_mul(top)).operator_add(llfO));
		vv.llf.copyFrom(vv.llfO.operator_add(vv.projPoint)); // For compatibility
		vv.lrf.copyFrom(vv.lrfO.operator_add(vv.projPoint));
		vv.ulf.copyFrom(vv.ulfO.operator_add(vv.projPoint));
		vv.nearDist = nearDist;
		vv.nearToFar = nearToFar;

		return vv;
	}

	/**
	 * Returns the positive z axis in eye space. In this coordinate system, the z
	 * value of the near plane should be GREATER than the z value of the far plane.
	 * 
	 * @return
	 */
	public SbVec3f zVector() {
		final SbPlane plane = new SbPlane();

		// note dependency on how the plane is calculated: we want the
		// returned vector to point away from the projDir
		plane.copyFrom(new SbPlane(llfO, ulfO, lrfO));

		// If we wanted a world-space plane, would also have to translate
		// it to projPoint. But all we're interested in here is the normal:
		return plane.getNormal().operator_minus();

	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns a narrowed view volume which contains as tightly as
	// possible the given interval on the z axis (in eye space). The
	// returned view volume will never be larger than the current volume,
	// however.
	//
	// Near and far are relative to the current clipping planes, where
	// 1.0 is the current near plane and 0.0 is the current far plane.
	//
	// Use: public

	public SbViewVolume zNarrow(float nearVal, float farVal)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbPlane plane = new SbPlane();
		final SbViewVolume narrowed = new SbViewVolume();
		final SbVec3f zVec = new SbVec3f(zVector());

		// make sure we aren't expanding the volume
		if (nearVal > 1.0)
			nearVal = 1.0f;
		if (farVal < 0.0)
			farVal = 0.0f;

		narrowed.type = type;
		narrowed.projPoint.copyFrom(projPoint);
		narrowed.projDir.copyFrom(projDir);

		// the near-to-far distance can be calculated from the new near
		// and far values
		narrowed.nearDist = nearVal;
		narrowed.nearToFar = (nearVal - farVal) * nearToFar;

		// the new near plane
		// find the old near plane
		plane.copyFrom(new SbPlane(zVec, llfO));
		// offset it
		plane.offset((nearVal - 1.0f) * nearToFar);

		// intersect various lines with the new near plane to find the new
		// info for the view volume
		if (type == ProjectionType.ORTHOGRAPHIC) {
			plane.intersect(new SbLine(llfO, llfO.operator_add(projDir)), narrowed.llfO);
			plane.intersect(new SbLine(lrfO, lrfO.operator_add(projDir)), narrowed.lrfO);
			plane.intersect(new SbLine(ulfO, ulfO.operator_add(projDir)), narrowed.ulfO);
		} else { // type == PERSPECTIVE
			final SbVec3f origin = new SbVec3f(0, 0, 0);
			plane.intersect(new SbLine(origin, llfO), narrowed.llfO);
			plane.intersect(new SbLine(origin, lrfO), narrowed.lrfO);
			plane.intersect(new SbLine(origin, ulfO), narrowed.ulfO);
		}

		narrowed.llf.copyFrom(narrowed.llfO.operator_add(narrowed.projPoint)); // For compatibility
		narrowed.lrf.copyFrom(narrowed.lrfO.operator_add(narrowed.projPoint));
		narrowed.ulf.copyFrom(narrowed.ulfO.operator_add(narrowed.projPoint));

		return narrowed;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets up an orthographic view volume with the given sides.
	// The parameters are the same as for the GL ortho() routine.
	//
	// Use: public

	public void ortho(float left, float right, float bottom, float top, float nearVal, float farVal)
	//
	////////////////////////////////////////////////////////////////////////
	{
		type = ProjectionType.ORTHOGRAPHIC;
		projPoint.copyFrom(new SbVec3f(0.0f, 0.0f, 0.0f));
		projDir.copyFrom(new SbVec3f(0.0f, 0.0f, -1.0f));
		llfO.copyFrom(new SbVec3f(left, bottom, -nearVal));
		lrfO.copyFrom(new SbVec3f(right, bottom, -nearVal));
		ulfO.copyFrom(new SbVec3f(left, top, -nearVal));
		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
		nearDist = nearVal;
		nearToFar = farVal - nearVal;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets up a perspective view volume with the given field of view
	// and aspect ratio. The parameters are the same as for the GL
	// perspective() routine, except that the field of view angle is
	// specified in radians.
	//
	// Use: public

	public void perspective(float fovy, float aspect, float nearVal, float farVal)
	//
	////////////////////////////////////////////////////////////////////////
	{
		float tanfov = (float) Math.tan(fovy / 2.0);

		type = ProjectionType.PERSPECTIVE;

		projPoint.setValue(0.0f, 0.0f, 0.0f);
		projDir.setValue(0.0f, 0.0f, -1.0f);
		llfO.operator_square_bracket(2, -nearVal);
		lrfO.operator_square_bracket(2, -nearVal);
		ulfO.operator_square_bracket(2, -nearVal);

		ulfO.operator_square_bracket(1, nearVal * tanfov);
		llfO.operator_square_bracket(1, -ulfO.operator_square_bracket(1));
		lrfO.operator_square_bracket(1, -ulfO.operator_square_bracket(1));
		ulfO.operator_square_bracket(0, aspect * llfO.operator_square_bracket(1));
		llfO.operator_square_bracket(0, aspect * llfO.operator_square_bracket(1));
		lrfO.operator_square_bracket(0, -llfO.operator_square_bracket(0));

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));

		nearDist = nearVal;
		nearToFar = farVal - nearVal;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Translate the camera viewpoint. Note that this accomplishes
	// the reverse of doing a GL translate() command after defining a
	// camera, which translates the scene viewed by the camera.
	//
	// Use: public

	public void translateCamera(final SbVec3f v)
	//
	////////////////////////////////////////////////////////////////////////
	{
		projPoint.operator_plus_equal(v);
		llf.operator_plus_equal(v);
		lrf.operator_plus_equal(v);
		ulf.operator_plus_equal(v);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Rotate the camera view direction. Note that this accomplishes
	// the reverse of doing a GL rotate() command after defining a
	// camera, which rotates the scene viewed by the camera.
	//
	// Use: public

	public void rotateCamera(final SbRotation r)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbMatrix m = new SbMatrix();
		m.setRotate(r);

		m.multDirMatrix(projDir, projDir);

		m.multDirMatrix(llfO, llfO);
		m.multDirMatrix(lrfO, lrfO);
		m.multDirMatrix(ulfO, ulfO);

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
	}

	@Override
	public void copyFrom(Object other) {

		SbViewVolume otherViewVolume = (SbViewVolume) other;

		type = otherViewVolume.type;

		projPoint.copyFrom(otherViewVolume.projPoint);
		projDir.copyFrom(otherViewVolume.projDir);
		nearDist = otherViewVolume.nearDist;
		nearToFar = otherViewVolume.nearToFar;
		llf.copyFrom(otherViewVolume.llf);
		lrf.copyFrom(otherViewVolume.lrf);
		ulf.copyFrom(otherViewVolume.ulf);

		llfO.copyFrom(otherViewVolume.llfO);
		lrfO.copyFrom(otherViewVolume.lrfO);
		ulfO.copyFrom(otherViewVolume.ulfO);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Scales width and height of view volume by given factor.
	//
	// Use: public

	public void scale(float factor)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Don't like negative factors:
		if (factor < 0)
			factor = -factor;

		// Compute amount to move corners
		float diff = (1.0f - factor) / 2.0f;

		// Find vectors from lower left corner to lower right corner and
		// to upper left corner and scale them
		final SbVec3f widthVec = (lrfO.operator_minus(llfO)).operator_mul(diff);
		final SbVec3f heightVec = (ulfO.operator_minus(llfO)).operator_mul(diff);

		// Move all corners in correct direction
		llfO.operator_add_equal((heightVec.operator_add(widthVec)));
		lrfO.operator_add_equal((heightVec.operator_minus(widthVec)));
		ulfO.operator_add_equal((widthVec.operator_minus(heightVec)));

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Scales view volume to be the given ratio of its current width,
	// leaving the resulting view volume centered about the same point
	// (in the near plane) as the current one.
	//
	// Use: public

	public void scaleWidth(float ratio)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Don't like negative ratios:
		if (ratio < 0)
			ratio = -ratio;

		// Find vector from lower left corner to lower right corner
		final SbVec3f widthVec = lrfO.operator_minus(llfO);

		// Compute amount to move corners left or right and scale vector
		widthVec.operator_mul_equal((1.0f - ratio) / 2.0f);

		// Move all corners in correct direction
		llfO.operator_add_equal(widthVec);
		ulfO.operator_add_equal(widthVec);
		lrfO.operator_minus_equal(widthVec);

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Scales view volume to be the given ratio of its current height,
	// leaving the resulting view volume centered about the same point
	// (in the near plane) as the current one.
	//
	// Use: public

	public void scaleHeight(float ratio)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Don't like negative ratios:
		if (ratio < 0)
			ratio = -ratio;

		// Find vector from lower left corner to upper left corner
		final SbVec3f heightVec = ulfO.operator_minus(llfO);

		// Compute amount to move corners up or down and scale vector
		heightVec.operator_mul_equal((1.0f - ratio) / 2.0f);

		// Move all corners in correct direction
		llfO.operator_plus_equal(heightVec);
		lrfO.operator_plus_equal(heightVec);
		ulfO.operator_minus_equal(heightVec);

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	//
	// Use: public

	public SbViewVolume narrow(final SbBox3f box)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbViewVolume view = new SbViewVolume();
		final SbVec3f max = box.getMax(), min = box.getMin();

		view.copyFrom(narrow(min.getValueRead()[0], min.getValueRead()[1], max.getValueRead()[0], max.getValueRead()[1]));

		return view.zNarrow(max.getValueRead()[2], min.getValueRead()[2]);
	}

	/*
	 * ! Returns the six planes defining the view volume in the following order:
	 * left, bottom, right, top, near, far. Plane normals are directed into the view
	 * volume.
	 * 
	 * This method is an extension for Coin, and is not available in the original
	 * Open Inventor.
	 */
	 public void
	 getViewVolumePlanes(final Array<SbPlane> planes)
	 {
		 this.dpvv.update(this); //YB 
		 this.dpvv.getViewVolumePlanes(planes);
	 }

	/*
	 * ! Returns the six planes defining the view volume in the following order:
	 * left, bottom, right, top, near, far. Plane normals are directed into the view
	 * volume.
	 * 
	 * This method is an extension for Coin, and is not available in the original
	 * Open Inventor.
	 */
//	public void getViewVolumePlanes(final Array<SbPlane> planes) {
//		final SbVec3f far_ll = new SbVec3f();
//		final SbVec3f far_lr = new SbVec3f();
//		final SbVec3f far_ul = new SbVec3f();
//		final SbVec3f far_ur = new SbVec3f();
//
//		this.getPlaneRectangle(this.nearToFar, far_ll, far_lr, far_ul, far_ur);
//		final SbVec3f near_ur = this.ulf.operator_add(this.lrf.operator_minus(this.llf));
//
//		final SbVec3f f_ulf = dp_to_sbvec3f(this.ulf.operator_add(this.projPoint));
//		final SbVec3f f_llf = dp_to_sbvec3f(this.llf.operator_add(this.projPoint));
//		final SbVec3f f_lrf = dp_to_sbvec3f(this.lrf.operator_add(this.projPoint));
//		final SbVec3f f_near_ur = dp_to_sbvec3f(near_ur.operator_add(this.projPoint));
//		final SbVec3f f_far_ll = dp_to_sbvec3f(far_ll.operator_add(this.projPoint));
//		final SbVec3f f_far_lr = dp_to_sbvec3f(far_lr.operator_add(this.projPoint));
//		final SbVec3f f_far_ul = dp_to_sbvec3f(far_ul.operator_add(this.projPoint));
//		final SbVec3f f_far_ur = dp_to_sbvec3f(far_ur.operator_add(this.projPoint));
//
//		planes.set(0, new SbPlane(f_ulf, f_llf, f_far_ll)); // left
//		planes.set(1, new SbPlane(f_llf, f_lrf, f_far_lr)); // bottom
//		planes.set(2, new SbPlane(f_lrf, f_near_ur, f_far_ur)); // right
//		planes.set(3, new SbPlane(f_near_ur, f_ulf, f_far_ul)); // top
//		planes.set(4, new SbPlane(f_ulf, f_near_ur, f_lrf)); // near
//		planes.set(5, new SbPlane(f_far_ll, f_far_lr, f_far_ur)); // far
//
//		// check for inverted view volume (negative aspectRatio)
//		if (!planes.get(0).isInHalfSpace(f_lrf)) {
//			final SbVec3f n = new SbVec3f();
//			float D;
//
//			n.copyFrom(planes.get(0).getNormal());
//			D = planes.get(0).getDistanceFromOrigin();
//			planes.set(0, new SbPlane(n.operator_minus(), -D));
//
//			n.copyFrom(planes.get(2).getNormal());
//			D = planes.get(2).getDistanceFromOrigin();
//			planes.set(2, new SbPlane(n.operator_minus(), -D));
//		}
//		if (!planes.get(1).isInHalfSpace(f_near_ur)) {
//			final SbVec3f n = new SbVec3f();
//			float D;
//
//			n.copyFrom(planes.get(1).getNormal());
//			D = planes.get(1).getDistanceFromOrigin();
//			planes.set(1, new SbPlane(n.operator_minus(), -D));
//
//			n.copyFrom(planes.get(3).getNormal());
//			D = planes.get(3).getDistanceFromOrigin();
//			planes.set(3, new SbPlane(n.operator_minus(), -D));
//
//		}
//
//		if (!planes.get(4).isInHalfSpace(f_far_ll)) {
//			final SbVec3f n = new SbVec3f();
//			float D;
//
//			n.copyFrom(planes.get(4).getNormal());
//			D = planes.get(4).getDistanceFromOrigin();
//			planes.set(4, new SbPlane(n.operator_minus(), -D));
//
//			n.copyFrom(planes.get(5).getNormal());
//			D = planes.get(5).getDistanceFromOrigin();
//			planes.set(5, new SbPlane(n.operator_minus(), -D));
//
//		}
//
//	}

	//
	// Returns the four points defining the view volume rectangle at the
	// specified distance from the near plane, towards the far plane. The
	// points are returned in normalized view volume coordinates
	// (projPoint is not added).
//	public void getPlaneRectangle(final float distance, final SbVec3f lowerleft, final SbVec3f lowerright,
//			final SbVec3f upperleft, final SbVec3f upperright) {
//		final SbVec3f near_ur = new SbVec3f(this.ulf.operator_add(this.lrf.operator_minus(this.llf)));
//
//		// #if COIN_DEBUG
//		if (this.llf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f))
//				|| this.lrf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f))
//				|| this.ulf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f))
//				|| near_ur.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f))) {
//			SoDebugError.postWarning("SbDPViewVolume::getPlaneRectangle", "Invalid frustum.");
//
//		}
//		// #endif // COIN_DEBUG
//
//		if (this.type == ProjectionType.PERSPECTIVE) {
//			float depth = this.nearDist + distance;
//			final SbVec3f dir = new SbVec3f();
//			dir.copyFrom(this.llf);
//			dir.normalize(); // safe to normalize here
//			lowerleft.copyFrom(dir.operator_mul(depth).operator_div(dir.dot(this.projDir)));
//
//			dir.copyFrom(this.lrf);
//			dir.normalize(); // safe to normalize here
//			lowerright.copyFrom(dir.operator_mul(depth).operator_div(dir.dot(this.projDir)));
//
//			dir.copyFrom(this.ulf);
//			dir.normalize(); // safe to normalize here
//			upperleft.copyFrom(dir.operator_mul(depth).operator_div(dir.dot(this.projDir)));
//
//			dir.copyFrom(near_ur);
//			dir.normalize(); // safe to normalize here
//			upperright.copyFrom(dir.operator_mul(depth).operator_div(dir.dot(this.projDir)));
//		} else {
//			lowerleft.copyFrom(this.llf.operator_add(this.projDir.operator_mul(distance)));
//			lowerright.copyFrom(this.lrf.operator_add(this.projDir.operator_mul(distance)));
//			upperleft.copyFrom(this.ulf.operator_add(this.projDir.operator_mul(distance)));
//			upperright.copyFrom(near_ur.operator_add(this.projDir.operator_mul(distance)));
//		}
//	}
	//
	// Returns the four points defining the view volume rectangle at the
	// specified distance from the near plane, towards the far plane.
	public void getPlaneRectangle( float distance, final SbVec3f lowerleft,
	                                final SbVec3f lowerright,
	                                final SbVec3f upperleft,
	                                final SbVec3f upperright)
	{
	  final SbVec3f near_ur = this.ulf.operator_add(this.lrf.operator_minus(this.llf));
	  
	//#if COIN_DEBUG
	  if (this.llf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f)) ||
	      this.lrf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f)) ||
	      this.ulf.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f)) ||
	      near_ur.operator_equal_equal(new SbVec3f(0.0f, 0.0f, 0.0f))) {
	    SoDebugError.postWarning("SbDPViewVolume::getPlaneRectangle",
	                              "Invalid frustum.");

	  }
	//#endif // COIN_DEBUG

	  if (this.type == ProjectionType.PERSPECTIVE) {
	    final SbVec3f dir = new SbVec3f();
	    dir.copyFrom( this.llf.operator_minus(this.projPoint));
	    dir.normalize(); // safe to normalize here
	    lowerleft.copyFrom( this.llf.operator_add(dir.operator_mul( distance / dir.dot(this.projDir))));

	    dir.copyFrom( this.lrf.operator_minus(this.projPoint));
	    dir.normalize(); // safe to normalize here
	    lowerright.copyFrom( this.lrf.operator_add(dir.operator_mul( distance / dir.dot(this.projDir))));

	    dir.copyFrom( this.ulf.operator_minus(this.projPoint));
	    dir.normalize(); // safe to normalize here
	    upperleft.copyFrom( this.ulf.operator_add(dir.operator_mul( distance / dir.dot(this.projDir))));

	    dir.copyFrom( near_ur.operator_minus(this.projPoint));
	    dir.normalize(); // safe to normalize here
	    upperright.copyFrom( near_ur.operator_add(dir.operator_mul( distance / dir.dot(this.projDir))));
	  }
	  else {
	    lowerleft.copyFrom( this.llf.operator_add(this.projDir.operator_mul(distance)));
	    lowerright.copyFrom( this.lrf.operator_add(this.projDir.operator_mul(distance)));
	    upperleft.copyFrom( this.ulf.operator_add(this.projDir.operator_mul(distance)));
	    upperright.copyFrom( near_ur.operator_add(this.projDir.operator_mul(distance)));
	  }
	}


	//
	// some convenience function for converting between single precision
	// and double precision classes.
	//
	public static SbVec3f dp_to_sbvec3f(final SbVec3f v) {
		return new SbVec3f((float) (v.vec[0]), (float) (v.vec[1]), (float) (v.vec[2]));
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Transforms the view volume by the given matrix.
	// NOTE: If there is a scale and a rotation in the matrix, the angles
	// between the transformed projection direction and everything else
	// are not preserved!
	//
	// Use: internal

	public void transform(SbMatrix matrix)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbViewVolume xfVol = new SbViewVolume();
		final SbVec3f nearPt = new SbVec3f(), farPt = new SbVec3f();

		xfVol.type = type;
		matrix.multVecMatrix(projPoint, xfVol.projPoint);
		matrix.multDirMatrix(projDir, xfVol.projDir);
		xfVol.projDir.normalize();

		// Matrices that translate to/from origin-centered space.
		// We want to find llf', and we know that:
		// llf'+projPoint' = matrix*(llf+projPoint)

		matrix.multVecMatrix((llfO.operator_add(projPoint)), xfVol.llfO);
		xfVol.llfO.operator_minus_equal(xfVol.projPoint);
		matrix.multVecMatrix((lrfO.operator_add(projPoint)), xfVol.lrfO);
		xfVol.lrfO.operator_minus_equal(xfVol.projPoint);
		matrix.multVecMatrix((ulfO.operator_add(projPoint)), xfVol.ulfO);
		xfVol.ulfO.operator_minus_equal(xfVol.projPoint);

		matrix.multVecMatrix(projPoint.operator_add(projDir.operator_mul(nearDist)), nearPt);
		matrix.multVecMatrix(projPoint.operator_add(projDir.operator_mul((nearDist + nearToFar))), farPt);
		xfVol.nearDist = (nearPt.operator_minus(xfVol.projPoint)).length();
		if (nearDist < 0)
			xfVol.nearDist = -xfVol.nearDist;
		xfVol.nearToFar = (farPt.operator_minus(xfVol.projPoint)).length() - xfVol.nearDist;

		this.copyFrom(xfVol);

		// Check for inside-out view volume:
		SbVec3f wVec = lrfO.operator_minus(llfO);
		SbVec3f hVec = ulfO.operator_minus(llfO);
		if ((hVec.cross(wVec)).dot(projDir) <= 0.0) {
			// Swap left and right:
			final SbVec3f temp = new SbVec3f(llfO);
			llfO.copyFrom(lrfO);
			lrfO.copyFrom(temp);
			ulfO.copyFrom(llfO.operator_add(hVec));
		}

		llf.copyFrom(llfO.operator_add(projPoint)); // For compatibility
		lrf.copyFrom(lrfO.operator_add(projPoint));
		ulf.copyFrom(ulfO.operator_add(projPoint));
	}

	// ! Returns projection information.
	public ProjectionType getProjectionType() {
		return type;
	}

	// ! Returns projection information.
	public SbVec3f getProjectionPoint() {
		return projPoint;
	}

	// ! Returns projection information.
	public SbVec3f getProjectionDirection() {
		return projDir;
	}

	// ! Returns distance from projection point to near plane.
	public float getNearDist() {
		return nearDist;
	}

	// ! Returns distance from projection point to far plane.
	public float getFarDist() {
		return nearDist + nearToFar;
	}

	// ! Returns bounds of viewing frustum.
	public float getWidth() {
		return (lrfO.operator_minus(llfO)).length();
	}

	// ! Returns bounds of viewing frustum.
	public float getHeight() {
		return (ulfO.operator_minus(llfO)).length();
	}

	// ! Returns bounds of viewing frustum.
	public float getDepth() {
		return nearToFar;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Intersects view volume with point. Returns true if intersection.
	//
	// Use: internal

	public boolean intersect(final SbVec3f point)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Transform point to origin:
		final SbVec3f pt = point.operator_minus(projPoint);

		// Compare the point with all 6 planes of the view volume
		final SbVec3f origin = new SbVec3f(0, 0, 0);

		if (type == ProjectionType.PERSPECTIVE) {
			// Left plane is formed from origin, llfO, ulfO
			final SbPlane leftPlane = new SbPlane(origin, llfO, ulfO);
			if (!leftPlane.isInHalfSpace(pt))
				return false;

			// Figure out urf point:
			final SbVec3f urfO = lrfO.operator_add((ulfO.operator_minus(llfO)));
			// Right is origin, urfO, lrfO
			final SbPlane rightPlane = new SbPlane(origin, urfO, lrfO);
			if (!rightPlane.isInHalfSpace(pt))
				return false;

			// Near is lrfO, llfO, ulfO:
			final SbPlane nearPlane = new SbPlane(lrfO, llfO, ulfO);
			if (!nearPlane.isInHalfSpace(pt))
				return false;

			// Far is near points in opposite order, translated to far plane:
			final SbVec3f farOffset = projDir.operator_mul(nearToFar);
			final SbPlane farPlane = new SbPlane(ulfO.operator_add(farOffset), llfO.operator_add(farOffset),
					lrfO.operator_add(farOffset));
			if (!farPlane.isInHalfSpace(pt))
				return false;

			// Bottom is origin, lrfO, llfO
			final SbPlane bottomPlane = new SbPlane(origin, lrfO, llfO);
			if (!bottomPlane.isInHalfSpace(pt))
				return false;

			// Finally, top is origin, ulfO, urfO
			final SbPlane topPlane = new SbPlane(origin, ulfO, urfO);
			if (!topPlane.isInHalfSpace(pt))
				return false;
		}

		else { // type == ORTHOGRAPHIC
			// Left plane is formed from llfO, lff+projDir, ulfO
			final SbPlane leftPlane = new SbPlane(llfO, llfO.operator_add(projDir), ulfO);
			if (!leftPlane.isInHalfSpace(pt))
				return false;

			// Figure out urfO point:
			final SbVec3f urfO = lrfO.operator_add((ulfO.operator_minus(llfO)));
			// Right is urfO+projDir, lrfO, urfO
			final SbPlane rightPlane = new SbPlane(urfO.operator_add(projDir), lrfO, urfO);
			if (!rightPlane.isInHalfSpace(pt))
				return false;

			// Near is lrfO, llfO, ulfO:
			final SbPlane nearPlane = new SbPlane(lrfO, llfO, ulfO);
			if (!nearPlane.isInHalfSpace(pt))
				return false;

			// Far is near points in opposite order, translated to far plane:
			final SbVec3f farOffset = projDir.operator_mul(nearToFar);
			final SbPlane farPlane = new SbPlane(ulfO.operator_add(farOffset), llfO.operator_add(farOffset),
					lrfO.operator_add(farOffset));
			if (!farPlane.isInHalfSpace(pt))
				return false;

			// Bottom is lrfO, lrfO+projDir, lrfO, llfO
			final SbPlane bottomPlane = new SbPlane(lrfO, lrfO.operator_add(projDir), llfO);
			if (!bottomPlane.isInHalfSpace(pt))
				return false;

			// Finally, top is urfO, ulfO, ulfO+projDir
			final SbPlane topPlane = new SbPlane(urfO, ulfO, ulfO.operator_add(projDir));
			if (!topPlane.isInHalfSpace(pt))
				return false;
		}

		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Intersects view volume with line segment. Returns true if intersection.
	//
	// Use: internal

	public boolean intersect(final SbVec3f p0, final SbVec3f p1, final SbVec3f closestPoint)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SbLine line = new SbLine(p0, p1);
		final SbLine projLine = new SbLine();
		final SbVec3f centerPt = new SbVec3f(), ptOnProjLine = new SbVec3f();

		// Use line from projection point through center of near rectangle
		centerPt.copyFrom(llfO.operator_add(projPoint.operator_add(
				((ulfO.operator_minus(llfO)).operator_add((lrfO.operator_minus(llfO)))).operator_mul(0.5f))));
		if (type == ProjectionType.ORTHOGRAPHIC)
			// projDir is normalized, so it might not be in the same
			// ballpark as centerPt. To fix this, use the vector from the
			// near plane to the far plane.
			projLine.setValue(centerPt.operator_minus(projDir.operator_mul(nearToFar)), centerPt);
		else
			projLine.setValue(projPoint, centerPt);

		boolean validIntersection =

				// Find point on segment that's closest to projection line.
				// (If they are parallel, no intersection.)
				(line.getClosestPoints(projLine, closestPoint, ptOnProjLine) &&

				// Make sure this point is within the ends of the segment
						((closestPoint.operator_minus(p0)).dot(p1.operator_minus(p0)) >= 0.0
								&& (closestPoint.operator_minus(p1)).dot(p0.operator_minus(p1)) >= 0.0)
						&&

						// Also make sure that the intersection is within the view volume
						intersect(closestPoint));

		return validIntersection;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Intersects view volume with box. Returns true if intersection.
	//
	// Use: internal

	public boolean intersect(final SbBox3f box)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Empty bboxes can cause problems:
		if (box.isEmpty())
			return false;

		//
		// The bounding box is the set of all points between its bounds;
		// that is, all points (x,y,z) such that:
		// x_min <= x <= x_max
		// y_min <= y <= y_max
		// z_min <= z <= z_max
		// (Inventor bounding boxes are axis-aligned)
		//
		// We will consider there to be no intersection if all of those
		// points are on the 'outside' side of any of the view-volume
		// planes (this may pass some things that could be culled, but
		// will never cull things that are visible). So we want to see if
		// any point is on the inside, in which case the intersection
		// returns true.
		//
		// The equation of the view-volume planes is Ax+By+Cz=D, where
		// (A,B,C) is the plane normal and D is the distance from the
		// origin. The condition for a point (x,y,z) being on the
		// 'outside' side is Ax+By+Cz-D < 0. We need to test the largest
		// value of Ax+By+Cz-D to see if it is negative. If it is, then we
		// know all points are outside.
		//
		// We can minimize the number of point/plane checks we do by
		// noticing that Ax+By+Cx-D will be greatest when each of its
		// terms is greatest; for example, if A is positive, Ax will be
		// greatest when x is x_max. If A is negative, Ax will be greatest
		// when x is x_min.
		// So, we can reduce the test to a check of one point against each
		// of the 6 plane equations. The outsideTest() method does this.
		//

		final SbVec3f min = new SbVec3f(), max = new SbVec3f();
		box.getBounds(min, max);

		// Transform bbox to origin:
		min.operator_minus_equal(projPoint);
		max.operator_minus_equal(projPoint);

		// OPPORTUNITY FOR OPTIMIZATION HERE: We could precompute planes
		// and save some work.

		final SbVec3f origin = new SbVec3f(0, 0, 0);

		if (type == ProjectionType.PERSPECTIVE) {
			// Left plane is formed from origin, llfO, ulfO
			final SbPlane leftPlane = new SbPlane(origin, llfO, ulfO);
			if (outsideTest(leftPlane, min, max))
				return false;

			// Figure out urf point:
			final SbVec3f urfO = lrfO.operator_add(ulfO.operator_minus(llfO));
			// Right is origin, urfO, lrfO
			final SbPlane rightPlane = new SbPlane(origin, urfO, lrfO);
			if (outsideTest(rightPlane, min, max))
				return false;

			// Near is lrfO, llfO, ulfO:
			final SbPlane nearPlane = new SbPlane(lrfO, llfO, ulfO);
			if (outsideTest(nearPlane, min, max))
				return false;

			// Far is near points in opposite order, translated to far plane:
			final SbVec3f farOffset = projDir.operator_mul(nearToFar);
			final SbPlane farPlane = new SbPlane(ulfO.operator_add(farOffset), llfO.operator_add(farOffset),
					lrfO.operator_add(farOffset));
			if (outsideTest(farPlane, min, max))
				return false;

			// Bottom is origin, lrfO, llfO
			final SbPlane bottomPlane = new SbPlane(origin, lrfO, llfO);
			if (outsideTest(bottomPlane, min, max))
				return false;

			// Finally, top is origin, ulfO, urfO
			final SbPlane topPlane = new SbPlane(origin, ulfO, urfO);
			if (outsideTest(topPlane, min, max))
				return false;
		}

		else { // type == ORTHOGRAPHIC
			// Left plane is formed from llfO, lff+projDir, ulfO
			final SbPlane leftPlane = new SbPlane(llfO, llfO.operator_add(projDir), ulfO);
			if (outsideTest(leftPlane, min, max))
				return false;

			// Figure out urfO point:
			final SbVec3f urfO = lrfO.operator_add((ulfO.operator_minus(llfO)));
			// Right is urfO+projDir, lrfO, urfO
			final SbPlane rightPlane = new SbPlane(urfO.operator_add(projDir), lrfO, urfO);
			if (outsideTest(rightPlane, min, max))
				return false;

			// Near is lrfO, llfO, ulfO:
			final SbPlane nearPlane = new SbPlane(lrfO, llfO, ulfO);
			if (outsideTest(nearPlane, min, max))
				return false;

			// Far is near points in opposite order, translated to far plane:
			final SbVec3f farOffset = projDir.operator_mul(nearToFar);
			final SbPlane farPlane = new SbPlane(ulfO.operator_add(farOffset), llfO.operator_add(farOffset),
					lrfO.operator_add(farOffset));
			if (outsideTest(farPlane, min, max))
				return false;

			// Bottom is lrfO, lrfO+projDir, lrfO, llfO
			final SbPlane bottomPlane = new SbPlane(lrfO, lrfO.operator_add(projDir), llfO);
			if (outsideTest(bottomPlane, min, max))
				return false;

			// Finally, top is urfO, ulfO, ulfO+projDir
			final SbPlane topPlane = new SbPlane(urfO, ulfO, ulfO.operator_add(projDir));
			if (outsideTest(topPlane, min, max))
				return false;
		}

		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns TRUE if the bounding box defined by the two given points
	// is totally outside the given plane. See the comments in
	// intersect(bbox) for details.
	//
	// Use: internal

	public boolean outsideTest(final SbPlane p, final SbVec3f min, final SbVec3f max)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final float[] abc = p.getNormal().getValueRead();
		float sum;

		// Compute the greatest value of Ax+By+Cz-D
		sum = -p.getDistanceFromOrigin(); // -D
		sum += abc[0] > 0.0 ? max.getValueRead()[0] * abc[0] : min.getValueRead()[0] * abc[0]; // Ax
		sum += abc[1] > 0.0 ? max.getValueRead()[1] * abc[1] : min.getValueRead()[1] * abc[1]; // By
		sum += abc[2] > 0.0 ? max.getValueRead()[2] * abc[2] : min.getValueRead()[2] * abc[2]; // Cz

		// Box is outside only if largest value is negative
		return (sum < 0.0 ? true : false);
	}

	/*!
	  Calculates the bbox of the intersection between \a bbox and the view volume.
	  
	  \since Coin 4.0
	*/

	public SbBox3f 
	intersectionBox(final SbBox3f box)
	{
	  int i;
	  //SbVec3f vvpts[8];
	    final Array<SbVec3f> vvpts = new Array<>(SbVec3f.class,new SbVec3f[8]);
	  final SbBox3f commonVolume = new SbBox3f();
	  final SbVec3f bmin = new SbVec3f(box.getMin());
	  final SbVec3f bmax = new SbVec3f(box.getMax());
	  //SbPlane planes[6];
	    final Array<SbPlane> planes = new Array<>(SbPlane.class,new SbPlane[6]);

	  //*****************************************************************************
	  // find the 8 view volume corners
	  this.getPlaneRectangle(0.0f, vvpts.get(0), vvpts.get(1), vvpts.get(2), vvpts.get(3));
	  this.getPlaneRectangle(this.nearToFar, vvpts.get(4), vvpts.get(5), vvpts.get(6), vvpts.get(7));

	  //*****************************************************************************
	  // all all view volume points inside the original bbox
	  for (i = 0; i < 8; i++) {
	    if (box.intersect(vvpts.get(i))) commonVolume.extendBy(vvpts.get(i));
	  }

	  //*****************************************************************************
	  // add all bbox corner points inside the view volume
	  this.getViewVolumePlanes(planes);
	  int inside = 0;
	  for (i = 0; i < 8; i++) {
	    final SbVec3f pt = new SbVec3f((i&1)!=0?bmin.getValueRead()[0]:bmax.getValueRead()[0],
	               (i&2)!=0?bmin.getValueRead()[1]:bmax.getValueRead()[1],
	               (i&4)!=0?bmin.getValueRead()[2]:bmax.getValueRead()[2]);
	    int j;
	    for (j = 0; j < 6; j++) {
	      if (!planes.get(j).isInHalfSpace(pt)) break;
	    }
	    if (j == 6) {
	      commonVolume.extendBy(pt);
	      inside++;
	    }
	  }
	  if (inside==8) return commonVolume;
	  
	  //*****************************************************************************
	  // clip the view volume against the bbox and add intersection points
	  // to commonVolume
	  //
	  final SbClip clipper = new SbClip();
	  // generate the 6 bbox planes, all pointing towards the center of the box
	  for (i = 0; i < 6; i++) {
	    int dim = i/2;
	    final SbVec3f n = new SbVec3f(0.0f, 0.0f, 0.0f);
	    n.setValue(dim, (i&1)!=0 ? 1.0f : -1.0f);
	    planes.get(i).copyFrom( new SbPlane(n, ((i&1)!=0 ? bmin.getValueRead()[dim] : -bmax.getValueRead()[dim])));
	  }

	  // clip view volume polygons against the bbox planes
	  clip_face(clipper, vvpts.get(0), vvpts.get(1), vvpts.get(3), vvpts.get(2), planes, commonVolume);
	  clip_face(clipper, vvpts.get(1), vvpts.get(5), vvpts.get(7), vvpts.get(3), planes, commonVolume);
	  clip_face(clipper, vvpts.get(5), vvpts.get(4), vvpts.get(6), vvpts.get(7), planes, commonVolume);
	  clip_face(clipper, vvpts.get(4), vvpts.get(0), vvpts.get(2), vvpts.get(6), planes, commonVolume);
	  clip_face(clipper, vvpts.get(4), vvpts.get(5), vvpts.get(1), vvpts.get(0), planes, commonVolume);
	  clip_face(clipper, vvpts.get(2), vvpts.get(3), vvpts.get(7), vvpts.get(6), planes, commonVolume);

	  return commonVolume;
	}

	public void clip_face(SbClip clipper, SbVec3f v0, SbVec3f v1,
              SbVec3f v2, SbVec3f v3,
              Array< SbPlane> planes, SbBox3f isect)
{
 int i;
 clipper.addVertex(v0);
 clipper.addVertex(v1);
 clipper.addVertex(v2);
 clipper.addVertex(v3);
 for (i = 0; i < 6; i++) {
   clipper.clip(planes.get(i));
 }
 int n = clipper.getNumVertices();
 for (i = 0; i < n; i++) {
   final SbVec3f tmp = new SbVec3f();
   clipper.getVertex(i, tmp,null);
   isect.extendBy(tmp);
 }
 clipper.reset();
}
}
