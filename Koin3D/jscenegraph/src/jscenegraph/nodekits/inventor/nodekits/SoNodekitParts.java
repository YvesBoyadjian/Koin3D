/**
 * 
 */
package jscenegraph.nodekits.inventor.nodekits;

import java.util.Objects;
import java.util.StringTokenizer;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodekitParts { //TODO

	private SoNodekitCatalog catalog;
	int numEntries;
	SoBaseKit rootPointer;
	SoSFNode[] fieldList;
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: internal

public SoNodekitParts( SoBaseKit rootOfKit )
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if ( rootOfKit == null ) {
        SoDebugError.post("SoNodekitParts::SoNodekitParts",
    "rootOfKit is NULL. Can not continue construction. Expect a core dump");
        return;
    }
//#endif

    rootPointer = rootOfKit;
    catalog = rootOfKit.getNodekitCatalog();  // assign the catalog

    numEntries = catalog.getNumEntries();        // make a empty node list
    fieldList = new  SoSFNode [numEntries] ;

    // Make each field in the fieldList point to the field corresponding
    // to the similarly indexed catalog entry.
    final SbName partName = new SbName();
    final SbName fieldName = new SbName();

    for ( int i = 0; i < numEntries; i++ ) {
        // Get the name of the part from the catalog
         partName.copyFrom(catalog.getName( i ));

        // Find the field for that part, and set fieldList entry 
        // to point at it.
         if ( i == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM ) {
            // For the part "this" we do NOT fill in a field. We set it to
            // NULL and are careful not to try to look at its contents.
            fieldList[i] = null;
         }
         else {
            SoFieldData fData;
            fData = rootOfKit.getFieldData();
            for ( int j = 0; j < fData.getNumFields(); j++ ) {
                fieldName.copyFrom(fData.getFieldName(j));
                if (partName.operator_equal_equal( fieldName)) {
                    SoSFNode theField =
                                (SoSFNode ) fData.getField(rootOfKit,j);
                    fieldList[i] = theField;

                    // Turn off notification on this field.
                    // We store the info as a field, but unless we 
                    // turn off notification, everything takes forever.
                    theField.enableNotify(false);
                }
            }
         }
    }
}


////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: internal

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
// delete the nodelist
	fieldList = null;//delete [ /*numEntries*/ ] fieldList;
}

	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates the node for the part asked for.
//    If the part already exists, then just return true without doing anything
//    If not, it creates the part and all necessary parts up to the top of
//    of the nodekit, and places them properly as children in the graph.
//
//    Note:
//       this routine will NOT search for the requested part name within the
//       catalogs of its child parts.
//
// Use: internal

private boolean
makePart( int partNum )
//
////////////////////////////////////////////////////////////////////////
{
    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
        return true;
//#ifdef DEBUG
    if ( !partFoundCheck( partNum ) )
        return false;
//#endif

    // if the part is already in the Node list, just return true
    if ( verifyPartExistence( partNum ) )
        return true;

    // create the node
    SoNode inst = (SoNode ) 
                catalog.getDefaultType( partNum ).createInstance();
//#ifdef DEBUG
    if ( inst == null ) {
        SoDebugError.post("SoNodekitParts::makePart",
    "Can't make part "+catalog.getName(partNum).getString()+". It belongs to an abstract class. Bad parts catalog");
        return false;
    }
//#endif

    // If it's a list part, set the containerType and childTypes...
    if ( catalog.isList( partNum ) ) {
        SoNodeKitListPart lp = (SoNodeKitListPart ) inst;
        lp.setContainerType( catalog.getListContainerType( partNum ) );

        final SoTypeList childTypes = catalog.getListItemTypes( partNum );
        for ( int i = 0; i < childTypes.getLength(); i++ )
            lp.addChildType( childTypes.operator_square_bracket(i) );

        lp.lockTypes();
    }

    inst.ref(); // temporarily ref it, until it has a parent

    // create its parent part
    int parentPartNum = catalog.getParentPartNumber( partNum );
    // Turn off notification while building the parent part.
    // We'll be notifying when adding the lower part, so let's not
    // notify twice.
    boolean wasEn = rootPointer.enableNotify(false);
    boolean madeOk = makePart( parentPartNum );
    rootPointer.enableNotify(wasEn);
    if ( ! madeOk ) {
//#ifdef DEBUG
//        SoDebugError::post("SoNodekitParts::makePart",
//                           "Can\'t make parent for part named %s", 
//                            catalog.getName(partNum).getString() );
//#endif
        inst.unref();             // undo the temporary ref
        return false;
    }

    // Don't need to check for 'this' part, since we would have returned
    // earlier from verifyPartExistence.
    fieldList[partNum].setValue( inst );

    // find the next closest right sibling that already exists
    int    sibPartNum, searchPartNum;

    for( sibPartNum = -1, 
         searchPartNum = catalog.getRightSiblingPartNumber( partNum );
         sibPartNum == -1 && searchPartNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND;
         searchPartNum = catalog.getRightSiblingPartNumber(searchPartNum )) {
        if ( verifyPartExistence( searchPartNum ) )
            sibPartNum = searchPartNum;
    }

    SoBaseKit parentKit = null;
    SoGroup   parentGroup = null;
    if ( parentPartNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
        parentKit   = rootPointer;
    else
        parentGroup = (SoGroup )   fieldList[parentPartNum].getValue();

    if (sibPartNum == -1 ) {
        // no right sibling is made yet, so just add this as a child to parent
        if ( parentKit != null )
            parentKit.addChild( inst );
        else
            parentGroup.addChild( inst );
    }
    else {
        // a right sibling exists. Put this node just in front of it.
        if ( parentKit != null ) {
        int sibIndex = parentKit.findChild(fieldList[sibPartNum].getValue());
        parentKit.insertChild( fieldList[partNum].getValue(), sibIndex );
        }
        else {
        int sibIndex =parentGroup.findChild(fieldList[sibPartNum].getValue());
        parentGroup.insertChild( fieldList[partNum].getValue(), sibIndex );
        }
    }
    inst.unref();  // undo the temporary ref
    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Replaces the node described with the one passed in.
//
//    If the part already exists, then the current part is removed from 
//    its parent and the node list.
//
//    Next, the new part is put in the list and made the child of its parent.
//
//    Note:
//       this routine will NOT search for the requested part name within the
//       catalogs of its child parts.
//
// Use: internal

boolean
replacePart( int partNum, SoNode newPartNode )
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
        return false;
    if ( !partFoundCheck( partNum ) )
        return false;
//#endif

    // make sure the node given is of the proper type
    if ( newPartNode != null
         && !newPartNode.isOfType( catalog.getType( partNum ) ) ) {
//#ifdef DEBUG
        SoDebugError.post( "SoNodekitParts::replacePart",
                            "the given part is not of the correct type");
//#endif
        return false;
    }

    // If it's a list part, make sure all the types match okay.
    if ( newPartNode != null && catalog.isList( partNum ) ) {

        SoTypeList  kidTypes = catalog.getListItemTypes( partNum );
        SoNodeKitListPart lp = (SoNodeKitListPart ) newPartNode;

        // If types aren't locked, make this part have the right types,
        // then lock them.
        if ( !lp.isTypeLocked() ) {
            lp.setContainerType(catalog.getListContainerType(partNum));
            for (int j = 0; j < kidTypes.getLength(); j++)
                lp.addChildType( kidTypes.operator_square_bracket(j) );
            lp.lockTypes();
        }

        // Make sure container is of correct type.
        if ( lp.getContainerType().operator_not_equal( catalog.getListContainerType(partNum))) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::replacePart",
//                            "This new list part has wrong type container");
//#endif
            return false;
        }

        boolean okay = true;

        // Make sure child types are all okay.
        for ( int i = 0; i < kidTypes.getLength(); i++ )
            if (    lp.isTypePermitted( kidTypes.operator_square_bracket(i)) == false )
                okay = false;

        if ( okay == false ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::replacePart",
//        "Allowable child types in given list do not match the specs in the catalog");
//#endif
            return false;
        }
    }

    int parentPartNum = catalog.getParentPartNumber( partNum );

    // So they don't try replacing 'this'
    if ( !partFoundCheck( parentPartNum ) )
        return false;

    // If new node isn't null, create its parent part
    if ( newPartNode != null ) {
        // Turn off notification while building the parent part.
        // We'll be notifying when adding the lower part, so let's not
        // notify twice.
        boolean wasEn = rootPointer.enableNotify(false);
        boolean madeOk = makePart( parentPartNum );
        rootPointer.enableNotify(wasEn);
        if ( ! madeOk ) {
//#ifdef DEBUG
            SoDebugError.post( "SoNodekitParts::replacePart",
                                "can't make parent for part named "+ 
                                catalog.getName(partNum).getString() );
//#endif
            return false;
        }
    }

    // Now we know there's a parent part, if we need one.
    // Set the appropriate parent pointers. 
        SoBaseKit parentKit = null;
        SoGroup   parentGroup = null;
        if ( parentPartNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
            parentKit   = rootPointer;
        else
            parentGroup = (SoGroup )   fieldList[parentPartNum].getValue();

    // If an old part is already in the Node list, then
    // get rid of any parts that are its descendants.
    // This will not get rid of the old part itself.
        SoNode oldPart = null;
        if ( verifyPartExistence( partNum ) ) {
            // unregister all the descendant parts of this part by setting 
            // the appropriate SoSFField values in the field list to null. 
            unregisterDescendants( partNum );

            oldPart = fieldList[partNum].getValue();
        }

    // set the new part as the new field value.
        fieldList[partNum].setValue( newPartNode);

    // If the new part is null, remove the oldPart from its parent.
    // Then, remove parent if it is no longer needed.
        if (newPartNode == null) {
            if ( oldPart != null) {
                if (parentKit != null) 
                    parentKit.removeChild( oldPart );
                else if (parentGroup != null) {
                    // If the parent is a switch, make sure this doesn't 
                    // screw it up...
                    parentGroup.removeChild( oldPart );
                    if ( parentGroup.isOfType( SoSwitch.getClassTypeId() ) ){
                        SoSwitch sw = (SoSwitch ) parentGroup;
                        int swNum = sw.getNumChildren();
                        if (sw.whichChild.getValue() >= swNum)
                            sw.whichChild.setValue(  swNum - 1 );
                    }
                }
            }
            // We just set partNum to null and removed from its parent.
            // Get rid of the parent, too, if we can...
            if ( partIsNonLeafAndMayBeDeleted( parentPartNum ) )
                replacePart( parentPartNum, null );

            return true;
        }

    // If the new part is not a leaf in the structure, it had better
    // not have any children.  You've got to set the children after setting
    // the internal part.
        if ( catalog.isLeaf( partNum ) == false ) {
            SoGroup partAsGroup = (SoGroup ) newPartNode;
            if ( partAsGroup.getNumChildren() != 0 ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::replacePart",
//                "The given part is a non-leaf in the catalog and contains children. When setting a non-leaf, the node must have 0 children.");
//#endif
                return false;
            }
        }

    // If there's already a part, then we'll use replace child.
    // This way, the new part will be installed in any paths containing oldPart
    // Then return.
        if (oldPart != null) {
            if ( parentKit != null )
                parentKit.replaceChild( oldPart, newPartNode );
            else if (parentGroup != null)
                parentGroup.replaceChild( oldPart, newPartNode );

            return true;
        }

    // There was no old part.
    // Find the next closest right sibling that already exists
        int sibPartNum, searchPartNum;
        for( sibPartNum = -1, 
             searchPartNum = catalog.getRightSiblingPartNumber( partNum );
             sibPartNum == -1 && searchPartNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND;
             searchPartNum = catalog.getRightSiblingPartNumber(searchPartNum)){
            if ( verifyPartExistence( searchPartNum ) )
                sibPartNum = searchPartNum;
        }

    // If newPartNode is going to be the rightmost child, use addChild...
        if (sibPartNum == -1 ) {
            if ( parentKit != null )
                parentKit.addChild( newPartNode );
            else if ( parentGroup != null )
                parentGroup.addChild( newPartNode );
            return true;
        }

    // Otherwise, insert the new part in front of the sibling.
        SoNode sibNode = fieldList[sibPartNum].getValue();
        if ( parentKit != null ) {
            int sibIndex = parentKit.findChild( sibNode );
            parentKit.insertChild( newPartNode, sibIndex );
        }
        else {
            int sibIndex =parentGroup.findChild( sibNode );
            parentGroup.insertChild( newPartNode, sibIndex );
        }

    return true;
}



	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the given part with the node passed in as an argument.
//    Returns true if successful, false if not.
//
// Use: private

boolean
setAnyPart( final SbName nameOfPart, SoNode newPartNode, 
                            boolean anyPart )
//
////////////////////////////////////////////////////////////////////////
{
    String nameCopy, firstName, remainderString;
    SoNode firstNode = null;
    boolean answer;

    // JUST A SINGLE NAME...
    if ( Util.strrchr( nameOfPart.getString(), '.' ) == null )
        return setSingleNamePart( nameOfPart, newPartNode, anyPart );

    // OTHERWISE, A COMPOUND NAME of the form:  part1.part2.part3 etc.
    // copy the string
    nameCopy = strdup( nameOfPart.getString() );

    // break string into two parts
    firstName = strtok( nameCopy, ".");   // everything before the first '.'
    remainderString = nameCopy.substring(nameCopy.indexOf('.')+1);//strtok( null, "");  // everything else

    // Get firstNode:
    // [1] Only need to create it if (newPartNode != null)
    //     This is because, if new part is to be null, then the
    //     nodekit containing it need not exist either.
    // [2] intermediate names have to be leaves, so arg3 (leafCheck) is true
    boolean needToMake = ( newPartNode != null);
    boolean[] firstExistedBefore = new boolean[1];
    firstNode = getSingleNamePart( new SbName(firstName), needToMake, 
                                   true, !anyPart, firstExistedBefore );

    if ( firstNode == null && needToMake == false ) {
        answer = true;
    }
    else if ( firstNode == null ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::setAnyPart",
//        "cannot set the part %s because the sub-part %s cannot be made",
//                                nameOfPart.getString(),firstName );
//#endif
        answer = false;
    }
    // call setAnyPart within firstNode...
    // but first, check that 'firstNode' is derived from SoBaseKit.
    // otherwise, 'setAnyPart' is not a valid method, since it doesn't have
    // parts
    else if ( !firstNode.isOfType( SoBaseKit.getClassTypeId() ) ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::setAnyPart",
//        "you asked for %s but %s is not a nodeKit, so you can not look inside it for %s. returning null",
//        nameOfPart.getString(), firstName, remainderString );
//#endif
        if ( firstExistedBefore[0] == false ) {
            // if we just created the node, we'd better get rid of it...
            setSingleNamePart( new SbName(firstName), null, true );
        }
        answer = false;
    }
    else {
        // call setAnyPart within firstNode...
        // Call the method on the parts list, not the node.
        // It's more efficient and won't do extra virtual things that
        // might be added by subclasses of SoBaseKit.
        SoNodekitParts interParts 
            = ((SoBaseKit )firstNode).nodekitPartsList;
        answer = interParts.setAnyPart(new SbName(remainderString), newPartNode, anyPart );

        if ( answer == false && firstExistedBefore[0] == false ) {
            // if we just created the list, we'd better get rid of it...
            setSingleNamePart( new SbName(firstName), null, true );
        }
    }

    // free ( nameCopy ); java port
    return answer;
}

//
 // Description:
 //    Gets  the part asked for
 //
 // Use: protected
 
protected SoNode 
 getAnyPart( final SbName nameOfPart, boolean makeIfNeeded, 
                             boolean leafCheck, boolean publicCheck )
 //
 {
     boolean[] existedBefore = new boolean[1];
     return (getAnyPart( nameOfPart, makeIfNeeded, leafCheck, publicCheck,
                         existedBefore ) );
 }
 
 //
 // Description:
 //    Gets  the part asked for
 //
 // Use: protected
 
 protected SoNode 
 getAnyPart( final SbName nameOfPart, boolean makeIfNeeded, 
                             boolean leafCheck, boolean publicCheck,
                             final boolean[] existedBefore )
 //
 {
     existedBefore[0] = false;
 
     String  nameCopy, firstName, remainderString;
     SoNode firstNode = null, secondNode = null;
 
     // JUST A SINGLE NAME...
     if ( Util.strrchr( nameOfPart.getString(), '.' ) == null )
         return getSingleNamePart( nameOfPart, makeIfNeeded, leafCheck, 
                                   publicCheck, existedBefore );
 
     // OTHERWISE, A COMPOUND NAME of the form:  part1.part2.part3 etc.
     // copy the string
     nameCopy = strdup( nameOfPart.getString() );
 
     // break string into two parts
     firstName = strtok( nameCopy, ".");   // everything before the first '.'
     remainderString = nameCopy.substring(nameCopy.indexOf('.')+1);//strtok( null, "");  // everything else
 
     // get node for first part
     // intermediate names have to be leaves, so arg3 (leafCheck) is true
     boolean[] firstExistedBefore = new boolean[1];
     firstNode = getSingleNamePart( new SbName(firstName), makeIfNeeded, true, 
                                    publicCheck, firstExistedBefore );
 
     if ( firstNode == null ) {
         secondNode = null;      // can't look any further
     }
     // get node for second part
     // first, check that 'firstNode' is derived from SoBaseKit.
     // otherwise, 'getPart' is not a valid method, since it doesn't have
     // parts
     else if ( !firstNode.isOfType( SoBaseKit.getClassTypeId() ) ) {
// #ifdef DEBUG
//         SoDebugError::post( "SoNodekitParts::getAnyPart",
//         "you asked for %s but %s is not a nodeKit, so you can not look inside it for %s. returning null",
//         nameOfPart.getString(),firstName, remainderString );
// #endif
         if ( firstExistedBefore[0] == false ) {
             // if we just created the node, we'd better get rid of it...
             setSingleNamePart( new SbName(firstName), null, true );
         }
         secondNode = null;
     }
     else {
         // get the second node from within the first...
         // Call the method on the parts list, not the node.
         // It's more efficient and won't do extra virtual things that
         // might be added by subclasses of SoBaseKit.
         SoNodekitParts interParts = ((SoBaseKit)firstNode).nodekitPartsList;
         boolean[] secondExistedBefore = new boolean[1];
         secondNode = interParts.getAnyPart( new SbName(remainderString), makeIfNeeded, 
                                 leafCheck, publicCheck, secondExistedBefore );
         if ( secondNode == null && firstExistedBefore[0] == false ) {
             // if we just created the list, we'd better get rid of it...
             setSingleNamePart( new SbName(firstName), null, true );
         }
         existedBefore[0] = firstExistedBefore[0] && secondExistedBefore[0];
     }
     //free ( nameCopy ); java port
     return secondNode;
 }
 
 //
  // Description:
  //    Gets  path to the part asked for
  //
  // Use: public
  
 public  SoFullPath 
  createPathToAnyPart( final SbName nameOfPart, 
                      boolean makeIfNeeded, boolean leafCheck, 
                      boolean publicCheck )
  //
  {
      boolean[] existedBefore = new boolean[1];
      return ( createPathToAnyPart( nameOfPart, makeIfNeeded, leafCheck,
                                    publicCheck, existedBefore ) );
  }
  
 
 	////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets  path to the part asked for
//
// Use: public

public SoFullPath 
createPathToAnyPart( final SbName nameOfPart, 
                    boolean makeIfNeeded, boolean leafCheck, 
                    boolean publicCheck,  final boolean[] existedBefore )
//
////////////////////////////////////////////////////////////////////////
{
    existedBefore[0] = false;

    String   nameCopy, firstName, remainderString;
    SoNode firstNode = null;
    SoFullPath firstPath = null, secondPath = null, answer = null;

    // JUST A SINGLE NAME...
    if ( Util.strrchr( nameOfPart.getString(), '.' ) == null )
        return getSingleNamePathToPart( nameOfPart, makeIfNeeded, 
                                    leafCheck, publicCheck, existedBefore );

    // OTHERWISE, A COMPOUND NAME of the form:  part1.part2.part3 etc.
    // copy the string
    nameCopy = strdup( nameOfPart.getString() );

    // break string into two parts
    firstName = strtok( nameCopy, ".");   // everything before the first '.'
    remainderString = nameCopy.substring(nameCopy.indexOf('.')+1);//strtok( null, "");  // everything else

    // get node for first part
    // intermediate names have to be leaves, so arg3 (leafCheck) is true
    boolean[] firstExistedBefore = new boolean[1];
    firstPath = getSingleNamePathToPart( new SbName(firstName), makeIfNeeded, true, 
                                         publicCheck, firstExistedBefore );
    if ( firstPath == null ) {
        answer = null;
    }
    else {

        firstPath.ref();

        firstNode = firstPath.getTail();

        if ( firstNode == null ) {
            answer = null;      // can't look any further
        }
        // get node for second part
        // first, check that 'firstNode' is derived from SoBaseKit.
        // otherwise, 'getPart' is not a valid method, since it doesn't have
        // parts
        else if ( !firstNode.isOfType( SoBaseKit.getClassTypeId() ) ) {
//#ifdef DEBUG
//            SoDebugError::post( "SoNodekitParts::createPathToAnyPart",
//            "you asked for %s but %s is not a nodeKit, so you can not look inside it for %s. returning null",
//            nameOfPart.getString(), firstName, remainderString );
//#endif
            if ( firstExistedBefore[0] == false ) {
                // if we just created the node, we'd better get rid of it...
                setSingleNamePart( new SbName(firstName), null, true );
            }
            answer = null;
        }
        else {
            // next, build a path from the second node to the end...
            // Call the method on the parts list, not the node.
            // It's more efficient and won't do extra virtual things that
            // might be added by subclasses of SoBaseKit.
            SoNodekitParts interParts 
                = ((SoBaseKit )firstNode).nodekitPartsList;
            boolean[] secondExistedBefore = new boolean[1];
            secondPath = SoFullPath.cast(
                interParts.createPathToAnyPart( new SbName(remainderString), makeIfNeeded,
                                leafCheck, publicCheck, secondExistedBefore ));
            if ( secondPath != null)
                secondPath.ref();
            answer = addPaths( firstPath, secondPath );
            if ( secondPath == null && firstExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(firstName), null, true );
            }
            existedBefore[0] = firstExistedBefore[0] && secondExistedBefore[0];
        }
    }
    // get rid of paths...
    if ( firstPath != null)
        firstPath.unref();
    if ( secondPath != null)
        secondPath.unref();
    // free ( nameCopy ); java port
    return answer;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called when 'partNum' is being set to a new value using replacePart.
//
//    Makes sure that all descendant parts of 'partNum' get NULLed out,
//    and is called prior to installing the new node value for 
//    'partNum'.
//
//    This is a dangerous and special purpose routine.
//
// Use: internal

private void
unregisterDescendants( final int partNum )
//
////////////////////////////////////////////////////////////////////////
{
    // For each part in the catalog...
    numEntries = catalog.getNumEntries();
    for ( int i = 0; i < numEntries; i++ ) {
        // If it's parent is partNum...
        if ( catalog.getParentPartNumber(i) == partNum ) {
            unregisterDescendants( i );
            // Don't need to worry about i being the index of 'this'.
            // since 'this' has no parent part number to match.
            fieldList[ i ].setValue( null );
        }
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets  the part asked for
//    Note:
//       this routine will NOT search for the requested part name within the
//       catalogs of its child parts.
//
// Use: private

private SoNode 
getPartFromThisCatalog( int partNum, 
                                  boolean makeIfNeeded, boolean leafCheck, 
                                  boolean publicCheck, boolean[] existedBefore )
//
////////////////////////////////////////////////////////////////////////
{
    existedBefore[0] = false;

    if ( !partFoundCheck( partNum ) )
        return null;

    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM  ||
         fieldList[partNum].getValue() != null )
        existedBefore[0] = true;

    if ( leafCheck ) {
        if ( !partIsLeafCheck( partNum ) )
            return null;
    }
    if ( publicCheck ) {
        if ( !partIsPublicCheck( partNum ) )
            return null;
    }

    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
        return rootPointer;

    // Return if part already exists...
    if ( verifyPartExistence(partNum) )
        return ( fieldList[ partNum ].getValue() );

    // Part doesn't exist yet.  Either return...
    if ( makeIfNeeded == false )
        return null;

    // or make the part
    if ( makePart( partNum ) == true )
        return ( fieldList[ partNum ].getValue() );   // it was made O.K.
    else
        return null;                      // it didn't get made properly
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets  path to the part asked for
//    Note:
//       this routine will NOT search for the requested part name within the
//       catalogs of its child parts.
//
// Use: private

private SoFullPath 
createPathToPartFromThisCatalog( int partNum, 
                    boolean makeIfNeeded, boolean leafCheck, 
                    boolean publicCheck,  boolean[] existedBefore )
//
////////////////////////////////////////////////////////////////////////
{
    SoNode n = getPartFromThisCatalog( partNum, makeIfNeeded, 
                                        leafCheck, publicCheck, existedBefore );

    return( createPathDownTo( catalog.getName(partNum), n ));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets  a path to the part asked for
//    Assumes that 'nameOfPart' does not need to be parsed into
//    names separated by '.' 
//
// Use: public

public SoFullPath 
getSingleNamePathToPart(final  SbName nameOfPart, 
                                boolean makeIfNeeded, boolean leafCheck, 
                                boolean publicCheck,  boolean[] existedBefore )
//
////////////////////////////////////////////////////////////////////////
{
    existedBefore[0] = false;

    SoFullPath answerPath = null;

    // IS THERE A BRACKET, WHICH SIGNIFIES INDEXING INTO A LIST?
    if ( Util.strrchr( nameOfPart.getString(), '[') != null ) {

        final String[] listNameCopy = new String[1]; listNameCopy[0] = strdup( nameOfPart.getString());
        int[]  arrayIndex = new int[1];

        if (!parseListItem( listNameCopy, arrayIndex)){
            //free(listNameCopy); java port
            return null;
        }

        // get the list given by 'listNameCopy'
        boolean[] listExistedBefore = new boolean[1];
        answerPath = getSingleNamePathToPart( new SbName(listNameCopy[0]), makeIfNeeded, 
                                      true, publicCheck, listExistedBefore );
        if (answerPath == null){
            //free(listNameCopy); java port
            return null;
        }

        answerPath.ref();

        if (!answerPath.getTail().isOfType(
                                    SoNodeKitListPart.getClassTypeId()))  {
//#ifdef DEBUG
//              SoDebugError::post("SoNodekitParts::getSingleNamePathToPart",
//              "list named %s is not actually a list part\n", listNameCopy );
//#endif
                answerPath.unref();
                if ( listExistedBefore[0] == false ) {
                    // if we just created the list, we'd better get rid of it...
                    setSingleNamePart( new SbName(listNameCopy[0]), null, true );
                }
                //free(listNameCopy); java port
                return null;
        }

        SoNodeKitListPart listGroup 
            = (SoNodeKitListPart ) answerPath.getTail();

        // If they are asking for the next child in the list, 
        // try to make it for them.  return this new node if successful.
        if ((listGroup.getNumChildren() == arrayIndex[0]) && makeIfNeeded ) {
            SoNode newNode = null;
            SoTypeList itemTypesList = listGroup.getChildTypes();
            for ( int i = 0; i < itemTypesList.getLength(); i++ ) {

                // Can this child type create an instance?
                // (and is it a type of node?)

                if ( itemTypesList.operator_square_bracket(i).canCreateInstance() && 
                     itemTypesList.operator_square_bracket(i).isDerivedFrom( SoNode.getClassTypeId())){

                    // Create an instance, and add it to the listGroup...
                    newNode = (SoNode ) itemTypesList.operator_square_bracket(i).createInstance();
                    listGroup.addChild( newNode );

                    // Now we extend the path to our new node.
                    // First, append '0' for the container node.
                    // Second, append the given index within the container node.
                    answerPath.append( 0 );
                    answerPath.append( arrayIndex[0] );
                    answerPath.unrefNoDelete();
                    break;
                }
            }
            if (newNode == null ) {
//#ifdef DEBUG
//                if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//                    fprintf(stderr,"\n");
//                    fprintf(stderr,"NODE KIT PARTS DEBUG:\n");
//                    fprintf(stderr,"    The part %s does not exist\n",
//                                        nameOfPart.getString());
//                    fprintf(stderr,"    and could not be created.The list %s\n",
//                                            listNameCopy );
//                    fprintf(stderr,"    only specifies abstract types\n");
//                    fprintf(stderr,"    Returning null\n");
//                    fprintf(stderr,"\n");
//                }
//#endif
                answerPath.unref();
                answerPath = null;
            }
            //free( listNameCopy); java port
            return answerPath;
        }

        // Return if child doesn't exist yet, and we were not able to
        // create one for them.
        if ( listGroup.getNumChildren() <= arrayIndex[0] ) {
            if ( makeIfNeeded ) {
//#ifdef DEBUG
//            if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//            fprintf(stderr,"\n");
//            fprintf(stderr,"NODE KIT PARTS DEBUG: Could not return the part\n");
//            fprintf(stderr,"    %s. The list %s only has\n", 
//                                nameOfPart.getString(), listNameCopy );
//            fprintf(stderr,"    %d entries, but you want entry number %d\n",
//                                listGroup.getNumChildren(), arrayIndex);
//            fprintf(stderr,"    Returning null\n");
//            fprintf(stderr,"\n");
//            }
//#endif
            }
            answerPath.unref();
            if ( listExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(listNameCopy[0]), null, true );
            }
            //free(listNameCopy); java port
            return null;
        }

        // we have path to list, so append to it:
        // First, append '0' for the container node.
        // Second, append the given index within the container node.
        answerPath.append( 0 );
        answerPath.append( arrayIndex[0] );
        answerPath.unrefNoDelete();
        existedBefore[0] = true;
        //free(listNameCopy); java port
        return answerPath;
    }

    // NOT A LIST ITEM, IF WE GOT HERE.

    // IS THE REQUESTED PART IN THIS CATALOG?
    int partNum = catalog.getPartNumber( nameOfPart );
    if ( partNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND )

        // IF SO, THEN GET IT FROM THIS CATALOG
        return ( createPathToPartFromThisCatalog( partNum, makeIfNeeded,
                                 leafCheck, publicCheck, existedBefore ) );

    else {

        // ELSE, SEARCH THE CATALOG RECURSIVELY FOR THE DESIRED PART
        // we need to pass a list to the recursive search saying which
        // types of nodes we have already checked.  This avoids infinite
        // loop (does chicken contain egg? does egg contain chicken? etc...)
        SoTypeList typesChecked = new SoTypeList();
        typesChecked.append( catalog.getType(SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM) );

        for (int i = 0; i < numEntries; i++ ) {

            // does it lie within this 'intermediary' part?
            if ( catalog.recursiveSearch(i,nameOfPart,typesChecked) == true){

                //delete typesChecked;  // don't need this anymore
            	try {
					typesChecked.destructor();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} typesChecked = null; // java port

                // if not making parts and 'intermediary' is null...
                if ( makeIfNeeded == false && !verifyPartExistence( i ) )
                    return null;

                boolean kitExistedBefore = (fieldList[i].getValue() != null);

                // create the intermediary part...
                final SbName interName = catalog.getName(i);
                // Turn off notification while building the parent part.
                // We'll be notifying when adding the lower part, so let's not
                // notify twice.
                boolean wasEn = rootPointer.enableNotify(false);
                boolean madeOk = makePart( i );
                rootPointer.enableNotify(wasEn);
                if ( ! madeOk )
                    return null;   // error making the part
                // NOTE: it's okay to cast the node into a base kit here,
                // since the recursive search would only have worked
                // if it was a nodekit
                // UGLY, yes, but it works.
//#ifdef DEBUG
//                // supposedly unnecessary type checking:
//                if ( !fieldList[i].getValue().isOfType( 
//                                        SoBaseKit::getClassTypeId() ) )
//                    return null;
//#endif
                SoBaseKit intermediary =(SoBaseKit )fieldList[i].getValue(); 

                // first, build a path from 'this' to 'intermediary'
                SoFullPath  pathA = createPathDownTo( interName, intermediary);

                // next, build a path from 'intermediary' to the end...
                // Call the method on the parts list, not the node.
                // It's more efficient and won't do extra virtual things that
                // might be added by subclasses of SoBaseKit.
                SoNodekitParts interParts = intermediary.nodekitPartsList;
                final boolean[] secondExistedBefore = new boolean[1];
                SoFullPath pathB = 
                    SoFullPath.cast ( interParts.createPathToAnyPart(nameOfPart,
                                makeIfNeeded, leafCheck, 
                                publicCheck, secondExistedBefore ));
                if (pathA != null)
                    pathA.ref();
                if (pathB != null)
                    pathB.ref();

                if ( pathB == null && kitExistedBefore == false ) {
                    // if we just created the kit, we'd better get rid of it...
                    replacePart( i, null );
                }
                existedBefore[0] = kitExistedBefore && secondExistedBefore[0];

                // tack pathB onto pathA and return it
                SoFullPath   totalPath = addPaths( pathA, pathB );

                if (pathA != null)
                    pathA.unref();
                if (pathB != null)
                    pathB.unref();

                return( totalPath );
            }
        }
        //delete typesChecked;  // don't need this anymore
        try {
			typesChecked.destructor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} typesChecked = null; // java port
    }

    // IF YOU GOT HERE, PARTNAME WAS NOT FOUND IN THE CATALOG OR ANY
    // OF THE SUB CATALOGS.
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//        fprintf(stderr,"\n");
//        fprintf(stderr,"NODE KIT PARTS DEBUG: The catalog for this class\n");
//        fprintf(stderr,"    of nodekit does not have a part named %s, \n",
//                            nameOfPart.getString() );
//        fprintf(stderr,"    nor do the catalogs for any of nested nodekits\n");
//        fprintf(stderr,"    Returning null\n");
//        fprintf(stderr,"\n");
//    }
//#endif
    return null;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets  the part given
//    Assumes that 'nameOfPart' does not need to be parsed into
//    names separated by '.' 
//
// Use: public

private boolean setSingleNamePart(SbName nameOfPart, SoNode newPartNode, boolean anyPart)
	
//
////////////////////////////////////////////////////////////////////////
{
    // IS THERE A BRACKET, WHICH SIGNIFIES INDEXING INTO A LIST?
    if ( Util.strrchr( nameOfPart.getString(), '[') != null ) {
        final String[] listNameCopy = new String[1]; listNameCopy[0] = strdup( nameOfPart.getString());
        int[]  arrayIndex = new int[1];

        if (!parseListItem( listNameCopy, arrayIndex )){
            //free(listNameCopy); java port
            return false;
        }

        // get the list given by 'listNameCopy'
        // Only need to create it if (newPartNode != null)
        // This is because, if new part is to be null, then the
        // list containing it need not exist either.
        boolean needToMake = ( newPartNode != null);
        final boolean[] listExistedBefore = new boolean[1];
        SoNode n = getSingleNamePart( new SbName(listNameCopy[0]), needToMake, true, 
                                       !anyPart, listExistedBefore );
        if ( n == null && needToMake == false ) {
            //free(listNameCopy); java port
            return true;
        }
        if ( n == null && needToMake == true ) {
            //free(listNameCopy); java port
            return false;
        }

        if ( !n.isOfType( SoNodeKitListPart.getClassTypeId() ) ){
//#ifdef DEBUG
//            SoDebugError::post("SoNodekitParts::setSingleNamePart",
//            "part specified %s was not a list", listNameCopy );
//#endif
            if ( listExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(listNameCopy[0]), null, true );
            }
            //free(listNameCopy); java port
            return false;
        }

        SoNodeKitListPart listGroup = (SoNodeKitListPart ) n;

        // handle case of setting to null
        if ( newPartNode == null ) {
            // remove if you can...
            if ( arrayIndex[0] >= 0 && listGroup.getNumChildren() > arrayIndex[0] )  
                 listGroup.removeChild( arrayIndex[0] );
            else if ( listExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(listNameCopy[0]), null, true );
            }
            //free(listNameCopy); java port
            return true;
        }

        // check type...
        SoTypeList itemTypesList = listGroup.getChildTypes();
        for ( int i = 0; i < itemTypesList.getLength(); i++ ) {
            if ( newPartNode.isOfType( itemTypesList.operator_square_bracket(i) ) ) {
                // Set the node...
                if ( arrayIndex[0] == listGroup.getNumChildren() )
                    // Create a new final entry, if a new index...
                    listGroup.insertChild(newPartNode, arrayIndex[0] );
                else if ( arrayIndex[0] < listGroup.getNumChildren() )
                    // Or, replace if it's not a new index...
                    listGroup.replaceChild(arrayIndex[0], newPartNode );
//#ifdef DEBUG
//                else if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//                    fprintf(stderr,"\n");
//                    fprintf(stderr,"NODE KIT PARTS DEBUG:\n");
//                    fprintf(stderr,"    The part %s could not be installed\n",
//                                        nameOfPart.getString());
//                    fprintf(stderr,"    because the index %d is greater \n",
//                                        arrayIndex);
//                    fprintf(stderr,"    than %d, the number of entries in \n",
//                                        listGroup.getNumChildren());
//                    fprintf(stderr,"    the list %s\n",
//                                            listNameCopy );
//                    fprintf(stderr,"    Returning null\n");
//                    fprintf(stderr,"\n");
//                }
//#endif
                //free(listNameCopy); java port
                return true;
            }
        }
        // child was not good for the list...
//#ifdef DEBUG
//        SoDebugError::post("SoNodekitParts::setSingleNamePart",
//        "Node given is wrong type. Its type is %s, which is inappropriate for list named %s",
//                 newPartNode.getTypeId().getName().getString(), listNameCopy );
//#endif
        if ( listExistedBefore[0] == false ) {
            // if we just created the list, we'd better get rid of it...
            setSingleNamePart( new SbName(listNameCopy[0]), null, true );
        }
        //free(listNameCopy); java port
        return false;
    }

    // NOT A LIST ITEM, IF WE GOT HERE.

    // IS THE REQUESTED PART IN THIS CATALOG?
    int partNum = catalog.getPartNumber( nameOfPart );
    if ( partNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND )

        // IF SO, THEN SET IT USING THIS CATALOG
        return ( setPartFromThisCatalog( partNum, newPartNode, anyPart ) );

    else {

        // ELSE, SEARCH THE CATALOG RECURSIVELY FOR THE DESIRED PART
        // we need to pass a list to the recursive search saying which
        // types of nodes we have already checked.  This avoids infinite
        // loop (does chicken contain egg? does egg contain chicken? etc...)
        SoTypeList typesChecked = new SoTypeList();
        typesChecked.append( catalog.getType(SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM));

        for (int i = 0; i < numEntries; i++ ) {

            // does it lie within this 'intermediary' part?
            if ( catalog.recursiveSearch(i,nameOfPart,typesChecked) == true){

                //delete typesChecked;  // don't need this anymore
            	try {
					typesChecked.destructor();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} typesChecked = null; // java port

                // If the part is not yet made, and the newPartNode is null,
                // we can just return. The part we seek to set to null
                // does not exist.
                if ( newPartNode == null   && !verifyPartExistence( i ) )
                    return true;

                boolean kitExistedBefore = (fieldList[i].getValue() != null);

                // create the intermediary part...
                // Turn off notification while building the parent part.
                // We'll be notifying when adding the lower part, so let's not
                // notify twice.
                boolean wasEn = rootPointer.enableNotify(false);
                boolean madeOk = makePart( i );
                rootPointer.enableNotify(wasEn);
                if ( ! madeOk )
                    return false;   // error making the part
                // NOTE: it's okay to cast the node into a base kit here,
                // since the recursive search would only have worked
                // if it was a nodekit
                // UGLY, yes, but it works.
//#ifdef DEBUG
//                // supposedly unnecessary type checking:
//                if ( !fieldList[i].getValue().isOfType( 
//                                            SoBaseKit::getClassTypeId() ) )
//                    return false;
//#endif
                SoBaseKit intermediary =(SoBaseKit )fieldList[i].getValue(); 

                // now that intermediary is built, set the part within it
                // Now that intermediary is built, set the part within it.
                // Call the method on the parts list, not the node.
                // It's more efficient and won't do extra virtual things that
                // might be added by subclasses of SoBaseKit.
                SoNodekitParts interParts = intermediary.nodekitPartsList;
                boolean didIt = interParts.setAnyPart(nameOfPart, newPartNode, 
                                                anyPart );
                if (didIt == false && kitExistedBefore == false ) {
                    // if we just created the kit, we'd better get rid of it...
                    replacePart( i, null );
                }
                return didIt;
            }
        }
        //delete typesChecked;  // don't need this anymore
        try {
			typesChecked.destructor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // java port
    }

    // IF YOU GOT HERE, PARTNAME WAS NOT FOUND IN THE CATALOG OR ANY
    // OF THE SUB CATALOGS.
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//        fprintf(stderr,"\n");
//        fprintf(stderr,"NODE KIT PARTS DEBUG: The catalog for this class\n");
//        fprintf(stderr,"    of nodekit does not have a part named %s, \n",
//                            nameOfPart.getString() );
//        fprintf(stderr,"    nor do the catalogs for any of nested nodekits\n");
//        fprintf(stderr,"    Returning null\n");
//        fprintf(stderr,"\n");
//    }
//#endif
    return false;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets  the part given by nameOfPart
//    Note:
//       this routine will NOT search for the requested part name within the
//       catalogs of its child parts.
//
// Use: private

private boolean
setPartFromThisCatalog( final int partNum, 
                                         SoNode newPartNode, boolean anyPart )
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM ) {
        SoDebugError.post("SoNodekitParts::setPartFromThisCatalog",
               "You can not never set the part \"this\" after construction");
        return false;
    }
//#endif

    if ( !partFoundCheck( partNum ) )
        return false;
    if ( !anyPart ) {
        if ( !partIsLeafCheck( partNum ) )
            return false;
        if ( !partIsPublicCheck( partNum ) )
            return false;
    }

    // otherwise, we need to replace the part that is currently being used
    return( replacePart( partNum, newPartNode ) );
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets  the part asked for
//    Assumes that 'nameOfPart' does not need to be parsed into
//    names separated by '.' 
//
// Use: public

public SoNode 
getSingleNamePart( final SbName nameOfPart, 
                    boolean makeIfNeeded, boolean leafCheck, boolean publicCheck,
                    boolean[] existedBefore )
//
////////////////////////////////////////////////////////////////////////
{
    existedBefore[0] = false;

    // IS THERE A BRACKET, WHICH SIGNIFIES INDEXING INTO A LIST?
    if ( Util.strrchr( nameOfPart.getString(), '[') != null ) {

        final String[] listNameCopy = new String[1]; listNameCopy[0] = strdup( nameOfPart.getString());
        int[]  arrayIndex = new int[1];

        if ( !parseListItem( listNameCopy, arrayIndex)) {
            //free ( listNameCopy ); java port
            return null;
        }

        // get the list given by 'listNameCopy'
        boolean[] listExistedBefore = new boolean[1];
        SoNode n = getSingleNamePart( new SbName(listNameCopy[0]), makeIfNeeded, true, 
                                        publicCheck, listExistedBefore );
        if ( n == null ) {
            //free ( listNameCopy ); java port
            return null;
        }

        if ( !n.isOfType( SoNodeKitListPart.getClassTypeId() ) ){
//#ifdef DEBUG
//            SoDebugError::post("SoNodekitParts::getSingleNamePart",
//                               "part specified %s was not a list", listNameCopy );
//#endif
            if ( listExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(listNameCopy[0]), null, true );
            }
            //free( listNameCopy); java port
            return null;
        }

        SoNodeKitListPart listGroup = (SoNodeKitListPart ) n;

        // If they are asking for the next child in the list, 
        // try to make it for them.  return this new node if successful.
        if ((listGroup.getNumChildren() == arrayIndex[0]) && makeIfNeeded ) {
            SoNode newNode = null;
            SoTypeList itemTypesList = listGroup.getChildTypes();
            for ( int i = 0; i < itemTypesList.getLength(); i++ ) {

                // Can this child type create an instance?
                // (and is it a type of node?)

                if ( itemTypesList.operator_square_bracket(i).canCreateInstance() && 
                     itemTypesList.operator_square_bracket(i).isDerivedFrom( SoNode.getClassTypeId())){

                    // Create an instance, and add it to the listGroup...
                    newNode = (SoNode ) itemTypesList.operator_square_bracket(i).createInstance();
                    listGroup.addChild( newNode );
                    break;
                }
            }
            if (newNode == null ) {
//#ifdef DEBUG
//                if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//                    fprintf(stderr,"\n");
//                    fprintf(stderr,"NODE KIT PARTS DEBUG:\n");
//                    fprintf(stderr,"    The part %s does not exist\n",
//                                        nameOfPart.getString());
//                    fprintf(stderr,"    and could not be created.The list %s\n",
//                                            listNameCopy );
//                    fprintf(stderr,"    only specifies abstract types\n");
//                    fprintf(stderr,"    Returning null\n");
//                    fprintf(stderr,"\n");
//                }
//#endif
            }
            //free( listNameCopy); java port
            return( newNode );
        }

        // Return if child doesn't exist yet, and we were not able to
        // create one for them.
        if ( listGroup.getNumChildren() <= arrayIndex[0] ) {
            if ( makeIfNeeded ) {
//#ifdef DEBUG
//                if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//                    fprintf(stderr,"\n");
//                    fprintf(stderr,"NODE KIT PARTS DEBUG:\n");
//                    fprintf(stderr,"    Could not return the part\n");
//                    fprintf(stderr,"    %s. The list %s only has\n", 
//                                        nameOfPart.getString(), listNameCopy );
//                    fprintf(stderr,"    %d entries. You want entry number %d\n",
//                                        listGroup.getNumChildren(),arrayIndex);
//                    fprintf(stderr,"    Returning null\n");
//                    fprintf(stderr,"\n");
//                }
//#endif
            }
            if ( listExistedBefore[0] == false ) {
                // if we just created the list, we'd better get rid of it...
                setSingleNamePart( new SbName(listNameCopy[0]), null, true );
            }
            //free( listNameCopy); java port
            return null;
        }

        // return entry number 'arrayIndex'
        existedBefore[0] = true;
        //free( listNameCopy); java port
        return( listGroup.getChild( arrayIndex[0] ) );
    }

    // NOT A LIST ITEM, IF WE GOT HERE.

    // IS THE REQUESTED PART IN THIS CATALOG?
    int partNum = catalog.getPartNumber( nameOfPart );
    if ( partNum != SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND )

        // IF SO, THEN GET IT FROM THIS CATALOG
        return ( getPartFromThisCatalog( partNum, makeIfNeeded,leafCheck, 
                                            publicCheck, existedBefore ));
    else {

        // ELSE, SEARCH THE CATALOG RECURSIVELY FOR THE DESIRED PART
        // we need to pass a list to the recursive search saying which
        // types of nodes we have already checked.  This avoids infinite
        // loop (does chicken contain egg? does egg contain chicken? etc...)
        SoTypeList typesChecked = new SoTypeList();
        typesChecked.append( catalog.getType(SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM) );

        for (int i = 0; i < numEntries; i++ ) {

            // does it lie within this 'intermediary' part?
            if ( catalog.recursiveSearch(i,nameOfPart,typesChecked) == true) {

                //delete typesChecked;  // don't need this anymore
            	try {
					typesChecked.destructor();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} typesChecked = null;

                // if not making parts and 'intermediary' is null...
                if ( makeIfNeeded == false && !verifyPartExistence( i ) )
                    return null;

                boolean kitExistedBefore = (fieldList[i].getValue() != null);

                // create the intermediary part...
                // Turn off notification while building the parent part.
                // We'll be notifying when adding the lower part, so let's not
                // notify twice.
                boolean wasEn = rootPointer.enableNotify(false);
                boolean madeOk = makePart( i );
                rootPointer.enableNotify(wasEn);
                if ( ! madeOk )
                    return null;   // error making the part
                // NOTE: it's okay to cast the node into a base kit here,
                // since the recursive search would only have worked
                // if it was a nodekit
                // UGLY, yes, but it works.
//#ifdef DEBUG
//                // supposedly unnecessary type checking:
//                if ( !fieldList[i].getValue().isOfType( 
//                                        SoBaseKit::getClassTypeId() ) )
//                    return null;
//#endif
                SoBaseKit intermediary =(SoBaseKit )fieldList[i].getValue(); 

                // Now that intermediary is built, get the part from within it
                // Call the method on the parts list, not the node.
                // It's more efficient and won't do extra virtual things that
                // might be added by subclasses of SoBaseKit.
                SoNodekitParts interParts = intermediary.nodekitPartsList;
                final boolean[] answerExistedBefore = new boolean[1];
                SoNode answer = interParts.getAnyPart(nameOfPart,makeIfNeeded,
                                leafCheck, publicCheck, answerExistedBefore );
                if (answer == null && kitExistedBefore == false ) {
                    // if we just created the kit, we'd better get rid of it...
                    replacePart( i, null );
                }
                existedBefore[0] = kitExistedBefore && answerExistedBefore[0];
                return answer;
            }
        }
        //delete typesChecked;  // don't need this anymore
        try {
			typesChecked.destructor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} typesChecked = null; // java port
    }

    // IF YOU GOT HERE, PARTNAME WAS NOT FOUND IN THE CATALOG OR ANY
    // OF THE SUB CATALOGS.
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_KIT_PARTS")) {
//        fprintf(stderr,"\n");
//        fprintf(stderr,"NODE KIT PARTS DEBUG: The catalog for this class\n");
//        fprintf(stderr,"    of nodekit does not have a part named %s, \n",
//                            nameOfPart.getString() );
//        fprintf(stderr,"    nor do the catalogs for any of nested nodekits\n");
//        fprintf(stderr,"    Returning null\n");
//        fprintf(stderr,"\n");
//    }
//#endif
    return null;
}



 //
 // Description:
 //    Builds list from 'this' down to 'theNode'
 //    Note:
 //       Assumes that 'nameOfPart' 'partNum', and 'theNode' have already
 //       been determined to correctly correlate within this partsList
 //
 // Use: private
 
private SoFullPath 
 createPathDownTo( SbName nameOfPart, SoNode theNode )
 //
 {
     if ( theNode == null )
         return null;
 
     // construct a path from 'this' to the given part.
     SoNode    childNode = null;
     SoGroup   parentNode = null;
     SoBaseKit thisNode = null;
     int     childPartNum, parentPartNum, count;
     int[]     backwardsKidIndexArray;
 
     // allocate enough room to hold all the entries...
     backwardsKidIndexArray = new int[numEntries];
 
     thisNode      = rootPointer;
     childPartNum  = catalog.getPartNumber( nameOfPart );
     childNode     = theNode;
 
     // go backwards up the catalog to the top, saving the child index
     // at each step of the way.
     for ( count = 0; childNode != thisNode; count++ ) {
         parentPartNum = catalog.getParentPartNumber( childPartNum );
         if ( parentPartNum != SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM ) {
             parentNode = (SoGroup ) fieldList[ parentPartNum ].getValue();
             if ( !parentNode.isOfType( SoGroup.getClassTypeId() ) ) {
// #ifdef DEBUG
//                 SoDebugError::post("NodekitParts::createPathDownTo",
//                 "Parent part not derived from a group");
// #endif
                 return null;
             }
             backwardsKidIndexArray[ count ] = parentNode.findChild( childNode);
             childPartNum = parentPartNum;
             childNode = parentNode;
         }
         else {
             backwardsKidIndexArray[ count ] = thisNode.findChild( childNode );
             childNode = thisNode;
         }
     }
 
     SoFullPath answer = SoFullPath.cast ( new SoPath( rootPointer ));
     answer.ref();
     for( int i = count - 1; i >= 0; i-- ) {
         answer.append( backwardsKidIndexArray[i] );
     }
 
     // delete [ /*numEntries*/ ] backwardsKidIndexArray; java port
     answer.unrefNoDelete();
     return answer;
 }
 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Appends pathB after pathA and returns the result.
//    Returns null if either path is null, or if
//    pathA.getTail() != pathB.getHead()
//
// Use: private

private SoFullPath 
addPaths( final SoFullPath pathA, 
                          final SoFullPath pathB )
//
////////////////////////////////////////////////////////////////////////
{
    if ( pathA == null )
        return null;

    if ( pathB == null )
        return null;

    if ( pathA.getTail() != pathB.getHead() )
        return null;

    // copy pathA into answer
    SoFullPath answer = SoFullPath.cast ( pathA.copy());
    answer.ref();

    // append entries in pathB, but leave out the head, since it already
    // matches the tail of pathA
    for( int i = 1; i < pathB.getLength(); i++ ) {
        answer.append( pathB.getIndex( i ) );
    }
    answer.unrefNoDelete();
    return answer;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    parses the given string into a list name and an index.
//    destructive to parseString, so give it a copy.
//    Syntax for string is:   
//               nameOfList[elementNum]
//    Returns true if successful, false if not.
//
// Use: private

private boolean
parseListItem( final String[] parseString, int[] index ) 
//
////////////////////////////////////////////////////////////////////////
{
    int indexStart = parseString[0].indexOf('[');
    int indexStop = parseString[0].indexOf(']');
    
    if ( indexStart == -1 || indexStop == -1 || indexStart>indexStop ) {
//#ifdef DEBUG
//        SoDebugError::post("SoNodekitParts::parseListItem",
//                           "Can\'t parse the name: %s", parseString );
//#endif
        return false;
    }
    int beginIndex = indexStart + 1;
    int endIndex = indexStop;
    String elementNum = parseString[0].substring(beginIndex, endIndex);
    index[0] = Integer.parseInt(elementNum);    // read index from string
    parseString[0] = parseString[0].substring(0, indexStart); // java port
    return true;
}

 	private static String strdup(String str) {
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
//    Determines if partNum is for a non-leaf part that can be deleted.
//    This will be true if the part is:
//     [a] an actual part.
//     [b] not "this"
//     [c] not a leaf.
//     [d] non-null
//     [e1] an SoGroup with no children 
//     OR 
//     [e2] an SoSeparator with no children and default field values.
//
private boolean
partIsNonLeafAndMayBeDeleted( int partNum )
{
    // [a] an actual part.
    if ( partNum == SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND )
        return false;

    // [b] not "this"
    if (partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM)
        return false;

    // [c] not a leaf.
    if ( catalog.isLeaf( partNum ) == true )
        return false;

    // [d] non-null
    SoNode  part = fieldList[partNum].getValue();
    if (  part == null )
        return false;

    // [e1] an SoGroup with no children 
    if ( Objects.equals(part.getTypeId(), SoGroup.getClassTypeId()) ) {
        if ( ((SoGroup )part).getNumChildren() == 0 )
            return true;
        else
            return false;
    }

    // OR 
    // [e2] an SoSeparator with no children and default field values.
    if ( Objects.equals(part.getTypeId(), SoSeparator.getClassTypeId())) {
        SoSeparator sep = (SoSeparator ) part;
        if ( sep.getNumChildren() != 0 )
            return false;
        if ( ! sep.renderCaching.isDefault() &&
               sep.renderCaching.getValue() != SoSeparator.CacheEnabled.AUTO.getValue() )
            return false;
        if ( ! sep.boundingBoxCaching.isDefault() &&
               sep.boundingBoxCaching.getValue() != SoSeparator.CacheEnabled.AUTO.getValue() )
            return false;
        if ( ! sep.renderCulling.isDefault() &&
               sep.renderCulling.getValue() != SoSeparator.CacheEnabled.AUTO.getValue() )
            return false;
        if ( ! sep.pickCulling.isDefault() &&
               sep.pickCulling.getValue() != SoSeparator.CacheEnabled.AUTO.getValue() )
            return false;

        // Sep has no children and default values, so go ahead and delete.
        return true;
    }

    // If we got here, it's neither a group nor a separator.
    return false;
}

 	 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    prints error if partNum is not legal
//
public boolean
partFoundCheck( int partNum )
{
    if ( partNum == SoNodekitCatalog.SO_CATALOG_NAME_NOT_FOUND ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::partFoundCheck",
//                            "Can\'t find part");
//#endif
        return false;
    }
    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    prints error if partNum is not a leaf
//
private boolean
partIsLeafCheck( int partNum )
{
    if ( catalog.isLeaf( partNum ) == false ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::partIsLeafCheck",
//        "can't return the part %s because it is not a leaf node in the nodekit\'s structure. Returning null instead",
//                           catalog.getName( partNum ).getString() );
//#endif
        return false;
    }
    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    prints error if partNum is not public
//
private boolean
partIsPublicCheck( int partNum )
{
    if ( catalog.isPublic( partNum ) == false ) {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::partIsPublicCheck",
//        "can\'t return the part %s because it is not a public node in the nodekit's structure returning null instead",
//                           catalog.getName( partNum ).getString() );
//#endif
        return false;
    }
    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This checks that a part still exists as the child of its parent,
//    and so on up to 'this'
//    It needs to be called as protection against outside parties deciding to
//    delete nodes in the kit.
//    If the integrity of the kit is broken, routines which call
//    'checkPartIntegrity' will know to create new parts instead, or
//    perhaps return null.
//
private boolean
verifyPartExistence( int partNum )
{
    if (partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM) {
        if ( rootPointer != null)
            return true;
        else {
//#ifdef DEBUG
//        SoDebugError::post( "SoNodekitParts::verifyPartExistence",
//        "the part \"this\" is null, and should NEVER be. Expect a core dump");
//#endif
            return false;
        }
    }

    SoNode  part = fieldList[partNum].getValue();

    // is the part there?
    if ( part == null )
        return false;

    // is the part the top of the catalog? Then we're OK
    if ( partNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM )
        return true;
    
    int parentPartNum = catalog.getParentPartNumber( partNum );
    SoNode parent = null;
    if (parentPartNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM)
        parent = rootPointer;
    else
        parent = fieldList[parentPartNum].getValue();

    // parent should exist.
    if ( parent == null )
        return false;

    // part should be a valid child of parent
    if ( parentPartNum != SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM &&   
         ((SoGroup )parent).findChild( part ) < 0 )
            return false;
    else if ( parentPartNum == SoNodekitCatalog.SO_CATALOG_THIS_PART_NUM  
              && ((SoBaseKit )parent).findChild( part ) < 0 )
            return false;

    // verify the parent
    return( verifyPartExistence( parentPartNum ) );
}

 }

