/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoReplacedElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

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

		  protected final float ambientIntensity[] = new float[1];
		  protected final SbColor ambientColor = new SbColor();
		  protected final SbVec3f attenuation = new SbVec3f();
		  protected final int[] fogType = new int[1];
		  protected final SbColor fogColor = new SbColor();
		  protected final float[] fogVisibility = new float[1];
		  protected final float[] fogStart = new float[1];
		  
		  public static  void set(SoState state, SoNode node,
                  float ambientIntensity, SbColor ambientColor,
                  SbVec3f attenuation, int fogType,
                  SbColor fogColor, float fogVisibility
                  ) {
			  set(state,node,ambientIntensity,ambientColor,attenuation,fogType,fogColor,fogVisibility,0.0f);
		  }
		  public static void
		  set(SoState state,
		                            SoNode node,
		                            float ambientIntensity,
		                            SbColor ambientColor,
		                            SbVec3f attenuation,
		                            int fogType,
		                            SbColor fogColor,
		                            float fogVisibility,
		                            float fogStart)
		  {
		    SoEnvironmentElement element =
		      (SoEnvironmentElement)
		      (
		       SoReplacedElement.getElement(state, classStackIndexMap.get(SoEnvironmentElement.class), node)
		       );
		    if (element != null) {
		      element.setElt(state, ambientIntensity, ambientColor, attenuation,
		                      fogType, fogColor, fogVisibility, fogStart);
		    }
		  }

		  public void
		  getDefault(final float[] ambientIntensity,
		                                   final SbColor ambientColor,
		                                   final SbVec3f attenuation,
		                                   final int[] fogType,
		                                   final SbColor fogColor,
		                                   final float[] fogVisibility,
		                                   final float[] fogStart)
		  {
		    ambientIntensity[0] = 0.2f;
		    ambientColor.copyFrom( new SbColor(1.0f, 1.0f, 1.0f));
		    attenuation.copyFrom( new SbVec3f(0.0f, 0.0f, 1.0f));
		    fogType[0] = FogType.NONE.getValue();
		    fogColor.copyFrom( new SbColor(1.0f, 1.0f, 1.0f));
		    fogVisibility[0] = 0.0f;
		    fogStart[0] = 0.0f;
		  }

		  public static int
		  getFogType(SoState state)
		  {
		    final SoEnvironmentElement element = (SoEnvironmentElement)
		      (
		       SoElement.getConstElement(state, classStackIndexMap.get(SoEnvironmentElement.class))
		       );
		    return element.fogType[0];
		  }


public void
init(SoState state)
{
  super.init(state);
  this.getDefault(this.ambientIntensity, this.ambientColor, this.attenuation,
                   fogType, fogColor, fogVisibility, fogStart);
}

//! FIXME: doc
public void
setElt(SoState state,
                             float ambientIntensityarg,
                             SbColor ambientColorarg,
                             SbVec3f attenuationarg,
                             int fogTypearg,
                             SbColor fogColorarg,
                             float fogVisibilityarg,
                             float fogStartarg)
{
  this.ambientIntensity[0] = ambientIntensityarg;
  this.ambientColor.copyFrom( ambientColorarg);
  this.attenuation.copyFrom( attenuationarg);
  this.fogType[0] = fogTypearg;
  this.fogColor.copyFrom( fogColorarg);
  this.fogVisibility[0] = fogVisibilityarg;
  this.fogStart[0] = fogStartarg;
}
}
