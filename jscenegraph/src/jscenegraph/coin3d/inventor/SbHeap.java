/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

/*!
  \class SbHeap SbHeap.h Inventor/SbHeap.h
  \brief The SbHeap class is a generic heap class.
  \ingroup base

  FIXME: write doc

  Note: SbHeap is an extension versus the Open Inventor API.
*/

/*!
  \struct SbHeapFuncs SbHeap.h Inventor/SbHeap.h
  \brief The SbHeapFuncs struct is used to specify functions on heap elements.
*/

/*!
  \var SbHeapFuncs::eval_func

  The \e eval_func member is a pointer to a function that should
  return a weight-value for a heap element. Smaller elements are
  extracted first from the heap.  */
/*!
  \var SbHeapFuncs::get_index_func

  \e get_index_func is a pointer to a function which should return the
  element's heap index. If you want to remove an element from the heap
  (other than the first element), or change the weight for a heap
  element, you must supply the two index functions. Each element must
  then store its heap index in its own data structures.  */
/*!
  \var SbHeapFuncs::set_index_func

  \e set_index_func is used to set this index value, and will be
  called whenever the element is moved in the heap.  */


package jscenegraph.coin3d.inventor;

import jscenegraph.coin3d.inventor.lists.SbList;

/**
 * @author Yves Boyadjian
 *
 */
public class SbHeap {
	
	public interface traverse_func {
		boolean traverse(Object o1, Object o2);
	}
	
	public interface progress_func {
		boolean progress(float f, Object o);
	}

/*!
  Constructor. \a hFuncs specifies the functions for modifying
  and returning information about the heap object, \a initsize
  specifies the initial number of allocated elements. This array
  will automatically grow when necessary, but if you know
  approximately how many elements the heap will contain, you
  should supply this to avoid some reallocs.
*/
	public SbHeap(SbHeapFuncs SbHeapFuncs) {
		this(SbHeapFuncs, 1024);
	}
	public SbHeap(final SbHeapFuncs hFuncs,
         final int initsize) {
  heap = new SbList(initsize);

  this.funcs = hFuncs;
  assert(funcs.eval_func != null);
  this.heap.append(null);
}

/*!
  Destructor.
*/
	public void destructor()
{
}

/*!
  Removes all the elements from the heap.
*/
public void emptyHeap()
{
  this.heap.truncate(0);
  this.heap.append(null);
}
	
/*!
  Returns the number of elements in the heap.
*/
public int size()
{
  return this.heap.getLength() - 1;
}

	
	
/*!
  Adds an element to the heap. Returns the element's heap position.
*/
public int add(Object obj)
{
  return this.heapInsert(obj);
}

	
	
/*!
  Removes an element from the heap.
*/
public void remove(final int idx)
{
  int hsize = this.heap.getLength()-1;
  if (idx == hsize) {
    this.heap.truncate(hsize);
    return;
  }
  this.heap.operator_square_bracket(idx, this.heap.operator_square_bracket(hsize));
  if (this.funcs.set_index_func != null)
    this.funcs.set_index_func.set_index_func(this.heap.operator_square_bracket(idx), idx);
  this.heap.truncate(hsize);

  this.newWeight(this.heap.operator_square_bracket(idx));
}

	
	
	
/*!
  \overload
*/
public void remove(Object obj)
{
  if (this.funcs.get_index_func != null) {
    this.remove(this.funcs.get_index_func.get_index_func(obj));
  }
  else { // slow!!!
    this.remove(this.heap.find(obj));
  }
}

	
	
/*!
  Returns and removes the first element in the heap, or \a NULL
  if heap is empty.
*/
public Object extractMin()
{
  return this.heapExtractMin();
}

	
	
/*!
  Returns the first element in the heap, or \e NULL if heap is empty.
*/
public Object getMin()
{
  if (this.heap.getLength() > 1)
    return heap.operator_square_bracket(1);
  else
    return null;
}

	
	
/*!
  Returns the heap element at index \a idx in the heap.
*/
public Object operator_square_bracket( int idx)
{
  assert(idx > 0 && idx < heap.getLength());
  return heap.operator_square_bracket(idx);
}

	
/**
 * Java port
 * @param obj
 */
	public void newWeight(Object obj) {
		newWeight(obj, -1);
	}

/*!
  Fixes heap if necessary when the element at \a hpos has changed weight.
  If you know the element's heap position you can supply it in \a hpos.
*/
public void newWeight(Object obj, int hpos)
{
  int hsize = this.heap.getLength()-1;
  if (hpos < 0) {
    if (this.funcs.get_index_func != null)
      hpos = this.funcs.get_index_func.get_index_func(obj);
    else
      hpos = this.heap.find(obj);
  }
  int i = hpos;

  SbHeapFuncs.Eval eval = this.funcs.eval_func;
  SbHeapFuncs.Set_index setindex = this.funcs.set_index_func;

  if (i > 1 && eval.eval_func(obj) < eval.eval_func(heap.operator_square_bracket(i/2))) {
    while (i > 1 && eval.eval_func(this.heap.operator_square_bracket(i/2)) > eval.eval_func(obj)) {
      this.heap.operator_square_bracket(i, this.heap.operator_square_bracket(i/2));
      if (setindex != null) setindex.set_index_func(this.heap.operator_square_bracket(i), i);
      i >>= 1;
    }
    this.heap.operator_square_bracket(i,obj);
    if (setindex != null) setindex.set_index_func(obj, i);
  }
  if ((i<<1) > hsize) return;
  if (eval.eval_func(obj) > eval.eval_func(this.heap.operator_square_bracket(i*2))) this.heapify(i);
  else {
    if ((i<<1)+1 > hsize) return;
    if (eval.eval_func(obj) > eval.eval_func(this.heap.operator_square_bracket(i*2+1))) this.heapify(i);
  }
}

	/**
	 * Java port
	 * @return
	 */
	public boolean buildHeap() {
		return buildHeap(null,null);
	}

/*!
  Builds heap out of randomly ordered data-structure.
*/
public boolean buildHeap(progress_func progresscb, Object data)
{
  boolean ok = true;
  int hsize = this.heap.getLength()-1;

  int nrelems = hsize >> 1;

  for (int i = nrelems; (i >= 1) && ok; i--) {
    this.heapify(i);
    if(progresscb != null && ((i & 31) == 0))
      ok = progresscb.progress((float)(nrelems - i)/(float)(nrelems), data);
  }
  return ok;
}

	
	
	
/*!
  Traverses each heap elements, and calls \a func for each element.
*/
public boolean traverseHeap(traverse_func func, Object userdata)
{
  boolean ok = true;
  int hsize = this.heap.getLength() - 1;

  for(int i = 1; (i <= hsize) && ok; i++)
    if (this.heap.operator_square_bracket(i)!=null) ok = func.traverse(this.heap.operator_square_bracket(i), userdata);

  return ok;
}



	private SbHeapFuncs funcs;
	private SbList <Object> heap;

private int heapInsert(Object obj)
{
  int i;
  int hsize = this.heap.getLength();
  i = hsize;
  SbHeapFuncs.Set_index setindex = this.funcs.set_index_func;
  SbHeapFuncs.Eval eval = this.funcs.eval_func;

  this.heap.append(null); // will be overwritten later

  while (i > 1 && eval.eval_func(this.heap.operator_square_bracket(i>>1)) > eval.eval_func(obj)) {
    this.heap.operator_square_bracket(i, this.heap.operator_square_bracket(i>>1));
    if (setindex != null) setindex.set_index_func(this.heap.operator_square_bracket(i), i);
    i >>= 1;
  }
  this.heap.operator_square_bracket(i, obj);
  if (setindex != null) setindex.set_index_func(obj, i);
  return i;
}

//
// Returns the smallest object, and removes it from the heap
//
private Object heapExtractMin()
{
  int hsize = this.heap.getLength()-1;
  Object min;
  if (hsize < 1) return null;
  min = this.heap.operator_square_bracket(1);
  this.heap.operator_square_bracket(1, this.heap.operator_square_bracket(hsize));
  if (this.funcs.set_index_func != null)
    this.funcs.set_index_func.set_index_func(this.heap.operator_square_bracket(1), 1);
  this.heap.truncate(hsize);
  this.heapify(1);

  return min;
}

	
	
//	private void heapReserve(final int newsize);

//
// Maintain the heap-structure. Both children must be heaps
//
private void heapify(final int idx)
{
  int l, r, smallest;
  Object tmp;
  int hsize = this.heap.getLength()-1;

  SbHeapFuncs.Eval eval = this.funcs.eval_func;
  SbHeapFuncs.Set_index setindex = this.funcs.set_index_func;

  l = 2*idx;
  r = l+1;
  if (l <= hsize && eval.eval_func(heap.operator_square_bracket(l)) < eval.eval_func(heap.operator_square_bracket(idx))) smallest = l;
  else smallest = idx;
  if (r <= hsize && eval.eval_func(heap.operator_square_bracket(r)) < eval.eval_func(heap.operator_square_bracket(smallest))) smallest = r;
  if (smallest != idx) {
    tmp = this.heap.operator_square_bracket(idx);
    this.heap.operator_square_bracket(idx, this.heap.operator_square_bracket(smallest));
    this.heap.operator_square_bracket(smallest, tmp);
    if (setindex != null) setindex.set_index_func(this.heap.operator_square_bracket(idx), idx);
    if (setindex != null) setindex.set_index_func(this.heap.operator_square_bracket(smallest), smallest);
    this.heapify(smallest);
  }
}


}
