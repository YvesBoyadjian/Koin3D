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
 |      This file defines the SoRotationXYZ node class.
 |
 |   Author(s)          : Paul S. Strauss
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
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node representing a 3D rotation about the x-, y-, or z-axis.
/*!
\class SoRotationXYZ
\ingroup Nodes
This node defines a 3D rotation about one of the three principal axes.
The rotation is accumulated into the current transformation, which is
applied to subsequent shapes.

\par File Format/Default
\par
\code
RotationXYZ {
  axis X
  angle 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Accumulates rotation transformation into the current transformation. 
\par
SoGetMatrixAction
<BR> Returns the matrix corresponding to the rotation. 

\par See Also
\par
SoRotation, SoTransform
*/
////////////////////////////////////////////////////////////////////////////////

public class SoRotationXYZ extends SoTransformation {

	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoRotationXYZ.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoRotationXYZ.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoRotationXYZ.class); }   
	  
	    //! Rotation axis
	    public enum Axis {
	        X,                                      //!< x-axis
	        Y,                                      //!< y-axis
	        Z;                                       //!< z-axis
	    	
	    	public int getValue() {
	    		return ordinal();
	    	}
	    };

	    //! \name Fields
	    //@{

	    //! Rotation axis.
	    public final SoSFEnum            axis = new SoSFEnum();           

	    //! Rotation angle (in radians), using the right-hand rule.
	    public final SoSFFloat           angle = new SoSFFloat();          

	    //! Creates a rotation node with default settings.
	    public SoRotationXYZ() {
	    	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoRotationXYZ.class*/);
	    	nodeHeader.SO_NODE_ADD_FIELD(axis,"axis",  (Axis.X.getValue()));
	    	nodeHeader.SO_NODE_ADD_FIELD(angle,"angle", (0.0f));

	    	    // Set up static info for enumerated type field
	    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Axis.X);
	    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Axis.Y);
	    	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Axis.Z);

	    	    // Set up info in enumerated type field
	    	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(axis,"axis", "Axis");

	    	    isBuiltIn = true;
	    }

	    //! Returns an SbRotation equivalent to the specified rotation.
	    public SbRotation          getRotation() {
	        if (angle.isIgnored() || angle.isDefault())
	            return SbRotation.identity();

	        else {
	            final SbVec3f         ax = new SbVec3f(0.0f, 0.0f, 0.0f);
	            final SbRotation      rot = new SbRotation();

	            switch (Axis.values()[axis.getValue()]) {

	              case X:
	                ax.getValue()[0] = 1.0f;
	                break;

	              case Y:
	                ax.getValue()[1] = 1.0f;
	                break;

	              case Z:
	                ax.getValue()[2] = 1.0f;
	                break;
	            }

	            rot.setValue(ax, angle.getValue());

	            return rot;
	        }	    	
	    }

	    public void        SoRotationXYZ_doAction(SoAction action) {
	        if (! angle.isIgnored() && ! angle.isDefault())
	            SoModelMatrixElement.rotateBy(action.getState(), this,
	                                           getRotation());
	    }
	    
	    public void        callback(SoCallbackAction action) {
	        SoRotationXYZ_doAction(action);	    	
	    }
	    
	    public void        GLRender(SoGLRenderAction action) {
	        SoRotationXYZ_doAction(action);
	    }
	    
	    public void        getBoundingBox(SoGetBoundingBoxAction action) {
	        SoRotationXYZ_doAction(action);
	    }
	    
	    public void        getMatrix(SoGetMatrixAction action) {
	        final SbMatrix    ctm = action.getMatrix();
	        final SbMatrix    inv = action.getInverse();
	        final SbMatrix    m = new SbMatrix();
	        final SbRotation  rot = new SbRotation(getRotation());

	        rot.getValue(m);
	        ctm.multLeft(m);
	        rot.invert();
	        rot.getValue(m);
	        inv.multRight(m);
	    }
	    
	    public void        pick(SoPickAction action) {
	        SoRotationXYZ_doAction(action);
	    }

	  
	
	////////////////////////////////////////////////////////////////////////
	//
	//Description:
	//This initializes the SoRotationXYZ class.
	//
	//Use: internal
	
	public static void initClass()
	//
	////////////////////////////////////////////////////////////////////////
	{
		SO__NODE_INIT_CLASS(SoRotationXYZ.class, "RotationXYZ", SoTransformation.class);
	}

}
