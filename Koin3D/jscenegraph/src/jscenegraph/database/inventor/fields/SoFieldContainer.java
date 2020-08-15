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
 |      Defines the SoFieldContainer class, which is the base class of
 |      all classes that contain fields (engines and nodes)
 |
 |   Classes:
 |      SoFieldContainer
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import java.lang.reflect.Field;

import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.mevis.inventor.SoProfiling;
import jscenegraph.port.Offset;

////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for objects that contain fields.
/*!
\class SoFieldContainer
\ingroup Fields
SoFieldContainer is the abstract base class for engines and nodes.
It contains methods for finding out what fields an object has,
controlling notification, and for dealing with all of the fields of an
object at once.


The fields of an engine are its inputs.  Note that even though an
engine's output corresponds to a specific type of field, an engine
output is not a field.

\par See Also
\par
SoSField, SoMField, SoNode, SoDB
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoFieldContainer extends SoBase {
	
	private static final SoType classTypeId = new SoType();
	private boolean notifyEnabled;
	
	 // This is a list of SbDict instances used duting copy operations to
	    // keep track of instances. It is a list to allow recursive copying.
	private static SbPList copyDictList = null;
	    	
	// Returns the type of this class.
	public static SoType getClassTypeId() { return new SoType(classTypeId); }
	
	   public
	    
       //! Is the subclass a built-in Inventor subclass or an extender subclass?
       //! This is used to determine whether to read/write field type information.
       boolean              isBuiltIn;
	   
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Constructor
	    //
	    // Use: protected
	    
	   public SoFieldContainer()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        notifyEnabled = true;
	    }
	    
	   //
	 // Description:
	 // Set up type info.
	 //
	 // Use: internal
	 
	 public static void initClass()
	 //
	 {
	  classTypeId.copyFrom(SoType.createType(SoBase.getClassTypeId(),new SbName("FieldContainer")));
	 }

	// Java Port
		/* (non-Javadoc)
		 * @see jscenegraph.database.inventor.fields.SoFieldContainer#plus(jscenegraph.port.Offset)
		 */
		public final Object plus(Offset offset) {
			return offset.plus(this);
		}
	
	/**
	 * Notification at this Field Container is enabled (if flag == TRUE) 
	 * or disabled (if flag == FALSE). 
	 * The returned boolean value indicates whether notification 
	 * was enabled immediately prior to applying this method. 
	 * 
	 * @param flag
	 * @return
	 */
	// returns old value
	public boolean enableNotify(boolean flag) {
		 boolean e = notifyEnabled; notifyEnabled = flag; return e; 
	}
	
	/**
	 * Notification is the process of telling interested objects that this
	 * object has changed. Notification is needed to make engines and sensors
	 * function, is used to keep SoPaths up to date when the scene graph's
	 * topology changes, and is also used to invalidate rendering or bounding
	 * box caches. Notification is normally enabled, but can be disabled on a
	 * node by node (or engine by engine) basis. If you are making extensive
	 * changes to a large part of the scene graph then disabling notification
	 * can increase performance, at the expense of increased responsibility for
	 * making sure that any interested engines, sensors or paths are kept up to
	 * date.
	 * 
	 * For example, if you will be making a lot of changes to a small part of
	 * your scene graph and you know that there are no engines or sensors
	 * attached to nodes in that part of the scene graph, you might disable
	 * notification on the nodes you are changing, modify them, re-enable
	 * notification, and then touch() one of the nodes to cause a redraw.
	 * 
	 * However, you should profile your application and make sure that
	 * notification is taking a significant amount of time before going to the
	 * trouble of manually controlling notification.
	 * 
	 * @return
	 */
	public boolean isNotifyEnabled() {
		return notifyEnabled;
	}

	// Propagates modification notification through an instance.
	public void notify(final SoNotList list) {
		
//	#ifdef DEBUG
	    if (list == null) {
	        SoDebugError.post("SoFieldContainer::notify",
	                           "Notification list pointer is NULL");
	        return;
	    }
//	#endif /* DEBUG */

	    // Don't notify if application has disabled notification for some reason
	    if (! isNotifyEnabled())
	        return;

	    boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeFieldContainerNotifyCB()!= null)
	      && (getTypeId() != SoGlobalField.getClassTypeId());
	    if (profilingEntered) {
	      SoProfiling.getEnterScopeFieldContainerNotifyCB().run(this);
	    }

	    // There are only 2 types of notification possible here: CONTAINER
	    // (a field notifying its container), or PARENT (a node notifying
	    // its parent). The other types are handled by the other classes.

	    switch (list.getLastRec().getType()) {
	      case CONTAINER:
	        // We don't need to create a new record, since the last
	        // record's base is already set to this. Just notify all
	        // auditors.
	        super.notify(list);
	        break;

	      case PARENT:
	        // If we're notifying from a child node to a parent node,
	        // create a new record that contains the parent node as a
	        // base, then notify.
	        {
	            final SoNotRec rec = new SoNotRec(this);
	            list.append(rec);
	            super.notify(list);
	        }
	        break;

	      default:
//	#ifdef DEBUG
//	        SoDebugError::post("SoFieldContainer::notify (internal)",
//	                           "Got notification of type %d",
//	                           list->getLastRec()->getType());
//	#endif /* DEBUG */
	        break;
	    }

	    if (profilingEntered && SoProfiling.getLeaveScopeCB() != null) {
	      SoProfiling.getLeaveScopeCB().run();
	    }
	}

	    //! Returns whether or not instance is considered 'built-in' to the
    //! library.  Used during writing.
    public boolean                getIsBuiltIn() { return isBuiltIn; }

	
	// Returns an SoFieldData structure for the node.
	// Objects with no fields should return NULL, which is what the
	// default method does.
	public SoFieldData getFieldData() {
		return null;
	}
	
	/**
	 * Copies the contents of the given node into this instance. 
	 * The default implementation copies just field values and the name. 
	 * 
	 * @param fromFC
	 * @param copyConnections
	 */
	public void copyContents(SoFieldContainer fromFC, boolean copyConnections) {
		
	     // Access the field data and overlay it
		       final SoFieldData fieldData = getFieldData();
		       if (fieldData != null)
		           fieldData.overlay(this, fromFC, copyConnections);
		   
		       // Copy the name, if it has one
		       final SbName name = fromFC.getName();
		       if (!name.operator_not())
		           setName(name);
		  	}
	
	/**
	 * During a copy operation, this copies an instance that is encountered 
	 * through a field connection. 
	 * The default implementation just returns the original pointer 
	 * - no copy is done. 
	 * Subclasses such as nodes and engines handle this differently. 
	 * 
	 * @return
	 */
	public SoFieldContainer copyThroughConnection() {
	     return (SoFieldContainer ) this;
	}
	
	 //
	   // Description:
	   //    Initializes the copy dictionary. Returns FALSE if the dictionary
	   //    already exists (probably indicating a recursive copy is taking
	   //    place).
	   //
	   // Use: internal, static
	   
	public static void initCopyDict() {
		
	     if (copyDictList == null)
	    	           copyDictList = new SbPList();
	    	   
	    	       SbDict copyDict = new SbDict();
	    	   
	    	       // Insert the new dictionary at the beginning. Since most copies
	    	       // are non-recursive, having to make room in the list won't happen
	    	       // too frequently. Accessing the list happens a lot, so using slot
	    	       // 0 will speed that up some.
	    	       copyDictList.insert(copyDict, 0);
	    	   	}
	
	// Adds an instance to the dictionary. 
	public static void addCopy(SoFieldContainer orig, SoFieldContainer copy) {
		
	     SbDict copyDict = (SbDict ) copyDictList.operator_square_bracket(0);
	      
	          // Add a reference when entering an instance into the
	          // dictionary. The references are removed before the dictionary is
	          // deleted.
	          copy.ref();
	      
	          // ??? HACK ALERT: We use the notifyEnabled flag in the copy to
	          // indicate whether its contents have been copied yet. If the flag
	          // is FALSE, the copy has not yet been done. It's pretty safe to do
	          // this because the notifyEnabled flag is not copied between
	          // instances. When copyContents() is called for the first time on
	          // a copied instance, we reset the flag to TRUE.
	          ((SoFieldContainer ) copy).notifyEnabled = false;
	      
	          copyDict.enter( orig, (Object) copy);
	     	}
	
	/**
	 * If a copy of the given instance is in the dictionary, 
	 * this returns it. 
	 * Otherwise, it returns NULL. 
	 * The copy is not changed in any way. 
	 * 
	 * @param orig
	 * @return
	 */
	public static SoFieldContainer checkCopy(SoFieldContainer orig) {
		
	     SbDict copyDict = (SbDict ) copyDictList.operator_square_bracket(0);
	      
	          Object[] copyPtr = new Object[1];
	          if (! copyDict.find(orig, copyPtr))
	              return null;
	      
	          return (SoFieldContainer ) copyPtr[0];
	     	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    If a copy of the given instance is not in the dictionary, this
//    returns NULL. Otherwise, this copies the contents of the
//    original into the copy (if not already done) and returns a
//    pointer to the copy.
//
// Use: internal, static

public static SoFieldContainer 
findCopy( SoFieldContainer orig,
                           boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    if ( copyDictList == null || (copyDictList).operator_square_bracket(0) == null)
        return null;

    SbDict copyDict = (SbDict ) (copyDictList).operator_square_bracket(0);

    final Object[] copyPtr = new Object[1];
    if (! copyDict.find(orig, copyPtr))
        return null;

    SoFieldContainer copyFC = (SoFieldContainer ) copyPtr[0];

    // ??? Copy the contents only if the notifyEnabled flag is FALSE,
    // indicating that the copy has not yet been done. See the HACK
    // ALERT above.
    if (! copyFC.notifyEnabled) {
        copyFC.notifyEnabled = true;
        copyFC.copyContents(orig, copyConnections);
    }

    return copyFC;
}

	
	
	// Cleans up the dictionary when done. 
	public static void copyDone() {
//		 #ifdef DEBUG
		       if (copyDictList.getLength() <= 0) {
		           SoDebugError.post("SoFieldContainer::copyDone",
		                              "No dictionary left to get rid of");
		           return;
		       }
//		   #endif /* DEBUG */
		   
		       SbDict copyDict = (SbDict ) copyDictList.operator_square_bracket(0);
		   
		       // Unref every instance in the copy dictionary
		       copyDict.applyToAll(unrefCopy);
		   
				copyDict.destructor();
		   
		       copyDictList.remove(0);
		  		
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This callback is used to unref() all instances in the
//    copyDict when copyDone() is called.
//
// Use: private, static

private static void
unrefCopy(Object key , Object instPtr)
//
////////////////////////////////////////////////////////////////////////
{
    SoFieldContainer inst = (SoFieldContainer ) instPtr;

    // Set the notifyEnabled bit to TRUE if it wasn't already done
    if (! inst.notifyEnabled)
        inst.notifyEnabled = true;

    inst.unref();
}

	
	 //
	   // Description:
	   //    This callback is used to unref() all instances in the
	   //    copyDict when copyDone() is called.
	   //
	   // Use: private, static
	   
	  	private static SbDict.Method unrefCopy = new SbDict.Method() {

		@Override
		public void apply(Object key, Object instPtr) {
			SoFieldContainer.unrefCopy(key, (SoFieldContainer ) instPtr);
   		}
		
	};
	
	@Override
	public SoType getTypeId() {
		return classTypeId;
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads stuff into instance of subclass of SoFieldContainer.
//    Returns FALSE on error.
//
// Use: protected

public boolean readInstance(SoInput in, 
                               short flags/* flags not used */) {
	return SoFieldContainer_readInstance( in, flags);
}

public boolean SoFieldContainer_readInstance(SoInput in, 
        short flags/* flags not used */)

//
////////////////////////////////////////////////////////////////////////
{
	  final SoFieldData fd = this.getFieldData();
	  if (fd != null) {
		  final boolean[] notBuiltIn = new boolean[1]; // Not used
		  return fd.read(in, this,
                  // The "error on unknown field" is FALSE when
                  // we're a group node, since fields can be
                  // followed by children node specifications.
                  (flags & SoBase.BaseFlags.IS_GROUP.getValue()) != 0 ? false : true,
				  notBuiltIn);
	  }
	  return true;
}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Return a list of my fields.
//
// Use: public

public int getFields(SoFieldList list)
//
////////////////////////////////////////////////////////////////////////
{
    int                 i;
    final SoFieldData   fd = getFieldData();

    if (fd == null)
        return 0;

    for (i = 0; i < fd.getNumFields(); i++) {
        list.append(fd.getField(this, i));
    }

    return fd.getNumFields();
}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns a pointer to the field with the given name. If no such
//field exists, NULL is returned.
//
//Use: public

    //! Returns a pointer to the field with the given name. If no such
    //! field exists, NULL is returned.
	public SoField getField(final String fieldName) { // java port
		return getField(new SbName(fieldName));
	}
    public SoField getField(final SbName fieldName) {
    int                 i;
    final SoFieldData   fd = getFieldData();

    if (fd == null)
        return null;

    // Search fields for one with given name
    for (i = 0; i < fd.getNumFields(); i++)
        if (fd.getFieldName(i).operator_equal_equal(fieldName))
            return fd.getField(this, i);

    // Not found...
    return null;    	
    }

	
    //! Sets one or more fields in this object to the values specified in the
    //! given string, which should be a string in the Inventor file format.
    //! TRUE is returned if the string was valid Inventor file format.  For
    //! example, you could set the fields of an SoCube by doing:
    //! \code
    //! SoCube *cube = ....
    //! cube->set("width 1.0 height 2.0 depth 3.2");
    //! \endcode
    public boolean                set(String fieldDataString)
        { return set(fieldDataString, null); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets fields in object from info in passed string. The format of
//    the string is the same as the ASCII file format, i.e., the stuff
//    that could appear between the '{' and '}' for the object in a file.
//
//    The SoInput parameter is NULL in the public method and may be
//    non-NULL in the internal one. If it is non-NULL, it specifies
//    the SoInput from which to get the reference dictionary to use
//    when reading from the string.
//
// Use: public/internal

public boolean
set(String fieldDataString, SoInput dictIn)
//
////////////////////////////////////////////////////////////////////////
{
    final SoFieldData   fieldData = getFieldData();

    if (fieldData != null) {
        final SoInput in = new SoInput(dictIn);
    	try {
        in.setBuffer( fieldDataString, fieldDataString.length());
        final boolean[] isBuiltIn = new boolean[1]; // Not used
        return fieldData.read(in, this, false, isBuiltIn);
    	}
    	finally {
    		in.destructor();
    	}
    }
    else
        return false;
}

/**
 * Java port : Gets offset of this field in engine
 * @param output
 * @return -1 if invalid offset
 */
public String fieldName(SoEngineOutput output) {
	SoFieldContainer container  = output.getContainer();
	if(this != container) {
		return ""; // wrong container
	}
	Field[] publicFields = container.getClass().getFields();
	for(Field publicField : publicFields) {			
		try {
			Object field = publicField.get(container);
			if(field == output) {
				String fieldName = publicField.getName();
				return fieldName;
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return "";
}

	
}
