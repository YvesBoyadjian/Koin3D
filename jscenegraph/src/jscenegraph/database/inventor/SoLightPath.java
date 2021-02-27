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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file contains the definition of the SoPath and SoFullPath
 |      classes, and the related class SoLightPath
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson, Alan Norton
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor;

import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoTempPath;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoLightPath
///
///  A SoLightPath is a light-weight version of an SoTempPath, intended to
///  be used just to keep track of the current path during traversal.
///  Eventually it is intended to replace SoTempPath (when appropriate 
///  modifications are made to SoDraggers).
///  Unlike SoPath, it keeps only a chain of childIndices and a headnode.
///  The methods implemented are only those needed for traversal.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoLightPath implements Destroyable {

    private SoNode headNode;       
    private final     SbListInt           indices;        
  	
	// Constructor given approximate number of nodes in chain. 
	public SoLightPath(int approxLength) {
		indices = new SbListInt(approxLength);
	}
	
	public void destructor() {
		truncate(0);
	}
	
	// Sets head node (first node in chain) Resulting path has only one node. 
	//
	  // Description:
	  //    Sets the head (first) node of the path chain. The path is
	  //    cleared first, if necessary.
	  //
	  // Use: public
	  	public void setHead(SoNode node) {
		
	  	     // Clear out existing nodes
	  		      truncate(0);
	  		  
	  		      // The head index value is set to -1, it should never be referenced. 
	  		      indices.append(-1);
	  		      headNode = node;
	  		 	}
	  	
	     //! Adds node specified by childindex to end of chain. 
	    public     void                append(int childIndex)
	             { indices.append(childIndex);}
	     
	    public void push(int childIndex) { append(childIndex); }
	    public void                push()                  { append(-1);}
	    public void pop() { truncate(getFullLength() - 1); }	    
	    public void                setTail(int childIndex)
	                      { indices.set(getFullLength()-1,childIndex);}
	     
	  	// Removes all nodes from indexed node on. 
	  	public void truncate(int start) {
	  		 indices.truncate(start);
	  	}
	  	
	     //! Returns full length of path chain (number of nodes)
	         //! note that public/private distinction is ignored.
	    public int getFullLength() {return indices.getLength();}
	     	  	

	  	 ////////////////////////////////////////////////////////////////////////
	  	  //
	  	  // Description:
	  	  //    fills in values for a full (Temp) path that is a copy of
	  	  //    all of this LightPath. The temp path should already be constructed. 
	  	  //
	  	  // Use: public
	  	  
	  	  public void
	  	  makeTempPath(SoTempPath tmpPath)
	  	  //
	  	  ////////////////////////////////////////////////////////////////////////
	  	  {
	  	      int         i;
	  	  
	  	      if (tmpPath == null){
	  	          SoDebugError.post("SoLightPath::makeTempPath",
	  	                           "Error, NULL SoTempPath");
	  	          return;
	  	          }
	  	  
	  	      tmpPath.setHead(headNode);
	  	      for (i = 1; i < getFullLength(); i++)
	  	          {
//	  	  #ifdef DEBUG
	  	          if (indices.operator_square_bracket(i) < 0 ) {
	  	              SoDebugError.post("SoLightPath::makeTempPath",
	  	                           "Error, invalid indices of child");
	  	              return;
	  	              }
//	  	  #endif
	  	          tmpPath.append(indices.operator_square_bracket(i));
	  	          }
	  	      return;
	  	  }

	  	  //////////////////////////////////////////////////////////////
	  	  //
	  	  // Description:
	  	  //    Returns SoNode that is ith node in path
	  	  //    (where 0th node is head node)
	  	  //
	  	  //    use: public
	  	  //
	  	 public SoNode 
	  	  getNode(int i)
	  	  //
	  	  /////////////////////////////////////////////////////////////
	  	  {
	  	      SoNode curNode = headNode;
	  	      SoChildList children;
	  	      for(int j=0; j<i; j++){
	  	          //traverse all (even hidden) children
	  	          children = curNode.getChildren();
//	  	  #ifdef DEBUG
	  	          // Make sure it's valid:
	  	          if(children == null || j>= getFullLength()) {
	  	              SoDebugError.post("SoLightPath::getNode",
	  	                           "Error, no child at level");
	  	          }
//	  	  #endif
	  	          curNode = (children).operator_square_bracket(indices.operator_square_bracket(j+1));
	  	      }
	  	      return curNode;
	  	  }	  	  

	  	 public     SoNode             getTail()
                { return getNode(getFullLength()-1);}
	  	 
	     //! Returns the first node in a path chain.
	   public SoNode             getHead() { return headNode; }

}
