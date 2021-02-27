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
 |      This file defines the SoUpgrader class, which is used to
 |      read in and convert old nodes into new nodes.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc.upgraders;

import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
///////////////////////////////////////////////////////////////////////////////
///
////\class SoUpgrader
///
//////////////////////////////////////////////////////////////////////////////

public abstract class SoUpgrader extends SoGroup {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoUpgrader.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoUpgrader.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoUpgrader.class); }    
	  
    private static SbDict       upgradeDictV1;
    private static SbDict       upgradeDictV2;
	
    protected
        //! This is set to TRUE in the SoUpgrader constructor. A subclass
        //! should set this to FALSE in its constructor if its
        //! createNewNode() method returns a group node but the node being
        //! upgraded is not really a group; it keeps children from being
        //! read.
        boolean                isGroup;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoUpgrader()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoUpgrader.class*/);

    isGroup = true;
}

    
    //! Subclasses MUST define this...
    abstract SoNode      createNewNode();


////////////////////////////////////////////////////////////////////////
//
//Description:
//Looks in the dictionary to figure out if a converter exists...
//
//Use: public, static

	  public
		    //! Find out if an upgrader exists for a specific class and a
		    //! specific version of the file format.
		    static SoUpgrader   getUpgrader(final SbName className,
		                                     float version) {
		    // There are 1.0 and 2.0 upgraders right now...
		    if (version != 1.0 && version != 2.0) return null;
		    
		    // Look in dictionary for converter type:
		    final Object[] t = new Object[1];
		    if (! getUpgradeDict(version).find(className.getString(), t))
		        return null;

		    SoType type = (SoType ) t[0];
		    
		    return (SoUpgrader )type.createInstance();
	  }

	    //! This is the key method that reads in fields, calls the
	    //! createNewNode() method (which is responsible for looking at the
	    //! fields read and setting the appropriate fields in the new
	    //! node), and then reads in and adds children to the new node if
	    //! it is derived from SoGroup.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Does the grunt work of reading in the old node-- relies on
//createNewNode() to actually create a node or scene graph that
//can be added to the scene.  Returns TRUE if everything was
//successful.
//
//Use: extender, virtual

	    public boolean        upgrade(SoInput in, final SbName refName,
	                                final SoBase[] result) {
    boolean returnValue = true;

    // Read in fields:
    final SoFieldData   fieldData = getFieldData();
    final boolean[] notBuiltIn = new boolean[1]; // Not used
    if (! fieldData.read(in, this, false, notBuiltIn))
        return false;

    // Create instance:
    result[0] = createNewNode();
    
    // Oops, something wrong...
    if (result[0] == null) return false;

    // Add to name dictionary:
    if (! (refName.operator_not()))
        in.addReference(refName, result[0]);
    
    // And read children, if node being upgraded is derived from group:
    if (isGroup && result[0].isOfType(SoGroup.getClassTypeId())) {
        returnValue = readChildren(in);
        if (returnValue) {

            SoGroup g = (SoGroup )result[0];

            // Turn off notification on result while we set it up:
            boolean notifySave = g.enableNotify(false);

            for (int i = 0; i < getNumChildren(); i++) {
                g.addChild(getChild(i));
            }

            // Re-enable notification.
            g.enableNotify(notifySave);
        }
        // No need to remove children, we're about to be unreffed
        // anyway (by SoBase::readBaseInstance).
    }
    
    return returnValue;
	    }


////////////////////////////////////////////////////////////////////////
//
//  Return either the V1.0 or V2.0 dictionary
//
static SbDict getUpgradeDict(float version)
//
////////////////////////////////////////////////////////////////////////
{
    SbDict upgradeDict = null;
    if (version == 1.0)
        upgradeDict = upgradeDictV1;
    else if (version == 2.0)
        upgradeDict = upgradeDictV2;

    return upgradeDict;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes ALL Inventor upgrader classes.
//
// Use: internal

public static void initClasses()
//
////////////////////////////////////////////////////////////////////////
{
    // Base class must be initialized first
    SoUpgrader.initClass();

//    SoV1CustomNode::initClass(); TODO
//    SoV1DrawStyle::initClass();
//    SoV1Environment::initClass();
//    SoV1IndexedTriangleMesh::initClass();
//    SoV1LayerGroup::initClass();
//    SoV1LightModel::initClass();
//    SoV1Material::initClass();
//    SoV1PackedColor::initClass();
//    SoV1PickStyle::initClass();
//    SoV1Separator::initClass();
//    SoV1ShapeHints::initClass();
//    SoV1Texture2::initClass();
//    SoV1Text3::initClass();
//    SoV1TextureCoordinateCube::initClass();
//    SoV1TextureCoordinateCylinder::initClass();
//    SoV1TextureCoordinateEnvironment::initClass();
//    SoV1TextureCoordinatePlane::initClass();
//    SoV1TextureCoordinateSphere::initClass();

//    SoV2Text2::initClass(); TODO
    SoV2Text3.initClass();
//    SoV2WWWAnchor::initClass(); //TODO
//    SoV2WWWInline::initClass();
//    SoV2AsciiText::initClass();
//    SoV2FontStyle::initClass();
//    SoV2MaterialIndex::initClass();
//    SoV2VertexProperty::initClass();
//    SoV2LOD::initClass();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoUpgrader class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_ABSTRACT_CLASS(SoUpgrader.class, "Upgrader", SoGroup.class);

    getClassTypeId().makeInternal();
    upgradeDictV1 = new SbDict(50);
    upgradeDictV2 = new SbDict(10); // there are only a few classes to upgrade
}

//! Macro to initialize upgrader classes.  Pass in the name of the
//! upgrader's class.
public static void SO_UPGRADER_INIT_CLASS(Class className)                    {                 
  SO__NODE_INIT_CLASS(className, className.getSimpleName(), SoUpgrader.class);
}

//! Macro to register a converter.  This should be put in the initClass
//! method just after SO_UPGRADER_INIT_CLASS.  This also marks the upgrader
//! class as internal (upgraders are transient, and should only exist
//! while reading an old Inventor file-- you shouldn't be able to just
//! create one using SoType::createInstance).  Pass in the name of the
//! class being upgraded (e.g. if this is MyV1Upgrader that upgrades
//! the MyClass node, you would SO_REGISTER_UPGRADER(MyClass, 1.0)).
public static void SO_REGISTER_UPGRADER(String oldClassName, float version,SoType classTypeId)         {                  
        do {                                                                  
            classTypeId.makeInternal();                                       
            registerUpgrader(classTypeId, new SbName(oldClassName), version);  
        } while (false);
}

//! Use this macro instead of calling "new" on the className. 
//! This ensures that overridden types are created during an upgrade.
public SoNode SO_UPGRADER_CREATE_NEW(Class<? extends SoNode> className) { 
        return className.cast(SoSubNode.getClassTypeId(className).createInstance());
}


  protected
    //! Register a converter for a specific class and a specific file
    //! format.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Register a converter with the converter dictionary.
//
//Use: protected, static

    static void         registerUpgrader(final SoType type,
                                         final SbName className,
                                         float version) {
    String str = className.getString();

//#ifdef DEBUG
    // There are 1.0 and 2.0 upgraders right now...
    if (version != 1.0 && version != 2.0) {
        SoDebugError.post("SoUpgrader::register", "Cannot handle "+
                           "converters from version "+version+", can only "+
                           "handle converters from version 1.0 or 2.0"
                           );
    }
//#endif

    SbDict upgradeDict = getUpgradeDict(version);
    assert(upgradeDict != null);
        
//#ifdef DEBUG
    final Object[] t = new Object[1];
    if (upgradeDict.find(str, t)) {
        SoDebugError.post("SoUpgrader::register", "Upgrader already "+
                           "registered for class "+ str);
    }
//#endif

    upgradeDict.enter(str, type);    
	  
  }
}
