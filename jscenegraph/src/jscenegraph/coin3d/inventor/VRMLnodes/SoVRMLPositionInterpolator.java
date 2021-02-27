package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.engines.SoSubNodeEngine;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.engines.SoSubEngine;
import jscenegraph.database.inventor.fields.*;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.SbVec3fArray;

public class SoVRMLPositionInterpolator extends SoVRMLInterpolator {

    private final SoSubNodeEngine nodeHeader = SoSubNodeEngine.SO_NODEENGINE_HEADER(SoVRMLPositionInterpolator.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLPositionInterpolator.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLPositionInterpolator.class); }

    @Override
    public SoEngineOutputData getOutputData() {
        return nodeHeader.getOutputData();
    }

    public static SoEngineOutputData[] getOutputDataPtr()
    {
        return SoSubNodeEngine.getOutputDataPtr(SoVRMLPositionInterpolator.class);
    }

    final public SoMFVec3f keyValue = new SoMFVec3f();
    final public SoEngineOutput value_changed = new SoEngineOutput(); // (SoSFRotaion)

/*!
  Constructor.
*/
    public SoVRMLPositionInterpolator()
    {
        nodeHeader.SO_NODEENGINE_INTERNAL_CONSTRUCTOR(SoVRMLPositionInterpolator.class);

        nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(keyValue,"keyValue");
        nodeHeader.SO_NODEENGINE_ADD_OUTPUT(value_changed,"value_changed", SoSFVec3f.class);
    }

    @Override
    public void evaluate() {
        final float[] interp = new float[1];
        int idx = this.getKeyValueIndex(interp, this.keyValue.getNum());
        if (idx < 0) return;

  final SbVec3fArray v = this.keyValue.getValues(0);

        final SbVec3f v0 = v.getO(idx);
        if (interp[0] > 0.0f) {
            SbVec3f v1 = v.getO(idx+1);
            v0.setValue(v0.operator_add((v1.operator_minus(v0)).operator_mul(interp[0])));
        }
        SoSubEngine.SO_ENGINE_OUTPUT(value_changed, SoSFVec3f.class, (o) -> ((SoSFVec3f)o).setValue(v0));
    }

/*!
  \copydetails SoNode::initClass(void)
*/
    public static void initClass() // static
    {
        //SO_NODEENGINE_INTERNAL_INIT_CLASS(SoVRMLPositionInterpolator);
        SoSubNodeEngine.SO_NODEENGINE_INTERNAL_INIT_CLASS(SoVRMLPositionInterpolator.class,"VRMLPositionInterpolator",SoVRMLInterpolator.class);
    }
}
