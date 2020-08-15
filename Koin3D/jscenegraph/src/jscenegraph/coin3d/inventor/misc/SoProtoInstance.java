/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoProtoInstance extends SoNode {

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

private void copyFieldData(SoFieldData deffielddata) {
	// TODO Auto-generated method stub
	
}

public void setRootNode(SoNode createInstanceRoot) {
	// TODO Auto-generated method stub
	
}
}
