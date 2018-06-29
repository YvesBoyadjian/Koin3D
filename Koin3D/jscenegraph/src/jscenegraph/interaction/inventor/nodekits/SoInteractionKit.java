/**
 * 
 */
package jscenegraph.interaction.inventor.nodekits;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.nodekits.inventor.SoNodeKitPath;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;
import jscenegraph.nodekits.inventor.nodekits.SoNodekitCatalog;
import jscenegraph.nodekits.inventor.nodekits.SoSubKit;

/**
 * @author Yves Boyadjian
 *
 */
public class SoInteractionKit extends SoBaseKit {
	
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoInteractionKit.class,this);
		   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoInteractionKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoInteractionKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! Possible values for caching
    public enum CacheEnabled {
        OFF,                    //!< Never build or use a cache
        ON,                     //!< Always try to build a cache
        AUTO;                    //!< Decide based on some heuristic
    	
    	public int getValue() {
    		return ordinal();
    	}
    };


    //! \name Fields
    //@{

    //! Set render caching mode.  Default is <tt>AUTO</tt>.
    public final SoSFEnum renderCaching = new SoSFEnum();     

    //! Set bounding box caching mode.
    //! 	Default is <tt>ON</tt>.  Setting this value to <tt>AUTO</tt> is equivalent
    //! 	to <tt>ON</tt> - automatic culling is not implemented.
    public final SoSFEnum boundingBoxCaching = new SoSFEnum();

    //! Set render culling mode.  Default is <tt>OFF</tt>.
    //! 	Setting this value to <tt>AUTO</tt> is equivalent to <tt>ON</tt> -
    //! 	automatic culling is not implemented.
    public final SoSFEnum renderCulling = new SoSFEnum();     

    //! Set pick caching mode.  Default is <tt>AUTO</tt>.
    public final SoSFEnum pickCulling = new SoSFEnum();       

    protected final SoSFNode topSeparator = new SoSFNode();
    protected final SoSFNode geomSeparator = new SoSFNode();

	
	   private SoPathList    surrogatePartPathList;
	   private SbPList       surrogatePartNameList;
		    	
	   protected SoFieldSensor fieldSensor;
	    protected SoSeparator oldTopSep;
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor

public SoInteractionKit()
//
////////////////////////////////////////////////////////////////////////
{
	kitHeader.SO_KIT_CONSTRUCTOR(SoInteractionKit.class);

    isBuiltIn = true;

    // Add a separator to the catalog.
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(topSeparator,"topSeparator", SoSeparator.class, true, "this","",false);
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(geomSeparator,"geomSeparator",SoSeparator.class, 
                                true, "topSeparator","",false);

    kitHeader.SO_KIT_ADD_FIELD(renderCaching,"renderCaching",            (CacheEnabled.AUTO.ordinal()));
    kitHeader.SO_KIT_ADD_FIELD(boundingBoxCaching,"boundingBoxCaching",       (CacheEnabled.AUTO.ordinal()));
    kitHeader.SO_KIT_ADD_FIELD(renderCulling,"renderCulling",            (CacheEnabled.AUTO.ordinal()));
    kitHeader.SO_KIT_ADD_FIELD(pickCulling,"pickCulling",              (CacheEnabled.AUTO.ordinal()));

    // Set up static info for enum fields
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.OFF);
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.ON);
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.AUTO);

    // Set up info in enumerated type fields
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(renderCaching,"renderCaching",     "CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching","CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(renderCulling,"renderCulling",     "CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(pickCulling,"pickCulling",       "CacheEnabled");

    SO_KIT_INIT_INSTANCE();

    surrogatePartPathList = new SoPathList();
    surrogatePartNameList = new SbPList();

    // This sensor will watch the topSeparator part.  If the part changes to a 
    // new node,  then the fields of the old part will be disconnected and
    // the fields of the new part will be connected.
    // Connections are made from/to the renderCaching, boundingBoxCaching,
    // renderCulling and pickCulling fields. This way, the SoInteractionKit
    // can be treated from the outside just like a regular SoSeparator node.
    // Setting the fields will affect caching and culling, even though the
    // topSeparator takes care of it.
    // oldTopSep keeps track of the part for comparison.
    fieldSensor = new SoFieldSensor( SoInteractionKit::fieldSensorCB, this );
    fieldSensor.setPriority(0);
    oldTopSep = null;
    setUpConnections( true, true );
}


//    detach/attach any sensors, callbacks, and/or field connections.
//    Called by:            start/end of SoBaseKit::readInstance
//    and on new copy by:   start/end of SoBaseKit::copy.
//    Classes that redefine must call setUpConnections(TRUE,TRUE) 
//    at end of constructor.
//    Returns the state of the node when this was called.
public boolean
setUpConnections( boolean onOff, boolean doItAlways )
{
	if(kitHeader == null) { //java port
		return super.setUpConnections(onOff, doItAlways);
	}
	
    if ( !doItAlways && connectionsSetUp == onOff)
        return onOff;

    if ( onOff ) {

        // We connect AFTER base class.
        super.setUpConnections( onOff, false );

        // Hookup the field-to-field connections on topSeparator.
        connectSeparatorFields( oldTopSep, true );

        // Call sensor CBs to make sure oldTopSep is up-to-date
        fieldSensorCB( this, null );

        // Connect the field sensors
        if (fieldSensor.getAttachedField() != topSeparator)
            fieldSensor.attach( topSeparator );
    }
    else {

        // We disconnect BEFORE base class.

        // Disconnect the field sensors.
        if (fieldSensor.getAttachedField()!=null)
            fieldSensor.detach();

        // Undo the field-to-field connections on topSeparator.
        connectSeparatorFields( oldTopSep, false );

        super.setUpConnections( onOff, false );
    }

    return !(connectionsSetUp = onOff);
}


public void
connectSeparatorFields( SoSeparator dest, boolean onOff )
{
    if (dest == null)
        return;
    if (onOff) {
        final SoField[] f = new SoField[1];
        if ( ! dest.renderCaching.getConnectedField(f) ||
               f[0] != renderCaching )
            dest.renderCaching.connectFrom( renderCaching );
        if ( ! dest.boundingBoxCaching.getConnectedField(f) ||
               f[0] != boundingBoxCaching )
            dest.boundingBoxCaching.connectFrom( boundingBoxCaching );
        if ( ! dest.renderCulling.getConnectedField(f) ||
               f[0] != renderCulling )
            dest.renderCulling.connectFrom( renderCulling );
        if ( ! dest.pickCulling.getConnectedField(f) ||
               f[0] != pickCulling )
            dest.pickCulling.connectFrom( pickCulling );
    }
    else {
        dest.renderCaching.disconnect();
        dest.boundingBoxCaching.disconnect();
        dest.renderCulling.disconnect();
        dest.pickCulling.disconnect();
    }
}


    //! This sensor will watch the topSeparator part.  If the part changes to a 
    //! new node,  then the fields of the old part will be disconnected and
    //! the fields of the new part will be connected.
    //! Connections are made from/to the renderCaching, boundingBoxCaching,
    //! renderCulling and pickCulling fields. This way, the SoInteractionKit
    //! can be treated from the outside just like a regular SoSeparator node.
    //! Setting the fields will affect caching and culling, even though the
    //! topSeparator takes care of it.
    //! oldTopSep keeps track of the part for comparison.
    protected static void fieldSensorCB( Object inKit, SoSensor sensor) {
    SoInteractionKit k  = (SoInteractionKit ) inKit;
    if ( k.oldTopSep == k.topSeparator.getValue())
        return;

    k.connectSeparatorFields( k.oldTopSep, false );

    SoNode newTopSep = k.topSeparator.getValue();
    if (newTopSep != null)
        newTopSep.ref();

    if (k.oldTopSep != null)
        k.oldTopSep.unref();

    k.oldTopSep = (SoSeparator ) newTopSep;
    k.connectSeparatorFields( k.oldTopSep, true );    	
    }
	   

////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
	   public SoNode getAnyPart( final String partName, boolean makeIfNeeded) {
		   return getAnyPart(new SbName(partName), makeIfNeeded);
	   }
	   public SoNode getAnyPart( final SbName partName, boolean makeIfNeeded) {
		   return getAnyPart(partName, makeIfNeeded, false, false);
	   }
public SoNode getAnyPart( final SbName partName, boolean makeIfNeeded,
                              boolean leafCheck, boolean publicCheck )
//
////////////////////////////////////////////////////////////////////////
{
    // Try to get the part:
    SoNode n;
    n = super.getAnyPart(partName, makeIfNeeded, leafCheck, publicCheck);

    // You might not be able to, for example:
    //    [a] makeIfNeeded == false
    //    [b] part does not exist
    //    [c] we ask it to leafCheck or publicCheck and it fails.
    if ( n == null )
        return null;

    // If you were successful, then try to set the surrogate path to null
    n.ref();
    if ( !setAnySurrogatePath( partName, null, leafCheck, publicCheck )) {
//#ifdef DEBUG
        SoDebugError.post("SoInteractionKit::getAnyPart",
                "can not set surrogate path to null for part "+ partName.getString() );
//#endif
    }
    n.unref();

    return n;
}


////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
public SoNodeKitPath 
createPathToAnyPart( final SbName partName, 
                        boolean makeIfNeeded, boolean leafCheck, 
                        boolean publicCheck, final SoPath pathToExtend )
//
////////////////////////////////////////////////////////////////////////
{
    // Try to create a path to the part:
    SoNodeKitPath  answerPath = super.createPathToAnyPart(
                                    partName, makeIfNeeded, leafCheck, 
                                    publicCheck, pathToExtend );

    // You might not be able to, for example:
    //    [a] makeIfNeeded == FALSE
    //    [b] part does not exist
    //    [c] we ask it to leafCheck or publicCheck and it fails.
    if ( answerPath == null )
        return null;

    // If you were successful, then try to set the surrogate path to NULL
    answerPath.ref();
    if ( !setAnySurrogatePath( partName, null, leafCheck, publicCheck )) {
//#ifdef DEBUG
        SoDebugError.post("SoInteractionKit::createPathToAnyPart",
                "can not set surrogate path to NULL for part "+
                partName.getString() );
//#endif
    }
    answerPath.unrefNoDelete();

    return answerPath;
}


	// Reimplemented from SoBaseKit.
protected boolean setAnyPart( String partName, SoNode from) {
        return setAnyPart(partName, from, true );
}
protected boolean setAnyPart(String partName, SoNode from, boolean anyPart) { // java port
	return setAnyPart(new SbName(partName), from, anyPart);
}
	protected boolean setAnyPart(SbName partName, SoNode from, boolean anyPart) {
		
	     // Try to create set the part:
		       // You might not be able to, for example:
		       //    [a] part does not exist
		       //    [b] anyPart is false and the part is a leaf or non-public.
		       if ( !super.setAnyPart( partName, from, anyPart ))
		           return false;
		   
		       // Temporary ref
		       if (from!= null)
		           from.ref();
		   
		       // If you were successful, then try to set the surrogate path to null
		       boolean success = true;
		       if ( !setAnySurrogatePath( partName, null, !anyPart, !anyPart )) {
//		   #ifdef DEBUG
//		           SoDebugError::post("SoInteractionKit::setAnyPart",
//		                   "can not set surrogate path to null for part %s",
//		                   partName.getString() );
//		   #endif
		           success = false;
		       }
		   
		       // Undo temporary ref
		       if (from != null)
		           from.unref();
		       return success;
		  	}
	

////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
 protected boolean
setAnySurrogatePath( final SbName partName, 
                        SoPath from, boolean leafCheck, boolean publicCheck )
//
////////////////////////////////////////////////////////////////////////
{
    // Strategy:
    //   [-2] If 'partName' is in this catalog, just call 
    //        setMySurrogatePath and return.
    //   [-1] If 'from' is null, and partName is not directly in this catalog,
    //        determine which of our leaf nodes is on the way to the part.
    //        This will be either a nodekit or a list part.
    //        If this 'intermediary' has not been created yet, then we
    //        can just return.  This is because if the intermediary is null,
    //        then the part below it can have no value as of yet. So we don't
    //        need to bother removing it's surrogate path (which is what 
    //        we do when 'from' is null.
    //   [0] Temporarily ref 'from' and 'this'
    //       We need to ref 'this' because creating a path refs this,
    //       and this can get called from within constructors since
    //       it's called from within setPart(), getPart(), etc.
    //   [1] get partPath, which leads down to the part.
    //       First time, use 'makeIfNeeded' of false.
    //       That's how we'll find out if there was something there to start.
    //
    //   [2] If (partPath == null), call it again with 'makeIfNeeded' of true,
    //       but remember that we must null out the part when we are finished.
    //
    //   [3] Now we've got a path from 'this' down to the part.
    //       Find 'owner', the first InteractionKit above the part in the 
    //       partPath. 
    //       Note:   'owner' might not == this.
    //   [4] Find the 'nameInOwner' the name of the part within 'owner'
    //   [5] Tell 'owner' to use the given path 'from' as its surrogate 
    //       path for 'nameInOwner'
    //   [6] If you need to, remember to set the node-part back to null
    //   [8] Undo temporary ref of 'from' and 'this'

    //   [-2] If 'partName' is in this catalog, just call 
    //        setMySurrogatePath and return.
        final SoNodekitCatalog cat = getNodekitCatalog();
        int partNum = cat.getPartNumber( partName );
        if ( partNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND ) {
            if ( leafCheck && (cat.isLeaf(partNum) == false) )
                return false;
            if ( publicCheck && (cat.isPublic(partNum) == false) )
                return false;
            setMySurrogatePath(partName, from);
            return true;
        }

    //   [-1] If 'from' is null, and partName is not directly in this catalog,
    //        determine which of our leaf nodes is on the way to the part.
    //        This will be either a nodekit or a list part.
    //        If this 'intermediary' has not been created yet, then we
    //        can just return.  This is because if the intermediary is null,
    //        then the part below it can have no value as of yet. So we don't
    //        need to bother removing its surrogate path (which is what 
    //        we do when 'from' is null.
        if (from == null) {

            // See if there's a '.' and/or a '[' in the partName.
            // (as in "childList[0].appearance")
            // If so, get the string up to whichever came first.
            // This will be the 'intermediary' we look for.
            Integer dotPtr   =  strchr( partName.getString(), '.' );
            Integer brackPtr =  strchr( partName.getString(), '[' );

            if ( dotPtr != null || brackPtr != null ) {
                String nameCopy = strdup( partName.getString() );
                String firstName;
                if (dotPtr == null)
                    firstName = strtok( nameCopy, "[");
                else if (brackPtr == null || dotPtr < brackPtr)
                    firstName = strtok( nameCopy, ".");
                else 
                    firstName = strtok( nameCopy, "[");

                // Okay, look for the part, then free the string copy.
                int firstPartNum = cat.getPartNumber( new SbName(firstName) );

                SoNode firstPartNode = null; 
                if ( firstPartNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND ) {
                    // Check if the part is there.
                    // 2nd arg is false, 'cause we don't want to create part.
                    // 3rd arg is true 'cause this better be a leaf.
                    firstPartNode = super.getAnyPart( new SbName(firstName), 
                                                false, true, publicCheck );
                }
                //free (nameCopy); java port

                // If the intermediary doesn't exist, return true
                if (firstPartNode == null)
                    return true;
            }
        }


    //   [0] Temporarily ref 'from' and 'this'
        if (from != null) from.ref();
        ref();

    //   [1] get partPath, which leads down to the part.
    //       First time, use 'makeIfNeeded' of false.
    //       That's how we'll find out if there was something there to start.
        boolean        makeIfNeeded = false;
        SoNodeKitPath partPath    = null;

        partPath = super.createPathToAnyPart( partName, makeIfNeeded, 
                                                   leafCheck, publicCheck );

    //   [2] If (partPath == null), call it again with 'makeIfNeeded' of true,
    //       but remember that we must null out the part when we are finished.
        if (partPath == null) {
            // Try again, this time with 'makeIfNeeded' true
            makeIfNeeded = true;
            partPath = super.createPathToAnyPart( partName, makeIfNeeded, 
                                                   leafCheck, publicCheck );
        }
        if (partPath == null) {
            // This would happen if leafCheck or publicCheck were true
            // and the check failed.
//#ifdef DEBUG
//            SoDebugError::post("SoInteractionKit::setAnySurrogatePath",
//                "can not get a part path for part %s", partName.getString());
//#endif
            // Undo temporary ref of 'from' and 'this'
            if (from != null) from.unref();
            unrefNoDelete();
            return false;
        }
        else
            partPath.ref();

    //   [3] Now we've got a path from 'this' down to the part.
    //       Find 'owner', the first InteractionKit above the part in the 
    //       partPath. 
    //       Note:   'owner' might not == this.
        SoInteractionKit owner = null;
        for (int i = partPath.getLength() - 1;  i >= 0; i-- ) {
            SoNode n = partPath.getNode(i);
            if ( n != (SoFullPath.cast (partPath)).getTail() &&
                 n.isOfType( SoInteractionKit.getClassTypeId() ) ) {
                    owner = (SoInteractionKit ) n;
                    owner.ref();
                    break;
            }
        }
        if ( owner == null ) {
            partPath.unref();
            // Undo temporary ref of 'from' and this.
            if (from != null) from.unref();
            unrefNoDelete();
            return false;
        }

    //   [4] Find the 'nameInOwner' the name of the part within 'owner'
        SbName nameInOwner = new SbName(owner.getPartString( partPath ));
        
    //   [5] Tell 'owner' to use the given path 'from' as its surrogate 
    //       path for 'nameInOwner'
    //       Use setMySurrogatePath for this...
        owner.setMySurrogatePath(nameInOwner,from);

    //   [6] If you need to, remember to set the node-part back to null
        boolean success = true;
        if (makeIfNeeded == true) {
            boolean anyPart = ( !leafCheck && !publicCheck );
            if ( !super.setAnyPart( partName, null, anyPart ) )
                success = false;
        }

        owner.unref();
        partPath.unref();

    //   [8] Undo temporary ref of 'from' and 'this'
        if (from != null) from.unref();
        unrefNoDelete();

        return success;
}
 
 //
  // Assumes that this node is the 'owner' of the surrogate part.
  // That is, it is the first InteractionKit above the part.
  //
  // This means that 'name' is registered in this node's catalog,
  // or if the name is 'listName[#]', then listName is in the catalog.
  //
  // Passing a value of null for newPath has the effect of removing 
  // this part from the surrogate path lists.
  //
  // Use: private
  //
  private void setMySurrogatePath( SbName name, SoPath newPath )
  //
  {
      int index = surrogatePartNameList.find( (Object) name.getString() );
      if ( index != -1 ) {
          // an entry already exists for this part name. So we need to remove
          // the old entry before adding the new one.
          surrogatePartNameList.remove( index );
          surrogatePartPathList.remove( index );
      }
  
     // Now append the new entry.
     if ( newPath != null ) {
         surrogatePartNameList.append( (Object) name.getString() );
         surrogatePartPathList.append( newPath );
     }
 }
  

  //! Is the 'inPath' valid as a surrogate path anywhere in this node's
  //! subgraph? 
  //! Examines the surrogate paths of all InteractionKits from this node down.
  //! The first time a surrogate path is found that is contained within 
  //! 'inPath', then TRUE is returned.
  //! The second version also returns information about the surrogate,
  //! only if (fillArgs == TRUE).
////////////////////////////////////////////////////////////////////////
//
// Use: EXTENDER public
//
public boolean
isPathSurrogateInMySubgraph( final SoPath pathToCheck )
//
////////////////////////////////////////////////////////////////////////
{
    final SoPath[] pathToOwner = new SoPath[1], pathInOwner = new SoPath[1];
    final SbName  nameInOwner = new SbName();

    return( isPathSurrogateInMySubgraph( pathToCheck, pathToOwner,
                                       nameInOwner, pathInOwner, false ));
}

////////////////////////////////////////////////////////////////////////
//
// Use: EXTENDER public
//
private static SoSearchAction searchAction = null;
public boolean
isPathSurrogateInMySubgraph( final SoPath pathToCheck,
                       final SoPath[] pathToOwner, final SbName  surrogateNameInOwner,
                       final SoPath[] surrogatePathInOwner) { //java port
	return isPathSurrogateInMySubgraph( pathToCheck,
                     pathToOwner,  surrogateNameInOwner,
                     surrogatePathInOwner, true );

}
public boolean
isPathSurrogateInMySubgraph( final SoPath pathToCheck,
                       final SoPath[] pathToOwner, final SbName  surrogateNameInOwner,
                       final SoPath[] surrogatePathInOwner, boolean fillArgs )
//
////////////////////////////////////////////////////////////////////////
{
    // Search inside this kit and each InteractionKit below.
    // See if they have a surrogate path that is wholely contained within
    // pathToCheck.
    // The first time you find one, fill in the info and return.

    // Get the paths to the InteractionKits
        if (searchAction == null)
            searchAction = new SoSearchAction();
        else
            searchAction.reset();
        searchAction.setType( SoInteractionKit.getClassTypeId() );
        searchAction.setInterest( SoSearchAction.Interest.ALL );
        boolean oldNkVal = SoBaseKit.isSearchingChildren();
        SoBaseKit.setSearchingChildren( true );
        searchAction.apply(this);
        SoBaseKit.setSearchingChildren( oldNkVal );

        SoPathList ownerPaths = searchAction.getPaths();

    // Start at the end and work backwards.
    // More likely to find a closer match this way...
    // For each path:
        for (int i = ownerPaths.getLength() - 1; i >= 0; i-- ) {

            // Get the potential owner:
                SoPath ownerPath = ownerPaths.operator_square_bracket(i);
                SoInteractionKit owner 
                    = (SoInteractionKit ) (SoFullPath.cast(ownerPath)).getTail();

            // See if the potential owner is in fact the owner...
                int surrogateNum = owner.indexOfSurrogateInMyList(pathToCheck);

            // If it's the owner, we've succeeded and should return.
                if ( surrogateNum != -1 ) {
                    if (fillArgs == true) {
                        pathToOwner[0] = ownerPath.copy();
                        surrogateNameInOwner.copyFrom((String) 
                              ((owner.surrogatePartNameList)).operator_square_bracket(surrogateNum));
                        surrogatePathInOwner[0] =
                              ((owner.surrogatePartPathList)).operator_square_bracket(surrogateNum);
                    }
                    return true;
                }
        }

    // Return FALSE if you could not find an owner.
        return false;
}

////////////////////////////////////////////////////////////////////////
//
// Use: private
//
private int
indexOfSurrogateInMyList( final SoPath pathToCheck )
//
////////////////////////////////////////////////////////////////////////
{
    if (pathToCheck == null)
        return -1;

    // Check each of the surrogate paths...
    for ( int i = 0; i < surrogatePartPathList.getLength(); i++ ) {

        // If the pathToCheck contains the surrogate path, we're okay!
        if ( pathToCheck.containsPath( (surrogatePartPathList).operator_square_bracket(i) ) )
            return i;
    }
    return -1; // no match found
}

  
 
  ////////////////////////////////////////////////////////////////////////
   //
   // Description:
   //    overrides method in SoNode to return false.
   //    Since there is a separator as the top child of the dragger.
   //
   // Use: public
   //
  public boolean
   affectsState()
   {
       return false;
   }
   

 // java port
 private Integer strchr(String str, int character) {
	 int index = str.indexOf(character);
	 if(index >= 0) {
		 return index;
	 }
	 else {
		 return null;
	 }
 }
 
 private String strdup(String str) {
	 return str;
 }
 
 private String strtok(String str, String tok) {
	 StringTokenizer stringTokenizer = new StringTokenizer(str, tok);
	 if(stringTokenizer.hasMoreTokens()) {
		 return stringTokenizer.nextToken();
	 }
	 return null;
 }
 
 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates dictionary of part names corresponding to different nodes.
//
//    The 'priority' of entries is as follows:
//
//    Anything in 'filename' will definitely be used.
//    Anything in 'defaultBuffer' will be used, if it's not found in
//    'filename'.
//
// Use: protected
//
protected void readDefaultParts(String fileName, 
                                short[] defaultBuffer, 
                                int defBufSize )
//
////////////////////////////////////////////////////////////////////////
{
    // Next, read the contents of 'defaultBuffer'. 
    // Add the parts found within this to the dictionary.
    SoGroup bufStuff = readFromBuffer( defaultBuffer, defBufSize );
    if (bufStuff != null)
        bufStuff.ref();

    // Next, read the contents of 'fileName'
    // Add the parts found within this to the dictionary.
    SoGroup fileStuff = readFromFile( fileName );
    if (fileStuff != null)
        fileStuff.ref();
}


////////////////////////////////////////////////////////////////////////
//
// Use: public
//
// Description:
//     This overloaded version of setPartAsDefault takes the name
//     of the node as an argument.  It looks the node up in the global
//      dictionary.
//
////////////////////////////////////////////////////////////////////////
public boolean
setPartAsDefault(final String partName, 
                               final String newNodeName )
{
	return setPartAsDefault(new SbName(partName), new SbName(newNodeName),true);
}
public boolean
setPartAsDefault(final SbName partName, 
                               final SbName newNodeName, boolean onlyIfAlready )
{
    return( setAnyPartAsDefault( partName, newNodeName, false, onlyIfAlready ));
}

////////////////////////////////////////////////////////////////////////
//
// Use: protected
//
// Description:
//     Protected versions of these methods that allow you to set non-leaf
//     and/or private parts.
////////////////////////////////////////////////////////////////////////
public boolean 
setAnyPartAsDefault(final SbName partName, 
                        final SbName newPartName, boolean anyPart,
                        boolean onlyIfAlready )
{
    // Try to find the given node in the global dictionary.
    SoNode newNode = super.getByName(newPartName);
    return( setAnyPartAsDefault( partName, newNode, anyPart, onlyIfAlready ));
}
public boolean // java port 
setAnyPartAsDefault(final String partName, 
                        SoNode newNode) {
	return setAnyPartAsDefault(new SbName(partName),newNode,true,true);
}
public boolean 
setAnyPartAsDefault(final SbName partName, 
                        SoNode newNode, boolean anyPart, boolean onlyIfAlready )
{
    // Ref ourself while in this routine.
    ref();

    // Find the 'owner' kit. This is the kit (which could be a child of this 
    // one) which is the closest parent above the part.
    SoField   f;
    SoBaseKit owner = null;
    boolean happyDone = false;
    boolean reply = true;

    // If the partName is in our catalog, we are the owner.
    if (getNodekitCatalog().getPartNumber(partName) 
            != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND)
        owner = this;

    if ( onlyIfAlready ) {
        // We have to determine if the part already isDefault()

        // If we're the owner, it's pretty easy to check...
        if ( owner == this ) {
            f = getField( partName );
            if ( f==null ||  ! f.isDefault() )
                reply = false;
        }
        else {
            // Create a path to the part, but use makeIfNeeded of false.
            // Don't check leaf or public. We just want information.
            SoNodeKitPath testP 
                = createPathToAnyPart(partName, false,false,false);
            if (testP != null) {
                testP.ref();

                // Cast to a full path:
                    SoFullPath fp = SoFullPath.cast(testP);
                // The part is the tail of the full path:
                    /*SoNode *part =*/ fp.getTail();
                    SoNode tester;
                // Owner is first nodekit above the tail:
                    for (int ind = 1; ind < fp.getLength(); ind ++ ) {
                        tester = fp.getNodeFromTail(ind);
                        if ( tester.isOfType(SoBaseKit.getClassTypeId()) ) {
                            owner = (SoBaseKit ) tester;
                            break;
                        }
                    }

                if (owner == null)     // This should never happen...
                    reply = false;
                else {

                    // Determine name of part in the owner's catalog.
                    // If contains '.' character (e.g., "duck.foot.toe")
                    // take LAST word.
                    String partStringInOwner;
                    int lastDot = partName.getString().lastIndexOf('.');
                    if (lastDot != -1)
                        partStringInOwner = partName.getString().substring(lastDot+1);//lastDot+1;
                    else
                        partStringInOwner = partName.getString();

                    // Find out if the part isDefault()
                    f = owner.getField(partStringInOwner);
                    if (f==null || ! f.isDefault() )
                        reply = false;
                    else if ( ((SoSFNode )f).getValue() == newNode ) {
                        // We can return successfully!  The part is default,
                        // and already equals newNode.
                        happyDone = true;
                    }
                }

                testP.unref();
            }
            // If path was null, the part has not been created, so 
            // we will allow ourselves to set the part as default. We may
            // continue...
        }
    }

    // Did we pass the onlyIfAlready test (if we were required to)??
    if ( reply == false ) {
        unrefNoDelete();
        return false;
    }

    // Was the part already there and already default?
    if ( happyDone == true ) {
        unrefNoDelete();
        return false;
    }

    // If we can't set the part, then bag it and return.
    if ( !setAnyPart(partName,   newNode, anyPart )) {
        unrefNoDelete();
        return false;
    }

    // If we still don't know the owner, we've got to find out now...
    if (owner == null) {

        // use makeIfNeed of TRUE. We'll set the part back to null if 
        // newNode is null.
        SoNodeKitPath nkp = createPathToAnyPart( partName, true,
                                                 !anyPart, !anyPart );
        if (nkp == null)
            reply = false;
        else {
            nkp.ref();

            SoBaseKit tail = (SoBaseKit ) nkp.getTail();

            if (tail != newNode)
                owner = (SoBaseKit ) tail;
            else
                owner = (SoBaseKit ) nkp.getNodeFromTail( 1 );

            nkp.unref();
        }
    }
    if (owner == null)
        reply = false;

    // We got the owner.
    if ( reply ) {
        owner.ref();

        // Determine name of part in the owner's catalog.
        // If contains '.' character (e.g., "duck.foot.toe")
        // take LAST word.
        String partStringInOwner;
        int lastDot = partName.getString().lastIndexOf('.');
        if (lastDot != -1)
            partStringInOwner = partName.getString().substring(lastDot+1);
        else
            partStringInOwner = partName.getString();

        // If newNode is null, set it back to null now.
        // (remember, we forced part creation when we
        // made the path to find the owner).
        if (newNode == null)
            setAnyPart( partName,null,anyPart);

        // call setDefault(TRUE) on the part.
        f = owner.getField(partStringInOwner);
        if (f != null)
            f.setDefault(true);
        else
            reply = false;

        owner.unref();
    }

    unrefNoDelete();
    return reply;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates shared dragger geometry 
//
// Use: protected
//
protected SoGroup readFromFile(String fileName)
//
////////////////////////////////////////////////////////////////////////
{
    final SoInput     in = new SoInput();

    in.destructor(); // java port
    
    // Only look for the file if the environment variable is set.
    if ( System.getenv("SO_DRAGGER_DIR") == null )
        return null;

    return null; //TODO
}
 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads geometry from a set of buffers.
//    Assumes that the last buffer is empty.
//    If the buffer contains numerous scene graphs, puts them all under
//    a common separator.
//
// Use: protected
//
protected SoGroup 
readFromBuffer(short[] defaultBuffer, int defBufSize )
//
////////////////////////////////////////////////////////////////////////
{
    final SoInput     in = new SoInput();

    if ( defaultBuffer == null )
        return null;

    int bufLen = defaultBuffer.length;
    StringBuffer buf = new StringBuffer();
    for(int i =0;i<bufLen;i++) {
    	buf.append((char)defaultBuffer[i]);
    }
    
    in.setBuffer( buf.toString(), defBufSize );

    // read it on in...
    SoSeparator  root = SoDB.readAll( in );

    return root;
}

 
/////////////////////////////////////////////////////////////////////////
//
// Called by the SoBaseKit::write() method. 
//
// InteractionKits don't want to write fields if they have default vals.
//
// Calls setDefault(true) on caching fields if default and not 
// connected.
//
// Calls setDefault(true) on the topSeparator. 
// Calls setDefault(true) on the geomSeparator. 
// 
// Calls setDefault(true) on any non-leaf part of type exactly == SoSwitch.
// Subclasses can override this if they like, since the function is virtual.
// But interactionKits use non-leaf switches to turn parts on and off, and 
// this does not need to be written to file.
//
// Lastly, calls same method on SoBaseKit.
//
// NOTE: Parts which are set to default may still wind up writing to file 
//       if, for example, they lie on a path.
/////////////////////////////////////////////////////////////////////////
//Here's a little macro to test the field value against another node and
//also make sure there's no active connection.
private boolean __SO_FCHECK( SoSFEnum f, int v ) {
       return ( ! (f.isConnected() && f.isConnectionEnabled())  
         && f.getValue() == v );
}
public void setDefaultOnNonWritingFields()
{

    // Calls setDefault(true) on caching fields if default and not 
    // connected.
        if ( __SO_FCHECK( renderCaching, SoInteractionKit.CacheEnabled.AUTO.ordinal() ) )
                          renderCaching.setDefault(true);
        if ( __SO_FCHECK( boundingBoxCaching, SoInteractionKit.CacheEnabled.AUTO.ordinal() ) )
                          boundingBoxCaching.setDefault(true);
        if ( __SO_FCHECK( renderCulling, SoInteractionKit.CacheEnabled.AUTO.ordinal() ) )
                          renderCulling.setDefault(true);
        if ( __SO_FCHECK( pickCulling, SoInteractionKit.CacheEnabled.AUTO.ordinal() ) )
                          pickCulling.setDefault(true);

//#undef __SO_FCHECK

    // Calls setDefault(true) on the topSeparator. 
        topSeparator.setDefault(true);
        geomSeparator.setDefault(true);


    // Calls setDefault(true) on any non-leaf part of type exactly == SoSwitch.
        final SoNodekitCatalog cat = getNodekitCatalog();
        int   numParts = cat.getNumEntries();

        for (int i = 1; i < numParts; i++ ) {

            // The part should not be a leaf.
                if ( cat.isLeaf(i) )
                    continue;

                SoSFNode fld = (SoSFNode ) getField( cat.getName(i) );
                SoNode   n   = fld.getValue();

            // set to default if node is null
                if (n == null ) {
                    fld.setDefault(true);
                    continue;
                }

            // set to default if node is exactly an SoSwitch.
                if ( n.getTypeId().operator_equal_equal(SoSwitch.getClassTypeId())) {
                    fld.setDefault(true);
                    continue;
                }
        }

    // Call the base class...
        super.setDefaultOnNonWritingFields();
}
 

////////////////////////////////////////////////////////////////////////
//
// Use: EXTENDER public, static
//
public void setSwitchValue( SoNode n, int newVal )
//
////////////////////////////////////////////////////////////////////////
{
    if ( n != null ) {
        if  (n.getTypeId().operator_equal_equal(SoSwitch.getClassTypeId())) {
            SoSwitch sw = (SoSwitch ) n;
            if (sw.whichChild.getValue() != newVal && 
                sw.getNumChildren() > newVal )
                sw.whichChild.setValue( newVal );
        }
    }
}


	
 ////////////////////////////////////////////////////////////////////////
  //
  // Description:
  //    Initialize the class
  //
  // Use: static, internal
  //
  
 public static void
  initClass()
  //
  ////////////////////////////////////////////////////////////////////////
  {
      SO__KIT_INIT_CLASS(SoInteractionKit.class, "InteractionKit", SoBaseKit.class);
  }
  }
