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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file contains the GlobalField class declaration.
 |      GlobalField is really only a special kind of container for
 |      ordinary SoFields.
 |      It is an internal class.
 |
 |   Classes:
 |      SoGlobalField
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGlobalField extends SoFieldContainer {
	
    private final SoSFName typeField = new SoSFName(); /* The type of field this contains */
  	private static SbDict nameDict;
	
	// The GlobalFieldContainer type (the name is simply GlobalField)
	private static SoType classTypeId;
	
    private SoField value;     /* The field all this is for! */
    private SoFieldData         fieldData;     /* FieldData containing value */
     
 	//
	// Description:
	// Setup type information. Called by SoDB::init()
	//
	// Use: internal
	public static void initClass() {
		
		classTypeId = SoType.createType(SoFieldContainer.getClassTypeId(),
				    new SbName("GlobalField"));
		nameDict = new SbDict(20); // Assume small number of global fields
	}

	public static SoType getClassTypeId() {
		return new SoType(classTypeId);
	}
	

    //! Create a new global field with the given name and of the given
    //! type, if one doesn't yet exists.  This returns NULL if one
    //! can't be created for some reason. "alreadyExists" will be set
    //! to TRUE if a global field of the same name and type already exists.
//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      This either creates a new globalField (if name hasn't been found
//      before) or returns the globalField with the given name and type.
//      It will return NULL if there is an error. "alreadyExists" will be
//      set to TRUE if a global field of the same name and type already
//      exists.
//
// Use: internal

public static SoGlobalField 
create(final SbName name, SoType type, final boolean[] alreadyExists)
//
//////////////////////////////////////////////////////////////////////////////
{
    // Look for an existing field of the same name/type:
    SoGlobalField result = SoGlobalField.find(name);
    if (result != null) {
        if (! result.getType().isDerivedFrom(type)) {
            String resName = result.getName().getString();
            String resTypeName = result.getType().getName().getString();
            SoDebugError.post("SoGlobalField::create",
                               "Global field "+resName+" has two types ("+resTypeName+", "+type.getName().getString()+")");
            return null;
        }
        else {
            alreadyExists[0] = true;
            return result;
        }
    }

    alreadyExists[0] = false;

    // Make sure field is a proper field type

    if (! type.isDerivedFrom(SoField.getClassTypeId(SoField.class))) {
        String resName = result.getName().getString();
        SoDebugError.post("SoGlobalField::create",
                           "(field "+resName+") Type "+type.getName().getString()+" is not a field type");
        return null;
    }
    SoField field = (SoField )type.createInstance();

    // Make sure it isn't an abstract field type...

    if (field == null) {
        String  typeName = type.getName().getString();
        SoDebugError.post("SoGlobalField::create",
                           "Cannot create field "+name.getString()+" of type "+typeName);
        return null;
    }

    return new SoGlobalField(name, field);
}

	
	
	 //////////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //      GlobalField constructor.  It creates a globalField, given the name
	   //      of the field and a pointer to the field to use.  This is private;
	   //      the create() method is the public interface to creating new global
	   //      fields.
	   //
	   // Use: private
	   
	   public SoGlobalField(final SbName name, final SoField field)
	   //
	   //////////////////////////////////////////////////////////////////////////////
	   {
	       typeField.setValue(field.getTypeId().getName());
	       value = field;
//	   #ifdef DEBUG
//	       if (field.getContainer())
//	           SoDebugError::post("SoGlobalField::SoGlobalField",
//	                              "Field already has container!");
//	   
//	   #endif /* DEBUG */
	       field.setContainer(this);
	       field.setDefault(false);
	   
	       Object key = (Object)(name.getString());
//	   #ifdef DEBUG
//	       void *junk;
//	       if (nameDict.find(key, junk) != FALSE)
//	           SoDebugError::post("SoGlobalField::SoGlobalField",
//	                              "There is already a global field named %s",
//	                              name.getString());
//	   #endif
	       nameDict.enter(key, this);
	   
	       fieldData = new SoFieldData();
	       fieldData.addField(this.getClass(),this, name.getString(), field);
	   
	       ref();
	   }
	   
//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      Destructor.  It is responsible for deleting the globalField from
//      the dictionary.
//
// Use: private

public void destructor()
//
//////////////////////////////////////////////////////////////////////////////

{
    // If fieldData is NULL, don't delete the entry. This happens only
    // for the "dummy" instance that is created to read in a real
    // instance of a global field.
    if (fieldData == null)
        return;

    String key = getName().getString();
    nameDict.remove(key);

    Destroyable.delete( value );
    
    super.destructor();
}

//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      This is a virtual function used by the database so notification and
//      field to field connections work.  The fieldData for globalFields
//      contains only the value field; the other are hidden from the
//      database and are used only when reading or writing.
//

public SoFieldData getFieldData()
//
//////////////////////////////////////////////////////////////////////////////
{
    return fieldData;
}

	   
	   	
	// Returns the type identifier for a specific instance.
	public SoType getTypeId() {
		return classTypeId;
	}

	 //////////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //      Return the field this is a container for:
	   //
	   // Use: internal
	   
	   public SoField 
	   getMyField()
	   //
	   //////////////////////////////////////////////////////////////////////////////
	   {
	       return value;
	   }
	   
	
	/**
	 * Find a global field with the given name. 
	 * This returns NULL if there is none. 
	 * 
	 * @param name
	 */
	public static SoGlobalField find(final SbName name) {
	     Object key = (Object)(name.getString());
	          final Object[] result = new Object[1];
	          if (nameDict.find(key, result) == false) return null;
	          return (SoGlobalField )result[0];	     	
	}
	
//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      Change the name of a global field.
//
// Use: internal

public void
changeName( SbName newName)
//
//////////////////////////////////////////////////////////////////////////////

{
    // Remove old entry from dictionary
    String  key = getName().getString();
    nameDict.remove(key);

    // Create a new fieldData with the correct name for the field:
    SoField field = value;
    fieldData.destructor();
    fieldData = new SoFieldData();
    fieldData.addField(this.getClass(), this, newName.getString(), field);
    key = getName().getString();

    // If there is already an entry with the new name...
    final Object[] oldGlobalField = new Object[1];
    if (nameDict.find(key, oldGlobalField) == true) {
        // Delete the old one
        ((SoGlobalField )oldGlobalField[0]).unref();
        nameDict.remove(key);
    }

    // Enter this node under the new name
    nameDict.enter(key, this);
}

	

//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      Return the name of this global field.
//
// Use: internal

public SbName
getName() 
//
//////////////////////////////////////////////////////////////////////////////
{
    return fieldData.getFieldName(0);
}

//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      A handy utility routine that saved some typing
//
// Use: private

private SoType
getType() 
//
//////////////////////////////////////////////////////////////////////////////
{
    return SoType.fromName(typeField.getValue());
}


//////////////////////////////////////////////////////////////////////////////
//
// Description:
//      Special method used by SoBase::read to read in a global field.
//      The regular reading method can't be used because there can be
//      only one global field with a given name, so SoBase can't just
//      immediately create a new instance when it reads the
//      "GlobalField {" string from the file.  So, it calls this
//      routine instead.  This returns NULL if there was a read error.
//
// Use: internal, static

public static SoGlobalField read(SoInput in)
//
//////////////////////////////////////////////////////////////////////////////
{
    // Read in "type SFFloat":
    final SbName typeName = new SbName();
    if (!in.read(typeName, true) || typeName.operator_not_equal(new SbName("type")))
        return null;

    // Read the value of this private "type" field, which is the name
    // of this globalField. It has to be read as a field, since that's
    // how it's written. (The binary version depends on this!)
    final SoSFName typeField = new SoSFName();
    if (! typeField.read(in, new SbName("type")))
        return null;
    typeName.copyFrom(typeField.getValue());

    SoType type = SoType.fromName(typeName);
    if (! type.isDerivedFrom(SoField.getClassTypeId(SoField.class))) {
        SoReadError.post(in, "\""+typeName.getString()+"\" is not a type of field");
        return null;
    }

    // Now, get the name of the next field, which is the name of this
    // globalField:
    final SbName myName = new SbName();
    if (! in.read(myName))
        return null;
    
    // Try to create/get an appropriate global field:
    final boolean[]        alreadyExists = new boolean[1];
    SoGlobalField result = create(myName, type, alreadyExists);

    // If there already was one with the same name and a different type
    if (result == null)
        return null;

    if (alreadyExists[0]) {
        // We need to read and throw away the value...
        SoField t = (SoField )type.createInstance();
        // If the field has connections, it needs to have a non-NULL
        // container...
        t.setContainer(result);
        t.enableNotify(false);
        if (!t.read(in, myName)) {
            result.destructor();
            t.destructor();
            return null;
        }
        // Get rid of any connections that may have been established--
        // this gets rid of any extraneous auditors.
        t.disconnect();
        t.destructor();
    }
    else {
        // Read into newly-created global field.
        if (!result.value.read(in, myName)) {
            // Problem reading value field
            result.destructor();
            return null;
        }
    }
    return result;
}



}
