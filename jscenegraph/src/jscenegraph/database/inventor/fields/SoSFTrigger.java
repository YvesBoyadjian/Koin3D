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
 |   Classes:
 |      SoSFTrigger
 |
 |   Author(s)          : Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.misc.SoNotList;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Field used to trigger engines or connection networks.
/*!
\class SoSFTrigger
\ingroup Fields
This class can be used to start or to synchronize a network of field
connections.  It is the "null" field em a field with no values.  It
is typically used as the "start button" for engines that change over
time.


Triggers can be connected from any other type of field, and will notify
any engines or nodes they are part of (or any other triggers they are
connected to) whenever the value of the field is set (even if it is set
to its current value) or the field is touch()'ed.


Since they have no value, SoSFTriggers are not written to file.  A
node or engine containing an SoSFTrigger field will write only the
field's name.

\par See Also
\par
SoSFBool, SoMFBool
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSFTrigger extends SoSField<Object> {

	@Override
	protected Object constructor() {
		return null;
	}


    //! Starts the notification process; this is equivalent to calling touch().
    public void                setValue()              { touch(); }

    //! Forces any connected engines or fields to evaluate themselves.
    public Object                getValue()        { return null;}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override startNotify to prevent notification from going any
//    further than just this field.
//    Temporarily sets container to NULL, which prevents notification
//    from going to downstream connections.  Relies on the dirty bit
//    being set and evaluate getting called to clear it (in notify()).
//
// Use: internal

public void startNotify()
//
////////////////////////////////////////////////////////////////////////
{
    SoFieldContainer container = getContainer();
    setContainer(null);

    super.startNotify();

    setContainer(container);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override notification to always evaluate, clearing upstream dirty
//    bits.
//
// Use: internal

public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    super.notify(list);
    evaluate();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override touch to notify.
//
// Use: internal

public void touch()
//
////////////////////////////////////////////////////////////////////////
{
    super.startNotify();  // Use SoField method explicitly
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    "Reads value from file." Doesn't bother reading anything, since
// write method is a no-op.  Note that trigger fields might get
// written/read for field connections to be written/read, so it's not
// an error for this routine to be called.
//
// Use: private

public boolean readValue(SoInput input)
//
////////////////////////////////////////////////////////////////////////
{
    return true;
}

}
