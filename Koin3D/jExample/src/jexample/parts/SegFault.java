/**
 * 
 */
package jexample.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.nodes.SoAnnotation;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.interaction.inventor.SoInteraction;
import jscenegraph.interaction.inventor.draggers.SoCenterballDragger;
import jscenegraph.port.Destroyable;
import jsceneviewer.inventor.qt.SoQt;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewer.inventor.qt.viewers.SoQtFullViewer.BuildFlag;

/**
 * @author BOYADJIAN
 *
 */
public class SegFault {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		
		SoQt.init();
		
		// An Annotation node must be used here because it delays the rendering
		SoSeparator anno = new SoAnnotation();
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		shell.setLayout(fillLayout);
		
		int style = SWT.NO_BACKGROUND;
		
		final boolean[] first = new boolean[1];
		first[0] = true;
		
		SoQtExaminerViewer eviewer = new SoQtExaminerViewer(BuildFlag.BUILD_ALL,Type.BROWSER,shell,style)
				{
				// Use this function to add an Annotation node after the
				// scene has been rendered
				public void setViewing(boolean enable) {
					if( first[0] ) {
						first[0] = false;
						super.setViewing(enable);
						return;
					}
					SoNode root = getSceneHandler().getSceneGraph();

					SoSearchAction searchAction = new SoSearchAction();
					searchAction.setType(SoSeparator.getClassTypeId());
					searchAction.setInterest(SoSearchAction.Interest.FIRST);
					searchAction.setName(new SbName("GroupOnTopPreSel"));
					searchAction.apply(root);
					SoPath selectionPath = searchAction.getPath();

					if (selectionPath != null) {
						SoSeparator sep = (SoSeparator)(selectionPath.getTail());
						sep.addChild(anno);
					}
					Destroyable.delete(searchAction);
				}
				};
		SoInteraction.init();

		SoSeparator root = new SoSeparator();
		root.ref();

		SoMaterial mat = new SoMaterial();
		mat.transparency.setValue(0.5f);
		mat.diffuseColor.setIgnored(true);
		mat.setOverride(true);

		SoSeparator edit = new SoSeparator();
		edit.setName("GroupOnTopPreSel");
		edit.addChild(mat);
		root.addChild(edit);

		SoSeparator view = new SoSeparator();
		view.renderCaching.setValue(SoSeparator.CacheEnabled.AUTO);
		view.addChild(new SoCenterballDragger/*SoTranslate1Dragger*/());

		root.addChild(view);

		anno.ref();
		anno.addChild(view);

//		QWidget* mainwin = SoQt::init(argc, argv, argv[0]);
		//HWND mainwin = SoWin::init(argv[0]);
		//MyViewer* eviewer = new MyViewer(mainwin, anno);

		// Transparency type must be set to SORTED_OBJECT_SORTED_TRIANGLE_BLEND in order
		// to activate the caching mechanism
		SoGLRenderAction glAction = eviewer.getSceneHandler().getGLRenderAction();
		glAction.setTransparencyType(SoGLRenderAction.TransparencyType.SORTED_OBJECT_SORTED_TRIANGLE_BLEND);

	    eviewer.buildWidget(style);
	    
		eviewer.setSceneGraph(root);

	    shell.pack();
		shell.setSize(700, 700);
	    Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    shell.setLocation(x, y);		shell.open ();
		shell.setLocation(x, y);
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
		
//		eviewer.show();
//
//		SoWin::show(mainwin);
//		SoWin::mainLoop();

		//delete eviewer;
		root.unref();
		anno.unref();
		//SoWin::done();
		//return 0;
	}

}
