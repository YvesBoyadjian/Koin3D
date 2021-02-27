package jscenegraph.coin3d.inventor.lists;

import jscenegraph.port.Destroyable;
import jscenegraph.port.Indexable;

import java.util.function.Supplier;

public class SbListIndexable<T extends Object, I extends Indexable<T>> implements Destroyable {

    private Allocator<I> allocator;

    private Indexable<T> array;

    private int size;

    @Override
    public void destructor() {
        allocator = null;
        array = null;
        size = -1;
    }

    public static interface Allocator<I> {
        I allocate(int size);
    }

    public SbListIndexable(Allocator<I> s,int initsize) {
        allocator = s;
        array = s.allocate(initsize);
        size = 0;
    }

    public void append(T element) {
        int array_size = array.length();
        if( array_size <= size ) {
            grow();
        }
        array.setO(size, element);
        size++;
    }

    public T operator_square_bracket(int indice) {
        return array.getO(indice);
    }

    public int getLength() {
        return size;
    }

    private void grow() {
        int previous_array_size = array.length();
        int new_array_size = previous_array_size * 3/2 + 4;
        I new_array = allocator.allocate(new_array_size);
        new_array.copy(previous_array_size,array);
        array = new_array;
    }
}
