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
  \class SoConvexDataCache Inventor/caches/SoConvexDataCache.h
  \brief The SoConvexDataCache class is used to cache convexified polygons.
  \ingroup caches

  SoConvexDataCache is used to speed up rendering of concave polygons
  by tessellating all polygons into triangles and storing the newly
  generated primitives in an internal cache.

  This class is not part of the original SGI Open Inventor v2.1
  API, but is a Coin extension.
*/

// *************************************************************************

package jscenegraph.database.inventor.caches;

import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoConvexDataCacheP implements Destroyable {

	public final SbListInt coordIndices = new SbListInt();
	public final SbListInt normalIndices = new SbListInt();
	public final SbListInt materialIndices = new SbListInt();
	public final SbListInt texIndices = new SbListInt();

	/**
	 * 
	 */
	public SoConvexDataCacheP() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destructor() {
		// TODO Auto-generated method stub
		
	}

}
