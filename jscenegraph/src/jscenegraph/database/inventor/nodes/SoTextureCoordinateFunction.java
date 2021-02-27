/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureCoordinateFunction extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinateFunction.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinateFunction.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinateFunction.class); }    	  	
	
	  public SoTextureCoordinateFunction() {
		    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinateFunction.class*/);
		    isBuiltIn = true;		  
	  }
	  
	  public void destructor() {
		  super.destructor();
	  }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinateFunction class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinateFunction.class,
                      "TextureCoordinateFunction", SoNode.class);
}

}
