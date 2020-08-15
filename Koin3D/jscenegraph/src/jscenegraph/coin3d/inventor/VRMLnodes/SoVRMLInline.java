/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLInline extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLInline.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLInline.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLInline.class); }    	  	

	  enum BboxVisibility {
	    NEVER,
	    UNTIL_LOADED,
	    ALWAYS
	  };

	  public final SoSFVec3f bboxCenter = new SoSFVec3f();
	  public final SoSFVec3f bboxSize = new SoSFVec3f();
	  public final SoMFString url = new SoMFString();


/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLInline, SO_VRML97_NODE_TYPE);
	SoSubNode.SO__NODE_INIT_CLASS(SoVRMLInline.class,"VRMLInline",SoNode.class);
//  sovrmlinline_bboxcolor = new SbColor(0.8f, 0.8f, 0.8f); TODO
//  coin_atexit((coin_atexit_f*) sovrmlinline_cleanup, CC_ATEXIT_NORMAL);
//  SoAudioRenderAction::addMethod(SoVRMLInline::getClassTypeId(),
//                                 SoAudioRenderAction::callDoAction);
}

/*!
  Constructor
*/
public SoVRMLInline()
{
//  pimpl = new SoVRMLInlineP(); TODO
//  pimpl.isrequested = false;
//  pimpl.children = new SoChildList(this);

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLInline.class);

  nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxCenter,"bboxCenter", new SbVec3f(0.0f, 0.0f, 0.0f));
  nodeHeader.SO_VRMLNODE_ADD_FIELD(bboxSize,"bboxSize", new SbVec3f(-1.0f, -1.0f, -1.0f));
  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(url,"url");

//  pimpl.urlsensor = new SoFieldSensor(SoVRMLInline::urlFieldModified, this); TODO
//  pimpl.urlsensor.setPriority(0); // immediate sensor
//  pimpl.urlsensor.attach(this.url);
}

}
