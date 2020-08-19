/**
 * 
 */
package jscenegraph.freecad;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.nodes.SoArray;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoFCSelection extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFCSelection.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFCSelection.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFCSelection.class); }    	  	

	    public enum HighlightModes {
	        AUTO, ON, OFF;
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	    public enum SelectionModes {
	        SEL_ON, SEL_OFF;
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	    public enum Selected {
	        NOTSELECTED, SELECTED;
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	    public enum Styles {
	        EMISSIVE, EMISSIVE_DIFFUSE, BOX;
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	    public final SoSFColor colorHighlight = new SoSFColor();
	    public final SoSFColor colorSelection = new SoSFColor();
	    public final SoSFEnum style = new SoSFEnum();
	    public final SoSFEnum selected = new SoSFEnum();
	    public final SoSFEnum highlightMode = new SoSFEnum();
	    public final SoSFEnum selectionMode = new SoSFEnum();

	    public final SoSFString documentName = new SoSFString();
	    public final SoSFString objectName = new SoSFString();
	    public final SoSFString subElementName = new SoSFString();
	    public final SoSFBool useNewSelection = new SoSFBool();
	    
/*!
  Constructor.
*/
public SoFCSelection()
{
    nodeHeader.SO_NODE_CONSTRUCTOR(SoFCSelection.class);

    nodeHeader.SO_NODE_ADD_FIELD(colorHighlight,"colorHighlight", (new SbColor(0.8f, 0.1f, 0.1f)));
    nodeHeader.SO_NODE_ADD_FIELD(colorSelection,"colorSelection", (new SbColor(0.1f, 0.8f, 0.1f)));
    nodeHeader.SO_NODE_ADD_FIELD(style,"style",          (Styles.EMISSIVE));
    nodeHeader.SO_NODE_ADD_FIELD(highlightMode,"highlightMode",  (HighlightModes.AUTO));
    nodeHeader.SO_NODE_ADD_FIELD(selectionMode,"selectionMode",  (SelectionModes.SEL_ON));
    nodeHeader.SO_NODE_ADD_FIELD(selected,"selected",       (Selected.NOTSELECTED));
    nodeHeader.SO_NODE_ADD_FIELD(documentName,"documentName",   (""));
    nodeHeader.SO_NODE_ADD_FIELD(objectName,"objectName",     (""));
    nodeHeader.SO_NODE_ADD_FIELD(subElementName,"subElementName", (""));
    nodeHeader.SO_NODE_ADD_FIELD(useNewSelection,"useNewSelection", (true));

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Styles.EMISSIVE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Styles.EMISSIVE_DIFFUSE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Styles.BOX);
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style,"style",   "Styles");

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(HighlightModes.AUTO);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(HighlightModes.ON);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(HighlightModes.OFF);
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE (highlightMode,"highlightMode", "HighlightModes");

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SelectionModes.SEL_ON);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SelectionModes.SEL_OFF);
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE (selectionMode,"selectionMode",  "SelectionModes");

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Selected.NOTSELECTED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Selected.SELECTED);
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(selected,"selected",  "Selected");

//    highlighted = false; TODO
//    bShift      = false;
//    bCtrl       = false;

    selected.setValue(Selected.NOTSELECTED);

//    useNewSelection = ViewParams.instance().getUseNewSelection(); TODO
//    selContext = std::make_shared<SelContext>();
//    selContext2 = std::make_shared<SelContext>();
}

	// doc from parent
	  public static void initClass()
	  {
	      SoSubNode.SO_NODE_INIT_CLASS(SoFCSelection.class,"FCSelection", SoGroup.class,"Group");
	  }

}
