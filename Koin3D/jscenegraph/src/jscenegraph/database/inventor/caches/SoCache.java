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
 |      This file defines the abstract SoCache class, the base class
 |      for all caches in Inventor.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.caches;

import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Destroyable;


/////////////////////////////////////////////////////////////////////////
///
///  Class SoCache:
///
///  This is the base class for all types of caches. Each cache
///  maintains the following information:
///
///      A reference count, used to allow sharing of cache instances.
///      ref() and unref() methods increment and decrement the count.
///
///      An elements-used list. This is a list of elements used in the
///      cache that are set outside it. A cache is invalidated if any
///      of these elements has changed since the cache was created.
///      There is also an elements-flag array so we can quickly tell if
///      an element type has already been added to the elements-used
///      list.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCache implements Destroyable {
	
	   private final SbPList             elementsUsed = new SbPList();           //!< Elements used in cache
	   private char[]       elementsUsedFlags;     //!< Which elements on list
		    
	   private        int                 refCount;               //!< Reference count
	   private        boolean                 invalidated;            //!< True if invalidate called
	   private        int                 depth;                  //!< Depth of state
		   	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor. Takes the state in effect when the cache is
//    created; it is assumed that the state is pushed before the cache
//    is created.
//
// Use: public

public SoCache(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    refCount = 0;
    invalidated = false;

    depth = state.getDepth();

    int bytesNeeded = (SoElement.getNumStackIndices()+7)/8;

    // Set the size of the elementsUsedFlags to be the maximum number
    // of elements currently enabled.  This is safe because if more
    // elements are enabled later, there is no way this cache could
    // care about them-- if a node that used the new element was added
    // below this cache, it would cause notification and would cause
    // the cache to be blown.
    elementsUsedFlags = new char[bytesNeeded];
    //memset(elementsUsedFlags, 0, bytesNeeded);
}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Adds an element to elements-used list if not already there and
	   //    its depth is less than the depth of this cache
	   //
	   // Use: public
	   
	  public void
	   addElement( SoElement elt)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // See if element is already on list
	       int byte_ = elt.getStackIndex() / 8;
	       int bit = elt.getStackIndex() % 8;
	      if (elt.getDepth() >= depth
	                   || ((elementsUsedFlags[byte_] >> bit) & 1) != 0)
	           return;
	   
	       // Make a copy we can later call matches() on:
	       SoElement newElt = elt.copyMatchInfo();
	       newElt.setDepth(elt.getDepth());
	   
	       // Add it to the list
	       elementsUsed.append((Object) newElt);
	       elementsUsedFlags[byte_] |= (1 << bit);
	   }
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Invalidates this cache.  Nodes sometime invalidate open caches. 
	   //
	   // Use: public
	   
	  public void
	   invalidate()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       invalidated = true;
	   }	  


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.  Unreferences any elements this cache depends on.
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Get rid of all the elements in list
    for (i = 0; i < elementsUsed.getLength(); i++)
        ((SoElement ) elementsUsed.operator_square_bracket(i)).destructor();

    elementsUsedFlags = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destroy this cache.  Called by unref(); by default, just calls the
//    destructor.  SoGLRenderCaches use this method to free up display
//    lists, if they can.
//
// Use: protected, virtual

protected void
destroy(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    this.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Increments reference count.
//
// Use: public

public void
ref()
//
////////////////////////////////////////////////////////////////////////
{
    refCount++;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Decrements reference count, destroying instance if count is now 0.
//
// Use: public

// java port
public void
unref() {
	unref(null);
}

public void
unref(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    if (--refCount == 0)
        destroy(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds a dependency of this instance on another cache instance.
//    The default method makes sure this cache depends on any elements
//    that the sub-cache depends on.
//
// Use: public, virtual

public void
addCacheDependency( SoState state, SoCache subCache)
//
////////////////////////////////////////////////////////////////////////
{
    for (int i = 0; i < subCache.elementsUsed.getLength(); i++) {
        SoElement eltInCache =
            ( SoElement ) subCache.elementsUsed.operator_square_bracket(i);

        //
        // This is pretty subtle: the depth of the element in the
        // cache may not match the depth of the (matching) element in
        // the state (a cache may be built at a node that is instanced
        // at two different depths, for example).  Because the depths
        // must be correct for the elementsUsed list to work
        // correctly, we must depend on the element in the state.
        //
        addElement(state.getConstElement(eltInCache.getStackIndex()));
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the cache is valid with resepect to the given
//    state. To be valid, each element in the elements-used list in
//    the cache must match (using the matches() method) the
//    corresponding element in the state, AND the list of overridden
//    elements in the cache must be exactly the same as the
//    corresponding list in the state.
//
// Use: public

public boolean
isValid(final SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // If explicitly invalidated, always return FALSE.
    if (invalidated) {
//#ifdef DEBUG
        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
            System.err.print( "CACHE DEBUG: cache(0x"+this+") not valid "
                    );
            System.err.print( "because SoCache::invalidate was called, ");
            System.err.print( "probably because a field or node\n");
            System.err.print( "underneath a node with a cache changed ");
            System.err.print( "or because a node with a cache contained\n");
            System.err.print( "an uncacheable node.\n");
        }           
//#endif
        return false;
    }

    int i;

    // Compare used elements for match
    for (i = 0; i < elementsUsed.getLength(); i++) {
         SoElement eltInCache = ( SoElement ) elementsUsed.operator_square_bracket(i);
         SoElement eltInState =
            state.getConstElement(eltInCache.getStackIndex());

        // If cache's version of element doesn't match what's in the
        // state, the cache is not valid.
        if (!eltInCache.matches(eltInState)) {

//#ifdef DEBUG
            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
            	System.err.print( "CACHE DEBUG: cache(0x"+this+") not valid"
                        );
            	System.err.print( " because element "+eltInState.getTypeId().getName().getString()+" does not match:\n");
            	System.err.print( "------\nElement in state:\n");
                eltInState.print(System.err);
                System.err.print( "------\nElement in cache:\n");
                eltInCache.print(System.err);
            }
//#endif
            return false;
        }           
    }

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the first element in the state that doesn't match an
//    element on the cache's dependency list.  Returns NULL if they
//    all match.
//
// Use: public

public SoElement 
getInvalidElement( SoState state) 
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Compare used elements for match
    for (i = 0; i < elementsUsed.getLength(); i++) {
        SoElement eltInCache = ( SoElement ) elementsUsed.operator_square_bracket(i);
        SoElement eltInState =
            state.getConstElement(eltInCache.getStackIndex());

        if (eltInCache != eltInState && !
            eltInCache.matches(eltInState)) {

            return eltInState;
        }           
    }

    return null;
}



}

