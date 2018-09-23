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
 |      This file defines the SoTextureCoordinate2 node class.
 |      This node is for normal (non-homogenouse), 2D texture coordinates
 |   Author(s)          : John Rohlf, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! 2D texture coordinate node.
/*!
\class SoTextureCoordinate2
\ingroup Nodes
This node defines a set of 2D coordinates to be used to map textures
to subsequent vertex-based shapes (those derived from
SoVertexShape). It replaces the current texture coordinates in the
rendering state for the shapes to use.


The current texture coordinate binding (see
SoTextureCoordinateBinding) determines how texture coordinates are
mapped to vertices of shapes. An alternative to using explicit texture
coordinates is to generate them using a function; see
SoTextureCoordinateFunction.


Texture coordinates range from 0 to 1 across the texture. The
horizontal coordinate, called <tt>S</tt>, is specified first, followed by
the vertical coordinate, <tt>T</tt>.

\par File Format/Default
\par
\code
TextureCoordinate2 {
  point [  ]
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current texture coordinates in the state. 

\par See Also
\par
SoTexture2, SoTextureCoordinateBinding, SoTextureCoordinateFunction, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTextureCoordinate2 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinate2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinate2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinate2.class); }    
	

	    public
    //! \name Fields
    //@{

    //! Texture coordinate points.
    final SoMFVec2f           point = new SoMFVec2f();          

	    protected final SoVBO[] _vbo = new SoVBO[1];
	  
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTextureCoordinate2()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinate2.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(point,"point", (new SbVec2f(0,0)));
    point.deleteValues(0);
    isBuiltIn = true;

    _vbo[0] = null;
}


////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	if(_vbo[0] != null)
		_vbo[0].destructor();
}

	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles actions.
//
// Use: extender

public void SoTextureCoordinate2_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! point.isIgnored() && point.getNum() > 0) {
        SoMultiTextureCoordinateElement.set2(state, this,
                                         point.getNum(), point.getValuesSbVec2fArray(/*0*/));
        if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
          SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.TEXCOORD_VBO, _vbo,
            point.getNum()*SbVec2f.sizeof(), VoidPtr.create(point.getValuesArray(0)), getNodeId());
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();
    
    if (! point.isIgnored() && point.getNum() > 0){
        SoGLMultiTextureCoordinateElement.setTexGen(state, this, 0, null);
        SoMultiTextureCoordinateElement.set2(state, this,
                                         point.getNum(), point.getValuesSbVec2fArray(/*0*/));
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: protected

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinate2_doAction(action);
}

	    
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinate2 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinate2.class, "TextureCoordinate2", SoNode.class);

    SO_ENABLE(SoCallbackAction.class, SoMultiTextureCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
    SO_ENABLE(SoPickAction.class,     SoMultiTextureCoordinateElement.class);
}

}
