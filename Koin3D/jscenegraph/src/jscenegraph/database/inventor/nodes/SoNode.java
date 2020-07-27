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
 |      This file defines the base SoNode class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowSpotLight;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate4;
import jscenegraph.coin3d.inventor.nodes.SoDepthBuffer;
import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.coin3d.inventor.nodes.SoLevelOfDetail;
import jscenegraph.coin3d.inventor.nodes.SoSceneTexture2;
import jscenegraph.coin3d.inventor.nodes.SoShuttle;
import jscenegraph.coin3d.inventor.nodes.SoTexture;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.coin3d.inventor.nodes.SoTexture2Transform;
import jscenegraph.coin3d.inventor.nodes.SoTexture3;
import jscenegraph.coin3d.inventor.nodes.SoTextureCombine;
import jscenegraph.coin3d.inventor.nodes.SoTextureScalePolicy;
import jscenegraph.coin3d.inventor.nodes.SoTextureUnit;
import jscenegraph.coin3d.inventor.nodes.SoTransparencyType;
import jscenegraph.coin3d.inventor.nodes.SoVertexAttribute;
import jscenegraph.coin3d.inventor.nodes.SoVertexAttributeBinding;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoNodeList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.actions.SoWriteAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldContainer;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoNotList;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all database nodes.
/*!
\class SoNode
\ingroup Nodes
This is the abstract base class from which all scene graph node
classes are derived.

\par Action Behavior
\par
SoSearchAction
<BR> If the node pointer, type, or name matches the search criteria, returns a path to the node.
\par
SoWriteAction
<BR> Writes the contents of the node to the current SoOutput.

\par See Also
\par
SoPath, SoAction, SoNodeKit
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoNode extends SoFieldContainer {

	private static long nextAddress;


	private final long address;

	//! Unique id for this node.
	protected long uniqueId;

	//! Next available unique id
	protected static long nextUniqueId;

    //! Next index into the action method table
    protected  static int          nextActionMethodIndex;

 	   private
		        static SoType       classTypeId;    //!< Type identifier

		   	private boolean override; //!< TRUE if node overrides others

		    public enum NodeType {
		        INVENTOR     ( 0x0000),
		        VRML1        ( 0x0001),
		        VRML2        ( 0x0002),
		        INVENTOR_1   ( 0x0004),
		        INVENTOR_2_0 ( 0x0008),
		        INVENTOR_2_1 ( 0x0010),
		        INVENTOR_2_5 ( 0x0020),
		        INVENTOR_2_6 ( 0x0040),
		        COIN_1_0     ( 0x0080),
		        COIN_2_0     ( 0x0100),
		        EXTENSION    ( 0x0200),
		        COIN_2_2     ( 0x0400),
		        COIN_2_3     ( 0x0800),
		        COIN_2_4     ( 0x1000),
		        INVENTOR_5_0 ( 0x2000),
		        COIN_2_5     ( 0x4000),
		        COIN_3_0     ( 0x8000),
		        INVENTOR_6_0 ( 0x10000),
		        COIN_4_0     ( 0x20000);
		        
		        private int value;
		        
		        NodeType(int value) {
		        	this.value = value;
		        }
		        
		        public int getValue() {
		        	return value;
		        }
		        
		        public static NodeType fromValue( int value) {
		        	for(NodeType nt : NodeType.values()) {
		        		if(nt.getValue() == value) {
		        			return nt;
		        		}		        		
		        	}
		        	return null;
		        }
		      };

// defines for node state flags

// we can currently have 31 node types. The last bit is used to store
// the override flag.
public static final int FLAG_TYPEMASK = 0x7fffffff;
public static final int FLAG_OVERRIDE = 0x80000000;

private int stateflags;		   	
		   	
	protected SoNode() {

//		 #ifdef DEBUG
		   if (! SoDB.isInitialized()) {
		   SoDebugError.post("SoNode.SoNode",
		   "Cannot construct nodes before "+
		   "calling SoDB.init()");
		   }
//		  #endif /* DEBUG */

		   override = false;
		   uniqueId = nextUniqueId++;

		   address = nextAddress++;
		 }

	public long getAddress() { return address;}

    //! Returns type identifier for the SoNode class.
    public  static SoType       getClassTypeId()        { return new SoType(classTypeId); }


	@Override
	public SoType getTypeId() {
		return classTypeId;
	}

	// Turns override flag on or off.
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Turns override flag on or off. Causes notification.
	   //
	   // Use: public

	  public void
	   setOverride(boolean state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       override = state;
	       startNotify();
	   }

	     //! Returns the state of the override flag.
	  public     boolean              isOverride()              { return override; }

	/**
	 * Creates and returns an exact copy of the node.
	 * If the node is a group, it copies the children as well.
	 * If copyConnections is TRUE (it is FALSE by default),
	 * any connections to (but not from) fields of the node are copied,
	 * as well.
	 * Note that multiple references to a node under the node
	 * to be copied will result in multiple references to the copy
	 * of that node.
	 *
	 * @return
	 */
	  // java port
	public SoNode copy() {
		return copy(false);
	}
	public SoNode copy(boolean copyConnections) {

	     //
		       // The copy operation is done in two passes:
		       //
		       // (1) Determine which nodes are "inside" the copy. That is, which
		       //     nodes are under the original node being copied.
		       //
		       // (2) Copy the graph. Inside nodes and engines re-use the same
		       //     instance each time encountered. References to "outside"
		       //     nodes and engines are copied as just pointers.
		       //     (An engine is inside only if there is at least one
		       //     inside node connected somewhere at both ends.)
		       //
		       // To make these steps more efficient, a dictionary of inside
		       // nodes is created in step (1) and used in step (2). Engines
		       // determined to be inside in step (2) are also added to the
		       // dictionary. This dictionary is maintained in the
		       // SoFieldContainer class so both nodes and engines can access it.
		       //

		       // Ref ourselves, just in case our ref count is 0
		       ref();

		       // Step (1):

		       // Set up a new dictionary. Recursive copy operations use new
		       // dictionaries to avoid confusion.
		       initCopyDict();

		       // Recursively figure out which nodes are inside and add them to
		       // the copy dictionary, each with NO ref().
		       SoNode newNode = addToCopyDict();
		       newNode.ref();

		       // Step (2):

		       // Copy the contents of this node into the new copy. This will
		       // recurse (for groups) and will also handle connections and
		       // fields that point to nodes, paths, or engines.
		       newNode.copyContents(this, copyConnections);

		       // Get rid of the dictionary
		       copyDone();

		       // Return the copy
		       newNode.unrefNoDelete();
		       unref();
		       return newNode;

	}

	// Initiates notification from an instance.
	@Override
	public void startNotify() {
		  // Update our unique id to indicate that we are a different node.
		  uniqueId = nextUniqueId;
		  nextUniqueId++;

		  // Let FieldContainer pass notification on to auditors...
		  super.startNotify();

	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Propagates notification through the node. First updates uniqueId
//    and then does base class action.
//
// Use: internal

@Override
public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (list == null) {
        SoDebugError.post("SoNode::notify",
                           "notification list pointer is NULL");
        return;
    }
//#endif /* DEBUG */

    // Do nothing if this node has already been notified by this
    // current notification. We can tell this by comparing the
    // notification list's time stamp with the node id.
    if (list.getTimeStamp() <= uniqueId) {
		return;
	}

    // Update our unique id to indicate that we are a different node
    uniqueId = nextUniqueId++;

    // Let FieldContainer do most of the work.
    super.notify(list);
}



	// Returns pointer to children, or NULL if none.
	public SoChildList getChildren() {
		return null;
	}

	/**
	 * Recursively adds this node and all nodes under it
	 * to the copy dictionary.
	 * Returns the copy of this node.
	 *
	 * @return
	 */
	public SoNode addToCopyDict() {

	     // If this node is already in the dictionary, nothing else to do
		       SoNode copy = (SoNode ) checkCopy(this);
		       if (copy == null) {

		           // Create and add a new instance to the dictionary
		           copy = (SoNode ) getTypeId().createInstance();
		           copy.ref();
		           addCopy(this, copy);            // Adds a ref()
		           copy.unrefNoDelete();

		           // Recurse on children, if any
		           SoChildList kids = getChildren();
		           if (kids != null) {
					for (int i = 0; i < kids.getLength(); i++) {
						kids.operator_square_bracket(i).addToCopyDict();
					}
				}
		       }

		       return copy;
		  	}

	/**
	 * Copies the contents of the given node into this instance.
	 * The default implementation copies just field values and the name.
	 *
	 * @param fromFC
	 * @param copyConnections
	 */
	@Override
	public void copyContents(SoFieldContainer fromFC, boolean copyConnections) {
	     // Copy the regular stuff
		 super.copyContents(fromFC, copyConnections);

		       // Copy the override flag
		       override = ((SoNode ) fromFC).override;

	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    During a copy operation, this copies an instance that is
//    encountered through a field connection.
//
// Use: internal, virtual

@Override
public SoFieldContainer
copyThroughConnection()
//
////////////////////////////////////////////////////////////////////////
{
    // If there is already a copy of this node (created during the
    // first pass of a copy() operation), use it. Otherwise, just use
    // this node.
    SoFieldContainer copy = findCopy(this, true);
    if (copy != null) {
		return copy;
	}
    return this;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the last node given the name 'name'.  Returns NULL if
//    there is no node with the given name.
//
// Use: public

public static SoNode
getByName( SbName name)
//
////////////////////////////////////////////////////////////////////////
{
    return (SoNode )getNamedBase(name, SoNode.getClassTypeId());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds all nodes named 'name' to the list.  Returns the number of
//    nodes found.
//
// Use: public

public int
getByName( SbName name, SoNodeList list)
//
////////////////////////////////////////////////////////////////////////
{
    return getNamedBases(name, list, SoNode.getClassTypeId());
}



	// Returns the next available unique id.
	public static long getNextNodeId() {
		 return nextUniqueId;
	}

	public static int getActionMethodIndex(SoType t)
	               { return t.getData(); }


/*!
  This function performs the typical operation of a node for any
  action.
*/
public void
doAction(SoAction action)
{
}

	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns TRUE if a node has an affect on the state during
	   //    traversal. The default method returns TRUE. Node classes (such
	   //    as SoSeparator) that isolate their effects from the rest of the
	   //    graph override this method to return FALSE.
	   //
	   // Use: public

	  public boolean
	   affectsState()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       return true;
	   }


	// Initialize ALL Inventor node classes.
	public static void initClasses() {
		//TODO

	     // Base class must be initialized first
		        SoNode.initClass();

		        // Parent classes must always be initialized before their subclasses
		        SoCamera.initClass();
		        SoGroup.initClass();
		        SoLight.initClass();
		        SoProfile.initClass();
		        SoSeparator.initClass();
		        SoShape.initClass();
		        SoTransformation.initClass();
		        SoVertexShape.initClass();
		        SoIndexedShape.initClass();
		        SoNonIndexedShape.initClass();

		        SoAnnotation.initClass();
		        SoArray.initClass();
//		        SoAsciiText.initClass();
		        SoBaseColor.initClass();
		        SoCallback.initClass();
		        SoClipPlane.initClass();
//		        SoColorIndex.initClass(); doesnt exist anymore
		        SoComplexity.initClass();
		        SoCone.initClass();
		        SoCoordinate3.initClass();
		        SoCoordinate4.initClass();
		        SoCube.initClass();
		       SoCylinder.initClass();
		       SoDirectionalLight.initClass();
		       SoDrawStyle.initClass();
		       SoEnvironment.initClass();
		       SoEventCallback.initClass();
		       SoFaceSet.initClass();
		       SoFile.initClass();
		       SoFont.initClass();
		       SoFontStyle.initClass();
		       SoIndexedFaceSet.initClass();
		       SoIndexedTriangleSet.initClass();
		       SoIndexedLineSet.initClass();
//		       SoIndexedNurbsCurve.initClass();
//		       SoIndexedNurbsSurface.initClass();
		       SoIndexedTriangleStripSet.initClass();
		       SoInfo.initClass();
		       SoLabel.initClass();
		       SoLevelOfDetail.initClass();
		       SoLOD.initClass();
		       SoLocateHighlight.initClass();
		       SoLightModel.initClass();
		       SoLineSet.initClass();
		       SoLinearProfile.initClass();
		       SoMaterial.initClass();
		       SoMaterialBinding.initClass();
		       SoVertexAttributeBinding.initClass(); // COIN 3D
		       SoMatrixTransform.initClass();
//		       SoMultipleCopy.initClass();
		       SoNormal.initClass();
		       SoNormalBinding.initClass();
//		       SoNurbsCurve.initClass();
//		       SoNurbsProfile.initClass();
//		       SoNurbsSurface.initClass();
		       SoOrthographicCamera.initClass();
		       SoPackedColor.initClass();
//		       SoPathSwitch.initClass();
		       SoPerspectiveCamera.initClass();
		       SoPickStyle.initClass();
		       SoPointLight.initClass();
		       SoPointSet.initClass();
		       SoMarkerSet.initClass();
		       SoProfileCoordinate2.initClass();
		       SoProfileCoordinate3.initClass();
		       SoQuadMesh.initClass();
		       SoResetTransform.initClass();
		       SoRotation.initClass();
		       SoRotationXYZ.initClass();
		       SoScale.initClass();
		       SoShapeHints.initClass();
		       SoSphere.initClass();
		       SoSpotLight.initClass();
		       SoSwitch.initClass();
		       SoText2.initClass();
		       SoText3.initClass();
		       SoTextureCoordinate2.initClass();
		       SoTextureCoordinateBinding.initClass();
		       SoTextureCoordinateFunction.initClass();
		       SoTextureCoordinateDefault.initClass();
		       SoTextureCoordinateEnvironment.initClass();
		       SoTextureCoordinatePlane.initClass();
		       SoTexture.initClass(); // COIN 3D
		       SoTexture2.initClass();
		       SoTexture3.initClass(); // COIN 3D
		       SoTexture2Transform.initClass();
		       SoTransform.initClass();
		       SoTransformSeparator.initClass();
		       SoTranslation.initClass();
		       SoTriangleStripSet.initClass();
		       SoUnits.initClass();
		       SoUnknownNode.initClass();
		       SoVertexProperty.initClass();
//		       SoWWWAnchor.initClass();
//		       SoWWWInline.initClass();

		       // Four self-animating nodes. These are subclassed from
		       // SoSwitch, SoRotation, and SoTranslation
//		       SoBlinker.initClass();
//		       SoPendulum.initClass();
//		       SoRotor.initClass();
		       SoShuttle.initClass();

		       SoTextureCombine.initClass(); // COIN 3D
		       
		SoDepthBuffer.initClass(); // COIN 3D
		
		SoTextureUnit.initClass(); // COIN 3D
       
       SoSceneTexture2.initClass(); // COIN 3D
       
       SoTransparencyType.initClass(); // COIN 3D
       
       SoTextureScalePolicy.initClass(); // COIN 3D
       
       SoVertexAttribute.initClass(); // COIN 3D
	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the base SoNode class.
	   //
	   // Use: internal

	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       nextActionMethodIndex = 0;
	       // Allocate a new node type id
	       // No real parent id
	       classTypeId = SoType.createType(SoFieldContainer.getClassTypeId(),
	                                        new SbName("Node"),
	                                        null,      // Cannot create, abstract
	                                        (short)nextActionMethodIndex++);

	       // Start nodeIds at 10--that way values of 0 through 9 can be used
	       // for special meanings to attach to nonexistent nodes,
	       // like "default" or "invalid"
	       nextUniqueId = 10;

	       // Add action methods
	       SoCallbackAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   callbackS(soAction, soNode);
	    	   }
	       });
	       SoGLRenderAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   GLRenderS(soAction, soNode);
	    	   }
	       }            );
	       SoGetBoundingBoxAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   getBoundingBoxS(soAction, soNode);
	    	   }
	       }      );
	       SoGetMatrixAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   getMatrixS(soAction, soNode);
	    	   }
	       }           );
	       SoHandleEventAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   handleEventS(soAction, soNode);
	    	   }
	       }         );
	       SoPickAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   pickS(soAction, soNode);
	    	   }
	       }                );
	       SoRayPickAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   rayPickS(soAction, soNode);
	    	   }
	       }             );
	       SoSearchAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   searchS(soAction, soNode);
	    	   }
	       }              );
	       SoWriteAction.addMethod(classTypeId, new SoActionMethod() {
	    	   @Override
			public void run(SoAction soAction, SoNode soNode) {
	    		   writeS(soAction, soNode);
	    	   }
	       }               );
	   }

	     //! These static methods are registered with the database - they
	       //! simply call the appropriate virtual function
	  private     static void         callbackS(SoAction action, SoNode node) {
		     SoCallbackAction a = (SoCallbackAction )action;

		          // Pre/post callbacks are automatically handled.  If the callbacks
		          // set the 'response' flag to stop traversal, handle that also.

		          if (a.hasTerminated()) {
					return;
				}

		          a.setCurrentNode(node);

		          a.invokePreCallbacks(node);

		          if (! a.hasTerminated() &&
		              a.getCurrentResponse() != SoCallbackAction.Response.PRUNE) {
					node.callback(a);
				}

		          a.invokePostCallbacks(node);

	  }
	  
// Note that this documentation will also be used for all subclasses
// which reimplements the method, so keep the doc "generic enough".
/*!
  Action method for the SoGetPrimitiveCountAction.

  Calculates the number of triangle, line segment and point primitives
  for the node and adds these to the counters of the \a action.

  Nodes influencing how geometry nodes calculates their primitive
  count also overrides this method to change the relevant state
  variables.
*/
public void
getPrimitiveCount(SoGetPrimitiveCountAction action)
{
}

	  
	  
	  private     static void         GLRenderS(SoAction action, SoNode node) {

		     SoGLRenderAction a = (SoGLRenderAction ) action;

		          if (! a.abortNow()) {
					node.GLRender(a);
				} else {
		              SoCacheElement.invalidate(action.getState());
		          }
		     	  }
	  private     static void         getBoundingBoxS(SoAction action, SoNode node) {
		     SoGetBoundingBoxAction a = (SoGetBoundingBoxAction )action;

		          a.checkResetBefore();
		          node.getBoundingBox(a);
		          a.checkResetAfter();

	  }
	  private     static void         getMatrixS(SoAction action, SoNode node) {
		  node.getMatrix((SoGetMatrixAction ) action);
	  }
	  private     static void         handleEventS(SoAction action, SoNode node) {
		  node.handleEvent((SoHandleEventAction ) action);
	  }
	  private     static void         pickS(SoAction action, SoNode node) {
		  node.pick((SoPickAction ) action);
	  }
	  public     static void         rayPickS(SoAction action, SoNode node) {
		     node.rayPick((SoRayPickAction ) action);
	  }
	  private     static void         searchS(SoAction action, SoNode node) {
		  node.search((SoSearchAction ) action);
	  }
	  private     static void         writeS(SoAction action, SoNode node) {
		  // not implemented
	  }

//	  protected static SoType SO__NODE_INIT_ABSTRACT_CLASS(Class className, String classPrintName,
//			  Class<? extends SoBase> parentClass,
//			  SoFieldData[][]    parentFieldData) {
//		  return SO__NODE_INIT_CLASS(className,classPrintName,parentClass,parentFieldData,false);
//	  }

	  protected static void SO__NODE_INIT_ABSTRACT_CLASS(
			  Class className, String classPrintName,
			  Class<? extends SoBase> parentClass) {
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(className, classPrintName, parentClass);
	  }

	  protected static void SO__NODE_INIT_CLASS(
			  Class className, String classPrintName,
			  Class<? extends SoBase> parentClass) {
		  SoSubNode.SO__NODE_INIT_CLASS(className, classPrintName, parentClass);
	  }

//	  protected static SoType SO__NODE_INIT_CLASS(
//			  Class className, String classPrintName,
//			  Class<? extends SoBase> parentClass,
//			  SoFieldData[][]    parentFieldData) {
//		  return SO__NODE_INIT_CLASS(className,classPrintName,parentClass,parentFieldData,true);
//	  }
//	  protected static SoType SO__NODE_INIT_CLASS(
//			  Class className, String classPrintName,
//			  Class<? extends SoBase> parentClass,
//			  SoFieldData[][]    parentFieldData,boolean create) {
//		    if ((SoNode.nextActionMethodIndex <     0) ||
//		            (SoNode.nextActionMethodIndex > 32767)){
//		            SoDebugError.post("SO__NODE_INIT_CLASS",
//		                               "Overflow of SoNode::nextActionMethodIndex");
//		            //abort();
//		            throw new IllegalStateException("Overflow of SoNode::nextActionMethodIndex");
//		        }
//		    	SoType parent = null;
//				try {
//					parent = (SoType)parentClass.getMethod("getClassTypeId").invoke(null);
//				} catch (IllegalArgumentException e2) {
//					throw new IllegalStateException(e2);
//				} catch (SecurityException e2) {
//					throw new IllegalStateException(e2);
//				} catch (IllegalAccessException e2) {
//					throw new IllegalStateException(e2);
//				} catch (InvocationTargetException e2) {
//					throw new IllegalStateException(e2);
//				} catch (NoSuchMethodException e2) {
//					throw new IllegalStateException(e2);
//				}
//
//				try {
//					CreateMethod createMethod = null;
//					if(create) {
//						final Constructor<?> constructor = className.getConstructor();
//
//				    	createMethod = new CreateMethod() {
//
//							@Override
//							public Object run() {
//								try {
//									return constructor.newInstance();
//								} catch (IllegalArgumentException e) {
//									throw new RuntimeException(e);
//								} catch (InstantiationException e) {
//									throw new RuntimeException(e);
//								} catch (IllegalAccessException e) {
//									throw new RuntimeException(e);
//								} catch (InvocationTargetException e) {
//									throw new RuntimeException(e);
//								}
//							}
//
//				    	};
//					}
//			        try {
//						parentFieldData[0] = (SoFieldData[])parentClass.getMethod("getFieldDataPtr").invoke(null);
//					} catch (IllegalArgumentException e) {
//						throw new RuntimeException(e);
//					} catch (IllegalAccessException e) {
//						throw new RuntimeException(e);
//					} catch (InvocationTargetException e) {
//						throw new RuntimeException(e);
//					}//parentClass.getFieldDataPtr();
//
//			        return SoType.createType(parent/*parentClass.getClassTypeId()*/,
//			                       new SbName(classPrintName),
//			                       createMethod/*className.createInstance*/,
//			                       (short)(SoNode.nextActionMethodIndex++));
//
//				} catch (SecurityException e1) {
//					throw new IllegalStateException(e1);
//				} catch (NoSuchMethodException e1) {
//					throw new IllegalStateException(e1);
//				}
//	  }

	  protected static void SO_ENABLE(Class<? extends SoAction> actionClass, Class<? extends SoElement> elementClass) {

		  Class<?>[] parameterTypes = new Class<?>[1];
		  parameterTypes[0] = Class.class;

		  try {
			Method enableElement = actionClass.getMethod("enableElement", parameterTypes);
			enableElement.invoke(actionClass, elementClass);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	  }

	   public
		        //! This is used by the field-inheritence mechanism, hidden in
		        //! the SoSubNode macros
		        static SoFieldData[] getFieldDataPtr() { return null; }

	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Implements search action for most nodes.
	    //
	    // Use: extender
	   public void
	    search(SoSearchAction action) {
		   SoNode_search(action);
	   }

	   public final void
	    SoNode_search(SoSearchAction action)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        int         lookingFor = action.getFind();
	        boolean      foundMe = true;

	        // We have to match everything set by the search action.
	        // First, see if node doesn't match:
	        if (((lookingFor & SoSearchAction.LookFor.NODE.getValue())!= 0) && action.getNode() != this) {
				foundMe = false;
			}

	        // Next, see if the name doesn't match:
	        if (((lookingFor & SoSearchAction.LookFor.NAME.getValue()) != 0) &&
	            action.getName().operator_not_equal(this.getName())) {
				foundMe = false;
			}

	        // Finally, figure out if types match:
	        if ((lookingFor & SoSearchAction.LookFor.TYPE.getValue()) != 0) {
	            final boolean[]     derivedOk = new boolean[1];
	            SoType  t = action.getType(derivedOk);
	            if (! (derivedOk[0] ? isOfType(t) : getTypeId().operator_equal_equal(t))) {
					foundMe = false;
				}
	        }

	        if (foundMe) {
	            // We have a match! Add it to the action.

	            if (action.getInterest() == SoSearchAction.Interest.ALL) {
					action.getPaths().append(action.getCurPath().copy());
				} else {
	                action.addPath(action.getCurPath().copy());
	                if (action.getInterest() == SoSearchAction.Interest.FIRST) {
						action.setFound();
					}
	            }
	        }
	    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements default setup of event grabbing
//
// Use: extender

public void
grabEventsSetup()
//
////////////////////////////////////////////////////////////////////////
{
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements default cleanup of event grabbing
//
// Use: extender

public void
grabEventsCleanup()
//
////////////////////////////////////////////////////////////////////////
{
}



	   public void
	    callback(SoCallbackAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	        SoDebugInfo::post("SoNode::callback",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }

	   public void
	   GLRender(SoGLRenderAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	        SoDebugInfo::post("SoNode::GLRender",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }


public void
GLRenderBelowPath(SoGLRenderAction action)
{
    GLRender(action);
}

public void
GLRenderInPath(SoGLRenderAction action)
{
    GLRender(action);
}

public void
GLRenderOffPath(SoGLRenderAction action)
{
    GLRender(action);
}



	     //! Returns the unique id for a node
	   public long getNodeId()        { return uniqueId; }

	   public void
	    getBoundingBox(SoGetBoundingBoxAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	        SoDebugInfo::post("SoNode::getBoundingBox",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }

	   protected void
	    getMatrix(SoGetMatrixAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	        SoDebugInfo::post("SoNode::getMatrix",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }


	   protected void
	    handleEvent(SoHandleEventAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	       SoDebugInfo::post("SoNode::handleEvent",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }

	   protected void
	    pick(SoPickAction action)
	    {
//	    #ifdef DEBUG_DEFAULT_METHODS
//	        SoDebugInfo::post("SoNode::pick",
//	                          "Called for %s", getTypeId().getName().getString());
//	    #endif /* DEBUG_DEFAULT_METHODS */
	    }

	    protected void
	    rayPick(SoRayPickAction action)
	    {
	        // If the node doesn't have a specific rayPick() method, it may
	        // have a more general pick() method for any pick action.
	        pick(action);
	    }
	    
// clear bits in stateflags
private void clearStateFlags(final int bits)
{
  this.stateflags &= ~bits;
}

// sets bits in stateflags
private void setStateFlags(final int bits)
{
  this.stateflags |= bits;
}

	    

/*!
  Sets the node type for this node to \a type. Since some nodes
  should be handled differently in VRML1 vs. Inventor, this
  should be used to get correct behavior for those cases.
  The default node type is INVENTOR.

  This method is an extension versus the Open Inventor API.

  \sa getNodeType()
*/
public void setNodeType(final NodeType type)
{
  // make sure we have enogh bits to store this type
  assert((int) type.getValue() <= FLAG_TYPEMASK);
  // clear old type
  this.clearStateFlags(FLAG_TYPEMASK);
  // set new type
  this.setStateFlags((int) type.getValue());
}

	    
	    /*!
	    Returns the node type set for this node.

	    This method is an extension versus the Open Inventor API.

	    \sa setNodeType()
	  */
	  public SoNode.NodeType
	  getNodeType() 
	  {
	    int type = this.stateflags & FLAG_TYPEMASK;
	    return NodeType.fromValue(type);
	  }

	  public boolean SoNode_readInstance(SoInput in,
              short flags)
{
		  return super.readInstance(in, flags);
}
}
