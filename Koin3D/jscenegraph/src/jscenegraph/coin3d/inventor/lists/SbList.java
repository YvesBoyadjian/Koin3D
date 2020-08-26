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

package jscenegraph.coin3d.inventor.lists;

import jscenegraph.port.Destroyable;
import jscenegraph.port.Indexable;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
// We usually implement inline functions below the class definition,
// since we think that makes the file more readable. However, this is
// not done for this class, since Microsoft Visual C++ is not too
// happy about having functions declared as inline for a template
// class.

// FIXME: the #pragmas below is just a quick hack to avoid heaps of
// irritating warning messages from the compiler for client code
// compiled under MSVC++. Should try to find the real reason for the
// warnings and fix the cause of the problem instead. 20020730 mortene.
//
// UPDATE 20030617 mortene: there is a Microsoft Knowledge Base
// article at <URL:http://support.microsoft.com> which is related to
// this problem. It's article number KB168958.
//
// In short, the general solution is that classes that exposes usage
// of SbList<type> needs to declare the specific template instance
// with "extern" and __declspec(dllimport/export).
//
// That is a lot of work to change, tho'. Another possibility which
// might be better is to simply avoid using (exposing) SbList from any
// of the other public classes. Judging from a quick look, this seems
// feasible, and just a couple of hours or so of work.
//
public class SbList<T extends Object> implements Mutable, Destroyable { //FIXME

	  // Older compilers aren't too happy about const declarations in the
	  // class definitions, so use the enum trick described by Scott
	  // Meyers in "Effective C++".
	  public static final int DEFAULTSIZE = 4;

  protected int itembuffersize;
  protected int numitems;
  protected Object[] itembuffer;
  protected final Object[] builtinbuffer = new Object[DEFAULTSIZE];
  
	/**
	 * 
	 */
	public SbList() {
		this(DEFAULTSIZE);
	}
	public SbList(int sizehint) {
	    itembuffersize = (DEFAULTSIZE);
	    numitems = (0);
	    itembuffer =(builtinbuffer);
	    if (sizehint > DEFAULTSIZE) 
	    	this.grow(sizehint);
	}

	public int getLength() {
		return this.numitems;
	}

	public T operator_square_bracket(int index) {
		return (T)this.itembuffer[index];
	}

	/**
	 * Java port
	 * @param index
	 * @param object
	 */
	public void operator_square_bracket(int index, T object) {
		this.itembuffer[index] = object;
	}

	public void truncate(int length) {
		int dofit = 0;
		  for(int i=length;i<numitems;i++) {
			  itembuffer[i] = null;
		  }
	    this.numitems = length;
	    if (dofit!=0) this.fit();
	}

  public void fit() {
    int items = this.numitems;

    if (items < this.itembuffersize) {
      Object[]  newitembuffer = this.builtinbuffer;
      if (items > DEFAULTSIZE) newitembuffer = new Object[items];

      if (newitembuffer != this.itembuffer) {
        for (int i = 0; i < items; i++) newitembuffer[i] = this.itembuffer[i];
      }

      //if (this.itembuffer != this.builtinbuffer) delete[] this.itembuffer;
      this.itembuffer = newitembuffer;
      this.itembuffersize = items > DEFAULTSIZE ? items : DEFAULTSIZE;
    }
  }
  
  public void insert(T item, int insertbefore) {
//#ifdef COIN_EXTRA_DEBUG
//    assert(insertbefore >= 0 && insertbefore <= this->numitems);
//#endif // COIN_EXTRA_DEBUG
    if (this.numitems == this.itembuffersize) this.grow();

    for (int i = this.numitems; i > insertbefore; i--)
      this.itembuffer[i] = this.itembuffer[i-1];
    this.itembuffer[insertbefore] = item;
    this.numitems++;
  }



	public void remove(int index) {
	    this.numitems--;
	    for (int i = index; i < this.numitems; i++)
	      this.itembuffer[i] = this.itembuffer[i + 1];
	    this.itembuffer[numitems] = null; // java port
	}

	public int find(T item) {
	    for (int i = 0; i < this.numitems; i++)
	        if (this.itembuffer[i] == item) return i;
	      return -1;
	}

	public void removeFast(int index) {
	    this.itembuffer[index] = this.itembuffer[--this.numitems];
	    this.itembuffer[numitems] = null; // java port
	}

	public void append(T item) {
	    if (this.numitems == this.itembuffersize) this.grow();
	    this.itembuffer[this.numitems++] = item;
	}

  public SbList <T>  operator_assign(final SbList<T> l) {
    this.copy(l);
    return this;
  }

  public void copy(final SbList<T> l) {
    if (this == l) return;
    final int n = l.numitems;
    this.expand(n);
    for (int i = 0; i < n; i++) this.itembuffer[i] = l.itembuffer[i];
  }

    protected void expand( int size) {
    this.grow(size);
    this.numitems = size;
  }

    private void grow() {
    	grow(-1); // java port
    }
protected void grow(final int size) {
    // Default behavior is to double array size.
    if (size == -1) this.itembuffersize <<= 1;
    else if (size <= this.itembuffersize) return;
    else { this.itembuffersize = size; }

    Object[] newbuffer = new Object[this.itembuffersize];
    int n = this.numitems;
    for (int i = 0; i < n; i++) newbuffer[i] = this.itembuffer[i];
    //if (this.itembuffer != this.builtinbuffer) delete[] this->itembuffer; java port
    this.itembuffer = newbuffer;
  }
@Override
public void copyFrom(Object other) {
	final SbList<T> l = (SbList<T>)other;
    this.copy(l);
}
@Override
public void destructor() {
	  itembuffersize = -1;
	  numitems = -1;
	  itembuffer = null;
	  for( int i=0; i< DEFAULTSIZE; i++)
		  builtinbuffer[i] = null;	  
}

public T[] getArrayPtr(T[] arrayWithGoodSize) {
	
	for(int i=0;i<getLength();i++) {
		arrayWithGoodSize[i] = operator_square_bracket(i);
	}
	return arrayWithGoodSize;
}

public Indexable<T> getArrayPtr(Indexable<T> arrayWithGoodSize) {
	
	for(int i=0;i<getLength();i++) {
		arrayWithGoodSize.setO(i, operator_square_bracket(i));
	}
	return arrayWithGoodSize;
}

public void truncate(int length, boolean b) {
	truncate(length, b ? 1 : 0);
}

public void truncate( int length, int dofit /*= 0*/) {
//#ifdef COIN_EXTRA_DEBUG
  assert(length <= this.numitems);
//#endif // COIN_EXTRA_DEBUG
  for(int i=length;i<numitems;i++) {
	  itembuffer[i] = null;
  }
  this.numitems = length;
  if (dofit != 0) this.fit();
}

  public void push(T item) {
    this.append(item);
  }

  public T pop() {
//#ifdef COIN_EXTRA_DEBUG
    assert(this.numitems > 0);
//#endif // COIN_EXTRA_DEBUG
    this.itembuffer[this.numitems] = null; // java port
	--this.numitems;
    return (T)this.itembuffer[this.numitems];
  }

  
}
