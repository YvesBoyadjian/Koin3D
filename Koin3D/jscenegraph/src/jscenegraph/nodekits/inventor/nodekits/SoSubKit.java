/**
 * 
 */
package jscenegraph.nodekits.inventor.nodekits;

import java.util.HashMap;
import java.util.Map;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.fields.SoSField;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSubKit extends SoSubNode {

	  static final Map<Class,SoNodekitCatalog[]> nodekitCatalog = new HashMap<Class,SoNodekitCatalog[]>(); /* design of this class */                                   
	  static final Map<Class, SoNodekitCatalog[][]>    parentNodekitCatalogPtr = new HashMap<Class,SoNodekitCatalog[][]>(); /* parent design */
	
	
	SoSubKit(Class<? extends SoNode> class1, SoNode parent) {
		super(class1, parent);
		
	}

	public static SoSubKit SO_KIT_HEADER(Class<? extends SoBaseKit> class1,
			SoBaseKit parent) {
		SoSubKit soSubKit = new SoSubKit(class1,parent);
		  
		  if( !nodekitCatalog.containsKey(class1)) {
			  throw new IllegalStateException("Class "+ class1 + " not initialized");
		  }
		
		return soSubKit;
	}

	public static SoNodekitCatalog[] getClassNodekitCatalogPtr(Class klass)
		            { return nodekitCatalog.get(klass); }

	public void SO_KIT_CONSTRUCTOR(Class<? extends SoBaseKit> class1) {
		SO_NODE_CONSTRUCTOR();
		SO__KIT_INHERIT_CATALOG(class1);
	}
		   	

/////////////////////////////////////////////
///
/// This is an internal macro.
/// It initializes the SoNodekitCatalog structure defined in
/// SO__KIT_CATALOG_HEADER. This macro is automatically called as 
/// part of SO_KIT_CONSTRUCTOR
///
///   This and other macros rely on static member variable "firstInstance"
///   from SoSubNode.h
///

	private void SO__KIT_INHERIT_CATALOG(Class<? extends SoBaseKit> className) {                                  
    /* get a copy of the catalog from the base class */                       
    int _value_false= 0;                                                      
    do {                                                                      
        if (firstInstance())                                                    
            if (parentNodekitCatalogPtr.get(className)[0] == null)  /* only true in SoBaseKit */
                nodekitCatalog.get(className)[0] = new SoNodekitCatalog();                        
            else                                                              
                nodekitCatalog.get(className)[0]                                                
              =(parentNodekitCatalogPtr.get(className)[0][0]).clone(SoType.fromName(new SbName(className.getSimpleName())));
    } while (_value_false != 0);                                                   
    }

	
/////////////////////////////////////////////
///
/// This adds the info for a single new part to the SoNodekitCatalog.
/// The parameters are as follows:
///
///      partName:     the name used to refer to this part in nodekitCatalog
///                    NOTE: do not make an entry for 'this'.
///                          'this' is implicitly the top of the tree when
///                          building the catalog.
///      partClassName: the class of node to which this part belongs.
///
///      nullByDefault: If TRUE, the part is not created during the constructor.
///                     (which is the usual case.) 
///                     If FALSE, then it will be automatically created by the 
///                     constructor during the call to the 
///                     macro SO_KIT_INIT_INSTANCE()
///
///      parentName:   the partName of the parent of this part within
///                    the nodekitCatalog
///                    NOTE: if this node is to be a direct descendant of 
///                          'this', then parentName should be given as "this"
///
///      rightName:    the partName of the right sibling of this part
///                    within the nodekitCatalog.
///                    NOTE: if this part is to be the rightmost child, then
///                    the rightName should be given as "" (the empty string).
///
///      isPublicPart: can a user receive a pointer to this part through
///                    getPart?  If a part is not a leaf, this property is 
///                    irrelevant (non-leaf parts are always private ). But if 
///                    it is a leaf, the user's access can be stopped through 
///                    this field.
///
/// For example,
///
/// SO_KIT_ADD_CATALOG_ENTRY(material,SoMaterial,TRUE,this,, TRUE);
///
///       adds to the catalog a part named 'material.'  This part will be an
///       SoMaterial node that is NOT created by default. It will be a direct 
///       child of the nodekit part 'this.' It will be installed as the 
///       rightmost child below 'this'.
///       Since it is public, a user will be able to receive a pointer to this 
///       part by calling getPart().
///
/// Another example:
///    if we are making a catalog for a class SoBirdKit, and we have already
///    created the class SoWingKit, then the following macros:
/// SO_KIT_ADD_CATALOG_ENTRY(mainSep,SoSeparator,TRUE,this,, FALSE);
/// SO_KIT_ADD_CATALOG_ENTRY(rightW,SoWingKit,TRUE,mainSep,, TRUE);
/// SO_KIT_ADD_CATALOG_ENTRY(leftW,SoWingKit,TRUE,mainSep,rightWing, TRUE);
///    describe a catalog with this structure:
///
///                  this
///                    |
///                 mainSep
///                    |
///              -------------
///              |           |
///          leftW        rightW 

public void SO_KIT_ADD_CATALOG_ENTRY(SoSFNode part, String partName, Class partClassName, boolean nullByDefault,      
                  String parentName, String rightName, boolean isPublicPart )                       {
   SO_KIT_ADD_FIELD(part, partName,(null));                                         
   if (firstInstance() && !nodekitCatalog.get(thisClass)[0].addEntry(new SbName(partName),        
                    SoType.fromName(new SbName(partClassName.getSimpleName())),               
                    SoType.fromName(new SbName(partClassName.getSimpleName())), nullByDefault,
                    new SbName(parentName),                                    
                    new SbName(rightName), false, SoType.badType(),           
                    SoType.badType(), isPublicPart  ))                       
	   ((SoBaseKit)thisParent).catalogError();
}



/////////////////////////////////////////////
///
/// This adds the info for a new part to the SoNodekitCatalog.
/// 'partName' may be of an abstract node type.
///
/// The parameters are as follows:
///
///      partName:      same as SO_KIT_ADD_CATALOG_ENTRY
///      partClassName: same as SO_KIT_ADD_CATALOG_ENTRY, except that
///                     abstract node classes are acceptable.
///
///      defaultPartClassName:   If the nodekit is asked to construct this part,
///                          using getPart, then the 'defaultPartClassName' will
///                          specify what type of node to build.
///                          This may NOT be an abstract class.
///                          This MUST be a subclass of 'partClassName'
///
///      nullByDefault: same as SO_KIT_ADD_CATALOG_ENTRY
///      parentName:    same as SO_KIT_ADD_CATALOG_ENTRY
///      rightName:     same as SO_KIT_ADD_CATALOG_ENTRY
///      isPublicPart:  same as SO_KIT_ADD_CATALOG_ENTRY
///
/// For example,
///
/// SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY(light,SoLight, SoDirectionalLight, 
///                                      TRUE, this,, TRUE);
///
///       makes a part node refered to as "light".
///       When calling setPart, any node that is a subclass of light can be 
///       used (e.g., SoDirectionalLight, SoSpotLight, SoPointLight )
///       However, if the user asks for the node and it has not been created yet,
///          (this happens, for example, in the case where there is currently 
///           no 'light' and the user calls
///           SO_GET_PART( myKit, "light", SoLight ), 
///       then an SoDirectionalLight will be created and returned.
/// 

public void SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY(SoSFNode part, String partName, Class partClassName,            
                  Class defaultPartClassName, boolean nullByDefault, String parentName,            
                  String rightName, boolean isPublicPart ) {                                   
   SO_KIT_ADD_FIELD(part, partName,(null));                                         
   if (firstInstance() && !nodekitCatalog.get(thisClass)[0].addEntry(new SbName(partName),        
                    SoType.fromName(new SbName(partClassName.getSimpleName())),               
                    SoType.fromName(new SbName(defaultPartClassName.getSimpleName())), nullByDefault,
                    new SbName(parentName), new SbName(rightName), false,       
                    SoType.badType(), SoType.badType(), isPublicPart  ))    
	   ((SoBaseKit)thisParent).catalogError();
}


/////////////////////////////////////////////
///
/// This adds the info for a new part to the SoNodekitCatalog.
/// 'partName' refers to a part that is a LIST.
///
/// Any list in a nodekit will automatically be a node of type SoNodeKitListPart.
/// These nodes act like subclasses of group but enforce type checking when 
/// you add children to them. 
///
/// The parameters you specify will determine 
/// [a] what kind of group node holds the children (SoGroup, SoSeparator,
///     SoSwitch, etc).
/// [b] what single class of node will be allowable as children in the list.
///
/// Subsequent calls to SO_KIT_ADD_LIST_ITEM_TYPE allow you to add other classes
/// of nodes that will be allowable children for the list.
///
/// The parameters are as follows:
///
///      partName:     Same as in SO_KIT_ADD_CATALOG_ENTRY
///      listContainerClassName:
///                    What class will be used to hold the children in the list?
///                    NOTE: since this is going to have children, it MUST
///                          be a subclass of SoGroup, such as SoSeparator
///                          or SoSwitch.
///      nullByDefault:same as SO_KIT_ADD_CATALOG_ENTRY
///      parentName:   Same as in SO_KIT_ADD_CATALOG_ENTRY
///      rightName:    Same as in SO_KIT_ADD_CATALOG_ENTRY
///
///      listItemClassName: The name of the class of node that may appear in 
///                         the list. Any node class is legal here.
///      isPublicPart: Same as in SO_KIT_ADD_CATALOG_ENTRY
///
/// For example,
///
/// SO_KIT_ADD_CATALOG_LIST_ENTRY(subCubes,SoSeparator,TRUE,this,,SoCube, TRUE );
///
/// makes a list that may contain only SoCubes. An SoSeparator will be used to
/// contain this list of cubes.

public void  SO_KIT_ADD_CATALOG_LIST_ENTRY(SoSFNode part, String partName, Class listContainerClassName,       
      boolean nullByDefault, String parentName, String rightName, Class listItemClassName, boolean isPublicPart ) {
    SO_KIT_ADD_FIELD(part,partName,(null));                                        
    if (firstInstance() && !nodekitCatalog.get(thisClass)[0].addEntry(new SbName(partName),       
                  SoNodeKitListPart.getClassTypeId(),                        
                  SoNodeKitListPart.getClassTypeId(), nullByDefault,         
                  new SbName(parentName), new SbName(rightName), true,          
                  SoType.fromName(new SbName(listContainerClassName.getSimpleName())),        
                  SoType.fromName(new SbName(listItemClassName.getSimpleName())), isPublicPart)) 
    	((SoBaseKit)thisParent).catalogError();
}


/////////////////////////////////////////////
///
/// Assuming that the part 'partName' was already put in the nodekit catalog,
/// using the macro SO_KIT_ADD_CATALOG_LIST_ENTRY(...)
/// this macro will add 'newListItemClassName' to its listItemTypes.
///
/// This means that nodes of type 'newListItemClassName' will be permitted 
/// in the list, as well as any other parts that are already permitted.
/// 
/// Example:
///    The macro:
///      SO_KIT_ADD_CATALOG_LIST_ENTRY(myList, SoSeparator, TRUE,
///                                         myListParent, , SoCube, TRUE )
///        creates a list called "myList" that accepts cubes.
///        calling:
///           myKit->addChild( "myList", myCube );
///        will work just fine, but:
///           myKit->addChild( "myList", mySphere );
///        will produce an error.
///    If, however, a subsequent call is made to:
///      SO_KIT_ADD_LIST_ITEM_TYPE( myList, SoSphere );
///        then both calls to addChild will be acceptable
///
///  partName:             Name of the part to add the listItemType to
///  newListItemClassName: Name of the class to add to partName's listItemTypes

public void SO_KIT_ADD_LIST_ITEM_TYPE(SoSFNode part, String partName, Class newListItemClassName ) {          
    if (firstInstance()) nodekitCatalog.get(thisClass)[0].addListItemType(new SbName(partName), 
          SoType.fromName(new SbName(newListItemClassName.getSimpleName())) );
}


	
public boolean SO_KIT_IS_FIRST_INSTANCE() {
    return SO_NODE_IS_FIRST_INSTANCE();
}

public <T> void SO_KIT_ADD_FIELD(SoSField<T> field,String fieldName,T defValue) {
	SO_NODE_ADD_FIELD(field, fieldName, defValue);
}

public static void SO__KIT_INIT_CLASS(Class<? extends SoBaseKit> className, String classPrintName) {

	if( ! nodekitCatalog.containsKey(className)) {
		final SoNodekitCatalog[] nodeKitCatalog = new SoNodekitCatalog[1]; 
		final SoNodekitCatalog[][] nodeKitCatalogPtr = new SoNodekitCatalog[1][];
		nodekitCatalog.put(className,nodeKitCatalog);
		parentNodekitCatalogPtr.put(className, nodeKitCatalogPtr);
	}
	  else {
		  throw new IllegalStateException("Class "+ className + " already initialized");
	  }
}

public SoNodekitCatalog getNodekitCatalog() {
	return nodekitCatalog.get(thisClass)[0];
}

public void SO_KIT_DEFINE_ENUM_VALUE(Enum value) {	
	SO_NODE_DEFINE_ENUM_VALUE(value);
}

public void SO_KIT_SET_SF_ENUM_TYPE(SoSFEnum field, String fieldName, String enumType) {
	SO_NODE_SET_SF_ENUM_TYPE(field, fieldName, enumType);
}
}
