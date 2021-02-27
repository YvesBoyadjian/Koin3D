/**
 * 
 */
package jscenegraph.coin3d.misc;

import java.nio.ByteOrder;

/**
 * @author Yves Boyadjian
 *
 */
public class Tidbits {


public enum CoinEndiannessValues {
  COIN_HOST_IS_UNKNOWNENDIAN( -1),
  COIN_HOST_IS_LITTLEENDIAN( 0),
  COIN_HOST_IS_BIGENDIAN( 1);
	
	private int value;
	
	CoinEndiannessValues(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
};

	private static CoinEndiannessValues coin_endianness = CoinEndiannessValues.COIN_HOST_IS_UNKNOWNENDIAN;

	/*
	  Returns nearest upward number for x that is a power of two, or x
	  itself if it is already a power of two.

	  ("geq" is short for "Greater or EQual".)
	 */
	public static int
	coin_geq_power_of_two(int x)
	{
	  if (coin_is_power_of_two(x)) return x;
	  return coin_next_power_of_two(x);
	}

	/* Quick check to see if an integer value is equal to 2^n, for any
	   n=[0, ...]. */
	public static boolean
	coin_is_power_of_two(int x)
	{
	  return (x != 0) && ((x & (x - 1)) == 0);
	}

	/*
	  Returns nearest upward number for x that is a power of two.

	  Note that if "x" is already a power of two, we will still return the
	  *next* number that is a power of two, and not x itself.
	 */
	public static int
	coin_next_power_of_two(int x)
	{
	  assert((x < (int)(1 << 31)));// && "overflow");

	  x |= (x >> 1);
	  x |= (x >> 2);
	  x |= (x >> 4);
	  x |= (x >> 8);
	  x |= (x >> 16);
	  /* This will make it work with 64-bit numbers: */
	  /* x |= (x >> 32); */

	  return x + 1;
	}

	public static CoinEndiannessValues coin_host_get_endianness() {
		
		  if (coin_endianness != CoinEndiannessValues.COIN_HOST_IS_UNKNOWNENDIAN) {
			    return coin_endianness;
			  }

		if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
			  return coin_endianness = CoinEndiannessValues.COIN_HOST_IS_BIGENDIAN;
			} else {
			  return coin_endianness = CoinEndiannessValues.COIN_HOST_IS_LITTLEENDIAN;
			}	}

/* We provide our own version of the isspace() method, as we don't
   really want it to be locale-dependent (which is known to have
   caused trouble for us with some specific German characters under
   Microsoft Windows, at least). */
public static boolean
coin_isspace( char c)
{
  /* This is what isspace() does under the POSIX and C locales,
     according to the GNU libc man page. */
  return (c==' ') || (c=='\n') || (c=='\t') ||
         (c=='\r') || (c=='\f') /*|| (c=='\v')*/;
}

}
