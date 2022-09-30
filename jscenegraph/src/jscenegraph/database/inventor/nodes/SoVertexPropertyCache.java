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
 * Copyright (C) 1995-96   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoVertexProperty node class, and the
 |       SoVertexPropertyCache class.
 |
 |   Author(s)          : Alan Norton, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoMachine;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.FloatArray;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.VoidPtr;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;


///////////////////////////////////////////////////////////////////////////////
///
///   Description:
///      The SoVertexPropertyCache class is a convenient way for vertex-based
///      shapes to store information on how they should render
///      themselves.  It takes care of some messiness that they'd
///      otherwise have to figure out.  It is designed to be a very
///      lightweight class.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///   Description:
///      The SoVertexPropertyCache class is a convenient way for vertex-based
///      shapes to store information on how they should render
///      themselves.  It takes care of some messiness that they'd
///      otherwise have to figure out.  It is designed to be a very
///      lightweight class.
///
//////////////////////////////////////////////////////////////////////////////

//#include <Inventor/elements/SoShapeStyleElement.h>

public class SoVertexPropertyCache {
	
	public interface SoVPCacheFunc {
		void run(GL2 gl2,Object argument);
	};


  public

    //! API used by shapes:

    boolean mightNeedSomethingFromState( SoShapeStyleElement sse) 
        { return (needFromState & sse.getRenderCaseMask()) != 0; }
  

  private void vp_glColor4ubv( GL2 gl2, byte[] v)
{
    byte[] _v = new byte[4];
    SoMachine.DGL_HTON_INT32(_v, v);

    int v_offset = 0;
    gl2.glColor4ubv(_v, v_offset);
}

  public    void        fillInColorAndTranspAvail( SoVertexProperty vp, SoState state)
{
    // Diffuse colors:
    transpIsInVP = false;
    if (!SoGLLazyElement.isColorIndex(state)) {
        boolean colorOverride = ((SoOverrideElement.getDiffuseColorOverride(state))||
                                (SoOverrideElement.getTransparencyOverride(state)));
        if ((!colorOverride) &&         
            (vp != null && (numColors = vp.orderedRGBA.getNum()) != 0)) {
            colorIsInVP = true;
            //Check to see if there really is a non-opaque transparency:
            for(int i = 0; i< vp.orderedRGBA.getNum(); i++){
                if ((vp.orderedRGBA.operator_square_bracketI(i) & 0xff) != 0xff){
                    transpIsInVP = true;
                    break;
                }
            }
        } else {  //Get color from state
            colorIsInVP = false;
        } 
    }
}

  public   void        fillInCache( SoVertexProperty vp,   SoState state)
{
    renderCase = 0;

    // Coordinates:
    if (vp != null && (numVerts = vp.vertex.getNum()) != 0) {
        vertexFunc = new SoVPCacheFunc() {
			@Override
			public void run(GL2 gl2, Object array) {
				if(array instanceof FloatBuffer) {
					FloatBuffer v = (FloatBuffer)array;
					gl2.glVertex3fv(v);
				}
				else {
					float[] v = (float[])array;
					int v_offset = 0;
					gl2.glVertex3fv(v, v_offset);
				}
			}
        };
        vertexStride = SbVec3f.sizeof();
        vertexPtr = vp.vertex.getValuesFloatArray();///*getValuesFloat(0)*/getValuesRef();
        needFromState &= ~BitMask.COORD_FROM_STATE_BIT.getValue();
    } else {
        SoCoordinateElement ce =
            SoCoordinateElement.getInstance(state);
        numVerts = ce.getNum();
        if (ce.is3D()) {
            vertexFunc = new SoVPCacheFunc() {
    			@Override
    			public void run(GL2 gl2, Object array) {
    				if(array instanceof FloatBuffer) {
    					FloatBuffer floatBuffer = (FloatBuffer)array;
    					gl2.glVertex3fv(floatBuffer);
    				}
    				else if(array instanceof ByteBuffer) {
    					ByteBuffer floatBuffer = (ByteBuffer)array;
    					gl2.glVertex3fv(floatBuffer.asFloatBuffer());
    				}
    				else {
    					float[] v = (float[])array;
    					int v_offset = 0;
    					gl2.glVertex3fv(v, v_offset);
    				}
    			}
            };
            vertexStride = SbVec3f.sizeof();
            vertexPtr = ce.getArrayPtr3f();//get3Ptr();
        } else {
            vertexFunc = new SoVPCacheFunc() {
    			@Override
    			public void run(GL2 gl2, Object array) {
    				float[] v = (float[])array;
    				int v_offset = 0;
    				gl2.glVertex4fv(v, v_offset);
    			}
            };
            vertexStride = SbVec4f.sizeof();
            vertexPtr = ce.getArrayPtr4f();//get4Ptr();
        }           
        needFromState |= BitMask.COORD_FROM_STATE_BIT.getValue();
    }

    // Diffuse colors:
    transpIsInVP = false;
    boolean colorOverride = ((SoOverrideElement.getDiffuseColorOverride(state))||
                    (SoOverrideElement.getTransparencyOverride(state)));
    if (SoGLLazyElement.isColorIndex(state)) {
        SoLazyElement le = SoLazyElement.getInstance(state);    
        numColors = le.getNumDiffuse();
        colorFunc = new SoVPCacheFunc() {
			@Override
			public void run(GL2 gl2, Object array) {
				int[] c = (int[])array;
				int c_offset = 0;
        		gl2.glIndexiv(c,c_offset);
			}
        	
        };
        colorStride = Integer.SIZE/Byte.SIZE;// sizeof(int32_t);
        colorPtr = (IntArrayPtr)new IntArrayPtr(le.getColorIndexPointer());
        needFromState |= BitMask.COLOR_FROM_STATE_BITS.getValue();
    } else {
        if ((!colorOverride) &&         
            (vp != null && (numColors = vp.orderedRGBA.getNum()) != 0)) {
            colorPtr = (IntArrayPtr)new IntArrayPtr(vp.orderedRGBA.getValues(0));
            colorFunc = new SoVPCacheFunc () {

				@Override
				public void run(GL2 gl2, Object argument) {
					if(argument instanceof IntBuffer) {
						IntBuffer buf = (IntBuffer)argument;						
						int v = buf.get(); 
						byte[] abgr = new byte[4];
						abgr[0] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[1] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[2] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[3] = (byte)(v & 0xFF);
						vp_glColor4ubv(gl2,abgr);
					}
					else {
						vp_glColor4ubv(gl2,(byte[])argument);
					}
				}            	
            };
            colorStride = Integer.SIZE/Byte.SIZE;//sizeof(uint32_t);
            needFromState &= ~BitMask.COLOR_FROM_STATE_BITS.getValue();
            colorIsInVP = true;
            //Check to see if there really is a non-opaque transparency:
            for(int i = 0; i< vp.orderedRGBA.getNum(); i++){
                if ((vp.orderedRGBA.operator_square_bracketI(i) & 0xff) != 0xff){
                    transpIsInVP = true;
                    break;
                }
            }
        } else {  //Get color from state
            SoLazyElement le = SoLazyElement.getInstance(state);
            colorIsInVP = false;
            numColors = le.getNumDiffuse();
            needFromState |= BitMask.COLOR_FROM_STATE_BITS.getValue();
            colorPtr = (IntArrayPtr) le.getPackedPointer();
            colorFunc = new SoVPCacheFunc () {

				@Override
				public void run(GL2 gl2, Object argument) {
					if(argument instanceof ByteBuffer) {
						argument = ((ByteBuffer)argument).asIntBuffer();
					}
					if(argument instanceof IntBuffer) {
						IntBuffer buf = (IntBuffer)argument;						
						int v = buf.get(); 
						byte[] abgr = new byte[4];
						abgr[0] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[1] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[2] = (byte)(v & 0xFF); v = v >>> 8;
						abgr[3] = (byte)(v & 0xFF);
						vp_glColor4ubv(gl2,abgr);
					}
					else if(argument instanceof Integer) {
                        int v = (Integer)argument;
                        byte[] abgr = new byte[4];
                        abgr[0] = (byte)(v & 0xFF); v = v >>> 8;
                        abgr[1] = (byte)(v & 0xFF); v = v >>> 8;
                        abgr[2] = (byte)(v & 0xFF); v = v >>> 8;
                        abgr[3] = (byte)(v & 0xFF);
                        vp_glColor4ubv(gl2,abgr);
                    }
					else {
						vp_glColor4ubv(gl2,(byte[])argument);
					}
				}
            };
            colorStride = Integer.SIZE/Byte.SIZE;
        } 

    }

    //setup materialBinding
    if ((needFromState & BitMask.COLOR_FROM_STATE_BITS.getValue())!=0 ||
            SoOverrideElement.getMaterialBindingOverride(state)) {
        materialBinding = (byte)SoMaterialBindingElement.get(state).getValue(); 
    }
    else {
        materialBinding = vp.materialBinding.getValue().byteValue();     
    }
    
    final SoMaterialBindingElement.Binding binding = SoMaterialBindingElement.Binding.fromValue(materialBinding);
    
    switch (binding) {
      case OVERALL:
        // 00... bits, nothing to do.  But if color is overridden, still
        // needFromState should remain on, to guarantee that we will
        // continue to fill in the cache.
        if(!colorOverride) needFromState &= ~BitMask.COLOR_FROM_STATE_BITS.getValue();
        numColors = 1;
        break;
      case PER_PART:
        renderCase |= Bits.PART_COLOR.getValue();
        break;
      case PER_PART_INDEXED:
        renderCase |= Bits.PART_COLOR.getValue();
        break;
      case PER_FACE:
        renderCase |= Bits.FACE_COLOR.getValue();
        break;
      case PER_FACE_INDEXED:
        renderCase |= Bits.FACE_COLOR.getValue();
        break;
      case PER_VERTEX:
        renderCase |= Bits.VERTEX_COLOR.getValue();
        break;
      case PER_VERTEX_INDEXED:
        renderCase |= Bits.VERTEX_COLOR.getValue();
        break;
    }

    // Normals:
    // Setup as if normals are needed, even if they aren't
   
    normalFunc = new SoVPCacheFunc() {

		@Override
		public void run(GL2 gl2, Object argument) {
			if(argument instanceof FloatBuffer) {
				FloatBuffer fb = (FloatBuffer)argument;
				gl2.glNormal3fv(fb);
			}
			else if(argument instanceof ByteBuffer) {
				ByteBuffer fb = (ByteBuffer)argument;
				gl2.glNormal3fv(fb.asFloatBuffer());
			}
			else {
				float[] v = (float[])argument; 
				int v_offset = 0;
				gl2.glNormal3fv(v, v_offset);
			}
		}
    };
    normalStride = SbVec3f.sizeof();

    if (vp != null && (numNorms = vp.normal.getNum()) != 0) {
        normalPtr = vp.normal.getValuesSbVec3fArray();// getValuesFloat(0);
        needFromState &= ~BitMask.NORMAL_FROM_STATE_BITS.getValue();
        generateNormals = false;
    } else {
        needFromState |= BitMask.NORMAL_FROM_STATE_BITS.getValue();

        SoNormalElement ne =
            SoNormalElement.getInstance(state);
        if ((numNorms = ne.getNum()) > 0) {
            normalPtr = ne.getArrayPtr();//get0();
            generateNormals = false;
        } else {
            generateNormals = true;
        }           
    }

    // set up normalBinding
    if (generateNormals) {
        normalBinding = (byte)SoNormalBindingElement.Binding.PER_VERTEX.getValue();
    }
    else if ((needFromState & BitMask.NORMAL_FROM_STATE_BITS.getValue())!=0) {
        normalBinding = (byte)SoNormalBindingElement.get(state).getValue();
    }
    else {
        normalBinding = (byte)SoNormalBindingElement.Binding.fromValue(vp.normalBinding.getValue()).getValue();
    }
        
    SoNormalBindingElement.Binding normalBindingEnum = SoNormalBindingElement.Binding.fromValue(normalBinding);
    switch (normalBindingEnum) {
        case OVERALL:
            // 00... bits, nothing to do
            break;
        case PER_PART:
            renderCase |= Bits.PART_NORMAL.getValue();
            break;
        case PER_PART_INDEXED:
            renderCase |= Bits.PART_NORMAL.getValue();
            break;
        case PER_FACE:
            renderCase |= Bits.FACE_NORMAL.getValue();
            break;
        case PER_FACE_INDEXED:
            renderCase |= Bits.FACE_NORMAL.getValue();
            break;
        case PER_VERTEX:
            renderCase |= Bits.VERTEX_NORMAL.getValue();
            break;
        case PER_VERTEX_INDEXED:
            renderCase |= Bits.VERTEX_NORMAL.getValue();
            break;
    }
    
  
    
    //Setup as if texture coords are needed, even if they are not:
    if (vp != null && (numTexCoords = vp.texCoord.getNum()) != 0) {
        texCoordFunc = new SoVPCacheFunc() {

			@Override
			public void run(GL2 gl2, Object argument) {
				float[] v = (float[])argument;
				int v_offset = 0;
				gl2.glTexCoord2fv(v,v_offset);
			}
        	//glTexCoord2fv;
        };
        texCoordStride = SbVec2f.sizeof();
        texCoordPtr = vp.texCoord.getValues(0).getValuesArray();
        needFromState &= ~BitMask.TEXCOORD_FROM_STATE_BIT.getValue();
        needFromState |= BitMask.TEXTURE_FUNCTION_BIT.getValue();
        texCoordBinding =
                (byte)SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED.getValue();
        renderCase |= Bits.TEXCOORD_BIT.getValue();
        generateTexCoords = false;
    } else {
        SoMultiTextureCoordinateElement tce =
                SoMultiTextureCoordinateElement.getInstance(state);
        
        int tue = SoTextureUnitElement.get(state);
        
        if ((numTexCoords = tce.getNum(tue)) > 0) {
            if (tce.is2D(tue)) {
                texCoordFunc = new SoVPCacheFunc(){

					@Override
					public void run(GL2 gl2, Object argument) {
						if(argument instanceof FloatBuffer) {
							FloatBuffer floatBuffer = (FloatBuffer)argument;
							gl2.glTexCoord2fv(floatBuffer);
						}
						else if(argument instanceof ByteBuffer) {
							ByteBuffer floatBuffer = (ByteBuffer)argument;
							gl2.glTexCoord2fv(floatBuffer.asFloatBuffer());
						}
						else {
							float[] v = (float[])argument;
							int v_offset = 0;
							gl2.glTexCoord2fv(v,v_offset);
						}
					}
                }; 
                texCoordStride = SbVec2f.sizeof();
                texCoordPtr = tce.getArrayPtr2(tue).toFloat();//get2Ptr(tue);
            } else {
                texCoordFunc = new SoVPCacheFunc () {

					@Override
					public void run(GL2 gl2, Object argument) {
						float[] v = (float[])argument;
						int v_offset = 0;
						gl2.glTexCoord4fv(v, v_offset);						
					}
                	//glTexCoord4fv;
                };
                texCoordStride = SbVec4f.sizeof();
                texCoordPtr = tce.getArrayPtr4(tue).toFloat();//get4Ptr(tue);
            }
                texCoordBinding =
                    (byte)SoTextureCoordinateBindingElement.get(state).getValue();
                renderCase |= Bits.TEXCOORD_BIT.getValue();
                generateTexCoords = false;
            } else {
                texCoordFunc = null;
                generateTexCoords = true;
                renderCase &= ~Bits.TEXCOORD_BIT.getValue();  // No normals, use glTexGen
            }
        needFromState |= BitMask.TEXCOORD_FROM_STATE_BIT.getValue();
        needFromState &= ~BitMask.TEXTURE_FUNCTION_BIT.getValue();
    }    
}

  

  public    boolean      shouldGenerateNormals( SoShapeStyleElement sse) 
        { return (generateNormals && sse.needNormals()); }

  public    boolean      shouldGenerateTexCoords( SoShapeStyleElement sse) 
        { return (generateTexCoords && sse.needTexCoords()); }

  public    int         getRenderCase( SoShapeStyleElement sse) 
        { return renderCase & sse.getRenderCaseMask(); }
        
  public   boolean      haveTexCoordsInVP()
        { return (needFromState & BitMask.TEXTURE_FUNCTION_BIT.getValue()) != 0; }

//  public   void        sendVertex( char *vp) 
//        { (*vertexFunc)(vp); }
//  public   void        sendNormal( char *np) 
//        { (*normalFunc)(np); }
//  public   void        sendColor( char *cp) 
//        { (*colorFunc)(cp); }
//  public   void        sendTexCoord( char *tcp) 
//        { (*texCoordFunc)(tcp); }
  
  void        sendNormal(GL2 gl2, FloatBuffer np)
  { (normalFunc).run(gl2, np); }


  public    int         getNumVertices()  { return numVerts; }
  public    int         getNumNormals()  { return numNorms; }
  public   int         getNumColors()  { return numColors; }
  public  int         getNumTexCoords()  { return numTexCoords; }

  public    FloatBuffer getVertices(int i) 
        {
            if(null == vertexPtr) {
                return null;
            }
	  	int offset = (int)((long)vertexStride*i/Float.BYTES);
	  	int length = vertexPtr.size()/* .length*/ - offset;
	  	ByteBuffer bb = vertexPtr.toByteBuffer();
	  	bb.position(offset*Float.BYTES);
	  	return bb.asFloatBuffer(); //Buffers.copyFloatBuffer(FloatBuffer.wrap(vertexPtr, offset, length));
	  }
  public    FloatBuffer getNormals(int i)  
        { 
	  	int offset = (int)((long)normalStride*i/Float.BYTES);
	  	int length = normalPtr.getSizeFloat()/*length*/ - offset;
	  	ByteBuffer bb = normalPtr.toByteBuffer();
	  	bb.position(offset*Float.BYTES);
	  	return bb.asFloatBuffer(); //Buffers.copyFloatBuffer(FloatBuffer.wrap(normalPtr, offset, length));
	  }
  public   /*Buffer*/VoidPtr getColors(int i) 
        { 
	  	int offset = (int)((long)colorStride*i/Integer.BYTES);
	  	int length = colorPtr.size() - offset;
	  	return VoidPtr.create(/*Buffers.copyIntBuffer(*/new IntArrayPtr(offset, colorPtr)/*IntBuffer.wrap(colorPtr, offset, length)*//*)*/);	  	
	  }
  public   FloatBuffer getTexCoords(int i) 
        { 
	  	int offset = (int)((long)texCoordStride*i/Float.BYTES);
	  	int length = texCoordPtr.numFloats() - offset;
	  	
	  	FloatBuffer fb = texCoordPtr.toByteBuffer().asFloatBuffer();
	  	fb.position(offset);
	  	return fb.slice();
	  	
	  	//return Buffers.copyFloatBuffer(FloatBuffer.wrap(texCoordPtr, offset, length));
	  }
        
  public    boolean colorIsInVtxProp()  {return colorIsInVP;}
    
    //!Indicates if non-opaque transparency is in vertex property.
  public   boolean transpIsInVtxProp()  {return transpIsInVP;}

  public   int         getVertexStride()  { return vertexStride; }
  public   int         getNormalStride()  { return normalStride; }
  public   int         getColorStride()  { return colorStride; }
  public   int         getTexCoordStride()  { return texCoordStride; }
    
  public   SoNormalBindingElement.Binding     getNormalBinding()
        { return SoNormalBindingElement.Binding.fromValue(normalBinding); }
        
  public   SoMaterialBindingElement.Binding   getMaterialBinding()
        { return SoMaterialBindingElement.Binding.fromValue(materialBinding); }

  public   SoTextureCoordinateBindingElement.Binding  getTexCoordBinding()
        { return SoTextureCoordinateBindingElement.Binding.fromValue(texCoordBinding); }

  public   void        invalidate() { needFromState = (byte)BitMask.ALL_FROM_STATE_BITS.getValue(); }

    //! Constructor.   Initializes everything.
  public   SoVertexPropertyCache()
{
    vertexFunc = normalFunc = colorFunc = texCoordFunc = null;
    vertexPtr = null; normalPtr = null; colorPtr = null; texCoordPtr = null;
    vertexStride = normalStride = colorStride = texCoordStride = 0;
    numVerts = numNorms = numColors = numTexCoords = 0;
    colorIsInVP = transpIsInVP = false;
    generateNormals = generateTexCoords = false;
    needFromState = (byte)BitMask.ALL_FROM_STATE_BITS.getValue();
    renderCase = 0;
}
    
  public

    //! Tables of functions, data, and strides to increment through data.
    SoVPCacheFunc vertexFunc;
  public    FloatArray vertexPtr;
  public    int vertexStride, numVerts;
  public   SoVPCacheFunc normalFunc;
  public    SbVec3fArray normalPtr;
  public    int normalStride, numNorms;
  public    SoVPCacheFunc colorFunc;
  public    IntArrayPtr colorPtr;
  public   int colorStride, numColors;
  public    SoVPCacheFunc texCoordFunc;
  public    FloatMemoryBuffer texCoordPtr;
  public   int texCoordStride, numTexCoords;

  public   boolean generateNormals;
  public   boolean generateTexCoords;
    
  public   byte needFromState;

    //! 32 rendering cases.  Non-indexed shapes can get away with just
    //! looking at the low 5 bits.  The bits are arranged like this:
    //! 43210  BITS
    //! -----  ----
    //! 00...  Overall color
    //! 01...  Part color
    //! 10...  Face color
    //! 11...  Vtx color
    //! ..00.  Overall/No norm
    //! ..01.  Part norm
    //! ..10.  Face norm
    //! ..11.  Vtx norm
    //! ....?  Explicit exture coordinates needed
  public
    enum Bits { 
        COLOR_BITS (0x18),
        PART_COLOR (0x8),
        FACE_COLOR (0x10),
        VERTEX_COLOR (0x18),

        NORMAL_BITS (0x6),
        PART_NORMAL (0x2),
        FACE_NORMAL (0x4),
        VERTEX_NORMAL (0x6),

        TEXCOORD_BIT (0x1);
        
        Bits(int value) {
        	this.value = value;
        }        
        private int value;
        
        public int getValue() {
        	return value;
        }
        };
    //! Bit-mask, set by ::fillInCache method
    //! Bits use same as above, plus one for coords and one for 
    //! overrides (diffuse color, material binding, transparency)
    //! plus one for texture function (if tcoords in VP)
  
        public   enum BitMask {
        TEXTURE_FUNCTION_BIT (0x80),
        OVERRIDE_FROM_STATE_BIT (0x40), 
        COORD_FROM_STATE_BIT (0x20),
        COLOR_FROM_STATE_BITS (Bits.COLOR_BITS.getValue()),
        NORMAL_FROM_STATE_BITS (Bits.NORMAL_BITS.getValue()),
        TEXCOORD_FROM_STATE_BIT (Bits.TEXCOORD_BIT.getValue()),
        ALL_FROM_STATE_BITS (0xFF);
        
        BitMask(int value) {
        	this.value = value;
        }
        
        private int value;
        
        public int getValue() {
        	return value;
        }
    };
  private

    //!
    //! VertexPropertyCache also stores whether or not colors, normals and
    //! texture coordinates should be indexed.  Suggestion for writing
    //! rendering loops:
    //!   
    //! To minimize loop overhead, loop unrolling is a good idea.
    //! Doing two iterations per loop makes it about 20% faster.  Doing
    //! three iterations per loop makes it about 28% faster (only 8%
    //! more than two iterations).
    //!  
    //! On 200MHZ machines, an unrolled-by-3 loop can do close to 5
    //! million iterations per second, assuming one call per iteration
    //! (e.g. one-color, unlit polygons).
    //!
    byte normalBinding;
  private    byte materialBinding;
  private    byte texCoordBinding;
  private    byte renderCase;
  private   boolean colorIsInVP;
  private   boolean transpIsInVP;
}

