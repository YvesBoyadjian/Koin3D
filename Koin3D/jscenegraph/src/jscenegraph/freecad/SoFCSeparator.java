/**
 * 
 */
package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoAnnotation;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoFCSeparator extends SoSeparator {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFCSeparator.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFCSeparator.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFCSeparator.class); }    	  	
	  
	  public SoFCSeparator() {
		  super();		  
		  nodeHeader.SO_NODE_CONSTRUCTOR(SoFCSeparator.class);
	  }

	  public static void initClass()
	  {
	      SoSubNode.SO_NODE_INIT_CLASS(SoFCSeparator.class,"FCSeparator",SoSeparator.class,"Separator");
	  }


}
