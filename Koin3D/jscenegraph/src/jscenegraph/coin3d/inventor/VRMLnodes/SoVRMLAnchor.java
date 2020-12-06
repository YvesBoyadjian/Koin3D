package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVRMLAnchor extends  SoVRMLParent {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLAnchor.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLAnchor.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLAnchor.class); }


    public final SoMFString url = new SoMFString();
    public final SoSFString description = new SoSFString();
    public final SoMFString parameter = new SoMFString();

    public final SoSFVec3f bboxCenter = new SoSFVec3f();
    public final SoSFVec3f bboxSize = new SoSFVec3f();


/*!
  Default constructor.
*/
    public SoVRMLAnchor()
    {
        nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLAnchor.class);

        nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(url,"url");
        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(description,"description", (""));
        nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(parameter,"parameter");

        nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxCenter,"bboxCenter", new SbVec3f(0.0f, 0.0f, 0.0f));
        nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxSize,"bboxSize", new SbVec3f(-1.0f, -1.0f, -1.0f));
    }

    /*!
  \copydetails SoNode::initClass(void)
*/
    public static void
    initClass() // static
    {
        //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLAnchor, SO_VRML97_NODE_TYPE);
        SoSubNode.SO__NODE_INIT_CLASS(SoVRMLAnchor.class, "VRMLAnchor", SoVRMLParent.class);
        //SoVRMLAnchor::fetchurlcb = NULL;
        //SoVRMLAnchor::userdata = NULL;
    }
}
