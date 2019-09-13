/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.database.inventor.SbColor4f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureCombineElement extends SoElement {

	  public enum Source {
		    PRIMARY_COLOR ( 0x8577),
		    TEXTURE ( 0x1702),
		    CONSTANT ( 0x8576),
		    PREVIOUS (  0x8578);
		    
		    Source(int value) {
		    	this.value = value;
		    }
		    
		    private int value;
		    
		    public int getValue() {
		    	return value;
		    }

			public static Source fromValue(int value2) {
				switch(value2) {
				case 0x8577: return PRIMARY_COLOR;
				case 0x1702: return TEXTURE;
				case 0x8576: return CONSTANT;
				case 0x8578: return PREVIOUS;
				}
				return null;
			}
		  };
		  public enum Operand {
		    SRC_COLOR ( 0x0300),
		    ONE_MINUS_SRC_COLOR ( 0x0301),
		    SRC_ALPHA ( 0x0302),
		    ONE_MINUS_SRC_ALPHA ( 0x0303);
		    
		    Operand(int value) {
		    	this.value = value;
		    }
		    
		    private int value;
		    
		    public int getValue() {
		    	return value;
		    }

			public static Operand fromValue(int value2) {
				switch(value2) {
				case 0x0300: return SRC_COLOR;
				case 0x0301: return ONE_MINUS_SRC_COLOR;
				case 0x0302: return SRC_ALPHA;
				case 0x0303: return ONE_MINUS_SRC_ALPHA;
				}
				return null;
			}
		  };
		  public enum Operation {
		    REPLACE ( 0x1E01),
		    MODULATE ( 0x2100),
		    ADD ( 0x0104),
		    ADD_SIGNED ( 0x8574),
		    SUBTRACT ( 0x84E7),
		    INTERPOLATE ( 0x8575),
		    DOT3_RGB ( 0x86AE),
		    DOT3_RGBA ( 0x86AF);
		    
		    Operation(int value) {
		    	this.value = value;
		    }
		    
		    private int value;
		    
		    public int getValue() {
		    	return value;
		    }

			public static Operation fromValue(Integer value2) {
				for(Operation o: values()) {
					if(o.getValue() == value2) {
						return o;
					}
				}
				return null;
			}
		  };
		  
		  public static class UnitData implements Mutable {
			  public
			    UnitData() {
				  nodeid = 0;
				    rgboperation =  Operation.REPLACE;
				    alphaoperation = Operation.REPLACE;
				    constantcolor.copyFrom( new SbColor4f(0.0f, 0.0f, 0.0f, 0.0f));
				    rgbscale = 1.0f;
				    alphascale = 1.0f;
				
				  for (int i = 0; i < 3; i++) {
				    rgbsource[i] = Source.CONSTANT;
				    alphasource[i] = Source.CONSTANT;
				    rgboperand[i] = Operand.SRC_COLOR;
				    alphaoperand[i] = Operand.SRC_COLOR;
				  }
				
				  
			  }
			    public UnitData( UnitData org) {
			    	copyFrom(org);
			    }
			    public void copyFrom( Object other) {
			    	UnitData org = (UnitData)other;
				    nodeid = org.nodeid;
				    rgbsource[0] = org.rgbsource[0];
				    rgbsource[1] = org.rgbsource[1];
				    rgbsource[2] = org.rgbsource[2];
				    alphasource[0] = org.alphasource[0];
				    alphasource[1] = org.alphasource[1];
				    alphasource[2] = org.alphasource[2];
				    rgboperand[0] = org.rgboperand[0];
				    rgboperand[1] = org.rgboperand[1];
				    rgboperand[2] = org.rgboperand[2];
				    alphaoperand[0] = org.alphaoperand[0];
				    alphaoperand[1] = org.alphaoperand[1];
				    alphaoperand[2] = org.alphaoperand[2];
				    rgboperation = org.rgboperation;
				    alphaoperation = org.alphaoperation;
				    constantcolor.copyFrom(org.constantcolor);
				    rgbscale = org.rgbscale;
				    alphascale = org.alphascale;			    	
			    }

			    int nodeid;
			    Source[] rgbsource = new Source[3];
			    Source[] alphasource = new Source[3];
			    Operand[] rgboperand = new Operand[3];
			    Operand[] alphaoperand = new Operand[3];
			    Operation rgboperation;
			    Operation alphaoperation;
			    final SbColor4f constantcolor = new SbColor4f();
			    float rgbscale;
			    float alphascale;
			  };

		  

private static class SoTextureCombineElementP implements Destroyable {
public
  void ensureCapacity(int unit) {
    while (unit >= this.unitdata.getLength()) {
      this.unitdata.append(new SoTextureCombineElement.UnitData());
    }
  }
  public final SbListOfMutableRefs<SoTextureCombineElement.UnitData> unitdata = new SbListOfMutableRefs<>(()->new SoTextureCombineElement.UnitData());
@Override
public void destructor() {
	unitdata.destructor();
}
};

//SO_ELEMENT_CUSTOM_CONSTRUCTOR_SOURCE(SoTextureCombineElement);

	private SoTextureCombineElementP pimpl;

public SoTextureCombineElement()
{
  this.pimpl = new SoTextureCombineElementP();

//  this.setTypeId(SoTextureCombineElement::classTypeId);
//  this.setStackIndex(SoTextureCombineElement::classStackIndex);
}

/*!
  This static method initializes static data for the
  SoTextureCombineElement class.
*/

//void
//SoTextureCombineElement::initClass(void)
//{
//  SO_ELEMENT_INIT_CLASS(SoTextureCombineElement, inherited);
//}

/*!
  The destructor.
*/

public void destructor()
{
  Destroyable.delete(this.pimpl);
  super.destructor();
}

//! FIXME: write doc.

public void
init(SoState state)
{
  super.init(state);
}

//! FIXME: write doc.
public static void
set(SoState state, SoNode node,
                             int unit,
                             Operation rgboperation,
                             Operation alphaoperation,
                             Source[] rgbsource,
                             Source[]alphasource,
                             Operand[] rgboperand,
                             Operand[] alphaoperand,
                             final SbColor4f constantcolor,
                             float rgbscale,
                             float alphascale)
{
  SoTextureCombineElement elem = (SoTextureCombineElement)
    (state.getElement(classStackIndexMap.get(SoTextureCombineElement.class)));
  elem.pimpl.ensureCapacity(unit);
  elem.setElt(unit, node.getNodeId(),
               rgboperation,
               alphaoperation,
               rgbsource,
               alphasource,
               rgboperand,
               alphaoperand,
               constantcolor,
               rgbscale,
               alphascale);
}


//! FIXME: write doc.

public static void
get(SoState state,
                             int unit,
                             final Operation[] rgboperation,
                             final Operation[] alphaoperation,
                             final Source[] rgbsource,
                             final Source[] alphasource,
                             final Operand[] rgboperand,
                             final Operand[] alphaoperand,
                             final SbColor4f constantcolor,
                             final float[] rgbscale,
                             final float[] alphascale)
{
  SoTextureCombineElement elem =
    (SoTextureCombineElement)
    (getConstElement(state, classStackIndexMap.get(SoTextureCombineElement.class)));
  
  assert(unit < elem.pimpl.unitdata.getLength());
  UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);
  
  rgboperation[0] = ud.rgboperation;
  alphaoperation[0] = ud.alphaoperation;
  for( int i=0;i<3;i++) {
  rgbsource[i] = ud.rgbsource[i];
  alphasource[i] = ud.alphasource[i];
  rgboperand[i] = ud.rgboperand[i];
  alphaoperand[i] =  ud.alphaoperand[i];
  }
  constantcolor.copyFrom( ud.constantcolor);
  rgbscale[0] = ud.rgbscale;
  alphascale[0] = ud.alphascale;
}


public static boolean
isDefault(SoState state,
                                   int unit)
{
  SoTextureCombineElement elem =
    (SoTextureCombineElement)
    (getConstElement(state, classStackIndexMap.get(SoTextureCombineElement.class)));

  if (unit < elem.pimpl.unitdata.getLength()) {
    return elem.pimpl.unitdata.operator_square_bracket(unit).nodeid == 0;
  }
  return true;
}


public SoTextureCombineElement.UnitData
getUnitData( int unit)
{
  assert(unit < this.pimpl.unitdata.getLength());
  return this.pimpl.unitdata.operator_square_bracket(unit);
}

public void
push(SoState state)
{
  SoTextureCombineElement prev = (SoTextureCombineElement)
    (this.getNextInStack());
  this.pimpl.unitdata.copyFrom( prev.pimpl.unitdata);
}

public boolean
matches( SoElement elem) 
{
  SoTextureCombineElement  e =
    (SoTextureCombineElement)(elem);
  int n = e.pimpl.unitdata.getLength();
  if (n != this.pimpl.unitdata.getLength()) return false;

  for (int i = 0; i < n; i++) {
    if (e.pimpl.unitdata.operator_square_bracket(i).nodeid != this.pimpl.unitdata.operator_square_bracket(i).nodeid) {
      return false;
    }
  }
  return true;
}

public SoElement 
copyMatchInfo()
{
  SoTextureCombineElement elem =
    (SoTextureCombineElement)(getTypeId().createInstance());
  elem.pimpl.unitdata.copyFrom(this.pimpl.unitdata);
  return elem;
}


//! FIXME: write doc.

public void
setElt( int unit,
                                int nodeid,
                                Operation rgboperation,
                                Operation alphaoperation,
                                Source[] rgbsource,
                                Source[] alphasource,
                                Operand[] rgboperand,
                                Operand[] alphaoperand,
                                SbColor4f  constantcolor,
                                float rgbscale,
                                float alphascale)

{
  this.pimpl.ensureCapacity(unit);
  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);

  ud.nodeid = nodeid;
  ud.rgboperation = rgboperation;
  ud.alphaoperation = alphaoperation;
  for(int i=0;i<3;i++) {
  ud.rgbsource[i]= rgbsource[i];
  ud.alphasource[i]= alphasource[i];
  ud.rgboperand[i]= rgboperand[i];
  ud.alphaoperand[i]= alphaoperand[i];
  }
  ud.constantcolor.copyFrom( constantcolor);
  ud.rgbscale = rgbscale;
  ud.alphascale = alphascale;
}

public static void
apply(SoState state, int unit)
{
  SoTextureCombineElement elem =
   (SoTextureCombineElement)
    (getConstElement(state, classStackIndexMap.get(SoTextureCombineElement.class)));

  assert(unit < elem.pimpl.unitdata.getLength());
  UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);
  
  GL2 gl2 = state.getGL2();
  
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_RGB, (int)(ud.rgboperation.getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_COMBINE_ALPHA, (int)(ud.alphaoperation.getValue()));

  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_RGB, (int)(ud.rgbsource[0].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE1_RGB, (int)(ud.rgbsource[1].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE2_RGB, (int)(ud.rgbsource[2].getValue()));

  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE0_ALPHA, (int)(ud.alphasource[0].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE1_ALPHA, (int)(ud.alphasource[1].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_SOURCE2_ALPHA, (int)(ud.alphasource[2].getValue()));

  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_RGB, (int)(ud.rgboperand[0].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND1_RGB, (int)(ud.rgboperand[1].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND2_RGB, (int)(ud.rgboperand[2].getValue()));

  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND0_ALPHA, (int)(ud.alphaoperand[0].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND1_ALPHA, (int)(ud.alphaoperand[1].getValue()));
  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_OPERAND2_ALPHA, (int)(ud.alphaoperand[2].getValue()));

  gl2.glTexEnvfv(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_COLOR,
             ud.constantcolor.getValueRead(),0);
  gl2.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_RGB_SCALE, ud.rgbscale);
  gl2.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_ALPHA_SCALE, ud.alphascale);
}


//#undef PRIVATE
}
