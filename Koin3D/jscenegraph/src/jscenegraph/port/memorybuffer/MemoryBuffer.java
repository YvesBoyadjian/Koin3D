/**
 * A class representing a zone of memory
 */
package jscenegraph.port.memorybuffer;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

/**
 * @author Yves Boyadjian
 *
 */
public class MemoryBuffer {
	
	private ByteBuffer byteBuffer; 
	
	private MemoryBuffer() {
		
	}

	public int numBytes() {
		return byteBuffer.capacity();
	}
	
	public static final MemoryBuffer allocateBytes(int numBytes) {
		
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		memoryBuffer.byteBuffer = BufferUtils.createByteBuffer(numBytes);

		return memoryBuffer;
	}

	public static final MemoryBuffer allocateBytes(int numBytes, byte value) {
		
		MemoryBuffer memoryBuffer = allocateBytes(numBytes);
		
		for( int index = 0; index < numBytes; index++) {
			memoryBuffer.byteBuffer.put(index,value);
		}
		return memoryBuffer;
	}

	public static MemoryBuffer allocateFromByteArray(byte[] array) {
		
		int numBytes = array.length;
		
		MemoryBuffer memoryBuffer = allocateBytes(numBytes);
		memoryBuffer.setBytes(array, numBytes);
		
		return memoryBuffer;
	}

	public void setBytes(MemoryBuffer srcBytes, int numBytes) {
		for( int index = 0; index < numBytes; index++) {
			byteBuffer.put(index,srcBytes.getByte(index));
		}
	}

	public void setBytes(byte[] srcBytes, int numBytes) {
		for( int index = 0; index < numBytes; index++) {
			byteBuffer.put(index,srcBytes[index]);
		}
	}

	public byte getByte(int index) {
		return byteBuffer.get(index);
	}
	
	public short getUnsignedByte(int index) {
		
		short value = byteBuffer.get(index);
		if(value < 0) {
			value += 256;
		}
		return value;
	}
	
	public void setByte(int index, byte value) {
		byteBuffer.put(index, value);
	}

	public ByteBuffer toByteBuffer() {
		return byteBuffer;
	}
}
