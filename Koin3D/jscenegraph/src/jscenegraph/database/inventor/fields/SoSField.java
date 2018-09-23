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

import java.lang.reflect.InvocationTargetException;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all single-value fields.
/*!
\class SoSField
\ingroup Fields
Each class derived from SoSField begins with an 
SoSF prefix and contains one value of a particular type. Each has 
setValue() and getValue() methods
that are used to change or
access this value. In addition, some field classes have extra
convenience routines that allow values to be set or retrieved in other
related formats (see below).


In addition to setValue(), all single-value fields overload the "="
assignment operator to set the field value from the correct datatype
or from another field instance of the same type.


The value of a single-value field is written to file in a format
dependent on the field type; see the subclass man pages for details.


A field that is ignored has a tilde (~)
either in place of the value (if the actual value is the default)
or after it (otherwise).

\par See Also
\par
SoField, SoMField
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoSField<T extends Object> extends SoField {
	
//	private static SoType classTypeId;
//	
//	public static SoType getClassTypeId() { return new SoType(classTypeId); }
	
	protected T value;
	
	public SoSField() {
		value = constructor();
	}
	
	public T getValue() {
		evaluate();
		return value;
	}
	
	public T operator_assign(T newValue) {
		setValue(newValue);
		return value;
	}
	
	/**
	 * Java port
	 * @param newValue
	 */
	public void s(T newValue) {
		setValue(newValue);
	}
	
	/* Set value from a value of the appropriate type */ 
	public void setValue(T newValue) {
		
		if( (newValue instanceof Enum) && (this instanceof SoSFEnum)) {
			try {
				Integer intVal = (Integer) newValue.getClass().getMethod("getValue", null).invoke(newValue, null);
				value = (T)intVal;
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException(e);
			} catch (SecurityException e) {
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
			
		}
		else {
			copyValue(newValue);
		}
		valueChanged();
	}
	
	// java port
	private void copyValue(T newValue) {
		if( value instanceof Mutable) {
			Mutable mv = (Mutable) value;
			mv.copyFrom(newValue);
		}
		else { // T is Immutable
			value = newValue;
		}		
	}
	
	public boolean equals(SoSField<T> f) {
		return getValue().equals(f.getValue());
	}

    public boolean        isSame(final SoField f) {
    	return (getTypeId().operator_equal_equal(f.getTypeId()) &&
    			getValue().equals(((SoSField)f).getValue()));
    }
	
	// java port
    protected abstract T constructor();
    
    // java port
	public void copyFrom(final SoField f) {
		SoSField<T> sf = (SoSField<T>)f;
		copyValue(sf.value);
	}
	
	// java port
	public void destructor() {
		if(value instanceof Destroyable) {
			Destroyable d = (Destroyable)value;
			d.destructor();
		}
		value = null;
		super.destructor();
	}

    //! Reads value of field
    public abstract boolean        readValue(SoInput in);

}
