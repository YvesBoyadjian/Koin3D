/**
 * 
 */
package jscenegraph.port;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jscenegraph.database.inventor.errors.SoError;

/**
 * @author Yves Boyadjian
 *
 */
public class Array<T extends Mutable> {
	
	private Class<T> klass;
	private T[] array;
	private Map<Mutable,Integer> indices;
	private int offset;

	public Array(Class<T>klass, T[] array) {
		this.klass = klass;
		offset = 0;
		this.array = array;
		if(array != null) {
			int nb = array.length;
			for(int i=0;i<nb;i++) {
				try {
					array[i] = klass.getConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					SoError.post(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Constructor with values
	 * @param klass
	 * @param values
	 */
	private Array(T[] values,Class<T>klass) {
		this.klass = klass;
		offset = 0;
		array = values;
	}
	
	public Array(Class<T>klass, int offset, T... values) {
		this.klass = klass;
		this.offset = offset;
		array = values;
	}
	
	public static final <T extends Mutable> Array createWithValues(Class<T>klass, T... values) {
		return new Array<T>(values,klass);
	}
	
	private void checkIndices() {
		if(indices == null) {
			indices = new HashMap<>();
			int nb = array.length;		
			for(int i=0;i<nb;i++) {
				indices.put(array[i], i);
			}
		}
	}

	/**
	 * length of array
	 * @return
	 */
	public int length() {
		return array.length;
	}
	
	/**
	 * size of array
	 * @return
	 */
	public int size() {
		return array.length - offset;
	}

	/**
	 * get ith element
	 * @param i
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(int i) {
		return array[i+offset];
	}

	/**
	 * sets the ith element
	 * @param i
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void set(int i, T value) {
		(array[i+offset]).copyFrom(value);
	}

	public static void destructor(Array<? extends Mutable> array) {
		if(array == null) {
			return;
		}
		int nb = array.length();
		for(int i=0;i<nb;i++) {
			//array.get(i-offset).destructor(); TODO
		}
	}

	public static int minus(Mutable object, Array<? extends Mutable> array) {
		array.checkIndices();
		return array.indices.get(object) - array.offset;
	}
	
	public Class<T> getElementClass() {
		return klass;
	}

	public Array<T> addressOf(int offset) {
		return new Array<T>(klass,offset,array);
	}
}
