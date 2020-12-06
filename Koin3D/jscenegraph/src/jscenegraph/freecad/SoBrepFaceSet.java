package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNonIndexedShape;
import jscenegraph.database.inventor.nodes.SoPointSet;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoBrepFaceSet extends SoIndexedFaceSet {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBrepFaceSet.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoBrepFaceSet.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoBrepFaceSet.class); }

    public final SoMFInt32 partIndex = new SoMFInt32();

    public SoBrepFaceSet()
    {
        nodeHeader.SO_NODE_CONSTRUCTOR(SoBrepFaceSet.class);
        nodeHeader.SO_NODE_ADD_MFIELD(partIndex,"partIndex", (-1));

//        selContext = std::make_shared<SelContext>();
//        selContext2 = std::make_shared<SelContext>();
//        packedColor = 0;
//
//        pimpl.reset(new VBO);
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoBrepFaceSet class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoBrepFaceSet.class, "BrepFaceSet", SoIndexedFaceSet.class);
    }

}
