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
 |      This file defines the SoMaterialBindingElement class.
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
///  \class SoMaterialBindingElement
///  \ingroup Elements
///
///  Element that stores the current material binding.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves
 *
 */
public class SoMaterialBindingElement extends SoInt32Element {

	   public enum Binding {
		            OVERALL(2),            
		            PER_PART(3),           
		            PER_PART_INDEXED(4),   
		            PER_FACE(5),           
		            PER_FACE_INDEXED(6),   
		            PER_VERTEX(7),         
		            PER_VERTEX_INDEXED(8)  ;
		    
//		    #ifndef IV_STRICT
//		            ,                       
//		            DEFAULT = OVERALL,
//		            NONE = OVERALL
//		    #endif
		            
		            private final int id;
		            Binding(int id) { this.id = id;}
		            public int getValue() {return id; }
		            public static Binding fromValue(int value) {
		            	switch(value) {
		            	case 2: return OVERALL;
		            	case 3: return PER_PART;
		            	case 4: return PER_PART_INDEXED;
		            	case 5: return PER_FACE;
		            	case 6: return PER_FACE_INDEXED;
		            	case 7: return PER_VERTEX;
		            	case 8: return PER_VERTEX_INDEXED;
		            	default: return null;
		            	}
		            }
		        };
		        
		        public    static void         set(SoState state, Binding binding)
        { SoInt32Element.set(classStackIndexMap.get(SoMaterialBindingElement.class), state, (int)binding.getValue()); }

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
	super.init(state);
    data = getDefault().getValue();
}

		        
		        
		        //! Returns current material binding from the state
		        public static Binding      get(SoState state)
		            { return Binding.fromValue(SoInt32Element.get(classStackIndexMap.get(SoMaterialBindingElement.class), state)); }

		        //! Returns the default material binding
		        public static Binding      getDefault()            { return Binding.OVERALL; }		        
		        
		  	  public static void
			   initClass(final Class<? extends SoElement> javaClass)
			   {
				  SoElement.initClass(javaClass);
			   }
		    }
