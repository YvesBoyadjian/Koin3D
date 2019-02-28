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
  \class SoVertexShape SoVertexShape.h Inventor/nodes/SoVertexShape.h
  \brief The SoVertexShape class is the superclass for all vertex based shapes.
  \ingroup nodes

  Basically, every polygon-, line- or point-based shape will inherit
  this class.  It contains methods for organizing the normal cache,
  and also holds the SoVertexShape::vertexProperty field which can be
  used to set vertex data inside the node.
*/


// *************************************************************************

/*!
  \var SoSFNode SoVertexShape::vertexProperty

  If you set the vertexProperty field, it should be with an
  SoVertexProperty node. Otherwise it will simply be
  ignored. Nodetypes inheriting SoVertexShape will then get their
  coordinate data from the vertexProperty node instead of from the
  global traversal state.

  The vertexProperty field of SoVertexShape-derived nodes breaks
  somewhat with the basic design of Open Inventor, as its contents are
  not passed to the global state. This is done to provide a simple
  path to highly optimized rendering of vertexbased shapes.

  \sa SoVertexProperty

  \since Coin 1.0
  \since SGI Inventor v2.1
*/

// *************************************************************************


package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.threads.SbRWMutex;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexShapeP {

	public
		  SoNormalCache normalcache; // ptr

		  // we can use a per-instance mutex here instead of this class-wide
		  // one, but we go for the class-wide one since at least MSWindows
		  // might have a rather strict limit on the total amount of mutex
		  // resources a process / user can hold at any one time.
		  //
		  // i haven't looked too hard at the affected code regions in the
		  // sub-classes, however. it might be that a class-wide lock can
		  // cause significantly less efficient execution in a multi-threaded
		  // environment. if so, we will have to come up with something better
		  // than just a class-wide lock (a mutex pool or something, i
		  // suppose).
		  //
		  // -mortene.
		  static SbRWMutex normalcachemutex; // ptr

		  static void cleanup() {
			  Destroyable.delete(normalcachemutex);
			  normalcachemutex = null;			  
		  }
		  
	/**
	 * 
	 */
	public SoVertexShapeP() {
		// TODO Auto-generated constructor stub
	}

}
