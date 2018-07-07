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
 |      This file defines the SoLinearProfile node class.
 |
 |   Author(s)          : Thad Beier, Dave Immel, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.elements.SoProfileCoordinateElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Piecewise-linear profile curve.
/*!
\class SoLinearProfile
\ingroup Nodes
This node specifies a piecewise-linear curve that is used as a profile
for either a trimming patch of a Nurbs surface or for the bevel of
SoText3.  The \b index  field indexes into the current profile
coordinates (specified in an SoProfileCoordinate2 or
SoProfileCoordinate3 node).
If the last value of the \b index  field is
<tt>SO_LINEAR_PROFILE_USE_REST_OF_VERTICES</tt> (-1), all remaining
coordinates in the current coordinates will be used, starting with
the coordinate after the previous index (all coordinates will be used
if <tt>SO_LINEAR_PROFILE_USE_REST_OF_VERTICES</tt> is the only value in the
\b index  field).  For example, if \b index  contains the values
<tt>[2,0,-1]</tt> and there are 4 profile coordinates (0-3), it is as if
the \b index  field contains <tt>[2,0,1,2,3]</tt>.

\par File Format/Default
\par
\code
LinearProfile {
  index 0
  linkage START_FIRST
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoRayPickAction, SoCallbackAction
<BR> Adds a profile to the current state. 

\par See Also
\par
SoNurbsProfile, SoProfileCoordinate2, SoProfileCoordinate3
*/
////////////////////////////////////////////////////////////////////////////////

public class SoLinearProfile extends SoProfile {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLinearProfile.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoLinearProfile.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoLinearProfile.class); }    	  	
	
	//! This value, when used as the last value of the index field, 
	//! the coordinates, starting from the last index+1 (zero if there is
	//! only one value in the index field).
	public static final int SO_LINEAR_PROFILE_USE_REST_OF_VERTICES  = (-1);


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoLinearProfile()
//
////////////////////////////////////////////////////////////////////////
{
	super();
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoLinearProfile.class*/);
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Static helper routine that figures out how many points will
//    really be used, taking into account USE_REST_OF_VERTICES
//
// Use: internal

public static int
getNumPoints(final SoMFInt32 index, int numCoords)
//
////////////////////////////////////////////////////////////////////////
{
    int numIndices = index.getNum();
    if (index.operator_square_bracketI(numIndices-1) != SO_LINEAR_PROFILE_USE_REST_OF_VERTICES)
        return numIndices;

    int lastIndex = (numIndices == 1 ? -1 : index.operator_square_bracketI(numIndices-2)+1);

    // Return regular indices plus number of coordinates left:
    return (numIndices-1) + (numCoords-1 - lastIndex);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Static helper routine that returns the correct index,
//    taking into account USE_REST_OF_VERTICES
//
// Use: internal

public static int
getIndex(int i, final SoMFInt32 index, int numCoords)
//
////////////////////////////////////////////////////////////////////////
{
    int numIndices = index.getNum();
    int result;

    if (index.operator_square_bracketI(numIndices-1) != SO_LINEAR_PROFILE_USE_REST_OF_VERTICES) {
        result = index.operator_square_bracketI(i);
    } else {
        if (i < numIndices-1) result = i;
        else {
            int lastIndex = (numIndices == 1 ? -1 : index.operator_square_bracketI(numIndices-2)+1);
            
            result = lastIndex+1 + i-(numIndices-1);
        }
    }

//#ifdef DEBUG
    if (result < 0 || result > numCoords) {
        SoDebugError.post("SoLinearProfile", "index["+i+"]="+result+" out of "+
                           "range (0,"+numCoords+")");
    }
//#endif
    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns a list of points used in making a piecewise linear trim
//    curve. numKnots and knotVector will be returned as 0 and NULL.
//
// Use: extender

public void getTrimCurve(SoState state, IntConsumer numPoints, Consumer<float[]> points,
                              IntConsumer floatsPerVec,
                              IntConsumer numKnots, Consumer<float[]> knotVector)
//
////////////////////////////////////////////////////////////////////////
{
    final SoProfileCoordinateElement    pce;
    int                                 i;

    pce = SoProfileCoordinateElement.getInstance(state);

    int np;
    numPoints.accept(np = getNumPoints(index, pce.getNum()));

    if (pce.is2D()) {
        floatsPerVec.accept(2);
        float[] p;
        points.accept(p = new float[np * 2]);

        for (i = 0; i < np; i++) {
            final SbVec2f t = pce.get2(getIndex(i, index, pce.getNum()));
            p[i*2]   = t.getValue()[0];
            p[i*2+1] = t.getValue()[1];
        }
    }
    else {
        floatsPerVec.accept(3);
        float[] p;
        points.accept(p = new float[np * 3]);

        for (i = 0; i < np; i++) {
            final SbVec3f t = pce.get3(getIndex(i, index, pce.getNum()));
            p[i*3]   = t.getValueRead()[0];
            p[i*3+1] = t.getValueRead()[1];
            p[i*3+2] = t.getValueRead()[2];
        }
    }   
    numKnots.accept(0);
    knotVector = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the vertices of the profile.  These will just be the
//    profile coordinates.  If the coordinates are rational, divide
//    out the third component.  Space will be allocated to store the
//    vertices.  The calling routine is responsible for freeing this
//    space.
//
// Use: extender

/* (non-Javadoc)
 * @see jscenegraph.database.inventor.nodes.SoProfile#getVertices(jscenegraph.database.inventor.misc.SoState, java.util.function.IntConsumer, java.util.function.Consumer)
 */
@Override
void getVertices(SoState state,	IntConsumer nVertices, Consumer<SbVec2f[]> vertices)
//
////////////////////////////////////////////////////////////////////////
{
    final SoProfileCoordinateElement    pce;
    int                                 i;

    pce = SoProfileCoordinateElement.getInstance(state);

    int nv;
    nVertices.accept(nv = getNumPoints(index, pce.getNum()));
    if (nv > 0) {
    	SbVec2f[] v; 
        vertices.accept(v = new SbVec2f[nv]);
    
        for (i = 0; i < nv; i++)
            v[i] = pce.get2(getIndex(i, index, pce.getNum()));
    } else {
        vertices = null;
    }
}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoLinearProfile class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoLinearProfile.class, "LinearProfile", SoProfile.class);
}

}
