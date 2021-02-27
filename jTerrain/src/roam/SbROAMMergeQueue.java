
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Prioritn�fronta diamant na spojen�
/// \file SbROAMMergeQueue.h
/// \author Radek Barto�- xbarto33
/// \date 25.08.2005
///
/// Do t�o prioritn�fronty se vkl�aj�diamanty, kter�lze spojit na dva
/// trojheln�y a t� dva trojheln�y z pvodn�h �y z triangulace odstranit.
/// Toto se prov��tehdy, pokud je teba triangulaci zjednoduit a to tak, e
/// se vyjme diamant s minim�n�prioritou, spoj�se a vloi se zp� p�adn�/// vznikl�nov�diamanty.
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

import jscenegraph.coin3d.inventor.SbHeap;
import jscenegraph.coin3d.inventor.SbHeapFuncs;

/**
 * @author Yves Boyadjian
 *
 */
public class SbROAMMergeQueue extends SbHeap {
  
    /* Metody. */
    /** Konstruktor.
    Vytvo�instanci t�y ::SbROAMMergeQueue o po�te��velikosti
    \p init_size poloek. Prioritn�fronta se sama podle poteb zv�uje.
    \param init_size Po�te��velikost prioritn�fronty trojheln�
    na rozd�en�  */
	public SbROAMMergeQueue() {
		this(4096);
	}
	public SbROAMMergeQueue( int init_size) {
		  super(heap_funcs, init_size);		
	}
    /** Destruktor.
    Zru�instanci t�y ::SbROAMMergeQueue. */
	public void destructor() {
		super.destructor();
	}
    /** Vypr�dn�prioritn�frontu.
    Zavol�� t�o metody dojde k vypr�dn��prioritn�fronty diamant na
    spojen� */
	public void emptyQueue() {
		  super.emptyHeap(); // vyprazdneni fronty		
	}
	
    /** Vloen�diamantu do fronty.
    Vlo�diamant \p diamond do prioritn�fronty diamant na spojen�
    \param diamond Diamant, kter se m�do fronty vloit. */
	public void add(SbROAMMergeQueueDiamond diamond) {
		  /* Vlozeni diamondu a aktualizace indexu ve fronte. */
		  int index = super.add(diamond);
		  diamond.index = index;		
	}
	
    /** Odstran�diamant z fronty.
    Odstran�diamant \p diamond z prioritn�fronty diamant.
    \param diamond Diamant, kter se m�z fronty odstranit. */
	public void remove(SbROAMMergeQueueDiamond diamond) {
		super.remove(diamond.index);
	}
	
    /** Odstran�diamant z fronty podle jeho trojheln�u.
    Najde ve front�diamant na spojen�diamant, jen obsahuje trojheln�
    \p triangle, vyjme ho z fronty a vr��na n� ukazatel.
    \param triangle Trojheln�, pat��odstra�van�u diamantu.
    \return Ukazatel na odstran� diamant. */
	public SbROAMMergeQueueDiamond remove(SbROAMSplitQueueTriangle triangle) {
		  /* Patri-li trojuhelnik pod nejaky diamond. */
		  SbROAMMergeQueueDiamond diamond = triangle.diamond;
		  if (diamond != null)
		  {
		    /* Vsechny trojuhelniky diamondu uz nejsou jeho soucasti. */
		    diamond.first.diamond = null;
		    diamond.second.diamond = null;

		    /* Nejde-li od degradovany diamond. */
		    if (diamond.third != null)
		    {
		      diamond.third.diamond = null;
		      diamond.fourth.diamond = null;
		    }
		    super.remove(diamond.index); // odstraneni diamondu z fronty
		  }
		  return diamond;		
	}
	
    /** Vyjme a vr��diamant z fronty.
    Odstran�z prioritn�fronty diamant na spojen�trojheln� s
    nejni�prioritou a vr��ukazatel na n�.
    \return Ukazatel na diamant s nejni�prioritou. */
	public SbROAMMergeQueueDiamond extractMin() {
  /* Vyber trojuhelniku s maximalni prioritou. */
		return (SbROAMMergeQueueDiamond)(super.extractMin());		
	}
	
	
	
    /** Vr��diamant z fronty.
    Vr��ukazatel na diamant s nejni�prioritou ve front� Diamant ve front�    nad�e zst��
    \return Ukazatel na diamant s nejni�prioritou. */
	public SbROAMMergeQueueDiamond getMin() {
		  /* Vraceni trojuhelniku s maximalni prioritou. */
		  return (SbROAMMergeQueueDiamond)(super.getMin());		
	}
	
    /** Vr��diamant z fronty podle indexu.
    Vr��ukazatel na diamant na indexu \p index v prioritn�front�diamant.
    \return Ukazatel na trojheln� na indexu \p index. */
	public SbROAMMergeQueueDiamond operator_square_bracket(final int index) {
		  return (SbROAMMergeQueueDiamond)(super.operator_square_bracket(index));		
	}
    //using SbHeap::size;
    //using SbHeap::newWeight;
    //using SbHeap::buildHeap;
  
    /* Metody. */
    /** Vr��prioritu diamantu.
    Vr��prioritu diamantu \p diamond v prioritn�front�diamant na spojen�
    \param diamond Diamant, jeho priorita se m�z�kat.
    \return Priorita diamantu. */
    protected static float getDiamondPriority(Object diamond) {
    	  /* Zjisteni priority diamondu ve fronte. */
    	  return ((SbROAMMergeQueueDiamond)(diamond)).priority;    	
    }
    /** Vr��index diamantu.
    Vr��index diamantu \p diamant v prioritn�front�diamant na spojen�
    \param diamond Diamant, jeho index se m�z�kat.
    \return Index diamantu. */
    protected static int getDiamondIndex(Object diamond) {
    	  /* Zjisteni priority diamondu ve fronte. */
    	  return ((SbROAMMergeQueueDiamond)(diamond)).index;    	
    }
    
    
    /** Nastav�index diamantu.
    Nastav�index diamantu \p diamond v prioritn�front�diamant na hodnotu
    \p index.
    \param diamond Diamant, u kter�o se m�index nastavit.
    \param index Nov index diamantu. */
    protected static void setDiamondIndex(Object diamond, int index) {
    	  /* Nataveni priority diamondu ve fronte. */
    	  ((SbROAMMergeQueueDiamond)(diamond)).index = index;    	
    }
    /* Datove polozky. */
    /// Struktura callback obsahuj��metody pro pr�i s diamanty.
    protected static final SbHeapFuncs heap_funcs = new SbHeapFuncs(
    SbROAMMergeQueue::getDiamondPriority, SbROAMMergeQueue::getDiamondIndex,
    SbROAMMergeQueue::setDiamondIndex);


}
