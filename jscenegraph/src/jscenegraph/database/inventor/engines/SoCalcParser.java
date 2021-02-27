/**
 * 
 */
package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class SoCalcParser {
	
	interface lookupFloatI {
		public float[] lookupFloat(Object data, String nm);		
	}

	interface lookupVec3fI {
		public SbVec3f lookupVec3f(Object data, String nm);		
	}

	private Object data;
	
	private ExprList elist;
	
	private lookupFloatI lookupFloatField;
	private lookupVec3fI lookupVec3fField;
	
	public SoCalcParser(lookupFloatI lf, lookupVec3fI lv, SoCalculator d) {
    lookupFloatField = lf;
    lookupVec3fField = lv;
    data = d;

    elist = new ExprList();
	}

	public void destructor() {
		elist.destructor();
	}
	

public boolean
parse(String buf)
{
    Expr.lookupFloatField = lookupFloatField;
    Expr.lookupVec3fField = lookupVec3fField;
    Expr.data = data;

    Expr.error = false;

//    extern bool SoCalcParse(ExprList *, const char *);

    if (SoCalcParse(elist, buf))
        return false;

// Enable this to print all parsed expressions
//#if DEBUG_PARSED_RESULT
//    elist.print();
//#endif

    return !Expr.error;
}
	
	
    private boolean SoCalcParse(ExprList elist2, String buf) {
	// TODO Auto-generated method stub
	return false;
}

	//! evaluate the collection of expressions
    public void        eval() { elist.eval(); }

    //! empty the collection of parsed expressions
    public void        clear() { elist.truncate(0); }

	
}
