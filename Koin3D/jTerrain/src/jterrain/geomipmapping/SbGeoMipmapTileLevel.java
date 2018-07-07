
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

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbGeoMipmapTileLevel implements Mutable {

	public SbGeoMipmapTileLevel() {
		this(0,null);
	}

	public SbGeoMipmapTileLevel(float error, int[] vertices) {
		this.error = error;
		this.vertices = vertices;
	}

	public float error;
	
	public int[] vertices;

	@Override
	public void copyFrom(Object other) {
		SbGeoMipmapTileLevel ot = (SbGeoMipmapTileLevel)other;
		error = ot.error;
		vertices = ot.vertices;
	}
}
