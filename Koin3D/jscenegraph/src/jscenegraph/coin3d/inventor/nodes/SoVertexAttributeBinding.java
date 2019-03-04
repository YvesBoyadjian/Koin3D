/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.SoVertexAttributeBindingElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexAttributeBinding extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVertexAttributeBinding.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVertexAttributeBinding.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVertexAttributeBinding.class); }    	  	
	
		 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoBaseColor class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoVertexAttributeBinding.class, "VertexAttributeBinding", SoNode.class);
	       
	       SO_ENABLE(SoGLRenderAction.class, SoVertexAttributeBindingElement.class);
	       SO_ENABLE(SoPickAction.class, SoVertexAttributeBindingElement.class);
	       SO_ENABLE(SoCallbackAction.class, SoVertexAttributeBindingElement.class);
	       SO_ENABLE(SoGetPrimitiveCountAction.class, SoVertexAttributeBindingElement.class);
	   }
	  
}
