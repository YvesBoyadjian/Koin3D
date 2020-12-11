/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLVertexLine extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLVertexLine.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLVertexLine.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLVertexLine.class); }    
	  

	  public final SoSFNode coord = new SoSFNode();
	  public final SoSFNode color = new SoSFNode();
	  public final SoSFBool colorPerVertex = new SoSFBool();

	  /*!
	  Constructor.
	*/
	public SoVRMLVertexLine()
	{
		nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLVertexLine.class);

		nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(coord,"coord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(color,"color", (null));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(colorPerVertex,"colorPerVertex", (true));
	}


// Doc in parent
	public void
	doAction(SoAction action)
	{
		SoNode node;

		node = this.coord.getValue();
		if (node!=null) node.doAction(action);

		node = this.color.getValue();
		if (node!=null) node.doAction(action);
	}

// Doc in parent
	public void
	GLRender(SoGLRenderAction action)
	{
		SoNode node;

		node = this.coord.getValue();
		if (node!=null) node.GLRender(action);

		node = this.color.getValue();
		if (node!=null) node.GLRender(action);
	}

// Doc in parent
	public boolean
	shouldGLRender(SoGLRenderAction action)
	{
		if (this.coord.getValue() == null) return false;
		return super.shouldGLRender(action);
	}

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLVertexLine, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLVertexLine.class, "VRMLVertexLine", SoVRMLGeometry.class);
	}

	
}
