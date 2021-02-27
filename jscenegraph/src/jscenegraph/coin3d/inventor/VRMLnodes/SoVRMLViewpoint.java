/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLViewpoint extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLViewpoint.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLViewpoint.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLViewpoint.class); }    	  	

	  public final SoSFVec3f position = new SoSFVec3f();
	  public final SoSFRotation orientation = new SoSFRotation();
	  public final SoSFFloat fieldOfView = new SoSFFloat();
	  public final SoSFString description = new SoSFString();
	  public final SoSFBool jump = new SoSFBool();

	  protected final SoSFBool set_bind = new SoSFBool();
	  protected final SoSFTime bindTime = new SoSFTime();
	  protected final SoSFBool isBound = new SoSFBool();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLViewpoint, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLViewpoint.class, "VRMLViewpoint", SoNode.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLViewpoint()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLViewpoint.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(position,"position", new SbVec3f(0.0f, 0.0f, 0.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(orientation,"orientation", (SbRotation.identity()));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(fieldOfView,"fieldOfView", ((float)(Math.PI)/4.0f));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(jump,"jump", (true));

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(description,"description", (""));

	  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(set_bind,"set_bind");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(bindTime,"bindTime");
	  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isBound,"isBound");
	}

}
