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
public class SoMFNode extends SoMField<SoNode[]> {
	
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
	protected SoNode[] constructor() {
		return new SoNode[1];
	}

	@Override
	protected SoNode[][] arrayConstructor(int length) {
		return new SoNode[length][1];
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
        if (values[i][0] == targetValue[0])
            return i;

    if (addIfNotFound)
        set1Value(num, targetValue);

    return -1;
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
    SoNode[]      value = values[index];

    // Play it safe if we are replacing one pointer with the same pointer
    if (newValue[0] != null)
        newValue[0].ref();

    // Get rid of old node, if any
    if (value[0] != null) {
        value[0].removeAuditor(this, SoNotRec.Type.FIELD);
        value[0].unref();
    }

    value[0] = values[index][0] = newValue[0];

    if (value[0] != null) {
        value[0].ref();
        value[0].addAuditor(this, SoNotRec.Type.FIELD);
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
            values = new SoNode[newNum][1];

            // Make sure all pointers are initialized to NULL
            for (i = 0; i < newNum; i++)
                values[i][0] = null;
        }
    }
    else {
        SoNode[][]  oldValues = values;

        if (newNum > 0) {
            values = new SoNode[newNum][1];
            for (i = 0; i < num && i < newNum; i++)
                values[i][0] = oldValues[i][0];

            // Initialize unused pointers to NULL
            for (i = num; i < newNum; i++)
                values[i][0] = null;
        }
        else
            values = null;

        // Free up any old stuff
        if (oldValues != null) {

            // Remove auditors and references on unused values
            for (i = newNum; i < num; i++) {
                if (oldValues[i][0] != null) {
                    oldValues[i][0].removeAuditor(this, SoNotRec.Type.FIELD);
                    oldValues[i][0].unref();
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
  Returns the number of nodes in this field.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public int getNumNodes()
{
  return this.getNum();
}
}
