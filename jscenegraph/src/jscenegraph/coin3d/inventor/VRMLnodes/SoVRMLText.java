package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVRMLText extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLText.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLText.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLText.class); }
	  
  public final SoMFString string = new SoMFString();
  public final SoSFNode fontStyle = new SoSFNode();
  public final SoSFFloat maxExtent = new SoSFFloat();
  public final SoMFFloat length = new SoMFFloat();

  public enum Justification {
    BEGIN   (0x01),
    END     (0x02),
    MIDDLE  (0x03);
    
    private int value;
    
    Justification(int value) {
    	this.value = value;
    }
  };

	  

	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generatePrimitives(SoAction action) {
		// TODO Auto-generated method stub

	}

/*!
  Constructor.
*/
public SoVRMLText()
{
  //PRIVATE(this) = new SoVRMLTextP(this); TODO

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLText.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(string,"string");

  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(fontStyle,"fontStyle", (null));
  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(maxExtent,"maxExtent", (0.0f));
  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(length,"length");

  // Default text setup
//  pimpl.textsize = 1.0f;
//  pimpl.textspacing = 1.0f;
//  pimpl.maxglyphheight = 1.0f;
//  pimpl.maxglyphwidth = 1.0f;
//  pimpl.lefttorighttext = TRUE;
//  pimpl.toptobottomtext = TRUE;
//  pimpl.horizontaltext = TRUE;
//  pimpl.justificationmajor = SoAsciiText::LEFT;
//  pimpl.justificationminor = SoAsciiText::LEFT;
//  
//  pimpl.fontstylesensor = new SoFieldSensor(fontstylechangeCB, PRIVATE(this));
//  pimpl.fontstylesensor->attach(&fontStyle);
//  pimpl.fontstylesensor->setPriority(0);
//  
//  pimpl.cache = NULL;
}

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLText, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLText.class, "VRMLText", SoVRMLGeometry.class);
	}

}
