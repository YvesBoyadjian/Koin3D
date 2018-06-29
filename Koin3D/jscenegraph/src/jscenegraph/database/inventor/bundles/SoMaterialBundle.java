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
 |      This file defines the SoMaterialBundle class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.bundles;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Destroyable;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoMaterialBundle
///
///  Bundle that allows shapes to deal with materials more easily.
///  Since materials deal with several elements simultaneously, this
///  bundle hides a lot of this work from shapes.
///
///  Much of the functionality of this bundle has been replaced by the
///  SoLazyElement. 
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterialBundle extends SoBundle implements Destroyable {
	
 private   boolean              firstTime;      //!< TRUE if nothing sent yet
 private   int                 lastIndex;      //!< Last index reallySend()ed
 private   boolean              colorOnly;      //!< TRUE if only base color sent
 private   boolean              fastColor;      //!< TRUE if can send color for diffColor
 private   int                 numMaterials;   //!< Number of materials 
 private   boolean              sendMultiple;   //!< indicates multiple diffuse are sent.

    //! Material component elements:
 private   SoGLLazyElement               lazyElt;

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor. Does as little as possible now to avoid making
//    unnecessary calls.
//
// Use: public

public SoMaterialBundle(SoAction action) {
	super(action);

//
////////////////////////////////////////////////////////////////////////


    // Remember that we haven't accessed elements yet
    firstTime = true;
    lastIndex = -1;
    lazyElt = null;  

    // See whether we need to deal with materials or just colors
    colorOnly = ((SoLazyElement.getLightModel(state)) ==
                 SoLazyElement.LightModel.BASE_COLOR.getValue());
    sendMultiple = false;
    fastColor = false;

    // Nodes that use material bundles haven't been optimized, and
    // should be render cached if possible:
    SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{    
    //If multiple diffuse colors were sent, we don't know what color
    // was left in GL.   
    if (sendMultiple)        
        lazyElt.reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    // turn off ColorMaterial, if it was turned on.
    if (fastColor)  
        SoGLLazyElement.setColorMaterial(state, false);
    super.destructor();
}



    //! This can be called by shapes to prepare for sending multiple
    //! materials later on. It is not required, since all of the send
    //! methods call it if necessary. However, it allows the bundle to
    //! set up some optimizations that are not possible to do later on.
    //! This should never be called between calls to glBegin() and
    //! glEnd().
    public void                setUpMultiple()    
            { accessElements(false, false);
              sendMultiple = true; }


    //! Makes sure the first defined material in the state is sent to
    //! GL. This should never be called between calls to glBegin() and
    //! glEnd().
	public    void                sendFirst()             { send(0, false); }

    //! Sends indexed material to the GL if it's not already there.
    //! The second paramater should be TRUE if this send is between a
    //! glBegin() and glEnd() call, in which case the stipple pattern
    //! commands for screen-door transparency won't be sent.
    public void                send(int index, boolean isBetweenBeginEnd)
        {  reallySend(index, isBetweenBeginEnd, false);  }
            
    //! Sends indexed material to the GL even if it's already there.
    //! This method can be used by SoMaterial and related nodes to send
    //! the base material to the GL to avoid cache dependencies on materials.
    public void                forceSend(int index)
        { reallySend(index, false, true); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Really sends material to the GL.
//
// Use: private

private void
reallySend(int index, boolean isBetweenBeginEnd,
                             boolean avoidFastColor)
//
////////////////////////////////////////////////////////////////////////
{
    // Make sure we have accessed all required elements (first time only)
    if (firstTime){
        accessElements(isBetweenBeginEnd, avoidFastColor);
        firstTime = false;
        // the first color has already been sent--
        if (index == 0) {
            lastIndex = index;
            return;
        }
    }

    if (lastIndex == index) return;

    //Indicate multiple colors are being sent:  This will force reset() after
    //shape is rendered
    sendMultiple = true;

//#ifdef DEBUG
    // Make sure the index is valid
    if (index >= numMaterials){
        SoDebugError.post("SoMaterialBundle::reallySend", 
            "Not enough colors specified");
    }    
//#endif
    lazyElt.sendDiffuseByIndex(index);
    lastIndex = index;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Accesses and stores pointers to the elements needed by
//    the bundle. This is done once per bundle construction, but is
//    delayed until the elements are actually needed. This also sets
//    up some other flags and values in the element, including the
//    number of materials
//
//
// Use: private

private void
accessElements(boolean isBetweenBeginEnd,
                                 boolean avoidFastColor)
//
////////////////////////////////////////////////////////////////////////
{   
    SoLazyElement le = SoLazyElement.getInstance(state);   

    // Initialize
    numMaterials = le.getNumDiffuse();

    // Determine if we can use "fast colors" - just sending GL colors
    // in place of the diffuse colors. This saves both time and space,
    // especially when materials are sent within GL display lists.
    // Note: we can't make the necessary GL calls if we are between a
    // glBegin() and a glEnd(), so that case is considered here.
    fastColor = (! isBetweenBeginEnd &&
                 ! colorOnly &&         
                 numMaterials > 1 );

    // Set up GL if necessary for fast color mode. When materials are
    // "forced" to send (as by SoMaterial nodes), we don't want to set
    // up fast colors. So check that case, too. 
    if (fastColor && !avoidFastColor)  
        SoGLLazyElement.setColorMaterial(state, true);
    
    //Note: it's important to save the lazyElt AFTER the first set(),
    //so that subsequent lazyElt->send()'s will use top-of-stack.
    lazyElt = ( SoGLLazyElement )SoLazyElement.getInstance(state);
    if (!colorOnly)   
        lazyElt.send(state,SoLazyElement.masks.ALL_MASK.getValue());
    else {
        lazyElt.send(state,SoLazyElement.internalMasks.DIFFUSE_ONLY_MASK.getValue());
    }
}

    //! Returns TRUE if only base color part of material is used
public boolean              isColorOnly()      { return colorOnly; }



}
