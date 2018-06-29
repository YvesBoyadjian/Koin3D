
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.elements.gl.SoGLTexture3EnabledElement;
import jscenegraph.coin3d.inventor.fields.SoSFImage3;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLTextureImageElement;
import jscenegraph.database.inventor.elements.SoTextureImageElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFColor;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */

/*!
  \class SoTexture3 SoTexture3.h Inventor/nodes/SoTexture3.h
  \brief The SoTexture3 class is used to map a 3D texture onto geometry.
  \ingroup nodes

  Shape nodes within the scope of SoTexture3 nodes in the scenegraph
  (ie below the same SoSeparator and to the righthand side of the
  SoTexture3) will have the texture applied according to each shape
  type's individual characteristics.  See the documentation of the
  various shape types (SoFaceSet, SoCube, SoSphere, etc etc) for
  information about the specifics of how the textures will be applied.
  An SoTexture3 node will override any previous encountered SoTexture2 nodes
  and vice versa. Mixing of SoTexture3 and SoTextureCoordinate2 (or the other
  way around) is legal, but the third texture coordinate component will
  be ignored (set to 0.0).

  <center>
  <img src="http://doc.coin3d.org/images/SoGuiExamples/nodes/texture3.png">
  </center>

  \COIN_CLASS_EXTENSION

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    Texture3 {
        filenames ""
        images 0 0 0 0
        wrapR REPEAT
        wrapS REPEAT
        wrapT REPEAT
        model MODULATE
        blendColor 0 0 0
        enableCompressedTexture FALSE
    }
  \endcode

  \since Coin 2.0
  \since TGS Inventor 2.6
*/

// *************************************************************************
public class SoTexture3 extends SoTexture {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTexture3.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTexture3.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTexture3.class); }    	  	
	
  public enum Model {
    MODULATE ( SoTextureImageElement.Model.MODULATE.getValue()),
    DECAL ( SoTextureImageElement.Model.DECAL.getValue()),
    BLEND ( SoTextureImageElement.Model.BLEND.getValue());
    
    private int value;
    Model(int value) {
    	this.value = value;
    }
    public int getValue() {
    	return value;
    }
  };

  public enum Wrap {
    REPEAT( SoTextureImageElement.Wrap.REPEAT.getValue()),
    CLAMP( SoTextureImageElement.Wrap.CLAMP.getValue());
    
    private int value;
    Wrap(int value) {
    	this.value = value;
    }
    public int getValue() {
    	return value;
    }
	public static Wrap fromValue(int value2) {
		if(value2 == REPEAT.getValue()) return REPEAT;
		if(value2 == CLAMP.getValue()) return CLAMP;		
		return null;
	}
  };

  public final SoMFString filenames = new SoMFString();
  public final SoSFImage3 images = new SoSFImage3();
  public final SoSFEnum wrapR = new SoSFEnum();
  public final SoSFEnum wrapS = new SoSFEnum();
  public final SoSFEnum wrapT = new SoSFEnum();
  public final SoSFEnum model = new SoSFEnum();
  public final SoSFColor blendColor = new SoSFColor();
  public final SoSFBool enableCompressedTexture = new SoSFBool();

  private int readstatus;
  private SoGLImage glimage;
  private boolean glimagevalid;

  private SoFieldSensor filenamesensor;
  

/*!
  Constructor.
*/
public SoTexture3()
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTexture3.class*/);

	nodeHeader.SO_NODE_ADD_MFIELD(filenames,"filenames", (""));
  nodeHeader.SO_NODE_ADD_FIELD(images,"images", new SbImage(null, new SbVec3s((short)0, (short)0, (short)0), 0));
  nodeHeader.SO_NODE_ADD_FIELD(wrapR,"wrapR", (Wrap.REPEAT.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(wrapS,"wrapS", (Wrap.REPEAT.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(wrapT,"wrapT", (Wrap.REPEAT.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(model,"model", (Model.MODULATE.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(blendColor,"blendColor", new SbColor(0.0f, 0.0f, 0.0f));
  nodeHeader.SO_NODE_ADD_FIELD(enableCompressedTexture,"enableCompressedTexture", (false));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.REPEAT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Wrap.CLAMP);

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapS,"wrapS", "Wrap");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapT,"wrapT", "Wrap");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(wrapR,"wrapR", "Wrap");

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.MODULATE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.DECAL);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Model.BLEND);
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(model,"model", "Model");

  this.glimage = null;
  this.glimagevalid = false;
  this.readstatus = 1;

  // use field sensor for filename since we will load an image if
  // filename changes. This is a time-consuming task which should
  // not be done in notify().
  this.filenamesensor = new SoFieldSensor(SoTexture3::filenameSensorCB, this);
  this.filenamesensor.setPriority(0);
  this.filenamesensor.attach(this.filenames);
}

/*!
  Destructor.
*/
public void destructor()
{
  if (this.glimage != null) this.glimage.unref(null);
  this.filenamesensor.destructor();
  super.destructor();
}

// doc from parent
public static void
initClass()
{
  SoSubNode.SO__NODE_INIT_CLASS(SoTexture3.class,"Texture3",SoTexture.class); /*SO_FROM_INVENTOR_2_6|SO_FROM_COIN_2_0);*/

  SO_ENABLE(SoGLRenderAction.class, SoGLTextureImageElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoGLTexture3EnabledElement.class);

  SO_ENABLE(SoCallbackAction.class, SoTextureImageElement.class);
}


// Documented in superclass.
public boolean
readInstance(SoInput  in, short flags)
{
  // Overridden to check if texture files (if any) can be found and
  // loaded.

  this.filenamesensor.detach();
  boolean readOK = super.readInstance(in, flags);
  this.setReadStatus( readOK ? 1 : 0);
  if (readOK && !filenames.isDefault() && filenames.getNum()>0) {
    if (!this.loadFilenames(in)) {
      this.setReadStatus(false ? 1 : 0);
    }
  }
  this.filenamesensor.attach(this.filenames);
  return readOK;
}

private static SoGLImage.Wrap
translateWrap(final SoTexture3.Wrap wrap)
{
  if (wrap == SoTexture3.Wrap.REPEAT) return SoGLImage.Wrap.REPEAT;
  return SoGLImage.Wrap.CLAMP;
}

static boolean first = true;

// doc from parent
public void
GLRender(SoGLRenderAction  action)
{
  SoState state = action.getState();

  final cc_glglue glue = SoGL.cc_glglue_instance( SoGLCacheContextElement.get(state));
  
  if (!SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_3D_TEXTURES)) {
    if (first) {
      SoDebugError.postWarning("SoTexture3::GLRender",
                                "The current OpenGL context does not support 3D textures "+
                                "(This warning message is only shown once, but "+
                                "there could be more cases of this in the "+
                                "scene graph.).");
      first = false;
    }
    return;
  }


  if (SoTextureOverrideElement.getImageOverride(state))
    return;

  float quality = SoTextureQualityElement.get(state);
  if (!this.glimagevalid) {
    final int[] nc = new int[1];
    final SbVec3s size = new SbVec3s();
    final byte[] bytes = this.images.getValue(size, nc);
    //FIXME: 3D support in SoGLBigImage (kintel 20011113)
//      SbBool needbig =
//        SoTextureScalePolicyElement::get(state) ==
//        SoTextureScalePolicyElement::DONT_SCALE;

//      if (needbig &&
//          (this.glimage == NULL ||
//           this.glimage.getTypeId() != SoGLBigImage::getClassTypeId())) {
//        if (this.glimage) this.glimage.unref(state);
//        this.glimage = new SoGLBigImage();
//      }
//      else if (!needbig &&
//               (this.glimage == NULL ||
//                this.glimage.getTypeId() != SoGLImage::getClassTypeId())) {
//        if (this.glimage) this.glimage.unref(state);
//        this.glimage = new SoGLImage();
//      }
    if (this.glimage != null) this.glimage.unref(state);
    this.glimage = new SoGLImage();

    if (this.enableCompressedTexture.getValue()) {
      this.glimage.setFlags(this.glimage.getFlags()|
                              SoGLImage.Flags.COMPRESSED.getValue());
    }

    if (bytes != null && size.operator_not_equal(new SbVec3s((short)0,(short)0,(short)0))) {
      this.glimage.setData(bytes, size, nc[0],
                             translateWrap(SoTexture3.Wrap.fromValue(this.wrapS.getValue())),
                             translateWrap(SoTexture3.Wrap.fromValue(this.wrapT.getValue())),
                             translateWrap(SoTexture3.Wrap.fromValue(this.wrapR.getValue())),
                             quality);
      this.glimagevalid = true;
    }
  }

  SoGLTexture3EnabledElement.set(state,
                                  /*this,*/ this.glimagevalid &&
                                  quality > 0.0f);
  SoGLTextureImageElement.set(state, this,
                               this.glimagevalid ? this.glimage : null,
                               SoTextureImageElement.Model.fromValue( model.getValue()),
                               this.blendColor.getValue());


  if (this.isOverride()) {
    SoTextureOverrideElement.setImageOverride(state, true);
  }
}

// doc from parent
public void
SoTexture3_doAction(SoAction action)
{
  SoState state = action.getState();

  if (SoTextureOverrideElement.getImageOverride(state))
    return;

  final int[] nc = new int[1];
  final SbVec3s size = new SbVec3s();
  final byte[] bytes = this.images.getValue(size, nc);

  if (size.operator_not_equal(new SbVec3s((short)0,(short)0,(short)0))) {
    SoTextureImageElement.set(state, this,
                               size, nc[0], bytes,
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapT.getValue()),
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapS.getValue()),
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapR.getValue()),
                               SoTextureImageElement.Model.fromValue(model.getValue()),
                               this.blendColor.getValue());
  }
  // if a filename has been set, but the file has not been loaded, supply
  // a dummy texture image to make sure texture coordinates are generated.
  else if (this.images.isDefault() &&
           this.filenames.getNum()>0 &&
           this.filenames.operator_square_bracket(0).length() != 0) {
    /*static*/ final byte dummytex[] = {(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff,
    		(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
    SoTextureImageElement.set(state, this,
                               new SbVec3s((short)2,(short)2,(short)2), 1, dummytex,
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapT.getValue()),
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapS.getValue()),
                               SoTextureImageElement.Wrap.fromValue((int)this.wrapR.getValue()),
                               SoTextureImageElement.Model.fromValue(model.getValue()),
                               this.blendColor.getValue());
  }
  else {
    SoTextureImageElement.setDefault(state, this);
  }
  if (this.isOverride()) {
    SoTextureOverrideElement.setImageOverride(state, true);
  }
}

// doc from parent
public void
callback(SoCallbackAction  action)
{
  SoTexture3_doAction(action);
}

/*!
  Returns read status. 1 for success, 0 for failure.
*/
public int
getReadStatus()
{
  return this.readstatus;
}

/*!
  Sets read status.
  \sa getReadStatus()
 */
public void
setReadStatus(int s)
{
  this.readstatus = s;
}

// Documented in superclass.
public void
notify(SoNotList  l)
{
  // Overridden to detect when fields change.

  SoField f = l.getLastField();
  if (f == this.images) {
    this.glimagevalid = false;
    this.filenames.setDefault(true); // write image, not filename
  }
  else if (f == this.wrapS || f == this.wrapT || f == this.wrapR) {
    this.glimagevalid = false;
  }
  super.notify(l);
}

//
// Called from readInstance() or when user changes the
// filenames field. \e in is set if this function is called
// while reading a scene graph.
//
//FIXME: Recalc so all images have same w, h and nc (kintel 20011201)
//FIXME: Rescale depth to be n^2 ? This might not work very well though
//       if someone decides to add one layer at the time (kintel 20011201)
public boolean
loadFilenames() {
	return loadFilenames(null);
}
public boolean
loadFilenames(SoInput  in)
{
  boolean retval = false;
  final SbVec3s volumeSize = new SbVec3s((short)0,(short)0,(short)0);
  final int[] volumenc = new int[1];
  int numImages = this.filenames.getNum();
  boolean sizeError = false;
  int i;

  // Fail on empty filenames
  for (i=0;i<numImages;i++) if (this.filenames.operator_square_bracket(i).length()==0) break;

  if (i==numImages) { // All filenames valid
    for (int n=0 ; n<numImages && !sizeError ; n++) {
      String filename = this.filenames.operator_square_bracket(n);
      final SbImage tmpimage = new SbImage();
      final SbStringList sl = SoInput.getDirectories();
      if (tmpimage.readFile(filename, sl.getArrayPtr(), sl.getLength())) {
        final int[] nc = new int[1];
        final SbVec3s size = new SbVec3s();
        byte[] imgbytes = tmpimage.getValue(size, nc);
        if (size.getValue()[2]==0) size.getValue()[2]=1;
        if (this.images.isDefault()) { // First time => allocate memory
          volumeSize.setValue(size.getValue()[0],
                              size.getValue()[1],
                              (short)(size.getValue()[2]*numImages));
          volumenc[0] = nc[0];
          this.images.setValue(volumeSize, nc[0], null);
        }
        else { // Verify size & components
          if (size.getValue()[0] != volumeSize.getValue()[0] ||
              size.getValue()[1] != volumeSize.getValue()[1] ||
              //FIXME: always 1 or what? (kintel 20020110)
              size.getValue()[2] != (volumeSize.getValue()[2]/numImages) ||
              nc[0] != volumenc[0]) {
            sizeError = true;
            retval = false;

            String errstr =
            "Texture file #"+n+" ("+filename+") has wrong size:"+
                           "Expected ("+volumeSize.getValue()[0]+","+volumeSize.getValue()[1]+","+volumeSize.getValue()[2]+","+volumenc+
                           ") got ("+size.getValue()[0]+","+size.getValue()[1]+","+size.getValue()[2]+","+nc+")\n";
            if (in != null) SoReadError.post(in, errstr);
            else SoDebugError.postWarning("SoTexture3::loadFilenames()",
                                           errstr);
          }
        }
        if (!sizeError) {
          // disable notification on images while setting data from the
          // filenames as a notify will cause a filenames.setDefault(TRUE).
          boolean oldnotify = this.images.enableNotify(false);
          byte[] volbytes = this.images.startEditing(volumeSize,
                                                              volumenc);
          Util.memcpy(volbytes,(int)(size.getValue()[0])*(int)(size.getValue()[1])*(int)(size.getValue()[2])*nc[0]*n,
                 imgbytes, (int)(size.getValue()[0])*(int)(size.getValue()[1])*(int)(size.getValue()[2])*nc[0]);
          this.images.finishEditing();
          this.images.enableNotify(oldnotify);
          this.glimagevalid = false; // recreate GL images in next GLRender()
          retval = true;
        }
      }
      else {
        String errstr=
        "Could not read texture file #"+n+": "+ filename;
        if (in!= null) SoReadError.post(in, errstr);
        else SoDebugError.postWarning("SoTexture3::loadFilenames()",
                                       errstr);
        retval = false;
      }
    }
  }
  //FIXME: If sizeError, invalidate texture? (kintel 20011113)
  this.images.setDefault(true); // write filenames, not images
  return retval;
}

//
// called when \e filenames changes
//
public static void
filenameSensorCB(Object data, SoSensor sensor)
{
  SoTexture3 thisp = (SoTexture3 )data;

  thisp.setReadStatus(true? 1:0);
  if (thisp.filenames.getNum()<0 ||
      thisp.filenames.operator_square_bracket(0).length()!=0 &&
      !thisp.loadFilenames()) {
    thisp.setReadStatus(false ? 1:0);
  }
}

}
