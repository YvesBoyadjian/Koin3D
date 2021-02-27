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
 |      Defines the SoSearchAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Searches for nodes in a scene graph.
/*!
\class SoSearchAction
\ingroup Actions
This class is used to search scene graphs for specific nodes, nodes of
a specific type, nodes with a specific name, or any combination of
these. It can search for just the first or last node satisfying the
criteria or for all such nodes. The actions return paths to each node
found.


Note that by default nodekits do not search their children when a search 
action is applied.  The man page for SoBaseKit discusses the methods
SoBaseKit::isSearchingChildren() and
SoBaseKit::setSearchingChildren(),
which allow you to query and control this behavior.

\par See Also
\par
SoPath, SoBaseKit
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSearchAction extends SoAction implements Destroyable {

	public                                                                     
    SoType              getTypeId() {
		return classTypeId; 
	}
   public static SoType               getClassTypeId()                              
                                   { return classTypeId; }                   
 public                                                          
   static void                 addMethod(SoType t, SoActionMethod method)    
                                   { methods.addMethod(t, method); }
 // java port
 public  static void                 enableElement(Class<?> klass)         
 { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}
 
  public  static void                 enableElement(SoType t, int stkIndex)         
                                   { enabledElements.enable(t, stkIndex);}  
 protected                                                                  
   SoEnabledElementsList  getEnabledElements()  {
	  return enabledElements;
 }
   protected static SoEnabledElementsList enabledElements;                            
   protected static SoActionMethodList   methods;                                     
 private                                                                
   static SoType               classTypeId;

	
    public enum LookFor {
           NODE(0x01),    
           TYPE(0x02),    
           NAME(0x04);
           
           private final int id;
           LookFor(int id) {
        	   this.id = id;
           }
           public int getValue() { return id; }
       };
   
      public enum Interest {
          FIRST,          
          LAST,           
          ALL             
      };
      
      //! This flag is used by the SoSwitch node, which must return a
           //! different result from its 'affectsState' method when called
           //! during a SearchAction that is searching all children.
      public static boolean       duringSearchAll;
             
      
      final SoType              type = new SoType();           
      boolean              derivedOk;      
      private SoNode node;
      private final SbName name = new SbName(); //!< Name of node to search for       private int lookingFor;
      private int lookingFor; //!< What to search for
      private Interest interest;  
      private boolean searchingAll;  
      private SoPath retPath;
      private final SoPathList retPaths = new SoPathList();       //!< Found nodes (if interest == ALL)
        

      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Constructor. Sets up default values: search for first node, no
       //    exact match necessary.
       //
       // Use: public
       
       public SoSearchAction()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           //SO_ACTION_CONSTRUCTOR(SoSearchAction);
    	   traversalMethods = methods;
       
           retPath = null;
           reset();
       }
       

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (retPath != null)
        retPath.unref();
    retPaths.destructor();
    super.destructor();
}

       
       
       //! Returns what to look for.
            //! \see setFind
       public int getFind() { return lookingFor; }
              
       //! Returns the node to search for.
       public     SoNode getNode() { return node; }
                    
	// Sets the node to search for. 
	public void setNode(SoNode n) {
	     node = n;
	      
	          if (n == null)
	              lookingFor &= ~LookFor.NODE.getValue();
	          else
	              lookingFor |= LookFor.NODE.getValue();
	     		
	}
	
    //! Gets the node type to search for. If \p derivedIsOk is TRUE,
      //! a node that is of a type that is derived from \p t will pass this
      //! search criterion.
    public  SoType              getType(boolean[] derivedIsOk)
          { derivedIsOk[0] = derivedOk; return type; }
 	
	public void setType(SoType t) {
		setType(t, true);
	}
	
	/**
	 * Sets the node type to search for. 
	 * If derivedIsOk is TRUE, a node that is of a type that is derived 
	 * from t will pass this search criterion. 
	 * 
	 * @param t
	 * @param derivedIsOk
	 */
	 //
	   // Description:
	   //    Sets the search to look for nodes of a specific type, or to look
	   //    for no type if BadType is passed in.  The derivedOk flag sets
	   //    whether or not the types need to match exactly.
	   //
	   // Use: public
	  
	  public void setType(SoType t, boolean derivedIsOk) {
		     derivedOk = derivedIsOk;
		          type.copyFrom(t);
		      
		          if (t.isBad())
		              lookingFor &= ~LookFor.TYPE.getValue();
		      
		          else {
//		      #ifdef DEBUG
		              if (! t.isDerivedFrom(SoNode.getClassTypeId()))
		                  SoDebugError.postWarning("SoSearchAction::setType",
		                                            "Type "+t.getName().getString()+" is not derived from SoNode");
//		      #endif /* DEBUG */
		      
		              lookingFor |= LookFor.TYPE.getValue();
		          }
		     		
	}
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the name of the node to look for.
//
// Use: public

public void
setName(final SbName n)
//
////////////////////////////////////////////////////////////////////////
{
    name.copyFrom(n);
    lookingFor |= LookFor.NAME.getValue();
}

	  
	     //! Sets/returns the name of the node to search for.
	  public SbName getName() { return name; }

	     //! Sets/returns which paths to return. Default is FIRST.
	  public     Interest            getInterest() { return interest; }
	  	  
	  // Sets/returns which paths to return. Default is FIRST. 
	  public void setInterest(Interest i) {
		  interest = i;    
	  }
	  
	     //! \see setSearchingAll
	  public     boolean              isSearchingAll() { return searchingAll; }
	  	  
	  /**
	   * Sets/returns whether searching uses regular traversal or 
	   * whether it traverses every single node. 
	   * For example, if this flag is FALSE, an SoSwitch node will 
	   * traverse only the child or children it would normally 
	   * traverse for an action. 
	   * If the flag is TRUE, the switch would always traverse all 
	   * of its children. The default is FALSE. 
	   * 
	   * @param flag
	   */
	public  void setSearchingAll(boolean flag) { searchingAll = flag; }
	
	/**
	 * Returns resulting path, or NULL if no path was found. 
	 * This should be used if the interest is FIRST or LAST. 
	 * 
	 * @return
	 */
	public SoPath getPath() {
		 return retPath; 
	}

    //! Returns resulting path list. This should be used if the interest is ALL.
    public SoPathList getPaths()              { return retPaths; }
  
    //! Sets/returns whether action has found all desired nodes
    public void                setFound()              { setTerminated(true); }
    public boolean isFound() { return hasTerminated(); }
 
	public void setNodeClass(Class<? extends SoNode> klass, boolean derivedIsOk) {
		
		Method method;
		try {
			method = klass.getMethod("getClassTypeId");
			SoType t = (SoType)method.invoke(null);
			setType(t,derivedIsOk);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}			
		
	}

	//! Resets options back to default values; clears list of returned paths.
	       //! This can be used to apply the action again with a different set of
	       //! search criteria.
	       	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Reset everything back to just-constructed state.
	    //
	    // Use: public
	   
	  public void
	   reset()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       if (retPath != null)
	           retPath.unref();
	   
	       retPath = null;
	       retPaths.truncate(0);
	   
	       setType(SoType.badType());
	       setNode(null);
	       setFind(0);
	       setInterest(Interest.FIRST);
	       setSearchingAll(false);
	   }
	   	
	     //! Sets what to look for; \p what is a bitmask of <tt>LookFor</tt>
	       //! enum values. Default is no flags at all. Note that setting a node,
	       //! type, and/or name to search for activates the relevant flag, so you
	       //! may never need to call this method directly.
	  public     void setFind(SoSearchAction.LookFor what) { lookingFor = what.getValue(); }
	  public     void setFind(int what) { lookingFor = what; }
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets the path to return (interest == FIRST or LAST) or adds to
	   //    the list of paths to return (interest == ALL).
	   //
	   // Use: extender
	   
	   public void
	   addPath(SoPath path)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       if (interest == Interest.ALL)
	           retPaths.append(path);
	   
	       else {
	           if (path != null)
	               path.ref();
	   
	           if (retPath != null)
	               retPath.unref();
	   
	           retPath = path;
	       }
	   }
	   	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initiates action on a graph.
	   //
	   // Use: protected
	   
	protected   void
	   beginTraversal(SoNode node)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
	       if (getFind() == 0) {
	           SoDebugError.post("SoSearchAction::apply",
	                              "No node type, node instance, or node name "+
	                              "specified for search");
	           return;
	       }
//	   #endif /* DEBUG */
	   
	       // Empty things so we can tell when we have a match
	       if (interest == Interest.ALL)
	           retPaths.truncate(0);
	       else
	           addPath(null);
	   
	       // Set duringSearchAll flag (being careful to preserve prev value
	       // in case of nested searches) so Switches are traversed properly:
	       boolean prevDuring = duringSearchAll;
	       duringSearchAll = searchingAll;
	   
	       traverse(node);
	   
	       duringSearchAll = prevDuring;
	   }	  
	  	  
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Initializes the SoSearchAction class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       //SO_ACTION_INIT_CLASS(SoSearchAction, SoAction);
		  enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
		    methods = new SoActionMethodList(SoAction.methods);                   
		    classTypeId    = SoType.createType(SoAction.getClassTypeId(),        
		                                        new SbName("SoSearchAction"), null);
	   }

	  ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Destructor.
	    //
	    // Use: public
	    
	   public void close()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        if (retPath != null)
	            retPath.unref();
	    }
	    }
