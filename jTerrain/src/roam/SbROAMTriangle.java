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

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbROAMTriangle implements Mutable {
    /* Metody. */
    /** Konstruktor.
    Vytvo�instanci t�y ::SbROAMTriangle a inicializuje jeho atributy.
    Parametry \p first, \p second a \p apex jsou indexy vrchol trojheln�u
    v seznamu koordin� a parametr \p level je �slo rovn�trojheln�u
    v bin�n� stromu trojheln�.
    \param first Prvn�(lev) vrchol pepony trojheln�u.
    \param second Druh (prav) vrchol pepony trojheln�u.
    \param apex Prav (nad peponou) vrchol trojheln�u.
    \param level �ove� na kter�se trojheln� nach��v bin�n� stromu
       trojheln�. */
    public SbROAMTriangle() {
    	this(-1, -1, -1, 0);
    }
    public SbROAMTriangle(int _first, int _second, int _apex, int _level) {
    	  first =_first;
    	  second =_second;
    	  apex = _apex;
    	  level = _level;
    	  error = 0.0f;
    	  radius = 0.0f;    	
    }
    /* Datove polozky. */
    /// Lev vrchol pepony.
    public int first;
    /// Prav vrchol pepony.
    public int second;
    /// Vrchol nad peponou.
    public int apex;
    /// �ove�trojheln�u ve stromu.
    public int level;
    /// Chyba nezavisl�na bodu pohledu.
    public float error;
    /// Polom� kuloplochy ohrani�j��trojheln� a jeho potomky.
    public float radius;
	@Override
	public void copyFrom(Object other) {
		
		SbROAMTriangle otherTriangle = (SbROAMTriangle)other;
		
	    /* Datove polozky. */
	    /// Lev vrchol pepony.
	    first = otherTriangle.first;
	    /// Prav vrchol pepony.
	    second = otherTriangle.second;
	    /// Vrchol nad peponou.
	    apex = otherTriangle.apex;
	    /// �ove�trojheln�u ve stromu.
	    level = otherTriangle.level;
	    /// Chyba nezavisl�na bodu pohledu.
	    error = otherTriangle.error;
	    /// Polom� kuloplochy ohrani�j��trojheln� a jeho potomky.
	    radius = otherTriangle.radius;
	}

}
