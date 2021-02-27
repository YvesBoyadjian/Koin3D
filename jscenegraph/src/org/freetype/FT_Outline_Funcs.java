/**
 * 
 */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_Outline_Funcs                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure to hold various function pointers used during outline  */
  /*    decomposition in order to emit segments, conic, and cubic Béziers. */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    move_to  :: The `move to' emitter.                                 */
  /*                                                                       */
  /*    line_to  :: The segment emitter.                                   */
  /*                                                                       */
  /*    conic_to :: The second-order Bézier arc emitter.                   */
  /*                                                                       */
  /*    cubic_to :: The third-order Bézier arc emitter.                    */
  /*                                                                       */
  /*    shift    :: The shift that is applied to coordinates before they   */
  /*                are sent to the emitter.                               */
  /*                                                                       */
  /*    delta    :: The delta that is applied to coordinates before they   */
  /*                are sent to the emitter, but after the shift.          */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The point coordinates sent to the emitters are the transformed     */
  /*    version of the original coordinates (this is important for high    */
  /*    accuracy during scan-conversion).  The transformation is simple:   */
  /*                                                                       */
  /*    {                                                                  */
  /*      x' = (x << shift) - delta                                        */
  /*      y' = (x << shift) - delta                                        */
  /*    }                                                                  */
  /*                                                                       */
  /*    Set the values of `shift' and `delta' to~0 to get the original     */
  /*    point coordinates.                                                 */
  /*                                                                       */
public class FT_Outline_Funcs {
	
	public FT_Outline_Funcs(
			FT_Outline_MoveToFunc move_To2, 
			FT_Outline_LineToFunc line_To2,
			FT_Outline_ConicToFunc conic_To2, 
			FT_Outline_CubicToFunc cubic_To2) {
		move_to = move_To2;
		line_to = line_To2;
		conic_to = conic_To2;
		cubic_to = cubic_To2;
	}
	public interface FT_Outline_MoveToFunc {
		
	}
	
	public interface FT_Outline_LineToFunc {
		
	}
	
	public interface FT_Outline_ConicToFunc {
		
	}
	
	public interface FT_Outline_CubicToFunc {
		
	}

    FT_Outline_MoveToFunc   move_to;
    FT_Outline_LineToFunc   line_to;
    FT_Outline_ConicToFunc  conic_to;
    FT_Outline_CubicToFunc  cubic_to;

    int                     shift;
    int                  delta;

}
