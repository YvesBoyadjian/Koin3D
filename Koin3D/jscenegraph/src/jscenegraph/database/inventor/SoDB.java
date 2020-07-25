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
 |      Defines the SoDB class
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.inventor.VRMLnodes.Init;
import jscenegraph.coin3d.inventor.annex.profiler.elements.SoProfilerElement;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.inventor.threads.SbRWMutex;
import jscenegraph.coin3d.shaders.SoShader;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.engines.SoEngine;
import jscenegraph.database.inventor.engines.SoFieldConverter;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoGlobalField;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.upgraders.SoUpgrader;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoSensorCB;
import jscenegraph.database.inventor.sensors.SoSensorManager;
import jscenegraph.database.inventor.sensors.SoTimerSensor;


////////////////////////////////////////////////////////////////////////////////
//! Scene graph database class.
/*!
\class SoDB
\ingroup General
The SoDB class holds all scene graphs, each representing a 3D
scene used by an application. A scene graph is a collection of SoNode
objects which come in several varieties (see SoNode).
Application programs must initialize the database by calling
SoDB::init()
before calling any other database routines and before constructing any
nodes, paths, functions, or actions. Note that 
SoDB::init() is called by 
SoInteraction::init(),
and SoNodeKit::init(), so if you are calling any of these
methods, you do not need to call 
SoDB::init() directly.
All methods on this class are static.


Typical program database initialization and scene reading is as follows:
<tt>\code
#include <Inventor/SoDB.h>
#include <Inventor/SoInput.h>
#include <Inventor/nodes/SoSeparator.h>
SoSeparator  *rootSep;
SoInput      in;
SoDB::init();
rootSep = SoDB::readAll(&in);
if (rootSep == null)
printf("Error on read...\n");
...\
\endcode
</tt>

\par See Also
\par
SoBase, SoNode, SoEngine, SoField, SoInput, SoFile, SoPath, SoOneShotSensor, SoDataSensor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDB {
	
	public static final String VRML1_HEADER = "#VRML V1.0 ascii";
	public static final String VRML2_HEADER = "#VRML V2.0 utf8";

	public interface SoDBHeaderCB {
		void run(Object userData, SoInput in);
	}


// Internal class for storing headers and the corresponding 
// callback functions.
	private static class SoDBHeaderData {
    String        headerString;
    boolean            isBinary;
    float           ivVersion;
    SoDBHeaderCB    preCB;
    SoDBHeaderCB    postCB;
    Object            userData;    
};

	
	/**
	 * The thread used to run JSceneGraph
	 */
	private final Thread soDBThread;
	
	 /////////////////////////////////////////////////////////////////////////////
	    //
	    // This class exists only for the "realTime" global field. It is
	    // exactly the same as SoSFTime in all respects except one: it
	    // redefines the connectionStatusChanged() method, allowing the global
	    // database to keep track of connections to realTime. This allows SoDB
	   // to disable the time-update sensor when nothing is connected to the
	   // realTime global field, improving performance.
	   //
	   // Note that this class does not set up a run-time type id; it is
	   // inherited as is from SoSFTime, meaning that realTime is treated
	   // just like any other SoSFTime field.
	   //
	   
	   private static class SoSFRealTime extends SoSFTime {
	     public SoSFRealTime() {
	    	  totalNumConnections = 0;
	     }
	     public void destructor() {	    	 
	    	 super.destructor();
	     }
	   
	     public void connectionStatusChanged(int numConnections) {
	         // Determine if realTime is connected to anything.
	    	       totalNumConnections += numConnections;
	    	   
	    	       // Enable sensor if we have any connections:
	    	       SoDB.enableRealTimeSensor((totalNumConnections > 0));
	    	  	     }
	   
	       int totalNumConnections;
	   };
	   
	  	
  private
    static final String   versionString  = "SGI Open Inventor 2.1 Beta"; //!< Returned by getVersion()
	
	// This is the famous global database that you hear so much about	
	private static SoDB globalDB = null; 
	
	private final SoSensorManager sensorManager = new SoSensorManager(); 
	
	// This keeps track of the number of current notifications. When this
	// is decremented to 0, all immediate idle queue sensors are triggered.
	private static int notifyCount = 0;

    private static SoTimerSensor realTimeSensor;//!< touches realTime global field 
    private  static SoSensorCB         realTimeSensorCallback = new SoSensorCB() {

    	 ////////////////////////////////////////////////////////////////////////
    	   //
    	   // Description:
    	   //      Callback to update the realTime global field.
    	   //
    	   // Use: private
    	   
    	  		@Override
		public void run(Object data, SoSensor sensor) {
			 realTime.setValue(SbTime.getTimeOfDay());
		}
    	
    };
     private static SoSFRealTime realTime;
 	
    //! List of valid header strings, and their corresponding callbacks
    @SuppressWarnings("unused")
	private  static SbPList      headerList;
  	
    //! This dictionary stores field conversion engine types. The key
      //! is created by mangling the types of the two fields to convert
      //! from. The dictionary maps the key to the type of the conversion
      //! engine.
 	private static SbDict conversionDict;
	
    //! Returns a conversionDict key from the two given field type id's.
 	private static int getConversionKey(SoType fromField, SoType toField) {
		return (fromField.getKey() << 16) | toField.getKey(); 
	}
	
	/**
	 * Returns the global field with the given name, 
	 * or null if there is none. 
	 * The type of the field may be checked using the 
	 * SoField.isOfType(), SoField.getClassTypeId(), 
	 * and SoField.getTypeId() methods. 
	 * 
	 * @param name
	 * @return
	 */
	public static SoField getGlobalField(final String name) {
		return getGlobalField(new SbName(name));
	}
	public static SoField getGlobalField(final SbName name) {
	     SoGlobalField result = SoGlobalField.find(name);
	          if (result == null) return null;
	      
	          return result.getMyField();
	     		
	}
	
	/**
	 * This sets the timeout value for sensors that are delay queue sensors 
	 * (one-shot sensors, data sensors). 
	 * Delay queue sensors are triggered whenever there is idle time. 
	 * If a long period of time elapses without any idle time 
	 * (as when there are continuous events to process), 
	 * these sensors may not be triggered. 
	 * Setting this timeout value ensures that if the specified length of time elapses 
	 * without any idle time, the delay queue sensors will be processed anyway. 
	 * 
	 * @param t
	 */
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //      Set the delay queue sensor timeout.
	   //
	   // Use: public, static
	   
	   public static void
	   setDelaySensorTimeout(final SbTime t)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       getSensorManager().setDelaySensorTimeout(t);
	   }
	   	
	   /**
	    * Private constructor
	    */
	   private SoDB() {
		   soDBThread = Thread.currentThread();
	   }
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//      Get the delay queue sensor timeout.
//
// Use: public, static

public SbTime 
getDelaySensorTimeout()
//
////////////////////////////////////////////////////////////////////////
{
    return getSensorManager().getDelaySensorTimeout();
}

public
    //! Registers a field conversion engine that can be used to
    //! convert from one type of field to another. The type id's of the
    //! two fields are passed in, as is the type id of the field
    //! converter engine (derived from SoFieldConverter).
////////////////////////////////////////////////////////////////////////
//
//Description:
//Registers a field conversion engine that can be used to
//convert from one type of field to another. The type id's of the
//two fields are passed in, as is the type id of the field
//converter engine (derived from SoFieldConverter).
//
//Use: extender, static

    static void         addConverter(SoType fromField, SoType toField,
                                     SoType converterEngine) {
	
//#ifdef DEBUG
    // Make sure the converter is of the correct type
    if (! converterEngine.isDerivedFrom(SoFieldConverter.getClassTypeId())) {
        SoDebugError.post("SoDB::addConverter",
        "class \""+converterEngine.getName().getString()+"\" is not derived from SoFieldConverter"
                           );
        return;
    }
//#endif

    conversionDict.enter(getConversionKey(fromField, toField),
                          converterEngine);
}

	
	/**
	 * Returns the field conversion engine registered for the two 
	 * given field types. 
	 * If no such engine exists, SoType.badType() is returned. 
	 * 
	 * @param fromField
	 * @param toField
	 * @return
	 */
	public static SoType getConverter(SoType fromField, SoType toField) {
	     Object[] typePtr = new Object[1];
	     
	          if (conversionDict.find(getConversionKey(fromField, toField), typePtr))
	              return new SoType((SoType)typePtr[0]);
	      
	          return new SoType(SoType.badType());
	     
	}
	
	// Accesses sensor manager. 
	public static SoSensorManager getSensorManager() {
		return globalDB.sensorManager;
	}

    //! Returns TRUE if database is initialized (for error checking)
    public static boolean         isInitialized() { return (globalDB != null); }

	
	/**
	 * This is called when some instance begins or ends a notification process.
	 * It increments or decrements a counter of notifications in progress.
	 * When the counter reaches 0, all priority 0 (immediate) delay queue sensors are triggered.
	 * By doing this, all notification has a chance to finish before any evaluation 
	 * (due to data sensors, primarily) takes place. 
	 */
	public static void startNotify() {
		
		if( Thread.currentThread() != globalDB.soDBThread) {
			throw new IllegalStateException("Not in SoDB thread");
		}
		
		
		 notifyCount++; 
	}
	
	public static void endNotify() {
		--notifyCount;
		 if (notifyCount == 0)
			 globalDB.sensorManager.processImmediateQueue(); 		
	}

	// Enables/disables realTime sensor processing. 
	public static void enableRealTimeSensor(boolean enable) {
	     if (enable && realTimeSensor.getInterval().operator_not_equal(SbTime.zero())) {
	    	   
	    	           // If we are enabling the sensor now, call the callback once
	    	           // to set the current time. Since the sensor may become
	    	           // enabled because of a new connection, the realTime field may
	    	           // be queried before the sensor callback is called. By calling
	    	           // it now, the realTime field will contain the current time in
	    	           // this case. However, we have to disable notification on the
	    	           // realTime field since we area already in the process of
	    	           // notifying.
	    	           boolean wasEnabled = realTime.enableNotify(false);
	    	           realTimeSensorCallback.run(null, null);
	    	           realTime.enableNotify(wasEnabled);
	    	           realTimeSensor.schedule();
	    	       }
	    	       else
	    	           realTimeSensor.unschedule();
	    	  		
	}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Reads a graph from the file specified by the given SoInput,
	   //    returning a pointer to the resulting root node in rootNode.
	   //    Returns FALSE on error.
	   //
	   // Use: public, static
	   
	   public static boolean
	   read(SoInput in, SoNode[] rootNode)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
    final SoBase[]      base = new SoBase[1];
    boolean        ret;

    ret = read(in, base);

    if (base[0] != null) {
        if (base[0].isOfType(SoNode.getClassTypeId()))
            rootNode[0] = (SoNode ) base[0];

        else {
            SoReadError.post(in, "looking for a node but got "+base[0].getTypeId().getName());
            ret = false;
            
            // Free the scene we just read by refing/unrefing
            base[0].ref();
            base[0].unref();
        }
    }

    else
        rootNode[0] = null;

    return ret;
	   } 	


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads an SoBase from the file specified by the given SoInput,
//    returning a pointer to the resulting instance in base. Returns
//    FALSE on error.
//
// Use: private, static

private static boolean read(SoInput in, final SoBase[] base)
//
////////////////////////////////////////////////////////////////////////
{
    boolean        ret;
    String dataFileName;
    String searchPath = null;

//#ifdef DEBUG
    if (globalDB == null) {
        SoDebugError.post("SoDB::read", "SoDB::init() was never called");
        return false;
    }
//#endif /* DEBUG */

    // Before reading, see if the SoInput is reading from a named
    // file. If so, make sure the directory search path in the SoInput
    // is set up to read from the same directory the file is in.
    dataFileName = in.getCurFileName();

    if (dataFileName != null) {
        int slashPtr;

        // Set up the directory search stack if necessary. Look for
        // the last '/' in the path. If there is none, there's no
        // path. Otherwise, remove the slash and everything after it.
        FileSystem fs = FileSystems.getDefault();
        Path path = fs.getPath(dataFileName);
        searchPath = path.getParent().toString();
        
        if (!searchPath.isEmpty()) {
            SoInput.addDirectoryFirst(searchPath);
        }
    }

    ret = SoBase.read(in, base, SoBase.getClassTypeId());

    // If no valid base was read, but we haven't hit EOF, that means
    // that there's extra crap in the input that's not an Inventor
    // thing. If so, report an error.
    if (ret && base == null && ! in.eof()) {
        final char[]    c = new char[1];
        in.get(c);
        SoReadError.post(in, "Extra characters ('"+c[0]+"') found in input");
        ret = false;
    }

    // Clean up directory list if necessary
    if (searchPath != null) {
        SoInput.removeDirectory(searchPath);
        //free(searchPath); java port
    }

    return ret;
}

    //! Reads all graphs and paths from the file specified by the given SoInput.  If
    //! there is only one graph in the file and its root is an
    //! SoSeparator, a pointer to the root is returned. In all other
    //! cases, this creates an SoSeparator, adds the root nodes of all
    //! graphs read as children of it, and returns a pointer to it. This
    //! returns NULL on error. This processes directory paths in the same way
    //! as the other reading routines.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Reads all graphs from the file specified by the given SoInput.
//If there is only one graph in the file and its root is an
//SoSeparator, a pointer to the root is returned. Otherwise, this
//creates an SoSeparator, adds the root nodes of all graphs read
//as children of it, and returns a pointer to it. 
//
//If a graph is a path, it will be read and its root returned.
//
//This returns NULL on error.
//
//Use: public, static

    public static SoSeparator  readAll(SoInput in) {
    final SoBase[]      base = new SoBase[1];
    SoSeparator root = new SoSeparator();

    root.ref();

    // Keep on reading until there are no more graphs to read
    do {
        if (! read(in, base)) {
            root.unref();
            return null;
        }
        else if (base[0] != null) {
            // Did we read a node or a path?
            if (base[0].isOfType(SoNode.getClassTypeId()))
                root.addChild((SoNode ) base[0]);
            else if (base[0].isOfType(SoPath.getClassTypeId())) {
                SoNode pathHead = ((SoPath ) base[0]).getHead();
                if (pathHead != null) {
                    pathHead.ref();
                    root.addChild(pathHead);
                    pathHead.unref();
                }
            }
        }
    } while (base[0] != null);

    // If only one graph was read, and it had a separator for a root,
    // get rid of the one we created
    if (root.getNumChildren() == 1 &&
        root.getChild(0).isOfType(SoSeparator.getClassTypeId())) {

        SoSeparator graphRoot = (SoSeparator ) root.getChild(0);
        graphRoot.ref();
        root.unref();
        root = graphRoot;
    }

    root.unrefNoDelete();
    return root;
    	
    }


	   
    //! Initializes the database.  This must be called before calling any
    //! other database routines, including the construction of any nodes,
    //! paths, engines, or actions.
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes the global database. After this call, the (private)
//    global variable "globalDB" refers to the global database. This
//    sets up all standard nodes, actions, elements, etc..
//
// Use: public, static

	   // java port
	   @SuppressWarnings("unused")
	private static Object realTimeGlobalField; // to avoid garbage collection
	   
public static void
init()
//
////////////////////////////////////////////////////////////////////////
{
    // Initialize global DB only once.
    if (globalDB == null) {
    	
        globalDB = new SoDB();

        SoDBP.globalmutex = new SbRWMutex(SbRWMutex.Precedence.READ_PRECEDENCE);
        
        // Initialize the runtime type system
        SoType.init();

        // Setup for file reading (initializes list of directories to
        // search in).
        SoInput.init();

        // Set up field conversion dictionary
        conversionDict = new SbDict();
        
        //
        // Initialize all standard classes. The significant ordering
        // rules are:
        //      actions must be done before nodes
        //      elements must be done before actions
        //
        //SoCatch.initClass();
        SoBase.initClass();
        SoFieldContainer.initClass();
        SoPath.initClass();
        SoGlobalField.initClass();

        SoError.initClasses();
        SoElement.initElements();

        // The profiler-elements must also be initialized before actions, of
        // course. (Note that at least the first one *must* be initialized
        // even if COIN_PROFILER is not set, as we use its classStackIndex
        // when checking if its present on the state stack.)
        SoProfilerElement.initClass(SoProfilerElement.class);

        SoAction.initClasses();
        SoNode.initClasses();
        SoField.initClasses();
        SoEngine.initClasses();
        SoEvent.initClasses();
        SoDetail.initClasses();
        
        SoGLDriverDatabase.init();
        SoGLImage.initClass();
        SoGLBigImage.initClass();        

        SoShader.init(); // COIN 3D        

        // FIXME: probably temporary. Add FXViz::init() or something? pederb, 2007-03-09
        SoShadowGroup.init(); // COIN 3D
        
        Init.so_vrml_init();
        
        SoUpgrader.initClasses();
        
        // Create the header list, and register valid headers we know about
        headerList = new SbPList();
        SoDB.registerHeader(SoOutput.getDefaultASCIIHeader(),  
                                false, 2.1f,
                                null, null, null);
        SoDB.registerHeader(SoOutput.getDefaultBinaryHeader(), 
                                true, 2.1f,
                                null, null, null);
        SoDB.registerHeader("#Inventor V2.0 ascii",  
                                false, 2.0f,
                                null, null, null);
        SoDB.registerHeader("#Inventor V2.0 binary", 
                                true, 2.0f,
                                null, null, null);
        SoDB.registerHeader("#Inventor V1.0 ascii",  
                                false, 1.0f,
                                null, null, null);
        SoDB.registerHeader("#Inventor V1.0 binary", 
                                true,  1.0f,
                                null, null, null);
                                    
        // For now, treat VRML files as Inventor 2.1 files.
        // In the future, we might want to verify that the VRML file
        // contains strictly VRML nodes, i.e. any Inventor (non-VRML)
        // nodes in the file generate read warnings.
        SoDB.registerHeader(VRML1_HEADER,  
                                false, 2.1f,
                                null, null, null);
        
        // YB : Let us give a chance to VRML V2.0
        SoDB.registerHeader(VRML2_HEADER,  
                false, 2.1f,
                null, null, null);

        // Create realTime global field. We have to bypass the
        // standard createGlobalField stuff because there is no
        // specific typeId info for SoSFRealTime.
        realTime = new SoSFRealTime();
        // Construct a global field (to add it to the dictionary); we
        // don't actually need a pointer to it.
        realTimeGlobalField = new SoGlobalField(new SbName("realTime"), realTime);
        realTime.setValue(SbTime.getTimeOfDay());
        realTime.getContainer().ref();

        // And setup the sensor to touch it periodically
        realTimeSensor = new SoTimerSensor();
        realTimeSensor.setFunction((SoSensorCB )
                                     SoDB.realTimeSensorCallback);
//#ifdef DEBUG
//        if (SoDebug.GetEnv("IV_DEBUG_SENSORS")) {
//            SoDebug.NamePtr("realTimeSensor", realTimeSensor);
//        }
//#endif

        // This doesn't have to be scheduled very often, because
        // the SoXt viewers update realTime right after a redraw, so
        // anything that is continuously animating will constantly
        // redraw as fast as either swapbuffers() or the redraw (if
        // single-buffered) can occur.
        // I chose 1/12 of a second because it is a multiple of the
        // two most common vide rates-- 60 HZ and 72 HZ
        realTimeSensor.setInterval(new SbTime(1.0/12.0));

        // Don't schedule the sensor until something is connected to
        // realTime. If nothing is connected, there's no sense wasting
        // time triggering the sensor.

        // Initialize delay queue timeout to 1/12th of a second to
        // make sure that redraws occur even when event processing is
        // time-consuming.
        setDelaySensorTimeout(new SbTime(1.0/12.0));
    }

}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Registers callbacks to be invoked when a particular header
//    string is found.  Returns FALSE if the string is not a valid header
//    string. Returns TRUE if the header is successfully registered.
//    Note, nothing prevents you from registering the same string multiple
//    times.

//
// Use: public, static

public static boolean registerHeader(final String header, boolean isBinary, float ivVersion,
                    SoDBHeaderCB preCB, SoDBHeaderCB postCB, Object userData)
//
////////////////////////////////////////////////////////////////////////
{
    // Header string cannot be greater than 80 characters in length,
    // and must have at least one character beyond the initial comment char.
    int headerLength = header.length();
    if (headerLength > 80 || headerLength < 2)
        return (false);
        
    // The first character must be the comment character
    String string = header;
    if (string.charAt(0) != '#')
        return (false);
        
    // The string must not contain any newline characters.
    for (int i = 1; i < header.length(); i++)
        if (string.charAt(i) == '\n')
            return (false);
         
    SoDBHeaderData data = new SoDBHeaderData();
    
    // Binary headers *must* be padded for correct alignment, but to make things 
    // simpler when looking up headers, we'll just pad all headers - including 
    // ascii ones.
    data.headerString = SoOutput.padHeader(header);
    
    data.isBinary = isBinary;
    data.ivVersion = ivVersion;
    data.preCB = preCB;
    data.postCB = postCB;
    data.userData = userData;
    headerList.append(data);
     
    return (true);
}


    //! Returns a character string identifying the version of the Inventor
    //! library in use.
   public static String   getVersion() {
	   return versionString;
   }


    //! The database maintains a namespace for global fields, making sure that
    //! there is at most one instance of a global field with any given name in
    //! the database. This routine is used to create new global fields.  If
    //! there is no global field with the given name, it will create a new
    //! global field with the given name and type. If there is already a
    //! global field with the given name and type, it will return it. If there
    //! is already a global field with the given name but a different type,
    //! this returns null.
    //! 
    //! 
    //! All global fields must be derived from SoField; typically the result
    //! of this routine is cast into the appropriate type; for example:
    //! \code
    //! SoSFInt32 *longField =
    //!     (SoSFInt32 *) SoDB::createGlobalField("Frame",
    //!                                          SoSFInt32::getClassTypeId());
    //! \endcode
    public static SoField     createGlobalField(final SbName name,
                                          SoType type) {
    final boolean[] alreadyExists = new boolean[1];
    SoGlobalField result = SoGlobalField.create(name, type, alreadyExists);

    if (result == null)
        return null;
    
    return result.getMyField();
    	
    }

    //! Renames the global field named \p oldName.  Renaming a global field to
    //! an empty name ("") deletes it.  If there is already a global field
    //! with the new name, that field will be deleted (the
    //! getGlobalField method
    //! can be used to guard against this).
    public static void         renameGlobalField(final SbName oldName,
                                          final SbName newName) {
        if (oldName.operator_equals(newName)) return;
        
        SoGlobalField oldGlobalField = SoGlobalField.find(oldName);
        if (oldGlobalField == null) return;
        
        if (newName.operator_equals(new SbName(""))) {
            oldGlobalField.unref();
            return;
        }
        
        SoGlobalField newGlobalField = SoGlobalField.find(newName);
        if (newGlobalField != null) {
            newGlobalField.unref();
        }

        oldGlobalField.changeName(newName);
    }

    //! The database automatically creates one global field when
    //! SoDB::init()
    //! is called.  The \b realTime  global field, which is of type
    //! SoSFTime, can be connected to engines and nodes for real-time
    //! animation.  The database will automatically update the \b realTime  global
    //! field 12 times per second, using a timer sensor.  Typically, there
    //! will be a node sensor on the root of the scene graph which schedules a
    //! redraw whenever the scene graph changes; by updating the \b realTime 
    //! global field periodically, scene graphs that are connected to
    //! \b realTime  (and are therefore animating) will be redrawn.  The rate
    //! at which the database updates \b realTime  can be controlled with this
    //! routine.  Passing in a zero time will disable automatic update of
    //! \b realTime . If there are no enabled connections from the
    //! \b realTime  field to any other field, the sensor is automatically
    //! disabled.
    //! .p
    //! Note that the SoSceneManager class automatically updates
    //! realTime immediately after redrawing, which will result in as high a
    //! frame rate as possible if the scene is continuously animating.  The
    //! SoDB::setRealTimeInterval method ensures that engines that do not
    //! continuously animate (such as SoTimeCounter) will eventually be
    //! scheduled.
    public static void         setRealTimeInterval(final SbTime deltaT) {
    if (deltaT.operator_equal(SbTime.zero())) {
        realTimeSensor.setInterval(deltaT);
        realTimeSensor.unschedule();
    }
    else {
        realTimeSensor.setInterval(deltaT);
        realTimeSensor.schedule();
    }    	
    }
    //! Returns how often the database is updating \b realTime .
    public static SbTime getRealTimeInterval() {
    	return realTimeSensor.getInterval();
    }
 
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Look up the given header string in our list of registered 
//    headers, and pass back the data associated with that header i.e.
//    pointers to the pre- and post- callbacks, the user data, and
//    a boolean indicating whether it's binary or ascii. 
//    The return value is true if the string is found in the list of
//    registered headers. 
//    If the substringOK flag is TRUE, then we also look for registered
//    headers that are substrings of the given header string. 
//
// Use: public, static

public static boolean getHeaderData(final String header, 
                    final boolean[] isBinary, final float[] ivVersion,
                    final SoDBHeaderCB[] preCB, final SoDBHeaderCB[] postCB, 
                    final Object[] userData, boolean substringOK)
//
////////////////////////////////////////////////////////////////////////
{    
    int whichHeader = -1;
    SoDBHeaderData data;
    String paddedHeader = SoOutput.padHeader(header);
    
    // First look for an exact match
    for (int i = headerList.getLength()-1; i >= 0 && whichHeader == -1; i--) {
        data = (SoDBHeaderData ) (headerList).operator_square_bracket(i);
        String registeredString = data.headerString;
        
        if (paddedHeader.equals(registeredString)) {
                whichHeader = i;
                
        } 
    }
    
    // If we didn't find an exact match,
    // look for a substring that is a valid registered string
    if (whichHeader == -1 && substringOK) {
    
        for (int i = headerList.getLength()-1; i >= 0 && whichHeader == -1; i--) {
            data = (SoDBHeaderData ) (headerList).operator_square_bracket(i);
            String registeredString = data.headerString;     
        
            if (paddedHeader.length() >= registeredString.length()) {
            
                // See how much padding there is in the registered header string
                String registeredStr = data.headerString;
                int lastNonPadChar = registeredString.length() - 1;
                while (registeredStr.charAt(lastNonPadChar) == ' ' && lastNonPadChar > 0) 
                    lastNonPadChar--;
                
                // Is the registered header (minus the padding) a substring 
                // of the the given header string?
                if (registeredString.substring(0, lastNonPadChar-1).equals(
                        paddedHeader.substring(0, lastNonPadChar-1))) {
                    whichHeader = i;                    
                }
            }       
        }
    }
   
    if (whichHeader == -1) {
        isBinary[0] = false;
        ivVersion[0] = -1;
        preCB[0] = null;
        postCB[0] = null;
        userData[0] = null;
        return (false);
    }
    
    data = (SoDBHeaderData ) (headerList).operator_square_bracket(whichHeader);
    isBinary[0] = data.isBinary;
    ivVersion[0] = data.ivVersion;
    preCB[0] = data.preCB;
    postCB[0] = data.postCB;
    userData[0] = data.userData;   
    return (true);      
}

// Note that the function names of the next four functions below are
// all lowercase to be compatible with client code written on TGS
// Inventor.

/*!

  Places a read lock on the global SoDB mutex. This can be used to
  synchronize between threads that are reading/writing Coin scene
  graphs.

  If you call this function, you must make sure that you also call
  SoDB::readunlock(). If you fail to do this, you might experience
  that your application locks up.

  All Coin actions has a read-lock on the global SoDB mutex while
  traversing the scene graph.

  \sa SoDB::readunlock(), SoDB::writelock()

  \since Coin 2.3
  \since TGS Inventor 3.0
*/
public static void
readlock()
{
//#ifdef COIN_THREADSAFE
  SoDBP.globalmutex.readLock();
//#endif // COIN_THREADSAFE
}

/*!
  Unlocks the read lock on the global SoDB mutex.

  \sa SoDB::readlock()
  \since Coin 2.3
  \since TGS Inventor 3.0
*/
public static void
readunlock()
{
//#ifdef COIN_THREADSAFE
  SoDBP.globalmutex.readUnlock();
//#endif // COIN_THREADSAFE
}

/*!
  Places a write lock on the global SoDB mutex. This can be used to
  prevent that the scene graph is read or traversed while you modify
  the scene graph.

  If you call this function, you must make sure that you also call
  SoDB::writeunlock(). If you fail to do this, you might experience
  that your application locks up.

  \sa SoDB::readlock()
  \since Coin 2.3
  \since TGS Inventor 3.0
*/
public static void
writelock()
{
//#ifdef COIN_THREADSAFE
  SoDBP.globalmutex.writeLock();
//#endif // COIN_THREADSAFE
}

/*!
  Unlocks the write lock on the global SoDB mutex.

  \sa SoDB::writelock()
  \since Coin 2.3
  \since TGS Inventor 3.0
*/
public static void
writeunlock()
{
//#ifdef COIN_THREADSAFE
  SoDBP.globalmutex.writeUnlock();
//#endif // COIN_THREADSAFE
}

    
}
