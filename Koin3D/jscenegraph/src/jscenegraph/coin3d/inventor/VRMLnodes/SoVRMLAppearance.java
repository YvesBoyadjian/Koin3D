/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLAppearance extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLAppearance.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLAppearance.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLAppearance.class); }
	  
  public final SoSFNode material = new SoSFNode();
  public final SoSFNode texture = new SoSFNode();
  public final SoSFNode textureTransform = new SoSFNode();
  
  private	  SoVRMLAppearanceP pimpl;
	  
/*!
  Constructor.
*/
public SoVRMLAppearance()
{
  pimpl = new SoVRMLAppearanceP();
  // supply a NULL-pointer as parent, since notifications will be
  // handled by the fields that actually contain the node(s)
  pimpl.childlist = new SoChildList(null);
  pimpl.childlistvalid = false;

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLAppearance.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(material,"material", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(texture,"texture", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(textureTransform,"textureTransform", (null));
}

  
	  
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLAppearance, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLAppearance.class, "VRMLAppearance", SoNode.class);	
	}

}
