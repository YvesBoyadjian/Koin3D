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
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLBox extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLBox.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLBox.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLBox.class); }
	  
	  public final SoSFVec3f size = new SoSFVec3f();


/*!
  Constructor.
*/
public SoVRMLBox()
{
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLBox.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(size,"size", new SbVec3f(2.0f, 2.0f, 2.0f));
}

// Doc in parent
	public void GLRender(SoGLRenderAction action)
	{
		if (!this.shouldGLRender(action)) return;
		SoState state = action.getState();

		boolean doTextures = SoGLMultiTextureEnabledElement.get(state, 0);

		final SoMaterialBundle mb = new SoMaterialBundle(action);
		mb.sendFirst();

		boolean sendNormals = !mb.isColorOnly() ||
				(SoMultiTextureCoordinateElement.getType(state) == SoMultiTextureCoordinateElement.CoordType.FUNCTION);

		int flags = 0;
		if (doTextures) flags |= SoGL.SOGL_NEED_TEXCOORDS;
		if (sendNormals) flags |= SoGL.SOGL_NEED_NORMALS;

		SbVec3f s = this.size.getValue();

		SoGL.sogl_render_cube(s.getValueRead()[0],
				s.getValueRead()[1],
				s.getValueRead()[2],
				mb,
				flags, state);

		mb.destructor();
	}

	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		center.setValue(0.0f, 0.0f, 0.0f);
		SbVec3f s = this.size.getValue();
		float w = s.getValueRead()[0] * 0.5f;
		float h = s.getValueRead()[1] * 0.5f;
		float d = s.getValueRead()[2] * 0.5f;

		// Allow negative values.
		if (w < 0.0f) w = -w;
		if (h < 0.0f) h = -h;
		if (d < 0.0f) d = -d;

		box.setBounds(-w, -h, -d, w, h, d);
	}

	@Override
	protected void generatePrimitives(SoAction action) {
//		SbVec3f s = this.size.getValue(); TODO
//		sogen_generate_cube(s.getValueRead()[0],
//				s.getValueRead()[1],
//				s.getValueRead()[2],
//				0,
//				this,
//				action);
	}


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass() // static
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLBox.class, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLBox.class, "VRMLBox", SoVRMLGeometry.class);
}
}
