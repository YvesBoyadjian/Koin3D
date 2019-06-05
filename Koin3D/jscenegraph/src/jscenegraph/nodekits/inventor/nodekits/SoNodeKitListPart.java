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
 |      Defines the SoNodeKitListPart class. 
 |      It behaves sort of like a group to the outsider, but it only allows
 |      certain types of nodes to be put beneath it.
 | 
 |      It contains hidden children, and is not derived from SoGroup.
 |      The addChild(), insertChild(), etc. methods all check that the
 |      new node is allowable beneath it.
 |
 |      The container node sits beteen the SoNodeKitListPart and the children.
 |      It's type is taken in the constructor, and must be derived from 
 |      SoGroup. By making the container an SoSeparator, SoSwitch, etc.,
 |      the SoNodeKitList can be made to encase its set of children in
 |      the appropriate behavior.
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import java.util.Objects;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.SoTypeList;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFName;
import jscenegraph.database.inventor.fields.SoSFName;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoSwitch;


/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Group node with restricted children.
/*!
\class SoNodeKitListPart
\ingroup Nodekits
This node class is very similar to SoGroup with the exception
that it specifies restrictions on the type of children that
it allows.  It is used by nodekits to restrict child types
within <em>list parts</em> (see the reference page for SoBaseKit).


By default, any kind of child may be added. Methods of this class
allow you to restrict the type of allowable children, and to lock
down the types so that this type list may no longer be altered.


Inside the SoNodeKitListPart is a <em>container</em> node, which in
turn contains the <em>children</em>. The <em>container</em> node is a hidden
child, and the type of node used may be set with 
setContainerType().
In this way, you can make the nodekitlist behave like a group, a
separator, or any other subclass of group. The <em>container</em> is not
accessible so that the nodekitlist may retain control over what kinds
of children are added.

\par See Also
\par
SoBaseKit, SoNodeKit, SoNodeKitDetail, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoNodeKitListPart extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoNodeKitListPart.class, this);
	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoNodeKitListPart.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoNodeKitListPart.class); }              
   
	protected SoChildList children;
	
	private final SoSFName containerTypeName = new SoSFName();
	private final SoMFName childTypeNames = new SoMFName();
	private final SoSFNode containerNode = new SoSFNode();
	
	private final SoTypeList childTypes = new SoTypeList();
	
	private boolean areTypesLocked;
	
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNodeKitListPart()
//
////////////////////////////////////////////////////////////////////////
{
    children = new SoChildList(this);
    // We use the NODE  constructor 
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoNodeKitListPart.class*/);

    isBuiltIn = true;

    // Create the container node field. The default is an SoGroup.
    nodeHeader.SO_NODE_ADD_FIELD(containerTypeName,"containerTypeName",new SbName("Group"));
    nodeHeader.SO_NODE_ADD_MFIELD(childTypeNames,"childTypeNames",   new SbName(""));
    nodeHeader.SO_NODE_ADD_FIELD(containerNode,"containerNode",    (null));

    // Turn off notification on this field.
    // We store the info as a field, but unless we 
    // turn off notification, everything takes forever.
    containerNode.enableNotify(false);

    // By default, any type of node is permitted.
    // The first time a new childType is added, this first entry of 
    // 'SoNode' is removed and only types added to the list will be legal.
    childTypes.append( SoNode.getClassTypeId() );

    areTypesLocked = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    children.destructor();
    super.destructor();
}

	

	// Gets and sets the type of node used as the container. 
	//
	   // Returns type of container.
	   //
	   	public SoType getContainerType() {
		return (SoType.fromName( containerTypeName.getValue() ) );
	}
	   	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    [1] Turn off notification for this node
//    [2] Read the fields for the SoNodeKitListPart.
//    [3] Check that containerType is legal.
//    [4] Copy child types from name field into type list.
//        If none are specified, then allow anything by making
//        the first entry be 'SoNode'
//    [5] Check that containerNode is of correct type
//    [6] If containerNode exists, make it the first hidden child.
//    [7] Check that any children are of correct type, 
//    [8] Turn notification back on for node.
// 
// Use: protected

public boolean
readInstance( SoInput in, short flags )
//
////////////////////////////////////////////////////////////////////////
{
    boolean readOK = true;

    // [1] Turn off notification for this node
        boolean saveNotify = enableNotify(false);

    // [2] Read the fields for the SoNodeKitListPart.
    if ( !super.readInstance( in, flags ) )
        readOK = false;

    if ( readOK ) {
        // [3] Check that containerType is legal.
        SoType ct = getContainerType();
        if (    !ct.isDerivedFrom( SoGroup.getClassTypeId())
             || !ct.canCreateInstance() ) {
            SoReadError.post(in,
            "Given container type is not derived from Group or is an abstract class");
            readOK = false;
        }
        else {
            // [4] Copy child types from name field into type list.
            //     If none are specified, then allow anything by making
            //     the first entry be 'SoNode'
            childTypes.truncate(0);
            if ( childTypeNames.isDefault() )
                childTypes.append( SoNode.getClassTypeId() );
            else {
                for ( int i = 0; i < childTypeNames.getNum(); i++ )
                    childTypes.append( SoType.fromName( childTypeNames.operator_square_bracket(i) ) );
            }

            // [5] Check that containerNode is of correct type
            SoNode contNode = containerNode.getValue();
            if ( contNode != null && (contNode.isOfType( ct ) == false ) ) {
                SoReadError.post(in, "Given container node is of incorrect type");
                readOK = false;
            }
            else if ( contNode != null ) {

                // [6] If containerNode exists, make it the first hidden child.
                if (children.getLength() == 0)
                    children.append( contNode );
                else
                    children.insert( contNode, 0 );

                // [7] Check that any children are of correct type, 
                SoGroup grp = (SoGroup ) contNode;
                for ( int i = grp.getNumChildren() - 1; i >= 0; i-- ) {
                    if (  !isChildPermitted( grp.getChild( i ) ) ) {
                    SoReadError.post(in, "Removing child number "+i+" of illegal type");
                        grp.removeChild( i );
                    }
                }
            }
        }
    }

    // [8] Turn notification back on for node.
        enableNotify(saveNotify); 

    return readOK;
}
	   	
	
	// Gets and sets the type of node used as the container. 
	public void setContainerType(SoType newContainerType) {
		
	     if ( isTypeLocked() ) {
//	    	   #ifdef DEBUG
//	    	           SoDebugError::post("SoNodeKitListPart::setContainerType",
//	    	           "You can\'t change the type because the type lock has been turned on");
//	    	   #endif
	    	           return;
	    	       }
	    	       
	    	       if ( Objects.equals(newContainerType, getContainerType()) )
	    	           return;
	    	   
	    	       if ( !newContainerType.isDerivedFrom( SoGroup.getClassTypeId() ) )
	    	           return;
	    	   
	    	       if ( newContainerType.canCreateInstance() == false )
	    	           return;
	    	   
	    	       // If necessary, create a new container node of the correct type:
	    	       SoGroup oldContainer = (SoGroup ) containerNode.getValue();
	    	       SoGroup newContainer = null;
	    	   
	    	       if (  oldContainer == null ||
	    	            !oldContainer.isOfType( newContainerType ) ) {
	    	   
	    	           newContainer = (SoGroup ) newContainerType.createInstance(); 
	    	           newContainer.ref();
	    	   
	    	               // copy children from oldContainer to new one.
	    	               if ( oldContainer != null ) {
	    	                   for (int i = 0; i < oldContainer.getNumChildren(); i++ ) 
	    	                       newContainer.addChild( oldContainer.getChild(i) );
	    	               }
	    	           
	    	               // replace the container in this nodes children list
	    	               int oldChildNum = children.find( oldContainer );
	    	               if ( oldChildNum == -1 )
	    	                   children.insert( newContainer, 0 );
	    	               else
	    	                   children.set( oldChildNum, newContainer );
	    	   
	    	               containerNode.setValue( newContainer);
	    	   
	    	           newContainer.unref();
	    	       }
	    	   
	    	       containerTypeName.setValue( newContainerType.getName() );
	    	  	}
	
	//
	   // Description:
	   //    Returns a list of the allowable child types.
	   //    If nothing has been specified, any type of node is allowed.
	   //
	   // Use: public
	public SoTypeList getChildTypes() {
		return childTypes;
	}
	
	/**
	 * Permits the node type typeToAdd as a child. 
	 * The first time the addChildType() method is called, 
	 * the default of SoNode is overridden and only the new typeToAdd is permitted.
	 * In subsequent calls to addChildType(), the typeToAdd is added to the 
	 * existing types. 
	 * 
	 * @param typeToAdd
	 */
	public void addChildType(SoType typeToAdd) {
	     if ( isTypeLocked() ) {
//	    	   #ifdef DEBUG
//	    	           SoDebugError::post("SoNodeKitListPart::addChildType",
//	    	           "You can\'t change the type because the type lock has been turned on");
//	    	   #endif
	    	           return;
	    	       }
	    	       
	    	       // If this is our first one, then truncate the childTypes to 0.
	    	       // By default, (i.e., until we set the first one), the initial
	    	       // entry is SoNode::getClassTypeId(), which allows any node to be
	    	       // permitted.
	    	       if ( childTypeNames.isDefault() )
	    	           childTypes.truncate(0);
	    	   
	    	       // Add the type to the childTypes list if it's not there yet.
	    	       if ( childTypes.find( typeToAdd ) == -1 ) {
	    	           childTypes.append( typeToAdd );
	    	   
	    	           // Set the value of the corresponding entry in the
	    	           // childTypeNames field.
	    	           childTypeNames.set1Value(childTypes.getLength()-1,typeToAdd.getName());
	    	       }
	    	  
	}
	
	/**
	 * These are the functions used to edit the children. 
	 * They parallel those of SoGroup, except that they always check 
	 * the child types against those which are permissible. 
	 * See SoGroup for details. 
	 * 
	 * @param child
	 * @param childIndex
	 */
	//
	   // Description:
	   //    This inserts a child into the container so that it will have the given
	   //    index.
	   //
	   // Use: public
	   	public void insertChild(SoNode child, int newChildIndex) {
	     if ( isChildPermitted( child ) ) {
	    	           getContainerNode().insertChild(child, newChildIndex);
	    	       }
//	    	   #ifdef DEBUG
//	    	       else {
//	    	           SoDebugError::post("SoNodeKitListPart::insertChild",
//	    	                              "--> Can\'t insert child of type \"%s\" ",
//	    	                              child->getTypeId().getName().getString() );
//	    	       }
//	    	   #endif
	    	  		
	}
	
	// Returns whether a node of type typeToCheck may be added as a child. 
	 //
	   // Description:
	   //    Returns whether a type is legal as a child of the container.
	   //
	   // Use: public
	  public boolean isTypePermitted(SoType typeToCheck) {
		     for ( int i = 0; i < childTypes.getLength(); i++ ) {
		    	           if ( typeToCheck.isDerivedFrom( childTypes.operator_square_bracket(i) ) )
		    	               return true;
		    	       }
		    	       return false;
		    	   		
	}
	
	 //
	   // Description:
	   //    Returns whether a type is legal as a child of the container.
	   //
	   // Use: public
	   
	   boolean
	   isChildPermitted( final SoNode child )
	   //
	   {
	       for ( int i = 0; i < childTypes.getLength(); i++ ) {
	           if ( child.isOfType( childTypes.operator_square_bracket(i) ) )
	               return true;
	       }
	       return false;
	   }
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Passes the given string to the set() method of the container node.
//
// Use: public

public void
containerSet( String fieldDataString )
//
////////////////////////////////////////////////////////////////////////
{
    getContainerNode().set( fieldDataString );
}

	   
	   
	   /**
	    * This function permanently locks the permitted child types and 
	    * the container type permanently. 
	    * Calls to setContainerType() and addChildType() will have no effect 
	    * after this function is called. 
	    */
	   //
	    // Description:
	    //    Locks the types of the container and child nodes.
	    //    Once called, the methods setContainerType and addChildType
	    //    no longer have any effect.
	    //
	    // Use: public
	    	   public void lockTypes() {
		   areTypesLocked = true;
	   }
	   
	   /**
	    * Returns whether the permitted child types and the 
	    * container type are locked (i.e. cannot be changed). 
	    * See lockTypes()
	    * 
	    * @return
	    */
	  	public boolean isTypeLocked() {
	  		 return areTypesLocked; 
	  	}
	
	/**
	 * These are the functions used to edit the children. 
	 * They parallel those of SoGroup, except that they always check 
	 * the child types against those which are permissible. 
	 * See SoGroup for details. 
	 * 
	 * @param child
	 */
	//
	   // Description:
	   //    This adds a child as the last one in the container.
	   //
	   // Use: public
	   	public void addChild(SoNode child) {
	        if ( isChildPermitted( child ) ) {
	        	           // Turn off notification while getting the container.
	        	           // We'll be notifying when the child gets added, so there's
	        	           // no reason to notify if the container is created as well.
	        	           boolean wasEn = enableNotify(false);
	        	           SoGroup cont = getContainerNode();
	        	           enableNotify(wasEn);
	        	   
	        	           cont.addChild(child);
	        	       }
//	        	   #ifdef DEBUG
//	        	       else {
//	        	           SoDebugError::post("SoNodeKitListPart::addChild",
//	        	                              "--> Can\'t add child of type \"%s\" ",
//	        	                               child->getTypeId().getName().getString() );
//	        	       }
//	        	   #endif
	        	  		
	}
	   	
	   	/**
	   	 * These are the functions used to edit the children. 
	   	 * They parallel those of SoGroup, except that they always check 
	   	 * the child types against those which are permissible. 
	   	 * See SoGroup for details. 
	   	 * 
	   	 * @param index
	   	 * @return
	   	 */
	   	//
	     // Description:
	     //    returns the node of the given index from the container
	     //
	     // Use: public
	    public SoNode getChild(int index) {
	        if ( containerNode.getValue() == null )
	        	           return null;
	        	   
	        	       return ((SoGroup )containerNode.getValue()).getChild(index);
	        	  	   		
	   	}
	    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the first index (starting with 0) of a child in container that
//    matches the given node pointer, or -1 if no such child is found.
//
// Use: public

public int
findChild( SoNode child ) 
//
////////////////////////////////////////////////////////////////////////
{
    if ( containerNode.getValue() == null )
        return -1;

    return ((SoGroup )containerNode.getValue()).findChild(child);
}

	    

	/**
	 * These are the functions used to edit the children. 
	 * They parallel those of SoGroup, except that they always check the child types 
	 * against those which are permissible. See SoGroup for details. 
	 * 
	 * @return
	 */
	public int getNumChildren() {
		
	     if ( containerNode.getValue() == null )
	    	           return 0;
	    	   
	    	       return ((SoGroup )containerNode.getValue()).getNumChildren();
	    	  	}
	
	/**
	 * These are the functions used to edit the children. 
	 * They parallel those of SoGroup, except that they always check 
	 * the child types against those which are permissible. 
	 * See SoGroup for details. 
	 * 
	 * @param index
	 */
	public void removeChild(int index) {
	     if ( containerNode.getValue() == null )
	    	           return;
	    	   
	    	       SoGroup grp = (SoGroup ) containerNode.getValue();
	    	       grp.removeChild(index);
	    	   
	    	       // If the parent is a switch, make sure this doesn't 
	    	       // screw it up...
	    	       if ( grp.isOfType( SoSwitch.getClassTypeId() ) ){
	    	           SoSwitch sw = (SoSwitch ) grp;
	    	           int swNum = sw.getNumChildren();
	    	           if (sw.whichChild.getValue() >= swNum)
	    	               sw.whichChild.setValue(  swNum - 1 );
	    	       }
	    	  		
	}
	
	/**
	 * These are the functions used to edit the children. 
	 * They parallel those of SoGroup, except that they always check the 
	 * child types against those which are permissible. 
	 * See SoGroup for details. 
	 * 
	 * @param index
	 * @param newChild
	 */
	public void replaceChild(int index, SoNode newChild) {
	     if ( containerNode.getValue() == null )
	    	           return;
	    	   
	    	       if ( isChildPermitted( newChild ) ) {
	    	           ((SoGroup )containerNode.getValue()).replaceChild(index, newChild);
	    	       }
//	    	   #ifdef DEBUG
//	    	       else {
//	    	           SoDebugError::post("SoNodeKitListPart::replaceChild",
//	    	                              "--> Can\'t replace with child of type \"%s\"",
//	    	                              newChild->getTypeId().getName().getString() );
//	    	       }
//	    	   #endif
	    	   		
	}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Does the node affect the state? Well, it all depends on the container
	   //    node.
	   //
	   // Use: public
	   
	  public boolean
	   affectsState()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       if ( containerNode.getValue() == null )
	           return false;
	       else
	           return ( containerNode.getValue().affectsState() );
	   }
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies the contents of the given nodekit into this instance.
//
// Use: protected, virtual

public void
copyContents(final SoFieldContainer fromFC,
                                boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    SoNodeKitListPart origList = ( SoNodeKitListPart ) fromFC;

    // Call the base class copy.
    super.copyContents(fromFC, copyConnections);

    // Copy the child types.
    childTypes.truncate(0);
    for ( int i = 0; i < origList.childTypes.getLength(); i++ )
        childTypes.append( origList.childTypes.operator_square_bracket(i) );

    // Copy the locked flag
    if ( origList.isTypeLocked() )
        lockTypes();

    // Right now, the value of this's containerNode matches the
    // value of origList's containerNode, because copying an SoSFNode merely
    // copies the pointer and ref's the node.  We need to make a new copy 
    // of origList's containerNode and put it in the field for this.
    SoNode origContNode  = origList.containerNode.getValue();
    SoNode newContNode = null;
    if ( origContNode != null ) {
        newContNode = origContNode.copy(copyConnections);
        containerNode.setValue( newContNode);
    }

    // If it exists, make newContNode be the first child of this
    if ( newContNode != null ) {
        if ( children.getLength() == 0 )
            children.append( newContNode );
        else
            children.insert( newContNode, 0 );
    }
}


////////////////////////////////////////////////////////////////////////
//
public SoChildList 
getChildren() 
//
////////////////////////////////////////////////////////////////////////
{
   return children; 
}

	  
	  	
	protected SoGroup getContainerNode() {
		
	     if ( containerNode.getValue() != null )
	    	           return ((SoGroup ) containerNode.getValue()); 
	    	       else {
	    	           SoType   contType = SoType.fromName( containerTypeName.getValue() );
	    	           SoGroup contNode = (SoGroup ) contType.createInstance(); 
	    	           contNode.ref();
	    	   
	    	           // put contNode into this node's children list
	    	           if (children.getLength() == 0)
	    	               children.append( contNode );
	    	           else
	    	               children.insert( contNode, 0 );
	    	   
	    	           containerNode.setValue( contNode);
	    	           contNode.unref();
	    	   
	    	           return contNode;
	    	       }
	    	  	}
	

private void 
SoNodeKitListPart_doAction( SoAction action )
{
    final int[] numIndices = new int[1];
    final int[][] indices = new int[1][];

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        children.traverse(action, 0, indices[0][numIndices[0] - 1]);
    else
        children.traverse(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements get matrix action.
//
// Use: protected

protected void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[] numIndices = new int[1];
    final int[][] indices = new int[1][];

    // Only need to compute matrix if group is a node in middle of
    // current path chain or is off path chain (since the only way
    // this could be called if it is off the chain is if the group is
    // under a group that affects the chain).
    switch (action.getPathCode(numIndices, indices)) {

        case NO_PATH:
            break;

        case IN_PATH:
            children.traverse(action, 0, indices[0][numIndices[0] - 1]);
            break;

        case BELOW_PATH:
            break;

        case OFF_PATH:
            children.traverse(action);
            break;
    }
}


// These functions implement all actions for nodekits.
public void 
callback( SoCallbackAction action )
{ 
    SoNodeKitListPart_doAction( action );
}

public void 
GLRender( SoGLRenderAction action )
{ SoNodeKitListPart_doAction( action ); }

public void 
getBoundingBox( SoGetBoundingBoxAction action )
{ 
    final SbVec3f     totalCenter = new SbVec3f(0,0,0);
    int         numCenters = 0;
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild;

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        lastChild = indices[0][numIndices[0] - 1];
    else
        lastChild = children.getLength() - 1;

    for (int i = 0; i <= lastChild; i++) {
        children.traverse(action, i, i);
        if (action.isCenterSet()) {
            totalCenter.operator_add_equal( action.getCenter());
            numCenters++;
            action.resetCenter();
        }
    }
    // Now, set the center to be the average:
    if (numCenters != 0)
        action.setCenter(totalCenter.operator_div( numCenters), false);

}

public void 
handleEvent( SoHandleEventAction action )
{ SoNodeKitListPart_doAction( action ); }

public void 
pick( SoPickAction action )
{ SoNodeKitListPart_doAction( action ); }

public void 
search( SoSearchAction action )
{ 
    super.search(action);
    SoNodeKitListPart_doAction( action );
}

	

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoNodeKitListPart class.
	   //
	   // Use: internal
	   
	public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoNodeKitListPart.class, "NodeKitListPart", SoNode.class);
	   }
}
