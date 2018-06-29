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
 |      Defines the SoSeparatorKit class. 
 |      Organizes an appearance, a transform, a local transform and a 
 |      group of child kits
 |
 |   Author(s)          : Paul Isaacs, Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
*/

package jscenegraph.nodekits.inventor.nodekits;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPickStyle;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoTexture2Transform;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.database.inventor.nodes.SoUnits;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Separator nodekit class.
/*!
\class SoSeparatorKit
\ingroup Nodekits
A nodekit that is used for creating nodekit hierarchies.
SoSeparatorKit contains a <em>transform</em> part, a <em>childList</em> part,
and a few others in its catalog.
The <em>transform</em> part (an SoTransform node) affects all of the
children in the <tt>childList</tt>. Each of these children must be an
SoSeparatorKit or from a class that is derived from SoSeparatorKit
(e.g., SoShapeKit and SoWrapperKit).
Since all members of the <em>childList</em> are in turn SoSeparatorKits,
and each contains a <em>transform</em>, these nested lists allow you to create
a hierarchy of motion, in which each <em>transform</em> affects an entire subgraph
of nodekits.


The other parts added to the catalog for the SoSeparatorKit are
<em>pickStyle</em>, <em>appearance</em>, <em>units</em> and <em>texture2Transform</em>.  
Furthermore,
since SoSeparator is derived from SoBaseKit, it inherits
the <em>callbackList</em> part. This is a list of SoCallback and/or
SoEventCallback nodes which enable the SoSeparatorKit to perform
special callbacks whenever an action is applied to it. 


By creating the <em>pickStyle</em> part, a user can alter the pick style
for the entire nodekit hierarchy. The <em>appearance</em> part is an
SoAppearanceKit nodekit. Note that all parts contained in the
SoAppearanceKit catalog can be accessed 
as if they were part of the SoSeparatorKit. For example:
\code
myMtl = mySepKit.getPart("material",true)
\endcode
and 
\code
mySepKit.setPart("material",myMtl)
\endcode
See SoBaseKit for further explanation.

\par Parts
\par
\b pickStyle
<BR> An SoPickStyle property node that can be used to set the     picking style of its children. This part is <tt>NULL</tt> by default, 	but is created automatically if necessary. 
\par
\b appearance
<BR> An SoAppearanceKit nodekit which can be used 	to set the appearance properties of its children. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b units
<BR> An SoUnits node which can be used 	to set the types of units, (e.g., feet), of its children. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b transform
<BR> An SoTransform node which can be used 	to set the overall position, orientation, and scale 	of its children. This part is <tt>NULL</tt> by default, 	but is created automatically if necessary. 
\par
\b texture2Transform
<BR> An SoTexture2Transform node which can be used 	to apply a transformation to any textures used by its children. 	This part is <tt>NULL</tt> by default, but is created automatically 	if necessary. 
\par
\b childList
<BR> This part contains the children nodekits of this 	SoSeparatorKit.  This part is a <em>list part</em> and can 	have multiple children. This part is <tt>NULL</tt> by default, 	but is created automatically when the first child is added  	to the <em>childList</em>.  	Also, when asked to build a member of the <em>childList</em>,  	the separatorKit 	will build an SoShapeKit by default. 	So if the <em>childList</em> part is NULL, and you call: 	getPart("childList[0]", true), 	the separator kit will create the <em>childList</em> and add  	an SoShapeKit as the new element in the list. 	

\par See Also
\par
SoAppearanceKit, SoBaseKit, SoCameraKit, SoLightKit, SoNodeKit, SoNodeKitDetail, SoNodeKitListPart, SoNodeKitPath, SoNodekitCatalog, SoSceneKit, SoShapeKit, SoWrapperKit
*/
////////////////////////////////////////////////////////////////////////////////

public class SoSeparatorKit extends SoBaseKit {

	private final SoSubKit kitHeader = SoSubKit.SO_KIT_HEADER(SoSeparatorKit.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoSeparatorKit.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return kitHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return kitHeader == null ? super.getFieldData() : kitHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoSeparatorKit.class); }              

	
    /* Returns an SoNodekitCatalog for the node */                            
    public SoNodekitCatalog getNodekitCatalog() {
    	if(kitHeader == null) {
    		return super.getNodekitCatalog();
    	}
    	return kitHeader.getNodekitCatalog();
    }

    //! defines fields for the new parts in the catalog
    protected final SoSFNode topSeparator = new SoSFNode();
    protected final SoSFNode pickStyle = new SoSFNode();
    protected final SoSFNode appearance = new SoSFNode();
    protected final SoSFNode units = new SoSFNode();
    protected final SoSFNode transform = new SoSFNode();
    protected final SoSFNode texture2Transform = new SoSFNode();
    protected final SoSFNode childList = new SoSFNode();
    
    //! Possible values for caching
    public enum CacheEnabled {
        OFF,                    //!< Never build or use a cache
        ON,                     //!< Always try to build a cache
        AUTO;                    //!< Decide based on some heuristic
    	
    	public int getValue() {
    		return ordinal();
    	}
    };

    //! \name Fields
    //@{

    //! Set render caching mode.  Default is <tt>AUTO</tt>.
    public final SoSFEnum renderCaching = new SoSFEnum();     

    //! Set bounding box caching mode.
    //! 	Default is <tt>ON</tt>.  Setting this value to <tt>AUTO</tt> is equivalent
    //! 	to <tt>ON</tt> (automatic culling is not implemented.)
    public final SoSFEnum boundingBoxCaching = new SoSFEnum();

    //! Set render culling mode.  Default is <tt>OFF</tt>.
    //! 	Setting this value to <tt>AUTO</tt> is equivalent to <tt>ON</tt>
    //!     (automatic culling is not implemented.)
    public final SoSFEnum renderCulling = new SoSFEnum();     

    //! Set pick culling mode.  Default is <tt>AUTO</tt>.
    public final SoSFEnum pickCulling = new SoSFEnum();       

    //@}
    
    //! This sensor will watch the topSeparator part.  If the part changes to a 
    //! new node,  then the fields of the old part will be disconnected and
    //! the fields of the new part will be connected.
    //! Connections are made from/to the renderCaching, boundingBoxCaching,
    //! renderCulling and pickCulling fields. This way, the SoSeparatorKit
    //! can be treated from the outside just like a regular SoSeparator node.
    //! Setting the fields will affect caching and culling, even though the
    //! topSeparator takes care of it.
    //! oldTopSep keeps track of the part for comparison.
    protected SoFieldSensor fieldSensor;
    
    protected SoSeparator oldTopSep;
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSeparatorKit()
//
////////////////////////////////////////////////////////////////////////
{
    kitHeader.SO_KIT_CONSTRUCTOR(SoSeparatorKit.class);

    isBuiltIn = true;

    // Initialize children catalog and add entries to it
    // These are the macros you use to make a catalog.
    // Use combinations of ...ADD_CATALOG_ENTRY 
    // and ...ADD_CATALOG_LIST_ENTRY.  See SoSubKit.h for more info
    // on syntax of these macros.

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(topSeparator,"topSeparator", SoSeparator.class, true,
                                    "this","", false );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(pickStyle,"pickStyle", SoPickStyle.class, true,
                                       "topSeparator","", true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(appearance,"appearance",     SoAppearanceKit.class, true,
                                    "topSeparator","", true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(units,"units",      SoUnits.class, true,
                                    "topSeparator","", true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(transform,"transform",      SoTransform.class, true,
                                    "topSeparator","", true );

    kitHeader.SO_KIT_ADD_CATALOG_ENTRY(texture2Transform,"texture2Transform", SoTexture2Transform.class, true,
                                    "topSeparator","", true );

    kitHeader.SO_KIT_ADD_CATALOG_LIST_ENTRY(childList,"childList", SoSeparator.class, true,
                                       "topSeparator","", SoShapeKit.class, true );
    kitHeader.SO_KIT_ADD_LIST_ITEM_TYPE(childList,"childList", SoSeparatorKit.class );

    kitHeader.SO_KIT_ADD_FIELD(renderCaching,"renderCaching",            (CacheEnabled.AUTO.getValue()));
    kitHeader.SO_KIT_ADD_FIELD(boundingBoxCaching,"boundingBoxCaching",       (CacheEnabled.AUTO.getValue()));
    kitHeader.SO_KIT_ADD_FIELD(renderCulling,"renderCulling",            (CacheEnabled.AUTO.getValue()));
    kitHeader.SO_KIT_ADD_FIELD(pickCulling,"pickCulling",              (CacheEnabled.AUTO.getValue()));

    // Set up static info for enum fields
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.OFF);
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.ON);
    kitHeader.SO_KIT_DEFINE_ENUM_VALUE(CacheEnabled.AUTO);

    // Set up info in enumerated type fields
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(renderCaching,"renderCaching",     "CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(boundingBoxCaching,"boundingBoxCaching", "CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(renderCulling,"renderCulling",     "CacheEnabled");
    kitHeader.SO_KIT_SET_SF_ENUM_TYPE(pickCulling,"pickCulling",       "CacheEnabled");

    SO_KIT_INIT_INSTANCE();

    // For Inventor 2.0, we disabled notification on the part fields because
    // it made things real slow.
    // For Inventor 2.1, the notification scheme was improved to the point
    // where this was not necessary. But caution (and the fact that I only
    // found out about this late) dictate that we not put notification back
    // for all part fields.
    // However, but #274396 reports a problem that arose from disabling 
    // notification on the topSeparator field, because the fieldSensor 
    // never gets notified and so connections are never built between the
    // topSeparator's fields and the cache/cull fields of the SeparatorKit.
    // So we enable notification on the field right here to fix this.
    // Note that we must do this AFTER SO_KIT_INIT_INSTANCE, for that is
    // where the field is DISabled in the first place, within the construction
    // of the partsList.
    topSeparator.enableNotify(true);

    // This sensor will watch the topSeparator part.  If the part changes to a 
    // new node,  then the fields of the old part will be disconnected and
    // the fields of the new part will be connected.
    // Connections are made from/to the renderCaching, boundingBoxCaching,
    // renderCulling and pickCulling fields. This way, the SoSeparatorKit
    // can be treated from the outside just like a regular SoSeparator node.
    // Setting the fields will affect caching and culling, even though the
    // topSeparator takes care of it.
    // oldTopSep keeps track of the part for comparison.
    fieldSensor = new SoFieldSensor( SoSeparatorKit::fieldSensorCB, this );
    fieldSensor.setPriority(0);
    oldTopSep = null;
    setUpConnections( true, true );
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (oldTopSep != null) {
        oldTopSep.unref();
        oldTopSep = null;
    }
    if (fieldSensor != null)
        fieldSensor.destructor();
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    overrides method in SoNode to return false.
//
// Use: public
//
public boolean
affectsState()
{
    return false;
}


//    detach/attach any sensors, callbacks, and/or field connections.
//    Called by:            start/end of SoBaseKit::readInstance
//    and on new copy by:   start/end of SoBaseKit::copy.
//    Classes that redefine must call setUpConnections(true,true) 
//    at end of constructor.
//    Returns the state of the node when this was called.
public boolean
setUpConnections( boolean onOff, boolean doItAlways )
{
	if(kitHeader == null) { //java port
		return super.setUpConnections(onOff, doItAlways);
	}
	
    if ( !doItAlways && connectionsSetUp == onOff)
        return onOff;

    if ( onOff ) {

        // We connect AFTER base class.
        super.setUpConnections( onOff, false );

        // Hookup the field-to-field connections on topSeparator.
        connectSeparatorFields( oldTopSep, true );

        // Call sensor CBs to make sure oldTopSep is up-to-date
        fieldSensorCB( this, null );

        // Connect the field sensors
        if (fieldSensor.getAttachedField() != topSeparator)
            fieldSensor.attach( topSeparator );
    }
    else {

        // We disconnect BEFORE base class.

        // Disconnect the field sensors.
        if (fieldSensor.getAttachedField() != null)
            fieldSensor.detach();

        // Undo the field-to-field connections on topSeparator.
        connectSeparatorFields( oldTopSep, false );

        super.setUpConnections( onOff, false );
    }

    return !(connectionsSetUp = onOff);
}

public void
connectSeparatorFields( SoSeparator dest, boolean onOff )
{
    if (dest == null)
        return;
    if (onOff) {
        final SoField[] f = new SoField[1];
        if ( ! dest.renderCaching.getConnectedField(f) ||
               f[0] != renderCaching )
            dest.renderCaching.connectFrom( renderCaching );
        if ( ! dest.boundingBoxCaching.getConnectedField(f) ||
               f[0] != boundingBoxCaching )
            dest.boundingBoxCaching.connectFrom( boundingBoxCaching );
        if ( ! dest.renderCulling.getConnectedField(f) ||
               f[0] != renderCulling )
            dest.renderCulling.connectFrom( renderCulling );
        if ( ! dest.pickCulling.getConnectedField(f) ||
               f[0] != pickCulling )
            dest.pickCulling.connectFrom( pickCulling );
    }
    else {
        dest.renderCaching.disconnect();
        dest.boundingBoxCaching.disconnect();
        dest.renderCulling.disconnect();
        dest.pickCulling.disconnect();
    }
}

public static void
fieldSensorCB( Object inKit, SoSensor sensor)
{
    SoSeparatorKit k  = (SoSeparatorKit ) inKit;
    if ( k.oldTopSep == k.topSeparator.getValue())
        return;

    k.connectSeparatorFields( k.oldTopSep, false );

    SoNode newTopSep = k.topSeparator.getValue();
    if (newTopSep != null)
        newTopSep.ref();

    if (k.oldTopSep != null)
        k.oldTopSep.unref();

    k.oldTopSep = (SoSeparator ) newTopSep;
    k.connectSeparatorFields( k.oldTopSep, true );
}

/////////////////////////////////////////////////////////////////////////
//
// Called by the SoBaseKit::write() method. Calls setDefault(true)
// on the topSeparator. Note that this may be overriden later by basekit
// if, for example, topSeparator lies on a path that is being written out.
//
/////////////////////////////////////////////////////////////////////////
public void
setDefaultOnNonWritingFields()
{
    topSeparator.setDefault(true);

    // Call the base class...
    super.setDefaultOnNonWritingFields();
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoSeparatorKit class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__KIT_INIT_CLASS(SoSeparatorKit.class, "SeparatorKit", SoBaseKit.class);
}

}
