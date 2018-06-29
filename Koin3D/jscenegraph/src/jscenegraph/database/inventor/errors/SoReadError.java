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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file defines the SoReadError class.
 |
 |   Classes:
 |      SoReadError
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.errors;

import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Read error handling.
/*!
\class SoReadError
\ingroup Errors
SoReadError  is used for errors reported
while reading Inventor data files.

\par See Also
\par
SoDebugError, SoMemoryError
*/
////////////////////////////////////////////////////////////////////////////////

public class SoReadError extends SoError {


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Posts an error.
//
// Use: extender

public static void post(final SoInput in, String formatString)
//
////////////////////////////////////////////////////////////////////////
{
    final SoReadError error = new SoReadError();
    String            str;
    final String[] locstr = new String[1];

    // Same stuff as in base class
    String        buf;
    //va_list     ap;

    //va_start(ap, formatString);
    /*vsprintf(*/buf = formatString/*, ap)*/;
    //va_end(ap);

    str  = "Inventor read error: ";
    str += buf;
    str += "\n";

    in.getLocationString(locstr);
    str += locstr[0];

    error.setDebugString(str/*.getString()*/);
    error.handleError();
}

}
