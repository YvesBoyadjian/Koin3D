/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLColor extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLColor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLColor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLColor.class); }    	  	
	
	  public final SoMFColor color = new SoMFColor();

	  private SoVRMLColorP pimpl;
	  /*!
	  Constructor.
	*/
	public SoVRMLColor()
	{
	  pimpl = new SoVRMLColorP();
	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLColor.class);

	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(color,"color");
	}


// Doc in parent
public void
SoVRMLColor_doAction(SoAction action)
{
  SoState state = action.getState();
  int num = this.color.getNum();
  if (num != 0 &&
      !this.color.isIgnored() &&
      !SoOverrideElement.getDiffuseColorOverride(state)) {

    SoLazyElement.setDiffuse(state,
                              this,
                              num,
                              this.color.getValuesSbColorArray(/*0*/),
                              pimpl.getColorPacker());

    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
      boolean setvbo = false;
      SoBase.staticDataLock();
      if (SoGLVBOElement.shouldCreateVBO(state, num)) {
        setvbo = true;
        if (pimpl.vbo == null) {
          pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER, GL2.GL_STATIC_DRAW);
        }
      }
      else if (pimpl.vbo != null) {
        pimpl.vbo.setBufferData(null, 0, 0);
      }
      // don't fill in any data in the VBO. Data will be filled in
      // using the ColorPacker right before the VBO is used
      SoBase.staticDataUnlock();
      if (setvbo) {
        SoGLVBOElement.setColorVBO(state, pimpl.vbo);
      }
    }
    if (this.isOverride()) {
      SoOverrideElement.setDiffuseColorOverride(state, this, true);
    }
  }
}

// Doc in parent
public void GLRender(SoGLRenderAction action)
{
  SoVRMLColor_doAction((SoAction) action);
}

// Doc in parent
public void callback(SoCallbackAction action)
{
  SoVRMLColor_doAction((SoAction) action);
}

	

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLColor, SO_VRML97_NODE_TYPE);
	       SoSubNode.SO__NODE_INIT_CLASS(SoVRMLColor.class, "VRMLColor", SoNode.class);
	}

}
