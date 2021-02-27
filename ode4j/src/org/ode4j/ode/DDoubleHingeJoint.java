/*************************************************************************
 *                                                                       *
 * Open Dynamics Engine, Copyright (C) 2001,2002 Russell L. Smith.       *
 * All rights reserved.  Email: russ@q12.org   Web: www.q12.org          *
 * Open Dynamics Engine 4J, Copyright (C) 2009-2014 Tilmann Zaeschke     *
 * All rights reserved.  Email: ode4j@gmx.de   Web: www.ode4j.org        *
 *                                                                       *
 * This library is free software; you can redistribute it and/or         *
 * modify it under the terms of EITHER:                                  *
 *   (1) The GNU Lesser General Public License as published by the Free  *
 *       Software Foundation; either version 2.1 of the License, or (at  *
 *       your option) any later version. The text of the GNU Lesser      *
 *       General Public License is included with this library in the     *
 *       file LICENSE.TXT.                                               *
 *   (2) The BSD-style license that is included with this library in     *
 *       the file ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT.         *
 *                                                                       *
 * This library is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the files    *
 * LICENSE.TXT, ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT for more   *
 * details.                                                              *
 *                                                                       *
 *************************************************************************/
package org.ode4j.ode;

import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;

public interface DDoubleHingeJoint extends DDoubleBallJoint {

	/**
	 * Set axis for double hinge joint.
	 * @param x x
	 * @param y y
	 * @param z Z
	 */
	void setAxis(double x, double y, double z);

	/**
	 * Set axis for double hinge joint.
	 * @param xyz xyz
	 */
	void setAxis(DVector3C xyz);

	/**
	 * Get axis for double hinge joint.
	 * @param result Vector containing the result
	 */
	void getAxis(DVector3 result);

	/**
	 * Set anchor1 for double hinge joint.
	 */
	@Override
	void setAnchor1(double x, double y, double z);

	/**
	 * Set anchor2 for double hinge joint.
	 */
	@Override
	void setAnchor2(double x, double y, double z);

	/**
	 * Get anchor1 from double hinge joint.
	 */
	@Override
	void getAnchor1(DVector3 result);

	/**
	 * Get anchor2 from double hinge joint.
	 */
	@Override
	void getAnchor2(DVector3 result);

	/**
	 * Get the set distance from double hinge joint.
	 */
	@Override
	double getDistance();

	/**
	 * Set double hinge joint parameter.
	 */
	@Override
	void setParam (PARAM_N parameter, double value);

	/**
	 * Get double hinge joint parameter.
	 */
	@Override
	double getParam (PARAM_N parameter);

}
