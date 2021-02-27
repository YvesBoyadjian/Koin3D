/**
 * 
 */
package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransform;

/**
 * @author Yves Boyadjian
 *
 */
public class SoWrapperKit extends SoSeparatorKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoWrapperKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoWrapperKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoShapeKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    protected final SoSFNode contents = new SoSFNode();
    protected final SoSFNode localTransform = new SoSFNode();
    
    /*!
    Constructor.
  */
  public SoWrapperKit()
  {
	  kitHeader.SO_KIT_CONSTRUCTOR(SoWrapperKit.class);

	    isBuiltIn = true;

    // Note: we must use "" instead of , , to humour MS VisualC++ 6.

	    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(localTransform,"localTransform", SoTransform.class, true, "topSeparator", /*"contents"*/"", true);
	    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(contents,"contents", SoSeparator.class, true, "topSeparator", "", true);

    SO_KIT_INIT_INSTANCE();
  }

//Documented in superclass.
public static void
initClass()
{
 SO__KIT_INIT_CLASS(SoWrapperKit.class, "WrapperKit", SoSeparatorKit.class);
}

}
