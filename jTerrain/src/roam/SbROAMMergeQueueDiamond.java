
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

import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbROAMMergeQueueDiamond implements Destroyable {
  
    /* Metody. */
    /** Konstruktor.
    Vytvo�instanci diamantu pro prioritn�frontu diamant na spojen�    z trojheln� \p first, \p second, \p third a \p fourth a nastav�    prioritu tohoto diamantu na hodnotu \p priority.
    \param first Prvn�trojheln� diamantu.
    \param second Druh trojheln� diamantu.
    \param third Tet�trojheln� diamantu.
    \param fourth �vrt trojheln� diamantu.
    \param priority Priorita diamantu ve front�diamant. */
	public  SbROAMMergeQueueDiamond() {
		
	this(
		null,
      null,
      null,
      null,
      SbROAMSplitQueueTriangle.PRIORITY_MAX);
	}
	public SbROAMMergeQueueDiamond(
  SbROAMSplitQueueTriangle  _first,
  SbROAMSplitQueueTriangle  _second,
  SbROAMSplitQueueTriangle  _third,
  SbROAMSplitQueueTriangle  _fourth,
  float _priority) {
  first = _first; second = _second; third =_third; fourth = _fourth;
  priority =_priority; index =-1;

  // nic
}
	
	public void destructor() {
		// nothing to do		
	}
	
    /** Z�k��priority diamantu ve front�
    Vr��hodnotu priority diamantu ve front�diamant na spojen�
    \return Priorita diamantu ve front�diamant na spojen� */
	public float getPriority()
	{
	  return priority; // vraceni priority
	}

	
	
	
    /** Nastaven�priority diamantu ve front�
    Nastav�hodnotu priority diamantu ve front�\p merge_queue na novou
    hodnotu \p priority.
    \param merge_queue Fronta, v n�se diamant nach��
    \param priority Nov�priorita diamantu. */
	public void setPriority(SbROAMMergeQueue merge_queue, final float
      _priority)

{
  /* Nastaveni nove priority a opraveni fronty. */
  priority = _priority;
  merge_queue.newWeight(this, index);
}

	
	
	
    /** Nastaven�priority diamantu.
    Nastav�hodnotu priority diamantu na novou hodnotu \p priority, ale
    neaktualizuje jeho pozici ve front�
    \param priority Nov�priorita diamantu. */
	public void setPriority(final float _priority)
{
  /* Nastaveni nove priority. */
  priority = _priority;
}

	
	
	
    /* Datove polozky. */
    /// Prvn�trojheln� diamantu.
	public SbROAMSplitQueueTriangle first; //ptr
    /// Druh trojheln� diamantu.
	public SbROAMSplitQueueTriangle second; //ptr
    /// Tet�trojheln� diamantu.
	public SbROAMSplitQueueTriangle third; //ptr
    /// �vrt trojheln� diamantu.
	public SbROAMSplitQueueTriangle fourth; //ptr
  
    /// Priorita diamantu ve front�diamant na spojen�
	float priority;
    /// Index diamantu ve front�diamant na spojen�
	int index;
  /** Prioritn�fronta diamant na spojen�me pistupovat k priv�n�
  atributm diamantu. */
  //friend class SbROAMMergeQueue;
}
