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
 |      This file contains the definition of the SoInput class.
 |
 |   Classes:
 |      SoInput, SoInputFile (internal)
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jscenegraph.database.inventor.SoDB.SoDBHeaderCB;
import jscenegraph.port.FILE;

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///  Structure: SoInputFile (internal)
///
///  This structure holds info about an opened file for use in the SoInput
///  class.
///
///  One of the items is a dictionary that correlates reference names
///  in files to nodes and paths (SoBase instances).
///
//////////////////////////////////////////////////////////////////////////////

public class SoInputFile {
    String            name;           //!< Name of file
    String            fullName;       //!< Name of file with full path
    FILE                fp;            //!< File pointer
    String          buffer;        //!< Buffer to read from (or NULL)
    int                curBuf;        //!< Current location in buffer
    long              bufSize;        //!< Buffer size
    int                 lineNum;        //!< Number of line currently reading
    boolean                openedHere;     //!< TRUE if opened by SoInput
    boolean                binary;         //!< TRUE if file has binary data
    boolean                readHeader;     //!< TRUE if header was checked for A/B
    public boolean                headerOk;       //!< TRUE if header was read ok
    SbDict              refDict;       //!< Node/path reference dictionary
    boolean                borrowedDict;   //!< TRUE if dict from another SoInput
    float               ivVersion;      //!< Version if standard Inventor file;
    String            headerString;   //!< The header string of the input file
    SoDBHeaderCB        postReadCB;    //!< CB to be called after reading file
    Object                CBData;        //!< User data to pass to the postReadCB

    public SoInputFile() {                      //!< Too complex for inlining
    // Initialize variables:
    name = "";
    fullName = "";
    fp = null;
    buffer = null;
    curBuf = 0;
    openedHere = false;
    binary = false;
    readHeader = false;
    headerOk = false;
    refDict = null;
    borrowedDict = false;
    ivVersion = 0.f;
    headerString = "";
    postReadCB = null;
    CBData = null;

    }

	public void destructor() {
		fp = null;
		refDict = null;
		postReadCB = null;
		CBData = null;
	}
	
	// java port
	public int curBufAsInt() {
		int n = Integer.BYTES;
		byte[] bytes = new byte[n];
		for(int i=0; i<n;i++) {
			bytes[i] = (byte)buffer.charAt(curBuf+i);
		}
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(0);
	}

	// java port
	public long curBufAsLong() {
		int n = Long.BYTES;
		byte[] bytes = new byte[n];
		for(int i=0; i<n;i++) {
			bytes[i] = (byte)buffer.charAt(curBuf+i);
		}
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asLongBuffer().get(0);
	}

	//java port
	public void memcpy(byte[] buf, int curBuf2, int n) {
		for(int i=0;i<n;i++,curBuf2++) {
			buf[i] = (byte)buffer.charAt(curBuf2);
		}
	}
}
