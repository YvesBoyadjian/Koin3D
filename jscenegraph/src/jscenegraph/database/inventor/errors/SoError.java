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
 |      This file defines the base SoError class.
 |
 |   Classes:
 |      SoError
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.errors;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Error handling base class.
/*!
\class SoError
\ingroup Errors
SoError is the base class for all error classes,
which provide error handling for applications.
There are two facets to errors: posting and handling. An error is
posted when some bad condition occurs. Posting is done primarily
by the Inventor library itself, but extenders can post their own
errors. Posting an error creates an instance of the appropriate
error class (or subclass) and then passes it to the active error
handler. The default handler just prints an appropriate message to
stderr. Applications can override this behavior by supplying a
different handler (by specifying a callback function).


Each subclass of SoError supports the
setHandlerCallback() method,
which is used to set the callback function to handle errors.
The callback function for a specfic error class is always used in preference
to that of any base classes when handling errors.
The error instance passed to a callback is deleted immediately
after the callback is called; an application that wishes to save
information from the instance has to copy it out first.


Each error class contains a run-time class type id (SoType) that
can be used to determine the type of an instance. The base class
defines a character string that represents a detailed error
message that is printed by the default handler.
All handlers are called by the SoError::handleError() method. When
debugging, you can set a breakpoint on this method to stop right
before an error is handled.

\par See Also
\par
SoDebugError, SoMemoryError, SoReadError
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoError {
	
	private static final SoType classTypeId = new SoType();
	private static SoErrorCB handlerCB;
	private static Object cbData;
	private static boolean wasInitted;
	private String debugString;
	
	public interface SoErrorCB {
		void run(SoError error, Object data);
	}
	
	// Returns debug string containing full error information from instance. 
	public String getDebugString() {
		return debugString;
	}
	
	// Returns type identifier for SoError class. 
	public static SoType getClassTypeId()  {
		 return classTypeId; 
	}
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Initializes the SoError class.
	    //
	   // Use: internal
	   
	  static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       // Initialize type id
	       classTypeId.copyFrom(SoType.createType(SoType.badType(), new SbName("Error")));
	   
	       // Set handler to default handler
	       handlerCB = defaultHandlerCB;
	       cbData    = null;
	   
	       wasInitted = true;
	   }
	   	
	// Initialize ALL Inventor error classes. 
	public static void initClasses() {
	     SoError.initClass();
	      
          SoDebugError.initClass();
          //SoMemoryError.initClass();
          //SoReadError.initClass();
	     		
	}
	
	// The default error handler callback - it just prints to stderr.
	protected static SoErrorCB defaultHandlerCB = new SoErrorCB() {
		public void run(SoError error, Object data) {
			System.err.println(error.getDebugString()/*.getString()*/); // java port
		}
	};
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns type id of instance.
//
// Use: public

public SoType getTypeId()
//
////////////////////////////////////////////////////////////////////////
{
    return new SoType(classTypeId);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if error is an instance of the given type or a
//    subclass of it.
//
// Use: public

public boolean
isOfType(SoType type)
//
////////////////////////////////////////////////////////////////////////
{
    return getTypeId().isDerivedFrom(type);
}

	
	
	// Returns handler callback (and data) to use for a given instance. 
	protected SoErrorCB getHandler(Object[] data) {
		data[0] = cbData;
		return handlerCB;
	}

	// Sets/appends to the debug string. 
	protected void setDebugString(String string) {
		debugString = string;
	}
	
    protected void                appendToDebugString(String string)
        { debugString += string; }
	
	/**
	 * Calls appropriate handler for an error instance. 
	 * Application writers can set breakpoints at this when debugging. 
	 */
	protected void handleError() {
		
		  // Determine the handler to call and call it
		  Object[] data = new Object[1];
		  SoErrorCB handler = getHandler(data);
		 
		  // If the handler is NULL, this can mean one of two things. Either
		  // the SoError class was never initialized, or someone purposely
		  // set the handler to NULL. We can use the wasInitted flag to
		  // tell these cases apart.
		 
		  if (handler == null) {
		 
		  // If the class was initialized, someone set the handler to
		  // NULL. So don't do anything.
		  if (wasInitted)
		  ;
		 
		  // The class was never initialized. Use the default handler
		  else
		  defaultHandlerCB.run(this, data[0]);
		  }
		 
		  else
			  handler.run(this, data[0]);
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Posts an error. The debugString will be created from the given
//    arguments, which are in printf() format.
//
// Use: extender

public static void
post(String formatString)
//
////////////////////////////////////////////////////////////////////////
{
    final SoError     error = new SoError();

    // Set up the debugString. This just passes everything to sprintf,
    // using the variable arguments stuff in stdarg.h. I got this from
    // the C++ Reference Manual, page 148.

    String        buf = formatString;
//    va_list     ap;
//
//    va_start(ap, formatString);
//    vsprintf(buf, formatString, ap);
//    va_end(ap);

    error.setDebugString("Inventor error: ");
    error.appendToDebugString(buf);
    error.handleError();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Used by all getString() methods.
//
// Use: private

private String
getBaseString(final SoBase base, String what)
//
////////////////////////////////////////////////////////////////////////
{
    final SbName        baseName = base.getName();
    String            str;
    //char                addrBuf[32];

    str  = what;

    if (! (baseName.operator_not())) {
        str += " named \"";
        str += baseName.getString();
        str += "\"";
    }

    //sprintf(addrBuf, "%#x", base);
    str += " at address ";
    str += base;

    return str;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns printable string representing a node.
//
// Use: extender

public String
getString(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    return getBaseString(node, "node");
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns printable string representing a path.
//
// Use: extender

public String
getString(SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    return getBaseString(path, "path");
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns printable string representing an engine.
//
// Use: extender

public String
getString(SoEngine engine)
//
////////////////////////////////////////////////////////////////////////
{
    return getBaseString(engine, "engine");
}


}
