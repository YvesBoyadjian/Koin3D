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
 |      This file defines the SoGLRenderCache class, which is used for
 |      storing caches during GL rendering.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */


package jscenegraph.database.inventor.caches;

import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;


/////////////////////////////////////////////////////////////////////////
///
///  Class SoGLRenderCache:
///
///  A render cache stores a GL display list containing commands that
///  draw a representation of the shape(s) represented by the cache.
///  The display list id is stored in the cache instance.
///
///  Each instance has a list of all nested display lists
///  that it calls. This list maintains reference counts on the
///  instances within it.
///  
///  This version has a copy of SoGLLazyElement, which is handled differently
///  than other elements.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLRenderCache extends SoCache {

  private
    SoGLDisplayList     list;          //!< Display list structure
  private  boolean              listOpen;       //!< Whether display list is open
  private  final SbPList             nestedCaches = new SbPList();   //!< List of nested caches

    //! Save state that opened a cache, to use when it is closed:
  private  SoState            saveState;

    //! Keep a copy of SoGLLazyElement for comparison
  private  SoGLLazyElement    GLCacheLazyElement;
    
    //! Keep a copy of the GL state for copying back after cache call:
  private  final SoGLLazyElement.GLLazyState    cachedGLState = new SoGLLazyElement.GLLazyState();

    //! BitFlags for maintaining Lazy Element:
    //! indicates that GL must match for cache to be valid
  private  int            checkGLFlag;

    //! indicates that IV must match for cache to be valid
  private  int            checkIVFlag;

    //! indicates that a send must be issued prior to calling cache.
  private  int            doSendFlag;
    

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Constructor
//
// Use: public

public SoGLRenderCache(SoState state) {
 super(state);
//
////////////////////////////////////////////////////////////////////////

    // We haven't compiled a list yet
    list = null;
    listOpen = false;
    saveState = null;
    GLCacheLazyElement = null;
    checkGLFlag = 0;
    checkIVFlag = 0;
    doSendFlag = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // Unref all of the nested caches
    for (int i = 0; i < nestedCaches.getLength(); i++)
        ((SoGLDisplayList ) nestedCaches.operator_square_bracket(i)).unref();

    // delete the GLLazyElement
    if (GLCacheLazyElement != null)
        GLCacheLazyElement.destructor();
}

///////////////////////////////////////////////////////////////////////
//
// Description:
// Returns TRUE if cache is valid with respect to the given state
//
// Use: public, virtual

public boolean      
isValid( SoState state)
{
    if (!super.isValid(state)) return(false);
    // get the lazy element from the state:
    SoGLLazyElement eltInState = SoGLLazyElement.getInstance(state);
    
    // Send the components of the lazy element requiring IV=GL,
    // do this prior to checking GL matches and IV matches.
    if (doSendFlag!=0) eltInState.send(state, doSendFlag);

    // If cache's version of lazy element doesn't match what's in the
    // state, the cache is not valid.
    
    if (!GLCacheLazyElement.
            lazyMatches(checkGLFlag,checkIVFlag,eltInState)){
//#ifdef DEBUG
        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
            System.err.print( "CACHE DEBUG: cache(0x"+this+") not valid "
                    );
            System.err.print( "Because a lazy element match failed,\n");
            System.err.print( "GL and IV flags were "+checkGLFlag+" "+checkIVFlag+"\n");       
        }
//#endif /*DEBUG*/           
        return(false);
    }

    return(true);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destroy this cache.  Called by unref(); frees up OpenGL display
//    list.
//
// Use: protected, virtual

protected void
destroy(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    if (listOpen) close();

    // Let the GLCacheContext element know that this display list can
    // be freed when the cache context is valid (this destructor may
    // be called when it is illegal to issue any GL commands!)
    if (list != null) {
        list.unref(state);
        list = null;
    }

    GLCacheLazyElement.destructor();
    GLCacheLazyElement = null;
    super.destroy(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds a dependency of this instance on another cache instance.
//
// Use: public, virtual

public void
addNestedCache(SoGLDisplayList child)
//
////////////////////////////////////////////////////////////////////////
{
    // Add the cache to the list of nested caches
    nestedCaches.append(child);

    // Increment the reference count on the nested cache
    child.ref();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Opens the cache. All GL commands made until the next close()
//    will be part of the the display list cache.
//
// Use: public

public void
open(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    // if there is already an open GLRender cache, bail.
    if (SoCacheElement.anyOpen(state) || listOpen) {
        SoDebugError.post("SoGLRenderCache.open",
                           "A cache is already open!");
        return;
    }
//#endif /* DEBUG */

    listOpen = true;

    // We are now the currently open cache...
    SoCacheElement.set(state, this);

    // save state for comparison later:
    saveState = state;

    // set up the GLLazyElement:
//#ifdef DEBUG
    if (GLCacheLazyElement != null)
        SoDebugError.post("SoGLRenderCache.open",
                "Error reallocating CacheLazyElement");
//#endif /*DEBUG*/

    //Get the top Lazy Element, and have it make a copy for the cache:
    SoGLLazyElement le = SoGLLazyElement.getInstance(state);  
    GLCacheLazyElement = le.copyLazyMatchInfo(state);
    
    //initialize flags:
    checkGLFlag = 0;
    checkIVFlag = 0;
    doSendFlag = 0;

    // Create a new display list
    list = new SoGLDisplayList(state, SoGLDisplayList.Type.DISPLAY_LIST, 1);
    list.ref();
    list.open(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Closes the cache.
//
// Use: public

public void
close()
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (! listOpen) {
        SoDebugError.post("SoGLRenderCache.close",
                           "No cache is currently open!");
        return;
    }

//#endif /* DEBUG */

    listOpen = false;

    // copy final GLState into CacheLazyElement
    // for anything in lazy element that was sent
   
    SoGLLazyElement le = (SoGLLazyElement)
        saveState.getConstElement(SoGLLazyElement.getClassStackIndex(SoGLLazyElement.class));
    le.getCopyGL(GLCacheLazyElement, cachedGLState);       

    list.close(saveState);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Calls the cache. The display list will be sent to GL.
//
// Use: public

public void
call(SoState state)
//
////////////////////////////////////////////////////////////////////////
{ 

//#ifdef DEBUG
    if (list == null) {
        SoDebugError.post("SoGLRenderCache.call",
                           "Cache was never compiled!");
        return;
    }
//#endif /* DEBUG */

    // Make sure all open caches depend on us
    SoCacheElement.addCacheDependency(state, this);

    list.call(state);
     
    // get the current lazy element from the state 
    SoGLLazyElement currentLazyElt = SoGLLazyElement.getInstance(state);
          
    //If this cache call occurred within a cache, must pass info to
    //parent cache.
    if (state.isCacheOpen()){
        SoGLRenderCache parentCache = (SoGLRenderCache)
                SoCacheElement.getCurrentCache(state);
        SoGLRenderCache childCache = this;      
           
        currentLazyElt.mergeCacheInfo(childCache, parentCache, doSendFlag, 
            checkIVFlag, checkGLFlag);
    }   
    
    // copy back the CacheLazyElement's GL State
    // also set GLSendBits and invalidBits.
    currentLazyElt.copyBackGL(GLCacheLazyElement, cachedGLState);
}
	    //! method for the lazy element to set flags:
    public void setLazyBits(int ivFlag, int glFlag, int sendFlag)
        {checkGLFlag |= glFlag;
        checkIVFlag |= ivFlag;
        doSendFlag |= sendFlag;}
        
    public SoGLLazyElement getLazyElt() 
        {return GLCacheLazyElement;}

	
}
