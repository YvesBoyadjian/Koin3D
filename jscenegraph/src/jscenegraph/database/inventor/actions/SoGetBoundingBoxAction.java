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
 |      Defines the SoGetBoundingBoxAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoBBoxModelMatrixElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoLocalBBoxMatrixElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.interaction.inventor.SoSceneManager;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Computes bounding box of a scene.
/*!
\class SoGetBoundingBoxAction
\ingroup Actions
This class is used to compute a 3D bounding box enclosing objects
defined by a scene graph. The box is a rectangular prism. The action
also computes the center point, which is defined differently for
different objects.  (For example, the center of an SoFaceSet is
the average of its vertices' coordinates.) For a group, the center
point is defined as the average of the centers of all shapes in it.


Each bounding box is calculated as a SbXfBox3f, where the
transformation matrix is defined so that the bounding box can be
stored in the object space of the SoShape. When two bounding boxes
are combined by a group node, the combination is performed so as to
produce the smaller untransformed box.  The result of the calculation
by the action can be returned as an SbXfBox3f or as a
world-space-aligned SbBox3f.


To calculate the bounding box of a subgraph bounded by two paths,
specify the left edge of the subgraph with setResetPath(), and
apply the action to the path that defines the right edge of the
subgraph. The accumulated bounding box and transformation will be
reset when the tail of the reset path is traversed.


If the subgraph being traversed does not contain any shapes, the returned
bounding box will be empty (that is,
box.isEmpty()
will return TRUE).

\par See Also
\par
SbBox3f, SbXfBox3f, SoGetMatrixAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGetBoundingBoxAction extends SoAction implements Destroyable {
	
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

    //! Set this flag to TRUE if you want the returned bounding box to be in
      //! the space of whatever camera is in the graph. Camera space is defined
      //! to have the viewpoint at the origin, with the direction of view along
      //! the negative z axis. This space can be used to determine distances of
      //! objects from the camera.
      void                setInCameraSpace(boolean flag)   { cameraSpace = flag; }

      //! Returns camera space flag.
      public     boolean              isInCameraSpace()          { return cameraSpace; }
             
     //! set a path to do a resetTransform/resetBoundingBox on. The default
      //! is to do the reset right before the given path.
      //! Which things get reset:
      public enum ResetType {
          TRANSFORM      ( 0x01),      //!< Transformation
          BBOX           ( 0x02),      //!< Bounding Box
          ALL            ( 0x03);       //!< Both Transform and Bounding Box
          
          private int value;
          
          ResetType(int value) {
        	  this.value = value;
          }
          
          public int getValue() {
        	  return value;
          }
      };
 	
	   private     final SbXfBox3f    box = new SbXfBox3f();            //!< Bounding box
	   private  final SbVec3f             center = new SbVec3f();         //!< Center point
	   private boolean              cameraSpace;    //!< Bounding box in camera space?
	     private    SoPath        resetPath;     //!< path to reset transform
	     private     boolean              resetBefore;    //!< reset before or after?
	     private     ResetType           resetWhat;      //!< which things get reset?
	     private final SbViewportRegion vpRegion = new SbViewportRegion(); //!< Current viewport region
	     private boolean              centerSet;      //!< setCenter() called?
	     	   
	/**
	 * Constructor takes viewport region to use for picking. 
	 * Even though the bounding box computation may not involve 
	 * a window per se, some nodes need this information to 
	 * determine their size and placement. 
	 * 
	 * @param viewportRegion
	 */
	public SoGetBoundingBoxAction(SbViewportRegion region) {
	     //SO_ACTION_CONSTRUCTOR(SoGetBoundingBoxAction);
	     traversalMethods = methods;
	    		 
	          vpRegion.copyFrom(region);
	      
	          resetPath = null;
	          resetBefore = false;
	          resetWhat = ResetType.ALL;
	      
	          // empty the bounding box
	          box.makeEmpty();
	      
	          // By default, we use world space even if a camera is present
	          setInCameraSpace(false);
	     	}
	
	public void destructor() {
	    if (resetPath != null)
	        resetPath.unref();
	    super.destructor();
	}
	
    //! If a non-NULL path is specified, the action will reset the computed
    //! bounding box to be empty and/or the current transformation to
    //! identity. The \p resetBefore flag indicates whether to perform the
    //! reset before or after the tail node of the path is traversed.
	// java port
	public void setResetPath( SoPath path) {
		setResetPath(path, true, ResetType.ALL);
	}
	public    void                setResetPath( SoPath path,
                                     boolean _resetBefore,
                                     ResetType _what) {
	    if (resetPath != null)
	        resetPath.unref();

	    resetPath = path;

	    if (resetPath != null)
	        resetPath.ref();

	    resetBefore = _resetBefore;
	    resetWhat = _what;
		
	}
    //! Returns the current reset path, or NULL.
    public SoPath       getResetPath() { return resetPath; }
    //! Returns TRUE if the current reset path is not NULL.
    public boolean              isResetPath() { return( resetPath != null); }
    //! Returns TRUE if the \p resetBefore flag was specified for the reset path.
    public boolean              isResetBefore() { return resetBefore; }
    public ResetType getWhatReset() { return resetWhat;}

	
	
	// Sets/returns current viewport region to use for action. 
	public void setViewportRegion(SbViewportRegion newRegion) {
		  vpRegion.copyFrom(newRegion);
	}
	
	// Returns computed bounding box in world space. 
	public SbBox3f getBoundingBox() {
		 return box.project(); 
	}
	
	// Returns computed bounding box before transformation into world space. 
	public SbXfBox3f getXfBoundingBox() {
		 return box; 
	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Gets the bounding box "center".  Different shapes may specify
	   //    that their center should not be considered the center of their
	   //    bounding box (e.g. text might be centered on its baseline, which
	   //    wouldn't be the center of its bounding box).
	   //
	   // Use: public
	   
	  public SbVec3f 
	   getCenter()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       return center;
	   }
	   
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Used by shapes to set their preferred center.
	   //
	   // Use: extender
	   
	   public void
	   setCenter( SbVec3f c, boolean transformCenter)
	   //
	   ///////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
//	       if (centerSet)
//	           SoDebugError::postWarning("SoGetBoundingBoxAction::setCenter",
//	                                     "overriding a previously set center; some "
//	                                     "group class is not doing its job!");
//	   #endif
	   
	       if (transformCenter) {
	           final SbMatrix m = new SbMatrix(SoLocalBBoxMatrixElement.get(getState()));
	   
	           if (isInCameraSpace())
	               m.multRight(SoViewingMatrixElement.get(getState()));
	   
	           m.multVecMatrix(c, center);
	       }
	       else
	           center.copyFrom(c);
	   
	       centerSet = true;
	   }
	   	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns TRUE if center has been set. Used by groups so they can
	   //    properly average the centers of their children.
	   //
	   // Use: extender
	   
	  public boolean
	   isCenterSet()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       return centerSet;
	   }

	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Resets the center to the origin and unsets the centerSet flag.
	   //    This is used by groups so they can properly average the centers
	   //    of their children.
	   //
	   // Use: extender
	   
	  public void
	   resetCenter()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       centerSet = false;
	       center.setValue(0.0f, 0.0f, 0.0f);
	   }
	   	  
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoGetBoundingBoxAction class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoGetBoundingBoxAction, SoAction);
		  enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
		    methods = new SoActionMethodList(SoAction.methods);                   
		    classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
		                                        new SbName("SoGetBoundingBoxAction"), null);
	   
	       //SO_ENABLE(SoGetBoundingBoxAction, SoViewportRegionElement);
	       SoGetBoundingBoxAction.enableElement(SoViewportRegionElement.class);

	   }
	public void close() {
	     if (resetPath != null)
	    	           resetPath.unref();
	    	  	}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Do a reset transform if the conditions are met
	   //
	   // Use: extender
	   
	  public void
	   checkResetBefore()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       checkReset(true);
	   }
	   
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Do a reset transform if the conditions are met
	   //
	   // Use: extender
	   
	   public void
	   checkResetAfter()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       checkReset(false);
	   }
	   
	   ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Do a reset transform if the conditions are met
	   //
	   // Use: public
	   
	   public void
	   checkReset(boolean _resetBefore)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       if (resetPath == null || _resetBefore != resetBefore)
	           return;
	   
	        SoPath curPath = getCurPath();
	   
	       if (curPath.operator_equals(resetPath)) {
	   
	           // Reset the transform if necessary. This has the side effect
	           // of clearing out (to identity) all currently open local
	           // matrices (instances of SoLocalBBoxMatrixElement). This is
	           // necessary because separators above the current node should
	           // NOT apply their local matrices to whatever we calculate
	           // here, since we reset the transformation.
	           if ((resetWhat.getValue() & ResetType.TRANSFORM.getValue())!=0)
	               SoBBoxModelMatrixElement.reset(state, curPath.getTail());
	   
	           // empty the bounding box if necessary
	           if ((resetWhat.getValue() & ResetType.BBOX.getValue())!=0)
	               box.makeEmpty();
	       }
	   }
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Extends the current bounding box.
//
// Use: extender

public void
extendBy(final SbBox3f b)
//
///////////////////////////////////////////////////////////////////////
{
	if( b.getMax().getX() == Float.MAX_VALUE) {
		int i=0;
	}
	
    SbXfBox3f   xfb = new SbXfBox3f(b);
    final SbMatrix    m = new SbMatrix(SoLocalBBoxMatrixElement.get(getState()));

    if (isInCameraSpace())
        m.multRight(SoViewingMatrixElement.get(getState()));

    xfb.transform(m);

    box.extendBy(xfb);
}

	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Extends the current bounding box.
//
// Use: extender

public void
extendBy( SbXfBox3f b)
//
///////////////////////////////////////////////////////////////////////
{
    final SbXfBox3f   xfb = new SbXfBox3f(b);
    final SbMatrix    m = new SbMatrix(SoLocalBBoxMatrixElement.get(getState()));

    if (isInCameraSpace())
        m.multRight(SoViewingMatrixElement.get(getState()));

    xfb.transform(m);

    box.extendBy(xfb);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

protected void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewportRegionElement.set(state, vpRegion);

    box.makeEmpty();
    resetCenter();

    traverse(node);
}
}
