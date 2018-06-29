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
 |      This file defines the SoTransform node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoBBoxModelMatrixElement;
import jscenegraph.database.inventor.elements.SoGLModelMatrixElement;
import jscenegraph.database.inventor.elements.SoLocalBBoxMatrixElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! General 3D geometric transformation node.
/*!
\class SoTransform
\ingroup Nodes
This node defines a geometric 3D transformation consisting of (in
order) a (possibly) non-uniform scale about an arbitrary point, a
rotation about an arbitrary point and axis, and a translation. (While
the transformations can be thought of as being applied in that order,
matrices are actually premultiplied in the opposite order. Therefore,
the operations are listed in the reverse order throughout this reference
page.)

\par File Format/Default
\par
\code
Transform {
  translation 0 0 0
  rotation 0 0 1 0
  scaleFactor 1 1 1
  scaleOrientation 0 0 1 0
  center 0 0 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Accumulates transformation into the current transformation. 
\par
SoGetMatrixAction
<BR> Returns the matrix corresponding to the total transformation. 

\par See Also
\par
SoMatrixTransform, SoResetTransform, SoRotation, SoRotationXYZ, SoScale, SoTransformManip, SoTransformSeparator, SoTranslation
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTransform extends SoTransformation {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTransform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTransform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTransform.class); }    	  	
	

	public final SoSFVec3f translation = new SoSFVec3f();
	
	public final SoSFRotation rotation = new SoSFRotation();
	
	public final SoSFVec3f scaleFactor = new SoSFVec3f();
	
    //! Rotational orientation for scale.
	public final SoSFRotation scaleOrientation = new SoSFRotation();
	
    //! Origin for scale and rotation.
	public final SoSFVec3f center = new SoSFVec3f();


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTransform class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoTransform.class, "Transform", SoTransformation.class);

//    SO_ENABLE(SoCallbackAction,         SoModelMatrixElement);
    SoCallbackAction.enableElement(SoModelMatrixElement.class);
//    SO_ENABLE(SoGetBoundingBoxAction,   SoBBoxModelMatrixElement);
    SoGetBoundingBoxAction.enableElement(SoBBoxModelMatrixElement.class);
//    SO_ENABLE(SoGetBoundingBoxAction,   SoLocalBBoxMatrixElement);
    SoGetBoundingBoxAction.enableElement(SoLocalBBoxMatrixElement.class);
//    SO_ENABLE(SoPickAction,             SoModelMatrixElement);
    SoPickAction.enableElement(SoModelMatrixElement.class);
//    SO_ENABLE(SoGLRenderAction,         SoGLModelMatrixElement);
    SoGLRenderAction.enableElement(SoGLModelMatrixElement.class);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTransform()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTransform*/);
    nodeHeader.SO_NODE_ADD_SFIELD(translation,"translation",     new SbVec3f(0.0f, 0.0f, 0.0f));
    nodeHeader.SO_NODE_ADD_SFIELD(rotation,"rotation",        new SbRotation(0.0f, 0.0f, 0.0f, 1.0f));
    nodeHeader.SO_NODE_ADD_SFIELD(scaleFactor,"scaleFactor",     new SbVec3f(1.0f, 1.0f, 1.0f));
    nodeHeader.SO_NODE_ADD_SFIELD(scaleOrientation,"scaleOrientation",new SbRotation(0.0f, 0.0f, 0.0f, 1.0f));
    nodeHeader.SO_NODE_ADD_SFIELD(center,"center",          new SbVec3f(0.0f, 0.0f, 0.0f));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the fields in the node to implement the transformation
//    represented by the given matrix.
//
// Use: public

public void setMatrix(final SbMatrix mat)
//
////////////////////////////////////////////////////////////////////////
{
    // Factor mat into the necessary fields, while keeping the center
    // field unchanged. Therefore, we need to factor a new matrix,
    // [mNoCenter], given by:
    //          [centerInverse][mNoCenter][center] = [mat]
    //
    // So, [mNoCenter] = [center][mat][centerInverse]

    final SbMatrix mNoCenter = new SbMatrix(), subMat = new SbMatrix();
    mNoCenter.setTranslate(center.getValue().operator_minus());
    mNoCenter.multLeft(mat);
    subMat.setTranslate(center.getValue());
    mNoCenter.multLeft(subMat);

    // Now, factor mNoCenter and plug it back into the fields
    final SbMatrix    shearRotMat = new SbMatrix(), rotMat = new SbMatrix(), projMat = new SbMatrix();
    final SbVec3f     sVec = new SbVec3f(), tVec = new SbVec3f();

    if (mNoCenter.factor(shearRotMat, sVec, rotMat, tVec, projMat)) {
        final SbRotation      rot = new SbRotation(rotMat);

        if (translation.getValue().operator_not_equal(tVec))
            translation.operator_assign(tVec);

        if (rotation.getValue().operator_not_equal(rot))
            rotation.operator_assign(rot);

        if (scaleFactor.getValue().operator_not_equal(sVec))
            scaleFactor.operator_assign(sVec);
        
        // Don't change the scale orientation if the scale is unity
        if (sVec.operator_not_equal(new SbVec3f(1.0f, 1.0f, 1.0f))) {
            rot.copyFrom( new SbRotation(shearRotMat.transpose()));

            if (scaleOrientation.getValue() != rot)
                scaleOrientation.operator_assign(rot);
        }
    }
    else
        SoDebugError.post("SoTransform.setMatrix",
                           "Can't factor given matrix");
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does all the work for most actions.
//
// Use: extender

public void SoTransform_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    boolean        doCenter, doScaleOrient;
    SoState     state = action.getState();

    doCenter      = (! center.isIgnored() && ! center.isDefault());
    doScaleOrient = (! scaleOrientation.isIgnored() &&
                     ! scaleOrientation.isDefault());

    // Do these in GL (right-to-left) order
    if (! translation.isIgnored() && ! translation.isDefault())
        SoModelMatrixElement.translateBy(state, this, translation.getValue());

    if (doCenter)
        SoModelMatrixElement.translateBy(state, this, center.getValue());

    if (! rotation.isIgnored() && ! rotation.isDefault())
        SoModelMatrixElement.rotateBy(state, this, rotation.getValue());

    if (! scaleFactor.isIgnored() && ! scaleFactor.isDefault()) {

        if (doScaleOrient)
            SoModelMatrixElement.rotateBy(state, this,
                                           scaleOrientation.getValue());

        SoModelMatrixElement.scaleBy(state, this, scaleFactor.getValue());

        if (doScaleOrient) {
            SbRotation r = new SbRotation(scaleOrientation.getValue());
            r.invert();
            SoModelMatrixElement.rotateBy(state, this, r);
        }
    }

    if (doCenter)
        SoModelMatrixElement.translateBy(state, this, center.getValue().operator_minus());
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
    SoTransform_doAction(action);
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
    SoTransform_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles get bounding box action
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTransform_doAction(action);
}


// Shorthand for accumulating a rotation into matrix and inverse using
// intermediate matrix m. Destructive to "rot".
private void ROTATE(SbRotation rot, SbMatrix matrix, SbMatrix inverse, SbMatrix m) {                                       
    rot.getValue(m);                                                          
    matrix.multLeft(m);                                                       
    rot.invert();                                                             
    rot.getValue(m);                                                          
    inverse.multRight(m);
}
// Shorthand for accumulating a scale vector into matrix and inverse
// using intermediate matrix m. Destructive to "vec".
private void SCALE(SbVec3f vec, SbMatrix matrix, SbMatrix inverse, SbMatrix m) {                                        
    m.setScale(vec);                                                          
    matrix.multLeft(m);                                                       
    vec.getValue()[0] = 1.0f / vec.getValue()[0];                                                    
    vec.getValue()[1] = 1.0f / vec.getValue()[1];                                                    
    vec.getValue()[2] = 1.0f / vec.getValue()[2];                                                    
    m.setScale(vec);                                                          
    inverse.multRight(m);
}
// Shorthand for accumulating a translation vector into matrix and
// inverse using intermediate matrix m. Non-destructive to "vec".
private void TRANSLATE(SbVec3f vec, SbMatrix matrix, SbMatrix inverse, SbMatrix m) {                                    
    m.setTranslate(vec);                                                      
    matrix.multLeft(m);                                                       
    m.setTranslate(vec.operator_minus());                                                     
    inverse.multRight(m);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: extender

public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbMatrix    ctm = action.getMatrix();
    final SbMatrix    inv = action.getInverse();
    final SbMatrix    m = new SbMatrix();
    final SbVec3f     v = new SbVec3f(), centerVec = new SbVec3f();
    boolean        doCenter, doScaleOrient;

    doCenter      = (! center.isIgnored() && ! center.isDefault());
    doScaleOrient = (! scaleOrientation.isIgnored() &&
                     ! scaleOrientation.isDefault());

    if (! translation.isIgnored() && ! translation.isDefault()) {
        v.copyFrom(translation.getValue());
        TRANSLATE(v, ctm, inv, m);
    }

    if (doCenter) {
        centerVec.copyFrom(center.getValue());
        TRANSLATE(centerVec, ctm, inv, m);
    }

    if (! rotation.isIgnored() && ! rotation.isDefault()) {
        final SbRotation      r = new SbRotation(rotation.getValue());
        ROTATE(r, ctm, inv, m);
    }

    if (! scaleFactor.isIgnored() && ! scaleFactor.isDefault()) {

        if (doScaleOrient) {
            final SbRotation  r = new SbRotation(scaleOrientation.getValue());
            ROTATE(r, ctm, inv, m);
        }

        final SbVec3f s = new SbVec3f(scaleFactor.getValue());
        SCALE(s, ctm, inv, m);

        if (doScaleOrient) {
            final SbRotation  r = new SbRotation(scaleOrientation.getValue());
            r.invert();
            ROTATE(r, ctm, inv, m);
        }

    }

    if (doCenter) {
        centerVec.negate();
        TRANSLATE(centerVec, ctm, inv, m);
    }
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
    SoTransform_doAction(action);
}



}
