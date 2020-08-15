package jexample.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import jexample.parts.SamplePart;
import jscenegraph.database.inventor.nodes.SoFile;
import jscenegraph.database.inventor.nodes.SoSeparator;
public class OpenHandler {

	@Inject
	EPartService partService;
	
	@Execute
	public void execute(Shell shell){
		FileDialog dialog = new FileDialog(shell);
		String fileName = dialog.open();
		if ( fileName != null && !fileName.isBlank() ) {
			MPart mpart = partService.getActivePart();
			if( null != mpart ) {
				Object obj = mpart.getObject();
				if ( obj instanceof SamplePart ) {
					SamplePart sp = (SamplePart) obj;
					
					SoSeparator root = new SoSeparator();
					//root.ref();

					// load scene from file
					SoFile file = new SoFile();
					file.name.setValue(fileName);
					root.addChild(file);
					
					sp.viewer.setSceneGraph(root);
				}
			}
		}
	}
}
