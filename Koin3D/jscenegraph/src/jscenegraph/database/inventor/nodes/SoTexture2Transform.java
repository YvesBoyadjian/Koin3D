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
 |      This file defines the SoTexture2Transform node class.
 |
 |   Author(s)          : Paul S. Strauss, Thaddeus Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLTextureMatrixElement;
import jscenegraph.database.inventor.elements.SoTextureMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! 2D texture transformation node.
/*!
\class SoTexture2Transform
\ingroup Nodes
This node defines a 2D transformation applied to texture coordinates.
This affects the way textures are applied to the surfaces of
subsequent shapes. The transformation consists of (in order) a
non-uniform scale about an arbitrary center point, a rotation about
that same point, and a translation. (Note: while the transformations
can be thought of as being applied in that order, the GL matrices are
actually premultiplied in the opposite order. Therefore, the
operations are listed in the reverse order throughout this reference page.)
This allows a user to change the size and position of the textures on
objects.

\par File Format/Default
\par
\code
Texture2Transform {
  translation 0 0
  rotation 0
  scaleFactor 1 1
  center 0 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Concatenates transformation with the current texture transformation. 

\par See Also
\par
SoTexture2, SoTextureCoordinate2, SoTextureCoordinateFunction
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTexture2Transform extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTexture2Transform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTexture2Transform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTexture2Transform.class); }    	  	
	
    //! Translation in S and T.
    public final SoSFVec2f           translation = new SoSFVec2f();    

    //! Counter-clockwise rotation of the coordinate space, in radians. This
    //! results in a clockwise rotation of the texture on the object.
    public final SoSFFloat           rotation = new SoSFFloat();       

    //! Scaling factors in S and T.
    public final SoSFVec2f           scaleFactor = new SoSFVec2f();    

    //! Center point used for scaling and rotation.
    public final SoSFVec2f           center = new SoSFVec2f();         


    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTexture2Transform()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTexture2Transform*/);
	nodeHeader.SO_NODE_ADD_SFIELD(translation,"translation", (new SbVec2f(0.0f, 0.0f)));
	nodeHeader.SO_NODE_ADD_SFIELD(rotation,"rotation",    (0.0f));
	nodeHeader.SO_NODE_ADD_SFIELD(scaleFactor,"scaleFactor", (new SbVec2f(1.0f, 1.0f)));
	nodeHeader.SO_NODE_ADD_SFIELD(center,"center",      (new SbVec2f(0.0f, 0.0f)));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does all the work for most actions
//
// Use: extender

public void
SoTexture2Transform_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    boolean        doCenter;
    SoState     state = action.getState();
    final SbVec2f     t2 = new SbVec2f();
    final SbVec3f     t3 = new SbVec3f();

    doCenter = (! center.isIgnored() && ! center.isDefault());

    // Do these in GL (right-to-left) order
    if (! translation.isIgnored() && ! translation.isDefault()) {
        t2.copyFrom(translation.getValue());
        t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 0.0f);
        SoTextureMatrixElement.translateBy(state, this, t3);
    }
    
    if (doCenter) {
        t2.copyFrom(center.getValue());
        t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 0.0f);
        SoTextureMatrixElement.translateBy(state, this, t3);
    }

    if (! rotation.isIgnored() && ! rotation.isDefault()) {
        SbRotation tRot = new SbRotation(new SbVec3f(0, 0, 1), rotation.getValue());
        SoTextureMatrixElement.rotateBy(state, this, tRot);
    }

    if (! scaleFactor.isIgnored() && ! scaleFactor.isDefault()) {
        t2.copyFrom(scaleFactor.getValue());
        t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 1.0f);
        SoTextureMatrixElement.scaleBy(state, this, t3);
    }
    if (doCenter) {
        t2.copyFrom(center.getValue().operator_minus());
        t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 0.0f);
        SoTextureMatrixElement.translateBy(state, this, t3);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles callback action
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTexture2Transform_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles GL render action
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTexture2Transform_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles get matrix action
//
// Use: extender

//Shorthand: accumulates translation stored in vector t2
private void TRANSLATE(
		final SbVec2f t2,
		final SbVec3f t3,
		final SbMatrix m,
		final SbMatrix ctm,
		final SbMatrix inv
		) {                                                           
	t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 0.0f);                                           
	m.setTranslate(t3);                                                       
	ctm.multLeft(m);                                                          
	m.setTranslate(t3.operator_minus());                                                      
	inv.multRight(m);
}
 
public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbMatrix    ctm = action.getTextureMatrix();
    final SbMatrix    inv = action.getTextureInverse();
    final SbMatrix    m = new SbMatrix();
    boolean        doCenter;
    final SbVec2f     t2 = new SbVec2f();
    final SbVec3f     t3 = new SbVec3f();


    doCenter = (! center.isIgnored() && ! center.isDefault());

    // Do these in GL (right-to-left) order
    if (! translation.isIgnored() && ! translation.isDefault()) {
        t2.copyFrom(translation.getValue());
        TRANSLATE(t2,t3,m,ctm,inv);
    }
    
    if (doCenter) {
        t2.copyFrom(center.getValue());
        TRANSLATE(t2,t3,m,ctm,inv);
    }

    if (! rotation.isIgnored() && ! rotation.isDefault()) {
        final SbRotation tRot = new SbRotation(new SbVec3f(0, 0, 1), rotation.getValue());
        tRot.getValue(m);
        ctm.multLeft(m);
        tRot.invert();
        tRot.getValue(m);
        inv.multRight(m);
    }

    if (! scaleFactor.isIgnored() && ! scaleFactor.isDefault()) {
        t2.copyFrom(scaleFactor.getValue());
        t3.setValue(t2.getValueRead()[0], t2.getValueRead()[1], 1.0f);
        m.setScale(t3);
        ctm.multLeft(m);
        t3.setValue(1.0f / t2.getValueRead()[0], 1.0f / t2.getValueRead()[1], 1.0f);
        m.setScale(t3);
        inv.multRight(m);
    }

    if (doCenter) {
        t2.copyFrom(center.getValue().operator_minus());
        TRANSLATE(t2,t3,m,ctm,inv);
    }

//#undef TRANSLATE
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles pick action.
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTexture2Transform_doAction(action);
}

    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTexture2Transform class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTexture2Transform.class, "Texture2Transform", SoNode.class);

    SO_ENABLE(SoCallbackAction.class,         SoTextureMatrixElement.class);
    SO_ENABLE(SoPickAction.class,             SoTextureMatrixElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoGLTextureMatrixElement.class);
}


}
