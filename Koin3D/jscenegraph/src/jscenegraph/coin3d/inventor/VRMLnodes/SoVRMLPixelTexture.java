/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 \**************************************************************************/


/*!
  \class SoVRMLPixelTexture SoVRMLPixelTexture.h Inventor/VRMLnodes/SoVRMLPixelTexture.h
  \brief The SoVRMLPixelTexture class is used for mapping a texture image onto geometry..

  \ingroup VRMLnodes

  \WEB3DCOPYRIGHT

  \verbatim
  PixelTexture {
    exposedField SFImage  image      0 0 0    # see SoSFImage
    field        SFBool   repeatS    TRUE
    field        SFBool   repeatT    TRUE
  }
  \endverbatim

  The PixelTexture node defines a 2D image-based texture map as an
  explicit array of pixel values (image field) and parameters
  controlling tiling repetition of the texture onto geometry.  Texture
  maps are defined in a 2D coordinate system (s, t) that ranges from
  0.0 to 1.0 in both directions. The bottom edge of the pixel image
  corresponds to the S-axis of the texture map, and left edge of the
  pixel image corresponds to the T-axis of the texture map. The
  lower-left pixel of the pixel image corresponds to s=0.0, t=0.0, and
  the top-right pixel of the image corresponds to s = 1.0, t = 1.0.
  See 4.6.11, Texture maps
  (<http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.6.11>),
  for a general description of texture
  maps. Figure 6.13 depicts an example PixelTexture.

  <center>
  <img src="http://www.web3d.org/documents/specifications/14772/V2.0/Images/PixelTexture.gif">
  Figure 6.13 -- PixelTexture node
  </center>

  See 4.14, Lighting model
  (<http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.14>),
  for a description of how the texture values interact with the
  appearance of the geometry.  SoSFImage, describes the
  specification of an image.  The repeatS and repeatT fields specify
  how the texture wraps in the S and T directions. If repeatS is TRUE
  (the default), the texture map is repeated outside the 0-to-1
  texture coordinate range in the S direction so that it fills the
  shape. If repeatS is FALSE, the texture coordinates are clamped in
  the S direction to lie within the 0.0 to 1.0 range. The repeatT
  field is analogous to the repeatS field.

*/

        package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.elements.*;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFImage;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

public class SoVRMLPixelTexture extends SoVRMLTexture {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLPixelTexture.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoVRMLPixelTexture.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoVRMLPixelTexture.class); }

    public final SoSFImage image = new SoSFImage();

    private
    SoVRMLPixelTextureP pimpl; //ptr

/*!
  Constructor.
*/
    public SoVRMLPixelTexture()
    {
        pimpl = new SoVRMLPixelTextureP();

        nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLPixelTexture.class);

        nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(image,"image", new SbImage(null,new SbVec2s((short)0,(short)0), 0));

        pimpl.glimage = null;
        pimpl.glimagevalid = false;
        pimpl.readstatus = 1;
    }

/*!
  Destructor.
*/
    public void destructor()
    {
        if (pimpl.glimage != null) pimpl.glimage.unref(null);
        Destroyable.delete(pimpl);
        super.destructor();
    }


    static SoGLImage.Wrap
    pixeltexture_translate_wrap(final boolean repeat)
    {
        if (repeat) return SoGLImage.Wrap.REPEAT;
        return SoGLImage.Wrap.CLAMP_TO_EDGE;
    }

// Doc in parent
    public void doAction(SoAction action)
    {
        SoVRMLPixelTexture_doAction(action);
    }
    public void
    SoVRMLPixelTexture_doAction(SoAction action)
    {
        SoState state = action.getState();
        int unit = SoTextureUnitElement.get(state);

        if (SoTextureOverrideElement.getImageOverride(state) && (unit == 0))
        return;

        final int[] nc = new int[1];
        final SbVec2s size = new SbVec2s();
   final MemoryBuffer bytes = this.image.getValue(size, nc);

        if (size.operator_equal_equal(new SbVec2s((short)0, (short)0))) {
            SoMultiTextureEnabledElement.set(state, this, unit, false);
        }
        else {
            SoMultiTextureImageElement.set(state, this, unit,
                                            size, nc[0], bytes,
                                    (this.repeatS.getValue() ?
                    SoMultiTextureImageElement.Wrap.REPEAT :
                    SoMultiTextureImageElement.Wrap.CLAMP_TO_BORDER),
            (this.repeatT.getValue() ?
                    SoMultiTextureImageElement.Wrap.REPEAT :
                    SoMultiTextureImageElement.Wrap.CLAMP_TO_BORDER),
            SoMultiTextureImageElement.Model.MODULATE,
                    new SbColor(1.0f, 1.0f, 1.0f));
            SoMultiTextureEnabledElement.set(state, this, unit, true);
        }
        if (this.isOverride() && (unit == 0)) {
        SoTextureOverrideElement.setImageOverride(state, true);
    }
    }

    public void rayPick(SoRayPickAction action)
    {
        SoVRMLPixelTexture_doAction(action);
    }

// Doc in parent
    public void GLRender(SoGLRenderAction action)
    {
        SoState state = action.getState();
        int unit = SoTextureUnitElement.get(state);

        if (SoTextureOverrideElement.getImageOverride(state) && (unit == 0))
        return;

        float quality = SoTextureQualityElement.get(state);

        pimpl.lock_glimage();

        if (!pimpl.glimagevalid) {
        final int[] nc = new int[1];
        final SbVec2s size = new SbVec2s();
    final MemoryBuffer bytes =
                this.image.getValue(size, nc);
        SoTextureScalePolicyElement.Policy scalepolicy =
                SoTextureScalePolicyElement.get(state);
        boolean needbig = (scalepolicy == SoTextureScalePolicyElement.Policy.FRACTURE);

        if (needbig &&
                (pimpl.glimage == null ||
                pimpl.glimage.getTypeId().operator_not_equal(SoGLBigImage.getClassTypeId()))) {
            if (pimpl.glimage != null) pimpl.glimage.unref(state);
            pimpl.glimage = new SoGLBigImage();
        }
    else if (!needbig &&
                (pimpl.glimage == null ||
                pimpl.glimage.getTypeId().operator_not_equal(SoGLImage.getClassTypeId()))) {
            if (pimpl.glimage != null) pimpl.glimage.unref(state);
            pimpl.glimage = new SoGLImage();
        }

        if (scalepolicy == SoTextureScalePolicyElement.Policy.SCALE_DOWN) {
            pimpl.glimage.setFlags(pimpl.glimage.getFlags()|SoGLImage.Flags.SCALE_DOWN.getValue());
        }

        if (bytes != null && size.operator_not_equal(new SbVec2s((short)0,(short)0))) {
            pimpl.glimage.setData(bytes, size, nc[0],
                    pixeltexture_translate_wrap(this.repeatS.getValue()),
            pixeltexture_translate_wrap(this.repeatT.getValue()),
            quality);
            pimpl.glimagevalid = true;
            // don't cache while creating a texture object
            SoCacheElement.setInvalid(true);
            if (state.isCacheOpen()) {
                SoCacheElement.invalidate(state);
            }
        }
    }

        pimpl.unlock_glimage();

        SoGLMultiTextureImageElement.set(state, this, unit,
                                          pimpl.glimagevalid ? pimpl.glimage : null,
            SoMultiTextureImageElement.Model.MODULATE,
            new SbColor(1.0f, 1.0f, 1.0f));

        SoGLMultiTextureEnabledElement.set(state,
                                      this, unit, pimpl.glimagevalid &&
            quality > 0.0f);

        if (this.isOverride() && (unit == 0)) {
        SoTextureOverrideElement.setImageOverride(state, true);
    }
    }

// doc in parent
    public void callback(SoCallbackAction action)
    {
        SoVRMLPixelTexture_doAction(action);
    }

// doc in parent
    public boolean readInstance(SoInput in,
                                short flags)
    {
        pimpl.glimagevalid = false;
        return super.readInstance(in, flags);
    }

/*!
  Overloaded to detect when fields change.
*/
    public void notify(SoNotList list)
    {
        pimpl.glimagevalid = false;
        super.notify(list);
    }

    /*!
  \copydetails SoNode::initClass(void)
*/
    public static void initClass()
    {
//        SO_NODE_INTERNAL_INIT_CLASS(SoVRMLPixelTexture.class, SO_VRML97_NODE_TYPE);
        SoSubNode.SO__NODE_INIT_CLASS(SoVRMLPixelTexture.class, "VRMLPixelTexture", SoVRMLTexture.class);

        SoType type = SoVRMLPixelTexture.getClassTypeId();
        SoRayPickAction.addMethod(type, SoNode::rayPickS);
    }
}
