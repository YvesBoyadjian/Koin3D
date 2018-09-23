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
 |      This file defines the SoIndexedShape node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FloatArray;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all indexed vertex-based shapes.
/*!
\class SoIndexedShape
\ingroup Nodes
This node is the abstract base class for all vertex-based shapes that
are constructed from indices, including SoIndexedFaceSet,
SoIndexedTriangleStripSet, and SoIndexedLineSet.
SoIndexedShape defines fields that are used in all of its
subclasses.


All subclasses of SoNonIndexedShape construct objects by using the
coordinates specified by the \b vertexProperty  field
(from SoVertexShape), or the current inherited coordinates. 
The \b coordIndex 
field defined by this class contains the indices into the current
coordinates of the vertices of the shape. These indices are also used
for materials, normals, or texture coordinates when the appropriate
binding is <tt>PER_VERTEX_INDEXED</tt>.


Material and normal bindings are interpreted as follows for each subclass:
\code
OVERALL		One material for the entire shape.
PER_PART		Specific to the subclass.
PER_PART_INDEXED	Same as PER_PART, using indices from the
			materialIndex or normalIndex field.
PER_FACE		Specific to the subclass.
PER_FACE_INDEXED	Same as PER_FACE, using indices from the
			materialIndex or normalIndex field.
PER_VERTEX		One material per vertex.
PER_VERTEX_INDEXED	One material per vertex, using indices from the
			materialIndex or normalIndex field.
\endcode


When any <tt>_INDEXED</tt> binding is used for materials or normals, the
\b materialIndex  or \b normalIndex  field is used to determine the
indices for the materials or normals. If this field contains a single
value of -1 (the default), the coordinate indices from the
\b coordIndex  field are used as well for materials or normals. When
the binding is <tt>PER_VERTEX_INDEXED</tt>, indices in these fields that
correspond to negative indices in \b coordIndex  are skipped; for
other index bindings all the values in the fields are used, in order.


Explicit texture coordinates (as defined by SoTextureCoordinate2)
may be bound to vertices of an indexed
shape consecutively (if the texture coordinate binding is
<tt>PER_VERTEX</tt>) or by using the indices in the \b textureCoordIndex 
field (if the binding is <tt>PER_VERTEX_INDEXED</tt>). As with all
vertex-based shapes, if there is a current texture but no texture
coordinates are specified, a default texture coordinate mapping is
calculated using the bounding box of the shape.


Be sure that the indices contained in the \b coordIndex ,
\b materialIndex , \b normalIndex , and \b textureCoordIndex  fields
are valid with respect to the current state, or errors will occur.

\par See Also
\par
SoIndexedFaceSet, SoIndexedTriangleSet, SoIndexedLineSet, SoIndexedTriangleStripSet, SoMaterialBinding, SoNonIndexedShape, SoNormalBinding, SoShapeHints, SoTextureCoordinateBinding
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoIndexedShape extends SoVertexShape implements Destroyable {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoIndexedShape.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoIndexedShape.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoIndexedShape.class); }              
	
	
	
	 public final SoMFInt32 coordIndex = new SoMFInt32();
	  public final SoMFInt32 materialIndex = new SoMFInt32();
	   public final SoMFInt32 normalIndex = new SoMFInt32();
	   public final SoMFInt32 textureCoordIndex = new SoMFInt32();
	   
    //! These are filled in by the setupIndices routine:
    private int[]       texCoordI;
    private int[]       colorI;
    private int[]       normalI;
    private static int[]      consecutiveIndices;
    private static int  numConsecutiveIndicesAllocated;

    private int materialBinding;
    private int normalBinding;
    private int texCoordBinding;
	   

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoIndexedShape()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoIndexedShape*/);

    nodeHeader.SO_NODE_ADD_MFIELD(coordIndex,"coordIndex", new Integer(0));
    nodeHeader.SO_NODE_ADD_MFIELD(materialIndex,"materialIndex",     new Integer(-1));
    nodeHeader.SO_NODE_ADD_MFIELD(normalIndex,"normalIndex",       new Integer(-1));
    nodeHeader.SO_NODE_ADD_MFIELD(textureCoordIndex,"textureCoordIndex", new Integer(-1));
    colorI = null;
    normalI = null;
    texCoordI = null;
    // force reevaluation of binding:
    materialBinding = normalBinding = texCoordBinding = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (materialBinding == SoMaterialBindingElement.Binding.PER_VERTEX.getValue())      
            colorI = null;
        
    if (normalBinding == SoNormalBindingElement.Binding.PER_VERTEX.getValue()) 
            normalI = null;
            
    if (texCoordBinding == SoTextureCoordinateBindingElement.Binding.PER_VERTEX.getValue()) 
            texCoordI = null;

    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Keep things up to date when my fields change
//
// Use: protected

public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER) {
        if (list.getLastField() == coordIndex ||
            list.getLastField() == materialIndex ||
            list.getLastField() == normalIndex ||
            list.getLastField() == textureCoordIndex) {

            // Force re-evaluation:
            materialBinding = normalBinding = texCoordBinding = 0;
        }
    }

    super.notify(list);
}

	   
	   
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
    int                     i, numIndices, numUsed;
    int[]                       indices;
    SoCoordinateElement   ce = null;
    FloatArray               vpCoords = null;

    SoVertexProperty vp = (SoVertexProperty )vertexProperty.getValue();
    if (vp != null && vp.vertex.getNum() > 0) {
        vpCoords = vp.vertex.getValuesArray(0);
    } else {
        ce = SoCoordinateElement.getInstance(action.getState());
    }

    // Start with an empty box and zero sum
    center.setValue(0, 0, 0);
    box.makeEmpty();

    // Loop through coordinates, keeping bounding box and sum of coords
    numIndices = coordIndex.getNum();
    indices    = coordIndex.getValuesI(0);
    numUsed    = 0;
    for (i = 0; i < numIndices; i++) {

        // Look only at non-negative index values
        if (indices[i] >= 0) {
            SbVec3f v = (ce != null ? ce.get3((int) indices[i]) :
                                new SbVec3f(vpCoords,indices[i]));
            box.extendBy(v);
            center.operator_add_equal(v);
            numUsed++;
        }
    }

    // Center is average of all coordinates
    center.operator_div_equal((float) numUsed);
	}
	  
	 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns true if texture coordinates should be indexed.
//
// Use: protected, static

protected static boolean
areTexCoordsIndexed(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    return SoTextureCoordinateBindingElement.get(action.getState()) 
            == SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Starting at a given index in the coordIndex array, figure out
//    how many vertices there are before either the end of the array
//    or the next 'border' index:
//
// Use: protected

protected int
getNumVerts(int startCoord)
//
////////////////////////////////////////////////////////////////////////
{
    int result = 0;
    int numIndices = coordIndex.getNum();
    
    while (startCoord+result < numIndices &&
           coordIndex.operator_square_bracketI(startCoord+result) >= 0) {
        result++;
    }

    return result;
}

    //! These must not be called unless setupIndices has been called first:
    public int[]     getNormalIndices()
        { return (normalI != null ? normalI : consecutiveIndices); }
    public int[]     getColorIndices()
        { return (colorI != null ? colorI : consecutiveIndices); }
    public int[]     getTexCoordIndices()
        { return (texCoordI != null ? texCoordI : consecutiveIndices); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Setup for fast rendering.  This should be called by subclasses,
// which can then use the texCoordI/colorI/normalI arrays (which
// will either point to one of the coordIndex arrays, or to a
// consective array of integers.  This must be called AFTER the
// vpCache has been filled in.
//
// Use: protected

protected void
setupIndices(int numParts, int numFaces,
                             boolean needNormals, boolean needTexCoords)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (vpCache.vertexPtr == null) {
        SoDebugError.post("SoIndexedShape.setupIndices",
                   "vpCache.fillInCache must be called first!\n");
    }
    //Check for valid indices:
    int j;
    for (j = 0; j < coordIndex.getNum(); j++){  
        if (coordIndex.operator_square_bracketI(j) != -1) {
            if (coordIndex.operator_square_bracketI(j) >= vpCache.getNumVertices() ||
                coordIndex.operator_square_bracketI(j) < 0 ){
                SoDebugError.post("SoIndexedShape",
                        "vertex index "+coordIndex.operator_square_bracket(j)+" is out of range 0 - "+vpCache.getNumVertices() );
            }
        }
    }
    for (j = 0; j < materialIndex.getNum(); j++){       
        if (materialIndex.operator_square_bracketI(j) != -1) {
            if (materialIndex.operator_square_bracketI(j) >= vpCache.getNumColors() ||
                materialIndex.operator_square_bracketI(j) < 0 ){
                SoDebugError.post("SoIndexedShape",
                        "material index "+materialIndex.operator_square_bracket(j)+" is out of range 0 - "+vpCache.getNumColors() );
            }
        }
    }
    if (needNormals) {
        for (j = 0; j < normalIndex.getNum(); j++){     
            if (normalIndex.operator_square_bracketI(j) != -1) {
                if (normalIndex.operator_square_bracketI(j) >= vpCache.getNumNormals() ||
                    normalIndex.operator_square_bracketI(j) < 0 ){
                    SoDebugError.post("SoIndexedShape",
                        "normal index "+normalIndex.operator_square_bracket(j)+" is out of range 0 - "+vpCache.getNumNormals() );
                }
            }
        }
    }
    if (needTexCoords){
        for (j = 0; j < textureCoordIndex.getNum(); j++){       
            if (textureCoordIndex.operator_square_bracketI(j) != -1) {
                if (textureCoordIndex.operator_square_bracketI(j) >= vpCache.getNumTexCoords() ||
                    textureCoordIndex.operator_square_bracketI(j) < 0 ){
                    SoDebugError.post("SoIndexedShape",
                        "texture coordinate index "+textureCoordIndex.operator_square_bracket(j)+" is out of range 0 - "+vpCache.getNumTexCoords());
                }
            }
        }
    }
//#endif /*DEBUG*/ 

    if (materialBinding != vpCache.getMaterialBinding().getValue()) {
        // Free if old binding was PER_VERTEX:
        if (materialBinding == SoMaterialBindingElement.Binding.PER_VERTEX.getValue()) {
            // Ok to cast const away here:
            //delete[] ((int32_t *)colorI); java port
            colorI = null;
        }
        materialBinding = vpCache.getMaterialBinding().getValue();

//#ifdef DEBUG
        int numIndices = 0;
        int numIndicesNeeded = 0;
        boolean useCoordIndexOK = true;
        int numColorsNeeded = 0;
        String bindingString = null;
//#endif

        switch (vpCache.getMaterialBinding()) {
          case OVERALL:
            break;
          case PER_PART:
            allocateSequential(numParts);
            colorI = null;
//#ifdef DEBUG
            numColorsNeeded = numParts;
            bindingString = "PER_PART";
//#endif
            break;
          case PER_FACE:
            allocateSequential(numFaces);
            colorI = null;
//#ifdef DEBUG
            numColorsNeeded = numFaces;
            bindingString = "PER_FACE";
//#endif
            break;

          case PER_VERTEX:
            {
            // The annoying case:
            colorI = allocateSequentialWithHoles();
//#ifdef DEBUG
            for (int i = 0; i < coordIndex.getNum(); i++) {
                if (coordIndex.operator_square_bracketI(i) >= 0) ++numColorsNeeded;
            }
            bindingString = "PER_VERTEX";
//#endif
            }
            break;

          case PER_PART_INDEXED:
            if (materialIndex.operator_square_bracketI(0) < 0)
                colorI = coordIndex.getValuesI(0);
            else
                colorI = materialIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = false;
            numIndicesNeeded = numParts;
            bindingString = "PER_PART_INDEXED";
            if (colorI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = materialIndex.getNum();
            }
//#endif
            break;
          case PER_FACE_INDEXED:
            if (materialIndex.operator_square_bracketI(0) < 0)
                colorI = coordIndex.getValuesI(0);
            else
                colorI = materialIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = false;
            numIndicesNeeded = numFaces;
            bindingString = "PER_FACE_INDEXED";
            if (colorI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = materialIndex.getNum();
            }
//#endif
            break;
          case PER_VERTEX_INDEXED:
            if (materialIndex.operator_square_bracketI(0) < 0)
                colorI = coordIndex.getValuesI(0);
            else
                colorI = materialIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = true;
            numIndicesNeeded = coordIndex.getNum();
            bindingString = "PER_VERTEX_INDEXED";
            if (colorI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = materialIndex.getNum();
            }
//#endif
            break;
        }

//#ifdef DEBUG
        // Check for mis-use of default materialIndex field
        if (useCoordIndexOK == false && 
            colorI == coordIndex.getValuesI(0)) {
            SoDebugError.post("SoIndexedShape",
                "Material binding is "+bindingString+
                " but materialIndex[0] < 0; coordIndex"+
                " will be used, which is probably not what"+
                " you want");
        }
        // Check for enough indices:
        if (numIndices < numIndicesNeeded) {
            SoDebugError.post("SoIndexedShape",
                "Need "+numIndicesNeeded+" indices for "+bindingString+" "+
                " material binding, have only "+numIndices);
        }
        else if (numIndices > 0) {
            // Find greatest index:
            for (int i = 0; i < numIndices; i++) {
                if (colorI[i] > numColorsNeeded)
                    numColorsNeeded = colorI[i];
            }
        }
        if (vpCache.getNumColors() < numColorsNeeded) {
            SoDebugError.post("SoIndexedShape",
                "Material binding is "+bindingString+", but only "+vpCache.getNumColors()+
                " colors given ("+numColorsNeeded+" needed)");
        }
//#endif
    }
    if (needNormals && (normalBinding != vpCache.getNormalBinding().getValue())) {
        // Free if old binding was PER_VERTEX:
        if (normalBinding == SoNormalBindingElement.Binding.PER_VERTEX.getValue()) {
            // Ok to cast const away here:
            //delete[] ((int32_t *)normalI); java port
            normalI = null;
        }
        normalBinding = vpCache.getNormalBinding().getValue();

//#ifdef DEBUG
        int numIndices = 0;
        int numIndicesNeeded = 0;
        boolean useCoordIndexOK = true;
        int numNormalsNeeded = 0;
        String bindingString = null;
//#endif

        switch (vpCache.getNormalBinding()) {
          case OVERALL:
            break;
          case PER_PART:
            allocateSequential(numParts);
            normalI = null;
//#ifdef DEBUG
            numNormalsNeeded = numParts;
            bindingString = "PER_PART";
//#endif
            break;
          case PER_FACE:
            allocateSequential(numFaces);
            normalI = null;
//#ifdef DEBUG
            numNormalsNeeded = numFaces;
            bindingString = "PER_FACE";
//#endif
            break;

          case PER_VERTEX:
            {
            // The annoying case:
            normalI = allocateSequentialWithHoles();
//#ifdef DEBUG
            for (int i = 0; i < coordIndex.getNum(); i++) {
                if (coordIndex.operator_square_bracketI(i) >= 0) ++numNormalsNeeded;
            }
            bindingString = "PER_VERTEX";
//#endif
            }
            break;

          case PER_PART_INDEXED:
            if (normalIndex.operator_square_bracketI(0) < 0)
                normalI = coordIndex.getValuesI(0);
            else
                normalI = normalIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = false;
            numIndicesNeeded = numParts;
            bindingString = "PER_PART_INDEXED";
            if (normalI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = normalIndex.getNum();
            }
//#endif
            break;
          case PER_FACE_INDEXED:
            if (normalIndex.operator_square_bracketI(0) < 0)
                normalI = coordIndex.getValuesI(0);
            else
                normalI = normalIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = false;
            numIndicesNeeded = numFaces;
            bindingString = "PER_FACE_INDEXED";
            if (normalI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = normalIndex.getNum();
            }
//#endif
            break;
          case PER_VERTEX_INDEXED:
            if (normalIndex.operator_square_bracketI(0) < 0)
                normalI = coordIndex.getValuesI(0);
            else
                normalI = normalIndex.getValuesI(0);
//#ifdef DEBUG
            useCoordIndexOK = true;
            numIndicesNeeded = coordIndex.getNum();
            bindingString = "PER_VERTEX_INDEXED";
            if (normalI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = normalIndex.getNum();
            }
//#endif
            break;
        }

//#ifdef DEBUG
        // Check for mis-use of default normalIndex field
        if (useCoordIndexOK == false && 
            normalI == coordIndex.getValuesI(0)) {
            SoDebugError.post("SoIndexedShape",
                "Normal binding is "+bindingString+
                " but normalIndex[0] < 0; coordIndex"+
                " will be used, which is probably not what"+
                " you want");
        }
        // Check for enough indices:
        if (numIndices < numIndicesNeeded) {
            SoDebugError.post("SoIndexedShape",
                "Need "+numIndicesNeeded+" indices for "+bindingString+" "+
                " normal binding, have only "+numIndices);
        }
        else if (numIndices > 0) {
            // Find greatest index:
            for (int i = 0; i < numIndices; i++) {
                if (normalI[i] > numNormalsNeeded)
                    numNormalsNeeded = normalI[i];
            }
        }
        if (vpCache.getNumNormals() < numNormalsNeeded) {
            SoDebugError.post("SoIndexedShape",
                "Normal binding is "+bindingString+", but only "+vpCache.getNumNormals()+
                " normals given ("+numNormalsNeeded+" needed)");
        }
//#endif
    }

    if (needTexCoords && (texCoordBinding != vpCache.getTexCoordBinding().getValue())) {
        // Free if old binding was PER_VERTEX:
        if (texCoordBinding == SoTextureCoordinateBindingElement.Binding.PER_VERTEX.getValue()) {
            // Ok to cast const away here:
            //delete[] ((int32_t *)texCoordI); java port
            texCoordI = null;
        }
        texCoordBinding = vpCache.getTexCoordBinding().getValue();

//#ifdef DEBUG
        int numIndices = 0;
        int numIndicesNeeded = 0;
        int numTexCoordsNeeded = 0;
        String bindingString = null;
//#endif
        switch (vpCache.getTexCoordBinding()) {
          case PER_VERTEX:
            {
            // The annoying case:
            texCoordI = allocateSequentialWithHoles();
//#ifdef DEBUG
            for (int i = 0; i < coordIndex.getNum(); i++) {
                if (coordIndex.operator_square_bracketI(i) >= 0) ++numTexCoordsNeeded;
            }
            bindingString = "PER_VERTEX";
//#endif
            }
            break;

          case PER_VERTEX_INDEXED:
            if (textureCoordIndex.operator_square_bracketI(0) < 0)
                texCoordI = coordIndex.getValuesI(0);
            else
                texCoordI = textureCoordIndex.getValuesI(0);
//#ifdef DEBUG
            numIndicesNeeded = numFaces;
            bindingString = "PER_VERTEX_INDEXED";
            if (texCoordI == coordIndex.getValuesI(0)) {
                numIndices = coordIndex.getNum();
            } else {
                numIndices = textureCoordIndex.getNum();
            }
//#endif
            break;
        }
//#ifdef DEBUG
        // Check for enough indices:
        if (numIndices < numIndicesNeeded) {
            SoDebugError.post("SoIndexedShape",
                "Need "+bindingString+" indices for "+numFaces+" "+
                " texCoord binding, have only "+numIndices);
        }
        else if (numIndices > 0) {
            // Find greatest index:
            for (int i = 0; i < numIndices; i++) {
                if (texCoordI[i] > numTexCoordsNeeded)
                    numTexCoordsNeeded = texCoordI[i];
            }
        }
        if (vpCache.getNumTexCoords() < numTexCoordsNeeded) {
            SoDebugError.post("SoIndexedShape",
                "TexCoord binding is "+bindingString+", but only "+vpCache.getNumTexCoords()+
                " texCoords given ("+numTexCoordsNeeded+" needed)");
        }
//#endif
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Re-allocate the consecutiveIndices array, if necessary
//
// Use: private

private void
allocateSequential(int howMany)
//
////////////////////////////////////////////////////////////////////////
{
    if (howMany > numConsecutiveIndicesAllocated) {
        numConsecutiveIndicesAllocated = howMany;
        if (consecutiveIndices != null) {
            consecutiveIndices = null;
        }
        consecutiveIndices = new int[howMany];
        for (int i = 0; i < howMany; i++) {
            consecutiveIndices[i] = i;
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Return an array for PER_VERTEX bindings (with -1's in the right
//    spots)
//
// Use: private

private int[]
allocateSequentialWithHoles()
//
////////////////////////////////////////////////////////////////////////
{
    int count = 0;
    int num = coordIndex.getNum();
    int[] result = new int[num];
    for (int i = 0; i < num; i++) {
        if (coordIndex.operator_square_bracketI(i) >= 0) {
            result[i] = count;
            count++;
        }
        else
            result[i] = coordIndex.operator_square_bracketI(i); // Just copy-over negatives
    }
    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedShape class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoIndexedShape.class, "IndexedShape",
                                 SoVertexShape.class);
}


}
