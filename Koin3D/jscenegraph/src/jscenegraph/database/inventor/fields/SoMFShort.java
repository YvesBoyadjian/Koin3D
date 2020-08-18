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

/*!
  \class SoMFShort SoMFShort.h Inventor/fields/SoMFShort.h
  \brief The SoMFShort class is a container for short integer values.
  \ingroup fields

  This field is used where nodes, engines or other field containers
  needs to store a group of multiple short integer values.

  This field supports application data sharing through a
  setValuesPointer() method. See SoMField documentation for
  information on how to use this function.

  \sa SoSFShort
*/

// *************************************************************************

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFShort extends SoMField<Short> {

	/**
	 * 
	 */
	public SoMFShort() {
		super();
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#read1Value(jscenegraph.database.inventor.SoInput, int)
	 */
	@Override
	public boolean read1Value(SoInput in, int idx) {
		  assert(idx < this.maxNum);
		  final short[] dummy = new short[1];
		  if( in.read(/*this.values[idx]*/dummy)) {
			  this.values[idx] = dummy[0];
			  return true;
		  }
		  return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Short constructor() {
		return (short) 0;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected Short[] arrayConstructor(int length) {
		return new Short[length];
	}


	// Store more than the default single value on each line for ASCII
	// format export.
	public int
	getNumValuesPerLine()
	{
	  return 8;
	}
}
