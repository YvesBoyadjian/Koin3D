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
 |      This file defines the SoCallback node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.util.function.Consumer;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Provides custom behavior during actions.
/*!
\class SoCallback
\ingroup Nodes
This node provides a general mechanism for inserting callback functions
into a scene graph. The callback function registered with the node is
called each time the node is traversed while performing any scene graph
action. The callback function is passed a pointer to the action being
performed and a user data pointer registered with the callback function.
You can use this node to make nonstandard OpenGL calls while rendering. If
you do, be careful not to interfere with Inventor's use of OpenGL.


If you use a callback node for GL rendering, you should be careful
to follow render caching rules. If your callback node can make
different rendering calls each time it is traversed, it cannot be
cached. In such a case, the node should invalidate any open caches,
as in the following example:

<tt>\code
void
myCallbackFunc(void *d, SoAction *action) {
    if (action->isOfType(SoGLRenderAction::getClassTypeId())) {
	// Make my custom GL calls
	((MyClass *) d)->myRender();
	
	// Invalidate the state so that a cache is not made
	SoCacheElement::invalidate(action->getState());
    }
}
\endcode
</tt>

\par Action Behavior
\par
SoGLRenderAction, SoBoundingBoxAction, SoPickAction
<BR> Calls the specified callback function for all actions. 

\par See Also
\par
SoAction, SoCallbackAction, SoEventCallback
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCallback extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCallback.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCallback.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCallback.class); }    	  	
	

	private  SoCallbackCB        callbackFunc;          
	private     Object                callbackData;          
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoCallback()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoCallback*/);

    isBuiltIn    = true;
    callbackFunc = null;
    callbackData = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copies the contents of the given node into this instance.
//
// Use: protected, virtual

public void
copyContents( SoFieldContainer fromFC,
                         boolean copyConnections)
//
////////////////////////////////////////////////////////////////////////
{
    // Copy the usual stuff
    super.copyContents(fromFC, copyConnections);

    // Copy the callback function and data
    SoCallback fromCB = ( SoCallback ) fromFC;
    setCallback(fromCB.callbackFunc, fromCB.callbackData);
}

	
	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Typical action method.
	   //
	   // Use: extender
	   
	  public void
	   SoCallback_doAction(SoAction action)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoType      actionType = action.getTypeId();
	       SoState state = action.getState();
	   
	       if (this.callbackFunc != null)
	           this.callbackFunc.run(this.callbackData, action);
	   }
	  
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the GL render action
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Ask to be cached, to match Inventor 2.0 default:
    SoGLCacheContextElement.shouldAutoCache(action.getState(), SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the get bounding box action
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the get matrix action
//
// Use: extender

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the handle event thing
//
// Use: extender

public void
handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pick.
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does search...
//
// Use: extender

public void
search(SoSearchAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCallback_doAction(action);
    super.search(action);
}

	  
	   	
	// java port
	public void setCallback(Consumer<SoAction> func) {
		setCallback(new SoCallbackCB() {

			@Override
			public void run(Object userData, SoAction action) {
				func.accept(action);
			}
			
		},null);
	}
	
	/**
	 * Sets pointer to callback function and user data. 
	 * By default, the function pointer in the node is NULL and does nothing. 
	 * 
	 * @param func
	 * @param userData
	 */
	public void setCallback(SoCallbackCB func, Object userData) {
		 callbackFunc = func; callbackData = userData; 
	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoCallback class.
	   //
	   // Use: internal
	   	  
	  public static void initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoCallback.class, "Callback", SoNode.class);
	   }
	   }
