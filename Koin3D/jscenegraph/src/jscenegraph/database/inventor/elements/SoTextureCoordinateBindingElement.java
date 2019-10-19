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
 |      This file defines the SoTextureCoordinateBindingElement class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoTextureCoordinateBindingElement
///  \ingroup Elements
///
///  Element storing the current texture coordinate binding
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureCoordinateBindingElement extends SoInt32Element {

	   public
		        //! The (seemingly random) choice of values is for compatibility
		        //! with Inventor 2.0 binary format files:
		        enum Binding {
		   			NO_BINDING (0), // java port
		            PER_VERTEX (2),         //!< per vertex texture coordinates
		            PER_VERTEX_INDEXED (3);  //!< per vertex, indexed, texture coordinates
		            
		            private int value;
		            
		            Binding(int value) {
		            	this.value = value;
		            }
		            
		            public int getValue() {
		            	return value;
		            }
		            
		            public static Binding fromValue(int value) {
		            	switch(value) {
		            	case 0: return NO_BINDING; // java port
		            	case 2: return PER_VERTEX;
		            	case 3: return PER_VERTEX_INDEXED;
		            	default: return null;
		            	}
		            }
		        };
		        
    //! Sets the current texture binding in the state
    public static void         set(SoState state, Binding binding)
        { SoInt32Element.set(classStackIndexMap.get(SoTextureCoordinateBindingElement.class), state, (int)binding.getValue()); }
		        
    //! Returns current texture binding from the state
    public static Binding      get(SoState state)
             { return Binding.fromValue(SoInt32Element.get(classStackIndexMap.get(SoTextureCoordinateBindingElement.class), state)); }		         		        		   

    //! Returns the default texture binding
    public static Binding      getDefault()            { return Binding.PER_VERTEX_INDEXED; }

    ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes texture coordinate binding element.
//
// Use: extender public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    data = getDefault().getValue();
}

    
}