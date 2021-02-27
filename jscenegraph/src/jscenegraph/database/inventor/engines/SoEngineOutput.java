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
 |      This file defines the base SoEngine class, and the
 |      SoEngineOutput class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.coin3d.inventor.engines.SoEngineAble;
import jscenegraph.coin3d.inventor.engines.SoNodeEngine;
import jscenegraph.database.inventor.SoFieldList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;


////////////////////////////////////////////////////////////////////////////////
//! Class for all engine outputs.
/*!
\class SoEngineOutput
\ingroup Engines
SoEngineOuput is the class for all engine output fields.
There is no public constructor routine for this class.  
Only the engine classes create instances of SoEngineOutput


Each engine creates one or more engine outputs.
The type of the output is documented in the engine reference pages.
There is also an SoEngineOutput  method for querying 
the connection type.  


The application can at any time enable or disable the engine outputs.
By default the engine outputs are enabled.

\par See Also
\par
SoEngine
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoEngineOutput {
	
	  boolean enabled;
	  final SoFieldList connections = new SoFieldList();
	  private SoFieldContainer container;
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor.  EngineOutputs may only be constructed inside
//    SoEngine instances.
//
// Use: internal

public SoEngineOutput()
//
////////////////////////////////////////////////////////////////////////
{
    enabled = true;
    container = null;
}

	  // Returns the type of field this output can connect to. 
	  public SoType getConnectionType() {
		  
		     // The connection type is stored in our container's
		       // static EngineOutputData member (because it is the same for
		       // all engine instances).
//		   #ifdef DEBUG
//		       if (container == NULL) {
//		           SoDebugError::post("SoEngineOutput::getConnectionType",
//		                              "container is NULL!");
//		           return SoType::badType();
//		       }
//		   #endif /* DEBUG */
		   
		       SoEngineOutputData od =
		    		   container instanceof SoEngine ?
		    		((SoEngine)container).getOutputData() :
		    	   ((SoNodeEngine)container).getOutputData();
		    		   //container.getOutputData();
		   
//		   #ifdef DEBUG
//		       if (od == NULL) {
//		           SoDebugError::post("SoEngineOutput::getConnectionType",
//		                              "container has no output data!");
//		           return SoType::badType();
//		       }
//		   #endif /* DEBUG */
		   
		       return od.getType(od.getIndex(container, this));
		  	  }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the number of fields this output is writing to, and
//    adds pointers to those fields to the given list:
//
// Use: public

public int
getForwardConnections(SoFieldList list)
//
////////////////////////////////////////////////////////////////////////
{
    int numConnections = 0;

    for (int i = 0; i < connections.getLength(); i++) {
        SoField field = (SoField)connections.operator_square_bracket(i);

        // Skip over converter, if any
        SoFieldContainer container = field.getContainer();
        if (container.isOfType(SoFieldConverter.getClassTypeId()))
            numConnections += ((SoFieldConverter ) container).
                getForwardConnections(list);

        else {
            list.append(field);
            numConnections++;
        }
    }

    return numConnections;
}

	  
//! Enables or disables all connections from this ouptut. If the
//! 	connections are disabled, values will not be output along them.  
//! 	By default, outputs are enabled.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Enables or disables all connections from this
//output. The enabled flag is checked at SO_ENGINE_OUTPUT time and
//at notify() to prevent notification/evaluation through disabled
//outputs.
//
//Use: public

public void                enable(boolean flag) {
    if (enabled != flag) {
        enabled = flag;

        // Notify if re-enabling connections
        if (flag) {

            // A very annoying double notification occurs with engines
            // that enable their outputs during inputChanged that we
            // prevent by not bothering to start notification if we're
            // already in the middle of notification:
            SoEngineAble e = getContainerEngine();
            if (e != null && e.isNotifying()) return;

            for (int j = 0; j < getNumConnections(); j++) {
                SoField f = (this).operator_square_bracket(j);

                if (!f.flags.isEngineModifying) {
                    f.startNotify();
                }
            }
        }
    }
	
}

	  
	  // Returns TRUE if this output is currently enabled. 
	  public boolean isEnabled() {
		  return enabled; 
	  }

	// Returns containing engine. 
	public SoFieldContainer getContainer() {
		 return container; 
	}
	
	// Returns containing engine. 
	public SoEngineAble getContainerEngine() {
		 return (SoEngineAble)container; 
	}
	
	// Returns containing engine. 
	public SoEngine getContainerSoEngine() {
		 return (SoEngine)container; 
	}
	
	/*!
	  Sets the NodeEngine containing this output.

	  \COIN_FUNCTION_EXTENSION

	  \sa getNodeContainer()
	  \since Coin 2.0
	*/

	public void setNodeContainer(SoNodeEngine nodeengine)
	{
	  // FIXME: hack cast to SoEngine. The type of the container member
	  // needs to be SoFieldContainer, not SoEngine.
	  this.container = (nodeengine);
	}

	
	// Adds/removes connection to field. 
	public void addConnection(SoField field) {
	     if (field != null) {
	    	           // Add to lists of connections
	    	           connections.append(field);
	    	   
	    	           // Increment containing engine's reference count
	    	           container.ref();
	    	       }
	    	   
	    	       // This forces the engine to write to the new connection.
	     	if( container instanceof SoEngine) { //java port
	    	       ((SoEngine)container).needsEvaluation = true;
	     	}
	}
	
	 //
	   // Description:
	   //    Called by SoField::disconnect() to remove a connection.
	   //
	   // Use: internal
	   	  
	public void removeConnection(SoField field) {
	     int index = connections.find(field);
//	      #ifdef DEBUG
//	          if (index == -1)
//	              SoDebugError::post("SoEngineOutput::removeConnection",
//	                                 "Field is not connected!");
//	      #endif /* DEBUG */
	      
	          connections.remove(index);
	      
	          // Decrement reference count of containing engine
	          container.unref();
	     
	}
	
	// Before evaluating (which is done with the regular field API), 
	// we must disable notification on all the fields we're about to 
	// write into. After evaluating, the bits are restored: 
	public void prepareToWrite() {
		
		for (int i = connections.getLength()-1; i >= 0; i--) {
			    SoField f = connections.get(i);
//			   #ifdef DEBUG
//			    if (f->flags.isEngineModifying) {
//			    SoDebugError::post("SoEngineOutput::prepareToWrite",
//			    "Internal field flags are wrong; "
//			    "did you call engine->evaluate() "
//			    "instead of engine->evaluateWrapper?");
//			    }
//			   #endif
			    f.flags.isEngineModifying = true;
	    }
			  
	}
	
	//
	 // Description:
	 // Done writing, reset notification flags.
	 //
	 // Use: internal
	 
	public void doneWriting() {
		
		for (int i = connections.getLength()-1; i >= 0; i--) {
		    SoField f = connections.get(i);
		    f.flags.isEngineModifying = false;
		}
			  
	}

    //! Number of connections this output currently has
    int                 getNumConnections() 
        { return connections.getLength(); }

        //! Returns the fields this output is writing into
    public SoField            operator_square_bracket(int i)
        { return connections.get(i); }

    public void setContainer(SoEngine eng)     { container = eng; }

	public void destructor() {
	    // A engine should only be deleted once it has nothing connected
	    // to its outputs.  So, in the DEBUG case, we'll make sure there
	    // are no more connections, and complain if there are.
	//#ifdef DEBUG
	    if (getNumConnections() != 0)
	        SoDebugError.post("SoEngineOutput::~SoEngineOutput",
	                           "Engine with output connections deleted.\n"+
	                           "Did you unref() an engine that you didn't ref()?");
	//#endif /* DEBUG */
	}    
}
