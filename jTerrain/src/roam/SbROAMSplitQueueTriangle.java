
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Z�ladn�datov�prvky algoritmu ROAM.
/// \file SbROAMPrimitives.h
/// \author Radek Barto�- xbarto33
/// \date 16.09.2005
///
/// Algoritmus ROAM ve sv�statick��sti vyu��jako z�ladn�datovou
/// strukturu bin�n�strom trojheln�. Pro tyto trojheln�y je zde
/// definov�a t�a ::SbROAMTriangle. V dynamick��sti jsou pak poteba
/// dv�prioritn�fronty. Prvn�z nich je prioritn�fronta trojheln� na
/// rozd�en� Obsahuje trojheln�y t�y ::SbROAMSplitQueueTriangle.
/// Druhou pak prioritn�fronta trojheln� na spojen� Ta obsahuje diamondy
/// (�veice trojheln�) t�y ::SbROAMMergeQueueDiamond.
//////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2006 Radek Barton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
///////////////////////////////////////////////////////////////////////////////

package roam;

/**
 * @author Yves Boyadjian
 *
 */
public class SbROAMSplitQueueTriangle {

	/// Trojheln�y s touto prioritou jsou zobrazeny vdy.
	final static float PRIORITY_MAX = 1e38f;
	/// Trojheln�y s touto prioritou se nezobrazuj�(teoreticky).
	final static float PRIORITY_MIN = 0.0f;

  
    /* Metody. */
    /** Konstruktor.
    Vytvo�instanci t�y trojheln�u \p triangle pro prioritn�frontu
    trojheln� na rozd�en�a inicializuje jeho ukazatele na sousedy
    \p left, \p right a \p base. Prioritu nastav�na hodnotu \p priority.
    \param triangle Trojheln� v bin�n� stromu trojheln�.
    \param priority Priorita trojheln�u ve front�
    \param left Lev soused trojheln�u.
    \param right Prav soused trojheln�u.
    \param base Z�ladnov soused trojheln�u.
    \param diamond Diamant, do kter�o trojheln� pat� */
	
	public  SbROAMSplitQueueTriangle(SbROAMTriangle triangle) {
		this(triangle, PRIORITY_MIN,null,null,null,null);
	}
	public  SbROAMSplitQueueTriangle(SbROAMTriangle triangle, float priority) {
		this(triangle, priority,null,null,null,null);
	}
	public  SbROAMSplitQueueTriangle(SbROAMTriangle _triangle,
      float _priority,
      SbROAMSplitQueueTriangle _left,
      SbROAMSplitQueueTriangle _right,
      SbROAMSplitQueueTriangle _base,
      SbROAMMergeQueueDiamond _diamond) {
  triangle =_triangle; left =_left; right =_right; base =_base;
  diamond =_diamond;  priority =-_priority; index =-1;

  // nic
}

	public void destructor() {
		// nothing to do
	}
	
	
    /** Z�k��priority trojheln�u ve front�
    Vr��hodnotu priority trojheln�u ve front�trojheln� na rozd�en�
    \return Priorita trojheln�u ve front�trojheln� na rozd�en� */
	public float getPriority()
{
  return -priority; // hledame maximum, takze opacna hodnota priority
}

	
    /** Nastaven�priority trojheln�u ve front�
    Nastav�prioritu trojheln�u ve fronte \p split_queue na novou hodnotu
    \p priority.
    \param split_queue Fronta, v n�se trojheln� nach��
    \param priority Nov�priorita trojheln�u. */
	public void setPriority(SbROAMSplitQueue split_queue, final float
      _priority)
{
  /* Nastaveni nove priority a opraveni fronty. */
  priority = -_priority;
  split_queue.newWeight(this, index);
}


	
    /** Nastaven�priority trojheln�u.
    Nastav�prioritu trojheln�u na novou hodnotu \p priority, ale
    neaktualizuje jeho pozici ve front�
    \param priority Nov�priorita trojheln�u. */
	public void setPriority(final float _priority)
{
  /* Nastaveni nove priority. */
  priority = -_priority;
}

	
	
    /* Datove polozky. */
    /// Trojheln� v bin�n� stromu trojheln�.
	public SbROAMTriangle triangle; //ptr
    /// Soused nad levou odv�nou.
	public SbROAMSplitQueueTriangle left; //ptr
    /// Soused nad pravou odv�nou.
	public SbROAMSplitQueueTriangle right; //ptr
    /// Soused pod peponou.
	public SbROAMSplitQueueTriangle base; //ptr
    /// Diamant, pod kter trojheln� pat�
	public SbROAMMergeQueueDiamond diamond; //ptr
  
    /// Priorita trojheln�u ve front�trojheln� na rozd�en�
    float priority;
    /// Index trojheln�u ve front�trojheln� na rozd�en�
    int index;
  /** Prioritn�fronta trojheln� na rozd�en�me pistupovat k priv�n�
  atributm trojheln�u. */
  //friend class SbROAMSplitQueue;
}
