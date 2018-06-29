/**
 * 
 */
package jscenegraph.database.inventor;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SbBox3d  implements Mutable {

	protected final 
		  SbVec3d minpt = new SbVec3d(), maxpt = new SbVec3d();

	  public SbBox3d setBounds(double xmin, double ymin, double zmin, double xmax, double ymax, double zmax)
	    { minpt.setValue(xmin, ymin, zmin); maxpt.setValue(xmax, ymax, zmax); return this; }

	  /*!
	  Extend the boundaries of the box by the given \a box parameter. This
	  is equal to calling extendBy() twice with the corner points.
	 */
	public void
	extendBy(final SbBox3d box)
	{
	  if (box.isEmpty()) { return; }

	  if (isEmpty()) {
	    copyFrom(box);
	  }
	  else {
	    extendBy(box.minpt);
	    extendBy(box.maxpt);
	  }
	}

	@Override
	public void copyFrom(Object other) {
		SbBox3d otherBox = (SbBox3d)other;
		minpt.copyFrom(otherBox.minpt);
		maxpt.copyFrom(otherBox.maxpt);
	}

	  public boolean isEmpty() { return (maxpt.getValue()[0] < minpt.getValue()[0]); }

	  /*!
	  Extend the boundaries of the box by the given point, i.e. make the
	  point fit inside the box if it isn't already so.
	*/
	public void
	extendBy(final SbVec3d point)
	{
	  if (isEmpty()) {
	    setBounds(point, point);
	  }
	  else {
	    // The explicit casts are done to humour the HPUX aCC compiler,
	    // which will otherwise say ``Template deduction failed to find a
	    // match for the call to 'SbMin'''. mortene.
	  minpt.setValue(Math.min((point.getValue()[0]), (minpt.getValue()[0])),
			  Math.min((point.getValue()[1]), (minpt.getValue()[1])),
			  Math.min((point.getValue()[2]), (minpt.getValue()[2])));
	  maxpt.setValue(Math.max((point.getValue()[0]), (maxpt.getValue()[0])),
			  Math.max((point.getValue()[1]), (maxpt.getValue()[1])),
			  Math.max((point.getValue()[2]), (maxpt.getValue()[2])));
	  }
	}

	  public SbBox3d setBounds( SbVec3d minpoint, SbVec3d maxpoint)
	    { minpt.copyFrom(minpoint); maxpt.copyFrom(maxpoint); return this; }

	  /**
	   * java port
	   * @return
	   */
	public double[] getBounds() {
		double[] bounds = new double[6];
		bounds[0] = minpt.getValue()[0];
		bounds[1] = minpt.getValue()[1];
		bounds[2] = minpt.getValue()[2];
		bounds[3] = maxpt.getValue()[0];
		bounds[4] = maxpt.getValue()[1];
		bounds[5] = maxpt.getValue()[2];
		return bounds;
	}
}
