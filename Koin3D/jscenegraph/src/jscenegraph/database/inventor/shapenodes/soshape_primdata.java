/**
 * 
 */
package jscenegraph.database.inventor.shapenodes;

import jscenegraph.coin3d.inventor.SbTesselator;
import jscenegraph.coin3d.inventor.base.SbGLUTessellator;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoLineDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jscenegraph.port.Util;
import sun.security.krb5.internal.crypto.Des;

/**
 * @author Yves Boyadjian
 *
 */
public class soshape_primdata {
	
	  SoShape.TriangleShape shapetype;
	  SoAction action; //ptr
	  SoShape shape; //ptr
	  Array<SoPrimitiveVertex> vertsArray; //ptr
	  Array<SoPointDetail> pointDetails; //ptr
	  SoFaceDetail faceDetail; //ptr
	  SoLineDetail lineDetail; //ptr
	  int arraySize;
	  int counter;
	  SbTesselator tess; //ptr
	  SbGLUTessellator glutess; //ptr
	  int faceCounter;

	  boolean matPerFace;
	  boolean normPerFace;


	public soshape_primdata()
	{
		this.counter = 0;
		this.action = null;
		this.shape = null;
		this.faceCounter = 0;
		this.arraySize = 4;
		this.vertsArray = new Array<>(SoPrimitiveVertex.class,new SoPrimitiveVertex[this.arraySize]);// for(int i=0;i<this.arraySize;i++) this.vertsArray[i] = new SoPrimitiveVertex();
		this.pointDetails = new Array<>(SoPointDetail.class, new SoPointDetail[this.arraySize]);// for(int i=0;i<this.arraySize;i++) this.pointDetails[i] = new SoPointDetail();
		this.faceDetail = null;
		this.lineDetail = null;
		this.matPerFace = false;
		this.normPerFace = false;

		this.tess = null;
		this.glutess = null;

		if (SbGLUTessellator.preferred()) {
		this.glutess = new SbGLUTessellator(soshape_primdata::tess_callback, this);
	}
  else {
		this.tess = new SbTesselator(soshape_primdata::tess_callback, this);
	}
	}


public void
beginShape(SoShape shapeptr, SoAction actionptr,
                               SoShape.TriangleShape shapetypearg,
                               SoDetail detail)
{
  this.shape = shapeptr;
  this.action = actionptr;
  this.shapetype = shapetypearg;
  // this is a hack. Only one of these will be used, and the
  // other one is an illegal cast.
  if(detail instanceof SoFaceDetail)
	  this.faceDetail = (SoFaceDetail )detail;
  if(detail instanceof SoLineDetail)
	  this.lineDetail = (SoLineDetail )detail;
  this.counter = 0;

  SoState state = action.getState();

  SoMaterialBindingElement.Binding mbind = SoMaterialBindingElement.get(state);
  SoNormalBindingElement.Binding nbind = SoNormalBindingElement.get(state);

  // need to test for PER_FACE bindings since special rules need to followed 
  // to get the correct per vertex material and normal indices in these cases
  // (basically the same rules as when sending geometry to OpenGL)
  this.matPerFace = 
    (mbind == SoMaterialBindingElement.Binding.PER_FACE) ||
    (mbind == SoMaterialBindingElement.Binding.PER_FACE_INDEXED);
  this.normPerFace = 
    (nbind == SoNormalBindingElement.Binding.PER_FACE) ||
    (nbind == SoNormalBindingElement.Binding.PER_FACE_INDEXED);
    
}

	public void
	endShape()
	{
		if (this.shapetype == SoShape.TriangleShape.POLYGON) {
		this.handleFaceDetail(this.counter);

		if (SoShapeHintsElement.getFaceType(action.getState()) ==
				SoShapeHintsElement.FaceType.CONVEX) {
			for (int i = 1; i < this.counter-1; i++) {
				this.shape.invokeTriangleCallbacks(this.action,
                                             vertsArray.get(0),
                                             vertsArray.get(i),
                                             vertsArray.get(i+1));
			}
		}
    else {
			if (SbGLUTessellator.preferred()) {
				this.glutess.beginPolygon();
				for (int i = 0; i < counter; i++) {
					this.glutess.addVertex(vertsArray.get(i).getPoint(), vertsArray.get(i));
				}
				this.glutess.endPolygon();
			}
      else {
				// FIXME: the keepVertices==TRUE setting may not be necessary,
				// according to pederb. (The flag causes us to get callbacks
				// even on empty polygons -- probably not useful in Coin, as
				// it was for Rational Reducer.)  20060216 mortene.
				this.tess.beginPolygon(true);
				for (int i = 0; i < counter; i++) {
					this.tess.addVertex(vertsArray.get(i).getPoint(), vertsArray.get(i));
				}
				this.tess.endPolygon();
			}
		}
	}
	}


	public void
	shapeVertex(SoPrimitiveVertex v)
	{
		switch (shapetype) {
			case TRIANGLE_STRIP:
				if (this.counter >= 3) {
				if ((this.counter & 1)!=0) this.copyVertex(2, 0);
      else this.copyVertex(2, 1);
			}
			this.setVertex(Math.min(this.counter, 2), v);
			this.counter++;
			if (this.counter >= 3) {
				this.handleFaceDetail(3);
				this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(1),
                                           vertsArray.get(2));
			}
			break;
			case TRIANGLE_FAN:
				if (this.counter == 3) {
				this.copyVertex(2, 1);
				this.setVertex(2, v);
			}
    else {
				this.setVertex(this.counter++, v);
			}
			if (this.counter == 3) {
				this.handleFaceDetail(3);
				this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(1),
                                           vertsArray.get(2));
			}
			break;
			case TRIANGLES:
				this.setVertex(counter++, v);
				if (this.counter == 3) {
				this.handleFaceDetail(3);
				this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(1),
                                           vertsArray.get(2));
				this.counter = 0;
			}
			break;
			case POLYGON:
				if (this.counter >= this.arraySize) {
				this.arraySize <<= 1;
				Array<SoPrimitiveVertex> newArray = new Array<>(SoPrimitiveVertex.class,new SoPrimitiveVertex[this.arraySize]);
				Util.memcpy(newArray, this.vertsArray,
						SoPrimitiveVertex.sizeof() * this.counter);
				//Destroyable.delete(this.vertsArray); TODO
				this.vertsArray = newArray;

				Array<SoPointDetail> newparray = new Array<>(SoPointDetail.class, new SoPointDetail[this.arraySize]);
				Util.memcpy(newparray, this.pointDetails,
						SoPointDetail.sizeof()* this.counter);
				//Destroyable.delete( this.pointDetails); TODO
				this.pointDetails = newparray;

				if (this.faceDetail != null) {
					for (int i = 0; i < this.counter; i++) {
						this.vertsArray.get(i).setDetail(this.pointDetails.get(i));
					}
				}
			}
			this.setVertex(this.counter++, v);
			break;
			case QUADS:
				this.setVertex(this.counter++, v);
				if (this.counter == 4) {
				this.handleFaceDetail(4);
				this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(1),
                                           vertsArray.get(2));
				this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(2),
                                           vertsArray.get(3));
				this.counter = 0;
			}
			break;
			case QUAD_STRIP:
				this.setVertex(this.counter++, v);
				if (counter == 4) {
					if (this.matPerFace) this.copyMaterialIndex(3);
					if (this.normPerFace) this.copyNormalIndex(3);
					// can't use handleFaceDetail(), because of the vertex
					// order.
					if (this.faceDetail != null) {
						this.faceDetail.setNumPoints(4);
						this.faceDetail.setPoint(0, this.pointDetails.get(0));
						this.vertsArray.get(0).setDetail(this.faceDetail);
						this.faceDetail.setPoint(1, this.pointDetails.get(1));
						this.vertsArray.get(1).setDetail(this.faceDetail);
						this.faceDetail.setPoint(2, this.pointDetails.get(3));
						this.vertsArray.get(2).setDetail(this.faceDetail);
						this.faceDetail.setPoint(3, this.pointDetails.get(2));
						this.vertsArray.get(3).setDetail(this.faceDetail);
					}
					this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(1),
                                           vertsArray.get(3));
					this.shape.invokeTriangleCallbacks(this.action,
                                           vertsArray.get(0),
                                           vertsArray.get(3),
                                           vertsArray.get(2));
					this.copyVertex(2, 0);
					this.copyVertex(3, 1);
					this.counter = 2;
				}
				break;
			case POINTS:
				this.shape.invokePointCallbacks(this.action, v);
				break;
			case LINES:
				this.setVertex(this.counter++, v);
				if (this.counter == 2) {
				this.handleLineDetail();
				this.shape.invokeLineSegmentCallbacks(this.action,
                                              vertsArray.get(0),
                                              vertsArray.get(1));
				this.counter = 0;
			}
			break;
			case LINE_STRIP:
				this.setVertex(this.counter++, v);
				if (this.counter == 2) {
				this.handleLineDetail();
				this.shape.invokeLineSegmentCallbacks(this.action,
                                              vertsArray.get(0),
                                              vertsArray.get(1));
				this.copyVertex(1, 0);
				this.counter = 1;
			}
			break;
			default:
				assert(false);// && "Unknown shape type");
		}
	}


	public void
	copyVertex(int src, int dest)
	{
		this.vertsArray.get(dest).copyFrom(this.vertsArray.get(src));
		if (this.faceDetail != null) {
		this.pointDetails.get(dest).copyFrom(this.pointDetails.get(src));
		this.vertsArray.get(dest).setDetail(this.pointDetails.get(dest));
	}
	}

	public void
	setVertex(int idx, SoPrimitiveVertex v)
	{
		this.vertsArray.get(idx).copyFrom(v);
		if (this.faceDetail != null || this.lineDetail != null) {
		SoPointDetail pd = (SoPointDetail )v.getDetail();
		assert(pd != null);
		this.pointDetails.get(idx).copyFrom(pd);
		this.vertsArray.get(idx).setDetail(this.pointDetails.get(idx));
	}
	}


	public void
	handleFaceDetail( int numv)
	{
		// if PER_FACE binding, copy indices from the last vertex we
		// received to the other vertices
		if (this.matPerFace) this.copyMaterialIndex(numv-1);
		if (this.normPerFace) this.copyNormalIndex(numv-1);

		if (this.faceDetail != null) {
		this.faceDetail.setNumPoints(numv);
		for (int i = 0; i < numv; i++) {
			this.faceDetail.setPoint(i, this.pointDetails.get(i));
			this.vertsArray.get(i).setDetail(this.faceDetail);
		}
	}
	}

	public void
	handleLineDetail()
	{
		if (this.lineDetail != null) {
		this.lineDetail.setPoint0(this.pointDetails.get(0));
		this.lineDetail.setPoint1(this.pointDetails.get(1));
		this.vertsArray.get(0).setDetail(this.lineDetail);
		this.vertsArray.get(1).setDetail(this.lineDetail);
	}
	}

	public static void tess_callback(Object v0, Object v1, Object v2, Object data)
	{
		soshape_primdata  thisp = (soshape_primdata ) data;
		thisp.shape.invokeTriangleCallbacks(thisp.action,
				(SoPrimitiveVertex )v0,
				(SoPrimitiveVertex )v1,
				(SoPrimitiveVertex )v2);
	}

	public void
	copyMaterialIndex(int lastvertex)
	{
		int i;
		int matidx = this.vertsArray.get(lastvertex).getMaterialIndex();
		for (i = 0; i < lastvertex; i++) {
			this.vertsArray.get(i).setMaterialIndex(matidx);
			this.pointDetails.get(i).setMaterialIndex(matidx);
		}
	}

	public void
	copyNormalIndex(int lastvertex)
	{
		int i;
		int normidx = this.pointDetails.get(lastvertex).getNormalIndex();
		for (i = 0; i < lastvertex; i++) {
			this.pointDetails.get(i).setNormalIndex(normidx);
		}
	}
}
