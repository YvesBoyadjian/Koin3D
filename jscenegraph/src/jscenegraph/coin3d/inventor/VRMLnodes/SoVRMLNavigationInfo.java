/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLNavigationInfo extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLNavigationInfo.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLNavigationInfo.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLNavigationInfo.class); }    	  	

	  public final SoMFString type = new SoMFString();
	  public final SoSFFloat speed = new SoSFFloat();
	  public final SoMFFloat avatarSize = new SoMFFloat();
	  public final SoSFFloat visibilityLimit = new SoSFFloat();
	  public final SoSFBool headlight = new SoSFBool();

	  protected final SoSFBool set_bind = new SoSFBool();
	  protected final SoSFBool isBound = new SoSFBool();

/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass() // static
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLNavigationInfo, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLNavigationInfo.class, "VRMLNavigationInfo", SoNode.class);
}

/*!
  Constructor.
*/
public SoVRMLNavigationInfo()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLNavigationInfo.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(type,"type", ("WALK"));
  this.type.setNum(2);
  this.type.set1Value(1, "ANY");
  this.type.setDefault(true);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(speed,"speed", (1.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(avatarSize,"avatarSize", (0.25f));
  this.avatarSize.setNum(3);
  this.avatarSize.set1Value(1, 1.6f);
  this.avatarSize.set1Value(2, 0.75f);
  this.avatarSize.setDefault(true);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(visibilityLimit,"visibilityLimit", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(headlight,"headlight", (true));

  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(set_bind,"set_bind");
  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isBound,"isBound");
}

}
