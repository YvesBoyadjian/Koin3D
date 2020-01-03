/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;

/*!
  \class SoGetPrimitiveCountAction SoGetPrimitiveCountAction.h Inventor/actions/SoGetPrimitiveCountAction.h
  \brief The SoGetPrimitiveCountAction class counts the primitives in a scene.
  \ingroup actions

  Apply this action to a scene if you need to know the number of
  primitives present in a scenegraph, or parts of a scenegraph.


  One common mistake to make when using this action is to think that
  it traverses just the parts currently in view, like SoGLRenderAction
  does. (SoGLRenderAction culls away the scenegraph parts outside the
  camera view volume and does not traverse those.) Like most other
  action classes, SoGetPrimitiveCountAction actually traverses the
  complete scenegraph, not just the parts currently in view.

  \since Coin 1.0
  \since TGS Inventor 2.5
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoGetPrimitiveCountAction extends SoAction {

	
	private	  int numtris;
	private   int numlines;
	private	  int numpoints;
	private	  int numtexts;
	private	  int numimages;

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.actions.SoAction#getTypeId()
	 */
	@Override
	public SoType getTypeId() {
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
  
   public  static void                 enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
  protected                                                                  
    SoEnabledElementsList  getEnabledElements()  {
	  return enabledElements;
  }

    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
	  private                                                                    
	    static SoType               classTypeId;

	// Override from parent class.
	  public static void
	  initClass()
	  {
	    //SO_ACTION_INTERNAL_INIT_CLASS(SoGetPrimitiveCountAction.class, SoAction.class);
		  enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
		    methods = new SoActionMethodList(SoAction.methods);                   
	    classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
                new SbName("SoGetPrimitiveCountAction"), null);

	    //TODO
	    //SO_ENABLE(SoGetPrimitiveCountAction.class, SoDecimationPercentageElement.class);
	    //SO_ENABLE(SoGetPrimitiveCountAction.class, SoDecimationTypeElement.class);
	  }

/*!
  Adds \a num texture image maps to total count. Used by node
  instances in the scene graph during traversal.
*/
public void
addNumImage(final int num)
{
  this.numimages += num;
}

/*!
  Adds \a num triangles to total count. Used by node instances in the
  scene graph during traversal.
*/
public void
addNumTriangles( int num)
{
  this.numtris += num;
}

	  
}
