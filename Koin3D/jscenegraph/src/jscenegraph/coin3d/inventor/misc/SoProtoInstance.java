/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yves Boyadjian
 *
 */
public class SoProtoInstance extends SoNode {

  public
  static SoType       getClassTypeId()        /* Returns class type id */
  { return classTypeId;  }
  public  SoType      getTypeId()      /* Returns type id      */
  {
    return classTypeId;
  }

  static SoType classTypeId = SoType.badType();

  // *************************************************************************

  //typedef SbHash<const SoNode *, SoProtoInstance *> SoNode2SoProtoInstanceMap;

  static final Map<SoNode,SoProtoInstance> protoinstance_dict = new HashMap<>();
  static Object protoinstance_mutex;

  // *************************************************************************

	private SoProtoInstanceP pimpl; //ptr

/*!
  Constructor.
*/
public SoProtoInstance(SoProto proto,
                                 SoFieldData deffielddata)
{
  pimpl = new SoProtoInstanceP();
  pimpl.fielddata = new SoFieldData();
  pimpl.protodef = proto;
  if (proto != null) proto.ref();
  this.copyFieldData(deffielddata);
}

// doc in parent
public SoFieldData getFieldData()
  {
    return pimpl.fielddata;
  }

/*!
  Returns the PROTO definition for this instance.
*/
  public SoProto
  getProtoDefinition()
  {
    return pimpl.protodef;
  }

/*!
  Given root node \a rootnode, return the PROTO instance, or NULL if
  \a rootnode is not a PROTO instance root node.
*/
  public static SoProtoInstance findProtoInstance(final SoNode rootnode)
  {
    final SoProtoInstance[] ret = new SoProtoInstance[1];
    //CC_MUTEX_LOCK(protoinstance_mutex); TODO
    if ((ret[0] = protoinstance_dict.get(rootnode)) == null) { ret[0] = null; }
    //CC_MUTEX_UNLOCK(protoinstance_mutex);
    return ret[0];
  }

private void copyFieldData(SoFieldData src) {
  int n = src.getNumFields();
  SoFieldContainer.initCopyDict();
  for (int i = 0; i < n; i++) {
    SoField f = src.getField(pimpl.protodef, i);
    SoField cp = (SoField) f.getTypeId().createInstance();
    cp.setContainer(this);
    pimpl.fielddata.addField(SoProtoInstance.class,this, src.getFieldName(i).getString(), cp);
    if (f.getFieldType() == SoField.FieldType.NORMAL_FIELD.getValue() ||
            f.getFieldType() == SoField.FieldType.EXPOSED_FIELD.getValue()) {
      cp.copyFrom(f);
      cp.fixCopy(true);
    }
    cp.setFieldType(f.getFieldType());
    cp.setDefault(f.isDefault());
  }
  SoFieldContainer.copyDone();
}


  /*!
    Sets the root node for this instance.
  */
public void setRootNode(SoNode root) {
  //CC_MUTEX_LOCK(protoinstance_mutex); TODO
  if (pimpl.root != null) {
    protoinstance_dict.remove(pimpl.root);
  }
  pimpl.root = root;
  if (root != null) {
    protoinstance_dict.put(root, this);
  }
  //CC_MUTEX_UNLOCK(protoinstance_mutex);
}

/*!
  Returns the instance root node.
*/
  public SoNode getRootNode()
  {
    return pimpl.root;
  }

// doc in parent
  public static void initClass()
  {
    /* Make sure we only initialize once. */
    assert(SoProtoInstance.classTypeId.operator_equal_equal(SoType.badType()));
    /* Make sure superclass gets initialized before subclass. */
    assert(SoNode.getClassTypeId().operator_not_equal(SoType.badType()));

    /* Set up entry in the type system. */
    SoProtoInstance.classTypeId =
            SoType.createType(SoNode.getClassTypeId(),
          new SbName("ProtoInstance"),
          null,
                    (short)(SoNode.nextActionMethodIndex++));

    //protoinstance_dict = new SoNode2SoProtoInstanceMap;
    //CC_MUTEX_CONSTRUCT(protoinstance_mutex);
    //coin_atexit((coin_atexit_f*) SoProtoInstance::cleanupClass, CC_ATEXIT_NORMAL);
  }
}
