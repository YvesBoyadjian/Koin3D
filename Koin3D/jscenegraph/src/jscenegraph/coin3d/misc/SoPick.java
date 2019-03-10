/**
 * 
 */
package jscenegraph.coin3d.misc;

import jscenegraph.database.inventor.SbCylinder;
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.details.SoCubeDetail;
import jscenegraph.database.inventor.details.SoCylinderDetail;
import jscenegraph.database.inventor.nodes.SoCylinder;
import jscenegraph.database.inventor.nodes.SoShape;

/**
 * @author Yves Boyadjian
 *
 */
public class SoPick {

	// flags for cone, cylinder and cube

	public static final int SOPICK_SIDES      =0x01;
	public static final int SOPICK_TOP        =0x02;
	public static final int SOPICK_BOTTOM     =0x04;
	public static final int SOPICK_MATERIAL_PER_PART   =0x08;

	  static int translation[/*6*/] = {2, 3, 5, 4, 1, 0}; // translate into detail part-num
	  static int textranslation[/*3*/][/*2*/] = {{2,1},{0,2},{0,1}}; // to get correct texcoords


//
// internal method used to set picked point attributes
// when picking on the side of the cylinder
//
public static void
set_side_pp_data(SoPickedPoint pp, final SbVec3f isect,
                 float halfh)
{
  // the normal vector for a cylinder side is the intersection point,
  // without the y-component, of course.
  final SbVec3f normal = new SbVec3f(isect.getValueRead()[0], 0.0f, isect.getValueRead()[2]);
  normal.normalize();
  pp.setObjectNormal(normal);

  // just reverse the way texture coordinates are generated to find
  // the picked point texture coordinate
  final SbVec4f texcoord = new SbVec4f();
  texcoord.setValue((float) Math.atan2(isect.getValueRead()[0], isect.getValueRead()[2]) *
                    (1.0f / (2.0f * (float) Math.PI)) + 0.5f,
                    (isect.getValueRead()[1] + halfh) / (2.0f * halfh),
                    0.0f, 1.0f);
  pp.setObjectTextureCoords(texcoord);
}


public static void
sopick_pick_cylinder( float r,
                      float height,
                      int flags,
                     SoShape shape,
                     SoRayPickAction action)
{
  action.setObjectSpace();
  final SbLine line = action.getLine();
  float halfh = height * 0.5f;

  // FIXME: should be possible to simplify cylinder test, since this
  // cylinder is aligned with the y-axis. 19991110 pederb.

  int numPicked = 0; // will never be > 2
  final SbVec3f enter = new SbVec3f(), exit = new SbVec3f();

  if ((flags & SOPICK_SIDES)!=0) {
//#if 0
//    // The following line of code doesn't compile with GCC 2.95, as
//    // reported by Petter Reinholdtsen (pere@hungry.com) on
//    // coin-discuss.
//    //
//    // Update: it doesn't work with GCC 2.95.2 either, which is now
//    // the current official release of GCC. And I can't find any
//    // mention of a bug like this being fixed from the CVS ChangeLog,
//    // neither in the gcc/egcs head branch nor the release-2.95
//    // branch.  20000103 mortene.
//    //
//    // FIXME: should a) make sure this is known to the GCC
//    // maintainers, b) have an autoconf check to test for this exact
//    // bug. 19991230 mortene.
//    SbCylinder cyl(SbLine(SbVec3f(0.0f, 0.0f, 0.0f), SbVec3f(0.0f, 1.0f, 0.0f)), r);
//#else // GCC 2.95 work-around.
    final SbVec3f v0 = new SbVec3f(0.0f, 0.0f, 0.0f);
    final SbVec3f v1 = new SbVec3f(0.0f, 1.0f, 0.0f);
    final SbLine l = new SbLine(v0, v1);
    final SbCylinder cyl = new SbCylinder(l, r);
//#endif // GCC 2.95 work-around.

    if (cyl.intersect(line, enter, exit)) {
      if ((Math.abs(enter.getValueRead()[1]) <= halfh) && action.isBetweenPlanes(enter)) {
        SoPickedPoint pp = action.addIntersection(enter);
        if (pp != null) {
          set_side_pp_data(pp, enter, halfh);
          SoCylinderDetail detail = new SoCylinderDetail();
          detail.setPart((int)SoCylinder.Part.SIDES.getValue());
          pp.setDetail(detail, shape);
          numPicked++;
        }
      }
      if ((Math.abs(exit.getValueRead()[1]) <= halfh) && (enter != exit) && action.isBetweenPlanes(exit)) {
        SoPickedPoint pp = action.addIntersection(exit);
        if (pp != null) {
          set_side_pp_data(pp, exit, halfh);
          SoCylinderDetail detail = new SoCylinderDetail();
          detail.setPart((int)SoCylinder.Part.SIDES.getValue());
          pp.setDetail(detail, shape);
          numPicked++;
        }
      }
    }
  }

  float r2 = r * r;

  boolean matperpart = (flags & SOPICK_MATERIAL_PER_PART)!=0;

  if ((numPicked < 2) && (flags & SOPICK_TOP)!=0) {
    final SbPlane top = new SbPlane(new SbVec3f(0.0f, 1.0f, 0.0f), halfh);
    if (top.intersect(line, enter)) {
      if (((enter.getValueRead()[0] * enter.getValueRead()[0] + enter.getValueRead()[2] * enter.getValueRead()[2]) <= r2) &&
          (action.isBetweenPlanes(enter))) {
        SoPickedPoint pp = action.addIntersection(enter);
        if (pp != null) {
          if (matperpart) pp.setMaterialIndex(1);
          pp.setObjectNormal(new SbVec3f(0.0f, 1.0f, 0.0f));
          pp.setObjectTextureCoords(new SbVec4f(0.5f + enter.getValueRead()[0] / (2.0f * r),
                                             0.5f - enter.getValueRead()[2] / (2.0f * r),
                                             0.0f, 1.0f));
          SoCylinderDetail detail = new SoCylinderDetail();
          detail.setPart((int)SoCylinder.Part.TOP.getValue());
          pp.setDetail(detail, shape);
          numPicked++;
        }
      }
    }
  }

  if ((numPicked < 2) && (flags & SOPICK_BOTTOM)!=0) {
    final SbPlane bottom = new SbPlane(new SbVec3f(0, 1, 0), -halfh);
    if (bottom.intersect(line, enter)) {
      if (((enter.getValueRead()[0] * enter.getValueRead()[0] + enter.getValueRead()[2] * enter.getValueRead()[2]) <= r2) &&
          (action.isBetweenPlanes(enter))) {
        SoPickedPoint pp = action.addIntersection(enter);
        if (pp != null) {
          if (matperpart) pp.setMaterialIndex(2);
          pp.setObjectNormal(new SbVec3f(0.0f, -1.0f, 0.0f));
          pp.setObjectTextureCoords(new SbVec4f(0.5f + enter.getValueRead()[0] / (2.0f * r),
                                             0.5f + enter.getValueRead()[2] / (2.0f * r),
                                             0.0f, 1.0f));
          SoCylinderDetail detail = new SoCylinderDetail();
          detail.setPart((int)SoCylinder.Part.BOTTOM.getValue());
          pp.setDetail(detail, shape);
        }
      }
    }
  }
}

	  
	  
public static void
sopick_pick_cube(float width,
                 float height,
                 float depth,
                 int flags,
                 SoShape shape,
                 SoRayPickAction action)
{
  action.setObjectSpace();
  final SbLine line = action.getLine();
  float[] size = new float[3];
  size[0] = width * 0.5f;
  size[1] = height * 0.5f;
  size[2] = depth * 0.5f;

  int cnt = 0;
  // test intersection with all six planes
  for (int i = 0; i < 3; i++) {
    for (float j = -1.0f; j <= 1.0f; j += 2.0f) {
      final SbVec3f norm = new SbVec3f(0, 0, 0);
      norm.operator_square_bracket(i, j);
      final SbVec3f isect = new SbVec3f();

      final SbPlane plane = new SbPlane(norm, size[i]);
      if (plane.intersect(line, isect)) {
        int i1 = (i+1) % 3;
        int i2 = (i+2) % 3;

        if (isect.getValueRead()[i1] >= -size[i1] && isect.getValueRead()[i1] <= size[i1] &&
            isect.getValueRead()[i2] >= -size[i2] && isect.getValueRead()[i2] <= size[i2] &&
            action.isBetweenPlanes(isect)) {
          SoPickedPoint pp = action.addIntersection(isect);
          if (pp != null) {
            SoCubeDetail detail = new SoCubeDetail();
            detail.setPart(translation[cnt]);
            pp.setDetail(detail, shape);
            if ((flags & SOPICK_MATERIAL_PER_PART)!=0)
              pp.setMaterialIndex(translation[cnt]);
            pp.setObjectNormal(norm);
            i1 = textranslation[i][0];
            i2 = textranslation[i][1];
            float s = isect.getValueRead()[i1] + size[i1];
            float t = isect.getValueRead()[i2] + size[i2];
            if (size[i1] != 0) s /= (size[i1]*2.0f);
            if (size[i2] != 0) t /= (size[i2]*2.0f);
            switch (i) {
            default: // just to avoid warnings
            case 0:
              if (j > 0.0f) s = 1.0f - s;
              break;
            case 1:
              if (j > 0.0f) t = 1.0f - t;
              break;
            case 2:
              if (j < 0.0f) s = 1.0f - s;
              break;
            }
            pp.setObjectTextureCoords(new SbVec4f(s, t, 0.0f, 1.0f));
          }
        }
      }
      cnt++;
    }
  }
}
	
}
