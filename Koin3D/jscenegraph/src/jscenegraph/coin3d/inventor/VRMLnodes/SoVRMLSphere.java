/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLSphere extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLSphere.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLSphere.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLSphere.class); }
	  
	  public final SoSFFloat radius = new SoSFFloat();


/*!
  Constructor.
*/
public SoVRMLSphere()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLSphere.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(radius,"radius", (1.0f));
}

	  
	  
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void generatePrimitives(SoAction action) {
		// TODO Auto-generated method stub
		
	}    	  	
	
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLSphere.class, SO_VRML97_NODE_TYPE);
    SoSubNode.SO__NODE_INIT_CLASS(SoVRMLSphere.class, "VRMLSphere", SoVRMLGeometry.class);	 
}

}
