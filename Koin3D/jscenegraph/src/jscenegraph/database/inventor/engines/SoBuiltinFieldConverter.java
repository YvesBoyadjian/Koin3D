/**
 * 
 */
package jscenegraph.database.inventor.engines;

import static jscenegraph.database.inventor.misc.SoBasic.SO__CONCAT;

import java.lang.reflect.InvocationTargetException;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbMatrixd;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbRotationd;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFDouble;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFMatrix;
import jscenegraph.database.inventor.fields.SoMFMatrixd;
import jscenegraph.database.inventor.fields.SoMFRotation;
import jscenegraph.database.inventor.fields.SoMFRotationd;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoMFTime;
import jscenegraph.database.inventor.fields.SoMField;
import jscenegraph.database.inventor.fields.SoSFDouble;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFMatrix;
import jscenegraph.database.inventor.fields.SoSFMatrixd;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFRotationd;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.fields.SoSField;

/**
 * @author Yves
 *
 */
public class SoBuiltinFieldConverter extends SoFieldConverter {

	private SoSubEngine engineHeader;

	// SO_ENGINE_ABSTRACT_HEADER

	public static SoType getClassTypeId() {
		return classTypeId;
	}

	public SoType getTypeId() /* Returns type id */
	{
		return classTypeId;
	}

	public SoFieldData getFieldData() {
		// return inputData[0];
		return myInputData;
	}

	public SoEngineOutputData getOutputData() {
		// return outputData[0];
		return myOutputData;
	}

	public static SoFieldData[] getInputDataPtr() {
		return (SoFieldData[]) inputData;
	}

	public static SoEngineOutputData[] getOutputDataPtr() {
		return (SoEngineOutputData[]) outputData;
	}

	private static SoType classTypeId; /* Type id */
	// private static boolean firstInstance = true; /* True for first ctor call
	// */
	private static final SoFieldData[] inputData = new SoFieldData[1]; /*
																		 * Info
																		 * on
																		 * input
																		 * fields
																		 */
	private static final SoEngineOutputData[] outputData = new SoEngineOutputData[1]; /*
																						 * Info
																						 * on
																						 * outputs
																						 */
	private static final SoFieldData[][] parentInputData = new SoFieldData[1][]; /*
																					 * parent
																					 * '
																					 * s
																					 * fields
																					 */
	private static final SoEngineOutputData[][] parentOutputData = new SoEngineOutputData[1][];

	// SO_ENGINE_ABSTRACT_HEADER

	  public static SoBuiltinFieldConverter createInstance() {
		  return new SoBuiltinFieldConverter();
	  }
	
	
	//
	// Maximum number of multi-valued field types, and maximum total
	// number of field types:
	//
	public final static int MAXMFIELDS = 1024;
	public final static int MAXFIELDS = MAXMFIELDS * 2;

	// Constants for all the field types; used so we can quickly switch()
	// on the field type at evaluate() time.
	enum TypeConst {
		MFBitMask(1), 
		MFBool(2), 
		MFColor(3), 
		MFDouble(4), 
		MFEnum(5), 
		MFFloat(6), 
		MFInt32(7), 
		MFMatrix(8), 
		MFMatrixd(9), 
		MFName(10), 
		MFNode(11), 
		MFPath(12), 
		MFPlane(13), 
		MFPlaned(14), 
		MFRotation(15), 
		MFRotationd(16), 
		MFShort(17), 
		MFString(18), 
		MFTime(19), 
		MFUInt32(20), 
		MFUShort(21), 
		MFVec2d(22), 
		MFVec2f(23), 
		MFVec3d(24), 
		MFVec3f(25), 
		MFVec4d(26), 
		MFVec4f(27), 
		SFBitMask(MAXMFIELDS + 1), 
		SFBool(MAXMFIELDS + 2), 
		SFColor(MAXMFIELDS + 3), 
		SFDouble(MAXMFIELDS	+ 4), 
		SFEnum(MAXMFIELDS + 5), 
		SFFloat(MAXMFIELDS + 6), 
		SFInt32(MAXMFIELDS + 7), 
		SFMatrix(MAXMFIELDS + 8), 
		SFMatrixd(MAXMFIELDS+ 9), 
		SFName(MAXMFIELDS + 10), 
		SFNode(MAXMFIELDS+ 11), 
		SFPath(MAXMFIELDS + 12), 
		SFPlane(MAXMFIELDS + 13), 
		SFPlaned(MAXMFIELDS+ 14), 
		SFRotation(MAXMFIELDS+ 15), 
		SFRotationd(MAXMFIELDS+ 16), 
		SFShort(MAXMFIELDS+ 17), 
		SFString(MAXMFIELDS+ 18), 
		SFTime(MAXMFIELDS+ 19), 
		SFUInt32(MAXMFIELDS+ 20), 
		SFUShort(MAXMFIELDS+ 21), 
		SFVec2d(MAXMFIELDS+ 22), 
		SFVec2f(MAXMFIELDS+ 23), 
		SFVec3d(MAXMFIELDS+ 24), 
		SFVec3f(MAXMFIELDS+ 25), 
		SFVec4d(MAXMFIELDS+ 26), 
		SFVec4f(MAXMFIELDS+ 27), 
		BAD_TYPE(MAXMFIELDS+ 28);

		private int value;

		TypeConst(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	public SoField input;
	public SoEngineOutput output;

	private int inType, outType;

	private SoFieldData myInputData;
	private SoEngineOutputData myOutputData;

	public SoBuiltinFieldConverter() {
		engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoBuiltinFieldConverter.class, this);
		engineHeader.SO_ENGINE_CONSTRUCTOR(SoBuiltinFieldConverter.class, inputData, outputData, parentInputData[0],
				parentOutputData[0]);
		myInputData = new SoFieldData(inputData[0]);
		myOutputData = new SoEngineOutputData(outputData[0]);
	}

	private final boolean DECIDEIN(TypeConst klass, Object defaultValue, SoType type) {
		try {
			Class<? extends SoField> arg = SO__CONCAT("So", klass.name());
			if (type.equals(SO__CONCAT("So", klass.name()).getMethod("getClassTypeId", arg.getClass()).invoke(null,
					SO__CONCAT("So", klass.name())))) {
				inType = klass.getValue();
				if (input instanceof SoSField) {
					SoSField soSField = (SoSField<Object>) input;
					soSField.setValue(defaultValue);
				} else if (input instanceof SoMField) {
					SoMField soMField = (SoMField<Object>) input;
					soMField.setValue(defaultValue);
				}
				// SO__CONCAT("So",klass.name())input.setValue(defaultValue);
				return true;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			SoError.post("Class So" + klass.name() + " does not exists");
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Called by database with the type we're converting from. The input
	// field is built on the fly, based on the type. Also sets the
	// 'inType' integer so we can quickly decide what conversion to do
	// at evaluate() time.
	//
	// Use: internal

	/*
	 * (non-Javadoc)
	 * 
	 * @see jscenegraph.database.inventor.engines.SoFieldConverter#getInput(
	 * jscenegraph.database.inventor.SoType)
	 */
	@Override
	public SoField getInput(SoType type) {
		input = (SoField) type.createInstance();

		if (DECIDEIN(TypeConst.MFBitMask, (0), type)) {
		} else if (DECIDEIN(TypeConst.MFBool, (false), type)) {
		} else if (DECIDEIN(TypeConst.MFColor, new SbColor(0, 0, 0), type)) {
		} else if (DECIDEIN(TypeConst.MFDouble, (0), type)) {
		} else if (DECIDEIN(TypeConst.MFEnum, (0), type)) {
		} else if (DECIDEIN(TypeConst.MFFloat, (0f), type)) {
		} else if (DECIDEIN(TypeConst.MFInt32, (0), type)) {
		}
		// TODO else if
		// (DECIDEIN(TypeConst.MFMatrixd,(SbMatrixd.identity()),type)){}
		else if (DECIDEIN(TypeConst.MFMatrix, (SbMatrix.identity()), type)) {
		} else if (DECIDEIN(TypeConst.MFName, (""), type)) {
		} else if (DECIDEIN(TypeConst.MFNode, (null), type)) {
		} else if (DECIDEIN(TypeConst.MFPath, (null), type)) {
		} else if (DECIDEIN(TypeConst.MFPlane, (new SbPlane(new SbVec3f(0, 0, 0), 0)), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.MFPlaned,(SbPlaned(new
		// SbVec3d(0,0,0),0)),type)){}
		else if (DECIDEIN(TypeConst.MFRotation, (new SbRotation()), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.MFRotationd,(new
		// SbRotationd()),type)){}
		else if (DECIDEIN(TypeConst.MFShort, (0), type)) {
		} else if (DECIDEIN(TypeConst.MFString, (""), type)) {
		} else if (DECIDEIN(TypeConst.MFTime, (SbTime.zero()), type)) {
		} else if (DECIDEIN(TypeConst.MFUInt32, (0), type)) {
		} else if (DECIDEIN(TypeConst.MFUShort, (0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.MFVec2d, new SbVec2d(0,0),type)){}
		else if (DECIDEIN(TypeConst.MFVec2f, new SbVec2f(0, 0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.MFVec3d, new SbVec3d(0,0,0),type)){}
		else if (DECIDEIN(TypeConst.MFVec3f, new SbVec3f(0, 0, 0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.MFVec4d, new
		// SbVec4d(0,0,0,0),type)){}
		else if (DECIDEIN(TypeConst.MFVec4f, new SbVec4f(0, 0, 0, 0), type)) {
		} else if (DECIDEIN(TypeConst.SFBitMask, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFBool, (false), type)) {
		} else if (DECIDEIN(TypeConst.SFColor, new SbColor(0, 0, 0), type)) {
		} else if (DECIDEIN(TypeConst.SFDouble, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFEnum, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFFloat, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFInt32, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFMatrix, (SbMatrix.identity()), type)) {
		}
		// TODO else if
		// (DECIDEIN(TypeConst.SFMatrixd,(SbMatrixd.identity()),type)){}
		else if (DECIDEIN(TypeConst.SFName, (""), type)) {
		} else if (DECIDEIN(TypeConst.SFNode, (null), type)) {
		} else if (DECIDEIN(TypeConst.SFPath, (null), type)) {
		} else if (DECIDEIN(TypeConst.SFPlane, (new SbPlane(new SbVec3f(0, 0, 0), 0)), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.SFPlaned,(SbPlaned(new
		// SbVec3d(0,0,0),0)),type)){}
		else if (DECIDEIN(TypeConst.SFRotation, (new SbRotation()), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.SFRotationd,(new
		// SbRotationd()),type)){}
		else if (DECIDEIN(TypeConst.SFShort, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFString, (""), type)) {
		} else if (DECIDEIN(TypeConst.SFTime, (SbTime.zero()), type)) {
		} else if (DECIDEIN(TypeConst.SFUInt32, (0), type)) {
		} else if (DECIDEIN(TypeConst.SFUShort, (0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.SFVec2d, new SbVec2d(0,0),type)){}
		else if (DECIDEIN(TypeConst.SFVec2f, new SbVec2f(0, 0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.SFVec3d, new SbVec3d(0,0,0),type)){}
		else if (DECIDEIN(TypeConst.SFVec3f, new SbVec3f(0, 0, 0), type)) {
		}
		// TODO else if (DECIDEIN(TypeConst.SFVec4d, new
		// SbVec4d(0,0,0,0),type)){}
		else if (DECIDEIN(TypeConst.SFVec4f, new SbVec4f(0, 0, 0, 0), type)) {
		}
		// #ifdef DEBUG
		else {
			SoDebugError.post("(internal) SoBuiltinFieldConverter::getInput",
					"no input for type '" + type.getName().getString() + "'");
		}
		// #endif

		input.setContainer(this);
		myInputData.addField(this, "input", input);

		return input;
	}

	private final boolean DECIDEOUT(TypeConst klass, SoType type) {
		try {
			Class<? extends SoField> arg = SO__CONCAT("So", klass.name());
			if (type.equals(SO__CONCAT("So", klass.name()).getMethod("getClassTypeId", arg.getClass()).invoke(null,
					SO__CONCAT("So", klass.name())))) {
				outType = klass.getValue();
				return true;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			SoError.post("Class So" + klass.name() + " does not exists");
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Called by database with the type we're converting to. The engine
	// output is built on the fly, based on the type. Also sets the
	// 'outType' integer so we can quickly decide what conversion to do
	// at evaluate() time.
	//
	// Use: internal

	/*
	 * (non-Javadoc)
	 * 
	 * @see jscenegraph.database.inventor.engines.SoFieldConverter#getOutput(
	 * jscenegraph.database.inventor.SoType)
	 */
	@Override
	public SoEngineOutput getOutput(SoType type) {
		output = new SoEngineOutput();
		output.setContainer(this);
		myOutputData.addOutput(this, "output", output, type);

		if (DECIDEOUT(TypeConst.MFBitMask, type)) {
		} else if (DECIDEOUT(TypeConst.MFBool, type)) {
		} else if (DECIDEOUT(TypeConst.MFColor, type)) {
		} else if (DECIDEOUT(TypeConst.MFDouble, type)) {
		} else if (DECIDEOUT(TypeConst.MFEnum, type)) {
		} else if (DECIDEOUT(TypeConst.MFFloat, type)) {
		} else if (DECIDEOUT(TypeConst.MFInt32, type)) {
		} else if (DECIDEOUT(TypeConst.MFMatrix, type)) {
		} else if (DECIDEOUT(TypeConst.MFMatrixd, type)) {
		} else if (DECIDEOUT(TypeConst.MFName, type)) {
		} else if (DECIDEOUT(TypeConst.MFNode, type)) {
		} else if (DECIDEOUT(TypeConst.MFPath, type)) {
		} else if (DECIDEOUT(TypeConst.MFPlane, type)) {
		} else if (DECIDEOUT(TypeConst.MFPlaned, type)) {
		} else if (DECIDEOUT(TypeConst.MFRotation, type)) {
		} else if (DECIDEOUT(TypeConst.MFRotationd, type)) {
		} else if (DECIDEOUT(TypeConst.MFShort, type)) {
		} else if (DECIDEOUT(TypeConst.MFString, type)) {
		} else if (DECIDEOUT(TypeConst.MFTime, type)) {
		} else if (DECIDEOUT(TypeConst.MFUInt32, type)) {
		} else if (DECIDEOUT(TypeConst.MFUShort, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec2d, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec2f, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec3d, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec3f, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec4d, type)) {
		} else if (DECIDEOUT(TypeConst.MFVec4f, type)) {
		} else if (DECIDEOUT(TypeConst.SFBitMask, type)) {
		} else if (DECIDEOUT(TypeConst.SFBool, type)) {
		} else if (DECIDEOUT(TypeConst.SFColor, type)) {
		} else if (DECIDEOUT(TypeConst.SFDouble, type)) {
		} else if (DECIDEOUT(TypeConst.SFEnum, type)) {
		} else if (DECIDEOUT(TypeConst.SFFloat, type)) {
		} else if (DECIDEOUT(TypeConst.SFInt32, type)) {
		} else if (DECIDEOUT(TypeConst.SFMatrix, type)) {
		} else if (DECIDEOUT(TypeConst.SFMatrixd, type)) {
		} else if (DECIDEOUT(TypeConst.SFName, type)) {
		} else if (DECIDEOUT(TypeConst.SFNode, type)) {
		} else if (DECIDEOUT(TypeConst.SFPath, type)) {
		} else if (DECIDEOUT(TypeConst.SFPlane, type)) {
		} else if (DECIDEOUT(TypeConst.SFPlaned, type)) {
		} else if (DECIDEOUT(TypeConst.SFRotation, type)) {
		} else if (DECIDEOUT(TypeConst.SFRotationd, type)) {
		} else if (DECIDEOUT(TypeConst.SFShort, type)) {
		} else if (DECIDEOUT(TypeConst.SFString, type)) {
		} else if (DECIDEOUT(TypeConst.SFTime, type)) {
		} else if (DECIDEOUT(TypeConst.SFUInt32, type)) {
		} else if (DECIDEOUT(TypeConst.SFUShort, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec2d, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec2f, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec3d, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec3f, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec4d, type)) {
		} else if (DECIDEOUT(TypeConst.SFVec4f, type)) {
		}
		// #ifdef DEBUG
		else {
			SoDebugError.post("(internal) SoBuiltinFieldConverter::getOutput\n" + "no output for type '"
					+ type.getName().getString() + "'");
		}
		// #endif
		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jscenegraph.database.inventor.engines.SoEngine#evaluate()
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Write input to output, with a conversion in between.
	//
	// Use: private
	//

	@Override
	protected void evaluate() {
		// For efficiency and to reduce bloat, we don't use the standard
		// SO_ENGINE_OUTPUT macro:

		// #ifdef DEBUG
		if (input == null) {
			SoDebugError.post("(internal) SoBuiltinFieldConverter::evaluate", "NULL input field");
			return;
		}
		// #endif

		// We know that our engineOutput cannot be disabled, since nobody
		// but us has access to it. If we are connected from an
		// engineOutput and that engineOutput is disabled, we still want
		// to convert that value - this will happen the first time this
		// engine is evaluated after the output is disabled. After the
		// first time, notification will be blocked, so we don't have to
		// check for a disabled output here.

		for (int i = 0; i < output.getNumConnections(); i++) {
			SoField outField = output.operator_square_bracket(i);
			if (!outField.isReadOnly())
				doConversion(outField);
		}
	}

	// Macro for registering the single/multi to multi/single field
	// conversions:
	private static void REG1(String type) {
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoSF", type)),
					SoField.getClassTypeId(SO__CONCAT("SoMF", type)), getClassTypeId());
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoMF", type)),
					SoField.getClassTypeId(SO__CONCAT("SoSF", type)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REG1 failed for " + type); // java port
		}
	}

	// Macro for registering all of the to/from single/multi-valued
	// string conversions (eight for each type):
	private static void REGSTRING(String type) {
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoSF", type)),
					SoField.getClassTypeId(SoSFString.class), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoMF", type)),
					SoField.getClassTypeId(SoSFString.class), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}

		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoSF", type)),
					SoField.getClassTypeId(SoMFString.class), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoMF", type)),
					SoField.getClassTypeId(SoMFString.class), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}

		try {
			SoDB.addConverter(SoField.getClassTypeId(SoSFString.class),
					SoField.getClassTypeId(SO__CONCAT("SoSF", type)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SoSFString.class),
					SoField.getClassTypeId(SO__CONCAT("SoMF", type)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}

		try {
			SoDB.addConverter(SoField.getClassTypeId(SoMFString.class),
					SoField.getClassTypeId(SO__CONCAT("SoSF", type)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SoMFString.class),
					SoField.getClassTypeId(SO__CONCAT("SoMF", type)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGSTRING failed for " + type); // java port
		}
	}

	// Macro for other conversions (float to int32_t, etc):

	private static void REGHALF(String type1, String type2) {
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoSF", type1)),
					SoField.getClassTypeId(SO__CONCAT("SoSF", type2)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGHALF failed for " + type1 + " to " + type2); // java
																			// port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoMF", type1)),
					SoField.getClassTypeId(SO__CONCAT("SoSF", type2)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGHALF failed for " + type1 + " to " + type2); // java
																			// port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoSF", type1)),
					SoField.getClassTypeId(SO__CONCAT("SoMF", type2)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGHALF failed for " + type1 + " to " + type2); // java
																			// port
		}
		try {
			SoDB.addConverter(SoField.getClassTypeId(SO__CONCAT("SoMF", type1)),
					SoField.getClassTypeId(SO__CONCAT("SoMF", type2)), getClassTypeId());
		} catch (ClassNotFoundException e) {
			SoError.post("REGHALF failed for " + type1 + " to " + type2); // java
																			// port
		}
	}

	private static void REGCONV(String type1, String type2) {
		REGHALF(type1, type2);
		REGHALF(type2, type1);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// This initializes the built-in field converter class.
	//
	// Use: internal

	public static void initClass()
	//
	////////////////////////////////////////////////////////////////////////
	{
		classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoBuiltinFieldConverter.class, "BuiltinFieldConverter",
				SoFieldConverter.class, parentInputData, parentOutputData);
		classTypeId.makeInternal();

		// Cases for all the field types:
		REG1("BitMask");
		REG1("Bool");
		REG1("Color");
		REG1("Double");
		REG1("Enum");
		REG1("Float");
		REG1("Int32");
		REG1("Matrix");
		REG1("Matrixd");
		REG1("Name");
		REG1("Node");
		REG1("Path");
		REG1("Plane");
		REG1("Planed");
		REG1("Rotation");
		REG1("Rotationd");
		REG1("Short");
		REG1("String");
		REG1("Time");
		REG1("UInt32");
		REG1("UShort");
		REG1("Vec2f");
		REG1("Vec2d");
		REG1("Vec3f");
		REG1("Vec3d");
		REG1("Vec4f");
		REG1("Vec4d");
		// #undef REG1

		// All types except string:
		REGSTRING("BitMask");
		REGSTRING("Bool");
		REGSTRING("Color");
		REGSTRING("Double");
		REGSTRING("Enum");
		REGSTRING("Float");
		REGSTRING("Int32");
		REGSTRING("Matrix");
		REGSTRING("Matrixd");
		REGSTRING("Name");
		REGSTRING("Node");
		REGSTRING("Path");
		REGSTRING("Plane");
		REGSTRING("Planed");
		REGSTRING("Rotation");
		REGSTRING("Rotationd");
		REGSTRING("Short");
		REGSTRING("Time");
		REGSTRING("UInt32");
		REGSTRING("UShort");
		REGSTRING("Vec2f");
		REGSTRING("Vec2d");
		REGSTRING("Vec3f");
		REGSTRING("Vec3d");
		REGSTRING("Vec4f");
		REGSTRING("Vec4d");
		// #undef REGSTRING

		REGCONV("Bool", "Double");
		REGCONV("Bool", "Float");
		REGCONV("Bool", "Int32");
		REGCONV("Bool", "Short");
		REGCONV("Bool", "UInt32");
		REGCONV("Bool", "UShort");

		REGCONV("Color", "Vec3f");
		REGCONV("Color", "Vec3d");

		REGCONV("Double", "Int32");
		REGCONV("Double", "Short");
		REGCONV("Double", "UInt32");
		REGCONV("Double", "UShort");
		REGCONV("Double", "Float");

		REGCONV("Float", "Int32");
		REGCONV("Float", "Short");
		REGCONV("Float", "UInt32");
		REGCONV("Float", "UShort");

		REGCONV("Int32", "Short");
		REGCONV("Int32", "UInt32");
		REGCONV("Int32", "UShort");

		REGCONV("Short", "UInt32");
		REGCONV("Short", "UShort");

		REGCONV("UInt32", "UShort");

		REGCONV("Float", "Time");

		REGCONV("Matrix", "Rotation");
		REGCONV("Matrix", "Rotationd");
		REGCONV("Matrixd", "Rotation");
		REGCONV("Matrixd", "Rotationd");

		REGCONV("Matrix", "Matrixd");
		REGCONV("Plane", "Planed");
		REGCONV("Rotation", "Rotationd");
		REGCONV("Matrixd", "Matrix");
		REGCONV("Planed", "Plane");
		REGCONV("Rotationd", "Rotation");

		REGCONV("Vec2f", "Vec2d");
		REGCONV("Vec3f", "Vec3d");
		REGCONV("Vec4f", "Vec4d");
		REGCONV("Vec2d", "Vec2f");
		REGCONV("Vec3d", "Vec3f");
		REGCONV("Vec4d", "Vec4f");
	}

	private int CASE(TypeConst typeIn, TypeConst typeOut) {
		return typeIn.getValue() * MAXFIELDS + typeOut.getValue();
	}

	// This macro is for converting the single/multi fields into their
	// corresponding multi/single value fields.
	// In normal code, it looks like:
	// Single to Multi:
	// SoMField.setValue(SoSField.getValue())
	// Multi so Single:
	// if (MField.getNum() > 0) SoSField.setValue(SoMField[0])

	private TypeConst SO__CONCATT(String arg1, String arg2) {
		String str = arg1 + arg2;
		return TypeConst.valueOf(str);
	}

	private boolean CONV1(String type, final int inOutType, SoField outField) {
		if (inOutType == CASE(SO__CONCATT("SF", type), SO__CONCATT("MF", type))) {
			((/*
				 * SO__CONCAT ( "SoMF" , type)
				 */SoMField) outField).setValue(((/*
												 * SO__CONCAT ( "SoSF" , type)
												 */SoSField) input).getValue());
		}
		else if (inOutType == CASE(SO__CONCATT("MF", type), SO__CONCATT("SF", type))) {
			if (((SoMField) input).getNum() > 0) {
				((/*
					 * SO__CONCAT ( "SoSF" , type)
					 */SoSField) outField)
						.setValue(((/*
									 * SO__CONCAT ( "SoMF" , type)
									 */SoMField) input).operator_square_bracket(0));
			}
		}
		else {
			return false;
		}
		return true;
	}

	//
	// Conversions to/from a string for all field types. The eight cases
	// are:
	// Single to/from Single string:
	// Single to/from Multiple string:
	// Multi to/from Single string:
	// input.get(string); outField.set(string);
	// Multi to/from Multi string:
	// for (i = 0; i < ((SoMField *)input).getNum(); i++) {
	// (SoMField *)input.get1(i, string);
	// (SoMField *)outField.set1(i, string);
	//
	// Note: we must use the SF/MFString.setValue() routines and not just
	// plain set() in case there is whitespace in the string, since set()
	// takes file format, and in the file format strings with whitespace
	// must be quoted.
	//
	private boolean CONVSTR(String type, final int inOutType, SoField outField) {
		if (inOutType == CASE(SO__CONCATT("SF", type), TypeConst.SFString)
				|| inOutType == CASE(SO__CONCATT("MF", type), TypeConst.SFString)) {
			String string = input.get();
			((SoSFString) outField).setValue(string);
		}
		else if (inOutType == CASE(SO__CONCATT("SF", type), TypeConst.MFString)) {
			String string = input.get();
			((SoMFString) outField).set1Value(0, string);
		}
		else if (inOutType == CASE(SO__CONCATT("MF", type), TypeConst.MFString)) {
			for (int i = 0; i < ((SoMField) input).getNum(); i++) {
				String string = ((SoMField) input).get1(i);
				((SoMFString) outField).set1Value(i, string);
			}
		}
		else if (inOutType == CASE(TypeConst.SFString, SO__CONCATT("SF", type))
				|| inOutType == CASE(TypeConst.MFString, SO__CONCATT("SF", type))
				|| inOutType == CASE(TypeConst.SFString, SO__CONCATT("MF", type))) {
			String string = input.get();
			outField.set(string/* .getString() */);
		}
		else if (inOutType == CASE(TypeConst.MFString, SO__CONCATT("MF", type))) {
			for (int i = 0; i < ((SoMField) input).getNum(); i++) {
				String string = ((SoMField) input).get1(i);
				((SoMField) outField).set1(i, string/* .getString() */);
			}
		}
		else {
			return false;
		}
		return true;
	}

	// This macro will do most of the conversions, relying on the C++
	// built-in type conversions. It does all eight combinations of
	// single/multi to single/multi conversions for two types that are
	// different. HALF_CONV does the conversions one-way, CONV does them
	// both ways:
	// Single to single:
	// SoSField.setValue(SoSField.getValue());
	// Multi to single:
	// if (SoMField.getNum() > 0) SoSField.setValue(SoMField[0])
	// Single to multi:
	// SoMField.setValue(SoSField.getValue())
	// Multi to multi:
	// for (i = 0; i < SoMField.getNum(); i++) {
	// SoMField.set1Value(i, SoMfield[i]);
	// }
	//
	private boolean HALF_CONV(String typeIn, String typeOut, final int inOutType, Class valTypeOut, SoField outField) {
		if (inOutType == CASE(SO__CONCATT("SF", typeIn), SO__CONCATT("SF", typeOut))) {
			((/* SO__CONCAT("SoSF",typeOut) */SoSField) outField).setValue(/* (valTypeOut) */
					((/* SO__CONCAT("SoSF",typeIn) */SoSField) input).getValue());
		}
		else if (inOutType == CASE(SO__CONCATT("MF", typeIn), SO__CONCATT("SF", typeOut))) {
			if (((SoMField) input).getNum() > 0)
				((/* SO__CONCAT("SoSF",typeOut) */SoSField) outField).setValue(/* (valTypeOut) */
						((/* SO__CONCAT("SoMF",typeIn) */SoMField) input).operator_square_bracket(0));
		}
		else if (inOutType == CASE(SO__CONCATT("SF", typeIn), SO__CONCATT("MF", typeOut))) {
			((/* SO__CONCAT("SoMF",typeOut) */SoMField) outField).setValue(/* (valTypeOut) */
					((/* SO__CONCAT("SoSF",typeIn) */SoSField) input).getValue());
		}
		else if (inOutType == CASE(SO__CONCATT("MF", typeIn), SO__CONCATT("MF", typeOut))) {
			for (int i = 0; i < ((SoMField) input).getNum(); i++) {
				Object obj = ((/* SO__CONCAT("SoMF",typeIn) */SoMField) input)
						.operator_square_bracket(i);
				
				if (outField instanceof SoMFColor && obj instanceof SbVec3f) {
					SbColor color = new SbColor();
					color.copyFrom(obj);
					obj = color;
				}
				((/* SO__CONCAT("SoMF",typeOut) */SoMField) outField).set1Value(i,
						/* (valTypeOut) */ obj);
			}
		}
		else {
			return false;
		}
		return true;
	}

	private boolean CONV(String type1, Class valType1, String type2, Class valType2, final int inOutType,
			SoField outField) {
		if(HALF_CONV(type1, type2, inOutType, valType2, outField)) {}
		else if (HALF_CONV(type2, type1, inOutType, valType1, outField)) {}
		else {
			return false;
		}
		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Actually do the conversion, writing to the given field.
	// A massive switch, which is the opposite of object-oriented but
	// which makes the library a lot smaller.
	//
	// Use: private

	private void doConversion(SoField outField)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Various variables needed by the conversion cases:
		int i;
		final SbMatrix matrix = new SbMatrix();
		final SbMatrixd matrixd = new SbMatrixd();
		String string;

		// Combine inType/outType into one integer.
		final int inOutType = inType * MAXFIELDS + outType;
		// switch (inType*MAXFIELDS + outType) {

		// Cases for all the field types:
		if (CONV1("BitMask", inOutType, outField)){}
		else if(CONV1("Bool", inOutType, outField)){}
		else if(CONV1("Color", inOutType, outField)){}
		else if(CONV1("Double", inOutType, outField)){}
		else if(CONV1("Enum", inOutType, outField)){}
		else if(CONV1("Float", inOutType, outField)){}
		else if(CONV1("Int32", inOutType, outField)){}
		else if(CONV1("Matrix", inOutType, outField)){}
		else if(CONV1("Matrixd", inOutType, outField)){}
		else if(CONV1("Name", inOutType, outField)){}
		else if(CONV1("Node", inOutType, outField)){}
		else if(CONV1("Path", inOutType, outField)){}
		else if(CONV1("Plane", inOutType, outField)){}
		else if(CONV1("Planed", inOutType, outField)){}
		else if(CONV1("Rotation", inOutType, outField)){}
		else if(CONV1("Rotationd", inOutType, outField)){}
		else if(CONV1("Short", inOutType, outField)){}
		else if(CONV1("String", inOutType, outField)){}
		else if(CONV1("Time", inOutType, outField)){}
		else if(CONV1("UInt32", inOutType, outField)){}
		else if(CONV1("UShort", inOutType, outField)){}
		else if(CONV1("Vec2d", inOutType, outField)){}
		else if(CONV1("Vec2f", inOutType, outField)){}
		else if(CONV1("Vec3d", inOutType, outField)){}
		else if(CONV1("Vec3f", inOutType, outField)){}
		else if(CONV1("Vec4d", inOutType, outField)){}
		else if(CONV1("Vec4f", inOutType, outField)){}

		// All types except string:
		else if (CONVSTR("BitMask", inOutType, outField)){}
		else if(CONVSTR("Bool", inOutType, outField)){}
		else if(CONVSTR("Color", inOutType, outField)){}
		else if(CONVSTR("Double", inOutType, outField)){}
		else if(CONVSTR("Enum", inOutType, outField)){}
		else if(CONVSTR("Float", inOutType, outField)){}
		else if(CONVSTR("Int32", inOutType, outField)){}
		else if(CONVSTR("Matrix", inOutType, outField)){}
		else if(CONVSTR("Matrixd", inOutType, outField)){}
		else if(CONVSTR("Name", inOutType, outField)){}
		else if(CONVSTR("Node", inOutType, outField)){}
		else if(CONVSTR("Path", inOutType, outField)){}
		else if(CONVSTR("Plane", inOutType, outField)){}
		else if(CONVSTR("Planed", inOutType, outField)){}
		else if(CONVSTR("Rotation", inOutType, outField)){}
		else if(CONVSTR("Rotationd", inOutType, outField)){}
		else if(CONVSTR("Short", inOutType, outField)){}
		else if(CONVSTR("UInt32", inOutType, outField)){}
		else if(CONVSTR("UShort", inOutType, outField)){}
		else if(CONVSTR("Vec2d", inOutType, outField)){}
		else if(CONVSTR("Vec2f", inOutType, outField)){}
		else if(CONVSTR("Vec3d", inOutType, outField)){}
		else if(CONVSTR("Vec3f", inOutType, outField)){}
		else if(CONVSTR("Vec4d", inOutType, outField)){}
		else if(CONVSTR("Vec4f", inOutType, outField)){}

		// Special case for time to string; if the time is great enough,
		// format as a date:
		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.SFString)) {
			SbTime t = ((SoSFTime) input).getValue();
			if (t.getValue() > 3.15e7)
				string = t.formatDate();
			else
				string = t.format();
			((SoSFString) outField).setValue(string);
		}

		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.MFString)) {
			SbTime t = ((SoSFTime) input).getValue();
			if (t.getValue() > 3.15e7)
				string = t.formatDate();
			else
				string = t.format();
			((SoMFString) outField).set1Value(0, string);
		}
		
		else if(inOutType == CASE(TypeConst.MFTime,TypeConst.SFString)) { 
			 SbTime t = ((SoMFTime)input).operator_square_bracket(0); 
			 if (t.getValue() > 3.15e7) 
				 string = t.formatDate(); 
			 else
				 string = t.format(); 
			 ((SoSFString )outField).setValue(string); }
		  
		 else if(inOutType == CASE(TypeConst.MFTime,TypeConst.MFString)) { 
			  for (i = 0; i < ((SoMField)input).getNum(); i++) { 
				  SbTime t = (((SoMFTime )input)).operator_square_bracket(i); 
				  if (t.getValue() > 3.15e7) 
					  string = t.formatDate(); 
				  else
					  string = t.format(); 
				  ((SoMFString )outField).set1Value(i, string); }
		  }
		 

		else if (inOutType == CASE(TypeConst.SFColor, TypeConst.SFVec3d)
				|| inOutType == CASE(TypeConst.SFVec3d, TypeConst.SFColor)

				|| inOutType == CASE(TypeConst.SFMatrix, TypeConst.SFMatrixd)
				|| inOutType == CASE(TypeConst.SFPlane, TypeConst.SFPlaned)
				|| inOutType == CASE(TypeConst.SFRotation, TypeConst.SFRotationd)
				|| inOutType == CASE(TypeConst.SFVec2f, TypeConst.SFVec2d)
				|| inOutType == CASE(TypeConst.SFVec3f, TypeConst.SFVec3d)
				|| inOutType == CASE(TypeConst.SFVec4f, TypeConst.SFVec4d)

				|| inOutType == CASE(TypeConst.SFMatrixd, TypeConst.SFMatrix)
				|| inOutType == CASE(TypeConst.SFPlaned, TypeConst.SFPlane)
				|| inOutType == CASE(TypeConst.SFRotationd, TypeConst.SFRotation)
				|| inOutType == CASE(TypeConst.SFVec2d, TypeConst.SFVec2f)
				|| inOutType == CASE(TypeConst.SFVec3d, TypeConst.SFVec3f)
				|| inOutType == CASE(TypeConst.SFVec4d, TypeConst.SFVec4f)

				|| inOutType == CASE(TypeConst.SFString, TypeConst.SFTime)
				|| inOutType == CASE(TypeConst.MFString, TypeConst.SFTime)
				|| inOutType == CASE(TypeConst.SFString, TypeConst.MFTime)) {
			string = input.get();
			outField.set(string/* .getString() */);
		}

		else if (inOutType == CASE(TypeConst.MFMatrix, TypeConst.MFMatrixd)
				|| inOutType == CASE(TypeConst.MFPlane, TypeConst.MFPlaned)
				|| inOutType == CASE(TypeConst.MFRotation, TypeConst.MFRotationd)
				|| inOutType == CASE(TypeConst.MFVec2f, TypeConst.MFVec2d)
				|| inOutType == CASE(TypeConst.MFVec3f, TypeConst.MFVec3d)
				|| inOutType == CASE(TypeConst.MFVec4f, TypeConst.MFVec4d)

				|| inOutType == CASE(TypeConst.MFMatrixd, TypeConst.MFMatrix)
				|| inOutType == CASE(TypeConst.MFPlaned, TypeConst.MFPlane)
				|| inOutType == CASE(TypeConst.MFRotationd, TypeConst.MFRotation)
				|| inOutType == CASE(TypeConst.MFVec2d, TypeConst.MFVec2f)
				|| inOutType == CASE(TypeConst.MFVec3d, TypeConst.MFVec3f)
				|| inOutType == CASE(TypeConst.MFVec4d, TypeConst.MFVec4f)

				|| inOutType == CASE(TypeConst.MFString, TypeConst.MFTime)) {
			for (i = 0; i < ((SoMField) input).getNum(); i++) {
				string = ((SoMField) input).get1(i);
				((SoMField) outField).set1(i, string/* .getString() */);
			}
		}

		// Simple conversions for most fields:
		else if(CONV("Bool", Boolean.class, "Double", Double.class, inOutType, outField)){}
		else if(CONV("Bool", Boolean.class, "Float", Float.class, inOutType, outField)){}
		else if(CONV("Bool", Boolean.class, "Int32", Integer.class, inOutType, outField)){}
		else if(CONV("Bool", Boolean.class, "Short", Short.class, inOutType, outField)){}
		else if(CONV("Bool", Boolean.class, "UInt32", Integer.class, inOutType, outField)){}
		else if(CONV("Bool", Boolean.class, "UShort", Short.class, inOutType, outField)){}

		else if(CONV("Color", SbColor.class, "Vec3f", SbVec3f.class, inOutType, outField)){}

		else if(CONV("Double", Double.class, "Int32", Integer.class, inOutType, outField)){}
		else if(CONV("Double", Double.class, "Short", Short.class, inOutType, outField)){}
		else if(CONV("Double", Double.class, "UInt32", Integer.class, inOutType, outField)){}
		else if(CONV("Double", Double.class, "UShort", Short.class, inOutType, outField)){}
		else if(CONV("Double", Double.class, "Float", Float.class, inOutType, outField)){}

		else if(CONV("Float", Float.class, "Int32", Integer.class, inOutType, outField)){}
		else if(CONV("Float", Float.class, "Short", Short.class, inOutType, outField)){}
		else if(CONV("Float", Float.class, "UInt32", Integer.class, inOutType, outField)){}
		else if(CONV("Float", Float.class, "UShort", Short.class, inOutType, outField)){}

		else if(CONV("Int32", Integer.class, "Short", Short.class, inOutType, outField)){}
		else if(CONV("Int32", Integer.class, "UInt32", Integer.class, inOutType, outField)){}
		else if(CONV("Int32", Integer.class, "UShort", Short.class, inOutType, outField)){}

		else if(CONV("Short", Short.class, "UInt32", Integer.class, inOutType, outField)){}
		else if(CONV("Short", Short.class, "UShort", Short.class, inOutType, outField)){}

		else if(CONV("UInt32", Integer.class, "UShort", Short.class, inOutType, outField)){}

		// Some wacky oddball conversions that we have to special-case:

		// Float to time can be handled by regular code because SbTime has a
		// constructor that takes a float, but time to float needs to be
		// special-cased:
		else if(HALF_CONV("Float", "Time", inOutType, Float.class, outField)) {}

		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.SFFloat)) {
			((SoSFFloat) outField).setValue((float) ((SoSFTime) input).getValue().getValue());
		}
		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.SFDouble)) {
			((SoSFDouble) outField).setValue(((SoSFTime) input).getValue().getValue());
		}
		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.MFFloat)) {
			((SoMFFloat) outField).setValue((float)((SoSFTime) input).getValue().getValue());
		}
		else if (inOutType == CASE(TypeConst.SFTime, TypeConst.MFDouble)) {
			((SoMFDouble) outField).setValue(((SoSFTime) input).getValue().getValue());
		}
		else if (inOutType == CASE(TypeConst.MFTime, TypeConst.SFFloat)) {
			((SoSFFloat) outField).setValue((float)((SoMFTime) input).operator_square_bracket(0).getValue());
		}
		else if (inOutType == CASE(TypeConst.MFTime, TypeConst.SFDouble)) {
			((SoSFDouble) outField).setValue(((SoMFTime) input).operator_square_bracket(0).getValue());
		}
		else if (inOutType == CASE(TypeConst.MFTime, TypeConst.MFFloat)) {
			for (i = 0; i < ((SoMFTime) input).getNum(); i++) {
				((SoMFFloat) outField).set1Value(i, (float)((SoMFTime) input).operator_square_bracket(i).getValue());
			}
		}
		else if (inOutType == CASE(TypeConst.MFTime, TypeConst.MFDouble)) {
			for (i = 0; i < ((SoMFTime) input).getNum(); i++) {
				((SoMFDouble) outField).set1Value(i, ((SoMFTime) input).operator_square_bracket(i).getValue());
			}
		}

		else if (inOutType == CASE(TypeConst.SFMatrix, TypeConst.SFRotation)) {
			((SoSFRotation) outField).setValue(new SbRotation(((SoSFMatrix) input).getValue()));
		}
		else if (inOutType == CASE(TypeConst.SFMatrix, TypeConst.SFRotationd)) {
			((SoSFRotationd) outField).setValue(new SbRotationd(getSbMatrixdFromSbMatrix(((SoSFMatrix) input).getValue())));
		}
		else if (inOutType == CASE(TypeConst.SFMatrixd, TypeConst.SFRotation)) {
			((SoSFRotation) outField).setValue(new SbRotation(getSbMatrixFromSbMatrixd(((SoSFMatrixd) input).getValue())));
		}
		else if (inOutType == CASE(TypeConst.SFMatrixd, TypeConst.SFRotationd)) {
			((SoSFRotationd) outField).setValue(new SbRotationd(((SoSFMatrixd) input).getValue()));
		}

		else if (inOutType == CASE(TypeConst.SFMatrix, TypeConst.MFRotation)) {
			((SoMFRotation) outField).setValue(new SbRotation(((SoSFMatrix) input).getValue()));
		}
		else if (inOutType == CASE(TypeConst.SFMatrix, TypeConst.MFRotationd)) {
			((SoMFRotationd) outField).setValue(new SbRotationd(getSbMatrixdFromSbMatrix(((SoSFMatrix) input).getValue())));
		}
		else if (inOutType == CASE(TypeConst.SFMatrixd, TypeConst.MFRotation)) {
			((SoMFRotation) outField).setValue(new SbRotation(getSbMatrixFromSbMatrixd(((SoSFMatrixd) input).getValue())));
		}
		else if (inOutType == CASE(TypeConst.SFMatrixd, TypeConst.MFRotationd)) {
			((SoMFRotationd) outField).setValue(new SbRotationd(((SoSFMatrixd) input).getValue()));
		}

		else if (inOutType == CASE(TypeConst.MFMatrix, TypeConst.SFRotation)) {
			((SoSFRotation) outField).setValue(new SbRotation(((SoMFMatrix) input).operator_square_bracket(0)));
		}
		else if (inOutType == CASE(TypeConst.MFMatrix, TypeConst.SFRotationd)) {
			((SoSFRotationd) outField)
					.setValue(new SbRotationd(getSbMatrixdFromSbMatrix(((SoMFMatrix) input).operator_square_bracket(0))));
		}
		else if (inOutType == CASE(TypeConst.MFMatrixd, TypeConst.SFRotation)) {
			((SoSFRotation) outField).setValue(new SbRotation(getSbMatrixFromSbMatrixd(((SoMFMatrixd) input).operator_square_bracket(0))));
		}
		else if (inOutType == CASE(TypeConst.MFMatrixd, TypeConst.SFRotationd)) {
			((SoSFRotationd) outField).setValue(new SbRotationd(((SoMFMatrixd) input).operator_square_bracket(0)));
		}

		else if (inOutType == CASE(TypeConst.MFMatrix, TypeConst.MFRotation)) {
			for (i = 0; i < ((SoMFMatrix) input).getNum(); i++) {
				((SoMFRotation) outField).set1Value(i, new SbRotation(((SoMFMatrix) input).operator_square_bracket(i)));
			}
		}
		else if (inOutType == CASE(TypeConst.MFMatrix, TypeConst.MFRotationd)) {
			for (i = 0; i < ((SoMFMatrix) input).getNum(); i++) {
				((SoMFRotationd) outField).set1Value(i,
						new SbRotationd(getSbMatrixdFromSbMatrix(((SoMFMatrix) input).operator_square_bracket(i))));
			}
		}
		else if (inOutType == CASE(TypeConst.MFMatrixd, TypeConst.MFRotation)) {
			for (i = 0; i < ((SoMFMatrixd) input).getNum(); i++) {
				((SoMFRotation) outField).set1Value(i,
						new SbRotation(getSbMatrixFromSbMatrixd(((SoMFMatrixd) input).operator_square_bracket(i))));
			}
		}
		else if (inOutType == CASE(TypeConst.MFMatrixd, TypeConst.MFRotationd)) {
			for (i = 0; i < ((SoMFMatrixd) input).getNum(); i++) {
				((SoMFRotationd) outField).set1Value(i, new SbRotationd(((SoMFMatrixd) input).operator_square_bracket(i)));
			}
		}

		else if (inOutType == CASE(TypeConst.SFRotation, TypeConst.SFMatrix)) {
			matrix.setRotate(((SoSFRotation) input).getValue());
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.SFRotation, TypeConst.SFMatrixd)) {
			matrixd.setRotate(getSbRotationdFromSbRotation(((SoSFRotation) input).getValue()));
			((SoSFMatrixd) outField).setValue(matrixd);
		}
		else if (inOutType == CASE(TypeConst.SFRotationd, TypeConst.SFMatrix)) {
			matrix.setRotate(getSbRotationFromSbRotationd(((SoSFRotationd) input).getValue()));
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.SFRotationd, TypeConst.SFMatrixd)) {
			matrixd.setRotate(((SoSFRotationd) input).getValue());
			((SoSFMatrixd) outField).setValue(matrixd);
		}

		else if (inOutType == CASE(TypeConst.SFRotation, TypeConst.MFMatrix)) {
			matrix.setRotate(((SoSFRotation) input).getValue());
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.SFRotation, TypeConst.MFMatrixd)) {
			matrixd.setRotate(getSbRotationdFromSbRotation(((SoSFRotation) input).getValue()));
			((SoSFMatrixd) outField).setValue(matrixd);
		}
		else if (inOutType == CASE(TypeConst.SFRotationd, TypeConst.MFMatrix)) {
			matrix.setRotate(getSbRotationFromSbRotationd(((SoSFRotationd) input).getValue()));
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.SFRotationd, TypeConst.MFMatrixd)) {
			matrixd.setRotate(((SoSFRotationd) input).getValue());
			((SoSFMatrixd) outField).setValue(matrixd);
		}

		else if (inOutType == CASE(TypeConst.MFRotation, TypeConst.SFMatrix)) {
			matrix.setRotate(((SoMFRotation) input).operator_square_bracket(0));
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.MFRotation, TypeConst.SFMatrixd)) {
			matrixd.setRotate(getSbRotationdFromSbRotation(((SoMFRotation) input).operator_square_bracket(0)));
			((SoSFMatrixd) outField).setValue(matrixd);
		}
		else if (inOutType == CASE(TypeConst.MFRotationd, TypeConst.SFMatrix)) {
			matrix.setRotate(getSbRotationFromSbRotationd(((SoMFRotationd) input).operator_square_bracket(0)));
			((SoSFMatrix) outField).setValue(matrix);
		}
		else if (inOutType == CASE(TypeConst.MFRotationd, TypeConst.SFMatrixd)) {
			matrixd.setRotate(((SoMFRotationd) input).operator_square_bracket(0));
			((SoSFMatrixd) outField).setValue(matrixd);
		}

		else if (inOutType == CASE(TypeConst.MFRotation, TypeConst.MFMatrix)) {
			for (i = 0; i < ((SoMFRotation) input).getNum(); i++) {
				matrix.setRotate(((SoMFRotation) input).operator_square_bracket(i));
				((SoMFMatrix) outField).set1Value(i, matrix);
			}
		}
		else if (inOutType == CASE(TypeConst.MFRotation, TypeConst.MFMatrixd)) {
			for (i = 0; i < ((SoMFRotation) input).getNum(); i++) {
				matrixd.setRotate(getSbRotationdFromSbRotation(((SoMFRotation) input).operator_square_bracket(i)));
				((SoMFMatrixd) outField).set1Value(i, matrixd);
			}
		}
		else if (inOutType == CASE(TypeConst.MFRotationd, TypeConst.MFMatrix)) {
			for (i = 0; i < ((SoMFRotationd) input).getNum(); i++) {
				matrix.setRotate(getSbRotationFromSbRotationd(((SoMFRotationd) input).operator_square_bracket(i)));
				((SoMFMatrix) outField).set1Value(i, matrix);
			}
		}
		else if (inOutType == CASE(TypeConst.MFRotationd, TypeConst.MFMatrixd)) {
			for (i = 0; i < ((SoMFRotationd) input).getNum(); i++) {
				matrixd.setRotate(((SoMFRotationd) input).operator_square_bracket(i));
				((SoMFMatrixd) outField).set1Value(i, matrixd);
			}
		}
		else {
		// #ifdef DEBUG
		SoDebugError.post("SoBuiltinFieldConverter::doConversion",
				"Can't convert type " + inType + " to type " + outType + "\n");
		// #endif
		}
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create an SbMatrixd from an SbMatrix.
//
// Use: private

public static SbMatrixd getSbMatrixdFromSbMatrix(final SbMatrix rM)
//
////////////////////////////////////////////////////////////////////////
{
  final float[][] matf = new float[4][4];
  rM.getValue(matf);
  final double[][] matd = new double[4][4];
  for (int c=0; c < 4; ++c){
    for (int r=0; r < 4; ++r){
      matd[r][c] = matf[r][c];
    }
  }
  return new SbMatrixd(matd);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create an SbMatrix from an SbMatrixd.
//
// Use: private

public static SbMatrix getSbMatrixFromSbMatrixd(final SbMatrixd rM)
//
////////////////////////////////////////////////////////////////////////
{
  final double[][] matd = new double[4][4];
  rM.getValue(matd);
  final float[][] matf = new float[4][4];
  for (int c=0; c < 4; ++c){
    for (int r=0; r < 4; ++r){
      matf[r][c] = (float)(matd[r][c]);
    }
  }
  return new SbMatrix(matf);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create an SbRotationd from an SbRotation.
//
// Use: private

private static SbRotationd getSbRotationdFromSbRotation(final SbRotation r)
//
////////////////////////////////////////////////////////////////////////
{
  final float[] q0q1q2q3 = new float[4];
  r.getValue(q0q1q2q3);
  return new SbRotationd(q0q1q2q3[0],q0q1q2q3[1],q0q1q2q3[2],q0q1q2q3[3]);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create an SbRotation from an SbRotationd.
//
// Use: private

private static SbRotation getSbRotationFromSbRotationd(final SbRotationd r)
//
////////////////////////////////////////////////////////////////////////
{
  final double[] q0q1q2q3 = new double[4];
  r.getValue(q0q1q2q3);
  return new SbRotation((float)(q0q1q2q3[0]), 
                    (float)(q0q1q2q3[1]), 
                    (float)(q0q1q2q3[2]), 
                    (float)(q0q1q2q3[3]));
}

}
