
  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    FT_Raster                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    An opaque handle (pointer) to a raster object.  Each object can be */
  /*    used independently to convert an outline into a bitmap or pixmap.  */
  /*                                                                       */
package org.freetype;

import jscenegraph.port.TDirection;
import jscenegraph.port.TFace;
import jscenegraph.port.TPoint;

/**
 * @author Yves Boyadjian
 *
 */
public class FT_Raster {
	/* The maximum number of stacked Bezier curves.  Setting this constant
	 * to more than 32 is a pure waste of space.
	 */
	public static final int MaxBezier               =32;

	  /*TPoint * */public int    cursor;           /* Current cursor in render pool */
	  public TPoint[]     pool;             /* The render pool base address */
	  /*TPoint * */public int   pool_size;        /* The render pool's size */
	  /*TPoint * */int   pool_limit;       /* Limit of faces zone in pool */

	  TPoint      last;
	  public long        minY;

	public long maxY;
	  public long        minX;

	public long maxX;

	  int         error;

//	#ifndef FT_RASTER_CONSTANT_PRECISION
//	  int         precision_bits;   /* precision related variables */
//	  int         precision;
//	  int         precision_half;
//	  long        precision_mask;
//	  int         precision_shift;
//	  int         precision_step;
//	  int         precision_jitter;
//	#endif

	  public FT_Outline outline;

	  int         n_points;         /* number of points in current glyph */
	  int         n_contours;       /* number of contours in current glyph */
	  int         n_extrema;        /* number of `extrema' scanlines */

	  TPoint     arc;              /* current Bezier arc pointer */

	  int         num_faces;        /* current number of faces */

	  boolean        fresh;            /* signals a fresh new face which */
	                                /* `start' field must be completed */
	  boolean        joint;            /* signals that the last arc ended */
	                                /* exactly on a scanline.  Allows */
	                                /* removal of doublets */
	  /*TFace*/int      cur_face;         /* current face */
	  TFace[]      start_face;       /* head of linked list of faces */
	  TFace      first_face;       /* contour's first face in case */
	                                /* of impact */
	  TDirection  state;    /* rendering state */

	  public int         scale_shift;      /* == 0  for bitmaps */
	                                /* == 1  for 5-levels pixmaps */
	                                /* == 2  for 17-levels pixmaps */

	  public int         scale_delta;      /* ras.precision_half for bitmaps */
	                                /* 0 for pixmaps */

	  final TPoint[]      arcs = new TPoint[2 * MaxBezier + 1]; /* The Bezier stack */

}
