/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.nodes.SoInfo;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLParentP implements Destroyable {

  public boolean childlistvalid;
  public SoFieldSensor addsensor; //ptr
  public SoFieldSensor removesensor; //ptr
  
private  static SoInfo nullnode; // ptr
  
public void lockChildList() {
	// TODO Auto-generated method stub
	
}
public void unlockChildList() {
	// TODO Auto-generated method stub
	
}

  public static SoInfo getNullNode() {
    if (SoVRMLParentP.nullnode == null) {
      SoVRMLParentP.nullnode = new SoInfo();
      SoVRMLParentP.nullnode.ref();
      SoVRMLParentP.nullnode.setName("SoVRMLParent::nullnode");
    }
    return SoVRMLParentP.nullnode;
  }
@Override
public void destructor() {
	// TODO Auto-generated method stub
	
}

}
