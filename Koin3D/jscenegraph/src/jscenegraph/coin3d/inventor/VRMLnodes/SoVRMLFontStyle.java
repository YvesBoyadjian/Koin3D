/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLFontStyle extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLFontStyle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLFontStyle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLFontStyle.class); }    	  	
	
	  enum Family {
		    SERIF,
		    SANS,
		    TYPEWRITER
		  };

		  enum Style {
		    PLAIN      ( 0x0),
		    BOLD       ( 0x1),
		    ITALIC     ( 0x2),
		    BOLDITALIC ( 0x3);
		    
		    private int value;
		    
		    Style(int value) {
		    	this.value = value;
		    }
		  };

		  public final SoSFFloat size = new SoSFFloat();
		  public final SoMFString family = new SoMFString();
		  public final SoMFString style = new SoMFString();
		  public final SoSFBool horizontal = new SoSFBool();
		  public final SoSFBool leftToRight = new SoSFBool();
		  public final SoSFBool topToBottom = new SoSFBool();
		  public final SoSFString language = new SoSFString();
		  public final SoMFString justify = new SoMFString();
		  public final SoSFFloat spacing = new SoSFFloat();

		  /*!
		  \copydetails SoNode::initClass(void)
		*/
		public static void initClass() // static
		{
		  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLFontStyle, SO_VRML97_NODE_TYPE);
			SoSubNode.SO__NODE_INIT_CLASS(SoVRMLFontStyle.class, "VRMLFontStyle", SoNode.class);
		}

		/*!
		  Constructor.
		*/
		public SoVRMLFontStyle()
		{
		  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLFontStyle.class);

		  nodeHeader.SO_VRMLNODE_ADD_FIELD(size,"size", (1.0f));
		  nodeHeader.SO_VRMLNODE_ADD_MFIELD(family,"family", ("SERIF"));
		  nodeHeader.SO_VRMLNODE_ADD_MFIELD(style,"style", ("PLAIN"));
		  nodeHeader.SO_VRMLNODE_ADD_FIELD(horizontal,"horizontal", (true));
		  nodeHeader.SO_VRMLNODE_ADD_FIELD(leftToRight,"leftToRight", (true));
		  nodeHeader.SO_VRMLNODE_ADD_FIELD(topToBottom,"topToBottom", (true));
		  nodeHeader.SO_VRMLNODE_ADD_FIELD(language,"language", (""));
		  nodeHeader.SO_VRMLNODE_ADD_MFIELD(justify,"justify", ("BEGIN"));
		  nodeHeader.SO_VRMLNODE_ADD_FIELD(spacing,"spacing", (1.0f));
		}


}
