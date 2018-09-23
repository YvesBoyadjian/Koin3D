/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.elements.SoAccumulatedElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMultiTextureMatrixElement extends SoAccumulatedElement {

	  static class UnitData implements Mutable {
		  public
		    UnitData() { textureMatrix.copyFrom(SbMatrix.identity());
		    }
		    public UnitData( UnitData org) {
		    	textureMatrix.copyFrom(org.textureMatrix);		    	
		    }
		    public final SbMatrix textureMatrix = new SbMatrix();
			@Override
			public void copyFrom(Object other) {
				textureMatrix.copyFrom(((SoMultiTextureMatrixElement.UnitData)other).textureMatrix);
			}
		  };
		  
		  static class SoMultiTextureMatrixElementP implements Destroyable{
			  public
			    void ensureCapacity(int unit) {
			      while (unit >= this.unitdata.getLength()) {
			        this.unitdata.append(new SoMultiTextureMatrixElement.UnitData());
			      }
			    }
			    public final SbListOfMutableRefs<SoMultiTextureMatrixElement.UnitData> unitdata = new SbListOfMutableRefs<>(()->new SoMultiTextureMatrixElement.UnitData());
				@Override
				public void destructor() {
					Destroyable.delete(unitdata);
				}
			  };

			  SoMultiTextureMatrixElementP pimpl;

		  public // Coin-3 support
			  static void makeIdentity(SoState state, SoNode node, int unit /*= 0*/) {
			    set(state, node, unit, SbMatrix.identity());
			  }
			  public static void set(SoState state, SoNode node,
			                  SbMatrix matrix) {
			    set(state, node, 0, matrix);
			  }
			  public static void mult(SoState state, SoNode node,
			                   SbMatrix matrix) {
			    mult(state, node, 0, matrix);
			  }
			  public static void translateBy(SoState state, SoNode node,
			                          SbVec3f translation) {
			    final SbMatrix m = new SbMatrix();
			    m.setTranslate(translation);
			    mult(state, node, 0, m);
			  }
			  public static void rotateBy(SoState state, SoNode node,
			                       SbRotation rotation) {
			    final SbMatrix m = new SbMatrix();
			    m.setRotate(rotation);
			    mult(state, node, 0, m);
			  }
			  public static void scaleBy(SoState state, SoNode node,
			                      final SbVec3f scaleFactor) {
			    final SbMatrix m = new SbMatrix();
			    m.setScale(scaleFactor);
			    mult(state, node, 0, m);
			  }
		  
			  /*!
			  The constructor.
			 */
			public SoMultiTextureMatrixElement()
			{
			  this.pimpl = new SoMultiTextureMatrixElementP();

//			  this.setTypeId(SoMultiTextureMatrixElement::classTypeId);
//			  this.setStackIndex(SoMultiTextureMatrixElement::classStackIndex);
			}

			/*!
			  The destructor.
			*/
			public void destructor()
			{
			  Destroyable.delete(this.pimpl);
			  super.destructor();
			}


			public static void
			set(SoState state, SoNode node, int unit, SbMatrix matrix)
			{
			  SoMultiTextureMatrixElement elem = (SoMultiTextureMatrixElement)
			    (SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureMatrixElement.class)));
			  elem.setElt(unit, matrix);
			  if (node != null) elem.addNodeId(node);
			}


			/*!
			  Multiplies \a matrix into the current texture matrix.
			*/
			public static void
			mult(SoState state,
			                                  SoNode node,
			                                  int unit,
			                                  SbMatrix matrix)
			{
			  SoMultiTextureMatrixElement elem = (SoMultiTextureMatrixElement)
			    (SoElement.getElement(state, classStackIndexMap.get(SoMultiTextureMatrixElement.class)));
			  elem.multElt(unit, matrix);
			  if (node != null) elem.addNodeId(node);
			}

			/*!
			  Returns current texture matrix.
			*/
			public static SbMatrix 
			get(SoState state, int unit)
			{
			  SoMultiTextureMatrixElement elem =
			    (SoMultiTextureMatrixElement)
			    (SoElement.getConstElement(state, classStackIndexMap.get(SoMultiTextureMatrixElement.class)));
			  return elem.getElt(unit);
			}

			public SoMultiTextureMatrixElement.UnitData 
			getUnitData( int unit)
			{
			  this.pimpl.ensureCapacity(unit);
			  return this.pimpl.unitdata.operator_square_bracket(unit);
			}

			public int 
			getNumUnits()
			{
			  return this.pimpl.unitdata.getLength();
			}


			/*!
			  virtual method which is called from mult(). Multiplies \a matrix
			  into element matrix.
			*/
			public void
			multElt( int unit, SbMatrix matrix)
			{
			  this.pimpl.ensureCapacity(unit);
			  this.pimpl.unitdata.operator_square_bracket(unit).textureMatrix.multLeft(matrix);
			}

			/*!
			  virtual method which is called from set(). Sets \a matrix
			  intp element matrix.
			*/
			public void
			setElt(int unit, SbMatrix matrix)
			{
			  this.pimpl.ensureCapacity(unit);
			  this.pimpl.unitdata.operator_square_bracket(unit).textureMatrix.copyFrom(matrix);
			}

			/*!
			  Returns element matrix. Called from get().
			*/
			public SbMatrix 
			getElt( int unit)
			{
			  this.pimpl.ensureCapacity(unit);
			  return this.pimpl.unitdata.operator_square_bracket(unit).textureMatrix;
			}

			// doc from parent
			public void
			init(SoState state)
			{
			  super.init(state);
			  this.clearNodeIds();
			}

			// Documented in superclass. Overridden to copy current matrix and
			// update accumulated node ids.
			public void
			push(SoState state)
			{
			  super.push(state);

			  SoMultiTextureMatrixElement prev =
			    (SoMultiTextureMatrixElement)
			    (this.getNextInStack());
			  
			  this.pimpl.unitdata.copyFrom(prev.pimpl.unitdata);
			  // make sure node ids are accumulated properly
			  this.copyNodeIds(prev);
			}
			  
}
