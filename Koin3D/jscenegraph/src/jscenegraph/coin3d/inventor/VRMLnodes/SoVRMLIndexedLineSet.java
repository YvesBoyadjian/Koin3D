/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedLineSet extends SoVRMLIndexedLine {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLIndexedLineSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedLineSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedLineSet.class); }
	  
	  private SoVRMLIndexedLineSetP pimpl;
	  
	  public SoVRMLIndexedLineSet()
	  {
	    pimpl = new SoVRMLIndexedLineSetP();
	    nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedLineSet.class);
	  }	  	  
	
	  public void generatePrimitives(SoAction action) {
		  //TODO
	  }
	  
	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLIndexedLineSet, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLIndexedLineSet.class, "VRMLIndexedLineSet", SoVRMLIndexedLine.class);
	}

	  
}
