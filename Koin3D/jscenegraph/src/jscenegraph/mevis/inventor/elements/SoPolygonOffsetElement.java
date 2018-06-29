// **InsertLicense** code

package jscenegraph.mevis.inventor.elements;

import jscenegraph.database.inventor.elements.SoReplacedElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoPolygonOffsetElement extends SoReplacedElement {

   protected

      Style  _style;
   protected   boolean _active;
   protected   float  _offsetfactor;
   protected   float  _offsetunits;

	
	   public

		      enum Style {
		         FILLED ( 0x01),
		         LINES  ( 0x02),
		         POINTS ( 0x04),
		         ALL    ( 0xff);
		         
		         private int value;
		         
		         Style(int value) {
		        	 this.value = value;
		         }
		         
		         public static Style fromValue(int value) {
		        	 switch(value) {
		        	 case 0x01: return FILLED;
		        	 case 0x02: return LINES;
		        	 case 0x04: return POINTS;
		        	 case 0xff: return ALL;
			         default: return null;
		        	 }
		         }
		         
		         public int getValue() {
		        	 return value;
		         }
		      };

	//TODO

public static void
set(SoState state, SoNode node, float factor, float units, Style styles, boolean on)
{
   SoPolygonOffsetElement elem = (SoPolygonOffsetElement )SoReplacedElement.getElement(state, classStackIndexMap.get(SoPolygonOffsetElement.class), node);
   
   if(elem != null)
      elem.setElt(factor, units, styles, on);
}

public void
setElt(float factor, float units, Style styles, boolean on)
{
   _offsetfactor = factor;
   _offsetunits  = units;
   _style        = styles;
   _active       = on;
}


}
