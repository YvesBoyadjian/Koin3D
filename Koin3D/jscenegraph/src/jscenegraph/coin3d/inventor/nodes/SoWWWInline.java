package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoLocateHighlight;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoWWWInline extends SoNode {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoWWWInline.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoWWWInline.class); }
    public SoType      getTypeId()       /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData() {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoWWWInline.class); }


    public final SoSFString name = new SoSFString();
    public final SoSFVec3f bboxCenter = new SoSFVec3f();
    public final SoSFVec3f bboxSize = new SoSFVec3f();
    public final SoSFNode alternateRep = new SoSFNode();

    enum BboxVisibility {
        NEVER,
        UNTIL_LOADED,
        ALWAYS
    };


/*!
  Constructor.
*/
    public SoWWWInline()
    {
        nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoWWWInline.class);

//        PRIVATE(this) = new SoWWWInlineP(this);
//        PRIVATE(this)->children = new SoChildList(this);
//        PRIVATE(this)->didrequest = FALSE;

        nodeHeader.SO_NODE_ADD_FIELD(name,"name", ("<Undefined file>"));
        nodeHeader.SO_NODE_ADD_FIELD(bboxCenter,"bboxCenter", new SbVec3f(0.0f, 0.0f, 0.0f));
        nodeHeader.SO_NODE_ADD_FIELD(bboxSize,"bboxSize", new SbVec3f(0.0f, 0.0f, 0.0f));
        nodeHeader.SO_NODE_ADD_FIELD(alternateRep,"alternateRep", (null));

        // Instantiated dynamically to avoid problems on platforms with
        // systemloaders that hate static constructors in C++ libraries.
//        if (SoWWWInline.bboxcolor == NULL) {
//            SoWWWInline.bboxcolor = new SbColor(0.8f, 0.8f, 0.8f);
//            wwwinline_colorpacker_storage = new SbStorage(sizeof(void*), alloc_colorpacker, free_colorpacker);
//            coin_atexit((coin_atexit_f *)SoWWWInline::cleanup, CC_ATEXIT_NORMAL);
//        }
    }

// doc in super
/*!
  \copybrief SoBase::initClass(void)
*/
    public static void
    initClass()
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoWWWInline.class, "WWWInline", SoNode.class);
        //SO_NODE_INTERNAL_INIT_CLASS(SoWWWInline.class, SO_FROM_INVENTOR_2_1|SoNode.NodeType.VRML1);
    }
}
