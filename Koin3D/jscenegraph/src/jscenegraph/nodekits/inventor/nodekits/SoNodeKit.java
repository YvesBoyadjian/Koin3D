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
 |      This class is used to initialize all of the nodeKit classes.
 |
 |   Author(s)  : Paul Isaacs
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.details.SoNodeKitDetail;


////////////////////////////////////////////////////////////////////////////////
//! Initializes nodekit classes.
/*!
\class SoNodeKit
\ingroup Nodekits
This class is used to initialize all nodekit classes.

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoCameraKit, SoInteraction, SoLightKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoSeparatorKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodeKit {

	   public
		        //! Initialize all nodekit classes by registering
		        //! them with the database.  This function needs to be called before <em>any</em>
		        //! other nodekit class may be constructed or accessed. Note that 
		        //! this is called automatically by 
		        //! SoInteraction::init(), so if you have made either of 
		        //! these calls, there is no need to call 
		        //! SoNodeKit::init() directly.
		        static void         init() // init all nodeKit classes
		        {
		     // Only initialize once
		        if (! initialized) {
		    
		            // Call SoDB::init();
		            // Okay if it's already been called.
		            SoDB.init();
		    
		            //
		            // initialize the nodekit catalog class
		            //
		            SoNodekitCatalog.initClass();
		    
		            //
		            // initialize all the node classes
		            //
		    
		            // base class
		            SoNodeKitListPart.initClass();
		   
		           // base class
		           SoBaseKit.initClass();
		   
		           SoAppearanceKit.initClass();
		           SoSeparatorKit.initClass();
//		           SoWrapperKit.initClass();
		           SoShapeKit.initClass();
		           SoLightKit.initClass();
		           SoCameraKit.initClass();
		           SoSceneKit.initClass();
		   
		           // The detail class -- Not a node class, but a detail used by them.
		           SoNodeKitDetail.initClass();
		   
		           // Initialize the V1.0 to V2.0 upgrader classes
		           //SoV1NodeKit.init();
		           
		           initialized = true;
		       }
		   		   
		        }
		      private
		        static boolean       initialized;    //!< has nodekit been initialized	
}
