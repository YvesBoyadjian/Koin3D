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
  \class SoVertexArrayIndexer
  \brief The SoVertexArrayIndexer class is used to simplify index handling for vertex array rendering.

  FIXME: more doc. when/if this class is made public, pederb 20050111
*/

package jscenegraph.coin3d.inventor.rendering;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.ShortPtr;
import jscenegraph.port.UShortPtr;
import jscenegraph.port.Util;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexArrayIndexer implements Destroyable {

	  private int target;
	  private SoVertexArrayIndexer next; //ptr

	  private int targetcounter;
	  private final SbListInt countarray = new SbListInt();
	  private final SbList <IntArrayPtr> ciarray = new SbList<>();
	  private final SbListInt indexarray = new SbListInt();
	  private SoVBO vbo;
	  private boolean use_shorts;
	  
	  /*!
	  Constructor
	*/
	public SoVertexArrayIndexer() {
	  target = 0;
	    next = null;
	    vbo = null;
	    use_shorts = true;
	
	}

	/*!
	  Destructor
	*/
	public void destructor()
	{
	  Destroyable.delete( this.vbo);
	  Destroyable.delete( this.next);
	}

	public void 
	addIndex(int i) 
	{
	  if (i >= 65536) {
		  this.use_shorts = false;
	  }
	  this.indexarray.append((int) (i));
	}

	/*!
	  Adds a line to be indexed.
	*/
	public void
	addLine( int v0,
	                               int v1)
	{
	  if (this.target == 0) this.target = GL2.GL_LINES;
	  if (this.target == GL2.GL_LINES) {
	    this.addIndex(v0);
	    this.addIndex(v1);
	  }
	  else {
	    this.getNext().addLine(v0, v1);
	  }
	}

	/*!
	  Adds a point to be indexed.
	*/
	public void
	addPoint( int v0)
	{
	  if (this.target == 0) this.target = GL2.GL_POINTS;
	  if (this.target == GL2.GL_POINTS) {
	    this.addIndex(v0);
	  }
	  else {
	    this.getNext().addPoint(v0);
	  }
	}

	/*!
	  Adds a triangle to be indexed.
	*/
	public void
	addTriangle( int v0,
	                                   int v1,
	                                   int v2)
	{
	  if (this.target == 0) this.target = GL2.GL_TRIANGLES;
	  if (this.target == GL2.GL_TRIANGLES) {
	    this.addIndex(v0);
	    this.addIndex(v1);
	    this.addIndex(v2);
	  }
	  else {
	    this.getNext().addTriangle(v0,v1,v2);
	  }
	}

	/*!
	  Adds a quad to be indexed.
	*/
	public void
	addQuad( int v0,
	                               int v1,
	                               int v2,
	                               int v3)
	{
	  if (this.target == 0) this.target = GL2.GL_QUADS;
	  if (this.target == GL2.GL_QUADS) {
	    this.addIndex(v0);
	    this.addIndex(v1);
	    this.addIndex(v2);
	    this.addIndex(v3);
	  }
	  else {
	    this.getNext().addQuad(v0,v1,v2,v3);
	  }
	}

	/*!
	  Sets up indexer for new indices of type \a targetin. Use
	  targetVertex() to add indices, and finish the target by using
	  endTarget().

	  \sa targetVertex()
	  \sa endTarget()
	*/
	public void
	beginTarget(/*GLenum*/int targetin)
	{
	  if (this.target == 0) this.target = targetin;
	  if (this.target == targetin) {
	    this.targetcounter = 0;
	  }
	  else {
	    this.getNext().beginTarget(targetin);
	  }
	}

	/*!
	  Adds an index to the indexer.

	  \sa beginTarget()
	  \sa endTarget()
	*/
	public void
	targetVertex(/*GLenum*/int targetin, int v)
	{
	  assert(this.target != 0);
	  if (this.target == targetin) {
	    this.targetcounter++;
	    this.addIndex(v);
	  }
	  else {
	    this.getNext().targetVertex(targetin, v);
	  }
	}

	/*!
	  Ends the current target.

	  \sa beginTarget()
	  \sa targetVertex()
	*/
	public void
	endTarget(/*GLenum*/int targetin)
	{
	  assert(this.target != 0);
	  if (this.target == targetin) {
	    this.countarray.append((int) this.targetcounter);
	  }
	  else {
	    this.getNext().endTarget(targetin);
	  }
	}

	/*!
	  Closes the indexer. This will reallocate the growable arrays to use as little
	  memory as possible. The indexer will also sort triangles and lines to
	  optimize rendering.
	*/
	public void
	close()
	{
	  this.indexarray.fit();
	  this.countarray.fit();
	  this.ciarray.truncate(0);

	  if (this.target != GL2.GL_TRIANGLES && this.target != GL2.GL_QUADS && this.target != GL2.GL_LINES && this.target != GL2.GL_POINTS) {
	    IntArrayPtr ptr = this.indexarray.getArrayPtr();
	    for (int i = 0; i < this.countarray.getLength(); i++) {
	      this.ciarray.append(ptr);
	      ptr = ptr.plus((int) this.countarray.operator_square_bracket(i));
	    }
	  }
	  if (this.target == GL2.GL_TRIANGLES) {
	    this.sort_triangles();
	  }
	  else if (this.target == GL2.GL_LINES) {
	    this.sort_lines();
	  }
	  // FIXME: sort lines and points
	  if (this.next != null) this.next.close();
	}

	/*!
	  Render all added targets/indices.
	*/
	public void
	render( cc_glglue glue, boolean renderasvbo, int contextid)
	{
	  switch (this.target) {
	  case GL2.GL_TRIANGLES:
	  case GL2.GL_QUADS:
	  case GL2.GL_LINES:
	  case GL2.GL_POINTS:
	    // common case
	    if (renderasvbo) {
	      if (this.vbo == null) {
	        this.vbo = new SoVBO(GL2.GL_ELEMENT_ARRAY_BUFFER);
	        if (this.use_shorts) {
	          UShortPtr dst = (UShortPtr) 
	            (this.vbo.allocBufferData(this.indexarray.getLength()*Short.BYTES).toUShortPtr());
	          IntArrayPtr src = this.indexarray.getArrayPtr();
	          for (int i = 0; i < this.indexarray.getLength(); i++) {
	            dst.set(i, src.get(i));
	          }
	        }
	        else {
	          this.vbo.setBufferData(VoidPtr.create(this.indexarray.getArrayPtr()),
	                                   this.indexarray.getLength()*Integer.BYTES);
	        }
	      }
	      this.vbo.bindBuffer(contextid);
	      SoGL.cc_glglue_glDrawElements(glue,
	                               this.target,
	                               this.indexarray.getLength(),
	                               this.use_shorts ? GL2.GL_UNSIGNED_SHORT : GL2.GL_UNSIGNED_INT, null);
	      SoGL.cc_glglue_glBindBuffer(glue, GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
	    }
	    else {
	      IntArrayPtr idxptr = this.indexarray.getArrayPtr();
	      SoGL.cc_glglue_glDrawElements(glue,
	                               this.target,
	                               this.indexarray.getLength(),
	                               GL2.GL_UNSIGNED_INT,
	                               VoidPtr.create(idxptr));
	    }
	    break;
	  default:
	    if (SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_MULTIDRAW_ELEMENTS)) {
	      SoGL.cc_glglue_glMultiDrawElements(glue,
	                                    this.target,
	                                    (IntArrayPtr) this.countarray.getArrayPtr(),
	                                    GL2.GL_UNSIGNED_INT,
	                                    VoidPtr.cast(this.ciarray.getArrayPtr(new IntArrayPtr[ciarray.getLength()])),
	                                    this.countarray.getLength());
	    }
	    else {
	      for (int i = 0; i < this.countarray.getLength(); i++) {
	        IntArrayPtr ptr = this.ciarray.operator_square_bracket(i);
	        int cnt = this.countarray.operator_square_bracket(i);
	        SoGL.cc_glglue_glDrawElements(glue,
	                                 this.target,
	                                 cnt,
	                                 GL2.GL_UNSIGNED_INT,
	                                 VoidPtr.create(ptr));
	      }
	    }
	    break;
	  }

	  if (this.next != null) this.next.render(glue, renderasvbo, contextid);
	}

	/*!
	  Returns the total number of vertex indices added to the indexer.
	*/
	public int
	getNumVertices()
	{
	  int count = this.indexarray.getLength();
	  if (this.next != null) count += this.next.getNumVertices();
	  return count;
	}

	//
	//  Returns the next indexer. If more than one target type is added to
	//  an indexer, the indexer will automatically create a new indexer to
	//  store the new target type.
	//
	public SoVertexArrayIndexer
	getNext()
	{
	  if (this.next == null) {
	    this.next = new SoVertexArrayIndexer();
	  }
	  return this.next;
	}

	// sort an array of three integers
	static void sort3(int[] arr)
	{
	  // simple bubble-sort
	  int tmp;
	  if (arr[1] < arr[0]) {
	    tmp = arr[0];
	    arr[0] = arr[1];
	    arr[1] = tmp;
	  }
	  if (arr[2] < arr[1]) {
	    tmp = arr[1];
	    arr[1] = arr[2];
	    arr[2] = tmp;
	  }
	  if (arr[1] < arr[0]) {
	    tmp = arr[0];
	    arr[0] = arr[1];
	    arr[1] = tmp;
	  }
	}

	// sort an array of two integers
	static void sort2(int[] arr)
	{
	  int tmp;
	  if (arr[1] < arr[0]) {
	    tmp = arr[0];
	    arr[0] = arr[1];
	    arr[1] = tmp;
	  }
	}

	// qsort callback used for sorting triangles based on vertex indices
	//extern "C" {
	static int
	compare_triangle(Object v0, Object v1)
	{
	  int i;
	  int[] t0 = (int[]) v0;
	  int[] t1 = (int[]) v1;

	  int[] ti0 = new int[3];
	  int[] ti1 = new int[3];
	  for (i = 0; i < 3; i++) {
	    ti0[i] = t0[i];
	    ti1[i] = t1[i];
	  }
	  sort3(ti0);
	  sort3(ti1);

	  for (i = 0; i < 3; i++) {
	    int diff = ti0[i] - ti1[i];
	    if (diff != 0) return diff;
	  }
	  return 0;
	}
	//}

	// qsort callback used for sorting lines based on vertex indices
//	extern "C" {
	static int
	compare_line(Object v0, Object v1)
	{
	  int i;
	  int[] t0 = (int[]) v0;
	  int[] t1 = (int[]) v1;

	  int[] ti0 = new int[2];
	  int[] ti1 = new int[2];
	  for (i = 0; i < 2; i++) {
	    ti0[i] = t0[i];
	    ti1[i] = t1[i];
	  }
	  sort2(ti0);
	  sort2(ti1);

	  for (i = 0; i < 2; i++) {
	    int diff = ti0[i] - ti1[i];
	    if (diff != 0) return diff;
	  }
	  return 0;
	}
	//}

	//
	// sort triangles to optimize rendering
	//
	public void
	sort_triangles()
	{
	  // sort triangles based on vertex indices to get more hits in the
	  // GPU vertex cache. Not the optimal solution, but should work
	  // pretty well. Example: bunny.iv (~70000 triangles) went from 238
	  // fps with no sorting to 380 fps with sorting.
	  if (this.indexarray.getLength()!=0) {
	    Util.qsort((Object) this.indexarray.getArrayPtr(),
	          this.indexarray.getLength() / 3,
	          Integer.BYTES * 3,
	          SoVertexArrayIndexer::compare_triangle);
	  }
	}

	//
	// sort lines to optimize rendering
	//
	public void
	sort_lines()
	{
	  // sort lines based on vertex indices to get more hits in the
	  // GPU vertex cache.
	  if (this.indexarray.getLength()!=0) {
	    Util.qsort((Object) this.indexarray.getArrayPtr(),
	          this.indexarray.getLength() / 2,
	          Integer.BYTES * 2,
	          SoVertexArrayIndexer::compare_line);
	  }

	}

	/*!
	  Returns the number of indices in the indexer.
	*/
	public int
	getNumIndices()
	{
	  return this.indexarray.getLength();

	}

	/*!
	  Returns a pointer to the index array.
	*/
	public IntArrayPtr
	getIndices()
	{
	  return this.indexarray.getArrayPtr();
	}

	/*!
	  Returns a pointer to the index array. It's allowed to reorganize
	  these indices to change the rendering order. Calling this function
	  will invalidate any VBO caches used by the indexer.
	*/
	public IntArrayPtr
	getWriteableIndices()
	{
	  Destroyable.delete(this.vbo);
	  this.vbo = null;
	  return (IntArrayPtr) this.indexarray.getArrayPtr();
	}
	  
}
