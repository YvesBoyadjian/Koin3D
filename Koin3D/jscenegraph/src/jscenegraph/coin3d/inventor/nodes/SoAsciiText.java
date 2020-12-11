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
  \class SoAsciiText SoAsciiText.h Inventor/nodes/SoAsciiText.h
  \brief The SoAsciiText class renders flat 3D text.

  \ingroup nodes

  The text is rendered using 3D polygon geometry.

  The size of the textual geometry representation is decided from the
  SoFont::size field of a preceding SoFont-node in the scene graph,
  which specifies the size in unit coordinates. This value sets the
  approximate vertical size of the letters.  The default value if no
  SoFont-nodes are used, is 10.

  The complexity of the glyphs is controlled by a preceding
  SoComplexity node with \e Type set to OBJECT_SPACE. Please note
  that the default built-in 3D font will not be affected by the
  SoComplexity node.

  This node is different from the SoText2 node in that it rotates,
  scales, translates etc just like other geometry in the scene. It is
  different from the SoText3 node in that it renders the text "flat",
  i.e. does not extrude the fonts to have depth.

  To get an intuitive feeling for how SoAsciiText works, take a look
  at this sample Inventor file in examiner viewer:

  \verbatim
  #Inventor V2.1 ascii

  Separator {
    Font {
      size 10
      name "Arial:Bold Italic"
    }

    BaseColor {
      rgb 1 0 0 #red
    }
    AsciiText {
      width [ 0, 1, 50 ]
      justification LEFT #Standard alignment
      string [ "LEFT", "LEFT", "LEFT", "LEFT", "LEFT LEFT" ]
    }
    BaseColor {
      rgb 1 1 0
    }
    Sphere { radius 1.5 }

    Translation {
      translation 0 -50 0
    }
    BaseColor {
      rgb 0 1 0 #green
    }
    AsciiText {
      width [ 0, 1, 50 ]
      justification RIGHT
      string [ "RIGHT", "RIGHT", "RIGHT", "RIGHT", "RIGHT RIGHT" ]
    }
    BaseColor {
      rgb 0 1 1
    }
    Sphere { radius 1.5 }

    Translation {
      translation 0 -50 0
    }
    BaseColor {
      rgb 0 0 1 #blue
    }
    AsciiText {
      width [ 0, 1, 50 ]
      justification CENTER
      string [ "CENTER", "CENTER", "CENTER", "CENTER", "CENTER CENTER" ]
    }
    BaseColor {
      rgb 1 0 1
    }
    Sphere { radius 1.5 }
  }
  \endverbatim

  In examinerviewer the Inventor file looks something like this:

  <center>
  \image html asciitext.png "Rendering of Example Scenegraph"
  </center>

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    AsciiText {
        string ""
        spacing 1
        justification LEFT
        width 0
    }
  \endcode

  \sa SoFont, SoFontStyle, SoText2, SoText3
  \since SGI Inventor 2.1
*/

// FIXME: Write doc about how text is textured. jornskaa 20040716

// *************************************************************************

package jscenegraph.coin3d.inventor.nodes;

import com.jogamp.opengl.GL2;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.database.inventor.*;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.fields.*;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.*;
import org.lwjgl.system.CallbackI;
import org.lwjglx.util.glu.GLUtessellator;
import com.jogamp.opengl.glu.GLU;
import org.lwjglx.util.glu.GLUtessellatorCallback;
import org.lwjglx.util.glu.GLUtessellatorCallbackAdapter;

import java.nio.FloatBuffer;


// *************************************************************************

/*!
  \enum SoAsciiText::Justification
  Used to specify horizontal string alignment.
*/
/*!
  \var SoAsciiText::Justification SoAsciiText::LEFT
  Left edges of strings are aligned.
*/
/*!
  \var SoAsciiText::Justification SoAsciiText::RIGHT
  Right edges of strings are aligned.
*/
/*!
  \var SoAsciiText::Justification SoAsciiText::CENTER
  Centers of strings are aligned.
*/

/*!  \var SoMFString SoAsciiText::string

  The set of strings to render.  Each string in the multiple value
  field will be rendered on a separate line.

  The default value of the field is a single empty string.
*/
/*!
  \var SoSFFloat SoAsciiText::spacing

  Vertical spacing between the baselines of two consecutive horizontal lines.
  Default value is 1.0, which means that it is equal to the vertical size of
  the highest character in the bitmap alphabet.
*/
/*!
  \var SoSFEnum SoAsciiText::justification

  Determines horizontal alignment of text strings.

  If justification is set to SoAsciiText::LEFT, the left edge of the first string
  is at the origin and all strings are aligned with their left edges.
  If set to SoAsciiText::RIGHT, the right edge of the first string is
  at the origin and all strings are aligned with their right edges. Otherwise,
  if set to SoAsciiText::CENTER, the center of the first string is at the
  origin and all strings are aligned with their centers.
  The origin is always located at the baseline of the first line of text.

  Default value is SoAsciiText::LEFT.
*/

/*!  \var SoMFFloat SoAsciiText::width
  Defines the width of each line.  The text is scaled to be within the
  specified units.  The size of the characters will remain the same;
  only the X-positions are scaled.  When width <= 0, the width
  value is ignored and the text rendered as normal.  The exact width of
  the rendered text depends not only on the width field, but also on
  the maximum character width in the rendered string.  The string will
  be attempted to fit within the specified width, but if it is unable
  to do so, it uses the largest character in the string as the
  width.  If fewer widths are specified than the number of strings, the
  strings without matching widths are rendered with default width.
*/

// *************************************************************************

public class SoAsciiText extends SoShape {

    private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoAsciiText.class,this);

    public
    static SoType getClassTypeId()        /* Returns class type id */
    { return SoSubNode.getClassTypeId(SoAsciiText.class);  }
    public  SoType      getTypeId()      /* Returns type id      */
    {
        return nodeHeader.getClassTypeId();
    }
    public SoFieldData getFieldData()  {
        return nodeHeader.getFieldData();
    }
    public  static SoFieldData[] getFieldDataPtr()
    { return SoSubNode.getFieldDataPtr(SoAsciiText.class); }


    public enum Justification {
        LEFT(1),
        RIGHT(2),
        CENTER(3);

        private int value;

        Justification( int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };

    public final SoMFString string = new SoMFString();
    public final SoSFFloat spacing = new SoSFFloat();
    public final SoSFEnum justification = new SoSFEnum();
    public final SoMFFloat width = new SoMFFloat();

    private SoAsciiTextP pimpl;

    //! Private data:
    //! MyOutlineFontCache is an internal, opaque class used to
    //! maintain gl display lists and other information for each
    //! character in a font.
    MyOutlineFontCache myFont; //ptr

    //! All this stuff is used while generating primitives:
    static SoAsciiText currentGeneratingNode; //ptr
    static SoPrimitiveVertex[] genPrimVerts = new SoPrimitiveVertex[3]; //ptr
    static final SbVec3fSingle genTranslate = new SbVec3fSingle();
    static int genWhichVertex;
    static int genPrimType;
    static SoAction genAction; //ptr
    static boolean genBack;
    static boolean genTexCoord;
    static SoMultiTextureCoordinateElement tce; //ptr

/*!
  Constructor.
*/
    public SoAsciiText()
    {
        nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoAsciiText.class);

        nodeHeader.SO_NODE_ADD_MFIELD(string,"string", (""));
        nodeHeader.SO_NODE_ADD_FIELD(spacing,"spacing", (1.0f));
        nodeHeader.SO_NODE_ADD_FIELD(justification,"justification", (SoAsciiText.Justification.LEFT));
        nodeHeader.SO_NODE_ADD_MFIELD(width,"width", (0.0f));

        nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.LEFT);
        nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.RIGHT);
        nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.CENTER);
        nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(justification,"justification", "Justification");

        pimpl = new SoAsciiTextP(this);
        pimpl.cache = null;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a Text3.
//
// Use: extender

    static GLUtessellator/*gluTESSELATOR*/ tobj = null; //ptr

    public void
    GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
    {

        // First see if the object is visible and should be rendered now
        if (! shouldGLRender(action))
            return;

        SoState state = action.getState();

        if (!setupFontCache(state, true))
            return;

        SoMaterialBindingElement.Binding mbe =
            SoMaterialBindingElement.get(state);
        boolean materialPerPart =
                (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
                        mbe == SoMaterialBindingElement.Binding.PER_PART);

        final SoMaterialBundle    mb = new SoMaterialBundle(action);
        if (!materialPerPart) {
            // Make sure the fist current material is sent to GL
            mb.sendFirst();
        }

        final GL2 gl2 = action.getState().getGL2();

        if (tobj == null) {
            tobj = /*(gluTESSELATOR )*/GLU.gluNewTess();
            GLUtessellatorCallback cb = new GLUtessellatorCallbackAdapter() {

                public void begin(int val) {
                    gl2.glBegin(val);
                }

                public void end() {
                    gl2.glEnd();
                }

                public void vertex(Object object) {
                    if(object instanceof FloatBuffer) {
                        gl2.glVertex2fv((FloatBuffer)object);
                    }
                    else if(object instanceof SbVec2f) {
                        SbVec2f vec = (SbVec2f)object;
                        gl2.glVertex2fv(vec.getValueRead(), 0);
                    }
                    else {
                        gl2.glVertex2fv((float[])object, 0);
                    }
                }

                public void error(int val) {
                    MyOutlineFontCache.errorCB(val);
                }
            };
            GLU.gluTessCallback(tobj, (int)GLU.GLU_BEGIN, cb/*(OPENGL_CALLBACKFUNC)glBegin*/);
            GLU.gluTessCallback(tobj, (int)GLU.GLU_END, cb/*(OPENGL_CALLBACKFUNC)glEnd*/);
            GLU.gluTessCallback(tobj, (int)GLU.GLU_VERTEX, cb/*(OPENGL_CALLBACKFUNC)glVertex2fv*/);
            GLU.gluTessCallback(tobj, (int)GLU.GLU_ERROR,
                    cb/*(OPENGL_CALLBACKFUNC)MyOutlineFontCache::errorCB*/);
        }

        // See if texturing is enabled
        genTexCoord = SoGLMultiTextureEnabledElement.get(action.getState(),0); // YB port to Koin3D

        if (materialPerPart) mb.sendFirst();

        gl2.glNormal3f(0, 0, 1);

        myFont.setupToRenderFront(state);

        if (genTexCoord) {
            gl2.glPushAttrib(GL2.GL_TEXTURE_BIT);
            gl2.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            gl2.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            float[] params = new float[4];
            params[0] = 1.0f/myFont.getHeight();
            params[1] = params[2] = params[3] = 0.0f;
            gl2.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, params);
            params[1] = params[0];
            params[0] = 0.0f;
            gl2.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, params);

            gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
            gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
        }

        for (int line = 0; line < string.getNum(); line++) {
            gl2.glPushMatrix();
            float w = (line < width.getNum()) ? width.getValueAt(line) : 0;
            SbVec2fSingle p = new SbVec2fSingle(getStringOffset(line, w));
            if (p.getValue()[0] != 0.0 || p.getValue()[1] != 0.0)
                gl2.glTranslatef(p.getValue()[0], p.getValue()[1], 0.0f);
            renderFront(action, string.getValueAt(line), w, tobj);
            gl2.glPopMatrix();
        }

        if (genTexCoord) {
            gl2.glPopAttrib();
        }
        mb.destructor();
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figure out how much to offset the given line of text.  The X
//    offset depends on the justification and the width of the string
//    in the current font.  The Y offset depends on the line spacing
//    and font height.
//
// Use: private

    public SbVec2f
    getStringOffset(int line, float width)
//
////////////////////////////////////////////////////////////////////////
    {
        final SbVec2fSingle result = new SbVec2fSingle(0,0);

        if (justification.getValue() == SoAsciiText.Justification.RIGHT.getValue()) {
            if (width <= 0)
                width = myFont.getWidth(string.getValueAt(line));
            result.getValue()[0] = -width;
        }
        if (justification.getValue() == SoAsciiText.Justification.CENTER.getValue()) {
            if (width <= 0)
                width = myFont.getWidth(string.getValueAt(line));
            result.getValue()[0] = -width/2.0f;
        }
        result.getValue()[1] = -line*myFont.getHeight()*spacing.getValue();

        return result;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Render the fronts of the given string.  The GL transformation
//    matrix is munged by this routine-- surround it by
//    PushMatrix/PopMatrix.
//
// Use: public, internal

    public void
    renderFront(SoGLRenderAction action, final String string,
                             float width, /*gluTESSELATOR*/GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
    {
        String chars = string/*.getString()*/;

        // if we have a fixed width, use it.
        float off = 0;
        if (width > 0) {
            float naturalWidth = myFont.getWidth(string);
            off = (width - naturalWidth) / (string.length() - 1);
        }

        // First, try to figure out if we can use glCallLists:
        boolean useCallLists = true;

        for (int i = 0; i < string.length(); i++) {
            // See if the font cache already has (or can build) a display
            // list for this character:
            if (!myFont.hasFrontDisplayList(chars.charAt(i), tobj)) {
                useCallLists = false;
                break;
            }
        }

        // if we have display lists for all of the characters, use
        // glCallLists:
        if (useCallLists && off == 0) {
            myFont.callFrontLists(string, off);
        }
        // if we don't, draw the string character-by-character, using the
        // display lists we do have:
        else {
            myFont.renderFront(string, off, tobj);
        }
    }

    /*!
      \copybrief SoBase::initClass(void)
    */
    public static void initClass()
    {
        //SO_NODE_INTERNAL_INIT_CLASS(SoAsciiText, SO_FROM_INVENTOR_2_1|SoNode::VRML1);
        SoSubNode.SO__NODE_INIT_CLASS(SoAsciiText.class, "AsciiText", SoShape.class);
    }

    @Override
    public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {

    }

    @Override
    protected void generatePrimitives(SoAction action) {

    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Setup internal font cache.  Called by all action methods before
//    doing their thing.  GLRender passes TRUE to do special rendering
//    setup.  Returns FALSE if there are problems and the action
//    should bail.
//
// Use: private

    public boolean
    setupFontCache(SoState state, boolean forRender)
//
////////////////////////////////////////////////////////////////////////
    {
        // The state must be pushed here because myFont->isRenderValid
        // depends on the state being the correct depth (it must be the
        // same depth as when the font cache was built).
        state.push();

        if (myFont != null) {
            boolean isValid;
            if (forRender)
                isValid = myFont.isRenderValid(state);
            else
                isValid = myFont.isValid(state);

            if (!isValid) {
                myFont.unref(state);
                myFont = null;
            }
        }
        if (myFont == null) {
            myFont = MyOutlineFontCache.getFont(state, forRender);
        }
        state.pop();
        return  myFont != null;
    }
}
