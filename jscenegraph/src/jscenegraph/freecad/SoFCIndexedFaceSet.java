package jscenegraph.freecad;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoIndexedShape;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoFCIndexedFaceSet extends SoIndexedFaceSet {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFCIndexedFaceSet.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoFCIndexedFaceSet.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoFCIndexedFaceSet.class); }

    public final SoSFBool updateGLArray = new SoSFBool();

    public SoFCIndexedFaceSet() {
        super();

        nodeHeader.SO_NODE_CONSTRUCTOR(SoFCIndexedFaceSet.class);
        nodeHeader.SO_NODE_ADD_FIELD(updateGLArray,"updateGLArray", (false));
        updateGLArray.setFieldType(SoField.FieldType.EVENTOUT_FIELD.getValue());
        setName(SoFCIndexedFaceSet.getClassTypeId().getName());
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedFaceSet class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        SO__NODE_INIT_CLASS(SoFCIndexedFaceSet.class, "FCIndexedFaceSet", SoIndexedFaceSet.class);
    }
}
