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
  \class SoVRMLIndexedLineSet SoVRMLIndexedLineSet.h Inventor/VRMLnodes/SoVRMLIndexedLineSet.h
  \brief The SoVRMLIndexedLineSet class is used to represent a generic 3D line shape.

  \ingroup VRMLnodes

  \WEB3DCOPYRIGHT

  \verbatim
  IndexedLineSet {
    eventIn       MFInt32 set_colorIndex
    eventIn       MFInt32 set_coordIndex
    exposedField  SFNode  color             NULL
    exposedField  SFNode  coord             NULL
    field         MFInt32 colorIndex        []     # [-1, inf)
    field         SFBool  colorPerVertex    TRUE
    field         MFInt32 coordIndex        []     # [-1, inf)
  }
  \endverbatim

  The IndexedLineSet node represents a 3D geometry formed by
  constructing polylines from 3D vertices specified in the coord
  field. IndexedLineSet uses the indices in its coordIndex field to
  specify the polylines by connecting vertices from the coord
  field. An index of "-1" indicates that the current polyline has
  ended and the next one begins. The last polyline may be (but does
  not have to be) followed by a "-1".  IndexedLineSet is specified in
  the local coordinate system and is affected by the transformations
  of its ancestors.

  The coord field specifies the 3D vertices of the line set and
  contains a Coordinate node.  Lines are not lit, are not
  texture-mapped, and do not participate in collision detection. The
  width of lines is implementation dependent and each line segment is
  solid (i.e., not dashed).  If the color field is not NULL, it shall
  contain a Color node.  The colours are applied to the line(s) as
  follows:

  - If colorPerVertex is FALSE:

    - If the colorIndex field is not empty, one colour is used for
      each polyline of the IndexedLineSet. There shall be at least as
      many indices in the colorIndex field as there are polylines in the
      IndexedLineSet.  If the greatest index in the colorIndex field is
      N, there shall be N+1 colours in the Color node. The colorIndex
      field shall not contain any negative entries.

    - If the colorIndex field is empty, the colours from the Color
      node are applied to each polyline of the IndexedLineSet in
      order. There shall be at least as many colours in the Color node
      as there are polylines.

  - If colorPerVertex is TRUE:

    - If the colorIndex field is not empty, colours are applied to
      each vertex of the IndexedLineSet in exactly the same manner that
      the coordIndex field is used to supply coordinates for each vertex
      from the Coordinate node. The colorIndex field shall contain at
      least as many indices as the coordIndex field and shall contain
      end-of-polyline markers (-1) in exactly the same places as the
      coordIndex field.  If the greatest index in the colorIndex field
      is N, there shall be N+1 colours in the Color node.

    - If the colorIndex field is empty, the coordIndex field is used
      to choose colours from the Color node. If the greatest index in
      the coordIndex field is N, there shall be N+1 colours in the Color
      node.

  If the color field is NULL and there is a Material defined for the
  Appearance affecting this IndexedLineSet, the emissiveColor of the
  Material shall be used to draw the lines. Details on lighting
  equations as they affect IndexedLineSet nodes are described in 4.14,
  Lighting model
  (<http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.14>).

*/

		package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedLineSet extends SoVRMLIndexedLine {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLIndexedLineSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedLineSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedLineSet.class); }
	  
	  private SoVRMLIndexedLineSetP pimpl;
	  
	  public SoVRMLIndexedLineSet()
	  {
	    pimpl = new SoVRMLIndexedLineSetP();
	    nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedLineSet.class);
	  }	  	  
	
	  public void generatePrimitives(SoAction action) {
		  //TODO
	  }

	public void notify(SoNotList list)
	{
		SoField f = list.getLastField();
		if (f == this.coordIndex) {
			SoBase.staticDataLock();//LOCK_VAINDEXER(this);
		Destroyable.delete(pimpl.vaindexer);
		pimpl.vaindexer = null;
			SoBase.staticDataUnlock();//UNLOCK_VAINDEXER(this);
	}
		super.notify(list);
	}

	  /*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLIndexedLineSet, SO_VRML97_NODE_TYPE);
		SoSubNode.SO__NODE_INIT_CLASS(SoVRMLIndexedLineSet.class, "VRMLIndexedLineSet", SoVRMLIndexedLine.class);
	}

	  
}
