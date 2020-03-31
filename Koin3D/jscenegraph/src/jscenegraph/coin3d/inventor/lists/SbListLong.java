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
public class SbListLong implements Destroyable, Mutable {

	private static final int DEFAULTSIZE = 4;

	  private int itembuffersize;
	  private int numitems;
	  
	  private long[] itembuffer;
	  private final long[] builtinbuffer = new long[DEFAULTSIZE];
	  
	  private IntArrayPtr internalIntArrayPtr;
	  
	/**
	 * 
	 */
	public SbListLong() {
		super();
		itembuffersize = DEFAULTSIZE;
		itembuffer = builtinbuffer;
	}
	
	public SbListLong(int sizehint) {
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

//	public IntArrayPtr getArrayPtr() {
//		return getArrayPtr(0);
//	}
//		public IntArrayPtr getArrayPtr(int start) {
//			if(internalIntArrayPtr == null) {
//				internalIntArrayPtr = new IntArrayPtr(0,itembuffer);
//			}
//		return new IntArrayPtr(start,internalIntArrayPtr);
//	}

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
		      long[] newitembuffer = this.builtinbuffer;
		      boolean copyDone = false;
		      if (items > DEFAULTSIZE) {
		    	  //newitembuffer = new int[items];
		    	  newitembuffer = Arrays.copyOf(this.itembuffer, items);
		    	  copyDone = true;
		      }

		      if (newitembuffer != this.itembuffer && !copyDone) {
		        for (int i = 0; i < items; i++) newitembuffer[i] = this.itembuffer[i];
		      }

		      // if (this.itembuffer != this.builtinbuffer) delete[] this.itembuffer; java magic !
		      this.itembuffer = newitembuffer;
		      this.itembuffersize = items > DEFAULTSIZE ? items : DEFAULTSIZE;
			  internalIntArrayPtr = null; // invalidate internal IntArrayPtr
		    }
		  }

	public void append(long item) {
	    if (this.numitems == this.itembuffersize) this.grow();
	    this.itembuffer[this.numitems++] = item;
	    internalIntArrayPtr = null; // invalidate internal IntArrayPtr
	}

	private void grow() {
		grow(-1);
	}
	private void grow(int size) {
	    // Default behavior is to double array size.
	    if (size == -1) this.itembuffersize <<= 1;
	    else if (size <= this.itembuffersize) return;
	    else { this.itembuffersize = size; }

	    //int[] newbuffer = new int[this.itembuffersize];
	    long[] newbuffer = Arrays.copyOf(this.itembuffer,this.itembuffersize);
	    int n = this.numitems;
	    //for (int i = 0; i < n; i++) newbuffer[i] = this.itembuffer[i];
	    // if (this.itembuffer != this.builtinbuffer) delete[] this.itembuffer; java magic !
	    this.itembuffer = newbuffer;
	    internalIntArrayPtr = null; // invalidate internal IntArrayPtr
	}

	public void truncate(int length, boolean b) {
		truncate(length, b ? 1 :0);
	}

	public long operator_square_bracket(int i) {
		if(i<numitems)
			return itembuffer[i];
		throw new IllegalArgumentException();
	}

	@Override
	public void destructor() {
		itembuffer = null;
		Destroyable.delete(internalIntArrayPtr);
		internalIntArrayPtr = null;
	}

    //! Assignment operator; copies list into this list.
	public SbListLong operator_copy(final SbListLong pl)   { copy(pl); return this; }
 	
    //! Assignment operator; copies list into this list.
    public SbListLong operator_equal(final SbListLong pl)   { copy(pl); return this; }
    
    private void setSize(int size) {
    	grow(size);
    	numitems = size;
    }
	  
	 //
	   // Copies a pointer list
	   //
	   
	  public void
	   copy(final SbListLong pl)
	   {
	       int i;
	   
	       setSize(pl.numitems);
	   
	       for (i = 0; i < numitems; i++)
	    	   itembuffer[i] = pl.itembuffer[i];
	   }

		// Inserts given pointer in list before pointer with given index. 
		public void insert(long ptr, int addBefore) {
			
		     int i;
		      
		          // If addBefore is off the end of the list, grow the list (and
		          // initialize any new elements to NULL)
		          if (addBefore > numitems) grow(addBefore);
		      
		          // Make room for one more
		          setSize(numitems + 1);
		      
		          // Move pointers forward to make room
		          for (i = numitems - 1; i > addBefore; --i)
		        	  itembuffer[i] = itembuffer[i - 1];
		      
		          // Insert the new one
		          itembuffer[addBefore] = ptr;
		     	}
		
		@Override
		public void copyFrom(Object other) {
			SbListLong otherList = (SbListLong)other;
			copy(otherList);
		}
		
}
