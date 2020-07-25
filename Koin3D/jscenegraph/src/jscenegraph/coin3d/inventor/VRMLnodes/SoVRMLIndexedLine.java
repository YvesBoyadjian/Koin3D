/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLIndexedLine extends SoVRMLVertexLine {	

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLIndexedLine.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedLine.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedLine.class); }    
	  
	
	  public final SoMFInt32 coordIndex = new SoMFInt32();
	  public final SoMFInt32 colorIndex = new SoMFInt32();


public SoVRMLIndexedLine() // protected
{
	nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedLine.class);

	nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(coordIndex,"coordIndex");
	nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(colorIndex,"colorIndex");
}



	  public void computeBBox(SoAction action,
              final SbBox3f box, final SbVec3f center) {
		  //TODO
	  }

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLIndexedLine, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLIndexedLine.class, "VRMLIndexedLine", SoVRMLVertexLine.class);
	}

	  
}
