/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoVRMLIndexedFaceSet SoVRMLIndexedFaceSet.h Inventor/VRMLnodes/SoVRMLIndexedFaceSet.h
  \brief The SoVRMLIndexedFaceSet class is used for representing a generic 3D shape.

  \ingroup VRMLnodes

  \WEB3DCOPYRIGHT

  \verbatim
  IndexedFaceSet {
    eventIn       MFInt32 set_colorIndex
    eventIn       MFInt32 set_coordIndex
    eventIn       MFInt32 set_normalIndex
    eventIn       MFInt32 set_texCoordIndex
    exposedField  SFNode  color             NULL
    exposedField  SFNode  coord             NULL
    exposedField  SFNode  normal            NULL
    exposedField  SFNode  texCoord          NULL
    field         SFBool  ccw               TRUE
    field         MFInt32 colorIndex        []        # [-1,)
    field         SFBool  colorPerVertex    TRUE
    field         SFBool  convex            TRUE
    field         MFInt32 coordIndex        []        # [-1,)
    field         SFFloat creaseAngle       0         # [0,)
    field         MFInt32 normalIndex       []        # [-1,)
    field         SFBool  normalPerVertex   TRUE
    field         SFBool  solid             TRUE
    field         MFInt32 texCoordIndex     []        # [-1,)
  }
  \endverbatim

  The IndexedFaceSet node represents a 3D shape formed by constructing
  faces (polygons) from vertices listed in the coord field. The coord
  field contains a Coordinate node that defines the 3D vertices
  referenced by the coordIndex field. IndexedFaceSet uses the indices
  in its coordIndex field to specify the polygonal faces by indexing
  into the coordinates in the Coordinate node. An index of "-1"
  indicates that the current face has ended and the next one
  begins. The last face may be (but does not have to be) followed by a
  "-1" index. If the greatest index in the coordIndex field is N, the
  Coordinate node shall contain N+1 coordinates (indexed as 0 to
  N). Each face of the IndexedFaceSet shall have:

  - at least three non-coincident vertices;
  - vertices that define a planar polygon;
  - vertices that define a non-self-intersecting polygon.

  Otherwise, The results are undefined.

  The IndexedFaceSet node is specified in the local coordinate system
  and is affected by the transformations of its ancestors.

  Descriptions of the coord, normal, and texCoord fields are provided
  in the SoVRMLCoordinate, SoVRMLNormal, and SoVRMLTextureCoordinate nodes,
  respectively.

  Details on lighting equations and the interaction
  between color field, normal field, textures, materials, and
  geometries are provided in 4.14, Lighting model.

  If the color field is not NULL, it shall contain a Color node whose
  colours are applied to the vertices or faces of the IndexedFaceSet
  as follows:

  - If colorPerVertex is FALSE, colours are applied to each face, as
    follows:

    - If the colorIndex field is not empty, then one colour is used
      for each face of the IndexedFaceSet. There shall be at least as many indices
      in the colorIndex field as there are faces in the IndexedFaceSet.
      If the greatest index in the colorIndex field is N, then there shall
      be N+1 colours in the Color node. The colorIndex field shall not
      contain any negative entries.

    - If the colorIndex field is empty, then the colours in the Color
      node are applied to each face of the IndexedFaceSet in order. There shall
      be at least as many colours in the Color node as there are faces.

  - If colorPerVertex is TRUE, colours are applied to each vertex,
    as follows:

    - If the colorIndex field is not empty, then colours are applied
      to each vertex of the IndexedFaceSet in exactly the same manner
      that the coordIndex field is used to choose coordinates for each
      vertex from the Coordinate node. The colorIndex field shall
      contain at least as many indices as the coordIndex field, and
      shall contain end-of-face markers (-1) in exactly the same places
      as the coordIndex field.  If the greatest index in the colorIndex
      field is N, then there shall be N+1 colours in the Color node.

    - If the colorIndex field is empty, then the coordIndex field is
      used to choose colours from the Color node. If the greatest index
      in the coordIndex field is N, then there shall be N+1 colours in
      the Color node.

  If the color field is NULL, the geometry shall be rendered normally
  using the Material and texture defined in the Appearance node (see
  4.14, Lighting model, for details
  http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.6.5).

  If the normal field is not NULL, it shall contain a Normal node
  whose normals are applied to the vertices or faces of the
  IndexedFaceSet in a manner exactly equivalent to that described
  above for applying colours to vertices/faces (where normalPerVertex
  corresponds to colorPerVertex and normalIndex corresponds to
  colorIndex). If the normal field is NULL, the browser shall
  automatically generate normals, using creaseAngle to determine if
  and how normals are smoothed across shared vertices (see 4.6.3.5,
  Crease angle field).

  If the texCoord field is not NULL, it shall contain a
  TextureCoordinate node. The texture coordinates in that node are
  applied to the vertices of the IndexedFaceSet as follows: If the
  texCoordIndex field is not empty, then it is used to choose texture
  coordinates for each vertex of the IndexedFaceSet in exactly the
  same manner that the coordIndex field is used to choose coordinates
  for each vertex from the Coordinate node. The texCoordIndex field
  shall contain at least as many indices as the coordIndex field, and
  shall contain end-of-face markers (-1) in exactly the same places as
  the coordIndex field. If the greatest index in the texCoordIndex
  field is N, then there shall be N+1 texture coordinates in the
  TextureCoordinate node.

  If the texCoordIndex field is empty, then the coordIndex array is
  used to choose texture coordinates from the TextureCoordinate
  node. If the greatest index in the coordIndex field is N, then there
  shall be N+1 texture coordinates in the TextureCoordinate node.  If
  the texCoord field is NULL, a default texture coordinate mapping is
  calculated using the local coordinate system bounding box of the
  shape.  The longest dimension of the bounding box defines the S
  coordinates, and the next longest defines the T coordinates. If two
  or all three dimensions of the bounding box are equal, ties shall be
  broken by choosing the X, Y, or Z dimension in that order of
  preference. The value of the S coordinate ranges from 0 to 1, from
  one end of the bounding box to the other. The T coordinate ranges
  between 0 and the ratio of the second greatest dimension of the
  bounding box to the greatest dimension. Figure 6.10 illustrates the
  default texture coordinates for a simple box shaped IndexedFaceSet
  with an X dimension twice as large as the Z dimension and four times
  as large as the Y dimension. Figure 6.11 illustrates the original
  texture image used on the IndexedFaceSet used in Figure 6.10.

  <center>
  <img src="http://www.web3d.org/documents/specifications/14772/V2.0/Images/IFStexture.gif">
  Figure 6.10
  </center>

  <center>
  <img src="http://www.web3d.org/documents/specifications/14772/V2.0/Images/IFStexture2.gif">
  Figure 6.11
  </center>

  Subclause 4.6.3, Shapes and geometry
  (<http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.6.5>),
  provides a description of the ccw, solid, convex, and creaseAngle
  fields.

*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::ccw
  Specifies if vertex ordering is counterclockwise. Default value is TRUE.
*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::solid
  Can be used to enable backface culling. Default value is TRUE.
*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::convex
  Specifies if all polygons are convex. Default value is TRUE.
*/

/*!
  \var SoSFFloat SoVRMLIndexedFaceSet::creaseAngle
  Specifies the crease angle for the generated normals. Default value is 0.0.
*/

package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedFaceSet extends SoVRMLIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLIndexedFaceSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedFaceSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedFaceSet.class); }

	// for concavestatus
	  public static final int STATUS_UNKNOWN = 0;
	  public static final int STATUS_CONVEX  = 1;
	  public static final int STATUS_CONCAVE = 2;

	  public final SoSFBool ccw = new SoSFBool();
	  public final SoSFBool solid = new SoSFBool();
	  public final SoSFBool convex = new SoSFBool();
	  public final SoSFFloat creaseAngle = new SoSFFloat();	  
	  
	  private SoVRMLIndexedFaceSetP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVRMLIndexedFaceSet()
	{
	  pimpl = new SoVRMLIndexedFaceSetP();
	  pimpl.convexCache = null;
	  pimpl.concavestatus = STATUS_UNKNOWN;
	  pimpl.vaindexer = null;

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedFaceSet.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(ccw,"ccw", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(solid,"solid", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(convex,"convex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(creaseAngle,"creaseAngle", (0.0f));

	}

	  

	// Doc in parent
	public void generatePrimitives(SoAction action)
	{
		//TODO
	}
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLIndexedFaceSet, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLIndexedFaceSet.class, "VRMLIndexedFaceSet", SoVRMLIndexedShape.class);
	}

}
