// **InsertLicense** code

package jscenegraph.mevis.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLPolygonOffsetElement;
import jscenegraph.mevis.inventor.elements.SoPolygonOffsetElement;


/**
 * @author Yves Boyadjian
 *
 */
public class SoPolygonOffset extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPolygonOffset.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPolygonOffset.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPolygonOffset.class); }    	  	
	
	  public enum Style {
		   FILLED(SoPolygonOffsetElement.Style.FILLED.getValue()),
		    LINES(SoPolygonOffsetElement.Style.LINES.getValue()),
		    POINTS(SoPolygonOffsetElement.Style.POINTS.getValue()),
		    ALL(SoPolygonOffsetElement.Style.ALL.getValue());
		    
		  private int value;
		  Style(int value) {
			  this.value = value;
		  }
		  public int getValue() {
			  return value;
		  }
		    };
		   
	public final 	    SoSFFloat factor = new SoSFFloat();
	public final	    SoSFFloat units = new SoSFFloat();
	public final	    SoSFBitMask styles = new SoSFBitMask();
	public final	    SoSFBool on = new SoSFBool();
		    	

public SoPolygonOffset()
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPolygonOffset*/);

	nodeHeader.SO_NODE_ADD_SFIELD(factor,"factor", (1.f));
	nodeHeader.SO_NODE_ADD_SFIELD(units,"units",  (1.f));
	nodeHeader.SO_NODE_ADD_SFIELD(styles,"styles", (Style.FILLED.getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(on,"on",     (true));

	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.FILLED);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.LINES);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.POINTS);

	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(styles,"styles", "Style");

   isBuiltIn = true;
}

public void destructor()
{
	super.destructor();
}

public static void
initClass()
{
   SoSubNode.SO__NODE_INIT_CLASS(SoPolygonOffset.class, "PolygonOffset", SoNode.class);

   SO_ENABLE(SoGLRenderAction.class, SoGLPolygonOffsetElement.class);
   SO_ENABLE(SoGLRenderAction.class, SoOverrideElement.class);
   
   SO_ENABLE(SoCallbackAction.class, SoPolygonOffsetElement.class);
   SO_ENABLE(SoCallbackAction.class, SoOverrideElement.class);
}

private void
SoPolygonOffset_doAction(SoAction  action)
{
   SoState state= action.getState();

   if(SoOverrideElement.getPolygonOffsetOverride(state))
      return;

   SoPolygonOffsetElement.set(state, this, factor.getValue(), units.getValue(),
                               SoPolygonOffsetElement.Style.fromValue(styles.getValue()),
                               on.getValue());

   if(isOverride())
      SoOverrideElement.setPolygonOffsetOverride(state, this, true);
}

public void
GLRender(SoGLRenderAction  action)
{
   SoPolygonOffset_doAction((SoAction )action);
}

public void
callback(SoCallbackAction  action)
{
   SoPolygonOffset_doAction((SoAction )action);
}
}
