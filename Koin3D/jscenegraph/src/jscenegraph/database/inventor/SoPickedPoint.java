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
 |      This file defines the base SoPickedPoint class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.coin3d.inventor.elements.SoMultiTextureMatrixElement;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoPickStyleElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Represents point on surface of picked object.
/*!
\class SoPickedPoint
\ingroup General
An SoPickedPoint represents a point on the surface of an object
that was picked by applying an SoRayPickAction to a scene. It
contains a path to the picked shape, the point of intersection, the
surface normal and texture coordinates at that point, and other
information.


Each node in the picked path may have a corresponding instance of a detail
subclass. These detail instances are stored in the SoPickedPoint.

\par See Also
\par
SoRayPickAction, SoPickStyle, SoDetail, SoPath
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPickedPoint implements Destroyable {
	
  private
    //! This action is used to get the world-to-object matrix
    static SoGetMatrixAction    matrixAction;

    //! Intersection point and normal in world space, and texture
      //! coordinates in image space
     private final SbVec3f worldPoint = new SbVec3f();
     private final SbVec3f worldNormal = new SbVec3f();
     private final SbVec4f imageTexCoords = new SbVec4f();
  
    //! ViewportRegion, which is needed when figuring out matrices
    private final SbViewportRegion    vpRegion = new SbViewportRegion();

    //! Material index
    private int                 materialIndex;

    //! TRUE if pick was on geometry of shape as opposed to bbox
    private boolean              onGeometry;

     //! The path to the shape that was picked
    private SoPath path;
    

    //! Details corresponding to nodes in path, one per node in path.
    //! Many may be NULL.
    private final SoDetailList        details = new SoDetailList();

    //! The pointer to the state allows us to access matrices to
    //! convert from object space to world and image space. This
    //! pointer is valid only during construction and setting of the
    //! info inside the instance.
    private SoState             state;

    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor - internal since instances are created only by the
//    SoRayPickAction.
//
// Use: internal

public SoPickedPoint(final SoPath _path, final SoState _state,
                             final SbVec3f objSpacePoint)
//
////////////////////////////////////////////////////////////////////////
{
    int i, n;

    // Make a copy of the path since it most likely comes from the
    // current traversal path in an action, which will be changed soon.
    path =  _path.copy();
    path.ref();

    // Save state so we can get matrices when we need them later
    state = _state;

    // Transform the object space point by the current modeling matrix
    // to get the world space point
    SoModelMatrixElement.get(state).multVecMatrix(objSpacePoint, worldPoint);

    // Make room in the detail list for one detail per node in the
    // path. Set all the detail pointers to NULL.
    n = ( SoFullPath.cast(path)).getLength();
    details.set(n - 1, null);           // Allocates space
    for (i = n - 2; i >= 0; --i)
        details.set(i, null);

    // Initialize material index to 0, the most common value
    materialIndex = 0;

    // Set on-geometry flag based on current pick style
    onGeometry = (SoPickStyleElement.get(state) !=
                  SoPickStyleElement.Style.BOUNDING_BOX);

    // Save the viewportRegion, we'll need it later:
    vpRegion.copyFrom(SoViewportRegionElement.get(state));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy constructor.
//
// Use: public

public SoPickedPoint( final SoPickedPoint pp)
//
////////////////////////////////////////////////////////////////////////
{
    worldPoint.copyFrom(pp.worldPoint);
    worldNormal.copyFrom(pp.worldNormal);
    imageTexCoords.copyFrom(pp.imageTexCoords);
    materialIndex  = pp.materialIndex;
    path           = pp.path;
    onGeometry     = pp.onGeometry;
    vpRegion.copyFrom(pp.vpRegion);

    // Copy the details - note that the copy() method for SoDetailList
    // makes copies of the detail instances. This has to be done
    // because the instances are deleted when the list is deleted.
    details.operator_equal(pp.details);

    // Ref the path, since we unref it when we are deleted
    path.ref();
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // Free up path
    path.unref();

    // The SoDetailList destructor deletes all details in the list, so
    // we don't have to do it here
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns an instance that is a copy of this instance. The caller
//    is responsible for deleting the copy when done.
//
// Use: public

public SoPickedPoint 
copy() 
//
////////////////////////////////////////////////////////////////////////
{
    SoPickedPoint newCopy = new SoPickedPoint(this);
    return newCopy;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the detail that corresponds to the given node in the path.
//
// Use: public

public SoDetail 
getDetail(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    int index;

    // Test for default case, corresponding to tail of path
    if (node == null)
        index = ( SoFullPath.cast ( path)).getLength() - 1;

    else
        index = getNodeIndex(node);

    return details.operator_square_bracket(index);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the transformation matrix to go from the object space
//    corresponding to the given node in the path to world space.
//
// Use: public

public SbMatrix
getObjectToWorld(final SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    getMatrix(node);
    return matrixAction.getMatrix();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the transformation matrix to go from world space to the
//    object space corresponding to the given node in the path.
//
// Use: public

public SbMatrix
getWorldToObject( SoNode node) 
//
////////////////////////////////////////////////////////////////////////
{
    getMatrix(node);
    return matrixAction.getInverse();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the transformation matrix to go from the object space
//    corresponding to the given node in the path to image space.
//
// Use: public

public SbMatrix
getObjectToImage( SoNode node) 
//
////////////////////////////////////////////////////////////////////////
{
    getMatrix(node);
    return matrixAction.getTextureMatrix();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the transformation matrix to go from image space to the
//    object space corresponding to the given node in the path.
//
// Use: public

public SbMatrix
getImageToObject(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    getMatrix(node);
    return matrixAction.getTextureInverse();
}

    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the intersection point in object space that corresponds
//    to the given node in the path.
//
// Use: public

// java port
public SbVec3f
getObjectPoint() {
	return getObjectPoint(null);
}

public SbVec3f
getObjectPoint( SoNode node) 
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     v = new SbVec3f();

    getWorldToObject(node).multVecMatrix(worldPoint, v);

    return v;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the surface normal in object space that corresponds to
//    the given node in the path.
//
// Use: public

// java port
public SbVec3f
getObjectNormal() {
	return getObjectNormal(null);
}

public SbVec3f
getObjectNormal( SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     v = new SbVec3f();

    getWorldToObject(node).multDirMatrix(worldNormal, v);
    v.normalize();

    return v;
}

    
    
    /**
     * These return the intersection point and surface 
     * normal in world space, and the texture coordinates 
     * in image space. 
     * 
     * @return
     */
    public SbVec3f getPoint() {
    	 return worldPoint; 
    }
    //! These return the intersection point and surface normal in world space,
      //! and the texture coordinates in image space.
    public  SbVec3f getNormal() { return worldNormal; }
 
	// Returns the path to the object that was intersected. 
	public SoPath getPath() {
		 return path; 
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the object-space normal.
//
// Use: extender

public void
setObjectNormal(final SbVec3f normal)
//
////////////////////////////////////////////////////////////////////////
{
    // Transform the object space normal by the current modeling
    // matrix o get the world space normal. Use the inverse transpose
    // of the odel matrix so that normals are not scaled incorrectly.
    SbMatrix normalMatrix =
        SoModelMatrixElement.get(state).inverse().transpose();

    normalMatrix.multDirMatrix(normal, worldNormal);
    worldNormal.normalize();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the object-space texture coordinates.
//
// Use: extender

public void
setObjectTextureCoords(final SbVec4f texCoords)
//
////////////////////////////////////////////////////////////////////////
{
    // Transform the object space coords by the current texture matrix
    // to get the image space texture coords
    imageTexCoords.copyFrom( multVecMatrix4(SoMultiTextureMatrixElement.get(state,0),
                                    texCoords));
}

    //! Sets the material index
	public void                setMaterialIndex(int index)  { materialIndex = index; }




////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void close()
//
////////////////////////////////////////////////////////////////////////
{
    // Free up path
    path.unref();

    // The SoDetailList destructor deletes all details in the list, so
    // we don't have to do it here
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Multiplies SbVec4f by matrix - for transforming texture coords.
//
// Use: private, static

private static SbVec4f
multVecMatrix4(final SbMatrix m, final SbVec4f v)
//
////////////////////////////////////////////////////////////////////////
{
    int         i;
    final SbVec4f     v2 = new SbVec4f();

    for (i = 0; i < 4; i++)
        v2.getValueRead()[i] = (v.getValueRead()[0] * m.getValue()[0][i] +
                 v.getValueRead()[1] * m.getValue()[1][i] +
                 v.getValueRead()[2] * m.getValue()[2][i] +
                 v.getValueRead()[3] * m.getValue()[3][i]);

    return v2;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the detail corresponding to the given node in the path.
//    NULL may be passed to remove a detail.
//
// Use: extender

public void
setDetail(SoDetail detail, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Find node in path
    i = getNodeIndex(node);

//#ifdef DEBUG
//    if (i < 0)
//        SoDebugError::post("SoPickedPoint::setDetail",
//                           "Node %#x is not found in path", node);
//#endif /* DEBUG */

    details.set(i, detail);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns index in path of given node, or -1 if not found.
//
// Use: private

private int
getNodeIndex(final SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    int i;

    // Search from bottom up for node in path, since details are
    // usually examined near the bottom
    for (i = ( SoFullPath.cast ( path)).getLength() - 1; i >= 0; i--)
        if (path.getNode(i) == node)
            return i;

//#ifdef DEBUG
    SoDebugError.post("SoPickedPoint::getNodeIndex",
                       "Node "+node+" is not found in path");
//#endif /* DEBUG */

    return -1;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Applies given instance of SoGetMatrixAction to path to node.
//
// Use: private

private void
getMatrix( SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoPath xfPath;

    // Construct a path from the root down to this node. Use the given
    // path if it's the same
    if (node == null || node == SoFullPath.cast(path).getTail())
        xfPath = path;

    else {
        int     index = getNodeIndex(node);
        xfPath = path.copy(0, index + 1);
        xfPath.ref();
    }

    // Create an action instance if necessary, then apply it to the path
    if (matrixAction == null)
        matrixAction = new SoGetMatrixAction(vpRegion);
    matrixAction.apply(xfPath);

    if (xfPath != path)
        xfPath.unref();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the texture coordinates in object space that corresponds
//    to the given node in the path.
//
// Use: public

public SbVec4f
getObjectTextureCoords(final SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    return multVecMatrix4(getImageToObject(node), imageTexCoords);
}

}
