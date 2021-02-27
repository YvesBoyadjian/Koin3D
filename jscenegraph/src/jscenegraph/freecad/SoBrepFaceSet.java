package jscenegraph.freecad;

import com.jogamp.opengl.GL2;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.elements.*;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.*;
import jscenegraph.port.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class SoBrepFaceSet extends SoIndexedFaceSet {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBrepFaceSet.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoBrepFaceSet.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoBrepFaceSet.class); }

    private
    enum Binding {
        NONE_OVERALL,
        PER_PART,
        PER_PART_INDEXED,
        PER_FACE,
        PER_FACE_INDEXED,
        PER_VERTEX,
        PER_VERTEX_INDEXED
        //NONE = OVERALL
    };

    public final SoMFInt32 partIndex = new SoMFInt32();

    private static class VBO implements Destroyable {

        @Override
        public void destructor() {

        }

        private class Buffer {
            final int[] myvbo = new int[2];
            long vertex_array_size;
            long index_array_size;
            boolean updateVbo;
            boolean vboLoaded;
        };

        static boolean vboAvailable = true;
        int indice_array;
        final Map<Integer, Buffer> vbomap = new HashMap<>();

        public void render(SoGLRenderAction action,
                                SoGLCoordinateElement vertexlist,
                                IntArrayPtr vertexindices,
                                        int num_indices,
                                IntArray/*Ptr*/ partindices,
                                        int num_partindices,
                                SbVec3fArray normalsC,
                                IntArrayPtr normalindices,
                                        SoMaterialBundle materials,
                                IntArrayPtr matindices,
                                        SoTextureCoordinateBundle texcoords,
                                IntArrayPtr texindices,
                                int nbind,
                                int mbind,
                                        boolean texture)
        {
            GL2 gl2 = action.getState().getGL2();

            MutableSbVec3fArray normals = MutableSbVec3fArray.from(normalsC);

            final float[] dummy = new float[3];

            //(void)texcoords; (void)texindices; (void)texture;
    SbVec3fArray coords3d = null;
            SbVec3fArray cur_coords3d = null;
            coords3d = vertexlist.getArrayPtr3();
            cur_coords3d = new SbVec3fArray(coords3d);

    final MutableIntArray viptr = MutableIntArray.from(vertexindices);
    final MutableIntArray viendptr = viptr.plus(num_indices);
    final MutableIntArray piptr = MutableIntArray.from(partindices);
    final MutableIntArray piendptr = piptr.plus(num_partindices);
            int v1, v2, v3, v4, pi;
            final SbVec3fSingle dummynormal = new SbVec3fSingle(0,0,1);
            int numverts = vertexlist.getNum();

    final MutableSbVec3fArray currnormal =
            (normals != null) ? MutableSbVec3fArray.from(normalsC) : new MutableSbVec3fArray(dummynormal);

            int matnr = 0;
            int trinr = 0;

            FloatBuffer vertex_array = null;
            IntBuffer index_array = null;
            final SbColor  mycolor1 = new SbColor(),mycolor2 = new SbColor(),mycolor3 = new SbColor();
            final MutableSbVec3fArray mynormal1 = new MutableSbVec3fArray(currnormal);
            final MutableSbVec3fArray mynormal2 = new MutableSbVec3fArray(currnormal);
            final MutableSbVec3fArray mynormal3 = new MutableSbVec3fArray(currnormal);
            int indice=0;
            int RGBA,R,G,B,A;
            float Rf,Gf,Bf,Af;

            int contextId = action.getCacheContext();
            boolean res_second = !this.vbomap.containsKey(contextId);
            if(res_second) {
                this.vbomap.put(contextId, new VBO.Buffer());
            }
            VBO.Buffer buf = this.vbomap.get(contextId);
            if (res_second) {
//#ifdef FC_OS_WIN32
//        const cc_glglue * glue = cc_glglue_instance(action.getCacheContext());
//                PFNGLGENBUFFERSPROC glGenBuffersARB = (PFNGLGENBUFFERSPROC)cc_glglue_getprocaddress(glue, "glGenBuffersARB");
//#endif
                gl2.glGenBuffersARB(2, buf.myvbo);
                buf.vertex_array_size = 0;
                buf.index_array_size = 0;
                buf.vboLoaded = false;
            }

            if ((buf.vertex_array_size != ((Float.BYTES) * num_indices * 10)) ||
            (buf.index_array_size != ((Integer.BYTES) * num_indices))) {
            if ((buf.vertex_array_size != 0 ) && ( buf.index_array_size != 0))
                buf.updateVbo = true;
        }

            // vbo loaded is defining if we must pre-load data into the VBO. When the variable is set to 0
            // it means that the VBO has not been initialized
            // updateVbo is tracking the need to update the content of the VBO which act as a buffer within
            // the graphic card
            // TODO FINISHING THE COLOR SUPPORT !

            if (!buf.vboLoaded || buf.updateVbo) {
//#ifdef FC_OS_WIN32
//        const cc_glglue * glue = cc_glglue_instance(action.getCacheContext());
//
//                PFNGLBINDBUFFERARBPROC glBindBufferARB = (PFNGLBINDBUFFERARBPROC) cc_glglue_getprocaddress(glue, "glBindBufferARB");
//                PFNGLMAPBUFFERARBPROC glMapBufferARB = (PFNGLMAPBUFFERARBPROC) cc_glglue_getprocaddress(glue, "glMapBufferARB");
//                PFNGLGENBUFFERSPROC glGenBuffersARB = (PFNGLGENBUFFERSPROC)cc_glglue_getprocaddress(glue, "glGenBuffersARB");
//                PFNGLDELETEBUFFERSARBPROC glDeleteBuffersARB = (PFNGLDELETEBUFFERSARBPROC)cc_glglue_getprocaddress(glue, "glDeleteBuffersARB");
//                PFNGLBUFFERDATAARBPROC glBufferDataARB = (PFNGLBUFFERDATAARBPROC)cc_glglue_getprocaddress(glue, "glBufferDataARB");
//#endif
                // We must manage buffer size increase let's clear everything and re-init to test the
                // clearing process
                gl2.glDeleteBuffersARB(/*2,*/ buf.myvbo);
                gl2.glGenBuffersARB(2, buf.myvbo);
                vertex_array = Util.float_malloc ( (Float.BYTES) * num_indices * 10 );
                index_array = Util.int_malloc ( (Integer.BYTES) * num_indices );
                buf.vertex_array_size = (Float.BYTES) * num_indices * 10;
                buf.index_array_size = (Integer.BYTES) * num_indices;
                this.vbomap.put(contextId, buf);
                this.indice_array = 0;

                // Get the initial colors
                SoState state = action.getState();
                mycolor1.copyFrom(SoLazyElement.getDiffuse(state,0));
                mycolor2.copyFrom(SoLazyElement.getDiffuse(state,0));
                mycolor3.copyFrom(SoLazyElement.getDiffuse(state,0));

                pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                while (pi == 0) {
                    // It may happen that a part has no triangles
                    pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                    if (mbind == Binding.PER_PART.ordinal())
                        matnr++;
                    else if (mbind == Binding.PER_PART_INDEXED.ordinal())
                        matindices.plusPlus();
                }

                while (viptr.plusLessThan(2, viendptr)) {
                    v1 = viptr.getPlusPlus();
                    v2 = viptr.getPlusPlus();
                    v3 = viptr.getPlusPlus();
                    // This test is for robustness upon buggy data sets
                    if (v1 < 0 || v2 < 0 || v3 < 0 ||
                            v1 >= numverts || v2 >= numverts || v3 >= numverts) {
                        break;
                    }
                    v4 = viptr.lessThan(viendptr) ? viptr.getPlusPlus() : -1;
                    //(void)v4;

                    if (mbind == Binding.PER_PART.ordinal()) {
                        if (trinr == 0) {
                            materials.send(matnr++, true);
                            mycolor1.copyFrom(SoLazyElement.getDiffuse(state,matnr-1));
                            mycolor2.copyFrom(mycolor1);
                            mycolor3.copyFrom(mycolor1);
                        }
                    }
                    else if (mbind == Binding.PER_PART_INDEXED.ordinal()) {
                        if (trinr == 0)
                            materials.send(matindices.getPlusPlus(), true);
                    }
                    else if (mbind == Binding.PER_VERTEX.ordinal() || mbind == Binding.PER_FACE.ordinal()) {
                        materials.send(matnr++, true);
                    }
                    else if (mbind == Binding.PER_VERTEX_INDEXED.ordinal() || mbind == Binding.PER_FACE_INDEXED.ordinal()) {
                        materials.send(matindices.getPlusPlus(), true);
                    }

                    if (normals !=null) {
                        if (nbind == Binding.PER_VERTEX.ordinal() || nbind == Binding.PER_FACE.ordinal()) {
                            currnormal.assign(normals); normals.plusPlus();
                            mynormal1.assign(currnormal);
                        }
                        else if (nbind == Binding.PER_VERTEX_INDEXED.ordinal() || nbind == Binding.PER_FACE_INDEXED.ordinal()) {
                            currnormal.assign(normals, normalindices.getPlusPlus());
                            mynormal1.assign(currnormal);
                        }
                    }
                    if (mbind == Binding.PER_VERTEX.ordinal())
                        materials.send(matnr++, true);
                    else if (mbind == Binding.PER_VERTEX_INDEXED.ordinal())
                        materials.send(matindices.getPlusPlus(), true);

                    if (normals != null) {
                        if (nbind == Binding.PER_VERTEX.ordinal()) {
                            currnormal.assign(normals); normals.plusPlus();
                            mynormal2.assign(currnormal);
                        }
                        else if (nbind == Binding.PER_VERTEX_INDEXED.ordinal()) {
                            currnormal.assign(normals,normalindices.getPlusPlus());
                            mynormal2.assign(currnormal);
                        }
                    }

                    if (mbind == Binding.PER_VERTEX.ordinal())
                        materials.send(matnr++, true);
                    else if (mbind == Binding.PER_VERTEX_INDEXED.ordinal())
                        materials.send(matindices.getPlusPlus(), true);
                    if (normals != null) {
                        if (nbind == Binding.PER_VERTEX.ordinal()) {
                            currnormal.assign(normals); normals.plusPlus();
                            mynormal3.assign(currnormal);
                        }
                        else if (nbind == Binding.PER_VERTEX_INDEXED.ordinal()) {
                            currnormal.assign(normals,normalindices.getPlusPlus());
                            mynormal3.assign(currnormal);
                        }
                    }
                    if (nbind == Binding.PER_VERTEX_INDEXED.ordinal())
                        normalindices.plusPlus();

                    /* We building the Vertex dataset there and push it to a VBO */
            /* The Vertex array shall contain per element vertex_coordinates[3],
            normal_coordinates[3], color_value[3] (RGBA format) */

                    index_array.put(this.indice_array,   this.indice_array);
                    index_array.put(this.indice_array+1, this.indice_array + 1);
                    index_array.put(this.indice_array+2, this.indice_array + 2);
                    this.indice_array += 3;

                    cur_coords3d.get3Floats(v1,dummy);
                    vertex_array.put(indice+0, dummy[0]);
                    vertex_array.put(indice+1, dummy[1]);
                    vertex_array.put(indice+2, dummy[2]);

                    mynormal1.get3Floats(dummy);
                    vertex_array.put(indice+3, dummy[0]);
                    vertex_array.put(indice+4, dummy[1]);
                    vertex_array.put(indice+5, dummy[2]);

                    /* We decode the Vertex1 color */
                    RGBA = mycolor1.getPackedValue();
                    R = ( RGBA & 0xFF000000 ) >>> 24 ;
                    G = ( RGBA & 0xFF0000 ) >>> 16;
                    B = ( RGBA & 0xFF00 ) >>> 8;
                    A = ( RGBA & 0xFF );

                    Rf = (((float )R) / 255.0f);
                    Gf = (((float )G) / 255.0f);
                    Bf = (((float )B) / 255.0f);
                    Af = (((float )A) / 255.0f);

                    vertex_array.put(indice+6, Rf);
                    vertex_array.put(indice+7, Gf);
                    vertex_array.put(indice+8, Bf);
                    vertex_array.put(indice+9, Af);
                    indice+=10;

                    cur_coords3d.get3Floats(v2,dummy);
                    vertex_array.put(indice+0, dummy[0]);
                    vertex_array.put(indice+1, dummy[1]);
                    vertex_array.put(indice+2, dummy[2]);

                    mynormal2.get3Floats(dummy);
                    vertex_array.put(indice+3, dummy[0]);
                    vertex_array.put(indice+4, dummy[1]);
                    vertex_array.put(indice+5, dummy[2]);

                    RGBA = mycolor2.getPackedValue();
                    R = ( RGBA & 0xFF000000 ) >>> 24 ;
                    G = ( RGBA & 0xFF0000 ) >>> 16;
                    B = ( RGBA & 0xFF00 ) >>> 8;
                    A = ( RGBA & 0xFF );

                    Rf = (((float )R) / 255.0f);
                    Gf = (((float )G) / 255.0f);
                    Bf = (((float )B) / 255.0f);
                    Af = (((float )A) / 255.0f);

                    vertex_array.put(indice+6, Rf);
                    vertex_array.put(indice+7, Gf);
                    vertex_array.put(indice+8, Bf);
                    vertex_array.put(indice+9, Af);
                    indice+=10;

                    cur_coords3d.get3Floats(v3,dummy);
                    vertex_array.put(indice+0, dummy[0]);
                    vertex_array.put(indice+1, dummy[1]);
                    vertex_array.put(indice+2, dummy[2]);

                    mynormal3.get3Floats(dummy);
                    vertex_array.put(indice+3, dummy[0]);
                    vertex_array.put(indice+4, dummy[1]);
                    vertex_array.put(indice+5, dummy[2]);

                    RGBA = mycolor3.getPackedValue();
                    R = ( RGBA & 0xFF000000 ) >>> 24 ;
                    G = ( RGBA & 0xFF0000 ) >>> 16;
                    B = ( RGBA & 0xFF00 ) >>> 8;
                    A = ( RGBA & 0xFF );

                    Rf = (((float )R) / 255.0f);
                    Gf = (((float )G) / 255.0f);
                    Bf = (((float )B) / 255.0f);
                    Af = (((float )A) / 255.0f);

                    vertex_array.put(indice+6, Rf);
                    vertex_array.put(indice+7, Gf);
                    vertex_array.put(indice+8, Bf);
                    vertex_array.put(indice+9, Af);
                    indice+=10;

                    /* ============================================================ */
                    trinr++;
                    if (pi == trinr) {
                        pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                        while (pi == 0) {
                            // It may happen that a part has no triangles
                            pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                            if (mbind == Binding.PER_PART.ordinal())
                                matnr++;
                            else if (mbind == Binding.PER_PART_INDEXED.ordinal())
                                matindices.plusPlus();
                        }
                        trinr = 0;
                    }
                }

                gl2.glBindBuffer/*ARB*/(GL2.GL_ARRAY_BUFFER/*_ARB*/, buf.myvbo[0]);
                gl2.glBufferData/*ARB*/(GL2.GL_ARRAY_BUFFER/*_ARB*/, (Float.BYTES) * indice , vertex_array, GL2.GL_DYNAMIC_DRAW/*_ARB*/);

                gl2.glBindBuffer/*ARB*/(GL2.GL_ELEMENT_ARRAY_BUFFER/*_ARB*/, buf.myvbo[1]);
                gl2.glBufferData/*ARB*/(GL2.GL_ELEMENT_ARRAY_BUFFER/*_ARB*/, (Integer.BYTES) * this.indice_array , index_array, GL2.GL_DYNAMIC_DRAW/*_ARB*/);

                gl2.glBindBuffer/*ARB*/(GL2.GL_ARRAY_BUFFER/*_ARB*/, 0);
                gl2.glBindBuffer/*ARB*/(GL2.GL_ELEMENT_ARRAY_BUFFER/*_ARB*/, 0);

                buf.vboLoaded = true;
                buf.updateVbo = false;
                //free(vertex_array); java port
                //free(index_array); java port
            }

            // This is the VBO rendering code
//#ifdef FC_OS_WIN32
//    const cc_glglue * glue = cc_glglue_instance(action.getCacheContext());
//            PFNGLBINDBUFFERARBPROC glBindBufferARB = (PFNGLBINDBUFFERARBPROC)cc_glglue_getprocaddress(glue, "glBindBufferARB");
//#endif

            if (!buf.updateVbo) {
                gl2.glBindBuffer/*ARB*/(GL2.GL_ARRAY_BUFFER/*_ARB*/, buf.myvbo[0]);
                gl2.glBindBuffer/*ARB*/(GL2.GL_ELEMENT_ARRAY_BUFFER/*_ARB*/, buf.myvbo[1]);
            }

            gl2.glEnableClientState(GL2.GL_VERTEX_ARRAY);
            gl2.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl2.glEnableClientState(GL2.GL_COLOR_ARRAY);

            gl2.glVertexPointer(3,GL2.GL_FLOAT,10*(Float.BYTES),0);
            gl2.glNormalPointer(GL2.GL_FLOAT,10*(Float.BYTES),(int)(3*(Float.BYTES)));
            gl2.glColorPointer(4,GL2.GL_FLOAT,10*(Float.BYTES),(int)(6*(Float.BYTES)));

            gl2.glDrawElements(GL2.GL_TRIANGLES, this.indice_array, GL2.GL_UNSIGNED_INT, (int)0);

            gl2.glDisableClientState(GL2.GL_COLOR_ARRAY);
            gl2.glDisableClientState(GL2.GL_NORMAL_ARRAY);
            gl2.glDisableClientState(GL2.GL_VERTEX_ARRAY);
            gl2.glBindBuffer/*ARB*/(GL2.GL_ARRAY_BUFFER/*_ARB*/, 0);
            gl2.glBindBuffer/*ARB*/(GL2.GL_ELEMENT_ARRAY_BUFFER/*_ARB*/, 0);
            buf.updateVbo = false;
            // The data is within the VBO we can clear it at application level
        }
    }

    VBO pimpl = new VBO();

    public SoBrepFaceSet()
    {
        nodeHeader.SO_NODE_CONSTRUCTOR(SoBrepFaceSet.class);
        nodeHeader.SO_NODE_ADD_MFIELD(partIndex,"partIndex", (-1));

//        selContext = std::make_shared<SelContext>();
//        selContext2 = std::make_shared<SelContext>();
//        packedColor = 0;
//
//        pimpl.reset(new VBO);
    }

    public void destructor() {
        pimpl.destructor();
        super.destructor();
    }

//#ifdef RENDER_GLARRAYS
    public void GLRender(SoGLRenderAction action)
    {
        SoState state = action.getState();
        // Disable caching for this node
        SoGLCacheContextElement.shouldAutoCache(state, SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());

        final SoMaterialBundle mb = new SoMaterialBundle(action);
        try {
            Binding mbind = this.findMaterialBinding2(state);

            final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, true, false);
            try {
                boolean doTextures = tb.needCoordinates();

//                if (ctx.coordIndex.getNum() < 3)
//                    return;

                {

                    // When setting transparency shouldGLRender() handles the rendering and returns false.
                    // Therefore generatePrimitives() needs to be re-implemented to handle the materials
                    // correctly.
                    if (!this.shouldGLRender(action))
                        return;

//#ifdef RENDER_GLARRAYS
//                    if (!doTextures && index_array.size() && hl_idx < 0 && num_selected <= 0) {
//                        if (mbind == 0) {
//                            mb.sendFirst(); // only one material . apply it!
//                            renderSimpleArray();
//                            return;
//                        } else if (mbind == 1) {
//                            renderColoredArray( & mb);
//                            return;
//                        }
//                    }
//#endif

                    Binding nbind = this.findNormalBinding2(state);

        final SoCoordinateElement[] coords = new SoCoordinateElement[1]; // ptr
        final SbVec3fArray[] normals = new SbVec3fArray[1];
        final IntArrayPtr[] cindices = new IntArrayPtr[1];
        final int[] numindices = new int[1];
        final IntArrayPtr[] nindices = new IntArrayPtr[1];
        final IntArrayPtr[] tindices = new IntArrayPtr[1];
        final IntArrayPtr[] mindices = new IntArrayPtr[1];
        IntArray pindices;
                    int numparts;
                    final boolean[] normalCacheUsed = new boolean[1];

                    boolean sendNormals = !mb.isColorOnly() || tb.isFunction();

                    this.getVertexData(state, coords, normals, cindices,
                            nindices, tindices, mindices, numindices,
                            sendNormals, normalCacheUsed);

                    mb.sendFirst(); // make sure we have the correct material

                    // just in case someone forgot
                    if (mindices[0]== null) mindices[0] = cindices[0];
                    if (nindices[0]==null) nindices[0] = cindices[0];
                    pindices = this.partIndex.getValues(0);
                    numparts = this.partIndex.getNum();

                    renderShape(/*state*/action, /*vboAvailable*/false, (
                                    SoGLCoordinateElement) (coords[0]), cindices[0], numindices[0],
                            pindices, numparts, normals[0], nindices[0], mb, mindices[0],
                    tb, tindices[0], nbind, mbind, doTextures ? 1 : 0);

                    if (normalCacheUsed[0])
                        this.readUnlockNormalCache();
                }

//        // Workaround for #0000433
////#if !defined(FC_OS_WIN32)
//        renderHighlight(action,ctx);
//        renderSelection(action,ctx);
////#endif
            }
            finally {
                tb.destructor();
            }
        }
        finally {
            mb.destructor();
        }
    }

//    public void GLRender(SoGLRenderAction action)
//    {
//        //SoBase::staticDataLock();
////        static bool init = false;
////        if (!init) {
////            std::string ext = (const char*)(glGetString(GL_EXTENSIONS));
//            pimpl.vboAvailable = true;//(ext.find("GL_ARB_vertex_buffer_object") != std::string::npos);
////            init = true;
////        }
//        //SoBase::staticDataUnlock();
//
//        if (this.coordIndex.getNum() < 3)
//        return;
//
////        SelContextPtr ctx2;
////        std::vector<SelContextPtr> ctxs;
////        SelContextPtr ctx = Gui::SoFCSelectionRoot::getRenderContext(this,selContext,ctx2);
////        if(ctx2 && ctx2.selectionIndex.empty())
////            return;
////        if(selContext2.checkGlobal(ctx))
////            ctx = selContext2;
////        if(ctx && (!ctx.selectionIndex.size() && ctx.highlightIndex<0))
////            ctx.reset();
//
//        SoState state = action.getState();
////        selCounter.checkRenderCache(state);
//
//        // override material binding to PER_PART_INDEX to achieve
//        // preselection/selection with transparency
//        boolean pushed = false;//overrideMaterialBinding(action,ctx,ctx2);
//        if(!pushed){
//            // for non transparent cases, we still use the old selection rendering
//            // code, because it can override emission color, which gives a more
//            // distinguishable selection highlight. The above material binding
//            // override method can't, because Coin does not support per part
//            // emission color
//
//            // There are a few factors affects the rendering order.
//            //
//            // 1) For normal case, the highlight (pre-selection) is the top layer. And since
//            // the depth buffer clipping is on here, we shall draw highlight first, then
//            // selection, then the rest part.
//            //
//            // 2) If action.isRenderingDelayedPaths() is true, it means we are rendering
//            // with depth buffer clipping turned off (always on top rendering), so we shall
//            // draw the top layer last, i.e. renderHighlight() last
//            //
//            // 3) If highlightIndex==INT_MAX, it means we are rendering full object highlight
//            // In order to not obscure selection layer, we shall draw highlight after selection
//            // if and only if it is not a full object selection.
//            //
//            // Transparency complicates stuff even more, but not here. It will be handled inside
//            // overrideMaterialBinding()
//            //
////            if(ctx && ctx.highlightIndex==INT_MAX) {
////                if(ctx.selectionIndex.empty() || ctx.isSelectAll()) {
////                    if(ctx2) {
////                        ctx2.selectionColor = ctx.highlightColor;
////                        renderSelection(action,ctx2);
////                    } else
////                        renderHighlight(action,ctx);
////                }else{
////                    if(!action.isRenderingDelayedPaths())
////                        renderSelection(action,ctx);
////                    if(ctx2) {
////                        ctx2.selectionColor = ctx.highlightColor;
////                        renderSelection(action,ctx2);
////                    } else
////                        renderHighlight(action,ctx);
////                    if(action.isRenderingDelayedPaths())
////                        renderSelection(action,ctx);
////                }
////                return;
////            }
//
//            if(!action.isRenderingDelayedPaths())
//                ;//renderHighlight(action,ctx);
////            if(ctx && ctx.selectionIndex.size()) {
////                if(ctx.isSelectAll()) {
////                    if(ctx2) {
////                        ctx2.selectionColor = ctx.selectionColor;
////                        renderSelection(action,ctx2);
////                    } else
////                        renderSelection(action,ctx);
////                    if(action.isRenderingDelayedPaths())
////                        renderHighlight(action,ctx);
////                    return;
////                }
////                if(!action.isRenderingDelayedPaths())
////                    renderSelection(action,ctx);
////            }
////            if(ctx2) {
////                renderSelection(action,ctx2,false);
////                if(action.isRenderingDelayedPaths()) {
////                    renderSelection(action,ctx);
////                    renderHighlight(action,ctx);
////                }
////                return;
////            }
//        }
//
//        final SoMaterialBundle mb = new SoMaterialBundle(action);
//        try {
//            // It is important to send material before shouldGLRender(), otherwise
//            // material override with transparncy won't work.
//            mb.sendFirst();
//
//            // When setting transparency shouldGLRender() handles the rendering and returns false.
//            // Therefore generatePrimitives() needs to be re-implemented to handle the materials
//            // correctly.
//            if (this.shouldGLRender(action)) {
//                Binding mbind = this.findMaterialBinding2(state);
//                Binding nbind = this.findNormalBinding2(state);
//
//        final SoCoordinateElement[] coords = new SoCoordinateElement[1];
//        final SbVec3fArray[] normals = new SbVec3fArray[1];
//        final IntArrayPtr[] cindices = new IntArrayPtr[1];
//                final int[] numindices = new int[1];
//        final IntArrayPtr[] nindices = new IntArrayPtr[1];
//        final IntArrayPtr[] tindices = new IntArrayPtr[1];
//        final IntArrayPtr[] mindices = new IntArrayPtr[1];
//        final IntArray pindices;
//                int numparts;
//                boolean doTextures;
//                final boolean[] normalCacheUsed = new boolean[1];
//
//                final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, true, false);
//                try {
//                    doTextures = tb.needCoordinates();
//                    boolean sendNormals = !mb.isColorOnly() || tb.isFunction();
//
//                    this.getVertexData(state, coords, normals, cindices,
//                            nindices, tindices, mindices, numindices,
//                            sendNormals, normalCacheUsed);
//
//                    // just in case someone forgot
//                    if (null==mindices[0]) mindices[0] = cindices[0];
//                    if (null==nindices[0]) nindices[0] = cindices[0];
//                    pindices = this.partIndex.getValues(0);
//                    numparts = this.partIndex.getNum();
//
//                    boolean hasVBO = /*!ctx2 &&*/ pimpl.vboAvailable;
//                    if (hasVBO) {
//                        // get the VBO status of the viewer
//                        //Gui::SoGLVBOActivatedElement::get(state, hasVBO);
//                        //
//                        //if (SoGLVBOElement::shouldCreateVBO(state, numindices)) {
//                        //    this.startVertexArray(action, coords, normals, false, false);
//                        //}
//                    }
//                    renderShape(action, hasVBO,
//                            (SoGLCoordinateElement) (coords[0]), cindices[0], numindices[0],
//                            pindices, numparts, normals[0], nindices[0], mb, mindices[0],
//                    tb, tindices[0], nbind, mbind, doTextures ? 1 : 0);
//
//                    // if (!hasVBO) {
//                    //     // Disable caching for this node
//                    //     SoGLCacheContextElement::shouldAutoCache(state, SoGLCacheContextElement::DONT_AUTO_CACHE);
//                    // }else
//                    //     SoGLCacheContextElement::setAutoCacheBits(state, SoGLCacheContextElement::DO_AUTO_CACHE);
//
//                    if (normalCacheUsed[0])
//                        this.readUnlockNormalCache();
//                } finally {
//                    tb.destructor();
//                }
//            }
//
////            if (pushed) {
////                SbBool notify = enableNotify(FALSE);
////                materialIndex.setNum(0);
////                if (notify) enableNotify(notify);
////                state.pop();
////            } else if (action.isRenderingDelayedPaths()) {
////                renderSelection(action, ctx);
////                renderHighlight(action, ctx);
////            }
//        } finally {
//            mb.destructor();
//        }
//    }

    void renderShape(SoGLRenderAction action,
                                    boolean hasVBO,
                                SoGLCoordinateElement vertexlist,
                                IntArrayPtr vertexindices,
                                    int num_indices,
                                IntArray partindices,
                                    int num_partindices,
                                SbVec3fArray normalsC,
                                IntArrayPtr normalindicesC,
                                    SoMaterialBundle materials,
                                IntArrayPtr matindicesC,
                                    SoTextureCoordinateBundle texcoords,
                                IntArrayPtr texindicesC,
                                Binding nbind,
                                Binding mbind,
                                    int texture)
    {
        MutableIntArray matindices = MutableIntArray.from(matindicesC); // java port
        MutableIntArray texindices = MutableIntArray.from(texindicesC); // java port
        MutableIntArray normalindices = MutableIntArray.from(normalindicesC); // java port
        MutableSbVec3fArray normals = MutableSbVec3fArray.from(normalsC); // java port

        final float[] dummy = new float[3]; // java port

        // Can we use vertex buffer objects?
        if (hasVBO) {
            int nbinding = nbind.ordinal();
            SoState state = action.getState();
            if (SoLazyElement.getLightModel(state) == SoLazyElement.LightModel.BASE_COLOR.getValue()) {
                // if no shading is set then the normals are all equal
                nbinding = (Binding.NONE_OVERALL.ordinal());
            }
            pimpl.render(action, vertexlist, vertexindices, num_indices, partindices, num_partindices, normalsC,
                    normalindicesC, materials, matindicesC, texcoords, texindicesC, nbinding, mbind.ordinal(), texture != 0);
            return;
        }

        int texidx = 0;

    SbVec3fArray coords3d = null;
        coords3d = vertexlist.getArrayPtr3();

        MutableIntArray viptr = MutableIntArray.from(vertexindices);
    MutableIntArray viendptr = viptr.plus(num_indices);
    MutableIntArray piptr = MutableIntArray.from(partindices);
    MutableIntArray piendptr = piptr.plus(num_partindices);
        int v1, v2, v3, v4, pi;
        final SbVec3fSingle dummynormal = new SbVec3fSingle(0,0,1);
        int numverts = vertexlist.getNum();

        MutableSbVec3fArray currnormal = new MutableSbVec3fArray(dummynormal);
        if (normals != null) currnormal = new MutableSbVec3fArray(normals);

        int matnr = 0;
        int trinr = 0;

        // Legacy code without VBO support
        pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
        while (pi == 0) {
            // It may happen that a part has no triangles
            pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
            if (mbind == Binding.PER_PART)
                matnr++;
            else if (mbind == Binding.PER_PART_INDEXED)
                matindices.plusPlus();
        }

        GL2 gl2 = action.getState().getGL2();

        gl2.glBegin(GL2.GL_TRIANGLES);
        while (viptr.plusLessThan(2, viendptr)) {
            v1 = viptr.getPlusPlus();
            v2 = viptr.getPlusPlus();
            v3 = viptr.getPlusPlus();
            if (v1 < 0 || v2 < 0 || v3 < 0 ||
                    v1 >= numverts || v2 >= numverts || v3 >= numverts) {
                break;
            }
            v4 = viptr.lessThan(viendptr) ? viptr.getPlusPlus() : -1;
            //(void)v4;
            /* vertex 1 *********************************************************/
            if (mbind == Binding.PER_PART) {
                if (trinr == 0)
                    materials.send(matnr++, true);
            }
            else if (mbind == Binding.PER_PART_INDEXED) {
                if (trinr == 0)
                    materials.send(matindices.getPlusPlus(), true);
            }
            else if (mbind == Binding.PER_VERTEX || mbind == Binding.PER_FACE) {
                materials.send(matnr++, true);
            }
            else if (mbind == Binding.PER_VERTEX_INDEXED || mbind == Binding.PER_FACE_INDEXED) {
                materials.send(matindices.getPlusPlus(), true);
            }

            if (normals != null) {
                if (nbind == Binding.PER_VERTEX || nbind == Binding.PER_FACE) {
                    currnormal.assign( normals); normals.plusPlus();
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
                else if (nbind == Binding.PER_VERTEX_INDEXED || nbind == Binding.PER_FACE_INDEXED) {
                    currnormal.assign(normals,normalindices.getPlusPlus());
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
            }

            if (texture != 0) {
                texcoords.send(texindices!=null ? texindices.getPlusPlus() : texidx++,
                        vertexlist.get3(v1),
                        currnormal.get(0));
            }
            gl2.glVertex3fv(coords3d.get3Floats(v1,dummy));

            /* vertex 2 *********************************************************/
            if (mbind == Binding.PER_VERTEX)
                materials.send(matnr++, true);
            else if (mbind == Binding.PER_VERTEX_INDEXED)
                materials.send(matindices.getPlusPlus(), true);

            if (normals!=null) {
                if (nbind == Binding.PER_VERTEX) {
                    currnormal.assign(normals); normals.plusPlus();
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
                else if (nbind == Binding.PER_VERTEX_INDEXED) {
                    currnormal.assign(normals,normalindices.getPlusPlus());
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
            }

            if (texture!=0) {
                texcoords.send(texindices!=null ? texindices.getPlusPlus() : texidx++,
                        vertexlist.get3(v2),
                            currnormal.get(0));
            }

            gl2.glVertex3fv(coords3d.get3Floats(v2,dummy));

            /* vertex 3 *********************************************************/
            if (mbind == Binding.PER_VERTEX)
                materials.send(matnr++, true);
            else if (mbind == Binding.PER_VERTEX_INDEXED)
                materials.send(matindices.getPlusPlus(), true);

            if (normals != null) {
                if (nbind == Binding.PER_VERTEX) {
                    currnormal.assign(normals); normals.plusPlus();
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
                else if (nbind == Binding.PER_VERTEX_INDEXED) {
                    currnormal.assign(normals,normalindices.getPlusPlus());
                    gl2.glNormal3fv(currnormal.get3Floats(dummy));
                }
            }

            if (texture!=0) {
                texcoords.send(texindices!=null ? texindices.getPlusPlus() : texidx++,
                        vertexlist.get3(v3),
                            currnormal.get(0));
            }

            gl2.glVertex3fv( coords3d.get3Floats(v3,dummy));

            if (mbind == Binding.PER_VERTEX_INDEXED)
                matindices.plusPlus();

            if (nbind == Binding.PER_VERTEX_INDEXED)
                normalindices.plusPlus();

            if (texture!=0 && texindices!=null) {
                texindices.plusPlus();
            }

            trinr++;
            if (pi == trinr) {
                pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                while (pi == 0) {
                    // It may happen that a part has no triangles
                    pi = piptr.lessThan(piendptr) ? piptr.getPlusPlus() : -1;
                    if (mbind == Binding.PER_PART)
                        matnr++;
                    else if (mbind == Binding.PER_PART_INDEXED)
                        matindices.plusPlus();
                }
                trinr = 0;
            }
        }
        gl2.glEnd();
    }

    public Binding
    findMaterialBinding2(SoState state)
    {
        Binding binding = Binding.NONE_OVERALL;
        SoMaterialBindingElement.Binding matbind =
            SoMaterialBindingElement.get(state);

        switch (matbind) {
            case OVERALL:
                binding = Binding.NONE_OVERALL;
                break;
            case PER_VERTEX:
                binding = Binding.PER_VERTEX;
                break;
            case PER_VERTEX_INDEXED:
                binding = Binding.PER_VERTEX_INDEXED;
                break;
            case PER_PART:
                binding = Binding.PER_PART;
                break;
            case PER_FACE:
                binding = Binding.PER_FACE;
                break;
            case PER_PART_INDEXED:
                binding = Binding.PER_PART_INDEXED;
                break;
            case PER_FACE_INDEXED:
                binding = Binding.PER_FACE_INDEXED;
                break;
            default:
                break;
        }
        return binding;
    }

    Binding
    findNormalBinding2(SoState state)
    {
        Binding binding = Binding.PER_VERTEX_INDEXED;
        SoNormalBindingElement.Binding normbind =
            (SoNormalBindingElement.Binding) SoNormalBindingElement.get(state);

        switch (normbind) {
            case OVERALL:
                binding = Binding.NONE_OVERALL;
                break;
            case PER_VERTEX:
                binding = Binding.PER_VERTEX;
                break;
            case PER_VERTEX_INDEXED:
                binding = Binding.PER_VERTEX_INDEXED;
                break;
            case PER_PART:
                binding = Binding.PER_PART;
                break;
            case PER_FACE:
                binding = Binding.PER_FACE;
                break;
            case PER_PART_INDEXED:
                binding = Binding.PER_PART_INDEXED;
                break;
            case PER_FACE_INDEXED:
                binding = Binding.PER_FACE_INDEXED;
                break;
            default:
                break;
        }
        return binding;
    }

//    public void GLRender(SoGLRenderAction action) {
//        GL2 gl2 = action.getState().getGL2();
//        SoState state = action.getState();
//        state.push();
//
//        SoMaterialBindingElement.set(state,SoMaterialBindingElement.Binding.PER_VERTEX);
//        super.GLRender(action);
//        state.pop();
//    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoBrepFaceSet class.
//
// Use: internal

    public static void initClass()
//
////////////////////////////////////////////////////////////////////////
    {
        SoSubNode.SO__NODE_INIT_CLASS(SoBrepFaceSet.class, "BrepFaceSet", SoIndexedFaceSet.class);
    }

}
