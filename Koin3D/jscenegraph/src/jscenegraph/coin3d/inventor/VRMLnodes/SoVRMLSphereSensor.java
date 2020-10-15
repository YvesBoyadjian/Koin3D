package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVRMLSphereSensor extends SoVRMLDragSensor {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLSphereSensor.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLSphereSensor.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLSphereSensor.class); }

    public final SoSFRotation offset = new SoSFRotation();
    public final SoSFRotation rotation_changed = new SoSFRotation();

/*!
  Constructor.
*/
    public SoVRMLSphereSensor()
    {
        nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLSphereSensor.class);

        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(offset,"offset", new SbRotation(0.0f, 1.0f, 0.0f, 0.0f));
        nodeHeader.SO_VRMLNODE_ADD_EVENT_OUT(rotation_changed,"rotation_changed");

        //this->sphereproj = new SbSpherePlaneProjector;
    }

/*!
  \copydetails SoNode::initClass(void)
*/
    public static void initClass()
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoVRMLSphereSensor.class, "VRMLSphereSensor", SoVRMLDragSensor.class);//SO_VRML97_NODE_TYPE);
    }
}
