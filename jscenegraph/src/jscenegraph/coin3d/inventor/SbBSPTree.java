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

package jscenegraph.coin3d.inventor;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListIndexable;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbBSPTree implements Destroyable {

	static class coin_bspnode implements Destroyable {

		  enum Dimension {
			    // do not change these values!
			    DIM_YZ, // =0
			    DIM_XZ, // =1
			    DIM_XY, // =2
			    DIM_NONE
			  };

		  private coin_bspnode left; // ptr
		  private coin_bspnode right; // ptr
		  private int dimension;   // which dimension?
		  private double position;  // position in dimension (double to avoid floating point precision problems)
		  private final SbListInt indices = new SbListInt(4);
		  private SbListIndexable<SbVec3f, SbVec3fArray> pointsArray; //ptr

		public coin_bspnode(SbListIndexable<SbVec3f, SbVec3fArray> ptsarray) {
		  //indices = new SbListInt(4);
		
		  this.left = this.right = null;
		  this.pointsArray = ptsarray;
		  this.dimension = Dimension.DIM_NONE.ordinal();
		}

		@Override
		public void destructor() {
			indices.destructor();
			  Destroyable.delete( left);
			  Destroyable.delete( right);
		}
		
		public boolean
		leftOf(final SbVec3f pt) 
		{
		  return (double)(pt.getValueRead()[this.dimension]) < this.position;
		}

		public boolean
		leftOf(final SbVec3fSingle pt)
		{
			return (double)(pt.getValue()[this.dimension]) < this.position;
		}

public int addPoint(final SbVec3f pt, final int maxpts)
{
  if (this.left != null) { // node has been split
    if (this.leftOf(pt)) return this.left.addPoint(pt, maxpts);
    else return this.right.addPoint(pt, maxpts);
  }
  else if (this.indices.getLength() >= maxpts) {
    split();
    return this.addPoint(pt, maxpts);
  }
  else {
    int n = this.indices.getLength();
    int i;
    final SbVec3fSingle tmp = new SbVec3fSingle();
    for (i = 0; i < n; i++) {
      tmp.copyFrom((pointsArray).operator_square_bracket(this.indices.operator_square_bracket(i)));
      if (pt.operator_equal_equal(tmp)) break;
    }
    if (i == n) {
      int idx = this.pointsArray.getLength();
      this.pointsArray.append(new SbVec3f(pt));
      this.indices.append(idx);
      return idx;
    }
    return this.indices.operator_square_bracket(i);
  }
}

public void
split()
{
  assert(this.left == null && this.right == null);
  this.left = new coin_bspnode(this.pointsArray);
  this.right = new coin_bspnode(this.pointsArray);

  final SbBox3f box = new SbBox3f();
  int i, n = this.indices.getLength();
  for (i = 0; i < n; i++) {
    box.extendBy((pointsArray).operator_square_bracket(this.indices.operator_square_bracket(i)));
  }
  final SbVec3f diag = box.getMax().operator_minus( box.getMin());
  int dim;
  double pos;

  if (diag.getValueRead()[0] > diag.getValueRead()[1]) {
    if (diag.getValueRead()[0] > diag.getValueRead()[2]) dim = Dimension.DIM_YZ.ordinal();
    else dim = Dimension.DIM_XY.ordinal();
  }
  else {
    if (diag.getValueRead()[1] > diag.getValueRead()[2]) dim = Dimension.DIM_XZ.ordinal();
    else dim = Dimension.DIM_XY.ordinal();
  }

  this.dimension = dim; // set the dimension

  float mid = (box.getMin().operator_square_bracket(dim) + box.getMax().operator_square_bracket(dim)) / 2.0f;
//#ifdef BSP_SORTED_SPLIT
//  this.sort(); // sort vertices on ascending dimension values
//
//  int splitidx = n / 2;
//  pos = ((*pointsArray)[this.indices[splitidx-1]][dim]+
//    (*pointsArray)[this.indices[splitidx]][dim]) / 2.0f;
//
//  // got to check and adjust for special cases
//  if (pos == box.getMin()[dim] || pos == box.getMax()[dim]) {
//    pos = (pos + mid) / 2.0f;
//  }
//
//#else
  pos = ((double)(box.getMin().getValueRead()[this.dimension])+(double)(box.getMax().getValueRead()[this.dimension])) / 2.0;
//#endif // BSP_SORTED_SPLIT

  this.position = pos;

  for (i = 0; i < n; i++) {
    int idx = this.indices.operator_square_bracket(i);
    if (this.leftOf((pointsArray).operator_square_bracket(idx)))
      this.left.indices.append(idx);
    else
      this.right.indices.append(idx);
  }

//   fprintf(stderr,"bsp split: %.3f %.3f %.3f, %.3f %.3f %.3f "
//        "==> (%d %d) %d %.3f\n",
//        box.min[0], box.min[1], box.min[2],
//        box.max[0], box.max[1], box.max[2],
//        this.left->indices.getLength(), this.right->indices.getLength(),
//        this.dimension, this.position);

//   for (i = 0; i < n; i++) {
//     SbVec3f p;
//     this.pointsArray->getElem(this.indices[i], p);
//     fprintf(stderr, "pt %d: %.3f %.3f %.3f\n", i, p[0], p[1], p[2]);
//   }


//#if COIN_DEBUG
  if (this.left.indices.getLength() == 0 ||
      this.right.indices.getLength() == 0) {
    System.err.print("Left:\n");
    n = this.indices.getLength();
    //final SbVec3f[] pts = this.pointsArray.getArrayPtr(new SbVec3f[pointsArray.getLength()]);
    for (i = 0; i < n; i++) {
      SbVec3f vec = pointsArray.operator_square_bracket(this.indices.operator_square_bracket(i));
      System.err.print("pt: "+vec.getValueRead()[0]+" "+vec.getValueRead()[1]+" "+vec.getValueRead()[2]+"\n");
    }
    System.err.print("pos: "+pos+"\n"
            );
    System.err.print("mid: "+mid+"\n");
    System.err.printf("dim: "+dim+"\n");
    System.err.printf("diag: "+diag.getValueRead()[0]+" "+diag.getValueRead()[1]+" "+diag.getValueRead()[2]+"\n");

//#ifdef BSP_SORTED_SPLIT
//    fprintf(stderr,"splitidx: %d\n", splitidx);
//#endif
  }

//#endif
  assert(this.left.indices.getLength() != 0 && this.right.indices.getLength() != 0);


  // will never be used anymore
  this.indices.truncate(0, true);
}

public void
findPoints(final SbSphere sphere, final SbListInt array)
{
  if (this.left != null) {
    final SbVec3fSingle min = new SbVec3fSingle(), max = new SbVec3fSingle();
    max.copyFrom(sphere.getCenter());
    min.copyFrom(sphere.getCenter());
    min.getValue()[this.dimension] -= sphere.getRadius();
    max.getValue()[this.dimension] += sphere.getRadius();

    if (this.leftOf(min)) this.left.findPoints(sphere, array);
    if (!this.leftOf(max)) this.right.findPoints(sphere, array);
  }
  else {
    int i, n = this.indices.getLength();
    for (i = 0; i < n; i++) {
      SbVec3f pt = (pointsArray).operator_square_bracket(this.indices.operator_square_bracket(i));
      if (sphere.pointInside(pt))
      	array.append(this.indices.operator_square_bracket(i));
    }
  }
}

	}
	
	private SbListIndexable<SbVec3f, SbVec3fArray> pointsArray;// = new SbList<>();
	private SbList<Object> userdataArray;// = new SbList<>();
	private coin_bspnode topnode; //ptr
	private int maxnodepoints;
	private final SbBox3f boundingBox = new SbBox3f();
	
	public SbBSPTree() {
		this(64);
	}
	
	public SbBSPTree(int maxnodepts) {
		this(maxnodepts, 4);
	}
	
	public SbBSPTree(int maxnodepts, int initsize) {
		  pointsArray = new SbListIndexable((size)-> { return new SbVec3fArray(FloatMemoryBuffer.allocateFloats(size*3)); },initsize);
		  userdataArray = new SbList<>(initsize);
		  
		  this.topnode = new coin_bspnode(this.pointsArray);
		  this.maxnodepoints = maxnodepts;
	}
	
	public void destructor() {
		Destroyable.delete(this.topnode);
		pointsArray.destructor();
		userdataArray.destructor();
	}

/*!
  Returns the user data for the point at index \a idx.
  \sa SbBSPTree::addPoint()
  \sa SbBSPTree::numPoints()
*/
	public Object
	getUserData(int idx)
	{
		assert(idx < this.userdataArray.getLength());
		return this.userdataArray.operator_square_bracket(idx);
	}

	/*!
	  Adds a new point \a pt to the BSP tree, and returns the index to
	  the new point. The user data for that point will be set to \a data.

	  If the point already exists in the BSP tree, the index to the
	  old point will be returned. The user data for that point will
	  not be changed.

	  \sa SbBSPTree::findPoint()
	*/
	public int
	addPoint(final SbVec3f pt, Object data)
	{
	  this.boundingBox.extendBy(pt);
	  int ret = this.topnode.addPoint(pt, this.maxnodepoints);
	  if (ret == this.userdataArray.getLength()) {
	    this.userdataArray.append(data);
	  }
	  return ret;
	}

	/*!
	  \overload
	*/
	public int
	findClosest(final SbVec3f pos)
	{
	  int n = this.pointsArray.getLength();
	  if (n < 32) { // points are very scattered when few are inserted
	    final SbVec3f tmp = new SbVec3f();
	    int smallidx = -1;
	    float smalldist = Float.MAX_VALUE;//FLT_MAX;
	    for (int i = 0; i < n; i++) {
	      tmp.copyFrom(this.pointsArray.operator_square_bracket(i));
	      float dist = (tmp.operator_minus(pos)).sqrLength();
	      if (dist < smalldist) {
	        smalldist = dist;
	        smallidx = i;
	      }
	    }
	    return smallidx;
	  }
	  final SbVec3f center =
	    (this.boundingBox.getMin().operator_add(
	     this.boundingBox.getMax())).operator_mul(0.5f);
	  center.operator_minus_equal(pos);

	  float siz = center.length() * 2 +
	    (this.boundingBox.getMax().operator_minus(this.boundingBox.getMin())).length();

	  float currsize = siz / 65536.0f;  // max 16 iterations (too much?).

	  final SbSphere sphere = new SbSphere(pos, currsize);
	  final SbListInt tmparray = new SbListInt(); // use only one array to avoid reallocs
	  int idx = -1;

	  // double size of sphere until a vertex is found
	  while (currsize < siz) {
	    sphere.setRadius(currsize);
	    tmparray.truncate(0);
	    idx = this.findClosest(sphere, tmparray);
	    if (idx >= 0) return idx;
	    currsize *= 2;
	  }
	  assert(false);
	  return -1; // this should not happen!
	}

	/*!
	  WARNING: Please don't use this function. It can cause hard to find
	  bugs on the Windows platform if your application is linked against a
	  different CRT than your Coin DLL.

	  Use int findClosest(const SbSphere &sphere, SbIntList & arr)
	  instead.
	*/
	public int
	findClosest(final SbSphere sphere,
	                       final SbListInt arr)
	{
	  this.findPoints(sphere, arr);
	  final SbVec3f pos = new SbVec3f(sphere.getCenter());
	  int n = arr.getLength();
	  int closeidx = -1;
	  float closedist = Float.MAX_VALUE;
	  for (int i = 0; i < n; i++) {
	    int idx = arr.operator_square_bracket(i);
	    float tmp = (pos.operator_minus(this.pointsArray.operator_square_bracket(idx))).sqrLength();
	    if (tmp < closedist) {
	      closeidx = idx;
	      closedist = tmp;
	    }
	  }
	  return closeidx;
	}

	/*!
	  WARNING: Please don't use this function. It can cause hard to find
	  bugs on the Windows platform if your application is linked against a
	  different CRT than your Coin DLL.

	  Use void findPoints(const SbSphere &sphere, SbIntList & array)
	  instead.
	*/
	public void
	findPoints(final SbSphere sphere,
	                      SbListInt array)
	{
	  this.topnode.findPoints(sphere, array);
	}

	
}
