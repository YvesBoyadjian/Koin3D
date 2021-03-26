/**
 * 
 */
package jscenegraph.coin3d.inventor.lists;

import java.util.Arrays;

import jscenegraph.database.inventor.SbPList;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbListInt implements Destroyable, Mutable {
	
	private static final int DEFAULTSIZE = 4;

	  private int itembuffersize;
	  private int numitems;
	  
	  private int[] itembuffer;
	  private final int[] builtinbuffer = new int[DEFAULTSIZE];
	  
	  private IntArrayPtr internalIntArrayPtr;
	  
	/**
	 * 
	 */
	public SbListInt() {
		super();
		itembuffersize = DEFAULTSIZE;
		itembuffer = builtinbuffer;
	}
	
	public SbListInt(int sizehint) {
		super();
		itembuffersize = DEFAULTSIZE;
		numitems = 0;
		itembuffer = builtinbuffer; 
		if(sizehint > DEFAULTSIZE) {
			this.grow(sizehint);
		}
	}

	public int getLength() {
		return this.numitems;
	}
	
	public int size() {
		return getLength();
	}

	public IntArrayPtr getArrayPtr() {
		return getArrayPtr(0);
	}
		public IntArrayPtr getArrayPtr(int start) {
			if(internalIntArrayPtr == null) {
				internalIntArrayPtr = new IntArrayPtr(0,itembuffer);
			}
		return new IntArrayPtr(start,internalIntArrayPtr);
	}

	// java port
	public void truncate(int length) {
		truncate(length,0);
	}
	  public void truncate( int length, int dofit) {
		  //#ifdef COIN_EXTRA_DEBUG
		      assert(length <= this.numitems);
		  //#endif // COIN_EXTRA_DEBUG
		      this.numitems = length;
		      if (dofit != 0) this.fit();
		    }

	  public void fit() {
		    final int items = this.numitems;

		    if (items < this.itembuffersize) {
		      int[] newitembuffer = this.builtinbuffer;
		      boolean copyDone = false;
		      if (items > DEFAULTSIZE) {
		    	  newitembuffer = Arrays.copyOf(this.itembuffer, items);
		    	  copyDone = true;
		      }

		      if (newitembuffer != this.itembuffer && !copyDone) {
		        for (int i = 0; i < items; i++) newitembuffer[i] = this.itembuffer[i];
		      }

		      this.itembuffer = newitembuffer;
		      this.itembuffersize = items > DEFAULTSIZE ? items : DEFAULTSIZE;
			  internalIntArrayPtr = null; // invalidate internal IntArrayPtr
		    }
		  }

	public void append(int item) {
	    if (this.numitems == this.itembuffersize) this.grow();
	    this.itembuffer[this.numitems++] = item;
	    internalIntArrayPtr = null; // invalidate internal IntArrayPtr
	}
	
	public void add(int item) {
		append(item);
	}

	private void grow() {
		grow(-1);
	}
	private void grow(int size) {
	    // Default behavior is to double array size.
	    if (size == -1) this.itembuffersize <<= 1;
	    else if (size <= this.itembuffersize) return;
	    else { this.itembuffersize = size; }

	    int[] newbuffer = Arrays.copyOf(this.itembuffer,this.itembuffersize);
	    this.itembuffer = newbuffer;
	    internalIntArrayPtr = null; // invalidate internal IntArrayPtr
	}

	public void truncate(int length, boolean b) {
		truncate(length, b ? 1 :0);
	}

	public int operator_square_bracket(int i) {
		if(i<numitems)
			return itembuffer[i];
		throw new IllegalArgumentException();
	}
	
	public int get(int i) {
		return operator_square_bracket(i);
	}

	@Override
	public void destructor() {
		numitems = -1;
		itembuffersize = -1;
		itembuffer = null;
		Destroyable.delete(internalIntArrayPtr);
		internalIntArrayPtr = null;
	}

	@Override
	public void copyFrom(Object other) {
		SbListInt otherList = (SbListInt)other;
		copy(otherList);
	}

/*!
  Make this list a copy of \a l.
*/
public void copy(final SbListInt l)
{
  if (this == l) return;
  int n = l.numitems;
  this.expand(n);
  for (int i = 0; i < n; i++) this.itembuffer[i] = l.itembuffer[i];
}
	  
		// java port
		public void operator_square_bracket(int i, int object) {
//			 if (i >= size()) grow(i);
			 itembuffer[i] = object; 
		}

protected void expand(int size) {
  this.grow(size);
  this.numitems = size;
}
		
		public  void set(int i, int j) { itembuffer[i] = j; }	

}
