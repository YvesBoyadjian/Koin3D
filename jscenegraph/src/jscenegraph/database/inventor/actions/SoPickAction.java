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
 |      Defines the abstract SoPickAction class
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for picking objects in a scene.
/*!
\class SoPickAction
\ingroup Actions
This is an abstract base class for all picking actions. Currently,
the only supported subclass is the SoRayPickAction.

\par See Also
\par
SoRayPickAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPickAction extends SoAction {

	public SoType getTypeId() {
		return classTypeId;
	}
    public static SoType getClassTypeId()                              
                                    { return classTypeId; }                   
    public static void addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
    // java port
    public  static void                 enableElement(Class<?> klass)         
    { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
    
    public static void enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
    protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
    private static SoType               classTypeId	;

	   protected final SbViewportRegion    vpRegion = new SbViewportRegion();       //!< Current viewport region
	   
  private
    boolean              culling;        //!< Pick culling enabled?
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor.
//
// Use: protected

public SoPickAction(final SbViewportRegion viewportRegion)
//
////////////////////////////////////////////////////////////////////////
{
    //SO_ACTION_CONSTRUCTOR(SoPickAction.class);
    traversalMethods = methods;
    		
    vpRegion.copyFrom(viewportRegion);

    enableCulling(true);
}

  
  
  
  
  public
    //! Setting this flag to FALSE disables any pick culling that might
    //! take place (as in SoSeparators). This can be used for nodes
    //! (such as SoArray and SoMultipleCopy) that traverse their
    //! children multiple times in different locations, thereby
    //! avoiding computing bounding boxes each time. (This problem is
    //! very severe when each bounding box traversal also traverses the
    //! children N times.) The default setting is TRUE.
    void                enableCulling(boolean flag)      { culling = flag; }
    public boolean              isCullingEnabled()         { return culling; }

	   
	   
	/**
	 * Sets current viewport region to use for action. 
	 * Even though the picking operation may not involve a window per se, 
	 * some nodes need this information to determine their size and placement. 
	 * 
	 * @param newRegion
	 */
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Sets the current viewport region.
	    //
	    // Use: public
	    
	  public  void
	    setViewportRegion(final SbViewportRegion newRegion)
	    //
	    ////////////////////////////////////////////////////////////////////////
	   {
	       vpRegion.copyFrom(newRegion);
	   }
	  
    //! Returns current viewport region to use for action.
    public SbViewportRegion getViewportRegion()    { return vpRegion; }

	  
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoPickAction class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoPickAction, SoAction);
	       enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
	       methods = new SoActionMethodList(SoAction.methods);                   
	       classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
	                                           new SbName("SoPickAction"), null);	       
	   
	       SO_ENABLE(SoPickAction.class, SoViewportRegionElement.class);
	       //SO_ENABLE(SoPickAction.class, SoDecimationTypeElement.class);
	       //SO_ENABLE(SoPickAction.class, SoDecimationPercentageElement.class);
	       SO_ENABLE(SoPickAction.class, SoLazyElement.class);
	       SO_ENABLE(SoPickAction.class, SoCacheElement.class);

	   }
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

public void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewportRegionElement.set(state, vpRegion);

    traverse(node);
}
	  
  }
