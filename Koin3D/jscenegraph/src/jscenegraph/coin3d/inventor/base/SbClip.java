/**
 * 
 */
package jscenegraph.coin3d.inventor.base;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class SbClip {
	
	private static class SbClipData {
		public final SbVec3f vertex = new SbVec3f();
		public Object data;
		
		public SbClipData(final SbVec3f v, Object data) {
			vertex.copyFrom(v);
			this.data = data;
		}

		public SbClipData(SbClipData other) {
			vertex.copyFrom(other.vertex);
			this.data = other.data;
		}

		public void get(SbVec3f v, Object[] data) {
			v.copyFrom(vertex);
			data[0] = this.data;
		}
	}
	
	public final SbList<?>[] array = new SbList<?>[2];
	public int curr;
	
	public SbClip() {
		array[0] = new SbList<SbClipData>();
		array[1] = new SbList<SbClipData>();
	}
	
	public SbList<SbClipData> array(int i) { //java port
		return (SbList<SbClipData>)array[i];
	}

	public void addVertex(SbVec3f v) {
		  ((SbList<SbClipData>)this.array[this.curr]).append(new SbClipData(v, /*vdata*/null));
		
	}

	/*!
	  Clip polygon against \a plane. This might change the number of
	  vertices in the polygon. For each time a new vertex is created, the
	  callback supplied in the constructor (if != NULL) is called with the
	  line being clipped and the new vertex calculated. The callback
	  should return a new void pointer to be stored by the clipper.
	*/
	public void
	clip(final SbPlane plane)
	{
	  int n = this.array[this.curr].getLength();

	  if (n == 0) return;

	  // create loop
	  SbClipData dummy = new SbClipData(this.array(this.curr).operator_square_bracket(0));
	  this.array(this.curr).append(dummy);

	  final SbVec3f planeN = plane.getNormal(); // ref;

	  for (int i = 0; i < n; i++) {
	    final SbVec3f v0 = new SbVec3f(), v1 = new SbVec3f();
	    final Object[] data0 = new Object[1], data1 = new Object[1];
	    this.array(this.curr).operator_square_bracket(i).get(v0, data0);
	    this.array(this.curr).operator_square_bracket(i+1).get(v1, data1);

	    float d0 = plane.getDistance(v0);
	    float d1 = plane.getDistance(v1);

	    if (d0 >= 0.0f && d1 < 0.0f) { // exit plane
	      final SbVec3f dir = v1.operator_minus(v0);
	      // we know that v0 != v1 since we got here
	      dir.normalize();
	      float dot = dir.dot(planeN);
	      final SbVec3f newvertex = v0.operator_minus(dir.operator_mul(d0/dot));
	      Object newdata = null;
//	      if (this.callback) {
//	        newdata = this.callback(v0, data0, v1, data1, newvertex, this.cbdata);
//	      }
	      outputVertex(newvertex, newdata);
	    }
	    else if (d0 < 0.0f && d1 >= 0.0f) { // enter plane
	      final SbVec3f dir = v1.operator_minus(v0);
	      // we know that v0 != v1 since we got here
	      dir.normalize();
	      float dot = dir.dot(planeN);
	      final SbVec3f newvertex = v0.operator_minus(dir.operator_mul(d0/dot));
	      Object newdata = null;
//	      if (this.callback) {
//	        newdata = this.callback(v0, data0, v1, data1, newvertex, this.cbdata);
//	      }
	      outputVertex(newvertex, newdata);
	      outputVertex(v1, data1);
	    }
	    else if (d0 >= 0.0f && d1 >= 0.0f) { // in plane
	      outputVertex(v1, data1);
	    }
	  }
	  this.array[this.curr].truncate(0);
	  this.curr ^= 1;
	}

	//
	// private method, inline only inside this class
	//
	public void
	outputVertex(final SbVec3f v, Object data)
	{
	  this.array(this.curr ^ 1).append(new SbClipData(v, data));
	}

	/*!
	  Returns the number of vertices in the polygon.
	  \sa SbClip::getVertex()
	*/
	public int
	getNumVertices()
	{
	  return this.array(this.curr).getLength();
	}

	/*!
	  Returns the vertex at index \a idx.
	  \sa SbClip::getNumVertices()
	*/
	public void
	getVertex(int idx, SbVec3f v, final Object [] vdata)
	{
	  v.copyFrom( this.array(this.curr).operator_square_bracket(idx).vertex);
	  if (vdata != null) vdata[0] = this.array(this.curr).operator_square_bracket(idx).data;
	}

	/*!
	  Resets the clipper. This should be called before adding any vertices
	  when reusing an SbClip instance.
	*/
	public void
	reset()
	{
	  this.array[0].truncate(0);
	  this.array[1].truncate(0);
	}


}
