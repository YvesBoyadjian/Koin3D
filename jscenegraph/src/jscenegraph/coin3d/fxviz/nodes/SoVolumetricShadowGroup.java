package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

public class SoVolumetricShadowGroup extends SoShadowGroup {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVolumetricShadowGroup.class,this);

    public static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVolumetricShadowGroup.class);  }

    public SoType getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }

    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }

    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVolumetricShadowGroup.class); }

    public final SoSFBool isVolumetricActive = new SoSFBool();

    public static void init() {
        SoVolumetricShadowGroup.initClass();
    }

    /*!
    Default constructor.
  */
    public SoVolumetricShadowGroup()
    {

        nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoVolumetricShadowGroup.class);

        nodeHeader.SO_NODE_ADD_FIELD(isVolumetricActive,"isVolumetricActive", (true));
    }

    SoShadowGroupP createPimpl() {
        return new SoVolumetricShadowGroupP(this);
    }


    public static void
    initClass()
    //
    ////////////////////////////////////////////////////////////////////////
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoVolumetricShadowGroup.class, "VolumetricShadowGroup", SoShadowGroup.class);
    }

}
