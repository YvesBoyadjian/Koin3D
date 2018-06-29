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
 |      Defines the SoBaseKit class. A base class for all
 |      of the SoNodeKit classes
 |
 |   Author(s)          : Paul Isaacs, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import java.lang.reflect.InvocationTargetException;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPickedPointList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.details.SoNodeKitDetail;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoEventCallback;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.nodekits.inventor.SoNodeKitPath;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Base class for all node kits.
/*!
\class SoBaseKit
\ingroup Nodekits
This is the base class from which all nodekit nodes are derived.
Nodekits provide a
convenient mechanism for creating groups of scene graph nodes with some
larger meaning. When you create a shape node such as an indexed face set,
for example, you almost always precede it with a coordinate node. You 
may also want to add a transform node or specify properties with
material, drawing style, material binding, etc.
Instead of creating each of these nodes
individually and then arranging them into a subgraph,
you can use a nodekit of the appropriate type (in this case, SoShapeKit).


Each class of nodekit has a <em>nodekit catalog</em> (SoNodekitCatalog)
that describes the nodes in the subgraph, referred to as <em>parts</em>.
The catalog has an entry for each part, with information
such as the <em>partName</em>, <em>partType</em>, and <em>nullByDefault</em> (if FALSE 
the constructor creates it).
The catalog also describes the arrangement of parts in the subgraph. 
(Other information is described below; a complete description is in the
SoNodekitCatalog reference page.)


If we regard the scene graph arrangement as a branching 
tree, then the top node (root) of the arrangement is always the 
nodekit itself. The leaf nodes are those at the bottom (containing no 
children).  Some leaves of the tree are defined in the catalog to be 
<em>public</em> parts, while
other leaves are <em>private</em>. All non-leaf parts are considered internal to
the nodekit structure and are marked private.
Public parts are accessible; they may be requested, changed, or set by the 
programmer with member functions 
such as getPart().  Private parts are not 
accessible, so
methods such as getPart() will have no effect on them. For example,
if you call getPart() to retrieve a private part, <tt>NULL</tt> will be returned even when the part exists.


Every nodekit reference page has a Parts section describing
the function of each public part it adds to those inherited from its parent 
class. 
Also, a Catalog Parts section has tables of often-needed 
information from the
catalog (part type, etc.). These tables include all public parts, 
both new and inherited.
Only the public parts of a nodekit are described in the reference pages.
Nodekits take care of the rest for you; they automatically arrange the 
subgraph, creating and deleting the private parts when necessary. 
(The SoNodekitCatalog reference page has methods for finding out
 the part names and arrangement of all parts, both public and private.)


The nodekit catalog is a template shared by all instances of a class.
They use the shared catalog  as a <em>guide</em> when creating parts 
(i.e., constructing actual nodes), 
but each instance stores its own parts separately.
Moreover, nodekits are <em>not</em> SoGroup 
nodes, and parts are added as <em>hidden children</em>;
you can only access parts with the methods of SoBaseKit and its
derived classes.


Any public part may be retrieved with getPart(), 
installed with setPart(), 
or removed by giving a <tt>NULL</tt> argument to setPart().
Paths from the nodekit down to a part can be created by 
createPathToPart().


By default, parts are not created until the user requests or sets them.
This keeps the subgraph uncluttered and efficient for traversal.
Additionally, removing a part (setting it to NULL) has the extra
effect of removing any internal parts that are no longer needed.


Since nodekits hide their children, any SoPath containing nodekits will
end at the topmost nodekit.  However, since nodekits may be nested within 
other nodekits, you may wish to cast an (SoPath *) into an
(SoNodeKitPath *). The methods of SoNodeKitPath allow you to view all 
nodekits that lie on the path (see the reference page for SoNodeKitPath).


Public parts in the nodekit catalog fall into three categories:


[1] <em>regular nodes</em>


[2] <em>nodekits</em>, or <em>nested nodekits</em> (which may nest recursively).
Any node which is public in a nested nodekit is accessible to 
the higher level nodekit(s) that contains it.  The description 
of getPart() below shows how to refer to nested 
parts by name (e.g., \p "appearance.material").  This works for any 
nodekit method that takes a part name for an argument.


[3] <em>lists</em>, or <em>list parts</em>. These parts group together children 
(<em>list elements</em>) of a particular type or types.  As with nested nodekits, 
you can refer to individual elements using notation described in 
getPart()
(e.g., \p "childList[0]", or if the list elements are in turn 
nodekits, \p "childList[2].transform").


When the catalog denotes that a part is a list, 
the part itself is always a node of type SoNodeKitListPart.
The catalog specifies a set of permissible \p listItemTypes and a 
\p listContainerType for that part. It gives this information to 
the SoNodeKitListPart
when it creates it. From then on, the list part will enforce
type checking. So even if you retrieve the SoNodeKitListPart
with getPart(), you will not be able to 
add illegal children. (See the SoNodeKitListPart reference page for more 
information).  As an example, the <em>callbackList</em> part of SoBaseKit 
has an SoSeparator 
container and allows only SoCallback and SoEventCallback nodes 
in the list.
Children may be added, retrieved, and removed from an SoNodeKitListPart 
node using
methods that parallel those of SoGroup. However, type-checking is strictly
enforced.


Note that, although all public parts are leaves in the nodekit catalog,
you are free to add children to them (assuming that they are groups, nodekits,
or list parts).  A part's status as a leaf in the catalog
just means that the nodekit will not manage the part's  children.
For example, SoWrapperKit has a part called <em>contents</em> with a 
part type of SoSeparator. You can put whatever you want underneath the
separator, as long as <em>contents</em> itself is an SoSeparator.


Thus, a nodekit only controls a section of the scene graph.  Above and
below that section, anything goes.


However, when nodekits are nested, they effectively create a larger
`known' section of the scene graph.
For example, the <em>appearance</em> part of the SoSeparatorKit is a leaf 
node in the SoSeparatorKit catalog. But <em>appearance</em> is in turn an
SoAppearanceKit, containing parts such as <em>material</em> and <em>drawStyle</em>.
The two nodekits combine to make an even larger template, which the 
SoSeparatorKit can examine by looking at the catalogs for both classes.
So an SoSeparatorKit can successfully return a part named \p "material";
first it finds (or creates)  the <em>appearance</em> part, then it gets the 
<em>material</em> by calling getPart()
on the <em>appearance</em>.


When the catalog defines the \p listItemTypes of a list part to be
nodekits, the name-able space expands further. For example,
SoSeparatorKit has a part <em>childList</em> which 
permits only SoSeparatorKits, so each list element can 
be further searched. Hence the name 
\b "childList[0].childList[1].childList[2].material"  is perfectly legal.

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoHandleEventAction
<BR> Behaves like an SoGroup. Traverses each child in order. 
\par
SoRayPickAction
<BR> Traverses each child in order. Then, for any pick containing the kit on its path, makes an SoNodeKitDetail as follows: Sets the <tt>"detailNodeKit"</tt> (retrievable with SoNodeKitDetail::getNodeKit()) to be a pointer to itself. Sets the <tt>"detailPart"</tt> (retrievable with SoNodeKitDetail::getPart()) to be a pointer to the kit's leaf-most part that lies on the pickPath.  Sets the <tt>"detailPartName"</tt> (retrievable with SoNodeKitDetail::getPartName()) to be the partName of that part, as found in the catalog.   Does not descend into nested nodekits. Each nodekit along the path  is the <tt>"detailPart"</tt> in its parent's detail.   However, if the pick path goes through a list part, a pointer to the child  is used for the <tt>"detailPart"</tt>, and  <tt>"detailPartName"</tt> is of the   form \p "listName[i]". 
\par
SoGetMatrixAction
<BR> Behaves like an SoGroup. Does nothing unless the kit is in the middle of the path chain the action is being applied to. If so, the children up to and including the next node in the chain are traversed. 
\par
SoSearchAction
<BR> First, searches itself like an SoNode. Then, checks the value of  isSearchingChildren(). If TRUE, traverses the children in order. If FALSE, returns. 
\par
SoWriteAction
<BR> Begins by writing out regular fields, then writes out the parts. A nodekit does <em>not</em> write out its parts the way an SoGroup writes out its children.  Instead, it writes each part as an SoSFNode field. First the partName is written, then the node being used for that part.   To keep the files terse, nodekits write out as few parts as possible. However, nodekits <em>always</em> write a part if another instance or a path is  writing it. If this is not the case, parts are left out according to  the following rules:   [1] NULL parts only write if the catalog states they are created by default.   [2] Empty SoGroup and SoSeparator nodes do not write.   [3] Non-leaf parts only write if they have non-default field values.   [4] List parts only write if they have children or if the container node has  non-default field values.   [5] Nested nodekit parts only write if they need to write one or more parts,  or if they have non-default field values. 

\par Parts
\par
\b callbackList
<BR> This is the only part that the base class SoBaseKit creates.       It is a public part that is inherited by <em>all</em> nodekits.      It provides an easy way to add callbacks for a nodekit to use      during action traversal      (e.g. SoHandleEventAction).  It is a list part and may contain      numerous SoCallback and/or SoEventCallback nodes.  

\par See Also
\par
SoAppearanceKit, SoCameraKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoBaseKit extends SoNode {
	
//	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBaseKit.class,this);
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoBaseKit.class,this);
	
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoBaseKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoBaseKit.class); }              

	
      //! Define fields for the new parts of the kit...
    protected final SoSFNode callbackList = new SoSFNode();

  
	protected SoChildList children;
	
	protected SoNodekitParts nodekitPartsList;

//	private static final SoNodekitCatalog[] nodekitCatalog = new SoNodekitCatalog[1];                          
//	private static SoNodekitCatalog[]  parentNodekitCatalogPtr = null;	
	
	private  static boolean searchingChildren;
	
    protected boolean connectionsSetUp;
	
	  
	  
	/**
	 * Returns the SoNodekitCatalog for this instance of SoBaseKit. 
	 * While each instance of a given class creates its own distinct 
	 * set of parts (which are actual nodes), 
	 * all instances share the same catalog 
	 * (which describes the parts but contains no actual node pointers). 
	 * 
	 * @return
	 */
	public SoNodekitCatalog getNodekitCatalog() {
		return kitHeader.getNodekitCatalog();
	}
	
    //! Used to store field data during writing. Need this because a temporary
    //! version of fieldData is made that differs from the real thing.
    private SoFieldData fieldDataForWriting;

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoBaseKit()
//
////////////////////////////////////////////////////////////////////////
{
    children = new SoChildList(this);
    fieldDataForWriting = null;

    // We use the NODE  constructor 
    kitHeader.SO_KIT_CONSTRUCTOR(SoBaseKit.class);

    isBuiltIn = true;

    // Create the Catalog entry for "this"
    // Don't use the macro, because we don't create a field for this part.

    if (kitHeader.firstInstance() && !SoSubKit.nodekitCatalog.get(SoBaseKit.class)[0].addEntry(new SbName("this"),
            SoBaseKit.getClassTypeId(), SoBaseKit.getClassTypeId(),
            true, new SbName(""), new SbName(""), false, SoType.badType(), SoType.badType(), false ))
        catalogError();

    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(callbackList,"callbackList", SoSeparator.class, true,
                                       "this","", SoCallback.class, true );
    kitHeader.SO_KIT_ADD_LIST_ITEM_TYPE(callbackList,"callbackList", SoEventCallback.class );


    if ( getNodekitCatalog() != null )
        nodekitPartsList = new SoNodekitParts( this );

    connectionsSetUp = false;
    setUpConnections( true, true );
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: public

public void destructor() {
    if (fieldDataForWriting != null)
        fieldDataForWriting.destructor();
    if ( nodekitPartsList != null )
        nodekitPartsList.destructor();
    children.destructor();
	super.destructor();
}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Return the part with the given name
	   //
	   // Use: public
	   
	public SoNode // java port
	getPart( String partName) {
	  return getPart(partName,true);
	}
	  public SoNode 
	   getPart( String partName, boolean makeIfNeeded ) {
		  return getPart(new SbName(partName),makeIfNeeded);
	  }
	  public SoNode 
	   getPart( SbName partName, boolean makeIfNeeded )
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // the fourth argument, leafCheck and publicCheck are TRUE, because we 
	       // don't ordinarily return parts unless they are public leaves.
	       return ( getAnyPart( partName, makeIfNeeded, true, true ) );
	   }
	   
	  	
		
	/**
	 * Given a node or a path to a node, checks if the part exists in the nodekit, 
	 * in a nested nodekit, or an element of a list part. 
	 * If so, returns a string describing the part name; 
	 * otherwise, returns an empty string (""). 
	 * 
	 * @param part
	 * @return
	 */
	public String getPartString(SoBase part) {
    final SoNodekitParts   partsList = getNodekitPartsList();
    final SoNodekitCatalog cat = getNodekitCatalog();

    if ( part == null)
        return "";

    if ( part.isOfType( SoNode.getClassTypeId() )) {
        // trivial case
        if ( part == this )
            return "this";

        // Look through the parts list and see if you can find a match.
        // Remember to skip entry 0, which is 'this'.
        for ( int i = 1; i < partsList.numEntries; i++ ) {

            SoNode iPart = partsList.fieldList[i].getValue();
            if (iPart != null) {

                // Simple match
                if ( iPart == part )
                    return cat.getName(i).getString();

                if (iPart.isOfType( SoBaseKit.getClassTypeId())) {

                    // Try to look inside the nodekit part.
                    SoBaseKit kit = (SoBaseKit ) iPart;
                    String subString = kit.getPartString(part);
                    if (subString != "") {
                        String answer =  cat.getName(i).getString() ;
                        answer += ".";
                        answer += subString/*.getString()*/;
                        return answer;
                    }
                }
                else if ( cat.isList(i) ) {

                    // Within a leaf that's a list part. Try to look inside.
                    SoNodeKitListPart lst = (SoNodeKitListPart ) iPart;

                    for (int indx = 0; 
                         indx < lst.getNumChildren(); indx++ ) {
                        SoNode kid = lst.getChild(indx);
                        if (kid == part) {
                            //char indxString[30];
                            String indxString = "["+indx+"]";
                            String answer = ( cat.getName(i).getString() );
                            answer += indxString;
                            return answer;
                        }
                        else if (kid.isOfType(SoBaseKit.getClassTypeId())) {
                            // Try to look inside the nodekit part.
                            String subString 
                                = ((SoBaseKit )kid).getPartString(part);
                            if (subString != "") {
                                //char indxString[30];
                                String indxString = "["+indx+"]";
                                String answer = ( cat.getName(i).getString() );
                                answer += indxString;
                                answer += ".";
                                answer += subString/*.getString()*/;
                                return answer;
                            }
                        }
                    }
                }
            }
        }
    }
    else if (part.isOfType( SoPath.getClassTypeId() )) {
        SoFullPath fullP = SoFullPath.cast((SoPath)part);
        int pathIndex;

        // First, find 'this' on the path. 
        for (pathIndex = 0; pathIndex < fullP.getLength(); pathIndex++ ) {
            if ( fullP.getNode(pathIndex) == this ) 
                break;
        }

        // If 'this' is not on path...
        if ( pathIndex >= fullP.getLength() )
            return "";

        // If 'this' is at end of path...
        if ( pathIndex == fullP.getLength() - 1 )
            return "this";

        pathIndex++;


        // This node appears on the path, but is not the tail.
        // See if the tail is named within this node. Remember to skip
        // entry 0, which is 'this'.
        for ( int i = 1; i < partsList.numEntries; i++ ) {

            SoNode iPart = partsList.fieldList[i].getValue();

            // If this part lies on the path, then we have work to do...
            if (iPart == fullP.getNode(pathIndex)) {

                // Is this part at the end of the path?
                if ( pathIndex == fullP.getLength() - 1 )
                    return cat.getName(i).getString();


                // Is this part a leaf in the catalog?
                if ( cat.isLeaf(i) ) {
                    if (iPart.isOfType( SoBaseKit.getClassTypeId())) {

                        // Try to look inside the nodekit part.
                        SoBaseKit kit = (SoBaseKit ) iPart;
                        String subString = kit.getPartString(part);
                        if (subString != "") {
                            String answer = ( cat.getName(i).getString() );
                            answer += ".";
                            answer += subString/*.getString()*/;
                            return answer;
                        }
                    }
                    else if ( cat.isList(i) ) {

                        SoNodeKitListPart lst = (SoNodeKitListPart ) iPart;

                        for (int indx = 0; 
                             indx < lst.getNumChildren(); indx++ ) {
                            SoNode kid = lst.getChild(indx);
                            if (kid == fullP.getTail()) {
                                //char indxString[30];
                                String indxString = "["+indx+"]";
                                String answer = ( cat.getName(i).getString() );
                                answer += indxString;
                                return answer;
                            }
                            else if (kid.isOfType(SoBaseKit.getClassTypeId())) {
                                // Try to look inside the nodekit part.
                                String subString 
                                    = ((SoBaseKit )kid).getPartString(part);
                                if (subString != "") {
                                    //char indxString[30];
                                    String indxString = "["+indx+"]";
                                    String answer = ( cat.getName(i).getString() );
                                    answer += indxString;
                                    answer += ".";
                                    answer += subString/*.getString()*/;
                                    return answer;
                                }
                            }
                        }
                    }
                }

                // Otherwise, increment the pathIndex and.
                // Keep on looking through the catalog...
                // We can keep using the same loop, since
                // successive entries in a path should always proceed to 
                // increasing indices in the parts list. This is because 
                // when classes define catalogs, entries must be added as 
                // children of pre-existing parts. 
                pathIndex++;
            }

        }
    }

    // we didn't find a match...
    return "";
	}
	
	/**
	 * Inserts the given node (not a copy) as the new part specified by partName. 
	 * See getPart() for the syntax of partName. 
	 * This method adds any extra nodes needed to fit the part into the nodekit's catalog. 
	 * For example, if you call:
	 * 
	 * mySepKit.setPart("childList[0]", myNewChild);
	 * 
	 * the kit may need to create the part childList before it can install myNewChild. 
	 * Run-time type checking verifies that the node type of newPart matches the type called for by partName. 
	 * For example, if partName was a material for an SoSeparatorKit, but newPart was an SoTransform node, 
	 * then the node would not be installed, and FALSE would be returned.
	 * If newPart is NULL, then the node specified by partName is removed. 
	 * If this renders any private parts useless (as occurs when you remove the last child of an SoGroup node), 
	 * they will also be removed. 
	 * Hence nodekits do not retain unnecessary nodes.
	 * 
	 * TRUE is returned on success, and FALSE upon error. 
	 * 
	 * @param partName
	 * @param from
	 * @return
	 */
	
	// java port
	public boolean setPart(String partName, SoNode from) {
		return setPart(new SbName(partName), from);
	}
	
	public boolean setPart(SbName partName, SoNode from) {
	     // the third argument, anyPart, is FALSE, because we don't ordinarily
		 // return parts unless they are public leaves.
		 return ( setAnyPart( partName, from, false ) );
	}
	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//     Returns the containerNode within the SoNodeKitListPart given
//     by listName.
//
// Use: protected
	public SoGroup getContainerNode(String listName) { // java port
		return getContainerNode(new SbName(listName),true);
	}
public SoGroup 
getContainerNode( final SbName listName, boolean makeIfNeeded )
{
    SoNodeKitListPart l 
        = (SoNodeKitListPart ) getAnyPart( listName, makeIfNeeded );
    if ( l == null )
        return null;
    return ( l.getContainerNode() );
}

	
	
	/**
	 * like their public versions, but are allowed access to non-leaf and private parts. 
	 * These are virtual so subclasses may do extra things when certain parts are requested. 
	 * 
	 * @param partName
	 * @param makeIfNeeded
	 * @param leafCheck
	 * @param publicCheck
	 * @return
	 */
	protected SoNode getAnyPart(SbName partName, boolean makeIfNeeded) {
		return getAnyPart(partName,makeIfNeeded,false,false);
	}
	protected SoNode getAnyPart(SbName partName, boolean makeIfNeeded, boolean leafCheck, boolean publicCheck) {
	     return (nodekitPartsList.getAnyPart( partName, makeIfNeeded, leafCheck, publicCheck ));		
	}
	
	// java port
	protected boolean setAnyPart(SbName partName, SoNode from) {
		return setAnyPart(partName, from, true);
	}
	
	//
	   // Description:
	   //    Return the part with the given name
	   //
	   // Use: protected
	   	
	protected boolean setAnyPart(SbName partName, SoNode from, boolean anyPart) {
	     if ( from != null)
	    	           from.ref();
	    	       boolean answer = nodekitPartsList.setAnyPart( partName, from, anyPart );
	    	       if ( from != null)
	    	           from.unref();
	    	       return answer;
	    	  
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets field values in the desired parts of the nodeKit...
//    The argument string contains a list of  desiredNodeName/fieldData
//    string pairs.  The string is parsed, and subsequent calls are made
//    to:     theNode = getPart( desiredNodeName);
//    and:    theNode.set( fieldData );
//
// Use: public

public boolean 
set(String nameValuePairListString) 
                            // the string to use in setting the values
//
////////////////////////////////////////////////////////////////////////
{
    String string = nameValuePairListString;
    int stringP = 0;//string; java port
    String c; //java port
    boolean        success = true;
    SoNode     node;

    while(star(string)) {
        string = skipWhiteSpace(string);

        if(!star(string))
            break;

        // look for the node name
        c = string;
        while(star(c) && !SbName.isspace(c.charAt(0)) && c.charAt(0) != '{')
            c = c.substring(1);

        if(!star(c))                 // no more data, return
            break;

        int length = -(c.length() - string.length()) + 1;
//        char *desiredNodeName = new char [length];
//        strncpy(desiredNodeName, string, c - string);
//        desiredNodeName[c - string] = 0;
        String desiredNodeName = string.substring(0, length-1);
        string = c;

        // find that node
        node = getPart(desiredNodeName, true);

        //delete [ /*length*/ ] desiredNodeName; // java port

        if (node == null)      // no node found, return
            break;

        // get the field data
        string = skipWhiteSpace(string);
        if(string.charAt(0) != '{') {
//#ifdef DEBUG
            SoDebugError.post("SoBaseKit::set",
                "Found char '"+string.charAt(0)+"' in string \""+nameValuePairListString+"\", expected '{'");
//#endif
            success = false;
            break;
        }

        string = string.substring(1);
        c = string;
        while(star(c) && c.charAt(0) != '}')
            c = c.substring(1);

        length = -(c.length() - string.length()) + 1;
//        char *fielddata = new char [length];
//        strncpy(fielddata, string, c - string);
//        fielddata[c - string] = 0;
        String fielddata = string.substring(0, length-1);
        string = c.substring(1);

        // call the SoNode::set method to set the fields
        success &= node.set(fielddata);

        //delete [ /*length*/ ] fielddata;
    }
    //free(stringP);
    return success;
}

//java port
private boolean star(String str) {
	return !str.isEmpty() && str.charAt(0)!=0;
}

public static final char __SO_COMMENT_CHAR       ='#';

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets field values in the desired parts of the nodeKit...
//    The partNameString contains the name of a part to set values for.
//    It may be of the form 'part1.part2.part3', so that the kit will
//    search within it's template for the node.
//    Next, calls the node's set, using parameterString as the input... 
//
// Use: public

public boolean
set(String partNameString,     // name of the part
               String parameterString )   // values for the part
//
////////////////////////////////////////////////////////////////////////
{
    SoNode node = getPart(partNameString, true);

    // call the SoNode::set method to set the fields
    if ( node == null )
        return false;

    if ( node.set(parameterString) )
        return true;
    else
        return false;
}

	

	//
	  // Description:
	  //    Redefines this to add this node and all part nodes to the dictionary.
	  //
	  // Use: protected, virtual
	  
	public SoNode addToCopyDict() {
		
	     // If this node is already in the dictionary, nothing else to do
		      SoNode copy = (SoNode ) checkCopy(this);
		      if (copy == null) {
		  
		          // Create and add a new instance to the dictionary
		          copy = (SoNode ) getTypeId().createInstance();
		          copy.ref();
		          addCopy(this, copy);            // Adds a ref()
		          copy.unrefNoDelete();
		  
		          // Recurse on all non-NULL parts. Skip part 0, which is "this".
		          for (int i = 1; i < nodekitPartsList.numEntries; i++) {
		              SoNode partNode = nodekitPartsList.fieldList[i].getValue();
		              if (partNode != null)
		                  partNode.addToCopyDict();
		          }
		      }
		  
		      return copy;
		 	}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//     Returns a path that begins with 'this', and ends at 
//     the part named.  
//
//     If 'pathToExtend' is not NULL:
//         the returned path will be a copy of 'pathToExtend' with the new 
//         path appended to it. In order to append, however, the following 
//         condition must be met:
//             'this' must lie on the pathToExtend, otherwise NULL is returned.
//         If 'this' lies on pathToExtend, then a copy is made, and the
//             copy is truncated to end at 'this.' Finally, the path from 'this'
//             down to the part will be appended.
//     
//
// Use: public

	// java port
	protected SoNodeKitPath 
	createPathToAnyPart(final SbName partName, 
	                            boolean makeIfNeeded, boolean leafCheck,
	                            boolean publicCheck) {
		return createPathToAnyPart(partName,makeIfNeeded,leafCheck,publicCheck,null);
	}
protected SoNodeKitPath 
createPathToAnyPart(final SbName partName, 
                            boolean makeIfNeeded, boolean leafCheck,
                            boolean publicCheck, final SoPath pathToExtend )
//
////////////////////////////////////////////////////////////////////////
{
    // Return if pathToExtend is non-NULL but doesn't contain 'this'
    if (   pathToExtend != null &&
         (SoFullPath.cast(pathToExtend)).containsNode(this) == false ) {
//#ifdef DEBUG
            SoDebugError.post("SoBaseKit::createPathToAnyPart",
            "The given pathToExtend does not contain this node.Returning NULL");
//#endif
        return null;
    }

    SoFullPath thePath = nodekitPartsList.createPathToAnyPart( partName, 
                                        makeIfNeeded, leafCheck, publicCheck );
    if ( thePath == null )
        return null;

    if ( pathToExtend == null )
        return SoNodeKitPath.cast(thePath);

    final SoFullPath fullPathToExtend = SoFullPath.cast( pathToExtend);

    thePath.ref();
    fullPathToExtend.ref();

    // Create a copy of 'fullPathToExtend' with 'thePath' tacked onto it

    // First, copy fullPathToExtend into longPath
    SoFullPath longPath = SoFullPath.cast( fullPathToExtend.copy());
    longPath.ref();

    // Now, truncate longPath to end at 'this'
    while ( longPath.getTail() != this )
        longPath.pop();

    // Finally, append 'thePath' after 'longPath'.  Leave out thePath.head(), 
    // since it's already at the tail of longPath...
    for( int i = 1; i < thePath.getLength(); i++ )
        longPath.append( thePath.getIndex( i ) );

    thePath.unref();
    fullPathToExtend.unref();
    longPath.unrefNoDelete();

    return SoNodeKitPath.cast(longPath);
}

	private int  getNumChildren() { return (children.getLength()); }
	
	void removeChild(int index) {
	     // copied from SoGroup
//		  #ifdef DEBUG
//		      if (index < 0 || index >= getNumChildren()) {
//		          SoDebugError::post( "SoBaseKit::removeChild",
//		                             "Index %d is out of range %d - %d", index, 0, getNumChildren() - 1);
//		          return;
//		      }
//		  #endif                          /* DEBUG */
		      // Play it safe anyway...
		      if (index >= 0) {
		          children.remove(index);
		      }
		  
		 		
	}
	
	void removeChild( SoNode child  ) { removeChild(findChild(child)); }
	
	void addChild(SoNode child) {
		
//		 #ifdef DEBUG
//		      if (child == NULL) {
//		          SoDebugError::post( "SoBaseKit::addChild", "NULL child node");
//		          return;
//		      }
//		  #endif                          /* DEBUG */
		  
		      children.append(child);
		 	}
	
	int findChild(final SoNode child) {
		      int i, num;
		  
		      num = getNumChildren();
		  
		      for (i = 0; i < num; i++)
		          if (getChild(i) == child) return(i);
		  
		      return(-1);
		  }		  		


	  void
	  insertChild(SoNode child, int newChildIndex)
	  {
//	  #ifdef DEBUG
//	      if (child == NULL) {
//	          SoDebugError::post( "SoBaseKit::insertChild", "NULL child node");
//	          return;
//	      }
//	  
//	      // Make sure index is reasonable
//	      if (newChildIndex < 0 || newChildIndex > getNumChildren()) {
//	          SoDebugError::post( "SoBaseKit::insertChild",
//	                             "Index %d is out of range %d - %d",newChildIndex, 0, getNumChildren());
//	          return;
//	      }
//	  #endif                          /* DEBUG */
	  
	      // See if adding at end
	      if (newChildIndex >= getNumChildren())
	          children.append(child);
	      else
	          children.insert(child, newChildIndex);
	  }
	  

public static void
setSearchingChildren( boolean newVal )
{
    searchingChildren = newVal;
}
	  
	  	
	private SoNode getChild( int index) { return children.operator_square_bracket(index); }


public boolean
isNodeFieldValuesImportant( SoNode node )
{
    // Create an instance of the same type - it will have default values
    // Use this for comparing in [Test1 ]
    SoFieldContainer def 
        = (SoFieldContainer ) node.getTypeId().createInstance();
    def.ref();

    final SoFieldData fd = node.getFieldData();
    for ( int i = 0; i < fd.getNumFields(); i++ ) { 

        // A field is important if:
        // [Test 1]: field has (isDefault() != TRUE) &&
        // [Test 2]: field does not have default value
        if (  ! fd.getField(node, i).isDefault() &&
             (! fd.getField(node, i).isSame(fd.getField(def, i)))) {

            def.unref();
            return true;
        }

    }

    // Get rid of default instance
    def.unref();

    return false;
}

	
	private void replaceChild( int index, SoNode newChild) {
	     // copied from SoGroup...
//		  #ifdef DEBUG
//		      if (index < 0 || index >= getNumChildren()) {
//		          SoDebugError::post( "SoBaseKit::replaceChild",
//		                             "Index %d is out of range %d - %d", index, 0, getNumChildren() - 1);
//		          return;
//		      }
//		  #endif                          /* DEBUG */
		  
		      // Play it safe anyway...
		      if (index >= 0)
		          children.set(index, newChild);
		 		
	}
	
	void replaceChild( SoNode oldChild, SoNode newChild)
	           { replaceChild(findChild(oldChild),newChild); }

    //! Sets and queries if nodekit children are searched during SoSearchAction
      //! traversal.  By default, they are not.
    public  static boolean isSearchingChildren() { return searchingChildren; }
 	
public SoChildList 
getChildren() 
{
   return children; 
}

	 /**
	  * This method performs the "typical" operation of a node for any action. 
	  * The default implementation does nothing. 
	  */
	 public void
	  SoBaseKit_doAction( SoAction action )
	  {
	      final int[]         numIndices = new int[1];
	      final int[][]  indices = new int[1][];
	  
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
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

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
    SoBaseKit_doAction( action );
}

public void 
GLRender( SoGLRenderAction action )
{ SoBaseKit_doAction( action ); }

public void 
getBoundingBox( SoGetBoundingBoxAction action )
{ 
    final SbVec3f totalCenter = new SbVec3f(0,0,0);
    int     numCenters = 0;
    final int[]     numIndices = new int[1];
    final int[][] indices = new int[1][];
    int     lastChild;

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        lastChild = indices[0][numIndices[0] - 1];
    else
        lastChild = getNumChildren() - 1;

    for (int i = 0; i <= lastChild; i++) {
        children.traverse(action, i, i);
        if (action.isCenterSet()) {
            totalCenter.operator_add_equal(action.getCenter());
            numCenters++;
            action.resetCenter();
        }
    }
    // Now, set the center to be the average:
    if (numCenters != 0)
        action.setCenter(totalCenter.operator_div(numCenters), false);
}

public void 
handleEvent( SoHandleEventAction action )
{ SoBaseKit_doAction( action ); }

public void 
rayPick( SoRayPickAction action )
{ 

    SoBaseKit_doAction( action ); 

    // Look at the picked point. If no detail has been constructed yet for 
    // this node in the pickedPoint's path list, then do so. (In the case
    // of instancing, the detail might already exist.)
    final SoPickedPointList pointList 
                    = ( SoPickedPointList ) action.getPickedPointList();
    SoPickedPoint kidPoint;
    SoFullPath    pointPath;

    for (int i = 0; i < pointList.getLength(); i++ ) {

        kidPoint = pointList.operator_square_bracket(i); 
        pointPath = SoFullPath.cast(kidPoint.getPath()); 

        // See if there is a detail for this node.
        if (   pointPath.containsNode( this ) 
            && kidPoint.getDetail(this) == null ) {

            // Set up the nodekit detail.
            SoNodeKitDetail newDetail = new SoNodeKitDetail();

            newDetail.setNodeKit( this );

            // Walk down the path until you find a node that is a leaf 
            // in this kit's catalog. That will be the 'part' in the 
            // detail
                // First, find index of 'this' in path
                int startIndex = -1;
                for ( int j = 0; j < pointPath.getLength(); j++ ) {
                    startIndex = j;
                    if ( pointPath.getNode(j) == this )
                        break;
                }
                // Next, walk down path starting at startIndex
                final SoNodekitCatalog cat = getNodekitCatalog();
                SoNode thePart = null;
                int    thePartNum = -1, partPathIndex = -1;
                for ( int k = startIndex; k < pointPath.getLength(); k++ ) {
                    thePart = pointPath.getNode(k);
                    partPathIndex = k;
                    // Find the part number of this part in the catalog
                    for (int l = 0; l < nodekitPartsList.numEntries; l++ ){
                        thePartNum = l;
                        if (l==0) {
                            if (nodekitPartsList.rootPointer == thePart)
                                break;
                        }
                        else {
                            if ( nodekitPartsList.fieldList[l].getValue()
                                 == thePart )
                                break;
                        }
                    }

                    if (cat.isLeaf(thePartNum) == true )
                        break;
                }

            // If the part was a nodeKitListPart, then see if one
            // of it's children is also in the path. If so, we'll
            // give back a part name like "childList[0]" instead of 
            // just childList.
                int childNumber = -1;
                if (thePart.isOfType(SoNodeKitListPart.getClassTypeId())){
                    if ( pointPath.getLength() >= (partPathIndex + 2) ) {
                        // The path contains the grandchild of the list 
                        // part. (The child is the container, the 
                        // grandchild is the actual 'child' that we expose)
                        childNumber = pointPath.getIndex(partPathIndex+2);
                    }
                }

            newDetail.setPart( thePart );
            SbName thePartName;
            if ( childNumber == -1 )
                thePartName = cat.getName(thePartNum);
            else {
                String fullStr =
                cat.getName(thePartNum).getString()+"["+childNumber+"]";
                thePartName = new SbName(fullStr);
            }

            newDetail.setPartName( thePartName );

            kidPoint.setDetail( newDetail, this );
        }
    }
}

public void
search( SoSearchAction action )
{
    super.search(action);
    if (isSearchingChildren())
        SoBaseKit_doAction( action );
}

	 
	 
	    public static SoNode typeCheck( final SbName partName, final SoType partType, 
                SoNode node ) {
    if (     node != null && !node.isOfType( partType ) ) {
//#ifdef DEBUG
        String typeName = partType.getName().getString();
        SoDebugError.post("SoBaseKit::typeCheck",
                           "ERROR getting part! The part you asked for, "+
                           "\""+partName.getString()+"\", is not of the type you asked for, "+typeName);
//#endif
        return null;
    }
    else
        return node;
	    	
	    }
	 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates the nodekitPartsList list.
//    Should be called once during the constructor for each instance.
//
//
//
// Use: protected

protected void createNodekitPartsList()
//
////////////////////////////////////////////////////////////////////////
{
    if ( getNodekitCatalog() != null ) {
        if (nodekitPartsList !=null)
            nodekitPartsList.destructor();
        nodekitPartsList = new SoNodekitParts( this );
    }
}


    //! called during construction to create any parts that are created by
    //! default (such as the cube in the SoCubeKit)
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Creates the parts in this nodekit for which
//    nullByDefault is FALSE in the nodekitCatalog
//
// Use: protected

protected void createDefaultParts()
//
////////////////////////////////////////////////////////////////////////
{
    final SoNodekitCatalog cat = getNodekitCatalog();
    SoSFNode theField;
    SoNode oldPart, myPart;

    // Start at index 1. Don't want to do anything to 'this', index 0
    for ( int i = 1; i < cat.getNumEntries(); i++ ) {
        final SbName pName = cat.getName(i);
        theField = nodekitPartsList.fieldList[i];
        // If part is created by default and either 
        //    [a] hasn't been created yet
        // or [b] has been created, but the default type has changed...
        if (cat.isNullByDefault(i) == false ) {
            oldPart = theField.getValue();
            if (   oldPart == null 
                || oldPart.isOfType( cat.getDefaultType(i) ) == false ) {
                myPart = (SoNode ) cat.getDefaultType(i).createInstance();
                setAnyPart( cat.getName(i) , myPart );
            }
        }
    }
}

    //! Return the node's partsList
public SoNodekitParts getNodekitPartsList() 
                            { return nodekitPartsList; };


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Skips white space in strings, used by the set method.
//
// Use: private

String
skipWhiteSpace(String string)
//
////////////////////////////////////////////////////////////////////////
{
    // Keep going while space and comments appear
    // Skip over space characters
    while ((!string.isEmpty() && string.charAt(0)!=0) && SbName.isspace(string.charAt(0)))
        string = string.substring(1);

    // If next character is comment character, flush until end of line
    if (!string.isEmpty() && (string.charAt(0) == __SO_COMMENT_CHAR)) {
        while ((!string.isEmpty() && string.charAt(0)!=0))
         string = string.substring(1);
    }
    return string; // java port
}


/////////////////////////////////////////////////////////////////////////
//
// call setDefault(TRUE) on the part fields (of type SoSFNode) for
// those parts which we do not wish to write out.
//
// We use the following test:
//
// First of all, we skip all the work if part already has isDefault() == TRUE
//
// Don't have to write out if:
//      [NULL Test]: value is NULL and nullByDefault is TRUE
// Otherwise, it must either be:
//      [Leaf SoGroup with no children]
// Or:
//      [Leaf SoSeparator with no children]
// Or:
//      [Leaf ListPart with no children and a container that's a group or sep]
// Or all three of the following:
//      [NonLeaf Test]:    part is not a leaf part          AND
//      [Group Test]:      node is non-NULL of type SoGroup AND
//      [FieldValue Test]: (node.isNodeFieldValuesImportant() == FALSE)
//
/////////////////////////////////////////////////////////////////////////

public void setDefaultOnNonWritingFields()
{
    final SoNodekitCatalog cat      = getNodekitCatalog();
    SoSFNode[]               pFields = nodekitPartsList.fieldList;
    int                    numP      = nodekitPartsList.numEntries;

    SoNode node;

    for ( int partNum = 1; partNum < numP; partNum++ ) {

        // Skip all the work if part already has isDefault() == TRUE
        if ( pFields[partNum].isDefault() )
            continue;

        // [NULL Test]: value is NULL and nullByDefault is TRUE
        node = pFields[partNum].getValue();
        if ( node == null && cat.isNullByDefault(partNum) ) {
            pFields[partNum].setDefault(true);
            continue;
        }

        SoType grpType = SoGroup.getClassTypeId();
        SoType sepType = SoSeparator.getClassTypeId();

        if ( cat.isLeaf(partNum) && node != null ) {

            // [Leaf SoGroup with no children]
            if ( node.getTypeId() == grpType 
                 && ((SoGroup )node).getNumChildren() == 0) {
                pFields[partNum].setDefault(true);
                continue;
            }

            // Or:
            // [Leaf SoSeparator with no children]
            if ( node.getTypeId().operator_equal_equal(sepType) 
                 && ((SoGroup )node).getNumChildren() == 0) {
                pFields[partNum].setDefault(true);
                continue;
            }

            // Or:
            // [Leaf ListPart with no kids and a group or sep container]
            if ( node.getTypeId() == SoNodeKitListPart.getClassTypeId() 
                 && ((SoNodeKitListPart )node).getNumChildren() == 0 ) {
                 SoGroup cn = ((SoNodeKitListPart )node).getContainerNode();
                 if (cn.getTypeId().operator_equal_equal(sepType) || cn.getTypeId().operator_equal_equal(grpType)) {
                    pFields[partNum].setDefault(true);
                    continue;
                }
            }
        }

        // Otherwise, must pass all three of the following:
        // [NonLeaf Test]:    part is not a leaf part          AND
        if (cat.isLeaf(partNum) == true )
            continue;

        // [Group Test]:      node is non-NULL of type SoGroup AND
        if (      node == null || 
            (   ! node.isOfType(SoGroup.getClassTypeId()))) 
            continue;

        // [FieldValue Test]: (node.isNodeFieldValuesImportant() == FALSE)
        if ( isNodeFieldValuesImportant( node ))
            continue;

        // passed all three tests...
        pFields[partNum].setDefault(true);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Skips white space in strings, used by the set method.
//
// Use: protected

public void
catalogError()
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    SoDebugError.post("SoBaseKit::catalogError",
        "ERROR creating nodekit catalog for class "+getTypeId().getName().getString()+" Did you remember to call initClass?");
//#endif
}

    //! detach/attach any sensors, callbacks, and/or field connections.
    //! Called by:            start/end of SoBaseKit::readInstance
    //! and on new copy by:   start/end of SoBaseKit::copyContents.
    //! Classes that redefine must call setUpConnections(TRUE,TRUE) 
    //! at end of constructor to add their own connections to the ones already
    //! connected by the base classes.
    //! The doItAlways flag can force the method to do the work.
    //! But if (doItAlways == FALSE && onOff == connectionsSetUp), then
    //! the method will return immediately without doing anything.
    //! Returns the state of the node when this was called.
public boolean setUpConnections( boolean onOff) {
	return setUpConnections(onOff, false);
}
    public boolean setUpConnections( boolean onOff, boolean doItAlways) {
        if ( !doItAlways && connectionsSetUp == onOff)
            return onOff;
        return !(connectionsSetUp = onOff);    	
    }

	  	 
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoBaseKit class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // We can not attempt to inherit a parentNodekitCatalogPtr
	       // from SoNode (it is undefined there).
	       // Therefore, we don't call SO_KIT_INIT_CLASS here.
	       // We just call it for SO_NODE and set parentNodekitCatalog to NULL
	   
	       SO__NODE_INIT_CLASS(SoBaseKit.class, "BaseKit", SoNode.class);
			SoSubKit.SO__KIT_INIT_CLASS(SoBaseKit.class, "BaseKit");
	       //parentNodekitCatalogPtr = null;
	   }
	   	 
	              
		public static void  SO__KIT_INIT_CLASS(Class<? extends SoBaseKit> className,String classPrintName,Class< ? extends SoBaseKit> parentClass) {
			SO__NODE_INIT_CLASS(className, classPrintName, parentClass);
			
			SoSubKit.SO__KIT_INIT_CLASS(className, classPrintName);
			
		    int _value_false= 0;                                                      
		    do {                                                                      
		    	SoSubKit.parentNodekitCatalogPtr.get(className)[0] = SoSubKit.getClassNodekitCatalogPtr(parentClass);   
		    } while (_value_false != 0);                                                   
	  }
		
		//! The macro SO_CHECK_PART will not build the part if it is not already in the
		//! kit, since it sends 'FALSE' as the 'makeIfNeeded' argument.
		public static SoNode SO_CHECK_PART( SoBaseKit kitContainingPart, String partName, Class< ? extends SoNode> partClassName ) {            
	        try {
				return (/*(partClassName )*/ SoBaseKit.typeCheck( new SbName(partName),                    
				                       (SoType)partClassName.getMethod("getClassTypeId").invoke(null),               
				                       kitContainingPart.getPart( partName, false )));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
		}
		
		public static SoNode SO_GET_PART( SoBaseKit kitContainingPart, String partName, Class< ? extends SoNode> partClassName ) {            
	        try {
				return (/*(partClassName )*/ SoBaseKit.typeCheck( new SbName(partName),                    
				                       (SoType)partClassName.getMethod("getClassTypeId").invoke(null),               
				                       kitContainingPart.getPart( partName, true )));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
		}
		
		public static SoNode SO_GET_ANY_PART( SoBaseKit kitContainingPart, String partName, Class< ? extends SoNode> partClassName ) {         
        try {
			return (/*(partClassName )*/ SoBaseKit.typeCheck( new SbName(partName),                    
					(SoType)partClassName.getMethod("getClassTypeId").invoke(null),                              
			        kitContainingPart.getAnyPart( new SbName(partName), true, false, false )));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
		}
		
		
/////////////////////////////////////////////
///
/// This must be called within the constructor of every class of nodekit.
/// It should be called immediately after the catalog is defined.
/// 
/// It does 2 things:
/// [1] created the nodekitPartsList, which keeps track of which parts
///     have been created.
/// [2] creates all parts that must be created by default (such as the
///      cube in SoCubeKit) as specified by the nullByDefault parameter
///      in the nodekit catalog
///
		protected void SO_KIT_INIT_INSTANCE() {
			   createNodekitPartsList(); 
			   createDefaultParts();			
		}
	 }
