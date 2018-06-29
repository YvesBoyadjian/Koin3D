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
 |      Defines the SoGetMatrixAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Computes transformation matrix for subgraph.
/*!
\class SoGetMatrixAction
\ingroup Actions
This action computes transformation matrices for a given subgraph.  It
computes the cumulative transformation matrix and its inverse, along
with a cumulative texture transformation matrix and its inverse.


This action is unlike most others in that it does not traverse
downwards from groups. When applied to a node, it computes the matrix
for just that node. (This makes sense for transformation nodes, but
not for others, really.) It is much more useful when applied to a
path. When applied to a path, it gathers the transformation info for
all nodes in the path and those that affect nodes in the path, but it
stops when it hits the last node in the path; it does not traverse
downwards from it as other actions (such as rendering) do. This
behavior makes the most sense for this action.

\par See Also
\par
SoGetBoundingBoxAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGetMatrixAction extends SoAction {
	
	public                                                                     
     SoType              getTypeId() {
		return classTypeId;
	}
    public static SoType               getClassTypeId()                              
                                    { return classTypeId; }                   
  public                                                          
    static void                 addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
  // java port
  public  static void                 enableElement(Class<?> klass)         
  { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
  
    public static void                 enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
  protected                                                                  
    SoEnabledElementsList getEnabledElements() {
	  return enabledElements; 
  }
  protected  static SoEnabledElementsList enabledElements;                            
  protected  static SoActionMethodList   methods;                                     
  private                                                                    
    static SoType               classTypeId;

  
    private
   final SbMatrix            ctm = new SbMatrix();            //!< Cumulative transformation matrix
    private  final  SbMatrix            inv = new SbMatrix();            //!< Inverse transformation matrix
    private  final  SbMatrix            texCtm = new SbMatrix();         //!< Cumulative texture transf matrix
    private  final  SbMatrix            texInv = new SbMatrix();         //!< Inverse texture transf matrix
    private final SbViewportRegion    vpRegion = new SbViewportRegion();       //!< Current viewport region
  
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoGetMatrixAction( SbViewportRegion region)
//
////////////////////////////////////////////////////////////////////////
{
    //SO_ACTION_CONSTRUCTOR(SoGetMatrixAction);
    traversalMethods = methods;
    
    vpRegion.copyFrom(region);
}


	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoGetMatrixAction class.
	   //
	   // Use: internal
	   
	  public static  void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoGetMatrixAction, SoAction);
		  enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
		    methods = new SoActionMethodList(SoAction.methods);                   
		    classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
		                                        new SbName("SoGetMatrixAction"), null);
	   
	       //SO_ENABLE(SoGetMatrixAction, SoViewportRegionElement);
		    SoGetMatrixAction.enableElement(SoViewportRegionElement.getClassTypeId(SoViewportRegionElement.class),      
		    		SoViewportRegionElement.getClassStackIndex(SoViewportRegionElement.class));

	   }
	  
	      //! Sets/returns current viewport region to use for action.
    public void                setViewportRegion(final SbViewportRegion newRegion)
{
    vpRegion.copyFrom(newRegion);
}

    //! Sets/returns current viewport region to use for action.
    public SbViewportRegion getViewportRegion()    { return vpRegion; }

    //! Returns cumulative transformation matrix.  Warning:
    //! the matrix returned by this routine should not be changed (unless
    //! you are implementing your own transformation nodes).
    public SbMatrix           getMatrix()             { return ctm; }
    //! Returns cumulative inverse transformation matrix.  Warning:
    //! the matrix returned by this routine should not be changed (unless
    //! you are implementing your own transformation nodes).
    public SbMatrix           getInverse()            { return inv; }

    //! Returns cumulative transformation matrix.  Warning:
    //! the matrix returned by this routine should not be changed (unless
    //! you are implementing your own transformation nodes).
    public SbMatrix           getTextureMatrix()      { return texCtm; }
    //! Returns cumulative inverse transformation matrix.  Warning:
    //! the matrix returned by this routine should not be changed (unless
    //! you are implementing your own transformation nodes).
    public SbMatrix           getTextureInverse()     { return texInv; }

	  ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

protected void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewportRegionElement.set(state, vpRegion);

    ctm.makeIdentity();
    inv.makeIdentity();

    texCtm.makeIdentity();
    texInv.makeIdentity();

    traverse(node);
}
	  
	   }
