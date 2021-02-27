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
 |      Upgrade from V2.0 SoText node to V2.1.  Necessary because the
 |      text now is UTF-8.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.misc.upgraders;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.nodes.SoText3;

/**
 * @author Yves Boyadjian
 *
 */
public class SoV2Text3 extends SoUpgrader {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoV2Text3.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoV2Text3.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoV2Text3.class); }    	  	
	
  public
    //! Justification types
    enum Justification {
        LEFT    ( 0x01),
        RIGHT   ( 0x02),
        CENTER  ( 0x03);
        
        private int value;
        Justification(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

    //! Justification types
    public enum Part {
        FRONT   ( 0x01),
        SIDES   ( 0x02),
        BACK    ( 0x04),
        ALL     ( 0x07);
        
        private int value;
        Part(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

    //! \name Fields
    //@{
    //! the strings to display
    public final SoMFString          string = new SoMFString();         
    //! interval between strings
    public final SoSFFloat           spacing = new SoSFFloat();        
    //! Visible parts of text
    public final SoSFBitMask         parts = new SoSFBitMask();          
    public final SoSFEnum            justification = new SoSFEnum();


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Default constructor
//
// Use: public

public SoV2Text3()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoV2Text3.class*/);

    nodeHeader.SO_NODE_ADD_MFIELD(string,"string",   (""));
    nodeHeader.SO_NODE_ADD_FIELD(spacing,"spacing",  (1.0f));
    nodeHeader.SO_NODE_ADD_FIELD(justification,"justification",    (Justification.LEFT.getValue()));
    nodeHeader.SO_NODE_ADD_FIELD(parts,"parts",            (Part.FRONT.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.LEFT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.RIGHT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.CENTER);

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.SIDES);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.FRONT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.BACK);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.ALL);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(justification,"justification", "Justification");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(parts,"parts", "Part");
    
    isBuiltIn = true;
}

	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create a version 2.1 SoText3.
//
// Use: private

public SoNode createNewNode()
//
////////////////////////////////////////////////////////////////////////
{
    SoText3 result = (SoText3)SO_UPGRADER_CREATE_NEW(SoText3.class);

    // If the parts field is default, change it to ALL (since the
    // default changed from ALL to FRONT from 1.0 to 2.0).
    if (parts.isDefault()) {
        result.parts.setValue(SoText3.Part.ALL);
    } else {
        result.parts.setValue(parts.getValue());
    }

    // if european characters present they are converted to UTF-8
    for (int i = 0; i < string.getNum(); i++) {
        final String[] str = new String[1];
        if (SoV2Text2.convertToUTF8(string.operator_square_bracket(i), str)) {
            result.string.set1Value(i, str[0]);   
        }
    }

    result.spacing.setValue(spacing.getValue());
    result.justification.setValue(justification.getValue());

    return result;
}
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoV2Text3 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO_UPGRADER_INIT_CLASS(SoV2Text3.class);
    SO_REGISTER_UPGRADER("Text3", 2.0f,getClassTypeId());
}
}
