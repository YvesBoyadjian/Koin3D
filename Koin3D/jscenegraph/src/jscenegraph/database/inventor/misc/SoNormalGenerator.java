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
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file contains the definition of the SoNormalGenerator class.
 |
 |   Author(s)          : Thad Beier, Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.port.Destroyable;


///////////////////////////////////////////////////////////////////////////////
///
////\class SoNormalGenerator
///
///  This class can be used by polyhedral shape classes to generate
///  surface normals when they do not have valid ones specified. To
///  generate normals, create an instance of this class, then specify
///  each polygon in the shape, then call generate(). After generate()
///  is called, the normals can be accessed from the instance. There
///  will be one normal generated for each vertex of each polygon, in
///  the order passed in.
///
///  For convenience, there is a method to send down a triangle of
///  vertices.
///
///  For efficiency, a constructor is provided that takes an
///  approximate number of vertices that will be specified. Use this
///  constructor if you know roughly how many vertices will be sent;
///  this will cut down on allocation overhead.
///
///  The generate() method takes a crease angle that is used to
///  determine whether to create normals that simulate a sharp crease
///  at an edge between two polygons or to create normals that simulate
///  smooth shading over the edge. The crease angle is the minimum
///  angle (in radians) between the face normals on either side of an
///  edge that will be used to consider the edge a crease. For example,
///  a crease angle of pi/4 radians (45 degrees) means that adjacent
///  face normals must be within 45 degrees of each other for the edge
///  to be shaded smoothly.
///
///  Note that the SoNormalGenerator destructor DOES NOT delete the
///  array of generated normals. The caller is responsible for doing
///  so. This allows the normals to be cached elsewhere without having
///  to copy them.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoNormalGenerator implements Destroyable {

  private
    //! Arrays of vertex points, face normals, and vertex normals. The
    //! face normals are stored one per vertex. The other vars are the
    //! number of items in the arrays, and the allocated sizes of the
    //! arrays. Since the points and faceNormals arrays are always the
    //! same size, no need for extra variables.
    SbVec3f[]             points, faceNormals, vertNormals;
  private  int             numPoints, numVertNormals;
  private  int             maxPoints, maxVertNormals;

    //! Flag: if TRUE, polygons are oriented counter-clockwise
  private  boolean              isCCW;

    //! Index into points array where current polygon started
  private  int             beginPolygonIndex;



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor that takes hint about number of vertices
//
// Use: public

public SoNormalGenerator(boolean _isCCW, int approxNumVertices)
//
////////////////////////////////////////////////////////////////////////
{
    // Protect against bad number of vertices
    maxPoints      = (approxNumVertices <= 0 ? 16 : approxNumVertices);
    maxVertNormals = 0;

    numPoints = numVertNormals = 0;

    points      = new SbVec3f[maxPoints]; for( int i=0;i<maxPoints;i++) points[i] = new SbVec3f();
    faceNormals = new SbVec3f[maxPoints]; for( int i=0;i<maxPoints;i++) faceNormals[i] = new SbVec3f();
    vertNormals = null;

    isCCW = _isCCW;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (points != null)
        points = null;

    if (vertNormals != faceNormals)
        faceNormals = null;

    // Do NOT delete vertNormals. The caller is responsible for this.
}
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Prepares to receive vertices of a polygon
//
// Use: public

public void
beginPolygon()
//
////////////////////////////////////////////////////////////////////////
{
    beginPolygonIndex = numPoints;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends a vertex of the current polygon
//
// Use: public

public void
polygonVertex(final SbVec3f point)
//
////////////////////////////////////////////////////////////////////////
{
    // Make sure there's enough room for a new vertex point and face normal
    if (numPoints == maxPoints) {
        SbVec3f[] newArray;

        newArray = new SbVec3f [2 * maxPoints]; for(int i=0; i<2*maxPoints;i++) {newArray[i] = new SbVec3f();}
        //memcpy(newArray, points, (int) (maxPoints * SbVec3f.sizeof()));
        for(int i = 0; i< maxPoints; i++) {
        	newArray[i].copyFrom(points[i]);
        }
        //delete [] points; java port
        points = newArray;

        newArray = new SbVec3f [maxPoints * 2]; for(int i=0; i<2*maxPoints;i++) {newArray[i] = new SbVec3f();}
        //memcpy(newArray, faceNormals, (int) (maxPoints * SbVec3f.sizeof()));
        for(int i = 0; i< maxPoints; i++) {
        	newArray[i].copyFrom(faceNormals[i]);
        }
        //delete [] faceNormals; java port
        faceNormals = newArray;

        maxPoints *= 2;
    }

    // Add the new point
    points[numPoints++] = point;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Closes the current polygon. Causes the face normal to be
//    computed and stored.
//
// Use: public

public void
endPolygon()
//
////////////////////////////////////////////////////////////////////////
{
    int     numVertices = numPoints - beginPolygonIndex;
    int     i, j;
    final SbVec3f     sum = new SbVec3f(0.0f, 0.0f, 0.0f);

    // Calculate a normal for this polygon.  Use Newell's method.
    // Newman & Sproull, pg. 499
    // We've gotta be careful of small polygons very far away from the
    // origin-- floating point errors can get really big.  So we'll
    // translate the first vertex of the polygon to the origin and
    // pull all the other vertices along with it:
    final SbVec3f firstPoint = points[beginPolygonIndex];
    for (i = 0; i < numVertices; i++) {
        j = i + 1;
        if (j == numVertices)
            j = 0;
        sum.operator_add_equal(
            (points[beginPolygonIndex + i].operator_minus(firstPoint)).cross(
             points[beginPolygonIndex + j].operator_minus(firstPoint)));
    }

    // Store the face normal for all of these points
    sum.normalize();

    // Invert if face is clockwise
    if (!isCCW) {
        sum.negate();
    }

    for (i = 0; i < numVertices; i++)
        faceNormals[beginPolygonIndex + i] = sum;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Send one triangle.  Uses polygon routines above...
//
// Use: public

public void
triangle(final SbVec3f p1,
                            final SbVec3f p2,
                            final SbVec3f p3)
//
////////////////////////////////////////////////////////////////////////
{
    beginPolygon();
    polygonVertex(p1);
    polygonVertex(p2);
    polygonVertex(p3);
    endPolygon();
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Calculates the vertex normals once all vertices have been sent.
//
// Use: public

public void
generate(float creaseAngle)
//
////////////////////////////////////////////////////////////////////////
{
    // First, check for fast case of normal-per-face:
    if (creaseAngle < 0.01) {
        vertNormals = faceNormals;
        numVertNormals = numPoints;
        return;
    }

    final SbBox3f     box = new SbBox3f();
    final SbVec3f     hashScale = new SbVec3f(), sum = new SbVec3f(), base = new SbVec3f();
    float       tolerance, cosCreaseAngle = (float)Math.cos(creaseAngle);
    int     i, j, hashValue, lowHashValue, highHashValue, hv;
    int[]    hashTable, hashNext, indirect;
    boolean      found;

    // Compute the bounding box of all vertices
    for (i = 0; i < numPoints; i++)
        box.extendBy(points[i]);

    // We will use a hash function to determine which vertices are
    // coincident within some tolerance. The tolerance is a function
    // of the size of the bounding box. The hash function is a linear
    // function of x, y, and z such that one corner of the bounding
    // box maps to the key "0" and the opposite corner maps to the key
    // "numPoints".  [As Thad says, we would have to be pretty unlucky
    // for this to be slow: all the points would have to be on the
    // exact wrong diagonal plane through the bounding box for them to
    // hash to the same value.]
    box.getSize(hashScale);
    tolerance = (hashScale.getValueRead()[0] + hashScale.getValueRead()[1] + hashScale.getValueRead()[2]) / 10000;
    SbVec3f toleranceVec = new SbVec3f(tolerance, tolerance, tolerance);
    if (hashScale.getValueRead()[0] != 0.0)
        hashScale.setValue(0, .333f * numPoints / hashScale.getValueRead()[0]);
    if (hashScale.getValueRead()[1] != 0.0)
        hashScale.setValue(1, .333f * numPoints / hashScale.getValueRead()[1]);
    if (hashScale.getValueRead()[2] != 0.0)
        hashScale.setValue(2, .333f * numPoints / hashScale.getValueRead()[2]);

    // Compute the base for the hash function, which is just the
    // minimum point of the bounding box:
    base.copyFrom( box.getMin().operator_minus());

    // Make a hash table.  There are numPoints entries in the hash
    // table. Each table entry points to the first point in the list
    // of points that hash to the corresponding key. Each entry in the
    // "hashNext" array points to the next point in the list. The
    // "indirect" table is a circularly linked list of indices that
    // are within tolerance of each other.
    hashTable = new int[numPoints];
    hashNext  = new int[numPoints];
    indirect  = new int[numPoints];
    for (i = 0; i < numPoints; i++) {
        hashTable[i] = -1;
        hashNext[i]  = -1;
        indirect[i]  = -1;
    }

    // Insert all points into the hash table.  Find common vertices.
    for (i = 0; i < numPoints; i++) {
        // Compute hash key
        hashValue = hash(points[i], hashScale, base, numPoints);

        // Set up "next" link
        hashNext[i] = hashTable[hashValue];

        // Enter in table
        hashTable[hashValue] = i;

        // Find all other vertices that are within tolerance
        found = false;
        lowHashValue  = hash(points[i].operator_minus(toleranceVec), hashScale,
                             base, numPoints);
        highHashValue  = hash(points[i].operator_minus(toleranceVec), hashScale,
                              base, numPoints);

        for (hv = lowHashValue; hv <= highHashValue; hv++) {
            for (j = hashTable[hv]; found == false && j >= 0; j = hashNext[j]){
                if (i != j && equal(points[j], points[i], tolerance)) {
                    // Splice into the circularly linked list
                    indirect[i] = indirect[j];
                    indirect[j] = i;
                    found = true;
                    break;
                }
            }

            // If no match found, link point to itself
            if (found == false)
                indirect[i] = i;
        }
    }

    // At this point, we're done with points[]; re-use storage to hold
    // vertNormals (memory optimization):
    vertNormals = points;
    numVertNormals = numPoints;
    points = null;

    // Calculate normals for all polygons
    final SbVec3f zeroVec = new SbVec3f(0,0,0);
    for(i = 0; i < numPoints; i++) {
        sum.copyFrom(faceNormals[i]);

        // This vertex is part of a degenerate face if its normal is
        // (mostly) the same as the zero vector.
        // We use a fixed tolerance for normals (suggested by Tim Wiegand)
        // since normals are unit length
        boolean isDegenerate = equal(zeroVec, sum, 1.e-4f);

        // Smooth normals if face normals are within crease angle
        for (j = indirect[i]; j != i; j = indirect[j]) {

            // If this vertex is part of a degenerate face, we always
            // want to smooth to get the normal:
            if (isDegenerate ||
                faceNormals[i].dot(faceNormals[j]) > cosCreaseAngle) 
                sum.operator_add_equal(faceNormals[j]);
        }
        sum.normalize();
        vertNormals[i] = sum;
    }

//    delete [] hashTable; java port
//    delete [] hashNext;
//    delete [] indirect;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    The hash function for vertices.  See comments below in generate
//    for details.
//
// Use: internal

private static int
hash(final SbVec3f vertex, final SbVec3f scale, final SbVec3f base,
     int numPoints)
//
////////////////////////////////////////////////////////////////////////
{
    int result;
    result = (int) Math.floor((vertex.operator_add(base)).dot(scale));

    if (result < 0) return 0;
    if (result >= numPoints) return numPoints-1;
    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the two points are the same within given
//    tolerance.
//
// Use: public

public boolean
equal(final SbVec3f a, final SbVec3f b, float tolerance)
//
////////////////////////////////////////////////////////////////////////
{
    float       diff;

    diff = a.getValueRead()[0] - b.getValueRead()[0];
    if ((diff < 0.0 ? -diff : diff) > tolerance)
        return false;

    diff = a.getValueRead()[1] - b.getValueRead()[1];
    if ((diff < 0.0 ? -diff : diff) > tolerance)
        return false;

    diff = a.getValueRead()[2] - b.getValueRead()[2];
    if ((diff < 0.0 ? -diff : diff) > tolerance)
        return false;

    return true;
}

    //! Returns number of normals generated. This will be equal to the
    //! number of points sent.
	public    int                 getNumNormals() { return numVertNormals; }

    //! Returns a pointer to the array of normals
    public SbVec3f[]      getNormals()       { return vertNormals; }

    //! Returns the i'th normal in the array
    public SbVec3f      getNormal(int i) { return vertNormals[i]; }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Allows shape to change number of normals after generation
//
// Use: public

public void setNumNormals(int newNum)
//
////////////////////////////////////////////////////////////////////////
{   
    if (newNum > numVertNormals) setNormal(newNum, new SbVec3f(0,0,0));
    else if (newNum < numVertNormals) numVertNormals = newNum;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Allows shape to change or rearrange normals after generation.
//
// Use: public

public void
setNormal(int index, final SbVec3f newNormal)
//
////////////////////////////////////////////////////////////////////////
{   
    // Make sure there's enough room for the new normal
    if (index >= numVertNormals) {
        int    newNumVertNormals = numVertNormals;
        
        if (newNumVertNormals <= 0) newNumVertNormals = index + 1;
                
        while (index >= newNumVertNormals)
            newNumVertNormals *= 2;

        final SbVec3f[] newVertNormals = new SbVec3f [newNumVertNormals]; for(int i=0; i<newNumVertNormals;i++) {newVertNormals[i] = new SbVec3f();}
        //memcpy(newVertNormals, vertNormals, (int) (numVertNormals * sizeof(SbVec3f)));
        for(int i=0; i<numVertNormals;i++) {newVertNormals[i].copyFrom(vertNormals[i]);}
        if (vertNormals != faceNormals) {
            //delete [] vertNormals; java port
        }
        vertNormals    = newVertNormals;
        numVertNormals = newNumVertNormals;
    }

    // Store new normal
    vertNormals[index] = newNormal;
}



}
