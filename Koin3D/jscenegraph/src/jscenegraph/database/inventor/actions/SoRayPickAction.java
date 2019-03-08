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
 |      Defines the SoRayPickAction class
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2fSingle;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPickedPointList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoClipPlaneElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoPickRayElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Intersects objects with a ray cast into scene.
/*!
\class SoRayPickAction
\ingroup Actions
This class performs picking by casting a ray into a scene and
performing intersection tests with each object. The ray is extended to
be a cone or cylinder, depending on the camera type, for intersection
with points and lines. Each intersection is returned as an
SoPickedPoint instance.


The picking ray can be specified as either a ray from the camera
location through a particular viewport pixel, or as a world-space ray.
In the former case, a valid camera must be encountered during
traversal of the graph to determine the location of the ray in world
space.


Callers can cause the action to compute all intersections along the
ray (sorted closest to farthest) by setting the \b pickAll  flag to 
TRUE. By default, the action computes only the closest intersection.
In either case, the
intersections are returned in an SoPickedPointList. Each
intersection can be examined by accessing the appropriate
SoPickedPoint in the list. The SoPickedPoint class provides
methods to get the intersection point, normal, and other info.

\par See Also
\par
SoPickedPoint, SoPickedPointList
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoRayPickAction extends SoPickAction {
	
	public SoType getTypeId() {
		return classTypeId;
	}
    public static SoType getClassTypeId()                              
                                    { return classTypeId; }                   
    public static void addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
    // java port
    public  static void                 enableElement(Class<?> klass)         
    { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
    
    public static void enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
    protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
    private static SoType               classTypeId	;

	
	private boolean              lineWasSet;     
	private boolean              rayWasComputed; 
    private boolean              pickAll;        
    private final SbVec2s             VPPoint = new SbVec2s();
    private final SbVec2fSingle             normVPPoint = new SbVec2fSingle();    //!< Point in normalized vp coordinates
    private boolean              normPointSet;   
    private float               VPRadius;   
    private final SbMatrix            objToWorld = new SbMatrix();     
    private final SbMatrix            worldToObj = new SbMatrix();
    private final SoPickedPointList ptList = new SoPickedPointList();
 
    //! The ray is defined as an SbViewVolume as in the
    //! SoPickRayElement, and is usually stored in an instance of the
    //! element. This stores the ray if it is set using setRay().
    private final SbViewVolume        worldVol = new SbViewVolume();

    //! Users can specify negative near and far distances to indicate
    //! that picks should not be clipped to those planes. These flags
    //! store that info, since the distances in the view volume can't
    //! be negative.
    private boolean              clipToNear, clipToFar;
    
    //! These store the object-space ray info as a view volume and a
    //! line. See the comments on getViewVolume() and getLine().
    private final SbLine              objLine = new SbLine();        //!< Line representing ray
    private final SbViewVolume        objVol = new SbViewVolume();         //!< View volume representing ray

    //! If the caller passes a matrix to setObjectSpace(), the inverse
    //! of it is stored here so the object-space angle can be computed
    //! correctly later. The extraMatrixSet flag is set to TRUE in this
    //! case.
    private boolean              extraMatrixSet;
    private final SbMatrix            extraMatrix = new SbMatrix();

    
	/**
	 * Constructor takes viewport region to use for picking. 
	 * Even though the picking operation may not involve a window per se, 
	 * some nodes need this information to determine their size and placement. 
	 * 
	 * @param viewportRegion
	 */
	public SoRayPickAction(final SbViewportRegion viewportRegion) {
        super(viewportRegion);
				
	     //SO_ACTION_CONSTRUCTOR(SoRayPickAction);
	     traversalMethods = methods;
	      
	          VPPoint.setValue((short)0,(short)0); // Random point
	          VPRadius = 5.0f;              // Big enough for easy line/point picking
	      
	          lineWasSet     = false;
	          rayWasComputed = false;
	          pickAll        = false;
	          normPointSet   = false;
	      
	          clipToNear = clipToFar = true;
	      
	          objToWorld.makeIdentity();
	          worldToObj.makeIdentity();
	      	}
	
	/**
	 * Setting up the action before it is applied:
	 * Sets the viewport-space point through which the ray passes, 
	 * starting at the camera's viewpoint. 
	 * Viewport coordinates range from (0,0) at the lower left to 
	 * (width-1,height-1) at the upper right. 
	 * 
	 * @param viewportPoint
	 */
	public void setPoint(final SbVec2s viewportPoint) {
	     VPPoint.copyFrom(viewportPoint);
	          normPointSet    = false;
	          lineWasSet      = false;
	          rayWasComputed  = false;
	      
	          clipToNear = clipToFar = true;
	     		
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the viewport point in normalized coordinates.
//
// Use: public

public void
setNormalizedPoint(final SbVec2f normPoint)
//
////////////////////////////////////////////////////////////////////////
{
    normVPPoint.copyFrom( normPoint);
    normPointSet    = true;
    lineWasSet      = false;
    rayWasComputed  = false;

    clipToNear = clipToFar = true;
}

	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets the radius around the point. This is used when testing the
	   //    ray against lines and points.
	   //    
	   // Use: public
	  
	  public void
	   setRadius(float radiusInPixels)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       VPRadius = radiusInPixels;
	       lineWasSet      = false;
	       rayWasComputed  = false;
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the world-space line along which to pick.
//
// Use: public

public void
setRay(final SbVec3f start, final SbVec3f direction,
                        float nearDistance, float farDistance)
//
////////////////////////////////////////////////////////////////////////
{
    lineWasSet = true;

    // If nearDistance or farDistance is negative, set up bogus
    // non-negative values to use for setting up the perspective view
    // volume. These values will be otherwise ignored.
    if (nearDistance < 0.0) {
        clipToNear = false;
        nearDistance = 1.1f;
    }
    else
        clipToNear = true;
    if (farDistance < 0.0) {
        clipToFar = false;
        farDistance = 10.0f;
    }
    else
        clipToFar = true;

    // Save values for later. Make the volume perspective for ease.
    worldVol.perspective(0.0f, 1.0f, nearDistance, farDistance);
    worldVol.translateCamera(start);
    worldVol.rotateCamera(new SbRotation(new SbVec3f(0.0f, 0.0f, -1.0f), direction));
}

	  
	   	
	/**
	 * Sets/returns whether the action will return all objects 
	 * intersected or just the closest one. 
	 * 
	 * @param flag
	 */
	public void setPickAll(boolean flag) {
		 pickAll = flag; 
	}
	
	public boolean              isPickAll()                { return pickAll;     }
 	
    //! Returns list of picked points.
    public SoPickedPointList getPickedPointList() { return ptList; }

	
	// java port
	public SoPickedPoint getPickedPoint() {
		return getPickedPoint(0);
	}
	
	// Returns the indexed picked point from the list. 
	public SoPickedPoint getPickedPoint(int index) {
		
	     if (index >= ptList.getLength())
	    	           return null;
	    	   
	    	       return ptList.operator_square_bracket(index);
	    	  	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    If a ray was not defined with setRay(), this causes the world
//    space pick ray to be computed from the screen space point and
//    radius, using the current view specification from the state.
//    This is typically done when a camera is encountered during
//    traversal.
//
// Use: extender

public void
computeWorldSpaceRay()
//
////////////////////////////////////////////////////////////////////////
{
    // See if the world-space line was set by the user
    if (lineWasSet)
        return;

    // Get the current viewport region and view volume from the state
    final SbViewportRegion      vpReg   = SoViewportRegionElement.get(state);
    final SbViewVolume          viewVol = SoViewVolumeElement.get(state);

    float                       invWidth, invHeight;
    float                       normRadius;

    // Figure out the radius of the pick circle in the near plane as a
    // fraction of the viewport size
    invWidth  = 1.0f / vpReg.getViewportSizePixels().getValue()[0];
    invHeight = 1.0f / vpReg.getViewportSizePixels().getValue()[1];
    normRadius = (VPRadius *
                  (invWidth >= invHeight ? invWidth : invHeight));

    // If necessary, convert the current pick point from viewport
    // coords into normalized viewport coords
    if (! normPointSet) {
        normVPPoint.getValue()[0] = invWidth  * (VPPoint.getValue()[0] -
                                      vpReg.getViewportOriginPixels().getValue()[0]); 
        normVPPoint.getValue()[1] = invHeight * (VPPoint.getValue()[1] -
                                      vpReg.getViewportOriginPixels().getValue()[1]);
    }

    // Narrow the camera's view volume to a small rectangle around
    // the selected point. The width and height of the rectangle
    // are twice the radius.
    worldVol.copyFrom(viewVol.narrow(normVPPoint.getValue()[0] - normRadius,
                              normVPPoint.getValue()[1] - normRadius,
                              normVPPoint.getValue()[0] + normRadius,
                              normVPPoint.getValue()[1] + normRadius));

    // Store the resulting volume in the state so it can be pushed/popped
    SoPickRayElement.set(state, worldVol);

    rayWasComputed = true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This returns TRUE if the action has had a world space ray set or
//    computed.
//
// Use: extender

public boolean
hasWorldSpaceRay() 
//
////////////////////////////////////////////////////////////////////////
{
    // This would be a simple matter of checking the lineWasSet and
    // rayWasComputed flags, except for one thing. Suppose a scene has
    // 2 cameras in it, under 2 different separators, S1 and S2. The
    // first camera calls computeWorldSpaceRay(), which sets
    // rayWasComputed to TRUE. Then S1 pops the state, so the first
    // camera is no longer in effect - but the flag remains. When S2
    // comes along, it calls this to see if it can do pick culling. We
    // want to make sure we don't think a camera was set in this
    // case. So we check the SoViewVolumeElement's depth to see if
    // it's > 0, indicating that a camera is active.

    boolean ret;

    if (lineWasSet)
        ret = true;

    else if (! rayWasComputed)
        ret = false;

    else {
        // Get the top element from the state
        SoElement VVElt =
            state.getConstElement(SoViewVolumeElement.getClassStackIndex(SoViewVolumeElement.class));
        ret = (VVElt.getDepth() > 0);
    }

    return ret;
}

    ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Initializes the SoRayPickAction class.
     //
     // Use: internal
     
    public static void
     initClass()
     //
     ////////////////////////////////////////////////////////////////////////
     {
         //SO_ACTION_INIT_CLASS(SoRayPickAction, SoPickAction);
	       enabledElements = new SoEnabledElementsList(SoPickAction.enabledElements);
	       methods = new SoActionMethodList(SoPickAction.methods);                   
	       classTypeId    = SoType.createType(SoPickAction.getClassTypeId(),        
	                                           new SbName("SoRayPickAction"), null);	       
    	
     
         //SoRayPickAction.enableElement(SoPickRayElement.class);
         SO_ENABLE(SoRayPickAction.class, SoPickRayElement.class);
         SO_ENABLE(SoRayPickAction.class, SoViewportRegionElement.class);
         SO_ENABLE(SoRayPickAction.class, SoOverrideElement.class);
         SO_ENABLE(SoRayPickAction.class, SoTextureOverrideElement.class);
     }
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up object space for later intersection tests, given extra
//    object-to-world-space matrix.
//
// Use: extender

public void
setObjectSpace(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
//    // Make sure world space ray was set up correctly
//    if (! hasWorldSpaceRay()) {
//        SoDebugError::post("SoRayPickAction::setObjectSpace",
//                           "Camera never set up world space ray");
//        return;
//    }
//#endif /* DEBUG */

    // Make sure matrices are up to date
    computeMatrices();

    // Save matrix so it can be used to compute object-space angle later
    extraMatrix.copyFrom(matrix.inverse());
    extraMatrixSet = true;

    // Compute object-space picking ray
    computeObjVolAndLine();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersects object-space triangle specified by three vertices
//    with current ray. Returns success or failure. If it succeeds, it
//    returns intersection point, barycentric coordinates, and whether
//    the front side (defined by right-hand-rule) was hit.
//
// Use: extender

public boolean
intersect(final SbVec3f v0,
                           final SbVec3f v1,
                           final SbVec3f v2,
                           final SbVec3f intersection, final SbVec3f barycentric,
                           final boolean[] front)
//
////////////////////////////////////////////////////////////////////////
{
    if (! objLine.intersect(v0, v1, v2, intersection, barycentric, front))
        return false;

    // Make sure intersection is between near/far bounds
    if (! isBetweenPlanes(intersection))
        return false;

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersects object-space line specified by two vertices
//    with current ray. Returns success or failure. If it succeeds, it
//    returns intersection point.
//
// Use: extender

public boolean
intersect(final SbVec3f v0, final SbVec3f v1,
                           final SbVec3f intersection)
//
////////////////////////////////////////////////////////////////////////
{
    return (objVol.intersect(v0, v1, intersection) &&
            isBetweenPlanes(intersection));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersects object-space point with current ray. Returns success
//    or failure.
//
// Use: extender

public boolean
intersect(final SbVec3f point)
//
////////////////////////////////////////////////////////////////////////
{
    return (objVol.intersect(point) &&
            isBetweenPlanes(point));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Intersects object-space bounding box with current ray. Returns
//    success or failure. If useFullViewVolume is TRUE, it intersects
//    the picking view volume with the box. Otherwise, it uses just
//    the picking ray, which is faster.
//
// Use: extender

public boolean
intersect(final SbBox3f box, boolean useFullViewVolume)
//
////////////////////////////////////////////////////////////////////////
{
    // If the ray was set as a world-space line, we don't really have
    // a valid object-space view volume to test against the box. (The
    // view volume is a degenerate case - a line.) So just use the
    // object-space line to do the intersection. Also, if
    // useFullViewVolume is FALSE, use this test as well.
    if (! useFullViewVolume || lineWasSet) {
        final SbVec3f enter = new SbVec3f(), exit = new SbVec3f();
        if (! objLine.intersect(box, enter, exit))
            return false;

        final SbVec3f worldEnter = new SbVec3f(), worldExit = new SbVec3f();
        objToWorld.multVecMatrix(enter, worldEnter);
        objToWorld.multVecMatrix(exit,  worldExit);

        // If the bounding box does not lie at least partially between
        // the near and far clipping planes, there is no valid
        // intersection. Most of this is stolen from isBetweenPlanes().
        if (clipToNear || clipToFar) {

            // See if the entry point is past the far plane
            float t = worldVol.getProjectionDirection().dot(    
                        worldEnter.operator_minus(worldVol.getProjectionPoint()));
            if (clipToFar && t > worldVol.nearDist + worldVol.nearToFar)
                return false;

            // See if the exit point is in front of the near plane
            t = worldVol.getProjectionDirection().dot(    
                        worldExit.operator_minus(worldVol.getProjectionPoint()));
            if (clipToNear && t < worldVol.nearDist)
                return false;
        }

        // If we are looking for only the frontmost pick (pickAll is
        // FALSE) and we have found a previous intersection with an
        // object, we can reject the box intersection if the entry
        // point is farther than that intersection point.
        if (! pickAll && ptList.getLength() > 0 &&
            worldVol.getProjectionDirection().dot(ptList.operator_square_bracket(0).getPoint()) <
            worldVol.getProjectionDirection().dot(worldEnter))
            return false;

        // If we get here, the intersection is valid
        return true;
    }

    else
        return objVol.intersect(box);
}

    //! Returns an SbViewVolume that represents the object-space ray to
    //! pick along. The projection point of the view volume is the
    //! starting point of the ray. The projection direction is the
    //! direction of the ray. The distance to the near plane is the
    //! same as the distance to the near plane for the ray. The
    //! distance to the far plane is the sum of the near distance and
    //! the depth of the view volume.
    public SbViewVolume getViewVolume()   { return objVol; }

    //! Returns SbLine that can be used for other intersection tests.
    //! The line's position is the starting point and the direction is
    //! the direction of the ray. Given an intersection with this ray,
    //! you can call isBetweenPlanes() to see if the intersection is
    //! between the near and far clipping planes.
    public SbLine       getLine()          { return objLine; }



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the given object-space intersection point is
//    between the near and far planes of the object-space view volume,
//    as well as any clipping planes that have been defined. This test
//    can be used to determine whether the point of intersection of
//    the ray with an object is valid with respect to the clipping
//    planes.
//
// Use: extender

public boolean
isBetweenPlanes(final SbVec3f intersection)
//
////////////////////////////////////////////////////////////////////////
{
    //final SbVec3f     centerVec = new SbVec3f();
    float       t;

    // Ok. So we actually do the test in world space. This is because
    // in object space, angles between the projection direction and
    // anything else are not preserved if there is a rotate and a scale,
    // making this test bogus. In world space, there is no such problem.
    // In world space, noone can hear you scream.

    final SbVec3f worldIntersection = new SbVec3f();
    objToWorld.multVecMatrix(intersection, worldIntersection);

    // The dot product of this vector with the vector from the
    // projection point to the intersection point is the parametric
    // distance to the intersection along the direction of projection.
    t = worldVol.getProjectionDirection().dot(    
            worldIntersection.operator_minus( worldVol.getProjectionPoint()));

    // Test this distance against the near and far planes. If either
    // plane is disabled, don't test against it.
    if ((clipToNear && t < worldVol.nearDist) ||
        (clipToFar  && t > worldVol.nearDist + worldVol.nearToFar))
        return false;

    // Test the point against each active user-defined clipping plane
    SoClipPlaneElement elt =
        SoClipPlaneElement.getInstance(getState());

    // Get each clipping plane in world space and test point
    for (int i = 0; i < elt.getNum(); i++)
        if (! elt.get(i, true).isInHalfSpace(worldIntersection))
            return false;

    // If we've made it this far, accept the intersection point
    return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes matrices to go between object and world space. Does
//    this only if they have changed from last time.
//
// Use: protected

protected void
computeMatrices()
//
////////////////////////////////////////////////////////////////////////
{
    SbMatrix m = new SbMatrix(SoModelMatrixElement.get(getState()));

    if (m.operator_diff_equal( objToWorld)) {
        objToWorld.copyFrom(m);
        worldToObj.copyFrom(m.inverse());
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes object-space volume and line from world-space info and
//    current matrices. Stores result in objVol and objLine in instance.
//
// Use: private

private void
computeObjVolAndLine()
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     centerPt = new SbVec3f();

    // Transform world-space view volume into object space
    objVol.copyFrom(SoPickRayElement.get(state));
    if (extraMatrixSet) {
        SbMatrix m = worldToObj.operator_mul(extraMatrix);
        objVol.transform(m);
    }
    else
        objVol.transform(worldToObj);

    // Set up object-space line for easy intersection tests. The line
    // passes through the center of the view volume's viewport.

    if (lineWasSet)
        centerPt.copyFrom(objVol.getProjectionPoint().operator_add(objVol.getProjectionDirection()));
    else
        centerPt.copyFrom(objVol.llf.operator_add(((objVol.ulf.operator_minus(objVol.llf)).operator_add(
                                       (objVol.lrf.operator_minus(objVol.llf)))).operator_mul(0.5f)));

    if (objVol.getProjectionType() == SbViewVolume.ProjectionType.ORTHOGRAPHIC) {
        float d = objVol.getNearDist();
        if (d == 0.0)
            objLine.setValue(centerPt, 
                             centerPt.operator_add(objVol.getProjectionDirection()));
        else {
            // Make sure the line points in the right direction
            if (d < 0.0)
                d = -d;
            objLine.setValue(centerPt.operator_minus((objVol.getProjectionDirection().operator_mul(d))),
                             centerPt);
        }
    }
    else
        objLine.setValue(objVol.getProjectionPoint(), centerPt);
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up object space for later intersection tests.
//
// Use: extender

public void
setObjectSpace()
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    // Make sure world space ray was set up correctly
    if (! hasWorldSpaceRay()) {
        SoDebugError.post("SoRayPickAction::setObjectSpace",
                           "Camera never set up world space ray");
        return;
    }
//#endif /* DEBUG */

    // Make sure matrices are up to date
    computeMatrices();

    // No matrix was passed in
    extraMatrixSet = false;

    // Compute object-space picking ray
    computeObjVolAndLine();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds an SoPickedPoint instance representing the given object
//    space point to the current list and returns a pointer to it. If
//    pickAll is TRUE, this inserts the instance in correct sorted
//    order. If it is FALSE, it replaces the one instance in the list
//    only if the new one is closer; if the new one is farther away,
//    no instance is created and NULL is returned, meaning that no
//    more work has to be done to set up the SoPickedPoint.
//
// Use: extender

public SoPickedPoint 
addIntersection(final SbVec3f objectSpacePoint)
//
////////////////////////////////////////////////////////////////////////
{
    SoPickedPoint pp = new SoPickedPoint(getCurPath(), getState(),
                                          objectSpacePoint);

    // If collecting all objects, find correct place in sorted order
    // and insert point
    if (pickAll) {
        int     i;

        for (i = 0; i < ptList.getLength(); i++)
            if (isCloser(pp, ptList.operator_square_bracket(i)))
                break;

        ptList.insert(pp, i);
    }

    // If just looking for closest object, replace first object (if
    // any) if new one is closer
    else {

        // Nothing in list yet? Add the new one.
        if (ptList.getLength() == 0)
            ptList.append(pp);

        // New intersection is closer than the one in the list, so
        // delete the old one and insert the new one.
        else if (isCloser(pp, ptList.operator_square_bracket(0)))
            ptList.set(0, pp);

        // Point is not going to be stored in list, so get rid of it
        // now and return NULL
        else {
        	pp.destructor();
            //delete pp;
            pp = null;
        }
    }

    return pp;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

public void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    // If someone set the ray in world space, store the world-space
    // picking ray in the state
    if (lineWasSet)
        SoPickRayElement.set(state, worldVol);

    // Clear the intersection list
    ptList.truncate(0);

    super.beginTraversal(node);
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the first intersection point is closer to the
//    starting point of the ray than the second.
//
// Use: private

private boolean
isCloser(final SoPickedPoint pp0, final SoPickedPoint pp1)
//
////////////////////////////////////////////////////////////////////////
{
    // See which world space point is closer along the viewing
    // direction of the world-space view volume
    return (worldVol.getProjectionDirection().dot(pp0.getPoint()) <
            worldVol.getProjectionDirection().dot(pp1.getPoint()));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes distance of point along ray: start * t * direction. The
//    point has to be on the ray for this to work.
//
// Use: private, static

public static float
rayDistance(final SbVec3f start, final SbVec3f direction,
                             final SbVec3f point)
//
////////////////////////////////////////////////////////////////////////
{
//#define ABS(x) ((x) < 0.0 ? -(x) : (x))

    // Find the vector component with the maximum absolute value
    float       max, c;
    int         which;

    max = Math.abs(direction.getValueRead()[0]);
    which = 0;

    c = Math.abs(direction.getValueRead()[1]);
    if (c > max) {
        max = c;
        which = 1;
    }

    c = Math.abs(direction.getValueRead()[2]);
    if (c > max) {
        max = c;
        which = 2;
    }

    // If all components are 0, can't do this
    if (max == 0.0)
        return 0.0f;

    return (point.getValueRead()[which] - start.getValueRead()[which]) / direction.getValueRead()[which];

//#undef ABS
}
    
     }
