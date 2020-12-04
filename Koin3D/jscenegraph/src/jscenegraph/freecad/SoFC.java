/**
 * 
 */
package jscenegraph.freecad;

/**
 * @author Yves Boyadjian
 *
 */
public class SoFC {

	public static void init() {
		SoFCSeparator.initClass();
		SoFCSelectionRoot.initClass();
		SoFCSelection.initClass();
		SoFCIndexedFaceSet.initClass();
		SoFCMaterialEngine.initClass();
	}

}
