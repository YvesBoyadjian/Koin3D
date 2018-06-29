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
 |      This file defines the SoViewportRegionElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoViewportRegionElement
///  \ingroup Elements
///
///  Element that stores the current viewport region, which indicates
///  which part of the window is being rendered into. This information
///  is also needed during picking and bounding box computation to
///  determine sizes/locations of screen-space objects.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoViewportRegionElement extends SoElement {

	   protected final SbViewportRegion viewportRegion = new SbViewportRegion();
		     
  private static SbViewportRegion emptyViewportRegion;
  
  
  
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
    // Choose a reasonable default window size
    viewportRegion.setWindowSize((short)100, (short)100);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets viewport region to given matrix in element accessed from state.
//
// Use: public, static

public static void
set(SoState state,
                           final  SbViewportRegion vpReg)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewportRegionElement     elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoViewportRegionElement ) getElement(state, classStackIndexMap.get(SoViewportRegionElement.class));

    if (elt != null)
        elt.setElt(vpReg);
}

  
  
  ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns viewport region from state.
//
// Use: public, static

public static SbViewportRegion 
get(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewportRegionElement elt;

    elt = ( SoViewportRegionElement )
        getConstElement(state, classStackIndexMap.get(SoViewportRegionElement.class));
    if (elt != null )
        return elt.viewportRegion;
    else
        return emptyViewportRegion;
}

  
  
  ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if viewport regions are the same -- if they have
//    the same size and origin.
//
// Use: public

public boolean
matches(SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    return (viewportRegion.operator_equal_equal(
            (( SoViewportRegionElement ) elt).viewportRegion));
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
       SoViewportRegionElement result =
           (SoViewportRegionElement )getTypeId().createInstance();
   
       result.viewportRegion.copyFrom(viewportRegion);
   
       return result;
   }
     
  public static void initClass(Class<? extends SoElement> klass) {
	  
	     //SO_ELEMENT_INIT_CLASS(SoViewportRegionElement, SoElement);
	     
	     SoElement.initClass(SoViewportRegionElement.class);
	     
	          emptyViewportRegion = new SbViewportRegion((short)0, (short)0);
	       }
    
    public static Object createInstance() {
    	return new SoViewportRegionElement();
    }
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets viewport region in element to given viewport region.
//
// Use: protected, virtual

protected void
setElt(final SbViewportRegion vpReg)
//
////////////////////////////////////////////////////////////////////////
{
    viewportRegion.copyFrom(vpReg);
}
    
}
