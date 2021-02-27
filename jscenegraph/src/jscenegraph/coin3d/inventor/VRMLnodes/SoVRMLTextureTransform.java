/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLTextureTransform extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLTextureTransform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTextureTransform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTextureTransform.class); }    	  	

	  public final SoSFVec2f translation = new SoSFVec2f();
	  public final SoSFFloat rotation = new SoSFFloat();
	  public final SoSFVec2f scale = new SoSFVec2f();
	  public final SoSFVec2f center = new SoSFVec2f();


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLTextureTransform, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLTextureTransform.class, "VRMLTextureTransform", SoNode.class);
}

/*!
  Constructor.
*/
public SoVRMLTextureTransform()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTextureTransform.class);

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(translation,"translation", new SbVec2f(0.0f, 0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(rotation,"rotation", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(scale,"scale", new SbVec2f(1.0f, 1.0f));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(center,"center", new SbVec2f(0.0f, 0.0f));
}
}
