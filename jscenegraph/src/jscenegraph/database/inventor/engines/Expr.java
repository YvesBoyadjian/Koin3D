/**
 * 
 */
package jscenegraph.database.inventor.engines;

/**
 * @author Yves Boyadjian
 *
 */
//!
//! base expression class
//!
public class Expr {

    //! global flag gets set true if there was an error
    static boolean error;

    //! Functions to look up data storage locations by name.
    //! These should be set by the client before creating a parse tree.
    static SoCalcParser.lookupFloatI lookupFloatField;
    static SoCalcParser.lookupVec3fI lookupVec3fField;
    static Object data;
    
	public void eval() {
		// TODO Auto-generated method stub
		
	}
}
