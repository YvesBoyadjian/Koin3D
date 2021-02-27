
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

package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderParameter extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoShaderParameter.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoShaderParameter.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoShaderParameter.class); }              
	
public
  final SoSFString name = new SoSFString();
  public final SoSFInt32 identifier = new SoSFInt32();


/*!
  Constructor.
*/
public SoShaderParameter()
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoShaderParameter.class*/);

  nodeHeader.SO_NODE_ADD_FIELD(name,"name", (""));
  nodeHeader.SO_NODE_ADD_FIELD(identifier,"identifier", (0));
}

/*!
  Destructor.
*/
public void destructor()
{
	super.destructor();
}
  
  
//doc from parent
public static void
initClass()
{
// SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoShaderParameter,
//                                      SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
    SoSubNode.SO__NODE_INIT_CLASS(SoShaderParameter.class, "ShaderParameter", SoNode.class);
}

}
