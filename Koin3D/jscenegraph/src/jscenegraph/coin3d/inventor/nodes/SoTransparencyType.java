/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTransparencyType extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTransparencyType.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTransparencyType.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTransparencyType.class); }    	  	
	
	  public enum Type {
		    SCREEN_DOOR ( SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()),
		    	    ADD ( SoGLRenderAction.TransparencyType.ADD.getValue()),
		    	    DELAYED_ADD ( SoGLRenderAction.TransparencyType.DELAYED_ADD.getValue()),
		    	    SORTED_OBJECT_ADD ( SoGLRenderAction.TransparencyType.SORTED_OBJECT_ADD.getValue()),
		    	    BLEND ( SoGLRenderAction.TransparencyType.BLEND.getValue()),
		    	    DELAYED_BLEND ( SoGLRenderAction.TransparencyType.DELAYED_BLEND.getValue()),
		    	    SORTED_OBJECT_BLEND ( SoGLRenderAction.TransparencyType.SORTED_OBJECT_BLEND.getValue()),
		    NONE( SoGLRenderAction.TransparencyType.NONE.getValue());
		    	    private int value;
		    	    Type(int value) {
		    	    	this.value = value;
		    	    }
					public int getValue() {
						return value;
					}
	  }
	  
	  public final SoSFEnum value = new SoSFEnum();
	  
	  
	  public static void
	  initClass()
	  {
	    SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoTransparencyType.class, "TransparencyType", SoNode.class);
	  }

	  /*!
	  Constructor.
	*/
	public SoTransparencyType()
	{
	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoTransparencyType.class);

	  nodeHeader.SO_NODE_ADD_FIELD(value,"value", (Type.SCREEN_DOOR.getValue()));

	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SCREEN_DOOR);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.ADD);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.DELAYED_ADD);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.BLEND);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.DELAYED_BLEND);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SORTED_OBJECT_ADD);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SORTED_OBJECT_BLEND);
//	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SORTED_OBJECT_SORTED_TRIANGLE_ADD);
//	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SORTED_OBJECT_SORTED_TRIANGLE_BLEND);
//	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.NONE);

	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(value,"value", "Type");
	}


	/*!
	  Destructor.
	*/
	public void destructor()
	{
		super.destructor();
	}

	// Doc from superclass.
	public void
	GLRender(SoGLRenderAction action)
	{
	  SoTransparencyType_doAction(action);
	}

	// Doc from superclass.
	public void
	SoTransparencyType_doAction(SoAction action)
	{
	  if (!this.value.isIgnored()
	      && !SoOverrideElement.getTransparencyTypeOverride(action.getState())) {
	    SoShapeStyleElement.setTransparencyType(action.getState(),
	                                             this.value.getValue());
	    SoLazyElement.setTransparencyType(action.getState(),
	                                       this.value.getValue());
	    if (this.isOverride()) {
	      SoOverrideElement.setTransparencyTypeOverride(action.getState(), this, true);
	    }
	  }
	}

	// Doc from superclass.
	public void
	callback(SoCallbackAction action)
	{
	  SoTransparencyType_doAction((SoAction )action);
	}
}
