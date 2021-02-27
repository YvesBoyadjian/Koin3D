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

package jscenegraph.coin3d.inventor.annex.profiler;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
/*
  The SoNodeProfiling class contains instrumentation code for scene
  graph profiling.

  The convention for using these preTraversal and postTraversal
  methods is that the parent node only applies these around child
  traversal invokations, and not around itself.  This is really
  important to get straight.  As a consequence of this, the applying
  action also needs to add preTraversal()/postTraversal() around
  invoking traversal of the root.

  If you combine doing both, then you get a lot of double-booking of
  timings and negative timing offsets, which causes mayhem in the
  statistics, and was a mess to figure out.
*/

public class SoNodeProfiling implements Destroyable {

	public void preTraversal(SoAction action) {
		// TODO Auto-generated method stub
		
	}

	public void postTraversal(SoAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destructor() {
		// TODO Auto-generated method stub
		
	}

}
