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
 |      This file defines the SoAnnotation node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.port.Ctx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Annotation group node.
/*!
\class SoAnnotation
\ingroup Nodes
This group node delays rendering its children until all other nodes
have been traversed, turning off depth buffer comparisons first. The
result is that the shapes under the annotation node are rendered on
top of the rest of the scene. This node is derived from
SoSeparator, so it saves and restores traversal state for all
actions.


Note that if more than one annotation node is present in a graph, the
order in which they are traversed determines the stacking order em
later nodes are rendered on top of earlier ones. Also note that since
depth buffer comparisons are disabled, complex 3D objects may not be
rendered correctly when used under annotation nodes.


Also note that the annotation node does nothing special when picking
along a ray. That is, it does not modify the sorting order of
intersected objects based on which ones are under annotation nodes. If
your application uses annotation nodes and you want to ensure that
objects under them are picked "in front of" other objects, you can
tell the pick action that you want to pick all objects along the ray
and then scan through the paths in the resulting picked point
instances to see if any of them passes through an annotation
node. Your program can then decide what to do in such a case.

\par File Format/Default
\par
\code
Annotation {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Delays rendering its children until all other nodes have been traversed, turning off depth buffer comparisons first. 
\par
SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoRayPickAction, SoSearchAction
<BR> Same as SoSeparator 

*/
////////////////////////////////////////////////////////////////////////////////

public class SoAnnotation extends SoSeparator {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoAnnotation.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoAnnotation.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoAnnotation.class); }    	  	

	  
	      //! If true, the depth buffer is cleared before rendering the children
    public final SoSFBool clearDepthBuffer = new SoSFBool();

    //! If true, the depth buffer is disabled during rendering of the children
    public final SoSFBool disableDepthBuffer = new SoSFBool();

	  
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoAnnotation()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoAnnotation.class*/);

    nodeHeader.SO_NODE_ADD_FIELD(clearDepthBuffer,"clearDepthBuffer", (false));
    nodeHeader.SO_NODE_ADD_FIELD(disableDepthBuffer,"disableDepthBuffer", (true));

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
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
//    Traversal for GL rendering
//
// Use: extender

public void
GLRenderBelowPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    // If the action is currently rendering the delayed paths, turn
    // off depth buffer comparisons and render like a separator
    if (action.isRenderingDelayedPaths()) {
        boolean zbuffEnabled = (glIsEnabled(GL2.GL_DEPTH_TEST));
        if (zbuffEnabled && disableDepthBuffer.getValue())
            gl2.glDisable(GL2.GL_DEPTH_TEST);
        
        if (clearDepthBuffer.getValue())
            gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);

        super.GLRenderBelowPath(action);
        
        // turn the depth comparisons if it was originally turned on
        if (zbuffEnabled && disableDepthBuffer.getValue())
            gl2.glEnable(GL2.GL_DEPTH_TEST); 
    }

    // Otherwise, tell the render action that we want to render this
    // node last
    else {
        // Since we need to get a chance to add the path to the
        // action, make sure we're not in any caches
        SoCacheElement.invalidate(action.getState());
        action.addDelayedPath(action.getCurPath().copy());
    }
}

public void
GLRenderInPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    // If the action is currently rendering the delayed paths, turn
    // off depth buffer comparisons and render like a separator
    if (action.isRenderingDelayedPaths()) {
        boolean zbuffEnabled = (glIsEnabled(GL2.GL_DEPTH_TEST));
        if (zbuffEnabled && disableDepthBuffer.getValue())
            gl2.glDisable(GL2.GL_DEPTH_TEST);

        if (clearDepthBuffer.getValue())
            gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        
        super.GLRenderInPath(action);
        
        // turn the depth comparisons if it was originally turned on
        if (zbuffEnabled && disableDepthBuffer.getValue())
            gl2.glEnable(GL2.GL_DEPTH_TEST); 
    }

    // Otherwise, tell the render action that we want to render this
    // node last
    else {
        // Since we need to get a chance to add the path to the
        // action, make sure we're not in any caches
        SoCacheElement.invalidate(action.getState());
        action.addDelayedPath(action.getCurPath().copy());
    }
}
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoAnnotation class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoAnnotation.class, "Annotation", SoSeparator.class);
}

}
