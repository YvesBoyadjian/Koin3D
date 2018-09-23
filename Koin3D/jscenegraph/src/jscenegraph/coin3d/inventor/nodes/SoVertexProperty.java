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
  \class SoVertexProperty SoVertexProperty.h Inventor/nodes/SoVertexProperty.h
  \brief The SoVertexProperty class collects the functionality of various appearance nodes.
  \ingroup nodes

  Instead of reading data from the current state stack of the
  scenegraph traversal, nodes inheriting SoVertexShape can be set up
  with an SoVertexProperty node in the SoVertexShape.vertexProperty
  field. Coordinates, normals, texture coordinates and material /
  color information will then be fetched from the vertexshape's
  SoVertexProperty node instead of from the state stack.

  The SoVertexProperty node provides fields for duplicating the
  functionality of all these other Inventor node types: SoCoordinate3,
  SoTextureCoordinate2, SoTextureCoordinate3, SoNormal, SoPackedColor,
  SoMaterialBinding and SoNormalBinding.


  The SoVertexProperty node was introduced fairly late in the design
  of the Inventor API by SGI. The idea behind it was to provide a
  means to specify the necessary data for vertexshape-derived nodes
  which would be more efficient to work with than fetching the data
  from the traversal state stack.

  In practice, the effect is not at all very noticable. Since the use
  of SoVertexProperty nodes in the SoVertexShape.vertexProperty field
  somewhat breaks with the basic design of the Open Inventor API (the
  SoVertexProperty data is not pushed to the traversal state stack),
  you might be better off design-wise by using the usual mechanisms,
  ie by setting up the individual nodes SoVertexProperty "collects".

  One of the drawbacks will for instance be that it's not possible to
  share \e parts of the SoVertexProperty node between several shapes,
  which is something that can easily be done when setting up
  individual state-changing nodes in the scenegraph.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    VertexProperty {
        vertex [  ]
        normal [  ]
        texCoord [  ]
        orderedRGBA [  ]
        texCoord3 [  ]
        normalBinding PER_VERTEX_INDEXED
        materialBinding OVERALL
    }
  \endcode

  \since Inventor 2.1
  \sa SoVertexShape
  \sa SoCoordinate3, SoTextureCoordinate2, SoTextureCoordinate3, SoNormal
  \sa SoPackedColor
  \sa SoMaterialBinding, SoNormalBinding
*/
// FIXME: should have a usage-example in the class-doc. 20020109 mortene.

// FIXME: this class is really not optimally supported in Coin. Each
// vertex shape node should support this node with internal handling
// to harvest the expected efficiency gains.  Right now we just push
// its values on the stack. ????-??-?? pederb.


/*!
  \enum SoVertexProperty.Binding

  The binding types available for our SoVertexProperty.normalBinding
  and SoVertexProperty.materialBinding fields.

  For a detailed explanation of each of the enumeration value binding
  types, see the documentation of the SoMaterialBinding node.
*/


/*!
  \var SoMFVec3f SoVertexProperty.vertex

  This field sets up vertex coordinates in the same manner as
  SoCoordinate3.point.

  By default the field contains no coordinates.

  \sa SoCoordinate3
*/
/*!
  \var SoMFVec2f SoVertexProperty.texCoord

  Same functionality as SoTextureCoordinate2.point.  By default the
  field contains no coordinates.

  \sa SoTextureCoordinate2
*/

/*!
  \var SoMFInt32 SoVertexProperty.textureUnit

  The texture unit(s) for the texture coordinates. By default this field
  contains one value, 0, and texture coordinates are then sent to
  texture unit 0. It's possible to supply multiple values in this field,
  and the texture coordinates in texCoord or texCoord3 will then be split
  into those units. The first totalnum/numunits coordinates will be sent
  to the first unit specified, the next totalnum/numunits coordinates will
  be sent to the second unit in this field, etc.
  
  \sa SoTextureCoordinate2, SoTextureUnit
  \since Coin 4.0
*/

// FIXME: this field was added between TGS Inventor 2.5 and 2.6, and
// between Coin 1.0 and Coin 2.0. This means it must get special
// handling when exporting .iv-files, with regard to what header we
// can put on the output. See also item #003 in the Coin/docs/todo.txt
// file. 20030519 mortene.
/*!
  \var SoMFVec3f SoVertexProperty.texCoord3

  Same functionality as SoTextureCoordinate3.point.  By default the
  field contains no coordinates.

  \sa SoTextureCoordinate3
  \since Coin 2.0
  \since TGS Inventor 2.6
*/

/*!
  \var SoMFVec3f SoVertexProperty.normal

  This field defines a set of normal vectors in the same manner as
  SoNormal.vector.  By default the field contains no vectors.

  \sa SoNormal
*/
/*!
  \var SoSFEnum SoVertexProperty.normalBinding

  Defines how to bind the normals specified in the
  SoVertexProperty.normal set to the parts of the "owner" shape.
  Must be one of the values in the SoVertexProperty.Binding enum.

  Default value of the field is SoVertexProperty.PER_VERTEX_INDEXED.

  \sa SoNormalBinding
*/
/*!
  \var SoMFUInt32 SoVertexProperty.orderedRGBA

  A set of "packed" 32-bit diffusecolor plus transparency
  values. Works in the same manner as the SoPackedColor.orderedRGBA
  field.

  By default the field has no data.

  \sa SoPackedColor
*/
/*!
  \var SoSFEnum SoVertexProperty.materialBinding

  Defines how to bind the colorvalues specified in the
  SoVertexProperty.orderedRGBA set to the parts of the "owner" shape.
  Must be one of the values in the SoVertexProperty.Binding enum.

  Default value of the field is SoVertexProperty.OVERALL.

  \sa SoMaterialBinding
*/

package jscenegraph.coin3d.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.misc.Tidbits;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLNormalElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoMFUInt32;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexProperty extends SoNode {

	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVertexProperty.class,this);	   
	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVertexProperty.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVertexProperty.class); }    
	  
	  // These must match 100% with the enum in SoMaterialBindingElement.
	  public enum Binding {
	    OVERALL( 2),
	    PER_PART(3),
	    PER_PART_INDEXED(4),
	    PER_FACE(5),
	    PER_FACE_INDEXED(6),
	    PER_VERTEX(7),
	    PER_VERTEX_INDEXED(8);
		  
		  private int value;
		  
		  Binding(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }
	  };

	  public final SoMFVec3f vertex = new SoMFVec3f();
	  public final SoMFVec2f texCoord = new SoMFVec2f();
	  public final SoMFVec3f texCoord3 = new SoMFVec3f();
	  public final SoMFVec3f normal = new SoMFVec3f();
	  public final SoSFEnum normalBinding = new SoSFEnum();
	  public final SoMFUInt32 orderedRGBA = new SoMFUInt32();
	  public final SoSFEnum materialBinding = new SoSFEnum();
	  public final SoMFInt32 textureUnit = new SoMFInt32();

	  private SoVertexPropertyP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVertexProperty()
	{
	  this.pimpl = new SoVertexPropertyP();

	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoVertexProperty.class);

	  nodeHeader.SO_NODE_ADD_EMPTY_MFIELD(vertex,"vertex");
	  nodeHeader.SO_NODE_ADD_EMPTY_MFIELD(normal,"normal");
	  nodeHeader.SO_NODE_ADD_EMPTY_MFIELD(texCoord,"texCoord");
	  nodeHeader.SO_NODE_ADD_EMPTY_MFIELD(orderedRGBA,"orderedRGBA");
	  // FIXME: this field was added in TGS Inventor 2.6 and Coin
	  // 2.0. This should have repercussions for file format
	  // compatibility. 20030227 mortene.
	  nodeHeader.SO_NODE_ADD_EMPTY_MFIELD(texCoord3,"texCoord3");
	  nodeHeader.SO_NODE_ADD_MFIELD(textureUnit,"textureUnit", (0));

	  nodeHeader.SO_NODE_ADD_FIELD(normalBinding,"normalBinding", (SoVertexProperty.Binding.PER_VERTEX_INDEXED.getValue()));
	  nodeHeader.SO_NODE_ADD_FIELD(materialBinding,"materialBinding", (SoVertexProperty.Binding.OVERALL.getValue()));

	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.OVERALL);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART_INDEXED);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE_INDEXED);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX_INDEXED);

	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(normalBinding,"normalBinding", "Binding");
	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(materialBinding,"materialBinding", "Binding");

	}

	/*!
	  Destructor.
	*/
	public void destructor()
	{
	  Destroyable.delete(this.pimpl);
	  super.destructor();
	}

	// Documented in superclass.
	public void
	getBoundingBox(SoGetBoundingBoxAction action)
	{
	  if (vertex.getNum() > 0) {
	    SoCoordinateElement.set3(action.getState(), this,
	                              vertex.getNum(), vertex.getValuesSbVec3fArray(/*0*/));
	  }
	}

	// Documented in superclass.
	public void
	GLRender(SoGLRenderAction action)
	{
	  SoVertexProperty_doAction(action);
	}

	private static boolean TEST_OVERRIDE( SoOverrideElement.ElementMask bit, int flags) {
		return ((bit.getValue() & flags) != 0);
	}

	// Documented in superclass.
	public void
	SoVertexProperty_doAction(SoAction action)
	{
	  SoState state = action.getState();

	  int overrideflags = SoOverrideElement.getFlags(state);
	  boolean glrender = action.isOfType(SoGLRenderAction.getClassTypeId());
	  if (glrender) SoBase.staticDataLock();

	  if (this.pimpl.checktransparent) {
	    this.pimpl.checktransparent = false;
	    this.pimpl.transparent = false;
	    int n = this.orderedRGBA.getNum();
	    for (int i = 0; i < n; i++) {
	      if ((this.orderedRGBA.operator_square_bracketI(i) & 0xff) != 0xff) {
	        this.pimpl.transparent = true;
	        break;
	      }
	    }
	  }
	   boolean shouldcreatevbo = glrender ? SoGLVBOElement.shouldCreateVBO(state, this.vertex.getNum()) : false;
	  this.updateVertex(state, glrender, shouldcreatevbo);
	  this.updateNormal(state, overrideflags, glrender, shouldcreatevbo);
	  this.updateMaterial(state, overrideflags, glrender, shouldcreatevbo);
	  this.updateTexCoord(state, glrender, shouldcreatevbo);

	  if (glrender) SoBase.staticDataUnlock();
	}

	// Documented in superclass.
	public void
	callback(SoCallbackAction action)
	{
	  SoVertexProperty_doAction((SoAction)action);
	}

	// Documented in superclass.
	public void
	pick(SoPickAction action)
	{
	  SoVertexProperty_doAction((SoAction)action);
	}

	// Documented in superclass.
	public void
	getPrimitiveCount(SoGetPrimitiveCountAction action)
	{
	  if (vertex.getNum() > 0) {
	    SoCoordinateElement.set3(action.getState(), this,
	                              vertex.getNum(), vertex.getValuesSbVec3fArray(/*0*/));
	  }
	}

	// Documented in superclass. Overridden to check for transparency when
	// orderedRGBA changes.
	public void
	notify(SoNotList list)
	{
	  SoField f = list.getLastField();
	  if (f == this.orderedRGBA) {
	    this.pimpl.checktransparent = true;
	  }
	  super.notify(list);
	}


	public void 
	updateVertex(SoState state, boolean glrender, boolean vbo)
	{
	  int num = this.vertex.getNum();

	  if (num > 0) {    
	    SoCoordinateElement.set3(state, this, num,
	                              this.vertex.getValuesSbVec3fArray(/*0*/));
	    if (glrender) {
	      if (vbo) {
	        boolean dirty = false;
	        if (this.pimpl.vertexvbo[0] == null) {
	          this.pimpl.vertexvbo[0] = new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/); // YB 
	          dirty =  true;
	        }
	        else if (this.pimpl.vertexvbo[0].getBufferDataId() != this.getNodeId()) {
	          dirty = true;
	        }
	        if (dirty) {
	          this.pimpl.vertexvbo[0].setBufferData(VoidPtr.create(this.vertex.getValuesSbVec3fArray(/*0*/)),
	                                                  num*SbVec3f.sizeof(),
	                                                  this.getNodeId(),state);
	        }
	      }
	      else if (this.pimpl.vertexvbo[0] != null && this.pimpl.vertexvbo[0].getBufferDataId() != 0) {
	        // clear buffers to deallocate VBO memory
	        this.pimpl.vertexvbo[0].setBufferData(null, 0, 0,state);
	      }
	      SoGLVBOElement.setVertexVBO(state, vbo ? this.pimpl.vertexvbo[0] : null);
	    }
	  }
	}

	public void 
	updateTexCoord(SoState state, boolean glrender, boolean vbo)
	{
	   int numvertex = this.vertex.getNum();
	  int num = this.texCoord3.getNum();
	  int dim = 3;
	   SbVec3fArray tc3 = (num != 0) ? this.texCoord3.getValuesSbVec3fArray(/*0*/) : null;
	   SbVec2fArray tc2 = null;
	  if (num == 0) {
	    num = this.texCoord.getNum();
	    dim = 2;
	    if (num != 0) {
	      tc2 = this.texCoord.getValuesSbVec2fArray(/*0*/);
	    }
	  }
	  if (num > 0) {
	     int numunits = this.textureUnit.getNum();  
	     int numperunit = num / numunits;

	    if ((num % numunits) != 0) {
	      SoDebugError.postWarning("SoVertexProperty.updateTexCoord",
	                                "Wrong number of texture coordinates. The number of texture "+
	                                "coordinates must be dividable by the number of units in "+
	                                "the textureUnit field.");
	    } 
	    else {
	      for (int i = 0; i < numunits; i++) {
	        int unit = this.textureUnit.operator_square_bracketI(i);
	        if (glrender) {
	          // it's important to call this _before_ setting the coordinates
	          // on the state.
	          SoGLMultiTextureCoordinateElement.setTexGen(state,
	                                                       this, unit, null);
	        }
	        if (dim == 2) {
	          SoMultiTextureCoordinateElement.set2(state, this, unit, numperunit,
	                                                tc2.plus(i*numperunit));
	        }
	        else {
	          SoMultiTextureCoordinateElement.set3(state, this, unit, numperunit,
	                                                tc3.plus(i*numperunit));
	        }
	      
	        if (glrender) {
	          boolean setvbo = false;

	          if (i >= this.pimpl.texcoordvbo.getLength()) {
	            this.pimpl.texcoordvbo.append(/*null*/new SoVBO[1]);
	          }
	          if ((numperunit == numvertex) && vbo) {
	            boolean dirty = false;
	            setvbo = true;
	            if (this.pimpl.texcoordvbo.operator_square_bracket(i) == null) {
	              this.pimpl.texcoordvbo.operator_square_bracket(i)[0]= new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/); 
	              dirty =  true;
	            }
	            else if (this.pimpl.texcoordvbo.operator_square_bracket(i)[0].getBufferDataId() != this.getNodeId()) {
	              dirty = true;
	            }
	            if (dirty) {
	              if (dim == 2) {
	                this.pimpl.texcoordvbo.operator_square_bracket(i)[0].setBufferData(VoidPtr.create(tc2.plus( i * numperunit)),
	                                                             numperunit*SbVec2f.sizeof(),
	                                                             this.getNodeId(),state);
	              }
	              else {
	                this.pimpl.texcoordvbo.operator_square_bracket(i)[0].setBufferData(VoidPtr.create(tc3.plus( i * numperunit)),
	                                                             numperunit*SbVec3f.sizeof(),
	                                                             this.getNodeId(),state);
	              }
	            }
	          }
	          else if (this.pimpl.texcoordvbo.operator_square_bracket(i)[0]!= null && 
	                   this.pimpl.texcoordvbo.operator_square_bracket(i)[0].getBufferDataId() != 0) {
	            // clear buffers to deallocate VBO memory
	            this.pimpl.texcoordvbo.operator_square_bracket(i)[0].setBufferData(null, 0, 0,state);
	          }
	          SoGLVBOElement.setTexCoordVBO(state, 0, setvbo ? this.pimpl.texcoordvbo.operator_square_bracket(i)[0] : null);
	        }
	      }
	    }
	  }
	}
	  
	public void 
	updateNormal(SoState state, int overrideflags, boolean glrender, boolean vbo)
	{
	   int numvertex = this.vertex.getNum();
	   int num = this.normal.getNum();
	  if (num > 0 && !TEST_OVERRIDE(SoOverrideElement.ElementMask.NORMAL_VECTOR, overrideflags)) {
	    SoNormalElement.set(state, this, num,
	                         this.normal.getValuesSbVec3fArray(/*0*/));
	    if (this.isOverride()) {
	      SoOverrideElement.setNormalVectorOverride(state, this, true);
	    }
	    if (glrender) {
	      boolean setvbo = false;
	      if ((num == numvertex) && vbo) {
	        boolean dirty = false;
	        setvbo = true;
	        if (this.pimpl.normalvbo[0] == null) {
	          this.pimpl.normalvbo[0] = new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/); 
	          dirty =  true;
	        }
	        else if (this.pimpl.normalvbo[0].getBufferDataId() != this.getNodeId()) {
	          dirty = true;
	        }
	        if (dirty) {
	          this.pimpl.normalvbo[0].setBufferData(VoidPtr.create(this.normal.getValuesSbVec3fArray(/*0*/)),
	                                                  num*SbVec3f.sizeof(),
	                                                  this.getNodeId(),state);
	        }
	      }
	      else if (this.pimpl.normalvbo[0] != null && this.pimpl.normalvbo[0].getBufferDataId() != 0) {
	        // clear buffers to deallocate VBO memory
	        this.pimpl.normalvbo[0].setBufferData(null, 0, 0,state);
	      }
	      SoGLVBOElement.setNormalVBO(state, setvbo ? this.pimpl.normalvbo[0] : null);
	    }
	  }
	  if (this.normal.getNum() > 0 && !TEST_OVERRIDE(SoOverrideElement.ElementMask.NORMAL_BINDING, overrideflags)) {
	    SoNormalBindingElement.set(state,/* this,*/
	                                SoNormalBindingElement.Binding.fromValue(
	                                this.normalBinding.getValue()));
	    if (this.isOverride()) {
	      SoOverrideElement.setNormalBindingOverride(state, this, true);
	    }
	  }
	}

	public void 
	updateMaterial(SoState state, int overrideflags, boolean glrender, boolean vbo)
	{
	   int numvertex = this.vertex.getNum();
	  int num = this.orderedRGBA.getNum();
	  if (num > 0 && 
	      !TEST_OVERRIDE(SoOverrideElement.ElementMask.DIFFUSE_COLOR, overrideflags)) {
	    
	    SoLazyElement.setPacked(state, this, num,
	                             this.orderedRGBA.getValuesI(0)/*,*/
	                             /*this.pimpl.transparent*/); // TODO YB COIN 3D
	    if (this.isOverride()) {
	      SoOverrideElement.setDiffuseColorOverride(state, this, true);
	    }
	    if (glrender) {
	      boolean setvbo = false;
	      if ((num == numvertex) && vbo) {
	        boolean dirty = false;
	        setvbo = true;
	        if (this.pimpl.colorvbo[0] == null) {
	          this.pimpl.colorvbo[0] = new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/);
	          dirty = true;
	        }
	        else if (this.pimpl.colorvbo[0].getBufferDataId() != this.getNodeId()) {
	          dirty = true;
	        }
	        if (dirty) {
	          if (Tidbits.coin_host_get_endianness() == Tidbits.CoinEndiannessValues.COIN_HOST_IS_BIGENDIAN) {
	            this.pimpl.colorvbo[0].setBufferData(VoidPtr.create(this.orderedRGBA.getValuesI(0)),
	                                                   num*Integer.BYTES,
	                                                   this.getNodeId(),state);
	          }
	          else {
	             int[] src = this.orderedRGBA.getValuesI(0);
	            int[] dst = (int[]) 
	              this.pimpl.colorvbo[0].allocBufferData(num*Integer.BYTES, 
	                                                       this.getNodeId(),state).toIntBuffer().array();  // YB
	            for (int i = 0; i < num; i++) {
	              int tmp = src[i];
	              dst[i] = 
	                (tmp << 24) |
	                ((tmp & 0xff00) << 8) |
	                ((tmp & 0xff0000) >> 8) |
	                (tmp >> 24);
	            }
	          }
	        }
	      }
	      else if (this.pimpl.colorvbo[0] != null) {
	        this.pimpl.colorvbo[0].setBufferData(null, 0, 0,state);
	      }
	      SoGLVBOElement.setColorVBO(state, setvbo ? this.pimpl.colorvbo[0] : null);
	    }
	  }
	  if (num != 0 && !TEST_OVERRIDE(SoOverrideElement.ElementMask.MATERIAL_BINDING, overrideflags)) {
	    SoMaterialBindingElement.set(state, /*this,*/
	                                  SoMaterialBindingElement.Binding.fromValue(
	                                  this.materialBinding.getValue()));
	    if (this.isOverride()) {
	      SoOverrideElement.setMaterialBindingOverride(state, this, true);
	    }
	  }
	}

//	#undef PRIVATE
//	#undef TEST_OVERRIDE
	  
	  
	// Documented in superclass.
	public static void
	initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVertexProperty, SO_FROM_INVENTOR_2_1);
		SoSubNode.SO__NODE_INIT_CLASS(SoVertexProperty.class, "VertexProperty", SoNode.class);

	  SO_ENABLE(SoGetBoundingBoxAction.class, SoCoordinateElement.class);

	  SO_ENABLE(SoGLRenderAction.class, SoGLCoordinateElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoMaterialBindingElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoGLNormalElement.class);
	  SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureCoordinateElement.class);

	  SO_ENABLE(SoPickAction.class, SoCoordinateElement.class);
	  SO_ENABLE(SoPickAction.class, SoMaterialBindingElement.class);
	  SO_ENABLE(SoPickAction.class, SoNormalBindingElement.class);
	  SO_ENABLE(SoPickAction.class, SoNormalElement.class);
	  SO_ENABLE(SoPickAction.class, SoMultiTextureCoordinateElement.class);

	  SO_ENABLE(SoCallbackAction.class, SoCoordinateElement.class);
	  SO_ENABLE(SoCallbackAction.class, SoMaterialBindingElement.class);
	  SO_ENABLE(SoCallbackAction.class, SoNormalBindingElement.class);
	  SO_ENABLE(SoCallbackAction.class, SoNormalElement.class);
	  SO_ENABLE(SoCallbackAction.class, SoMultiTextureCoordinateElement.class);

	  SO_ENABLE(SoGetPrimitiveCountAction.class, SoCoordinateElement.class);
	  SO_ENABLE(SoGetPrimitiveCountAction.class, SoMaterialBindingElement.class);
	  SO_ENABLE(SoGetPrimitiveCountAction.class, SoNormalBindingElement.class);
	  SO_ENABLE(SoGetPrimitiveCountAction.class, SoNormalElement.class);
	  SO_ENABLE(SoGetPrimitiveCountAction.class, SoMultiTextureCoordinateElement.class);
	}
	
	/**
	 * Java port YB
	 * @param action
	 */
	public void doAction(SoAction action) {
		SoVertexProperty_doAction(action);
	}
	
	public void putVBOsIntoState(SoState state) {
		  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
			    if (vertex.getNum() > 0) {
			      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.VERTEX_VBO, pimpl.vertexvbo,
			        vertex.getNum() * SbVec3f.sizeof(), VoidPtr.create(vertex.getValuesArray(0)), getNodeId());
			    }
			    if (normal.getNum() > 0) {
			      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.NORMAL_VBO, pimpl.normalvbo,
			        normal.getNum() * SbVec3f.sizeof(), VoidPtr.create(normal.getValuesArray(0)), getNodeId());
			    }
			    if (orderedRGBA.getNum() > 0
			      && ! SoOverrideElement.getDiffuseColorOverride(state)) {
			        // update the VBO, no data is passed since that is done by SoColorPacker later on
			        SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, pimpl.colorvbo);
			    }
			    if ( texCoord.getNum() > 0) {
			      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.TEXCOORD_VBO, pimpl.texcoordvbo.operator_square_bracket(0),
			        texCoord.getNum() * SbVec2f.sizeof(), VoidPtr.create(texCoord.getValuesArray(0)), getNodeId());
			    }
			  }
	}

}
