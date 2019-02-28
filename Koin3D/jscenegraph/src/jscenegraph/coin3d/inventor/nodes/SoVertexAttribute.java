/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.gl.SoGLVertexAttributeElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.actions.SoGLRenderAction;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexAttribute extends SoNode {

	// java port
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVertexAttribute.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVertexAttribute.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVertexAttribute.class); }    	  	
	
	  public static void
	  initClass()
	  {
//	    SoVertexAttribute::classTypeId =
//	      SoType::createType(SoNode::getClassTypeId(),
//	                         SbName("VertexAttribute"),
//	                         SoVertexAttribute::createInstance,
//	                         SoNode::nextActionMethodIndex++);
	    
		  // java port
	       SoSubNode.SO__NODE_INIT_CLASS(SoVertexAttribute.class, "VertexAttribute", SoNode.class);

	    //SoNode.setCompatibilityTypes(SoVertexAttribute.getClassTypeId(), SO_FROM_COIN_3_0);
	    SO_ENABLE(SoGLRenderAction.class, SoGLVertexAttributeElement.class);
	  }

	  
}
