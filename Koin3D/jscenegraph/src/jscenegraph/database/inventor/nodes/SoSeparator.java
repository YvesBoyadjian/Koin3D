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
 |      This file defines the SoSeparator node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.caches.SoGLCacheList;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoLocalBBoxMatrixElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 * 
 */

////////////////////////////////////////////////////////////////////////////////
//! Group node that saves and restores traversal state.
/*!
\class SoSeparator
\ingroup Nodes
This group node performs a push (save) of the traversal state before
traversing its children and a pop (restore) after traversing them.
This isolates the separator's children from the rest of the scene
graph. A separator can include lights, cameras, coordinates, normals,
bindings, and all other properties. Separators are relatively
inexpensive, so they can be used freely within scenes.


The SoSeparator node provides caching of state during rendering
and bounding box computation. This feature can be enabled by setting
the \b renderCaching  and \b boundingBoxCaching  fields. By default,
these are set to <tt>AUTO</tt>, which means that Inventor decides whether
to build a cache based on internal heuristics.


Separators can also perform culling during rendering and picking.
Culling skips over traversal of the separator's children if they are
not going to be rendered or picked, based on the comparison of the
separator's bounding box with the current view volume. Culling is
controlled by the \b renderCulling  and \b pickCulling  fields.  These
are also set to <tt>AUTO</tt> by default; however, render culling can be
expensive (and can interfere with render caching), so the <tt>AUTO</tt>
heuristics leave it disabled unless specified otherwise.

\par File Format/Default
\par
\code
Separator {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoHandleEventAction, SoRayPickAction, SoSearchAction
<BR> Saves the current traversal state, traverses all children, and restores the previous traversal state. 

\par See Also
\par
SoSelection, SoTransformSeparator
*/
////////////////////////////////////////////////////////////////////////////////


public class SoSeparator extends SoGroup implements Destroyable {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSeparator.class,this);
	   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSeparator.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSeparator.class); }    
	  
		public enum CacheEnabled {
			OFF(0),
			ON(1),
			AUTO(2);
			
			private int value;
			
			CacheEnabled(int value) {
				this.value = value;
			}
			
			public int getValue() {
				return value;
			}
		}

	   private
		        //! Each separator that is created will contain up to this many
		        //! render caches.
		        static int          numRenderCaches = 2;
		    
	private	        SoBoundingBoxCache  bboxCache;     //!< Cache for bounding boxes
	private	        SoGLCacheList       cacheList;     //!< Caches for GL rendering
		    	   
	// Whether to cache during rendering traversal.
	public final SoSFEnum renderCaching = new SoSFEnum();
	
	// Whether to cache during bounding box traversal.
	public final SoSFEnum boundingBoxCaching = new SoSFEnum();
	
	// Whether to cull during rendering traversal.
	public final SoSFEnum renderCulling = new SoSFEnum();
	
	// Whether to cull during picking traversal.
	public final SoSFEnum pickCulling = new SoSFEnum();

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSeparator()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR();

	nodeHeader.SO_NODE_ADD_SFIELD(renderCaching,     "renderCaching",       (CacheEnabled.AUTO.getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(boundingBoxCaching,   "boundingBoxCaching",    (CacheEnabled.AUTO.getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(renderCulling,      "renderCulling",      (CacheEnabled.AUTO.getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(pickCulling,        "pickCulling",      (CacheEnabled.AUTO.getValue()));

    // Set up static info for enum fields
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.ON);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.OFF);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.AUTO);

    // Set up info in enumerated type fields
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCaching, "renderCaching",    "CacheEnabled");
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching" ,"CacheEnabled");
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCulling, "renderCulling",    "CacheEnabled");
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(pickCulling,  "pickCulling"  ,   "CacheEnabled");

    bboxCache   = null;
    cacheList = new SoGLCacheList(numRenderCaches);
    isBuiltIn   = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor taking approximate number of children
//
// Use: public

public SoSeparator(int nChildren){
 super(nChildren);
//
////////////////////////////////////////////////////////////////////////

 nodeHeader.SO_NODE_CONSTRUCTOR();

 nodeHeader.SO_NODE_ADD_SFIELD(renderCaching, "renderCaching",           (CacheEnabled.AUTO.getValue()));
 nodeHeader.SO_NODE_ADD_SFIELD(boundingBoxCaching, "boundingBoxCaching", (CacheEnabled.AUTO.getValue()));
 nodeHeader.SO_NODE_ADD_SFIELD(renderCulling, "renderCulling",           (CacheEnabled.AUTO.getValue()));
 nodeHeader.SO_NODE_ADD_SFIELD(pickCulling,  "pickCulling",              (CacheEnabled.AUTO.getValue()));

    // Set up static info for enum fields
 nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.ON);
 nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.OFF);
 nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(CacheEnabled.AUTO);

    // Set up info in enumerated type fields
 nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCaching,  "renderCaching",   "CacheEnabled");
 nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching","CacheEnabled");
 nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCulling,  "renderCulling" ,  "CacheEnabled");
 nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(pickCulling,   "pickCulling"  ,  "CacheEnabled");

    bboxCache   = null;
    cacheList = new SoGLCacheList(numRenderCaches);
    isBuiltIn   = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor has to free caches
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (bboxCache != null)
        bboxCache.unref();

    cacheList.destructor();
	super.destructor();
}

    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Overrides method in SoNode to return FALSE.
     //
     // Use: public
     
    public boolean
     affectsState()
     //
     ////////////////////////////////////////////////////////////////////////
     {
         return false;
     }
         
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the number of render caches a newly created separator can
//    have.
//
// Use: public, static

public static void
setNumRenderCaches(int howMany)
//
////////////////////////////////////////////////////////////////////////
{
    numRenderCaches = howMany;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the number of render caches newly created separators
//    will have.
//
// Use: public, static

public static int
getNumRenderCaches()
//
////////////////////////////////////////////////////////////////////////
{
    return numRenderCaches;
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Turn off notification on fields to avoid notification when
//    reading, so that caching works properly:
//
// Use: protected

public boolean readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    int i;
    final SoFieldList myFields = new SoFieldList();
    getFields(myFields);
    for (i = 0; i < myFields.getLength(); i++) {
        ((SoField)myFields.operator_square_bracket(i)).enableNotify(false);
    }

    boolean result = super.readInstance(in, flags);

    for (i = 0; i < myFields.getLength(); i++) {
    	((SoField)myFields.operator_square_bracket(i)).enableNotify(true);
    }
    myFields.destructor();
    return result;
}

    
    
    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    This initializes the SoSeparator class.
     //
     // Use: internal
     
   public static void
     initClass()
     //
     ////////////////////////////////////////////////////////////////////////
     {
        SoSubNode.SO__NODE_INIT_CLASS(SoSeparator.class, "Separator", SoGroup.class);
     
         // Enable cache element in those actions that support caching
        //SO_ENABLE(SoGetBoundingBoxAction,   SoCacheElement);
        SoGetBoundingBoxAction.enableElement(SoCacheElement.class);
        //SO_ENABLE(SoGLRenderAction,         SoCacheElement);
        SoGLRenderAction.enableElement(SoCacheElement.class);
        //SO_ENABLE(SoGLRenderAction,         SoGLCacheContextElement);
        SoGLRenderAction.enableElement(SoGLCacheContextElement.class);
    
        // Allow environment var to control caching:
        String NRC;
        if ((NRC = System.getenv("IV_SEPARATOR_MAX_CACHES")) != null) {
            numRenderCaches = Integer.valueOf(NRC);
        }
    }
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Passes on notification after invalidating all caches and
//    recording that notification has passed through a separator
//
// Use: internal

public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    // Destroy all caches, if present
    if (bboxCache != null) {
        bboxCache.unref();
        bboxCache = null;
    }
    cacheList.invalidateAll();

    // Then do the usual stuff
    super.notify(list);
}

    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Implements typical traversal.
     //
     // Use: extender
     
     public void
     SoSeparator_doAction(SoAction action)
     //
     ////////////////////////////////////////////////////////////////////////
     {
         final int[]         numIndices = new int[1];
         final int[][]   indices = new int[1][];
     
         // This differs from SoGroup: if the separator is not on the
         // path, don't bother traversing its children
     
         switch (action.getPathCode(numIndices, indices)) {
     
           case NO_PATH:
           case BELOW_PATH:
             action.getState().push();
             children.traverse(action);
             action.getState().pop();
             break;
     
           case IN_PATH:
             action.getState().push();
             children.traverse(action, 0, indices[0][numIndices[0] - 1]);
             action.getState().pop();
             break;
     
           case OFF_PATH:
             break;
         }
     }
     
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements callback action for separator nodes.
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoSeparator_doAction(action);
}
 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for computing bounding box.  This also takes care of
//    creating the bounding box cache.
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    boolean      canCache = false;
    SoState     state = action.getState();

    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    switch (action.getPathCode(numIndices, indices)) {
      case OFF_PATH:
        // If off the path, don't need to do anything.
        return;

      case NO_PATH:
      case BELOW_PATH:
        canCache = (boundingBoxCaching.getValue() != CacheEnabled.OFF.getValue() &&
                    ! action.isInCameraSpace() &&
                    ! action.isResetPath());
        break;

      case IN_PATH:
        canCache = false;
        break;
    }
    
    // If we have a valid cache already, just use it
    if (canCache && bboxCache != null && bboxCache.isValid(state)) {

        SoCacheElement.addCacheDependency(state, bboxCache);

        action.extendBy(bboxCache.getBox());

        // We want the center to be transformed by the current local
        // transformation, just as if we were a shape node
        if (bboxCache.isCenterSet())
            action.setCenter(bboxCache.getCenter(), true);

        // If our cache has lines or points, set the flag in any open
        // caches above us
        if (bboxCache.hasLinesOrPoints())
            SoBoundingBoxCache.setHasLinesOrPoints(state);
    }

    // If we can't cache, just do what group does, with push/pop added
    else if (! canCache) {
        state.push();
        super.getBoundingBox(action);
        state.pop();
    }

    // Otherwise, we have to do some extra work
    else {
        // Save the current bounding box from the action and empty it.
        // (We can assume the center has not been set, or else some
        // group is not doing its job.)
        final SbXfBox3f       savedBBox = new SbXfBox3f(action.getXfBoundingBox());
        action.getXfBoundingBox().makeEmpty();

        state.push();

        // Set the local bbox matrix to identity, so shapes' bounding
        // boxes will be transformed into our local space
        SoLocalBBoxMatrixElement.makeIdentity(state);

        // Build cache. We've already tested for a valid cache, so the
        // only other possibility is for a NULL cache or an invalid one
        if (bboxCache != null)
            bboxCache.unref();

        // Create a new cache:
        bboxCache = new SoBoundingBoxCache(state);
        bboxCache.ref();
        SoCacheElement.set(state, bboxCache);

        // Traverse the kids
        super.getBoundingBox(action);

        // This has to be done before the extendBy
        state.pop();

        // Save the bounding box around our kids and save the center
        final SbXfBox3f       kidsBBox      = new SbXfBox3f(action.getXfBoundingBox());
        final SbVec3f         kidsCenter    = new SbVec3f(action.getCenter());
        boolean          kidsCenterSet = action.isCenterSet();

        // Store it in the cache
        // Note: bboxCache might be NULL if notification happened
        // during traversal.
        if (bboxCache != null)
            bboxCache.set(kidsBBox, kidsCenterSet, kidsCenter);
//#ifdef DEBUG
//        else {
//            SoDebugError::post("SoSeparator::getBoundingBox",
//                               "Bbox cache was destroyed during "
//                               "traversal, meaning a change was "
//                               "made to the scene during a "
//                               "getBoundingBox action.  If you "
//                               "must change the scene during an "
//                               "action traversal, you should disable "
//                               "notification first using methods "
//                               "on SoFieldContainer or SoField.");
//        }
//#endif
        
        // If the bounding box was reset by one of our children, we
        // don't want to restore the previous bounding box. Instead,
        // we just set it to the children's bounding box so that the
        // current local transformation is multiplied in. Otherwise,
        // we have to restore the previous bounding box and extend it
        // by the children's bounding box.
        if (action.isResetPath() &&
            (action.getWhatReset().getValue() & SoGetBoundingBoxAction.ResetType.BBOX.getValue()) != 0 &&
            action.getResetPath().containsNode(this))
            action.getXfBoundingBox().makeEmpty();
        else
            action.getXfBoundingBox().copyFrom(savedBBox);

        // Extend the bounding box by the one our kids returned -
        // this will multiply in the current local transformation
        action.extendBy(kidsBBox);
        
        // Set the center to be the computed center of our kids,
        // transformed by the current local transformation
        if (kidsCenterSet) {
            action.resetCenter();
            action.setCenter(kidsCenter, true);
        }
    }
}

 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements getMatrix action.
//
// Use: extender

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    // Only need to compute matrix if separator is a node in middle of
    // current path chain. We don't need to push or pop the state,
    // since this shouldn't have any effect on other nodes being
    // traversed.

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH) {
        // need to push/pop to handle SoUnitsElement correctly
        action.getState().push();
        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
        action.getState().pop();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Traversal for rendering.  This is different from generic
//    traversal because we may need to do caching.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[] numIndices = new int[1];
    final int[][] indices = new int[1][];
    SoAction.PathCode pc = action.getPathCode(numIndices,indices);
    if (pc == SoAction.PathCode.NO_PATH || pc == SoAction.PathCode.BELOW_PATH)
        GLRenderBelowPath(action);
    else if (pc == SoAction.PathCode.IN_PATH)
        GLRenderInPath(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Renders all children.  Also does caching and culling.
//
// Use: extender

public void
GLRenderBelowPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    // Do a cull test, if culling is turned on:
    int savedCullBits = -1;

    // For now, just do culling if turned ON explicitly.  Eventually,
    // we might want to change this to:
    //  (cullFlag == AUTO && !state.isCacheOpen()) || (cullFlag == ON)
    boolean doCullTest = (renderCulling.getValue() == CacheEnabled.ON.getValue());

    if (doCullTest) {
        final int[] cullBits = new int[1]; 
        cullBits[0] = savedCullBits = action.getCullTestResults();

        if (cullBits[0] != 0) {
//#ifdef DEBUG
//            static int printCullInfo = -1;
//            if (printCullInfo == -1)
//                printCullInfo =
//                    SoDebug::GetEnv("IV_DEBUG_RENDER_CULL") != NULL;
//            if (printCullInfo) {
//                if (getName().getLength() != 0)
//                    SoDebug::RTPrintf("Separator named %s",
//                                      getName().getString());
//                else
//                    SoDebug::RTPrintf("Separator 0x%x", this);
//            }
//#endif
            if (cullTest(action, cullBits)) {
//#ifdef DEBUG
//                if (printCullInfo)
//                    SoDebug::RTPrintf("  render culled\n");
//#endif
                // Don't cache above if doing culling:
                SoGLCacheContextElement.shouldAutoCache(state,
                    SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
                return;
            }
//#ifdef DEBUG
//            if (printCullInfo)
//                printf(" render cull results: %c%c%c\n",
//                       cullBits&1 ? 'S' : 'i',
//                       cullBits&2 ? 'S' : 'i',
//                       cullBits&4 ? 'S' : 'i');
//#endif
            action.setCullTestResults(cullBits[0]);
        }
    }

    boolean canCallCache = (renderCaching.getValue() != CacheEnabled.OFF.getValue());
    boolean canBuildCache = (canCallCache  && ! state.isCacheOpen());

    state.push();

    // if we can't call a cache:
    if (canCallCache && cacheList.call(action)) {
        // Just pop the state
        state.pop();
    } else {
        if (canBuildCache) {
            // Let the cacheList open a new cache, if it can.  This
            // HAS to come after push() so that the cache element can
            // be set correctly.
            cacheList.open(action, renderCaching.getValue() == CacheEnabled.AUTO.getValue());
        }

        action.pushCurPath();
        final int numKids = children.getLength();
        for (int i = 0; i < numKids && !action.hasTerminated(); i++) {
            action.popPushCurPath(i);
            if (! action.abortNow())
                ((SoNode )children.get(i)).GLRenderBelowPath(action);
            else
                SoCacheElement.invalidate(action.getState());
        }
        action.popCurPath();
        state.pop();
        if (canBuildCache) {
            // Let the cacheList close the cache, if it decided to
            // open one.  This HAS to come after the pop() so that any
            // GL commands executed by pop() are part of the display
            // list.
            cacheList.close(action);
        }
    }
    // Reset cull bits, if did a cull test:
    if (doCullTest) {
        action.setCullTestResults(savedCullBits);

        // Don't cache above if doing culling:
        SoGLCacheContextElement.shouldAutoCache(state,
                SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the GL render action
//
// Use: extender

public void
GLRenderInPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    final int[] numIndices = new int[1];
    final int[][] indices = new int[1][];
    SoAction.PathCode pc = action.getPathCode(numIndices, indices);
    if (pc == SoAction.PathCode.IN_PATH) { // still rendering in path:
        SoState state = action.getState();
        state.push();
        int whichChild = 0;
        for (int i = 0; i < numIndices[0] && !action.hasTerminated(); i++) {
            while (whichChild < indices[0][i] && !action.hasTerminated()) {
                SoNode kid = (SoNode )children.get(whichChild);
                if (kid.affectsState()) {
                    action.pushCurPath(whichChild);
                    if (! action.abortNow())
                        kid.GLRenderOffPath(action);
                    else
                        SoCacheElement.invalidate(action.getState());
                    action.popCurPath(pc);
                }
                ++whichChild;
            }
            action.pushCurPath(whichChild);
            if (action.abortNow())
                SoCacheElement.invalidate(action.getState());
            else
                ((SoNode )children.get(whichChild)).GLRenderInPath(action);
            action.popCurPath(pc);
            ++whichChild;
        }
        state.pop();
    } else if (pc == SoAction.PathCode.BELOW_PATH) { // This must be tail node
        GLRenderBelowPath(action);
    } else { // This should NEVER happen:
//#ifdef DEBUG
//        SoDebugError::post("SoSeparator::GLRenderInPath",
//                           "PathCode went to NO_PATH or OFF_PATH!");
//#endif
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the GL render action
//
// Use: extender

public void
GLRenderOffPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
//    SoDebugError::post("SoSeparator::GLRenderOffPath",
//                           "affectsState() is FALSE");
//#endif
}


 
     
     ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Implements handle event action for separator nodes.
     //
     // Use: extender
     
    public void
     handleEvent(SoHandleEventAction action)
     //
     ////////////////////////////////////////////////////////////////////////
     {
         SoSeparator_doAction(action);
     }
     
     

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figure out if this separator is culled or not.  Returns TRUE if
//    the separator is culled.
//
// Use: extender

    private static SoGetBoundingBoxAction bba = null;
    
public boolean
cullTest(SoGLRenderAction action, final int[] cullBits)
//
////////////////////////////////////////////////////////////////////////
{
    // Don't bother if bbox caching is turned off:
    if (boundingBoxCaching.getValue() == CacheEnabled.OFF.getValue()) return false;

    SoState state = action.getState();
    
    // Next, get our bounding box.  We do this in a way that is a
    // little dangerous and hacky-- we use the state from the
    // renderAction and pass to bounding box cache's isValid.  This
    // assumes that the state for the bounding box and render action
    // can be compared, which happens to be true (all elements needed
    // for getBoundingBox are also needed for glRender).

    if (bboxCache == null || !bboxCache.isValid(state)) {
        if (bba == null) 
            bba = new SoGetBoundingBoxAction(
                SoViewportRegionElement.get(state));
        else
            bba.setViewportRegion(SoViewportRegionElement.get(state));

        bba.apply((SoPath )action.getCurPath());
    }

    if (bboxCache == null) return false;

    final SbBox3f bbox = bboxCache.getProjectedBox();

    final SbMatrix cullMatrix =
        SoModelMatrixElement.getCombinedCullMatrix(state);

    return bbox.outside(cullMatrix, cullBits);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements ray picking.
//
// Use: extender

public void
rayPick(SoRayPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]                 numIndices = new int[1];
    final int[][]           indices = new int[1][];

    // Bail out if there is nothing to traverse...
    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.OFF_PATH)
        return;
    
    // Note: even if we are only traversing some of our children we
    // can still use the bounding box cache  computed for all of our
    // children for culling, because the bounding box for all of our
    // children is guaranteed to be at least as big as the bounding
    // box for some of them.

    // The action may not have yet computed a world space ray. (This
    // will be true if the pick was specified as a window-space point
    // and no camera has yet been traversed. This could happen if
    // there is no camera or the camera appears under this separator.)
    // In this case, don't bother trying to intersect the ray with a
    // cached bounding box, sine there ain't no ray!
    if (action.isCullingEnabled() && pickCulling.getValue() != CacheEnabled.OFF.getValue() &&
        action.hasWorldSpaceRay()) {

        // If we don't have a valid cache, try to build one by
        // applying an SoGetBoundingBoxAction to the current path to
        // the separator. (Testing if the cache is invalid uses the
        // state of the pick action; this assumes that any element
        // that is valid in a bounding box action will also be valid
        // in a pick action.)
        if (bboxCache == null || ! bboxCache.isValid(action.getState())) {
            final SoGetBoundingBoxAction      ba = new SoGetBoundingBoxAction(action.getViewportRegion());
            ba.apply((SoPath ) action.getCurPath());
        }

        // It's conceivable somehow that the cache was not built, so
        // check it again
        if (bboxCache != null) {

            // Test the bounding box in the cache for intersection
            // with the pick ray. If none, traverse no further.
            // If there are no lines or points in the cache, we can
            // use a faster intersection test: intersect the picking
            // ray with the bounding box. Otherwise, we have to use
            // the picking view volume to make sure we pick near lines
            // and points
            action.setObjectSpace();

//#ifdef DEBUG
//            static int printCullInfo = -1;
//            if (printCullInfo == -1)
//                printCullInfo =
//                    SoDebug::GetEnv("IV_DEBUG_PICK_CULL") != NULL;
//            if (printCullInfo) {
//                if (getName().getLength() != 0)
//                    SoDebug::RTPrintf("Separator named %s",
//                                      getName().getString());
//                else
//                    SoDebug::RTPrintf("Separator 0x%x", this);
//            }
//#endif
            if (! action.intersect(bboxCache.getBox().project(),
                                    bboxCache.hasLinesOrPoints())) {
//#ifdef DEBUG
//                if (printCullInfo)
//                    SoDebug::RTPrintf("  pick culled\n");
//#endif
                return;
            }
//#ifdef DEBUG
//            else if (printCullInfo) {
//                SoDebug::RTPrintf("  pick cull test passed\n");
//            }
//#endif
        }
    }

    // If we got here, we're supposed to traverse our children
    action.getState().push();
    super.rayPick(action);
    action.getState().pop();
}

     
      
////////////////////////////////////////////////////////////////////////
 //
 // Description:
 //    Implements search action for separator nodes. This determines if
 //    the separator should be searched. If so, this calls the search
 //    method for SoGroup to do the work.
 //
 // Use: extender
 
 public void
 search(SoSearchAction action)
 //
 ////////////////////////////////////////////////////////////////////////
 {
     boolean      doSearch = true;
 
     // See if we're supposed to search only if the stuff under the
     // separator is relevant to the search path
 
     if (! action.isSearchingAll()) {
         final int[]             numIndices = new int[1];
         final int[][]       indices = new int[1][];
 
         // Search through this separator node only if not searching along
         // a path or this node is on the path
         if (action.getPathCode(numIndices, indices) == SoAction.PathCode.OFF_PATH)
             doSearch = false;
     }
 
     if (doSearch) {
         action.getState().push();
         super.search(action);
         action.getState().pop();
     }
 }
 
 
}
