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
 |      This file defines the SoNormalCache class, which is used for
 |      storing generated normals.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.caches;

import jscenegraph.coin3d.TidBits;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoNormalGenerator;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbVec3fArray;


/////////////////////////////////////////////////////////////////////////
///
///  Class SoNormalCache:
///
///  A normal cache stores a list of normals.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNormalCache extends SoCache implements Destroyable {

	private static float NORMAL_EPSILON = Math.ulp(1.0f);//FLT_EPSILON;

  private SoNormalCacheP pimpl;
    //int                 numNormals;             //!< Number of normals
    //private SbVec3fArray       normals;               //!< Array of normals


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNormalCache(SoState state) { super(state);
//
////////////////////////////////////////////////////////////////////////
	pimpl = new SoNormalCacheP();

    pimpl.numNormals = 0;
    pimpl.normalData_normals = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	clearGenerator();
    if (pimpl.normalData_normals != null) {
        pimpl.normalData_normals = null;
        }
    pimpl = null;
    super.destructor();
}

    //! Returns the number of normals and list of normals
public     int                 getNum()           {
	  if (pimpl.numNormals == 0 && pimpl.normalData_generator != null) {
		    return pimpl.normalData_generator.getNumNormals();
		  }
	  return pimpl.numNormals; 
	}
public  SbVec3fArray      getNormals()       {
	  if (pimpl.numNormals == 0 && pimpl.normalData_generator != null) {
		    return pimpl.normalData_generator.getNormals();
		  }
		  return pimpl.normalData_normals;
	}

// java port
public float[] getNormalsFloat() {
	
	SbVec3fArray normals = getNormals();
	
	int numNormals = getNum();
	float[] normalArray = new float[numNormals*3];
	int index=0;
	for(int i=0;i<numNormals;i++) {
		normalArray[index] = normals.get(i).getValueRead()[0];
		index++;
		normalArray[index] = normals.get(i).getValueRead()[1];
		index++;
		normalArray[index] = normals.get(i).getValueRead()[2];
		index++;
	}
	return normalArray;
}

/*!
  Returns the normal indices.
*/
public IntArrayPtr 
getIndices()
{
  if (pimpl.indices.getLength() != 0) return pimpl.indices.getArrayPtr();
  return null;
}

//
// calculates the normal vector for a vertex, based on the
// normal vectors of all incident faces
//
static int calc_norm_error = 0;
static void
calc_normal_vec( SbVec3fArray facenormals, int facenum, 
                int numfacenorm, SbListInt faceArray, 
                float threshold, final SbVec3f vertnormal)
{
  // start with face normal vector
  SbVec3fArray facenormal = facenormals.plus(facenum);
  vertnormal.copyFrom(facenormal.get(0));

  int n = faceArray.getLength();
  int currface;

  for (int i = 0; i < n; i++) {
    currface = faceArray.operator_square_bracket(i);
    if (currface != facenum) { // check all but this face
      if (currface < numfacenorm || numfacenorm == -1) { // -1 means: assume
        final SbVec3f normal = facenormals.get(currface);  // everything is ok
        if ((normal.dot(facenormal.get(0))) > threshold) {
          // smooth towards this face
          vertnormal.operator_add_equal(normal);
        }
      }
      else {
        if (calc_norm_error < 1) {
          SoDebugError.postWarning("SoNormalCache::calc_normal_vec", "Normals "+
                                    "have not been specified for all faces. "+
                                    "this warning will only be shown once, "+
                                    "but there might be more errors");
        }

        calc_norm_error++;
      }
    }
  }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Stores a list of normals in the cache.
//
// Use: public

public void
set(int _numNormals, final SbVec3fArray _normals)
//
////////////////////////////////////////////////////////////////////////
{
    this.clearGenerator();
    pimpl.numNormals = _numNormals;
    pimpl.normalData_normals    = _normals; pimpl.normalData_generator = null;
    pimpl.indices.truncate(0, true);
    pimpl.normalArray.truncate(0, true);
}

/*!
  Uses a normal generator in this cache. The normal generator will
  be deleted when the cache is deleted or reset.
*/
public void set(SoNormalGenerator generator) {
	  this.clearGenerator();
	  pimpl.indices.truncate(0, true);
	  pimpl.normalArray.truncate(0, true);
	  pimpl.numNormals = 0;
	  pimpl.normalData_generator = generator; pimpl.normalData_normals = null; // java port
}
//
// frees generator and resets normal data.
//
public void
clearGenerator()
{
  if (pimpl.numNormals == 0 && pimpl.normalData_generator != null) {
    Destroyable.delete(pimpl.normalData_generator);
  }
  pimpl.normalData_normals = null; pimpl.normalData_generator = null; // java port
  pimpl.numNormals = 0;
}

/*!
  Generates normals for each vertex for each face. It is possible to
  specify face normals if these have been calculated somewhere else,
  otherwise the face normals will be calculated before the vertex
  normals are calculated. \a tristrip should be \c TRUE if the
  geometry consists of triangle strips.
*/
public void generatePerVertex(SbVec3fArray coords,
        int numcoords,
        IntArrayPtr coordindices,
        int numcoordindices,
        float crease_angle,
        SbVec3fArray facenormals,
        int numfacenormals,
        boolean ccw ) {
	generatePerVertex(coords,
			numcoords,
			coordindices,
			numcoordindices,
			crease_angle,
			facenormals,
			numfacenormals,
			ccw,
			false);
}

static int normgenerrors_vertex = 0;
public void
generatePerVertex( SbVec3fArray coords,
                                 int numcoords,
                                 IntArrayPtr vindex,
                                 int numvi,
                                 float crease_angle,
                                 SbVec3fArray facenormals,
                                 int numfacenormals,
                                 boolean ccw,
                                 boolean tristrip)
{
//#if NORMALCACHE_DEBUG && COIN_DEBUG
//  SoDebugError::postInfo("SoNormalCache::generatePerVertex", "generating normals");
//#endif

  this.clearGenerator();
  pimpl.indices.truncate(0);
  pimpl.normalArray.truncate(0);


//#if COIN_DEBUG && 0 // debug
//  SoDebugError::postInfo("SoNormalCache::generatePerVertex", "%d", numvi);
//  for (int vrtidx=0; vrtidx < numvi; vrtidx++)
//    fprintf(stdout, "%d ", vindex[vrtidx]);
//  fprintf(stdout, "\n");
//#endif // debug


  int numfacenorm = numfacenormals;
  final SoNormalCache tempcache = new SoNormalCache(null);
  SbVec3fArray facenorm = SbVec3fArray.copyOf(facenormals);
  if (facenorm == null) {
    // use a SoNormalCache to store temporary data
    if (tristrip) {
      tempcache.generatePerFaceStrip(coords, numcoords, vindex, numvi, ccw);
    }
    else {
      tempcache.generatePerFace(coords, numcoords, vindex, numvi, ccw);
    }

    facenorm = tempcache.getNormals();
    numfacenorm = tempcache.getNum();

    if(facenorm == null) {
    	throw new IllegalStateException("Normals should be generated for all coords");
    }
  }

  // find biggest vertex index
  int i;
  int maxi = 0;
  int temp;
  for (i = 0; i < numvi; i++) {
    temp = vindex.get(i); // don't care about -1's
    if (temp > maxi) maxi = temp;
  }

  // for each vertex, store all faceindices the vertex is a part of
  SbListInt[] vertexFaceArray = new SbListInt[maxi+1]; // [0, maxi]
  for(i=0;i<maxi+1;i++) vertexFaceArray[i] = new SbListInt();

  // for each vertex, store all normals that have been calculated
  SbListInt[] vertexNormalArray = new SbListInt[maxi+1]; // [0, maxi]
  for(i=0;i<maxi+1;i++) vertexNormalArray[i] = new SbListInt();

  int numfaces = 0;

  if (tristrip) {
    // Find and save the faces belonging to the different vertices
    i = 0;
    while (i + 2 < numvi) {
      temp = vindex.get(i);
      if (temp >= 0 && (int)(temp) < numcoords) {
        vertexFaceArray[temp].append(numfaces);
      }
      else {
        i = i+1;
        numfaces++;
        continue;
      }

      temp = vindex.get(i+1);
      if (temp >= 0 && (int)(temp) < numcoords) {
        vertexFaceArray[temp].append(numfaces);
      }
      else {
        i = i+2;
        numfaces++;
        continue;
      }

      temp = vindex.get(i+2);
      if (temp >= 0 && (int)(temp) < numcoords) {
        vertexFaceArray[temp].append(numfaces);
      }
      else {
        i = i+3;
        numfaces++;
        continue;
      }

      temp = i+3 < numvi ? vindex.get(i+3) : -1;
      if (temp < 0 || (int)(temp) >= numcoords) {
        i = i + 4; // Jump to next possible face
        numfaces++;
        continue;
      }

      i++;
      numfaces++;
    }
  }
  else { // !tristrip
    for (i = 0; i < numvi; i++) {
      temp = vindex.get(i);
      if (temp >= 0 && (int)(temp) < numcoords) {
        vertexFaceArray[temp].append(numfaces);
      }
      else {
        numfaces++;
      }
    }
  }

  float threshold = (float)(Math.cos(SbBasic.SbClamp(crease_angle, 0.0f, (float)(Math.PI))));
  boolean found;
  int currindex = 0; // current normal index
  int nindex = 0;
  int j, n ;
  int facenum = 0;
  int stripcnt = 0;

  for (i = 0; i < numvi; i++) {
    currindex = vindex.get(i);
    if (currindex >= 0 && (int)(currindex) < numcoords) {
      if (tristrip) {
        if (++stripcnt > 3) facenum++; // next face
      }
      // calc normal for this vertex
      final SbVec3f tmpvec = new SbVec3f();
      calc_normal_vec(facenorm, facenum, numfacenorm, vertexFaceArray[currindex], 
                      threshold, tmpvec);

      // Be robust when it comes to erroneously specified triangles.
      if ((tmpvec.normalize() == 0.0f) && TidBits.coin_debug_extra()!=0) {
//#if COIN_DEBUG
        if (normgenerrors_vertex < 1) {
          SoDebugError.postWarning("SoNormalCache::generatePerVertex","Unable to "+
                                    "generate valid normal for face "+facenum);
        }
        normgenerrors_vertex++;
//#endif // COIN_DEBUG
      }
      // it's really ok to have a null vector for a face/vertex, and we
      // should not set it to some dummy vector. A null vector just
      // means that the face is empty, and that the face shouldn't be
      // considered when generating vertex normals.  
      // pederb, 2005-12-21

      if (pimpl.normalArray.getLength() <= nindex)
        pimpl.normalArray.append(new SbVec3f(tmpvec));
      else
        pimpl.normalArray.operator_square_bracket(nindex, new SbVec3f(tmpvec));

      // try to find equal normal (total smoothing)
      SbListInt array = vertexNormalArray[currindex];
      found = false;
      n = array.getLength();
      int same_normal = -1;
      for (j = 0; j < n && !found; j++) {
        same_normal = array.operator_square_bracket(j);
        found = pimpl.normalArray.operator_square_bracket(same_normal).equals(pimpl.normalArray.operator_square_bracket(nindex),
                                                      NORMAL_EPSILON);
      }
      if (found)
        pimpl.indices.append(same_normal);
      // might be equal to the previous normal (when all normals for a face are equal)
      else if ((nindex > 0) &&
               pimpl.normalArray.operator_square_bracket(nindex).equals(pimpl.normalArray.operator_square_bracket(nindex-1),
                                                NORMAL_EPSILON)) {
        pimpl.indices.append(nindex-1);
      }
      else {
        pimpl.indices.append(nindex);
        array.append(nindex);
        nindex++;
      }
    }
    else { // new face
      facenum++;
      stripcnt = 0;
      pimpl.indices.append(-1); // add a -1 for PER_VERTEX_INDEXED binding
    }
  }
  if (pimpl.normalArray.getLength()!=0) {
    pimpl.normalData_normals = SbVec3fArray.fromArray(pimpl.normalArray.getArrayPtr(new SbVec3f[pimpl.normalArray.getLength()])); pimpl.normalData_generator = null; // java port
    pimpl.numNormals = pimpl.normalArray.getLength();
  }
//#if NORMALCACHE_DEBUG && COIN_DEBUG
  SoDebugError.postInfo("SoNormalCache::generatePerVertex",
                         "generated normals per vertex: "+pimpl.normalData_normals+" "+pimpl.numNormals+" "+pimpl.indices.getLength()+"\n"
                         );
//#endif
  //delete [] vertexFaceArray; java port
  //delete [] vertexNormalArray; java port
  tempcache.destructor();
}

/*!
  Generates face normals for the faceset defined by \a coords
  and \a cind. 
*/
static int normgenerrors_face = 0;

public void
generatePerFace(SbVec3fArray coords,
                               int numcoords,
                               IntArrayPtr cind,
                               int nv,
                               boolean ccw)
{
	cind = IntArrayPtr.copyOf(cind); // java port : dont modify argument
//#if NORMALCACHE_DEBUG && COIN_DEBUG
//    SoDebugError::postInfo("SoNormalCache::generatePerFace", "generating normals");
//#endif

  this.clearGenerator();
  pimpl.indices.truncate(0);
  pimpl.normalArray.truncate(0, true);

  IntArrayPtr cstart = new IntArrayPtr(cind);
  IntArrayPtr endptr = cind.plus(nv);

  final SbVec3fSingle tmpvec = new SbVec3fSingle();

  int maxcoordidx = numcoords - 1;

  while (cind.plus(2).lessThan(endptr)) {
    int v0 = cind.get(0);
    int v1 = cind.get(1);
    int v2 = cind.get(2);

    if (v0 < 0 || v1 < 0 || v2 < 0 ||
        v0 > maxcoordidx || v1 > maxcoordidx || v2 > maxcoordidx) {
//#if COIN_DEBUG
      SoDebugError.postWarning("SoNormalCache::generatePerFace",
                                "Polygon with less than three valid "+
                                "vertices detected. (offset: "+cind.minus(cstart)+", ["+v0+" "+v1+" "+v2+"]). "+
                                "Should be within [0, "+maxcoordidx+"]."
                                 );
//#endif // COIN_DEBUG

       // Insert dummy normal for robustness
      final SbVec3f dummynormal = new SbVec3f();
      dummynormal.setValue(0.0f, 0.0f, 0.0f);
      pimpl.normalArray.append(dummynormal);

      // Skip ahead to next possible index
      if (cind.get(0) < 0 || cind.get(0) > maxcoordidx) {
        cind = cind.plus(1);
      }
      else if (cind.get(1) < 0 || cind.get(1) > maxcoordidx) {
        cind = cind.plus(2);
      }
      else if (cind.plus(3).lessThan(endptr) && (cind.get(2) < 0 || cind.get(2) > maxcoordidx)) {
        cind = cind.plus(3);
      }
      else {
        cind = cind.plus(3); // For robustness check after while loop
        break;
      }

      continue;
    }
    
    if (cind.plus(3).greaterOrEqual(endptr) || cind.get(3) < 0 || cind.get(3) > maxcoordidx) { // triangle
      if (!ccw)
        tmpvec.copyFrom((coords.get(v0).operator_minus(coords.get(v1))).cross(coords.get(v2).operator_minus(coords.get(v1))));
      else
        tmpvec.copyFrom((coords.get(v2).operator_minus(coords.get(v1))).cross(coords.get(v0).operator_minus(coords.get(v1))));

      // Be robust when it comes to erroneously specified triangles.
      if ((tmpvec.normalize() == 0.0f) && TidBits.coin_debug_extra()!=0) {
        if (normgenerrors_face < 1) {
          SoDebugError.postWarning("SoNormalCache::generatePerFace",
                                    "Erroneous triangle specification in model "+
                                    "(indices= ["+v0+", "+v1+", "+v2+"], "+
                                    "coords=<"+coords.get(v0).getValueRead()[0]+", "+coords.get(v0).getValueRead()[1]+", "+coords.get(v0).getValueRead()[2]+">, <"+coords.get(v1).getValueRead()[0]+", "+coords.get(v1).getValueRead()[1]+", "+coords.get(v1).getValueRead()[2]+">, <"+coords.get(v2).getValueRead()[0]+", "+coords.get(v2).getValueRead()[1]+", "+coords.get(v2).getValueRead()[2]+">) "+
                                    "(this warning will be printed only once, "+
                                    "but there might be more errors).");
        }
        normgenerrors_face++;
      }
      
      pimpl.normalArray.append(new SbVec3f(tmpvec));
      cind = cind.plus(4); // goto next triangle/polygon
    }
    else { // more than 3 vertices
      // use Newell's method to calculate normal vector
      SbVec3fArray vert1, vert2;
      tmpvec.setValue(0.0f, 0.0f, 0.0f);
      vert2 = coords.plus(v0);
      cind.plusPlus(); // v0 is already read

      // The cind < endptr check makes us robust with regard to a
      // missing "-1" termination of the coordIndex field of the
      // IndexedShape nodetype.
      while (cind.lessThan(endptr) && cind.get() >= 0 && cind.get() <= maxcoordidx) {
        vert1 = SbVec3fArray.copyOf(vert2);
        vert2 = coords.plus(cind.starPlusPlus());
        tmpvec.getValue()[0] += ((vert1.get(0)).getValueRead()[1] - (vert2.get(0)).getValueRead()[1]) * ((vert1.get(0)).getValueRead()[2] + (vert2.get(0)).getValueRead()[2]);
        tmpvec.getValue()[1] += ((vert1.get(0)).getValueRead()[2] - (vert2.get(0)).getValueRead()[2]) * ((vert1.get(0)).getValueRead()[0] + (vert2.get(0)).getValueRead()[0]);
        tmpvec.getValue()[2] += ((vert1.get(0)).getValueRead()[0] - (vert2.get(0)).getValueRead()[0]) * ((vert1.get(0)).getValueRead()[1] + (vert2.get(0)).getValueRead()[1]);
      }

      vert1 = SbVec3fArray.copyOf(vert2);  // last edge (back to v0)
      vert2 = coords.plus(v0);
      tmpvec.getValue()[0] += ((vert1.get(0)).getValueRead()[1] - (vert2.get(0)).getValueRead()[1]) * ((vert1.get(0)).getValueRead()[2] + (vert2.get(0)).getValueRead()[2]);
      tmpvec.getValue()[1] += ((vert1.get(0)).getValueRead()[2] - (vert2.get(0)).getValueRead()[2]) * ((vert1.get(0)).getValueRead()[0] + (vert2.get(0)).getValueRead()[0]);
      tmpvec.getValue()[2] += ((vert1.get(0)).getValueRead()[0] - (vert2.get(0)).getValueRead()[0]) * ((vert1.get(0)).getValueRead()[1] + (vert2.get(0)).getValueRead()[1]);

      // Be robust when it comes to erroneously specified polygons.
      if ((tmpvec.normalize() == 0.0f) && TidBits.coin_debug_extra()!=0) {
        if (normgenerrors_face < 1) {
          SoDebugError.postWarning("SoNormalCache::generatePerFace",
                                    "Erroneous polygon specification in model. "+
                                    "Unable to generate normal; using dummy normal. "+
                                    "(this warning will be printed only once, "+
                                    "but there might be more errors).");
        }
        normgenerrors_face++;
      }

      pimpl.normalArray.append(ccw ? new SbVec3f(tmpvec) : tmpvec.operator_minus());
      cind.plusPlus(); // skip the -1
    }
  }

  if (endptr.minus(cind) > 0) {
//#if COIN_DEBUG
    SoDebugError.postWarning("SoNormalCache::generatePerFace", "Face "+
                              "specification did not end with a valid "+
                              "polygon. Too few points");
//#endif // COIN_DEBUG
    final SbVec3f dummynormal = new SbVec3f();
    dummynormal.setValue(0.0f, 0.0f, 0.0f);
    pimpl.normalArray.append(dummynormal);
  }

  if (pimpl.normalArray.getLength()!=0) {
    pimpl.normalData_normals = SbVec3fArray.fromArray(pimpl.normalArray.getArrayPtr(new SbVec3f[pimpl.normalArray.getLength()]));
    pimpl.numNormals = pimpl.normalArray.getLength();
  }

//#if NORMALCACHE_DEBUG && COIN_DEBUG // debug
  SoDebugError.postInfo("SoNormalCache::generatePerFace",
                         "generated normals per face: "+pimpl.normalData_normals+" "+pimpl.numNormals);
//#endif // debug
}

/*!
  Generates face normals for triangle strips.
*/
static int normgenerrors_facestrip = 0;
public void
generatePerFaceStrip(SbVec3fArray coords,
                                    int numcoords,
                                    IntArrayPtr cind,
                                    int nv,
                                    boolean ccw)
{
	cind = IntArrayPtr.copyOf(cind); // java port : dont modifiy argument
	
//#if NORMALCACHE_DEBUG && COIN_DEBUG
  SoDebugError.postInfo("SoNormalCache::generatePerFaceStrip", "generating normals");
//#endif

  this.clearGenerator();
  pimpl.indices.truncate(0);
  pimpl.normalArray.truncate(0, true);

  IntArrayPtr cstart = new IntArrayPtr(cind);
  IntArrayPtr endptr = cind.plus(nv);

  SbVec3fArray c0, c1, c2; //ptr
  final SbVec3f n = new SbVec3f();

  boolean flip = ccw;

  int maxcoordidx = numcoords - 1;

  while (cind.plus(2).lessThan(endptr)) {
    if (cind.get(0) < 0 || cind.get(1) < 0 || cind.get(2) < 0 ||
        cind.get(0) > maxcoordidx || cind.get(1) > maxcoordidx || cind.get(2) > maxcoordidx) {
//#if COIN_DEBUG
      SoDebugError.postWarning("SoNormalCache::generatePerFaceStrip", "Erroneous "+
                                "coordinate index detected (offset: "+cind.minus(cstart)+", ["+cind.get()+" "+cind.plus(1).get()+" "+cind.plus(2).get()+"]). Should be "+
                                "within [0, "+maxcoordidx+"]."
                                );
//#endif // COIN_DEBUG

      // Insert dummy normal for robustness
      final SbVec3f dummynormal = new SbVec3f();
      dummynormal.setValue(0.0f, 0.0f, 0.0f);
      pimpl.normalArray.append(dummynormal);

      // Skip to next possibly valid index
      if (cind.get(0) < 0 || cind.get(0) > maxcoordidx) {
        cind = cind.plus(1);
      }
      else if (cind.get(1) < 0 || cind.get(1) > maxcoordidx) {
        cind = cind.plus(2);
      }
      else if (cind.plus(3).lessThan(endptr) && (cind.get(2) < 0 || cind.get(2) > maxcoordidx)) {
        cind = cind.plus(3);
      }
      else {
        cind = cind.plus(3); // For robustness check after while loop
        break;
      }

      continue;
    }

    flip = ccw;
    c0 = coords.plus(cind.get()); cind.plusPlus();
    c1 = coords.plus(cind.get()); cind.plusPlus();
    c2 = coords.plus(cind.get()); cind.plusPlus();

    if (!flip)
      n.copyFrom((c0.get(0).operator_minus(c1.get(0))).cross(c2.get(0).operator_minus(c1.get(0))));
    else
      n.copyFrom((c2.get(0).operator_minus(c1.get(0))).cross(c0.get(0).operator_minus(c1.get(0))));

    if ((n.normalize() == 0.0f) && TidBits.coin_debug_extra()!=0) {
      if (normgenerrors_facestrip < 1) {
        SoDebugError.postWarning("SoNormalCache::generatePerFaceStrip",
                                  "Erroneous triangle specification in model "+
                                  "(coords=<"+c0.get(0).getValueRead()[0]+", "+c0.get(0).getValueRead()[1]+", "+c0.get(0).getValueRead()[2]+">, <"+c1.get(0).getValueRead()[0]+", "+c1.get(0).getValueRead()[1]+", "+c1.get(0).getValueRead()[2]+">, <"+c2.get(0).getValueRead()[0]+", "+c2.get(0).getValueRead()[1]+", "+c2.get(0).getValueRead()[2]+">) "+
                                  "(this warning will be printed only once, "+
                                  "but there might be more errors).");



      }
      normgenerrors_facestrip++;
    }
    
    pimpl.normalArray.append(n);

    int idx = cind.lessThan(endptr) ? cind.starPlusPlus() : -1;
    while (idx >= 0 && idx <= maxcoordidx) {
      c0 = SbVec3fArray.copyOf(c1);
      c1 = SbVec3fArray.copyOf(c2);
      c2 = coords.plus(idx);
      flip = !flip;
      if (!flip)
        n.copyFrom((c0.get(0).operator_minus(c1.get(0))).cross(c2.get(0).operator_minus(c1.get(0))));
      else
        n.copyFrom((c2.get(0).operator_minus(c1.get(0))).cross(c0.get(0).operator_minus(c1.get(0))));

      if ((n.normalize() == 0.0f) && TidBits.coin_debug_extra()!=0) {
        if (normgenerrors_facestrip < 1) {
          SoDebugError.postWarning("SoNormalCache::generatePerFaceStrip",
                                    "Erroneous triangle specification in model "+
                                    "(coords=<"+c0.get(0).getValueRead()[0]+", "+c0.get(0).getValueRead()[1]+", "+c0.get(0).getValueRead()[2]+">, <"+c1.get(0).getValueRead()[0]+", "+c1.get(0).getValueRead()[1]+", "+c1.get(0).getValueRead()[2]+">, <"+c2.get(0).getValueRead()[0]+", "+c2.get(0).getValueRead()[1]+", "+c2.get(0).getValueRead()[2]+">) "+
                                    "(this warning will be printed only once, "+
                                    "but there might be more errors).");
        }
        normgenerrors_facestrip++;
      }

      pimpl.normalArray.append(n);
      idx = cind.lessThan(endptr) ? cind.starPlusPlus() : -1;
    }
//#if COIN_DEBUG
    if (idx > maxcoordidx) {
      if (normgenerrors_facestrip < 1) {
        SoDebugError.postWarning("SoNormalCache::generatePerFaceStrip",
                                  "Erroneous polygon specification in model. "+
                                  "Index out of bounds: "+idx+". Max index: "+maxcoordidx+". "+
                                  "(this warning will be printed only once, "+
                                  "but there might be more errors)." 
                                  );
      }
      normgenerrors_facestrip++;
    }
//#endif // COIN_DEBUG
  }

  if (endptr.minus(cind) > 0) {
//#if COIN_DEBUG
    SoDebugError.postWarning("SoNormalCache::generatePerFaceStrip", "Strip "+
                              "did not end with a valid polygon. Too few "+
                              "points");
//#endif // COIN_DEBUG
    final SbVec3f dummynormal = new SbVec3f();
    dummynormal.setValue(0.0f, 0.0f, 0.0f);
    pimpl.normalArray.append(dummynormal);
  }

  if (pimpl.normalArray.getLength()!=0) {
    pimpl.normalData_normals = SbVec3fArray.fromArray(pimpl.normalArray.getArrayPtr(new SbVec3f[pimpl.normalArray.getLength()])); pimpl.normalData_generator = null;
    pimpl.numNormals = pimpl.normalArray.getLength();
  }

//#if NORMALCACHE_DEBUG && COIN_DEBUG
  SoDebugError.postInfo("SoNormalCache::generatePerFaceStrip",
                         "generated tristrip normals per face: "+pimpl.normalData_normals+" "+pimpl.numNormals+""
                         );
//#endif // debug

}


}
