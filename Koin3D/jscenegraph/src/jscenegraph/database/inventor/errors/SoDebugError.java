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
 |      This file defines the SoDebugError class.
 |
 |   Classes:
 |      SoDebugError
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.errors;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;


////////////////////////////////////////////////////////////////////////////////
//! Debug error handling.
/*!
\class SoDebugError
\ingroup Errors
SoDebugError is used for all errors reported from the debugging
version of the Inventor library. These errors are typically
programmer errors, such as passing a NULL pointer or an
out-of-range index. 
The post() method takes the name of the
Inventor method that detected the error, to aid the programmer in
debugging.

\par See Also
\par
SoMemoryError, SoReadError
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDebugError extends SoError {
	
	   private static final SoType       classTypeId = new SoType();    //!< Type id of SoDebugError class
	   private static SoErrorCB    handlerCB;     //!< Handler callback for class
	   private static Object         cbData;        //!< User data for callback
		    
		   private Severity severity; //!< Severity of error
	
	public enum Severity {
		ERROR,
		WARNING,
		INFO
	}

	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Initializes the SoDebugError class.
	    //
	    // Use: internal
	    
	   static void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        // Initialize type id
	        classTypeId.copyFrom(SoType.createType(SoError.getClassTypeId(), new SbName("DebugError")));
	    
	        // Set handler to default handler
	        handlerCB = defaultHandlerCB;
	        cbData    = null;
	    }
	    
	   
    //! Sets/returns handler callback for SoDebugError class.
    public static void         setHandlerCallback(SoErrorCB cb, Object data)
        { handlerCB = cb; cbData = data; }
    //! Sets/returns handler callback for SoDebugError class.
    public static SoErrorCB   getHandlerCallback()    { return handlerCB; }
    //! Sets/returns handler callback for SoDebugError class.
    public static Object       getHandlerData()        { return cbData; }

    //! Returns type identifier for SoDebugError class.
    public static SoType       getClassTypeId()        { return new SoType(classTypeId); }

    //! Returns type identifier for error instance
    public SoType      getTypeId() {return new SoType(classTypeId);}

	   
    //! Returns severity of error (for use by handlers)
    public SoDebugError.Severity getSeverity() { return severity; }	   
	   	
	public static void post(String methodName, String formatString) {
		
		 SoDebugError error = new SoDebugError();
		   String str = new String();
		  
		   // Same stuff as in base class
		   //char buf[10000];
		   //va_list ap;
		  
		   //va_start(ap, formatString);
		   //vsprintf(buf, formatString, ap);
		   //va_end(ap);
		  
		   str = "Inventor error in ";
		   str += methodName;
		   str += "(): ";
		   //str += buf;
		   str += formatString;
		  
		   error.severity = Severity.ERROR;
		   error.setDebugString(str/*.getString()*/);
		   error.handleError();
		 
		 }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Posts a warning.
//
// Use: extender

public static void
postWarning(String methodName, String formatString)
//
////////////////////////////////////////////////////////////////////////
{
    SoDebugError        error = new SoDebugError();
    String            str = new String();

    // Same stuff as in base class
//    char        buf[10000];
//    va_list     ap;

//    va_start(ap, formatString);
//    vsprintf(buf, formatString, ap);
//    va_end(ap);

    str  += "Inventor warning in ";
    str += methodName;
    str += "(): ";
    //str += buf;
    str += formatString;

    error.severity = Severity.WARNING;
    error.setDebugString(str/*.getString()*/);
    error.handleError();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Posts an informational (non-error) message.
//
// Use: extender

public static void
postInfo(String methodName, String formatString )
//
////////////////////////////////////////////////////////////////////////
{
    SoDebugError        error = new SoDebugError();
    String            str = new String();

    // Same stuff as in base class
//    char        buf[10000];
//    va_list     ap;
//
//    va_start(ap, formatString);
//    vsprintf(buf, formatString, ap);
//    va_end(ap);

    str  = "Inventor: in ";
    str += methodName;
    str += "(): ";
    //str += buf;
    str += formatString;

    error.severity = Severity.INFO;
    error.setDebugString(str/*.getString()*/);
    error.handleError();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns handler callback (and data) to use for a given instance.
//
// Use: protected, virtual

protected SoErrorCB 
getHandler(final Object[] data)
//
////////////////////////////////////////////////////////////////////////
{
    data[0] = cbData;
    return handlerCB;
}

}
