/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.SbVec4fArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMultiTextureCoordinateElement extends SoElement {

	public interface SoTextureCoordinateFunctionCB {
	
		SbVec4f apply(Object userdata,
            SbVec3f point,
            SbVec3f normal);
	}
	
	
	public static class UnitData implements Mutable {
		
	    public int nodeid;
	    public CoordType whatKind;
	    public SoTextureCoordinateFunctionCB funcCB;
	    public Object funcCBData;
	    public int numCoords;
	    public SbVec2fArray coords2;
	    public SbVec3fArray coords3;
	    public SbVec4fArray coords4;
	    int coordsDimension;
	    
		  public UnitData() {
		  nodeid = 0;
		    whatKind = CoordType.DEFAULT;
		    funcCB = null;
		    funcCBData = null;
		    numCoords = 0;
		    coords2 = null;
		    coords3 = null;
		    coords4 = null;
		    coordsDimension = 2;
		
		}

			public UnitData(final UnitData org) {
				copyFrom(org);
			}
		public void copyFrom(Object other) {
			final UnitData org = (UnitData)other;
		  nodeid = org.nodeid;
		    whatKind = org.whatKind;
		    funcCB =org.funcCB;
		    funcCBData = org.funcCBData;
		    numCoords = org.numCoords;
		    coords2 = org.coords2;
		    coords3 = org.coords3;
		    coords4 = org.coords4;
		    coordsDimension = org.coordsDimension;
		
		}
	    
	}
	
	  public enum CoordType {
		    NONE_TEXGEN(0),
		    EXPLICIT(1),
		    FUNCTION(2),
		    DEFAULT(3);
		    
		    private int value;
		    
		    CoordType(int value) {
		    	this.value = value;
		    }
		  };
		  
		  SoMultiTextureCoordinateElementP pimpl;

	protected final SbVec2f convert2 = new SbVec2f();
	protected final SbVec3f convert3 = new SbVec3f();
	protected final SbVec4f convert4 = new SbVec4f();

	  // Coin-3 support
	  public SbVec4f get( SbVec3f point,
	                      SbVec3f normal) {
	    return this.get(0, point, normal);
	  }
	  
	  public static void setFunction(SoState state, 
	                          SoNode node,
	                          SoTextureCoordinateFunctionCB func,
	                          Object userdata) {
	    setFunction(state, node, 0, func, userdata);
	  }

	  public static void set2(SoState state, SoNode node,
	                   int numCoords, SbVec2fArray coords) {
	    set2(state, node, 0, numCoords, coords);
	  }
	  public static void set3(SoState state, SoNode node,
	                   int numCoords, SbVec3fArray coords) {
	    set3(state, node, 0, numCoords, coords);
	  }
	  public static void set4(SoState state, SoNode node,
	                   int numCoords, SbVec4fArray coords) {
	    set4(state, node, 0, numCoords, coords);
	  }
	  public SbVec2f get2(int index) {
	    return this.get2(0, index);
	  }
	  public SbVec3f get3(int index) {
	    return this.get3(0, index);
	  }
	  public SbVec4f get4( int index) {
	    return this.get4(0, index);
	  }


	static class SoMultiTextureCoordinateElementP {
	public final SbListOfMutableRefs<SoMultiTextureCoordinateElement.UnitData> unitdata = new SbListOfMutableRefs<>(()->new SoMultiTextureCoordinateElement.UnitData());

	  void ensureCapacity(int units) {
	    for (int i = this.unitdata.getLength(); i <= units; i++) {
	      this.unitdata.append(new SoMultiTextureCoordinateElement.UnitData());
	    }
	  }

	public void destructor() {
		unitdata.destructor();		
	}
	};

	//SO_ELEMENT_CUSTOM_CONSTRUCTOR_SOURCE(SoMultiTextureCoordinateElement);

	/*!
	  This static method initializes static data for the
	  SoMultiTextureCoordinateElement class.
	*/

//	void
//	SoMultiTextureCoordinateElement::initClass()
//	{
//	  SO_ELEMENT_INIT_CLASS(SoMultiTextureCoordinateElement, inherited);
//	}


	/*!
	  The constructor.
	*/
	public SoMultiTextureCoordinateElement()
	{
	  this.pimpl = new SoMultiTextureCoordinateElementP();

	  //this.setTypeId(SoMultiTextureCoordinateElement::classTypeId);
	  //this.setStackIndex(SoMultiTextureCoordinateElement::classStackIndex);
	}

	/*!
	  The destructor.
	*/

	public void destructor()
	{
	  this.pimpl.destructor();
	  super.destructor();
	}

	//! FIXME: write doc.

	public static void
	setDefault(SoState state,
	                                            SoNode node,
	                                            int unit)
	{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setTexCoordVBO(state, unit, null);
	  }
	  SoMultiTextureCoordinateElement element =
	    (SoMultiTextureCoordinateElement)
	    (SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class)));

	  element.pimpl.ensureCapacity(unit);
	  UnitData ud = element.pimpl.unitdata.operator_square_bracket(unit);
	  ud.nodeid = 0;
	  ud.whatKind = CoordType.DEFAULT;
	  ud.numCoords = 0;
	}

	//! FIXME: write doc.

	public static void
	setFunction(SoState state,
	                                             SoNode node,
	                                             int unit,
	                                             SoTextureCoordinateFunctionCB func,
	                                             Object userdata)
	{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setTexCoordVBO(state, unit, null);
	  }

	  SoMultiTextureCoordinateElement element =
	    (SoMultiTextureCoordinateElement)
	    (SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class)));

	  element.pimpl.ensureCapacity(unit);
	  UnitData ud = element.pimpl.unitdata.operator_square_bracket(unit);

	  ud.nodeid = node.getNodeId();
	  ud.funcCB = func;
	  ud.funcCBData = userdata;
	  ud.whatKind = CoordType.FUNCTION;
	  ud.coords2 = null;
	  ud.coords3 = null;
	  ud.coords4 = null;
	  ud.numCoords = 0;
	}

	//! FIXME: write doc.

	public static void
	set2(SoState state,
	                                      SoNode node,
	                                      int unit,
	                                      int numCoords,
	                                      SbVec2fArray coords)
	{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setTexCoordVBO(state, unit, null);
	  }
	  SoMultiTextureCoordinateElement element = (SoMultiTextureCoordinateElement)
	    (
	     SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class))
	     );

	  element.pimpl.ensureCapacity(unit);
	  UnitData ud = element.pimpl.unitdata.operator_square_bracket(unit);

	  ud.nodeid = node.getNodeId();
	  ud.coordsDimension = 2;
	  ud.numCoords = numCoords;
	  ud.coords2 = coords;
	  ud.coords3 = null;
	  ud.coords4 = null;
	  ud.whatKind = CoordType.EXPLICIT;
	}

	/*!
	  FIXME: write doc.
	*/
	public static void
	set3(SoState state,
	                                      SoNode node,
	                                      int unit,
	                                      int numCoords,
	                                      SbVec3fArray coords)
	{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setTexCoordVBO(state, unit, null);
	  }
	  SoMultiTextureCoordinateElement element =
	    (SoMultiTextureCoordinateElement)
	    (
	     SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class))
	     );

	  element.pimpl.ensureCapacity(unit);
	  UnitData ud = element.pimpl.unitdata.operator_square_bracket(unit);

	  ud.nodeid = node.getNodeId();
	  ud.coordsDimension = 3;
	  ud.numCoords = numCoords;
	  ud.coords2 = null;
	  ud.coords3 = coords;
	  ud.coords4 = null;
	  ud.whatKind = CoordType.EXPLICIT;
	}

	//! FIXME: write doc.

	public static void
	set4(SoState state,
	                                      SoNode node,
	                                      int unit,
	                                      int numCoords,
	                                      SbVec4fArray coords)
	{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setTexCoordVBO(state, unit, null);
	  }
	  SoMultiTextureCoordinateElement element =
	    (SoMultiTextureCoordinateElement)
	    (
	     SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class))
	     );

	  element.pimpl.ensureCapacity(unit);
	  UnitData ud = element.pimpl.unitdata.operator_square_bracket(unit);

	  ud.nodeid = node.getNodeId();
	  ud.coordsDimension = 4;
	  ud.numCoords = numCoords;
	  ud.coords2 = null;
	  ud.coords3 = null;
	  ud.coords4 = coords;
	  ud.whatKind = CoordType.EXPLICIT;
	}

	//! FIXME: write doc.

	public static SoMultiTextureCoordinateElement 
	getInstance(SoState state)
	{
	  return (SoMultiTextureCoordinateElement)
	    (getConstElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class)));
	}

	/*!
	  This method returns texture coordinate for the given point and normal.
	  The coordinate is returned as a 4D vector where the r and q coordinates
	  may be set to 0 and 1 respecively depending on what texture coordinate
	  dimension we're using.

	  This method should only be used if the CoordType is FUNCTION.
	*/

	public SbVec4f 
	get( int unit,
	                                     SbVec3f point,
	                                     SbVec3f normal) 
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);

	  assert((ud.whatKind == CoordType.FUNCTION ||
	          ud.whatKind == CoordType.NONE_TEXGEN) && ud.funcCB != null);
	  return ud.funcCB.apply(ud.funcCBData, point, normal);
	}

	//! FIXME: write doc.

	public SbVec2f 
	get2( int unit,  int index)
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);

	  assert(index >= 0 && index < ud.numCoords);
	  assert(ud.whatKind == CoordType.EXPLICIT);
	  if (ud.coordsDimension == 2) {
	    return ud.coords2.get(index);
	  }
	  else {
	    // need an instance we can write to
	    SoMultiTextureCoordinateElement elem = (SoMultiTextureCoordinateElement)(this);

	    if (ud.coordsDimension == 4) {
	      float tmp = ud.coords4.get(index).getValueRead()[3];
	      float to2D = tmp == 0.0f ? 1.0f : 1.0f / tmp;

	      elem.convert2.setValue(ud.coords4.get(index).getValueRead()[0] * to2D,
	                              ud.coords4.get(index).getValueRead()[1] * to2D);
	    }
	    else { // coordsDimension == 3
	      elem.convert2.setValue(ud.coords3.get(index).getValueRead()[0],
	                              ud.coords3.get(index).getValueRead()[1]);
	    }
	    return this.convert2;
	  }
	}

	/*!
	  FIXME: write doc.

	*/
	public SbVec3f 
	get3( int unit, int index)
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);

	  assert(index >= 0 && index < ud.numCoords);
	  assert(ud.whatKind == CoordType.EXPLICIT);
	  if (ud.coordsDimension == 3) {
	    return ud.coords3.get(index);
	  }
	  else {
	    // need an instance we can write to
	    SoMultiTextureCoordinateElement elem =
	      (SoMultiTextureCoordinateElement)(this);

	    if (ud.coordsDimension==2) {
	      elem.convert3.setValue(ud.coords2.get(index).getValueRead()[0],
	                              ud.coords2.get(index).getValueRead()[1],
	                              0.0f);
	    }
	    else { // this->coordsDimension==4
	      ud.coords4.get(index).getReal(elem.convert3);
	    }
	    return this.convert3;
	  }
	}

	//!  FIXME: write doc.

	public SbVec4f 
	get4( int unit,  int index)
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);

	  assert(index >= 0 && index < ud.numCoords);
	  assert(ud.whatKind == CoordType.EXPLICIT);
	  if (ud.coordsDimension==4) {
	    return ud.coords4.get(index);
	  }
	  else {
	    // need an instance we can write to
	    SoMultiTextureCoordinateElement elem =
	      (SoMultiTextureCoordinateElement)(this);
	    if (ud.coordsDimension == 2) {
	      elem.convert4.setValue(ud.coords2.get(index).getValueRead()[0],
	                              ud.coords2.get(index).getValueRead()[1],
	                              0.0f,
	                              1.0f);
	    }
	    else { // this->coordsDimension==3
	      elem.convert4.setValue(ud.coords3.get(index).getValueRead()[0],
	                              ud.coords3.get(index).getValueRead()[1],
	                              ud.coords3.get(index).getValueRead()[2],
	                              1.0f);
	    }
	    return this.convert4;
	  }
	}

	/*!
	  This method is used by shapes.  Three return values are possible.

	  DEFAULT means that the shapes should generate their own texture coordinates.

	  EXPLICIT means that discrete texture coordinates are stored, and should be
	  fetched with get2(), get3() or get4().

	  FUNCTION means that get(point, normal) must be used to generate texture
	  coordinates.
	*/

	public static SoMultiTextureCoordinateElement.CoordType
	getType(SoState state, int unit)
	{
	  SoMultiTextureCoordinateElement element =
	    (SoMultiTextureCoordinateElement)
	    (getConstElement(state, classStackIndexMap.get(SoMultiTextureCoordinateElement.class)));
	  return element.getType(unit);
	}

	//! FIXME: write doc.

	// side effect, will increase array size
	public SoMultiTextureCoordinateElement.CoordType
	getType( int unit) 
	{
	  this.pimpl.ensureCapacity(unit);
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.whatKind;
	}

	//! FIXME: write doc.

	public void
	init(SoState state)
	{
	  super.init(state);
	  this.pimpl.unitdata.truncate(0);
	}

	//! FIXME: write doc.

	//$ EXPORT INLINE
	public int
	getNum( int unit)
	{
	  this.pimpl.ensureCapacity(unit);
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.numCoords;
	}

	//! FIXME: write doc. (for backwards compability. Use getDimension() instead).

	//$ EXPORT INLINE
	public boolean
	is2D( int unit) 
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return (ud.coordsDimension==2);
	}

	/*!
	  FIXME: write doc.
	*/
	public int
	getDimension(int unit) 
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.coordsDimension;
	}

	/*!
	  Returns a pointer to the 2D texture coordinate array. This method is not
	  part of the OIV API.
	*/
	public SbVec2fArray
	getArrayPtr2( int unit) 
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.coords2;
	}

	/*!
	  Returns a pointer to the 3D texture coordinate array.

	*/
	public SbVec3fArray
	getArrayPtr3( int unit) 
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.coords3;
	}

	/*!
	  Returns a pointer to the 4D texture coordinate array. This method is not
	  part of the OIV API.
	*/
	public SbVec4fArray
	getArrayPtr4(int unit)
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  UnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
	  return ud.coords4;
	}

	public void
	push(SoState state)
	{
	  SoMultiTextureCoordinateElement prev =
	    (SoMultiTextureCoordinateElement)
	    (this.getNextInStack());
	  
	  this.pimpl.unitdata.copyFrom( prev.pimpl.unitdata);
	}

	public boolean
	matches( SoElement elem)
	{
	  SoMultiTextureCoordinateElement e =
	    (SoMultiTextureCoordinateElement)(elem);
	  if (e.pimpl.unitdata.getLength() != this.pimpl.unitdata.getLength()) return false;
	  
	  for (int i = 0; i < this.pimpl.unitdata.getLength(); i++) {
	    if (e.pimpl.unitdata.operator_square_bracket(i).nodeid != this.pimpl.unitdata.operator_square_bracket(i).nodeid) {
	      return false;
	    }
	  }
	  return true;
	}

	public SoElement 
	copyMatchInfo() 
	{
	  SoMultiTextureCoordinateElement elem =
	    (SoMultiTextureCoordinateElement)(getTypeId().createInstance());
	  elem.pimpl.unitdata.copyFrom( this.pimpl.unitdata);
	  return elem;
	}

	/*!
	  Returns the per-unit data for this element.
	*/
	public SoMultiTextureCoordinateElement.UnitData
	getUnitData( int unit)
	{
	  assert(unit < this.pimpl.unitdata.getLength());
	  return this.pimpl.unitdata.operator_square_bracket(unit);
	}

	public int 
	getMaxUnits()
	{
	  return this.pimpl.unitdata.getLength();
	}

	//#undef PRIVATE
	
}
