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
 * Copyright (C) 1995-96   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoLocateHighlight node class.
 |
 |   It has two fields, 
 |      color and style
 |
 |   When the cursor is over the stuff below this node,
 |   it will be redrawn (to the front buffer) with the color 
 |   emissive and/or diffuse in the state and the override flag ON.
 |
 |   Author(s)  : Alain Dumesny, Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

//import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.lwjgl.opengl.swt.GLCanvas;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoWindowElement;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;
import jscenegraph.port.GLXContext;



////////////////////////////////////////////////////////////////////////////////
//! Special separator that performs locate highlighting.
/*!
\class SoLocateHighlight
\ingroup Nodes
This is a subclass of SoSeparator that redraws itself in a different color
when the cursor is over the contents of the separator. 


The redraw happens for that separator only and not the entire window (redraw along 
the handle event pick path) and in the front buffer, to efficiently
track the mouse motion. The highlighted redraw overrides the emissive and/or diffuse
color of the subgraph based on the field values in this node.


NOTE: when using <tt>SoLightModel.BASE_COLOR</tt> (to turn lighting off) only the diffuse color
will be used to render objects, so <tt>EMISSIVE_DIFFUSE</tt> must be used for
this node to have any effect.

\par File Format/Default
\par
\code
LocateHighlight {
  renderCaching AUTO
  boundingBoxCaching AUTO
  renderCulling AUTO
  pickCulling AUTO
  mode AUTO
  style EMISSIVE
  color 0.300000011920929 0.300000011920929 0.300000011920929
}
\endcode

\par Action Behavior
\par
SoHandleEventAction
<BR> Checks to see if the cursor moves onto or off of the contents of the separator, and redraws appropriately (if \b mode  is <tt>AUTO</tt>), otherwise traverses  as a normal separator. 
\par
SoGLRenderAction
<BR> Redraws either highlighted (if cursor is over the contents of the separator when \b mode  == <tt>AUTO</tt> or always if \b mode  == <tt>ON</tt>), otherwise traverses  as a normal separator. 

\par See Also
\par
SoSeparator, SoSelection, SoMaterial
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoLocateHighlight extends SoSeparator {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLocateHighlight.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoLocateHighlight.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoLocateHighlight.class); }              
   
  public
  
      //! Possible values for draw style
      enum Styles {
          EMISSIVE(0),           //!< changes emissive color only (default)
          EMISSIVE_DIFFUSE(1);    //!< changes emissive and diffuse colors
          
          private int value;
          
          Styles(int value) {
        	  this.value = value;
          }
          
          public int getValue() {
        	  return value;
          }
      };
  
      //! Possible values for the mode
      public    enum Modes {
          AUTO(0),               //!< highlight when mouse is over (default)
          ON(1),                 //!< always highlight
          OFF(2);                 //!< never highlight
          
          private int value;
          
          Modes(int value) {
        	  this.value = value;
          }
          
          public int getValue() {
        	  return value;
          }
      };
  
      //! \name Fields
      //@{
  
      //! Highlighting color - default [.3, .3, .3]
      public final   SoSFColor   color = new SoSFColor();
  
      //! Highlighting draw style - default EMISSIVE
      public final  SoSFEnum    style = new SoSFEnum();
  
      //! Whether to highlight or not - default AUTO
      public final  SoSFEnum    mode = new SoSFEnum();
  	
	
      private boolean              highlightingPass;
      private static SoFullPath currentHighlightPath;
      private SoColorPacker       colorPacker;
      
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Constructor
	   //
	   // Use: public
	   //
	   public SoLocateHighlight()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
		   nodeHeader.SO_NODE_CONSTRUCTOR();
	   
		   nodeHeader.SO_NODE_ADD_SFIELD( mode, "mode",(Modes.AUTO.getValue()));
		   nodeHeader.SO_NODE_ADD_SFIELD( style, "style",(Styles.EMISSIVE.getValue()));
		   nodeHeader.SO_NODE_ADD_SFIELD( color, "color",new SbColor(.3f,.3f,.3f));
	  
	       // Set up static info for enum fields
		   nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Styles", "EMISSIVE",  Styles.EMISSIVE.getValue());
		   nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Styles", "EMISSIVE_DIFFUSE",   Styles.EMISSIVE_DIFFUSE.getValue());
		   nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Modes", "AUTO",   Modes.AUTO.getValue());
		   nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Modes", "ON",   Modes.ON.getValue());
		   nodeHeader.SO_NODE_DEFINE_ENUM_VALUE("Modes", "OFF",   Modes.OFF.getValue());
	   
	       // Set up info in enumerated type fields
		   nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style, "style",    "Styles");
		   nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(mode, "mode",     "Modes");
	   
	       isBuiltIn = true;
	   
	       // Locate highlighting vars
	       highlightingPass = false;
	       // make a colorPacker
	       colorPacker = new SoColorPacker();
	   }



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // If we're being deleted and we're the current highlight,
    // null out that variable
    if (currentHighlightPath != null &&
        (!currentHighlightPath.getTail().isOfType(SoLocateHighlight.getClassTypeId()))) {
//#ifdef DEBUG
//???    fprintf(stderr,"Removing current highlight because node was deleted\n");
//#endif
        currentHighlightPath.unref();
        currentHighlightPath = null;
    }
    colorPacker.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    redefine this from SoSeparator to also highlight.
//
// Use: extender

public void
GLRenderBelowPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    // Set up state for locate highlighting (if necessary)
    final int[] oldDepthFunc = new int[1];
    boolean drawHighlighted = preRender(action, oldDepthFunc);
    
    // now invoke the parent method
    super.GLRenderBelowPath(action);
    
    // Restore old depth buffer model if needed
    if (drawHighlighted || highlightingPass) {
    	GL2 gl2 = Ctx.get(action.getCacheContext()); // java port
        gl2.glDepthFunc(oldDepthFunc[0]);
    }
    // Clean up state if needed
    if (drawHighlighted)
        action.getState().pop();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    redefine this from SoSeparator to also highlight.
//
// Use: extender

public void
GLRenderInPath(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    // Set up state for locate highlighting (if necessary)
    final int[] oldDepthFunc = new int[1];
    boolean drawHighlighted = preRender(action, oldDepthFunc);
    
    // now invoke the parent method
    super.GLRenderInPath(action);
    
    // Restore old depth buffer model if needed
    if (drawHighlighted || highlightingPass) {
    	GL2 gl2 = Ctx.get(action.getCacheContext());
        gl2.glDepthFunc(oldDepthFunc[0]);
    }
    // Clean up state if needed
    if (drawHighlighted)
        action.getState().pop();
}

///////////////////////////////////////////////////////////////////
// Description:
//    called just before rendering - this will setup highlighting 
//  stuff if needed.
//
// Use: private

private boolean
preRender(SoGLRenderAction action, final int[] oldDepthFunc)
//
////////////////////////////////////////////////////////////////////////
{
    // If not performing locate highlighting, just return.
    if (mode.getValue() == Modes.OFF.getValue())
        return false;
    
    SoState state = action.getState();
    
    // ??? prevent caching at this level - for some reason the 
    // ??? SoWindowElement.copyMatchInfo() method get called, which should
    // ??? never be called. We are not caching this node correctly yet....
    SoCacheElement.invalidate(state);
    
    boolean drawHighlighted = (mode.getValue() == Modes.ON.getValue() || isHighlighted(action));
    
    if (drawHighlighted) {
        
        // prevent diffuse & emissive color from leaking out...
        state.push(); 
        
        SbColor col = color.getValue();
        
        // Emissive Color
        SoOverrideElement.setEmissiveColorOverride(state, this, true);
        SoLazyElement.setEmissive(state, col);
        
        // Diffuse Color
        if (style.getValue() == Styles.EMISSIVE_DIFFUSE.getValue()) {
            SoOverrideElement.setDiffuseColorOverride(state, this, true);
            SbColor[] colors = new SbColor[1];
            colors[0] = col;
            SoLazyElement.setDiffuse(state, this, 1, colors, colorPacker);
        }
    }

    // Draw on top of other things at same z-buffer depth if:
    // [a] we're highlighted
    // [b] this is the highlighting pass. This occurs when changing from
    //     non-hilit to lit OR VICE VERSA.
    // Otherwise, leave it alone...
    if (drawHighlighted || highlightingPass) {
    	
    	GL2 gl2 = state.getGL2();
    	
        gl2.glGetIntegerv(GL2.GL_DEPTH_FUNC, oldDepthFunc,0);
        if (oldDepthFunc[0] != GL2.GL_LEQUAL)
            gl2.glDepthFunc(GL2.GL_LEQUAL);
    }
    
    return drawHighlighted;
}

///////////////////////////////////////////////////////////////////
// handleEvent
//
//   Overloaded from SoSeparator to handle locate highlighting.
//

public void
handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // If we don't need to pick for locate highlighting, 
    // then just behave as separator and return.
    // NOTE: we still have to pick for ON even though we don't have
    // to re-render, because the app needs to be notified as the mouse
    // goes over locate highlight nodes.
    if ( mode.getValue() == Modes.OFF.getValue() ) {
        super.handleEvent( action );
        return;
    }

    // get event from the action
    final SoEvent event = action.getEvent();

    //
    // If this is a mouseMotion event, then check for locate highlighting
    //
    if (event.isOfType(SoLocation2Event.getClassTypeId())) {

        // check to see if the mouse is over our geometry...
        boolean underTheMouse = false;
        final SoPickedPoint pp = action.getPickedPoint();
        SoFullPath pPath = (pp != null) ? SoFullPath.cast ( pp.getPath()) : null;
        if (pPath != null && pPath.containsPath(action.getCurPath())) {
            // Make sure I'm the lowest LocHL in the pick path!
            underTheMouse = true;
            for (int i = 0; i < pPath.getLength(); i++) {
                SoNode node = pPath.getNodeFromTail(i);
                if (node.isOfType(SoLocateHighlight.getClassTypeId())) {
                    if (node != this)
                        underTheMouse = false;
                    break; // found the lowest LocHL - look no further
                }
            }
        }
        
        // Am I currently highlighted?
        if (isHighlighted(action)) {
            if ( ! underTheMouse)
                // re-draw the object with it's normal color
                redrawHighlighted(action, false);
            else
                action.setHandled();
        }
        // Else I am not currently highlighted
        else {
            // If under the mouse, then highlight!
            if (underTheMouse)
                // draw this object highlighted
                redrawHighlighted(action, true);
        }
    }
        
    // Let the base class traverse the children.
    if ( action.getGrabber() != this )
        super.handleEvent(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//     This is called by the handleEvent routine to hightlight/de-highlight
// ourself in the current window the mouse is over.
//
// Usage: private
private void
redrawHighlighted(
    SoAction action, boolean doHighlight)
//
////////////////////////////////////////////////////////////////////////
{
    // If we are about to highlight, and there is something else highlighted,
    // that something else needs to unhighlight.
    if (doHighlight && currentHighlightPath != null && 
        !(SoFullPath.cast (action.getCurPath()).operator_equals(currentHighlightPath))) {
        
        SoNode tail = currentHighlightPath.getTail();
        if (tail.isOfType( SoLocateHighlight.getClassTypeId()))
            ((SoLocateHighlight )tail).redrawHighlighted(action, false);
        else {
            // Just get rid of the path. It's no longer valid for redraw.
            currentHighlightPath.unref();
            currentHighlightPath = null;
        }
    }
    
    SoPath pathToRender;       
    // save the path to ourself for later de-highlight
    if (doHighlight) {

        if (currentHighlightPath != null)
            currentHighlightPath.unref();
        currentHighlightPath = SoFullPath.cast ( action.getCurPath().copy());
        currentHighlightPath.ref();
        
        // We will be rendering this new path to highlight it
        pathToRender = currentHighlightPath;
        pathToRender.ref();
    }
    // delete our path if we are no longer highlighted
    else {

        // We will be rendering this old path to unhighlight it
        pathToRender = currentHighlightPath;
        pathToRender.ref();
        
        currentHighlightPath.unref();
        currentHighlightPath = null;
    }

    // If highlighting is forced on for this node, we don't need this special render.
    if (mode.getValue() != Modes.AUTO.getValue()) {
        pathToRender.unref();
        return;
    }
    
    SoState state = action.getState();
    
    final GLCanvas[] window = new GLCanvas[1];
    final GLXContext[] context = new GLXContext[1];
    final Display[] display = new Display[1];
    final SoGLRenderAction[] glAction = new SoGLRenderAction[1];
    SoWindowElement.get(state, window, context, display, glAction);
    
    // If we don't have a current window, then simply return...
    if (window[0] == null || context == null || display == null || glAction == null)
        return;
    
    GL2 gl2 = state.getGL2();
    
    // set the current window
//#ifdef WIN32
//    wglMakeCurrent((HDC)display, (HGLRC)context);
//#elif defined(__APPLE__) && !defined(APPLE_GLX)
//    // (AGLDrawable)window
//    aglSetCurrentContext((AGLContext)context);
//#else
//    gl2.glXMakeCurrent(display, window, context);
//#endif
    
    // render into the front buffer (save the current buffering type)
    final int[] whichBuffer = new int[1];
    gl2.glGetIntegerv(GL2.GL_DRAW_BUFFER, whichBuffer,0);
    if (whichBuffer[0] != GL2.GL_FRONT)
        gl2.glDrawBuffer(GL2.GL_FRONT);

    highlightingPass = true;
    glAction[0].apply(pathToRender);
    highlightingPass = false;

    // restore the buffering type
    if (whichBuffer[0] != GL2.GL_FRONT)
        gl2.glDrawBuffer(whichBuffer[0]);
    gl2.glFlush();
    
    pathToRender.unref();
}

	   	
	
	/**
	 * This will de-highlight the currently highlighted node if any. 
	 * this should be called when the cursor leaves a window or a mode 
	 * changes happen which would prevent a highlighted node from 
	 * receiving more mouse motion events. 
	 * The GL render action used to render into that window needs to be 
	 * passed to correctly un-highlight. 
	 * 
	 * @param action
	 */
	public static void turnOffCurrentHighlight(SoGLRenderAction action) {
		
	     if (currentHighlightPath == null)
	    	           return;
	    	       
	    	       SoNode tail = currentHighlightPath.getTail();
	    	       if (tail.isOfType(SoLocateHighlight.getClassTypeId())) {
	    	           
	    	           // don't redraw if we already are in the middle of rendering
	    	           // (processing events during render abort might cause this)
	    	           SoState state = action.getState();
	    	           if (state != null && state.getDepth() == 1)
	    	               ((SoLocateHighlight )tail).redrawHighlighted(action, false);
	    	       }
	    	       else {
	    	           // Just get rid of the path. It's no longer valid for redraw.
	    	           currentHighlightPath.unref();
	    	           currentHighlightPath = null;
	    	       }
	    	  	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//     Returns TRUE if this node should be highlighted.
//
// Usage: private
private boolean
isHighlighted(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoFullPath actionPath = SoFullPath.cast ( action.getCurPath());
    return (currentHighlightPath != null &&
            currentHighlightPath.getTail() == actionPath.getTail() && // nested SoHL!
            currentHighlightPath.operator_equals(actionPath));
}
	
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Initialize the node
	    //
	    // Use: public, internal
	    //
	    
	  public static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	       SoSubNode.SO__NODE_INIT_CLASS(SoLocateHighlight.class, "LocateHighlight", SoSeparator.class);
	   }
	   	
}
