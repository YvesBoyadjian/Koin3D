/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import com.jogamp.opengl.GL2;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLTextureCoordinate extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLTextureCoordinate.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLTextureCoordinate.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLTextureCoordinate.class); }
	  
	  private SoVRMLTextureCoordinateP pimpl;
	  
	  public final SoMFVec2f point = new SoMFVec2f();

	  
/*!
  Constructor.
*/
public SoVRMLTextureCoordinate()
{
  pimpl = new SoVRMLTextureCoordinateP();
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLTextureCoordinate.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(point,"point");
}


// Doc in parent
public void doAction(SoAction action)
{
	SoVRMLTextureCoordinate_doAction(action);
}
	public void SoVRMLTextureCoordinate_doAction(SoAction action)
	{
		SoMultiTextureCoordinateElement.set2(action.getState(), this, 0,
			point.getNum(),
			point.getValues(0));
	}

// Doc in parent
	public void GLRender(SoGLRenderAction action)
	{
		SoState state = action.getState();
		SoGLMultiTextureCoordinateElement.setTexGen(state,
			this, 0, null);
		SoVRMLTextureCoordinate_doAction((SoAction)action);

		SoBase.staticDataLock();
  final int num = this.point.getNum();
		boolean setvbo = false;
		if (SoGLVBOElement.shouldCreateVBO(state, num)) {
		setvbo = true;
		boolean dirty = false;
		if (pimpl.vbo == null) {
			pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER, GL2.GL_STATIC_DRAW);
			dirty =  true;
		}
    else if (pimpl.vbo.getBufferDataId() != this.getNodeId()) {
			dirty = true;
		}
		if (dirty) {
			pimpl.vbo.setBufferData(VoidPtr.create(this.point.getValues(0)),
					num*(SbVec2f.sizeof()),
					(int)this.getNodeId());
		}
	}
  else if (pimpl.vbo != null && pimpl.vbo.getBufferDataId() != 0) {
		// clear buffers to deallocate VBO memory
		pimpl.vbo.setBufferData(null, 0, 0);
	}
		SoBase.staticDataUnlock();
		if (setvbo) {
			SoGLVBOElement.setTexCoordVBO(state, 0, pimpl.vbo);
		}
	}

	  
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLTextureCoordinate, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLTextureCoordinate.class, "VRMLTextureCoordinate", SoNode.class);
}

}
