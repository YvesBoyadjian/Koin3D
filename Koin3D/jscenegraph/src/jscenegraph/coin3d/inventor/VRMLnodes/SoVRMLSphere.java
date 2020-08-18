/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoGLShapeHintsElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;
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

	  public static final float SPHERE_NUM_SLICES = 30.0f;
	  public static final float SPHERE_NUM_STACKS = 30.0f;


/*!
  Constructor.
*/
public SoVRMLSphere()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLSphere.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(radius,"radius", (1.0f));
}


// Doc in parent
public void GLRender(SoGLRenderAction action)
{
  if (!shouldGLRender(action)) return;

  SoState state = action.getState();

  final SoMaterialBundle mb = new SoMaterialBundle(action);
  mb.sendFirst();

  boolean doTextures = SoGLMultiTextureEnabledElement.get(state,0); // YB

  boolean sendNormals = !mb.isColorOnly() ||
    (SoMultiTextureCoordinateElement.getType(state) == SoMultiTextureCoordinateElement.CoordType.FUNCTION);
  
  float complexity = this.getComplexityValue(action);

  int flags = 0;
  if (sendNormals) flags |= SoGL.SOGL_NEED_NORMALS;
  if (doTextures) flags |= SoGL.SOGL_NEED_TEXCOORDS;

  // enable back face culling
  SoGLShapeHintsElement.forceSend(state, true, true);

//  sogl_render_sphere(this.radius.getValue(), TODO YB
//                     (int)(SPHERE_NUM_SLICES * complexity),
//                     (int)(SPHERE_NUM_STACKS * complexity),
//                     mb,
//                     flags, state);
  
  mb.destructor();  
}
	  
	  
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
  float r = this.radius.getValue();

  // Allow negative values.
  if (r < 0.0f) r = -r;

  box.setBounds(new SbVec3f(-r, -r, -r), new SbVec3f(r, r, r));
  center.setValue(0.0f, 0.0f, 0.0f);
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
