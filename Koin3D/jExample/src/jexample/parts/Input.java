/**
 * 
 */
package jexample.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jsceneviewer.inventor.qt.SoQt;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewer.inventor.qt.viewers.SoQtFullViewer.BuildFlag;

/**
 * @author Yves Boyadjian
 *
 */
public class Input {


public static SoSeparator
readFile( String filename)
{
   // Open the input file
   final SoInput mySceneInput = new SoInput();
   if (!mySceneInput.openFile(filename)) {
      System.err.printf("Cannot open file "+filename+"\n");
      System.exit(1);
   }

   // Read the whole file into the database
   SoSeparator myGraph = SoDB.readAll(mySceneInput);
   if (myGraph == null) {
      System.err.printf("Problem reading file\n");
      System.exit(1);
   }
   mySceneInput.closeFile();
   mySceneInput.destructor();
   return myGraph;
}

public static void main(String[] argv)
{

	Display display = new Display ();
	Shell shell = new Shell(display);
	
	FillLayout fillLayout = new FillLayout();
	fillLayout.type = SWT.VERTICAL;
	shell.setLayout(fillLayout);
	
  // Initialize the Qt system:
  //QApplication app(argc, argv);

  // Make a main window:
//  QWidget mainwin;
//  mainwin.resize(400,400);

  // Initialize SoQt
  SoQt.init("");

  // The root of a scene graph
  SoSeparator root = new SoSeparator();
  root.ref();

  root.addChild(readFile("examples_iv/data/test.iv"));
  //root.addChild(readFile("examples_iv/data/sphere1.iv"));

  // Initialize an examiner viewer:
  SoQtExaminerViewer eviewer = new SoQtExaminerViewer(BuildFlag.BUILD_ALL,Type.BROWSER,shell);
  
  //eviewer.buildWidget(SWT.NO_BACKGROUND);
  
  eviewer.setSceneGraph(root);
  eviewer.show();

  // Pop up the main window.
  //SoQt.show(/*mainwin*/);
  shell.open();

  // Loop until exit.
  //SoQt.mainLoop();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

  // Clean up resources.
  //eviewer.destructor();
  root.unref();
}
}
