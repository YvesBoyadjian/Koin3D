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
 |      This file defines the SoComplexityTypeElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoComplexityTypeElement
///  \ingroup Elements
///
///  Element that stores the current complexity type.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoComplexityTypeElement extends SoInt32Element {

	   public
		        //! These are the available complexity types:
		        enum Type {
		            OBJECT_SPACE(0),           //!< Complexity computed in object space
		            SCREEN_SPACE(1),           //!< Complexity computed in screen space
		            BOUNDING_BOX(2);            //!< Bounding box used to represent object
		            
		            private int value;
		            
		            Type(int value) {
		            	this.value = value;
		            }
		            
		            public int getValue() {
		            	return value;
		            }
		            
		            public static Type fromValue(int value) {
		            	switch(value) {
		            	case 0: return OBJECT_SPACE;
		            	case 1: return SCREEN_SPACE;
		            	case 2: return BOUNDING_BOX;
		            	default : return null;
		            	}
		            }
		        };


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    data = getDefault().getValue();
}

    //! Returns current complexity type from the state
public    static Type         get(SoState state)
        { return Type.fromValue(SoInt32Element.get(classStackIndexMap.get(SoComplexityTypeElement.class), state)); }

    //! Returns the default complexity type
	public    static Type         getDefault()            { return Type.OBJECT_SPACE; }


///////////////////////////////////////////////////////////////////////
//
// Description:
//  Set the complexity type in the state
//
public static void set(SoState state, Type type)
{
    SoInt32Element.set(classStackIndexMap.get(SoComplexityTypeElement.class), state, (int)type.getValue()); 
    SoShapeStyleElement.setComplexityType(state,(int)type.getValue());
}

		        
}
