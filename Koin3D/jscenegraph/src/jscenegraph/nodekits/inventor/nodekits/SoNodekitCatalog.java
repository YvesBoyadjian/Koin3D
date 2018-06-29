/**
 * 
 */
package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoGroup;


/**
 * @author Yves Boyadjian
 *
 */
public class SoNodekitCatalog {
	
	public static final Integer SO_CATALOG_NAME_NOT_FOUND = -1;
	public static final Integer SO_CATALOG_THIS_PART_NUM = 0;

	private static SbName emptyName;
	private static SoTypeList emptyList;
	private static SoType badType;
	
	private int numEntries;
	private SoNodekitCatalogEntry[] entries; 
    private final SbDict                partNameDict = new SbDict(); 
  	
    // Returns number of entries in the catalog. 
    public int getNumEntries() {
    	 return numEntries; 
    }
    
	//
	   // Description:
	   //    For finding the partNumber of an entry given its reference name.
	   //
	   // Use: internal
	   
	// Given the name of a part, returns its part number in the catalog.
	public int getPartNumber(String theName) {
		return getPartNumber(new SbName(theName));
	}
	public int getPartNumber(SbName theName) {
	    Object[] castPNum = new Object[1];
	      
	          if ( partNameDict.find( theName.getString(), castPNum ) )
//	      #if (_MIPS_SZPTR == 64 || __ia64 || __LP64__)
	              return  (Integer)castPNum[0];  // System long
//	      #else
//	              return ( (int) castPNum );
//	      #endif
	          else 
	              return SO_CATALOG_NAME_NOT_FOUND;
	     		
	}
	
	// Given the part number of a part, returns its name in the catalog. 
	public SbName getName(int thePartNumber) {
	     // return the name of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].getName();
		       else
		           return emptyName;
		  		
	}
	
	public SoType getType(int thePartNumber) {
	     // return the type of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].getType();
		       else
		           return badType;
		  		
	}
	
	public SoType getDefaultType(int thePartNumber) {
		
	     // return the defaultType of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].getDefaultType();
		       else
		           return badType;
		  	}
	
	public boolean isLeaf(int thePartNumber) {
	     // return the type of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].isLeaf();
		       else
		           return true;
		  		
	}
	
	//
	   // Description:
	   //    For finding the name of the parent of an entry.
	   //
	   // Use: internal
	   
	  public SbName 
	   getParentName( int thePartNumber )
	   //
	   {
	       // return the entry, if you can find it.
	       if ( thePartNumber >= 0 && thePartNumber < numEntries )
	           return entries[thePartNumber].getParentName();
	       else
	           return emptyName;
	   }
	   	
	//
	   // Description:
	   //    For finding the name of the parent of an entry.
	   //
	   // Use: internal
	   
	public int getParentPartNumber(int thePartNumber) {
	     SbName pName = getParentName( thePartNumber );
	          return( getPartNumber( pName ) );
	     		
	}
	

	   //
	   // Description:
	   //    For finding the name of the right sibling of an entry.
	   //
	   // Use: internal
	   
	  public SbName 
	   getRightSiblingName( int thePartNumber )
	   //
	   {
	       // return the entry, if you can find it.
	       if ( thePartNumber >= 0 && thePartNumber < numEntries )
	           return entries[thePartNumber].getRightSiblingName();
	       else
	           return emptyName;
	   }
	   	
	//
	   // Description:
	   //    For finding the name of the rightSibling of an entry.
	   //
	   // Use: internal
	  
	public int getRightSiblingPartNumber(int thePartNumber) {
	     SbName sName = getRightSiblingName( thePartNumber );
	          return( getPartNumber( sName ) );
	     		
	}
	
	//
	   // Description:
	   //    For finding 'listPart' of an entry.
	   //
	   // Use: internal
	   	
	public boolean isList(int thePartNumber) {
		
	     // return the type of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].isList();
		       else
		           return false;
		  	}
	
	//
	   // Description:
	   //    For finding the type of the container of a list entry.
	   //
	   // Use: internal
	   	
	public SoType getListContainerType( int thePartNumber) {
	     // return the defaultType of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].getListContainerType();
		       else
		           return badType;
		  		
	}
	
	//
	   // Description:
	   //    For finding the list item type of an entry.
	   //
	   // Use: internal
	   
	public SoTypeList getListItemTypes(int thePartNumber) {
	     // return the type of the entry, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].getListItemTypes();
		       else
		           return ( emptyList );
		  
	}
	
	public boolean isPublic(int thePartNumber) {
	     // return whether the part is public, if you can find it.
		       if ( thePartNumber >= 0 && thePartNumber < numEntries )
		           return entries[thePartNumber].isPublic();
		       else
		           return true;
		  		
	}
	
	/**
	 * used by SoNodekitParts to search through catalogs. 
	 * recursively search a given part for 'name to find' in the templates of 
	 * that entry and its descendants 
	 * 
	 * 
	 * @param partNumber
	 * @param nameTofind
	 * @param typesChecked
	 * @return
	 */
	public boolean recursiveSearch(int partNumber, SbName nameToFind, final SoTypeList typesChecked) {
		
	     // just call the recursive search method on the given entry...
		      return( entries[partNumber].recursiveSearch( nameToFind, typesChecked ));
		 	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    For finding 'nullByDefault' of an entry.
//
// Use: internal

public boolean isNullByDefault( int thePartNumber )
//
////////////////////////////////////////////////////////////////////////
{
    // return the value for this entry, if you can find it.
    if ( thePartNumber >= 0 && thePartNumber < numEntries )
        return entries[thePartNumber].isNullByDefault();
    else
        return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates a new copy of this catalog
//
// Use: public

public SoNodekitCatalog clone( SoType typeOfThis ) 
//
////////////////////////////////////////////////////////////////////////
{
    SoNodekitCatalog      theClone;

    theClone = new SoNodekitCatalog();
    theClone.numEntries = numEntries;
    if (numEntries == 0)
        theClone.entries = null;
    else {
        theClone.entries = new  SoNodekitCatalogEntry [numEntries];
        for (int i = 0; i < numEntries; i++) {
            if ( i == SO_CATALOG_THIS_PART_NUM )
                theClone.entries[i] = entries[i].clone( typeOfThis, 
                                                          typeOfThis );
            else
                theClone.entries[i] = entries[i].clone();
            theClone.partNameDict.enter( entries[i].getName().getString(), 
                                          (Object) (int) i );
        }
    }

    return theClone;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Checks that the name is not null and not empty.
//
// Use: private

private boolean
checkName( final SbName theName )  // proposed name
//
////////////////////////////////////////////////////////////////////////
{
    // CHECK IF IT'S null
    if ( theName.getString() == null ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkName",
                            "given name is null" );
//#endif
        return false;
    }

    // CHECK IF IT'S EMPTY
    if ( theName.operator_equal_equal("") ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkName",
                            "given name is the empty string" );
//#endif
        return false;
    }

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Checks that the name is OK to use for the new entry.
//
// Use: private

private boolean
checkNewName( final SbName theName )  // proposed name
//
////////////////////////////////////////////////////////////////////////
{
    if ( !checkName( theName ) )
        return false;

    // CHECK IF IT'S UNIQUE FOR THIS CATALOG
    if ( getPartNumber( theName ) == SO_CATALOG_NAME_NOT_FOUND ) {
        return true;
    }
    else {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkNewName",
                            "the name "+theName.getString()+" is already in this catalog");
//#endif
        return false;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Checks that the type is not for an abstract node type.
//
// Use: private

private boolean
checkNewTypes( SoType theType,  // proposed type
                                 SoType theDefaultType ) // and default
//
////////////////////////////////////////////////////////////////////////
{
    if ( !theDefaultType.canCreateInstance()) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkNewTypes",
        "the defaultType "+theDefaultType.getName().getString()+" is an abstract class, and cannot be part of a nodekits structure");
//#endif
        return false;
    }

    if ( !theDefaultType.isDerivedFrom( theType ) ) {
//#ifdef DEBUG
        String defName = theDefaultType.getName().getString();
        SoDebugError.post("SoNodekitCatalog.checkNewTypes",
                           "the defaultType "+defName+" is not a subclass of the type "+
                           theType.getName().getString()+". It can not be part of a nodekits structure"
                           );
//#endif
        return false;
    }
    else
        return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Checks that the name is OK to use the parent for a new entry.
//
// Use: private

private boolean
checkAndGetParent( 
        final SbName                theName,          // the child's name
        final SbName                theParentName,    // proposed parent name
        final SoNodekitCatalogEntry[]  parentEntry ) // corresponding entry filled 
                                                // in this method
//
////////////////////////////////////////////////////////////////////////
{
    // CHECK FOR "THIS"
    // The string "this" means that a node is to be the nodekit node itself 
    // In this case, return null as the parentEntry, and a status of true!
    if ( theName.operator_equal_equal("this") ) {
        // 'this' has no parent
        parentEntry[0] = null;
        return true;
    }
    else {
        // only 'this' can have no parent
        if ( !checkName( theParentName ) )
            return false;
    }

    // CHECK THAT THE PARENT IS IN THE CATALOG
    int parentPartNumber = getPartNumber( theParentName );
    if ( parentPartNumber == SO_CATALOG_NAME_NOT_FOUND ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkAndGetParent",
        "requested parent >> "+theParentName.getString()+" << is not in this catalog" );
//#endif
        return false;
    }
    parentEntry[0] = entries[ parentPartNumber ];

    // CHECK THE NODE TYPE OF THE PARENT

    // [1] Unless it's "this", it must be a subclass of SoGroup, 
    //     or you can't add children to it.
    if ( parentPartNumber !=  SO_CATALOG_THIS_PART_NUM &&
         !parentEntry[0].getType().isDerivedFrom( SoGroup.getClassTypeId() )) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkAndGetParent",
        "requested parent >> "+theParentName.getString()+" << is a node that is not subclassed from SoGroup, so it can\'t have children");
//#endif
        return false;
    }
    // [2] If they didn't call 'initClass', then 'theParentName' will == 'this',
    //     but parentEntry.getType() will not yet be something derived from
    //     SoBaseKit. Check for this.
    if ( parentPartNumber ==  SO_CATALOG_THIS_PART_NUM &&
         !parentEntry[0].getType().isDerivedFrom( SoBaseKit.getClassTypeId() )) {
//#ifdef DEBUG
        SoDebugError.post( "SoNodekitCatalog.checkAndGetParent",
         "  It looks like you forgot to call initClass for one of your nodekit classes! Expect a core dump!");
//#endif
    }
    // [3] Unless it is 'this', the parent can NOT be subclass of SoBaseKit.
    //     This is because you can only add child nodes to an nodekit through 
    //     its own class's nodekitCatalog
    if ( parentPartNumber !=  SO_CATALOG_THIS_PART_NUM &&
         parentEntry[0].getType().isDerivedFrom( SoBaseKit.getClassTypeId()) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkAndGetParent",
        " Requested parent >> "+theParentName.getString()+" << is a node that is subclassed from SoBaseKit, so it can\'t children except through its own class\'s nodekitCatalog");
//#endif
        return false;
    }

    // MAKE SURE THAT THE NODE HAS NOT BEEN DESIGNATED AS A LIST
    if ( parentEntry[0].isList() == true ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkAndGetParent",
        " Requested parent >> "+theParentName.getString()+" << is a node that has been designated as a list.  \n It can not have explicit children in the catalog although nodes \n can be added to the list for a given instance by using the special list editting \n methods on the nodekit node.");
//#endif
        return false;
    }

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Finds the left and right siblings for the new entry.
//    Also, checks that the right sibling exists in the catalog (unless
//    the name is "", in which case the new entry is to be the rightmost child
//
// Use: private

private boolean
checkAndGetSiblings( 
        final SbName            theParentName,       // parent name
        final SbName            theRightSiblingName, // proposed sibling name
        final SoNodekitCatalogEntry[]  leftEntry,      // new left (to be filled in)
        final SoNodekitCatalogEntry[]  rightEntry )    // new right (to be filled )
//
////////////////////////////////////////////////////////////////////////
{
    leftEntry[0] = null;
    rightEntry[0] = null;

    for ( int i = 0; i < numEntries; i++ ) {
        if ( entries[i].getParentName().operator_equal_equal( theParentName) ) {
            // is it the left sibling?
            if ( entries[i].getRightSiblingName().operator_equal_equal( theRightSiblingName) )
                 leftEntry[0] = entries[i];
            // is it the right sibling?
            else if ( theRightSiblingName.operator_equal_equal( entries[i].getName()) )
                 rightEntry[0] = entries[i];
        }
    }
    if ( ( rightEntry[0] == null ) && ( theRightSiblingName.operator_not_equal("") ) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkAndGetSiblings",
        "Requested right sibling >> "+theRightSiblingName.getString()+" << can not be found in the nodekitCatalog");
//#endif
        return false;
    }

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used as a check when adding a new entry to a catalog.
//    Called if the new node is to be a list of other nodes.
//    Checks that the given type is OK as a list.
//
// Use: private

private boolean
checkCanTypesBeList(   SoType theType,
                                         SoType theDefaultType,
                                         SoType theListContainerType )
//
////////////////////////////////////////////////////////////////////////
{
    // CHECK IF IT'S A GROUP OR SEPARATOR
    if ( !theType.isDerivedFrom( SoNodeKitListPart.getClassTypeId() ) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkCanTypesBeList",
        "requested node type can not be a list. It is not derived from SoNodeKitListPart");
//#endif
        return false;
    }
    if ( !theDefaultType.isDerivedFrom( theType ) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkCanTypesBeList",
        "requested default type is not derived from requested type.");
//#endif
        return false;
    }
    if ( !theListContainerType.isDerivedFrom( SoGroup.getClassTypeId() ) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.checkCanTypesBeList",
        " requested list container node type can not be used. It is not derived from SoGroup");
//#endif
        return false;
    }
    return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    For adding a new entry into the nodekitCatalog
//
// Use: internal

public boolean addEntry( final SbName theName, 
                                  SoType theType,
                                  SoType theDefaultType,
                                  boolean    theNullByDefault,
                            final SbName theParentName, 
                            final SbName theRightSiblingName, 
                                  boolean theListPart, 
                                  SoType theListContainerType,
                                  SoType theListItemType,
                            boolean thePublicPart )
//
////////////////////////////////////////////////////////////////////////
{
    final SoNodekitCatalogEntry[] parentEntry = new SoNodekitCatalogEntry[1], rightEntry = new SoNodekitCatalogEntry[1], leftEntry = new SoNodekitCatalogEntry[1];
    SoNodekitCatalogEntry newEntry;
    SoNodekitCatalogEntry[] newArray;

    // CHECK IF THE NEW ENTRY IS OK

    if ( !checkNewName( theName ) )
        return false;

    if ( !checkNewTypes( theType, theDefaultType ) ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.addEntry",
                " Error creating catalog entry "+ theName.getString());
//#endif
        return false;
    }

    if ( !checkAndGetParent( theName, theParentName, parentEntry ) )
        return false;

    if ( !checkAndGetSiblings( theParentName, theRightSiblingName, 
                               leftEntry, rightEntry ) )
        return false;

    if ( theListPart && !checkCanTypesBeList( theType, theDefaultType, 
                                              theListContainerType ) )
        return false;

    if ( numEntries == SO_CATALOG_THIS_PART_NUM  && theName.operator_not_equal("this") ) {
//#ifdef DEBUG
        SoDebugError.post("SoNodekitCatalog.addEntry",
                " Entry number "+SO_CATALOG_THIS_PART_NUM+" must be named \"this\" ");
//#endif
        return false;
    }

    // IF ALL TESTS WERE PASSED...

    // expand the list by one slot
    newArray = new  SoNodekitCatalogEntry [numEntries + 1];
    if ( entries != null ) {
        for (int i = 0; i < numEntries; i++ )
            newArray[i] = entries[i];
        //delete [] entries; java port
    }
    entries = newArray;
    numEntries++;       

    // make a list containing only the given list item type.
    final SoTypeList listItemTypeList = new SoTypeList(0);
    listItemTypeList.append( theListItemType );

    // create the new entry
    newEntry = new SoNodekitCatalogEntry( theName, theType, theDefaultType,
            theNullByDefault, theParentName, theRightSiblingName,
            theListPart,theListContainerType, listItemTypeList, thePublicPart);
    // enter the new entry in the array
    entries[numEntries - 1] = newEntry;

    // add the new name to the quick-reference part name dictionary
    partNameDict.enter( theName.getString(), 
                        (Object) (int) (numEntries - 1));

    // parent is no longer a leaf node in the nodekit structure
    if ( parentEntry[0] != null ) {
        parentEntry[0].setLeaf( false );
        parentEntry[0].setPublic( false );
    }

    // stitch up sibling names.
    if ( leftEntry[0] != null )
        leftEntry[0].setRightSiblingName( theName );
    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    For adding an allowable child-type to an entry.
//    This will only make a difference if the entry has listPart set to TRUE
//
// Use: public

public void
addListItemType( int thePartNumber,
                                        SoType typeToAdd )
//
////////////////////////////////////////////////////////////////////////
{
    // add typeToAdd to the entry's listItemTypes, if you can find
    // the entry...
    if ( thePartNumber >= 0 && thePartNumber < numEntries )
        entries[thePartNumber].addListItemType( typeToAdd );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    For adding an allowable child-type to an entry.
//    This will only make a difference if the entry has listPart set to TRUE
//
// Use: public

public void
addListItemType( final SbName theName,
                                        SoType typeToAdd )
//
////////////////////////////////////////////////////////////////////////
{
    addListItemType( getPartNumber( theName ), typeToAdd );
}


	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes static variables.
	   //
	   // Use: public
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       emptyName = new SbName("");
	       emptyList = new SoTypeList();
	       badType   = new SoType();
	       badType.copyFrom(SoType.badType());
	   }
	   	
}
