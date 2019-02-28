/**
 * 
 */
package jscenegraph.coin3d.inventor.lists;

import jscenegraph.port.IntArrayPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SbListInt {
	
	private static final int DEFAULTSIZE = 4;

	  private int itembuffersize;
	  private int numitems;
	  
	  private int[] itembuffer;
	  private final int[] builtinbuffer = new int[DEFAULTSIZE];
	  
	/**
	 * 
	 */
	public SbListInt() {
		super();
		itembuffersize = DEFAULTSIZE;
		itembuffer = builtinbuffer;
	}

	public int getLength() {
		return this.numitems;
	}

	public IntArrayPtr getArrayPtr() {
		return getArrayPtr(0);
	}
		public IntArrayPtr getArrayPtr(int start) {
		return new IntArrayPtr(start,itembuffer);
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
		      if (items > DEFAULTSIZE) newitembuffer = new int[items];

		      if (newitembuffer != this.itembuffer) {
		        for (int i = 0; i < items; i++) newitembuffer[i] = this.itembuffer[i];
		      }

		      // if (this.itembuffer != this.builtinbuffer) delete[] this.itembuffer; java magic !
		      this.itembuffer = newitembuffer;
		      this.itembuffersize = items > DEFAULTSIZE ? items : DEFAULTSIZE;
		    }
		  }

	public void append(int item) {
	    if (this.numitems == this.itembuffersize) this.grow();
	    this.itembuffer[this.numitems++] = item;
	}

	private void grow() {
		grow(-1);
	}
	private void grow(int size) {
	    // Default behavior is to double array size.
	    if (size == -1) this.itembuffersize <<= 1;
	    else if (size <= this.itembuffersize) return;
	    else { this.itembuffersize = size; }

	    int[] newbuffer = new int[this.itembuffersize];
	    int n = this.numitems;
	    for (int i = 0; i < n; i++) newbuffer[i] = this.itembuffer[i];
	    // if (this.itembuffer != this.builtinbuffer) delete[] this.itembuffer; java magic !
	    this.itembuffer = newbuffer;
	}

	public void truncate(int length, boolean b) {
		truncate(length, b ? 1 :0);
	}

	  
}
