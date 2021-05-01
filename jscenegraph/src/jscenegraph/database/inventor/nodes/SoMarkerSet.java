/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLTexture3EnabledElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoCullElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.Util;

/*!
  \class SoMarkerSet SoMarkerSet.h Inventor/nodes/SoMarkerSet.h
  \brief The SoMarkerSet class displays a set of 2D bitmap markers in 3D.
  \ingroup nodes

  This node uses the coordinates currently on the state (or in the
  vertexProperty field) in order. The numPoints field specifies the
  number of points in the set.

  In addition to supplying the user with a set of standard markers to
  choose from, it is also possible to specify one's own bitmaps for
  markers.

  This node class is an extension versus the original SGI Inventor
  v2.1 API.  In addition to being a Coin extension, it is also present
  in TGS' Inventor implementation. (Note that TGS's implementation
  doesn't support the NONE markerIndex value.)

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    MarkerSet {
        vertexProperty NULL
        startIndex 0
        numPoints -1
        markerIndex 0
    }
  \endcode

  \since TGS Inventor 2.5
  \since Coin 1.0
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoMarkerSet extends SoPointSet {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoMarkerSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoMarkerSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoMarkerSet.class); }    	  	
	
	  public final static int NONE = -1;
	  public enum MarkerType {
		    /*NONE = -1,*/
		    CROSS_5_5, PLUS_5_5, MINUS_5_5, SLASH_5_5, BACKSLASH_5_5, BAR_5_5,
		    STAR_5_5, Y_5_5, LIGHTNING_5_5, WELL_5_5,

		    CIRCLE_LINE_5_5, SQUARE_LINE_5_5, DIAMOND_LINE_5_5, TRIANGLE_LINE_5_5,
		    RHOMBUS_LINE_5_5, HOURGLASS_LINE_5_5, SATELLITE_LINE_5_5,
		    PINE_TREE_LINE_5_5, CAUTION_LINE_5_5, SHIP_LINE_5_5,

		    CIRCLE_FILLED_5_5, SQUARE_FILLED_5_5, DIAMOND_FILLED_5_5,
		    TRIANGLE_FILLED_5_5, RHOMBUS_FILLED_5_5, HOURGLASS_FILLED_5_5,
		    SATELLITE_FILLED_5_5, PINE_TREE_FILLED_5_5, CAUTION_FILLED_5_5,
		    SHIP_FILLED_5_5,

		    CROSS_7_7, PLUS_7_7, MINUS_7_7, SLASH_7_7, BACKSLASH_7_7, BAR_7_7,
		    STAR_7_7, Y_7_7, LIGHTNING_7_7, WELL_7_7,

		    CIRCLE_LINE_7_7, SQUARE_LINE_7_7, DIAMOND_LINE_7_7, TRIANGLE_LINE_7_7,
		    RHOMBUS_LINE_7_7, HOURGLASS_LINE_7_7, SATELLITE_LINE_7_7,
		    PINE_TREE_LINE_7_7, CAUTION_LINE_7_7, SHIP_LINE_7_7,

		    CIRCLE_FILLED_7_7, SQUARE_FILLED_7_7, DIAMOND_FILLED_7_7,
		    TRIANGLE_FILLED_7_7, RHOMBUS_FILLED_7_7, HOURGLASS_FILLED_7_7,
		    SATELLITE_FILLED_7_7, PINE_TREE_FILLED_7_7, CAUTION_FILLED_7_7,
		    SHIP_FILLED_7_7,

		    CROSS_9_9, PLUS_9_9, MINUS_9_9, SLASH_9_9, BACKSLASH_9_9, BAR_9_9,
		    STAR_9_9, Y_9_9, LIGHTNING_9_9, WELL_9_9,

		    CIRCLE_LINE_9_9, SQUARE_LINE_9_9, DIAMOND_LINE_9_9, TRIANGLE_LINE_9_9,
		    RHOMBUS_LINE_9_9, HOURGLASS_LINE_9_9, SATELLITE_LINE_9_9,
		    PINE_TREE_LINE_9_9, CAUTION_LINE_9_9, SHIP_LINE_9_9,

		    CIRCLE_FILLED_9_9, SQUARE_FILLED_9_9, DIAMOND_FILLED_9_9,
		    TRIANGLE_FILLED_9_9, RHOMBUS_FILLED_9_9, HOURGLASS_FILLED_9_9,
		    SATELLITE_FILLED_9_9, PINE_TREE_FILLED_9_9, CAUTION_FILLED_9_9,
		    SHIP_FILLED_9_9,
		    NUM_MARKERS; /* must be last, and is _not_ a marker :) */
		  
		  	public int getValue() {
		  		return ordinal();
		  	}
		  };
		  
		  public final SoMFInt32 markerIndex = new SoMFInt32();
		  		  
		  private
			  enum Binding {
			    OVERALL,
			    PER_VERTEX
			  };
		  

		  private static class so_marker {
			  int width;
			  int height;
			  int align;
			  byte[] data;
			  int dataOffset; // java port
			  boolean deletedata;
			};

			static String marker_char_bitmaps =
				
				  // CROSS_5_5
				  "         "+
				  "         "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  "         "+
				  "         "+
				  // PLUS_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "  xxxxx  "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // MINUS_5_5
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  // SLASH_5_5
				  "         "+
				  "         "+
				  "      x  "+
				  "     x   "+
				  "    x    "+
				  "   x     "+
				  "  x      "+
				  "         "+
				  "         "+
				  // BACKSLASH_5_5
				  "         "+
				  "         "+
				  "  x      "+
				  "   x     "+
				  "    x    "+
				  "     x   "+
				  "      x  "+
				  "         "+
				  "         "+
				  // BAR_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // STAR_5_5
				  "         "+
				  "         "+
				  "  x x x  "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "  x x x  "+
				  "         "+
				  "         "+
				  // Y_5_5
				  "         "+
				  "         "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // LIGHTNING_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "     x   "+
				  "  xxxxx  "+
				  "   x     "+
				  "    x    "+
				  "         "+
				  "         "+
				  // WELL_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "   x x   "+
				  "   xxx   "+
				  "  x   x  "+
				  "         "+
				  "         "+
				  // CIRCLE_LINE_5_5
				  "         "+
				  "         "+
				  "   xxx   "+
				  "  x   x  "+
				  "  x   x  "+
				  "  x   x  "+
				  "   xxx   "+
				  "         "+
				  "         "+
				  // SQUARE_LINE_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "  x   x  "+
				  "  x   x  "+
				  "  x   x  "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // DIAMOND_LINE_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "         "+
				  "         "+
				  // TRIANGLE_LINE_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "   x x   "+
				  "   x x   "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // RHOMBUS_LINE_5_5
				  "         "+
				  "         "+
				  "         "+
				  "   xxxxx "+
				  "  x   x  "+
				  " xxxxx   "+
				  "         "+
				  "         "+
				  "         "+
				  // HOURGLASS_LINE_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // SATELLITE_LINE_5_5
				  "         "+
				  "         "+
				  "  x   x  "+
				  "   xxx   "+
				  "   x x   "+
				  "   xxx   "+
				  "  x   x  "+
				  "         "+
				  "         "+
				  // PINE_TREE_LINE_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "   x x   "+
				  "  xxxxx  "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // CAUTION_LINE_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "   x x   "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // SHIP_LINE_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "  xxxxx  "+
				  "   x x   "+
				  "    x    "+
				  "         "+
				  "         "+
				  // CIRCLE_FILLED_5_5
				  "         "+
				  "         "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "         "+
				  "         "+
				  // SQUARE_FILLED_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // DIAMOND_FILLED_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "         "+
				  "         "+
				  // TRIANGLE_FILLED_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "   xxx   "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // RHOMBUS_FILLED_5_5
				  "         "+
				  "         "+
				  "         "+
				  "   xxxxx "+
				  "  xxxxx  "+
				  " xxxxx   "+
				  "         "+
				  "         "+
				  "         "+
				  // HOURGLASS_FILLED_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "         "+
				  "         "+
				  // SATELLITE_FILLED_5_5
				  "         "+
				  "         "+
				  "  x   x  "+
				  "   xxx   "+
				  "   xxx   "+
				  "   xxx   "+
				  "  x   x  "+
				  "         "+
				  "         "+
				  // PINE_TREE_FILLED_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // CAUTION_FILLED_5_5
				  "         "+
				  "         "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "   xxx   "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  "         "+
				  // SHIP_FILLED_5_5
				  "         "+
				  "         "+
				  "    x    "+
				  "    x    "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "         "+
				  "         "+
				  // CROSS_7_7
				  "         "+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "         "+
				  // PLUS_7_7
				  "         "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  " xxxxxxx "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // MINUS_7_7
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  " xxxxxxx "+
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  // SLASH_7_7
				  "         "+
				  "       x "+
				  "      x  "+
				  "     x   "+
				  "    x    "+
				  "   x     "+
				  "  x      "+
				  " x       "+
				  "         "+
				  // BACKSLASH_7_7
				  "         "+
				  " x       "+
				  "  x      "+
				  "   x     "+
				  "    x    "+
				  "     x   "+
				  "      x  "+
				  "       x "+
				  "         "+
				  // BAR_7_7,
				  "         "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // STAR_7_7
				  "         "+
				  "    x    "+
				  "  x x x  "+
				  "   xxx   "+
				  " xxxxxxx "+
				  "   xxx   "+
				  "  x x x  "+
				  "    x    "+
				  "         "+
				  // Y_7_7
				  "         "+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // LIGHTNING_7_7
				  "         "+
				  "    x    "+
				  "     x   "+
				  "      x  "+
				  " xxxxxxx "+
				  "  x      "+
				  "   x     "+
				  "    x    "+
				  "         "+
				  // WELL_7_7
				  "         "+
				  "    x    "+
				  "   x x   "+
				  "   xxx   "+
				  "  xx xx  "+
				  "  x x x  "+
				  "  xx xx  "+
				  " xx   xx "+
				  "         "+
				  // CIRCLE_LINE_7_7
				  "         "+
				  "  xxxxx  "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  "  xxxxx  "+
				  "         "+
				  // SQUARE_LINE_7_7
				  "         "+
				  " xxxxxxx "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  " xxxxxxx "+
				  "         "+
				  // DIAMOND_LINE_7_7
				  "         "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "         "+
				  // TRIANGLE_LINE_7_7
				  "         "+
				  "    x    "+
				  "    x    "+
				  "   x x   "+
				  "   x x   "+
				  "  x   x  "+
				  "  x   x  "+
				  " xxxxxxx "+
				  "         "+
				  // RHOMBUS_LINE_7_7
				  "         "+
				  "         "+
				  "    xxxxx"+
				  "   x   x "+
				  "  x   x  "+
				  " x   x   "+
				  "xxxx     "+
				  "         "+
				  "         "+
				  // HOURGLASS_LINE_7_7
				  "         "+
				  " xxxxxxx "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " xxxxxxx "+
				  "         "+
				  // SATELLITE_LINE_7_7
				  "         "+
				  " x     x "+
				  "  x x x  "+
				  "   x x   "+
				  "  x   x  "+
				  "   x x   "+
				  "  x x x  "+
				  " x     x "+
				  "         "+
				  // PINE_TREE_LINE_7_7
				  "         "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " xxxxxxx "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // CAUTION_LINE_7_7
				  "         "+
				  " xxxxxxx "+
				  "  x   x  "+
				  "  x   x  "+
				  "   x x   "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // SHIP_LINE_7_7
				  "         "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  " xxxxxxx "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "         "+
				  // CIRCLE_FILLED_7_7
				  "         "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "         "+
				  // SQUARE_FILLED_7_7
				  "         "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "         "+
				  // DIAMOND_FILLED_7_7
				  "         "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "         "+
				  // TRIANGLE_FILLED_7_7
				  "         "+
				  "    x    "+
				  "    x    "+
				  "   xxx   "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "         "+
				  // RHOMBUS_FILLED_7_7
				  "         "+
				  "         "+
				  "    xxxxx"+
				  "   xxxxx "+
				  "  xxxxx  "+
				  " xxxxx   "+
				  "xxxxx    "+
				  "         "+
				  "         "+
				  // HOURGLASS_FILLED_7_7
				  "         "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "         "+
				  // SATELLITE_FILLED_7_7
				  "         "+
				  " x     x "+
				  "  x x x  "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "  x x x  "+
				  " x     x "+
				  "         "+
				  // PINE_TREE_FILLED_7_7
				  "         "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // CAUTION_FILLED_7_7
				  "         "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "   xxx   "+
				  "    x    "+
				  "    x    "+
				  "         "+
				  // SHIP_FILLED_7_7
				  "         "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "         "+
				  // CROSS_9_9
				  "x       x"+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "x       x"+
				  // PLUS_9_9
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "xxxxxxxxx"+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  // MINUS_9_9
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  "xxxxxxxxx"+
				  "         "+
				  "         "+
				  "         "+
				  "         "+
				  // SLASH_9_9
				  "        x"+
				  "       x "+
				  "      x  "+
				  "     x   "+
				  "    x    "+
				  "   x     "+
				  "  x      "+
				  " x       "+
				  "x        "+
				  // BACKSLASH_9_9
				  "x        "+
				  " x       "+
				  "  x      "+
				  "   x     "+
				  "    x    "+
				  "     x   "+
				  "      x  "+
				  "       x "+
				  "        x"+
				  // BAR_9_9,
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  // STAR_9_9
				  "    x    "+
				  " x  x  x "+
				  "  x x x  "+
				  "   xxx   "+
				  "xxxxxxxxx"+
				  "   xxx   "+
				  "  x x x  "+
				  " x  x  x "+
				  "    x    "+
				  // Y_9_9
				  "x       x"+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  // LIGHTNING_9_9
				  "    x    "+
				  "     x   "+
				  "      x  "+
				  "       x "+
				  "xxxxxxxxx"+
				  " x       "+
				  "  x      "+
				  "   x     "+
				  "    x    "+
				  // WELL_9_9
				  "    x    "+
				  "   xxx   "+
				  "   x x   "+
				  "   x x   "+
				  "  x x x  "+
				  "  xxxxx  "+
				  "  x x x  "+
				  " x x x x "+
				  "xxx   xxx"+
				  // CIRCLE_LINE_9_9
				  "   xxx   "+
				  " xx   xx "+
				  " x     x "+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  " x     x "+
				  " xx   xx "+
				  "   xxx   "+
				  // SQUARE_LINE_9_9
				  "xxxxxxxxx"+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  "x       x"+
				  "xxxxxxxxx"+
				  // DIAMOND_LINE_9_9
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "x       x"+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  // TRIANGLE_LINE_9_9
				  "    x    "+
				  "    x    "+
				  "   x x   "+
				  "   x x   "+
				  "  x   x  "+
				  "  x   x  "+
				  " x     x "+
				  " x     x "+
				  "xxxxxxxxx"+
				  // RHOMBUS_LINE_9_9
				  "         "+
				  "     xxxx"+
				  "    x   x"+
				  "   x   x "+
				  "  x   x  "+
				  " x   x   "+
				  "x   x    "+
				  "xxxx     "+
				  "         "+
				  // HOURGLASS_LINE_9_9
				  "xxxxxxxxx"+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "xxxxxxxxx"+
				  // SATELLITE_LINE_9_9
				  "x       x"+
				  " x xxx x "+
				  "  x   x  "+
				  " x     x "+
				  " x     x "+
				  " x     x "+
				  "  x   x  "+
				  " x xxx x "+
				  "x       x"+
				  // PINE_TREE_LINE_9_9
				  "    x    "+
				  "   x x   "+
				  "  x   x  "+
				  " x     x "+
				  "xxxxxxxxx"+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  // CAUTION_LINE_9_9
				  "xxxxxxxxx"+
				  " x     x "+
				  " x     x "+
				  "  x   x  "+
				  "  x   x  "+
				  "   x x   "+
				  "   x x   "+
				  "    x    "+
				  "    x    "+
				  // SHIP_LINE_9_9
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "xxxxxxxxx"+
				  " x     x "+
				  "  x   x  "+
				  "   x x   "+
				  "    x    "+
				  // CIRCLE_FILLED_9_9
				  "   xxx   "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "   xxx   "+
				  // SQUARE_FILLED_9_9
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  "xxxxxxxxx"+
				  // DIAMOND_FILLED_9_9
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "xxxxxxxxx"+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  // TRIANGLE_FILLED_9_9
				  "    x    "+
				  "    x    "+
				  "   xxx   "+
				  "   xxx   "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "xxxxxxxxx"+
				  // RHOMBUS_FILLED_9_9
				  "         "+
				  "     xxxx"+
				  "    xxxxx"+
				  "   xxxxx "+
				  "  xxxxx  "+
				  " xxxxx   "+
				  "xxxxx    "+
				  "xxxx     "+
				  "         "+
				  // HOURGLASS_FILLED_9_9
				  "xxxxxxxxx"+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "xxxxxxxxx"+
				  // SATELLITE_FILLED_9_9
				  "x       x"+
				  " x xxx x "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  " x xxx x "+
				  "x       x"+
				  // PINE_TREE_FILLED_9_9
				  "    x    "+
				  "   xxx   "+
				  "  xxxxx  "+
				  " xxxxxxx "+
				  "xxxxxxxxx"+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  // CAUTION_FILLED_9_9
				  "xxxxxxxxx"+
				  " xxxxxxx "+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "   xxx   "+
				  "    x    "+
				  "    x    "+
				  // SHIP_FILLED_9_9
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "    x    "+
				  "xxxxxxxxx"+
				  " xxxxxxx "+
				  "  xxxxx  "+
				  "   xxx   "+
				  "    x    "
				  //"#"
				;

	  
		  private static SbList <so_marker> markerlist;	  
		  private static byte[] markerimages;

		  private static void
		  convert_bitmaps()
		  {
		    int rpos = 0;
		    int wpos = 0;
		    for (int img = 0; img < SoMarkerSet.MarkerType.NUM_MARKERS.ordinal(); img++) {
		      for (int l=8;l>=0;l--) {
		        /*unsigned*/ byte v1 = 0;
		        /*unsigned*/ byte v2 = 0;
		        if (marker_char_bitmaps.charAt((l*9)+rpos  ) == 'x') v1 += 0x80;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+1) == 'x') v1 += 0x40;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+2) == 'x') v1 += 0x20;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+3) == 'x') v1 += 0x10;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+4) == 'x') v1 += 0x08;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+5) == 'x') v1 += 0x04;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+6) == 'x') v1 += 0x02;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+7) == 'x') v1 += 0x01;
		        if (marker_char_bitmaps.charAt((l*9)+rpos+8) == 'x') v2 += 0x80;
		        markerimages[wpos  ] = v1;
		        markerimages[wpos+1] = v2;
		        markerimages[wpos+2] = 0;
		        markerimages[wpos+3] = 0;
		        wpos += 4;
		      }
		      rpos += (9*9);
		    }
		  }

		/*!
		  Constructor.
		*/
		public SoMarkerSet()
		{
			nodeHeader.SO_NODE_CONSTRUCTOR(/*SoMarkerSet.class*/);
			nodeHeader.SO_NODE_ADD_MFIELD(markerIndex,"markerIndex", (0));
			this.isBuiltIn = true;
		}

		/*!
		  Destructor.
		*/
		public void destructor()
		{
			super.destructor();
		}

		// Internal method which translates the current material binding found
		// on the state to a material binding for this node.  PER_PART,
		// PER_FACE, PER_VERTEX and their indexed counterparts are translated
		// to PER_VERTEX binding. OVERALL means overall binding for point set
		// also, of course. The default material binding is OVERALL.
		private Binding
		findMaterialBinding(SoState state)
		{
		  Binding binding = Binding.OVERALL;
		  if (SoMaterialBindingElement.get(state) !=
		      SoMaterialBindingElement.Binding.OVERALL) binding = Binding.PER_VERTEX;
		  return binding;
		}
		
		private static boolean firsterror = true;

// doc in super
public void
GLRender(SoGLRenderAction action)
{
  // FIXME: the marker bitmaps are toggled off when the leftmost pixel
  // is outside the left border, and ditto for the bottommost pixel
  // versus the bottom border. They should be drawn partly until they
  // are wholly outside the canvas instead. 20011218 mortene.

  SoState state = action.getState();

  state.push();
  // We just disable lighting and texturing for markers, since we
  // can't see any reason this should ever be enabled.  send an angry
  // email to <pederb@coin3d.org> if you disagree.

  SoLazyElement.setLightModel(state, SoLazyElement.LightModel.BASE_COLOR.getValue());
  SoGLMultiTextureEnabledElement.set(state, this, 0, false);
  SoGLTexture3EnabledElement.set(state, /*this,*/ false);

  if (this.vertexProperty.getValue() != null) {
    this.vertexProperty.getValue().GLRender(action);
  }

  final SoCoordinateElement[] tmpcoord = new SoCoordinateElement[1]; // pointer
  final SbVec3fArray[]  dummy = new SbVec3fArray[1]; // pointer
  final boolean needNormals = false;

  SoVertexShape.getVertexData(state, tmpcoord, dummy, needNormals);

  if (!this.shouldGLRender(action)) {
    state.pop();
    return;
  }

  SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());

  SoGLCoordinateElement  coords = (SoGLCoordinateElement )tmpcoord[0];

  Binding mbind = this.findMaterialBinding(action.getState());

  final SoMaterialBundle mb = new SoMaterialBundle(action);
  mb.sendFirst();

  int idx = this.startIndex.getValue();
  int numpts = this.numPoints.getValue();
  if (numpts < 0) numpts = coords.getNum() - idx;

  int matnr = 0;

  final SbMatrix  mat = SoModelMatrixElement.get(state);
  final SbViewVolume  vv = SoViewVolumeElement.get(state);
  final SbViewportRegion  vp = SoViewportRegionElement.get(state);
  final SbMatrix  projmatrix = (mat.operator_mul(SoViewingMatrixElement.get(state)).operator_mul(
                                 SoProjectionMatrixElement.get(state)));
  SbVec2s vpsize = new SbVec2s(vp.getViewportSizePixels());
  
  GL2 gl2 = state.getGL2();

  gl2.glMatrixMode(GL2.GL_MODELVIEW);
  gl2.glPushMatrix();
  gl2.glLoadIdentity();
  gl2.glMatrixMode(GL2.GL_PROJECTION);
  gl2.glPushMatrix();
  gl2.glLoadIdentity();
  gl2.glOrtho(0, vpsize.getValue()[0], 0, vpsize.getValue()[1], -1.0f, 1.0f);

  for (int i = 0; i < numpts; i++) {
    int midx = Math.min(i, this.markerIndex.getNum() - 1);
//#if COIN_DEBUG
      if (midx < 0 || (this.markerIndex.operator_square_bracketI(midx) >= markerlist.getLength())) {
        // static boolean firsterror = true;
        if (firsterror) {
        	if(midx>= 0) {
        		SoDebugError.postWarning("SoMarkerSet.GLRender",
                                    "markerIndex "+markerIndex.operator_square_bracket(i)+" out of bound");
        	}
        	else {
          SoDebugError.postWarning("SoMarkerSet.GLRender",
                  "markerIndex field empty");
        	}
          firsterror = false;
        }
        // Don't render, jump back to top of for-loop and continue with
        // next index.
        continue;
      }
//#endif // COIN_DEBUG

    if (mbind == Binding.PER_VERTEX) mb.send(matnr++, true);

    final SbVec3fSingle point = new SbVec3fSingle(coords.get3(idx));
    idx++;

    if (this.markerIndex.operator_square_bracketI(midx) == NONE) { continue; }

    // OpenGL's glBitmap() will not be clipped against anything but
    // the near and far planes. We want markers to also be clipped
    // against other clipping planes, to behave like the SoPointSet
    // superclass.
    final SbBox3f bbox = new SbBox3f(point, point);
    // FIXME: if there are *heaps* of markers, this next line will
    // probably become a bottleneck. Should really partition marker
    // positions in a oct-tree data structure and cull several at
    // the same time.  20031219 mortene.
    if (SoCullElement.cullTest(state, bbox, true)) { continue; }

    projmatrix.multVecMatrix(point, point);
    point.getValue()[0] = (point.getValueRead()[0] + 1.0f) * 0.5f * vpsize.getValue()[0];
    point.getValue()[1] = (point.getValueRead()[1] + 1.0f) * 0.5f * vpsize.getValue()[1];      

    // To have the exact center point of the marker drawn at the
    // projected 3D position.  (FIXME: I haven't actually checked that
    // this is what TGS' implementation of the SoMarkerSet node does
    // when rendering, but it seems likely. 20010823 mortene.)
    so_marker tmp = (markerlist).operator_square_bracket(this.markerIndex.operator_square_bracketI(midx) );
    point.getValue()[0] = point.getValueRead()[0] - (tmp.width - 1) / 2;
    point.getValue()[1] = point.getValueRead()[1] - (tmp.height - 1) / 2;

    gl2.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, tmp.align);
    gl2.glRasterPos3f(point.getValueRead()[0], point.getValueRead()[1], -point.getValueRead()[2]);
    gl2.glBitmap(tmp.width, tmp.height, 0, 0, 0, 0, tmp.data, tmp.dataOffset);
  }

  // FIXME: this looks wrong, shouldn't we rather reset the alignment
  // value to what it was previously?  20010824 mortene.
  gl2.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 4); // restore default value
  gl2.glMatrixMode(GL2.GL_PROJECTION);
  gl2.glPopMatrix();
  gl2.glMatrixMode(GL2.GL_MODELVIEW);
  gl2.glPopMatrix();

  state.pop(); // we pushed, remember
  
  mb.destructor();
}

// ----------------------------------------------------------------------------------------------------

// Documented in superclass.
public void
getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  // Overridden to add the number of markers to the number of images
  // in \a action.

  if (!this.shouldPrimitiveCount(action)) return;

  SoState state = action.getState();

  state.push(); // in case we have a vertexProperty node

  if (this.vertexProperty.getValue() != null) {
    this.vertexProperty.getValue().getPrimitiveCount(action);
  }

  final SoCoordinateElement[] coord = new SoCoordinateElement[1];
  final SbVec3fArray[] dummy = new SbVec3fArray[1];
  final boolean needNormals = false;

  SoVertexShape.getVertexData(state, coord, dummy,
                               needNormals);

  int idx = this.startIndex.getValue();
  int numpts = this.numPoints.getValue();
  if (numpts < 0) numpts = coord[0].getNum() - idx;

  action.addNumImage(numpts);

  state.pop();
}

/*!
  Returns the number of defined markers.
 */
public static int
getNumDefinedMarkers()
{
  return markerlist.getLength();
}

static void
swap_leftright( byte[] data, int width, int height)
{
  // FIXME: sloppy code... 20000906. skei
  byte t;

  int y;
  int linewidth = (width + 7) / 8;
  for (y=0; y<height; y++) {
    for (int x=0; x < (int) Math.floor((double)(linewidth/2)); x++) {
      byte tmp = data[y*linewidth+x];
      data[ y*linewidth + x ] = data[ (y*linewidth) + (linewidth-x-1) ];
      data[ (y*linewidth) + (linewidth-x-1) ] = tmp;
    }
  }
  for (y=0; y<height; y++) {
    for (int x=0; x<linewidth; x++) {
      t = 0;
      if ((data[y*linewidth+x] & 128) != 0) t += 1;
      if ((data[y*linewidth+x] &  64) != 0) t += 2;
      if ((data[y*linewidth+x] &  32) != 0) t += 4;
      if ((data[y*linewidth+x] &  16) != 0) t += 8;
      if ((data[y*linewidth+x] &   8) != 0) t += 16;
      if ((data[y*linewidth+x] &   4) != 0) t += 32;
      if ((data[y*linewidth+x] &   2) != 0) t += 64;
      if ((data[y*linewidth+x] &   1) != 0) t += 128;
      data[y*linewidth+x] = t;
    }
  }
}

static void
swap_updown(byte[] data, int width, int height)
{
  int linewidth = (width + 7) / 8;
  for (int y = 0; y < (height>>1); y++) {
    for (int x = 0; x < linewidth; x++) {
      byte tmp = data[y*linewidth+x];
      data[y*linewidth + x] = data[((height-y-1)*linewidth) + x];
      data[((height-y-1)*linewidth) + x] = tmp;
    }
  }
}

/*!
  Replace the bitmap for the marker at \a idx with the
  representation given by \a size dimensions with the bitmap data at
  \a bytes. \a isLSBFirst and \a isUpToDown indicates how the bitmap
  data is ordered. Does nothing if \a markerIndex is NONE.

  Here's a complete usage example which demonstrates how to set up a
  user-specified marker from a char-map.  Note that the "multi-colored"
  pixmap data is converted to a monochrome bitmap before being passed to
  addMarker() because addMarker() supports only bitmaps.

  \code
  const int WIDTH = 18;
  const int HEIGHT = 19;
  const int BYTEWIDTH = (WIDTH + 7) / 2;

  const char coin_marker[WIDTH * HEIGHT + 1] = {
    ".+                "
    "+@.+              "
    " .@#.+            "
    " +$@##.+          "
    "  .%@&##.+        "
    "  +$@&&*##.+      "
    "   .%@&&*=##.+    "
    "   +$@&&&&=-##.+  "
    "    .%@&&&&&-;#&+ "
    "    +$@&&&&&&=#.  "
    "     .%@&&&&*#.   "
    "     +$@&&&&#.    "
    "      .%@&@%@#.   "
    "      +$%@%.$@#.  "
    "       .%%. .$@#. "
    "       +$.   .$>#."
    "        +     .$. "
    "               .  "
    "                  " };

  int byteidx = 0;
  unsigned char bitmapbytes[BYTEWIDTH * HEIGHT];
  for (int h = 0; h < HEIGHT; h++) {
    unsigned char bits = 0;
    for (int w = 0; w < WIDTH; w++) {
      if (coin_marker[(h * WIDTH) + w] != ' ') { bits |= (0x80 >> (w % 8)); }
      if ((((w + 1) % 8) == 0) || (w == WIDTH - 1)) {
        bitmapbytes[byteidx++] = bits;
        bits = 0;
      }
    }
  }

  int MYAPP_ARROW_IDX = SoMarkerSet.getNumDefinedMarkers(); // add at end
  SoMarkerSet.addMarker(MYAPP_ARROW_IDX, SbVec2s(WIDTH, HEIGHT),
                         bitmapbytes, false, true);
  \endcode

  This will provide you with an index given by MYAPP_ARROW_IDX which
  can be used in SoMarkerSet.markerIndex to display the new marker.
*/
public static void
addMarker(int idx, final SbVec2s  size,
                       byte[] bytes)
{
	addMarker(idx, size,
            bytes, true);	
}
public static void
addMarker(int idx, final SbVec2s  size,
                       byte[] bytes, boolean isLSBFirst)
{
	addMarker(idx, size,
            bytes, isLSBFirst,
            true);	
}
public static void
addMarker(int idx, final SbVec2s  size,
                       byte[] bytes, boolean isLSBFirst,
                       boolean isUpToDown)
{
  if (idx == NONE) return;

  boolean appendnew = idx >= markerlist.getLength() ? true : false;
  final so_marker tempmarker = new so_marker();
  so_marker temp = tempmarker;
  if (appendnew) {
    tempmarker.width  = 0;
    tempmarker.height = 0;
    tempmarker.align  = 0;
    tempmarker.data   = null;
    tempmarker.deletedata = false;
    while (idx > markerlist.getLength()) markerlist.append(tempmarker);
  }
  else temp = (markerlist).operator_square_bracket(idx);
  temp.width = size.getValue()[0];
  temp.height = size.getValue()[1];
  temp.align = 1;

  int datasize = ((size.getValue()[0] + 7) / 8) * size.getValue()[1];
  //if (temp.deletedata) delete temp.data; java port
  temp.deletedata = true;
  temp.data = new byte[ datasize ];
  Util.memcpy(temp.data,bytes,datasize);
  temp.dataOffset = 0; // java port
  // FIXME: the swap_leftright() function seems
  // buggy. Investigate. 20011120 mortene.
  if (isLSBFirst) { swap_leftright(temp.data,size.getValue()[0],size.getValue()[1]); }
  if (isUpToDown) { swap_updown(temp.data,size.getValue()[0],size.getValue()[1]); }
  if (appendnew) markerlist.append(tempmarker);
}

/*!
  Returns data for marker at \a idx in the \a size, \a bytes and
  \a isLSBFirst parameters.

  If no marker is defined for given \a idx, or SoMarkerSet.markerIndex
  is NONE (not removable), \c false is returned. If everything is OK,
  \c true is returned.
*/
public static boolean
getMarker(int idx, final SbVec2s  size,
                       byte[][] bytes, boolean[] isLSBFirst)
{
  // FIXME: handle isLSBFirst. skei 20000905
  if (idx == NONE ||
      idx >= markerlist.getLength()) return false;
  so_marker temp = (markerlist).operator_square_bracket(idx);
  size.getValue()[0] = (short)temp.width;
  size.getValue()[1] = (short)temp.height;
  bytes[0] = temp.data;
  isLSBFirst[0] = false;
  return true;
}

/*!
  Removes marker at \a idx.

  If no marker is defined for given \a idx, or SoMarkerSet.markerIndex
  is NONE (not removable), \c false is returned. If everything is OK,
  \c true is returned.
*/
public static boolean
removeMarker(int idx)
{
  if (idx == NONE ||
      idx >= markerlist.getLength()) return false;
  so_marker tmp = (markerlist).operator_square_bracket(idx);
  //if (tmp.deletedata) delete tmp.data; java port
  markerlist.remove(idx);
  return true;
}

/*!
  Not supported in Coin. Should probably not have been part of the
  public Open Inventor API.
*/
public boolean
isMarkerBitSet(int idx, int bitNumber)
{
  // FIXME: seems simple enough to support.. 20010815 mortene.
  //COIN_OBSOLETED();
  return false;
}
		  
	  
	// doc in super
	  public static void
	  initClass()
	  {
	    //SO_NODE_INTERNAL_INIT_CLASS(SoMarkerSet, SO_FROM_INVENTOR_2_5|SO_FROM_COIN_1_0);
		  SoSubNode.SO__NODE_INIT_CLASS(SoMarkerSet.class, "MarkerSet", SoPointSet.class);
		  
		  
	    markerimages = new byte[MarkerType.NUM_MARKERS.ordinal()*9*4]; // hardcoded markers, 32x9 bitmaps (9x9 used), dword alignment
	    markerlist = new SbList<so_marker>();
	    //coin_atexit((coin_atexit_f *)free_marker_images, CC_ATEXIT_NORMAL);
	    convert_bitmaps();
	    for (int i = 0; i < MarkerType.NUM_MARKERS.ordinal(); i++) {
			final so_marker temp = new so_marker();
			temp.width  = 9;
			temp.height = 9;
			temp.align  = 4;
			temp.data   = markerimages;
			temp.dataOffset = (i * 36);
			temp.deletedata = false;
			markerlist.append(temp);
	    }
	  }
	public static void addMarker(int markerIndex2, SbVec2s size, byte[] bytes, int[] data) {
		// TODO
		addMarker(markerIndex2,size,bytes);
	}
	
	/**
	 * Java port
	 * @param idx
	 * @return
	 */
	public static Object getMarker(int idx) {
		final SbVec2s  size = new SbVec2s();
		byte[][] bytes = new byte[1][];
		boolean[] isLSBFirst = new boolean[1];
		return getMarker(idx, size, bytes, isLSBFirst) ? true : null;
	}

}
