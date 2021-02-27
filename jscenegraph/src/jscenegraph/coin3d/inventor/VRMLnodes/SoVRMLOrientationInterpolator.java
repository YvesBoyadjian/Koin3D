/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.engines.SoSubNodeEngine;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.engines.SoSubEngine;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFRotation;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.SbRotationArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLOrientationInterpolator extends SoVRMLInterpolator {
	
	private final SoSubNodeEngine nodeHeader = SoSubNodeEngine.SO_NODEENGINE_HEADER(SoVRMLOrientationInterpolator.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLOrientationInterpolator.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLOrientationInterpolator.class); }    	  	
	
	@Override
	public SoEngineOutputData getOutputData() {
		return nodeHeader.getOutputData();
	}

	public static SoEngineOutputData[] getOutputDataPtr() 
	{ 
	  return SoSubNodeEngine.getOutputDataPtr(SoVRMLOrientationInterpolator.class); 
	} 
	

	  final public SoMFRotation keyValue = new SoMFRotation();
	  final public SoEngineOutput value_changed = new SoEngineOutput(); // (SoSFRotaion)


/*!
  Constructor.
*/
public SoVRMLOrientationInterpolator()
{
  nodeHeader.SO_NODEENGINE_INTERNAL_CONSTRUCTOR(SoVRMLOrientationInterpolator.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(keyValue,"keyValue");
  nodeHeader.SO_NODEENGINE_ADD_OUTPUT(value_changed,"value_changed", SoSFRotation.class);
}

	@Override
	public void evaluate() {
		  final float[] interp = new float[1];
		  int idx = this.getKeyValueIndex(interp, this.keyValue.getNum());
		  if (idx < 0) return;

		  SbRotationArray v = this.keyValue.getValues(0);
		  final SbRotation v0 = new SbRotation( v.getO(idx));
		  if (interp[0] > 0.0f) {
		    final SbRotation v1 = new SbRotation(v.getO(idx+1));
		    v0.copyFrom(SbRotation.slerp(v0, v1, interp[0]));
		  }
		  SoSubEngine.SO_ENGINE_OUTPUT(value_changed, SoSFRotation.class,(o) -> ((SoSFRotation)o).setValue(v0));
	}

/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
	SoSubNodeEngine.SO_NODEENGINE_INTERNAL_INIT_CLASS(SoVRMLOrientationInterpolator.class,"VRMLOrientationInterpolator",SoVRMLInterpolator.class);
}

}
