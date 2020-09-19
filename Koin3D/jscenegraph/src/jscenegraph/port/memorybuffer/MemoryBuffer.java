/**
 * A class representing a zone of memory
 */
package jscenegraph.port.memorybuffer;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import jscenegraph.port.ByteBufferAble;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class MemoryBuffer implements Destroyable, ByteBufferAble {
	
	protected ByteBuffer byteBuffer; 
	protected boolean malloc;
	
	protected MemoryBuffer() {
		
	}
	
	protected void updateByteBuffer() {		
	}

	public int numBytes() {
		return byteBuffer.capacity();
	}
	
	public static final MemoryBuffer allocateBytes(int numBytes) {
		
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		memoryBuffer.byteBuffer = BufferUtils.createByteBuffer(numBytes);

		return memoryBuffer;
	}

	public static final MemoryBuffer allocateBytesMalloc(int numBytes) {
		
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		memoryBuffer.byteBuffer = MemoryUtil.memAlloc(numBytes);
		memoryBuffer.malloc = true;

		return memoryBuffer;
	}

	public static final MemoryBuffer allocateBytes(int numBytes, byte value) {
		
		MemoryBuffer memoryBuffer = allocateBytes(numBytes);
		
		for( int index = 0; index < numBytes; index++) {
			memoryBuffer.byteBuffer.put(index,value);
		}
		return memoryBuffer;
	}

	/**
	 * Note : array data is copied
	 * @param array
	 * @return
	 */
	public static MemoryBuffer allocateFromByteArray(byte[] array) {
		
		int numBytes = array.length;
		
		MemoryBuffer memoryBuffer = allocateBytes(numBytes);
		memoryBuffer.setBytes(array, numBytes);
		
		return memoryBuffer;
	}

	public void setBytes(MemoryBuffer srcBytes, int numBytes) {
		updateByteBuffer();
		
		for( int index = 0; index < numBytes; index++) {
			byteBuffer.put(index,srcBytes.getByte(index));
		}
	}

	public void setBytes(byte[] srcBytes, int numBytes) {
		updateByteBuffer();
		
		for( int index = 0; index < numBytes; index++) {
			byteBuffer.put(index,srcBytes[index]);
		}
	}

	public byte getByte(int index) {
		updateByteBuffer();
		
		return byteBuffer.get(index);
	}
	
	public short getUnsignedByte(int index) {
		updateByteBuffer();
				
		short value = byteBuffer.get(index);
		if(value < 0) {
			value += 256;
		}
		return value;
	}
	
	public void setByte(int index, byte value) {
		updateByteBuffer();
		
		byteBuffer.put(index, value);
	}

	/**
	 * Position of byte buffer is not guaranteed
	 * @return
	 */
	public ByteBuffer toByteBuffer() {
		updateByteBuffer();
		
		return byteBuffer;
	}

	public void free() {
		if(byteBuffer != null && malloc) {
			MemoryUtil.memFree(byteBuffer);
			byteBuffer = null;
			malloc = false;
		}
	}
	
	public static void free(MemoryBuffer buffer) {
		if(buffer !=null) {
			buffer.free();
		}
	}

	@Override
	public void destructor() {
		free();
	}
	
}
