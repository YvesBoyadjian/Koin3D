package jscenegraph.freecad;

import jscenegraph.coin3d.inventor.engines.SoInterpolate;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.*;
import jscenegraph.database.inventor.fields.*;

public class SoFCMaterialEngine extends SoEngine {
    //SO_ENGINE_HEADER(SoFCMaterialEngine);


    public
    static SoType getClassTypeId() { return classTypeId; }
    	public    SoType      getTypeId()  /* Returns type id   */
	{
		return classTypeId;
	}
	  public
	     SoFieldData          getFieldData()  {
		  return inputData[0];
	  }
	public    SoEngineOutputData   getOutputData() {
		return outputData[0];
	}
    public
    static SoFieldData[]         getInputDataPtr()
    { return ( SoFieldData[])inputData; }
    public   static SoEngineOutputData[] getOutputDataPtr()
    { return ( SoEngineOutputData[])outputData; }
    private
    static SoType       classTypeId;    /* Type id              */
    //private  static boolean       firstInstance = true;  /* True for first ctor call */
    private  static final SoFieldData[]  inputData = new SoFieldData[1];     /* Info on input fields */
    private  static final SoEngineOutputData[]  outputData = new SoEngineOutputData[1];            /* Info on outputs */
    private  static final SoFieldData[][]    parentInputData = new SoFieldData[1][];      /* parent's fields */
    private  static final SoEngineOutputData[][] parentOutputData = new SoEngineOutputData[1][];

    // SO_ENGINE_ABSTRACT_HEADER

    public final SoMFColor diffuseColor = new SoMFColor();
    public final SoEngineOutput trigger = new SoEngineOutput();


    public SoFCMaterialEngine()
    {
        super();
        engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoFCMaterialEngine.class,this);

        engineHeader.SO_ENGINE_CONSTRUCTOR(SoFCMaterialEngine.class,inputData, outputData, parentInputData[0], parentOutputData[0]);

        engineHeader.SO_ENGINE_ADD_MINPUT(diffuseColor,"diffuseColor", (new SbColor(0.0f, 0.0f, 0.0f)));
        engineHeader.SO_ENGINE_ADD_OUTPUT(trigger,"trigger", SoSFBool.class);
    }

    public void inputChanged(SoField field)
    {
        engineHeader.SO_ENGINE_OUTPUT(trigger, SoSFBool.class, (o)-> ((SoSFBool)o).setValue(true));
    }

    @Override
    protected void evaluate() {
        // do nothing here
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//This initializes the SoInterpolate class.
//
//Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        //SO_ENGINE_INTERNAL_INIT_ABSTRACT_CLASS(SoInterpolate);
        classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoFCMaterialEngine.class, "FCMaterialEngine", SoEngine.class, parentInputData, parentOutputData);
    }
}
