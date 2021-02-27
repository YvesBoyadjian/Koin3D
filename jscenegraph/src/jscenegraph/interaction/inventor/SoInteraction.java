/**
 * 
 */
package jscenegraph.interaction.inventor;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.actions.SoLineHighlightRenderAction;
import jscenegraph.interaction.inventor.draggers.SoDragger;
import jscenegraph.interaction.inventor.manips.SoHandleBoxManip;
import jscenegraph.interaction.inventor.manips.SoTrackballManip;
import jscenegraph.interaction.inventor.manips.SoTransformBoxManip;
import jscenegraph.interaction.inventor.manips.SoTransformManip;
import jscenegraph.interaction.inventor.nodekits.SoInteractionKit;
import jscenegraph.interaction.inventor.nodes.SoAntiSquish;
import jscenegraph.interaction.inventor.nodes.SoSelection;
import jscenegraph.interaction.inventor.nodes.SoSurroundScale;
import jscenegraph.nodekits.inventor.nodekits.SoNodeKit;

/**
 * @author Yves Boyadjian
 *
 */
public class SoInteraction {

	   public
		        //! This calls 
		        //! SoDB.init() and SoNodeKit.init(),
		        //! calls initClasses() on SoDragger,
		        //! and calls initClass() on the following classes:
		        //! SoAntiSquish,
		        //! SoBoxHighlightRenderAction,
		        //! SoCenterballManip,
		        //! SoDirectionalLightManip,
		        //! SoHandleBoxManip,
		        //! SoInteractionKit,
		        //! SoJackManip,
		        //! SoLineHighlightRenderAction,
		        //! SoPointLightManip,
		        //! SoSelection,
		        //! SoSpotLightManip,
		        //! SoSurroundScale,
		        //! SoTabBoxManip,
		        //! SoTrackballManip,
		        //! SoTransformBoxManip, and
		       //! SoTransformManip.
		       static void         init()         // init all interaction classes
		       {
		     // Only initialize once
		        if (! initialized) {
		    
		            SoDB.init(); // no problem if called multiple times
		            SoNodeKit.init(); // no problem if called multiple times
		    
		            // Nodes used in interaction kits to help with transforms.
		            SoAntiSquish.initClass();
		            SoSurroundScale.initClass();
		    
		            // draggers
		            SoInteractionKit.initClass();
		            SoDragger.initClasses();
		    
		            // transform manips
		            SoTransformManip.initClass();
//		            SoCenterballManip.initClass();
		            SoHandleBoxManip.initClass();
//		            SoJackManip.initClass();
//		            SoTabBoxManip.initClass();
		            SoTrackballManip.initClass();
		            SoTransformBoxManip.initClass();
//		            SoTransformerManip.initClass();
		            // lightManips
//		            SoPointLightManip.initClass();
//		            SoDirectionalLightManip.initClass();
//		            SoSpotLightManip.initClass();
		    
		    
		            // nodes
		            SoSelection.initClass();
		    
		            // actions
//		            SoBoxHighlightRenderAction.initClass();
		            SoLineHighlightRenderAction.initClass();
		    
		            initialized = true;
		        }
		   		   
		       }
		     private
		       static boolean       initialized;    //!< has interaction been initialized
		  
}
