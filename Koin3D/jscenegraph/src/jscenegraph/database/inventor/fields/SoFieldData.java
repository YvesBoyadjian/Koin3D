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
 |      Definition of the SoFieldData class, which is used by all objects
 |      that have fields.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */


package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Offset;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoFieldData
///
///  The SoFieldData class holds data about fields of an object: the
///  number of fields the object has, the names of the fields, and a
///  pointer to a prototype instance of the field. (Typically, this
///  prototype field is contained within a prototype instance of the
///  node or engine.)
///
///  All objects of a given class share the same field data. Therefore,
///  to get information about a particular field in a particular object
///  instance, it is necessary to pass that instance to the appropriate
///  method.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFieldData implements Destroyable {
	

	// Syntax for reading/writing type information to files
	public static final char OPEN_BRACE_CHAR       =  '[';
	public static final char CLOSE_BRACE_CHAR       = ']';
	public static final char VALUE_SEPARATOR_CHAR   = ',';

// In the binary file format, the number of fields integer is actually
// a bitfield containing the number of fields written and whether or
// not there is a description of those fields following.  This assumes
// there are less than 2^14 (16,384) different fields in any node.
// We also assume that unsigned shorts have at least 14 bits.
public static final int NOT_BUILTIN_BIT = (1<<14);

	
	
	private final SbPList fields; //!< List of fields (SoFieldEntry)
	private final SbPList enums = new SbPList(); //!< List of enums (SoEnumEntry)
	
	private static class SoFieldEntry implements Destroyable {
		public final SbName name = new SbName(); // Name of field
		public Offset offset; // Offset of field within object
		
		public SoFieldEntry() {
			
		}
		
		public SoFieldEntry(SoFieldEntry other) {
			name.copyFrom(other.name);
			offset = other.offset;
		}

		@Override
		public void destructor() {
			name.destructor();
			offset = null;
		}
	}

	 
	   private static class SoEnumEntry implements Destroyable {
	        final SbName              typeName = new SbName();       // Name of enum type
	        int                 num;            // number of values
	        int                 arraySize;      // size of arrays
	        int[]                 vals;          // array of values
	        SbName[]              names;         // array of names
	   
	       SoEnumEntry(final SbName name) {
		       typeName.copyFrom(name);
		       num         = 0;
		       arraySize   = growSize;
		       vals        = new int[arraySize];
		       names       = new SbName[arraySize];
	    	   
	       }
	       SoEnumEntry(final SoEnumEntry o) {
		       typeName.copyFrom( o.typeName);
		    	       num         = o.num;
		    	       arraySize   = num;
		    	       vals                = new int[arraySize];
		    	       names               = new SbName[arraySize];
		    	       for (int i=0; i<num; i++) {
		    	           vals[i] = o.vals[i];
		    	           names[i] = new SbName(o.names[i]);
		    	       }
	    	   
	       }
	   
	       static int  growSize = 6;               // amount to grow arrays

		@Override
		public void destructor() {
			typeName.destructor();
		       vals = null;
		       names = null;	    	   
		}
	   };	   
	   
	   public SoFieldData() {
		   fields = new SbPList();
	   }
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Copy constructor
	   //
	   // Use: public
	   
	   public SoFieldData(final SoFieldData src) {
		   	   
	           fields = new SbPList(src != null ? src.fields.getLength() : 0);
	   //
	   ////////////////////////////////////////////////////////////////////////
	   
	       copy(src);
	   }
	   	
	/**
	 * Adds a field to current data, given default value object, 
	 * name of field and pointer to field within default value object. 
	 * This is used to define fields of a FieldContainer (node or engine) class. 
	 * 
	 * @param defobj
	 * @param fieldName
	 * @param field
	 */
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Adds a field to current fieldData.
	   //
	   // Use: public
	   
	   public void
	   addField(SoFieldContainer defobj, // Object with default values
	                         String fieldName,    // Name of field
	                         final SoField field)     // Pointer 2 field (in defNode)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoFieldEntry newField = new SoFieldEntry();
	       newField.name.copyFrom(new SbName(fieldName));
	       newField.offset = new Offset(fieldName);
	   
	       fields.append((Object) newField);
	   }
	   
	   /////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //
	    //     Strips leading and trailing white space off a string, returning
	    // an SbName.
	    // 
	    // Use: private in this file.  Should really be a method of SbName
	    //
	    //#include <ctype.h>
	    static SbName stripWhite(String name)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        int firstchar = -1;
	        int lastchar = -1;
	        int lastwhite = -1;
	    
	        // scan string, marking first & last non-white char, and last white
	        int i;
	        for (i=0; i < name.length(); i++) {
	            if (Character.isSpaceChar(name.charAt(i)))
	                lastwhite = i;
	            else {
	                if (firstchar == -1) firstchar = i;
	                lastchar = i;
	            }
	        }
	    
	        // No trailing whitespace?  -- just start at first non-white char
	        if (lastchar > lastwhite)
	            return new SbName(name.substring(firstchar));
	    
	        // trailing whitespace -- need to copy into buffer (to be able
	        // to place trailing NULL-termination, since the the string is const).
	        char[] buf = new char[lastchar - firstchar +1];
	        int b;
	        for (b=0, i=firstchar; i<=lastchar; i++, b++)
	            buf[b] = name.charAt(i);
	        //buf[b] = 0;
	        return new SbName(new String(buf));
	    }
	    	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Adds an enum value to current fieldData.  If the enum type
	    //    hasn't been added yet, a new entry is created for it.
	    //
	    // Use: public
	    
	   public void
	    addEnumValue(String typeNameArg, String valNameArg,
	                              int val)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SoEnumEntry  e = null;
	        final SbName typeName = stripWhite(typeNameArg);
	        final SbName valName = stripWhite(valNameArg);
	    
	        // look for an entry for this type name
	        for (int i=0; i<enums.getLength(); i++) {
	            e = ( SoEnumEntry ) enums.get(i);
	            if (e.typeName.operator_equal_equal(typeName))
	                break;
	            else
	                e = null;
	        }
	        // make an entry if there wasn't one already
	        if (e == null) {
	            e = new SoEnumEntry(typeName);
	            enums.append((Object) e);
	        }
	        // grow arrays if needed
	        if (e.num == e.arraySize) {
	            e.arraySize += SoEnumEntry.growSize;
	            int[] ovals = e.vals;
	            SbName[] onames = e.names;
	            e.vals = new int[e.arraySize];
	            e.names = new SbName[e.arraySize];
	            for (int i=0; i<e.num; i++) {
	                e.vals[i] = ovals[i];
	                e.names[i] = onames[i];
	            }
	            //delete [] ovals;
	            //delete [] onames;
	        }
	        e.vals[e.num] = val;
	        e.names[e.num] = valName;
	        e.num++;
	    }
	    	  	
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Returns name/value arrays for a given enum type.
	    //
	    // Use: public
	    
	   public void
	    getEnumData(String typeNameArg, final int[] num,
	                             final int[][] vals, final SbName[][] names)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SbName typeName = stripWhite(typeNameArg);
	    
	        // look for an entry for this type name
	        for (int i=0; i<enums.getLength(); i++) {
	            SoEnumEntry e = ( SoEnumEntry ) enums.get(i);
	            if (e.typeName.operator_equals(typeName)) {
	                num[0]         = e.num;
	                vals[0]        = e.vals;
	                names[0]       = e.names;
	                return;
	            }
	        }
	        // no entry found.
	        num[0] = 0;
	        vals[0] = null;
	        names[0] = null;
	    }
	    	   
	   
	/**
	 * Copy values and flags of fields from one object to another (of the same type). 
	 * If copyConnections is true, any connections to the fields are 
	 * copied as well 
	 * 
	 * @param to
	 * @param from
	 * @param copyConnections
	 */
	public void overlay(SoFieldContainer to, SoFieldContainer from, boolean copyConnections) {
		
	     // Access both field datas instead of just one, in case they
		       // differ per instance.
		       final SoFieldData fromFD = from.getFieldData();
		       final SoFieldData   toFD =   to.getFieldData();
		   
		       SoField     fromField, toField;
		       int         i;
		   
		       for (i = 0; i < fromFD.fields.getLength(); i++) {
		   
		           // Access the fields using the appropriate field data instances
		           toField   =   toFD.getField(to,   i);
		           fromField = fromFD.getField(from, i);
		   
		           // If both fields have default values, we don't bother copying
		           // the value:
		           if (! fromField.isDefault() || ! toField.isDefault())
		               // This method copies just the value...
		               toField.copyFrom(fromField);
		   
		           // ... so we still have to copy the rest
		           toField.setIgnored(fromField.isIgnored());
		           toField.setDefault(fromField.isDefault());
		           toField.enableNotify(fromField.isNotifyEnabled());
		   
		           // Allow the field to fix itself up after the copy. Most
		           // fields do nothing here, but node, path, and engine fields
		           // need to make sure instances are handled properly.
		           toField.fixCopy(copyConnections);
		   
		           // Copy the connection if necessary
		           if (fromField.isConnected() && copyConnections)
		               toField.copyConnection(fromField);
		       }
		  	}

	// Returns number of fields.
	public int getNumFields() {
		 return fields.getLength(); 
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns name of field with given index in given object.
//
// Use: public

public SbName 
getFieldName(int index)
//
////////////////////////////////////////////////////////////////////////
{
    return ((SoFieldEntry ) fields.operator_square_bracket(index)).name;
}

	
	// Returns pointer to field with given index within given object instance. 
	public SoField getField(SoFieldContainer object, int index) {
		
		 // This generates a CC warning; there's not much we can do about it...
		SoFieldEntry fieldEntry = (SoFieldEntry) fields.operator_square_bracket(index); 
		  return (SoField) (object.plus(fieldEntry.offset));
	}
	
	// Returns index of field, given the field and the instance it is in. 
	public int getIndex(SoFieldContainer fc, SoField field) {
		
	     for (int i = 0; i < getNumFields(); i++)
	    	           if (getField(fc, i) == field)
	    	               return i;
	    	   
//	    	   #ifdef DEBUG
	    	       SoDebugError.post("SoFieldData::getIndex", "Can't find index for field");
//	    	   #endif /* DEBUG */
	    	   
	    	       return -1;
	    	   	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Copies all fields from given field data into this one, creating
	   //    the new fields. This should be used only for copying fields from
	   //    a base class into a derived class, since that's the only
	   //    situation in which we are guaranteed that the field offsets are
	   //    the same.
	   //
	   // Use: internal
	   
	  public void
	   copy(final SoFieldData from)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       int                 i;
	       if (from == null)
	           return;
	   
	       // Copy field entries
	       for (i = 0; i < from.fields.getLength(); i++) {
	   
	           SoFieldEntry fromField =
	               (SoFieldEntry ) from.fields.operator_square_bracket(i);
	   
	           fields.append((Object) new SoFieldEntry(fromField));
	       }
	   
	       // Copy enum entries
	       for (i = 0; i < from.enums.getLength(); i++) {
	   
	           SoEnumEntry fromEnum = (SoEnumEntry ) from.enums.operator_square_bracket(i);
	   
	           enums.append((Object) new SoEnumEntry(fromEnum));
	       }
	   }

	@Override
	public void destructor() {
	    SoFieldEntry tmpField;
	    SoEnumEntry  tmpEnum;

	    // Delete the list of fields and enums;
	    for (int i=0; i<fields.getLength(); i++) {
	        tmpField = ( SoFieldEntry )fields.operator_square_bracket(i);
	        tmpField.destructor();
	    }

	    for (int j=0; j<enums.getLength(); j++) {
	        tmpEnum = ( SoEnumEntry )enums.operator_square_bracket(j);
	        tmpEnum.destructor();
	    }
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads field type information for an unrecognized node.  The
//    ASCII syntax is a set of field type - field name pairs,
//    separated by commas, enclosed withing square brackets.  Returns
//    true on success, false if there is a syntax error or
//    unrecognized field type.
//
//    numDescriptions is a very large number when reading ASCII (where
//    we just read until the closing square brace).
//
// Use: private

private boolean readFieldDescriptions(
    SoInput in, SoFieldContainer object, int numDescriptions)

//
////////////////////////////////////////////////////////////////////////
{
    boolean gotChar;
    final SbName fieldType = new SbName();//, fieldName; java port
    final char[]   c = new char[1];

    boolean isBinary = in.isBinary();

    boolean hadFieldsDefined = fields.getLength() > 0;

    if (!isBinary) 
        if (! ((gotChar = in.read(c)) || c[0] != OPEN_BRACE_CHAR))
            return false;

    for (int i = 0; i < numDescriptions; i++) {

        // Check for closing brace:
        if (!isBinary) {
            // Check for closing brace:
            if (in.read(c) && c[0] == CLOSE_BRACE_CHAR)
                return true;
            else in.putBack(c[0]);
        }

        final SbName type = new SbName(), fieldName = new SbName();
        if (!in.read(type, true)) return false;
        if (!in.read(fieldName, true)) return false;

        SoType fldType = SoType.fromName(type);

        if (!hadFieldsDefined) {
            // Only create fields if fields haven't already been
            // defined.  This isn't 100% correct-- we'll create
            // field for nodes/engines that have not fields if the
            // user specifies fields/inputs for them.  But that's
            // a case I'm not going to worry about (there are VERY
            // few nodes that have no fields).

            if (fldType.isBad())
                return false;

            // Create and initialize an instance of the field.
            // Add it to the field data.
            SoField fld = (SoField )(fldType.createInstance());
            fld.setContainer(object);

            // Cast const away:
            SoFieldData This = (SoFieldData )this;
            This.addField(object, fieldName.getString(), fld);
        }
//#ifdef DEBUG
        else {
            // Check to make sure specification matches reality:
            SoField f = object.getField(fieldName);
            if (f == null) {
                SoDebugError.post("SoFieldData::readFieldDescriptions",
                		object.getTypeId().getName().getString()+" does not have a field named "+fieldName.getString());
            }
            else if (!f.isOfType(fldType)) {
                SoDebugError.postWarning("SoFieldData::readFieldDescriptions",
                		object.getTypeId().getName().getString()+"."+fieldName.getString()+" is type "+f.getTypeId().getName().getString()+", NOT type "+type.getString());
            }
        }
//#endif
        if (!isBinary) {
            // Better get a ',' or a ']' at this point:
            if (! in.read(c))
                return false;
            if (c[0] != VALUE_SEPARATOR_CHAR) {
                if (c[0] == CLOSE_BRACE_CHAR)
                    return true;
                else return false;
            }
            // Got a ',', continue reading
        }
    }

    return true;
}


////////////////////////////////////////////////////////////////////////
//
//Description:
//Reads into fields of object according to SoInput. The third
//parameter indicates whether an unknown field should be reported
//as an error (default is true); this can be false for nodes that
//have children.
//
//Use: internal

	public boolean read(SoInput in, SoFieldContainer object, boolean errorOnUnknownField, final boolean[] notBuiltIn) {
	    // true if reading an Inventor file pre-Inventor 2.1:
	    boolean        oldFileFormat = (in.getIVVersion() < 2.1f);

	    // true if reading binary file format:
	    boolean        isBinary = in.isBinary();

	    // Assume it is a built-in node, until we figure out otherwise:
	    notBuiltIn[0] = false;

	    // COMPATIBILITY case:

	    if (oldFileFormat && isBinary) {

	        // If 2.0 file format, and the thing we're reading
	        // isn't (and, we assume, wasn't) built-in:
	        if (in.getIVVersion() > 1.0f && !object.getIsBuiltIn()) {
	            // The "fields" or "inputs" string is read by SoBase,
	            // because it may need them to decide whether or not to
	            // create an UnknownNode or an UnknownEngine.
	            final int[] numDescriptions = new int[1];
	            if (!in.read(numDescriptions))
	                return false;

	            notBuiltIn[0] = true;
	            // The rest of it is just like 2.1 format, IF the object
	            // has any fields:
	            if (!readFieldDescriptions(in, object, numDescriptions[0]))
	                return false;
	        }
	        // In the old file format, objects with no fields
	        // didn't write out anything for the field values:
	        if (fields.getLength() == 0) return true;

	        // This is mostly like Inventor 2.1 file format, except that
	        // there is no NOT_BUILTIN_BIT:
	        final int[] numFieldsWritten = new int[1];
	        if (!in.read(numFieldsWritten))
	            return false;
	        return readFields(in, object, numFieldsWritten[0]);
	    }

	    // BINARY case:

	    else if (isBinary) {
	        final short[] numWritten = new short[1];

	        // First read number of fields written
	        if (! in.read(numWritten))
	            return false;

	        // Figure out if field descriptions were written:
	        if ((numWritten[0] & NOT_BUILTIN_BIT)!=0) {
	            notBuiltIn[0] = true;
	            numWritten[0] &= (~NOT_BUILTIN_BIT); // Clear bit
	        }

	        if (notBuiltIn[0]) {
	            if (!readFieldDescriptions(in, object, numWritten[0]))
	                return false;
	        }

	        return readFields(in, object, numWritten[0]);
	    }

	    // ASCII case:
	    else {

	        // Only check for "fields" or "inputs" the first time:
	        boolean firstTime = true;

	        final SbName fieldName = new SbName();

	        // Keep reading fields until done
	        while (true) {

	            // If no field name, just return.
	            if (!in.read(fieldName, true) || fieldName.operator_not())
	                return true;

	            // Read field descriptions.  Field descriptions may be
	            // given for built-in nodes, and do NOT have to be given
	            // for non-built-in nodes as long as their code can be
	            // DSO-loaded.
	            if (firstTime) {
	                firstTime = false;
	                if (fieldName.operator_equal_equal(new SbName("fields")) || fieldName.operator_equal_equal(new SbName("inputs"))) {
	                    notBuiltIn[0] = true;
	                    if (!readFieldDescriptions(in, object, NOT_BUILTIN_BIT))
	                        return true;
	                    continue;
	                }
	            }

	            final boolean[] foundName = new boolean[1];
	            if (! read(in, object, fieldName, foundName))
	                return false;

	            // No match with any valid field name
	            if (!foundName[0]) {
	                if (errorOnUnknownField) {
	                    SoReadError.post(in, "Unknown field \""+fieldName.getString()+"\"");
	                    return false;
	                }
	                else {
	                    // Put the field name back in the stream
	                    in.putBack(fieldName.getString());
	                    return true;
	                }
	            }
	        }
	    }
	}
	

////////////////////////////////////////////////////////////////////////
//
// Used when reading binary file format:
//
// Use: internal

public boolean readFields(SoInput in, SoFieldContainer object,
                       int numWritten)
//
////////////////////////////////////////////////////////////////////////
{
    final SbName fieldName = new SbName();

    // Read the fields - no tolerance for bad data
    for (int f = 0; f < numWritten; f++) {
        if (! in.read(fieldName, true) || fieldName.operator_not())
            return false;

        final boolean[] foundName = new boolean[1];
        if (! read(in, object, fieldName, foundName))
            return false;

        // No match with any valid field name
        if (! foundName[0]) {
            SoReadError.post(in, "Unknown field \""+fieldName.getString()+"\"");
            return false;
        }
    }
    return true;
}

	
////////////////////////////////////////////////////////////////////////
//
// This function is used when the field name has already been
// read, and just the value needs to be read in.  It is used by
// the above read() method and to read in GlobalFields.
//
// Use: internal

private boolean read(SoInput in, SoFieldContainer object,
                  final SbName fieldName, final boolean[] foundName)
//
////////////////////////////////////////////////////////////////////////
{
    int i;
    for (i = 0; i < fields.getLength(); i++) {
        if (fieldName.operator_equal_equal(getFieldName(i))) {
            if (! getField(object, i).read(in, fieldName))
                return false;
            break;
        }
    }
    if (i == fields.getLength())
        foundName[0] = false;
    else foundName[0] = true;

    return true;
}

	
	   }
