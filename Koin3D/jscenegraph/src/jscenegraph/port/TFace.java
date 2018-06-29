/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class TFace {

	  TFace link;  /* link to next face - various purpose */
	  TPoint offset;       /* start of face's data in render pool */
	  int     n_points;     /* number of points */
	  int     flow;         /* Face orientation: Asc/Descending */
	  TFace next;  /* next face in same contour, used */
	                        /* during drop-out control */
}
