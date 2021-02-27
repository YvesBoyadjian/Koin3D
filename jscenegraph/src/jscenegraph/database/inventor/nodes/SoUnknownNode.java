/**
 * 
 */
package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.coin3d.inventor.nodes.SoLevelOfDetail;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
///////////////////////////////////////////////////////////////////////////////
///
////\class SoUnknownNode
///
///  The Unknown Node! This node is created during file reading when a
///  node is read that is unrecognized by Inventor and a DSO cannot be found
///  for it.  Note that even though it is derived from SoGroup, we lie
///  and tell SoType that it is derived from SoNode.  This, along with
///  the fact that users are not shipped this header file (so they
///  can't call SoGroup methods directly), has the effect of making the
///  normally public children hidden.
///
//////////////////////////////////////////////////////////////////////////////

public class SoUnknownNode extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoUnknownNode.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoUnknownNode.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return instanceFieldData; //nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { 
		  return SoSubNode.getFieldDataPtr(SoUnknownNode.class); 
		  }    	  		
	  
    //! Per-instance field data
    SoFieldData instanceFieldData; // ptr

    //! Real class name
    private String className;

    //! List of hidden children.
    SoChildList hiddenChildren;
  
    //! Will be true if read in with children
    boolean hasChildren;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoUnknownNode() {
	hiddenChildren = new SoChildList(this);

//
////////////////////////////////////////////////////////////////////////

	nodeHeader.SO_NODE_CONSTRUCTOR(SoUnknownNode.class);

    instanceFieldData = new SoFieldData(nodeHeader.getFieldData());
    className = null;

    hasChildren = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the class name of the unknown node
//
// Use: public

public void setClassName( String name )

//
////////////////////////////////////////////////////////////////////////
{
    className = name;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    //
    // The fields of the unknown node have been allocated by the SoFieldData
    // class.  The unknown node will delete them here, because the field
    // data does not have enough information to know where its fields are
    // stored.  This could be redesigned.
    //
    final SoFieldList fieldList = new SoFieldList();
    int         numFields = getFields(fieldList);

    for (int i=0; i<numFields; i++)
    	Destroyable.delete( fieldList.operator_square_bracket(i) );

    Destroyable.delete( instanceFieldData );

    //if (className) free((void *)className);
    
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads field type data, fields, and children into instance of
//    SoUnknownNode. Returns FALSE on error.
//
// Use: virtual, protected

public boolean readInstance(SoInput in, short flags)

//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // This is mostly the normal SoGroup reading code, but we look for
    // an alternateRep field after reading and move our public
    // children onto the hidden children list:

    hasChildren = (!in.isBinary() || (flags & SoBase.BaseFlags.IS_GROUP.getValue()) != 0);
    boolean result;
    if (hasChildren) {
        result = /*SoGroup_*/super.readInstance(in, flags);

        // If read ASCII AND didn't read any children, set hasChildren
        // to FALSE:
        if (!in.isBinary() && getNumChildren() == 0) hasChildren = false;

        // Add all kids to hiddenChildren, then remove them all from the
        // regular (SoGroup) list
        for (i = 0; i < getNumChildren(); i++) {
            hiddenChildren.append(getChild(i));
        }
        removeAllChildren();
    }
    else {
        result = SoFieldContainer_readInstance(in, flags);
    }

    // Check to see if an alternate representation was read and
    // store a pointer to it if one is found.
    int num = instanceFieldData.getNumFields();
    boolean haveAlternateRep = false;
    for (i=0; i<num; i++) {
        if (instanceFieldData.getFieldName(i).operator_equal_equal(new SbName("alternateRep"))) {
            SoField f = instanceFieldData.getField(this, i);
            if (f.isOfType(SoSFNode.getClassTypeId(SoSFNode.class))) {
                haveAlternateRep = true;
                SoSFNode alternateRepField = (SoSFNode )f;
                SoNode n = alternateRepField.getValue();
                if (n != null)
                    addChild(n);
            }
            break;
        }
    }

    // If no alternateRep was specified, look for a field named "isA"
    // of type MFString and try to automatically create an
    // alternateRep:
    if (!haveAlternateRep) for (i=0; i<num; i++) {
        if (instanceFieldData.getFieldName(i).operator_equal_equal(new SbName("isA"))) {
            SoField f = instanceFieldData.getField(this, i);
            if (f.isOfType(SoMFString.getClassTypeId(SoMFString.class))) {
                createFromIsA((SoMFString )f);
            }
        }
    }

    return result;
}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Given an MFString "isA", create an appropriate alternateRep and
//adds it as a child.
//
//Use: private

void createFromIsA(SoMFString isA) {
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override searchAction so children (alternateRep) aren't
//    searched.
//
// Use: public

public void search(SoSearchAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoNode_search(action);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns file format name.
//
// Use: protected

String getFileFormatName()
//
////////////////////////////////////////////////////////////////////////
{
    return className;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoUnknownNode class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoUnknownNode.class, "UnknownNode", SoNode.class);
}

}
