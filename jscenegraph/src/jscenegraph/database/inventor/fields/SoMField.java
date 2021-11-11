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

import java.util.Objects;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Indexable;
import jscenegraph.port.Mutable;

////////////////////////////////////////////////////////////////////////////////
//! Base class for all multiple-valued fields.
/*!
\class SoMField
\ingroup Fields
Each class derived from SoMField begins with an SoMF prefix
and contains a dynamic array of values of a particular type.  Each has a
setValues() method
that is passed a pointer to a const array of values of the correct
type; these values are copied into the array in the field, making
extra room in the array if necessary. The start and num parameters to
this method indicate the starting array index to copy into and the
number of values to copy.


The getValues() method
for a multiple-value field returns a const pointer to the array of
values in the field. (Because this pointer is const, it cannot be used
to change values in this array.)


In addition, the indexing operator "[]" is overloaded to return the
\p i'th value in the array; because it returns a const reference, it can
be used only to get values, not to set them.


Methods are provided for getting the number of values in the field,
inserting space for new values in the middle, and deleting values.


There are other methods that allow you to set only one value of
several in the field and to set the field to contain one and only one
value.


Two other methods can be used to make several changes to a
multiple-value field without the overhead of copying values into and
out of the fields. The startEditing() method
returns a non-const pointer to the array of values in the field; this
pointer can then be used to change (but not add or remove) any values
in the array. The finishEditing() method
indicates that the editing is done and notifies any sensors or engines
that may be connected to the field.


SoMFields are written to file as a series of values separated by
commas, all enclosed in square brackets.  If the field has no values
(getNum() returns zero), then only the square brackets ("[]") are
written.  The last value may optionally be followed by a comma.  Each
field subtype defines how the values are written; for example, a field
whose values are integers might be written as:
\code
[ 1, 2, 3, 4 ]
or:
[ 1, 2, 3, 4, ]
\endcode

\par See Also
\par
SoNode, SoEngine
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoMField<T extends Object, U extends Indexable<T>> extends SoField {

	protected int num;
	protected int maxNum;
	protected boolean userDataIsUsed;

	protected /*T[]*/U values;

	private int changedIndex, numChangedIndices;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openinventor.inventor.fields.SoField#copyFrom(com.openinventor.
	 * inventor.fields.SoField)
	 */
	@Override
	public void copyFrom(SoField f) {
		this.operator_equal((SoMField<T,U>) f);
	}

	public SoMField<T,U> operator_equal(final SoMField<T,U> f) {
		if (f.getNum() < getNum())
			deleteAllValues();
		setValues(0, /* f.getNum(), */ f.getValues(0));
		return this;
	}
	
    //! Forces this field to have exactly num values, inserting or deleting
    //! values as necessary.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Inserts or deletes values to adjust to the given size
//
//Use: public

    public void                setNum(int n) {
	    if (n > num)
	        insertSpace(num, n-num);
	    else if (n < num)
	        deleteValues(n);
    }

	// Returns the number of values currently in the field.
	public int getNum() {
		evaluate();
		return num;
	}

	// Make sure there is room for newNum vals.
	//
	// Description:
	// This makes sure there is enough memory allocated to hold
	// "newNum" values. It reallocates the values if necessary.
	//
	// Use: protected
	protected void makeRoom(int newNum) // New number of values
	{
		// Allocate room to hold all values if necessary
		if (newNum != num) {
			allocValues(newNum);
		}
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Reads all field values from file. Works for ASCII and binary
	// output. Binary values are read as a chunk.
	//
	// Use: private

	public boolean readValue(SoInput in)
	//
	////////////////////////////////////////////////////////////////////////
	{
		if (in.isBinary()) {
			final int[] numToRead = new int[1];

			// Read number of values
			if (!in.read(numToRead)) {
				SoReadError.post(in, "Couldn't read number of binary values " + "in multiple-value field");
				return false;
			}

			// Make space for values; also sets number of values
			makeRoom(numToRead[0]);

			// Read values
			if (!readBinaryValues(in, numToRead[0]))
				return false;
		}

		else {
			final char[] c = new char[1];
			int curIndex = 0;

			// Check for multiple field values
			if (in.read(c) && c[0] == OPEN_BRACE_CHAR) {

				// Check for no values: just an open and close brace
				if (in.read(c) && c[0] == CLOSE_BRACE_CHAR)
					; // Do nothing now

				else {
					in.putBack(c[0]);

					while (true) {

						// Make some room at end if necessary
						if (curIndex >= getNum())
							makeRoom(getNum()*3/2 + VALUE_CHUNK_SIZE);

						if (!read1Value(in, curIndex++) || !in.read(c)) {
							SoReadError.post(in, "Couldn't read value " + curIndex + " of field");
							return false;
						}

						if (c[0] == VALUE_SEPARATOR_CHAR) {

							// See if this is a trailing separator (right before
							// the closing brace). This is legal, but ignored.

							if (in.read(c)) {
								if (c[0] == CLOSE_BRACE_CHAR)
									break;
								//								else
								//									in.putBack(c[0]);
							}
						}

						else if (c[0] == CLOSE_BRACE_CHAR)
							break;

						if (c[0] == '}') { //COIN3D
							SoReadError.post(in, "Premature end of array, got '"+c[0]+"'");
							return false;
						}

						in.putBack(c[0]);

						// COIN3D : This is no more an error
						// else {
						//     SoReadError.post(in, "Expected '" + VALUE_SEPARATOR_CHAR + "' or '" + CLOSE_BRACE_CHAR
						//     + "' but got " + "'" + c[0] + "' while reading value " + curIndex);
						//     return false;
						// }
					}
				}

				// If extra space left over, nuke it
				if (curIndex < getNum())
					makeRoom(curIndex);
			}

			else {
				// Try reading 1 value
				in.putBack(c[0]);
				makeRoom(1);
				if (!read1Value(in, 0))
					return false;
			}
		}

		return true;
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Reads array of binary values from file as one chunk.
	//
	// Use: private

	protected boolean readBinaryValues(SoInput in, // Reading specification
			int numToRead) // Number of values to read
	//
	////////////////////////////////////////////////////////////////////////
	{
		int i;

		for (i = 0; i < numToRead; i++)
			if (!read1Value(in, i))
				return false;

		return true;
	}

	// ! Reads one indexed value of field from file
	public abstract boolean read1Value(SoInput in, int index);

	protected void allocValues(int newNum) {
		if (values == null) {
			if (newNum > 0) {
				values = arrayConstructor(newNum);
//				for (int i = 0; i < newNum; i++) { done in arrayConstructor
//					values.setO(i, constructor());
//				}
			    this.userDataIsUsed = false;
			}
		} else {
			/*T[]*/U oldValues = values;
			int i;

			if (newNum > 0) {				
				values = arrayConstructor(newNum);
//				for (i = 0; i < newNum; i++) { done in arrayConstructor
//					values.setO(i, constructor());
//				}
			    this.userDataIsUsed = false;
			    values.copy(Math.min(num,newNum),oldValues);
			} else
				values = null;
			if(!this.userDataIsUsed) {
				if( oldValues instanceof Destroyable ) {
					Destroyable d = (Destroyable) oldValues;
					Destroyable.delete(d);
				}
			}
		}

		num = maxNum = newNum;
	}

	/* Set field to have one value */
	public void setValue(T newValue) {
		makeRoom(1);
		if (values.getO(0) instanceof Mutable) {
			Mutable dest = (Mutable) (values.getO(0));
			Mutable src = (Mutable) newValue;
			dest.copyFrom(src);
		} else if( newValue instanceof Integer && values.getO(0) instanceof Float){
			values.setO(0, (T)Float.valueOf(((Integer)newValue).floatValue()));
		}
		else {
			values.setO(0, newValue);
		}
		valueChanged();
	}

	public void setValues(int start, /*T[]*/U newValues) {
		int localNum = newValues != null ? newValues.length() : 0;
		int newNum = start + localNum, i;

		if (newNum > getNum())
			makeRoom(newNum);

		for (i = 0; i < localNum; i++) {
			if (values.getO(start + i) instanceof Mutable) {
				((Mutable) values.getO(start + i)).copyFrom((Mutable) newValues.getO(i));
			} else {
				values.setO(start + i, newValues.getO(i));
			}
		}
		valueChanged();

	}

	/* Set 1 value at given index */
	public void set1Value(int index, T newValue) {
		if (index >= getNum())
			makeRoom(index + 1);
		if (values.getO(index) instanceof Mutable) {
			((Mutable) values.getO(index)).copyFrom((Mutable) newValue);
		} else {
			values.setO(index, newValue);
		}
		valueChanged();
	}

	/* Get pointer into array of values */
	public /*T[]*/U getValues(int start) {
		evaluate();
		
		if(values == null) {
			if (start == 0) {
				return null;
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		
		return doGetValues(start);
//		
//		if(start == 0) {
//			return values;
//		}
//
//		int retLength = values.length - start;
//
//		T[] retVal = arrayConstructor(retLength);
//
//		for (int i = 0; i < retLength; i++) {
//			retVal[i] = (T) values[i + start];
//		}
//		return retVal;
	}
	
	private final U doGetValues(int start) {
		return (U) values.plus(start);
	}

	public T operator_square_bracket(int i) {
		evaluate();
		return (T) values.getO(i);
	}

	protected abstract T constructor();

	protected abstract /*T[]*/U arrayConstructor(int length);

	// ! Deletes all current values
	protected void deleteAllValues() {
		allocValues(0);
	}

	// ! Copies value indexed by "from" to value indexed by "to"
	protected void copyValue(int to, int from) {
		if (values.getO(to) instanceof Mutable) {
			((Mutable) (values.getO(to))).copyFrom(values.getO(from));
		} else {
			values.setO(to, values.getO(from));
		}
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// Deletes num values starting at index start. A num of -1 (the default)
	// means delete all values after start, inclusive.
	//
	// Use: public

	// java port
	public void deleteValues(int start) {
		deleteValues(start, -1);
	}

//	public void deleteValues(int start, // Starting index
//			int numToDelete) // Number of values to delete
//	//
//	////////////////////////////////////////////////////////////////////////
//	{
//		int lastToCopy, i;
//
//		if (numToDelete < 0)
//			numToDelete = getNum() - start;
//
//		// Special case of deleting all values
//		if (numToDelete == getNum())
//			deleteAllValues();
//
//		else {
//			// Copy from the end of the array to the middle
//			lastToCopy = (getNum() - 1) - numToDelete;
//			for (i = start; i <= lastToCopy; i++)
//				copyValue(i, i + numToDelete);
//
//			// Truncate the array
//			makeRoom(getNum() - numToDelete);
//		}
//
//		// The field value has changed...
//		valueChanged();
//	}

	/*!
      Remove value elements from index \a start up to and including index
      \a start + \a num - 1.

      Elements with indices larger than the last deleted element will
      be moved downwards in the value array.

      If \a num equals -1, delete from index \a start and to the end of
      the array.
    */
	public void
	deleteValues(int start, int numarg)
	{
		// Note: this function is overridden in SoMFNode, SoMFEngine and
		// SoMFPath, so if you do any changes here, take a look at those
		// methods as well (they are collected in the common template
		// MFNodeEnginePath.tpl).

		// Don't use getNum(), so we avoid recursive evaluate() calls.
		int oldnum = this.num;

		if (numarg == -1) numarg = oldnum - start;
		if (numarg == 0) return;
		int end = start + numarg; // First element behind the delete block.

//#if COIN_DEBUG
		if (start < 0 || start >= oldnum || end > oldnum || numarg < -1) {
			SoDebugError.post("SoMField::deleteValues",
					"invalid indices ["+start+", "+(end-1)+"] for array of size "+oldnum);
			return;
		}
//#endif // COIN_DEBUG

		// Move elements downward to fill the gap.
		for (int i = 0; i < oldnum-(start+numarg); i++)
			this.copyValue(start+i, start+numarg+i);

		// Truncate array.
		this.allocValues(oldnum - numarg);

		// Send notification.
		this.valueChanged();
	}


	/*!
  Can be used to make Coin delete the array pointer set through
  a setValuesPointer() call. See SoMField documentation for
  information about the setValuesPointer() function.

  This method is a TGS extension (introduced in TGS OIV v3.0) and is
  supported only for compatibility. We suggest that you don't use it
  since it can lead to hard-to-find bugs.

  \since Coin 2.0
  \since TGS Inventor 3.0
*/
public void enableDeleteValues()
{
  this.userDataIsUsed = false;
}

/*!
  Returns whether SoMField::enableDeleteValues() has been
  called on a field. The result is only valid if setValuesPointer()
  has been called on the field first.

  This method is a TGS extension (introduced in TGS OIV v3.0) and is
  supported only for compatibility. We suggest that you don't use it
  since it can lead to hard-to-find bugs.

  \since Coin 2.0
  \since TGS Inventor 3.0
*/
public boolean isDeleteValuesEnabled()
{
  return !this.userDataIsUsed;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Inserts space for num values starting at start.  The initial
//    values in the new space are undefined.
//
// Use: public

public void
insertSpace(int start,        // Starting index
                      int numToInsert)          // Number of spaces to insert
//
////////////////////////////////////////////////////////////////////////
{
    // Expand the array
    makeRoom(getNum() + numToInsert);

    // Copy stuff out of the inserted area to later in the array
    for (int i = num - 1; i >= start + numToInsert; --i)
        copyValue(i, i - numToInsert);

    // The field value has changed...
    valueChanged();
}

	

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// This is equivalent to the SoField::set() method, but operates on
	// only the value given by the index.
	//
	// Use: public

	public boolean set1(int index, String valueString)
	//
	////////////////////////////////////////////////////////////////////////
	{
		final SoInput in = new SoInput();
		try {
			in.setBuffer(valueString, valueString.length());

			if (read1Value(in, index)) {

				// We have to do this here because read1Value() doesn't
				// indicate that values have changed, since it's usually used
				// in a larger reading context.
				valueChanged();

				return true;
			}
			return false;
		} finally {
			in.destructor();
		}
	}

	////////////////////////////////////////////////////////////////////////
	//
	// Description:
	// This is equivalent to the SoField::get() method, but operates on
	// only the value given by the index.
	//
	// Use: public

	public String get1(int index) {
		String valueString = "";
		// TODO
		
		throw new RuntimeException();
		
		//return valueString;
	}

	public boolean operator_equal_equal(final SoMField<T,U> f) {
		int i, localNum = getNum();
		/*T[]*/U myVals, itsVals;

		if (localNum != f.getNum())
			return false;

		myVals = getValues(0);
		itsVals = f.getValues(0);

		for (i = 0; i < localNum; i++)
			if (!Objects.equals(myVals.getO(i), itsVals.getO(i)))
				return false;

		return true;
	}

	public boolean isSame(final SoField f) {
		return (getTypeId().operator_equal_equal(f.getTypeId()) && this.operator_equal_equal((SoMField) f));
	}

	/**
	 * java port
	 * @param i
	 * @return
	 */
	public T getValueAt(int i) {
		return operator_square_bracket(i);
	}

    /* Get non-const pointer into array of values for batch edits */          
    public /*T[]*/U startEditing()                                
        {
    	return getValues(0); // java port
//    	evaluate(); 
//    	return values; 
    	}                                        
                                                                              
    /* Indicate that batch edits have finished */                             
    public void finishEditing() { valueChanged(); }           
                                                                              
	public void destructor() {
		  enableNotify(false); /* Avoid notifying destructed containers. */ 
		  deleteAllValues(); 
		super.destructor();
	}

	public void setChangedIndices() {
    	setChangedIndices(-1,0);
	}
	public void setChangedIndices(final int chgidx, final int numchgind)
	{
		this.changedIndex = chgidx;
		this.numChangedIndices = numchgind;
	}
}
