/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoReplacedElement;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoEnvironmentElement extends SoReplacedElement {

	 public enum FogType {
		    NONE,
		    HAZE,
		    FOG,
		    SMOKE;

			public int getValue() {
				return ordinal();
			}

			public static FogType fromValue(int fog2) {
				switch(fog2) {
				case 0: return NONE;
				case 1: return HAZE;
				case 2: return FOG;
				case 3: return SMOKE;
				default:
					return null;
				}
			}
		  };

		  protected float ambientIntensity;
		  protected final SbColor ambientColor = new SbColor();
		  protected final SbVec3f attenuation = new SbVec3f();
		  protected int fogType;
		  protected final SbColor fogColor = new SbColor();
		  protected float fogVisibility;
		  protected float fogStart;
		  
		  public static int
		  getFogType(SoState state)
		  {
		    final SoEnvironmentElement element = (SoEnvironmentElement)
		      (
		       SoElement.getConstElement(state, classStackIndexMap.get(SoEnvironmentElement.class))
		       );
		    return element.fogType;
		  }

}
