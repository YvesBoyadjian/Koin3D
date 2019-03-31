/**
 * 
 */
package jscenegraph.port;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;

/**
 * @author Yves Boyadjian
 *
 */
public class VoidPtr implements Destroyable {
	
	private Object object;
	
	private Buffer buffer; 
	
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
		else if(object instanceof IntArrayPtr) {
			IntArrayPtr intArrayPtr = (IntArrayPtr)object;
			int[] array = intArrayPtr.getValues();
			int offset = intArrayPtr.getStart();
			int length = intArrayPtr.size();
			return IntBuffer.wrap(array, offset, length);
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
		if(object instanceof FloatArray) {
			buffer = Util.toByteBuffer((FloatArray)object);
		}
		if(object instanceof IntArrayPtr) {
			buffer = Util.toByteBuffer((IntArrayPtr)object);
		}
		if(object instanceof ByteBuffer) {
			buffer = (ByteBuffer)object;
		}
		if(object instanceof Integer[]) {
			buffer = Util.toByteBuffer((Integer[])object);
		}
		if(object instanceof IntBuffer) {
			buffer = (IntBuffer)object;
		}
		if(object instanceof SbVec3fArray) {
			buffer = ((SbVec3fArray)object).toFloatBuffer();
		}
		return buffer;
	}

	public boolean isFloatArray() {
		return object instanceof FloatArray;
	}

	public FloatArray toFloatArray() {
		if(object instanceof FloatArray) {
			return (FloatArray)object;
		}
		return null;
	}

	public boolean isIntArray() {
		return object instanceof IntArrayPtr;
	}

	public IntArrayPtr toIntArray() {
		if(object instanceof IntArrayPtr) {
			return (IntArrayPtr)object;
		}
		return null;
	}

	public UShortPtr toUShortPtr() {
		if(object instanceof UShortPtr) {
			return (UShortPtr)object;
		}
		if(object instanceof ByteBuffer) {
			return new UShortPtr((ByteBuffer)object);
		}
		return null;
	}

	public ByteBuffer toByteBuffer() {
		if(object instanceof ByteBuffer) {
			return (ByteBuffer) object;
		}
		else if( object instanceof IntArrayPtr) {			
			return Buffers.newDirectByteBuffer((IntArrayPtr)object);
		}
		return null;
	}

	public static VoidPtr[] cast(IntArrayPtr[] arrayPtr) {
		if(arrayPtr == null) {
			return null;
		}
		int length = arrayPtr.length;
		VoidPtr[] casted = new VoidPtr[length];
		for(int i=0; i<length;i++) {
			casted[i] = VoidPtr.create(arrayPtr[i]);
		}
		return casted;
	}
}
