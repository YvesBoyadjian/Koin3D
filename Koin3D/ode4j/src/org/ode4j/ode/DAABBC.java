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

/**
 * Immutable interface for AABBs (Axis-aligned bounding boxes),
 * as defined in DAABB.
 * <p>
 * Axes are numbered from 0 to 2.
 *
 * @see DAABB
 * @author Tilmann Zaeschke
 */
public interface DAABBC {
	double getMin0();
	double getMin1();
	double getMin2();
	double getMax0();
	double getMax1();
	double getMax2();
	
	boolean isDisjoint(DAABBC aabb2);
	boolean isValid();
	
	double len0();
	double len1();
	double len2();
	
	/** 
	 * Get maximum extension of <tt>axis</tt>
	 * @param axis The axis (0, 1 or 2)
	 * @return max
	 */
	double getMax(int axis);

	
	/** 
	 * Get minimum extension of <tt>axis</tt>
	 * @param axis The axis (0, 1 or 2)
	 * @return min
	 */
	double getMin(int axis);
	
	
	/** 
	 * @return the lengths of this AABB as a vector.
	 */
	DVector3 getLengths();

	
	/** 
	 * @return the center of this AABB.
	 */
	DVector3 getCenter();
}
