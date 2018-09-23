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
 |      This file defines the abstract SoEXTENDER SoFloatElement class.
 |      This is a base class used to make the library smaller by
 |      sharing code.
 |
 |   Classes:
 |      SoFloatElement
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoFloatElement
///  \ingroup Elements
///
///  Subclasses store a single float, int, or enum value.  This class
///  defines generic matches() and copy() methods.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFloatElement extends SoElement {

    //! Storage for data.
      protected float data;
      
    //! Get value.  Derived classes have static inline methods to pass
    //! in the stackIndex and cast the returned value to the right type.

      public static float        get(int stackIndex, SoState state)
        {return (( SoFloatElement )
            getConstElement(state, stackIndex)).data;}
    
      
  
////////////////////////////////////////////////////////////////////////
//
//Description:
//Overrides this method to return TRUE if the two
//elements match.
//
//Use: public

public boolean
matches( SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
	return (data == (( SoFloatElement ) elt).data);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set element's value.
//
// Use: public, static

public static void
set(int stackIndex, SoState state, float value)
//
////////////////////////////////////////////////////////////////////////
{
    SoFloatElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoFloatElement ) getElement(state, stackIndex);

    if (elt != null)
        elt.setElt(value);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Really set the element.
//
// Use: protected, virtual

protected void
setElt(float value)
//
////////////////////////////////////////////////////////////////////////
{
    data = value;
}


 	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Create a copy of this instance suitable for calling matches()
	   //     on.
	   //
	   // Use: protected
	   
	  public SoElement 
	   copyMatchInfo()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoFloatElement result =
	           (SoFloatElement )getTypeId().createInstance();
	   
	       result.data = data;
	   
	       return result;
	   }
	   	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes SoFloatElement class.
	   //
	   // Use: internal
	   
	  public static void
	   SoFloatElement_initClass(final Class<? extends SoElement> javaClass)
	   {
	       // We can't use the SO_ELEMENT_INIT_CLASS() macro here, because we
	       // don't want to set the stackIndex for this class to anything
	       // real. So we'll just do the rest by hand.
	   
	       classTypeIdMap.put(javaClass, SoType.createType(SoElement.getClassTypeId(SoElement.class),
	                                        new SbName("SoFloatElement"), null));
	       classStackIndexMap.put(javaClass, -1);
	   }
	   }
