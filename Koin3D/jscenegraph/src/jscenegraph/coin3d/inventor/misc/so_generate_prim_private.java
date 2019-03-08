/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.details.SoCubeDetail;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;

/**
 * @author Yves Boyadjian
 *
 */
public class so_generate_prim_private {


	  static void generate_cube( float width,
	                             float height,
	                             float depth,
	                            int flags,
	                            SoShape shape,
	                            SoAction action) {
	    SbVec3fArray varray = new SbVec3fArray(new float[8*3]);
	    SoGenerate.sogenerate_generate_cube_vertices(varray,
	                           width * 0.5f,
	                           height * 0.5f,
	                           depth * 0.5f);


	    final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
	    final SoCubeDetail cubeDetail = new SoCubeDetail();
	    vertex.setDetail(cubeDetail);
	    vertex.setMaterialIndex(0);

	    shape.beginShape(action, SoShape.TriangleShape.QUADS);
	    IntArrayPtr iptr = new IntArrayPtr(SoGenerate.sogenerate_cube_vindices);
	    final SbVec3fArray nptr = new SbVec3fArray(SoGenerate.sogenerate_cube_normals);
	    final SbVec2fArray tptr = new SbVec2fArray(SoGenerate.sogenerate_cube_texcoords);

	    for (int i = 0; i < 6; i++) { // 6 quads
	      vertex.setNormal(nptr.get(i));
	      if ((flags & SoGenerate.SOGEN_MATERIAL_PER_PART)!=0) vertex.setMaterialIndex(i);
	      for (int j = 0; j < 4; j++) {
	        vertex.setTextureCoords(tptr.get(j));
	        vertex.setPoint(varray.get(iptr.get())); iptr.plusPlus();
	        shape.shapeVertex(vertex);
	      }
	    }
	    shape.endShape();
	    
	    cubeDetail.destructor();
	    vertex.destructor();
	  }

}
