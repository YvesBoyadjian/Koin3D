
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Z�ladn�datov�prvky algoritmu Geo Mip-mapping.
/// \file SbGeoMipmapPrimitives.h
/// \author Radek Barto�- xbarto33
/// \date 30.01.2006
///
/// Zde jsou definov�y z�ladn�datov�typy algoritmu Geo Mip-Mapping.
/// Kvadrantov strom dladic ve t��::SbGeoMipmapTileTree rekurzivn�/// rozd�uje vkovou mapu ter�u na dladice t�y SbGeoMipmapTile. V jeho
/// horn�h patrech jsou dladice pouze virtu�n� tj. ty, kter�neobsahuj�/// vlastn�geometrii, ale jenom sv�potomky. Dladice na nejni�rovni
/// stromu obsahuj�pole jednotlivch rovn�detail dladice ve t��/// ::SbGeoMipmapTileLevel, jen u geometrii zahrnuj�
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

package jterrain.geomipmapping;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.port.Array;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbGeoMipmapTile implements Mutable {
	
	public SbGeoMipmapTile() {
		this(null,
			      null, null,
			      null, null,
			      LEVEL_NONE, new SbBox3f(),
			      new SbVec3f());
	}

  public
    /* Metody */
    /** Konstruktor.
    Vytvo�instanci t�y ::SbGeoMipmapTile a inicializuje jeho datov�    prvky na hodnoty \p levels, \p left, \p right, \p top, \p bottom, \p level,
    \p bounds a \p center.
    \param levels Ukazatel na pole rovn�detail dladice.
    \param left Ukazatel na lev�o souseda dladice.
    \param right Ukazatel na prav�o souseda dladice.
    \param top Ukazatel na horn�o souseda dladice.
    \param bottom Ukazatel na doln�o souseda dladice.
    \param level �slo rovn�detail, kter�se m�vykreslit.
    \param bounds Ohrani�n�vrchol dladice.
    \param center Sted ohrani�n�vrchol dladice. */
    SbGeoMipmapTile(Array<SbGeoMipmapTileLevel> levels ,
      SbGeoMipmapTile left , SbGeoMipmapTile right,
      SbGeoMipmapTile top, SbGeoMipmapTile bottom,
      int level, SbBox3f bounds ,
      SbVec3f center ) {
	this.levels = levels;
	this.left = left;
	this.right = right;
	this.top = top;
	this.bottom = bottom;
	this.level = level;
	this.bounds.copyFrom( bounds);
	this.center.copyFrom( center);
}
    /** Destruktor.
    Zru�instanci t�y ::SbGeoMipmapTile a uvoln�pole rovni detail
    dladice. */
    public void destructor() {
    	
    }
    /* Datove polozky */
    /// Ukazatel na pole rovn�detail dladice.
    public Array<SbGeoMipmapTileLevel> levels; //ptr
    /// Ukazatel na lev�o souseda dladice.
    public SbGeoMipmapTile left; //ptr
    /// Ukazatel na prav�o souseda dladice.
    public SbGeoMipmapTile right; //ptr
    /// Ukazatel na horn�o souseda dladice.
    public SbGeoMipmapTile top; //ptr
    /// Ukazatel na doln�o souseda dladice.
    public SbGeoMipmapTile bottom; //ptr
    /// �slo rovn�detail, kter�se m�vykreslit.
    public int level;
    /// Ohrani�n�vrchol dladice.
    public final SbBox3f bounds = new SbBox3f();
    /// Sted ohrani�n�vrchol dladice.
    public final SbVec3f center = new SbVec3f();
    /* Konstanty. */
    /// �slo rovn�detail dladice, kter�se nem�vykreslovat vbec.
    public static final int LEVEL_NONE = -1;
	@Override
	public void copyFrom(Object other) {
		SbGeoMipmapTile ot = (SbGeoMipmapTile)other;
		levels = ot.levels;
		left = ot.left;
		right = ot.right;
		top = ot.top;
		bottom = ot.bottom;
		level = ot.level;
		bounds.copyFrom(ot.bounds);
		center.copyFrom(ot.center);
	}
}
