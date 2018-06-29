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
 |      This file defines the SoUnits node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoUnitsElement;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Node that scales to convert units of length.
/*!
\class SoUnits
\ingroup Nodes
This node defines a uniform 3D scale about the origin relative to the
previously defined units. The default units for all data are meters.
Adding a units node with the value <tt>INCHES</tt> will have the same
effect as adding an SoScale node with the \b scaleFactor  of
(.0254, .0254, .0254). Any subsequent SoUnits node will take the
previous units into account. When building a composite object out of
a bunch of pieces, it would be a good practice to add an SoUnits
node at the beginning of each of the pieces, under an SoSeparator
node, to make sure all the pieces fit together with the same scale.

\par File Format/Default
\par
\code
Units {
  units METERS
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Accumulates the scale that is the ratio of the size from the previous unit to the current unit into the current transformation. 
\par
SoGetMatrixAction
<BR> Returns the matrix corresponding to the units scaling. 

\par See Also
\par
SoScale, SoTransform
*/
////////////////////////////////////////////////////////////////////////////////

public class SoUnits extends SoTransformation {
	//TODO


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoUnits class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoUnits.class, "Units", SoTransformation.class);

    SO_ENABLE(SoCallbackAction.class,         SoUnitsElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoUnitsElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class,   SoUnitsElement.class);
    SO_ENABLE(SoGetMatrixAction.class,        SoUnitsElement.class);
    SO_ENABLE(SoPickAction.class,             SoUnitsElement.class);
}

}
