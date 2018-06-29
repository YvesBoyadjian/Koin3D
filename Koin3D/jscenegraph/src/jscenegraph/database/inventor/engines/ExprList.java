/**
 * 
 */
package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbPList;

/**
 * @author Yves Boyadjian
 *
 */
public class ExprList extends SbPList {

	public     Expr operator_square_bracket(int i) 
        { return (Expr) super.operator_square_bracket(i); }
	
	public void
	eval()
	{
	    for (int i=0; i<getLength(); i++)
	        (this).operator_square_bracket(i).eval();
	}

}
