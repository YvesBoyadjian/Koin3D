/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedFaceSet extends SoVRMLIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLIndexedFaceSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedFaceSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedFaceSet.class); }

	// for concavestatus
	  public static final int STATUS_UNKNOWN = 0;
	  public static final int STATUS_CONVEX  = 1;
	  public static final int STATUS_CONCAVE = 2;

	  public final SoSFBool ccw = new SoSFBool();
	  public final SoSFBool solid = new SoSFBool();
	  public final SoSFBool convex = new SoSFBool();
	  public final SoSFFloat creaseAngle = new SoSFFloat();	  
	  
	  private SoVRMLIndexedFaceSetP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVRMLIndexedFaceSet()
	{
	  pimpl = new SoVRMLIndexedFaceSetP();
	  pimpl.convexCache = null;
	  pimpl.concavestatus = STATUS_UNKNOWN;
	  pimpl.vaindexer = null;

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedFaceSet.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(ccw,"ccw", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(solid,"solid", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(convex,"convex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(creaseAngle,"creaseAngle", (0.0f));

	}

	  

	// Doc in parent
	public void generatePrimitives(SoAction action)
	{
		//TODO
	}
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLIndexedFaceSet, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLIndexedFaceSet.class, "VRMLIndexedFaceSet", SoVRMLIndexedShape.class);
	}

}
