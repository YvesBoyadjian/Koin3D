/**
 * 
 */
package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;


/**
 * @author Yves Boyadjian
 *
 */
public class SoNodekitCatalogEntry {

    private           final SbName     name = new SbName();                
    private           final SoType     type = new SoType();                
    private           final SoType     defaultType = new SoType();         
    private           boolean     nullByDefault;       
    private           boolean     leafPart;            
    private           final SbName     parentName = new SbName();          
    private           final SbName     rightSiblingName = new SbName();    
    private           boolean     listPart;            
    private           final SoType     listContainerType = new SoType();   
    private           final SoTypeList listItemTypes = new SoTypeList();       
    private           boolean     publicPart;          
   	
    public SbName  getName() { return name; };
    public SoType  getType() { return type; };
    public SoType  getDefaultType() { return defaultType; };
    
	public boolean isLeaf() {
		 return leafPart; 
	}
	
	public SbName  getParentName() { return parentName; };
	public  SbName getRightSiblingName() { return rightSiblingName; };
	
	public boolean isList() {
		 return listPart; 
	}
	
	public SoType getListContainerType() {
		return listContainerType;
	}
	
	public SoTypeList getListItemTypes() {
		 return listItemTypes; 
	}
	
	public boolean isPublic() {
		 return publicPart; 
	}
	
	public boolean    isNullByDefault() { return nullByDefault; };
	
	
	 //
	   // Description:
	   //    Looks for the given part.
	   //    Checks this part, then recursively checks all entries in the 
	   //    catalog of this part (it gets its own catalog by looking
	   //    at the catalog for the 'dummy' for this type, which is accessed
	   //    through: type.getInstance().getNodekitCatalog()
	   //    or, if type is an abstract type, 
	   //    uses defaultType.getInstance().getNodekitCatalog()
	   //
	   // Use: public
	   
	   public boolean
	   recursiveSearch( SbName    nameToFind, 
	                                             SoTypeList   typesChecked )
	   //
	   {
	       SoNodekitCatalog subCat;
	   
	       // is this the part of my dreams?
	       if ( name == nameToFind )
	           return true;
	   
	       // make sure the part isn't a list
	       if ( listPart == true )
	           return false;
	       // make sure the part is subclassed off of SoBaseKit
	       if ( !type.isDerivedFrom( SoBaseKit.getClassTypeId() ))
	           return false;
	   
	       // avoid an infinite search loop by seeing if this type has already been
	       // checked...
	       if ( typesChecked.find( type ) != -1 )
	           return false;
	   
	       // if it's still ok, then search within the catalog of this part
	       // first check each name:
	       SoBaseKit inst = (SoBaseKit ) type.createInstance();
	       if ( inst == null )
	           inst = ( SoBaseKit ) defaultType.createInstance();
	       if ( inst == null ) {
//	   #ifdef DEBUG
//	           SoDebugError::post("SoNodekitCatalogEntry::recursiveSearch",
//	           "part type and defaultType are both abstract classes");
//	           abort();
//	   #endif
	       }
	   
	       subCat = inst.getNodekitCatalog();
	       inst.ref();
	       inst.unref();
	   
	       int i;
	       for( i = 0; i < subCat.getNumEntries(); i++ ) {
	           if ( subCat.getName( i ).operator_equal_equal(nameToFind) ) 
	                   return true;
	       }
	       // at these point, we've checked all the names in this class, so 
	       // we can add it to typesChecked
	       typesChecked.append( type );
	   
	       // then, recursively check each part
	       for( i = 0; i < subCat.getNumEntries(); i++ ) {
	           if ( subCat.recursiveSearch( i, nameToFind, typesChecked ) )
	                   return true;
	       }
	   
	       return false;  // couldn't find it ANYwhere!
	   }	   	  	


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: internal

SoNodekitCatalogEntry( final SbName theName, 
                              SoType  theType , SoType theDefaultType,
                              boolean     theNullByDefault,
                        final SbName  theParentName, 
                        final SbName  theRightSiblingName, boolean theListPart, 
                              SoType  theListContainerType,
                        final SoTypeList theListItemTypes,
                              boolean thePublicPart )
//
////////////////////////////////////////////////////////////////////////
{
    name                   .copyFrom( theName);
    type                   .copyFrom( theType);
    defaultType            .copyFrom( theDefaultType);
    nullByDefault          = theNullByDefault;
    leafPart               = true;    // everything is a leaf 'til given a child
    parentName             .copyFrom( theParentName);
    rightSiblingName       .copyFrom( theRightSiblingName);
    listPart               = theListPart;
    listContainerType      .copyFrom( theListContainerType);
    listItemTypes.copy( theListItemTypes );
    publicPart             = thePublicPart;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates a new copy of this catalog
//
// Use: private

public SoNodekitCatalogEntry 
clone() 
//
////////////////////////////////////////////////////////////////////////
{
    // make a clone with the current type and defaultType...
    return clone( type, defaultType );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates a new copy of this catalog, but sets the type to newType
//
// Use: private

public SoNodekitCatalogEntry 
clone( SoType newType, 
                              SoType newDefaultType )
//
////////////////////////////////////////////////////////////////////////
{
    SoNodekitCatalogEntry theClone;

    theClone = new SoNodekitCatalogEntry( name, newType, newDefaultType,
         nullByDefault, parentName, rightSiblingName, listPart, 
         listContainerType, listItemTypes, publicPart );
    theClone.leafPart = leafPart;

    return theClone;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates a new copy of this catalog
//
// Use: public

public void
addListItemType( SoType typeToAdd )
//
////////////////////////////////////////////////////////////////////////
{
    listItemTypes.append( typeToAdd );
}



        public void setNullByDefault( boolean newNullByDefault ) 
                { nullByDefault = newNullByDefault; }
        //! these should only by used by catalogs when an entry
        //! is given a new child or left sibling...
        public void setPublic( boolean newPublic ) { publicPart = newPublic; }
        public void setLeaf( boolean newLeafPart ) { leafPart = newLeafPart; }
        public void setRightSiblingName( final SbName newN ) { rightSiblingName.copyFrom(newN); }
        //! This should only by used by catalogs when an entry is changing
        //! type and/or defaultType
        public void setTypes( SoType newType, SoType newDefaultType )
                            { type.copyFrom(newType); defaultType.copyFrom(newDefaultType); }

}
