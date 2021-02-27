
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

import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jterrain.Utils;

/**
 * @author Yves Boyadjian
 *
 */
public class SbGeoMipmapTileTree implements Destroyable {
	  public
		    /* Metody. */
		    /** Konstruktor.
		    Vytvo�kvadrantov strom dladic algoritmu Geo Mip-Mapping t�y
		    ::SbGeoMipmapTileTree obsahuj��o dladice o velikosti strany \p tile_size
		    uspo�anch do �verce o stran�\p tile_count dladic.
		    \param tile_count Po�t dladic na stranu vkov�mapy.
		    \param tile_size Velikost strany jedn�dladice. */
		    SbGeoMipmapTileTree(int tile_count, int tile_size) {
		  this.tile_count = tile_count;
		  this.tile_size = tile_size;
		  tree_size =0;
		  level_count=0;
		  level_sizes = null;
		  tiles = null;
		
		  /* Vypocet velikosti stromu dlazdic. */
		  tree_size = tile_count;
		  int tmp = tile_count;
		  while (tmp > 1)
		  {
		    tmp >>= 2;
		    tree_size+= tmp;
		  }

		  /* Vypocet zacatku dlazdic na nejnizzsi urovni. */
		  bottom_start = tree_size - tile_count;

		  /* Vypocet poctu urovni detailu v jedne dlazdici. */
		  level_count = Utils.ilog2(tile_size - 1) + 1;

		  /* Alokace a inicializace pole velikosti jednotlivych urovni detailu
		  jedne dlazdice. */
		  level_sizes = new int[level_count];
		  level_sizes[0] = tile_size;
		  for (int I = 1; I < level_count; ++I)
		  {
		    // predpoklad licheho cisla
		    level_sizes[I] = (level_sizes[I - 1] >> 1) + 1;
		  }

		  /* Alokace pole dlazdic. */
		  tiles = new Array<>(SbGeoMipmapTile.class, new SbGeoMipmapTile[tree_size]);
		  
	  }
		    /** Destruktor.
		    Zru�instanci t�y ::SbGeoMipmapTileTree a uvoln�pole velikost�rovn�    detail dladic i vechny dladice ve strom� */
		    //~SbGeoMipmapTileTree();
		    /* Datove polozky. */
		    /// Po�t dladic na stranu vstupn�vkov�mapy.
		    public int tile_count;
		    /// Velikost strany jedn�dladice.
		    public int tile_size;
		    /// Vka kvadrantov�o stromu dladic.
		    public int tree_size;
		    /// Index v poli dladic, kde za�n�nejni�patro stromu.
		    public int bottom_start;
		    /// Po�t rovn�detail kad�dladice v nejni� patru stromu.
		    public int level_count;
		    /// Pole velikost�stran pol�vrchol rovn�kad�dladice.
		    public int[] level_sizes;
		    /// Pole jednotlivch dladic kvadrantov�o stromu dladic.
		    public Array<SbGeoMipmapTile> tiles;
		  private
		    /** Kop�ovac�konstruktor.
		    Zprivatizov�, aby se zabr�ilo vytv�en�kopi�kvadrantov�o stromu dladic.
		    \param old_tree Pvodn�kvadrantov strom dladic. */
		    SbGeoMipmapTileTree(final SbGeoMipmapTileTree old_tree) {
			  // nic
		  }
		@Override
		public void destructor() {
			// TODO Auto-generated method stub
			
		}

}
