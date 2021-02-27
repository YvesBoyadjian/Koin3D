/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoRotation;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoRotor extends SoRotation {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoRotor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoRotor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoRotor.class); }
	  
	
	  public final SoSFFloat speed = new SoSFFloat();
	  public final SoSFBool on = new SoSFBool();

		  SoRotorP pimpl;
	  

/*!
  Constructor.
*/
public SoRotor()
{
  pimpl = new SoRotorP(this);

  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoRotor.class);

  nodeHeader.SO_NODE_ADD_FIELD(speed,"speed", (1.0f));
  nodeHeader.SO_NODE_ADD_FIELD(on,"on", (true));

  SoField f = SoDB.getGlobalField("realTime");
  pimpl.rtfieldsensor = new SoFieldSensor(SoRotorP::rtFieldSensorCB, this);
  pimpl.rtfieldsensor.attach(f);
  pimpl.rtfieldsensor.setPriority(0);
  pimpl.onfieldsensor = new SoFieldSensor(SoRotorP::fieldSensorCB, this);
  pimpl.onfieldsensor.setPriority(0);
  pimpl.onfieldsensor.attach(this.on);
  pimpl.speedfieldsensor = new SoFieldSensor(SoRotorP::fieldSensorCB, this);
  pimpl.speedfieldsensor.setPriority(0);
  pimpl.speedfieldsensor.attach(this.speed);
  pimpl.rotfieldsensor = new SoFieldSensor(SoRotorP::fieldSensorCB, this);
  pimpl.rotfieldsensor.attach(this.rotation);
  pimpl.rotfieldsensor.setPriority(0);

  pimpl.starttime.copyFrom(SbTime.zero());
  pimpl.startangle = this.rotation.getValue(pimpl.startaxis);
}


/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete( pimpl.rotfieldsensor );
  Destroyable.delete( pimpl.rtfieldsensor );
  Destroyable.delete( pimpl.onfieldsensor );
  Destroyable.delete( pimpl.speedfieldsensor );
  //Destroyable.delete( pimpl ); TODO
}

// Doc from parent.
/*!
  \copybrief SoBase::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoRotor.class, SO_FROM_INVENTOR_1);
	SO__NODE_INIT_CLASS(SoRotor.class, "Rotor", SoRotation.class);
}

	  
}
