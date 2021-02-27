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
 |      Defines the SoWriteAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;


////////////////////////////////////////////////////////////////////////////////
//! Writes a scene graph to a file.
/*!
\class SoWriteAction
\ingroup Actions
This class is used for writing scene graphs to files. It contains an
SoOutput instance that by default writes to the standard output.
Methods on the SoOutput can be called to specify what file or
memory buffer to write to.

\par See Also
\par
SoOutput
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoWriteAction extends SoAction {

	public SoType getTypeId() {
		return classTypeId;
	}
    public static SoType getClassTypeId()                              
                                    { return classTypeId; }                   
    public static void addMethod(SoType t, SoActionMethod method)    
                                    { methods.addMethod(t, method); }        
    public static void enableElement(SoType t, int stkIndex)         
                                    { enabledElements.enable(t, stkIndex);}  
    protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;                            
    protected  static SoActionMethodList   methods;                                     
    private static SoType               classTypeId	;
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoWriteAction class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoWriteAction, SoAction);
		  
		  enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
		    methods = new SoActionMethodList(SoAction.methods);                   
		    classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
		                                        new SbName("SoWriteAction"), null);
	   }
	  }
