/**
 * 
 */
package jscenegraph.port;

import java.util.Objects;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class Indexable<T extends Object> {

	@Override
	public int hashCode() {
		return Objects.hash(delta(), values());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Indexable)) {
			return false;
		}
		Indexable other = (Indexable) obj;
		return delta() == other.delta() && Objects.equals(values(), other.values());
	}

	public abstract T getO(int index);

	public abstract int length();

	public abstract void setO(int i, T object);
	
	public abstract Indexable<T> plus( int delta );
	
	protected abstract int delta();
	
	protected abstract Object values();
}
