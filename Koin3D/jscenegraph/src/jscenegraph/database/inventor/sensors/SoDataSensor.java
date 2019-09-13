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
 |      Abstract base class for sensors that attach to some part of a
 |      scene graph and detect changes to it. A data sensor is scheduled
 |      when a change is made to the thing to which it is attached. This
 |      change is detected via the notification mechanism.
 |
 |   Author(s)          : Nick Thompson, Gavin Bell, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.sensors;

import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.nodes.SoNode;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for sensors attached to parts of a scene.
/*!
\class SoDataSensor
\ingroup Sensors
Data sensors detect changes to scene graph objects (paths, nodes, or
fields) and trigger their callback function when the object changes.


Data sensors provide a delete callback that is called just before the
object the data sensor is attached to is deleted; note that the
callback should not attempt to modify the object in any way, or core
dumps may result.


Priority zero data sensors also provide methods that can be called in
the callback function to determine exactly which node, field, or path
caused the sensor to be triggered.

\par See Also
\par
SoNodeSensor, SoPathSensor, SoFieldSensor, SoDelayQueueSensor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoDataSensor extends SoDelayQueueSensor {
	
	private SoSensorCB deleteFunc;
	private Object deleteData;
	private SoBase trigNode;
	private SoField trigField; 
	private SoPath trigPath;
	private boolean doTrigPath;

	 //
	 // Description:
	 // Constructor
	 //
	 // Use: protected
	 
	 public SoDataSensor() {
		 super();
	 
	 //
	 
	  deleteFunc = null;
	  deleteData = null;
	 
	  trigNode = null;
	  trigField = null;
	  trigPath = null;
	  doTrigPath = false;
	 }
	 
	 //
	 // Description:
	 // Constructor with function and data.
	 //
	 // Use: protected
	 
	 public SoDataSensor(SoSensorCB func, Object data) {
	  super(func, data);
	 //
	 
	  deleteFunc = null;
	  deleteData = null;
	 
	  trigNode = null;
	  trigField = null;
	  trigPath = null;
	  doTrigPath = false;
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
    unschedule();
    super.destructor();
}

    //! Sets a callback that will be called when the object the sensor is
    //! sensing is deleted.
// java port
	public void setDeleteCallbakc(SoSensorCB f) {
		setDeleteCallback(f,null);
	}
   public void                setDeleteCallback(SoSensorCB f, Object data)
        { deleteFunc = f; deleteData = data; }

	 
	// Override unschedule() to reset trigNode and trigPath.
	public void unschedule() {
		  super.unschedule();
		    if (trigNode != null) {
		    trigNode = null;
		    }
		    if (trigPath != null) {
		    trigPath.unref();
		    trigPath = null;
		    }
		   		
	}

/**
 * Propagates modification notification through an instance. 
 * By default, this schedules the sensor to be triggered and saves 
 * some information from the notification list for use by the callback function. 
 * Called by SoBase.
 * 
 * @param list
 */
	//
	// Description:
	// Propagates modification notification through an instance. By
	// default, this schedules the sensor to be triggered and saves
	// some information from the notification list. Subclasses may
	// override this method to do more detailed checks of the
	// notification record list before scheduling.
	//
	// Use: protected, virtual
	 
	 public void notify(SoNotList list)
	//
	 {
	  if (trigPath != null)
	  trigPath.unref();
	 
	  // Save node that triggered notification
	  SoNotRec nodeNotRec = list.getFirstRecAtNode();
	  // nodeNotRec may be NULL if a path change causes notification.
	  if (nodeNotRec != null) {
	  trigNode = (SoBase ) nodeNotRec.getBase();
	  }
	  else trigNode = null;
	 
	  // Save field that triggered notification
	  trigField = list.getLastField();
	 
	  // If requested, save path from last node down to trigNode
	  if (doTrigPath && trigNode != null) {
	  SoNotRec rec;
	 
	  trigPath = new SoPath();
	  trigPath.ref();
	 
	  // Find last notification record that has a node
	  for (rec = list.getLastRec();
	  ! rec.getBase().isOfType(SoNode.getClassTypeId());
	  rec = rec.getPrevious())
	  ;
	 
	  // That node is the head of the path
	  trigPath.setHead((SoNode ) rec.getBase());
	 
	  // Add successive nodes (if any) until we get to the one that
	  // triggered the notification
	  if (rec.getBase() != trigNode) {
	  do {
	  rec = rec.getPrevious();
	  trigPath.append((SoNode ) rec.getBase());
	  } while (rec.getBase() != trigNode);
	  }
	  }
	  else
	  trigPath = null;
	 
	  schedule();
	 }
	 
	 /*
	  * This is called when the base (path, field, node, whatever) is deleted. 
	  * All subclasses must implement this to do the right thing. 
	  */
	 public abstract void dyingReference();
	 
	 // Invokes the delete callback. 
	 protected void invokeDeleteCallback() {
		  if (deleteFunc != null)
			    deleteFunc.run(deleteData, this);
			  		 
	 }
	 
	 // Override trigger to reset trigNode and trigPath, if necessary. 
	 public void trigger() {
		  super.trigger();
		    if (trigNode != null) {
		    trigNode = null;
		    }
		    if (trigPath != null) {
		    trigPath.unref();
		    trigPath = null;
		    }
		  		 
	 }
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get the trigger node.  This is available only for immediate
//  sensors (see header for an explanation of why).
//
// Use: public

public SoBase
getTriggerNode() 
//
////////////////////////////////////////////////////////////////////////
{
    if (getPriority() != 0) {
//#ifdef DEBUG
        SoDebugError.postWarning("SoDataSensor::getTriggerNode",
                                  "Sensor priority is not zero"+
                                  " (priority is "+getPriority()+")");
//#endif
        return null;
    }
    return trigNode;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get the trigger path.  This is available only for immediate
//  sensors (see header for an explanation of why).
//
// Use: public

public SoPath 
getTriggerPath() 
//
////////////////////////////////////////////////////////////////////////
{
    if (getPriority() != 0) {
//#ifdef DEBUG
        SoDebugError.postWarning("SoDataSensor::getTriggerPath",
                                  "Sensor priority is not zero"+
                                  " (priority is "+getPriority()+")" );
//#endif
        return null;
    }
    return trigPath;
}

    //! \see getTriggerNode()
    public void                setTriggerPathFlag(boolean flag) { doTrigPath = flag; }
    //! \see getTriggerNode()
    public boolean                getTriggerPathFlag()     { return doTrigPath; }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get the trigger field.  This is available only for immediate
//  sensors (see header for an explanation of why).
//
// Use: public

public SoField 
getTriggerField() 
//
////////////////////////////////////////////////////////////////////////
{
    if (getPriority() != 0) {
//#ifdef DEBUG
        SoDebugError.postWarning("SoDataSensor::getTriggerField",
                                  "Sensor priority is not zero"+
                                  " (priority is "+getPriority()+")" );
//#endif
        return null;
    }
    return trigField;
}

	 
	}
