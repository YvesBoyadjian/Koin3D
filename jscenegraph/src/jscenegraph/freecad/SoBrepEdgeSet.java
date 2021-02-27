package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoIndexedLineSet;
import jscenegraph.database.inventor.nodes.SoNonIndexedShape;
import jscenegraph.database.inventor.nodes.SoPointSet;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoBrepEdgeSet extends SoIndexedLineSet {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBrepEdgeSet.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoBrepEdgeSet.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoBrepEdgeSet.class); }


    public SoBrepEdgeSet() {
        nodeHeader.SO_NODE_CONSTRUCTOR(SoBrepEdgeSet.class);
        isBuiltIn = true;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoBrepEdgeSet class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoBrepEdgeSet.class, "BrepEdgeSet", SoIndexedLineSet.class);
    }
}
