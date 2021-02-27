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
  \class SoLevelOfDetail SoLevelOfDetail.h Inventor/nodes/SoLevelOfDetail.h
  \brief The SoLevelOfDetail class is used to choose a child based on projected size.
  \ingroup nodes

  A level-of-detail mechanism is typically used by application
  programmers to assist the library in speeding up the rendering.

  The way a level-of-detail mechanism works is basically like this:

  Several versions of varying complexity of \e the \e same geometry /
  shape is provided by the application programmer in sorted order from
  "most complex" to "least complex" (where "complex" in this context
  should be taken to mean more or less detailed in the number of
  polygons / shapes used for rendering it).

  The run-time rendering system then, upon scenegraph traversal, will
  on-the-fly calculate either the distance from the camera to the
  3D-model in question, or the number of pixels in the screen
  projection of the 3D-model. This value is then used to decide which
  version of the model to actually render: as the model is moved
  farther away from the camera, a less detailed version of the model
  is used. And vice versa, as the model moves closer to the camera,
  more and more detailed versions of it are rendered.

  This is under many different circumstances a very effective way to
  let the application programmer assist to \e profoundly optimize the
  rendering of her 3D-scene.

  There is of course a trade-off with the level-of-detail technique:
  more versions of the same 3D model means the scenegraph will use up
  more application memory resources. Also, generating the set of less
  and less detailed versions of a 3D model from the original is often
  not a trivial task to do properly. The process is often assisted by
  software like what Kongsberg Oil & Gas Technologies offers in their
  <a href="http://www.rational-reducer.com>Rational Reducer</a> package.


  The SoLevelOfDetail node implements the "projected size" variety
  level-of-detail technique (as opposed to the "object distance"
  technique, as done by the SoLOD node).

  The node works by comparing the current projected size of the
  smallest rectangle covering the bounding box of it's child geometry.
  

  Along with this set of models of the same shape, a specification of
  when to switch between them is also provided.

  Example scenegraph layout:

  \code
  LevelOfDetail {
     screenArea [ 2000, 500, 50 ]

     DEF version-0 Separator {
       # most complex / detailed / heavy version of subgraph
     }
     DEF version-1 Separator {
       # less complex version of subgraph
     }
     DEF version-2 Separator {
       # even less complex version of subgraph
     }
     DEF version-3 Separator {
       # simplest / "lightest" version of subgraph
     }
  }
  \endcode

  The way the above sub-scenegraph would work would be the following:
  if the rectangular area around the model's projected bounding box
  covers \e more than 2000 pixels (meaning it will be up pretty close
  to the camera), the most complex version of the model (\c version-0)
  would be traversed (and rendered, of course). If the projected area
  would be \e between 500 and 2000 pixels, the \c version-1 model
  would be used. Ditto if the projected area was between 50 and 500
  pixels, the \c version-2 version of the model would be
  used. Finally, if the projected bounding box area would be \e less
  than 50 square pixels, the presumably least detailed version of the
  modeled would be used.

  (A common "trick" is to let the last of the SoLevelOfDetail node's
  children be just an empty subgraph, so no geometry will be rendered
  at all if the model is sufficiently far away. This will of course
  have a positive effect on the total rendering time for the complete
  scenegraph.)

  Note that the SoLevelOfDetail::screenArea vector will be influenced
  by preceding SoComplexity nodes in the following way: if
  SoComplexity::value is set from 0.0 up to 0.5, lower detail levels
  than normal will be selected for traversal. If SoComplexity::value
  is above 0.5, higher level details than normal will be used. An
  SoComplexity::value equal to 1.0 will cause the first child of
  SoLevelOfDetail to always be used.


  As mentioned above, there is one other level-of-detail node in the
  Coin library: SoLOD. The difference between that one and this is
  just that instead of projected bounding box area, SoLOD uses the
  distance between the camera and the object to find out when to
  switch between the different model versions.

  Using SoLOD is faster, since figuring out the projected bounding box
  area needs a certain amount of calculations. But using
  SoLevelOfDetail is often "better", in the sense that it's really a
  model's size and visibility in the viewport that determines whether
  we could switch to a less complex version without losing enough
  detail that it gives a noticable visual degradation.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    LevelOfDetail {
        screenArea 0
    }
  \endcode

  \sa SoLOD
*/

// *************************************************************************

package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.actions.SoAudioRenderAction;
import jscenegraph.coin3d.inventor.threads.SbMutex;
import jscenegraph.coin3d.inventor.threads.SbStorage;
import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoLocalBBoxMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoLevelOfDetail extends SoGroup {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLOD.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoLevelOfDetail.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoLevelOfDetail.class); }    	  		
	  

	// *************************************************************************

	/*!
	  \var SoMFFloat SoLevelOfDetail::screenArea

	  The screen area limits for the children. See usage example in main
	  class documentation of SoLevelOfDetail for an explanation of how
	  this vector should be set up correctly.

	  By default this vector just contains a single value 0.0f.
	*/

	// *************************************************************************

	  public final SoMFFloat screenArea = new SoMFFloat();
	  
	// *************************************************************************

	  class so_lod_static_data {
	    SoGetBoundingBoxAction bboxaction; //ptr
	  } ;

	  static void
	  so_lod_construct_data(Object closure)
	  {
	    so_lod_static_data data = (so_lod_static_data) closure;
	    data.bboxaction = null;
	  }

	  static void
	  so_lod_destruct_data(Object closure)
	  {
	    so_lod_static_data data = (so_lod_static_data) closure;
	    Destroyable.delete(data.bboxaction);
	  }

	  static SbStorage so_lod_storage = null; // ptr


static SoGetBoundingBoxAction 
so_lod_get_bbox_action()
{
  so_lod_static_data data = null;
  data = (so_lod_static_data) so_lod_storage.get();
  
  if (data.bboxaction == null) {
    // The viewport region will be replaced every time the action is
    // used, so we can just feed it a dummy here.
    data.bboxaction = new SoGetBoundingBoxAction(new SbViewportRegion());
  }
  return data.bboxaction;
}

private static class SoLevelOfDetailP implements Destroyable {
public  SoBoundingBoxCache bboxcache; //ptr
//#ifdef COIN_THREADSAFE
  // FIXME: a mutex for every instance seems a bit excessive,
  // especially since MSWindows might have rather strict limits on the
  // total amount of mutex resources a process (or even a user) can
  // allocate. so consider making this a class-wide instance instead.
  // -mortene.
  final SbMutex mutex = new SbMutex();
//#endif // COIN_THREADSAFE

  void lock() {
//#ifdef COIN_THREADSAFE
    this.mutex.lock();
//#endif // COIN_THREADSAFE 
 }
  void unlock() {
//#ifdef COIN_THREADSAFE
    this.mutex.unlock();
//#endif // COIN_THREADSAFE
  }
@Override
public void destructor() {
	mutex.destructor();
}
};

private SoLevelOfDetailP pimpl; //ptr

/*!
  Default constructor.
*/
public SoLevelOfDetail()
{
  this.commonConstructor();
}

/*!
  Constructor.

  The argument should be the approximate number of children which is
  expected to be inserted below this node. The number need not be
  exact, as it is only used as a hint for better memory resource
  allocation.
*/
public SoLevelOfDetail(int numchildren) {
  super(numchildren);

  this.commonConstructor();
}

// private
private void
commonConstructor()
{
  pimpl = new SoLevelOfDetailP();
  pimpl.bboxcache = null;

  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoLevelOfDetail.class);

  nodeHeader.SO_NODE_ADD_MFIELD(screenArea,"screenArea", (0f));
}

/*!
  Destructor.
*/
public void destructor()
{
  if (pimpl.bboxcache != null) pimpl.bboxcache.unref();
  Destroyable.delete( pimpl);
}


// Documented in superclass.
public void
doAction(SoAction action)
{
  switch (action.getCurPathCode()) {
  case IN_PATH:
    super.doAction(action); // normal path traversal
    return;
  case OFF_PATH:
    return; // this is a separator node, return.
  case BELOW_PATH:
  case NO_PATH:
    break; // go on
  default:
    //assert(0 && "unknown path code");
    return;
  }

  // for some strange reason, gcc (egcs-2.91.66) won't accept the code
  // below inside a case (yes, I did use brackets).
  // That's the reason for the strange switch/case above. pederb 19991116

  SoState state = action.getState();
  int n = this.getNumChildren();
  if (n == 0) return;

  final SbVec2s size = new SbVec2s();
  final SbBox3f bbox = new SbBox3f();
  int i;
  int idx = -1;
  float projarea = 0.0f;

  SoComplexityTypeElement.Type complext = SoComplexityTypeElement.get(state);
  float complexity = SbBasic.SbClamp(SoComplexityElement.get(state), 0.0f, 1.0f);
  
  do {

  if (n == 1) { idx = 0; break; }
  if (complext == SoComplexityTypeElement.Type.BOUNDING_BOX) { idx = n - 1; break; }
  if (complexity == 0.0f) { idx = n - 1; break; }
  if (complexity == 1.0f) { idx = 0; break; }
  if (this.screenArea.getNum() == 0) { idx = 0; break; }

  if ( pimpl.bboxcache == null || !pimpl.bboxcache.isValid(state)) {
    SoGetBoundingBoxAction bboxAction = so_lod_get_bbox_action();

    bboxAction.setViewportRegion(SoViewportRegionElement.get(state));
    // need to apply on the current path, not on the node, since we
    // might need coordinates from the state. Also, we need to set the
    // reset path so that we get the local bounding box for the nodes
    // below this node.
    bboxAction.setResetPath(action.getCurPath());
    bboxAction.apply((SoPath) action.getCurPath()); // find bbox of all children
    bbox.copyFrom( bboxAction.getBoundingBox());
  }
  else {
    bbox.copyFrom( pimpl.bboxcache.getProjectedBox());
  }
  SoShape.getScreenSize(state, bbox, size);

  // The multiplication factor from the complexity setting is
  // complexity+0.5 because the documented behavior of SoLevelOfDetail
  // is to show lower detail levels than normal when
  // SoComplexity::value < 0.5, and to show higher detail levels when
  // SoComplexity::value > 0.5.
  projarea = (float)(size.getValue()[0]) * (float)(size.getValue()[1]) * (complexity + 0.5f);

  // In case there are too few or too many screenArea values.
  n = Math.min(n, this.screenArea.getNum());

  for (i = 0; i < n; i++) {
    if (projarea > this.screenArea.operator_square_bracket(i)) { idx = i; break; }
  }
  if( i < n ) { // YB java port
	  break;
  }

  // If we get here, projected area was lower than any of the
  // screenArea value, so the last child should be traversed.
  idx = this.getNumChildren() - 1;
  // (fall through to traverse:)

  } while(false);
 traverse:
  this.getChildren().traverse(action, idx);
  return;
}

// Documented in superclass.
public void
callback(SoCallbackAction action)
{
  doAction((SoAction)action);
}

// Documented in superclass.
public void
GLRender(SoGLRenderAction action)
{
  doAction((SoAction)action);
  // don't auto cache LevelOfDetail nodes.
  SoGLCacheContextElement.shouldAutoCache(action.getState(),
                                           SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
}

// Documented in superclass.
public void
rayPick(SoRayPickAction action)
{
  doAction((SoAction)action);
}

// Documented in superclass.
public void
audioRender(SoAudioRenderAction action)
{
  /* 
     FIXME: Implement proper support for audio rendering. The
     implementation will be similar to SoLOD, but will require
     enabling some more elements for SoAudioRenderAction, as well as
     rewriting this->doAction().

     The current implementation will render _all_ children instead of
     just one of them.

     2003-02-05 thammer.
   */
  // let SoGroup traverse the children
  //super.audioRender(action); FIXME
}

public void 
getBoundingBox(SoGetBoundingBoxAction action)
{
  SoState state = action.getState();

  final SbXfBox3f childrenbbox = new SbXfBox3f();
  boolean childrencenterset;
  final SbVec3f childrencenter = new SbVec3f();

  boolean iscaching = true;

  switch (action.getCurPathCode()) {
  case OFF_PATH:
  case IN_PATH:
    // can't cache if we're not traversing all children
    iscaching = false;
    break;
    //return; // no need to do any more work FIXME
  case BELOW_PATH:
  case NO_PATH:
    // check if this is a normal traversal
    if (action.isInCameraSpace()) iscaching = false;
    break;
  default:
    iscaching = false;
    //assert(0 && "unknown path code");
    break;
  }

  boolean validcache = pimpl.bboxcache != null && pimpl.bboxcache.isValid(state);

  if (iscaching && validcache) {
    SoCacheElement.addCacheDependency(state, pimpl.bboxcache);
    childrenbbox.copyFrom( pimpl.bboxcache.getBox());
    childrencenterset = pimpl.bboxcache.isCenterSet();
    childrencenter.copyFrom( pimpl.bboxcache.getCenter());
    if (pimpl.bboxcache.hasLinesOrPoints()) {
      SoBoundingBoxCache.setHasLinesOrPoints(state);
    }
  }
  else {
    // used to restore the bounding box after we have traversed children
    SbXfBox3f abox = new SbXfBox3f(action.getXfBoundingBox());

    boolean storedinvalid = false;
    
    // always push since we update SoLocalBBoxMatrixElement
    state.push();

    if (iscaching) {
      storedinvalid = SoCacheElement.setInvalid(false);

      // if we get here, we know bbox cache is not created or is invalid
      pimpl.lock();
      if (pimpl.bboxcache != null) pimpl.bboxcache.unref();
      pimpl.bboxcache = new SoBoundingBoxCache(state);
      pimpl.bboxcache.ref();
      pimpl.unlock();
      // set active cache to record cache dependencies
      SoCacheElement.set(state, pimpl.bboxcache);
    }

    // the bounding box cache should be in the local coordinate system
    SoLocalBBoxMatrixElement.makeIdentity(state);
    action.getXfBoundingBox().makeEmpty();
    action.getXfBoundingBox().setTransform(SbMatrix.identity());

    super.getBoundingBox(action);

    childrenbbox.copyFrom( action.getXfBoundingBox());
    childrencenterset = action.isCenterSet();
    if (childrencenterset) childrencenter.copyFrom(action.getCenter());

    action.getXfBoundingBox().copyFrom( abox); // reset action bbox

    if (iscaching) {
      pimpl.bboxcache.set(childrenbbox, childrencenterset, childrencenter);
    }
    state.pop(); // we pushed before calculating the cache

    if (iscaching) {
      SoCacheElement.setInvalid(storedinvalid);
    }
  }
  
  if (!childrenbbox.isEmpty()) {
    action.extendBy(childrenbbox);
    if (childrencenterset) {
      // FIXME: shouldn't this assert() hold up? Investigate. 19990422 mortene.
//#if 0 // disabled
//      assert(!action->isCenterSet());
//#else
      action.resetCenter();
//#endif
      action.setCenter(childrencenter, true);
    }
  }
}

// Doc from superclass.
public void
notify(SoNotList nl)
{
  if (nl.getLastField() != this.screenArea) {
    pimpl.lock();
    if (pimpl.bboxcache != null) pimpl.bboxcache.invalidate();
    pimpl.unlock();
  }
  super.notify(nl);
}



	// Documented in superclass.
	  public static void
	  initClass()
	  {
		    SoSubNode.SO__NODE_INIT_CLASS(SoLevelOfDetail.class, "LevelOfDetail", SoGroup.class);	 
	    //SO_NODE_INTERNAL_INIT_CLASS(SoLevelOfDetail, SO_FROM_INVENTOR_1);

	    so_lod_storage = new SbStorage<so_lod_static_data>(so_lod_static_data.class,
	                                   SoLevelOfDetail::so_lod_construct_data, SoLevelOfDetail::so_lod_destruct_data);
	    //coin_atexit((coin_atexit_f*) so_lod_cleanup, CC_ATEXIT_NORMAL);
	  }


}
