/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file contains the SbViewportRegion class definition.
 |
 |   Author(s)  : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.port.Mutable;


////////////////////////////////////////////////////////////////////////////////
//! Class for representing a viewport.
/*!
\class SbViewportRegion
\ingroup Basics
This class represents the active viewport region in a display
window. It contains the screen-space size of the window as well as
the origin and size of the viewport within the window. By default,
the viewport is the same as the full window. Methods allow the
viewport to be set either in terms of screen-space pixels or as
normalized coordinates, where (0,0) is the lower-left corner of
the window and (1,1) is the upper-right corner.

\par See Also
\par
SbVec2f, SbVec2s
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SbViewportRegion implements Mutable {

	private final SbVec2s windowSize = new SbVec2s();
	private final SbVec2f vpOrigin = new SbVec2f();
	private final SbVec2s vpOriginPix = new SbVec2s();
	private final SbVec2f vpSize = new SbVec2f();
	private final SbVec2s vpSizePix = new SbVec2s();
	private boolean vpSet;
	private float pixelsPerInch;
	
	public SbViewportRegion() {
		
		  windowSize.setValue((short)100, (short)100);
		   
		    // Viewport is full window
		    setFullViewport();
		   
		    vpSet = false;
		   
		    pixelsPerInch = 72.0f;
		  	}
	
	//
	 // Description:
	 // Constructor that takes window size as width and height in pixels.
	 //
	 // Use: public
		public SbViewportRegion(short width, short height) {
		
		  windowSize.setValue(width, height);
		   
		    // Viewport is full window
		    setFullViewport();
		   
		    vpSet = false;
		   
		    pixelsPerInch = 72.0f;
		   	}
		
		 ////////////////////////////////////////////////////////////////////////
		    //
		   // Description:
		   //    Constructor that takes window size as width and height in pixels
		   //    as SbVec2s.
		   //
		   // Use: public
		   
		  		public SbViewportRegion(final SbVec2s winSize) {
			
		  		     windowSize.copyFrom(winSize);
		  		    
		  		        // Viewport is full window
		  		        setFullViewport();
		  		    
		  		        vpSet = false;
		  		    
		  		        pixelsPerInch = 72.0f;
		  		    		}
		
		 ////////////////////////////////////////////////////////////////////////
		   //
		   // Description:
		   //    Constructor that takes an SbViewportRegion to copy from.
		   //
		   // Use: public
		   
		  public SbViewportRegion(final SbViewportRegion vpReg)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       this.copyFrom(vpReg);
		   }
		   
		      //! Changes window size to given width and height in pixels.
    public void                setWindowSize(short width, short height)
        { setWindowSize(new SbVec2s(width, height)); }

		  
		  
		  ////////////////////////////////////////////////////////////////////////
		   //
		   // Description:
		   //    Changes window size to given width and height in pixels.
		   //
		   // Use: public
		   
		  public void
		   setWindowSize(SbVec2s winSize)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       windowSize.copyFrom(winSize);
		   
		       if (! vpSet)
		           setFullViewport();
		   
		       else
		           adjustViewport();
		   }
		   

    //! Returns window size in pixels.
    public SbVec2s      getWindowSize()            { return windowSize; }

    //! Returns viewport origin in normalized coordinates.
    public SbVec2f      getViewportOrigin()        { return vpOrigin; }

    //! Returns viewport origin in pixels.
    public SbVec2s      getViewportOriginPixels() { return vpOriginPix; }

    //! Returns viewport size in normalized coordinates.
    public SbVec2f      getViewportSize()          { return vpSize; }

    //! Returns viewport size in pixels.
    public SbVec2s      getViewportSizePixels()   { return vpSizePix;}

		// Returns aspect ratio (width/height) of viewport. 
		public float getViewportAspectRatio() {
			 return (vpSizePix.operator_square_bracket(1) == 0 ? 1.0f :
				                     (float) vpSizePix.operator_square_bracket(0) / (float) vpSizePix.operator_square_bracket(1)); 
		}
		
		/**
		 * Scales viewport within window to be the given ratio of 
		 * its current width, leaving the resulting viewport 
		 * centered about the same point as the current one. 
		 * 
		 * @param ratio
		 */
		 ////////////////////////////////////////////////////////////////////////
		   //
		   // Description:
		   //    Scales viewport within window to be the given ratio of its
		   //    current width, leaving the resulting viewport centered about the
		   //    same point as the current one.
		   //
		   // Use: public
		   
		  public void
		   scaleWidth(float ratio)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       float       halfWidth   = vpSize.operator_square_bracket(0) / 2.0f;
		       float       widthCenter = vpOrigin.operator_square_bracket(0) + halfWidth;
		   
		       vpOrigin.operator_square_bracket(0, widthCenter - ratio * halfWidth);
		       vpSize.operator_square_bracket(0, vpSize.operator_square_bracket(0)  * ratio);
		   
		       // Make sure the viewport remains in the window
		       if (vpOrigin.operator_square_bracket(0) < 0.0)
		           vpOrigin.operator_square_bracket(0, 0.0f);
		       if (vpSize.operator_square_bracket(0) > 1.0)
		           vpSize.operator_square_bracket(0, 1.0f);
		   
		       adjustViewport();
		   }
		   		
		/**
		 * Scales viewport within window to be the given ratio 
		 * of its current height, leaving the resulting viewport 
		 * centered about the same point as the current one. 
		 * 
		 * @param ratio
		 */
		  ////////////////////////////////////////////////////////////////////////
		   //
		   // Description:
		   //    Scales viewport within window to be the given ratio of its
		   //    current height, leaving the resulting viewport centered about the
		   //    same point as the current one.
		   //
		   // Use: public
		   
		   public void
		   scaleHeight(float ratio)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       float       halfHeight   = vpSize.operator_square_bracket(1) / 2.0f;
		       float       heightCenter = vpOrigin.operator_square_bracket(1) + halfHeight;
		   
		       vpOrigin.operator_square_bracket(1, heightCenter - ratio * halfHeight);
		       vpSize.operator_square_bracket(1 , vpSize.operator_square_bracket(1) * ratio);
		   
		       // Make sure the viewport remains in the window
		       if (vpOrigin.operator_square_bracket(1) < 0.0)
		           vpOrigin.operator_square_bracket(1, 0.0f);
		       if (vpSize.operator_square_bracket(1) > 1.0)
		           vpSize.operator_square_bracket(1 , 1.0f);
		   
		       adjustViewport();
		   }
		   		
		 //
		 // Description:
		 // Sets viewport to full window. Assumes viewport was not set
		 // explicitly by caller.
		 //
		 // Use: private
				private void setFullViewport() {
					  vpOrigin.setValue(0.0f, 0.0f);
					    vpSize.setValue(1.0f, 1.0f);
					    vpOriginPix.setValue((short)0, (short)0);
					    vpSizePix.operator_assign(windowSize);
					  		}
				
				/**
				 * Adjusts viewport pixel size based on new window size 
				 * or new viewport. 
				 */
				 ////////////////////////////////////////////////////////////////////////
				   //
				   // Description:
				   //    Adjusts viewport pixel size based on new window size or new
				   //    viewport. Assumes viewport was set explicitly by caller.
				   //
				   // Use: private
				   
				  private void
				   adjustViewport()
				   //
				   ////////////////////////////////////////////////////////////////////////
				   {
				       vpOriginPix.setValue((short) (vpOrigin.operator_square_bracket(0) * windowSize.operator_square_bracket(0)),
				                            (short) (vpOrigin.operator_square_bracket(1) * windowSize.operator_square_bracket(1)));
				       vpSizePix.setValue((short) (vpSize.operator_square_bracket(0) * windowSize.operator_square_bracket(0)),
				                          (short) (vpSize.operator_square_bracket(1) * windowSize.operator_square_bracket(1)));
				   }
		@Override
		public void copyFrom(Object other) {
			SbViewportRegion otherVP = (SbViewportRegion)other;
			windowSize.copyFrom(otherVP.windowSize);
			vpOrigin.copyFrom(otherVP.vpOrigin);
			vpOriginPix.copyFrom(otherVP.vpOriginPix);
			vpSize.copyFrom(otherVP.vpSize);
			vpSizePix.copyFrom(otherVP.vpSizePix);
			vpSet = otherVP.vpSet;
			pixelsPerInch = otherVP.pixelsPerInch;
			
		}

    //! Convenience function that returns number of pixels per printer's point.
    public float               getPixelsPerPoint() 
        { return pixelsPerInch / 72.0f; }

		
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if two viewport regions are identical.
//
// Use: public

public boolean
operator_equal_equal( SbViewportRegion reg2)
{
	SbViewportRegion reg1 = this;
    return (reg1.windowSize.operator_equal_equal(reg2.windowSize) &&
            reg1.vpOrigin.operator_equal_equal(reg2.vpOrigin)   &&
            reg1.vpSize.operator_equal_equal(reg2.vpSize));
}

public boolean operator_not_equal(SbViewportRegion oldvp) {
	return !operator_equal_equal(oldvp);
}

}
