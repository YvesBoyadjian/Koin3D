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
 |      This file defines the SoNormalBundle class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.bundles;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.elements.SoCreaseAngleElement;
import jscenegraph.database.inventor.elements.SoGLNormalElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.misc.SoNormalGenerator;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec3fArray;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoNormalBundle
///
///  Bundle that allows shapes to deal with normals and normal bindings
///  more easily. This class provides a fairly simple interface to
///  normal handling, including normal generation.
///
///  This class can be used during either rendering or primitive
///  generation. For primitive generation, the get() method can be used
///  to access normals. For GL rendering, the send() method can be used
///  to send normals to GL.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves boyadjian
 *
 */
public class SoNormalBundle extends SoBundle implements Destroyable {

  public
    SoNormalGenerator   generator;     //!< Normal generator

	
  private
    //! Normal elements:
     SoNormalElement       normElt;
    private SoGLNormalElement     GLNormElt;

    private boolean              isRendering;    //!< Bundle being used for rendering
    private boolean              pushedState;    //!< We pushed state to set normals
    private SoNode              currentNode;   //!< Node that created the bundle
	
	
    //! Constructor - takes the action the bundle is used for and a
    //! flag to indicate whether the bundle is being used for
    //! rendering. If this is TRUE, the bundle can be used to send
    //! normals to GL. 
    public SoNormalBundle(SoAction action, boolean forRendering) {
    	super(action);
    	
    normElt = SoNormalElement.getInstance(state);

    isRendering = forRendering;

    // If we are using the bundle for GL rendering, access a GL
    // version of the element for sending normals to GL
    GLNormElt = isRendering ? ( SoGLNormalElement ) normElt : null;

    generator   = null;
    pushedState = false;

    // Save a pointer to the node that created the bundle, which is
    // assumed to be the tail of the current path in the action
    currentNode = action.getCurPathTail();
    }
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // Restore state if we did a push() in generate() or set()
    if (pushedState)
        state.pop();

    if (generator != null)
        generator.destructor();
    //super.destructor(); not necessary
}

    

    //! Returns indexed normal. This can be used for primitive
    //! generation or during rendering
    public SbVec3f      get(int index)     { return normElt.get(index); }

    //! Sends indexed normal to the GL, for use only during GL rendering
    public void                send(int index)    { GLNormElt.send(index); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This method may be called by shapes that generate their own
//    normals and want them added to the state.
//
// Use: public

public void
set(int numNormals, final SbVec3fArray normals)
//
////////////////////////////////////////////////////////////////////////
{
    // Push the state before we change it
    state.push();
    pushedState = true;

    // Set the normals in the normal element
    SoNormalElement.set(state, currentNode,
                         numNormals, normals);

    // Get the new instance of the normal element for inquiring or
    // sending normals
    normElt = SoNormalElement.getInstance(state);
    if (isRendering)
        GLNormElt = ( SoGLNormalElement ) normElt;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Return TRUE if there are no normals in the state.
//
// Use: public

public boolean
shouldGenerate(int numNormalsNeeded)
//
////////////////////////////////////////////////////////////////////////
{
    if (normElt.getNum() > 0) return false;

    initGenerator(numNormalsNeeded);

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes generation in the case where shouldGenerate() is not
//    called. (shouldGenerate() does this automatically).
//
// Use: public

public void
initGenerator(int initialNum)
//
////////////////////////////////////////////////////////////////////////
{
    // Figure out whether or not polys are clockwise or
    // counter-clockwise
    boolean ccw = true;
    final SoShapeHintsElement.VertexOrdering[] vertexOrdering = new SoShapeHintsElement.VertexOrdering[1];
    final SoShapeHintsElement.ShapeType[] shapeType = new SoShapeHintsElement.ShapeType[1];
    final SoShapeHintsElement.FaceType[] faceType = new SoShapeHintsElement.FaceType[1];
    SoShapeHintsElement.get(state, vertexOrdering, shapeType, faceType);
    if (vertexOrdering[0] == SoShapeHintsElement.VertexOrdering.CLOCKWISE) ccw = false;

    if (generator != null) generator.destructor();
    generator = new SoNormalGenerator(ccw, initialNum);
}


    ///////////////////////
    ///
    /// If shouldGenerate() returns TRUE, these methods can be used by
    /// shapes to specify the geometry to generate normals for. They
    /// are front-ends to methods on the SoNormalGenerator class:
    ///

    //! Send a polygon's worth of vertices. Begin a polygon, send as
    //! many vertices as you want, and then end the polygon.
    public void                beginPolygon()
        { generator.beginPolygon(); }
    public void                polygonVertex(final SbVec3f point)
        { generator.polygonVertex(point); }
    public void                endPolygon()
        { generator.endPolygon(); }

    //! Send a triangle
    public void                triangle(final SbVec3f p1,
                                 final SbVec3f p2,
                                 final SbVec3f p3)
        { generator.triangle(p1, p2, p3); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Calculates the normals once all vertices have been sent. The
//    normals are stored by the bundle so the caller does not have to
//    deal with them directly. The startIndex argument specifies the
//    index at which the generated normals will begin - it can be used
//    by shapes that allow the coordinates and normals to be offset.
//
// Use: public

    public void generate() {
    	generate(0, true);
    }
public void generate(int startIndex ) {
	
    generate(startIndex, true);
}

public void
generate(int startIndex, boolean addToState)
//
////////////////////////////////////////////////////////////////////////
{
    // Get crease angle from state and generate normals
    generator.generate(SoCreaseAngleElement.get(state));

    // Offset the normals, if necessary:
    if (startIndex > 0) {
        int numNorms = generator.getNumNormals();
        for (int i = numNorms-1; i >= 0; i--) {
            SbVec3f n = generator.getNormal(i);
            generator.setNormal(i+startIndex, n);
        }
    }

    // Set the normals in the state
    if (addToState) {
        set(generator.getNumNormals(), generator.getNormals());
    }
}

    //! Returns the generated normals.
    public SbVec3fArray      getGeneratedNormals()
        { return generator.getNormals(); }
    public int                 getNumGeneratedNormals()
        { return generator.getNumNormals(); }


}
