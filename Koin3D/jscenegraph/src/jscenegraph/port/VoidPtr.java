/**
 * 
 */
package jscenegraph.port;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;

/**
 * @author Yves Boyadjian
 *
 */
public class VoidPtr implements Destroyable {
	
	private Object object;
	
	private ByteBuffer buffer; 
	
	private static Map<Object,VoidPtr> ptrs = new IdentityHashMap<>();

	private VoidPtr(Object obj) {
		super();
		object = obj;
	}
	
	/**
	 * Creates a new Void object
	 * @param obj
	 * @return
	 */
	public static VoidPtr create(Object obj) {
		VoidPtr voide = ptrs.get(obj);
		if(voide == null) {
			voide = new VoidPtr(obj);
			ptrs.put(obj, voide);
		}
		return voide;
	}

	@Override
	public void destructor() {
		ptrs.remove(object);
		if(object instanceof Destroyable) {
			Destroyable.delete((Destroyable)object);
		}
	}

	public IntBuffer toIntBuffer() {
		if(object instanceof IntBuffer) {
			return (IntBuffer)object;
		}
		return ((ByteBuffer)toBuffer()).asIntBuffer();
	}

	public Buffer toBuffer() {
		if(buffer!= null) {
			return buffer;
		}
		if(object instanceof Array<?>) {
			buffer = Util.toByteBuffer((Array<?>)object);
		}
		if(object instanceof ByteBuffer) {
			buffer = (ByteBuffer)object;
		}
		if(object instanceof Integer[]) {
			buffer = Util.toByteBuffer((Integer[])object);
		}
		return buffer;
	}
}
