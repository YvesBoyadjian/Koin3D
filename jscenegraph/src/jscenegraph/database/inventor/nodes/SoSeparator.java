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

/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoSeparator SoSeparator.h Inventor/nodes/SoSeparator.h
  \brief The SoSeparator class is a state-preserving group node.

  \ingroup nodes

  Subgraphs parented by SoSeparator nodes will not affect the state,
  as they push and pop the traversal state before and after traversal
  of its children.

  SoSeparator nodes also provides options for traversal optimization
  through the use of caching.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    Separator {
        renderCaching AUTO
        boundingBoxCaching AUTO
        renderCulling AUTO
        pickCulling AUTO
    }
  \endcode

  \sa SoTransformSeparator
*/

// *************************************************************************


// *************************************************************************

/*!
  \enum SoSeparator::CacheEnabled

  Enumeration of flags for the fields deciding which optimizations
  to do in SoSeparator nodes. There are two types of settings
  available: caching policies or culling policies. See documentation of
  fields.
*/
/*!
  \var SoSeparator::CacheEnabled SoSeparator::OFF
  No caching.
*/
/*!
  \var SoSeparator::CacheEnabled SoSeparator::ON
  Always try to cache state.
*/
/*!
  \var SoSeparator::CacheEnabled SoSeparator::AUTO
  Use heuristics to try to figure out the optimal caching policy.
*/


/*!
  \var SoSFEnum SoSeparator::renderCaching

  Policy for caching of rendering instructions for faster
  execution. This will typically use the OpenGL \e displaylist
  mechanism.

  Default value is SoSeparator::AUTO.

  If you know that some parts of your scene will never change,
  rendering might happen faster if you explicitly set this field to
  SoSeparator::ON. If you on the other hand know that parts of the
  scene will change a lot (like for every redraw), it will be
  beneficial to set this field to SoSeparator::OFF for the top-level
  separator node of this (sub)graph.

  Usually the default setting of \c AUTO will handle any scene very
  well. The advantages that \e can be had from setting
  SoSeparator::renderCaching to \c ON are:

  <ul>
   <li> If you positively know that the geometry under the SoSeparator is
     static, you get the cache set up right away.

     Otherwise, the code in Coin will do a bit of testing and decide
     by some heuristics whether or not to enable it. That will make
     the rendering be a tiny bit slower right after startup than with
     renderCaching set to \c ON.

     (The slow-down should hardly be noticeable, though, so we don't
     advice application programmers to do this.)
   </li>

   <li> For many of the shape nodes that can contain many basic
     primitives, like e.g. SoFaceSet, SoIndexedFaceSet, SoLineSet, etc
     etc, there is an internal threshold for how many primitives a
     node can contain before we don't do caching when
     SoSeparator::renderCaching is set to \c AUTO.

     The reason we do this is because OpenGL render lists can
     potentially suck up a lot of memory resources on the graphics
     card.

     But if you know that it will be advantageous on your particular
     platform, you can override this by setting
     SoSeparator::renderCaching equal to \c ON.

     (We don't advice application programmers to do this either. A
     better solution in these cases would simply be to get in touch
     with SIM and describe the platform and the problem, and we could
     integrate a proper fix into Coin.)
   </li>
  </ul>

  There are good reasons for setting renderCaching to \c OFF, like
  when you know the geometry will be changing a lot. Still, Coin
  should work fairly well even without this optimization.  (If
  renderCaching is \c AUTO over a sub-graph with changing geometry or
  other cache smashing nodes, the caching heuristics will stop the
  SoSeparator node from trying to make caches -- at least after a few
  tries have been made and failed.)


  The short story about how auto-caching works is as follows:

  <ul>

    <li>For vertex-based shapes with fewer than 100 triangles and
        where the geometry is detected to be fairly static, caching is
        enabled.</li>

    <li>For shapes with more than 1000 triangles, it is disabled, to
        avoid spending too much of the on-board graphics card's memory
        resources.</li>

    <li>For shapes with between 100 and 1000 shapes, display list
        caching will be turned on if our heuristics decides that the
        geometry can be considered static.</li>

  </ul>

  The maximum threshold (of 1000) is higher when doing remote
  rendering (as when rendering from one X11-based system to another).

  Disabling the display list caching takes precedence over enabling, so
  if you have an SoSeparator with a shape with more than 1000
  triangles and a shape with fewer than 100 triangles, caching will be
  disabled for the SoSeparator.

  It's possible to tune the limits using some environment variables:

  <ul>

    <li>\c COIN_AUTOCACHE_LOCAL_MIN can be used to change the
        enable-caching limit, while \c COIN_AUTOCACHE_LOCAL_MAX
        controls the disable-caching limit.</li>

    <li>The corresponding variables for remote rendering are \c
        COIN_AUTOCACHE_REMOTE_MIN and \c
        COIN_AUTOCACHE_REMOTE_MAX.</li>

  </ul>
*/

/*!
  \var SoSFEnum SoSeparator::boundingBoxCaching

  Policy for caching bounding box calculations. Default value is
  SoSeparator::AUTO.

  See also documentation for SoSeparator::renderCaching.
*/
/*!
  \var SoSFEnum SoSeparator::renderCulling

  Policy for doing viewport culling during rendering
  traversals. Default value is SoSeparator::AUTO.

  When the render culling is turned off for Coin, it will be left to
  be done for the underlying immediate mode rendering library. This
  will often be faster than doing culling from within Coin, so be
  careful to monitor the change in execution speed if setting this
  field to SoSeparator::ON.

  See also documentation for SoSeparator::renderCaching.
*/
/*!
  \var SoSFEnum SoSeparator::pickCulling

  Policy for doing viewport culling during pick traversals. Default
  value is SoSeparator::AUTO.

  See documentation for SoSeparator::renderCulling.
*/

// *************************************************************************

package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.glue.Gl;
import jscenegraph.coin3d.inventor.annex.profiler.SoNodeProfiling;
import jscenegraph.coin3d.misc.SoGL;
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
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
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
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.misc.SoTempPath;
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
    if (bboxCache != null) {
        bboxCache.unref();
        bboxCache = null;
    }

    if (cacheList != null) {
        cacheList.destructor();
        cacheList = null;
    }
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
         

// Doc from superclass.
public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  SoSeparator_doAction((SoAction)action);
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the number of render caches a newly created separator can
//    have.
//
// Use: public, static
/*!
  Set the maximum number of caches that SoSeparator nodes may allocate
  for render caching.

  This is a global value which will be used for all SoSeparator nodes,
  but the value indicate the maximum number \e per SoSeparator node.

  More caches might give better performance, but will use more memory.
  The built-in default value is 2.

  The value can also be changed globally by setting the host system's
  environment variable IV_SEPARATOR_MAX_CACHES to the wanted
  number. This is primarily meant as an aid during debugging, to make
  it easy to turn off render caching completely (by setting
  "IV_SEPARATOR_MAX_CACHES=0") without having to change any
  application code.
*/

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
    if (cacheList != null) { // Can happen if called from base destructor
        cacheList.invalidateAll();
    }

    // Then do the usual stuff
    super.notify(list);
}

    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Implements typical traversal.
     //
     // Use: extender
     
public void doAction(SoAction action) {
	SoSeparator_doAction(action);
}
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

//public void
//GLRender(SoGLRenderAction action)
////
//////////////////////////////////////////////////////////////////////////
//{
//    final int[] numIndices = new int[1];
//    final int[][] indices = new int[1][];
//    SoAction.PathCode pc = action.getPathCode(numIndices,indices);
//    if (pc == SoAction.PathCode.NO_PATH || pc == SoAction.PathCode.BELOW_PATH)
//        GLRenderBelowPath(action);
//    else if (pc == SoAction.PathCode.IN_PATH)
//        GLRenderInPath(action);
//}

// Doc from superclass.
public void
GLRender(SoGLRenderAction action)
{
  switch (action.getCurPathCode()) {
  case NO_PATH:
  case BELOW_PATH:
    this.GLRenderBelowPath(action);
    break;
  case OFF_PATH:
    // do nothing. Separator will reset state.
    break;
  case IN_PATH:
    this.GLRenderInPath(action);
    break;
  }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Renders all children.  Also does caching and culling.
//
// Use: extender
static boolean chkglerr = SoGL.sogl_glerror_debugging();

/*!
  SGI Open Inventor v2.1 obsoleted support for
  SoGLRenderAction::addMethod().  Instead, GLRender() might be called
  directly, and to optimize traversal, the SoSeparator node calls
  GLRenderBelowPath whenever the path code is BELOW_PATH or NO_PATH
  (path code is guaranteed not to change). To be compatible with SGI's
  Inventor (and thereby also TGS') we have chosen to follow their
  implementation in this respect.

  SoSeparator::GLRenderBelowPath() do not traverse its children using
  SoChildList::traverse(), but calls GLRenderBelowPath() directly
  for all its children.
*/
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

        int n = this.children.getLength();
        Object[] childarray = (n!=0)? this.children.getArrayPtr() : null;
        action.pushCurPath();
        final int numKids = children.getLength();
        for (int i = 0; i < numKids && !action.hasTerminated(); i++) {
            action.popPushCurPath(i, (SoNode)childarray[i]);
            if (! action.abortNow())
                ((SoNode )children.get(i)).GLRenderBelowPath(action);
            else
                SoCacheElement.invalidate(action.getState());
            
//#if COIN_DEBUG
      // The GL error test is default disabled for this optimized
      // path.  If you get a GL error reporting an error in the
      // Separator node, enable this code by setting the environment
      // variable COIN_GLERROR_DEBUGGING to "1" to see exactly which
      // node caused the error.
      if (chkglerr) {
        final String[] str = new String[1];
        int errs = Gl.coin_catch_gl_errors(str);
        if (errs > 0) {
          SoDebugError.post("SoSeparator::GLRenderBelowPath",
                             "GL error: '"+str[0]+"', nodetype: "+(this.children).operator_square_bracket(i).getTypeId().getName().getString());
        }
        //cc_string_clean(&str);
      }
//#endif // COIN_DEBUG
            
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

//public void
//GLRenderInPath(SoGLRenderAction action)
//
//////////////////////////////////////////////////////////////////////////
//{
//    final int[] numIndices = new int[1];
//    final int[][] indices = new int[1][];
//    SoAction.PathCode pc = action.getPathCode(numIndices, indices);
//    if (pc == SoAction.PathCode.IN_PATH) { // still rendering in path:
//        SoState state = action.getState();
//        state.push();
//        int whichChild = 0;
//        for (int i = 0; i < numIndices[0] && !action.hasTerminated(); i++) {
//            while (whichChild < indices[0][i] && !action.hasTerminated()) {
//                SoNode kid = (SoNode )children.get(whichChild);
//                if (kid.affectsState()) {
//                    action.pushCurPath(whichChild);
//                    if (! action.abortNow())
//                        kid.GLRenderOffPath(action);
//                    else
//                        SoCacheElement.invalidate(action.getState());
//                    action.popCurPath(pc);
//                }
//                ++whichChild;
//            }
//            action.pushCurPath(whichChild);
//            if (action.abortNow())
//                SoCacheElement.invalidate(action.getState());
//            else
//                ((SoNode )children.get(whichChild)).GLRenderInPath(action);
//            action.popCurPath(pc);
//            ++whichChild;
//        }
//        state.pop();
//    } else if (pc == SoAction.PathCode.BELOW_PATH) { // This must be tail node
//        GLRenderBelowPath(action);
//    } else { // This should NEVER happen:
////#ifdef DEBUG
////        SoDebugError::post("SoSeparator::GLRenderInPath",
////                           "PathCode went to NO_PATH or OFF_PATH!");
////#endif
//    }
//}
public void
GLRenderInPath(SoGLRenderAction action)
{
  final int[] numindices = new int[1];
  final int[][] indices = new int[1][];

  SoAction.PathCode pathcode = action.getPathCode(numindices, indices);

  if (pathcode == SoAction.PathCode.IN_PATH) {
    SoState state = action.getState();
    Object[] childarray = (Object[]) this.children.getArrayPtr();
    state.push();
    int childidx = 0;
    for (int i = 0; i < numindices[0]; i++) {
      for (; childidx < indices[0][i] && !action.hasTerminated(); childidx++) {
        SoNode offpath = (SoNode)childarray[childidx];
        if (offpath.affectsState()) {
          action.pushCurPath(childidx, offpath);
          if (!action.abortNow()) {
            final SoNodeProfiling profiling = new SoNodeProfiling();
            profiling.preTraversal(action);
            offpath.GLRenderOffPath(action); // traversal call
            profiling.postTraversal(action);
          }
          else {
            SoCacheElement.invalidate(state);
          }
          action.popCurPath(pathcode);
        }
      }
      SoNode inpath = (SoNode)childarray[childidx];
      action.pushCurPath(childidx, inpath);
      if (!action.abortNow()) {
        final SoNodeProfiling profiling = new SoNodeProfiling();
        profiling.preTraversal(action);
        inpath.GLRenderInPath(action); // traversal call
        profiling.postTraversal(action);
      }
      else {
        SoCacheElement.invalidate(state);
      }
      action.popCurPath(pathcode);
      childidx++;
    }
    state.pop();
  }
  else if (pathcode == SoAction.PathCode.BELOW_PATH) {
    this.GLRenderBelowPath(action);
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

    
    /*!
    This is an internal Open Inventor method. We've implemented
    view frustum culling in a different manner. Let us know if
    you need this function, and we'll consider implementing it.
  */
//  public boolean
//  cullTest(SoGLRenderAction action, final int[] cullresults)
//  {
//    //COIN_OBSOLETED();
//    SoDebugError.post("SoSeparator::cullTest(SoGLRenderAction action, final int[] cullresults)", 
//            "OBSOLETED: functionality not supported (any more)"); 
//    return false;
//  }

    
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

        SoPath dummy = action.getCurPath().copy();
        dummy.ref();
        bba.apply(dummy);
        dummy.unref();
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
