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

import java.util.function.IntConsumer;

import jscenegraph.coin3d.inventor.SbVec2i32;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! 2D vector class.
/*!
\class SbVec2s
\ingroup Basics
2D vector class used to store 2D integer vectors and points. This class is used
throughout Inventor for arguments and return values.
{}

\par See Also
\par
SbVec3f, SbVec4f, SbVec2f
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2s implements Mutable {
	protected final short[] vec = new short[2]; 
	
	public SbVec2s() {
		
	}
	
    //! Constructor given 2 components.
    public SbVec2s( short v[])                           { setValue(v); }

	// Constructor given 2 components. 
	public SbVec2s(short x, short y) {
		 setValue(x, y); 
	}
	
    public SbVec2s(SbVec2s other) {
    	vec[0] = other.vec[0];
    	vec[1] = other.vec[1];
	}
    
    public SbVec2s( SbVec2i32 v) { setValue(v); }    

	//! Sets vector components.
    public SbVec2s    setValue(final short v[]) {
        vec[0] = v[0];
        vec[1] = v[1];

        return (this);    	
    }

	// Sets vector components. 
	public SbVec2s setValue(short x, short y) {
		
		  vec[0] = x;
		  vec[1] = y;
		   
		    return this;
		  }
	
/*!
  \since Coin 2.5
*/

public SbVec2s
setValue( SbVec2i32 v)
{
//#if COIN_DEBUG
  if (v.getValue()[0] > Short.MAX_VALUE || v.getValue()[0] < Short.MIN_VALUE ||
      v.getValue()[1] > Short.MAX_VALUE || v.getValue()[1] < Short.MIN_VALUE) {
    SoDebugError.post("SbVec2s::setValue", "SbVec2i32 argument out of range for SbVec2s");
  }
//#endif // COIN_DEBUG
  vec[0] = (short)(v.getValue()[0]);
  vec[1] = (short)(v.getValue()[1]);
  return this;
}

	
	
	// java port
	public void operator_assign(SbVec2s other) {
		vec[0] = other.vec[0];
		vec[1] = other.vec[1];
	}
	
	public short operator_square_bracket(int i) {
		return vec[i];
	}
	
//
// Component-wise binary vector addition operator
//

public SbVec2s
operator_add(final SbVec2s v2)
{
	final SbVec2s v1 = this;
    return new SbVec2s((short)(v1.vec[0] + v2.vec[0]),
                  (short)(v1.vec[1] + v2.vec[1]));
}


//
// Component-wise binary vector subtraction operator
//

public SbVec2s
operator_minus(final SbVec2s v2)
{
	final SbVec2s v1 = this;
    return new SbVec2s((short)(v1.vec[0] - v2.vec[0]),
                  (short)(v1.vec[1] - v2.vec[1]));
}

public SbVec2s operator_div( double d)
{ 
	final SbVec2s v = this;
	return v.operator_mul (1.0 / d); 
}

public SbVec2s
operator_mul(double d)
{
	final SbVec2s v = this;
    return new SbVec2s((short)(v.vec[0] * d), (short)(v.vec[1] * d));
}



	//! Returns vector components.
    public short[] getValue() {
    	return vec;
    }

	@Override
	public void copyFrom(Object other) {
		operator_assign((SbVec2s)other);
	}
 
	//
	// Equality comparison operator
	//

	public boolean
	operator_equal_equal(final SbVec2s v2)
	{
		final SbVec2s v1 = this;
	    return (v1.vec[0] == v2.vec[0] &&
	            v1.vec[1] == v2.vec[1]);
	}

	/**
	 * Java port
	 * @return
	 */
	public IntConsumer[] getRef() {
		IntConsumer[] ref = new IntConsumer[2];
		ref[0] = value -> vec[0] = (short) value;
		ref[1] = value -> vec[1] = (short) value;
		return ref;
	}

	// java port
	public short getX() {
		return vec[0];
	}

	//java port
	public short getY() {
		return vec[1];
	}

	public boolean operator_not_equal(SbVec2s other) {
		return !operator_equal_equal(other);
	}
}

