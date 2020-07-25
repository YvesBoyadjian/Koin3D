/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.TidBits;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFVec3f;
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
