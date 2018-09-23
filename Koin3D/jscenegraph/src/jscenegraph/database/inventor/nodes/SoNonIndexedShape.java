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
 |      This file defines the SoNonIndexedShape node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all non-indexed vertex-based shapes.
/*!
\class SoNonIndexedShape
\ingroup Nodes
This node is the abstract base class for all vertex-based shapes that
are not constructed from indices, such as SoFaceSet,
SoLineSet, and SoQuadMesh.


All subclasses of SoNonIndexedShape construct objects by using the
coordinates specified in the \b vertexProperty  field (from
SoVertexShape), or the current inherited coordinates. 
The \b startIndex 
field defined by this class is now obsolete, and is provided
here only for compatibility with old files and programs.


The subclass decides what to do with this
and any subsequent coordinates. The shape is drawn with the current
lighting model and drawing style and is transformed by the current
transformation matrix.


Material, normal, and texture coordinate bindings for shapes derived
from this class ignore any index specifications. That is, a binding
value of <tt>PER_FACE_INDEXED</tt> is treated the same way as <tt>PER_FACE</tt>,
and so on.


If there aren't sufficient values in the current coordinates,
material, or texture coordinates, errors will occur.

\par See Also
\par
SoFaceSet, SoIndexedShape, SoLineSet, SoPointSet, SoQuadMesh, SoTriangleStripSet, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoNonIndexedShape extends SoVertexShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoNonIndexedShape.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoNonIndexedShape.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoNonIndexedShape.class); }              
	
	

	   public final SoSFInt32 startIndex = new SoSFInt32();
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoNonIndexedShape()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoNonIndexedShape*/);

    nodeHeader.SO_NODE_ADD_SFIELD(startIndex,"startIndex", (0));
}

	   

	    //! This is a convenience method to simplify the job of computing
	    //! bounding boxes for subclasses; it can be called from a
	    //! subclass's computeBBox() method. It sets the given bounding box
	    //! to contain all vertices of the shape, assuming that the shape
	    //! uses the numVertices coordinates beginning at the value in
	    //! startIndex. (If numVertices is negative, it uses all
	    //! coordinates from startIndex on.) It also sets the center to the
	    //! average of the vertices' coordinates.
	public void computeCoordBBox(SoAction action, int numVertices,
            final SbBox3f box, final SbVec3f center) {
    int                     i, lastIndex;
    SoCoordinateElement   ce = null;
    SbVec3f[]               vpCoords = null;

    SoVertexProperty vp = (SoVertexProperty )vertexProperty.getValue();
    if (vp != null && vp.vertex.getNum() > 0) {
        vpCoords = vp.vertex.getValues(0);
    } else {
        ce = SoCoordinateElement.getInstance(action.getState());
    }

    // Start with an empty box and zero sum
    center.setValue(0.0f, 0.0f, 0.0f);
    box.makeEmpty();

    // Loop through coordinates, keeping max bounding box and sum of coords
    i = startIndex.getValue();
    if (numVertices < 0) {
        lastIndex = (ce != null ? ce.getNum() - 1 : vp.vertex.getNum() - 1);
        numVertices = (int) (lastIndex - i + 1);
    }
    else
        lastIndex = i + numVertices - 1;

    while (i <= lastIndex) {

        SbVec3f v = (ce != null ? ce.get3((int) i) : vpCoords[i]);

        box.extendBy(v);
        center.operator_add_equal(v);

        i++;
    }

    // The center point is the average of the vertices
    center.operator_div_equal((float) numVertices);
	}     
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoNonIndexedShape class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoNonIndexedShape.class, "NonIndexedShape",
                                 SoVertexShape.class);
}

	
}
