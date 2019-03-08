/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.port.SbVec3fArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGenerate {

	// flags for cone, cylinder and cube

	public static final int SOGEN_GENERATE_SIDE       =0x01;
	public static final int SOGEN_GENERATE_TOP        =0x02;
	public static final int SOGEN_GENERATE_BOTTOM     =0x04;
	public static final int SOGEN_MATERIAL_PER_PART   =0x08;


public static void
sogen_generate_cube(float width,
                    float height,
                    float depth,
                    int flags,
                    SoShape shape,
                    SoAction action)
{
  so_generate_prim_private.generate_cube(width,
                                          height,
                                          depth,
                                          flags,
                                          shape,
                                          action);
}


//
// the 12 triangles in the cube
//
public static final int sogenerate_cube_vindices[] =
{
  0, 1, 3, 2,
  5, 4, 6, 7,
  1, 5, 7, 3,
  4, 0, 2, 6,
  4, 5, 1, 0,
  2, 3, 7, 6
};

public static final float sogenerate_cube_texcoords[] =
{
  1.0f, 1.0f,
  0.0f, 1.0f,
  0.0f, 0.0f,
  1.0f, 0.0f
};

//
// a cube needs 6 normals
//
public static final float sogenerate_cube_normals[] =
{
  0.0f, 0.0f, 1.0f,
  0.0f, 0.0f, -1.0f,
  -1.0f, 0.0f, 0.0f,
  1.0f, 0.0f, 0.0f,
  0.0f, 1.0f, 0.0f,
  0.0f, -1.0f, 0.0f
};


public static void
sogenerate_generate_cube_vertices(SbVec3fArray varray,
                       float w,
                       float h,
                       float d)
{
  for (int i = 0; i < 8; i++) {
    varray.get(i).setValue((i&1)!=0 ? -w : w,
                       (i&2)!=0 ? -h : h,
                       (i&4)!=0 ? -d : d);
  }
}


}
