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
 |      This file defines the abstract SoEXTENDER SoAccumulatedElement
 |      class.
 |
 |   Classes:
 |      SoAccumulatedElement
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import java.util.Objects;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoAccumulatedElement
///  \ingroup Elements
///
///  This is the abstract base class for each state element whose value
///  may be accumulated when it is set. (This is rare.) Examples are
///  transformations and profiles.
///
///  Subclasses may need to override the push() method to copy values
///  from the next instance in the stack (using getNextInStack() ),
///  if the new one has to accumulate values on top of the old ones.
///
///  This class defines the matches() method to compare lists of
///  node-id's. The node-id's represent the states of all nodes that
///  changed the value of the element. SoAccumulatedElement provides
///  methods that maintain lists of node-id's of all nodes that affect
///  an instance. Subclasses must call these methods to make sure the
///  id's are up to date, if they plan to use the standard matches()
///  method. Otherwise, they can define matches() differently, if they
///  wish.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoAccumulatedElement extends SoElement {
	
    //! This stores the list of node id's as pointers, since they
      //! should be the same length as int32_ts. The id's are sorted by
      //! increasing value, to make comparisons easier.
	protected final SbPList             nodeIds = new SbPList();
 	
	   private
		    
		        boolean              accumulatesWithParentFlag;
	   
	   //private boolean recursecapture;
		   
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoAccumulatedElement()
//
////////////////////////////////////////////////////////////////////////
{
    accumulatesWithParentFlag = true;
}

	   
	   
	   
	@Override
	public SoElement copyMatchInfo() {
	     SoAccumulatedElement result =
	    		           (SoAccumulatedElement )getTypeId().createInstance();
	    		   
	    		       result.nodeIds.operator_copy(nodeIds);
	    		   
	    		       return result;
	    		  	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Sets the node id list to JUST the id of the given node.
	   //
	   // Use: protected
	   
	  protected void
	   setNodeId(final SoNode node)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
	       if (node == null) {
	           SoDebugError.post("SoAccumulatedElement::setNodeId",
	                              "NULL node pointer passed");
	           return;
	       }
//	   #endif /* DEBUG */
	   
	       nodeIds.truncate(0);
	       nodeIds.append((Object) /*(unsigned long)*/ node.getNodeId());
	   
	       accumulatesWithParentFlag = false;
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to return TRUE if the all of the node-id's
//    of the two elements match.
//
// Use: public

public boolean
matches( SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    SoAccumulatedElement  accElt = ( SoAccumulatedElement ) elt;
    int                         i;

    if (accElt.nodeIds.getLength() != nodeIds.getLength())
        return false;

    for (i = 0; i < nodeIds.getLength(); i++)
        if (!Objects.equals(nodeIds.operator_square_bracket(i), accElt.nodeIds.operator_square_bracket(i)))
            return false;

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Clears out the list of node id's
//
// Use: protected

protected void
clearNodeIds()
//
////////////////////////////////////////////////////////////////////////
{
    nodeIds.truncate(0);
    accumulatesWithParentFlag = false;
}


	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes SoAccumulatedElement class.
//
// Use: internal

public static void
SoAccumulatedElement_initClass(final Class<? extends SoElement> javaClass)
{
    // We can't use the SO_ELEMENT_INIT_CLASS() macro here, because we
    // don't want to set the stackIndex for this class to anything
    // real. So we'll just do the rest by hand.

    classTypeIdMap.put(javaClass, SoType.createType(SoElement.getClassTypeId(SoElement.class),
                                     new SbName("AccumulatedElement"), null));
    classStackIndexMap.put(javaClass, -1);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//     Adds the id of the given node to the current list. Keeps the
//     list sorted by increasing node-id.
//
// Use: protected

protected void
addNodeId( SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (node == null) {
        SoDebugError.post("SoAccumulatedElement::addNodeId",
                           "NULL node pointer passed");
        return;
    }
//#endif /* DEBUG */

    int                 i;
    int id = node.getNodeId();

    // Search through list for correct place for id
    for (i = 0; i < nodeIds.getLength(); i++)
        if (id <= (int) nodeIds.operator_square_bracket(i))
            break;

    // Otherwise, i will contain the index where the new element belongs
    if (i >= nodeIds.getLength())
        nodeIds.append((Object) id);
        
    // Insert it in the list if it is not already there:
    else { 
        if (id != (int) nodeIds.operator_square_bracket(i))
            nodeIds.insert((Object) id, i);
    }
}

/*!
  Convenience method which copies the node ids from \a copyfrom to
  this element.

  \COIN_FUNCTION_EXTENSION
*/
public void
copyNodeIds(final SoAccumulatedElement copyfrom)
{
  this.nodeIds.operator_equal(copyfrom.nodeIds);

  // this elements uses data from previous element in stack
  //this.recursecapture = true;
  this.accumulatesWithParentFlag = true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    "Captures" this element for caching purposes.  When
//    accumulating, all the elements up to the last reset (when the
//    nodeId list was cleared) must be captured in the cache.
//
// Use: virtual, protected

public void
captureThis(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    super.captureThis(state);  // Capture this...

    if (accumulatesWithParentFlag) {
        SoAccumulatedElement parent =
            (SoAccumulatedElement )getNextInStack();
        if (parent != null)
            parent.captureThis(state);
    }
}


	   }
