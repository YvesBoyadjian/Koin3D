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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file contains the SbColor class definition.
 |
 |   Author(s)  : Alain Dumesny
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.awt.Color;


////////////////////////////////////////////////////////////////////////////////
//! Color vector class.
/*!
\class SbColor
\ingroup Basics
This class is used to represent an RGB color. Each component of the vector
is a floating-point number between 0.0 and 1.0. There are routines to
convert back and forth between RGB and HSV.
{}

\par See Also
\par
SbVec3f
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbColor extends SbVec3f {

	// Default constructor. 
	public SbColor() {
		super();
	}
	
	// Copy constructor
	public SbColor(SbColor other) {
		copyFrom(other);
	}
	
	// Constructors for color vector. 
	public SbColor(final float[] rgb) {
		 setValue(rgb); 
	}
	
	// Constructors for color vector. 
	public SbColor(float r, float g, float b) {
		 setValue(r, g, b); 
	}
	
	public SbColor(Color color) {
		float[] compArray = color.getRGBColorComponents(null);
		setValue(compArray);
	}
	
	public SbColor(float[] valuesArray, int indice) {
		super(valuesArray,indice);
	}

	// java port
	public int getPackedValue() {
		return getPackedValue(0);
	}
	
//
// Set value of vector from rgba color
//
public SbColor 
setPackedValue(int orderedRGBA, float[] transparency)
{
    float f = 1.0f / 255.0f;
    s(0, ((orderedRGBA & 0xFF000000)>>>24) * f);
    s(1, ((orderedRGBA & 0xFF0000) >>> 16) * f);
    s(2, ((orderedRGBA & 0xFF00) >>> 8) * f);
    transparency[0] = 1.0f - (orderedRGBA & 0xFF) * f;
    
    return (this);
}

	
	/**
	 * Returns an RGBA packed color value, derived from the color vector 
	 * and the passed transparency value. 
	 * The alpha component is set to (1.0 - transparency) * 255, 
	 * resulting in a hex value between 0 and 0xFF. 
	 * If transparency not specified, alpha is set to 0xFF (opaque). 
	 * 
	 * @param transparency
	 * @return
	 */
	//
	   // Returns orderedRGBA packed color format
	   //
	   
	public int getPackedValue(float transparency) {
		
	     return (
	    		           (((int) (g(0) * 255 + 0.5f)) << 24) +
	    		           (((int) (g(1) * 255 + 0.5f)) << 16) +
	    		           (((int) (g(2) * 255 + 0.5f)) << 8) +
	    		           ((int) ((1.0f - transparency) * 255 + 0.5f)));
	    		  	}
}

