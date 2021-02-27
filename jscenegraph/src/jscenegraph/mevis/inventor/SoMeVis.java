// **InsertLicense** code

package jscenegraph.mevis.inventor;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.interaction.inventor.SoInteraction;
import jscenegraph.mevis.inventor.elements.SoGLPolygonOffsetElement;
import jscenegraph.mevis.inventor.elements.SoGLStencilBitsElement;
import jscenegraph.mevis.inventor.elements.SoPolygonOffsetElement;
import jscenegraph.mevis.inventor.elements.SoStencilBitsElement;
import jscenegraph.mevis.inventor.events.SoLocation2RefreshEvent;
import jscenegraph.mevis.inventor.events.SoMouseWheelEvent;
import jscenegraph.mevis.inventor.fields.SoSFVec2s;
import jscenegraph.mevis.inventor.fields.SoSFVec3s;
import jscenegraph.mevis.inventor.nodes.SoPolygonOffset;
import jscenegraph.nodekits.inventor.nodekits.SoNodeKit;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMeVis {

	   public static

		      //! Init all MeVis classes

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initialize all MeVis classes.
//
// Use: public

void
init()
//
////////////////////////////////////////////////////////////////////////
{
   // Only initialize once
   if(!initialized) {
        
      SoDB.init();           // no problem if called multiple times
      SoNodeKit.init();      // no problem if called multiple times
      SoInteraction.init();  // no problem if called multiple times
        
      // elements
      SoPolygonOffsetElement.initClass(SoPolygonOffsetElement.class);
      SoStencilBitsElement.initClass(SoStencilBitsElement.class);
      SoGLPolygonOffsetElement.initClass(SoGLPolygonOffsetElement.class);
      SoGLStencilBitsElement.initClass(SoGLStencilBitsElement.class);

      // events
      SoMouseWheelEvent.initClass();
      SoLocation2RefreshEvent.initClass();
      
      // fields
      SoSFVec2s.initClass(SoSFVec2s.class);
      SoSFVec3s.initClass(SoSFVec3s.class);
        
      // nodes
      SoPolygonOffset.initClass();

      initialized = true;
   }
}

	   private
		    
		      //! Have MeVis classes been initialized?
		      static boolean     initialized;
}
