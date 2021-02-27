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
 |      SoMFNode
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.coin3d.inventor.nodes.SoFragmentShader;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.SoNodePtr;
import jscenegraph.port.SoNodePtrArray;

////////////////////////////////////////////////////////////////////////////////
//! Multiple-value field containing any number of pointers to nodes.
/*!
\class SoMFNode
\ingroup Fields
This field maintains a set of pointers to SoNode instances,
correctly maintaining their reference counts.


SoMFNodes are written to file as one or more nodes.
When more than one value is present, all of the
values are enclosed in square brackets and separated by commas; for
example:
\code
[ Cube { }, Sphere { radius 2.0 }, USE myTranslation ]
\endcode

*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFNode extends SoMField<SoNodePtr,SoNodePtrArray> {
	
	public static Object[] SOMFNODE_NULL;
	
	static {
		SOMFNODE_NULL = new Object[1];
		SOMFNODE_NULL[0] = new SoNode[1];
	}

	@Override
	public boolean read1Value(SoInput in, int index) {
	    final SbName      name = new SbName();
	    final SoBase[]      base = new SoBase[1];

	    // See if it's a null pointer
	    if (in.read(name)) {
	        if (name.operator_equal_equal("NULL")) {
	        	SoNode[] nullVal = new SoNode[1];
	            setVal(index, nullVal);
	            return true;
	        }
	        else
	            in.putBack(name.getString());
	    }

	    // Read node
	    if (! SoBase.read(in, base, SoNode.getClassTypeId())) {
        	SoNode[] nullVal = new SoNode[1];
	        setVal(index, nullVal);
	        return false;
	    }

	    // Set value (adds a reference to node)
	    SoNode[] newVal = new SoNode[1]; newVal[0] = (SoNode)base[0]; 
	    setVal(index, /*(SoNode) base*/newVal);

	    return true;
	}

	@Override
	protected SoNodePtr constructor() {
		return new SoNodePtr();
	}

	@Override
	protected SoNodePtrArray arrayConstructor(int length) {
		SoNodePtrArray ret_val = new SoNodePtrArray(length);
		return ret_val;
	}

	public void destructor() {
		deleteAllValues();
		super.destructor();
	}

////////////////////////////////////////////////////////////////////////
//
// These all override the definitions in SO_MFIELD_VALUE_SOURCE() to
// keep track of references and auditors.
//
////////////////////////////////////////////////////////////////////////

public int
find(SoNode[] targetValue, boolean addIfNotFound)
{
    int i, num = getNum();

    for (i = 0; i < num; i++)
        if (values.getO(i).get() == targetValue[0])
            return i;

    if (addIfNotFound)
        set1Value(num, targetValue);

    return -1;
}

    public void setValues(int start, final SoNodePtrArray newvals)
    {
        int numarg = newvals != null ? newvals.length(): 0;

        // Disable temporarily, so we under any circumstances will not send
        // more than one notification about the changes.
        boolean notificstate = this.enableNotify(false);
        // Important note: the notification state is reset at the end, so
        // this function should *not* have multiple return-points.

        // ref() new nodes before unref()-ing old ones, in case there are
        // common nodes (we don't want any premature destruction to happen).
        { for (int i=0; i < numarg; i++) if (newvals.getO(i).get()!=null) newvals.getO(i).get().ref(); }

        // We favor simplicity of code over performance here.
        { for (int i=0; i < numarg; i++)
            this.set1Value(start+i, (SoNode)(newvals.getO(i).get())); }

        // unref() to match the initial ref().
        { for (int i=0; i < numarg; i++) if (newvals.getO(i).get()!=null) newvals.getO(i).get().unref(); }

        // Finally, send notification.
        this.enableNotify(notificstate);
        this.setChangedIndices(start, numarg);
        if (notificstate) this.valueChanged();
        this.setChangedIndices();
    }


	public void
	setValues(int start,/* int num,*/ final SoNode[][] newValues)
	{
		int num = newValues.length; // java port
		
	    int newNum = start + num, i;

	    if (newNum > getNum())
	        makeRoom(newNum);

	    for (i = 0; i < num; i++)
	        setVal(start + i, (SoNode[] ) newValues[i]);

	    valueChanged();
	}

public void
set1Value(int index, SoNode[] newValue)
{
    if (index >= getNum())
        makeRoom(index + 1);
    setVal(index, newValue);
    valueChanged();
}


public void
setValue(SoNode[] newValue)
{
    makeRoom(1);
    setVal(0, newValue);
    valueChanged();
}

public void
deleteAllValues()
{
    allocValues(0);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Changes value in field without doing other notification stuff.
//    Keeps track of references and auditors.
//
// Use: private

private void
setVal(int index, SoNode[] newValue)
//
////////////////////////////////////////////////////////////////////////
{
    final SoNodePtr      value = values.getO(index);

    // Play it safe if we are replacing one pointer with the same pointer
    if (newValue[0] != null)
        newValue[0].ref();

    // Get rid of old node, if any
    if (value.get() != null) {
        value.get().removeAuditor(this, SoNotRec.Type.FIELD);
        value.get().unref();
    }

    value.set( values.getO(index).set( newValue[0]));

    if (value.get() != null) {
        value.get().ref();
        value.get().addAuditor(this, SoNotRec.Type.FIELD);
    }

    if (newValue[0] != null)
        newValue[0].unref();
}


////////////////////////////////////////////////////////////////////////
//
// This overrides the definition in SO_MFIELD_ALLOC_SOURCE() to
// keep track of references and auditors.
//
////////////////////////////////////////////////////////////////////////

public void
allocValues(int newNum)
{
    int i;

    if (values == null) {
        if (newNum > 0) {
            values = new SoNodePtrArray(newNum);

            // Make sure all pointers are initialized to NULL
//            for (i = 0; i < newNum; i++)
//                values[i] = new SoNodePtr();
        }
    }
    else {
        SoNodePtrArray  oldValues = values;

        if (newNum > 0) {
            values = new SoNodePtrArray(newNum);
//            for (i = 0; i < newNum; i++) // java port
//                values[i] = new SoNodePtr();
            
            for (i = 0; i < num && i < newNum; i++)
                values.getO(i).set( oldValues.getO(i).get());

            // Initialize unused pointers to NULL
            for (i = num; i < newNum; i++)
                values.getO(i).set( null);
        }
        else
            values = null;

        // Free up any old stuff
        if (oldValues != null) {

            // Remove auditors and references on unused values
            for (i = newNum; i < num; i++) {
                if (oldValues.getO(i).get() != null) {
                    oldValues.getO(i).get().removeAuditor(this, SoNotRec.Type.FIELD);
                    oldValues.getO(i).get().unref();
                }
            }

            //delete [] oldValues; java port
        }
    }

    num = maxNum = newNum;
}

/**
 * Java port
 * @param index
 * @param node
 */
public void set1Value(int index, SoNode node) {
	SoNode[] nodePtr = new SoNode[1];
	nodePtr[0] = node;
	set1Value(index, nodePtr);
}

/*!
  Adds a node at the end of the array.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void addNode(SoNode node)
{
  this.set1Value(this.getNum(), node);
}

/*!
  Inserts a node at index \a idx.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void insertNode(SoNode node, int idx)
{
  assert(idx >= 0 && idx <= this.getNum());
  this.insertSpace(idx, 1);
  this.set1Value(idx, node);
}

/*!
  Returns the node at index \a idx.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public SoNode getNode(int idx)
{
  assert(idx >= 0 && idx < this.getNum());
  return (SoNode)(this.getValues(0).getO(idx).get());
}

/*!
  Returns the index for the first instance of \a node in the field,
  or -1 if not found.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public int findNode(SoNode node)
{
  SoNodePtrArray ptr = this.getValues(0);
  int n = this.getNum();
  for (int i = 0; i < n; i++) {
    if (ptr.getO(i).get() == node) return i;
  }
  return -1;
}

/*!
  Returns the number of nodes in this field.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public int getNumNodes()
{
  return this.getNum();
}

/*!
  Removes the node at index \a idx.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void removeNode(int idx)
{
  assert(idx >= 0 && idx < this.getNum());
  this.deleteValues(idx, 1);
}

/*!
  Removes the first instance of \a node in the field.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void removeNode(SoNode node)
{
  int idx = this.findNode(node);
  if (idx >= 0) this.removeNode(idx);
}

/*!
  Removes all nodes from the field.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void removeAllNodes()
{
  this.setNum(0);
}
/*!
  Replaces the node at index \a idx with \a newnode.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void replaceNode(int idx, SoNode newnode)
{
  assert(idx >= 0 && idx < this.getNum());
  this.set1Value(idx, newnode);
}

/*!
  Replaces the first instance of \a oldnode with \a newnode.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public void replaceNode(SoNode oldnode, SoNode newnode)
{
  int idx = this.findNode(oldnode);
  if (idx >= 0) this.replaceNode(idx, newnode);
}

}
