
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Prioritn�fronta trojheln� na rozd�en�
/// \file SbROAMSplitQueue.h
/// \author Radek Barto�- xbarto33
/// \date 25.08.2005
///
/// Do t�o prioritn�fronty se vkl�aj�trojheln�y, kter�je teba zaadit
/// do triangulace. Pokud je teba triangulaci d�e zjem�vat, vyjme se z fronty
/// trojheln� s maxim�n�prioritou a vlo�se zp� jeho potomci.
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
public class SbROAMSplitQueue extends SbHeap {
  
    /* Metody. */
    /** Konstruktor.
    Vytvo�instanci t�y ::SbROAMSplitQueue o po�te��velikosti
    \p init_size poloek. Prioritn�fronta se sama podle poteb zv�uje.
    \param init_size Po�te��velikost prioritn�fronty trojheln�
    na rozd�en�  */
	
	public SbROAMSplitQueue() {
		this(1024);
	}
	public SbROAMSplitQueue(final int init_size) {
		super(heap_funcs, init_size);
	}
    /** Destruktor.
    Zru�instanci t�y ::SbROAMSplitQueue. */
	public void destructor() {
		super.destructor();
	}
    /** Vypr�dn�prioritn�frontu.
    Zavol�� t�o metody dojde k vypr�dn��prioritn�fronty trojheln�
    na rozd�en� */
	public void emptyQueue() {
		super.emptyHeap();
	}
    /** Vlo�trojheln� do fronty.
    Vlo�trojheln� bin�n�o stromu trojheln� \p triangle do prioritn�    fronty trojheln� na rozd�en�
    \param triangle Trojheln�, kter se m�do fronty vloit. */
	public void add(SbROAMSplitQueueTriangle triangle) {
		  /* Vlozeni trojuhelniku a aktualizace indexu ve fronte. */
		  int index = super.add(triangle);
		  triangle.index = index;		
	}
	
    /** Odstran�trojheln� z fronty.
    Odstran�trojheln� \p triangle z prioritn�fronty trojheln�.
    \param triangle Trojheln�, kter se m�z fronty odstranit. */
	public void remove(SbROAMSplitQueueTriangle triangle) {
		  super.remove(triangle.index); // odstraneni trojuhelniku z fronty		
	}
	
    /** Vyjme a vr��trojheln� z fronty.
    Odstran�z prioritn�fronty trojheln� na rozd�en�trojheln� s
    nejvy�prioritou a vr��ukazatel na n�.
    \return Ukazatel na trojheln� s nejvy�prioritou. */
	public SbROAMSplitQueueTriangle extractMax() {
		  /* Vyber trojuhelniku s maximalni prioritou. */
		  return (SbROAMSplitQueueTriangle)(super.extractMin());		
	}
	
	
    /** Vr��trojheln� z fronty.
    Vr��ukazatel na trojheln� s nejvy�prioritou ve front� Trojheln�
    ve front�nad�e zst��
    \return Ukazatel na trojheln� s nejvy�prioritou. */
	public SbROAMSplitQueueTriangle getMax() {
		  /* vraceni trojuhelniku s maximalni prioritou. */
		  return (SbROAMSplitQueueTriangle)(super.getMin());		
	}
	
	
    /** Vr��trojheln� z fronty podle indexu.
    Vr��ukazatel na trojheln� na indexu \p index v prioritn�front�    trojheln�.
    \return Ukazatel na trojheln� na indexu \p index. */
	public SbROAMSplitQueueTriangle operator_square_bracket(final int index) {
		  return (SbROAMSplitQueueTriangle)
		    (super.operator_square_bracket(index));		
	}
	
	
    /* Importovane metody. */
    //using SbHeap::size;
    //using SbHeap::newWeight;
    //using SbHeap::buildHeap;
 
    /* Metody. */
    /** Vr��prioritu trojheln�u.
    Vr��prioritu trojheln�u \p triangle v prioritn�front�trojheln�
    na rozd�en�
    \param triangle Trojheln�, jeho priorita se m�z�kat.
    \return Priorita trojheln�u. */
    protected static float getTrianglePriority(Object triangle) {
    	  /* Zjisteni priority trojuhelniku ve fronte. */
    	  return ((SbROAMSplitQueueTriangle)(triangle)).priority;    	
    }
    
    
    /** Vr��index trojheln�u.
    Vr��index trojheln�u \p triangle v prioritn�front�trojheln�
    na rozd�en�
    \param triangle Trojheln�, jeho index se m�z�kat.
    \return Index trojheln�u. */
    protected static int getTriangleIndex(Object triangle) {
    	  /* Zjisteni priority trojuhelniku ve fronte. */
    	  return ((SbROAMSplitQueueTriangle)(triangle)).index;    	
    }
    
    
    /** Nastav�index trojheln�u.
    Nastav�index trojheln�u \p triangle v prioritn�front�trojheln�
    na hodnotu \p index.
    \param triangle Trojheln�, u kter�o se m�index nastavit.
    \param index Nov index trojheln�u. */
    protected static void setTriangleIndex(Object triangle, int index) {
    	  /* Nataveni priority trojuhelniku ve fronte. */
    	  ((SbROAMSplitQueueTriangle)(triangle)).index = index;    	
    }
    
    /* Datove polozky. */
    /// Struktura callback obsahuj��metody pro pr�i s trojheln�y.
    protected static final SbHeapFuncs heap_funcs = new SbHeapFuncs(SbROAMSplitQueue::getTrianglePriority,
    		SbROAMSplitQueue::getTriangleIndex, SbROAMSplitQueue::setTriangleIndex);
}
