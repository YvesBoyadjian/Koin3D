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
  \class SoShadowGroup SoShadowGroup.h FXViz/nodes/SoShadowGroup.h
  \brief The SoShadowGroup node is a group node used for shadow rendering.
  \ingroup fxviz

  Children of this node can recieve shadows, and cast shadows on other children.
  Use the SoShadowStyle node to control shadow casters and shadow receivers.

  Please note that all shadow casters will be rendered twice. Once to
  create the shadow map, and once for normal rendering. If you're
  having performance issues, you should consider reducing the number of
  shadow casters.

  The algorithm used to render the shadows is Variance Shadow Maps
  (http://www.punkuser.net/vsm/). As an extra bonus, all geometry
  rendered with shadows can also be rendered with per fragment phong
  lighting.

  This node will search its subgraph and calculate shadows for all
  SoSpotLight nodes. The node will use one texture unit for each spot
  light, so for this node to work 100%, you need to have
  num-spotlights free texture units while rendering the subgraph.

  Currently, we only support scenes with maximum two texture units
  active while doing shadow rendering (unit 0 and unit 1). This is due
  to the fact that we emulate the OpenGL shading model in a shader
  program, and we're still working on creating a solution that updates
  the shader program during the scene graph traversal. Right now a
  shader program is created when entering the SoShadowGroup node, and
  this is used for the entire subgraph.


  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    SoShadowGroup {
      isActive TRUE
      intensity 0.5
      precision 0.5
      quality 0.5
      shadowCachingEnabled TRUE
      visibilityRadius -1.0
      visibilityFlag LONGEST_BBOX_EDGE_FACTOR

      epsilon 0.00001
      threshold 0.1
      smoothBorder 0.0

    }
  \endcode

  Example scene graph:
  \code
  #Inventor V2.1 ascii

  # to get some lighting when headlight is turned off in the viewer
  DirectionalLight { direction 0 0 -1 intensity 0.2 }

  ShadowGroup {
    quality 1 # to get per pixel lighting

    ShadowStyle { style NO_SHADOWING }

    SpotLight {
      location -8 -8 8.0
      direction 1 1 -1
      cutOffAngle 0.35
      dropOffRate 0.7
    }

    ShadowStyle { style CASTS_SHADOW_AND_SHADOWED }

    Separator {
      Complexity { value 1.0 }
      Material { diffuseColor 1 1 0 specularColor 1 1 1 shininess 0.9 }
      Shuttle { translation0 -3 1 0 translation1 3 -5 0 speed 0.25 on TRUE }
      Translation { translation -5 0 2 }
      Sphere { radius 2.0 }
    }

    Separator {
      Material { diffuseColor 1 0 0 specularColor 1 1 1 shininess 0.9 }
      Shuttle { translation0 0 -5 0 translation1 0 5 0 speed 0.15 on TRUE }
      Translation { translation 0 0 -3 }
      Cube { depth 1.8 }
    }
    Separator {
      Material { diffuseColor 0 1 0 specularColor 1 1 1 shininess 0.9 }
      Shuttle { translation0 -5 0 0 translation1 5 0 0 speed 0.3 on TRUE }
      Translation { translation 0 0 -3 }
      Cube { }
    }

    ShadowStyle { style SHADOWED }
    Coordinate3 { point [ -10 -10 -3, 10 -10 -3, 10 10 -3, -10 10 -3 ] }
    Material { specularColor 1 1 1 shininess 0.9 }

    Complexity { textureQuality 0.1 }
    Texture2 { image 2 2 3 0xffffff 0x225588 0x225588 0xffffff }
    Texture2Transform { scaleFactor 4 4 }
    FaceSet { numVertices 4 }
  }

  \endcode

  \since Coin 2.5
*/


/*!
  \var SoSFBool SoShadowGroup::isActive

  Use this field to turn shadow rendering for the subgraph
  on/off. Default value is TRUE.
*/

/*!
  \var SoSFFloat SoShadowGroup::intensity

  Not used yet. Provided for TGS Inventor compatibility.
*/

/*!
  \var SoSFFloat SoShadowGroup::precision

  Use to calculate the size of the shadow map. A precision of 1.0
  means the maximum shadow buffer size will be used (typically
  2048x2048 on current graphics cards). Default value is 0.5.
*/

/*!
  \var SoSFFloat SoShadowGroup::quality

  Can be used to tune the shader program complexity. A higher value
  will mean that more calculations are done per-fragment instead of
  per-vertex. Default value is 0.5.

*/

/*!
  \var SoSFBool SoShadowGroup::shadowCachingEnabled

  Not used yet. Provided for TGS Inventor compatibility.
*/

/*!
  \var SoSFFloat SoShadowGroup::visibilityNearRadius

  Can be used to manually set the near clipping plane of the shadow
  maps.  If a negative value is provided, the group will calculate a
  near plane based on the bounding box of the children. Default value
  is -1.0.

  \sa visibilityFlag
*/

/*!
  \var SoSFFloat SoShadowGroup::visibilityRadius

  Can be used to manually set the far clipping plane of the shadow
  maps.  If a negative value is provided, the group will calculate a
  near plane based on the bounding box of the children. Default value
  is -1.0.

  \sa visibilityFlag
*/

/*!
  \var SoSFEnum SoShadowGroup::visibilityFlag

  Determines how visibilityRadius and visibilitNearRadius is used to
  calculate near and far clipping planes for the shadow volume.
*/

/*!
  SoShadowGroup::VisibilityFlag SoShadowGroup::ABSOLUTE_RADIUS

  The absolute values of visibilityNearRadius and visibilityRadius will be used.
*/

/*!
  SoShadowGroup::VisibilityFlag SoShadowGroup::LONGEST_BBOX_EDGE_FACTOR

  The longest bbox edge will be used to determine near and far clipping planes.

*/

/*!
  SoShadowGroup::VisibilityFlag SoShadowGroup::PROJECTED_BBOX_DEPTH_FACTOR

  The bbox depth (projected to face the camera) will be used to calculate the clipping planes.

*/

/*!
  \var SoSFInt32 SoShadowGroup::smoothBorder

  We have some problems with this feature so it's not supported at the
  moment.

  Used to add shadow border smoothing. This is currently done as a
  post processing step on the shadow map. The algorithm used is Gauss
  Smoothing, but in the future we'll probably change this, and use a
  summed area sampling merhod instead. The value should be a
  number between 0 (no smoothing), and 1 (max smoothing).

  If you want to enable smoothing, choosing a low value (~0.1) works
  best in the current implementation.

  Default value is 0.0.
*/

/*!
  \var SoSFFloat SoShadowGroup::epsilon

  Epsilon is used to offset the shadow map depth from the model depth.
  Should be set to as low a number as possible without causing
  flickering in the shadows or on non-shadowed objects. Default value
  is 0.00001.
*/

/*!
  \var SoSFFloat SoShadowGroup::threshold

  Can be used to avoid light bleeding in merged shadows cast from different objects.

  A threshold to completely eliminate all light bleeding can be
  computed from the ratio of overlapping occluder distances from the
  light's perspective. See
  http://forum.beyond3d.com/showthread.php?t=38165 for a discussion
  about this problem.

*/



package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.coin3d.fxviz.elements.SoShadowStyleElement;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author BOYADJIAN
 *
 */
public class SoShadowGroup extends SoSeparator {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShadowGroup.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShadowGroup.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShadowGroup.class); }    
	  
	  public enum VisibilityFlag {
		    ABSOLUTE_RADIUS(0),
		    LONGEST_BBOX_EDGE_FACTOR(0),
		    PROJECTED_BBOX_DEPTH_FACTOR(0);
		  
		  private int value;
		  
		  VisibilityFlag(int value) {
			  this.value = value;
		  }
		  public int getValue() {
			  return value;
		  }
		  };	  
	  
	  public final SoSFBool isActive = new SoSFBool();
	  public final SoSFFloat intensity = new SoSFFloat();
	  public final SoSFFloat precision = new SoSFFloat();
	  public final SoSFFloat quality = new SoSFFloat();
	  public final SoSFFloat smoothBorder = new SoSFFloat();
	  public final SoSFBool shadowCachingEnabled = new SoSFBool();
	  public final SoSFFloat visibilityNearRadius = new SoSFFloat();
	  public final SoSFFloat visibilityRadius = new SoSFFloat();
	  public final SoSFEnum visibilityFlag = new SoSFEnum();
	  
	  public final SoSFFloat epsilon = new SoSFFloat();
	  public final SoSFFloat threshold = new SoSFFloat();

	  
	  
	  private SoShadowGroupP pimpl;
	  
	  private SoShadowGroupP PRIVATE(SoShadowGroup obj) {return (obj).pimpl;}
	  
	  public static void init()
	  {
	    SoShadowGroup.initClass();
	    SoShadowStyleElement.initClass(SoShadowStyleElement.class);
	    //SoGLShadowCullingElement.initClass();
	    SoShadowStyle.initClass();
	    //SoShadowSpotLight.initClass();
	    //SoShadowDirectionalLight.initClass();
	    //SoShadowCulling.initClass();
	  }

	  /*!
	  Default constructor.
	*/
	public SoShadowGroup()
	{
	  pimpl = new SoShadowGroupP(this);

	  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShadowGroup.class);

	  nodeHeader.SO_NODE_ADD_FIELD(isActive,"isActive", (true));
	  nodeHeader.SO_NODE_ADD_FIELD(intensity,"intensity", (0.5f));
	  nodeHeader.SO_NODE_ADD_FIELD(precision,"precision", (0.5f));
	  nodeHeader.SO_NODE_ADD_FIELD(quality,"quality", (0.5f));
	  nodeHeader.SO_NODE_ADD_FIELD(shadowCachingEnabled,"shadowCachingEnabled", (true));
	  nodeHeader.SO_NODE_ADD_FIELD(visibilityNearRadius,"visibilityNearRadius", (-1.0f));
	  nodeHeader.SO_NODE_ADD_FIELD(visibilityRadius,"visibilityRadius", (-1.0f));
	  nodeHeader.SO_NODE_ADD_FIELD(epsilon,"epsilon", (0.00001f));
	  nodeHeader.SO_NODE_ADD_FIELD(threshold,"threshold", (0.1f));
	  nodeHeader.SO_NODE_ADD_FIELD(smoothBorder,"smoothBorder", (0.0f));

	  nodeHeader.SO_NODE_ADD_FIELD(visibilityFlag,"visibilityFlag",VisibilityFlag.LONGEST_BBOX_EDGE_FACTOR.getValue());

	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VisibilityFlag.LONGEST_BBOX_EDGE_FACTOR);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VisibilityFlag.ABSOLUTE_RADIUS);
	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VisibilityFlag.PROJECTED_BBOX_DEPTH_FACTOR);
	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(visibilityFlag,"visibilityFlag", "VisibilityFlag");

	}

	  
	
	   public static void
	     initClass()
	     //
	     ////////////////////////////////////////////////////////////////////////
	     {
	        SoSubNode.SO__NODE_INIT_CLASS(SoShadowGroup.class, "ShadowGroup", SoSeparator.class);
	     
	     }	
}
