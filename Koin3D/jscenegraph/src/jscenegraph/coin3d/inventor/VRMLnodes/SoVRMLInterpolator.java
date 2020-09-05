/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.engines.SoNodeEngine;
import jscenegraph.coin3d.inventor.engines.SoSubNodeEngine;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.FloatArray;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoVRMLInterpolator extends SoNodeEngine {
	
	private final SoSubNodeEngine nodeHeader = SoSubNodeEngine.SO_NODEENGINE_ABSTRACT_HEADER(SoVRMLInterpolator.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLInterpolator.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLInterpolator.class); }    	  	
	
	@Override
	public SoEngineOutputData getOutputData() {
		return nodeHeader.getOutputData();
	}

	public static SoEngineOutputData[] getOutputDataPtr() 
	{ 
	  return SoSubNodeEngine.getOutputDataPtr(SoVRMLInterpolator.class); 
	} 

	public final
		  SoMFFloat key = new SoMFFloat();
	public final SoSFFloat set_fraction = new SoSFFloat();


public SoVRMLInterpolator() // protected
{
  nodeHeader.SO_NODEENGINE_CONSTRUCTOR(SoVRMLInterpolator.class);

  nodeHeader.SO_VRMLNODE_ADD_EVENT_IN(set_fraction,"set_fraction");

  // initialize set_fraction to some value, since if set_fraction is
  // never set, we'll attempt to read an unitialized value when the
  // interpolator is destructed (all engines evaluates when
  // destructed)
  this.set_fraction.enableNotify(false);
  this.set_fraction.setValue(0.0f);
  this.set_fraction.enableNotify(true);
  
  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(key,"key");
}

/*!
  \COININTERNAL
*/
protected int getKeyValueIndex(final float[] interp, int numvalues)
{
  float fraction = this.set_fraction.getValue();
  int n = this.key.getNum();
  if (n == 0 || numvalues == 0) return -1;

  FloatArray t = this.key.getValues(0); 
  for (int i = 0; i < Math.min(n, numvalues); i++) {
    if (fraction < t.get(i)) {
      if (i == 0) {
        interp[0] = 0.0f;
        return 0;
      }
      else {
        float delta = t.get(i) - t.get(i-1);
        if (delta > 0.0f) {
          interp[0] = (fraction - t.get(i-1)) / delta;
        }
        else interp[0] = 0.0f;
      }
      return i-1;
    }
  }
  interp[0] = 0.0f;
  return Math.min(numvalues,n)-1;
}

	
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass() // static
{
	SoSubNodeEngine.SO_NODEENGINE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLInterpolator.class,"VRMLInterpolator",SoNodeEngine.class);
}
	
}
