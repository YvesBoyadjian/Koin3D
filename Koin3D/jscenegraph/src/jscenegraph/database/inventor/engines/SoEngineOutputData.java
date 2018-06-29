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
 |      Definition of the SoEngineOutputData class, which is used by
 |      engines to store information about their outputs.  Based on
 |      SoFieldData.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoEngineOutputData
///
///  The SoEngineOutputData class holds data about engine outputs: the
///  number of outputs the engine has, the names of the outputs.
///
///  All engines of a given class share the same output data. Therefore,
///  to get information about a particular output in a particular engine
///  instance, it is necessary to pass that instance to the appropriate
///  method.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoEngineOutputData {
	
	private final SbPList outputs = new SbPList();
	
	private class SoOutputEntry {
		final SbName name = new SbName(); // Name of output
		String offset; // Offset of output within object
		final SoType type = new SoType(); // Type of output
		public void copyFrom(SoOutputEntry fromOutput) {
			name.copyFrom(fromOutput.name);
			offset = fromOutput.offset;
			type.copyFrom(fromOutput.type);
		}
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    like a copy constructor, but takes a pointer
//
public SoEngineOutputData( SoEngineOutputData from)
//
////////////////////////////////////////////////////////////////////////
{
    int                 i;
    SoOutputEntry fromOutput, toOutput;

    if (from == null)
        return;

    for (i = 0; i < from.outputs.getLength(); i++) {

        fromOutput = ( SoOutputEntry ) from.outputs.operator_square_bracket(i);

        toOutput = new SoOutputEntry();
        toOutput.copyFrom(fromOutput);

        outputs.append((Object) toOutput);
    }
}

	
    //! Adds an output to current data, given name of output,
    //! a pointer to field within the engine, and the type of output.

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds an output to current outputData.  Called inside SoEngine
//    subclass constructors, from SO_ENGINE_ADD_OUTPUT macro.
//
// Use: internal

public void                addOutput(final SoEngine defEngine,
                                  final String outputName,
                                  final SoEngineOutput output,
                                  final SoType type) {
    SoOutputEntry newOutput = new SoOutputEntry();

    newOutput.name.copyFrom( new SbName(outputName));
    newOutput.offset = outputName;
    newOutput.type.copyFrom(type);

    outputs.append( newOutput);
    }
	
	// Returns number of outputs. 
	public int getNumOutputs() {
		 return outputs.getLength(); 
	}
	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns name of output with given index in given object.
//
// Use: internal

public SbName 
getOutputName(int index)
//
////////////////////////////////////////////////////////////////////////
{
    return ((SoOutputEntry ) outputs.operator_square_bracket(index)).name;
}

	
	
	// Returns pointer to output with given index within given object instance. 
	public SoEngineOutput getOutput(SoEngine func, int index) {
		
		SoOutputEntry soOutputEntry = (SoOutputEntry)outputs.operator_square_bracket(index);
		String offset = soOutputEntry.offset;
		
		  // This generates a CC warning; there's not much we can do about it...
		  return (SoEngineOutput ) func.plus(offset);
	}
	
	// Returns index of output, given the output and the engine it is in. 
	public int getIndex(SoEngine func, SoEngineOutput output) {
	     String offset = func.fieldName(output); // java port
	      
	          // Loop through the list looking for the correct offset:
	          // (we'll assume this won't be very slow, since the list will
	          // typically be very short):
	          for (int i = 0; i < outputs.getLength(); i++) {
	              SoOutputEntry entry = (SoOutputEntry )outputs.operator_square_bracket(i);
	              if (entry.offset.equals(offset)) return i;
	          }
	      
	          // This should never happen.
	          SoDebugError.post("(internal) SoEngineOutputData::getIndex",
	                             "Did not find engineOutput");
	          return 0;
	      		
	}
	
	// Returns type of output with given index. 
	public SoType getType(int index) {
		
//		 #ifdef DEBUG
//		       if (index >= outputs.getLength())
//		           SoDebugError::post("SoEngineOutputData::getType",
//		                              "Trying to get type of output %d, but engine has "
//		                              "only %d outputs", index, outputs.getLength());
//		   #endif /* DEBUG */
		       return ((SoOutputEntry )(outputs.operator_square_bracket(index))).type;
		  	}


	public void destructor() {
		// TODO Auto-generated method stub
		
	}
}
