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
 |      Data sensor that is attached to a node in a scene graph. The
 |      sensor is scheduled when a change is made to the node or to any
 |      node below it in the graph.
 |
 |   Author(s)          : Nick Thompson, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;



////////////////////////////////////////////////////////////////////////////////
//! Sensor class that can be attached to Inventor nodes.
/*!
\class SoNodeSensor
\ingroup Sensors
Node sensors detect changes to nodes, calling a callback function
whenever any field of the node or, if the node is a group node, any
children of the node change.

\par See Also
\par
SoFieldSensor, SoPathSensor, SoDataSensor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodeSensor extends SoDataSensor implements Destroyable {
	
	private SoNode node;
	
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNodeSensor() { super();
//
////////////////////////////////////////////////////////////////////////

    node = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor with function and data.
//
// Use: public

public SoNodeSensor(SoSensorCB func, Object data) {
        super(func, data);
//
////////////////////////////////////////////////////////////////////////

    node = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: protected

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    detach();
}

	
	// Makes this sensor detect changes to the given node. 
	public void attach(SoNode nodeToAttachTo) {
		
	     if (node != null)
	    	           detach();
	    	   
	    	       node = nodeToAttachTo;
	    	       node.addAuditor(this, SoNotRec.Type.SENSOR);
	    	  	}
	
	/**
	 * Unschedules this sensor (if it is scheduled) and makes it ignore changes to the scene graph. 
	 */
	//
	   // Description:
	   //    Detaches the sensor if it is attached to a node.
	   //
	   // Use: public
	   	public void detach() {
	     if (node != null) {
	    	           node.removeAuditor(this, SoNotRec.Type.SENSOR);
	    	           node = null;
	    	   
	    	           // If we are scheduled, there's no point leaving it scheduled,
	    	           // since it's not attached any more to whatever caused it to
	    	           // become scheduled.
	    	           unschedule();
	    	       }
	    	  	
	}

	 //
	   // Description:
	   //    This is called by the attached node when it (the node) is about
	   //    to be deleted.
	   //
	   // Use: private
	   
	  	public void dyingReference() {
		
	  	     // We want to detach the sensor if it's still attached to the
	  	       // dying node after we invoke the callback. If the callback
	  	       // attaches to something else, we don't want to detach it. So
	  	       // we'll compare the nodes before and after the callback is
	  	       // invoked and detach only if it's the same one.
	  	   
	  	       SoNode dyingNode = getAttachedNode();
	  	   
	  	       invokeDeleteCallback();
	  	   
	  	       if (getAttachedNode() == dyingNode)
	  	           detach();
	  	  	}
	  	
	  	/**
	  	 * Returns the node that this sensor is sensing, or NULL if it is not attached to any node. 
	  	 * 
	  	 * @return
	  	 */
	  	public SoNode getAttachedNode() {
	  		 return node; 
	  	}
}
