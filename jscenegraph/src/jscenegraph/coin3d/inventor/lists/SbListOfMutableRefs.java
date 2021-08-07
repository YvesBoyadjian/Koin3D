/**
 * 
 */
package jscenegraph.coin3d.inventor.lists;

import java.util.Objects;
import java.util.function.Supplier;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbListOfMutableRefs<T extends Mutable> extends SbList<T> {
	
	Supplier<T> supplier;
	
	public SbListOfMutableRefs(Supplier<T> supplier) {
		super();
		this.supplier = supplier;
		for(int i=0;i< DEFAULTSIZE;i++) {
			builtinbuffer[i] = supplier.get();
		}
	}

	  public void copy(final SbList<T> l) {
		    if (this == l) return;
		    final int n = l.numitems;
		    this.expand(n);
		    for (int i = 0; i < n; i++) ((Mutable)this.itembuffer[i]).copyFrom(l.itembuffer[i]);
		  }

	  protected void grow(final int size) {
		    // Default behavior is to double array size.
		    if (size == -1) this.itembuffersize <<= 1;
		    else if (size <= this.itembuffersize) return;
		    else { this.itembuffersize = size; }

		    Object[] newbuffer = new Object[this.itembuffersize];
		    int n = this.numitems;
		    for (int i = 0; i < n; i++) newbuffer[i] = this.itembuffer[i];
		    for (int i = n ; i<this.itembuffersize;i++) {
		    	newbuffer[i] = supplier.get();
		    }
		    //if (this.itembuffer != this.builtinbuffer) delete[] this->itembuffer; java port
		    this.itembuffer = newbuffer;
		  }

	public T operator_square_bracket(int index) {
//		T retVal = supplier.get();
//		retVal.copyFrom((T)this.itembuffer[index]);
//		return retVal;
		return (T)this.itembuffer[index]; // It is the job of the caller to enventually copy the value
	}

	public void insert(T item, int insertbefore) {
//#ifdef COIN_EXTRA_DEBUG
//    assert(insertbefore >= 0 && insertbefore <= this->numitems);
//#endif // COIN_EXTRA_DEBUG
		if (this.numitems == this.itembuffersize) this.grow();

		for (int i = this.numitems; i > insertbefore; i--)
			((Mutable)this.itembuffer[i]).copyFrom(this.itembuffer[i-1]);
		((Mutable)this.itembuffer[insertbefore]).copyFrom(item);
		this.numitems++;
	}

	public void append(T item) {
		if (this.numitems == this.itembuffersize) this.grow();
		((Mutable)this.itembuffer[this.numitems++]).copyFrom(item);
	}

	public void remove(int index) {
		this.numitems--;
		for (int i = index; i < this.numitems; i++)
			((Mutable)this.itembuffer[i]).copyFrom(this.itembuffer[i + 1]);
		((Mutable)this.itembuffer[numitems]).copyFrom(supplier.get()); // java port
	}

	public int find(T item) {
		for (int i = 0; i < this.numitems; i++)
			if (Objects.equals(this.itembuffer[i], item)) return i;
		return -1;
	}

	public void removeFast(int index) {
		this.itembuffer[index] = this.itembuffer[--this.numitems];
		((Mutable)this.itembuffer[numitems]).copyFrom(supplier.get()); // java port
	}

	public void truncate(int length) {
		int dofit = 0;
		for(int i=length;i<numitems;i++) {
			itembuffer[i] = supplier.get();
		}
		this.numitems = length;
		if (dofit!=0) this.fit();
	}
}
