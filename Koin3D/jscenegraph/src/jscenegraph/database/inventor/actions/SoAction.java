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

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoLightPath;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
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
  
          
      protected       SoState             state;         
    	   
      //! The list of what to do when.  Subclasses set this pointer to
      //! their per-class instance in their constructors.
      protected    	       SoActionMethodList  traversalMethods;
    	   
      protected    	       static SoEnabledElementsList enabledElements;
    	   
      protected    	       static SoActionMethodList methods;
    	   
    	     
      private    	       static SoType       classTypeId;            
    	   
      private    	       class AppliedTo {
    	           AppliedCode             code;
    	           SoNode                  node;          
    	           SoPath                  path;          
    	           SoPathList        pathList;      
    	           SoPathList        origPathList;
    	           SoCompactPathList       compactPathList;
    	           boolean                     isLastPathList;
    	          PathCode                curPathCode;
    	          
    	          public void copyFrom(AppliedTo from) {
    	        	  code = from.code;
    	        	  node = from.node;
    	        	  path = from.path;
    	        	  pathList = from.pathList;
    	        	  origPathList = from.origPathList;
    	        	  compactPathList = from.compactPathList;
    	        	  isLastPathList = from.isLastPathList;
    	        	  curPathCode = from.curPathCode;
    	          }
    	       }
      private final AppliedTo appliedTo = new AppliedTo();
    	   
      private    	       boolean              isBeingApplied;
    	   
      private    	       final SoLightPath         curPath;
    	   
      private    	       SoTempPath         tempPath;
    	    
    	   
      private    	       int                 enabledElementsCounter;
	   
      private    	       boolean              terminated;
	   
      private    	       int                 index;

      private SoSceneManager sceneManager;
      
      public static SoType getClassTypeId() {
    	  return new SoType(classTypeId);
      }

      //! Returns the type identifier for a specific instance.
      public abstract SoType      getTypeId();
             
    	       
    	       protected SoAction() {
    	    	   curPath = new SoLightPath(32);
    	    	   //SO_ACTION_CONSTRUCTOR(SoAction);
    	    	   traversalMethods = methods;
    	    	   
//    	    	   #ifdef DEBUG
    	    	        if (! SoDB.isInitialized())
    	    	            SoDebugError.post("SoAction.SoAction",
    	    	                               "Cannot construct actions before "+
    	    	                               "calling SoDB.init()");
//    	    	    #endif /* DEBUG */
    	    	    
    	    	        isBeingApplied      = false;
    	    	        appliedTo.node      = null;
    	    	        appliedTo.path      = null;
    	    	        appliedTo.pathList  = null;
    	    	        state               = null;
    	    	        terminated          = false;
    	    	        tempPath            = null;
    	    	    
    	    	        // Make sure enabled elements list is set up the first time this
    	    	        // is applied.
    	    	        enabledElementsCounter = -1;
    	    	       	       }
    	       
////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: protected

    	       public void destructor() {
    if (appliedTo.node != null)
        appliedTo.node.unref();

    if (appliedTo.path != null)
        appliedTo.path.unref();

    if (state != null)
        state.destructor();
    if (tempPath != null)
        tempPath.unref();      
    	    	   
    	       }
    	
    	       //! Returns code indicating what action is being applied to
    	     public       AppliedCode         getWhatAppliedTo()    { return appliedTo.code; }
    	        
    	            //! These returns a pointer to the node, path, or path list the
    	            //! action is being applied to. Each returns NULL if the action is
    	            //! not being applied to the appropriate class.
    	     public SoNode             getNodeAppliedTo()   { return appliedTo.node; }
    	     public SoPath             getPathAppliedTo()   { return appliedTo.path; }
    	           	       
	 //
	   // Description:
	   //    Applies action to the graph rooted by a node.
	   //
	   // Use: public
	   	public void apply(SoNode node) {
		
//	   	 #ifdef DEBUG
	          // Check for the common user error of applying an action to an
	          // unreferenced root. This may save some grief.
	          if (node.getRefCount() == 0)
	              SoDebugError.postWarning("SoAction.apply",
	                                        "Action applied to a node with a 0 "+
	                                        "reference count. Did you forget to call "+
	                                        "ref() on the node?");
//	      #endif /* DEBUG */
	      
	          // If we are already in the middle of being applied, save the
	          // current state of what we are applied to, so we can restore it
	          // afterwards. This happens, for example, when the
	          // SoGLRenderAction applies itself to traverse transparent paths
	          // after normal traversal.
	          boolean              needToRestore = isBeingApplied;
	          final AppliedTo    saveAppliedTo = new AppliedTo();
	      
	          if (isBeingApplied)
	              saveAppliedTo.copyFrom(appliedTo);
	      
	          isBeingApplied = true;
	      
	          appliedTo.code = AppliedCode.NODE;
	          appliedTo.node = node;
	          appliedTo.node.ref();
	          appliedTo.curPathCode = PathCode.NO_PATH;
	      
	          curPath.setHead(node);
	          terminated = false;
	      
	          setUpState();
	      
	          beginTraversal(node);
	      
	          cleanUp();
	      
	          // Restore to previous state if necessary
	          if (needToRestore)
	              appliedTo.copyFrom(saveAppliedTo);
	      
	          isBeingApplied = needToRestore;
	      	}
	   	

	     ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Applies action to the graph defined by a path.
	     //
	     // Use: public
	     
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
	     
	         // If we are already in the middle of being applied, save the
	         // current state of what we are applied to, so we can restore it
	         // afterwards. This happens, for example, when the
	         // SoGLRenderAction applies itself to traverse transparent paths
	         // after normal traversal.
	         boolean              needToRestore = isBeingApplied;
	         final AppliedTo    saveAppliedTo = new AppliedTo();
	     
	         if (isBeingApplied)
	             saveAppliedTo.copyFrom(appliedTo);
	     
	         isBeingApplied = true;
	     
	         appliedTo.code = AppliedCode.PATH;
	         appliedTo.path = path;
	         appliedTo.path.ref();
	         appliedTo.curPathCode = ((SoFullPath.cast(path).getLength() == 1) ?
	                                  PathCode.BELOW_PATH : PathCode.IN_PATH);
	     
	         curPath.setHead(path.getHead());
	         terminated    = false;
	     
	         setUpState();
	     
	         beginTraversal(path.getHead());
	     
	         cleanUp();
	    
	         // Restore to previous state if necessary
	         if (needToRestore) {
	             appliedTo.copyFrom(saveAppliedTo);
	     
	             // Restore the head of the path - we assume this is what was
	             // in the current path when we got here. NOTE: This rules out
	             // the possibility that the action was in the middle of being
	             // applied to some graph; it requires that the recursive
	             // apply() was called after the graph was traversed, so the
	             // current path had only the head node in it (the cleanUp()
	             // for the first apply() was not yet called).
	             SoNode head = (appliedTo.code == AppliedCode.NODE ? appliedTo.node :
	                             appliedTo.code == AppliedCode.PATH ? appliedTo.path.getHead() :
	                             appliedTo.pathList.operator_square_bracket(0).getHead());
	             curPath.setHead(head);
	         }
	     
	         isBeingApplied = needToRestore;
	     }
	     	   	
	   	
	     //! Returns path code based on where current node (the node at the
	     //! end of the current path) lies with respect to the path(s) the
	     //! action is being applied to. If this returns IN_PATH, indices is
	     //! set to point to an array of indices corresponding to the
	     //! children that continue the paths and numIndices is set to the
	     //! number of such children.
	    public     PathCode    getPathCode(int[] numIndices, final int[][] indices)
	             {   if (appliedTo.curPathCode == PathCode.IN_PATH) {
	                     usePathCode(numIndices, indices);
	                 }
	                 return appliedTo.curPathCode;
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

public void
apply(final SoPathList pathList, final boolean obeysRules)
//
////////////////////////////////////////////////////////////////////////
{
    // Check for empty path list
    if (pathList.getLength() == 0)
        return;

    // If path list obeys the rules, just apply the action to it
    if (obeysRules) {
        apply(pathList, pathList, true);
        return;
    }

    //
    // Otherwise, we may have to break it into smaller lists that do
    // obey the rules
    //

    // First, sort the paths
    final SoPathList  sortedPathList = new SoPathList(pathList);
    sortedPathList.sort();

    // Remove any duplicate paths and any paths that continue through
    // the tail node of another path
    sortedPathList.uniquify();

    int numPaths = sortedPathList.getLength();

    // If all the remaining paths have the same head, just apply to
    // the sorted path list
    final SoNode firstHead = sortedPathList.operator_square_bracket(0).getHead();
    if (sortedPathList.operator_square_bracket(numPaths-1).getHead() == firstHead)
        apply(sortedPathList, pathList, true);

    // Otherwise, we have to break the path list into smaller ones
    else
        splitPathList(sortedPathList, pathList);
}

	   	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a sorted path list that contains paths from different head
//    nodes, this splits the list into one list per head node, then
//    applies the action to each of those lists, in turn.
//
// Use: private

private void
splitPathList(final SoPathList sortedList,
                        final SoPathList origPathList)
//
////////////////////////////////////////////////////////////////////////
{
    int         numPaths, curStart, i;
    SoNode      curHead;

    numPaths = sortedList.getLength();

    // Create a list to hold one of the split lists
    final SoPathList  splitList = new SoPathList(numPaths);

    // Split list while there are still paths to examine
    curStart = 0;
    while (curStart < numPaths) {

        // Gather all paths with same head
        curHead = sortedList.operator_square_bracket(curStart).getHead();
        splitList.append(sortedList.operator_square_bracket(curStart));

        for (i = curStart + 1; i < numPaths; i++) {
            if (sortedList.operator_square_bracket(i).getHead() != curHead)
                break;
            splitList.append(sortedList.operator_square_bracket(i));
        }

        // Apply action to split list. Indicate that it's the last
        // path list if there are no more paths after these.
        apply(splitList, origPathList, i >= numPaths);

        // Prepare for next set of paths
        splitList.truncate(0);
        curStart = i;
    }
}

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

private void
apply(final SoPathList sortedList,
                final SoPathList origPathList, boolean isLastPathList)
//
////////////////////////////////////////////////////////////////////////
{
    // If we are already in the middle of being applied, save the
    // current state of what we are applied to, so we can restore it
    // afterwards. This happens, for example, when the
    // SoGLRenderAction applies itself to traverse transparent paths
    // after normal traversal.
    boolean              needToRestore = isBeingApplied;
    final AppliedTo    saveAppliedTo = new AppliedTo();

    if (isBeingApplied)
        saveAppliedTo.copyFrom(appliedTo);

    isBeingApplied = true;

    appliedTo.code = AppliedCode.PATH_LIST;
    appliedTo.pathList       = sortedList;
    appliedTo.origPathList   = origPathList;
    appliedTo.isLastPathList = isLastPathList;
    appliedTo.curPathCode    =
        ((SoFullPath.cast( sortedList.operator_square_bracket(0))).getLength() == 1 ?
         PathCode.BELOW_PATH : PathCode.IN_PATH);

    curPath.setHead(sortedList.operator_square_bracket(0).getHead());
    terminated = false;

    setUpState();

    // If requested, create compact path lists for easier traversal
    // and apply to them
    if (shouldCompactPathLists())
        appliedTo.compactPathList = new SoCompactPathList(sortedList);
    else
        appliedTo.compactPathList = null;

    beginTraversal(sortedList.operator_square_bracket(0).getHead());

    cleanUp();

    if (appliedTo.compactPathList != null)
        /*delete*/ appliedTo.compactPathList.close();

    // Restore to previous state if necessary
    if (needToRestore) {
        appliedTo.copyFrom(saveAppliedTo);

        // Restore the head of the path - we assume this is what was
        // in the current path when we got here. NOTE: This rules out
        // the possibility that the action was in the middle of being
        // applied to some graph; it requires that the recursive
        // apply() was called after the graph was traversed, so the
        // current path had only the head node in it (the cleanUp()
        // for the first apply() was not yet called).
        SoNode head = (appliedTo.code == AppliedCode.NODE ? appliedTo.node :
                        appliedTo.code == AppliedCode.PATH ? appliedTo.path.getHead() :
                        (appliedTo.pathList).operator_square_bracket(0).getHead());
        curPath.setHead(head);
    }

    isBeingApplied = needToRestore;
}


	   	
	   	// Returns TRUE if the traversal has reached a termination condition. 
	   	public boolean hasTerminated() {
	   	 return terminated; 
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
	         if(tempPath == null){
	             tempPath = new SoTempPath(32);
	             tempPath.ref();
	             }
	         curPath.makeTempPath(tempPath);
	         return tempPath;
	     }
	     	   	
	   	
	   	// Get the state from the action. 
	   	public SoState getState() {
	   	 return state; 
	   	}
	   	
	     //! These methods maintain the current path accumulated so far
	         //! during traversal. The action needs to know whether this path is
	         //! a subset of the path being applied to; it saves this info in
	         //! the onPath variable. Before a node is pushed onto the current
	         //! path, call getOnPath() to determine the current setting. The
	         //! value of this flag should be passed in to popCurPath() so the
	         //! onPath variable can be restored.
	    public     PathCode            getCurPathCode() { return appliedTo.curPathCode;}
	    	
	    
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
	     usePathCode(int[] numIndices, final int[][] indices)
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	     
	             if (appliedTo.code == AppliedCode.PATH) {
	                 // Use "index" storage in instance to return next index
	                 index = appliedTo.path.getIndex(curPath.getFullLength());
	                 numIndices[0]  = 1;
	                 int[] dummy = new int[1];
	                 dummy[0] = index;
	                 indices[0]     = dummy;
	             }
	     
	             // Path list case
	             else
	                 appliedTo.compactPathList.getChildren(numIndices, indices);
	     
	     }
	     	    
	    
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    Pushes a node onto the current path being traversed.
	     //
	     // Use: internal
	     
	     public void
	     pushCurPath(int childIndex)
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	         // Push the new node
	         curPath.push(childIndex);
	     
	         // See if new node is on path being applied to. (We must have been
	         // on the path already for this to be true.)
	         if (appliedTo.curPathCode == PathCode.IN_PATH) {
	     
	             // If traversing a path list, let it know where we are and
	             // find out if we are still on a path being traversed.
	     
	             if (appliedTo.code == AppliedCode.PATH_LIST) {
	                 boolean      onPath = appliedTo.compactPathList.push(childIndex);
	     
	                 if (! onPath)
	                     appliedTo.curPathCode = PathCode.OFF_PATH;
	     
	                 // If still on a path, see if we are at the end by seeing
	                 // if there are any children left on the path
	                 else {
	                     final int[]     numChildren = new int[1];
	                     final int[][] indices = new int[1][];
	     
	                     appliedTo.compactPathList.getChildren(numChildren, indices);
	                     if (numChildren[0] == 0)
	                         appliedTo.curPathCode = PathCode.BELOW_PATH;
	                     else
	                         appliedTo.curPathCode = PathCode.IN_PATH;
	                 }
	             }
	     
	             // Otherwise, we're applying to a path:
	             else {
	     
	                 // Get new length of current path
	                 int l = curPath.getFullLength();
	     
	                 // There are three possible cases:
	                 // (1) New node is the last node in the path chain    => BELOW_PATH
	                 // (2) It's the next node (not the last) in the chain => IN_PATH
	                 // (3) It veered off the path chain                   => OFF_PATH
	     
	                 // If the new node is NOT the next node in the path, we must
	                 // be off the path
	                 final SoNode nextPathNode = appliedTo.path.getNode(l - 1);
	                 if (curPath.getNode(l - 1) != nextPathNode)
	                     appliedTo.curPathCode = PathCode.OFF_PATH;
	     
	                 // Otherwise, if cur path length is now the same as the path
	                 // being applied to, we must at the last node in that path
	                 else if (l == ( SoFullPath.cast( appliedTo.path)).getLength())
	                     appliedTo.curPathCode = PathCode.BELOW_PATH;
	     
	                 // Otherwise, we're still IN_PATH
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
	      
	     public void
	      popCurPath(PathCode prevPathCode)
	      //
	      ////////////////////////////////////////////////////////////////////////
	      {
	          curPath.pop();
	      
	          appliedTo.curPathCode = prevPathCode;
	      
	          // If we're traversing a path list, let it know where we are
	          if (appliedTo.code == AppliedCode.PATH_LIST && appliedTo.curPathCode == PathCode.IN_PATH)
	              appliedTo.compactPathList.pop();
	      }
	      	     
	    
	     //! Optimized versions of push/pop when we know path codes won't
	         //! change:
	    public     void                pushCurPath()
	                             { curPath.append(-1); }
	    public void                popPushCurPath(int childIndex)
	                              { curPath.setTail(childIndex);}
	    public      void                popCurPath()
	                              { curPath.pop(); }
	     	    	    
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
	     
	     //! Allows subclass instance to indicate that traversal has reached
	          //! a termination condition
	     protected void setTerminated(boolean flag)      { terminated = flag; }
	      	   	
	   	
	   	
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
	     
	    private void setUpState()
	     //
	     {
	         boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeApplyActionCB()!=null);
	         if (profilingEntered) {
	           SoProfiling.getEnterScopeApplyActionCB().run(this);
	         }
	     
	         // Setup method traversal table
	         traversalMethods.setUp();
	     
	         // Create state if necessary.  When an new or different element is
	         // enabled, the recreateState flag is set.
	         if (state == null ||
	             enabledElementsCounter != SoEnabledElementsList.getCounter()) {
	     
	             if (state != null) {
					state.destructor();
	             }
	     
	             state = new SoState(this, getEnabledElements().getElements());
	     
	             // Store current counter in instance
	             enabledElementsCounter = SoEnabledElementsList.getCounter();
	         }   
	     }

	    //
	     // Description:
	     //    Cleans up after an action has been applied.
	     //
	     // Use: private
	     
	    private void
	     cleanUp()
	     //
	     {
	         boolean profilingEntered = SoProfiling.isEnabled() && (SoProfiling.getEnterScopeApplyActionCB()!=null)
	                                 && SoProfiling.getLeaveScopeCB() != null;
	         if (profilingEntered) {
	           SoProfiling.getLeaveScopeCB().run();
	         }
	     
	         switch (appliedTo.code) {
	     
	           case NODE:
	             if (appliedTo.node != null) {
	                 appliedTo.node.unref();
	                 appliedTo.node = null;
	             }
	             break;
	     
	           case PATH:
	             if (appliedTo.path != null) {
	                 appliedTo.path.unref();
	                 appliedTo.path = null;
	             }
	             break;
	     
	           case PATH_LIST:
	             appliedTo.pathList = null;
	             break;
	         }
	     
	         curPath.truncate(0);
	     }
	    
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
    if (curPath.getTail() != (SoFullPath.cast(getCurPath())).getTail()){
        SoDebugError.post("SoAction::getCurPathTail\n", 
        "Inconsistent path tail.  Did you change the scene graph\n"+
        "During traversal?\n");
    }
//#endif /*DEBUG*/
    return(curPath.getTail());
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
	   
	     
	   }
