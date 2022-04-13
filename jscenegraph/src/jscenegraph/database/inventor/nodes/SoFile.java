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
 |   $Revision $
 |
 |   Description:
 |      This file defines the SoFile node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoSensorCB;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipError;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node that reads children from a named file.
/*!
\class SoFile
\ingroup Nodes
This node represents a subgraph that was read from a named input file.
When an SoFile node is written out, just the field containing the
name of the file is written; no children are written out. When an
SoFile is encountered during reading, reading continues from the
named file, and all nodes read from the file are added as hidden
children of the file node.


Whenever the \b name  field changes, any existing children are removed
and the contents of the new file is read in. The file node remembers
what directory the last file was read from and will read the new file
from the same directory after checking the standard list of
directories (see SoInput), assuming the field isn't set to an
absolute path name.


The children of an SoFile node are hidden; there is no way of
accessing or editing them. If you wish to edit the contents of an
SoFile node, you can modify the contents of the named file and
then "touch" the \b name  field (see SoField). Alternatively,
you can use the
copyChildren() method
to get a editable copy of the file node's children. Note that this
does not affect the original file on disk, however.

\par File Format/Default
\par
\code
File {
  name <Undefined file>
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoHandleEventAction
<BR> Traverses its children just as SoGroup does. 
\par
SoRayPickAction
<BR> Traverses its hidden children, but, if intersections are found, generates paths that end at the SoFile node. 
\par
SoWriteAction
<BR> Writes just the \b name  field and no children. 

\par See Also
\par
SoInput, SoPath
*/
////////////////////////////////////////////////////////////////////////////////

public class SoFile extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFile.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFile.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFile.class); }    
	  
	  static boolean searchok = false;

  public
    //! \name Fields
    //@{

    //! Name of file from which to read children.
    final SoSFString          name = new SoSFString();           

  private
    SoChildList         children;

    //! These keep the image and filename fields in sync.
    private SoFieldSensor      nameChangedSensor;
    
    //! Creates a file node with default settings.
    public SoFile() {
    	children = new SoChildList(this);
        nodeHeader.SO_NODE_CONSTRUCTOR(/*SoFile.class*/);
        nodeHeader.SO_NODE_ADD_FIELD(name,"name", ("<Undefined file>"));

        // Set up sensors to read in the file when the filename field
        // changes.
        // Sensors are used instead of field to field connections or raw
        // notification so that the fields can still be attached to/from
        // other fields.
        nameChangedSensor = new SoFieldSensor( new SoSensorCB() {

			@Override
			public void run(Object data, SoSensor sensor) {
				nameChangedCB(data,sensor);
			}
        	
        }
        		, this);
        nameChangedSensor.setPriority(0);
        nameChangedSensor.attach(name);

        readOK = true;
        isBuiltIn = true;
    	
    }

    public void destructor() {
    	nameChangedSensor.destructor();
    	super.destructor();
    }
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads into instance of SoFile. Returns FALSE on error.
//
// Use: protected

public boolean readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    // Detach sensor temporarily
    nameChangedSensor.detach();

    // Read field info as usual.
    if (! super.readInstance(in, flags))
        readOK = false;

    // If file name is default, there's a problem, since the default
    // file name is not a valid one
    else if (name.isDefault()) {
        SoReadError.post(in, "\"name\" field of SoFile node was never set");
        readOK = false;
    }
    else {
        // Call nameChangedCB to read in children.  There is a really
        // cool bug that occurs if we let the sensor do this for us.
        // The sensor is called right after notification, in
        // processImmediateQueue.  It would then call nameChanged,
        // which calls SoDB::read, which sets up the directory search
        // path.  If there is another File node in that directory
        // search path, its name field will be set, but, since we are
        // already in the middle of a processImmediateQueue, its field
        // sensor isn't called right away.  The SoDB::read returns,
        // removing the directory it added to the search path,
        // nameChanged returns, and THEN the field sensor for the
        // inner File node goes off.  But, by then it is too late--
        // the directory search path no longer contains the directory
        // of the containing File node.
        nameChangedCB(this, null);
    }

    // Reattach sensor
    nameChangedSensor.attach(name);

    return readOK;
}

private static Path findIVOrWRL(Path parentPath) {
        Path fileName = parentPath.getFileName();
        if(
        fileName != null
        && (fileName.toString().endsWith(".iv")||fileName.toString().endsWith(".wrl"))
        && ( parentPath.getParent().getFileName() == null
        || fileName.toString().startsWith(parentPath.getParent().getFileName().toString()))) {
            return parentPath;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentPath)) {
            for (Path file: stream) {
                Path found = findIVOrWRL(file);
                if( found != null ) {
                    return found;
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            //System.err.println(x);
        }
        return null;
}
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Callback that reads in the file when the name field changes.
//
//Use: static, internal
    
    private static void         nameChangedCB(Object data, SoSensor sensor) {
    SoFile f = (SoFile )data;

    f.children.truncate(0);

    final SoInput in = new SoInput();
    String filename = f.name.getValue();

    // _______________________________________ Test if it is a zip file
    boolean zip = true;
        FileSystem fs = null;

    try {
        Path zipfile = Paths.get(filename);
        //ZipFileSystemProvider provider = new ZipFileSystemProvider();
        //Map<String,?> env = Collections.emptyMap();
        //fs = provider.newFileSystem(zipfile,env);
        fs = FileSystems.newFileSystem(zipfile, (ClassLoader) null);
    }
    catch (IOException e) {
        zip = false;
    }
    catch (ProviderNotFoundException e) {
        zip = false;
    }
    catch (UnsupportedOperationException e) {
        zip = false;
    }
    catch (ZipError e) {
        zip = false;
    }
    catch(FileSystemNotFoundException e) {
        zip = false;
    }

    Path ivOrwrl = null;
    if ( fs != null) {
        for (Path root : fs.getRootDirectories()) {
            ivOrwrl = findIVOrWRL(root);
            if (ivOrwrl != null) {
                break;
            }
        }
    }

    // Open file
    f.readOK = true;

    boolean found = (ivOrwrl != null) ? in.openFile(ivOrwrl,true) : in.openFile(filename, true);

    if (! /*in.openFile(filename, true)*/found) {
        f.readOK = false;
        SoReadError.post(in, "Can't open included file \""+filename+"\" in File node");
    }

    if (f.readOK) {
        final SoNode[]  node = new SoNode[1];

        // Read children from opened file.

        while (true) {
            if (SoDB.read(in, node)) {
                if (node[0] != null)
                    f.children.append(node[0]);
                else
                    break;
            }
            else {
                f.readOK = false;
                break; // Coin3D
            }
        }
        in.closeFile();
    }
    // Note: if there is an error reading one of the children, the
    // other children will still be added properly...    	
    in.destructor(); //java port
    }

    private boolean                readOK;         //!< FALSE on read error.
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns pointer to children.
//
// Use: internal

public SoChildList getChildren()
//
////////////////////////////////////////////////////////////////////////
{
    return (SoChildList ) children;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements typical traversal.
//
// Use: extender public

public void SoFile_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        getChildren().traverse(action, 0, indices[0][numIndices[0] - 1]);

    else
        getChildren().traverse(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the callback action
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoFile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the GL render action
//
// Use: extender

public void GLRender(SoGLRenderAction action)

////////////////////////////////////////////////////////////////////////
{
    SoFile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the get bounding box action.  This takes care of averaging
//    the centers of all children to get a combined center.
//
// Use: extender

public void getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     totalCenter = new SbVec3f(0,0,0);
    int         numCenters = 0;
    final int[]         numIndices = new int[1];
    final int[][]   indices = new int[1][];
    int         lastChild;

    if (action.getPathCode(numIndices, indices) == SoAction.PathCode.IN_PATH)
        lastChild = indices[0][numIndices[0] - 1];
    else
        lastChild = getChildren().getLength() - 1;

    for (int i = 0; i <= lastChild; i++) {
        getChildren().traverse(action, i, i);
        if (action.isCenterSet()) {
            totalCenter.operator_add_equal(action.getCenter());
            numCenters++;
            action.resetCenter();
        }
    }
    // Now, set the center to be the average:
    if (numCenters != 0)
        action.setCenter(totalCenter.operator_div(numCenters), false);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the handle event thing
//
// Use: extender

public void handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoFile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pick.
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoFile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements get matrix action.
//
// Use: extender

public void getMatrix(SoGetMatrixAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final int[]         numIndices = new int[1];
    final  int[][]   indices = new int[1][];

    // Only need to compute matrix if group is a node in middle of
    // current path chain or is off path chain (since the only way
    // this could be called if it is off the chain is if the group is
    // under a group that affects the chain).

    switch (action.getPathCode(numIndices, indices)) {

      case NO_PATH:
        break;

      case IN_PATH:
          getChildren().traverse(action, 0, indices[0][numIndices[0] - 1]);
        break;

      case BELOW_PATH:
        break;

      case OFF_PATH:
          getChildren().traverse(action);
        break;
    }
}

public void search(SoSearchAction action)
{
  super.search(action); // always include this node in the search

  // only search children if the user has requested it
  if (searchok) SoFile_doAction((SoAction)action);
}
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoFile class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoFile.class, "File", SoNode.class);
}

}
