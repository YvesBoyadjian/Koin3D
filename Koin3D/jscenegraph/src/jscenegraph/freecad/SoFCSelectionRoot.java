/**
 * 
 */
package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.nodes.SoAnnotation;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoFCSelectionRoot extends SoFCSeparator {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFCSelectionRoot.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFCSelectionRoot.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFCSelectionRoot.class); }    	  	

	    public final SoSFEnum selectionStyle = new SoSFEnum();

	    public enum SelectStyles {
	        Full, Box, PassThrough;
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	  public SoFCSelectionRoot(/*boolean trackCacheMode*/) {
	    super(/*trackCacheMode*/); //TODO
	
	    nodeHeader.SO_NODE_CONSTRUCTOR(SoFCSelectionRoot.class);
	    nodeHeader.SO_NODE_ADD_FIELD(selectionStyle,"selectionStyle",(SelectStyles.Full));
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SelectStyles.Full);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SelectStyles.Box);
	    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SelectStyles.PassThrough);
	    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(selectionStyle,"selectionStyle", "SelectStyles");
	}

	public static void initClass()
	{
	    SoSubNode.SO_NODE_INIT_CLASS(SoFCSelectionRoot.class,"FCSelectionRoot",SoFCSeparator.class,"FCSeparator");

//	    so_bbox_storage = new SbStorage(sizeof(SoFCBBoxRenderInfo),
//	            so_bbox_construct_data, so_bbox_destruct_data);

	    // cc_coin_atexit((coin_atexit_f*) so_bbox_cleanup);
	}

}
