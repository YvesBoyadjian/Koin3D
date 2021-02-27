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
 |      Defines the SoType class.  The SoType class is used for
 |       run-time typing.  It is designed to be very small (it
 |       contains only two shorts) and to support comparison of types,
 |       single inheritence type hierarchies, and creation of a type
 |       from a name and create of a C++ instance from an SoType.
 |       No two types can share the same name, even if they are
 |       derived from different parents.
 |       SoTypes may also store an extra short with the type;
 |       this is used by nodes and actions to keep track of their
 |       place in the dispatch table.
 |
 |   Author(s)          : Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.port.Mutable;


//!
//! SoType has no virtual functions to keep it small...
//!
////////////////////////////////////////////////////////////////////////////////
//! Stores runtime type information.
/*!
\class SoType
\ingroup General
The SoType class keeps track of runtime type information in
Inventor. Each type is associated with a given name, so lookup is
possible in either direction.


Many Inventor classes request a unique SoType when they
are initialized.  This type can then be used to find out the actual
class of an instance when only its base class is known, or to obtain
an instance of a particular class given its type or name.


Note that the names associated with types of Inventor classes do not
contain the "So" prefix.

\par See Also
\par
SoAction, SoBase, SoDetail, SoError, SoEvent, SoField
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoType implements Mutable {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + storage_index;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SoType)) {
			return false;
		}
		SoType other = (SoType) obj;
		if (storage_index != other.storage_index) {
			return false;
		}
		return true;
	}
	//private class Storage {
		private int storage_data;
		private int storage_index;
		private boolean storage_isPublic;
	//}
	
	//private final Storage storage = new Storage();
	
	private boolean immutable;
	
	// Dictionary mapping SbNames to pointers into indices in the typeData
	// array (pointers into the array would be bad, since the array can
	// move around as it gets expanded, but indices are OK):
	private static SbDict nameDict;
	   
	private static int nextIndex;
	private static int arraySize;
	private static SoTypeData[] typeData;
	
	public interface CreateMethod {
		public Object run();
	};
	  
	private static class SoTypeData {		
		public final SoType type = new SoType();
		public final SoType parent = new SoType();
		public final SbName name = new SbName();
		public CreateMethod createMethod;
	}
	
	
	private static final int INITIAL_ARRAY_SIZE  = 64;

	// java port 
	private static final Map<SoType, Object> toVoidPtr = new HashMap<SoType, Object>(); 

	// java port 
	private static final Map<Object, SoType> fromVoidPtr = new HashMap<Object, SoType>(); 
	
	// java port
	public SoType() {
		
	}
	
	// java port
	public SoType(final SoType other) {
		if(other != null) {
		storage_data = other.storage_data;
		storage_index = other.storage_index;
		storage_isPublic = other.storage_isPublic;
		}
	}
	
	// Returns the name associated with a type. 
	public SbName getName() {
		return new SbName(typeData[storage_index].name);
	}
	
	// Returns the type of the parent class. 
	public SoType getParent() {
		return new SoType(typeData[storage_index].parent);
	}

	// Returns the type of the parent class.
	// Don't modify the returned object !
	public SoType getParentFast() {
		return typeData[storage_index].parent;
	}

	// Returns TRUE if the type is a bad type. 
	public boolean isBad() {
		return storage_index == 0;
	}
	
	// Returns TRUE if the type is derived from type t. 
	public boolean isDerivedFrom(SoType t) {
		  /*final */SoType thisType = /*new SoType(*/this/*)*/;
		   
		    while (! thisType.isBad())
		    if (thisType.operator_equal_equal(t))
		    return true;
		    else
		    thisType/*.copyFrom(*/=thisType.getParentFast()/*)*/;
		   
		    return false;
		  
	}
	
	/**
	 * Some types are able to create instances; 
	 * for example, most nodes and engines 
	 * (those which are not abstract classes) can be created this way. 
	 * This method returns TRUE if the type supports such creation. 
	 * 
	 * @return
	 */
	//
	   // Description:
	   //    Test if we know how to create an instance
	   //
	   // Use: public
	  public boolean canCreateInstance() {
		     SoTypeData  data = typeData[storage_index];
		      
		          return (data.createMethod != null);
		      		
	}
	
	/**
	 * Creates and returns a pointer to an instance of the type. 
	 * Returns NULL if an instance could not be created for some reason. 
	 * The pointer is returned as a generic pointer, but can be cast to the 
	 * appropriate type. 
	 * For example:
	 * 
	 *	SoCube *c = (SoCube *) SoCube::getClassTypeId().createInstance();
	 *
	 * is a convoluted way of creating a new instance of an SoCube. 
	 * 
	 * @return
	 */
	public Object createInstance() {
		
	     SoTypeData  data = typeData[storage_index];
	      
	          if (data.createMethod != null)
	         return data.createMethod.run();
	      
	          return null;
	      	}
	
	// Get data. 
	public int getData() {
		 return storage_data;
	}
	
	// Returns the type key as a short. 
	public int getKey() {
		 return storage_index;
	}
	 
	 //
	 // Description:
	 // Make the table of type data bigger.
	 //
	 // Use: private, static
	 
	private static void expandTypeData() {
		
		  if (typeData == null) {
			    arraySize = INITIAL_ARRAY_SIZE;
			    typeData = new SoTypeData[arraySize];
			    for(int i=0;i<arraySize;i++) { // java port
			    	typeData[i] = new SoTypeData();
			    }
	    }
	    else {
			    SoTypeData[] newTypeData = Arrays.copyOf(typeData, 2 * arraySize);
			    typeData = newTypeData;
			    arraySize *= 2;
			    for(int i=arraySize/2;i<arraySize;i++) { // java port
			    	typeData[i] = new SoTypeData();
			    }
		    }			  
	}
	
	/**
	 * Returns an always-illegal type. 
	 * Useful for returning errors. 
	 * 
	 * @return
	 */
	public static SoType badType() {
	     SoType t = new SoType();
	      
	          t.storage_index = 0;
	          t.storage_isPublic = true;
	          t.storage_data = 0;
	      
	          return t;
	     
	}
	
	// Adds all types derived from the given type to the given type list. 
	// Returns the number of types added. 
	public static int getAllDerivedFrom(SoType type, SoTypeList typeList) {
		
	     int   numAdded, i;
	          final SoType  curType = new SoType();
	      
	          // Gather all valid types into array (skip index 0 - badType)
	          numAdded = 0;
	          for (i = 1; i < nextIndex; i++) {
	        curType.copyFrom(typeData[i].type);
	      
	        // See if the type corresponds to a non-abstract node class 
	        if (! curType.isBad() && curType.isDerivedFrom(type) &&
	            curType.storage_isPublic) {
	            typeList.append(curType);
	            ++numAdded;
	        }
	          }
	      
	          return numAdded;
	     	}
	  
	// Create a new type.
	// java port
	public static SoType createType(SoType parent, SbName name) {
		return createType(parent,name,(CreateMethod)null,(short)0);
	}
	
	// java port
	public static SoType createType(SoType parent, SbName name, CreateMethod createMethod) {
		return createType(parent,name,createMethod,(short)0);		
	}
	
	public static SoType createType(SoType parent, SbName name, CreateMethod createMethod, short data) {
		
		  final SoType t = new SoType();
		  SoTypeData td;
		   
		    if (nextIndex >= arraySize)
		    expandTypeData();
		   
		    t.storage_index = nextIndex++;
		    t.storage_isPublic = true;
		    t.storage_data = data;
		   
		    td = typeData[t.storage_index];
		   
		    td.type.copyFrom(t);
		    td.parent.copyFrom(parent);
		    td.name.copyFrom(name);
		    td.createMethod = createMethod;
		   
		    nameDict.enter( name.getString(), t.storage_index);
		   
		    return t;
		  
		  }
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Make a new type act like an existing type.  "new" in this case
//    means a different subclass of an existing type-- for examply, a
//    user can create an MyCube subclass derived from SoCube.  By
//    registering it with the type system using this method, whenever
//    the database uses SoType::fromName and then
//    SoType::createInstance, an instance of MyCube will be created
//    instead of an SoCube.  Since the pointer returned from
//    createInstance may be cast to an SoCube, the instance created
//    must be a subclass of SoCube.
//
// Use: extender, static

public static SoType
overrideType(SoType oldType, CreateMethod createMethod)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (oldType.isBad() || ! oldType.canCreateInstance()) {
    	String str = oldType.getName().getString(); 
  SoDebugError.post("SoType::overrideType",
         "Type to override ("+str+") is bad: "+str);
    }
//#endif

    SoTypeData td = typeData[oldType.storage_index];

    td.createMethod  = createMethod;

    return oldType;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Mark this type as internal.  This MUST be done just after the
//    type is first created.
//
// Use: private, static

public void
makeInternal()
//
////////////////////////////////////////////////////////////////////////
{
	if( immutable ) {
		throw new IllegalStateException("SoType is immutable");
	}
    // This is gross, but necessary.  After creation, copies of the
    // type exist in two places:  the classes' classTypeId member, and
    // in the typeData array.  So, we need to change them both:
    storage_isPublic = false;
    typeData[storage_index].type.storage_isPublic = false;
}

	
	
	/**
	 * Get the number of types currently registed in the types dictionary. 
	 * This is used by SoAction when setting up the action method list. 
	 * 
	 * @return
	 */
	public static int getNumTypes() {
		 return nextIndex; 
	}
	
	// Returns the type associated with the given name. 
	public static SoType fromName(SbName name) {
		
		final Object[] b = new Object[1];
		
	     String nameChars = name.getString();
	          String nameString = nameChars;  // For easier manipulation...
	      
	          // Look for an existing type; if the type begins with "So", then
	          // look at a type matching the stuff after the "So", also.  If not
	          // found, we'll try the DSO thing:
	          boolean notFound = !nameDict.find(nameChars, b);
	          
	          if(notFound && (name.getLength() > 2)  &&
	        	        (nameString.substring(0,2).equals("So"))) {
	        	  notFound = !nameDict.find(new SbName(nameChars.substring(2)).getString(), b);
	          }
	          if(b[0] == null) {
	        	  return SoType.badType();
	          }
	          SoType result = typeData[(Integer)b[0]].type;
	          
	          if (result.storage_isPublic == false) {
	        	  //#ifdef DEBUG
	        	        SoDebugError.post("SoType::fromName", nameChars+" is internal");
	        	  //#endif
	        	        return SoType.badType();
	        	      }

	          return result;
	     	}
	
	// java port
	public void copyFrom(Object other) {
		if( immutable ) {
			throw new IllegalStateException("SoType is immutable");
		}
		SoType otherType = (SoType)other;
		storage_data = otherType.storage_data;
		storage_index = otherType.storage_index;
		storage_isPublic = otherType.storage_isPublic;
	}
	
	// Initialize the type system. 
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Initialize the type system.  Called by SoDB::init.
	    //
	    // Use: public, static
	    
	   	public static void init() {
	   		
	   		// java port
	   		if(nameDict != null) { 
	   			throw new IllegalStateException("SoType already initialized");
	   		}
	   		
	        nameDict = new SbDict();
	         
	            // This will change when expandTypeData() is called below
	            arraySize = 0;
	            typeData = null;
	        
	            // Initialize bad type at index 0. Make room first.
	            expandTypeData();
	            typeData[0].type.storage_index = 0;
	            typeData[0].type.storage_isPublic = true;
	            typeData[0].type.storage_data  = 0;
	        
	            // The first real type will have index 1
	            nextIndex = 1;
	       		
	}
	   	
    //! Returns TRUE if this type is the same as or not the same as the given type.
	   	public boolean                 operator_equal_equal(final SoType t)
        { return (storage_index == t.storage_index);}
    //! Returns TRUE if this type is the same as or not the same as the given type.
    public boolean                 operator_not_equal(final SoType t)
        { return (storage_index != t.storage_index);}

    // java port
	   	public Object toVoidPtr() {
	   		Object voidObject = toVoidPtr.get(this);
	   		if( voidObject == null) {
	   			voidObject = new Object();
	   			toVoidPtr.put(this, voidObject);
	   			fromVoidPtr.put(voidObject, this);
	   		}
	   		return voidObject;
	   	}
	   	
	   	// java port
	   	public static SoType fromVoidPtr(Object voidObject) {
	   		SoType type = fromVoidPtr.get(voidObject);
	   		if(type == null) {
	   			type = SoType.badType();
	   		}
	   		return type;
	   	}
	   	
	   	/**
	   	 * Java port
	   	 */
	   	public String toString() {
	   		return typeData[storage_index].name.getString();
	   	}

	   	/**
	   	 * Java port
	   	 * @param klass
	   	 * @return
	   	 */
		public static SoType getClassTypeId(Class<?> klass) {
			
			try {
				try {
					Method method = klass.getMethod("getClassTypeId");
					Object object = method.invoke(klass);
					return (SoType)object;
				} catch (NoSuchMethodException e) {
					Method method = klass.getMethod("getClassTypeId",Class.class);
					Object object = method.invoke(klass,klass);
					return (SoType)object;
				}
			} catch (NoSuchMethodException | SecurityException|IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException();
			}
		}
		
		public void markImmutable() {
			immutable = true;
		}
}
