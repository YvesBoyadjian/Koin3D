package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoNonIndexedShape;
import jscenegraph.database.inventor.nodes.SoPointSet;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoBrepPointSet extends SoPointSet {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBrepPointSet.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoBrepPointSet.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoBrepPointSet.class); }

    public SoBrepPointSet() {
        nodeHeader.SO_NODE_CONSTRUCTOR(SoBrepPointSet.class);
        isBuiltIn = true;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoBrepPointSet class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoBrepPointSet.class, "BrepPointSet", SoPointSet.class);
    }
}
