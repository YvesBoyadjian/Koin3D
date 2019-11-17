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
 |      Defines the SoAction class and related classes.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jscenegraph.coin3d.inventor.annex.profiler.SbProfilingData;
import jscenegraph.coin3d.inventor.annex.profiler.SoProfiler;
import jscenegraph.coin3d.inventor.annex.profiler.SoProfilerP;
import jscenegraph.coin3d.inventor.annex.profiler.elements.SoProfilerElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoLightPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoCompactPathList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.misc.SoTempPath;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.interaction.inventor.SoSceneManager;
import jscenegraph.mevis.inventor.SoProfiling;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all actions.
/*!
\class SoAction
\ingroup Actions
SoAction is the abstract base class for all actions. Classes
derived from SoAction define operations to be applied at each node
encountered during traversal of a scene graph. The function that gets
called to implement the action for a particular node type is
determined by a lookup table in the global database.

\par See Also
\par
SoNode, SoPath, SoPathList, SoCallbackAction, SoGLRenderAction, SoGetBoundingBoxAction, SoGetMatrixAction, SoHandleEventAction, SoPickAction, SoRayPickAction, SoSearchAction, SoWriteAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoAction implements Destroyable {

    public enum AppliedCode {
          NODE,                   
         PATH,                   
          PATH_LIST               
      };
  
    public  enum PathCode {
          NO_PATH,                
          IN_PATH,                
          BELOW_PATH,             
          OFF_PATH                
      };
      
      public static final SoActionMethod nullAction = new SoActionMethod() {

		@Override
		public void run(SoAction a, SoNode n) {
//			 #ifdef DEBUG
			       String nodeName = n.getTypeId().getName().getString();
			       String actName  = a.getTypeId().getName().getString();
			       
			       SoDebugError.postWarning("SoAction.nullAction",
			                                 "Called for node "+nodeName+", action "+actName);
//			   #endif
			   		}
    	  
      };
  
      private SoActionP pimpl = new SoActionP();
          
      protected       SoState             state;         
    	   
      //! The list of what to do when.  Subclasses set this pointer to
      //! their per-class instance in their constructors.
      protected    	       SoActionMethodList  traversalMethods;
    	   
      protected    	       static SoEnabledElementsList enabledElements;
    	   
      protected    	       static SoActionMethodList methods;
    	   
    	     
      private    	       static SoType       classTypeId;            
    	   
//      private    	       class AppliedTo {
//    	           AppliedCode             code;
//    	           SoNode                  node;          
//    	           SoPath                  path;          
//    	           SoPathList        pathList;      
//    	           SoPathList        origPathList;
//    	           SoCompactPathList       compactPathList;
//    	           boolean                     isLastPathList;
//    	          //PathCode                curPathCode;
//    	          
//    	          public void copyFrom(AppliedTo from) {
//    	        	  code = from.code;
//    	        	  node = from.node;
//    	        	  path = from.path;
//    	        	  pathList = from.pathList;
//    	        	  origPathList = from.origPathList;
//    	        	  compactPathList = from.compactPathList;
//    	        	  isLastPathList = from.isLastPathList;
//    	        	  //curPathCode = from.curPathCode;
//    	          }
//    	       }
//      private final AppliedTo appliedTo = new AppliedTo();
    	   
      //private    	       boolean              isBeingApplied;
    	   
      //private    	       final SoLightPath         curPath;
    	   
      //private    	       SoTempPath         tempPath;
    	    
    	   
      //private    	       int                 enabledElementsCounter;
	   
      //private    	       boolean              terminated;
	   
      private    	       int                 index;

      private SoSceneManager sceneManager;
      
      private final SoTempPath currentpath = new SoTempPath(8);
      private PathCode currentpathcode = PathCode.NO_PATH;

      
      public static SoType getClassTypeId() {
    	  return new SoType(classTypeId);
      }

      //! Returns the type identifier for a specific instance.
      public abstract SoType      getTypeId();
             
    	       
    	       protected SoAction() {
    	    	   //curPath = new SoLightPath(32);
    	    	   //SO_ACTION_CONSTRUCTOR(SoAction);
    	    	   state = null;
    	    	   traversalMethods = null; // methods; COIN3D
    	    	   //currentpath = new SoTempPath(8);
    	    	   currentpathcode = PathCode.NO_PATH;
    	    	   
//    	    	   #ifdef DEBUG
    	    	        if (! SoDB.isInitialized())
    	    	            SoDebugError.post("SoAction.SoAction",
    	    	                               "Cannot construct actions before "+
    	    	                               "calling SoDB.init()");
//    	    	    #endif /* DEBUG */
    	    	        
    	    	        pimpl.appliedcode = AppliedCode.NODE;
    	    	        pimpl.applieddata.node = null;
    	    	        pimpl.terminated = false;
    	    	        pimpl.prevenabledelementscounter = 0;
    	    	        
    	    	        currentpath.ref(); // to avoid having a zero refcount instance
    	    	    
    	    	        //isBeingApplied      = false;
    	    	        //tempPath            = null;
    	    	    
    	    	        // Make sure enabled elements list is set up the first time this
    	    	        // is applied.
    	    	        //enabledElementsCounter = -1;
    	    	       	       }
    	       
////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: protected

    	       public void destructor() {
    	    	   
    	    	   int n = pimpl.pathcodearray.getLength();
    	    	   for (int i = 0; i < n; i++) Destroyable.delete( pimpl.pathcodearray.operator_square_bracket(i));
    	    	   Destroyable.delete( this.state);

    	    	   this.currentpath.unrefNoDelete(); // to match the ref() in the constructor
    	    	   
    	    	   Destroyable.delete(currentpath); // java port    	    	   
    	    	   Destroyable.delete(pimpl); // java port
    	       }
    	
    	       //! Returns code indicating what action is being applied to
    	       /*!
    	       Returns a code indicating what (node, path, or pathlist) the action
    	       instance is being applied to.
    	     */
    	     public       AppliedCode         getWhatAppliedTo()    {
    	    	  return pimpl.appliedcode;
    	    	 }
    	        
    	            //! These returns a pointer to the node, path, or path list the
    	            //! action is being applied to. Each returns NULL if the action is
    	            //! not being applied to the appropriate class.
    	     public SoNode             getNodeAppliedTo()   {
    	    	  return pimpl.appliedcode == SoAction.AppliedCode.NODE ? pimpl.applieddata.node : null;
    	    	 }
    	     public SoPath             getPathAppliedTo()   {
    	    	  return pimpl.appliedcode == SoAction.AppliedCode.PATH ? pimpl.applieddata.path : null;
    	    	 }

	            private static boolean first = true;
    	     
	 //
	   // Description:
	   //    Applies action to the graph rooted by a node.
	   //
	   // Use: public
	   	public void apply(SoNode root) {
		
//	   	 #ifdef DEBUG
	          // Check for the common user error of applying an action to an
	          // unreferenced root. This may save some grief.
	          if (root.getRefCount() == 0)
	              SoDebugError.postWarning("SoAction.apply",
	                                        "Action applied to a node with a 0 "+
	                                        "reference count. Did you forget to call "+
	                                        "ref() on the node?");
//	      #endif /* DEBUG */
//	      
//	          // If we are already in the middle of being applied, save the
//	          // current state of what we are applied to, so we can restore it
//	          // afterwards. This happens, for example, when the
//	          // SoGLRenderAction applies itself to traverse transparent paths
//	          // after normal traversal.
//	          boolean              needToRestore = isBeingApplied;
//	          final AppliedTo    saveAppliedTo = new AppliedTo();
//	          PathCode storedcurr = this.currentpathcode;
//	      
//	          if (isBeingApplied)
//	              saveAppliedTo.copyFrom(appliedTo);
//	      
//	          isBeingApplied = true;
//	      
//	          appliedTo.code = AppliedCode.NODE;
//	          appliedTo.node = node;
//	          appliedTo.node.ref();
//	          /*appliedTo.curPathCode*/currentpathcode = PathCode.NO_PATH;
//	      
//	          currentpath.setHead(node);
//	          terminated = false;
//	      
//	          setUpState();
//	      
//	          beginTraversal(node);
//	          endTraversal(node); // COIN3D
//	      
//	          cleanUp();
//	      
//	          // Restore to previous state if necessary
//	          if (needToRestore)
//	              appliedTo.copyFrom(saveAppliedTo);
//	          
//	          this.currentpathcode = storedcurr; // COIN3D
//	      
//	          isBeingApplied = needToRestore;
	          
	          SoDB.readlock();
	          // need to store these in case action is re-applied
	          AppliedCode storedcode = pimpl.appliedcode;
	          SoActionP.AppliedData storeddata = new SoActionP.AppliedData();
	          storeddata.copyFrom(pimpl.applieddata);
	          PathCode storedcurr = this.currentpathcode;

	          // This is a pretty good indicator on whether or not we remembered
	          // to use the SO_ACTION_CONSTRUCTOR() macro in the constructor of
	          // the SoAction subclass.
	          assert(this.traversalMethods != null);
	          this.traversalMethods.setUp();

	          pimpl.terminated = false;

	          this.currentpathcode = SoAction.PathCode.NO_PATH;
	          pimpl.applieddata.node = root;
	          pimpl.appliedcode = SoAction.AppliedCode.NODE;

	          if (root != null) {
	        //#if COIN_DEBUG
	            if ((root.getRefCount() == 0) && first) {

	              // This problem has turned out to be a FAQ, the reason probably
	              // being that it "works" under SGI / TGS Inventor with no
	              // warning that the client application code is actually buggy.
	              //
	              // We prefer to spit out a verbose warning to aid the
	              // application programmer in finding the bug quickly instead of
	              // her having to track down the bug due to some _really_ nasty
	              // sideeffects later.

	              SoDebugError.postWarning("SoAction::apply",

	                                        "The root node that the "+this.getTypeId().getName().getString()+" was applied to "+
	                                        "has a reference count equal to zero. "+

	                                        "This is a bug in your application code which "+
	                                        "you should rectify: you need to ref() (and "+
	                                        "later unref()) the top-level root node to "+
	                                        "make sure you avoid memory leaks (bad) and "+
	                                        "/ or premature memory destruction (*really* "+
	                                        "bad) under certain conditions. "+

	                                        "Coin has an internal workaround to avoid "+
	                                        "just responding with mysterious crashes, "+
	                                        "but as it is not possible to cover _all_ "+
	                                        "cases of what can go wrong with this "+
	                                        "workaround you are *strongly* advised to "+
	                                        "fix the bug in your application code."

	                                        );
	              first = false;
	            }
	        //#endif // COIN_DEBUG
	            // So the graph is not deallocated during traversal.
	            root.ref();
	            this.currentpath.setHead(root);

	            // make sure state is created before traversing
	            this.getState();

	            // send events to overlay graph first
	            if (SoProfiler.isEnabled() &&
	                SoProfiler.isOverlayActive() &&
	                this.isOfType(SoHandleEventAction.getClassTypeId()))
	            {
	              // FIXME: also check that the scene graph view is actually enabled, or
	              // else this is of no point - sending events to the overlay scene
	              // graph.

	              SoNode profileroverlay = SoActionP.getProfilerOverlay();
	              if (profileroverlay != null) {
	                SoProfiler.enable(false);
	                this.beginTraversal(profileroverlay);
	                this.endTraversal(profileroverlay);
	                SoProfiler.enable(true);
	              }

	              // FIXME: if there was a hit on the overlay scene graph view and
	              // the scene graph view is modified, then we should schedule a
	              // redraw.  However, the isHandled() flag isn't affected by that
	              // change for now, so there's no way to detect it.
	              //if (static_cast<SoHandleEventAction *>(this)->isHandled()) {
	              //  root->touch();
	              //}

	            }

	            // start profiling
	            if (SoProfiler.isEnabled() &&
	                state.isElementEnabled(SoProfilerElement.getClassStackIndex(SoProfilerElement.class))) {
	              SoProfilerElement elt = SoProfilerElement.get(state);
	              assert(elt != null);
	              SbProfilingData data = elt.getProfilingData();
	              data.reset();
	              data.setActionType(this.getTypeId());
	              data.setActionStartTime(SbTime.getTimeOfDay());
	            }

	            this.beginTraversal(root);
	            this.endTraversal(root);

	            if (SoProfiler.isEnabled() &&
	                state.isElementEnabled(SoProfilerElement.getClassStackIndex(SoProfilerElement.class))) {
	              SoProfilerElement elt = SoProfilerElement.get(state);
	              assert(elt != null);
	              SbProfilingData data = elt.getProfilingData();
	              data.setActionStopTime(SbTime.getTimeOfDay());
	            }

	            if (SoProfiler.isOverlayActive() &&
	                !this.isOfType(SoGLRenderAction.getClassTypeId())) {
	              // update profiler stats node with the profiling data from the traversal
	              SoNode profilerstats = SoActionP.getProfilerStatsNode();
	              SoProfiler.enable(false);
	              this.traverse(profilerstats);
	              SoProfiler.enable(true);
	            }

	            if (SoProfiler.isConsoleActive()) {
	              SoType profileactiontype = SoProfilerP.getActionType();
	              if (this.isOfType(SoProfilerP.getActionType())) {
	                SoProfilerElement pelt = SoProfilerElement.get(state);
	                if (pelt != null) {
	                  SbProfilingData pdata = pelt.getProfilingData();
	                  SoProfilerP.dumpToConsole(pdata);
	                }
	              }
	            }

	            pimpl.applieddata.node = null;
	            root.unrefNoDelete();
	          }
	          pimpl.appliedcode = storedcode;
	          pimpl.applieddata.copyFrom( storeddata);
	          this.currentpathcode = storedcurr;
	          SoDB.readunlock();
	      	}
	   	

	     ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Applies action to the graph defined by a path.
	     //
	     // Use: public
	     
	   	/*!
	    Applies the action to the parts of the graph defined by \a path.

	    Note that an SoPath will also contain all nodes that may influence
	    e.g. geometry nodes in the path. So for instance applying an
	    SoGLRenderAction on an SoPath will render that path as expected in
	    the view, where geometry will get its materials, textures, and other
	    appearance settings correctly.

	    If the \a path ends in an SoGroup node, the action will also
	    traverse the tail node's children.
	  */
	    public void
	     apply(SoPath path)
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
//	     #ifdef DEBUG
	         // Check for the common user error of applying an action to an
	         // unreferenced path. This may save some grief.
	         if (path.getRefCount() == 0) {
	             SoDebugError.postWarning("SoAction::apply",
	                                       "Action applied to a path with a 0 "+
	                                       "reference count. Did you forget to call "+
	                                       "ref() on the path?");
	         }
//	     #endif /* DEBUG */
	     
//	         // If we are already in the middle of being applied, save the
//	         // current state of what we are applied to, so we can restore it
//	         // afterwards. This happens, for example, when the
//	         // SoGLRenderAction applies itself to traverse transparent paths
//	         // after normal traversal.
//	         boolean              needToRestore = isBeingApplied;
//	         final AppliedTo    saveAppliedTo = new AppliedTo();
//	     
//	         if (isBeingApplied)
//	             saveAppliedTo.copyFrom(appliedTo);
//	         
//	         PathCode storedcurr = this.currentpathcode;	         
//	     
//	         isBeingApplied = true;
//	     
//	         appliedTo.code = AppliedCode.PATH;
//	         appliedTo.path = path;
//	         appliedTo.path.ref();
//	         /*appliedTo.curPathCode*/currentpathcode = ((SoFullPath.cast(path).getLength() == 1) ?
//	                                  PathCode.BELOW_PATH : PathCode.IN_PATH);
//	     
//	         currentpath.setHead(path.getHead());
//	         terminated    = false;
//	     
//	         setUpState();
//	     
//	         beginTraversal(path.getHead());
//	         endTraversal(path.getHead()); // COIN3D
//	     
//	         cleanUp();
//	    
//	         // Restore to previous state if necessary
//	         if (needToRestore) {
//	             appliedTo.copyFrom(saveAppliedTo);
//	     
//	             // Restore the head of the path - we assume this is what was
//	             // in the current path when we got here. NOTE: This rules out
//	             // the possibility that the action was in the middle of being
//	             // applied to some graph; it requires that the recursive
//	             // apply() was called after the graph was traversed, so the
//	             // current path had only the head node in it (the cleanUp()
//	             // for the first apply() was not yet called).
//	             SoNode head = (appliedTo.code == AppliedCode.NODE ? appliedTo.node :
//	                             appliedTo.code == AppliedCode.PATH ? appliedTo.path.getHead() :
//	                             appliedTo.pathList.operator_square_bracket(0).getHead());
//	             currentpath.setHead(head);
//	         }
//	         this.currentpathcode = storedcurr;
//	     
//	         isBeingApplied = needToRestore;
	         SoDB.readlock();
	         // need to store these in case action in reapplied
	         AppliedCode storedcode = pimpl.appliedcode;
	         SoActionP.AppliedData storeddata = new SoActionP.AppliedData();
	         storeddata.copyFrom(pimpl.applieddata);
	         PathCode storedcurr = this.currentpathcode;

	         // This is a pretty good indicator on whether or not we remembered
	         // to use the SO_ACTION_CONSTRUCTOR() macro in the constructor of
	         // the SoAction subclass.
	         assert(this.traversalMethods != null);
	         this.traversalMethods.setUp();

	         pimpl.terminated = false;

	       //#if COIN_DEBUG
	         if (path.getRefCount() == 0) {
	           SoDebugError.postWarning("SoAction::apply",
	                                     "path has reference count equal to zero");
	         }
	       //#endif // COIN_DEBUG

	         // So the path is not deallocated during traversal.
	         path.ref();

	         this.currentpathcode =
	           path.getFullLength() > 1 ? SoAction.PathCode.IN_PATH : SoAction.PathCode.BELOW_PATH;
	         pimpl.applieddata.path = path;
	         pimpl.appliedcode = SoAction.AppliedCode.PATH;

	         // make sure state is created before traversing
	         this.getState();

	         if (path.getLength() != 0 && path.getNode(0) != null) {
	           SoNode node = path.getNode(0);
	           this.currentpath.setHead(node);
	           this.beginTraversal(node);
	           this.endTraversal(node);
	         }

	         path.unrefNoDelete();
	         pimpl.appliedcode = storedcode;
	         pimpl.applieddata.copyFrom( storeddata);
	         this.currentpathcode = storedcurr;
	         SoDB.readunlock();
	     }
	     	   	
	   	
	     //! Returns path code based on where current node (the node at the
	     //! end of the current path) lies with respect to the path(s) the
	     //! action is being applied to. If this returns IN_PATH, indices is
	     //! set to point to an array of indices corresponding to the
	     //! children that continue the paths and numIndices is set to the
	     //! number of such children.
	    public     PathCode    getPathCode(int[] numIndices, final int[][] indices)
	             {   if (/*appliedTo.curPathCode*/currentpathcode == PathCode.IN_PATH) {
	                     usePathCode(numIndices, indices);
	                 }
	                 return /*appliedTo.curPathCode*/currentpathcode;
	             }
	     	   	
	   	
	   	// Does traversal of a graph rooted by a node. 
	   	public void traverse(SoNode node) {
	   		
	   	    // Permit execution in a try/catch blcok or without as described
	   	      // in SoCatch settings. So exceptions and crashes within calls to 
	   	      // inventor nodes can be catched and reported by SoError.
	   		
	   	      //SO_CATCH_START
	   		   
	   			SoType typeId = node.getTypeId();
	   			int actionMethodIndex = SoNode.getActionMethodIndex(typeId);
	   			SoActionMethod method = traversalMethods.operator_square_bracket(actionMethodIndex);
  				method.run(this, node);
	   	      //traversalMethods.operator_square_bracket(SoNode.getActionMethodIndex(node.getTypeId())).run(this, node);
	   	   
	   	      //SO_CATCH_ELSE(;)
	        	   	   
	   	      
//	   	      (*traversalMethods)[
//	   	               SoNode.getActionMethodIndex(node.getTypeId())](this, node);
	   	   
	   	      // On crash and enabled exception handling the following will be 
	   	      // reported to SoError:
	   	      // "Crash in 
	   	      //  <node->getTypeId()>
	   	      //  while
	   	      //  <applying> 
	   	      //  <this->getTypeId()->getName()>
	   	      //  <to it.>"
	   	      //SO_CATCH_END(node.getTypeId(), "applying", getTypeId(), "to it.")
	   	}
	   	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Applies action to the graphs defined by a path list.
//
// Use: public

//public void
//apply(final SoPathList pathList, final boolean obeysRules)
////
//////////////////////////////////////////////////////////////////////////
//{
//    // Check for empty path list
//    if (pathList.getLength() == 0)
//        return;
//
//    // If path list obeys the rules, just apply the action to it
//    if (obeysRules) {
//        apply(pathList, pathList, true);
//        return;
//    }
//
//    //
//    // Otherwise, we may have to break it into smaller lists that do
//    // obey the rules
//    //
//
//    // First, sort the paths
//    final SoPathList  sortedPathList = new SoPathList(pathList);
//    sortedPathList.sort();
//
//    // Remove any duplicate paths and any paths that continue through
//    // the tail node of another path
//    sortedPathList.uniquify();
//
//    int numPaths = sortedPathList.getLength();
//
//    // If all the remaining paths have the same head, just apply to
//    // the sorted path list
//    final SoNode firstHead = sortedPathList.operator_square_bracket(0).getHead();
//    if (sortedPathList.operator_square_bracket(numPaths-1).getHead() == firstHead)
//        apply(sortedPathList, pathList, true);
//
//    // Otherwise, we have to break the path list into smaller ones
//    else
//        splitPathList(sortedPathList, pathList);
//}

/*!
  Applies action to the graphs defined by \a pathlist.  If \a
  obeysrules is set to \c TRUE, \a pathlist must obey the following
  four conditions (which is the case for path lists returned from
  search actions for non-group nodes and path lists returned from
  picking actions):

  All paths must start at the same head node. All paths must be sorted
  in traversal order. The paths must be unique. No path can continue
  through the end point of another path.

  \sa SoAction::apply(SoPath * path)
*/
public void
apply(final SoPathList pathlist, boolean obeysrules)
{
  SoDB.readlock();
  // This is a pretty good indicator on whether or not we remembered
  // to use the SO_ACTION_CONSTRUCTOR() macro in the constructor of
  // the SoAction subclass.
  assert(this.traversalMethods != null);
  this.traversalMethods.setUp();
  if (pathlist.getLength() == 0) {
    SoDB.readunlock();
    return;
  }

  // need to store these in case action in reapplied
  AppliedCode storedcode = pimpl.appliedcode;
  final SoActionP.AppliedData storeddata = new SoActionP.AppliedData(); storeddata.copyFrom( pimpl.applieddata);
  PathCode storedcurr = this.currentpathcode;

  pimpl.terminated = false;

  // make sure state is created before traversing
  this.getState();

  pimpl.applieddata.pathlistdata.origpathlist = pathlist; // ptr
  pimpl.applieddata.pathlistdata.pathlist = pathlist; // ptr
  pimpl.applieddata.pathlistdata.compactlist = null;
  pimpl.appliedcode = AppliedCode.PATH_LIST;
  this.currentpathcode = pathlist.operator_square_bracket(0).getFullLength() > 1 ?
    SoAction.PathCode.IN_PATH : SoAction.PathCode.BELOW_PATH;

  if (obeysrules) {
    // GoGoGo
    if (this.shouldCompactPathList()) {
      pimpl.applieddata.pathlistdata.compactlist = new SoCompactPathList(pathlist);
    }
    this.currentpath.setHead(pathlist.operator_square_bracket(0).getHead());
    this.beginTraversal(pathlist.operator_square_bracket(0).getHead());
    this.endTraversal(pathlist.operator_square_bracket(0).getHead());
    Destroyable.delete( pimpl.applieddata.pathlistdata.compactlist);
    pimpl.applieddata.pathlistdata.compactlist = null;
  }
  else {
    // make copy of path list and make sure it obeys rules
    final SoPathList sortedlist = new SoPathList(pathlist);
    // sort
    sortedlist.sort();
    // remove unnecessary paths
    sortedlist.uniquify();
    int num = sortedlist.getLength();

    // if all head nodes are the same we can traverse in one go
    if (sortedlist.operator_square_bracket(0).getHead() == sortedlist.operator_square_bracket(num-1).getHead()) {
      this.currentpath.setHead(sortedlist.operator_square_bracket(0).getHead());
      pimpl.applieddata.pathlistdata.pathlist = sortedlist; // ptr
      if (this.shouldCompactPathList()) {
        pimpl.applieddata.pathlistdata.compactlist = new SoCompactPathList(sortedlist);
      }
      else {
        pimpl.applieddata.pathlistdata.compactlist = null;
      }
      this.beginTraversal(sortedlist.operator_square_bracket(0).getHead());
      this.endTraversal(sortedlist.operator_square_bracket(0).getHead());
      Destroyable.delete( pimpl.applieddata.pathlistdata.compactlist);
      pimpl.applieddata.pathlistdata.compactlist = null;
    }
    else {
      // make one pass per head node. sortedlist is sorted on
      // different head nodes first, so this is very easy
      SoNode head; // ptr
      final SoPathList templist = new SoPathList();
      int i = 0;
      while (i < num && !this.hasTerminated()) {
        head = sortedlist.operator_square_bracket(i).getHead();
        templist.append(sortedlist.operator_square_bracket(i));
        i++;
        while (i < num && sortedlist.operator_square_bracket(i).getHead() == head) {
          templist.append(sortedlist.operator_square_bracket(i));
          i++;
        }
        pimpl.applieddata.pathlistdata.pathlist = templist; // ptr
        pimpl.appliedcode = AppliedCode.PATH_LIST;
        this.currentpathcode = templist.operator_square_bracket(0).getFullLength() > 1 ?
          SoAction.PathCode.IN_PATH : SoAction.PathCode.BELOW_PATH;
        this.currentpath.setHead(templist.operator_square_bracket(0).getHead());

        if (this.shouldCompactPathList()) {
          pimpl.applieddata.pathlistdata.compactlist = new SoCompactPathList(templist);
        }
        else {
          pimpl.applieddata.pathlistdata.compactlist = null;
        }
        this.beginTraversal(templist.operator_square_bracket(0).getHead());
        Destroyable.delete( pimpl.applieddata.pathlistdata.compactlist);
        pimpl.applieddata.pathlistdata.compactlist = null;
        templist.truncate(0);
      }
    }
  }
  pimpl.appliedcode = storedcode;
  pimpl.applieddata.copyFrom( storeddata);
  this.currentpathcode = storedcurr;
  SoDB.readunlock();
}

	   	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a sorted path list that contains paths from different head
//    nodes, this splits the list into one list per head node, then
//    applies the action to each of those lists, in turn.
//
// Use: private

//private void
//splitPathList(final SoPathList sortedList,
//                        final SoPathList origPathList)
////
//////////////////////////////////////////////////////////////////////////
//{
//    int         numPaths, curStart, i;
//    SoNode      curHead;
//
//    numPaths = sortedList.getLength();
//
//    // Create a list to hold one of the split lists
//    final SoPathList  splitList = new SoPathList(numPaths);
//
//    // Split list while there are still paths to examine
//    curStart = 0;
//    while (curStart < numPaths) {
//
//        // Gather all paths with same head
//        curHead = sortedList.operator_square_bracket(curStart).getHead();
//        splitList.append(sortedList.operator_square_bracket(curStart));
//
//        for (i = curStart + 1; i < numPaths; i++) {
//            if (sortedList.operator_square_bracket(i).getHead() != curHead)
//                break;
//            splitList.append(sortedList.operator_square_bracket(i));
//        }
//
//        // Apply action to split list. Indicate that it's the last
//        // path list if there are no more paths after these.
//        apply(splitList, origPathList, i >= numPaths);
//
//        // Prepare for next set of paths
//        splitList.truncate(0);
//        curStart = i;
//    }
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Applies action to the graph defined by a path list, which is
//    guaranteed to obey the rules of sorting, uniqueness, etc. The
//    original path list is passed in so the subclass can use it if
//    necessary. The isLastPathList flag indicates that this is the
//    last path list created from the original, in case a derived
//    class wants this info.
//
// Use: private

//private void
//apply(final SoPathList sortedList,
//                final SoPathList origPathList, boolean isLastPathList)
////
//////////////////////////////////////////////////////////////////////////
//{
//    // If we are already in the middle of being applied, save the
//    // current state of what we are applied to, so we can restore it
//    // afterwards. This happens, for example, when the
//    // SoGLRenderAction applies itself to traverse transparent paths
//    // after normal traversal.
//    boolean              needToRestore = isBeingApplied;
//    final AppliedTo    saveAppliedTo = new AppliedTo();
//
//    if (isBeingApplied)
//        saveAppliedTo.copyFrom(appliedTo);
//
//    PathCode storedcurr = this.currentpathcode;    
//    
//    isBeingApplied = true;
//
//    appliedTo.code = AppliedCode.PATH_LIST;
//    appliedTo.pathList       = sortedList;
//    appliedTo.origPathList   = origPathList;
//    appliedTo.isLastPathList = isLastPathList;
//    /*appliedTo.curPathCode*/currentpathcode    =
//        ((SoFullPath.cast( sortedList.operator_square_bracket(0))).getLength() == 1 ?
//         PathCode.BELOW_PATH : PathCode.IN_PATH);
//
//    currentpath.setHead(sortedList.operator_square_bracket(0).getHead());
//    terminated = false;
//
//    setUpState();
//
//    // If requested, create compact path lists for easier traversal
//    // and apply to them
//    if (shouldCompactPathLists())
//        appliedTo.compactPathList = new SoCompactPathList(sortedList);
//    else
//        appliedTo.compactPathList = null;
//
//    beginTraversal(sortedList.operator_square_bracket(0).getHead());
//    endTraversal(sortedList.operator_square_bracket(0).getHead());
//
//    cleanUp();
//
//    if (appliedTo.compactPathList != null)
//        /*delete*/ appliedTo.compactPathList.close();
//
//    // Restore to previous state if necessary
//    if (needToRestore) {
//        appliedTo.copyFrom(saveAppliedTo);
//
//        // Restore the head of the path - we assume this is what was
//        // in the current path when we got here. NOTE: This rules out
//        // the possibility that the action was in the middle of being
//        // applied to some graph; it requires that the recursive
//        // apply() was called after the graph was traversed, so the
//        // current path had only the head node in it (the cleanUp()
//        // for the first apply() was not yet called).
//        SoNode head = (appliedTo.code == AppliedCode.NODE ? appliedTo.node :
//                        appliedTo.code == AppliedCode.PATH ? appliedTo.path.getHead() :
//                        (appliedTo.pathList).operator_square_bracket(0).getHead());
//        currentpath.setHead(head);
//    }
//    this.currentpathcode = storedcurr;
//
//    isBeingApplied = needToRestore;
//}


	   	
	   	// Returns TRUE if the traversal has reached a termination condition. 
/*!
  Returns \c TRUE if the action was prematurely terminated.

  Note that the termination flag will be \c FALSE if the action simply
  completed its run over the scene graph in the "ordinary" fashion,
  i.e. was not explicitly aborted from any of the nodes in the graph.

  \sa setTerminated()
*/
public boolean
hasTerminated()
{
  return pimpl.terminated;
}

	   	
	   	/**
	   	 * Returns a pointer to the path accumulated during traversal, i.e., 
	   	 * the chain of nodes from the root of the traversed graph to the current node 
	   	 * being traversed. 
	   	 * 
	   	 */
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Construct current path from curPath indices
	     //
	     //    use: public
	     //////////////////////////////////////////////////////////////////////
	     //
	     public SoPath 
	     getCurPath()
	     {
	    	  return this.currentpath;
//	         if(tempPath == null){
//	             tempPath = new SoTempPath(32);
//	             tempPath.ref();
//	             }
//	         currentpath.makeTempPath(tempPath);
//	         return tempPath;
	     }
	     	   	
	   	
	   	// Get the state from the action. 
	     /*!
	     Returns a pointer to the state of the action instance. The state
	     contains the current set of elements used during traversal.
	   */
	   public SoState 
	   getState() 
	   {
	     // if a new element has been enabled, we need to recreate the state
	     if (this.state != null &&
	         (SoEnabledElementsList.getCounter() != pimpl.prevenabledelementscounter)) {
	       SoAction thisp = (SoAction) (this);
	       Destroyable.delete( thisp.state);
	       thisp.state = null;
	     }
	     if (this.state == null) {
	       // cast away constness to set state
	       ((SoAction)(this)).state =
	         new SoState((SoAction)(this), this.getEnabledElements().getElements());
	       SoActionP thisp = (SoActionP)(pimpl);
	       thisp.prevenabledelementscounter = this.getEnabledElements().getCounter();
	     }
	     return this.state;
	   }

	   	
	     //! These methods maintain the current path accumulated so far
	         //! during traversal. The action needs to know whether this path is
	         //! a subset of the path being applied to; it saves this info in
	         //! the onPath variable. Before a node is pushed onto the current
	         //! path, call getOnPath() to determine the current setting. The
	         //! value of this flag should be passed in to popCurPath() so the
	         //! onPath variable can be restored.
	    public     PathCode            getCurPathCode() { return /*appliedTo.curPathCode*/currentpathcode;}
	    	
	    
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:  usePathCode is invoked by getPathCode, which
	     //    Returns path code based on where current node (the node at the
	     //    end of the current path) lies with respect to the path(s) the
	     //    action is being applied to. If this returns IN_PATH, indices is
	     //    set to point to an array of indices corresponding to the
	     //    children that continue the paths and numIndices is set to the
	     //    number of such children.
	     //
	     // Use: extender
	     
	     public void
	     usePathCode(int[] numindices, final int[][] indices)
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	     
//	             if (appliedTo.code == AppliedCode.PATH) {
//	                 // Use "index" storage in instance to return next index
//	                 index = appliedTo.path.getIndex(currentpath.getFullLength());
//	                 numIndices[0]  = 1;
//	                 int[] dummy = new int[1];
//	                 dummy[0] = index;
//	                 indices[0]     = dummy;
//	             }
//	     
//	             // Path list case
//	             else
//	                 appliedTo.compactPathList.getChildren(numIndices, indices);
	     
  int curlen = this.currentpath.getFullLength();

  while (pimpl.pathcodearray.getLength() < curlen) {
    pimpl.pathcodearray.append(new SbListInt());
  }

  SbListInt myarray = pimpl.pathcodearray.operator_square_bracket(curlen-1); // ptr
  myarray.truncate(0);

  if (this.getWhatAppliedTo() == AppliedCode.PATH_LIST) {
    if (pimpl.applieddata.pathlistdata.compactlist != null) {
      assert(pimpl.applieddata.pathlistdata.compactlist.getDepth() == this.currentpath.getLength());
      pimpl.applieddata.pathlistdata.compactlist.getChildren(numindices, indices);
    }
    else {
      // this might be very slow if the list contains a lot of
      // paths. See comment in pushCurPath(int, SoNode*) about this.
      SoPathList pl = pimpl.applieddata.pathlistdata.pathlist;
      int n = pl.getLength();
      int previdx = -1;
      myarray.truncate(0);
      for (int i = 0; i < n; i++) {
        SoPath path = (pl).operator_square_bracket(i);
        if (path.getFullLength() > curlen &&
            path.containsPath(this.currentpath)) {
          int idx = path.getIndex(curlen);
          if (idx != previdx) {
            myarray.append(idx);
            previdx = idx;
          }
        }
      }
      numindices[0] = myarray.getLength();
      indices[0] = myarray.getArrayPtr().getValues();
    }
  }
  else {
    numindices[0] = 1;
    myarray.append(pimpl.applieddata.path.getIndex(curlen));
    indices[0] = myarray.getArrayPtr().getValues();
  }
	     }
	     	    
	    
//	    ////////////////////////////////////////////////////////////////////////
//	     //
//	     // Description:
//	     //    Pushes a node onto the current path being traversed.
//	     //
//	     // Use: internal
//	     
//	     public void
//	     pushCurPath(int childIndex)
//	     //
//	     ////////////////////////////////////////////////////////////////////////
//	     {
//	         // Push the new node
//	         currentpath.push(childIndex);
//	     
//	         // See if new node is on path being applied to. (We must have been
//	         // on the path already for this to be true.)
//	         if (/*appliedTo.curPathCode*/currentpathcode == PathCode.IN_PATH) {
//	     
//	             // If traversing a path list, let it know where we are and
//	             // find out if we are still on a path being traversed.
//	     
//	             if (appliedTo.code == AppliedCode.PATH_LIST) {
//	                 boolean      onPath = appliedTo.compactPathList.push(childIndex);
//	     
//	                 if (! onPath)
//	                     /*appliedTo.curPathCode*/currentpathcode = PathCode.OFF_PATH;
//	     
//	                 // If still on a path, see if we are at the end by seeing
//	                 // if there are any children left on the path
//	                 else {
//	                     final int[]     numChildren = new int[1];
//	                     final int[][] indices = new int[1][];
//	     
//	                     appliedTo.compactPathList.getChildren(numChildren, indices);
//	                     if (numChildren[0] == 0)
//	                         /*appliedTo.curPathCode*/currentpathcode = PathCode.BELOW_PATH;
//	                     else
//	                         /*appliedTo.curPathCode*/currentpathcode = PathCode.IN_PATH;
//	                 }
//	             }
//	     
//	             // Otherwise, we're applying to a path:
//	             else {
//	     
//	                 // Get new length of current path
//	                 int l = currentpath.getFullLength();
//	     
//	                 // There are three possible cases:
//	                 // (1) New node is the last node in the path chain    => BELOW_PATH
//	                 // (2) It's the next node (not the last) in the chain => IN_PATH
//	                 // (3) It veered off the path chain                   => OFF_PATH
//	     
//	                 // If the new node is NOT the next node in the path, we must
//	                 // be off the path
//	                 final SoNode nextPathNode = appliedTo.path.getNode(l - 1);
//	                 if (currentpath.getNode(l - 1) != nextPathNode)
//	                     /*appliedTo.curPathCode*/currentpathcode = PathCode.OFF_PATH;
//	     
//	                 // Otherwise, if cur path length is now the same as the path
//	                 // being applied to, we must at the last node in that path
//	                 else if (l == ( SoFullPath.cast( appliedTo.path)).getLength())
//	                     /*appliedTo.curPathCodecurrentpathcode*/currentpathcode = PathCode.BELOW_PATH;
//	     
//	                 // Otherwise, we're still IN_PATH
//	             }
//	         }
//	     }
	     
	     /*!
	     Get ready to traverse the \a childindex'th child. Use this method
	     if the path code might change as a result of this.

	     This method is very internal. Do not use unless you know
	     what you're doing.
	   */
	   public void
	   pushCurPath( int childindex, SoNode node)
	   {
	     if (node != null) this.currentpath.simpleAppend(node, childindex);
	     else {
	       this.currentpath.append(childindex);
	     }
	     int curlen = this.currentpath.getFullLength();

	     if (this.currentpathcode == PathCode.IN_PATH) {
	       if (this.getWhatAppliedTo() == AppliedCode.PATH) {
	         assert(curlen <= pimpl.applieddata.path.getFullLength());
	         if (this.currentpath.getIndex(curlen-1) !=
	             pimpl.applieddata.path.getIndex(curlen-1)) {
//	   #ifdef DEBUG_PATH_TRAVERSAL
//	           fprintf(stderr,"off path at: %d (%s), depth: %d\n",
//	                   childindex, node->getName().getString(), curlen);
//	   #endif // DEBUG_PATH_TRAVERSAL
	           this.currentpathcode = PathCode.OFF_PATH;
	         }
	         else if (curlen == pimpl.applieddata.path.getFullLength()) {
	           this.currentpathcode = PathCode.BELOW_PATH;
//	   #ifdef DEBUG_PATH_TRAVERSAL
//	           fprintf(stderr,"below path at: %d (%s), depth: %d\n",
//	                   childindex, node->getName().getString(),curlen);
//	   #endif // DEBUG_PATH_TRAVERSAL
	         }
	       }
	       else {
	         if (pimpl.applieddata.pathlistdata.compactlist != null) {
	           boolean inpath = pimpl.applieddata.pathlistdata.compactlist.push(childindex);
	           assert(pimpl.applieddata.pathlistdata.compactlist.getDepth() == this.currentpath.getLength());

	           if (!inpath) {
	             this.currentpathcode = PathCode.OFF_PATH;
	           }
	           else {
	             final int[] numchildren = new int[1];
	             final int[][] dummy = new int[1][];
	             pimpl.applieddata.pathlistdata.compactlist.getChildren(numchildren, dummy);
	             this.currentpathcode = numchildren[0] == 0 ? PathCode.BELOW_PATH : PathCode.IN_PATH;
	           }
	         }
	         else {
	           // test for below path by testing for one path that contains
	           // current path, and is longer than current.  At the same time,
	           // test for off path by testing if there is no paths that
	           // contains current path.  This is a lame and slow way to do it,
	           // but SoCompactPathList will always be used. This is just backup
	           // code in case some action actually disables compact path list.
	           final SoPathList pl = pimpl.applieddata.pathlistdata.pathlist;
	           int i, n = pl.getLength();
	           int len = -1;

	           for (i = 0; i < n; i++) {
	             final SoPath path = (pl).operator_square_bracket(i);
	             len = path.getFullLength();
	             // small optimization, no use testing if path is shorter
	             if (len >= curlen) {
	               if (path.containsPath(this.currentpath)) break;
	             }
	           }
	           // if no path is found, we're off path
	           if (i == n) {
	             this.currentpathcode = PathCode.OFF_PATH;
//	   #ifdef DEBUG_PATH_TRAVERSAL
//	             fprintf(stderr,"off path at: %d (%s), depth: %d\n",
//	                     childindex, node->getName().getString(), curlen);
//	   #endif // DEBUG_PATH_TRAVERSAL
	           }
	           else if (len == curlen) {
	             this.currentpathcode = PathCode.BELOW_PATH;
//	   #ifdef DEBUG_PATH_TRAVERSAL
//	             fprintf(stderr,"below path at: %d (%s), depth: %d\n",
//	                     childindex, node->getName().getString(), curlen);
//	   #endif // DEBUG_PATH_TRAVERSAL
	           }
	         }
	       }
	     }
	   }

	     
	     	    
	     ////////////////////////////////////////////////////////////////////////
	      //
	      // Description:
	      //    Pops the last node from the current path being traversed.
	      //    Restores the path code to the given value.
	      //
	      // Use: internal
	      
	   /*!
	   \fn void SoAction::popCurPath(const PathCode prevpathcode)
	   Pops the current path, and sets the path code to \a prevpathcode.

	   This method is very internal. Do not use unless you know
	   what you're doing.
	 */
	     public void
	      popCurPath(PathCode prevpathcode)
	      //
	      ////////////////////////////////////////////////////////////////////////
	      {
//	          currentpath.pop();
	      
//	          /*appliedTo.curPathCode*/currentpathcode = prevPathCode;
//	      
//	          // If we're traversing a path list, let it know where we are
//	          if (appliedTo.code == AppliedCode.PATH_LIST && /*appliedTo.curPathCode*/currentpathcode == PathCode.IN_PATH)
//	              appliedTo.compactPathList.pop();
	          this.currentpath.pop();
	          this.currentpathcode = prevpathcode;

	          // If we're traversing a path list, let it know where we are
	          if ((pimpl.appliedcode == AppliedCode.PATH_LIST) && (prevpathcode == PathCode.IN_PATH)) {
	            if (pimpl.applieddata.pathlistdata.compactlist != null) {
	              pimpl.applieddata.pathlistdata.compactlist.pop();
	              assert(pimpl.applieddata.pathlistdata.compactlist.getDepth() == this.currentpath.getLength());
	            }
	          }
	      }
	      	     
	    
	     //! Optimized versions of push/pop when we know path codes won't
	         //! change:
	     /*!
	     Pushes a NULL node onto the current path. Use this before
	     traversing all children when you know that the path code will not
	     change while traversing children.

	     This method is very internal. Do not use unless you know
	     what you're doing.
	   */
	    public     void                pushCurPath()
	                             {
	    	  this.currentpath.simpleAppend((SoNode)( null), -1);
	    	}
	    
	    public void                popPushCurPath(int childindex) {
	    	popPushCurPath(childindex,null);
	    }
	    /*!
	    Get ready to traverse the \a childindex'th child. Use this method
	    if you know the path code will not change as a result of this.

	    This method is very internal. Do not use unless you know
	    what you're doing.
	  */
	    public void                popPushCurPath(int childindex, SoNode node)
	                              {
	    	  if (node == null) {
	    		    this.currentpath.pop(); // pop off previous or NULL node
	    		    this.currentpath.append(childindex);
	    		  }
	    		  else {
	    		    this.currentpath.replaceTail(node, childindex);
	    		  }
	                              }
	    public      void                popCurPath()
	                              { currentpath.pop(); }
	     	    	    
	   	//
	     // Description:
	     //    Returns the list of enabled elements for a given action
	     //    subclass.
	     //
	     // Use: protected
	     
	   	protected SoEnabledElementsList getEnabledElements() {
	   		return enabledElements;
	   	}
	   	
	    //
	     // Description:
	     //    Begins traversal of an action at the given node. The default
	     //    method here just calls traverse(node). This is virtual to allow
	     //    subclasses to do extra work before or after traversing the node.
	     //
	     // Use: protected
	     
	     protected void beginTraversal(SoNode node)
	     //
	     {
	         traverse(node);
	     }

	     /*!
	     This virtual method can be overridden to execute code after the
	     scene graph traversal.  Default method does nothing.
	   */
	   protected void endTraversal(SoNode node)
	   {
	   }

	     
	     //! Allows subclass instance to indicate that traversal has reached
	          //! a termination condition
	   /*!
	   Set the termination flag.

	   Typically set to TRUE from nodes upon special
	   conditions being met during scene graph traversal -- like the
	   correct node being found when doing SoSearchAction traversal or
	   when grabbing the event from an SoHandleEventAction.

	   \sa hasTerminated()
	 */
	     protected void setTerminated(boolean flag)      {
	    	  pimpl.terminated = flag;
	    	 }
	      	   	
	     /*!
	     \COININTERNAL
	   */
	   public boolean
	   shouldCompactPathList()
	   {
	     return true;
	   }

	   	
	   	
	    //
	     // Description:
	     //    Creates state if it is NULL or it is no longer valid because new
	     //    elements have been enabled since it was created. If any element
	     //    was enabled since then, the global counter will be different
	     //    from the one stored in this instance.  This will never happen
	     //    with standard Inventor nodes, but might happen when a
	     //    user-defined node is dynamically loaded.
	     //
	     // Use: private
	     
//	    private void setUpState()
//	     //
//	     {
//	         boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeApplyActionCB()!=null);
//	         if (profilingEntered) {
//	           SoProfiling.getEnterScopeApplyActionCB().run(this);
//	         }
//	     
//	         // Setup method traversal table
//	         traversalMethods.setUp();
//	     
//	         // Create state if necessary.  When an new or different element is
//	         // enabled, the recreateState flag is set.
//	         if (state == null ||
//	             enabledElementsCounter != SoEnabledElementsList.getCounter()) {
//	     
//	             if (state != null) {
//					state.destructor();
//	             }
//	     
//	             state = new SoState(this, getEnabledElements().getElements());
//	     
//	             // Store current counter in instance
//	             enabledElementsCounter = SoEnabledElementsList.getCounter();
//	         }   
//	     }

	    //
	     // Description:
	     //    Cleans up after an action has been applied.
	     //
	     // Use: private
	     
//	    private void
//	     cleanUp()
//	     //
//	     {
//	         boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeApplyActionCB()!=null)
//	                                 && SoProfiling.getLeaveScopeCB() != null;
//	         if (profilingEntered) {
//	           SoProfiling.getLeaveScopeCB().run();
//	         }
//	     
//	         switch (appliedTo.code) {
//	     
//	           case NODE:
//	             if (appliedTo.node != null) {
//	                 appliedTo.node.unref();
//	                 appliedTo.node = null;
//	             }
//	             break;
//	     
//	           case PATH:
//	             if (appliedTo.path != null) {
//	                 appliedTo.path.unref();
//	                 appliedTo.path = null;
//	             }
//	             break;
//	     
//	           case PATH_LIST:
//	             appliedTo.pathList = null;
//	             break;
//	         }
//	     
//	         currentpath.truncate(0);
//	     }
	    
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Returns TRUE if action is an instance of a action of the given type
	     //    or an instance of a subclass of it.
	     //
	     // Use: public
	     
	    public boolean
	     isOfType(SoType type)
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	         return getTypeId().isDerivedFrom(type);
	     }
	     	    
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Invalidate the state so that it will be created again
	     // next time the action is applied.
	     //
	     // Use: public, virtual
	     
	    public void
	     invalidateState()
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	         if (state != null) {
				state.destructor();
				state = null;
	         }
	     }
	     	    
	     
	    // Initialize ALL Inventor action classes. 
	    public static void initClasses() {
	        // base class must be initialized first
	    	        SoAction.initClass();
	    	    
	    	        SoCallbackAction.initClass();
	    	        SoGLRenderAction.initClass();
	    	        SoGetBoundingBoxAction.initClass();
	    	        SoGetMatrixAction.initClass();
	    	        SoGetPrimitiveCountAction.initClass();
	    	        SoHandleEventAction.initClass();
	    	        SoPickAction.initClass();
	    	        SoRayPickAction.initClass();
	    	        SoSearchAction.initClass();
	    	        SoWriteAction.initClass();
	    	   	    	
	    }

	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Initializes the SoAction class.
	     //
	     // Use: internal
	     
	     static void
	     initClass()
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	         enabledElements = new SoEnabledElementsList(null);
	         methods = new SoActionMethodList(null);
	     
	         // Allocate a new action type id. There's no real parent id, so we
	         // can't use the regular macro.
	        classTypeId = SoType.createType(SoType.badType(), new SbName("SoAction"), null);
	    
	        // Enable override element for all actions.
	        enabledElements.enable(SoOverrideElement.getClassTypeId(SoOverrideElement.class),
	                                SoOverrideElement.getClassStackIndex(SoOverrideElement.class));
	    }
	    
	     
////////////////////////////////////////////////////////////////////////
//
// Description:
//    find current path tail
//
//    use: public, virtual (SoCallbackAction overrides this)
//////////////////////////////////////////////////////////////////////
//
public SoNode 
getCurPathTail() 
{
//#ifdef DEBUG
    if (currentpath.getTail() != (SoFullPath.cast(getCurPath())).getTail()){
        SoDebugError.post("SoAction::getCurPathTail\n", 
        "Inconsistent path tail.  Did you change the scene graph\n"+
        "During traversal?\n");
    }
//#endif /*DEBUG*/
    return(currentpath.getTail());
}

	     
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This is used when applying an action to an SoPathList. It
//    returns TRUE to indicate that the action should create a compact
//    version of the path list before applying itself to it.
//
// Use: protected

protected boolean
shouldCompactPathLists()
//
////////////////////////////////////////////////////////////////////////
{
    return true;
}

/**
 * Java port
 * @return
 */
public SoSceneManager getSceneManager() {
	return sceneManager;
}

/**
 * Java port
 * @param sceneManager
 */
public void setSceneManager(SoSceneManager sceneManager) {
	this.sceneManager = sceneManager;
}
	   
	     
/*!
  Store our state, traverse the subgraph rooted at the given \a node,
  restore our state and continue traversal.
 */
public void
switchToNodeTraversal(SoNode node)
{
  // Store current state.
  SoActionP.AppliedData storeddata = new SoActionP.AppliedData(this.pimpl.applieddata);
  AppliedCode storedcode = this.pimpl.appliedcode;
  PathCode storedpathcode = this.currentpathcode;
  SoTempPath storedpath = new SoTempPath(this.currentpath);

  this.pimpl.appliedcode = SoAction.AppliedCode.NODE;
  this.pimpl.applieddata.node = node;
  this.currentpathcode = SoAction.PathCode.NO_PATH;
  this.currentpath.truncate(0);

  this.traverse(node);

  // Restore previous state.
  this.currentpath.copyFrom(storedpath);
  this.currentpathcode = storedpathcode;
  this.pimpl.applieddata.copyFrom(storeddata);
  this.pimpl.appliedcode = storedcode;
}

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

	   }
