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

/**
 * @author Yves Boyadjian
 *
 */
public class SbColor4f extends SbVec4f {

	/**
	 * java port
	 * @param r
	 * @param g
	 * @param b
	 */
	  public SbColor4f(float r, float g, float b) {
		  this(r,g,b,1);
	  }
	  
	  public SbColor4f(float r, float g, float b, float a) {
		  vec[0] = r;
		  vec[1] = g;
		  vec[2] = b;
		  vec[3] = a;		  
	  }

	  /*!
	  Return color as a 32 bit packed integer in the form 0xRRGGBBAA.
	  \sa setPackedValue().
	 */
	public int getPackedValue()
	{
	  return (((int)(red()*255.0f + 0.5f) << 24) |
	          ((int)(green()*255.0f + 0.5f) << 16) |
	          ((int)(blue()*255.0f + 0.5f) << 8) |
	          (int)(alpha()*255.0f + 0.5f));
	}

	private
		  float red()  { return vec[0]; }
	private		  float green()  { return vec[1]; }
	private		  float blue()  { return vec[2]; }
	private		  float alpha() { return vec[3]; }
}
