/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLTexture extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLTexture.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTexture.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTexture.class); }    
	  
  public final SoSFBool repeatS = new SoSFBool();
  public final SoSFBool repeatT = new SoSFBool();

/*!
  Constructor.
*/
public SoVRMLTexture()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTexture.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(repeatS,"repeatS", (true));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(repeatT,"repeatT", (true));
}

	  
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLTexture, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLTexture.class, "VRMLTexture", SoNode.class);
	}

}
