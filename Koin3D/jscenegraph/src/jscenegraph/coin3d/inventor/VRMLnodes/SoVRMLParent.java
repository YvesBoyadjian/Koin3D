/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLParent extends SoGroup {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLParent.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLParent.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLParent.class); }    
	  
  public final SoMFNode children = new SoMFNode();
  
  protected final SoMFNode addChildren = new SoMFNode();
  protected final SoMFNode removeChildren = new SoMFNode();

  private SoVRMLParentP pimpl; // ptr

	  public SoVRMLParent() {
		  SoVRMLParent_commonConstructor();
	  }
	  
	  public SoVRMLParent(int numChildren) {
		  super( numChildren);
		  SoVRMLParent_commonConstructor();
	  }
	  
private void SoVRMLParent_commonConstructor()
{
  pimpl = new SoVRMLParentP();
  pimpl.childlistvalid = false;

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLParent.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(children,"children");
  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(addChildren, "addChildren");
  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(removeChildren, "removeChildren");

  pimpl.addsensor = new SoFieldSensor(SoVRMLParent::field_sensor_cb, this);
  pimpl.removesensor = new SoFieldSensor(SoVRMLParent::field_sensor_cb, this);
  pimpl.addsensor.attach(this.addChildren);
  pimpl.removesensor.attach(this.removeChildren);

  // HACK WARNING: All children of this node are stored in the
  // children field. Avoid double notifications (because of
  // notification through SoChildList) be reallocating the SoChildList
  // with a NULL-parent here. SoGroup will have allocated an
  // SoChildList in its constructor when we get here.
  Destroyable.delete( super.children);
  super.children = new SoChildList(null);
}

// add children in addChildren field
public void processAddChildren()
{
  int n = this.addChildren.getNum();
  SoNode[][] nodes = this.addChildren.getValues(0);
  for (int i = 0; i < n; i++) {
    SoNode node = nodes[i][0];
    if (this.findChild(node) < 0) {
      this.addChild((SoNode)node);
    }
  }
}

// remove children in removeChildren field
public void processRemoveChildren()
{
  int n = this.removeChildren.getNum();
  SoNode[][] nodes = this.removeChildren.getValues(0);
  for (int i = 0; i < n; i++) {
    int idx = this.findChild(nodes[i][0]);
    if (idx >= 0) {
      this.removeChild(idx);
    }
  }
}



	  // callback from the addChildren/removeChildren sensors
private static void field_sensor_cb(Object data, SoSensor sensor)
{
  SoVRMLParent thisp = (SoVRMLParent) data;
  if (sensor == thisp.pimpl.addsensor) {
    thisp.processAddChildren();
  }
  else {
    assert(sensor == thisp.pimpl.removesensor);
    thisp.processRemoveChildren();
  }
}

// Doc in parent
public boolean readInstance(SoInput in,
                           short flags)
{
  boolean oldnot = this.children.enableNotify(false);
  // call SoNode::readInstance(), not SoGroup::readInstance() since
  // this node stores all children in the children field.
  boolean ret = SoNode_readInstance(in, flags);
  if (oldnot) this.children.enableNotify(true);
  pimpl.childlistvalid = false;
  return ret;
}


// Doc in parent
public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f == this.children) {
    pimpl.childlistvalid = false;
  }
  super.notify(list);
}

	  // Doc in parent
public SoChildList getChildren()
{
  if (!pimpl.childlistvalid) {
    // this is not 100% thread safe. The assumption is that no nodes
    // will be added or removed while a scene graph is being
    // traversed. For Coin, this is an ok assumption.
    pimpl.lockChildList();
    // test again after we've locked
    if (!pimpl.childlistvalid) {

      SoVRMLParent.updateChildList(this.children.getValues(0),
                                    this.children.getNum(),
                                    super.children);
      pimpl.childlistvalid = true;
    }
    pimpl.unlockChildList();
  }
  return super.children;
}

	  /*!

  A convenience method that can be used to update \a cl to match the
  list of nodes in \a nodes.

*/
public static void updateChildList(final SoNode[][] nodes,
                              final int numnodes,
                              SoChildList cl)
{
  int i;
  boolean needcopy = true;
  int numChildren = cl.getLength();
  if (numChildren != 0 && (numnodes == numChildren)) {
    SoNode[][] clarr = (SoNode[][]) cl.getArrayPtr();
    for (i = 0; i < numnodes; i++) {
      // if the MFNode contains NULL values, we insert a dummy node
      // (of type SoInfo). This is to simplify the traversal code, and
      // to make it easier to check if the SoChildList is up-to-date
      if (clarr[i] == null) {
        if (nodes[i][0] != SoVRMLParentP.getNullNode()) break;
      }
      else {
        if (clarr[i][0] != nodes[i][0]) break;
      }
    }
    if (i == numnodes) needcopy = false;
  }
  if (needcopy) {
    cl.truncate(0);
    for (i = 0; i < numnodes; i++) {
      if (nodes[i][0] != null) {
        cl.append((SoNode) nodes[i][0]);
      }
      else {
        // insert a dummy SoInfo node
        cl.append(SoVRMLParentP.getNullNode());
      }
    }
  }
}

/*!

  A convenience method that is used to sync the nodes in \a cl with
  all nodes in SoSFNode fields in \a nodewithsfnode.

*/
public void updateChildList(SoNode nodewithsfnode,
                              SoChildList cl)
{
  final SbList <SoNode[]> nodelist = new SbList<>();

  SoFieldData fd = nodewithsfnode.getFieldData();
  int n = fd.getNumFields();

  SoType sosftype = SoSFNode.getClassTypeId(SoSFNode.class);
  for (int i = 0; i < n; i++) {
    SoField f = fd.getField(nodewithsfnode, i);
    if (f.getTypeId().operator_equal_equal(sosftype)) {
      SoNode node = ((SoSFNode) f).getValue();
      SoNode[] elem = new SoNode[1];
      elem[0] = node;
      if (node != null) nodelist.append(/*node*/elem);
    }
  }
  SoNode[][] array = new SoNode[nodelist.getLength()][];
  SoVRMLParent.updateChildList(nodelist.getArrayPtr(array),
                                nodelist.getLength(),
                                cl);
}


// Doc in parent
public int getNumChildren()
{
  return this.children.getNumNodes();
}

	  
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLParent.class, SO_VRML97_NODE_TYPE);
  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLParent.class, "VRMLParent", SoGroup.class);
  // used when the 'children' field contains NULL-nodes
  //coin_atexit((coin_atexit_f*) vrmlparent_cleanup, CC_ATEXIT_NORMAL);
}

}
