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
 |      This file defines the SoBoundingBoxCache class, which is used for
 |      storing caches during GL rendering.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.caches;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.misc.SoState;


/////////////////////////////////////////////////////////////////////////
///
///  Class SoBoundingBoxCache:
///
///  A bounding box cache stores a bounding box and center point. It
///  also stores a flag indicating whether the box contains any line or
///  point objects, meaning that intersection tests with the box should
///  use a little extra leeway.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoBoundingBoxCache extends SoCache {
	
	  private	final		    SbXfBox3f           box = new SbXfBox3f();            //!< Bounding box
	  private	final		    SbBox3f             projectedBox = new SbBox3f();   //!< Box in local space
		    private		    boolean              centerSet;      //!< If the center was set
		    private	final		    SbVec3f             center = new SbVec3f();         //!< Center point
		    private	    boolean              hasLOrP;        //!< TRUE if contains lines or points
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoBoundingBoxCache(SoState state) {
 super(state);
//
////////////////////////////////////////////////////////////////////////

    hasLOrP = false;
}


    public SbXfBox3f    getBox()           { return box; }

    public SbBox3f      getProjectedBox() { return projectedBox; }

    
    //! Returns TRUE if the center is valid.
    public boolean              isCenterSet()      { return centerSet; }
        
    //! Returns the center (in object space)
    public SbVec3f      getCenter()        { return center; }

    //! Sets the hasLinesOrPoints flag to TRUE in all open bounding box
    //! caches in the given state. (The flag is FALSE by default.)
    public static void         setHasLinesOrPoints(SoState state)
{
    // Find all open caches in the state and set the flag in them
    SoCacheElement ce = ( SoCacheElement )
        state.getConstElement(SoCacheElement.getClassStackIndex(SoCacheElement.class));

    while (ce != null) {
        if (ce.getCache() != null)
            ((SoBoundingBoxCache ) ce.getCache()).hasLOrP = true;
        ce = ce.getNextCacheElement();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Stores a bounding box and center point in the cache.
//
// Use: public

public void
set( SbXfBox3f boundingBox, 
                        boolean cSet, SbVec3f centerPoint)
//
////////////////////////////////////////////////////////////////////////
{
    box.copyFrom(boundingBox);
    projectedBox.copyFrom(box.project());
    centerSet = cSet;
    center.copyFrom(centerPoint);
}



    //! Returns the hasLinesOrPoints flag
    public boolean              hasLinesOrPoints()          { return hasLOrP; }

}
