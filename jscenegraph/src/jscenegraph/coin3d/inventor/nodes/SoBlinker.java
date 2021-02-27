/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoBlinker extends SoSwitch {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBlinker.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoBlinker.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoBlinker.class); }    	  	
	
	  public final SoSFFloat speed = new SoSFFloat();
	  public final SoSFBool on = new SoSFBool();

	  /*!
	  Constructor.
	*/
	public SoBlinker()
	{
//	  pimpl = new SoBlinkerP(this); TODO
//
//	  pimpl.calculator = new SoCalculator;
//	  pimpl.calculator->ref();
//	  pimpl.calculator->a.connectFrom(&this->on);
//	  pimpl.calculator->b.connectFrom(&this->speed);
//	  pimpl.calculator->expression = "oa = ((b > 0) && (a != 0)) ? 1.0 : 0.0;";
//	  
//	  pimpl.counter = new SoTimeCounter;
//	  pimpl.counter->ref();
//	  pimpl.counter->min = SO_SWITCH_NONE;
//	  pimpl.counter->max = SO_SWITCH_NONE;
//	  pimpl.counter->frequency.connectFrom(&this->speed);
//	  pimpl.counter->on.connectFrom(&pimpl.calculator->oa);
//	  pimpl.whichChildSensor = 
//	    new SoOneShotSensor(SoBlinkerP::whichChildCB, PRIVATE(this));
//	  pimpl.whichChildSensor->setPriority(1);
//	  pimpl.whichvalue = SO_SWITCH_NONE;


	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoBlinker.class);

	  nodeHeader.SO_NODE_ADD_FIELD(speed,"speed", (1.0f));
	  nodeHeader.SO_NODE_ADD_FIELD(on,"on", (true));
	  
	  //this.whichChild.connectFrom(pimpl.counter.output, true); TODO
	}

	/*!
	  Destructor.
	*/
	public void destructor()
	{
//	  Destroyable.delete( pimpl.whichChildSensor ); TODO
//	  pimpl.counter.unref();
//	  pimpl.calculator.unref();
//	  Destroyable.delete( pimpl );
	}

	/*!
	  \copybrief SoBase::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoBlinker, SO_FROM_INVENTOR_1);
		SoSubNode.SO__NODE_INIT_CLASS(SoBlinker.class, "Blinker", SoSwitch.class);
	}


}
