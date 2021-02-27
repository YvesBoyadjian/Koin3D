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
 |   Classes:
 |      SoSFNode
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Field containing a pointer to a node.
/*!
\class SoSFNode
\ingroup Fields
This field maintains a pointer to an SoNode instance,
correctly maintaining its reference count.


SoSFNodes are written to file as the node they are pointing to.
For example:
\code
mySoSFNodeField Cube {}
\endcode
is an SoSFNode field named 'mySoSFNodeField', pointing to an SoCube
node.  If the node is used elsewhere, the regular DEF/USE instancing
mechanism applies:
\code
anotherSoSFNodeField USE topSeparator
\endcode
is an SoSFNode field that points to a node named 'topSeparator' that
was DEF'ed earlier in the scene.

\par See Also
\par
SoField, SoSField, SoMFNode, SoNode
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFNode extends SoSField<SoNode> {
	
	@Override
	protected SoNode constructor() {		
		return null;
	}

	public SoSFNode() {
		super();
	}
	
	public void destructor() {
	    if (value != null) {
	        value.removeAuditor(this, SoNotRec.Type.FIELD);
	        value.unref();
	    }		
		super.destructor();
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value to given node pointer.
//
// Use: public

public void
setValue(SoNode newValue)
//
////////////////////////////////////////////////////////////////////////
{
    setVal(newValue);
    valueChanged();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Equality test. (Standard definition.)
//
// Use: public

public boolean
operator_equal_equal(final SoSFNode f)
//
////////////////////////////////////////////////////////////////////////
{
    return getValue() == f.getValue();
}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error.
//
// Use: private

//public boolean readValue(SoInput in)
////
//////////////////////////////////////////////////////////////////////////
//{
//    final SbName      name = new SbName();
//    final SoBase[]      base = new SoBase[1];
//
//    // See if it's a null pointer
//    if (in.read(name)) {
//        if (name.operator_equal_equal(new SbName("NULL"))) {
//            setVal(null);
//            return true;
//        }
//        else
//            in.putBack(name.getString());
//    }
//    
//    // Read node
//    if (! SoBase.read(in, base, SoNode.getClassTypeId())) {
//        setVal(null);
//        return false;
//    }
//
//    setVal((SoNode ) base[0]);
//
//    return true;
//}

// Import node.
public boolean readValue(SoInput in)
{
  final SoBase[] baseptr = new SoBase[1];
  boolean isVRMLspecialCase = false;

  // Note: do *not* simply check for baseptr==NULL here, as that is a
  // valid condition for VRML97 files, where nodes can indeed be
  // explicitly given as a NULL value. See the 'vrml97nullchild' test
  // case near the end of this file for a valid case that would fail.
  if(in.isFileVRML1() || in.isFileVRML2()) {
    final SbName name = new SbName();
    in.read(name, true);
    if (name.operator_equal_equal("NULL")) {
      baseptr[0] = null;
      isVRMLspecialCase = true;
    }
    else {
      in.putBack(name.getString());
    }
  }

  if (!isVRMLspecialCase) {
    if (!SoBase.read(in, baseptr, SoNode.getClassTypeId())) return false;
    if (baseptr[0] == null) {
      SoReadError.post(in, "Invalid node specification");
      return false;
    }
  }

  if (in.eof()) {
    SoReadError.post(in, "Premature end of file");
    return false;
  }

  if (baseptr[0] != null) {
    this.setValue((SoNode)(baseptr[0]));
  }
  return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Changes value in field without doing other notification stuff.
//    Keeps track of references and auditors.
//
// Use: private

private void setVal(SoNode newValue)
//
////////////////////////////////////////////////////////////////////////
{
    // Play it safe if we are replacing one pointer with the same pointer
    if (newValue != null)
        newValue.ref();

    // Get rid of old node, if any
    if (value != null) {
        value.removeAuditor(this, SoNotRec.Type.FIELD);
        value.unref();
    }

    value = newValue;

    if (value != null) {
        value.ref();
        value.addAuditor(this, SoNotRec.Type.FIELD);
    }

    if (newValue != null)
        newValue.unref();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Update a copied field to use the copy of the node if there is
//    one.
//
// Use: internal

public void
fixCopy(boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    if (value != null) {
        SoNode nodeCopy = (SoNode )
            SoFieldContainer.findCopy(value, copyConnections);
        if (nodeCopy != null)
            setVal(nodeCopy);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override this to also check the stored node.
//
// Use: internal, virtual

public boolean
referencesCopy() 
//
////////////////////////////////////////////////////////////////////////
{
    // Do the normal test, and also see if the stored node is a copy
    return (super.referencesCopy() ||
            (value != null && SoFieldContainer.checkCopy(value) != null));
}

}
