/**
 * 
 */
package jterrain;

/**
 * @author Yves Boyadjian
 *
 */
public class Utils {

	/** Vpo�t dvojkov�o logaritmu celo�seln�o �sla.
	Vypo�e logaritmus celo�seln�o �sla \p number se z�ladem logaritmu 2.
	Vsledek je op� celo�seln a pedpokl��se, ze �slo \p number je n�terou
	mocninou �sla 2.
	\param number �slo, jeho logaritmus se m�spo�tat.
	\return Vypo�en celo�seln logaritmus se z�ladem 2. */
	public static int ilog2(int number)
	{
	  int tmp = number;
	  int result = 0;
	  while (tmp > 1)
	  {
	    result++;
	    tmp >>= 1;
	  }
	  return result;
	}

}
