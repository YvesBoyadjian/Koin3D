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
 |      Defines the SoShapeKit class. 
 |      Subclassed off of SoSeparatorKit, this adds all the nodes potentially
 |      used to describe a shape. Not all of these nodes are needed for any
 |      one type of shape. For example, if you set the 'shape' part to be
 |      an SoSphere node, it will not make much sense to create the part
 |      called 'profileCoordinate3' since it will be ignored in drawing the
 |      sphere.
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoCoordinate4;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNormal;
import jscenegraph.database.inventor.nodes.SoNormalBinding;
import jscenegraph.database.inventor.nodes.SoProfile;
import jscenegraph.database.inventor.nodes.SoProfileCoordinate2;
import jscenegraph.database.inventor.nodes.SoProfileCoordinate3;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.database.inventor.nodes.SoShapeHints;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTextureCoordinate2;
import jscenegraph.database.inventor.nodes.SoTextureCoordinateBinding;
import jscenegraph.database.inventor.nodes.SoTextureCoordinateDefault;
import jscenegraph.database.inventor.nodes.SoTextureCoordinateFunction;
import jscenegraph.database.inventor.nodes.SoTransform;

/**
 * @author Yves Boyadjian
 *
 */

/////////////////////////////////////////////////////////////////////
///  \class SoShapeKit
///  \ingroup Nodekits
///
/// NOTE TO DEVELOPERS:
///     For info about the structure of SoShapeKit:
///     [1] compile: /usr/share/src/Inventor/samples/ivNodeKitStructure
///     [2] type:    ivNodeKitStructure SoShapeKit.
///     [3] The program prints a diagram of the scene graph and a table with 
///         information about each part.
///
///    New nodes in this subclass are:
///         materialBinding, normalBinding,                 
///         and textureCoordinateBinding
///
///      A parent node that manages a collection of child nodes 
///      into a unit with the following structure:
///
///                            this
///              ----------------|
///              |        "topSeparator"
///         "callbackList"       |
///      ---------------------------------------------------------------
///      |       |           |     |         |    |   |               |
///  "pickStyle" |    "transform"  |         |    |   |               | 
///     "appearance"  "texture2Transform"    |    |   |               |
///                                          |    |  "localTransform" |
///                                          |    |                   |
///                                          |    |            "shapeSeparator"
///                                          |    |                   |
///                                          |    |                "shape"
///                                          |  "childList"
///          ---------------------------------                   
///          |        
/// "materialBinding" "normalBinding"  
///  "textureCoordinateBinding" "shapeHints"
///  "coordinate3" "coordinate4" "normal"
///  "textureCoordinate2" "textureCoordinateFunction"
/// "profileCoordinate2" "profileCoordinate3"
/// "profileList" 
///

/////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//! Shape nodekit class.
/*!
\class SoShapeKit
\ingroup Nodekits
A nodekit that is used for creating a shape (i.e. geometry).
SoShapeKit is derived from SoSeparatorKit and SoBaseKit,
and thus inherits all the parts defined by these classes.


Furthermore, SoShapeKit adds numerous parts that can be used
to define a variety of <em>shape</em> objects, a <em>localTransform</em> part,
and of course a <em>shape</em> part.


All of the property nodes requires to define any of the Inventor shapes
are included as parts in this class.
Not all of these parts (nodes) are needed for any
one type of shape. For example, if you set the <em>shape</em> part to be
an SoSphere node, then it is not necessary to create a
<em>profileCoordinate3</em> part since it will be ignored in drawing the
sphere.  (And the unneeded parts will not be created, so there is 
no performance penalty for using this class of node.


This class contains two private parts. They are both SoSeparator
nodes.  One of them sits just below the nodekit itself, and serves to
contain all properties within this nodekit from affecting nodes that
come after this nodekit. The second separator sits above the <em>shape</em>
part, and serves to cache the shape even when the <em>transform</em>,
<em>localTransform</em>, or <em>appearance</em> parts are changing.

\par Parts
\par
\b shapeSeparator
<BR> This is a private part. The parent node of the actual shape part. 	  It is a SoSeparator and is <tt>NULL</tt> by default, but 	  is created automatically if necessary. 
\par
\b materialBinding
<BR> An SoMaterialBinding node that can be used to set the 	material binding for the shape. This part is <tt>NULL</tt> by default, 	but is created automatically if necessary. 
\par
\b normalBinding
<BR> An SoNormalBinding node that can be used to set the 	normal binding for the shape. This part is <tt>NULL</tt> by default, 	but is created automatically if necessary. 
\par
\b textureCoordinateBinding
<BR> An SoTextureCoordinateBinding node that can be used to set the 	texture coordinate binding for the shape. This part is <tt>NULL</tt> 	by default, but is created automatically if necessary. 
\par
\b shapeHints
<BR> An SoShapeHints node that can be used to set the 	shape hints for the shape. This part is <tt>NULL</tt> 	by default, but is created automatically if necessary. 
\par
\b coordinate3
<BR> An SoCoordinate3 node that can be used to set the 	3D coordinates for a vertex-based shape. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b coordinate4
<BR> An SoCoordinate4 node that can be used to set the 	4D coordinates for a NURBS shapes. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b normal
<BR> An SoNormal node that can be used to set the 	normal vectors for a vertex-based shape. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b textureCoordinate2
<BR> An SoTextureCoordinate2 node that can be used to set the 	texture coordinates for a vertex-based shape. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b textureCoordinateFunction
<BR> An SoTextureCoordinateFunction node that can be used to set the 	a procedural texture coordinates function 	for a vertex-based shape. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b profileCoordinate2
<BR> An SoProfileCoordinate2 node that can be used to set the 	2D profile coordinates for a shape that uses them, 	(e.g., SoText3). 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b profileCoordinate3
<BR> An SoProfileCoordinate3 node that can be used to set the 	3D profile coordinates for a shape that uses them, 	(e.g., SoSoNURBSCurve). 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b profileList
<BR> An SoProfileList node that can be used to set the 	profile curve for a shape that uses them, (e.g., SoNurbsCurve). 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b localTransform
<BR> An SoTransform node that can be used to set a 	local tranformation on the shape. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b shape
<BR> This is the part which specifies the actual shape node. 	  This can be any node derived from SoShape 	  By default, an SoCube is created. 	  It is important to set all of the appropriate parts 	  within this nodekit to suit the type of SoShape 	  that is used.  For example, if the <em>shape</em> part is 	  set to an SoFaceSet, then the <em>coordinate3</em> shape 	  and probably the <em>normal</em> shape would be set as well. 	  See the reference page of the shape used for details on which 	  other nodes are necessary. 	

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoCameraKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoWrapperKit, SoCone, SoCube, SoCylinder, SoIndexedNurbsCurve, SoIndexedNurbsSurface, SoNurbsCurve, SoNurbsSurface, SoShapeHints, SoSphere, SoText2, SoText3, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

public class SoShapeKit extends SoSeparatorKit {
	
	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoShapeKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoShapeKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoShapeKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! defines fields for the new parts in the catalog
    //! Binding nodes
    protected final SoSFNode materialBinding = new SoSFNode();
    protected final SoSFNode normalBinding = new SoSFNode();
    protected final SoSFNode textureCoordinateBinding = new SoSFNode();

    //! Shapehints
    protected final SoSFNode shapeHints = new SoSFNode();

    //! Coordinate and normal nodes
    protected final SoSFNode coordinate3 = new SoSFNode();
    protected final SoSFNode coordinate4 = new SoSFNode();
    protected final SoSFNode normal = new SoSFNode();
    protected final SoSFNode textureCoordinate2 = new SoSFNode();
    protected final SoSFNode textureCoordinateFunction = new SoSFNode();

    //! Profile Stuff (for text and for nurbs)
    protected final SoSFNode profileCoordinate2 = new SoSFNode();
    protected final SoSFNode profileCoordinate3 = new SoSFNode();
    protected final SoSFNode profileList = new SoSFNode();

    //! Shape
    protected final SoSFNode localTransform = new SoSFNode();
    protected final SoSFNode shapeSeparator = new SoSFNode();
    protected final SoSFNode shape = new SoSFNode();


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoShapeKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoShapeKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.

    // Binding nodes
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(materialBinding,"materialBinding", SoMaterialBinding.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(normalBinding,"normalBinding", SoNormalBinding.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(textureCoordinateBinding,"textureCoordinateBinding", 
            SoTextureCoordinateBinding.class, true, "topSeparator", "childList", true );

    // Shapehints
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(shapeHints,"shapeHints", SoShapeHints.class, true,
                                    "topSeparator", "childList", true );

    // Coordinate and normal nodes
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(coordinate3,"coordinate3", SoCoordinate3.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(coordinate4,"coordinate4", SoCoordinate4.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(normal,"normal", SoNormal.class, true, 
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(textureCoordinate2,"textureCoordinate2", SoTextureCoordinate2.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY( textureCoordinateFunction,"textureCoordinateFunction", 
                    SoTextureCoordinateFunction.class, SoTextureCoordinateDefault.class,
                    true, "topSeparator","",true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(profileCoordinate2,"profileCoordinate2", SoProfileCoordinate2.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(profileCoordinate3,"profileCoordinate3", SoProfileCoordinate3.class, true,
                                    "topSeparator", "childList", true );
    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(profileList,"profileList", SoGroup.class, true,
                                    "topSeparator", "childList", SoProfile.class, true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(localTransform,"localTransform", SoTransform.class, true,
                                    "topSeparator","", true );
    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(shapeSeparator,"shapeSeparator", SoSeparator.class, true,
                                    "topSeparator","", true );
    kitHeader.SO_KIT_ADD_CATALOG_ABSTRACT_ENTRY(shape,"shape", SoShape.class,
                            SoCube.class, false, "shapeSeparator","", true  );

    SO_KIT_INIT_INSTANCE();

    setUpConnections( true, true );
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}
/////////////////////////////////////////////////////////////////////////
//
// Called by the SoBaseKit::write() method. Calls setDefault(true)
// on the shapeSeparator. Note that this may be overriden later by basekit
// if, for example, topSeparator lies on a path that is being written out.
//
/////////////////////////////////////////////////////////////////////////
public void
setDefaultOnNonWritingFields()
{
    shapeSeparator.setDefault(true);

    // Call the base class...
    super.setDefaultOnNonWritingFields();
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoShapeKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoShapeKit.class, "ShapeKit", SoSeparatorKit.class );
}

}
