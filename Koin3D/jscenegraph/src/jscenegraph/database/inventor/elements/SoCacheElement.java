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
 |   Classes:
 |      SoCacheElement
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoCacheElement
///  \ingroup Elements
///
///  Element that stores the most recently opened cache.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCacheElement extends SoElement {
	
	   private SoCache             cache;                 //!< Stores pointer to cache
		    
	   private static boolean       invalidated;            //!< invalidate() called?
		    	
		 public static void initClass(final Class<? extends SoElement> javaClass) {
			 SoElement.initClass(javaClass);
			 invalidated = false;
		 }
	   
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
    cache = null;
}

	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets cache in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, SoCache cache)
//
////////////////////////////////////////////////////////////////////////
{
    SoCacheElement      elem;

//#ifdef DEBUG
//    if (cache == NULL) {
//        SoDebugError::post("SoCacheElement::set",
//                           "NULL argument illegal");
//        return;
//    }
//#endif    

    // Get an instance we can change (pushing if necessary)
    elem = (SoCacheElement ) getElement(state, classStackIndexMap.get(SoCacheElement.class));

//    if (elt == null) {
//        SoDebugError.post("SoCacheElement::set", "unable to access element");
//        return;
//    }
//
//    elt.cache = cache;
//    elt.cache.ref();
//
//    // Let the state know a cache is open.
//    state.setCacheOpen(true);
    if (elem != null) {
        if (elem.cache != null) elem.cache.unref();
        elem.cache = cache;
        if (elem.cache != null) {
          elem.cache.ref();
          state.setCacheOpen(true);
        }
      }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if any cache is currently open in the state.
//
// Use: public, static

public static boolean
anyOpen(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoCacheElement elt = ( SoCacheElement )
        state.getConstElement(classStackIndexMap.get(SoCacheElement.class));

    // Assume that nobody has set a cache to NULL; that means the only
    // NULL cache is the first instance on the stack, which doesn't
    // represent a real cache.
    return (elt.cache != null);
}



	       //! Returns the cache stored in an instance. This may be NULL.
    public SoCache            getCache() { return cache; }
	   
// Documented in superclass. Overridden to initialize element.
public void
push(SoState state)
{
  super.push(state);
  this.cache = null;
}

	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, unref'ing the cache.
//
// Use: public

//public void
//pop(SoState state, SoElement prevTopElement)
////
//////////////////////////////////////////////////////////////////////////
//{
//    // The previous element is gone, so get rid of its cache
//    ((SoCacheElement ) prevTopElement).cache.unref();
//
//    // Let the state know whether a cache is still open. NOTE: this
//    // assumes there can't be an element with a NULL cache deeper than
//    // an element with a non-NULL cache.
//    state.setCacheOpen(cache != null);
//}

// Documented in superclass. Overridden to unref the cache, since the
// cache is ref'ed in set().
public void
pop(SoState state, SoElement prevTopElement)
{
  SoCacheElement prev =
    (SoCacheElement)
                                          prevTopElement
     ;
  if (prev.cache != null) {
    prev.cache.unref();
    prev.cache = null;
  }
  super.pop(state, prevTopElement);
  if (!this.anyOpen(state)) state.setCacheOpen(false);
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to print an error message and return
//    FALSE. Cache elements should never be compared, since they never
//    appear in the elements-used list of caches!
//
// Use: public

public boolean
matches( SoElement element)
//
////////////////////////////////////////////////////////////////////////
{
    SoDebugError.post("SoCacheElement::matches",
                       "cache elements should never be compared!!!");

    return false;
}

	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //     Override method to spit out error
	    //
	    // Use: protected
	    
	   public SoElement 
	    copyMatchInfo()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SoDebugError.post("SoCacheElement::copyMatchInfo",
	                           "cache elements should never be copied!");
	    
	       return null;
	    }
	    
    //! Returns the next cache element in the stack
    public SoCacheElement     getNextCacheElement() 
        { return (SoCacheElement ) getNextInStack(); }

	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Adds the given element to the elements used lists of all
	   //    currently open caches in the state.
	   //
	   // Use: internal
	   
	  public static void
	   addElement(SoState state, final SoElement elt)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // Each currently open caching separator has a corresponding
	       // SoCacheElement in the stack. Run through each instance in the
	       // stack, adding the element if the element's depth is smaller
	       // than the depth of the cache element instance.
	   
	       SoCacheElement        cacheElt;
	   
	       cacheElt = ( SoCacheElement )
	           state.getConstElement(classStackIndexMap.get(SoCacheElement.class));
	   
	       while (cacheElt != null && elt.getDepth() < cacheElt.getDepth()) {
	           cacheElt.cache.addElement(elt);
	           cacheElt = ( SoCacheElement ) cacheElt.getNextInStack();
	       }
	   }
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Invalidates any open caches.  Called by nodes that can't be
	   //    cached for some reason.
	   //
	   // Use: public, static
	   
	  public static void
	   invalidate(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoCacheElement        cacheElt;
	   
	       invalidated = true;
	   
	       cacheElt = ( SoCacheElement )
	           state.getConstElement(classStackIndexMap.get(SoCacheElement.class));
	   
	       while (cacheElt != null && cacheElt.cache != null) {
	           cacheElt.cache.invalidate();
	           cacheElt = ( SoCacheElement ) cacheElt.getNextInStack();
	       }
	   }
	   	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds a dependency on the given cache to all currently open
//    caches in the state.  This is called when a cache is used, to
//    make sure any dependencies it has are propogated to any
//    parent cache(s).
//
// Use: internal

public static void
addCacheDependency(SoState state, SoCache cache)
//
////////////////////////////////////////////////////////////////////////
{
    // Each currently open caching separator has a corresponding
    // SoCacheElement in the stack.  

    SoCacheElement        cacheElt;

    cacheElt = ( SoCacheElement )
        state.getConstElement(classStackIndexMap.get(SoCacheElement.class));

    while (cacheElt != null && cacheElt.cache != null) {
        cacheElt.cache.addCacheDependency(state, cache);
        cacheElt = ( SoCacheElement ) cacheElt.getNextInStack();
    }
}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns the previous invalidated state, and sets the invalidated
//flag to TRUE.  Used by SoGLCacheList to avoid auto-caching
//things that call ::invalidate().
//
//Use: internal, static

public static boolean
setInvalid(boolean newValue)
//
////////////////////////////////////////////////////////////////////////
{
	boolean oldValue = invalidated;
	invalidated = newValue;
	return oldValue;
}



    //! returns the current cache, from the top of the stack.  Does not
    //! cause a cache dependence like getConstElement().
    public static SoCache     getCurrentCache(SoState state)
        {return ((SoCacheElement)(state.getElementNoPush(classStackIndexMap.get(SoCacheElement.class)))).cache;}


	  
}
