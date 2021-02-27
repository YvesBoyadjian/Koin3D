/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLSwitch extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLSwitch.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLSwitch.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLSwitch.class); }    	  	
	
	  public final SoMFNode choice = new SoMFNode();
	  public final SoSFInt32 whichChoice = new SoSFInt32();


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLSwitch, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLSwitch.class, "VRMLSwitch", SoGroup.class);
}

/*!
  Constructor.
*/
public SoVRMLSwitch()
{
  SoVRMLSwitch_commonConstructor();
}

/*!
  Constructor. \a choices is the expected number of children.
*/
public SoVRMLSwitch(int choices) {
  super(choices);

  SoVRMLSwitch_commonConstructor();
}

// commen constructor
private void
SoVRMLSwitch_commonConstructor()
{
//  pimpl = new SoVRMLSwitchP();
//  pimpl.childlistvalid = false;

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLSwitch.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(whichChoice,"whichChoice", (SoSwitch.SO_SWITCH_NONE));
  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(choice,"choice");

  // HACK WARNING: All children of this node are stored in the choice
  // field. Avoid double notifications (because of notification
  // through SoChildList) be reallocating the SoChildList with a
  // NULL-parent here. SoGroup will have allocated an SoChildList in
  // its constructor when we get here.
  Destroyable.delete( super.children );
  super.children = new SoChildList(null);
}

}
