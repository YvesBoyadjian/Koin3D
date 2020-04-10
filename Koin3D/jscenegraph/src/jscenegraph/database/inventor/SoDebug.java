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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Internal debugging helper routines.  This entire class is #ifdef
 |      DEBUG, it is not part of the optimized library.
 |
 |   Classes:
 |      SoDebug
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;


//!
//! List of environment variables for tracing:
//!
//!   IV_DEBUG_BUFLEN   : Number of lines RTPrintf saves up before
//!      dumping out.  Defaults to 100, set to 1 to get it to dump
//!      after every line.
//!   IV_DEBUG_SENSORS  : will print out info as sensors are
//!      triggered/etc
//!   IV_DEBUG_CACHES   : print out info on cache validity, etc.
//!   IV_DEBUG_CACHELIST : print out info on Separator render caches.
//!

/**
 * @author Yves Boyadjian
 *
 */
public class SoDebug {
	
	private static SbDict envDict = null;
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Fast version of getenv(), stores environment strings in an
//    SbDict for fast lookup (the standard getenv() does a linear
//    search through your whole environment).
//
// Use: public 

public static String
GetEnv(String envVar)
//
////////////////////////////////////////////////////////////////////////
{
    if (envDict == null) envDict = new SbDict();

    final SbName name = new SbName(envVar);
    String key = name.getString();

    final Object[] value = new Object[1];

    // Try looking in the dictionary first...
    if (!envDict.find(key, value)) {
        value[0] = (Object)System.getenv(envVar);
        if(value[0] == null) {
        	value[0] = System.getProperty(envVar);
        }
        envDict.enter(key, value[0]);
    }

    return (String)value[0];
}


public static void RTPrintf(String string) {
	System.err.println(string);
}

	private static SbDict ptrNameDict = null;


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Give a pointer a name.  This can be extremely useful when trying
//    to track objects through queues, etc...  Uses an SbDict so this
//    is fast...
//
// Use: public 

public static void NamePtr(String name, Object ptr)
//
////////////////////////////////////////////////////////////////////////
{
    if (ptrNameDict == null) ptrNameDict = new SbDict();

    // Ignore const-cast-away warning
    ptrNameDict.enter(ptr, (Object)name);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Return the name of a pointer named with NamePtr.
//
// Use: public 

public static String PtrName(Object ptr)
//
////////////////////////////////////////////////////////////////////////
{
    if (ptrNameDict == null) ptrNameDict = new SbDict();

    final Object[] value = new Object[1];
    if (!ptrNameDict.find(ptr, value))
        return "<noName>";
    return (String)value[0];
}



}
