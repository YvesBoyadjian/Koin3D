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
 |      This file defines the SoGLCacheContextElement class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.caches.SoGLRenderCache;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;

/////////////////////////////////////////////////////////////////////////
///
///  Class SoGLDisplayList:
///
///  A helper class used to store OpenGL display-list-like objects.
///  Currently, it can store either texture objects (which must be
///  treated like display lists; texture objects bound inside a display
///  list must be reference counted, etc) and display lists.
///
////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLDisplayList implements Destroyable {

	private   Type type;
    private final int[] startIndex = new int[1];
    private int num;
    private int refCount;
    private int context;
    
    private boolean mipmap; //COIN 3D

    int texturetarget; // COIN 3D

   public enum Type {
        DISPLAY_LIST,
        TEXTURE_OBJECT,
        VERTEX_BUFFER_OBJECT //!< create a Vertex Buffer Object (MeVis ONLY)
        };
        
        public SoGLDisplayList(SoState state, Type type) {
        	this(state, type, 1, false);
        }
        public SoGLDisplayList(SoState state, Type type, int numToAllocate) {
        	this(state, type, numToAllocate, false);
        }
    public SoGLDisplayList(SoState state, Type _type, int numToAllocate, boolean mimaptexobj) {
    	    refCount = 0;

    num = numToAllocate;
    
    mipmap = mimaptexobj;

    // We must depend on the GL cache context; we can't assume that a
    // cache is valid between any two render actions, since the render
    // actions could be directed at different X servers on different
    // machines (with different ideas about which display lists have
    // been created).
    context = SoGLCacheContextElement.get(state);

    type = _type;
    
    GL2 gl2 = Ctx.get(context);

    if (type == Type.TEXTURE_OBJECT) {
        glGenTextures(/*1,*/ startIndex); //glGenTextures(1, &startIndex);
//#ifdef DEBUG
        if (num != 1)
            SoDebugError.post("SoGLDisplayList", "Sorry, can only "+
                               "construct 1 texture object at a time");
//#endif
    } else if (type == Type.VERTEX_BUFFER_OBJECT) {
        glGenBuffersARB(/*1,*/ startIndex);//glGenBuffersARB(1, &startIndex);
    } else {
        startIndex[0] = gl2.glGenLists(num);//glGenLists(num);
    }
    	
    }
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    GL2 gl2 = Ctx.get(context);

    if (type == Type.TEXTURE_OBJECT) {
        gl2.glDeleteTextures( startIndex); //glDeleteTextures(1, &startIndex);
    } else if (type == Type.VERTEX_BUFFER_OBJECT) {
        gl2.glDeleteBuffersARB(/*1,*/ startIndex);//glDeleteBuffersARB(1, &startIndex);
    } else {
        gl2.glDeleteLists(startIndex[0], num);//glDeleteLists(startIndex, num);
    }
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

public void
ref()
//
////////////////////////////////////////////////////////////////////////
{
    ++refCount;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

public void
unref() {
	unref(null);
}
public void
unref(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    --refCount;
    if (refCount <= 0) {
        // Let the CacheContextElement delete us:
        SoGLCacheContextElement.freeList(state, this);
//        // Let SoGLCacheContext delete this instance the next time context is current.
//        SoGLCacheContextElement.scheduleDelete(state, this);
    }
}

        //!
    //! Get methods
    //!
public    Type getType() { return type; }
public    int getNumAllocated() { return num; }
public    int getFirstIndex() { return startIndex[0]; }
public    int getContext() { return context; }

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

    //! Open/close a display list.  Display lists are done in
    //! COMPILE_AND_EXECUTE mode, so you don't need to call() the
    //! display list after close().
    //! Opening a texture object binds it; closing it does nothing.
    // java port
    public void open(SoState state) {
    	open(state,0);
    }
    
public void
open(SoState state, int index)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(context);
	
    if (type == SoGLDisplayList.Type.TEXTURE_OBJECT) {
        gl2.glBindTexture(GL2.GL_TEXTURE_2D, startIndex[0]+index);
    } else if (type == SoGLDisplayList.Type.VERTEX_BUFFER_OBJECT) {
        // not implemented here
    } else {
        gl2.glNewList(startIndex[0]+index, GL2.GL_COMPILE_AND_EXECUTE);
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

public void
close(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(context);
	
    if (type == SoGLDisplayList.Type.DISPLAY_LIST) {
        gl2.glEndList();
    }
    else {
        cc_glglue glw = SoGL.cc_glglue_instance(this.context);
        //assert(SoGL.cc_glglue_has_texture_objects(glw));
        int target = this.texturetarget;
        if (target == 0) {
          // target is not set. Assume normal 2D texture.
          target = GL_TEXTURE_2D;
        }
        // unbind current texture object
        SoGL.cc_glglue_glBindTexture(glw, target, 0);
      }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

//!
//! Call a display list, or bind a texture object.  This
//! automatically sets up a dependency if there is another display
//! list open in the state.
//! You can also use the get() methods below and make the OpenGL
//! calls yourself, in which case you should call the
//! addDependency() method to do the correct reference counting...
public void call(SoState state) {
	call(state, 0);
}
public void
call(SoState state, int index)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(context);
	
    if (type == SoGLDisplayList.Type.TEXTURE_OBJECT) {
        gl2.glBindTexture(GL2.GL_TEXTURE_2D, startIndex[0]+index);
    } else if (type == SoGLDisplayList.Type.VERTEX_BUFFER_OBJECT) {
        // not implemented here
    } else {
        gl2.glCallList(startIndex[0]+index);
    }
    addDependency(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//
//
// Use: public

public void
addDependency(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    if (state.isCacheOpen()) {
        SoGLRenderCache c = (SoGLRenderCache )
            SoCacheElement.getCurrentCache(state);
        c.addNestedCache(this);
    }
}


/*! COIN 3D
  Returns whether the texture object stored in this instance
  was created with mipmap data. This method is an extension
  versus the Open Inventor API.
*/
public boolean isMipMapTextureObject() {
	return this.mipmap;
}


/*!
Sets the texture object target
\since Coin 2.5
*/
public void
setTextureTarget(int target)
{
	this.texturetarget = target;
}

/*!
Returns the texture target
\since Coin 2.5
*/
public int getTextureTarget()
{
if (this.texturetarget != 0)
  return (int) this.texturetarget;
return GL_TEXTURE_2D;
}


}
