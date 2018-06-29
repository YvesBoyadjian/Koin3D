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
 * Copyright (C) 1990,91,92   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file contains the declaration of the Calculator engine
 |
 |   Classes:
 |      SoCalculator
 |
 |   Author(s)          : Ronen Barzel
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.fields.SoMField;


/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! A general-purpose calculator.
/*!
\class SoCalculator
\ingroup Engines
This engine is a general-purpose calculator.
The calculator operates on floating-point values and 3D
floating-point vectors.
The engine takes up to eight inputs of each type 
(SoMFFloat and SoMFVec3f),
and produces up to four outputs of each type.


Each input field (\b a -\b h , \b A -\b H ) can have multiple values, 
allowing the engine to 
evaluate the expression with different values in parallel. 
Some inputs may have more values than others.  In such cases,
the last value of the shorter inputs will be repeated as necessary.


The \b expression  input string specifies the expression to be evaluated.
An expression can consist of multiple subexpressions.
Several subexpressions can be specified in one string, 
separated by semicolons (;).
Alternatively, the subexpressions can be stored in separate strings
in the multiple-valued input field.


Each subexpression is of the form:
\code
<lhs> = <rhs>
\endcode
The <tt>&lt;lhs&gt;</tt> can be any one of the outputs or a temporary variable.
The engine provides 8 temporary floating-point variables 
(ta, tb, tc, td, te, tf, tg, and th), 
and 8 temporary vector variables (tA, tB, tC, tD, tE, tF, tG, and tH). 
You can assign a value to one component of a vector output (\b A -\b H )
or a vector variable (\b tA -\b tH ) by using the [] operator.
For example, oA[0] = &lt;rhs&gt;, will evaluate the right hand side and assign
the value to the first component of the output vector \b oA .


The <tt>&lt;rhs&gt;</tt> supports arithmetic, logical and conditional operators.
They are:
\code
(unary)   !, -
(binary)  +, -, *, /, %, <, > <=, >=, ==, !=, &&, ||
(ternary) ? : 
\endcode
The ternary operator is a conditional operator.  For example,
<tt>a ? b : c</tt> evaluates to b if a != 0, and to c if a==0.


Valid operands for the <tt>&lt;rhs&gt;</tt> include the inputs, outputs, temporary
variables, and their components (e.g. oA[0]).
Operands can also be numeric constants (e.g. 1.0), 
pre-defined named constants, or pre-defined functions.


The named constants are:
\code
MAXFLOAT 
MINFLOAT  
M_E 
M_LOG2E 
M_LOG10E 
M_LN2 
M_LN10 
M_PI 
M_SQRT2  = sqrt(2)
M_SQRT1_2 = sqrt(1/2)
\endcode
Most of the pre-defined functions come from the math library:
\code
cos, sin, tan, 
acos, asin, atan, atan2, 
cosh, sinh, tanh,
sqrt, pow, exp, log, log10,
ceil, floor, fabs, fmod. 
\endcode
Other functions are defined by SoCalculator. They are:
\code
rand(f) - Random number generator
cross(v1, v2) - Vector cross product 
dot(v1, v2) - Vector dot product 
length(v) - Vector length
normalize(v) - Normalize vector
vec3f(f1, f2, f3) - Generate a vector from 3 floats
\endcode
The subexpressions are evaluated in order, so a variable
set in the &lt;lhs&gt; of an earlier expression may be used in the
&lt;rhs&gt; of a later expression.


Note, when the input has multiple values, all the 
subexpressions specified in the \b expression  are applied
to all the multiple input values.
This is unlike the SoBoolOperation engine, where each 
operation is applied only to the corresponding entries 
of the input data.
Note also, that even though the inputs and outputs can have multiple 
values the [] operator is only for indexing into the values of a 
single vector.
It does not index into the multiple values of a field.
For example, if the floating-point input field \b a  has two values:
1.0, and 2.0, then the expression
\code
"oA[0]=a; oA[1]=a; oA[2]=0.0"
\endcode
will produce two output vectors in \b oA : (1.0, 1.0, 0.0) and
(2.0, 2.0, 0.0).


Examples of expressions:
\code
"ta = oA[0]*floor(a)"
"tb = (a+b)*sin(M_PI)"
"oA = vec3f(ta, tb, ta+tb)"
"oB = normalize(oA)"
"ta = a; tb = sin(ta); oA = vec3f(ta, tb, 0)"
\endcode

\par File Format/Default
\par
\code
Calculator {
}
\endcode

\par See Also
\par
SoEngineOutput, SoBoolOperation
*/
////////////////////////////////////////////////////////////////////////////////

public class SoCalculator extends SoEngine {
	
	// SO_ENGINE_ABSTRACT_HEADER	
	
	public                                                                     
	  static SoType getClassTypeId() { return classTypeId; }                      
	public    SoType      getTypeId()  /* Returns type id   */
	{
		return classTypeId;
	}
	  public                                                                     
	     SoFieldData          getFieldData()  {
		  return inputData[0];
	  }
	public    SoEngineOutputData   getOutputData() {
		return outputData[0];
	}
	  public                                                                  
	    static SoFieldData[]         getInputDataPtr()                     
	        { return ( SoFieldData[])inputData; }                          
	 public   static SoEngineOutputData[] getOutputDataPtr()                    
	        { return ( SoEngineOutputData[])outputData; }                  
	  private                                                                    
	    static SoType       classTypeId;    /* Type id              */            
	  //private  static boolean       firstInstance = true;  /* True for first ctor call */        
	  private  static final SoFieldData[]  inputData = new SoFieldData[1];     /* Info on input fields */            
	  private  static final SoEngineOutputData[]  outputData = new SoEngineOutputData[1];            /* Info on outputs */ 
	  private  static final SoFieldData[][]    parentInputData = new SoFieldData[1][];      /* parent's fields */ 
	  private  static final SoEngineOutputData[][] parentOutputData = new SoEngineOutputData[1][];

	 // SO_ENGINE_ABSTRACT_HEADER
	
	  public static SoCalculator createInstance() {
		  return new SoCalculator();
	  }
	
	
	public final SoMFFloat a = new SoMFFloat(),b = new SoMFFloat(),c = new SoMFFloat(),d = new SoMFFloat(),e = new SoMFFloat(),f = new SoMFFloat(),g = new SoMFFloat(),h = new SoMFFloat();
	
	public final SoMFVec3f A = new SoMFVec3f(),B = new SoMFVec3f(),C = new SoMFVec3f(),D = new SoMFVec3f(),E = new SoMFVec3f(),F = new SoMFVec3f(),G = new SoMFVec3f(),H = new SoMFVec3f();

	public final SoMFString expression = new SoMFString();
	
	public final SoEngineOutput oa = new SoEngineOutput(),ob = new SoEngineOutput(),oc = new SoEngineOutput(),od = new SoEngineOutput();
	public final SoEngineOutput oA = new SoEngineOutput(),oB = new SoEngineOutput(),oC = new SoEngineOutput(),oD = new SoEngineOutput();
	 	
	
	private SoCalcParser parser;
	private boolean reparse;
	
    //! working storage for the evaluation
    final float[]       va = new float[1], vb = new float[1], vc = new float[1], vd = new float[1], ve = new float[1], vf = new float[1], vg = new float[1], vh = new float[1], ova = new float[1], ovb = new float[1], ovc = new float[1], ovd = new float[1];
    final float[]       ta = new float[1], tb = new float[1], tc = new float[1], td = new float[1], te = new float[1], tf = new float[1], tg = new float[1], th = new float[1];
    final SbVec3f     vA = new SbVec3f(), vB = new SbVec3f(), vC = new SbVec3f(), vD = new SbVec3f(), vE = new SbVec3f(), vF = new SbVec3f(), vG = new SbVec3f(), vH = new SbVec3f(), ovA = new SbVec3f(), ovB = new SbVec3f(), ovC = new SbVec3f(), ovD = new SbVec3f();
    final SbVec3f     tA = new SbVec3f(), tB = new SbVec3f(), tC = new SbVec3f(), tD = new SbVec3f(), tE = new SbVec3f(), tF = new SbVec3f(), tG = new SbVec3f(), tH = new SbVec3f();
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Constructor
//
//Use: public

	public SoCalculator() {
		super();
		engineHeader = SoSubEngine.SO_ENGINE_HEADER(SoCalculator.class,this);
	   	
		engineHeader.SO_ENGINE_CONSTRUCTOR(SoCalculator.class, inputData, outputData, parentInputData[0], parentOutputData[0]);
		engineHeader.SO_ENGINE_ADD_MINPUT(a,"a",        (0.0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(b,"b",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(c,"c",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(d,"d",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(e,"e",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(f,"f",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(g,"g",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(h,"h",        (0f));
		engineHeader.SO_ENGINE_ADD_MINPUT(A,"A",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(B,"B",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(C,"C",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(D,"D",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(E,"E",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(F,"F",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(G,"G",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(H,"H",        (new SbVec3f(0,0,0)));
		engineHeader.SO_ENGINE_ADD_MINPUT(expression,"expression",       (""));
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oa,"oa", SoMFFloat.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(ob,"ob", SoMFFloat.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oc,"oc", SoMFFloat.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(od,"od", SoMFFloat.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oA,"oA", SoMFVec3f.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oB,"oB", SoMFVec3f.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oC,"oC", SoMFVec3f.class);
		engineHeader.SO_ENGINE_ADD_MOUTPUT(oD,"oD", SoMFVec3f.class);

    parser = new SoCalcParser(SoCalculator::lookupFloat, SoCalculator::lookupVec3f, this);
    reparse = false;
    isBuiltIn = true;
	}
	
	public void destructor() {
		parser.destructor();
		super.destructor();
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    called when new inputs are presented
//
// Use: private

protected void
inputChanged(SoField which)
{
    if (which == expression)
        reparse = true;
}

	
	
	/* (non-Javadoc)
	 * @see com.openinventor.inventor.engines.SoEngine#evaluate()
	 */
	@Override
	protected void evaluate() {
    if (reparse) {
        parser.clear();
        boolean OK = true;
        int i;
        for (i=0; i<expression.getNum(); i++) {
            if (!parser.parse(expression.operator_square_bracket(i)/*.getString()*/)) {
                OK = false;
                break;
            }
        }
        if (! OK) {
//#ifdef DEBUG
            SoDebugError.post("SoCalculator::evaluate",
                               "Invalid expression '"+expression.operator_square_bracket(i)/*.getString()*/+"'");
//#endif /* DEBUG */

            expression.setValue("");
            parser.clear();
        }
        reparse = false;
    }

    int max = 0;
    int na = a.getNum(); if (na > max) max = na;
    int nb = b.getNum(); if (nb > max) max = nb;
    int nc = c.getNum(); if (nc > max) max = nc;
    int nd = d.getNum(); if (nd > max) max = nd;
    int ne = e.getNum(); if (ne > max) max = ne;
    int nf = f.getNum(); if (nf > max) max = nf;
    int ng = g.getNum(); if (ng > max) max = ng;
    int nh = h.getNum(); if (nh > max) max = nh;

    int nA = A.getNum(); if (nA > max) max = nA;
    int nB = B.getNum(); if (nB > max) max = nB;
    int nC = C.getNum(); if (nC > max) max = nC;
    int nD = D.getNum(); if (nD > max) max = nD;
    int nE = E.getNum(); if (nE > max) max = nE;
    int nF = F.getNum(); if (nF > max) max = nF;
    int nG = G.getNum(); if (nG > max) max = nG;
    int nH = H.getNum(); if (nG > max) max = nH;

    int nout = max;
    SoSubEngine.SO_ENGINE_OUTPUT(oa, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(ob, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(oc, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(od, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(oA, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(oB, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(oC, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));
    SoSubEngine.SO_ENGINE_OUTPUT(oD, SoMFFloat.class, (o)-> ((SoMField)o).setNum(nout));

    for (int i=0; i<nout; i++) {
        va[0] = a.operator_square_bracket(clamp(i,na));
        vb[0] = b.operator_square_bracket(clamp(i,nb));
        vc[0] = c.operator_square_bracket(clamp(i,nc));
        vd[0] = d.operator_square_bracket(clamp(i,nd));
        ve[0] = e.operator_square_bracket(clamp(i,ne));
        vf[0] = f.operator_square_bracket(clamp(i,nf));
        vg[0] = g.operator_square_bracket(clamp(i,ng));
        vh[0] = h.operator_square_bracket(clamp(i,nh));
        vA.copyFrom( A.operator_square_bracket(clamp(i,nA)));
        vB.copyFrom( B.operator_square_bracket(clamp(i,nB)));
        vC.copyFrom( C.operator_square_bracket(clamp(i,nC)));
        vD.copyFrom( D.operator_square_bracket(clamp(i,nD)));
        vE.copyFrom( E.operator_square_bracket(clamp(i,nE)));
        vF.copyFrom( F.operator_square_bracket(clamp(i,nF)));
        vG.copyFrom( G.operator_square_bracket(clamp(i,nG)));
        vH.copyFrom( H.operator_square_bracket(clamp(i,nH)));
        ova[0] = 0;
        ovb[0] = 0;
        ovc[0] = 0;
        ovd[0] = 0;
        ovA.copyFrom( new SbVec3f(0,0,0));
        ovB.copyFrom( new SbVec3f(0,0,0));
        ovC.copyFrom( new SbVec3f(0,0,0));
        ovD.copyFrom( new SbVec3f(0,0,0));

        parser.eval();
        
        final int ii = i;

        SoSubEngine.SO_ENGINE_OUTPUT(oa, SoMFFloat.class, (o) -> ((SoMFFloat)o).set1Value(ii, ova[0]));
        SoSubEngine.SO_ENGINE_OUTPUT(ob, SoMFFloat.class, (o) -> ((SoMFFloat)o).set1Value(ii, ovb[0]));
        SoSubEngine.SO_ENGINE_OUTPUT(oc, SoMFFloat.class, (o) -> ((SoMFFloat)o).set1Value(ii, ovc[0]));
        SoSubEngine.SO_ENGINE_OUTPUT(od, SoMFFloat.class, (o) -> ((SoMFFloat)o).set1Value(ii, ovd[0]));
        SoSubEngine.SO_ENGINE_OUTPUT(oA, SoMFVec3f.class, (o) -> ((SoMFVec3f)o).set1Value(ii, ovA));
        SoSubEngine.SO_ENGINE_OUTPUT(oB, SoMFVec3f.class, (o) -> ((SoMFVec3f)o).set1Value(ii, ovB));
        SoSubEngine.SO_ENGINE_OUTPUT(oC, SoMFVec3f.class, (o) -> ((SoMFVec3f)o).set1Value(ii, ovC));
        SoSubEngine.SO_ENGINE_OUTPUT(oD, SoMFVec3f.class, (o) -> ((SoMFVec3f)o).set1Value(ii, ovD));
    }
	}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    maps field names to storage locations
//
// Use: private, given as callback to the parser

public static float[] lookupFloat(Object data, String nm)
{
    SoCalculator e = (SoCalculator ) data;
    assert(e.isOfType(SoCalculator.getClassTypeId()));
    if (strcmp(nm, "a") ==0) return e.va;
    if (strcmp(nm, "b") ==0) return e.vb;
    if (strcmp(nm, "c") ==0) return e.vc;
    if (strcmp(nm, "d") ==0) return e.vd;
    if (strcmp(nm, "e") ==0) return e.ve;
    if (strcmp(nm, "f") ==0) return e.vf;
    if (strcmp(nm, "g") ==0) return e.vg;
    if (strcmp(nm, "h") ==0) return e.vh;
    if (strcmp(nm, "oa") ==0) return e.ova;
    if (strcmp(nm, "ob") ==0) return e.ovb;
    if (strcmp(nm, "oc") ==0) return e.ovc;
    if (strcmp(nm, "od") ==0) return e.ovd;
    if (strcmp(nm, "ta") ==0) return e.ta;
    if (strcmp(nm, "tb") ==0) return e.tb;
    if (strcmp(nm, "tc") ==0) return e.tc;
    if (strcmp(nm, "td") ==0) return e.td;
    if (strcmp(nm, "te") ==0) return e.te;
    if (strcmp(nm, "tf") ==0) return e.tf;
    if (strcmp(nm, "tg") ==0) return e.tg;
    if (strcmp(nm, "th") ==0) return e.th;
    return null;
}

public static SbVec3f lookupVec3f(Object data, String nm)
{
    SoCalculator e = (SoCalculator ) data;
    assert(e.isOfType(SoCalculator.getClassTypeId()));
    if (strcmp(nm, "A") ==0) return e.vA;
    if (strcmp(nm, "B") ==0) return e.vB;
    if (strcmp(nm, "C") ==0) return e.vC;
    if (strcmp(nm, "D") ==0) return e.vD;
    if (strcmp(nm, "E") ==0) return e.vE;
    if (strcmp(nm, "F") ==0) return e.vF;
    if (strcmp(nm, "G") ==0) return e.vG;
    if (strcmp(nm, "H") ==0) return e.vH;
    if (strcmp(nm, "oA") ==0) return e.ovA;
    if (strcmp(nm, "oB") ==0) return e.ovB;
    if (strcmp(nm, "oC") ==0) return e.ovC;
    if (strcmp(nm, "oD") ==0) return e.ovD;
    if (strcmp(nm, "tA") ==0) return e.tA;
    if (strcmp(nm, "tB") ==0) return e.tB;
    if (strcmp(nm, "tC") ==0) return e.tC;
    if (strcmp(nm, "tD") ==0) return e.tD;
    if (strcmp(nm, "tE") ==0) return e.tE;
    if (strcmp(nm, "tF") ==0) return e.tF;
    if (strcmp(nm, "tG") ==0) return e.tG;
    if (strcmp(nm, "tH") ==0) return e.tH;
    return null;
}

private static int strcmp(String a, String b) {
	return a.compareTo(b);
}
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoCalculator class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
	classTypeId = SoSubEngine.SO__ENGINE_INIT_CLASS(SoCalculator.class, "Calculator", SoEngine.class, parentInputData, parentOutputData);
}

	
}
