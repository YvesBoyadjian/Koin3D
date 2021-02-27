package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVRMLCollision extends SoVRMLGroup {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLCollision.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLCollision.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLCollision.class); }


    public final SoSFBool collide = new SoSFBool();
    public final SoSFNode proxy = new SoSFNode();
    public final SoSFTime collideTime = new SoSFTime();

/*!
  Constructor.
*/
    public SoVRMLCollision()
    {
        this.commonConstructor();
    }

/*!
  Constructor. \a numchildren is the expected number of children.
*/
    public SoVRMLCollision(int numchildren) {
  super(numchildren);

        this.commonConstructor();
    }

    public void commonConstructor()
    {
        nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLCollision.class);

        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(collide,"collide", (true));
        nodeHeader.SO_VRMLNODE_ADD_FIELD(proxy,"proxy", (null));

        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(collideTime,"collideTime");
    }

/*!
  \copydetails SoNode::initClass(void)
*/
    public static void initClass() // static
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoVRMLCollision.class,"VRMLCollision",SoVRMLGroup.class);// SO_VRML97_NODE_TYPE);
    }
}
