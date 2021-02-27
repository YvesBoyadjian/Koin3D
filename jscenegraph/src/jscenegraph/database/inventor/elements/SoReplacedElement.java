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
 |      This file defines the abstract SoEXTENDER SoReplacedElement class.
 |
 |   Classes:
 |      SoReplacedElement
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoReplacedElement
///  \ingroup Elements
///
///  This is the abstract base class for each state element whose value
///  is replaced whenever it is set. (Most state elements fall into
///  this category, so most are derived from this class.)
///
///  This class overrides the SoElement::getElement() method to store
///  the node-id of the node that is about to set the value in the
///  element (i.e., the node that is passed to getElement()). This
///  class also defines the SoElement::matches() method to return TRUE
///  if the node-id's of the two elements match. Subclasses can change
///  this behavior by defining matches() differently, if they wish. For
///  example, they can compare the elements' values, instead.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoReplacedElement extends SoElement {

	   protected
		        long            nodeId;
		    	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides this method to return TRUE if the node-id's of the two
//    elements match.
//
// Use: public

public boolean
matches(SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    return (nodeId == ((SoReplacedElement ) elt).nodeId);
}

	   ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides SoElement::getElement() to set the nodeId in the
//    element instance before returning it.
//
// Use: protected

protected static SoElement 
getElement(SoState state, int stackIndex, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoReplacedElement elt;

    elt = (SoReplacedElement ) SoElement.getElement(state, stackIndex);

    if (elt != null) {
    	if( node != null) {
    		elt.nodeId = node.getNodeId();
    	}
    	else {
    		elt.nodeId = 0;
    	}
    }
    return elt;
}

	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Create a copy of this instance suitable for calling matches()
	   //     on.
	   //
	   // Use: protected
	   
	  public SoElement 
	   copyMatchInfo()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoReplacedElement result =
	           (SoReplacedElement )getTypeId().createInstance();
	   
	       result.nodeId = nodeId;
	   
	       return result;
	   }
	   	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes SoReplacedElement class.
	   //
	   // Use: internal
	   
	   public static void
	   SoReplacedElement_initClass(final Class<? extends SoElement> javaClass)
	   {
	       // We can't use the SO_ELEMENT_INIT_CLASS() macro here, because we
	       // don't want to set the stackIndex for this class to anything
	       // real. So we'll just do the rest by hand.
	   
		   classTypeIdMap.put(javaClass, SoType.createType(SoElement.getClassTypeId(SoElement.class),
	                                        new SbName("SoReplacedElement"), null));
		   classStackIndexMap.put(javaClass, -1);
	   }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    nodeId = 0;
}

	   
    //! Return nodeId.  This was added so the SoTransformSeparator
    //! class can figure out whether or not it contains a camera:
    public long            getNodeId() { return nodeId; }

    // java port
    public final long            SoReplacedElement_getNodeId() { return nodeId; }
}
