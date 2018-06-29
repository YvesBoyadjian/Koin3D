/**
 * 
 */
package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoViewingMatrixElement extends SoReplacedElement {

	   protected
		        final SbMatrix            viewingMatrix = new SbMatrix();
		   	
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    super.init(state);

    viewingMatrix.makeIdentity();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets viewing matrix to given matrix in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, SoNode node,
                            final  SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    SoViewingMatrixElement      elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoViewingMatrixElement ) getElement(state, classStackIndexMap.get(SoViewingMatrixElement.class), node);

    if (elt != null)
        elt.setElt(matrix);
}

	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns viewing matrix from state.
	   //
	   // Use: public, static
	   
	  public static SbMatrix 
	   get(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       final SoViewingMatrixElement elt;
	   
	       elt = ( SoViewingMatrixElement )
	           getConstElement(state, classStackIndexMap.get(SoViewingMatrixElement.class));
	   
	       return elt.viewingMatrix;
	   }
	   
	  public static void
	   initClass(final Class<? extends SoElement> javaClass)
	   {
		  SoElement.initClass(javaClass);
	   }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets viewing matrix in element to given matrix.
//
// Use: protected, virtual

protected void
setElt(final SbMatrix matrix)
//
////////////////////////////////////////////////////////////////////////
{
    viewingMatrix.copyFrom(matrix);
}
}
