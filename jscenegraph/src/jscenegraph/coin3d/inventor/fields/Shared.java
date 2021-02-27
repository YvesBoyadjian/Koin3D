/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

// This file contains various functions that are shared between two or
// more field classes.
//
// Typically there's some common code for both the single-field value
// and multiple-field value version of the same field-type.

// *************************************************************************

// FIXME: SoSFEnum and SoMFEnum could probably share some of their
// read / write code. Ditto for SoSFImage and SoSFImage3. If so, it
// should be collected here. 20040630 mortene.

// *************************************************************************

package jscenegraph.coin3d.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;

/**
 * @author BOYADJIAN
 *
 */
public class Shared {

	// Read boolean value from input stream, return TRUE if
	// successful. Used from SoSFBool and SoMFBool.
	public boolean
	sosfbool_read_value(SoInput in, final boolean[]  val)
	{
	  // accept 0 or 1
		int[] valInt = new int[1];
	  if (in.read(valInt)) {
	    if (valInt[0] != 0 && valInt[0] != 1) {
	      SoReadError.post(in, "Illegal value for field: "+valInt[0]+" (must be 0 or 1)");
	      return false;
	    }
	    val[0] = (valInt[0] != 0);
	    return true;
	  }

	  if (in.isBinary()) {
	    SoReadError.post(in, "Premature end of file");
	    return false;
	  }

	  // read TRUE/FALSE keyword

	  final SbName n = new SbName();
	  if (!in.read(n, true)) {
	    SoReadError.post(in, "Couldn't read field value");
	    return false;
	  }

	  if (n.operator_equal_equal("TRUE")) {
	    val[0] = true;
	    return true;
	  }

	  if (n.operator_equal_equal("FALSE")) {
	    val[0] = false;
	    return true;
	  }

	  SoReadError.post(in,
	                    "Invalid value \""+n.getString()+"\" for field (must be TRUE or FALSE)");
	  return false;
	}

}
