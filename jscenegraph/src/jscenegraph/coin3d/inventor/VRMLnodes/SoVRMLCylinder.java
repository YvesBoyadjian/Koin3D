/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLCylinder extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLCylinder.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLCylinder.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLCylinder.class); }

	  public final SoSFFloat radius = new SoSFFloat();
	  public final SoSFFloat height = new SoSFFloat();
	  public final SoSFBool side = new SoSFBool();
	  public final SoSFBool top = new SoSFBool();
	  public final SoSFBool bottom = new SoSFBool();

/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLCylinder, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLCylinder.class, "VRMLCylinder", SoVRMLGeometry.class);
}

/*!
  Constructor.
*/
public SoVRMLCylinder()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLCylinder.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(radius,"radius", (1.0f));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(height,"height", (2.0f));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(side,"side", (true));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(top,"top", (true));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(bottom,"bottom", (true));
}

	  
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generatePrimitives(SoAction action) {
		// TODO Auto-generated method stub

	}

}
