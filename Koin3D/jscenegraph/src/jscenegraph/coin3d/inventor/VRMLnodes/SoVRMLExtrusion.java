/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFRotation;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLExtrusion extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLExtrusion.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLExtrusion.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLExtrusion.class); }

	  public final SoSFBool beginCap = new SoSFBool();
	  public final SoSFBool ccw = new SoSFBool();
	  public final SoSFBool convex = new SoSFBool();
	  public final SoSFFloat creaseAngle = new SoSFFloat();
	  public final SoMFVec2f crossSection = new SoMFVec2f();
	  public final SoSFBool endCap = new SoSFBool();
	  public final SoMFRotation orientation = new SoMFRotation();
	  public final SoMFVec2f scale = new SoMFVec2f();
	  public final SoSFBool solid = new SoSFBool();
	  public final SoMFVec3f spine = new SoMFVec3f();

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLExtrusion, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLExtrusion.class, "VRMLExtrusion", SoVRMLGeometry.class);
	}

	/*!
	  Constructor.
	*/
	public SoVRMLExtrusion()
	{
	  //pimpl = new SoVRMLExtrusionP(this); TODO

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLExtrusion.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(beginCap,"beginCap", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(endCap,"endCap", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(solid,"solid", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(ccw,"ccw", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(convex,"convex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(creaseAngle,"creaseAngle", (0.0f));

	  nodeHeader.SO_NODE_ADD_MFIELD(crossSection,"crossSection", new SbVec2f(0.0f, 0.0f));
	  this.crossSection.setNum(5);
	  SbVec2f[] cs = this.crossSection.startEditing();
	  cs[0] = new SbVec2f(1.0f, 1.0f);
	  cs[1] = new SbVec2f(1.0f, -1.0f);
	  cs[2] = new SbVec2f(-1.0f, -1.0f);
	  cs[3] = new SbVec2f(-1.0f, 1.0f);
	  cs[4] = new SbVec2f(1.0f, 1.0f);
	  this.crossSection.finishEditing();
	  this.crossSection.setDefault(true);

	  nodeHeader.SO_NODE_ADD_MFIELD(orientation,"orientation", (SbRotation.identity()));
	  nodeHeader.SO_NODE_ADD_MFIELD(scale,"scale", new SbVec2f(1.0f, 1.0f));

	  nodeHeader.SO_NODE_ADD_MFIELD(spine,"spine", new SbVec3f(0.0f, 0.0f, 0.0f));
	  this.spine.setNum(2);
	  this.spine.set1Value(1, 0.0f, 1.0f, 0.0f);
	  this.spine.setDefault(true);
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
