/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoLazyElement and SoColorPacker classes.
 |
 |   Author(s)          : Alan Norton, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////
///
////\class SoColorPacker
/// This class is meant to be used by all property nodes that set either
/// a diffuse color or transparency in the lazy element.  It maintains
/// a cache of the current diffuse color and transparency in a packed
/// color array.
//////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoColorPacker implements Destroyable {

    private
    //! nodeids are used for testing cache validity
    long    transpNodeId;
    private long    diffuseNodeId;
    //! array of packed colors, or NULL if empty
    private int[]   packedColors;
    //! size of packed color array (not necessarily number of valid colors)
    private int     packedArraySize;

    public int[] getPackedColors() 
    { return packedColors;}
    
    public boolean diffuseMatch(long nodeId)
    { return (nodeId == diffuseNodeId);}
    
    public boolean transpMatch(long nodeId)
    { return (nodeId == transpNodeId);}
    
    public void setNodeIds(long diffNodeId, long tNodeId)
    {diffuseNodeId = diffNodeId; transpNodeId = tNodeId;}

    public int getSize()
    { return packedArraySize;}
    
    ///////////////////////////////////////////////////////////////////////////
//
// class:  SoColorPacker
//
// Maintains a packed color array to store current colors.  Intended to
// be used by all property nodes that can issue setDiffuse or setTransparency
/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
//
// Description:
//  constructor for  SoColorPacker
//
// use: public
//
////////////////////////////////////////////////////////////////////////////
public SoColorPacker()
{
    packedColors = null;
    packedArraySize = 0;
    //Assign nodeids that can never occur in practice:
    diffuseNodeId = transpNodeId = 2;
}

/////////////////////////////////////////////////////////////////////////////
//
// Description:
//  destructor
//
//  use: public
////////////////////////////////////////////////////////////////////////////
public void destructor()
{
    if(packedColors != null) /*delete []*/ packedColors = null;
}

////////////////////////////////////////////////////////////////////////////
//
// Description:
//
//  reallocate packed color array for SoColorPacker
//
// use: public, SoINTERNAL
//
////////////////////////////////////////////////////////////////////////////
public void
reallocate(int size)
{
    //if (packedColors != null) delete [] packedColors; java port
    packedColors = new int[size];
    packedArraySize = size;
}

    public long getDiffuseId() {
        return diffuseNodeId;//this.diffuseid;
    }
    public long getTranspId() {
        return transpNodeId;//this.transpid;
    }

}
