/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.TidBits;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoLocalBBoxMatrixElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Util;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLGroup extends SoVRMLParent {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLGroup.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLGroup.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLGroup.class); }    	  	
	
	
	protected static int numRenderCaches = 2;
	
	  public final SoSFEnum renderCaching = new SoSFEnum();
	  public final SoSFEnum boundingBoxCaching = new SoSFEnum();
	  public final SoSFEnum renderCulling = new SoSFEnum();
	  public final SoSFEnum pickCulling = new SoSFEnum();
	  public final SoSFVec3f bboxCenter = new SoSFVec3f();
	  public final SoSFVec3f bboxSize = new SoSFVec3f();	

	private SoVRMLGroupP pimpl;
/*!
  Constructor.
*/
public SoVRMLGroup()
{
  this.SoVRMLGroup_commonConstructor();
}

/*!
  Constructor. \a numchildren is the expected number of children.
*/
public SoVRMLGroup(int numchildren) {
  super(numchildren);

  this.SoVRMLGroup_commonConstructor();
}

static int maxcaches = -1;

private void
SoVRMLGroup_commonConstructor()
{
  pimpl = new SoVRMLGroupP();
  pimpl.bboxcache = null;
  pimpl.bboxcache_usecount = 0;
  pimpl.bboxcache_destroycount = 0;

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLGroup.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxCenter,"bboxCenter", new SbVec3f(0.0f, 0.0f, 0.0f));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxSize,"bboxSize", new SbVec3f(-1.0f, -1.0f, -1.0f));

  nodeHeader.SO_VRMLNODE_ADD_FIELD(renderCaching,"renderCaching", (SoSeparator.CacheEnabled.AUTO));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(boundingBoxCaching,"boundingBoxCaching", (SoSeparator.CacheEnabled.AUTO));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(renderCulling,"renderCulling", (SoSeparator.CacheEnabled.AUTO));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(pickCulling,"pickCulling", (SoSeparator.CacheEnabled.AUTO));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.ON);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.OFF);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SoSeparator.CacheEnabled.AUTO);

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCaching,"renderCaching", "CacheEnabled");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching", "CacheEnabled");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(renderCulling,"renderCulling", "CacheEnabled");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(pickCulling,"pickCulling", "CacheEnabled");

  pimpl.hassoundchild = SoVRMLGroupP.HasSound.MAYBE;

  if (maxcaches == -1) {
    maxcaches = -2; // so we don't request the envvar later if it is not set
    String maxcachesstr = TidBits.coin_getenv("IV_SEPARATOR_MAX_CACHES");
    if (maxcachesstr != null) {
      maxcaches = Util.strtol(maxcachesstr, null, 10);
      if ((maxcaches == /*LONG_MIN*/Integer.MIN_VALUE) || (maxcaches == /*LONG_MAX*/Integer.MAX_VALUE) || (maxcaches < 0)) {
        SoDebugError.post("SoVRMLGroup::commonConstructor",
                           "Environment variable IV_SEPARATOR_MAX_CACHES "+
                           "has invalid setting \""+maxcachesstr+"\"" );
      }
      else {
        SoVRMLGroup.setNumRenderCaches(maxcaches);
      }
    }
  }
}

/*!
  Set the maximum number of render caches per group node.
*/
public static void setNumRenderCaches(int num )
{
  numRenderCaches = num;
}

/*!
  Returns the maximum number of render caches per group node.
*/
public static int getNumRenderCaches()
{
  return numRenderCaches;
}

//Doc in parent
public void getBoundingBox(SoGetBoundingBoxAction action)
{
  SoState state = action.getState();

  final SbXfBox3f childrenbbox = new SbXfBox3f();
  boolean childrencenterset;
  final SbVec3f childrencenter = new SbVec3f();

  // FIXME: AUTO is interpreted as ON for the boundingBoxCaching
  // field, but we should trigger some heuristics based on scene graph
  // "behavior" in the children subgraphs if the value is set to
  // AUTO. 19990513 mortene.
  boolean iscaching = this.boundingBoxCaching.getValue() != SoSeparator.CacheEnabled.OFF.getValue();

  switch (action.getCurPathCode()) {
  case IN_PATH:
    // can't cache if we're not traversing all children
    iscaching = false;
    break;
  case OFF_PATH:
    return; // no need to do any more work
  case BELOW_PATH:
  case NO_PATH:
    // check if this is a normal traversal
    if (action.isInCameraSpace() || action.isResetPath()) iscaching = false;
    break;
  default:
    iscaching = false;
    assert(false && "unknown path code"!= null);
    break;
  }

  boolean validcache = iscaching && pimpl.bboxcache != null && pimpl.bboxcache.isValid(state);

  if (iscaching && validcache) {
    SoCacheElement.addCacheDependency(state, pimpl.bboxcache);
    pimpl.bboxcache_usecount++;
    childrenbbox.copyFrom(pimpl.bboxcache.getBox());
    childrencenterset = pimpl.bboxcache.isCenterSet();
    childrencenter.copyFrom(pimpl.bboxcache.getCenter());
    if (pimpl.bboxcache.hasLinesOrPoints()) {
      SoBoundingBoxCache.setHasLinesOrPoints(state);
    }
  }
  else {
    SbXfBox3f abox = new SbXfBox3f(action.getXfBoundingBox());
    boolean storedinvalid = false;

    // check if we should disable auto caching
    if (pimpl.bboxcache_destroycount > 10 && this.boundingBoxCaching.getValue() == SoSeparator.CacheEnabled.AUTO.getValue()) {
      if ((float)(pimpl.bboxcache_usecount) / (float)(pimpl.bboxcache_destroycount) < 5.0f) { 
        iscaching = false;
      }
    }
    if (iscaching) {
      storedinvalid = SoCacheElement.setInvalid(false);
    }
    state.push();

    if (iscaching) {
      // if we get here, we know bbox cache is not created or is invalid
      pimpl.lock();
      if (pimpl.bboxcache != null) {
        pimpl.bboxcache_destroycount++;
        pimpl.bboxcache.unref();
      }
      pimpl.bboxcache = new SoBoundingBoxCache(state);
      pimpl.bboxcache.ref();
      pimpl.unlock();
      // set active cache to record cache dependencies
      SoCacheElement.set(state, pimpl.bboxcache);
    }

    SoLocalBBoxMatrixElement.makeIdentity(state);
    action.getXfBoundingBox().makeEmpty();
    action.getXfBoundingBox().setTransform(SbMatrix.identity());
    super.getBoundingBox(action);

    childrenbbox.copyFrom(action.getXfBoundingBox());
    childrencenterset = action.isCenterSet();
    if (childrencenterset) childrencenter.copyFrom(action.getCenter());

    action.getXfBoundingBox().copyFrom(abox); // reset action bbox

    if (iscaching) {
      pimpl.bboxcache.set(childrenbbox, childrencenterset, childrencenter);
    }
    state.pop();
    if (iscaching) SoCacheElement.setInvalid(storedinvalid);
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

// Doc in parent
public void notify(SoNotList list)
{
  super.notify(list);
  
  pimpl.lock();
  if (pimpl.bboxcache != null) pimpl.bboxcache.invalidate();
  pimpl.invalidateGLCaches();
  pimpl.hassoundchild = SoVRMLGroupP.HasSound.MAYBE;
  pimpl.unlock();
}

// Doc in parent
public void
SoVRMLGroup_doAction(SoAction action)
{
  SoState state = action.getState();
  state.push();
  super.doAction(action);
  state.pop();
}


// Doc in parent
public void search(SoSearchAction action)
{
  SoNode_search(action);
  if (action.isFound()) return;

  SoVRMLGroup_doAction(action);
}



/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLGroup, SO_VRML97_NODE_TYPE);
  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLGroup.class, "VRMLGroup", SoVRMLParent.class);

  SoType type = new SoType(SoVRMLGroup.getClassTypeId());
  SoRayPickAction.addMethod(type, SoNode::rayPickS);
  SoVRMLGroup.numRenderCaches = 2;
}

}
