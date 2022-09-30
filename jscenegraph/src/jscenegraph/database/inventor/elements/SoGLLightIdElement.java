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
 |      This file defines the SoGLLightIdElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLLightIdElement
///  \ingroup Elements
///
///  Element that stores the id of the current light. The first light
///  active in a scene has id 0, the next has 1, and so on. This
///  element can be used to determine how many lights are currently
///  active. The initial value of this element is -1 to indicate that
///  there are no lights active.
///
///  Note that even though the effects of this element accumulate (each
///  light source increments the id), it is derived from
///  SoInt32Element. This is because each call to increment()
///  effectively does a "get" of the current top instance, so caching
///  knows about the dependency of each instance on the previous one.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLLightIdElement extends SoInt32Element {

  private
    static int          maxGLSources = -1;   //!< Holds max number of GL sources
	
    
	  public static void
   initClass(final Class<? extends SoElement> javaClass)
   {
	  SoElement.initClass(javaClass);
   }

    //! Returns the default light id
    public static int          getDefault()                    { return -1; }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    data = getDefault();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Increments the current light id. This should be called when a
//    light source becomes active. This returns the new id, or -1 if
//    the element is being overridden or if the maximum number of GL
//    light sources has been exceeded. Otherwise, enables light source.
//
// Use: public, static

public static int
increment(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = state.getGL2();
	
    SoGLLightIdElement  elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoGLLightIdElement ) getElement(state, classStackIndexMap.get(SoGLLightIdElement.class));

    // Being overridden?
    if (elt == null)
        return -1;

    // Increment the current data in the element. Since we initialize
    // this to -1 in init() and copy it in push(), the data field is
    // always up to date. This makes it possible to increment() twice
    // without a push() in between.
    elt.data++;

    // Too many sources?
    if (elt.data >= getMaxGLSources(gl2))
        return -1;

    // It's a valid source, so enable it
    gl2.glEnable((int)(GL2.GL_LIGHT0 + elt.data));

    return elt.data;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the maximum number of concurrent light sources supported
//    by GL implementation.
//
// Use: protected, static

protected static int
getMaxGLSources(GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
	
	
    // Inquire GL if not already done
    if (maxGLSources < 0) {
        final int[]   max = new int[1];
        gl2.glGetIntegerv(GL2.GL_MAX_LIGHTS, max,0);
        maxGLSources = max[0];
    }

    return maxGLSources;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying current id from previous top instance.
//
// Use: public

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    data = ((SoGLLightIdElement ) getNextInStack()).data;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, disabling light in GL
//
// Use: public

public void
pop(SoState state, SoElement prevTopElement)
//
////////////////////////////////////////////////////////////////////////
{
    // Since popping this element has GL side effects, make sure any
    // open caches capture it
    capture(state);
    
    GL2 gl2 = state.getGL2();

    SoGLLightIdElement prevElt =
        ( SoGLLightIdElement ) prevTopElement;
    int i, max;

    // Disable previous light(s), if valid. All lights between the
    // previous element and this one should be turned off.
    max = getMaxGLSources(gl2);
    for (i = (int) prevElt.data; i > data; i--)
        if (i < max)
            gl2.glDisable((int)(GL2.GL_LIGHT0 + i));
}

public static int
get(SoState state)
{
  return SoInt32Element.get(classStackIndexMap.get(SoGLLightIdElement.class), state);
}

}
