package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.*;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVRMLProximitySensor extends SoNode {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLProximitySensor.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLProximitySensor.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLProximitySensor.class); }

    public final SoSFVec3f center = new SoSFVec3f();
    public final SoSFVec3f size = new SoSFVec3f();
    public final SoSFBool enabled = new SoSFBool();

    public final SoSFBool isActive = new SoSFBool();
    public final SoSFVec3f position_changed = new SoSFVec3f();
    public final SoSFRotation orientation_changed = new SoSFRotation();
    public final SoSFTime enterTime = new SoSFTime();
    public final SoSFTime exitTime = new SoSFTime();

/*!
  Constructor.
*/
    public SoVRMLProximitySensor()
    {
        nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLProximitySensor.class);

        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(center,"center", new SbVec3f(0.0f, 0.0f, 0.0f));
        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(size,"size", new SbVec3f(0.0f, 0.0f, 0.0f));
        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(enabled,"enabled", (true));

        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(isActive,"isActive");
        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(position_changed,"position_changed");
        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(orientation_changed,"orientation_changed");
        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(enterTime,"enterTime");
        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(exitTime,"exitTime");

        // initialize eventOut values that we might read
//        this->isActive = FALSE; TODO
//        this->position_changed = SbVec3f(0.0f, 0.0f, 0.0f);
//        this->orientation_changed = SbRotation();
    }


/*!
  \copydetails SoNode::initClass(void)
*/
    public static void initClass()
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoVRMLProximitySensor.class,"VRMLProximitySensor",SoNode.class);// SO_VRML97_NODE_TYPE);
    }

}
