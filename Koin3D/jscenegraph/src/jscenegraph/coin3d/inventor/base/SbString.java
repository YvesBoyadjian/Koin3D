/**
 * 
 */
package jscenegraph.coin3d.inventor.base;

import jscenegraph.coin3d.inventor.lists.SbList;

/**
 * @author Yves Boyadjian
 *
 */
public class SbString {

	/*!
	  If \a s is found, the method returns the first index where \a s
	  starts. Otherwise it returns -1.

	  Note: SbString::find() is a Coin specific extension to the original
	  Open Inventor API.

	  \sa SbString::findAll()
	  \since Coin 2.0
	*/
	public static int
	find(String THIS, String strarg)
	{
	  int lenthis = THIS.length();//getLength();
	  int lenstr = strarg.length();//getLength();

	  if (lenthis==0) return -1;
	  if (lenstr > lenthis) return -1;

	  final SbList<Integer> pi = new SbList<>();
	  compute_prefix_function(pi, strarg);
	  int q = 0;

	  for (int i = 0; i < lenthis; i ++){
	    while (q > 0 && (strarg.charAt(q) != THIS.charAt(i)))
	      q = pi.operator_square_bracket(q - 1);
	    if (strarg.charAt(q) == THIS.charAt(i))
	      q++;
	    if (q == lenstr){
	      return (i - (lenstr - 1));
	    }
	  }
	  return -1;
	}

	// Helper function for find() and findAll().
	private static void
	compute_prefix_function(SbList <Integer> pi, String str)
	{
	  int len = str.length();//getLength();
	  pi.append(0);
	  int k = 0;

	  for (int q = 1; q < len; q++){
	    while(k > 0 && (str.charAt(k) != str.charAt(q)))
	      k = pi.operator_square_bracket(k);
	    if (str.charAt(k) == str.charAt(q))
	      k++;
	    pi.append(k);
	  }
	}

}
