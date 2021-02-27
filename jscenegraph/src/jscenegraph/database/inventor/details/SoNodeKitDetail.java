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
 |      This file defines classes for describing or extracting
 |      detail information about a node kit.
 | 
 |
 |   The details contains a pointer to the node, a pointer to the 
 |   child part within the node, and the name of the child part.
 |   
 |   Classes:
 |      SoNodeKitDetail
 |
 |   Author(s)          : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.details;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Stores detail information about a nodekit.
/*!
\class SoNodeKitDetail
\ingroup Details
This class contains detail information about a nodekit.
This consists of a pointer to the nodekit, a pointer to the 
child part within the nodekit, and the name of the child part.


During a pick action, each nodekit along the picked path creates its own
SoNodeKitDetail.   Together, the full set of details gives you 
complete picture of the pickpath.  


Since nodekits have hidden children (See the reference page for 
SoBaseKit), a regular SoPath ends at the topmost nodekit in the 
path. If you cast the pickpath from an SoPath pointer to an 
SoNodeKitPath pointer, you can then retrieve all nodekits along the 
path and examine their corresponding details.

\par See Also
\par
SoBaseKit, SoNodeKitPath, SoDetail, SoPickedPoint
*/
////////////////////////////////////////////////////////////////////////////////

public class SoNodeKitDetail extends SoDetail {

	  private
	    static final SoType       classTypeId = new SoType();            //!< Type identifier
	  
	    //! Returns type identifier for this class.
	   public static SoType       getClassTypeId() { return new SoType(classTypeId); }

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.details.SoDetail#getTypeId()
	 */
	@Override
	public SoType getTypeId() {
		return classTypeId;
	}
	
  protected SoBaseKit       myNodeKit;
  protected SoNode          myPart;
  protected final SbName          myPartName = new SbName();
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructors.
//
// Use: public
//
////////////////////////////////////////////////////////////////////////

public SoNodeKitDetail()
{
    myNodeKit = null;
    myPart = null;
    myPartName.copyFrom("");
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	if ( myNodeKit !=null)
		myNodeKit.unref();
	if ( myPart !=null)
		myPart.unref();
	super.destructor();
}


  //! Returns an instance that is a copy of this instance. The caller
  //! is responsible for deleting the copy when done.
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.details.SoDetail#copy()
	 */
	@Override
	public SoDetail copy() {
    SoNodeKitDetail newDetail = new SoNodeKitDetail();

    newDetail.myNodeKit  = myNodeKit;
    newDetail.myPart     = myPart;
    newDetail.myPartName.copyFrom(myPartName);

    if (myNodeKit != null)
        myNodeKit.ref();
    if (myPart != null)
        myPart.ref();

    return newDetail;
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the the nodekit.
//
public void
setNodeKit( SoBaseKit theNodeKit )
//
////////////////////////////////////////////////////////////////////////
{
    theNodeKit.ref();
    if ( myNodeKit != null)
        myNodeKit.unref();
    myNodeKit = theNodeKit;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the the part node within the nodekit.
//
public void
setPart( SoNode thePart )
//
////////////////////////////////////////////////////////////////////////
{
    thePart.ref();
    if ( myPart != null)
        myPart.unref();
    myPart = thePart;
}

public void setPartName( final SbName theName )      { myPartName.copyFrom(theName); }

////////////////////////////////////////////////////////////////////////
//
// Class initialization
//
// SoINTERNAL public
//
public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    //SO_DETAIL_INIT_CLASS(SoNodeKitDetail.class, SoDetail.class);
    classTypeId.copyFrom(SoType.createType(SoDetail.getClassTypeId(),           
    		new SbName("SoNodeKitDetail"), null));
}

}
