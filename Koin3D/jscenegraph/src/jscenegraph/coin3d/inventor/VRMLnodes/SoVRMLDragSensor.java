/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLDragSensor extends SoVRMLSensor {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLDragSensor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLDragSensor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLDragSensor.class); }    	  	
	
	  public
		  final SoSFVec3f trackPoint_changed = new SoSFVec3f();
		  public final SoSFBool autoOffset = new SoSFBool();

		  private final
			  // FIXME: move to private class
			  SbVec3f hitpt = new SbVec3f();
		  private final SbMatrix obj2world = new SbMatrix();
			  private final SbMatrix world2obj = new SbMatrix();
			  private final SbVec2s mousepos = new SbVec2s();
			  private final SbVec2f normpos = new SbVec2f();
			  private final  SbViewVolume viewvolume = new SbViewVolume();


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLDragSensor, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLDragSensor.class, "VRMLDragSensor", SoVRMLSensor.class);
}

/*!
  Constructor.
*/
public SoVRMLDragSensor()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(SoVRMLDragSensor.class);

  nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(trackPoint_changed,"trackPoint_changed");
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(autoOffset,"autoOffset", (true));
}

}
