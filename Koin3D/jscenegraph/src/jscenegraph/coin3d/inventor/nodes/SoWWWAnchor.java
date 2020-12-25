package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.nodes.SoLocateHighlight;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoWWWAnchor extends SoLocateHighlight {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoWWWAnchor.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoWWWAnchor.class); }
    public SoType      getTypeId()       /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData() {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoWWWAnchor.class); }


    public enum Mapping {
        NONE(0), POINT(1);

        private int value;
        Mapping(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    };

    public final SoSFString name = new SoSFString();
    public final SoSFString description = new SoSFString();
    public final SoSFEnum map = new SoSFEnum();

/*!
  Constructor.
*/
    public SoWWWAnchor()
    {
        nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoWWWAnchor.class);

        //PRIVATE(this) = new SoWWWAnchorP(this);

        nodeHeader.SO_NODE_ADD_FIELD(name,"name", ("<Undefined URL>"));
        nodeHeader.SO_NODE_ADD_FIELD(description,"description", (""));
        nodeHeader.SO_NODE_ADD_FIELD(map,"map", (Mapping.NONE));

        nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Mapping.NONE);
        nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Mapping.POINT);
        nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(map,"map", "Mapping");
    }

// doc in super
/*!
  \copybrief SoBase::initClass(void)
*/
    public static void
    initClass()
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoWWWAnchor.class, "WWWAnchor", SoLocateHighlight.class);
        //SO_NODE_INTERNAL_INIT_CLASS(SoWWWAnchor.class, SO_FROM_INVENTOR_2_1| SoNode.NodeType.VRML1);
        //coin_atexit((coin_atexit_f*)SoWWWAnchorP::atexit_cleanup, CC_ATEXIT_NORMAL);
    }
}
