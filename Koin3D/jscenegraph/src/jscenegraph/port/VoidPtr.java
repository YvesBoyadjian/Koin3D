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
import java.util.WeakHashMap;

import com.jogamp.common.nio.Buffers;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class VoidPtr implements Destroyable {
	
	private Object object;
	
	private Buffer buffer; 
	
	//private static Map<Object,VoidPtr> ptrs = new IdentityHashMap<>();

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
		VoidPtr voide/* = ptrs.get(obj)*/;
		//if(voide == null) {
			voide = new VoidPtr(obj);
			//ptrs.put(obj, voide);
			//System.out.println("vp = "+ptrs.size()+" class : "+obj.getClass());
		//}
		return voide;
	}
	
//	public static boolean has(Object obj) {
//		return ptrs.containsKey(obj);
//	}

	@Override
	public void destructor() {
//		if( ptrs.remove(object) == null ) {
//			int ii=0;
//		}
		if(object instanceof Destroyable) {
			Destroyable.delete((Destroyable)object);
		}
		object = null;
		buffer = null;
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
		else if(object instanceof FloatArray) {
			buffer = Util.toByteBuffer((FloatArray)object);
		}
		else if(object instanceof IntArrayPtr) {
			buffer = Util.toByteBuffer((IntArrayPtr)object);
		}
		else if(object instanceof ByteBuffer) {
			buffer = (ByteBuffer)object;
		}
		else if(object instanceof Integer[]) {
			buffer = Util.toByteBuffer((Integer[])object);
		}
		else if(object instanceof IntBuffer) {
			buffer = (IntBuffer)object;
		}
		else if(object instanceof SbVec4fArray) {
			buffer = ((SbVec4fArray)object).toFloatBuffer();
		}
		else if(object instanceof SbVec3fArray) {
			buffer = ((SbVec3fArray)object).toFloatBuffer();
		}
		else if(object instanceof SbVec2fArray) {
			buffer = ((SbVec2fArray)object).toFloatBuffer();
		}
		else if(object instanceof MemoryBuffer) {
			buffer = ((MemoryBuffer)object).toByteBuffer();
		}
		else if(object != null && buffer == null) {
			throw new RuntimeException();
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
		if(object instanceof MemoryBuffer) {
			return new UShortPtr(((MemoryBuffer)object).toByteBuffer());
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
		else if( object instanceof MemoryBuffer) {
			return ((MemoryBuffer)object).toByteBuffer();
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
