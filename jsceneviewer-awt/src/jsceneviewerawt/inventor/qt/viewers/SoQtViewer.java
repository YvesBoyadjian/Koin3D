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
 * Copyright (C) 1990-93   Silicon Graphics, Inc.
 * Author(s): Alain Dumesny
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */

package jsceneviewerawt.inventor.qt.viewers;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.misc.SoCallbackList;
import jscenegraph.database.inventor.misc.SoCallbackListCB;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoComplexity;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoLightModel;
import jscenegraph.database.inventor.nodes.SoLocateHighlight;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPackedColor;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSwitch;
import jscenegraph.port.Ctx;
import jsceneviewerawt.inventor.qt.SoQtCameraController;
import jsceneviewerawt.inventor.qt.SoQtCameraController.Type;
import jsceneviewerawt.inventor.qt.SoQtRenderArea;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Yves Boyadjian
 *
 */
//!      The Viewer component is the abstract base class for all viewers.
//!  It is subclassed from renderArea, adding viewing semantics to Inventor
//!  rendering.
public class SoQtViewer extends SoQtRenderArea {

	public

	    //! list of possible drawing styles
	    //!
	    //! Note: Refer to the SoQtViewer man pages for a complete
	    //! description of those draw styles.
	    enum DrawStyle {
	        VIEW_AS_IS,             //! unchanged
	        VIEW_HIDDEN_LINE,       //! render only the front most lines
	        VIEW_NO_TEXTURE,        //! render withought textures
	        VIEW_LOW_COMPLEXITY,    //! render low complexity and no texture
	        VIEW_LINE,              //! wireframe draw style
	        VIEW_POINT,             //! point draw style
	        VIEW_BBOX,              //! bounding box draw style
	        VIEW_LOW_RES_LINE,      //! low complexity wireframe + no depth clearing
	        VIEW_LOW_RES_POINT,     //! low complexity point + no depth clearing
	        VIEW_SAME_AS_STILL,     //! forces the INTERACTIVE draw style to match STILL
	        VIEW_WIREFRAME_OVERLAY  //! currently same effect as VIEW_LINE
	    };
	    public enum DrawType {
	        STILL,                  //! default to VIEW_NO_TEXTURE (or VIEW_AS_IS)
	        INTERACTIVE             //! default to VIEW_SAME_AS_STILL
	    };

	    //! list of different buffering types
	    public enum BufferType {
	        BUFFER_SINGLE,
	        BUFFER_DOUBLE,
	        BUFFER_INTERACTIVE      //! changes to double only when interactive
	    };
	    
	  //! callback function prototypes
	    public interface SoQtViewerCB {
	    	void run ( Object userData, SoQtViewer viewer);
	    }

	    //! global vars
	    protected boolean          viewingFlag;        //! false when the viewer is off
	    protected boolean          altSwitchBack;      //! flag to return to PICK after an Alt release
	    protected boolean          cursorEnabledFlag;
	    protected static SoSFTime viewerRealTime;    //! pointer to "realTime" global field
	    protected float           sceneSize;          //! the larger of the X,Y,Z scene BBox
	    protected float           viewerSpeed;        //! default to 1.0 - SoXtFullViewer add UI to inc/dec

	    //! local tree variables
	    protected SoSeparator     sceneRoot;         //! root node given to the RA

	    private
	        //! current state vars
	        BufferType      bufferType;
	    private boolean          interactiveFlag;    //! true while doing interactive work
	    private float           stereoOffset;
	    private boolean          altSwitchingEnabled; // if true, alt key switches between viewing and picking
	    private boolean          keyCommandsEnabled;  // if true, cursor keys manipulate camera (and S sets seek mode)

	        //! draw style vars
	    private DrawStyle       stillDrawStyle, interactiveDrawStyle;
	    private SoSwitch        drawStyleSwitch;   //! on/off draw styles
	    private SoDrawStyle     drawStyleNode;     //! LINE vs POINT
	    private SoLightModel    lightModelNode;    //! BASE_COLOR vs PHONG
	    private SoPackedColor   colorNode;         //! hidden line first pass
	    private SoMaterialBinding   matBindingNode; //! hidden line first pass
	    private SoComplexity    complexityNode;    //! low complexity & texture off

	        //! interactive viewing callbacks
	        private int             interactiveCount;
	        private SoCallbackList  startCBList;
	        private SoCallbackList  finishCBList;
	        
	    protected SoQtCameraController cameraController;

	    //! Constructor/Destructor
	    public SoQtViewer() {
	    	this(Type.BROWSER);
	    }
	    public SoQtViewer( Type type) {
	    	this(type, null);
	    }
	    public SoQtViewer( Type type, Container parent) {
	    	this(type, parent, 0);
	    }
	    
/*!
  Constructor. \a parent, \a name and \a embed are passed on to
  SoQtRenderArea, so see the documentation for our parent
  constructor for for more information on those.

  The \a t type setting hints about what context the viewer will be
  used in.  Usually not very interesting for the application
  programmer, but if you want to make sure the SoQtViewer class
  doesn't muck about with your supplied scenegraph, set the type-flag
  to SoQtViewer::BROWSER.  (This "feature" of the viewer is
  included just to be compatible with the old SGI Inventor API.)

  The \a build flag decides whether or not to delay building the
  widgets / window which is going to make up the components of the
  viewer.
*/
	    public SoQtViewer(Container parent,
                             String name,
                             boolean embed,
                             Type t,
                             boolean build) {
	    	this(t,parent,0,build);
	    }
	    public SoQtViewer (Type type, Container parent, int style, boolean build) {
	    	super(parent, style, build);
	    	constructorCommon(type);
	    }
	    	
	    	    	    
	    public SoQtViewer (Type type, Container parent, int style) {
	    	super(parent, style, false);
	    	constructorCommon(type);
	    }
	    	
	    private void constructorCommon(Type type) {
	    	cameraController = new SoQtCameraController(type) {

				@Override
				protected void animationStarted() {
					SoQtViewer.this.animationStarted();
				}

				@Override
				protected void animationFinished() {
					SoQtViewer.this.animationFinished();
				}

				@Override
				public void setCamera(SoCamera newCamera, boolean cameraCreated) {
					SoQtViewer.this.setCamera(newCamera, cameraCreated);
				}

				@Override
				public void setSeekMode(boolean flag) {
					SoQtViewer.this.setSeekMode(flag);
				}
	    		
	    	};
	    	
	        // init local vars
	        viewingFlag = true;
	        altSwitchBack = false;
	        altSwitchingEnabled = true;
	        keyCommandsEnabled = true;
	        cursorEnabledFlag = true;
	        interactiveFlag = false;
	        startCBList = new SoCallbackList();
	        finishCBList = new SoCallbackList();
	        interactiveCount = 0;
	        bufferType = isDoubleBuffer() ? BufferType.BUFFER_DOUBLE : BufferType.BUFFER_SINGLE;
	        stereoOffset = 3.0f;
	        sceneSize = 0.0f;    // not computed yet.
	        viewerSpeed = 1.0f;  // default. SoQtFullViewer add UI to increase/decrease

	        if ( viewerRealTime == null) {
	            viewerRealTime = (SoSFTime ) SoDB.getGlobalField(new SbName("realTime"));
	        }

	        //
	        // build the small internal graph (nodes used for draw style stuff)
	        //
	        sceneRoot           = new SoSeparator(4);
	        drawStyleSwitch     = new SoSwitch(6);
	        drawStyleNode       = new SoDrawStyle();
	        lightModelNode      = new SoLightModel();
	        colorNode           = new SoPackedColor();
	        matBindingNode      = new SoMaterialBinding();
	        complexityNode      = new SoComplexity();

	        // note: we cannot setSceneGraph on the renderArea in the constructor
	        // since it calls virtual functions, and all of our members aren't
	        // initialized yet. We'll call it the first time our setSceneGraph
	        // is called.
	        sceneRoot.ref();
	        sceneRoot.renderCaching.setValue(SoSeparator.CacheEnabled.OFF); // no caching there
	        sceneRoot.renderCulling.setValue(SoSeparator.CacheEnabled.OFF); // no culling there
	        sceneRoot.addChild(drawStyleSwitch);
	        drawStyleSwitch.addChild(drawStyleNode);
	        drawStyleSwitch.addChild(lightModelNode);
	        drawStyleSwitch.addChild(colorNode);
	        drawStyleSwitch.addChild(matBindingNode);
	        drawStyleSwitch.addChild(complexityNode);

	        // set the draw style vars and fields that don't change - once we
	        // have a context, will will use glGetString() to pick a better default
	        // draw style.
	        stillDrawStyle = DrawStyle.VIEW_AS_IS;
	        interactiveDrawStyle = DrawStyle.VIEW_SAME_AS_STILL;
	        drawStyleSwitch.whichChild.operator_assign(SoSwitch.SO_SWITCH_NONE);

	        drawStyleNode.setOverride(true); // only use style field
	        drawStyleNode.pointSize.operator_assign(3.0f);
	        drawStyleNode.lineWidth.setIgnored(true);
	        drawStyleNode.linePattern.setIgnored(true);

	        lightModelNode.setOverride(true);

	        colorNode.setOverride(true);

	        matBindingNode.setOverride(true);
	        matBindingNode.value.operator_assign(SoMaterialBinding.Binding.OVERALL.getValue());

	        complexityNode.setOverride(true);
	        complexityNode.textureQuality.operator_assign(0f); // always turn texture off under switch
	        complexityNode.value.operator_assign(0.15f);
	        complexityNode.textureQuality.setIgnored(true);

	        addStartCallback(SoQtViewer::drawStyleStartCallback);
	        addFinishCallback(SoQtViewer::drawStyleFinishCallback);

	        cameraController.setSceneRoot(sceneRoot, 1);
	    }
	    

public void destructor()
{
    // detach everything
    if ( cameraController.sceneGraph != null ) {
        setSceneGraph(null);
    }
    sceneRoot.unref();

    // delete everything
    startCBList.destructor(); startCBList = null;
    finishCBList.destructor(); finishCBList = null;
    cameraController.destructor();
    super.destructor();
}

	    

	        //! add/remove start and finish callback routines on the viewer. Start callbacks will
	        //! be called whenever the user starts doing interactive viewing (ex: mouse
	        //! down), and finish callbacks are called when user is done doing
	        //! interactive work (ex: mouse up).
	        //!
	        //! Note: The viewer pointer 'this' is passed as callback data
	    public void addStartCallback(SoQtViewerCB f) {
	    	addStartCallback(f, null);
	    }
	    public void    addStartCallback(SoQtViewerCB f, Object userData)
	                { startCBList.addCallback(new SoCallbackListCB() {

						@Override
						public void invoke(Object callbackData) {
							f.run(callbackData, SoQtViewer.this);
						}
	                	
	                }, userData); }
	    
	    public void addFinishCallback(SoQtViewerCB f) {
	    	addFinishCallback(f, null);
	    }
	        public void    addFinishCallback(SoQtViewerCB f, Object userData)
	                { finishCBList.addCallback(new SoCallbackListCB() {

						@Override
						public void invoke(Object callbackData) {
							f.run(callbackData, SoQtViewer.this);
						}
	                	
	                }, userData); }
	        
		    public void removeStartCallback(SoQtViewerCB f) {
		    	removeStartCallback(f, null);
		    }
	        public void    removeStartCallback(SoQtViewerCB f, Object userData)
	                { startCBList.removeCallback(new SoCallbackListCB() {

						@Override
						public void invoke(Object callbackData) {
							f.run(callbackData, SoQtViewer.this);
						}
	                	
	                }, userData); }
	        
		    public void removeFinishCallback(SoQtViewerCB f) {
		    	removeFinishCallback(f, null);
		    }
	        public void    removeFinishCallback(SoQtViewerCB f, Object userData)
	                { finishCBList.removeCallback(new SoCallbackListCB() {

						@Override
						public void invoke(Object callbackData) {
							f.run(callbackData, SoQtViewer.this);
						}
	                	
	                }, userData); }

	        //! This can be used to let the viewer know that the scene graph
	        //! has changed so that the viewer can recompute things like speed which
	        //! depend on the scene graph size.
	        //!
	        //! NOTE: This routine is automatically called whenever setSceneGraph()
	        //! is called.
	        public void    recomputeSceneSize() {
	            if ( cameraController.sceneGraph == null || sceneRoot == null) {
	                sceneSize = 0.0f;
	                return;
	            }

	            // Use assignment notation to disambiguate from expression (edison)
	            SoGetBoundingBoxAction bboxAct = new SoGetBoundingBoxAction(new SbViewportRegion(getGlxSize()));
	            bboxAct.apply(sceneRoot);
	            SbBox3f bbox = bboxAct.getBoundingBox();

	            if (bbox.isEmpty()) {
	                sceneSize = 0.0f;
	                return;
	            }

	            final float[] xyz = new float[3];
	            bbox.getSize( xyz);
	            sceneSize = (xyz[0] > xyz[2]) ? xyz[0] : xyz[2];
	            if (xyz[1] > sceneSize) {
	                sceneSize = xyz[1];
	            }
	            if (sceneSize <= 0.0) {
	                sceneSize = 0.0f;
	            }	        	
	            bboxAct.destructor();
	        }


private static void drawStyleStartCallback(Object object, SoQtViewer v)
{
    v.interactiveFlag = true;  // must happen first

    // if still and move draw styles are the same, return...
    if (v.interactiveDrawStyle == DrawStyle.VIEW_SAME_AS_STILL ||
        v.interactiveDrawStyle == v.stillDrawStyle)
    {
        return;
    }

    // if we have "MOVE NO TEXTURE", then we have nothing
    // to do if we have a current draw style (since they all have
    // texturing turned off in the first place).
    if (v.interactiveDrawStyle == DrawStyle.VIEW_NO_TEXTURE &&
        v.stillDrawStyle != DrawStyle.VIEW_AS_IS)
    {
        return;
    }

    v.setCurrentDrawStyle(v.interactiveDrawStyle);
}

private static void drawStyleFinishCallback(Object object, SoQtViewer v)
{
    v.interactiveFlag = false;  // must happen first

    // if still and move draw styles are the same, return...
    if (v.interactiveDrawStyle == DrawStyle.VIEW_SAME_AS_STILL ||
        v.interactiveDrawStyle == v.stillDrawStyle)
    {
        return;
    }

    // if we have "MOVE NO TEXTURE", then we have nothing
    // to do if we have a current draw style (since they all have
    // texturing turned off in the first place).
    if (v.interactiveDrawStyle == DrawStyle.VIEW_NO_TEXTURE &&
        v.stillDrawStyle != DrawStyle.VIEW_AS_IS)
    {
        return;
    }

    v.setCurrentDrawStyle(v.stillDrawStyle);
}


private void setCurrentDrawStyle(SoQtViewer.DrawStyle style)
{
    if (style != DrawStyle.VIEW_AS_IS) {
        drawStyleSwitch.whichChild.operator_assign(SoSwitch.SO_SWITCH_ALL);
    }
    switch(style) {
        case VIEW_AS_IS:
            drawStyleSwitch.whichChild.operator_assign(SoSwitch.SO_SWITCH_NONE);
            break;

        case VIEW_HIDDEN_LINE:
            // texture is always off under the switch node.
            // List only stuff common to both rendering passes
            // (the rest is done when rendering)
            drawStyleNode.style.setIgnored(false);
            drawStyleNode.pointSize.setIgnored(true);
            lightModelNode.model.operator_assign(SoLightModel.Model.BASE_COLOR.getValue());
            lightModelNode.model.setIgnored(false);
            complexityNode.type.setIgnored(true);
            complexityNode.value.setIgnored(true);
            complexityNode.textureQuality.setIgnored(false);
            break;

        case VIEW_NO_TEXTURE:
        case VIEW_LOW_COMPLEXITY:
            // texture is always off under the switch node
            drawStyleNode.style.setIgnored(true);
            drawStyleNode.pointSize.setIgnored(true);
            lightModelNode.model.setIgnored(true);
            colorNode.orderedRGBA.setIgnored(true);
            matBindingNode.value.setIgnored(true);
            complexityNode.type.setIgnored(true);
            complexityNode.value.setIgnored(style != DrawStyle.VIEW_LOW_COMPLEXITY);
            complexityNode.textureQuality.setIgnored(style == DrawStyle.VIEW_LOW_COMPLEXITY);
            break;

        case VIEW_LINE:
        case VIEW_LOW_RES_LINE:
        case VIEW_POINT:
        case VIEW_LOW_RES_POINT:
        case VIEW_WIREFRAME_OVERLAY:
            // texture is always off under the switch node
            drawStyleNode.style.operator_assign((style == DrawStyle.VIEW_LINE || style == DrawStyle.VIEW_LOW_RES_LINE) ?
                SoDrawStyle.Style.LINES.getValue() : SoDrawStyle.Style.POINTS.getValue());
            drawStyleNode.style.setIgnored(false);
            drawStyleNode.pointSize.setIgnored(style != DrawStyle.VIEW_POINT && style != DrawStyle.VIEW_LOW_RES_POINT);
            lightModelNode.model.operator_assign(SoLightModel.Model.BASE_COLOR.getValue());
            lightModelNode.model.setIgnored(false);
            colorNode.orderedRGBA.setIgnored(true);
            matBindingNode.value.setIgnored(true);

            // Force a lower complexity for the low res draw styles
            // ??? this only works if the object didn't have
            // ??? something lower in the first place...
            if (style == DrawStyle.VIEW_LOW_RES_LINE || style == DrawStyle.VIEW_LOW_RES_POINT) {
                complexityNode.type.operator_assign(SoComplexity.Type.OBJECT_SPACE.getValue());
                complexityNode.type.setIgnored(false);
                complexityNode.value.setIgnored(false);
            } else {
                complexityNode.type.setIgnored(true);
                complexityNode.value.setIgnored(true);
            }
            complexityNode.textureQuality.setIgnored(false);
            break;

        case VIEW_BBOX:
            // texture is always off under the switch node
            drawStyleNode.style.operator_assign(SoDrawStyle.Style.LINES.getValue());
            drawStyleNode.style.setIgnored(false);
            drawStyleNode.pointSize.setIgnored(true);
            lightModelNode.model.operator_assign(SoLightModel.Model.BASE_COLOR.getValue());
            lightModelNode.model.setIgnored(false);
            colorNode.orderedRGBA.setIgnored(true);
            matBindingNode.value.setIgnored(true);
            complexityNode.type.operator_assign(SoComplexity.Type.BOUNDING_BOX.getValue());
            complexityNode.type.setIgnored(false);
            complexityNode.value.setIgnored(true);
            complexityNode.textureQuality.setIgnored(false);
            break;

        case VIEW_SAME_AS_STILL:
//#ifdef DEBUG
            SoDebugError.post("SoQtViewer::setCurrentDrawStyle", "VIEW_SAME_AS_STILL was passed !");
//#endif
            break;
    }

    setZbufferState();
}


public void actualRedraw()
{
    if (cameraController.isAutoClipping() && ! isStereoViewing()) {
    	cameraController.adjustCameraClippingPlanes();
    }

    // update the headlight if necessary
    cameraController.updateHeadlight();

    // make sure that we have a valid sceneSize value - but don't compute
    // a new sceneSize value for every redraw since the walking speed should
    // really be constant.
    if (sceneSize == 0.0) {
        recomputeSceneSize();
    }

    // Check to see if we are in stereo mode, if so draw the scene
    // twice with the camera offseted between the two views, else
    // do a simple redraw.
    
    SoCamera camera = cameraController.getCamera();

    if ( isStereoViewing() && camera != null) {

        // Check the camera type, since stereo is different:
        //
        // Ortho camera: stereo is accomplished by simply rorating
        // the camera (around the point of interest) by 6 degree.
        //
        // Perspective camera: we translate the camera and rotate
        // them to look at the same point of interest (idealy we also would
        // make sure the plane of convergence is exactly the same for
        // both perspective views, unfortunatly we cannot do this with
        // the current symetric view volumes).
        //

        // save the camera original values to restore the camera after
        // both views are rendered. This means we will use this in between
        // left and right view for things like picking.
        final SbVec3f     camOrigPos = camera.position.getValue();
        final SbRotation  camOrigRot = camera.orientation.getValue();

        // get the camera focal point
        final SbMatrix mx = new SbMatrix();
        mx.copyFrom(camOrigRot);
        final SbVec3f forward = new SbVec3f( -mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        float radius = camera.focalDistance.getValue();
        final SbVec3f center = camOrigPos.operator_add( forward.operator_mul(radius));


        //
        // change the camera for the LEFT eye view, and render
        //
        getShareID().glDrawBuffer( isDoubleBuffer() ? GL2.GL_BACK_LEFT : GL2.GL_FRONT_LEFT);
        // rotate the camera by - stereoOffset/2 degrees
        camera.orientation.setValue(
            new SbRotation(new SbVec3f(0, 1, 0), - stereoOffset * (float)Math.PI / 360.0f).operator_mul(camOrigRot));

        // reposition camera to look at pt of interest
        mx.copyFrom( camera.orientation.getValue());
        forward.setValue( -mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        camera.position.setValue(center.operator_minus(forward.operator_mul(radius)));

        if (cameraController.isAutoClipping()) {
        	cameraController.adjustCameraClippingPlanes();
        }
        doRendering();

        //
        // change the camera for the RIGHT eye view, and render
        //
        getShareID().glDrawBuffer( isDoubleBuffer() ? GL2.GL_BACK_RIGHT : GL2.GL_FRONT_RIGHT);
        // rotate the camera by + stereoOffset/2 degrees
        camera.orientation.setValue(
            new SbRotation(new SbVec3f(0, 1, 0), stereoOffset * (float)Math.PI / 360.0f).operator_mul( camOrigRot));

        // reposition camera to look at pt of interest
        mx.copyFrom( camera.orientation.getValue());
        forward.setValue( -mx.getValue()[2][0], -mx.getValue()[2][1], -mx.getValue()[2][2]);
        camera.position.setValue(center.operator_minus( forward.operator_mul(radius)));

        if (cameraController.isAutoClipping()) {
        	cameraController.adjustCameraClippingPlanes();
        }
        doRendering();

        // reset the camera original values now that we are done rendering
        // the stereo views (leave aspect ratio to do correct picking).
        camera.enableNotify(false); // don't cause a redraw
        camera.position.setValue( camOrigPos);
        camera.orientation.setValue( camOrigRot);
        camera.enableNotify(true);

        // restore to draw to both buffer (viewer feedback)
        getShareID().glDrawBuffer( isDoubleBuffer() ? GL2.GL_BACK : GL2.GL_FRONT);
    } else {
        // not stereo viewing, so do the regular rendering....
        doRendering();
    }
}


public void doRendering()
{
    // check if we need two pass rendering for hidden line rendering
    boolean drawHiddenLine =
        (stillDrawStyle == DrawStyle.VIEW_HIDDEN_LINE && (! interactiveFlag ||
                                interactiveDrawStyle == DrawStyle.VIEW_NO_TEXTURE ||
                                interactiveDrawStyle == DrawStyle.VIEW_LOW_COMPLEXITY ||
                                interactiveDrawStyle == DrawStyle.VIEW_SAME_AS_STILL))
        || (interactiveFlag && interactiveDrawStyle == DrawStyle.VIEW_HIDDEN_LINE);

    SoCamera camera = cameraController.getCamera();
    
    if (camera != null && drawHiddenLine) {

        // ??? what do we do about highlights ??

        // the smaller the near clipping plane is relative to the far
        // plane, the smaller the zbuffer offset needs to be (because
        // the granularity will be pretty big). The closer the clipping
        // planes are relative to each other, the bigger the zbuffer offset
        // needs to be (because the zbuffer granularity will be small).
        // The scale factor was found empirically to work best with the
        // current settings of near/far.
        float zOffset = camera.nearDistance.getValue() /
            (40 * camera.farDistance.getValue());

        // render the first pass as solid, using the background color
        // for the object base color.
        drawStyleNode.style.setValue( SoDrawStyle.Style.FILLED);
        colorNode.orderedRGBA.setValue( getSceneHandler().getBackgroundColor().getPackedValue());
        colorNode.orderedRGBA.setIgnored(false);
        matBindingNode.value.setIgnored(false);

        // ??? this should match the SoQtRenderArea::actualRedraw()
        // ??? method exactly (apart for not clearing the z-buffer)
        getShareID().glDepthRange(zOffset, 1); // enable wireframe to be draw on top
        getSceneHandler().getSceneManager().render(getSceneHandler().isClearBeforeRender(), true);

        // render the second pass as wireframe
        // (the first pass rendered the objects solid with base color
        // set to the background color to set the zbuffer values)
        drawStyleNode.style.setValue( SoDrawStyle.Style.LINES);
        colorNode.orderedRGBA.setIgnored(true);
        matBindingNode.value.setIgnored(true);

        // ??? this should match the SoQtRenderArea::actualRedraw()
        // ??? method exactly (apart for not clearing the color and z-buffer)
        getShareID().glDepthRange(0,1-zOffset); // enable wireframe to be draw on top
        getSceneHandler().getSceneManager().render(false, false);

        getShareID().glDepthRange(0, 1); // restore the range
    } else {
        // ??? this should match the SoQtRenderArea::actualRedraw()
        // ??? method exactly (apart for not clearing the z-buffer)
    	getSceneHandler().getSceneManager().render(getSceneHandler().isClearBeforeRender(), ! isZbufferOff());
    }
}



private boolean isZbufferOff()
{
    DrawStyle style = (interactiveFlag ? interactiveDrawStyle : stillDrawStyle);
    if (interactiveFlag && interactiveDrawStyle == DrawStyle.VIEW_SAME_AS_STILL) {
        style = stillDrawStyle;
    }

    // for these draw styles, turn the zbuffer off
    return (style == DrawStyle.VIEW_LOW_RES_LINE || style == DrawStyle.VIEW_LOW_RES_POINT
        || style == DrawStyle.VIEW_BBOX);
}


private void setZbufferState()
{
    if (getGLWidget() == null) {
        return;
    }

    makeCurrent();

    if (isZbufferOff()) {
    	Ctx.get(shareID).glDisable(GL2.GL_DEPTH_TEST);
    } else {
    	Ctx.get(shareID).glEnable(GL2.GL_DEPTH_TEST);
    }
}


protected void processEvent (ComponentEvent anyEvent, EventType type, final boolean[] isAccepted)
{
    boolean isMouseEvent = false;
    // check for special key which turns viewing on/off
    if (type == EventType.KEY_EVENT_KEY_PRESSED) {
        KeyEvent ke = (KeyEvent)anyEvent;
        //int modifiers = ke.stateMask & SWT.MODIFIER_MASK;
        //int buttons = ke.stateMask & SWT.BUTTON_MASK;
        if (keyCommandsEnabled && (ke.getKeyCode() == KeyEvent.VK_ESCAPE/* Qt::Key_Escape*/)) {
            setViewing( !isViewing());  // toggle the viewing mode...
            isAccepted[0] = true;//ke.accept();
        } else if (altSwitchingEnabled && !altSwitchBack && (ke.getKeyCode() == KeyEvent.VK_ALT/*Qt::Key_Alt*/)) {
            // Alt-key goes from PICK to VIEW if
            // 1] we are not in VIEW mode already
            // 2] no mouse buttons are pressed
            altSwitchBack = true;   // later return back
            setViewing(!isViewing());
        }
    } else if (type == EventType.KEY_EVENT_KEY_RELEASED) {
        KeyEvent ke = (KeyEvent )anyEvent;
        //int modifiers = ke.stateMask & SWT.MODIFIER_MASK;
        //int buttons = ke.stateMask & SWT.BUTTON_MASK;
        if (altSwitchBack && (ke.getKeyCode() == KeyEvent.VK_ALT/*Qt::Key_Alt*/)) {
            // if Alt-key, then return to PICK (if we had switched)
            setViewing(!isViewing());
            altSwitchBack = false;  // clear the flag
        }
    } else if (type == EventType.MOUSE_EVENT_MOUSE_ENTER) {
        // because the application might use Alt-key for motif menu
        // accelerators we might not receive a key-up event, so make sure
        // to reset any Alt mode (temporary viewing) when the mouse re-enters
        // the window.
        int modifiers = ((MouseEvent)anyEvent).getModifiersEx();//QApplication::keyboardModifiers();
        //int buttons = ((MouseEvent)anyEvent).stateMask & SWT.BUTTON_MASK;
        if (altSwitchingEnabled && !altSwitchBack && (modifiers & MouseEvent.ALT_DOWN_MASK/*Qt::AltModifier*/)!=0) {
            altSwitchBack = true;   // later return back
            setViewing(!isViewing());
        } else if (altSwitchBack && (modifiers & MouseEvent.ALT_DOWN_MASK/*Qt::AltModifier*/)==0) {
            setViewing(!isViewing());
            altSwitchBack = false;  // clear the flag
        }
    } else if (type == EventType.MOUSE_EVENT_MOUSE_DOWN ||
               type == EventType.MOUSE_EVENT_MOUSE_UP ||
               type == EventType.MOUSE_EVENT_DOUBLE_CLICK ||
               type == EventType.MOUSE_EVENT_MOUSE_MOVE)
    {
        isMouseEvent = true;
    }

    // send the event to the scene graph and application callback if viewing
    // is (temporarily) off or if it is a non-mouse event we didn't use
    if ( (isCurrentlyPicking() || !isMouseEvent) && !isAccepted[0]) {
        super.processEvent (anyEvent, type, isAccepted);
    }
    if (getCameraController().getCamera() != null && keyCommandsEnabled && 
        !isAccepted[0] && type == EventType.KEY_EVENT_KEY_PRESSED)
    {
        // additional camera control keys
        KeyEvent ke = (KeyEvent )anyEvent;
        switch ( ke.getKeyCode() ) {
            case KeyEvent.VK_HOME:
            	getCameraController().resetToHomePosition();
            	isAccepted[0] = true;//ke.accept();
                break;
            case KeyEvent.VK_S:
                setSeekMode( !getCameraController().isSeekMode());
                // ??? this is kind of a hack, but it is needed
                // ??? until a better solution is found
                if ( getCameraController().isSeekMode() && interactiveCount != 0 ) {
                    interactiveCount = 0;
                    finishCBList.invokeCallbacks(this);
                }
                isAccepted[0] = true;//ke.accept();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_DOWN:
                getCameraController().arrowKeyPressed (ke.getKeyCode());
                getSceneHandler().scheduleRedraw();
                isAccepted[0] = true;//ke.accept();
                break;
        }
    }
}

protected void    animationStarted()  { interactiveCountInc(); }
protected void    animationFinished() { interactiveCountDec(); }



public void resizeGL (GL2 gl2, int width, int height)
{
    super.resizeGL(gl2, width, height);
    cameraController.setSceneSize(new SbVec2s((short)width, (short)height));
}


public void setSceneGraph(SoNode newScene)
{
    // if we haven't already given the render area a scene graph sceneRoot,
    // give it the scene graph now. This is a one shot deal, which
    // cannot be done in the constructor.
    if (super.getSceneHandler().getSceneGraph() == null) {
        super.getSceneHandler().setSceneGraph(sceneRoot);
    }

    cameraController.setSceneGraph(newScene);

    // recompute the scene size variables...
    recomputeSceneSize();
}


public void setCamera(SoCamera newCamera, boolean createdCamera)
{
	SoCamera camera = getCameraController().getCamera();
	
    // check for trivual return
    if (camera == newCamera) {
        return;
    }
    if (camera != null) {
        if (viewingFlag) {
            setViewing(false);
            viewingFlag = true;  // can later be turned on
        }
    }
    getCameraController().doSetCamera(newCamera, createdCamera);
    camera = getCameraController().getCamera();
    if (camera != null) {
        if (viewingFlag) {
            viewingFlag = false;  // enables the routine to be called
            setViewing(true);
        }
    }
}



//! Set and get the feature that the alt key switches between viewing
//! and picking mode. Default is ON
public void    setAltSwitchingEnabled (boolean on) {
    altSwitchingEnabled = on;	
}
public boolean            isAltSwitchingEnabled() { return altSwitchingEnabled; };

//! Set and get the feature that the cursor keys manipulate the camera,
//! S requests seek mode, and ESC switches picking mode. Default is ON
public  void    setKeyCommandsEnabled (boolean on) {
    keyCommandsEnabled = on;
}
public boolean            isKeyCommandsEnabled() { return keyCommandsEnabled; };



//! Set/get whether the viewer is turned on or off. When turned off
//! events over the renderArea are sent down the sceneGraph
//! (picking can occurs). (default viewing is ON)
	public void    setViewing (boolean flag) {
	    if (flag == viewingFlag) {
	        return;
	    }

	    viewingFlag = flag;

	    // if we are goind into viewing mode, then de-highlight any
	    // currently highlighted nodes (since the object will never receive
	    // any motion events).
	    if (viewingFlag) {
	        SoGLRenderAction glAct = getSceneHandler().getGLRenderAction();
	        if (glAct != null) {
	            SoLocateHighlight.turnOffCurrentHighlight(glAct);
	        }
	    }		
	}
	public boolean          isViewing()        { return viewingFlag; };



public void setCursorEnabled(boolean flag)
{
    cursorEnabledFlag = flag;
}


public void setAutoClipping(boolean flag)
{
    if (cameraController.isAutoClipping() == flag) { return; }

    cameraController.setAutoClipping(flag);

    // cause a redraw to correctly place the near and far plane now that
    // auto clipping is on.
    if (cameraController.isAutoClipping()) {
        getSceneHandler().scheduleRedraw();
    }
}


public void setStereoViewing(boolean flag)
{
    if (flag != isStereoViewing()) {
        setStereoBuffer(flag);
    }
}


public boolean isStereoViewing()
{
    // done in SoXtGLWidget
    return isStereoBuffer();
}


	
    //! Invokes the start and finish viewing callbacks. Subclasses NEED to call
    //! those routines when they start and finish doing interactive viewing
    //! operations so that correct interactive drawing style and buffering
    //! types, as well as application callbacks, gets set and called properly.
    //!
    //! Those routines simply increment and decrement a counter. When the counter
    //! changes from 0->1 the start viewing callbacks are called. When the counter
    //! changes back from.->0 the finish viewing callbacks are called.
    //! The counter approach enables different parts of a viewer to call those
    //! routines withough having to know about each others (which is not
    //!
    protected void            interactiveCountInc() {
        interactiveCount++;

        if (interactiveCount == 1) {
            startCBList.invokeCallbacks(this);
        }

    }
    protected void            interactiveCountDec() {
        if (interactiveCount > 0) {
            interactiveCount--;
            if (interactiveCount == 0) {
                finishCBList.invokeCallbacks(this);
            }
        }

    }
    protected int             getInteractiveCount()   { return interactiveCount; }


    public SoQtCameraController getCameraController() {
    	return cameraController;
    }

    //! Proxy slots for methods in SoQtCameraController:
    public void    toggleCameraTypeSlot()    { cameraController.toggleCameraType();    }
    public void    viewAllSlot()             { cameraController.viewAll();             }
    public void    saveHomePositionSlot()    { cameraController.saveHomePosition();    }
    public void    resetToHomePositionSlot() { cameraController.resetToHomePosition(); }

    //! this method was introduced so that mouse events can be used for picking even
    //! if the viewer is nominally in viewing mode (but the mouse events are otherwise
    //! unused)
    protected boolean    isCurrentlyPicking() { return !isViewing(); }
    
	//! Externally set the viewer into/out off seek mode (default OFF). Actual
	//! seeking will not happen until the viewer decides to (ex: mouse click).
	//!
	//! Note: setting the viewer out of seek mode while the camera is being
	//! animated will stop the animation to the current location.
	protected void setSeekMode(boolean flag)
	{
	    if (isViewing()) {
	        getCameraController().doSetSeekMode(flag);
	    }	
	}

}
