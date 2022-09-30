/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SoCoordinate3 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCoordinate3.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCoordinate3.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCoordinate3.class); }    	  	
	
	  public final SoMFVec3f point = new SoMFVec3f();

	  
	  private SoCoordinate3P pimpl;

/*!
  Constructor.
*/
public SoCoordinate3()
{
  this.pimpl = new SoCoordinate3P();

  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoCoordinate3.class);

  nodeHeader.SO_NODE_ADD_MFIELD(point,"point", new SbVec3f(0.0f, 0.0f, 0.0f));
}

/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete( this.pimpl);
  super.destructor();
}

// Doc from superclass.
public static void
initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoCoordinate3, SO_FROM_INVENTOR_1|SoNode.VRML1);
    SO__NODE_INIT_CLASS(SoCoordinate3.class, "Coordinate3", SoNode.class);

  SO_ENABLE(SoGetBoundingBoxAction.class, SoCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoGLCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
  SO_ENABLE(SoPickAction.class, SoCoordinateElement.class);
  SO_ENABLE(SoCallbackAction.class, SoCoordinateElement.class);
  SO_ENABLE(SoGetPrimitiveCountAction.class, SoCoordinateElement.class);
}

// Doc from superclass.
public void
SoCoordinate3_doAction(SoAction action)
{
  SoCoordinateElement.set3(action.getState(), this,
                            point.getNum(), point.getValuesSbVec3fArray(/*0*/));
}

// Doc from superclass.
public void
GLRender(SoGLRenderAction action)
{
  SoCoordinate3_doAction(action);
  SoState state = action.getState();
  int num = this.point.getNum();
  boolean setvbo = false;
  SoBase.staticDataLock();
  if (SoGLVBOElement.shouldCreateVBO(state, num)) {
    boolean dirty = false;
    setvbo = true;
    if (this.pimpl.vbo == null) {
      this.pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/); 
      dirty =  true;
    }
    else if (this.pimpl.vbo.getBufferDataId() != this.getNodeId()) {
      dirty = true;
    }
    if (dirty) {
      this.pimpl.vbo.setBufferData(VoidPtr.create(this.point.getValuesSbVec3fArray(/*0*/)),
                                        num*SbVec3f.sizeof(),
                                        this.getNodeId(),state);
    }
  }
  else if (this.pimpl.vbo != null && this.pimpl.vbo.getBufferDataId() != 0) {
    // clear buffers to deallocate VBO memory
    this.pimpl.vbo.setBufferData(null, 0, 0,state);
  }
  SoBase.staticDataUnlock();
  SoGLVBOElement.setVertexVBO(state, setvbo ? this.pimpl.vbo : null);
}

// Doc from superclass.
public void
callback(SoCallbackAction action)
{
  SoCoordinate3_doAction(action);
}

// Doc from superclass.
public void
pick(SoPickAction action)
{
  SoCoordinate3_doAction(action);
}

// Doc from superclass.
public void
getBoundingBox(SoGetBoundingBoxAction action)
{
  SoCoordinate3_doAction(action);
}

// Doc from superclass.
public void
getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  SoCoordinate3_doAction(action);
}

//#undef PRIVATE
	  
}
