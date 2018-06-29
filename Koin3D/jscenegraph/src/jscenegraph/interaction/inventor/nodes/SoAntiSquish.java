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
 |      This file defines the SoAntiSquish node class.
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.interaction.inventor.nodes;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTransformation;



////////////////////////////////////////////////////////////////////////////////
//! Transformation node that undoes non-uniform 3D scales.
/*!
\class SoAntiSquish
\ingroup Nodes
This node removes nonuniform 3D scaling from the current transformation matrix
when traversed by an action.   It is used by draggers such as the 
SoTrackballDragger that need to stay uniformly scaled no matter where
they are located in the scene graph.


The magnitude of the new scale is determined
by the current transformation matrix and the \p sizing field.
This node does not change the translation or rotation in the matrix.

\par File Format/Default
\par
\code
AntiSquish {
  sizing AVERAGE_DIMENSION
  recalcAlways TRUE
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoRayPickAction
<BR> Appends the current transformation with a new matrix to create an unsquished result. 

\par See Also
\par
SoCenterballDragger, SoJackDragger, SoTrackballDragger, SoTransformerDragger, SoTransformation, SoTransformBoxDragger
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoAntiSquish extends SoTransformation {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoAntiSquish.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoAntiSquish.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoAntiSquish.class); }    	  	
	
	  public enum Sizing {
		    X(0),
		    Y(1),
		    Z(2),
		    AVERAGE_DIMENSION(3),
		    BIGGEST_DIMENSION(4),
		    SMALLEST_DIMENSION(5),
		    LONGEST_DIAGONAL(6);
		    
		    final private int value;
		    
		    Sizing(int value) {
		    	this.value = value;
		    }
		    
		    public static Sizing fromValue(int value) {
		    	switch(value) {
		    	case 0: return X;
		    	case 1: return Y; 
		    	case 2: return Z;
		    	case 3: return AVERAGE_DIMENSION;
		    	case 4: return BIGGEST_DIMENSION;
		    	case 5: return SMALLEST_DIMENSION;
		    	case 6: return LONGEST_DIAGONAL;
		    		default:
		    			return null;
		    	}
		    }
		    
		    public int getValue() {
		    	return value;
		    }
		    };
		   
	public final SoSFEnum sizing = new SoSFEnum();
	
    //! If recalcAlways is <tt>TRUE</tt>, this node calculates its unsquishing matrix every 
    //! time it is traversed.  If <tt>FALSE</tt>, then this only occurs during the first 
    //! traversal folllowing a call to recalc().
    public final SoSFBool                recalcAlways = new SoSFBool();

  private
    final SbMatrix savedAnswer = new SbMatrix(), savedInverseAnswer = new SbMatrix();

    private boolean recalcNextTime;
    
	////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoAntiSquish class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoAntiSquish.class, "AntiSquish", SoTransformation.class);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoAntiSquish()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoAntiSquish*/);
    isBuiltIn = true;

    nodeHeader.SO_NODE_ADD_SFIELD(sizing,"sizing",     (Sizing.AVERAGE_DIMENSION.getValue()) );
    nodeHeader.SO_NODE_ADD_SFIELD(recalcAlways,"recalcAlways",         (true) );

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.X);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.Y);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.Z);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.AVERAGE_DIMENSION);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.BIGGEST_DIMENSION);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.SMALLEST_DIMENSION);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Sizing.LONGEST_DIAGONAL);

    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(sizing,"sizing", "Sizing");

    savedAnswer.copyFrom( SbMatrix.identity());
    savedInverseAnswer.copyFrom( SbMatrix.identity());

    recalcNextTime = false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: private

	public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Calculates an unsquished matrix based on the 'sizing' field and the
//    given matrix.
//
// Use: private
private SbMatrix
getUnsquishingMatrix( final SbMatrix squishedMatrix,
                                    boolean  doInverse ,
                                    final SbMatrix inverseAnswer )
//
////////////////////////////////////////////////////////////////////////
{
    // Think of the squishedMatrix as the following chain of matrices, given the
    // results of SbMatrix::factor()
    // [T]  = translation 
    // [R]  = rotation 
    // [S]  = scale
    // [SO] = scaleOrientation
    // squishedMatrix = [SO-Inv][S][SO][R][T]

    final SbVec3f  scaleV = new SbVec3f(), translV = new SbVec3f();
    final SbMatrix scaleOrientM = new SbMatrix(), rotM = new SbMatrix(), projM = new SbMatrix(); 
    if (!squishedMatrix.factor(scaleOrientM,scaleV,rotM,translV,projM)) {
        // If the matrix was singular, then we can not unsquish it.
        // Return identity.
        final SbMatrix answer = SbMatrix.identity();
        // Bug 323082: no longer a check for the doInverse flag. We
        // always set this
        inverseAnswer.copyFrom( SbMatrix.identity());
        savedAnswer.copyFrom( answer);
        savedInverseAnswer.copyFrom( inverseAnswer);
        return answer;
    }

final float TINY = .00001f;
    for (int i = 0; i < 2; i++ ) {
        if (scaleV.getValue()[i] < .00001f)
            scaleV.getValue()[i] = TINY;
    }
//#undef TINY

    // We want to append our squishedMatrix with a new matrix:
    // desiredM = [NEWSCALE][R][T]
    // where NEWSCALE is a uniform scale based on [S] and the sizing field
    float scl = Float.NaN;

    Sizing whichSizing = Sizing.fromValue( sizing.getValue());
    if ( whichSizing == Sizing.X )
        scl = scaleV.getValue()[0];
    else if ( whichSizing == Sizing.Y )
        scl = scaleV.getValue()[1];
    else if ( whichSizing == Sizing.Z )
        scl = scaleV.getValue()[2];
    else if ( whichSizing == Sizing.AVERAGE_DIMENSION )
        scl = (scaleV.getValue()[0] + scaleV.getValue()[1] + scaleV.getValue()[2]) / 3.0f ;
    else if ( whichSizing == Sizing.BIGGEST_DIMENSION ) {
        scl =   (scaleV.getValue()[0] >= scaleV.getValue()[1] && scaleV.getValue()[0] >= scaleV.getValue()[2] ) ? scaleV.getValue()[0]
              : (scaleV.getValue()[1] >= scaleV.getValue()[2] )                           ? scaleV.getValue()[1] 
              :  scaleV.getValue()[2];
    }
    else if ( whichSizing == Sizing.SMALLEST_DIMENSION ) {
        scl =   (scaleV.getValue()[0] <= scaleV.getValue()[1] && scaleV.getValue()[0] <= scaleV.getValue()[2] ) ? scaleV.getValue()[0]
              : (scaleV.getValue()[1] <= scaleV.getValue()[2] )                           ? scaleV.getValue()[1] 
              :  scaleV.getValue()[2];
    }
    else if ( whichSizing == Sizing.LONGEST_DIAGONAL ) {
        // Determine the aggregate scaleOrientation-scale matrix
        final SbMatrix aggregate = new SbMatrix();
        aggregate.setScale(scaleV);
        aggregate.multLeft( scaleOrientM );
        // Multiply each of 4 Diagonals (symmetry says we don't need all 8)
        // by this matrix.
        final SbVec3f v1 = new SbVec3f(1,1,1), v2 = new SbVec3f(-1,1,1), v3 = new SbVec3f(-1,-1,1), v4 = new SbVec3f(1,-1,1);
        aggregate.multVecMatrix(v1,v1);
        aggregate.multVecMatrix(v2,v2);
        aggregate.multVecMatrix(v3,v3);
        aggregate.multVecMatrix(v4,v4);
        float[] ls = new float[4];
        ls[0] = v1.length();
        ls[1] = v2.length();
        ls[2] = v3.length();
        ls[3] = v4.length();
        // Use the length of the biggest.
        scl = ls[0];
        for (int i = 0; i < 4; i++ )
            if ( ls[i] > scl )
                scl = ls[i];
    }

    float invScl;
    invScl = 1.0f / scl;

    final SbVec3f newScale = new SbVec3f( scl, scl, scl), newScaleInv = new SbVec3f( invScl, invScl, invScl );

    final SbMatrix desiredM = new SbMatrix(), tempM = new SbMatrix();

    desiredM.setTranslate(  translV );
    desiredM.multLeft(rotM);
    tempM.setScale( newScale );
    desiredM.multLeft(tempM);

    // Now find our answer, which does the following:
    // [answerM][squishMatrix] = [desiredM]
    // [answerM] = [desiredM][squishMatrixInverse]
    SbMatrix answerM = new SbMatrix(desiredM);
    answerM.multRight( squishedMatrix.inverse() );

    inverseAnswer.copyFrom(answerM.inverse());

    savedAnswer.copyFrom(answerM);
    savedInverseAnswer.copyFrom(inverseAnswer);

    return new SbMatrix(answerM);
}

public void
callback( SoCallbackAction action )
{
    SoAntiSquish_doAction( action );
}

public void
GLRender( SoGLRenderAction action )
{
    SoAntiSquish_doAction( action );
}

public void
getBoundingBox( SoGetBoundingBoxAction action )
{
    //??? HACK!!!
    //    The recalc'ing action of these nodes behaves poorly because
    //    often, the first traversal after recalc() is called is
    //    during the getboundingboxaction applied by an SoSurroundScale node.
    //    This action is applied so the surround scale can figure out what
    //    to surround, and has poor state information for the antisquish.
    //    [1] it is not applied to a full path so it lacks the transforms
    //        from above.
    //    [2] the surround scale does not participate, so it lacks the scale
    //        usually issued by the surround scale.
    //
    //    To get around this, we do not undo our recalcNextTime flag in this 
    //    case.  We can identify the case by testing if we are below the 
    //    resetpath of the action.  If we are mistaken, it still can't hurt 
    //    to wait:
    //    since we're under the reset path it won't make a difference what
    //    we do here.
    if ( recalcAlways.getValue() == false && recalcNextTime == true ) {

        // return if we're below the reset path. I.E., if current traversal
        // path contains the reset path.
        final SoPath cp = action.getCurPath();
        final SoPath rp = action.getResetPath();
        if ( ! action.isResetBefore() && cp != null && rp != null && cp.containsPath(rp) )
            return;
    }

    SoAntiSquish_doAction( action );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns transformation matrix.
//
// Use: protected

public void
getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbMatrix    ctm = action.getMatrix();
    final SbMatrix    inv = action.getInverse();

    if ( recalcAlways.getValue() == true || recalcNextTime == true ) {
        final SbMatrix answer = new SbMatrix(), invAnswer = new SbMatrix();
        answer.copyFrom( getUnsquishingMatrix( ctm, true, invAnswer ));

        ctm.multLeft( answer );
        inv.multRight( invAnswer );
    }
    else {
        ctm.multLeft( savedAnswer );
        inv.multRight( savedInverseAnswer );
    }
}

public void
pick( SoPickAction action )
{
    SoAntiSquish_doAction( action );
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles any action derived from SoAction.
//
// Use: private

private void
SoAntiSquish_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if ( recalcAlways.getValue() == true || recalcNextTime == true ) {

        final SbMatrix oldCtm = new SbMatrix(SoModelMatrixElement.get(action.getState()));

        final SbMatrix answer = new SbMatrix(), dummyM = new SbMatrix();
        answer.copyFrom(getUnsquishingMatrix( oldCtm, false, dummyM ));

        recalcNextTime = false;
        SoModelMatrixElement.mult(action.getState(), this, answer );
    }
    else
        SoModelMatrixElement.mult(action.getState(), this, savedAnswer );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This method sets a flag to recalculate our matrix next time an
//    action is applied.
//
// Use: protected
protected void
recalc()
//
////////////////////////////////////////////////////////////////////////
{
    recalcNextTime = true;
    touch();
}
	
}
