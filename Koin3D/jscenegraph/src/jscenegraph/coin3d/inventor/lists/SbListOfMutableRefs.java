/**
 * 
 */
package jscenegraph.coin3d.inventor.lists;

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
}
