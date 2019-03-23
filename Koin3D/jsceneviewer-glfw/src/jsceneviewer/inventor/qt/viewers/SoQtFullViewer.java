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

package jsceneviewer.inventor.qt.viewers;

//import org.eclipse.jface.action.Action;
//import org.eclipse.jface.action.ToolBarManager;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.FillLayout;
import jsceneviewer.Composite;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Layout;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.ToolBar;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2fSingle;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoOrthographicCamera;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.SoQtIcons;

/**
 * @author Yves Boyadjian
 *
 */

//////////////////////////////////////////////////////////////////////////////
//
//Class: SoQtFullViewer
//
//The SoQtFullViewer component class is the abstract base class for all
//viewers which include a decoration around the rendering area. The
//decoration is made of thumbwheels, sliders and push/toggle buttons. There
//is also a popup menu which includes all of the viewing options and methods.
//
//////////////////////////////////////////////////////////////////////////////

public class SoQtFullViewer extends SoQtViewer {

	public
	    //! This specifies what should be build by default in the constructor
	    enum BuildFlag {
	    BUILD_NONE          ( 0x00),
	    BUILD_DECORATION    ( 0x01),
	    BUILD_POPUP         ( 0x02),
	    BUILD_ALL           ( 0xff);
	    private int value;
	    BuildFlag(int value) {
	    	this.value = value;
	    }
	    };
	    
	    private boolean          decorationFlag;
	    
	    private String         popupMenuTitle;

	    // popup menu vars
//	    private Menu popupMenu;

	    
	    // pref sheet dialog
	    //private Dialog        prefSheetDialog;
	    
	    // thumb wheel names
	    private String         rightWheelName;
	    private String         bottomWheelName;
	    private String         leftWheelName;

//	    private Label         rightWheelLabel;
//	    private Label         bottomWheelLabel;
//	    private Label         leftWheelLabel;

	    // general decoration vars
	    protected Composite        leftTrimForm;
	    protected Composite        bottomTrimForm;
	    protected Composite        rightTrimForm;

	    // viewer button container
//	    protected ToolBar       viewerButtonBar;

	    private final SbVec2fSingle         zoomSldRange = new SbVec2fSingle();
	    
	    // zoom pref sheet
	    private Composite      zoomPrefSheet;
//	    private QSlider        zoomSlider;
//	    private QDoubleSpinBox zoomInput;

	    // viewer toolbar actions
//	    private Action        pickAction;
//	    private Action        viewAction;
//	    private Action        seekAction;
//	    private Action        helpAction;
//	    private Action        gotoHomeAction;
//	    private Action        setHomeAction;
//	    private Action        viewAllAction;

	    //! Constructor/Destructor
	    public SoQtFullViewer (BuildFlag flag) {
	    	this(flag, Type.BROWSER);
	    }
	    public SoQtFullViewer (BuildFlag flag, Type type) {
	    	this(flag,type,null);
	    }
	    public SoQtFullViewer (BuildFlag flag, Type type, Composite parent) {
	    	this(flag,type,parent,0);
	    }
	    public SoQtFullViewer (BuildFlag flag, Type type, Composite parent, int style) {
	    	super(type,parent,style);
	        // init decoration vars
	        // (decorations really are created all the time, they just are hidden if this flag is not set)
	        decorationFlag = (flag.value & BuildFlag.BUILD_DECORATION.value) != 0;

	        initWidgets();
	    	//TODO
	    }

	    private void initWidgets()
	    {
//	        // pre-create actions
//	        pickAction = new Action("Pick mode", Action.AS_CHECK_BOX) {
//	        	@Override
//	        	public void run() {
//	        		SoQtFullViewer.this.setPickMode();
//	        		pickAction.setChecked(true);
//	        	}
//			};
//			pickAction.setImageDescriptor(SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_PICK));
//
//	        viewAction = new Action("View mode", Action.AS_CHECK_BOX) {
//	        	@Override
//	        	public void run() {
//	        		SoQtFullViewer.this.setViewMode();
//	        		viewAction.setChecked(true);
//	        	}	        	
//	        };
//	        viewAction.setImageDescriptor(SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_VIEW));
//
////	        helpAction = new Action ("Help", SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_HELP)){
////	                };
////	            connect (helpAction, SIGNAL (triggered(bool)), this, SIGNAL (showHelp()));
////	            helpAction.setVisible (helpClientCount > 0);
//
//	            // second help action without icon
////	            helpAction2 = new Action ("Help") {
////	            	
////	            };
////	            connect (helpAction2, SIGNAL (triggered(bool)), this, SIGNAL (showHelp()));
////	            helpAction2.setVisible (helpClientCount > 0);
//
//	            seekAction = new Action("Seek mode",Action.AS_CHECK_BOX) {
//		        	@Override
//		        	public void run() {
//		        		SoQtFullViewer.this.toggleSeekMode();
//		        	}	            	
//	            };
//	                seekAction.setImageDescriptor(SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_SEEK));
//	            seekAction.setChecked (getCameraController().isSeekMode());
//
//	            gotoHomeAction =
//	                new Action ("Goto home position", SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_HOME)) {
//		        	@Override
//		        	public void run() {
//		        		SoQtFullViewer.this.resetToHomePositionSlot();
//		        	}	            	
//	            };
//
//	            setHomeAction =
//	                new Action ("Set home position", SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_SET_HOME)) {
//		        	@Override
//		        	public void run() {
//		        		SoQtFullViewer.this.saveHomePositionSlot();
//		        	}	            	
//	            };
//
//	            viewAllAction =
//	                new Action ("View all",SoQtIcons.getIcon (SoQtIcons.IconNum.ICON_VIEW_ALL)) {
//		        	@Override
//		        	public void run() {
//		        		SoQtFullViewer.this.viewAllSlot();
//		        	}	            	
//	            };
	    }

	    void toggleSeekMode()   { setSeekMode   (!getCameraController().isSeekMode()); }
	    void toggleHeadlight()  { setHeadlight  (!getCameraController().isHeadlight()); }
	    void toggleViewing()    { setViewing    (!isViewing()); }
	    void toggleDecoration() { setDecoration (!decorationFlag); }
	    void setSeekDetailMode (int mode) { setDetailSeek (mode != 0); }
	    void setPickMode()      { setViewing    (false); }
	    void setViewMode()      { setViewing    (true); }
	    void increaseSpeed()    { viewerSpeed *= 2.0; }
	    void decreaseSpeed()    { viewerSpeed /= 2.0; }
	    

protected void setSeekMode (boolean onOrOff)
{
    super.setSeekMode (onOrOff);
//    seekAction.setChecked (getCameraController().isSeekMode());
}


void setDetailSeek(boolean onOrOff)
{
    getCameraController().setDetailSeek (onOrOff);
//    if (seekDistModeGroup) { TODO
//        seekDistModeGroup.button ((int) seekDistAsPercentage).setChecked (true);
//    }
}


public void setViewing (boolean flag)
{
    if (flag == viewingFlag) { return; }

    // call the base class
    super.setViewing(flag);

    // update the push buttons
//    viewAction.setChecked (viewingFlag);
//    pickAction.setChecked (!viewingFlag);

    // update the popup menu entry
    //viewingAction.setChecked (viewingFlag); TODO
}


public void setHeadlight (boolean flag)
{
    if (flag == getCameraController().isHeadlight()) {
        return;
    }

    // call base class routine
    getCameraController().setHeadlight (flag);

    // update the popup menu entry
    //headlightAction.setChecked (getCameraController().isHeadlight()); TODO
}


	    
protected void showEvent() {
	
}
	    
protected void hideEvent ()
{
    //super.hideEvent ();

//    if (prefSheetDialog != null) {
//        prefSheetDialog.close();
//    }
}

	public void setVisible(boolean visible) {
		boolean previousvisible = isVisible();
		super.setVisible(visible);
		if(visible != previousvisible) {
			if(visible) {
				showEvent();
			}
			else {
				hideEvent();
			}
		}
	}

	public void
	setPopupMenuTitle (final String str)
	{
	    popupMenuTitle = str;
//	    if (popupMenu != null) {
//	        //popupMenu.setTitle (str); TODO
//	    }
	}

    // Subclasses SHOULD set those wheel string to whatever functionality
    // name they are redefining the thumb wheels to do. Default names are
    // "Motion X, "Motion Y" and "Motion Z" for bottom, left and right wheels.
    protected void            setBottomWheelTitle (final String str) {
        bottomWheelName = str;
//        if (bottomWheelLabel != null) {
//            bottomWheelLabel.setText (str);
//        }

    }
    protected void            setLeftWheelTitle   (final String str) {
        leftWheelName = str;
//        if (leftWheelLabel != null) {
//            leftWheelLabel.setText (str);
//        }

    }
    protected void            setRightWheelTitle  (final String str) {
        rightWheelName = str;
//        if (rightWheelLabel != null) {
//            rightWheelLabel.setText (str);
//        }

    }


//protected Composite createRightBorder (Composite parent)
//{
//    rightTrimForm = new Composite (parent,0);
//    FillLayout box = new FillLayout();
//    box.marginHeight = 2;
//    box.marginWidth = 2;
//    rightTrimForm.setLayout (box);
//    viewerButtonBar = new ToolBar (rightTrimForm,SWT.VERTICAL | SWT.BORDER);
//    //viewerButtonBar.setOrientation (Qt::Vertical);
////#if defined(MACOS)
////    viewerButtonBar.setIconSize(QSize(26,26));
////#endif
//    final ToolBarManager manager = new ToolBarManager(viewerButtonBar);
//    createViewerButtons (manager);
//    manager.update(true);
////    box.addWidget (viewerButtonBar, 1);
////    rightWheel = new SoQtThumbWheel (Qt::Vertical, rightTrimForm);
////    rightWheel.setValue (rightWheelValue);
////    connect (rightWheel, SIGNAL (valueChanged(double)), this, SLOT (rightWheelMotion(double)));
////    connect (rightWheel, SIGNAL (dragStart(double)),    this, SLOT (rightWheelStart()));
////    connect (rightWheel, SIGNAL (dragStop(double)),     this, SLOT (rightWheelFinish()));
////    rightWheel.setAngleFactor (-2.0*M_PI);
////    box.addWidget (rightWheel);
////    box.setAlignment (rightWheel, Qt::AlignBottom | Qt::AlignHCenter);
//    return rightTrimForm;
//}


//protected void createViewerButtons (ToolBarManager parent)
//{
//	// TODO
//    parent.add (pickAction);
//    parent.add (viewAction);
//    //parent.add (helpAction); TODO
//    parent.add (gotoHomeAction);
//    parent.add (setHomeAction);
//    parent.add (viewAllAction);
//    parent.add (seekAction);
//}


public void setCamera (SoCamera newCamera, boolean createdCamera)
{
    // call base class routine
    super.setCamera(newCamera, createdCamera);
    
    SoCamera camera = getCameraController().getCamera();

    // check if the zoom slider needs to be enabled
    if (zoomPrefSheet != null) {

        boolean enable = camera != null &&
                        camera.isOfType (SoPerspectiveCamera.getClassTypeId());
        zoomPrefSheet.setEnabled (enable);

        // update the UI if enabled
        if (enable) {
            double zoom = getCameraZoom();
            setZoomSliderPosition (zoom);
        }
    }
}


public void setDecoration (boolean flag)
{
    if (flag == decorationFlag) {
        return;
    }
    decorationFlag = flag;
    doSetDecoration();
}

private void doSetDecoration() {

//    if (leftTrimForm != null) {
//        leftTrimForm.setVisible (decorationFlag);
//    }
//    if (rightTrimForm != null) {
//        rightTrimForm.setVisible (decorationFlag);
//        if(!decorationFlag) {
//	        rightTrimForm.setLayout(new Layout() {
//	
//				@Override
//				protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
//					return new Point(0,0);
//				}
//	
//				@Override
//				protected void layout(Composite composite, boolean flushCache) {
//					// nothing to do
//				}
//	        	
//	        });
//        }
//    }
//    if (bottomTrimForm != null) {
//        bottomTrimForm.setVisible (decorationFlag);
//    }

    // update the popup menu entry
    //decorationAction.setChecked (decorationFlag); TODO
}

public boolean          isDecoration()      { return decorationFlag; }

public double getCameraZoom()
{
	SoCamera camera = getCameraController().getCamera();
	
    if (camera == null) {
        return 0.0;
    } else if ( camera.isOfType (SoPerspectiveCamera.getClassTypeId()) ) {
        return ((SoPerspectiveCamera )camera).heightAngle.getValue() * 180.0 / Math.PI;
    } else if ( camera.isOfType (SoOrthographicCamera.getClassTypeId()) ) {
        return ((SoOrthographicCamera )camera).height.getValue();
    } else {
//#if DEBUG
        SoDebugError.post("SoQtFullViewer::getCameraZoom",
                            "unknown camera type");
//#endif
        return 0.0;
    }
}


public void setZoomSliderPosition (double zoom)
{
//    if (zoomSlider != null) {
//        // find the slider position, using a square root distance to make the
//        // slider smoother and less sensitive when close to zero.
//        double f = (zoom - zoomSldRange.getValue()[0]) / (zoomSldRange.getValue()[1] - zoomSldRange.getValue()[0]);
//        f = (f < 0.0) ? 0.0 : ((f > 1.0) ? 1.0 : f);
//        f = Math.sqrt (f);
//        // finally position the slider
//        zoomSlider.setValue ((int) (f * 1000));
//    }
//    if (zoomInput != null) {
//        zoomInput.setValue (zoom);
//    }
}

	public void buildWidget(int style) {
		super.buildWidget(style);
		doSetDecoration();
	}
}
