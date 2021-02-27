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
public class SoVRMLCone extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLCone.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLCone.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLCone.class); }
	  

	  public final SoSFFloat bottomRadius = new SoSFFloat();
	  public final SoSFFloat height = new SoSFFloat();
	  public final SoSFBool side = new SoSFBool();
	  public final SoSFBool bottom = new SoSFBool();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLCone, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLCone.class,"VRMLCone",SoVRMLGeometry.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLCone()
	{
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLCone.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(bottomRadius,"bottomRadius", (1.0f));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(height,"height", (2.0f));

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(side,"side", (true));
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
