/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */

/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Defines the SoField class, which is the base class for all fields.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jscenegraph.coin3d.inventor.engines.SoEngineAble;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoType.CreateMethod;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.engines.SoFieldConverter;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.misc.SoAuditorList;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.sensors.SoDataSensor;
import jscenegraph.mevis.inventor.SoProfiling;
import jscenegraph.port.Destroyable;

////////////////////////////////////////////////////////////////////////////////
//! Base class for all fields.
/*!
\class SoField
\ingroup Fields
SoField is the abstract base class for all fields. Fields
are the data elements contained within nodes and are the input values
for engines.
Each node or engine class specifies a set of fields and associates a
name with each.
These names define the semantics of the field (e.g., the SoCube
node contains three float fields named width, height, and depth).
Field classes provide the access methods that indirectly allow editing
and querying of data within nodes.


There are two abstract subclasses of SoField: SoSField
is the base class for all single-valued field classes and
SoMField is the base class for all multiple-valued fields, which
contain dynamic arrays of values. Subclasses of SoSField have an
SoSF prefix, and subclasses of SoMField have an SoMF
prefix.  See the reference pages for SoSField and SoMField for
additional methods.


Fields are typically constructed only within node or engine
instances; if you need a field that is not part of a node or engine,
you can create a GlobalField; see the methods on SoDB for
creating global fields.


Fields can be connected either directly to another field, or can be
connected to the output of an engine.  The value of a field with a
connection will change when the thing it is connected to changes.  For
example, consider a field "A" that is connected from "B" (by
A-&gt;connectFrom(B)).
When B's value is changed, A's value will also
change.  Note that A and B may have different values, even if they are
connected: if A's value is set after B's value, A's value will be
different from B's until B's value is set.


A field can be connected to several other fields, but can be connected
from only one source.


It is possible (and often useful) to create loops of field
connections (for example, A connected from B and B connected from A).
If there are loops, then the rule is that the last 
setValue() done
overrides any connections in to that value.  You can think of setting
the value of a field as immediately propagating that value forward
into all the fields it is connected to, with the propagation stopping
at the place where the original setValue() occurred if there is a
connection loop.  (Actually, a more efficient mechanism than this is
used, but the semantics are the same.)


If you try to connect two fields of differing types, Inventor will
automatically try to insert a field converter engine between them to
convert values from one type into the other.  Inventor has most
reasonable conversions built-in (multiple-valued field to single-valued 
and vice versa, anything to SoSFString, anything to
SoSFTrigger, float/short/unsigned short/int32_t/uint32_t/etc
numeric conversions, etc). You can add field converters using SoDB's
extender method addConverter(); see the SoDB.h header file for
details.  You can also find out if a converter is available with the
SoDB.getConverter() method.


Fields each define their own file format for reading and being written
to files, but all fields follow the same conventions:


Fields in a node or engine are written as the name of the field
followed by the field's value; fields are not written if they have not
been modified since they were created (if they have their default
value).


The ignored flag is written as a "~" character after the field's
value (if the field's value is its default value, just the "~" is
written).


Field connections are written as an "=" followed by the container of
the field or engine output that the field is connected to, followed by
a "." and the name of the field or engine output.  For example:
\code
DEF node1 Transform { translation 1 1 1 }
DEF node2 Scale { scaleFactor 1 1 1 = USE node1.translation }
\endcode


Global fields are written as part of an internal SoFieldContainer
class called GlobalField, which writes out an \b SoSFName  field named
\b type  whose value is the type of the global field, followed by
a field of that type whose name is the name of the global field.  For
example, a global uint32_t field called "FrameCounter" whose
value is 494 would be written as:
\code
GlobalField {
type SoSFUInt32
FrameCounter 494
}
\endcode

\par See Also
\par
SoSField, SoMField, SoNode, SoDB
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoField implements Destroyable {


// Special characters in files
public static final char OPEN_BRACE_CHAR         ='[';
public static final char CLOSE_BRACE_CHAR        =']';
public static final char VALUE_SEPARATOR_CHAR    =',';
public static final char IGNORE_CHAR             ='~';
public static final char CONNECTION_CHAR         ='=';
public static final char FIELD_SEP_CHAR          ='.';

// Bit flags in flags token in binary files
public static final int FIELD_IGNORED           =0x01;
public static final int FIELD_CONNECTED         =0x02;
public static final int FIELD_DEFAULT           =0x04;

// Amount of values to allocate at a time when reading in multiple values.
public static final int VALUE_CHUNK_SIZE        =32;

  // enums for setFieldType()/getFieldType()
  public enum FieldType {
    NORMAL_FIELD,
    EVENTIN_FIELD,
    EVENTOUT_FIELD,
    EXPOSED_FIELD;

	public int getValue() {
		return ordinal();
	}

	public static FieldType fromValue(int fieldType) {
		switch(fieldType) {
		case 0: return NORMAL_FIELD;
		case 1: return EVENTIN_FIELD;
		case 2: return EVENTOUT_FIELD;
		case 3: return EXPOSED_FIELD;
		}
		return null;
	}
  };

  private static final int FLAG_TYPEMASK = 0x0007;  // need 3 bits for values [0-5]
  private enum FieldFlags {
    FLAG_ISDEFAULT(0x0008),
    FLAG_IGNORE(0x0010),
    FLAG_EXTSTORAGE(0x0020),
    FLAG_ENABLECONNECTS(0x0040),
    FLAG_NEEDEVALUATION(0x0080),
    FLAG_READONLY(0x0100),
    FLAG_DONOTIFY(0x0200),
    FLAG_ISDESTRUCTING(0x0400),
    FLAG_ISEVALUATING(0x0800),
    FLAG_ISNOTIFIED(0x1000);
	  
	  private int value;
	  
	  FieldFlags( int value) {
		  this.value = value;
	  }
  };

	
	// ! The "flags" field contains several bit flags:
	public class Flags implements Cloneable {
		public boolean hasDefault; // !< Field is set to default value
		public boolean ignored; // !< Field value is to be ignored
		public boolean connected; // !< Field connected from something
		public boolean converted; // !< Connection required converter
		public boolean fromEngine; // !< Connection is from engine
		public boolean connectionEnabled;// !< Connection is enabled
		public boolean notifyEnabled; // !< Notification is enabled
		public boolean hasAuditors; // !< Connected, or FieldSensor
		public boolean isEngineModifying;// !< Engine evaluating
		public boolean readOnly; // !< Must not write into
									// ! this field
		public boolean dirty; // !< Field was notified and
								// ! needs evaluation
		public boolean notifyContainerEnabled; // !< If set to 0, the container
												// is not notified on Field
												// changed

		public void copyFrom(Flags other) {
			hasDefault = other.hasDefault; // !< Field is set to default value
			ignored = other.ignored; // !< Field value is to be ignored
			connected = other.connected; // !< Field connected from something
			converted = other.converted; // !< Connection required converter
			fromEngine = other.fromEngine; // !< Connection is from engine
			connectionEnabled = other.connectionEnabled;// !< Connection is enabled
			notifyEnabled = other.notifyEnabled; // !< Notification is enabled
			hasAuditors = other.hasAuditors; // !< Connected, or FieldSensor
			isEngineModifying = other.isEngineModifying;// !< Engine evaluating
			readOnly = other.readOnly; // !< Must not write into
										// ! this field
			dirty = other.dirty; // !< Field was notified and
									// ! needs evaluation
			notifyContainerEnabled = other.notifyContainerEnabled; // !< If set to 0, the container
		}
		
		public Object clone() throws CloneNotSupportedException {
			Flags clone = (Flags)super.clone();
			clone.copyFrom(this);
			return clone;
		}
	};

	// If the field is connected or there are any FieldSensors attached to
	// this field, flags.hasAuditors will be true, and this structure is
	// used to contain the extra information needed. Done this way to
	// save space in the common case.
	private class SoFieldAuditorInfo implements Destroyable {
		public SoFieldContainer container;

		// List of auditors: objects to pass notification to.
		final SoAuditorList auditors = new SoAuditorList();

		// The "connection" field points to either an engine output or
		// another field:
		Object connection;

		SoEngineOutput connection_engineOutput() {
			return (SoEngineOutput) connection;
		};

		SoField connection_field() {
			return (SoField) connection;
		}

		public void destructor() {
			container = null;
			auditors.destructor();
			connection = null;
		}
	}

	public final Flags flags = new Flags();

	private int statusbits;
	private SoFieldContainer container; // ptr
	private SoFieldAuditorInfo auditorInfo; // ptr

	// java port : no
//	protected void copy(SoField other) {
//			flags.copyFrom( other.flags);
//		container = other.container;
//		auditorInfo = other.auditorInfo;
//	}

	// Return the type identifier for this field instance (SoField *).
	public final SoType getTypeId() {
		SoType classTypeId;
		Class<?> javaClass = getClass();
		do {
			classTypeId = typeMap.get(javaClass);		
			javaClass = javaClass.getSuperclass();
		} while(classTypeId == null);
		return classTypeId;
	}

	// java port initialize the SoType from java class
	// java port
	private final static Map<Class<? extends SoField>, SoType> typeMap = new HashMap<Class<? extends SoField>, SoType>();

	// java port
	public static final void initClass(final Class<? extends SoField> javaClass) {
		SoType classTypeId = typeMap.get(javaClass);
		if (classTypeId == null) {
			classTypeId = createClass(javaClass);
			typeMap.put(javaClass, classTypeId);		
		}
	}
	
	/**
	 * Creates a new SoType
	 * @param javaClass
	 * @return
	 */
	private static final SoType createClass(final Class<? extends SoField> javaClass) {
		SoType classTypeId;
		Class<?> parentClass = javaClass.getSuperclass();
		if (SoField.class.isAssignableFrom(parentClass)) {
			SoType parentClassTypeId = getClassTypeId(parentClass.asSubclass(SoField.class));// initClass((Class<?
																								// extends
																								// SoField>)parentClass);

			String className = javaClass.getSimpleName();
			SbName classPrintName = new SbName(className.substring(2));
			CreateMethod createInstance = new CreateMethod() {

				@Override
				public Object run() {
					Class[] parameterTypes = new Class[0];
					try {
						Constructor<? extends SoField> constructor = javaClass.getConstructor(parameterTypes);
						Object[] initargs = new Object[0];
						try {
							return constructor.newInstance(initargs);
						} catch (IllegalArgumentException e) {
							throw new IllegalStateException();
						} catch (InstantiationException e) {
							throw new IllegalStateException();
						} catch (IllegalAccessException e) {
							throw new IllegalStateException();
						} catch (InvocationTargetException e) {
							throw new IllegalStateException();
						}
					} catch (SecurityException e) {
						throw new IllegalStateException();
					} catch (NoSuchMethodException e) {
						throw new IllegalStateException();
					}
				}

			};
			classTypeId = SoType.createType(parentClassTypeId, classPrintName, createInstance, (short) 0);
		} else {
			// Allocate a new field type id. There's no real parent id, so
			// we
			// can't use the regular macro.
			classTypeId = SoType.createType(SoType.badType(), new SbName("Field"));
		}
		return classTypeId;
	}

	/**
	 * Sets the ignore flag for this field. When a field's ignore flag is set to
	 * true, the field is not used during traversal for rendering and other
	 * actions. The default value for this flag is false.
	 * 
	 * @param ig
	 */
	public void setIgnored(boolean ig) {
		if (flags.ignored != ig) {
			flags.ignored = ig;

			// Indicate that the value changed, but leave the default flag as is
			valueChanged(false);
		}

	}

	// Gets the ignore flag for this field.
	public boolean isIgnored() {
		return flags.ignored;
	}

	/**
	 * Gets the state of default flag of the field. This flag will be true for
	 * any field whose value is not modified after construction and will be
	 * false for those that have changed (each node or engine determines what
	 * the default values for its fields are). Note: the state of this flag
	 * should not be set explicitly from within applications.
	 * 
	 * @return
	 */
	public boolean isDefault() {
		return flags.hasDefault;
	}

	/**
	 * Field connections may be enabled and disabled. Disabling a field's
	 * connection is almost exactly like disconnecting it; the only difference
	 * is that you can later re-enable the connection by calling
	 * enableConnection(true). Note that disconnecting an engine output can
	 * cause the engine's reference count to be decremented and the engine to be
	 * deleted, but disabling the connection does not decrement its reference
	 * count.
	 * 
	 * Re-enabling a connection will cause the value of the field to be changed
	 * to the engine output or field to which it is connected.
	 * 
	 * A field's connection-enabled status is maintained even if the field is
	 * disconnected or reconnected. By default, connections are enabled.
	 * 
	 * @param flag
	 */
	public void enableConnection(boolean flag) {

		if (flags.connectionEnabled == flag)
			return;

		// Before disabling, pull value through if out-of-date:
		if (!flag) {
			evaluate();
			flags.connectionEnabled = false;
			flags.readOnly = true;

			if (isConnectedFromField() && !flags.converted)
				auditorInfo.connection_field().connectionStatusChanged(-1);
		} else {
			// Mark the field dirty when re-enabling so it will get
			// evaluated.
			flags.readOnly = false;
			flags.connectionEnabled = true;
			flags.dirty = true;

			if (isConnectedFromEngine() || flags.converted) {
				// Mark engine as needing evaluation to force it to write
				// value (just as if we added a connection):
				auditorInfo.connection_engineOutput().addConnection(null);
			} else if (isConnectedFromField()) {
				auditorInfo.connection_field().connectionStatusChanged(1);
			}
			evaluate(); // Pull value through
		}
	}

	/**
	 * Returns false if connections to this field are disabled. Note that this
	 * may return false even if the field is not connected to anything.
	 * 
	 * @return
	 */
	public boolean isConnectionEnabled() {
		return flags.connectionEnabled;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Connects the field to the given output of an engine. Returns
	// false if the connection could not be made.
	//
	// Use: public

	public boolean connectFrom(SoEngineOutput engineOutput) {
		return connectFrom(engineOutput,false,false);
	}
	
/*!
  Connects this field as a slave to \a master. This means that the value
  of this field will be automatically updated when \a master is changed (as
  long as the connection also is enabled).

  If this field had any master-relationships beforehand, these are all
  broken up if \a append is \c FALSE.

  Call with \a notnotify if you want to avoid the initial notification
  of connected auditors (a.k.a. \e slaves).

  Function will return \c TRUE unless:

  \li If the field output connected \e from is of a different type
      from the engine output field-type connected \e to, a field
      converter is inserted. For some combinations of fields no such
      conversion is possible, and we'll return \c FALSE.

  \li If this field is already connected to the \a master, we will
      return \c FALSE.

  \sa enableConnection(), isConnectionEnabled(), isConnectedFromField()
  \sa getConnectedField(), appendConnection(SoEngineOutput *)
*/
	public boolean connectFrom(SoEngineOutput engineOutput, boolean notnotify, boolean append)
	//
	////////////////////////////////////////////////////////////////////////
	{
		SoFieldContainer engineCont = engineOutput.getContainer();

		// #ifdef DEBUG
		// Make sure everything is contained, or this won't work at all
		if (engineCont == null) {
			SoDebugError.post("SoField.connectFrom",
					"Can't connect to an engine output " + "that is not contained in an engine");
			return false;
		}
		// #endif

		// Ref the engine, just in case it's currently connected, and this
		// is its only ref
		engineCont.ref();

		// Disconnect any previous connection
		if(!append) disconnect(); //COIN3D

		// Check the type of the field and the output for compatability
		final SoType outputType = new SoType(engineOutput.getConnectionType());
		if (getTypeId().operator_not_equal(outputType)) {

			boolean ret;
			SoFieldConverter converter;

			converter = createConverter(outputType);

			// Check for error
			if (converter == null)
				ret = false;
			else {
				converter.ref();

				// Hook the converter up to the other field first, then
				// hook this up to the converter, to avoid multiple
				// notifications or evaluations if something downstream is
				// pulling values during notification:
				// converter:
				SoField c_input = converter.getInput(outputType);
				SoEngineOutput c_output = converter.getOutput(getTypeId());
				// #ifdef DEBUG
				if (c_input == null || c_output == null) {
					SoDebugError.post("SoField.connectFrom",
							"Created converter, but converter" + "input or output is null");
					return false;
				}
				// #endif
				// Making the connection may result in downstream engines
				// requesting evaluation, so this must be set before the
				// connection is made:
				flags.converted = true;
				flags.fromEngine = true;

				c_input.connectFrom(engineOutput);
				connectFrom(c_output);

				converter.unref();
				ret = true;
			}
			// See comment below
			engineCont.unrefNoDelete();

			return ret;
		}

		createAuditorInfo();

		flags.connected = true;

		// If converted, this flag was set when the converter was created:
		if (!flags.converted)
			flags.fromEngine = true;

		auditorInfo.connection/* .engineOutput */ = engineOutput; // java port

		// Tell the engine output about this connection
		engineOutput.addConnection(this);

		if (!notnotify && isConnectionEnabled() && engineOutput.isEnabled()) {
			// A connection means that the field no longer contains the
			// default value
			setDefault(false);

			// Notify
			startNotify();
		}

		// Get rid of the extra reference. Note that if the container was
		// handed to us with 0 references, we don't want to delete it,
		// since presumably whoever asked for the connection still wants
		// it, and will ref it later.
		engineCont.unrefNoDelete();

		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Connects the field to the given field. Returns false if the
	// connection could not be made.
	//
	// Use: public

	public boolean connectFrom(SoField field) {
		return connectFrom(field,false,false);
	}
	
/*!
  Connects this field as a slave to \a master. This means that the
  value of this field will be automatically updated when \a master is
  changed (as long as the connection also is enabled).

  If this field had any connections to master fields beforehand, these
  are all broken up if \a append is \c FALSE.

  Call with \a notnotify if you want to avoid the initial notification
  of connected auditors (a.k.a. \e slaves).

  Function will return \c TRUE unless:

  \li If the field connected \e from has a different type from the
      field connected \e to, a field converter is inserted. For some
      combinations of fields no such conversion is possible, and we'll
      return \c FALSE.

  \li If this field is already connected to the \a master, we will
      return \c FALSE.

  \sa enableConnection(), isConnectionEnabled(), isConnectedFromField()
  \sa getConnectedField(), appendConnection(SoField *)
*/
	
	public boolean connectFrom(SoField field,boolean notnotify,boolean append)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Disconnect any previous connection
		if(!append) disconnect();

		// Check the types of the two fields
		final SoType fieldType = new SoType(field.getTypeId());
		if (getTypeId().operator_not_equal(fieldType)) {

			boolean ret;
			SoFieldConverter converter;

			converter = createConverter(fieldType);

			// Check for error
			if (converter == null)
				ret = false;
			else {
				converter.ref();

				// Hook the converter up to the other field first, then
				// hook this up to the converter, to avoid multiple
				// notifications or evaluations if something downstream is
				// pulling values during notification:
				// converter:
				SoField c_input = converter.getInput(fieldType);
				SoEngineOutput c_output = converter.getOutput(getTypeId());
				// #ifdef DEBUG
				if (c_input == null || c_output == null) {
					SoDebugError.post("SoField.connectFrom",
							"Created converter, but converter" + "input or output is null");
					return false;
				}
				// #endif
				// Making the connection may result in downstream engines
				// requesting evaluation, so this must be set before the
				// connection is made:
				flags.converted = true;
				flags.fromEngine = false;

				c_input.connectFrom(field);
				connectFrom(c_output);

				converter.unref();
				ret = true;
			}
			return ret;
		}
		 // Can do direct field-to-field link.

		createAuditorInfo();

		flags.connected = true;

		// If converted, this flag was set when the converter was created:
		if (!flags.converted)
			flags.fromEngine = false;

		auditorInfo.connection/* .field */ = field; // java port

		// Make sure this field gets notified when the connected field
		// changes
		field.addAuditor(this, SoNotRec.Type.FIELD);

		if (!notnotify && isConnectionEnabled()) {
			// A connection means that the field no longer contains the
			// default value
			setDefault(false);

			// Notify
			startNotify();
		}

		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Disconnects the field from whatever it's connected to. Harmless
	// if not already connected.
	//
	// Use: public

	public void disconnect()
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (flags.connected) {

			// Make sure the field is evaluated first. (There may have
			// been a notification of the field but no corresponding
			// evaluation before the field was disconnected.)
			evaluate();

			reallyDisconnect();
		}
	}

	// Returns true if the field is connected to anything.
	public boolean isConnected() {
		return flags.connected;
	}

	// Returns true if the field is connected to an engine's output.
	public boolean isConnectedFromEngine() {
		return (flags.connected && flags.fromEngine);
	}

	// Returns true if the field is connected to another field.
	public boolean isConnectedFromField() {
		return (flags.connected && !flags.fromEngine);
	}

	/**
	 * Returns true if this field is being written into by an engine, and
	 * returns the engine output it is connected to in engineOutput. Returns
	 * false and does not modify engineOutput if it is not connected to an
	 * engine.
	 * 
	 * @param engineOut
	 * @return
	 */
	public boolean getConnectedEngine(final SoEngineOutput[] engineOut) {

		if (!isConnectedFromEngine())
			return false;

		// Skip over field converter, if any
		final SoField connectedField = (!flags.converted ? this : getConverter().getConnectedInput());

		engineOut[0] = connectedField.auditorInfo.connection_engineOutput();

		return true;
	}

	/**
	 * Returns true if this field is being written into by another field, and
	 * returns the field it is connected to in writingField. Returns false and
	 * does not modify writingField if it is not connected to a field.
	 * 
	 * @param field
	 * @return
	 */
	public boolean getConnectedField(final SoField[] field) {

		if (!isConnectedFromField())
			return false;

		// Skip over field converter, if any
		final SoField connectedField = (!flags.converted ? this : getConverter().getConnectedInput());

		field[0] = connectedField.auditorInfo.connection_field();

		return true;
	}

	public void evaluate() {
		if (flags.dirty)
			evaluateConnection();
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns the number of fields this field is writing into, and
	// adds pointers to those fields to the given field list.
	//
	// Use: public

	public int getForwardConnections(final SoFieldList list)
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (!flags.hasAuditors)
			return 0;

		int numAuditors, numConnections = 0, i;

		// Loop through our auditor list, finding fields we are connected
		// to.
		final SoAuditorList auditors = auditorInfo.auditors;
		numAuditors = auditors.getLength();
		for (i = 0; i < numAuditors; i++) {
			if (auditors.getType(i) == SoNotRec.Type.FIELD) {

				SoField field = (SoField) auditors.getObject(i);

				// Skip over converter, if any
				SoFieldContainer container = field.getContainer();
				if (container.isOfType(SoFieldConverter.getClassTypeId()))
					numConnections += ((SoFieldConverter) container).getForwardConnections(list);

				else {
					list.append(field);
					numConnections++;
				}
			}
		}

		return numConnections;
	}

	/**
	 * Sets default flag.
	 * 
	 * @param def
	 */
	public void setDefault(boolean def) {
		flags.hasDefault = def;
	}

	// Returns the containing node or engine.
	public SoFieldContainer getContainer() {
		if (flags.hasAuditors)
			return auditorInfo.container;
		else
			return container;
	}

	/**
	 * Sets the field to the given value, which is an ASCII string in the
	 * Inventor file format. Each field subclass defines its own file format;
	 * see their reference pages for information on their file format. The
	 * string should contain only the field's value, not the field's name (e.g.,
	 * "1.0", not "width 1.0"). This method returns true if the string is valid,
	 * false if it is not.
	 * 
	 * @param valueString
	 * @return
	 */
	public boolean set(String valueString) {

		final SoInput in = new SoInput();
		in.setBuffer(valueString, valueString.length());
		return read(in, new SbName("<field passed to SoField.set>"));
	}
	
    //! Returns the value of the field in the Inventor file format, even if
    //! the field has its default value.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Stores field value (in the same format expected by the set()
//method) in the given SbString. This always returns a string,
//even if the field has a default value.
//
//Use: public

	public String get() {
		//TODO
		return "";
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Touches a field, starts notification
	//
	// Use: public

	public void touch()
	//
	////////////////////////////////////////////////////////////////////////
	{
		evaluate();
		valueChanged(false);
	}

	// If this returns true, it means we're in the middle of doing a
	// setValue()+valueChanged() and values from an upstream connection
	// shouldn't write into this field.
	public boolean isReadOnly() {
		return flags.readOnly;
	}

    //! Returns TRUE if the given field is of the same type and has the
    //! same value(s) as this. Subclasses must define this as well as
    //! an == operator.
    public abstract boolean        isSame(final SoField f);

	public abstract void copyFrom(final SoField f);

	/**
	 * After a field value has been copied using copyFrom(), this is called to
	 * allow fields to update the copy. This is used by node, engine, and path
	 * fields to make sure instances are handled properly. The default
	 * implementation does nothing.
	 * 
	 * @param copyConnections
	 */
	public void fixCopy(boolean copyConnections) {

	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// This returns true if this field contains a reference to a node
	// or engine that is copied during a copy operation (i.e., it is
	// "inside"). The default method just checks if the field is
	// connected to such a node or engine. Subclasses may contain other
	// tests, such as those that contain pointers to nodes or engines.
	//
	// Use: internal, virtual

	public boolean referencesCopy()
	//
	////////////////////////////////////////////////////////////////////////
	{
		// If this field is not connected, it doesn't reference anything
		if (!isConnected())
			return false;

		// Find the container of the connected field or engine
		// output, and determine if it is an engine
		SoFieldContainer container;
		boolean containerKnownToBeEngine = false;

		if (isConnectedFromField()) {
			final SoField[] connectedField = new SoField[1];
			getConnectedField(connectedField);
			container = connectedField[0].getContainer();
			containerKnownToBeEngine = false;
		} else {
			final SoEngineOutput[] connectedOutput = new SoEngineOutput[1];
			getConnectedEngine(connectedOutput);
			container = (SoEngine) connectedOutput[0].getContainer();
			containerKnownToBeEngine = true;
		}

		// If a copy of the container exists, this field references a copy
		if (SoFieldContainer.checkCopy(container) != null)
			return true;

		// If the container is an engine, see if that engine should be
		// copied, recursively.
		// ??? Optimize this by keeping a dict of "outside"
		// ??? engines so we never do the same work twice?
		// ??? We could actually store a pointer to the original engine in
		// ??? the dictionary if it is outside, but then we have to be
		// ??? careful to test for this when we call checkCopy(). It's
		// ??? probably not worth the effort, since people won't copy
		// ??? complicated engine networks often.
		if (containerKnownToBeEngine
				|| container.isOfType(SoEngine.getClassTypeId()) && ((SoEngine) container).shouldCopy())
			return true;

		return false;
	}

	/**
	 * Copies connection from one field to another. Assumes fields are the same
	 * subclass and that this field is connected.
	 * 
	 * @param fromField
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Copies connection from one field to another. Assumes fields are
	// the same subclass and that fromField is connected. The index of
	// the field in the SoFieldData instance is passed in.
	//
	// Use: internal

	public final void copyConnection(final SoField fromField)
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Connected from another field:
		if (fromField.isConnectedFromField()) {
			final SoField[] connectedField = new SoField[1];
			fromField.getConnectedField(connectedField);

			// Find the index of the connected field in its container
			SoFieldContainer connectedFC = connectedField[0].getContainer();
			final SoFieldData fieldData = connectedFC.getFieldData();
			int index = fieldData.getIndex(connectedFC, connectedField[0]);

			// We may need to copy the field's container
			SoFieldContainer FCCopy = connectedFC.copyThroughConnection();

			// Get the other field data in case it is different
			final SoFieldData otherFieldData = FCCopy.getFieldData();

			// Connect from the corresponding field in the container
			connectFrom(otherFieldData.getField(FCCopy, index));
		}

		// Connected from engine:
		else {
			final SoEngineOutput[] connectedOutput = new SoEngineOutput[1];
			fromField.getConnectedEngine(connectedOutput);

			// Find the index of this output in the containing engine
			SoEngine connectedEngine = connectedOutput[0].getContainerSoEngine();
			final SoEngineOutputData outputData = connectedEngine.getOutputData();
			int outputIndex = outputData.getIndex(connectedEngine, connectedOutput[0]);

			// We may need to copy the engine itself
			SoEngine engineCopy = (SoEngine) connectedEngine.copyThroughConnection();

			// Get the other output data in case it is different
			final SoEngineOutputData outputDataCopy = engineCopy.getOutputData();

			// Connect from the corresponding output in the engine copy
			connectFrom(outputDataCopy.getOutput(engineCopy, outputIndex));
		}

		// Make sure state of connection is identical
		if (!fromField.isConnectionEnabled())
			enableConnection(false);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Propagates notification through the field. The notification must
	// be coming from the engine output or field from which this
	// field is connected.
	//
	// Use: internal

	public void notify(SoNotList list) {
		// If notification is disabled, don't do anything.
		if (flags.dirty || !flags.notifyEnabled || flags.isEngineModifying)
			return;

		// If being notified through a connection and the connection is
		// disabled, don't notify:
		if (!flags.connectionEnabled && (list.getLastRec() != null)) {

			SoNotRec.Type t = list.getLastRec().getType();
			if (t == SoNotRec.Type.ENGINE || t == SoNotRec.Type.FIELD)
				return;
		}

		// Indicate that we are notifying, to break future cycles.
		// SFTrigger relies on this being done BEFORE checking for a null
		// field container.
		flags.dirty = true;

		// Propagate to all auditors.
		// NOTE: Since this may be done for fields that are being
		// constructed, we have to check for a null container first.
		// NOTE: SFTrigger fields set their container to null temporarily
		// when being read in to prevent notification propagating when
		// they're read.
		SoFieldContainer cont = getContainer();

		if (cont != null) {

			boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeFieldNotifyCB() != null)
					&& (cont.getTypeId().operator_not_equal(SoGlobalField.getClassTypeId()));
			if (profilingEntered) {
				SoProfiling.getEnterScopeFieldNotifyCB().run(this);
			}

			// First create and append a new record to indicate we passed
			// through this field.
			final SoNotRec rec = new SoNotRec(cont);
			list.append(rec, this);
			list.setLastType(SoNotRec.Type.CONTAINER);

			// fli2011 (MeVis Only)
			// Only notify container if container notify is turned on
			if (flags.notifyContainerEnabled) {
				// If no auditors, just notify container
				if (!flags.hasAuditors) {
					cont.notify(list);
				} else {
					// Otherwise, notify container and auditors. We have to make
					// sure to copy the list before notifying anyone.
					final SoNotList listCopy = new SoNotList(list);
					cont.notify(list);
					auditorInfo.auditors.notify(listCopy);
					listCopy.destructor();
				}
			} else {
				// if container notification is disabled, we only notify
				// the auditors (and we do not need a copy of list)
				if (flags.hasAuditors) {
					auditorInfo.auditors.notify(list);
				}
			}
			if (profilingEntered && SoProfiling.getLeaveScopeCB() != null) {
				SoProfiling.getLeaveScopeCB().run();
			}
			rec.destructor();
		}

	}

	/**
	 * Sets the containing node. This also calls enableNotify(true) and
	 * setDefault(true).
	 * 
	 * @param cont
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Sets the containing node, engine, or global. Also marks this
	// field as Default, and turns on notification (this saves the
	// nodes from having to do it).
	//
	// Use: internal
	public void setContainer(SoFieldContainer cont) {
		if (flags.hasAuditors)
			auditorInfo.container = cont;
		else
			container = cont;
		setDefault(true);
		enableNotify(true);
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns true if the field (or its description) needs to be
	// written out.
	//
	// Use: internal

	public boolean shouldWrite()
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (!isDefault() || isConnected() || isIgnored())
			return true;

		// The hard case; if we have any forward connections, should also
		// write ourselves out (actually, that's only strictly necessary
		// if the field is part of a non-builtin node...).

		if (!flags.hasAuditors)
			return false;

		// Loop through our auditor list, finding fields we are connected
		// to.
		SoAuditorList auditors = auditorInfo.auditors;
		int numAuditors = auditors.getLength();
		for (int i = 0; i < numAuditors; i++) {
			if (auditors.getType(i) == SoNotRec.Type.FIELD) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Initiates or propagates notification through container.
	 */
	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Initiates notification due to a change in the field.
	//
	// Use: internal

	public void startNotify()
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (!flags.notifyEnabled || flags.isEngineModifying)
			return;

		SoDB.startNotify();

		// Create a new notification list:
		final SoNotList list = new SoNotList();

		// Use notify() method to do all the work:
		notify(list);

		SoDB.endNotify();
		list.destructor();
	}

	// Adds/removes an auditor to/from list.
	public void addAuditor(Object auditor, SoNotRec.Type type) {
		// #ifdef DEBUG
		if (auditor == null) {
			SoDebugError.post("SoField.addAuditor", "ACK! Trying to add a null auditor");
			return;
		}
		// #endif
		createAuditorInfo();

		auditorInfo.auditors.append(auditor, type);

		// Connection status is different
		connectionStatusChanged(1);

	}

	public void removeAuditor(Object auditor, SoNotRec.Type type) {
		int audIndex = -1;
		if (flags.hasAuditors)
			audIndex = auditorInfo.auditors.find(auditor, type);

		// #ifdef DEBUG
		if (audIndex < 0) {
			SoDebugError.post("SoField.removeAuditor", "can't find auditor " + auditor + "\n");
			return;
		}
		// #endif /* DEBUG */

		auditorInfo.auditors.remove(audIndex);

		// Connection status is different
		connectionStatusChanged(-1);

	}

	/**
	 * Indicates whether notification will propagate as the result of setting
	 * the field value. Engines turn this off when writing results into fields,
	 * since notification has already propagated.
	 * 
	 * @param flag
	 * @return
	 */
	public boolean enableNotify(boolean flag) {
		if (flags.notifyEnabled == flag)
			return flag;

		if (flag)
			evaluate();

		flags.notifyEnabled = flag;
		return !flag; // Previous state of flag is returned

	}

	////////////////////////////////////////////////////////////////////////

	public boolean enableContainerNotify(boolean flag) {
		boolean oldFlag = flags.notifyContainerEnabled;
		flags.notifyContainerEnabled = flag;
		return oldFlag;
	}

	public boolean isNotifyEnabled() {
		return flags.notifyEnabled;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Called by an instance to indicate that a value has changed. If
	// resetDefault is true, this turns off default flag. Initiates
	// notification to clear downstream dirty bits, and evaluates
	// (after protecting the value in the field) to clear upstream
	// dirty bits.
	//
	// Use: protected

	protected void valueChanged() {
		valueChanged(true);
	}

	protected void valueChanged(boolean resetDefault) // Default is true
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (resetDefault)
			setDefault(false);

		flags.readOnly = true;
		flags.dirty = false; // So notification WILL happen, at least as
		// far as sensors/field container
		startNotify();
		evaluate();
		flags.readOnly = !flags.connectionEnabled;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Really disconnects the field from whatever it's connected to.
	//
	// Use: private

	private void reallyDisconnect()
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (flags.fromEngine || flags.converted) {
			SoEngineOutput out = auditorInfo.connection_engineOutput();
			out.removeConnection(this);
			auditorInfo.connection/* .engineOutput */ = null; // java port
		} else {
			SoField field = auditorInfo.connection_field();
			field.removeAuditor(this, SoNotRec.Type.FIELD);
			auditorInfo.connection/* .field */ = null; // java port
		}
		flags.connected = false;
		flags.converted = false;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Evaluates the field or engine the field is connected to,
	// storing the result in the field.
	//
	// Use: private

	protected void evaluateConnection()
	//
	////////////////////////////////////////////////////////////////////////
	{
		// Cast away the nasty const...
		SoField f = (SoField) this;
		f.flags.dirty = false; // This must be done before upstream
		// evaluation to break evaluation cycles.

		if (flags.isEngineModifying || !flags.connected || !flags.connectionEnabled)
			return;

		// If connected to an engine, evaluate that engine. This will
		// cause the value to be written into the field by the engine.
		if (flags.fromEngine || flags.converted) {

			SoEngineAble e = auditorInfo.connection_engineOutput().getContainerEngine();
			e.evaluateWrapper();
		}

		// If connected to a field, just copy the value from that field
		// UNLESS the readOnly bit is set.
		// This uses the virtual "=" operator, which should do the right thing.
		else if (!f.isReadOnly()) {
			// Disable notification since we already did it
			boolean notifySave = f.flags.notifyEnabled;
			f.flags.notifyEnabled = false;

			// Copy value
			f.copyFrom(auditorInfo.connection_field());

			// Reenable notification
			f.flags.notifyEnabled = notifySave;
		}
	}
	
	    //! Reads value(s) of field
    public abstract boolean        readValue(SoInput in);

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads field connection from file. Works for ASCII and binary
//    input. Returns false on error.  The upstream connection is
//    evaluated before connecting so we don't end up with dirty fields
//    upstream from clean ones (fields are clean once they've been
//    read).
//
// Use: private

public boolean readConnection(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    SoFieldContainer    connContainer;
    final SbName              fieldName = new SbName();
    boolean                gotChar;
    final char[]                c = new char[1];

    // The field is connected to either an engine output or to a field
    // of an engine or node. First, read the node or engine as a base.
    final SoBase[] baseTemp = new SoBase[1];
    if (! SoBase.read(in, baseTemp, SoFieldContainer.getClassTypeId()))
        return false;

    if (baseTemp[0] == null) {
        SoReadError.post(in, "Missing node or engine name in "+
                          "connection specification");
        return false;
    }

    connContainer = (SoFieldContainer ) baseTemp[0];

    // Read the field separator character (ASCII only)
    if (! in.isBinary() &&
        (! (gotChar = in.read(c)) || c[0] != FIELD_SEP_CHAR)) {

        if (gotChar)
            SoReadError.post(in, "Expected '"+FIELD_SEP_CHAR+"'; got '"+c[0]+"'");
        else
            SoReadError.post(in, "Expected '"+FIELD_SEP_CHAR+"'; got EOF");

        return false;
    }

    // Read the field name or output name
    if (! in.read(fieldName, true)) {
        SoReadError.post(in,
                          "Premature end of file before connection was read");
        return false;
    }

    // If the base is a node, make sure the field name is valid and
    // then connect to it.
    if (connContainer.isOfType(SoNode.getClassTypeId())) {
        SoNode  node = (SoNode ) connContainer;
        SoField connField;

        connField = node.getField(fieldName);

        if (connField == null) {
            String nodeName = node.getTypeId().getName().getString();
            SoReadError.post(in, "No such field \""+fieldName.getString()+"\" in node "+nodeName);
            return false;
        }

        if (! connectFrom(connField)) {
            String nodeName = node.getTypeId().getName().getString();
            SoReadError.post(in, "Can't connect to field \""+nodeName+"."+fieldName.getString()+"\"");
            return false;
        }
    }

    // If the base is an engine, make sure the name is a valid field
    // or output and then connect to it
    else if (connContainer.isOfType(SoEngine.getClassTypeId())) {
        SoEngine        engine = (SoEngine ) connContainer;
        SoField         connField;
        SoEngineOutput  connOutput;

        connField = engine.getField(fieldName);

        if (connField == null) {

            // See if it's an output
            connOutput = engine.getOutput(fieldName);

            if (connOutput == null) {
                String eName = engine.getTypeId().getName().getString();
                SoReadError.post(in, "No such field or output \""+fieldName.getString()+"\" "+
                                  "in engine "+eName);
                return false;
            }

            if (! connectFrom(connOutput)) {
                String eName = engine.getTypeId().getName().getString();
                SoReadError.post(in,
                                  "Can't connect to engine output \""+eName+"."+fieldName.getString()+"\"");
                return false;
            }
        }
        else {
            if (! connectFrom(connField)) {
                String eName = engine.getTypeId().getName().getString();
                SoReadError.post(in, "Can't connect to field \""+eName+"."+fieldName.getString()+"\"");
                return false;
            }
        }
    }

    // If it is a global field...
    else if (connContainer.isOfType(SoGlobalField.getClassTypeId())) {

        SoGlobalField gf = (SoGlobalField ) connContainer;
        SoField connField = gf.getMyField();

        // Make sure the name in the input is the name of the global field
        if (fieldName != gf.getName()) {
            String globalName = gf.getName().getString();
            SoReadError.post(in, "Wrong field name (\""+fieldName.getString()+"\") for global "+
                              "field \""+globalName+"\"");
            return false;
        }

        if (! connectFrom(connField)) {
            SoReadError.post(in, "Can't connect to global field \""+gf.getName().getString()+"\"");
            return false;
        }
    }

    // If it's not a node, engine or global field, problem
    else {
        SoReadError.post(in, "Trying to connect to a "+
                          connContainer.getTypeId().getName().getString());
        return false;
    }

    return true;
}

	

	/**
	 * Indicates to a field that a change has been made involving a connection
	 * from it (as source) to another field. Passed the number of things being
	 * connected to the field; the number will be negative when things are
	 * disconnected. The default method does nothing.
	 * 
	 * @param numConnections
	 */
	public void connectionStatusChanged(int numConnections) {

	}

	/**
	 * Reads value of field (with given name) from file as defined by SoInput.
	 * This does the work common to all fields, then calls other read methods to
	 * do the rest.
	 * 
	 * @param in
	 * @param name
	 * @return
	 */
////////////////////////////////////////////////////////////////////////
//
//Description:
//Reads field from file. Works for ASCII and binary input. The
//name of the field has already been read and is passed here (for
//error messages).
//
//Use: private

	public boolean read(SoInput in, SbName name) {
    final char[]        c = new char[1];
    boolean        shouldReadConnection = false;
    boolean        gotValue = false;
    final boolean[] readok = new boolean[1]; readok[0] = true;

    // Turn off notification during reading process.  It is turned
    // back on and notification is started below:
    boolean wasNotifyEnabled = flags.notifyEnabled;
    flags.notifyEnabled = false;

  if (in.checkISReference(this.getContainer(), name, readok) || readok[0] == false) {
    if (!readok[0]) {
      SoFieldContainer fc = this.getContainer();
      String s = ("");
      if (fc != null) { s = " of "+ fc.getTypeId().getName().getString(); }
      SoReadError.post(in, "Couldn't read value for field \""+name.getString()+"\""+s);
    }
    //goto sofield_read_return; java port : done with the else
  }
  else {
    
    if (in.isBinary()) {
        final short[]   readFlags = new short[1];

        if (! readValue(in)) {
            SoReadError.post(in,
                              "Couldn't read binary value for field \""+name.getString()+"\"");
            flags.notifyEnabled = wasNotifyEnabled;
            return false;
        }

        // Read flags
        if (! in.read(readFlags)) {
            SoReadError.post(in,
                              "Couldn't read binary flags for field \""+name.getString()+"\"");
            flags.notifyEnabled = wasNotifyEnabled;
            return false;
        }

        // (Don't use setIgnored() to set this, since it would cause
        // notification, which we don't want to have happen for trigger
        // fields.)
        flags.ignored = (readFlags[0] & FIELD_IGNORED) != 0;
        shouldReadConnection = (readFlags[0] & FIELD_CONNECTED) != 0;
        setDefault((readFlags[0] & FIELD_DEFAULT) != 0);
        gotValue      = true;
    }

    // ASCII version...
    else {
        // Check for ignore flag with no value
        if (in.read(c) && c[0] == IGNORE_CHAR) {
            setDefault(true);
            setIgnored(true);

            // Check for connection to engine/field
            if (in.read(c) && c[0] == CONNECTION_CHAR)
                shouldReadConnection = true;
            else
                in.putBack(c[0]);
            gotValue = false;
        }

        else {
            setIgnored(false);

            // If character is connection character, we just use the
            // default value and skip the reading-value stuff
            if (c[0] != CONNECTION_CHAR) {
                in.putBack(c[0]);

                if (! readValue(in)) {
                    SoReadError.post(in,
                                      "Couldn't read value for field \""+name.getString()+"\"");
                    flags.notifyEnabled = wasNotifyEnabled;
                    return false;
                }

                gotValue = true;
                setDefault(false);

                // Check for ignore flag after value
                if (in.read(c) && c[0] == IGNORE_CHAR) {
                    // (Don't use setIgnored() to set this, since it
                    // would cause notification, which we don't want
                    // to have happen for trigger fields.)
                    flags.ignored = true;

                    // Get character to check for connection to
                    // engine/field below.
                    in.read(c);
                }
            } else {
                gotValue = false;
            }

            // Check for connection to engine/field
            if (c[0] == CONNECTION_CHAR)
                shouldReadConnection = true;
            else
                in.putBack(c[0]);
        }
    }

    // Read connection info if necessary.
    if (shouldReadConnection  && !readConnection(in)) {
        flags.notifyEnabled = wasNotifyEnabled;
        return false;
    }

  }
    sofield_read_return:
    // Turn notification back the way it was:
    flags.notifyEnabled = wasNotifyEnabled;

    // If a value was read (even if there's a connection), call
    // valueChanged. Otherwise, just notify.
    if (gotValue) {
        valueChanged(false);
    } else {
        startNotify();
    }

    return true;
	}
	
	

	//
	// Description:
	// Helper routine, allocates the auditorInfo structure, if needed:
	//
	// Use: private, static

	private void createAuditorInfo()
	//
	{
		if (!flags.hasAuditors) {
			SoFieldContainer myContainer = container;
			auditorInfo = new SoFieldAuditorInfo();
			auditorInfo.container = myContainer;
			auditorInfo.connection/* .field */ = null; // java port
			flags.hasAuditors = true;
		}
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Constructor.
	//
	// Use: public

	public SoField()
	//
	////////////////////////////////////////////////////////////////////////
	{
		container = null;
		flags.hasDefault = true;
		flags.ignored = false;
		flags.connected = false;
		flags.converted = false;
		flags.connectionEnabled = true;
		flags.notifyEnabled = false;
		flags.hasAuditors = false;
		flags.isEngineModifying = false;
		flags.readOnly = false;
		;
		flags.dirty = false;
		flags.notifyContainerEnabled = true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Destructor. Also disconnects if connected.
	//
	// Use: public

	public void destructor()
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (flags.connected)
			reallyDisconnect();

		// Make sure any auditors are notified of the impending doom of
		// this field. We don't have to notify the container, since we
		// can't be destroyed unless it is already being destroyed. So
		// just notify the rest of the auditors. The only type of auditors
		// that can be attached to a field are sensors and other fields
		// connected from this one. Both of these are given a chance to
		// detach/disconnect themselves.

		if (flags.hasAuditors) {
			final SoAuditorList auditors = auditorInfo.auditors;
			for (int i = auditors.getLength() - 1; i >= 0; i--) {
				switch (auditors.getType(i)) {
				case SENSOR:
					// Tell sensor that we are going away. (This must be a
					// field sensor, but cast it to a data sensor for ease.)
					((SoDataSensor) auditors.getObject(i)).dyingReference();

					// The call to dyingReference() might remove auditors,
					// shortening the auditors list; make sure we're not
					// trying to access past the end.
					if (i > auditors.getLength())
						i = auditors.getLength();
					break;

				case FIELD: {
					// Disconnect other field from this one
					SoField f = (SoField) auditors.getObject(i);
					SoFieldContainer fc = f.getContainer();

					// If connected to a converter, must remove the other
					// side of the connection:
					if (fc.isOfType(SoFieldConverter.getClassTypeId())) {
						SoFieldConverter converter = (SoFieldConverter) fc;
						final SoFieldList fieldList = new SoFieldList();
						converter.getForwardConnections(fieldList);
						for (int j = 0; j < fieldList.getLength(); j++) {
							((SoField) fieldList.operator_square_bracket(0)).disconnect(); // TODO
																							// bug
																							// ?
						}
						// The converter will be deleted and will
						// disconnect itself from us when all of its
						// forward connections go away.
					} else {
						f.disconnect();
					}
				}
					break;

				default:
					SoDebugError.post("(internal) SoField::~SoField", "Got an auditor of type " + auditors.getType(i));
					break;
				}
			}
			// delete auditorInfo;
			auditorInfo.destructor();
		}
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Returns true if field is an instance of a field of the given type
	// or an instance of a subclass of it.
	//
	// Use: public

	public final boolean isOfType(SoType type)
	//
	////////////////////////////////////////////////////////////////////////
	{
		return getTypeId().isDerivedFrom(type);
	}

	//
	// Description:
	// Creates a converter engine to convert from the given field
	// type to the type of this field. Returns null on error.
	//
	// Use: private

	private SoFieldConverter createConverter(SoType fromFieldType) {
		SoFieldConverter converter;

		// Find the type of the conversion engine (if any)
		SoType converterType = SoDB.getConverter(fromFieldType, getTypeId());

		// If no engine exists for these types, don't connect
		if (converterType.isBad()) {

			// #ifdef DEBUG
			String fromName = fromFieldType.getName().getString();
			SoDebugError.post("SoField.connectFrom", "Connection failed - no conversion supported " + "for types ("
					+ fromName + " --> " + getTypeId().getName().getString() + ")");
			// #endif /* DEBUG */
			return null;
		}

		// If there is a converter, instantiate it and link it in
		converter = (SoFieldConverter) converterType.createInstance();

		// #ifdef DEBUG
		// If it's an abstract class, error. This should never happen.
		if (converter == null)
			SoDebugError.post("SoField.connectFrom", "Connection failed - unable to create an " + "instance of \""
					+ converterType.getName().getString() + "\" converter");
		// #endif /* DEBUG */

		return converter;

	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Utility function that returns converter through which this field
	// is connected.
	//
	// Use: private

	private SoFieldConverter getConverter() {
		return (SoFieldConverter) auditorInfo.connection_engineOutput().getContainer();
	}

	// Initialize ALL Inventor node classes.
	public static void initClasses() {
		// TODO

		SoField.initClass(SoField.class);
		SoSField.initClass(SoSField.class);
		SoMField.initClass(SoMField.class);

		SoSFBool.initClass(SoSFBool.class);
		SoSFColor.initClass(SoSFColor.class);
		SoSFDouble.initClass(SoSFDouble.class);
		// SoSFEngine.initClass();
		SoSFEnum.initClass(SoSFEnum.class);
		SoSFBitMask.initClass(SoSFBitMask.class); // Note: derived from
													// SoSFEnum!
		SoSFFloat.initClass(SoSFFloat.class);
		SoSFImage.initClass(SoSFImage.class);
		SoSFInt32.initClass(SoSFInt32.class);
		SoSFMatrix.initClass(SoSFMatrix.class);
		SoSFMatrixd.initClass(SoSFMatrixd.class);
		SoSFName.initClass(SoSFName.class);
		SoSFNode.initClass(SoSFNode.class);
		// SoSFPath.initClass();
		SoSFPlane.initClass(SoSFPlane.class);
		// SoSFPlaned.initClass();
		SoSFRotation.initClass(SoSFRotation.class);
		SoSFRotationd.initClass(SoSFRotationd.class);
		SoSFShort.initClass(SoSFShort.class);
		SoSFString.initClass(SoSFString.class);
		SoSFTime.initClass(SoSFTime.class);
		SoSFTrigger.initClass(SoSFTrigger.class);
		// SoSFUInt32.initClass();
		SoSFUShort.initClass(SoSFUShort.class);
		SoSFVec2f.initClass(SoSFVec2f.class);
		// SoSFVec2d.initClass();
		SoSFVec3f.initClass(SoSFVec3f.class);
		SoSFVec3d.initClass(SoSFVec3d.class);
		SoSFVec4f.initClass(SoSFVec4f.class);
		// SoSFVec4d.initClass();

		SoMFBool.initClass(SoMFBool.class);
		SoMFColor.initClass(SoMFColor.class);
		SoMFDouble.initClass(SoMFDouble.class);
		// SoMFEngine.initClass();
		SoMFEnum.initClass(SoMFEnum.class);
		// SoMFBitMask.initClass(); // Note: derived from SoMFEnum!
		SoMFFloat.initClass(SoMFFloat.class);
		SoMFInt32.initClass(SoMFInt32.class);
		SoMFMatrix.initClass(SoMFMatrix.class);
		SoMFMatrixd.initClass(SoMFMatrixd.class);
		SoMFName.initClass(SoMFName.class);
		SoMFNode.initClass(SoMFNode.class);
		// SoMFPath.initClass();
		// SoMFPlane.initClass();
		// SoMFPlaned.initClass();
		SoMFRotation.initClass(SoMFRotation.class);
		SoMFRotationd.initClass(SoMFRotationd.class);
		SoMFShort.initClass(SoMFShort.class);
		SoMFString.initClass(SoMFString.class);
		SoMFTime.initClass(SoMFTime.class);
		SoMFUInt32.initClass(SoMFUInt32.class);
		// SoMFUShort.initClass();
		SoMFVec2f.initClass(SoMFVec2f.class);
		// SoMFVec2d.initClass();
		SoMFVec3f.initClass(SoMFVec3f.class);
		SoMFVec3d.initClass(SoMFVec3d.class);
		SoMFVec4f.initClass(SoMFVec4f.class);
		// SoMFVec4d.initClass();

	}

	public static final SoType getClassTypeId(Class<? extends SoField> klass) {
		return typeMap.get(klass);
	}

	public void get(String[] s) {
		// TODO Auto-generated method stub
		s[0] = "";
	}

/*!
  Set type of this field.

  The possible values for \a type is: 0 for ordinary fields, 1 for
  eventIn fields, 2 for eventOut fields, 3 for internal fields, 4 for
  VRML2 exposedField fields. There are also enum values in SoField.h.
*/
public void setFieldType(int type)
{
  this.clearStatusBits(FLAG_TYPEMASK);
  assert(type >=0 && type <= FLAG_TYPEMASK);
  this.setStatusBits((int)(type));
}

/*!
  Return the type of this field.

  \sa setFieldType()
*/
public int getFieldType()
{
  return this.statusbits & FLAG_TYPEMASK;
}

// private methods. Inlined inside this file only.

// clear bits in statusbits
public void clearStatusBits(int bits)
{
  this.statusbits &= ~bits;
}

// sets bits in statusbits
public void setStatusBits(int bits)
{
  this.statusbits |= bits;
}

// return TRUE if any of bits is set
public boolean getStatus(int bits)
{
  return (this.statusbits & bits) != 0;
}

	
}
