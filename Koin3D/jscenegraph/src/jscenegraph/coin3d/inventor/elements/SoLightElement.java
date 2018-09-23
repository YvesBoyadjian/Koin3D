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
  \class SoLightElement Inventor/elements/SoLightElement.h
  \brief The SoLightElement class manages the currently active light sources.
  \ingroup elements
*/

/*!
  \fn SoLightElement::lights
  List of current light nodes.
*/

/*!
  \fn SoLightElement::matrixlist

  List of matrices to map from world coordinates to view reference
  coordinates. To avoid getting a hugs element (sizeof), this
  list is only allocated in the bottom element, and the pointer
  to this list is passed along to the other elements.
*/


package jscenegraph.coin3d.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoNodeList;
import jscenegraph.database.inventor.elements.SoAccumulatedElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoLightElement extends SoAccumulatedElement {

		  protected final SoNodeList lights = new SoNodeList();
		  protected SbList<SbMatrix> matrixlist;

		private

		  // dummy class needed to initialize didalloc when constructed.
		  class so_light_elem_flag {
		  public
		    so_light_elem_flag() {
		      this.state = false;
		    }
		    boolean state;
		  };
		  private so_light_elem_flag didalloc = new so_light_elem_flag();


/*!
  The constructor
*/
public SoLightElement()
{
  //this.setTypeId(SoLightElement.classTypeId);
  //this.setStackIndex(SoLightElement.classStackIndex);
  // this is safe since a node should never be deleted while it's
  // active during a traversal
  this.lights.addReferences(false);
}

/*!
  The destructor.
*/
public void destructor()
{
  if (this.didalloc.state) Destroyable.delete( this.matrixlist);
  
  super.destructor();
}


/*!
  Adds \a light to the list of active lights. \a matrix should
  transform the light from the world coordinate system to
  the view reference coordinate system.
*/
public static void
add(SoState state, SoLight light,
                    final SbMatrix matrix)
{
  SoLightElement elem =
    (SoLightElement)
    (
     SoElement.getElement(state, classStackIndexMap.get(SoLightElement.class))
     );

  if (elem != null) {
    int i = elem.lights.getLength();
    elem.lights.append(light);
    elem.addNodeId(light);
    if (i >= elem.matrixlist.getLength())
      elem.matrixlist.append(matrix);
    else
      elem.matrixlist.operator_square_bracket(i, matrix);
  }
}

/*!
  Returns the list of light nodes.
*/
public static SoNodeList getLights(SoState state)
{
  SoLightElement elem = (SoLightElement)
    (
    SoElement.getConstElement(state, classStackIndexMap.get(SoLightElement.class))
    );
  return elem.lights;
}

/*!
  Get matrix which transforms light \a index from the world
  coordinate system to the view reference system.
*/
public static SbMatrix 
getMatrix(SoState state, int index)
{
  SoLightElement elem = (SoLightElement)
    (
     SoElement.getConstElement(state, classStackIndexMap.get(SoLightElement.class))
     );
  assert(index >= 0 && index < elem.matrixlist.getLength());
  return elem.matrixlist./*getArrayPtr()*/operator_square_bracket(index);
}

// doc from parent
public void
init(SoState state)
{
  super.init(state);
  this.matrixlist = new SbList<SbMatrix>();
  this.didalloc.state = true;
}

// Documented in superclass. Overridden to copy lights to the new top
// of stack. Also copies node ids.
public void
push(SoState state)
{
  super.push(state);

  SoLightElement prev =
    (SoLightElement)
    (
     this.getNextInStack()
     );
  this.lights.truncate(0);
  int numLights = prev.lights.getLength();
  int i;
  for (i = 0; i < numLights; i++)
    this.lights.append(prev.lights.operator_square_bracket(i));
  this.matrixlist = prev.matrixlist; // just pass pointer to list
  this.copyNodeIds(prev);
}
}
