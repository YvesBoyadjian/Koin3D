/**
 * 
 */
package jscenegraph.database.inventor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMachine {

	// For LITTLE_ENDIAN
	public static void DGL_HTON_INT32(byte[] t, byte[] f)     
    {                       
            t[0] = f[3];        
            t[1] = f[2];        
            t[2] = f[1];        
            t[3] = f[0];        
    }
	
	public static int DGL_NTOH_INT32(int f) {
		return Integer.reverseBytes(f);
	}
	
	public static long DGL_NTOH_INT64(long f) {
		return Long.reverseBytes(f);
	}
	
	public static void DGL_HTON_INT32(int[] t, int f) {
		t[0] = Integer.reverseBytes(f);
	}
	
	public static void DGL_HTON_INT32(int[] t, int[] f) {
		t[0] = Integer.reverseBytes(f[0]);
	}
	
	// TODO : verify that it is the good order
	public static void DGL_HTON_INT32(byte[] t, int f) {
		int i = f;
        t[0] = (byte)(i >>> 24);
        t[1] = (byte)((i >> 16) & 0xFF); 
        t[2] = (byte)((i >> 8) & 0xFF);
        t[3] = (byte)((i & 0xFF));		
	}

	// TODO : verify that it is the good order
	public static void DGL_HTON_INT32(byte[] t, int[] f) {
		int i = f[0];
        t[0] = (byte)(i >>> 24);
        t[1] = (byte)((i >> 16) & 0xFF); 
        t[2] = (byte)((i >> 8) & 0xFF);
        t[3] = (byte)((i & 0xFF));		
	}

	public static float DGL_NTOH_FLOAT(byte[] array) {
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).getFloat();
	}
	
	public static double DGL_NTOH_DOUBLE(byte[] array) {
		return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).getDouble();
	}
	
	/**
	 * Helper method java port
	 * @param b
	 * @return
	 */
	public static short toUByte(byte b) {
		short retVal = b;
		if(retVal <0) {
			retVal += 256;
		}
		return retVal;
	}
}
