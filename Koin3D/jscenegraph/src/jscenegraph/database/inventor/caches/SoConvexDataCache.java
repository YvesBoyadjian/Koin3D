
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

package jscenegraph.database.inventor.caches;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.inventor.SbTesselator;
import jscenegraph.coin3d.inventor.base.SbGLUTessellator;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SoConvexDataCache extends SoCache {

	/*!
	  \enum SoConvexDataCache::Binding
	  \brief The Binding enum is used to specify bindings.

	  Binding applies to normals, materials and texture coordinates.
	*/

	  public enum Binding {
		    // do not change these values. We rely on them matching
		    // values in SoIndededFaceSet.h and SoGL.cpp...
		    NONE,
		    PER_FACE,
		    PER_FACE_INDEXED,
		    PER_VERTEX,
		    PER_VERTEX_INDEXED;

			public int getValue() {
				return ordinal();
			}
			
			public static Binding fromValue(int value) {
				switch(value) {
				case 0 : return NONE;
				case 1 : return PER_FACE;
				case 2 : return PER_FACE_INDEXED;
				case 3 : return PER_VERTEX;
				case 4 : return PER_VERTEX_INDEXED;
				default: return null;
				}
			}
		  };

		  private SoConvexDataCacheP pimpl;
	
	/*!
	  Constructor with \a state being the current state.
	*/
	public SoConvexDataCache(SoState state) {
	  super(state);
	
	  pimpl = new SoConvexDataCacheP();
	//#if COIN_DEBUG
	  if (TidBits.coin_debug_caching_level() > 0) {
	    SoDebugError.postInfo("SoConvexDataCache::SoConvexDataCache",
	                           "Cache created: "+ this);
	    
	  }
	//#endif // debug
	}

	/*!
	  Destructor.
	*/
	public void destructor()
	{
	//#if COIN_DEBUG
	  if (TidBits.coin_debug_caching_level() > 0) {
	    SoDebugError.postInfo("SoConvexDataCache::~SoConvexDataCache",
	                           "Cache destructed: "+ this);
	    
	  }
	//#endif // debug
	  Destroyable.delete (pimpl);
	  super.destructor();
	}

	/*!
	  Returns a pointer to the convexified coordinate indices.
	  \sa SoConvexDataCache::getNumCoordIndices()
	*/
	public IntArrayPtr
	getCoordIndices()
	{
	  if (pimpl.coordIndices.getLength() != 0) return pimpl.coordIndices.getArrayPtr();
	  return null;
	}

	/*!
	  Returns the number of coordinate indices.
	  \sa SoConvexDataCache::getCoordIndices()
	*/
	public int
	getNumCoordIndices()
	{
	  return pimpl.coordIndices.getLength();
	}

	/*!
	  Returns the convexified material indices.
	  \sa SoConvexDataCache::getNumMaterialIndices()
	*/
	public IntArrayPtr
	getMaterialIndices()
	{
	  if (pimpl.materialIndices.getLength() != 0) return pimpl.materialIndices.getArrayPtr();
	  return null;
	}

	/*!
	  Returns the number of material indices.
	  \sa SoConvexDataCache::getMaterialIndices()
	*/
	public int
	getNumMaterialIndices()
	{
	  return pimpl.materialIndices.getLength();
	}

	/*!
	  Returns the convexified normal indices.
	  \sa SoConvexDataCache::getNumNormalIndices()
	*/
	public IntArrayPtr
	getNormalIndices()
	{
	  if (pimpl.normalIndices.getLength() != 0) return pimpl.normalIndices.getArrayPtr();
	  return null;
	}

	/*!
	  Returns the number of normal indices.
	  \sa SoConvexDataCache::getNormalIndices()
	*/
	public int
	getNumNormalIndices()
	{
	  return pimpl.normalIndices.getLength();
	}

	/*!
	  Returns the convexified texture coordinate indices.
	  \sa SoConvexDataCache::getNumTexIndices()
	*/
	public IntArrayPtr
	getTexIndices()
	{
	  if (pimpl.texIndices.getLength() != 0) return pimpl.texIndices.getArrayPtr();
	  return null;
	}

	/*!
	  Returns the number of texture coordinate indices.
	  \sa SoConvexDataCache::getTexIndices()
	*/
	public int
	getNumTexIndices()
	{
	  return pimpl.texIndices.getLength();
	}

/*!
  Generates the convexified data. FIXME: doc
*/
public void
generate( SoCoordinateElement coords,
                            final SbMatrix matrix,
                            final IntArrayPtr vind,
                            int numv,
                            IntArrayPtr mind, IntArrayPtr nind,
                            IntArrayPtr tind,
                            Binding matbind, Binding normbind,
                            Binding texbind)
{
//#if COIN_DEBUG && 0
//  SoDebugError.postInfo("SoConvexDataCache::generate",
//                         "generating convex data");
//#endif

  boolean identity = matrix.operator_equal_equal(SbMatrix.identity());

  // remove old data
  pimpl.coordIndices.truncate(0);
  pimpl.materialIndices.truncate(0);
  pimpl.normalIndices.truncate(0);
  pimpl.texIndices.truncate(0);

  int matnr = 0;
  int texnr = 0;
  int normnr = 0;

  // initialize the struct with data needed during tessellation
  final tTessData tessdata = new tTessData();
  tessdata.matbind = matbind;
  tessdata.normbind = normbind;
  tessdata.texbind = texbind;
  tessdata.numvertexind = 0;
  tessdata.nummatind = 0;
  tessdata.numnormind = 0;
  tessdata.numtexind = 0;
  // FIXME: stupid to have a separate struct for each coordIndex
  // should only allocate enough to hold the largest polygon
  tessdata.vertexInfo = new Array<tVertexInfo>(tVertexInfo.class, new tVertexInfo[numv]);// for (int i=0;i<numv;i++) tessdata.vertexInfo[i] = new tVertexInfo();
  tessdata.vertexIndex = null;
  tessdata.matIndex = null;
  tessdata.normIndex = null;
  tessdata.texIndex = null;
  tessdata.firstvertex = true;

  // create tessellator
  final SbGLUTessellator glutess = new SbGLUTessellator(SoConvexDataCache::do_triangle, tessdata);
  final SbTesselator tess = new SbTesselator(SoConvexDataCache::do_triangle, tessdata);
  final boolean gt = SbGLUTessellator.preferred();

  // if PER_FACE binding, the binding must change to PER_FACE_INDEXED
  // if convexify data is used.
  tessdata.vertexIndex = pimpl.coordIndices;
  if (matbind != Binding.NONE)
    tessdata.matIndex = pimpl.materialIndices;
  if (normbind != Binding.NONE)
    tessdata.normIndex = pimpl.normalIndices;
  if (texbind != Binding.NONE)
    tessdata.texIndex = pimpl.texIndices;

  if (gt) { glutess.beginPolygon(); }
  else { tess.beginPolygon(); }
  for (int i = 0; i < numv; i++) {
    if (vind.get(i) < 0) {
      if (gt) { glutess.endPolygon(); }
      else { tess.endPolygon(); }
      if (matbind == Binding.PER_VERTEX_INDEXED || 
          matbind == Binding.PER_FACE ||
          matbind == Binding.PER_FACE_INDEXED) matnr++;
      if (normbind == Binding.PER_VERTEX_INDEXED ||
          normbind == Binding.PER_FACE ||
          normbind == Binding.PER_FACE_INDEXED) normnr++;
      if (texbind == Binding.PER_VERTEX_INDEXED) texnr++;
      if (i < numv - 1) { // if not last polygon
        if (gt) { glutess.beginPolygon(); }
        else { tess.beginPolygon(); }
      }
    }
    else {
      tessdata.vertexInfo.get(i).vertexnr = vind.get(i);
      if (mind != null)
        tessdata.vertexInfo.get(i).matnr = mind.get(matnr);
      else tessdata.vertexInfo.get(i).matnr = matnr;
      if (matbind.getValue() >= Binding.PER_VERTEX.getValue()) {
        matnr++;
      }
      if (nind != null)
        tessdata.vertexInfo.get(i).normnr = nind.get(normnr);
      else tessdata.vertexInfo.get(i).normnr = normnr;
      if (normbind.getValue() >= Binding.PER_VERTEX.getValue())
        normnr++;
      if (tind != null)
        tessdata.vertexInfo.get(i).texnr = tind.get(texnr++);
      else
        tessdata.vertexInfo.get(i).texnr = texnr++;

      final SbVec3f v = new SbVec3f(coords.get3(vind.get(i)));
      if (!identity) matrix.multVecMatrix(v,v);
      if (gt) { glutess.addVertex(v, (Object)(tessdata.vertexInfo.addressOf(i))); }
      else { tess.addVertex(v, (Object)(tessdata.vertexInfo.addressOf(i))); }
    }
  }
  
  // if last coordIndex != -1, terminate polygon
  if (numv > 0 && vind.get(numv-1) != -1) {
    if (gt) { glutess.endPolygon(); }
    else { tess.endPolygon(); }
  }

  //delete [] tessdata.vertexInfo; java magic !

  pimpl.coordIndices.fit();
  if (tessdata.matIndex != null) pimpl.materialIndices.fit();
  if (tessdata.normIndex != null) pimpl.normalIndices.fit();
  if (tessdata.texIndex != null) pimpl.texIndices.fit();
  
  tess.destructor();
  glutess.destructor();
}

//
// helper function for do_triangle() below
//
static void
vertex_tri(tVertexInfo info, tTessData tessdata)
{
  tessdata.vertexIndex.append(info.vertexnr);
  tessdata.numvertexind++;

  if (tessdata.matIndex != null &&
      (tessdata.firstvertex ||
       tessdata.matbind.getValue() >= SoConvexDataCache.Binding.PER_VERTEX.getValue())) {
    tessdata.matIndex.append(info.matnr);
    tessdata.nummatind++;
  }

  if (tessdata.normIndex != null &&
      (tessdata.firstvertex ||
       tessdata.normbind.getValue() >= SoConvexDataCache.Binding.PER_VERTEX.getValue())) {
    tessdata.normIndex.append(info.normnr);
    tessdata.numnormind++;
  }
  if (tessdata.texIndex != null &&
      tessdata.texbind.getValue() != SoConvexDataCache.Binding.NONE.getValue()) {
    tessdata.texIndex.append(info.texnr);
    tessdata.numtexind++;
  }
  tessdata.firstvertex = false;
}



//
// handles callbacks from SbTesselator or SbGLUTessellator
//
private static void
do_triangle(Object v0, Object v1, Object v2, Object data)
{
  tTessData tessdata = (tTessData)(data);
  tessdata.firstvertex = true;
  vertex_tri((((Array<tVertexInfo>)v0).get(0)), tessdata);
  vertex_tri((((Array<tVertexInfo>)v1).get(0)), tessdata);
  vertex_tri((((Array<tVertexInfo>)v2).get(0)), tessdata);

  tessdata.vertexIndex.append(-1);
  if (tessdata.matIndex != null &&
      tessdata.matbind.getValue() >= SoConvexDataCache.Binding.PER_VERTEX.getValue()) {
    tessdata.matIndex.append(-1);
    tessdata.nummatind++;
  }
  if (tessdata.normIndex != null &&
      tessdata.normbind.getValue() >= SoConvexDataCache.Binding.PER_VERTEX.getValue()) {
    tessdata.normIndex.append(-1);
    tessdata.numnormind++;
  }
  if (tessdata.texIndex != null &&
      tessdata.texbind.getValue() != SoConvexDataCache.Binding.NONE.getValue()) {
    tessdata.texIndex.append(-1);
    tessdata.numtexind++;
  }
}

	
}
